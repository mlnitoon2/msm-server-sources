package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticDataLookup;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MonsterFlexEggDefLookup extends StaticDataLookup<MonsterFlexEggDef> {
   private static MonsterFlexEggDefLookup instance;
   private static Map<Integer, MonsterFlexEggDef> eggDefs_;
   static final String CACHE_NAME = "flex_egg_def_data";

   public static MonsterFlexEggDefLookup getInstance() {
      return instance;
   }

   public static void init(IDbWrapper db) throws Exception {
      instance = new MonsterFlexEggDefLookup(db);
   }

   private MonsterFlexEggDefLookup(IDbWrapper db) throws Exception {
      eggDefs_ = new ConcurrentHashMap();
      String sql = "SELECT * FROM monster_flexegg_defs";
      ISFSArray results = db.query(sql);
      Iterator i = results.iterator();

      while(i.hasNext()) {
         SFSObject row = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         MonsterFlexEggDef def = new MonsterFlexEggDef(row);
         eggDefs_.put(def.id(), def);
      }

   }

   public String getCacheName() {
      return "flex_egg_def_data";
   }

   public Iterable<MonsterFlexEggDef> entries() {
      return eggDefs_.values();
   }

   public MonsterFlexEggDef getEntry(int id) {
      return (MonsterFlexEggDef)eggDefs_.get(id);
   }
}
