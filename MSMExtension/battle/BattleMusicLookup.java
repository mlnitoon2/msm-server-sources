package com.bigbluebubble.mysingingmonsters.battle;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticDataLookup;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BattleMusicLookup extends StaticDataLookup<BattleMusicData> {
   public static BattleMusicLookup instance;
   private Map<Integer, BattleMusicData> music_ = new HashMap();
   static final String CACHE_NAME = "battle_music_data";

   private BattleMusicLookup(IDbWrapper db) throws Exception {
      this.lastChanged_ = 0L;
      String sql = "SELECT * FROM battle_music";
      ISFSArray results = db.query(sql);

      BattleMusicData battleMusicData;
      for(Iterator i = results.iterator(); i.hasNext(); this.lastChanged_ = Math.max(this.lastChanged_, battleMusicData.lastChanged())) {
         ISFSObject data = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
         battleMusicData = new BattleMusicData(data);
         this.music_.put(battleMusicData.getId(), battleMusicData);
      }

   }

   public static void init(IDbWrapper db) throws Exception {
      instance = new BattleMusicLookup(db);
   }

   public static BattleMusicData get(int id) {
      return instance.getEntry(id);
   }

   public String getCacheName() {
      return "battle_music_data";
   }

   public Iterable<BattleMusicData> entries() {
      return this.music_.values();
   }

   public BattleMusicData getEntry(int id) {
      return (BattleMusicData)this.music_.get(id);
   }
}
