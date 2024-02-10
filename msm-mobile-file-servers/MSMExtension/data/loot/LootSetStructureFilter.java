package com.bigbluebubble.mysingingmonsters.data.loot;

import com.bigbluebubble.mysingingmonsters.data.Structure;
import com.bigbluebubble.mysingingmonsters.data.StructureLookup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LootSetStructureFilter implements ILootSetFilter {
   public List<Integer> filterList(List<Integer> items, int playerLevel) {
      List<Integer> filtered = new ArrayList();
      Iterator var4 = items.iterator();

      while(var4.hasNext()) {
         int entityId = (Integer)var4.next();
         Structure structureData = StructureLookup.getFromEntityId(entityId);
         if (playerLevel >= structureData.getLevelUnlocked()) {
            filtered.add(entityId);
         }
      }

      return filtered;
   }
}
