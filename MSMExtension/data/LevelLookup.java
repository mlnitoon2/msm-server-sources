package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticDataLookup;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LevelLookup extends StaticDataLookup<Level> {
   private static LevelLookup instance;
   public static int maxLevel = 0;
   private Map<Integer, Level> levels_ = new ConcurrentHashMap();
   static final String CACHE_NAME = "level_data";

   public static LevelLookup getInstance() {
      return instance;
   }

   private LevelLookup(IDbWrapper db) throws Exception {
      this.lastChanged_ = 0L;
      String sql = "SELECT * FROM level_xp ORDER BY level ASC";
      ISFSArray results = db.query("SELECT * FROM level_xp ORDER BY level ASC");

      Level level;
      for(Iterator i = results.iterator(); i.hasNext(); this.lastChanged_ = Math.max(this.lastChanged_, level.lastChanged())) {
         SFSObject levelData = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         level = new Level(levelData);
         this.levels_.put(level.getLevel(), level);
      }

      maxLevel = this.levels_.size();
   }

   public static void init(IDbWrapper db) throws Exception {
      instance = new LevelLookup(db);
   }

   public static Level get(int level) {
      return instance.getEntry(level);
   }

   public String getCacheName() {
      return "level_data";
   }

   public Iterable<Level> entries() {
      return this.levels_.values();
   }

   public Level getEntry(int id) {
      return (Level)this.levels_.get(id);
   }
}
