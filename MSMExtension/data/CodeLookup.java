package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.lang.ArrayUtils;

public class CodeLookup {
   public static Map<String, Code> codes;
   public static Cipher encryptCipher = null;
   public static Cipher decryptCipher = null;

   public static void init(IDbWrapper db, String secretKey, String initializationVector) throws Exception {
      codes = new ConcurrentHashMap();
      SecretKeySpec KS = new SecretKeySpec(secretKey.getBytes(), "Blowfish");
      IvParameterSpec IV = new IvParameterSpec(initializationVector.getBytes());

      try {
         decryptCipher = Cipher.getInstance("Blowfish/CBC/NoPadding");
         decryptCipher.init(2, KS, IV);
         encryptCipher = Cipher.getInstance("Blowfish/CBC/NoPadding");
         encryptCipher.init(1, KS, IV);
      } catch (Exception var9) {
         Logger.trace(var9, "Error initializing code decryption");
      }

      String sql = "SELECT * FROM codes WHERE code_id < 1000000 AND game_id = 1 AND promo_code IS NOT NULL";
      ISFSArray results = db.query("SELECT * FROM codes WHERE code_id < 1000000 AND game_id = 1 AND promo_code IS NOT NULL");
      Iterator i = results.iterator();

      while(i.hasNext()) {
         Code code = new Code((SFSObject)((SFSDataWrapper)i.next()).getObject());
         codes.put(code.getPromoCode(), code);
      }

   }

   public static Code decrypt(String code) throws CodeLookup.CodeException {
      Code codeData = null;
      int codeLength = code.length();
      if (codeLength >= 16) {
         if (codeLength == 16) {
            try {
               byte[] decrypted = decryptCipher.doFinal(DatatypeConverter.parseHexBinary(code));
               byte checksum = decrypted[decrypted.length - 1];
               decrypted = ArrayUtils.remove(decrypted, decrypted.length - 1);
               MessageDigest md = MessageDigest.getInstance("MD5");
               byte[] digest = md.digest(decrypted);
               if (digest[0] != checksum) {
                  return null;
               }

               int advertiserId = decrypted[0] & 255;
               int gameId = (decrypted[1] & 255) >>> 2;
               int advertismentId = (decrypted[1] & 3) << 10 | (decrypted[2] & 255) << 2 | (decrypted[3] & 255) >>> 6;
               int codeId = (decrypted[3] & 63) << 24 | (decrypted[4] & 255) << 16 | (decrypted[5] & 255) << 8 | decrypted[6] & 255;
               String sql = "SELECT * FROM codes WHERE advertiser_id=? AND game_id=? AND advertisment_id=?";
               ISFSArray results = MSMExtension.getInstance().getDB().query(sql, new Object[]{advertiserId, gameId, advertismentId});
               if (results == null || results.size() != 1) {
                  return null;
               }

               results.getSFSObject(0).putInt("code", codeId);
               codeData = new Code(results.getSFSObject(0));
               if (codeData.getStartDate() != 0L && codeData.getStartDate() > MSMExtension.CurrentDBTime()) {
                  return null;
               }

               if (codeData.getEndDate() != 0L && codeData.getEndDate() < MSMExtension.CurrentDBTime()) {
                  throw new CodeLookup.CodeException("CODE_EXPIRED_TEXT");
               }

               if ((long)codeId >= codeData.getCodeLimit()) {
                  return null;
               }
            } catch (CodeLookup.CodeException var13) {
               throw var13;
            } catch (Exception var14) {
               return null;
            }
         }
      } else {
         codeData = (Code)codes.get(code);
         if (codeData != null && codeData.getCodeLimit() > 0L) {
            if (codeData.getClaimCount() >= codeData.getCodeLimit()) {
               throw new CodeLookup.CodeException("ALREADY_CLAIMED_CODE_TEXT");
            }

            try {
               String sql = "SELECT * FROM codes WHERE code_id = ?";
               ISFSArray results = MSMExtension.getInstance().getDB().query(sql, new Object[]{codeData.getCodeID()});
               if (results == null || results.size() != 1) {
                  return null;
               }

               codeData = new Code(results.getSFSObject(0));
               if (codeData.getClaimCount() >= codeData.getCodeLimit()) {
                  throw new CodeLookup.CodeException("ALREADY_CLAIMED_CODE_TEXT");
               }
            } catch (CodeLookup.CodeException var15) {
               throw var15;
            } catch (Exception var16) {
               return null;
            }
         }

         if (codeData != null && codeData.getStartDate() != 0L && codeData.getStartDate() > MSMExtension.CurrentDBTime()) {
            return null;
         }

         if (codeData != null && codeData.getEndDate() != 0L && codeData.getEndDate() < MSMExtension.CurrentDBTime()) {
            throw new CodeLookup.CodeException("CODE_EXPIRED_TEXT");
         }
      }

      return codeData;
   }

   public static ISFSArray getCodes(int codeId) {
      SFSArray codes = new SFSArray();

      try {
         String sql = "SELECT * FROM codes WHERE code_id=?";
         ISFSArray results = MSMExtension.getInstance().getDB().query(sql, new Object[]{codeId});
         if (results != null && results.size() == 1) {
            Code codeData = new Code(results.getSFSObject(0));
            if (codeData.getCodeLimit() == 0L) {
               return null;
            } else {
               MessageDigest md = MessageDigest.getInstance("MD5");

               for(int i = 0; (long)i < codeData.getCodeLimit(); ++i) {
                  byte[] packedData = new byte[]{(byte)(codeData.getAdvertiserID() & 255), (byte)((codeData.getGameID() & 255) << 2 | codeData.getAdvertismentID() >>> 10), (byte)((codeData.getAdvertismentID() & 1023) >>> 2), (byte)((codeData.getAdvertismentID() & 3) << 6 | i >>> 24), (byte)((i & 16777215) >>> 16), (byte)((i & '\uffff') >>> 8), (byte)(i & 255)};
                  packedData = ArrayUtils.add(packedData, md.digest(packedData)[0]);
                  codes.addUtfString(DatatypeConverter.printHexBinary(encryptCipher.doFinal(packedData)));
               }

               return codes;
            }
         } else {
            return null;
         }
      } catch (Exception var8) {
         return null;
      }
   }

   public static class CodeException extends Exception {
      private static final long serialVersionUID = 1L;

      public CodeException(String message) {
         super(message);
      }
   }
}
