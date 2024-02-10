package com.bigbluebubble.mysingingmonsters.player;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.GameStateHandler;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.data.DailyCumulativeLoginCalendar;
import com.bigbluebubble.mysingingmonsters.data.DailyCumulativeLoginLookup;
import com.bigbluebubble.mysingingmonsters.data.DailyCumulativeLoginReward;
import com.bigbluebubble.mysingingmonsters.data.loot.LootResult;
import com.bigbluebubble.mysingingmonsters.data.loot.LootTable;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

public class PlayerDailyCumulativeLogin {
   public static final String ID_KEY = "id";
   public static final String CALENDAR_KEY = "calendar_id";
   public static final String REWARD_KEY = "reward_idx";
   public static final String NEXT_COLLECT_KEY = "next_collect";
   public static final String TOTAL_KEY = "total";
   static Random rand = new Random();
   static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");
   static final long MS_PER_DAY = 86400000L;
   long id;
   int calendarId;
   int rewardIdx;
   long nextCollect;
   int total;
   boolean isDirty = false;

   public void setDirty(boolean dirty) {
      this.isDirty = dirty;
   }

   private long currentTime() {
      return MSMExtension.CurrentDBTime();
   }

   private long today() {
      return MSMExtension.previousDailyTime(GameSettings.get("DAILY_LOGIN_RESET_HOUR", 0), GameSettings.get("DAILY_LOGIN_RESET_MINUTE", 0)).getTimeInMillis();
   }

   public long tomorrow() {
      return this.today() + 86400000L;
   }

   public PlayerDailyCumulativeLogin() {
      this.calendarId = 1;
      this.rewardIdx = 0;
      this.nextCollect = this.today();
      this.total = 0;
   }

   public PlayerDailyCumulativeLogin(ISFSObject data) {
      this.initFromSFSObject(data);
   }

   public void initFromSFSObject(ISFSObject data) {
      this.calendarId = data.getInt("calendar_id");
      this.rewardIdx = data.getInt("reward_idx");
      this.nextCollect = data.getLong("next_collect");
      this.total = data.getInt("total");
   }

   public ISFSObject getData() {
      return this.toSFSObject();
   }

   public ISFSObject toSFSObject() {
      ISFSObject s = new SFSObject();
      s.putInt("calendar_id", this.calendarId);
      s.putInt("reward_idx", this.rewardIdx);
      s.putLong("next_collect", this.nextCollect);
      s.putInt("total", this.total);
      return s;
   }

   public String toString() {
      return this.toSFSObject().getDump();
   }

   public int getCalendarId() {
      return this.calendarId;
   }

   public int getRewardId() {
      return this.rewardIdx;
   }

   public long getNextCollect() {
      return this.nextCollect;
   }

   public int getTotal() {
      return this.total;
   }

   private List<LootResult> rawCollect(DailyCumulativeLoginCalendar calendar, DailyCumulativeLoginReward reward, Player player, User sender, GameStateHandler gs) throws Exception {
      List<LootResult> loot = null;
      LootTable lootTable = reward.getLootTable();
      if (lootTable != null) {
         loot = lootTable.pull(rand, 1, player.getLevel());
         LootResult.Collect(player, loot, sender, gs);
      }

      ++this.rewardIdx;
      if (this.rewardIdx >= calendar.getNumRewards()) {
         ++this.calendarId;
         this.rewardIdx = 0;
      }

      return loot;
   }

