package com.bigbluebubble.mysingingmonsters.data.loot;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootItem implements ILootable {
   public static final String TYPE_KEY = "type";
   public static final String ID_KEY = "id";
   public static final String MIN_KEY = "min";
   public static final String MAX_KEY = "max";
   public static final String EXTRA_KEY = "extra";
   LootType type;
   int id = 0;
   int min = 1;
   int max = 1;
   ISFSObject extra;

   public LootItem(ISFSObject sfsObject) {
      this.type = LootType.valueOf(sfsObject.getUtfString("type"));
      if (sfsObject.containsKey("id")) {
         this.id = sfsObject.getInt("id");
      }

      if (sfsObject.containsKey("min")) {
         this.min = sfsObject.getInt("min");
      }

      if (sfsObject.containsKey("max")) {
         this.max = sfsObject.getInt("max");
      }

      if (this.max < this.min) {
         this.max = this.min;
      }

      if (sfsObject.containsKey("extra")) {
         this.extra = sfsObject.getSFSObject("extra");
      }

   }

   public List<LootResult> pull(Random rng, int amount, int maxLevel) {
      List<LootResult> results = new ArrayList();
      int total = 0;

      for(int i = 0; i < amount; ++i) {
         total += this.min + (this.max > this.min ? rng.nextInt(this.max - this.min) : 0);
      }

      if (total > 0) {
         results.add(new LootResult(this.type, this.id, total, this.extra));
      }

      return results;
   }
}
