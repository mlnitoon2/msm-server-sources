package com.bigbluebubble.mysingingmonsters.battle;

import com.bigbluebubble.BBBServer.GameSettings;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class BattlePlayerVersusState {
   public static final String CAMPAIGN_KEY = "campaign_id";
   public static final String SCHEDULE_START_KEY = "schedule_started_on";
   public static final String TIER_KEY = "tier";
   public static final String STARS_KEY = "stars";
   public static final String STREAK_KEY = "streak";
   public static final String ATTEMPTS_KEY = "attempts";
   public static final String LOADOUT_KEY = "loadout";
   public static final String MATCH_SCORE_KEY = "match_score";
   public static final String REFRESHES_ON_KEY = "refreshes_on";
   public static final String STARTED_ON_KEY = "started_on";
   public static final String COMPLETED_ON_KEY = "completed_on";
   public static final String CLAIMED_ON_KEY = "claimed_on";
   public static final String CHAMPION_SCORE_KEY = "champion_score";
   public static final String LAST_BATTLE_OPPONENT_LOADOUT_KEY = "last_opponent_loadout";
   public static final String LAST_SEED_KEY = "last_seed";
   ISFSObject data_;
   BattleOpponents loadout_;
   BattleOpponents lastOpponentLoadout_;
   boolean isDirty_;
   public static final int DEFAULT_NUM_BATTLE_VERSUS_ATTEMPTS = 5;

   public BattlePlayerVersusState(int campaignId, long scheduleStart) {
      this.data_ = new SFSObject();
      this.data_.putInt("campaign_id", campaignId);
      this.data_.putLong("schedule_started_on", scheduleStart);
   }

   public BattlePlayerVersusState(ISFSObject data) {
      this.data_ = data;
      if (data.containsKey("loadout")) {
         this.loadout_ = new BattleOpponents(SFSArray.newFromJsonData(this.data_.getUtfString("loadout")));
      }

      if (data.containsKey("last_opponent_loadout")) {
         this.lastOpponentLoadout_ = new BattleOpponents(SFSArray.newFromJsonData(this.data_.getUtfString("last_opponent_loadout")));
      }

   }

   public ISFSObject toSFSObject() {
      this.data_.putUtfString("loadout", this.loadout_.toSFSArray().toJson());
      return this.data_;
   }

   public boolean isDirty() {
      return this.isDirty_;
   }

   public void setDirty(boolean dirty) {
      this.isDirty_ = dirty;
   }

   public int getCampaignId() {
      return this.data_.getInt("campaign_id");
   }

   public long getScheduleTimestamp() {
      return this.data_.getLong("schedule_started_on");
   }

   public int getTier() {
      return this.data_.getInt("tier");
   }

   public void setTier(int tier) {
      this.data_.putInt("tier", tier);
      this.isDirty_ = true;
   }

   public int getStars() {
      return this.data_.getInt("stars");
   }

   public void setStars(int stars) {
      this.data_.putInt("stars", stars);
      this.isDirty_ = true;
   }

   public int getStreak() {
      return this.data_.getInt("streak");
   }

   public void setStreak(int streak) {
      this.data_.putInt("streak", streak);
      this.isDirty_ = true;
   }

   public int getAttempts() {
      return this.data_.getInt("attempts");
   }

   public void setAttempts(int attempts) {
      this.data_.putInt("attempts", attempts);
      this.isDirty_ = true;
   }

   public BattleOpponents getLoadout() {
      return this.loadout_;
   }

   public void setLoadout(BattleOpponents loadout) {
      this.loadout_ = loadout;
      this.isDirty_ = true;
   }

   public int getMatchScore() {
      return this.data_.getInt("match_score");
   }

   public void setMatchScore(int matchScore) {
      this.data_.putInt("match_score", matchScore);
      this.isDirty_ = true;
   }

   public long getRefreshTimestamp() {
      return this.data_.containsKey("refreshes_on") ? this.data_.getLong("refreshes_on") : 0L;
   }

   public void setRefreshTimestamp(long ts) {
      this.data_.putLong("refreshes_on", ts);
      this.isDirty_ = true;
   }

   public long getStartedTimestamp() {
      return this.data_.containsKey("started_on") ? this.data_.getLong("started_on") : 0L;
   }

   public void setStartedTimestamp(long ts) {
      this.data_.putLong("started_on", ts);
      this.isDirty_ = true;
   }

   public long getCompletedTimestamp() {
      return this.data_.containsKey("completed_on") ? this.data_.getLong("completed_on") : 0L;
   }

   public void setCompletedTimestamp(long ts) {
      this.data_.putLong("completed_on", ts);
      this.isDirty_ = true;
   }

   public long getClaimedTimestamp() {
      return this.data_.containsKey("claimed_on") ? this.data_.getLong("claimed_on") : 0L;
   }

   public void setClaimedTimestamp(long ts) {
      this.data_.putLong("claimed_on", ts);
      this.isDirty_ = true;
   }

   public BattleOpponents getLastOpponentLoadout() {
      return this.lastOpponentLoadout_;
   }

   public void setLastOpponentLoadout(BattleOpponents loadout) {
      this.lastOpponentLoadout_ = loadout;
      this.isDirty_ = true;
   }

   public long getLastSeed() {
      return this.data_.containsKey("last_seed") ? this.data_.getLong("last_seed") : 0L;
   }

   public void setLastSeed(long seed) {
      this.data_.putLong("last_seed", seed);
      this.isDirty_ = true;
   }

   public int getChampionScore() {
      return this.data_.containsKey("champion_score") ? this.data_.getInt("champion_score") : 0;
   }

   public void setChampionScore(int rank) {
      this.data_.putInt("champion_score", rank);
      this.isDirty_ = true;
   }

   public void start(long startTime) {
      this.setStartedTimestamp(startTime);
      this.setTier(0);
      this.setStars(0);
      this.setStreak(0);
      this.setAttempts(GameSettings.get("NUM_BATTLE_VERSUS_ATTEMPTS", 5));
      this.setLoadout((BattleOpponents)null);
      this.setMatchScore(0);
      this.setRefreshTimestamp(GetNewDayTimestamp());
      this.setCompletedTimestamp(0L);
      this.setClaimedTimestamp(0L);
      this.setLastOpponentLoadout((BattleOpponents)null);
      this.setLastSeed(0L);
      this.setChampionScore(0);
   }

   public static long GetNewDayTimestamp() {
      Calendar midnightThisMorning = new GregorianCalendar();
      midnightThisMorning.set(11, 0);
      midnightThisMorning.set(12, 0);
      midnightThisMorning.set(13, 0);
      midnightThisMorning.set(14, 0);
      midnightThisMorning.add(5, 1);
      return midnightThisMorning.getTimeInMillis();
   }

   public boolean hasCompletedSeason() {
      return this.getCompletedTimestamp() > 0L;
   }
}
