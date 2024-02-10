package com.bigbluebubble.BBBServer.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

public class Misc {
   public static String getStackTraceAsString(Exception exception) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      pw.print(" [ ");
      pw.print(exception.getClass().getName());
      pw.print(" ] ");
      pw.println(exception.getMessage());
      exception.printStackTrace(pw);
      pw.flush();
      return sw.toString();
   }

   public static String implode(String[] ary, String delim) {
      StringBuilder out = new StringBuilder();

      for(int i = 0; i < ary.length; ++i) {
         if (i != 0) {
            out.append(delim);
         }

         out.append(ary[i]);
      }

      return out.toString();
   }

   public static String implode(Long[] ary, String delim) {
      StringBuilder out = new StringBuilder();

      for(int i = 0; i < ary.length; ++i) {
         if (i != 0) {
            out.append(delim);
         }

         out.append(ary[i]);
      }

      return out.toString();
   }

   public static String md5(String s) {
      try {
         MessageDigest digest = MessageDigest.getInstance("MD5");
         digest.update(s.getBytes());
         byte[] messageDigest = digest.digest();
         StringBuffer hexString = new StringBuffer();

         for(int i = 0; i < messageDigest.length; ++i) {
            String h;
            for(h = Integer.toHexString(255 & messageDigest[i]); h.length() < 2; h = "0" + h) {
            }

            hexString.append(h);
         }

         return hexString.toString();
      } catch (NoSuchAlgorithmException var6) {
         SimpleLogger.trace((Exception)var6);
         return "";
      }
   }

   public static String URLEncode(String s) {
      if (s != null && !s.isEmpty()) {
         try {
            return URLEncoder.encode(s, "UTF-8");
         } catch (UnsupportedEncodingException var2) {
            return "";
         }
      } else {
         return "";
      }
   }

   public static byte[] compress(String s, int deflater) throws Exception {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      DeflaterOutputStream dos = new DeflaterOutputStream(os, new Deflater(deflater));
      PrintStream ps = new PrintStream(dos, true, "UTF-8");
      ps.print(s);
      dos.close();
      return os.toByteArray();
   }

   public static byte[] uncompress(byte[] b) throws Exception {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      InflaterOutputStream ios = new InflaterOutputStream(os);
      ios.write(b, 0, b.length);
      ios.close();
      return os.toByteArray();
   }

   public static String compressTextData(String s) throws Exception {
      return MigBase64.encodeToString(compress(s, 1), false);
   }

   public static String decompressTextData(String s) throws Exception {
      return new String(uncompress(MigBase64.decodeFast(s)), "UTF-8");
   }

   public static void updateEnv(String name, String val) throws Exception {
      Map<String, String> env = System.getenv();
      Field field = env.getClass().getDeclaredField("m");
      field.setAccessible(true);
      ((Map)field.get(env)).put(name, val);
   }

   public static String getEnv(String name) throws Exception {
      Map<String, String> env = System.getenv();
      return env.containsKey(name) ? (String)env.get(name) : null;
   }

   public static boolean hasEnv(String name) throws Exception {
      Map<String, String> env = System.getenv();
      return env.containsKey(name);
   }
}
