package com.bigbluebubble.monitoring;

import com.bigbluebubble.BBBServer.util.SimpleLogger;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;

public class EC2Metadata {
   public static boolean runningOnEC2() {
      return !getEC2InstanceId().isEmpty();
   }

   public static String getEC2InstanceMetadata(String category) {
      String metadata = "";

      try {
         URL url = new URL("http://169.254.169.254/latest/meta-data/" + category);
         URLConnection conn = url.openConnection();

         BufferedReader br;
         String input;
         for(br = new BufferedReader(new InputStreamReader(conn.getInputStream())); (input = br.readLine()) != null; metadata = input) {
         }

         br.close();
      } catch (ConnectException var6) {
      } catch (Exception var7) {
         SimpleLogger.trace(var7);
      }

      return metadata;
   }

   public static String getEC2InstanceId() {
      return getEC2InstanceMetadata("instance-id");
   }
}
