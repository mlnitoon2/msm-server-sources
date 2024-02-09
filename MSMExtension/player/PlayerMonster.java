package com.bigbluebubble.mysingingmonsters.player;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.mysingingmonsters.GameStateHandler;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.costumes.MonsterCostumeState;
import com.bigbluebubble.mysingingmonsters.data.FlexEgg;
import com.bigbluebubble.mysingingmonsters.data.Monster;
import com.bigbluebubble.mysingingmonsters.data.MonsterCommonToEpicMapping;
import com.bigbluebubble.mysingingmonsters.data.MonsterCommonToRareMapping;
import com.bigbluebubble.mysingingmonsters.data.MonsterFlexEggDef;
import com.bigbluebubble.mysingingmonsters.data.MonsterFlexEggDefLookup;
import com.bigbluebubble.mysingingmonsters.data.MonsterLookup;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventManager;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.EvolveAvailabilityEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.ReturningUserBonusEvent;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PlayerMonster extends PlayerEntity {
   public static final String ID_KEY = "user_monster_id";
   public static final String TYPE_KEY = "monster";
   public static final String NAME_KEY = "name";
   public static final String LEVEL_KEY = "level";
   public static final String HAPPINESS_KEY = "happiness";
   public static final String TIMES_FED_KEY = "times_fed";
   public static final String COLLECTED_COINS_KEY = "collected_coins";
   public static final String COLLECTED_ETH_KEY = "collected_ethereal";
   public static final String COLLECTED_DIAMONDS_KEY = "collected_diamonds";
   public static final String COLLECTED_FOOD_KEY = "collected_food";
   public static final String COLLECTED_STARPOWER_KEY = "collected_starpower";
   public static final String COLLECTED_KEYS_KEY = "collected_keys";
   public static final String COLLECTED_RELICS_KEY = "collected_relics";
   public static final String VOLUME_KEY = "volume";
   public static final String LAST_FEEDING_KEY = "last_feeding";
   public static final String RANDOM_UNDERLING_COLLECTION_TIME_MIN = "random_underling_collection_min";
   public static final String GOLD_ISLAND_MONSTER_PARENT_ISLAND_KEY = "parent_island";
   public static final String GOLD_ISLAND_MONSTER_PARENT_MONSTER_KEY = "parent_monster";
   public static final String GOLD_ISLAND_MONSTER_GI_CHILD_ISLAND_KEY = "gi_child_island";
   public static final String GOLD_ISLAND_MONSTER_GI_CHILD_MONSTER_KEY = "gi_child_monster";
   public static final String BOXED_EGGS_KEY = "boxed_eggs";
   public static final String BOXED_REQUIREMENTS_KEY = "box_requirements";
   public static final String MARKED_FOR_DELETE_KEY = "delete";
   public static final String IN_HOTEL_KEY = "in_hotel";
   public static final String MEGA_KEY = "megamonster";
   public static final String EGG_TIMER_START = "egg_timer_start";
   public static final String UNDERLING_COLLECTION_TYPE_KEY = "collection_type";
   public static final String UNDERLING_EVOLVE_UNLOCKED = "evolve_unlocked";
   public static final String CELESTIAL_POWERUP_UNLOCKED = "powerup_unlocked";
   public static final String HAS_EVOLVE_REQS_KEY = "has_evolve_reqs";
   public static final String HAS_EVOLVE_FLEXEGGS_KEY = "has_evolve_flexeggs";
   public static final String UNDERLING_COLLECTION_HAPPINESS = "underling_collection_happiness";
   private static Random random_ = null;
   private static final String LEVEL_DIAMOND_AMOUNT_KEY = "amount_diamonds";
   private static final String LEVEL_COINS_AMOUNT_KEY = "amount_coins_hrly";
   private static final String LEVEL_SHARDS_AMOUNT_KEY = "amount_shards_hrly";
   private static final String LEVEL_FOOD_AMOUNT_KEY = "amount_food_hrly";
   private static final String LEVEL_STARPOWER_AMOUNT_KEY = "amount_starpower_hrly";
   private static final String LEVEL_KEYS_AMOUNT_KEY = "amount_keys";
   private static final String LEVEL_DIAMOND_CHANCE_KEY = "chance_diamonds";
   private static final String LEVEL_COINS_CHANCE_KEY = "chance_coins";
   private static final String LEVEL_SHARDS_CHANCE_KEY = "chance_shards";
   private static final String LEVEL_FOOD_CHANCE_KEY = "chance_food";
   private static final String LEVEL_STARPOWER_CHANCE_KEY = "chance_starpower";
   private static final String LEVEL_KEYS_CHANCE_KEY = "chance_keys";
   private static PlayerMonster.UnderlingCollectionType[] cachedCollectionTypes_ = null;
   private PlayerMonster.UnderlingCollectionType currentCollectionType;
   private int randomUnderlingTimeToCollect;
   private int underlingCollectionHappiness;
   private static int underlingTimeToCollectH2MModifier = 60;
   private static final String[] clientCurrencyKeys = new String[]{"diamond", "ethereal_currency", "star", "food", "coins", "key", "relic", ""};
   private static final String[] amountKeys = new String[]{"amount_diamonds", "amount_shards_hrly", "amount_starpower_hrly", "amount_food_hrly", "amount_coins_hrly", "amount_keys"};
   private static final String[] chanceKeys = new String[]{"chance_diamonds", "chance_shards", "chance_starpower", "chance_food", "chance_coins", "chance_keys"};
   private String name;
   private long user_monster_id;
   private long last_feeding;
   private int collected_coins;
   private int collected_eth;
   private int collected_diamonds;
   private int collected_food;
   private int collected_starpower;
   private int collected_keys;
   private float collected_relics;
   private short monster;
   private short level;
   private short happiness;
   private short times_fed;
   private float volume;
   private boolean in_hotel;
   private long parent_island;
   private long parent_monster;
   private long gi_child_island;
   private long gi_child_monster;
   private boolean markedForDeletion;
   protected ISFSObject boxMonsterData;
   private ISFSArray boxedEggs;
   protected MonsterMegaData megaData;
   protected long eggTimerStart;
   protected boolean evolutionUnlocked;
   protected boolean powerupUnlocked;
   private ISFSArray evolveReqsMetStatic;
   private ISFSArray evolveReqsMetFlex;
   Random rng;
   private int debuggingCurIslandIndex;
   public static final String IS_TRAINING_KEY = "is_training";
   public static final String TRAINING_START_KEY = "training_start";
   public static final String TRAINING_COMPLETION_KEY = "training_completion";
   private boolean is_training;
   private long training_start;
   private long training_completion;
   public static final String COSTUME_KEY = "costume";
   MonsterCostumeState costumeState_;

   private static Random GetRandom() {
      if (random_ == null) {
         random_ = new Random(MSMExtension.CurrentDBTime());
      }

      return random_;
   }

   private static PlayerMonster.UnderlingCollectionType[] CachedCollectionTypes() {
      if (cachedCollectionTypes_ == null) {
         cachedCollectionTypes_ = PlayerMonster.UnderlingCollectionType.values();
      }

      return cachedCollectionTypes_;
   }

   public static String getCurrencyStrFromType(PlayerMonster.UnderlingCollectionType type) {
      return clientCurrencyKeys[type.ordinal()];
   }

   public ISFSArray boxedEggs() {
      return this.boxedEggs;
   }

   public ISFSArray evolveReqsMetStatic() {
      return this.evolveReqsMetStatic;
   }

   public ISFSArray evolveReqsMetFlex() {
      return this.evolveReqsMetFlex;
   }

   public boolean hasAllEvolveEggs() {
      Monster staticMonster = MonsterLookup.get(this.getType());
      if (this.evolveReqsMetStatic != null && this.evolveReqsMetStatic.size() < staticMonster.getEvolveReqsStatic().size()) {
         return false;
      } else {
         return this.evolveReqsMetFlex == null || this.evolveReqsMetFlex.size() >= staticMonster.getEvolveReqsFlex().size();
      }
   }

   public boolean evolveStarted() {
      if (this.evolveReqsMetStatic != null && this.evolveReqsMetStatic.size() > 0) {
         return true;
      } else {
         return this.evolveReqsMetFlex != null && this.evolveReqsMetFlex.size() > 0;
      }
   }

   public static void setDebugFasterUnderlingCurrencyGen(boolean faster) {
      if (!faster) {
         underlingTimeToCollectH2MModifier = 60;
      } else {
         underlingTimeToCollectH2MModifier = 1;
      }

   }

   public boolean debugFasterCurrencyGen() {
      return underlingTimeToCollectH2MModifier != 60;
   }

   public PlayerMonster(ISFSObject monsterData, PlayerIsland pi) {
      super(monsterData);
      this.currentCollectionType = PlayerMonster.UnderlingCollectionType.MaxCollectionTypes;
      this.randomUnderlingTimeToCollect = 0;
      this.underlingCollectionHappiness = 0;
      this.parent_island = 0L;
      this.parent_monster = 0L;
      this.gi_child_island = 0L;
      this.gi_child_monster = 0L;
      this.markedForDeletion = false;
      this.boxMonsterData = null;
      this.boxedEggs = null;
      this.megaData = null;
      this.eggTimerStart = -1L;
      this.evolutionUnlocked = false;
      this.powerupUnlocked = false;
      this.evolveReqsMetStatic = null;
      this.evolveReqsMetFlex = null;
      this.rng = new Random();
      this.debuggingCurIslandIndex = 0;
      this.initDefaultValues();
      this.initFromSFSObject(monsterData, pi);
   }

   public static SFSObject createMonsterSFS(long monsterIndex, PlayerBuyback pbb, PlayerIsland island, int xPos, int yPos, int flip) {
      Monster monsterData = MonsterLookup.getFromEntityId((int)pbb.getID());
      String boxData = null;
      if (monsterData.isBoxMonsterType() && pbb.getBoxedEggs() != null) {
         boxData = pbb.getBoxedEggs().toJson();
      }

      SFSObject newMonsterData = createMonsterSFS(monsterData.getMonsterID(), pbb.getName(), pbb.getMegaMonsterData(), pbb.getCostumeData(), island.getIndex(), island.getID(), xPos, yPos, flip, monsterIndex, pbb.getLevel(), MSMExtension.CurrentDBTime(), MSMExtension.CurrentDBTime(), false, (long)pbb.getTimesFed(), boxData, pbb.getEvolutionUnlocked(), pbb.getPowerupUnlocked());
      newMonsterData.putInt("book_value", pbb.bookValue());
      return newMonsterData;
   }

   public static SFSObject createMonsterSFS(int monsterId, String name, ISFSObject megaData, ISFSObject costume, int islandInd, long islandId, int posX, int posY, int flip, long uid, int level, long lastFeeding, long lastCollection, boolean muted, long timesFed, String boxData, boolean evolutionUnlocked, boolean powerupUnlocked) {
      SFSObject newMonsterData = new SFSObject();
      newMonsterData.putInt("monster", monsterId);
      newMonsterData.putUtfString("name", name);
      if (megaData != null) {
         newMonsterData.putSFSObject("megamonster", megaData);
      }

      newMonsterData.putLong("island", islandId);
      newMonsterData.putInt("level", level);
      newMonsterData.putInt("pos_x", posX);
      newMonsterData.putInt("pos_y", posY);
      newMonsterData.putInt("flip", flip);
      newMonsterData.putLong("last_feeding", lastFeeding);
      newMonsterData.putLong("last_collection", lastCollection);
      newMonsterData.putLong("user_monster_id", uid);
      newMonsterData.putInt("muted", muted ? 1 : 0);
      newMonsterData.putLong("times_fed", timesFed);
      if (boxData != null) {
         newMonsterData.putUtfString("boxed_eggs", boxData);
      }

      if (evolutionUnlocked) {
         newMonsterData.putInt("evolve_unlocked", evolutionUnlocked ? 1 : 0);
      }

      if (powerupUnlocked) {
         newMonsterData.putInt("powerup_unlocked", powerupUnlocked ? 1 : 0);
      }

      if (costume != null) {
         newMonsterData.putSFSObject("costume", costume);
      }

      Monster staticMonster;
      if (islandInd != 10 && islandInd != 12) {
         if (islandInd == 22 && (boxData == null || !boxData.isEmpty())) {
            staticMonster = MonsterLookup.get(monsterId);
            newMonsterData.putLong("egg_timer_start", getAmberEggTimer(staticMonster, islandInd));
         }
      } else {
         staticMonster = MonsterLookup.get(monsterId);
         ISFSObject levelData = staticMonster.getLevel(level);
         newMonsterData.putUtfString("collection_type", clientCurrencyKeys[GetRandomCollectionType(levelData).ordinal()]);
         newMonsterData.putInt("random_underling_collection_min", getRandomUnderlingCollectTime(0));
         if (boxData == null && staticMonster.isEvolvable() && !staticMonster.evolveEnabled()) {
            newMonsterData.putLong("egg_timer_start", getEvolveAvailabilityEggTimer(staticMonster, islandInd));
         } else {
            newMonsterData.putLong("egg_timer_start", -1L);
         }
      }

      return newMonsterData;
   }

   public void addAmberEggTimer(int islandType) {
      Monster staticMonster = MonsterLookup.get(this.getType());
      if (staticMonster.getViewInMarket() == 0 && staticMonster.getViewInStarmarket() == 0) {
         this.eggTimerStart = getAmberEggTimer(staticMonster, islandType);
      } else {
         this.eggTimerStart = -1L;
      }

   }

   public void addCelestialEggTimer(int islandType) {
      if (!this.hasAllEvolveEggs()) {
         this.eggTimerStart = getEvolveAvailabilityEggTimer(MonsterLookup.get(this.getType()), islandType);
      } else {
         this.eggTimerStart = -1L;
      }

   }

   public static long getAmberEggTimer(Monster staticMonster, int islandType) {
      if (staticMonster.getViewInMarket() == 0 && staticMonster.getViewInStarmarket() == 0) {
         List<TimedEvent> te = TimedEventManager.instance().currentActiveOnKey(TimedEventType.EntityStoreAvailability, staticMonster.getEntityId(), islandType);
         Iterator it = te.iterator();

         while(it.hasNext()) {
            TimedEvent curTE = (TimedEvent)it.next();
            if (curTE != null) {
               return curTE.getStartDate();
            }
         }
      }

      return -1L;
   }

   public static long getEvolveAvailabilityEggTimer(Monster staticMonster, int islandType) {
      if (!staticMonster.evolveEnabled()) {
         List<TimedEvent> te = TimedEventManager.instance().currentActiveOnKey(TimedEventType.EvolveAvailability, staticMonster.getEntityId(), islandType);
         Iterator it = te.iterator();

         while(it.hasNext()) {
            TimedEvent curTE = (TimedEvent)it.next();
            if (curTE != null) {
               return curTE.getStartDate();
            }
         }
      }

      return -1L;
   }

   public void startEggTimer(PlayerIsland boxingIsland) {
      if (this.eggTimerStart == -1L && this.hasEggTimer(boxingIsland)) {
         if (boxingIsland.isUnderlingIsland()) {
            this.eggTimerStart = MSMExtension.CurrentDBTime();
         } else if (boxingIsland.isAmberIsland()) {
            this.eggTimerStart = getAmberEggTimer(MonsterLookup.get(this.getType()), boxingIsland.getIndex());
         } else if (boxingIsland.isCelestialIsland()) {
            if (this.isInactiveBoxMonster()) {
               this.eggTimerStart = MSMExtension.CurrentDBTime();
            } else {
               this.eggTimerStart = getEvolveAvailabilityEggTimer(MonsterLookup.get(this.getType()), boxingIsland.getIndex());
            }
         }
      }

   }

   public long eggTimerStart() {
      return this.eggTimerStart;
   }

   public boolean hasEggTimer(PlayerIsland pi) {
      if (pi.isAmberIsland()) {
         return this.isInactiveBoxMonster() && !this.hasAllRequiredEggs(false) && !this.isAmberEvolvable();
      } else if (pi.isCelestialIsland()) {
         if (this.isInactiveBoxMonster()) {
            return !this.hasAllRequiredEggs(false) && this.boxedEggs.size() != 0 && !MonsterLookup.get(this.getType()).isWubbox();
         } else {
            return this.isEvolvable() && !MonsterLookup.get(this.getType()).evolveEnabled() && !this.hasAllEvolveEggs();
         }
      } else if (!pi.isUnderlingIsland()) {
         return false;
      } else {
         return this.isInactiveBoxMonster() && !this.hasAllRequiredEggs(false) && this.boxedEggs.size() != 0 && !MonsterLookup.get(this.getType()).isWubbox();
      }
   }

   public int collectionOfDyingFetuses(PlayerIsland island, Player player) {
      int coinReward = this.clearRottenEggs(island, player);
      if (island.isCelestialIsland() && !this.isInactiveBoxMonster() && this.isEvolvable() && !MonsterLookup.get(this.getType()).evolveEnabled()) {
         this.eggTimerStart = getEvolveAvailabilityEggTimer(MonsterLookup.get(this.getType()), island.getType());
      } else {
         this.eggTimerStart = -1L;
      }

      return coinReward;
   }

   public void resetEggTimer() {
      this.eggTimerStart = -1L;
   }

   private int clearRottenEggs(PlayerIsland island, Player player) {
      int coinCost = 0;
      if (this.isInactiveBoxMonster()) {
         coinCost = this.boxedEggsBuyPrice(island);
         this.setBoxMonsterData(new SFSArray(), false);
      } else {
         Monster staticMonster = MonsterLookup.get(this.getType());
         if (staticMonster.isEvolvable()) {
            coinCost = this.evolveEggsBuyPrice(island);
            if (staticMonster.getEvolveReqsStatic() != null) {
               this.evolveReqsMetStatic = new SFSArray();
            }

            if (staticMonster.getEvolveReqsFlex() != null) {
               this.evolveReqsMetFlex = new SFSArray();
            }

            if (!staticMonster.evolveEnabled() && EvolveAvailabilityEvent.hasTimedEventNow(staticMonster, player, island.getType())) {
               this.startEggTimer(island);
            }
         }
      }

      this.last_collection = MSMExtension.CurrentDBTime();
      this.collected_coins = 0;
      this.collected_eth = 0;
      this.collected_diamonds = 0;
      this.collected_food = 0;
      this.collected_starpower = 0;
      this.collected_keys = 0;
      this.collected_relics = 0.0F;
      this.resetUnderlingCurrencyType(island);
      return coinCost;
   }

   private void initDefaultValues() {
      this.level = 1;
      this.happiness = 0;
      this.times_fed = 0;
      this.collected_coins = 0;
      this.collected_eth = 0;
      this.collected_diamonds = 0;
      this.collected_food = 0;
      this.collected_starpower = 0;
      this.collected_keys = 0;
      this.collected_relics = 0.0F;
      this.muted = false;
      this.flip = false;
      this.volume = 1.0F;
      this.in_hotel = false;
      this.currentCollectionType = PlayerMonster.UnderlingCollectionType.MaxCollectionTypes;
      this.randomUnderlingTimeToCollect = 0;
      this.underlingCollectionHappiness = 0;
   }

   private void initFromSFSObject(ISFSObject s, PlayerIsland pi) {
      this.name = s.getUtfString("name");
      this.user_monster_id = SFSHelpers.getLong("user_monster_id", s);
      this.island = SFSHelpers.getLong("island", s);
      this.last_feeding = SFSHelpers.getLong("last_feeding", s);
      this.last_collection = SFSHelpers.getLong("last_collection", s);
      this.monster = (short)((int)SFSHelpers.getLong("monster", s));
      this.pos_x = (short)((int)SFSHelpers.getLong("pos_x", s));
      this.pos_y = (short)((int)SFSHelpers.getLong("pos_y", s));
      if (s.containsKey("level")) {
         this.level = (short)((int)SFSHelpers.getLong("level", s));
      }

      if (s.containsKey("happiness")) {
         this.happiness = (short)((int)SFSHelpers.getLong("happiness", s));
      }

      if (s.containsKey("times_fed")) {
         this.times_fed = (short)((int)SFSHelpers.getLong("times_fed", s));
      }

      if (s.containsKey("collected_coins")) {
         this.collected_coins = SFSHelpers.getInt("collected_coins", s);
      }

      if (s.containsKey("collected_ethereal")) {
         this.collected_eth = SFSHelpers.getInt("collected_ethereal", s);
      }

      if (s.containsKey("collected_diamonds")) {
         this.collected_diamonds = SFSHelpers.getInt("collected_diamonds", s);
      }

      if (s.containsKey("collected_food")) {
         this.collected_food = SFSHelpers.getInt("collected_food", s);
      }

      if (s.containsKey("collected_starpower")) {
         this.collected_starpower = SFSHelpers.getInt("collected_starpower", s);
      }

      if (s.containsKey("collected_keys")) {
         this.collected_keys = SFSHelpers.getInt("collected_keys", s);
      }

      if (s.containsKey("collected_relics")) {
         this.collected_relics = (float)SFSHelpers.getDouble("collected_relics", s);
      }

      if (s.containsKey("muted")) {
         this.muted = SFSHelpers.getInt("muted", s) != 0;
      }

      if (s.containsKey("flip")) {
         this.flip = SFSHelpers.getInt("flip", s) != 0;
      }

      if (s.containsKey("volume")) {
         this.volume = (float)SFSHelpers.getDouble("volume", s);
      }

      if (s.containsKey("in_hotel")) {
         this.in_hotel = SFSHelpers.getInt("in_hotel", s) != 0;
      }

      if (s.containsKey("parent_island")) {
         this.parent_island = SFSHelpers.getLong("parent_island", s);
      }

      if (s.containsKey("parent_monster")) {
         this.parent_monster = SFSHelpers.getLong("parent_monster", s);
      }

      if (s.containsKey("gi_child_island")) {
         this.gi_child_island = SFSHelpers.getLong("gi_child_island", s);
      }

      if (s.containsKey("gi_child_monster")) {
         this.gi_child_monster = SFSHelpers.getLong("gi_child_monster", s);
      }

      if (s.containsKey("delete")) {
         this.markedForDeletion = SFSHelpers.getInt("delete", s) != 0;
      }

      if (s.containsKey("boxed_eggs") && s.getUtfString("boxed_eggs").length() != 0) {
         this.setBoxMonsterData(SFSArray.newFromJsonData(s.getUtfString("boxed_eggs")), pi.isGoldIsland());
      } else {
         this.setBoxMonsterData((ISFSArray)null, pi.isGoldIsland());
      }

      if (s.containsKey("has_evolve_reqs")) {
         this.evolveReqsMetStatic = SFSArray.newFromJsonData(s.getUtfString("has_evolve_reqs"));
      } else if (!MonsterLookup.get(this.getType()).isEvolvable()) {
         this.evolveReqsMetStatic = null;
      } else {
         this.evolveReqsMetStatic = new SFSArray();
      }

      if (s.containsKey("has_evolve_flexeggs")) {
         this.evolveReqsMetFlex = SFSArray.newFromJsonData(s.getUtfString("has_evolve_flexeggs"));
      } else if (!MonsterLookup.get(this.getType()).isEvolvable()) {
         this.evolveReqsMetFlex = null;
      } else {
         this.evolveReqsMetFlex = new SFSArray();
      }

      if (s.containsKey("evolve_unlocked")) {
         this.evolutionUnlocked = s.getInt("evolve_unlocked") != 0;
      }

      if (s.containsKey("powerup_unlocked")) {
         this.powerupUnlocked = s.getInt("powerup_unlocked") != 0;
      }

      if (s.containsKey("megamonster")) {
         this.megaData = new MonsterMegaData(s.getSFSObject("megamonster"));
      }

      if (pi.isBattleIsland() && s.containsKey("is_training")) {
         this.is_training = SFSHelpers.getInt("is_training", s) != 0;
         this.training_start = SFSHelpers.getLong("training_start", s);
         this.training_completion = SFSHelpers.getLong("training_completion", s);
      }

      if (s.containsKey("costume")) {
         this.costumeState_ = new MonsterCostumeState(s.getSFSObject("costume"));
      } else {
         this.costumeState_ = new MonsterCostumeState();
      }

      if (s.containsKey("book_value")) {
         this.bookValue = s.getInt("book_value");
      }

      if (s.containsKey("egg_timer_start")) {
         if (this.hasEggTimer(pi)) {
            this.eggTimerStart = s.getLong("egg_timer_start");
         }
      } else if (this.hasEggTimer(pi)) {
         if (pi.isAmberIsland()) {
            if (this.boxedEggs != null && !this.hasAllRequiredEggs(false)) {
               this.eggTimerStart = getAmberEggTimer(MonsterLookup.get(this.getType()), pi.getType());
            }
         } else if (pi.isCelestialIsland() && !this.isInactiveBoxMonster() && this.isEvolvable() && !MonsterLookup.get(this.getType()).evolveEnabled()) {
            this.eggTimerStart = getEvolveAvailabilityEggTimer(MonsterLookup.get(this.getType()), pi.getType());
         }
      }

      if (pi.isRandomCollectionIsland()) {
         if (s.containsKey("collection_type")) {
            boolean found = false;
            String currencyTypeStr = SFSHelpers.getUtfString("collection_type", s);

            int min;
            for(min = 0; min < clientCurrencyKeys.length; ++min) {
               if (currencyTypeStr.compareTo(clientCurrencyKeys[min]) == 0) {
                  this.currentCollectionType = CachedCollectionTypes()[min];
                  Monster staticMonster = MonsterLookup.get(this.monster);
                  ISFSObject levelData = staticMonster.getLevel(this.level);
                  if (!this.validCollectionType(this.currentCollectionType, levelData)) {
                     this.currentCollectionType = GetRandomCollectionType(levelData);
                  }

                  found = true;
                  break;
               }
            }

            if (!found) {
               Logger.trace("Underling monster data has invalid collection_type: " + currencyTypeStr);
               ISFSObject levelData = MonsterLookup.get(this.monster).getLevel(this.level);
               this.currentCollectionType = GetRandomCollectionType(levelData);
               this.randomUnderlingTimeToCollect = 0;
            } else {
               if (s.containsKey("random_underling_collection_min")) {
                  this.randomUnderlingTimeToCollect = s.getInt("random_underling_collection_min");
               } else {
                  this.randomUnderlingTimeToCollect = 0;
               }

               if (s.containsKey("underling_collection_happiness")) {
                  this.underlingCollectionHappiness = s.getInt("underling_collection_happiness");
               } else {
                  this.underlingCollectionHappiness = 0;
               }

               if (!this.validRandomTime(this.randomUnderlingTimeToCollect)) {
                  min = (int)((float)GameSettings.getInt("MIN_RANDOM_MINUTES_FOR_UNDERLING_COLLECTION") / (60.0F / (float)underlingTimeToCollectH2MModifier));
                  int max = (int)((float)GameSettings.getInt("MAX_RANDOM_MINUTES_FOR_UNDERLING_COLLECTION") / (60.0F / (float)underlingTimeToCollectH2MModifier));
                  if (this.randomUnderlingTimeToCollect < min) {
                     this.randomUnderlingTimeToCollect = min;
                  } else if (this.randomUnderlingTimeToCollect > max) {
                     this.randomUnderlingTimeToCollect = max;
                  }

                  this.underlingCollectionHappiness = this.happiness;
               }
            }
         } else if (!this.isHibernating()) {
            if (this.currentCollectionType == PlayerMonster.UnderlingCollectionType.MaxCollectionTypes) {
               Logger.trace("Underling monster is missing collection_type");
               Monster staticMonster = MonsterLookup.get(this.getType());
               ISFSObject levelData = staticMonster.getLevel(this.getLevel());
               this.currentCollectionType = GetRandomCollectionType(levelData);
               this.randomUnderlingTimeToCollect = getRandomUnderlingCollectTime(this.happiness);
               this.underlingCollectionHappiness = this.happiness;
            }
         } else {
            this.currentCollectionType = PlayerMonster.UnderlingCollectionType.MaxCollectionTypes;
            this.randomUnderlingTimeToCollect = 0;
         }
      }

      this.checkEggIntegrity(pi.isGoldIsland());
   }

   public void checkEggIntegrity(boolean goldIsland) {
      Monster staticMonster = MonsterLookup.get(this.getType());
      ISFSArray boxReqs = this.getBoxRequirements(goldIsland);
      ISFSArray reqsStatic = staticMonster.getEvolveReqsStatic();
      ISFSArray reqsFlex = staticMonster.getEvolveReqsFlex();
      if (reqsStatic == null && reqsFlex == null) {
         if (this.evolveReqsMetStatic != null) {
            this.evolveReqsMetStatic = null;
         }

         if (this.evolveReqsMetFlex != null) {
            this.evolveReqsMetFlex = null;
         }
      }

      if (boxReqs == null && this.boxedEggs != null) {
         this.boxedEggs = null;
      }

      if (this.isInactiveBoxMonster()) {
         this.checkBoxedEggIntegrity(boxReqs);
      } else if (staticMonster.isEvolvable()) {
         this.checkEvolveEggIntegrityStatic(reqsStatic);
         this.checkEvolveEggIntegrityFlex(reqsFlex);
      }

   }

   private static ArrayList<Integer> getInvalidBoxedEggs(ISFSArray reqs, ISFSArray possessed) {
      ArrayList<Integer> removeIds = new ArrayList();
      if (possessed == null) {
         return removeIds;
      } else {
         HashMap<Integer, Integer> requiredEggs = new HashMap();

         for(int index = 0; index < reqs.size(); ++index) {
            Integer monsterType = reqs.getInt(index);
            if (!requiredEggs.containsKey(monsterType)) {
               requiredEggs.put(monsterType, 1);
            } else {
               requiredEggs.put(monsterType, (Integer)requiredEggs.get(monsterType) + 1);
            }
         }

         HashMap<Integer, Integer> hasEggs = new HashMap();

         Integer eggId;
         for(int index = 0; index < possessed.size(); ++index) {
            eggId = possessed.getInt(index);
            if (!hasEggs.containsKey(eggId)) {
               hasEggs.put(eggId, 1);
            } else {
               hasEggs.put(eggId, (Integer)hasEggs.get(eggId) + 1);
            }
         }

         Iterator hasMonsterIds = hasEggs.keySet().iterator();

         while(true) {
            while(hasMonsterIds.hasNext()) {
               eggId = (Integer)hasMonsterIds.next();
               int removeNum;
               int i;
               if (!requiredEggs.containsKey(eggId)) {
                  removeNum = (Integer)hasEggs.get(eggId);

                  for(i = 0; i < removeNum; ++i) {
                     removeIds.add(eggId);
                  }
               } else if ((Integer)requiredEggs.get(eggId) < (Integer)hasEggs.get(eggId)) {
                  removeNum = (Integer)hasEggs.get(eggId) - (Integer)requiredEggs.get(eggId);

                  for(i = 0; i < removeNum; ++i) {
                     removeIds.add(eggId);
                  }
               }
            }

            return removeIds;
         }
      }
   }

   private boolean checkBoxedEggIntegrity(ISFSArray reqs) {
      if (this.boxedEggs == null) {
         return false;
      } else {
         ArrayList<Integer> removeIds = getInvalidBoxedEggs(reqs, this.boxedEggs);
         boolean correctionNeeded = false;
         Iterator removeItr = removeIds.iterator();

         while(true) {
            while(removeItr.hasNext()) {
               Integer removeId = (Integer)removeItr.next();

               for(int i = 0; i < this.boxedEggs.size(); ++i) {
                  if (this.boxedEggs.getInt(i).compareTo(removeId) == 0) {
                     correctionNeeded = true;
                     this.boxedEggs.removeElementAt(i);
                     break;
                  }
               }
            }

            return correctionNeeded;
         }
      }
   }

   private boolean checkEvolveEggIntegrityStatic(ISFSArray reqsStatic) {
      if (this.evolveReqsMetStatic != null && reqsStatic != null) {
         ArrayList<Integer> removeIds = getInvalidBoxedEggs(reqsStatic, this.evolveReqsMetStatic);
         boolean correctionNeeded = false;
         Iterator removeItr = removeIds.iterator();

         while(true) {
            while(removeItr.hasNext()) {
               Integer removeId = (Integer)removeItr.next();

               for(int i = 0; i < this.evolveReqsMetStatic.size(); ++i) {
                  if (this.evolveReqsMetStatic.getInt(i).compareTo(removeId) == 0) {
                     correctionNeeded = true;
                     this.evolveReqsMetStatic.removeElementAt(i);
                     break;
                  }
               }
            }

            return correctionNeeded;
         }
      } else {
         return false;
      }
   }

   private boolean checkEvolveEggIntegrityFlex(ISFSArray reqsFlex) {
      if (this.evolveReqsMetFlex != null && reqsFlex != null) {
         ArrayList<Integer> removeIds = getInvalidBoxedEggs(reqsFlex, this.evolveReqsMetFlex);
         boolean correctionNeeded = false;
         Iterator removeItr = removeIds.iterator();

         while(true) {
            while(removeItr.hasNext()) {
               Integer removeId = (Integer)removeItr.next();

               for(int i = 0; i < this.evolveReqsMetFlex.size(); ++i) {
                  if (this.evolveReqsMetFlex.getInt(i).compareTo(removeId) == 0) {
                     correctionNeeded = true;
                     this.evolveReqsMetFlex.removeElementAt(i);
                     break;
                  }
               }
            }

            return correctionNeeded;
         }
      } else {
         return false;
      }
   }

   public ISFSObject toSFSObject(PlayerIsland pi) {
      ISFSObject s = this.toSFSObjectWithoutBoxRequirements(pi);

      try {
         if (this.boxMonsterData != null && this.boxMonsterData.size() > 0) {
            if (this.boxMonsterData.containsKey("box_requirements")) {
               s.putUtfString("box_requirements", this.boxMonsterData.getUtfString("box_requirements"));
            }

            if (s.containsKey("boxed_eggs") && this.boxMonsterData.containsKey("boxed_eggs") && s.getUtfString("boxed_eggs").compareTo(this.boxMonsterData.getUtfString("boxed_eggs")) != 0) {
               throw new Exception("boxed eggs don't match: " + s.getUtfString("boxed_eggs") + " != " + this.boxMonsterData.getUtfString("boxed_eggs"));
            }

            if (!s.containsKey("boxed_eggs") && this.boxMonsterData.containsKey("boxed_eggs")) {
               throw new Exception("s doesn't contain boxed eggs, boxMonsterData does");
            }

            if (s.containsKey("boxed_eggs") && !this.boxMonsterData.containsKey("boxed_eggs")) {
               throw new Exception("s contains boxed eggs, boxMonsterData doesn't");
            }
         }
      } catch (Exception var4) {
         Logger.trace(var4);
      }

      return s;
   }

   public void DebuggingIslandSet(int islandIndex) {
      this.debuggingCurIslandIndex = islandIndex;
   }

   public ISFSObject toSFSObjectWithoutBoxRequirements(PlayerIsland pi) {
      ISFSObject s = super.toSFSObject(pi);
      if (this.getName() != null) {
         s.putUtfString("name", this.getName());
      }

      s.putLong("user_monster_id", this.user_monster_id);
      s.putLong("last_feeding", this.last_feeding);
      if (this.currentCollectionType != PlayerMonster.UnderlingCollectionType.MaxCollectionTypes && this.boxedEggs != null) {
         s.putLong("last_collection", MSMExtension.CurrentDBTime());
      } else {
         s.putLong("last_collection", this.last_collection);
      }

      s.putInt("monster", this.monster);
      s.putInt("level", this.level);
      s.putInt("happiness", this.happiness);
      s.putInt("times_fed", this.times_fed);
      if (this.collected_coins != 0) {
         s.putInt("collected_coins", this.collected_coins);
      }

      if (this.collected_eth != 0) {
         s.putInt("collected_ethereal", this.collected_eth);
      }

      if (this.collected_diamonds != 0) {
         s.putInt("collected_diamonds", this.collected_diamonds);
      }

      if (this.collected_food != 0) {
         s.putInt("collected_food", this.collected_food);
      }

      if (this.collected_starpower != 0) {
         s.putInt("collected_starpower", this.collected_starpower);
      }

      if (this.collected_keys != 0) {
         s.putInt("collected_keys", this.collected_keys);
      }

      if (this.collected_relics != 0.0F) {
         s.putDouble("collected_relics", (double)this.collected_relics);
      }

      s.putInt("in_hotel", this.in_hotel ? 1 : 0);
      s.putDouble("volume", (double)this.volume);
      if (this.markedForDeletion) {
         s.putInt("delete", this.markedForDeletion ? 1 : 0);
      }

      if (this.parent_island != 0L) {
         s.putLong("parent_island", this.parent_island);
      }

      if (this.parent_monster != 0L) {
         s.putLong("parent_monster", this.parent_monster);
      }

      if (this.gi_child_island != 0L) {
         s.putLong("gi_child_island", this.gi_child_island);
      }

      if (this.gi_child_monster != 0L) {
         s.putLong("gi_child_monster", this.gi_child_monster);
      }

      if (this.is_training) {
         s.putInt("is_training", 1);
         s.putLong("training_start", this.training_start);
         s.putLong("training_completion", this.training_completion);
      }

      if (this.megaData != null && !this.megaData.megaExpired(MSMExtension.CurrentDBTime())) {
         s.putSFSObject("megamonster", this.megaData.toSFSObject());
      }

      String blah;
      if (this.boxMonsterData != null) {
         blah = this.boxMonsterData.getUtfString("boxed_eggs");
         if (blah != null) {
            s.putUtfString("boxed_eggs", blah);
         }
      }

      if (this.evolveReqsMetStatic != null) {
         blah = this.evolveReqsMetStatic.toJson();
         if (blah != null) {
            s.putUtfString("has_evolve_reqs", blah);
         }
      }

      if (this.evolveReqsMetFlex != null) {
         blah = this.evolveReqsMetFlex.toJson();
         if (blah != null) {
            s.putUtfString("has_evolve_flexeggs", blah);
         }
      }

      if (this.evolutionUnlocked) {
         s.putInt("evolve_unlocked", 1);
      }

      if (this.powerupUnlocked) {
         s.putInt("powerup_unlocked", 1);
      }

      if (this.currentCollectionType != PlayerMonster.UnderlingCollectionType.MaxCollectionTypes) {
         s.putUtfString("collection_type", clientCurrencyKeys[this.currentCollectionType.ordinal()]);
      } else if (this.debuggingCurIslandIndex == 10 || this.debuggingCurIslandIndex == 12) {
         Logger.trace("==========ERROR IN toSFSObjectWithoutBoxData: PlayerMonster underling currentCollectionType = UnderlingCollectionTypes.MaxCollectionTypes");
      }

      if (this.randomUnderlingTimeToCollect != 0) {
         s.putInt("random_underling_collection_min", this.randomUnderlingTimeToCollect);
         s.putInt("underling_collection_happiness", this.underlingCollectionHappiness);
      }

      if (this.eggTimerStart != -1L) {
         if (this.hasEggTimer(pi)) {
            s.putLong("egg_timer_start", this.eggTimerStart);
         } else {
            this.eggTimerStart = -1L;
         }
      }

      if (this.costumeState_ != null) {
         s.putSFSObject("costume", this.costumeState_.toSFSObject());
      }

      return s;
   }

   public ISFSObject getMegaSFS() {
      return this.megaData == null ? null : this.megaData.toSFSObject();
   }

   public ISFSObject getCostumeSFS() {
      return this.costumeState_ == null ? null : this.costumeState_.toSFSObject();
   }

   public String getName() {
      return this.name;
   }

   public long getID() {
      return this.user_monster_id;
   }

   public int getType() {
      return this.monster;
   }

   public long getLastCollectedTime() {
      return this.last_collection;
   }

   public int getLevel() {
      return this.level;
   }

   public long getParentID() {
      return this.island;
   }

   public int getTimesFed() {
      return this.times_fed;
   }

   public int getCollectedCoins() {
      return this.collected_coins;
   }

   public int getCollectedKeys() {
      return this.collected_keys;
   }

   public int getCollectedEth() {
      return this.collected_eth;
   }

   public float getCollectedRelics() {
      return this.collected_relics;
   }

   public int getCollectedDiamonds() {
      return this.collected_diamonds;
   }

   public int getCollectedFood() {
      return this.collected_food;
   }

   public int getCollectedStarpower() {
      return this.collected_starpower;
   }

   public float getVolume() {
      return this.volume;
   }

   public int getHappiness() {
      return this.happiness;
   }

   public int getMuted() {
      return this.muted ? 1 : 0;
   }

   private void levelUp(Player p, PlayerIsland island) {
      this.resetTimesFed();
      this.stashCurrency(p, island);
      ++this.level;
   }

   public void setLevel(int lvl) {
      this.resetTimesFed();
      this.level = (short)lvl;
   }

   public SFSObject getGoldIslandData() {
      if (this.gi_child_island > 0L) {
         SFSObject blah = new SFSObject();
         blah.putLong("island", this.gi_child_island);
         blah.putLong("monster", this.gi_child_monster);
         return blah;
      } else {
         return null;
      }
   }

   public void removeGoldIslandData() {
      this.gi_child_island = 0L;
      this.gi_child_monster = 0L;
   }

   public SFSObject getParentIslandData() {
      if (this.parent_island > 0L) {
         SFSObject blah = new SFSObject();
         blah.putLong("island", this.parent_island);
         blah.putLong("monster", this.parent_monster);
         return blah;
      } else {
         return null;
      }
   }

   public void markForDeletion() {
      this.markedForDeletion = true;
   }

   public boolean inHotel() {
      return this.in_hotel;
   }

   public void setID(long ID) {
      this.user_monster_id = ID;
   }

   public void setVolume(float v) {
      this.volume = v;
   }

   public void setHappiness(int h) {
      if (this.boxedEggs == null) {
         this.happiness = (short)h;
      }

   }

   private void resetTimesFed() {
      this.times_fed = 0;
   }

   public void setName(String n) {
      this.name = n;
   }

   public void toggleMute() {
      this.muted = !this.muted;
   }

   public boolean feed(Player p, PlayerIsland island) {
      ++this.times_fed;
      this.last_feeding = MSMExtension.CurrentDBTime();
      int feedingsPerLevel = GameSettings.getInt("MONSTER_FEEDINGS_PER_LEVEL");
      if (this.getTimesFed() >= feedingsPerLevel) {
         this.levelUp(p, island);
         return true;
      } else {
         return false;
      }
   }

   public boolean tribalFeed() {
      ++this.times_fed;
      this.last_feeding = MSMExtension.CurrentDBTime();
      int feedingsPerLevel = GameSettings.getInt("MONSTER_FEEDINGS_PER_LEVEL");
      if (this.getTimesFed() >= feedingsPerLevel) {
         this.resetTimesFed();
         ++this.level;
         return true;
      } else {
         return false;
      }
   }

   public void setGoldIslandData(long goldIslandId, long goldMonsterId) {
      this.gi_child_island = goldIslandId;
      this.gi_child_monster = goldMonsterId;
   }

   public void setParentIslandData(long parentIslandId, long parentMonsterId) {
      this.parent_island = parentIslandId;
      this.parent_monster = parentMonsterId;
   }

   public boolean isAmberEvolvable() {
      return this.isEvolvable() && !this.inHotel() && this.level >= GameSettings.getInt("USER_CRUCIBLE_MONSTER_MIN_LEVEL");
   }

   public int getEvolveKeyCost(boolean overheating) {
      if (!overheating) {
         return MonsterLookup.get(this.monster).getEvolveKeyCost();
      } else {
         int evolveInto = MonsterLookup.get(this.monster).evolveInto();
         if (evolveInto == 0) {
            return MonsterLookup.get(this.monster).getEvolveKeyCost();
         } else {
            return this.isCommon() ? MonsterLookup.getFromEntityId(evolveInto).getEvolveKeyCost() : MonsterLookup.get(this.monster).getEvolveKeyCost();
         }
      }
   }

   public int getPowerupKeyCost() {
      return GameSettings.get("USER_RARE_CELESTIAL_POWERUP_KEY_COST", 10);
   }

   public boolean isUnderlingEvolveUnlocked() {
      return this.evolutionUnlocked;
   }

   public boolean isCelestialPowerupUnlocked() {
      return this.powerupUnlocked;
   }

   public void setUnlockUnderlingEvolution(boolean set) {
      this.evolutionUnlocked = set;
   }

   public void setUnlockCelestialPowerup(boolean set) {
      this.powerupUnlocked = set;
   }

   public boolean isEvolvable() {
      return !this.isInactiveBoxMonster() && MonsterLookup.get(this.getType()).isEvolvable();
   }

   public boolean evolveExpired(int islandType) {
      if (this.evolveReqsMetStatic == null && this.evolveReqsMetFlex == null) {
         return false;
      } else if (!this.isEvolvable()) {
         return false;
      } else if (MonsterLookup.get(this.getType()).evolveEnabled()) {
         return false;
      } else {
         ISFSArray reqsStatic = MonsterLookup.get(this.getType()).getEvolveReqsStatic();
         ISFSArray reqsFlex = MonsterLookup.get(this.getType()).getEvolveReqsFlex();
         if (reqsStatic == null && reqsFlex == null) {
            return false;
         } else if ((this.evolveReqsMetStatic == null || this.evolveReqsMetStatic.size() == reqsStatic.size()) && (this.evolveReqsMetFlex == null || this.evolveReqsMetFlex.size() == reqsFlex.size())) {
            return false;
         } else if (this.eggTimerStart == -1L) {
            return false;
         } else {
            if (TimedEventManager.instance().hasTimedEventNow(TimedEventType.EvolveAvailability, this.getEntityId(), islandType)) {
               List<TimedEvent> currentEvents = TimedEventManager.instance().currentActiveOnKey(TimedEventType.EvolveAvailability, this.getEntityId(), islandType);

               for(int i = 0; i < currentEvents.size(); ++i) {
                  if (this.eggTimerStart == ((TimedEvent)currentEvents.get(i)).getStartDate()) {
                     return false;
                  }
               }
            }

            return true;
         }
      }
   }

   public boolean isInactiveBoxMonster() {
      return this.boxedEggs != null;
   }

   public boolean isInactiveBoxMonster(boolean rare, boolean epic) {
      if (this.isInactiveBoxMonster()) {
         if (rare) {
            return this.isRare();
         } else {
            return epic ? this.isEpic() : this.isCommon();
         }
      } else {
         return false;
      }
   }

   public void setBoxMonsterData(ISFSArray possessedEggs, boolean goldIsland) {
      if (this.boxMonsterData == null) {
         this.boxMonsterData = new SFSObject();
      }

      ISFSArray boxReqsStatic = null;
      if (possessedEggs != null && possessedEggs.size() != 0) {
         this.boxedEggs = new SFSArray();
         boxReqsStatic = null;
         Monster m = MonsterLookup.get(this.getType());
         if (!this.isEvolvable() && m.isBoxMonsterType()) {
            boxReqsStatic = this.getBoxRequirements(goldIsland);
         }

         if (boxReqsStatic != null) {
            for(int i = 0; i < possessedEggs.size(); ++i) {
               boolean found = false;

               for(int j = 0; j < boxReqsStatic.size(); ++j) {
                  if (possessedEggs.getInt(i) == boxReqsStatic.getInt(j)) {
                     found = true;
                     break;
                  }
               }

               if (found) {
                  this.boxedEggs.addInt(possessedEggs.getInt(i));
               }
            }
         }
      } else {
         this.boxedEggs = possessedEggs;
         boxReqsStatic = this.getBoxRequirements(goldIsland);
      }

      if (this.boxedEggs != null) {
         this.boxMonsterData.putUtfString("boxed_eggs", this.boxedEggs.toJson());
      }

      if (boxReqsStatic != null) {
         this.boxMonsterData.putUtfString("box_requirements", boxReqsStatic.toJson());
      }

   }

   public void setEvolveDataStatic(ISFSArray possessedEggs) {
      this.evolveReqsMetStatic = possessedEggs;
   }

   public void setEvolveDataFlex(ISFSArray possessedEggs) {
      this.evolveReqsMetFlex = possessedEggs;
   }

   public boolean isPermaMega() {
      return this.megaData != null && this.megaData.permamega;
   }

   public boolean megaEnable(boolean e) {
      return this.megaData == null ? false : this.megaData.enableMega(e);
   }

   public boolean isMega(MSMExtension ext) {
      return this.isPermaMega() || this.isDailyMega(ext);
   }

   public boolean isDailyMega(MSMExtension ext) {
      if (this.megaData != null && !this.megaData.permamega) {
         return !this.dailyMegaExpired(ext);
      } else {
         return false;
      }
   }

   private boolean dailyMegaExpired(MSMExtension ext) {
      return this.megaData == null || this.megaData.megaExpired(MSMExtension.CurrentDBTime());
   }

   public void makeMega(ISFSObject s) {
      this.megaData = new MonsterMegaData(s);
   }

   public void makeMega(MSMExtension ext, boolean permanent) {
      if (this.megaData == null) {
         this.megaData = new MonsterMegaData(ext, permanent);
      } else {
         this.megaData.resetMega(ext, permanent);
      }

   }

   public int numReqsRemaining(boolean goldIsland) {
      Monster m = MonsterLookup.get(this.getType());
      int numEggsReqStatic = 0;
      int numEggsReqFlex = 0;
      int numBoxed = 0;
      if (this.isEvolvable()) {
         if (this.evolveReqsMetStatic() != null) {
            numBoxed += this.evolveReqsMetStatic().size();
         }

         if (this.evolveReqsMetFlex() != null) {
            numBoxed += this.evolveReqsMetFlex().size();
         }

         if (m.getEvolveReqsStatic() != null) {
            numEggsReqStatic = m.getEvolveReqsStatic().size();
         }

         if (m.getEvolveReqsFlex() != null) {
            numEggsReqFlex = m.getEvolveReqsFlex().size();
         }
      } else if (m.isBoxMonsterType()) {
         numBoxed = this.boxedEggs != null ? this.boxedEggs.size() : 0;
         numEggsReqStatic = this.getBoxRequirements(goldIsland).size();
      }

      return numEggsReqStatic + numEggsReqFlex - numBoxed;
   }

   public int fillRemainingEggsXp(PlayerIsland island) {
      ArrayList<Integer> checkedEggs = new ArrayList();
      Monster m = MonsterLookup.get(this.getType());
      ISFSArray requiredEggsStatic = null;
      ISFSArray requiredEggsFlex = null;
      if (this.isEvolvable()) {
         requiredEggsStatic = m.getEvolveReqsStatic();
         requiredEggsFlex = m.getEvolveReqsFlex();
      } else if (m.isBoxMonsterType()) {
         requiredEggsStatic = this.getBoxRequirements(island.isGoldIsland());
      }

      int xpReward = 0;
      ArrayList<Integer> remainingEggs = new ArrayList();

      int i;
      int defId;
      int numRequired;
      int i;
      for(i = 0; i < requiredEggsStatic.size(); ++i) {
         defId = requiredEggsStatic.getInt(i);
         if (!checkedEggs.contains(defId)) {
            numRequired = 0;

            for(i = 0; i < requiredEggsStatic.size(); ++i) {
               if (requiredEggsStatic.getInt(i) == defId) {
                  ++numRequired;
               }
            }

            for(i = this.hasNumOfEggStatic(defId); i < numRequired; ++i) {
               remainingEggs.add(defId);
            }

            checkedEggs.add(defId);
         }
      }

      for(i = 0; i < remainingEggs.size(); ++i) {
         xpReward += MonsterLookup.get((Integer)remainingEggs.get(i)).getXp();
      }

      if (requiredEggsFlex != null) {
         checkedEggs.clear();
         remainingEggs.clear();

         for(i = 0; i < requiredEggsFlex.size(); ++i) {
            defId = requiredEggsFlex.getInt(i);
            if (!checkedEggs.contains(defId)) {
               numRequired = 0;

               for(i = 0; i < requiredEggsFlex.size(); ++i) {
                  if (requiredEggsFlex.getInt(i) == defId) {
                     ++numRequired;
                  }
               }

               for(i = this.hasNumOfEggFlex(defId); i < numRequired; ++i) {
                  remainingEggs.add(defId);
               }
            }
         }

         for(i = 0; i < remainingEggs.size(); ++i) {
            MonsterFlexEggDef def = MonsterFlexEggDefLookup.getInstance().getEntry((Integer)remainingEggs.get(i));
            if (def != null) {
               xpReward += def.xp();
            }
         }
      }

      return xpReward;
   }

   public PlayerMonster.BoxFillCost fillRemainingEggsDiamondCost(PlayerIsland island, boolean prefWildcard, boolean restrictToDiamonds, long maxDiamonds, long maxWildcards) {
      return !restrictToDiamonds && prefWildcard ? this.getPreferWildcardsFillPrice(island, maxDiamonds, maxWildcards) : this.getPreferDiamondsFillPrice(island, true, maxDiamonds, maxWildcards);
   }

   ArrayList<FlexEgg> getEggsRemaining(PlayerIsland island) {
      ISFSArray requiredEggsStatic = null;
      ISFSArray requiredEggsFlex = null;
      Monster m = MonsterLookup.get(this.getType());
      if (this.isEvolvable()) {
         requiredEggsStatic = m.getEvolveReqsStatic();
         requiredEggsFlex = m.getEvolveReqsFlex();
      } else if (m.isBoxMonsterType()) {
         requiredEggsStatic = this.getBoxRequirements(island.isGoldIsland());
      }

      ArrayList<FlexEgg> eggsRemaining = new ArrayList();
      ArrayList<Integer> checkedEggs = new ArrayList();

      int index;
      int defId;
      int numRequired;
      int i;
      for(index = 0; index < requiredEggsStatic.size(); ++index) {
         defId = requiredEggsStatic.getInt(index);
         if (!checkedEggs.contains(defId)) {
            numRequired = 0;

            for(i = 0; i < requiredEggsStatic.size(); ++i) {
               if (requiredEggsStatic.getInt(i) == defId) {
                  ++numRequired;
               }
            }

            for(i = this.hasNumOfEggStatic(defId); i < numRequired; ++i) {
               eggsRemaining.add(new FlexEgg(defId, 0));
            }

            checkedEggs.add(defId);
         }
      }

      if (requiredEggsFlex != null) {
         checkedEggs.clear();

         for(index = 0; index < requiredEggsFlex.size(); ++index) {
            defId = requiredEggsFlex.getInt(index);
            if (!checkedEggs.contains(defId)) {
               numRequired = 0;

               for(i = 0; i < requiredEggsFlex.size(); ++i) {
                  if (requiredEggsFlex.getInt(i) == defId) {
                     ++numRequired;
                  }
               }

               for(i = this.hasNumOfEggFlex(defId); i < numRequired; ++i) {
                  eggsRemaining.add(new FlexEgg(0, defId));
               }

               checkedEggs.add(defId);
            }
         }
      }

      return eggsRemaining;
   }

   private PlayerMonster.BoxFillCost getPreferWildcardsFillPrice(final PlayerIsland island, long maxDiamonds, long maxWildcards) {
      ArrayList<FlexEgg> eggsRemaining = this.getEggsRemaining(island);
      eggsRemaining.sort(new Comparator<FlexEgg>() {
         public int compare(FlexEgg f1, FlexEgg f2) {
            int r1 = (int)((float)f1.diamondFillCost(PlayerMonster.this, island) / (float)f1.eggWildcardFillCost() * 100.0F);
            int r2 = (int)((float)f2.diamondFillCost(PlayerMonster.this, island) / (float)f2.eggWildcardFillCost() * 100.0F);
            return r2 - r1;
         }
      });
      ArrayList<Boolean> paidForWithWildcards = new ArrayList(eggsRemaining.size());

      int diamondFillPrice;
      for(diamondFillPrice = 0; diamondFillPrice < eggsRemaining.size(); ++diamondFillPrice) {
         paidForWithWildcards.add(false);
      }

      diamondFillPrice = 0;
      int wildcardFillPrice = 0;

      int i;
      for(i = 0; i < eggsRemaining.size(); ++i) {
         int nextPrice = ((FlexEgg)eggsRemaining.get(i)).eggWildcardFillCost();
         if (maxWildcards >= (long)(wildcardFillPrice + nextPrice)) {
            paidForWithWildcards.set(i, true);
            wildcardFillPrice += nextPrice;
         }
      }

      for(i = 0; i < paidForWithWildcards.size(); ++i) {
         if (!(Boolean)paidForWithWildcards.get(i)) {
            diamondFillPrice += ((FlexEgg)eggsRemaining.get(i)).diamondFillCost(this, island);
         }
      }

      PlayerMonster.BoxFillCost fillCost = new PlayerMonster.BoxFillCost(diamondFillPrice, wildcardFillPrice);
      return fillCost;
   }

   private PlayerMonster.BoxFillCost getPreferDiamondsFillPrice(final PlayerIsland island, boolean restrictToDiamonds, long maxDiamonds, long maxWildcards) {
      ArrayList<FlexEgg> eggsRemaining = this.getEggsRemaining(island);
      eggsRemaining.sort(new Comparator<FlexEgg>() {
         public int compare(FlexEgg f1, FlexEgg f2) {
            int r1 = (int)((float)f1.diamondFillCost(PlayerMonster.this, island) / (float)f1.eggWildcardFillCost() * 100.0F);
            int r2 = (int)((float)f2.diamondFillCost(PlayerMonster.this, island) / (float)f2.eggWildcardFillCost() * 100.0F);
            return r1 - r2;
         }
      });
      int diamondFillPrice = 0;
      int wildcardFillPrice = 0;
      ArrayList<Boolean> paidForWithDiamonds = new ArrayList(eggsRemaining.size());

      int i;
      for(i = 0; i < eggsRemaining.size(); ++i) {
         paidForWithDiamonds.add(false);
      }

      for(i = 0; i < eggsRemaining.size(); ++i) {
         int nextPrice = ((FlexEgg)eggsRemaining.get(i)).diamondFillCost(this, island);
         if (restrictToDiamonds || maxDiamonds >= (long)(diamondFillPrice + nextPrice)) {
            paidForWithDiamonds.set(i, true);
            diamondFillPrice += nextPrice;
         }
      }

      if (!restrictToDiamonds) {
         for(i = 0; i < paidForWithDiamonds.size(); ++i) {
            if (!(Boolean)paidForWithDiamonds.get(i)) {
               wildcardFillPrice += ((FlexEgg)eggsRemaining.get(i)).eggWildcardFillCost();
            }
         }
      }

      PlayerMonster.BoxFillCost fillCost = new PlayerMonster.BoxFillCost(diamondFillPrice, wildcardFillPrice);
      return fillCost;
   }

   /** @deprecated */
   @Deprecated
   public boolean adminRemoveEgg(User sender, GameStateHandler handler, int monsterId, PlayerIsland island) {
      ISFSArray possessedEggs = null;
      if (this.isInactiveBoxMonster()) {
         possessedEggs = this.boxedEggs;
      } else if (MonsterLookup.get(this.getType()).isEvolvable()) {
         possessedEggs = this.evolveReqsMetStatic;
      }

      if (possessedEggs == null) {
         return false;
      } else {
         boolean found = false;
         Iterator it = possessedEggs.iterator();

         while(it.hasNext()) {
            SFSDataWrapper curEggSFS = (SFSDataWrapper)it.next();
            Integer curEgg = (Integer)((Integer)curEggSFS.getObject());
            if (curEgg == monsterId) {
               it.remove();
               found = true;
               break;
            }
         }

         if (found) {
            if (this.isInactiveBoxMonster() && possessedEggs != null) {
               this.boxMonsterData.putUtfString("boxed_eggs", possessedEggs.toJson());
               if (possessedEggs.size() != this.boxedEggs.size()) {
                  Logger.trace("***********************************");
                  Logger.trace("error removing egg from box monster");
                  Logger.trace("***********************************");
               }
            } else if (MonsterLookup.get(this.getType()).isEvolvable() && this.evolveReqsMetStatic != null && possessedEggs.size() != this.evolveReqsMetStatic.size()) {
               Logger.trace("***********************************");
               Logger.trace("error removing egg from evolve monster");
               Logger.trace("***********************************");
            }
         }

         return found;
      }
   }

   public boolean adminRemoveEggNew(User sender, GameStateHandler handler, int reqInd, boolean isFlexEgg, PlayerIsland island) {
      ISFSArray requiredEggs = null;
      ISFSArray possessedEggs = null;
      Monster m = MonsterLookup.get(this.getType());
      if (this.isInactiveBoxMonster()) {
         requiredEggs = this.getBoxRequirements(island.isGoldIsland());
         possessedEggs = this.boxedEggs;
      } else if (m.isEvolvable()) {
         if (!isFlexEgg) {
            requiredEggs = m.getEvolveReqsStatic();
            possessedEggs = this.evolveReqsMetStatic;
         } else {
            requiredEggs = m.getEvolveReqsFlex();
            possessedEggs = this.evolveReqsMetFlex;
         }
      }

      if (possessedEggs == null) {
         return false;
      } else if (reqInd >= requiredEggs.size()) {
         return false;
      } else {
         int reqType = requiredEggs.getInt(reqInd);
         int numOfTypeReqd = 0;

         int foundAt;
         for(foundAt = 0; foundAt < requiredEggs.size(); ++foundAt) {
            if (requiredEggs.getInt(foundAt) == reqType) {
               ++numOfTypeReqd;
            }
         }

         foundAt = -1;
         if (numOfTypeReqd != 0) {
            for(int i = 0; i < possessedEggs.size(); ++i) {
               if (possessedEggs.getInt(i) == reqType) {
                  foundAt = i;
                  break;
               }
            }

            if (foundAt != -1) {
               possessedEggs.removeElementAt(foundAt);
            }
         }

         if (foundAt != -1 && this.isInactiveBoxMonster()) {
            this.boxMonsterData.putUtfString("boxed_eggs", possessedEggs.toJson());
         }

         return foundAt != -1;
      }
   }

   private boolean addEggStatic(User sender, GameStateHandler handler, int monsterId, PlayerIsland island) {
      ISFSArray possessedEggsStatic = null;
      ISFSArray requiredEggsStatic = null;
      Monster m = MonsterLookup.get(this.getType());
      if (this.isEvolvable()) {
         requiredEggsStatic = m.getEvolveReqsStatic();
         possessedEggsStatic = this.evolveReqsMetStatic;
      } else if (m.isBoxMonsterType()) {
         requiredEggsStatic = this.getBoxRequirements(island.isGoldIsland());
         possessedEggsStatic = this.boxedEggs;
      }

      if (possessedEggsStatic != null && requiredEggsStatic != null) {
         int numOfStaticTypeReqd = 0;

         int hasNumOfEgg;
         for(hasNumOfEgg = 0; hasNumOfEgg < requiredEggsStatic.size(); ++hasNumOfEgg) {
            if (requiredEggsStatic.getInt(hasNumOfEgg) == monsterId) {
               ++numOfStaticTypeReqd;
            }
         }

         if (numOfStaticTypeReqd != 0) {
            hasNumOfEgg = 0;

            for(int i = 0; i < possessedEggsStatic.size(); ++i) {
               if (possessedEggsStatic.getInt(i) == monsterId) {
                  ++hasNumOfEgg;
               }
            }

            if (hasNumOfEgg < numOfStaticTypeReqd) {
               possessedEggsStatic.addInt(monsterId);
               if (this.isInactiveBoxMonster()) {
                  this.boxedEggs = possessedEggsStatic;
                  this.boxMonsterData.putUtfString("boxed_eggs", this.boxedEggs.toJson());
                  ISFSObject qe = new SFSObject();
                  qe.putInt("box_egg", monsterId);
                  qe.putInt("on_island", island.getType());
                  handler.serverQuestEvent(sender, qe);
               } else if (MonsterLookup.get(this.getType()).isEvolvable()) {
                  this.evolveReqsMetStatic = possessedEggsStatic;
               }

               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private boolean addEggFlex(User sender, GameStateHandler handler, MonsterFlexEggDef addDef, PlayerIsland island) {
      ISFSArray possessedEggsFlex = null;
      ISFSArray requiredEggsFlex = null;
      Monster m = MonsterLookup.get(this.getType());
      if (this.isEvolvable()) {
         requiredEggsFlex = m.getEvolveReqsFlex();
         possessedEggsFlex = this.evolveReqsMetFlex;
      }

      if (possessedEggsFlex != null && requiredEggsFlex != null) {
         ArrayList<Integer> distinctDefIds = new ArrayList();

         int i;
         for(i = 0; i < requiredEggsFlex.size(); ++i) {
            if (!distinctDefIds.contains(requiredEggsFlex.getInt(i))) {
               distinctDefIds.add(requiredEggsFlex.getInt(i));
            }
         }

         for(i = 0; i < distinctDefIds.size(); ++i) {
            int defId = (Integer)distinctDefIds.get(i);
            MonsterFlexEggDef def = MonsterFlexEggDefLookup.getInstance().getEntry(defId);
            if (def.id() == addDef.id()) {
               int numReqd = 0;

               int numPossessed;
               for(numPossessed = 0; numPossessed < requiredEggsFlex.size(); ++numPossessed) {
                  if (requiredEggsFlex.getInt(numPossessed) == defId) {
                     ++numReqd;
                  }
               }

               numPossessed = 0;

               for(int j = 0; j < possessedEggsFlex.size(); ++j) {
                  if (possessedEggsFlex.getInt(j) == defId) {
                     ++numPossessed;
                  }
               }

               if (numPossessed < numReqd) {
                  this.evolveReqsMetFlex.addInt(addDef.id());
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private boolean addEggFlex(User sender, GameStateHandler handler, int monsterId, PlayerIsland island) {
      ISFSArray possessedEggsFlex = null;
      ISFSArray requiredEggsFlex = null;
      Monster m = MonsterLookup.get(this.getType());
      if (this.isEvolvable()) {
         requiredEggsFlex = m.getEvolveReqsFlex();
         possessedEggsFlex = this.evolveReqsMetFlex;
      }

      if (possessedEggsFlex != null && requiredEggsFlex != null) {
         ArrayList<Integer> distinctDefIds = new ArrayList();

         for(int i = 0; i < requiredEggsFlex.size(); ++i) {
            if (!distinctDefIds.contains(requiredEggsFlex.getInt(i))) {
               distinctDefIds.add(requiredEggsFlex.getInt(i));
            }
         }

         Monster staticEgg = MonsterLookup.get(monsterId);

         for(int i = 0; i < distinctDefIds.size(); ++i) {
            int defId = (Integer)distinctDefIds.get(i);
            MonsterFlexEggDef def = MonsterFlexEggDefLookup.getInstance().getEntry(defId);
            if (def != null && def.evaluate(staticEgg)) {
               int numReqd = 0;

               int numPossessed;
               for(numPossessed = 0; numPossessed < requiredEggsFlex.size(); ++numPossessed) {
                  if (requiredEggsFlex.getInt(numPossessed) == defId) {
                     ++numReqd;
                  }
               }

               numPossessed = 0;

               for(int j = 0; j < possessedEggsFlex.size(); ++j) {
                  if (possessedEggsFlex.getInt(j) == defId) {
                     ++numPossessed;
                  }
               }

               if (numPossessed < numReqd) {
                  this.evolveReqsMetFlex.addInt(defId);
                  return true;
               }
            }
         }
      }

      return false;
   }

   public boolean addEggAdmin(User sender, GameStateHandler handler, int reqInd, boolean isFlexEgg, PlayerIsland island) {
      ISFSArray requiredEggs = null;
      ISFSArray possessedEggs = null;
      Monster m = MonsterLookup.get(this.getType());
      if (this.isEvolvable()) {
         if (!isFlexEgg) {
            requiredEggs = m.getEvolveReqsStatic();
            possessedEggs = this.evolveReqsMetStatic;
         } else {
            requiredEggs = m.getEvolveReqsFlex();
            possessedEggs = this.evolveReqsMetFlex;
         }
      } else if (m.isBoxMonsterType()) {
         requiredEggs = this.getBoxRequirements(island.isGoldIsland());
         possessedEggs = this.boxedEggs;
      }

      if (requiredEggs != null && possessedEggs != null) {
         if (reqInd >= requiredEggs.size()) {
            return false;
         } else {
            int reqType = requiredEggs.getInt(reqInd);
            int numOfTypeReqd = 0;

            int numFound;
            for(numFound = 0; numFound < requiredEggs.size(); ++numFound) {
               if (requiredEggs.getInt(numFound) == reqType) {
                  ++numOfTypeReqd;
               }
            }

            numFound = 0;
            if (numOfTypeReqd != 0) {
               for(int i = 0; i < possessedEggs.size(); ++i) {
                  if (possessedEggs.getInt(i) == reqType) {
                     ++numFound;
                  }
               }

               if (numFound < numOfTypeReqd) {
                  possessedEggs.addInt(reqType);
                  if (this.isInactiveBoxMonster()) {
                     this.boxMonsterData.putUtfString("boxed_eggs", possessedEggs.toJson());
                  }

                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public boolean addEgg(User sender, GameStateHandler handler, int monsterId, PlayerIsland island) {
      ISFSArray possessedEggsStatic = null;
      ISFSArray possessedEggsFlex = null;
      ISFSArray requiredEggsStatic = null;
      ISFSArray requiredEggsFlex = null;
      Monster m = MonsterLookup.get(this.getType());
      if (this.isEvolvable()) {
         requiredEggsStatic = m.getEvolveReqsStatic();
         requiredEggsFlex = m.getEvolveReqsFlex();
         possessedEggsStatic = this.evolveReqsMetStatic;
         possessedEggsFlex = this.evolveReqsMetFlex;
      } else if (m.isBoxMonsterType()) {
         requiredEggsStatic = this.getBoxRequirements(island.isGoldIsland());
         possessedEggsStatic = this.boxedEggs;
      }

      if (possessedEggsStatic == null && possessedEggsFlex == null || requiredEggsStatic == null && requiredEggsFlex == null) {
         return false;
      } else {
         return this.addEggStatic(sender, handler, monsterId, island) ? true : this.addEggFlex(sender, handler, monsterId, island);
      }
   }

   public int hasNumOfEggStatic(int monsterId) {
      ISFSArray possessedReqs = null;
      if (this.isInactiveBoxMonster()) {
         possessedReqs = this.boxedEggs;
      } else if (MonsterLookup.get(this.getType()).isEvolvable()) {
         possessedReqs = this.evolveReqsMetStatic;
      }

      if (possessedReqs == null) {
         return 0;
      } else {
         int num = 0;

         for(int i = 0; i < possessedReqs.size(); ++i) {
            if (possessedReqs.getInt(i) == monsterId) {
               ++num;
            }
         }

         return num;
      }
   }

   public int hasNumOfEggFlex(int defId) {
      if (this.isInactiveBoxMonster()) {
         return 0;
      } else if (!MonsterLookup.get(this.getType()).isEvolvable()) {
         return 0;
      } else if (this.evolveReqsMetFlex == null) {
         return 0;
      } else {
         int num = 0;

         for(int i = 0; i < this.evolveReqsMetFlex.size(); ++i) {
            if (this.evolveReqsMetFlex.getInt(i) == defId) {
               ++num;
            }
         }

         return num;
      }
   }

   public ISFSArray getBoxRequirements(boolean goldIsland) {
      ISFSArray requiredEggs = null;
      Monster m = MonsterLookup.get(this.getType());
      if (m.isBoxMonsterType()) {
         if (goldIsland) {
            if (this.isRare()) {
               requiredEggs = Monster.getGoldRareBoxMonsterRequirements();
            } else if (this.isEpic()) {
               requiredEggs = MonsterLookup.get(this.getType()).getBoxRequirements();
            } else {
               requiredEggs = Monster.getGoldBoxMonsterRequirements();
            }
         } else {
            requiredEggs = MonsterLookup.get(this.getType()).getBoxRequirements();
         }
      }

      return requiredEggs;
   }

   public void fillEggs(PlayerIsland pi, GameStateHandler gsh, User sender) {
      ISFSArray eggReqsStatic = null;
      ISFSArray eggReqsFlex = null;
      Monster m = MonsterLookup.get(this.getType());
      if (this.isEvolvable()) {
         eggReqsStatic = m.getEvolveReqsStatic();
         eggReqsFlex = m.getEvolveReqsFlex();
      } else if (m.isBoxMonsterType()) {
         eggReqsStatic = this.getBoxRequirements(pi.isGoldIsland());
      }

      int i;
      for(i = 0; i < eggReqsStatic.size(); ++i) {
         this.addEggStatic(sender, gsh, eggReqsStatic.getInt(i), pi);
      }

      if (eggReqsFlex != null) {
         for(i = 0; i < eggReqsFlex.size(); ++i) {
            int defId = eggReqsFlex.getInt(i);
            MonsterFlexEggDef def = MonsterFlexEggDefLookup.getInstance().getEntry(defId);
            this.addEggFlex(sender, gsh, def, pi);
         }
      }

   }

   private int getEggBuyingPriceOfStaticEggIds(ISFSArray eggs, PlayerIsland island) {
      int eggsBuyingPrice = 0;
      if (eggs != null) {
         for(int i = 0; i < eggs.size(); ++i) {
            Monster staticMonsterEgg = MonsterLookup.get(eggs.getInt(i));
            if (island.isEtherealIsland()) {
               eggsBuyingPrice += staticMonsterEgg.getCostEth(island.getType());
            } else {
               eggsBuyingPrice += staticMonsterEgg.getCostCoins(island.getType());
            }
         }
      }

      return eggsBuyingPrice;
   }

   private int getEggBuyingPriceOfFlexEggDefIds(ISFSArray defIds, PlayerIsland island) {
      int eggsBuyingPrice = 0;
      if (defIds != null) {
         for(int i = 0; i < defIds.size(); ++i) {
            MonsterFlexEggDef def = MonsterFlexEggDefLookup.getInstance().getEntry(defIds.getInt(i));
            if (def != null) {
               eggsBuyingPrice += def.costCoins();
            }
         }
      }

      return eggsBuyingPrice;
   }

   public int boxedEggsBuyPrice(PlayerIsland island) {
      boolean isGoldIsland = island.isGoldIsland();
      if (isGoldIsland) {
         return 0;
      } else {
         int eggsBuyingPrice = 0;
         ISFSArray boxReqs = this.getBoxRequirements(isGoldIsland);
         if (boxReqs != null) {
            if (!this.isInactiveBoxMonster()) {
               eggsBuyingPrice += this.getEggBuyingPriceOfStaticEggIds(boxReqs, island);
            } else {
               eggsBuyingPrice += this.getEggBuyingPriceOfStaticEggIds(this.boxedEggs, island);
            }
         }

         return eggsBuyingPrice;
      }
   }

   public int evolveEggsBuyPrice(PlayerIsland island) {
      boolean isGoldIsland = island.isGoldIsland();
      if (isGoldIsland) {
         return 0;
      } else if (!MonsterLookup.get(this.getType()).isEvolvable()) {
         return 0;
      } else if (this.isInactiveBoxMonster()) {
         return 0;
      } else {
         int eggsBuyingPrice = 0;
         if (this.evolveReqsMetStatic != null) {
            eggsBuyingPrice += this.getEggBuyingPriceOfStaticEggIds(this.evolveReqsMetStatic, island);
         }

         if (this.evolveReqsMetFlex != null) {
            eggsBuyingPrice += this.getEggBuyingPriceOfFlexEggDefIds(this.evolveReqsMetFlex, island);
         }

         return eggsBuyingPrice;
      }
   }

   public boolean hasAllRequiredEggs(boolean goldIsland) {
      ISFSArray possessedEggsStatic = null;
      ISFSArray possessedEggsFlex = null;
      if (this.isInactiveBoxMonster()) {
         possessedEggsStatic = this.boxedEggs;
      } else if (MonsterLookup.get(this.getType()).isEvolvable()) {
         possessedEggsStatic = this.evolveReqsMetStatic;
         possessedEggsFlex = this.evolveReqsMetFlex;
      }

      if (possessedEggsStatic == null && possessedEggsFlex == null) {
         return false;
      } else {
         Monster m = MonsterLookup.get(this.getType());
         ISFSArray requiredEggsStatic = null;
         ISFSArray requiredEggsFlex = null;
         if (this.isEvolvable()) {
            requiredEggsStatic = m.getEvolveReqsStatic();
            requiredEggsFlex = m.getEvolveReqsFlex();
         } else if (m.isBoxMonsterType()) {
            requiredEggsStatic = this.getBoxRequirements(goldIsland);
         }

         ArrayList hasReqInInd;
         int reqInd;
         boolean reqFound;
         int i;
         if (requiredEggsStatic != null) {
            if (possessedEggsStatic.size() < requiredEggsStatic.size()) {
               return false;
            }

            hasReqInInd = new ArrayList(requiredEggsStatic.size());

            for(reqInd = 0; reqInd < requiredEggsStatic.size(); ++reqInd) {
               hasReqInInd.add(0);
            }

            for(reqInd = 0; reqInd < requiredEggsStatic.size(); ++reqInd) {
               reqFound = false;

               for(i = 0; i < possessedEggsStatic.size(); ++i) {
                  if (requiredEggsStatic.getInt(reqInd).equals(possessedEggsStatic.getInt(i)) && (Integer)hasReqInInd.get(reqInd) != 1) {
                     hasReqInInd.set(reqInd, 1);
                     reqFound = true;
                     break;
                  }
               }

               if (!reqFound) {
                  return false;
               }
            }
         }

         if (requiredEggsFlex != null) {
            if (possessedEggsFlex.size() < requiredEggsFlex.size()) {
               return false;
            }

            hasReqInInd = new ArrayList(requiredEggsFlex.size());

            for(reqInd = 0; reqInd < requiredEggsFlex.size(); ++reqInd) {
               hasReqInInd.add(0);
            }

            for(reqInd = 0; reqInd < requiredEggsFlex.size(); ++reqInd) {
               reqFound = false;

               for(i = 0; i < possessedEggsFlex.size(); ++i) {
                  if (requiredEggsFlex.getInt(reqInd).equals(possessedEggsFlex.getInt(i)) && (Integer)hasReqInInd.get(reqInd) != 1) {
                     hasReqInInd.set(reqInd, 1);
                     reqFound = true;
                     break;
                  }
               }

               if (!reqFound) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public boolean attemptEarlyEvolve(Player p, PlayerIsland island) {
      if (this.isInactiveBoxMonster()) {
         return false;
      } else if (this.evolveReqsMetStatic == null && this.evolveReqsMetFlex == null) {
         return false;
      } else if (this.hasAllRequiredEggs(island.isGoldIsland())) {
         return true;
      } else if (GameSettings.get("DEBUG_EARLY_ASCEND_TOGGLE", 0) == 0) {
         double fillValue = this.evolvingCelestialFillValue(p, island.getType());
         double successChance = 0.0D;
         if (this.meetsEarlyFillRequirements(fillValue)) {
            fillValue *= 100.0D;
            successChance = (22.0D - 1.65D * fillValue + 0.025D * fillValue * fillValue) / 100.0D;
            if (successChance < 0.0D) {
               successChance = 0.0D;
            } else if (successChance > 1.0D) {
               successChance = 1.0D;
            }
         }

         return this.rng.nextDouble() <= successChance;
      } else {
         return this.rng.nextDouble() <= GameSettings.get("DEBUG_EARLY_ASCEND_SUCCESS_OVERWRITE", 0.0D);
      }
   }

   public void runEarlyEvolveFailureEggCanning(int curIslandType, Player player) {
      Monster staticMonster = MonsterLookup.get(this.getType());
      if (this.isEvolvable() && !staticMonster.evolveEnabled() && !EvolveAvailabilityEvent.hasTimedEventNow(staticMonster, player, curIslandType)) {
         this.evolveReqsMetStatic = new SFSArray();
         this.evolveReqsMetFlex = new SFSArray();
      } else {
         Monster m = MonsterLookup.get(this.getType());
         ISFSArray reqEggsStatic = null;
         ISFSArray reqEggsFlex = null;
         if (this.isEvolvable()) {
            reqEggsStatic = m.getEvolveReqsStatic();
            reqEggsFlex = m.getEvolveReqsFlex();
         } else if (m.isBoxMonsterType()) {
            reqEggsStatic = this.getBoxRequirements(curIslandType == 6);
         }

         double totalEggValuation = (double)(this.totalEggValuationStatic(reqEggsStatic, curIslandType) + this.totalEggValuationFlex(reqEggsFlex));
         double valueOfEggsToDelete = totalEggValuation * GameSettings.getDouble("USER_CELESTIAL_EARLY_ASCEND_FAIL_VAL_DELETE");
         List<Integer> sortedEggList = new ArrayList();

         int removeTo;
         for(removeTo = 0; removeTo < this.evolveReqsMetStatic.size(); ++removeTo) {
            sortedEggList.add(this.evolveReqsMetStatic.getInt(removeTo));
         }

         sortedEggList.sort(new SortFilledEggsByVal(curIslandType));
         removeTo = -1;

         int i;
         for(i = 0; i < sortedEggList.size() && valueOfEggsToDelete >= 0.0D; ++i) {
            valueOfEggsToDelete -= (double)MonsterLookup.get((Integer)sortedEggList.get(i)).getCostCoins(curIslandType);
            ++removeTo;
            if (valueOfEggsToDelete <= 0.0D) {
               break;
            }
         }

         List sortedEggList;
         if (valueOfEggsToDelete <= 0.0D) {
            sortedEggList = sortedEggList.subList(removeTo, sortedEggList.size());
            this.evolveReqsMetStatic = new SFSArray();

            for(i = 0; i < sortedEggList.size(); ++i) {
               this.evolveReqsMetStatic.addInt((Integer)sortedEggList.get(i));
            }
         } else {
            this.evolveReqsMetStatic = new SFSArray();
            sortedEggList.clear();

            for(i = 0; i < this.evolveReqsMetFlex.size(); ++i) {
               sortedEggList.add(this.evolveReqsMetFlex.getInt(i));
            }

            sortedEggList.sort(new SortFilledEggsByVal(curIslandType));
            removeTo = -1;

            for(i = 0; i < sortedEggList.size() && valueOfEggsToDelete >= 0.0D; ++i) {
               valueOfEggsToDelete -= (double)MonsterFlexEggDefLookup.getInstance().getEntry((Integer)sortedEggList.get(i)).costCoins();
               ++removeTo;
               if (valueOfEggsToDelete <= 0.0D) {
                  break;
               }
            }

            sortedEggList = sortedEggList.subList(removeTo, sortedEggList.size());
            this.evolveReqsMetFlex = new SFSArray();

            for(i = 0; i < sortedEggList.size(); ++i) {
               this.evolveReqsMetFlex.addInt((Integer)sortedEggList.get(i));
            }
         }
      }

   }

   private boolean meetsEarlyFillRequirements(double fillValue) {
      return fillValue > GameSettings.getDouble("USER_CELESTIAL_EARLY_ASCEND_FILL_REQ");
   }

   public double evolvingCelestialFillValue(Player p, int curIslandType) {
      Monster m = MonsterLookup.get(this.getType());
      ISFSArray reqEggsStatic = null;
      ISFSArray reqEggsFlex = null;
      if (this.isEvolvable()) {
         reqEggsStatic = m.getEvolveReqsStatic();
         reqEggsFlex = m.getEvolveReqsFlex();
      } else if (m.isBoxMonsterType()) {
         reqEggsStatic = this.getBoxRequirements(curIslandType == 6);
      }

      double haveEggsValuation = (double)(this.totalEggValuationStatic(this.evolveReqsMetStatic, curIslandType) + this.totalEggValuationFlex(this.evolveReqsMetFlex));
      double totalEggValuation = (double)(this.totalEggValuationStatic(reqEggsStatic, curIslandType) + this.totalEggValuationFlex(reqEggsFlex));
      return haveEggsValuation / totalEggValuation;
   }

   private int totalEggValuationStatic(ISFSArray monsterSet, int curIslandType) {
      int totalValue = 0;

      for(int i = 0; i < monsterSet.size(); ++i) {
         totalValue += MonsterLookup.get(monsterSet.getInt(i)).getCostCoins(curIslandType);
      }

      return totalValue;
   }

   private int totalEggValuationFlex(ISFSArray flexDefSet) {
      int totalValue = 0;

      for(int i = 0; i < flexDefSet.size(); ++i) {
         totalValue += MonsterFlexEggDefLookup.getInstance().getEntry(flexDefSet.getInt(i)).costCoins();
      }

      return totalValue;
   }

   public boolean activateBoxMonster(User sender, GameStateHandler handler, Player player, PlayerIsland island) {
      if (!this.hasAllRequiredEggs(island.isGoldIsland())) {
         Logger.trace(LogLevel.WARN, "box monster doesn't have all the eggs, cannot activate!!");
         return false;
      } else {
         if (this.boxedEggs != null) {
            if (!island.isAmberIsland()) {
               ISFSObject qe = new SFSObject();
               qe.putInt("activate_box", 1);
               qe.putInt("on_island", island.getType());
               handler.serverQuestEvent(sender, qe);
               this.boxedEggs = null;
               this.boxMonsterData = null;
               Monster staticMonster = MonsterLookup.get(this.monster);
               if (this.evolveReqsMetStatic == null && (this.evolveReqsMetFlex == null || staticMonster.evolveEnabled())) {
                  this.eggTimerStart = -1L;
               } else {
                  this.eggTimerStart = getEvolveAvailabilityEggTimer(staticMonster, island.getIndex());
               }
            }
         } else if (this.evolveReqsMetStatic != null || this.evolveReqsMetFlex != null) {
            Monster staticMonster = MonsterLookup.get(this.monster);
            if (staticMonster.isCelestial() && !this.isCelestialPowerupUnlocked()) {
               Logger.trace(LogLevel.WARN, "celestial powerup not unlocked, cannot activate!!");
               return false;
            }

            this.evolveReqsMetStatic = null;
            this.evolveReqsMetFlex = null;
            this.evolve(staticMonster.evolveInto(), island);
            this.clearRottenEggs(island, player);
            staticMonster = MonsterLookup.get(this.monster);
            if (this.evolveReqsMetStatic == null && (this.evolveReqsMetFlex == null || staticMonster.evolveEnabled())) {
               this.eggTimerStart = -1L;
            } else {
               this.eggTimerStart = getEvolveAvailabilityEggTimer(staticMonster, island.getIndex());
            }
         }

         this.last_collection = MSMExtension.CurrentDBTime();
         return true;
      }
   }

   private boolean isHibernating() {
      return (this.evolveReqsMetStatic == null || this.evolveReqsMetStatic.size() <= 0) && (this.evolveReqsMetFlex == null || this.evolveReqsMetFlex.size() <= 0) ? false : MonsterLookup.get(this.getType()).isUnderling();
   }

   public void evolve(int evolveIntoEntity, PlayerIsland island) {
      this.boxMonsterData = null;
      this.boxedEggs = null;
      this.evolveReqsMetStatic = null;
      this.evolveReqsMetFlex = null;
      this.evolutionUnlocked = false;
      this.powerupUnlocked = false;
      this.muted = false;
      this.costumeState_ = new MonsterCostumeState();
      this.bookValue = -1;
      if (evolveIntoEntity != 0) {
         Monster newMonster = MonsterLookup.getFromEntityId(evolveIntoEntity);
         this.monster = (short)newMonster.getMonsterID();
         if (newMonster.isEvolvable()) {
            this.setEvolveDataStatic(new SFSArray());
            this.setEvolveDataFlex(new SFSArray());
         }
      }

      this.last_collection = MSMExtension.CurrentDBTime();
      this.resetUnderlingCurrencyType(island);
   }

   public PlayerMonster.UnderlingCollectionType CurrentCollectionType() {
      return this.currentCollectionType;
   }

   public int debugUnderlingCollectionTimeModifier() {
      return underlingTimeToCollectH2MModifier;
   }

   public int getRandomUnderlingCollectionMin() {
      return this.randomUnderlingTimeToCollect;
   }

   public void resetUnderlingCurrencyType(PlayerIsland island) {
      if (island.isRandomCollectionIsland() && !this.isHibernating()) {
         Monster staticMonster = MonsterLookup.get(this.getType());
         ISFSObject levelData = staticMonster.getLevel(this.getLevel());
         this.currentCollectionType = GetRandomCollectionType(levelData);
         this.randomUnderlingTimeToCollect = getRandomUnderlingCollectTime(this.happiness);
         this.underlingCollectionHappiness = this.happiness;
      } else {
         this.currentCollectionType = PlayerMonster.UnderlingCollectionType.MaxCollectionTypes;
         this.randomUnderlingTimeToCollect = 0;
      }

   }

   private static int getRandomUnderlingCollectTime(int happiness) {
      int min = (int)((float)GameSettings.getInt("MIN_RANDOM_MINUTES_FOR_UNDERLING_COLLECTION") / (60.0F / (float)underlingTimeToCollectH2MModifier));
      int max = (int)((float)GameSettings.getInt("MAX_RANDOM_MINUTES_FOR_UNDERLING_COLLECTION") / (60.0F / (float)underlingTimeToCollectH2MModifier));
      int diff = max - min;
      int randMinOffset = diff > 0 ? GetRandom().nextInt(diff) : 0;
      float happinessModifier = 1.0F + GameSettings.getFloat("WUBLIN_HAPPINESS_COLLECTION_TIME_MODIFIER") * (float)happiness / 100.0F * -1.0F;
      return Math.round((float)(randMinOffset + min) * happinessModifier);
   }

   private boolean validCollectionType(PlayerMonster.UnderlingCollectionType collectionType, ISFSObject monsterLevelData) {
      String amountKey = amountKeys[collectionType.ordinal()];
      String chanceKey = chanceKeys[collectionType.ordinal()];
      double chance = monsterLevelData.getDouble(chanceKey);
      if (chance <= 0.0D) {
         return false;
      } else {
         return monsterLevelData.getInt(amountKey) > 0;
      }
   }

   private boolean validRandomTime(int timeToCollect) {
      float happinessMod = GameSettings.getFloat("WUBLIN_HAPPINESS_COLLECTION_TIME_MODIFIER");
      float min = (float)GameSettings.getInt("MIN_RANDOM_MINUTES_FOR_UNDERLING_COLLECTION") / (60.0F / (float)underlingTimeToCollectH2MModifier);
      float max = (float)GameSettings.getInt("MAX_RANDOM_MINUTES_FOR_UNDERLING_COLLECTION") / (60.0F / (float)underlingTimeToCollectH2MModifier);
      Monster staticMonster = MonsterLookup.get(this.getType());
      if (staticMonster.isUnderling()) {
         min *= 1.0F - happinessMod;
         max *= 1.0F + happinessMod;
      }

      return min <= (float)timeToCollect && (float)timeToCollect <= max || this.isHibernating() && timeToCollect == 0;
   }

   public void startUnderlingHibernation(ISFSObject monsterUpdateResponse, Player p, User sender, ISFSObject params, GameStateHandler handler) {
      if (p.getActiveIsland().isUnderlingIsland()) {
         handler.collectFromMonster(sender, params);
         this.currentCollectionType = PlayerMonster.UnderlingCollectionType.MaxCollectionTypes;
         this.randomUnderlingTimeToCollect = 0;
      } else {
         long timeThenSec = this.last_collection / 1000L;
         long timeNowSec = MSMExtension.CurrentDBTime() / 1000L;
         long numSec = timeNowSec - timeThenSec;
         if (numSec < (long)(this.randomUnderlingTimeToCollect * underlingTimeToCollectH2MModifier)) {
            this.currentCollectionType = PlayerMonster.UnderlingCollectionType.MaxCollectionTypes;
            this.randomUnderlingTimeToCollect = 0;
            this.last_collection = MSMExtension.CurrentDBTime();
         } else {
            this.randomUnderlingTimeToCollect = (int)Math.round((double)numSec / (double)underlingTimeToCollectH2MModifier);
         }
      }

      monsterUpdateResponse.putLong("user_monster_id", this.getID());
      monsterUpdateResponse.putInt("random_underling_collection_min", this.randomUnderlingTimeToCollect);
      monsterUpdateResponse.putUtfString("collection_type", clientCurrencyKeys[this.currentCollectionType.ordinal()]);
   }

   public float stashCurrency(Player p, PlayerIsland island) {
      float total = 0.0F;
      Monster staticMonster = MonsterLookup.get(this.getType());
      ISFSObject levelData = staticMonster.getLevel(this.getLevel());
      long timeThenSec = this.last_collection / 1000L;
      long timeNowSec = MSMExtension.CurrentDBTime() / 1000L;
      if (island.isRandomCollectionIsland()) {
         if (this.currentCollectionType != PlayerMonster.UnderlingCollectionType.MaxCollectionTypes) {
            long numSec = timeNowSec - timeThenSec;
            int waitTimeSec = this.randomUnderlingTimeToCollect * 60;
            if (numSec >= (long)waitTimeSec) {
               String amountKey = amountKeys[this.currentCollectionType.ordinal()];
               if (this.currentCollectionType != PlayerMonster.UnderlingCollectionType.Diamonds && this.currentCollectionType != PlayerMonster.UnderlingCollectionType.Keys) {
                  int hourlyRate = levelData.getInt(amountKey);
                  float happinessModifier = 1.0F + GameSettings.getFloat("WUBLIN_HAPPINESS_COLLECTION_TIME_MODIFIER") * ((float)this.underlingCollectionHappiness / 100.0F);
                  float hoursCollecting = (float)this.randomUnderlingTimeToCollect / (float)underlingTimeToCollectH2MModifier;
                  total = (float)((int)(hoursCollecting * (float)hourlyRate * happinessModifier));
                  if (total == 0.0F && GameSettings.getInt("MIN_RANDOM_MINUTES_FOR_UNDERLING_COLLECTION") < 60) {
                     total = 1.0F;
                  }
               } else {
                  total = (float)levelData.getInt(amountKey);
               }
            }

            if (total > 0.0F) {
               this.last_collection = MSMExtension.CurrentDBTime();
            }
         }
      } else {
         if (island.isComposerIsland() || island.isBattleIsland()) {
            return 0.0F;
         }

         boolean ethereal = island.isEtherealIsland();
         boolean amber = island.isAmberIsland();
         float maxCurObj;
         if (!amber) {
            Integer iMco = levelData.getInt(ethereal ? "max_ethereal" : "max_coins");
            if (iMco != null) {
               maxCurObj = (float)iMco;
            } else {
               maxCurObj = 0.0F;
            }
         } else {
            Double d = levelData.getDouble("max_relics");
            if (d != null) {
               maxCurObj = (float)d;
            } else {
               maxCurObj = 0.0F;
            }
         }

         float islandThemeCoinProductionModifier = island.getIslandThemeModifier("coin_production_mod", p);
         float timedEventMod = 1.0F;
         PlayerTimedEvents pte = p.getTimedEvents();
         List<TimedEvent> playerEvents = null;
         if (pte != null) {
            playerEvents = pte.currentActiveOnKey(TimedEventType.ReturningUserBonus, 0, 0);
         }

         if (playerEvents != null && playerEvents.size() > 0) {
            ReturningUserBonusEvent bonusEvent = (ReturningUserBonusEvent)((ReturningUserBonusEvent)playerEvents.get(0));
            timedEventMod = bonusEvent.coinProductionMod();
         } else {
            List<TimedEvent> events = TimedEventManager.instance().currentActiveOnKey(TimedEventType.ReturningUserBonus, 0, 0);
            if (events != null && events.size() > 0) {
               ReturningUserBonusEvent bonusEvent = (ReturningUserBonusEvent)((ReturningUserBonusEvent)events.get(0));
               timedEventMod = bonusEvent.coinProductionMod();
            }
         }

         float buffMod = p.getBuffs().getMultiplier(PlayerBuffs.Buffs.CurrencyCollectionBonus, island.getType());
         double rate = (ethereal ? staticMonster.etherealRate(this) : (amber ? staticMonster.relicRate(this) : staticMonster.coinRate(this))) * (1.0D + (double)this.happiness / 100.0D) * (double)islandThemeCoinProductionModifier * (double)timedEventMod * (double)buffMod;
         double reward = rate * (double)(timeNowSec - timeThenSec);
         reward += (double)(ethereal ? (float)this.collected_eth : (amber ? this.collected_relics : (float)this.collected_coins));
         total = Math.min(maxCurObj, (float)reward);
         if (total > 0.0F) {
            if (ethereal) {
               this.collected_eth = (int)total;
            } else if (amber) {
               this.collected_relics = total;
            } else {
               this.collected_coins = (int)total;
            }

            this.last_collection = MSMExtension.CurrentDBTime();
         }
      }

      return total;
   }

   private static PlayerMonster.UnderlingCollectionType GetRandomCollectionType(ISFSObject levelData) {
      double diamondsCutoff = levelData.getDouble("chance_diamonds") / 100.0D;
      double shardsCutoff = levelData.getDouble("chance_shards") / 100.0D + diamondsCutoff;
      double starpowerCutoff = levelData.getDouble("chance_starpower") / 100.0D + shardsCutoff;
      double foodCutoff = levelData.getDouble("chance_food") / 100.0D + starpowerCutoff;
      double coinCutoff = levelData.getDouble("chance_coins") / 100.0D + foodCutoff;
      double keysCutoff = levelData.getDouble("chance_keys") / 100.0D + coinCutoff;
      double[] chances = new double[]{diamondsCutoff, shardsCutoff, starpowerCutoff, foodCutoff, coinCutoff, keysCutoff};
      float rand = GetRandom().nextFloat();
      int foundCurrency = -1;

      for(int i = 0; i < chances.length; ++i) {
         if ((double)rand < chances[i]) {
            foundCurrency = i;
            break;
         }
      }

      return foundCurrency != -1 ? CachedCollectionTypes()[foundCurrency] : PlayerMonster.UnderlingCollectionType.Coins;
   }

   public void TESTMaxCollectTime() {
      this.last_collection = MSMExtension.CurrentDBTime() - (long)(this.randomUnderlingTimeToCollect * 60 * 1000);
   }

   public int collectCurrency(Player p, PlayerIsland island) {
      if (this.boxedEggs == null) {
         if (island.isRandomCollectionIsland() && this.randomUnderlingTimeToCollect != 0) {
            long timeThenSec = this.last_collection / 1000L;
            long timeNowSec = MSMExtension.CurrentDBTime() / 1000L;
            long numSec = timeNowSec - timeThenSec;
            int waitTimeSec = this.randomUnderlingTimeToCollect * 60;
            if (numSec < (long)waitTimeSec) {
               return 0;
            }
         }

         float total = this.stashCurrency(p, island);
         this.collected_coins = 0;
         this.collected_eth = 0;
         this.collected_diamonds = 0;
         this.collected_food = 0;
         this.collected_starpower = 0;
         this.collected_keys = 0;
         if (island.isAmberIsland()) {
            this.collected_relics -= (float)((int)total);
         } else {
            this.collected_relics = 0.0F;
         }

         this.resetUnderlingCurrencyType(island);
         return (int)total;
      } else {
         return 0;
      }
   }

   public int getEntityId() {
      return MonsterLookup.get(this.getType()).getEntityId();
   }

   public void setInHotel(boolean inHotel) {
      this.in_hotel = inHotel;
   }

   public void resetLastCollection() {
      this.last_collection = MSMExtension.CurrentDBTime();
   }

   public PlayerMonster getGoldMonster(Player player) {
      PlayerMonster goldMonster = null;
      SFSObject goldIslandData = this.getGoldIslandData();
      if (goldIslandData == null) {
         return null;
      } else {
         long goldUserMonsterId = goldIslandData.getLong("monster");
         Long goldIslandId = goldIslandData.getLong("island");
         PlayerIsland goldIsland = player.getIslandByID(goldIslandId);
         if (goldIsland == null) {
            goldIsland = player.getIslandByIslandIndex(6);
         }

         goldMonster = goldIsland.getMonsterByID(goldUserMonsterId);
         return goldMonster;
      }
   }

   public boolean isTraining() {
      return this.is_training;
   }

   public long trainingStart() {
      return this.training_start;
   }

   public long trainingCompletion() {
      return this.training_completion;
   }

   public void startTraining(long startTimestamp, long completionTimestamp) {
      this.is_training = true;
      this.training_start = startTimestamp;
      this.training_completion = completionTimestamp;
   }

   public void finishTraining(Player p, PlayerIsland island) {
      this.is_training = false;
      this.levelUp(p, island);
   }

   public void reduceTrainingTimeByVideo() {
      long reduceTimeAmount = GameSettings.getLong("USER_SPEED_UP_DURATION") * 60L * 1000L;
      this.training_start -= reduceTimeAmount;
      this.training_completion -= reduceTimeAmount;
   }

   public MonsterCostumeState getCostumeState() {
      return this.costumeState_;
   }

   public void collectSpecialFromMonster(User sender, ISFSObject collectResponse, Player player, GameStateHandler handler) {
      MSMExtension ext = MSMExtension.getInstance();
      if (this.getLevel() > GameSettings.getInt("GOLD_ISLAND_LEVEL")) {
         double totalPrizeDistribution = 0.0D;

         for(int i = 0; i < MonsterLookup.prizes.size(); ++i) {
            totalPrizeDistribution += SFSHelpers.getDouble("dist", MonsterLookup.prizes.getSFSObject(i));
         }

         long now = MSMExtension.CurrentDBTime();
         Monster staticMonster = MonsterLookup.get(this.getType());
         double monsterCollectChance = staticMonster.specialCollectionRate(this);
         double monsterDayValue = (double)(now - this.getLastCollectedTime()) / MonsterLookup.prizeDuration;
         double playerUnclaimedDays = (double)(now - player.getSpecialCollectionTimer("default")) / MonsterLookup.prizeDuration;
         if (monsterDayValue > MonsterLookup.monsterPrizeRollover) {
            monsterDayValue = MonsterLookup.monsterPrizeRollover;
         }

         if (playerUnclaimedDays > MonsterLookup.minimumPrizeTimer || this.rng.nextDouble() < monsterDayValue * monsterCollectChance * playerUnclaimedDays) {
            if (playerUnclaimedDays > MonsterLookup.globalPrizeRollover) {
               playerUnclaimedDays = MonsterLookup.globalPrizeRollover;
            }

            playerUnclaimedDays -= 1.0D / (double)MonsterLookup.maximumDailyPrizes;
            player.setSpecialCollectionTimer("default", (long)((double)now - playerUnclaimedDays * MonsterLookup.prizeDuration));
            double rewardTypeRNG = this.rng.nextDouble();
            int rewardType = 0;
            double totalOdds = 0.0D;

            int rewardAmount;
            for(rewardAmount = 1; rewardAmount < MonsterLookup.prizes.size(); ++rewardAmount) {
               ISFSObject curPrize = MonsterLookup.prizes.getSFSObject(rewardAmount);
               double unclaimedDays = (double)(now - player.getSpecialCollectionTimer(curPrize.getUtfString("name"))) / MonsterLookup.prizeDuration;
               if (unclaimedDays > MonsterLookup.globalPrizeRollover) {
                  unclaimedDays = MonsterLookup.globalPrizeRollover;
               }

               if (unclaimedDays > 0.0D) {
                  totalOdds += SFSHelpers.getDouble("dist", curPrize) / totalPrizeDistribution;
                  if (rewardTypeRNG < totalOdds) {
                     rewardType = rewardAmount;
                     unclaimedDays -= 1.0D / (double)SFSHelpers.getInt("cap", curPrize);
                     player.setSpecialCollectionTimer(curPrize.getUtfString("name"), (long)((double)now - unclaimedDays * MonsterLookup.prizeDuration));
                     break;
                  }
               }
            }

            rewardAmount = SFSHelpers.getInt("amount", MonsterLookup.prizes.getSFSObject(rewardType));
            String prizeName = MonsterLookup.prizes.getSFSObject(rewardType).getUtfString("name");
            collectResponse.putInt(prizeName, rewardAmount);
            if (prizeName.equals("key")) {
               player.adjustKeys(sender, handler, rewardAmount);
            } else if (prizeName.equals("relic")) {
               player.adjustRelics(sender, handler, rewardAmount);
            } else if (prizeName.equals("diamond")) {
               player.adjustDiamonds(sender, handler, rewardAmount);
               ext.stats.trackReward(sender, "monster_collect", "diamonds", (long)rewardAmount);
            } else if (prizeName.equals("ethereal_currency")) {
               player.adjustEthCurrency(sender, handler, rewardAmount);
            } else if (prizeName.equals("star")) {
               player.adjustStarpower(sender, handler, (long)rewardAmount);
            } else if (prizeName.equals("food")) {
               player.adjustFood(sender, handler, rewardAmount);
            } else if (prizeName.equals("xp")) {
            }

            if (!prizeName.equals("xp")) {
               ext.stats.trackMonsterCollectSpecial(sender, prizeName, rewardAmount, this.getType(), this.getID(), this.getLevel());
            }
         }

      }
   }

   public boolean isRare() {
      return MonsterCommonToRareMapping.isRare(this.getType());
   }

   public boolean isEpic() {
      return MonsterCommonToEpicMapping.isEpic(this.getType());
   }

   public boolean isCommon() {
      return !this.isRare() && !this.isEpic();
   }

   public class BoxFillCost {
      public int diamonds;
      public int wildcards;

      public BoxFillCost(int diamondCost, int wildcardCost) {
         this.diamonds = diamondCost;
         this.wildcards = wildcardCost;
      }
   }

   public static enum UnderlingCollectionType {
      Diamonds,
      Shards,
      Starpower,
      Food,
      Coins,
      Keys,
      Relics,
      MaxCollectionTypes;
   }
}
