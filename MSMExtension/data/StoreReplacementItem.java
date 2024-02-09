package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.mysingingmonsters.player.PlayerIsland;
import java.util.ArrayList;

public class StoreReplacementItem {
   public ArrayList<Integer> sourceEntityIds;
   public int numToOwnBeforeReplaced;
   public int replacementEntityId;

   public StoreReplacementItem(ArrayList<Integer> sourceEntIds, int numToOwn, int replacementEntId) {
      this.sourceEntityIds = sourceEntIds;
      this.numToOwnBeforeReplaced = numToOwn;
      this.replacementEntityId = replacementEntId;
   }

   public boolean ownsEnoughToMakeReplacement(PlayerIsland island) {
      if (island == null) {
         return false;
      } else {
         int numSourceEntity = 0;

         for(int i = 0; i < this.sourceEntityIds.size(); ++i) {
            numSourceEntity += island.numStructuresOfEntTypeOnIsland((Integer)this.sourceEntityIds.get(i));
         }

         return numSourceEntity >= this.numToOwnBeforeReplaced;
      }
   }
}
