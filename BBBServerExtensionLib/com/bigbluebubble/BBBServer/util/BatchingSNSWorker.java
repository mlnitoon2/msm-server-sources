package com.bigbluebubble.BBBServer.util;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.bigbluebubble.BBBServer.BBBServerExtension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import net.spy.memcached.util.StringUtils;

public class BatchingSNSWorker extends SNSWorker {
   private static final int COMMA_BYTE_LENGTH = getByteLength();
   private static final int ARRAY_BRACKETS_BYTE_LENGTH = getByteArrayLength();
   private static final int MAX_FLUSH_INTERVAL = 60000;

   public BatchingSNSWorker(BBBServerExtension ext, Region region, String topic, String bucket, String prefix, int maxThreads, BasicAWSCredentials credentials) {
      super(ext, region, topic, bucket, prefix, maxThreads, credentials);
   }

   protected Runnable getWorker() {
      return new Runnable() {
         public void run() {
            ArrayList<String> batch = new ArrayList();

            String message;
            for(int batchLength = 0; !Thread.interrupted(); batchLength = BatchingSNSWorker.this.addMessageToBatch(batch, batchLength, message)) {
               message = null;

               try {
                  message = BatchingSNSWorker.this.takeMessage();
                  BatchingSNSWorker.this.monitorThreads();
               } catch (InterruptedException var5) {
                  BatchingSNSWorker.this.addMessages(batch);
                  return;
               }
            }

            BatchingSNSWorker.this.addMessages(batch);
         }
      };
   }

   protected void dealWithRemainingMessagesAtShutdown() {
      Collection<String> remainingMessages = this.drainQueue();
      int batchLength = 0;
      ArrayList<String> batch = new ArrayList();

      String message;
      for(Iterator var4 = remainingMessages.iterator(); var4.hasNext(); batchLength = this.addMessageToBatch(batch, batchLength, message)) {
         message = (String)var4.next();
      }

      if (batchLength > 0) {
         this.sendMessageBatch(batch);
      }

   }

   private void sendMessageBatch(Collection<String> batch) {
      String all = StringUtils.join(batch, ",");
      if (all.endsWith(",")) {
         all = all.substring(0, all.length() - 1);
      }

      String message = "[" + all + "]";
      if (!this.sendMessage(message)) {
         this.addMessages(batch);
      }

   }

   private int addMessageToBatch(ArrayList<String> batch, int currentBatchLength, String message) {
      int messageLength = 0;

      try {
         messageLength = message.getBytes("UTF8").length;
      } catch (Exception var6) {
         SimpleLogger.trace(var6);
      }

      int batchLength = currentBatchLength;
      if (ARRAY_BRACKETS_BYTE_LENGTH + currentBatchLength + batch.size() * COMMA_BYTE_LENGTH + messageLength > 262144 || System.currentTimeMillis() > 60000L + this.getLastSendTime()) {
         this.sendMessageBatch(batch);
         batch.clear();
         batchLength = 0;
      }

      batch.add(message);
      return batchLength + messageLength;
   }

   private static int getByteLength() {
      try {
         return ",".getBytes("UTF8").length;
      } catch (Exception var1) {
         SimpleLogger.trace(var1);
         return 1;
      }
   }

   private static int getByteArrayLength() {
      try {
         return "[".getBytes("UTF8").length + "]".getBytes("UTF8").length;
      } catch (Exception var1) {
         SimpleLogger.trace(var1);
         return 2;
      }
   }
}
