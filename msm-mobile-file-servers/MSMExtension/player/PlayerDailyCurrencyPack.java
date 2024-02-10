package com.bigbluebubble.mysingingmonsters.player;

import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.data.DailyCurrencyPackLookup;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class PlayerDailyCurrencyPack {
   public static final String ID_KEY = "user";
   public static final String PACK_ID_KEY = "pack_id";
   public static final String DAILY_AMOUNT_KEY = "amount";
   public static final String DAILY_CURRENCY_KEY = "currency";
   public static final String DAYS_COLLECTED_KEY = "days_collected";
   public static final String DAYS_MISSED_KEY = "days_missed";
   public static final String REDEEMED_TODAY_KEY = "redeemed_today";
   public static final String REWARD_REFRESHES_KEY = "reward_refreshes";
   public static final String LAST_CLAIMED_ON_KEY = "last_claimed_on";
   public static final String EXPIRES_ON_KEY = "expires";
   public static final String COOLDOWN_EXPIRES_ON_KEY = "cooldown_expires";
   public static final String REFRESH_PERIOD_KEY = "refresh_period";
   public static final String LAST_PROCESSED_REFRESH = "last_processed_refresh";
   protected long user_id;
   protected int pack_id;
   protected int days_collected;
   protected int days_missed;
   protected long last_claimed_on;
   protected long expires;
   protected long cooldown_expires;
   protected long last_processed_refresh;
   protected int amount;
   protected String currency;
   protected boolean redeemed_today;
   protected long reward_refreshes;
   private boolean is_dirty;

   public PlayerDailyCurrencyPack(ISFSObject data) {
      this.initFromSFSObject(data);
   }

   public void initFromSFSObject(ISFSObject data) {
      this.user_id = data.getLong("user");
      this.pack_id = data.getInt("pack_id");
      this.days_collected = data.getInt("days_collected");
      this.days_missed = data.getInt("days_missed");
      this.last_claimed_on = data.getLong("last_claimed_on");
      this.reward_refreshes = data.getLong("reward_refreshes");
      this.expires = data.getLong("expires");
      this.last_processed_refresh = data.getLong("last_processed_refresh");
      if (data.containsKey("cooldown_expires")) {
         this.cooldown_expires = data.getLong("cooldown_expires");
      } else {
         this.cooldown_expires = this.expires + (long)DailyCurrencyPackLookup.get(this.pack_id).getCooldown();
      }

      this.amount = DailyCurrencyPackLookup.get(this.pack_id).getAmount();
      this.currency = DailyCurrencyPackLookup.get(this.pack_id).getCurrency();
      this.calculateRewardRefresh();
      this.redeemed_today = this.hasPackBeenClaimedToday();
      this.updateDaysMissed();
   }

   public ISFSObject getData() {
      return this.toSFSObject();
   }

   public ISFSObject toSFSObject() {
      ISFSObject s = new SFSObject();
      s.putLong("user", this.getID());
      s.putInt("pack_id", this.getPackID());
      s.putInt("days_collected", this.getDaysCollected());
      s.putInt("days_missed", this.getDaysMissed());
      s.putBool("redeemed_today", this.getRedeemedToday());
      s.putInt("amount", this.amount);
      s.putUtfString("currency", this.currency);
      s.putLong("reward_refreshes", this.getRewardRefresh());
      s.putInt("refresh_period", DailyCurrencyPackLookup.get(this.pack_id).getRefresh());
      s.putLong("last_claimed_on", this.getLastClaimed());
      s.putLong("expires", this.getExpiry());
      s.putLong("cooldown_expires", this.getCooldownExpiry());
      return s;
   }

   public String toString() {
      return this.toSFSObject().getDump();
   }

   public long getID() {
      return this.user_id;
   }

   public int getPackID() {
      return this.pack_id;
   }

   public int getDaysCollected() {
      return this.days_collected;
   }

   public int getDaysMissed() {
      this.updateDaysMissed();
      return this.days_missed;
   }

   public boolean getRedeemedToday() {
      return this.redeemed_today;
   }

   public long getRewardRefresh() {
      return this.reward_refreshes;
   }

   public long getLastClaimed() {
      return this.last_claimed_on;
   }

   public long getExpiry() {
      return this.expires;
   }

   public long getCooldownExpiry() {
      return this.cooldown_expires;
   }

   public long getLastProcessedRefresh() {
      return this.last_processed_refresh;
   }

   public boolean isDirty() {
      return this.is_dirty;
   }

   public void setDirty(boolean dirty) {
      this.is_dirty = dirty;
   }

   public boolean hasPackBeenClaimedToday() {
      this.calculateRewardRefresh();
      long refreshSpan = (long)DailyCurrencyPackLookup.get(this.pack_id).getRefresh();
      refreshSpan *= DailyCurrencyPackLookup.get(this.pack_id).getRefreshUnit();
      return this.last_claimed_on > this.reward_refreshes - refreshSpan;
   }

   public void calculateRewardRefresh() {
      long refresh = (long)DailyCurrencyPackLookup.get(this.pack_id).getRefresh();

      for(refresh *= DailyCurrencyPackLookup.get(this.pack_id).getRefreshUnit(); this.reward_refreshes < MSMExtension.CurrentDBTime(); this.reward_refreshes += refresh) {
      }

   }

   public void updateDaysMissed() {
      long periodLengthMs = (long)DailyCurrencyPackLookup.get(this.pack_id).getRefresh() * DailyCurrencyPackLookup.get(this.pack_id).getRefreshUnit();
      long startTime = this.expires - DailyCurrencyPackLookup.get(this.pack_id).getRefreshUnit() * (long)DailyCurrencyPackLookup.get(this.pack_id).getDuration();
      long elapsedMs = MSMExtension.CurrentDBTime() - startTime;
      int periodsElapsed = (int)(elapsedMs / periodLengthMs);
      if (this.redeemed_today) {
         ++periodsElapsed;
      }

      this.days_missed = periodsElapsed - this.days_collected;
      if (this.days_missed < 0) {
         this.days_missed = 0;
      }

   }

   public boolean hasExpired() {
      return MSMExtension.CurrentDBTime() > this.getExpiry();
   }

   public boolean hasCooldownExpired() {
      return MSMExtension.CurrentDBTime() > this.getCooldownExpiry();
   }

   public void claimDailyReward() {
      this.redeemed_today = true;
      this.last_claimed_on = MSMExtension.CurrentDBTime();
      ++this.days_collected;
      this.is_dirty = true;
   }

   public void refreshPack() {
      this.calculateRewardRefresh();
      this.updateDaysMissed();
      this.redeemed_today = this.hasPackBeenClaimedToday();
      this.is_dirty = true;
   }
}
