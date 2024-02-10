package com.bigbluebubble.mysingingmonsters.battle;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticDataLookup;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BattleMonsterActionLookup extends StaticDataLookup<BattleMonsterActionData> {
   public static BattleMonsterActionLookup instance;
   private Map<Integer, BattleMonsterActionData> battleMonsterActions_ = new HashMap();
   static final String CACHE_NAME = "battle_monster_actions_data";

   private BattleMonsterActionLookup(IDbWrapper db) throws Exception {
      this.lastChanged_ = 0L;
      String sql = "SELECT * from battle_monster_actions";
      ISFSArray results = db.query(sql);

      BattleMonsterActionData battleMonsterActionData;
      for(Iterator i = results.iterator(); i.hasNext(); this.lastChanged_ = Math.max(this.lastChanged_, battleMonsterActionData.lastChanged())) {
         battleMonsterActionData = new BattleMonsterActionData((ISFSObject)((SFSDataWrapper)i.next()).getObject());
         this.battleMonsterActions_.put(battleMonsterActionData.getId(), battleMonsterActionData);
      }

   }

   public static void init(IDbWrapper db) throws Exception {
      instance = new BattleMonsterActionLookup(db);
   }

   public static BattleMonsterActionData get(int id) {
      return instance.getEntry(id);
   }

   public String getCacheName() {
      return "battle_monster_actions_data";
   }

   public Iterable<BattleMonsterActionData> entries() {
      return this.battleMonsterActions_.values();
   }

   public BattleMonsterActionData getEntry(int id) {
      return (BattleMonsterActionData)this.battleMonsterActions_.get(id);
   }
}
