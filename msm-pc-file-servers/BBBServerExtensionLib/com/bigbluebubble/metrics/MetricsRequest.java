package com.bigbluebubble.metrics;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.bigbluebubble.BBBServer.util.SimpleLogger;
import com.smartfoxserver.v2.SmartFoxServer;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

public class MetricsRequest {
   private static String queueUrl = null;
   private String endpoint;
   private JSONObject params = new JSONObject();

   public static void setQueueUrl(String url) {
      queueUrl = url;
   }

   public void setRequestEndpoint(String endpoint) {
      this.endpoint = endpoint;
   }

   public void addParam(String key, String value) {
      try {
         this.params.put(key, (Object)value);
      } catch (JSONException var4) {
         SimpleLogger.trace((Exception)var4);
      }

   }

   public void send() {
      SmartFoxServer.getInstance().getTaskScheduler().schedule(new Runnable() {
         public void run() {
            try {
               AmazonSQSClient sqs = new AmazonSQSClient();
               JSONObject message = new JSONObject();
               message.put("endpoint", (Object)MetricsRequest.this.endpoint);
               message.put("params", (Object)MetricsRequest.this.params);
               SendMessageRequest request = new SendMessageRequest(MetricsRequest.queueUrl, message.toString());
               sqs.sendMessage(request);
            } catch (Exception var4) {
               SimpleLogger.trace(var4);
            }

         }
      }, 0, TimeUnit.SECONDS);
   }
}
