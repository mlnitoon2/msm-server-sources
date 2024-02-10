package com.bigbluebubble.BBBServer.util;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPost implements Runnable {
   private final String _targetURL;
   private final String _urlParameters;

   public HttpPost(String targetURL, String urlParameters) {
      this._targetURL = targetURL;
      this._urlParameters = urlParameters;
   }

   public void run() {
      HttpURLConnection connection = null;

      try {
         URL url = new URL(this._targetURL);
         connection = (HttpURLConnection)url.openConnection();
         connection.setRequestMethod("POST");
         connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
         connection.setRequestProperty("Content-Length", "" + Integer.toString(this._urlParameters.getBytes().length));
         connection.setRequestProperty("Content-Language", "en-US");
         connection.setUseCaches(false);
         connection.setDoOutput(true);
         DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
         wr.writeBytes(this._urlParameters);
         wr.flush();
         wr.close();
         if (connection.getResponseCode() != 200) {
         }
      } catch (Exception var7) {
         SimpleLogger.trace(var7);
      } finally {
         if (connection != null) {
            connection.disconnect();
         }

      }

   }
}
