package com.bigbluebubble.mysingingmonsters.player;

import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import java.util.Iterator;
import org.json.JSONObject;

public class PlayerActiveIslandStats {
   private int islandIndex = 0;
   private long bbbId = 0L;
   private long startDiamonds = 0L;
   private long startCoins = 0L;
   private long startShards = 0L;
   private long startFood = 0L;
   private long startRelics = 0L;
   private long spentDiamonds = 0L;
   private long spentKeys = 0L;
   private long spentCoins = 0L;
   private long spentShards = 0L;
   private long spentStarpower = 0L;
   private long spentRelics = 0L;
   private long spentMedals = 0L;
   private long spentEggWildcards = 0L;
   private long spentFood = 0L;
   private long spentSpeedUpCredit = 0L;
   private long earnedDiamonds = 0L;
   private long earnedKeys = 0L;
   private long earnedCoins = 0L;
   private long earnedShards = 0L;
   private long earnedStarpower = 0L;
   private long earnedFood = 0L;
   private long earnedRelics = 0L;
   private long earnedMedals = 0L;
   private long earnedEggWildcards = 0L;
   private long earnedSpeedUpCredit = 0L;
   private long sessionStartTime = 0L;
   private long startTime = 0L;
   private int startLevel = 0;
   private long tribeID = 0L;

   public PlayerActiveIslandStats(long bbb_id) {
      this.bbbId = bbb_id;
      this.sessionStartTime = MSMExtension.CurrentDBTime() / 1000L;
   }

   public void islandOpened(Player player) {
      try {
         if (player.getActiveIsland() == null) {
            return;
         }

         this.islandIndex = player.getActiveIsland().getIndex();
         this.startLevel = player.getLevel();
         this.startDiamonds = player.getActualDiamonds();
         this.startCoins = player.getActualCoins();
         this.startFood = player.getActualFood();
         this.startShards = player.getActualEthCurrency();
         this.tribeID = player.getActiveIsland().getTribalID();
         this.startTime = MSMExtension.CurrentDBTime() / 1000L;
         this.earnedDiamonds = 0L;
         this.earnedKeys = 0L;
         this.earnedCoins = 0L;
         this.earnedShards = 0L;
         this.earnedStarpower = 0L;
         this.earnedFood = 0L;
         this.earnedRelics = 0L;
         this.earnedMedals = 0L;
         this.earnedEggWildcards = 0L;
         this.earnedSpeedUpCredit = 0L;
         this.spentDiamonds = 0L;
         this.spentKeys = 0L;
         this.spentCoins = 0L;
         this.spentShards = 0L;
         this.spentStarpower = 0L;
         this.spentFood = 0L;
         this.spentRelics = 0L;
         this.spentMedals = 0L;
         this.spentSpeedUpCredit = 0L;
         this.spentEggWildcards = 0L;
      } catch (Exception var3) {
         Logger.trace(var3);
      }

   }

