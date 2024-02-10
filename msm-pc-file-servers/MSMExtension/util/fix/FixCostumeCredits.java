package com.bigbluebubble.mysingingmonsters.util.fix;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.costumes.CostumeData;
import com.bigbluebubble.mysingingmonsters.costumes.CostumeLookup;
import com.bigbluebubble.mysingingmonsters.costumes.IslandCostumeState;
import com.bigbluebubble.mysingingmonsters.data.Island;
import com.bigbluebubble.mysingingmonsters.data.IslandLookup;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.bigbluebubble.mysingingmonsters.player.PlayerIsland;
import java.util.Iterator;

public class FixCostumeCredits {
   public static void ApplyFix(Player p) {
      if (p.LastLoginTime() <= GameSettings.get("COSTUME_CREDIT_FIX_TIMESTAMP", 1675209600000L)) {
         Logger.trace(LogLevel.DEBUG, "Applying Costume Credits Fix for Player " + p.getPlayerId());
         Iterator var1 = p.getIslands().iterator();

         while(var1.hasNext()) {
            PlayerIsland island = (PlayerIsland)var1.next();
            ApplyFix(p, island);
         }

      }
   }

   private static void ApplyFix(Player p, PlayerIsland island) {
      try {
         boolean isDirty = false;
         Island islandData = IslandLookup.get(island.getIndex());
         IslandCostumeState islandCostumeState = island.getCostumeState();
         if (islandCostumeState != null) {
            Iterator var5 = islandCostumeState.costumeIds().iterator();

            while(var5.hasNext()) {
               int costumeId = (Integer)var5.next();
               int credits = islandCostumeState.getCredits(costumeId);
               if (credits > 0) {
                  CostumeData costumeData = CostumeLookup.get(costumeId);
                  int targetMonster = costumeData.getMonster();
                  if (!islandData.hasMonster(targetMonster)) {
                     Logger.trace("Found a bad costume on island... p:" + p.getPlayerId() + " i: " + island.getIndex() + " c:" + costumeId + " credits:" + credits + " --- fixing!");
                     p.getCostumes().inventory().addItem(costumeId, credits);
                     islandCostumeState.removeCredit(costumeId, credits);
                     isDirty = true;
                  }
               }
            }
         }

         if (isDirty) {
            p.saveCostumes();
            MSMExtension.getInstance().savePlayerIsland(p, island, false);
         }
      } catch (Exception var10) {
         Logger.trace(var10);
      }

   }
}
