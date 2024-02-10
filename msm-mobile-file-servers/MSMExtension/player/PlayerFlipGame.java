package com.bigbluebubble.mysingingmonsters.player;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.data.FlipLevelLookup;
import com.bigbluebubble.mysingingmonsters.data.FlipPrizePool;
import com.bigbluebubble.mysingingmonsters.data.FlipPrizePoolLookup;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Random;

public class PlayerFlipGame {
   private final String LAST_FREE_PLAY_KEY = "last_free_play";
   private final String PRIZE_CLAIMED_KEY = "prize_claimed";
   private final String CONSUMED_PRIZES_KEY = "consumed_prizes";
   private final String DIAMONDS_SINCE_RESET_KEY = "diamonds_since_reset";
   private final String LAST_PRIZE_RESET_KEY = "last_prize_reset";
   private final String LAST_LEVEL_PLAYED_KEY = "last_level_played";
   private static Random rand = new Random();
   private Long lastFreePlay = null;
   private int lastLevelPlayed = 0;
   private int endgamePrizeLevel = 0;
   private Timestamp lastPrizeReset = null;
   private int diamondsSinceReset = 0;
   private ISFSArray consumedUnscaledPrizePools;
   private int selectedFromPool = 0;
   private ISFSObject unscaledEmbeddedPrizeSelected = null;
   private ISFSObject scaledEmbeddedPrizeSelected = null;
   private int diamondLevel = -1;
   private boolean deleteOldRecord = false;
   private boolean createNewRecord = false;
   private boolean isDirty_ = false;

   public boolean isDirty() {
      return this.isDirty_;
   }

   public PlayerFlipGame(IDbWrapper db, long playerId) {
      try {
         String sql = "SELECT * FROM user_flip_minigame_new WHERE user=?";
         ISFSArray records = db.query(sql, new Object[]{playerId});
         if (records.size() == 0) {
            sql = "SELECT * FROM user_flip_mini_game WHERE user=?";
            records = MSMExtension.getInstance().getDB().query(sql, new Object[]{playerId});
            this.deleteOldRecord = true;
            this.createNewRecord = true;
         }

         if (records.size() > 0) {
            ISFSObject record = records.getSFSObject(0);
            if (record.containsKey("last_level_played")) {
               this.lastLevelPlayed = record.getInt("last_level_played");
               this.lastFreePlay = record.getLong("last_free_play");
               if (this.consumedUnscaledPrizePools == null) {
                  if (record.containsKey("consumed_prizes")) {
                     String consumedStr = record.getUtfString("consumed_prizes");
                     if (consumedStr != null) {
                        this.consumedUnscaledPrizePools = SFSArray.newFromJsonData(consumedStr);
                     }
                  }

                  if (this.consumedUnscaledPrizePools == null) {
                     this.consumedUnscaledPrizePools = new SFSArray();
                  }

                  this.setDiamondsSinceReset(record.getInt("diamonds_since_reset"));
                  this.isDirty_ = true;
               }

               if (record.containsKey("last_prize_reset")) {
                  this.lastPrizeReset = new Timestamp(record.getLong("last_prize_reset"));
               }
            } else {
               if (!record.containsKey("prize_claimed")) {
                  throw new Exception("invalid user_flip table data");
               }

               this.lastLevelPlayed = record.getInt("prize_claimed") == 0 ? 1 : 0;
               this.lastFreePlay = record.getLong("last_free_play");
               this.consumedUnscaledPrizePools = new SFSArray();
               this.setDiamondsSinceReset(0);
               this.lastPrizeReset = new Timestamp(this.lastFreePlay);
            }
         } else {
            this.lastFreePlay = null;
            this.lastLevelPlayed = FlipLevelLookup.getInstance().firstLevel();
            this.lastPrizeReset = null;
            this.consumedUnscaledPrizePools = new SFSArray();
            this.createNewRecord = true;
         }
      } catch (Exception var8) {
         Logger.trace(var8);
      }

   }

