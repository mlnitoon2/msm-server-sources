package com.bigbluebubble.mysingingmonsters.data.loot;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class LootGroup implements ILootable {
   public static final String GROUP_KEY = "groups";
   public static final String MIN_KEY = "min";
   public static final String MAX_KEY = "max";
   public List<LootTableEntry> groups = new ArrayList();
   int min = 1;
   int max = 1;

   public LootGroup(ISFSObject sfsObject, IDbWrapper db) throws Exception {
      if (sfsObject.containsKey("min")) {
         this.min = sfsObject.getInt("min");
      }

      if (sfsObject.containsKey("max")) {
         this.max = sfsObject.getInt("max");
      }

      assert this.max >= this.min;

      ISFSArray groupData = sfsObject.getSFSArray("groups");

      for(int i = 0; i < groupData.size(); ++i) {
         this.groups.add(new LootTableEntry(groupData.getSFSObject(i), db));
      }

   }

   public List<LootResult> pull(Random rng, int amount, int maxLevel) {
      List<LootResult> results = new ArrayList();

      for(int i = 0; i < amount; ++i) {
         int a = this.min + (this.max - this.min > 0 ? rng.nextInt(this.max - this.min) : 0);
         if (a > 0) {
            Iterator var7 = this.groups.iterator();

            while(var7.hasNext()) {
               LootTableEntry g = (LootTableEntry)var7.next();
               float roll = rng.nextFloat();
               if (roll <= g.probability) {
                  List<LootResult> pullResults = g.loot.pull(rng, a, maxLevel);
                  results = LootResult.Merge((List)results, pullResults);
               }
            }
         }
      }

      return (List)results;
   }
}
