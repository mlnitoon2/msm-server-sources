package com.bigbluebubble.BBBServer.util;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.bigbluebubble.BBBServer.BBBServerExtension;
import com.smartfoxserver.v2.SmartFoxServer;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;

public class SqsWrapper {
   BBBServerExtension ext = null;
   AmazonSQSClient sqs = null;

   public SqsWrapper(BBBServerExtension ext, BasicAWSCredentials awsCredentials) {
      this.ext = ext;
      this.sqs = new AmazonSQSClient(awsCredentials);
   }

   public void sendToQueue(final String queueUrl, final String endpoint, final JSONObject params) {
      SmartFoxServer.getInstance().getTaskScheduler().schedule(new Runnable() {
         public void run() {
            try {
               JSONObject message = new JSONObject();
               message.put("endpoint", (Object)endpoint);
               message.put("params", (Object)params);
               SendMessageRequest request = new SendMessageRequest(queueUrl, message.toString());
               SqsWrapper.this.sqs.sendMessage(request);
            } catch (Exception var3) {
               SimpleLogger.trace(var3);
            }

         }
      }, 0, TimeUnit.SECONDS);
   }

   public void sendToQueue(final String queueUrl, final String msg) {
      SmartFoxServer.getInstance().getTaskScheduler().schedule(new Runnable() {
         public void run() {
            try {
               SqsWrapper.this.sqs.sendMessage(new SendMessageRequest(queueUrl, msg));
            } catch (Exception var2) {
               SimpleLogger.trace(var2);
            }

         }
      }, 0, TimeUnit.SECONDS);
   }
}
