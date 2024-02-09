package com.bigbluebubble.mysingingmonsters;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import com.amazonaws.util.EC2MetadataUtils;
import com.bigbluebubble.BBBServer.BBBServerExtension;
import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.exceptions.UserNotFoundException;
import com.bigbluebubble.BBBServer.util.BBBToken;
import com.bigbluebubble.BBBServer.util.CacheWrapper;
import com.bigbluebubble.BBBServer.util.CloudwatchMetricAdder;
import com.bigbluebubble.BBBServer.util.CloudwatchMetrics;
import com.bigbluebubble.BBBServer.util.DbWrapper;
import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.BBBServer.util.Misc;
import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.BBBServer.util.SimpleLogger;
import com.bigbluebubble.BBBServer.util.SnsWrapper;
import com.bigbluebubble.BBBServer.util.SqsWrapper;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.battle.BattleIslandState;
import com.bigbluebubble.mysingingmonsters.battle.BattleSeasons;
import com.bigbluebubble.mysingingmonsters.battle.BattleVersusChampionRanks;
import com.bigbluebubble.mysingingmonsters.costumes.IslandCostumeState;
import com.bigbluebubble.mysingingmonsters.data.AccessKeys;
import com.bigbluebubble.mysingingmonsters.data.CodeLookup;
import com.bigbluebubble.mysingingmonsters.data.DailyCurrencyPack;
import com.bigbluebubble.mysingingmonsters.data.DailyCurrencyPackLookup;
import com.bigbluebubble.mysingingmonsters.data.IslandTheme;
import com.bigbluebubble.mysingingmonsters.data.IslandThemeLookup;
import com.bigbluebubble.mysingingmonsters.data.MailLookup;
import com.bigbluebubble.mysingingmonsters.data.ScratchTicketFunctions;
import com.bigbluebubble.mysingingmonsters.data.SynthesizerSettings;
import com.bigbluebubble.mysingingmonsters.data.TribalQuestLookup;
import com.bigbluebubble.mysingingmonsters.data.VersionData;
import com.bigbluebubble.mysingingmonsters.data.groups.AdvertisementGroup;
import com.bigbluebubble.mysingingmonsters.data.groups.BattleHintLevelGroup;
import com.bigbluebubble.mysingingmonsters.data.groups.TutorialGroup;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventManager;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.PromoEvent;
import com.bigbluebubble.mysingingmonsters.exceptions.InvalidPlayerException;
import com.bigbluebubble.mysingingmonsters.logging.Firebase;
import com.bigbluebubble.mysingingmonsters.logging.MSMCommandLogging;
import com.bigbluebubble.mysingingmonsters.player.InitialPlayerIslandData;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.bigbluebubble.mysingingmonsters.player.PlayerCrucibleData;
import com.bigbluebubble.mysingingmonsters.player.PlayerDailyCurrencyPack;
import com.bigbluebubble.mysingingmonsters.player.PlayerIsland;
import com.bigbluebubble.mysingingmonsters.player.PlayerIslandFactory;
import com.bigbluebubble.mysingingmonsters.player.PlayerLoadingException;
import com.bigbluebubble.mysingingmonsters.player.PlayerMonster;
import com.bigbluebubble.mysingingmonsters.player.PlayerQuest;
import com.bigbluebubble.mysingingmonsters.player.PlayerTimedEvents;
import com.bigbluebubble.mysingingmonsters.task.AuthTokenUpdateTask;
import com.bigbluebubble.mysingingmonsters.task.CheckUserMailTask;
import com.bigbluebubble.mysingingmonsters.task.MSMAdminCatchUnreleasedAccount;
import com.bigbluebubble.mysingingmonsters.task.MSMServerStatusTask;
import com.bigbluebubble.mysingingmonsters.task.MSMStats;
import com.bigbluebubble.mysingingmonsters.task.MailUpdateTask;
import com.bigbluebubble.mysingingmonsters.task.UpdateActiveTimedEvents;
import com.bigbluebubble.mysingingmonsters.util.ip.Geo;
import com.smartfoxserver.bitswarm.service.IService;
import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.Zone;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataType;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseSFSExtension;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.MySQLCodec;
import org.owasp.esapi.codecs.MySQLCodec.Mode;

public class MSMExtension extends BBBServerExtension {
   private static MSMExtension instance;
   static final int LAG_SIMULATION_MILLIS = 0;
   static volatile boolean isReloadingStaticData = false;
   public MSMStats stats;
   protected long topStagesRefresh;
   protected SFSArray topStages;
   protected String previousTopStageWinners;
   protected long topComposerStagesRefresh;
   protected SFSArray topComposerStages;
   protected String previousTopComposerStageWinners;
   protected long topTribesRefresh;
   protected SFSArray topTribes;
   private static long dbTimeOffset = 0L;
   public static long cache_loaded_on = 0L;
   public List<Integer> dailyLoginLvlCutoff;
   public List<String> dailyLoginStages;
   public int dailyLoginScaleStartStage = -1;
   public List<List<Player.CurrencyType>> dailyLoginTypes;
   public List<List<Integer>> dailyLoginAmounts;
   public int collectAllWaitMinutesPaying = 0;
   public int collectAllWaitMinutesNonPaying = 0;
   public List<List<Integer>> dailyLoginBonusEntities;
   public List<Integer> etherealIslandsWithModifiers;
   public JSONArray relicDiamondCosts;
   private static final String AWS_ACCESS_KEY = "AKIAILEBZY6UGI2N4XTQ";
   private static final String AWS_SECRET_KEY = "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";
   private boolean devServer = false;
   public Codec<?> sqlCodec = null;
   public BBBToken serverToken = null;
   public static final String DEFAULT_API_KEY = "Y*7vzp^0Xs#Jf812tmtTeLa%$LDQ4yZP";
   private IDbWrapper staticDataDB;

   public static MSMExtension getInstance() {
      return instance;
   }

   public boolean isDevServer() {
      return this.devServer;
   }

   public IDbWrapper getStaticDataDB() {
      return this.staticDataDB;
   }

   public static long CurrentDBTime() {
      return System.currentTimeMillis() - dbTimeOffset;
   }

   public static Calendar previousDailyTime(int hour, int minute) {
      Calendar prevTime = new GregorianCalendar();
      prevTime.set(11, hour);
      prevTime.set(12, minute);
      prevTime.set(13, 0);
      prevTime.set(14, 0);
      if (CurrentDBTime() < prevTime.getTimeInMillis()) {
         prevTime.add(5, -1);
      }

      return prevTime;
   }

   public static Calendar nextDailyTime(int hour, int minute) {
      Calendar nextReset = new GregorianCalendar();
      nextReset.set(11, hour);
      nextReset.set(12, minute);
      nextReset.set(13, 0);
      nextReset.set(14, 0);
      if (CurrentDBTime() >= nextReset.getTimeInMillis()) {
         nextReset.add(5, 1);
      }

      return nextReset;
   }

   public boolean scratchOffPrizeSupportedByServerVer(ISFSObject scratchPrize, VersionInfo serverVersion) {
      return serverVersion.compareTo(scratchPrize.getUtfString("min_server_version")) >= 0;
   }

   protected boolean getIsShimmed() {
      try {
         IService apiManager = SmartFoxServer.getInstance().getAPIManager();
         SimpleLogger.trace(new Object[]{"Extension is running in SFS Mode " + apiManager});
         return false;
      } catch (Exception var2) {
         SimpleLogger.trace(new Object[]{"Extension is running in Tomcat Mode"});
         return true;
      }
   }

   public MSMExtension() {
      super(MSMServerEventHandler.class);
      if (!this.getIsShimmed()) {
         Thread t = new ShutdownHookThread();
         Runtime.getRuntime().addShutdownHook(t);
      }

   }

   private static String GetEnvironmentName() {
      String systemEnvironment = System.getenv("ENVIRONMENT");
      if (systemEnvironment != null && !systemEnvironment.isEmpty()) {
         return systemEnvironment;
      } else {
         String userData = null;

         try {
            Pattern userDataPattern = Pattern.compile(".*bbb-games/boot_straps/my_singing_monsters/na/([^/]+)/bootstrap.*", 32);

            try {
               userData = EC2MetadataUtils.getUserData();
            } catch (Exception var4) {
               userData = "aws s3 cp s3://bbb-games/boot_straps/my_singing_monsters/na/mobile_sfs_dev/bootstrap.php bootstrap.php";
            }

            Matcher userDateMatcher = userDataPattern.matcher(userData);
            return userDateMatcher.find() ? userDateMatcher.group(1) : null;
         } catch (Exception var5) {
            Logger.trace(var5, "Unable to parse environment from userData: " + userData);
            return null;
         }
      }
   }

