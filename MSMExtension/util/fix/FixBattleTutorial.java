package com.bigbluebubble.mysingingmonsters.util.fix;

import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.bigbluebubble.mysingingmonsters.player.PlayerQuest;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import org.json.JSONObject;

public class FixBattleTutorial {
   public static void ApplyFix(Player p, User user) {
      if (p.hasActiveQuest(358) && !p.questComplete(358) && !p.hasQuest(366)) {
         try {
            String sql = "SELECT * FROM user_mail WHERE user = ? AND message_id = ?";
            ISFSArray currentMail = MSMExtension.getInstance().getDB().query(sql, new Object[]{p.getPlayerId(), 900000L});
            if (currentMail.size() > 0) {
               Logger.trace("Applying Battle Tutorial Fix for Player " + p.getPlayerId());
               int[] battleQuests = new int[]{359, 360, 361, 362, 363, 364, 365, 366};
               int[] var5 = battleQuests;
               int var6 = battleQuests.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  int i = var5[var7];
                  p.killActiveQuest(i);
                  p.addCollectedQuest(i);
               }

               if (!p.hasQuest(371)) {
                  p.addQuest(PlayerQuest.getInitialSFSObject(p.getNextQuestIndex(), 371, (int)p.getPlayerId(), "false", true, false));
               }

               ISFSObject qe = new SFSObject();
               qe.putUtfString("quest", "BATT_TUT_CLAIM_TROPHY");
               qe.putBool("server_generated", true);
               qe.putBool("nosend", true);
               MSMExtension.getInstance().handleClientRequest("gs_quest_event", user, qe);
               MSMExtension.getInstance().stats.sendUserEvent(user, "fix_battle_tutorial", new JSONObject());
            }
         } catch (Exception var9) {
            Logger.trace(var9);
         }
      }

   }
}
