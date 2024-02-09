package com.bigbluebubble.BBBServer.util;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BBBToken {
   String rawEncrypted;
   JSONObject rawJson;
   long generatedOn;
   long expiresAt;
   ArrayList<String> userGameIds;
   ArrayList<String> permissions;
   String username = null;
   String loginType = null;

   public BBBToken(JSONObject json) {
      this.rawJson = json;

      try {
         this.generatedOn = (long)json.getInt("generated_on");
         this.expiresAt = (long)json.getInt("expires_at");
         JSONArray gameIdsJson = json.getJSONArray("user_game_ids");
         this.userGameIds = new ArrayList();

         for(int i = 0; i < gameIdsJson.length(); ++i) {
            this.userGameIds.add(gameIdsJson.getString(i));
         }

         this.permissions = new ArrayList();
         if (json.has("permissions")) {
            JSONObject permissionJson = json.getJSONObject("permissions");
            Iterator itr = permissionJson.keys();

            while(itr.hasNext()) {
               String key = itr.next().toString();
               if (permissionJson.getBoolean(key)) {
                  this.permissions.add(key);
               }
            }
         }

         this.username = json.getString("username");
         this.loginType = json.getString("login_type");
      } catch (JSONException var6) {
         SimpleLogger.trace((Exception)var6);
      }

   }

   public boolean isTokenValid() {
      return this.timeLeft() >= 0L;
   }

   public long timeLeft() {
      return this.expiresAt - System.currentTimeMillis() / 1000L;
   }

   public long createdOn() {
      return this.generatedOn;
   }

   public ArrayList<String> getUserGameIds() {
      return this.userGameIds;
   }

   public boolean hasPermission(String s) {
      return this.permissions.contains(s);
   }

   public String username() {
      return this.username;
   }

   public String loginType() {
      return this.loginType;
   }

   public String getRawEncrypted() {
      return this.rawEncrypted;
   }

   public void setRawEncrypted(String encryptedAccessToken) {
      this.rawEncrypted = encryptedAccessToken;
   }

   public static BBBToken getAuthTokenFromServer(String gameId, String authServerUrl, String useHttps, String username, String password, String loginType, String vector, String secret) throws Exception {
      boolean useHttpsBoolean = Boolean.parseBoolean(useHttps);
      return getAuthTokenFromServer(gameId, authServerUrl, useHttpsBoolean, username, password, loginType, vector, secret);
   }

   public static BBBToken getAuthTokenFromServer(String gameId, String authServerUrl, boolean useHttps, String username, String password, String loginType, String vector, String secret) throws Exception {
      URL url = new URL(authServerUrl + "auth/api/token?g=" + URLEncoder.encode(gameId, "UTF-8"));
      String urlParameters = "u=" + URLEncoder.encode(username, "UTF-8") + "&p=" + URLEncoder.encode(password, "UTF-8") + "&t=" + URLEncoder.encode(loginType, "UTF-8");
      JSONObject res = WebHelper.getJsonDataFromUrl(url, useHttps, urlParameters);
      JSONObject decrypted = decryptToken(res.getString("access_token"), vector, secret);
      BBBToken token = new BBBToken(decrypted);
      token.setRawEncrypted(res.getString("access_token"));
      return !res.getBoolean("ok") ? null : token;
   }

   public static JSONObject decryptToken(String encryptedToken, String vector, String secret) {
      if (encryptedToken != null && !encryptedToken.isEmpty()) {
         try {
            return new JSONObject(Encryption.decrypt(encryptedToken, vector, secret));
         } catch (Exception var4) {
            SimpleLogger.trace(var4);
         }
      }

      return null;
   }
}