   public void init() {
      if (!this.getIsShimmed()) {
         try {
            Field loggerField = BaseSFSExtension.class.getDeclaredField("logger");
            loggerField.setAccessible(true);
            SimpleLogger.SetLogger((org.slf4j.Logger)loggerField.get(this));
            if (!Misc.hasEnv("AWS_REGION")) {
               Misc.updateEnv("AWS_REGION", "us-east-1");
            }
         } catch (Exception var17) {
            Logger.trace(var17);
         }
      }

      Properties props = this.getConfigProperties();
      this.devServer = "1".equalsIgnoreCase(props.getProperty("dev"));
      SmartFoxServer.getInstance().getEventManager().setThreadPoolSize(75);
      super.init();
      instance = this;
      this.lagSimulationMillis = 0;
      this.db = new DbWrapper(this, "com.bigbluebubble.mysingingmonsters.MSMExtension");
      this.sqlCodec = new MySQLCodec(Mode.STANDARD);
      ESAPI.encoder().encodeForSQL(this.sqlCodec, "test");

      try {
         this.db.update("SET CHARACTER SET 'utf8mb4';");
         this.db.update("SET SESSION collation_connection = 'utf8mb4_general_ci';");
      } catch (Exception var16) {
         Logger.trace(var16);
      }

      this.topStagesRefresh = 0L;
      this.topStages = null;
      this.topComposerStagesRefresh = 0L;
      this.topComposerStages = null;
      this.topTribesRefresh = 0L;
      this.topTribes = null;
      this.initGameSettings(this.db);
      int i;
      if (!this.isDevServer() && GameSettings.get("CLOUDWATCH_METRICS_ENABLED", false)) {
         i = GameSettings.get("CLOUDWATCH_METRICS_THREADS", 1);
         String environmentName = GetEnvironmentName();
         Logger.trace("Startup in in environment: ", environmentName);
         if (environmentName == null) {
            environmentName = "";
         } else {
            environmentName = "/" + environmentName;
         }

         this.cw = new CloudwatchMetrics(this, "MSM/SFS" + environmentName, i, 60000L, "us-east-1", new String[0]);
         this.cw.addMetricAdder(new CloudwatchMetricAdder() {
            final Runtime runtime = Runtime.getRuntime();
            final Zone zone = MSMExtension.this.getParentZone();

            public void add(CloudwatchMetrics cw) {
               long totalMemory = this.runtime.totalMemory();
               long freeMemory = this.runtime.freeMemory();
               long usedMemory = totalMemory - freeMemory;
               cw.addMetric("Heap Total", StandardUnit.Bytes, (double)totalMemory);
               cw.addMetric("Heap Usage", StandardUnit.Bytes, (double)usedMemory);
               cw.addMetric("Heap Free", StandardUnit.Bytes, (double)freeMemory);
               cw.addMetric("Players", StandardUnit.None, (double)this.zone.getUserCount());
            }
         });
      }

      try {
         this.relicDiamondCosts = new JSONArray(GameSettings.get("USER_RELIC_DIAMOND_COSTS", "[3,6,9,12,19,27,39,50]"));
         PlayerCrucibleData.setHeatCosts(new JSONArray(GameSettings.get("USER_CRUCIBLE_FUEL_RELIC_COSTS_v2", "[2,4,6,12]")));

         JSONArray debugVals;
         for(i = 0; i < PlayerCrucibleData.maxNumHeatLevels(); ++i) {
            debugVals = new JSONArray(GameSettings.get("CRUCIBLE_HEAT_COLLECTION_RANGE_LEVEL_" + (i + 1), "[0,0]"));
            PlayerCrucibleData.setHeatCollectionRange(i + 1, debugVals.getInt(0), debugVals.getInt(1));
         }

         JSONArray levelRange = new JSONArray(GameSettings.get("USER_CRUCIBLE_LEVEL_BOOST_RANGE", "[5,15]"));
         PlayerCrucibleData.setLevelBoostRange(levelRange.getInt(0), levelRange.getInt(1));
         debugVals = new JSONArray(GameSettings.get("DEBUG_CRUCIBLE_GUARANTEE_SUCC_v2", "[0.0, 0.0]"));
         PlayerCrucibleData.setDebugSuccess(debugVals.getDouble(0), debugVals.getDouble(1));
      } catch (Exception var18) {
         Logger.trace(var18);
      }

      PlayerCrucibleData.setUnlockStages(GameSettings.get("USER_CRUCIBLE_UNLOCKS"));
      PlayerCrucibleData.setFailTimeMultiplier(GameSettings.getDouble("CRUCIBLE_FAILTIME_MULTIPLIER"));
      PlayerCrucibleData.setBoostFromHeatLevel(GameSettings.getDouble("USER_CRUCIBLE_HEAT_BOOST"), GameSettings.getDouble("USER_CRUCIBLE_HEAT_BOOST_EPIC_FIXED"));
      PlayerCrucibleData.setMaxFlagBoost(GameSettings.getDouble("USER_CRUCIBLE_MAX_FLAG_BOOST"), GameSettings.getDouble("USER_CRUCIBLE_MAX_FLAG_BOOST_EPIC"));
      PlayerCrucibleData.setMaxMonsterLevelBoost(GameSettings.getDouble("USER_CRUCIBLE_MAX_LEVEL_BOOST"), GameSettings.getDouble("USER_CRUCIBLE_MAX_LEVEL_BOOST_EPIC"));
      PlayerCrucibleData.setBoostPerMonsterLevel(GameSettings.getDouble("USER_CRUCIBLE_LEVEL_BOOST"), GameSettings.getDouble("USER_CRUCIBLE_LEVEL_BOOST_EPIC"));
      PlayerCrucibleData.setDebuggingOn(GameSettings.get("CRUCIBLE_DEBUGGING_ON", 0) != 0);
      PlayerMonster.setDebugFasterUnderlingCurrencyGen(GameSettings.get("DEBUG_UNDERLING_FASTER_CURRENCY_GEN", 0) != 0);
      this.initSynthesizingSettings();
      this.initDailyLoginRewards();

      try {
         this.serverHostname = props.getProperty("hostname");
         if (this.serverHostname == null) {
            this.serverHostname = InetAddress.getLocalHost().getHostName();
         }

         this.serverPublicHostname = props.getProperty("public_hostname");
         if (this.serverPublicHostname == null) {
            this.serverPublicHostname = this.serverHostname;
         }
      } catch (UnknownHostException var15) {
         Logger.trace((Exception)var15, "Unable to retrieve valid server host name");
      }

      try {
         this.stats = new MSMStats(this, new BasicAWSCredentials("AKIAILEBZY6UGI2N4XTQ", "RPEpxrfrTUzOyDXNq8mMvvznFmi28gest3KsG1Mg"));
         this.sns = new SnsWrapper(this, new BasicAWSCredentials("AKIAILEBZY6UGI2N4XTQ", "RPEpxrfrTUzOyDXNq8mMvvznFmi28gest3KsG1Mg"));
         this.sqs = new SqsWrapper(this, new BasicAWSCredentials("AKIAILEBZY6UGI2N4XTQ", "RPEpxrfrTUzOyDXNq8mMvvznFmi28gest3KsG1Mg"));
         this.cache = new CacheWrapper(GameSettings.get("CACHE_SERVER"), GameSettings.getInt("CACHE_PORT"), GameSettings.get("CACHE_PREFIX"));
      } catch (Exception var14) {
         Logger.trace(var14);
      }

      this.initAuthTasks();

      try {
         long startTime = System.currentTimeMillis();
         String sql = "SELECT NOW() AS time_stamp_now";
         ISFSArray result = this.db.query(sql);
         if (result.size() == 1) {
            long dbTime = result.getSFSObject(0).getLong("time_stamp_now");
            long endTime = System.currentTimeMillis();
            long diffTime = endTime - startTime;
            dbTimeOffset = endTime - dbTime - diffTime / 2L;
            Logger.trace("dbTimeOffset: " + dbTimeOffset);
         }
      } catch (Exception var13) {
      }

      this.collectAllWaitMinutesPaying = GameSettings.getInt("USER_COLLECT_ALL_WAIT_MINS_PAYING");
      this.collectAllWaitMinutesNonPaying = GameSettings.getInt("USER_COLLECT_ALL_WAIT_MINS_NON_PAYING");
      String etherealIslandsWithModsStr = GameSettings.get("USER_ETHEREAL_ISLANDS_WITH_MODS");
      String[] tokens = etherealIslandsWithModsStr.split("[ ,]+");
      this.etherealIslandsWithModifiers = new ArrayList();

      for(int i = 0; i < tokens.length; ++i) {
         this.etherealIslandsWithModifiers.add(Integer.parseInt(tokens[i]));
      }

      this.initMail();
      this.initGeo();
      StaticDataSqlLoader.loadAndCacheStaticData();

      try {
         AccessKeys.init(this.db);
         TimedEventManager.init(this.db);
         CodeLookup.init(this.db, GameSettings.get("CODE_ENCRYPTION_KEY"), GameSettings.get("CODE_ENCRYPTION_IV"));
         BattleVersusChampionRanks.init(this.db);
         BattleSeasons.init(this.db, GameSettings.get("BATTLE_SEASONS_REFRESH_MIN", 5));
      } catch (Exception var12) {
         Logger.trace(var12);
      }

      UpdateActiveTimedEvents.schedule();
      this.clearAllLockedAccounts();
      MSMCommandLogging.init();
      this.addRequestHandler("db_monster", GameStateHandler.class);
      this.addRequestHandler("db_versions", GameStateHandler.class);
      this.addRequestHandler("db_gene", GameStateHandler.class);
      this.addRequestHandler("db_structure", GameStateHandler.class);
      this.addRequestHandler("db_island", GameStateHandler.class);
      this.addRequestHandler("db_level", GameStateHandler.class);
      this.addRequestHandler("db_store", GameStateHandler.class);
      this.addRequestHandler("db_store_v2", GameStateHandler.class);
      this.addRequestHandler("db_scratch_offs", GameStateHandler.class);
      this.addRequestHandler("db_flexeggdefs", GameStateHandler.class);
      this.addRequestHandler("gs_quest", GameStateHandler.class);
      this.addRequestHandler("gs_store_replacements", GameStateHandler.class);
      this.addRequestHandler("gs_timed_events", GameStateHandler.class);
      this.addRequestHandler("gs_rare_monster_data", GameStateHandler.class);
      this.addRequestHandler("gs_epic_monster_data", GameStateHandler.class);
      this.addRequestHandler("gs_monster_island_2_island_data", GameStateHandler.class);
      this.addRequestHandler("gs_flip_levels", GameStateHandler.class);
      this.addRequestHandler("gs_flip_boards", GameStateHandler.class);
      this.addRequestHandler("test_types", GameStateHandler.class);
      this.addRequestHandler("gs_dipster_data", GameStateHandler.class);
      this.addRequestHandler("gs_entity_alt_cost_data", GameStateHandler.class);
      this.addRequestHandler("gs_cant_breed", GameStateHandler.class);
      this.addRequestHandler("gs_player", GameStateHandler.class);
      this.addRequestHandler("gs_request_next_relic_reset", GameStateHandler.class);
      this.addRequestHandler("gs_set_displayname", GameStateHandler.class);
      this.addRequestHandler("gs_set_tribename", GameStateHandler.class);
      this.addRequestHandler("gs_set_islandname", GameStateHandler.class);
      this.addRequestHandler("gs_refresh_tribe_requests", GameStateHandler.class);
      this.addRequestHandler("gs_get_code", GameStateHandler.class);
      this.addRequestHandler("gs_transfer_code", GameStateHandler.class);
      this.addRequestHandler("gs_set_moniker", GameStateHandler.class);
      this.addRequestHandler("gs_set_last_timed_theme", GameStateHandler.class);
      this.addRequestHandler("gs_update_island_tutorials", GameStateHandler.class);
      this.addRequestHandler("gs_buy_island", GameStateHandler.class);
      this.addRequestHandler("gs_change_island", GameStateHandler.class);
      this.addRequestHandler("gs_place_on_gold_island", GameStateHandler.class);
      this.addRequestHandler("gs_save_island_warp_speed", GameStateHandler.class);
      this.addRequestHandler("gs_place_on_tribal", GameStateHandler.class);
      this.addRequestHandler("gs_cancel_tribe_request", GameStateHandler.class);
      this.addRequestHandler("gs_send_tribe_request", GameStateHandler.class);
      this.addRequestHandler("gs_send_tribe_invite", GameStateHandler.class);
      this.addRequestHandler("gs_join_tribe", GameStateHandler.class);
      this.addRequestHandler("gs_leave_tribe_request", GameStateHandler.class);
      this.addRequestHandler("gs_kick_tribe_request", GameStateHandler.class);
      this.addRequestHandler("gs_cancel_tribe_invite", GameStateHandler.class);
      this.addRequestHandler("gs_decline_all_tribal_invites", GameStateHandler.class);
      this.addRequestHandler("gs_get_random_tribes", GameStateHandler.class);
      this.addRequestHandler("gs_activate_island_theme", GameStateHandler.class);
      this.addRequestHandler("gs_buy_egg", GameStateHandler.class);
      this.addRequestHandler("gs_sell_egg", GameStateHandler.class);
      this.addRequestHandler("gs_hatch_egg", GameStateHandler.class);
      this.addRequestHandler("gs_speed_up_hatching", GameStateHandler.class);
      this.addRequestHandler("gs_box_add_egg", GameStateHandler.class);
      this.addRequestHandler("gs_box_add_monster", GameStateHandler.class);
      this.addRequestHandler("gs_box_activate_monster", GameStateHandler.class);
      this.addRequestHandler("gs_attempt_early_box_activate", GameStateHandler.class);
      this.addRequestHandler("gs_box_purchase_fill_cost", GameStateHandler.class);
      this.addRequestHandler("gs_box_purchase_fill", GameStateHandler.class);
      this.addRequestHandler("gs_start_amber_evolve", GameStateHandler.class);
      this.addRequestHandler("gs_finish_amber_evolve", GameStateHandler.class);
      this.addRequestHandler("gs_speedup_amber_evolve", GameStateHandler.class);
      this.addRequestHandler("gs_collect_cruc_heat", GameStateHandler.class);
      this.addRequestHandler("gs_purchase_evolve_unlock", GameStateHandler.class);
      this.addRequestHandler("gs_purchase_evo_powerup_unlock", GameStateHandler.class);
      this.addRequestHandler("gs_viewed_cruc_monst", GameStateHandler.class);
      this.addRequestHandler("gs_viewed_cruc_unlock", GameStateHandler.class);
      this.addRequestHandler("gs_send_ethereal_monster", GameStateHandler.class);
      this.addRequestHandler("gs_send_monster_home", GameStateHandler.class);
      this.addRequestHandler("gs_move_monster", GameStateHandler.class);
      this.addRequestHandler("gs_feed_monster", GameStateHandler.class);
      this.addRequestHandler("gs_sell_monster", GameStateHandler.class);
      this.addRequestHandler("gs_mute_monster", GameStateHandler.class);
      this.addRequestHandler("gs_flip_monster", GameStateHandler.class);
      this.addRequestHandler("gs_collect_monster", GameStateHandler.class);
      this.addRequestHandler("gs_test_collect_monster", GameStateHandler.class);
      this.addRequestHandler("gs_name_monster", GameStateHandler.class);
      this.addRequestHandler("gs_multi_neighbors", GameStateHandler.class);
      this.addRequestHandler("gs_store_monster", GameStateHandler.class);
      this.addRequestHandler("gs_unstore_monster", GameStateHandler.class);
      this.addRequestHandler("gs_tribal_feed_monster", GameStateHandler.class);
      this.addRequestHandler("gs_breed_monsters", GameStateHandler.class);
      this.addRequestHandler("gs_finish_breeding", GameStateHandler.class);
      this.addRequestHandler("gs_speed_up_breeding", GameStateHandler.class);
      this.addRequestHandler("gs_unlock_breeding_structure", GameStateHandler.class);
      this.addRequestHandler("gs_buy_structure", GameStateHandler.class);
      this.addRequestHandler("gs_move_structure", GameStateHandler.class);
      this.addRequestHandler("gs_sell_structure", GameStateHandler.class);
      this.addRequestHandler("gs_flip_structure", GameStateHandler.class);
      this.addRequestHandler("gs_mute_structure", GameStateHandler.class);
      this.addRequestHandler("gs_start_upgrade_structure", GameStateHandler.class);
      this.addRequestHandler("gs_finish_upgrade_structure", GameStateHandler.class);
      this.addRequestHandler("gs_finish_structure", GameStateHandler.class);
      this.addRequestHandler("gs_collect_structure", GameStateHandler.class);
      this.addRequestHandler("gs_speed_up_structure", GameStateHandler.class);
      this.addRequestHandler("gs_store_decoration", GameStateHandler.class);
      this.addRequestHandler("gs_unstore_decoration", GameStateHandler.class);
      this.addRequestHandler("gs_store_buddy", GameStateHandler.class);
      this.addRequestHandler("gs_unstore_buddy", GameStateHandler.class);
      this.addRequestHandler("gs_mega_monster_message", GameStateHandler.class);
      this.addRequestHandler("gs_light_torch", GameStateHandler.class);
      this.addRequestHandler("gs_get_torchgifts", GameStateHandler.class);
      this.addRequestHandler("gs_collect_torchgift", GameStateHandler.class);
      this.addRequestHandler("gs_additional_friend_torch_data", GameStateHandler.class);
      this.addRequestHandler("gs_set_light_torch_flag", GameStateHandler.class);
      this.addRequestHandler("gs_visit_specific_friend_island", GameStateHandler.class);
      this.addRequestHandler("gs_set_fav_friend", GameStateHandler.class);
      this.addRequestHandler("gs_start_baking", GameStateHandler.class);
      this.addRequestHandler("gs_finish_baking", GameStateHandler.class);
      this.addRequestHandler("gs_speed_up_baking", GameStateHandler.class);
      this.addRequestHandler("gs_start_rebake", GameStateHandler.class);
      this.addRequestHandler("gs_start_fuzing", GameStateHandler.class);
      this.addRequestHandler("gs_finish_fuzing", GameStateHandler.class);
      this.addRequestHandler("gs_speed_up_fuzing", GameStateHandler.class);
      this.addRequestHandler("gs_collect_from_mine", GameStateHandler.class);
      this.addRequestHandler("gs_start_obstacle", GameStateHandler.class);
      this.addRequestHandler("gs_clear_obstacle", GameStateHandler.class);
      this.addRequestHandler("gs_clear_obstacle_speed_up", GameStateHandler.class);
      this.addRequestHandler("gs_quest_event", GameStateHandler.class);
      this.addRequestHandler("gs_quest_read", GameStateHandler.class);
      this.addRequestHandler("gs_quests_read", GameStateHandler.class);
      this.addRequestHandler("gs_quest_collect", GameStateHandler.class);
      this.addRequestHandler("gs_update_achievement_status", GameStateHandler.class);
      this.addRequestHandler("gs_get_friends", GameStateHandler.class);
      this.addRequestHandler("gs_get_messages", GameStateHandler.class);
      this.addRequestHandler("gs_delete_message", GameStateHandler.class);
      this.addRequestHandler("gs_get_friend_visit_data", GameStateHandler.class);
      this.addRequestHandler("gs_get_random_visit_data", GameStateHandler.class);
      this.addRequestHandler("gs_get_tribal_island_data", GameStateHandler.class);
      this.addRequestHandler("gs_get_ranked_island_data", GameStateHandler.class);
      this.addRequestHandler("gs_get_island_rank", GameStateHandler.class);
      this.addRequestHandler("gs_rate_island", GameStateHandler.class);
      this.addRequestHandler("gs_report_user", GameStateHandler.class);
      this.addRequestHandler("gs_collect_invite_reward", GameStateHandler.class);
      this.addRequestHandler("gs_collect_rewards", GameStateHandler.class);
      this.addRequestHandler("gs_process_unclaimed_purchases", GameStateHandler.class);
      this.addRequestHandler("gs_currency_conversion", GameStateHandler.class);
      this.addRequestHandler("gs_currency_coins2eth_conversion", GameStateHandler.class);
      this.addRequestHandler("gs_currency_diamonds2eth_conversion", GameStateHandler.class);
      this.addRequestHandler("gs_currency_eth2diamonds_conversion", GameStateHandler.class);
      this.addRequestHandler("gs_currency_generic_conversion", GameStateHandler.class);
      this.addRequestHandler("gs_collect_facebook_reward", GameStateHandler.class);
      this.addRequestHandler("gs_referral_request", GameStateHandler.class);
      this.addRequestHandler("gs_collect_daily_currency_pack", GameStateHandler.class);
      this.addRequestHandler("gs_refresh_daily_currency_pack", GameStateHandler.class);
      this.addRequestHandler("gs_player_has_scratch_off", GameStateHandler.class);
      this.addRequestHandler("gs_play_scratch_off", GameStateHandler.class);
      this.addRequestHandler("gs_purchase_scratch_off", GameStateHandler.class);
      this.addRequestHandler("gs_collect_scratch_off", GameStateHandler.class);
      this.addRequestHandler("gs_get_memory_game_numbers", GameStateHandler.class);
      this.addRequestHandler("gs_memory_minigame_current_cost", GameStateHandler.class);
      this.addRequestHandler("gs_flip_minigame_cost", GameStateHandler.class);
      this.addRequestHandler("gs_purchase_memory_mini_game", GameStateHandler.class);
      this.addRequestHandler("gs_collect_memory_mini_game", GameStateHandler.class);
      this.addRequestHandler("gs_collect_flip_level", GameStateHandler.class);
      this.addRequestHandler("gs_collect_flip_mini_game", GameStateHandler.class);
      this.addRequestHandler("gs_sticker", GameStateHandler.class);
      this.addRequestHandler("gs_purchase_flip_mini_game", GameStateHandler.class);
      this.addRequestHandler("gs_delete_composer_template", GameStateHandler.class);
      this.addRequestHandler("gs_save_composer_template", GameStateHandler.class);
      this.addRequestHandler("gs_save_composer_track", GameStateHandler.class);
      this.addRequestHandler("gs_delete_mail", GameStateHandler.class);
      this.addRequestHandler("gs_offer_viewed", GameStateHandler.class);
      this.addRequestHandler("gs_offer_completed", GameStateHandler.class);
      this.addRequestHandler("gs_promos", GameStateHandler.class);
      this.addRequestHandler("gs_paywall_updated", GameStateHandler.class);
      this.addRequestHandler("gs_app_link", GameStateHandler.class);
      this.addRequestHandler("gs_send_facebook_help", GameStateHandler.class);
      this.addRequestHandler("gs_handle_facebook_help_instances", GameStateHandler.class);
      this.addRequestHandler("gs_request_facebook_help_permissions", GameStateHandler.class);
      this.addRequestHandler("gs_purchase_buyback", GameStateHandler.class);
      this.addRequestHandler("gs_admin_purchase_buyback", GameStateHandler.class);
      this.addRequestHandler("gs_collect_daily_reward", GameStateHandler.class);
      this.addRequestHandler("gs_daily_login_buyback", GameStateHandler.class);
      this.addRequestHandler("gs_give_me_shit", GameStateHandler.class);
      this.addRequestHandler("gs_add_friend", GameStateHandler.class);
      this.addRequestHandler("gs_remove_friend", GameStateHandler.class);
      this.addRequestHandler("gs_sync_friends", GameStateHandler.class);
      this.addRequestHandler("gs_delete_account", GameStateHandler.class);
      this.addRequestHandler("keep_alive", GameStateHandler.class);
      this.addRequestHandler("gs_get_starting_ccu", GameStateHandler.class);
      this.addRequestHandler("gs_get_finishing_ccu", GameStateHandler.class);
      this.addRequestHandler("gs_get_ccu", GameStateHandler.class);
      this.addRequestHandler("gs_admin_get_codes", GameStateHandler.class);
      this.addRequestHandler("gs_hacky_make_monster_without_egg", GameStateHandler.class);
      this.addRequestHandler("gs_admin_get_user_visit_data", GameStateHandler.class);
      this.addRequestHandler("gs_admin_move_users_monster", GameStateHandler.class);
      this.addRequestHandler("gs_admin_move_users_structure", GameStateHandler.class);
      this.addRequestHandler("gs_admin_destroy_users_monster", GameStateHandler.class);
      this.addRequestHandler("gs_admin_destroy_users_structure", GameStateHandler.class);
      this.addRequestHandler("gs_admin_buy_egg", GameStateHandler.class);
      this.addRequestHandler("gs_admin_user_neighbors", GameStateHandler.class);
      this.addRequestHandler("gs_admin_multi_neighbors", GameStateHandler.class);
      this.addRequestHandler("gs_admin_hatch_egg", GameStateHandler.class);
      this.addRequestHandler("gs_admin_refresh_scratch_off_prizes", GameStateHandler.class);
      this.addRequestHandler("gs_admin_refresh_all", GameStateHandler.class);
      this.addRequestHandler("gs_admin_disable_logins", GameStateHandler.class);
      this.addRequestHandler("gs_admin_enable_logins", GameStateHandler.class);
      this.addRequestHandler("gs_admin_buy_structure", GameStateHandler.class);
      this.addRequestHandler("gs_admin_buy_island", GameStateHandler.class);
      this.addRequestHandler("gs_admin_change_island", GameStateHandler.class);
      this.addRequestHandler("gs_admin_destroy_island", GameStateHandler.class);
      this.addRequestHandler("gs_admin_feed_monster", GameStateHandler.class);
      this.addRequestHandler("gs_admin_start_upgrade_structure", GameStateHandler.class);
      this.addRequestHandler("gs_admin_give_me_shit", GameStateHandler.class);
      this.addRequestHandler("gs_admin_speed_up_breeding", GameStateHandler.class);
      this.addRequestHandler("gs_admin_remove_breeding", GameStateHandler.class);
      this.addRequestHandler("gs_admin_finish_breeding", GameStateHandler.class);
      this.addRequestHandler("gs_admin_speed_up_hatching", GameStateHandler.class);
      this.addRequestHandler("gs_admin_sell_egg", GameStateHandler.class);
      this.addRequestHandler("gs_admin_flip_monster", GameStateHandler.class);
      this.addRequestHandler("gs_admin_flip_structure", GameStateHandler.class);
      this.addRequestHandler("gs_admin_start_baking", GameStateHandler.class);
      this.addRequestHandler("gs_admin_speed_up_baking", GameStateHandler.class);
      this.addRequestHandler("gs_admin_start_fuzing", GameStateHandler.class);
      this.addRequestHandler("gs_admin_speed_up_fuzing", GameStateHandler.class);
      this.addRequestHandler("gs_admin_clear_obstacle_speed_up", GameStateHandler.class);
      this.addRequestHandler("gs_admin_speed_up_structure", GameStateHandler.class);
      this.addRequestHandler("gs_admin_name_monster", GameStateHandler.class);
      this.addRequestHandler("gs_admin_quest", GameStateHandler.class);
      this.addRequestHandler("gs_admin_complete_quest", GameStateHandler.class);
      this.addRequestHandler("gs_admin_check_quests", GameStateHandler.class);
      this.addRequestHandler("gs_admin_send_ethereal_monster", GameStateHandler.class);
      this.addRequestHandler("gs_admin_send_monster_home", GameStateHandler.class);
      this.addRequestHandler("gs_admin_kick_user", GameStateHandler.class);
      this.addRequestHandler("gs_admin_store_monster", GameStateHandler.class);
      this.addRequestHandler("gs_admin_unstore_monster", GameStateHandler.class);
      this.addRequestHandler("gs_admin_store_decoration", GameStateHandler.class);
      this.addRequestHandler("gs_admin_unstore_decoration", GameStateHandler.class);
      this.addRequestHandler("gs_admin_unlight_torch", GameStateHandler.class);
      this.addRequestHandler("gs_admin_box_monster_toggle", GameStateHandler.class);
      this.addRequestHandler("gs_admin_box_monster_toggle_new", GameStateHandler.class);
      this.addRequestHandler("gs_admin_box_purchase_fill", GameStateHandler.class);
      this.addRequestHandler("gs_admin_box_activate_monster", GameStateHandler.class);
      this.addRequestHandler("gs_admin_finished_edit", GameStateHandler.class);
      this.addRequestHandler("gs_admin_store_buddy", GameStateHandler.class);
      this.addRequestHandler("gs_admin_unstore_buddy", GameStateHandler.class);
      this.addRequestHandler("gs_admin_finish_fuzing", GameStateHandler.class);
      this.addRequestHandler("gs_admin_swap_tribal_monster", GameStateHandler.class);
      this.addRequestHandler("gs_test_breed_monsters", GameStateHandler.class);
      this.addRequestHandler("gs_test_breed_costumes", GameStateHandler.class);
      this.addRequestHandler("gs_test_spin_probabilities", GameStateHandler.class);
      this.addRequestHandler("gs_test_scratch", GameStateHandler.class);
      this.addRequestHandler("gs_admin_select_breed_egg", GameStateHandler.class);
      this.addRequestHandler("gs_test_cruc_evolv", GameStateHandler.class);
      this.addRequestHandler("gs_test_synthesis", GameStateHandler.class);
      this.addRequestHandler("db_battle", GameStateHandler.class);
      this.addRequestHandler("db_battle_levels", GameStateHandler.class);
      this.addRequestHandler("db_battle_monster_training", GameStateHandler.class);
      this.addRequestHandler("db_battle_monster_actions", GameStateHandler.class);
      this.addRequestHandler("db_battle_monster_stats", GameStateHandler.class);
      this.addRequestHandler("db_battle_music", GameStateHandler.class);
      this.addRequestHandler("battle_teleport", GameStateHandler.class);
      this.addRequestHandler("battle_start", GameStateHandler.class);
      this.addRequestHandler("battle_start_versus", GameStateHandler.class);
      this.addRequestHandler("battle_start_friend", GameStateHandler.class);
      this.addRequestHandler("battle_finish", GameStateHandler.class);
      this.addRequestHandler("battle_start_training", GameStateHandler.class);
      this.addRequestHandler("battle_finish_training", GameStateHandler.class);
      this.addRequestHandler("battle_purchase_campaign_reward", GameStateHandler.class);
      this.addRequestHandler("battle_set_music", GameStateHandler.class);
      this.addRequestHandler("battle_refresh_versus_attempts", GameStateHandler.class);
      this.addRequestHandler("battle_claim_versus_rewards", GameStateHandler.class);
      this.addRequestHandler("update_viewed_campaigns", GameStateHandler.class);
      this.addRequestHandler("admin_battle_campaign_reset", GameStateHandler.class);
      this.addRequestHandler("gs_set_avatar", GameStateHandler.class);
      this.addRequestHandler("db_costumes", GameStateHandler.class);
      this.addRequestHandler("purchase_costume", GameStateHandler.class);
      this.addRequestHandler("equip_costume", GameStateHandler.class);
      this.addRequestHandler("update_awakener", GameStateHandler.class);
      this.addRequestHandler("gs_viewed_egg", GameStateHandler.class);
      this.addRequestHandler("db_daily_cumulative_login", GameStateHandler.class);
      this.addRequestHandler("collect_daily_cumulative_login_rewards", GameStateHandler.class);
      this.addRequestHandler("gs_start_attuning", GameStateHandler.class);
      this.addRequestHandler("gs_finish_attuning", GameStateHandler.class);
      this.addRequestHandler("gs_speedup_attuning", GameStateHandler.class);
      this.addRequestHandler("db_attuner_gene", GameStateHandler.class);
      this.addRequestHandler("gs_start_synthesizing", GameStateHandler.class);
      this.addRequestHandler("gs_speedup_synthesizing", GameStateHandler.class);
      this.addRequestHandler("gs_collect_synthesizing_failure", GameStateHandler.class);
      this.addRequestHandler("test", TestMultiHandler.class);
   }

