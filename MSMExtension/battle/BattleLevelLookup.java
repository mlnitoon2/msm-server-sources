package com.bigbluebubble.mysingingmonsters.battle;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticDataLookup;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BattleLevelLookup extends StaticDataLookup<BattleLevel> {
   public static BattleLevelLookup instance;
   private int maxLevel = 0;
   private Map<Integer, BattleLevel> levels_ = new HashMap();
   static final String CACHE_NAME = "battle_level_data";

   private BattleLevelLookup(IDbWrapper db) throws Exception {
      this.lastChanged_ = 0L;
      String sql = "SELECT * FROM battle_level_xp ORDER BY level ASC";
      ISFSArray results = db.query(sql);

      BattleLevel level;
      for(Iterator i = results.iterator(); i.hasNext(); this.lastChanged_ = Math.max(this.lastChanged_, level.lastChanged())) {
         ISFSObject levelData = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
         level = new BattleLevel(levelData);
         this.levels_.put(level.getLevel(), level);
      }

      this.maxLevel = results.size();
   }

   public static void init(IDbWrapper db) throws Exception {
      instance = new BattleLevelLookup(db);
   }

   public static BattleLevel get(int level) {
      return instance.getEntry(level);
   }

   public static int getMaxLevel() {
      return instance.maxLevel;
   }

   public String getCacheName() {
      return "battle_level_data";
   }

   public Iterable<BattleLevel> entries() {
      return this.levels_.values();
   }

   public BattleLevel getEntry(int level) {
      return (BattleLevel)this.levels_.get(level);
   }
}
