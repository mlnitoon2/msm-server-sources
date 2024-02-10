package com.bigbluebubble.mysingingmonsters.task;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.bigbluebubble.BBBServer.BBBServerExtension;
import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.BatchingSNSWorker;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.data.Monster;
import com.bigbluebubble.mysingingmonsters.data.MonsterLookup;
import com.bigbluebubble.mysingingmonsters.data.MsmSession;
import com.bigbluebubble.mysingingmonsters.logging.MetricCost;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.bigbluebubble.mysingingmonsters.player.PlayerMonster;
import com.smartfoxserver.bitswarm.controllers.IController;
import com.smartfoxserver.bitswarm.controllers.IControllerManager;
import com.smartfoxserver.bitswarm.core.BitSwarmEngine;
import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MSMStats {
   private BBBServerExtension _ext;
   private BatchingSNSWorker _snsWorker;
   private String _serverHostname;
   private int _installationId;
   private boolean _trackSessionLength;
   private List<Long> _ignoredBbbIds;
   private final String NEW_USER = "new_user";
   private final String SESSION = "session";
   private final String PURCHASE_REDEEM = "purchase_redeem";
   private final String OFFER_REDEEM = "offer_redeem";
   private final String MONSTER_BUY = "monster_buy";
   private final String MONSTER_SELL = "monster_sell";
   private final String MONSTER_BREED = "monster_breed";
   private final String MONSTER_BREED_COLLECT = "monster_breed_collect";
   private final String STRUCTURE_BUY = "structure_buy";
   private final String STRUCTURE_SELL = "structure_sell";
   private final String ISLAND_UNLOCK = "island_unlock";
   private final String LEVEL_UP = "level_up";
   private final String TORCH_GIFT = "torch_gift";
   private final String AD_ACTION = "ad_action";
   private final String SFS_ERROR = "sfs_error";
   private final String USER_SFS_ERROR = "user_sfs_error";
   private final String ACTIVE_ISLAND = "active_island";
   private final String PLAYER_SAVE_ERROR = "player_save_error";
   private final String SCRATCH_TICKET_COLLECT = "scratch_ticket_collect";
   private final String SCRATCH_TICKET_PLAY = "scratch_ticket_play";
   private final String FLIP_GAME_COLLECT = "flip_game_collect";
   private final String DAILY_LOGIN_REWARD_COLLECT = "daily_login_reward_collect";
   private final String SPEEDUP_WITH_VIDEO = "speedup_with_video";
   private final String ADDED_TO_GROUP = "added_to_group";
   private final String START_BAKING = "start_baking";
   private final String MONSTER_LEVEL_UP = "monster_level_up";
   private final String MONSTER_COLLECT_SPECIAL = "monster_collect_special";
   private final String ADD_FRIEND = "add_friend";
   private final String BOX_ADD_MONSTER = "box_add_monster";
   private final String FILL_BOX_MONSTER = "fill_box_monster";
   private final String EARLY_BOX_MONSTER_ACTIVATION = "early_box_monster_activation";
   private final String PLAYER_REFERRAL = "player_referral";
   private final String STRUCTURE_UPGRADE_START = "structure_upgrade_start";
   private final String STRUCTURE_UPGRADE_COMPLETE = "structure_upgrade_complete";
   private final String ISLAND_THEME = "island_theme";
   private final String PROMO_START = "promo_start";
   private final String PROMO_COMPLETE = "promo_complete";
   private final String SEND_MONSTER_TO_GOLD = "send_monster_to_gold";
   private final String RENAME_MONSTER = "rename_monster";
   private final String COLLECT_ALL = "collect_all";
   private final String TELEPORT_MONSTER = "teleport_monster";
   private final String START_BATTLE = "batt_start_battle";
   private final String SEND_MONSTER_TO_BATTLE = "batt_send_monster_to_battle";
   private final String START_TRAINING = "batt_start_training";
   private final String FINISH_TRAINING = "batt_finish_training";
   private final String FINISH_BATTLE = "batt_finish_battle";
   private final String PURCHASE_COSTUME = "batt_purchase_costume";
   private final String EQUIP_COSTUME = "batt_equip_cosume";
   private final String SET_BATTLE_MUSIC = "batt_set_battle_music";
   private final String PURCHASE_CAMPAIGN_REWARD = "batt_purchase_campaign_reward";
   private final String EARNED_BATTLE_TROPHY = "batt_earned_trophy";
   private final String FINISH_FRIEND_BATTLE = "batt_finish_friend_battle";
   private final String CLAIM_VERSUS_REWARD = "batt_claim_versus_reward";
   private final String LOGIN_CALENDAR_COLLECT = "login_calendar_collect";
   private final String LOGIN_CALENDAR_CATCHUP = "login_calendar_catchup";
   private final String SNITCH = "snitch";
   private final String ATTUNE_START = "attune_start";
   private final String ATTUNE_COMPLETE = "attune_complete";
   private final String SYNTHESIZE_START = "synthesize_start";
   private final String SYNTHESIZE_COMPLETE = "synthesize_complete";
   private final String SYNTHESIZE_FAIL = "synthesize_fail";
   private final String SERVER_STATUS = "server_status";
   private final String TRIBAL_REQUEST = "tribal_request";
   private final String TRIBAL_CREATE = "tribal_create";
   private final String TRIBAL_JOIN = "tribal_join";
   private final String TRIBAL_QUIT = "tribal_quit";
   private final String TRIBAL_KICK = "tribal_kick";
   private final String TRIBAL_MONSTER_PLACED = "tribal_monster_placed";
   private final String TRIBAL_CANCEL = "tribal_cancel";
   private final String COMPOSER_DELETE_TEMPLATE = "composer_delete_template";
   private final String COMPOSER_SAVE_TEMPLATE = "composer_save_template";
   private final String COMPOSER_SAVE_TRACK = "composer_save_track";
   private final String CRUCIBLE_START_EVOLVE = "crucible_start_evolve";
   private final String CRUCIBLE_COLLECT_EVOLVE = "crucible_collect_evolve";
   private final String CRUCIBLE_REMOVE_HEAT = "crucible_remove_heat";
   private final String CRUCIBLE_UNLOCK = "crucible_unlock";
   private final String ISLAND_VISIT = "island_visit";
   public static final String FACEBOOK_HELP_REQUEST = "facebook_help_request";
   public static final String FACEBOOK_HELP_COLLECT = "facebook_help_collect";
   public static final String FACEBOOK_HELP_SEND = "facebook_help_send";
   public static final String ACTION_BUY = "buy";
   public static final String ACTION_SELL = "sell";

   public MSMStats(BBBServerExtension ext, BasicAWSCredentials credentials) {
      this._ext = ext;
      String batchingEndpoint = GameSettings.get("METRIC_BATCH_ENDPOINT");
      String emergencyS3Bucket = GameSettings.get("METRIC_EMERGENCY_DUMP_BUCKET");
      String emergencyS3Prefix = GameSettings.get("METRIC_EMERGENCY_DUMP_PREFIX");
      if (batchingEndpoint != null && !batchingEndpoint.isEmpty()) {
         Logger.trace("Sending batched metric events to arn: " + batchingEndpoint);

         try {
            this._snsWorker = new BatchingSNSWorker(this._ext, Region.getRegion(Regions.US_EAST_1), batchingEndpoint, emergencyS3Bucket, emergencyS3Prefix, 5, credentials);
         } catch (NullPointerException var15) {
            Logger.trace((Exception)var15, "Unable to init BatchingSNSWorker");
         }
      }

      this._serverHostname = this._ext.getServerHostname() != null ? this._ext.getServerHostname() : "unknown";
      this._installationId = 0;
      this._trackSessionLength = false;

      try {
         this._installationId = GameSettings.getInt("INSTALLATION_ID");
      } catch (NumberFormatException var14) {
         Logger.trace("Unable to get INSTALLATION_ID ... using default value " + this._installationId);
      }

      try {
         this._trackSessionLength = GameSettings.get("SESSION_LENGTH_STATS_ENABLED", false);
      } catch (NumberFormatException var13) {
         Logger.trace("Unable to get SESSION_LENGTH_STATS_ENABLED ... using default value " + this._trackSessionLength);
      }

      this._ignoredBbbIds = new ArrayList();
      String[] var6 = GameSettings.get("ALARM_BBB_IDS").split(",");
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         String s = var6[var8];

         try {
            this._ignoredBbbIds.add(Long.parseLong(s));
         } catch (NumberFormatException var12) {
            Logger.trace((Exception)var12);
         }
      }

      int serverStatsInterval = 0;

      try {
         serverStatsInterval = GameSettings.getInt("SERVER_STATS_INTERVAL");
      } catch (NumberFormatException var11) {
         Logger.trace((Exception)var11, "Unable to get SERVER_STATS_INTERVAL ... using default value " + serverStatsInterval);
      }

      if (serverStatsInterval > 0) {
         SmartFoxServer.getInstance().getTaskScheduler().scheduleAtFixedRate(new MSMStats.ServerStatsTask(), 10, serverStatsInterval, TimeUnit.SECONDS);
      } else {
         Logger.trace("Disabling server stats ...");
      }

   }

   public void trackNewUser(User sender) {
      JSONObject postParams = new JSONObject();
      this.sendUserEvent(sender, "new_user", postParams);
   }

   public void trackSession(User sender, MsmSession sessionData) {
      if (!sessionData.clientVersion.equals("9.9.9")) {
         JSONObject postParams = new JSONObject();

         try {
            postParams.put("event", "session");
            postParams.put("user_id", sessionData.userId);
            postParams.put("server_id", sessionData.gameServerId);
            postParams.put("starpower", sessionData.starpower);
            postParams.put("diamonds", sessionData.diamonds);
            postParams.put("coins", sessionData.coins);
            postParams.put("eth_currency", sessionData.ethCurrency);
            postParams.put("session_length", sessionData.sessionLength);
            postParams.put("device_model", sessionData.clientDevice);
            postParams.put("food", sessionData.food);
            postParams.put("relics", sessionData.relics);
            postParams.put("keys", sessionData.keys);
            postParams.put("client_lang", sessionData.clientLang);
            this.sendUserEvent(sender, "session", postParams);
         } catch (JSONException var5) {
            Logger.trace((Exception)var5);
         }

      }
   }

   public void trackPurchase(User sender, String storePlatform, String resource, Integer amount, Integer cashValue, String transactionId, String iapName, int salePurchase, String rawData) {
      JSONObject rawDataJson = null;

      try {
         rawDataJson = new JSONObject(StringEscapeUtils.unescapeJava(rawData));
      } catch (JSONException var14) {
         Logger.trace((Exception)var14);
      }

      JSONObject postParams = new JSONObject();

      try {
         postParams.put("resource", resource);
         postParams.put("amount", amount);
         postParams.put("revenue", cashValue);
         postParams.put("store_platform", storePlatform);
         postParams.put("transaction_id", transactionId);
         postParams.put("sale_purchase", salePurchase);
         postParams.put("product_id", iapName != null ? iapName : "unknown");
         if (rawDataJson != null) {
            postParams.put("raw_data", rawDataJson);
         } else {
            postParams.put("raw_data_string", rawData);
         }

         this.sendUserEvent(sender, "purchase_redeem", postParams);
      } catch (JSONException var13) {
         Logger.trace((Exception)var13);
      }

   }

   public void trackOffer(User sender, String source, String resource, Integer amount, String transactionId) {
      JSONObject params = new JSONObject();

      try {
         params.put("source", source);
         params.put("amount", amount);
         params.put("resource", resource);
         params.put("transaction_id", transactionId);
         this.sendUserEvent(sender, "offer_redeem", params);
      } catch (JSONException var8) {
         Logger.trace((Exception)var8);
      }

   }

   public void trackMonsterBuy(User sender, int monsterId, MetricCost cost) {
      JSONObject params = new JSONObject();

      try {
         params.put("monster_id", monsterId);
         params.put("resource", cost.getResource());
         params.put("amount", cost.getAmount());
         this.sendUserEvent(sender, "monster_buy", params);
      } catch (JSONException var6) {
         Logger.trace((Exception)var6);
      }

   }

   public void trackMonsterSell(User sender, int monsterId, int monsterLevel, MetricCost cost, ISFSObject costumeState, String monsterState) {
      JSONObject params = new JSONObject();

      try {
         params.put("monster_id", monsterId);
         params.put("monster_level", monsterLevel);
         params.put("resource", cost.getResource());
         params.put("amount", cost.getAmount());
         params.put("monster_state", monsterState);
         if (costumeState != null) {
            params.put("costume", new JSONObject(costumeState.toJson()));
         }

         this.sendUserEvent(sender, "monster_sell", params);
      } catch (JSONException var9) {
         Logger.trace((Exception)var9);
      }

   }

   public void trackMonsterBreeding(User sender, Integer parentMonster1, Integer parentMonster2, Integer childMonsterId) {
      JSONObject params = new JSONObject();

      try {
         params.put("parent_monster_1", parentMonster1.toString());
         params.put("parent_monster_2", parentMonster2.toString());
         params.put("monster_id", childMonsterId.toString());
         this.sendUserEvent(sender, "monster_breed", params);
      } catch (JSONException var7) {
         Logger.trace((Exception)var7);
      }

   }

   public void trackMonsterBreedingCollect(User sender, Integer childMonsterId, ISFSObject breedData, ISFSObject eggData) {
      JSONObject params = new JSONObject();

      try {
         params.put("monster_id", childMonsterId.toString());
         int costumeId = -1;
         if (eggData.containsKey("costume")) {
            ISFSObject costume = eggData.getSFSObject("costume");
            if (costume.containsKey("eq")) {
               costumeId = costume.getInt("eq");
            }
         }

         params.put("equipped_costume_id", costumeId);
         if (breedData.containsKey("started_on") && breedData.containsKey("complete_on")) {
            long breedTime = (long)Math.round((float)((breedData.getLong("complete_on") - breedData.getLong("started_on")) / 1000L));
            params.put("breed_seconds", breedTime);
         }

         this.sendUserEvent(sender, "monster_breed_collect", params);
      } catch (JSONException var9) {
         Logger.trace((Exception)var9);
      }

   }

   public void trackStructureBuy(User sender, int structureId, MetricCost cost) {
      JSONObject params = new JSONObject();

      try {
         params.put("structure_id", structureId);
         params.put("resource", cost.getResource());
         params.put("amount", cost.getAmount());
         this.sendUserEvent(sender, "structure_buy", params);
      } catch (JSONException var6) {
         Logger.trace((Exception)var6);
      }

   }

   public void trackStructureSell(User sender, int structureId, MetricCost cost) {
      JSONObject params = new JSONObject();

      try {
         params.put("structure_id", structureId);
         params.put("resource", cost.getResource());
         params.put("amount", cost.getAmount());
         this.sendUserEvent(sender, "structure_sell", params);
      } catch (JSONException var6) {
         Logger.trace((Exception)var6);
      }

   }

   public void trackIslandTransaction(User sender, int islandId, MetricCost cost) {
      JSONObject params = new JSONObject();

      try {
         params.put("new_island_id", islandId);
         params.put("resource", cost.getResource());
         params.put("amount", cost.getAmount());
         this.sendUserEvent(sender, "island_unlock", params);
      } catch (JSONException var6) {
         Logger.trace((Exception)var6);
      }

   }

   public void trackSpend(User sender, String action, String resource, long amount) {
      String event = resource + "_spend";
      JSONObject params = new JSONObject();

      try {
         params.put("action", action);
         params.put("resource", resource);
         params.put("amount", amount);
         this.sendUserEvent(sender, event, params);
      } catch (JSONException var9) {
         Logger.trace((Exception)var9);
      }

   }

   public void trackSpend(User sender, String action, String resource, long amount, JSONObject extra, int entityId) {
      String event = resource + "_spend";
      JSONObject params = new JSONObject();

      try {
         params.put("action", action);
         params.put("resource", resource);
         params.put("amount", amount);
         params.put("entity_id", entityId);
         if (extra != null) {
            params.put("extra", extra);
         }

         this.sendUserEvent(sender, event, params);
      } catch (JSONException var11) {
         Logger.trace((Exception)var11);
      }

   }

   public void trackReward(Player player, String source, String resource, long amount) {
      String event = resource + "_reward";
      JSONObject params = new JSONObject();

      try {
         params.put("action", source);
         params.put("resource", resource);
         params.put("amount", amount);
         this.sendPlayerEvent(player, event, params);
      } catch (JSONException var9) {
         Logger.trace((Exception)var9);
      }

   }

   public void trackReward(User sender, String source, String resource, long amount) {
      String event = resource + "_reward";
      JSONObject params = new JSONObject();

      try {
         params.put("action", source);
         params.put("resource", resource);
         params.put("amount", amount);
         this.sendUserEvent(sender, event, params);
      } catch (JSONException var9) {
         Logger.trace((Exception)var9);
      }

   }

   public void trackLevelUp(User sender, int newLevel) {
      JSONObject params = new JSONObject();

      try {
         params.put("new_level", newLevel);
         this.sendUserEvent(sender, "level_up", params);
      } catch (Exception var5) {
         Logger.trace(var5);
      }

   }

   public void trackFacebookHelpRequest(User sender, long friend_bbb_id) {
      JSONObject params = new JSONObject();

      try {
         params.put("friend_bbb_id", friend_bbb_id);
         this.sendUserEvent(sender, "facebook_help_request", params);
      } catch (JSONException var6) {
         Logger.trace((Exception)var6);
      }

   }

   public void trackFacebookHelpCollect(User sender, long friend_bbb_id, String type) {
      JSONObject params = new JSONObject();

      try {
         params.put("friend_bbb_id", friend_bbb_id);
         params.put("type", type);
         this.sendUserEvent(sender, "facebook_help_collect", params);
      } catch (JSONException var7) {
         Logger.trace((Exception)var7);
      }

   }

   public void trackFacebookHelpSend(User sender, long friend_bbb_id) {
      JSONObject params = new JSONObject();

      try {
         params.put("friend_bbb_id", friend_bbb_id);
         this.sendUserEvent(sender, "facebook_help_send", params);
      } catch (JSONException var6) {
         Logger.trace((Exception)var6);
      }

   }

   public void trackTorchGift(User sender, Long structureId, Long islandId, long friendBbbId) {
      JSONObject params = new JSONObject();

      try {
         params.put("structure_id", structureId);
         params.put("friend_bbb_id", friendBbbId);
         this.sendUserEvent(sender, "torch_gift", params);
      } catch (JSONException var8) {
         Logger.trace((Exception)var8);
      }

   }

   public void trackThirdPartyAdEvent(User sender, String action) {
      String event = "ad_action_action";
      JSONObject params = new JSONObject();

      try {
         params.put("action", action);
         this.sendUserEvent(sender, event, params);
      } catch (JSONException var6) {
         Logger.trace((Exception)var6);
      }

   }

   public void trackUserSFSError(User sender, String errorType, String errorMessage) {
      JSONObject params = new JSONObject();

      try {
         params.put("type", errorType);
         params.put("msg", errorMessage);
         this.sendUserEvent(sender, "user_sfs_error", params);
      } catch (JSONException var6) {
         Logger.trace((Exception)var6);
      }

   }

   public void trackSFSError(String errorType, String errorMessage) {
      JSONObject params = new JSONObject();

      try {
         params.put("type", errorType);
         params.put("msg", errorMessage);
         this.sendEvent("sfs_error", params);
      } catch (JSONException var5) {
         Logger.trace((Exception)var5);
      }

   }

   public void trackActiveIslandEvent(JSONObject params) {
      this.sendEvent("active_island", params);
   }

   public void trackNewTribalIsland(User sender, long tribeId, String name) {
      JSONObject params = new JSONObject();

      try {
         params.put("tribe_name", name);
         params.put("tribe_id", tribeId);
         this.sendUserEvent(sender, "tribal_create", params);
      } catch (JSONException var7) {
         Logger.trace((Exception)var7);
      }

   }

   public void trackTribalIslandRequest(User sender, long tribeId, int monsterId, String name) {
      JSONObject params = new JSONObject();

      try {
         params.put("tribe_id", tribeId);
         params.put("monster_id", monsterId);
         params.put("monster_display_name", name);
         this.sendUserEvent(sender, "tribal_request", params);
      } catch (JSONException var8) {
         Logger.trace((Exception)var8);
      }

   }

   public void trackTribalIslandQuit(User sender, long tribeId, boolean isChief) {
      JSONObject params = new JSONObject();

      try {
         params.put("tribe_id", tribeId);
         params.put("is_chief", isChief);
         this.sendUserEvent(sender, "tribal_quit", params);
      } catch (JSONException var7) {
         Logger.trace((Exception)var7);
      }

   }

   public void trackTribalIslandJoin(User sender, long tribeId) {
      JSONObject params = new JSONObject();

      try {
         params.put("tribe_id", tribeId);
         this.sendUserEvent(sender, "tribal_join", params);
      } catch (JSONException var6) {
         Logger.trace((Exception)var6);
      }

   }

   public void trackTribalReqCancel(User sender) {
      JSONObject params = new JSONObject();
      this.sendUserEvent(sender, "tribal_cancel", params);
   }

   public void trackTribalMonsterPlaced(User sender, long chiefBbbId, long memberBbbId, int memberLevel, long tribeId, int monsterId) {
      JSONObject params = new JSONObject();

      try {
         params.put("chief_bbb_id", chiefBbbId);
         params.put("member_bbb_id", memberBbbId);
         params.put("member_level", memberLevel);
         params.put("tribe_id", tribeId);
         params.put("monster_id", monsterId);
         this.sendUserEvent(sender, "tribal_monster_placed", params);
      } catch (JSONException var12) {
         Logger.trace((Exception)var12);
      }

   }

   public void trackTribalIslandKick(User sender, long memberBbbId, int memberLevel, int monsterId, int monsterLevel, long tribeId) {
      JSONObject params = new JSONObject();

      try {
         params.put("member_bbb_id", memberBbbId);
         params.put("member_level", memberLevel);
         params.put("monster_id", monsterId);
         params.put("monster_level", monsterLevel);
         params.put("tribe_id", tribeId);
         this.sendUserEvent(sender, "tribal_kick", params);
      } catch (JSONException var11) {
         Logger.trace((Exception)var11);
      }

   }

   public void trackComposerTemplateSaved(User sender, Long id, String name) {
      JSONObject params = new JSONObject();

      try {
         params.put("user_track_id", id);
         params.put("template_name", name);
         this.sendUserEvent(sender, "composer_save_template", params);
      } catch (JSONException var6) {
         Logger.trace((Exception)var6);
      }

   }

   public void trackComposerDeletedTrack(User sender, long userTrackId) {
      JSONObject params = new JSONObject();

      try {
         params.put("user_track_id", userTrackId);
         this.sendUserEvent(sender, "composer_delete_template", params);
      } catch (JSONException var6) {
         Logger.trace((Exception)var6);
      }

   }

   public void trackComposerTrackSaved(User sender, long id, int tempo, int timeNumerator, int timeDenom, int keySig, long island) {
      JSONObject params = new JSONObject();

      try {
         params.put("user_track_id", id);
         params.put("tempo", tempo);
         params.put("time_numerator", timeNumerator);
         params.put("time_denom", timeDenom);
         params.put("key_sig", keySig);
         params.put("user_island_id", island);
         this.sendUserEvent(sender, "composer_save_track", params);
      } catch (JSONException var12) {
         Logger.trace((Exception)var12);
      }

   }

   public void trackIslandVisit(User sender, long visited_bbb_id, int visited_island_id, long visited_user_island_id) {
      JSONObject params = new JSONObject();

      try {
         params.put("visited_bbb_id", visited_bbb_id);
         params.put("visited_island_id", visited_island_id);
         params.put("visited_user_island_id", visited_user_island_id);
         this.sendUserEvent(sender, "island_visit", params);
      } catch (JSONException var9) {
         Logger.trace((Exception)var9);
      }

   }

   public void trackPlayerSaveError(Player player, String err) {
      JSONObject params = new JSONObject();

      try {
         params.put("error", err);
         params.put("xp", player.getXp());
         params.put("starpower", player.getActualStarpower());
         params.put("diamonds", player.getActualDiamonds());
         params.put("coins", player.getActualCoins());
         params.put("food", player.getActualFood());
         params.put("eth_currency", player.getActualEthCurrency());
         this.sendPlayerEvent(player, "player_save_error", params);
      } catch (JSONException var5) {
         Logger.trace((Exception)var5);
      }

   }

   public void trackFlipMinigameReward(User sender, String rewardType, int rewardAmount) {
      JSONObject params = new JSONObject();

      try {
         params.put("reward_type", rewardType);
         params.put("amount", rewardAmount);
         this.sendUserEvent(sender, "flip_game_collect", params);
      } catch (JSONException var6) {
         Logger.trace((Exception)var6);
      }

   }

   public void trackScratchTicketReward(User sender, String rewardType, int rewardAmount, int isTopPrize) {
      JSONObject params = new JSONObject();

      try {
         params.put("reward_type", rewardType);
         params.put("amount", rewardAmount);
         params.put("is_top_prize", isTopPrize);
         this.sendUserEvent(sender, "scratch_ticket_collect", params);
      } catch (JSONException var7) {
         Logger.trace((Exception)var7);
      }

   }

   public void trackScratchTicketPlay(User sender, String ticketType, int isPurchase) {
      JSONObject params = new JSONObject();

      try {
         params.put("ticket_type", ticketType);
         params.put("paid", isPurchase);
         this.sendUserEvent(sender, "scratch_ticket_play", params);
      } catch (JSONException var6) {
         Logger.trace((Exception)var6);
      }

   }

   public void trackDailyLoginReward(Player player, String rewardType, int rewardDay, int rewardAmount) {
      JSONObject params = new JSONObject();

      try {
         params.put("reward_type", rewardType);
         params.put("reward_amount", rewardAmount);
         params.put("reward_day", rewardDay);
         this.sendPlayerEvent(player, "daily_login_reward_collect", params);
      } catch (JSONException var7) {
         Logger.trace((Exception)var7);
      }

   }

   public void trackSpeedupWithVideo(User sender, String action, long type, long uid, long timeRemaining, long initialTimeRemaining) {
      JSONObject params = new JSONObject();

      try {
         params.put("action", action);
         params.put("item_type", type);
         params.put("item_uid", uid);
         params.put("time_remaining", timeRemaining / 1000L);
         params.put("initial_time_remaining", initialTimeRemaining / 1000L);
         this.sendUserEvent(sender, "speedup_with_video", params);
      } catch (JSONException var13) {
         Logger.trace((Exception)var13);
      }

   }

   public void trackFriend(User sender, long friendBbbId) {
      JSONObject params = new JSONObject();

      try {
         params.put("friend_bbb_id", friendBbbId);
         this.sendUserEvent(sender, "add_friend", params);
      } catch (JSONException var6) {
         Logger.trace((Exception)var6);
      }

   }

   public void trackAddedToGroup(Player player, int groupID) {
      JSONObject params = new JSONObject();

      try {
         params.put("group_id", groupID);
         this.sendPlayerEvent(player, "added_to_group", params);
      } catch (JSONException var5) {
         Logger.trace((Exception)var5);
      }

   }

   private long getTimestamp() {
      return System.currentTimeMillis() / 1000L;
   }

   public void trackBaking(User sender, int foodItem, int structureId, long userStructureId) {
      JSONObject params = new JSONObject();

      try {
         params.put("food_id", foodItem);
         params.put("structure_id", structureId);
         params.put("user_structure_id", userStructureId);
         this.sendUserEvent(sender, "start_baking", params);
      } catch (JSONException var8) {
         Logger.trace((Exception)var8);
      }

   }

   public void trackMonsterLevelUp(User sender, int monsterId, long userMonsterId, int monsterLevel) {
      JSONObject params = new JSONObject();

      try {
         params.put("monster_level", monsterLevel);
         params.put("monster_id", monsterId);
         params.put("user_monster_id", userMonsterId);
         this.sendUserEvent(sender, "monster_level_up", params);
      } catch (JSONException var8) {
         Logger.trace((Exception)var8);
      }

   }

   public void trackMonsterCollectSpecial(User sender, String resource, int amount, int monsterId, long userMonsterId, int monsterLevel) {
      JSONObject params = new JSONObject();

      try {
         params.put("monster_level", monsterLevel);
         params.put("monster_id", monsterId);
         params.put("user_monster_id", userMonsterId);
         params.put("resource", resource);
         this.sendUserEvent(sender, "monster_collect_special", params);
      } catch (JSONException var10) {
         Logger.trace((Exception)var10);
      }

   }

   public void TrackBoxAddMonster(User sender, PlayerMonster boxMonster, int boxedMonsterId, boolean isEgg, boolean isGoldIsland) {
      Monster staticMonster = MonsterLookup.get(boxMonster.getType());
      long elapsedEggTime = Math.max((MSMExtension.CurrentDBTime() - boxMonster.eggTimerStart()) / 1000L, 0L);
      long eggTimeRemaining = (long)staticMonster.getTimeToFillSec() - elapsedEggTime;
      boolean isTimed = staticMonster.getTimeToFillSec() != -1;
      ArrayList<Integer> checkedEggs = new ArrayList();
      ISFSArray requiredEggsStatic = null;
      ISFSArray requiredEggsFlex = null;
      JSONObject params = new JSONObject();

      try {
         params.put("user_box_monster_id", boxMonster.getID());
         params.put("box_monster_id", boxMonster.getType());
         params.put("box_gene", staticMonster.getGenes());
         if (boxMonster.isInactiveBoxMonster()) {
            requiredEggsStatic = boxMonster.getBoxRequirements(isGoldIsland);
            params.put("box_state", "inactive");
            params.put("num_boxed", boxMonster.boxedEggs().size());
         } else if (MonsterLookup.get(boxMonster.getType()).isEvolvable()) {
            requiredEggsStatic = staticMonster.getEvolveReqsStatic();
            requiredEggsFlex = staticMonster.getEvolveReqsFlex();
            params.put("box_state", "evolvable");
            params.put("num_boxed", boxMonster.evolveReqsMetStatic().size() + boxMonster.evolveReqsMetFlex().size());
         }

         params.put("num_remaining", boxMonster.numReqsRemaining(isGoldIsland));
         params.put("monster_id", boxedMonsterId);
         params.put("is_timed", isTimed);
         params.put("time_remaining", eggTimeRemaining);
         params.put("is_egg", isEgg);
         JSONArray flexEggState;
         int index;
         int defId;
         int numRequired;
         int i;
         JSONObject row;
         if (requiredEggsStatic != null) {
            flexEggState = new JSONArray();
            index = 0;

            while(true) {
               if (index >= requiredEggsStatic.size()) {
                  params.put("eggs", flexEggState);
                  break;
               }

               defId = requiredEggsStatic.getInt(index);
               if (!checkedEggs.contains(defId)) {
                  numRequired = 0;

                  for(i = 0; i < requiredEggsStatic.size(); ++i) {
                     if (requiredEggsStatic.getInt(i) == defId) {
                        ++numRequired;
                     }
                  }

                  row = new JSONObject();
                  row.put("monster_id", defId);
                  row.put("has", boxMonster.hasNumOfEggStatic(defId));
                  row.put("req", numRequired);
                  flexEggState.put(row);
                  checkedEggs.add(defId);
               }

               ++index;
            }
         }

         if (requiredEggsFlex != null) {
            checkedEggs.clear();
            flexEggState = new JSONArray();
            index = 0;

            while(true) {
               if (index >= requiredEggsFlex.size()) {
                  params.put("flex_eggs", flexEggState);
                  break;
               }

               defId = requiredEggsFlex.getInt(index);
               if (!checkedEggs.contains(defId)) {
                  numRequired = 0;

                  for(i = 0; i < requiredEggsFlex.size(); ++i) {
                     if (requiredEggsFlex.getInt(i) == defId) {
                        ++numRequired;
                     }
                  }

                  row = new JSONObject();
                  row.put("def_id", defId);
                  row.put("has", boxMonster.hasNumOfEggFlex(defId));
                  row.put("req", numRequired);
                  flexEggState.put(row);
                  checkedEggs.add(defId);
               }

               ++index;
            }
         }

         this.sendUserEvent(sender, "box_add_monster", params);
      } catch (JSONException var21) {
         Logger.trace((Exception)var21);
      }

   }

   public void TrackFillBoxMonster(User sender, long userBoxMonsterId, int boxMonsterId, String genes, int numBoxed, int numToFill, int costDiamonds, int costWildcards, long eggTimeRemaining, boolean isStarted, boolean isTimed, String boxState) {
      JSONObject params = new JSONObject();

      try {
         params.put("user_box_monster_id", userBoxMonsterId);
         params.put("box_monster_id", boxMonsterId);
         params.put("box_gene", genes);
         params.put("box_state", boxState);
         params.put("num_boxed", numBoxed);
         params.put("num_to_fill", numToFill);
         params.put("diamonds_cost", costDiamonds);
         params.put("wildcards_cost", costWildcards);
         params.put("time_remaining", eggTimeRemaining);
         params.put("is_timed", isTimed);
         params.put("is_started", isStarted);
         this.sendUserEvent(sender, "fill_box_monster", params);
      } catch (JSONException var17) {
         Logger.trace((Exception)var17);
      }

   }

   public void TrackEarlyBoxMonsterActivation(User sender, PlayerMonster boxMonster, int wildcardsReceived, boolean attemptSucceeded, int keyCost, boolean isGoldIsland) {
      Monster staticMonster = MonsterLookup.get(boxMonster.getType());
      long elapsedEggTime = Math.max((MSMExtension.CurrentDBTime() - boxMonster.eggTimerStart()) / 1000L, 0L);
      long eggTimeRemaining = (long)staticMonster.getTimeToFillSec() - elapsedEggTime;
      boolean isTimed = staticMonster.getTimeToFillSec() != -1;
      JSONObject params = new JSONObject();

      try {
         params.put("user_box_monster_id", boxMonster.getID());
         params.put("box_monster_id", boxMonster.getType());
         params.put("box_gene", staticMonster.getGenes());
         if (boxMonster.isInactiveBoxMonster()) {
            params.put("box_state", "inactive");
            params.put("num_boxed", boxMonster.boxedEggs().size());
         } else if (MonsterLookup.get(boxMonster.getType()).isEvolvable()) {
            params.put("box_state", "evolvable");
            params.put("num_boxed", boxMonster.evolveReqsMetStatic().size() + boxMonster.evolveReqsMetFlex().size());
         }

         params.put("num_remaining", boxMonster.numReqsRemaining(isGoldIsland));
         params.put("is_timed", isTimed);
         params.put("time_remaining", eggTimeRemaining);
         params.put("wildcards_earned", wildcardsReceived);
         params.put("attempt_succeeded", attemptSucceeded);
         this.sendUserEvent(sender, "early_box_monster_activation", params);
      } catch (JSONException var15) {
         Logger.trace((Exception)var15);
      }

   }

   public void trackPlayerReferral(User sender, long referrerBbbId) {
      JSONObject params = new JSONObject();

      try {
         params.put("referrer_bbb_id", referrerBbbId);
         this.sendUserEvent(sender, "player_referral", params);
      } catch (JSONException var6) {
         Logger.trace((Exception)var6);
      }

   }

   public void trackStructureUpgradeStart(User sender, String structureType, int oldStructureId, int newStructureId, long userStructureId) {
      JSONObject params = new JSONObject();

      try {
         params.put("structure_type", structureType);
         params.put("old_structure_id", oldStructureId);
         params.put("new_structure_id", newStructureId);
         params.put("user_structure_id", userStructureId);
         this.sendUserEvent(sender, "structure_upgrade_start", params);
      } catch (JSONException var9) {
         Logger.trace((Exception)var9);
      }

   }

   public void trackStructureUpgradeComplete(User sender, String structureType, int newStructureId, long userStructureId) {
      JSONObject params = new JSONObject();

      try {
         params.put("structure_type", structureType);
         params.put("new_structure_id", newStructureId);
         params.put("user_structure_id", userStructureId);
         this.sendUserEvent(sender, "structure_upgrade_complete", params);
      } catch (JSONException var8) {
         Logger.trace((Exception)var8);
      }

   }

   public void trackIslandTheme(User sender, int islandId, int newIslandThemeId, boolean enabled, boolean buyAndActivate, boolean owned) {
      JSONObject params = new JSONObject();

      try {
         params.put("current_island_id", islandId);
         params.put("island_theme_id", newIslandThemeId);
         params.put("enabled", enabled);
         params.put("buy_and_activate", buyAndActivate);
         params.put("previously_owned", owned);
         this.sendUserEvent(sender, "island_theme", params);
      } catch (JSONException var9) {
         Logger.trace((Exception)var9);
      }

   }

   public void trackPromoStart(Player player, String tag, int promoId, int hours, int promoLevel) {
      JSONObject params = new JSONObject();

      try {
         params.put("promo_id", promoId);
         params.put("duration_hours", hours);
         params.put("promo_level", promoLevel);
         params.put("tag", tag);
         this.sendPlayerEvent(player, "promo_start", params);
      } catch (JSONException var8) {
         Logger.trace((Exception)var8);
      }

   }

   public void trackPromoComplete(User sender, int promoId, int promoType, long startTime, long endTime) {
      JSONObject params = new JSONObject();

      try {
         params.put("promo_id", promoId);
         params.put("promo_type", promoType);
         params.put("start_time", startTime);
         params.put("end_time", endTime);
         this.sendUserEvent(sender, "promo_complete", params);
      } catch (JSONException var10) {
         Logger.trace((Exception)var10);
      }

   }

   public void trackGoldIslandMonster(User sender, int type, int level, long userMonsterId) {
      JSONObject params = new JSONObject();

      try {
         params.put("monster_id", type);
         params.put("monster_level", level);
         params.put("user_monster_id", userMonsterId);
         this.sendUserEvent(sender, "send_monster_to_gold", params);
      } catch (JSONException var8) {
         Logger.trace((Exception)var8);
      }

   }

   public void trackTeleportMonster(User sender, JSONObject params) {
      this.sendUserEvent(sender, "teleport_monster", params);
   }

   public void trackSendMonsterToBattleIsland(User sender, int monsterId, int monsterLevel, long userMonsterId) {
      JSONObject params = new JSONObject();

      try {
         params.put("monster_id", monsterId);
         params.put("monster_level", monsterLevel);
         params.put("user_monster_id", userMonsterId);
         this.sendUserEvent(sender, "batt_send_monster_to_battle", params);
      } catch (JSONException var8) {
         Logger.trace((Exception)var8);
      }

   }

   public void trackBattleFinish(User sender, int campaignId, int battleId, ISFSObject response, boolean isPvp) {
      JSONObject params = new JSONObject();

      try {
         params.put("campaign_id", campaignId);
         params.put("battle_id", battleId);
         params.put("is_pvp", isPvp);
         params.put("response", new JSONObject(response.toJson()));
         this.sendUserEvent(sender, "batt_finish_battle", params);
      } catch (JSONException var8) {
         Logger.trace((Exception)var8);
      }

   }

   public void trackBattleStart(User sender, int campaignId, int battleId, ISFSObject response, String battleType) {
      JSONObject params = new JSONObject();

      try {
         params.put("campaign_id", campaignId);
         params.put("battle_id", battleId);
         params.put("battle_type", battleType);
         params.put("response", new JSONObject(response.toJson()));
         this.sendUserEvent(sender, "batt_start_battle", params);
      } catch (JSONException var8) {
         Logger.trace((Exception)var8);
      }

   }

   public void trackTrainingStart(User sender, int coinCost, int duration, long startTime, long endTime, PlayerMonster monster, int gymId, int numInGym, int maxCapacity) {
      JSONObject params = new JSONObject();

      try {
         params.put("coin_cost", coinCost);
         params.put("training_start_time", startTime);
         params.put("training_end_time", endTime);
         params.put("training_duration_secs", duration);
         params.put("monster_id", monster.getType());
         params.put("user_monster_id", monster.getID());
         params.put("monster_level", monster.getLevel());
         params.put("gym_id", gymId);
         params.put("monsters_in_gym", numInGym);
         params.put("max_capacity", maxCapacity);
         this.sendUserEvent(sender, "batt_start_training", params);
      } catch (JSONException var14) {
         Logger.trace((Exception)var14);
      }

   }

   public void trackTrainingFinish(User sender, int diamondCost, long secondsRemaining, PlayerMonster monster) {
      JSONObject params = new JSONObject();

      try {
         params.put("diamond_cost", diamondCost);
         params.put("seconds_remaining", secondsRemaining);
         params.put("monster_id", monster.getType());
         params.put("user_monster_id", monster.getID());
         params.put("monster_level", monster.getLevel());
         this.sendUserEvent(sender, "batt_finish_training", params);
      } catch (JSONException var8) {
         Logger.trace((Exception)var8);
      }

   }

   public void trackEquippedCostume(User sender, PlayerMonster monster, int costumeId, ISFSObject costumeState) {
      JSONObject params = new JSONObject();

      try {
         params.put("costume_id", costumeId);
         params.put("monster_id", monster.getType());
         params.put("user_monster_id", monster.getID());
         params.put("monster_level", monster.getLevel());
         params.put("costume", new JSONObject(costumeState.toJson()));
         this.sendUserEvent(sender, "batt_equip_cosume", params);
      } catch (JSONException var7) {
         Logger.trace((Exception)var7);
      }

   }

   public void trackBattleMusic(User sender, int musicId) {
      JSONObject params = new JSONObject();

      try {
         params.put("music_id", musicId);
         this.sendUserEvent(sender, "batt_set_battle_music", params);
      } catch (JSONException var5) {
         Logger.trace((Exception)var5);
      }

   }

   public void trackTrophy(User sender, int trophyId) {
      JSONObject params = new JSONObject();

      try {
         params.put("trophy_id", trophyId);
         this.sendUserEvent(sender, "batt_earned_trophy", params);
      } catch (JSONException var5) {
         Logger.trace((Exception)var5);
      }

   }

   public void trackCostumePurchase(User sender, JSONObject params) {
      this.sendUserEvent(sender, "batt_purchase_costume", params);
   }

   public void trackCampaignRewardPurchase(User sender, int campaignId, ISFSObject response) {
      JSONObject params = new JSONObject();

      try {
         params.put("campaign_id", campaignId);
         params.put("response", new JSONObject(response.toJson()));
         this.sendUserEvent(sender, "batt_purchase_campaign_reward", params);
      } catch (JSONException var6) {
         Logger.trace((Exception)var6);
      }

   }

   public void trackMonsterRename(User sender, PlayerMonster monster) {
      JSONObject params = new JSONObject();

      try {
         params.put("monster_id", monster.getType());
         params.put("user_monster_id", monster.getID());
         params.put("monster_level", monster.getLevel());
         params.put("pos_x", monster.getXPosition());
         params.put("pos_y", monster.getYPosition());
         params.put("monster_name", monster.getName());
         this.sendUserEvent(sender, "rename_monster", params);
      } catch (JSONException var5) {
         Logger.trace((Exception)var5);
      }

   }

   public void trackFriendBattle(User sender, long friendBbbId, int updatedWins, int updatedLoses, int won, int playerBattleLevel) {
      JSONObject params = new JSONObject();

      try {
         params.put("friend_bbb_id", friendBbbId);
         params.put("battles_won", updatedWins);
         params.put("battles_lost", updatedLoses);
         params.put("won", won);
         params.put("player_battle_level", playerBattleLevel);
         this.sendUserEvent(sender, "batt_finish_friend_battle", params);
      } catch (JSONException var10) {
         Logger.trace((Exception)var10);
      }

   }

   public void trackClaimVersusReward(User sender, int campaignId, long scheduleStartTime, ISFSObject response) {
      JSONObject params = new JSONObject();

      try {
         params.put("campaign_id", campaignId);
         params.put("schedule_start_time", scheduleStartTime);
         params.put("response", new JSONObject(response.toJson()));
         this.sendUserEvent(sender, "batt_claim_versus_reward", params);
      } catch (JSONException var8) {
         Logger.trace((Exception)var8);
      }

   }

   public void trackCollectAll(User sender, ISFSObject data) {
      try {
         JSONObject params = new JSONObject(data.toJson());
         this.sendUserEvent(sender, "collect_all", params);
      } catch (Exception var5) {
         Logger.trace(var5);
      }

   }

   public void trackCrucibleStartEvolve(User sender, ISFSObject metricData) {
      try {
         JSONObject params = new JSONObject(metricData.toJson());
         params.put("event_ver", "2");
         this.sendUserEvent(sender, "crucible_start_evolve", params);
      } catch (Exception var5) {
         Logger.trace(var5);
      }

   }

   public void trackCrucibleCollectEvolve(User sender, ISFSObject metricData) {
      try {
         JSONObject params = new JSONObject(metricData.toJson());
         params.put("event_ver", "2");
         this.sendUserEvent(sender, "crucible_collect_evolve", params);
      } catch (Exception var5) {
         Logger.trace(var5);
      }

   }

   public void trackCrucibleRemoveHeat(User sender, ISFSObject metricData) {
      try {
         JSONObject params = new JSONObject(metricData.toJson());
         this.sendUserEvent(sender, "crucible_remove_heat", params);
      } catch (Exception var5) {
         Logger.trace(var5);
      }

   }

   public void trackCrucibleUnlock(User sender, ISFSObject metricData) {
      try {
         JSONObject params = new JSONObject(metricData.toJson());
         this.sendUserEvent(sender, "crucible_unlock", params);
      } catch (Exception var5) {
         Logger.trace(var5);
      }

   }

   public void trackLoginCalendarCollect(User sender, ISFSObject metricData) {
      try {
         JSONObject params = new JSONObject(metricData.toJson());
         this.sendUserEvent(sender, "login_calendar_collect", params);
      } catch (Exception var5) {
         Logger.trace(var5);
      }

   }

   public void trackLoginCalendarCatchup(User sender, int fromDays, int totalDays, int diamonds, int calendarId) {
      JSONObject params = new JSONObject();

      try {
         params.put("from_days", fromDays);
         params.put("total_days", totalDays);
         params.put("diamonds", diamonds);
         params.put("calendar_id", calendarId);
         this.sendUserEvent(sender, "login_calendar_catchup", params);
      } catch (Exception var8) {
         Logger.trace(var8);
      }

   }

   public void trackSnitch(User sender, long reportedUserId, long reportedUserIslandId, String reason) {
      JSONObject params = new JSONObject();

      try {
         params.put("flagged_user_id", reportedUserId);
         params.put("flagged_user_island_id", reportedUserIslandId);
         params.put("flagged_reason", reason);
         this.sendUserEvent(sender, "snitch", params);
      } catch (Exception var9) {
         Logger.trace(var9);
      }

   }

   public void trackAttuneStart(User sender, int cost, int duration, ISFSObject attuningData) {
      JSONObject params = new JSONObject();

      try {
         params.put("eth_cost", cost);
         params.put("duration_hrs", duration);
         params.put("attuning_data", new JSONObject(attuningData.toJson()));
         this.sendUserEvent(sender, "attune_start", params);
      } catch (Exception var7) {
         Logger.trace(var7);
      }

   }

   public void trackAttuneComplete(User sender, ISFSObject attuningData) {
      JSONObject params = new JSONObject();

      try {
         params.put("attuning_data", new JSONObject(attuningData.toJson()));
         this.sendUserEvent(sender, "attune_complete", params);
      } catch (Exception var5) {
         Logger.trace(var5);
      }

   }

   public void trackSynthesizeStart(User sender, int cost, int instability, double chance, ISFSObject synthesizeData) {
      JSONObject params = new JSONObject();

      try {
         params.put("eth_cost", cost);
         params.put("instability", instability);
         params.put("chance", chance);
         params.put("syth_data", new JSONObject(synthesizeData.toJson()));
         this.sendUserEvent(sender, "synthesize_start", params);
      } catch (Exception var9) {
         Logger.trace(var9);
      }

   }

   public void trackSynthesizeComplete(User sender, ISFSObject synthesizeData) {
      JSONObject params = new JSONObject();

      try {
         params.put("syth_data", new JSONObject(synthesizeData.toJson()));
         this.sendUserEvent(sender, "synthesize_complete", params);
      } catch (Exception var5) {
         Logger.trace(var5);
      }

   }

   public void trackSynthesizeFailure(User sender, ISFSObject synthesizeData, ISFSArray reattunedCritters) {
      JSONObject params = new JSONObject();

      try {
         params.put("syth_data", new JSONObject(synthesizeData.toJson()));
         params.put("reattuned_critters", new JSONArray(reattunedCritters.toJson()));
         this.sendUserEvent(sender, "synthesize_fail", params);
      } catch (Exception var6) {
         Logger.trace(var6);
      }

   }

   public void sendUserEvent(User sender, String event, JSONObject data) {
      if (sender != null) {
         JSONObject deviceObj = new JSONObject();

         try {
            if (sender.getProperty("sess_start") != null) {
               data.put("session_start", (Long)sender.getProperty("sess_start") / 1000L);
            }
         } catch (JSONException var7) {
            Logger.trace((Exception)var7, "Exception adding session data properties to metric event.");
         }

         try {
            String platform = sender.getProperty("client_platform") != null ? (String)sender.getProperty("client_platform") : null;
            if (platform != null) {
               deviceObj.put("platform", (String)sender.getProperty("client_platform"));
            }

            if (sender.getProperty("client_version") != null) {
               deviceObj.put("client_version", (String)sender.getProperty("client_version"));
            }

            if (sender.getProperty("client_os") != null) {
               deviceObj.put("os_version", (String)sender.getProperty("client_os"));
            }

            if (sender.getProperty("ip_address") != null) {
               deviceObj.put("ip_address", sender.getProperty("ip_address"));
            }

            if (sender.getProperty("android_id") != null) {
               deviceObj.put("android_id", sender.getProperty("android_id"));
            }

            if (sender.getProperty("idfv") != null) {
               deviceObj.put("idfv", sender.getProperty("idfv"));
            }

            data.put("device", deviceObj);
         } catch (JSONException var6) {
            Logger.trace((Exception)var6, "Exception adding device data properties to metric event.");
         }

         if (sender.containsProperty("player_object")) {
            Player player = (Player)sender.getProperty("player_object");
            this.sendPlayerEvent(player, event, data);
         } else {
            this.sendEvent(event, data);
         }

      }
   }

   private void sendPlayerEvent(Player player, String event, JSONObject data) {
      int activeIslandId = 0;
      if (player.getActiveIsland() != null) {
         activeIslandId = player.getActiveIsland().getIndex();
      }

      try {
         data.put("bbb_id", player.getBbbId());
         data.put("level", player.getLevel());
         data.put("island_id", activeIslandId);
         data.put("user_created", player.getDateCreatedTime() / 1000L);
         data.put("has_iap", player.hasMadePurchase());
         data.put("diamonds_remaining", player.getActualDiamonds());
         data.put("coins_remaining", player.getActualCoins());
         if (player.isAdmin()) {
            data.put("is_admin", true);
         }
      } catch (JSONException var6) {
         Logger.trace((Exception)var6, "Exception adding common player properties to metric.");
      }

      this.sendEvent(event, data);
   }

   private void sendEvent(String event, JSONObject data) {
      if (this._snsWorker != null) {
         JSONObject rootObj = new JSONObject();

         try {
            rootObj.put("event", event);
            rootObj.put("time", this.getTimestamp());
            rootObj.put("source", "server");
            rootObj.put("data", data);
         } catch (JSONException var5) {
            Logger.trace((Exception)var5);
         }

         this._snsWorker.addMessage(rootObj.toString());
      }
   }

   public static JSONObject extraSpeedupInfo(long timeRemaining, long initialTimeRemaining, int id) {
      JSONObject json = new JSONObject();

      try {
         json.put("time_remaining", timeRemaining / 1000L);
         json.put("initial_time_remaining", initialTimeRemaining / 1000L);
         json.put("id", id);
      } catch (JSONException var7) {
         Logger.trace((Exception)var7);
      }

      return json;
   }

   public void sendPlayerGroups(Player player) {
      if (player.getGroups() != null) {
         Iterator var2 = player.getGroups().iterator();

         while(var2.hasNext()) {
            int group = (Integer)var2.next();
            this.trackAddedToGroup(player, group);
         }
      }

   }

   private class ServerStatsTask implements Runnable {
      private ServerStatsTask() {
      }

      public void run() {
         double mb = 1048576.0D;
         Runtime runtime = Runtime.getRuntime();
         JSONObject params = new JSONObject();

         try {
            params.put("server_hostname", MSMStats.this._serverHostname);
            params.put("user_count", MSMStats.this._ext.getParentZone().getUserCount());
            params.put("free_memory", (double)runtime.freeMemory() / 1048576.0D);
            params.put("total_memory", (double)runtime.totalMemory() / 1048576.0D);
            params.put("max_memory", (double)runtime.maxMemory() / 1048576.0D);
            params.put("server_id", MSMStats.this._ext.serverId);
            params.put("outgoing_message_queue_size", BitSwarmEngine.getInstance().getSocketWriter().getQueueSize());
            IControllerManager cmg = BitSwarmEngine.getInstance().getControllerManager();
            IController extCtrl = cmg.getControllerById((byte)1);
            params.put("extension_request_queue_size", extCtrl.getQueueSize());
            MSMStats.this.sendEvent("server_status", params);
         } catch (JSONException var7) {
            Logger.trace((Exception)var7);
         }

      }

      // $FF: synthetic method
      ServerStatsTask(Object x1) {
         this();
      }
   }
}