   protected void sendVersionError(User user, boolean optionalUpdate) {
      ISession session = user.getSession();
      String clientPlatform = (String)session.getProperty("client_platform");
      ISFSArray urls = SFSArray.newFromJsonData(GameSettings.get("CLIENT_UPGRADE_URLS"));
      ISFSObject platformObject = null;
      ISFSObject androidObject = null;

      for(int i = 0; i < urls.size(); ++i) {
         ISFSObject o = urls.getSFSObject(i);
         if (o.getUtfString("platform").equals("android")) {
            androidObject = o;
         }

         if (o.getUtfString("platform").equals(clientPlatform)) {
            platformObject = o;
         }
      }

      if (platformObject != null && androidObject != null) {
         androidObject.putUtfString("url", platformObject.getUtfString("url"));
      }

      ISFSObject response = new SFSObject();
      response.putBool("success", false);
      response.putUtfString("message", "client version fail");
      response.putSFSArray("urls", urls);
      if (optionalUpdate) {
         this.send("gs_friend_version_error", response, user);
      } else {
         session.setProperty("version_fail", true);
         this.send("gs_client_version_error", response, user);
      }

   }

   protected void sendFriendBannedError(User user, Player friend) {
      ISFSObject response = new SFSObject();
      response.putBool("success", false);
      response.putBool("banned", true);
      response.putUtfString("message", friend.requestedDeletion() ? "NOTIFICATION_ACCOUNT_DELETED" : "NOTIFICATION_FRIEND_BANNED");
      this.send("gs_get_friend_visit_data", response, user);
   }

   public void clearAllLockedAccounts() {
      String sql = "DELETE FROM admin_user_accounts_currently_being_accessed";

      try {
         getInstance().getDB().update(sql);
      } catch (Exception var3) {
         Logger.trace(var3, "error clearing admin_user_accounts_currently_being_accessed during extension restart");
      }

   }

   public void clearAdminsLockedAccounts(long adminBbbid) {
      String sql = "DELETE FROM admin_user_accounts_currently_being_accessed WHERE admin_bbbid=?";
      Object[] args = new Object[]{adminBbbid};

      try {
         getInstance().getDB().update(sql, args);
      } catch (Exception var6) {
         Logger.trace(var6, "error clearing admin " + adminBbbid + "'s admin_user_accounts_currently_being_accessed user during new account access");
      }

   }

   public void markAccountLockedForAdminEditing(User adminSender, long bbbid, long adminBbbid) {
      this.clearAdminsLockedAccounts(adminBbbid);
      long time = CurrentDBTime();
      String sql = "INSERT INTO admin_user_accounts_currently_being_accessed SET user_bbbid=?, admin_bbbid=?, date_accessed=?";
      Object[] args = new Object[]{bbbid, adminBbbid, time};

      try {
         this.getDB().update(sql, args);
         MSMAdminCatchUnreleasedAccount.addAccountToCSRPermaLockPreventionList(bbbid, time);
      } catch (Exception var11) {
         Logger.trace(var11, "error adding user " + bbbid + " to admin editing table admin_user_accounts_currently_being_accessed");
      }

   }

   public void resetAccountAccessTime(long bbbid) {
      MSMAdminCatchUnreleasedAccount.resetAccountAccessTime(bbbid);
   }

   public void markAccountAdminEditingComplete(long bbbid) {
      String sql = "DELETE FROM admin_user_accounts_currently_being_accessed WHERE user_bbbid=?";
      Object[] args = new Object[]{bbbid};

      try {
         this.getDB().update(sql, args);
         MSMAdminCatchUnreleasedAccount.removeFromLockedAccountsList(bbbid);
      } catch (Exception var6) {
         Logger.trace(var6, "error removing user " + bbbid + " from editing table");
      }

   }

   public Player adminCreatePlayerByBBBID(long bbbID) throws Exception {
      Object[] query_params = new Object[]{bbbID};
      String sql = "SELECT user_id FROM users WHERE bbb_id=?";
      SFSArray records = this.db.query(sql, query_params);
      if (records.size() == 0) {
         throw new UserNotFoundException();
      } else {
         int userId = records.getSFSObject(0).getInt("user_id");
         sql = "SELECT island FROM user_island_data WHERE user=?";
         ISFSArray hasNewDataResult = this.db.query(sql, new Object[]{userId});
         if (hasNewDataResult.size() == 0) {
            throw new UserNotFoundException();
         } else {
            Player p = this.createPlayerByID((long)userId, true, new VersionInfo("0.0.0"), "ios", (String)null);
            getInstance().initializePlayerMusic(p, new Object[]{p.getPlayerId()});
            return p;
         }
      }
   }

   public void updateLastLogin(long userID) throws Exception {
      String sql = "UPDATE users SET last_login = NOW() WHERE user_id = ?";
      this.db.update("UPDATE users SET last_login = NOW() WHERE user_id = ?", new Object[]{userID});
   }