   public void collect(Player p, ISFSObject results, User sender, GameStateHandler gs) throws Exception {
      List<LootResult> loot = null;
      DailyCumulativeLoginCalendar rewardsCalendar = DailyCumulativeLoginLookup.get(this.calendarId);
      if (rewardsCalendar != null) {
         DailyCumulativeLoginReward rewardsData = rewardsCalendar.getReward(this.rewardIdx);
         int nextRewardIdx;
         if (this.currentTime() >= this.nextCollect) {
            if (this.rewardIdx > 0) {
               nextRewardIdx = GameSettings.get("USER_DCL_CATCHUP_MAX_DAYS", 5);
               this.total = Math.max(0, Math.min(nextRewardIdx, (int)((this.currentTime() - this.nextCollect) / 86400000L)));
            } else {
               this.total = 0;
            }

            nextRewardIdx = this.rewardIdx + 1;
            if (nextRewardIdx + this.total >= rewardsCalendar.getNumRewards()) {
               this.total = rewardsCalendar.getNumRewards() - nextRewardIdx;
            }

            loot = this.rawCollect(rewardsCalendar, rewardsData, p, sender, gs);
            this.nextCollect = this.tomorrow();
            this.isDirty = true;
         } else {
            nextRewardIdx = this.total & 255;
            int usedDays = this.total >> 8 & 255;
            if (usedDays < nextRewardIdx) {
               int BASE_DIAMOND_COST = GameSettings.get("USER_DCL_CATCHUP_BASE_DIAMOND_COST", 5);
               int diamondCost = BASE_DIAMOND_COST + usedDays;
               if (p.canBuy(0L, 0L, (long)diamondCost, 0L, 0L, 0L, 0)) {
                  loot = this.rawCollect(rewardsCalendar, rewardsData, p, sender, gs);
                  p.chargePlayer(sender, gs, 0, 0, diamondCost, 0L, 0, 0, 0);
                  this.total = usedDays + 1 << 8 | nextRewardIdx;
                  this.isDirty = true;
                  gs.logDiamondUsage(sender, "daily_cumulative_login_catchup", diamondCost, p.getLevel());
                  MSMExtension.getInstance().stats.trackLoginCalendarCatchup(sender, usedDays, nextRewardIdx, diamondCost, this.calendarId);
               }
            }
         }
      }

      if (results != null) {
         if (loot != null) {
            ISFSArray rewardsArr = new SFSArray();
            Iterator var13 = loot.iterator();

            while(var13.hasNext()) {
               LootResult lr = (LootResult)var13.next();
               rewardsArr.addSFSObject(lr.toSFSObject());
               switch(lr.getType()) {
               case MONSTER:
               case STRUCTURE:
                  if (!results.containsKey("updateInventory")) {
                     results.putBool("updateInventory", true);
                  }
                  break;
               case COSTUME:
                  if (!results.containsKey("updateCostumes")) {
                     results.putBool("updateCostumes", true);
                     results.putInt("updateCostumesIsland", lr.getTargetIsland());
                  }
                  break;
               case BUFF:
                  if (!results.containsKey("updateTimedEvents")) {
                     results.putBool("updateTimedEvents", true);
                  }
               }
            }

            results.putSFSArray("loot", rewardsArr);
         }

         if (this.rewardIdx == 0) {
            results.putInt("completed", 1);
         }

         results.putInt("calendar_id", this.calendarId);
         results.putInt("reward_idx", this.rewardIdx);
         results.putLong("next_collect", this.nextCollect);
         results.putInt("total", this.total);
         MSMExtension.getInstance().stats.trackLoginCalendarCollect(sender, results);
      }

   }

   public static PlayerDailyCumulativeLogin load(IDbWrapper db, long playerId) throws Exception {
      PlayerDailyCumulativeLogin pdcl = null;
      String SELECT_SQL = "SELECT * FROM user_daily_cumulative_logins WHERE user_id = ?";
      ISFSArray results = db.query("SELECT * FROM user_daily_cumulative_logins WHERE user_id = ?", new Object[]{playerId});
      if (results.size() > 0) {
         pdcl = new PlayerDailyCumulativeLogin(results.getSFSObject(0));
      } else {
         pdcl = new PlayerDailyCumulativeLogin();
         pdcl.saveNew(db, playerId);
      }

      return pdcl;
   }

   private void saveNew(IDbWrapper db, long playerId) throws Exception {
      String INSERT_SQL = "INSERT INTO user_daily_cumulative_logins SET user_id = ?, calendar_id = ?, reward_idx = ?, next_collect = ?, total = ?";
      db.insertGetId("INSERT INTO user_daily_cumulative_logins SET user_id = ?, calendar_id = ?, reward_idx = ?, next_collect = ?, total = ?", new Object[]{playerId, this.calendarId, this.rewardIdx, new Timestamp(this.nextCollect), this.total});
   }

   public void save(IDbWrapper db, long playerId) throws Exception {
      if (this.isDirty) {
         String UPDATE_SQL = "UPDATE user_daily_cumulative_logins SET user_id = ?, calendar_id = ?, reward_idx = ?, next_collect = ?, total = ? WHERE user_id = ?";
         db.update("UPDATE user_daily_cumulative_logins SET user_id = ?, calendar_id = ?, reward_idx = ?, next_collect = ?, total = ? WHERE user_id = ?", new Object[]{playerId, this.calendarId, this.rewardIdx, new Timestamp(this.nextCollect), this.total, playerId});
         this.isDirty = false;
      }
   }
}