   public void save(long playerId) {
      try {
         String sql;
         if (this.deleteOldRecord) {
            sql = "DELETE FROM user_flip_mini_game WHERE user=?";
            MSMExtension.getInstance().getDB().update(sql, new Object[]{playerId});
            this.deleteOldRecord = false;
         }

         if (this.lastFreePlay != null) {
            Timestamp freePlay = new Timestamp(this.lastFreePlay);
            if (this.createNewRecord) {
               if (this.consumedUnscaledPrizePools == null) {
                  this.consumedUnscaledPrizePools = new SFSArray();
               }

               sql = "INSERT INTO user_flip_minigame_new SET user=?, last_free_play=?, consumed_prizes=?, diamonds_since_reset=?, last_prize_reset=?, last_level_played=?";
               MSMExtension.getInstance().getDB().update(sql, new Object[]{playerId, freePlay.toString(), this.consumedUnscaledPrizePools.toJson(), this.diamondsSinceReset, this.lastPrizeReset.toString(), this.lastLevelPlayed});
               this.createNewRecord = false;
            } else {
               sql = "UPDATE user_flip_minigame_new SET last_free_play=?, consumed_prizes=?, diamonds_since_reset=?, last_prize_reset=?, last_level_played=? WHERE user=?";
               MSMExtension.getInstance().getDB().update(sql, new Object[]{freePlay.toString(), this.consumedUnscaledPrizePools.toJson(), this.diamondsSinceReset, this.lastPrizeReset.toString(), this.lastLevelPlayed, playerId});
            }
         }

         this.isDirty_ = false;
      } catch (Exception var5) {
         Logger.trace(var5);
      }

   }

   public int lastLevelPlayed() {
      return this.lastLevelPlayed;
   }

   public Long lastFreePlay() {
      return this.lastFreePlay;
   }

   public void resetLastFreePlay() {
      this.lastFreePlay = MSMExtension.CurrentDBTime();
      this.isDirty_ = true;
   }

   public boolean timeForFlipgamePrizeReset() {
      if (this.lastPrizeReset == null) {
         return true;
      } else {
         GregorianCalendar todaysReset = new GregorianCalendar();
         todaysReset.set(11, FlipPrizePoolLookup.getInstance().getResetTimeHour());
         todaysReset.set(12, FlipPrizePoolLookup.getInstance().getResetTimeMin());
         todaysReset.set(13, 0);
         todaysReset.set(14, 0);
         Timestamp todaysResetT = new Timestamp(todaysReset.getTimeInMillis());
         GregorianCalendar previousReset = (GregorianCalendar)todaysReset.clone();
         previousReset.add(11, -24);
         Timestamp previousResetT = new Timestamp(previousReset.getTimeInMillis());
         if (this.lastPrizeReset.before(previousResetT)) {
            return true;
         } else {
            return todaysResetT.before(new Timestamp(MSMExtension.CurrentDBTime())) && todaysResetT.after(this.lastPrizeReset);
         }
      }
   }

   public void resetLastPrizeReset() {
      this.lastPrizeReset = new Timestamp(MSMExtension.CurrentDBTime());
      this.resetConsumedPrizePools();
      this.isDirty_ = true;
   }

   private void setDiamondsSinceReset(int diamonds) {
      this.diamondsSinceReset = diamonds;
      this.isDirty_ = true;
   }

   public int diamondLevel() {
      return this.diamondLevel;
   }

   public void startNewGame() {
      this.lastLevelPlayed = FlipLevelLookup.getInstance().firstLevel();
      this.selectNewDiamondLevel();
      this.isDirty_ = true;
   }

   public ISFSObject scaledEmbeddedPrizeSelected() {
      return this.scaledEmbeddedPrizeSelected;
   }

   private void resetConsumedPrizePools() {
      this.consumedUnscaledPrizePools = new SFSArray();
      this.diamondsSinceReset = 0;
      this.diamondLevel = -1;
      this.isDirty_ = true;
   }