   public Player createPlayerByID(long userID, boolean fullPlayer, VersionInfo clientVer, String clientPlatform, String clientSubplatform) throws UserNotFoundException, InvalidPlayerException {
      try {
         Object[] query_params = new Object[]{userID};
         String sql = "SELECT users.*, user_tribal_starpower.*, user_keys.*, user_relics.relics, user_relics.daily_relic_purchase_count, user_relics.last_relic_purchase, user_egg_wildcards.egg_wildcards, user_banned.reason, user_banned.ban_expiry FROM users LEFT OUTER JOIN user_tribal_starpower ON users.user_id=user LEFT OUTER JOIN user_keys ON users.bbb_id=user_keys.bbb_id LEFT OUTER JOIN user_relics ON users.user_id=user_relics.user_id LEFT OUTER JOIN user_egg_wildcards ON users.user_id=user_egg_wildcards.user_id LEFT OUTER JOIN user_banned ON users.bbb_id=user_banned.bbb_id WHERE users.user_id=?";
         SFSArray records = this.db.query(sql, query_params);
         if (records.size() == 0) {
            throw new UserNotFoundException();
         } else {
            if (fullPlayer) {
               sql = "SELECT * FROM user_prizes WHERE user_id = ?";
               SFSArray prizes = this.db.query(sql, query_params);
               if (prizes != null && prizes.size() > 0) {
                  records.getSFSObject(0).putSFSObject("prizes", prizes.getSFSObject(0));
               }
            }

            return this.createPlayerWithUserRecord(records.getSFSObject(0), fullPlayer, clientVer, clientPlatform, clientSubplatform);
         }
      } catch (Exception var11) {
         Logger.trace(var11, "==== USER CREATION ERROR ========");
         throw new InvalidPlayerException("Error creating Player for user " + userID);
      }
   }

   public Player createPlayerWithUserRecord(ISFSObject record, boolean fullPlayer, VersionInfo clientVer, String clientPlatform, String clientSubplatform) throws Exception {
      Player p = new Player(record, fullPlayer, false);
      VersionInfo maxServerVerOfPlayerBeingCreated = VersionData.Instance().getMaxServerVersionFromClientVersion(p.lastClientVersion());
      VersionInfo maxServerVerSupportedByCurrentClient = VersionData.Instance().getMaxServerVersionFromClientVersion(clientVer);
      if (!fullPlayer) {
         if (p.isBanned()) {
            return p;
         }

         if (clientVer != null && maxServerVerSupportedByCurrentClient.compareTo(maxServerVerOfPlayerBeingCreated) < 0) {
            return null;
         }
      }

      Object[] userIdParam = new Object[]{p.getPlayerId()};
      if (fullPlayer) {
         p.setPlatform(clientPlatform);
         p.setSubplatform(clientSubplatform);
         this.initializePlayerGroupsFromUserGroupsTable(p, userIdParam);
         this.initializeAdvertisementGroup(p);
         this.initializePlayerQuestData(p, userIdParam);
         this.initializePlayerAchievementData(p, userIdParam);
         this.initializePlayerScratchTicketData(p);
      }

      this.initializePlayerGlobalData(p, userIdParam, fullPlayer);
      this.initializePlayerIslandThemes(p, userIdParam);
      this.initializePlayerIslands(p, userIdParam, fullPlayer);
      if (clientVer != null) {
         this.initializePlayerMusic(p, userIdParam);
      }

      if (fullPlayer) {
         this.initializePlayerAds(p, userIdParam);
         PromotionManager.setupPlayerLoginPromotions(p, clientVer);
         PromotionManager.setupPlayerTimedEvents(p, clientVer);
      }

      return p;
   }

   private void initializePlayerQuestData(Player p, Object[] userIdParam) throws Exception {
      String sql = "SELECT * from user_quest_data where user = ?";
      ISFSArray questData = this.db.query(sql, userIdParam);
      p.initQuests(questData);
   }

   private void initializePlayerGlobalData(Player p, Object[] userIdParam, boolean fullPlayer) throws Exception {
      String sql = "SELECT * from user_global_data where user_id = ?";
      ISFSArray globalData = this.db.query(sql, userIdParam);
      if (globalData.size() > 0) {
         p.initGlobalData(globalData.getSFSObject(0), fullPlayer);
      }

   }

   private void initializePlayerAchievementData(Player p, Object[] userIdParam) throws Exception {
      String sql = "SELECT * FROM user_achievements LEFT JOIN achievement_ids ON user_achievements.achievement=achievement_ids.bbb_achieve_id WHERE user = ?";
      ISFSArray achievementResult = this.db.query(sql, userIdParam);
      p.initAchievements(achievementResult);
   }

   public void initializePlayerGroupsFromUserGroupsTable(Player p, Object[] userIdParam) throws Exception {
      String sql = "SELECT * from user_groups where user = ?";
      ISFSArray groupData = this.db.query(sql, userIdParam);
      p.initGroups(groupData);
   }

   private void initializeAdvertisementGroup(Player p) {
      int groupId = AdvertisementGroup.getGroupToAssign(p);
      if (groupId != -1) {
         p.addToGroup(groupId);
      }

   }

   private void initializePlayerScratchTicketData(Player p) throws Exception {
      this.setupPlayerScratchData(p, "M");
      this.setupPlayerScratchData(p, "C");
      this.setupPlayerScratchData(p, "S");
   }

   private void initializePlayerIslandThemes(Player p, Object[] userIdParam) throws Exception {
      String sql = "SELECT * FROM user_island_themes where user_id = ?";
      ISFSArray islandThemesResult = this.db.query(sql, userIdParam);
      p.initIslandThemes(islandThemesResult);
   }

   private void initializePlayerIslands(Player p, Object[] userIdParam, boolean fullPlayer) throws Exception {
      String sql = "SELECT * FROM user_islands where user = ?";
      ISFSArray islandsResult = this.db.query(sql, userIdParam);
      p.initIslands(islandsResult);
      Iterator var6 = p.getIslands().iterator();

      while(var6.hasNext()) {
         PlayerIsland island = (PlayerIsland)var6.next();
         if (island.getIndex() != 9) {
            this.loadPlayerIslandData(p, island);
            island.initIslandThemeModifiers(p.getIslandThemes());
         } else {
            this.loadPlayerTribalIslandData(p, island, fullPlayer);
         }

         if (fullPlayer && island.isBattleIsland()) {
            IslandCostumeState islandCostumeState = island.getCostumeState();
            Iterator var9 = islandCostumeState.costumeIds().iterator();

            while(var9.hasNext()) {
               int costumeId = (Integer)var9.next();
               p.getCostumes().unlockCostume(costumeId);
            }

            p.saveCostumes();
         }
      }

      PlayerIsland goldIsland;
      if (p.getActiveIsland() == null) {
         goldIsland = p.getIslandByIslandIndex(1);
         if (goldIsland == null) {
            throw new Exception(String.format("Player %d with bad active island %d has NO first island", p.getBbbId(), p.getActiveIslandId()));
         }

         Logger.trace(String.format("Player %d has bad active island %d ... fixing to %d", p.getBbbId(), p.getActiveIslandId(), goldIsland.getID()));
         p.setActiveIsland(goldIsland.getID());
      }

      if (fullPlayer) {
         p.calculateMonsterIndex();
         goldIsland = p.getIslandByIslandIndex(6);
         if (goldIsland != null) {
            SFSArray giMappings = new SFSArray();
            Iterator goldMonsters = goldIsland.getMonsters().iterator();

            while(goldMonsters.hasNext()) {
               PlayerMonster curMonster = (PlayerMonster)goldMonsters.next();
               SFSObject parentData = curMonster.getParentIslandData();
               if (parentData != null) {
                  SFSObject curGiMappingData = new SFSObject();
                  curGiMappingData.putLong("user_gi_monster", curMonster.getID());
                  long parentId = parentData.getLong("monster");
                  curGiMappingData.putLong("user_monster", parentId);
                  giMappings.addSFSObject(curGiMappingData);
               }
            }

            goldIsland.setGiMappings(giMappings);
         }

      }
   }

   private void initializePlayerAds(Player p, Object[] userIdParam) throws Exception {
      ISFSObject jsonAdsObject = SFSObject.newFromJsonData(GameSettings.get("ADS_SHOW_THIRDPARTY_WHEN"));
      p.setThirdpartyAds(p.getInGamePurchaseCount() == 0 && p.getLevel() >= jsonAdsObject.getInt("player_level"));
      ISFSObject jsonVideoAdsObject = SFSObject.newFromJsonData(GameSettings.get("ADS_SHOW_THIRDPARTY_VIDEO_WHEN"));
      p.setThirdpartyVideoAds(p.getInGamePurchaseCount() == 0 && p.getLevel() >= jsonVideoAdsObject.getInt("player_level"));
   }

   public void initializePlayerMusic(Player p, Object[] userIdParam) throws Exception {
      String sql = "SELECT * from user_songs WHERE user=?";
      ISFSArray songData = this.db.query(sql, userIdParam);
      sql = "SELECT * from user_tracks WHERE user = ?";
      ISFSArray trackData = this.db.query(sql, userIdParam);
      p.initMusic(trackData, songData);
   }

   private void setupPlayerScratchData(Player player, String scratchType) throws Exception {
      String table;
      long scratchOffTimeBetweenPlays;
      if (scratchType.equalsIgnoreCase("M")) {
         table = "user_monster_scratch_offs";
         scratchOffTimeBetweenPlays = (long)ScratchTicketFunctions.monsterScratchOffTimeBetweenFreePlays();
      } else if (scratchType.equalsIgnoreCase("C")) {
         table = "user_scratch_offs";
         scratchOffTimeBetweenPlays = GameSettings.getLong("SCRATCHOFF_TIME_BETWEEN_FREE_PLAYS");
      } else {
         if (!scratchType.equalsIgnoreCase("S")) {
            throw new Exception("Error Invalid scratch type: " + scratchType);
         }

         table = "user_spin_wheels";
         scratchOffTimeBetweenPlays = GameSettings.getLong("SCRATCHOFF_TIME_BETWEEN_FREE_PLAYS");
      }

      try {
         String sql = "SELECT * FROM " + table + " WHERE user=?";
         Object[] args = new Object[]{player.getPlayerId()};
         ISFSArray results = this.getDB().query(sql, args);
         if (results.size() > 0) {
            ISFSObject ticket = results.getSFSObject(0);
            long lastFreePlay = ticket.getLong("last_free_play");
            long timeDelay = 3600000L * scratchOffTimeBetweenPlays;
            long eligibleToPlay = lastFreePlay + timeDelay;
            boolean unclaimedPrize = ticket.getInt("prize_claimed") == 0;
            String prize = ticket.getUtfString("last_prize");
            player.setNextScratchOffTime(scratchType, eligibleToPlay);
            player.setUnclaimedScratchOff(scratchType, unclaimedPrize);
            player.setScratchPrize(scratchType, prize);
         }

      } catch (Exception var18) {
         Logger.trace(var18);
         throw var18;
      }
   }

   public void loadPlayerIslandData(Player p, PlayerIsland island) throws Exception {
      try {
         String sql = "SELECT user_island_data.*, monsters_sold, costumes_owned FROM user_island_data LEFT JOIN user_sold_monsters ON user_island_data.island = user_sold_monsters.island LEFT JOIN user_prev_owned_costumes ON user_island_data.island = user_prev_owned_costumes.island WHERE user_island_data.island = ?";
         Object[] query_params = new Object[]{island.getID()};
         ISFSArray islandData = this.db.query(sql, query_params);
         if (islandData.size() == 1) {
            ISFSObject compactIslandData = islandData.getSFSObject(0);
            String structureData = Helpers.decompressJsonDataField(compactIslandData.getUtfString("structures"), "[]");
            island.initStructuresFromJson(structureData);
            String monsterData = Helpers.decompressJsonDataField(compactIslandData.getUtfString("monsters"), "[]");
            island.initMonstersFromJson(monsterData);
            island.fixMonsters();
            island.fixMonsterLevels();
            island.initSoldMonstersFromJson(compactIslandData.getUtfString("monsters_sold"));
            island.initPrevCostumesFromJson(compactIslandData.getUtfString("costumes_owned"));
            String jsonVolatileData = Helpers.decompressJsonDataField(compactIslandData.getUtfString("volatile"), "{}");
            ISFSObject volatileData = SFSObject.newFromJsonData(jsonVolatileData);
            SFSDataWrapper breedObject;
            if (volatileData.containsKey("eggs")) {
               breedObject = volatileData.get("eggs");
               if (breedObject.getTypeId() == SFSDataType.SFS_ARRAY) {
                  island.initEggs((ISFSArray)breedObject.getObject());
               } else {
                  if (breedObject.getTypeId() != SFSDataType.SFS_OBJECT) {
                     throw new PlayerLoadingException("Invalid type for eggs");
                  }

                  island.initEggs((ISFSObject)breedObject.getObject());
               }
            }

            if (volatileData.containsKey("breeding")) {
               breedObject = volatileData.get("breeding");
               if (breedObject.getTypeId() == SFSDataType.SFS_ARRAY) {
                  island.initBreeding((ISFSArray)breedObject.getObject());
               } else {
                  if (breedObject.getTypeId() != SFSDataType.SFS_OBJECT) {
                     throw new PlayerLoadingException("Invalid type for breeding");
                  }

                  island.initBreeding((ISFSObject)breedObject.getObject());
               }
            }

            if (volatileData.containsKey("evolving")) {
               island.initCrucible(volatileData.getSFSArray("evolving"));
            }

            if (volatileData.containsKey("attuning")) {
               island.initAttuning(volatileData.getSFSArray("attuning"));
            }

            if (volatileData.containsKey("attuned_critters")) {
               island.initCritters(volatileData.getSFSArray("attuned_critters"));
            }

            if (volatileData.containsKey("synthesizing")) {
               island.initSynthesizing(volatileData.getSFSArray("synthesizing"));
            }

            if (volatileData.containsKey("baking")) {
               island.initBaking(volatileData.getSFSArray("baking"));
            }

            if (volatileData.containsKey("last_baked")) {
               island.initLastBaked(volatileData.getSFSArray("last_baked"));
            }

            if (volatileData.containsKey("fuzer")) {
               island.initFuzeBuddies(volatileData.getSFSArray("fuzer"));
            }

            if (volatileData.containsKey("torches")) {
               island.initTorches(volatileData.getSFSArray("torches"));
            }

            if (volatileData.containsKey("buyback")) {
               island.initBuyback(volatileData.getSFSObject("buyback"));
            }

            if (volatileData.containsKey("last_bred")) {
               island.initLastBred(volatileData.getSFSObject("last_bred"));
            }

            if (volatileData.containsKey("last_player_level")) {
               island.setLastPlayerLevel(volatileData.getInt("last_player_level"));
            }

            if (volatileData.containsKey("light_torch_flag")) {
               island.setLightTorchFlag(volatileData.getBool("light_torch_flag"));
            }

            if (volatileData.containsKey("island_skin_trial")) {
               int islandThemeId = volatileData.getInt("island_skin_trial");
               if (islandThemeId != 0) {
                  IslandTheme islandTheme = (IslandTheme)IslandThemeLookup.themes.get(islandThemeId);
                  if (!islandTheme.getViewInMarket() && TimedEventManager.instance().hasTimedEventNow(TimedEventType.IslandThemeAvailability, islandThemeId, islandTheme.getIsland())) {
                     island.setIslandThemeTrial(islandThemeId);
                  }
               }
            }

            if (volatileData.containsKey("costume_data")) {
               island.initCostumeState(volatileData.getSFSObject("costume_data"));
            } else {
               IslandCostumeState defaultIslandCostumeState = new IslandCostumeState();
               island.initCostumeState(defaultIslandCostumeState.toSFSObject());
            }

            if (island.isBattleIsland()) {
               if (volatileData.containsKey("battle")) {
                  ISFSObject battleIslandData = volatileData.getSFSObject("battle");
                  island.initBattleIslandState(volatileData.getSFSObject("battle"));
                  if (battleIslandData.containsKey("costume_data")) {
                     ISFSObject oldCostumeData = battleIslandData.getSFSObject("costume_data");
                     island.initCostumeState(oldCostumeData);
                  }
               } else {
                  BattleIslandState defaultBattleIslandState = new BattleIslandState();
                  island.initBattleIslandState(defaultBattleIslandState.toSFSObject());
               }
            }

            island.checkForInvalidData(p);
         }

      } catch (Exception var13) {
         String msg = String.format("Error loading island %d\n", island.getID());
         Logger.trace(var13, msg);
         this.stats.trackSFSError("COMPRESSION_ERROR", msg);
         throw new PlayerLoadingException(var13, msg);
      }
   }

