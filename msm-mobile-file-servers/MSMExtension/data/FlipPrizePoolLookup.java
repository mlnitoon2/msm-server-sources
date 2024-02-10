package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class FlipPrizePoolLookup {
   private static FlipPrizePoolLookup instance;
   private int resetTimeHour;
   private int resetTimeMin;
   private int minPlayerScalingLevel = 10;
   private int maxPlayerScalingLevel = 75;
   private ConcurrentHashMap<Integer, FlipPrizePool> prizePools;

   public static FlipPrizePoolLookup getInstance() {
      return instance;
   }

   public int getResetTimeHour() {
      return this.resetTimeHour;
   }

   public int getResetTimeMin() {
      return this.resetTimeMin;
   }

   public int getMinPlayerScalingLevel() {
      return this.minPlayerScalingLevel;
   }

   public int getMaxPlayerScalingLevel() {
      return this.maxPlayerScalingLevel;
   }

   private FlipPrizePoolLookup(IDbWrapper db) throws Exception {
      String resetTime = GameSettings.get("FLIP_MINIGAME_RESET_TIME_UTC_24H");
      int col = resetTime.indexOf(":");
      this.resetTimeHour = Integer.parseInt(resetTime.substring(0, col));
      this.resetTimeMin = Integer.parseInt(resetTime.substring(col + 1, resetTime.length()));
      this.minPlayerScalingLevel = GameSettings.getInt("FLIP_MINIGAME_SCALING_MIN_LEVEL");
      this.maxPlayerScalingLevel = GameSettings.getInt("FLIP_MINIGAME_SCALING_MAX_LEVEL");
      this.prizePools = new ConcurrentHashMap();
      String sql = "SELECT id,consumeable,is_top_prize,fallback,rewards FROM memory_flip_prize_pools";
      ISFSArray results = db.query("SELECT id,consumeable,is_top_prize,fallback,rewards FROM memory_flip_prize_pools");
      Iterator i = results.iterator();

      while(i.hasNext()) {
         SFSObject prizePoolData = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         if (prizePoolData != null) {
            FlipPrizePool pool = new FlipPrizePool(prizePoolData);
            this.prizePools.put(pool.getId(), pool);
         }
      }

   }

   public static void init(IDbWrapper db) throws Exception {
      instance = new FlipPrizePoolLookup(db);
   }

   public Iterator<FlipPrizePool> iterator() {
      return this.prizePools.values().iterator();
   }

   public FlipPrizePool getEntry(int id) {
      return (FlipPrizePool)this.prizePools.get(id);
   }
}