   public void collectLevel() {
      FlipPrizePool p = FlipPrizePoolLookup.getInstance().getEntry(this.selectedFromPool);
      if (p.isConsumeable()) {
         boolean foundPool = false;

         for(int i = 0; i < this.consumedUnscaledPrizePools.size(); ++i) {
            ISFSObject currentPool = this.consumedUnscaledPrizePools.getSFSObject(i);
            if (currentPool.getInt("pool") == this.selectedFromPool) {
               ISFSArray consumedArr = currentPool.getSFSArray("consumed");
               consumedArr.addSFSObject(this.unscaledEmbeddedPrizeSelected);
               currentPool.putSFSArray("consumed", consumedArr);
               foundPool = true;
               break;
            }
         }

         if (!foundPool) {
            ISFSObject consumedPoolSfs = new SFSObject();
            FlipPrizePool pool = FlipPrizePoolLookup.getInstance().getEntry(this.selectedFromPool);
            if (pool != null) {
               consumedPoolSfs.putInt("pool", this.selectedFromPool);
               ISFSArray consumedArr = new SFSArray();
               consumedArr.addSFSObject(this.unscaledEmbeddedPrizeSelected);
               consumedPoolSfs.putSFSArray("consumed", consumedArr);
               this.consumedUnscaledPrizePools.addSFSObject(consumedPoolSfs);
            }
         }
      }

      this.resetSelectedPrize();
      if (this.lastLevelPlayed == this.diamondLevel) {
         ++this.diamondsSinceReset;
         this.diamondLevel = -1;
      }

      this.isDirty_ = true;
   }

   public int endLevel() {
      ++this.lastLevelPlayed;
      this.endgamePrizeLevel = this.lastLevelPlayed;
      this.isDirty_ = true;
      if (this.lastLevelPlayed > FlipLevelLookup.getInstance().maxLevel()) {
         this.lastLevelPlayed = 0;
      }

      return this.lastLevelPlayed;
   }

   public int endgamePrizeLevelReached() {
      return this.endgamePrizeLevel;
   }

   public void endGame() {
      this.resetSelectedPrize();
      this.lastLevelPlayed = 0;
      this.endgamePrizeLevel = 0;
      this.isDirty_ = true;
   }

   private void resetSelectedPrize() {
      this.selectedFromPool = 0;
      this.unscaledEmbeddedPrizeSelected = null;
      this.scaledEmbeddedPrizeSelected = null;
      this.isDirty_ = true;
   }

   private void selectNewDiamondLevel() {
      if (!FlipLevelLookup.getInstance().disabledDiamondLevels()) {
         float chance = FlipLevelLookup.getInstance().getChanceOfDiamondReward(this.diamondsSinceReset);
         float r = rand.nextFloat();
         if (r < chance) {
            this.diamondLevel = rand.nextInt(FlipLevelLookup.getInstance().maxLevel()) + 1;
         } else {
            this.diamondLevel = -1;
         }

      }
   }

