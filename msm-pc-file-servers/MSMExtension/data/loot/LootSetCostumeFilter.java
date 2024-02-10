package com.bigbluebubble.mysingingmonsters.data.loot;

import com.bigbluebubble.mysingingmonsters.costumes.CostumeData;
import com.bigbluebubble.mysingingmonsters.costumes.CostumeLookup;
import com.bigbluebubble.mysingingmonsters.data.Monster;
import com.bigbluebubble.mysingingmonsters.data.MonsterLookup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LootSetCostumeFilter implements ILootSetFilter {
   public List<Integer> filterList(List<Integer> items, int playerLevel) {
      List<Integer> filtered = new ArrayList();
      Iterator var4 = items.iterator();

      while(var4.hasNext()) {
         int id = (Integer)var4.next();
         CostumeData costumeData = CostumeLookup.get(id);
         Monster monsterData = MonsterLookup.get(costumeData.getMonster());
         if (playerLevel >= monsterData.getLevelUnlocked()) {
            filtered.add(id);
         }
      }

      return filtered;
   }
}
