package com.bigbluebubble.mysingingmonsters.player;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.BBBServer.util.Misc;
import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.GameStateHandler;
import com.bigbluebubble.mysingingmonsters.Helpers;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.battle.BattlePlayerState;
import com.bigbluebubble.mysingingmonsters.battle.BattlePlayerVersusState;
import com.bigbluebubble.mysingingmonsters.battle.BattleSeasons;
import com.bigbluebubble.mysingingmonsters.battle.BattleTrophyLookup;
import com.bigbluebubble.mysingingmonsters.battle.BattleVersusChampionRanks;
import com.bigbluebubble.mysingingmonsters.costumes.PlayerCostumeState;
import com.bigbluebubble.mysingingmonsters.data.DailyCurrencyPack;
import com.bigbluebubble.mysingingmonsters.data.DailyCurrencyPackLookup;
import com.bigbluebubble.mysingingmonsters.data.Level;
import com.bigbluebubble.mysingingmonsters.data.LevelLookup;
import com.bigbluebubble.mysingingmonsters.data.Mail;
import com.bigbluebubble.mysingingmonsters.data.MailLookup;
import com.bigbluebubble.mysingingmonsters.data.MonsterLookup;
import com.bigbluebubble.mysingingmonsters.data.QuestLookup;
import com.bigbluebubble.mysingingmonsters.data.ScratchTicketFunctions;
import com.bigbluebubble.mysingingmonsters.data.VersionData;
import com.bigbluebubble.mysingingmonsters.data.groups.BattleHintLevelGroup;
import com.bigbluebubble.mysingingmonsters.data.groups.TutorialGroup;
import com.bigbluebubble.mysingingmonsters.data.groups.UserGroup;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventManager;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.EntitySalesEvent;
import com.bigbluebubble.mysingingmonsters.logging.Firebase;
import com.bigbluebubble.mysingingmonsters.logging.GooglePlayEvents;
import com.bigbluebubble.mysingingmonsters.logging.TapjoyTagger;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TimeZone;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Player {
   public static final String ISLANDS_KEY = "islands";
   public static final String QUESTS_KEY = "quests";
   public static final String ACHIEVEMENTS_KEY = "achievements";
   public static final String COINS_KEY = "coins";
   public static final String DIAMONDS_KEY = "diamonds";
   public static final String KEYS_KEY = "keys";
   public static final String ETH_CURRENCY_KEY = "ethereal_currency";
   public static final String FOOD_KEY = "food";
   public static final String RELICS_KEY = "relics";
   public static final String EGG_WILDCARDS_KEY = "egg_wildcards";
   public static final String COINS_ACTUAL_KEY = "coins_actual";
   public static final String DIAMONDS_ACTUAL_KEY = "diamonds_actual";
   public static final String FOOD_ACTUAL_KEY = "food_actual";
   public static final String ETH_CURRENCY_ACTUAL_KEY = "ethereal_currency_actual";
   public static final String KEYS_ACTUAL_KEY = "keys_actual";
   public static final String RELICS_ACTUAL_KEY = "relics_actual";
   public static final String EGG_WILDCARDS_ACTUAL_KEY = "egg_wildcards_actual";
   public static final String STARPOWER_ACTUAL_KEY = "starpower_actual";
   public static final String STARPOWER_KEY = "starpower";
   public static final String TOTAL_STARPOWER_COLLECTED_KEY = "total_starpower_collected";
   public static final String EST_STARPOWER_EARNED = "earned_starpower";
   public static final String DAILY_RELICS_COUNT_KEY = "daily_relic_purchase_count";
   public static final String LAST_RELIC_PURCHASE_KEY = "last_relic_purchase";
   public static final String NEXT_RELIC_RESET_KEY = "next_relic_reset";
   public static final String HAS_FREE_AD_SCRATCH_KEY = "has_free_ad_scratch";
   public static final String XP_KEY = "xp";
   public static final String LEVEL_KEY = "level";
   public static final String PREMIUM_KEY = "premium";
   public static final String HAS_PROMOTIONS_KEY = "has_promo";
   public static final String FRIEND_GIFT_KEY = "friend_gift";
   public static final String LAST_CLIENT_VER_KEY = "last_client_version";
   public static final String LAST_LOGIN_KEY = "last_login";
   public static final String DATE_CREATED_KEY = "date_created";
   public static final String COUNTRY_CODE_KEY = "country";
   public static final String DAILY_BONUS_DIAMONDS_KEY = "daily_bonus_diamonds";
   public static final String DAILY_BONUS_COINS_KEY = "daily_bonus_coins";
   public static final String DAILY_BONUS_TYPE_KEY = "daily_bonus_type";
   public static final String DAILY_BONUS_AMOUNT_KEY = "daily_bonus_amount";
   public static final String PURCHASES_TOTAL_KEY = "purchases_total";
   public static final String PURCHASES_AMOUNT_KEY = "purchases_amount";
   public static final String SPEED_UP_CREDIT_KEY = "speed_up_credit";
   public static final String RELIC_DIAMOND_COST_KEY = "relic_diamond_cost";
   public static final String TOTAL_SPEEDUPS_KEY = "rewards_total";
   public static final String TRACKS_KEY = "tracks";
   public static final String SONGS_KEY = "songs";
   public static final String MAILBOX_KEY = "mailbox";
   public static final String NEW_MAIL_KEY = "new_mail";
   public static final String BAN_EXPIRY_KEY = "ban_expiry";
   public static final String BAN_REASON_KEY = "reason";
   public static final String OWNED_ISLAND_THEMES = "owned_island_themes";
   public static final String ACTIVE_ISLAND_THEMES = "active_island_themes";
   public static final String DAILY_REWARD_PACKS = "daily_currency_pack";
   public static final String BATTLE_XP_KEY = "battle_xp";
   public static final String BATTLE_LEVEL_KEY = "battle_level";
   public static final String PERMA_CAMPAIGNS_VIEWED_KEY = "perma_campaigns_viewed";
   public static final String AVATAR_KEY = "avatar";
   public static final String COSTUMES_KEY = "costumes";
   public static final String TIMED_EVENTS_KEY = "timed_events";
   public static final String LAST_COLLECT_ALL_KEY = "last_collect_all";
   public static final String SHOW_WELCOMEBACK_KEY = "show_welcomeback";
   public static final String LAST_TIMED_THEME_KEY = "last_timed_theme";
   public static final String ISLAND_TUTS_KEY = "island_tutorials";
   public static final String MONIKER_ID_KEY = "moniker_id";
   public static final String CURRENCY_SCRATCH_TIME_KEY = "currencyScratchTime";
   public static final String MONSTER_SCRATCH_TIME_KEY = "monsterScratchTime";
   public static final String FLIP_GAME_TIME_KEY = "flipGameTime";
   public static final String INVENTORY_KEY = "inventory";
   public static final String DELETE_ACCOUNT_REQUEST = "DELETE_ACCOUNT_REQUEST";
   public static final String DAILY_CUMULATIVE_LOGIN_KEY = "daily_cumulative_login";
   public static final String BBB_ID_KEY = "bbb_id";
   public static final long MAX_DISPLAYED_CURRENCY = 1999999999L;
   private final boolean fullPlayer_;
   private ISFSObject data = null;
   private String userGameId = null;
   private HashMap<Long, PlayerIsland> islandMap = null;
   private ArrayList<PlayerQuest> quests = null;
   private HashMap<String, Integer> questEvents = null;
   private ArrayList<PlayerAchievement> achievements = null;
   private ArrayList<Integer> collectedQuests_ = null;
   private ISFSArray tracks = null;
   private ISFSArray songs = null;
   private SFSArray mailbox = null;
   private boolean newMail = false;
   private PlayerDailyCurrencyPack purchasedDailyCurrencyPack = null;
   private HashMap<Integer, Integer> activeIslandThemes = null;
   private ISFSArray ownedIslandThemes = null;
   private ArrayList<Integer> groups_;
   private ArrayList<Integer> newGroups_;
   TutorialGroup tutorialGroup_ = null;
   BattleHintLevelGroup battleHintGroup_ = null;
   private long coins_;
   private long diamonds_;
   private long keys_;
   private long ethCurrency_;
   private long food_;
   private long starpower_;
   private long totalStarpowerCollected_;
   private long relics_;
   private long eggWildcards_;
   private int cachedEarnedStarpowerCalc = -1;
   private long coinScratchTime_ = Long.MAX_VALUE;
   private long monsterScratchTime_ = Long.MAX_VALUE;
   private boolean coinScratchUncollected_ = false;
   private boolean monsterScratchUncollected_ = false;
   private boolean spinWheelUncollected_ = false;
   private String coinScratchPrize_ = null;
   private String monsterScratchPrize_ = null;
   private String spinWheelPrize_ = null;
   private String lastCommand = "";
   private long lastCommandTime = 0L;
   private long memoryGameTime = 0L;
   private int lastQuestIndex = 0;
   private long lastMonsterIndex = 0L;
   private transient boolean isDirty = false;
   private transient boolean saveAllowed_ = true;
   private boolean hasPromotions_ = false;
   private boolean requestedDeletion_ = false;
   private long banExpiry_ = 0L;
   private List<String> reportedUsers_;
   private long lastReportTime_ = 0L;
   private String countryCode_ = null;
   private VersionInfo lastClientVersion_;
   private long lastLoginTime_ = 0L;
   private long dateCreatedTime_ = 0L;
   private Player.CurrencyType pendingDailyRewardType;
   private Integer pendingDailyRewardAmount;
   List<Player.CurrencyType> rewardTypes;
   private List<Integer> scaledLoginAmounts;
   private int rewardDay;
   private int activeStreak;
   private int dailyRewardsStage;
   private long lastCodeCheck_;
   private long friendGift_;
   private int totalSpeedups_;
   /** @deprecated */
   @Deprecated
   private int curRelicDiamondCost_;
   private int dailyRelicsCount_;
   private Long lastRelicPurchase_;
   private PlayerActiveIslandStats activeIslandStats;
   private HashMap<Player.SPEED_UP_TYPES, Player.PlayerSpeedUpInfo> speedUpMap;
   private Calendar cachedNextReset;
   private String platform;
   private String subplatform;
   private ISFSObject collectionTimers_;
   private Collection<Integer> viewedPermaCampaigns_;
   private PlayerAvatar avatar_;
   private PlayerCostumeState costumes_;
   private PlayerTimedEvents timedEvents_;
   private PlayerInventory inventory_;
   public PlayerFlipGame flipGame_;
   public PlayerDailyCumulativeLogin dailyCumulativeLogin_;
   public PlayerBuffs buffs_;
   private String prevGlobalDataStr;
   private Player.GlobalData curGlobalData;
   boolean showWelcomeBack;
   long lastClaimedFreeScratchAd;
   Long nextAdScratch;
   public static final String BATTLE_KEY = "battle";
   private BattlePlayerState battleState_;
   public static final String BATTLE_VERSUS_KEY = "battle_versus";
   public Player.VersusStateMap battleVersusStates_;

   public static Player.CurrencyType getCurrencyTypeFromString(String str) {
      Player.CurrencyType[] var1 = Player.CurrencyType.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Player.CurrencyType currencyType = var1[var3];
         if (str.equalsIgnoreCase(currencyType.getCurrencyKey())) {
            return currencyType;
         }
      }

      return Player.CurrencyType.Undefined;
   }

   public PlayerAvatar getAvatar() {
      return this.avatar_;
   }

   public PlayerCostumeState getCostumes() {
      return this.costumes_;
   }

   public PlayerTimedEvents getTimedEvents() {
      return this.timedEvents_;
   }

   public PlayerInventory getInventory() {
      return this.inventory_;
   }

   public PlayerDailyCumulativeLogin getDailyCumulativeLogin() {
      return this.dailyCumulativeLogin_;
   }

   public PlayerBuffs getBuffs() {
      return this.buffs_;
   }

   public long getSpecialCollectionTimer(String which) {
      return this.collectionTimers_.containsKey(which) ? SFSHelpers.getLong(which, this.collectionTimers_) : 0L;
   }

   public void setSpecialCollectionTimer(String which, long newValue) {
      this.collectionTimers_.putLong(which, newValue);
   }

   public void setDailyBonusToBeCollected() throws Exception {
      MSMExtension ext = MSMExtension.getInstance();
      String sql = "SELECT * FROM user_daily_logins_new WHERE user_id = ?";
      Object[] userIdParam = new Object[]{this.getPlayerId()};
      ISFSArray result = ext.getDB().query(sql, userIdParam);
      this.rewardDay = 0;
      ISFSObject record;
      if (result != null && result.size() != 0) {
         record = result.getSFSObject(0);
         this.activeStreak = record.getInt("active_streak");
         this.dailyRewardsStage = record.getInt("reward_stage");
         this.updateDailyRewardsStage();
         this.checkDailyLogin(record.getInt("longest_streak"));
      } else {
         sql = "SELECT * FROM user_daily_logins WHERE user_id = ?";
         result = ext.getDB().query(sql, userIdParam);
         this.dailyRewardsStage = ext.dailyLoginLvlCutoff.size();

         for(int i = 0; i < ext.dailyLoginLvlCutoff.size(); ++i) {
            if (this.getLevel() < (Integer)ext.dailyLoginLvlCutoff.get(i)) {
               this.dailyRewardsStage = i;
               break;
            }
         }

         this.updateDailyRewardsStage();
         if (result != null && result.size() != 0) {
            record = result.getSFSObject(0);
            this.activeStreak = record.getInt("active_streak");
            String insertsql = "INSERT INTO user_daily_logins_new SET user_id = ?, active_streak=?, longest_streak=?, reward_stage=?";
            userIdParam = new Object[]{this.getPlayerId(), this.activeStreak, record.getInt("longest_streak"), this.dailyRewardsStage};
            ext.getDB().insertGetId(insertsql, userIdParam);
            String deletesql = "DELETE FROM user_daily_logins WHERE user_id = ?";
            ext.getDB().update(deletesql, new Object[]{this.getPlayerId()});
            this.checkDailyLogin(record.getInt("longest_streak"));
         } else {
            String insertsql = "INSERT INTO user_daily_logins_new SET user_id = ?, reward_stage=?";
            userIdParam = new Object[]{this.getPlayerId(), this.dailyRewardsStage};
            ext.getDB().insertGetId(insertsql, userIdParam);
            this.rewardDay = 0;
         }
      }

   }

   public void doFinalizeThings() throws Exception {
      if (this.rewardDay == -1 && this.pendingDailyRewardType == Player.CurrencyType.Deferred) {
         this.restartLoginStreak();
      }

      if (this.pendingDailyRewardAmount > 0) {
         this.collectPendingDailyReward();
      }

   }

   boolean validBonusEntity(Integer bonusEntity) {
      return bonusEntity != null && MonsterLookup.getFromEntityId(bonusEntity) != null;
   }

   public void collectPendingDailyReward() {
      if (this.firstDayTutorial()) {
         this.pendingDailyRewardType = (Player.CurrencyType)((List)MSMExtension.getInstance().dailyLoginTypes.get(0)).get(1);
         this.pendingDailyRewardAmount = (Integer)((List)MSMExtension.getInstance().dailyLoginAmounts.get(0)).get(1);
      }

      if (this.pendingDailyRewardAmount > 0) {
         switch(this.pendingDailyRewardType) {
         case Diamonds:
            this.adjustDiamonds((User)null, (GameStateHandler)null, this.pendingDailyRewardAmount);
            MSMExtension.getInstance().stats.trackReward(this, "daily_diamond_reward", "diamonds", (long)this.pendingDailyRewardAmount);
            break;
         case Coins:
            this.adjustCoins((User)null, (GameStateHandler)null, this.pendingDailyRewardAmount);
            break;
         case Ethereal:
            this.adjustEthCurrency((User)null, (GameStateHandler)null, this.pendingDailyRewardAmount);
            break;
         case Starpower:
            this.adjustStarpower((User)null, (GameStateHandler)null, (long)this.pendingDailyRewardAmount);
            break;
         case Food:
            this.adjustFood((User)null, (GameStateHandler)null, this.pendingDailyRewardAmount);
            break;
         case Keys:
            this.adjustKeys((User)null, (GameStateHandler)null, this.pendingDailyRewardAmount);
            break;
         case Relics:
            this.adjustRelics((User)null, (GameStateHandler)null, this.pendingDailyRewardAmount);
         }

         MSMExtension.getInstance().stats.trackDailyLoginReward(this, this.pendingDailyRewardType.getCurrencyKey(), this.rewardDay, this.pendingDailyRewardAmount);
      }

      this.pendingDailyRewardType = Player.CurrencyType.Undefined;
      this.pendingDailyRewardAmount = 0;
   }

   private boolean firstDayTutorial() {
      if (this.getLevel() < 4) {
         Calendar dailyLoginResetTime = MSMExtension.previousDailyTime(GameSettings.get("DAILY_LOGIN_RESET_HOUR", 0), GameSettings.get("DAILY_LOGIN_RESET_MINUTE", 0));
         Date dailyLoginResetDate = new Date(dailyLoginResetTime.getTimeInMillis());
         Date dateCreated = new Date(this.getDateCreatedTime());
         return dateCreated.after(dailyLoginResetDate);
      } else {
         return false;
      }
   }

   private void checkDailyLogin(int longestStreak) throws Exception {
      Calendar dailyLoginResetTime = MSMExtension.previousDailyTime(GameSettings.get("DAILY_LOGIN_RESET_HOUR", 0), GameSettings.get("DAILY_LOGIN_RESET_MINUTE", 0));
      Date dailyLoginResetDate = new Date(dailyLoginResetTime.getTimeInMillis());
      Date lastLoginDate = new Date(this.LastLoginTime());
      this.rewardDay = 0;
      if (lastLoginDate.before(dailyLoginResetDate)) {
         Calendar prevDailyLoginResetTime = (Calendar)dailyLoginResetTime.clone();
         prevDailyLoginResetTime.add(5, -1);
         prevDailyLoginResetTime.add(11, -1);
         Date prevDailyLoginResetDate = new Date(prevDailyLoginResetTime.getTimeInMillis());
         Calendar prevPrevDailyLoginResetTime = (Calendar)prevDailyLoginResetTime.clone();
         prevPrevDailyLoginResetTime.add(5, -2);
         Date prevPrevDailyLoginResetDate = new Date(prevPrevDailyLoginResetTime.getTimeInMillis());
         MSMExtension ext = MSMExtension.getInstance();
         String insertsql;
         if (lastLoginDate.before(prevPrevDailyLoginResetDate)) {
            this.dailyRewardsStage = ext.dailyLoginLvlCutoff.size();

            for(int i = 0; i < ext.dailyLoginLvlCutoff.size(); ++i) {
               if (this.getLevel() < (Integer)ext.dailyLoginLvlCutoff.get(i)) {
                  this.dailyRewardsStage = i;
                  break;
               }
            }

            this.updateDailyRewardsStage();
            this.activeStreak = 0;
            insertsql = "UPDATE user_daily_logins_new SET active_streak=?, reward_stage=? WHERE user_id = ?";
            ext.getDB().update(insertsql, new Object[]{this.activeStreak, this.dailyRewardsStage, this.getPlayerId()});
            this.rewardDay = 1;
         } else if (lastLoginDate.before(prevDailyLoginResetDate) && (this.activeStreak + 1) % (((List)ext.dailyLoginTypes.get(this.dailyRewardsStage)).size() - 1) + 1 != 1) {
            this.rewardDay = -1;
         } else {
            ++this.activeStreak;
            if (this.activeStreak > longestStreak) {
               longestStreak = this.activeStreak;
            }

            this.rewardDay = this.activeStreak % (((List)ext.dailyLoginTypes.get(this.dailyRewardsStage)).size() - 1) + 1;
            if (this.rewardDay == 1 && this.dailyRewardsStage < MSMExtension.getInstance().dailyLoginLvlCutoff.size() && this.dailyRewardsStage < MSMExtension.getInstance().dailyLoginLvlCutoff.size() && this.getLevel() >= (Integer)MSMExtension.getInstance().dailyLoginLvlCutoff.get(this.dailyRewardsStage)) {
               ++this.dailyRewardsStage;
               this.updateDailyRewardsStage();
            }

            insertsql = "UPDATE user_daily_logins_new SET active_streak=?, longest_streak=?, reward_stage=? WHERE user_id = ?";
            ext.getDB().insertGetId(insertsql, new Object[]{this.activeStreak, longestStreak, this.dailyRewardsStage, this.getPlayerId()});
         }
      }

      this.updatePendingFromRewardDay();
   }

   public void buyDailyLoginStreak() throws Exception {
      MSMExtension ext = MSMExtension.getInstance();
      String sql = "SELECT * FROM user_daily_logins_new WHERE user_id = ?";
      Object[] userIdParam = new Object[]{this.getPlayerId()};
      ISFSArray result = ext.getDB().query(sql, userIdParam);
      if (result != null && result.size() > 0) {
         ISFSObject record = result.getSFSObject(0);
         this.activeStreak = record.getInt("active_streak");
         int longestStreak = record.getInt("longest_streak");
         ++this.activeStreak;
         if (this.activeStreak > longestStreak) {
            longestStreak = this.activeStreak;
         }

         String insertsql = "UPDATE user_daily_logins_new SET active_streak=?, longest_streak=? WHERE user_id = ?";
         ext.getDB().insertGetId(insertsql, new Object[]{this.activeStreak, longestStreak, this.getPlayerId()});
         this.rewardDay = this.activeStreak % (this.rewardTypes.size() - 1) + 1;
         this.updatePendingFromRewardDay();
      }

   }

   public void clearDailyLoginStreak() throws Exception {
      MSMExtension ext = MSMExtension.getInstance();
      this.dailyRewardsStage = ext.dailyLoginLvlCutoff.size();

      for(int i = 0; i < ext.dailyLoginLvlCutoff.size(); ++i) {
         if (this.getLevel() < (Integer)ext.dailyLoginLvlCutoff.get(i)) {
            this.dailyRewardsStage = i;
            break;
         }
      }

      this.updateDailyRewardsStage();
      this.rewardDay = 0;
      this.activeStreak = 0;
      String sql = "UPDATE user_daily_logins_new SET active_streak=?, reward_stage=? WHERE user_id = ?";
      ext.getDB().update(sql, new Object[]{this.activeStreak, this.dailyRewardsStage, this.getPlayerId()});
   }

   public boolean restartLoginStreak() throws Exception {
      this.clearDailyLoginStreak();
      this.rewardDay = 1;
      this.updatePendingFromRewardDay();
      return true;
   }

   public static ISFSArray getCurIslandTorchGifts(Long bbbId, Long islandId, IDbWrapper db) throws Exception {
      String sql = "SELECT * FROM user_torch_gifts WHERE recipient_bbbid=? AND island_id=? and collected=0";
      Object[] args = new Object[]{bbbId, islandId};
      ISFSArray currentTorchGifts = db.query(sql, args);
      return currentTorchGifts.size() == 0 ? null : currentTorchGifts;
   }

   public void clearOutInvalidTorchGifts(MSMExtension ext) throws Exception {
      String sql = "SELECT * FROM user_torch_gifts WHERE recipient_bbbid=? and collected=0";
      Object[] args = new Object[]{this.getBbbId()};
      ISFSArray currentTorchGifts = ext.getDB().query(sql, args);
      if (currentTorchGifts != null && currentTorchGifts.size() != 0) {
         ArrayList<Long> giftsToRemove = new ArrayList();

         Long giftRecordId;
         for(int i = 0; i < currentTorchGifts.size(); ++i) {
            ISFSObject torchGift = currentTorchGifts.getSFSObject(i);
            giftRecordId = torchGift.getLong("island_id");
            Long structureId = torchGift.getLong("user_structure");
            PlayerIsland island = this.getIslandByID(giftRecordId);
            if (island == null) {
               giftsToRemove.add(torchGift.getLong("id"));
            } else {
               PlayerStructure structure = island.getStructureByID(structureId);
               if (structure == null) {
                  giftsToRemove.add(torchGift.getLong("id"));
               } else if (!structure.isTorch()) {
                  giftsToRemove.add(torchGift.getLong("id"));
               } else {
                  LitPlayerTorch litTorch = island.getLitTorchData(structureId);
                  if (litTorch != null && litTorch.isPermalit()) {
                     giftsToRemove.add(torchGift.getLong("id"));
                  }
               }
            }
         }

         if (giftsToRemove.size() != 0) {
            StringBuilder giftIdBuilder = new StringBuilder();

            for(int i = 0; i < giftsToRemove.size(); ++i) {
               giftRecordId = (Long)giftsToRemove.get(i);
               giftIdBuilder.append(giftRecordId);
               if (i + 1 < giftsToRemove.size()) {
                  giftIdBuilder.append(",");
               }
            }

            sql = "DELETE FROM user_torch_gifts WHERE id IN (" + giftIdBuilder.toString() + ")";
            args = new Object[0];
            ext.getDB().update(sql, args);
         }
      }
   }

   public SFSArray canLightFriendTorchTimes() throws Exception {
      long giverBbbId = this.getBbbId();
      MSMExtension ext = MSMExtension.getInstance();
      String sql = "DELETE FROM user_torch_gifts WHERE giver_bbbid=? AND collected=1 AND TIMESTAMPADD(HOUR, ?, date) < NOW()";
      ext.getDB().update(sql, new Object[]{giverBbbId, GameSettings.get("MAX_TORCH_GIFT_GIVING_FREQUENCY_HOURS")});
      sql = "SELECT * FROM user_torch_gifts WHERE giver_bbbid=?";
      Object[] args = new Object[]{giverBbbId};
      ISFSArray currentTorchGifts = ext.getDB().query(sql, args);
      if (currentTorchGifts.size() == 0) {
         return null;
      } else {
         Long maxGivingFreqMs = GameSettings.getLong("MAX_TORCH_GIFT_GIVING_FREQUENCY_HOURS") * 60L * 60L * 1000L;
         SFSArray timeOfNextGiftPerUser = new SFSArray();
         long prevGiftTime = 0L;
         long timeOfNextGift = 0L;

         for(int i = 0; i < currentTorchGifts.size(); ++i) {
            SFSObject torchGiftClientData = new SFSObject();
            ISFSObject torchGiftRecord = currentTorchGifts.getSFSObject(i);
            torchGiftClientData.putLong("recipient_bbbid", torchGiftRecord.getLong("recipient_bbbid"));
            prevGiftTime = torchGiftRecord.getLong("date");
            timeOfNextGift = prevGiftTime + maxGivingFreqMs;
            torchGiftClientData.putLong("time_of_next_gift", timeOfNextGift);
            timeOfNextGiftPerUser.addSFSObject(torchGiftClientData);
         }

         return timeOfNextGiftPerUser;
      }
   }

   public boolean canLightFriendTorchNow(Long friendBbbId) throws Exception {
      long giverBbbId = this.getBbbId();
      MSMExtension ext = MSMExtension.getInstance();
      String sql = "SELECT * FROM user_torch_gifts WHERE giver_bbbid=? and recipient_bbbid=?";
      Object[] args = new Object[]{giverBbbId, friendBbbId};
      ISFSArray currentTorchGifts = ext.getDB().query(sql, args);
      if (currentTorchGifts.size() == 0) {
         return true;
      } else {
         Long maxGivingFreqMs = GameSettings.getLong("MAX_TORCH_GIFT_GIVING_FREQUENCY_HOURS") * 60L * 60L * 1000L;
         boolean canSend = true;

         for(int i = 0; i < currentTorchGifts.size(); ++i) {
            ISFSObject s = currentTorchGifts.getSFSObject(i);
            Long dateGifted = s.getLong("date");
            if (MSMExtension.CurrentDBTime() <= dateGifted + maxGivingFreqMs) {
               canSend = false;
               break;
            }

            if (s.getInt("collected") == 1) {
               sql = "DELETE FROM user_torch_gifts WHERE id=?";
               args = new Object[]{s.getLong("id")};
               ext.getDB().update(sql, args);
            }
         }

         return canSend;
      }
   }

   public static int getNumLitTorches(String jsonDataStr) {
      if (jsonDataStr != null && !jsonDataStr.isEmpty()) {
         try {
            JSONObject jsonData = new JSONObject(jsonDataStr);
            if (jsonData != null && jsonData.has("torches")) {
               String torchDataStr = jsonData.getString("torches");
               if (torchDataStr != null) {
                  JSONArray torchArray = new JSONArray(torchDataStr);
                  int numLitTorchesOnIsland = 0;

                  for(int curLitTorch = 0; curLitTorch < torchArray.length(); ++curLitTorch) {
                     JSONObject jsonTorchData = torchArray.getJSONObject(curLitTorch);
                     if (torchLitFromTorchJsonObject(jsonTorchData)) {
                        ++numLitTorchesOnIsland;
                     }
                  }

                  return numLitTorchesOnIsland;
               }
            }
         } catch (JSONException var7) {
            Logger.trace((Exception)var7);
         }
      }

      return 0;
   }

   public static boolean torchLitFromTorchJsonObject(JSONObject volatileTorchData) {
      if (volatileTorchData != null) {
         try {
            boolean permalit = volatileTorchData.getBoolean("permalit");
            if (permalit) {
               return true;
            }

            long finishedAt = volatileTorchData.getLong("finished_at");
            if (finishedAt >= MSMExtension.CurrentDBTime()) {
               return true;
            }
         } catch (JSONException var4) {
            Logger.trace((Exception)var4);
         }
      }

      return false;
   }

   public static boolean friendHasUnlitTorches(IDbWrapper db, long bbbid, int userID) {
      String sql = "SELECT user_island_id, num_torches, volatile FROM user_islands, user_island_data WHERE user_islands.user=? AND user_island_id=user_island_data.island";
      Object[] query_params = new Object[]{userID};

      try {
         ISFSArray islandsResult = db.query(sql, query_params);
         if (islandsResult != null) {
            for(int i = 0; i < islandsResult.size(); ++i) {
               ISFSObject curIslandData = islandsResult.getSFSObject(i);
               int numTorches = curIslandData.containsKey("num_torches") ? curIslandData.getInt("num_torches") : 0;
               if (numTorches != 0) {
                  int numTorchesLitOnThisIsland = getNumLitTorches(Helpers.decompressJsonDataField(curIslandData.getUtfString("volatile"), "{}"));
                  int numTorchesGiftedOnThisIsland = 0;
                  sql = "SELECT recipient_bbbid, island_id, collected, date FROM user_torch_gifts WHERE (recipient_bbbid = ? and island_id=?) and ((NOW() < (date + INTERVAL 1 day)) or collected=0)";
                  query_params = new Object[]{bbbid, curIslandData.getLong("user_island_id")};
                  ISFSArray giftsResult = db.query(sql, query_params);
                  if (giftsResult != null) {
                     numTorchesGiftedOnThisIsland = giftsResult.size();
                  }

                  if (numTorchesLitOnThisIsland + numTorchesGiftedOnThisIsland < numTorches) {
                     return true;
                  }
               }
            }
         }

         return false;
      } catch (Exception var13) {
         Logger.trace(var13);
         return false;
      }
   }

   public Player(ISFSObject playerData, boolean fullPlayer, boolean newPlayer) {
      this.pendingDailyRewardType = Player.CurrencyType.Undefined;
      this.pendingDailyRewardAmount = 0;
      this.rewardTypes = null;
      this.scaledLoginAmounts = null;
      this.rewardDay = 0;
      this.activeStreak = 0;
      this.dailyRewardsStage = 1;
      this.lastCodeCheck_ = 0L;
      this.friendGift_ = 0L;
      this.totalSpeedups_ = 0;
      this.curRelicDiamondCost_ = 0;
      this.dailyRelicsCount_ = 0;
      this.lastRelicPurchase_ = null;
      this.activeIslandStats = null;
      this.speedUpMap = null;
      this.cachedNextReset = null;
      this.platform = null;
      this.subplatform = null;
      this.collectionTimers_ = null;
      this.viewedPermaCampaigns_ = null;
      this.prevGlobalDataStr = "{}";
      this.curGlobalData = new Player.GlobalData();
      this.showWelcomeBack = false;
      this.lastClaimedFreeScratchAd = 0L;
      this.nextAdScratch = null;
      this.battleVersusStates_ = new Player.VersusStateMap();
      this.fullPlayer_ = fullPlayer;
      this.data = playerData;
      this.lastClientVersion_ = new VersionInfo(this.data.getUtfString("last_client_version"));
      this.islandMap = new HashMap();
      this.quests = new ArrayList();
      this.questEvents = new HashMap();
      this.achievements = new ArrayList();
      this.collectedQuests_ = new ArrayList();
      this.activeIslandThemes = new HashMap();
      this.ownedIslandThemes = new SFSArray();
      this.tracks = new SFSArray();
      this.songs = new SFSArray();
      this.mailbox = new SFSArray();
      this.data.putUtfString("display_name", Helpers.sanitizeName(this.data.getUtfString("display_name"), " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!¨\"#$&'()*+,-./:;<=>?@{}0123456789|£©¿®`~^ÀÁÂÄÇÈÉÊËÌÍÎÏÑÒÓÔÖÙÚÛÜßàáâäçèéêëìíîïñòóôöùúûü_ÆæÃãÕõАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя€₽¡"));
      this.speedUpMap = new HashMap();
      this.coins_ = fullPlayer ? this.data.getLong("coins") : 0L;
      this.data.removeElement("coins");
      this.diamonds_ = fullPlayer ? this.data.getLong("diamonds") : 0L;
      this.data.removeElement("diamonds");
      this.ethCurrency_ = fullPlayer ? this.data.getLong("ethereal_currency") : 0L;
      this.data.removeElement("ethereal_currency");
      this.food_ = fullPlayer ? this.data.getLong("food") : 0L;
      this.data.removeElement("food");
      if (this.data.containsKey("starpower")) {
         this.starpower_ = this.data.getLong("starpower");
      }

      if (this.data.containsKey("keys")) {
         this.keys_ = this.data.getLong("keys");
      } else {
         this.keys_ = (long)GameSettings.getInt("PLAYER_START_KEYS");
      }

      if (this.data.containsKey("relics")) {
         this.relics_ = this.data.getLong("relics");
      }

      if (this.data.containsKey("egg_wildcards")) {
         this.eggWildcards_ = this.data.getLong("egg_wildcards");
      }

      if (this.data.containsKey("friend_gift")) {
         this.friendGift_ = this.data.getLong("friend_gift");
      } else {
         this.friendGift_ = MSMExtension.CurrentDBTime() + GameSettings.getLong("FRIEND_GIFT_START_MINUTES") * 60L * 1000L;
      }

      if (this.data.containsKey("total_starpower_collected")) {
         this.totalStarpowerCollected_ = this.data.getLong("total_starpower_collected");
      }

      if (this.data.containsKey("daily_relic_purchase_count")) {
         this.dailyRelicsCount_ = this.data.getInt("daily_relic_purchase_count");
      }

      if (this.data.containsKey("last_relic_purchase")) {
         this.lastRelicPurchase_ = this.data.getLong("last_relic_purchase");
      }

      if (this.data.containsKey("rewards_total")) {
         this.totalSpeedups_ = this.data.getInt("rewards_total");
      }

      this.updateDailyRelicCount();
      if (this.data.containsKey("last_login")) {
         this.lastLoginTime_ = this.data.getLong("last_login");
      }

      if (this.data.containsKey("date_created")) {
         this.dateCreatedTime_ = this.data.getLong("date_created");
      }

      if (this.data.containsKey("country")) {
         this.countryCode_ = this.data.getUtfString("country");
      }

      this.groups_ = new ArrayList();
      this.newGroups_ = new ArrayList();
      this.activeIslandStats = new PlayerActiveIslandStats(this.getBbbId());
      if (this.data.containsKey("prizes")) {
         this.collectionTimers_ = this.data.getSFSObject("prizes");
      } else {
         this.collectionTimers_ = new SFSObject();
      }

      if (this.data.containsKey("ban_expiry")) {
         this.banExpiry_ = this.data.getLong("ban_expiry");
      }

      if (this.data.containsKey("reason")) {
         this.requestedDeletion_ = this.data.getUtfString("reason").equalsIgnoreCase("DELETE_ACCOUNT_REQUEST");
      }

      if (fullPlayer) {
         this.loadInventory();
         this.setDailySpeedUpCredit();
         this.updateAdScratch();
         if (!newPlayer) {
            try {
               this.setDailyBonusToBeCollected();
            } catch (Exception var9) {
               Logger.trace(var9, "**** error attempting to collect daily bonus ****");
            }
         }

         this.updateDailyCurrencyPacks();
         this.updateMail();
         this.loadDailyCumulativeLogin();
         this.loadBattleState();
         this.loadBattleVersusStates();
         this.loadViewedBattles();
         this.loadAvatar();
         this.loadCostumes();
         this.reportedUsers_ = new ArrayList();
         this.loadTimedEvents();
         this.buffs_ = new PlayerBuffs(this);
      }

      try {
         String sql = "SELECT * FROM user_game_id_to_bbb_id WHERE bbb_id = ?;";
         Object[] args = new Object[]{playerData.getLong("bbb_id")};
         ISFSArray result = MSMExtension.getInstance().getDB().query(sql, args);
         if (result != null && result.size() > 0) {
            ISFSObject record = result.getSFSObject(0);
            this.userGameId = record.getUtfString("user_game_id");
            Logger.trace(LogLevel.DEBUG, "**** LOADED userGameId " + this.userGameId + " bbbId " + playerData.getLong("bbb_id") + " ****");
         }
      } catch (Exception var8) {
         Logger.trace(var8, "**** error loading userGameId ***");
      }

   }

   private void updateDailyRewardsStage() {
      this.rewardTypes = (List)MSMExtension.getInstance().dailyLoginTypes.get(this.dailyRewardsStage);
      List baseRewardAmounts;
      int i;
      if (this.dailyRewardsStage < MSMExtension.getInstance().dailyLoginScaleStartStage) {
         baseRewardAmounts = (List)MSMExtension.getInstance().dailyLoginAmounts.get(this.dailyRewardsStage);
         this.scaledLoginAmounts = new ArrayList();

         for(i = 0; i < this.rewardTypes.size(); ++i) {
            this.scaledLoginAmounts.add(baseRewardAmounts.get(i));
         }
      } else {
         baseRewardAmounts = (List)MSMExtension.getInstance().dailyLoginAmounts.get(this.dailyRewardsStage);
         if (this.scaledLoginAmounts == null) {
            this.scaledLoginAmounts = new ArrayList();

            for(i = 0; i < this.rewardTypes.size(); ++i) {
               this.scaledLoginAmounts.add(ScratchTicketFunctions.scaleScratchReward(this.getLevel(), (Player.CurrencyType)this.rewardTypes.get(i), (Integer)baseRewardAmounts.get(i), false));
            }
         } else {
            for(i = 0; i < this.rewardTypes.size(); ++i) {
               this.scaledLoginAmounts.set(i, ScratchTicketFunctions.scaleScratchReward(this.getLevel(), (Player.CurrencyType)this.rewardTypes.get(i), (Integer)baseRewardAmounts.get(i), false));
            }
         }
      }

   }

   public void updatePendingFromRewardDay() {
      if (this.rewardDay >= 0) {
         this.pendingDailyRewardType = (Player.CurrencyType)this.rewardTypes.get(this.rewardDay);
         this.pendingDailyRewardAmount = (Integer)this.scaledLoginAmounts.get(this.rewardDay);
      } else {
         this.pendingDailyRewardType = Player.CurrencyType.Deferred;
         this.pendingDailyRewardAmount = 1;
      }

   }

   private void addPlayerOnlyData(ISFSObject fullData) {
      fullData.putLong("coins_actual", this.getActualCoins());
      fullData.putLong("diamonds_actual", this.getActualDiamonds());
      fullData.putLong("keys_actual", this.getActualKeys());
      fullData.putLong("ethereal_currency_actual", this.getActualEthCurrency());
      fullData.putLong("food_actual", this.getActualFood());
      fullData.putLong("relics_actual", this.getActualRelics());
      fullData.putLong("egg_wildcards_actual", this.getActualEggWildcards());
      fullData.putLong("starpower_actual", this.getActualStarpower());
      fullData.putInt("relic_diamond_cost", this.curRelicDiamondCost_);
      fullData.putInt("daily_relic_purchase_count", this.dailyRelicsCount_);
      Long nextReset = this.getNextReset();
      if (nextReset != null) {
         fullData.putLong("next_relic_reset", nextReset);
      }

      fullData.putInt("premium", this.hasMadePurchase() ? 1 : 0);
      String currencyKey = this.pendingDailyRewardType.getCurrencyKey();
      fullData.putUtfString("daily_bonus_type", currencyKey);
      fullData.putInt("daily_bonus_amount", this.pendingDailyRewardAmount);
      SFSArray seasonResults;
      int i;
      if (this.scaledLoginAmounts != null) {
         List<Player.CurrencyType> dailyLoginTypes = this.rewardTypes;
         List<Integer> dailyBonusEntities = (List)MSMExtension.getInstance().dailyLoginBonusEntities.get(this.dailyRewardsStage);
         seasonResults = new SFSArray();

         for(i = 0; i < dailyLoginTypes.size(); ++i) {
            SFSObject reward = new SFSObject();
            reward.putUtfString("type", ((Player.CurrencyType)dailyLoginTypes.get(i)).getCurrencyKey());
            reward.putInt("amt", (Integer)this.scaledLoginAmounts.get(i));
            Integer b = (Integer)dailyBonusEntities.get(i);
            reward.putInt("bonus_entity", b == null ? -1 : b);
            seasonResults.addSFSObject(reward);
         }

         fullData.putSFSArray("scaled_daily_reward", seasonResults);
      }

      fullData.putInt("reward_day", this.rewardDay);
      fullData.putLong("nextDailyLogin", MSMExtension.nextDailyTime(GameSettings.get("DAILY_LOGIN_RESET_HOUR", 0), GameSettings.get("DAILY_LOGIN_RESET_MINUTE", 0)).getTimeInMillis());
      int cachedRewardDay = this.activeStreak % (this.rewardTypes.size() - 1) + 1;
      fullData.putInt("cachedRewardDay", cachedRewardDay);
      fullData.putUtfString("extra_ad_params", this.getExtraAdParamString());
      fullData.putLong("speed_up_credit", this.getActualSpeedUpCredit(Player.SPEED_UP_TYPES.VIDEO));
      fullData.putIntArray("player_groups", this.getAllGroups());
      if (this.nextAdScratch != null) {
         fullData.putBool("has_free_ad_scratch", this.nextAdScratch <= MSMExtension.CurrentDBTime());
      }

      ISFSArray achieveArray = new SFSArray();
      Iterator var23 = this.achievements.iterator();

      while(var23.hasNext()) {
         PlayerAchievement a = (PlayerAchievement)var23.next();
         achieveArray.addSFSObject(a.toSFSObject());
      }

      fullData.putSFSArray("achievements", achieveArray);
      fullData.putSFSArray("mailbox", this.mailbox);
      fullData.putBool("new_mail", this.newMail);
      if (this.purchasedDailyCurrencyPack != null) {
         fullData.putSFSObject("daily_currency_pack", this.purchasedDailyCurrencyPack.toSFSObject());
      }

      fullData.putBool("has_promo", this.hasPromotions_);
      fullData.putLong("friend_gift", this.friendGift_);
      fullData.putLong("currencyScratchTime", this.coinScratchTime_);
      fullData.putBool("has_scratch_off_c", this.hasUnclaimedScratchOff("C") || this.getNextScratchOffTime("C") <= MSMExtension.CurrentDBTime());
      fullData.putLong("monsterScratchTime", this.monsterScratchTime_);
      fullData.putBool("has_scratch_off_m", this.hasUnclaimedScratchOff("M") || this.getNextScratchOffTime("M") <= MSMExtension.CurrentDBTime());
      fullData.putLong("monsterScratchTime", this.monsterScratchTime_);
      fullData.putBool("has_scratch_off_s", this.hasUnclaimedScratchOff("S") || this.getNextScratchOffTime("S") <= MSMExtension.CurrentDBTime());
      Long lastFlipFreePlay = this.lastFlipgameFreePlay();
      if (lastFlipFreePlay != null) {
         if (this.hasFreeFlipPlay()) {
            fullData.putLong("flipGameTime", lastFlipFreePlay);
         } else {
            fullData.putLong("flipGameTime", lastFlipFreePlay + 3600L * (long)GameSettings.getInt("MEMORY_TIME_BETWEEN_FREE_PLAYS") * 1000L);
         }
      }

      if (this.inventory_ != null) {
         fullData.putSFSObject("inventory", this.inventory_.toSFSObject());
      }

      if (this.costumes_ != null) {
         fullData.putSFSObject("costumes", this.costumes_.toSFSObject());
      }

      if (this.timedEvents_ != null) {
         fullData.putSFSArray("timed_events", this.timedEvents_.toSFSArray());
      }

      try {
         seasonResults = BattleSeasons.getSeasons();

         for(i = 0; i < seasonResults.size(); ++i) {
            ISFSObject season = seasonResults.getSFSObject(i);
            fullData.putSFSObject("pvpSeason" + i, season);
            if (i != 0) {
               int campaignId = season.getInt("campaign_id");
               long scheduleStart = season.getLong("schedule_started_on");
               int prevTier = -1;
               long prevRank = 0L;
               String selectPlayerSql = "SELECT tier, completed_on FROM user_battle_pvp WHERE campaign_id = ? AND schedule_started_on = FROM_UNIXTIME(?) AND user_id=?";
               SFSArray playerPvpResults = MSMExtension.getInstance().getDB().query(selectPlayerSql, new Object[]{campaignId, scheduleStart / 1000L, this.getPlayerId()});
               if (playerPvpResults.size() > 0) {
                  ISFSObject pvpStatus = playerPvpResults.getSFSObject(0);
                  prevTier = pvpStatus.getInt("tier");
                  if (pvpStatus.containsKey("completed_on") && pvpStatus.getLong("completed_on") > 0L) {
                     BattleVersusChampionRanks.RankedUser ru = BattleVersusChampionRanks.getRankedUser(campaignId, scheduleStart, this.getPlayerId());
                     if (ru != null) {
                        prevRank = ru.rank();
                     } else {
                        prevRank = (long)(BattleVersusChampionRanks.getRankTable(campaignId, scheduleStart).rankedList.size() + 1);
                     }
                  }
               }

               fullData.putInt("prev_tier", prevTier);
               fullData.putLong("prev_rank", prevRank);
            }
         }
      } catch (Exception var19) {
         Logger.trace(var19);
      }

      if (this.viewedPermaCampaigns_ != null) {
         fullData.putIntArray("perma_campaigns_viewed", this.viewedPermaCampaigns_);
      }

      if (this.curGlobalData.lastCollectAll != 0L) {
         fullData.putLong("last_collect_all", this.curGlobalData.lastCollectAll);
      }

      if (this.curGlobalData.lastTimedTheme != null) {
         fullData.putSFSArray("last_timed_theme", this.curGlobalData.lastTimedTheme);
      }

      if (this.curGlobalData.activeIslandTuts != null) {
         fullData.putIntArray("island_tutorials", this.curGlobalData.activeIslandTuts);
      }

      if (this.dailyCumulativeLogin_ != null) {
         fullData.putSFSObject("daily_cumulative_login", this.dailyCumulativeLogin_.toSFSObject());
      }

      fullData.putBool("show_welcomeback", this.showWelcomeBack);
   }

   private void addFriendData(ISFSObject fullData) {
   }

   private void addCommonData(ISFSObject fullData) {
      fullData.putLong("total_starpower_collected", this.totalStarpowerCollected_);
      ISFSArray versusStates = new SFSArray();
      Iterator i = this.islandMap.entrySet().iterator();

      while(i.hasNext()) {
         PlayerIsland s = (PlayerIsland)((Entry)i.next()).getValue();
         versusStates.addSFSObject(s.toSFSObject());
      }

      fullData.putSFSArray("islands", versusStates);
      ArrayList<Integer> ownedThemeIds = new ArrayList();
      i = this.ownedIslandThemes.iterator();

      while(i.hasNext()) {
         ownedThemeIds.add((Integer)((SFSDataWrapper)i.next()).getObject());
      }

      fullData.putIntArray("owned_island_themes", ownedThemeIds);
      ISFSArray activeThemes = new SFSArray();
      Iterator var5 = this.activeIslandThemes.entrySet().iterator();

      while(var5.hasNext()) {
         Entry<Integer, Integer> entry = (Entry)var5.next();
         ISFSObject sfsObj = new SFSObject();
         sfsObj.putInt("i", (Integer)entry.getKey());
         sfsObj.putInt("t", (Integer)entry.getValue());
         activeThemes.addSFSObject(sfsObj);
      }

      fullData.putSFSArray("active_island_themes", activeThemes);
      fullData.putSFSArray("tracks", this.tracks);
      fullData.putSFSArray("songs", this.songs);
      if (this.battleState_ != null) {
         fullData.putSFSObject("battle", this.battleState_.toSFSObject());
      }

      if (this.battleVersusStates_ != null && !this.battleVersusStates_.isEmpty()) {
         versusStates = new SFSArray();

         ISFSObject pvsSFS;
         for(i = this.battleVersusStates_.states().iterator(); i.hasNext(); versusStates.addSFSObject(pvsSFS)) {
            BattlePlayerVersusState playerVersusState = (BattlePlayerVersusState)i.next();
            pvsSFS = playerVersusState.toSFSObject();
            if (playerVersusState.hasCompletedSeason()) {
               BattleVersusChampionRanks.RankedUser ru = BattleVersusChampionRanks.getRankedUser(playerVersusState.getCampaignId(), playerVersusState.getScheduleTimestamp(), this.getPlayerId());
               long rank = 0L;
               if (ru != null) {
                  rank = ru.rank();
               } else {
                  rank = (long)(BattleVersusChampionRanks.getRankTable(playerVersusState.getCampaignId(), playerVersusState.getScheduleTimestamp()).rankedList.size() + 1);
               }

               pvsSFS.putLong("rank", rank);
            }
         }

         fullData.putSFSArray("battle_versus", versusStates);
      }

      if (this.avatar_ != null) {
         fullData.putSFSObject("avatar", this.avatar_.toSFSObject());
      }

      if (this.curGlobalData.monikerId != 0) {
         fullData.putInt("moniker_id", this.curGlobalData.monikerId);
      }

   }

   public ISFSObject toSFSObject() {
      ISFSObject fullData = SFSHelpers.clone(this.getData());
      if (this.fullPlayer_) {
         this.addPlayerOnlyData(fullData);
      } else {
         this.addFriendData(fullData);
      }

      this.addCommonData(fullData);
      return fullData;
   }

   public void updateViewedCampaigns(ISFSObject data) {
      this.viewedPermaCampaigns_ = data.getIntArray("perma_campaigns_viewed");
   }

   public ISFSArray getCurIslandTorchGifts(IDbWrapper db) throws Exception {
      return getCurIslandTorchGifts(this.getBbbId(), this.getActiveIslandId(), db);
   }

   public void addPlayerPropertyData(ISFSArray properties, boolean forIsAdmin) {
      SFSObject prop = new SFSObject();
      prop.putLong("coins_actual", this.getActualCoins());
      properties.addSFSObject(prop);
      prop = new SFSObject();
      prop.putLong("diamonds_actual", this.getActualDiamonds());
      properties.addSFSObject(prop);
      prop = new SFSObject();
      prop.putLong("food_actual", this.getActualFood());
      properties.addSFSObject(prop);
      prop = new SFSObject();
      prop.putLong("ethereal_currency_actual", this.getActualEthCurrency());
      properties.addSFSObject(prop);
      prop = new SFSObject();
      prop.putLong("keys_actual", this.getActualKeys());
      properties.addSFSObject(prop);
      prop = new SFSObject();
      prop.putLong("relics_actual", this.getActualRelics());
      properties.addSFSObject(prop);
      prop = new SFSObject();
      prop.putLong("egg_wildcards_actual", this.getActualEggWildcards());
      properties.addSFSObject(prop);
      prop = new SFSObject();
      prop.putLong("starpower_actual", this.getActualStarpower());
      properties.addSFSObject(prop);
      prop = new SFSObject();
      prop.putInt("xp", this.getXp());
      properties.addSFSObject(prop);
      prop = new SFSObject();
      prop.putInt("level", this.getLevel());
      properties.addSFSObject(prop);
      prop = new SFSObject();
      prop.putUtfString("daily_bonus_type", this.pendingDailyRewardType.getCurrencyKey());
      properties.addSFSObject(prop);
      prop = new SFSObject();
      prop.putInt("daily_bonus_amount", this.pendingDailyRewardAmount);
      properties.addSFSObject(prop);
      prop = new SFSObject();
      prop.putBool("has_free_ad_scratch", this.nextAdScratch <= MSMExtension.CurrentDBTime());
      properties.addSFSObject(prop);
      prop = new SFSObject();
      prop.putLong("daily_relic_purchase_count", (long)this.dailyRelicsCount_);
      properties.addSFSObject(prop);
      prop = new SFSObject();
      prop.putInt("relic_diamond_cost", this.curRelicDiamondCost_);
      properties.addSFSObject(prop);
      Long nextReset = this.getNextReset();
      if (nextReset != null) {
         prop = new SFSObject();
         prop.putLong("next_relic_reset", nextReset);
         properties.addSFSObject(prop);
      }

      prop = new SFSObject();
      prop.putInt("premium", this.hasMadePurchase() ? 1 : 0);
      properties.addSFSObject(prop);
      prop = new SFSObject();
      if (this.cachedEarnedStarpowerCalc == -1) {
         this.calculateEarnedStarpower();
      }

      prop.putInt("earned_starpower", this.cachedEarnedStarpowerCalc);
      properties.addSFSObject(prop);
      prop = new SFSObject();
      prop.putLong("speed_up_credit", this.getActualSpeedUpCredit(Player.SPEED_UP_TYPES.VIDEO));
      properties.addSFSObject(prop);
      prop = new SFSObject();
      prop.putInt("battle_xp", this.battleState_.getXp());
      properties.addSFSObject(prop);
      prop = new SFSObject();
      prop.putInt("battle_level", this.battleState_.getLevel());
      properties.addSFSObject(prop);
      prop = new SFSObject();
      prop.putInt("medals", this.getBattleState().getMedals());
      properties.addSFSObject(prop);
      if (forIsAdmin) {
         prop = new SFSObject();
         prop.putLong("bbb_id", this.getBbbId());
         properties.addSFSObject(prop);
      }

   }

   public void addPlayerPropertyData(ISFSArray properties, EnumSet<Player.PROPERTY> enumProps) {
      SFSObject prop;
      if (enumProps.contains(Player.PROPERTY.COINS)) {
         prop = new SFSObject();
         prop.putLong("coins_actual", this.getActualCoins());
         properties.addSFSObject(prop);
      }

      if (enumProps.contains(Player.PROPERTY.DIAMONDS)) {
         prop = new SFSObject();
         prop.putLong("diamonds_actual", this.getActualDiamonds());
         properties.addSFSObject(prop);
      }

      if (enumProps.contains(Player.PROPERTY.ETH_CURRENCY)) {
         prop = new SFSObject();
         prop.putLong("ethereal_currency_actual", this.getActualEthCurrency());
         properties.addSFSObject(prop);
      }

      if (enumProps.contains(Player.PROPERTY.STARPOWER)) {
         prop = new SFSObject();
         prop.putLong("starpower_actual", this.getActualStarpower());
         properties.addSFSObject(prop);
      }

      if (enumProps.contains(Player.PROPERTY.KEYS)) {
         prop = new SFSObject();
         prop.putLong("keys_actual", this.getActualKeys());
         properties.addSFSObject(prop);
      }

      if (enumProps.contains(Player.PROPERTY.FOOD)) {
         prop = new SFSObject();
         prop.putLong("food_actual", this.getActualFood());
         properties.addSFSObject(prop);
      }

      if (enumProps.contains(Player.PROPERTY.XP)) {
         prop = new SFSObject();
         prop.putInt("xp", this.getXp());
         properties.addSFSObject(prop);
      }

      if (enumProps.contains(Player.PROPERTY.LEVEL)) {
         prop = new SFSObject();
         prop.putInt("level", this.getLevel());
         properties.addSFSObject(prop);
      }

      if (enumProps.contains(Player.PROPERTY.SPEED_UP)) {
         prop = new SFSObject();
         prop.putLong("speed_up_credit", this.getActualSpeedUpCredit(Player.SPEED_UP_TYPES.VIDEO));
         properties.addSFSObject(prop);
      }

      if (enumProps.contains(Player.PROPERTY.RELICS)) {
         prop = new SFSObject();
         prop.putLong("relics_actual", this.getActualRelics());
         properties.addSFSObject(prop);
      }

      if (enumProps.contains(Player.PROPERTY.EGG_WILDCARDS)) {
         prop = new SFSObject();
         prop.putLong("egg_wildcards_actual", this.getActualEggWildcards());
         properties.addSFSObject(prop);
      }

      if (enumProps.contains(Player.PROPERTY.DAILY_RELICS_COUNT)) {
         prop = new SFSObject();
         prop.putLong("daily_relic_purchase_count", (long)this.dailyRelicsCount_);
         properties.addSFSObject(prop);
      }

      if (enumProps.contains(Player.PROPERTY.RELIC_DIAMOND_COST)) {
         prop = new SFSObject();
         prop.putInt("relic_diamond_cost", this.curRelicDiamondCost_);
         properties.addSFSObject(prop);
      }

      if (enumProps.contains(Player.PROPERTY.NEXT_RELIC_RESET)) {
         Long nextReset = this.getNextReset();
         if (nextReset != null) {
            prop = new SFSObject();
            prop.putLong("next_relic_reset", nextReset);
            properties.addSFSObject(prop);
         }
      }

      if (enumProps.contains(Player.PROPERTY.BATTLE_XP)) {
         prop = new SFSObject();
         prop.putInt("battle_xp", this.getXp());
         properties.addSFSObject(prop);
      }

      if (enumProps.contains(Player.PROPERTY.BATTLE_LEVEL)) {
         prop = new SFSObject();
         prop.putInt("battle_level", this.getLevel());
         properties.addSFSObject(prop);
      }

      if (enumProps.contains(Player.PROPERTY.MEDALS)) {
         prop = new SFSObject();
         prop.putInt("medals", this.getBattleState().getMedals());
         properties.addSFSObject(prop);
      }

   }

   public long getLastCommandTime() {
      return this.lastCommandTime;
   }

   public String getLastCommand() {
      return this.lastCommand;
   }

   public void setLastCommand(String c) {
      this.lastCommand = c;
      this.lastCommandTime = MSMExtension.CurrentDBTime();
   }

   public boolean duplicateCommand(String c, long t) {
      return this.lastCommand.equals(c) && this.lastCommandTime + t > MSMExtension.CurrentDBTime();
   }

   public long getMemoryGameTime() {
      return this.memoryGameTime;
   }

   public void setMemoryGameTime(long t) {
      this.memoryGameTime = t;
   }

   public long getPlayerId() {
      return (long)this.data.getInt("user_id");
   }

   public long getBbbId() {
      return this.data.getLong("bbb_id");
   }

   public String getUserGameId() {
      return this.userGameId;
   }

   public ISFSObject getData() {
      return this.data;
   }

   public void setDisplayName(String newName) {
      this.data.putUtfString("display_name", newName);
      this.isDirty = true;
   }

   public String getDisplayName() {
      return this.data.getUtfString("display_name");
   }

   public String toString() {
      return this.getData().getDump();
   }

   public boolean isAdmin() {
      return this.data.getInt("is_admin") > 0;
   }

   public boolean isDirty() {
      return this.isDirty;
   }

   public void setDirty(boolean b) {
      this.isDirty = b;
   }

   public void setSaveAllowed(boolean b) {
      this.saveAllowed_ = b;
   }

   public boolean saveAllowed() {
      return this.saveAllowed_;
   }

   public void setFriendGift(long time) {
      this.friendGift_ = time;
      this.isDirty = true;
   }

   public long getFriendGift() {
      return this.friendGift_;
   }

   public void updateDailyRelicCount() {
      Calendar relicResetTime = MSMExtension.previousDailyTime(GameSettings.get("DAILY_LOGIN_RESET_HOUR", 0), GameSettings.get("DAILY_LOGIN_RESET_MINUTE", 0));
      this.cachedNextReset = (Calendar)relicResetTime.clone();
      this.cachedNextReset.add(5, 1);
      if (this.lastRelicPurchase_ == null || this.lastRelicPurchase_ < relicResetTime.getTimeInMillis()) {
         this.dailyRelicsCount_ = 0;
      }

      this.curRelicDiamondCost_ = getRelicDiamondCostForCount(this.dailyRelicsCount_);
   }

   public PlayerDailyCurrencyPack getPurchasedDailyCurrencyPack() {
      return this.purchasedDailyCurrencyPack;
   }

   public long getLastClaimedFreeScratchAd() {
      return this.lastClaimedFreeScratchAd;
   }

   public long getNextFreeScratchAd() {
      return this.nextAdScratch;
   }

   public Player.CurrencyType getPendingDailyRewardType() {
      return this.pendingDailyRewardType;
   }

   public int getPendingDailyRewardAmount() {
      return this.pendingDailyRewardAmount;
   }

   public int getPendingRewardDay() {
      return this.rewardDay;
   }

   public int cachedRewardDay() {
      return this.activeStreak % (this.rewardTypes.size() - 1) + 1;
   }

   public void claimFreeScratchWithAd() {
      this.updateNextAdScratch(MSMExtension.CurrentDBTime());
   }

   private void updateNextAdScratch(Long claimed) {
      if (claimed == null) {
         this.lastClaimedFreeScratchAd = 0L;
      } else {
         this.lastClaimedFreeScratchAd = claimed;
      }

      long timeDelay = 3600000L * GameSettings.getLong("SCRATCHOFF_HOURS_BETWEEN_AD_PLAYS");
      this.nextAdScratch = this.lastClaimedFreeScratchAd + timeDelay;
      this.isDirty = true;
   }

   private void updateAdScratch() {
      String sql = "SELECT * FROM user_video_ad_credits WHERE bbb_id = ? AND type='EXTRA_SCRATCH'";
      Object[] args = new Object[]{this.getBbbId()};

      try {
         ISFSArray result = MSMExtension.getInstance().getDB().query(sql, args);
         if (result != null && result.size() != 0) {
            this.updateNextAdScratch(result.getSFSObject(0).getLong("last_claimed"));
         } else {
            this.updateNextAdScratch(0L);
         }
      } catch (Exception var4) {
         Logger.trace(var4);
      }

   }

   public void addPurchasedDailyCurrencyPack(DailyCurrencyPack requestedCurrencyPack) {
      if (this.purchasedDailyCurrencyPack == null || this.purchasedDailyCurrencyPack.hasCooldownExpired()) {
         ISFSObject currencyPackSFSObject = new SFSObject();
         currencyPackSFSObject.putLong("user", this.getPlayerId());
         currencyPackSFSObject.putInt("pack_id", requestedCurrencyPack.getID());
         currencyPackSFSObject.putInt("days_collected", 0);
         currencyPackSFSObject.putInt("days_missed", 0);
         long refreshUnit = DailyCurrencyPackLookup.get(requestedCurrencyPack.getID()).getRefreshUnit();
         long refreshPeriod = (long)requestedCurrencyPack.getRefresh();
         long refresh = MSMExtension.CurrentDBTime() + refreshPeriod * refreshUnit;
         currencyPackSFSObject.putLong("reward_refreshes", refresh);
         long lastClaimed = MSMExtension.CurrentDBTime() - refreshPeriod * (refreshUnit / 2L);
         currencyPackSFSObject.putLong("last_claimed_on", lastClaimed);
         long duration = (long)requestedCurrencyPack.getDuration();
         duration = MSMExtension.CurrentDBTime() + duration * refreshUnit;
         currencyPackSFSObject.putLong("expires", duration);
         long cooldown = (long)requestedCurrencyPack.getCooldown();
         cooldown = duration + cooldown * refreshUnit;
         currencyPackSFSObject.putLong("cooldown_expires", cooldown);
         currencyPackSFSObject.putLong("last_processed_refresh", MSMExtension.CurrentDBTime());
         this.purchasedDailyCurrencyPack = new PlayerDailyCurrencyPack(currencyPackSFSObject);
         this.purchasedDailyCurrencyPack.setDirty(true);
      }
   }

   public boolean collectDailyCurrencyPack(User sender, GameStateHandler handler) {
      if (this.purchasedDailyCurrencyPack != null && !this.purchasedDailyCurrencyPack.hasExpired()) {
         if (this.purchasedDailyCurrencyPack.hasPackBeenClaimedToday()) {
            return false;
         } else {
            DailyCurrencyPack currentPack = DailyCurrencyPackLookup.get(this.purchasedDailyCurrencyPack.getPackID());
            if (currentPack.getCurrency().contentEquals("diamonds")) {
               this.adjustDiamonds(sender, handler, currentPack.getAmount());
               MSMExtension.getInstance().stats.trackReward(this, "daily_pack", "diamonds", (long)currentPack.getAmount());
            } else if (currentPack.getCurrency().contentEquals("coins")) {
               this.adjustCoins(sender, handler, currentPack.getAmount());
            } else if (currentPack.getCurrency().contentEquals("food")) {
               this.adjustFood(sender, handler, currentPack.getAmount());
            } else if (currentPack.getCurrency().contentEquals("relics")) {
               this.adjustRelics(sender, handler, currentPack.getAmount());
            } else if (currentPack.getCurrency().contentEquals("keys")) {
               this.adjustKeys(sender, handler, currentPack.getAmount());
            }

            this.purchasedDailyCurrencyPack.claimDailyReward();
            this.setDirty(true);
            return true;
         }
      } else {
         return false;
      }
   }

   public void refreshDailyCurrencyPack() {
      if (this.purchasedDailyCurrencyPack != null && !this.purchasedDailyCurrencyPack.hasExpired()) {
         this.purchasedDailyCurrencyPack.refreshPack();
         this.setDirty(this.purchasedDailyCurrencyPack.isDirty());
      }
   }

   private void updateDailyCurrencyPacks() {
      try {
         String sql = "SELECT * FROM user_daily_currency_packs WHERE user = ?";
         ISFSArray dailyRewardPacks = MSMExtension.getInstance().getDB().query(sql, new Object[]{this.getPlayerId()});
         if (dailyRewardPacks != null) {
            for(int i = 0; i < dailyRewardPacks.size(); ++i) {
               PlayerDailyCurrencyPack dcp = new PlayerDailyCurrencyPack(dailyRewardPacks.getSFSObject(i));
               if (dcp.hasCooldownExpired()) {
                  String deletesql = "DELETE FROM user_daily_currency_packs WHERE user = ? and pack_id = ?";
                  MSMExtension.getInstance().getDB().update(deletesql, new Object[]{this.getPlayerId(), dcp.getPackID()});
               }

               this.purchasedDailyCurrencyPack = dcp;
            }
         }
      } catch (Exception var6) {
         Logger.trace(var6);
      }

   }

   public void updateMail() {
      try {
         String sql = "SELECT * FROM user_mail WHERE user = ?";
         ISFSArray currentMail = MSMExtension.getInstance().getDB().query(sql, new Object[]{this.getPlayerId()});
         sql = "SELECT * FROM mail WHERE message_id >= 1000000 AND bbb_id = ?";
         ISFSArray userMailData = MSMExtension.getInstance().getDB().query(sql, new Object[]{this.getBbbId()});
         ConcurrentHashMap<Long, Mail> userMail = new ConcurrentHashMap();
         ArrayList<Mail> toAdd = new ArrayList();
         int i = 0;

         while(i < userMailData.size()) {
            Mail m = new Mail(userMailData.getSFSObject(i));
            userMail.put(m.getID(), m);
            boolean match = false;
            int j = 0;

            while(true) {
               if (j < currentMail.size()) {
                  ISFSObject cur = currentMail.getSFSObject(j);
                  if (SFSHelpers.getLong("message_id", cur) != m.getID()) {
                     ++j;
                     continue;
                  }

                  match = true;
               }

               if (!match) {
                  toAdd.add(m);
               }

               ++i;
               break;
            }
         }

         this.expireMail(currentMail, userMail);
         this.getValidMail(currentMail, toAdd);
         this.addNewMail(toAdd);
         sql = "SELECT * FROM user_mail WHERE user = ? AND deleted = 0";
         currentMail = MSMExtension.getInstance().getDB().query(sql, new Object[]{this.getPlayerId()});
         this.newMail = toAdd.size() > 0;
         this.mailbox = new SFSArray();

         for(i = 0; i < currentMail.size(); ++i) {
            ISFSObject cur = currentMail.getSFSObject(i);
            Mail m = MailLookup.get(cur.getLong("message_id"));
            if (m == null) {
               m = (Mail)userMail.get(cur.getLong("message_id"));
            }

            if (m != null) {
               ISFSObject mailObj = m.toSFSObject();
               mailObj.putLong("received_on", cur.getLong("received_on"));
               mailObj.putLong("user_mail_id", cur.getLong("user_mail_id"));
               this.mailbox.addSFSObject(mailObj);
            }
         }
      } catch (Exception var11) {
         Logger.trace(var11, "Error updating user mail for user " + this.getPlayerId());
      }

   }

   public int giveTrophy(int trophyID) {
      long messageId = BattleTrophyLookup.getMailboxIdFromTrophyId(trophyID);
      if (messageId == 0L) {
         Logger.trace("ERROR: trophyId " + trophyID + " does not exist in battle_trophies table\n");
      }

      this.addNewTrophyMessage(messageId);
      return BattleTrophyLookup.getEntityIdForTrophy(trophyID);
   }

   private void addNewTrophyMessage(long messageID) {
      try {
         String sql = "SELECT * FROM mail WHERE message_id = ?";
         ISFSArray newMailData = MSMExtension.getInstance().getDB().query(sql, new Object[]{messageID});
         ArrayList<Mail> toAdd = new ArrayList();

         for(int i = 0; i < newMailData.size(); ++i) {
            Mail m = new Mail(newMailData.getSFSObject(i));
            toAdd.add(m);
         }

         if (toAdd.size() > 0) {
            this.addNewMail(toAdd);
            this.updateMail();
         }
      } catch (Exception var8) {
         Logger.trace(var8, "Error adding user mail for user " + this.getPlayerId());
      }

   }

   private void expireMail(ISFSArray currentMail, ConcurrentHashMap<Long, Mail> userMail) throws Exception {
      ArrayList<Long> toDelete = new ArrayList();

      for(int i = 0; i < currentMail.size(); ++i) {
         ISFSObject cur = currentMail.getSFSObject(i);
         if (cur.getInt("deleted") == 0) {
            long messageId = cur.getLong("message_id");
            Mail m = MailLookup.get(messageId);
            if (m == null) {
               m = (Mail)userMail.get(messageId);
            }

            if (m != null) {
               long expiry = (long)m.getExpiry();
               if (expiry == 0L) {
                  Calendar end = (Calendar)Calendar.getInstance(TimeZone.getTimeZone("UTC")).clone();
                  end.setTimeInMillis(m.getEndDate());
                  int calendarField = 5;
                  int calendarValue = 1;
                  if (m.getRepeatMode() == Mail.MailRepeatMode.Weekly) {
                     calendarValue = 7;
                  } else if (m.getRepeatMode() == Mail.MailRepeatMode.Monthly) {
                     calendarField = 2;
                  } else if (m.getRepeatMode() == Mail.MailRepeatMode.Yearly) {
                     calendarField = 1;
                  }

                  end.add(calendarField, calendarValue * m.getCurRepeat());
                  expiry = end.getTimeInMillis() - MSMExtension.CurrentDBTime();
               }

               long rec = cur.getLong("received_on") + expiry * 1000L;
               if (rec < MSMExtension.CurrentDBTime()) {
                  cur.putInt("deleted", 1);
                  toDelete.add(cur.getLong("user_mail_id"));
               }
            }
         }
      }

      if (toDelete.size() > 0) {
         StringBuilder mailBuilder = new StringBuilder();
         mailBuilder.append("UPDATE user_mail SET deleted=1 WHERE user_mail_id IN (");

         for(int i = 0; i < toDelete.size(); ++i) {
            if (i != 0) {
               mailBuilder.append(",");
            }

            mailBuilder.append(toDelete.get(i));
         }

         mailBuilder.append(")");
         MSMExtension.getInstance().getDB().update(mailBuilder.toString());
      }

   }

   private void getValidMail(ISFSArray currentMail, ArrayList<Mail> toAdd) {
      Calendar current = (Calendar)Calendar.getInstance(TimeZone.getTimeZone("UTC")).clone();
      current.setTimeInMillis(MSMExtension.CurrentDBTime());
      Collection<Mail> commonMail = MailLookup.getValidValues();
      Iterator it = commonMail.iterator();

      while(it.hasNext()) {
         Mail m = (Mail)it.next();
         Calendar start = (Calendar)Calendar.getInstance(TimeZone.getTimeZone("UTC")).clone();
         start.setTimeInMillis(m.getStartDate());
         Calendar end = (Calendar)Calendar.getInstance(TimeZone.getTimeZone("UTC")).clone();
         end.setTimeInMillis(m.getEndDate());
         if (m.getRepeatMode() != Mail.MailRepeatMode.Never) {
            int calendarField = 5;
            int calendarValue = 1;
            if (m.getRepeatMode() == Mail.MailRepeatMode.Weekly) {
               calendarValue = 7;
            } else if (m.getRepeatMode() == Mail.MailRepeatMode.Monthly) {
               calendarField = 2;
            } else if (m.getRepeatMode() == Mail.MailRepeatMode.Yearly) {
               calendarField = 1;
            }

            start.add(calendarField, m.getCurRepeat() * calendarValue);
            end.add(calendarField, m.getCurRepeat() * calendarValue);
         }

         boolean match = false;

         for(int j = 0; j < currentMail.size(); ++j) {
            ISFSObject cur = currentMail.getSFSObject(j);
            if (SFSHelpers.getLong("message_id", cur) == m.getID()) {
               Calendar received = (Calendar)Calendar.getInstance(TimeZone.getTimeZone("UTC")).clone();
               received.setTimeInMillis(cur.getLong("received_on"));
               if (start.before(received) && end.after(received)) {
                  match = true;
                  break;
               }
            }
         }

         if (!match && this.segmentationCheck(m.getSegmentation())) {
            toAdd.add(m);
         }
      }

   }

   private boolean segmentationCheck(ISFSObject segmentationData) {
      ISFSArray segments = segmentationData.getSFSArray("segments");
      boolean ands = segmentationData.getUtfString("type").equals("and");

      for(int i = 0; i < segments.size(); ++i) {
         boolean inGroup = MailLookup.inSegment(SFSHelpers.getLong(i, segments), this);
         if (ands && !inGroup) {
            return false;
         }

         if (!ands && inGroup) {
            return true;
         }
      }

      return ands;
   }

   private void addNewMail(ArrayList<Mail> toAdd) throws Exception {
      if (toAdd.size() > 0) {
         StringBuilder mailBuilder = new StringBuilder();
         mailBuilder.append("INSERT INTO user_mail (user, message_id, received_on) VALUES");

         for(int i = 0; i < toAdd.size(); ++i) {
            mailBuilder.append(" ");
            if (i != 0) {
               mailBuilder.append(", ");
            }

            mailBuilder.append("(");
            mailBuilder.append(this.getPlayerId());
            mailBuilder.append(", ");
            mailBuilder.append(((Mail)toAdd.get(i)).getID());
            mailBuilder.append(", NOW())");
         }

         MSMExtension.getInstance().getDB().update(mailBuilder.toString());
      }

   }

   private Long getNextReset() {
      return this.cachedNextReset != null ? this.cachedNextReset.getTimeInMillis() : null;
   }

   static int getRelicDiamondCostForCount(int count) {
      int curRelicDiamondCost = 0;

      try {
         int idx = Math.min(count, MSMExtension.getInstance().relicDiamondCosts.length() - 1);
         curRelicDiamondCost = MSMExtension.getInstance().relicDiamondCosts.getInt(idx);
      } catch (Exception var3) {
         Logger.trace(var3);
      }

      return curRelicDiamondCost;
   }

   public boolean purchaseRelic(User sender, GameStateHandler handler, int amount) {
      if (amount < 1) {
         return false;
      } else {
         this.updateDailyRelicCount();
         int cost = 0;

         for(int i = 0; i < amount; ++i) {
            cost += getRelicDiamondCostForCount(this.dailyRelicsCount_ + i);
         }

         if (!this.canBuy(0L, 0L, (long)cost, 0L, 0L, 0L, 0)) {
            return false;
         } else {
            handler.logDiamondUsage(sender, "convert_currency_to_relics", cost, this.getLevel());
            this.adjustRelics(sender, handler, amount);
            this.chargePlayer(sender, handler, 0, 0, cost, 0L, 0, 0, 0);
            this.lastRelicPurchase_ = MSMExtension.CurrentDBTime();
            this.dailyRelicsCount_ += amount;
            this.isDirty = true;
            this.curRelicDiamondCost_ = getRelicDiamondCostForCount(this.dailyRelicsCount_);
            return true;
         }
      }
   }

   public void chargePlayer(User sender, GameStateHandler handler, int coins, int ethCurrency, int diamonds, long starpower, int keys, int relics, int medals) {
      if (relics > 0) {
         this.adjustRelics(sender, handler, -relics);
      } else if (keys > 0) {
         this.adjustKeys(sender, handler, -keys);
      } else if (starpower > 0L) {
         this.adjustStarpower(sender, handler, -starpower);
      } else if (medals > 0) {
         this.adjustMedals(sender, handler, -medals);
      } else if (diamonds > 0) {
         this.adjustDiamonds(sender, handler, -diamonds);
      } else if (ethCurrency > 0) {
         this.adjustEthCurrency(sender, handler, -ethCurrency);
      } else {
         this.adjustCoins(sender, handler, -coins);
      }

   }

   public void paySecondaryCurrencySellingPrice(User sender, int sellingPrice, PlayerIsland island, GameStateHandler gsh) {
      if (island.isEtherealIsland()) {
         this.adjustEthCurrency(sender, gsh, sellingPrice);
      } else {
         this.adjustCoins(sender, gsh, sellingPrice);
      }

   }

   public boolean canBuy(long coinCost, long etherealCost, long diamondCost, long starpowerCost, long keyCost, long relicCost, int medalCost) {
      if (relicCost > 0L) {
         return this.getActualRelics() >= relicCost;
      } else if (keyCost > 0L) {
         return this.getActualKeys() >= keyCost;
      } else if (starpowerCost > 0L) {
         return this.getActualStarpower() >= starpowerCost;
      } else if (medalCost > 0) {
         return this.getActualMedals() >= medalCost;
      } else if (diamondCost > 0L) {
         return this.getActualDiamonds() >= diamondCost;
      } else if (etherealCost > 0L) {
         return this.getActualEthCurrency() >= etherealCost;
      } else {
         return this.getActualCoins() >= coinCost;
      }
   }

   public boolean canFeed(long foodCost) {
      return this.getActualFood() >= foodCost;
   }

   public boolean hasSpeedUpCredit() {
      return this.getActualSpeedUpCredit(Player.SPEED_UP_TYPES.VIDEO) > 0L;
   }

   public long getActualCoins() {
      return this.coins_;
   }

   public long getActualDiamonds() {
      return this.diamonds_;
   }

   public long getActualEthCurrency() {
      return this.ethCurrency_;
   }

   public long getActualFood() {
      return this.food_;
   }

   public long getActualStarpower() {
      return this.starpower_;
   }

   public long getActualKeys() {
      return this.keys_;
   }

   public long getActualRelics() {
      return this.relics_;
   }

   public long getActualEggWildcards() {
      return this.eggWildcards_;
   }

   public int getActualMedals() {
      return this.battleState_ != null ? this.battleState_.getMedals() : 0;
   }

   public int getDailyRelicsCount() {
      return this.dailyRelicsCount_;
   }

   public Long getLastRelicPurchase() {
      return this.lastRelicPurchase_;
   }

   public long getActualSpeedUpCredit(Player.SPEED_UP_TYPES type) {
      Player.PlayerSpeedUpInfo info = (Player.PlayerSpeedUpInfo)this.speedUpMap.get(type);
      return info != null ? (long)info.credit_amount : 0L;
   }

   public long getTotalStarpower() {
      return this.totalStarpowerCollected_;
   }

   /** @deprecated */
   @Deprecated
   public int getDisplayedCoins() {
      return (int)Math.min(this.getActualCoins(), 1999999999L);
   }

   /** @deprecated */
   @Deprecated
   public int getDisplayedDiamonds() {
      return (int)Math.min(this.getActualDiamonds(), 1999999999L);
   }

   /** @deprecated */
   @Deprecated
   public int getDisplayedKeys() {
      return (int)Math.min(this.getActualKeys(), 1999999999L);
   }

   /** @deprecated */
   @Deprecated
   public int getDisplayedEthCurrency() {
      return (int)Math.min(this.getActualEthCurrency(), 1999999999L);
   }

   /** @deprecated */
   @Deprecated
   public int getDisplayedFood() {
      return (int)Math.min(this.getActualFood(), 1999999999L);
   }

   /** @deprecated */
   @Deprecated
   public int getDisplayedRelics() {
      return (int)Math.min(this.getActualRelics(), 1999999999L);
   }

   /** @deprecated */
   @Deprecated
   public int getDisplayedStarpower() {
      return (int)Math.min(this.getActualStarpower(), 1999999999L);
   }

   public int getXp() {
      return this.data.getInt("xp");
   }

   public int getLevel() {
      return this.data.getInt("level");
   }

   public boolean rewardXp(User sender, GameStateHandler handler, int reward) {
      boolean levelUp = false;
      int currentLevel = this.getLevel();
      int currentXp = this.getXp();
      if (currentLevel < LevelLookup.maxLevel) {
         currentXp += reward;

         for(int neededXp = LevelLookup.get(currentLevel + 1).getXp(); currentXp >= neededXp; neededXp = LevelLookup.get(currentLevel + 1).getXp()) {
            currentXp -= neededXp;
            ++currentLevel;
            levelUp = true;
            Firebase.reportLevel(sender, currentLevel);
            TapjoyTagger.reportLevel(this, sender, currentLevel);
            GooglePlayEvents.reportLevel(sender, currentLevel);
            MSMExtension.getInstance().stats.trackLevelUp(sender, currentLevel);
            if (currentLevel >= LevelLookup.maxLevel) {
               break;
            }
         }

         this.data.putInt("xp", currentXp);
         this.data.putInt("level", currentLevel);
         this.isDirty = true;
      }

      if (levelUp && handler != null) {
         handler.serverQuestEvent(sender, "level", this.getLevel());
      }

      return levelUp;
   }

   public void adjustCoins(User sender, GameStateHandler handler, int amount) {
      this.coins_ += (long)amount;
      this.isDirty = true;
      this.activeIslandStats.coinChange(amount);
      if (amount > 0 && handler != null && sender != null) {
         handler.serverQuestEvent(sender, "coins", this.getActualCoins());
      }

   }

   public void adjustEthCurrency(User sender, GameStateHandler handler, int amount) {
      this.ethCurrency_ += (long)amount;
      this.isDirty = true;
      this.activeIslandStats.ethCurrencyChange(amount);
      if (amount > 0 && handler != null && sender != null) {
         handler.serverQuestEvent(sender, "ethereal_currency", this.getActualEthCurrency());
      }

   }

   public void adjustDiamonds(User sender, GameStateHandler handler, int amount) {
      this.diamonds_ += (long)amount;
      this.isDirty = true;
      this.activeIslandStats.diamondChange(amount);
      if (amount < 0) {
         this.data.putInt("diamonds_spent", this.data.getInt("diamonds_spent") - amount);
      }

      if (amount > 0 && handler != null && sender != null) {
         handler.serverQuestEvent(sender, "diamonds", this.getActualDiamonds());
      }

   }

   public void adjustKeys(User sender, GameStateHandler handler, int amount) {
      this.keys_ += (long)amount;
      this.isDirty = true;
      this.activeIslandStats.keysChange(amount);
      if (amount > 0 && handler != null && sender != null) {
         handler.serverQuestEvent(sender, "keys", this.getActualKeys());
      }

   }

   public void adjustStarpower(User sender, GameStateHandler handler, long amount) {
      this.starpower_ += amount;
      this.isDirty = true;
      this.activeIslandStats.starpowerCurrencyChange(amount);
      if (amount > 0L && handler != null && sender != null) {
         handler.serverQuestEvent(sender, "starpower", this.getActualStarpower());
      }

   }

   public void adjustMedals(User sender, GameStateHandler handler, int amount) {
      this.battleState_.adjustMedals(amount);
      this.saveBattleState();
      this.activeIslandStats.medalsChange((long)amount);
      if (amount > 0 && handler != null && sender != null) {
         handler.serverQuestEvent(sender, "medals", this.getActualMedals());
      }

   }

   public void adjustRelics(User sender, GameStateHandler handler, int amount) {
      this.relics_ += (long)amount;
      this.isDirty = true;
      this.activeIslandStats.relicsChange(amount);
      if (amount > 0 && handler != null && sender != null) {
         handler.serverQuestEvent(sender, "relics", this.getActualRelics());
      }

   }

   public void adjustEggWildcards(User sender, GameStateHandler handler, int amount) {
      this.eggWildcards_ += (long)amount;
      this.isDirty = true;
      this.activeIslandStats.eggWildcardsChange(amount);
      if (amount > 0 && handler != null && sender != null) {
         handler.serverQuestEvent(sender, "egg_wildcards", this.getActualEggWildcards());
      }

   }

   public void adjustFood(User sender, GameStateHandler handler, int amount) {
      this.food_ += (long)amount;
      this.isDirty = true;
      this.activeIslandStats.foodChange(amount);
      if (amount > 0 && handler != null && sender != null) {
         handler.serverQuestEvent(sender, "food", this.getActualFood());
      }

   }

   public void adjustSpeedUpCredit(User sender, GameStateHandler handler, int amount, Player.SPEED_UP_TYPES type) {
      this.isDirty = true;
      Player.PlayerSpeedUpInfo info = (Player.PlayerSpeedUpInfo)this.speedUpMap.get(type);
      info.credit_amount += amount;
      info.status = info.status == Player.SPEED_UP_STATUS.NEW ? info.status : Player.SPEED_UP_STATUS.MODIFIED;
      info.last_claimed_on = this.refreshLastClaimedOn(info.last_claimed_on) ? MSMExtension.CurrentDBTime() : info.last_claimed_on;
      this.activeIslandStats.speedUpCreditChange(amount);
      if (type == Player.SPEED_UP_TYPES.VIDEO && amount > 0) {
         if (this.totalSpeedups_ == 0) {
            Firebase.reportSpeedup(sender);
         }

         this.totalSpeedups_ += amount;
      }

      if (amount > 0 && handler != null && sender != null) {
         handler.serverQuestEvent(sender, "speed_up_credit", this.getActualSpeedUpCredit(type));
      }

   }

   public void calculateMonsterIndex() {
      long monsterIndex = 0L;
      Iterator var3 = this.islandMap.values().iterator();

      while(true) {
         PlayerIsland island;
         do {
            if (!var3.hasNext()) {
               this.lastMonsterIndex = monsterIndex;

               try {
                  HashMap<Long, PlayerIsland> fixedIslands = new HashMap();
                  HashMap<Long, PlayerMonster> composerMonsters = new HashMap();
                  List<MonsterFixup> monsterFixes = new ArrayList();
                  Iterator var20 = this.islandMap.values().iterator();

                  label124:
                  while(true) {
                     PlayerIsland island;
                     Iterator var8;
                     PlayerMonster monster;
                     do {
                        if (!var20.hasNext()) {
                           var20 = this.islandMap.values().iterator();

                           label109:
                           while(true) {
                              class MonsterFixup {
                                 PlayerIsland island;
                                 long oldId;
                                 long newId;

                                 MonsterFixup(PlayerIsland i, long o, long n) {
                                    this.island = i;
                                    this.oldId = o;
                                    this.newId = n;
                                 }
                              }

                              do {
                                 do {
                                    if (!var20.hasNext()) {
                                       var20 = monsterFixes.iterator();

                                       while(var20.hasNext()) {
                                          MonsterFixup mf = (MonsterFixup)var20.next();
                                          mf.island.changeMonsterID(mf.oldId, mf.newId);
                                       }

                                       var20 = fixedIslands.values().iterator();

                                       while(var20.hasNext()) {
                                          island = (PlayerIsland)var20.next();
                                          Logger.trace(String.format("COMPOSER_CRASH: Saving island %d for Player %d", island.getID(), this.getBbbId()));
                                          MSMExtension.getInstance().savePlayerIsland(this, island, false);
                                       }
                                       break label124;
                                    }

                                    island = (PlayerIsland)var20.next();
                                 } while(island.isTribalIsland());
                              } while(island.isComposerIsland());

                              var8 = island.getMonsters().iterator();

                              while(true) {
                                 while(true) {
                                    label96:
                                    while(true) {
                                       do {
                                          if (!var8.hasNext()) {
                                             continue label109;
                                          }

                                          monster = (PlayerMonster)var8.next();
                                       } while(!composerMonsters.containsKey(monster.getID()));

                                       long oldId = monster.getID();
                                       monster.setID(this.getNextMonsterIndex());
                                       monsterFixes.add(new MonsterFixup(island, oldId, monster.getID()));
                                       Logger.trace(String.format("COMPOSER_CRASH: Player %d has duplicate monster with id %d, changed to id %d", this.getBbbId(), oldId, monster.getID()));
                                       if (!fixedIslands.containsKey(island.getID())) {
                                          fixedIslands.put(island.getID(), island);
                                       }

                                       Iterator var12 = this.islandMap.values().iterator();

                                       while(var12.hasNext()) {
                                          PlayerIsland newIsland = (PlayerIsland)var12.next();
                                          if (newIsland.isGoldIsland()) {
                                             Iterator var14 = newIsland.getMonsters().iterator();

                                             while(var14.hasNext()) {
                                                PlayerMonster newMonster = (PlayerMonster)var14.next();
                                                if (newMonster.getParentIslandData() != null && newMonster.getParentIslandData().getLong("monster") == oldId) {
                                                   newMonster.setParentIslandData(island.getID(), monster.getID());
                                                   Logger.trace(String.format("COMPOSER_CRASH: Changed Gold Island monster parent for Player %d from %d to %d", this.getBbbId(), oldId, monster.getID()));
                                                   if (!fixedIslands.containsKey(newIsland.getID())) {
                                                      fixedIslands.put(newIsland.getID(), newIsland);
                                                   }
                                                   continue label96;
                                                }
                                             }
                                             break;
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }

                        island = (PlayerIsland)var20.next();
                     } while(!island.isComposerIsland());

                     var8 = island.getMonsters().iterator();

                     while(var8.hasNext()) {
                        monster = (PlayerMonster)var8.next();
                        if (!composerMonsters.containsKey(monster.getID())) {
                           composerMonsters.put(monster.getID(), monster);
                        }
                     }
                  }
               } catch (Exception var16) {
                  Logger.trace(var16, "**** error fixing islands ****");
               }

               return;
            }

            island = (PlayerIsland)var3.next();
         } while(island.isTribalIsland());

         PlayerMonster monster;
         for(Iterator var5 = island.getMonsters().iterator(); var5.hasNext(); monsterIndex = Math.max(monsterIndex, monster.getID())) {
            monster = (PlayerMonster)var5.next();
         }
      }
   }

   public long getNextMonsterIndex() {
      return ++this.lastMonsterIndex;
   }

   public boolean hasNewerContent(VersionInfo clientVersion) {
      VersionInfo prevServerContentVer = VersionData.Instance().getMaxServerVersionFromClientVersion(this.lastClientVersion_);
      VersionInfo currentlySupportedServerContentVer = VersionData.Instance().getMaxServerVersionFromClientVersion(clientVersion);
      return currentlySupportedServerContentVer.compareTo(prevServerContentVer) < 0;
   }

   public boolean hasBoxMonster(boolean rare, boolean epic) {
      Iterator var3 = this.islandMap.values().iterator();

      PlayerIsland island;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         island = (PlayerIsland)var3.next();
      } while(!island.hasInactiveBoxMonster(rare, epic));

      return true;
   }

   public long getMonsterIslandId(long monsterId) {
      Iterator var3 = this.islandMap.values().iterator();

      while(var3.hasNext()) {
         PlayerIsland island = (PlayerIsland)var3.next();
         Iterator var5 = island.getMonsters().iterator();

         while(var5.hasNext()) {
            PlayerMonster monster = (PlayerMonster)var5.next();
            if (monster.getID() == monsterId) {
               return island.getID();
            }
         }
      }

      return 0L;
   }

   public void initIslands(ISFSArray islandsData) {
      this.islandMap.clear();
      Iterator i = islandsData.iterator();

      while(i.hasNext()) {
         PlayerIsland island = new PlayerIsland((ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject()));
         this.islandMap.put(island.getID(), island);
      }

   }

   public Collection<PlayerIsland> getIslands() {
      return this.islandMap.values();
   }

   public PlayerIsland getIslandByID(long id) {
      return (PlayerIsland)this.islandMap.get(id);
   }

   public void addIsland(PlayerIsland island) {
      this.islandMap.put(island.getID(), island);
   }

   public long getActiveIslandId() {
      return this.data.getLong("active_island");
   }

   public PlayerIsland getActiveIsland() {
      return (PlayerIsland)this.islandMap.get(this.data.getLong("active_island"));
   }

   public void setActiveIsland(long islandId) {
      this.getActiveIslandStats().islandClosed(this);
      this.data.putLong("active_island", islandId);
      this.isDirty = true;
      this.getActiveIslandStats().islandOpened(this);
   }

   public PlayerIsland getIslandByIslandIndex(int islandId) {
      Iterator var2 = this.islandMap.entrySet().iterator();

      PlayerIsland island;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         Entry<Long, PlayerIsland> entry = (Entry)var2.next();
         island = (PlayerIsland)entry.getValue();
      } while(island.getIndex() != islandId);

      return island;
   }

   public void initIslandThemes(ISFSArray islandThemeData) {
      this.ownedIslandThemes = new SFSArray();
      this.activeIslandThemes.clear();
      Iterator i = islandThemeData.iterator();

      while(i.hasNext()) {
         ISFSObject object = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
         String ownedString = object.getUtfString("owned");
         String activeMapString = object.getUtfString("active");
         new SFSArray();
         if (ownedString == null || ownedString.isEmpty()) {
            ownedString = "[]";
         }

         ISFSArray sfs = SFSArray.newFromJsonData(ownedString);

         for(int j = 0; j < sfs.size(); ++j) {
            this.addIslandTheme(sfs.getInt(j));
         }

         try {
            ISFSArray activeArray = SFSArray.newFromJsonData(activeMapString);

            for(int j = 0; j < activeArray.size(); ++j) {
               if (!activeArray.isNull(j)) {
                  ISFSObject sfsObj = (SFSObject)activeArray.getElementAt(j);
                  this.activeIslandThemes.put(new Integer(sfsObj.getInt("i")), new Integer(sfsObj.getInt("t")));
               }
            }
         } catch (Exception var10) {
            Logger.trace(var10);
         }
      }

   }

   public ISFSArray getIslandThemes() {
      return this.ownedIslandThemes;
   }

   public void addIslandTheme(int themeId) {
      this.ownedIslandThemes.addInt(new Integer(themeId));
   }

   public HashMap<Integer, Integer> getActiveIslandThemes() {
      return this.activeIslandThemes;
   }

   public int hasTrialTheme(int islandId) {
      PlayerIsland pi = this.getIslandByIslandIndex(islandId);
      return pi != null ? pi.getIslandThemeTrial() : 0;
   }

   public void removeTrialTheme(int islandId) {
      PlayerIsland pi = this.getIslandByIslandIndex(islandId);
      if (pi != null) {
         pi.setIslandThemeTrial(0);
      }

   }

   private int removeOldTheme(int islandId) {
      int oldTheme = -1;
      if (this.activeIslandThemes.containsKey(new Integer(islandId))) {
         oldTheme = (Integer)this.activeIslandThemes.get(new Integer(islandId));
         this.activeIslandThemes.remove(new Integer(islandId));
      }

      PlayerIsland pi = this.getIslandByIslandIndex(islandId);
      if (pi != null && pi.getIslandThemeTrial() != 0) {
         oldTheme = pi.getIslandThemeTrial();
         pi.setIslandThemeTrial(0);
      }

      return oldTheme;
   }

   public void toggleIslandTheme(int islandId, int themeId) {
      int oldTheme = this.removeOldTheme(islandId);
      if (themeId != oldTheme) {
         this.activeIslandThemes.put(new Integer(islandId), new Integer(themeId));
      }

      this.isDirty = true;
   }

   public boolean hasIslandThemeEnabled(int islandId) {
      return this.activeIslandThemes.containsKey(new Integer(islandId));
   }

   public boolean ownsIslandTheme(int islandThemeId) {
      return this.ownedIslandThemes.contains(new Integer(islandThemeId));
   }

   public void toggleTrialIslandTheme(int islandId, int themeId) {
      int oldTheme = this.removeOldTheme(islandId);
      if (themeId != oldTheme) {
         PlayerIsland pi = this.getIslandByIslandIndex(islandId);
         if (pi != null) {
            pi.setIslandThemeTrial(themeId);
         }
      }

   }

   public void initGlobalData(ISFSObject rowData, boolean fullPlayer) {
      String strData = rowData.getUtfString("global_data");
      if (strData != null && !strData.isEmpty()) {
         ISFSObject globalData = SFSObject.newFromJsonData(strData);
         if (fullPlayer) {
            if (globalData.containsKey("last_collect_all")) {
               this.curGlobalData.lastCollectAll = globalData.getLong("last_collect_all");
            }

            if (globalData.containsKey("last_timed_theme")) {
               this.curGlobalData.lastTimedTheme = globalData.getSFSArray("last_timed_theme");
            }

            if (globalData.containsKey("island_tutorials")) {
               this.curGlobalData.activeIslandTuts = globalData.getIntArray("island_tutorials");
            }
         }

         if (globalData.containsKey("moniker_id")) {
            this.curGlobalData.monikerId = globalData.getInt("moniker_id");
         }
      }

      this.cleanGlobalData();
   }

   public void updateTimedTheme(int themeId, int tutStage) {
      if (themeId == 0) {
         this.curGlobalData.lastTimedTheme = new SFSArray();
      } else {
         if (this.curGlobalData.lastTimedTheme == null) {
            this.curGlobalData.lastTimedTheme = new SFSArray();
         }

         boolean themeIdFound = false;

         for(int i = 0; i < this.curGlobalData.lastTimedTheme.size(); ++i) {
            ISFSObject s = this.curGlobalData.lastTimedTheme.getSFSObject(i);
            if (s.getInt("theme_id") == themeId) {
               if (tutStage == 0) {
                  this.curGlobalData.lastTimedTheme.removeElementAt(i);
               } else {
                  s.putInt("tut_stage", tutStage);
               }

               themeIdFound = true;
               break;
            }
         }

         if (!themeIdFound) {
            SFSObject newTimedTheme = new SFSObject();
            newTimedTheme.putInt("theme_id", themeId);
            newTimedTheme.putInt("tut_stage", tutStage);
            this.curGlobalData.lastTimedTheme.addSFSObject(newTimedTheme);
         }
      }

   }

   public void updateIslandTutorials(Collection<Integer> islandTuts) {
      this.curGlobalData.activeIslandTuts = islandTuts;
   }

   public void setWelcomeBack(boolean b) {
      this.showWelcomeBack = b;
   }

   public boolean setMonikerId(int id) {
      Level lvl = LevelLookup.get(id);
      if (lvl != null && !lvl.getTitle().isEmpty()) {
         if (lvl.getLevel() > this.getLevel()) {
            return false;
         } else {
            this.curGlobalData.monikerId = id;
            return true;
         }
      } else {
         return false;
      }
   }

   public boolean globalDataIsDirty() {
      return !this.curGlobalData.toSFSObject().toJson().equals(this.prevGlobalDataStr);
   }

   public void cleanGlobalData() {
      this.prevGlobalDataStr = this.curGlobalData.toSFSObject().toJson();
   }

   public ISFSObject globalDataSfs() {
      return this.curGlobalData.toSFSObject();
   }

   public long lastCollectAll() {
      return this.curGlobalData.lastCollectAll;
   }

   public void collectAllFromIsland(Player player, PlayerIsland island, User sender, GameStateHandler handler, SFSArray collectResponses, SFSArray monsterUpdateResponses) {
      if (this.curGlobalData.lastCollectAll != 0L) {
         long numSec = (MSMExtension.CurrentDBTime() - this.curGlobalData.lastCollectAll) / 1000L;
         long nonpayingWaitSec = (long)MSMExtension.getInstance().collectAllWaitMinutesNonPaying * 60L;
         if (!player.hasMadePurchase() && !TimedEventManager.instance().hasTimedEventNow(TimedEventType.CollectAllTrial, 0, island.getType()) && numSec < nonpayingWaitSec) {
            return;
         }
      }

      island.collectAll(player, sender, handler, collectResponses, monsterUpdateResponses);
      this.curGlobalData.lastCollectAll = MSMExtension.CurrentDBTime();
   }

   public void calculateQuestIndex() {
      int questIndex = 0;

      PlayerQuest quest;
      for(Iterator var2 = this.quests.iterator(); var2.hasNext(); questIndex = Math.max(questIndex, quest.getDataId())) {
         quest = (PlayerQuest)var2.next();
      }

      this.lastQuestIndex = questIndex;
   }

   public int getNextQuestIndex() {
      return ++this.lastQuestIndex;
   }

   public boolean hasCollectedQuest(int questId) {
      Iterator var2 = this.collectedQuests_.iterator();

      Integer i;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         i = (Integer)var2.next();
      } while(i != questId);

      return true;
   }

   public boolean hasActiveQuest(int questId) {
      Iterator var2 = this.quests.iterator();

      PlayerQuest q;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         q = (PlayerQuest)var2.next();
      } while(q.getDataId() != questId);

      return true;
   }

   public boolean hasQuest(int questId) {
      return this.hasCollectedQuest(questId) || this.hasActiveQuest(questId);
   }

   public void addCollectedQuest(int questId) {
      if (!this.hasCollectedQuest(questId)) {
         this.collectedQuests_.add(questId);
      }

   }

   public ArrayList<Integer> getCollectedQuests() {
      return this.collectedQuests_;
   }

   public void initQuests(ISFSArray questData) throws Exception {
      this.quests.clear();
      this.questEvents.clear();
      this.collectedQuests_.clear();
      if (questData != null && questData.size() != 0) {
         ISFSObject userQuestData = questData.getSFSObject(0);
         String activeQuests = userQuestData.getUtfString("active");
         String collectedQuests = userQuestData.getUtfString("completed");
         JSONArray ja;
         int i;
         if (activeQuests != null && !activeQuests.isEmpty()) {
            activeQuests = Helpers.decompressJsonDataField(activeQuests, "[]");
            ja = new JSONArray(activeQuests);

            for(i = 0; i < ja.length(); ++i) {
               if (!ja.isNull(i)) {
                  PlayerQuest quest = new PlayerQuest(this, ja.getString(i));
                  if (!quest.invalid()) {
                     this.quests.add(quest);
                     this.addQuestEvents(quest);
                  }
               }
            }
         }

         if (collectedQuests != null && !collectedQuests.isEmpty()) {
            collectedQuests = Helpers.decompressJsonDataField(collectedQuests, "[]");
            ja = new JSONArray(collectedQuests);

            for(i = 0; i < ja.length(); ++i) {
               if (!ja.isNull(i) && QuestLookup.getById(ja.getInt(i), this.getPlatform(), this.getSubplatform(), this.lastClientVersion()) != null) {
                  this.addCollectedQuest(ja.getInt(i));
               }
            }
         }

         this.giveInitialQuests();
         this.giveRetroactiveTriggeredQuests();
      }
   }

   private void giveInitialQuests() {
      Collection<ISFSObject> quests = QuestLookup.getValues();
      Iterator var2 = quests.iterator();

      while(var2.hasNext()) {
         ISFSObject q = (ISFSObject)var2.next();
         int initial = SFSHelpers.getInt("initial", q);
         int questId = SFSHelpers.getInt("id", q);
         if (initial == 1 && !this.hasQuest(questId)) {
            ISFSObject newQuestData = PlayerQuest.getInitialSFSObject(this.getNextQuestIndex(), questId, (int)this.getPlayerId(), "false", true, false);
            this.addQuest(newQuestData);
         }
      }

      this.giveTutorialGroupSpecificQuests();
   }

   public void giveTutorialGroupSpecificQuests() {
      TutorialGroup t = this.getTutorialGroup();
      ISFSArray initialQuests;
      if (t == null) {
         initialQuests = QuestLookup.getDefaultTutorialQuests();
      } else {
         initialQuests = t.getInitialQuests();
      }

      this.addTutorialQuests(initialQuests);
   }

   private void addTutorialQuests(ISFSArray questArray) {
      if (questArray != null) {
         for(int i = 0; i < questArray.size(); ++i) {
            ISFSObject quest = QuestLookup.getByName(questArray.getUtfString(i));
            int questId = SFSHelpers.getInt("id", quest);
            if (!this.hasQuest(questId)) {
               ISFSObject newQuestData = PlayerQuest.getInitialSFSObject(this.getNextQuestIndex(), questId, (int)this.getPlayerId(), "false", true, false);
               this.addQuest(newQuestData);
            }
         }

      }
   }

   private void giveRetroactiveTriggeredQuests() {
      Set<Integer> newQuests = new HashSet();
      ArrayList<PlayerQuest> activeQuests = this.getQuests();
      Iterator var3 = activeQuests.iterator();

      while(true) {
         PlayerQuest q;
         int id;
         do {
            if (!var3.hasNext()) {
               ArrayList<Integer> collectedQuests = this.getCollectedQuests();
               Iterator var12 = collectedQuests.iterator();

               Integer id;
               ISFSObject newQuestData;
               while(var12.hasNext()) {
                  id = (Integer)var12.next();
                  newQuestData = QuestLookup.getById(id, this.getPlatform(), this.getSubplatform(), this.lastClientVersion());
                  ISFSArray a = newQuestData.getSFSArray("next");

                  for(id = 0; id < a.size(); ++id) {
                     ISFSObject nq = QuestLookup.getByName(a.getSFSObject(id).getUtfString("quest"));
                     int id = SFSHelpers.getInt("id", nq);
                     if (!this.hasQuest(id)) {
                        newQuests.add(id);
                     }
                  }
               }

               var12 = newQuests.iterator();

               while(var12.hasNext()) {
                  id = (Integer)var12.next();
                  newQuestData = PlayerQuest.getInitialSFSObject(this.getNextQuestIndex(), id, (int)this.getPlayerId(), "false", true, false);
                  this.addQuest(newQuestData);
               }

               return;
            }

            q = (PlayerQuest)var3.next();
         } while(!q.isComplete());

         ISFSArray a = q.getNext();

         for(int i = 0; i < a.size(); ++i) {
            ISFSObject nq = QuestLookup.getByName(a.getSFSObject(i).getUtfString("quest"));
            id = SFSHelpers.getInt("id", nq);
            if (!this.hasQuest(id)) {
               newQuests.add(id);
            }
         }
      }
   }

   private void addQuestEvents(PlayerQuest quest) {
      ArrayList<String> events = quest.getGoalEvents();

      for(int i = 0; i < events.size(); ++i) {
         String event = (String)events.get(i);
         if (!this.questEvents.containsKey(event)) {
            this.questEvents.put(event, 1);
         } else {
            this.questEvents.put(event, (Integer)this.questEvents.get(event) + 1);
         }
      }

   }

   public void removeQuestEvents(PlayerQuest quest) {
      ArrayList<String> events = quest.getGoalEvents();

      for(int i = 0; i < events.size(); ++i) {
         String event = (String)events.get(i);
         if (this.questEvents.containsKey(event)) {
            int newValue = (Integer)this.questEvents.get(event) - 1;
            if (newValue <= 0) {
               this.questEvents.remove(event);
            } else {
               this.questEvents.put(event, newValue);
            }
         } else {
            Logger.trace("*** ERROR: The key should exist in the quest events! ***\n");
         }
      }

   }

   public PlayerQuest addQuest(ISFSObject logData) {
      PlayerQuest newQuest = new PlayerQuest(this, logData);
      if (!newQuest.invalid()) {
         this.quests.add(newQuest);
         this.addQuestEvents(newQuest);
         return newQuest;
      } else {
         return null;
      }
   }

   public boolean killActiveQuest(int questId) {
      for(int i = 0; i < this.quests.size(); ++i) {
         if (((PlayerQuest)this.quests.get(i)).getDataId() == questId) {
            this.removeQuestEvents((PlayerQuest)this.quests.get(i));
            this.quests.remove(i);
            return true;
         }
      }

      return false;
   }

   public boolean questComplete(int questId) {
      for(int i = 0; i < this.quests.size(); ++i) {
         if (((PlayerQuest)this.quests.get(i)).getDataId() == questId) {
            return ((PlayerQuest)this.quests.get(i)).isComplete();
         }
      }

      return false;
   }

   public boolean hasQuestGoal(String goal) {
      return this.questEvents.containsKey(goal);
   }

   public boolean hasQuestGoals(ISFSObject goals) {
      String[] keys = (String[])goals.getKeys().toArray(new String[0]);

      for(int j = 0; j < keys.length; ++j) {
         if (this.hasQuestGoal(keys[j])) {
            return true;
         }
      }

      return false;
   }

   public ArrayList<PlayerQuest> getQuests() {
      return this.quests;
   }

   public PlayerQuest getQuestByUid(long userQuestId) {
      Iterator var3 = this.quests.iterator();

      PlayerQuest q;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         q = (PlayerQuest)var3.next();
      } while(q.getId() != userQuestId);

      return q;
   }

   public void initAchievements(ISFSArray achievementData) {
      this.achievements.clear();
      Iterator i = achievementData.iterator();

      while(i.hasNext()) {
         PlayerAchievement pAchievement = new PlayerAchievement((ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject()));
         if (!pAchievement.invalid()) {
            this.achievements.add(pAchievement);
         }
      }

   }

   public PlayerAchievement addAchievement(PlayerAchievement achievement) {
      this.achievements.add(achievement);
      return achievement;
   }

   public ArrayList<PlayerAchievement> getAchievements() {
      return this.achievements;
   }

   public PlayerAchievement getAchievement(Long achievementId) {
      for(int i = 0; i < this.achievements.size(); ++i) {
         if (((PlayerAchievement)this.achievements.get(i)).getID().equals(achievementId)) {
            return (PlayerAchievement)this.achievements.get(i);
         }
      }

      return null;
   }

   public long getNextScratchOffTime(String type) {
      if (!type.equalsIgnoreCase("C") && !type.equalsIgnoreCase("S")) {
         return type.equalsIgnoreCase("M") ? this.monsterScratchTime_ : Long.MAX_VALUE;
      } else {
         return this.coinScratchTime_;
      }
   }

   public String scratchOffKey(String type) {
      if (type.equalsIgnoreCase("M")) {
         return "monsterScratchTime";
      } else {
         return !type.equalsIgnoreCase("C") && !type.equalsIgnoreCase("S") ? "" : "currencyScratchTime";
      }
   }

   public String hasScratchOffKey(String type) {
      if (type.equalsIgnoreCase("M")) {
         return "has_scratch_off_m";
      } else {
         return type.equalsIgnoreCase("S") ? "has_scratch_off_s" : "has_scratch_off_c";
      }
   }

   public void setNextScratchOffTime(String type, long time) {
      if (!type.equalsIgnoreCase("C") && !type.equalsIgnoreCase("S")) {
         if (type.equalsIgnoreCase("M")) {
            this.monsterScratchTime_ = time;
         }
      } else {
         this.coinScratchTime_ = time;
      }

   }

   public boolean hasUnclaimedScratchOff(String type) {
      if (type.equalsIgnoreCase("C")) {
         return this.coinScratchUncollected_;
      } else if (type.equalsIgnoreCase("M")) {
         return this.monsterScratchUncollected_;
      } else {
         return type.equalsIgnoreCase("S") ? this.spinWheelUncollected_ : false;
      }
   }

   public void setUnclaimedScratchOff(String type, boolean b) {
      if (type.equalsIgnoreCase("C")) {
         this.coinScratchUncollected_ = b;
      } else if (type.equalsIgnoreCase("M")) {
         this.monsterScratchUncollected_ = b;
      } else if (type.equalsIgnoreCase("S")) {
         this.spinWheelUncollected_ = b;
      }

   }

   public String getScratchPrize(String type) {
      if (type.equalsIgnoreCase("C")) {
         return this.coinScratchPrize_;
      } else if (type.equalsIgnoreCase("M")) {
         return this.monsterScratchPrize_;
      } else {
         return type.equalsIgnoreCase("S") ? this.spinWheelPrize_ : null;
      }
   }

   public void setScratchPrize(String type, String s) {
      if (type.equalsIgnoreCase("C")) {
         this.coinScratchPrize_ = s;
      } else if (type.equalsIgnoreCase("M")) {
         this.monsterScratchPrize_ = s;
      } else if (type.equalsIgnoreCase("S")) {
         this.spinWheelPrize_ = s;
      }

   }

   public Integer getFacebookInviteReward() {
      return this.data.getInt("fb_invite_reward");
   }

   public void setFacebookInviteReward(int value) {
      this.data.putInt("fb_invite_reward", value);
      this.isDirty = true;
   }

   public Integer getTwitterInviteReward() {
      return this.data.getInt("twitter_invite_reward");
   }

   public void setTwitterInviteReward(int value) {
      this.data.putInt("twitter_invite_reward", value);
      this.isDirty = true;
   }

   public Integer getEmailInviteReward() {
      return this.data.getInt("email_invite_reward");
   }

   public void setEmailInviteReward(int value) {
      this.data.putInt("email_invite_reward", value);
      this.isDirty = true;
   }

   public Long getLastFacebookPostReward() {
      return this.data.getLong("last_fb_post_reward");
   }

   public void setLastFacebookPostReward(long value) {
      this.data.putLong("last_fb_post_reward", value);
      this.isDirty = true;
   }

   public int cachedEarnedStarpower() {
      return this.cachedEarnedStarpowerCalc == -1 ? this.calculateEarnedStarpower() : this.cachedEarnedStarpowerCalc;
   }

   public int calculateEarnedStarpower() {
      this.cachedEarnedStarpowerCalc = 0;
      PlayerIsland tribalIsland = this.getIslandByIslandIndex(9);
      if (tribalIsland != null) {
         List<PlayerMonster> sortedMonsters = new ArrayList();
         Iterator monsters = tribalIsland.getMonsters().iterator();

         while(monsters.hasNext()) {
            sortedMonsters.add(monsters.next());
         }

         Collections.sort(sortedMonsters, new Comparator<PlayerMonster>() {
            public int compare(PlayerMonster a, PlayerMonster b) {
               return a.getLevel() - b.getLevel();
            }
         });
         int i = 2;

         for(Iterator monsters = sortedMonsters.iterator(); monsters.hasNext(); ++i) {
            PlayerMonster pm = (PlayerMonster)monsters.next();
            this.cachedEarnedStarpowerCalc = (int)((long)this.cachedEarnedStarpowerCalc + Math.round((double)pm.getLevel() * 6.0D * Math.pow((double)i, -0.8D)));
            if (pm.getID() == this.getPlayerId()) {
               this.cachedEarnedStarpowerCalc += pm.getLevel() * 5;
            }
         }
      }

      return this.cachedEarnedStarpowerCalc;
   }

   public int getInGamePurchaseCount() {
      return this.data.getInt("purchases_total");
   }

   public void setInGamePurchaseCount(int purchaseCount) {
      this.data.putInt("purchases_total", purchaseCount);
   }

   public int getInGamePurchaseAmountTotal() {
      return this.data.getInt("purchases_amount");
   }

   public void setInGamePurchaseAmountTotal(int purchaseAmountTotal) {
      this.data.putInt("purchases_amount", purchaseAmountTotal);
   }

   public boolean getThirdpartyAds() {
      return this.data.getBool("third_party_ads");
   }

   public void setThirdpartyAds(boolean shouldSeeThirdPartyAds) {
      this.data.putBool("third_party_ads", shouldSeeThirdPartyAds);
   }

   public void setThirdpartyVideoAds(boolean shouldSeeThirdPartyVideoAds) {
      this.data.putBool("third_party_video_ads", shouldSeeThirdPartyVideoAds);
   }

   public boolean hasMadePurchase() {
      return this.getInGamePurchaseCount() > 0;
   }

   public void updateLastClientVersion(String ver) {
      this.lastClientVersion_ = new VersionInfo(ver);
      this.loadFlipGame();
      this.isDirty = true;
   }

   public int getTotalSpeedups() {
      return Math.max(0, Math.min(this.totalSpeedups_, 32767));
   }

   public String getCountryCode() {
      return this.countryCode_ == null ? "" : this.countryCode_;
   }

   public VersionInfo lastClientVersion() {
      return this.lastClientVersion_;
   }

   public long LastLoginTime() {
      return this.lastLoginTime_;
   }

   public long getDateCreatedTime() {
      return this.dateCreatedTime_;
   }

   public void setLastCodeTime(long time) {
      this.lastCodeCheck_ = time;
   }

   public long getLastCodeTime() {
      return this.lastCodeCheck_;
   }

   public String getExtraAdParamString() {
      String qs = "";
      qs = qs + "iap_count=" + this.getInGamePurchaseCount();
      qs = qs + "&iap_value=" + this.getInGamePurchaseAmountTotal();
      qs = qs + "&level=" + this.getLevel();
      qs = qs + "&date_created=" + this.dateCreatedTime_;
      qs = qs + "&last_login=" + this.lastLoginTime_;
      qs = qs + "&client_version=" + this.lastClientVersion().toString();
      qs = qs + "&game=msm";
      qs = qs + "&coins=" + this.getDisplayedCoins();
      qs = qs + "&diamonds=" + this.getDisplayedDiamonds();
      qs = qs + "&food=" + this.getDisplayedFood();
      qs = qs + "&ethereal=" + this.getDisplayedEthCurrency();
      qs = qs + "&keys=" + this.getDisplayedKeys();
      qs = qs + "&relics=" + this.getDisplayedRelics();
      int count;
      Iterator var3;
      if (!this.groups_.isEmpty() || !this.newGroups_.isEmpty()) {
         qs = qs + "&groups=";
         count = 0;

         Integer i;
         for(var3 = this.groups_.iterator(); var3.hasNext(); qs = qs + (count++ > 0 ? "," : "") + i) {
            i = (Integer)var3.next();
         }

         for(var3 = this.newGroups_.iterator(); var3.hasNext(); qs = qs + (count++ > 0 ? "," : "") + i) {
            i = (Integer)var3.next();
         }
      }

      if (!this.getIslands().isEmpty()) {
         qs = qs + "&islands=";
         count = 0;

         PlayerIsland island;
         for(var3 = this.getIslands().iterator(); var3.hasNext(); qs = qs + (count++ > 0 ? "," : "") + island.getIndex()) {
            island = (PlayerIsland)var3.next();
         }
      }

      if (this.ownedIslandThemes.size() > 0) {
         qs = qs + "&themes=";
         count = 0;

         for(int i = 0; i < this.ownedIslandThemes.size(); ++i) {
            qs = qs + (count++ > 0 ? "," : "") + this.ownedIslandThemes.getInt(i);
         }
      }

      qs = qs + "&env=" + GameSettings.get("DEPLOYMENT_ENVIRONMENT", "dev");
      return qs;
   }

   public void setDailySpeedUpCredit() {
      try {
         this.speedUpMap.clear();
         String sql = "SELECT * FROM user_speed_up_credit WHERE bbb_id = ?;";
         Object[] args = new Object[]{this.getBbbId()};
         ISFSArray result = MSMExtension.getInstance().getDB().query(sql, args);
         int dailyVideoSpeedUpMax = GameSettings.getInt("DAILY_SPEED_UP_VIDEO_AMOUNT");
         String defaultStrType = GameSettings.get("DEFAULT_SPEED_UP_TYPE");
         Player.SPEED_UP_TYPES defaultType = this.getSpeedUpTypeFromString(defaultStrType);
         if (null != result && 0 != result.size()) {
            for(int i = 0; i < result.size(); ++i) {
               String strType = result.getSFSObject(i).getUtfString("speed_up_type");
               int speedUpCredit_ = 0;
               Player.SPEED_UP_STATUS status = Player.SPEED_UP_STATUS.STATIC;
               long lastClaimedOn = 0L;
               Player.SPEED_UP_TYPES type = this.getSpeedUpTypeFromString(strType);
               if (Player.SPEED_UP_TYPES.EMPTY == type) {
                  type = defaultType;
                  status = Player.SPEED_UP_STATUS.MODIFIED;
               }

               switch(type) {
               case VIDEO:
                  int userSpeedUpCredit = result.getSFSObject(0).getInt("speed_up_credit");
                  if (userSpeedUpCredit > dailyVideoSpeedUpMax) {
                     status = Player.SPEED_UP_STATUS.MODIFIED;
                     speedUpCredit_ = dailyVideoSpeedUpMax;
                  } else {
                     speedUpCredit_ = userSpeedUpCredit;
                  }

                  if (null != result.getSFSObject(0).getLong("last_claimed_on")) {
                     lastClaimedOn = result.getSFSObject(0).getLong("last_claimed_on");
                     if (this.refreshLastClaimedOn(lastClaimedOn)) {
                        status = Player.SPEED_UP_STATUS.MODIFIED;
                        speedUpCredit_ = dailyVideoSpeedUpMax;
                     }
                  } else {
                     Logger.trace(" NULL DATETIME OBJECT TYPE. Time will be : " + lastClaimedOn);
                  }
                  break;
               case EMPTY:
                  Logger.trace(" NULL DEFAULT SPEED UP TYPE FROM GAMESETTING. UPDATE SPEEDUP TYPE!");
               }

               Player.PlayerSpeedUpInfo info = new Player.PlayerSpeedUpInfo(speedUpCredit_, status, lastClaimedOn);
               this.speedUpMap.put(type, info);
            }
         } else {
            Player.PlayerSpeedUpInfo info = new Player.PlayerSpeedUpInfo(dailyVideoSpeedUpMax, Player.SPEED_UP_STATUS.NEW, 0L);
            this.speedUpMap.put(defaultType, info);
         }
      } catch (Exception var15) {
         Logger.trace(var15, "ERROR, cannot initialize speed up credit for user!");
      }

   }

   private Player.SPEED_UP_TYPES getSpeedUpTypeFromString(String type) {
      return (new String("video")).equals(type) ? Player.SPEED_UP_TYPES.VIDEO : Player.SPEED_UP_TYPES.EMPTY;
   }

   private boolean refreshLastClaimedOn(long lastClaimedOn) {
      long refreshDate = lastClaimedOn + GameSettings.getLong("SPEED_UP_REFRESH_PERIOD") * 3600L * 1000L;
      long currentDate = MSMExtension.CurrentDBTime();
      return currentDate > refreshDate;
   }

   public void updateSpeedUp() {
      Player.PlayerSpeedUpInfo info;
      try {
         for(Iterator var1 = this.speedUpMap.entrySet().iterator(); var1.hasNext(); info.status = Player.SPEED_UP_STATUS.STATIC) {
            Entry<Player.SPEED_UP_TYPES, Player.PlayerSpeedUpInfo> entry = (Entry)var1.next();
            info = (Player.PlayerSpeedUpInfo)entry.getValue();
            Player.SPEED_UP_TYPES type = (Player.SPEED_UP_TYPES)entry.getKey();
            String updateSql;
            switch(info.status) {
            case NEW:
               updateSql = "INSERT INTO user_speed_up_credit (bbb_id, speed_up_credit, last_claimed_on, speed_up_type) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE speed_up_credit=?, last_claimed_on=?";
               MSMExtension.getInstance().getDB().insertGetId(updateSql, new Object[]{this.getBbbId(), info.credit_amount, new Timestamp(info.last_claimed_on), type.toString(), info.credit_amount, new Timestamp(info.last_claimed_on)});
               break;
            case MODIFIED:
               updateSql = "UPDATE user_speed_up_credit SET speed_up_credit=?, last_claimed_on=? WHERE bbb_id=? and speed_up_type=?;";
               MSMExtension.getInstance().getDB().update(updateSql, new Object[]{info.credit_amount, new Timestamp(info.last_claimed_on), this.getBbbId(), type.toString()});
            case STATIC:
            }
         }
      } catch (Exception var6) {
         Logger.trace(var6, "ERROR, cannot save user's speed up credit!");
      }

   }

   public boolean memberOfGroup(int group) {
      return this.groups_.contains(group) || this.newGroups_.contains(group);
   }

   public void addToGroup(int group) {
      if (group != -1 && !this.memberOfGroup(group)) {
         this.newGroups_.add(group);
      }

   }

   public boolean hasUnsavedGroups() {
      return this.newGroups_.size() > 0;
   }

   public List<Integer> getUnsavedGroups() {
      return this.newGroups_;
   }

   public void markUnsavedGroups() {
      Iterator var1 = this.newGroups_.iterator();

      while(var1.hasNext()) {
         Integer i = (Integer)var1.next();
         this.groups_.add(i);
      }

      this.newGroups_.clear();
   }

   public List<Integer> getGroups() {
      return this.groups_;
   }

   public List<Integer> getAllGroups() {
      List<Integer> allGroups = new ArrayList();
      allGroups.addAll(this.getGroups());
      allGroups.addAll(this.getUnsavedGroups());
      return allGroups;
   }

   public void initGroups(ISFSArray groupData) throws Exception {
      this.groups_.clear();
      this.newGroups_.clear();
      Iterator i = groupData.iterator();

      while(i.hasNext()) {
         ISFSObject o = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
         this.groups_.add(o.getInt("group"));
      }

      this.cacheTutorialGroup();
   }

   public void cacheTutorialGroup() {
      this.tutorialGroup_ = null;
      ArrayList<TutorialGroup> tutorialGroups = UserGroup.getGroup(UserGroup.GroupType.Tutorial);
      Iterator var2 = tutorialGroups.iterator();

      while(var2.hasNext()) {
         TutorialGroup g = (TutorialGroup)var2.next();
         if (this.memberOfGroup(g.id())) {
            this.tutorialGroup_ = g;
            break;
         }
      }

      ArrayList<BattleHintLevelGroup> bhGroups = UserGroup.getGroup(UserGroup.GroupType.BattleHintLevel);
      Iterator var6 = bhGroups.iterator();

      while(var6.hasNext()) {
         BattleHintLevelGroup g = (BattleHintLevelGroup)var6.next();
         if (this.memberOfGroup(g.id())) {
            this.battleHintGroup_ = g;
            break;
         }
      }

   }

   public TutorialGroup getTutorialGroup() {
      return this.tutorialGroup_;
   }

   public BattleHintLevelGroup getBattleHintGroup() {
      return this.battleHintGroup_;
   }

   public String clientTutorialSetup() {
      String s = "";
      ArrayList<TutorialGroup> tutorialGroups = UserGroup.getGroup(UserGroup.GroupType.Tutorial);
      Iterator var3 = tutorialGroups.iterator();

      while(var3.hasNext()) {
         TutorialGroup t = (TutorialGroup)var3.next();
         if (this.memberOfGroup(t.id())) {
            s = t.clientTutorial();
            break;
         }
      }

      return s;
   }

   public boolean timedEventsUnlocked() {
      if (this.getTutorialGroup() != null && this.getTutorialGroup().hasUserGameSetting("TIMED_EVENT_UNLOCK_LEVEL_V2")) {
         return this.getLevel() >= GameSettings.getInt("TIMED_EVENT_UNLOCK_LEVEL_V2");
      } else {
         return this.getLevel() >= GameSettings.getInt("USER_TIMED_EVENT_UNLOCK_LEVEL_DEFAULT");
      }
   }

   public boolean memberOfGroupType(UserGroup.GroupType type) {
      ArrayList<UserGroup> groups = UserGroup.getGroup(type);
      Iterator var3 = groups.iterator();

      UserGroup g;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         g = (UserGroup)var3.next();
      } while(!this.memberOfGroup(g.id()));

      return true;
   }

   public boolean isBanned() {
      return this.banExpiry_ > MSMExtension.CurrentDBTime();
   }

   public boolean requestedDeletion() {
      return this.requestedDeletion_;
   }

   public String getPlatform() {
      return this.platform;
   }

   public void setPlatform(String newPlatform) {
      this.platform = newPlatform;
   }

   public String getSubplatform() {
      return this.subplatform;
   }

   public void setSubplatform(String newSubplatform) {
      this.subplatform = newSubplatform;
   }

   public void initMusic(ISFSArray trackData, ISFSArray songData) {
      this.tracks = trackData;

      for(int i = 0; i < songData.size(); ++i) {
         ISFSObject obj = songData.getSFSObject(i);
         ISFSArray trackArray = SFSArray.newFromJsonData(obj.getUtfString("tracks"));
         obj.removeElement("tracks");
         obj.putSFSArray("tracks", trackArray);
      }

      this.songs = songData;
   }

   public boolean deleteTrack(long id) {
      for(int i = 0; i < this.tracks.size(); ++i) {
         ISFSObject obj = this.tracks.getSFSObject(i);
         if (obj.getLong("user_track_id") == id && obj.getUtfString("name") != null) {
            this.tracks.removeElementAt(i);
            return true;
         }
      }

      return false;
   }

   public ISFSArray getTracks() {
      return this.tracks;
   }

   public ISFSArray getSongs() {
      return this.songs;
   }

   public SFSArray getMailbox() {
      return this.mailbox;
   }

   public boolean getNewMail() {
      return this.newMail;
   }

   public PlayerActiveIslandStats getActiveIslandStats() {
      return this.activeIslandStats;
   }

   public BattlePlayerState getBattleState() {
      return this.battleState_;
   }

   void loadBattleState() {
      try {
         String SELECT_SQL = "SELECT * FROM user_battle WHERE user_id = ?";
         ISFSArray result = MSMExtension.getInstance().getDB().query("SELECT * FROM user_battle WHERE user_id = ?", new Object[]{this.getPlayerId()});
         if (result.size() > 0) {
            this.battleState_ = new BattlePlayerState(result.getSFSObject(0));
         } else {
            this.battleState_ = new BattlePlayerState();
            String INSERT_SQL = "INSERT INTO user_battle SET user_id = ?, xp = ?, level = ?,  loadout = ?, loadout_versus = ?, medals = ?, max_training_level = ?";
            MSMExtension.getInstance().getDB().insertGetId("INSERT INTO user_battle SET user_id = ?, xp = ?, level = ?,  loadout = ?, loadout_versus = ?, medals = ?, max_training_level = ?", new Object[]{this.getPlayerId(), this.battleState_.getXp(), this.battleState_.getLevel(), this.battleState_.getLoadout().toSFSObject().toJson(), this.battleState_.getLoadoutVersus().toSFSObject().toJson(), this.battleState_.getMedals(), this.battleState_.getMaxTrainingLevel()});
         }
      } catch (Exception var4) {
         Logger.trace(var4);
      }

   }

   void loadViewedBattles() {
      try {
         String SELECT_SQL = "SELECT * FROM user_battle_campaigns_viewed WHERE user_id = ?";
         ISFSArray result = MSMExtension.getInstance().getDB().query("SELECT * FROM user_battle_campaigns_viewed WHERE user_id = ?", new Object[]{this.getPlayerId()});
         if (result.size() > 0) {
            JSONArray ja = new JSONArray(result.getSFSObject(0).getUtfString("perma_campaigns_viewed"));
            if (ja.length() > 0) {
               this.viewedPermaCampaigns_ = new ArrayList();
            }

            for(int i = 0; i < ja.length(); ++i) {
               if (!ja.isNull(i)) {
                  this.viewedPermaCampaigns_.add(ja.getInt(i));
               }
            }
         }
      } catch (Exception var5) {
         Logger.trace(var5);
      }

   }

   public void saveBattleState() {
      try {
         String UPDATE_SQL = "UPDATE user_battle SET xp = ?, level = ?, loadout = ?,  loadout_versus = ?, medals = ?, max_training_level = ?  WHERE user_id = ?";
         MSMExtension.getInstance().getDB().update("UPDATE user_battle SET xp = ?, level = ?, loadout = ?,  loadout_versus = ?, medals = ?, max_training_level = ?  WHERE user_id = ?", new Object[]{this.battleState_.getXp(), this.battleState_.getLevel(), this.battleState_.getLoadout().toSFSObject().toJson(), this.battleState_.getLoadoutVersus().toSFSObject().toJson(), this.battleState_.getMedals(), this.battleState_.getMaxTrainingLevel(), this.getPlayerId()});
      } catch (Exception var2) {
         Logger.trace(var2);
      }

      this.saveViewedBattles();
   }

   public void saveViewedBattles() {
      if (this.viewedPermaCampaigns_ != null) {
         try {
            String UPDATE_SQL = "INSERT INTO user_battle_campaigns_viewed  (user_id, perma_campaigns_viewed)  VALUES (?,?)  ON DUPLICATE KEY UPDATE  perma_campaigns_viewed = VALUES(perma_campaigns_viewed) ";
            String perma = (new JSONArray(this.viewedPermaCampaigns_)).toString();
            MSMExtension.getInstance().getDB().update("INSERT INTO user_battle_campaigns_viewed  (user_id, perma_campaigns_viewed)  VALUES (?,?)  ON DUPLICATE KEY UPDATE  perma_campaigns_viewed = VALUES(perma_campaigns_viewed) ", new Object[]{this.getPlayerId(), perma});
         } catch (Exception var3) {
            Logger.trace(var3);
         }
      }

   }

   public BattlePlayerVersusState getLatestBattleVersusState(int campaignId) {
      Map<Long, BattlePlayerVersusState> states = this.battleVersusStates_.get(campaignId);
      if (states == null) {
         return null;
      } else {
         long ts = 0L;
         BattlePlayerVersusState latest = null;
         Iterator var6 = states.entrySet().iterator();

         while(var6.hasNext()) {
            Entry<Long, BattlePlayerVersusState> entry = (Entry)var6.next();
            long k = (Long)entry.getKey();
            if (k > ts) {
               ts = k;
               latest = (BattlePlayerVersusState)entry.getValue();
            }
         }

         return latest;
      }
   }

   public BattlePlayerVersusState getBattleVersusState(int campaignId, long scheduleStartTime) {
      return this.battleVersusStates_.get(campaignId, scheduleStartTime);
   }

   public BattlePlayerVersusState createBattleVersusState(int campaignId, long scheduleStartTime) {
      BattlePlayerVersusState newState = new BattlePlayerVersusState(campaignId, scheduleStartTime);
      this.battleVersusStates_.put(campaignId, scheduleStartTime, newState);
      return newState;
   }

   public BattlePlayerVersusState createBattleVersusState(ISFSObject data) {
      BattlePlayerVersusState newState = new BattlePlayerVersusState(data);
      int campaignId = data.getInt("campaign_id");
      long scheduleStartTime = data.getLong("schedule_started_on");
      if (MSMExtension.CurrentDBTime() >= newState.getRefreshTimestamp()) {
         newState.setAttempts(5);
         long newDayTimestamp = BattlePlayerVersusState.GetNewDayTimestamp();
         newState.setRefreshTimestamp(newDayTimestamp);
      }

      this.battleVersusStates_.put(campaignId, scheduleStartTime, newState);
      return newState;
   }

   void loadBattleVersusStates() {
      try {
         this.battleVersusStates_ = new Player.VersusStateMap();
         String sql = "SELECT * FROM user_battle_pvp WHERE user_id = ? AND claimed_on IS NULL";
         ISFSArray results = MSMExtension.getInstance().getDB().query(sql, new Object[]{this.getPlayerId()});

         for(int i = 0; i < results.size(); ++i) {
            this.createBattleVersusState(results.getSFSObject(i));
         }
      } catch (Exception var4) {
         Logger.trace(var4);
      }

   }

   public void saveBattleVersusStates() {
      try {
         IDbWrapper db = MSMExtension.getInstance().getDB();
         String SQL_EXISTS = "SELECT campaign_id, user_id FROM user_battle_pvp WHERE campaign_id=? AND schedule_started_on=? AND user_id=?";
         String SQL_UPDATE = "UPDATE user_battle_pvp SET `display_name`=?, `tier`=?, `stars`=?, `streak`=?, `attempts`=?, `refreshes_on`=?, `loadout`=?, `match_score`=?, `started_on`=?, `completed_on`=?, `claimed_on`=?, `last_opponent_loadout`=?, `last_seed`=?, `champion_score`=?  WHERE campaign_id=? AND schedule_started_on=? AND user_id=?";
         String SQL_INSERT = "INSERT INTO user_battle_pvp (`campaign_id`, `schedule_started_on`, `user_id`, `display_name`, `tier`, `stars`, `streak`, `attempts`, `refreshes_on`, `loadout`, `match_score`, `last_opponent_loadout`, `last_seed`, `champion_score`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
         Iterator var5 = this.battleVersusStates_.states().iterator();

         while(var5.hasNext()) {
            BattlePlayerVersusState state = (BattlePlayerVersusState)var5.next();
            int campaignId = state.getCampaignId();
            long scheduleStart = state.getScheduleTimestamp();
            if (state.isDirty()) {
               if (db.exists("SELECT campaign_id, user_id FROM user_battle_pvp WHERE campaign_id=? AND schedule_started_on=? AND user_id=?", new Object[]{campaignId, new Timestamp(scheduleStart), this.getPlayerId()})) {
                  db.update("UPDATE user_battle_pvp SET `display_name`=?, `tier`=?, `stars`=?, `streak`=?, `attempts`=?, `refreshes_on`=?, `loadout`=?, `match_score`=?, `started_on`=?, `completed_on`=?, `claimed_on`=?, `last_opponent_loadout`=?, `last_seed`=?, `champion_score`=?  WHERE campaign_id=? AND schedule_started_on=? AND user_id=?", new Object[]{this.getDisplayName(), state.getTier(), state.getStars(), state.getStreak(), state.getAttempts(), state.getRefreshTimestamp() > 0L ? new Timestamp(state.getRefreshTimestamp()) : null, state.getLoadout().toSFSArray().toJson(), state.getMatchScore(), new Timestamp(state.getStartedTimestamp()), state.getCompletedTimestamp() > 0L ? new Timestamp(state.getCompletedTimestamp()) : null, state.getClaimedTimestamp() > 0L ? new Timestamp(state.getClaimedTimestamp()) : null, state.getLastOpponentLoadout().toSFSArray().toJson(), state.getLastSeed(), state.getChampionScore(), campaignId, new Timestamp(scheduleStart), this.getPlayerId()});
               } else {
                  db.insertGetId("INSERT INTO user_battle_pvp (`campaign_id`, `schedule_started_on`, `user_id`, `display_name`, `tier`, `stars`, `streak`, `attempts`, `refreshes_on`, `loadout`, `match_score`, `last_opponent_loadout`, `last_seed`, `champion_score`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new Object[]{campaignId, new Timestamp(scheduleStart), this.getPlayerId(), this.getDisplayName(), state.getTier(), state.getStars(), state.getStreak(), state.getAttempts(), new Timestamp(state.getRefreshTimestamp()), state.getLoadout().toSFSArray().toJson(), state.getMatchScore(), state.getLastOpponentLoadout().toSFSArray().toJson(), state.getLastSeed(), state.getChampionScore()});
               }
            }
         }
      } catch (Exception var10) {
         Logger.trace(var10);
      }

   }

   void loadAvatar() {
      try {
         IDbWrapper db = MSMExtension.getInstance().getDB();
         String SELECT_SQL = "SELECT * FROM user_avatar WHERE user_id = ?";
         ISFSArray results = db.query("SELECT * FROM user_avatar WHERE user_id = ?", new Object[]{this.getPlayerId()});
         if (results.size() > 0) {
            this.avatar_ = new PlayerAvatar(results.getSFSObject(0));
         } else {
            this.avatar_ = new PlayerAvatar();
            String INSERT_SQL = "INSERT INTO user_avatar SET user_id = ?, pp_type=?, pp_info = ?";
            db.insertGetId("INSERT INTO user_avatar SET user_id = ?, pp_type=?, pp_info = ?", new Object[]{this.getPlayerId(), this.avatar_.getType(), this.avatar_.getInfo()});
         }
      } catch (Exception var5) {
         Logger.trace(var5);
      }

   }

   public void saveAvatar() {
      if (this.avatar_ != null && this.avatar_.isDirty()) {
         try {
            String UPDATE_SQL = "UPDATE user_avatar SET pp_type = ?, pp_info = ?  WHERE user_id = ?";
            MSMExtension.getInstance().getDB().update("UPDATE user_avatar SET pp_type = ?, pp_info = ?  WHERE user_id = ?", new Object[]{this.avatar_.getType(), this.avatar_.getInfo(), this.getPlayerId()});
         } catch (Exception var2) {
            Logger.trace(var2);
         }

      }
   }

   void loadCostumes() {
      try {
         IDbWrapper db = MSMExtension.getInstance().getDB();
         String SELECT_SQL = "SELECT * FROM user_costumes WHERE user_id = ?";
         ISFSArray results = db.query("SELECT * FROM user_costumes WHERE user_id = ?", new Object[]{this.getPlayerId()});
         String costumesJson;
         if (results.size() > 0) {
            costumesJson = results.getSFSObject(0).getUtfString("costumes");
            this.costumes_ = new PlayerCostumeState(SFSObject.newFromJsonData(costumesJson));
         } else {
            this.costumes_ = new PlayerCostumeState();
            costumesJson = "INSERT INTO user_costumes SET user_id = ?, costumes = '{}'";
            db.insertGetId("INSERT INTO user_costumes SET user_id = ?, costumes = '{}'", new Object[]{this.getPlayerId()});
         }
      } catch (Exception var5) {
         Logger.trace(var5);
      }

   }

   public void saveCostumes() {
      if (this.costumes_ != null && this.costumes_.isDirty()) {
         try {
            String UPDATE_SQL = "UPDATE user_costumes SET costumes = ?  WHERE user_id = ?";
            MSMExtension.getInstance().getDB().update("UPDATE user_costumes SET costumes = ?  WHERE user_id = ?", new Object[]{this.costumes_.toSFSObject().toJson(), this.getPlayerId()});
         } catch (Exception var2) {
            Logger.trace(var2);
         }

      }
   }

   public void loadInventory() {
      try {
         IDbWrapper db = MSMExtension.getInstance().getDB();
         String SELECT_SQL = "SELECT * FROM user_inventory WHERE user_id = ?";
         ISFSArray results = db.query("SELECT * FROM user_inventory WHERE user_id = ?", new Object[]{this.getPlayerId()});
         String inventoryJson;
         if (results.size() > 0) {
            inventoryJson = results.getSFSObject(0).getUtfString("inventory");
            this.inventory_ = new PlayerInventory(SFSObject.newFromJsonData(inventoryJson));
         } else {
            this.inventory_ = new PlayerInventory();
            inventoryJson = "INSERT INTO user_inventory SET user_id = ?, inventory = '{}'";
            db.insertGetId("INSERT INTO user_inventory SET user_id = ?, inventory = '{}'", new Object[]{this.getPlayerId()});
         }
      } catch (Exception var5) {
         Logger.trace(var5);
      }

   }

   public void saveInventory() {
      if (this.inventory_ != null && this.inventory_.isDirty()) {
         try {
            String UPDATE_SQL = "UPDATE user_inventory SET inventory = ?  WHERE user_id = ?";
            MSMExtension.getInstance().getDB().update("UPDATE user_inventory SET inventory = ?  WHERE user_id = ?", new Object[]{this.inventory_.toSFSObject().toJson(), this.getPlayerId()});
            this.inventory_.setDirty(false);
         } catch (Exception var2) {
            Logger.trace(var2);
         }

      }
   }

   private void loadFlipGame() {
      if (this.lastClientVersion_.compareTo(GameSettings.get("MEMORY_FLIP_UPDATE")) >= 0) {
         IDbWrapper db = MSMExtension.getInstance().getDB();
         this.flipGame_ = new PlayerFlipGame(db, this.getPlayerId());
      }

   }

   public void saveFlipGame() {
      if (this.flipGame_ != null && this.flipGame_.isDirty()) {
         this.flipGame_.save(this.getPlayerId());
      }
   }

   private String getReportKey(long userId, String reason, long islandId) {
      return String.format("%d-%s-%d", userId, reason, islandId);
   }

   public boolean canReportUser(long userId, String reason, long islandId) {
      long now = MSMExtension.CurrentDBTime();
      if (now < this.lastReportTime_ + GameSettings.getLong("REPORT_WAIT_TIME") * 1000L) {
         return false;
      } else {
         String key = this.getReportKey(userId, reason, islandId);
         return !this.reportedUsers_.contains(key);
      }
   }

   public void reportUser(long userId, String reason, long islandId) {
      this.reportedUsers_.add(this.getReportKey(userId, reason, islandId));
      this.lastReportTime_ = MSMExtension.CurrentDBTime();
   }

   public int lastFlipLevelPlayed() {
      return this.flipGame_ == null ? 1 : this.flipGame_.lastLevelPlayed();
   }

   public boolean hasFreeFlipPlay() {
      Long lastFreePlay = this.lastFlipgameFreePlay();
      if (lastFreePlay == null) {
         return true;
      } else if (this.lastFlipLevelPlayed() == 1) {
         return true;
      } else {
         return lastFreePlay / 1000L + (long)(3600 * GameSettings.getInt("MEMORY_TIME_BETWEEN_FREE_PLAYS")) <= MSMExtension.CurrentDBTime() / 1000L;
      }
   }

   public Long lastFlipgameFreePlay() {
      return this.flipGame_ == null ? null : this.flipGame_.lastFreePlay();
   }

   public void startNewFlipGame() {
      if (this.flipGame_ != null) {
         this.flipGame_.startNewGame();
         this.saveFlipGame();
      }
   }

   public void resetLastFlipgameFreePlay() {
      if (this.flipGame_ != null) {
         this.flipGame_.resetLastFreePlay();
      }
   }

   public boolean timeForFlipgamePrizeReset() {
      return this.flipGame_ == null ? false : this.flipGame_.timeForFlipgamePrizeReset();
   }

   public void doFlipgamePrizeReset() {
      if (this.flipGame_ != null) {
         this.flipGame_.resetLastPrizeReset();
      }
   }

   public int flipDiamondLevel() {
      return this.flipGame_ == null ? -1 : this.flipGame_.diamondLevel();
   }

   public ISFSArray getRemainingScaledTopEmbeddedPrizes() {
      return this.flipGame_ == null ? null : this.flipGame_.getRemainingScaledTopEmbeddedPrizes(this.getLevel());
   }

   public ISFSObject selectFlipgameEmbeddedLevelPrize(int flipLevelId) {
      return this.flipGame_ == null ? null : this.flipGame_.selectEmbeddedLevelPrize(flipLevelId, this.getLevel());
   }

   public boolean collectFlipgameSelectPrize(User sender, GameStateHandler handler) {
      if (this.flipGame_ == null) {
         return false;
      } else {
         ISFSObject scaledFlipEmbeddedPrizeSelected = this.flipGame_.scaledEmbeddedPrizeSelected();
         if (scaledFlipEmbeddedPrizeSelected != null) {
            int amt = scaledFlipEmbeddedPrizeSelected.getInt("amt");
            String type = scaledFlipEmbeddedPrizeSelected.getUtfString("type");
            Player.CurrencyType cType = getCurrencyTypeFromString(type);
            switch(cType) {
            case Diamonds:
               this.adjustDiamonds(sender, handler, amt);
               MSMExtension.getInstance().stats.trackFlipMinigameReward(sender, cType.getCurrencyKey(), amt);
               MSMExtension.getInstance().stats.trackReward(sender, "flip_mini_game", "diamonds", (long)amt);
               break;
            case Coins:
               this.adjustCoins(sender, handler, amt);
               MSMExtension.getInstance().stats.trackFlipMinigameReward(sender, cType.getCurrencyKey(), amt);
               break;
            case Ethereal:
               this.adjustEthCurrency(sender, handler, amt);
               MSMExtension.getInstance().stats.trackFlipMinigameReward(sender, cType.getCurrencyKey(), amt);
               break;
            case Starpower:
               this.adjustStarpower(sender, handler, (long)amt);
               MSMExtension.getInstance().stats.trackFlipMinigameReward(sender, cType.getCurrencyKey(), amt);
               break;
            case Food:
               this.adjustFood(sender, handler, amt);
               MSMExtension.getInstance().stats.trackFlipMinigameReward(sender, cType.getCurrencyKey(), amt);
               break;
            case Keys:
               this.adjustKeys(sender, handler, amt);
               MSMExtension.getInstance().stats.trackFlipMinigameReward(sender, cType.getCurrencyKey(), amt);
               break;
            case Relics:
               this.adjustRelics(sender, handler, amt);
               MSMExtension.getInstance().stats.trackFlipMinigameReward(sender, cType.getCurrencyKey(), amt);
               break;
            case Xp:
               this.rewardXp(sender, handler, amt);
               break;
            default:
               Logger.trace("collectFlipGamePrize: UNSUPPORTED MEMORY FLIP REWARD TYPE: " + type);
               return false;
            }

            this.flipGame_.collectLevel();
            return true;
         } else {
            return false;
         }
      }
   }

   public int endgamePrizeLevelReached() {
      return this.flipGame_ == null ? 0 : this.flipGame_.endgamePrizeLevelReached();
   }

   public int endFlipLevel() {
      return this.flipGame_ == null ? 0 : this.flipGame_.endLevel();
   }

   public void endFlipgame() {
      if (this.flipGame_ != null) {
         this.flipGame_.endGame();
         this.saveFlipGame();
      }
   }

   void loadTimedEvents() {
      try {
         this.timedEvents_ = new PlayerTimedEvents(this.getPlayerId());
         this.timedEvents_.load(MSMExtension.getInstance().getDB());
      } catch (PlayerLoadingException var2) {
         Logger.trace((Exception)var2, "**** error loading player timed events ****");
      }

   }

   public void loadDailyCumulativeLogin() {
      try {
         IDbWrapper db = MSMExtension.getInstance().getDB();
         this.dailyCumulativeLogin_ = PlayerDailyCumulativeLogin.load(db, this.getPlayerId());
      } catch (Exception var2) {
         MSMExtension.getInstance().trace(new Object[]{Misc.getStackTraceAsString(var2)});
      }

   }

   class VersusStateMap {
      Map<Integer, Map<Long, BattlePlayerVersusState>> map_ = new HashMap();

      boolean isEmpty() {
         return this.map_.isEmpty();
      }

      public boolean containsKey(int campaignId, long scheduleStartTime) {
         return this.map_.containsKey(campaignId) ? ((Map)this.map_.get(campaignId)).containsKey(scheduleStartTime) : false;
      }

      public Map<Long, BattlePlayerVersusState> get(int campaignId) {
         return (Map)this.map_.get(campaignId);
      }

      public BattlePlayerVersusState get(int campaignId, long scheduleStartTime) {
         Map<Long, BattlePlayerVersusState> innerMap = (Map)this.map_.get(campaignId);
         return innerMap != null ? (BattlePlayerVersusState)innerMap.get(scheduleStartTime) : null;
      }

      public void put(int campaignId, long scheduleStartTime, BattlePlayerVersusState state) {
         Map<Long, BattlePlayerVersusState> innerMap = (Map)this.map_.get(campaignId);
         if (innerMap == null) {
            innerMap = new HashMap();
            this.map_.put(campaignId, innerMap);
         }

         ((Map)innerMap).put(scheduleStartTime, state);
      }

      public void clear() {
         this.map_.clear();
      }

      public Iterable<BattlePlayerVersusState> states() {
         return new Iterable<BattlePlayerVersusState>() {
            public Iterator<BattlePlayerVersusState> iterator() {
               return new Iterator<BattlePlayerVersusState>() {
                  Iterator<Map<Long, BattlePlayerVersusState>> outer;
                  Iterator<BattlePlayerVersusState> inner;

                  {
                     this.outer = VersusStateMap.this.map_.values().iterator();
                     this.inner = this.outer.hasNext() ? ((Map)this.outer.next()).values().iterator() : null;
                  }

                  public boolean hasNext() {
                     return this.inner != null && this.inner.hasNext();
                  }

                  public BattlePlayerVersusState next() {
                     if (!this.hasNext()) {
                        throw new NoSuchElementException();
                     } else {
                        BattlePlayerVersusState next = (BattlePlayerVersusState)this.inner.next();
                        if (!this.inner.hasNext()) {
                           this.inner = this.outer.hasNext() ? ((Map)this.outer.next()).values().iterator() : null;
                        }

                        return next;
                     }
                  }

                  public void remove() {
                     throw new UnsupportedOperationException("remove");
                  }
               };
            }
         };
      }
   }

   public class PlayerSpeedUpInfo {
      public int credit_amount = 0;
      public Player.SPEED_UP_STATUS status;
      public long last_claimed_on;

      public PlayerSpeedUpInfo(int amount, Player.SPEED_UP_STATUS status_, long date) {
         this.status = Player.SPEED_UP_STATUS.STATIC;
         this.last_claimed_on = 0L;
         this.credit_amount = amount;
         this.status = status_;
         this.last_claimed_on = date;
      }
   }

   class GlobalData {
      long lastCollectAll = 0L;
      int monikerId = 0;
      ISFSArray lastTimedTheme = null;
      Collection<Integer> activeIslandTuts = null;

      public ISFSObject toSFSObject() {
         ISFSObject data = new SFSObject();
         if (this.lastCollectAll != 0L) {
            data.putLong("last_collect_all", this.lastCollectAll);
         }

         if (this.monikerId != 0) {
            data.putLong("moniker_id", (long)this.monikerId);
         }

         if (this.lastTimedTheme != null) {
            data.putSFSArray("last_timed_theme", this.lastTimedTheme);
         }

         if (this.activeIslandTuts != null && this.activeIslandTuts.size() > 0) {
            data.putIntArray("island_tutorials", this.activeIslandTuts);
         }

         return data;
      }
   }

   public static enum SPEED_UP_TYPES {
      EMPTY,
      VIDEO;
   }

   public static enum SPEED_UP_STATUS {
      STATIC,
      NEW,
      MODIFIED;
   }

   public static enum PROPERTY {
      COINS,
      DIAMONDS,
      ETH_CURRENCY,
      STARPOWER,
      KEYS,
      FOOD,
      RELICS,
      XP,
      LEVEL,
      SPEED_UP,
      DAILY_RELICS_COUNT,
      RELIC_DIAMOND_COST,
      NEXT_RELIC_RESET,
      BATTLE_XP,
      BATTLE_LEVEL,
      MEDALS,
      EGG_WILDCARDS;
   }

   public static enum CurrencyType {
      Diamonds {
         public String getEntityCostKey() {
            return "cost_diamonds";
         }

         public EntitySalesEvent.DiscountType SpecificCurrencyDiscountType() {
            return EntitySalesEvent.DiscountType.NewCostDiamonds;
         }

         public String getCurrencyKey() {
            return "diamonds";
         }
      },
      Coins {
         public String getEntityCostKey() {
            return "cost_coins";
         }

         public EntitySalesEvent.DiscountType SpecificCurrencyDiscountType() {
            return EntitySalesEvent.DiscountType.NewCostCoins;
         }

         public String getCurrencyKey() {
            return "coins";
         }
      },
      Ethereal {
         public String getEntityCostKey() {
            return "cost_eth_currency";
         }

         public EntitySalesEvent.DiscountType SpecificCurrencyDiscountType() {
            return EntitySalesEvent.DiscountType.NewCostEthereal;
         }

         public String getCurrencyKey() {
            return "ethereal_currency";
         }
      },
      Starpower {
         public String getEntityCostKey() {
            return "cost_starpower";
         }

         public EntitySalesEvent.DiscountType SpecificCurrencyDiscountType() {
            return EntitySalesEvent.DiscountType.None;
         }

         public String getCurrencyKey() {
            return "starpower";
         }
      },
      Food {
         public String getEntityCostKey() {
            return "";
         }

         public EntitySalesEvent.DiscountType SpecificCurrencyDiscountType() {
            return EntitySalesEvent.DiscountType.None;
         }

         public String getCurrencyKey() {
            return "food";
         }
      },
      Keys {
         public String getEntityCostKey() {
            return "cost_keys";
         }

         public EntitySalesEvent.DiscountType SpecificCurrencyDiscountType() {
            return EntitySalesEvent.DiscountType.NewCostKey;
         }

         public String getCurrencyKey() {
            return "keys";
         }
      },
      Relics {
         public String getEntityCostKey() {
            return "cost_relics";
         }

         public EntitySalesEvent.DiscountType SpecificCurrencyDiscountType() {
            return EntitySalesEvent.DiscountType.NewCostRelic;
         }

         public String getCurrencyKey() {
            return "relics";
         }
      },
      Xp {
         public String getEntityCostKey() {
            return "";
         }

         public EntitySalesEvent.DiscountType SpecificCurrencyDiscountType() {
            return EntitySalesEvent.DiscountType.None;
         }

         public String getCurrencyKey() {
            return "xp";
         }
      },
      Medals {
         public String getEntityCostKey() {
            return "cost_medals";
         }

         public EntitySalesEvent.DiscountType SpecificCurrencyDiscountType() {
            return EntitySalesEvent.DiscountType.NewCostMedal;
         }

         public String getCurrencyKey() {
            return "medals";
         }
      },
      EggWildcards {
         public String getEntityCostKey() {
            return "";
         }

         public EntitySalesEvent.DiscountType SpecificCurrencyDiscountType() {
            return EntitySalesEvent.DiscountType.None;
         }

         public String getCurrencyKey() {
            return "egg_wildcards";
         }
      },
      Deferred {
         public String getEntityCostKey() {
            return "";
         }

         public EntitySalesEvent.DiscountType SpecificCurrencyDiscountType() {
            return EntitySalesEvent.DiscountType.None;
         }

         public String getCurrencyKey() {
            return "deferred";
         }
      },
      Undefined {
         public String getEntityCostKey() {
            return "";
         }

         public EntitySalesEvent.DiscountType SpecificCurrencyDiscountType() {
            return EntitySalesEvent.DiscountType.None;
         }

         public String getCurrencyKey() {
            return "none";
         }
      };

      private CurrencyType() {
      }

      public abstract String getEntityCostKey();

      public abstract EntitySalesEvent.DiscountType SpecificCurrencyDiscountType();

      public abstract String getCurrencyKey();

      // $FF: synthetic method
      CurrencyType(Object x2) {
         this();
      }
   }
}