   public ISFSArray getRemainingScaledTopEmbeddedPrizes(int playerLevel) {
      ISFSArray remainingPrizePools = new SFSArray();
      Iterator prizePool = FlipPrizePoolLookup.getInstance().iterator();

      while(true) {
         FlipPrizePool curPrizePool;
         do {
            if (!prizePool.hasNext()) {
               return remainingPrizePools;
            }

            curPrizePool = (FlipPrizePool)prizePool.next();
         } while(!curPrizePool.isTopPrize());

         ISFSArray consumedPrizesForThisPool = null;

         for(int i = 0; i < this.consumedUnscaledPrizePools.size(); ++i) {
            ISFSObject pool = this.consumedUnscaledPrizePools.getSFSObject(i);
            if (pool.getInt("pool") == curPrizePool.getId()) {
               consumedPrizesForThisPool = pool.getSFSArray("consumed");
               break;
            }
         }

         ISFSObject rewardPool = new SFSObject();
         rewardPool.putInt("pool", curPrizePool.getId());
         ISFSArray unscaledRemaining = curPrizePool.getRemainingUnscaled(consumedPrizesForThisPool, playerLevel);
         ISFSArray scaledRemaining = new SFSArray();

         for(int i = 0; i < unscaledRemaining.size(); ++i) {
            ISFSObject unscaledPrize = unscaledRemaining.getSFSObject(i);
            int scaled = FlipPrizePool.scaleReward(playerLevel, Player.getCurrencyTypeFromString(unscaledPrize.getUtfString("type")), unscaledPrize.getInt("amt"), curPrizePool.getId());
            ISFSObject scaledPrize = new SFSObject();
            scaledPrize.putUtfString("type", unscaledPrize.getUtfString("type"));
            scaledPrize.putInt("amt", scaled);
            scaledRemaining.addSFSObject(scaledPrize);
         }

         rewardPool.putSFSArray("remaining", scaledRemaining);
         remainingPrizePools.addSFSObject(rewardPool);
      }
   }

   public ISFSObject selectEmbeddedLevelPrize(int flipLevelId, int playerLevel) {
      SFSObject levelData = FlipLevelLookup.getInstance().getLevelById(flipLevelId);
      int fallbackId = levelData.getInt("prize_pool");
      int prizePoolId = true;
      ISFSArray remainingPrizePool = null;

      int prizePoolId;
      do {
         prizePoolId = fallbackId;
         FlipPrizePool prizePool = FlipPrizePoolLookup.getInstance().getEntry(fallbackId);
         ISFSArray consumedPrizesForThisPool = null;

         for(int i = 0; i < this.consumedUnscaledPrizePools.size(); ++i) {
            ISFSObject pool = this.consumedUnscaledPrizePools.getSFSObject(i);
            if (pool.getInt("pool") == prizePoolId) {
               consumedPrizesForThisPool = pool.getSFSArray("consumed");
               break;
            }
         }

         remainingPrizePool = prizePool.getRemainingUnscaled(consumedPrizesForThisPool, playerLevel);
         fallbackId = prizePool.getFallback();
      } while(remainingPrizePool.size() == 0 && prizePoolId != -1);

      if (remainingPrizePool.size() != 0) {
         this.selectedFromPool = prizePoolId;
         this.unscaledEmbeddedPrizeSelected = remainingPrizePool.getSFSObject(rand.nextInt(remainingPrizePool.size()));
         this.updateScaledLevelEmbeddedPrize(playerLevel);
         if (this.scaledEmbeddedPrizeSelected != null) {
            this.scaledEmbeddedPrizeSelected.putInt("pool", this.selectedFromPool);
            return this.scaledEmbeddedPrizeSelected;
         }
      }

      return null;
   }

   private void updateScaledLevelEmbeddedPrize(int playerLevel) {
      if (this.unscaledEmbeddedPrizeSelected != null) {
         String type = this.unscaledEmbeddedPrizeSelected.getUtfString("type");
         Player.CurrencyType rewardType = Player.getCurrencyTypeFromString(type);
         if (rewardType != Player.CurrencyType.Undefined) {
            SFSObject scaledReward = new SFSObject();
            scaledReward.putUtfString("type", type);
            scaledReward.putInt("amt", FlipPrizePool.scaleReward(playerLevel, rewardType, this.unscaledEmbeddedPrizeSelected.getInt("amt"), this.selectedFromPool));
            this.scaledEmbeddedPrizeSelected = scaledReward;
         } else {
            Logger.trace(LogLevel.WARN, "UNSUPPORTED MEMORY FLIP REWARD TYPE: " + type);
            this.scaledEmbeddedPrizeSelected = null;
         }
      } else {
         this.scaledEmbeddedPrizeSelected = null;
      }

   }
}
