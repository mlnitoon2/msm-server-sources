package com.bigbluebubble.mysingingmonsters;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.DbWrapper;
import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.battle.BattleCampaignLookup;
import com.bigbluebubble.mysingingmonsters.battle.BattleLevelLookup;
import com.bigbluebubble.mysingingmonsters.battle.BattleMonsterActionLookup;
import com.bigbluebubble.mysingingmonsters.battle.BattleMonsterStatLookup;
import com.bigbluebubble.mysingingmonsters.battle.BattleMonsterTrainingLookup;
import com.bigbluebubble.mysingingmonsters.battle.BattleMusicLookup;
import com.bigbluebubble.mysingingmonsters.battle.BattleTrophyLookup;
import com.bigbluebubble.mysingingmonsters.costumes.CostumeLookup;
import com.bigbluebubble.mysingingmonsters.data.AttunerGeneLookup;
import com.bigbluebubble.mysingingmonsters.data.BreedingLookup;
import com.bigbluebubble.mysingingmonsters.data.DailyCumulativeLoginLookup;
import com.bigbluebubble.mysingingmonsters.data.DailyCurrencyPackLookup;
import com.bigbluebubble.mysingingmonsters.data.EntityAltCostLookup;
import com.bigbluebubble.mysingingmonsters.data.FlipBoardLookup;
import com.bigbluebubble.mysingingmonsters.data.FlipLevelLookup;
import com.bigbluebubble.mysingingmonsters.data.FlipPrizePoolLookup;
import com.bigbluebubble.mysingingmonsters.data.GeneLookup;
import com.bigbluebubble.mysingingmonsters.data.IslandLookup;
import com.bigbluebubble.mysingingmonsters.data.IslandThemeLookup;
import com.bigbluebubble.mysingingmonsters.data.LevelLookup;
import com.bigbluebubble.mysingingmonsters.data.Monster;
import com.bigbluebubble.mysingingmonsters.data.MonsterCommonToEpicMapping;
import com.bigbluebubble.mysingingmonsters.data.MonsterCommonToRareMapping;
import com.bigbluebubble.mysingingmonsters.data.MonsterFlexEggDefLookup;
import com.bigbluebubble.mysingingmonsters.data.MonsterIslandToIslandMapping;
import com.bigbluebubble.mysingingmonsters.data.MonsterLookup;
import com.bigbluebubble.mysingingmonsters.data.PrizeLookup;
import com.bigbluebubble.mysingingmonsters.data.PromoLookup;
import com.bigbluebubble.mysingingmonsters.data.ProperNouns;
import com.bigbluebubble.mysingingmonsters.data.QuestLookup;
import com.bigbluebubble.mysingingmonsters.data.StickerLookup;
import com.bigbluebubble.mysingingmonsters.data.StoreCurrencyLookup;
import com.bigbluebubble.mysingingmonsters.data.StoreGroupsLookup;
import com.bigbluebubble.mysingingmonsters.data.StoreItemsLookup;
import com.bigbluebubble.mysingingmonsters.data.StoreReplacements;
import com.bigbluebubble.mysingingmonsters.data.StructureLookup;
import com.bigbluebubble.mysingingmonsters.data.TorchLookup;
import com.bigbluebubble.mysingingmonsters.data.TribalQuestLookup;
import com.bigbluebubble.mysingingmonsters.data.VersionData;
import com.bigbluebubble.mysingingmonsters.data.groups.UserGroup;
import com.bigbluebubble.mysingingmonsters.data.loot.LootTableLookup;
import com.bigbluebubble.mysingingmonsters.player.PlayerFuzeBuddy;
import com.bigbluebubble.mysingingmonsters.schedules.ScheduleLookup;
import com.smartfoxserver.v2.db.DBConfig;

public class StaticDataSqlLoader {
   public static void loadAndCacheStaticData() {
      IDbWrapper db = getStaticDB();
      MSMExtension.cache_loaded_on = MSMExtension.CurrentDBTime();

      try {
         VersionData.Instance().init(db);
         MonsterLookup.init(db);
         StructureLookup.init(db);
         StoreReplacements.init(db);
         GeneLookup.init(db);
         BreedingLookup.init(db);
         IslandLookup.init(db);
         EntityAltCostLookup.init(db);
         IslandThemeLookup.init(db);
         TorchLookup.init(db);
         LevelLookup.init(db);
         StoreCurrencyLookup.init(db);
         StoreGroupsLookup.init(db);
         StoreItemsLookup.init(db);
         MonsterIslandToIslandMapping.init(db);
         MonsterCommonToRareMapping.init(db);
         MonsterCommonToEpicMapping.init(db);
         MonsterFlexEggDefLookup.init(db);
         FlipPrizePoolLookup.init(db);
         FlipBoardLookup.init(db);
         FlipLevelLookup.init(db);
         Monster.initGoldBoxMonsterRequirements();
         UserGroup.initStaticData(db);
         QuestLookup.init(db);
         TribalQuestLookup.init(db);
         PrizeLookup.init(db);
         StickerLookup.init(db);
         DailyCurrencyPackLookup.init(db);
         DailyCumulativeLoginLookup.init(db);
         CostumeLookup.init(db);
         ScheduleLookup.init(db);
         BattleTrophyLookup.init(db);
         BattleMonsterStatLookup.init(db);
         BattleMonsterActionLookup.init(db);
         BattleCampaignLookup.init(db);
         BattleLevelLookup.init(db);
         BattleMonsterTrainingLookup.init(db);
         BattleMusicLookup.init(db);
         PlayerFuzeBuddy.initStaticData();
         LootTableLookup.init(db);
         ProperNouns.init(db);
         AttunerGeneLookup.init(db);
         PromoLookup.init(db);
         MSMExtension.cache_loaded_on = MSMExtension.CurrentDBTime();
      } catch (Exception var2) {
         Logger.trace(var2);
      }

   }

   public static IDbWrapper getStaticDB() {
      if (GameSettings.get("STATIC_DB_CONNECTION_STRING") != null) {
         DBConfig dbConfig = new DBConfig();
         dbConfig.userName = GameSettings.get("STATIC_DB_USERNAME", "root");
         dbConfig.password = GameSettings.get("STATIC_DB_PASSWORD", "password");
         dbConfig.connectionString = GameSettings.get("STATIC_DB_CONNECTION_STRING");
         dbConfig.driverName = "org.gjt.mm.mysql.Driver";
         dbConfig.testSql = "SELECT NOW()";
         dbConfig.active = true;
         IDbWrapper staticDb = new DbWrapper(MSMExtension.getInstance(), dbConfig, MSMExtension.getInstance().getParentZone());
         boolean debugStaticDB = GameSettings.get("STATIC_DB_DEBUG", false);
         staticDb.setDebugMode(debugStaticDB);
         staticDb.setTimeQueries(false);
         return staticDb;
      } else {
         return MSMExtension.getInstance().getDB();
      }
   }
}
