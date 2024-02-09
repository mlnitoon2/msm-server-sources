package com.bigbluebubble.mysingingmonsters.data.loot;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticData;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class LootTable extends StaticData implements ILootable {
   public static final String ID_KEY = "id";
   public static final String LOOT_KEY = "loot";
   public List<LootTableEntry> loot = new ArrayList();

   protected LootTable(ISFSObject data, IDbWrapper db) throws Exception {
      super(data);

      try {
         String json = data.getUtfString("loot");
         ISFSArray lootEntries = SFSArray.newFromJsonData(json);

         for(int i = 0; i < lootEntries.size(); ++i) {
            this.loot.add(new LootTableEntry(lootEntries.getSFSObject(i), db));
         }

      } catch (Exception var6) {
         Logger.trace("Error initializing loot table id: " + this.getId());
         throw var6;
      }
   }

   public int getId() {
      return this.data.getInt("id");
   }

   public List<LootResult> pull(Random rng, int amount, int maxLevel) {
      List<LootResult> results = new ArrayList();

      for(int i = 0; i < amount; ++i) {
         float totalProbability = 0.0F;

         LootTableEntry li;
         for(Iterator var7 = this.loot.iterator(); var7.hasNext(); totalProbability += li.probability) {
            li = (LootTableEntry)var7.next();
         }

         float roll = rng.nextFloat();
         float currentProbability = 0.0F;
         Iterator var9 = this.loot.iterator();

         while(var9.hasNext()) {
            LootTableEntry li = (LootTableEntry)var9.next();
            currentProbability += li.probability;
            if (roll <= currentProbability / totalProbability) {
               List<LootResult> pullResults = li.loot.pull(rng, 1, maxLevel);
               results = LootResult.Merge((List)results, pullResults);
               break;
            }
         }
      }

      return (List)results;
   }
}
