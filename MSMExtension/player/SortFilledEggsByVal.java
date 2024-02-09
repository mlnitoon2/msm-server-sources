package com.bigbluebubble.mysingingmonsters.player;

import com.bigbluebubble.mysingingmonsters.data.MonsterLookup;
import java.util.Comparator;

class SortFilledEggsByVal implements Comparator<Integer> {
   int curIslandType;

   public SortFilledEggsByVal(int curIslandType) {
      this.curIslandType = curIslandType;
   }

   public int compare(Integer mid1, Integer mid2) {
      return MonsterLookup.get(mid1).getCostCoins(this.curIslandType) - MonsterLookup.get(mid2).getCostCoins(this.curIslandType);
   }
}