   public void loadPlayerTribalIslandData(Player player, PlayerIsland island, boolean fullPlayer) throws PlayerLoadingException {
      try {
         long playerId = player.getPlayerId();
         if (island.getTribalID() != 0L) {
            String sql = "SELECT user_tribal_monsters.user_monster_id, user_tribal_monsters.monster, user_tribal_islands.* FROM user_tribal_islands LEFT JOIN user_tribal_monsters ON user_tribal_monsters.user_monster_id=user_tribal_islands.chief WHERE user_island_id = ?";
            Object[] query_params = new Object[]{island.getTribalID()};
            ISFSArray islandData = this.db.query(sql, query_params);
            int kaynaIndex = PlayerIsland.KAYNA_TRAPPED_INDEX;
            SFSArray chiefData;
            if (islandData != null && islandData.size() > 0) {
               ISFSObject islandObj = islandData.getSFSObject(0);
               if (fullPlayer && islandObj.getLong("chief") == playerId) {
                  sql = "SELECT * FROM user_tribal_requests WHERE tribe=? AND status='pending'";
                  chiefData = this.db.query(sql, query_params);
                  island.initTribalRequests(chiefData);
               }

               if (islandObj.getLong("rank") >= (long)GameSettings.getInt("USER_KAYNA_UNLOCK_LEVEL")) {
                  kaynaIndex = PlayerIsland.KAYNA_INDEX;
               }

               if (fullPlayer) {
                  island.initTribalIslandData(islandObj);
                  island.initTribalQuests(TribalQuestLookup.getValues());
                  sql = "SELECT display_name FROM users WHERE user_id=?";
                  chiefData = this.db.query(sql, new Object[]{islandObj.getLong("chief")});
                  if (chiefData != null && chiefData.size() > 0) {
                     island.addChiefData(Helpers.sanitizeName(chiefData.getSFSObject(0).getUtfString("display_name"), " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!¨\"#$&'()*+,-./:;<=>?@{}0123456789|£©¿®`~^ÀÁÂÄÇÈÉÊËÌÍÎÏÑÒÓÔÖÙÚÛÜßàáâäçèéêëìíîïñòóôöùúûü_ÆæÃãÕõАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя€₽¡"));
                  }
               }
            }

            sql = "SELECT * FROM user_tribal_monsters WHERE island = ?";
            ISFSArray islandMonsters = this.db.query(sql, query_params);
            island.initMonsters(islandMonsters);
            chiefData = new SFSArray();
            ISFSObject kayna = new SFSObject();
            kayna.putLong("user_structure_id", island.getNextStructureId());
            kayna.putLong("island", island.getID());
            kayna.putInt("structure", kaynaIndex);
            kayna.putDouble("scale", 1.0D);
            kayna.putInt("pos_x", 24);
            kayna.putInt("pos_y", 14);
            kayna.putInt("muted", 0);
            kayna.putInt("flip", 0);
            kayna.putInt("is_upgrading", 0);
            kayna.putInt("is_complete", 1);
            kayna.putInt("in_warehouse", 0);
            chiefData.addSFSObject(kayna);
            island.initStructures(chiefData);
            if (fullPlayer) {
               PlayerMonster m = island.getMonsterByID(playerId);
               if (m != null) {
                  sql = "UPDATE user_tribal_monsters SET reset=0, last_updated=NOW() WHERE user_monster_id=?";
                  query_params = new Object[]{playerId};
                  this.db.update(sql, query_params);
               }

               player.calculateEarnedStarpower();
            }
         }

      } catch (Exception var14) {
         Logger.trace(var14, "Error Loading Tribal Island");
         throw new PlayerLoadingException(var14, "Error Loading Tribal Island");
      }
   }

   public long createPlayerIsland(Player player) {
      return this.createPlayerIsland(player, 1);
   }

   public long createPlayerIsland(Player player, int islandIndex) {
      try {
         String sql = "INSERT INTO user_islands SET user=?, island=?, date_created=NOW()";
         long islandId = this.getDB().insertGetId(sql, new Object[]{player.getPlayerId(), islandIndex});
         InitialPlayerIslandData islandData = PlayerIslandFactory.CreateIslandInitialStructures(player, islandIndex, islandId);
         String islandMonsterDataAsJson = islandData.monsters == null ? "[]" : islandData.monsters.toJson();
         String islandVolatileDataAsJson = islandData.volatiles == null ? "{}" : islandData.volatiles.toJson();
         String islandStructureDataAsJson = islandData.structures.toJson();
         sql = "INSERT INTO user_island_data SET structures=?, monsters=?, volatile=?, user=?, island=?";
         Object[] arguments = new Object[]{islandStructureDataAsJson, islandMonsterDataAsJson, islandVolatileDataAsJson, player.getPlayerId(), islandId};
         this.getDB().update(sql, arguments);
         return islandId;
      } catch (Exception var11) {
         Logger.trace(var11);
         return -1L;
      }
   }

   public long createTribalIsland(Player player, String name) {
      try {
         String sql = "INSERT INTO user_tribal_islands SET chief=?, name=?, date_created=NOW(), last_updated=NOW()";
         long tribeId = this.getDB().insertGetId(sql, new Object[]{player.getPlayerId(), name});
         sql = "INSERT INTO user_islands SET user=?, island=?, tribal_id=?, date_created=NOW()";
         long islandId = this.getDB().insertGetId(sql, new Object[]{player.getPlayerId(), 9, tribeId});
         sql = "INSERT INTO user_tribal_mappings SET user=?, tribe=?, date_created=NOW()";
         this.getDB().update(sql, new Object[]{player.getPlayerId(), tribeId});
         sql = "INSERT IGNORE INTO user_tribal_starpower SET user=?";
         this.getDB().update(sql, new Object[]{player.getPlayerId()});
         sql = "DELETE FROM user_tribal_requests WHERE user=?";
         this.getDB().update(sql, new Object[]{player.getPlayerId()});
         sql = "INSERT INTO user_tribal_requests SET user=?, tribe=?, date_created=NOW(), last_updated=NOW() ON DUPLICATE KEY UPDATE user=?, tribe=?, monster=NULL, name=NULL, status='pending', last_updated=NOW()";
         this.getDB().update(sql, new Object[]{player.getPlayerId(), tribeId, player.getPlayerId(), tribeId});
         return islandId;
      } catch (Exception var8) {
         Logger.trace(var8);
         return -1L;
      }
   }

   public long joinTribalIsland(Player player, long tribeId) {
      try {
         String sql = "INSERT INTO user_islands SET user=?, island=?, tribal_id=?, date_created=NOW()";
         long islandId = this.getDB().insertGetId(sql, new Object[]{player.getPlayerId(), 9, tribeId});
         sql = "INSERT INTO user_tribal_mappings SET user=?, tribe=?, date_created=NOW() ON DUPLICATE KEY UPDATE tribe=?, date_created=NOW()";
         this.getDB().update(sql, new Object[]{player.getPlayerId(), tribeId, tribeId});
         sql = "INSERT IGNORE INTO user_tribal_starpower SET user=?";
         this.getDB().update(sql, new Object[]{player.getPlayerId()});
         sql = "DELETE FROM user_tribal_requests WHERE user=?";
         this.getDB().update(sql, new Object[]{player.getPlayerId()});
         return islandId;
      } catch (Exception var7) {
         Logger.trace(var7);
         return -1L;
      }
   }

   public void sendPlayerState(Player player, User recipient) {
      ISFSObject returnObj = new SFSObject();
      ISFSObject playerSFS = player.toSFSObject();
      playerSFS.putUtfString("client_tutorial_setup", player.clientTutorialSetup());

      try {
         SFSArray nonExpiredGiftTimes = player.canLightFriendTorchTimes();
         if (nonExpiredGiftTimes != null) {
            playerSFS.putSFSArray("can_gift_torch_times", nonExpiredGiftTimes);
         }
      } catch (Exception var6) {
         Logger.trace(var6, "error retrieving time_before_torch_gifting", returnObj.getDump());
      }

      player.getActiveIslandStats().islandOpened(player);
      returnObj.putSFSObject("player_object", playerSFS);
      this.send("client_keep_alive", new SFSObject(), recipient);
      this.send("gs_player", returnObj, recipient);
   }

   public void sendQuests(Player player, User recipient, boolean admin) {
      ISFSObject returnObj = new SFSObject();
      if (recipient.getPrivilegeId() == 3 && admin) {
         returnObj.putInt("admin_quest_msg", 1);
      } else {
         returnObj.putInt("admin_quest_msg", 0);
      }

      ISFSArray quests = new SFSArray();
      if (player == null) {
         Logger.trace("ERROR: sendQuests: player is null");
      } else {
         ArrayList<PlayerQuest> questList = player.getQuests();
         Iterator var7 = questList.iterator();

         while(var7.hasNext()) {
            PlayerQuest q = (PlayerQuest)var7.next();
            ISFSObject newObject = new SFSObject();
            newObject.putSFSArray("new", q.toSFS());
            quests.addSFSObject(newObject);
         }

         returnObj.putSFSArray("result", quests);

         try {
            this.send("gs_quest", returnObj, recipient);
         } catch (Exception var10) {
            Logger.trace(var10, "***************************       SEND QUESTS ERROR       ***********************************", returnObj.getDump());
         }

      }
   }

   public void sendErrorResponse(String cmd, String errorMsg, User recipient) {
      ISFSObject response = new SFSObject();
      response.putUtfString("cmd", cmd);
      response.putBool("success", false);
      if (errorMsg != null && !errorMsg.equals("")) {
         response.putUtfString("message", errorMsg);
      }

      this.send(cmd, response, recipient);
   }

   public void sendAdminMessage(String adminMessage, User recipient) {
      SFSObject message = new SFSObject();
      message.putLong("date_sent", CurrentDBTime() / 1000L);
      message.putUtfString("message", adminMessage);
      ISFSObject response = new SFSObject();
      response.putBool("success", false);
      response.putSFSObject("message", message);
      this.send("gs_admin_message", response, recipient);
   }

   public void savePlayer(User user) {
      try {
         this.savePlayer((Player)user.getProperty("player_object"));
      } catch (Exception var3) {
         Logger.trace(var3, "**********   FAILED TO SAVE PLAYER   ***************");
      }

   }

   public void savePlayer(Player player) {
      if (player != null) {
         synchronized(player) {
            if (player.saveAllowed()) {
               try {
                  if (player.isDirty()) {
                     player.doFinalizeThings();
                     String displayName = player.getDisplayName();
                     if (displayName != null && displayName.length() > 25) {
                        displayName = displayName.substring(0, 25);
                     }

                     String sql = "UPDATE users SET display_name=?,xp=?,coins=?,ethereal_currency=?,diamonds=?,food=?,level=?,active_island=?,rewards_total=?,diamonds_spent=?,last_client_version=?,fb_invite_reward=?,twitter_invite_reward=?,email_invite_reward=?,last_fb_post_reward=? WHERE user_id=?";
                     Object[] arguments = new Object[]{displayName, player.getXp(), player.getActualCoins(), player.getActualEthCurrency(), player.getActualDiamonds(), player.getActualFood(), player.getLevel(), player.getActiveIslandId(), player.getTotalSpeedups(), player.getData().getInt("diamonds_spent"), player.lastClientVersion().toString(), player.getFacebookInviteReward(), player.getTwitterInviteReward(), player.getEmailInviteReward(), new Timestamp(player.getLastFacebookPostReward()), player.getData().getInt("user_id")};
                     this.getDB().update(sql, arguments);
                     if (player.globalDataIsDirty()) {
                        String jsonGlobalData = player.globalDataSfs().toJson();
                        sql = "INSERT INTO user_global_data SET user_id=?, global_data=? ON DUPLICATE KEY UPDATE global_data=?";
                        arguments = new Object[]{player.getData().getInt("user_id"), jsonGlobalData, jsonGlobalData};
                        getInstance().getDB().update(sql, arguments);
                        player.cleanGlobalData();
                     }

                     if (player.getActualStarpower() > 0L || player.getTotalStarpower() > 0L) {
                        sql = "INSERT INTO user_tribal_starpower SET user=?, starpower=? ON DUPLICATE KEY UPDATE starpower=?";
                        arguments = new Object[]{player.getPlayerId(), player.getActualStarpower(), player.getActualStarpower()};
                        getInstance().getDB().update(sql, arguments);
                     }

                     sql = "INSERT INTO user_keys SET bbb_id=?, `keys`=?, friend_gift=? ON DUPLICATE KEY UPDATE `keys`=?, friend_gift=?";
                     getInstance().getDB().update(sql, new Object[]{player.getBbbId(), player.getActualKeys(), new Timestamp(player.getFriendGift()), player.getActualKeys(), new Timestamp(player.getFriendGift())});
                     sql = "INSERT INTO user_relics SET user_id=?, `relics`=?, daily_relic_purchase_count=?, last_relic_purchase=? ON DUPLICATE KEY UPDATE `relics`=?, daily_relic_purchase_count=?, last_relic_purchase=?";
                     Timestamp lastRelicPurch = player.getLastRelicPurchase() == null ? null : new Timestamp(player.getLastRelicPurchase());
                     getInstance().getDB().update(sql, new Object[]{player.getPlayerId(), player.getActualRelics(), player.getDailyRelicsCount(), lastRelicPurch, player.getActualRelics(), player.getDailyRelicsCount(), lastRelicPurch});
                     sql = "INSERT INTO user_video_ad_credits SET bbb_id = ?, type='EXTRA_SCRATCH', last_claimed=? ON DUPLICATE KEY UPDATE last_claimed=?";
                     Timestamp lastClaimedFreeScratch = player.getLastClaimedFreeScratchAd() == 0L ? null : new Timestamp(player.getLastClaimedFreeScratchAd());
                     getInstance().getDB().update(sql, new Object[]{player.getBbbId(), lastClaimedFreeScratch, lastClaimedFreeScratch});
                     sql = "INSERT INTO user_egg_wildcards SET user_id=?, `egg_wildcards`=? ON DUPLICATE KEY UPDATE `egg_wildcards`=?";
                     getInstance().getDB().update(sql, new Object[]{player.getPlayerId(), player.getActualEggWildcards(), player.getActualEggWildcards()});
                     player.updateSpeedUp();
                     if (player.getSpecialCollectionTimer("default") != 0L) {
                        sql = "INSERT INTO user_prizes SET user_id = ?, `default` = ?, `key` = ?, relic = ?, diamond = ?, star = ? ON DUPLICATE KEY UPDATE `default` = ?, `key` = ?, relic = ?, diamond = ?, star = ?";
                        getInstance().getDB().update(sql, new Object[]{player.getPlayerId(), new Timestamp(player.getSpecialCollectionTimer("default")), new Timestamp(player.getSpecialCollectionTimer("key")), new Timestamp(player.getSpecialCollectionTimer("relic")), new Timestamp(player.getSpecialCollectionTimer("diamond")), new Timestamp(player.getSpecialCollectionTimer("star")), new Timestamp(player.getSpecialCollectionTimer("default")), new Timestamp(player.getSpecialCollectionTimer("key")), new Timestamp(player.getSpecialCollectionTimer("relic")), new Timestamp(player.getSpecialCollectionTimer("diamond")), new Timestamp(player.getSpecialCollectionTimer("star"))});
                     }

                     this.savePlayerIslandThemes(player);
                     player.setDirty(false);
                  }

                  this.savePlayerQuests(player);
                  this.savePlayerDailyCurrencyPacks(player);
                  player.saveFlipGame();
                  if (player.getActiveIslandId() != 6L) {
                     boolean fixedMonster = false;
                     Collection<PlayerMonster> monsters = player.getActiveIsland().getMonsters();
                     Iterator var16 = monsters.iterator();

                     while(var16.hasNext()) {
                        PlayerMonster pm = (PlayerMonster)var16.next();
                        SFSObject goldIslandData = pm.getGoldIslandData();
                        if (goldIslandData != null) {
                           PlayerIsland goldIsland = player.getIslandByID(goldIslandData.getLong("island"));
                           long goldMonsterId = goldIslandData.getLong("monster");
                           if (goldIsland != null) {
                              PlayerMonster goldMonsterToFix = goldIsland.getMonsterByID(goldMonsterId);
                              if (goldMonsterToFix != null && goldMonsterToFix.getLevel() != pm.getLevel()) {
                                 goldMonsterToFix.setLevel(pm.getLevel());
                                 fixedMonster = true;
                              }
                           }
                        }
                     }

                     if (fixedMonster) {
                        this.savePlayerIsland(player, player.getIslandByIslandIndex(6), false);
                     }
                  }

                  this.savePlayerIsland(player, player.getActiveIsland(), true);
                  this.savePlayerGroups(player);
                  player.getDailyCumulativeLogin().save(this.db, player.getPlayerId());
                  player.saveInventory();
               } catch (Exception var13) {
                  this.stats.trackPlayerSaveError(player, var13.toString());
                  Logger.trace(var13, "*******   FAILED TO SAVE USER DATA   ***************");
               }
            }

         }
      }
   }

