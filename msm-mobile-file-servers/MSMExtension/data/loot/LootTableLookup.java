package com.bigbluebubble.mysingingmonsters.data.loot;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticDataLookup;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LootTableLookup extends StaticDataLookup<LootTable> {
   private static LootTableLookup instance;
   private Map<Integer, LootTable> lootTables_ = new ConcurrentHashMap();
   static final String CACHE_NAME = "loot_data";

   public static LootTableLookup getInstance() {
      return instance;
   }

   public static void init(IDbWrapper db) throws Exception {
      instance = new LootTableLookup(db);
   }

   public static LootTable get(int lootTableId) {
      return instance.getEntry(lootTableId);
   }

   private LootTableLookup(IDbWrapper db) throws Exception {
      String SELECT_LOOT_SQL = "SELECT * FROM loot_tables";
      ISFSArray results = db.query("SELECT * FROM loot_tables");
      Iterator i = results.iterator();

      while(i.hasNext()) {
         SFSObject lootTableData = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         if (lootTableData != null) {
            LootTable lootTable = new LootTable(lootTableData, db);
            this.lootTables_.put(lootTable.getId(), lootTable);
         }
      }

   }

   public String getCacheName() {
      return "loot_data";
   }

   public Iterable<LootTable> entries() {
      return this.lootTables_.values();
   }

   public LootTable getEntry(int id) {
      if (!this.lootTables_.containsKey(id)) {
         Logger.trace("Loot Table not found:" + id);
      }

      return (LootTable)this.lootTables_.get(id);
   }
}
