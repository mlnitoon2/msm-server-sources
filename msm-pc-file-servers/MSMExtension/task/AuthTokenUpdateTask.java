package com.bigbluebubble.mysingingmonsters.task;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.BBBToken;
import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;

public class AuthTokenUpdateTask implements Runnable {
   public AuthTokenUpdateTask() {
      try {
         Logger.trace("Auth Token Update Task running...");
      } catch (Exception var2) {
         Logger.trace(var2);
      }

   }

   public void run() {
      MSMExtension ext = MSMExtension.getInstance();

      try {
         BBBToken serverToken = BBBToken.getAuthTokenFromServer(GameSettings.get("AUTH_GAME_ID"), GameSettings.get("AUTH2_SERVER_URL"), GameSettings.get("AUTH2_USE_HTTPS"), GameSettings.get("AUTH2_SFS_USERNAME"), GameSettings.get("AUTH2_SFS_PASSWORD"), GameSettings.get("AUTH2_SFS_LOGINTYPE"), GameSettings.get("AUTH2_ENCRYPTION_VECTOR"), GameSettings.get("AUTH2_ENCRYPTION_SECRET"));
         if (serverToken == null) {
            Logger.trace("********************************************************************\n                   COULD NOT GET A SERVER TOKEN                     \n********************************************************************\n");
         } else {
            ext.serverToken = serverToken;
            Logger.trace(LogLevel.DEBUG, "Auth token updated");
         }
      } catch (Exception var3) {
         Logger.trace(var3);
      }

   }
}
