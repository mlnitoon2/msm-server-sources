package com.bigbluebubble.mysingingmonsters;

import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.mysingingmonsters.data.DailyCumulativeLoginCalendar;
import com.bigbluebubble.mysingingmonsters.data.DailyCumulativeLoginLookup;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.bigbluebubble.mysingingmonsters.player.PlayerDailyCumulativeLogin;
import com.smartfoxserver.v2.annotations.MultiHandler;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.util.ClientDisconnectionReason;

@MultiHandler
public class TestMultiHandler extends BaseClientRequestHandler {
   MSMExtension ext;

   public void handleClientRequest(User sender, ISFSObject params) {
      try {
         this.ext = (MSMExtension)this.getParentExtension();
         if (sender.getPrivilegeId() != 3) {
            throw new Exception("Unauthorized Access");
         } else if ((Long)sender.getProperty("sess_start") < MSMExtension.cache_loaded_on) {
            this.ext.savePlayer(sender);
            sender.disconnect(ClientDisconnectionReason.KICK);
         } else {
            String requestID = params.getUtfString("__[[REQUEST_ID]]__");
            if (requestID.equals("checkA")) {
               Player player = (Player)sender.getProperty("player_object");
               if (player != null) {
                  synchronized(player) {
                     this.testCheckA(sender, params);
                  }
               }
            } else if (requestID.equals("checkB")) {
               this.testCheckB(sender, params);
            } else if (requestID.equals("resetCalendar")) {
               this.resetCalendar(sender, params);
            }

         }
      } catch (Exception var8) {
         Logger.trace(var8, "Exception while processing request:", params.getDump());
         throw new RuntimeException("Exception while processing request", var8);
      }
   }

   private void testCheckA(User sender, ISFSObject params) {
      Logger.trace(LogLevel.INFO, "testCheckA");
   }

   private void testCheckB(User sender, ISFSObject params) {
      Logger.trace(LogLevel.INFO, "testCheckB");
   }

   private void resetCalendar(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      ISFSObject response = new SFSObject();
      boolean success = true;

      try {
         PlayerDailyCumulativeLogin dcl = player.getDailyCumulativeLogin();
         if (!params.containsKey("calendar_id")) {
            throw new Exception("Missing Data:calendar_id");
         }

         int calendarId = params.getInt("calendar_id");
         DailyCumulativeLoginCalendar calendarData = DailyCumulativeLoginLookup.get(calendarId);
         if (calendarData == null) {
            throw new Exception("Invalid Calendar Id: " + calendarId);
         }

         if (!params.containsKey("reward_idx")) {
            throw new Exception("Missing Data:reward_idx");
         }

         int rewardIdx = params.getInt("reward_idx");
         if (rewardIdx < 0 || rewardIdx >= calendarData.getNumRewards()) {
            throw new Exception("Bad Reward index " + rewardIdx);
         }

         Logger.trace("TEST >>> Resetting Calendar! Player = " + player.getPlayerId() + " Calendar = " + calendarId + " RewardIdx = " + rewardIdx);
         dcl.initFromSFSObject(params);
         dcl.setDirty(true);
         dcl.save(this.ext.getDB(), player.getPlayerId());
      } catch (Exception var10) {
         Logger.trace(var10);
         success = false;
      }

      response.putSFSObject("state", player.getDailyCumulativeLogin().toSFSObject());
      response.putBool("success", success);
      this.send("test.resetCalendar", response, sender);
   }
}
