package com.bigbluebubble.mysingingmonsters.battle;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticDataLookup;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BattleMonsterStatLookup extends StaticDataLookup<BattleMonsterStatData> {
   public static BattleMonsterStatLookup instance;
   private Map<Integer, BattleMonsterStatData> battleMonsterStats_ = new HashMap();
   static final String CACHE_NAME = "battle_monster_stats_data";

   private BattleMonsterStatLookup(IDbWrapper db) throws Exception {
      this.lastChanged_ = 0L;
      String sql = "SELECT * from battle_monster_stats";
      ISFSArray results = db.query(sql);

      BattleMonsterStatData battleMonsterStatData;
      for(Iterator i = results.iterator(); i.hasNext(); this.lastChanged_ = Math.max(this.lastChanged_, battleMonsterStatData.lastChanged())) {
         battleMonsterStatData = new BattleMonsterStatData((ISFSObject)((SFSDataWrapper)i.next()).getObject());
         this.battleMonsterStats_.put(battleMonsterStatData.getMonsterId(), battleMonsterStatData);
      }

   }

   public static void init(IDbWrapper db) throws Exception {
      instance = new BattleMonsterStatLookup(db);
   }

   public static BattleMonsterStatData get(int id) {
      return instance.getEntry(id);
   }

   public String getCacheName() {
      return "battle_monster_stats_data";
   }

   public Iterable<BattleMonsterStatData> entries() {
      return this.battleMonsterStats_.values();
   }

   public BattleMonsterStatData getEntry(int id) {
      return (BattleMonsterStatData)this.battleMonsterStats_.get(id);
   }
}
