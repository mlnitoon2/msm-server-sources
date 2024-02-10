package com.bigbluebubble.BBBServer.util;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.bigbluebubble.BBBServer.BBBServerExtension;
import com.bigbluebubble.BBBServer.GameSettings;
import com.smartfoxserver.v2.SmartFoxServer;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;

public class SnsWrapper {
   BBBServerExtension ext = null;
   AmazonSNSClient sns = null;

   public SnsWrapper(BBBServerExtension ext, BasicAWSCredentials awsCredentials) {
      this.ext = ext;
      this.sns = new AmazonSNSClient(awsCredentials);
      String snsEndpoint = GameSettings.get("AWS_SNS_ENDPOINT", "https://sns.us-east-1.amazonaws.com");
      SimpleLogger.trace("SNS Endpoint set to " + snsEndpoint);
      this.sns.setEndpoint(snsEndpoint);
   }

   public SnsWrapper(BBBServerExtension ext) {
      this.ext = ext;
      SimpleLogger.trace("Starting bogus SNS Wrapper ...");
   }

   public void publish(final String topicArn, final String endpoint, final JSONObject params) {
      if (this.sns != null) {
         SmartFoxServer.getInstance().getTaskScheduler().schedule(new Runnable() {
            public void run() {
               try {
                  JSONObject message = new JSONObject();
                  message.put("endpoint", (Object)endpoint);
                  message.put("params", (Object)params);
                  PublishRequest request = new PublishRequest(topicArn, message.toString());
                  SnsWrapper.this.sns.publish(request);
               } catch (Exception var3) {
                  SimpleLogger.trace(var3);
               }

            }
         }, 0, TimeUnit.SECONDS);
      }
   }
}
