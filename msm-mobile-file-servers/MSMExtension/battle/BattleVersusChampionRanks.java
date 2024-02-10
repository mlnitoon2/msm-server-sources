package com.bigbluebubble.mysingingmonsters.battle;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BattleVersusChampionRanks {
   private IDbWrapper db_;
   private Map<Integer, Map<Long, BattleVersusChampionRanks.RankTable>> rankedCampaignResults_ = new ConcurrentHashMap();
   static BattleVersusChampionRanks instance;

   BattleVersusChampionRanks(IDbWrapper db) {
      this.db_ = db;
   }

   private BattleVersusChampionRanks.RankedUser _getRankedUser(int campaignId, long scheduleStart, long userId) {
      Map<Long, BattleVersusChampionRanks.RankTable> inner = (Map)this.rankedCampaignResults_.get(campaignId);
      if (inner == null) {
         inner = new ConcurrentHashMap();
         this.rankedCampaignResults_.put(campaignId, inner);
      }

      BattleVersusChampionRanks.RankTable rankTable = (BattleVersusChampionRanks.RankTable)((Map)inner).get(scheduleStart);
      if (rankTable == null) {
         rankTable = new BattleVersusChampionRanks.RankTable(campaignId, scheduleStart);
         ((Map)inner).put(scheduleStart, rankTable);
      }

      long refreshTime = (long)GameSettings.get("BATTLE_RANK_REFRESH_MS", 30000);
      if (MSMExtension.CurrentDBTime() > rankTable.lastUpdate + refreshTime) {
         Logger.trace("Refreshing Champion Ranks for campaign: " + campaignId + " started: " + scheduleStart);
         rankTable.Refresh(this.db_);
      }

      return rankTable.getRankedUser(userId);
   }

   private BattleVersusChampionRanks.RankTable _getRankTable(int campaignId, long scheduleStart) {
      Map<Long, BattleVersusChampionRanks.RankTable> inner = (Map)this.rankedCampaignResults_.get(campaignId);
      if (inner == null) {
         inner = new ConcurrentHashMap();
         this.rankedCampaignResults_.put(campaignId, inner);
      }

      BattleVersusChampionRanks.RankTable rankTable = (BattleVersusChampionRanks.RankTable)((Map)inner).get(scheduleStart);
      if (rankTable == null) {
         rankTable = new BattleVersusChampionRanks.RankTable(campaignId, scheduleStart);
         ((Map)inner).put(scheduleStart, rankTable);
      }

      long refreshTime = (long)GameSettings.get("BATTLE_RANK_REFRESH_MS", 30000);
      if (MSMExtension.CurrentDBTime() > rankTable.lastUpdate + refreshTime) {
         Logger.trace("Refreshing Champion Ranks for campaign: " + campaignId + " started: " + scheduleStart);
         rankTable.Refresh(this.db_);
      }

      return rankTable;
   }

   public static void init(IDbWrapper db) {
      instance = new BattleVersusChampionRanks(db);
   }

   public static BattleVersusChampionRanks.RankedUser getRankedUser(int campaignId, long scheduleStart, long userId) {
      return instance._getRankedUser(campaignId, scheduleStart, userId);
   }

   public static BattleVersusChampionRanks.RankTable getRankTable(int campaignId, long scheduleStart) {
      return instance._getRankTable(campaignId, scheduleStart);
   }

   public class RankTable {
      public int campaignId;
      public Timestamp scheduleStart;
      public Map<Long, BattleVersusChampionRanks.RankedUser> rankedUsers;
      public List<BattleVersusChampionRanks.RankedUser> rankedList;
      public long lastUpdate = 0L;
      public static final int MIN_UPDATE_REFRESH_MS = 30000;
      final String GET_RANKED_USERS_SQL = "SELECT user_battle_pvp.user_id, display_name, tier, champion_score, claimed_on,user_avatar.pp_type, user_avatar.pp_info FROM user_battle_pvp LEFT JOIN user_avatar ON user_avatar.user_id = user_battle_pvp.user_id WHERE campaign_id = ? AND schedule_started_on = ? AND champion_score IS NOT NULL AND champion_score != 0 ORDER BY champion_score DESC";

      public RankTable(int campaignId, long scheduleStart) {
         this.campaignId = campaignId;
         this.scheduleStart = new Timestamp(scheduleStart);
      }

      public ISFSArray toSFSArrayLimited() {
         ISFSArray arr = new SFSArray();
         if (this.rankedList == null) {
            this.Refresh(BattleVersusChampionRanks.this.db_);
         }

         Iterator<BattleVersusChampionRanks.RankedUser> itr = this.rankedList.iterator();

         for(int i = 0; itr.hasNext() && i < GameSettings.get("MAX_GLOBAL_BATTLE_RANKINGS_SHOWN", 10); ++i) {
            BattleVersusChampionRanks.RankedUser next = (BattleVersusChampionRanks.RankedUser)itr.next();
            arr.addSFSObject(next.toSFSObject());
         }

         return arr;
      }

      public void Refresh(IDbWrapper db) {
         try {
            ISFSArray results = db.query("SELECT user_battle_pvp.user_id, display_name, tier, champion_score, claimed_on,user_avatar.pp_type, user_avatar.pp_info FROM user_battle_pvp LEFT JOIN user_avatar ON user_avatar.user_id = user_battle_pvp.user_id WHERE campaign_id = ? AND schedule_started_on = ? AND champion_score IS NOT NULL AND champion_score != 0 ORDER BY champion_score DESC", new Object[]{this.campaignId, this.scheduleStart.toString()});
            this.rankedUsers = new ConcurrentHashMap(results.size());
            List<BattleVersusChampionRanks.RankedUser> newRankedList = new ArrayList(results.size());
            int rank = 1;
            Iterator itr = results.iterator();

            while(itr.hasNext()) {
               ISFSObject data = (ISFSObject)((ISFSObject)((SFSDataWrapper)itr.next()).getObject());
               data.putInt("rank", rank++);
               BattleVersusChampionRanks.RankedUser rankedUser = BattleVersusChampionRanks.this.new RankedUser(data);
               newRankedList.add(rankedUser);
               this.rankedUsers.put(rankedUser.userId(), rankedUser);
            }

            this.rankedList = newRankedList;
            this.lastUpdate = MSMExtension.CurrentDBTime();
         } catch (Exception var8) {
            Logger.trace(var8, "Error Refreshing Ranking Table:" + this.campaignId + ":" + this.scheduleStart);
         }

      }

      public BattleVersusChampionRanks.RankedUser getRankedUser(long userId) {
         return (BattleVersusChampionRanks.RankedUser)this.rankedUsers.get(userId);
      }
   }

   public class RankedUser {
      private ISFSObject data_;

      public long userId() {
         return this.data_.getLong("user_id");
      }

      public String name() {
         return this.data_.getUtfString("display_name");
      }

      public int championScore() {
         return this.data_.getInt("champion_score");
      }

      public long rank() {
         return SFSHelpers.getLong("rank", this.data_);
      }

      public int tier() {
         return this.data_.getInt("tier");
      }

      public long claimedOn() {
         return this.data_.getLong("claimed_on");
      }

      public int profilePicType() {
         return this.data_.containsKey("pp_type") ? this.data_.getInt("pp_type") : 0;
      }

      public String profilePicInfo() {
         return this.data_.containsKey("pp_info") ? this.data_.getUtfString("pp_info") : "0";
      }

      public RankedUser(ISFSObject data) {
         this.data_ = data;
      }

      public ISFSObject toSFSObject() {
         return this.data_;
      }
   }
}
