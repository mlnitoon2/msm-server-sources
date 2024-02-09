package com.bigbluebubble.mysingingmonsters.data.loot;

import com.bigbluebubble.mysingingmonsters.data.Monster;
import com.bigbluebubble.mysingingmonsters.data.MonsterLookup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LootSetMonsterFilter implements ILootSetFilter {
   public List<Integer> filterList(List<Integer> items, int playerLevel) {
      List<Integer> filtered = new ArrayList();
      Iterator var4 = items.iterator();

      while(var4.hasNext()) {
         int entityId = (Integer)var4.next();
         Monster monsterData = MonsterLookup.getFromEntityId(entityId);
         if (playerLevel >= monsterData.getLevelUnlocked()) {
            filtered.add(entityId);
         }
      }

      return filtered;
   }
}