   protected void savePlayerIslandThemes(Player player) throws Exception {
      String owned = player.getIslandThemes().toJson();
      ISFSArray activeIslandThemes = new SFSArray();
      HashMap<Integer, Integer> activeThemes = player.getActiveIslandThemes();
      Iterator var5 = activeThemes.entrySet().iterator();

      while(var5.hasNext()) {
         Entry<Integer, Integer> entry = (Entry)var5.next();
         ISFSObject sfsObj = new SFSObject();
         sfsObj.putInt("i", (Integer)entry.getKey());
         sfsObj.putInt("t", (Integer)entry.getValue());
         activeIslandThemes.addSFSObject(sfsObj);
      }

      String active = activeIslandThemes.toJson();
      String sql = "INSERT INTO user_island_themes SET user_id=?, owned=?, active=? ON DUPLICATE KEY UPDATE owned=?, active=?";
      this.getDB().update(sql, new Object[]{player.getPlayerId(), owned, active, owned, active});
   }

   protected void savePlayerDailyCurrencyPacks(Player player) throws Exception {
      PlayerDailyCurrencyPack dcp = player.getPurchasedDailyCurrencyPack();
      if (dcp != null && !dcp.hasCooldownExpired()) {
         int id = dcp.getPackID();
         int daysCollected = dcp.getDaysCollected();
         int daysMissed = dcp.getDaysMissed();
         Calendar lastClaimed = (Calendar)Calendar.getInstance(TimeZone.getTimeZone("UTC")).clone();
         lastClaimed.setTimeInMillis(dcp.getLastClaimed());
         Calendar lastProcessedRefresh = (Calendar)Calendar.getInstance(TimeZone.getTimeZone("UTC")).clone();
         lastProcessedRefresh.setTimeInMillis(dcp.getLastProcessedRefresh());
         Calendar expires = (Calendar)Calendar.getInstance(TimeZone.getTimeZone("UTC")).clone();
         expires.setTimeInMillis(dcp.getExpiry());
         Calendar refreshes = (Calendar)Calendar.getInstance(TimeZone.getTimeZone("UTC")).clone();
         refreshes.setTimeInMillis(dcp.getRewardRefresh());
         String sql = "INSERT INTO user_daily_currency_packs SET user=?, pack_id=?, days_collected=?, days_missed=?, last_claimed_on=?, reward_refreshes=?, expires=?, last_processed_refresh=? ON DUPLICATE KEY UPDATE pack_id=?, days_collected=?, days_missed=?, last_claimed_on=?, reward_refreshes=?, expires=?, last_processed_refresh=?";
         this.getDB().update(sql, new Object[]{player.getPlayerId(), id, daysCollected, daysMissed, lastClaimed.getTime(), refreshes.getTime(), expires.getTime(), lastProcessedRefresh.getTime(), id, daysCollected, daysMissed, lastClaimed.getTime(), refreshes.getTime(), expires.getTime(), lastProcessedRefresh.getTime()});
         dcp.setDirty(false);
      }
   }

   protected void savePlayerQuests(Player player) throws Exception {
      StringBuilder activeQuests = new StringBuilder("[");
      Iterator var3 = player.getQuests().iterator();

      while(var3.hasNext()) {
         PlayerQuest quest = (PlayerQuest)var3.next();
         if (!quest.invalid() && quest.collected() == 0) {
            activeQuests.append(quest.getCompactJSON());
            activeQuests.append(",");
         }
      }

      if (activeQuests.charAt(activeQuests.length() - 1) == ',') {
         activeQuests.deleteCharAt(activeQuests.length() - 1);
      }

      activeQuests.append("]");
      String active = activeQuests.toString();
      JSONArray jsonCompletedQuests = new JSONArray(player.getCollectedQuests());
      String completed = jsonCompletedQuests.toString();
      active = Helpers.compressJsonDataField(active, GameSettings.get("COMPRESS_QUEST_DATA"), this);
      completed = Helpers.compressJsonDataField(completed, GameSettings.get("COMPRESS_QUEST_DATA"), this);
      String sql = "UPDATE user_quest_data SET active=?, completed=? WHERE user=?";
      Object[] args = new Object[]{active, completed, player.getPlayerId()};
      this.getDB().update(sql, args);
   }

   void savePlayerGroups(Player player) throws Exception {
      if (player.hasUnsavedGroups()) {
         String sql = "INSERT INTO user_groups VALUES";
         long pid = player.getPlayerId();
         boolean comma = false;

         for(Iterator var6 = player.getUnsavedGroups().iterator(); var6.hasNext(); comma = true) {
            int i = (Integer)var6.next();
            sql = sql + String.format("%s (%d, %d)", comma ? "," : "", pid, i);
         }

         this.getDB().update(sql);
         player.markUnsavedGroups();
      }

   }

   public void savePlayerIsland(Player player, PlayerIsland island, boolean playerLeavingIsland) throws Exception {
      long playerId = player.getPlayerId();
      String sql;
      if (island.getIndex() != 9) {
         int numTorches = island.getNumTorchesOnIsland();
         sql = "UPDATE user_islands SET warp_speed=?, num_torches=? WHERE user_island_id=?";
         Object[] args = new Object[]{island.getWarpSpeed(), numTorches, island.getID()};
         this.getDB().update(sql, args);
         SFSArray islandMonsterData = island.getIslandMonsterDataForSave();
         SFSArray islandStructureData = island.getIslandStructureData();
         SFSObject islandVolatileData = island.getIslandVolatileData();
         if (playerLeavingIsland) {
            islandVolatileData.putInt("last_player_level", player.getLevel());
         }

         String islandMonsterDataForDB = islandMonsterData.toJson();
         String islandStructureDataForDB = islandStructureData.toJson();
         String islandVolatileDataForDB = islandVolatileData.toJson();
         String compressIslandDataSetting = GameSettings.get("COMPRESS_ISLAND_DATA");
         islandMonsterDataForDB = Helpers.compressJsonDataField(islandMonsterDataForDB, compressIslandDataSetting, this);
         islandStructureDataForDB = Helpers.compressJsonDataField(islandStructureDataForDB, compressIslandDataSetting, this);
         islandVolatileDataForDB = Helpers.compressJsonDataField(islandVolatileDataForDB, compressIslandDataSetting, this);
         sql = "UPDATE user_island_data SET monsters=?, structures=?, volatile=? WHERE user=? AND island=?";
         args = new Object[]{islandMonsterDataForDB, islandStructureDataForDB, islandVolatileDataForDB, playerId, island.getID()};
         this.getDB().update(sql, args);
         if (island.isBattleIsland()) {
            player.saveBattleState();
         }

         SFSArray soldMonsterTypes = island.soldMonsterTypes();
         if (soldMonsterTypes != null) {
            sql = "INSERT INTO user_sold_monsters (user,island,monsters_sold) VALUES(?,?,?) ON DUPLICATE KEY UPDATE monsters_sold=?";
            args = new Object[]{playerId, island.getID(), soldMonsterTypes.toJson(), soldMonsterTypes.toJson()};
            this.getDB().update(sql, args);
         }

         SFSArray allPrevOwnedCostumes = island.allPrevOwnedCostumes();
         if (allPrevOwnedCostumes != null && island.getAllPrevOwnedCostumesDirty()) {
            sql = "INSERT INTO user_prev_owned_costumes (user,island,costumes_owned) VALUES(?,?,?) ON DUPLICATE KEY UPDATE costumes_owned=?";
            args = new Object[]{playerId, island.getID(), allPrevOwnedCostumes.toJson(), allPrevOwnedCostumes.toJson()};
            this.getDB().update(sql, args);
            island.setAllPrevOwnedCostumesDirty(false);
         }
      } else {
         Iterator monsters = island.getMonsters().iterator();

         while(monsters.hasNext()) {
            PlayerMonster curMonster = (PlayerMonster)monsters.next();
            String sql;
            if (island.getTribalRequests() != null) {
               sql = "UPDATE user_tribal_monsters SET pos_x=?, pos_y=?, flip=?, muted=?, volume=?, last_updated=NOW() WHERE user_monster_id=?";
               this.getDB().update(sql, new Object[]{curMonster.getXPosition(), curMonster.getYPosition(), curMonster.getFlip(), curMonster.getMuted(), curMonster.getVolume(), curMonster.getID()});
            }

            if (curMonster.getID() == playerId) {
               sql = "UPDATE user_tribal_monsters SET level=IF(reset=0, ?, 1), times_fed=IF(reset=0, ?, 0), last_updated=NOW() WHERE user_monster_id=?";
               this.getDB().update(sql, new Object[]{curMonster.getLevel(), curMonster.getTimesFed(), curMonster.getID()});
            }
         }

         sql = "UPDATE user_tribal_islands SET rank=(SELECT COALESCE(SUM(level), 0) FROM user_tribal_monsters WHERE island=?), members=(SELECT COUNT(*) FROM user_tribal_mappings WHERE tribe=?), last_updated=NOW() WHERE user_island_id=?";
         this.getDB().update(sql, new Object[]{island.getTribalID(), island.getTribalID(), island.getTribalID()});
      }

   }

   public void destroy() {
      Collection<User> users = this.getParentZone().getUserList();
      Logger.trace("user count @ destroy: " + users.size());
      Iterator var2 = users.iterator();

      while(var2.hasNext()) {
         User u = (User)var2.next();
         Player p = (Player)u.getProperty("player_object");
         if (p != null) {
            Logger.trace("Attempting to save user: " + p.getPlayerId());
            this.savePlayer(u);
         }
      }

      if (this.cw != null) {
         this.cw.shutdown();
      }

      this.cw = null;
      super.destroy();
   }

