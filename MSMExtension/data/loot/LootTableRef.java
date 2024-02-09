package com.bigbluebubble.mysingingmonsters.data.loot;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootTableRef implements ILootable {
   public static final String ID_KEY = "id";
   public static final String MIN_KEY = "min";
   public static final String MAX_KEY = "max";
   int lootTableId;
   int min = 1;
   int max = 1;

   public LootTableRef(ISFSObject sfsObject) {
      if (sfsObject.containsKey("min")) {
         this.min = sfsObject.getInt("min");
      }

      if (sfsObject.containsKey("max")) {
         this.max = sfsObject.getInt("max");
      }

      assert this.max >= this.min;

      this.lootTableId = sfsObject.getInt("id");
   }

   public List<LootResult> pull(Random rng, int amount, int maxLevel) {
      List<LootResult> results = new ArrayList();
      LootTable table = LootTableLookup.get(this.lootTableId);

      for(int i = 0; i < amount; ++i) {
         int a = this.min + (this.max - this.min > 0 ? rng.nextInt(this.max - this.min) : 0);
         if (a > 0) {
            List<LootResult> pullResults = table.pull(rng, a, maxLevel);
            results = LootResult.Merge((List)results, pullResults);
         }
      }

      return (List)results;
   }
}
