package com.bigbluebubble.BBBServer.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class Encryption {
   public static String encrypt(String message, String initialVectorString, String secretKey) {
      String encryptedString = null;

      try {
         SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
         IvParameterSpec initialVector = new IvParameterSpec(initialVectorString.getBytes());
         Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
         cipher.init(1, skeySpec, initialVector);
         byte[] encrypted = cipher.doFinal(message.getBytes());
         encryptedString = new String(encrypted);
      } catch (Exception var8) {
      }

      return encryptedString;
   }

   public static String decrypt(String encryptedData, String initialVectorString, String secretKey) throws Exception {
      String decryptedData = null;
      SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
      IvParameterSpec initialVector = new IvParameterSpec(initialVectorString.getBytes());
      Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
      cipher.init(2, skeySpec, initialVector);
      byte[] encryptedByteArray = (new Base64()).decode(encryptedData.getBytes());
      byte[] decryptedByteArray = cipher.doFinal(encryptedByteArray);
      decryptedData = new String(decryptedByteArray, "UTF8");
      return decryptedData;
   }
}
