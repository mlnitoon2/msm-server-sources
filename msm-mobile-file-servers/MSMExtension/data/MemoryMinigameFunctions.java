package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.SFSArray;

/** @deprecated */
@Deprecated
public class MemoryMinigameFunctions {
   public static boolean canPlayMemoryMinigame(User sender, SFSArray record) throws Exception {
      if (record == null) {
         Player player = (Player)sender.getProperty("player_object");
         record = getMemoryMinigameUserData(player);
      }

      if (record.size() > 0) {
         Long lastFreePlay = record.getSFSObject(0).getLong("last_free_play") / 1000L;
         long twentyFourHours = 3600L * (long)GameSettings.getInt("MEMORY_TIME_BETWEEN_FREE_PLAYS");
         long eligableToPlayAt = lastFreePlay + twentyFourHours;
         long timeNow = MSMExtension.CurrentDBTime() / 1000L;
         return eligableToPlayAt <= timeNow;
      } else {
         return true;
      }
   }

   public static SFSArray getMemoryMinigameUserData(Player player) throws Exception {
      String sql = "SELECT * FROM user_memory_mini_game WHERE user=?";
      Object[] args = new Object[]{player.getPlayerId()};
      new SFSArray();
      SFSArray record = MSMExtension.getInstance().getDB().query(sql, args);
      return record;
   }
}