   public void processUnclaimedPurchases(User sender, GameStateHandler handler) throws Exception {
      Player p = (Player)sender.getProperty("player_object");
      int playerId = p.getData().getInt("user_id");
      IDbWrapper db = this.getDB();
      String[] platforms = new String[]{"ios", "android", "amazon", "samsung", "steam", "steam"};
      String[] purchaseTables = new String[]{"user_apple_purchases", "user_android_purchases", "user_amazon_purchases", "user_samsung_purchases", "user_steam_purchases", "user_steam_dlc"};
      String[] purchaseTableIds = new String[]{"apple_purchase_id", "android_purchase_id", "amazon_purchase_id", "samsung_purchase_id", "steam_purchase_id", "steam_dlc_id"};
      String[] purchaseTableTransactions = new String[]{"transaction_id", "order_id", "purchase_token", "purchase_token", "transaction_id", ""};
      ISFSObject response = new SFSObject();
      boolean success = false;
      List<String> purchasedItems = new ArrayList();
      List<Boolean> activateNowIslandThemes = new ArrayList();
      List<Integer> purchasedIslandThemes = new ArrayList();
      int purchasedDiamonds = 0;
      int purchasedCoins = 0;
      int purchasedFood = 0;
      int purchasedKeys = 0;
      int purchasedRelics = 0;
      int purchasedDailyCurrencyPack = 0;

      String resource;
      int currentCount;
      for(int table = 0; table < purchaseTables.length; ++table) {
         String platform = platforms[table];
         String tableName = purchaseTables[table];
         String tableId = purchaseTableIds[table];
         String transactionField = purchaseTableTransactions[table];
         String transactionFieldQuery = transactionField.isEmpty() ? "" : ", " + transactionField;
         String storeItemQuery = "SELECT " + tableId + transactionFieldQuery + ", iap_name, quantity, amount, resource, cash_value, sale_purchase, raw_data FROM " + tableName + " WHERE user=? AND claimed_on IS NULL";
         ISFSArray result = db.query(storeItemQuery, new Object[]{playerId});
         success |= result.size() != 0;

         for(int i = 0; i < result.size(); ++i) {
            String iapName = result.getSFSObject(i).getUtfString("iap_name");
            int purchaseId = result.getSFSObject(i).getInt(tableId);
            resource = result.getSFSObject(i).getUtfString("resource");
            currentCount = result.getSFSObject(i).getInt("quantity");
            int amount = result.getSFSObject(i).getInt("amount");
            int cashValue = result.getSFSObject(i).getInt("cash_value");
            String transactionId = result.getSFSObject(i).getUtfString(transactionField);
            int salePurchase = result.getSFSObject(i).getInt("sale_purchase");
            String rawData = result.getSFSObject(i).getUtfString("raw_data");
            String updateUnclaimedPurchases;
            if (iapName != null && !iapName.isEmpty()) {
               updateUnclaimedPurchases = iapName.replace("com.bbb.mysingingmonsters.", "");
               if (!purchasedItems.contains(updateUnclaimedPurchases)) {
                  purchasedItems.add(updateUnclaimedPurchases);
               }
            }

            if (resource.equalsIgnoreCase("gold")) {
               resource = "coins";
            }

            amount *= currentCount;
            if (resource.equalsIgnoreCase("coins")) {
               p.adjustCoins(sender, handler, amount);
               purchasedCoins += amount;
            } else if (resource.equalsIgnoreCase("food")) {
               p.adjustFood(sender, handler, amount);
               purchasedFood += amount;
            } else if (resource.equalsIgnoreCase("diamonds")) {
               p.adjustDiamonds(sender, handler, amount);
               purchasedDiamonds += amount;
            } else if (resource.equalsIgnoreCase("key")) {
               p.adjustKeys(sender, handler, amount);
               purchasedKeys += amount;
            } else if (resource.equalsIgnoreCase("relic")) {
               p.adjustRelics(sender, handler, amount);
               purchasedRelics += amount;
            } else if (resource.equalsIgnoreCase("island_theme")) {
               boolean activateNow = false;
               if (!p.ownsIslandTheme(amount)) {
                  IslandTheme requestedIslandTheme = (IslandTheme)IslandThemeLookup.themes.get(amount);
                  p.removeTrialTheme(requestedIslandTheme.getIsland());
                  p.addIslandTheme(amount);
                  p.getActiveIsland().initIslandThemeModifiers(p.getIslandThemes());
                  activateNow = true;
               }

               activateNowIslandThemes.add(activateNow);
               purchasedIslandThemes.add(amount);
            } else if (resource.equalsIgnoreCase("daily_currency")) {
               purchasedDailyCurrencyPack = amount;
               DailyCurrencyPack requestedCurrencyPack = DailyCurrencyPackLookup.get(amount);
               p.addPurchasedDailyCurrencyPack(requestedCurrencyPack);
            }

            updateUnclaimedPurchases = "UPDATE " + tableName + " SET claimed_on=NOW() WHERE " + tableId + "=?";
            db.update(updateUnclaimedPurchases, new Object[]{purchaseId});
            this.stats.trackPurchase(sender, platform, resource, amount, cashValue, transactionId, iapName, salePurchase, rawData);
         }
      }

      response.putBool("success", success);
      this.send("gs_process_unclaimed_purchases", response, sender);
      if (success) {
         String sql = "SELECT purchases_total, purchases_amount FROM users WHERE bbb_id=?";
         Object[] args = new Object[]{p.getBbbId()};
         ISFSArray result = db.query(sql, args);
         if (result != null && result.size() > 0) {
            int purchTotal = result.getSFSObject(0).getInt("purchases_total");
            int purchAmount = result.getSFSObject(0).getInt("purchases_amount");
            p.setInGamePurchaseCount(purchTotal);
            p.setInGamePurchaseAmountTotal(purchAmount);
         }

         this.savePlayer(p);
         response = new SFSObject();
         SFSArray responseVars = new SFSArray();
         p.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         ISFSObject purchaseVars = new SFSObject();
         purchaseVars.putInt("purchased_diamonds", purchasedDiamonds);
         purchaseVars.putInt("purchased_coins", purchasedCoins);
         purchaseVars.putInt("purchased_food", purchasedFood);
         purchaseVars.putInt("purchased_keys", purchasedKeys);
         purchaseVars.putInt("purchased_relics", purchasedRelics);
         response.putSFSObject("purchases", purchaseVars);

         for(int i = 0; i < purchasedIslandThemes.size(); ++i) {
            ISFSObject islandThemeResponse = new SFSObject();
            islandThemeResponse.putBool("success", success);
            islandThemeResponse.putInt("user_island_theme_id", (Integer)purchasedIslandThemes.get(i));
            islandThemeResponse.putBool("buy_and_activate_now", (Boolean)activateNowIslandThemes.get(i));
            islandThemeResponse.putInt("island", ((IslandTheme)IslandThemeLookup.themes.get(purchasedIslandThemes.get(i))).getIsland());
            this.send("gs_activate_island_theme", islandThemeResponse, sender);
         }

         if (purchasedDailyCurrencyPack != 0) {
            ISFSObject dailyCurrencyPackResponse = new SFSObject();
            dailyCurrencyPackResponse.putBool("success", success);
            dailyCurrencyPackResponse.putSFSObject("daily_currency_pack", p.getPurchasedDailyCurrencyPack().toSFSObject());
            this.send("gs_activate_daily_currency_pack", dailyCurrencyPackResponse, sender);
         }

         ISFSArray promotionUpdates = new SFSArray();
         PlayerTimedEvents playerTimedEvents = p.getTimedEvents();
         Iterator var54 = playerTimedEvents.getEvents().iterator();

         while(var54.hasNext()) {
            TimedEvent te = (TimedEvent)var54.next();
            if (te.getEventType() == TimedEventType.Promo) {
               PromoEvent promoEvent = (PromoEvent)te;
               if (promoEvent.currentlyActive()) {
                  ISFSObject promoData = promoEvent.getPromo().getData();
                  if (promoData.containsKey("name")) {
                     resource = promoData.getUtfString("name");
                     if (purchasedItems.contains(resource)) {
                        currentCount = te.getCount();
                        te.setCount(currentCount + 1);
                        promotionUpdates.addSFSObject(te.getSfsObject());
                     }
                  }
               }
            }
         }

         playerTimedEvents.save(this.getDB());
         if (promotionUpdates.size() > 0) {
            ISFSObject timedEventsUpdate = new SFSObject();
            timedEventsUpdate.putSFSArray("timed_events", promotionUpdates);
            responseVars.addSFSObject(timedEventsUpdate);
         }

         this.send("gs_update_properties", response, sender);
      }

   }

   public void updateTapjoyUserTags(User sender, Collection<String> addTags, Collection<String> removeTags) {
      if (addTags != null || removeTags != null) {
         ISFSObject data = new SFSObject();
         if (addTags != null) {
            data.putUtfStringArray("tj_add_tags", addTags);
         }

         if (removeTags != null) {
            data.putUtfStringArray("tj_remove_tags", removeTags);
         }

         this.send("gs_update_user_tags", data, sender);
      }

   }

   public void processUncollectedOfferRewards(User sender, GameStateHandler handler, ISFSObject params) throws Exception {
      ISFSArray items = this.processPlayerReferrals(sender, handler);
      Player p = (Player)sender.getProperty("player_object");
      long bbbId = p.getBbbId();
      String[] paidSources = new String[]{"w3i", "tapjoy", "trialpay", "tapjoy_video", "supersonic", "unityads", "fyber", "fyber_offerwall"};
      String[] offerSources = new String[]{"tapjoy", "fyber_offerwall"};
      int numOffers = 0;
      boolean notificationOnFail = false;
      if (params.containsKey("notificationOnFail")) {
         notificationOnFail = params.getBool("notificationOnFail");
      }

      String storeItemQuery = "SELECT offer_reward_id, source, transaction_id, amount, resource FROM user_offer_rewards WHERE bbb_id=? AND claimed_on IS NULL";
      Object[] args = new Object[]{bbbId};
      ISFSArray result = this.getDB().query(storeItemQuery, args);
      ISFSObject response = new SFSObject();
      if (result.size() == 0 && items.size() == 0) {
         response.putBool("success", false);
         response.putBool("notificationOnFail", notificationOnFail);
         this.send("gs_collect_rewards", response, sender);
      } else {
         for(int i = 0; i < result.size(); ++i) {
            long rewardId = result.getSFSObject(i).getLong("offer_reward_id");
            String resource = result.getSFSObject(i).getUtfString("resource");
            String source = result.getSFSObject(i).getUtfString("source");
            String transactionId = result.getSFSObject(i).getUtfString("transaction_id");
            int amount = result.getSFSObject(i).getInt("amount");
            if (resource.equalsIgnoreCase("gold")) {
               resource = "coins";
            }

            result.getSFSObject(i).removeElement("offer_reward_id");
            result.getSFSObject(i).removeElement("transaction_id");
            items.addSFSObject(result.getSFSObject(i));
            if (resource.equalsIgnoreCase("coins")) {
               p.adjustCoins(sender, handler, amount);
            } else if (resource.equalsIgnoreCase("food")) {
               p.adjustFood(sender, handler, amount);
            } else if (resource.equalsIgnoreCase("diamonds")) {
               p.adjustDiamonds(sender, handler, amount);
            } else if (resource.equalsIgnoreCase("ethereal_currency")) {
               p.adjustEthCurrency(sender, handler, amount);
            } else if (resource.equalsIgnoreCase("starpower")) {
               p.adjustStarpower(sender, handler, (long)amount);
            } else if (resource.equalsIgnoreCase("keys")) {
               p.adjustKeys(sender, handler, amount);
            } else if (resource.equalsIgnoreCase("speedup")) {
               amount = 1;
               p.adjustSpeedUpCredit(sender, handler, amount, Player.SPEED_UP_TYPES.VIDEO);
            }

            if (Arrays.asList(paidSources).contains(source)) {
               this.stats.trackOffer(sender, source + "_reward", resource, amount, transactionId);
            }

            if (Arrays.asList(offerSources).contains(source)) {
               ++numOffers;
            }

            this.stats.trackReward(sender, source, resource, (long)amount);
            String updateUnclaimedPurchases = "UPDATE user_offer_rewards SET claimed_on=NOW() WHERE offer_reward_id=?";
            this.getDB().update(updateUnclaimedPurchases, new Object[]{rewardId});
         }

         if (numOffers > 0) {
            Firebase.reportOfferCompleted(sender, numOffers);
         }

         this.savePlayer(p);
         response.putBool("success", true);
         response.putSFSArray("items", items);
         this.send("gs_collect_rewards", response, sender);
      }

      response = new SFSObject();
      ISFSArray responseVars = new SFSArray();
      p.addPlayerPropertyData(responseVars, false);
      response.putSFSArray("properties", responseVars);
      this.send("gs_update_properties", response, sender);
   }

   protected ISFSArray processPlayerReferrals(User sender, GameStateHandler handler) throws Exception {
      Player p = (Player)sender.getProperty("player_object");
      long bbbId = p.getBbbId();
      int escrowDuration = GameSettings.getInt("REFERRAL_ESCROW_DURATION_HOURS");
      int minAccountPlaytime = GameSettings.getInt("REFERRAL_MIN_ACCOUNT_PLAYTIME_HOURS");
      String sql = "SELECT player_referral_id, resource, amount, user_bots.bbb_id AS bot_id FROM user_player_referrals LEFT JOIN user_bots ON user_player_referrals.from_bbb_id = user_bots.bbb_id LEFT JOIN users on user_player_referrals.from_bbb_id = users.bbb_id WHERE user_player_referrals.bbb_id=? AND claimed_on IS NULL AND bot_id IS NULL AND referred_on < TIMESTAMPADD(HOUR, ?, NOW()) AND referred_on < TIMESTAMPADD(HOUR, ?, last_login)";
      Object[] args = new Object[]{bbbId, -escrowDuration, -minAccountPlaytime};
      ISFSArray result = this.getDB().query(sql, args);
      sql = "UPDATE user_player_referrals SET claimed_on=NOW() WHERE player_referral_id=?";
      ISFSArray items = new SFSArray();

      for(int i = 0; i < result.size(); ++i) {
         long rewardId = result.getSFSObject(i).getLong("player_referral_id");
         String resource = result.getSFSObject(i).getUtfString("resource");
         String source = "player_referral";
         int amount = result.getSFSObject(i).getInt("amount");
         if (resource.equalsIgnoreCase("gold")) {
            resource = "coins";
         }

         result.getSFSObject(i).removeElement("player_referral_id");
         result.getSFSObject(i).putUtfString("source", source);
         items.addSFSObject(result.getSFSObject(i));
         if (resource.equalsIgnoreCase("coins")) {
            p.adjustCoins(sender, handler, amount);
         } else if (resource.equalsIgnoreCase("food")) {
            p.adjustFood(sender, handler, amount);
         } else if (resource.equalsIgnoreCase("diamonds")) {
            p.adjustDiamonds(sender, handler, amount);
         } else if (resource.equalsIgnoreCase("ethereal_currency")) {
            p.adjustEthCurrency(sender, handler, amount);
         } else if (resource.equalsIgnoreCase("starpower")) {
            p.adjustStarpower(sender, handler, (long)amount);
         } else if (resource.equalsIgnoreCase("keys")) {
            p.adjustKeys(sender, handler, amount);
         }

         this.stats.trackReward(sender, source, resource, (long)amount);
         this.getDB().update(sql, new Object[]{rewardId});
      }

      return items;
   }

   public void sendGameSettings(User user) {
      VersionInfo clientVersion = new VersionInfo((String)user.getProperty("client_version"));
      VersionInfo clientServerVersion = VersionData.Instance().getMaxServerVersionFromClientVersion(clientVersion);
      ISFSArray userGameSettings = GameSettings.getUserGameSettings(clientServerVersion);
      ISFSObject response = new SFSObject();
      if (userGameSettings.size() > 0) {
         response.putSFSArray("user_game_settings", userGameSettings);
      }

      this.send("game_settings", response, user);
      ISFSArray extraGameSettings = new SFSArray();
      extraGameSettings.addUtfString("AMBER_BOX_INVENTORY_DIAMOND_PRICE_PER_EGG");
      extraGameSettings.addUtfString("ASCEND_INVENTORY_DIAMOND_PRICE_PER_EGG");
      extraGameSettings.addUtfString("ASCEND_INVENTORY_DIAMOND_PRICE_PER_EGG_RARE");
      extraGameSettings.addUtfString("ASCEND_INVENTORY_DIAMOND_PRICE_PER_EGG_EPIC");
      extraGameSettings.addUtfString("BOX_INVENTORY_DIAMOND_PRICE_PER_MONSTER");
      extraGameSettings.addUtfString("CELESTIAL_INVENTORY_DIAMOND_PRICE_PER_EGG");
      extraGameSettings.addUtfString("EPIC_BOX_INVENTORY_DIAMOND_PRICE_PER_MONSTER");
      extraGameSettings.addUtfString("ETHEREAL_BOX_INVENTORY_DIAMOND_PRICE_PER_MONSTER");
      extraGameSettings.addUtfString("EVOLVE_INVENTORY_DIAMOND_PRICE_PER_EGG");
      extraGameSettings.addUtfString("EVOLVE_INVENTORY_DIAMOND_PRICE_PER_EGG_RARE");
      extraGameSettings.addUtfString("EVOLVE_INVENTORY_DIAMOND_PRICE_PER_EGG_EPIC");
      extraGameSettings.addUtfString("GOLD_BOX_INVENTORY_DIAMOND_PRICE_PER_MONSTER");
      extraGameSettings.addUtfString("GOLD_RARE_BOX_INVENTORY_DIAMOND_PRICE_PER_MONSTER");
      extraGameSettings.addUtfString("GOLD_EPIC_BOX_INVENTORY_DIAMOND_PRICE_PER_MONSTER");
      extraGameSettings.addUtfString("RARE_BOX_INVENTORY_DIAMOND_PRICE_PER_MONSTER");
      extraGameSettings.addUtfString("RARE_ETHEREAL_BOX_INVENTORY_DIAMOND_PRICE_PER_MONSTER");
      extraGameSettings.addUtfString("UNDERLING_INVENTORY_DIAMOND_PRICE_PER_EGG");
      extraGameSettings.addUtfString("WUBLIN_BOX_INVENTORY_DIAMOND_PRICE_PER_MONSTER");
      this.sendAdditionalUserGameSettings(user, (ISFSArray)extraGameSettings);
   }

