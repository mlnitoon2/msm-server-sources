package com.bigbluebubble.mysingingmonsters.data.loot;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class LootSet implements ILootable {
   public static final String SQL_KEY = "sql";
   public static final String IDS_KEY = "ids";
   public static final String SET_TYPE_KEY = "set";
   public static final String MIN_KEY = "min";
   public static final String MAX_KEY = "max";
   public static final String KEY_KEY = "key";
   public static final String EXTRA_KEY = "extra";
   public static final String DEFAULT_KEY = "default";
   LootType type;
   int min = 1;
   int max = 1;
   String sql;
   List<Integer> ids;
   ISFSObject extra;
   ILootSetFilter filter;
   int defaultItem = 0;

   public LootSet(ISFSObject sfsObject, IDbWrapper db) throws Exception {
      this.type = LootType.valueOf(sfsObject.getUtfString("set"));
      switch(this.type) {
      case COSTUME:
         this.filter = new LootSetCostumeFilter();
         break;
      case MONSTER:
         this.filter = new LootSetMonsterFilter();
         break;
      case STRUCTURE:
         this.filter = new LootSetStructureFilter();
      }

      if (sfsObject.containsKey("min")) {
         this.min = sfsObject.getInt("min");
      }

      if (sfsObject.containsKey("max")) {
         this.max = sfsObject.getInt("max");
      }

      assert this.max >= this.min;

      if (sfsObject.containsKey("extra")) {
         this.extra = sfsObject.getSFSObject("extra");
      }

      if (sfsObject.containsKey("sql")) {
         this.ids = new ArrayList();
         String sql = sfsObject.getUtfString("sql");
         ISFSArray results = null;
         Logger.trace(LogLevel.DEBUG, "Querying for LootSet: " + sql);
         results = db.query(sql);
         Iterator i = results.iterator();

         while(i.hasNext()) {
            SFSObject row = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
            Integer id = row.getInt("id");
            if (id == null && sfsObject.containsKey("key")) {
               id = row.getInt(sfsObject.getUtfString("key"));
            }

            if (id == null) {
               throw new Exception("Query result with no id!");
            }

            this.ids.add(id);
         }

         if (this.ids.isEmpty()) {
            throw new Exception("Set query returned empty result!!!");
         }

         Logger.trace(LogLevel.DEBUG, "LootSet Results:" + this.ids.toString());
      } else {
         this.ids = new ArrayList(sfsObject.getIntArray("ids"));
      }

      if (sfsObject.containsKey("default")) {
         this.defaultItem = sfsObject.getInt("default");
      }

   }

   public List<LootResult> pull(Random rng, int amount, int maxLevel) {
      List<LootResult> results = new ArrayList();
      List<Integer> filteredIds = this.ids;
      if (this.filter != null) {
         filteredIds = this.filter.filterList(this.ids, maxLevel);
      }

      for(int i = 0; i < amount; ++i) {
         int id = this.defaultItem;
         int a;
         if (filteredIds.size() > 0) {
            a = rng.nextInt(filteredIds.size());
            id = (Integer)filteredIds.get(a);
         }

         if (id > 0) {
            a = this.min + (this.max - this.min > 0 ? rng.nextInt(this.max - this.min) : 0);
            if (a > 0) {
               LootResult result = null;

               for(int j = 0; j < results.size(); ++j) {
                  LootResult r = (LootResult)results.get(j);
                  if (r.id == id) {
                     result = r;
                     break;
                  }
               }

               if (result == null) {
                  result = new LootResult(this.type, id, 0, this.extra);
                  results.add(result);
               }

               result.amount += a;
            }
         }
      }

      return results;
   }
}