   public void islandClosed(Player player) {
      try {
         if (player.getActiveIsland() == null) {
            return;
         }

         int endLevel = player.getLevel();
         int monsterCount = player.getActiveIsland().getMonsters().size();
         int bedsAvailable = player.getActiveIsland().bedsAvailable();
         int monstersInStorage = player.getActiveIsland().getNumMonstersInStorage();
         int bedsInStorage = player.getActiveIsland().getNumMonsterBedsInStorage();
         JSONObject happyMonsterCounts = new JSONObject();
         Iterator monsters = player.getActiveIsland().getMonsters().iterator();

         while(monsters.hasNext()) {
            PlayerMonster curMonster = (PlayerMonster)monsters.next();
            String happyKey = "happy_" + curMonster.getHappiness();
            int happyCount = happyMonsterCounts.has(happyKey) ? happyMonsterCounts.getInt(happyKey) : 0;
            happyMonsterCounts.put(happyKey, happyCount + 1);
         }

         JSONObject params = new JSONObject();
         params.put("bbb_id", this.bbbId);
         params.put("island_id", this.islandIndex);
         params.put("session_start", this.sessionStartTime);
         params.put("start_time", this.startTime);
         params.put("end_time", MSMExtension.CurrentDBTime() / 1000L);
         params.put("start_level", this.startLevel);
         params.put("end_level", endLevel);
         params.put("start_diamonds", this.startDiamonds);
         params.put("start_coins", this.startCoins);
         params.put("start_shards", this.startShards);
         params.put("start_food", this.startFood);
         params.put("start_relics", this.startRelics);
         params.put("monster_count", monsterCount);
         params.put("beds_available", bedsAvailable);
         params.put("monsters_in_storage", monstersInStorage);
         params.put("beds_in_storage", bedsInStorage);
         if (happyMonsterCounts.length() > 0) {
            params.put("happy_monster_counts", happyMonsterCounts);
         }

         if (this.spentDiamonds > 0L) {
            params.put("spent_diamonds", this.spentDiamonds);
         }

         if (this.spentKeys > 0L) {
            params.put("spent_keys", this.spentKeys);
         }

         if (this.spentCoins > 0L) {
            params.put("spent_coins", this.spentCoins);
         }

         if (this.spentShards > 0L) {
            params.put("spent_shards", this.spentShards);
         }

         if (this.spentFood > 0L) {
            params.put("spent_food", this.spentFood);
         }

         if (this.spentStarpower > 0L) {
            params.put("spent_starpower", this.spentStarpower);
         }

         if (this.spentRelics > 0L) {
            params.put("spent_relics", this.spentRelics);
         }

         if (this.spentEggWildcards > 0L) {
            params.put("spent_eggWildcards", this.spentEggWildcards);
         }

         if (this.spentMedals > 0L) {
            params.put("spent_medals", this.spentMedals);
         }

         if (this.spentSpeedUpCredit > 0L) {
            params.put("spent_speedUpCredit", this.spentSpeedUpCredit);
         }

         if (this.earnedDiamonds > 0L) {
            params.put("earned_diamonds", this.earnedDiamonds);
         }

         if (this.earnedKeys > 0L) {
            params.put("earned_keys", this.earnedKeys);
         }

         if (this.earnedCoins > 0L) {
            params.put("earned_coins", this.earnedCoins);
         }

         if (this.earnedShards > 0L) {
            params.put("earned_shards", this.earnedShards);
         }

         if (this.earnedStarpower > 0L) {
            params.put("earned_starpower", this.earnedStarpower);
         }

         if (this.earnedFood > 0L) {
            params.put("earned_food", this.earnedFood);
         }

         if (this.earnedRelics > 0L) {
            params.put("earned_relics", this.earnedRelics);
         }

         if (this.earnedEggWildcards > 0L) {
            params.put("earned_eggWildcards", this.earnedEggWildcards);
         }

         if (this.earnedMedals > 0L) {
            params.put("earned_medals", this.earnedMedals);
         }

         if (this.earnedSpeedUpCredit > 0L) {
            params.put("earned_speedUpCredit", this.earnedSpeedUpCredit);
         }

         if (this.tribeID > 0L) {
            params.put("tribe_id", this.tribeID);
         }

         MSMExtension.getInstance().stats.trackActiveIslandEvent(params);
      } catch (Exception var12) {
         Logger.trace(var12);
      }

   }

   public void diamondChange(int amount) {
      if (amount > 0) {
         this.earnedDiamonds += (long)amount;
      } else {
         this.spentDiamonds -= (long)amount;
      }

   }

   public void keysChange(int amount) {
      if (amount > 0) {
         this.earnedKeys += (long)amount;
      } else {
         this.spentKeys -= (long)amount;
      }

   }

   public void relicsChange(int amount) {
      if (amount > 0) {
         this.earnedRelics += (long)amount;
      } else {
         this.spentRelics -= (long)amount;
      }

   }

   public void eggWildcardsChange(int amount) {
      if (amount > 0) {
         this.earnedEggWildcards += (long)amount;
      } else {
         this.spentEggWildcards -= (long)amount;
      }

   }

   public void coinChange(int amount) {
      if (amount > 0) {
         this.earnedCoins += (long)amount;
      } else {
         this.spentCoins -= (long)amount;
      }

   }

   public void ethCurrencyChange(int amount) {
      if (amount > 0) {
         this.earnedShards += (long)amount;
      } else {
         this.spentShards -= (long)amount;
      }

   }

   public void starpowerCurrencyChange(long amount) {
      if (amount > 0L) {
         this.earnedStarpower += amount;
      } else {
         this.spentStarpower -= amount;
      }

   }

   public void medalsChange(long amount) {
      if (amount > 0L) {
         this.earnedMedals += amount;
      } else {
         this.spentMedals -= amount;
      }

   }

   public void foodChange(int amount) {
      if (amount > 0) {
         this.earnedFood += (long)amount;
      } else {
         this.spentFood -= (long)amount;
      }

   }

   public void speedUpCreditChange(int amount) {
      if (amount > 0) {
         this.earnedSpeedUpCredit += (long)amount;
      } else {
         this.spentSpeedUpCredit -= (long)amount;
      }

   }
}