   public void sendAdditionalUserGameSettings(User user, TutorialGroup tutGroup) {
      if (tutGroup == null) {
         ISFSArray defaultGameSettings = new SFSArray();
         String key = "MIN_PLAYER_TELEPORT_LEVEL";
         defaultGameSettings.addUtfString(key);
         this.sendAdditionalUserGameSettings(user, (ISFSArray)defaultGameSettings);
      } else {
         this.sendAdditionalUserGameSettings(user, tutGroup.getUserGameSettings());
      }

   }

   public void sendAdditionalUserGameSettings(User user, BattleHintLevelGroup group) {
      if (group != null) {
         this.sendAdditionalUserGameSettings(user, group.getUserGameSettings());
      }

   }

   public void sendAdditionalUserGameSettings(User user, ISFSArray extraGameSettings) {
      if (extraGameSettings != null) {
         ISFSArray userGameSettings = new SFSArray();

         for(int i = 0; i < extraGameSettings.size(); ++i) {
            String key = extraGameSettings.getUtfString(i);
            String value = GameSettings.get(key);
            ISFSObject item = new SFSObject();
            item.putUtfString("key", "USER_" + key);
            item.putUtfString("value", value);
            userGameSettings.addSFSObject(item);
         }

         ISFSObject response = new SFSObject();
         if (userGameSettings.size() > 0) {
            response.putSFSArray("user_game_settings", userGameSettings);
         }

         this.send("game_settings", response, user);
      }
   }

   public void updateSocialUsers(Long bbbId, String username, String loginType, boolean insertNewOnly) {
      try {
         String sql = "SELECT * FROM user_social WHERE login_type=? AND bbb_id=?";
         Object[] args = new Object[]{loginType, bbbId};
         SFSArray bbbResult = this.getDB().query(sql, args);
         if (bbbResult.size() == 0) {
            bbbResult = null;
         }

         sql = "SELECT * FROM user_social WHERE login_type=? AND username=?";
         args = new Object[]{loginType, username};
         SFSArray socialResult = this.getDB().query(sql, args);
         if (socialResult.size() == 0) {
            socialResult = null;
         }

         if (bbbResult == null && socialResult == null) {
            sql = "INSERT INTO user_social SET bbb_id=?, username=?, login_type=?";
            args = new Object[]{bbbId, username, loginType};
            this.getDB().update(sql, args);
         } else if (!insertNewOnly) {
            if (bbbResult != null && !bbbResult.getSFSObject(0).getUtfString("username").equals(username)) {
               if (socialResult != null) {
                  sql = "DELETE FROM user_social WHERE social_user_id=?";
                  args = new Object[]{socialResult.getSFSObject(0).getInt("social_user_id")};
                  this.getDB().update(sql, args);
               }

               sql = "UPDATE user_social SET username=? WHERE social_user_id=?";
               args = new Object[]{username, bbbResult.getSFSObject(0).getInt("social_user_id")};
               this.getDB().update(sql, args);
            } else if (socialResult != null && !socialResult.getSFSObject(0).getLong("bbb_id").equals(bbbId)) {
               if (bbbResult != null) {
                  sql = "DELETE FROM user_social WHERE social_user_id=?";
                  args = new Object[]{bbbResult.getSFSObject(0).getInt("social_user_id")};
                  this.getDB().update(sql, args);
               }

               sql = "UPDATE user_social SET bbb_id=? WHERE social_user_id=?";
               args = new Object[]{bbbId, socialResult.getSFSObject(0).getInt("social_user_id")};
               this.getDB().update(sql, args);
            }
         }
      } catch (Exception var9) {
         Logger.trace(var9, " ***** Exception trying to update social users ***** ");
      }

   }

   public void forwardUserEvent(User sender, String eventType, Collection<String> events) {
      ISFSObject response = new SFSObject();
      response.putUtfStringArray(eventType, events);
      this.send("gs_forward_events", response, sender);
   }

   protected long getUserIdByBBBId(long bbbId) throws UserNotFoundException {
      try {
         String sql = "SELECT user_id FROM users WHERE bbb_id=?";
         Object[] args = new Object[]{bbbId};
         SFSArray result = this.getDB().query(sql, args);
         return SFSHelpers.getLong("user_id", result.getSFSObject(0));
      } catch (Exception var6) {
         throw new UserNotFoundException();
      }
   }

   private void initGeo() {
      try {
         if (GameSettings.get("ENABLE_GEO_IP_LOCATE", false)) {
            Geo.init();
         }
      } catch (Exception var2) {
         Logger.trace(var2);
      }

   }

   private void initMail() {
      MailLookup.init();
      int mailInterval = 5;

      try {
         mailInterval = GameSettings.getInt("MAIL_UPDATE_INTERVAL");
      } catch (Exception var3) {
         Logger.trace(var3, "Unable to get MAIL_UPDATE_INTERVAL ... using default value of " + mailInterval);
      }

      SmartFoxServer.getInstance().getTaskScheduler().scheduleAtFixedRate(new MailUpdateTask(), 0, mailInterval, TimeUnit.MINUTES);
      int checkUserMailInterval = GameSettings.get("CHECK_USER_MAIL_TASK_MINUTES", 1);
      if (checkUserMailInterval > 0) {
         SmartFoxServer.getInstance().getTaskScheduler().scheduleAtFixedRate(new CheckUserMailTask(), 0, checkUserMailInterval, TimeUnit.MINUTES);
      }

   }

   private void initSynthesizingSettings() {
      try {
         JSONArray costs = new JSONArray(GameSettings.get("USER_SYNTHESIZER_GENE_COSTS", "[1000,2500,5000]"));
         JSONArray numPerGenes = new JSONArray(GameSettings.get("USER_SYNTHESIZER_REQUIRED_GENES", "[1,3,3]"));
         JSONArray basePercentages = new JSONArray(GameSettings.get("SYNTHESIZER_BASE_PERCENTAGES", "[1.0,0.25,0.10]"));
         JSONArray failDurations = new JSONArray(GameSettings.get("SYNTHESIZER_FAIL_DURATIONS", "[10,17,23]"));
         if (costs.length() != numPerGenes.length() || numPerGenes.length() != basePercentages.length() || basePercentages.length() != failDurations.length()) {
            throw new Exception("Bad Synthesizer data");
         }

         SynthesizerSettings.init(costs, numPerGenes, basePercentages, failDurations);
      } catch (Exception var5) {
         Logger.trace(var5);
      }

   }

   private void initDailyLoginRewards() {
      String dailyLoginRewardLvlCutoffStr = GameSettings.get("DAILY_LOGIN_STAGE_LVLS");
      String[] tokens = dailyLoginRewardLvlCutoffStr.split("[ ,]+");
      this.dailyLoginLvlCutoff = new ArrayList();

      for(int i = 0; i < tokens.length; ++i) {
         this.dailyLoginLvlCutoff.add(Integer.parseInt(tokens[i]));
      }

      String dailyLoginStagesStr = GameSettings.get("DAILY_LOGIN_STAGES");
      tokens = dailyLoginStagesStr.split("[ ,]+");
      this.dailyLoginStages = new ArrayList();

      for(int i = 0; i < tokens.length; ++i) {
         this.dailyLoginStages.add(tokens[i]);
      }

      String dailyLoginScaleStr = GameSettings.get("DAILY_LOGIN_START_SCALE_STAGE");
      this.dailyLoginScaleStartStage = Integer.parseInt(dailyLoginScaleStr);
      this.dailyLoginTypes = new ArrayList();
      this.dailyLoginAmounts = new ArrayList();
      this.dailyLoginBonusEntities = new ArrayList();

      int stageInd;
      for(stageInd = 0; stageInd < this.dailyLoginStages.size(); ++stageInd) {
         String dailyLoginRewardStr = GameSettings.get((String)this.dailyLoginStages.get(stageInd));
         List<Player.CurrencyType> dailyLoginType = new ArrayList();
         List<Integer> dailyLoginAmount = new ArrayList();
         ArrayList dailyLoginBonusEntity = new ArrayList();

         try {
            JSONArray dailyLoginReward = new JSONArray(dailyLoginRewardStr);
            if (dailyLoginReward != null) {
               for(int i = 0; i < dailyLoginReward.length(); ++i) {
                  JSONObject e = dailyLoginReward.getJSONObject(i);
                  String type = e.getString("type");
                  Integer amt = e.getInt("amount");
                  Integer bonusEntity = null;
                  if (e.has("bonus_entity")) {
                     bonusEntity = e.getInt("bonus_entity");
                  }

                  Player.CurrencyType cType = Player.getCurrencyTypeFromString(type);
                  if (cType == Player.CurrencyType.Undefined && !type.equals("none")) {
                     throw new Exception("Invalid type string in game_setting " + (String)this.dailyLoginStages.get(stageInd) + ": " + type);
                  }

                  dailyLoginType.add(cType);
                  dailyLoginAmount.add(amt);
                  dailyLoginBonusEntity.add(bonusEntity);
               }
            }
         } catch (Exception var18) {
            Logger.trace(var18);
         }

         this.dailyLoginTypes.add(dailyLoginType);
         this.dailyLoginAmounts.add(dailyLoginAmount);
         this.dailyLoginBonusEntities.add(dailyLoginBonusEntity);
      }

      try {
         stageInd = -1;

         for(int i = 0; i < this.dailyLoginTypes.size(); ++i) {
            if (stageInd == -1) {
               stageInd = ((List)this.dailyLoginTypes.get(i)).size();
            } else if (((List)this.dailyLoginTypes.get(i)).size() != stageInd) {
               throw new Exception("Number of daily logins don't match each other: dailyLoginTypes[" + i + "].size() != numTypes: " + ((List)this.dailyLoginTypes.get(i)).size() + " != " + stageInd);
            }
         }

         if (this.dailyLoginLvlCutoff.size() + 1 != this.dailyLoginTypes.size()) {
            throw new Exception("Number of daily logins reward stages doesn't match with number of daily login cutoff levels: dailyLoginLvlCutoff.size() != dailyLoginTypes.size(): " + this.dailyLoginLvlCutoff.size() + " != " + this.dailyLoginTypes.size());
         }
      } catch (Exception var17) {
         Logger.trace(var17);
      }

   }

   private void initAuthTasks() {
      int authServerStatsInterval = 60;

      try {
         authServerStatsInterval = GameSettings.getInt("AUTH_SERVER_STATS_INTERVAL");
      } catch (NumberFormatException var5) {
         Logger.trace("Unable to get SERVER_STATS_INTERVAL ... using default value " + authServerStatsInterval);
      }

      if (authServerStatsInterval > 0) {
         MSMServerStatusTask.schedule(authServerStatsInterval);
      } else {
         Logger.trace("Disabling session stats ...");
      }

      int authTokenUpdateInterval = 900;

      try {
         authTokenUpdateInterval = GameSettings.getInt("AUTH2_TOKEN_UPDATE_INTERVAL");
      } catch (NumberFormatException var4) {
         Logger.trace("Unable to get AUTH2_TOKEN_UPDATE_INTERVAL ... using default value " + authServerStatsInterval);
      }

      SmartFoxServer.getInstance().getTaskScheduler().scheduleAtFixedRate(new AuthTokenUpdateTask(), 0, authTokenUpdateInterval, TimeUnit.SECONDS);
   }

   void initGameSettings(IDbWrapper db) {
      GameSettings.init();

      String LOAD_SETTINGS_SQL;
      try {
         Logger.trace("\n\n================> Loading GameSettings from Deployment Database <================\n");
         LOAD_SETTINGS_SQL = "SELECT * FROM game_settings";
         GameSettings.loadFromDB(db, "SELECT * FROM game_settings", true);
      } catch (Exception var5) {
         Logger.trace(var5, "* FAILED TO LOAD BASE (SERVER) SETTINGS *");
      }

      this.staticDataDB = StaticDataSqlLoader.getStaticDB();
      if (this.staticDataDB != db) {
         try {
            Logger.trace("\n\n================> Loading GameSettings from Static Data Database <================\n");
            LOAD_SETTINGS_SQL = "SELECT * FROM game_settings";
            GameSettings.loadFromDB(this.staticDataDB, "SELECT * FROM game_settings", false);
         } catch (Exception var4) {
            Logger.trace(var4, "* FAILED TO LOAD STATIC GAME SETTINGS *");
         }
      }

      try {
         Logger.trace("\n\n================> Loading GameSettings from Deployment Database Override Table <================\n");
         LOAD_SETTINGS_SQL = "SELECT * FROM game_settings_override";
         GameSettings.loadFromDB(db, "SELECT * FROM game_settings_override", true);
      } catch (Exception var3) {
         Logger.trace(var3, "* FAILED TO LOAD GAME SETTINGS OVERRIDES*");
      }

   }

   public void multiSend(String cmdName, ISFSObject params, User recipient, String paramSplitName) {
      this.multiSend(cmdName, params, recipient, paramSplitName, GameSettings.get("DB_ENTRIES_PER_MESSAGE", -1));
   }

   public void multiSend(String cmdName, ISFSObject params, User recipient, String paramSplitName, int maxEntries) {
      ISFSArray data = params.getSFSArray(paramSplitName);
      if (maxEntries > 0 && data != null && data.size() > maxEntries) {
         int chunk = 1;
         int numChunks = (data.size() + maxEntries - 1) / maxEntries;
         ISFSArray chunkedData = new SFSArray();

         for(int i = 0; i < data.size(); ++i) {
            chunkedData.addSFSObject(data.getSFSObject(i));
            if (chunkedData.size() == maxEntries) {
               params.putInt("chunk", chunk);
               params.putInt("numChunks", numChunks);
               params.putSFSArray(paramSplitName, chunkedData);
               this.send(cmdName, params, recipient);
               chunkedData = new SFSArray();
               ++chunk;
            }
         }

         if (chunkedData.size() > 0) {
            params.putInt("chunk", chunk);
            params.putInt("numChunks", numChunks);
            params.putSFSArray(paramSplitName, chunkedData);
            this.send(cmdName, params, recipient);
         }

      } else {
         this.send(cmdName, params, recipient);
      }
   }
}
