package com.bigbluebubble.mysingingmonsters;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.BBBServer.util.Misc;
import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.battle.Battle;
import com.bigbluebubble.mysingingmonsters.battle.BattleCampaignData;
import com.bigbluebubble.mysingingmonsters.battle.BattleCampaignEventData;
import com.bigbluebubble.mysingingmonsters.battle.BattleCampaignLookup;
import com.bigbluebubble.mysingingmonsters.battle.BattleFriendPvp;
import com.bigbluebubble.mysingingmonsters.battle.BattleIslandCampaignState;
import com.bigbluebubble.mysingingmonsters.battle.BattleIslandMusicState;
import com.bigbluebubble.mysingingmonsters.battle.BattleIslandState;
import com.bigbluebubble.mysingingmonsters.battle.BattleLevelLookup;
import com.bigbluebubble.mysingingmonsters.battle.BattleLoadout;
import com.bigbluebubble.mysingingmonsters.battle.BattleMonsterActionData;
import com.bigbluebubble.mysingingmonsters.battle.BattleMonsterActionLookup;
import com.bigbluebubble.mysingingmonsters.battle.BattleMonsterStatData;
import com.bigbluebubble.mysingingmonsters.battle.BattleMonsterStatLookup;
import com.bigbluebubble.mysingingmonsters.battle.BattleMonsterTrainingLookup;
import com.bigbluebubble.mysingingmonsters.battle.BattleMusicLookup;
import com.bigbluebubble.mysingingmonsters.battle.BattleOpponentData;
import com.bigbluebubble.mysingingmonsters.battle.BattleOpponents;
import com.bigbluebubble.mysingingmonsters.battle.BattlePlayerVersusState;
import com.bigbluebubble.mysingingmonsters.battle.BattleRequirements;
import com.bigbluebubble.mysingingmonsters.battle.BattleRewardData;
import com.bigbluebubble.mysingingmonsters.battle.BattleSeasons;
import com.bigbluebubble.mysingingmonsters.battle.BattleTrophyLookup;
import com.bigbluebubble.mysingingmonsters.battle.BattleVersusChampionRanks;
import com.bigbluebubble.mysingingmonsters.battle.BattleVersusOpponent;
import com.bigbluebubble.mysingingmonsters.battle.BattleVersusTierData;
import com.bigbluebubble.mysingingmonsters.battle.verification.BattleState;
import com.bigbluebubble.mysingingmonsters.battle.verification.BattleVerification;
import com.bigbluebubble.mysingingmonsters.costumes.CostumeData;
import com.bigbluebubble.mysingingmonsters.costumes.CostumeLookup;
import com.bigbluebubble.mysingingmonsters.costumes.IslandCostumeState;
import com.bigbluebubble.mysingingmonsters.costumes.MonsterCostumeState;
import com.bigbluebubble.mysingingmonsters.costumes.PlayerCostumeState;
import com.bigbluebubble.mysingingmonsters.data.AttunerGene;
import com.bigbluebubble.mysingingmonsters.data.AttunerGeneLookup;
import com.bigbluebubble.mysingingmonsters.data.Code;
import com.bigbluebubble.mysingingmonsters.data.CodeLookup;
import com.bigbluebubble.mysingingmonsters.data.DailyCumulativeLoginLookup;
import com.bigbluebubble.mysingingmonsters.data.EntityAltCostLookup;
import com.bigbluebubble.mysingingmonsters.data.FlipBoardLookup;
import com.bigbluebubble.mysingingmonsters.data.FlipLevelLookup;
import com.bigbluebubble.mysingingmonsters.data.GeneLookup;
import com.bigbluebubble.mysingingmonsters.data.Island;
import com.bigbluebubble.mysingingmonsters.data.IslandLookup;
import com.bigbluebubble.mysingingmonsters.data.IslandTheme;
import com.bigbluebubble.mysingingmonsters.data.IslandThemeLookup;
import com.bigbluebubble.mysingingmonsters.data.LevelLookup;
import com.bigbluebubble.mysingingmonsters.data.Monster;
import com.bigbluebubble.mysingingmonsters.data.MonsterCommonToEpicMapping;
import com.bigbluebubble.mysingingmonsters.data.MonsterCommonToRareMapping;
import com.bigbluebubble.mysingingmonsters.data.MonsterFlexEggDefLookup;
import com.bigbluebubble.mysingingmonsters.data.MonsterIslandData;
import com.bigbluebubble.mysingingmonsters.data.MonsterIslandToIslandMapping;
import com.bigbluebubble.mysingingmonsters.data.MonsterLookup;
import com.bigbluebubble.mysingingmonsters.data.MonsterRareData;
import com.bigbluebubble.mysingingmonsters.data.PrizeLookup;
import com.bigbluebubble.mysingingmonsters.data.ProperNouns;
import com.bigbluebubble.mysingingmonsters.data.QuestLookup;
import com.bigbluebubble.mysingingmonsters.data.ScratchTicketFunctions;
import com.bigbluebubble.mysingingmonsters.data.StickerLookup;
import com.bigbluebubble.mysingingmonsters.data.StoreCurrencyLookup;
import com.bigbluebubble.mysingingmonsters.data.StoreGroupsLookup;
import com.bigbluebubble.mysingingmonsters.data.StoreItemsLookup;
import com.bigbluebubble.mysingingmonsters.data.StoreReplacements;
import com.bigbluebubble.mysingingmonsters.data.Structure;
import com.bigbluebubble.mysingingmonsters.data.StructureLookup;
import com.bigbluebubble.mysingingmonsters.data.SynthesizerSettings;
import com.bigbluebubble.mysingingmonsters.data.VersionData;
import com.bigbluebubble.mysingingmonsters.data.groups.CurrencyGroup;
import com.bigbluebubble.mysingingmonsters.data.groups.TutorialGroup;
import com.bigbluebubble.mysingingmonsters.data.groups.UserGroup;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventManager;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.CostumeAvailabilityEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.CostumeSalesEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.EggstravaganzaEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.EntityAvailabilityEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.EntitySalesEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.EvolveAvailabilityEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.IslandSalesEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.MegafySaleEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.PermalightTorchSaleEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.ShortenedFuzingEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.StarSalesEvent;
import com.bigbluebubble.mysingingmonsters.logging.Firebase;
import com.bigbluebubble.mysingingmonsters.logging.GooglePlayEvents;
import com.bigbluebubble.mysingingmonsters.logging.MSMCommandLogging;
import com.bigbluebubble.mysingingmonsters.logging.MetricCost;
import com.bigbluebubble.mysingingmonsters.player.InitialPlayerIslandData;
import com.bigbluebubble.mysingingmonsters.player.LitPlayerTorch;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.bigbluebubble.mysingingmonsters.player.PlayerAchievement;
import com.bigbluebubble.mysingingmonsters.player.PlayerAttuningData;
import com.bigbluebubble.mysingingmonsters.player.PlayerAvatar;
import com.bigbluebubble.mysingingmonsters.player.PlayerBaking;
import com.bigbluebubble.mysingingmonsters.player.PlayerBreeding;
import com.bigbluebubble.mysingingmonsters.player.PlayerBuffs;
import com.bigbluebubble.mysingingmonsters.player.PlayerBuyback;
import com.bigbluebubble.mysingingmonsters.player.PlayerCrucibleData;
import com.bigbluebubble.mysingingmonsters.player.PlayerDailyCumulativeLogin;
import com.bigbluebubble.mysingingmonsters.player.PlayerEgg;
import com.bigbluebubble.mysingingmonsters.player.PlayerFuzeBuddy;
import com.bigbluebubble.mysingingmonsters.player.PlayerIsland;
import com.bigbluebubble.mysingingmonsters.player.PlayerIslandFactory;
import com.bigbluebubble.mysingingmonsters.player.PlayerMonster;
import com.bigbluebubble.mysingingmonsters.player.PlayerQuest;
import com.bigbluebubble.mysingingmonsters.player.PlayerStructure;
import com.bigbluebubble.mysingingmonsters.player.PlayerSynthesizingData;
import com.bigbluebubble.mysingingmonsters.player.PlayerTimedEvents;
import com.bigbluebubble.mysingingmonsters.schedules.Schedule;
import com.bigbluebubble.mysingingmonsters.schedules.ScheduleLookup;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticData;
import com.bigbluebubble.mysingingmonsters.task.MSMStats;
import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.annotations.MultiHandler;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.util.ClientDisconnectionReason;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.Vector;
import java.util.Map.Entry;
import org.apache.commons.lang.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.owasp.esapi.ESAPI;

@MultiHandler
public class GameStateHandler extends BaseClientRequestHandler {
   MSMExtension ext;
   static Random gRandom = new Random();
   private final int BUILD_BREEDING_CAVE_QUEST_ID = 17;
   private final int COLLECT_SYNTHESIZER_FAILURE_QUEST_ID = 505;
   public static final String REQUEST_GET_BATTLE_CAMPAIGN_DATA = "db_battle";
   public static final String REQUEST_GET_BATTLE_LEVELS_DATA = "db_battle_levels";
   public static final String REQUEST_GET_BATTLE_MONSTER_TRAINING_DATA = "db_battle_monster_training";
   public static final String REQUEST_GET_BATTLE_MONSTER_ACTIONS_DATA = "db_battle_monster_actions";
   public static final String REQUEST_GET_BATTLE_MONSTER_STATS_DATA = "db_battle_monster_stats";
   public static final String REQUEST_GET_BATTLE_MUSIC_DATA = "db_battle_music";
   public static final String REQUEST_BATTLE_TELEPORT = "battle_teleport";
   public static final String REQUEST_START_BATTLE = "battle_start";
   public static final String REQUEST_START_BATTLE_VERSUS = "battle_start_versus";
   public static final String REQUEST_START_BATTLE_FRIEND = "battle_start_friend";
   public static final String REQUEST_FINISH_BATTLE = "battle_finish";
   public static final String REQUEST_START_TRAINING = "battle_start_training";
   public static final String REQUEST_FINISH_TRAINING = "battle_finish_training";
   public static final String REQUEST_PURCHASE_BATTLE_CAMPAIGN_REWARD = "battle_purchase_campaign_reward";
   public static final String REQUEST_SET_BATTLE_MUSIC = "battle_set_music";
   public static final String REQUEST_REFRESH_VERSUS_ATTEMPTS = "battle_refresh_versus_attempts";
   public static final String REQUEST_CLAIM_VERSUS_REWARDS = "battle_claim_versus_rewards";
   public static final String UPDATE_VIEWED_CAMPAIGNS = "update_viewed_campaigns";
   public static final String REQUEST_ADMIN_BATTLE_CAMPAIGN_RESET = "admin_battle_campaign_reset";
   public static final String REQUEST_SET_PLAYER_AVATAR = "gs_set_avatar";
   public static final String REQUEST_GET_COSTUME_DATA = "db_costumes";
   public static final String REQUEST_PURCHASE_COSTUME = "purchase_costume";
   public static final String REQUEST_EQUIP_COSTUME = "equip_costume";
   public static final String REQUEST_SET_AWAKENER_STATE = "update_awakener";
   public static final String REQUEST_GET_DAILY_CUMULATIVE_LOGIN_DATA = "db_daily_cumulative_login";
   public static final String REQUEST_COLLECT_DAILY_CUMULATIVE_LOGIN_REWARDS = "collect_daily_cumulative_login_rewards";
   public static final String REQUEST_START_ATTUNING = "gs_start_attuning";
   public static final String REQUEST_FINISH_ATTUNING = "gs_finish_attuning";
   public static final String REQUEST_SPEEDUP_ATTUNING = "gs_speedup_attuning";
   public static final String REQUEST_START_SYNTHESIZING = "gs_start_synthesizing";
   public static final String REQUEST_SPEEDUP_SYNTHESIZING = "gs_speedup_synthesizing";
   public static final String REQUEST_COLLECT_SYNTHESIZING_FAILURE = "gs_collect_synthesizing_failure";
   final int maxRandomTribesReturned = 20;
   static final boolean TEST_START_BATTLE_FAILURE = false;
   static final int TUTORIAL_CAMPAIGN_ID = 1;

   public void handleClientRequest(User sender, ISFSObject params) {
      try {
         this.ext = (MSMExtension)this.getParentExtension();
         if ((Long)sender.getProperty("sess_start") < MSMExtension.cache_loaded_on) {
            this.ext.savePlayer(sender);
            sender.disconnect(ClientDisconnectionReason.KICK);
         } else {
            String requestID = params.getUtfString("__[[REQUEST_ID]]__");
            MSMCommandLogging.addClientRequestLog(sender, requestID);
            Player player;
            if (requestID.equals("gs_give_me_shit")) {
               player = (Player)sender.getProperty("player_object");
               if (player != null) {
                  Logger.trace(String.format("Player %d sending invalid message '%s'", player.getPlayerId(), "gs_give_me_shit"));
               }
            } else if (requestID.equals("db_monster")) {
               this.getMonsterDB(sender, params);
            } else if (requestID.equals("db_gene")) {
               this.getGeneDB(sender, params);
            } else if (requestID.equals("db_versions")) {
               this.getVersionData(sender, params);
            } else if (requestID.equals("db_structure")) {
               this.getStructureDB(sender, params);
            } else if (requestID.equals("db_island")) {
               this.getIslandDB(sender, params);
            } else if (requestID.equals("db_level")) {
               this.getLevelDB(sender, params);
            } else if (requestID.equals("db_store")) {
               this.getStoreDB(sender, params);
            } else if (requestID.equals("db_store_v2")) {
               this.getStoreDBv2(sender, params);
            } else if (requestID.equals("db_scratch_offs")) {
               this.getScratchOffsDB(sender, params);
            } else if (requestID.equals("test_types")) {
               this.testTypes(sender, params);
            } else if (requestID.equals("gs_sticker")) {
               this.getStickerDB(sender, params);
            } else if (requestID.equals("db_attuner_gene")) {
               this.getAttunerGeneDB(sender, params);
            } else if (requestID.equals("gs_quest")) {
               this.getQuestInfo(sender, params);
            } else if (requestID.equals("gs_timed_events")) {
               this.getTimedEventInfo(sender, params);
            } else if (requestID.equals("gs_store_replacements")) {
               this.getReplacementStoreStructures(sender, params);
            } else if (requestID.equals("gs_rare_monster_data")) {
               this.getRareMonsterData(sender, params);
            } else if (requestID.equals("gs_epic_monster_data")) {
               this.getEpicMonsterData(sender, params);
            } else if (requestID.equals("db_flexeggdefs")) {
               this.getFlexEggDefs(sender, params);
            } else if (requestID.equals("gs_flip_boards")) {
               this.getFlipBoardsData(sender, params);
            } else if (requestID.equals("gs_flip_levels")) {
               this.getFlipLevelsData(sender, params);
            } else if (requestID.equals("gs_monster_island_2_island_data")) {
               this.getMonsterIsland2IslandMapData(sender, params);
            } else if (requestID.equals("gs_entity_alt_cost_data")) {
               this.getEntityAltCostData(sender, params);
            } else if (requestID.equals("gs_cant_breed")) {
               this.getCantBreeds(sender, params);
            } else if (requestID.equals("db_battle")) {
               this.getBattleCampaignDB(sender, params);
            } else if (requestID.equals("db_battle_levels")) {
               this.getBattleLevelsDB(sender, params);
            } else if (requestID.equals("db_battle_monster_training")) {
               this.getBattleMonsterTrainingDB(sender, params);
            } else if (requestID.equals("db_battle_monster_actions")) {
               this.getBattleMonsterActionsDB(sender, params);
            } else if (requestID.equals("db_battle_monster_stats")) {
               this.getBattleMonsterStatsDB(sender, params);
            } else if (requestID.equals("db_battle_music")) {
               this.getBattleMusicDB(sender, params);
            } else if (requestID.equals("db_costumes")) {
               this.getCostumeDB(sender, params);
            } else if (requestID.equals("gs_collect_daily_currency_pack")) {
               this.collectDailyCurrencyPack(sender, params);
            } else if (requestID.equals("gs_refresh_daily_currency_pack")) {
               this.refreshDailyCurrencyPack(sender, params);
            } else if (requestID.equals("db_daily_cumulative_login")) {
               this.getDailyCumulativeLoginRewardsDB(sender, params);
            } else if (!requestID.equals("keep_alive")) {
               player = (Player)sender.getProperty("player_object");
               if (player == null) {
                  Logger.trace("ERROR: Player is null when attempting to process " + requestID + ", aborting ...");
                  return;
               }

               synchronized(player) {
                  if (!sender.containsProperty("player_object")) {
                     Logger.trace("ERROR: Player is null when attempting to process " + requestID + ", aborting ...");
                     return;
                  }

                  if (requestID.equals("gs_set_displayname")) {
                     this.updateDisplayName(sender, params);
                  } else if (requestID.equals("gs_get_code")) {
                     this.redeemCode(sender, params);
                  } else if (requestID.equals("gs_transfer_code")) {
                     this.transferCode(sender, params);
                  } else if (requestID.equals("gs_set_last_timed_theme")) {
                     this.updateLastTimedTheme(sender, params);
                  } else if (requestID.equals("gs_update_island_tutorials")) {
                     this.updateIslandTutorials(sender, params);
                  } else if (requestID.equals("gs_quest_event")) {
                     this.questEvent(sender, params);
                  } else if (requestID.equals("gs_quest_read")) {
                     this.questRead(sender, params);
                  } else if (requestID.equals("gs_quests_read")) {
                     this.questsRead(sender, params);
                  } else if (requestID.equals("gs_quest_collect")) {
                     this.questCollect(sender, params);
                  } else if (requestID.equals("gs_update_achievement_status")) {
                     this.updateAchievementStatus(sender, params);
                  } else if (requestID.equals("gs_buy_island")) {
                     this.buyIsland(sender, params);
                  } else if (requestID.equals("gs_change_island")) {
                     this.changeIsland(sender, params);
                  } else if (requestID.equals("gs_place_on_gold_island")) {
                     this.placeOnGoldIsland(sender, params);
                  } else if (requestID.equals("gs_save_island_warp_speed")) {
                     this.saveIslandWarpSpeed(sender, params);
                  } else if (requestID.equals("gs_place_on_tribal")) {
                     this.placeOnTribal(sender, params);
                  } else if (requestID.equals("gs_cancel_tribe_invite")) {
                     this.cancelTribeInvite(sender, params);
                  } else if (requestID.equals("gs_decline_all_tribal_invites")) {
                     this.declineAllTribeInvites(sender, params);
                  } else if (requestID.equals("gs_cancel_tribe_request")) {
                     this.cancelTribeRequest(sender, params);
                  } else if (requestID.equals("gs_send_tribe_request")) {
                     this.sendTribeRequest(sender, params);
                  } else if (requestID.equals("gs_send_tribe_invite")) {
                     this.sendTribeInvite(sender, params);
                  } else if (requestID.equals("gs_join_tribe")) {
                     this.joinTribe(sender, params);
                  } else if (requestID.equals("gs_leave_tribe_request")) {
                     this.leaveTribeRequest(sender, params);
                  } else if (requestID.equals("gs_kick_tribe_request")) {
                     this.kickTribeRequest(sender, params);
                  } else if (requestID.equals("gs_set_tribename")) {
                     this.updateTribeName(sender, params);
                  } else if (requestID.equals("gs_set_islandname")) {
                     this.updateIslandName(sender, params);
                  } else if (requestID.equals("gs_refresh_tribe_requests")) {
                     this.refreshTribeRequests(sender, params);
                  } else if (requestID.equals("gs_activate_island_theme")) {
                     this.activateIslandTheme(sender, params);
                  } else if (requestID.equals("gs_buy_egg")) {
                     this.buyEgg(sender, params);
                  } else if (requestID.equals("gs_hacky_make_monster_without_egg")) {
                     this.adminHatchNonEgg(sender, params);
                  } else if (requestID.equals("gs_hatch_egg")) {
                     this.hatchEgg(sender, params);
                  } else if (requestID.equals("gs_sell_egg")) {
                     this.sellEgg(sender, params);
                  } else if (requestID.equals("gs_breed_monsters")) {
                     this.breedMonsters(sender, params);
                  } else if (requestID.equals("gs_finish_breeding")) {
                     this.finishBreeding(sender, params);
                  } else if (requestID.equals("gs_feed_monster")) {
                     this.feedMonster(sender, params);
                  } else if (requestID.equals("gs_move_monster")) {
                     this.moveMonster(sender, params);
                  } else if (requestID.equals("gs_mute_monster")) {
                     this.muteMonster(sender, params);
                  } else if (requestID.equals("gs_flip_monster")) {
                     this.flipMonster(sender, params);
                  } else if (requestID.equals("gs_sell_monster")) {
                     this.sellMonster(sender, params);
                  } else if (requestID.equals("gs_collect_monster")) {
                     this.collectFromMonster(sender, params);
                  } else if (requestID.equals("gs_test_collect_monster")) {
                     this.testCollectFromMonster(sender, params);
                  } else if (requestID.equals("gs_name_monster")) {
                     this.nameMonster(sender, params);
                  } else if (requestID.equals("gs_tribal_feed_monster")) {
                     this.tribalFeedMonster(sender, params);
                  } else if (requestID.equals("gs_admin_user_neighbors")) {
                     this.processAdminNeighborChangeMsg(sender, params);
                  } else if (requestID.equals("gs_admin_multi_neighbors")) {
                     this.processAdminMultiNeighborChangeMsg(sender, params);
                  } else if (requestID.equals("gs_multi_neighbors")) {
                     this.processMultiNeighborChangeMsg(sender, params);
                  } else if (requestID.equals("gs_store_monster")) {
                     this.putMonsterInStorage(sender, params);
                  } else if (requestID.equals("gs_unstore_monster")) {
                     this.removeMonsterFromStorage(sender, params);
                  } else if (requestID.equals("gs_mega_monster_message")) {
                     this.megaMonsterMessage(sender, params);
                  } else if (requestID.equals("gs_light_torch")) {
                     this.lightTorch(sender, params, false);
                  } else if (requestID.equals("gs_additional_friend_torch_data")) {
                     this.getFriendTorchInfo(sender, params);
                  } else if (requestID.equals("gs_get_torchgifts")) {
                     this.getTorchGifts(sender, params);
                  } else if (requestID.equals("gs_collect_torchgift")) {
                     this.lightTorch(sender, params, true);
                  } else if (requestID.equals("gs_set_light_torch_flag")) {
                     this.lightTorchFlag(sender, params);
                  } else if (requestID.equals("gs_box_activate_monster")) {
                     this.boxActivateMonster(sender, params);
                  } else if (requestID.equals("gs_attempt_early_box_activate")) {
                     this.attemptEarlyBoxActivate(sender, params);
                  } else if (requestID.equals("gs_viewed_cruc_unlock")) {
                     this.unlockedCrucible(sender, params);
                  } else if (requestID.equals("gs_start_amber_evolve")) {
                     this.startAmberEvolve(sender, params);
                  } else if (requestID.equals("gs_finish_amber_evolve")) {
                     this.finishAmberEvolve(sender, params);
                  } else if (requestID.equals("gs_speedup_amber_evolve")) {
                     this.speedupAmberEvolve(sender, params);
                  } else if (requestID.equals("gs_collect_cruc_heat")) {
                     this.collectCrucHeat(sender, params);
                  } else if (requestID.equals("gs_purchase_evolve_unlock")) {
                     this.purchaseEvolveUnlock(sender, params);
                  } else if (requestID.equals("gs_purchase_evo_powerup_unlock")) {
                     this.purchaseEvolvePowerupUnlock(sender, params);
                  } else if (requestID.equals("gs_box_purchase_fill")) {
                     this.boxPurchaseFill(sender, params);
                  } else if (requestID.equals("gs_box_add_egg")) {
                     this.boxAddEgg(sender, params);
                  } else if (requestID.equals("gs_box_add_monster")) {
                     this.boxAddMonster(sender, params);
                  } else if (requestID.equals("gs_send_ethereal_monster")) {
                     this.sendMonsterHome(requestID, sender, params, false);
                  } else if (requestID.equals("gs_send_monster_home")) {
                     this.sendMonsterHome(requestID, sender, params, false);
                  } else if (requestID.equals("gs_buy_structure")) {
                     this.buyStructure(sender, params);
                  } else if (requestID.equals("gs_move_structure")) {
                     this.moveStructure(sender, params);
                  } else if (requestID.equals("gs_sell_structure")) {
                     this.sellStructure(sender, params);
                  } else if (requestID.equals("gs_start_upgrade_structure")) {
                     this.startUpgradeStructure(sender, params);
                  } else if (requestID.equals("gs_finish_upgrade_structure")) {
                     this.finishUpgradeStructure(sender, params);
                  } else if (requestID.equals("gs_finish_structure")) {
                     this.finishStructure(sender, params);
                  } else if (requestID.equals("gs_flip_structure")) {
                     this.flipStructure(sender, params);
                  } else if (requestID.equals("gs_mute_structure")) {
                     this.muteStructure(sender, params);
                  } else if (requestID.equals("gs_start_obstacle")) {
                     this.startObstacle(sender, params);
                  } else if (requestID.equals("gs_clear_obstacle")) {
                     this.clearObstacle(sender, params);
                  } else if (requestID.equals("gs_clear_obstacle_speed_up")) {
                     this.clearObstacleSpeedUp(sender, params);
                  } else if (requestID.equals("gs_start_baking")) {
                     this.startBaking(sender, params);
                  } else if (requestID.equals("gs_start_rebake")) {
                     this.startRebake(sender, params);
                  } else if (requestID.equals("gs_finish_baking")) {
                     this.finishBaking(sender, params);
                  } else if (requestID.equals("gs_start_fuzing")) {
                     this.startFuzing(sender, params);
                  } else if (requestID.equals("gs_finish_fuzing")) {
                     this.finishFuzing(sender, params);
                  } else if (requestID.equals("gs_collect_from_mine")) {
                     this.collectFromMine(sender, params);
                  } else if (requestID.equals("gs_speed_up_structure")) {
                     this.structureSpeedUp(sender, params);
                  } else if (requestID.equals("gs_store_decoration")) {
                     this.putDecorationInStorage(sender, params);
                  } else if (requestID.equals("gs_unstore_decoration")) {
                     this.removeDecorationFromStorage(sender, params);
                  } else if (requestID.equals("gs_store_buddy")) {
                     this.putBuddyInFuzer(sender, params);
                  } else if (requestID.equals("gs_unstore_buddy")) {
                     this.removeBuddyFromFuzer(sender, params);
                  } else if (requestID.equals("gs_speed_up_breeding")) {
                     this.breedingSpeedUp(sender, params);
                  } else if (requestID.equals("gs_speed_up_baking")) {
                     this.bakingSpeedUp(sender, params);
                  } else if (requestID.equals("gs_speed_up_fuzing")) {
                     this.fuzingSpeedUp(sender, params);
                  } else if (requestID.equals("gs_speed_up_hatching")) {
                     this.hatchingSpeedUp(sender, params);
                  } else if (requestID.equals("gs_get_friends")) {
                     this.getFriends(sender, params);
                  } else if (requestID.equals("gs_get_random_tribes")) {
                     this.getRandomTribes(sender, params);
                  } else if (requestID.equals("gs_unlock_breeding_structure")) {
                     this.unlockBreedingStructure(sender, params);
                  } else if (requestID.equals("gs_get_messages")) {
                     this.getMessages(sender, params);
                  } else if (requestID.equals("gs_delete_message")) {
                     this.deleteMessage(sender, params);
                  } else if (requestID.equals("gs_get_friend_visit_data")) {
                     this.getFriendVisitData(sender, params);
                  } else if (requestID.equals("gs_visit_specific_friend_island")) {
                     this.visitSpecificFriendIsland(sender, params);
                  } else if (requestID.equals("gs_set_fav_friend")) {
                     this.setFriendAsFav(sender, params);
                  } else if (requestID.equals("gs_get_random_visit_data")) {
                     this.getRandomVisitData(sender, params);
                  } else if (requestID.equals("gs_get_tribal_island_data")) {
                     this.getTribalIslandData(sender, params);
                  } else if (requestID.equals("gs_get_ranked_island_data")) {
                     this.getRankedIslandData(sender, params);
                  } else if (requestID.equals("gs_get_island_rank")) {
                     this.getIslandRank(sender, params);
                  } else if (requestID.equals("gs_collect_invite_reward")) {
                     this.collectInviteReward(sender, params);
                  } else if (requestID.equals("gs_rate_island")) {
                     this.rateIsland(sender, params);
                  } else if (requestID.equals("gs_report_user")) {
                     this.reportUser(sender, params);
                  } else if (requestID.equals("gs_process_unclaimed_purchases")) {
                     this.processUnclaimedPurchases(sender, params);
                  } else if (requestID.equals("gs_collect_rewards")) {
                     this.processUncollectedOfferRewards(sender, params);
                  } else if (requestID.equals("gs_collect_facebook_reward")) {
                     this.collectFacebookReward(sender, params);
                  } else if (requestID.equals("gs_currency_conversion")) {
                     this.convertCurrency(sender, params);
                  } else if (requestID.equals("gs_currency_coins2eth_conversion")) {
                     this.convertCoinsToEthCurrency(sender, params);
                  } else if (requestID.equals("gs_currency_diamonds2eth_conversion")) {
                     this.convertDiamondsToEthCurrency(sender, params);
                  } else if (requestID.equals("gs_currency_eth2diamonds_conversion")) {
                     this.convertEthToDiamondsCurrency(sender, params);
                  } else if (requestID.equals("gs_referral_request")) {
                     this.addBbbIdReferral(sender, params);
                  } else if (!requestID.equals("gs_offer_viewed")) {
                     if (requestID.equals("gs_offer_completed")) {
                        this.completeOffer(sender, params);
                     } else if (requestID.equals("gs_paywall_updated")) {
                        this.updatePaywall(sender, params);
                     } else if (requestID.equals("gs_app_link")) {
                        this.goDeeper(sender, params);
                     } else if (requestID.equals("gs_player_has_scratch_off")) {
                        this.playerHasScratchOff(sender, params);
                     } else if (requestID.equals("gs_play_scratch_off")) {
                        this.playScratchOff(sender, params, false);
                     } else if (requestID.equals("gs_purchase_scratch_off")) {
                        this.playScratchOff(sender, params, true);
                     } else if (requestID.equals("gs_collect_scratch_off")) {
                        this.collectScratchOffPrize(sender, params);
                     } else if (requestID.equals("gs_flip_minigame_cost")) {
                        this.requestFlipMinigameCost(sender, params);
                     } else if (requestID.equals("gs_collect_flip_level")) {
                        this.collectFlipLevelPrize(sender, params);
                     } else if (requestID.equals("gs_collect_flip_mini_game")) {
                        this.collectFlipEndgamePrize(sender, params);
                     } else if (requestID.equals("gs_purchase_flip_mini_game")) {
                        this.purchaseFlipGame(sender, params);
                     } else if (requestID.equals("gs_delete_composer_template")) {
                        this.deleteComposerTemplate(sender, params);
                     } else if (requestID.equals("gs_save_composer_template")) {
                        this.saveComposerTemplate(sender, params);
                     } else if (requestID.equals("gs_save_composer_track")) {
                        this.saveComposerTrack(sender, params);
                     } else if (requestID.equals("gs_delete_mail")) {
                        this.deleteMail(sender, params);
                     } else if (requestID.equals("gs_daily_login_buyback")) {
                        this.dailyLoginBuyback(sender, params);
                     } else if (requestID.equals("gs_send_facebook_help")) {
                        this.sendFacebookHelp(sender, params);
                     } else if (requestID.equals("gs_handle_facebook_help_instances")) {
                        this.handleFacebookHelpInstances(sender, params);
                     } else if (requestID.equals("gs_request_facebook_help_permissions")) {
                        this.requestFacebookHelpPermissions(sender, params);
                     } else if (requestID.equals("gs_purchase_buyback")) {
                        this.purchaseBuyback(sender, params);
                     } else if (requestID.equals("gs_admin_purchase_buyback")) {
                        this.adminPurchaseBuyback(sender, params);
                     } else if (requestID.equals("gs_add_friend")) {
                        this.addFriend(sender, params);
                     } else if (requestID.equals("gs_remove_friend")) {
                        this.removeFriend(sender, params);
                     } else if (requestID.equals("gs_sync_friends")) {
                        this.syncFriends(sender, params);
                     } else if (!requestID.equals("gs_get_starting_ccu") && !requestID.equals("gs_get_finishing_ccu") && !requestID.equals("gs_get_ccu")) {
                        if (requestID.equals("gs_admin_get_codes")) {
                           this.adminGetCodes(sender, params);
                        } else if (requestID.equals("gs_admin_refresh_all")) {
                           this.adminRefreshAll(sender, params);
                        } else if (requestID.equals("gs_admin_move_users_monster")) {
                           this.adminMoveUsersMonster(sender, params);
                        } else if (requestID.equals("gs_admin_move_users_structure")) {
                           this.adminMoveUsersStructure(sender, params);
                        } else if (requestID.equals("gs_admin_destroy_users_monster")) {
                           this.adminDestroyUsersMonster(sender, params);
                        } else if (requestID.equals("gs_admin_destroy_users_structure")) {
                           this.adminDestroyUsersStructure(sender, params);
                        } else if (requestID.equals("gs_admin_get_user_visit_data")) {
                           this.getAdminVisitData(sender, params);
                        } else if (requestID.equals("gs_admin_sell_egg")) {
                           this.adminSellEgg(sender, params);
                        } else if (requestID.equals("gs_admin_buy_egg")) {
                           this.adminBuyEgg(sender, params);
                        } else if (requestID.equals("gs_admin_hatch_egg")) {
                           this.adminHatchEgg(sender, params);
                        } else if (requestID.equals("gs_admin_buy_structure")) {
                           this.adminBuyStructure(sender, params);
                        } else if (requestID.equals("gs_admin_buy_island")) {
                           this.adminBuyIsland(sender, params);
                        } else if (requestID.equals("gs_admin_change_island")) {
                           this.adminChangeIsland(sender, params);
                        } else if (requestID.equals("gs_admin_destroy_island")) {
                           this.adminDestroyIsland(sender, params);
                        } else if (requestID.equals("gs_admin_feed_monster")) {
                           this.adminFeedMonster(sender, params);
                        } else if (requestID.equals("gs_admin_start_upgrade_structure")) {
                           this.adminFinishUpgradeStructure(sender, params);
                        } else if (requestID.equals("gs_admin_clear_obstacle_speed_up")) {
                           this.adminClearObstacleSpeedUp(sender, params);
                        } else if (requestID.equals("gs_admin_speed_up_structure")) {
                           this.adminStructureSpeedUp(sender, params);
                        } else if (requestID.equals("gs_admin_speed_up_breeding")) {
                           this.adminBreedingSpeedUp(sender, params);
                        } else if (requestID.equals("gs_admin_start_baking")) {
                           this.adminStartBaking(sender, params);
                        } else if (requestID.equals("gs_admin_speed_up_baking")) {
                           this.adminBakingSpeedUp(sender, params);
                        } else if (requestID.equals("gs_admin_start_fuzing")) {
                           this.adminStartFuzing(sender, params);
                        } else if (requestID.equals("gs_admin_speed_up_fuzing")) {
                           this.adminFuzingSpeedUp(sender, params);
                        } else if (requestID.equals("gs_admin_remove_breeding")) {
                           this.adminRemoveBreeding(sender, params);
                        } else if (requestID.equals("gs_admin_finish_breeding")) {
                           this.adminFinishBreeding(sender, params);
                        } else if (requestID.equals("gs_admin_speed_up_hatching")) {
                           this.adminHatchingSpeedUp(sender, params);
                        } else if (requestID.equals("gs_admin_give_me_shit")) {
                           this.adminGiveMeShit(sender, params);
                        } else if (requestID.equals("gs_admin_flip_monster")) {
                           this.adminFlipMonster(sender, params);
                        } else if (requestID.equals("gs_admin_flip_structure")) {
                           this.adminFlipStructure(sender, params);
                        } else if (requestID.equals("gs_admin_send_ethereal_monster")) {
                           this.sendMonsterHome("gs_send_ethereal_monster", sender, params, true);
                        } else if (requestID.equals("gs_admin_send_monster_home")) {
                           this.sendMonsterHome("gs_send_monster_home", sender, params, true);
                        } else if (requestID.equals("gs_admin_name_monster")) {
                           this.adminNameMonster(sender, params);
                        } else if (requestID.equals("gs_admin_quest")) {
                           this.adminGetUserQuestInfo(sender, params);
                        } else if (requestID.equals("gs_admin_complete_quest")) {
                           this.adminCompleteUserQuest(sender, params);
                        } else if (requestID.equals("gs_player")) {
                           this.getPlayerInfo(sender, params);
                        } else if (requestID.equals("gs_request_next_relic_reset")) {
                           this.requestNextRelicReset(sender, params);
                        } else if (requestID.equals("gs_collect_daily_reward")) {
                           this.collectDailyReward(sender, params);
                        } else if (requestID.equals("gs_admin_kick_user")) {
                           this.adminKickUser(sender, params);
                        } else if (requestID.equals("gs_admin_store_monster")) {
                           this.adminPutMonsterInStorage(sender, params);
                        } else if (requestID.equals("gs_admin_unstore_monster")) {
                           this.adminRemoveMonsterFromStorage(sender, params);
                        } else if (requestID.equals("gs_admin_store_decoration")) {
                           this.adminPutDecorationInStorage(sender, params);
                        } else if (requestID.equals("gs_admin_unstore_decoration")) {
                           this.adminRemoveDecorationFromStorage(sender, params);
                        } else if (requestID.equals("gs_admin_store_buddy")) {
                           this.adminPutBuddyInFuzer(sender, params);
                        } else if (requestID.equals("gs_admin_unstore_buddy")) {
                           this.adminRemoveBuddyFromFuzer(sender, params);
                        } else if (requestID.equals("gs_admin_finish_fuzing")) {
                           this.adminFinishFuzing(sender, params);
                        } else if (requestID.equals("gs_admin_unlight_torch")) {
                           this.adminUnlightTorch(sender, params);
                        } else if (requestID.equals("gs_admin_box_monster_toggle")) {
                           this.adminBoxMonsterToggle(sender, params);
                        } else if (requestID.equals("gs_admin_box_monster_toggle_new")) {
                           this.adminBoxMonsterToggleNew(sender, params);
                        } else if (requestID.equals("gs_admin_box_purchase_fill")) {
                           this.adminBoxPurchaseFill(sender, params);
                        } else if (requestID.equals("gs_admin_box_activate_monster")) {
                           this.adminBoxActivateMonster(sender, params);
                        } else if (requestID.equals("gs_admin_finished_edit")) {
                           this.adminFinishedEdit(sender, params);
                        } else if (requestID.equals("gs_admin_swap_tribal_monster")) {
                           this.adminSwapTribalMonster(sender, params);
                        } else if (requestID.equals("gs_test_breed_monsters")) {
                           this.testBreeding(sender, params);
                        } else if (requestID.equals("gs_test_breed_costumes")) {
                           this.testBreedCostumes(sender, params);
                        } else if (requestID.equals("gs_test_cruc_evolv")) {
                           this.testCrucEvolve(sender, params);
                        } else if (requestID.equals("gs_test_spin_probabilities")) {
                           this.testSpinProbabilities(sender, params);
                        } else if (requestID.contentEquals("gs_test_scratch")) {
                           this.testScratchTicket(sender, params);
                        } else if (requestID.contentEquals("gs_test_synthesis")) {
                           this.testSynthesis(sender, params);
                        } else if (requestID.equals("gs_admin_select_breed_egg")) {
                           this.adminQASelectBreedEgg(sender, params);
                        } else if (requestID.equals("battle_start")) {
                           this.startBattle(sender, params);
                        } else if (requestID.equals("battle_start_versus")) {
                           this.startBattleVersus(sender, params);
                        } else if (requestID.equals("battle_start_friend")) {
                           this.startBattleFriend(sender, params);
                        } else if (requestID.equals("battle_finish")) {
                           this.finishBattle(sender, params);
                        } else if (requestID.equals("battle_start_training")) {
                           this.startTraining(sender, params);
                        } else if (requestID.equals("battle_finish_training")) {
                           this.trainingSpeedUp(sender, params);
                        } else if (requestID.equals("battle_teleport")) {
                           this.sendMonsterToBattleIsland(sender, params, false);
                        } else if (requestID.equals("battle_purchase_campaign_reward")) {
                           this.purchaseBattleCampaignReward(sender, params);
                        } else if (requestID.equals("battle_set_music")) {
                           this.setBattleMusic(sender, params);
                        } else if (requestID.equals("battle_refresh_versus_attempts")) {
                           this.refreshBattleVersusAttempts(sender, params);
                        } else if (requestID.equals("battle_claim_versus_rewards")) {
                           this.claimBattleVersusRewards(sender, params);
                        } else if (requestID.equals("update_viewed_campaigns")) {
                           this.updateViewedCampaigns(sender, params);
                        } else if (requestID.equals("admin_battle_campaign_reset")) {
                           this.adminBattleCampaignReset(sender, params);
                        } else if (requestID.equals("gs_set_avatar")) {
                           this.setPlayerAvatar(sender, params);
                        } else if (requestID.equals("purchase_costume")) {
                           this.purchaseCostume(sender, params);
                        } else if (requestID.equals("equip_costume")) {
                           this.equipCostume(sender, params);
                        } else if (requestID.equals("gs_viewed_egg")) {
                           this.viewedEgg(sender, params);
                        } else if (requestID.equals("gs_viewed_cruc_monst")) {
                           this.viewedInCrucible(sender, params);
                        } else if (requestID.equals("gs_set_moniker")) {
                           this.setMoniker(sender, params);
                        } else if (requestID.equals("gs_delete_account")) {
                           this.deleteAccount(sender, params);
                        } else if (requestID.equals("update_awakener")) {
                           this.updateAwakener(sender, params);
                        } else if (requestID.equals("collect_daily_cumulative_login_rewards")) {
                           this.collectDailyCumulativeLoginRewards(sender, params);
                        } else if (requestID.equals("gs_start_attuning")) {
                           this.startAttuning(sender, params);
                        } else if (requestID.equals("gs_finish_attuning")) {
                           this.finishAttuning(sender, params);
                        } else if (requestID.equals("gs_speedup_attuning")) {
                           this.speedUpAttuning(sender, params);
                        } else if (requestID.equals("gs_start_synthesizing")) {
                           this.startSynthesizing(sender, params);
                        } else if (requestID.equals("gs_speedup_synthesizing")) {
                           this.speedUpSynthesizing(sender, params);
                        } else if (requestID.equals("gs_collect_synthesizing_failure")) {
                           this.collectSynthesizingFailure(sender, params);
                        } else {
                           Logger.trace(requestID + " is not supported yet.");
                        }
                     } else {
                        this.getCcu(requestID, sender, params);
                     }
                  }
               }
            }

         }
      } catch (Exception var8) {
         Logger.trace(var8, "Exception while processing request:", params.getDump());
         throw new RuntimeException("Exception while processing request", var8);
      }
   }

   private void deleteComposerTemplate(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      Long id = params.getLong("id");
      if (id != null && player.deleteTrack(id)) {
         try {
            String sql = "DELETE FROM user_tracks WHERE user_track_id = ?";
            this.ext.getDB().update(sql, new Object[]{id});
            this.ext.stats.trackComposerDeletedTrack(sender, id);
         } catch (Exception var6) {
            Logger.trace(var6, "error deleting user track", "   params : " + params.getDump());
         }
      }

   }

   private void saveComposerTemplate(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      String name = params.getUtfString("name");
      byte[] noteArray = params.getByteArray("bintrack");
      int format = params.getInt("format");
      ISFSArray tracks = player.getTracks();
      int totalTemplates = 0;
      ISFSObject replacementObj = null;
      String sanitizedName = Helpers.sanitizeName(name, " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!¨\"#$&'()*+,-./:;<=>?@{}0123456789|£©¿®`~^ÀÁÂÄÇÈÉÊËÌÍÎÏÑÒÓÔÖÙÚÛÜßàáâäçèéêëìíîïñòóôöùúûü_ÆæÃãÕõ€¡");
      if (!sanitizedName.equals(name)) {
         name = null;
      }

      int i;
      String sql;
      for(i = 0; i < tracks.size(); ++i) {
         sql = tracks.getSFSObject(i).getUtfString("name");
         if (sql != null) {
            if (sql.equals(name)) {
               replacementObj = tracks.getSFSObject(i);
            }

            ++totalTemplates;
         }
      }

      if (totalTemplates >= GameSettings.getInt("USER_MAX_COMPOSER_TEMPLATES") && replacementObj == null) {
         this.ext.sendErrorResponse("gs_save_composer_template", "TEMPLATE_TOO_MANY_NOTIFICATION", sender);
      } else {
         if (format == 1) {
            if (name == null || noteArray == null || noteArray.length > 512) {
               this.ext.sendErrorResponse("gs_save_composer_template", "TEMPLATE_FORMAT_ERROR", sender);
               return;
            }

            for(i = 0; i < noteArray.length; ++i) {
               int val = noteArray[i] & 255;
               if (val != 0 && (val < 28 || val > 87)) {
                  this.ext.sendErrorResponse("gs_save_composer_template", "TEMPLATE_FORMAT_ERROR", sender);
                  return;
               }
            }
         } else {
            if (format != 2) {
               this.ext.sendErrorResponse("gs_save_composer_template", "TEMPLATE_FORMAT_ERROR", sender);
               return;
            }

            if (name == null || noteArray == null || noteArray.length > 512) {
               this.ext.sendErrorResponse("gs_save_composer_template", "TEMPLATE_FORMAT_ERROR", sender);
               return;
            }
         }

         try {
            Long id = 0L;
            if (replacementObj == null) {
               ISFSObject replacementObj = new SFSObject();
               replacementObj.putLong("user", player.getPlayerId());
               replacementObj.putUtfString("name", name);
               replacementObj.putByteArray("bintrack", noteArray);
               sql = "INSERT INTO user_tracks SET user=?, name=?, bintrack=?, format=?";
               id = this.ext.getDB().insertGetIdNoBruno(sql, new Object[]{player.getPlayerId(), name, noteArray, format});
               replacementObj.putLong("user_track_id", id);
               player.getTracks().addSFSObject(replacementObj);
            } else {
               replacementObj.putByteArray("bintrack", noteArray);
               sql = "UPDATE user_tracks SET bintrack=?, format=? WHERE user_track_id=?";
               this.ext.getDB().update(sql, new Object[]{noteArray, format, replacementObj.getLong("user_track_id")});
               id = replacementObj.getLong("user_track_id");
            }

            this.ext.stats.trackComposerTemplateSaved(sender, id, name);
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            response.putLong("id", id);
            this.send("gs_save_composer_template", response, sender);
         } catch (Exception var13) {
            Logger.trace(var13, "error saving user template", "   params : " + params.getDump());
            this.ext.sendErrorResponse("gs_save_composer_template", "TEMPLATE_SAVING_ERROR", sender);
         }
      }
   }

   private void saveComposerTrack(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      long island = params.getLong("island");
      long id = params.getLong("track");
      byte[] noteArray = params.getByteArray("bintrack");
      int keySig = params.getInt("key_sig");
      int timeNumerator = params.getInt("time_numerator");
      int timeDenom = params.getInt("time_denom");
      int tempo = params.getInt("tempo");
      int format = params.getInt("format");
      if (player.getIslandByID(island) != null && player.getIslandByID(island).isComposerIsland()) {
         if (keySig < -7 || keySig > 7) {
            keySig = 0;
         }

         if (timeNumerator < 1 || timeNumerator > 12) {
            timeNumerator = 4;
         }

         if (timeDenom != 2 && timeDenom != 4 && timeDenom != 8) {
            timeDenom = 4;
         }

         if (tempo < 40 || tempo > 480) {
            tempo = 120;
         }

         ISFSArray tracks = player.getTracks();
         ISFSObject replacementObj = null;

         int i;
         for(i = 0; i < tracks.size(); ++i) {
            long trackId = tracks.getSFSObject(i).getLong("user_track_id");
            if (trackId == id) {
               replacementObj = tracks.getSFSObject(i);
               break;
            }
         }

         if (format == 1) {
            label70: {
               if (replacementObj != null && noteArray != null && noteArray.length <= 512) {
                  i = 0;

                  while(true) {
                     if (i >= noteArray.length) {
                        break label70;
                     }

                     int val = noteArray[i] & 255;
                     if (val != 0 && (val < 28 || val > 87)) {
                        this.ext.sendErrorResponse("gs_save_composer_track", "TRACK_FORMAT_ERROR", sender);
                        return;
                     }

                     ++i;
                  }
               }

               this.ext.sendErrorResponse("gs_save_composer_track", "TRACK_FORMAT_ERROR", sender);
               return;
            }
         } else {
            if (format != 2) {
               this.ext.sendErrorResponse("gs_save_composer_track", "TRACK_FORMAT_ERROR", sender);
               return;
            }

            if (replacementObj == null || noteArray == null || noteArray.length > 512) {
               this.ext.sendErrorResponse("gs_save_composer_track", "TRACK_FORMAT_ERROR", sender);
               return;
            }
         }

         try {
            replacementObj.putByteArray("bintrack", noteArray);
            String sql = "UPDATE user_tracks SET bintrack=?, format=? WHERE user_track_id=?";
            this.ext.getDB().update(sql, new Object[]{noteArray, format, id});
            sql = "UPDATE user_songs SET tempo=?, time_numerator=?, time_denom=?, key_sig=? WHERE island=?";
            this.ext.getDB().update(sql, new Object[]{tempo, timeNumerator, timeDenom, keySig, island});
            this.ext.stats.trackComposerTrackSaved(sender, id, tempo, timeNumerator, timeDenom, keySig, island);
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            this.send("gs_save_composer_track", response, sender);
         } catch (Exception var19) {
            Logger.trace(var19, "error saving user track", "   params : " + params.getDump());
            this.ext.sendErrorResponse("gs_save_composer_track", "TRACK_SAVING_ERROR", sender);
         }
      } else {
         this.ext.sendErrorResponse("gs_save_composer_track", "TRACK_SAVING_ERROR", sender);
      }
   }

   private void deleteMail(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      long id = params.getLong("id");
      ISFSObject response = new SFSObject();
      SFSArray updateVars = new SFSArray();
      boolean success = false;

      try {
         ISFSArray mailbox = player.getMailbox();

         for(int i = 0; i < mailbox.size(); ++i) {
            ISFSObject curMail = mailbox.getSFSObject(i);
            if (curMail.getLong("user_mail_id") == id) {
               ISFSObject attachment = curMail.getSFSObject("attachment");
               String type;
               if (attachment != null) {
                  type = attachment.getUtfString("type");
                  ISFSObject updateObj = new SFSObject();
                  if (!type.equals("entity")) {
                     if (type.equals("coins")) {
                        player.adjustCoins(sender, this, SFSHelpers.getInt("quantity", attachment));
                        updateObj.putLong("coins_actual", player.getActualCoins());
                        this.ext.stats.trackReward(sender, "mailbox", "coins", (long)SFSHelpers.getInt("quantity", attachment));
                     } else if (type.equals("diamond")) {
                        player.adjustDiamonds(sender, this, SFSHelpers.getInt("quantity", attachment));
                        updateObj.putLong("diamonds_actual", player.getActualDiamonds());
                        this.ext.stats.trackReward(sender, "mailbox", "diamonds", (long)SFSHelpers.getInt("quantity", attachment));
                     } else if (type.equals("eth_currency")) {
                        player.adjustEthCurrency(sender, this, SFSHelpers.getInt("quantity", attachment));
                        updateObj.putLong("ethereal_currency_actual", player.getActualEthCurrency());
                        this.ext.stats.trackReward(sender, "mailbox", "ethereal_currency", (long)SFSHelpers.getInt("quantity", attachment));
                     } else if (type.equals("food")) {
                        player.adjustFood(sender, this, SFSHelpers.getInt("quantity", attachment));
                        updateObj.putLong("food_actual", player.getActualFood());
                        this.ext.stats.trackReward(sender, "mailbox", "food", (long)SFSHelpers.getInt("quantity", attachment));
                     } else if (type.equals("key")) {
                        player.adjustKeys(sender, this, SFSHelpers.getInt("quantity", attachment));
                        updateObj.putLong("keys_actual", player.getActualKeys());
                        this.ext.stats.trackReward(sender, "mailbox", "keys", (long)SFSHelpers.getInt("quantity", attachment));
                     } else if (type.equals("relics")) {
                        player.adjustRelics(sender, this, SFSHelpers.getInt("quantity", attachment));
                        updateObj.putLong("relics_actual", player.getActualRelics());
                        this.ext.stats.trackReward(sender, "mailbox", "relics", (long)SFSHelpers.getInt("quantity", attachment));
                     } else if (type.equals("egg_wildcards")) {
                        player.adjustEggWildcards(sender, this, SFSHelpers.getInt("quantity", attachment));
                        updateObj.putLong("egg_wildcards_actual", player.getActualEggWildcards());
                        this.ext.stats.trackReward(sender, "mailbox", "egg_wildcards", (long)SFSHelpers.getInt("quantity", attachment));
                     } else if (type.equals("starpower")) {
                        player.adjustStarpower(sender, this, (long)SFSHelpers.getInt("quantity", attachment));
                        updateObj.putLong("starpower_actual", player.getActualStarpower());
                        this.ext.stats.trackReward(sender, "mailbox", "starpower", (long)SFSHelpers.getInt("quantity", attachment));
                     } else if (type.equals("xp")) {
                        boolean hasNewLevel = player.rewardXp(sender, this, SFSHelpers.getInt("quantity", attachment));
                        updateObj.putInt("xp", player.getXp());
                        if (hasNewLevel) {
                           updateVars.addSFSObject(updateObj);
                           updateObj = new SFSObject();
                           updateObj.putInt("level", player.getLevel());
                        }
                     }
                  }

                  updateVars.addSFSObject(updateObj);
               }

               mailbox.removeElementAt(i);
               type = "UPDATE user_mail SET deleted = 1 WHERE user_mail_id = ?";
               this.ext.getDB().update(type, new Object[]{id});
               success = true;
               break;
            }
         }

         if (updateVars.size() > 0) {
            response.putSFSArray("properties", updateVars);
         }

         response.putBool("success", success);
         this.send("gs_delete_mail", response, sender);
      } catch (Exception var16) {
         Logger.trace(var16, "error deleting user mail", "   params : " + params.getDump());
      }
   }

   private void updateMail(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      SFSArray updateVars = new SFSArray();
      SFSObject updateObj = new SFSObject();
      updateObj.putSFSArray("mailbox", player.getMailbox());
      updateVars.addSFSObject(updateObj);
      updateObj = new SFSObject();
      updateObj.putBool("new_mail", player.getNewMail());
      updateVars.addSFSObject(updateObj);
      ISFSObject response = new SFSObject();
      response.putSFSArray("properties", updateVars);
      this.send("gs_update_properties", response, sender);
   }

   private void dailyLoginBuyback(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      ISFSObject response = new SFSObject();
      SFSArray updateVars = new SFSArray();
      boolean success = false;
      if (player.getPendingDailyRewardType() != Player.CurrencyType.Deferred) {
         this.ext.sendErrorResponse("gs_daily_login_buyback", "daily_login_buyback not valid", sender);
      } else {
         boolean clearBuyback = false;
         if (params.containsKey("clear_buyback")) {
            clearBuyback = params.getBool("clear_buyback");
         }

         try {
            if (clearBuyback) {
               if (player.restartLoginStreak()) {
                  new SFSObject();
                  ISFSObject updateObj = new SFSObject();
                  updateObj.putInt("reward_day", player.getPendingRewardDay());
                  updateVars.addSFSObject(updateObj);
                  player.updatePendingFromRewardDay();
                  updateObj = new SFSObject();
                  updateObj.putUtfString("daily_bonus_type", player.getPendingDailyRewardType().getCurrencyKey());
                  updateVars.addSFSObject(updateObj);
                  updateObj = new SFSObject();
                  updateObj.putInt("daily_bonus_amount", player.getPendingDailyRewardAmount());
                  updateVars.addSFSObject(updateObj);
                  updateObj = new SFSObject();
                  updateObj.putInt("cachedRewardDay", player.cachedRewardDay());
                  updateVars.addSFSObject(updateObj);
                  success = true;
               } else {
                  this.ext.sendErrorResponse("gs_daily_login_buyback", "ERROR_REINITIALIZING_DAILY_LOGIN", sender);
               }
            } else {
               if (!player.canBuy(0L, 0L, (long)GameSettings.getInt("USER_DAILY_LOGIN_BUYBACK_PRICE"), 0L, 0L, 0L, 0)) {
                  player.clearDailyLoginStreak();
                  this.ext.sendErrorResponse("gs_daily_login_buyback", "DAILY_LOGIN_INSUFFICIENT_FUNDS", sender);
                  return;
               }

               int diamondCost = GameSettings.getInt("USER_DAILY_LOGIN_BUYBACK_PRICE");
               player.buyDailyLoginStreak();
               player.chargePlayer(sender, this, 0, 0, diamondCost, 0L, 0, 0, 0);
               ISFSObject updateObj = new SFSObject();
               updateObj.putUtfString("daily_bonus_type", player.getPendingDailyRewardType().getCurrencyKey());
               updateVars.addSFSObject(updateObj);
               updateObj = new SFSObject();
               updateObj.putInt("daily_bonus_amount", player.getPendingDailyRewardAmount());
               updateVars.addSFSObject(updateObj);
               updateObj = new SFSObject();
               updateObj.putInt("reward_day", player.getPendingRewardDay());
               updateVars.addSFSObject(updateObj);
               updateObj = new SFSObject();
               updateObj.putInt("cachedRewardDay", player.cachedRewardDay());
               updateVars.addSFSObject(updateObj);
               updateObj = new SFSObject();
               updateObj.putInt("diamonds", player.getDisplayedDiamonds());
               updateObj.putLong("diamonds_actual", player.getActualDiamonds());
               updateVars.addSFSObject(updateObj);
               this.logDiamondUsage(sender, "daily_login_buyback", diamondCost, player.getLevel());
               success = true;
            }
         } catch (Exception var10) {
            Logger.trace(var10, "error buying back into daily login", "   params : " + params.getDump());
            return;
         }

         if (updateVars.size() > 0) {
            response.putSFSArray("properties", updateVars);
         }

         response.putBool("success", success);
         this.send("gs_daily_login_buyback", response, sender);
      }
   }

   private void updateDisplayName(User sender, ISFSObject params) {
      String newDisplayName = Helpers.sanitizeName(params.getUtfString("newName"), " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!¨\"#$&'()*+,-./:;<=>?@{}0123456789|£©¿®`~^ÀÁÂÄÇÈÉÊËÌÍÎÏÑÒÓÔÖÙÚÛÜßàáâäçèéêëìíîïñòóôöùúûü_ÆæÃãÕõАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя€₽¡");
      Player player = (Player)sender.getProperty("player_object");
      ArrayList<String> exclusions = new ArrayList(ProperNouns.getNouns());
      exclusions.add(Long.toString(player.getBbbId()));
      String errMsg = Helpers.invalidName(player.getPlayerId(), newDisplayName, "DISPLAY_NAME", exclusions);
      SFSObject response;
      if (errMsg != null) {
         response = new SFSObject();
         response.putBool("success", false);
         response.putUtfString("message", errMsg);
         response.putBool("responseToUser", params.containsKey("responseToUser") ? params.getBool("responseToUser") : true);
         this.send("gs_set_displayname", response, sender);
      } else {
         String fbMsg;
         if (GameSettings.get("FB_FIX", false) && newDisplayName.equalsIgnoreCase(GameSettings.get("FB_FIX_NAME", "Facebook123"))) {
            fbMsg = "FB_FIX_VALID_MESSAGE";
            if (player.getDateCreatedTime() > GameSettings.get("FB_FIX_TIME", 1678665600000L) && !player.hasMadePurchase() && !((String)sender.getSession().getProperty("loginType")).equalsIgnoreCase("fb")) {
               try {
                  String sql = "INSERT IGNORE INTO user_fb_fix SET guest_bbb_id = ?";
                  this.ext.getDB().update(sql, new Object[]{player.getBbbId()});
               } catch (Exception var9) {
                  fbMsg = "FB_FIX_ERROR_MESSAGE";
               }
            } else {
               fbMsg = "FB_FIX_INVALID_MESSAGE";
            }

            ISFSObject response = new SFSObject();
            response.putBool("success", false);
            response.putUtfString("message", fbMsg);
            response.putBool("responseToUser", true);
            this.send("gs_set_displayname", response, sender);
         } else {
            player.setDisplayName(newDisplayName);

            try {
               fbMsg = "UPDATE user_tribal_monsters SET name=? WHERE user_monster_id=?";
               Object[] args = new Object[]{newDisplayName, player.getPlayerId()};
               this.ext.getDB().update(fbMsg, args);
            } catch (Exception var10) {
               Logger.trace("error setting tribal name : " + newDisplayName + " for user_id: " + player.getPlayerId());
            }

            response = new SFSObject();
            response.putBool("success", true);
            response.putUtfString("displayName", newDisplayName);
            this.send("gs_set_displayname", response, sender);
         }
      }
   }

   private void redeemCode(User sender, ISFSObject params) {
      String codeString = params.getUtfString("code");
      Player player = (Player)sender.getProperty("player_object");
      if (player.getLastCodeTime() + 1000L > MSMExtension.CurrentDBTime()) {
         this.ext.sendErrorResponse("gs_get_code", "TOO_MANY_CODE_TRIES_TEXT", sender);
      } else {
         boolean referralCode = codeString.startsWith("R:");
         long referralId = 500000000L;
         if (!referralCode) {
            this.ext.sendErrorResponse("gs_get_code", "INVALID_CODE_TEXT", sender);
         } else if (referralCode && !this.canDoReferral(player, referralId, true)) {
            this.ext.sendErrorResponse("gs_get_code", "INVALID_CODE_TEXT", sender);
         } else {
            player.setLastCodeTime(MSMExtension.CurrentDBTime());
            Code code = null;

            try {
               code = CodeLookup.decrypt(codeString);
            } catch (CodeLookup.CodeException var20) {
               this.ext.sendErrorResponse("gs_get_code", var20.getMessage(), sender);
               return;
            }

            if (code == null) {
               this.ext.sendErrorResponse("gs_get_code", "INVALID_CODE_TEXT", sender);
            } else {
               try {
                  String sql = null;
                  if (code.getCode() != null) {
                     sql = "SELECT * FROM user_codes WHERE code=?";
                     if (MSMExtension.getInstance().getDB().exists(sql, new Object[]{codeString})) {
                        this.ext.sendErrorResponse("gs_get_code", "ALREADY_CLAIMED_CODE_TEXT", sender);
                        return;
                     }
                  } else if (code.getPromoCode() != null) {
                     sql = "SELECT * FROM user_codes WHERE code_id=? AND user=?";
                     if (MSMExtension.getInstance().getDB().exists(sql, new Object[]{code.getCodeID(), player.getPlayerId()})) {
                        this.ext.sendErrorResponse("gs_get_code", "ALREADY_CLAIMED_CODE_TEXT", sender);
                        return;
                     }

                     if (code.getCodeLimit() > 0L) {
                        sql = "UPDATE codes SET claim_count=claim_count+1 WHERE code_id=?";
                        MSMExtension.getInstance().getDB().update(sql, new Object[]{code.getCodeID()});
                     }

                     codeString = code.getPromoCode();
                  }

                  sql = "INSERT IGNORE INTO user_codes SET user=?, code=?, code_id=?, date_claimed=NOW()";
                  MSMExtension.getInstance().getDB().update(sql, new Object[]{player.getPlayerId(), codeString, code.getCodeID()});
                  JSONObject metricData = (new JSONObject()).put("code", codeString).put("advertiser_id", code.getAdvertiserID()).put("advertisement_id", code.getAdvertismentID()).put("campaign_id", code.getCampaignID()).put("claim_count", code.getClaimCount()).put("code_id", code.getCodeID());
                  this.ext.stats.sendUserEvent(sender, "get_code", metricData);
               } catch (Exception var19) {
                  this.ext.sendErrorResponse("gs_get_code", "INVALID_CODE_TEXT", sender);
                  Logger.trace(var19, "error looking up user code", "   params : " + params.getDump());
                  return;
               }

               SFSArray updateVars = new SFSArray();
               int diamondReward = 0;
               ISFSObject rewards = code.getRewards();
               Iterator rewardItr = rewards.iterator();

               while(true) {
                  String key;
                  int value;
                  SFSObject updateObj;
                  while(true) {
                     if (!rewardItr.hasNext()) {
                        SFSObject response;
                        if (referralCode) {
                           response = new SFSObject();

                           try {
                              referralId += (long)code.getCodeID();
                              this.doReferralTracking(sender, player, referralId, 0, true);
                              if (diamondReward > 0) {
                                 this.ext.stats.trackReward(sender, "player_referral_added", "diamonds", (long)diamondReward);
                              }

                              response.putBool("success", true);
                              response.putLong("referring_bbb_id", referralId);
                              ISFSArray responseVars = new SFSArray();
                              player.addPlayerPropertyData(responseVars, false);
                              response.putSFSArray("properties", responseVars);
                           } catch (Exception var18) {
                              Logger.trace(String.format("EXCEPTION: Unable to process referral by player '%d' for friend bbb id '%d'", player.getBbbId(), referralId));
                              response.putBool("success", false);
                           }

                           this.send("gs_referral_request", response, sender);
                        } else {
                           response = new SFSObject();
                           if (updateVars.size() > 0) {
                              response.putSFSArray("properties", updateVars);
                           }

                           response.putUtfString("message", code.getRewardText());
                           response.putBool("success", true);
                           this.send("gs_get_code", response, sender);
                        }

                        return;
                     }

                     Entry<String, SFSDataWrapper> entry = (Entry)rewardItr.next();
                     key = (String)entry.getKey();
                     value = (Integer)((SFSDataWrapper)entry.getValue()).getObject();
                     updateObj = new SFSObject();
                     if (key.equals("coins")) {
                        player.adjustCoins(sender, this, value);
                        updateObj.putInt("coins", player.getDisplayedCoins());
                        updateObj.putLong("coins_actual", player.getActualCoins());
                        break;
                     }

                     if (key.equals("diamonds")) {
                        diamondReward = value;
                        player.adjustDiamonds(sender, this, value);
                        updateObj.putInt("diamonds", player.getDisplayedDiamonds());
                        updateObj.putLong("diamonds_actual", player.getActualDiamonds());
                        break;
                     }

                     if (key.equals("ethereal_currency")) {
                        player.adjustEthCurrency(sender, this, value);
                        updateObj.putInt("ethereal_currency", player.getDisplayedEthCurrency());
                        updateObj.putLong("ethereal_currency_actual", player.getActualEthCurrency());
                        break;
                     }

                     if (key.equals("food")) {
                        player.adjustFood(sender, this, value);
                        updateObj.putInt("food", player.getDisplayedFood());
                        updateObj.putLong("food_actual", player.getActualFood());
                        break;
                     }

                     if (key.equals("xp")) {
                        boolean hasNewLevel = player.rewardXp(sender, this, value);
                        updateObj.putInt("xp", player.getXp());
                        if (hasNewLevel) {
                           updateVars.addSFSObject(updateObj);
                           updateObj = new SFSObject();
                           updateObj.putInt("level", player.getLevel());
                        }
                        break;
                     }

                     if (key.equals("keys")) {
                        player.adjustKeys(sender, this, value);
                        updateObj.putInt("keys", player.getDisplayedKeys());
                        updateObj.putLong("keys_actual", player.getActualKeys());
                        break;
                     }

                     Logger.trace("ERROR: code reward unknown: " + key);
                  }

                  this.ext.stats.trackReward(sender, "redeem_code", key, (long)value);
                  updateVars.addSFSObject(updateObj);
               }
            }
         }
      }
   }

   private void sendKeyGiftError(String errorMsg, User recipient) {
      String cmd = "gs_get_code";
      ISFSObject response = new SFSObject();
      response.putUtfString("cmd", "gs_get_code");
      response.putBool("success", false);
      response.putBool("key_gift", true);
      if (errorMsg != null && !errorMsg.isEmpty()) {
         response.putUtfString("message", errorMsg);
      }

      this.send("gs_get_code", response, recipient);
   }

   private void transferCode(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      ISFSObject response = new SFSObject();
      if (player.getLevel() < GameSettings.getInt("USER_MINIMUM_KEYS_LEVEL")) {
         this.sendKeyGiftError("KEY_GIFT_ERROR_LOW_LEVEL", sender);
      } else if (player.getFriendGift() > MSMExtension.CurrentDBTime()) {
         this.sendKeyGiftError("ALREADY_SENT_CODE_TEXT", sender);
      } else {
         try {
            String sql = "INSERT INTO user_codes SET user=?, code='FRIENDGIFT', code_id=1, date_claimed=NOW() ON DUPLICATE KEY UPDATE code_id=1, date_claimed=NOW()";
            MSMExtension.getInstance().getDB().update(sql, new Object[]{player.getPlayerId()});
            sql = "INSERT INTO user_offer_rewards SET bbb_id=?, source='code', transaction_id=?, resource='keys', amount=1";
            MSMExtension.getInstance().getDB().update(sql, new Object[]{params.getLong("user_id"), player.getBbbId()});
            player.setFriendGift(MSMExtension.CurrentDBTime() + GameSettings.getLong("FRIEND_GIFT_RESET_MINUTES") * 60L * 1000L);
            response.putLong("friend_gift", player.getFriendGift());
            JSONObject metricData = (new JSONObject()).put("friend_bbb_id", params.getLong("user_id")).put("code", "FRIENDGIFT").put("code_id", 1);
            this.ext.stats.sendUserEvent(sender, "give_gift_code", metricData);
         } catch (Exception var7) {
            this.sendKeyGiftError("ERROR_LOADING_DATA", sender);
            Logger.trace(var7, "error looking up user code", "   params : " + params.getDump());
            return;
         }

         response.putUtfString("message", "CODE_TRANSFERRED_TEXT");
         response.putBool("success", true);
         response.putBool("key_gift", true);
         this.send("gs_get_code", response, sender);
      }
   }

   private void updateLastTimedTheme(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      if (params.containsKey("theme_id") && params.containsKey("tut_stage")) {
         player.updateTimedTheme(params.getInt("theme_id"), params.getInt("tut_stage"));
      }

   }

   private void updateIslandTutorials(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      player.updateIslandTutorials(params.getIntArray("island_tutorials"));
   }

   private void adminGetCodes(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminGetCodes: Error! Trying to invoke admin without privileges!");
      } else {
         int codeId = params.getInt("code_id");
         ISFSArray result = CodeLookup.getCodes(codeId);
         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         response.putSFSArray("codes", result);
         this.send("gs_admin_get_codes", response, sender);
      }
   }

   public void serverQuestEvent(User sender, String event, int value) {
      ISFSObject qe = new SFSObject();
      qe.putInt(event, value);
      this.serverQuestEvent(sender, qe);
   }

   public void serverQuestEvent(User sender, String event, long value) {
      ISFSObject qe = new SFSObject();
      qe.putLong(event, value);
      this.serverQuestEvent(sender, qe);
   }

   public void serverQuestEvent(User sender, ISFSObject event) {
      Player player = (Player)sender.getProperty("player_object");
      if (player == null) {
         Logger.trace("serverQuestEvent: Sender is null, user logged out during quest event");
      }

      if (event == null) {
         Logger.trace("serverQuestEvent: event is null, user logged out during quest event");
      }

      PromotionManager.handleQuestEvent(sender, event);
      if (player.hasQuestGoals(event)) {
         this.questEvent(sender, event);
      }

   }

   private void buyEgg(User sender, ISFSObject params) {
      try {
         ISFSObject response = new SFSObject();
         int monsterId = params.getInt("monster_id");
         boolean starpowerPurchase = false;
         if (params.containsKey("starpower_purchase")) {
            starpowerPurchase = params.getBool("starpower_purchase");
         }

         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland pi = player.getActiveIsland();
         Monster monsterType = MonsterLookup.get(monsterId);
         if (pi.hasMax(monsterType)) {
            this.ext.sendErrorResponse("gs_buy_egg", "You already have the max amount of this monster", sender);
            return;
         }

         if (monsterType.isDipster()) {
            this.ext.sendErrorResponse("gs_buy_egg", "You can't buy a dipster egg!", sender);
            return;
         }

         if (monsterType.isBoxMonsterType() && pi.hasInactiveBoxMonster(monsterType.isRare(), monsterType.isEpic())) {
            response.putBool("success", false);
            response.putUtfString("error_msg", "NOTIFICATION_ALREADY_INACTIVE_BOX");
            this.send("gs_buy_egg", response, sender);
            return;
         }

         if (!IslandLookup.get(pi.getType()).hasMonster(monsterId)) {
            response.putBool("success", false);
            response.putUtfString("error_msg", "NOT_AVAILABLE_IN_MARKET");
            this.send("gs_buy_egg", response, sender);
            return;
         }

         if (player.getLevel() < monsterType.getLevelUnlocked()) {
            TutorialGroup t = player.getTutorialGroup();
            if (t == null || !t.clientTutorial().equals("streamlined") || player.getLevel() >= 4 || monsterId != PlayerIslandFactory.streamlinedToejammerMonstId()) {
               response.putBool("success", false);
               response.putUtfString("error_msg", "NOT_AVAILABLE_IN_MARKET");
               this.send("gs_buy_egg", response, sender);
               return;
            }
         }

         long structureId = 0L;
         if (params.containsKey("nursery_id")) {
            structureId = params.getLong("nursery_id");
         } else if (params.containsKey("structure_id")) {
            structureId = params.getLong("structure_id");
         }

         long questClaimId = 0L;
         if (params.containsKey("quest_claim_id")) {
            questClaimId = params.getLong("quest_claim_id");
         }

         ISFSObject monsterCostumeState = null;
         if (params.containsKey("costume")) {
            try {
               MonsterCostumeState mcs = new MonsterCostumeState();
               JSONObject metricsParams = new JSONObject();
               int costumeId = params.getInt("costume");
               if (costumeId > 0) {
                  response.putInt("costume_id", costumeId);
                  CostumeData costumeData = CostumeLookup.get(costumeId);
                  if (costumeData == null) {
                     throw new Exception("Costume not found");
                  }

                  metricsParams.put("monster_id", costumeData.getMonster());
                  metricsParams.put("purchase_type", "monster_with_costume");
                  this.purchaseCostume(sender, player, pi, false, costumeData, mcs, response, metricsParams);
                  mcs.setEquipped(costumeId);
                  monsterCostumeState = mcs.toSFSObject();
                  metricsParams.put("costume", new JSONObject(monsterCostumeState.toJson()));
                  this.ext.stats.trackCostumePurchase(sender, metricsParams);
               }
            } catch (Exception var18) {
               this.ext.sendErrorResponse("gs_buy_egg", var18.getMessage(), sender);
               return;
            }
         }

         this.buyEgg(sender, player, monsterId, pi, structureId, starpowerPurchase, questClaimId, false, "", (ISFSObject)null, monsterCostumeState, response, false);
      } catch (Exception var19) {
         Logger.trace(var19, "error during buy egg", "   params : " + params.getDump());
      }

   }

   private long transferEgg(User sender, Player player, PlayerMonster teleportableMonster, int destMonsterType, PlayerIsland goalIsland, boolean admin) throws Exception {
      MonsterCostumeState costume = teleportableMonster.getCostumeState();
      return this.buyEgg(sender, player, destMonsterType, goalIsland, 0L, false, 0L, true, teleportableMonster.getName(), teleportableMonster.getMegaSFS(), costume != null ? costume.toSFSObject() : null, (ISFSObject)null, admin);
   }

   private long buyEgg(User sender, Player player, int monsterId, PlayerIsland island, long structureId, boolean starpowerPurchase, long questClaimId, boolean teleportMonster, String previousMonsterName, ISFSObject mega, ISFSObject monsterCostumeData, ISFSObject response, boolean admin) throws Exception {
      if (structureId == 0L && island.eggsFull()) {
         this.ext.sendErrorResponse("gs_buy_egg", "Island already has an egg", sender);
         return 0L;
      } else {
         if (structureId != 0L && island.eggHolderHasEgg(structureId)) {
            if (island.eggsFull()) {
               this.ext.sendErrorResponse("gs_buy_egg", "Structure already has an egg", sender);
               return 0L;
            }

            structureId = 0L;
         }

         Monster staticMonsterClaimed = MonsterLookup.get(monsterId);
         boolean questClaimValidated = false;
         int costDiamonds;
         if (questClaimId != 0L) {
            ArrayList<PlayerQuest> quests = player.getQuests();

            for(int i = 0; i < quests.size(); ++i) {
               PlayerQuest quest = (PlayerQuest)quests.get(i);
               if (quest.getId() == questClaimId && quest.isComplete()) {
                  if (quest.collected() == 0) {
                     ISFSObject rewards = quest.getRewards();
                     if (rewards != null && rewards.containsKey("entity") && island.getIndex() != 6) {
                        costDiamonds = rewards.getInt("entity");
                        Island staticIsland = IslandLookup.get(island.getIndex());
                        if (costDiamonds == staticMonsterClaimed.getEntityId()) {
                           if (staticIsland.hasMonster(staticMonsterClaimed.getMonsterID())) {
                              questClaimValidated = true;
                           }
                        } else if (staticIsland.hasMonster(staticMonsterClaimed.getMonsterID())) {
                           Monster monsterReward = MonsterLookup.getFromEntityId(costDiamonds);
                           if (MonsterIslandToIslandMapping.monsterTeleportsToIslandAs(monsterReward.getMonsterID(), staticMonsterClaimed.getMonsterID(), island.getIndex())) {
                              questClaimValidated = true;
                           } else if (MonsterIslandToIslandMapping.monsterTeleportsFromIslandAs(monsterReward.getMonsterID(), staticMonsterClaimed.getMonsterID(), island.getIndex())) {
                              questClaimValidated = true;
                           }
                        }
                     }
                  }
                  break;
               }
            }

            if (!questClaimValidated) {
               this.ext.sendErrorResponse("gs_buy_egg", "Quest Claim not validated", sender);
               return 0L;
            }
         }

         boolean hasCredit = this.hasInventoryIslandItem(player, staticMonsterClaimed.getEntityId());
         if (!questClaimValidated) {
            if (!teleportMonster && !staticMonsterClaimed.canPurchaseFromStore(admin, player, island, starpowerPurchase, VersionData.Instance().getMaxServerVersionFromClientVersion(player.lastClientVersion()))) {
               this.ext.sendErrorResponse("gs_buy_egg", "This item is not available for purchase", sender);
               return 0L;
            }

            if (!teleportMonster && !hasCredit && !staticMonsterClaimed.userAllowedToPurchase(player, admin)) {
               this.ext.sendErrorResponse("gs_buy_egg", "You are not allowed to purchase this item", sender);
               return 0L;
            }
         }

         Island staticIsland = IslandLookup.get(island.getIndex());
         if (island.isBattleIsland() || !island.isGoldIsland() && staticIsland.hasMonster(monsterId)) {
            int costCoins = 0;
            int costEth = 0;
            costDiamonds = 0;
            int costKeys = 0;
            int costStarpower = 0;
            int costRelics = 0;
            int costMedals = 0;
            if (!teleportMonster && !admin && !questClaimValidated) {
               if (!starpowerPurchase) {
                  costKeys = staticMonsterClaimed.getCostKeys(staticIsland.getType());
                  costDiamonds = staticMonsterClaimed.getCostDiamonds(island.getType());
                  costEth = island.isEtherealIsland() ? staticMonsterClaimed.getCostEth(island.getType()) : 0;
                  costCoins = staticMonsterClaimed.getCostCoins(island.getType());
                  costRelics = staticMonsterClaimed.getCostRelics(island.getType());
                  costMedals = staticMonsterClaimed.getCostMedals(island.getType());
               } else {
                  costStarpower = staticMonsterClaimed.getCostStarpower(island.getType());
               }

               if (player.timedEventsUnlocked()) {
                  if (starpowerPurchase && StarSalesEvent.hasTimedEventNow(staticMonsterClaimed, player, staticIsland.getType())) {
                     costStarpower = StarSalesEvent.getTimedEventSaleCost(staticMonsterClaimed, player, staticIsland.getType(), player.lastClientVersion());
                  } else if (EntitySalesEvent.hasTimedEventNow(staticMonsterClaimed, player, staticIsland.getType())) {
                     if (costRelics != 0) {
                        costRelics = EntitySalesEvent.getTimedEventSaleCost(staticMonsterClaimed, player, Player.CurrencyType.Relics, staticIsland.getType());
                     } else if (costKeys != 0) {
                        costKeys = EntitySalesEvent.getTimedEventSaleCost(staticMonsterClaimed, player, Player.CurrencyType.Keys, staticIsland.getType());
                     } else if (costDiamonds != 0) {
                        costDiamonds = EntitySalesEvent.getTimedEventSaleCost(staticMonsterClaimed, player, Player.CurrencyType.Diamonds, staticIsland.getType());
                     } else if (costEth != 0) {
                        costEth = EntitySalesEvent.getTimedEventSaleCost(staticMonsterClaimed, player, Player.CurrencyType.Ethereal, staticIsland.getType());
                     } else if (costCoins != 0) {
                        costCoins = EntitySalesEvent.getTimedEventSaleCost(staticMonsterClaimed, player, Player.CurrencyType.Coins, staticIsland.getType());
                     }
                  }
               }
            }

            if (!hasCredit && !player.canBuy((long)costCoins, (long)costEth, (long)costDiamonds, (long)costStarpower, (long)costKeys, (long)costRelics, costMedals)) {
               this.ext.sendErrorResponse("gs_buy_egg", "You do not have enough currency to buy this egg", sender);
               return 0L;
            } else {
               PlayerEgg newEgg = island.addNewEggToIsland(player, staticMonsterClaimed, structureId, costDiamonds > 0, questClaimValidated, teleportMonster, previousMonsterName, mega, monsterCostumeData, (String)null, admin);
               if (!teleportMonster && !admin) {
                  int sellCost = staticMonsterClaimed.getSecondaryCurrencyCost(player, island, true, island.isAmberIsland());
                  newEgg.setBookValue(sellCost);
               }

               if (response == null) {
                  response = new SFSObject();
               }

               structureId = newEgg.getStructureID();
               if (!admin) {
                  if (hasCredit) {
                     this.consumeInventoryIslandItem(player, staticMonsterClaimed.getEntityId());
                     ((ISFSObject)response).putInt("inventory_used", 1);
                  } else {
                     player.chargePlayer(sender, this, costCoins, costEth, costDiamonds, (long)costStarpower, costKeys, costRelics, costMedals);
                     if (costDiamonds > 0) {
                        this.logDiamondUsage(sender, "buy_egg", costDiamonds, player.getLevel(), MonsterLookup.get(newEgg.getType()).getEntityId());
                     } else if (costStarpower > 0) {
                        this.logStarpowerUsage(sender, "buy_egg", costStarpower, player.getLevel());
                     } else if (costKeys > 0) {
                        this.ext.stats.trackSpend(sender, "buy_egg", "keys", (long)costKeys);
                     }
                  }
               }

               ((ISFSObject)response).putBool("success", true);
               ((ISFSObject)response).putSFSObject("user_egg", newEgg.getData());
               if (questClaimValidated) {
                  ((ISFSObject)response).putLong("quest_claim_id", questClaimId);
               }

               if (staticMonsterClaimed.isBoxMonsterType() && island.hasBuyback() && island.getBuyback().getID() == (long)staticMonsterClaimed.getEntityId()) {
                  island.removeBuyback();
                  ((ISFSObject)response).putBool("remove_buyback", true);
               } else {
                  ((ISFSObject)response).putBool("remove_buyback", false);
               }

               ISFSArray responseVars = new SFSArray();
               player.addPlayerPropertyData(responseVars, admin);
               ((ISFSObject)response).putSFSArray("properties", responseVars);
               if (admin) {
                  this.ext.savePlayerIsland(player, island, false);
                  this.send("gs_admin_buy_egg", (ISFSObject)response, sender);
               } else {
                  this.send("gs_buy_egg", (ISFSObject)response, sender);
                  int monsterType = newEgg.getType();
                  if (MonsterCommonToRareMapping.rareToCommonMapContainsKey(monsterType)) {
                     monsterType = MonsterCommonToRareMapping.rareToCommonMapGet(monsterType).commonMonsterId();
                  }

                  this.serverQuestEvent(sender, "monster", monsterType);
                  this.ext.savePlayer(player);
               }

               if (hasCredit) {
                  this.ext.stats.trackMonsterBuy(sender, newEgg.getType(), new MetricCost(0L, 0, 0L, 0L));
               } else {
                  MetricCost metricCost = new MetricCost((long)costCoins, costDiamonds, (long)costEth, (long)costStarpower, (long)costKeys, (long)costRelics);
                  this.ext.stats.trackMonsterBuy(sender, newEgg.getType(), metricCost);
               }

               GooglePlayEvents.reportMonsterBuy(sender, island.getIndex(), newEgg.getType());
               return structureId;
            }
         } else {
            this.ext.sendErrorResponse("gs_buy_egg", "Invalid monster for island", sender);
            return 0L;
         }
      }
   }

   private void sellEgg(User sender, ISFSObject params) {
      long userEggId = params.getLong("user_egg_id");

      try {
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         PlayerEgg playerEgg = island.getEgg(userEggId);
         if (playerEgg == null) {
            this.ext.sendErrorResponse("gs_sell_egg", "Could not find the egg you're trying to sell", sender);
            return;
         }

         if (playerEgg.getTimeRemaining() > 0L) {
            this.ext.sendErrorResponse("gs_sell_egg", "The egg is not ready to be hatched yet", sender);
            return;
         }

         long structureId = playerEgg.getStructureID();
         PlayerSynthesizingData playerSynthesizingData = island.getSynthesizingData(structureId);
         if (playerSynthesizingData != null) {
            island.removeSynthesizingData(playerSynthesizingData);
            PlayerMonster playerMonster = player.getActiveIsland().getMonsterByID(playerSynthesizingData.getUsedMonster());
            if (playerMonster != null) {
               island.removeMonster(playerMonster, false);
            }
         }

         int monsterId = playerEgg.getType();
         island.removeEgg(userEggId);
         Monster staticMonster = MonsterLookup.get(monsterId);
         boolean inRelics = island.isAmberIsland();
         int monsterCost = false;
         int monsterCost;
         if (playerEgg.bookValue() != -1) {
            monsterCost = playerEgg.bookValue();
         } else {
            monsterCost = staticMonster.getSecondaryCurrencyCost(player, island, true, inRelics);
         }

         double sellPercentage = GameSettings.getDouble("USER_SELLING_PERCENTAGE");
         if (inRelics) {
            sellPercentage = GameSettings.getDouble("USER_VESSEL_RELIC_TRADE_PERCENT");
         }

         monsterCost = (int)((double)monsterCost * sellPercentage);
         ISFSObject costumeState = playerEgg.getCostumeData();
         if (costumeState != null) {
            Collection<Integer> purchasedCostumes = costumeState.getIntArray("p");
            if (!inRelics) {
               Iterator itr = purchasedCostumes.iterator();

               while(itr.hasNext()) {
                  Integer costumeId = (Integer)itr.next();
                  CostumeData data = CostumeLookup.get(costumeId);
                  if (data != null) {
                     monsterCost += data.getSecondaryCurrencySellCost(island);
                  }
               }
            } else {
               monsterCost = (int)((double)monsterCost + (double)purchasedCostumes.size() * GameSettings.getDouble("USER_COSTUME_RELICS_SELL_COST"));
            }
         }

         if (!inRelics) {
            player.paySecondaryCurrencySellingPrice(sender, monsterCost, island, this);
         } else {
            player.adjustRelics(sender, this, monsterCost);
         }

         String sql = "DELETE FROM user_facebook_help_instances WHERE island_id=? AND ( structure_id = 0 OR structure_id = ? ) AND type=?";
         Object[] args = new Object[]{island.getID(), structureId, "nursery"};
         this.ext.getDB().update(sql, args);
         MetricCost metricCost;
         if (!inRelics) {
            metricCost = new MetricCost((long)staticMonster.coinSellingPrice(player, island.getType()), 0, (long)staticMonster.ethSellingPrice(player, island.getType()), 0L);
         } else {
            metricCost = new MetricCost(0L, 0, 0L, 0L, 0L, (long)staticMonster.getSecondaryCurrencyCost(player, island, true, true), 0);
         }

         this.ext.stats.trackMonsterSell(sender, monsterId, 0, metricCost, costumeState, "egg");
         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         response.putLong("user_egg_id", userEggId);
         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_sell_egg", response, sender);
      } catch (Exception var23) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         response.putLong("user_egg_id", userEggId);
         this.send("gs_sell_egg", response, sender);
         Logger.trace(var23, "error deleting egg", "   params : " + params.getDump());
      }

   }

   private void adminHatchNonEgg(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminHatchNonEgg: Error! Trying to invoke admin without privileges!");
      } else {
         try {
            int monsterId = params.getInt("monster_id");
            long userEggId = params.getLong("user_egg_id");
            long structureId = params.getLong("user_structure_id");
            int pos_x = params.getInt("pos_x");
            int pos_y = params.getInt("pos_y");
            int flip = params.getInt("flip");
            Player player = (Player)sender.getProperty("player_object");
            PlayerIsland island = player.getActiveIsland();
            Monster monster = MonsterLookup.get(monsterId);
            int bedsAvailable = island.bedsAvailable();
            if (bedsAvailable != -1) {
               int bedsNeeded = monster.beds();
               Logger.trace("bedsAvailable: " + bedsAvailable);
               Logger.trace("bedsNeeded: " + bedsNeeded);
               if (bedsAvailable < bedsNeeded) {
                  this.ext.sendErrorResponse("gs_hatch_egg", "Not enough beds available in castle", sender);
                  return;
               }
            }

            String name;
            if (params.containsKey("name")) {
               name = params.getUtfString("name");
            } else {
               name = monster.generateRandomMonsterName();
            }

            SFSObject newMonsterData = PlayerMonster.createMonsterSFS(monsterId, name, (ISFSObject)null, (ISFSObject)null, player.getActiveIsland().getIndex(), player.getActiveIslandId(), pos_x, pos_y, flip, player.getNextMonsterIndex(), 1, MSMExtension.CurrentDBTime(), MSMExtension.CurrentDBTime(), false, 0L, (String)null, false, false);
            PlayerMonster newPlayerMonster = new PlayerMonster(newMonsterData, island);
            if (structureId == 0L) {
               PlayerEgg egg = island.getEgg(userEggId);
               structureId = egg.getStructureID();
            }

            island.removeEgg(userEggId);
            island.addMonster(newPlayerMonster);
            Monster staticMonster = MonsterLookup.get(monsterId);
            if (staticMonster.isBoxMonsterType()) {
               newPlayerMonster.setBoxMonsterData(new SFSArray(), island.isGoldIsland());
            }

            if (staticMonster.isEvolvable()) {
               newPlayerMonster.setEvolveDataStatic(new SFSArray());
               newPlayerMonster.setEvolveDataFlex(new SFSArray());
            }

            int xpReward = false;
            int xpReward;
            if (island.isEtherealIslandWithModifiers() && staticMonster.getGenes().length() == 1 && !staticMonster.isBoxMonsterType()) {
               xpReward = (int)(GameSettings.getFloat("USER_ETHEREAL_ISLAND_HATCH_XP_MODIFIER") * (float)MonsterLookup.get(monsterId).getXp());
            } else {
               xpReward = MonsterLookup.get(monsterId).getXp();
            }

            player.rewardXp(sender, this, xpReward);
            if (structureId >= 0L) {
               String sql = "DELETE FROM user_facebook_help_instances WHERE island_id=? AND ( structure_id = 0 OR structure_id = ? ) AND type=?";
               Object[] args = new Object[]{island.getID(), structureId, "nursery"};
               this.ext.getDB().update(sql, args);
            }

            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            response.putSFSObject("monster", newPlayerMonster.toSFSObject(island));
            response.putLong("user_egg_id", userEggId);
            ISFSArray responseVars = new SFSArray();
            player.addPlayerPropertyData(responseVars, true);
            response.putSFSArray("properties", responseVars);
            this.send("gs_hatch_egg", response, sender);
            ISFSObject qe = new SFSObject();
            qe.putInt("object", newPlayerMonster.getEntityId());
            qe.putInt("on_island", player.getActiveIsland().getIndex());
            qe.putInt("genes", MonsterLookup.get(newPlayerMonster.getType()).getGenes().length());
            this.serverQuestEvent(sender, qe);
         } catch (Exception var22) {
            Logger.trace(var22, "error during admin hatch egg", "   params : " + params.getDump());
         }

      }
   }

   private void hatchEgg(User sender, ISFSObject params) {
      try {
         long userEggId = params.getLong("user_egg_id");
         int pos_x = params.getInt("pos_x");
         int pos_y = params.getInt("pos_y");
         int flip = params.getInt("flip");
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland playerIsland = player.getActiveIsland();
         Island staticIsland = IslandLookup.get(playerIsland.getType());
         int monsterLevel = 1;
         String monsterName = null;
         Integer mailIndex = null;
         int monsterId;
         int costEth;
         int costCoins;
         if (params.containsKey("mailId")) {
            long mailId = params.getLong("mailId");

            for(monsterId = 0; monsterId < player.getMailbox().size(); ++monsterId) {
               ISFSObject curMail = player.getMailbox().getSFSObject(monsterId);
               if (SFSHelpers.getLong("user_mail_id", curMail) == mailId) {
                  ISFSObject attachment = curMail.getSFSObject("attachment");
                  if (attachment.getUtfString("type").equals("entity")) {
                     Monster hatchMonst = MonsterLookup.get((int)userEggId);
                     Monster mailMonst = MonsterLookup.getFromEntityId(SFSHelpers.getInt("id", attachment));
                     if (mailMonst != null) {
                        boolean giveEnt = false;
                        if (SFSHelpers.getInt("id", attachment) == hatchMonst.getEntityId()) {
                           if (staticIsland.hasMonster(hatchMonst.getMonsterID())) {
                              giveEnt = true;
                           }
                        } else {
                           costEth = MonsterIslandToIslandMapping.monsterSourceGivenDestIsland(hatchMonst.getMonsterID(), staticIsland.getType());
                           if (costEth != 0) {
                              if (staticIsland.hasMonster(hatchMonst.getMonsterID())) {
                                 giveEnt = true;
                              }
                           } else {
                              costCoins = MonsterIslandToIslandMapping.monsterDestGivenSourceIsland(hatchMonst.getMonsterID(), staticIsland.getType());
                              if (costCoins != 0) {
                                 if (staticIsland.hasMonster(hatchMonst.getMonsterID())) {
                                    giveEnt = true;
                                 }
                              } else if (MonsterIslandToIslandMapping.equivMonstersGivenAnyIsland(hatchMonst.getMonsterID(), mailMonst.getMonsterID()) && staticIsland.hasMonster(hatchMonst.getMonsterID())) {
                                 giveEnt = true;
                              }
                           }
                        }

                        if (giveEnt) {
                           if (attachment.containsKey("level")) {
                              monsterLevel = SFSHelpers.getInt("level", attachment);
                           }

                           if (attachment.containsKey("name")) {
                              monsterName = SFSHelpers.getUtfString("name", attachment);
                           }

                           mailIndex = monsterId;
                           break;
                        }
                     }
                  }
               }
            }

            if (mailIndex == null) {
               this.ext.sendErrorResponse("gs_hatch_egg", "Mail attachment did not match hatch attempt", sender);
               ISFSObject response = new SFSObject();
               response.putUtfString("msg", "NOTIFICATION_NOT_VALID_FOR_ISLAND");
               this.send("gs_display_generic_message", response, sender);
               return;
            }
         }

         ISFSObject response = new SFSObject();
         PlayerEgg playerEgg = playerIsland.getEgg(userEggId);
         int monsterId = false;
         boolean directPlacementMonster = false;
         Monster monster;
         if (playerEgg == null) {
            monsterId = (int)userEggId;
            monster = MonsterLookup.get(monsterId);
            if (mailIndex == null && !playerIsland.isNoNurseryIsland()) {
               if (!monster.isDipster()) {
                  this.ext.sendErrorResponse("gs_hatch_egg", "Could not find the egg ", sender);
                  return;
               }

               directPlacementMonster = true;
            } else {
               directPlacementMonster = true;
            }
         } else {
            monsterId = playerEgg.getType();
            monster = MonsterLookup.get(monsterId);
            ISFSObject costumeState = playerEgg.getCostumeData();
            if (costumeState != null) {
               Collection<Integer> purchasedCostumes = costumeState.getIntArray("p");
               Iterator itr = purchasedCostumes.iterator();

               while(itr.hasNext()) {
                  Integer costumeId = (Integer)itr.next();
                  playerIsland.addToOwnedCostumes(costumeId);
                  response.putLong("island_id", playerIsland.getID());
                  response.putUtfString("costumes_owned", playerIsland.allPrevOwnedCostumes().toJson());
                  this.send("gs_update_owned_costumes", response, sender);
               }
            }
         }

         if (monster == null) {
            this.ext.sendErrorResponse("gs_hatch_egg", "no monster type to place", sender);
            return;
         }

         if (playerIsland.hasMax(monster)) {
            this.ext.sendErrorResponse("gs_hatch_egg", "You already have the max amount of this monster", sender);
            return;
         }

         boolean hasCredit = this.hasInventoryIslandItem(player, monster.getEntityId());
         boolean storeInHotel;
         if (mailIndex == null && directPlacementMonster) {
            if (!monster.canPurchaseFromStore(false, player, playerIsland, false, VersionData.Instance().getMaxServerVersionFromClientVersion(player.lastClientVersion()))) {
               if (!playerIsland.isAmberIsland()) {
                  this.ext.sendErrorResponse("gs_hatch_egg", "This item is not available for purchase", sender);
               } else {
                  storeInHotel = true;
                  if (monster.getViewInMarket() == 1 || EntityAvailabilityEvent.hasTimedEventNow(monster, player, playerIsland.getType())) {
                     storeInHotel = false;
                  }

                  if (storeInHotel) {
                     response.putBool("success", false);
                     response.putInt("entity", monster.getEntityId());
                     response.putUtfString("message", "VESSEL_AVAILABILITY_EXPIRED");
                     this.send("gs_hatch_egg", response, sender);
                  } else {
                     this.ext.sendErrorResponse("gs_hatch_egg", "This item is not available for purchase", sender);
                  }
               }

               return;
            }

            if (!monster.userAllowedToPurchase(player, false) && !hasCredit) {
               this.ext.sendErrorResponse("gs_hatch_egg", "You are not allowed to purchase this item", sender);
               return;
            }
         }

         storeInHotel = false;
         if (params.containsKey("store_in_hotel")) {
            storeInHotel = params.getBool("store_in_hotel");
         }

         int costKeys;
         int costDiamonds;
         if (!storeInHotel) {
            costDiamonds = playerIsland.bedsAvailable();
            if (costDiamonds != -1) {
               costEth = monster.beds();
               if (costDiamonds < costEth) {
                  this.ext.sendErrorResponse("gs_hatch_egg", "Not enough beds available in castle", sender);
                  return;
               }
            }
         } else {
            PlayerStructure hotel = playerIsland.getHotel();
            if (hotel == null) {
               this.ext.sendErrorResponse("gs_hatch_egg", "MSG_HOTEL_NONE", sender);
               return;
            }

            Structure hotelStructure = StructureLookup.get(hotel.getType());
            costCoins = hotelStructure.getExtra().getInt("capacity");
            if (playerIsland.isEtherealIslandWithModifiers()) {
               costCoins = (int)((float)costCoins * GameSettings.getFloat("ETHEREAL_CAPACITY_MULTIPLIER"));
            }

            costKeys = playerIsland.getNumMonsterBedsInStorage();
            if (costKeys + monster.beds() > costCoins) {
               this.ext.sendErrorResponse("gs_hatch_egg", "MSG_HOTEL_FULL", sender);
               return;
            }
         }

         if (playerEgg != null && playerEgg.getTimeRemaining() > 0L) {
            this.ext.sendErrorResponse("gs_hatch_egg", "The egg is not ready to be hatched yet", sender);
            return;
         }

         if (playerIsland.isGoldIsland() || playerIsland.isTribalIsland() || !staticIsland.hasMonster(monsterId)) {
            this.ext.sendErrorResponse("gs_hatch_egg", "Invalid monster for island", sender);
            return;
         }

         costDiamonds = 0;
         costEth = 0;
         costCoins = 0;
         costKeys = 0;
         int costRelics = 0;
         int costMedals = 0;
         ISFSObject monsterCostumeState = null;
         if (mailIndex == null && directPlacementMonster) {
            costDiamonds = monster.getCostDiamonds(playerIsland.getType());
            costEth = monster.getCostEth(playerIsland.getType());
            costCoins = monster.getCostCoins(playerIsland.getType());
            costKeys = monster.getCostKeys(playerIsland.getType());
            costRelics = monster.getCostRelics(playerIsland.getType());
            costMedals = monster.getCostMedals(playerIsland.getType());
            if (player.timedEventsUnlocked() && EntitySalesEvent.hasTimedEventNow(monster, player, playerIsland.getType())) {
               if (costRelics != 0) {
                  costRelics = EntitySalesEvent.getTimedEventSaleCost(monster, player, Player.CurrencyType.Relics, playerIsland.getType());
               } else if (costKeys != 0) {
                  costKeys = EntitySalesEvent.getTimedEventSaleCost(monster, player, Player.CurrencyType.Keys, playerIsland.getType());
               } else if (costDiamonds != 0) {
                  costDiamonds = EntitySalesEvent.getTimedEventSaleCost(monster, player, Player.CurrencyType.Diamonds, playerIsland.getType());
               } else if (costEth != 0) {
                  costEth = EntitySalesEvent.getTimedEventSaleCost(monster, player, Player.CurrencyType.Ethereal, playerIsland.getType());
               } else if (costCoins != 0) {
                  costCoins = EntitySalesEvent.getTimedEventSaleCost(monster, player, Player.CurrencyType.Coins, playerIsland.getType());
               }
            }

            if (!hasCredit && !player.canBuy((long)costCoins, (long)costEth, (long)costDiamonds, 0L, (long)costKeys, (long)costRelics, costMedals)) {
               this.ext.sendErrorResponse("gs_buy_egg", "You do not have enough currency to buy this egg", sender);
               return;
            }

            if (params.containsKey("costume")) {
               try {
                  MonsterCostumeState mcs = new MonsterCostumeState();
                  JSONObject metricsParams = new JSONObject();
                  int costumeId = params.getInt("costume");
                  if (costumeId > 0) {
                     response.putInt("costume_id", costumeId);
                     CostumeData costumeData = CostumeLookup.get(costumeId);
                     if (costumeData == null) {
                        throw new Exception("Costume not found");
                     }

                     metricsParams.put("monster_id", costumeData.getMonster());
                     metricsParams.put("purchase_type", "monster_with_costume");
                     this.purchaseCostume(sender, player, playerIsland, false, costumeData, mcs, response, metricsParams);
                     mcs.setEquipped(costumeId);
                     monsterCostumeState = mcs.toSFSObject();
                     metricsParams.put("costume", new JSONObject(monsterCostumeState.toJson()));
                     this.ext.stats.trackCostumePurchase(sender, metricsParams);
                  }
               } catch (Exception var46) {
                  this.ext.sendErrorResponse("gs_buy_egg", var46.getMessage(), sender);
                  return;
               }
            }
         }

         String name;
         if (params.containsKey("name")) {
            name = params.getUtfString("name");
         } else if (playerEgg != null && !playerEgg.getPreviousName().isEmpty()) {
            name = playerEgg.getPreviousName();
         } else if (monsterName != null && !monsterName.isEmpty()) {
            name = monsterName;
         } else {
            name = monster.generateRandomMonsterName();
         }

         ISFSObject prevPermaMega = playerEgg != null ? playerEgg.getPrevPermaMega() : null;
         ISFSObject prevCostume = playerEgg != null ? playerEgg.getCostumeData() : monsterCostumeState;
         String prevBoxed = playerEgg != null ? playerEgg.getBoxedEggs() : null;
         SFSObject newMonsterData = PlayerMonster.createMonsterSFS(monsterId, name, prevPermaMega, prevCostume, player.getActiveIsland().getIndex(), player.getActiveIslandId(), pos_x, pos_y, flip, player.getNextMonsterIndex(), monsterLevel, MSMExtension.CurrentDBTime(), MSMExtension.CurrentDBTime(), false, 0L, prevBoxed, false, false);
         PlayerMonster newPlayerMonster = new PlayerMonster(newMonsterData, playerIsland);
         long structureId = 0L;
         if (playerEgg != null) {
            structureId = playerEgg.getStructureID();
            playerIsland.removeEgg(userEggId);
            newPlayerMonster.setBookValue(playerEgg.bookValue());
            PlayerSynthesizingData data = playerIsland.getSynthesizingData(structureId);
            if (data != null) {
               playerIsland.removeSynthesizingData(data);
               PlayerMonster playerMonster = player.getActiveIsland().getMonsterByID(data.getUsedMonster());
               if (playerMonster != null) {
                  playerIsland.removeMonster(playerMonster, false);
               }
            }
         }

         playerIsland.addMonster(newPlayerMonster);
         if (directPlacementMonster && monster.isBoxMonsterType() && playerIsland.hasBuyback() && playerIsland.getBuyback().getID() == (long)monster.getEntityId()) {
            playerIsland.removeBuyback();
            response.putBool("remove_buyback", true);
         } else if (playerIsland.hasMax(monster) && playerIsland.hasBuyback() && playerIsland.buybackCountsAs((int)playerIsland.getBuyback().getID(), monster)) {
            playerIsland.removeBuyback();
            response.putBool("remove_buyback", true);
         }

         int xpReward;
         if (directPlacementMonster && (costDiamonds > 0 || costCoins > 0 || costEth > 0 || costKeys > 0 || costRelics > 0 || costMedals > 0)) {
            xpReward = monster.getSecondaryCurrencyCost(player, playerIsland, true, playerIsland.isAmberIsland());
            newPlayerMonster.setBookValue(xpReward);
            if (hasCredit) {
               this.consumeInventoryIslandItem(player, monster.getEntityId());
               response.putInt("inventory_used", 1);
               this.ext.stats.trackMonsterBuy(sender, monsterId, new MetricCost(0L, 0, 0L, 0L));
            } else {
               player.chargePlayer(sender, this, costCoins, costEth, costDiamonds, 0L, costKeys, costRelics, costMedals);
               if (costDiamonds > 0) {
                  this.logDiamondUsage(sender, "buy_egg", costDiamonds, player.getLevel(), newPlayerMonster.getEntityId());
               } else if (costKeys > 0) {
                  this.ext.stats.trackSpend(sender, "buy_egg", "keys", (long)costKeys);
               }

               this.ext.stats.trackMonsterBuy(sender, monsterId, new MetricCost((long)costCoins, costDiamonds, (long)costEth, 0L, (long)costKeys, (long)costRelics, costMedals));
            }
         }

         if ((playerEgg == null || playerEgg.getBoxedEggs() == null) && monster.isBoxMonsterType()) {
            newPlayerMonster.setBoxMonsterData(new SFSArray(), playerIsland.isGoldIsland());
         }

         if (monster.isEvolvable()) {
            newPlayerMonster.setEvolveDataStatic(new SFSArray());
            newPlayerMonster.setEvolveDataFlex(new SFSArray());
         }

         if (newPlayerMonster.isInactiveBoxMonster() && playerIsland.isAmberIsland()) {
            newPlayerMonster.addAmberEggTimer(playerIsland.getType());
         } else if (!newPlayerMonster.isInactiveBoxMonster() && playerIsland.isCelestialIsland()) {
            newPlayerMonster.addCelestialEggTimer(playerIsland.getType());
         } else {
            newPlayerMonster.resetEggTimer();
         }

         if (playerIsland.hasMax(monster) && playerIsland.hasBuyback() && playerIsland.getBuyback().getID() == (long)monster.getEntityId()) {
            playerIsland.removeBuyback();
            response.putBool("remove_buyback", true);
         }

         int xpReward = false;
         if (playerIsland.isBattleIsland()) {
            xpReward = 0;
         } else if (playerIsland.isAmberIsland() && directPlacementMonster) {
            xpReward = 0;
         } else if (playerIsland.isEtherealIslandWithModifiers() && monster.getGenes().length() == 1 && !monster.isBoxMonsterType()) {
            xpReward = (int)(GameSettings.getFloat("USER_ETHEREAL_ISLAND_HATCH_XP_MODIFIER") * (float)MonsterLookup.get(monsterId).getXp());
         } else {
            boolean useAltTutorialXp = false;
            if (player.getLevel() < 4) {
               TutorialGroup t = player.getTutorialGroup();
               if (t != null && t.clientTutorial().equals("streamlined")) {
                  useAltTutorialXp = true;
               }
            }

            if (useAltTutorialXp) {
               xpReward = 150;
            } else {
               xpReward = monster.getXp();
            }
         }

         if (xpReward != 0) {
            player.rewardXp(sender, this, xpReward);
         }

         String sql;
         if (structureId != 0L) {
            sql = "DELETE FROM user_facebook_help_instances WHERE island_id=? AND ( structure_id = 0 OR structure_id = ? ) AND type=?";
            Object[] args = new Object[]{playerIsland.getID(), structureId, "nursery"};
            this.ext.getDB().update(sql, args);
         }

         if (playerIsland.isComposerIsland()) {
            sql = "INSERT INTO user_tracks SET user=?, bintrack='', format=1";
            long trackID = this.ext.getDB().insertGetId(sql, new Object[]{player.getPlayerId()});
            ISFSObject trackObj = new SFSObject();
            trackObj.putLong("user_track_id", trackID);
            trackObj.putLong("user", player.getPlayerId());
            trackObj.putByteArray("bintrack", new byte[0]);
            player.getTracks().addSFSObject(trackObj);
            response.putSFSObject("track_data", trackObj);
            ISFSObject songObject = new SFSObject();
            songObject.putLong("monster", newPlayerMonster.getID());
            songObject.putLong("track", trackID);
            ISFSArray songs = player.getSongs();
            boolean found = false;

            int i;
            ISFSObject song;
            for(i = 0; i < songs.size(); ++i) {
               song = songs.getSFSObject(i);
               if (song.getLong("island") == playerIsland.getID()) {
                  found = true;
                  break;
               }
            }

            if (!found) {
               sql = "INSERT INTO user_songs SET island=?, user=?, tracks='[]'";
               this.ext.getDB().update(sql, new Object[]{playerIsland.getID(), player.getPlayerId()});
            }

            this.ext.initializePlayerMusic(player, new Object[]{player.getPlayerId()});
            songs = player.getSongs();

            for(i = 0; i < songs.size(); ++i) {
               song = songs.getSFSObject(i);
               if (song.getLong("island") == playerIsland.getID()) {
                  song.getSFSArray("tracks").addSFSObject(songObject);
                  response.putSFSObject("song_data", song);
                  sql = "UPDATE user_songs SET tracks=? WHERE island=?";
                  this.ext.getDB().update(sql, new Object[]{song.getSFSArray("tracks").toJson(), playerIsland.getID()});
                  break;
               }
            }
         }

         if (mailIndex != null) {
            player.getMailbox().removeElementAt(mailIndex);
            sql = "UPDATE user_mail SET deleted = 1 WHERE user_mail_id = ?";
            this.ext.getDB().update(sql, new Object[]{params.getLong("mailId")});
         }

         response.putBool("success", true);
         response.putSFSObject("monster", newPlayerMonster.toSFSObject(playerIsland));
         response.putBool("directPlace", directPlacementMonster);
         response.putLong("user_egg_id", userEggId);
         response.putLong("island", playerIsland.getID());
         response.putBool("create_in_storage", storeInHotel);
         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_hatch_egg", response, sender);
         SFSObject storageParams;
         if (!playerIsland.isAmberIsland() || !directPlacementMonster) {
            storageParams = new SFSObject();
            int entityId = newPlayerMonster.getEntityId();
            if (MonsterCommonToRareMapping.rareToCommonMapContainsKey(newPlayerMonster.getType())) {
               int commonType = MonsterCommonToRareMapping.rareToCommonMapGet(newPlayerMonster.getType()).commonMonsterId();
               Monster commonMonster = MonsterLookup.get(commonType);
               entityId = commonMonster.getEntityId();
            }

            storageParams.putInt("object", entityId);
            storageParams.putInt("on_island", player.getActiveIsland().getIndex());
            storageParams.putInt("genes", MonsterLookup.get(newPlayerMonster.getType()).getGenes().length());
            this.serverQuestEvent(sender, storageParams);
         }

         if (storeInHotel) {
            storageParams = new SFSObject();
            storageParams.putLong("user_monster_id", newPlayerMonster.getID());
            this.putMonsterInStorage(sender, storageParams);
         }
      } catch (Exception var47) {
         Logger.trace(var47, "error during hatch egg", "   params : " + params.getDump());
      }

   }

   private void hatchingSpeedUp(User sender, ISFSObject params) {
      try {
         long userEggId = params.getLong("user_egg_id");
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         PlayerEgg playerEgg = island.getEgg(userEggId);
         if (playerEgg == null) {
            this.ext.sendErrorResponse("gs_speed_up_hatching", "Could not find the egg you're trying to sell", sender);
            return;
         }

         if (null != params.getInt("speed_up_type") && 0 != params.getInt("speed_up_type")) {
            if (1 == params.getInt("speed_up_type")) {
               if (!player.hasSpeedUpCredit()) {
                  this.ext.sendErrorResponse("gs_speed_up_hatching", "Unfortunately there are currently no offers available to you. Please try again later", sender);
                  return;
               }

               this.ext.stats.trackSpeedupWithVideo(sender, "speedup_hatch", (long)playerEgg.getType(), playerEgg.getID(), playerEgg.getTimeRemaining(), playerEgg.getInitialTimeRemaining());
               playerEgg.reduceHatchingTimeByVideo();
               player.adjustSpeedUpCredit(sender, this, -1, Player.SPEED_UP_TYPES.VIDEO);
            }
         } else {
            int diamondCost = Game.DiamondsRequiredToComplete(playerEgg.getTimeRemaining());
            if (!player.canBuy(0L, 0L, (long)diamondCost, 0L, 0L, 0L, 0)) {
               this.ext.sendErrorResponse("gs_speed_up_hatching", "You do not have enough diamonds to speed up hatching", sender);
               return;
            }

            if (playerEgg.getTimeRemaining() > 5L) {
               player.chargePlayer(sender, this, 0, 0, diamondCost, 0L, 0, 0, 0);
               JSONObject speedupInfo = MSMStats.extraSpeedupInfo(playerEgg.getTimeRemaining(), playerEgg.getInitialTimeRemaining(), playerEgg.getType());
               this.logDiamondUsage(sender, "speedup_hatch", diamondCost, player.getLevel(), speedupInfo, MonsterLookup.get(playerEgg.getType()).getEntityId());
               playerEgg.finishHatchingNow();
            }
         }

         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         response.putLong("user_egg_id", userEggId);
         response.putLong("hatches_on", playerEgg.getCompletionTime());
         response.putLong("laid_on", playerEgg.getStartTime());
         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_speed_up_hatching", response, sender);
      } catch (Exception var10) {
         Logger.trace(var10, "error during hatching speedup", "   params : " + params.getDump());
      }

   }

   private void feedMonster(User sender, ISFSObject params) {
      try {
         long playerMonsterId = params.getLong("user_monster_id");
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland activeIsland = player.getActiveIsland();
         PlayerMonster playerMonster = activeIsland.getMonsterByID(playerMonsterId);
         if (playerMonster == null || activeIsland.isTribalIsland()) {
            this.ext.sendErrorResponse("gs_feed_monster", "Cannot find monster to feed, monsterId: " + playerMonsterId, sender);
            return;
         }

         if (activeIsland.noMonsterLevellingIsland()) {
            int maxLevel = GameSettings.getInt("MAX_UNDERLING_LEVEL");
            if (playerMonster.getLevel() >= maxLevel) {
               this.ext.sendErrorResponse("gs_feed_monster", "Cannot feed monsters on underling island, yet", sender);
               return;
            }
         }

         Monster staticMonster = MonsterLookup.get(playerMonster.getType());
         int maxLevel = GameSettings.getInt("MAX_MONSTER_LEVEL");
         if (playerMonster.getLevel() >= maxLevel) {
            this.ext.sendErrorResponse("gs_feed_monster", "no", sender);
            return;
         }

         ISFSObject staticMonsterLevel = staticMonster.getLevel(playerMonster.getLevel());
         if (!player.canFeed((long)staticMonsterLevel.getInt("food"))) {
            this.ext.sendErrorResponse("gs_feed_monster", "You don't have enough food to feed this monster", sender);
            return;
         }

         player.adjustFood(sender, this, -staticMonsterLevel.getInt("food"));
         boolean leveledUp = playerMonster.feed(player, player.getActiveIsland());
         if (leveledUp) {
            int monsterType = staticMonster.getMonsterID();
            if (MonsterCommonToRareMapping.rareToCommonMapContainsKey(monsterType)) {
               monsterType = MonsterCommonToRareMapping.rareToCommonMapGet(monsterType).commonMonsterId();
            }

            SFSObject qe;
            if (player.hasQuestGoal("breedable_tut_isl") && playerMonster.getLevel() >= 4 && !activeIsland.isTribalIsland() && !activeIsland.isAmberIsland() && !MonsterCommonToEpicMapping.epicToCommonMapContainsKey(monsterType) && !staticMonster.isEtherealMonster() && !staticMonster.isLegendaryMonster() && !staticMonster.isBoxMonsterType() && !staticMonster.isSeasonalMonster()) {
               Iterator monstersOnIsland = activeIsland.getMonsters().iterator();

               while(monstersOnIsland.hasNext()) {
                  PlayerMonster itr = (PlayerMonster)monstersOnIsland.next();
                  if (itr.getID() != playerMonster.getID() && itr.getLevel() >= 4) {
                     int secondMonstType = itr.getType();
                     if (MonsterCommonToRareMapping.rareToCommonMapContainsKey(secondMonstType)) {
                        secondMonstType = MonsterCommonToRareMapping.rareToCommonMapGet(secondMonstType).commonMonsterId();
                     }

                     Monster mType = MonsterLookup.get(secondMonstType);
                     if (!MonsterCommonToEpicMapping.epicToCommonMapContainsKey(secondMonstType) && !mType.isEtherealMonster() && !mType.isLegendaryMonster() && !mType.isBoxMonsterType() && !mType.isSeasonalMonster()) {
                        qe = new SFSObject();
                        qe.putInt("breedable_tut_isl", monsterType);
                        this.serverQuestEvent(sender, qe);
                        qe = new SFSObject();
                        qe.putInt("breedable_tut_isl", secondMonstType);
                        this.serverQuestEvent(sender, qe);
                        break;
                     }
                  }
               }
            }

            qe = new SFSObject();
            qe.putInt("on_monster", monsterType);
            qe.putInt("monster_level", playerMonster.getLevel());
            this.serverQuestEvent(sender, qe);
            this.ext.stats.trackMonsterLevelUp(sender, monsterType, playerMonster.getID(), playerMonster.getLevel());
         }

         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         this.send("gs_feed_monster", response, sender);
         response = new SFSObject();
         response.putLong("user_monster_id", playerMonster.getID());
         response.putInt("times_fed", playerMonster.getTimesFed());
         if (leveledUp) {
            response.putInt("level", playerMonster.getLevel());
            response.putLong("last_collection", playerMonster.getLastCollectedTime());
            response.putInt("collected_coins", playerMonster.getCollectedCoins());
            response.putInt("collected_ethereal", playerMonster.getCollectedEth());
            response.putDouble("collected_relics", (double)playerMonster.getCollectedRelics());
            if (activeIsland.noMonsterLevellingIsland()) {
               response.putInt("collected_diamonds", playerMonster.getCollectedDiamonds());
               response.putInt("collected_food", playerMonster.getCollectedFood());
               response.putInt("collected_starpower", playerMonster.getCollectedStarpower());
               response.putInt("collected_keys", playerMonster.getCollectedKeys());
            }
         }

         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_update_monster", response, sender);
      } catch (Exception var17) {
         Logger.trace(var17, "error during feed monster", "   params : " + params.getDump());
      }

   }

   private void muteMonster(User sender, ISFSObject params) {
      try {
         long playerMonsterId = params.getLong("user_monster_id");
         Player player = (Player)sender.getProperty("player_object");
         PlayerMonster playerMonster = player.getActiveIsland().getMonsterByID(playerMonsterId);
         if (playerMonster == null || player.getActiveIsland().isTribalIsland() && player.getActiveIsland().getTribalRequests() == null) {
            this.ext.sendErrorResponse("gs_mute_monster", "Cannot find monster to mute", sender);
            return;
         }

         playerMonster.toggleMute();
         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         this.send("gs_mute_monster", response, sender);
         response = new SFSObject();
         response.putLong("user_monster_id", playerMonster.getID());
         response.putInt("muted", playerMonster.getMuted());
         this.send("gs_update_monster", response, sender);
         if (playerMonster.getMuted() != 0) {
            this.serverQuestEvent(sender, "mute_monster", 1);
         }
      } catch (Exception var8) {
         Logger.trace(var8, "error during mute monster", "   params : " + params.getDump());
      }

   }

   private void flipMonster(User sender, ISFSObject params) {
      try {
         long playerMonsterId = params.getLong("user_monster_id");
         boolean toggle = true;
         boolean flipped = false;
         if (params.containsKey("flipped")) {
            toggle = false;
            flipped = params.getBool("flipped");
         }

         Player player = (Player)sender.getProperty("player_object");
         PlayerMonster playerMonster = player.getActiveIsland().getMonsterByID(playerMonsterId);
         if (playerMonster == null) {
            this.ext.sendErrorResponse("gs_flip_monster", "Cannot find monster to flip", sender);
            return;
         }

         if (toggle) {
            playerMonster.toggleFlip();
         } else {
            playerMonster.setFlip(flipped);
         }

         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         this.send("gs_flip_monster", response, sender);
         response = new SFSObject();
         response.putLong("user_monster_id", playerMonster.getID());
         response.putInt("flip", playerMonster.getFlip());
         this.send("gs_update_monster", response, sender);
         this.serverQuestEvent(sender, "flip_monster", 1);
      } catch (Exception var10) {
         Logger.trace(var10, "error during flip monster", "   params : " + params.getDump());
      }

   }

   private void moveMonster(User sender, ISFSObject params) {
      try {
         long playerMonsterId = params.getLong("user_monster_id");
         int posX = params.getInt("pos_x");
         int posY = params.getInt("pos_y");
         float volume = 1.0F;
         if (params.containsKey("volume")) {
            volume = params.getDouble("volume").floatValue();
         }

         Player player = (Player)sender.getProperty("player_object");
         PlayerMonster playerMonster = player.getActiveIsland().getMonsterByID(playerMonsterId);
         if (playerMonster == null || player.getActiveIsland().isTribalIsland() && player.getActiveIsland().getTribalRequests() == null) {
            this.ext.sendErrorResponse("gs_move_monster", "Cannot find monster to move", sender);
            return;
         }

         playerMonster.setPosition(posX, posY);
         playerMonster.setVolume(volume);
         ISFSObject questEvent = new SFSObject();
         questEvent.putInt("move_object", playerMonster.getEntityId());
         this.serverQuestEvent(sender, questEvent);
         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         this.send("gs_move_monster", response, sender);
         response = new SFSObject();
         response.putLong("user_monster_id", playerMonster.getID());
         response.putInt("pos_x", playerMonster.getXPosition());
         response.putInt("pos_y", playerMonster.getYPosition());
         response.putDouble("volume", (double)playerMonster.getVolume());
         this.send("gs_update_monster", response, sender);
      } catch (Exception var12) {
         Logger.trace(var12, "error during move monster", "   params : " + params.getDump());
      }

   }

   private void sellMonster(User sender, ISFSObject params) {
      long playerMonsterId = params.getLong("user_monster_id");
      Player player = (Player)sender.getProperty("player_object");
      PlayerIsland island = player.getActiveIsland();

      try {
         boolean pureDestroy = params.containsKey("pure_destroy") ? params.getBool("pure_destroy") : false;
         this.sellMonster(sender, player, playerMonsterId, pureDestroy, island, true, true, false, false);
      } catch (Exception var9) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         response.putLong("user_monster_id", playerMonsterId);
         this.send("gs_sell_monster", response, sender);
         Logger.trace(var9, "error deleting monster", "   params : " + params.getDump());
      }

   }

   private void sellMonster(User sender, Player player, long playerMonsterId, boolean pureDestroy, PlayerIsland fromIsland, boolean addToSoldMonsters, boolean withSellResponse, boolean urnExpiry, boolean admin) throws Exception {
      if (admin) {
         this.adminSellMonster(sender, player, playerMonsterId, pureDestroy, fromIsland, addToSoldMonsters, withSellResponse, urnExpiry);
      } else {
         PlayerMonster playerMonster = fromIsland.getMonsterByID(playerMonsterId);
         if (playerMonster != null && !fromIsland.isTribalIsland()) {
            if (fromIsland.isAmberIsland() && fromIsland.monsterBeingEvolvedInCrucible(playerMonsterId)) {
               this.ext.sendErrorResponse("gs_sell_monster", "Trying to sell evolving monster", sender);
            } else {
               SFSObject monsterParentIslandData = playerMonster.getParentIslandData();
               PlayerMonster goldMonstToDelete;
               if (monsterParentIslandData != null) {
                  PlayerIsland parentIsland = player.getIslandByID(monsterParentIslandData.getLong("island"));
                  long parentMonsterId = monsterParentIslandData.getLong("monster");
                  if (parentIsland != null) {
                     goldMonstToDelete = parentIsland.getMonsterByID(parentMonsterId);
                     if (goldMonstToDelete != null) {
                        goldMonstToDelete.setGoldIslandData(0L, 0L);
                     }

                     this.ext.savePlayerIsland(player, parentIsland, false);
                  }
               }

               Long goldIslandMonsterIdDeleted = null;
               SFSObject monsterGoldIslandData = playerMonster.getGoldIslandData();
               SFSObject monsterUpdateResponse;
               if (monsterGoldIslandData != null) {
                  PlayerIsland goldIsland = player.getIslandByIslandIndex(6);
                  goldIslandMonsterIdDeleted = monsterGoldIslandData.getLong("monster");
                  goldMonstToDelete = goldIsland.getMonsterByID(goldIslandMonsterIdDeleted);
                  if (goldMonstToDelete != null) {
                     goldIsland.removeMonster(goldMonstToDelete, addToSoldMonsters);
                     this.ext.savePlayerIsland(player, goldIsland, false);
                     if (goldIsland.soldMonsterTypes() != null && addToSoldMonsters) {
                        monsterUpdateResponse = new SFSObject();
                        monsterUpdateResponse.putLong("island_id", goldIsland.getID());
                        monsterUpdateResponse.putUtfString("monsters_sold", goldIsland.soldMonsterTypes().toJson());
                        this.send("gs_update_sold_monsters", monsterUpdateResponse, sender);
                     }
                  }
               }

               SFSObject response;
               boolean inRelics;
               if (!urnExpiry && !fromIsland.isGoldIsland() && !fromIsland.isTribalIsland()) {
                  response = new SFSObject();
                  ISFSObject monsterUpdateResponse = new SFSObject();
                  inRelics = false;

                  try {
                     if (fromIsland.canStandardCollect(playerMonster)) {
                        inRelics = fromIsland.standardCollectFromMonster(playerMonster, player, sender, this, response, monsterUpdateResponse, false);
                        response.putBool("success", inRelics);
                        this.send("gs_collect_monster", response, sender);
                     }

                     if (playerMonster.isInactiveBoxMonster() && fromIsland.isAmberIsland()) {
                        this.sellMonster(sender, player, playerMonsterId, true, fromIsland, false, true, true, false);
                        monsterUpdateResponse = null;
                     }
                  } catch (Exception var28) {
                     inRelics = false;
                     Logger.trace(var28, "error during collectFromMonster");
                  }

                  if (inRelics && monsterUpdateResponse != null) {
                     ISFSArray responseVars = new SFSArray();
                     player.addPlayerPropertyData(responseVars, false);
                     monsterUpdateResponse.putSFSArray("properties", responseVars);
                     this.send("gs_update_monster", monsterUpdateResponse, sender);
                  }
               }

               fromIsland.removeMonster(playerMonster, addToSoldMonsters);
               if (fromIsland.isALastBredMonster(playerMonster.getID())) {
                  fromIsland.setLastBred(0L, 0L);
               }

               this.ext.savePlayerIsland(player, fromIsland, false);
               response = new SFSObject();
               if (fromIsland.soldMonsterTypes() != null && addToSoldMonsters) {
                  response.putLong("island_id", fromIsland.getID());
                  response.putUtfString("monsters_sold", fromIsland.soldMonsterTypes().toJson());
                  this.send("gs_update_sold_monsters", response, sender);
               }

               int j;
               if (!pureDestroy && !player.getActiveIsland().isGoldIsland()) {
                  Monster staticMonster = MonsterLookup.get(playerMonster.getType());
                  inRelics = player.getActiveIsland().isAmberIsland();
                  int originalMonsterPrice = false;
                  int originalMonsterPrice;
                  if (playerMonster.bookValue() != -1) {
                     originalMonsterPrice = playerMonster.bookValue();
                  } else {
                     originalMonsterPrice = staticMonster.getSecondaryCurrencyCost(player, fromIsland, true, inRelics);
                  }

                  int monsterCost = originalMonsterPrice;
                  int wublinBuybackPrice = originalMonsterPrice;
                  double sellPercentage;
                  if (!inRelics) {
                     int allEggsBuyingPrice = 0;
                     if (MonsterLookup.get(playerMonster.getType()).isBoxMonsterType()) {
                        allEggsBuyingPrice = playerMonster.boxedEggsBuyPrice(fromIsland);
                     }

                     monsterCost = originalMonsterPrice + allEggsBuyingPrice;
                     if (!playerMonster.isInactiveBoxMonster()) {
                        wublinBuybackPrice = monsterCost;
                     }

                     allEggsBuyingPrice = 0;
                     if (!playerMonster.isInactiveBoxMonster() && MonsterLookup.get(playerMonster.getType()).isEvolvable()) {
                        allEggsBuyingPrice = playerMonster.evolveEggsBuyPrice(fromIsland);
                     }

                     monsterCost += allEggsBuyingPrice;
                     sellPercentage = GameSettings.getDouble("USER_SELLING_PERCENTAGE");
                  } else {
                     sellPercentage = GameSettings.getDouble("USER_VESSEL_RELIC_TRADE_PERCENT");
                  }

                  monsterCost = (int)((double)monsterCost * sellPercentage);
                  wublinBuybackPrice = (int)((double)wublinBuybackPrice * sellPercentage);
                  MonsterCostumeState costumeState = playerMonster.getCostumeState();
                  if (costumeState != null) {
                     Collection<Integer> purchasedCostumes = costumeState.getPurchased();
                     if (!inRelics) {
                        Iterator itr = purchasedCostumes.iterator();

                        while(itr.hasNext()) {
                           Integer costumeId = (Integer)itr.next();
                           CostumeData costume = CostumeLookup.get(costumeId);
                           if (costume != null) {
                              monsterCost += costume.getSecondaryCurrencySellCost(fromIsland);
                              wublinBuybackPrice += costume.getSecondaryCurrencySellCost(fromIsland);
                           }
                        }
                     } else {
                        monsterCost = (int)((double)monsterCost + (double)purchasedCostumes.size() * GameSettings.getDouble("USER_COSTUME_RELICS_SELL_COST"));
                        wublinBuybackPrice = (int)((double)wublinBuybackPrice + (double)purchasedCostumes.size() * GameSettings.getDouble("USER_COSTUME_RELICS_SELL_COST"));
                     }
                  }

                  if (!inRelics) {
                     player.paySecondaryCurrencySellingPrice(sender, monsterCost, fromIsland, this);
                  } else {
                     player.adjustRelics(sender, this, monsterCost);
                  }

                  j = fromIsland.isEtherealIsland() ? monsterCost : 0;
                  int costCoins = fromIsland.isEtherealIsland() ? 0 : monsterCost;
                  MetricCost metricCost;
                  if (!inRelics) {
                     metricCost = new MetricCost((long)costCoins, 0, (long)j, 0L);
                  } else {
                     metricCost = new MetricCost(0L, 0, 0L, 0L, 0L, (long)staticMonster.getSecondaryCurrencyCost(player, fromIsland, true, true), 0);
                  }

                  ISFSObject metricCostumeState = costumeState != null ? costumeState.toSFSObject() : null;
                  this.ext.stats.trackMonsterSell(sender, playerMonster.getType(), playerMonster.getLevel(), metricCost, metricCostumeState, "monster");
                  if (!staticMonster.isWubbox() && (playerMonster.isInactiveBoxMonster() || playerMonster.isEvolvable())) {
                     monsterCost = wublinBuybackPrice;
                  }

                  if (!fromIsland.isComposerIsland()) {
                     if (!fromIsland.isAmberIsland()) {
                        fromIsland.setBuyback(new PlayerBuyback(playerMonster, monsterCost, playerMonster.isInactiveBoxMonster() && !staticMonster.isWubbox()));
                     } else if (!playerMonster.isInactiveBoxMonster()) {
                        fromIsland.setBuyback(new PlayerBuyback(playerMonster, monsterCost, playerMonster.isInactiveBoxMonster() && !staticMonster.isWubbox()));
                     }
                  }
               }

               if (fromIsland.isComposerIsland()) {
                  ISFSArray songs = player.getSongs();

                  for(int i = 0; i < songs.size(); ++i) {
                     ISFSObject song = songs.getSFSObject(i);
                     if (song.getLong("island") == fromIsland.getID()) {
                        ISFSArray tracks = song.getSFSArray("tracks");
                        ISFSObject removeTrack = null;

                        for(int j = 0; j < tracks.size(); ++j) {
                           ISFSObject curTrack = tracks.getSFSObject(j);
                           if (SFSHelpers.getLong("monster", curTrack) == playerMonster.getID()) {
                              removeTrack = curTrack;
                              tracks.removeElementAt(j);
                              break;
                           }
                        }

                        String sql = "UPDATE user_songs SET tracks=? WHERE island=?";
                        this.ext.getDB().update(sql, new Object[]{tracks.toJson(), fromIsland.getID()});
                        if (removeTrack != null) {
                           long trackID = SFSHelpers.getLong("track", removeTrack);

                           for(j = 0; j < player.getTracks().size(); ++j) {
                              ISFSObject track = player.getTracks().getSFSObject(j);
                              if (track.getLong("user_track_id") == trackID) {
                                 player.getTracks().removeElementAt(j);
                                 break;
                              }
                           }

                           sql = "DELETE FROM user_tracks WHERE user_track_id=?";
                           this.ext.getDB().update(sql, new Object[]{trackID});
                        }
                        break;
                     }
                  }
               }

               SFSArray responseVars;
               if (withSellResponse) {
                  response = new SFSObject();
                  response.putBool("success", true);
                  if (fromIsland.hasHappinessTree()) {
                     responseVars = this.addHappyTreeEffects(fromIsland, sender);
                     response.putSFSArray("monster_happy_effects", responseVars);
                  }

                  response.putLong("user_monster_id", playerMonsterId);
                  if (urnExpiry) {
                     response.putInt("urn_expiry", 1);
                  }

                  if (goldIslandMonsterIdDeleted != null) {
                     response.putLong("user_gi_monster_id", goldIslandMonsterIdDeleted);
                  }

                  if (fromIsland.hasBuyback()) {
                     response.putSFSObject("buyback_properties", fromIsland.getBuyback().toSFSObject());
                  }

                  responseVars = new SFSArray();
                  player.addPlayerPropertyData(responseVars, false);
                  response.putSFSArray("properties", responseVars);
                  this.send("gs_sell_monster", response, sender);
               } else {
                  if (goldIslandMonsterIdDeleted != null) {
                     response = new SFSObject();
                     response.putBool("success", true);
                     response.putLong("user_gi_monster_id", goldIslandMonsterIdDeleted);
                     responseVars = new SFSArray();
                     player.addPlayerPropertyData(responseVars, false);
                     response.putSFSArray("properties", responseVars);
                     this.send("gs_sell_monster", response, sender);
                  }

                  if (fromIsland.hasHappinessTree()) {
                     responseVars = this.addHappyTreeEffects(fromIsland, sender);
                     monsterUpdateResponse = new SFSObject();
                     monsterUpdateResponse.putLong("user_monster_id", playerMonster.getID());
                     monsterUpdateResponse.putSFSArray("monster_happy_effects", responseVars);
                     this.send("gs_update_monster", monsterUpdateResponse, sender);
                  }
               }

            }
         } else {
            this.ext.sendErrorResponse("gs_sell_monster", "Could not find the monster you're trying to sell", sender);
         }
      }
   }

   private void adminSellMonster(User sender, Player player, long playerMonsterId, boolean pureDestroy, PlayerIsland island, boolean addToSoldMonsters, boolean withSellResponse, boolean urnExpiry) {
      try {
         PlayerMonster playerMonster = island.getMonsterByID(playerMonsterId);
         if (playerMonster == null) {
            this.ext.sendErrorResponse("gs_admin_destroy_users_monster", "Could not find the monster you're trying to destroy", sender);
            return;
         }

         ISFSObject response = new SFSObject();
         SFSObject goldData = playerMonster.getGoldIslandData();
         if (goldData != null) {
            Long goldMonsterId = goldData.getLong("monster");
            PlayerIsland goldIsland = player.getIslandByIslandIndex(6);
            if (goldIsland != null) {
               PlayerMonster goldMonster = goldIsland.getMonsterByID(goldMonsterId);
               if (goldMonster != null) {
                  response.putLong("user_gi_monster_id", goldMonster.getID());
                  goldIsland.removeMonster(goldMonster, true);
                  this.ext.savePlayerIsland(player, goldIsland, false);
               }
            }
         }

         SFSObject parentData = playerMonster.getParentIslandData();
         if (parentData != null) {
            Long parentIslandId = parentData.getLong("island");
            Long parentMonsterId = parentData.getLong("monster");
            PlayerIsland parentIsland = player.getIslandByID(parentIslandId);
            if (parentIsland != null) {
               PlayerMonster parentMonster = parentIsland.getMonsterByID(parentMonsterId);
               if (parentMonster != null) {
                  parentMonster.setGoldIslandData(0L, 0L);
                  this.ext.savePlayerIsland(player, parentIsland, false);
               }
            }
         }

         island.removeMonster(playerMonster, true);
         this.ext.savePlayerIsland(player, island, false);
         response.putBool("success", true);
         response.putLong("user_monster_id", playerMonsterId);
         this.send("gs_admin_destroy_users_monster", response, sender);
      } catch (Exception var18) {
         this.ext.sendErrorResponse("gs_admin_destroy_users_monster", "Error admin destroying monster", sender);
      }

   }

   public void collectFromMonster(User sender, ISFSObject params) {
      long playerMonsterId = params.getLong("user_monster_id");
      Player player = (Player)sender.getProperty("player_object");
      PlayerIsland island = player.getActiveIsland();
      if (!island.isGoldIsland() && !island.isTribalIsland()) {
         ISFSObject collectResponse = new SFSObject();
         SFSArray responseVars;
         if (playerMonsterId != -1L) {
            PlayerMonster playerMonster = island.getMonsterByID(playerMonsterId);
            if (playerMonster == null) {
               this.ext.sendErrorResponse("gs_collect_monster", "Could not find the monster you're trying to collect", sender);
               return;
            }

            ISFSObject monsterUpdateResponse = new SFSObject();
            boolean success = false;

            try {
               if (island.canStandardCollect(playerMonster)) {
                  success = island.standardCollectFromMonster(playerMonster, player, sender, this, collectResponse, monsterUpdateResponse, false);
               } else if (island.isExpiryCollection(playerMonster)) {
                  success = island.collectExpiryFromMonster(playerMonster, player, sender, (GameStateHandler)null, collectResponse, monsterUpdateResponse, false);
                  if (playerMonster.isInactiveBoxMonster() && island.isAmberIsland()) {
                     this.sellMonster(sender, player, playerMonsterId, true, island, false, true, true, false);
                     monsterUpdateResponse = null;
                  }
               }
            } catch (Exception var14) {
               success = false;
               Logger.trace(var14, "error during collectFromMonster", "params : " + params.getDump());
            }

            collectResponse.putBool("success", success);
            this.send("gs_collect_monster", collectResponse, sender);
            if (success && monsterUpdateResponse != null) {
               responseVars = new SFSArray();
               player.addPlayerPropertyData(responseVars, false);
               monsterUpdateResponse.putSFSArray("properties", responseVars);
               this.send("gs_update_monster", monsterUpdateResponse, sender);
            }
         } else {
            long previousCollectAll = player.lastCollectAll();
            SFSArray monsterUpdateResponses = new SFSArray();
            responseVars = new SFSArray();
            player.collectAllFromIsland(player, island, sender, this, responseVars, monsterUpdateResponses);
            if (monsterUpdateResponses.size() > 0) {
               collectResponse.putSFSArray("monster_collections", responseVars);
               collectResponse.putSFSArray("update_monster_list", monsterUpdateResponses);
               collectResponse.putLong("island", island.getID());
               collectResponse.putLong("last_collect_all", player.lastCollectAll());
               collectResponse.putBool("success", true);
               ISFSArray responseVars = new SFSArray();
               player.addPlayerPropertyData(responseVars, false);
               collectResponse.putSFSArray("properties", responseVars);
               SFSObject metricData = new SFSObject();
               metricData.putLong("previous_collect_all", previousCollectAll);
               metricData.putLong("last_collect_all", player.lastCollectAll());
               metricData.putInt("num_monsters_collected", responseVars.size());
               this.ext.stats.trackCollectAll(sender, metricData);
            } else {
               collectResponse.putBool("success", false);
            }

            this.send("gs_collect_multi_monster", collectResponse, sender);
         }

      } else {
         this.ext.sendErrorResponse("gs_collect_monster", "You cannot collect from a monster on Gold or Tribal Island.", sender);
      }
   }

   private void testCollectFromMonster(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         ISFSObject response = new SFSObject();
         response.putUtfString("msg", "Error! Trying to invoke admin without privileges!");
         this.send("gs_display_generic_message", response, sender);
         this.ext.sendErrorResponse("gs_test_collect_monster", "Error! Trying to invoke admin without privileges!", sender);
      } else {
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         if (!island.isGoldIsland() && !island.isTribalIsland()) {
            long playerMonsterId = params.getLong("user_monster_id");

            try {
               if (playerMonsterId == -1L) {
                  this.ext.sendErrorResponse("gs_test_collect_monster", "Cannot run test on collectAll.", sender);
                  return;
               }

               PlayerMonster playerMonster = island.getMonsterByID(playerMonsterId);
               if (playerMonster == null) {
                  this.ext.sendErrorResponse("gs_collect_monster", "Could not find the monster you're trying to collect", sender);
                  return;
               }

               ISFSObject response = new SFSObject();
               island.testCollectFromMonster(playerMonster, player, sender, this, response);
               response.putBool("success", true);
               this.send("gs_test_collect_monster", response, sender);
            } catch (Exception var9) {
               Logger.trace(var9, "error breeding monsters", "   params : " + params.getDump());
            }

         } else {
            this.ext.sendErrorResponse("gs_collect_monster", "You cannot collect from a monster on Gold or Tribal Island.", sender);
         }
      }
   }

   private void nameMonster(User sender, ISFSObject params) {
      long playerMonsterId = params.getLong("user_monster_id");
      Player player = (Player)sender.getProperty("player_object");
      PlayerMonster playerMonster = player.getActiveIsland().getMonsterByID(playerMonsterId);
      String monsterName = Helpers.sanitizeName(params.getUtfString("name"), " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!¨\"#$&'()*+,-./:;<=>?@{}0123456789|£©¿®`~^ÀÁÂÄÇÈÉÊËÌÍÎÏÑÒÓÔÖÙÚÛÜßàáâäçèéêëìíîïñòóôöùúûü_ÆæÃãÕõАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя€₽¡");
      if (playerMonster != null && !player.getActiveIsland().isTribalIsland()) {
         playerMonster.setName(monsterName);
         PlayerMonster goldMonster = playerMonster.getGoldMonster(player);
         if (goldMonster != null) {
            goldMonster.setName(monsterName);
         }

         this.ext.stats.trackMonsterRename(sender, playerMonster);
         this.serverQuestEvent(sender, "rename_monster", 1);
      } else {
         this.ext.sendErrorResponse("rename_monster", "Cannot find monster to rename", sender);
      }
   }

   private void tribalFeedMonster(User sender, ISFSObject params) {
      try {
         long playerMonsterId = params.getLong("user_monster_id");
         String type = params.getUtfString("type");
         Player player = (Player)sender.getProperty("player_object");
         PlayerMonster playerMonster = player.getActiveIsland().getMonsterByID(playerMonsterId);
         if (playerMonster == null || !player.getActiveIsland().isTribalIsland()) {
            this.ext.sendErrorResponse("gs_tribal_feed_monster", "Cannot find monster to tribal feed, monsterId: " + playerMonsterId, sender);
            return;
         }

         if (playerMonster.getID() != player.getPlayerId()) {
            this.ext.sendErrorResponse("gs_tribal_feed_monster", "You can't feed other players monsters", sender);
            return;
         }

         if (player.getActiveIsland().getTribalIslandData().getLong("ends_on") <= MSMExtension.CurrentDBTime()) {
            this.ext.sendErrorResponse("gs_tribal_feed_monster", "You can't feed your monster until the tribal reset happens", sender);
            return;
         }

         int amount = 2500 * (playerMonster.getLevel() + 4) * playerMonster.getLevel();
         if (type.equals("food")) {
            amount /= 10;
            if (amount < 1) {
               amount = 1;
            }

            if (player.getActualFood() < (long)amount) {
               this.ext.sendErrorResponse("gs_tribal_feed_monster", "You don't have enough food to feed this monster", sender);
               return;
            }

            player.adjustFood(sender, this, -amount);
         } else if (type.equals("coins")) {
            if (player.getActualCoins() < (long)amount) {
               this.ext.sendErrorResponse("gs_tribal_feed_monster", "You don't have enough coins to feed this monster", sender);
               return;
            }

            player.chargePlayer(sender, this, amount, 0, 0, 0L, 0, 0, 0);
         } else if (type.equals("ethereal")) {
            amount /= 10000;
            if (amount < 1) {
               amount = 1;
            }

            if (player.getActualEthCurrency() < (long)amount) {
               this.ext.sendErrorResponse("gs_tribal_feed_monster", "You don't have enough shards to feed this monster", sender);
               return;
            }

            player.chargePlayer(sender, this, 0, amount, 0, 0L, 0, 0, 0);
         } else {
            if (!type.equals("diamonds")) {
               this.ext.sendErrorResponse("gs_tribal_feed_monster", "Unknown currency type to feed the monster with", sender);
               return;
            }

            amount /= 37500;
            if (amount < 1) {
               amount = 1;
            }

            if (player.getActualDiamonds() < (long)amount) {
               this.ext.sendErrorResponse("gs_tribal_feed_monster", "You don't have enough diamonds to feed this monster", sender);
               return;
            }

            player.chargePlayer(sender, this, 0, 0, amount, 0L, 0, 0, 0);
            this.logDiamondUsage(sender, "tribal_monster_feed", amount, player.getLevel(), playerMonster.getEntityId());
         }

         boolean leveledUp = playerMonster.tribalFeed();
         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         response.putUtfString("type", type);
         response.putBool("update_rank", leveledUp);
         this.send("gs_tribal_feed_monster", response, sender);
         response = new SFSObject();
         response.putLong("user_monster_id", playerMonster.getID());
         response.putInt("times_fed", playerMonster.getTimesFed());
         ISFSArray responseVars = new SFSArray();
         if (leveledUp) {
            String sql = "UPDATE user_tribal_monsters SET level=IF(reset=0, ?, 1), last_updated=NOW() WHERE user_monster_id=?";
            this.ext.getDB().update(sql, new Object[]{playerMonster.getLevel(), playerMonsterId});
            response.putInt("level", playerMonster.getLevel());
            player.calculateEarnedStarpower();
         }

         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_update_monster", response, sender);
      } catch (Exception var13) {
         Logger.trace(var13, "error during tribal feed monster", "params : " + params.getDump());
      }

   }

   private void adminNameMonster(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminNameMonster: Error! Trying to invoke admin without privileges!");
      } else {
         Player player = (Player)sender.getProperty("friend_object");
         long userIslandId = params.getLong("user_island_id");
         PlayerIsland playerIsland = player.getIslandByID(userIslandId);
         long playerMonsterId = params.getLong("user_monster_id");
         PlayerMonster playerMonster = playerIsland.getMonsterByID(playerMonsterId);
         if (playerMonster == null) {
            this.ext.sendErrorResponse("rename_monster", "Cannot find monster to rename", sender);
         } else {
            playerMonster.setName(params.getUtfString("name"));

            try {
               this.ext.savePlayerIsland(player, playerIsland, false);
               ISFSObject response = new SFSObject();
               response.putLong("user_monster_id", playerMonsterId);
               response.putUtfString("boxed_eggs", "");
               this.send("gs_admin_update_users_monster", response, sender);
            } catch (Exception var11) {
               Logger.trace(var11, "error activating box monster", "   params : " + params.getDump());
            }

         }
      }
   }

   private void adminBoxActivateMonster(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminBoxActivateMonster: Error! Trying to invoke admin without privileges!");
      } else {
         Player player = (Player)sender.getProperty("friend_object");
         long userIslandId = params.getLong("user_island_id");
         PlayerIsland playerIsland = player.getIslandByID(userIslandId);
         long playerMonsterId = params.getLong("user_monster_id");
         PlayerMonster boxMonster = playerIsland.getMonsterByID(playerMonsterId);
         if (boxMonster == null) {
            this.ext.sendErrorResponse("gs_box_activate_monster", "Could not find the monster you're trying to purchase fill", sender);
         } else {
            boolean success = false;
            ISFSObject response = new SFSObject();
            boolean evolutionActivation = false;

            try {
               if (playerIsland.isAmberIsland() && playerIsland.eggsFull()) {
                  this.ext.sendErrorResponse("gs_box_activate_monster", "Island already has an egg", sender);
                  return;
               }

               if (params.containsKey("validate_only") && params.getInt("validate_only") == 1) {
                  response.putBool("success", success);
                  if (!boxMonster.hasAllRequiredEggs(playerIsland.isGoldIsland())) {
                     response.putBool("validated", false);
                  } else {
                     response.putLong("nursery_id", playerIsland.getAvailableEmptyNursery());
                     response.putBool("validated", true);
                  }

                  response.putLong("user_monster_id", playerMonsterId);
                  this.send("gs_box_activate_monster", response, sender);
                  return;
               }

               evolutionActivation = boxMonster.isEvolvable();
               int prevMonstType = boxMonster.getType();
               if (boxMonster.activateBoxMonster(sender, this, player, playerIsland)) {
                  success = true;
                  if (evolutionActivation) {
                     playerIsland.addToSoldMonsters(prevMonstType);
                  }

                  this.ext.savePlayerIsland(player, playerIsland, false);
               }
            } catch (Exception var22) {
               Logger.trace(var22, "error activating box monster", "   params : " + params.getDump());
            }

            if (success) {
               if (!playerIsland.isAmberIsland()) {
                  response.putBool("success", success);
                  response.putLong("user_monster_id", playerMonsterId);
                  if (evolutionActivation) {
                     response.putBool("evolution", true);
                  }

                  this.send("gs_box_activate_monster", response, sender);
                  response = new SFSObject();
                  response.putLong("user_monster_id", playerMonsterId);
                  if (evolutionActivation) {
                     response.putUtfString("has_evolve_reqs", "");
                     response.putUtfString("has_evolve_flexeggs", "");
                  } else {
                     response.putUtfString("boxed_eggs", "");
                  }

                  response.putLong("last_collection", boxMonster.getLastCollectedTime());
                  response.putInt("book_value", boxMonster.bookValue());
                  response.putLong("egg_timer_start", boxMonster.eggTimerStart());
                  this.send("gs_admin_update_users_monster", response, sender);
                  if (playerIsland.soldMonsterTypes() != null) {
                     response = new SFSObject();
                     response.putLong("island_id", playerIsland.getID());
                     response.putUtfString("monsters_sold", playerIsland.soldMonsterTypes().toJson());
                     this.send("gs_update_sold_monsters", response, sender);
                  }
               } else {
                  Monster staticMonster = MonsterLookup.get(boxMonster.getType());
                  String previousMonsterName = boxMonster.getName();
                  ISFSObject mega = boxMonster.getMegaSFS();
                  ISFSObject costumes = boxMonster.getCostumeSFS();
                  String boxedEggsStr = "";
                  int sellCost = boxMonster.bookValue();

                  try {
                     this.sellMonster(sender, player, playerMonsterId, true, playerIsland, false, true, false, true);
                     PlayerEgg newEgg = playerIsland.addNewEggToIsland(player, staticMonster, 0L, false, false, false, previousMonsterName, mega, costumes, boxedEggsStr, false);
                     newEgg.setBookValue(sellCost);
                     response.putBool("success", true);
                     response.putSFSObject("user_egg", newEgg.getData());
                     ISFSArray responseVars = new SFSArray();
                     player.addPlayerPropertyData(responseVars, true);
                     response.putSFSArray("properties", responseVars);
                     this.send("gs_admin_buy_egg", response, sender);
                     this.ext.savePlayer(player);
                  } catch (Exception var21) {
                     Logger.trace(var21, "error activating box monster", "   params : " + params.getDump());
                  }
               }
            } else {
               response.putBool("success", success);
               response.putLong("user_monster_id", playerMonsterId);
               this.send("gs_box_activate_monster", response, sender);
            }

         }
      }
   }

   private void adminFinishedEdit(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminFinishedEdit: Error! Trying to invoke admin without privileges!");
      } else {
         Player player = (Player)sender.getProperty("friend_object");
         if (player != null) {
            MSMExtension.getInstance().markAccountAdminEditingComplete(player.getBbbId());
         }

      }
   }

   private void attemptEarlyBoxActivate(User sender, ISFSObject params) {
      long playerMonsterId = params.getLong("user_monster_id");
      Player player = (Player)sender.getProperty("player_object");
      PlayerMonster boxMonster = player.getActiveIsland().getMonsterByID(playerMonsterId);
      if (boxMonster == null) {
         ISFSObject response = new SFSObject();
         response.putUtfString("cmd", "gs_attempt_early_box_activate");
         response.putBool("success", false);
         response.putUtfString("message", "Cannot find box monster to activate");
         response.putLong("user_monster_id", playerMonsterId);
         this.ext.send("gs_attempt_early_box_activate", response, sender);
      } else {
         PlayerIsland playerIsland = player.getActiveIsland();
         if (!playerIsland.isCelestialIsland()) {
            ISFSObject response = new SFSObject();
            response.putUtfString("cmd", "gs_attempt_early_box_activate");
            response.putBool("success", false);
            response.putUtfString("message", "Cannot early activate on this island");
            response.putLong("user_monster_id", playerMonsterId);
            this.ext.send("gs_attempt_early_box_activate", response, sender);
         } else {
            Monster staticMonster = MonsterLookup.get(boxMonster.getType());
            SFSObject response;
            if (staticMonster == null) {
               response = new SFSObject();
               response.putUtfString("cmd", "gs_attempt_early_box_activate");
               response.putBool("success", false);
               response.putUtfString("message", "Invalid monster");
               response.putLong("user_monster_id", playerMonsterId);
               this.ext.send("gs_attempt_early_box_activate", response, sender);
            } else if (!boxMonster.isEvolvable()) {
               response = new SFSObject();
               response.putUtfString("cmd", "gs_attempt_early_box_activate");
               response.putBool("success", false);
               response.putUtfString("message", "Invalid monster to evolve");
               response.putLong("user_monster_id", playerMonsterId);
               this.ext.send("gs_attempt_early_box_activate", response, sender);
            } else if (boxMonster.evolveExpired(playerIsland.getType())) {
               response = new SFSObject();
               response.putUtfString("cmd", "gs_attempt_early_box_activate");
               response.putBool("success", false);
               response.putUtfString("message", "EARLY_AWAKEN_ON_EXPIRED_MONSTER");
               response.putLong("user_monster_id", playerMonsterId);
               this.ext.send("gs_attempt_early_box_activate", response, sender);
            } else {
               boolean success = false;
               boolean attemptSucceeded = false;
               int numEggWildcardsReceived = 0;
               ISFSObject response = new SFSObject();
               int keyCost = GameSettings.get("USER_CELESTIAL_EARLY_ASCEND_RARE_KEY_COST", 10);
               if (player.canBuy(0L, 0L, 0L, 0L, (long)keyCost, 0L, 0)) {
                  player.chargePlayer(sender, this, 0, 0, 0, 0L, keyCost, 0, 0);
                  attemptSucceeded = boxMonster.attemptEarlyEvolve(player, playerIsland);
                  if (attemptSucceeded) {
                     boxMonster.fillEggs(playerIsland, this, sender);
                     boxMonster.setUnlockCelestialPowerup(true);
                     this.boxActivateMonster(sender, params);
                  } else {
                     double filledPercent = boxMonster.evolvingCelestialFillValue(player, playerIsland.getType());
                     boxMonster.runEarlyEvolveFailureEggCanning(playerIsland.getType(), player);
                     ++numEggWildcardsReceived;
                     double random = gRandom.nextDouble();
                     if ((filledPercent - 0.65D) * 5.0D >= random) {
                        ++numEggWildcardsReceived;
                     }

                     if ((filledPercent - 0.75D) * 5.0D >= random) {
                        ++numEggWildcardsReceived;
                     }

                     player.adjustEggWildcards(sender, this, numEggWildcardsReceived);
                  }

                  success = true;
               }

               response.putBool("success", success);
               response.putBool("attempt_success", attemptSucceeded);
               response.putInt("num_egg_wildcards", numEggWildcardsReceived);
               response.putLong("user_monster_id", playerMonsterId);
               ISFSArray responseVars = new SFSArray();
               player.addPlayerPropertyData(responseVars, false);
               response.putSFSArray("properties", responseVars);
               this.ext.stats.TrackEarlyBoxMonsterActivation(sender, boxMonster, numEggWildcardsReceived, attemptSucceeded, keyCost, playerIsland.isGoldIsland());
               this.send("gs_attempt_early_box_activate", response, sender);
               if (!attemptSucceeded) {
                  response = new SFSObject();
                  response.putLong("user_monster_id", boxMonster.getID());
                  if (boxMonster.isInactiveBoxMonster()) {
                     response.putUtfString("boxed_eggs", boxMonster.boxedEggs().toJson());
                  } else if (boxMonster.isEvolvable()) {
                     response.putUtfString("has_evolve_reqs", boxMonster.evolveReqsMetStatic().toJson());
                     response.putUtfString("has_evolve_flexeggs", boxMonster.evolveReqsMetFlex().toJson());
                  }

                  this.send("gs_update_monster", response, sender);
               }

            }
         }
      }
   }

   private void boxActivateMonster(User sender, ISFSObject params) {
      long playerMonsterId = params.getLong("user_monster_id");
      Player player = (Player)sender.getProperty("player_object");
      PlayerIsland playerIsland = player.getActiveIsland();
      PlayerMonster boxMonster = playerIsland.getMonsterByID(playerMonsterId);
      if (boxMonster == null) {
         this.ext.sendErrorResponse("gs_box_activate_monster", "Cannot find box monster to activate", sender);
      } else {
         boolean success = false;
         ISFSObject response = new SFSObject();
         Monster staticMonster = MonsterLookup.get(boxMonster.getType());
         boolean evolutionActivation = boxMonster.isEvolvable();

         try {
            if (playerIsland.isAmberIsland() && playerIsland.eggsFull()) {
               this.ext.sendErrorResponse("gs_box_activate_monster", "Island already has an egg", sender);
               return;
            }

            if (evolutionActivation && playerIsland.isUnderlingIsland() && !boxMonster.isUnderlingEvolveUnlocked()) {
               this.ext.sendErrorResponse("gs_box_activate_monster", "Rare Wublin not unlocked before gs_box_activate_monster", sender);
               return;
            }

            if (evolutionActivation && playerIsland.isCelestialIsland() && !boxMonster.isCelestialPowerupUnlocked()) {
               this.ext.sendErrorResponse("gs_box_activate_monster", "Adult Celestial not unlocked before gs_box_activate_monster", sender);
               return;
            }

            if (params.containsKey("validate_only") && params.getInt("validate_only") == 1) {
               response.putBool("success", success);
               if (!boxMonster.hasAllRequiredEggs(playerIsland.isGoldIsland())) {
                  response.putBool("validated", false);
               } else {
                  response.putLong("nursery_id", playerIsland.getAvailableEmptyNursery());
                  response.putBool("validated", true);
               }

               response.putLong("user_monster_id", playerMonsterId);
               this.send("gs_box_activate_monster", response, sender);
               return;
            }

            int prevMonstType = boxMonster.getType();
            if (boxMonster.activateBoxMonster(sender, this, player, playerIsland)) {
               success = true;
               if (evolutionActivation) {
                  playerIsland.addToSoldMonsters(prevMonstType);
               }

               player.setDirty(true);
            }
         } catch (Exception var20) {
            Logger.trace(var20, "error activating box monster", "   params : " + params.getDump());
         }

         if (success) {
            if (!playerIsland.isAmberIsland()) {
               response.putBool("success", success);
               response.putLong("user_monster_id", playerMonsterId);
               if (evolutionActivation) {
                  response.putBool("evolution", true);
               }

               this.send("gs_box_activate_monster", response, sender);
               response = new SFSObject();
               response.putLong("user_monster_id", playerMonsterId);
               if (evolutionActivation) {
                  response.putUtfString("has_evolve_reqs", "");
                  response.putUtfString("has_evolve_flexeggs", "");
               } else {
                  response.putUtfString("boxed_eggs", "");
               }

               response.putLong("last_collection", boxMonster.getLastCollectedTime());
               response.putInt("book_value", boxMonster.bookValue());
               response.putLong("egg_timer_start", boxMonster.eggTimerStart());
               this.send("gs_update_monster", response, sender);
               if (playerIsland.soldMonsterTypes() != null) {
                  response = new SFSObject();
                  response.putLong("island_id", playerIsland.getID());
                  response.putUtfString("monsters_sold", playerIsland.soldMonsterTypes().toJson());
                  this.send("gs_update_sold_monsters", response, sender);
               }
            } else {
               String previousMonsterName = boxMonster.getName();
               ISFSObject mega = boxMonster.getMegaSFS();
               ISFSObject costumes = boxMonster.getCostumeSFS();
               String boxedEggsStr = "";
               int sellCost = boxMonster.bookValue();

               try {
                  this.sellMonster(sender, player, playerMonsterId, true, playerIsland, false, true, false, false);
                  PlayerEgg newEgg = playerIsland.addNewEggToIsland(player, staticMonster, 0L, false, false, false, previousMonsterName, mega, costumes, boxedEggsStr, false);
                  this.calculateCostumeHatchingResult(player, newEgg, playerIsland.getType());
                  newEgg.setBookValue(sellCost);
                  response.putBool("success", true);
                  response.putSFSObject("user_egg", newEgg.getData());
                  ISFSArray responseVars = new SFSArray();
                  player.addPlayerPropertyData(responseVars, false);
                  response.putSFSArray("properties", responseVars);
                  this.send("gs_buy_egg", response, sender);
                  this.ext.savePlayer(player);
               } catch (Exception var19) {
                  Logger.trace(var19, "error activating box monster", "   params : " + params.getDump());
               }
            }
         } else {
            response.putBool("success", success);
            response.putLong("user_monster_id", playerMonsterId);
            this.send("gs_box_activate_monster", response, sender);
         }

      }
   }

   private void unlockedCrucible(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      long playerStructureId = params.getLong("user_structure_id");
      int unlockStage = params.getInt("unlock_stage");
      PlayerIsland playerIsland = player.getActiveIsland();
      if (!playerIsland.isAmberIsland()) {
         this.ext.sendErrorResponse("gs_start_amber_evolve", "Evolve attempted on non-amber island", sender);
      } else {
         ISFSObject response = new SFSObject();
         boolean success = false;

         try {
            playerIsland.unlockCrucibleStage(playerStructureId, unlockStage);
            success = true;
            PlayerCrucibleData c = playerIsland.getCrucibleData(playerStructureId);
            if (c != null) {
               response.putSFSObject("user_crucible", c.getData());
            }
         } catch (Exception var11) {
            Logger.trace(var11, "error starting amber evolve", "   params : " + params.getDump());
         }

         response.putBool("success", success);
         this.ext.stats.trackCrucibleUnlock(sender, response);
         this.send("gs_viewed_cruc_unlock", response, sender);
      }
   }

   private void testCrucEvolve(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         ISFSObject response = new SFSObject();
         response.putUtfString("msg", "Error! Trying to invoke admin without privileges!");
         this.send("gs_display_generic_message", response, sender);
         this.ext.sendErrorResponse("gs_test_cruc_evolv", "Error! Trying to invoke admin without privileges!", sender);
      } else {
         Player player = (Player)sender.getProperty("player_object");
         long playerMonsterId = params.getLong("user_monster_id");
         long playerStructureId = params.getLong("user_structure_id");
         int newHeatLevel = params.getInt("heat_level");
         PlayerMonster monster = player.getActiveIsland().getMonsterByID(playerMonsterId);
         if (monster == null) {
            this.ext.sendErrorResponse("gs_test_cruc_evolv", "Cannot find monster to evolve", sender);
         } else {
            PlayerIsland playerIsland = player.getActiveIsland();
            if (!playerIsland.isAmberIsland()) {
               this.ext.sendErrorResponse("gs_test_cruc_evolv", "Evolve attempted on non-amber island", sender);
            } else {
               PlayerStructure pStruct = playerIsland.getStructureByID(playerStructureId);
               Structure staticStruct = StructureLookup.get(pStruct.getType());
               ISFSObject extra = staticStruct.getExtra();
               int supportedHeatLevels = PlayerCrucibleData.defaultNumCrucibleHeatLevels();
               if (extra != null && extra.containsKey("h")) {
                  supportedHeatLevels = extra.getInt("h");
               }

               if (newHeatLevel >= 1 && newHeatLevel <= supportedHeatLevels) {
                  boolean success = false;
                  ISFSObject response = new SFSObject();
                  PlayerCrucibleData c = playerIsland.getCrucibleData(playerStructureId);
                  if (monster.isAmberEvolvable()) {
                     try {
                        c = playerIsland.testEvolve(playerStructureId, monster, MSMExtension.CurrentDBTime(), newHeatLevel, params.getBool("flagA"), params.getBool("flagB"), params.getBool("flagC"), params.getBool("flagD"), params.getBool("flagE"), params.getBool("flagN"), playerIsland.getType(), player, response);
                        success = true;
                     } catch (Exception var19) {
                        Logger.trace(var19, "error starting amber evolve, gs_test_cruc_evolv", "   params : " + params.getDump());
                     }
                  }

                  if (success) {
                     response.putBool("success", success);
                     response.putSFSObject("user_crucible", c.getData());
                     this.collectFromMonster(sender, params);
                     ISFSArray responseVars = new SFSArray();
                     player.addPlayerPropertyData(responseVars, false);
                     response.putSFSArray("properties", responseVars);
                     this.send("gs_test_cruc_evolv", response, sender);
                     response = new SFSObject();
                     response.putLong("user_monster_id", playerMonsterId);
                     response.putLong("last_collection", monster.getLastCollectedTime());
                     this.send("gs_update_monster", response, sender);
                  } else {
                     response.putBool("success", success);
                     response.putLong("user_monster_id", playerMonsterId);
                     this.send("gs_test_cruc_evolv", response, sender);
                  }

               } else {
                  this.ext.sendErrorResponse("gs_test_cruc_evolv", "Invalid heat level", sender);
               }
            }
         }
      }
   }

   private void startAmberEvolve(User sender, ISFSObject params) {
      long playerMonsterId = params.getLong("user_monster_id");
      long playerStructureId = params.getLong("user_structure_id");
      int newHeatLevel = params.getInt("heat_level");
      Player player = (Player)sender.getProperty("player_object");
      PlayerIsland playerIsland = player.getActiveIsland();
      boolean isAdmin = false;
      if (params.containsKey("is_admin")) {
         isAdmin = params.getBool("is_admin");
      }

      if (isAdmin) {
         if (sender.getPrivilegeId() != 3) {
            this.ext.sendErrorResponse("gs_start_amber_evolve", "Error! Trying to invoke admin without privileges!", sender);
            return;
         }

         player = (Player)sender.getProperty("friend_object");
         playerIsland = player.getIslandByID(params.getLong("user_island_id"));
         if (playerIsland == null) {
            this.ext.sendErrorResponse("gs_start_amber_evolve", "Cannot find island", sender);
            return;
         }
      }

      PlayerMonster monster = playerIsland.getMonsterByID(playerMonsterId);
      if (monster == null) {
         this.ext.sendErrorResponse("gs_start_amber_evolve", "Cannot find monster to evolve", sender);
      } else if (!playerIsland.isAmberIsland()) {
         this.ext.sendErrorResponse("gs_start_amber_evolve", "Evolve attempted on non-amber island", sender);
      } else {
         PlayerStructure pStruct = playerIsland.getStructureByID(playerStructureId);
         Structure staticStruct = StructureLookup.get(pStruct.getType());
         ISFSObject extra = staticStruct.getExtra();
         int supportedHeatLevels = PlayerCrucibleData.defaultNumCrucibleHeatLevels();
         if (extra != null && extra.containsKey("h")) {
            supportedHeatLevels = extra.getInt("h");
         }

         if (newHeatLevel >= 1 && newHeatLevel <= supportedHeatLevels) {
            boolean success = false;
            ISFSObject response = new SFSObject();
            ISFSObject metricData = new SFSObject();
            PlayerCrucibleData c = playerIsland.getCrucibleData(playerStructureId);
            if (monster.isAmberEvolvable()) {
               try {
                  if (!isAdmin) {
                     int relicCost = PlayerCrucibleData.getHeatCost(c, newHeatLevel, playerIsland.getType(), supportedHeatLevels);
                     int keyCost = monster.getEvolveKeyCost(newHeatLevel == PlayerCrucibleData.overheatingLevel);
                     if ((long)relicCost <= player.getActualRelics() && (long)keyCost <= player.getActualKeys()) {
                        c = playerIsland.startCrucibleEvolve(playerStructureId, monster, MSMExtension.CurrentDBTime(), newHeatLevel, playerIsland.getType(), player);
                        player.chargePlayer(sender, this, 0, 0, 0, 0L, keyCost, 0, 0);
                        player.chargePlayer(sender, this, 0, 0, 0, 0L, 0, relicCost, 0);
                        metricData.putInt("key_cost", keyCost);
                        metricData.putInt("relic_cost", relicCost);
                        metricData.putLong("relics_remaining", player.getActualRelics());
                        metricData.putLong("keys_remaining", player.getActualKeys());
                        metricData.putInt("heat", newHeatLevel);
                        metricData.putInt("monster_id", monster.getType());
                        metricData.putInt("monster_level", monster.getLevel());
                        metricData.putSFSObject("crucible_data", c.getData());
                        success = true;
                     }
                  } else {
                     c = playerIsland.startCrucibleEvolve(playerStructureId, monster, MSMExtension.CurrentDBTime(), newHeatLevel, playerIsland.getType(), player);
                     this.ext.savePlayerIsland(player, playerIsland, false);
                     success = true;
                  }
               } catch (Exception var22) {
                  Logger.trace(var22, "error starting amber evolve", "   params : " + params.getDump());
               }
            }

            if (success) {
               response.putBool("success", success);
               response.putSFSObject("user_crucible", c.getData());
               if (!isAdmin) {
                  this.collectFromMonster(sender, params);
               } else {
                  response.putBool("admin", true);
               }

               ISFSArray responseVars = new SFSArray();
               player.addPlayerPropertyData(responseVars, isAdmin);
               response.putSFSArray("properties", responseVars);
               this.ext.stats.trackCrucibleStartEvolve(sender, metricData);
               this.send("gs_start_amber_evolve", response, sender);
               response = new SFSObject();
               response.putLong("user_monster_id", playerMonsterId);
               response.putLong("last_collection", monster.getLastCollectedTime());
               if (!isAdmin) {
                  this.send("gs_update_monster", response, sender);
               } else {
                  this.send("gs_admin_update_users_monster", response, sender);
               }
            } else {
               response.putBool("success", success);
               response.putLong("user_monster_id", playerMonsterId);
               this.send("gs_start_amber_evolve", response, sender);
            }

         } else {
            this.ext.sendErrorResponse("gs_start_amber_evolve", "Invalid heat level", sender);
         }
      }
   }

   private void speedupAmberEvolve(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      PlayerIsland playerIsland = player.getActiveIsland();
      boolean isAdmin = false;
      if (params.containsKey("is_admin")) {
         isAdmin = params.getBool("is_admin");
      }

      if (isAdmin) {
         if (sender.getPrivilegeId() != 3) {
            this.ext.sendErrorResponse("gs_speedup_amber_evolve", "Error! Trying to invoke admin without privileges!", sender);
            return;
         }

         player = (Player)sender.getProperty("friend_object");
         playerIsland = player.getIslandByID(params.getLong("user_island_id"));
         if (playerIsland == null) {
            this.ext.sendErrorResponse("gs_start_amber_evolve", "Cannot find island", sender);
            return;
         }
      }

      long playeStructureId = params.getLong("user_structure_id");
      PlayerStructure structure = playerIsland.getStructureByID(playeStructureId);
      if (structure == null) {
         this.ext.sendErrorResponse("gs_speedup_amber_evolve", "Cannot find structure to speedup", sender);
      } else if (!playerIsland.isAmberIsland()) {
         this.ext.sendErrorResponse("gs_speedup_amber_evolve", "Evolve attempted on non-amber island", sender);
      } else {
         PlayerCrucibleData c = playerIsland.getCrucibleData(playeStructureId);
         if (null != params.getInt("speed_up_type") && 0 != params.getInt("speed_up_type")) {
            if (1 == params.getInt("speed_up_type")) {
               if (!player.hasSpeedUpCredit()) {
                  this.ext.sendErrorResponse("gs_speedup_amber_evolve", "Unfortunately there are currently no offers available to you. Please try again later", sender);
                  return;
               }

               this.ext.stats.trackSpeedupWithVideo(sender, "speedup_cruc_evolving", (long)c.getNewMonster(), c.getUserMonsterId(), c.getTimeRemaining(), c.getInitialTimeRemaining());
               c.reduceEvolvingTimeByVideo();
               player.adjustSpeedUpCredit(sender, this, -1, Player.SPEED_UP_TYPES.VIDEO);
            }
         } else {
            int diamondCost = Game.DiamondsRequiredToComplete(c.getTimeRemaining());
            if (!player.canBuy(0L, 0L, (long)diamondCost, 0L, 0L, 0L, 0)) {
               this.ext.sendErrorResponse("gs_speedup_amber_evolve", "You do not have enough diamonds to speed up breeding", sender);
               return;
            }

            if (c.getTimeRemaining() > 5L) {
               player.chargePlayer(sender, this, 0, 0, diamondCost, 0L, 0, 0, 0);
               JSONObject speedupInfo = MSMStats.extraSpeedupInfo(c.getTimeRemaining(), c.getInitialTimeRemaining(), c.getNewMonster());
               int newEntityId = c.getNewMonster() != 0 ? MonsterLookup.get(c.getNewMonster()).getEntityId() : -1;
               this.logDiamondUsage(sender, "speedup_cruc_evolving", diamondCost, player.getLevel(), speedupInfo, newEntityId);
               c.finishEvolvingNow();
            }
         }

         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         response.putLong("user_structure_id", playeStructureId);
         response.putSFSObject("user_crucible", c.getData());
         if (isAdmin) {
            response.putBool("admin", true);
         }

         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, isAdmin);
         response.putSFSArray("properties", responseVars);
         this.send("gs_speedup_amber_evolve", response, sender);
      }
   }

   private void viewedInCrucible(User sender, ISFSObject params) {
      try {
         long playerStructureId = params.getLong("user_structure_id");
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland playerIsland = player.getActiveIsland();
         boolean isAdmin = false;
         if (params.containsKey("is_admin")) {
            isAdmin = params.getBool("is_admin");
         }

         if (isAdmin) {
            if (sender.getPrivilegeId() != 3) {
               this.ext.sendErrorResponse("gs_viewed_cruc_monst", "Admin Crucible operation attempted on non admin account", sender);
               return;
            }

            if (!params.containsKey("user_island_id")) {
               this.ext.sendErrorResponse("gs_viewed_cruc_monst", "Crucible operation  attempted on undefined island", sender);
               return;
            }

            player = (Player)sender.getProperty("friend_object");
            playerIsland = player.getIslandByID(params.getLong("user_island_id"));
         }

         if (!playerIsland.isAmberIsland()) {
            this.ext.sendErrorResponse("gs_viewed_cruc_monst", "Crucible operation attempted on non-amber island", sender);
            return;
         }

         PlayerStructure structure = playerIsland.getStructureByID(playerStructureId);
         if (structure == null) {
            this.ext.sendErrorResponse("gs_viewed_cruc_monst", "Cannot find crucible structure", sender);
            return;
         }

         PlayerCrucibleData c = playerIsland.getCrucibleData(playerStructureId);
         PlayerMonster monst = null;
         long playerMonsterId = 0L;
         if (c != null) {
            playerMonsterId = c.getUserMonsterId();
            monst = playerIsland.getMonsterByID(playerMonsterId);
            if (monst != null && playerIsland.finishCrucibleEvolve(c, monst, true, isAdmin)) {
               int newType = c.getNewMonster();
               if (newType > 0) {
                  playerIsland.addToSoldMonsters(newType);
                  if (playerIsland.soldMonsterTypes() != null) {
                     SFSObject r = new SFSObject();
                     r.putLong("island_id", playerIsland.getID());
                     r.putUtfString("monsters_sold", playerIsland.soldMonsterTypes().toJson());
                     this.send("gs_update_sold_monsters", r, sender);
                  }
               }
            }
         }

         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         this.send("gs_viewed_egg", response, sender);
      } catch (Exception var15) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         this.send("gs_viewed_egg", response, sender);
         Logger.trace(var15, "error setting egg viewed", "   params : " + params.getDump());
      }

   }

   private void finishAmberEvolve(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      PlayerIsland playerIsland = player.getActiveIsland();
      boolean isAdmin = false;
      if (params.containsKey("is_admin")) {
         isAdmin = params.getBool("is_admin");
      }

      if (isAdmin) {
         if (sender.getPrivilegeId() != 3) {
            this.ext.sendErrorResponse("gs_finish_amber_evolve", "Admin Finish Amber Evolve attempted on non admin account", sender);
            return;
         }

         if (!params.containsKey("user_island_id")) {
            this.ext.sendErrorResponse("gs_finish_amber_evolve", "Evolve attempted on undefined island", sender);
            return;
         }

         player = (Player)sender.getProperty("friend_object");
         playerIsland = player.getIslandByID(params.getLong("user_island_id"));
      }

      if (!playerIsland.isAmberIsland()) {
         this.ext.sendErrorResponse("gs_finish_amber_evolve", "Evolve attempted on non-amber island", sender);
      } else {
         long playerStructureId = params.getLong("user_structure_id");
         PlayerStructure structure = playerIsland.getStructureByID(playerStructureId);
         if (structure == null) {
            this.ext.sendErrorResponse("gs_finish_amber_evolve", "Cannot find crucible structure", sender);
         } else {
            boolean verify = false;
            if (params.containsKey("verify")) {
               verify = params.getBool("verify");
            }

            boolean success = false;
            ISFSObject response = new SFSObject();
            ISFSObject metricData = new SFSObject();
            PlayerCrucibleData c = playerIsland.getCrucibleData(playerStructureId);
            PlayerMonster monst = null;
            long playerMonsterId = 0L;
            boolean evolutionSucceeded = false;
            int newMonsterVerification = 0;
            int xpReward = 0;
            if (c != null) {
               playerMonsterId = c.getUserMonsterId();
               if (playerMonsterId == 0L) {
                  this.ext.sendErrorResponse("gs_finish_amber_evolve", "Evolution already finished", sender);
                  return;
               }

               char flag = c.getQueuedFlag();
               monst = playerIsland.getMonsterByID(playerMonsterId);
               if (monst != null) {
                  int oldMonsterType = monst.getType();
                  if (playerIsland.finishCrucibleEvolve(c, monst, verify, isAdmin)) {
                     if (!verify) {
                        if (monst.getType() != oldMonsterType) {
                           newMonsterVerification = monst.getType();
                           evolutionSucceeded = true;
                           xpReward = MonsterLookup.get(monst.getType()).getXp();
                           playerIsland.addToSoldMonsters(monst.getType());
                        } else if (flag != 0) {
                           response.putUtfString("mercy_flag", String.valueOf(flag));
                        }

                        if (!isAdmin) {
                           player.setDirty(true);
                           this.ext.savePlayer(player);
                        } else {
                           try {
                              MSMExtension.getInstance().savePlayerIsland(player, playerIsland, false);
                           } catch (Exception var23) {
                              success = false;
                           }
                        }
                     } else {
                        evolutionSucceeded = c.getNewMonster() != 0;
                        newMonsterVerification = c.getNewMonster();
                        if (flag != 0) {
                           response.putUtfString("mercy_flag", String.valueOf(flag));
                        }
                     }

                     success = true;
                     metricData.putInt("old_monster_id", oldMonsterType);
                     metricData.putInt("monster_id", monst.getType());
                  }
               } else {
                  c.errorHandlingReset();
                  playerMonsterId = 0L;
                  verify = true;
                  success = true;
                  evolutionSucceeded = false;
               }
            }

            if (success) {
               response.putBool("success", success);
               response.putSFSObject("user_crucible", c.getData());
               response.putLong("user_monster_id", playerMonsterId);
               response.putBool("evolve_success", evolutionSucceeded);
               if (evolutionSucceeded) {
                  response.putInt("new_monst", newMonsterVerification);
               }

               response.putBool("verify", verify);
               this.send("gs_finish_amber_evolve", response, sender);
               if (!verify && monst != null) {
                  response = new SFSObject();
                  response.putLong("user_monster_id", monst.getID());
                  response.putUtfString("name", monst.getName());
                  response.putInt("book_value", monst.bookValue());
                  response.putLong("last_collection", monst.getLastCollectedTime());
                  response.putLong("muted", (long)monst.getMuted());
                  if (xpReward != 0) {
                     player.rewardXp(sender, this, xpReward);
                     ISFSArray responseVars = new SFSArray();
                     player.addPlayerPropertyData(responseVars, isAdmin);
                     response.putSFSArray("properties", responseVars);
                  }

                  if (!isAdmin) {
                     this.send("gs_update_monster", response, sender);
                  } else {
                     this.send("gs_admin_update_users_monster", response, sender);
                  }

                  metricData.putBool("is_admin", isAdmin);
                  metricData.putSFSObject("response", response);
                  this.ext.stats.trackCrucibleCollectEvolve(sender, metricData);
               }
            } else {
               response.putBool("success", success);
               response.putSFSObject("user_crucible", c.getData());
               response.putLong("user_monster_id", playerMonsterId);
               if (isAdmin) {
                  response.putBool("admin", true);
               }

               this.send("gs_finish_amber_evolve", response, sender);
            }

         }
      }
   }

   private void collectCrucHeat(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      PlayerIsland playerIsland = player.getActiveIsland();
      if (!playerIsland.isAmberIsland()) {
         this.ext.sendErrorResponse("gs_collect_cruc_heat", "Collect crucible heat attempted on non-amber island", sender);
      } else {
         long playerStructureId = params.getLong("user_structure_id");
         PlayerStructure structure = playerIsland.getStructureByID(playerStructureId);
         if (structure == null) {
            this.ext.sendErrorResponse("gs_collect_cruc_heat", "Cannot find crucible structure", sender);
         } else {
            boolean success = false;
            ISFSObject response = new SFSObject();
            ISFSObject metricData = new SFSObject();
            PlayerCrucibleData c = playerIsland.getCrucibleData(playerStructureId);
            if (c != null && c.getHeatLevel() > 0) {
               metricData.putSFSObject("crucible_data", c.getData());

               try {
                  int relics = c.collectExcessHeat();
                  response.putInt("collected_relics", relics);
                  metricData.putInt("collected_relics", relics);
                  player.adjustRelics(sender, this, relics);
                  metricData.putLong("relics_remaining", player.getActualRelics());
                  ISFSArray responseVars = new SFSArray();
                  player.addPlayerPropertyData(responseVars, false);
                  response.putSFSArray("properties", responseVars);
                  success = true;
               } catch (Exception var14) {
                  Logger.trace(var14, "error starting crucible heat collect", "   params : " + params.getDump());
               }
            }

            response.putBool("success", success);
            if (c != null) {
               response.putSFSObject("user_crucible", c.getData());
            }

            this.send("gs_collect_cruc_heat", response, sender);
            this.ext.stats.trackCrucibleRemoveHeat(sender, metricData);
         }
      }
   }

   private void purchaseEvolveUnlock(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      long underlingIslandId = params.getLong("user_underling_island");
      PlayerIsland playerIsland = player.getIslandByID(underlingIslandId);
      boolean isAdmin = false;
      if (params.containsKey("is_admin")) {
         isAdmin = params.getBool("is_admin");
      }

      if (isAdmin) {
         if (sender.getPrivilegeId() != 3) {
            this.ext.sendErrorResponse("gs_purchase_evolve_unlock", "Error! Trying to invoke admin without privileges!", sender);
            return;
         }

         player = (Player)sender.getProperty("friend_object");
         playerIsland = player.getIslandByID(underlingIslandId);
      }

      if (playerIsland != null && playerIsland.isUnderlingIsland()) {
         long playerMonsterId = params.getLong("user_monster_id");
         PlayerMonster playerMonster = playerIsland.getMonsterByID(playerMonsterId);
         if (playerMonster == null) {
            this.ext.sendErrorResponse("gs_purchase_evolve_unlock", "Cannot find user monster", sender);
         } else {
            ISFSObject response = new SFSObject();
            boolean success = false;
            if (!playerMonster.isUnderlingEvolveUnlocked()) {
               if (!playerIsland.canEvolveMoreOfSelectedType(playerMonster.getType())) {
                  this.ext.sendErrorResponse("gs_purchase_evolve_unlock", "Attempt to exceed underling evolve limit", sender);
                  return;
               }

               if (!isAdmin) {
                  int keyCost = playerMonster.getEvolveKeyCost(false);
                  if ((long)keyCost <= player.getActualKeys()) {
                     playerMonster.setUnlockUnderlingEvolution(true);
                     player.chargePlayer(sender, this, 0, 0, 0, 0L, keyCost, 0, 0);
                     if (playerIsland.getID() != player.getActiveIslandId()) {
                        try {
                           this.ext.savePlayerIsland(player, playerIsland, false);
                        } catch (Exception var16) {
                           Logger.trace(var16, "error unlocking monster in admin", "   params : " + params.getDump());
                           success = false;
                        }
                     }

                     ISFSArray responseVars = new SFSArray();
                     player.addPlayerPropertyData(responseVars, isAdmin);
                     response.putSFSArray("properties", responseVars);
                     success = true;
                  }
               } else {
                  playerMonster.setUnlockUnderlingEvolution(true);

                  try {
                     this.ext.savePlayerIsland(player, playerIsland, false);
                     success = true;
                  } catch (Exception var15) {
                     Logger.trace(var15, "error unlocking monster in admin", "   params : " + params.getDump());
                     success = false;
                  }
               }
            }

            response.putBool("success", success);
            response.putLong("user_monster_id", playerMonsterId);
            this.send("gs_purchase_evolve_unlock", response, sender);
            if (success) {
               response = new SFSObject();
               response.putLong("user_monster_id", playerMonsterId);
               response.putInt("evolve_unlocked", playerMonster.isUnderlingEvolveUnlocked() ? 1 : 0);
               if (!isAdmin) {
                  this.send("gs_update_monster", response, sender);
               } else {
                  this.send("gs_admin_update_users_monster", response, sender);
               }
            }

         }
      } else {
         this.ext.sendErrorResponse("gs_purchase_evolve_unlock", "Purchase Evolve attempted on invalid island", sender);
      }
   }

   private void purchaseEvolvePowerupUnlock(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      PlayerIsland playerIsland = null;
      boolean isAdmin = false;
      if (params.containsKey("is_admin")) {
         isAdmin = params.getBool("is_admin");
      }

      long playerMonsterId;
      if (isAdmin) {
         playerMonsterId = params.getLong("user_underling_island");
         if (sender.getPrivilegeId() != 3) {
            this.ext.sendErrorResponse("gs_purchase_evo_powerup_unlock", "Error! Trying to invoke admin without privileges!", sender);
            return;
         }

         player = (Player)sender.getProperty("friend_object");
         playerIsland = player.getIslandByID(playerMonsterId);
      } else {
         playerIsland = player.getIslandByIslandIndex(12);
      }

      if (playerIsland != null && playerIsland.isCelestialIsland()) {
         playerMonsterId = params.getLong("user_monster_id");
         PlayerMonster playerMonster = playerIsland.getMonsterByID(playerMonsterId);
         if (playerMonster == null) {
            this.ext.sendErrorResponse("gs_purchase_evo_powerup_unlock", "Cannot find user monster", sender);
         } else {
            ISFSObject response = new SFSObject();
            boolean success = false;
            if (!playerMonster.isCelestialPowerupUnlocked()) {
               if (!isAdmin) {
                  int keyCost = playerMonster.getPowerupKeyCost();
                  if ((long)keyCost <= player.getActualKeys()) {
                     playerMonster.setUnlockCelestialPowerup(true);
                     player.chargePlayer(sender, this, 0, 0, 0, 0L, keyCost, 0, 0);
                     ISFSArray responseVars = new SFSArray();
                     player.addPlayerPropertyData(responseVars, isAdmin);
                     response.putSFSArray("properties", responseVars);
                     success = true;
                  }
               } else {
                  playerMonster.setUnlockCelestialPowerup(true);

                  try {
                     this.ext.savePlayerIsland(player, playerIsland, false);
                     success = true;
                  } catch (Exception var13) {
                     Logger.trace(var13, "error unlocking monster in admin", "   params : " + params.getDump());
                     success = false;
                  }
               }
            }

            response.putBool("success", success);
            response.putLong("user_monster_id", playerMonsterId);
            this.send("gs_purchase_evo_powerup_unlock", response, sender);
            if (success) {
               response = new SFSObject();
               response.putLong("user_monster_id", playerMonsterId);
               response.putInt("powerup_unlocked", playerMonster.isCelestialPowerupUnlocked() ? 1 : 0);
               if (!isAdmin) {
                  this.send("gs_update_monster", response, sender);
               } else {
                  this.send("gs_admin_update_users_monster", response, sender);
               }
            }

         }
      } else {
         this.ext.sendErrorResponse("gs_purchase_evo_powerup_unlock", "Purchase Powerup attempted on invalid island", sender);
      }
   }

   private void setMoniker(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      int monikerId = params.getInt("moniker_id");
      boolean success = player.setMonikerId(monikerId);
      SFSObject response = new SFSObject();
      if (success) {
         response.putInt("id", monikerId);
      }

      response.putBool("success", success);
      this.send("gs_set_moniker", response, sender);
   }

   private void adminBoxPurchaseFill(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminBoxPurchaseFill: Error! Trying to invoke admin without privileges!");
      } else {
         Player player = (Player)sender.getProperty("friend_object");
         long userIslandId = params.getLong("user_island_id");
         PlayerIsland playerIsland = player.getIslandByID(userIslandId);
         long boxMonsterId = params.getLong("user_box_monster_id");
         PlayerMonster boxMonster = playerIsland.getMonsterByID(boxMonsterId);
         if (boxMonster == null) {
            this.ext.sendErrorResponse("gs_admin_box_purchase_fill", "Could not find the monster you're trying to purchase fill", sender);
         } else {
            SFSObject response = new SFSObject();
            boolean success = false;

            try {
               boolean isGoldIsland = playerIsland.isGoldIsland();
               int xpReward = boxMonster.fillRemainingEggsXp(playerIsland);
               if (xpReward > 0) {
                  boxMonster.fillEggs(playerIsland, this, sender);
                  if (boxMonster.hasAllRequiredEggs(isGoldIsland)) {
                     Logger.trace("hasAllRequiredEggs: ");
                     player.rewardXp(sender, this, xpReward);
                     ISFSArray responseVars = new SFSArray();
                     player.addPlayerPropertyData(responseVars, true);
                     response.putSFSArray("properties", responseVars);
                     success = true;
                     Logger.trace("savePlayerIsland: ");
                     this.ext.savePlayerIsland(player, playerIsland, false);
                  }
               }
            } catch (Exception var15) {
               Logger.trace(var15, "error activating box monster", "   params : " + params.getDump());
            }

            response.putBool("success", success);
            response.putLong("user_monster_id", boxMonsterId);
            this.send("gs_admin_box_purchase_fill", response, sender);
            if (success) {
               response = new SFSObject();
               response.putLong("user_monster_id", boxMonsterId);
               if (boxMonster.isInactiveBoxMonster()) {
                  response.putUtfString("boxed_eggs", boxMonster.boxedEggs().toJson());
               } else if (boxMonster.isEvolvable()) {
                  response.putUtfString("has_evolve_reqs", boxMonster.evolveReqsMetStatic().toJson());
                  response.putUtfString("has_evolve_flexeggs", boxMonster.evolveReqsMetFlex().toJson());
               }

               this.send("gs_admin_update_users_monster", response, sender);
            }

         }
      }
   }

   private void boxPurchaseFill(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      PlayerIsland island = player.getActiveIsland();
      boolean prefWildcards = false;
      if (params.containsKey("pref_wildcards")) {
         prefWildcards = params.getBool("pref_wildcards");
      }

      long boxMonsterId = params.getLong("user_monster_id");
      PlayerMonster boxMonster = island.getMonsterByID(boxMonsterId);
      if (boxMonster == null) {
         this.ext.sendErrorResponse("gs_box_purchase_fill", "Could not find the monster you're trying to purchase fill", sender);
      } else {
         Monster staticMonster = MonsterLookup.get(boxMonster.getType());
         if (boxMonster.isEvolvable() && island.isUnderlingIsland() && !boxMonster.isUnderlingEvolveUnlocked()) {
            this.ext.sendErrorResponse("gs_box_purchase_fill", "Rare Wublin not unlocked before fill", sender);
         } else {
            SFSObject response = new SFSObject();
            boolean success = false;
            boolean notEnoughDiamonds = false;
            boolean notEnoughWildcards = false;

            try {
               boolean isGoldIsland = island.isGoldIsland();
               PlayerMonster.BoxFillCost fillCost = boxMonster.fillRemainingEggsDiamondCost(island, prefWildcards, player.getActualEggWildcards() == 0L, player.getActualDiamonds(), player.getActualEggWildcards());
               int xpReward = boxMonster.fillRemainingEggsXp(island);
               int numBoxed = 0;
               String boxState = "unknown";
               if (fillCost.diamonds > 0 || fillCost.wildcards > 0 || xpReward > 0) {
                  if (player.getActualDiamonds() < (long)fillCost.diamonds) {
                     notEnoughDiamonds = true;
                  }

                  if (player.getActualEggWildcards() < (long)fillCost.wildcards) {
                     notEnoughWildcards = true;
                  }

                  if (!notEnoughDiamonds && !notEnoughWildcards) {
                     int numToFill = boxMonster.numReqsRemaining(isGoldIsland);
                     if (boxMonster.boxedEggs() != null) {
                        numBoxed += boxMonster.boxedEggs().size();
                     }

                     if (boxMonster.evolveReqsMetStatic() != null) {
                        numBoxed += boxMonster.evolveReqsMetStatic().size();
                     }

                     if (boxMonster.evolveReqsMetFlex() != null) {
                        numBoxed += boxMonster.evolveReqsMetFlex().size();
                     }

                     if (boxMonster.isInactiveBoxMonster()) {
                        boxState = "inactive";
                     } else if (boxMonster.isEvolvable()) {
                        boxState = "evolvable";
                     }

                     long elapsedEggTime = Math.max((MSMExtension.CurrentDBTime() - boxMonster.eggTimerStart()) / 1000L, 0L);
                     long eggTimeRemaining = (long)staticMonster.getTimeToFillSec() - elapsedEggTime;
                     boolean isTimed = staticMonster.getTimeToFillSec() != -1;
                     boolean isStarted = boxMonster.eggTimerStart() != -1L;
                     boxMonster.fillEggs(island, this, sender);
                     if (!boxMonster.hasAllRequiredEggs(isGoldIsland)) {
                        this.ext.sendErrorResponse("gs_box_purchase_fill", "Fill failed", sender);
                        return;
                     }

                     if (boxMonster.isEvolvable()) {
                        ISFSObject monsterUpdateResponse = new SFSObject();
                        boxMonster.startUnderlingHibernation(monsterUpdateResponse, player, sender, params, this);
                        this.send("gs_update_monster", monsterUpdateResponse, sender);
                     }

                     boxMonster.resetEggTimer();
                     player.adjustDiamonds(sender, this, -fillCost.diamonds);
                     player.adjustEggWildcards(sender, this, -fillCost.wildcards);
                     player.rewardXp(sender, this, xpReward);
                     ISFSArray responseVars = new SFSArray();
                     player.addPlayerPropertyData(responseVars, false);
                     response.putSFSArray("properties", responseVars);
                     String action = "box_purchase_fill_" + staticMonster.getGenes() + "_" + boxState;
                     this.logDiamondUsage(sender, action, fillCost.diamonds, player.getLevel(), staticMonster.getEntityId());
                     this.ext.stats.TrackFillBoxMonster(sender, boxMonster.getID(), staticMonster.getMonsterID(), staticMonster.getGenes(), numBoxed, numToFill, fillCost.diamonds, fillCost.wildcards, Math.max(0L, eggTimeRemaining), isTimed, isStarted, boxState);
                     success = true;
                  }
               }
            } catch (Exception var28) {
               Logger.trace(var28, "error activating box monster", "   params : " + params.getDump());
            }

            response.putBool("success", success);
            if (notEnoughDiamonds) {
               response.putBool("not_enough_diamonds", notEnoughDiamonds);
            }

            if (notEnoughWildcards) {
               response.putBool("not_enough_wildcards", notEnoughWildcards);
            }

            response.putLong("user_monster_id", boxMonsterId);
            this.send("gs_box_purchase_fill", response, sender);
            if (success) {
               response = new SFSObject();
               response.putLong("user_monster_id", boxMonsterId);
               if (boxMonster.isInactiveBoxMonster()) {
                  response.putUtfString("boxed_eggs", boxMonster.boxedEggs().toJson());
               } else if (boxMonster.isEvolvable()) {
                  response.putUtfString("has_evolve_reqs", boxMonster.evolveReqsMetStatic().toJson());
                  response.putUtfString("has_evolve_flexeggs", boxMonster.evolveReqsMetFlex().toJson());
               }

               response.putLong("egg_timer_start", boxMonster.eggTimerStart());
               this.send("gs_update_monster", response, sender);
            }

         }
      }
   }

   private void boxAddEgg(User sender, ISFSObject params) {
      long playerMonsterId = params.getLong("user_monster_id");
      long userEggId = params.getLong("user_egg_id");
      boolean zapping = false;
      if (params.containsKey("underling")) {
         zapping = params.getBool("underling");
      }

      ISFSObject response = new SFSObject();
      boolean admin = false;
      if (params.containsKey("admin")) {
         admin = params.getBool("admin");
      }

      Player player;
      PlayerIsland playerActiveIsland;
      long destIslandId;
      if (!admin) {
         player = (Player)sender.getProperty("player_object");
         playerActiveIsland = player.getActiveIsland();
      } else {
         if (sender.getPrivilegeId() != 3) {
            Logger.trace("boxAddEgg: Error! Trying to invoke admin without privileges!");
            return;
         }

         player = (Player)sender.getProperty("friend_object");
         destIslandId = params.getLong("island_id");
         playerActiveIsland = player.getIslandByID(destIslandId);
         response.putLong("island_id", playerActiveIsland.getID());
      }

      destIslandId = playerActiveIsland.getID();
      PlayerMonster boxMonster = null;
      PlayerIsland boxingIsland = null;
      if (!zapping) {
         boxMonster = playerActiveIsland.getMonsterByID(playerMonsterId);
      } else {
         if (playerActiveIsland.isSeasonalIsland()) {
            this.ext.sendErrorResponse("gs_box_add_egg", "Zapping from invalid island Seasonal Shanty", sender);
            return;
         }

         int[] zappingIslands = new int[]{10, 12, 22};

         for(int i = 0; i < zappingIslands.length; ++i) {
            boxingIsland = player.getIslandByIslandIndex(zappingIslands[i]);
            if (boxingIsland != null) {
               boxMonster = boxingIsland.getMonsterByID(playerMonsterId);
               if (boxMonster != null) {
                  break;
               }
            }
         }

         if (boxMonster == null) {
            this.ext.sendErrorResponse("gs_box_add_egg", "Could not find underling/celestial", sender);
            return;
         }

         destIslandId = boxingIsland.getID();
      }

      Monster staticMonster = MonsterLookup.get(boxMonster.getType());
      if (boxMonster.isEvolvable()) {
         if (!staticMonster.evolveEnabled() && !EvolveAvailabilityEvent.hasTimedEventNow(staticMonster, player, boxingIsland.getType())) {
            this.ext.sendErrorResponse("gs_box_add_egg", "CELESTIAL_OUT_OF_SEASON", sender);
            return;
         }

         if (boxingIsland.isUnderlingIsland() && !boxMonster.isUnderlingEvolveUnlocked()) {
            this.ext.sendErrorResponse("gs_box_add_egg", "Rare Wublin not unlocked before gs_box_add_egg", sender);
            return;
         }
      }

      response.putLong("dest_island_id", destIslandId);
      PlayerEgg playerEgg = null;
      PlayerBreeding playerBreeding = null;
      boolean success = false;
      int eggType = 0;

      try {
         if (!zapping) {
            playerEgg = playerActiveIsland.getEgg(userEggId);
            eggType = playerEgg.getType();
            if (playerEgg.getTimeRemaining() > 0L) {
               this.ext.sendErrorResponse("gs_box_add_egg", "Hatching is not finished", sender);
               return;
            }
         } else {
            playerBreeding = playerActiveIsland.getBreeding(userEggId);
            if (playerBreeding == null) {
               this.ext.sendErrorResponse("gs_box_add_egg", "Could not find playerBreeding holding userEggId " + userEggId, sender);
               return;
            }

            if (playerBreeding.getTimeRemaining() > 0L) {
               this.ext.sendErrorResponse("gs_box_add_egg", "Breeding is not finished", sender);
               return;
            }

            eggType = playerBreeding.getChildType();
         }

         if (playerEgg != null) {
            success = this.boxAddEgg(sender, player, playerActiveIsland, boxMonster, playerEgg);
            if (!success) {
               return;
            }
         } else {
            if (playerBreeding == null) {
               this.ext.sendErrorResponse("gs_box_add_egg", "Could not find playerEgg or playerBreeding", sender);
               return;
            }

            response.putBool("isWublin", boxingIsland.isUnderlingIsland());
            if (boxingIsland.isAmberIsland() && !staticMonster.canPurchaseFromStore(admin, player, boxingIsland, false, VersionData.Instance().getMaxServerVersionFromClientVersion(player.lastClientVersion()))) {
               this.ext.sendErrorResponse("gs_box_add_egg", "Cannot zap to amber island outside of availability", sender);
               return;
            }

            success = this.boxAddZapEgg(sender, player, playerActiveIsland, boxMonster, playerBreeding, boxingIsland, params);
            if (success) {
               if (boxMonster.hasAllRequiredEggs(false)) {
                  response.putBool("has_all", true);
                  boxMonster.resetEggTimer();
               } else {
                  boxMonster.startEggTimer(boxingIsland);
               }
            }

            this.ext.savePlayerIsland(player, boxingIsland, false);
         }

         if (admin) {
            this.ext.savePlayerIsland(player, playerActiveIsland, false);
         }

         response.putLong("user_monster_id", playerMonsterId);
         response.putLong("user_egg_id", userEggId);
         response.putInt("egg_type", eggType);
         response.putBool("underling", zapping);
         this.ext.stats.TrackBoxAddMonster(sender, boxMonster, eggType, true, playerActiveIsland.isGoldIsland());
      } catch (Exception var23) {
         Logger.trace(var23, "error boxing eggs", "   params : " + params.getDump());
      }

      response.putBool("success", success);
      this.send("gs_box_add_egg", response, sender);
      if (success) {
         int xpReward = MonsterLookup.get(eggType).getXp();
         player.rewardXp(sender, this, xpReward);
         response = new SFSObject();
         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         response.putLong("user_monster_id", playerMonsterId);
         if (boxMonster.isInactiveBoxMonster()) {
            response.putUtfString("boxed_eggs", boxMonster.boxedEggs().toJson());
         } else if (boxMonster.isEvolvable()) {
            response.putUtfString("has_evolve_reqs", boxMonster.evolveReqsMetStatic().toJson());
            response.putUtfString("has_evolve_flexeggs", boxMonster.evolveReqsMetFlex().toJson());
         }

         response.putLong("egg_timer_start", boxMonster.eggTimerStart());
         this.send("gs_update_monster", response, sender);
      }

   }

   private boolean boxAddEgg(User sender, Player player, PlayerIsland island, PlayerMonster boxMonster, PlayerEgg playerEgg) throws Exception {
      if (boxMonster == null) {
         this.ext.sendErrorResponse("gs_box_add_egg", "BOX_MONSTER_ERROR_BOX_NOT_FOUND", sender);
         return false;
      } else if (playerEgg == null) {
         this.ext.sendErrorResponse("gs_box_add_egg", "BOX_MONSTER_ERROR_EGG_NOT_FOUND", sender);
         return false;
      } else if (MonsterLookup.get(playerEgg.getType()).isBoxMonsterType()) {
         this.ext.sendErrorResponse("gs_box_add_egg", "BOX_MONSTER_ERROR_WUBBOX_EGG_INVALID", sender);
         return false;
      } else if (boxMonster.addEgg(sender, this, playerEgg.getType(), player.getActiveIsland())) {
         island.removeEgg(playerEgg.getID());
         return true;
      } else {
         this.ext.sendErrorResponse("gs_box_add_egg", "NOTIFICATION_MONSTER_NOT_REQUIRED", sender);
         return false;
      }
   }

   private boolean boxAddZapEgg(User sender, Player player, PlayerIsland breedingStructureIsland, PlayerMonster boxMonster, PlayerBreeding playerBreeding, PlayerIsland boxingIsland, ISFSObject params) throws Exception {
      boolean success = false;
      if (boxMonster == null) {
         this.ext.sendErrorResponse("gs_box_add_egg", "Could not find the boxmonster you're trying to add an egg to", sender);
         return success;
      } else if (playerBreeding == null) {
         this.ext.sendErrorResponse("gs_box_add_egg", "Could not find the breeding egg you're trying to box", sender);
         return success;
      } else if (boxingIsland == null) {
         this.ext.sendErrorResponse("gs_box_add_egg", "Could not find the island you're tying to add an egg to", sender);
         return success;
      } else {
         int monsterId = MonsterIslandToIslandMapping.monsterSourceGivenAnyIsland(playerBreeding.getChildType());
         if (monsterId == 0) {
            monsterId = playerBreeding.getChildType();
         }

         if (boxMonster.addEgg(sender, this, monsterId, boxingIsland)) {
            breedingStructureIsland.removeBreeding(playerBreeding.getID());
            success = true;
         } else if (monsterId != 0) {
            if (boxMonster.addEgg(sender, this, monsterId, boxingIsland)) {
               breedingStructureIsland.removeBreeding(playerBreeding.getID());
               success = true;
            } else {
               this.ext.sendErrorResponse("gs_box_add_egg", "Failed attempted egg replacement", sender);
            }
         } else {
            Monster m = MonsterLookup.get(playerBreeding.getChildType());
            if (m != null) {
               this.ext.sendErrorResponse("gs_box_add_egg", "Could not find compatible egg requirement for " + m.commonName(), sender);
            } else {
               this.ext.sendErrorResponse("gs_box_add_egg", "Could not find compatible egg requirement", sender);
            }
         }

         if (success && boxMonster.isEvolvable() && boxingIsland.isUnderlingIsland()) {
            ISFSObject monsterUpdateResponse = new SFSObject();
            boxMonster.startUnderlingHibernation(monsterUpdateResponse, player, sender, params, this);
            this.send("gs_update_monster", monsterUpdateResponse, sender);
         }

         return success;
      }
   }

   /** @deprecated */
   @Deprecated
   private void adminBoxMonsterToggle(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminBoxMonsterToggle: Error! Trying to invoke admin without privileges!");
      } else {
         Player player = (Player)sender.getProperty("friend_object");
         long userIslandId = params.getLong("user_island_id");
         PlayerIsland playerIsland = player.getIslandByID(userIslandId);
         long boxMonsterId = params.getLong("user_box_monster_id");
         int monsterId = params.getInt("monster_id");
         boolean toggleOn = params.getBool("toggleOn");

         try {
            PlayerMonster boxMonster = playerIsland.getMonsterByID(boxMonsterId);
            boolean success = false;
            success = this.adminBoxToggleMonster(sender, player, playerIsland, boxMonster, monsterId, toggleOn);
            if (success) {
               this.ext.savePlayerIsland(player, playerIsland, false);
            }

            ISFSObject response = new SFSObject();
            response.putBool("success", success);
            response.putLong("user_box_monster_id", boxMonsterId);
            response.putInt("monster_type", monsterId);
            this.send("gs_admin_box_monster_toggle", response, sender);
            if (success) {
               response = new SFSObject();
               response.putLong("user_monster_id", boxMonsterId);
               if (boxMonster.isInactiveBoxMonster()) {
                  response.putUtfString("boxed_eggs", boxMonster.boxedEggs().toJson());
               } else if (boxMonster.isEvolvable()) {
                  response.putUtfString("has_evolve_reqs", boxMonster.evolveReqsMetStatic().toJson());
                  response.putUtfString("has_evolve_flexeggs", boxMonster.evolveReqsMetFlex().toJson());
               }

               this.send("gs_admin_update_users_monster", response, sender);
            }
         } catch (Exception var14) {
            Logger.trace(var14, "error admin boxing monsters", "   params : " + params.getDump());
         }

      }
   }

   private void adminBoxMonsterToggleNew(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminBoxMonsterToggle: Error! Trying to invoke admin without privileges!");
      } else {
         Player player = (Player)sender.getProperty("friend_object");
         boolean isQABuild = false;
         if (player == null) {
            player = (Player)sender.getProperty("player_object");
            isQABuild = true;
         }

         long userIslandId = params.getLong("user_island_id");
         PlayerIsland playerIsland = player.getIslandByID(userIslandId);
         long boxMonsterId = params.getLong("user_box_monster_id");
         int reqInd = params.getInt("req_ind");
         boolean isFlexReq = params.getBool("flex");
         boolean toggleOn = params.getBool("toggleOn");

         try {
            PlayerMonster boxMonster = playerIsland.getMonsterByID(boxMonsterId);
            boolean success = false;
            success = this.adminBoxToggleMonsterNew(sender, player, playerIsland, boxMonster, reqInd, isFlexReq, toggleOn);
            if (success) {
               this.ext.savePlayerIsland(player, playerIsland, false);
            }

            ISFSObject response = new SFSObject();
            response.putBool("success", success);
            response.putLong("user_box_monster_id", boxMonsterId);
            this.send("gs_admin_box_monster_toggle_new", response, sender);
            if (success) {
               response = new SFSObject();
               response.putLong("user_monster_id", boxMonsterId);
               if (boxMonster.isInactiveBoxMonster()) {
                  response.putUtfString("boxed_eggs", boxMonster.boxedEggs().toJson());
               } else if (boxMonster.isEvolvable()) {
                  response.putUtfString("has_evolve_reqs", boxMonster.evolveReqsMetStatic().toJson());
                  response.putUtfString("has_evolve_flexeggs", boxMonster.evolveReqsMetFlex().toJson());
               }

               if (!isQABuild) {
                  this.send("gs_admin_update_users_monster", response, sender);
               } else {
                  this.send("gs_update_monster", response, sender);
               }
            }
         } catch (Exception var16) {
            Logger.trace(var16, "error admin boxing monsters", "   params : " + params.getDump());
         }

      }
   }

   private void boxAddMonster(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      long boxMonsterId = params.getLong("user_box_monster_id");
      long userMonsterId = params.getLong("user_monster_id");
      PlayerIsland island = player.getActiveIsland();
      PlayerMonster boxMonster = island.getMonsterByID(boxMonsterId);
      PlayerMonster userMonster = island.getMonsterByID(userMonsterId);
      boolean success = false;
      SFSObject response = new SFSObject();

      try {
         success = this.boxAddMonster(sender, player, island, boxMonster, userMonster, response);
      } catch (Exception var14) {
         Logger.trace(var14, "error boxing monsters", "   params : " + params.getDump());
      }

      response.putBool("success", success);
      response.putLong("user_box_monster_id", boxMonsterId);
      response.putLong("user_monster_id", userMonsterId);
      if (userMonster != null) {
         response.putInt("monster_type", userMonster.getType());
      }

      this.send("gs_box_add_monster", response, sender);
      if (success) {
         if (island.isALastBredMonster(userMonsterId)) {
            island.setLastBred(0L, 0L);
         }

         response = new SFSObject();
         response.putLong("user_monster_id", boxMonsterId);
         if (boxMonster.isInactiveBoxMonster()) {
            response.putUtfString("boxed_eggs", boxMonster.boxedEggs().toJson());
         } else if (boxMonster.isEvolvable()) {
            response.putUtfString("has_evolve_reqs", boxMonster.evolveReqsMetStatic().toJson());
            response.putUtfString("has_evolve_flexeggs", boxMonster.evolveReqsMetFlex().toJson());
         }

         this.send("gs_update_monster", response, sender);
         this.ext.stats.TrackBoxAddMonster(sender, boxMonster, userMonster.getType(), false, island.isGoldIsland());
      }

   }

   /** @deprecated */
   @Deprecated
   private boolean adminBoxToggleMonster(User sender, Player player, PlayerIsland island, PlayerMonster boxMonster, int monsterId, boolean toggleOn) {
      boolean success = false;
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminBoxToggleMonster: Error! Trying to invoke admin without privileges!");
         return success;
      } else if (boxMonster == null) {
         this.ext.sendErrorResponse("gs_admin_box_monster_toggle", "Could not find the boxMonster you're trying to add a monster to", sender);
         return success;
      } else {
         int num = boxMonster.hasNumOfEggStatic(monsterId);
         if (num > 0 && !toggleOn) {
            success = boxMonster.adminRemoveEgg(sender, this, monsterId, island);
         } else {
            Monster m = MonsterLookup.get(boxMonster.getType());
            ISFSArray requiredEggs = null;
            if (boxMonster.isEvolvable()) {
               requiredEggs = m.getEvolveReqsStatic();
            } else if (m.isBoxMonsterType()) {
               requiredEggs = boxMonster.getBoxRequirements(island.isGoldIsland());
            }

            int numBoxReqsOfTypeReqd = 0;

            for(int i = 0; i < requiredEggs.size(); ++i) {
               int monsterType = requiredEggs.getInt(i);
               if (monsterType == monsterId) {
                  ++numBoxReqsOfTypeReqd;
               }
            }

            if (num < numBoxReqsOfTypeReqd && toggleOn) {
               success = boxMonster.addEgg(sender, this, monsterId, island);
            }
         }

         return success;
      }
   }

   private boolean adminBoxToggleMonsterNew(User sender, Player player, PlayerIsland island, PlayerMonster boxMonster, int reqInd, boolean isFlexEgg, boolean toggleOn) {
      boolean success = false;
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminBoxToggleMonster: Error! Trying to invoke admin without privileges!");
         return success;
      } else if (boxMonster == null) {
         this.ext.sendErrorResponse("gs_admin_box_monster_toggle", "Could not find the boxMonster you're trying to add a monster to", sender);
         return success;
      } else {
         if (!toggleOn) {
            success = boxMonster.adminRemoveEggNew(sender, this, reqInd, isFlexEgg, island);
         } else {
            success = boxMonster.addEggAdmin(sender, this, reqInd, isFlexEgg, island);
         }

         return success;
      }
   }

   private boolean boxAddMonster(User sender, Player player, PlayerIsland island, PlayerMonster boxMonster, PlayerMonster playerMonster, ISFSObject response) throws Exception {
      if (boxMonster == null) {
         this.ext.sendErrorResponse("gs_box_add_monster", "BOX_MONSTER_ERROR_BOX_NOT_FOUND", sender);
         return false;
      } else if (playerMonster == null) {
         this.ext.sendErrorResponse("gs_box_add_monster", "BOX_MONSTER_ERROR_MONSTER_NOT_FOUND", sender);
         return false;
      } else if (island.isTribalIsland()) {
         this.ext.sendErrorResponse("gs_box_add_monster", "Can not box tribal island monsters", sender);
         return false;
      } else if (playerMonster.isInactiveBoxMonster()) {
         this.ext.sendErrorResponse("gs_box_add_monster", "BOX_MONSTER_ERROR_WUBBOX_MONSTER_INACTIVE", sender);
         return false;
      } else if (boxMonster.addEgg(sender, this, playerMonster.getType(), island)) {
         Long parentDeletedId;
         if (island.isGoldIsland()) {
            parentDeletedId = this.deleteParentData(player, playerMonster);
            if (parentDeletedId != null) {
               response.putLong("parent_monster_id", parentDeletedId);
            }
         } else {
            parentDeletedId = this.deleteAssociatedGoldMonster(playerMonster, player);
            if (parentDeletedId != null) {
               response.putLong("gi_monster_id", parentDeletedId);
               PlayerIsland goldIsland = player.getIslandByIslandIndex(6);
               if (goldIsland.soldMonsterTypes() != null) {
                  SFSObject response2 = new SFSObject();
                  response2.putLong("island_id", goldIsland.getID());
                  response2.putUtfString("monsters_sold", goldIsland.soldMonsterTypes().toJson());
                  this.send("gs_update_sold_monsters", response2, sender);
               }
            }
         }

         island.removeMonster(playerMonster, true);
         if (island.soldMonsterTypes() != null) {
            SFSObject response2 = new SFSObject();
            response2.putLong("island_id", island.getID());
            response2.putUtfString("monsters_sold", island.soldMonsterTypes().toJson());
            this.send("gs_update_sold_monsters", response2, sender);
         }

         if (island.hasHappinessTree()) {
            SFSArray monsterEffects = this.addHappyTreeEffects(island, sender);
            response.putSFSArray("monster_happy_effects", monsterEffects);
         }

         return true;
      } else {
         this.ext.sendErrorResponse("gs_box_add_monster", "NOTIFICATION_MONSTER_NOT_REQUIRED", sender);
         return false;
      }
   }

   private void sendMonsterHome(String clientSentRequestId, User sender, ISFSObject params, boolean admin) {
      Player player = null;
      PlayerIsland sourceIsland = null;
      if (admin) {
         if (sender.getPrivilegeId() != 3) {
            Logger.trace("sendMonsterHome: Error! Trying to invoke admin without privileges!");
            return;
         }

         player = (Player)sender.getProperty("friend_object");
         sourceIsland = player.getIslandByID(params.getLong("user_island_id"));
      } else {
         player = (Player)sender.getProperty("player_object");
         sourceIsland = player.getActiveIsland();
      }

      long teleportableMonsterId = params.getLong("user_monster_id");
      PlayerMonster teleportableMonster = sourceIsland.getMonsterByID(teleportableMonsterId);
      if (teleportableMonster.getLevel() >= GameSettings.getInt("GOLD_ISLAND_LEVEL")) {
         long sentIsland = params.getLong("user_island_id");
         if (sourceIsland.getID() == sentIsland && !sourceIsland.isTribalIsland()) {
            MonsterIslandData destinationData = MonsterIslandToIslandMapping.get(sourceIsland.getType(), teleportableMonster.getType());
            PlayerIsland goalIsland = player.getIslandByIslandIndex(destinationData.islandId);
            if (goalIsland == null) {
               params.putInt("island_id", destinationData.islandId);
               params.putBool("no_change_island", true);
               if (admin) {
                  this.adminBuyIsland(sender, params);
               } else {
                  this.buyIsland(sender, params);
               }

               goalIsland = player.getIslandByIslandIndex(destinationData.islandId);
            }

            try {
               int destMonsterType = destinationData.monsterId;
               ISFSObject response = new SFSObject();
               response.putLong("user_monster_id", teleportableMonsterId);
               long destNurseryId = this.transferEgg(sender, player, teleportableMonster, destMonsterType, goalIsland, admin);
               if (destNurseryId != 0L) {
                  SFSObject qe = new SFSObject();
                  qe.putInt("teleport_monster", destinationData.islandId);
                  this.serverQuestEvent(sender, qe);
                  this.ext.savePlayerIsland(player, goalIsland, true);
                  response.putLong("sent_to_island", (long)destinationData.islandId);
                  response.putLong("dest_nursery", destNurseryId);
                  this.sellMonster(sender, player, teleportableMonsterId, true, sourceIsland, true, false, false, admin);
                  response.putBool("success", true);
               } else {
                  response.putBool("has_egg", true);
                  switch(destinationData.islandId) {
                  case 7:
                     response.putUtfString("message", "NOTIFICATION_ETH_NURSERY_FULL");
                     break;
                  case 8:
                     response.putUtfString("message", "NOTIFICATION_SHUGA_NURSERY_FULL");
                     break;
                  case 19:
                     response.putUtfString("message", "NOTIFICATION_MAGICAL_NURSERY_FULL");
                     break;
                  case 21:
                     response.putUtfString("message", "NOTIFICATION_SEASONAL_NURSERY_FULL");
                     break;
                  case 23:
                     response.putUtfString("message", "NOTIFICATION_MYTHICAL_NURSERY_FULL");
                     break;
                  default:
                     response.putUtfString("message", "NOTIFICATION_NOT_ENOUGH_ROOM_IN_NURSERY");
                  }

                  response.putBool("success", false);
               }

               JSONObject metricData = new JSONObject();
               metricData.put("source_monster_id", teleportableMonster.getType());
               metricData.put("dest_monster_id", destinationData.monsterId);
               metricData.put("source_island_id", sourceIsland.getType());
               metricData.put("dest_island_id", destinationData.islandId);
               metricData.put("monster_name", teleportableMonster.getName());
               metricData.put("monster_level", teleportableMonster.getLevel());
               metricData.put("success", response.getBool("success"));
               this.ext.stats.trackTeleportMonster(sender, metricData);
               this.send(clientSentRequestId, response, sender);
            } catch (Exception var19) {
               Logger.trace(var19, "error teleporting monster", "   params : " + params.getDump());
            }

         }
      }
   }

   private Long deleteParentData(Player player, PlayerMonster childUserMonster) throws Exception {
      SFSObject parentIslandData = childUserMonster.getParentIslandData();
      if (parentIslandData == null) {
         return null;
      } else {
         long parentUserMonsterId = parentIslandData.getLong("monster");
         Long parentIslandId = parentIslandData.getLong("island");
         PlayerIsland parentIsland = player.getIslandByID(parentIslandId);
         PlayerMonster parentMonster = parentIsland.getMonsterByID(parentUserMonsterId);
         if (parentMonster != null) {
            parentMonster.markForDeletion();
            this.ext.savePlayerIsland(player, parentIsland, false);
         }

         childUserMonster.setParentIslandData(0L, 0L);
         return parentUserMonsterId;
      }
   }

   private Long deleteAssociatedGoldMonster(PlayerMonster parentMonster, Player player) throws Exception {
      SFSObject goldMonsterData = parentMonster.getGoldIslandData();
      if (goldMonsterData == null) {
         return null;
      } else {
         Long userGoldChildMonsterId = goldMonsterData.getLong("monster");
         parentMonster.setGoldIslandData(0L, 0L);
         PlayerIsland goldIsland = player.getIslandByIslandIndex(6);
         if (goldIsland != null) {
            PlayerMonster goldMonster = goldIsland.getMonsterByID(userGoldChildMonsterId);
            goldIsland.removeMonster(goldMonster, true);
            this.ext.savePlayerIsland(player, goldIsland, false);
         }

         return userGoldChildMonsterId;
      }
   }

   private void testBreeding(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         ISFSObject response = new SFSObject();
         response.putUtfString("msg", "Error! Trying to invoke admin without privileges!");
         this.send("gs_display_generic_message", response, sender);
         this.ext.sendErrorResponse("gs_test_breed_monsters", "Error! Trying to invoke admin without privileges!", sender);
      } else {
         try {
            long userMonsterId_1 = params.getLong("user_monster_id_1");
            long userMonsterId_2 = params.getLong("user_monster_id_2");
            Player player = (Player)sender.getProperty("player_object");
            PlayerIsland island = player.getActiveIsland();
            if (island.breedingFull()) {
               this.ext.sendErrorResponse("gs_breed_monsters", "Not enough breeding slots available", sender);
               return;
            }

            PlayerMonster playerMonster_1 = island.getMonsterByID(userMonsterId_1);
            if (playerMonster_1 == null) {
               this.ext.sendErrorResponse("gs_breed_monsters", "can't find monster being bred", sender);
               return;
            }

            PlayerMonster playerMonster_2 = island.getMonsterByID(userMonsterId_2);
            if (playerMonster_2 == null) {
               this.ext.sendErrorResponse("gs_breed_monsters", "can't find monster being bred", sender);
               return;
            }

            Monster monster_1 = MonsterLookup.get(playerMonster_1.getType());
            Monster monster_2 = MonsterLookup.get(playerMonster_2.getType());
            ISFSObject response = new SFSObject();
            long structureId = 0L;
            if (params.containsKey("structure_id")) {
               structureId = params.getLong("structure_id");
            }

            PlayerBreeding newBreeding = island.testBreed(structureId, monster_1, playerMonster_1, monster_2, playerMonster_2, player, VersionData.Instance().getMaxServerVersionFromClientVersion(player.lastClientVersion()), response);
            this.serverQuestEvent(sender, "breed", 1);
            this.ext.savePlayer(player);
            response.putBool("success", true);
            if (newBreeding != null) {
               response.putSFSObject("user_breeding", newBreeding.getData());
            }

            this.send("gs_test_breed_monsters", response, sender);
         } catch (Exception var17) {
            Logger.trace(var17, "error breeding monsters", "   params : " + params.getDump());
         }

      }
   }

   private void testBreedCostumes(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         ISFSObject response = new SFSObject();
         response.putUtfString("msg", "Error! Trying to invoke admin without privileges!");
         this.send("gs_display_generic_message", response, sender);
         this.ext.sendErrorResponse("gs_test_breed_costumes", "Error! Trying to invoke admin without privileges!", sender);
      } else {
         try {
            int monsterType = params.getInt("monster_type");
            ISFSObject response = new SFSObject();
            Player player = (Player)sender.getProperty("player_object");
            PlayerIsland island = player.getActiveIsland();
            ISFSObject breedingData = new SFSObject();
            breedingData.putLong("user_breeding_id", 0L);
            breedingData.putLong("island", island.getID());
            breedingData.putLong("structure", 0L);
            breedingData.putInt("monster_1", 0);
            breedingData.putInt("monster_2", 0);
            breedingData.putInt("new_monster", monsterType);
            breedingData.putLong("started_on", 0L);
            breedingData.putLong("complete_on", 0L);
            PlayerBreeding breeding = new PlayerBreeding(breedingData);
            int ITERATIONS = 100000;
            Map<Integer, Integer> bins = new HashMap();

            for(int i = 0; i < 100000; ++i) {
               this.calculateCostumeBreedingResult(player, breeding, island.getType());
               ISFSObject monsterCostumeState = breeding.getCostumeData();
               int costumeId = 0;
               if (monsterCostumeState != null) {
                  costumeId = monsterCostumeState.getInt("eq");
                  breeding.setCostumeData((MonsterCostumeState)null);
               }

               if (!bins.containsKey(costumeId)) {
                  bins.put(costumeId, 1);
               } else {
                  int count = (Integer)bins.get(costumeId);
                  bins.put(costumeId, count + 1);
               }
            }

            ISFSArray probabilities = new SFSArray();
            Iterator var18 = bins.entrySet().iterator();

            while(var18.hasNext()) {
               Entry<Integer, Integer> entry = (Entry)var18.next();
               ISFSObject probability = new SFSObject();
               probability.putInt("costume", (Integer)entry.getKey());
               probability.putFloat("probability", 100.0F * (float)(Integer)entry.getValue() / 100000.0F);
               probabilities.addSFSObject(probability);
            }

            response.putSFSArray("probabilities", probabilities);
            response.putBool("success", true);
            this.send("gs_test_breed_costumes", response, sender);
         } catch (Exception var15) {
            Logger.trace(var15, "error test breeding costumes", "   params : " + params.getDump());
         }

      }
   }

   private void testSpinProbabilities(User sender, ISFSObject params) {
      SFSObject response;
      if (sender.getPrivilegeId() != 3) {
         response = new SFSObject();
         response.putUtfString("msg", "Error! Trying to invoke admin without privileges!");
         this.send("gs_display_generic_message", response, sender);
         this.ext.sendErrorResponse("gs_test_spin_probabilities", "Error! Trying to invoke admin without privileges!", sender);
      } else {
         try {
            response = new SFSObject();
            ScratchTicketFunctions.testSpinWheel(sender, response);
            response.putBool("success", true);
            this.send("gs_test_spin_probabilities", response, sender);
         } catch (Exception var4) {
            Logger.trace(var4, "Error running spin wheel probabilities");
         }

      }
   }

   private void testScratchTicket(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         ISFSObject response = new SFSObject();
         response.putUtfString("msg", "Error! Trying to invoke admin without privileges!");
         this.send("gs_display_generic_message", response, sender);
         this.ext.sendErrorResponse("gs_test_scratch", "Error! Trying to invoke admin without privileges!", sender);
      } else {
         boolean success = true;
         String type = params.getUtfString("type");
         SFSArray arr = new SFSArray();
         if (!type.equals("M")) {
            success = false;
         } else {
            int numRuns = true;
            ArrayList<Integer> scratchResults = new ArrayList();
            Player player = (Player)sender.getProperty("player_object");
            Island island = IslandLookup.get(player.getActiveIsland().getIndex());

            int i;
            for(i = 0; i < 10000; ++i) {
               ISFSObject scratchOff = ScratchTicketFunctions.initializeScratchOff(sender, false, type, true);
               if (scratchOff == null) {
                  success = false;
                  break;
               }

               int monsterId = scratchOff.getInt("amount");
               int rewardNew = ScratchTicketFunctions.getPossibleRare(monsterId, island.getType());
               if (rewardNew != monsterId && island.hasMonster(rewardNew)) {
                  monsterId = rewardNew;
               }

               if (monsterId < scratchResults.size()) {
                  scratchResults.set(monsterId, (Integer)scratchResults.get(monsterId) + 1);
               } else {
                  while(scratchResults.size() <= monsterId) {
                     scratchResults.add(0);
                  }

                  scratchResults.set(monsterId, 1);
               }
            }

            for(i = 0; i < scratchResults.size(); ++i) {
               int timesOccurred = (Integer)scratchResults.get(i);
               if (timesOccurred != 0) {
                  SFSObject curProbability = new SFSObject();
                  curProbability.putInt("id", i);
                  curProbability.putFloat("probability", (float)timesOccurred / 10000.0F * 100.0F);
                  arr.addSFSObject(curProbability);
               }
            }
         }

         ISFSObject response = new SFSObject();
         response.putBool("success", success);
         response.putSFSArray("probabilities", arr);
         this.send("gs_test_scratch", response, sender);
      }
   }

   private void adminQASelectBreedEgg(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         ISFSObject response = new SFSObject();
         response.putUtfString("msg", "Error! Trying to invoke admin without privileges!");
         this.send("gs_display_generic_message", response, sender);
         this.ext.sendErrorResponse("gs_admin_select_breed_egg", "Error! Trying to invoke admin without privileges!", sender);
      } else {
         int monsterId = params.getInt("monster_id");
         long structureId = params.getLong("structure_id");
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         if (structureId == 0L) {
            structureId = island.getAvailableEmptyBreedingStructure();
            if (structureId == 0L) {
               this.ext.sendErrorResponse("gs_admin_select_breed_egg", "Error! No breeding structure!", sender);
               return;
            }
         }

         Monster newMonster = MonsterLookup.get(monsterId);

         try {
            PlayerBreeding newBreeding = island.makeNewBreeding(structureId, (Monster)null, (Monster)null, newMonster, player);
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            response.putSFSObject("user_breeding", newBreeding.getData());
            this.send("gs_admin_select_breed_egg", response, sender);
         } catch (Exception var11) {
            Logger.trace(var11, "error breeding monsters", "   params : " + params.getDump());
         }

      }
   }

   private void breedMonsters(User sender, ISFSObject params) {
      try {
         long userMonsterId_1 = params.getLong("user_monster_id_1");
         long userMonsterId_2 = params.getLong("user_monster_id_2");
         long islandId = -1L;
         if (params.containsKey("island_id")) {
            islandId = params.getLong("island_id");
         }

         Player player;
         PlayerIsland island;
         if (islandId != -1L) {
            if (sender.getPrivilegeId() != 3) {
               Logger.trace("breedMonsters: Error! Trying to invoke admin without privileges!");
               return;
            }

            player = (Player)sender.getProperty("friend_object");
            island = player.getIslandByID(islandId);
         } else {
            player = (Player)sender.getProperty("player_object");
            island = player.getActiveIsland();
         }

         if (island == null) {
            this.ext.sendErrorResponse("gs_breed_monsters", "Can't find island", sender);
         } else if (island.breedingFull() || island.isGoldIsland() || island.getAvailableEmptyBreedingStructure() == 0L) {
            this.ext.sendErrorResponse("gs_breed_monsters", "Not enough breeding slots available", sender);
         } else {
            PlayerMonster playerMonster_1 = island.getMonsterByID(userMonsterId_1);
            PlayerMonster playerMonster_2 = island.getMonsterByID(userMonsterId_2);
            if (playerMonster_1 == null || playerMonster_2 == null) {
               this.ext.sendErrorResponse("gs_breed_monsters", "can't find monster being bred", sender);
            } else if (playerMonster_1.getLevel() >= 4 && playerMonster_2.getLevel() >= 4) {
               Monster monster_1 = MonsterLookup.get(playerMonster_1.getType());
               Monster monster_2 = MonsterLookup.get(playerMonster_2.getType());
               if (!monster_1.isEpic() && !monster_2.isEpic()) {
                  long structureId = 0L;
                  if (params.containsKey("structure_id")) {
                     structureId = params.getLong("structure_id");
                  }

                  PlayerBreeding newBreeding = island.startNewBreed(structureId, monster_1, playerMonster_1, monster_2, playerMonster_2, player, VersionData.Instance().getMaxServerVersionFromClientVersion(player.lastClientVersion()));
                  if (newBreeding == null) {
                     this.ext.sendErrorResponse("gs_breed_monsters", "Invalid breed attempted", sender);
                  } else {
                     this.calculateCostumeBreedingResult(player, newBreeding, island.getType());
                     this.serverQuestEvent(sender, "breed", 1);
                     this.ext.savePlayer(player);
                     ISFSObject response = new SFSObject();
                     response.putBool("success", true);
                     response.putSFSObject("user_breeding", newBreeding.getData());
                     response.putLong("user_monster_1", island.getLastBredUserMonster1());
                     response.putLong("user_monster_2", island.getLastBredUserMonster2());
                     if (islandId != -1L) {
                        response.putBool("admin", true);
                     }

                     this.send("gs_breed_monsters", response, sender);
                     Monster staticMonster = MonsterLookup.get(newBreeding.getChildType());
                     this.ext.stats.trackMonsterBreeding(sender, monster_1.getMonsterID(), monster_2.getMonsterID(), staticMonster.getMonsterID());
                     GooglePlayEvents.reportMonsterBreed(sender, island.getIndex(), staticMonster.getMonsterID());
                  }
               } else {
                  this.ext.sendErrorResponse("gs_breed_monsters", "can't breed epic monsters", sender);
               }
            } else {
               this.ext.sendErrorResponse("gs_breed_monsters", "Both monsters must be at least level 4", sender);
            }
         }
      } catch (Exception var20) {
         Logger.trace(var20, "error breeding monsters", "   params : " + params.getDump());
      }
   }

   private void finishBreeding(User sender, ISFSObject params) {
      try {
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         if (island.eggsFull()) {
            this.ext.sendErrorResponse("gs_finish_breeding", "Not enough beds available in nursery", sender);
            return;
         }

         long userBreedingId = 0L;
         if (params.containsKey("user_breeding_id")) {
            userBreedingId = params.getLong("user_breeding_id");
         }

         long structureId = 0L;
         PlayerBreeding breedingEgg = island.getBreeding(userBreedingId);
         if (breedingEgg != null) {
            structureId = breedingEgg.getStructureID();
         }

         PlayerEgg newEgg = island.finishBreeding(player, userBreedingId, false);
         if (newEgg == null) {
            this.ext.sendErrorResponse("gs_finish_breeding", "The breeding has not yet completed", sender);
            return;
         }

         String sqlRequest = "DELETE FROM user_facebook_help_instances WHERE island_id=? AND ( structure_id = 0 OR structure_id = ? ) AND type=?";
         Object[] arguments = new Object[]{island.getID(), structureId, "breeding"};
         boolean speedup = false;
         if (params.containsKey("speedup")) {
            speedup = params.getBool("speedup");
         }

         ISFSObject response = new SFSObject();
         if (speedup) {
            int diamondCost = Game.DiamondsRequiredToComplete(newEgg.getTimeRemaining());
            if (!player.canBuy(0L, 0L, (long)diamondCost, 0L, 0L, 0L, 0)) {
               this.ext.sendErrorResponse("gs_speed_up_hatching", "You do not have enough diamonds to speed up hatching", sender);
            } else {
               if (newEgg.getTimeRemaining() > 5L) {
                  player.chargePlayer(sender, this, 0, 0, diamondCost, 0L, 0, 0, 0);
                  JSONObject speedupInfo = MSMStats.extraSpeedupInfo(newEgg.getTimeRemaining(), newEgg.getInitialTimeRemaining(), newEgg.getType());
                  this.logDiamondUsage(sender, "speedup_hatch", diamondCost, player.getLevel(), speedupInfo, MonsterLookup.get(newEgg.getType()).getEntityId());
                  newEgg.finishHatchingNow();
               }

               ISFSArray responseVars = new SFSArray();
               player.addPlayerPropertyData(responseVars, false);
               response.putSFSArray("properties", responseVars);
            }
         }

         this.ext.getDB().update(sqlRequest, arguments);
         response.putBool("success", true);
         response.putSFSObject("user_egg", newEgg.getData());
         response.putLong("user_breeding_id", userBreedingId);
         this.send("gs_finish_breeding", response, sender);
         this.serverQuestEvent(sender, "monster", newEgg.getType());
         this.ext.savePlayer(player);
         this.ext.stats.trackMonsterBreedingCollect(sender, newEgg.getType(), breedingEgg.toSFSObject(), newEgg.toSFSObject());
      } catch (Exception var17) {
         Logger.trace(var17, "error during finish breeding", "   params : " + params.getDump());
      }

   }

   private void breedingSpeedUp(User sender, ISFSObject params) {
      try {
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         long userBreedingId = 0L;
         if (params.containsKey("user_breeding_id")) {
            userBreedingId = params.getLong("user_breeding_id");
         }

         PlayerBreeding playerBreeding = island.getBreeding(userBreedingId);
         if (null != params.getInt("speed_up_type") && 0 != params.getInt("speed_up_type")) {
            if (1 == params.getInt("speed_up_type")) {
               if (!player.hasSpeedUpCredit()) {
                  this.ext.sendErrorResponse("gs_speed_up_breeding", "Unfortunately there are currently no offers available to you. Please try again later", sender);
                  return;
               }

               this.ext.stats.trackSpeedupWithVideo(sender, "speedup_breeding", (long)playerBreeding.getChildType(), playerBreeding.getID(), playerBreeding.getTimeRemaining(), playerBreeding.getInitialTimeRemaining());
               playerBreeding.reduceBreedingTimeByVideo();
               player.adjustSpeedUpCredit(sender, this, -1, Player.SPEED_UP_TYPES.VIDEO);
            }
         } else {
            int diamondCost = Game.DiamondsRequiredToComplete(playerBreeding.getTimeRemaining());
            if (!player.canBuy(0L, 0L, (long)diamondCost, 0L, 0L, 0L, 0)) {
               this.ext.sendErrorResponse("gs_finish_breeding", "You do not have enough diamonds to speed up breeding", sender);
               return;
            }

            if (playerBreeding.getTimeRemaining() > 5L) {
               player.chargePlayer(sender, this, 0, 0, diamondCost, 0L, 0, 0, 0);
               JSONObject speedupInfo = MSMStats.extraSpeedupInfo(playerBreeding.getTimeRemaining(), playerBreeding.getInitialTimeRemaining(), playerBreeding.getChildType());
               this.logDiamondUsage(sender, "speedup_breeding", diamondCost, player.getLevel(), speedupInfo, MonsterLookup.get(playerBreeding.getChildType()).getEntityId());
               playerBreeding.finishBreedingNow();
            }
         }

         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         response.putLong("userBreedingId", userBreedingId);
         response.putLong("complete_on", playerBreeding.getCompletionTime());
         response.putLong("started_on", playerBreeding.getStartTime());
         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_speed_up_breeding", response, sender);
      } catch (Exception var10) {
         Logger.trace(var10, "error during breeding speedup", "   params : " + params.getDump());
      }

   }

   private int rollRandomCostume(Player player, int monsterType, int islandType) {
      int costumeId = 0;
      String clientPlatform = player.getPlatform();
      String clientSubplatform = player.getSubplatform();
      VersionInfo maxServerVersion = VersionData.Instance().getMaxServerVersionFromClientVersion(player.lastClientVersion());
      int minPlayerLevel = GameSettings.get("MIN_PLAYER_COSTUME_BREED_LEVEL", 10);
      if (player.getLevel() < minPlayerLevel) {
         return 0;
      } else {
         Random random = gRandom;
         float costumeBreedChance = GameSettings.get("COSTUME_BREED_CHANCE", 0.5F);
         costumeBreedChance *= player.getBuffs().getMultiplier(PlayerBuffs.Buffs.CostumeChanceIncrease, islandType);
         if (random.nextFloat() < costumeBreedChance) {
            int baseMonsterType = MonsterIslandToIslandMapping.monsterSourceGivenAnyIsland(monsterType);
            if (baseMonsterType > 0) {
               monsterType = baseMonsterType;
            }

            List<CostumeData> costumes = CostumeLookup.instance.getCostumesForMonster(player, monsterType, islandType, clientPlatform, clientSubplatform, maxServerVersion);
            double totalProbability = 0.0D;

            CostumeData costumeData;
            for(Iterator var15 = costumes.iterator(); var15.hasNext(); totalProbability += costumeData.getBreedChance()) {
               costumeData = (CostumeData)var15.next();
            }

            double roll = random.nextDouble() * totalProbability;
            double cumulativeProbability = 0.0D;
            Iterator var19 = costumes.iterator();

            while(var19.hasNext()) {
               CostumeData costumeData = (CostumeData)var19.next();
               cumulativeProbability += costumeData.getBreedChance();
               if (roll < cumulativeProbability) {
                  costumeId = costumeData.getId();
                  break;
               }
            }
         }

         return costumeId;
      }
   }

   private void calculateCostumeBreedingResult(Player player, PlayerBreeding breeding, int islandType) {
      int costumeId = this.rollRandomCostume(player, breeding.getChildType(), islandType);
      if (costumeId > 0) {
         MonsterCostumeState mcs = new MonsterCostumeState();
         mcs.setPurchased(costumeId);
         mcs.setEquipped(costumeId);
         breeding.setCostumeData(mcs);
      }

   }

   private void calculateCostumeHatchingResult(Player player, PlayerEgg hatching, int islandType) {
      int costumeId = this.rollRandomCostume(player, hatching.getType(), islandType);
      if (costumeId > 0) {
         MonsterCostumeState mcs = new MonsterCostumeState();
         mcs.setPurchased(costumeId);
         mcs.setEquipped(costumeId);
         hatching.setCostumeData(mcs);
      }

   }

   private void processAdminNeighborChangeMsg(User sender, ISFSObject params) {
      if (params.containsKey("user_monster_id")) {
         this.adminChangeUserHappiness(sender, params);
      }

   }

   private void processAdminMultiNeighborChangeMsg(User sender, ISFSObject params) {
      Player friend = (Player)sender.getProperty("friend_object");
      long userIslandId = params.getLong("user_island_id");
      PlayerIsland island = friend.getIslandByID(userIslandId);
      boolean sendMonsterUpdateResponse = false;
      ISFSObject response = new SFSObject();
      if (island.hasHappinessTree()) {
         SFSArray monsterEffects = this.addHappyTreeEffects(island, (User)null);
         response.putSFSArray("monster_happy_effects", monsterEffects);
         sendMonsterUpdateResponse = true;
      } else {
         ISFSArray entities = params.getSFSArray("entity_array");
         ISFSArray updateTheseMonsters = new SFSArray();

         for(int i = 0; i < entities.size(); ++i) {
            ISFSObject curEntity = entities.getSFSObject(i);
            ISFSArray neighbors = curEntity.getSFSArray("neighbors");
            if (curEntity.containsKey("user_monster_id")) {
               long userMonsterId = curEntity.getLong("user_monster_id");
               response.putLong("user_monster_id", userMonsterId);
               ISFSObject monsterUpdate = this.generateMonsterHappinessUpdate(friend, userMonsterId, neighbors, island, sender);
               if (monsterUpdate != null) {
                  updateTheseMonsters.addSFSObject(monsterUpdate);
               }
            }
         }

         if (updateTheseMonsters.size() > 0) {
            sendMonsterUpdateResponse = true;
            response.putSFSArray("update_monster_list", updateTheseMonsters);
         }
      }

      if (sendMonsterUpdateResponse) {
         try {
            this.ext.savePlayerIsland(friend, island, false);
            response.putBool("success", true);
            this.send("gs_admin_multi_update_monster", response, sender);
         } catch (Exception var17) {
            Logger.trace(var17, "error during save of user island data", "   params : " + params.getDump());
         }
      }

   }

   private void processMultiNeighborChangeMsg(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      PlayerIsland island = player.getActiveIsland();
      boolean sendMonsterUpdateResponse = false;
      ISFSObject response = new SFSObject();
      int i;
      ISFSObject curEntity;
      ISFSArray neighbors;
      if (island.hasHappinessTree()) {
         SFSArray monsterEffects = this.addHappyTreeEffects(island, sender);
         response.putSFSArray("monster_happy_effects", monsterEffects);
         sendMonsterUpdateResponse = true;
         ISFSArray entities = params.getSFSArray("entity_array");

         for(i = 0; i < entities.size(); ++i) {
            curEntity = entities.getSFSObject(i);
            neighbors = curEntity.getSFSArray("neighbors");
            this.triggerNeighborQuest(neighbors, sender);
         }
      } else {
         ISFSArray entities = params.getSFSArray("entity_array");
         ISFSArray updateTheseMonsters = new SFSArray();

         for(i = 0; i < entities.size(); ++i) {
            curEntity = entities.getSFSObject(i);
            neighbors = curEntity.getSFSArray("neighbors");
            if (curEntity.containsKey("user_monster_id")) {
               long userMonsterId = curEntity.getLong("user_monster_id");
               response.putLong("user_monster_id", userMonsterId);
               ISFSObject monsterUpdate = this.generateMonsterHappinessUpdate(player, userMonsterId, neighbors, island, sender);
               if (monsterUpdate != null) {
                  updateTheseMonsters.addSFSObject(monsterUpdate);
               }
            }

            this.triggerNeighborQuest(neighbors, sender);
         }

         if (updateTheseMonsters.size() > 0) {
            sendMonsterUpdateResponse = true;
            response.putSFSArray("update_monster_list", updateTheseMonsters);
         }
      }

      if (sendMonsterUpdateResponse) {
         response.putBool("success", true);
         this.send("gs_multi_update_monster", response, sender);
      } else {
         response.putBool("success", false);
         this.send("gs_multi_update_monster", response, sender);
      }

   }

   private ISFSObject generateMonsterHappinessUpdate(Player p, long userMonsterId, ISFSArray neighbors, PlayerIsland island, User sender) {
      if (island.noMonsterHappinessIsland()) {
         return null;
      } else {
         SFSObject updateObject = null;

         try {
            PlayerMonster playerMonster = island.getMonsterByID(userMonsterId);
            if (playerMonster == null) {
               return updateObject;
            }

            int oldHappiness = playerMonster.getHappiness();
            int happiness = this.updateMonsterHappiness(island, playerMonster, neighbors);
            if (!island.isUnderlingIsland()) {
               if (oldHappiness != happiness) {
                  playerMonster.stashCurrency(p, island);
               }

               if (happiness == 100) {
                  this.serverQuestEvent(sender, "happy", playerMonster.getID());
               }

               if (happiness > 0) {
                  this.serverQuestEvent(sender, "any_happiness", 1);
               }
            }

            updateObject = new SFSObject();
            updateObject.putLong("user_monster_id", playerMonster.getID());
            updateObject.putInt("happiness", playerMonster.getHappiness());
            updateObject.putLong("last_collection", playerMonster.getLastCollectedTime());
            updateObject.putInt("collected_coins", playerMonster.getCollectedCoins());
            updateObject.putInt("collected_ethereal", playerMonster.getCollectedEth());
            updateObject.putDouble("collected_relics", (double)playerMonster.getCollectedRelics());
         } catch (Exception var11) {
            Logger.trace(var11, "error during change happiness");
         }

         return updateObject;
      }
   }

   private int updateMonsterHappiness(PlayerIsland island, PlayerMonster playerMonster, ISFSArray neighbors) {
      int happiness = false;
      int happiness;
      if (playerMonster.isInactiveBoxMonster()) {
         happiness = 0;
      } else if (island.hasHappinessTree()) {
         happiness = this.getMonsterHappinessWithEntireIsland(playerMonster, island, GameSettings.getInt("MONSTER_HAPPINESS_INCREMENTS"));
      } else {
         ISFSArray happinessFactors = MonsterLookup.get(playerMonster.getType()).getLikes();
         happiness = this.calculateHappiness(playerMonster.getType(), neighbors, happinessFactors);
      }

      playerMonster.setHappiness(happiness);
      return happiness;
   }

   private int calculateHappiness(int myType, ISFSArray neighbors, ISFSArray happinessFactors) {
      int happinessIncrements = GameSettings.getInt("MONSTER_HAPPINESS_INCREMENTS");
      int happiness = 0;
      HashMap<Integer, Boolean> alreadyAddedList = new HashMap();
      Monster monster = MonsterLookup.get(myType);

      for(int i = 0; i < neighbors.size(); ++i) {
         ISFSObject neighbor = neighbors.getSFSObject(i);
         int neighborEntityId = neighbor.getInt("id");
         int origEntityId = neighborEntityId;
         Monster neighborMonster = MonsterLookup.getFromEntityId(neighborEntityId);
         int j;
         if (neighborMonster != null) {
            j = neighborMonster.getMonsterID();
            if (j == myType) {
               continue;
            }

            if (!monster.isUnderling() && MonsterCommonToRareMapping.rareToCommonMapContainsKey(j)) {
               int commonMonsterId = MonsterCommonToRareMapping.rareToCommonMapGet(j).commonMonsterId();
               neighborEntityId = MonsterLookup.get(commonMonsterId).getEntityId();
            }
         }

         if (!alreadyAddedList.containsKey(neighborEntityId)) {
            for(j = 0; j < happinessFactors.size(); ++j) {
               ISFSObject factor = happinessFactors.getSFSObject(j);
               if (neighborEntityId == factor.getInt("entity") || origEntityId == factor.getInt("entity")) {
                  happiness += factor.getInt("value") * happinessIncrements;
                  alreadyAddedList.put(neighborEntityId, true);
                  break;
               }

               if (monster.isUnderling()) {
                  Monster factorMonster = MonsterLookup.getFromEntityId(factor.getInt("entity"));
                  if (factorMonster != null) {
                     int factorMonsterId = factorMonster.getMonsterID();
                     if (MonsterCommonToRareMapping.rareToCommonMapContainsKey(factorMonsterId)) {
                        if (neighborEntityId == factor.getInt("entity")) {
                           happiness += factor.getInt("value") * happinessIncrements;
                           alreadyAddedList.put(neighborEntityId, true);
                        } else {
                           int commonMonsterId = MonsterCommonToRareMapping.rareToCommonMapGet(factorMonsterId).commonMonsterId();
                           int commonMonsterEntityId = MonsterLookup.get(commonMonsterId).getEntityId();
                           if (neighborEntityId == commonMonsterEntityId) {
                              happiness = (int)((double)happiness + (double)(factor.getInt("value") * happinessIncrements) * 0.5D);
                              alreadyAddedList.put(neighborEntityId, true);
                           }
                        }
                     } else if (!MonsterCommonToEpicMapping.epicToCommonMapContainsKey(factorMonsterId)) {
                        if (neighborEntityId == factor.getInt("entity")) {
                           happiness += factor.getInt("value") * happinessIncrements;
                           alreadyAddedList.put(neighborEntityId, true);
                        } else {
                           ArrayList<MonsterRareData> rareDataArr = MonsterCommonToRareMapping.commonToRareMapGet(factorMonsterId);
                           if (rareDataArr != null) {
                              Iterator rareItr = rareDataArr.iterator();

                              while(rareItr.hasNext()) {
                                 MonsterRareData rareDat = (MonsterRareData)rareItr.next();
                                 int rareMonsterId = rareDat.rareMonsterId();
                                 int rareMonsterEntityId = MonsterLookup.get(rareMonsterId).getEntityId();
                                 if (neighborEntityId == rareMonsterEntityId) {
                                    happiness += factor.getInt("value") * happinessIncrements;
                                    alreadyAddedList.put(neighborEntityId, true);
                                    break;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      happiness = Math.max(monster.isUnderling() ? -100 : 0, happiness);
      happiness = Math.min(100, happiness);
      return happiness;
   }

   private void triggerNeighborQuest(ISFSArray neighbors, User sender) {
      if (neighbors != null) {
         ISFSArray neighborIds = new SFSArray();

         for(int i = 0; i < neighbors.size(); ++i) {
            neighborIds.addInt(neighbors.getSFSObject(i).getInt("id"));
         }

         ISFSObject qe = new SFSObject();
         qe.putSFSArray("neighbor", neighborIds);
         qe.putBool("server_generated", true);
         this.serverQuestEvent(sender, qe);
      }
   }

   private void adminChangeUserHappiness(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminChangeUserHappiness: Error! Trying to invoke admin without privileges!");
      } else {
         try {
            Player friend = (Player)sender.getProperty("friend_object");
            long userIslandId = params.getLong("user_island_id");
            PlayerIsland island = friend.getIslandByID(userIslandId);
            long userMonsterId = params.getLong("user_monster_id");
            PlayerMonster monster = island.getMonsterByID(userMonsterId);
            ISFSArray neighbors = params.getSFSArray("neighbors");
            int monsterId = monster.getType();
            Monster monsterType = MonsterLookup.get(monsterId);
            ISFSArray happinessFactors = null;
            happinessFactors = monsterType.getLikes();
            int happiness = 0;
            if (happinessFactors != null) {
               happiness = this.calculateHappiness(monsterId, neighbors, happinessFactors);
               monster.setHappiness(happiness);
            }

            this.ext.savePlayerIsland(friend, island, false);
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            if (happinessFactors != null) {
               response = new SFSObject();
               response.putLong("user_monster_id", userMonsterId);
               response.putInt("happiness", happiness);
               this.send("gs_admin_update_users_monster", response, sender);
            }
         } catch (Exception var16) {
            Logger.trace(var16, "error during change happiness");
         }

      }
   }

   private void buyIsland(User sender, ISFSObject params) {
      try {
         Player player = (Player)sender.getProperty("player_object");
         if (player.duplicateCommand("gs_buy_island", 5000L)) {
            this.ext.sendErrorResponse("gs_buy_island", "", sender);
            return;
         }

         boolean starpowerPurchase = false;
         if (params.containsKey("starpower_purchase")) {
            starpowerPurchase = params.getBool("starpower_purchase");
         }

         int newIslandIndex = params.getInt("island_id");
         Collection<PlayerIsland> playerIslands = player.getIslands();
         Iterator islandData = playerIslands.iterator();

         PlayerIsland playerIsland;
         while(islandData.hasNext()) {
            playerIsland = (PlayerIsland)islandData.next();
            if (newIslandIndex == playerIsland.getIndex()) {
               this.ext.sendErrorResponse("gs_buy_island", "You already own this island", sender);
               return;
            }
         }

         islandData = null;
         playerIsland = null;
         int costCoins = 0;
         int costDiamonds = 0;
         int costStarpower = 0;
         Island islandData;
         if (newIslandIndex != 9) {
            islandData = IslandLookup.get(newIslandIndex);
            if (!starpowerPurchase) {
               costDiamonds = islandData.getDiamondCost();
               costCoins = islandData.getCoinCost();
            } else {
               costStarpower = islandData.getStarpowerCost();
            }

            if (player.timedEventsUnlocked() && IslandSalesEvent.hasTimedEventNow(islandData, player, player.getActiveIsland().getType())) {
               costDiamonds = IslandSalesEvent.getTimedEventSaleCostDiamonds(islandData, player);
               costCoins = IslandSalesEvent.getTimedEventSaleCostCoins(islandData, player);
            }

            if (!player.canBuy((long)costCoins, 0L, (long)costDiamonds, (long)costStarpower, 0L, 0L, 0)) {
               this.ext.sendErrorResponse("gs_buy_island", "You do not have enough currency to buy a new island", sender);
               return;
            }

            long userIslandId = this.ext.createPlayerIsland(player, newIslandIndex);
            String sql = "SELECT * FROM user_islands where user_island_id = ?";
            Object[] args = new Object[]{userIslandId};
            ISFSArray newIslandResult = this.ext.getDB().query(sql, args);
            playerIsland = new PlayerIsland(newIslandResult.getSFSObject(0));
            this.ext.loadPlayerIslandData(player, playerIsland);
            playerIsland.initIslandThemeModifiers(player.getIslandThemes());
            player.addIsland(playerIsland);
            ISFSArray defaultEggs = SFSArray.newFromJsonData(GameSettings.get("DEFAULT_ISLAND_EGGS_V2"));
            if (newIslandIndex < defaultEggs.size() && defaultEggs.getInt(newIslandIndex) != -1) {
               Iterator var30 = playerIsland.getStructures().iterator();

               while(var30.hasNext()) {
                  PlayerStructure structure = (PlayerStructure)var30.next();
                  if (structure.canHoldEggs()) {
                     Monster staticMonsterClaimed = MonsterLookup.get(defaultEggs.getInt(newIslandIndex));
                     playerIsland.addNewEggToIsland(player, staticMonsterClaimed, structure.getID(), true, false, false, "", (ISFSObject)null, (ISFSObject)null, (String)null, false);
                     break;
                  }
               }
            }

            player.chargePlayer(sender, this, costCoins, 0, costDiamonds, (long)costStarpower, 0, 0, 0);
            if (costDiamonds > 0) {
               this.logDiamondUsage(sender, "buy_island_" + Integer.toString(islandData.getID()), costDiamonds, player.getLevel());
            } else if (costStarpower > 0) {
               this.logStarpowerUsage(sender, "buy_island", costStarpower, player.getLevel());
            }
         } else {
            String islandName = Helpers.sanitizeName(params.getUtfString("island_name"), " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!¨\"#$&'()*+,-./:;<=>?@{}0123456789|£©¿®`~^ÀÁÂÄÇÈÉÊËÌÍÎÏÑÒÓÔÖÙÚÛÜßàáâäçèéêëìíîïñòóôöùúûü_ÆæÃãÕõАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя€₽¡");
            String errMsg = Helpers.invalidName(player.getPlayerId(), islandName, "TRIBAL_NAME", ProperNouns.getNouns());
            SFSObject response;
            if (errMsg != null) {
               response = new SFSObject();
               response.putUtfString("message", errMsg);
               response.putBool("success", false);
               this.send("gs_buy_island", response, sender);
               return;
            }

            if (this.ext.getDB().exists("SELECT * FROM user_tribal_mappings WHERE user=?", new Object[]{player.getPlayerId()})) {
               response = new SFSObject();
               response.putUtfString("message", "Player already in a tribe");
               response.putBool("success", false);
               this.send("gs_buy_island", response, sender);
               return;
            }

            islandData = IslandLookup.get(newIslandIndex);
            long userIslandId = this.ext.createTribalIsland(player, islandName);
            String sql = "SELECT * FROM user_islands where user_island_id = ?";
            Object[] args = new Object[]{userIslandId};
            ISFSArray newIslandResult = this.ext.getDB().query(sql, args);
            playerIsland = new PlayerIsland(newIslandResult.getSFSObject(0));
            player.addIsland(playerIsland);
            this.ext.loadPlayerTribalIslandData(player, playerIsland, true);
            this.ext.stats.trackNewTribalIsland(sender, playerIsland.getTribalID(), islandName);
         }

         this.ext.savePlayer(player);
         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         if (params.containsKey("no_change_island") && params.getBool("no_change_island")) {
            response.putBool("no_change_island", true);
         }

         response.putSFSObject("user_island", playerIsland.toSFSObject());
         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         this.serverQuestEvent(sender, "island", islandData.getID());
         response.putSFSArray("properties", responseVars);
         response.putSFSArray("tracks", player.getTracks());
         response.putSFSArray("songs", player.getSongs());
         this.send("gs_buy_island", response, sender);
         player.setLastCommand("gs_buy_island");
         this.ext.stats.trackIslandTransaction(sender, newIslandIndex, new MetricCost((long)costCoins, costDiamonds, 0L, (long)costStarpower));
         Firebase.reportIsland(sender, newIslandIndex);
         GooglePlayEvents.reportIsland(sender, newIslandIndex);
      } catch (Exception var21) {
         Logger.trace(var21, "**** error buying island ****", "   params : " + params.getDump());
      }

   }

   private void changeIsland(User sender, ISFSObject params) {
      long islandId = params.getLong("user_island_id");
      Player player = (Player)sender.getProperty("player_object");
      ISFSObject response = new SFSObject();
      PlayerIsland island = player.getIslandByID(islandId);
      if (player.getActiveIslandId() != islandId && island != null) {
         this.ext.savePlayer(player);
         this.serverQuestEvent(sender, "island", island.getIndex());
         player.setActiveIsland(islandId);
         response.putBool("success", true);
         if (params.containsKey("user_structure_focus")) {
            response.putLong("user_structure_focus", params.getLong("user_structure_focus"));
         }

         if (params.containsKey("user_monster_focus")) {
            response.putLong("user_monster_focus", params.getLong("user_monster_focus"));
         }

         response.putLong("user_island_id", islandId);
      } else {
         response.putBool("success", false);
      }

      this.send("gs_change_island", response, sender);
   }

   private void adminPlaceOnGoldIsland(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminPlaceOnGoldIsland: Error! Trying to invoke admin without privileges!");
      } else {
         Player player = (Player)sender.getProperty("friend_object");
         Long playerIslandId = params.getLong("user_island_id");
         PlayerIsland island = player.getIslandByID(playerIslandId);
         long parentMonsterId = params.getLong("user_monster_id");
         int posX = params.getInt("pos_x");
         int posY = params.getInt("pos_y");
         int flip = params.getInt("flip");
         long parentIslandId = params.getLong("user_parent_island_id");

         try {
            this.placeOnGoldIsland(sender, player, island, parentIslandId, parentMonsterId, posX, posY, flip);
         } catch (Exception var14) {
            Logger.trace(var14, "**** error placing monster on gold island  ****", "   params : " + params.getDump());
         }

      }
   }

   private void placeOnGoldIsland(User sender, ISFSObject params) {
      try {
         if (params.containsKey("user_id")) {
            this.adminPlaceOnGoldIsland(sender, params);
         } else {
            Player player = (Player)sender.getProperty("player_object");
            long parentMonsterId = params.getLong("user_monster_id");
            int posX = params.getInt("pos_x");
            int posY = params.getInt("pos_y");
            int flip = params.getInt("flip");
            long parentIslandId = params.getLong("user_parent_island_id");
            PlayerIsland goldIsland = player.getActiveIsland();
            PlayerIsland parentIsland = player.getIslandByID(parentIslandId);
            PlayerMonster parentMonster = parentIsland.getMonsterByID(parentMonsterId);
            SFSObject goldMonsterData;
            if (goldIsland != null && parentIsland != null && parentMonster != null && goldIsland.isGoldIsland() && !parentIsland.isTribalIsland()) {
               goldMonsterData = parentMonster.getGoldIslandData();
               boolean goldMonsterExists = false;
               if (goldMonsterData != null) {
                  Long userGoldIslandId = goldMonsterData.getLong("island");
                  PlayerIsland referencedGoldIsland = player.getIslandByID(userGoldIslandId);
                  if (referencedGoldIsland != null) {
                     Long userGoldChildMonsterId = goldMonsterData.getLong("monster");
                     if (referencedGoldIsland.getMonsterByID(userGoldChildMonsterId) != null) {
                        goldMonsterExists = true;
                     }
                  }
               }

               if (goldMonsterExists) {
                  ISFSObject response = new SFSObject();
                  response.putBool("success", false);
                  response.putLong("parent_id", parentMonsterId);
                  this.send("gs_place_on_gold_island", response, sender);
               } else {
                  this.placeOnGoldIsland(sender, player, goldIsland, parentIslandId, parentMonsterId, posX, posY, flip);
               }
            } else {
               goldMonsterData = new SFSObject();
               goldMonsterData.putBool("success", false);
               goldMonsterData.putLong("parent_id", parentMonsterId);
               this.send("gs_place_on_gold_island", goldMonsterData, sender);
            }
         }
      } catch (Exception var19) {
         Logger.trace(var19, "**** error placing monster on gold island  ****", "   params : " + params.getDump());
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         this.send("gs_place_on_gold_island", response, sender);
      }

   }

   private void placeOnGoldIsland(User sender, Player player, PlayerIsland goldIsland, long parentIslandId, long parentMonsterId, int posX, int posY, int flip) throws Exception {
      PlayerIsland parentIsland = player.getIslandByID(parentIslandId);
      PlayerMonster parentMonster = parentIsland.getMonsterByID(parentMonsterId);
      Monster parentMonsterData = MonsterLookup.get(parentMonster.getType());
      if (parentMonster.getLevel() >= GameSettings.getInt("GOLD_ISLAND_LEVEL") && parentMonsterData.isGoldUpgradeMonster()) {
         int monsterType = parentMonster.getType();
         if (parentMonsterData.isEpic() && parentMonsterData.isWubbox()) {
            monsterType = GameSettings.get("USER_GOLD_EPIC_WUBBOX_BASE_MONSTER_ID", 670);
         }

         SFSObject newMonsterData = PlayerMonster.createMonsterSFS(monsterType, parentMonster.getName(), parentMonster.getMegaSFS(), parentMonster.getCostumeSFS(), goldIsland.getIndex(), goldIsland.getID(), posX, posY, flip, player.getNextMonsterIndex(), parentMonster.getLevel(), MSMExtension.CurrentDBTime(), MSMExtension.CurrentDBTime(), false, 0L, (String)null, false, false);
         PlayerMonster newGoldMonster = new PlayerMonster(newMonsterData, goldIsland);
         newGoldMonster.getCostumeState().setEquipped(parentMonster.getCostumeState().getEquipped());
         goldIsland.addMonster(newGoldMonster);
         newGoldMonster.setParentIslandData(parentIslandId, parentMonsterId);
         parentMonster.setGoldIslandData(goldIsland.getID(), newGoldMonster.getID());
         Monster staticMonster = MonsterLookup.get(newGoldMonster.getType());
         if (staticMonster.isBoxMonsterType()) {
            newGoldMonster.setBoxMonsterData(new SFSArray(), goldIsland.isGoldIsland());
         }

         if (staticMonster.isEvolvable()) {
            newGoldMonster.setEvolveDataStatic(new SFSArray());
            newGoldMonster.setEvolveDataFlex(new SFSArray());
         }

         if ((Player)sender.getProperty("player_object") == player) {
            this.ext.savePlayer(player);
         } else {
            this.ext.savePlayerIsland(player, goldIsland, false);
         }

         this.ext.savePlayerIsland(player, parentIsland, false);
         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         response.putSFSObject("monster", newGoldMonster.toSFSObject(goldIsland));
         response.putLong("user_monster_id", parentMonsterId);
         this.send("gs_place_on_gold_island", response, sender);
         ISFSObject qe = new SFSObject();
         int entityId = newGoldMonster.getEntityId();
         if (MonsterCommonToRareMapping.rareToCommonMapContainsKey(newGoldMonster.getType())) {
            int commonType = MonsterCommonToRareMapping.rareToCommonMapGet(newGoldMonster.getType()).commonMonsterId();
            Monster commonMonster = MonsterLookup.get(commonType);
            entityId = commonMonster.getEntityId();
         }

         qe.putInt("object", entityId);
         qe.putInt("on_island", goldIsland.getIndex());
         this.serverQuestEvent(sender, qe);
         this.ext.stats.trackGoldIslandMonster(sender, newGoldMonster.getType(), newGoldMonster.getLevel(), newGoldMonster.getID());
      } else {
         ISFSObject badResponse = new SFSObject();
         badResponse.putBool("success", false);
         badResponse.putLong("parent_id", parentMonsterId);
         this.send("gs_place_on_gold_island", badResponse, sender);
      }
   }

   private void saveIslandWarpSpeed(User sender, ISFSObject params) {
      double warpSpeed = params.getDouble("warp_speed");
      long islandId = params.getLong("user_island_id");
      boolean admin = false;
      if (params.containsKey("admin")) {
         admin = params.getBool("admin");
      }

      Player player;
      if (admin) {
         if (sender.getPrivilegeId() != 3) {
            Logger.trace("saveIslandWarpSpeed: Error! Trying to invoke admin without privileges!");
            return;
         }

         player = (Player)sender.getProperty("friend_object");
      } else {
         player = (Player)sender.getProperty("player_object");
      }

      PlayerIsland isle = player.getIslandByID(islandId);
      isle.setWarpSpeed(warpSpeed);

      try {
         if (admin) {
            this.ext.savePlayerIsland(player, isle, false);
         }

         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         this.send("gs_save_island_warp_speed", response, sender);
      } catch (Exception var11) {
         Logger.trace(var11, "**** error in saveIslandWarpSpeed ****", "   params : " + params.getDump());
      }

   }

   private void placeOnTribal(User sender, ISFSObject params) {
      try {
         long parentMonsterId = params.getLong("user_monster_id");
         long memberId = params.getLong("member_id");
         int posX = params.getInt("pos_x");
         int posY = params.getInt("pos_y");
         int flip = params.getInt("flip");
         boolean isChief = params.getInt("chief") == 1;
         Player chief = null;
         if (params.containsKey("user_id")) {
            if (sender.getPrivilegeId() != 3) {
               Logger.trace("admin - placeOnTribal: Error! Trying to invoke admin without privileges!");
               return;
            }

            chief = (Player)sender.getProperty("friend_object");
         } else {
            chief = (Player)sender.getProperty("player_object");
         }

         PlayerIsland tribalIsland = chief.getIslandByIslandIndex(9);
         if (tribalIsland == null || tribalIsland.getTribalRequests() == null || tribalIsland.getTribalIslandData() == null || tribalIsland.getTribalIslandData().getLong("members") >= (long)GameSettings.getInt("USER_MAX_TRIBE_MEMBERS")) {
            ISFSObject response = new SFSObject();
            response.putBool("success", false);
            this.send("gs_place_on_tribal", response, sender);
            return;
         }

         ISFSArray reqs = tribalIsland.getTribalRequests();
         int requestIndex = -1;
         int monsterType = 0;
         String monsterName = "";
         if (isChief) {
            PlayerMonster parentMonster = null;
            Collection<PlayerIsland> islands = chief.getIslands();
            Iterator var19 = islands.iterator();

            while(var19.hasNext()) {
               PlayerIsland pi = (PlayerIsland)var19.next();
               Collection<PlayerMonster> monsters = pi.getMonsters();
               Iterator var22 = monsters.iterator();

               while(var22.hasNext()) {
                  PlayerMonster pm = (PlayerMonster)var22.next();
                  if (pm.getID() == parentMonsterId) {
                     parentMonster = pm;
                     break;
                  }
               }

               if (parentMonster != null) {
                  break;
               }
            }

            if (parentMonster == null || !IslandLookup.get(tribalIsland.getIndex()).hasMonster(parentMonster.getType())) {
               ISFSObject response = new SFSObject();
               response.putBool("success", false);
               this.send("gs_place_on_tribal", response, sender);
            }

            monsterType = parentMonster.getType();
            monsterName = chief.getDisplayName();
         }

         for(int i = 0; i < reqs.size(); ++i) {
            ISFSObject request = reqs.getSFSObject(i);
            if (request.getLong("user") == memberId) {
               requestIndex = i;
               if (!isChief) {
                  monsterType = request.getInt("monster");
                  monsterName = request.getUtfString("name");
               }
               break;
            }
         }

         SFSObject response;
         if (requestIndex != -1 && !islandHasTribalMonster(tribalIsland, memberId) && !playerInOtherTribe(tribalIsland, memberId)) {
            response = PlayerMonster.createMonsterSFS(monsterType, monsterName, (ISFSObject)null, (ISFSObject)null, 9, tribalIsland.getTribalID(), posX, posY, flip, memberId, 1, MSMExtension.CurrentDBTime(), MSMExtension.CurrentDBTime(), false, 0L, (String)null, false, false);
            PlayerMonster newMonster = new PlayerMonster(response, tribalIsland);
            tribalIsland.addMonster(newMonster);
            String sql;
            if (isChief) {
               sql = "DELETE FROM user_tribal_requests WHERE user=? AND tribe=?";
               this.ext.getDB().update(sql, new Object[]{chief.getPlayerId(), tribalIsland.getTribalID()});
            } else {
               sql = "INSERT INTO user_tribal_requests SET user=?, tribe=?, monster=?, name=?, status='accepted', date_created=NOW(), last_updated=NOW() ON DUPLICATE KEY UPDATE status='accepted', last_updated=NOW()";
               this.ext.getDB().update(sql, new Object[]{tribalIsland.getTribalRequests().getSFSObject(requestIndex).getLong("user"), tribalIsland.getTribalID(), monsterType, monsterName});
               sql = "DELETE FROM user_tribal_requests WHERE user=? AND tribe!=?";
               this.ext.getDB().update(sql, new Object[]{tribalIsland.getTribalRequests().getSFSObject(requestIndex).getLong("user"), tribalIsland.getTribalID()});
            }

            tribalIsland.getTribalRequests().removeElementAt(requestIndex);
            sql = "INSERT INTO user_tribal_monsters SET user_monster_id=?, island=?, monster=?, name=?, last_feeding=NOW(), last_collection=NOW(), level=1, pos_x=?, pos_y=?, flip=?, date_created=NOW(), last_updated=NOW()";
            this.ext.getDB().update(sql, new Object[]{newMonster.getID(), newMonster.getParentID(), newMonster.getType(), newMonster.getName(), newMonster.getXPosition(), newMonster.getYPosition(), newMonster.getFlip()});
            sql = "INSERT IGNORE INTO user_tribal_mappings SET user=?, tribe=?, date_created=NOW()";
            this.ext.getDB().update(sql, new Object[]{memberId, tribalIsland.getTribalID()});
            sql = "INSERT IGNORE INTO user_tribal_starpower SET user=?";
            this.ext.getDB().update(sql, new Object[]{memberId});
            sql = "UPDATE user_tribal_islands SET rank=(SELECT COALESCE(SUM(level), 0) FROM user_tribal_monsters WHERE island=?), members=(SELECT COUNT(*) FROM user_tribal_mappings WHERE tribe=?), last_updated=NOW() WHERE user_island_id=?";
            this.ext.getDB().update(sql, new Object[]{tribalIsland.getTribalID(), tribalIsland.getTribalID(), tribalIsland.getTribalID()});
            tribalIsland.getTribalIslandData().putLong("rank", tribalIsland.getTribalIslandData().getLong("rank") + 1L);
            if (!isChief) {
               tribalIsland.getTribalIslandData().putLong("members", tribalIsland.getTribalIslandData().getLong("members") + 1L);
            }

            sql = "SELECT bbb_id, level FROM users WHERE user_id=?";
            ISFSArray users = this.ext.getDB().query(sql, new Object[]{memberId});
            if (users != null && users.size() > 0) {
               long memberBbbId = users.getSFSObject(0).getLong("bbb_id");
               int level = users.getSFSObject(0).getInt("level");
               this.ext.stats.trackTribalMonsterPlaced(sender, chief.getBbbId(), memberBbbId, level, tribalIsland.getTribalID(), newMonster.getType());
            }

            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            response.putSFSObject("monster", newMonster.toSFSObject(tribalIsland));
            response.putInt("remove_index", requestIndex);
            this.send("gs_place_on_tribal", response, sender);
         } else {
            response = new SFSObject();
            response.putBool("success", false);
            this.send("gs_place_on_tribal", response, sender);
         }
      } catch (Exception var24) {
         Logger.trace(var24, "**** error placing monster on tribal island  ****", "   params : " + params.getDump());
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         this.send("gs_place_on_tribal", response, sender);
      }

   }

   private void adminSwapTribalMonster(User sender, ISFSObject params) {
      try {
         long parentMonsterId = params.getLong("user_monster_id");
         long memberId = params.getLong("member_id");
         int posX = params.getInt("pos_x");
         int posY = params.getInt("pos_y");
         int flip = params.getInt("flip");
         Player player = null;
         if (params.containsKey("user_id")) {
            if (sender.getPrivilegeId() != 3) {
               Logger.trace("admin - adminSwapTribalMonster: Error! Trying to invoke admin without privileges!");
               return;
            }

            player = (Player)sender.getProperty("friend_object");
         }

         PlayerIsland tribalIsland = player.getIslandByIslandIndex(9);
         if (tribalIsland == null || tribalIsland.getTribalIslandData() == null) {
            ISFSObject response = new SFSObject();
            response.putBool("success", false);
            this.send("gs_admin_swap_tribal_monster", response, sender);
            return;
         }

         PlayerMonster currentMonster = null;
         Iterator tribalMonsters = tribalIsland.getMonsters().iterator();

         PlayerMonster parentMonster;
         while(tribalMonsters.hasNext()) {
            parentMonster = (PlayerMonster)tribalMonsters.next();
            if (parentMonster.getID() == memberId) {
               currentMonster = parentMonster;
            }
         }

         parentMonster = null;
         Collection<PlayerIsland> islands = player.getIslands();
         Iterator var16 = islands.iterator();

         while(var16.hasNext()) {
            PlayerIsland pi = (PlayerIsland)var16.next();
            Collection<PlayerMonster> monsters = pi.getMonsters();
            Iterator var19 = monsters.iterator();

            while(var19.hasNext()) {
               PlayerMonster pm = (PlayerMonster)var19.next();
               if (pm.getID() == parentMonsterId) {
                  parentMonster = pm;
                  break;
               }
            }

            if (parentMonster != null) {
               break;
            }
         }

         SFSObject newMonsterData;
         if (parentMonster == null || !IslandLookup.get(tribalIsland.getIndex()).hasMonster(parentMonster.getType())) {
            newMonsterData = new SFSObject();
            newMonsterData.putBool("success", false);
            this.send("gs_admin_swap_tribal_monster", newMonsterData, sender);
         }

         tribalIsland.removeMonster(currentMonster, true);
         newMonsterData = PlayerMonster.createMonsterSFS(parentMonster.getType(), parentMonster.getName(), (ISFSObject)null, (ISFSObject)null, 9, tribalIsland.getTribalID(), posX, posY, flip, memberId, 1, MSMExtension.CurrentDBTime(), MSMExtension.CurrentDBTime(), false, 0L, (String)null, false, false);
         PlayerMonster newMonster = new PlayerMonster(newMonsterData, tribalIsland);
         tribalIsland.addMonster(newMonster);
         String sql = "UPDATE user_tribal_monsters SET monster=?, pos_x=?, pos_y=?, flip=? where user_monster_id=? ";
         this.ext.getDB().update(sql, new Object[]{newMonster.getType(), posX, posY, flip, memberId});
         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         response.putSFSObject("monster", newMonster.toSFSObject(tribalIsland));
         this.send("gs_admin_swap_tribal_monster", response, sender);
      } catch (Exception var21) {
         Logger.trace(var21, "**** error swapping monster on tribal island  ****", "   params : " + params.getDump());
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         this.send("gs_admin_swap_tribal_monster", response, sender);
      }

   }

   private static boolean islandHasTribalMonster(PlayerIsland tribalIsland, long playerId) {
      Iterator monsters = tribalIsland.getMonsters().iterator();

      PlayerMonster curMonster;
      do {
         if (!monsters.hasNext()) {
            return false;
         }

         curMonster = (PlayerMonster)monsters.next();
      } while(curMonster.getID() != playerId);

      return true;
   }

   private static boolean playerInOtherTribe(PlayerIsland tribalIsland, long playerId) {
      String sql = "SELECT * FROM user_tribal_mappings WHERE user=?";

      try {
         ISFSArray res = MSMExtension.getInstance().getDB().query(sql, new Object[]{playerId});
         if (res != null && res.size() > 0) {
            return res.getSFSObject(0).getLong("tribe") != tribalIsland.getTribalID();
         } else {
            return false;
         }
      } catch (Exception var5) {
         Logger.trace(var5, "**** error checking tribal status  ****");
         return true;
      }
   }

   private void cancelTribeInvite(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      ISFSObject response = new SFSObject();
      long tribeID = params.getLong("tribe_id");

      try {
         if (tribeID < 0L) {
            tribeID = -tribeID;
            PlayerIsland pi = player.getIslandByIslandIndex(9);
            if (pi != null && pi.getTribalRequests() != null) {
               String sql = "UPDATE user_tribal_requests SET status='declined', last_updated=NOW() WHERE user=? and tribe=?";
               this.ext.getDB().update(sql, new Object[]{tribeID, pi.getTribalID()});
               response.putBool("success", true);
            } else {
               response.putBool("success", false);
            }
         } else {
            String sql = "DELETE FROM user_tribal_requests WHERE user=? AND tribe=? AND status='invited'";
            this.ext.getDB().update(sql, new Object[]{player.getPlayerId(), tribeID});
            response.putBool("success", true);
            this.ext.stats.trackTribalReqCancel(sender);
         }
      } catch (Exception var9) {
         Logger.trace(var9, "**** error removing tribal invite  ****", "   params : " + params.getDump());
         response.putBool("success", false);
      }

      this.send("gs_cancel_tribe_request", response, sender);
      this.refreshTribeRequests(sender, params);
   }

   private void declineAllTribeInvites(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      ISFSObject response = new SFSObject();
      String sql = "DELETE FROM user_tribal_requests WHERE user=? AND status='invited'";

      try {
         this.ext.getDB().update(sql, new Object[]{player.getPlayerId()});
         response.putBool("success", true);
      } catch (Exception var7) {
         Logger.trace(var7, "**** error removing all tribal invites  ****", "   params : " + params.getDump());
         response.putBool("success", false);
      }

      this.ext.stats.trackTribalReqCancel(sender);
      this.send("gs_decline_all_tribal_invites", response, sender);
      this.refreshTribeRequests(sender, params);
   }

   private void cancelTribeRequest(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      SFSObject response = new SFSObject();

      try {
         String sql = "DELETE FROM user_tribal_requests WHERE user=? AND (status='pending' OR status='declined')";
         this.ext.getDB().update(sql, new Object[]{player.getPlayerId()});
         response.putBool("success", true);
         this.ext.stats.trackTribalReqCancel(sender);
      } catch (Exception var6) {
         Logger.trace(var6, "**** error removing tribal requests  ****", "   params : " + params.getDump());
         response.putBool("success", false);
      }

      this.send("gs_cancel_tribe_request", response, sender);
   }

   private void sendTribeRequest(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      long tribeID = params.getLong("tribe_id");
      long monsterID = params.getLong("monster_id");
      PlayerMonster monster = null;
      if (player.getIslandByIslandIndex(9) != null) {
         this.ext.sendErrorResponse("gs_send_tribe_request", "You can't request to join a tribe when you're already in one", sender);
      } else {
         Collection<PlayerIsland> islands = player.getIslands();
         Iterator var10 = islands.iterator();

         while(var10.hasNext()) {
            PlayerIsland pi = (PlayerIsland)var10.next();
            Collection<PlayerMonster> monsters = pi.getMonsters();
            Iterator var13 = monsters.iterator();

            while(var13.hasNext()) {
               PlayerMonster pm = (PlayerMonster)var13.next();
               if (pm.getID() == monsterID) {
                  monster = pm;
                  break;
               }
            }

            if (monster != null) {
               break;
            }
         }

         if (monster != null && IslandLookup.get(9).hasMonster(monster.getType())) {
            SFSObject response = new SFSObject();

            try {
               if (this.ext.getDB().exists("SELECT * FROM user_tribal_requests WHERE user=? AND status='pending'", new Object[]{player.getPlayerId()})) {
                  this.ext.sendErrorResponse("gs_send_tribe_request", "You already have an outstanding tribal request", sender);
                  return;
               }

               if (!this.ext.getDB().exists("SELECT * FROM user_tribal_islands WHERE user_island_id=?", new Object[]{tribeID})) {
                  this.ext.sendErrorResponse("gs_send_tribe_request", "That tribe no longer exists", sender);
                  return;
               }

               String sql = "INSERT INTO user_tribal_requests SET user=?, tribe=?, monster=?, name=?, date_created=NOW(), last_updated=NOW() ON DUPLICATE KEY UPDATE monster=?, name=?, status=IF(status='invited', 'pending', status), last_updated=NOW()";
               this.ext.getDB().update(sql, new Object[]{player.getPlayerId(), tribeID, monster.getType(), player.getDisplayName(), monster.getType(), player.getDisplayName()});
               response.putBool("success", true);
               this.ext.stats.trackTribalIslandRequest(sender, tribeID, monster.getType(), player.getDisplayName());
            } catch (Exception var15) {
               Logger.trace(var15, "**** error adding tribal requests  ****", "   params : " + params.getDump());
               response.putBool("success", false);
            }

            this.getFriends(sender, params);
            this.send("gs_send_tribe_request", response, sender);
         } else {
            this.ext.sendErrorResponse("gs_send_tribe_request", "You don't have that monster or it isn't wanted on tribal island", sender);
         }
      }
   }

   private void sendTribeInvite(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      long userID = params.getLong("user_id");
      PlayerIsland tribalIsland = player.getIslandByIslandIndex(9);
      ISFSObject response = new SFSObject();
      if (tribalIsland == null) {
         response.putUtfString("msg", "TRIBAL_ISLAND_INVITE_NOT_IN_TRIBE");
         this.send("gs_display_generic_message", response, sender);
         this.ext.sendErrorResponse("gs_send_tribe_invite", "TRIBAL_ISLAND_INVITE_NOT_IN_TRIBE", sender);
      } else {
         try {
            String sql = "INSERT IGNORE INTO user_tribal_requests SET user=?, tribe=?, status='invited', date_created=NOW(), last_updated=NOW()";
            this.ext.getDB().update(sql, new Object[]{userID, tribalIsland.getTribalID()});
            response.putBool("success", true);
         } catch (Exception var9) {
            Logger.trace(var9, "**** error adding tribal invite  ****", "   params : " + params.getDump());
            response.putBool("success", false);
         }

         this.send("gs_send_tribe_invite", response, sender);
      }
   }

   private void joinTribe(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      SFSObject response = new SFSObject();

      try {
         String sql = "SELECT * FROM user_tribal_requests WHERE user=? AND status='accepted'";
         ISFSArray reqs = this.ext.getDB().query(sql, new Object[]{player.getPlayerId()});
         if (reqs == null || reqs.size() == 0) {
            this.ext.sendErrorResponse("gs_join_tribe", "You don't have an accepted tribal request", sender);
            return;
         }

         long tribe = reqs.getSFSObject(0).getLong("tribe");
         long userIslandId = this.ext.joinTribalIsland(player, tribe);
         sql = "SELECT * FROM user_islands where user_island_id = ?";
         ISFSArray newIslandResult = this.ext.getDB().query(sql, new Object[]{userIslandId});
         PlayerIsland playerIsland = new PlayerIsland(newIslandResult.getSFSObject(0));
         player.addIsland(playerIsland);
         this.ext.loadPlayerTribalIslandData(player, playerIsland, true);
         this.ext.savePlayer(player);
         response.putBool("success", true);
         response.putSFSObject("user_island", playerIsland.toSFSObject());
         this.ext.stats.trackTribalIslandJoin(sender, tribe);
      } catch (Exception var13) {
         Logger.trace(var13, "**** error joining tribal island  ****", "   params : " + params.getDump());
         response.putBool("success", false);
      }

      this.send("gs_join_tribe", response, sender);
   }

   private void leaveTribeRequest(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      ISFSObject response = new SFSObject();
      long newChief = params.getLong("chief");

      try {
         PlayerIsland tribalIsland = player.getIslandByIslandIndex(9);
         if (tribalIsland == null) {
            this.ext.sendErrorResponse("gs_leave_tribe_request", "You don't have a tribal island to leave", sender);
            player.setActiveIsland(player.getIslandByIslandIndex(1).getID());
            return;
         }

         long members = tribalIsland.getTribalIslandData().getLong("members");
         long chief = tribalIsland.getTribalIslandData().getLong("chief");
         if (members > 1L && chief == player.getPlayerId() && (newChief == player.getPlayerId() || tribalIsland.getMonsterByID(newChief) == null)) {
            this.ext.sendErrorResponse("gs_leave_tribe_request", "The new chief is not a member of this tribe", sender);
            player.setActiveIsland(player.getIslandByIslandIndex(1).getID());
            return;
         }

         if (newChief != -1L && !this.ext.getDB().exists("SELECT * FROM user_tribal_mappings WHERE user=? AND tribe=?", new Object[]{newChief, tribalIsland.getTribalID()})) {
            this.ext.sendErrorResponse("gs_leave_tribe_request", "The new chief is no longer a member of this tribe", sender);
            player.setActiveIsland(player.getIslandByIslandIndex(1).getID());
            return;
         }

         String sql = "DELETE FROM user_tribal_monsters WHERE user_monster_id=?";
         this.ext.getDB().update(sql, new Object[]{player.getPlayerId()});
         sql = "DELETE FROM user_tribal_mappings WHERE user=?";
         this.ext.getDB().update(sql, new Object[]{player.getPlayerId()});
         sql = "DELETE FROM user_islands WHERE user_island_id=?";
         this.ext.getDB().update(sql, new Object[]{tribalIsland.getID()});
         if (members <= 1L) {
            sql = "DELETE FROM user_tribal_islands WHERE user_island_id=?";
            this.ext.getDB().update(sql, new Object[]{tribalIsland.getTribalID()});
            sql = "DELETE FROM user_tribal_requests WHERE tribe=?";
            this.ext.getDB().update(sql, new Object[]{tribalIsland.getTribalID()});
         } else {
            sql = "DELETE FROM user_tribal_requests WHERE user=? AND tribe=?";
            this.ext.getDB().update(sql, new Object[]{player.getPlayerId(), tribalIsland.getTribalID()});
            if (chief == player.getPlayerId()) {
               sql = "UPDATE user_tribal_islands SET rank=(SELECT COALESCE(SUM(level), 0) FROM user_tribal_monsters WHERE island=?), members=(SELECT COUNT(*) FROM user_tribal_mappings WHERE tribe=?), chief=?, last_updated=NOW() WHERE user_island_id=?";
               this.ext.getDB().update(sql, new Object[]{tribalIsland.getTribalID(), tribalIsland.getTribalID(), newChief, tribalIsland.getTribalID()});
            } else {
               sql = "UPDATE user_tribal_islands SET rank=(SELECT COALESCE(SUM(level), 0) FROM user_tribal_monsters WHERE island=?), members=(SELECT COUNT(*) FROM user_tribal_mappings WHERE tribe=?), last_updated=NOW() WHERE user_island_id=?";
               this.ext.getDB().update(sql, new Object[]{tribalIsland.getTribalID(), tribalIsland.getTribalID(), tribalIsland.getTribalID()});
               sql = "UPDATE user_tribal_islands SET chief=(SELECT user_monster_id FROM user_tribal_monsters WHERE island=? ORDER BY LEVEL DESC, user_tribal_monsters.last_updated DESC LIMIT 1) WHERE user_island_id=? AND chief=?";
               this.ext.getDB().update(sql, new Object[]{tribalIsland.getTribalID(), tribalIsland.getTribalID(), player.getPlayerId()});
            }

            sql = "DELETE FROM user_tribal_islands WHERE user_island_id=? and user_island_id not in(select tribe from user_tribal_mappings where tribe=?)";
            this.ext.getDB().update(sql, new Object[]{tribalIsland.getTribalID(), tribalIsland.getTribalID()});
            sql = "DELETE FROM user_tribal_requests WHERE tribe=? and tribe not in(select user_island_id from user_tribal_islands where user_island_id=?)";
            this.ext.getDB().update(sql, new Object[]{tribalIsland.getTribalID(), tribalIsland.getTribalID()});
         }

         this.ext.stats.trackTribalIslandQuit(sender, tribalIsland.getTribalID(), chief == player.getPlayerId());
         player.getIslands().remove(tribalIsland);
         player.setActiveIsland(player.getIslandByIslandIndex(1).getID());
         response.putBool("success", true);
      } catch (Exception var13) {
         Logger.trace(var13, "**** error leaving tribe  ****", "   params : " + params.getDump());
         response.putBool("success", false);
      }

      this.send("gs_leave_tribe_request", response, sender);
   }

   private void kickTribeRequest(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      ISFSObject response = new SFSObject();
      long user = params.getLong("user");

      try {
         PlayerIsland tribalIsland = player.getIslandByIslandIndex(9);
         if (tribalIsland == null) {
            this.ext.sendErrorResponse("gs_kick_tribe_request", "You don't have a tribal to kick users from", sender);
            return;
         }

         PlayerMonster monster = tribalIsland.getMonsterByID(user);
         if (tribalIsland.getTribalIslandData().getLong("chief") != player.getPlayerId() || monster == null && user != player.getPlayerId()) {
            this.ext.sendErrorResponse("gs_kick_tribe_request", "Only the chief can kick users, and they must exist, and can't be yourself", sender);
            return;
         }

         String sql = "SELECT * FROM user_islands WHERE user=?";
         ISFSArray islands = this.ext.getDB().query(sql, new Object[]{user});
         long userIsland = 0L;

         for(int i = 0; i < islands.size(); ++i) {
            if (islands.getSFSObject(i).containsKey("tribal_id") && islands.getSFSObject(i).getLong("tribal_id") == tribalIsland.getTribalID()) {
               userIsland = islands.getSFSObject(i).getLong("user_island_id");
               break;
            }
         }

         tribalIsland.removeMonster(monster, true);
         sql = "DELETE FROM user_tribal_monsters WHERE user_monster_id=?";
         this.ext.getDB().update(sql, new Object[]{user});
         sql = "DELETE FROM user_tribal_mappings WHERE user=?";
         this.ext.getDB().update(sql, new Object[]{user});
         sql = "DELETE FROM user_tribal_requests WHERE user=? AND tribe=?";
         this.ext.getDB().update(sql, new Object[]{user, tribalIsland.getTribalID()});
         if (userIsland != 0L) {
            sql = "DELETE FROM user_islands WHERE user_island_id=?";
            this.ext.getDB().update(sql, new Object[]{userIsland});
         }

         sql = "UPDATE user_tribal_islands SET rank=(SELECT COALESCE(SUM(level), 0) FROM user_tribal_monsters WHERE island=?), members=(SELECT COUNT(*) FROM user_tribal_mappings WHERE tribe=?), last_updated=NOW() WHERE user_island_id=?";
         this.ext.getDB().update(sql, new Object[]{tribalIsland.getTribalID(), tribalIsland.getTribalID(), tribalIsland.getTribalID()});
         sql = "SELECT bbb_id, level FROM users WHERE user_id=?";
         ISFSArray users = this.ext.getDB().query(sql, new Object[]{user});
         if (users != null && users.size() > 0) {
            long memberBbbId = users.getSFSObject(0).getLong("bbb_id");
            int memberLevel = users.getSFSObject(0).getInt("level");
            this.ext.stats.trackTribalIslandKick(sender, memberBbbId, memberLevel, monster.getType(), monster.getLevel(), tribalIsland.getTribalID());
         }

         response.putLong("user_monster_id", user);
         response.putBool("success", true);
      } catch (Exception var17) {
         Logger.trace(var17, "**** error kicking user from tribe  ****", "   params : " + params.getDump());
         response.putBool("success", false);
      }

      this.send("gs_kick_tribe_request", response, sender);
   }

   private void updateTribeName(User sender, ISFSObject params) {
      String islandName = Helpers.sanitizeName(params.getUtfString("island_name"), " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!¨\"#$&'()*+,-./:;<=>?@{}0123456789|£©¿®`~^ÀÁÂÄÇÈÉÊËÌÍÎÏÑÒÓÔÖÙÚÛÜßàáâäçèéêëìíîïñòóôöùúûü_ÆæÃãÕõАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя€₽¡");
      Player player = (Player)sender.getProperty("player_object");
      String errMsg = Helpers.invalidName(player.getPlayerId(), islandName, "TRIBAL_NAME", ProperNouns.getNouns());
      if (errMsg != null) {
         ISFSObject response = new SFSObject();
         response.putUtfString("message", errMsg);
         response.putBool("success", false);
         this.send("gs_set_tribename", response, sender);
      } else {
         PlayerIsland tribalIsland = player.getIslandByIslandIndex(9);
         SFSObject response;
         if (tribalIsland == null) {
            response = new SFSObject();
            response.putUtfString("message", "INVALID_ISLAND");
            response.putBool("success", false);
            this.send("gs_set_tribename", response, sender);
         } else {
            try {
               String sql = "UPDATE user_tribal_islands SET name=?, last_updated=NOW() WHERE chief=?";
               this.ext.getDB().update(sql, new Object[]{islandName, player.getPlayerId()});
            } catch (Exception var9) {
               Logger.trace(var9, "**** error renaming tribe****", "   params : " + params.getDump());
               ISFSObject response = new SFSObject();
               response.putUtfString("message", "SERVER_ERROR");
               response.putBool("success", false);
               this.send("gs_set_tribename", response, sender);
               return;
            }

            response = new SFSObject();
            response.putBool("success", true);
            response.putUtfString("displayName", islandName);
            ISFSObject o = tribalIsland.getTribalIslandData();
            o.putUtfString("name", islandName);
            response.putSFSObject("tribal_island_data", o);
            this.send("gs_set_tribename", response, sender);
         }
      }
   }

   private void updateIslandName(User sender, ISFSObject params) {
      String islandName = Helpers.sanitizeName(params.getUtfString("island_name"), " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!¨\"#$&'()*+,-./:;<=>?@{}0123456789|£©¿®`~^ÀÁÂÄÇÈÉÊËÌÍÎÏÑÒÓÔÖÙÚÛÜßàáâäçèéêëìíîïñòóôöùúûü_ÆæÃãÕõ€¡");
      long islandId = params.getLong("island_id");
      Player player = (Player)sender.getProperty("player_object");
      String errMsg = Helpers.invalidName(player.getPlayerId(), islandName, "COMPOSER_NAME", ProperNouns.getNouns());
      if (errMsg != null) {
         ISFSObject response = new SFSObject();
         response.putUtfString("message", errMsg);
         response.putBool("success", false);
         this.send("gs_set_islandname", response, sender);
      } else {
         PlayerIsland island = player.getIslandByID(islandId);
         SFSObject response;
         if (island == null) {
            response = new SFSObject();
            response.putUtfString("message", "INVALID_ISLAND");
            response.putBool("success", false);
            this.send("gs_set_islandname", response, sender);
         } else if (island.getType() != 11) {
            response = new SFSObject();
            response.putUtfString("message", "RENAMING_INVALID_ISLAND");
            response.putBool("success", false);
            this.send("gs_set_islandname", response, sender);
         } else {
            try {
               String sql = "UPDATE user_islands SET name=? WHERE user=? and user_island_id=?";
               this.ext.getDB().update(sql, new Object[]{islandName, player.getPlayerId(), islandId});
            } catch (Exception var11) {
               Logger.trace(var11, "**** error renaming island****", "   params : " + params.getDump());
               ISFSObject response = new SFSObject();
               response.putBool("success", false);
               this.send("gs_set_islandname", response, sender);
            }

            response = new SFSObject();
            response.putBool("success", true);
            response.putLong("islandId", islandId);
            response.putUtfString("displayName", islandName);
            this.send("gs_set_islandname", response, sender);
         }
      }
   }

   private void refreshTribeRequests(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      PlayerIsland pi = player.getActiveIsland();
      ISFSObject response = new SFSObject();
      if (pi.getTribalIslandData() != null && pi.getTribalIslandData().getLong("chief") == player.getPlayerId()) {
         String sql = "SELECT * FROM user_tribal_requests WHERE tribe=? AND status='pending'";
         Object[] query_params = new Object[]{pi.getTribalID()};

         try {
            ISFSArray requestData = this.ext.getDB().query(sql, query_params);
            pi.initTribalRequests(requestData);
         } catch (Exception var9) {
            Logger.trace(var9, "**** error renaming tribe****", "   params : " + params.getDump());
            response.putBool("success", false);
            this.send("gs_set_tribename", response, sender);
         }

         response.putSFSArray("tribal_requests", pi.getTribalRequests());
      }

      ISFSArray requests = this.getRequests(player, sender);
      response.putBool("success", true);
      response.putSFSArray("invites", (ISFSArray)(requests != null ? requests : new SFSArray()));
      response.putBool("success", true);
      this.send("gs_refresh_tribe_requests", response, sender);
   }

   private ISFSArray getRequests(Player player, User sender) {
      SFSArray requests = null;

      try {
         String sql = "SELECT * FROM user_tribal_requests WHERE user=? LIMIT " + GameSettings.get("FRIEND_MAX_LOAD");
         requests = this.ext.getDB().query(sql, new Object[]{player.getPlayerId()});
      } catch (Exception var5) {
         Logger.trace(var5, "Error getting request data from DB");
      }

      return requests;
   }

   private void activateIslandTheme(User sender, ISFSObject params) {
      try {
         Player player = (Player)sender.getProperty("player_object");
         Integer islandThemeId = params.getInt("island_theme_id");
         boolean buyAndActivateNow = false;
         if (params.containsKey("buy_and_activate_now")) {
            buyAndActivateNow = params.getBool("buy_and_activate_now");
         }

         boolean trial = false;
         if (params.containsKey("trial")) {
            trial = params.getBool("trial");
         }

         int newIslandThemeId = -1;
         boolean owned = false;
         if (islandThemeId != null) {
            newIslandThemeId = islandThemeId;
            ISFSArray playerIslandThemes = player.getIslandThemes();

            for(int i = 0; i < playerIslandThemes.size(); ++i) {
               if (newIslandThemeId == playerIslandThemes.getInt(i)) {
                  owned = true;
               }
            }
         }

         if (newIslandThemeId != -1) {
            IslandTheme requestedIslandTheme = (IslandTheme)IslandThemeLookup.themes.get(newIslandThemeId);
            if (requestedIslandTheme == null || requestedIslandTheme.getData() == null) {
               this.ext.sendErrorResponse("gs_activate_island_theme", "ACTIVATE_THEME_ERROR_INVALID_THEME_ID", sender);
               return;
            }

            if (owned) {
               player.toggleIslandTheme(requestedIslandTheme.getIsland(), newIslandThemeId);
            } else {
               boolean availabilityEventRunning = TimedEventManager.instance().hasTimedEventNow(TimedEventType.IslandThemeAvailability, requestedIslandTheme.getId(), requestedIslandTheme.getIsland());
               if (trial || !requestedIslandTheme.getViewInMarket() && !availabilityEventRunning) {
                  if (availabilityEventRunning) {
                     player.toggleTrialIslandTheme(requestedIslandTheme.getIsland(), requestedIslandTheme.getId());
                  }
               } else {
                  int costCoins = requestedIslandTheme.getCoinCost();
                  int costEth = requestedIslandTheme.getShardCost();
                  int costDiamonds = requestedIslandTheme.getDiamondCost();
                  int costStarpower = requestedIslandTheme.getStarpowerCost();
                  int costKeys = requestedIslandTheme.getKeyCost();
                  int costRelics = requestedIslandTheme.getRelicCost();
                  if (!player.canBuy((long)costCoins, (long)costEth, (long)costDiamonds, (long)costStarpower, (long)costKeys, (long)costRelics, 0)) {
                     this.ext.sendErrorResponse("gs_activate_island_theme", "NOTIFICATION_NOT_ENOUGH_DIAMONDS", sender);
                     return;
                  }

                  if (buyAndActivateNow || player.hasTrialTheme(requestedIslandTheme.getIsland()) == newIslandThemeId) {
                     player.removeTrialTheme(requestedIslandTheme.getIsland());
                  }

                  player.addIslandTheme(newIslandThemeId);
                  player.getActiveIsland().initIslandThemeModifiers(player.getIslandThemes());
                  player.chargePlayer(sender, this, costCoins, costEth, costDiamonds, (long)costStarpower, costKeys, costRelics, 0);
                  if (costDiamonds > 0) {
                     this.logDiamondUsage(sender, "buy_island_theme_" + Integer.toString(newIslandThemeId), costDiamonds, player.getLevel());
                  } else if (costStarpower > 0) {
                     this.logStarpowerUsage(sender, "buy_island_theme", costStarpower, player.getLevel());
                  }

                  if (buyAndActivateNow) {
                     player.toggleIslandTheme(requestedIslandTheme.getIsland(), newIslandThemeId);
                  }
               }
            }

            if (!trial) {
               this.ext.stats.trackIslandTheme(sender, requestedIslandTheme.getIsland(), newIslandThemeId, player.hasIslandThemeEnabled(requestedIslandTheme.getIsland()), buyAndActivateNow, owned);
            }

            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            response.putInt("user_island_theme_id", newIslandThemeId);
            response.putBool("buy_and_activate_now", buyAndActivateNow);
            if (trial) {
               response.putBool("trial", trial);
            }

            response.putInt("island", requestedIslandTheme.getIsland());
            ISFSArray responseVars = new SFSArray();
            player.addPlayerPropertyData(responseVars, false);
            response.putSFSArray("properties", responseVars);
            this.send("gs_activate_island_theme", response, sender);
         }
      } catch (Exception var17) {
         Logger.trace(var17);
      }

   }

   private void buyStructure(User sender, ISFSObject params) {
      try {
         int structureId = params.getInt("structure_id");
         int pos_x = params.getInt("pos_x");
         int pos_y = params.getInt("pos_y");
         int flip = params.getInt("flip");
         double scale = params.containsKey("scale") ? params.getDouble("scale") : 1.0D;
         boolean starpowerPurchase = false;
         if (params.containsKey("starpower_purchase")) {
            starpowerPurchase = params.getBool("starpower_purchase");
         }

         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland pi = player.getActiveIsland();
         Structure structure = StructureLookup.get(structureId);
         if (!pi.isComposerIsland() && !pi.isGoldIsland() && !pi.isTribalIsland() && structure.allowedOnIsland(IslandLookup.get(pi.getType()))) {
            if (player.getLevel() < structure.getLevelUnlocked()) {
               TutorialGroup t = player.getTutorialGroup();
               if (t == null || !t.clientTutorial().equals("streamlined") || player.getLevel() >= 4 || structureId != PlayerIslandFactory.streamlinedBakeryStructId()) {
                  this.ext.sendErrorResponse("gs_buy_structure", "player not high enough level for this decoration", sender);
                  return;
               }
            } else if (player.getBattleState().getLevel() < structure.getBattleLevelUnlocked()) {
               this.ext.sendErrorResponse("gs_buy_structure", "player not high enough battle level for this decoration", sender);
               return;
            }

            if (structure.isAwakener()) {
               int calendar = structure.getExtra().getInt("calendar");
               if (player.getDailyCumulativeLogin().getCalendarId() < calendar) {
                  this.ext.sendErrorResponse("gs_buy_structure", "requires completing previous conundrum", sender);
                  return;
               }
            }

            boolean isComplete = structure.getBuildTimeMs() == 0;
            Integer mailIndex = null;
            Float colorR = null;
            Float colorY = null;
            Float colorB = null;
            if (params.containsKey("mailId")) {
               long mailId = params.getLong("mailId");

               for(int i = 0; i < player.getMailbox().size(); ++i) {
                  ISFSObject curMail = player.getMailbox().getSFSObject(i);
                  if (SFSHelpers.getLong("user_mail_id", curMail) == mailId) {
                     ISFSObject attachment = curMail.getSFSObject("attachment");
                     if (attachment.getUtfString("type").equals("entity") && SFSHelpers.getInt("id", attachment) == structure.getEntityId()) {
                        mailIndex = i;
                        if (attachment.containsKey("colorR")) {
                           colorR = attachment.getFloat("colorR");
                        }

                        if (attachment.containsKey("colorY")) {
                           colorY = attachment.getFloat("colorY");
                        }

                        if (attachment.containsKey("colorB")) {
                           colorB = attachment.getFloat("colorB");
                        }

                        isComplete = true;
                        break;
                     }
                  }
               }

               if (mailIndex == null) {
                  this.ext.sendErrorResponse("gs_buy_structure", "Mail attachment did not match structure placement attempt", sender);
                  return;
               }
            }

            boolean questClaimValidated = false;
            long questClaimId = 0L;
            if (params.containsKey("quest_claim_id")) {
               questClaimId = params.getLong("quest_claim_id");
            }

            if (questClaimId != 0L) {
               ArrayList<PlayerQuest> quests = player.getQuests();

               for(int i = 0; i < quests.size(); ++i) {
                  if (((PlayerQuest)quests.get(i)).getId() == questClaimId && ((PlayerQuest)quests.get(i)).isComplete()) {
                     PlayerQuest quest = (PlayerQuest)quests.get(i);
                     if (quest.collected() == 0) {
                        ISFSObject rewards = quest.getRewards();
                        if (rewards != null && rewards.containsKey("entity") && rewards.getInt("entity") == structure.getEntityId()) {
                           questClaimValidated = true;
                        }
                     }
                     break;
                  }
               }
            }

            PlayerIsland island = player.getActiveIsland();
            boolean hasCredit = player.getInventory().hasItem(structure.getEntityId());
            ISFSObject response = new SFSObject();
            if (mailIndex != null || questClaimValidated || structure.canPurchaseFromStore(false, player, island, starpowerPurchase, VersionData.Instance().getMaxServerVersionFromClientVersion(player.lastClientVersion())) && !island.isGoldIsland() && !island.isTribalIsland()) {
               if (!hasCredit && structure.isPremium() && !player.hasMadePurchase() && !questClaimValidated && mailIndex == null) {
                  this.ext.sendErrorResponse("gs_buy_structure", "You are not allowed to purchase this item", sender);
               } else {
                  int costDiamonds = 0;
                  int costEth = 0;
                  int costCoins = 0;
                  int costStarpower = 0;
                  int costMedals = 0;
                  boolean etherealIsland = island.isEtherealIsland();
                  if (mailIndex == null && !questClaimValidated) {
                     if (starpowerPurchase) {
                        costStarpower = structure.getCostStarpower(island.getType());
                     } else {
                        costDiamonds = structure.getCostDiamonds(island.getType());
                        costEth = etherealIsland ? structure.getCostEth(island.getType()) : 0;
                        costCoins = structure.getCostCoins(island.getType());
                        costMedals = structure.getCostMedals(island.getType());
                     }

                     if (player.timedEventsUnlocked()) {
                        if (starpowerPurchase) {
                           if (StarSalesEvent.hasTimedEventNow(structure, player, island.getType())) {
                              costStarpower = StarSalesEvent.getTimedEventSaleCost(structure, player, island.getType(), player.lastClientVersion());
                           }
                        } else if (EntitySalesEvent.hasTimedEventNow(structure, player, island.getType())) {
                           costDiamonds = EntitySalesEvent.getTimedEventSaleCost(structure, player, Player.CurrencyType.Diamonds, island.getType());
                           if (etherealIsland) {
                              costEth = EntitySalesEvent.getTimedEventSaleCost(structure, player, Player.CurrencyType.Ethereal, island.getType());
                           } else {
                              costCoins = EntitySalesEvent.getTimedEventSaleCost(structure, player, Player.CurrencyType.Coins, island.getType());
                           }
                        }
                     }
                  }

                  if (!hasCredit && !player.canBuy((long)costCoins, (long)costEth, (long)costDiamonds, (long)costStarpower, 0L, 0L, costMedals)) {
                     this.ext.sendErrorResponse("gs_buy_structure", "You do not have enough currency to buy this structure", sender);
                  } else {
                     boolean applyHappyTreeEffects = false;
                     if (island.hasHappinessTree()) {
                        applyHappyTreeEffects = true;
                     }

                     if (island.hasMax(structure, player)) {
                        this.ext.sendErrorResponse("gs_buy_structure", "You already have one of those structures and cannot add a second one", sender);
                     } else {
                        if (structure.isHappyTree()) {
                           applyHappyTreeEffects = true;
                        } else if (structure.isHotel()) {
                           if (island.getHotel() != null) {
                              response.putBool("success", false);
                              response.putUtfString("error_msg", "MSG_HOTEL_BUY_FULL");
                              this.send("gs_buy_structure", response, sender);
                              return;
                           }
                        } else if (structure.isWarehouse()) {
                           if (island.getWarehouse() != null) {
                              response.putBool("success", false);
                              response.putUtfString("error_msg", "MSG_WAREHOUSE_BUY_FULL");
                              this.send("gs_buy_structure", response, sender);
                              return;
                           }
                        } else if (structure.isFuzer() && island.getFuzer() != null) {
                           response.putBool("success", false);
                           response.putUtfString("error_msg", "MSG_FUZER_BUY_FULL");
                           this.send("gs_buy_structure", response, sender);
                           return;
                        }

                        if (hasCredit) {
                           player.getInventory().removeItem(structure.getEntityId(), 1);
                           player.saveInventory();
                           response.putInt("inventory_used", 1);
                        } else {
                           player.chargePlayer(sender, this, costCoins, costEth, costDiamonds, (long)costStarpower, 0, 0, costMedals);
                           if (costDiamonds > 0) {
                              this.logDiamondUsage(sender, "buy_structure", costDiamonds, player.getLevel(), structure.getEntityId());
                           } else if (costStarpower > 0) {
                              this.logStarpowerUsage(sender, "buy_structure", costStarpower, player.getLevel());
                           }
                        }

                        long currentTimestamp = MSMExtension.CurrentDBTime();
                        SFSObject newStructure = new SFSObject();
                        newStructure.putLong("user_structure_id", island.getNextStructureId());
                        newStructure.putLong("island", island.getID());
                        newStructure.putLong("structure", (long)structure.getID());
                        newStructure.putInt("pos_x", pos_x);
                        newStructure.putInt("pos_y", pos_y);
                        newStructure.putDouble("scale", scale);
                        newStructure.putInt("flip", flip);
                        newStructure.putInt("is_complete", isComplete ? 1 : 0);
                        newStructure.putLong("date_created", currentTimestamp);
                        newStructure.putLong("building_completed", currentTimestamp + (long)structure.getBuildTimeMs());
                        newStructure.putLong("last_collection", MSMExtension.CurrentDBTime());
                        newStructure.putInt("book_value", etherealIsland ? costEth : costCoins);
                        if (colorR != null || colorY != null || colorB != null) {
                           newStructure.putFloat("colorR", colorR == null ? 0.0F : colorR);
                           newStructure.putFloat("colorY", colorY == null ? 0.0F : colorY);
                           newStructure.putFloat("colorB", colorB == null ? 0.0F : colorB);
                           newStructure.putInt("settings", 43690);
                        }

                        PlayerStructure playerStructure = new PlayerStructure(newStructure);
                        island.addStructure(playerStructure);
                        SFSArray responseVars;
                        if (isComplete) {
                           if (applyHappyTreeEffects) {
                              responseVars = this.addHappyTreeEffects(island, sender);
                              response.putSFSArray("monster_happy_effects", responseVars);
                           }

                           ISFSObject questEvent = new SFSObject();
                           questEvent.putInt("object", playerStructure.getEntityId());
                           questEvent.putUtfString("structure_type", playerStructure.getStructureType());
                           this.serverQuestEvent(sender, questEvent);
                        }

                        response.putBool("success", true);
                        response.putSFSObject("user_structure", playerStructure.toSFSObject(island));
                        if (questClaimValidated) {
                           response.putLong("quest_claim_id", questClaimId);
                        }

                        if (isComplete) {
                           int xpReward = StructureLookup.get(playerStructure.getType()).getXp();
                           if (island.isBattleIsland()) {
                              player.getBattleState().rewardXp(xpReward);
                              player.saveBattleState();
                           } else {
                              player.rewardXp(sender, this, xpReward);
                           }
                        }

                        if (mailIndex != null) {
                           player.getMailbox().removeElementAt(mailIndex);
                           String sql = "UPDATE user_mail SET deleted = 1 WHERE user_mail_id = ?";
                           this.ext.getDB().update(sql, new Object[]{params.getLong("mailId")});
                        }

                        responseVars = new SFSArray();
                        player.addPlayerPropertyData(responseVars, false);
                        response.putSFSArray("properties", responseVars);
                        this.send("gs_buy_structure", response, sender);
                        if (hasCredit) {
                           this.ext.stats.trackStructureBuy(sender, playerStructure.getType(), new MetricCost(0L, 0, 0L, 0L));
                        } else {
                           this.ext.stats.trackStructureBuy(sender, playerStructure.getType(), new MetricCost((long)costCoins, costDiamonds, (long)costEth, (long)costStarpower));
                        }

                     }
                  }
               }
            } else {
               this.ext.sendErrorResponse("gs_buy_structure", "This item is not available for purchase", sender);
            }
         } else {
            this.ext.sendErrorResponse("gs_buy_structure", "can't buy this structure on this island", sender);
         }
      } catch (Exception var36) {
         Logger.trace(var36, "**** error buying structure ****", "   params : " + params.getDump());
      }
   }

   private SFSArray addHappyTreeEffects(PlayerIsland island, User sender) {
      Iterator<PlayerMonster> monsters = island.getMonsters().iterator();
      int happinessIncrements = GameSettings.getInt("MONSTER_HAPPINESS_INCREMENTS");
      SFSArray responseMonsters = new SFSArray();

      while(monsters.hasNext()) {
         PlayerMonster curMonster = (PlayerMonster)monsters.next();
         if (!curMonster.isInactiveBoxMonster()) {
            int monsterHappiness = this.getMonsterHappinessWithEntireIsland(curMonster, island, happinessIncrements);
            boolean happChanged = monsterHappiness != curMonster.getHappiness();
            curMonster.setHappiness(monsterHappiness);
            if (sender != null) {
               if (monsterHappiness == 100) {
                  this.serverQuestEvent(sender, "happy", curMonster.getID());
               }

               if (monsterHappiness > 0) {
                  this.serverQuestEvent(sender, "any_happiness", 1);
               }
            }

            ISFSObject monster = new SFSObject();
            monster.putLong("user_monster_id", curMonster.getID());
            monster.putInt("happiness", monsterHappiness);
            if (happChanged) {
               monster.putLong("last_collection", curMonster.getLastCollectedTime());
               monster.putInt("collected_coins", curMonster.getCollectedCoins());
               monster.putInt("collected_ethereal", curMonster.getCollectedEth());
               monster.putDouble("collected_relics", (double)curMonster.getCollectedRelics());
            }

            responseMonsters.addSFSObject(monster);
         }
      }

      return responseMonsters;
   }

   int getMonsterHappinessWithEntireIsland(PlayerMonster curMonster, PlayerIsland island, int happinessIncrements) {
      int happinessWithEntireIsland = 0;
      if (curMonster.isInactiveBoxMonster()) {
         return happinessWithEntireIsland;
      } else {
         Monster monsterType = MonsterLookup.get(curMonster.getType());
         ISFSArray likes = monsterType.getLikes();
         Iterator<PlayerMonster> otherMonsters = null;
         Iterator<PlayerStructure> islandStructures = null;

         for(int i = 0; i < likes.size() && happinessWithEntireIsland < 100; ++i) {
            ISFSObject like = likes.getSFSObject(i);
            Integer happyMakingEntity = like.getInt("entity");
            if (happyMakingEntity != null) {
               int curLikedEntityId = happyMakingEntity;
               boolean entityFound = false;
               otherMonsters = island.getMonsters().iterator();

               while(otherMonsters.hasNext()) {
                  PlayerMonster potentialIslandFriend = (PlayerMonster)otherMonsters.next();
                  if (!potentialIslandFriend.isInactiveBoxMonster()) {
                     if (potentialIslandFriend.getEntityId() == curLikedEntityId) {
                        happinessWithEntireIsland += like.getInt("value") * happinessIncrements;
                        entityFound = true;
                        break;
                     }

                     if (potentialIslandFriend.getEntityId() != curMonster.getEntityId()) {
                        int potentialIslandFriendMonsterId = potentialIslandFriend.getType();
                        if (MonsterCommonToRareMapping.rareToCommonMapContainsKey(potentialIslandFriendMonsterId)) {
                           int commonMonsterId = MonsterCommonToRareMapping.rareToCommonMapGet(potentialIslandFriendMonsterId).commonMonsterId();
                           Monster commonMonster = MonsterLookup.get(commonMonsterId);
                           if (commonMonster.getEntityId() == curLikedEntityId) {
                              happinessWithEntireIsland += like.getInt("value") * happinessIncrements;
                              entityFound = true;
                              break;
                           }
                        }
                     }
                  }
               }

               if (!entityFound) {
                  islandStructures = island.getStructures().iterator();

                  while(islandStructures.hasNext()) {
                     PlayerStructure potentialFriendStructure = (PlayerStructure)islandStructures.next();
                     if (potentialFriendStructure.getEntityId() == curLikedEntityId) {
                        happinessWithEntireIsland += like.getInt("value") * happinessIncrements;
                        break;
                     }
                  }
               }
            }
         }

         return happinessWithEntireIsland;
      }
   }

   private void moveStructure(User sender, ISFSObject params) {
      try {
         long playerStructurerId = params.getLong("user_structure_id");
         int posX = params.getInt("pos_x");
         int posY = params.getInt("pos_y");
         double scale = 1.0D;
         if (params.containsKey("scale")) {
            scale = params.getDouble("scale");
         }

         Player player = (Player)sender.getProperty("player_object");
         PlayerStructure playerStructure = player.getActiveIsland().getStructureByID(playerStructurerId);
         if (StructureLookup.get(playerStructure.getType()).isCastle()) {
            this.ext.sendErrorResponse("gs_update_structure", "Can't move structure of type", sender);
            return;
         }

         if (StructureLookup.get(playerStructure.getType()).isObstacle()) {
            this.ext.sendErrorResponse("gs_update_structure", "Can't move structure of type", sender);
            return;
         }

         playerStructure.setPosition(posX, posY, scale);
         if (params.containsKey("settings")) {
            playerStructure.setSettings(params.getInt("settings"));
         }

         if (params.containsKey("colorR") && params.containsKey("colorY") && params.containsKey("colorB")) {
            playerStructure.setColor(params.getFloat("colorR"), params.getFloat("colorY"), params.getFloat("colorB"));
         }

         ISFSObject questEvent = new SFSObject();
         questEvent.putInt("move_object", playerStructure.getEntityId());
         this.serverQuestEvent(sender, questEvent);
         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         this.send("gs_move_structure", response, sender);
         response = new SFSObject();
         response.putLong("user_structure_id", playerStructure.getID());
         SFSArray responseVars = new SFSArray();
         ISFSObject property = new SFSObject();
         property.putInt("pos_x", playerStructure.getXPosition());
         responseVars.addSFSObject(property);
         property = new SFSObject();
         property.putInt("pos_y", playerStructure.getYPosition());
         responseVars.addSFSObject(property);
         response.putSFSArray("properties", responseVars);
         this.send("gs_update_structure", response, sender);
      } catch (Exception var15) {
         Logger.trace(var15, "**** error buying structure ****", "   params : " + params.getDump());
      }

   }

   private void sellStructure(User sender, ISFSObject params) {
      long playerStructureId = params.getLong("user_structure_id");
      SFSObject response = new SFSObject();

      try {
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         PlayerStructure playerStructure = island.getStructureByID(playerStructureId);
         if (playerStructure == null) {
            this.ext.sendErrorResponse("gs_sell_structure", "Could not find the structure you're trying to sell", sender);
            return;
         }

         Structure s = StructureLookup.get(playerStructure.getType());
         if (s.isCastle()) {
            response.putBool("success", false);
            response.putUtfString("error_msg", "MSG_NO_SELL_CASTLE");
            this.send("gs_sell_structure", response, sender);
            return;
         }

         if (s.isBakery()) {
            PlayerBaking pb = island.getBakingByStructureId(playerStructureId);
            if (pb != null) {
               island.removeBaking(pb);
            }
         } else if (s.isNursery()) {
            if (island.numNurseries() == 1) {
               response.putBool("success", false);
               response.putUtfString("error_msg", "MSG_NO_SELL_LAST_NURSERY");
               this.send("gs_sell_structure", response, sender);
               return;
            }

            PlayerEgg e = island.getEggByStructureId(playerStructureId);
            if (e != null) {
               if (e.getTimeRemaining() > 0L) {
                  response.putBool("success", false);
                  response.putUtfString("error_msg", "MSG_NO_SELL_ACTIVE_NURSERY");
                  this.send("gs_sell_structure", response, sender);
                  return;
               }

               island.removeEgg(e.getID());
            }
         } else if (s.isBreeding()) {
            if (island.numBreedingStructures() == 1) {
               response.putBool("success", false);
               response.putUtfString("error_msg", "MSG_NO_SELL_LAST_BREEDING_STRUCTURE");
               this.send("gs_sell_structure", response, sender);
               return;
            }

            PlayerBreeding b = island.getBreedingByStructureId(playerStructureId);
            if (b != null) {
               if (b.getTimeRemaining() > 0L) {
                  response.putBool("success", false);
                  response.putUtfString("error_msg", "MSG_NO_SELL_ACTIVE_BREEDING_STRUCTURE");
                  this.send("gs_sell_structure", response, sender);
                  return;
               }

               island.removeBreeding(b.getID());
            }
         } else if (s.isTorch()) {
            island.removeLitTorch(playerStructure);
            String sql = "DELETE FROM user_torch_gifts WHERE recipient_bbbid=? AND island_id=? AND user_structure=?";
            Object[] args = new Object[]{player.getBbbId(), island.getID(), playerStructureId};
            this.ext.getDB().update(sql, args);
         } else if (s.isHotel()) {
            if (island.getNumMonstersInStorage() > 0) {
               response.putBool("success", false);
               response.putUtfString("error_msg", "MSG_HOTEL_SELL_FULL");
               this.send("gs_sell_structure", response, sender);
               return;
            }
         } else if (s.isWarehouse()) {
            if (island.getNumDecorationsInStorage() > 0) {
               response.putBool("success", false);
               response.putUtfString("error_msg", "MSG_WAREHOUSE_SELL_FULL");
               this.send("gs_sell_structure", response, sender);
               return;
            }
         } else if (s.isFuzer()) {
            if (island.getNumBuddiesInFuzer() > 0) {
               response.putBool("success", false);
               response.putUtfString("error_msg", "MSG_FUZER_SELL_FULL");
               this.send("gs_sell_structure", response, sender);
               return;
            }
         } else {
            if (s.isCrucible()) {
               response.putBool("success", false);
               response.putUtfString("error_msg", "MSG_NO_SELL_CRUCIBLE");
               this.send("gs_sell_structure", response, sender);
               return;
            }

            if (s.isSynthesizer()) {
               response.putBool("success", false);
               response.putUtfString("error_msg", "MSG_NO_SELL_SYNTH");
               this.send("gs_sell_structure", response, sender);
               return;
            }

            if (s.isAttuner()) {
               response.putBool("success", false);
               response.putUtfString("error_msg", "MSG_NO_SELL_ATTUNER");
               this.send("gs_sell_structure", response, sender);
               return;
            }
         }

         island.removeStructure(playerStructure);
         Structure staticStructure = StructureLookup.get(playerStructure.getType());
         int structCost = false;
         int structCost;
         if (playerStructure.bookValue() != -1) {
            structCost = playerStructure.bookValue();
         } else {
            structCost = staticStructure.getSecondaryCurrencyCost(player, island, true, false);
         }

         structCost = (int)((double)structCost * GameSettings.getDouble("USER_SELLING_PERCENTAGE"));
         player.paySecondaryCurrencySellingPrice(sender, structCost, island, this);
         boolean inventoryChanged = false;
         if (staticStructure.getExtra().containsKey("trophy") && !player.getInventory().hasItem(staticStructure.getEntityId())) {
            player.getInventory().addItem(staticStructure.getEntityId(), 1);
            inventoryChanged = true;
         }

         response.putBool("success", true);
         SFSArray responseVars;
         if (island.hasHappinessTree()) {
            responseVars = this.addHappyTreeEffects(island, sender);
            response.putSFSArray("monster_happy_effects", responseVars);
         }

         response.putLong("user_structure_id", playerStructureId);
         responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         if (inventoryChanged) {
            responseVars.addSFSObject(player.getInventory().toSFSObject());
         }

         response.putSFSArray("properties", responseVars);
         this.send("gs_sell_structure", response, sender);
         int coinSellingPrice = 0;
         int ethSellingPrice = 0;
         if (island.isEtherealIsland()) {
            ethSellingPrice = staticStructure.ethSellingPrice(player, island.getType());
         } else {
            coinSellingPrice = staticStructure.coinSellingPrice(player, island.getType());
         }

         this.ext.stats.trackStructureSell(sender, staticStructure.getID(), new MetricCost((long)coinSellingPrice, 0, (long)ethSellingPrice, 0L));
      } catch (Exception var16) {
         response.putBool("success", false);
         response.putLong("user_structure_id", playerStructureId);
         this.send("gs_sell_structure", response, sender);
         Logger.trace(var16, "error deleting monster", "   params : " + params.getDump());
      }

   }

   private void adminPutDecorationInStorage(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminPutDecorationInStorage: Error! Trying to invoke admin without privileges!");
      } else {
         Player player = (Player)sender.getProperty("friend_object");
         Long playerIslandId = params.getLong("user_island_id");
         PlayerIsland island = player.getIslandByID(playerIslandId);
         this.putDecorationInStorage(sender, player, island, params, true);
      }
   }

   private void putDecorationInStorage(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      PlayerIsland island = player.getActiveIsland();
      this.putDecorationInStorage(sender, player, island, params, false);
   }

   private void putDecorationInStorage(User sender, Player player, PlayerIsland island, ISFSObject params, boolean adminStore) {
      boolean success = true;
      ISFSObject response = new SFSObject();
      long playerStructureId = params.getLong("user_structure_id");

      try {
         PlayerStructure playerStructure = island.getStructureByID(playerStructureId);
         if (playerStructure == null) {
            throw new Exception("Could not find structure.");
         }

         String structureType = playerStructure.getStructureType();
         if (!structureType.equalsIgnoreCase("decoration")) {
            response.putUtfString("error_msg", "MSG_WAREHOUSE_DECO");
            throw new Exception("Only decorations may be stored in the warehouse");
         }

         PlayerStructure warehouse = island.getWarehouse();
         if (warehouse == null) {
            response.putUtfString("error_msg", "MSG_WAREHOUSE_NONE");
            throw new Exception("A warehouse is required to store decorations.");
         }

         Structure warehouseStructure = StructureLookup.get(warehouse.getType());
         int capacity = warehouseStructure.getExtra().getInt("capacity");
         int numDecorationsInStorage = island.getNumDecorationsInStorage();
         if (numDecorationsInStorage == capacity) {
            success = false;
            response.putUtfString("error_msg", "MSG_WAREHOUSE_FULL");
            throw new Exception("The warehouse is full.");
         }

         if (playerStructure.inWarehouse()) {
            success = false;
         } else {
            playerStructure.setInWarehouse(true);
            if (adminStore) {
               this.ext.savePlayerIsland(player, island, false);
            }
         }
      } catch (Exception var16) {
         success = false;
      }

      response.putBool("success", success);
      response.putLong("user_structure_id", playerStructureId);
      this.send("gs_store_decoration", response, sender);
   }

   private void adminPutBuddyInFuzer(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminPutBuddyInFuzer: Error! Trying to invoke admin without privileges!");
      } else {
         Player player = (Player)sender.getProperty("friend_object");
         Long playerIslandId = params.getLong("user_island_id");
         PlayerIsland island = player.getIslandByID(playerIslandId);
         this.putBuddyInFuzer(sender, player, island, params, true);
      }
   }

   private void putBuddyInFuzer(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      PlayerIsland island = player.getActiveIsland();
      this.putBuddyInFuzer(sender, player, island, params, false);
   }

   private void putBuddyInFuzer(User sender, Player player, PlayerIsland island, ISFSObject params, boolean adminStore) {
      boolean success = true;
      ISFSObject response = new SFSObject();
      long playerStructureId = params.getLong("user_structure_id");

      try {
         PlayerStructure playerStructure = island.getStructureByID(playerStructureId);
         if (playerStructure == null) {
            throw new Exception("Could not find structure.");
         }

         if (!playerStructure.isBuddy()) {
            response.putUtfString("error_msg", "MSG_FUZER_BUDDY");
            throw new Exception("Only buddies may be stored in the fuzer");
         }

         PlayerStructure fuzer = island.getFuzer();
         if (fuzer == null) {
            response.putUtfString("error_msg", "MSG_FUZER_NONE");
            throw new Exception("A fuzer is required to fuze buddies.");
         }

         if (island.getNumBuddiesInFuzer() == 2) {
            success = false;
            response.putUtfString("error_msg", "MSG_FUZER_FULL");
            throw new Exception("The fuzer is full.");
         }

         playerStructure.setInFuzer(true);
         if (adminStore) {
            this.ext.savePlayerIsland(player, island, false);
         }
      } catch (Exception var12) {
         success = false;
      }

      response.putBool("success", success);
      response.putLong("user_structure_id", playerStructureId);
      this.send("gs_store_buddy", response, sender);
   }

   private void adminRemoveDecorationFromStorage(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminRemoveDecorationFromStorage: Error! Trying to invoke admin without privileges!");
      } else {
         Player player = (Player)sender.getProperty("friend_object");
         Long playerIslandId = params.getLong("user_island_id");
         PlayerIsland island = player.getIslandByID(playerIslandId);
         this.removeDecorationFromStorage(sender, player, island, params, true);
      }
   }

   private void removeDecorationFromStorage(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      PlayerIsland island = player.getActiveIsland();
      this.removeDecorationFromStorage(sender, player, island, params, false);
   }

   private void removeDecorationFromStorage(User sender, Player player, PlayerIsland island, ISFSObject params, boolean adminStore) {
      boolean success = true;
      ISFSObject response = new SFSObject();
      long playerStructureId = params.getLong("user_structure_id");

      try {
         PlayerStructure playerStructure = island.getStructureByID(playerStructureId);
         if (playerStructure == null) {
            throw new Exception("Could not find structure.");
         }

         PlayerStructure warehouse = island.getWarehouse();
         if (warehouse == null) {
            throw new Exception("A warehouse is required to store decorations.");
         }

         playerStructure.setInWarehouse(false);
         if (adminStore) {
            this.ext.savePlayerIsland(player, island, false);
         }
      } catch (Exception var12) {
         success = false;
      }

      response.putBool("success", success);
      response.putLong("user_structure_id", playerStructureId);
      this.send("gs_unstore_decoration", response, sender);
   }

   private void adminRemoveBuddyFromFuzer(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminRemoveBuddyFromFuzer: Error! Trying to invoke admin without privileges!");
      } else {
         Player player = (Player)sender.getProperty("friend_object");
         Long playerIslandId = params.getLong("user_island_id");
         PlayerIsland island = player.getIslandByID(playerIslandId);
         this.removeBuddyFromFuzer(sender, player, island, params, true);
      }
   }

   private void removeBuddyFromFuzer(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      PlayerIsland island = player.getActiveIsland();
      this.removeBuddyFromFuzer(sender, player, island, params, false);
   }

   private void removeBuddyFromFuzer(User sender, Player player, PlayerIsland island, ISFSObject params, boolean adminStore) {
      boolean success = true;
      ISFSObject response = new SFSObject();
      long playerStructureId = params.getLong("user_structure_id");

      try {
         PlayerStructure playerStructure = island.getStructureByID(playerStructureId);
         if (playerStructure == null) {
            throw new Exception("Could not find structure.");
         }

         PlayerStructure fuzer = island.getFuzer();
         if (fuzer == null) {
            throw new Exception("A fuzer is required to fuse buddies.");
         }

         playerStructure.setInFuzer(false);
         if (adminStore) {
            this.ext.savePlayerIsland(player, island, false);
         }
      } catch (Exception var12) {
         success = false;
      }

      response.putBool("success", success);
      response.putLong("user_structure_id", playerStructureId);
      this.send("gs_unstore_buddy", response, sender);
   }

   private void adminPutMonsterInStorage(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminPutMonsterInStorage: Error! Trying to invoke admin without privileges!");
      } else {
         Player player = (Player)sender.getProperty("friend_object");
         Long playerIslandId = params.getLong("user_island_id");
         PlayerIsland island = player.getIslandByID(playerIslandId);
         this.putMonsterInStorage(sender, player, island, params, true);
      }
   }

   private void putMonsterInStorage(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      PlayerIsland island = player.getActiveIsland();
      this.putMonsterInStorage(sender, player, island, params, false);
   }

   private void putMonsterInStorage(User sender, Player player, PlayerIsland island, ISFSObject params, boolean adminStore) {
      boolean success = true;
      long playerMonsterId = params.getLong("user_monster_id");
      SFSObject response = new SFSObject();

      try {
         PlayerMonster playerMonster = island.getMonsterByID(playerMonsterId);
         if (playerMonster == null) {
            throw new Exception("Couldn't find monster.");
         }

         if (island.isAmberIsland() && playerMonster.isInactiveBoxMonster()) {
            throw new Exception("Inactive amber island monsters, aren't allowed in the hotel.");
         }

         if (playerMonster.inHotel()) {
            success = false;
         } else {
            PlayerStructure hotel = island.getHotel();
            if (hotel == null) {
               response.putUtfString("error_msg", "MSG_HOTEL_NONE");
               throw new Exception("A hotel is required to store monsters.");
            }

            Structure hotelStructure = StructureLookup.get(hotel.getType());
            int capacity = hotelStructure.getExtra().getInt("capacity");
            if (island.isEtherealIslandWithModifiers()) {
               capacity = (int)((float)capacity * GameSettings.getFloat("ETHEREAL_CAPACITY_MULTIPLIER"));
            }

            int numMonsterBedsInStorage = island.getNumMonsterBedsInStorage();
            if (numMonsterBedsInStorage + MonsterLookup.get(playerMonster.getType()).beds() > capacity) {
               response.putUtfString("error_msg", "MSG_HOTEL_FULL");
               throw new Exception("The hotel is full.");
            }

            if (!adminStore) {
               this.collectFromMonster(sender, params);
            }

            playerMonster.setInHotel(true);
            if (island.isALastBredMonster(playerMonsterId)) {
               island.setLastBred(0L, 0L);
            }

            if (adminStore) {
               this.ext.savePlayerIsland(player, island, false);
            }
         }
      } catch (Exception var15) {
         success = false;
      }

      response.putBool("success", success);
      response.putLong("user_monster_id", playerMonsterId);
      this.send("gs_store_monster", response, sender);
   }

   private void adminRemoveMonsterFromStorage(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminRemoveMonsterFromStorage: Error! Trying to invoke admin without privileges!");
      } else {
         Player player = (Player)sender.getProperty("friend_object");
         Long playerIslandId = params.getLong("user_island_id");
         PlayerIsland island = player.getIslandByID(playerIslandId);
         this.removeMonsterFromStorage(sender, player, island, params, true);
      }
   }

   private void removeMonsterFromStorage(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      PlayerIsland island = player.getActiveIsland();
      this.removeMonsterFromStorage(sender, player, island, params, false);
   }

   private void megaMonsterMessage(User sender, ISFSObject params) {
      boolean adminResponse = false;
      if (params.containsKey("admin")) {
         adminResponse = params.getBool("admin");
      }

      if (adminResponse && sender.getPrivilegeId() != 3) {
         Logger.trace("megaMonsterMessage: Error! Trying to invoke admin without privileges!");
      } else {
         Player player;
         PlayerIsland island;
         if (adminResponse) {
            player = (Player)sender.getProperty("friend_object");
            Long playerIslandId = params.getLong("user_island_id");
            island = player.getIslandByID(playerIslandId);
         } else {
            player = (Player)sender.getProperty("player_object");
            island = player.getActiveIsland();
         }

         long playerMonsterId = params.getLong("user_monster_id");
         ISFSObject response = new SFSObject();
         boolean success = false;
         boolean permanent = false;
         boolean megaToggleMsg = params.containsKey("mega_enable");
         if (island.isTribalIsland()) {
            response.putBool("success", false);
            response.putLong("user_monster_id", playerMonsterId);
            response.putUtfString("error", "Cannot megafy tribal island monsters");
            this.send("gs_mega_monster_message", response, sender);
         }

         PlayerMonster playerMonster = island.getMonsterByID(playerMonsterId);
         if (megaToggleMsg) {
            boolean enable = params.getBool("mega_enable");
            success = playerMonster.megaEnable(enable);
            if (success) {
               PlayerMonster goldMonster = playerMonster.getGoldMonster(player);
               if (goldMonster != null) {
                  if (!goldMonster.isMega(this.ext)) {
                     goldMonster.makeMega(playerMonster.getMegaSFS());
                  }

                  goldMonster.megaEnable(enable);
               }
            }
         } else {
            permanent = params.getBool("permanent");
            if (playerMonster.isPermaMega()) {
               success = false;
               Logger.trace("error megafying monster: Monster is already permamegafied");
               response.putUtfString("error", "MEGAMONSTER_ALREADY_PERMAMEGA");
            } else {
               String diamondCostString = "";
               if (permanent) {
                  success = true;
                  diamondCostString = "USER_DIAMOND_COST_PER_PERMALIT_MEGAMONSTER";
               } else if (playerMonster.isDailyMega(MSMExtension.getInstance())) {
                  success = false;
                  response.putUtfString("error", "MEGAMONSTER_ALREADY_DAILYMEGA");
               } else {
                  success = true;
                  diamondCostString = "USER_DIAMOND_COST_PER_DAILY_MEGAMONSTER";
               }

               if (success) {
                  int diamondCost = 0;
                  if (permanent && MegafySaleEvent.hasTimedEventNow(player, island.getType())) {
                     try {
                        if (!adminResponse) {
                           diamondCost = MegafySaleEvent.getSaleCostDiamonds(player, island.getType());
                        }
                     } catch (Exception var20) {
                        Logger.trace("megaMonsterMessage: Error! Sale not found!");
                        return;
                     }
                  } else {
                     diamondCost = adminResponse ? 0 : GameSettings.getInt(diamondCostString);
                  }

                  if (player.canBuy(0L, 0L, (long)diamondCost, 0L, 0L, 0L, 0)) {
                     player.chargePlayer(sender, this, 0, 0, diamondCost, 0L, 0, 0, 0);
                     this.logDiamondUsage(sender, "megafy_monster", diamondCost, player.getLevel(), playerMonster.getEntityId());
                     success = true;
                  } else {
                     response.putUtfString("error", "NOTIFICATION_NOT_ENOUGH_DIAMONDS");
                     Logger.trace("error megafying monster: not enough diamonds");
                     success = false;
                  }
               }
            }
         }

         if (success) {
            PlayerMonster goldMonster = playerMonster.getGoldMonster(player);
            if (!megaToggleMsg) {
               playerMonster.makeMega(this.ext, permanent);
               if (goldMonster != null) {
                  goldMonster.makeMega(this.ext, permanent);
               }
            }

            ISFSObject goldmonsresponse = new SFSObject();
            goldmonsresponse.putLong("user_monster_id", playerMonster.getID());
            ISFSObject megaData = playerMonster.getMegaSFS();
            if (megaData != null) {
               goldmonsresponse.putSFSObject("megamonster", megaData);
            }

            if (adminResponse) {
               try {
                  this.ext.savePlayerIsland(player, island, false);
               } catch (Exception var19) {
                  Logger.trace(var19);
                  success = false;
               }

               goldmonsresponse.putBool("success", success);
               this.send("gs_admin_update_users_monster", goldmonsresponse, sender);
            } else {
               ISFSArray responseVars = new SFSArray();
               player.addPlayerPropertyData(responseVars, false);
               goldmonsresponse.putSFSArray("properties", responseVars);
               this.send("gs_update_monster", goldmonsresponse, sender);
            }

            if (goldMonster != null) {
               goldmonsresponse = new SFSObject();
               goldmonsresponse.putLong("user_monster_id", goldMonster.getID());
               megaData = goldMonster.getMegaSFS();
               if (megaData != null) {
                  goldmonsresponse.putSFSObject("megamonster", megaData);
               }

               PlayerIsland goldIsland;
               if (!adminResponse) {
                  try {
                     goldIsland = player.getIslandByIslandIndex(6);
                     this.ext.savePlayerIsland(player, goldIsland, false);
                  } catch (Exception var18) {
                     Logger.trace(var18);
                     success = false;
                  }

                  this.send("gs_update_monster", goldmonsresponse, sender);
               } else {
                  try {
                     goldIsland = player.getIslandByIslandIndex(6);
                     this.ext.savePlayerIsland(player, goldIsland, false);
                  } catch (Exception var17) {
                     Logger.trace(var17);
                     success = false;
                  }

                  goldmonsresponse.putBool("success", success);
                  this.send("gs_admin_update_users_monster", goldmonsresponse, sender);
               }
            }
         }

         response.putBool("success", success);
         response.putLong("user_monster_id", playerMonsterId);
         this.send("gs_mega_monster_message", response, sender);
      }
   }

   private void removeMonsterFromStorage(User sender, Player player, PlayerIsland island, ISFSObject params, boolean adminResponse) {
      boolean success = true;
      long playerMonsterId = params.getLong("user_monster_id");
      SFSObject response = new SFSObject();

      try {
         PlayerMonster playerMonster = island.getMonsterByID(playerMonsterId);
         if (playerMonster == null) {
            throw new Exception("Couldn't find monster.");
         }

         PlayerStructure hotel = island.getHotel();
         if (hotel == null) {
            throw new Exception("A hotel is required to remove monsters.");
         }

         int bedsAvailable = island.bedsAvailable();
         int bedsNeeded;
         if (bedsAvailable != -1) {
            bedsNeeded = MonsterLookup.get(playerMonster.getType()).beds();
            if (bedsAvailable < bedsNeeded) {
               response.putUtfString("error_msg", "NOTIFICATION_NOT_ENOUGH_BEDS");
               throw new Exception("Not enough beds in castle: bedsAvailable: " + bedsAvailable + ", bedsNeeded: " + bedsNeeded);
            }
         }

         bedsAvailable = params.getInt("pos_x");
         bedsNeeded = params.getInt("pos_y");
         playerMonster.setPosition(bedsAvailable, bedsNeeded);
         playerMonster.resetLastCollection();
         playerMonster.setInHotel(false);
         ISFSObject monsresponse = new SFSObject();
         monsresponse.putLong("user_monster_id", playerMonster.getID());
         monsresponse.putLong("last_collection", playerMonster.getLastCollectedTime());
         monsresponse.putInt("collected_coins", playerMonster.getCollectedCoins());
         if (adminResponse) {
            this.ext.savePlayerIsland(player, island, false);
            this.send("gs_admin_update_users_monster", monsresponse, sender);
         } else {
            this.send("gs_update_monster", monsresponse, sender);
         }
      } catch (Exception var15) {
         success = false;
      }

      response.putBool("success", success);
      response.putLong("user_monster_id", playerMonsterId);
      this.send("gs_unstore_monster", response, sender);
   }

   private void getTorchGifts(User sender, ISFSObject params) {
      Long userId = params.getLong("user_id");
      Long islandId = params.getLong("island_id");
      Player senderPlayer = (Player)sender.getProperty("player_object");
      ISFSObject response = new SFSObject();
      boolean success = false;

      try {
         senderPlayer.clearOutInvalidTorchGifts(this.ext);
         String sql = "DELETE FROM user_torch_gifts WHERE recipient_bbbid=? AND collected=1 AND TIMESTAMPADD(HOUR, ?, date) < NOW()";
         this.ext.getDB().update(sql, new Object[]{senderPlayer.getBbbId(), GameSettings.get("MAX_TORCH_GIFT_GIVING_FREQUENCY_HOURS")});
         ISFSArray torchGifts;
         if (senderPlayer.getBbbId() == userId) {
            torchGifts = senderPlayer.getCurIslandTorchGifts(this.ext.getDB());
            if (torchGifts != null) {
               response.putSFSArray("torch_gifts", torchGifts);
               success = true;
            }
         } else {
            torchGifts = Player.getCurIslandTorchGifts(userId, islandId, this.ext.getDB());
            if (torchGifts != null) {
               response.putBool("success", true);
               Iterator i = torchGifts.iterator();

               while(i.hasNext()) {
                  ISFSObject torchData = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
                  Long structureId = torchData.getLong("structure_id");
                  if (structureId != null) {
                     response.putLong("user_id", userId);
                     response.putLong("island_id", islandId);
                     response.putLong("user_structure", structureId);
                     this.send("gs_light_torch", response, sender);
                  }
               }
            }
         }
      } catch (Exception var14) {
         response.putUtfString("error_msg", "Error getting torch gifts");
         Logger.trace(var14, "Error getting torch gifts");
      }

      try {
         ISFSArray torchTimes = senderPlayer.canLightFriendTorchTimes();
         if (torchTimes != null) {
            SFSObject prop = new SFSObject();
            prop.putSFSArray("can_gift_torch_times", torchTimes);
            ISFSArray responseVars = new SFSArray();
            responseVars.addSFSObject(prop);
            response.putSFSArray("properties", responseVars);
         }
      } catch (Exception var13) {
         Logger.trace(var13, "Error getting canLightFriendTorchTimes");
      }

      response.putBool("success", success);
      this.send("gs_get_torchgifts", response, sender);
   }

   private void adminUnlightTorch(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminUnlightTorch: Error! Trying to invoke admin without privileges!");
      } else {
         Long userBbbId = params.getLong("user_id");
         Long islandId = params.getLong("island_id");
         Long structureId = params.getLong("user_structure_id");
         if (userBbbId != null && structureId != null && islandId != null) {
            Player adminFriend = (Player)sender.getProperty("friend_object");
            if (adminFriend != null && userBbbId == adminFriend.getBbbId()) {
               try {
                  ISFSObject response = new SFSObject();
                  boolean success = false;
                  Logger.trace("islandId: " + islandId);
                  PlayerIsland pi = adminFriend.getIslandByID(islandId);
                  Logger.trace("structureId: " + structureId);
                  PlayerStructure torchStructure = pi.getStructureByID(structureId);
                  if (torchStructure == null) {
                     Logger.trace("lightTorch: torch structure not found\n");
                     response.putUtfString("error_msg", "unlightTorch: torch structure not found: " + structureId);
                     response.putBool("success", success);
                     this.send("gs_unlight_torch", response, sender);
                     return;
                  }

                  LitPlayerTorch t = pi.getLitTorchData(torchStructure);
                  if (t != null) {
                     pi.removeLitTorch(torchStructure);
                     this.ext.savePlayerIsland(adminFriend, pi, false);
                  }

                  Player senderPlayer = (Player)sender.getProperty("player_object");
                  String sql = "DELETE FROM user_torch_gifts WHERE recipient_bbbid=? AND giver_bbbid=? AND island_id=? AND user_structure=? and collected=0";
                  Object[] args = new Object[]{adminFriend.getBbbId(), senderPlayer.getBbbId(), islandId, structureId};
                  this.ext.getDB().update(sql, args);
                  success = true;
                  response.putLong("user_id", adminFriend.getBbbId());
                  response.putLong("island_id", islandId);
                  response.putLong("user_structure", structureId);
                  response.putBool("success", success);
                  this.send("gs_admin_unlight_torch", response, sender);
               } catch (Exception var15) {
                  Logger.trace(var15, "error gs_admin_unlight_torch", "   params : " + params.getDump());
               }
            }

         } else {
            ISFSObject response = new SFSObject();
            Logger.trace("unlightTorch: required data not defined\n");
            response.putUtfString("error_msg", "unlightTorch: required data not defined");
            response.putBool("success", false);
            this.send("gs_admin_unlight_torch", response, sender);
         }
      }
   }

   private void getFriendTorchInfo(User sender, ISFSObject params) {
      Long userBbbId = params.getLong("user_id");
      Long islandId = params.getLong("island_id");
      Long structureId = params.getLong("user_structure_id");
      if (userBbbId != null && structureId != null && islandId != null) {
         String sql = "SELECT * FROM user_torch_gifts WHERE recipient_bbbid=? AND island_id=? AND user_structure=? AND collected=0";
         Object[] args = new Object[]{userBbbId, islandId, structureId};

         try {
            ISFSArray currentTorchGifts = this.ext.getDB().query(sql, args);
            ISFSObject response = new SFSObject();
            response.putLong("user_id", userBbbId);
            response.putLong("island_id", islandId);
            response.putLong("user_structure", structureId);
            boolean success = false;
            if (currentTorchGifts.size() != 0) {
               ISFSObject currentTorchGift = currentTorchGifts.getSFSObject(0);
               response.putSFSObject("user_torch", currentTorchGift);
               success = true;
            } else {
               sql = "SELECT volatile FROM user_island_data WHERE island=?";
               Object[] query_params = new Object[]{islandId};
               ISFSArray volatileResult = this.ext.getDB().query(sql, query_params);
               if (volatileResult != null) {
                  String volatileDataStr = Helpers.decompressJsonDataField(volatileResult.getSFSObject(0).getUtfString("volatile"), "{}");
                  ISFSObject volatileData = SFSObject.newFromJsonData(volatileDataStr);
                  ISFSArray torchData = volatileData.getSFSArray("torches");
                  long curTime = MSMExtension.CurrentDBTime();
                  Iterator i = torchData.iterator();

                  while(i.hasNext()) {
                     ISFSObject data = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
                     LitPlayerTorch litTorch = new LitPlayerTorch(data);
                     if (litTorch != null && litTorch.getStructureId() == structureId && !litTorch.torchExpired(curTime)) {
                        response.putSFSObject("user_torch", litTorch.getData());
                        success = true;
                        break;
                     }
                  }
               }
            }

            response.putBool("success", success);
            this.send("gs_additional_friend_torch_data", response, sender);
         } catch (Exception var21) {
            Logger.trace(var21, "error querying friend torches", "   params : " + params.getDump());
         }

      } else {
         ISFSObject response = new SFSObject();
         Logger.trace("getFriendTorchInfo: required data not defined\n");
         response.putUtfString("error_msg", "getFriendTorchInfo: required data not defined");
         response.putBool("success", false);
         this.send("gs_additional_friend_torch_data", response, sender);
      }
   }

   private void lightTorch(User sender, ISFSObject params, boolean collection) {
      Long userBbbId = params.getLong("user_id");
      Long islandId = params.getLong("island_id");
      Long structureId = params.getLong("user_structure_id");
      Boolean collectNow = params.getBool("collect_now");
      if (collectNow == null) {
         collectNow = true;
      }

      Player senderPlayer = (Player)sender.getProperty("player_object");
      if (userBbbId != null && userBbbId != 0L && structureId != null && islandId != null) {
         if (senderPlayer.getBbbId() == userBbbId) {
            boolean permanentlyLit = false;
            if (!collection) {
               permanentlyLit = params.getBool("permalit");
            }

            try {
               this.lightOwnTorch(sender, senderPlayer, islandId, structureId, collection, permanentlyLit, collectNow);
            } catch (Exception var11) {
               Logger.trace(var11, "error clearing torch gifts", "   params : " + params.getDump());
            }
         } else {
            try {
               Player adminFriend = (Player)sender.getProperty("friend_object");
               if (sender.getPrivilegeId() == 3 && adminFriend != null && userBbbId == adminFriend.getBbbId()) {
                  this.adminLightUserTorch(sender, senderPlayer, adminFriend, islandId, structureId, params.getBool("permalit"));
               } else {
                  this.lightFriendTorch(sender, senderPlayer, userBbbId, islandId, structureId);
               }
            } catch (Exception var12) {
               Logger.trace(var12, "error lighting friend torch", "   params : " + params.getDump());
            }
         }

      } else {
         ISFSObject response = new SFSObject();
         Logger.trace("lightTorch: required data not defined\n");
         response.putUtfString("error_msg", "lightTorch: required data not defined");
         response.putBool("success", false);
         this.send("gs_light_torch", response, sender);
      }
   }

   private void lightOwnTorch(User sender, Player senderPlayer, long islandId, long structureId, boolean collection, boolean permanentlyLit, boolean collectNow) throws Exception {
      ISFSObject response = new SFSObject();
      boolean success = false;
      PlayerIsland island = senderPlayer.getIslandByID(islandId);
      island.clearOutOldTorchData();
      PlayerStructure torchStructure = island.getStructureByID(structureId);
      if (torchStructure == null) {
         Logger.trace("lightTorch: torch structure not found\n");
         response.putUtfString("error_msg", "lightTorch: torch structure not found: " + structureId);
         response.putBool("success", success);
         this.send("gs_light_torch", response, sender);
      } else {
         Structure structType = StructureLookup.get(torchStructure.getType());
         if (!structType.isTorch()) {
            Logger.trace("lightTorch: structure is not a torch\n");
            if (collection) {
               String sql = "DELETE FROM user_torch_gifts WHERE recipient_bbbid=? AND island_id=? AND user_structure=?";
               Object[] args = new Object[]{senderPlayer.getBbbId(), islandId, structureId};
               this.ext.getDB().update(sql, args);
            }

            response.putUtfString("error_msg", "lightTorch: structure is not a torch");
            success = false;
         } else {
            Integer diamondCostToLight = 0;
            if (!collection) {
               LitPlayerTorch litTorch = island.getLitTorchData(torchStructure);
               if (permanentlyLit) {
                  if (litTorch != null && litTorch.isPermalit()) {
                     Logger.trace("lightTorch: trying to permalight a permanently lit torch\n");
                     response.putUtfString("error_msg", "lightTorch: torch is already permalit");
                     response.putBool("success", false);
                     this.send("gs_light_torch", response, sender);
                     return;
                  }

                  diamondCostToLight = GameSettings.getInt("USER_DIAMOND_COST_PER_PERMALIT_TORCH");
                  if (PermalightTorchSaleEvent.hasTimedEventNow(senderPlayer, island.getType())) {
                     diamondCostToLight = PermalightTorchSaleEvent.getSaleCostDiamonds(senderPlayer, diamondCostToLight, island.getType());
                  }
               } else {
                  if (litTorch != null && (litTorch.isPermalit() || !litTorch.torchExpired(MSMExtension.CurrentDBTime()))) {
                     Logger.trace("lightTorch: trying to temp light an already lit torch\n");
                     response.putUtfString("error_msg", "lightTorch: torch is already lit");
                     response.putBool("success", false);
                     this.send("gs_light_torch", response, sender);
                     return;
                  }

                  diamondCostToLight = GameSettings.getInt("USER_DIAMOND_COST_PER_LIT_TORCH");
               }
            } else {
               permanentlyLit = false;
            }

            if (senderPlayer.canBuy(0L, 0L, (long)diamondCostToLight, 0L, 0L, 0L, 0)) {
               if (diamondCostToLight > 0) {
                  this.logDiamondUsage(sender, "lit_torch", diamondCostToLight, senderPlayer.getLevel(), structType.getEntityId());
               }

               senderPlayer.chargePlayer(sender, this, 0, 0, diamondCostToLight, 0L, 0, 0, 0);
               ISFSArray responseVars = new SFSArray();
               senderPlayer.addPlayerPropertyData(responseVars, false);
               response.putSFSArray("properties", responseVars);
               if (!collection) {
                  LitPlayerTorch litTorch = island.lightTorch(torchStructure, permanentlyLit);
                  success = true;
                  response.putSFSObject("user_torch", litTorch.getData());
                  this.serverQuestEvent(sender, "light_torch", 1);
               } else {
                  String SELECT_TORCH_GIFTS_SQL = "SELECT * FROM user_torch_gifts WHERE recipient_bbbid=? AND island_id=? AND user_structure=? and collected=0";
                  Object[] args = new Object[]{senderPlayer.getBbbId(), islandId, structureId};
                  SFSArray result = this.ext.getDB().query("SELECT * FROM user_torch_gifts WHERE recipient_bbbid=? AND island_id=? AND user_structure=? and collected=0", args);
                  if (result.size() > 0) {
                     if (collectNow) {
                        LitPlayerTorch litTorch = island.lightTorch(torchStructure, permanentlyLit);
                        response.putSFSObject("user_torch", litTorch.getData());
                     }

                     success = true;
                     ISFSObject s = result.getSFSObject(0);
                     Long dateGifted = s.getLong("date");
                     Long maxGivingFreqMs = GameSettings.getLong("MAX_TORCH_GIFT_GIVING_FREQUENCY_HOURS") * 60L * 60L * 1000L;
                     if (collectNow) {
                        String DELETE_TORCH_GIFTS_SQL;
                        if (MSMExtension.CurrentDBTime() > dateGifted + maxGivingFreqMs) {
                           DELETE_TORCH_GIFTS_SQL = "DELETE FROM user_torch_gifts WHERE recipient_bbbid=? AND island_id=? AND user_structure=?";
                           args = new Object[]{senderPlayer.getBbbId(), islandId, structureId};
                           this.ext.getDB().update("DELETE FROM user_torch_gifts WHERE recipient_bbbid=? AND island_id=? AND user_structure=?", args);
                        } else {
                           DELETE_TORCH_GIFTS_SQL = "UPDATE user_torch_gifts SET collected=1 WHERE recipient_bbbid=? AND island_id=? AND user_structure=?";
                           args = new Object[]{senderPlayer.getBbbId(), islandId, structureId};
                           this.ext.getDB().update("UPDATE user_torch_gifts SET collected=1 WHERE recipient_bbbid=? AND island_id=? AND user_structure=?", args);
                        }
                     } else {
                        long giverBbbId = s.getLong("giver_bbbid");
                        String SELECT_USER_INFO_SQL = "SELECT display_name, pp_type, pp_info FROM users LEFT JOIN user_avatar ON users.user_id = user_avatar.user_id WHERE bbb_id=?";
                        args = new Object[]{giverBbbId};
                        result = this.ext.getDB().query("SELECT display_name, pp_type, pp_info FROM users LEFT JOIN user_avatar ON users.user_id = user_avatar.user_id WHERE bbb_id=?", args);
                        if (result != null && result.size() > 0) {
                           ISFSObject r = result.getSFSObject(0);
                           response.putLong("giver_bbbid", giverBbbId);
                           response.putUtfString("giver_name", Helpers.sanitizeName(r.getUtfString("display_name"), " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!¨\"#$&'()*+,-./:;<=>?@{}0123456789|£©¿®`~^ÀÁÂÄÇÈÉÊËÌÍÎÏÑÒÓÔÖÙÚÛÜßàáâäçèéêëìíîïñòóôöùúûü_ÆæÃãÕõАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя€₽¡"));
                           if (r.containsKey("pp_type") && r.containsKey("pp_info")) {
                              response.putInt("giver_pp_type", r.getInt("pp_type"));
                              response.putUtfString("giver_pp_info", r.getUtfString("pp_info"));
                           } else {
                              response.putInt("giver_pp_type", 0);
                              response.putUtfString("giver_pp_info", "0");
                           }
                        }
                     }
                  }
               }
            } else {
               response.putUtfString("error_msg", "cheater, can't afford this yo");
               success = false;
            }
         }

         response.putLong("user_id", senderPlayer.getBbbId());
         response.putLong("island_id", islandId);
         response.putLong("user_structure", structureId);
         response.putBool("success", success);
         this.send("gs_light_torch", response, sender);
      }
   }

   private void adminLightUserTorch(User sender, Player senderPlayer, Player receiver, long islandId, long structureId, boolean permalit) throws Exception {
      ISFSObject response = new SFSObject();
      boolean success = false;
      SFSArray responseVars;
      if (permalit) {
         PlayerIsland pi = receiver.getIslandByID(islandId);
         PlayerStructure torchStructure = pi.getStructureByID(structureId);
         if (torchStructure == null) {
            Logger.trace("lightTorch: torch structure not found\n");
            response.putUtfString("error_msg", "lightTorch: torch structure not found: " + structureId);
            response.putBool("success", success);
            this.send("gs_light_torch", response, sender);
            return;
         }

         LitPlayerTorch litTorch = pi.lightTorch(torchStructure, permalit);
         response.putSFSObject("user_torch", litTorch.getData());
         success = true;
         this.ext.savePlayerIsland(receiver, pi, false);
      } else {
         String sql = "SELECT * FROM user_torch_gifts WHERE recipient_bbbid=? AND island_id=? AND user_structure=? AND collected=0";
         Object[] args = new Object[]{receiver.getBbbId(), islandId, structureId};
         responseVars = this.ext.getDB().query(sql, args);
         if (responseVars.size() == 0) {
            sql = "INSERT INTO user_torch_gifts SET recipient_bbbid=?, giver_bbbid=?, island_id=?, user_structure=?, date=NOW()";
            args = new Object[]{receiver.getBbbId(), senderPlayer.getBbbId(), islandId, structureId};
            this.ext.getDB().insertGetId(sql, args);
            success = true;
            this.incrTorchesILitCount(senderPlayer.getBbbId(), receiver.getBbbId());
            SFSObject result = new SFSObject();
            result.putLong("island_id", islandId);
            result.putLong("user_structure", structureId);
            response.putSFSObject("user_torch", result);
            this.ext.stats.trackTorchGift(sender, structureId, islandId, receiver.getBbbId());
         } else {
            Logger.trace("lightTorch: torch already lit by another friend\n");
            response.putUtfString("error_msg", "lightTorch: torch already lit by another friend");
            success = true;
         }
      }

      response.putLong("user_id", receiver.getBbbId());
      response.putLong("island_id", islandId);
      response.putLong("user_structure", structureId);
      SFSObject prop = new SFSObject();
      ISFSArray torchTimes = senderPlayer.canLightFriendTorchTimes();
      if (torchTimes != null) {
         prop.putSFSArray("can_gift_torch_times", torchTimes);
      }

      responseVars = new SFSArray();
      responseVars.addSFSObject(prop);
      response.putSFSArray("properties", responseVars);
      response.putBool("success", success);
      this.send("gs_light_torch", response, sender);
   }

   private void lightFriendTorch(User sender, Player senderPlayer, long receiverBbbId, long islandId, long structureId) throws Exception {
      ISFSObject response = new SFSObject();
      boolean success = false;
      boolean incrTorchCount = false;
      String sql;
      Object[] args;
      SFSArray currentTorchGifts;
      if (senderPlayer.canLightFriendTorchNow(receiverBbbId)) {
         sql = "SELECT * FROM user_torch_gifts WHERE recipient_bbbid=? AND island_id=? AND user_structure=? AND collected=0";
         args = new Object[]{receiverBbbId, islandId, structureId};
         currentTorchGifts = this.ext.getDB().query(sql, args);
         if (currentTorchGifts.size() == 0) {
            sql = "INSERT INTO user_torch_gifts SET recipient_bbbid=?, giver_bbbid=?, island_id=?, user_structure=?, date=NOW()";
            args = new Object[]{receiverBbbId, senderPlayer.getBbbId(), islandId, structureId};
            this.ext.getDB().insertGetId(sql, args);
            success = true;
            incrTorchCount = true;
            this.incrTorchesILitCount(senderPlayer.getBbbId(), receiverBbbId);
            this.ext.stats.trackTorchGift(sender, structureId, islandId, receiverBbbId);
         } else {
            Logger.trace("lightTorch: torch already lit by another friend\n");
            response.putUtfString("error_msg", "lightTorch: torch already lit by another friend");
            incrTorchCount = true;
            this.incrTorchesILitCount(senderPlayer.getBbbId(), receiverBbbId);
            success = true;
         }
      } else {
         Logger.trace("lightTorch: can't light anymore friend torches right now\n");
         response.putUtfString("error_msg", "lightTorch: can't light anymore friend torches right now");
         success = false;
      }

      if (receiverBbbId == 0L) {
         Logger.trace("lightFriendTorch: receiverBbbId is 0");
         response.putBool("success", false);
         this.send("gs_light_torch", response, sender);
      }

      response.putLong("user_id", receiverBbbId);
      response.putLong("island_id", islandId);
      response.putLong("user_structure", structureId);
      response.putBool("incrTorchCount", incrTorchCount);
      sql = "SELECT * FROM user_torch_gifts WHERE recipient_bbbid=? AND island_id=? AND user_structure=? AND collected=0";
      args = new Object[]{receiverBbbId, islandId, structureId};
      currentTorchGifts = this.ext.getDB().query(sql, args);
      if (currentTorchGifts.size() != 0) {
         ISFSObject currentTorchGift = currentTorchGifts.getSFSObject(0);
         response.putSFSObject("new_friend_light_dat", currentTorchGift);
      }

      SFSObject prop = new SFSObject();
      ISFSArray torchTimes = senderPlayer.canLightFriendTorchTimes();
      if (torchTimes != null) {
         prop.putSFSArray("can_gift_torch_times", torchTimes);
      }

      ISFSArray responseVars = new SFSArray();
      responseVars.addSFSObject(prop);
      response.putSFSArray("properties", responseVars);
      response.putBool("success", success);
      this.send("gs_light_torch", response, sender);
   }

   private void lightTorchFlag(User sender, ISFSObject params) {
      long islandId = params.getLong("island_id");
      Boolean flag = params.getBool("light_torch_flag");
      Player player = (Player)sender.getProperty("player_object");
      PlayerIsland island = player.getIslandByID(islandId);
      island.setLightTorchFlag(flag);
      ISFSObject response = new SFSObject();
      response.putLong("island_id", islandId);
      response.putBool("light_torch_flag", flag);
      response.putBool("success", true);
      this.send("gs_set_light_torch_flag", response, sender);
   }

   private void startUpgradeStructure(User sender, ISFSObject params) {
      long playerStructureId = params.getLong("user_structure_id");

      try {
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         PlayerStructure playerStructure = island.getStructureByID(playerStructureId);
         if (playerStructure == null) {
            this.ext.sendErrorResponse("gs_start_upgrade_structure", "Could not find the structure you're trying to upgrade", sender);
            return;
         }

         if (playerStructure.isUpgrading()) {
            this.ext.sendErrorResponse("gs_start_upgrade_structure", "Structure is already upgrading", sender);
            return;
         }

         Structure staticStructure = StructureLookup.get(playerStructure.getType());
         Structure newStructure = StructureLookup.getNextUpgrade(staticStructure);
         if (newStructure == null) {
            this.ext.sendErrorResponse("gs_start_upgrade_structure", "No upgrades available", sender);
            return;
         }

         if (playerStructure.isCrucible()) {
            PlayerCrucibleData crucData = island.getCrucibleData(playerStructureId);
            if (!crucData.fullyUnlocked()) {
               this.ext.sendErrorResponse("gs_start_upgrade_structure", "Cannot upgrade locked crucible", sender);
               return;
            }
         }

         boolean inRelics = island.isAmberIsland() && staticStructure.isCastle();
         int costDiamonds = newStructure.getCostDiamonds(island.getType());
         int costEth = 0;
         int costCoins = 0;
         int costRelics = 0;
         if (!inRelics) {
            costEth = island.isEtherealIsland() ? newStructure.getCostEth(island.getType()) : 0;
            costCoins = newStructure.getCostCoins(island.getType());
         } else if (costDiamonds == 0) {
            costRelics = newStructure.getCostRelics(island.getType());
         }

         if (player.timedEventsUnlocked() && EntitySalesEvent.hasTimedEventNow(newStructure, player, island.getType())) {
            costDiamonds = EntitySalesEvent.getTimedEventSaleCost(newStructure, player, Player.CurrencyType.Diamonds, island.getType());
            costEth = island.isEtherealIsland() ? EntitySalesEvent.getTimedEventSaleCost(newStructure, player, Player.CurrencyType.Ethereal, island.getType()) : 0;
            costCoins = EntitySalesEvent.getTimedEventSaleCost(newStructure, player, Player.CurrencyType.Coins, island.getType());
            costRelics = EntitySalesEvent.getTimedEventSaleCost(newStructure, player, Player.CurrencyType.Relics, island.getType());
         }

         if (newStructure.isBakery()) {
            int sellingPrice = staticStructure.coinSellingPrice(player, island.getType());
            costCoins -= sellingPrice;
         } else if (newStructure.isMine()) {
            costDiamonds = (int)((float)costDiamonds * GameSettings.getFloat("USER_MINE_UPGRADE_COST_PERCENT"));
         }

         if (!player.canBuy((long)costCoins, (long)costEth, (long)costDiamonds, 0L, 0L, (long)costRelics, 0)) {
            this.ext.sendErrorResponse("gs_start_upgrade_structure", "You do not have enough coins/diamonds/relics to upgrade this structure", sender);
            return;
         }

         if (newStructure.isPremium() && !player.hasMadePurchase()) {
            this.ext.sendErrorResponse("gs_start_upgrade_structure", "You are not allowed to purchase this item", sender);
            return;
         }

         playerStructure.startUpgradeStructure(newStructure);
         player.chargePlayer(sender, this, costCoins, costEth, costDiamonds, 0L, 0, costRelics, 0);
         if (costDiamonds > 0) {
            this.logDiamondUsage(sender, "structrue_upgrade", costDiamonds, player.getLevel(), newStructure.getEntityId());
         }

         this.ext.stats.trackStructureUpgradeStart(sender, playerStructure.getStructureType(), staticStructure.getID(), newStructure.getID(), playerStructure.getID());
         ISFSObject response = new SFSObject();
         response.putLong("user_structure_id", playerStructure.getID());
         ISFSArray responseVars = new SFSArray();
         ISFSObject property = new SFSObject();
         property.putInt("is_complete", 0);
         responseVars.addSFSObject(property);
         property = new SFSObject();
         property.putInt("is_upgrading", 1);
         responseVars.addSFSObject(property);
         property = new SFSObject();
         property.putLong("date_created", playerStructure.getDateCreated());
         responseVars.addSFSObject(property);
         property = new SFSObject();
         property.putLong("building_completed", playerStructure.getBuildingCompletedTime());
         responseVars.addSFSObject(property);
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_update_structure", response, sender);
      } catch (Exception var18) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         response.putLong("user_structure_id", playerStructureId);
         this.send("gs_update_structure", response, sender);
         Logger.trace(var18, "error upgrading structure", "   params : " + params.getDump());
      }

   }

   private void finishUpgradeStructure(User sender, ISFSObject params) {
      long playerStructureId = params.getLong("user_structure_id");

      try {
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         PlayerStructure playerStructure = island.getStructureByID(playerStructureId);
         if (playerStructure == null) {
            this.ext.sendErrorResponse("gs_finish_upgrade_structure", "Could not find the structure you're trying to finish upgrading", sender);
            return;
         }

         if (!playerStructure.isUpgrading()) {
            this.ext.sendErrorResponse("gs_finish_upgrade_structure", "This structure is not currently upgrading", sender);
            return;
         }

         if (playerStructure.getTimeRemaining() > 0L) {
            this.ext.sendErrorResponse("gs_finish_upgrade_structure", "This structure has not yet completed its upgrade", sender);
            return;
         }

         Structure newStructure = playerStructure.finishUpgradeStructure();
         if (newStructure == null) {
            this.ext.sendErrorResponse("gs_finish_upgrade_structure", "No upgrades available", sender);
            return;
         }

         ISFSObject response = new SFSObject();
         if (newStructure.isCrucible()) {
            PlayerCrucibleData c = island.getCrucibleData(playerStructureId);
            if (c != null) {
               response.putSFSObject("user_crucible", c.getData());
            }
         }

         int xpReward = newStructure.getXp();
         if (island.isBattleIsland()) {
            player.getBattleState().rewardXp(xpReward);
            player.saveBattleState();
         } else {
            player.rewardXp(sender, this, xpReward);
         }

         response.putBool("success", true);
         response.putLong("user_structure_id", playerStructureId);
         response.putSFSObject("user_structure", playerStructure.toSFSObject(island));
         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_finish_upgrade_structure", response, sender);
         ISFSObject questEvent = new SFSObject();
         questEvent.putInt("object", playerStructure.getEntityId());
         questEvent.putLong(playerStructure.getStructureType() + "_level", playerStructure.getID());
         questEvent.putUtfString("structure_type", playerStructure.getStructureType());
         this.serverQuestEvent(sender, questEvent);
         this.ext.stats.trackStructureUpgradeComplete(sender, playerStructure.getStructureType(), newStructure.getID(), playerStructure.getID());
      } catch (Exception var13) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         response.putLong("user_structure_id", playerStructureId);
         this.send("gs_update_structure", response, sender);
         Logger.trace(var13, "error upgrading structure", "   params : " + params.getDump());
      }

   }

   private void finishStructure(User sender, ISFSObject params) {
      long playerStructureId = params.getLong("user_structure_id");

      try {
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         PlayerStructure playerStructure = island.getStructureByID(playerStructureId);
         if (playerStructure == null) {
            this.ext.sendErrorResponse("gs_finish_structure", "Could not find the structure you're trying to finish building", sender);
            return;
         }

         if (playerStructure.getTimeRemaining() > 0L) {
            this.ext.sendErrorResponse("gs_finish_structure", "Structure is not finished building yet", sender);
            return;
         }

         playerStructure.finishBuildingStructure();
         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         response.putLong("user_structure_id", playerStructure.getID());
         this.send("gs_finish_structure", response, sender);
         ISFSObject questEvent = new SFSObject();
         questEvent.putInt("object", playerStructure.getEntityId());
         questEvent.putUtfString("structure_type", playerStructure.getStructureType());
         this.serverQuestEvent(sender, questEvent);
         int xpReward = StructureLookup.get(playerStructure.getType()).getXp();
         player.rewardXp(sender, this, xpReward);
         response = new SFSObject();
         response.putLong("user_structure_id", playerStructure.getID());
         ISFSArray responseVars = new SFSArray();
         ISFSObject property = new SFSObject();
         property.putInt("is_complete", 1);
         responseVars.addSFSObject(property);
         property = new SFSObject();
         property.putInt("is_upgrading", 0);
         responseVars.addSFSObject(property);
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_update_structure", response, sender);
      } catch (Exception var13) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         response.putLong("user_structure_id", playerStructureId);
         this.send("gs_finish_structure", response, sender);
         Logger.trace(var13, "error finishing structure", "   params : " + params.getDump());
      }

   }

   private void flipStructure(User sender, ISFSObject params) {
      try {
         long playerStructureId = params.getLong("user_structure_id");
         boolean toggle = true;
         boolean flipped = false;
         if (params.containsKey("flipped")) {
            toggle = false;
            flipped = params.getBool("flipped");
         }

         Player player = (Player)sender.getProperty("player_object");
         PlayerStructure playerStructure = player.getActiveIsland().getStructureByID(playerStructureId);
         if (toggle) {
            playerStructure.toggleFlip();
         } else {
            playerStructure.setFlip(flipped);
         }

         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         this.send("gs_flip_structure", response, sender);
         response = new SFSObject();
         response.putLong("user_structure_id", playerStructure.getID());
         SFSArray responseVars = new SFSArray();
         ISFSObject property = new SFSObject();
         property.putInt("flip", playerStructure.getFlip());
         responseVars.addSFSObject(property);
         response.putSFSArray("properties", responseVars);
         this.send("gs_update_structure", response, sender);
      } catch (Exception var12) {
         Logger.trace(var12, "error during flip structure", "   params : " + params.getDump());
      }

   }

   private void structureSpeedUp(User sender, ISFSObject params) {
      long playerStructureId = params.getLong("user_structure_id");

      try {
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         PlayerStructure playerStructure = island.getStructureByID(playerStructureId);
         if (playerStructure == null) {
            this.ext.sendErrorResponse("gs_speed_up_structure", "Could not find the structure you're trying to speed up", sender);
            return;
         }

         if (null != params.getInt("speed_up_type") && 0 != params.getInt("speed_up_type")) {
            if (1 == params.getInt("speed_up_type")) {
               if (!player.hasSpeedUpCredit()) {
                  this.ext.sendErrorResponse("gs_speed_up_structure", "Unfortunately there are currently no offers available to you. Please try again later", sender);
                  return;
               }

               this.ext.stats.trackSpeedupWithVideo(sender, "speedup_structure", (long)playerStructure.getType(), playerStructure.getID(), playerStructure.getTimeRemaining(), playerStructure.getInitialTimeRemaining());
               playerStructure.reduceStructureTimeByVideo();
               player.adjustSpeedUpCredit(sender, this, -1, Player.SPEED_UP_TYPES.VIDEO);
            }
         } else {
            int diamondsRequired = Game.DiamondsRequiredToComplete(playerStructure.getTimeRemaining());
            if (!player.canBuy(0L, 0L, (long)diamondsRequired, 0L, 0L, 0L, 0)) {
               this.ext.sendErrorResponse("gs_speed_up_structure", "You do not have enough diamonds to speed up construction", sender);
               return;
            }

            if (playerStructure.getTimeRemaining() > 5L) {
               player.chargePlayer(sender, this, 0, 0, diamondsRequired, 0L, 0, 0, 0);
               JSONObject speedupInfo = MSMStats.extraSpeedupInfo(playerStructure.getTimeRemaining(), playerStructure.getInitialTimeRemaining(), playerStructure.getType());
               this.logDiamondUsage(sender, "speedup_structure", diamondsRequired, player.getLevel(), speedupInfo, playerStructure.getEntityId());
               playerStructure.finishBuildingNow();
            }
         }

         ISFSObject response = new SFSObject();
         response.putLong("user_structure_id", playerStructure.getID());
         SFSArray responseVars = new SFSArray();
         ISFSObject completed = new SFSObject();
         completed.putLong("building_completed", playerStructure.getBuildingCompletedTime());
         responseVars.addSFSObject(completed);
         ISFSObject created = new SFSObject();
         created.putLong("date_created", playerStructure.getDateCreated());
         responseVars.addSFSObject(created);
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_update_structure", response, sender);
         response = new SFSObject();
         response.putBool("success", true);
         this.send("gs_speed_up_structure", response, sender);
      } catch (Exception var12) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         response.putLong("user_structure_id", playerStructureId);
         this.send("gs_clear_obstacle_speed_up", response, sender);
         Logger.trace(var12, "error clearing obstacle", "   params : " + params.getDump());
      }

   }

   private void muteStructure(User sender, ISFSObject params) {
      try {
         long playerStructureId = params.getLong("user_structure_id");
         Player player = (Player)sender.getProperty("player_object");
         PlayerStructure playerStructure = player.getActiveIsland().getStructureByID(playerStructureId);
         playerStructure.toggleMute();
         SFSObject response = new SFSObject();
         response.putLong("user_structure_id", playerStructure.getID());
         SFSArray responseVars = new SFSArray();
         ISFSObject property = new SFSObject();
         property.putInt("muted", playerStructure.getMuted());
         responseVars.addSFSObject(property);
         response.putSFSArray("properties", responseVars);
         this.send("gs_update_structure", response, sender);
      } catch (Exception var10) {
         Logger.trace(var10, "error during mute structure", "   params : " + params.getDump());
      }

   }

   private void startObstacle(User sender, ISFSObject params) {
      long playerStructureId = params.getLong("user_structure_id");

      try {
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         PlayerStructure playerStructure = island.getStructureByID(playerStructureId);
         if (playerStructure == null) {
            Logger.trace("could not find the structure");
            this.ext.sendErrorResponse("gs_start_obstacle", "Could not find the obstacle you're trying to start clearing", sender);
            return;
         }

         if (playerStructure.getDateCreated() > 0L) {
            Logger.trace("cannot destroy a structure that's being destroyed");
            this.ext.sendErrorResponse("gs_start_obstacle", "Cannot destroy a structure that's being destroyed", sender);
            return;
         }

         Structure structure = StructureLookup.get(playerStructure.getType());
         boolean inRelics = island.isAmberIsland();
         int costDiamonds = 0;
         int costEth = 0;
         int costCoins = 0;
         int costRelics = 0;
         if (!inRelics) {
            costDiamonds = structure.getCostDiamonds(island.getType());
            costEth = island.isEtherealIsland() ? structure.getCostEth(island.getType()) : 0;
            costCoins = structure.getCostCoins(island.getType());
         } else {
            costRelics = structure.getCostRelics(island.getType());
         }

         if (!player.canBuy((long)costCoins, (long)costEth, (long)costDiamonds, 0L, 0L, (long)costRelics, 0)) {
            this.ext.sendErrorResponse("gs_buy_structure", "You do not have enough coins/diamonds to clear this structure", sender);
            return;
         }

         player.chargePlayer(sender, this, costCoins, costEth, costDiamonds, 0L, 0, costRelics, 0);
         if (costDiamonds > 0) {
            this.logDiamondUsage(sender, "start_obstacle", costDiamonds, player.getLevel(), structure.getEntityId());
         }

         long now = MSMExtension.CurrentDBTime();
         long later = now + (long)structure.getBuildTimeMs();
         playerStructure.setDateCreated(now);
         playerStructure.setBuildingCompletedTime(later);
         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         response.putLong("user_structure_id", playerStructureId);
         response.putSFSObject("user_structure", playerStructure.toSFSObject(island));
         SFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_start_obstacle", response, sender);
      } catch (Exception var20) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         response.putLong("user_structure_id", playerStructureId);
         this.send("gs_start_obstacle", response, sender);
         Logger.trace(var20, "error starting obstacle", "   params : " + params.getDump());
      }

   }

   private void clearObstacle(User sender, ISFSObject params) {
      long playerStructureId = params.getLong("user_structure_id");

      try {
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         PlayerStructure playerStructure = island.getStructureByID(playerStructureId);
         if (playerStructure == null) {
            this.ext.sendErrorResponse("gs_clear_obstacle", "Could not find the obstacle you're trying to clear", sender);
            return;
         }

         if (playerStructure.getTimeRemaining() > 0L) {
            this.ext.sendErrorResponse("gs_clear_obstacle", "The obstacle has not yet completely destroyed", sender);
            return;
         }

         int xpReward = StructureLookup.get(playerStructure.getType()).getXp();
         player.rewardXp(sender, this, xpReward);
         island.removeStructure(playerStructure);
         this.serverQuestEvent(sender, "obstacle_removed", 1);
         boolean hasObstacle = false;
         Iterator var10 = island.getStructures().iterator();

         while(var10.hasNext()) {
            PlayerStructure pStructure = (PlayerStructure)var10.next();
            Structure s = StructureLookup.get(pStructure.getType());
            if (s.isObstacle()) {
               hasObstacle = true;
               break;
            }
         }

         if (!hasObstacle) {
            this.serverQuestEvent(sender, "no_obstacles", 1);
         }

         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         response.putLong("user_structure_id", playerStructureId);
         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_clear_obstacle", response, sender);
      } catch (Exception var13) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         response.putLong("user_structure_id", playerStructureId);
         this.send("gs_clear_obstacle", response, sender);
         Logger.trace(var13, "error clearing obstacle", "   params : " + params.getDump());
      }

   }

   private void clearObstacleSpeedUp(User sender, ISFSObject params) {
      long playerStructureId = params.getLong("user_structure_id");

      try {
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         PlayerStructure playerStructure = island.getStructureByID(playerStructureId);
         if (playerStructure == null) {
            this.ext.sendErrorResponse("gs_clear_obstacle_speed_up", "Could not find the obstacle you're trying to speed up", sender);
            return;
         }

         if (null != params.getInt("speed_up_type") && 0 != params.getInt("speed_up_type")) {
            if (1 == params.getInt("speed_up_type")) {
               if (!player.hasSpeedUpCredit()) {
                  this.ext.sendErrorResponse("gs_clear_obstacle_speed_up", "Unfortunately there are currently no offers available to you. Please try again later", sender);
                  return;
               }

               this.ext.stats.trackSpeedupWithVideo(sender, "speedup_clear", (long)playerStructure.getType(), playerStructure.getID(), playerStructure.getTimeRemaining(), playerStructure.getInitialTimeRemaining());
               playerStructure.reduceStructureTimeByVideo();
               player.adjustSpeedUpCredit(sender, this, -1, Player.SPEED_UP_TYPES.VIDEO);
            }
         } else {
            int diamondsRequired = Game.DiamondsRequiredToComplete(playerStructure.getTimeRemaining());
            if (!player.canBuy(0L, 0L, (long)diamondsRequired, 0L, 0L, 0L, 0)) {
               this.ext.sendErrorResponse("gs_clear_obstacle_speed_up", "You do not have enough diamonds to speed up destroying", sender);
               return;
            }

            if (playerStructure.getTimeRemaining() > 5L) {
               player.chargePlayer(sender, this, 0, 0, diamondsRequired, 0L, 0, 0, 0);
               JSONObject speedupInfo = MSMStats.extraSpeedupInfo(playerStructure.getTimeRemaining(), playerStructure.getInitialTimeRemaining(), playerStructure.getType());
               this.logDiamondUsage(sender, "speedup_clear", diamondsRequired, player.getLevel(), speedupInfo, playerStructure.getEntityId());
               playerStructure.finishBuildingNow();
            }
         }

         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         this.send("gs_clear_obstacle_speed_up", response, sender);
         response = new SFSObject();
         response.putLong("user_structure_id", playerStructure.getID());
         ISFSArray responseVars = new SFSArray();
         ISFSObject completed = new SFSObject();
         completed.putLong("building_completed", playerStructure.getBuildingCompletedTime());
         responseVars.addSFSObject(completed);
         ISFSObject created = new SFSObject();
         created.putLong("date_created", playerStructure.getDateCreated());
         responseVars.addSFSObject(created);
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_update_structure", response, sender);
      } catch (Exception var12) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         response.putLong("user_structure_id", playerStructureId);
         this.send("gs_clear_obstacle_speed_up", response, sender);
         Logger.trace(var12, "error clearing obstacle", "   params : " + params.getDump());
      }

   }

   private void startBaking(User sender, ISFSObject params) {
      long playerStructureId = params.getLong("user_structure_id");

      try {
         int foodIndex = params.getInt("food_index");
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         PlayerStructure playerStructure = island.getStructureByID(playerStructureId);
         if (island.getBakingByStructureId(playerStructureId) != null) {
            Logger.trace("ERROR: Bakery already has baking in progress");
            this.ext.sendErrorResponse("gs_start_baking", "Bakery already has baking in progress", sender);
            return;
         }

         ISFSArray foodOptions = StructureLookup.get(playerStructure.getType()).getExtra().getSFSArray("food_options");
         ISFSObject bakingData = foodOptions.getSFSObject(foodIndex);
         int costCoins = bakingData.getInt("cost");
         if (!player.canBuy((long)costCoins, 0L, 0L, 0L, 0L, 0L, 0)) {
            this.ext.sendErrorResponse("gs_buy_structure", "You do not have enough coins to produce this food", sender);
            return;
         }

         int amountOfFood = bakingData.getInt("food");
         int foodOptionId = bakingData.getInt("id");
         long finishedAt = island.getBakeryCompletionTimeForIsland(bakingData, player);
         ISFSObject userBakingData = new SFSObject();
         userBakingData.putLong("user_baking_id", island.getNextBakingId());
         userBakingData.putLong("island", island.getID());
         userBakingData.putLong("user_structure", playerStructureId);
         userBakingData.putInt("food_option_id", foodOptionId);
         userBakingData.putInt("food_count", amountOfFood);
         userBakingData.putLong("started_at", MSMExtension.CurrentDBTime());
         userBakingData.putLong("finished_at", finishedAt);
         PlayerBaking playerBaking = new PlayerBaking(userBakingData);
         island.addBaking(playerBaking);
         player.chargePlayer(sender, this, costCoins, 0, 0, 0L, 0, 0, 0);
         ISFSObject questEvent = new SFSObject();
         questEvent.putInt("bake_item", foodOptionId);
         this.serverQuestEvent(sender, questEvent);
         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         response.putLong("user_structure_id", playerStructureId);
         response.putSFSObject("user_baking", playerBaking.getData());
         response.putInt("food_option_id", foodOptionId);
         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_start_baking", response, sender);
         this.ext.stats.trackBaking(sender, foodOptionId, playerStructure.getType(), playerStructureId);
      } catch (Exception var21) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         response.putLong("user_structure_id", playerStructureId);
         this.send("gs_start_baking", response, sender);
         Logger.trace(var21, "error starting baking", "   params : " + params.getDump());
      }

   }

   private void startRebake(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      PlayerIsland island = player.getActiveIsland();
      ArrayList<ISFSObject> bakingOrders = new ArrayList();
      int totalCost = 0;
      Iterator islandStructures = island.getStructures().iterator();

      while(islandStructures.hasNext()) {
         PlayerStructure playerStructure = (PlayerStructure)islandStructures.next();
         if (playerStructure.isBakery()) {
            PlayerBaking pb = island.getBakingByStructureId(playerStructure.getID());
            if (pb == null) {
               Integer lastBaked = island.getLastBaked(playerStructure.getID());
               if (lastBaked != null) {
                  ISFSArray foodOptions = StructureLookup.get(playerStructure.getType()).getExtra().getSFSArray("food_options");
                  ISFSObject bakingData = foodOptions.getSFSObject(lastBaked);
                  totalCost += bakingData.getInt("cost");
                  ISFSObject order = new SFSObject();
                  order.putLong("user_structure_id", playerStructure.getID());
                  order.putInt("food_index", lastBaked);
                  bakingOrders.add(order);
               }
            }
         }
      }

      if (!player.canBuy((long)totalCost, 0L, 0L, 0L, 0L, 0L, 0)) {
         this.ext.sendErrorResponse("gs_start_baking", "You do not have enough coins to produce this food", sender);
      } else {
         Iterator orders = bakingOrders.iterator();

         while(orders.hasNext()) {
            this.startBaking(sender, (ISFSObject)orders.next());
         }

      }
   }

   private void finishBaking(User sender, ISFSObject params) {
      long playerBakingId = params.getLong("user_baking_id");

      try {
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         PlayerBaking playerBaking = island.getBakingByID(playerBakingId);
         if (playerBaking == null) {
            this.ext.sendErrorResponse("gs_finish_baking", "user_baking_id does not exist in player active island", sender);
            return;
         }

         PlayerStructure playerStructure = island.getStructureByID(playerBaking.getStructureId());
         if (playerStructure == null) {
            this.ext.sendErrorResponse("gs_finish_baking", "baking structure does not exist in player active island", sender);
            return;
         }

         ISFSArray foodOptions = StructureLookup.get(playerStructure.getType()).getExtra().getSFSArray("food_options");
         if (playerBaking.getTimeRemaining() > 0L) {
            this.ext.sendErrorResponse("gs_finish_baking", "Baking is not yet complete", sender);
            return;
         }

         int xpReward = 0;
         int bakedFoodAmount = playerBaking.getData().getInt("food_count");
         Integer bakedFoodOptionId = playerBaking.getData().getInt("food_option_id");
         Iterator i = foodOptions.iterator();

         SFSObject food;
         while(i.hasNext()) {
            food = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
            int foodOptionId;
            if (bakedFoodOptionId != null) {
               foodOptionId = food.getInt("id");
               if (foodOptionId == bakedFoodOptionId) {
                  xpReward = food.getInt("xp");
                  break;
               }
            } else {
               foodOptionId = food.getInt("food");
               if (foodOptionId == bakedFoodAmount) {
                  xpReward = food.getInt("xp");
                  break;
               }
            }
         }

         player.rewardXp(sender, this, xpReward);
         player.adjustFood(sender, this, bakedFoodAmount);
         island.removeBaking(playerBaking);
         food = new SFSObject();
         food.putBool("success", true);
         food.putLong("user_baking_id", playerBakingId);
         SFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         food.putSFSArray("properties", responseVars);
         this.send("gs_finish_baking", food, sender);
      } catch (Exception var16) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         response.putLong("user_baking_id", playerBakingId);
         this.send("gs_finish_baking", response, sender);
         Logger.trace(var16, "error finishing baking", "   params : " + params.getDump());
      }

   }

   private void bakingSpeedUp(User sender, ISFSObject params) {
      try {
         long userBakingId = params.getLong("user_baking_id");
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         PlayerBaking playerBaking = island.getBakingByID(userBakingId);
         if (playerBaking == null) {
            this.ext.sendErrorResponse("gs_speed_up_baking", "Could not find the bakery you're trying to speed up", sender);
            return;
         }

         if (null != params.getInt("speed_up_type") && 0 != params.getInt("speed_up_type")) {
            if (1 == params.getInt("speed_up_type")) {
               if (!player.hasSpeedUpCredit()) {
                  this.ext.sendErrorResponse("gs_speed_up_baking", "Unfortunately there are currently no offers available to you. Please try again later", sender);
                  return;
               }

               this.ext.stats.trackSpeedupWithVideo(sender, "speedup_baking", playerBaking.getStructureId(), playerBaking.getID(), playerBaking.getTimeRemaining(), playerBaking.getInitialTimeRemaining());
               playerBaking.reduceBakingTimeByVideo();
               player.adjustSpeedUpCredit(sender, this, -1, Player.SPEED_UP_TYPES.VIDEO);
            }
         } else {
            int diamondCost = Game.DiamondsRequiredToComplete(playerBaking.getTimeRemaining());
            if (!player.canBuy(0L, 0L, (long)diamondCost, 0L, 0L, 0L, 0)) {
               this.ext.sendErrorResponse("gs_speed_up_baking", "You do not have enough diamonds to speed up baking", sender);
               return;
            }

            if (playerBaking.getTimeRemaining() > 5L) {
               player.chargePlayer(sender, this, 0, 0, diamondCost, 0L, 0, 0, 0);
               JSONObject speedupInfo = MSMStats.extraSpeedupInfo(playerBaking.getTimeRemaining(), playerBaking.getInitialTimeRemaining(), 0);
               this.logDiamondUsage(sender, "speedup_baking", diamondCost, player.getLevel(), speedupInfo, -1);
               playerBaking.finishBakingNow();
            }
         }

         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         response.putLong("user_baking_id", userBakingId);
         response.putLong("finished_at", playerBaking.getCompletionTime());
         response.putLong("started_at", playerBaking.getStartTime());
         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_speed_up_baking", response, sender);
      } catch (Exception var10) {
         Logger.trace(var10, "error during baking speedup", "   params : " + params.getDump());
      }

   }

   private void startFuzing(User sender, ISFSObject params) {
      long playerStructureId = params.getLong("structure_id");

      try {
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         if (island.getFuzerBuddyByStructureId(playerStructureId) != null) {
            Logger.trace("ERROR: Fuzer already has fuzing in progress");
            this.ext.sendErrorResponse("gs_start_fuzing", "Fuzer already has fuzing in progress", sender);
            return;
         }

         if (PlayerFuzeBuddy.BuddyStructure == null) {
            this.ext.sendErrorResponse("gs_start_fuzing", "Could not find buddy entity", sender);
            return;
         }

         boolean create = params.getBool("create");
         int costDiamonds = PlayerFuzeBuddy.BuddyStructure.getCostDiamonds(island.getType());
         int costEth = island.isEtherealIsland() ? PlayerFuzeBuddy.BuddyStructure.getCostEth(island.getType()) : 0;
         int costCoins = PlayerFuzeBuddy.BuddyStructure.getCostCoins(island.getType());
         if (create && !player.canBuy((long)costCoins, (long)costEth, (long)costDiamonds, 0L, 0L, 0L, 0)) {
            this.ext.sendErrorResponse("gs_start_fuzing", "You do not have enough money to fuze", sender);
            return;
         }

         ArrayList<PlayerStructure> fuzerBuddies = new ArrayList();
         Iterator islandStructures = island.getStructures().iterator();

         while(islandStructures.hasNext()) {
            PlayerStructure islandStructure = (PlayerStructure)islandStructures.next();
            if (islandStructure.inFuzer()) {
               fuzerBuddies.add(islandStructure);
            }
         }

         if (!create && fuzerBuddies.size() != 2) {
            this.ext.sendErrorResponse("gs_start_fuzing", "Incorrect number of buddies in the fuzer, (should be 2)", sender);
            return;
         }

         float colorR = 0.0F;
         float colorY = 0.0F;
         float colorB = 0.0F;
         if (!create) {
            float buddy1R = ((PlayerStructure)fuzerBuddies.get(0)).getR();
            float buddy1Y = ((PlayerStructure)fuzerBuddies.get(0)).getY();
            float buddy1B = ((PlayerStructure)fuzerBuddies.get(0)).getB();
            float buddy2R = ((PlayerStructure)fuzerBuddies.get(1)).getR();
            float buddy2Y = ((PlayerStructure)fuzerBuddies.get(1)).getY();
            float buddy2B = ((PlayerStructure)fuzerBuddies.get(1)).getB();
            colorR = (float)Math.sqrt((double)(buddy1R * buddy1R + buddy2R * buddy2R) / 2.0D);
            colorY = (float)Math.sqrt((double)(buddy1Y * buddy1Y + buddy2Y * buddy2Y) / 2.0D);
            colorB = (float)Math.sqrt((double)(buddy1B * buddy1B + buddy2B * buddy2B) / 2.0D);
         } else {
            colorR = params.getFloat("colorR");
            colorY = params.getFloat("colorY");
            colorB = params.getFloat("colorB");
            boolean isPrimary = false;
            if (colorR == 1.0F && colorY == 0.0F && colorB == 0.0F) {
               isPrimary = true;
            } else if (colorR == 0.0F && colorY == 1.0F && colorB == 0.0F) {
               isPrimary = true;
            } else if (colorR == 0.0F && colorY == 0.0F && colorB == 1.0F) {
               isPrimary = true;
            }

            if (!isPrimary) {
               colorR = 1.0F;
               colorY = 0.0F;
               colorB = 0.0F;
            }
         }

         int fuzeTime = PlayerFuzeBuddy.BuddyStructure.getBuildTimeMs();
         if (ShortenedFuzingEvent.hasTimedEventNow(player, island.getType())) {
            fuzeTime = ShortenedFuzingEvent.getTimedEventFuzeTime(player, fuzeTime, island.getType());
         }

         ISFSObject userFuzingData = new SFSObject();
         userFuzingData.putLong("structure_id", island.getStructureByID(playerStructureId).getID());
         userFuzingData.putDouble("colorR", (double)colorR);
         userFuzingData.putDouble("colorY", (double)colorY);
         userFuzingData.putDouble("colorB", (double)colorB);
         userFuzingData.putLong("started_on", MSMExtension.CurrentDBTime());
         userFuzingData.putLong("finished_on", MSMExtension.CurrentDBTime() + (long)fuzeTime);
         userFuzingData.putBool("create", create);
         PlayerFuzeBuddy playerFuzing = new PlayerFuzeBuddy(userFuzingData);
         island.addFuzeBuddy(playerFuzing);
         if (create) {
            player.chargePlayer(sender, this, costCoins, costEth, costDiamonds, 0L, 0, 0, 0);
            if (costDiamonds > 0) {
               this.logDiamondUsage(sender, "start_fuzing", costDiamonds, player.getLevel());
            }
         }

         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         response.putLong("structure_id", playerStructureId);
         response.putSFSObject("user_fuzing", playerFuzing.getData());
         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_start_fuzing", response, sender);
      } catch (Exception var22) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         response.putLong("structure_id", playerStructureId);
         this.send("gs_start_fuzing", response, sender);
         Logger.trace(var22, "error starting fuzing", "   params : " + params.getDump());
      }

   }

   private void finishFuzing(User sender, ISFSObject params) {
      long structureId = params.getLong("structure_id");

      try {
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         PlayerFuzeBuddy playerFuzing = island.getFuzerBuddyByStructureId(structureId);
         if (playerFuzing == null) {
            this.ext.sendErrorResponse("gs_finish_fuzing", "Fuzer buddy does not exist in player active island", sender);
            return;
         }

         PlayerStructure playerStructure = island.getStructureByID(playerFuzing.getStructureID());
         if (playerStructure == null) {
            this.ext.sendErrorResponse("gs_finish_fuzing", "Fuzer structure does not exist in player active island", sender);
            return;
         }

         if (playerFuzing.getTimeRemaining() > 0L) {
            this.ext.sendErrorResponse("gs_finish_fuzing", "Fuzing is not yet complete", sender);
            return;
         }

         if (PlayerFuzeBuddy.BuddyStructure == null) {
            this.ext.sendErrorResponse("gs_finish_fuzing", "Could not find buddy entity", sender);
            return;
         }

         Structure structure = StructureLookup.get(playerStructure.getType());
         int pos_x = params.getInt("pos_x");
         int pos_y = params.getInt("pos_y");
         int flip = params.getInt("flip");
         long currentTimestamp = MSMExtension.CurrentDBTime();
         SFSObject newBuddyData = new SFSObject();
         newBuddyData.putLong("user_structure_id", island.getNextStructureId());
         newBuddyData.putLong("island", island.getID());
         newBuddyData.putLong("structure", (long)PlayerFuzeBuddy.BuddyStructure.getID());
         newBuddyData.putInt("pos_x", pos_x);
         newBuddyData.putInt("pos_y", pos_y);
         newBuddyData.putDouble("scale", 1.0D);
         newBuddyData.putInt("flip", flip);
         newBuddyData.putInt("is_complete", 1);
         newBuddyData.putFloat("colorR", playerFuzing.getR());
         newBuddyData.putFloat("colorY", playerFuzing.getY());
         newBuddyData.putFloat("colorB", playerFuzing.getB());
         newBuddyData.putLong("date_created", currentTimestamp);
         newBuddyData.putLong("building_completed", currentTimestamp + (long)structure.getBuildTimeMs());
         newBuddyData.putLong("last_collection", currentTimestamp);
         newBuddyData.putInt("settings", 43690);
         island.removeFuzeBuddy(playerFuzing);
         PlayerStructure newBuddy = new PlayerStructure(newBuddyData);
         island.addStructure(newBuddy);
         ISFSObject response = new SFSObject();
         SFSArray deleteIds;
         if (playerFuzing.getCreate()) {
            this.serverQuestEvent(sender, "conjure_buddy", 1);
         } else {
            ArrayList<PlayerStructure> deleteBuddies = new ArrayList();
            deleteIds = new SFSArray();
            Iterator islandStructures = island.getStructures().iterator();

            PlayerStructure islandStructure;
            while(islandStructures.hasNext()) {
               islandStructure = (PlayerStructure)islandStructures.next();
               if (islandStructure.inFuzer()) {
                  ISFSObject obj = new SFSObject();
                  obj.putLong("id", islandStructure.getID());
                  deleteIds.addSFSObject(obj);
                  deleteBuddies.add(islandStructure);
               }
            }

            response.putSFSArray("delete_ids", deleteIds);
            islandStructures = deleteBuddies.iterator();

            while(islandStructures.hasNext()) {
               islandStructure = (PlayerStructure)islandStructures.next();
               island.removeStructure(islandStructure);
            }

            this.serverQuestEvent(sender, "fuze_buddies", 1);
         }

         if (island.hasHappinessTree()) {
            SFSArray monsterEffects = this.addHappyTreeEffects(island, sender);
            response.putSFSArray("monster_happy_effects", monsterEffects);
         }

         ISFSObject questEvent = new SFSObject();
         questEvent.putInt("object", newBuddy.getEntityId());
         questEvent.putUtfString("structure_type", newBuddy.getStructureType());
         this.serverQuestEvent(sender, questEvent);
         response.putBool("success", true);
         response.putSFSObject("user_structure", newBuddy.toSFSObject(island));
         response.putLong("structure_id", structureId);
         deleteIds = new SFSArray();
         player.addPlayerPropertyData(deleteIds, false);
         response.putSFSArray("properties", deleteIds);
         this.send("gs_finish_fuzing", response, sender);
      } catch (Exception var23) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         response.putLong("structure_id", structureId);
         this.send("gs_finish_fuzing", response, sender);
         Logger.trace(var23, "error finishing fuzing", "   params : " + params.getDump());
      }

   }

   private void adminFinishFuzing(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminFinishFuzing: Error! Trying to invoke admin without privileges!");
      } else {
         long structureId = params.getLong("structure_id");

         try {
            long playerIslandId = params.getLong("user_island_id");
            Player player = (Player)sender.getProperty("friend_object");
            PlayerIsland island = player.getIslandByID(playerIslandId);
            PlayerFuzeBuddy playerFuzing = island.getFuzerBuddyByStructureId(structureId);
            if (playerFuzing == null) {
               this.ext.sendErrorResponse("gs_admin_finish_fuzing", "Fuzer buddy does not exist in player active island", sender);
               return;
            }

            PlayerStructure playerStructure = island.getStructureByID(playerFuzing.getStructureID());
            if (playerStructure == null) {
               this.ext.sendErrorResponse("gs_admin_finish_fuzing", "Fuzer structure does not exist in player active island", sender);
               return;
            }

            if (PlayerFuzeBuddy.BuddyStructure == null) {
               this.ext.sendErrorResponse("gs_admin_finish_fuzing", "Could not find buddy entity", sender);
               return;
            }

            Structure structure = StructureLookup.get(playerStructure.getType());
            int pos_x = params.getInt("pos_x");
            int pos_y = params.getInt("pos_y");
            int flip = params.getInt("flip");
            long currentTimestamp = MSMExtension.CurrentDBTime();
            SFSObject newBuddyData = new SFSObject();
            newBuddyData.putLong("user_structure_id", island.getNextStructureId());
            newBuddyData.putLong("island", island.getID());
            newBuddyData.putLong("structure", (long)PlayerFuzeBuddy.BuddyStructure.getID());
            newBuddyData.putInt("pos_x", pos_x);
            newBuddyData.putInt("pos_y", pos_y);
            newBuddyData.putDouble("scale", 1.0D);
            newBuddyData.putInt("flip", flip);
            newBuddyData.putInt("is_complete", 1);
            newBuddyData.putFloat("colorR", playerFuzing.getR());
            newBuddyData.putFloat("colorY", playerFuzing.getY());
            newBuddyData.putFloat("colorB", playerFuzing.getB());
            newBuddyData.putLong("date_created", currentTimestamp);
            newBuddyData.putLong("building_completed", currentTimestamp + (long)structure.getBuildTimeMs());
            newBuddyData.putLong("last_collection", currentTimestamp);
            newBuddyData.putInt("settings", 43690);
            island.removeFuzeBuddy(playerFuzing);
            PlayerStructure newBuddy = new PlayerStructure(newBuddyData);
            island.addStructure(newBuddy);
            ISFSObject response = new SFSObject();
            SFSArray deleteIds;
            if (!playerFuzing.getCreate()) {
               ArrayList<PlayerStructure> deleteBuddies = new ArrayList();
               deleteIds = new SFSArray();
               Iterator islandStructures = island.getStructures().iterator();

               PlayerStructure islandStructure;
               while(islandStructures.hasNext()) {
                  islandStructure = (PlayerStructure)islandStructures.next();
                  if (islandStructure.inFuzer()) {
                     ISFSObject obj = new SFSObject();
                     obj.putLong("id", islandStructure.getID());
                     deleteIds.addSFSObject(obj);
                     deleteBuddies.add(islandStructure);
                  }
               }

               response.putSFSArray("delete_ids", deleteIds);
               islandStructures = deleteBuddies.iterator();

               while(islandStructures.hasNext()) {
                  islandStructure = (PlayerStructure)islandStructures.next();
                  island.removeStructure(islandStructure);
               }
            }

            if (island.hasHappinessTree()) {
               SFSArray monsterEffects = this.addHappyTreeEffects(island, sender);
               response.putSFSArray("monster_happy_effects", monsterEffects);
            }

            ISFSObject questEvent = new SFSObject();
            questEvent.putInt("object", newBuddy.getEntityId());
            questEvent.putUtfString("structure_type", newBuddy.getStructureType());
            this.serverQuestEvent(sender, questEvent);
            this.ext.savePlayerIsland(player, island, false);
            response.putBool("success", true);
            response.putSFSObject("user_structure", newBuddy.toSFSObject(island));
            response.putLong("structure_id", structureId);
            deleteIds = new SFSArray();
            player.addPlayerPropertyData(deleteIds, true);
            response.putSFSArray("properties", deleteIds);
            this.send("gs_admin_finish_fuzing", response, sender);
         } catch (Exception var25) {
            ISFSObject response = new SFSObject();
            response.putBool("success", false);
            response.putLong("structure_id", structureId);
            this.send("gs_admin_finish_fuzing", response, sender);
            Logger.trace(var25, "error finishing fuzing", "   params : " + params.getDump());
         }

      }
   }

   private void fuzingSpeedUp(User sender, ISFSObject params) {
      try {
         long userFuzingId = params.getLong("structure_id");
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         PlayerFuzeBuddy playerFuzing = island.getFuzeBuddyByID(userFuzingId);
         if (null != params.getInt("speed_up_type") && 0 != params.getInt("speed_up_type")) {
            if (1 == params.getInt("speed_up_type")) {
               if (!player.hasSpeedUpCredit()) {
                  this.ext.sendErrorResponse("gs_speed_up_fuzing", "Unfortunately there are currently no offers available to you. Please try again later", sender);
                  return;
               }

               this.ext.stats.trackSpeedupWithVideo(sender, "speedup_fuzing", playerFuzing.getStructureID(), 0L, playerFuzing.getTimeRemaining(), playerFuzing.getTimeRemaining());
               playerFuzing.reduceFuzingTimeByVideo();
               player.adjustSpeedUpCredit(sender, this, -1, Player.SPEED_UP_TYPES.VIDEO);
            }
         } else {
            int diamondCost = Game.DiamondsRequiredToComplete(playerFuzing.getTimeRemaining());
            if (!player.canBuy(0L, 0L, (long)diamondCost, 0L, 0L, 0L, 0)) {
               this.ext.sendErrorResponse("gs_speed_up_fuzing", "You do not have enough diamonds to speed up fuzing", sender);
               return;
            }

            if (playerFuzing.getTimeRemaining() > 5L) {
               player.chargePlayer(sender, this, 0, 0, diamondCost, 0L, 0, 0, 0);
               JSONObject speedupInfo = MSMStats.extraSpeedupInfo(playerFuzing.getTimeRemaining(), playerFuzing.getInitialTimeRemaining(), 0);
               this.logDiamondUsage(sender, "speedup_fuzing", diamondCost, player.getLevel(), speedupInfo, -1);
               playerFuzing.finishNow();
            }
         }

         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         response.putLong("structure_id", userFuzingId);
         response.putLong("finished_on", playerFuzing.getCompletionTime());
         response.putLong("started_on", playerFuzing.getStartTime());
         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_speed_up_fuzing", response, sender);
      } catch (Exception var10) {
         Logger.trace(var10, "error during fuzing speedup", "   params : " + params.getDump());
      }

   }

   private void collectFromMine(User sender, ISFSObject params) {
      try {
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         PlayerStructure playerMine = island.getMine();
         if (playerMine == null) {
            this.ext.sendErrorResponse("gs_collect_from_mine", "Player has no mine", sender);
            return;
         }

         int diamonds = playerMine.collectFromMine();
         if (diamonds == 0) {
            this.ext.sendErrorResponse("gs_collect_from_mine", "Mine is not ready yet", sender);
            return;
         }

         player.adjustDiamonds(sender, this, diamonds);
         this.ext.stats.trackReward(sender, "mine", "diamonds", (long)diamonds);
         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         this.send("gs_collect_from_mine", response, sender);
         response = new SFSObject();
         response.putLong("user_structure_id", playerMine.getID());
         ISFSArray responseVars = new SFSArray();
         ISFSObject property = new SFSObject();
         property.putLong("last_collection", playerMine.getLastCollectionTime());
         responseVars.addSFSObject(property);
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_update_structure", response, sender);
      } catch (Exception var10) {
         Logger.trace(var10, "error collecting from mine", "   params : " + params.getDump());
      }

   }

   private void questEvent(User sender, ISFSObject params) {
      try {
         if (this.solveQuestForUser(sender, (Player)sender.getProperty("player_object"), params)) {
            this.ext.savePlayer((Player)sender.getProperty("player_object"));
         }
      } catch (Exception var4) {
         Logger.trace(var4);
      }

   }

   private boolean solveQuestForUser(User sender, Player player, ISFSObject params) throws Exception {
      boolean questsUpdated = false;
      ISFSObject response = new SFSObject();
      int eventID = params.containsKey("event_id") ? params.getInt("event_id") : 0;
      response.putInt("event_id", eventID);
      params.removeElement("event_id");
      boolean serverGeneratedQuest = params.containsKey("server_generated") ? params.getBool("server_generated") : false;
      params.removeElement("server_generated");
      boolean noSend = params.containsKey("nosend") ? params.getBool("nosend") : false;
      ISFSArray result = this.handleQuestEvent(sender, player, new JSONObject(params.toJson()));
      questsUpdated = result.size() != 0;
      boolean newQuest = false;
      ArrayList<String> newQuestTypes = new ArrayList();

      for(int i = 0; i < result.size(); ++i) {
         ISFSObject obj = result.getSFSObject(i);
         if (obj.containsKey("new")) {
            ISFSArray questArray = obj.getSFSArray("new");
            PlayerQuest q = player.addQuest(questArray.getSFSObject(0));
            if (q != null) {
               obj.putSFSArray("new", q.toSFS());
               newQuest = true;
               ArrayList<String> goals = q.getGoalEvents();
               Iterator var17 = goals.iterator();

               while(var17.hasNext()) {
                  String goal = (String)var17.next();
                  if (!goal.equals("num") && !goal.equals("eval") && !goal.equals("unique")) {
                     newQuestTypes.add(goal);
                  }
               }
            }
         }

         if (obj.containsKey("quest_id")) {
            PlayerQuest pq = player.getQuestByUid(SFSHelpers.getLong("quest_id", obj));
            if (pq != null && pq.getName().startsWith("ACH_") && pq.isComplete()) {
               ISFSObject achievementData = this.createAchievementFromQuest(player, pq);
               if (achievementData != null) {
                  player.addAchievement(new PlayerAchievement(achievementData));
                  ISFSObject objWrapper = new SFSObject();
                  objWrapper.putSFSObject("achievement", achievementData);
                  this.ext.send("gs_achievement_unlocked", objWrapper, sender);
               }
            }
         }
      }

      if (!noSend && (!serverGeneratedQuest || eventID != 0 || result.size() != 0)) {
         response.putSFSArray("result", result);
         this.send("gs_quest", response, sender);
      }

      if (newQuest && params.containsKey("recurse")) {
         questsUpdated = this.solveQuestForUser(sender, player, params) || questsUpdated;
      }

      if (newQuestTypes.contains("on_monster") && newQuestTypes.contains("monster_level")) {
         Collection<PlayerIsland> islands = player.getIslands();
         Iterator var20 = islands.iterator();

         while(var20.hasNext()) {
            PlayerIsland pi = (PlayerIsland)var20.next();
            Collection<PlayerMonster> monsters = pi.getMonsters();
            Iterator var26 = monsters.iterator();

            while(var26.hasNext()) {
               PlayerMonster pm = (PlayerMonster)var26.next();
               ISFSObject qe = new SFSObject();
               qe.putInt("monster_level", pm.getLevel());
               qe.putInt("on_monster", pm.getType());
               qe.putBool("nosend", noSend);
               this.serverQuestEvent(sender, qe);
            }
         }
      }

      return questsUpdated;
   }

   private ISFSArray handleQuestEvent(User sender, Player player, JSONObject event) {
      ISFSArray resultArray = new SFSArray();
      ArrayList<PlayerQuest> quests = player.getQuests();

      for(int i = 0; i < quests.size(); ++i) {
         PlayerQuest quest = (PlayerQuest)quests.get(i);
         if (!quest.isComplete()) {
            String previousStatus = quest.getStatus();
            boolean questResult = quest.onQuestEvent(event);
            this.processNewQuestStatus(sender, player, quest, questResult, previousStatus, this.ext.getDB(), resultArray);
         }
      }

      return resultArray;
   }

   private void processNewQuestStatus(User sender, Player player, PlayerQuest quest, boolean completed, String previousStatus, IDbWrapper db, ISFSArray resultArray) {
      String newStatus = quest.getStatus();
      if (!newStatus.equals(previousStatus)) {
         ISFSObject result = new SFSObject();
         result.put("quest_id", quest.getWrappedId());
         result.putUtfString("status", newStatus);
         resultArray.addSFSObject(result);
      }

      if (completed) {
         try {
            this.processCompletionEventsAsQuestEvents(sender, player, quest, resultArray);
         } catch (Exception var12) {
            Logger.trace(var12);
         }

         this.giveNextQuests(player, quest, db, resultArray);
         JSONObject metricData = new JSONObject();

         try {
            metricData.put("quest_id", quest.getDataId());
            metricData.put("quest_name", quest.getName());
            this.ext.stats.sendUserEvent(sender, "quest_complete", metricData);
         } catch (JSONException var11) {
            Logger.trace((Exception)var11);
         }
      }

   }

   private void processCompletionEventsAsQuestEvents(User sender, Player player, PlayerQuest quest, ISFSArray resultArray) throws Exception {
      if (player.hasQuestGoal("quest")) {
         ISFSObject questCompletedEvent = new SFSObject();
         questCompletedEvent.putUtfString("quest", quest.getName());
         ISFSArray recurseArray = this.handleQuestEvent(sender, player, new JSONObject(questCompletedEvent.toJson()));

         for(int j = 0; j < recurseArray.size(); ++j) {
            resultArray.addSFSObject(recurseArray.getSFSObject(j));
         }
      }

   }

   private void giveNextQuests(Player player, PlayerQuest quest, IDbWrapper db, ISFSArray resultArray) {
      int playerId = player.getData().getInt("user_id");
      ISFSArray next = quest.getNext();

      for(int j = 0; j < next.size(); ++j) {
         String questName = next.getSFSObject(j).getUtfString("quest");
         ISFSObject staticQuestData = QuestLookup.getByName(questName);
         if (staticQuestData == null) {
            Logger.trace("newQuestData is null: " + questName);
         } else {
            int questId = staticQuestData.getInt("id");
            if (!player.hasQuest(questId) && !this.crazyQuestArrayContainsNewQuestId(resultArray, questId)) {
               ISFSObject instanceQuestData = PlayerQuest.getInitialSFSObject(player.getNextQuestIndex(), questId, playerId, "false", true, false);
               ISFSArray newQuest = new SFSArray();
               newQuest.addSFSObject(instanceQuestData);
               newQuest.addSFSObject(staticQuestData);
               ISFSObject result = new SFSObject();
               result.putSFSArray("new", newQuest);
               resultArray.addSFSObject(result);
            }
         }
      }

   }

   private boolean crazyQuestArrayContainsNewQuestId(ISFSArray resultArray, int questId) {
      try {
         for(int i = 0; i < resultArray.size(); ++i) {
            ISFSArray arr = resultArray.getSFSObject(i).getSFSArray("new");
            if (arr != null) {
               int id = arr.getSFSObject(0).getInt("quest_id");
               if (id == questId) {
                  return true;
               }
            }
         }
      } catch (Exception var6) {
         Logger.trace(var6);
      }

      return false;
   }

   private void questRead(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      PlayerQuest quest = player.getQuestByUid(SFSHelpers.getLong("quest_id", params));
      if (quest != null) {
         quest.markRead();
      }

   }

   private void questsRead(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      Collection<Integer> questIds = params.getIntArray("quest_ids");
      Iterator var5 = questIds.iterator();

      while(var5.hasNext()) {
         Integer questId = (Integer)var5.next();
         PlayerQuest quest = player.getQuestByUid((long)questId);
         if (quest != null) {
            quest.markRead();
         }
      }

   }

   private void questCollect(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      ArrayList<PlayerQuest> quests = player.getQuests();
      long userQuestId = SFSHelpers.getLong("quest_id", params);

      for(int i = 0; i < quests.size(); ++i) {
         if (((PlayerQuest)quests.get(i)).getId() == userQuestId) {
            if (!((PlayerQuest)quests.get(i)).isComplete()) {
               break;
            }

            ISFSObject playerUpdate = new SFSObject();
            SFSArray updateVars = new SFSArray();
            playerUpdate.putSFSArray("properties", updateVars);
            PlayerQuest quest = (PlayerQuest)quests.get(i);
            if (quest.collected() != 1) {
               quest.collect();
               ISFSObject rewards = quest.getRewards();
               Iterator rewardItr = rewards.iterator();

               while(true) {
                  SFSObject updateObj;
                  while(true) {
                     if (!rewardItr.hasNext()) {
                        ISFSArray result = new SFSArray();
                        ISFSObject questComplete = new SFSObject();
                        questComplete.put("collect", quest.getWrappedId());
                        result.addSFSObject(questComplete);
                        ISFSObject response = new SFSObject();
                        response.putSFSArray("result", result);
                        this.send("gs_quest", response, sender);
                        this.serverQuestEvent(sender, "collect", quest.getDataId());
                        if (updateVars.size() > 0) {
                           this.send("gs_update_properties", playerUpdate, sender);
                        }

                        player.removeQuestEvents((PlayerQuest)quests.get(i));
                        quests.remove(i);
                        player.addCollectedQuest(quest.getDataId());
                        JSONObject metricData = new JSONObject();

                        try {
                           metricData.put("quest_id", quest.getDataId());
                           metricData.put("quest_name", quest.getName());
                           metricData.put("rewards", updateVars.toJson());
                           this.ext.stats.sendUserEvent(sender, "quest_collect", metricData);
                        } catch (JSONException var18) {
                           Logger.trace((Exception)var18);
                        }

                        return;
                     }

                     Entry<String, SFSDataWrapper> entry = (Entry)rewardItr.next();
                     String key = (String)entry.getKey();
                     int value = (Integer)((SFSDataWrapper)entry.getValue()).getObject();
                     updateObj = new SFSObject();
                     if (key.equals("coins")) {
                        player.adjustCoins(sender, this, value);
                        updateObj.putLong("coins_actual", player.getActualCoins());
                        break;
                     }

                     if (key.equals("diamonds")) {
                        player.adjustDiamonds(sender, this, value);
                        updateObj.putLong("diamonds_actual", player.getActualDiamonds());
                        this.ext.stats.trackReward(sender, "quest_reward", "diamonds", (long)value);
                        break;
                     }

                     if (key.equals("ethereal_currency")) {
                        player.adjustEthCurrency(sender, this, value);
                        updateObj.putLong("ethereal_currency_actual", player.getActualEthCurrency());
                        break;
                     }

                     if (key.equals("food")) {
                        player.adjustFood(sender, this, value);
                        updateObj.putLong("food_actual", player.getActualFood());
                        break;
                     }

                     if (key.equals("xp")) {
                        boolean hasNewLevel = player.rewardXp(sender, this, value);
                        updateObj.putInt("xp", player.getXp());
                        if (hasNewLevel) {
                           updateVars.addSFSObject(updateObj);
                           updateObj = new SFSObject();
                           updateObj.putInt("level", player.getLevel());
                        }
                        break;
                     }

                     if (key.equals("keys")) {
                        player.adjustKeys(sender, this, value);
                        updateObj.putLong("keys_actual", player.getActualKeys());
                        break;
                     }

                     if (key.equals("relics")) {
                        player.adjustRelics(sender, this, value);
                        updateObj.putLong("relics_actual", player.getActualRelics());
                        break;
                     }

                     if (key.equals("egg_wildcards")) {
                        player.adjustEggWildcards(sender, this, value);
                        updateObj.putLong("egg_wildcards_actual", player.getActualEggWildcards());
                        break;
                     }

                     if (key.equals("starpower")) {
                        player.adjustStarpower(sender, this, (long)value);
                        updateObj.putLong("starpower_actual", player.getActualStarpower());
                        break;
                     }

                     if (key.equals("entity")) {
                        break;
                     }

                     Logger.trace("ERROR: quest reward unknown: " + key);
                  }

                  updateVars.addSFSObject(updateObj);
               }
            }

            Logger.trace("ERROR: quest already collected");
         }
      }

   }

   private void updateAchievementStatus(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      ISFSArray achievements = params.getSFSArray("achievements");

      for(int i = 0; i < achievements.size(); ++i) {
         ISFSObject ach = achievements.getSFSObject(i);
         Long id = ach.getLong("user_achievement_id");
         int fbPosted = ach.containsKey("fb_posted") ? ach.getInt("fb_posted") : 0;
         int gcPosted = ach.containsKey("gc_posted") ? ach.getInt("gc_posted") : 0;
         int gpPosted = ach.containsKey("gp_posted") ? ach.getInt("gp_posted") : 0;
         PlayerAchievement achievement = player.getAchievement(id);
         if (achievement != null) {
            achievement.setFacebookPosted(fbPosted);
            achievement.setGameCenterPosted(gcPosted);
            achievement.setGooglePlayPosted(gpPosted);
            this.saveAchievement(achievement);
         }
      }

      ISFSObject response = new SFSObject();
      response.putBool("success", true);
      this.ext.send("gs_update_achievement_status", response, sender);
   }

   private void saveAchievement(PlayerAchievement achievement) {
      try {
         String sql = "UPDATE user_achievements SET fb_posted=?, gc_posted=?, gp_posted=? WHERE user_achievement_id=?";
         Object[] args = new Object[]{achievement.facebookPosted(), achievement.gameCenterPosted(), achievement.googlePlayPosted(), achievement.getID()};
         this.ext.getDB().update(sql, args);
      } catch (Exception var4) {
         Logger.trace(var4);
      }

   }

   private ISFSObject createAchievementFromQuest(Player player, PlayerQuest quest) {
      IDbWrapper db = this.ext.getDB();
      if (player == null) {
         return null;
      } else {
         Long playerId = player.getPlayerId();
         Long questId = quest.getId();
         String achievementName = quest.getName();

         try {
            String sql = "INSERT IGNORE INTO user_achievements SET user=?, user_quest=?, achievement=?";
            Object[] args = new Object[]{playerId, questId, achievementName};
            long newAchId = db.insertGetId(sql, args);
            sql = "SELECT * FROM user_achievements LEFT JOIN achievement_ids ON user_achievements.achievement=achievement_ids.bbb_achieve_id where user_achievement_id=? AND user=?";
            args = new Object[]{newAchId, playerId};
            ISFSArray newAchResult = db.query(sql, args);
            return newAchResult.getSFSObject(0);
         } catch (Exception var12) {
            Logger.trace(var12);
            return null;
         }
      }
   }

   private void processUnclaimedPurchases(User sender, ISFSObject params) {
      this.ext = (MSMExtension)this.getParentExtension();

      try {
         this.ext.processUnclaimedPurchases(sender, this);
         this.ext.processUncollectedOfferRewards(sender, this, params);
      } catch (Exception var4) {
         Logger.trace(var4);
      }

   }

   private void processUncollectedOfferRewards(User sender, ISFSObject params) {
      this.ext = (MSMExtension)this.getParentExtension();

      try {
         this.ext.processUncollectedOfferRewards(sender, this, params);
      } catch (Exception var4) {
         Logger.trace(var4);
      }

   }

   private void convertCurrency(User sender, ISFSObject params) {
      if (params.containsKey("from") && params.containsKey("to")) {
         Player.CurrencyType from = Player.getCurrencyTypeFromString(params.getUtfString("from"));
         Player.CurrencyType to = Player.getCurrencyTypeFromString(params.getUtfString("to"));
         if (from == Player.CurrencyType.Undefined || to == Player.CurrencyType.Undefined) {
            this.ext.sendErrorResponse("gs_currency_conversion", "invalid values for from/to", sender);
         }

         int amount = 1;
         if (params.containsKey("amt")) {
            amount = params.getInt("amt");
         }

         if (from == Player.CurrencyType.Diamonds) {
            if (to == Player.CurrencyType.Coins) {
               this.convertDiamondsToCoins(sender, params);
            } else if (to == Player.CurrencyType.Ethereal) {
               this.convertDiamondsToEthCurrency(sender, params);
            } else if (to == Player.CurrencyType.Relics) {
               Player player = (Player)sender.getProperty("player_object");
               if (player.purchaseRelic(sender, this, amount)) {
                  ISFSObject response = new SFSObject();
                  SFSArray responseVars = new SFSArray();
                  player.addPlayerPropertyData(responseVars, false);
                  response.putSFSArray("properties", responseVars);
                  this.send("gs_update_properties", response, sender);
               }
            }
         } else if (from == Player.CurrencyType.Coins && to == Player.CurrencyType.Ethereal) {
            this.convertCoinsToEthCurrency(sender, params);
         } else if (from == Player.CurrencyType.Ethereal && to == Player.CurrencyType.Diamonds) {
            this.convertEthToDiamondsCurrency(sender, params);
         }

      } else {
         this.convertDiamondsToCoins(sender, params);
      }
   }

   private void convertDiamondsToCoins(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      String[] parts = GameSettings.get("USER_DIAMOND_EXCHANGE_RATE").split(",");
      int diamondCost = Integer.parseInt(parts[0]);
      if (player.canBuy(0L, 0L, (long)diamondCost, 0L, 0L, 0L, 0)) {
         player.chargePlayer(sender, this, 0, 0, diamondCost, 0L, 0, 0, 0);
         this.logDiamondUsage(sender, "convert_currency", diamondCost, player.getLevel());
         float multiplier = Float.parseFloat(parts[2]) * (float)(player.getLevel() - 1);
         int coinsCollected = (int)((float)Integer.parseInt(parts[1]) * (1.0F + multiplier) + 0.5F);
         player.adjustCoins(sender, this, coinsCollected);
         ISFSObject response = new SFSObject();
         SFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_update_properties", response, sender);
      }

   }

   private void convertCoinsToEthCurrency(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      String[] parts = GameSettings.get("USER_COIN_ETH_EXCHANGE_RATE").split(",");
      int coinsCost = Integer.parseInt(parts[0]);
      if (player.canBuy((long)coinsCost, 0L, 0L, 0L, 0L, 0L, 0)) {
         player.chargePlayer(sender, this, coinsCost, 0, 0, 0L, 0, 0, 0);
         int ethCollected = Integer.parseInt(parts[1]);
         player.adjustEthCurrency(sender, this, ethCollected);
         ISFSObject response = new SFSObject();
         SFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_update_properties", response, sender);
      }

   }

   private void convertDiamondsToEthCurrency(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      String[] parts = GameSettings.get("USER_DIAMOND_ETH_EXCHANGE_RATE").split(",");
      int diamondCost = Integer.parseInt(parts[0]);
      if (player.canBuy(0L, 0L, (long)diamondCost, 0L, 0L, 0L, 0)) {
         player.chargePlayer(sender, this, 0, 0, diamondCost, 0L, 0, 0, 0);
         this.logDiamondUsage(sender, "convert_currency_to_eth", diamondCost, player.getLevel());
         int ethCollected = Integer.parseInt(parts[1]);
         player.adjustEthCurrency(sender, this, ethCollected);
         ISFSObject response = new SFSObject();
         SFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_update_properties", response, sender);
      }

   }

   private void convertEthToDiamondsCurrency(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      String[] parts = GameSettings.get("USER_ETH_DIAMOND_EXCHANGE_RATE").split(",");
      int ethRequired = Integer.parseInt(parts[0]);
      if (player.canBuy(0L, (long)ethRequired, 0L, 0L, 0L, 0L, 0)) {
         player.chargePlayer(sender, this, 0, ethRequired, 0, 0L, 0, 0, 0);
         int diamondsCollected = Integer.parseInt(parts[1]);
         player.adjustDiamonds(sender, this, diamondsCollected);
         this.ext.stats.trackReward(sender, "convert_eth_to_diamonds", "diamonds", (long)diamondsCollected);
         ISFSObject response = new SFSObject();
         SFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_update_properties", response, sender);
      }

   }

   private void sendFacebookHelp(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      long friendIslandId = params.getLong("islandId");
      long structureId = 0L;
      if (params.containsKey("structureId")) {
         structureId = params.getLong("structureId");
      }

      long friendBbbId = params.getLong("bbbId");
      String type = params.getUtfString("type");
      if (player.getBbbId() == friendBbbId) {
         this.ext.sendErrorResponse("gs_send_facebook_help", "Tsk tsk.  You can't send yourself help.", sender);
      } else {
         try {
            String sql = "SELECT volatile FROM user_island_data WHERE island=?";
            Object[] args = new Object[]{friendIslandId};
            ISFSArray volatileResultsForIsland = this.ext.getDB().query(sql, args);
            if (volatileResultsForIsland.size() == 0) {
               this.ext.sendErrorResponse("gs_send_facebook_help", "No breeding or hatching data for that friend", sender);
               return;
            }

            String associatedVolatileKey = "";
            if (type.compareTo("nursery") == 0) {
               associatedVolatileKey = "eggs";
            } else if (type.compareTo("breeding") == 0) {
               associatedVolatileKey = "breeding";
            }

            String volatileData = Helpers.decompressJsonDataField(volatileResultsForIsland.getSFSObject(0).getUtfString("volatile"), "{}");
            if (volatileData == null || associatedVolatileKey.length() != 0 && volatileData.indexOf(associatedVolatileKey) == -1) {
               this.ext.sendErrorResponse("gs_facebook_help_nursery", "Can't find breeding or hatching data for that friend", sender);
               return;
            }

            ISFSObject volatileJson = SFSObject.newFromJsonData(volatileData);
            ISFSArray associatedJsonData = volatileJson.getSFSArray(associatedVolatileKey);
            int numStructureTypeNeedsHelpOnIsland = associatedJsonData != null ? associatedJsonData.size() : 0;
            sql = "SELECT id FROM user_facebook_help_instances WHERE island_id=? AND type=?";
            args = new Object[]{friendIslandId, type};
            ISFSArray facebookHelpForIslandType = this.ext.getDB().query(sql, args);
            SFSObject userResponse;
            if (facebookHelpForIslandType.size() >= numStructureTypeNeedsHelpOnIsland * GameSettings.getInt("FACEBOOK_HELP_NUM_INSTANCES")) {
               userResponse = new SFSObject();
               userResponse.putBool("success", false);
               userResponse.putInt("error", 1);
               this.send("gs_send_facebook_help", userResponse, sender);
               return;
            }

            sql = "INSERT INTO user_facebook_help_instances SET bbb_id=?, from_bbb_id=?, island_id=?, structure_id=?, collected=0, type=?";
            args = new Object[]{friendBbbId, player.getBbbId(), friendIslandId, structureId, type};
            this.ext.getDB().update(sql, args);
            this.ext.stats.trackFacebookHelpSend(sender, friendBbbId);
            userResponse = new SFSObject();
            userResponse.putBool("success", true);
            userResponse.putUtfString("type", type);
            this.send("gs_send_facebook_help", userResponse, sender);
         } catch (Exception var21) {
            ISFSObject userResponse = new SFSObject();
            userResponse.putBool("success", false);
            this.send("gs_send_facebook_help", userResponse, sender);
            Logger.trace(var21);
         }

      }
   }

   private void handleFacebookHelpInstances(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");

      try {
         String sql = "SELECT * FROM user_facebook_help_instances WHERE bbb_id=?";
         Object[] query_params = new Object[]{player.getBbbId()};
         SFSArray allHelpInstances = this.ext.getDB().query(sql, query_params);
         SFSArray newHelpInstances = new SFSArray();
         SFSArray oldHelpInstances = new SFSArray();

         for(int i = 0; i < allHelpInstances.size(); ++i) {
            ISFSObject result = allHelpInstances.getSFSObject(i);
            if (result.getInt("collected") == 0) {
               newHelpInstances.addSFSObject(result);
            } else {
               oldHelpInstances.addSFSObject(result);
            }
         }

         ArrayList<PlayerEgg> updatedEggs = new ArrayList();
         ArrayList<PlayerBreeding> updatedBreeding = new ArrayList();
         int i;
         if (newHelpInstances.size() > 0) {
            ArrayList<PlayerEgg> prevSpedUpEggs = new ArrayList();
            ArrayList<PlayerBreeding> prevSpedUpBreeds = new ArrayList();
            i = 0;

            while(true) {
               if (i >= newHelpInstances.size()) {
                  sql = "UPDATE user_facebook_help_instances SET collected=1 WHERE bbb_id=?";
                  query_params = new Object[]{player.getBbbId()};
                  this.ext.getDB().update(sql, query_params);
                  break;
               }

               ISFSObject result = newHelpInstances.getSFSObject(i);
               long islandId = result.getLong("island_id");
               PlayerIsland pIsland = player.getIslandByID(islandId);
               SFSArray collectedHelpForThisIsland = new SFSArray();

               int helpInd;
               ISFSObject row;
               for(helpInd = 0; helpInd < oldHelpInstances.size(); ++helpInd) {
                  row = oldHelpInstances.getSFSObject(helpInd);
                  if (row.getLong("island_id") == islandId) {
                     collectedHelpForThisIsland.addSFSObject(row);
                  }
               }

               for(helpInd = 0; helpInd < collectedHelpForThisIsland.size(); ++helpInd) {
                  row = collectedHelpForThisIsland.getSFSObject(helpInd);
                  long structureId = row.getLong("structure_id");
                  if (structureId != 0L) {
                     PlayerEgg previouslyHelpedEgg = pIsland.getEggByStructureId(structureId);
                     if (previouslyHelpedEgg != null) {
                        prevSpedUpEggs.add(previouslyHelpedEgg);
                     } else {
                        PlayerBreeding prevHelpedBreed = pIsland.getBreedingByStructureId(structureId);
                        if (prevHelpedBreed != null) {
                           prevSpedUpBreeds.add(prevHelpedBreed);
                        }
                     }
                  }
               }

               if (pIsland == null) {
                  Logger.trace("Island " + islandId + " for bbbid " + player.getBbbId() + " could not be found.");
                  this.ext.sendErrorResponse("gs_handle_facebook_help_instances", "The island being targeted for help does not exist.", sender);
                  return;
               }

               long structureRewarded = 0L;
               String type = result.getUtfString("type");
               if (type.equals("nursery")) {
                  PlayerEgg egg = pIsland.getAnyValidEggForFacebookSpeedup(prevSpedUpEggs);
                  if (egg != null) {
                     prevSpedUpEggs.add(egg);
                     egg.reduceHatchByFacebookHelpTime();
                     structureRewarded = egg.getStructureID();
                     if (!updatedEggs.contains(egg)) {
                        updatedEggs.add(egg);
                     }
                  }
               } else if (type.equals("breeding")) {
                  PlayerBreeding breeding = pIsland.getAnyValidBreedForFacebookSpeedup(prevSpedUpBreeds);
                  if (breeding != null && breeding.getTimeRemaining() > 0L) {
                     prevSpedUpBreeds.add(breeding);
                     breeding.reduceBreedingByFacebookHelpTime();
                     structureRewarded = breeding.getStructureID();
                     if (!updatedBreeding.contains(breeding)) {
                        updatedBreeding.add(breeding);
                     }
                  }
               }

               this.ext.stats.trackFacebookHelpCollect(sender, result.getLong("from_bbb_id"), type);
               if (structureRewarded != 0L && result.getLong("structure_id") != structureRewarded) {
                  sql = "UPDATE user_facebook_help_instances SET structure_id=? WHERE id=?";
                  query_params = new Object[]{structureRewarded, result.getLong("id")};
                  this.ext.getDB().update(sql, query_params);
               }

               ++i;
            }
         }

         ISFSArray eggResults = new SFSArray();
         if (updatedEggs.size() > 0) {
            for(int i = 0; i < updatedEggs.size(); ++i) {
               eggResults.addSFSObject(((PlayerEgg)updatedEggs.get(i)).toSFSObject());
            }
         }

         ISFSArray breedingResults = new SFSArray();
         if (updatedBreeding.size() > 0) {
            for(i = 0; i < updatedBreeding.size(); ++i) {
               breedingResults.addSFSObject(((PlayerBreeding)updatedBreeding.get(i)).toSFSObject());
            }
         }

         ISFSObject userResponse = new SFSObject();
         userResponse.putSFSArray("egg_results", eggResults);
         userResponse.putSFSArray("breeding_results", breedingResults);
         userResponse.putInt("count", newHelpInstances.size());
         userResponse.putBool("success", true);
         this.send("gs_handle_facebook_help_instances", userResponse, sender);
      } catch (Exception var25) {
         ISFSObject userResponse = new SFSObject();
         userResponse.putBool("success", false);
         this.send("gs_handle_facebook_help_instances", userResponse, sender);
         Logger.trace(var25);
      }

   }

   private void requestFacebookHelpPermissions(User sender, ISFSObject params) {
      long islandId = params.getLong("island_id");
      long structureId = 0L;
      if (params.containsKey("structure_id")) {
         structureId = params.getLong("structure_id");
      }

      String type = "";
      if (params.containsKey("type")) {
         type = params.getUtfString("type");
      } else {
         type = "nursery";
      }

      try {
         String sql = "SELECT * FROM user_facebook_help_instances WHERE island_id=? AND structure_id=? AND type=?";
         Object[] query_params = new Object[]{islandId, structureId, type};
         SFSArray results = this.ext.getDB().query(sql, query_params);
         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         response.putLong("island_id", islandId);
         if (results.size() >= GameSettings.getInt("FACEBOOK_HELP_NUM_INSTANCES")) {
            response.putBool("has_permission", false);
         } else {
            response.putBool("has_permission", true);
         }

         this.send("gs_request_facebook_help_permissions", response, sender);
      } catch (Exception var12) {
         ISFSObject userResponse = new SFSObject();
         userResponse.putBool("success", false);
         this.send("gs_request_facebook_help_permissions", userResponse, sender);
         Logger.trace(var12);
      }

   }

   private void adminPurchaseBuyback(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminPurchaseBuyback: Error! Trying to invoke admin without privileges!");
      } else {
         try {
            Player friend = (Player)sender.getProperty("friend_object");
            long playerIslandId = params.getLong("user_island_id");
            PlayerIsland pi = friend.getIslandByID(playerIslandId);
            if (pi == null) {
               this.ext.sendErrorResponse("gs_purchase_buyback", "Specified island for buyback does not exist.", sender);
               return;
            }

            if (!pi.hasBuyback()) {
               this.ext.sendErrorResponse("gs_purchase_buyback", "No valid buyback found for this island.", sender);
               return;
            }

            int xPos = params.getInt("x_pos");
            int yPos = params.getInt("y_pos");
            String sql = "SELECT monster_id FROM monsters WHERE entity=?";
            Object[] query_params = new Object[]{pi.getBuyback().getID()};
            SFSArray results = this.ext.getDB().query(sql, query_params);
            if (results.size() == 0) {
               this.ext.sendErrorResponse("gs_purchase_buyback", "Invalid entity id.", sender);
               return;
            }

            int monsterId = results.getSFSObject(0).getInt("monster_id");
            Monster monsterData = MonsterLookup.get(monsterId);
            int bedsAvailable = pi.bedsAvailable();
            if (bedsAvailable != -1 && monsterData.beds() > bedsAvailable) {
               this.ext.sendErrorResponse("gs_purchase_buyback", "Not enough beds on island.", sender);
               return;
            }

            SFSObject newMonsterData = PlayerMonster.createMonsterSFS(friend.getNextMonsterIndex(), pi.getBuyback(), pi, xPos, yPos, 0);
            PlayerMonster newPlayerMonster = new PlayerMonster(newMonsterData, pi);
            pi.addMonster(newPlayerMonster);
            pi.removeBuyback();
            this.ext.savePlayerIsland(friend, pi, false);
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            response.putSFSObject("monster", newPlayerMonster.toSFSObject(pi));
            this.send("gs_admin_purchase_buyback", response, sender);
         } catch (Exception var17) {
            Logger.trace(var17, "error during admin buyback", "   params : " + params.getDump());
            ISFSObject response = new SFSObject();
            response.putBool("success", false);
            this.send("gs_purchase_buyback", response, sender);
         }

      }
   }

   private void purchaseBuyback(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      long islandId = params.getLong("island_id");
      int xPos = params.getInt("x_pos");
      int yPos = params.getInt("y_pos");
      int flip = 0;
      if (params.containsKey("flip")) {
         flip = params.getInt("flip");
      }

      try {
         PlayerIsland pi = player.getIslandByID(islandId);
         if (pi == null) {
            this.ext.sendErrorResponse("gs_purchase_buyback", "Specified island for buyback does not exist.", sender);
            return;
         }

         if (!pi.hasBuyback()) {
            this.ext.sendErrorResponse("gs_purchase_buyback", "No valid buyback found for this island.", sender);
            return;
         }

         int buybackEntityId = (int)pi.getBuyback().getID();
         Monster monsterData = MonsterLookup.getFromEntityId(buybackEntityId);
         if (monsterData == null) {
            this.ext.sendErrorResponse("gs_purchase_buyback", "Invalid entity id.", sender);
            return;
         }

         int bedsAvailable = pi.bedsAvailable();
         if (bedsAvailable != -1 && monsterData.beds() > bedsAvailable) {
            this.ext.sendErrorResponse("gs_purchase_buyback", "Not enough beds on island.", sender);
            return;
         }

         boolean inRelics = pi.isAmberIsland();
         int coinCost = 0;
         int ethCost = 0;
         int relicCost = 0;
         PlayerBuyback pbb = pi.getBuyback();
         if (inRelics) {
            relicCost = pbb.getCoinCost();
         } else if (pi.isEtherealIsland()) {
            ethCost = pbb.getCoinCost();
         } else {
            coinCost = pbb.getCoinCost();
         }

         if (!player.canBuy((long)coinCost, (long)ethCost, 0L, 0L, 0L, (long)relicCost, 0)) {
            this.ext.sendErrorResponse("gs_purchase_buyback", "Not enough coins.", sender);
            return;
         }

         String boxData = null;
         if (monsterData.isBoxMonsterType() && pbb.getBoxedEggs() != null) {
            boxData = pbb.getBoxedEggs().toJson();
         }

         SFSObject newMonsterData = PlayerMonster.createMonsterSFS(monsterData.getMonsterID(), pbb.getName(), pbb.getMegaMonsterData(), pbb.getCostumeData(), player.getIslandByID(islandId).getIndex(), islandId, xPos, yPos, flip, player.getNextMonsterIndex(), pbb.getLevel(), MSMExtension.CurrentDBTime(), MSMExtension.CurrentDBTime(), false, (long)pbb.getTimesFed(), boxData, pbb.getEvolutionUnlocked(), pbb.getPowerupUnlocked());
         PlayerMonster newPlayerMonster = new PlayerMonster(newMonsterData, pi);
         int bookValue = newPlayerMonster.bookValue();
         if (bookValue == -1) {
            bookValue = MonsterLookup.get(newPlayerMonster.getType()).getSecondaryCurrencyCost(player, pi, true, pi.isAmberIsland());
         }

         newPlayerMonster.setBookValue(bookValue);
         pi.addMonster(newPlayerMonster);
         pi.removeBuyback();
         if (inRelics) {
            if (relicCost != -1) {
               player.adjustRelics(sender, this, -1 * relicCost);
            }
         } else if (pi.isEtherealIsland()) {
            if (ethCost != -1) {
               player.adjustEthCurrency(sender, this, -1 * ethCost);
            } else {
               player.adjustEthCurrency(sender, this, -1 * monsterData.ethSellingPrice(player, pi.getType()));
            }
         } else if (coinCost != -1) {
            player.adjustCoins(sender, this, -1 * coinCost);
         } else {
            player.adjustCoins(sender, this, -1 * monsterData.coinSellingPrice(player, pi.getType()));
         }

         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         response.putSFSObject("monster", newPlayerMonster.toSFSObject(pi));
         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_purchase_buyback", response, sender);
      } catch (Exception var23) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         this.send("gs_purchase_buyback", response, sender);
         Logger.trace(var23);
      }

   }

   private void collectFacebookReward(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      String type = params.getUtfString("type");
      Long serverTime = MSMExtension.CurrentDBTime() / 1000L;
      Long lastReward = player.getLastFacebookPostReward();
      int rewardInterval = GameSettings.getInt("USER_FB_POST_REWARD_REFRESH");
      int reward = GameSettings.getInt("USER_FB_POST_REWARD");
      if (lastReward != null) {
         lastReward = lastReward / 1000L + (long)(3600 * rewardInterval);
      }

      SFSObject response;
      if (reward > 0 && (lastReward == null || serverTime > lastReward) && type.equalsIgnoreCase("feedpost")) {
         player.adjustDiamonds(sender, this, reward);
         response = new SFSObject();
         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_update_properties", response, sender);
         player.setLastFacebookPostReward(MSMExtension.CurrentDBTime());
         response = new SFSObject();
         response.putBool("success", true);
         response.putLong("last_fb_post_reward", MSMExtension.CurrentDBTime());
         this.send("gs_collect_facebook_reward", response, sender);
         this.ext.stats.trackReward(sender, "fb_post_reward", "diamonds", (long)reward);
      } else {
         response = new SFSObject();
         response.putBool("success", false);
         this.send("gs_collect_facebook_reward", response, sender);
      }

   }

   private void getFriends(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      ISFSArray friends = null;
      ISFSArray tribes = null;
      ISFSArray requests = null;
      StringBuilder tribeBuilder = new StringBuilder();
      SFSArray seasonResults = null;

      SFSObject globalRankings;
      int i;
      ISFSObject friend;
      try {
         if (params.containsKey("season0") && params.containsKey("season1")) {
            seasonResults = new SFSArray();
            seasonResults.addSFSObject(params.getSFSObject("season0"));
            seasonResults.addSFSObject(params.getSFSObject("season1"));
         } else {
            seasonResults = BattleSeasons.getSeasons();
         }

         ISFSArray rawFriends = this.loadFriendsWithFavs(player, seasonResults);
         if (rawFriends != null) {
            friends = this.getFriendData(rawFriends);
         }

         if (friends != null) {
            globalRankings = new SFSObject();
            globalRankings.putInt("friends", friends.size());
            globalRankings.putInt("recurse", 1);
            this.serverQuestEvent(sender, globalRankings);

            for(i = 0; i < friends.size(); ++i) {
               friend = friends.getSFSObject(i);
               if (friend.containsKey("tribe")) {
                  if (tribeBuilder.length() != 0) {
                     tribeBuilder.append(",");
                  }

                  tribeBuilder.append(friend.getLong("tribe"));
               }
            }
         }

         String sql = "SELECT * FROM user_tribal_requests WHERE user=? LIMIT " + GameSettings.get("FRIEND_MAX_LOAD");
         requests = this.ext.getDB().query(sql, new Object[]{player.getPlayerId()});
      } catch (Exception var18) {
         Logger.trace(var18, "Error getting request data from DB");
      }

      if (requests != null && requests.size() > 0) {
         for(int i = 0; i < requests.size(); ++i) {
            if (tribeBuilder.length() != 0) {
               tribeBuilder.append(",");
            }

            tribeBuilder.append(requests.getSFSObject(i).getLong("tribe"));
         }
      }

      if (tribeBuilder.length() != 0) {
         try {
            String sql = "SELECT user_tribal_islands.*, user_tribal_monsters.monster FROM user_tribal_islands LEFT JOIN user_tribal_monsters ON user_tribal_islands.chief = user_tribal_monsters.user_monster_id WHERE user_tribal_islands.user_island_id IN (" + tribeBuilder.toString() + ")";
            tribes = this.ext.getDB().query(sql);
         } catch (Exception var17) {
            Logger.trace(var17, "Error getting tribe data from DB");
         }
      }

      this.loadTopTribes();
      ISFSObject response = new SFSObject();
      response.putBool("success", true);
      response.putSFSArray("friends", (ISFSArray)(friends != null ? friends : new SFSArray()));
      response.putSFSArray("tribes", tribes != null ? tribes : new SFSArray());
      response.putSFSArray("requests", requests != null ? requests : new SFSArray());
      response.putSFSArray("top_tribes", this.ext.topTribes != null ? this.ext.topTribes : new SFSArray());
      if (seasonResults != null) {
         globalRankings = new SFSObject();

         for(i = 0; i < seasonResults.size(); ++i) {
            friend = seasonResults.getSFSObject(i);
            int campaignId = friend.getInt("campaign_id");
            long scheduleStart = friend.getLong("schedule_started_on");
            BattleVersusChampionRanks.RankTable rt = BattleVersusChampionRanks.getRankTable(campaignId, scheduleStart);
            globalRankings.putSFSArray("rankTable" + i, rt.toSFSArrayLimited());
         }

         response.putSFSObject("global_battle_rankings", globalRankings);
      }

      this.send("gs_get_friends", response, sender);
   }

   private void getRandomTribes(User sender, ISFSObject params) {
      boolean success = false;
      ISFSObject response = new SFSObject();
      ISFSArray randomTribes = this.getRandomTribes();
      if (randomTribes != null) {
         success = true;
         response.putSFSArray("random_tribes", randomTribes);
      }

      response.putBool("success", success);
      this.send("gs_get_random_tribes", response, sender);
   }

   private ISFSArray getRandomTribes() {
      String sql = "SELECT MAX(user_island_id) FROM user_tribal_islands";
      long maxTribeId = 0L;

      try {
         ISFSArray maxTribeIdQuery = null;
         maxTribeIdQuery = this.ext.getDB().query(sql);
         if (maxTribeIdQuery != null && maxTribeIdQuery.size() > 0) {
            ISFSObject r = maxTribeIdQuery.getSFSObject(0);
            maxTribeId = r.getLong("MAX(user_island_id)");
         }
      } catch (Exception var14) {
         Logger.trace(var14, "Error getting max tribe id from DB");
      }

      maxTribeId -= 60L;
      if (maxTribeId < 0L) {
         maxTribeId = 0L;
      }

      Random rng = new Random();
      long randomId = (long)(rng.nextDouble() * (double)maxTribeId);
      ISFSArray randomTribes = null;

      try {
         int maxNumMultiUserTribes = 10;
         randomTribes = this.queryForRandomTribes(randomId, maxNumMultiUserTribes, true, false);
         if (randomTribes.size() < maxNumMultiUserTribes && maxTribeId != 0L) {
            ISFSArray randomTribes2 = this.queryForRandomTribes(0L, maxNumMultiUserTribes - randomTribes.size(), true, false);
            Iterator itr = randomTribes2.iterator();

            while(itr.hasNext()) {
               SFSDataWrapper data = (SFSDataWrapper)itr.next();
               randomTribes.add(data);
            }
         }

         int maxNumSingleUserTribes = 20 - randomTribes.size();
         ISFSArray singleUserTribes = this.queryForRandomTribes(randomId, maxNumSingleUserTribes, false, true);
         if (singleUserTribes.size() < maxNumSingleUserTribes && maxTribeId != 0L) {
            ISFSArray singleUserTribes2 = this.queryForRandomTribes(0L, maxNumSingleUserTribes - singleUserTribes.size(), false, true);
            Iterator itr2 = singleUserTribes2.iterator();

            while(itr2.hasNext()) {
               SFSDataWrapper data = (SFSDataWrapper)itr2.next();
               randomTribes.add(data);
            }
         }

         Iterator itr = singleUserTribes.iterator();

         while(itr.hasNext()) {
            SFSDataWrapper data = (SFSDataWrapper)itr.next();
            randomTribes.add(data);
         }
      } catch (Exception var15) {
         Logger.trace(var15, "Error getting random tribe data from DB");
      }

      return randomTribes;
   }

   ISFSArray queryForRandomTribes(long minUserIslandId, int limit, boolean multiuserOnly, boolean singleUserOnly) throws Exception {
      String additionalConstraint = null;
      if (multiuserOnly) {
         additionalConstraint = "members > 1";
      } else if (singleUserOnly) {
         additionalConstraint = "members = 1";
      }

      String sql;
      if (additionalConstraint == null) {
         sql = "SELECT user_tribal_islands.user_island_id, user_tribal_islands.chief, user_tribal_islands.name, user_tribal_islands.rank, user_tribal_islands.members, user_tribal_monsters.user_monster_id, user_tribal_monsters.monster FROM user_tribal_islands LEFT JOIN user_tribal_monsters ON user_tribal_islands.chief=user_tribal_monsters.user_monster_id WHERE user_tribal_islands.user_island_id > ? AND user_tribal_islands.members < 30 LIMIT ?";
      } else {
         sql = "SELECT user_tribal_islands.user_island_id, user_tribal_islands.chief, user_tribal_islands.name, user_tribal_islands.rank, user_tribal_islands.members, user_tribal_monsters.user_monster_id, user_tribal_monsters.monster FROM user_tribal_islands LEFT JOIN user_tribal_monsters ON user_tribal_islands.chief=user_tribal_monsters.user_monster_id WHERE user_tribal_islands.user_island_id > ? AND user_tribal_islands.members < 30 AND " + additionalConstraint + " LIMIT ?";
      }

      return this.ext.getDB().query(sql, new Object[]{minUserIslandId, limit});
   }

   private void unlockBreedingStructure(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      if (player.hasActiveQuest(17) && !player.questComplete(17)) {
         PlayerIsland plantIsland = player.getActiveIsland();
         if (plantIsland != null) {
            Collection<PlayerStructure> structures = plantIsland.getStructures();
            Iterator var6 = structures.iterator();

            while(var6.hasNext()) {
               PlayerStructure s = (PlayerStructure)var6.next();
               if (s.isBreeding()) {
                  ISFSObject qe = new SFSObject();
                  qe.putInt("object", s.getEntityId());
                  this.serverQuestEvent(sender, qe);
                  break;
               }
            }
         }
      }

   }

   private void collectInviteReward(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      String type = params.getUtfString("type");
      boolean rewarded = false;
      if (type.equals("fb") && player.getFacebookInviteReward() == 0) {
         player.setFacebookInviteReward(1);
         this.serverQuestEvent(sender, "invite_friend_fb", 1);
         rewarded = true;
      } else if (type.equals("twitter") && player.getTwitterInviteReward() == 0) {
         player.setTwitterInviteReward(1);
         this.serverQuestEvent(sender, "invite_friend_twitter", 1);
         rewarded = true;
      } else if (type.equals("email") && player.getEmailInviteReward() == 0) {
         player.setEmailInviteReward(1);
         this.serverQuestEvent(sender, "invite_friend_email", 1);
         rewarded = true;
      }

      ISFSObject response = new SFSObject();
      response.putBool("success", rewarded);
      this.send("gs_collect_invite_reward", response, sender);
      if (rewarded) {
         int diamondReward = GameSettings.getInt("USER_INVITE_REWARD");
         player.adjustDiamonds(sender, this, diamondReward);
         response = new SFSObject();
         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_update_properties", response, sender);
         this.ext.stats.trackReward(sender, "invite_" + type, "diamonds", (long)diamondReward);
      }

   }

   private void getFriendVisitData(User sender, ISFSObject params) {
      long userID = params.getLong("user_id");

      try {
         Player friend = this.ext.createPlayerByID(userID, false, new VersionInfo((String)sender.getProperty("client_version")), (String)null, (String)null);
         if (friend == null) {
            this.ext.sendVersionError(sender, true);
            return;
         }

         if (friend.isBanned()) {
            this.ext.sendFriendBannedError(sender, friend);
            return;
         }

         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         response.putSFSObject("friend_object", friend.toSFSObject());
         Vector<SFSObject> removeEntries = new Vector();
         String sql = "SELECT island_id, user_structure FROM user_torch_gifts WHERE recipient_bbbid=? AND collected=0";
         Object[] args = new Object[]{friend.getBbbId()};
         SFSArray result = this.ext.getDB().query(sql, args);

         int i;
         SFSObject s;
         Long islandId;
         Long userStructureId;
         for(i = 0; i < result.size(); ++i) {
            s = (SFSObject)result.getElementAt(i);
            islandId = s.getLong("island_id");
            userStructureId = s.getLong("user_structure");
            PlayerStructure curStructure = friend.getIslandByID(islandId).getStructureByID(userStructureId);
            if (curStructure == null) {
               removeEntries.add(s);
            } else if (!curStructure.isTorch()) {
               removeEntries.add(s);
            }
         }

         if (removeEntries.size() > 0) {
            for(i = 0; i < removeEntries.size(); ++i) {
               s = (SFSObject)removeEntries.elementAt(i);
               islandId = s.getLong("island_id");
               userStructureId = s.getLong("user_structure");
               sql = "DELETE FROM user_torch_gifts WHERE recipient_bbbid=? AND island_id=? AND user_structure=?";
               args = new Object[]{friend.getBbbId(), islandId, userStructureId};
               this.ext.getDB().update(sql, args);
            }

            sql = "SELECT island_id, user_structure FROM user_torch_gifts WHERE recipient_bbbid=? AND collected=0";
            args = new Object[]{friend.getBbbId()};
            result = this.ext.getDB().query(sql, args);
         }

         if (result.size() > 0) {
            response.putSFSArray("torch_gifts", result);
         }

         SFSArray ratings = new SFSArray();

         try {
            Player player = (Player)sender.getProperty("player_object");
            Iterator itr = friend.getIslands().iterator();

            while(itr.hasNext()) {
               PlayerIsland island = (PlayerIsland)itr.next();
               sql = "SELECT id FROM user_island_weekly_user_likes WHERE user=? AND user_island_id=? LIMIT 1";
               args = new Object[]{player.getPlayerId(), island.getID()};
               SFSObject s = new SFSObject();
               s.putLong("island", island.getID());
               boolean rated = false;
               if (this.ext.getDB().exists(sql, args)) {
                  rated = true;
               }

               s.putBool("rated", rated);
               ratings.addSFSObject(s);
            }
         } catch (Exception var17) {
            Logger.trace(var17);
         }

         if (ratings.size() > 0) {
            response.putSFSArray("ratings", ratings);
         }

         this.serverQuestEvent(sender, "island_visited", 1);
         if (params.containsKey("island_type")) {
            response.putInt("island_type", params.getInt("island_type"));
         }

         this.send("gs_get_friend_visit_data", response, sender);
      } catch (Exception var18) {
         Logger.trace(var18);
         MSMExtension.getInstance().addCWMetric("Visit Error", 1.0D);
      }

   }

   private void getRandomVisitData(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      Player friend = null;
      boolean islandRated = false;
      PlayerIsland targetIsland = null;

      try {
         String actualClientVersion = (String)sender.getProperty("client_version");
         VersionInfo clientVer = new VersionInfo(actualClientVersion);
         Random random = new Random();
         int maxIslandTypes = IslandLookup.maxIslandTypeIndex;
         SFSArray topStages;
         if (random.nextInt(maxIslandTypes) < maxIslandTypes - 1) {
            this.loadTopStages(false);
            topStages = this.ext.topStages;
         } else {
            this.loadTopStages(true);
            topStages = this.ext.topComposerStages;
         }

         int count;
         for(count = 0; count < 5; ++count) {
            if (topStages != null && topStages.size() > 0) {
               random = new Random();
               ISFSObject stage = topStages.getSFSObject(random.nextInt(topStages.size()));
               friend = this.ext.createPlayerByID(stage.getLong("user"), false, clientVer, (String)null, (String)null);
               if (friend != null && !friend.isBanned()) {
                  targetIsland = friend.getActiveIsland();
                  Collection<PlayerIsland> friendIslands = friend.getIslands();
                  if (friendIslands.size() > 0) {
                     targetIsland = (PlayerIsland)friendIslands.toArray()[random.nextInt(friendIslands.size())];
                  }

                  if (targetIsland != null && !targetIsland.isTribalIsland() && (!targetIsland.isCelestialIsland() || VersionData.Instance().getMaxServerVersionFromClientVersion(clientVer).compareTo(IslandLookup.get(12).minVersion()) >= 0)) {
                     islandRated = this.hasRatedIsland(player.getPlayerId(), targetIsland.getID());
                     break;
                  }
               }
            }
         }

         ISFSObject response = new SFSObject();
         if (count == 5) {
            Logger.trace("count == 5 ");
            friend = player;
            islandRated = true;
         } else if (targetIsland != null) {
            response.putLong("user_island", targetIsland.getID());
         }

         response.putBool("success", true);
         response.putSFSObject("friend_object", friend.toSFSObject());
         boolean updateStructures = false;
         String sql = "SELECT island_id, user_structure FROM user_torch_gifts WHERE recipient_bbbid=? AND collected=0";
         SFSArray result = this.ext.getDB().query(sql, new Object[]{friend.getBbbId()});

         for(int i = 0; i < result.size(); ++i) {
            SFSObject s = (SFSObject)result.getElementAt(i);
            Long islandId = s.getLong("island_id");
            Long userStructureId = s.getLong("user_structure");
            PlayerIsland curIsland = friend.getIslandByID(islandId);
            PlayerStructure curStructure = null;
            if (curIsland != null) {
               curStructure = curIsland.getStructureByID(userStructureId);
            }

            if (curIsland == null || curStructure == null || !curStructure.isTorch()) {
               updateStructures = true;
               sql = "DELETE FROM user_torch_gifts WHERE recipient_bbbid=? AND island_id=? AND user_structure=?";
               this.ext.getDB().update(sql, new Object[]{friend.getBbbId(), islandId, userStructureId});
            }
         }

         if (updateStructures) {
            sql = "SELECT island_id, user_structure FROM user_torch_gifts WHERE recipient_bbbid=? AND collected=0";
            result = this.ext.getDB().query(sql, new Object[]{friend.getBbbId()});
         }

         if (result.size() > 0) {
            response.putSFSArray("torch_gifts", result);
         }

         response.putBool("island_rated", islandRated);
         this.serverQuestEvent(sender, "island_visited", 1);
         this.send("gs_get_random_visit_data", response, sender);
      } catch (Exception var23) {
         Logger.trace(var23, "Error getting user data from DB");
         MSMExtension.getInstance().addCWMetric("Visit Error", 1.0D);
      }

   }

   private void visitSpecificFriendIsland(User sender, ISFSObject params) {
      try {
         long friendBbbID = params.getLong("bbb_id");
         long islandId = params.getLong("user_island_id");
         Player friend = this.ext.createPlayerByID(this.ext.getUserIdByBBBId(friendBbbID), false, new VersionInfo((String)sender.getProperty("client_version")), (String)null, (String)null);
         if (friend == null) {
            this.ext.sendVersionError(sender, true);
            return;
         }

         if (friend.isBanned()) {
            this.ext.sendFriendBannedError(sender, friend);
            return;
         }

         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         response.putSFSObject("friend_object", friend.toSFSObject());
         Vector<SFSObject> removeEntries = new Vector();
         String sql = "SELECT island_id, user_structure FROM user_torch_gifts WHERE recipient_bbbid=? AND collected=0";
         Object[] args = new Object[]{friend.getBbbId()};
         SFSArray result = this.ext.getDB().query(sql, args);

         int i;
         SFSObject s;
         Long torchIslandId;
         Long userStructureId;
         for(i = 0; i < result.size(); ++i) {
            s = (SFSObject)result.getElementAt(i);
            torchIslandId = s.getLong("island_id");
            userStructureId = s.getLong("user_structure");
            PlayerStructure curStructure = friend.getIslandByID(torchIslandId).getStructureByID(userStructureId);
            if (curStructure == null) {
               removeEntries.add(s);
            } else if (!curStructure.isTorch()) {
               removeEntries.add(s);
            }
         }

         if (removeEntries.size() > 0) {
            for(i = 0; i < removeEntries.size(); ++i) {
               s = (SFSObject)removeEntries.elementAt(i);
               torchIslandId = s.getLong("island_id");
               userStructureId = s.getLong("user_structure");
               sql = "DELETE FROM user_torch_gifts WHERE recipient_bbbid=? AND island_id=? AND user_structure=?";
               args = new Object[]{friend.getBbbId(), torchIslandId, userStructureId};
               this.ext.getDB().update(sql, args);
            }

            sql = "SELECT island_id, user_structure FROM user_torch_gifts WHERE recipient_bbbid=? AND collected=0";
            args = new Object[]{friend.getBbbId()};
            result = this.ext.getDB().query(sql, args);
         }

         if (result.size() > 0) {
            response.putSFSArray("torch_gifts", result);
         }

         response.putLong("user_island", islandId);
         this.serverQuestEvent(sender, "island_visited", 1);
         this.ext.stats.trackIslandVisit(sender, friend.getBbbId(), friend.getIslandByID(islandId).getType(), islandId);
         this.send("gs_visit_specific_friend_island", response, sender);
      } catch (Exception var18) {
         Logger.trace(var18);
         MSMExtension.getInstance().addCWMetric("Visit Error", 1.0D);
      }

   }

   private void setFriendAsFav(User sender, ISFSObject params) {
      long friendBbbId = params.getLong("bbb_id");
      boolean isFav = params.getBool("is_fav");
      Player player = (Player)sender.getProperty("player_object");
      boolean success = this.makeMyFriendAFav(player.getBbbId(), friendBbbId, isFav);
      ISFSObject response = new SFSObject();
      response.putBool("success", success);
      this.send("gs_set_fav_friend", response, sender);
   }

   private boolean makeMyFriendAFav(long bbbIdOfMe, long bbbIdOfFriend, boolean fav) {
      String sql = "SELECT * FROM user_friends WHERE (user_1=? and user_2=?) or (user_2=? and user_1=?)";
      Object[] args = new Object[]{bbbIdOfMe, bbbIdOfFriend, bbbIdOfMe, bbbIdOfFriend};

      try {
         SFSArray result = this.ext.getDB().query(sql, args);
         if (result.size() == 0) {
            return false;
         } else {
            for(int i = 0; i < result.size(); ++i) {
               ISFSObject curResult = result.getSFSObject(i);
               long user1 = curResult.getLong("user_1");
               long user2 = curResult.getLong("user_2");
               String fieldToMod;
               if (user1 == bbbIdOfMe) {
                  fieldToMod = "user_1_fav";
               } else {
                  fieldToMod = "user_2_fav";
               }

               sql = "UPDATE user_friends SET " + fieldToMod + "=? WHERE user_1=? and user_2=?";
               args = new Object[]{fav ? 1 : 0, user1, user2};
               this.ext.getDB().update(sql, args);
            }

            return true;
         }
      } catch (Exception var16) {
         Logger.trace(var16);
         return false;
      }
   }

   private void incrTorchesILitCount(long bbbIdOfMe, long bbbIdOfFriend) {
      String sql = "SELECT * FROM user_friends WHERE (user_1=? and user_2=?) or (user_2=? and user_1=?)";
      Object[] args = new Object[]{bbbIdOfMe, bbbIdOfFriend, bbbIdOfMe, bbbIdOfFriend};

      try {
         SFSArray result = this.ext.getDB().query(sql, args);
         if (result.size() == 0) {
            return;
         }

         for(int i = 0; i < result.size(); ++i) {
            ISFSObject curResult = result.getSFSObject(i);
            long user1 = curResult.getLong("user_1");
            long user2 = curResult.getLong("user_2");
            String fieldToMod;
            if (user1 == bbbIdOfMe) {
               fieldToMod = "user_2_torches_lit";
            } else {
               fieldToMod = "user_1_torches_lit";
            }

            Integer oldValue = curResult.getInt(fieldToMod);
            sql = "UPDATE user_friends SET " + fieldToMod + "=? WHERE user_1=? and user_2=?";
            args = new Object[]{oldValue == null ? 1 : oldValue + 1, user1, user2};
            this.ext.getDB().update(sql, args);
         }
      } catch (Exception var16) {
         Logger.trace(var16);
      }

   }

   private void getTribalIslandData(User sender, ISFSObject params) {
      long tribe = params.getLong("tribe");
      SFSObject response = new SFSObject();

      try {
         String sql = "SELECT ABS(CAST(i.chief AS SIGNED)-CAST(m.user AS SIGNED)) AS d, user, name FROM user_tribal_mappings m INNER JOIN user_tribal_islands i ON m.tribe = i.user_island_id INNER JOIN users u ON m.user = u.user_id WHERE tribe = ? ORDER BY d ASC";
         ISFSArray results = this.ext.getDB().query(sql, new Object[]{tribe});
         if (results != null && results.size() > 0) {
            long chief = SFSHelpers.getLong("user", results.getSFSObject(0));
            Player tribeChief = this.ext.createPlayerByID(chief, false, (VersionInfo)null, (String)null, (String)null);
            if (tribeChief == null) {
               this.ext.sendVersionError(sender, true);
               return;
            }

            if (tribeChief.isBanned()) {
               Logger.trace(">>>> Attempting to visit banned player in getTribalIslandData()");
               this.ext.sendFriendBannedError(sender, tribeChief);
               return;
            }

            PlayerIsland tribalIsland = tribeChief.getIslandByIslandIndex(9);
            long tribeIslandID;
            if (tribalIsland == null) {
               tribeIslandID = 0L;
               InitialPlayerIslandData islandData = PlayerIslandFactory.CreateIslandInitialStructures(tribeChief, 9, tribeIslandID);
               ISFSObject islandSFS = new SFSObject();
               islandSFS.putLong("user", tribeChief.getPlayerId());
               islandSFS.putLong("user_island_id", tribeIslandID);
               islandSFS.putInt("island", 9);
               islandSFS.putSFSArray("structures", islandData.structures);
               islandSFS.putSFSArray("monsters", new SFSArray());
               islandSFS.putSFSArray("volatile", new SFSArray());
               islandSFS.putUtfString("name", "");
               islandSFS.putLong("date_created", MSMExtension.CurrentDBTime());
               islandSFS.putInt("likes", 0);
               islandSFS.putInt("dislikes", 0);
               islandSFS.putDouble("warp_speed", 1.0D);
               islandSFS.putLong("tribal_id", tribe);
               tribalIsland = new PlayerIsland(islandSFS);
               tribeChief.addIsland(tribalIsland);
               this.ext.loadPlayerTribalIslandData(tribeChief, tribalIsland, false);
            } else {
               tribeIslandID = tribalIsland.getID();
            }

            response.putSFSObject("friend_object", tribeChief.toSFSObject());
            response.putLong("tribal_island_id", tribeIslandID);
            response.putUtfString("tribe_name", results.getSFSObject(0).getUtfString("name"));
            response.putBool("success", true);
         } else {
            response.putBool("success", false);
         }
      } catch (Exception var16) {
         Logger.trace(var16);
      }

      this.send("gs_get_tribal_island_data", response, sender);
   }

   private void getRankedIslandData(User sender, ISFSObject params) {
      int rankIndex = params.getInt("weekly_rank") - 1;
      boolean composer = false;
      if (params.containsKey("composer")) {
         composer = params.getBool("composer");
      }

      SFSObject response = new SFSObject();

      try {
         this.loadTopStages(composer);
         SFSArray topStages;
         if (!composer) {
            topStages = this.ext.topStages;
         } else {
            topStages = this.ext.topComposerStages;
         }

         if (topStages != null && topStages.size() > 0) {
            if (rankIndex < 0 || rankIndex >= topStages.size()) {
               rankIndex = 0;
            }

            Player rankedPlayer = this.ext.createPlayerByID(topStages.getSFSObject(rankIndex).getLong("user"), false, new VersionInfo((String)sender.getProperty("client_version")), (String)null, (String)null);
            if (rankedPlayer == null) {
               this.ext.sendVersionError(sender, true);
               return;
            }

            if (rankedPlayer.isBanned()) {
               this.ext.sendFriendBannedError(sender, rankedPlayer);
               return;
            }

            long rankedIslandId = topStages.getSFSObject(rankIndex).getLong("user_island_id");
            boolean islandRated = this.hasRatedIsland(((Player)sender.getProperty("player_object")).getPlayerId(), rankedIslandId);
            response.putSFSObject("friend_object", rankedPlayer.toSFSObject());
            Vector<SFSObject> removeEntries = new Vector();
            String sql = "SELECT island_id, user_structure FROM user_torch_gifts WHERE recipient_bbbid=? AND collected=0";
            Object[] args = new Object[]{rankedPlayer.getBbbId()};
            SFSArray result = this.ext.getDB().query(sql, args);

            int i;
            SFSObject s;
            Long islandId;
            Long userStructureId;
            for(i = 0; i < result.size(); ++i) {
               s = (SFSObject)result.getElementAt(i);
               islandId = s.getLong("island_id");
               userStructureId = s.getLong("user_structure");
               PlayerStructure curStructure = rankedPlayer.getIslandByID(islandId).getStructureByID(userStructureId);
               if (curStructure == null) {
                  removeEntries.add(s);
               } else if (!curStructure.isTorch()) {
                  removeEntries.add(s);
               }
            }

            if (removeEntries.size() > 0) {
               for(i = 0; i < removeEntries.size(); ++i) {
                  s = (SFSObject)removeEntries.elementAt(i);
                  islandId = s.getLong("island_id");
                  userStructureId = s.getLong("user_structure");
                  sql = "DELETE FROM user_torch_gifts WHERE recipient_bbbid=? AND island_id=? AND user_structure=?";
                  args = new Object[]{rankedPlayer.getBbbId(), islandId, userStructureId};
                  this.ext.getDB().update(sql, args);
               }

               sql = "SELECT island_id, user_structure FROM user_torch_gifts WHERE recipient_bbbid=? AND collected=0";
               args = new Object[]{rankedPlayer.getBbbId()};
               result = this.ext.getDB().query(sql, args);
            }

            if (result.size() > 0) {
               response.putSFSArray("torch_gifts", result);
            }

            response.putLong("ranked_island_id", rankedIslandId);
            response.putBool("island_rated", islandRated);
            response.putInt("weekly_rank", rankIndex + 1);
            response.putLong("num_ranked_islands", (long)topStages.size());
            response.putBool("success", true);
         } else {
            response.putUtfString("message", "No ranked islands found");
            response.putLong("num_ranked_islands", 0L);
            response.putBool("success", false);
         }
      } catch (Exception var20) {
         Logger.trace(var20);
      }

      this.serverQuestEvent(sender, "top_island_visited", 1);
      this.send("gs_get_ranked_island_data", response, sender);
   }

   private void loadTopStages(boolean composer) {
      try {
         long refresh;
         String weeklyWinnersTable;
         String weeklyTotalTable;
         if (!composer) {
            refresh = this.ext.topStagesRefresh;
            weeklyWinnersTable = "user_island_weekly_winners";
            weeklyTotalTable = "user_island_weekly_total_likes";
         } else {
            refresh = this.ext.topComposerStagesRefresh;
            weeklyWinnersTable = "user_island_weekly_composer_winners";
            weeklyTotalTable = "user_island_weekly_composer_total_likes";
         }

         if (MSMExtension.CurrentDBTime() > refresh + (long)GameSettings.get("TOP_ISLAND_REFRESH_TIME_SEC", 3600) * 1000L) {
            Logger.trace("reloading top stages cache!");
            String sql = "SELECT winning_users FROM " + weeklyWinnersTable + " ORDER BY date DESC LIMIT 4";
            SFSArray result = this.ext.getDB().query(sql);
            String previousWinners = "";

            int i;
            ISFSObject obj;
            for(i = 0; i < result.size(); ++i) {
               obj = result.getSFSObject(i);
               String winners = obj.getUtfString("winning_users");
               if (winners != null) {
                  JSONArray ja = new JSONArray(winners);

                  for(int j = 0; j < ja.length(); ++j) {
                     if (!ja.isNull(j)) {
                        if (!previousWinners.isEmpty()) {
                           previousWinners = previousWinners + ",";
                        }

                        previousWinners = previousWinners + ja.getInt(j);
                     }
                  }
               }
            }

            sql = "SELECT user_id FROM users WHERE bbb_id IN (SELECT bbb_id FROM user_banned)";
            result = this.ext.getDB().query(sql);

            for(i = 0; i < result.size(); ++i) {
               obj = result.getSFSObject(i);
               if (!previousWinners.isEmpty()) {
                  previousWinners = previousWinners + ",";
               }

               previousWinners = previousWinners + obj.getInt("user_id");
            }

            if (!composer) {
               this.ext.previousTopStageWinners = previousWinners;
            } else {
               this.ext.previousTopComposerStageWinners = previousWinners;
            }

            if (!previousWinners.isEmpty()) {
               previousWinners = " AND user NOT IN (" + previousWinners + ")";
            }

            if (!composer) {
               this.ext.topStagesRefresh = MSMExtension.CurrentDBTime();
            } else {
               this.ext.topComposerStagesRefresh = MSMExtension.CurrentDBTime();
            }

            sql = "SELECT * FROM (SELECT user, user_island_id, likes, dislikes FROM " + weeklyTotalTable + " WHERE likes > dislikes" + previousWinners + " ORDER BY (likes - dislikes) DESC LIMIT 5000) AS `t1` GROUP BY user ORDER BY (likes - dislikes) DESC LIMIT 1000";
            if (!composer) {
               this.ext.topStages = this.ext.getDB().query(sql);
            } else {
               this.ext.topComposerStages = this.ext.getDB().query(sql);
            }
         }
      } catch (Exception var14) {
         Logger.trace(var14);
      }

   }

   private void getIslandRank(User sender, ISFSObject params) {
      try {
         boolean composer = false;
         if (params.containsKey("composer")) {
            composer = params.getBool("composer");
         }

         String previousTopStageWinners;
         String table;
         SFSArray topStages;
         if (!composer) {
            topStages = this.ext.topStages;
            previousTopStageWinners = this.ext.previousTopStageWinners;
            table = "user_island_weekly_total_likes";
         } else {
            topStages = this.ext.topComposerStages;
            previousTopStageWinners = this.ext.previousTopComposerStageWinners;
            table = "user_island_weekly_composer_total_likes";
         }

         this.loadTopStages(composer);
         SFSObject response;
         if (topStages != null && topStages.size() > 0 && params.containsKey("island_id")) {
            response = new SFSObject();
            String previousWinners = "";
            if (!previousTopStageWinners.isEmpty()) {
               previousWinners = " AND user NOT IN (" + previousTopStageWinners + ")";
            }

            String sql = "SELECT * FROM " + table + " WHERE likes > dislikes AND user=?" + previousWinners + " ORDER BY (likes - dislikes) DESC LIMIT 1";
            Player player = (Player)sender.getProperty("player_object");
            Object[] args = new Object[]{player.getPlayerId()};
            SFSArray result = this.ext.getDB().query(sql, args);
            if (result.size() == 0 || result.getSFSObject(0).getLong("user_island_id") != params.getLong("island_id")) {
               response = new SFSObject();
               response.putBool("success", false);
               this.send("gs_get_island_rank", response, sender);
               return;
            }

            int score = result.getSFSObject(0).getInt("likes") - result.getSFSObject(0).getInt("dislikes");
            if (score > 0 && topStages.size() != 0) {
               int rank = 0;
               int[] intervals = new int[]{10, 100, 500, 1000};

               for(int i = 0; i < intervals.length; ++i) {
                  if (topStages.size() < intervals[i]) {
                     if (score >= topStages.getSFSObject(topStages.size() - 1).getInt("likes") - topStages.getSFSObject(topStages.size() - 1).getInt("dislikes")) {
                        rank = intervals[i];
                        break;
                     }
                  } else if (score >= topStages.getSFSObject(intervals[i] - 1).getInt("likes") - topStages.getSFSObject(intervals[i] - 1).getInt("dislikes")) {
                     rank = intervals[i];
                     break;
                  }
               }

               response.putInt("rank", rank);
            } else {
               response.putInt("rank", 0);
            }

            response.putLong("island_id", params.getLong("island_id"));
            response.putBool("success", true);
            this.send("gs_get_island_rank", response, sender);
         } else {
            response = new SFSObject();
            response.putBool("success", false);
            this.send("gs_get_island_rank", response, sender);
         }
      } catch (Exception var17) {
         Logger.trace(var17);
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         this.send("gs_get_island_rank", response, sender);
      }

   }

   private boolean hasRatedIsland(long userId, long userIslandId) {
      String sql = "SELECT id FROM user_island_weekly_user_likes WHERE user=? AND user_island_id=? LIMIT 1";
      Object[] args = new Object[]{userId, userIslandId};

      try {
         return this.ext.getDB().exists(sql, args);
      } catch (Exception var8) {
         Logger.trace(var8);
         return false;
      }
   }

   private void rateIsland(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      long userId = player.getPlayerId();
      long friendId = params.getLong("friend_id");
      long friendIslandId = params.getLong("friend_island_id");
      boolean liked = params.getBool("liked");
      int islandType = 0;
      if (params.containsKey("island_type")) {
         islandType = params.getInt("island_type");
      }

      long insertId = -1L;
      ISFSObject response = new SFSObject();
      String sql = "INSERT IGNORE INTO user_island_weekly_user_likes SET user_island_id=?, user=?, liked=?, island_type=?, date_rated=NOW()";
      Object[] args = new Object[]{friendIslandId, userId, liked ? 1 : 0, islandType};

      try {
         insertId = (long)((int)this.ext.getDB().insertGetId(sql, args));
      } catch (Exception var21) {
         Logger.trace(var21, "Error inserting user island rating.");
      }

      if (insertId != -1L) {
         if (liked) {
            sql = "UPDATE user_islands SET likes=likes+1 WHERE user_island_id=?";
         } else {
            sql = "UPDATE user_islands SET dislikes=dislikes+1 WHERE user_island_id=?";
         }

         args = new Object[]{friendIslandId};

         try {
            this.ext.getDB().update(sql, args);
         } catch (Exception var20) {
            Logger.trace(var20, "Error updating island rating.");
         }

         String weeklyTotalLikesTable;
         if (islandType != 11) {
            weeklyTotalLikesTable = "user_island_weekly_total_likes";
         } else {
            weeklyTotalLikesTable = "user_island_weekly_composer_total_likes";
         }

         if (liked) {
            sql = "INSERT INTO " + weeklyTotalLikesTable + " (user_island_id, user, likes, dislikes) VALUES (?, ?, 1, 0)ON DUPLICATE KEY UPDATE likes=likes+1";
         } else {
            sql = "INSERT INTO " + weeklyTotalLikesTable + " (user_island_id, user, likes, dislikes) VALUES (?, ?, 0, 1)ON DUPLICATE KEY UPDATE dislikes=dislikes+1";
         }

         args = new Object[]{friendIslandId, friendId};

         try {
            this.ext.getDB().update(sql, args);
         } catch (Exception var19) {
            Logger.trace(var19, "Error updating weekly island likes.");
         }

         response.putBool("success", true);
      } else {
         response.putBool("success", false);
      }

      this.send("gs_rate_island", response, sender);
   }

   private void reportUser(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      long userId = player.getPlayerId();
      long reportedId = params.getLong("reported_id");
      long reportedIslandId = params.getLong("reported_island_id");
      String reportReason = params.getUtfString("reason");
      String[] REASONS = new String[]{"DISPLAY_NAME", "TRIBAL_NAME", "COMPOSER_NAME", "ISLAND"};
      if (reportReason.equals("DISPLAY_NAME")) {
         reportedIslandId = 0L;
      }

      boolean success = false;

      try {
         if (!ArrayUtils.contains(REASONS, reportReason)) {
            throw new Exception(String.format("User %d sent invalid report reason '%s'", userId, reportReason));
         }

         if (!player.canReportUser(reportedId, reportReason, reportedIslandId)) {
            success = true;
            throw new Exception(String.format("User %d cannot report user %d for island %d reason '%s' right now", userId, reportedId, reportedIslandId, reportReason));
         }

         String GET_USER_REPORT_SQL = "SELECT data FROM user_snitches WHERE user_id = ? AND reason = ? AND island = ? AND reported_on > DATE_SUB(NOW(), INTERVAL ? SECOND) ORDER BY id DESC LIMIT 1";
         ISFSArray results = this.ext.getDB().query("SELECT data FROM user_snitches WHERE user_id = ? AND reason = ? AND island = ? AND reported_on > DATE_SUB(NOW(), INTERVAL ? SECOND) ORDER BY id DESC LIMIT 1", new Object[]{reportedId, reportReason, reportedIslandId, GameSettings.getLong("REPORT_SAVE_TIME")});
         long dataId = 0L;
         String INSERT_USER_REPORT_SQL;
         if (results.size() == 0) {
            INSERT_USER_REPORT_SQL = "INSERT INTO user_snitch_data (user_id, reason, island, data) VALUES (?, ?, ?, ?)";
            String userData = this.getUserReportData(reportedId, reportedIslandId, reportReason);
            dataId = this.ext.getDB().insertGetIdNoBruno("INSERT INTO user_snitch_data (user_id, reason, island, data) VALUES (?, ?, ?, ?)", new Object[]{reportedId, reportReason, reportedIslandId, userData});
         } else {
            dataId = results.getSFSObject(0).getLong("data");
         }

         INSERT_USER_REPORT_SQL = "INSERT INTO user_snitches (user_id, from_user_id, reason, island, data) VALUES (?, ?, ?, ?, ?)";
         this.ext.getDB().insertGetIdNoBruno("INSERT INTO user_snitches (user_id, from_user_id, reason, island, data) VALUES (?, ?, ?, ?, ?)", new Object[]{reportedId, userId, reportReason, reportedIslandId, dataId});
         player.reportUser(reportedId, reportReason, reportedIslandId);
         this.ext.stats.trackSnitch(sender, reportedId, reportedIslandId, reportReason);
         success = true;
      } catch (Exception var19) {
         Logger.trace(var19);
      }

      ISFSObject response = new SFSObject();
      response.putBool("success", success);
      this.send("gs_report_user", response, sender);
   }

   private String getUserReportData(long userId, long userIslandId, String reportReason) throws Exception {
      String GET_USER_ISLAND_SQL;
      SFSArray results;
      if (reportReason.equals("DISPLAY_NAME")) {
         GET_USER_ISLAND_SQL = "SELECT display_name FROM users WHERE user_id = ?";
         results = this.ext.getDB().query("SELECT display_name FROM users WHERE user_id = ?", new Object[]{userId});
         return String.format("{\"display_name\":\"%s\"}", results.getSFSObject(0).getUtfString("display_name"));
      } else if (reportReason.equals("TRIBAL_NAME")) {
         GET_USER_ISLAND_SQL = "SELECT user_tribal_islands.name, user_islands.tribal_id\tFROM user_tribal_islands  LEFT JOIN user_islands ON user_tribal_islands.user_island_id = user_islands.tribal_id WHERE user_islands.user_island_id = ?";
         results = this.ext.getDB().query("SELECT user_tribal_islands.name, user_islands.tribal_id\tFROM user_tribal_islands  LEFT JOIN user_islands ON user_tribal_islands.user_island_id = user_islands.tribal_id WHERE user_islands.user_island_id = ?", new Object[]{userIslandId});
         return String.format("{\"tribal_name\":\"%s\",\"tribal_id\":%d}", results.getSFSObject(0).getUtfString("name"), results.getSFSObject(0).getLong("tribal_id"));
      } else if (reportReason.equals("COMPOSER_NAME")) {
         GET_USER_ISLAND_SQL = "SELECT name FROM user_islands WHERE user = ? AND user_island_id = ?";
         results = this.ext.getDB().query("SELECT name FROM user_islands WHERE user = ? AND user_island_id = ?", new Object[]{userId, userIslandId});
         return String.format("{\"composer_name\":\"%s\"}", results.getSFSObject(0).getUtfString("name"));
      } else if (reportReason.equals("ISLAND")) {
         GET_USER_ISLAND_SQL = "SELECT user_islands.island, user_islands.tribal_id, structures, monsters FROM user_islands LEFT JOIN user_island_data ON user_islands.user_island_id = user_island_data.island WHERE user_islands.user = ? AND user_islands.user_island_id = ?";
         results = this.ext.getDB().query("SELECT user_islands.island, user_islands.tribal_id, structures, monsters FROM user_islands LEFT JOIN user_island_data ON user_islands.user_island_id = user_island_data.island WHERE user_islands.user = ? AND user_islands.user_island_id = ?", new Object[]{userId, userIslandId});
         if (results.getSFSObject(0).getInt("island") == 9) {
            results.getSFSObject(0).putUtfString("structures", "[]");
            results.getSFSObject(0).putUtfString("monsters", "[]");
         }

         return String.format("{\"structures\":\"%s\",\"monsters\":\"%s\"%s}", results.getSFSObject(0).getUtfString("structures"), results.getSFSObject(0).getUtfString("monsters"), results.getSFSObject(0).containsKey("tribal_id") ? ",\"tribal_id\":" + results.getSFSObject(0).getLong("tribal_id") : "");
      } else {
         throw new Exception("Invalid report reason: " + reportReason);
      }
   }

   private void loadTopTribes() {
      try {
         if (MSMExtension.CurrentDBTime() > this.ext.topTribesRefresh + 3600000L) {
            Logger.trace("reloading top tribes cache!");
            this.ext.topTribesRefresh = MSMExtension.CurrentDBTime();
            String sql = "SELECT user_tribal_islands.user_island_id, user_tribal_islands.chief, user_tribal_islands.name, user_tribal_islands.rank, user_tribal_islands.members, user_tribal_monsters.user_monster_id, user_tribal_monsters.monster FROM user_tribal_islands LEFT JOIN user_tribal_monsters ON user_tribal_monsters.user_monster_id=user_tribal_islands.chief WHERE user_tribal_islands.chief NOT IN (SELECT user_id FROM users WHERE bbb_id IN (SELECT bbb_id FROM user_banned)) ORDER BY rank DESC LIMIT 30";
            this.ext.topTribes = this.ext.getDB().query(sql);
         }
      } catch (Exception var2) {
         Logger.trace(var2);
      }

   }

   private ISFSArray getFriendData(ISFSArray friendDataArr) {
      try {
         MSMExtension ext = MSMExtension.getInstance();
         if (friendDataArr != null && friendDataArr.size() > 0) {
            HashMap<Long, ISFSObject> bbbIdToFriendAuthData = new HashMap();

            for(int i = 0; i < friendDataArr.size(); ++i) {
               Long friendBbbId = friendDataArr.getSFSObject(i).getLong("bbb_id");
               bbbIdToFriendAuthData.put(friendBbbId, friendDataArr.getSFSObject(i));
            }

            StringBuilder friendBbbIdBuilder = new StringBuilder();

            int maxTorchFriends;
            for(maxTorchFriends = 0; maxTorchFriends < friendDataArr.size(); ++maxTorchFriends) {
               Long friendBbbId = friendDataArr.getSFSObject(maxTorchFriends).getLong("bbb_id");
               friendBbbIdBuilder.append(friendBbbId);
               if (maxTorchFriends + 1 < friendDataArr.size()) {
                  friendBbbIdBuilder.append(",");
               }
            }

            maxTorchFriends = GameSettings.get("MAX_TORCH_FRIENDS", -1);
            int torchFriends = 0;
            String sql = "SELECT users.user_id, bbb_id, level, display_name, tribe,  user_avatar.pp_type, user_avatar.pp_info FROM users LEFT OUTER JOIN user_tribal_mappings ON user_id=user LEFT OUTER JOIN user_avatar on users.user_id = user_avatar.user_id WHERE bbb_id IN (" + friendBbbIdBuilder.toString() + ")";
            SFSArray userResult = ext.getDB().query(sql);
            if (userResult != null && userResult.size() > 0) {
               ISFSArray returnArray = new SFSArray();

               int i;
               ISFSObject userdata;
               ISFSObject friendDatFromAuth;
               boolean fav;
               boolean hasTorches;
               for(i = 0; i < userResult.size(); ++i) {
                  userdata = userResult.getSFSObject(i);
                  friendDatFromAuth = (ISFSObject)bbbIdToFriendAuthData.get(userdata.getLong("bbb_id"));
                  fav = friendDatFromAuth.getBool("is_fav");
                  if (fav) {
                     hasTorches = false;
                     if (torchFriends < maxTorchFriends || maxTorchFriends == -1) {
                        hasTorches = Player.friendHasUnlitTorches(ext.getDB(), userdata.getLong("bbb_id"), userdata.getInt("user_id"));
                        ++torchFriends;
                     }

                     userdata.putBool("has_unlit_torches", hasTorches);
                     userdata.putBool("is_favorite", fav);
                     this.wtfTransferFriendData(friendDatFromAuth, userdata);
                     returnArray.addSFSObject(userdata);
                  }
               }

               for(i = 0; i < userResult.size(); ++i) {
                  userdata = userResult.getSFSObject(i);
                  friendDatFromAuth = (ISFSObject)bbbIdToFriendAuthData.get(userdata.getLong("bbb_id"));
                  fav = friendDatFromAuth.getBool("is_fav");
                  if (!fav) {
                     hasTorches = false;
                     if (torchFriends < maxTorchFriends || maxTorchFriends == -1) {
                        hasTorches = Player.friendHasUnlitTorches(ext.getDB(), userdata.getLong("bbb_id"), userdata.getInt("user_id"));
                        ++torchFriends;
                     }

                     userdata.putBool("has_unlit_torches", hasTorches);
                     userdata.putBool("is_favorite", fav);
                     this.wtfTransferFriendData(friendDatFromAuth, userdata);
                     returnArray.addSFSObject(userdata);
                  }
               }

               return returnArray;
            }
         }
      } catch (Exception var15) {
         Logger.trace(var15, "Error getting friends data from DB");
      }

      return null;
   }

   private void wtfTransferFriendData(ISFSObject friendDatFromAuth, ISFSObject userdata) {
      userdata.putInt("litByMe", friendDatFromAuth.getInt("litByMe"));
      userdata.putInt("litByFriend", friendDatFromAuth.getInt("litByFriend"));
      if (friendDatFromAuth.containsKey("battle_level")) {
         userdata.putInt("battle_level", friendDatFromAuth.getInt("battle_level"));
      }

      if (friendDatFromAuth.containsKey("wonBattles")) {
         userdata.putInt("wonBattles", friendDatFromAuth.getInt("wonBattles"));
      }

      if (friendDatFromAuth.containsKey("lostBattles")) {
         userdata.putInt("lostBattles", friendDatFromAuth.getInt("lostBattles"));
      }

      if (friendDatFromAuth.containsKey("tier")) {
         userdata.putInt("tier", friendDatFromAuth.getInt("tier"));
      }

      if (friendDatFromAuth.containsKey("rank")) {
         userdata.putLong("rank", friendDatFromAuth.getLong("rank"));
      }

      if (friendDatFromAuth.containsKey("prev_tier")) {
         userdata.putInt("prev_tier", friendDatFromAuth.getInt("prev_tier"));
      }

      if (friendDatFromAuth.containsKey("prev_rank")) {
         userdata.putLong("prev_rank", friendDatFromAuth.getLong("prev_rank"));
      }

      if (friendDatFromAuth.containsKey("canPvp")) {
         userdata.putInt("canPvp", friendDatFromAuth.getInt("canPvp"));
      }

      userdata.putUtfString("display_name", Helpers.sanitizeName(userdata.getUtfString("display_name"), " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!¨\"#$&'()*+,-./:;<=>?@{}0123456789|£©¿®`~^ÀÁÂÄÇÈÉÊËÌÍÎÏÑÒÓÔÖÙÚÛÜßàáâäçèéêëìíîïñòóôöùúûü_ÆæÃãÕõАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя€₽¡"));
      boolean isFb = friendDatFromAuth.getBool("fb");
      String fbid = isFb ? friendDatFromAuth.getUtfString("fbid") : "";
      if (!fbid.isEmpty()) {
         userdata.putUtfString("fbid", fbid);
      }

   }

   private SFSArray loadFriendsWithFavs(Player player, ISFSArray seasonResults) {
      Long bbbId = player.getBbbId();
      SFSArray friendsToReturn = new SFSArray();

      try {
         BattleCampaignData curPvpSeason = BattleCampaignLookup.getCurrentPvpSeason();
         long pvpSeasonStart = 0L;
         if (curPvpSeason != null) {
            Schedule s = ScheduleLookup.schedule(curPvpSeason.getScheduleID());
            pvpSeasonStart = s.getCurrentRepeatStartTime();
         }

         String SELECT_FRIENDS_SQL = "SELECT *, user_battle_friends.won_battles AS won_battles, user_battle_friends.lost_battles AS lost_battles FROM user_friends LEFT JOIN user_battle_friends ON user_friends.user_1=user_battle_friends.user_1 AND user_friends.user_2=user_battle_friends.user_2 WHERE user_friends.user_1=? OR user_friends.user_2=? LIMIT ?";
         SFSArray result = this.ext.getDB().query("SELECT *, user_battle_friends.won_battles AS won_battles, user_battle_friends.lost_battles AS lost_battles FROM user_friends LEFT JOIN user_battle_friends ON user_friends.user_1=user_battle_friends.user_1 AND user_friends.user_2=user_battle_friends.user_2 WHERE user_friends.user_1=? OR user_friends.user_2=? LIMIT ?", new Object[]{bbbId, bbbId, GameSettings.get("FRIEND_MAX_LOAD", 1000)});

         for(int i = 0; i < result.size(); ++i) {
            ISFSObject obj = result.getSFSObject(i);
            boolean imUser1 = obj.getLong("user_1").equals(bbbId);
            Integer isFavInt = imUser1 ? obj.getInt("user_1_fav") : obj.getInt("user_2_fav");
            if (isFavInt == null) {
               isFavInt = 0;
            }

            ISFSObject friendData = new SFSObject();
            friendData.putLong("bbb_id", imUser1 ? obj.getLong("user_2") : obj.getLong("user_1"));
            friendData.putBool("is_fav", isFavInt > 0);
            friendData.putInt("litByMe", imUser1 ? obj.getInt("user_2_torches_lit") : obj.getInt("user_1_torches_lit"));
            friendData.putInt("litByFriend", imUser1 ? obj.getInt("user_1_torches_lit") : obj.getInt("user_2_torches_lit"));
            String SELECT_USERS_SQL = "SELECT *, user_avatar.pp_type, user_avatar.pp_info FROM users LEFT JOIN user_avatar on users.user_id = user_avatar.user_id WHERE bbb_id=?";
            SFSArray userResult = this.ext.getDB().query("SELECT *, user_avatar.pp_type, user_avatar.pp_info FROM users LEFT JOIN user_avatar on users.user_id = user_avatar.user_id WHERE bbb_id=?", new Object[]{friendData.getLong("bbb_id")});
            Integer wonBattles = obj.getInt("won_battles");
            if (userResult.size() > 0) {
               Integer friendUserId = userResult.getSFSObject(0).getInt("user_id");
               String SELECT_BATTLE_SQL = "SELECT level, loadout_versus FROM user_battle WHERE user_id = ?";
               SFSArray battleResult = this.ext.getDB().query("SELECT level, loadout_versus FROM user_battle WHERE user_id = ?", new Object[]{friendUserId});
               if (battleResult.size() > 0) {
                  friendData.putInt("battle_level", battleResult.getSFSObject(0).getInt("level"));
                  String loadoutVersusStr = battleResult.getSFSObject(0).getUtfString("loadout_versus");
                  int canPvp = 0;
                  int prevTier;
                  if (loadoutVersusStr != null && !loadoutVersusStr.isEmpty()) {
                     ISFSObject loadoutVersus = SFSObject.newFromJsonData(loadoutVersusStr);

                     for(prevTier = 0; prevTier < BattleLoadout.SlotKeys.length; ++prevTier) {
                        if (loadoutVersus.containsKey(BattleLoadout.SlotKeys[prevTier]) && SFSHelpers.getLong(BattleLoadout.SlotKeys[prevTier], loadoutVersus) != 0L) {
                           canPvp = 1;
                           break;
                        }
                     }
                  } else {
                     canPvp = 0;
                  }

                  friendData.putInt("canPvp", canPvp);
                  int curTier = -1;
                  prevTier = -1;
                  long curRank = 0L;
                  long prevRank = 0L;
                  if (canPvp == 1) {
                     for(int curSeasonInd = 0; curSeasonInd < seasonResults.size(); ++curSeasonInd) {
                        int seasonTier = -1;
                        long seasonRank = 0L;
                        ISFSObject campaign = seasonResults.getSFSObject(curSeasonInd);
                        int campaignId = campaign.getInt("campaign_id");
                        long scheduleStart = campaign.getLong("schedule_started_on");
                        String SELECT_PVP_SQL = "SELECT tier, completed_on FROM user_battle_pvp WHERE campaign_id = ? AND schedule_started_on = FROM_UNIXTIME(?) AND user_id=?";
                        SFSArray results = this.ext.getDB().query("SELECT tier, completed_on FROM user_battle_pvp WHERE campaign_id = ? AND schedule_started_on = FROM_UNIXTIME(?) AND user_id=?", new Object[]{campaignId, scheduleStart / 1000L, friendUserId});
                        if (results.size() > 0) {
                           ISFSObject friendPvpStatus = results.getSFSObject(0);
                           seasonTier = friendPvpStatus.getInt("tier");
                           if (friendPvpStatus.containsKey("completed_on") && friendPvpStatus.getLong("completed_on") > 0L) {
                              BattleVersusChampionRanks.RankedUser ru = BattleVersusChampionRanks.getRankedUser(campaignId, scheduleStart, (long)friendUserId);
                              if (ru != null) {
                                 seasonRank = ru.rank();
                              } else {
                                 seasonRank = (long)(BattleVersusChampionRanks.getRankTable(campaignId, scheduleStart).rankedList.size() + 1);
                              }
                           }
                        }

                        if (curSeasonInd == 0 && scheduleStart == pvpSeasonStart) {
                           curTier = seasonTier;
                           curRank = seasonRank;
                        } else {
                           prevTier = seasonTier;
                           prevRank = seasonRank;
                        }
                     }
                  }

                  friendData.putInt("tier", curTier);
                  friendData.putLong("rank", curRank);
                  friendData.putInt("prev_tier", prevTier);
                  friendData.putLong("prev_rank", prevRank);
                  if (wonBattles == null && player.getBattleState() != null) {
                     String SELECT_BATTLE_FRIENDS_SQL = "SELECT * FROM user_battle_friends WHERE user_1 = ? AND user_2=?";
                     SFSArray sanity = this.ext.getDB().query("SELECT * FROM user_battle_friends WHERE user_1 = ? AND user_2=?", new Object[]{obj.getLong("user_2"), obj.getLong("user_1")});
                     if (sanity.size() > 0) {
                        throw new Exception("user_battle_friends key order doesn't match user_friends for : " + obj.getLong("user_1") + ", " + obj.getLong("user_2"));
                     }

                     String INSERT_BATTLE_FRIENDS_SQL = "INSERT IGNORE INTO user_battle_friends (user_1, user_2, won_battles, lost_battles) VALUES (?, ?, 0, 0)";
                     this.ext.getDB().update("INSERT IGNORE INTO user_battle_friends (user_1, user_2, won_battles, lost_battles) VALUES (?, ?, 0, 0)", new Object[]{obj.getLong("user_1"), obj.getLong("user_2")});
                  }
               }

               wonBattles = wonBattles != null ? wonBattles : 0;
               Integer lostBattles = obj.getInt("lost_battles");
               lostBattles = lostBattles != null ? lostBattles : 0;
               friendData.putInt("wonBattles", imUser1 ? wonBattles : lostBattles);
               friendData.putInt("lostBattles", imUser1 ? lostBattles : wonBattles);
            }

            friendData.putBool("fb", false);
            friendsToReturn.addSFSObject(friendData);
         }

         if (friendsToReturn.size() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(friendsToReturn.getSFSObject(0).getLong("bbb_id"));

            for(int x = 1; x < friendsToReturn.size(); ++x) {
               builder.append(",");
               builder.append(friendsToReturn.getSFSObject(x).getLong("bbb_id"));
            }

            String SELECT_SOCIAL_SQL = "SELECT bbb_id, username FROM user_social WHERE bbb_id IN (" + builder.toString() + ") AND login_type='fb'";
            result = this.ext.getDB().query(SELECT_SOCIAL_SQL);

            for(int x = 0; x < result.size(); ++x) {
               ISFSObject fbFriend = result.getSFSObject(x);

               for(int f = 0; f < friendsToReturn.size(); ++f) {
                  ISFSObject friend = friendsToReturn.getSFSObject(f);
                  if (friend.getLong("bbb_id").equals(fbFriend.getLong("bbb_id"))) {
                     friend.putBool("fb", true);
                     friend.putUtfString("fbid", fbFriend.getUtfString("username"));
                  }
               }
            }
         }
      } catch (Exception var41) {
         Logger.trace(var41, "Exception loading Friends With Favs");
      }

      return friendsToReturn;
   }

   private void getMessages(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      String sql = "SELECT * FROM user_messages WHERE user=?";
      Object[] args = new Object[]{player.getPlayerId()};
      SFSArray messages = new SFSArray();

      try {
         messages = this.ext.getDB().query(sql, args);
      } catch (Exception var8) {
         Logger.trace("exception getting messages");
      }

      ISFSObject response = new SFSObject();
      if (messages.size() == 0) {
         response.putBool("success", false);
      } else {
         response.putBool("success", true);
         response.putSFSArray("messages", messages);
      }

      this.send("gs_get_messages", response, sender);
   }

   private void deleteMessage(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      long messageId = params.getLong("message");
      if (messageId != -1L) {
         String sql = "DELETE FROM user_messages WHERE message_id=? AND user=?";
         Object[] args = new Object[]{messageId, player.getPlayerId()};

         try {
            this.ext.getDB().update(sql, args);
         } catch (Exception var9) {
            Logger.trace("Exception removing message " + messageId);
         }

      }
   }

   private boolean canDoReferral(Player player, Long referralId, boolean dummyReferralId) {
      if (player.getData().getLong("referral") == null && player.getLevel() >= 4 && referralId != player.getBbbId() && referralId != 0L) {
         try {
            String sql = "SELECT bbb_id FROM users WHERE bbb_id=?";
            if (dummyReferralId || this.ext.getDB().exists(sql, new Object[]{referralId})) {
               sql = "SELECT referral FROM users WHERE bbb_id=? AND referral IS NOT NULL";
               if (!this.ext.getDB().exists(sql, new Object[]{player.getBbbId()})) {
                  return true;
               }
            }
         } catch (Exception var5) {
            Logger.trace(String.format("EXCEPTION: Unable to determine if player '%d' can do referral for bbb id '%d'", player.getBbbId(), referralId));
         }
      }

      return false;
   }

   private void doReferralTracking(User sender, Player player, Long referralId, int referralReward, boolean dummyReferralId) throws Exception {
      String sql = "UPDATE users SET referral=? WHERE bbb_id=?";
      this.ext.getDB().update(sql, new Object[]{referralId, player.getBbbId()});
      if (!dummyReferralId) {
         sql = "INSERT INTO user_player_referrals SET bbb_id=?, from_bbb_id=?, resource='diamonds', amount=?";
         this.ext.getDB().update(sql, new Object[]{referralId, player.getBbbId(), referralReward});
      }

      this.ext.stats.trackPlayerReferral(sender, referralId);
      Firebase.reportPlayerReferral(sender, referralId);
   }

   private void doReferralReward(User sender, Player player, int referralReward) {
      player.adjustDiamonds(sender, this, referralReward);
      this.ext.stats.trackReward(sender, "player_referral_added", "diamonds", (long)referralReward);
   }

   private void addBbbIdReferral(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      Long referralId = params.getLong("referring_bbb_id");
      SFSObject response = new SFSObject();

      try {
         if (this.canDoReferral(player, referralId, false)) {
            int referralReward = GameSettings.getInt("PLAYER_REFERRAL_REWARD");
            this.doReferralTracking(sender, player, referralId, referralReward, false);
            this.doReferralReward(sender, player, referralReward);
            response.putBool("success", true);
            response.putLong("referring_bbb_id", referralId);
            ISFSArray responseVars = new SFSArray();
            player.addPlayerPropertyData(responseVars, false);
            response.putSFSArray("properties", responseVars);
         } else {
            response.putBool("success", false);
         }
      } catch (Exception var8) {
         Logger.trace(String.format("EXCEPTION: Unable to process referral by player '%d' for friend bbb id '%d'", player.getBbbId(), referralId));
         response.putBool("success", false);
      }

      this.send("gs_referral_request", response, sender);
   }

   private void completeOffer(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      String wall = params.getUtfString("offer");
      int amount = params.getInt("amount");
      SFSObject response;
      if (GameSettings.get("CLIENT_OFFERS_ALLOWED", 0) == 1) {
         player.adjustDiamonds(sender, this, amount);
         MSMExtension.getInstance().stats.trackOffer(sender, "non_validated_offer", "diamonds", amount, "");
         response = new SFSObject();
         response.putBool("success", true);
         response.putUtfString("offer", wall);
         response.putInt("amount", amount);
         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("gs_offer_completed", response, sender);
      } else {
         Logger.trace(String.format("Player %d attempting to claim client side offer '%s' for %d on non-accepting server", player.getBbbId(), wall, amount));
         response = new SFSObject();
         response.putBool("success", false);
         this.send("gs_offer_completed", response, sender);
      }

   }

   private void updatePaywall(User sender, ISFSObject params) {
      ISFSObject response = new SFSObject();
      response.putBool("success", false);
      this.send("gs_paywall_updated", response, sender);
   }

   private void goDeeper(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      StringBuilder deep = new StringBuilder(String.format("gs_app_link for bbb id %d: ", player.getBbbId()));
      Iterator var5 = params.getKeys().iterator();

      while(var5.hasNext()) {
         String key = (String)var5.next();
         deep.append(key + ": ");
         deep.append(params.getUtfString(key));
      }

      Logger.trace(deep.toString());
   }

   private void playerHasScratchOff(User sender, ISFSObject params) {
      String type = params.getUtfString("type");
      Player player = (Player)sender.getProperty("player_object");
      if (type == null || !type.equalsIgnoreCase("M") && !type.equalsIgnoreCase("S")) {
         this.ext.sendErrorResponse("gs_player_has_scratch_off", "invalid type for scratchoff has", sender);
      } else {
         boolean hasTicketAvailable = player.hasUnclaimedScratchOff(type) || player.getNextScratchOffTime(type) <= MSMExtension.CurrentDBTime();
         SFSObject response = new SFSObject();
         response.putBool("success", hasTicketAvailable);
         response.putUtfString("type", type);
         this.send("gs_player_has_scratch_off", response, sender);
      }
   }

   private void playScratchOff(User sender, ISFSObject params, boolean purchasing) {
      try {
         String type = params.getUtfString("type");
         Player player = (Player)sender.getProperty("player_object");
         if (type == null || !type.equalsIgnoreCase("M") && !type.equalsIgnoreCase("S")) {
            this.ext.sendErrorResponse("gs_play_scratch_off", "invalid type for scratch off play", sender);
            return;
         }

         SFSObject response = new SFSObject();
         boolean success = false;
         if (type.equalsIgnoreCase("M") && player.getActiveIsland().eggsFull()) {
            response.putUtfString("type", type);
            response.putBool("has_egg", true);
            success = false;
         } else if (!player.hasUnclaimedScratchOff(type) || type.equalsIgnoreCase("C") && !purchasing && (player.getNextScratchOffTime(type) == Long.MAX_VALUE || player.getNextScratchOffTime(type) <= MSMExtension.CurrentDBTime()) || type.equalsIgnoreCase("S") && !purchasing && (player.getNextScratchOffTime(type) == Long.MAX_VALUE || player.getNextScratchOffTime(type) <= MSMExtension.CurrentDBTime())) {
            boolean requestFree = false;
            if (params.containsKey("requestFree")) {
               requestFree = params.getBool("requestFree");
            }

            if (requestFree) {
               if (player.getNextFreeScratchAd() > MSMExtension.CurrentDBTime()) {
                  response.putBool("success", false);
                  response.putUtfString("type", type);
                  if (type.equalsIgnoreCase("M")) {
                     response.putBool("sale", ScratchTicketFunctions.monsterScratchOffTimeBetweenFreePlays() < 166);
                  }

                  this.send("gs_play_scratch_off", response, sender);
                  return;
               }

               player.claimFreeScratchWithAd();
            } else if (purchasing) {
               int diamondCost = false;
               int diamondCost;
               if (type.equalsIgnoreCase("M")) {
                  if (!TimedEventManager.instance().hasTimedEventNow(TimedEventType.Eggstravaganza, 0, 0)) {
                     diamondCost = GameSettings.getInt("USER_MONSTER_SCRATCHOFF_PRICE");
                  } else {
                     List<TimedEvent> eggstravaganzas = TimedEventManager.instance().currentActiveOnKey(TimedEventType.Eggstravaganza, 0, 0);
                     diamondCost = ((EggstravaganzaEvent)eggstravaganzas.get(0)).scratchOffPrice();
                  }
               } else {
                  diamondCost = GameSettings.getInt("USER_SCRATCHOFF_PRICE");
               }

               if (!player.canBuy(0L, 0L, (long)diamondCost, 0L, 0L, 0L, 0)) {
                  response.putBool("success", false);
                  response.putUtfString("type", type);
                  if (type.equalsIgnoreCase("M")) {
                     response.putBool("sale", ScratchTicketFunctions.monsterScratchOffTimeBetweenFreePlays() < 166);
                  }

                  this.send("gs_play_scratch_off", response, sender);
                  return;
               }

               player.chargePlayer(sender, this, 0, 0, diamondCost, 0L, 0, 0, 0);
               String eventName = type.equalsIgnoreCase("M") ? "scratchoff_mini_game_monster" : "spin_wheel";
               this.logDiamondUsage(sender, eventName, diamondCost, player.getLevel());
            } else {
               long currentTime = MSMExtension.CurrentDBTime();
               long eligibleToPlay = player.getNextScratchOffTime(type);
               if (eligibleToPlay != Long.MAX_VALUE && eligibleToPlay > currentTime) {
                  response.putBool("success", false);
                  response.putUtfString("type", type);
                  if (type.equalsIgnoreCase("M")) {
                     response.putBool("sale", ScratchTicketFunctions.monsterScratchOffTimeBetweenFreePlays() < 166);
                  }

                  this.send("gs_play_scratch_off", response, sender);
                  return;
               }
            }

            ISFSObject scratchOff = ScratchTicketFunctions.initializeScratchOff(sender, purchasing, type, false);
            if (scratchOff == null) {
               success = false;
               response.putUtfString("type", type);
            } else {
               success = true;
               response.putSFSObject("ticket", scratchOff);
               if (type.equalsIgnoreCase("C") || type.equalsIgnoreCase("S")) {
                  response.putSFSObject("scaled_prizes", ScratchTicketFunctions.getScaledPrizeAmounts(sender, type));
               }

               if (purchasing || requestFree) {
                  ISFSArray responseVars = new SFSArray();
                  player.addPlayerPropertyData(responseVars, false);
                  SFSObject prop = new SFSObject();
                  prop.putLong(player.scratchOffKey(type), player.getNextScratchOffTime(type));
                  responseVars.addSFSObject(prop);
                  prop = new SFSObject();
                  prop.putBool(player.hasScratchOffKey(type), player.hasUnclaimedScratchOff(type) || player.getNextScratchOffTime(type) <= MSMExtension.CurrentDBTime());
                  responseVars.addSFSObject(prop);
                  response.putSFSArray("properties", responseVars);
               }
            }
         } else {
            ISFSObject existingTicket = SFSObject.newFromJsonData(player.getScratchPrize(type));
            ISFSObject scratchOff = new SFSObject();
            scratchOff.putInt("id", existingTicket.getInt("id"));
            scratchOff.putUtfString("type", existingTicket.getUtfString("type") != null ? existingTicket.getUtfString("type") : "C");
            scratchOff.putInt("amount", existingTicket.getInt("amount"));
            scratchOff.putUtfString("prize", existingTicket.getUtfString("prize"));
            if (type.equalsIgnoreCase("M")) {
               int reward = scratchOff.getInt("amount");
               Island island = IslandLookup.get(player.getActiveIsland().getIndex());
               if (!island.hasMonster(reward)) {
                  Integer newMonster = ScratchTicketFunctions.getAlternativeMonsterScratch(sender, island, reward);
                  if (newMonster == null) {
                     Logger.trace("ERROR in playScratchOff(), couldn't find alternative for monster " + reward + " on island " + island.getID() + " for user " + player.getPlayerId());
                     return;
                  }

                  scratchOff.putInt("amount", newMonster);
               }
            }

            success = true;
            response.putSFSObject("ticket", scratchOff);
            if (type.equalsIgnoreCase("C") || type.equalsIgnoreCase("S")) {
               response.putSFSObject("scaled_prizes", ScratchTicketFunctions.getScaledPrizeAmounts(sender, type));
            }
         }

         this.ext.stats.trackScratchTicketPlay(sender, !type.equals("C") && !type.equalsIgnoreCase("S") ? "monster" : "currency", purchasing ? 1 : 0);
         response.putBool("success", success);
         this.send("gs_play_scratch_off", response, sender);
      } catch (Exception var13) {
         Logger.trace(var13, "exception in gs_play_scratch_off");
      }

   }

   private void collectScratchOffPrize(User sender, ISFSObject params) {
      String type = params.getUtfString("type");
      long structureId = 0L;
      if (params.containsKey("structure")) {
         structureId = params.getLong("structure");
      }

      Player player = (Player)sender.getProperty("player_object");
      if (type == null || !type.equalsIgnoreCase("M") && !type.equalsIgnoreCase("S")) {
         this.ext.sendErrorResponse("gs_collect_scratch_off", "invalid type for scratch off collect", sender);
      } else {
         try {
            if (type.equalsIgnoreCase("M") && player.getActiveIsland().eggsFull()) {
               SFSObject response = new SFSObject();
               response.putBool("success", false);
               response.putUtfString("type", type);
               response.putBool("has_egg", true);
               this.send("gs_collect_scratch_off", response, sender);
               return;
            }

            PlayerIsland playerIsland = player.getActiveIsland();
            Island island = IslandLookup.get(playerIsland.getIndex());
            if (!player.hasUnclaimedScratchOff(type)) {
               ISFSObject response = new SFSObject();
               response.putBool("success", false);
               response.putUtfString("type", type);
               this.send("gs_collect_scratch_off", response, sender);
               return;
            }

            ISFSObject rewardObject = SFSObject.newFromJsonData(player.getScratchPrize(type));
            int reward = rewardObject.getInt("amount");
            boolean isRare = false;
            boolean isEpic = false;
            if (type.equalsIgnoreCase("M")) {
               if (!island.hasMonster(reward)) {
                  Integer newMonster = ScratchTicketFunctions.getAlternativeMonsterScratch(sender, island, reward);
                  if (newMonster == null) {
                     Logger.trace("ERROR in collectScratchOffPrize(), couldn't find alternative for monster " + reward + " on island " + island.getID() + " for user " + player.getPlayerId());
                     return;
                  }

                  reward = newMonster;
               }

               int rewardNew = ScratchTicketFunctions.getPossibleRare(reward, island.getType());
               if (rewardNew != reward && island.hasMonster(rewardNew)) {
                  reward = rewardNew;
                  isRare = MonsterCommonToRareMapping.isRare(rewardNew);
                  isEpic = MonsterCommonToEpicMapping.isEpic(rewardNew);
               }

               PlayerEgg newEgg = playerIsland.makeNewScratchWinEgg(player, reward, structureId);
               SFSObject response;
               if (newEgg == null) {
                  response = new SFSObject();
                  response.putBool("success", false);
                  response.putUtfString("type", type);
                  this.send("gs_collect_scratch_off", response, sender);
               } else {
                  response = new SFSObject();
                  response.putBool("success", true);
                  response.putSFSObject("user_egg", newEgg.getData());
                  ISFSArray responseVars = new SFSArray();
                  player.addPlayerPropertyData(responseVars, false);
                  response.putSFSArray("properties", responseVars);
                  this.send("gs_buy_egg", response, sender);
               }

               this.ext.stats.trackScratchTicketReward(sender, "monster", reward, rewardObject.getInt("is_top_prize"));
            } else {
               Player.CurrencyType rewardType = Player.getCurrencyTypeFromString(rewardObject.getUtfString("prize"));
               int rewardAmount = 0;
               switch(rewardType) {
               case Coins:
                  rewardAmount = ScratchTicketFunctions.scaleScratchReward(player.getLevel(), rewardType, reward, false);
                  player.adjustCoins(sender, this, rewardAmount);
                  break;
               case Food:
                  rewardAmount = ScratchTicketFunctions.scaleScratchReward(player.getLevel(), rewardType, reward, false);
                  player.adjustFood(sender, this, rewardAmount);
                  break;
               case Keys:
                  rewardAmount = reward;
                  player.adjustKeys(sender, this, reward);
                  break;
               case Relics:
                  rewardAmount = reward;
                  player.adjustRelics(sender, this, reward);
                  break;
               case Diamonds:
                  rewardAmount = reward;
                  player.adjustDiamonds(sender, this, reward);
                  this.ext.stats.trackReward(sender, "spin_wheel", "diamonds", (long)reward);
               }

               this.ext.stats.trackScratchTicketReward(sender, rewardType.getCurrencyKey(), rewardAmount, rewardObject.getInt("is_top_prize"));
            }

            String table = null;
            if (type.equalsIgnoreCase("M")) {
               table = "user_monster_scratch_offs";
            } else if (type.equalsIgnoreCase("C")) {
               table = "user_scratch_offs";
            } else if (type.equalsIgnoreCase("S")) {
               table = "user_spin_wheels";
            }

            if (table == null) {
               return;
            }

            String sql = "UPDATE " + table + " SET prize_claimed=1 WHERE user=?";
            Object[] args = new Object[]{player.getPlayerId()};
            this.ext.getDB().update(sql, args);
            int isTopPrize = rewardObject.getInt("is_top_prize");
            if (isTopPrize == 1) {
               sql = "UPDATE user_scratch_offs_winners SET collected=1 WHERE prize=? AND amount=? AND user=?";
               args = new Object[]{rewardObject.getUtfString("prize"), rewardObject.getInt("amount"), player.getPlayerId()};
               this.ext.getDB().update(sql, args);
            }

            player.setUnclaimedScratchOff(type, false);
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            response.putBool("rare", isRare);
            response.putBool("epic", isEpic);
            ISFSArray responseVars = new SFSArray();
            player.addPlayerPropertyData(responseVars, false);
            SFSObject prop = new SFSObject();
            prop.putLong(player.scratchOffKey(type), player.getNextScratchOffTime(type));
            responseVars.addSFSObject(prop);
            prop = new SFSObject();
            prop.putBool(player.hasScratchOffKey(type), player.hasUnclaimedScratchOff(type) || player.getNextScratchOffTime(type) <= MSMExtension.CurrentDBTime());
            responseVars.addSFSObject(prop);
            response.putSFSArray("properties", responseVars);
            this.send("gs_collect_scratch_off", response, sender);
         } catch (Exception var20) {
            Logger.trace(var20, "exception claiming scratch off prize for user " + player.getPlayerId());
         }

      }
   }

   private void requestFlipMinigameCost(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      int diamondCost = 0;
      int coinCost = 0;
      VersionInfo clientVer = new VersionInfo((String)sender.getProperty("client_version"));
      boolean success = true;
      SFSObject response = new SFSObject();

      try {
         if (clientVer.compareTo(GameSettings.get("MEMORY_FLIP_UPDATE")) >= 0) {
            boolean sendPrizes = false;
            if (params.containsKey("show_prizes")) {
               sendPrizes = params.getBool("show_prizes");
            }

            if (player.lastFlipLevelPlayed() != 1 && player.lastFlipgameFreePlay() != null) {
               Long lastFreePlay = player.lastFlipgameFreePlay() / 1000L;
               long freePlayDuration = 3600L * (long)GameSettings.getInt("MEMORY_TIME_BETWEEN_FREE_PLAYS");
               if (lastFreePlay + freePlayDuration > MSMExtension.CurrentDBTime() / 1000L) {
                  diamondCost = GameSettings.getInt("MEMORY_DIAMOND_PRICE");
                  coinCost = GameSettings.getInt("MEMORY_COIN_PRICE");
               }
            }

            if (sendPrizes) {
               response.putSFSArray("prizes_remaining", player.getRemainingScaledTopEmbeddedPrizes());
            }
         } else {
            String sql = "SELECT * FROM user_flip_mini_game WHERE user=?";
            SFSArray record = MSMExtension.getInstance().getDB().query(sql, new Object[]{player.getPlayerId()});
            if (record.size() > 0 && record.getSFSObject(0).getInt("prize_claimed") != 0) {
               Long lastFreePlay = record.getSFSObject(0).getLong("last_free_play") / 1000L;
               long freePlayDuration = 3600L * (long)GameSettings.getInt("MEMORY_TIME_BETWEEN_FREE_PLAYS");
               if (lastFreePlay + freePlayDuration > MSMExtension.CurrentDBTime() / 1000L) {
                  diamondCost = GameSettings.getInt("MEMORY_DIAMOND_PRICE");
                  coinCost = GameSettings.getInt("MEMORY_COIN_PRICE");
               }
            }
         }
      } catch (Exception var14) {
         success = false;
         Logger.trace(var14);
      }

      response.putBool("success", success);
      response.putInt("diamond_cost", diamondCost);
      response.putInt("coin_cost", coinCost);
      this.send("gs_flip_minigame_cost", response, sender);
   }

   private void purchaseFlipGame(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      VersionInfo clientVer = new VersionInfo((String)sender.getProperty("client_version"));
      boolean sendOldData = true;
      if (clientVer.compareTo(GameSettings.get("MEMORY_FLIP_UPDATE")) >= 0) {
         sendOldData = false;
      }

      if (!sendOldData) {
         try {
            SFSObject response = new SFSObject();
            boolean doPrizeReset = player.timeForFlipgamePrizeReset();
            long nextFreePlay = 0L;
            if (!player.hasFreeFlipPlay()) {
               int diamondCost = GameSettings.getInt("MEMORY_DIAMOND_PRICE");
               int coinCost = 0;
               if (!player.canBuy((long)coinCost, 0L, (long)diamondCost, 0L, 0L, 0L, 0)) {
                  response.putBool("success", false);
                  response.putInt("diamond_cost", diamondCost);
                  response.putInt("coin_cost", coinCost);
                  this.send("gs_purchase_flip_mini_game", response, sender);
                  return;
               }

               player.chargePlayer(sender, this, coinCost, 0, diamondCost, 0L, 0, 0, 0);
               this.logDiamondUsage(sender, "flip_mini_game", diamondCost, player.getLevel());
               ISFSArray responseVars = new SFSArray();
               player.addPlayerPropertyData(responseVars, false);
               response.putSFSArray("properties", responseVars);
            } else {
               player.resetLastFlipgameFreePlay();
            }

            nextFreePlay = player.lastFlipgameFreePlay();
            if (doPrizeReset) {
               player.doFlipgamePrizeReset();
            }

            player.startNewFlipGame();
            SFSArray scaledLevelRewardsArr = new SFSArray();
            Iterator itr = FlipLevelLookup.getInstance().iterator();

            while(itr.hasNext()) {
               ISFSObject levelData = (ISFSObject)itr.next();
               int level = levelData.getInt("level");
               if (level != -1) {
                  SFSObject obj = new SFSObject();
                  obj.putInt("level", levelData.getInt("level"));
                  ISFSObject unscaledPrize = FlipLevelLookup.getInstance().unscaledEndgameRewardsByLevel(level);
                  if (unscaledPrize != null) {
                     String type = unscaledPrize.getUtfString("type");
                     ISFSObject scaledPrize = new SFSObject();
                     scaledPrize.putUtfString("type", type);
                     Player.CurrencyType rewardType = Player.getCurrencyTypeFromString(type);
                     if (rewardType != Player.CurrencyType.Undefined) {
                        int val = unscaledPrize.getInt("amt");
                        val = ScratchTicketFunctions.scaleScratchReward(player.getLevel(), rewardType, val, false);
                        scaledPrize.putInt("amt", val);
                     } else {
                        Logger.trace(LogLevel.WARN, "UNSUPPORTED MEMORY FLIP REWARD TYPE: " + type);
                     }

                     obj.putSFSObject("prize", scaledPrize);
                  }

                  scaledLevelRewardsArr.addSFSObject(obj);
               }
            }

            response.putSFSArray("scaled_endgame_rewards", scaledLevelRewardsArr);
            SFSObject startLevel = FlipLevelLookup.getInstance().getStartLevel();
            if (startLevel != null) {
               int levelId = startLevel.getInt("id");
               ISFSObject randomPrize = player.selectFlipgameEmbeddedLevelPrize(levelId);
               if (randomPrize != null) {
                  response.putSFSObject("ingame_reward", randomPrize);
                  int firstLevel = FlipLevelLookup.getInstance().firstLevel();
                  if (firstLevel != 1) {
                     response.putInt("debugLevel", firstLevel);
                  }

                  response.putInt("level", startLevel.getInt("level"));
                  response.putInt("level_id", levelId);
                  response.putLong("flipGameTime", nextFreePlay);
                  response.putBool("success", true);
               } else {
                  response.putBool("success", false);
               }
            } else {
               response.putBool("success", false);
            }

            this.send("gs_purchase_flip_mini_game", response, sender);
         } catch (Exception var20) {
            Logger.trace(var20);
         }
      } else {
         this.oldPurchaseFlipGame(sender, player);
      }

   }

   private void collectFlipLevelPrize(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      SFSObject response = new SFSObject();

      try {
         if (!player.collectFlipgameSelectPrize(sender, this)) {
            this.ext.sendErrorResponse("gs_collect_flip_level", "Flip level skip detected, no flip game purchase was made", sender);
            return;
         }

         this.ext.savePlayer(player);
         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putBool("silent", true);
         response.putSFSArray("properties", responseVars);
         int nextLevel = player.endFlipLevel();
         if (nextLevel != 0) {
            SFSObject levelDat = FlipLevelLookup.getInstance().getLevelByLevel(nextLevel);
            if (nextLevel == player.flipDiamondLevel()) {
               SFSObject special = FlipLevelLookup.getInstance().getSpecialLevelId(nextLevel);
               if (special != null) {
                  levelDat = special;
               }
            }

            response.putInt("level", nextLevel);
            response.putInt("level_id", levelDat.getInt("id"));
            ISFSObject randomPrize = player.selectFlipgameEmbeddedLevelPrize(levelDat.getInt("id"));
            if (randomPrize == null) {
               this.ext.sendErrorResponse("gs_collect_flip_level", "Issue selecting new flip level prize on level " + nextLevel, sender);
               return;
            }

            response.putSFSObject("ingame_reward", randomPrize);
            response.putBool("success", true);
            this.send("gs_collect_flip_level", response, sender);
         } else {
            response.putBool("success", true);
            response.putInt("level", -1);
            response.putInt("level_id", -1);
            this.send("gs_collect_flip_level", response, sender);
         }
      } catch (Exception var9) {
         Logger.trace(var9);
      }

   }

   private void collectFlipEndgamePrize(User sender, ISFSObject params) {
      if (!params.containsKey("tier")) {
         Player player = (Player)sender.getProperty("player_object");

         try {
            if (player.lastFlipgameFreePlay() == null) {
               this.ext.sendErrorResponse("gs_collect_flip_mini_game", "Trying to collect from level never played", sender);
               return;
            }

            SFSObject response = new SFSObject();
            response.putBool("success", true);
            int level = player.endgamePrizeLevelReached();
            if (level < 1) {
               this.ext.sendErrorResponse("gs_collect_flip_mini_game", "Trying to collect from level 0", sender);
               return;
            }

            Iterator itr = FlipLevelLookup.getInstance().iterator();

            while(itr.hasNext()) {
               ISFSObject levelData = (ISFSObject)itr.next();
               SFSObject obj = new SFSObject();
               int rewardLevel = levelData.getInt("level");
               if (rewardLevel >= level) {
                  break;
               }

               obj.putInt("level", rewardLevel);
               ISFSObject unscaledPrize = FlipLevelLookup.getInstance().unscaledEndgameRewardsByLevel(rewardLevel);
               if (unscaledPrize != null) {
                  int val = unscaledPrize.getInt("amt");
                  String type = unscaledPrize.getUtfString("type");
                  Player.CurrencyType rewardType = Player.getCurrencyTypeFromString(type);
                  if (rewardType != Player.CurrencyType.Undefined) {
                     val = ScratchTicketFunctions.scaleScratchReward(player.getLevel(), rewardType, val, false);
                  }

                  switch(rewardType) {
                  case Coins:
                     player.adjustCoins(sender, this, val);
                     this.ext.stats.trackFlipMinigameReward(sender, rewardType.getCurrencyKey(), val);
                     break;
                  case Food:
                     player.adjustFood(sender, this, val);
                     this.ext.stats.trackFlipMinigameReward(sender, rewardType.getCurrencyKey(), val);
                     break;
                  case Keys:
                     player.adjustKeys(sender, this, val);
                     this.ext.stats.trackFlipMinigameReward(sender, rewardType.getCurrencyKey(), val);
                     break;
                  case Relics:
                     player.adjustRelics(sender, this, val);
                     this.ext.stats.trackFlipMinigameReward(sender, rewardType.getCurrencyKey(), val);
                     break;
                  case Diamonds:
                     player.adjustDiamonds(sender, this, val);
                     this.ext.stats.trackFlipMinigameReward(sender, rewardType.getCurrencyKey(), val);
                     this.ext.stats.trackReward(sender, "flip_game", "diamonds", (long)val);
                     break;
                  case Ethereal:
                     player.adjustEthCurrency(sender, this, val);
                     this.ext.stats.trackFlipMinigameReward(sender, rewardType.getCurrencyKey(), val);
                     break;
                  case Starpower:
                     player.adjustStarpower(sender, this, (long)val);
                     this.ext.stats.trackFlipMinigameReward(sender, rewardType.getCurrencyKey(), val);
                     break;
                  default:
                     Logger.trace(LogLevel.WARN, "collectFlipGamePrize: UNSUPPORTED MEMORY FLIP REWARD TYPE: " + type);
                  }
               }
            }

            player.endFlipgame();
            ISFSArray responseVars = new SFSArray();
            player.addPlayerPropertyData(responseVars, false);
            long nextFreePlay = player.lastFlipgameFreePlay();
            if (!player.hasFreeFlipPlay()) {
               nextFreePlay += 3600L * (long)GameSettings.getInt("MEMORY_TIME_BETWEEN_FREE_PLAYS") * 1000L;
            }

            SFSObject prop = new SFSObject();
            prop.putLong("flipGameTime", nextFreePlay);
            responseVars.addSFSObject(prop);
            response.putBool("silent", true);
            response.putSFSArray("properties", responseVars);
            this.send("gs_collect_flip_mini_game", response, sender);
         } catch (Exception var14) {
            Logger.trace(var14);
         }
      } else {
         this.oldCollectFlipEndgamePrize(sender, params);
      }

   }

   /** @deprecated */
   @Deprecated
   private void oldPurchaseFlipGame(User sender, Player player) {
      try {
         SFSObject response = new SFSObject();
         List<ISFSObject> allTicketPrizes = PrizeLookup.getScratchOffs();
         ISFSArray ticketPrizes = new SFSArray();

         for(int i = 0; i < allTicketPrizes.size(); ++i) {
            ISFSObject prize = (ISFSObject)allTicketPrizes.get(i);
            if (prize.getUtfString("type").equalsIgnoreCase("C")) {
               ticketPrizes.addSFSObject(prize);
            }
         }

         ISFSObject scaledPrizes = ScratchTicketFunctions.getScaledPrizeAmounts(sender, "C");
         response.putSFSArray("scaledPrizes", scaledPrizes.getSFSArray("prizes"));
         String sql = "SELECT * FROM user_flip_mini_game WHERE user=?";
         SFSArray record = MSMExtension.getInstance().getDB().query(sql, new Object[]{player.getPlayerId()});
         ISFSObject newPrizes = this.generateFlipPrize(ticketPrizes);
         if (record.size() > 0) {
            if (record.getSFSObject(0).getInt("prize_claimed") != 0) {
               response.putSFSArray("prizes", newPrizes.getSFSArray("prizes"));
               response.putSFSArray("tiers", newPrizes.getSFSArray("tiers"));
               Long lastFreePlay = record.getSFSObject(0).getLong("last_free_play") / 1000L;
               long freePlayDuration = (long)(3600 * GameSettings.getInt("MEMORY_TIME_BETWEEN_FREE_PLAYS"));
               if (lastFreePlay + freePlayDuration <= MSMExtension.CurrentDBTime() / 1000L) {
                  sql = "UPDATE user_flip_mini_game SET last_free_play=NOW(), last_prize=?, prize_claimed=0 WHERE user=?";
                  this.ext.getDB().update(sql, new Object[]{newPrizes.getSFSArray("prizes").toJson(), player.getPlayerId()});
               } else {
                  int diamondCost = GameSettings.getInt("MEMORY_DIAMOND_PRICE");
                  int coinCost = 0;
                  if (!player.canBuy((long)coinCost, 0L, (long)diamondCost, 0L, 0L, 0L, 0)) {
                     response.putBool("success", false);
                     response.putInt("diamond_cost", diamondCost);
                     response.putInt("coin_cost", coinCost);
                     this.send("gs_purchase_flip_mini_game", response, sender);
                     return;
                  }

                  sql = "UPDATE user_flip_mini_game SET last_prize=?, prize_claimed=0 WHERE user=?";
                  this.ext.getDB().update(sql, new Object[]{newPrizes.getSFSArray("prizes").toJson(), player.getPlayerId()});
                  player.chargePlayer(sender, this, coinCost, 0, diamondCost, 0L, 0, 0, 0);
                  this.logDiamondUsage(sender, "flip_mini_game", diamondCost, player.getLevel());
                  ISFSArray responseVars = new SFSArray();
                  player.addPlayerPropertyData(responseVars, false);
                  response.putSFSArray("properties", responseVars);
               }
            } else {
               response.putSFSArray("prizes", SFSArray.newFromJsonData(record.getSFSObject(0).getUtfString("last_prize")));
               response.putSFSArray("tiers", newPrizes.getSFSArray("tiers"));
            }
         } else {
            response.putSFSArray("prizes", newPrizes.getSFSArray("prizes"));
            response.putSFSArray("tiers", newPrizes.getSFSArray("tiers"));
            sql = "INSERT INTO user_flip_mini_game SET user=?, last_free_play=NOW(), last_prize=?";
            this.ext.getDB().update(sql, new Object[]{player.getPlayerId(), newPrizes.getSFSArray("prizes").toJson()});
         }

         response.putBool("success", true);
         this.send("gs_purchase_flip_mini_game", response, sender);
      } catch (Exception var16) {
         Logger.trace(var16);
      }

   }

   /** @deprecated */
   @Deprecated
   private ISFSObject generateFlipPrize(ISFSArray ticketPrizes) {
      ISFSObject result = new SFSObject();
      ISFSArray tiers = new SFSArray();
      ISFSArray prizes = new SFSArray();
      ISFSArray curTier = new SFSArray();
      ISFSArray remainingPrizes = SFSHelpers.clone(ticketPrizes);
      int numTiers = 3;
      int tierTotal = ticketPrizes.size();

      while(remainingPrizes.size() > 0 && numTiers > 0) {
         int tierSize = (int)Math.ceil((double)((float)tierTotal / (float)numTiers));

         int totalProbability;
         int i;
         for(totalProbability = 0; totalProbability < tierSize && remainingPrizes.size() > 0; ++totalProbability) {
            int maxProb = 0;
            i = -1;

            for(int i = 0; i < remainingPrizes.size(); ++i) {
               if (remainingPrizes.getSFSObject(i).getInt("probability") > maxProb) {
                  maxProb = remainingPrizes.getSFSObject(i).getInt("probability");
                  i = i;
               }
            }

            curTier.addSFSObject(remainingPrizes.getSFSObject(i));
            remainingPrizes.removeElementAt(i);
         }

         --numTiers;
         tierTotal -= tierSize;
         totalProbability = 0;
         ISFSArray tier = new SFSArray();

         for(i = 0; i < curTier.size(); ++i) {
            ISFSObject curPrize = new SFSObject();
            curPrize.putInt("p", curTier.getSFSObject(i).getInt("id"));
            tier.addSFSObject(curPrize);
            totalProbability += curTier.getSFSObject(i).getInt("probability");
         }

         ISFSObject t = new SFSObject();
         t.putSFSArray("t", tier);
         tiers.addSFSObject(t);
         Random random = new Random();
         int randomValue = random.nextInt(totalProbability);
         totalProbability = 0;

         for(int i = 0; i < curTier.size(); ++i) {
            int curPrizeProb = curTier.getSFSObject(i).getInt("probability");
            if (0 > curPrizeProb) {
               totalProbability = 0;
            }

            totalProbability += curPrizeProb;
            if (curPrizeProb > 0 && randomValue < totalProbability) {
               ISFSObject prize = new SFSObject();
               prize.putInt("p", curTier.getSFSObject(i).getInt("id"));
               prizes.addSFSObject(prize);
               break;
            }
         }
      }

      result.putSFSArray("prizes", prizes);
      result.putSFSArray("tiers", tiers);
      return result;
   }

   /** @deprecated */
   @Deprecated
   private void oldCollectFlipEndgamePrize(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      int tier = params.getInt("tier");
      if (tier < 0 || tier > 2) {
         tier = 0;
      }

      try {
         String sql = "SELECT * FROM user_flip_mini_game WHERE user=?";
         SFSArray results = this.ext.getDB().query(sql, new Object[]{player.getPlayerId()});
         SFSObject response = new SFSObject();
         if (results.size() == 0 || results.getSFSObject(0).getInt("prize_claimed") == 1) {
            response.putBool("success", false);
            this.send("gs_collect_flip_mini_game", response, sender);
            return;
         }

         response.putBool("success", true);
         ISFSArray rewards = SFSArray.newFromJsonData(results.getSFSObject(0).getUtfString("last_prize"));
         int scratchId = rewards.getSFSObject(tier).getInt("p");
         List<ISFSObject> scratchPrizes = PrizeLookup.getScratchOffs();
         ISFSObject curPrize = null;

         int reward;
         for(reward = 0; reward < scratchPrizes.size(); ++reward) {
            if (((ISFSObject)scratchPrizes.get(reward)).getInt("id") == scratchId) {
               curPrize = (ISFSObject)scratchPrizes.get(reward);
               break;
            }
         }

         if (curPrize == null) {
            sql = "UPDATE user_flip_mini_game SET prize_claimed=1 WHERE user=?";
            this.ext.getDB().update(sql, new Object[]{player.getPlayerId()});
            response.putBool("success", false);
            this.send("gs_collect_flip_mini_game", response, sender);
            return;
         }

         reward = curPrize.getInt("amount");
         Player.CurrencyType rewardType = Player.getCurrencyTypeFromString(curPrize.getUtfString("prize"));
         switch(rewardType) {
         case Coins:
            reward = ScratchTicketFunctions.scaleScratchReward(player.getLevel(), rewardType, reward, false);
            player.adjustCoins(sender, this, reward);
            break;
         case Food:
            reward = ScratchTicketFunctions.scaleScratchReward(player.getLevel(), rewardType, reward, false);
            player.adjustFood(sender, this, reward);
            break;
         case Keys:
            player.adjustKeys(sender, this, reward);
            break;
         case Relics:
            player.adjustRelics(sender, this, reward);
            break;
         case Diamonds:
            player.adjustDiamonds(sender, this, reward);
            this.ext.stats.trackReward(sender, "flip_game", "diamonds", (long)reward);
         }

         this.ext.stats.trackFlipMinigameReward(sender, rewardType.getCurrencyKey(), reward);
         sql = "UPDATE user_flip_mini_game SET prize_claimed=1 WHERE user=?";
         this.ext.getDB().update(sql, new Object[]{player.getPlayerId()});
         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putBool("silent", true);
         response.putSFSArray("properties", responseVars);
         this.send("gs_collect_flip_mini_game", response, sender);
      } catch (Exception var15) {
         Logger.trace(var15);
      }

   }

   public void addFriend(User sender, ISFSObject params) {
      ISFSObject response = new SFSObject();
      Player player = (Player)sender.getProperty("player_object");
      long bbbId = player.getBbbId();
      long friendBbbId = params.getLong("friend_id");
      if (bbbId == friendBbbId) {
         response.putBool("success", false);
         response.putUtfString("error_msg", "FRIEND_ERROR_USER_NOT_FOUND");
         this.send("gs_add_friend", response, sender);
      } else {
         try {
            String sql = "SELECT * FROM users WHERE bbb_id=?";
            Object[] args = new Object[]{friendBbbId};
            SFSArray result = this.ext.getDB().query(sql, args);
            if (result.size() == 0) {
               response.putBool("success", false);
               response.putUtfString("error_msg", "FRIEND_ERROR_USER_NOT_FOUND");
               this.send("gs_add_friend", response, sender);
               return;
            }

            int friendUserId = result.getSFSObject(0).getInt("user_id");
            sql = "SELECT * FROM user_friends WHERE (user_1=? and user_2=?) or (user_2=? and user_1=?)";
            args = new Object[]{bbbId, friendBbbId, bbbId, friendBbbId};
            result = this.ext.getDB().query(sql, args);
            if (result.size() == 0) {
               sql = "INSERT INTO user_friends (user_1, user_2) VALUES (?, ?)";
               args = new Object[]{bbbId, friendBbbId};
               this.ext.getDB().update(sql, args);
               response.putBool("success", true);
               response.putLong("friend_id", friendBbbId);
               this.send("gs_add_friend", response, sender);
               this.ext.stats.trackFriend(sender, friendBbbId);
               if (player.getBattleState() != null) {
                  sql = "SELECT level from user_battle WHERE user_id=?";
                  args = new Object[]{friendUserId};
                  result = this.ext.getDB().query(sql, args);
                  if (result.size() > 0) {
                     sql = "INSERT INTO user_battle_friends (user_1, user_2, won_battles, lost_battles) VALUES (?, ?, 0, 0)";
                     args = new Object[]{bbbId, friendBbbId};
                     this.ext.getDB().update(sql, args);
                  }
               }

               return;
            }

            response.putUtfString("error_msg", "FRIEND_ERROR_ALREADY_EXISTS");
         } catch (Exception var13) {
            Logger.trace(var13, " ***** Exception trying to add friend ***** ");
         }

         response.putBool("success", false);
         this.send("gs_add_friend", response, sender);
      }
   }

   public void removeFriend(User sender, ISFSObject params) {
      ISFSObject response = new SFSObject();
      Player player = (Player)sender.getProperty("player_object");
      long bbbId = player.getBbbId();
      long friendBbbId = params.getLong("friend_id");
      if (bbbId == friendBbbId) {
         response.putBool("success", false);
         response.putUtfString("error_msg", "FRIEND_ERROR_USER_NOT_FOUND");
         this.send("gs_remove_friend", response, sender);
      } else {
         try {
            String sql = "SELECT * FROM user_friends WHERE (user_1=? and user_2=?) or (user_2=? and user_1=?)";
            Object[] args = new Object[]{bbbId, friendBbbId, bbbId, friendBbbId};
            SFSArray result = this.ext.getDB().query(sql, args);
            if (result.size() > 0) {
               sql = "DELETE FROM user_friends WHERE (user_1=? AND user_2=?) OR (user_2=? AND user_1=?)";
               args = new Object[]{bbbId, friendBbbId, bbbId, friendBbbId};
               this.ext.getDB().update(sql, args);
               sql = "DELETE FROM user_battle_friends WHERE (user_1=? AND user_2=?) OR (user_2=? AND user_1=?)";
               args = new Object[]{bbbId, friendBbbId, bbbId, friendBbbId};
               this.ext.getDB().update(sql, args);
               response.putBool("success", true);
               this.send("gs_remove_friend", response, sender);
               return;
            }
         } catch (Exception var12) {
            Logger.trace(var12, " ***** Exception trying to remove friend ***** ");
         }

         response.putBool("success", false);
         this.send("gs_remove_friend", response, sender);
      }
   }

   public void syncFriends(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      long bbbId = player.getBbbId();
      String friendList = params.getUtfString("friends");
      String username = params.getUtfString("username");
      String loginType = params.getUtfString("login_type");
      this.ext.updateSocialUsers(bbbId, username, loginType, false);
      if (!friendList.isEmpty()) {
         String[] friendsArray = friendList.split(",");
         if (friendsArray.length > 0) {
            ArrayList currentFriends = new ArrayList();

            try {
               String sql = "SELECT * FROM user_friends WHERE user_1=? OR user_2=?";
               ISFSArray currentResult = this.ext.getDB().query(sql, new Object[]{bbbId, bbbId});

               int maxSize;
               for(maxSize = 0; maxSize < currentResult.size(); ++maxSize) {
                  ISFSObject row = currentResult.getSFSObject(maxSize);
                  currentFriends.add(row.getLong("user_1") != bbbId ? row.getLong("user_1") : row.getLong("user_2"));
               }

               maxSize = GameSettings.getInt("FRIEND_MAX_LOAD");
               if (friendsArray.length < maxSize) {
                  maxSize = friendsArray.length;
               }

               StringBuilder sb = new StringBuilder();
               sb.append("SELECT bbb_id FROM user_social WHERE login_type=? AND username IN ('");

               for(int i = 0; i < maxSize - 1; ++i) {
                  sb.append(ESAPI.encoder().encodeForSQL(this.ext.sqlCodec, friendsArray[i]));
                  sb.append("','");
               }

               sb.append(ESAPI.encoder().encodeForSQL(this.ext.sqlCodec, friendsArray[maxSize - 1]));
               sb.append("') AND bbb_id!=?");
               SFSArray socialResult = this.ext.getDB().query(sb.toString(), new Object[]{loginType, bbbId});

               for(int i = 0; i < socialResult.size(); ++i) {
                  Long friendId = socialResult.getSFSObject(i).getLong("bbb_id");
                  if (!currentFriends.contains(friendId)) {
                     sql = "INSERT INTO user_friends SET user_1=?, user_2=?";
                     this.ext.getDB().update(sql, new Object[]{bbbId, friendId});
                  }
               }
            } catch (Exception var18) {
               Logger.trace(var18, " ***** Exception trying to sync friends ***** ");
            }
         }
      }

      ISFSObject response = new SFSObject();
      this.send("gs_sync_friends", response, sender);
   }

   public void adminRefreshAll(User sender, ISFSObject params) {
      SFSObject adminResponse = new SFSObject();
      adminResponse.putBool("success", false);
      this.send("gs_admin_refresh_all", adminResponse, sender);
   }

   public void getCcu(String requestID, User sender, ISFSObject params) {
      ISFSObject response = new SFSObject();
      response.putInt("total_ccu", SmartFoxServer.getInstance().getUserManager().getUserCount());
      this.send(requestID, response, sender);
   }

   public void logDiamondUsage(User sender, String action, int diamonds, int level, JSONObject extra, int entityId) {
      this.ext.stats.trackSpend(sender, action, "diamonds", (long)diamonds, extra, entityId);
   }

   public void logDiamondUsage(User sender, String action, int diamonds, int level, int entityId) {
      this.logDiamondUsage(sender, action, diamonds, level, (JSONObject)null, entityId);
   }

   public void logDiamondUsage(User sender, String action, int diamonds, int level) {
      this.logDiamondUsage(sender, action, diamonds, level, (JSONObject)null, -1);
   }

   public void logStarpowerUsage(User sender, String action, int starpower, int level) {
      this.ext.stats.trackSpend(sender, action, "starpower", (long)starpower);
   }

   private void getAdminVisitData(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("Unauthorized!  User is not an administrator!");
      } else {
         long userID = 0L;
         SFSObject response = new SFSObject();

         try {
            if (params.containsKey("bbb_id")) {
               userID = params.getLong("bbb_id");
            } else if (params.containsKey("support_id")) {
               String supportId = params.getUtfString("support_id");
               String sql = "SELECT bbb_id FROM user_game_id_to_bbb_id WHERE user_game_id=?";
               Object[] args = new Object[]{supportId};
               SFSArray result = this.ext.getDB().query(sql, args);
               if (result.size() <= 0) {
                  Logger.trace("Attempting to access non-existant user_game_id: " + supportId);
                  return;
               }

               ISFSObject r = result.getSFSObject(0);
               userID = r.getLong("bbb_id");
            }

            Player adminPlayer = (Player)sender.getProperty("player_object");
            MSMExtension.getInstance().markAccountLockedForAdminEditing(sender, userID, adminPlayer.getBbbId());
            Player friend = this.ext.adminCreatePlayerByBBBID(userID);
            ISFSObject friendSFS = friend.toSFSObject();
            friendSFS.putUtfString("client_tutorial_setup", friend.clientTutorialSetup());
            response.putSFSObject("friend_object", friendSFS);
            Vector<SFSObject> removeEntries = new Vector();
            String sql = "SELECT island_id, user_structure FROM user_torch_gifts WHERE recipient_bbbid=? AND collected=0";
            Object[] args = new Object[]{friend.getBbbId()};
            SFSArray result = this.ext.getDB().query(sql, args);
            int i = 0;

            while(true) {
               SFSObject s;
               Long islandId;
               Long userStructureId;
               if (i >= result.size()) {
                  if (removeEntries.size() > 0) {
                     for(i = 0; i < removeEntries.size(); ++i) {
                        s = (SFSObject)removeEntries.elementAt(i);
                        islandId = s.getLong("island_id");
                        userStructureId = s.getLong("user_structure");
                        sql = "DELETE FROM user_torch_gifts WHERE recipient_bbbid=? AND island_id=? AND user_structure=?";
                        args = new Object[]{friend.getBbbId(), islandId, userStructureId};
                        this.ext.getDB().update(sql, args);
                     }

                     sql = "SELECT island_id, user_structure FROM user_torch_gifts WHERE recipient_bbbid=? AND collected=0";
                     args = new Object[]{friend.getBbbId()};
                     result = this.ext.getDB().query(sql, args);
                  }

                  if (result.size() > 0) {
                     response.putSFSArray("torch_gifts", result);
                  }

                  sender.setProperty("friend_object", friend);
                  response.putBool("success", true);
                  break;
               }

               s = (SFSObject)result.getElementAt(i);
               islandId = s.getLong("island_id");
               userStructureId = s.getLong("user_structure");
               PlayerStructure curStructure = friend.getIslandByID(islandId).getStructureByID(userStructureId);
               if (curStructure == null) {
                  removeEntries.add(s);
               } else if (!curStructure.isTorch()) {
                  removeEntries.add(s);
               }

               ++i;
            }
         } catch (Exception var18) {
            Logger.trace(var18);
            this.ext.sendErrorResponse("gs_admin_get_user_visit_data", "Error thrown in getAdminVisitData()", sender);
            return;
         }

         this.send("gs_admin_get_user_visit_data", response, sender);
      }
   }

   private void adminMoveUsersMonster(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminMoveUsersMonster: Error! Trying to invoke admin without privileges!");
      } else {
         try {
            Long playerIslandId = params.getLong("user_island_id");
            Player friend = (Player)sender.getProperty("friend_object");
            PlayerIsland pi = friend.getIslandByID(playerIslandId);
            long playerMonsterId = params.getLong("user_monster_id");
            int posX = params.getInt("pos_x");
            int posY = params.getInt("pos_y");
            PlayerMonster monster = pi.getMonsterByID(playerMonsterId);
            monster.setPosition(posX, posY);
            this.ext.savePlayerIsland(friend, pi, false);
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            this.send("gs_admin_move_users_monster", response, sender);
            response = new SFSObject();
            response.putLong("user_monster_id", playerMonsterId);
            response.putInt("pos_x", posX);
            response.putInt("pos_y", posY);
            this.send("gs_admin_update_users_monster", response, sender);
         } catch (Exception var12) {
            Logger.trace(var12, "error during admin move monster", "   params : " + params.getDump());
         }

      }
   }

   private void adminMoveUsersStructure(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminMoveUsersStructure: Error! Trying to invoke admin without privileges!");
      } else {
         try {
            long islandId = params.getLong("island_id");
            long structureId = params.getLong("structure_id");
            int posX = params.getInt("pos_x");
            int posY = params.getInt("pos_y");
            boolean isObstacle = params.getBool("isObstacle");
            Player friend = (Player)sender.getProperty("friend_object");
            PlayerIsland pi = friend.getIslandByID(islandId);
            PlayerStructure ps = pi.getStructureByID(structureId);
            ps.setPosition(posX, posY);
            this.ext.savePlayerIsland(friend, pi, false);
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            this.send("gs_admin_move_users_structure", response, sender);
            response = new SFSObject();
            response.putLong("structure_id", structureId);
            SFSArray responseVars = new SFSArray();
            ISFSObject property = new SFSObject();
            property.putInt("pos_x", posX);
            responseVars.addSFSObject(property);
            property = new SFSObject();
            property.putInt("pos_y", posY);
            responseVars.addSFSObject(property);
            property = new SFSObject();
            property.putBool("isObstacle", isObstacle);
            responseVars.addSFSObject(property);
            response.putSFSArray("properties", responseVars);
            this.send("gs_admin_update_users_structure", response, sender);
         } catch (Exception var16) {
            Logger.trace(var16, "**** error buying structure ****", "   params : " + params.getDump());
         }

      }
   }

   private void adminDestroyUsersMonster(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminDestroyUsersMonster: Error! Trying to invoke admin without privileges!");
      } else {
         long playerMonsterId = params.getLong("user_monster_id");
         Long playerIslandId = params.getLong("user_island_id");

         try {
            Player player = (Player)sender.getProperty("friend_object");
            PlayerIsland island = player.getIslandByID(playerIslandId);
            PlayerMonster playerMonster = island.getMonsterByID(playerMonsterId);
            if (playerMonster == null) {
               this.ext.sendErrorResponse("gs_admin_destroy_users_monster", "Could not find the monster you're trying to destroy", sender);
               return;
            }

            ISFSObject response = new SFSObject();
            SFSObject goldData = playerMonster.getGoldIslandData();
            if (goldData != null) {
               Long goldMonsterId = goldData.getLong("monster");
               PlayerIsland goldIsland = player.getIslandByIslandIndex(6);
               if (goldIsland != null) {
                  PlayerMonster goldMonster = goldIsland.getMonsterByID(goldMonsterId);
                  if (goldMonster != null) {
                     response.putLong("user_gi_monster_id", goldMonster.getID());
                     goldIsland.removeMonster(goldMonster, true);
                     this.ext.savePlayerIsland(player, goldIsland, false);
                  }
               }
            }

            SFSObject parentData = playerMonster.getParentIslandData();
            if (parentData != null) {
               Long parentIslandId = parentData.getLong("island");
               Long parentMonsterId = parentData.getLong("monster");
               PlayerIsland parentIsland = player.getIslandByID(parentIslandId);
               if (parentIsland != null) {
                  PlayerMonster parentMonster = parentIsland.getMonsterByID(parentMonsterId);
                  if (parentMonster != null) {
                     parentMonster.setGoldIslandData(0L, 0L);
                     this.ext.savePlayerIsland(player, parentIsland, false);
                  }
               }
            }

            island.removeMonster(playerMonster, true);
            this.ext.savePlayerIsland(player, island, false);
            response.putBool("success", true);
            response.putLong("user_monster_id", playerMonsterId);
            this.send("gs_admin_destroy_users_monster", response, sender);
         } catch (Exception var16) {
            ISFSObject response = new SFSObject();
            response.putBool("success", false);
            response.putLong("user_monster_id", playerMonsterId);
            this.send("gs_admin_destroy_users_monster", response, sender);
            Logger.trace(var16, "error deleting monster", "   params : " + params.getDump());
         }

      }
   }

   private void adminDestroyUsersStructure(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminDestroyUsersStructure: Error! Trying to invoke admin without privileges!");
      } else {
         Long playerIslandId = params.getLong("user_island_id");
         Long playerStructureId = params.getLong("user_structure_id");

         try {
            Player friend = (Player)sender.getProperty("friend_object");
            PlayerIsland pi = friend.getIslandByID(playerIslandId);
            PlayerStructure ps = pi.getStructureByID(playerStructureId);
            Structure s = StructureLookup.get(ps.getType());
            SFSObject response;
            if (s.isBakery()) {
               PlayerBaking pb = pi.getBakingByStructureId(playerStructureId);
               if (pb != null) {
                  pi.removeBaking(pb);
               }
            } else if (s.isNursery()) {
               PlayerEgg e = pi.getEggByStructureId(playerStructureId);
               if (e != null) {
                  pi.removeEgg(e.getID());
               }
            } else if (s.isBreeding()) {
               PlayerBreeding b = pi.getBreedingByStructureId(playerStructureId);
               if (b != null) {
                  pi.removeBreeding(b.getID());
               }
            } else if (s.isTorch()) {
               pi.removeLitTorch(ps);
               String sql = "DELETE FROM user_torch_gifts WHERE recipient_bbbid=? AND island_id=? AND user_structure=?";
               Object[] args = new Object[]{friend.getBbbId(), playerIslandId, playerStructureId};
               this.ext.getDB().update(sql, args);
            } else if (s.isHotel()) {
               if (pi.getNumMonstersInStorage() > 0) {
                  response = new SFSObject();
                  response.putBool("success", false);
                  response.putLong("user_structure_id", playerStructureId);
                  this.send("gs_admin_destroy_users_structure", response, sender);
                  return;
               }
            } else if (s.isWarehouse()) {
               if (pi.getNumDecorationsInStorage() > 0) {
                  response = new SFSObject();
                  response.putBool("success", false);
                  response.putUtfString("error_msg", "MSG_WAREHOUSE_SELL_FULL");
                  this.send("gs_admin_destroy_users_structure", response, sender);
                  return;
               }
            } else if (s.isFuzer() && pi.getNumBuddiesInFuzer() > 0) {
               response = new SFSObject();
               response.putBool("success", false);
               response.putUtfString("error_msg", "MSG_FUZER_SELL_FULL");
               this.send("gs_admin_destroy_users_structure", response, sender);
               return;
            }

            pi.removeStructure(ps);
            this.ext.savePlayerIsland(friend, pi, false);
            response = new SFSObject();
            response.putBool("success", true);
            response.putLong("user_structure_id", playerStructureId);
            this.send("gs_admin_destroy_users_structure", response, sender);
         } catch (Exception var11) {
            ISFSObject response = new SFSObject();
            response.putBool("success", false);
            response.putLong("user_structure_id", playerStructureId);
            this.send("gs_admin_destroy_users_structure", response, sender);
            Logger.trace(var11, "error deleting monster", "   params : " + params.getDump());
         }

      }
   }

   private void adminSellEgg(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminSellEgg: Error! Trying to invoke admin without privileges!");
      } else {
         long userEggId = params.getLong("user_egg_id");

         try {
            long playerIslandId = params.getLong("user_island_id");
            Player friend = (Player)sender.getProperty("friend_object");
            PlayerIsland pi = friend.getIslandByID(playerIslandId);
            pi.removeEgg(userEggId);
            this.ext.savePlayerIsland(friend, pi, false);
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            response.putLong("user_egg_id", userEggId);
            this.send("gs_admin_sell_egg", response, sender);
         } catch (Exception var10) {
            ISFSObject response = new SFSObject();
            response.putBool("success", false);
            response.putLong("user_egg_id", userEggId);
            this.send("gs_admin_sell_egg", response, sender);
            Logger.trace(var10, "error deleting egg", "   params : " + params.getDump());
         }

      }
   }

   private void adminBuyEgg(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminBuyEgg: Error! Trying to invoke admin without privileges!");
      } else {
         try {
            int monsterId = params.getInt("monster_id");
            Player friend = (Player)sender.getProperty("friend_object");
            long islandUid = 0L;
            if (params.containsKey("island_uid")) {
               islandUid = params.getLong("island_uid");
            } else if (params.containsKey("island_id")) {
               islandUid = (long)params.getInt("island_id");
            }

            PlayerIsland island = friend.getIslandByID(islandUid);
            long structureId = 0L;
            if (params.containsKey("nursery_id")) {
               structureId = params.getLong("nursery_id");
            }

            ISFSObject monsterCostumeState = null;
            ISFSObject response = new SFSObject();
            if (params.containsKey("costume")) {
               try {
                  MonsterCostumeState mcs = new MonsterCostumeState();
                  int costumeId = params.getInt("costume");
                  if (costumeId > 0) {
                     response.putInt("costume_id", costumeId);
                     CostumeData costumeData = CostumeLookup.get(costumeId);
                     if (costumeData == null) {
                        throw new Exception("Costume not found");
                     }

                     this.purchaseCostume(sender, friend, island, true, costumeData, mcs, response, (JSONObject)null);
                     mcs.setEquipped(costumeId);
                     monsterCostumeState = mcs.toSFSObject();
                  }
               } catch (Exception var15) {
                  this.ext.sendErrorResponse("gs_buy_egg", var15.getMessage(), sender);
                  return;
               }
            }

            this.buyEgg(sender, friend, monsterId, island, structureId, false, 0L, false, "", (ISFSObject)null, monsterCostumeState, response, true);
         } catch (Exception var16) {
            Logger.trace(var16, "error during admin buy egg", "   params : " + params.getDump());
         }

      }
   }

   private void adminHatchEgg(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminHatchEgg: Error! Trying to invoke admin without privileges!");
      } else {
         try {
            long playerIslandId = params.getLong("user_island_id");
            long userEggId = params.getLong("user_egg_id");
            int pos_x = params.getInt("pos_x");
            int pos_y = params.getInt("pos_y");
            int flip = params.getInt("flip");
            Player friend = (Player)sender.getProperty("friend_object");
            PlayerIsland island = friend.getIslandByID(playerIslandId);
            PlayerEgg playerEgg = null;
            Integer explicitMonsterType = params.getInt("monster_type");
            int monsterId;
            if (explicitMonsterType != null && explicitMonsterType != 0) {
               monsterId = explicitMonsterType;
            } else {
               playerEgg = island.getEgg(userEggId);
               monsterId = playerEgg.getType();
            }

            Monster monster = MonsterLookup.get(monsterId);
            if (monster == null) {
               this.ext.sendErrorResponse("gs_admin_hatch_egg", "monster " + monsterId + " is not a valid type id", sender);
            }

            if (playerEgg != null && playerEgg.getTimeRemaining() > 0L) {
               this.ext.sendErrorResponse("gs_admin_hatch_egg", "The egg is not ready to be hatched yet", sender);
               return;
            }

            String name;
            if (params.containsKey("name")) {
               name = params.getUtfString("name");
            } else if (playerEgg != null && playerEgg.getPreviousName() != null && playerEgg.getPreviousName() != "") {
               name = playerEgg.getPreviousName();
            } else {
               name = monster.generateRandomMonsterName();
            }

            SFSObject newMonsterData = PlayerMonster.createMonsterSFS(monsterId, name, playerEgg != null ? playerEgg.getPrevPermaMega() : null, playerEgg != null ? playerEgg.getCostumeData() : null, island.getIndex(), playerIslandId, pos_x, pos_y, flip, friend.getNextMonsterIndex(), 1, MSMExtension.CurrentDBTime(), MSMExtension.CurrentDBTime(), false, 0L, playerEgg != null ? playerEgg.getBoxedEggs() : null, false, false);
            PlayerMonster newPlayerMonster = new PlayerMonster(newMonsterData, island);
            if (playerEgg != null) {
               island.removeEgg(userEggId);
               long structureId = playerEgg.getStructureID();
               PlayerSynthesizingData data = island.getSynthesizingData(structureId);
               if (data != null) {
                  island.removeSynthesizingData(data);
               }
            }

            island.addMonster(newPlayerMonster);
            boolean directBuy = false;
            if (params.containsKey("direct_buy")) {
               directBuy = params.getBool("direct_buy");
            }

            if ((playerEgg == null || playerEgg.getBoxedEggs() == null) && monster.isBoxMonsterType() && !directBuy) {
               newPlayerMonster.setBoxMonsterData(new SFSArray(), island.isGoldIsland());
            }

            if (monster.isEvolvable()) {
               newPlayerMonster.setEvolveDataStatic(new SFSArray());
               newPlayerMonster.setEvolveDataFlex(new SFSArray());
            }

            this.ext.savePlayerIsland(friend, island, false);
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            response.putSFSObject("monster", newPlayerMonster.toSFSObject(island));
            response.putLong("user_egg_id", userEggId);
            ISFSArray responseVars = new SFSArray();
            friend.addPlayerPropertyData(responseVars, true);
            response.putSFSArray("properties", responseVars);
            this.send("gs_admin_hatch_egg", response, sender);
            this.serverQuestEvent(sender, "object", newPlayerMonster.getEntityId());
            this.serverQuestEvent(sender, "genes", MonsterLookup.get(newPlayerMonster.getType()).getGenes().length());
         } catch (Exception var22) {
            Logger.trace(var22, "error during admin hatch egg", "   params : " + params.getDump());
         }

      }
   }

   private void adminBuyStructure(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminBuyStructure: Error! Trying to invoke admin without privileges!");
      } else {
         try {
            int structureId = params.getInt("structure_id");
            int pos_x = params.getInt("pos_x");
            int pos_y = params.getInt("pos_y");
            int flip = params.getInt("flip");
            long islandId = params.getLong("user_island_id");
            Player friend = (Player)sender.getProperty("friend_object");
            PlayerIsland pi = friend.getIslandByID(islandId);
            boolean applyHappyTreeEffects = false;
            if (pi.hasHappinessTree()) {
               applyHappyTreeEffects = true;
            }

            long currentTimestamp = MSMExtension.CurrentDBTime();
            SFSObject newStructure = new SFSObject();
            newStructure.putLong("user_structure_id", pi.getNextStructureId());
            newStructure.putLong("island", pi.getID());
            newStructure.putLong("structure", (long)structureId);
            newStructure.putInt("pos_x", pos_x);
            newStructure.putInt("pos_y", pos_y);
            newStructure.putDouble("scale", 1.0D);
            newStructure.putInt("flip", flip);
            newStructure.putInt("is_complete", 1);
            newStructure.putLong("date_created", currentTimestamp);
            newStructure.putLong("building_completed", currentTimestamp);
            newStructure.putLong("last_collection", currentTimestamp);
            PlayerStructure playerStructure = new PlayerStructure(newStructure);
            Structure structure = StructureLookup.get(playerStructure.getType());
            if (pi.hasMax(structure, friend)) {
               this.ext.sendErrorResponse("gs_buy_structure", "You already have one of those structures and cannot add a second one", sender);
               return;
            }

            if (structure.isHappyTree()) {
               applyHappyTreeEffects = true;
            }

            pi.addStructure(playerStructure);
            boolean isComplete = playerStructure.isComplete();
            ISFSObject response = new SFSObject();
            if (isComplete && applyHappyTreeEffects) {
               SFSArray monsterEffects = this.addHappyTreeEffects(pi, (User)null);
               response.putSFSArray("monster_happy_effects", monsterEffects);
            }

            this.ext.savePlayerIsland(friend, pi, false);
            ISFSObject questEvent = new SFSObject();
            questEvent.putInt("object", playerStructure.getEntityId());
            questEvent.putUtfString("structure_type", playerStructure.getStructureType());
            this.serverQuestEvent(sender, questEvent);
            response.putBool("success", true);
            response.putSFSObject("user_structure", playerStructure.toSFSObject(pi));
            SFSArray responseVars = new SFSArray();
            friend.addPlayerPropertyData(responseVars, true);
            response.putSFSArray("properties", responseVars);
            this.send("gs_admin_buy_structure", response, sender);
         } catch (Exception var21) {
            Logger.trace(var21, "**** error admin buying structure ****", "   params : " + params.getDump());
         }

      }
   }

   private void adminBuyIsland(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminBuyIsland: Error! Trying to invoke admin without privileges!");
      } else {
         try {
            Player player = (Player)sender.getProperty("friend_object");
            if (player.duplicateCommand("gs_buy_island", 5000L)) {
               this.ext.sendErrorResponse("gs_buy_island", "Ignoring likely double-tap message", sender);
               return;
            }

            Integer islandId = params.getInt("island_id");
            int newIslandIndex;
            if (islandId != null) {
               newIslandIndex = islandId;
               Collection<PlayerIsland> playerIslands = player.getIslands();
               Iterator var7 = playerIslands.iterator();

               while(var7.hasNext()) {
                  PlayerIsland i = (PlayerIsland)var7.next();
                  if (newIslandIndex == i.getIndex()) {
                     this.ext.sendErrorResponse("gs_admin_buy_island", "They already own this island", sender);
                     return;
                  }
               }
            } else {
               newIslandIndex = player.getIslands().size() + 1;
            }

            Island islandData = IslandLookup.get(newIslandIndex);
            long userIslandId = this.ext.createPlayerIsland(player, newIslandIndex);
            String sql = "SELECT * FROM user_islands where user_island_id = ?";
            Object[] args = new Object[]{userIslandId};
            ISFSArray newIslandResult = this.ext.getDB().query(sql, args);
            PlayerIsland playerIsland = new PlayerIsland(newIslandResult.getSFSObject(0));
            this.ext.loadPlayerIslandData(player, playerIsland);
            player.addIsland(playerIsland);
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            if (params.containsKey("no_change_island") && params.getBool("no_change_island")) {
               response.putBool("no_change_island", true);
            }

            response.putSFSObject("user_island", playerIsland.toSFSObject());
            this.ext.savePlayer(player);
            this.serverQuestEvent(sender, "island", islandData.getID());
            SFSArray responseVars = new SFSArray();
            player.addPlayerPropertyData(responseVars, true);
            response.putSFSArray("properties", responseVars);
            this.send("gs_admin_buy_island", response, sender);
         } catch (Exception var15) {
            Logger.trace(var15, "**** error admin buying island ****", "   params : " + params.getDump());
         }

      }
   }

   private void adminChangeIsland(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminChangeIsland: Error! Trying to invoke admin without privileges!");
      } else {
         try {
            long islandId = params.getLong("user_island_id");
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            response.putLong("user_island_id", islandId);
            this.send("gs_admin_change_island", response, sender);
            Player friend = (Player)sender.getProperty("friend_object");
            MSMExtension.getInstance().resetAccountAccessTime(friend.getBbbId());
         } catch (Exception var7) {
            Logger.trace(var7, "**** error admin change island ****", "   params : " + params.getDump());
         }

      }
   }

   private void adminDestroyIsland(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminDestroyIsland: Error! Trying to invoke admin without privileges!");
      } else {
         try {
            long islandToDeleteId = params.getLong("user_island_id");
            long playerId = params.getLong("user_id");
            Player player = this.ext.createPlayerByID(playerId, true, new VersionInfo((String)sender.getProperty("client_version")), (String)sender.getProperty("client_platform"), (String)sender.getProperty("client_subplatform"));
            if (player == null) {
               this.ext.sendVersionError(sender, true);
            }

            Collection<PlayerIsland> islands = player.getIslands();
            Iterator<PlayerIsland> itr = islands.iterator();
            PlayerIsland islandToDelete = null;
            long newActiveIsland = -1L;

            while(itr.hasNext()) {
               PlayerIsland p = (PlayerIsland)itr.next();
               long id = p.getID();
               if (id != islandToDeleteId) {
                  if (newActiveIsland == -1L) {
                     newActiveIsland = id;
                  }
               } else {
                  islandToDelete = p;
               }
            }

            if (newActiveIsland == -1L || islandToDelete == null) {
               throw new Exception("invalid newActiveIsland");
            }

            String sql = "UPDATE users SET active_island=? WHERE user_id=?";
            Object[] args = new Object[]{newActiveIsland, playerId};
            this.ext.getDB().update(sql, args);
            sql = "DELETE FROM user_islands WHERE user_island_id=?";
            args = new Object[]{islandToDeleteId};
            this.ext.getDB().update(sql, args);
            sql = "UPDATE user_island_data SET user=1 WHERE island=?";
            args = new Object[]{islandToDeleteId};
            this.ext.getDB().update(sql, args);
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            response.putLong("user_island_id", islandToDeleteId);
            response.putLong("new_active_island_id", newActiveIsland);
            this.send("gs_admin_destroy_island", response, sender);
         } catch (Exception var16) {
            Logger.trace(var16, "**** error gs_admin_destroy_island ****", "   params : " + params.getDump());
         }

      }
   }

   private void adminFeedMonster(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminFeedMonster: Error! Trying to invoke admin without privileges!");
      } else {
         try {
            long userMonsterId = params.getLong("user_monster_id");
            long playerIslandId = params.getLong("user_island_id");
            Player friend = (Player)sender.getProperty("friend_object");
            PlayerIsland pi = friend.getIslandByID(playerIslandId);
            PlayerMonster monster = pi.getMonsterByID(userMonsterId);
            int maxLevel = GameSettings.getInt("MAX_MONSTER_LEVEL");
            if (monster.getLevel() >= maxLevel) {
               this.ext.sendErrorResponse("gs_feed_monster", "This monster has entered a higher state of being and no longer requires food", sender);
               return;
            }

            boolean leveledUp = monster.feed(friend, pi);
            this.ext.savePlayerIsland(friend, pi, false);
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            this.send("gs_feed_monster", response, sender);
            response = new SFSObject();
            response.putLong("user_monster_id", userMonsterId);
            response.putInt("times_fed", monster.getTimesFed());
            if (leveledUp) {
               response.putInt("level", monster.getLevel());
               response.putLong("last_collection", monster.getLastCollectedTime());
               response.putInt("collected_coins", monster.getCollectedCoins());
            }

            this.send("gs_admin_update_users_monster", response, sender);
         } catch (Exception var13) {
            Logger.trace(var13, "error during feed monster", "   params : " + params.getDump());
         }

      }
   }

   private void adminFinishUpgradeStructure(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminFinishUpgradeStructure: Error! Trying to invoke admin without privileges!");
      } else {
         long islandId = params.getLong("island_id");
         long playerStructureId = params.getLong("user_structure_id");

         try {
            Player friend = (Player)sender.getProperty("friend_object");
            PlayerIsland pi = friend.getIslandByID(islandId);
            PlayerStructure playerStructure = pi.getStructureByID(playerStructureId);
            if (playerStructure == null) {
               this.ext.sendErrorResponse("gs_admin_finish_upgrade_structure", "Could not find the structure you're trying to finish upgrading", sender);
               return;
            }

            if (playerStructure.getTimeRemaining() > 0L) {
               this.ext.sendErrorResponse("gs_admin_finish_upgrade_structure", "This structure is not yet compelted it's upgrade", sender);
               return;
            }

            if (playerStructure.finishUpgradeStructure() == null) {
               this.ext.sendErrorResponse("gs_admin_finish_upgrade_structure", "No upgrades available", sender);
               return;
            }

            this.ext.savePlayerIsland(friend, pi, false);
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            response.putLong("user_structure_id", playerStructureId);
            response.putSFSObject("user_structure", playerStructure.toSFSObject(pi));
            SFSArray responseVars = new SFSArray();
            response.putSFSArray("properties", responseVars);
            this.send("gs_admin_finish_upgrade_structure", response, sender);
            ISFSObject questEvent = new SFSObject();
            questEvent.putInt("object", playerStructure.getEntityId());
            questEvent.putLong(playerStructure.getStructureType() + "_level", playerStructure.getID());
            questEvent.putUtfString("structure_type", playerStructure.getStructureType());
            this.serverQuestEvent(sender, questEvent);
         } catch (Exception var13) {
            ISFSObject response = new SFSObject();
            response.putBool("success", false);
            response.putLong("user_structure_id", playerStructureId);
            this.send("gs_update_structure", response, sender);
            Logger.trace(var13, "error upgrading structure", "   params : " + params.getDump());
         }

      }
   }

   private void adminStartBaking(User sender, ISFSObject params) {
      long playerStructureId = params.getLong("user_structure_id");
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminStartBaking: Error! Trying to invoke admin without privileges!");
      } else {
         try {
            long playerIslandId = params.getLong("user_island_id");
            Player friend = (Player)sender.getProperty("friend_object");
            PlayerIsland pi = friend.getIslandByID(playerIslandId);
            PlayerStructure playerStructure = pi.getStructureByID(playerStructureId);
            if (playerStructure == null) {
               this.ext.sendErrorResponse("gs_admin_start_baking", "Could not find the structure you're trying to finish upgrading", sender);
               return;
            }

            int foodIndex = params.getInt("food_index");
            PlayerBaking pb = pi.getBakingByStructureId(playerStructureId);
            if (pb != null) {
               Logger.trace("ERROR: Bakery already has baking in progress");
               this.ext.sendErrorResponse("gs_admin_start_baking", "Bakery already has baking in progress", sender);
               return;
            }

            int structureId = playerStructure.getType();
            Structure structure = StructureLookup.get(structureId);
            ISFSObject extra = structure.getExtra();
            if (extra == null) {
               Logger.trace("ERROR: extra is null ");
               this.ext.sendErrorResponse("gs_admin_start_baking", "invalid user_structure_id", sender);
               return;
            }

            ISFSArray foodOptions = extra.getSFSArray("food_options");
            ISFSObject bakingData = foodOptions.getSFSObject(foodIndex);
            int amountOfFood = bakingData.getInt("food");
            int foodOptionId = bakingData.getInt("id");
            SFSObject newBakingData = new SFSObject();
            newBakingData.putLong("island", playerIslandId);
            newBakingData.putLong("user_structure", playerStructureId);
            newBakingData.putInt("food_option_id", foodOptionId);
            newBakingData.putInt("food_count", amountOfFood);
            newBakingData.putLong("started_at", MSMExtension.CurrentDBTime());
            newBakingData.putLong("finished_at", MSMExtension.CurrentDBTime());
            PlayerBaking newBaking = new PlayerBaking(newBakingData);
            pi.addBaking(newBaking);
            this.ext.savePlayerIsland(friend, pi, false);
            ISFSObject response = new SFSObject();
            long serverTime = MSMExtension.CurrentDBTime();
            response.putLong("server_time", serverTime);
            response.putBool("success", true);
            response.putLong("user_structure_id", playerStructureId);
            response.putSFSObject("user_baking", newBaking.toSFSObject());
            this.send("gs_admin_start_baking", response, sender);
         } catch (Exception var24) {
            ISFSObject response = new SFSObject();
            response.putBool("success", false);
            response.putLong("user_structure_id", playerStructureId);
            this.send("gs_admin_start_baking", response, sender);
            Logger.trace(var24, "error starting baking", "   params : " + params.getDump());
         }

      }
   }

   private void adminBakingSpeedUp(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminBakingSpeedUp: Error! Trying to invoke admin without privileges!");
      } else {
         try {
            long userBakingId = params.getLong("user_baking_id");
            long playerIslandId = params.getLong("user_island_id");
            Player friend = (Player)sender.getProperty("friend_object");
            PlayerIsland pi = friend.getIslandByID(playerIslandId);
            PlayerBaking pb = pi.getBakingByID(userBakingId);
            pb.finishBakingNow();
            this.ext.savePlayerIsland(friend, pi, false);
            long finishedAt = pb.getCompletionTime();
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            response.putLong("user_baking_id", userBakingId);
            response.putLong("finished_at", finishedAt);
            this.send("gs_admin_speed_up_baking", response, sender);
         } catch (Exception var13) {
            Logger.trace(var13, "error during baking speedup", "   params : " + params.getDump());
         }

      }
   }

   private void adminStartFuzing(User sender, ISFSObject params) {
      long playerStructureId = params.getLong("structure_id");
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminStartFuzing: Error! Trying to invoke admin without privileges!");
      } else {
         try {
            long playerIslandId = params.getLong("user_island_id");
            Player player = (Player)sender.getProperty("friend_object");
            PlayerIsland island = player.getIslandByID(playerIslandId);
            if (island.getFuzerBuddyByStructureId(playerStructureId) != null) {
               Logger.trace("ERROR: Fuzer already has fuzing in progress");
               this.ext.sendErrorResponse("gs_admin_start_fuzing", "Fuzer already has fuzing in progress", sender);
               return;
            }

            if (PlayerFuzeBuddy.BuddyStructure == null) {
               this.ext.sendErrorResponse("gs_admin_start_fuzing", "Could not find buddy entity", sender);
               return;
            }

            boolean create = params.getBool("create");
            ArrayList<PlayerStructure> fuzerBuddies = new ArrayList();
            Iterator islandStructures = island.getStructures().iterator();

            while(islandStructures.hasNext()) {
               PlayerStructure islandStructure = (PlayerStructure)islandStructures.next();
               if (islandStructure.inFuzer()) {
                  fuzerBuddies.add(islandStructure);
               }
            }

            if (!create && fuzerBuddies.size() != 2) {
               this.ext.sendErrorResponse("gs_admin_start_fuzing", "Incorrect number of buddies in the fuzer, (should be 2)", sender);
               return;
            }

            float colorR = 0.0F;
            float colorY = 0.0F;
            float colorB = 0.0F;
            if (!create) {
               float buddy1R = ((PlayerStructure)fuzerBuddies.get(0)).getR();
               float buddy1Y = ((PlayerStructure)fuzerBuddies.get(0)).getY();
               float buddy1B = ((PlayerStructure)fuzerBuddies.get(0)).getB();
               float buddy2R = ((PlayerStructure)fuzerBuddies.get(1)).getR();
               float buddy2Y = ((PlayerStructure)fuzerBuddies.get(1)).getY();
               float buddy2B = ((PlayerStructure)fuzerBuddies.get(1)).getB();
               colorR = (float)Math.sqrt((double)(buddy1R * buddy1R + buddy2R * buddy2R) / 2.0D);
               colorY = (float)Math.sqrt((double)(buddy1Y * buddy1Y + buddy2Y * buddy2Y) / 2.0D);
               colorB = (float)Math.sqrt((double)(buddy1B * buddy1B + buddy2B * buddy2B) / 2.0D);
            } else {
               colorR = params.getFloat("colorR");
               colorY = params.getFloat("colorY");
               colorB = params.getFloat("colorB");
               boolean isPrimary = false;
               if (colorR == 1.0F && colorY == 0.0F && colorB == 0.0F) {
                  isPrimary = true;
               } else if (colorR == 0.0F && colorY == 1.0F && colorB == 0.0F) {
                  isPrimary = true;
               } else if (colorR == 0.0F && colorY == 0.0F && colorB == 1.0F) {
                  isPrimary = true;
               }

               if (!isPrimary) {
                  colorR = 1.0F;
                  colorY = 0.0F;
                  colorB = 0.0F;
               }
            }

            int fuzeTime = PlayerFuzeBuddy.BuddyStructure.getBuildTimeMs();
            if (ShortenedFuzingEvent.hasTimedEventNow(player, island.getType())) {
               fuzeTime = ShortenedFuzingEvent.getTimedEventFuzeTime(player, fuzeTime, island.getType());
            }

            ISFSObject userFuzingData = new SFSObject();
            userFuzingData.putLong("structure_id", island.getStructureByID(playerStructureId).getID());
            userFuzingData.putDouble("colorR", (double)colorR);
            userFuzingData.putDouble("colorY", (double)colorY);
            userFuzingData.putDouble("colorB", (double)colorB);
            userFuzingData.putLong("started_on", MSMExtension.CurrentDBTime());
            userFuzingData.putLong("finished_on", MSMExtension.CurrentDBTime() + (long)fuzeTime);
            userFuzingData.putBool("create", create);
            PlayerFuzeBuddy playerFuzing = new PlayerFuzeBuddy(userFuzingData);
            island.addFuzeBuddy(playerFuzing);
            this.ext.savePlayerIsland(player, island, false);
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            response.putLong("structure_id", playerStructureId);
            response.putSFSObject("user_fuzing", playerFuzing.getData());
            ISFSArray responseVars = new SFSArray();
            player.addPlayerPropertyData(responseVars, true);
            response.putSFSArray("properties", responseVars);
            this.send("gs_admin_start_fuzing", response, sender);
         } catch (Exception var21) {
            ISFSObject response = new SFSObject();
            response.putBool("success", false);
            response.putLong("structure_id", playerStructureId);
            this.send("gs_start_fuzing", response, sender);
            Logger.trace(var21, "error starting fuzing", "   params : " + params.getDump());
         }

      }
   }

   private void adminFuzingSpeedUp(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminFuzingSpeedUp: Error! Trying to invoke admin without privileges!");
      } else {
         try {
            long userFuzingId = params.getLong("structure_id");
            long playerIslandId = params.getLong("user_island_id");
            Player player = (Player)sender.getProperty("friend_object");
            PlayerIsland island = player.getIslandByID(playerIslandId);
            PlayerFuzeBuddy playerFuzing = island.getFuzeBuddyByID(userFuzingId);
            playerFuzing.finishNow();
            this.ext.savePlayerIsland(player, island, false);
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            response.putLong("structure_id", userFuzingId);
            response.putLong("finished_on", playerFuzing.getCompletionTime());
            ISFSArray responseVars = new SFSArray();
            player.addPlayerPropertyData(responseVars, true);
            response.putSFSArray("properties", responseVars);
            this.send("gs_admin_speed_up_fuzing", response, sender);
         } catch (Exception var12) {
            Logger.trace(var12, "error during fuzing speedup", "   params : " + params.getDump());
         }

      }
   }

   private void adminBreedingSpeedUp(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminBreedingSpeedUp: Error! Trying to invoke admin without privileges!");
      } else {
         try {
            long playerIslandId = params.getLong("user_island_id");
            Player friend = (Player)sender.getProperty("friend_object");
            PlayerIsland pi = friend.getIslandByID(playerIslandId);
            long userBreedingId = 0L;
            if (params.containsKey("user_breeding_id")) {
               userBreedingId = params.getLong("user_breeding_id");
            }

            PlayerBreeding pb = pi.getBreeding(userBreedingId);
            ISFSObject response = new SFSObject();
            boolean success = false;
            if (pb != null) {
               pb.finishBreedingNow();
               this.ext.savePlayerIsland(friend, pi, false);
               long finishedAt = pb.getCompletionTime();
               success = true;
               response.putLong("complete_on", finishedAt);
               response.putLong("user_breeding_id", pb.getID());
            }

            response.putBool("success", success);
            this.send("gs_admin_speed_up_breeding", response, sender);
         } catch (Exception var14) {
            Logger.trace(var14, "error during breeding speedup", "   params : " + params.getDump());
         }

      }
   }

   private void adminRemoveBreeding(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminRemoveBreeding: Error! Trying to invoke admin without privileges!");
      } else {
         try {
            long playerIslandId = params.getLong("user_island_id");
            Player friend = (Player)sender.getProperty("friend_object");
            PlayerIsland pi = friend.getIslandByID(playerIslandId);
            long userBreedingId = 0L;
            if (params.containsKey("user_breeding_id")) {
               userBreedingId = params.getLong("user_breeding_id");
            }

            long userBreedingIdRemoved = pi.removeBreeding(userBreedingId);
            this.ext.savePlayerIsland(friend, pi, false);
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            response.putLong("userBreedingId", userBreedingIdRemoved);
            this.send("gs_admin_remove_breeding", response, sender);
         } catch (Exception var12) {
            Logger.trace(var12, "error during finish breeding", "   params : " + params.getDump());
         }

      }
   }

   private void adminFinishBreeding(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminFinishBreeding: Error! Trying to invoke admin without privileges!");
      } else {
         try {
            Player friend = (Player)sender.getProperty("friend_object");
            long playerIslandId = params.getLong("user_island_id");
            PlayerIsland pi = friend.getIslandByID(playerIslandId);
            if (pi.eggsFull()) {
               this.ext.sendErrorResponse("gs_finish_breeding", "Not enough beds available in nursery", sender);
               return;
            }

            long userBreedingId = 0L;
            if (params.containsKey("user_breeding_id")) {
               userBreedingId = params.getLong("user_breeding_id");
            }

            if (userBreedingId == 0L) {
               userBreedingId = pi.getBusyBreedingStructure();
            }

            if (userBreedingId != 0L) {
               PlayerEgg newEgg = pi.finishBreeding(friend, userBreedingId, true);
               this.ext.savePlayerIsland(friend, pi, false);
               ISFSObject response = new SFSObject();
               response.putSFSObject("user_egg", newEgg.getData());
               response.putLong("user_breeding_id", userBreedingId);
               response.putBool("success", true);
               this.send("gs_admin_finish_breeding", response, sender);
               this.serverQuestEvent(sender, "monster", newEgg.getType());
            } else {
               ISFSObject response = new SFSObject();
               response.putBool("success", false);
               this.send("gs_admin_finish_breeding", response, sender);
            }
         } catch (Exception var11) {
            Logger.trace(var11, "error during finish breeding", "   params : " + params.getDump());
         }

      }
   }

   private void adminClearObstacleSpeedUp(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminClearObstacleSpeedUp : Error! Trying to invoke admin without privileges!");
      } else {
         long playerStructureId = params.getLong("user_structure_id");

         try {
            long playerIslandId = params.getLong("user_island_id");
            Player friend = (Player)sender.getProperty("friend_object");
            PlayerIsland pi = friend.getIslandByID(playerIslandId);
            PlayerStructure playerStructure = pi.getStructureByID(playerStructureId);
            if (playerStructure == null) {
               this.ext.sendErrorResponse("gs_admin_clear_obstacle_speed_up", "Could not find the structure you're trying to finish upgrading", sender);
               return;
            }

            playerStructure.setBuildingCompletedTime(MSMExtension.CurrentDBTime());
            this.ext.savePlayerIsland(friend, pi, false);
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            response.putLong("user_structure_id", playerStructureId);
            ISFSObject property = new SFSObject();
            property.putLong("building_completed", playerStructure.getBuildingCompletedTime());
            SFSArray responseVars = new SFSArray();
            responseVars.addSFSObject(property);
            response.putSFSArray("properties", responseVars);
            this.send("gs_admin_clear_obstacle_speed_up", response, sender);
         } catch (Exception var13) {
            ISFSObject response = new SFSObject();
            response.putBool("success", false);
            response.putLong("user_structure_id", playerStructureId);
            this.send("gs_admin_clear_obstacle_speed_up", response, sender);
            Logger.trace(var13, "error clearing obstacle", "   params : " + params.getDump());
         }

      }
   }

   private void adminHatchingSpeedUp(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminHatchingSpeedUp: Error! Trying to invoke admin without privileges!");
      } else {
         try {
            long userEggId = params.getLong("user_egg_id");
            long playerIslandId = params.getLong("user_island_id");
            Player friend = (Player)sender.getProperty("friend_object");
            PlayerIsland pi = friend.getIslandByID(playerIslandId);
            PlayerEgg playerEgg = pi.getEgg(userEggId);
            playerEgg.finishHatchingNow();
            this.ext.savePlayerIsland(friend, pi, false);
            long finishedAt = playerEgg.getCompletionTime();
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            response.putLong("user_egg_id", userEggId);
            response.putLong("hatches_on", finishedAt);
            this.send("gs_admin_speed_up_hatching", response, sender);
         } catch (Exception var13) {
            Logger.trace(var13, "error during hatching speedup", "   params : " + params.getDump());
         }

      }
   }

   private void adminStructureSpeedUp(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminStructureSpeedUp: Error! Trying to invoke admin without privileges!");
      } else {
         long playerStructureId = params.getLong("user_structure_id");

         try {
            long playerIslandId = params.getLong("user_island_id");
            Player friend = (Player)sender.getProperty("friend_object");
            PlayerIsland pi = friend.getIslandByID(playerIslandId);
            PlayerStructure playerStructure = pi.getStructureByID(playerStructureId);
            if (playerStructure == null) {
               this.ext.sendErrorResponse("gs_admin_speed_up_structure", "Could not find the structure you're trying to finish upgrading", sender);
               return;
            }

            playerStructure.setBuildingCompletedTime(MSMExtension.CurrentDBTime());
            this.ext.savePlayerIsland(friend, pi, false);
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            response.putLong("user_structure_id", playerStructureId);
            SFSArray responseVars = new SFSArray();
            ISFSObject property = new SFSObject();
            property.putLong("building_completed", playerStructure.getBuildingCompletedTime());
            responseVars.addSFSObject(property);
            response.putSFSArray("properties", responseVars);
            this.send("gs_admin_speed_up_structure", response, sender);
         } catch (Exception var13) {
            ISFSObject response = new SFSObject();
            response.putBool("success", false);
            response.putLong("user_structure_id", playerStructureId);
            this.send("gs_admin_speed_up_structure", response, sender);
            Logger.trace(var13, "error speeding up structure", "   params : " + params.getDump());
         }

      }
   }

   private void adminGetUserQuestInfo(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminGetUserQuestInfo: Error! Trying to invoke admin without privileges!");
      } else {
         long playerId = params.getLong("user_bbb_id");

         try {
            Logger.trace("adminGetUserQuestInfo: playerId: " + playerId);
            Player player = this.ext.adminCreatePlayerByBBBID(playerId);
            this.ext.sendQuests(player, sender, true);
         } catch (Exception var7) {
            ISFSObject response = new SFSObject();
            response.putBool("success", false);
            Logger.trace(var7);
         }

      }
   }

   private void adminCompleteUserQuest(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminCompleteUserQuest: Error! Trying to invoke admin without privileges!");
      } else {
         long playerId = params.getLong("user_bbb_id");
         long questId = params.getLong("user_quest_id");
         Logger.trace("adminCompleteUserQuest: playerId: " + playerId);
         Logger.trace("adminCompleteUserQuest: questId: " + questId);
         ISFSObject response = new SFSObject();
         response.putLong("user_bbb_id", playerId);
         response.putLong("user_quest_id", questId);
         response.putInt("admin_quest_msg", 1);

         try {
            ISFSArray resultArray = this.adminCompleteUserQuest(playerId, questId, response);
            if (resultArray.size() != 0) {
               response.putBool("success", true);
               response.putSFSArray("result", resultArray);
            } else {
               response.putBool("success", false);
            }
         } catch (Exception var9) {
            response.putBool("success", false);
            Logger.trace(var9);
         }

         this.send("gs_quest", response, sender);
      }
   }

   private ISFSArray adminCompleteUserQuest(long bbbId, long userQuestId, ISFSObject result) throws Exception {
      ISFSArray resultArray = new SFSArray();
      Player player = this.ext.adminCreatePlayerByBBBID(bbbId);
      PlayerQuest quest = player.getQuestByUid(userQuestId);
      if (quest != null && this.adminConfirmGoalsComplete(quest, player, result)) {
         String previousStatus = quest.getStatus();
         quest.adminMarkStatusComplete();
         this.processNewQuestStatus((User)null, player, quest, true, previousStatus, this.ext.getDB(), resultArray);
      }

      this.ext.savePlayerQuests(player);
      return resultArray;
   }

   private void adminGiveMeShit(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminGiveMeShit: Error! Trying to invoke admin without privileges!");
      } else {
         try {
            long playerId = params.getLong("user_id");
            Player player = this.ext.adminCreatePlayerByBBBID(playerId);
            String resource = params.getUtfString("resource");
            int amount = params.getInt("amount");
            ISFSObject response = new SFSObject();
            ISFSArray responseVars = new SFSArray();
            String sql;
            Object[] args;
            if (resource.equals("coins")) {
               player.adjustCoins((User)null, (GameStateHandler)null, amount);
               sql = "UPDATE users SET coins=? WHERE bbb_id=?";
               args = new Object[]{player.getActualCoins(), playerId};
               this.ext.getDB().update(sql, args);
               player.addPlayerPropertyData(responseVars, EnumSet.of(Player.PROPERTY.COINS));
            } else if (resource.equals("diamonds")) {
               player.adjustDiamonds((User)null, (GameStateHandler)null, amount);
               sql = "UPDATE users SET diamonds=? WHERE bbb_id=?";
               args = new Object[]{player.getActualDiamonds(), playerId};
               this.ext.getDB().update(sql, args);
               player.addPlayerPropertyData(responseVars, EnumSet.of(Player.PROPERTY.DIAMONDS));
            } else if (resource.equals("ethereal_currency")) {
               player.adjustEthCurrency((User)null, (GameStateHandler)null, amount);
               sql = "UPDATE users SET ethereal_currency=? WHERE bbb_id=?";
               args = new Object[]{player.getActualEthCurrency(), playerId};
               this.ext.getDB().update(sql, args);
               player.addPlayerPropertyData(responseVars, EnumSet.of(Player.PROPERTY.ETH_CURRENCY));
            } else if (resource.equals("food")) {
               player.adjustFood((User)null, (GameStateHandler)null, amount);
               sql = "UPDATE users SET food=? WHERE bbb_id=?";
               args = new Object[]{player.getActualFood(), playerId};
               this.ext.getDB().update(sql, args);
               player.addPlayerPropertyData(responseVars, EnumSet.of(Player.PROPERTY.FOOD));
            }

            response.putSFSArray("properties", responseVars);
            this.send("gs_admin_update_properties", response, sender);
         } catch (Exception var12) {
            ISFSObject response = new SFSObject();
            response.putBool("success", false);
            this.send("gs_admin_give_me_shit", response, sender);
            Logger.trace(var12, "error giving user shit", "   params : " + params.getDump());
         }

      }
   }

   private void adminFlipMonster(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminFlipMonster: Error! Trying to invoke admin without privileges!");
      } else {
         try {
            long islandId = params.getLong("user_island_id");
            Player friend = (Player)sender.getProperty("friend_object");
            PlayerIsland island = friend.getIslandByID(islandId);
            long playerMonsterId = params.getLong("user_monster_id");
            PlayerMonster playerMonster = island.getMonsterByID(playerMonsterId);
            playerMonster.toggleFlip();
            this.ext.savePlayerIsland(friend, island, false);
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            this.send("gs_flip_monster", response, sender);
            response = new SFSObject();
            response.putLong("user_monster_id", playerMonster.getID());
            response.putInt("flip", playerMonster.getFlip());
            this.send("gs_admin_update_users_monster", response, sender);
         } catch (Exception var11) {
            Logger.trace(var11, "error during admin flip monster", "   params : " + params.getDump());
         }

      }
   }

   private void adminFlipStructure(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminFlipStructure: Error! Trying to invoke admin without privileges!");
      } else {
         try {
            long playerStructureId = params.getLong("user_structure_id");
            long islandId = params.getLong("user_island_id");
            Player friend = (Player)sender.getProperty("friend_object");
            PlayerIsland pi = friend.getIslandByID(islandId);
            PlayerStructure playerStructure = pi.getStructureByID(playerStructureId);
            playerStructure.toggleFlip();
            this.ext.savePlayerIsland(friend, pi, false);
            ISFSObject response = new SFSObject();
            response.putBool("success", true);
            this.send("gs_flip_structure", response, sender);
            response = new SFSObject();
            response.putLong("structure_id", playerStructure.getID());
            SFSArray responseVars = new SFSArray();
            ISFSObject property = new SFSObject();
            property.putInt("flip", playerStructure.getFlip());
            responseVars.addSFSObject(property);
            response.putSFSArray("properties", responseVars);
            this.send("gs_admin_update_users_structure", response, sender);
         } catch (Exception var13) {
            Logger.trace(var13, "error during flip structure", "   params : " + params.getDump());
         }

      }
   }

   private void adminKickUser(User sender, ISFSObject params) {
      String userGameId = params.getUtfString("user_game_id");
      Logger.trace("\n\n\nTry to kick " + userGameId + "\n\n\n");
      if (sender.getPrivilegeId() != 3) {
         Logger.trace("adminKickUser: Error! Trying to invoke admin without privileges!");
      } else {
         User user = this.ext.getParentZone().getUserByName(userGameId);
         Logger.trace("Found user " + userGameId + "... Saving and Kicking.");
         this.ext.savePlayer(user);
         user.disconnect(ClientDisconnectionReason.KICK);
      }
   }

   private void getGeneDB(User sender, ISFSObject params) {
      try {
         ISFSObject response = StaticData.getStaticData(sender, params, GeneLookup.getInstance());
         this.ext.multiSend("db_gene", response, sender, GeneLookup.getInstance().getCacheName());
      } catch (Exception var4) {
         Logger.trace(var4);
      }

   }

   private void getVersionData(User sender, ISFSObject params) {
      ISFSObject response = new SFSObject();
      ISFSArray sfsarray = new SFSArray();
      Iterator itr = VersionData.Instance().clientToServerVer.entrySet().iterator();

      while(itr.hasNext()) {
         Entry<VersionInfo, VersionInfo> cur = (Entry)itr.next();
         SFSObject o = new SFSObject();
         o.putUtfString("minClientVer", ((VersionInfo)cur.getKey()).toString());
         o.putUtfString("serverVer", ((VersionInfo)cur.getValue()).toString());
         sfsarray.addSFSObject(o);
      }

      response.putSFSArray("version_data", sfsarray);
      this.send("db_versions", response, sender);
   }

   private void getMonsterDB(User sender, ISFSObject params) {
      try {
         ISFSObject response = StaticData.getStaticData(sender, params, MonsterLookup.getInstance());
         this.ext.multiSend("db_monster", response, sender, MonsterLookup.getInstance().getCacheName());
      } catch (Exception var4) {
         Logger.trace(var4);
      }

   }

   private void getStructureDB(User sender, ISFSObject params) {
      try {
         ISFSObject response = StaticData.getStaticData(sender, params, StructureLookup.getInstance());
         this.ext.multiSend("db_structure", response, sender, StructureLookup.getInstance().getCacheName());
      } catch (Exception var4) {
         Logger.trace(var4);
      }

   }

   private void getIslandDB(User sender, ISFSObject params) {
      try {
         ISFSObject response = new SFSObject();
         long serverTime = MSMExtension.CurrentDBTime();
         response.putLong("server_time", serverTime);
         Long lastUpdate = params.getLong("last_updated");
         String clientPlatform = (String)sender.getProperty("client_platform");
         String clientSubplatform = (String)sender.getProperty("client_subplatform");
         VersionInfo currentClientVer = new VersionInfo((String)sender.getProperty("client_version"));
         VersionInfo lastUpdatedClient = new VersionInfo((String)sender.getProperty("last_update_version"));
         VersionInfo maxSupportedServerVersion = VersionData.Instance().getMaxServerVersionFromClientVersion(currentClientVer);
         if (lastUpdate == null || lastUpdate > serverTime) {
            lastUpdate = 0L;
         }

         if (currentClientVer.compareTo(lastUpdatedClient) > 0) {
            lastUpdate = 0L;
         }

         long newestUpdate = 0L;
         long currentUpdate = 0L;
         ISFSArray islands = new SFSArray();
         Iterator var17 = IslandLookup.getInstance().entries().iterator();

         while(var17.hasNext()) {
            Island s = (Island)var17.next();
            if (s.supportedByClient(clientPlatform, clientSubplatform, maxSupportedServerVersion)) {
               currentUpdate = s.lastChanged();
               if (currentUpdate > lastUpdate) {
                  islands.addSFSObject(Island.getValidIslandDataForUser(s, clientPlatform, clientSubplatform, maxSupportedServerVersion));
               }

               if (currentUpdate > newestUpdate) {
                  newestUpdate = currentUpdate;
               }
            }
         }

         if (islands.size() > 0) {
            response.putSFSArray("islands_data", islands);
         }

         ISFSArray islandThemes = new SFSArray();
         Iterator var23 = IslandThemeLookup.themes.entrySet().iterator();

         while(var23.hasNext()) {
            Entry<Integer, IslandTheme> pairs = (Entry)var23.next();
            IslandTheme it = (IslandTheme)pairs.getValue();
            if (it.supportedByClient(clientPlatform, clientSubplatform, maxSupportedServerVersion)) {
               islandThemes.addSFSObject(it.getData());
            }
         }

         if (islandThemes.size() > 0) {
            response.putSFSArray("island_theme_data", islandThemes);
         }

         response.putLong("last_updated", Math.min(serverTime, newestUpdate));
         this.send("db_island", response, sender);
      } catch (Exception var21) {
         Logger.trace(var21);
      }

   }

   private void getLevelDB(User sender, ISFSObject params) {
      try {
         ISFSObject response = StaticData.getStaticData(sender, params, LevelLookup.getInstance());
         this.ext.multiSend("db_level", response, sender, LevelLookup.getInstance().getCacheName());
      } catch (Exception var4) {
         Logger.trace(var4);
      }

   }

   private void getScratchOffsDB(User sender, ISFSObject params) {
      try {
         ISFSObject response = new SFSObject();
         long serverTime = MSMExtension.CurrentDBTime();
         response.putLong("server_time", serverTime);
         VersionInfo currentClientVer = new VersionInfo((String)sender.getSession().getProperty("client_version"));
         VersionInfo maxSupportedServerVersion = VersionData.Instance().getMaxServerVersionFromClientVersion(currentClientVer);
         ISFSArray ticketPrizes = new SFSArray();
         Iterator var9 = PrizeLookup.getScratchOffs().iterator();

         while(var9.hasNext()) {
            ISFSObject scratchPrize = (ISFSObject)var9.next();
            if (this.ext.scratchOffPrizeSupportedByServerVer(scratchPrize, maxSupportedServerVersion)) {
               ticketPrizes.addSFSObject(scratchPrize);
            }
         }

         response.putSFSArray("scratch_offs", ticketPrizes);
         ISFSArray spinPrizes = new SFSArray();
         Iterator var13 = PrizeLookup.getSpinWheelPrizes().iterator();

         ISFSObject spinPrize;
         while(var13.hasNext()) {
            spinPrize = (ISFSObject)var13.next();
            if (this.ext.scratchOffPrizeSupportedByServerVer(spinPrize, maxSupportedServerVersion)) {
               spinPrizes.addSFSObject(spinPrize);
            }
         }

         var13 = PrizeLookup.getSpinWheelJackpotPrizes().iterator();

         while(var13.hasNext()) {
            spinPrize = (ISFSObject)var13.next();
            if (this.ext.scratchOffPrizeSupportedByServerVer(spinPrize, maxSupportedServerVersion)) {
               spinPrizes.addSFSObject(spinPrize);
            }
         }

         response.putSFSArray("spin_wheel_prizes", spinPrizes);
         this.send("db_scratch_offs", response, sender);
      } catch (Exception var12) {
         Logger.trace(var12);
      }

   }

   private void getStoreDB(User sender, ISFSObject params) {
      try {
         ISFSObject response = new SFSObject();
         long serverTime = MSMExtension.CurrentDBTime();
         response.putLong("server_time", serverTime);
         response.putLong("last_updated", serverTime);
         this.send("db_store", response, sender);
      } catch (Exception var6) {
         Logger.trace(var6);
      }

   }

   private void getStoreDBv2(User sender, ISFSObject params) {
      try {
         VersionInfo currentClientVer = new VersionInfo((String)sender.getProperty("client_version"));
         VersionInfo maxSupportedServerVersion = VersionData.Instance().getMaxServerVersionFromClientVersion(currentClientVer);
         Player player = (Player)sender.getProperty("player_object");
         ISFSObject response = new SFSObject();
         ISFSArray storeCurrency = new SFSArray();
         Iterator var8 = StoreCurrencyLookup.currency.entrySet().iterator();

         while(var8.hasNext()) {
            Entry<Integer, ISFSObject> pairs = (Entry)var8.next();
            ISFSObject currencyData = (ISFSObject)pairs.getValue();
            if (StoreCurrencyLookup.storeCurrencySupportedByServerVersion(currencyData, maxSupportedServerVersion)) {
               storeCurrency.addSFSObject(currencyData);
            }
         }

         if (storeCurrency.size() > 0) {
            response.putSFSArray("store_currency_data", storeCurrency);
         }

         ISFSArray storeGroups = new SFSArray();
         Iterator var19 = StoreGroupsLookup.groups.entrySet().iterator();

         while(var19.hasNext()) {
            Entry<Integer, ISFSObject> pairs = (Entry)var19.next();
            ISFSObject groupData = (ISFSObject)pairs.getValue();
            if (StoreGroupsLookup.storeGroupSupportedByServerVersion(groupData, maxSupportedServerVersion)) {
               storeGroups.addSFSObject(groupData);
            }
         }

         if (storeGroups.size() > 0) {
            response.putSFSArray("store_group_data", storeGroups);
         }

         ISFSArray storeItems = new SFSArray();
         Iterator var22 = StoreItemsLookup.items.entrySet().iterator();

         while(true) {
            ISFSObject itemData;
            do {
               if (!var22.hasNext()) {
                  if (storeItems.size() > 0) {
                     response.putSFSArray("store_item_data", storeItems);
                  }

                  this.send("db_store_v2", response, sender);
                  return;
               }

               Entry<Integer, ISFSObject> pairs = (Entry)var22.next();
               itemData = (ISFSObject)pairs.getValue();
            } while(itemData.getInt("exclude") == 1);

            boolean skipCurrency = false;
            ArrayList<CurrencyGroup> cg = UserGroup.getGroup(UserGroup.GroupType.CurrencyExclude);
            Iterator var15 = cg.iterator();

            while(var15.hasNext()) {
               CurrencyGroup g = (CurrencyGroup)var15.next();
               if (player.memberOfGroup(g.id()) && g.excludesCurrency(itemData.getUtfString("item_name"))) {
                  skipCurrency = true;
                  break;
               }
            }

            if (!skipCurrency && StoreItemsLookup.storeItemSupportedByServerVersion(itemData, maxSupportedServerVersion)) {
               storeItems.addSFSObject(itemData);
            }
         }
      } catch (Exception var17) {
         Logger.trace(var17);
      }
   }

   private void getStickerDB(User sender, ISFSObject params) {
      try {
         ISFSObject response = StaticData.getStaticData(sender, params, StickerLookup.getInstance());
         this.ext.multiSend("gs_sticker", response, sender, StickerLookup.getInstance().getCacheName());
      } catch (Exception var4) {
         Logger.trace(var4);
      }

   }

   private void getAttunerGeneDB(User sender, ISFSObject params) {
      try {
         ISFSObject response = StaticData.getStaticData(sender, params, AttunerGeneLookup.getInstance());
         this.ext.multiSend("db_attuner_gene", response, sender, AttunerGeneLookup.getInstance().getCacheName());
      } catch (Exception var4) {
         Logger.trace(var4);
      }

   }

   private void testTypes(User sender, ISFSObject params) {
      ISFSObject response = new SFSObject();
      response.putBool("bool", true);
      response.putFloat("float", 23.0F);
      response.putInt("int", 42);
      response.putLong("long", 1337L);
      response.putUtfString("string", "foobar");
      response.putUtfStringArray("string_array", Arrays.asList("one", "two", "three"));
      response.putIntArray("int_array", Arrays.asList(1, 2, 3));
      response.putSFSObject("object", new SFSObject());
      response.putSFSArray("goals", SFSArray.newFromJsonData("[{\"object\": [67,805], \"eval\" : \"==\"}]"));
      this.send("test_types", response, sender);
   }

   protected void retroActiveQuests(Player player, User user) {
      try {
         if (player.hasQuestGoal("level")) {
            for(int i = 1; i <= player.getLevel(); ++i) {
               ISFSObject qe = new SFSObject();
               qe.putInt("level", i);
               qe.putBool("server_generated", true);
               qe.putBool("nosend", true);
               this.ext.handleClientRequest("gs_quest_event", user, qe);
            }
         }

         if (player.getLevel() >= 7 && player.hasActiveQuest(17) && !player.questComplete(17)) {
            ISFSObject qe = new SFSObject();
            qe.putInt("object", 2);
            qe.putBool("server_generated", true);
            qe.putBool("nosend", true);
            this.ext.handleClientRequest("gs_quest_event", user, qe);
         }

         int BATT_TUT_SECOND_BATTLE_LOSS = true;
         int BATT_TUT_START_TRAIN_MONST = true;
         VersionInfo clientVer = new VersionInfo((String)user.getProperty("client_version"));
         if (clientVer.compareTo(GameSettings.get("BATTLE_TUT_V2_CLIENT_VERSION")) >= 0 && player.hasActiveQuest(361) && !player.questComplete(361) && !player.hasQuest(362)) {
            player.addQuest(PlayerQuest.getInitialSFSObject(player.getNextQuestIndex(), 362, (int)player.getPlayerId(), "false", true, false));
         }
      } catch (Exception var6) {
         Logger.trace(var6, "Error checking retroactive quests");
      }

   }

   private void getQuestInfo(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      this.retroActiveQuests(player, sender);
      this.ext.sendQuests(player, sender, false);
   }

   private void getReplacementStoreStructures(User sender, ISFSObject params) {
      ISFSObject response = new SFSObject();
      response.putSFSArray("store_replacement_data", StoreReplacements.getData());
      this.send("gs_store_replacements", response, sender);
   }

   private void getTimedEventInfo(User sender, ISFSObject params) {
      VersionInfo clientVersion = new VersionInfo((String)sender.getProperty("client_version"));
      ISFSObject response = new SFSObject();
      response.putSFSArray("timed_event_list", TimedEventManager.instance().activeEventsInNearFuture(clientVersion));
      this.send("gs_timed_events", response, sender);
   }

   private void getRareMonsterData(User sender, ISFSObject params) {
      ISFSObject response = new SFSObject();
      response.putSFSArray("rare_monster_data", MonsterCommonToRareMapping.sfsData());
      this.send("gs_rare_monster_data", response, sender);
   }

   private void getEpicMonsterData(User sender, ISFSObject params) {
      ISFSObject response = new SFSObject();
      response.putSFSArray("epic_monster_data", MonsterCommonToEpicMapping.sfsData());
      this.send("gs_epic_monster_data", response, sender);
   }

   private void getFlexEggDefs(User sender, ISFSObject params) {
      try {
         ISFSObject response = StaticData.getStaticData(sender, params, MonsterFlexEggDefLookup.getInstance());
         String cache = MonsterFlexEggDefLookup.getInstance().getCacheName();
         this.ext.multiSend("db_flexeggdefs", response, sender, cache);
      } catch (Exception var5) {
         Logger.trace(var5);
      }

   }

   private void getFlipBoardsData(User sender, ISFSObject params) {
      ISFSObject response = new SFSObject();
      long serverTime = MSMExtension.CurrentDBTime();
      response.putLong("server_time", serverTime);
      Long lastUpdate = params.getLong("last_updated");
      if (lastUpdate == null || lastUpdate > serverTime) {
         lastUpdate = 0L;
      }

      long newestUpdate = 0L;
      long currentUpdate = 0L;
      ISFSArray flipBoards = new SFSArray();
      Iterator itr2 = FlipBoardLookup.iterator();

      while(itr2.hasNext()) {
         SFSObject board = (SFSObject)itr2.next();
         currentUpdate = board.getLong("last_changed");
         if (currentUpdate > lastUpdate) {
            flipBoards.addSFSObject(board);
         }

         if (currentUpdate > newestUpdate) {
            newestUpdate = currentUpdate;
         }
      }

      if (flipBoards.size() > 0) {
         response.putSFSArray("flip_boards", flipBoards);
      }

      response.putLong("last_updated", Math.min(serverTime, newestUpdate));
      this.send("gs_flip_boards", response, sender);
   }

   private void getFlipLevelsData(User sender, ISFSObject params) {
      ISFSObject response = new SFSObject();
      long serverTime = MSMExtension.CurrentDBTime();
      response.putLong("server_time", serverTime);
      Long lastUpdate = params.getLong("last_updated");
      if (lastUpdate == null || lastUpdate > serverTime) {
         lastUpdate = 0L;
      }

      long newestUpdate = 0L;
      long currentUpdate = 0L;
      ISFSArray flipLevels = new SFSArray();
      Iterator itr = FlipLevelLookup.getInstance().iterator();

      while(itr.hasNext()) {
         SFSObject lvl = (SFSObject)itr.next();
         currentUpdate = lvl.getLong("last_changed");
         if (currentUpdate > lastUpdate) {
            flipLevels.addSFSObject(lvl);
         }

         if (currentUpdate > newestUpdate) {
            newestUpdate = currentUpdate;
         }
      }

      if (flipLevels.size() > 0) {
         response.putSFSArray("flip_levels", flipLevels);
      }

      response.putLong("last_updated", Math.min(serverTime, newestUpdate));
      this.send("gs_flip_levels", response, sender);
   }

   private void getMonsterIsland2IslandMapData(User sender, ISFSObject params) {
      ISFSObject response = new SFSObject();
      ISFSArray island2islandMapping = MonsterIslandToIslandMapping.sfsData();
      ISFSArray prunedMapping = new SFSArray();

      for(int i = 0; i < island2islandMapping.size(); ++i) {
         ISFSObject mapping = island2islandMapping.getSFSObject(i);
         int source = -1;
         int dest = -1;
         if (mapping.containsKey("source_island")) {
            source = mapping.getInt("source_island");
         }

         if (mapping.containsKey("dest_island")) {
            dest = mapping.getInt("dest_island");
         }

         String clientPlatform = (String)sender.getProperty("client_platform");
         String clientSubplatform = (String)sender.getProperty("client_subplatform");
         VersionInfo currentClientVer = new VersionInfo((String)sender.getSession().getProperty("client_version"));
         VersionInfo maxSupportedServerVersion = VersionData.Instance().getMaxServerVersionFromClientVersion(currentClientVer);
         if ((source == 0 || IslandLookup.get(source) != null && IslandLookup.get(dest) != null) && (source == 0 || IslandLookup.get(source).supportedByClient(clientPlatform, clientSubplatform, maxSupportedServerVersion) && IslandLookup.get(dest).supportedByClient(clientPlatform, clientSubplatform, maxSupportedServerVersion))) {
            prunedMapping.addSFSObject(mapping);
         }
      }

      response.putSFSArray("monster_island_2_island_data", prunedMapping);
      this.send("gs_monster_island_2_island_data", response, sender);
   }

   private void getEntityAltCostData(User sender, ISFSObject params) {
      ISFSObject response = new SFSObject();
      response.putSFSArray("entity_alt_data", EntityAltCostLookup.sfsData());
      this.send("gs_entity_alt_cost_data", response, sender);
   }

   private void getCantBreeds(User sender, ISFSObject params) {
      ISFSObject response = new SFSObject();
      response.putIntArray("monsterIds", MonsterLookup.cantBeBredMonsters);
      this.send("gs_cant_breed", response, sender);
   }

   private void getPlayerInfo(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      if (sender.containsProperty("client_version")) {
         String clientVer = (String)sender.getProperty("client_version");
         player.updateLastClientVersion(clientVer);
      }

      this.ext.sendPlayerState(player, sender);
      ISFSObject s0 = player.getData().getSFSObject("pvpSeason0");
      ISFSObject s1 = player.getData().getSFSObject("pvpSeason1");
      if (s0 != null) {
         params.putSFSObject("pvpSeason0", s0);
      }

      if (s1 != null) {
         params.putSFSObject("pvpSeason1", s1);
      }

      this.getFriends(sender, params);
   }

   private void requestNextRelicReset(User sender, ISFSObject params) {
      ISFSObject response = new SFSObject();
      ISFSArray responseVars = new SFSArray();
      Player player = (Player)sender.getProperty("player_object");
      player.updateDailyRelicCount();
      player.addPlayerPropertyData(responseVars, EnumSet.of(Player.PROPERTY.NEXT_RELIC_RESET, Player.PROPERTY.DAILY_RELICS_COUNT, Player.PROPERTY.RELIC_DIAMOND_COST));
      response.putSFSArray("properties", responseVars);
      this.send("gs_update_properties", response, sender);
   }

   private void collectDailyReward(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      player.collectPendingDailyReward();
      ISFSObject response = new SFSObject();
      ISFSArray responseVars = new SFSArray();
      player.addPlayerPropertyData(responseVars, false);
      response.putSFSArray("properties", responseVars);
      this.send("gs_collect_daily_reward", response, sender);
   }

   private void collectDailyCurrencyPack(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      if (player.collectDailyCurrencyPack(sender, this)) {
         ISFSObject response = new SFSObject();
         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putBool("success", true);
         response.putSFSArray("properties", responseVars);
         response.putSFSObject("daily_currency_pack", player.getPurchasedDailyCurrencyPack().toSFSObject());
         this.send("gs_collect_daily_currency_pack", response, sender);
      } else {
         this.ext.sendErrorResponse("gs_collect_daily_currency_pack", "Failed to collect daily currency pack", sender);
      }

   }

   private void refreshDailyCurrencyPack(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      player.refreshDailyCurrencyPack();
      ISFSObject response = new SFSObject();
      response.putSFSObject("daily_currency_pack", player.getPurchasedDailyCurrencyPack().toSFSObject());
      this.send("gs_refresh_daily_currency_pack", response, sender);
   }

   private boolean adminConfirmGoalsComplete(PlayerQuest quest, Player player, ISFSObject result) {
      ISFSArray goals = quest.getGoals();

      for(int i = 0; i < goals.size(); ++i) {
         ISFSObject goal = goals.getSFSObject(i);
         int islandId;
         if (goal.containsKey("level")) {
            islandId = goal.getInt("level");
            String evaluator = goal.getUtfString("eval");
            if (!PlayerQuest.Evaluate(evaluator, player.getLevel(), islandId)) {
               result.putUtfString("message", "User does not have requisite conditions: does not have required level for : " + goals.toJson());
               return false;
            }
         }

         if (goal.containsKey("island")) {
            islandId = goal.getInt("island");
            boolean hasIsland = false;
            Iterator itr = player.getIslands().iterator();

            while(itr.hasNext()) {
               PlayerIsland p = (PlayerIsland)itr.next();
               if (p.getIndex() == islandId) {
                  hasIsland = true;
                  break;
               }
            }

            if (!hasIsland) {
               result.putUtfString("message", "User does not have requisite conditions: does not have required island mentioned in quest: " + goals.toJson());
               return false;
            }
         }
      }

      return true;
   }

   private void getBattleCampaignDB(User sender, ISFSObject params) {
      try {
         ISFSObject response = StaticData.getStaticData(sender, params, BattleCampaignLookup.instance);
         this.ext.multiSend("db_battle", response, sender, BattleCampaignLookup.instance.getCacheName());
      } catch (Exception var4) {
         Logger.trace(var4);
      }

   }

   private void getBattleLevelsDB(User sender, ISFSObject params) {
      try {
         ISFSObject response = StaticData.getStaticData(sender, params, BattleLevelLookup.instance);
         this.ext.multiSend("db_battle_levels", response, sender, BattleLevelLookup.instance.getCacheName());
      } catch (Exception var4) {
         Logger.trace(var4);
      }

   }

   private void getBattleMonsterTrainingDB(User sender, ISFSObject params) {
      try {
         ISFSObject response = StaticData.getStaticData(sender, params, BattleMonsterTrainingLookup.instance);
         this.ext.multiSend("db_battle_monster_training", response, sender, BattleMonsterTrainingLookup.instance.getCacheName());
      } catch (Exception var4) {
         Logger.trace(var4);
      }

   }

   private void getBattleMonsterActionsDB(User sender, ISFSObject params) {
      try {
         ISFSObject response = StaticData.getStaticData(sender, params, BattleMonsterActionLookup.instance);
         this.ext.multiSend("db_battle_monster_actions", response, sender, BattleMonsterActionLookup.instance.getCacheName());
      } catch (Exception var4) {
         Logger.trace(var4);
      }

   }

   private void getBattleMonsterStatsDB(User sender, ISFSObject params) {
      try {
         ISFSObject response = StaticData.getStaticData(sender, params, BattleMonsterStatLookup.instance);
         this.ext.multiSend("db_battle_monster_stats", response, sender, BattleMonsterStatLookup.instance.getCacheName());
      } catch (Exception var4) {
         Logger.trace(var4);
      }

   }

   private void getBattleMusicDB(User sender, ISFSObject params) {
      try {
         ISFSObject response = StaticData.getStaticData(sender, params, BattleMusicLookup.instance);
         this.ext.multiSend("db_battle_music", response, sender, BattleMusicLookup.instance.getCacheName());
      } catch (Exception var4) {
         Logger.trace(var4);
      }

   }

   private void getCostumeDB(User sender, ISFSObject params) {
      try {
         ISFSObject response = StaticData.getStaticData(sender, params, CostumeLookup.instance);
         this.ext.multiSend("db_costumes", response, sender, CostumeLookup.instance.getCacheName());
      } catch (Exception var4) {
         Logger.trace(var4);
      }

   }

   private void sendMonsterToBattleIsland(User sender, ISFSObject params, boolean admin) {
      try {
         Player player = null;
         PlayerIsland sourceIsland = null;
         if (admin) {
            if (sender.getPrivilegeId() != 3) {
               Logger.trace("sendMonsterHome: Error! Trying to invoke admin without privileges!");
               return;
            }

            player = (Player)sender.getProperty("friend_object");
            sourceIsland = player.getIslandByID(params.getLong("user_island_id"));
         } else {
            player = (Player)sender.getProperty("player_object");
            sourceIsland = player.getActiveIsland();
         }

         long teleportableMonsterId = params.getLong("user_monster_id");
         PlayerMonster teleportableMonster = sourceIsland.getMonsterByID(teleportableMonsterId);
         int minLevel = 5;
         if (teleportableMonster.getLevel() < minLevel) {
            return;
         }

         long sentIsland = params.getLong("user_island_id");
         if (sourceIsland.getID() != sentIsland || sourceIsland.isTribalIsland()) {
            return;
         }

         PlayerIsland goalIsland = player.getIslandByIslandIndex(20);
         if (goalIsland == null) {
            params.putInt("island_id", 20);
            params.putBool("no_change_island", true);
            if (admin) {
               this.adminBuyIsland(sender, params);
            } else {
               this.buyIsland(sender, params);
            }

            goalIsland = player.getIslandByIslandIndex(20);
         }

         ISFSObject response = new SFSObject();
         response.putLong("user_monster_id", teleportableMonsterId);
         int monsterType = teleportableMonster.getType();
         int baseMonsterType = MonsterIslandToIslandMapping.monsterSourceGivenAnyIsland(monsterType);
         if (baseMonsterType > 0) {
            monsterType = baseMonsterType;
         }

         long destNurseryId = this.transferEgg(sender, player, teleportableMonster, monsterType, goalIsland, admin);
         if (destNurseryId == 0L) {
            response.putBool("has_egg", true);
            response.putUtfString("message", "NOTIFICATION_BATTLE_NURSERY_FULL");
            response.putBool("success", false);
         } else {
            Collection<Integer> unlockedCostumes = null;
            PlayerCostumeState pcs = player.getCostumes();
            MonsterCostumeState mcs = teleportableMonster.getCostumeState();
            Iterator var21 = mcs.getPurchased().iterator();

            while(var21.hasNext()) {
               int costumeId = (Integer)var21.next();
               if (!pcs.isCostumeUnlocked(costumeId)) {
                  CostumeData costumeData = CostumeLookup.get(costumeId);
                  if (costumeData.unlocksOnTeleport()) {
                     pcs.unlockCostume(costumeId);
                     if (unlockedCostumes == null) {
                        unlockedCostumes = new ArrayList();
                     }

                     unlockedCostumes.add(costumeId);
                  }
               }
            }

            if (unlockedCostumes != null) {
               player.saveCostumes();
               response.putIntArray("unlocked_costumes", unlockedCostumes);
            }

            SFSObject qe = new SFSObject();
            qe.putInt("teleport_monster", 20);
            this.serverQuestEvent(sender, qe);
            this.ext.savePlayerIsland(player, goalIsland, true);
            response.putLong("sent_to_island", 20L);
            response.putLong("dest_nursery", destNurseryId);
            this.sellMonster(sender, player, teleportableMonsterId, true, sourceIsland, true, false, false, admin);
            response.putBool("success", true);
            this.ext.stats.trackSendMonsterToBattleIsland(sender, teleportableMonster.getType(), teleportableMonster.getLevel(), teleportableMonsterId);
         }

         this.send("battle_teleport", response, sender);
      } catch (Exception var24) {
         Logger.trace(var24, "error teleporting monster", "   params : " + params.getDump());
      }

   }

   private void startTraining(User sender, ISFSObject params) {
      try {
         long monsterId = params.getLong("monster_id");
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland activeIsland = player.getActiveIsland();
         if (!activeIsland.isBattleIsland()) {
            this.ext.sendErrorResponse("battle_start_training", "Can't train on this island", sender);
            return;
         }

         PlayerMonster monster = activeIsland.getMonsterByID(monsterId);
         if (monster == null) {
            this.ext.sendErrorResponse("battle_start_training", "Cannot find monster to train, monsterId: " + monsterId, sender);
            return;
         }

         if (monster.isTraining()) {
            this.ext.sendErrorResponse("battle_start_training", "Monster already training, monsterId: " + monsterId, sender);
            return;
         }

         int monsterLevel = monster.getLevel();
         if (monsterLevel >= 20) {
            this.ext.sendErrorResponse("battle_start_training", "Monster at max level, monsterId: " + monsterId, sender);
            return;
         }

         int maxTrainingLevel = player.getBattleState().getMaxTrainingLevel();
         if (monsterLevel >= maxTrainingLevel) {
            this.ext.sendErrorResponse("battle_start_training", "Monster is at player's max training level, monsterId: " + monsterId, sender);
            return;
         }

         PlayerStructure battleGym = activeIsland.getBattleGym();
         if (battleGym == null) {
            this.ext.sendErrorResponse("battle_start_training", "Cannot find battle gym!", sender);
            return;
         }

         Structure battleGymStructure = StructureLookup.get(battleGym.getType());
         int capacity = battleGymStructure.getExtra().getInt("capacity");
         if (activeIsland.getNumMonstersInGym() >= capacity) {
            this.ext.sendErrorResponse("battle_start_training", "Battle gym is full!", sender);
            return;
         }

         int monsterType = monster.getType();
         int cost = Battle.GetBattleTrainingCost(monsterType, monsterLevel + 1);
         if (!player.canBuy((long)cost, 0L, 0L, 0L, 0L, 0L, 0)) {
            this.ext.sendErrorResponse("battle_start_training", "Not enough coins to pay for training!", sender);
            return;
         }

         player.chargePlayer(sender, (GameStateHandler)null, cost, 0, 0, 0L, 0, 0, 0);
         this.serverQuestEvent(sender, "start_train_monst", 1);
         int durationSecs = Battle.GetBattleTrainingDuration(monsterType, monsterLevel + 1);
         long trainingStart = MSMExtension.CurrentDBTime();
         long trainingCompletion = trainingStart + (long)durationSecs * 1000L;
         monster.startTraining(trainingStart, trainingCompletion);
         ISFSObject response = new SFSObject();
         response.putLong("monster_id", monsterId);
         response.putBool("success", true);
         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.send("battle_start_training", response, sender);
         ISFSObject monsterUpdateResponse = new SFSObject();
         monsterUpdateResponse.putLong("user_monster_id", monsterId);
         monsterUpdateResponse.putInt("is_training", 1);
         monsterUpdateResponse.putLong("training_start", trainingStart);
         monsterUpdateResponse.putLong("training_completion", trainingCompletion);
         this.ext.stats.trackTrainingStart(sender, cost, durationSecs, trainingStart, trainingCompletion, monster, battleGymStructure.getID(), activeIsland.getNumMonstersInGym(), capacity);
         this.send("gs_update_monster", monsterUpdateResponse, sender);
      } catch (Exception var23) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         this.send("battle_start_training", response, sender);
         Logger.trace(var23, "error starting training", "   params : " + params.getDump());
      }

   }

   private void trainingSpeedUp(User sender, ISFSObject params) {
      try {
         long monsterId = params.getLong("monster_id");
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland activeIsland = player.getActiveIsland();
         if (!activeIsland.isBattleIsland()) {
            this.ext.sendErrorResponse("battle_finish_training", "Can't train on this island", sender);
            return;
         }

         PlayerMonster monster = activeIsland.getMonsterByID(monsterId);
         if (monster == null) {
            this.ext.sendErrorResponse("battle_finish_training", "Cannot find monster to train, monsterId: " + monsterId, sender);
            return;
         }

         if (!monster.isTraining()) {
            this.ext.sendErrorResponse("battle_finish_training", "Monster is not training, monsterId: " + monsterId, sender);
            return;
         }

         ISFSObject response = new SFSObject();
         boolean isTraining = false;
         long secondsRemaining = (monster.trainingCompletion() - MSMExtension.CurrentDBTime()) / 1000L;
         if (secondsRemaining > 5L) {
            if (params.getInt("speed_up_type") != null && params.getInt("speed_up_type") != 0) {
               if (params.getInt("speed_up_type") != 1) {
                  this.ext.sendErrorResponse("battle_finish_training", "Monster is not done training yet, monsterId: " + monsterId, sender);
                  return;
               }

               if (!player.hasSpeedUpCredit()) {
                  this.ext.sendErrorResponse("battle_finish_training", "Unfortunately there are currently no offers available to you. Please try again later", sender);
                  return;
               }

               long initialTimeRemaining = secondsRemaining;
               monster.reduceTrainingTimeByVideo();
               player.adjustSpeedUpCredit(sender, this, -1, Player.SPEED_UP_TYPES.VIDEO);
               secondsRemaining = (monster.trainingCompletion() - MSMExtension.CurrentDBTime()) / 1000L;
               if (secondsRemaining > 5L) {
                  isTraining = true;
               }

               this.ext.stats.trackSpeedupWithVideo(sender, "speedup_battle_training", (long)monster.getType(), monster.getID(), Math.max(secondsRemaining, 0L), Math.max(initialTimeRemaining, 0L));
            } else {
               int diamondCost = Game.DiamondsRequiredToComplete(secondsRemaining * 1000L);
               if (!player.canBuy(0L, 0L, (long)diamondCost, 0L, 0L, 0L, 0)) {
                  this.ext.sendErrorResponse("battle_finish_training", "Not enough diamonds to pay to skip training", sender);
                  return;
               }

               player.chargePlayer(sender, (GameStateHandler)null, 0, 0, diamondCost, 0L, 0, 0, 0);
               this.logDiamondUsage(sender, "speedup_battle_training", diamondCost, player.getLevel(), monster.getEntityId());
               ISFSArray responseVars = new SFSArray();
               player.addPlayerPropertyData(responseVars, false);
               response.putSFSArray("properties", responseVars);
            }
         }

         if (!isTraining) {
            monster.finishTraining(player, activeIsland);
            this.serverQuestEvent(sender, "complete_train_monst", 1);
         }

         response.putBool("success", true);
         response.putLong("monster_id", monsterId);
         response.putInt("level", monster.getLevel());
         response.putInt("is_training", isTraining ? 1 : 0);
         this.send("battle_finish_training", response, sender);
         ISFSObject monsterUpdateResponse = new SFSObject();
         monsterUpdateResponse.putLong("user_monster_id", monsterId);
         monsterUpdateResponse.putLong("training_start", monster.trainingStart());
         monsterUpdateResponse.putLong("training_completion", monster.trainingCompletion());
         monsterUpdateResponse.putInt("is_training", isTraining ? 1 : 0);
         if (!isTraining) {
            monsterUpdateResponse.putInt("level", monster.getLevel());
         }

         if (!isTraining) {
            this.ext.stats.trackTrainingFinish(sender, Game.DiamondsRequiredToComplete(secondsRemaining), secondsRemaining, monster);
         }

         this.ext.savePlayer(player);
         this.send("gs_update_monster", monsterUpdateResponse, sender);
      } catch (Exception var14) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         this.send("battle_finish_training", response, sender);
         Logger.trace(var14, "error finishing training", "   params : " + params.getDump());
      }

   }

   private void startBattle(User sender, ISFSObject params) {
      try {
         ISFSObject response = new SFSObject();
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland activeIsland = player.getActiveIsland();
         if (!activeIsland.isBattleIsland()) {
            this.ext.sendErrorResponse("battle_start", "Can't finish a battle on this island", sender);
            return;
         }

         int campaignId = params.getInt("campaign_id");
         BattleCampaignData campaignData = BattleCampaignLookup.get(campaignId);
         if (campaignData == null) {
            this.ext.sendErrorResponse("battle_start", "Campaign not found", sender);
            return;
         }

         response.putInt("campaign_id", campaignId);
         int scheduleId = campaignData.getScheduleID();
         Schedule scheduleData = null;
         if (scheduleId > 0) {
            scheduleData = ScheduleLookup.schedule(scheduleId);
            if (!scheduleData.isActive()) {
               this.ext.sendErrorResponse("battle_start", "Schedule is not currently active!", sender);
               return;
            }
         }

         BattleIslandState battleIslandState = activeIsland.getBattleIslandState();
         BattleIslandCampaignState battleIslandCampaignState = battleIslandState.getCampaignState();
         if (battleIslandCampaignState.hasCompletedCampaign(campaignId)) {
            if (scheduleId <= 0) {
               this.ext.sendErrorResponse("battle_start", "Campaign already completed", sender);
               return;
            }

            battleIslandCampaignState.resetCampaign(campaignId);
         } else if (scheduleId > 0 && battleIslandCampaignState.getCampaignStartTime(campaignId) < scheduleData.getCurrentRepeatStartTime()) {
            battleIslandCampaignState.resetCampaign(campaignId);
         }

         int battleId = params.getInt("battle_id");
         if (battleIslandCampaignState.getCampaignProgress(campaignId) != battleId) {
            this.ext.sendErrorResponse("battle_start", "Invalid battle id", sender);
            return;
         }

         response.putInt("battle_id", battleId);
         BattleCampaignEventData battleData = campaignData.getBattle(battleId);
         if (battleData == null) {
            this.ext.sendErrorResponse("battle_start", "Invalid battle", sender);
            return;
         }

         if (battleId == 0) {
            long startTime = MSMExtension.CurrentDBTime();
            battleIslandCampaignState.startCampaign(campaignId, startTime);
            response.putLong("started", startTime);
         }

         int teamSize = battleData.getTeamSize();

         int i;
         String slotKey;
         long uniqueMonsterId;
         for(i = 0; i < teamSize; ++i) {
            slotKey = BattleLoadout.SlotKeys[i];
            uniqueMonsterId = params.getLong(slotKey);

            for(int j = 0; j < i; ++j) {
               String otherSlotKey = BattleLoadout.SlotKeys[j];
               long other = params.getLong(otherSlotKey);
               if (uniqueMonsterId == other) {
                  this.ext.sendErrorResponse("battle_start", "Found duplicate ID in loadout", sender);
                  return;
               }
            }

            PlayerMonster monster = activeIsland.getMonsterByID(uniqueMonsterId);
            if (monster == null) {
               this.ext.sendErrorResponse("battle_start", "Invalid monster in loadout", sender);
               return;
            }

            if (monster.isTraining()) {
               this.ext.sendErrorResponse("battle_start", "Found training monster in loadout", sender);
               return;
            }

            if (monster.inHotel()) {
               this.ext.sendErrorResponse("battle_start", "Found hotel monster in loadout", sender);
               return;
            }

            BattleRequirements campaignRequirements = campaignData.getRequirements();
            if (campaignRequirements != null && !campaignRequirements.evaluate(monster)) {
               this.ext.sendErrorResponse("battle_start", "Monster does not meet campaign requirements", sender);
               return;
            }

            BattleRequirements slotRequirements = battleData.getRequirementsForSlot(i);
            if (slotRequirements != null && !slotRequirements.evaluate(monster)) {
               this.ext.sendErrorResponse("battle_start", "Monster does not meet slot requirements", sender);
               return;
            }
         }

         for(i = 0; i < teamSize; ++i) {
            slotKey = BattleLoadout.SlotKeys[i];
            uniqueMonsterId = params.getLong(slotKey);
            player.getBattleState().getLoadout().setSlot(i, uniqueMonsterId);
            response.putLong(slotKey, uniqueMonsterId);
         }

         player.saveBattleState();
         long seed = System.nanoTime();
         battleIslandState.setSeed(seed);
         response.putLong("seed", seed);
         response.putBool("success", true);
         this.ext.stats.trackBattleStart(sender, campaignId, battleId, response, "campaign");
         this.send("battle_start", response, sender);
      } catch (Exception var23) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         this.send("battle_start", response, sender);
         Logger.trace(var23, "error starting battle", "   params : " + params.getDump());
      }

   }

   int calculateMatchMakingRanking(BattleOpponents loadout) {
      int total = 0;

      int stamina;
      float MATCHMAKING_POWER_WEIGHT;
      float MATCHMAKING_STAMINA_WEIGHT;
      int strongestPower;
      for(Iterator var3 = loadout.opponents().iterator(); var3.hasNext(); total += (int)((float)strongestPower * MATCHMAKING_POWER_WEIGHT + (float)stamina * MATCHMAKING_STAMINA_WEIGHT)) {
         BattleOpponentData opponentData = (BattleOpponentData)var3.next();
         BattleMonsterStatData monsterStats = BattleMonsterStatLookup.get(opponentData.getId());
         List<BattleMonsterActionData> actions = monsterStats.getActions();
         int costumeId = opponentData.getCostume();
         CostumeData costumeData = CostumeLookup.get(costumeId);
         if (costumeData != null && costumeData.action() > 0) {
            BattleMonsterActionData overrideAction = BattleMonsterActionLookup.get(costumeData.action());
            if (overrideAction.replaces() >= actions.size()) {
               actions.add(overrideAction);
            } else {
               actions.set(overrideAction.replaces(), overrideAction);
            }
         }

         strongestPower = 0;
         Iterator var10 = actions.iterator();

         while(var10.hasNext()) {
            BattleMonsterActionData action = (BattleMonsterActionData)var10.next();
            int power = BattleState.GetBattleMonsterActionPowerForLevel(monsterStats, action, opponentData.getLevel());
            if (power > strongestPower) {
               strongestPower = power;
            }
         }

         stamina = BattleState.GetBattleMonsterStaminaForLevel(monsterStats.getBaseStamina(), opponentData.getLevel());
         MATCHMAKING_POWER_WEIGHT = GameSettings.get("BATTLE_MMR_POWER_WEIGHT", 1.0F);
         MATCHMAKING_STAMINA_WEIGHT = GameSettings.get("BATTLE_MMR_STAMINA_WEIGHT", 2.0F);
      }

      return total;
   }

   BattleVersusOpponent findBattleVersusOpponent(long playerId, BattleCampaignData campaignData, long scheduleStartTime, int matchMakingRanking, int streak) throws Exception {
      String FIND_OPPONENT_SQL = "SELECT user_battle_pvp.user_id, user_battle_pvp.loadout, user_battle_pvp.display_name, user_avatar.pp_type, user_avatar.pp_info  FROM user_battle_pvp  LEFT JOIN user_avatar  ON user_avatar.user_id = user_battle_pvp.user_id  WHERE NOT user_battle_pvp.user_id = ?  AND campaign_id = ? AND schedule_started_on = ? AND match_score < ? AND match_score >= ?  LIMIT ?";
      int retries = 0;
      int MAX_RETRIES = GameSettings.get("BATTLE_MMR_RETRIES", 3);
      int HIGH_END = GameSettings.get("BATTLE_MMR_HIGH_END", 500);
      int LOW_END = GameSettings.get("BATTLE_MMR_LOW_END", 50);
      int STREAK_SHIFT = GameSettings.get("BATTLE_MMR_STREAK_SHIFT", 100);
      int MIN_MATCHES = GameSettings.get("BATTLE_MMR_MIN_MATCHES", 3);
      int MAX_MATCHES = GameSettings.get("BATTLE_MMR_MAX_MATCHES", 10);
      int maxScore = matchMakingRanking + HIGH_END;
      int minScore = matchMakingRanking - LOW_END;
      maxScore += STREAK_SHIFT * streak;

      int sqr;
      for(minScore += STREAK_SHIFT * streak; retries < MAX_RETRIES; minScore -= LOW_END * sqr) {
         Object[] args = new Object[]{playerId, campaignData.getId(), new Timestamp(scheduleStartTime), maxScore, minScore, MAX_MATCHES};
         ISFSArray results = this.ext.getDB().query("SELECT user_battle_pvp.user_id, user_battle_pvp.loadout, user_battle_pvp.display_name, user_avatar.pp_type, user_avatar.pp_info  FROM user_battle_pvp  LEFT JOIN user_avatar  ON user_avatar.user_id = user_battle_pvp.user_id  WHERE NOT user_battle_pvp.user_id = ?  AND campaign_id = ? AND schedule_started_on = ? AND match_score < ? AND match_score >= ?  LIMIT ?", args);
         if (results.size() >= MIN_MATCHES) {
            Random rng = new Random();
            int idx = rng.nextInt(results.size());
            return new BattleVersusOpponent(results.getSFSObject(idx));
         }

         String FIND_GENERATED_OPPONENT_SQL = "SELECT * FROM battle_teamgen WHERE pool_id = ? AND match_score < ? AND match_score >= ? LIMIT ?";
         ISFSArray poolResults = this.ext.getStaticDataDB().query("SELECT * FROM battle_teamgen WHERE pool_id = ? AND match_score < ? AND match_score >= ? LIMIT ?", new Object[]{campaignData.getPool(), maxScore, minScore, MAX_MATCHES});
         if (results.size() + poolResults.size() > MIN_MATCHES) {
            Random rng = new Random();
            int idx = rng.nextInt(results.size() + poolResults.size());
            if (idx < results.size()) {
               return new BattleVersusOpponent(results.getSFSObject(idx));
            }

            return new BattleVersusOpponent(poolResults.getSFSObject(idx - results.size()));
         }

         ++retries;
         sqr = retries * retries;
         maxScore += HIGH_END * sqr;
      }

      return this.generateBattleVersusOpponent(campaignData);
   }

   BattleVersusOpponent generateBattleVersusOpponent(BattleCampaignData campaignData) throws Exception {
      BattleVersusOpponent generated = new BattleVersusOpponent();
      ISFSArray defaultPvpLoadout = campaignData.getDefaultPvpLoadout();
      if (defaultPvpLoadout != null) {
         generated.opponents = new BattleOpponents(defaultPvpLoadout);
      } else {
         generated.opponents = new BattleOpponents();
         BattleOpponentData generatedMonster = new BattleOpponentData();
         generatedMonster.setId(1);
         generatedMonster.setLevel(20);
         generatedMonster.setCostume(3);
         generated.opponents.add(generatedMonster);
         generatedMonster = new BattleOpponentData();
         generatedMonster.setId(1);
         generatedMonster.setLevel(5);
         generatedMonster.setCostume(3);
         generated.opponents.add(generatedMonster);
         generatedMonster = new BattleOpponentData();
         generatedMonster.setId(1);
         generatedMonster.setLevel(5);
         generatedMonster.setCostume(3);
         generated.opponents.add(generatedMonster);
      }

      return generated;
   }

   private void startBattleVersus(User sender, ISFSObject params) {
      try {
         ISFSObject response = new SFSObject();
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland activeIsland = player.getActiveIsland();
         if (!activeIsland.isBattleIsland()) {
            this.ext.sendErrorResponse("battle_start_versus", "Can't start a battle on this island", sender);
            return;
         }

         int campaignId = params.getInt("campaign_id");
         BattleCampaignData campaignData = BattleCampaignLookup.get(campaignId);
         if (campaignData == null) {
            this.ext.sendErrorResponse("battle_start_versus", "Campaign not found", sender);
            return;
         }

         response.putInt("campaign_id", campaignId);
         int scheduleId = campaignData.getScheduleID();
         Schedule scheduleData = ScheduleLookup.schedule(scheduleId);
         if (!scheduleData.isActive()) {
            this.ext.sendErrorResponse("battle_start_versus", "Schedule is not currently active!", sender);
            return;
         }

         long scheduleStartTime = scheduleData.getCurrentRepeatStartTime();
         BattlePlayerVersusState versusState = player.getBattleVersusState(campaignId, scheduleStartTime);
         if (versusState == null) {
            versusState = player.createBattleVersusState(campaignId, scheduleStartTime);
            long startTime = MSMExtension.CurrentDBTime();
            versusState.start(startTime);
            response.putLong("started_on", startTime);
         }

         int currentTier = versusState.getTier();
         BattleVersusTierData tierData = campaignData.getTier(currentTier);
         if (tierData == null) {
            this.ext.sendErrorResponse("battle_start_versus", "Invalid tier", sender);
            return;
         }

         int attempts = versusState.getAttempts();
         if (attempts <= 0) {
            if (MSMExtension.CurrentDBTime() < versusState.getRefreshTimestamp()) {
               this.ext.sendErrorResponse("battle_start_versus", "Not enough attempts remaining", sender);
               return;
            }

            attempts = 5;
            long newDayTimestamp = BattlePlayerVersusState.GetNewDayTimestamp();
            versusState.setRefreshTimestamp(newDayTimestamp);
         }

         --attempts;
         versusState.setAttempts(attempts);
         if (attempts <= 0) {
            versusState.setAttempts(0);
         }

         response.putInt("attempts", versusState.getAttempts());
         response.putLong("refreshes_on", versusState.getRefreshTimestamp());
         int maxTeamSize = tierData.getTeamSize();

         int i;
         String slotKey;
         long uniqueMonsterId;
         for(i = 0; i < maxTeamSize; ++i) {
            slotKey = BattleLoadout.SlotKeys[i];
            uniqueMonsterId = params.getLong(slotKey);
            if (uniqueMonsterId == 0L) {
               BattleRequirements slotRequirements = tierData.getRequirementsForSlot(i);
               if (slotRequirements != null && !slotRequirements.evaluate((PlayerMonster)null)) {
                  this.ext.sendErrorResponse("battle_start_versus", "Empty slot does not meet slot requirements", sender);
                  return;
               }
            } else {
               for(int j = 0; j < i; ++j) {
                  String otherSlotKey = BattleLoadout.SlotKeys[j];
                  long other = params.getLong(otherSlotKey);
                  if (uniqueMonsterId == other) {
                     this.ext.sendErrorResponse("battle_start_versus", "Found duplicate ID in loadout", sender);
                     return;
                  }
               }

               PlayerMonster monster = activeIsland.getMonsterByID(uniqueMonsterId);
               if (monster == null) {
                  this.ext.sendErrorResponse("battle_start_versus", "Invalid monster in loadout", sender);
                  return;
               }

               if (monster.isTraining()) {
                  this.ext.sendErrorResponse("battle_start_versus", "Found training monster in loadout", sender);
                  return;
               }

               if (monster.inHotel()) {
                  this.ext.sendErrorResponse("battle_start_versus", "Found hotel monster in loadout", sender);
                  return;
               }

               BattleRequirements campaignRequirements = campaignData.getRequirements();
               if (campaignRequirements != null && !campaignRequirements.evaluate(monster)) {
                  this.ext.sendErrorResponse("battle_start_versus", "Monster does not meet campaign requirements", sender);
                  return;
               }

               BattleRequirements slotRequirements = tierData.getRequirementsForSlot(i);
               if (slotRequirements != null && !slotRequirements.evaluate(monster)) {
                  this.ext.sendErrorResponse("battle_start_versus", "Monster does not meet slot requirements", sender);
                  return;
               }
            }
         }

         for(i = 0; i < maxTeamSize; ++i) {
            slotKey = BattleLoadout.SlotKeys[i];
            uniqueMonsterId = params.getLong(slotKey);
            player.getBattleState().getLoadoutVersus().setSlot(i, uniqueMonsterId);
            response.putLong(slotKey, uniqueMonsterId);
         }

         BattleOpponents loadout = new BattleOpponents();

         long seed;
         int matchScore;
         for(matchScore = 0; matchScore < maxTeamSize; ++matchScore) {
            String slotKey = BattleLoadout.SlotKeys[matchScore];
            seed = params.getLong(slotKey);
            if (seed > 0L) {
               PlayerMonster monster = activeIsland.getMonsterByID(seed);
               BattleOpponentData data = new BattleOpponentData();
               data.setId(monster.getType());
               data.setLevel(monster.getLevel());
               MonsterCostumeState costumeState = monster.getCostumeState();
               if (costumeState != null) {
                  data.setCostume(costumeState.getEquipped());
               }

               loadout.add(data);
            }
         }

         versusState.setLoadout(loadout);
         matchScore = this.calculateMatchMakingRanking(loadout);
         versusState.setMatchScore(matchScore);
         if (versusState.getLastSeed() != 0L) {
            this.handleBattleVersusLoss(versusState, campaignData);
         }

         BattleVersusOpponent opponent = this.findBattleVersusOpponent(player.getPlayerId(), campaignData, scheduleStartTime, matchScore, versusState.getStreak());
         if (opponent == null) {
            this.ext.sendErrorResponse("battle_start_versus", "Could not find opponent", sender);
            return;
         }

         response.putSFSObject("opponent", opponent.toSFSObject());
         versusState.setLastOpponentLoadout(opponent.opponents);
         seed = System.nanoTime();
         activeIsland.getBattleIslandState().setSeed(seed);
         response.putLong("seed", seed);
         versusState.setLastSeed(seed);
         player.saveBattleState();
         player.saveBattleVersusStates();
         response.putBool("success", true);
         this.ext.stats.trackBattleStart(sender, campaignId, -1, response, "pvp_match");
         this.send("battle_start_versus", response, sender);
      } catch (Exception var25) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         this.send("battle_start_versus", response, sender);
         Logger.trace(var25, "error starting battle", "   params : " + params.getDump());
      }

   }

   private void startBattleFriend(User sender, ISFSObject params) {
      SFSObject response;
      try {
         Player friend = this.ext.createPlayerByID(params.getLong("friend_user_id"), false, new VersionInfo((String)sender.getProperty("client_version")), (String)null, (String)null);
         if (friend == null) {
            this.ext.sendErrorResponse("battle_start_friend", "UPDATE_VERSION", sender);
            return;
         }

         if (friend.isBanned()) {
            this.ext.sendErrorResponse("battle_start_friend", "NOTIFICATION_FRIEND_BANNED", sender);
            return;
         }

         response = new SFSObject();
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland playerIsland = player.getActiveIsland();
         if (!BattleFriendPvp.validateFriendBattle(sender, player, playerIsland, params.getLong("friend_bbb_id"))) {
            return;
         }

         Long opponentFriendUserId = params.getLong("friend_user_id");
         SFSArray opponentLoadout = null;
         opponentLoadout = BattleFriendPvp.getFriendPvpLoadout(sender, opponentFriendUserId);
         if (opponentLoadout == null || opponentLoadout.size() == 0) {
            opponentLoadout = BattleFriendPvp.getFriendSoloCampaignLoadout(sender, opponentFriendUserId);
         }

         if (opponentLoadout == null || opponentLoadout.size() <= 0) {
            MSMExtension.getInstance().sendErrorResponse("battle_start_friend", "Friend has no valid loadouts", sender);
            return;
         }

         for(int i = 0; i < 3; ++i) {
            String slotKey = BattleLoadout.SlotKeys[i];
            if (params.containsKey(slotKey)) {
               long uniqueMonsterId = params.getLong(slotKey);
               player.getBattleState().getLoadoutVersus().setSlot(i, uniqueMonsterId);
               response.putLong(slotKey, uniqueMonsterId);
            }
         }

         SFSObject opponent = new SFSObject();
         opponent.putSFSArray("loadout", opponentLoadout);
         opponent.putLong("friend", params.getLong("friend_bbb_id"));
         response.putSFSObject("opponent", opponent);
         response.putBool("success", true);
         this.ext.stats.trackBattleStart(sender, -1, -1, response, "pvp_friend");
         this.send("battle_start_friend", response, sender);
      } catch (Exception var13) {
         response = new SFSObject();
         response.putBool("success", false);
         this.send("battle_start_friend", response, sender);
         Logger.trace(var13, "error starting battle", "   params : " + params.getDump());
      }

   }

   private ISFSObject giveBattleReward(User sender, BattleRewardData battleReward, Player player, ISFSObject params) throws Exception {
      ISFSObject rewardObj = new SFSObject();
      if (battleReward != null) {
         if (battleReward.coins() > 0) {
            player.adjustCoins(sender, this, battleReward.coins());
            rewardObj.putInt("coins", battleReward.coins());
         }

         if (battleReward.diamonds() > 0) {
            player.adjustDiamonds(sender, this, battleReward.diamonds());
            this.ext.stats.trackReward(sender, "battle_reward", "diamonds", (long)battleReward.diamonds());
            rewardObj.putInt("diamonds", battleReward.diamonds());
         }

         if (battleReward.medals() > 0) {
            player.adjustMedals(sender, this, battleReward.medals());
            rewardObj.putInt("medals", battleReward.medals());
            this.ext.stats.trackReward(sender, "battle_reward", "medals", (long)battleReward.medals());
         }

         if (battleReward.food() > 0) {
            player.adjustFood(sender, this, battleReward.food());
            rewardObj.putInt("food", battleReward.food());
            this.ext.stats.trackReward(sender, "battle_reward", "food", (long)battleReward.food());
         }

         if (battleReward.starpower() > 0) {
            player.adjustStarpower(sender, this, (long)battleReward.starpower());
            rewardObj.putInt("starpower", battleReward.starpower());
            this.ext.stats.trackReward(sender, "battle_reward", "starpower", (long)battleReward.starpower());
         }

         if (battleReward.relics() > 0) {
            player.adjustRelics(sender, this, battleReward.relics());
            rewardObj.putInt("relics", battleReward.relics());
            this.ext.stats.trackReward(sender, "battle_reward", "relics", (long)battleReward.relics());
         }

         if (battleReward.keys() > 0) {
            player.adjustKeys(sender, this, battleReward.keys());
            rewardObj.putInt("keys", battleReward.keys());
            this.ext.stats.trackReward(sender, "battle_reward", "keys", (long)battleReward.keys());
         }

         if (battleReward.trophy() > 0) {
            VersionInfo clientVer = new VersionInfo((String)sender.getProperty("client_version"));
            int trophyEntId = BattleTrophyLookup.getEntityIdForTrophy(battleReward.trophy());
            if (clientVer.compareTo(GameSettings.get("BATTLE_TUT_V2_CLIENT_VERSION")) < 0) {
               player.giveTrophy(battleReward.trophy());
               this.updateMail(sender, params);
            } else {
               boolean giveTrophy = true;
               if (player.getInventory().hasItem(trophyEntId)) {
                  giveTrophy = false;
               } else {
                  Collection<PlayerIsland> islands = player.getIslands();
                  Iterator itr = islands.iterator();

                  while(itr.hasNext()) {
                     PlayerIsland curIsland = (PlayerIsland)itr.next();
                     if (curIsland.hasStructure(trophyEntId)) {
                        giveTrophy = false;
                        break;
                     }
                  }
               }

               if (giveTrophy) {
                  player.getInventory().addItem(trophyEntId, 1);
                  player.saveInventory();
                  rewardObj.putInt("trophyEntityId", trophyEntId);
                  this.ext.stats.trackTrophy(sender, trophyEntId);
               }
            }
         }

         if (battleReward.xp() > 0) {
            rewardObj.putInt("xp", battleReward.xp());
            boolean levelUp = player.getBattleState().rewardXp(battleReward.xp());
            player.saveBattleState();
            rewardObj.putInt("player_xp", player.getBattleState().getXp());
            rewardObj.putInt("player_level", player.getBattleState().getLevel());
            if (levelUp) {
               rewardObj.putBool("level_up", true);
            }
         }

         if (battleReward.costume() > 0) {
            int costumeId = battleReward.costume();
            PlayerIsland activeIsland = player.getActiveIsland();
            IslandCostumeState islandCostumes = activeIsland.getCostumeState();
            if (!islandCostumes.hasCostume(battleReward.costume())) {
               islandCostumes.addCostume(battleReward.costume());
            }

            player.getCostumes().unlockCostume(costumeId);
            player.saveCostumes();
            rewardObj.putInt("costumeId", battleReward.costume());
            islandCostumes.addCredit(battleReward.costume(), 1);
            rewardObj.putInt("credits", 1);
         }
      }

      return rewardObj;
   }

   private void finishBattle(User sender, ISFSObject params) {
      if (params.containsKey("friend") && params.getLong("friend") != 0L) {
         this.finishFriendBattle(sender, params);
      } else {
         try {
            ISFSObject response = new SFSObject();
            Player player = (Player)sender.getProperty("player_object");
            PlayerIsland activeIsland = player.getActiveIsland();
            if (!activeIsland.isBattleIsland()) {
               this.ext.sendErrorResponse("battle_finish", "Can't finish a battle on this island", sender);
               return;
            }

            int campaignId = params.getInt("campaign_id");
            BattleCampaignData campaignData = BattleCampaignLookup.get(campaignId);
            if (campaignData == null) {
               this.ext.sendErrorResponse("battle_finish", "Campaign not found", sender);
               return;
            }

            response.putInt("campaign_id", campaignId);
            int battleId = params.getInt("battle_id");
            BattleIslandState battleIslandState = activeIsland.getBattleIslandState();
            if (!campaignData.isPVP()) {
               BattleIslandCampaignState battleIslandCampaignState = battleIslandState.getCampaignState();
               if (battleIslandCampaignState.hasCompletedCampaign(campaignId)) {
                  this.ext.sendErrorResponse("battle_finish", "Campaign already completed", sender);
                  return;
               }

               if (battleIslandCampaignState.getCampaignProgress(campaignId) != battleId) {
                  this.ext.sendErrorResponse("battle_finish", "Invalid battle id", sender);
                  return;
               }

               response.putInt("battle_id", battleId);
            }

            if (campaignData.isPVP()) {
               BattlePlayerVersusState battlePlayerVersusState = player.getLatestBattleVersusState(campaignId);
               response.putInt("last_tier", battlePlayerVersusState.getTier());
               response.putInt("last_stars", battlePlayerVersusState.getStars());
               response.putInt("last_streak", battlePlayerVersusState.getStreak());
            }

            int result = params.getInt("result");
            BattlePlayerVersusState battlePlayerVersusState;
            if (result == 1) {
               long seed = battleIslandState.getSeed();
               String calculatedSignature = Misc.md5("u:" + player.getPlayerId() + "c:" + campaignId + "b:" + battleId + "r:" + result + "seed:" + battleIslandState.getSeed() + "secret:" + GameSettings.get("USER_SPHINX", "Y*7vzp^0Xs#Jf812tmtTeLa%$LDQ4yZP"));
               String requestSignature = params.getUtfString("sig");
               if (calculatedSignature.compareTo(requestSignature) != 0) {
                  Logger.trace("error validating signature   params : " + params.getDump());
                  this.ext.sendErrorResponse("battle_finish", "INVALID_REQUEST", sender);
                  return;
               }

               boolean isTutorialBattle = campaignId == 1;
               if (GameSettings.get("BATTLE_VERIFICATION_ENABLED", true) && !isTutorialBattle) {
                  ISFSArray actions = params.getSFSArray("actions");
                  BattleOpponents playerTeam = null;
                  BattleOpponents opponentTeam = null;
                  boolean opponentCanSwap = false;
                  float pickOptimalPercentage = 0.0F;
                  if (campaignData.isPVP()) {
                     BattlePlayerVersusState battlePlayerVersusState = player.getLatestBattleVersusState(campaignId);
                     playerTeam = battlePlayerVersusState.getLoadout();
                     opponentTeam = battlePlayerVersusState.getLastOpponentLoadout();
                     opponentCanSwap = true;

                     try {
                        pickOptimalPercentage = GameSettings.get("USER_BATTLE_VERSUS_AI_SMARTNESS", 0.0F);
                     } catch (NumberFormatException var25) {
                        Logger.trace("make sure USER_BATTLE_VERSUS_AI_SMARTNESS is a float!");
                     }
                  } else {
                     BattleCampaignEventData battleData = campaignData.getBattle(battleId);
                     BattleLoadout playerLoadout = player.getBattleState().getLoadout();
                     playerTeam = playerLoadout.convertToBattleOpponents(player, battleData.getTeamSize());
                     opponentTeam = battleData.getOpponents();
                  }

                  if (!BattleVerification.IsValidResult(playerTeam, opponentTeam, seed, actions, opponentCanSwap, pickOptimalPercentage)) {
                     Logger.trace("error validating result   params : " + params.getDump());
                     this.ext.sendErrorResponse("battle_finish", "VERIFICATION_FAILED", sender);
                     return;
                  }
               }

               this.serverQuestEvent(sender, "win_battle", 1);
               int currentStars;
               if (campaignData.isPVP()) {
                  BattlePlayerVersusState battlePlayerVersusState = player.getLatestBattleVersusState(campaignId);
                  int currentTier = battlePlayerVersusState.getTier();
                  BattleVersusTierData tierData = campaignData.getTier(currentTier);
                  int currentStreak = battlePlayerVersusState.getStreak();
                  ++currentStreak;
                  battlePlayerVersusState.setStreak(currentStreak);
                  int requiredStars;
                  if (battlePlayerVersusState.hasCompletedSeason()) {
                     currentStars = 30 + Math.min(currentStreak * 5, 25);
                     requiredStars = battlePlayerVersusState.getChampionScore();
                     battlePlayerVersusState.setChampionScore(requiredStars + currentStars);
                  } else {
                     currentStars = battlePlayerVersusState.getStars();
                     requiredStars = tierData.getStars();
                     int earnedStars = 1;
                     if (currentStreak > 1) {
                        ++earnedStars;
                     }

                     currentStars += earnedStars;
                     if (currentStars >= requiredStars) {
                        currentStars -= requiredStars;
                        ++currentTier;
                        tierData = campaignData.getTier(currentTier);
                        requiredStars = tierData.getStars();
                        if (requiredStars <= 0) {
                           long completedOn = MSMExtension.CurrentDBTime();
                           battlePlayerVersusState.setCompletedTimestamp(completedOn);
                           response.putLong("completed_on", completedOn);
                           currentStars = 0;
                        }
                     }

                     battlePlayerVersusState.setStars(currentStars);
                     battlePlayerVersusState.setTier(currentTier);
                  }

                  String rewardDataJson = GameSettings.get("VERSUS_BATTLE_REWARD", "{\"xp\":7,\"medals\":7}");
                  BattleRewardData rewardData = new BattleRewardData(SFSObject.newFromJsonData(rewardDataJson));
                  ISFSObject battleReward = this.giveBattleReward(sender, rewardData, player, params);
                  response.putSFSObject("battle_reward", battleReward);
               } else {
                  BattleIslandCampaignState battleIslandCampaignState = battleIslandState.getCampaignState();
                  List<BattleCampaignEventData> battles = campaignData.getBattles();
                  if (battleId < battles.size()) {
                     BattleCampaignEventData battleData = (BattleCampaignEventData)battles.get(battleId);
                     ISFSObject battleReward = this.giveBattleReward(sender, battleData.getReward(), player, params);
                     response.putSFSObject("battle_reward", battleReward);
                     currentStars = battleId + 1;
                     battleIslandCampaignState.setCampaignProgress(campaignId, currentStars);
                     response.putInt("next_battle", currentStars);
                  }

                  if (battleId == battles.size() - 1) {
                     this.serverQuestEvent(sender, "win_battle_campaign", 1);
                     ISFSObject campaignReward = this.giveBattleReward(sender, campaignData.getReward(), player, params);
                     response.putSFSObject("campaign_reward", campaignReward);
                     long completionTime = MSMExtension.CurrentDBTime();
                     battleIslandCampaignState.completeCampaign(campaignId, completionTime);
                     response.putLong("completed", completionTime);
                     if (campaignData.getMaxTrainingLevel() > player.getBattleState().getMaxTrainingLevel()) {
                        player.getBattleState().setMaxTrainingLevel(campaignData.getMaxTrainingLevel());
                        player.saveBattleState();
                        response.putInt("max_training_level", campaignData.getMaxTrainingLevel());
                     }
                  }
               }
            } else {
               if (campaignData.isPVP()) {
                  battlePlayerVersusState = player.getLatestBattleVersusState(campaignId);
                  this.handleBattleVersusLoss(battlePlayerVersusState, campaignData);
               }

               this.serverQuestEvent(sender, "lose_battle", 1);
            }

            if (campaignData.isPVP()) {
               battlePlayerVersusState = player.getLatestBattleVersusState(campaignId);
               response.putInt("new_tier", battlePlayerVersusState.getTier());
               response.putInt("new_stars", battlePlayerVersusState.getStars());
               response.putInt("new_streak", battlePlayerVersusState.getStreak());
               if (battlePlayerVersusState.hasCompletedSeason()) {
                  long rank = 0L;
                  BattleVersusChampionRanks.RankedUser ru = BattleVersusChampionRanks.getRankedUser(battlePlayerVersusState.getCampaignId(), battlePlayerVersusState.getScheduleTimestamp(), player.getPlayerId());
                  if (ru != null) {
                     rank = ru.rank();
                  } else {
                     rank = (long)(BattleVersusChampionRanks.getRankTable(battlePlayerVersusState.getCampaignId(), battlePlayerVersusState.getScheduleTimestamp()).rankedList.size() + 1);
                  }

                  response.putLong("rank", rank);
               }

               battlePlayerVersusState.setLastSeed(0L);
               player.saveBattleVersusStates();
            }

            response.putInt("result", result);
            response.putBool("success", true);
            ISFSArray responseVars = new SFSArray();
            player.addPlayerPropertyData(responseVars, false);
            responseVars.addSFSObject(player.getInventory().toSFSObject());
            response.putSFSArray("properties", responseVars);
            this.ext.stats.trackBattleFinish(sender, campaignId, battleId, response, campaignData.isPVP());
            this.send("battle_finish", response, sender);
         } catch (Exception var26) {
            ISFSObject response = new SFSObject();
            response.putBool("success", false);
            this.send("battle_finish", response, sender);
            Logger.trace(var26, "error finishing battle", "   params : " + params.getDump());
         }

      }
   }

   private void handleBattleVersusLoss(BattlePlayerVersusState battlePlayerVersusState, BattleCampaignData campaignData) {
      int currentTier = battlePlayerVersusState.getTier();
      BattleVersusTierData tierData = campaignData.getTier(currentTier);
      battlePlayerVersusState.setStreak(0);
      if (battlePlayerVersusState.getCompletedTimestamp() > 0L) {
         int rankIncrease = 5;
         int currentRank = battlePlayerVersusState.getChampionScore();
         battlePlayerVersusState.setChampionScore(currentRank + rankIncrease);
      } else {
         int stars;
         switch(tierData.getPenalty()) {
         case STARS:
            stars = battlePlayerVersusState.getStars();
            stars = Math.max(0, stars - 1);
            battlePlayerVersusState.setStars(stars);
            break;
         case TIER:
            stars = battlePlayerVersusState.getStars();
            --stars;
            if (stars < 0) {
               if (currentTier == 0) {
                  stars = 0;
               } else {
                  --currentTier;
                  battlePlayerVersusState.setTier(currentTier);
                  tierData = campaignData.getTier(currentTier);
                  stars += tierData.getStars();
               }
            }

            battlePlayerVersusState.setStars(stars);
         case NONE:
         }
      }

   }

   public void finishFriendBattle(User sender, ISFSObject params) {
      ISFSObject response = new SFSObject();
      Player player = (Player)sender.getProperty("player_object");
      long bbbId = player.getBbbId();
      long friendBbbId = params.getLong("friend");
      int won = params.getInt("result");
      int updatedWins = 0;
      int updatedLoses = 0;
      if (bbbId == friendBbbId) {
         response.putBool("success", false);
         response.putUtfString("error_msg", "FRIEND_ERROR_USER_NOT_FOUND");
         this.send("gs_update_friendbattle", response, sender);
      } else {
         try {
            String sql = "SELECT * FROM users WHERE bbb_id=?";
            Object[] args = new Object[]{friendBbbId};
            SFSArray result = this.ext.getDB().query(sql, args);
            if (result.size() == 0) {
               response.putBool("success", false);
               response.putUtfString("error_msg", "FRIEND_ERROR_USER_NOT_FOUND");
               this.send("gs_update_friendbattle", response, sender);
               return;
            }

            int friendUserId = result.getSFSObject(0).getInt("user_id");
            sql = "SELECT * FROM user_friends WHERE (user_1=? and user_2=?) or (user_2=? and user_1=?)";
            args = new Object[]{bbbId, friendBbbId, bbbId, friendBbbId};
            result = this.ext.getDB().query(sql, args);
            if (result.size() == 0) {
               response.putBool("success", false);
               response.putUtfString("error_msg", "FRIEND_ERROR_USER_NOT_FOUND");
               this.send("gs_update_friendbattle", response, sender);
            }

            int minPvpLevel = GameSettings.get("USER_PVP_MIN_BATTLE_LVL", 5);
            if (player.getBattleState().getLevel() >= minPvpLevel) {
               long user1 = result.getSFSObject(0).getLong("user_1");
               long user2 = result.getSFSObject(0).getLong("user_2");
               boolean imUser1 = user1 == bbbId;
               sql = "SELECT level from user_battle WHERE user_id=?";
               args = new Object[]{friendUserId};
               result = this.ext.getDB().query(sql, args);
               if (result.size() > 0) {
                  int friendBattleLevel = result.getSFSObject(0).getInt("level");
                  if (friendBattleLevel >= minPvpLevel) {
                     sql = "SELECT * FROM user_battle_friends WHERE (user_1 = ? AND user_2 =?) OR (user_2 = ? AND user_1 =?)";
                     args = new Object[]{bbbId, friendBbbId, bbbId, friendBbbId};
                     result = this.ext.getDB().query(sql, args);
                     int wonIncr = 0;
                     int lostIncr = 0;
                     if (result.size() == 0) {
                        if (won != 0 && imUser1) {
                           ++wonIncr;
                        } else {
                           ++lostIncr;
                        }

                        sql = "INSERT INTO user_battle_friends (user_1, user_2, won_battles, lost_battles) VALUES (?, ?, ?, ?)";
                        args = new Object[]{user1, user2, wonIncr, lostIncr};
                        this.ext.getDB().update(sql, args);
                     } else {
                        user1 = result.getSFSObject(0).getLong("user_1");
                        user2 = result.getSFSObject(0).getLong("user_2");
                        imUser1 = user1 == bbbId;
                        if (won != 0 && imUser1) {
                           ++wonIncr;
                        } else {
                           ++lostIncr;
                        }

                        sql = "UPDATE user_battle_friends SET won_battles=?, lost_battles=? WHERE user_1=? AND user_2=?";
                        args = new Object[]{result.getSFSObject(0).getInt("won_battles") + wonIncr, result.getSFSObject(0).getInt("lost_battles") + lostIncr, result.getSFSObject(0).getLong("user_1"), result.getSFSObject(0).getLong("user_2")};
                        this.ext.getDB().update(sql, args);
                     }

                     sql = "SELECT won_battles,lost_battles FROM user_battle_friends WHERE (user_1 = ? AND user_2 =?) OR (user_2 = ? AND user_1 =?)";
                     args = new Object[]{bbbId, friendBbbId, bbbId, friendBbbId};
                     result = this.ext.getDB().query(sql, args);
                     if (result.size() > 0) {
                        if (imUser1) {
                           updatedWins = result.getSFSObject(0).getInt("won_battles");
                           updatedLoses = result.getSFSObject(0).getInt("lost_battles");
                        } else {
                           updatedWins = result.getSFSObject(0).getInt("lost_battles");
                           updatedLoses = result.getSFSObject(0).getInt("won_battles");
                        }
                     }
                  }
               }
            }
         } catch (Exception var26) {
            Logger.trace(var26, " ***** Exception trying to update battle friend ***** ");
         }

         response.putInt("result", won);
         response.putInt("updatedWins", updatedWins);
         response.putInt("updatedLoses", updatedLoses);
         response.putLong("friend", friendBbbId);

         try {
            BattleRewardData brd = new BattleRewardData(SFSObject.newFromJsonData(GameSettings.get("FRIEND_PVP_BATTLE_REWARD")));
            ISFSObject battleReward = this.giveBattleReward(sender, brd, player, params);
            response.putSFSObject("battle_reward", battleReward);
         } catch (Exception var25) {
            Logger.trace(var25, " ***** Exception trying to give battle reward ***** ");
         }

         response.putBool("success", true);
         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         this.ext.stats.trackFriendBattle(sender, friendBbbId, updatedWins, updatedLoses, won, player.getBattleState().getLevel());
         this.send("battle_finish", response, sender);
      }
   }

   private void purchaseCostume(User sender, Player player, PlayerIsland activeIsland, boolean admin, CostumeData costumeData, MonsterCostumeState monsterCostumeState, ISFSObject response, JSONObject metricsData) throws Exception {
      int costumeId = costumeData.getId();
      if (monsterCostumeState.hasPurchased(costumeId)) {
         throw new Exception("COSTUME_ALREADY_PURCHASED");
      } else {
         PlayerCostumeState playerCostumeState = player.getCostumes();
         IslandCostumeState islandCostumeState = activeIsland.getCostumeState();
         int islandCredits = islandCostumeState.getCredits(costumeId);
         int globalCredits = playerCostumeState.inventory().getItemAmount(costumeId);
         int unlockLevel = costumeData.unlockLevel();
         boolean isCostumeUnlocked = playerCostumeState.isCostumeUnlocked(costumeId) || globalCredits > 0 || islandCredits > 0 || admin || unlockLevel != 0 && player.getBattleState().getLevel() >= unlockLevel || costumeData.ignoreLocks();
         if (activeIsland.isBattleIsland() && !isCostumeUnlocked) {
            throw new Exception("Costume is locked");
         } else {
            boolean hasTimedEventNow = CostumeAvailabilityEvent.hasTimedEventNow(costumeId, player, activeIsland.getType());
            if (!admin) {
               boolean canPurchase;
               if (costumeData.hidden()) {
                  canPurchase = hasTimedEventNow || islandCredits > 0 || globalCredits > 0;
               } else if (activeIsland.isBattleIsland()) {
                  canPurchase = isCostumeUnlocked;
               } else {
                  canPurchase = costumeData.alwaysVisible() || isCostumeUnlocked;
               }

               if (!canPurchase) {
                  throw new Exception("Costume is currently not available for purchase");
               }

               int diamondCost = 0;
               int medalCost = 0;
               int coinCost = 0;
               int etherealCost = 0;
               if (islandCredits > 0) {
                  islandCostumeState.removeCredit(costumeId, 1);
                  response.putInt("credits_used", 1);
                  response.putInt("credits_src", 0);
               } else if (globalCredits > 0) {
                  playerCostumeState.inventory().removeItem(costumeId, 1);
                  player.saveCostumes();
                  response.putInt("credits_used", 1);
                  response.putInt("credits_src", 1);
               } else {
                  diamondCost = costumeData.getDiamondCost();
                  medalCost = costumeData.getMedalCost();
                  if (CostumeSalesEvent.hasTimedEventNow(costumeData, player, activeIsland.getType())) {
                     if (diamondCost > 0) {
                        diamondCost = CostumeSalesEvent.getTimedEventSaleCost(costumeData, player, Player.CurrencyType.Diamonds, activeIsland.getType());
                     }

                     if (medalCost > 0) {
                        medalCost = CostumeSalesEvent.getTimedEventSaleCost(costumeData, player, Player.CurrencyType.Medals, activeIsland.getType());
                     }
                  }

                  float DEFAULT_MEDALS_TO_SHARDS;
                  if (!activeIsland.isBattleIsland()) {
                     if (costumeData.action() > 0) {
                        DEFAULT_MEDALS_TO_SHARDS = 0.2F;
                        diamondCost = (int)((float)diamondCost + (float)medalCost * GameSettings.get("USER_COSTUME_MEDALS_TO_DIAMONDS", 0.2F));
                     } else if (activeIsland.isEtherealIsland()) {
                        DEFAULT_MEDALS_TO_SHARDS = 0.25F;
                        etherealCost = (int)((float)etherealCost + (float)medalCost * GameSettings.get("USER_COSTUME_MEDALS_TO_SHARDS", 0.25F));
                     } else {
                        DEFAULT_MEDALS_TO_SHARDS = 500.0F;
                        coinCost = (int)((float)coinCost + (float)medalCost * GameSettings.get("USER_COSTUME_MEDALS_TO_COINS", 500.0F));
                     }

                     medalCost = 0;
                  }

                  if (!isCostumeUnlocked) {
                     DEFAULT_MEDALS_TO_SHARDS = 4.0F;
                     float priceModifier = GameSettings.get("USER_COSTUME_BUY_NOW_MULTIPLIER", 4.0F);
                     diamondCost = (int)((float)diamondCost * priceModifier);
                     medalCost = (int)((float)medalCost * priceModifier);
                     coinCost = (int)((float)coinCost * priceModifier);
                     etherealCost = (int)((float)etherealCost * priceModifier);
                  }

                  if (!player.canBuy((long)coinCost, (long)etherealCost, (long)diamondCost, 0L, 0L, 0L, medalCost)) {
                     throw new Exception("Insufficient funds to pay for costume");
                  }

                  player.chargePlayer(sender, this, coinCost, etherealCost, diamondCost, 0L, 0, 0, medalCost);
                  if (diamondCost > 0) {
                     JSONObject extra = new JSONObject();
                     extra.put("costume_id", costumeId);
                     this.logDiamondUsage(sender, "purchase_costume", diamondCost, player.getLevel(), extra, -1);
                  }

                  if (medalCost > 0) {
                     this.ext.stats.trackSpend(sender, "purchase_costume", "medals", (long)medalCost);
                  }
               }

               ISFSArray responseVars = new SFSArray();
               player.addPlayerPropertyData(responseVars, admin);
               response.putSFSArray("properties", responseVars);
               activeIsland.addToOwnedCostumes(costumeId);
               ISFSObject ownedCostumeData = new SFSObject();
               ownedCostumeData.putLong("island_id", activeIsland.getID());
               ownedCostumeData.putUtfString("costumes_owned", activeIsland.allPrevOwnedCostumes().toJson());
               this.send("gs_update_owned_costumes", ownedCostumeData, sender);
               monsterCostumeState.setPurchased(costumeId);
               if (metricsData != null && !admin) {
                  metricsData.put("diamonds_cost", diamondCost);
                  metricsData.put("medals_cost", medalCost);
                  metricsData.put("coins_cost", coinCost);
                  metricsData.put("ethereal_cost", coinCost);
               }
            } else {
               ISFSArray responseVars = new SFSArray();
               player.addPlayerPropertyData(responseVars, admin);
               response.putSFSArray("properties", responseVars);
               activeIsland.addToOwnedCostumes(costumeId);
               ISFSObject ownedCostumeData = new SFSObject();
               ownedCostumeData.putLong("island_id", activeIsland.getID());
               ownedCostumeData.putUtfString("costumes_owned", activeIsland.allPrevOwnedCostumes().toJson());
               this.send("gs_update_owned_costumes", ownedCostumeData, sender);
               monsterCostumeState.setPurchased(costumeId);
            }

         }
      }
   }

   private void purchaseCostume(User sender, ISFSObject params) {
      String requestID = params.getUtfString("__[[REQUEST_ID]]__");

      try {
         ISFSObject response = new SFSObject();
         JSONObject metricsParams = new JSONObject();
         int costumeId = params.getInt("costume_id");
         CostumeData costumeData = CostumeLookup.get(costumeId);
         if (costumeData == null) {
            this.ext.sendErrorResponse(requestID, "Costume not found", sender);
            return;
         }

         response.putInt("costume_id", costumeId);
         metricsParams.put("costume_id", costumeId);
         boolean admin = false;
         Player player;
         PlayerIsland activeIsland;
         long monsterId;
         if (!params.containsKey("user_bbb_id")) {
            player = (Player)sender.getProperty("player_object");
            activeIsland = player.getActiveIsland();
         } else {
            if (sender.getPrivilegeId() != 3) {
               Logger.trace("adminBuyEgg: Error! Trying to invoke admin without privileges!");
               return;
            }

            admin = true;
            player = (Player)sender.getProperty("friend_object");
            monsterId = params.getLong("user_island_id");
            response.putLong("user_island_id", monsterId);
            activeIsland = player.getIslandByID(monsterId);
         }

         monsterId = params.getLong("monster_id");
         PlayerMonster monster = activeIsland.getMonsterByID(monsterId);
         if (monster == null) {
            this.ext.sendErrorResponse(requestID, "Can't find monster to purchase costume for, monsterId: " + monsterId, sender);
            return;
         }

         response.putLong("monster_id", monsterId);
         if (!admin) {
            metricsParams.put("monster_id", monster.getType());
            metricsParams.put("user_monster_id", monster.getID());
            metricsParams.put("monster_level", monster.getLevel());
         }

         boolean autoEquip = params.containsKey("auto_equip") && params.getInt("auto_equip") == 1;
         MonsterCostumeState monsterCostumeState = monster.getCostumeState();

         try {
            this.purchaseCostume(sender, player, activeIsland, admin, costumeData, monsterCostumeState, response, metricsParams);
            if (autoEquip) {
               monsterCostumeState.setEquipped(costumeId);
               PlayerMonster goldMonster = monster.getGoldMonster(player);
               if (goldMonster != null) {
                  goldMonster.getCostumeState().setEquipped(costumeId);
                  ISFSObject updateGoldMonsterResponse = new SFSObject();
                  updateGoldMonsterResponse.putLong("user_monster_id", goldMonster.getID());
                  updateGoldMonsterResponse.putInt("equipped_costume", costumeId);

                  try {
                     PlayerIsland goldIsland = player.getIslandByIslandIndex(6);
                     this.ext.savePlayerIsland(player, goldIsland, false);
                  } catch (Exception var19) {
                     Logger.trace(var19);
                  }

                  this.send("gs_update_monster", updateGoldMonsterResponse, sender);
               }

               if (!admin) {
                  this.ext.stats.trackEquippedCostume(sender, monster, costumeId, monsterCostumeState.toSFSObject());
                  this.serverQuestEvent(sender, "equip_monst_costume", costumeId);
               }

               response.putInt("auto_equip", costumeId);
            }

            if (admin) {
               this.ext.savePlayerIsland(player, activeIsland, false);
            }

            response.putBool("success", true);
            this.send(requestID, response, sender);
            if (!admin) {
               metricsParams.put("costume", new JSONObject(monsterCostumeState.toSFSObject().toJson()));
               metricsParams.put("purchase_type", "costume");
               this.ext.stats.trackCostumePurchase(sender, metricsParams);
            }
         } catch (Exception var20) {
            this.ext.sendErrorResponse(requestID, var20.getMessage(), sender);
            return;
         }
      } catch (Exception var21) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         this.send(requestID, response, sender);
         Logger.trace(var21, "error purchasing costume", "   params : " + params.getDump());
      }

   }

   private void equipCostume(User sender, ISFSObject params) {
      try {
         ISFSObject response = new SFSObject();
         int costumeId = params.getInt("costume_id");
         CostumeData costumeData = null;
         if (costumeId != 0) {
            costumeData = CostumeLookup.get(costumeId);
            if (costumeData == null) {
               this.ext.sendErrorResponse("equip_costume", "Costume not found", sender);
               return;
            }
         }

         response.putInt("costume_id", costumeId);
         boolean admin = false;
         Player player;
         PlayerIsland activeIsland;
         long monsterId;
         if (params.containsKey("user_bbb_id")) {
            player = (Player)sender.getProperty("friend_object");
            monsterId = params.getLong("user_island_id");
            activeIsland = player.getIslandByID(monsterId);
            admin = true;
         } else {
            player = (Player)sender.getProperty("player_object");
            activeIsland = player.getActiveIsland();
         }

         monsterId = params.getLong("monster_id");
         PlayerMonster monster = activeIsland.getMonsterByID(monsterId);
         if (monster == null) {
            this.ext.sendErrorResponse("equip_costume", "Can't find monster to equip costume to, monsterId: " + monsterId, sender);
            return;
         }

         response.putLong("monster_id", monsterId);
         MonsterCostumeState monsterCostumeState = monster.getCostumeState();
         if (!monsterCostumeState.hasPurchased(costumeId)) {
            this.ext.sendErrorResponse("equip_costume", "Costume has not been purchased", sender);
            return;
         }

         monsterCostumeState.setEquipped(costumeId);
         if (!admin) {
            this.serverQuestEvent(sender, "equip_monst_costume", costumeId);
         }

         PlayerMonster goldMonster = monster.getGoldMonster(player);
         if (goldMonster != null) {
            goldMonster.getCostumeState().setEquipped(costumeId);
            ISFSObject updateGoldMonsterResponse = new SFSObject();
            updateGoldMonsterResponse.putLong("user_monster_id", goldMonster.getID());
            updateGoldMonsterResponse.putInt("equipped_costume", costumeId);

            try {
               PlayerIsland goldIsland = player.getIslandByIslandIndex(6);
               this.ext.savePlayerIsland(player, goldIsland, false);
            } catch (Exception var16) {
               Logger.trace(var16);
            }

            this.send("gs_update_monster", updateGoldMonsterResponse, sender);
         }

         if (admin) {
            this.ext.savePlayerIsland(player, activeIsland, false);
         }

         response.putBool("success", true);
         if (!admin) {
            this.ext.stats.trackEquippedCostume(sender, monster, costumeId, monsterCostumeState.toSFSObject());
         }

         String requestID = params.getUtfString("__[[REQUEST_ID]]__");
         this.send(requestID, response, sender);
      } catch (Exception var17) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         this.send("equip_costume", response, sender);
         Logger.trace(var17, "error equipping costume", "   params : " + params.getDump());
      }

   }

   private void purchaseBattleCampaignReward(User sender, ISFSObject params) {
      try {
         ISFSObject response = new SFSObject();
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland activeIsland = player.getActiveIsland();
         if (!activeIsland.isBattleIsland()) {
            this.ext.sendErrorResponse("battle_purchase_campaign_reward", "Can't complete a battle campaign on this island", sender);
            return;
         }

         int campaignId = params.getInt("campaign_id");
         BattleCampaignData campaignData = BattleCampaignLookup.get(campaignId);
         if (campaignData == null) {
            this.ext.sendErrorResponse("battle_purchase_campaign_reward", "Campaign not found", sender);
            return;
         }

         response.putInt("campaign_id", campaignId);
         Schedule schedule = ScheduleLookup.schedule(campaignData.getScheduleID());
         if (schedule == null) {
            this.ext.sendErrorResponse("battle_purchase_campaign_reward", "Campaign is not timed", sender);
            return;
         }

         if (!schedule.isActive()) {
            this.ext.sendErrorResponse("battle_purchase_campaign_reward", "Campaign reward time has ended", sender);
            return;
         }

         if (schedule.timeRemaining() > campaignData.getRewardTime()) {
            this.ext.sendErrorResponse("battle_purchase_campaign_reward", "Campaign has not ended yet", sender);
            return;
         }

         BattleIslandState battleIslandState = activeIsland.getBattleIslandState();
         BattleIslandCampaignState battleIslandCampaignState = battleIslandState.getCampaignState();
         if (battleIslandCampaignState.getCampaignStartTime(campaignId) < schedule.getCurrentRepeatStartTime()) {
            battleIslandCampaignState.resetCampaign(campaignId);
            battleIslandCampaignState.startCampaign(campaignId, MSMExtension.CurrentDBTime());
         }

         if (battleIslandCampaignState.hasPurchasedCampaignReward(campaignId)) {
            this.ext.sendErrorResponse("battle_purchase_campaign_reward", "Campaign reward already purchased", sender);
            return;
         }

         int cost = campaignData.getRewardCost();
         if (!player.canBuy(0L, 0L, (long)cost, 0L, 0L, 0L, 0)) {
            this.ext.sendErrorResponse("battle_purchase_campaign_reward", "Not enough diamonds to purchase campaign reward", sender);
            return;
         }

         player.adjustDiamonds(sender, this, -cost);
         this.logDiamondUsage(sender, "purchase_campaign_rewards", cost, player.getLevel());
         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         ISFSObject campaignReward = this.giveBattleReward(sender, campaignData.getReward(), player, params);
         response.putSFSObject("campaign_reward", campaignReward);
         battleIslandCampaignState.purchaseCampaignReward(campaignId);
         response.putBool("success", true);
         this.ext.stats.trackCampaignRewardPurchase(sender, campaignId, response);
         this.send("battle_purchase_campaign_reward", response, sender);
      } catch (Exception var14) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         this.send("battle_purchase_campaign_reward", response, sender);
         Logger.trace(var14, "error purchasing battle campaign reward", "   params : " + params.getDump());
      }

   }

   private void setBattleMusic(User sender, ISFSObject params) {
      try {
         ISFSObject response = new SFSObject();
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland activeIsland = player.getActiveIsland();
         if (!activeIsland.isBattleIsland()) {
            this.ext.sendErrorResponse("battle_set_music", "Can't set battle music on this island", sender);
            return;
         }

         if (params.containsKey("music_id")) {
            BattleIslandState battleIslandState = activeIsland.getBattleIslandState();
            BattleIslandMusicState musicState = battleIslandState.getMusicState();
            int musicId = params.getInt("music_id");
            if (musicId == 0) {
               musicState.setCurrentlyPlaying(0);
               musicState.setMuted(true);
            } else {
               musicState.setCurrentlyPlaying(1);
               musicState.setMuted(false);
            }
         } else {
            int currentlyPlaying = params.getInt("currently_playing");
            boolean muted = params.getBool("muted");
            if (currentlyPlaying == 0) {
               currentlyPlaying = 1;
            }

            BattleIslandState battleIslandState = activeIsland.getBattleIslandState();
            BattleIslandMusicState musicState = battleIslandState.getMusicState();
            musicState.setCurrentlyPlaying(currentlyPlaying);
            musicState.setMuted(muted);
            response.putInt("currently_playing", currentlyPlaying);
            response.putBool("muted", muted);
            this.ext.stats.trackBattleMusic(sender, currentlyPlaying);
         }

         response.putBool("success", true);
         this.send("battle_set_music", response, sender);
      } catch (Exception var10) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         this.send("battle_set_music", response, sender);
         Logger.trace(var10, "error purchasing costume", "   params : " + params.getDump());
      }

   }

   private void refreshBattleVersusAttempts(User sender, ISFSObject params) {
      try {
         ISFSObject response = new SFSObject();
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland activeIsland = player.getActiveIsland();
         if (!activeIsland.isBattleIsland()) {
            this.ext.sendErrorResponse("battle_refresh_versus_attempts", "Can't refresh battle versus attempts on this island", sender);
            return;
         }

         int campaignId = params.getInt("campaign_id");
         BattleCampaignData campaignData = BattleCampaignLookup.get(campaignId);
         if (campaignData == null) {
            this.ext.sendErrorResponse("battle_refresh_versus_attempts", "Campaign not found", sender);
            return;
         }

         response.putInt("campaign_id", campaignId);
         int scheduleId = campaignData.getScheduleID();
         Schedule scheduleData = ScheduleLookup.schedule(scheduleId);
         if (!scheduleData.isActive()) {
            this.ext.sendErrorResponse("battle_refresh_versus_attempts", "Schedule is not currently active!", sender);
            return;
         }

         BattlePlayerVersusState versusState = player.getLatestBattleVersusState(campaignId);
         if (versusState == null) {
            this.ext.sendErrorResponse("battle_refresh_versus_attempts", "Campaign State not found", sender);
            return;
         }

         if (versusState.getAttempts() > 0) {
            this.ext.sendErrorResponse("battle_refresh_versus_attempts", "Player has attempts remaining", sender);
            return;
         }

         boolean withDiamonds = true;
         if (params.containsKey("with_diamonds")) {
            withDiamonds = params.getBool("with_diamonds");
         }

         if (MSMExtension.CurrentDBTime() < versusState.getRefreshTimestamp()) {
            if (!withDiamonds) {
               this.ext.sendErrorResponse("battle_refresh_versus_attempts", "Must refresh with diamonds", sender);
               return;
            }

            int diamondCost = GameSettings.get("USER_BATTLE_VERSUS_REFRESH_COST", 20);
            if (!player.canBuy(0L, 0L, (long)diamondCost, 0L, 0L, 0L, 0)) {
               this.ext.sendErrorResponse("battle_refresh_versus_attempts", "Insufficient Funds", sender);
               return;
            }

            player.chargePlayer(sender, this, 0, 0, diamondCost, 0L, 0, 0, 0);
            this.logDiamondUsage(sender, "REQUEST_REFRESH_VERSUS_ATTEMPTS", diamondCost, player.getLevel());
            ISFSArray responseVars = new SFSArray();
            player.addPlayerPropertyData(responseVars, false);
            response.putSFSArray("properties", responseVars);
         }

         int attempts = 5;
         versusState.setAttempts(attempts);
         response.putInt("attempts", attempts);
         long newDayTimestamp = BattlePlayerVersusState.GetNewDayTimestamp();
         versusState.setRefreshTimestamp(newDayTimestamp);
         response.putLong("refreshes_on", newDayTimestamp);
         player.saveBattleVersusStates();
         response.putBool("success", true);
         this.send("battle_refresh_versus_attempts", response, sender);
      } catch (Exception var15) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         this.send("battle_refresh_versus_attempts", response, sender);
         Logger.trace(var15, "error refreshing battle versus attempts", "   params : " + params.getDump());
      }

   }

   ISFSObject coalesceRewardObjects(ISFSObject first, ISFSObject second) {
      Iterator itr = second.iterator();

      while(itr.hasNext()) {
         Entry<String, SFSDataWrapper> entry = (Entry)itr.next();
         String key = (String)entry.getKey();
         SFSDataWrapper secondData = (SFSDataWrapper)entry.getValue();
         if (first.containsKey(key)) {
            SFSDataWrapper firstData = first.get(key);
            if (firstData.getTypeId() == secondData.getTypeId()) {
               switch(firstData.getTypeId()) {
               case INT:
                  int firstInt = (Integer)firstData.getObject();
                  int secondInt = (Integer)secondData.getObject();
                  first.putInt(key, firstInt + secondInt);
               }
            }
         } else {
            first.put(key, secondData);
         }
      }

      return first;
   }

   private void claimBattleVersusRewards(User sender, ISFSObject params) {
      try {
         ISFSObject response = new SFSObject();
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland activeIsland = player.getActiveIsland();
         if (!activeIsland.isBattleIsland()) {
            this.ext.sendErrorResponse("battle_claim_versus_rewards", "Can't claim battle versus rewards on this island", sender);
            return;
         }

         int campaignId = params.getInt("campaign_id");
         BattleCampaignData campaignData = BattleCampaignLookup.get(campaignId);
         if (campaignData == null) {
            this.ext.sendErrorResponse("battle_claim_versus_rewards", "Campaign not found", sender);
            return;
         }

         response.putInt("campaign_id", campaignId);
         long scheduleStartTime = params.getLong("schedule_started_on");
         BattlePlayerVersusState versusState = null;
         if (scheduleStartTime == 0L) {
            versusState = player.getLatestBattleVersusState(campaignId);
         } else {
            versusState = player.getBattleVersusState(campaignId, scheduleStartTime);
         }

         if (versusState == null) {
            this.ext.sendErrorResponse("battle_claim_versus_rewards", "Campaign State not found", sender);
            return;
         }

         if (versusState.getClaimedTimestamp() > 0L) {
            this.ext.sendErrorResponse("battle_claim_versus_rewards", "Already Claimed!", sender);
            return;
         }

         int tier = versusState.getTier();
         response.putInt("tier", tier);
         List<BattleVersusTierData> tiers = campaignData.getTiers();
         ISFSObject rewardObject = new SFSObject();

         for(int i = 0; i < tiers.size(); ++i) {
            BattleVersusTierData tierData = (BattleVersusTierData)tiers.get(i);
            if (tierData.getStars() > 0 && tier >= i) {
               ISFSObject tierReward = this.giveBattleReward(sender, tierData.getReward(), player, params);
               rewardObject = this.coalesceRewardObjects((ISFSObject)rewardObject, tierReward);
            }
         }

         response.putSFSObject("season_rewards", (ISFSObject)rewardObject);
         long completedOn = versusState.getCompletedTimestamp();
         long rank;
         if (completedOn > 0L) {
            response.putLong("completed_on", completedOn);
            ISFSObject rewardObject = this.giveBattleReward(sender, campaignData.getReward(), player, params);
            response.putSFSObject("campaign_rewards", rewardObject);
            rank = 0L;
            BattleVersusChampionRanks.RankedUser rankedUser = BattleVersusChampionRanks.getRankedUser(campaignId, versusState.getScheduleTimestamp(), player.getPlayerId());
            if (rankedUser != null) {
               rank = rankedUser.rank();
               response.putLong("rank", rank);
            } else {
               rank = (long)(BattleVersusChampionRanks.getRankTable(campaignId, versusState.getScheduleTimestamp()).rankedList.size() + 1);
               response.putLong("rank", rank);
            }

            int champs = 0;

            for(int i = tiers.size() - 1; i >= 0; --i) {
               BattleVersusTierData tierData = (BattleVersusTierData)tiers.get(i);
               if (tierData.getStars() == 0) {
                  champs += tierData.getNumChamps();
                  if (rank <= (long)champs) {
                     rewardObject = this.giveBattleReward(sender, tierData.getReward(), player, params);
                     response.putSFSObject("champion_rewards", rewardObject);
                     break;
                  }
               }
            }
         }

         rank = MSMExtension.CurrentDBTime();
         versusState.setClaimedTimestamp(rank);
         response.putLong("claimed_on", rank);
         ISFSArray responseVars = new SFSArray();
         player.addPlayerPropertyData(responseVars, false);
         response.putSFSArray("properties", responseVars);
         player.saveBattleVersusStates();
         response.putBool("success", true);
         this.send("battle_claim_versus_rewards", response, sender);
         this.ext.stats.trackClaimVersusReward(sender, campaignId, scheduleStartTime, response);
      } catch (Exception var22) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         this.send("battle_claim_versus_rewards", response, sender);
         Logger.trace(var22, "error claiming battle versus rewards", "   params : " + params.getDump());
      }

   }

   private void updateViewedCampaigns(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      player.updateViewedCampaigns(params);
      this.send("update_viewed_campaigns", new SFSObject(), sender);
   }

   private void adminBattleCampaignReset(User sender, ISFSObject params) {
      try {
         ISFSObject response = new SFSObject();
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland activeIsland = player.getActiveIsland();
         if (!activeIsland.isBattleIsland()) {
            this.ext.sendErrorResponse("admin_battle_campaign_reset", "Can't reset battle campaign on this island", sender);
            return;
         }

         int campaignId = params.getInt("campaign_id");
         BattleIslandState battleIslandState = activeIsland.getBattleIslandState();
         BattleIslandCampaignState battleIslandCampaignState = battleIslandState.getCampaignState();
         battleIslandCampaignState.resetCampaign(campaignId);
         response.putInt("campaign_id", campaignId);
         response.putBool("success", true);
         this.send("admin_battle_campaign_reset", response, sender);
      } catch (Exception var9) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         this.send("admin_battle_campaign_reset", response, sender);
         Logger.trace(var9, "error resetting campaign", "   params : " + params.getDump());
      }

   }

   private void setPlayerAvatar(User sender, ISFSObject params) {
      try {
         ISFSObject response = new SFSObject();
         Player player = (Player)sender.getProperty("player_object");
         PlayerAvatar avatar = player.getAvatar();
         if (avatar == null) {
            this.ext.sendErrorResponse("gs_set_avatar", "Can't find avatar", sender);
            return;
         }

         int type = params.getInt("pp_type");
         avatar.setType(type);
         response.putInt("pp_type", type);
         String info = params.getUtfString("pp_info");
         avatar.setInfo(info);
         response.putUtfString("pp_info", info);
         player.saveAvatar();
         response.putBool("success", true);
         this.send("gs_set_avatar", response, sender);
      } catch (Exception var8) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         this.send("gs_set_avatar", response, sender);
         Logger.trace(var8, "error setting player avatar", "   params : " + params.getDump());
      }

   }

   private void viewedEgg(User sender, ISFSObject params) {
      try {
         long userEggId = params.getLong("user_egg_id");
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland activeIsland = player.getActiveIsland();
         PlayerEgg egg = activeIsland.getEgg(userEggId);
         if (egg == null) {
            throw new Exception("Couldn't find egg with id " + userEggId);
         }

         int monsterType = egg.getType();
         activeIsland.addToSoldMonsters(monsterType);
         SFSObject response;
         if (activeIsland.soldMonsterTypes() != null) {
            response = new SFSObject();
            response.putLong("island_id", activeIsland.getID());
            response.putUtfString("monsters_sold", activeIsland.soldMonsterTypes().toJson());
            this.send("gs_update_sold_monsters", response, sender);
         }

         response = new SFSObject();
         response.putBool("success", true);
         this.send("gs_viewed_egg", response, sender);
      } catch (Exception var10) {
         ISFSObject response = new SFSObject();
         response.putBool("success", false);
         this.send("gs_viewed_egg", response, sender);
         Logger.trace(var10, "error setting egg viewed", "   params : " + params.getDump());
      }

   }

   private void deleteAccount(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      SFSObject response = new SFSObject();

      try {
         String sql = "INSERT INTO user_banned SET bbb_id=?, reason=?, ban_date=NOW()";
         this.ext.getDB().update(sql, new Object[]{player.getBbbId(), "DELETE_ACCOUNT_REQUEST"});
         response.putBool("success", true);
      } catch (Exception var6) {
         response.putBool("success", false);
      }

      this.send("gs_delete_account", response, sender);
   }

   private boolean hasInventoryIslandItem(Player player, int entityId) {
      int amount = player.getInventory().getItemAmount(entityId);
      Monster monster = MonsterLookup.getFromEntityId(entityId);
      if (monster != null) {
         int baseMonsterId = MonsterIslandToIslandMapping.monsterSourceGivenAnyIsland(monster.getMonsterID());
         if (baseMonsterId != 0 && baseMonsterId != monster.getMonsterID()) {
            Monster baseMonster = MonsterLookup.get(baseMonsterId);
            amount += player.getInventory().getItemAmount(baseMonster.getEntityId());
         }
      }

      return amount > 0;
   }

   private void consumeInventoryIslandItem(Player player, int entityId) throws Exception {
      int amount = player.getInventory().getItemAmount(entityId);
      if (amount > 0) {
         player.getInventory().removeItem(entityId, 1);
         player.saveInventory();
      } else {
         Monster monster = MonsterLookup.getFromEntityId(entityId);
         if (monster != null) {
            int baseMonsterId = MonsterIslandToIslandMapping.monsterSourceGivenAnyIsland(monster.getMonsterID());
            if (baseMonsterId != 0 && baseMonsterId != monster.getMonsterID()) {
               Monster baseMonster = MonsterLookup.get(baseMonsterId);
               player.getInventory().removeItem(baseMonster.getEntityId(), 1);
               player.saveInventory();
               return;
            }
         }

         throw new Exception("Unable to use inventory item");
      }
   }

   private void updateAwakener(User sender, ISFSObject params) {
      Player player = (Player)sender.getProperty("player_object");
      PlayerIsland island = player.getActiveIsland();
      boolean success = true;
      String AWAKENED_STATE_KEY = "awakened_state";
      int awakenedState = params.getInt("awakened_state");
      SFSObject response = new SFSObject();

      try {
         PlayerStructure awakener = island.getAwakener();
         if (awakener == null) {
            response.putUtfString("error_msg", "MSG_AWAKENER_NONE");
            throw new Exception("An awakener is required to perform this action.");
         }

         Structure awakenerData = StructureLookup.getFromEntityId(awakener.getEntityId());
         int calendarId = awakenerData.getExtra().getInt("calendar");
         if (player.getDailyCumulativeLogin().getCalendarId() <= calendarId) {
            response.putUtfString("error_msg", "MSG_AWAKENER_LOCKED");
            throw new Exception("Linked calendar must be completed before sleeper can awaken");
         }

         ISFSObject awakenerExtra = awakener.getExtra();
         if (awakenerExtra == null) {
            awakenerExtra = new SFSObject();
            awakener.setExtra((ISFSObject)awakenerExtra);
         }

         ((ISFSObject)awakenerExtra).putInt("awakened_state", awakenedState);
         response.putInt("awakened_state", awakenedState);
         this.ext.savePlayerIsland(player, island, false);
      } catch (Exception var13) {
         success = false;
      }

      response.putBool("success", success);
      this.send("update_awakener", response, sender);
   }

   private void getDailyCumulativeLoginRewardsDB(User sender, ISFSObject params) {
      try {
         ISFSObject response = StaticData.getStaticData(sender, params, DailyCumulativeLoginLookup.getInstance());
         this.ext.multiSend("db_daily_cumulative_login", response, sender, DailyCumulativeLoginLookup.getInstance().getCacheName());
      } catch (Exception var4) {
         Logger.trace(var4);
      }

   }

   private void collectDailyCumulativeLoginRewards(User sender, ISFSObject params) {
      boolean success = true;
      Player player = (Player)sender.getProperty("player_object");
      ISFSObject lootResponse = new SFSObject();
      ISFSObject updateResponse = new SFSObject();
      SFSArray updateVars = new SFSArray();

      try {
         PlayerDailyCumulativeLogin dcl = player.getDailyCumulativeLogin();
         dcl.collect(player, lootResponse, sender, this);
         dcl.save(this.ext.getDB(), player.getPlayerId());
      } catch (Exception var12) {
         Logger.trace(var12);
         success = false;
      }

      lootResponse.putSFSObject("state", player.getDailyCumulativeLogin().toSFSObject());
      lootResponse.putBool("success", success);
      this.send("collect_daily_cumulative_login_rewards", lootResponse, sender);
      player.addPlayerPropertyData(updateVars, false);
      ISFSArray timedEvents = new SFSArray();
      PlayerTimedEvents playerTimedEvents = player.getTimedEvents();
      Iterator var10 = playerTimedEvents.getEvents().iterator();

      while(var10.hasNext()) {
         TimedEvent te = (TimedEvent)var10.next();
         timedEvents.addSFSObject(te.getSfsObject());
      }

      if (timedEvents.size() > 0) {
         ISFSObject timedEventsUpdate = new SFSObject();
         timedEventsUpdate.putSFSArray("timed_events", timedEvents);
         updateVars.addSFSObject(timedEventsUpdate);
      }

      if (lootResponse.containsKey("updateInventory") && player.getInventory() != null) {
         updateVars.addSFSObject(player.getInventory().toSFSObject());
      }

      if (lootResponse.containsKey("updateCostumes") && player.getCostumes() != null && player.getCostumes().inventory() != null) {
         int targetIsland = 0;
         if (lootResponse.containsKey("updateCostumesIsland")) {
            targetIsland = lootResponse.getInt("updateCostumesIsland");
         }

         if (targetIsland == 0) {
            ISFSObject costumes = new SFSObject();
            costumes.putSFSObject("costumes", player.getCostumes().inventory().toSFSObject());
            updateVars.addSFSObject(costumes);
         }
      }

      long serverTime = MSMExtension.CurrentDBTime();
      updateResponse.putLong("server_time", serverTime);
      updateResponse.putSFSArray("properties", updateVars);
      this.send("gs_update_properties", updateResponse, sender);
   }

   private void startAttuning(User sender, ISFSObject params) {
      long playerStructureId = params.getLong("user_structure_id");
      String startGene = params.getUtfString("start_gene");
      String endGene = params.getUtfString("end_gene");
      int clientAttunedIslandId = params.getInt("attuned_island_id");
      Player player = (Player)sender.getProperty("player_object");
      PlayerIsland playerIsland = player.getActiveIsland();
      ISFSObject attuneResponse = new SFSObject();
      boolean success = false;
      int cost = AttunerGeneLookup.getInstance().attuningCost(endGene);
      int duration = AttunerGeneLookup.getInstance().attuningDurationInHours(endGene);
      if (startGene == endGene) {
         this.ext.sendErrorResponse("gs_start_attuning", "Start and End genes cannot be the same", sender);
      } else {
         int serverAttunedIslandId = AttunerGeneLookup.getInstance().attunedIslandId();
         if (clientAttunedIslandId != serverAttunedIslandId) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.add(12, -5);
            AttunerGene attunerGene = AttunerGeneLookup.getInstance().activeAttunerGene(calendar);
            if (clientAttunedIslandId != attunerGene.getIslandId()) {
               this.ext.sendErrorResponse("gs_start_attuning", "Attuned island id mismatch", sender);
               return;
            }

            cost = AttunerGeneLookup.getInstance().attuningCost(endGene, attunerGene);
            duration = AttunerGeneLookup.getInstance().attuningDurationInHours(endGene, attunerGene);
         }

         if (player.getActualEthCurrency() < (long)cost) {
            this.ext.sendErrorResponse("gs_start_attuning", "You don't have enough shards to attune", sender);
         } else if ((PlayerAttuningData.isValidGene(startGene) || startGene.isEmpty()) && PlayerAttuningData.isValidGene(endGene)) {
            PlayerAttuningData attuningData = playerIsland.getAttuningData(playerStructureId);
            if (attuningData != null) {
               this.ext.sendErrorResponse("gs_start_attuning", "Attuner is already attuning", sender);
            } else if (playerIsland.hasAvailableCritter(startGene)) {
               try {
                  long durationMS = (long)(duration * 3600000);
                  long startTime = MSMExtension.CurrentDBTime();
                  attuningData = playerIsland.startAttuning(playerStructureId, startGene, endGene, startTime, startTime + durationMS, player);
                  player.adjustEthCurrency(sender, this, -cost);
                  ISFSArray responseVars = new SFSArray();
                  player.addPlayerPropertyData(responseVars, false);
                  attuneResponse.putSFSArray("properties", responseVars);
                  attuneResponse.putLong("user_structure_id", playerStructureId);
                  attuneResponse.putSFSObject("user_attuning_data", attuningData.getData());
                  this.ext.stats.trackAttuneStart(sender, cost, duration, attuningData.getData());
                  success = true;
               } catch (Exception var21) {
                  Logger.trace(var21, "error starting attuning", "   params : " + params.getDump());
               }

               attuneResponse.putBool("success", success);
               this.send("gs_start_attuning", attuneResponse, sender);
            } else {
               this.ext.sendErrorResponse("gs_start_attuning", "No Critters with requested gene available", sender);
            }
         } else {
            this.ext.sendErrorResponse("gs_start_attuning", "Invalid gene", sender);
         }
      }
   }

   private void finishAttuning(User sender, ISFSObject params) {
      try {
         long playerStructureId = params.getLong("user_structure_id");
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         PlayerAttuningData attuningData = island.getAttuningData(playerStructureId);
         if (attuningData == null) {
            this.ext.sendErrorResponse("gs_finish_attuning", "No attuning data for structure", sender);
            return;
         }

         String endGene = attuningData.endGene();
         if (!island.finishAttuning(playerStructureId, false)) {
            this.ext.sendErrorResponse("gs_finish_attuning", "The attuning has not yet completed", sender);
            return;
         }

         this.ext.stats.trackAttuneComplete(sender, attuningData.toSFSObject());
         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         response.putLong("user_structure_id", playerStructureId);
         response.putUtfString("end_gene", endGene);
         this.send("gs_finish_attuning", response, sender);
      } catch (Exception var10) {
         Logger.trace(var10, "error during finish attuning", "   params : " + params.getDump());
      }

   }

   private void speedUpAttuning(User sender, ISFSObject params) {
      try {
         long playerStructureId = params.getLong("user_structure_id");
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         PlayerAttuningData a = island.getAttuningData(playerStructureId);
         if (a == null) {
            this.ext.sendErrorResponse("gs_speedup_attuning", "Could not find attuning data for structure", sender);
            return;
         }

         ISFSObject response = new SFSObject();
         boolean isAttuning = false;
         long secondsRemaining = a.getTimeRemaining() / 1000L;
         if (secondsRemaining > 5L) {
            if (params.getInt("speed_up_type") != null && params.getInt("speed_up_type") != 0) {
               if (params.getInt("speed_up_type") != 1) {
                  this.ext.sendErrorResponse("gs_speedup_attuning", "Attuning is not done yet", sender);
                  return;
               }

               if (!player.hasSpeedUpCredit()) {
                  this.ext.sendErrorResponse("gs_speedup_attuning", "Unfortunately there are currently no offers available to you. Please try again later", sender);
                  return;
               }

               long initialTimeRemaining = secondsRemaining;
               a.reduceAttuningTimeByVideo();
               player.adjustSpeedUpCredit(sender, this, -1, Player.SPEED_UP_TYPES.VIDEO);
               secondsRemaining = a.getTimeRemaining() / 1000L;
               if (secondsRemaining > 5L) {
                  isAttuning = true;
               }

               this.ext.stats.trackSpeedupWithVideo(sender, "speedup_attuning_" + a.endGene(), 0L, 0L, Math.max(secondsRemaining, 0L), Math.max(initialTimeRemaining, 0L));
            } else {
               int diamondCost = Game.DiamondsRequiredToComplete(secondsRemaining * 1000L);
               if (!player.canBuy(0L, 0L, (long)diamondCost, 0L, 0L, 0L, 0)) {
                  this.ext.sendErrorResponse("gs_speedup_attuning", "Not enough diamonds to speed up attuning", sender);
                  return;
               }

               player.chargePlayer(sender, (GameStateHandler)null, 0, 0, diamondCost, 0L, 0, 0, 0);
               this.logDiamondUsage(sender, "speedup_attuning_" + a.endGene(), diamondCost, player.getLevel());
               ISFSArray responseVars = new SFSArray();
               player.addPlayerPropertyData(responseVars, false);
               response.putSFSArray("properties", responseVars);
            }
         }

         if (!isAttuning) {
            a.finishAttuningNow();
         }

         response.putBool("success", true);
         response.putLong("user_structure_id", playerStructureId);
         response.putLong("complete_on", a.getCompletionTime());
         response.putLong("started_on", a.getStartTime());
         this.send("gs_speedup_attuning", response, sender);
      } catch (Exception var14) {
         Logger.trace(var14, "error during finish attuning", "   params : " + params.getDump());
      }

   }

   private void startSynthesizing(User sender, ISFSObject params) {
      long playerStructureId = params.getLong("user_structure_id");
      Player player = (Player)sender.getProperty("player_object");
      PlayerIsland playerIsland = player.getActiveIsland();
      Island island = IslandLookup.get(playerIsland.getStaticId());
      String genes = params.getUtfString("genes");
      long playerMonsterId = params.getLong("user_monster_id");
      PlayerMonster playerMonster = player.getActiveIsland().getMonsterByID(playerMonsterId);
      String playerMonsterGenes = "";
      Monster targetMonster;
      if (playerMonster != null) {
         targetMonster = MonsterLookup.get(playerMonster.getType());
         playerMonsterGenes = targetMonster.getGenes();
         genes = genes + playerMonsterGenes;
      } else if (playerMonsterId != 0L) {
         this.ext.sendErrorResponse("gs_start_synthesizing", "Player doesn't own required monster", sender);
         return;
      }

      targetMonster = island.getMonsterWithGenes(genes);
      if (targetMonster == null) {
         this.ext.sendErrorResponse("gs_start_synthesizing", "No monsters has those genes", sender);
      } else {
         PlayerSynthesizingData synthesizingData = playerIsland.getSynthesizingData(playerStructureId);
         if (synthesizingData != null) {
            this.ext.sendErrorResponse("gs_start_synthesizing", "Synthesizer is already synthesizing", sender);
         } else {
            ISFSObject synthesizeResponse = new SFSObject();
            int numGenes = genes.length();
            if (numGenes != 3) {
               this.ext.sendErrorResponse("gs_start_synthesizing", "Number of genes not supported", sender);
            } else {
               int cost = SynthesizerSettings.GetCost(numGenes);
               int numPerGene = SynthesizerSettings.GetNumPerGene(numGenes);
               if (player.getActualEthCurrency() < (long)cost) {
                  this.ext.sendErrorResponse("gs_start_synthesizing", "You don't have enough shards to attune", sender);
               } else {
                  boolean hasEnoughCritters = true;

                  int instability;
                  for(instability = 0; instability < genes.length(); ++instability) {
                     String gene = Character.toString(genes.charAt(instability));
                     int availableCritters = playerIsland.numAttunedCritters(gene);
                     int numRequired = numPerGene;
                     if (playerMonsterGenes.contains(gene)) {
                        numRequired = numPerGene - 1;
                     }

                     if (availableCritters < numRequired) {
                        hasEnoughCritters = false;
                     }
                  }

                  if (!hasEnoughCritters) {
                     this.ext.sendErrorResponse("gs_start_synthesizing", "Not enough critters to synthesize", sender);
                  } else {
                     instability = this.getSynthesizerInstability(genes);
                     PlayerStructure pStruct = playerIsland.getSynthesizer();
                     Structure staticStruct = StructureLookup.get(pStruct.getType());
                     double maxInstability = (double)staticStruct.getExtra().getInt("max_instability");
                     if ((double)instability > maxInstability) {
                        this.ext.sendErrorResponse("gs_start_synthesizing", "Too unstable", sender);
                     } else {
                        float chance = this.getSynthesizerChance(pStruct, genes);
                        Random gRandom = new Random();
                        boolean synthesizingSuccess = gRandom.nextFloat() < chance;
                        if (playerMonster != null) {
                           Monster staticMonster = MonsterLookup.get(playerMonster.getType());
                           playerMonsterGenes = staticMonster.getGenes();
                           genes = genes + playerMonsterGenes;
                        }

                        HashMap<String, Integer> usedCritters = new HashMap();

                        for(int i = 0; i < numGenes; ++i) {
                           String gene = Character.toString(genes.charAt(i));
                           int numRequired = numPerGene;
                           if (playerMonsterGenes.contains(gene)) {
                              numRequired = numPerGene - 1;
                           }

                           playerIsland.removeAttunedCritters(gene, numRequired);
                           usedCritters.put(gene, numRequired);
                        }

                        try {
                           PlayerEgg newEgg = null;
                           if (synthesizingSuccess) {
                              newEgg = playerIsland.makeNewSynthesizerEgg(player, targetMonster.getMonsterID(), playerStructureId);
                              if (newEgg == null) {
                                 this.ext.sendErrorResponse("gs_start_synthesizing", "Error creating egg", sender);
                                 return;
                              }
                           }

                           long startTime = MSMExtension.CurrentDBTime();
                           long completionTime = newEgg != null ? newEgg.getCompletionTime() : startTime + (long)(SynthesizerSettings.GetFailDuration(numGenes) * 3600000);
                           long usedMonster = playerMonster != null ? playerMonster.getID() : 0L;
                           synthesizingData = new PlayerSynthesizingData(playerStructureId, targetMonster.getMonsterID(), startTime, completionTime, synthesizingSuccess, usedCritters, usedMonster);
                           playerIsland.addSynthesizingData(synthesizingData);
                           player.adjustEthCurrency(sender, this, -cost);
                           ISFSArray responseVars = new SFSArray();
                           player.addPlayerPropertyData(responseVars, false);
                           synthesizeResponse.putSFSArray("properties", responseVars);
                           synthesizeResponse.putLong("user_structure_id", playerStructureId);
                           synthesizeResponse.putSFSObject("user_synthesizing_data", synthesizingData.getData());
                           if (newEgg != null) {
                              synthesizeResponse.putSFSObject("user_egg", newEgg.getData());
                           }

                           synthesizeResponse.putBool("success", true);
                           this.send("gs_start_synthesizing", synthesizeResponse, sender);
                           this.ext.stats.trackSynthesizeStart(sender, cost, instability, (double)chance, synthesizingData.getData());
                        } catch (Exception var37) {
                           Logger.trace(var37, "error during finish attuning", "   params : " + params.getDump());
                        }

                     }
                  }
               }
            }
         }
      }
   }

   private void testSynthesis(User sender, ISFSObject params) {
      if (sender.getPrivilegeId() != 3) {
         ISFSObject response = new SFSObject();
         response.putUtfString("msg", "Error! Trying to invoke admin without privileges!");
         this.send("gs_display_generic_message", response, sender);
         this.ext.sendErrorResponse("gs_test_cruc_evolv", "Error! Trying to invoke admin without privileges!", sender);
      } else {
         long playerStructureId = params.getLong("user_structure_id");
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland playerIsland = player.getActiveIsland();
         Island island = IslandLookup.get(playerIsland.getStaticId());
         String genes = params.getUtfString("genes");
         long playerMonsterId = params.getLong("user_monster_id");
         PlayerMonster playerMonster = player.getActiveIsland().getMonsterByID(playerMonsterId);
         String playerMonsterGenes = "";
         Monster targetMonster;
         if (playerMonster != null) {
            targetMonster = MonsterLookup.get(playerMonster.getType());
            playerMonsterGenes = targetMonster.getGenes();
            genes = genes + playerMonsterGenes;
         } else if (playerMonsterId != 0L) {
            this.ext.sendErrorResponse("gs_start_synthesizing", "Player doesn't own required monster", sender);
            return;
         }

         targetMonster = island.getMonsterWithGenes(genes);
         if (targetMonster == null) {
            this.ext.sendErrorResponse("gs_start_synthesizing", "No monsters has those genes", sender);
         } else {
            PlayerSynthesizingData synthesizingData = playerIsland.getSynthesizingData(playerStructureId);
            if (synthesizingData != null) {
               this.ext.sendErrorResponse("gs_start_synthesizing", "Synthesizer is already synthesizing", sender);
            } else {
               ISFSObject synthesizeResponse = new SFSObject();
               int numGenes = genes.length();
               if (numGenes != 3) {
                  this.ext.sendErrorResponse("gs_start_synthesizing", "Number of genes not supported", sender);
               } else {
                  int cost = SynthesizerSettings.GetCost(numGenes);
                  int numPerGene = SynthesizerSettings.GetNumPerGene(numGenes);
                  if (player.getActualEthCurrency() < (long)cost) {
                     this.ext.sendErrorResponse("gs_start_synthesizing", "You don't have enough shards to attune", sender);
                  } else {
                     boolean hasEnoughCritters = true;

                     int instability;
                     for(instability = 0; instability < genes.length(); ++instability) {
                        String gene = Character.toString(genes.charAt(instability));
                        int availableCritters = playerIsland.numAttunedCritters(gene);
                        int numRequired = numPerGene;
                        if (playerMonsterGenes.contains(gene)) {
                           numRequired = numPerGene - 1;
                        }

                        if (availableCritters < numRequired) {
                           hasEnoughCritters = false;
                        }
                     }

                     if (!hasEnoughCritters) {
                        this.ext.sendErrorResponse("gs_start_synthesizing", "Not enough critters to synthesize", sender);
                     } else {
                        instability = this.getSynthesizerInstability(genes);
                        PlayerStructure pStruct = playerIsland.getSynthesizer();
                        Structure staticStruct = StructureLookup.get(pStruct.getType());
                        double maxInstability = (double)staticStruct.getExtra().getInt("max_instability");
                        if ((double)instability > maxInstability) {
                           this.ext.sendErrorResponse("gs_start_synthesizing", "Too unstable", sender);
                        } else {
                           this.doTestSynthRuns(pStruct, genes, synthesizeResponse);
                           if (playerMonster != null) {
                              Monster staticMonster = MonsterLookup.get(playerMonster.getType());
                              playerMonsterGenes = staticMonster.getGenes();
                              (new StringBuilder()).append(genes).append(playerMonsterGenes).toString();
                           }

                           synthesizeResponse.putBool("success", true);
                           this.send("gs_test_synthesis", synthesizeResponse, sender);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void doTestSynthRuns(PlayerStructure pStruct, String genes, ISFSObject response) {
      ArrayList<Integer> listOfResults = new ArrayList();
      listOfResults.add(0);
      listOfResults.add(0);
      int numRuns = 10000;

      for(int i = 0; i < numRuns; ++i) {
         float chance = this.getSynthesizerChance(pStruct, genes);
         Random gRandom = new Random();
         boolean synthesizingSuccess = gRandom.nextFloat() < chance;
         if (synthesizingSuccess) {
            listOfResults.set(1, (Integer)listOfResults.get(1) + 1);
         } else {
            listOfResults.set(0, (Integer)listOfResults.get(0) + 1);
         }
      }

      float percentageDivisor = (float)numRuns / 100.0F;
      SFSObject probs = new SFSObject();

      for(int i = 0; i < listOfResults.size(); ++i) {
         float percentProbability = (float)(Integer)listOfResults.get(i) / percentageDivisor;
         probs.putFloat(i != 0 ? "successProb" : "failProb", percentProbability);
      }

      response.putSFSObject("probabilities", probs);
   }

   private float getSynthesizerChance(PlayerStructure pStruct, String genes) {
      if (GameSettings.get("DEBUG_SYNTHESIZER_SUCCESS_CHANCE") != null && GameSettings.getFloat("DEBUG_SYNTHESIZER_SUCCESS_CHANCE") >= 0.0F) {
         return GameSettings.getFloat("DEBUG_SYNTHESIZER_SUCCESS_CHANCE");
      } else {
         float basePercentage = SynthesizerSettings.GetBasePercentage(genes.length());
         int instability = this.getSynthesizerInstability(genes);
         Structure staticStruct = StructureLookup.get(pStruct.getType());
         float maxInstability = (float)staticStruct.getExtra().getInt("max_instability");
         return basePercentage * (1.5F - (float)instability / maxInstability);
      }
   }

   private int getSynthesizerInstability(String genes) {
      int instability = 1;

      for(int i = 0; i < genes.length(); ++i) {
         instability *= AttunerGeneLookup.get(String.valueOf(genes.charAt(i))).getInstability();
      }

      return instability;
   }

   private void speedUpSynthesizing(User sender, ISFSObject params) {
      try {
         long playerStructureId = params.getLong("user_structure_id");
         Player player = (Player)sender.getProperty("player_object");
         PlayerIsland island = player.getActiveIsland();
         PlayerSynthesizingData synthesizingData = island.getSynthesizingData(playerStructureId);
         if (synthesizingData == null) {
            this.ext.sendErrorResponse("gs_speedup_synthesizing", "Could not find synthesizing data for structure", sender);
            return;
         }

         ISFSObject response = new SFSObject();
         boolean isSynthesizing = false;
         long secondsRemaining = synthesizingData.getTimeRemaining() / 1000L;
         if (secondsRemaining > 5L) {
            if (params.getInt("speed_up_type") != null && params.getInt("speed_up_type") != 0) {
               if (params.getInt("speed_up_type") != 1) {
                  this.ext.sendErrorResponse("gs_speedup_synthesizing", "Synthesizing is not done yet", sender);
                  return;
               }

               if (!player.hasSpeedUpCredit()) {
                  this.ext.sendErrorResponse("gs_speedup_synthesizing", "Unfortunately there are currently no offers available to you. Please try again later", sender);
                  return;
               }

               long initialTimeRemaining = secondsRemaining;
               synthesizingData.reduceAttuningTimeByVideo();
               if (synthesizingData.getSuccess()) {
                  PlayerEgg playerEgg = island.getEggByStructureId(playerStructureId);
                  playerEgg.reduceHatchingTimeByVideo();
                  response.putLong("user_egg_id", playerEgg.getID());
               }

               player.adjustSpeedUpCredit(sender, this, -1, Player.SPEED_UP_TYPES.VIDEO);
               secondsRemaining = synthesizingData.getTimeRemaining() / 1000L;
               if (secondsRemaining > 5L) {
                  isSynthesizing = true;
               }

               this.ext.stats.trackSpeedupWithVideo(sender, "speedup_sythesizing", 0L, 0L, Math.max(secondsRemaining, 0L), Math.max(initialTimeRemaining, 0L));
            } else {
               int diamondCost = Game.DiamondsRequiredToComplete(secondsRemaining * 1000L);
               if (!player.canBuy(0L, 0L, (long)diamondCost, 0L, 0L, 0L, 0)) {
                  this.ext.sendErrorResponse("gs_speedup_synthesizing", "Not enough diamonds to speed up attuning", sender);
                  return;
               }

               player.chargePlayer(sender, (GameStateHandler)null, 0, 0, diamondCost, 0L, 0, 0, 0);
               this.logDiamondUsage(sender, "speedup_sythesizing", diamondCost, player.getLevel(), MonsterLookup.get(synthesizingData.getMonster()).getEntityId());
               ISFSArray responseVars = new SFSArray();
               player.addPlayerPropertyData(responseVars, false);
               response.putSFSArray("properties", responseVars);
            }
         }

         if (!isSynthesizing) {
            synthesizingData.finishSynthesizingNow();
            if (synthesizingData.getSuccess()) {
               PlayerEgg playerEgg = island.getEggByStructureId(playerStructureId);
               playerEgg.finishHatchingNow();
               response.putLong("user_egg_id", playerEgg.getID());
            }

            this.ext.stats.trackSynthesizeComplete(sender, synthesizingData.getData());
         }

         response.putBool("success", true);
         response.putLong("user_structure_id", playerStructureId);
         response.putLong("complete_on", synthesizingData.getCompletionTime());
         response.putLong("started_on", synthesizingData.getStartTime());
         this.send("gs_speedup_synthesizing", response, sender);
      } catch (Exception var15) {
         Logger.trace(var15, "error during finish synthesizing", "   params : " + params.getDump());
      }

   }

   private void collectSynthesizingFailure(User sender, ISFSObject params) {
      long playerStructureId = params.getLong("user_structure_id");
      Player player = (Player)sender.getProperty("player_object");
      PlayerIsland playerIsland = player.getActiveIsland();
      PlayerSynthesizingData synthesizingData = playerIsland.getSynthesizingData(playerStructureId);
      if (synthesizingData == null) {
         this.ext.sendErrorResponse("gs_collect_synthesizing_failure", "No Synthesis data", sender);
      } else if (synthesizingData != null && synthesizingData.getTimeRemaining() > 0L) {
         this.ext.sendErrorResponse("gs_collect_synthesizing_failure", "Synthesis not done yet", sender);
      } else if (synthesizingData != null && synthesizingData.getSuccess()) {
         this.ext.sendErrorResponse("gs_collect_synthesizing_failure", "No critters reattuned, synthesis succeeded", sender);
      } else {
         playerIsland.removeSynthesizingData(synthesizingData);
         int numCrittersSynthesizing = synthesizingData.getUsedCritters();
         int numCrittersReattuned = false;
         int numCrittersReattuned;
         if (!player.hasCollectedQuest(505)) {
            numCrittersReattuned = numCrittersSynthesizing + 1;
         } else {
            float reattuneChance = gRandom.nextFloat();
            if (reattuneChance < 0.6F) {
               numCrittersReattuned = numCrittersSynthesizing - 1;
            } else if (reattuneChance < 0.9F) {
               numCrittersReattuned = numCrittersSynthesizing;
            } else {
               numCrittersReattuned = numCrittersSynthesizing + 1;
            }
         }

         numCrittersReattuned = numCrittersReattuned <= playerIsland.availableCritters() ? numCrittersReattuned : playerIsland.availableCritters();
         String genes = synthesizingData.getUsedCritterGenes();
         HashMap<String, Integer> reattunedCritters = new HashMap();
         int numUnattunedcritters = numCrittersSynthesizing - numCrittersReattuned;
         if (numUnattunedcritters > 0) {
            reattunedCritters.put("", numUnattunedcritters);
         }

         for(int i = 0; i < numCrittersReattuned; ++i) {
            int randomIndex = gRandom.nextInt(genes.length());
            String gene = String.valueOf(genes.charAt(randomIndex));
            int count = reattunedCritters.containsKey(gene) ? (Integer)reattunedCritters.get(gene) : 0;
            reattunedCritters.put(gene, count + 1);
         }

         ISFSArray critters = new SFSArray();
         Iterator var20 = reattunedCritters.entrySet().iterator();

         while(var20.hasNext()) {
            Entry<String, Integer> entry = (Entry)var20.next();
            if (PlayerAttuningData.isValidGene((String)entry.getKey())) {
               playerIsland.addAttunedCritters((String)entry.getKey(), (Integer)entry.getValue());
            }

            ISFSObject count = new SFSObject();
            count.putUtfString("gene", (String)entry.getKey());
            count.putInt("num", (Integer)entry.getValue());
            critters.addSFSObject(count);
         }

         this.serverQuestEvent(sender, "collect_synthesize_failure", 1);
         this.ext.stats.trackSynthesizeFailure(sender, synthesizingData.toSFSObject(), critters);
         ISFSObject response = new SFSObject();
         response.putBool("success", true);
         response.putLong("user_structure_id", playerStructureId);
         response.putSFSArray("reattuned_critters", critters);
         this.send("gs_collect_synthesizing_failure", response, sender);
      }
   }
}
