package com.bigbluebubble.mysingingmonsters.data.loot;

import java.util.HashMap;
import java.util.Map;

public enum LootType {
   TABLE(1),
   SET(2),
   GROUP(3),
   COINS(4),
   SHARDS(5),
   DIAMONDS(6),
   FOOD(7),
   RELICS(8),
   KEYS(9),
   STARPOWER(10),
   MONSTER(11),
   STRUCTURE(12),
   COSTUME(13),
   BUFF(14);

   private static Map<Integer, LootType> idToEnumValuesMapping = new HashMap();
   private int id;

   public static LootType fromInt(int id) {
      return (LootType)idToEnumValuesMapping.get(id);
   }

   public int getId() {
      return this.id;
   }

   private LootType(int id) {
      this.id = id;
   }

   static {
      LootType[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         LootType t = var0[var2];
         idToEnumValuesMapping.put(t.getId(), t);
      }

   }
}
