package com.bigbluebubble.BBBServer.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.InvalidParameterException;
import com.amazonaws.services.sns.model.PublishRequest;
import com.bigbluebubble.BBBServer.BBBServerExtension;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import net.spy.memcached.util.StringUtils;

public class SNSWorker {
   protected static final int MAX_SNS_MESSAGE_SIZE = 262144;
   private static final int CONNECTION_TIMEOUT = 1000;
   private static final int MAX_TRIES_BEFORE_BACKOFF = 3;
   private static final int STARTING_BACKOFF_TIME = 1000;
   private static final int FAILURE_EXPIRY_TIME = 30000;
   private static final int MAX_TRIES_BEFORE_DUMP = 15;
   private static final int MAX_QUEUE_SIZE_BEFORE_DUMP = 500000;
   private static final int MAX_QUEUE_SIZE_TO_SPAWN_NEW_WORKER = 3000;
   private static final int MIN_QUEUE_SIZE_TO_STOP_WORKER = 100;
   private static final int MIN_TIME_TO_WAIT_BETWEEN_WORKER_SPAWNS = 10000;
   public static boolean isAmazon = false;
   private final String FailedMessagesS3Bucket;
   private final String FailedMessageS3Prefix;
   protected final AmazonSNSClient sns;
   private final BlockingQueue<String> queue = new LinkedBlockingQueue();
   private final AtomicInteger queueSize = new AtomicInteger(0);
   private final AtomicBoolean emergencyExit = new AtomicBoolean(false);
   protected String snsTopic;
   private int maxThreads;
   private CooldownCounter failureCounter = new CooldownCounter(30000L);
   protected int backoffTime = 0;
   protected boolean shouldBackoff = false;
   protected long lastSendTime = System.currentTimeMillis();
   private List<Thread> workerThreads = new ArrayList();
   private CooldownCounter workerSpawnCounter = new CooldownCounter(10000L);
   protected final BBBServerExtension ext;

   public SNSWorker(BBBServerExtension ext, Region region, String topic, String bucket, String prefix, int maxThreads, BasicAWSCredentials credentials) {
      this.ext = ext;
      this.snsTopic = topic;
      this.maxThreads = maxThreads;
      this.FailedMessagesS3Bucket = bucket;
      this.FailedMessageS3Prefix = prefix;
      this.sns = new AmazonSNSClient(credentials, (new ClientConfiguration()).withConnectionTimeout(1000));
      this.sns.setRegion(region);
      this.addNewWorkerToPool();
   }

   public void addMessage(String message) {
      this.queueSize.incrementAndGet();
      this.queue.add(message);
   }

   public void addMessages(Collection<String> messages) {
      this.queueSize.addAndGet(messages.size());
      this.queue.addAll(messages);
   }

   public void shutdown() {
      if (this.emergencyExit.compareAndSet(false, true) && isAmazon) {
         try {
            Iterator var1 = this.workerThreads.iterator();

            while(var1.hasNext()) {
               Thread t = (Thread)var1.next();
               t.interrupt();
               t.join();
            }
         } catch (InterruptedException var3) {
            System.err.println("Interrupted while joining with MetricsProcessor worker thread!!!");
         }

         this.dealWithRemainingMessagesAtShutdown();
         if (!this.queue.isEmpty()) {
            ArrayList<String> failedMessages = new ArrayList();
            this.queue.drainTo(failedMessages);
            this.dumpToS3(failedMessages);
         }
      }

   }

   protected Runnable getWorker() {
      return new Runnable() {
         public void run() {
            while(!Thread.interrupted()) {
               String message = null;

               try {
                  message = SNSWorker.this.takeMessage();
                  SNSWorker.this.monitorThreads();
               } catch (InterruptedException var3) {
                  return;
               }

               if (!SNSWorker.this.sendMessage(message)) {
                  SNSWorker.this.addMessage(message);
               }
            }

         }
      };
   }

   protected void dealWithRemainingMessagesAtShutdown() {
      ArrayList<String> remainingMessages = new ArrayList();
      this.queue.drainTo(remainingMessages);
      Iterator var2 = remainingMessages.iterator();

      while(var2.hasNext()) {
         String message = (String)var2.next();
         if (!this.sendMessage(message)) {
            this.queue.add(message);
         }
      }

   }

   protected final String takeMessage() throws InterruptedException {
      String message = (String)this.queue.take();
      this.queueSize.decrementAndGet();
      return message;
   }

   protected final Collection<String> drainQueue() {
      ArrayList<String> messages = new ArrayList();
      this.queue.drainTo(messages);
      this.queueSize.addAndGet(-messages.size());
      return messages;
   }

   protected final synchronized void monitorThreads() {
      int currentQueueSize = this.queueSize.get();
      if ((!this.failureCounter.isOnCooldown() || this.failureCounter.count() <= 15) && currentQueueSize <= 500000) {
         if (this.workerThreads.size() < this.maxThreads && currentQueueSize > 3000 && !this.workerSpawnCounter.isOnCooldown()) {
            this.addNewWorkerToPool();
         } else if (currentQueueSize < 100 && this.workerThreads.size() > 1) {
            SimpleLogger.trace("Removing SNS Worker for " + this.snsTopic);
            ((Thread)this.workerThreads.remove(this.workerThreads.size() - 1)).interrupt();
            this.workerSpawnCounter.decrement();
         }
      } else {
         ArrayList<String> messages = new ArrayList();
         this.queue.drainTo(messages);
         this.dumpToS3(messages);
         this.failureCounter.reset();
      }

   }

   protected final boolean sendMessage(String message) {
      boolean successfull = false;

      try {
         this.doBackoff();
         this.sns.publish((new PublishRequest()).withTopicArn(this.snsTopic).withMessage(message));
         successfull = true;
         this.resetBackoff();
      } catch (InvalidParameterException var4) {
         SimpleLogger.trace("Exception: Failed to send message: " + message);
         successfull = true;
      } catch (AmazonServiceException var5) {
         this.handleException(var5);
      } catch (InterruptedException var6) {
         Thread.currentThread().interrupt();
      } catch (Exception var7) {
         this.handleException(var7);
      }

      if (!successfull) {
         SimpleLogger.trace("Failed to send SNS message: " + message);
      }

      this.updateLastSendTime();
      return successfull;
   }

   private void addNewWorkerToPool() {
      SimpleLogger.trace("Adding SNS Worker for " + this.snsTopic);
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
      SimpleLogger.trace(e, "Error sending SNS message (" + this.snsTopic + ")");
      this.increaseBackoff();
   }

   private void dumpToS3(Collection<String> messages) {
      AmazonS3Client s3 = new AmazonS3Client();
      String s3ObjectPath = this.FailedMessageS3Prefix + System.currentTimeMillis() + ".json";

      try {
         String all = StringUtils.join(messages, ",");
         if (all.endsWith(",")) {
            all = all.substring(0, all.length() - 1);
         }

         byte[] bytes = ("[" + all + "]").getBytes();
         ObjectMetadata metaData = new ObjectMetadata();
         metaData.setContentLength((long)bytes.length);
         metaData.setContentType("application/json");
         s3.putObject(this.FailedMessagesS3Bucket, s3ObjectPath, new ByteArrayInputStream(bytes), metaData);
      } catch (AmazonServiceException var7) {
         this.handleException(var7);
      } catch (Exception var8) {
         this.handleException(var8);
      }

   }

   private void updateLastSendTime() {
      this.lastSendTime = System.currentTimeMillis();
   }

   protected long getLastSendTime() {
      return this.lastSendTime;
   }
}
