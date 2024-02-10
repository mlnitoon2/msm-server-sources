package com.bigbluebubble.BBBServer.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONObject;

public class WebHelper {
   public static String getDataFromUrl(URL url) {
      return getDataFromUrl(url, false, (String)null, (BBBToken)null);
   }

   public static String getDataFromUrl(URL url, boolean secure) {
      return getDataFromUrl(url, false, (String)null, (BBBToken)null);
   }

   public static String getDataFromUrl(URL url, boolean secure, String urlParameters) {
      return getDataFromUrl(url, false, urlParameters, (BBBToken)null);
   }

   public static String getDataFromUrl(URL url, boolean secure, String urlParameters, BBBToken token) {
      String returnValue = null;

      try {
         InputStream response;
         String contentType;
         byte[] postData;
         int postDataLength;
         DataOutputStream wr;
         int responseCode;
         if (secure) {
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            if (token != null) {
               connection.setRequestProperty("Authorization", "Bearer " + token.getRawEncrypted());
            }

            connection.setRequestProperty("Accept-Charset", "UTF-8");
            if (urlParameters != null) {
               postData = urlParameters.getBytes(Charset.forName("UTF-8"));
               postDataLength = postData.length;
               connection.setDoOutput(true);
               connection.setInstanceFollowRedirects(false);
               connection.setRequestMethod("POST");
               connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
               connection.setRequestProperty("charset", "utf-8");
               connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
               connection.setUseCaches(false);
               wr = new DataOutputStream(connection.getOutputStream());
               wr.write(postData);
            }

            responseCode = connection.getResponseCode();
            if (responseCode != 200) {
               throw new Exception("Got response code " + responseCode + " from " + url.toString());
            }

            response = connection.getInputStream();
            contentType = connection.getHeaderField("Content-Type");
         } else {
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            if (token != null) {
               connection.setRequestProperty("Authorization", "Bearer " + token.getRawEncrypted());
            }

            connection.setRequestProperty("Accept-Charset", "UTF-8");
            if (urlParameters != null) {
               postData = urlParameters.getBytes(Charset.forName("UTF-8"));
               postDataLength = postData.length;
               connection.setDoOutput(true);
               connection.setInstanceFollowRedirects(false);
               connection.setRequestMethod("POST");
               connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
               connection.setRequestProperty("charset", "utf-8");
               connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
               connection.setUseCaches(false);
               wr = new DataOutputStream(connection.getOutputStream());
               wr.write(postData);
            }

            responseCode = connection.getResponseCode();
            if (responseCode != 200) {
               throw new Exception("Got response code " + responseCode + " from " + url.toString());
            }

            response = connection.getInputStream();
            contentType = connection.getHeaderField("Content-Type");
         }

         String charset = null;
         String[] var24 = contentType.replace(" ", "").split(";");
         postDataLength = var24.length;

         for(int var27 = 0; var27 < postDataLength; ++var27) {
            String param = var24[var27];
            if (param.startsWith("charset=")) {
               charset = param.split("=", 2)[1];
               break;
            }
         }

         if (charset == null) {
            throw new Exception("Response looks binary...or something.");
         }

         BufferedReader reader = null;

         try {
            String s = "";

            String line;
            for(reader = new BufferedReader(new InputStreamReader(response, charset)); (line = reader.readLine()) != null; s = s + line) {
            }

            returnValue = s;
         } finally {
            if (reader != null) {
               try {
                  reader.close();
               } catch (IOException var18) {
                  throw var18;
               }
            }

         }
      } catch (Exception var20) {
         SimpleLogger.trace(var20);
      }

      return returnValue;
   }

   public static JSONObject getJsonDataFromUrl(URL url) {
      return getJsonDataFromUrl(url, false);
   }

   public static JSONObject getJsonDataFromUrl(URL url, boolean secure) {
      return getJsonDataFromUrl(url, secure, (String)null);
   }

   public static JSONObject getJsonDataFromUrl(URL url, boolean secure, String urlParameters) {
      JSONObject obj = null;

      try {
         String strData = getDataFromUrl(url, secure, urlParameters);
         obj = new JSONObject(strData);
      } catch (Exception var5) {
         SimpleLogger.trace(var5);
      }

      return obj;
   }
}
