package com.bigbluebubble.mysingingmonsters.battle;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.entities.data.SFSArray;
import java.util.concurrent.TimeUnit;

public final class BattleSeasons implements Runnable {
   private IDbWrapper _db;
   private static volatile String _seasons = "[]";

   private BattleSeasons(IDbWrapper db) {
      this._db = db;
   }

   private void refresh() {
      try {
         String GET_SEASONS_SQL = "SELECT DISTINCT campaign_id, schedule_started_on FROM user_battle_pvp ORDER BY schedule_started_on DESC LIMIT 2";
         _seasons = this._db.query(GET_SEASONS_SQL).toJson();
      } catch (Exception var2) {
         Logger.trace(var2, "Unable to retrieve recent battle seasons");
      }

   }

   public void run() {
      this.refresh();
   }

   public static void init(IDbWrapper db, int refreshInterval) throws Exception {
      if (refreshInterval <= 0) {
         (new BattleSeasons(db)).run();
      } else {
         SmartFoxServer.getInstance().getTaskScheduler().scheduleAtFixedRate(new BattleSeasons(db), 0, refreshInterval, TimeUnit.MINUTES);
      }

   }

   public static SFSArray getSeasons() {
      return SFSArray.newFromJsonData(_seasons);
   }
}
