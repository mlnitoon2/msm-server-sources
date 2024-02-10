package com.bigbluebubble.BBBServer.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import com.amazonaws.services.sns.model.InvalidParameterException;
import com.amazonaws.util.EC2MetadataUtils;
import com.bigbluebubble.BBBServer.BBBServerExtension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class CloudwatchMetrics {
   private static final int MAX_TRIES_BEFORE_BACKOFF = 3;
   private static final int STARTING_BACKOFF_TIME = 1000;
   private static final int FAILURE_EXPIRY_TIME = 30000;
   private static final int MAX_TRIES_BEFORE_DUMP = 15;
   private static final int MAX_QUEUE_SIZE_BEFORE_DUMP = 500000;
   private static final int MAX_QUEUE_SIZE_TO_SPAWN_NEW_WORKER = 3000;
   private static final int MIN_QUEUE_SIZE_TO_STOP_WORKER = 100;
   private static final int MIN_TIME_TO_WAIT_BETWEEN_WORKER_SPAWNS = 10000;
   protected final AmazonCloudWatch cw;
   protected final Dimension[] dimensions;
   protected final String baseNamespace;
   private final BlockingQueue<PutMetricDataRequest> queue = new LinkedBlockingQueue();
   private final AtomicInteger queueSize = new AtomicInteger(0);
   private final int maxThreads;
   private CooldownCounter failureCounter = new CooldownCounter(30000L);
   protected int backoffTime = 0;
   protected boolean shouldBackoff = false;
   protected long lastSendTime = System.currentTimeMillis();
   private List<Thread> workerThreads = new ArrayList();
   private CooldownCounter workerSpawnCounter = new CooldownCounter(10000L);
   private Thread heartbeatThread;
   private CopyOnWriteArrayList<CloudwatchMetricAdder> metricAdders = new CopyOnWriteArrayList();

   public boolean addMetricAdder(CloudwatchMetricAdder adder) {
      if (this.metricAdders.contains(adder)) {
         return false;
      } else {
         this.metricAdders.add(adder);
         adder.add(this);
         return true;
      }
   }

   public boolean removeMetricAdder(CloudwatchMetricAdder adder) {
      return this.metricAdders.remove(adder);
   }

   public CloudwatchMetrics(BBBServerExtension ext, String baseNamespace, int maxThreads, final long defaultHeartbeat, String fallbackRegion, String... metricDimensions) {
      this.maxThreads = maxThreads;
      this.baseNamespace = baseNamespace;
      AmazonCloudWatch cwInstance = null;
      ArrayList dimensionsInstance = new ArrayList();

      try {
         cwInstance = (AmazonCloudWatch)((AmazonCloudWatchClientBuilder)AmazonCloudWatchClient.builder().withCredentials(InstanceProfileCredentialsProvider.getInstance())).build();
         dimensionsInstance.add((new Dimension()).withName("Instance").withValue(EC2MetadataUtils.getInstanceId()));
         if (metricDimensions != null && metricDimensions.length % 2 == 0) {
            for(int i = 0; i < metricDimensions.length; i += 2) {
               dimensionsInstance.add((new Dimension()).withName(metricDimensions[i]).withValue(metricDimensions[i + 1]));
            }
         }
      } catch (SdkClientException var12) {
      }

      this.cw = cwInstance;
      this.dimensions = (Dimension[])dimensionsInstance.toArray(new Dimension[0]);
      Runnable heartbeat = new Runnable() {
         public void run() {
            while(true) {
               try {
                  Iterator var1 = CloudwatchMetrics.this.metricAdders.iterator();

                  while(var1.hasNext()) {
                     CloudwatchMetricAdder adder = (CloudwatchMetricAdder)var1.next();
                     adder.add(CloudwatchMetrics.this);
                  }

                  Thread.sleep(defaultHeartbeat);
               } catch (InterruptedException var3) {
                  SimpleLogger.trace((Exception)var3);
                  throw new RuntimeException(var3);
               }
            }
         }
      };
      this.heartbeatThread = new Thread(heartbeat);
      this.heartbeatThread.start();
      this.addNewWorkerToPool();
   }

   public void addMetric(String name, StandardUnit unit, double data_point) {
      this.addMetric(name, "", unit, data_point);
   }

   public void addMetric(String name, String namespaceSuffix, StandardUnit unit, double data_point) {
      MetricDatum datum = (new MetricDatum()).withMetricName(name).withUnit(unit).withValue(data_point).withDimensions(this.dimensions);
      PutMetricDataRequest request = (new PutMetricDataRequest()).withNamespace(this.baseNamespace + namespaceSuffix).withMetricData(new MetricDatum[]{datum});
      this.queue.add(request);
   }

   public void addMetric(PutMetricDataRequest request) {
      this.queue.add(request);
   }

   public void shutdown() {
   }

   protected Runnable getWorker() {
      return new Runnable() {
         public void run() {
            while(!Thread.interrupted()) {
               PutMetricDataRequest message = null;

               try {
                  message = CloudwatchMetrics.this.takeMessage();
                  CloudwatchMetrics.this.monitorThreads();
               } catch (InterruptedException var3) {
                  return;
               }

               if (!CloudwatchMetrics.this.sendMessage(message)) {
                  CloudwatchMetrics.this.addMetric(message);
               }
            }

         }
      };
   }

   protected void dealWithRemainingMessagesAtShutdown() {
      ArrayList<PutMetricDataRequest> remainingMessages = new ArrayList();
      this.queue.drainTo(remainingMessages);
      Iterator var2 = remainingMessages.iterator();

      while(var2.hasNext()) {
         PutMetricDataRequest message = (PutMetricDataRequest)var2.next();
         if (!this.sendMessage(message)) {
            this.queue.add(message);
         }
      }

   }

   protected final PutMetricDataRequest takeMessage() throws InterruptedException {
      PutMetricDataRequest message = (PutMetricDataRequest)this.queue.take();
      this.queueSize.decrementAndGet();
      return message;
   }

   protected final synchronized void monitorThreads() {
      int currentQueueSize = this.queueSize.get();
      if ((!this.failureCounter.isOnCooldown() || this.failureCounter.count() <= 15) && currentQueueSize <= 500000) {
         if (this.workerThreads.size() < this.maxThreads && currentQueueSize > 3000 && !this.workerSpawnCounter.isOnCooldown()) {
            this.addNewWorkerToPool();
         } else if (currentQueueSize < 100 && this.workerThreads.size() > 1) {
            SimpleLogger.trace("Removing Cloudwatch Worker");
            ((Thread)this.workerThreads.remove(this.workerThreads.size() - 1)).interrupt();
            this.workerSpawnCounter.decrement();
         }
      } else {
         ArrayList<PutMetricDataRequest> messages = new ArrayList();
         this.queue.drainTo(messages);
         this.failureCounter.reset();
      }

   }

   protected final boolean sendMessage(PutMetricDataRequest request) {
      boolean successfull = false;

      try {
         this.doBackoff();
         this.cw.putMetricData(request);
         successfull = true;
         this.resetBackoff();
      } catch (InvalidParameterException var4) {
         SimpleLogger.trace((Exception)var4, "Exception: Failed to send message: " + request.getMetricData());
         successfull = true;
      } catch (AmazonServiceException var5) {
         this.handleException(var5);
      } catch (InterruptedException var6) {
         Thread.currentThread().interrupt();
      } catch (Exception var7) {
         this.handleException(var7);
      }

      if (!successfull) {
         SimpleLogger.trace("Failed to send Cloudwatch metric: " + request.getMetricData());
      }

      this.updateLastSendTime();
      return successfull;
   }

   private void addNewWorkerToPool() {
      SimpleLogger.trace("Adding Cloudwatch Worker");
      Thread worker = new Thread(this.getWorker());
      worker.start();
      this.workerThreads.add(worker);
      this.workerSpawnCounter.increment();
   }

   private void resetBackoff() {
      this.failureCounter.reset();
      this.backoffTime = 0;
   }

   private void increaseBackoff() {
      this.failureCounter.increment();
      if (this.failureCounter.count() > 3) {
         this.backoffTime = Math.max(1000, this.backoffTime * 2);
         this.shouldBackoff = true;
      }

   }

   private void doBackoff() throws InterruptedException {
      if (this.shouldBackoff) {
         Thread.sleep((long)this.backoffTime);
      }

   }

   private void handleException(Exception e) {
      SimpleLogger.trace(e, "Error sending Cloudwatch metric");
      this.increaseBackoff();
   }

   private void updateLastSendTime() {
      this.lastSendTime = System.currentTimeMillis();
   }

   protected long getLastSendTime() {
      return this.lastSendTime;
   }
}
