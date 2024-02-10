package com.bigbluebubble.mysingingmonsters.player;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.GameStateHandler;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.battle.BattleIslandState;
import com.bigbluebubble.mysingingmonsters.costumes.IslandCostumeState;
import com.bigbluebubble.mysingingmonsters.data.BreedingLookup;
import com.bigbluebubble.mysingingmonsters.data.Island;
import com.bigbluebubble.mysingingmonsters.data.IslandLookup;
import com.bigbluebubble.mysingingmonsters.data.IslandTheme;
import com.bigbluebubble.mysingingmonsters.data.IslandThemeLookup;
import com.bigbluebubble.mysingingmonsters.data.LevelLookup;
import com.bigbluebubble.mysingingmonsters.data.Monster;
import com.bigbluebubble.mysingingmonsters.data.MonsterLookup;
import com.bigbluebubble.mysingingmonsters.data.Structure;
import com.bigbluebubble.mysingingmonsters.data.StructureLookup;
import com.bigbluebubble.mysingingmonsters.data.TorchLookup;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventManager;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.ReturningUserBonusEvent;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class PlayerIsland {
   public static final String ID_KEY = "user_island_id";
   public static final String INDEX_KEY = "island";
   private static final String TRIBAL_KEY = "tribal_id";
   public static final String TRIBAL_REQUESTS_KEY = "tribal_requests";
   public static final String TRIBAL_ISLAND_DATA_KEY = "tribal_island_data";
   private static final String TRIBAL_QUESTS_KEY = "tribal_quests";
   public static final String STRUCTURES_KEY = "structures";
   public static final String MONSTERS_KEY = "monsters";
   public static final String VOLATILE_KEY = "volatile";
   public static final String EGGS_KEY = "eggs";
   public static final String BREEDING_KEY = "breeding";
   public static final String BAKING_KEY = "baking";
   public static final String WAREHOUSE_KEY = "warehouse";
   public static final String HOTEL_KEY = "hotel";
   public static final String TORCH_KEY = "torches";
   public static final String FUZER_KEY = "fuzer";
   public static final String BUYBACK_KEY = "buyback";
   public static final String CRUCIBLE_KEY = "evolving";
   public static final String ATTUNING_KEY = "attuning";
   public static final String ATTUNED_CRITTERS_KEY = "attuned_critters";
   public static final String CRITTER_GENE_KEY = "gene";
   public static final String CRITTER_NUM_KEY = "num";
   public static final String SYNTHESIZING_KEY = "synthesizing";
   public static final String LAST_BRED_KEY = "last_bred";
   public static final String LAST_BRED_MONSTER_1_KEY = "user_monster_1";
   public static final String LAST_BRED_MONSTER_2_KEY = "user_monster_2";
   private static final String CHIEF_KEY = "chief_name";
   public static final String LAST_PLAYER_LEVEL = "last_player_level";
   public static final String LIGHT_TORCH_FLAG = "light_torch_flag";
   public static final String LAST_BAKED_KEY = "last_baked";
   public static final String LAST_BAKED_BAKERY_KEY = "bakery";
   public static final String LAST_BAKED_FOOD_KEY = "food";
   public static final String ISLAND_SKIN_TRIAL = "island_skin_trial";
   public static final int GOLD_ISLAND_INDEX = 6;
   public static final int ETHEREAL_ISLAND_INDEX = 7;
   public static final int SHUGA_ISLAND_INDEX = 8;
   public static final int TRIBAL_ISLAND_INDEX = 9;
   public static final int UNDERLING_ISLAND_INDEX = 10;
   public static final int COMPOSER_ISLAND_INDEX = 11;
   public static final int CELESTIAL_ISLAND_INDEX = 12;
   public static final int FIRE_ISLAND_INDEX = 13;
   public static final int FIRE_OASIS_ISLAND_INDEX = 14;
   public static final int FIRE_ISLAND_3_INDEX = 15;
   public static final int FAIRY_ISLAND_INDEX = 16;
   public static final int MAGICAL_ETHEREAL_ISLAND_INDEX = 19;
   public static final int BATTLE_ISLAND_INDEX = 20;
   public static final int SEASONAL_ISLAND_INDEX = 21;
   public static final int AMBER_ISLAND_INDEX = 22;
   public static final int MYTHICAL_ISLAND_INDEX = 23;
   public static final int ETHEREAL_WORKSHOP_ISLAND_INDEX = 24;
   public static int KAYNA_INDEX = 289;
   public static int KAYNA_TRAPPED_INDEX = 290;
   private static final double MINIMUM_WARP_SPEED = 0.5D;
   private static final double MAXIMUM_WARP_SPEED = 2.0D;
   private ISFSObject data = null;
   private HashMap<Long, PlayerMonster> monsterMap;
   private HashMap<Long, PlayerStructure> structureMap;
   private SFSArray soldMonsterTypes = null;
   private SFSArray allPrevOwnedCostumes_ = null;
   private boolean allPrevOwnedCostumesDirty_ = false;
   private ArrayList<PlayerStructure> eggHolders;
   private ArrayList<PlayerEgg> eggObjects = new ArrayList();
   private ArrayList<PlayerStructure> breedingStructures;
   private ArrayList<PlayerBreeding> breedingObjects = new ArrayList();
   private ArrayList<PlayerCrucibleData> crucibleObjects = new ArrayList();
   private ArrayList<PlayerAttuningData> attuningObjects = new ArrayList();
   private ArrayList<PlayerSynthesizingData> synthesizingObjects = new ArrayList();
   private HashMap<String, Integer> attunedCritterCounts;
   private PlayerBuyback buybackObject = null;
   private HashMap<Long, PlayerBaking> bakingMap;
   private HashMap<Long, Integer> lastBakedMap;
   private HashMap<Long, PlayerFuzeBuddy> buddyMap;
   private HashMap<Long, LitPlayerTorch> litTorchMap;
   private long lastBredUserMonster1 = 0L;
   private long lastBredUserMonster2 = 0L;
   private HashMap<String, Float> islandThemeModifierMap;
   private String name;
   private long user_island_id;
   private long user;
   private int island;
   private long tribal_id = 0L;
   private long date_created;
   private int likes;
   private int dislikes;
   private float warp_speed;
   int lastPlayerLevel = -1;
   private boolean lightTorchFlag = false;
   private int islandThemeTrial = 0;
   public static final String COSTUME_KEY = "costume_data";
   private IslandCostumeState costumeState_;
   public static final String BATTLE_KEY = "battle";
   BattleIslandState battleIslandState_;

   public PlayerIsland(ISFSObject islandData) {
      this.initFromSFSObject(islandData);
      this.monsterMap = new HashMap();
      this.structureMap = new HashMap();
      this.eggHolders = new ArrayList();
      this.breedingStructures = new ArrayList();
      this.bakingMap = new HashMap();
      this.lastBakedMap = new HashMap();
      this.buddyMap = new HashMap();
      this.litTorchMap = new HashMap();
      this.islandThemeModifierMap = new HashMap();
      this.attunedCritterCounts = new HashMap();
   }

   public String toString() {
      return this.toSFSObject().getDump();
   }

   public void initFromSFSObject(ISFSObject s) {
      try {
         this.name = s.getUtfString("name");
         this.user_island_id = s.getLong("user_island_id");
         this.user = s.getLong("user");
         this.island = s.getInt("island");
         this.date_created = s.getLong("date_created");
         this.likes = s.getInt("likes");
         this.dislikes = s.getInt("dislikes");
         this.warp_speed = s.getDouble("warp_speed").floatValue();
         if (s.containsKey("tribal_id")) {
            this.tribal_id = s.getLong("tribal_id");
         }
      } catch (ClassCastException var3) {
         Logger.trace((Exception)var3, "Player Island initialization error");
      }

      this.data = s;
   }

   public ISFSObject toSFSObject() {
      ISFSObject fullData = SFSHelpers.clone(this.data);
      if (this.getName() != null) {
         fullData.putUtfString("name", this.getName());
      }

      fullData.putLong("user_island_id", this.getID());
      fullData.putLong("user", this.getUser());
      fullData.putInt("island", this.getIndex());
      fullData.putInt("type", this.getType());
      fullData.putLong("date_created", this.getDateCreated());
      fullData.putInt("likes", this.getLikes());
      fullData.putInt("dislikes", this.getDislikes());
      fullData.putDouble("warp_speed", this.getWarpSpeed());
      ISFSArray torches = new SFSArray();
      Iterator i = this.structureMap.entrySet().iterator();

      while(i.hasNext()) {
         PlayerStructure s = (PlayerStructure)((Entry)i.next()).getValue();
         torches.addSFSObject(s.toSFSObject(this));
      }

      fullData.putSFSArray("structures", torches);
      torches = new SFSArray();
      i = this.monsterMap.entrySet().iterator();

      while(i.hasNext()) {
         PlayerMonster s = (PlayerMonster)((Entry)i.next()).getValue();
         torches.addSFSObject(s.toSFSObject(this));
      }

      fullData.putSFSArray("monsters", torches);
      torches = new SFSArray();

      int i;
      for(i = 0; i < this.eggObjects.size(); ++i) {
         torches.addSFSObject(((PlayerEgg)this.eggObjects.get(i)).toSFSObject());
      }

      fullData.putSFSArray("eggs", torches);
      ISFSArray synthesizing = new SFSArray();

      int i;
      for(i = 0; i < this.breedingObjects.size(); ++i) {
         synthesizing.addSFSObject(((PlayerBreeding)this.breedingObjects.get(i)).toSFSObject());
      }

      fullData.putSFSArray("breeding", synthesizing);
      if (this.isAmberIsland()) {
         torches = new SFSArray();

         for(i = 0; i < this.crucibleObjects.size(); ++i) {
            torches.addSFSObject(((PlayerCrucibleData)this.crucibleObjects.get(i)).toSFSObject());
         }

         fullData.putSFSArray("evolving", torches);
      }

      Iterator lastBakedItr;
      SFSArray critters;
      if (this.isEtherealWorkshopIsland()) {
         torches = new SFSArray();

         for(i = 0; i < this.attuningObjects.size(); ++i) {
            torches.addSFSObject(((PlayerAttuningData)this.attuningObjects.get(i)).toSFSObject());
         }

         fullData.putSFSArray("attuning", torches);
         synthesizing = new SFSArray();

         for(i = 0; i < this.synthesizingObjects.size(); ++i) {
            synthesizing.addSFSObject(((PlayerSynthesizingData)this.synthesizingObjects.get(i)).toSFSObject());
         }

         fullData.putSFSArray("synthesizing", synthesizing);
         critters = new SFSArray();
         lastBakedItr = this.attunedCritterCounts.entrySet().iterator();

         while(lastBakedItr.hasNext()) {
            Entry<String, Integer> entry = (Entry)lastBakedItr.next();
            ISFSObject count = new SFSObject();
            count.putUtfString("gene", (String)entry.getKey());
            count.putInt("num", (Integer)entry.getValue());
            critters.addSFSObject(count);
         }

         fullData.putSFSArray("attuned_critters", critters);
      }

      torches = new SFSArray();
      i = this.bakingMap.entrySet().iterator();

      while(i.hasNext()) {
         PlayerBaking s = (PlayerBaking)((Entry)i.next()).getValue();
         torches.addSFSObject(s.toSFSObject());
      }

      fullData.putSFSArray("baking", torches);
      critters = new SFSArray();
      lastBakedItr = this.lastBakedMap.entrySet().iterator();

      while(lastBakedItr.hasNext()) {
         critters.addSFSObject(this.lastBakedToSFSObject((Long)((Entry)lastBakedItr.next()).getKey()));
      }

      fullData.putSFSArray("last_baked", critters);
      torches = new SFSArray();
      i = this.buddyMap.entrySet().iterator();

      while(i.hasNext()) {
         PlayerFuzeBuddy b = (PlayerFuzeBuddy)((Entry)i.next()).getValue();
         torches.addSFSObject(b.toSFSObject());
      }

      fullData.putSFSArray("fuzer", torches);
      torches = new SFSArray();
      i = this.litTorchMap.entrySet().iterator();

      while(i.hasNext()) {
         LitPlayerTorch s = (LitPlayerTorch)((Entry)i.next()).getValue();
         torches.addSFSObject(s.toSFSObject());
      }

      fullData.putSFSArray("torches", torches);
      if (this.buybackObject != null) {
         fullData.putSFSObject("buyback", this.buybackObject.toSFSObject());
      }

      fullData.putInt("last_player_level", this.lastPlayerLevel);
      fullData.putBool("light_torch_flag", this.getLightTorchFlag());
      ISFSObject lastBred = this.lastBredToSFSObject();
      if (lastBred != null) {
         fullData.putSFSObject("last_bred", lastBred);
      }

      if (this.soldMonsterTypes != null) {
         fullData.putUtfString("monsters_sold", this.soldMonsterTypes.toJson());
      }

      if (this.allPrevOwnedCostumes_ == null) {
         this.initializeOwnedCostumes();
      }

      fullData.putUtfString("costumes_owned", this.allPrevOwnedCostumes_.toJson());
      if (this.costumeState_ != null) {
         fullData.putSFSObject("costume_data", this.costumeState_.toSFSObject());
      }

      if (this.battleIslandState_ != null) {
         ISFSObject battleIslandStateObj = this.battleIslandState_.toSFSObject();
         if (this.costumeState_ != null) {
            battleIslandStateObj.putSFSObject("costume_data", this.costumeState_.toSFSObject());
         }

         fullData.putSFSObject("battle", battleIslandStateObj);
      }

      if (this.islandThemeTrial != 0) {
         fullData.putInt("island_skin_trial", this.islandThemeTrial);
      }

      return fullData;
   }

   public void initializeOwnedCostumes() {
      this.allPrevOwnedCostumes_ = new SFSArray();
      Iterator i = this.monsterMap.entrySet().iterator();

      while(i.hasNext()) {
         PlayerMonster s = (PlayerMonster)((Entry)i.next()).getValue();
         this.addToOwnedCostumes(s.costumeState_.getEquipped());
         Iterator p = s.costumeState_.getPurchased().iterator();

         while(p.hasNext()) {
            this.addToOwnedCostumes((Integer)p.next());
         }
      }

      this.setAllPrevOwnedCostumesDirty(true);
   }

   public SFSArray soldMonsterTypes() {
      return this.soldMonsterTypes;
   }

   public SFSArray allPrevOwnedCostumes() {
      return this.allPrevOwnedCostumes_;
   }

   public boolean getAllPrevOwnedCostumesDirty() {
      return this.allPrevOwnedCostumesDirty_;
   }

   public void setAllPrevOwnedCostumesDirty(boolean b) {
      this.allPrevOwnedCostumesDirty_ = b;
   }

   public void initStructures(ISFSArray structuresData) throws Exception {
      this.structureMap.clear();
      Iterator i = structuresData.iterator();

      while(i.hasNext()) {
         ISFSObject s = (ISFSObject)((SFSDataWrapper)i.next()).getObject();
         Integer structureType = s.getInt("structure");
         if (StructureLookup.get(structureType) != null) {
            PlayerStructure structure = new PlayerStructure(s);
            this.structureMap.put(structure.getID(), structure);
            if (structure.isNursery()) {
               this.eggHolders.add(structure);
               if (this.eggHolders.size() > GameSettings.getInt("USER_MAX_NUM_NURSERIES")) {
                  throw new Exception("PlayerIsland::initStructures: too many nurseries added");
               }
            } else if (structure.isSynthesizer()) {
               this.eggHolders.add(structure);
               if (this.eggHolders.size() > GameSettings.getInt("USER_MAX_NUM_SYNTHESIZERS")) {
                  throw new Exception("PlayerIsland::initStructures: too many synthesizers added");
               }
            } else if (structure.isBreeding()) {
               this.breedingStructures.add(structure);
               if (this.breedingStructures.size() > GameSettings.getInt("USER_MAX_NUM_BREEDING_STRUCTURES")) {
                  throw new Exception("PlayerIsland::initStructures: too many breeding structures added");
               }
            }
         }
      }

   }

   public void initMonsters(ISFSArray monstersData) {
      this.monsterMap.clear();
      Iterator i = monstersData.iterator();

      while(i.hasNext()) {
         ISFSObject monsterData = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
         PlayerMonster monster = new PlayerMonster(monsterData, this);
         this.monsterMap.put(monster.getID(), monster);
      }

   }

   public void initTribalRequests(ISFSArray requestData) {
      this.data.putSFSArray("tribal_requests", requestData);
   }

   public void initTribalIslandData(ISFSObject tribalData) {
      this.data.putSFSObject("tribal_island_data", tribalData);
   }

   public void initTribalQuests(ISFSArray questData) {
      this.data.putSFSArray("tribal_quests", questData);
   }

   public void addChiefData(String chiefName) {
      this.data.getSFSObject("tribal_island_data").putUtfString("chief_name", chiefName);
   }

   public ISFSObject getTribalIslandData() {
      return this.data.containsKey("tribal_island_data") ? this.data.getSFSObject("tribal_island_data") : null;
   }

   public ISFSArray getTribalRequests() {
      return this.data.containsKey("tribal_requests") ? this.data.getSFSArray("tribal_requests") : null;
   }

   public void initMonstersFromJson(String jsonMonsterData) {
      ISFSArray monsterData = SFSArray.newFromJsonData(jsonMonsterData);
      this.initMonsters(monsterData);
   }

   public void initSoldMonstersFromJson(String jsonSoldMonsterData) {
      if (jsonSoldMonsterData != null) {
         this.soldMonsterTypes = SFSArray.newFromJsonData(jsonSoldMonsterData);
      }

   }

   public void initPrevCostumesFromJson(String jsonPrevCostumeData) {
      if (jsonPrevCostumeData != null) {
         this.allPrevOwnedCostumes_ = SFSArray.newFromJsonData(jsonPrevCostumeData);
      }

   }

   public void initStructuresFromJson(String jsonStructureData) throws Exception {
      ISFSArray structuresData = SFSArray.newFromJsonData(jsonStructureData);
      this.initStructures(structuresData);
   }

   public void initEggs(ISFSObject eggData) throws Exception {
      this.eggObjects.clear();
      if (!eggData.containsKey("structure") && this.eggHolders.size() != 0) {
         eggData.putLong("structure", ((PlayerStructure)this.eggHolders.get(0)).getID());
      }

      this.eggObjects.add(new PlayerEgg(eggData));
   }

   public void initEggs(ISFSArray eggData) throws Exception {
      this.eggObjects.clear();

      for(int i = 0; i < eggData.size(); ++i) {
         this.eggObjects.add(new PlayerEgg(eggData.getSFSObject(i)));
      }

      if (this.eggObjects.size() > GameSettings.getInt("USER_MAX_NUM_NURSERIES")) {
         throw new Exception("too many nurseries somehow");
      }
   }

   public void initBreeding(ISFSObject breedingData) throws PlayerLoadingException {
      this.breedingObjects.clear();
      if (!breedingData.containsKey("structure") && this.breedingStructures.size() != 0) {
         breedingData.putLong("structure", ((PlayerStructure)this.breedingStructures.get(0)).getID());
      }

      this.breedingObjects.add(new PlayerBreeding(breedingData));
   }

   public void initBreeding(ISFSArray breedingData) throws PlayerLoadingException {
      for(int i = 0; i < breedingData.size(); ++i) {
         this.breedingObjects.add(new PlayerBreeding(breedingData.getSFSObject(i)));
      }

      if (this.breedingObjects.size() > GameSettings.getInt("USER_MAX_NUM_BREEDING_STRUCTURES")) {
         throw new PlayerLoadingException("too many breeding structures somehow");
      }
   }

   public void initCrucible(ISFSArray crucibleData) throws PlayerLoadingException {
      for(int i = crucibleData.size() - 1; i >= 0; --i) {
         ISFSObject sfs = crucibleData.getSFSObject(i);
         long structureId = SFSHelpers.getLong(sfs.get("struct"));
         PlayerCrucibleData c = this.getCrucibleData(structureId);
         if (c == null) {
            this.crucibleObjects.add(new PlayerCrucibleData(sfs));
         } else {
            c.initFromSFSObject(sfs);
         }
      }

      if (this.crucibleObjects.size() > 1) {
         throw new PlayerLoadingException("too many crucibles somehow");
      }
   }

   public void initAttuning(ISFSArray attuningData) throws PlayerLoadingException {
      for(int i = 0; i < attuningData.size(); ++i) {
         this.attuningObjects.add(new PlayerAttuningData(attuningData.getSFSObject(i)));
      }

      if (this.attuningObjects.size() > 1) {
         throw new PlayerLoadingException("too many attunings somehow");
      }
   }

   public void initCritters(ISFSArray critters) throws PlayerLoadingException {
      for(int i = critters.size() - 1; i >= 0; --i) {
         ISFSObject sfs = critters.getSFSObject(i);
         if (this.attunedCritterCounts.containsKey(sfs.getUtfString("gene"))) {
            throw new PlayerLoadingException("duplicate critter entry");
         }

         this.attunedCritterCounts.put(sfs.getUtfString("gene"), sfs.getInt("num"));
      }

   }

   public void initSynthesizing(ISFSArray synthesizingData) throws PlayerLoadingException {
      for(int i = 0; i < synthesizingData.size(); ++i) {
         this.synthesizingObjects.add(new PlayerSynthesizingData(synthesizingData.getSFSObject(i)));
      }

      if (this.synthesizingObjects.size() > 1) {
         throw new PlayerLoadingException("too many synthesizing somehow");
      }
   }

   public void initBaking(ISFSArray bakingData) {
      this.bakingMap.clear();
      Iterator i = bakingData.iterator();

      while(i.hasNext()) {
         ISFSObject data = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
         PlayerBaking pBaking = new PlayerBaking(data);
         this.bakingMap.put(pBaking.getID(), pBaking);
      }

   }

   public void initFuzeBuddies(ISFSArray buddyData) {
      this.buddyMap.clear();
      Iterator i = buddyData.iterator();

      while(i.hasNext()) {
         ISFSObject data = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
         PlayerFuzeBuddy buddy = new PlayerFuzeBuddy(data);
         this.buddyMap.put(buddy.getStructureID(), buddy);
      }

   }

   public void initTorches(ISFSArray torchData) {
      long curTime = MSMExtension.CurrentDBTime();
      this.litTorchMap.clear();
      Iterator i = torchData.iterator();

      while(i.hasNext()) {
         ISFSObject data = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
         LitPlayerTorch litTorch = new LitPlayerTorch(data);
         if (litTorch != null && !this.torchExpired(litTorch, curTime)) {
            this.litTorchMap.put(litTorch.userStructure, litTorch);
         }
      }

   }

   public void fixMonsters() {
      if (this.getIndex() != 20) {
         Iterator<Entry<Long, PlayerMonster>> i = this.monsterMap.entrySet().iterator();
         Island staticIsland = IslandLookup.get(this.island);

         while(i.hasNext()) {
            PlayerMonster pm = (PlayerMonster)((Entry)i.next()).getValue();
            if (!staticIsland.hasMonster(pm.getType())) {
               Logger.trace("Removing monster " + pm.getID() + " from island " + this.getID());
               i.remove();
            }
         }

      }
   }

   public void fixMonsterLevels() {
      int maxLevel = GameSettings.get("MAX_MONSTER_LEVEL", 20);
      int maxUnderlingLevel = GameSettings.get("MAX_UNDERLING_LEVEL", 1);
      Iterator i = this.monsterMap.entrySet().iterator();

      while(i.hasNext()) {
         PlayerMonster pm = (PlayerMonster)((Entry)i.next()).getValue();
         if (!this.noMonsterLevellingIsland()) {
            if (pm.getLevel() > maxLevel) {
               pm.setLevel(maxLevel);
            }
         } else if (pm.getLevel() > maxUnderlingLevel) {
            pm.setLevel(maxUnderlingLevel);
         }
      }

   }

   public void initIslandThemeModifiers(ISFSArray modifierData) {
      this.islandThemeModifierMap.clear();
      Iterator i = modifierData.iterator();

      while(true) {
         IslandTheme theme;
         do {
            if (!i.hasNext()) {
               return;
            }

            Integer data = (Integer)((Integer)((SFSDataWrapper)i.next()).getObject());
            theme = IslandThemeLookup.get(data);
         } while(theme.getIsland() != this.island);

         ISFSObject modifiers = theme.getModifiers();
         Iterator var6 = modifiers.getKeys().iterator();

         while(var6.hasNext()) {
            String key = (String)var6.next();
            if (this.islandThemeModifierMap.containsKey(key)) {
               this.islandThemeModifierMap.put(key, (Float)this.islandThemeModifierMap.get(key) * modifiers.getFloat(key));
            } else {
               this.islandThemeModifierMap.put(key, modifiers.getFloat(key));
            }
         }
      }
   }

   public void checkForInvalidData(Player p) {
      Iterator<Entry<Long, LitPlayerTorch>> it = this.litTorchMap.entrySet().iterator();
      ArrayList invalidTorchStructureIds = new ArrayList();

      Long structureId;
      PlayerStructure n;
      while(it.hasNext()) {
         Entry<Long, LitPlayerTorch> torchPairData = (Entry)it.next();
         structureId = (Long)torchPairData.getKey();
         n = (PlayerStructure)this.structureMap.get(structureId);
         if (n == null) {
            invalidTorchStructureIds.add(structureId);
         } else if (!n.isTorch()) {
            invalidTorchStructureIds.add(structureId);
         }
      }

      Iterator invalidDataIt = invalidTorchStructureIds.iterator();

      while(invalidDataIt.hasNext()) {
         structureId = (Long)invalidDataIt.next();
         this.litTorchMap.remove(structureId);
      }

      ArrayList<PlayerEgg> badEggs = new ArrayList();

      for(int i = 0; i < this.eggObjects.size(); ++i) {
         PlayerEgg e = (PlayerEgg)this.eggObjects.get(i);
         if (e.getStructureID() == 0L && e.getType() != 0) {
            try {
               Iterator var13 = this.eggHolders.iterator();

               while(var13.hasNext()) {
                  n = (PlayerStructure)var13.next();
                  if (!this.eggHolderHasEgg(n.getID())) {
                     Logger.trace(LogLevel.WARN, String.format("Replacing monster %d egg for user %d in structure %d on island %d", e.getType(), this.getUser(), n.getID(), this.getID()));
                     PlayerEgg newEgg = new PlayerEgg(this.makeNewISFSEgg(p, e.getType(), false, n.getID()));
                     newEgg.prevName = e.prevName;
                     if (this.isBattleIsland()) {
                        newEgg.setCostumeData(e.getCostumeData());
                     }

                     this.eggObjects.set(i, newEgg);
                     break;
                  }
               }
            } catch (Exception var8) {
               Logger.trace(var8);
            }

            if (((PlayerEgg)this.eggObjects.get(i)).getID() == 0L) {
               badEggs.add(this.eggObjects.get(i));
            }
         }
      }

      if (badEggs.size() > 0) {
         Logger.trace(LogLevel.WARN, String.format("Removing %d bad eggs for user %d on island %d", badEggs.size(), this.getUser(), this.getID()));
         this.eggObjects.removeAll(badEggs);
      }

   }

   public void initBuyback(ISFSObject buybackData) {
      this.buybackObject = new PlayerBuyback(buybackData);
   }

   public void initLastBred(ISFSObject lastBredData) {
      try {
         Iterator iterator = lastBredData.iterator();

         while(iterator.hasNext()) {
            Entry<String, SFSDataWrapper> e = (Entry)iterator.next();
            if (((String)e.getKey()).equals("user_monster_1")) {
               this.lastBredUserMonster1 = SFSHelpers.getLong((SFSDataWrapper)e.getValue());
            } else {
               if (!((String)e.getKey()).equals("user_monster_2")) {
                  throw new ClassCastException("Invalid SFSObject key " + (String)e.getKey());
               }

               this.lastBredUserMonster2 = SFSHelpers.getLong((SFSDataWrapper)e.getValue());
            }
         }
      } catch (ClassCastException var4) {
         Logger.trace((Exception)var4, "Last bred initialization error");
      }

   }

   public void initLastBaked(ISFSArray lastBakedData) {
      try {
         this.lastBakedMap.clear();

         for(int i = 0; i < lastBakedData.size(); ++i) {
            ISFSObject sfsObj = lastBakedData.getSFSObject(i);
            this.lastBakedMap.put(SFSHelpers.getLong(sfsObj.get("bakery")), SFSHelpers.getInt(sfsObj.get("food")));
         }
      } catch (ClassCastException var4) {
         Logger.trace((Exception)var4, "Last baked initialization error");
      }

   }

   public void setLastPlayerLevel(int level) {
      this.lastPlayerLevel = level;
   }

   public void setGiMappings(SFSArray mappings) {
      this.data.putSFSArray("gi_mappings", mappings);
   }

   public SFSArray getIslandMonsterDataForSave() {
      SFSArray islandMonsterData = new SFSArray();
      Collection<PlayerMonster> monsters = this.getMonsters();
      if (monsters != null && monsters.size() > 0) {
         Iterator s = monsters.iterator();

         while(s.hasNext()) {
            PlayerMonster monster = (PlayerMonster)s.next();
            islandMonsterData.addSFSObject(monster.toSFSObjectWithoutBoxRequirements(this));
         }
      }

      return islandMonsterData;
   }

   public SFSArray getIslandStructureData() {
      SFSArray islandStructureData = new SFSArray();
      Collection<PlayerStructure> structures = this.getStructures();
      if (structures != null && structures.size() > 0) {
         Iterator s = structures.iterator();

         while(s.hasNext()) {
            PlayerStructure structure = (PlayerStructure)s.next();
            islandStructureData.addSFSObject(structure.toSFSObject(this));
         }
      }

      return islandStructureData;
   }

   public SFSObject getIslandVolatileData() {
      SFSObject islandVolatileData = new SFSObject();
      ArrayList<PlayerEgg> eggs = this.getEggs();
      if (eggs != null && eggs.size() != 0) {
         ISFSArray eggArray = new SFSArray();

         for(int i = 0; i < eggs.size(); ++i) {
            eggArray.addSFSObject(((PlayerEgg)eggs.get(i)).toSFSObject());
         }

         if (eggArray.size() > 0) {
            islandVolatileData.putSFSArray("eggs", eggArray);
         }
      }

      ArrayList<PlayerBreeding> breedings = this.getBreedings();
      if (breedings != null && breedings.size() != 0) {
         ISFSArray breedingArray = new SFSArray();

         for(int i = 0; i < breedings.size(); ++i) {
            breedingArray.addSFSObject(((PlayerBreeding)breedings.get(i)).toSFSObject());
         }

         if (breedingArray.size() > 0) {
            islandVolatileData.putSFSArray("breeding", breedingArray);
         }
      }

      ArrayList<PlayerCrucibleData> crucibles = this.getCrucibleEvolves();
      if (crucibles != null && crucibles.size() != 0) {
         ISFSArray crucibleArray = new SFSArray();

         for(int i = 0; i < crucibles.size(); ++i) {
            crucibleArray.addSFSObject(((PlayerCrucibleData)crucibles.get(i)).toSFSObject());
         }

         if (crucibleArray.size() > 0) {
            islandVolatileData.putSFSArray("evolving", crucibleArray);
         }
      }

      ArrayList<PlayerAttuningData> attuners = this.getAttuningData();
      SFSArray attunedCrittersArray;
      if (attuners != null && attuners.size() != 0) {
         attunedCrittersArray = new SFSArray();

         for(int i = 0; i < attuners.size(); ++i) {
            attunedCrittersArray.addSFSObject(((PlayerAttuningData)attuners.get(i)).toSFSObject());
         }

         if (attunedCrittersArray.size() > 0) {
            islandVolatileData.putSFSArray("attuning", attunedCrittersArray);
         }
      }

      if (this.attunedCritterCounts != null && this.attunedCritterCounts.keySet().size() != 0) {
         attunedCrittersArray = new SFSArray();
         Iterator var24 = this.attunedCritterCounts.entrySet().iterator();

         while(var24.hasNext()) {
            Entry<String, Integer> entry = (Entry)var24.next();
            if ((Integer)entry.getValue() > 0) {
               ISFSObject count = new SFSObject();
               count.putUtfString("gene", (String)entry.getKey());
               count.putInt("num", (Integer)entry.getValue());
               attunedCrittersArray.addSFSObject(count);
            }
         }

         if (attunedCrittersArray.size() > 0) {
            islandVolatileData.putSFSArray("attuned_critters", attunedCrittersArray);
         }
      }

      ArrayList<PlayerSynthesizingData> synthesizers = this.getSynthesizingData();
      SFSArray synthesizerArray;
      if (synthesizers != null && synthesizers.size() != 0) {
         synthesizerArray = new SFSArray();

         for(int i = 0; i < synthesizers.size(); ++i) {
            synthesizerArray.addSFSObject(((PlayerSynthesizingData)synthesizers.get(i)).toSFSObject());
         }

         if (synthesizerArray.size() > 0) {
            islandVolatileData.putSFSArray("synthesizing", synthesizerArray);
         }
      }

      synthesizerArray = new SFSArray();
      Collection<PlayerBaking> islandBaking = this.getBaking();
      if (islandBaking != null && islandBaking.size() > 0) {
         Iterator b = islandBaking.iterator();

         while(b.hasNext()) {
            PlayerBaking baking = (PlayerBaking)b.next();
            synthesizerArray.addSFSObject(baking.toSFSObject());
         }
      }

      if (synthesizerArray.size() > 0) {
         islandVolatileData.putSFSArray("baking", synthesizerArray);
      }

      ISFSArray islandLastBakedData = new SFSArray();
      Iterator i = this.lastBakedMap.entrySet().iterator();

      while(i.hasNext()) {
         ISFSObject sfsObj = this.lastBakedToSFSObject((Long)((Entry)i.next()).getKey());
         if (sfsObj != null) {
            islandLastBakedData.addSFSObject(sfsObj);
         }
      }

      if (islandLastBakedData.size() > 0) {
         islandVolatileData.putSFSArray("last_baked", islandLastBakedData);
      }

      ISFSArray islandFuzerData = new SFSArray();
      Collection<PlayerFuzeBuddy> islandFuzing = this.getFuzerBuddies();
      if (islandFuzing != null && islandFuzing.size() > 0) {
         Iterator b = islandFuzing.iterator();

         while(b.hasNext()) {
            PlayerFuzeBuddy buddy = (PlayerFuzeBuddy)b.next();
            islandFuzerData.addSFSObject(buddy.toSFSObject());
         }
      }

      if (islandFuzerData.size() > 0) {
         islandVolatileData.putSFSArray("fuzer", islandFuzerData);
      }

      ISFSArray islandTorchData = new SFSArray();
      Collection<LitPlayerTorch> islandTorches = this.getLitTorches();
      if (islandTorches != null && islandTorches.size() > 0) {
         Iterator t = islandTorches.iterator();

         while(t.hasNext()) {
            LitPlayerTorch litTorch = (LitPlayerTorch)t.next();
            islandTorchData.addSFSObject(litTorch.toSFSObject());
         }
      }

      if (islandTorchData.size() > 0) {
         islandVolatileData.putSFSArray("torches", islandTorchData);
      }

      PlayerBuyback buyback = this.getBuyback();
      if (buyback != null) {
         islandVolatileData.putSFSObject("buyback", buyback.toSFSObject());
      }

      ISFSObject lastBred = this.lastBredToSFSObject();
      if (lastBred != null) {
         islandVolatileData.putSFSObject("last_bred", lastBred);
      }

      if (this.lastPlayerLevel != -1) {
         islandVolatileData.putInt("last_player_level", this.lastPlayerLevel);
      }

      if (this.costumeState_ != null) {
         islandVolatileData.putSFSObject("costume_data", this.costumeState_.toSFSObject());
      }

      if (this.isBattleIsland()) {
         islandVolatileData.putSFSObject("battle", this.battleIslandState_.toSFSObject());
      }

      islandVolatileData.putBool("light_torch_flag", this.lightTorchFlag);
      if (this.islandThemeTrial != 0) {
         islandVolatileData.putInt("island_skin_trial", this.islandThemeTrial);
      }

      return islandVolatileData;
   }

   public String getName() {
      return this.name;
   }

   public long getID() {
      return this.user_island_id;
   }

   public long getUser() {
      return this.user;
   }

   public int getStaticId() {
      return IslandLookup.get(this.island).getID();
   }

   public int getType() {
      return IslandLookup.get(this.island).getType();
   }

   public int getIndex() {
      return this.island;
   }

   public long getTribalID() {
      return this.tribal_id;
   }

   private long getDateCreated() {
      return this.date_created;
   }

   private int getLikes() {
      return this.likes;
   }

   private int getDislikes() {
      return this.dislikes;
   }

   public double getWarpSpeed() {
      return (double)this.warp_speed;
   }

   public void setWarpSpeed(double d) {
      this.warp_speed = (float)Math.max(Math.min(d, 2.0D), 0.5D);
   }

   public boolean getLightTorchFlag() {
      return this.lightTorchFlag;
   }

   public void setLightTorchFlag(boolean flag) {
      this.lightTorchFlag = flag;
   }

   public boolean isGoldIsland() {
      return this.getType() == 6;
   }

   public boolean isRegularIsland() {
      return this.getType() != 6 && this.getType() != 7;
   }

   public boolean isShugaIsland() {
      return this.getType() == 8;
   }

   public boolean isEtherealIsland() {
      return this.getType() == 7 || this.getType() == 19 || this.getType() == 24;
   }

   public boolean isTribalIsland() {
      return this.getType() == 9;
   }

   public boolean isZapIsland() {
      return this.isUnderlingIsland() || this.isCelestialIsland() || this.isAmberIsland();
   }

   public boolean noMonsterHappinessIsland() {
      return this.isGoldIsland() || this.isTribalIsland() || this.isComposerIsland() || this.isCelestialIsland();
   }

   public boolean noMonsterLevellingIsland() {
      return this.isUnderlingIsland() || this.isCelestialIsland();
   }

   public boolean isRandomCollectionIsland() {
      return this.isUnderlingIsland() || this.isCelestialIsland();
   }

   public boolean isNoNurseryIsland() {
      return this.isUnderlingIsland() || this.isCelestialIsland() || this.isComposerIsland() || this.isAmberIsland();
   }

   public boolean isUnderlingIsland() {
      return this.getType() == 10;
   }

   public boolean isComposerIsland() {
      return this.getType() == 11;
   }

   public boolean isAmberIsland() {
      return this.getType() == 22;
   }

   public boolean isCelestialIsland() {
      return this.getType() == 12;
   }

   public boolean isFireIsland() {
      return this.getType() == 13;
   }

   public boolean isEtherealIslandWithModifiers() {
      return MSMExtension.getInstance().etherealIslandsWithModifiers.contains(new Integer(this.getType()));
   }

   public boolean isBattleIsland() {
      return this.getType() == 20;
   }

   public boolean isSeasonalIsland() {
      return this.getType() == 21;
   }

   public boolean isMythicalIsland() {
      return this.getType() == 23;
   }

   public boolean isEtherealWorkshopIsland() {
      return this.getType() == 24;
   }

   public Collection<PlayerMonster> getMonsters() {
      return this.monsterMap.values();
   }

   public PlayerMonster getMonsterByID(long id) {
      return (PlayerMonster)this.monsterMap.get(id);
   }

   public void changeMonsterID(long oldId, long newId) {
      PlayerMonster pm = (PlayerMonster)this.monsterMap.remove(oldId);
      if (pm != null && pm.getID() == newId) {
         this.monsterMap.put(newId, pm);
      }

   }

   public void addMonster(PlayerMonster m) throws Exception {
      if (this.monsterMap.containsKey(m.getID())) {
         throw new Exception("Monster " + m.getID() + " already exists\n");
      } else {
         this.monsterMap.put(m.getID(), m);
      }
   }

   public void removeMonster(PlayerMonster m, boolean addToSoldMonsters) {
      PlayerMonster mConfirm = (PlayerMonster)this.monsterMap.remove(m.getID());
      if (mConfirm != null && addToSoldMonsters && !mConfirm.isInactiveBoxMonster()) {
         this.addToSoldMonsters(mConfirm.getType());
      }

   }

   public void addToSoldMonsters(Integer monsterType) {
      if (this.soldMonsterTypes != null) {
         for(int i = 0; i < this.soldMonsterTypes.size(); ++i) {
            if (this.soldMonsterTypes.getInt(i).equals(monsterType)) {
               return;
            }
         }
      } else {
         this.soldMonsterTypes = new SFSArray();
      }

      this.soldMonsterTypes.addInt(monsterType);
   }

   public void addToOwnedCostumes(Integer costumeId) {
      if (costumeId > 0) {
         if (this.allPrevOwnedCostumes_ != null) {
            for(int i = 0; i < this.allPrevOwnedCostumes_.size(); ++i) {
               if (this.allPrevOwnedCostumes_.getInt(i).equals(costumeId)) {
                  return;
               }
            }
         } else {
            this.allPrevOwnedCostumes_ = new SFSArray();
         }

         this.allPrevOwnedCostumes_.addInt(costumeId);
         this.setAllPrevOwnedCostumesDirty(true);
      }
   }

   public Collection<PlayerStructure> getStructures() {
      return this.structureMap.values();
   }

   public PlayerStructure getStructureByID(long id) {
      return (PlayerStructure)this.structureMap.get(id);
   }

   public void addStructure(PlayerStructure s) throws Exception {
      s.setParentID(this.getID());
      this.structureMap.put(s.getID(), s);
      if (s.isNursery()) {
         this.eggHolders.add(s);
         if (this.eggHolders.size() > GameSettings.getInt("USER_MAX_NUM_NURSERIES")) {
            throw new Exception("PlayerIsland::addStructure: too many nurseries added!");
         }
      }

      if (s.isSynthesizer()) {
         this.eggHolders.add(s);
         if (this.eggHolders.size() > GameSettings.getInt("USER_MAX_NUM_SYNTHESIZERS")) {
            throw new Exception("PlayerIsland::addStructure: too many synthesizers added!");
         }
      } else if (s.isBreeding()) {
         this.breedingStructures.add(s);
         if (this.breedingStructures.size() > GameSettings.getInt("USER_MAX_NUM_BREEDING_STRUCTURES")) {
            throw new Exception("PlayerIsland::addStructure: too many breeding structures added!");
         }
      }

   }

   public void removeStructure(PlayerStructure s) {
      s.setParentID(0L);
      this.structureMap.remove(s.getID());
      if (s.canHoldEggs()) {
         this.eggHolders.remove(s);
      } else if (s.isBreeding()) {
         this.breedingStructures.remove(s);
      }

   }

   public boolean hasMax(Structure structure, Player p) {
      if (structure.isMine()) {
         return this.hasStructure(structure.getID());
      } else if (structure.isBreeding()) {
         return this.breedingStructures.size() >= GameSettings.getInt("USER_MAX_NUM_BREEDING_STRUCTURES");
      } else if (structure.isBakery()) {
         return this.numBakeries() >= LevelLookup.get(p.getLevel()).maxBakeries();
      } else if (structure.isNursery()) {
         return this.eggHolders.size() >= GameSettings.getInt("USER_MAX_NUM_NURSERIES");
      } else if (structure.isSynthesizer()) {
         return this.eggHolders.size() >= GameSettings.getInt("USER_MAX_NUM_SYNTHESIZERS");
      } else if (structure.isTorch()) {
         return this.getNumTorchesOnIsland() >= TorchLookup.getMaxTorchesAllowed();
      } else if (structure.isHappyTree()) {
         return this.hasHappinessTree();
      } else {
         Iterator structures;
         if (structure.isTimeMachine()) {
            structures = this.getStructures().iterator();

            while(structures.hasNext()) {
               if (StructureLookup.get(((PlayerStructure)structures.next()).getType()).isTimeMachine()) {
                  return true;
               }
            }
         } else if (structure.isWarehouse()) {
            structures = this.getStructures().iterator();

            while(structures.hasNext()) {
               if (StructureLookup.get(((PlayerStructure)structures.next()).getType()).isWarehouse()) {
                  return true;
               }
            }
         } else if (structure.isHotel()) {
            structures = this.getStructures().iterator();

            while(structures.hasNext()) {
               if (StructureLookup.get(((PlayerStructure)structures.next()).getType()).isHotel()) {
                  return true;
               }
            }
         } else if (structure.isRecordingStudio()) {
            structures = this.getStructures().iterator();

            while(structures.hasNext()) {
               if (StructureLookup.get(((PlayerStructure)structures.next()).getType()).isRecordingStudio()) {
                  return true;
               }
            }
         } else if (structure.isFuzer()) {
            structures = this.getStructures().iterator();

            while(structures.hasNext()) {
               if (StructureLookup.get(((PlayerStructure)structures.next()).getType()).isFuzer()) {
                  return true;
               }
            }
         } else if (structure.isAwakener()) {
            structures = this.getStructures().iterator();

            while(structures.hasNext()) {
               if (StructureLookup.get(((PlayerStructure)structures.next()).getType()).isAwakener()) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public boolean hasHappinessTree() {
      Iterator structures = this.getStructures().iterator();

      PlayerStructure structure;
      do {
         if (!structures.hasNext()) {
            return false;
         }

         structure = (PlayerStructure)structures.next();
      } while(!StructureLookup.get(structure.getType()).isHappyTree());

      return true;
   }

   public boolean buybackCountsAs(int buybackEntityId, Monster monster) {
      if (this.island != 12) {
         return buybackEntityId == monster.getEntityId();
      } else if (buybackEntityId == monster.getEntityId()) {
         return true;
      } else {
         int evolveIntoEnt;
         Monster evolveInto;
         for(evolveIntoEnt = monster.evolveInto(); evolveIntoEnt != 0; evolveIntoEnt = evolveInto.evolveInto()) {
            evolveInto = MonsterLookup.getFromEntityId(evolveIntoEnt);
            if (buybackEntityId == evolveIntoEnt) {
               return true;
            }
         }

         Monster evolvedFrom;
         for(int evolvedFromEnt = monster.evolvedFrom(); evolvedFromEnt != 0; evolvedFromEnt = evolvedFrom.evolvedFrom()) {
            evolvedFrom = MonsterLookup.getFromEntityId(evolvedFromEnt);
            if (buybackEntityId == evolveIntoEnt) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean hasMax(Monster monster) {
      int numOfTypeOnIsland;
      Iterator i;
      PlayerMonster m;
      int evolveIntoEnt;
      if (monster.isWubbox()) {
         boolean rare = monster.isRare();
         boolean epic = monster.isEpic();
         Iterator i = this.monsterMap.entrySet().iterator();

         while(i.hasNext()) {
            PlayerMonster m = (PlayerMonster)((Entry)i.next()).getValue();
            if (m.getType() == monster.getMonsterID() && m.isInactiveBoxMonster(rare, epic)) {
               return true;
            }
         }
      } else if (monster.isDipster()) {
         numOfTypeOnIsland = 0;
         i = this.monsterMap.entrySet().iterator();

         while(i.hasNext()) {
            m = (PlayerMonster)((Entry)i.next()).getValue();
            if (m.getType() == monster.getMonsterID()) {
               ++numOfTypeOnIsland;
            }
         }

         evolveIntoEnt = GameSettings.getInt("USER_MAX_NUM_DIPSTERS_OF_TYPE");
         if (numOfTypeOnIsland >= evolveIntoEnt) {
            return true;
         }
      }

      if (this.island == 10) {
         numOfTypeOnIsland = 0;
         i = this.monsterMap.entrySet().iterator();

         while(i.hasNext()) {
            m = (PlayerMonster)((Entry)i.next()).getValue();
            if (m.getType() == monster.getMonsterID()) {
               ++numOfTypeOnIsland;
            }
         }

         evolveIntoEnt = GameSettings.getInt("USER_MAX_NUM_UNDERLINGS_OF_TYPE");
         if (evolveIntoEnt != 0 && numOfTypeOnIsland >= evolveIntoEnt) {
            return true;
         }
      } else if (this.island == 12) {
         numOfTypeOnIsland = 0;
         i = this.monsterMap.entrySet().iterator();

         while(i.hasNext()) {
            m = (PlayerMonster)((Entry)i.next()).getValue();
            if (m.getType() == monster.getMonsterID()) {
               ++numOfTypeOnIsland;
            }
         }

         int maxOfType;
         Monster evolveInto;
         for(evolveIntoEnt = monster.evolveInto(); evolveIntoEnt != 0; evolveIntoEnt = evolveInto.evolveInto()) {
            evolveInto = MonsterLookup.getFromEntityId(evolveIntoEnt);
            maxOfType = evolveInto.getMonsterID();
            i = this.monsterMap.entrySet().iterator();

            while(i.hasNext()) {
               PlayerMonster m = (PlayerMonster)((Entry)i.next()).getValue();
               if (m.getType() == maxOfType) {
                  ++numOfTypeOnIsland;
               }
            }
         }

         Monster evolvedFrom;
         for(int evolvedFromEnt = monster.evolvedFrom(); evolvedFromEnt != 0; evolvedFromEnt = evolvedFrom.evolvedFrom()) {
            evolvedFrom = MonsterLookup.getFromEntityId(evolvedFromEnt);
            int evolveMid = evolvedFrom.getMonsterID();
            i = this.monsterMap.entrySet().iterator();

            while(i.hasNext()) {
               PlayerMonster m = (PlayerMonster)((Entry)i.next()).getValue();
               if (m.getType() == evolveMid) {
                  ++numOfTypeOnIsland;
               }
            }
         }

         maxOfType = GameSettings.getInt("USER_MAX_NUM_CELESTIALS_OF_TYPE");
         if (maxOfType != 0 && numOfTypeOnIsland >= maxOfType) {
            return true;
         }
      }

      return false;
   }

   public boolean hasStructure(int structureId) {
      Iterator i = this.structureMap.entrySet().iterator();

      PlayerStructure s;
      do {
         if (!i.hasNext()) {
            return false;
         }

         s = (PlayerStructure)((Entry)i.next()).getValue();
      } while(s.getType() != structureId);

      return true;
   }

   public int numStructuresOfEntTypeOnIsland(int entityStructureId) {
      int numStructure = 0;
      Iterator i = this.structureMap.entrySet().iterator();

      while(i.hasNext()) {
         PlayerStructure s = (PlayerStructure)((Entry)i.next()).getValue();
         if (s.getEntityId() == entityStructureId) {
            ++numStructure;
         }
      }

      return numStructure;
   }

   public long getNextStructureId() {
      long nextId = 0L;
      Iterator i = this.structureMap.entrySet().iterator();

      while(i.hasNext()) {
         PlayerStructure s = (PlayerStructure)((Entry)i.next()).getValue();
         if (s.getID() > nextId) {
            nextId = s.getID();
         }
      }

      return ++nextId;
   }

   private double getMonsterBaseBuildTimeForIsland(Monster staticMonster) {
      double monsterBuildTime = (double)staticMonster.getBuildTimeMs();
      if (this.isEtherealIslandWithModifiers() && staticMonster.getGenes().length() == 1) {
         monsterBuildTime *= GameSettings.getDouble("USER_ETHEREAL_BREEDTIME_MULTIPLIER");
      }

      return monsterBuildTime;
   }

   public PlayerBreeding getBreeding(long userBreedingId) {
      if (userBreedingId == 0L) {
         return this.breedingObjects.size() > 0 ? (PlayerBreeding)this.breedingObjects.get(0) : null;
      } else {
         for(int i = 0; i < this.breedingObjects.size(); ++i) {
            PlayerBreeding curBreeding = (PlayerBreeding)this.breedingObjects.get(i);
            if (curBreeding.getID() == userBreedingId) {
               return curBreeding;
            }
         }

         return null;
      }
   }

   public ArrayList<PlayerBreeding> getBreedings() {
      return this.breedingObjects;
   }

   private long getBreedCompletionTimeForIsland(Monster staticMonster, long breedingStructureUid, Player p) {
      float islandThemeBreedMod = this.getIslandThemeModifier("breed_speed_mod", p);
      float speedMod = 1.0F - (1.0F - this.breedingStructureSpeedMod(breedingStructureUid)) - (1.0F - islandThemeBreedMod);
      float buffMod = p.getBuffs().getMultiplier(PlayerBuffs.Buffs.BreedingTimeReduction, this.getType());
      return (long)((double)MSMExtension.CurrentDBTime() + this.getMonsterBaseBuildTimeForIsland(staticMonster) * (double)speedMod * (double)buffMod);
   }

   public PlayerBreeding testBreed(long structureId, Monster monster_1, PlayerMonster playerMonster_1, Monster monster_2, PlayerMonster playerMonster_2, Player player, VersionInfo playerVersion, ISFSObject response) throws Exception {
      this.getBreedResult(monster_1, playerMonster_1, monster_2, playerMonster_2, player, playerVersion, true, response);
      return null;
   }

   public long getAvailableEmptyBreedingStructure() {
      if (this.breedingFull()) {
         return 0L;
      } else {
         for(int i = 0; i < this.breedingStructures.size(); ++i) {
            boolean nurseryHasEgg = false;
            long curNurseryId = ((PlayerStructure)this.breedingStructures.get(i)).getID();

            for(int j = 0; j < this.eggObjects.size(); ++j) {
               if (((PlayerEgg)this.eggObjects.get(j)).structure == curNurseryId) {
                  nurseryHasEgg = true;
                  break;
               }
            }

            if (!nurseryHasEgg) {
               return curNurseryId;
            }
         }

         return 0L;
      }
   }

   public long getBusyBreedingStructure() {
      for(int j = 0; j < this.eggObjects.size(); ++j) {
         if (((PlayerEgg)this.eggObjects.get(j)).structure != 0L) {
            return ((PlayerEgg)this.eggObjects.get(j)).structure;
         }
      }

      return 0L;
   }

   public PlayerBreeding startNewBreed(long structureId, Monster monster_1, PlayerMonster playerMonster_1, Monster monster_2, PlayerMonster playerMonster_2, Player player, VersionInfo playerVersion) throws Exception {
      Monster newMonster = this.getBreedResult(monster_1, playerMonster_1, monster_2, playerMonster_2, player, playerVersion, false, (ISFSObject)null);
      if (newMonster == null) {
         return null;
      } else {
         if (structureId == 0L) {
            structureId = this.getAvailableEmptyBreedingStructure();
            if (structureId == 0L) {
               return null;
            }
         } else {
            for(int j = 0; j < this.breedingObjects.size(); ++j) {
               if (((PlayerBreeding)this.breedingObjects.get(j)).structure == structureId) {
                  return null;
               }
            }
         }

         PlayerBreeding newBreeding = this.makeNewBreeding(structureId, monster_1, monster_2, newMonster, player);
         this.setLastBred(playerMonster_1.getID(), playerMonster_2.getID());
         return newBreeding;
      }
   }

   public PlayerBreeding makeNewBreeding(long structureId, Monster monster_1, Monster monster_2, Monster newMonster, Player p) throws Exception {
      SFSObject newBreedingData = new SFSObject();
      newBreedingData.putLong("user_breeding_id", this.getNextBreedingId());
      newBreedingData.putLong("island", this.getID());
      newBreedingData.putLong("structure", structureId);
      PlayerMonster m;
      if (monster_1 != null) {
         newBreedingData.putInt("monster_1", monster_1.getMonsterID());
      } else {
         m = this.getMonsterByID(this.getLastBredUserMonster1());
         if (m != null) {
            newBreedingData.putInt("monster_1", m.getType());
         }
      }

      if (monster_2 != null) {
         newBreedingData.putInt("monster_2", monster_2.getMonsterID());
      } else {
         m = this.getMonsterByID(this.getLastBredUserMonster2());
         if (m != null) {
            newBreedingData.putInt("monster_2", m.getType());
         }
      }

      newBreedingData.putInt("new_monster", newMonster.getMonsterID());
      newBreedingData.putLong("started_on", MSMExtension.CurrentDBTime());
      Monster staticMonster = MonsterLookup.get(newMonster.getMonsterID());
      newBreedingData.putLong("complete_on", this.getBreedCompletionTimeForIsland(staticMonster, structureId, p));
      PlayerBreeding newBreeding = new PlayerBreeding(newBreedingData);
      this.setBreeding(newBreeding);
      return newBreeding;
   }

   private Monster getBreedResult(Monster monster_1, PlayerMonster playerMonster_1, Monster monster_2, PlayerMonster playerMonster_2, Player player, VersionInfo playerVersion, boolean qaTest, ISFSObject response) {
      Monster newMonster = null;
      if (!qaTest) {
         newMonster = BreedingLookup.getInstance().getBreedingResult(monster_1, monster_2, playerMonster_1.getLevel(), playerMonster_2.getLevel(), player, this, playerVersion);
      } else {
         newMonster = this.testBreeding(monster_1, monster_2, playerMonster_1, playerMonster_2, player, this, playerVersion, response);
      }

      return newMonster;
   }

   private void setBreeding(PlayerBreeding b) throws Exception {
      for(int i = 0; i < this.breedingObjects.size(); ++i) {
         if (((PlayerBreeding)this.breedingObjects.get(i)).getStructureID() == b.getStructureID()) {
            this.breedingObjects.remove(i);
            break;
         }
      }

      this.breedingObjects.add(b);
      if (this.breedingObjects.size() > GameSettings.getInt("USER_MAX_NUM_BREEDING_STRUCTURES")) {
         throw new Exception("too many breedings somehow");
      }
   }

   public long removeBreeding(long userBreedingId) {
      if (userBreedingId == 0L) {
         userBreedingId = this.getBusyBreedingStructure();
         if (userBreedingId == 0L) {
            return 0L;
         }
      }

      for(int i = 0; i < this.breedingObjects.size(); ++i) {
         if (((PlayerBreeding)this.breedingObjects.get(i)).getID() == userBreedingId) {
            this.breedingObjects.remove(i);
            break;
         }
      }

      return userBreedingId;
   }

   public boolean breedingFull() {
      return this.breedingObjects.size() >= this.breedingStructures.size();
   }

   public PlayerEgg finishBreeding(Player p, long userBreedingId, boolean admin) throws Exception {
      PlayerBreeding playerBreeding = this.getBreeding(userBreedingId);
      if (!admin && playerBreeding.getTimeRemaining() > 0L) {
         return null;
      } else {
         this.removeBreeding(userBreedingId);
         int monsterId = playerBreeding.getChildType();
         long availableNurseryId = this.getAvailableEmptyNursery();
         ISFSObject newEggData = this.makeNewISFSEgg(p, monsterId, false, availableNurseryId);
         PlayerEgg newEgg = new PlayerEgg(newEggData);
         ISFSObject costumeData = playerBreeding.getCostumeData();
         if (costumeData != null) {
            newEgg.setCostumeData(costumeData);
         }

         this.setEgg(newEgg);
         return newEgg;
      }
   }

   public PlayerBreeding getAnyValidBreedForFacebookSpeedup(ArrayList<PlayerBreeding> prevSpedUpBreeds) {
      float greatestTimeRemaining = 0.0F;
      PlayerBreeding greatestTimeRemainingBreed = null;

      for(int i = 0; i < this.breedingObjects.size(); ++i) {
         PlayerBreeding curBreed = (PlayerBreeding)this.breedingObjects.get(i);
         if (curBreed.getTimeRemaining() > 0L && !prevSpedUpBreeds.contains(curBreed) && (greatestTimeRemainingBreed == null || greatestTimeRemaining < (float)curBreed.getTimeRemaining())) {
            greatestTimeRemainingBreed = curBreed;
            greatestTimeRemaining = (float)curBreed.getTimeRemaining();
         }
      }

      return greatestTimeRemainingBreed;
   }

   public PlayerEgg getEgg(long userEggId) throws Exception {
      for(int i = 0; i < this.eggObjects.size(); ++i) {
         PlayerEgg curEgg = (PlayerEgg)this.eggObjects.get(i);
         if (curEgg.getID() == userEggId) {
            return curEgg;
         }
      }

      return null;
   }

   public ArrayList<PlayerEgg> getEggs() {
      return this.eggObjects;
   }

   private long getHatchCompletionTimeForIsland(Player p, Monster staticMonster, long nurseryUid) {
      float islandThemeNurseryMod = this.getIslandThemeModifier("nursery_speed_mod", p);
      float timedEventMod = 1.0F;
      PlayerTimedEvents pte = p.getTimedEvents();
      List<TimedEvent> playerEvents = null;
      if (pte != null) {
         playerEvents = pte.currentActiveOnKey(TimedEventType.ReturningUserBonus, 0, 0);
      }

      if (playerEvents != null && playerEvents.size() > 0) {
         ReturningUserBonusEvent bonusEvent = (ReturningUserBonusEvent)((ReturningUserBonusEvent)playerEvents.get(0));
         timedEventMod = bonusEvent.nurserySpeedMod();
      } else {
         List<TimedEvent> events = TimedEventManager.instance().currentActiveOnKey(TimedEventType.ReturningUserBonus, 0, 0);
         if (events != null && events.size() > 0) {
            ReturningUserBonusEvent bonusEvent = (ReturningUserBonusEvent)((ReturningUserBonusEvent)events.get(0));
            timedEventMod = bonusEvent.nurserySpeedMod();
         }
      }

      float buffMod = p.getBuffs().getMultiplier(PlayerBuffs.Buffs.NurseryTimeReduction, this.getType());
      float speedMod = (1.0F - (1.0F - this.nurseryStructureSpeedMod(nurseryUid)) - (1.0F - islandThemeNurseryMod) - (1.0F - timedEventMod)) * buffMod;
      return (long)((double)MSMExtension.CurrentDBTime() + this.getMonsterBaseBuildTimeForIsland(staticMonster) * (double)speedMod);
   }

   public long getBakeryCompletionTimeForIsland(ISFSObject bakingData, Player p) {
      float buffMod = p.getBuffs().getMultiplier(PlayerBuffs.Buffs.BakingTimeReduction, this.getType());
      float speedMod = 1.0F - (1.0F - this.getIslandThemeModifier("bakery_speed_mod", p));
      return (long)((double)MSMExtension.CurrentDBTime() + (double)((float)bakingData.getInt("time") * speedMod * buffMod) * 1000.0D);
   }

   public PlayerEgg addNewEggToIsland(Player p, Monster staticMonster, long structureId, boolean diamondCost, boolean questClaim, boolean teleported, String name, ISFSObject prevPermaMega, ISFSObject costumes, String boxedEggs, boolean admin) throws Exception {
      boolean hatchNow = !teleported && diamondCost || questClaim || admin;
      boolean isEggholder = false;

      for(int i = 0; i < this.eggHolders.size(); ++i) {
         PlayerStructure curStructure = (PlayerStructure)this.eggHolders.get(i);
         if (curStructure.getID() == structureId) {
            isEggholder = true;
            break;
         }
      }

      if (structureId == 0L || !isEggholder) {
         structureId = this.getAvailableEmptyEggHolder();
      }

      ISFSObject newEggData = this.makeNewISFSEgg(p, staticMonster.getMonsterID(), hatchNow, structureId);
      if (!name.isEmpty()) {
         newEggData.putUtfString("previous_name", name);
      }

      if (prevPermaMega != null) {
         newEggData.putSFSObject("prev_permamega", prevPermaMega);
      }

      if (costumes != null) {
         newEggData.putSFSObject("costume", costumes);
      }

      if (boxedEggs != null) {
         newEggData.putUtfString("boxed_eggs", boxedEggs);
      }

      PlayerEgg newEgg = new PlayerEgg(newEggData);
      this.setEgg(newEgg);
      return newEgg;
   }

   public long getAvailableEmptyEggHolder() throws Exception {
      return this.isEtherealWorkshopIsland() ? this.getAvailableEmptySynthesizer() : this.getAvailableEmptyNursery();
   }

   public long getAvailableEmptyNursery() throws Exception {
      if (this.eggObjects.size() >= this.eggHolders.size()) {
         return 0L;
      } else {
         float bestSpeedMod = 1.0F;
         long bestNurseryId = -1L;

         for(int i = 0; i < this.eggHolders.size(); ++i) {
            PlayerStructure curNursery = (PlayerStructure)this.eggHolders.get(i);
            Structure nurseryType = StructureLookup.getFromEntityId(curNursery.getEntityId());
            if (!nurseryType.isNursery()) {
               throw new Exception("structure in nurseries is not a nursery");
            }

            float curSpeedMod = 1.0F;
            if (nurseryType.getExtra().containsKey("speed_mod")) {
               curSpeedMod = nurseryType.getExtra().getFloat("speed_mod");
            }

            long curNurseryId = curNursery.getID();
            boolean nurseryHasEgg = false;

            for(int j = 0; j < this.eggObjects.size(); ++j) {
               if (((PlayerEgg)this.eggObjects.get(j)).structure == curNurseryId) {
                  nurseryHasEgg = true;
                  break;
               }
            }

            if (!nurseryHasEgg && (bestNurseryId == -1L || curSpeedMod < bestSpeedMod)) {
               bestNurseryId = curNurseryId;
               bestSpeedMod = curSpeedMod;
            }
         }

         if (bestNurseryId < 0L) {
            return 0L;
         } else {
            return bestNurseryId;
         }
      }
   }

   public long getAvailableEmptySynthesizer() throws Exception {
      for(int i = 0; i < this.eggHolders.size(); ++i) {
         PlayerStructure synthesizer = (PlayerStructure)this.eggHolders.get(i);
         if (!synthesizer.isSynthesizer()) {
            throw new Exception("structure in eggholders is not a synthesizer");
         }

         boolean hasEgg = false;

         for(int j = 0; j < this.eggObjects.size(); ++j) {
            if (((PlayerEgg)this.eggObjects.get(j)).getStructureID() == synthesizer.getID()) {
               hasEgg = true;
            }
         }

         if (this.getSynthesizingData(synthesizer.getID()) == null && !hasEgg) {
            return synthesizer.getID();
         }
      }

      return 0L;
   }

   public PlayerEgg makeNewScratchWinEgg(Player p, int monsterType, long nurseryStructureId) throws Exception {
      if (nurseryStructureId == 0L) {
         nurseryStructureId = this.getAvailableEmptyNursery();
      }

      if (nurseryStructureId == 0L) {
         return null;
      } else {
         ISFSObject newEggData = this.makeNewISFSEgg(p, monsterType, true, nurseryStructureId);
         PlayerEgg newEgg = new PlayerEgg(newEggData);
         this.setEgg(newEgg);
         return newEgg;
      }
   }

   public PlayerEgg makeNewSynthesizerEgg(Player p, int monsterType, long structureId) throws Exception {
      if (structureId == 0L) {
         structureId = this.getAvailableEmptySynthesizer();
      }

      if (structureId == 0L) {
         return null;
      } else {
         ISFSObject newEggData = this.makeNewISFSEgg(p, monsterType, false, structureId);
         PlayerEgg newEgg = new PlayerEgg(newEggData);
         this.setEgg(newEgg);
         return newEgg;
      }
   }

   private ISFSObject makeNewISFSEgg(Player p, int monsterId, boolean hatchNow, long structureId) throws Exception {
      if (structureId == 0L) {
         throw new Exception("no space, eggsFull should have before entering this function");
      } else {
         ISFSObject newEggData = new SFSObject();
         newEggData.putLong("user_egg_id", this.getNextEggId());
         newEggData.putLong("island", this.getID());
         newEggData.putLong("structure", structureId);
         newEggData.putInt("monster", monsterId);
         long currentTime = MSMExtension.CurrentDBTime();
         newEggData.putLong("laid_on", currentTime);
         if (hatchNow) {
            newEggData.putLong("hatches_on", currentTime);
         } else {
            Monster staticMonster = MonsterLookup.get(monsterId);
            newEggData.putLong("hatches_on", this.getHatchCompletionTimeForIsland(p, staticMonster, structureId));
         }

         return newEggData;
      }
   }

   private void setEgg(PlayerEgg e) throws Exception {
      for(int i = 0; i < this.eggObjects.size(); ++i) {
         if (((PlayerEgg)this.eggObjects.get(i)).getStructureID() == e.getStructureID()) {
            this.eggObjects.remove(i);
            break;
         }
      }

      this.eggObjects.add(e);
      if (this.eggObjects.size() > GameSettings.getInt("USER_MAX_NUM_NURSERIES")) {
         throw new Exception("too many nurseries somehow");
      }
   }

   public void removeEgg(long userEggId) {
      for(int i = 0; i < this.eggObjects.size(); ++i) {
         if (((PlayerEgg)this.eggObjects.get(i)).getID() == userEggId) {
            this.eggObjects.remove(i);
            break;
         }
      }

   }

   public int numNurseries() {
      int numNurseries = 0;

      for(int i = 0; i < this.eggHolders.size(); ++i) {
         if (((PlayerStructure)this.eggHolders.get(i)).isNursery()) {
            ++numNurseries;
         }
      }

      return numNurseries;
   }

   public int numBreedingStructures() {
      return this.breedingStructures.size();
   }

   public int numBakeries() {
      int num = 0;
      Iterator var2 = this.structureMap.values().iterator();

      while(var2.hasNext()) {
         PlayerStructure ps = (PlayerStructure)var2.next();
         if (ps.isBakery()) {
            ++num;
         }
      }

      return num;
   }

   public boolean eggsFull() throws Exception {
      return this.getAvailableEmptyEggHolder() == 0L;
   }

   public boolean eggHolderHasEgg(long structureId) {
      for(int i = 0; i < this.eggObjects.size(); ++i) {
         if (((PlayerEgg)this.eggObjects.get(i)).structure == structureId) {
            return true;
         }
      }

      return false;
   }

   public PlayerEgg getAnyValidEggForFacebookSpeedup(ArrayList<PlayerEgg> exclude) {
      float greatestTimeRemaining = 0.0F;
      PlayerEgg greatestTimeRemainingEgg = null;

      for(int i = 0; i < this.eggObjects.size(); ++i) {
         PlayerEgg curEgg = (PlayerEgg)this.eggObjects.get(i);
         if (curEgg.getTimeRemaining() > 0L && (exclude == null || !exclude.contains(curEgg)) && (greatestTimeRemainingEgg == null || greatestTimeRemaining < (float)curEgg.getTimeRemaining())) {
            greatestTimeRemainingEgg = curEgg;
            greatestTimeRemaining = (float)curEgg.getTimeRemaining();
         }
      }

      return greatestTimeRemainingEgg;
   }

   public Collection<PlayerBaking> getBaking() {
      return this.bakingMap.values();
   }

   public Collection<LitPlayerTorch> getLitTorches() {
      return this.litTorchMap.values();
   }

   public PlayerBaking getBakingByStructureId(long structureId) {
      Iterator i = this.bakingMap.entrySet().iterator();

      PlayerBaking b;
      do {
         if (!i.hasNext()) {
            return null;
         }

         b = (PlayerBaking)((Entry)i.next()).getValue();
      } while(b.getStructureId() != structureId);

      return b;
   }

   public Collection<PlayerFuzeBuddy> getFuzerBuddies() {
      return this.buddyMap.values();
   }

   public PlayerFuzeBuddy getFuzerBuddyByStructureId(long structureId) {
      Iterator i = this.buddyMap.entrySet().iterator();

      PlayerFuzeBuddy b;
      do {
         if (!i.hasNext()) {
            return null;
         }

         b = (PlayerFuzeBuddy)((Entry)i.next()).getValue();
      } while(b.getStructureID() != structureId);

      return b;
   }

   public PlayerEgg getEggByStructureId(long structureId) {
      Iterator i = this.eggObjects.iterator();

      PlayerEgg e;
      do {
         if (!i.hasNext()) {
            return null;
         }

         e = (PlayerEgg)i.next();
      } while(e.getStructureID() != structureId);

      return e;
   }

   public PlayerBreeding getBreedingByStructureId(long structureId) {
      Iterator i = this.breedingObjects.iterator();

      PlayerBreeding e;
      do {
         if (!i.hasNext()) {
            return null;
         }

         e = (PlayerBreeding)i.next();
      } while(e.getStructureID() != structureId);

      return e;
   }

   public PlayerBaking getBakingByID(long id) {
      return (PlayerBaking)this.bakingMap.get(id);
   }

   public void addBaking(PlayerBaking b) {
      b.setParentID(this.getID());
      this.bakingMap.put(b.getID(), b);
      this.lastBakedMap.remove(b.getStructureId());
      this.lastBakedMap.put(b.getStructureId(), b.getData().getInt("food_option_id"));
   }

   public void removeBaking(PlayerBaking b) {
      b.setParentID(0L);
      this.bakingMap.remove(b.getID());
   }

   public long getNextBakingId() {
      long nextId = 0L;
      Iterator i = this.bakingMap.entrySet().iterator();

      while(i.hasNext()) {
         PlayerBaking b = (PlayerBaking)((Entry)i.next()).getValue();
         if (b.getID() > nextId) {
            nextId = b.getID();
         }
      }

      return ++nextId;
   }

   public PlayerCrucibleData getCrucibleData(long structureId) {
      Iterator i = this.crucibleObjects.iterator();

      PlayerCrucibleData c;
      do {
         if (!i.hasNext()) {
            return null;
         }

         c = (PlayerCrucibleData)i.next();
      } while(c.getStructureID() != structureId);

      return c;
   }

   public PlayerAttuningData getAttuningData(long structureId) {
      Iterator i = this.attuningObjects.iterator();

      PlayerAttuningData a;
      do {
         if (!i.hasNext()) {
            return null;
         }

         a = (PlayerAttuningData)i.next();
      } while(a.getStructureID() != structureId);

      return a;
   }

   public PlayerSynthesizingData getSynthesizingData(long structureId) {
      Iterator i = this.synthesizingObjects.iterator();

      PlayerSynthesizingData s;
      do {
         if (!i.hasNext()) {
            return null;
         }

         s = (PlayerSynthesizingData)i.next();
      } while(s.getStructureID() != structureId);

      return s;
   }

   public boolean monsterBeingEvolvedInCrucible(long monsterId) {
      Iterator i = this.crucibleObjects.iterator();

      PlayerCrucibleData c;
      do {
         if (!i.hasNext()) {
            return false;
         }

         c = (PlayerCrucibleData)i.next();
      } while(c.getUserMonsterId() != monsterId);

      return true;
   }

   private ArrayList<PlayerCrucibleData> getCrucibleEvolves() {
      return this.crucibleObjects;
   }

   public PlayerCrucibleData unlockCrucibleStage(long structure, int stage) throws Exception {
      PlayerCrucibleData c = this.getCreateCrucibleData(structure);
      if (c != null) {
         c.setUnlockCrucibleStage(stage);
      }

      return c;
   }

   public PlayerCrucibleData startCrucibleEvolve(long structure, PlayerMonster monster, long startedOn, int heatLevel, int islandType, Player player) throws Exception {
      PlayerCrucibleData c = this.getCreateCrucibleData(structure);
      if (c != null) {
         c.startEvolve(monster, startedOn, heatLevel, islandType, player);
      }

      return c;
   }

   public PlayerCrucibleData testEvolve(long structure, PlayerMonster monster, long startedOn, int heatLevel, boolean flagA, boolean flagB, boolean flagC, boolean flagD, boolean flagE, boolean flagN, int islandType, Player player, ISFSObject response) throws Exception {
      PlayerCrucibleData c = this.getCreateCrucibleData(structure);
      if (c != null) {
         c.testEvolve(monster, startedOn, heatLevel, flagA, flagB, flagC, flagD, flagE, flagN, islandType, player, response);
      }

      return c;
   }

   private PlayerCrucibleData getCreateCrucibleData(long structure) throws Exception {
      PlayerCrucibleData c = this.getCrucibleData(structure);
      if (c == null) {
         PlayerStructure s = (PlayerStructure)this.structureMap.get(structure);
         if (s.isCrucible()) {
            ISFSObject sfs = new SFSObject();
            sfs.putLong("struct", structure);

            try {
               c = new PlayerCrucibleData(sfs);
               this.crucibleObjects.add(c);
            } catch (PlayerLoadingException var7) {
               Logger.trace((Exception)var7, "error in startCrucibleEvolve creating new crucible data");
            }
         }

         c = this.getCrucibleData(structure);
      }

      return c;
   }

   public boolean finishCrucibleEvolve(PlayerCrucibleData c, PlayerMonster m, boolean verify, boolean admin) {
      return !admin && c.getTimeRemaining() > 0L ? false : c.finishEvolve(m, verify, this);
   }

   public boolean canEvolveMoreOfSelectedType(int monsterType) {
      Collection<PlayerMonster> monsters = this.getMonsters();
      Iterator<PlayerMonster> itr = monsters.iterator();
      Monster staticMonster = MonsterLookup.get(monsterType);
      int evolveInto = staticMonster.evolveInto();
      if (evolveInto == 0) {
         return false;
      } else {
         Monster staticEvolveMonster = MonsterLookup.getFromEntityId(evolveInto);
         if (!this.isUnderlingIsland()) {
            return false;
         } else {
            int evolveIntoTypeCount = 0;

            do {
               PlayerMonster nextMonster;
               do {
                  if (!itr.hasNext()) {
                     return true;
                  }

                  nextMonster = (PlayerMonster)itr.next();
               } while(nextMonster.getType() != staticEvolveMonster.getMonsterID() && (nextMonster.getType() != staticMonster.getMonsterID() || !nextMonster.isUnderlingEvolveUnlocked()));

               ++evolveIntoTypeCount;
            } while(evolveIntoTypeCount < GameSettings.getInt("USER_MAX_NUM_RARE_UNDERLINGS_OF_TYPE"));

            return false;
         }
      }
   }

   public PlayerFuzeBuddy getFuzeBuddyByID(long id) {
      return (PlayerFuzeBuddy)this.buddyMap.get(id);
   }

   public void addFuzeBuddy(PlayerFuzeBuddy b) {
      this.buddyMap.put(b.getStructureID(), b);
   }

   public void removeFuzeBuddy(PlayerFuzeBuddy b) {
      this.buddyMap.remove(b.getStructureID());
   }

   public long getNextEggId() {
      long nextId = 0L;
      Iterator i = this.eggObjects.iterator();

      while(i.hasNext()) {
         PlayerEgg e = (PlayerEgg)i.next();
         if (e.getID() > nextId) {
            nextId = e.getID();
         }
      }

      return ++nextId;
   }

   public long getNextBreedingId() {
      long nextId = 0L;
      Iterator i = this.breedingObjects.iterator();

      while(i.hasNext()) {
         PlayerBreeding e = (PlayerBreeding)i.next();
         if (e.getID() > nextId) {
            nextId = e.getID();
         }
      }

      return ++nextId;
   }

   public long getNextLitTorchId() {
      long nextId = 0L;
      Iterator i = this.litTorchMap.entrySet().iterator();

      while(i.hasNext()) {
         LitPlayerTorch t = (LitPlayerTorch)((Entry)i.next()).getValue();
         if (t.getID() > nextId) {
            nextId = t.getID();
         }
      }

      return ++nextId;
   }

   public PlayerBuyback getBuyback() {
      return this.buybackObject;
   }

   public void setBuyback(PlayerBuyback newBuyback) {
      this.buybackObject = newBuyback;
   }

   public boolean hasBuyback() {
      return this.buybackObject != null;
   }

   public void removeBuyback() {
      this.buybackObject = null;
   }

   public long getLastBredUserMonster1() {
      return this.lastBredUserMonster1;
   }

   public long getLastBredUserMonster2() {
      return this.lastBredUserMonster2;
   }

   public void setLastBred(long monster1, long monster2) {
      this.lastBredUserMonster1 = monster1;
      this.lastBredUserMonster2 = monster2;
   }

   public ISFSObject lastBredToSFSObject() {
      if (this.lastBredUserMonster1 != 0L && this.lastBredUserMonster2 != 0L) {
         ISFSObject lastBredObject = new SFSObject();
         lastBredObject.putLong("user_monster_1", this.lastBredUserMonster1);
         lastBredObject.putLong("user_monster_2", this.lastBredUserMonster2);
         return lastBredObject;
      } else {
         return null;
      }
   }

   public Integer getLastBaked(Long bakeryId) {
      return (Integer)this.lastBakedMap.get(bakeryId);
   }

   public ISFSObject lastBakedToSFSObject(Long bakeryId) {
      if (this.getLastBaked(bakeryId) != null) {
         ISFSObject lastBakedObject = new SFSObject();
         lastBakedObject.putLong("bakery", bakeryId);
         lastBakedObject.putInt("food", this.getLastBaked(bakeryId));
         return lastBakedObject;
      } else {
         return null;
      }
   }

   public boolean canPlace(int id, int posX, int posY) {
      return true;
   }

   public boolean hasMonster(int monsterId) {
      Iterator i = this.monsterMap.entrySet().iterator();

      PlayerMonster s;
      do {
         if (!i.hasNext()) {
            return false;
         }

         s = (PlayerMonster)((Entry)i.next()).getValue();
      } while(s.getType() != monsterId);

      return true;
   }

   public PlayerStructure getWarehouse() {
      Iterator structures = this.getStructures().iterator();

      PlayerStructure structure;
      do {
         if (!structures.hasNext()) {
            return null;
         }

         structure = (PlayerStructure)structures.next();
      } while(!StructureLookup.get(structure.getType()).isWarehouse());

      return structure;
   }

   public PlayerStructure getHotel() {
      Iterator structures = this.getStructures().iterator();

      PlayerStructure structure;
      do {
         if (!structures.hasNext()) {
            return null;
         }

         structure = (PlayerStructure)structures.next();
      } while(!StructureLookup.get(structure.getType()).isHotel());

      return structure;
   }

   public int getNumDecorationsInStorage() {
      int count = 0;
      Iterator structures = this.getStructures().iterator();

      while(structures.hasNext()) {
         PlayerStructure structure = (PlayerStructure)structures.next();
         if (structure.inWarehouse()) {
            ++count;
         }
      }

      return count;
   }

   public PlayerStructure getFuzer() {
      Iterator structures = this.getStructures().iterator();

      PlayerStructure structure;
      do {
         if (!structures.hasNext()) {
            return null;
         }

         structure = (PlayerStructure)structures.next();
      } while(!StructureLookup.get(structure.getType()).isFuzer());

      return structure;
   }

   public PlayerStructure getBattleGym() {
      Iterator structures = this.getStructures().iterator();

      PlayerStructure structure;
      do {
         if (!structures.hasNext()) {
            return null;
         }

         structure = (PlayerStructure)structures.next();
      } while(!StructureLookup.get(structure.getType()).isBattleGym());

      return structure;
   }

   public int getNumMonstersInGym() {
      int count = 0;
      Iterator monsters = this.getMonsters().iterator();

      while(monsters.hasNext()) {
         PlayerMonster monster = (PlayerMonster)monsters.next();
         if (monster.isTraining()) {
            ++count;
         }
      }

      return count;
   }

   public int getNumBuddiesInFuzer() {
      int count = 0;
      Iterator structures = this.getStructures().iterator();

      while(structures.hasNext()) {
         PlayerStructure structure = (PlayerStructure)structures.next();
         if (structure.inFuzer()) {
            ++count;
         }
      }

      return count;
   }

   public int getNumMonstersInStorage() {
      int count = 0;
      Iterator monsters = this.getMonsters().iterator();

      while(monsters.hasNext()) {
         PlayerMonster monster = (PlayerMonster)monsters.next();
         if (monster.inHotel()) {
            ++count;
         }
      }

      return count;
   }

   public int getNumMonsterBedsInStorage() {
      int count = 0;
      Iterator monsters = this.getMonsters().iterator();

      while(monsters.hasNext()) {
         PlayerMonster monster = (PlayerMonster)monsters.next();
         if (monster.inHotel()) {
            count += MonsterLookup.get(monster.getType()).beds();
         }
      }

      return count;
   }

   public int bedsAvailable() {
      if (this.island != 10 && this.island != 12 && this.island != 20) {
         Collection<PlayerMonster> monsters = this.getMonsters();
         int bedsUsed = 0;
         Iterator var3 = monsters.iterator();

         while(var3.hasNext()) {
            PlayerMonster monster = (PlayerMonster)var3.next();
            if (!monster.inHotel()) {
               bedsUsed += MonsterLookup.get(monster.getType()).beds();
            }
         }

         int beds = 0;
         Iterator var8 = this.getStructures().iterator();

         while(var8.hasNext()) {
            PlayerStructure playerStructure = (PlayerStructure)var8.next();
            Structure s = StructureLookup.get(playerStructure.getType());
            if (s.isCastle()) {
               beds += s.getExtra().getInt("beds");
               break;
            }
         }

         return beds - bedsUsed;
      } else {
         return -1;
      }
   }

   public PlayerStructure getMine() {
      Iterator var1 = this.getStructures().iterator();

      PlayerStructure playerStructure;
      Structure s;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         playerStructure = (PlayerStructure)var1.next();
         s = StructureLookup.get(playerStructure.getType());
      } while(!s.isMine());

      return playerStructure;
   }

   public float nurseryStructureSpeedMod(long nurseryUid) {
      PlayerStructure ps = this.getStructureByID(nurseryUid);
      Structure s = StructureLookup.get(ps.getType());
      if (s.isNursery()) {
         return s.getExtra().containsKey("speed_mod") ? s.getExtra().getFloat("speed_mod") : 1.0F;
      } else {
         return 1.0F;
      }
   }

   public float breedingStructureSpeedMod(long breedingStructUid) {
      PlayerStructure ps = this.getStructureByID(breedingStructUid);
      Structure s = StructureLookup.get(ps.getType());
      if (s.isBreeding()) {
         return s.getExtra().containsKey("speed_mod") ? s.getExtra().getFloat("speed_mod") : 1.0F;
      } else {
         return 1.0F;
      }
   }

   public LitPlayerTorch getLitTorchData(PlayerStructure torchStruct) {
      return this.getLitTorchData(torchStruct.getID());
   }

   public LitPlayerTorch getLitTorchData(Long playerStructureId) {
      LitPlayerTorch litTorch = null;
      if (this.litTorchMap.containsKey(playerStructureId)) {
         litTorch = (LitPlayerTorch)this.litTorchMap.get(playerStructureId);
      }

      return litTorch;
   }

   public void removeLitTorch(PlayerStructure torchStruct) {
      this.litTorchMap.remove(torchStruct.getID());
   }

   public LitPlayerTorch lightTorch(PlayerStructure torchStruct, boolean permanent) {
      LitPlayerTorch litTorch = this.getLitTorchData(torchStruct);
      if (litTorch == null) {
         litTorch = new LitPlayerTorch(torchStruct, this, permanent);
         this.litTorchMap.put(torchStruct.getID(), litTorch);
      } else {
         litTorch.resetTorchLight(permanent);
      }

      return litTorch;
   }

   public void clearOutOldTorchData() {
      long curTime = MSMExtension.CurrentDBTime();
      Iterator i = this.litTorchMap.values().iterator();

      while(i.hasNext()) {
         LitPlayerTorch torch = (LitPlayerTorch)i.next();
         if (this.torchExpired(torch, curTime)) {
            i.remove();
         }
      }

   }

   public boolean torchExpired(LitPlayerTorch torch, long curTime) {
      return torch.torchExpired(curTime);
   }

   public int getNumLitTorchesOnIsland() {
      int numLitTorches = 0;
      long curTime = MSMExtension.CurrentDBTime();
      Iterator i = this.litTorchMap.values().iterator();

      while(i.hasNext()) {
         LitPlayerTorch torch = (LitPlayerTorch)i.next();
         if (!this.torchExpired(torch, curTime)) {
            ++numLitTorches;
         }
      }

      numLitTorches = Math.min(numLitTorches, TorchLookup.getMaxTorchesAllowed());
      return numLitTorches;
   }

   public int getNumTorchesOnIsland() {
      int numTorches = 0;
      Iterator var2 = this.structureMap.values().iterator();

      while(var2.hasNext()) {
         PlayerStructure ps = (PlayerStructure)var2.next();
         if (ps.isTorch()) {
            ++numTorches;
         }
      }

      numTorches = Math.min(numTorches, TorchLookup.getMaxTorchesAllowed());
      return numTorches;
   }

   public boolean isALastBredMonster(long monsterId) {
      return monsterId == this.getLastBredUserMonster1() || monsterId == this.getLastBredUserMonster2();
   }

   private Monster testBreeding(Monster monster_1, Monster monster_2, PlayerMonster playerMonster_1, PlayerMonster playerMonster_2, Player player, PlayerIsland island, VersionInfo playerVersion, ISFSObject response) {
      Monster newMonster = null;
      HashMap<Integer, Integer> listOfCombos = new HashMap();
      int numRuns = 10000;

      Iterator var14;
      Entry combo;
      for(int i = 0; i < numRuns; ++i) {
         newMonster = BreedingLookup.getInstance().getBreedingResult(monster_1, monster_2, playerMonster_1.getLevel(), playerMonster_2.getLevel(), player, island, playerVersion);
         if (newMonster == null) {
            break;
         }

         boolean foundKey = false;
         var14 = listOfCombos.entrySet().iterator();

         while(var14.hasNext()) {
            combo = (Entry)var14.next();
            if ((Integer)combo.getKey() == newMonster.getMonsterID()) {
               combo.setValue((Integer)combo.getValue() + 1);
               foundKey = true;
               break;
            }
         }

         if (!foundKey) {
            listOfCombos.put(newMonster.getMonsterID(), 1);
         }
      }

      float percentageDivisor = (float)numRuns / 100.0F;
      SFSArray arr = new SFSArray();
      Logger.trace("Combo probabilities are: ");
      var14 = listOfCombos.entrySet().iterator();

      while(var14.hasNext()) {
         combo = (Entry)var14.next();
         int resultMonsterId = (Integer)combo.getKey();
         float percentProbability = (float)(Integer)combo.getValue() / percentageDivisor;
         Logger.trace("Chance of getting monster " + resultMonsterId + " is " + percentProbability);
         SFSObject curProbability = new SFSObject();
         curProbability.putInt("resultMonsterId", resultMonsterId);
         curProbability.putFloat("probability", percentProbability);
         arr.addSFSObject(curProbability);
      }

      response.putSFSArray("probabilities", arr);
      return newMonster;
   }

   public boolean hasInactiveBoxMonster(boolean rare, boolean epic) {
      Collection<PlayerMonster> monsters = this.monsterMap.values();
      Iterator var4 = monsters.iterator();

      PlayerMonster monster;
      do {
         if (!var4.hasNext()) {
            for(int i = 0; i < this.eggObjects.size(); ++i) {
               PlayerEgg e = (PlayerEgg)this.eggObjects.get(i);
               int monsterId = e.getType();
               Monster monster = MonsterLookup.get(monsterId);
               if (monster.isBoxMonsterType()) {
                  if (rare) {
                     return monster.isRare();
                  }

                  if (epic) {
                     return monster.isEpic();
                  }

                  return monster.isCommon();
               }
            }

            return false;
         }

         monster = (PlayerMonster)var4.next();
      } while(!monster.isInactiveBoxMonster(rare, epic));

      return true;
   }

   public float getIslandThemeModifier(String modifierKey, Player p) {
      float mod = 1.0F;
      if (this.islandThemeModifierMap.containsKey(modifierKey)) {
         mod = (Float)this.islandThemeModifierMap.get(modifierKey);
      }

      Iterator itr = IslandThemeLookup.getInstance().entries().iterator();

      while(itr.hasNext()) {
         IslandTheme theme = (IslandTheme)itr.next();
         if (theme.getIsland() == this.island && !theme.getViewInMarket()) {
            if (TimedEventManager.instance().hasTimedEventNow(TimedEventType.IslandThemeAvailability, theme.getId(), this.island) && !p.ownsIslandTheme(theme.getId())) {
               ISFSObject modifiers = theme.getModifiers();
               if (modifiers.containsKey(modifierKey)) {
                  mod *= modifiers.getFloat(modifierKey);
               }
            }
            break;
         }
      }

      return mod;
   }

   public int getIslandThemeTrial() {
      return this.islandThemeTrial;
   }

   public void setIslandThemeTrial(int islandThemeId) {
      this.islandThemeTrial = islandThemeId;
   }

   public IslandCostumeState getCostumeState() {
      return this.costumeState_;
   }

   public void initCostumeState(ISFSObject costumeIslandData) {
      this.costumeState_ = new IslandCostumeState(costumeIslandData);
   }

   public BattleIslandState getBattleIslandState() {
      return this.battleIslandState_;
   }

   public void initBattleIslandState(ISFSObject battleIslandData) {
      this.battleIslandState_ = new BattleIslandState(battleIslandData);
   }

   public void collectAll(Player player, User sender, GameStateHandler handler, SFSArray collectResponses, SFSArray monsterUpdateResponses) {
      Iterator it = this.monsterMap.values().iterator();

      while(it.hasNext()) {
         PlayerMonster m = (PlayerMonster)it.next();
         if (!m.inHotel() && !m.isInactiveBoxMonster()) {
            ISFSObject collectResponse = new SFSObject();
            ISFSObject monsterUpdateResponse = new SFSObject();
            if (this.canStandardCollect(m)) {
               boolean success = this.standardCollectFromMonster(m, player, sender, handler, collectResponse, monsterUpdateResponse, true);
               if (success) {
                  collectResponses.addSFSObject(collectResponse);
                  monsterUpdateResponses.addSFSObject(monsterUpdateResponse);
               }
            }
         }
      }

   }

   public boolean canStandardCollect(PlayerMonster playerMonster) {
      if (playerMonster.isInactiveBoxMonster()) {
         return false;
      } else if (this.isAmberIsland() && this.monsterBeingEvolvedInCrucible(playerMonster.getID())) {
         return false;
      } else if (playerMonster.isEvolvable() && playerMonster.evolveExpired(this.getType())) {
         return false;
      } else {
         return !this.isRandomCollectionIsland() || playerMonster.CurrentCollectionType() != PlayerMonster.UnderlingCollectionType.MaxCollectionTypes;
      }
   }

   public boolean isExpiryCollection(PlayerMonster playerMonster) {
      return this.isZapIsland() && (playerMonster.isInactiveBoxMonster() || playerMonster.evolveExpired(this.getType()));
   }

   public boolean collectExpiryFromMonster(PlayerMonster playerMonster, Player player, User sender, GameStateHandler handler, ISFSObject collectResponse, ISFSObject monsterUpdateResponse, boolean collectAll) {
      collectResponse.putLong("user_monster_id", playerMonster.getID());
      int collectedCoins;
      if (this.isAmberIsland()) {
         collectedCoins = (int)((float)MonsterLookup.get(playerMonster.getType()).getCostRelics(this.getType()) * GameSettings.getFloat("USER_VESSEL_RELIC_TRADE_PERCENT"));
         player.adjustRelics(sender, handler, collectedCoins);
         collectResponse.putInt(PlayerMonster.getCurrencyStrFromType(PlayerMonster.UnderlingCollectionType.Relics), collectedCoins);
      } else {
         if (collectAll) {
            return false;
         }

         collectedCoins = playerMonster.collectionOfDyingFetuses(this, player);
         player.adjustCoins(sender, handler, collectedCoins);
         collectResponse.putInt(PlayerMonster.getCurrencyStrFromType(PlayerMonster.UnderlingCollectionType.Coins), collectedCoins);
         monsterUpdateResponse.putLong("user_monster_id", playerMonster.getID());
         if (playerMonster.isInactiveBoxMonster()) {
            monsterUpdateResponse.putUtfString("boxed_eggs", playerMonster.boxedEggs().toJson());
         }

         if (MonsterLookup.get(playerMonster.getType()).isEvolvable()) {
            monsterUpdateResponse.putUtfString("has_evolve_reqs", playerMonster.evolveReqsMetStatic().toJson());
            monsterUpdateResponse.putUtfString("has_evolve_flexeggs", playerMonster.evolveReqsMetFlex().toJson());
         }

         monsterUpdateResponse.putInt("collected_coins", playerMonster.getCollectedCoins());
         monsterUpdateResponse.putInt("collected_ethereal", playerMonster.getCollectedEth());
         monsterUpdateResponse.putInt("collected_diamonds", playerMonster.getCollectedDiamonds());
         monsterUpdateResponse.putInt("collected_food", playerMonster.getCollectedFood());
         monsterUpdateResponse.putInt("collected_starpower", playerMonster.getCollectedStarpower());
         monsterUpdateResponse.putInt("collected_keys", playerMonster.getCollectedKeys());
         monsterUpdateResponse.putDouble("collected_relics", (double)playerMonster.getCollectedRelics());
         monsterUpdateResponse.putLong("last_collection", playerMonster.getLastCollectedTime());
         monsterUpdateResponse.putLong("egg_timer_start", playerMonster.eggTimerStart());
      }

      return true;
   }

   public boolean standardCollectFromMonster(PlayerMonster playerMonster, Player player, User sender, GameStateHandler handler, ISFSObject collectResponse, ISFSObject monsterUpdateResponse, boolean collectAll) {
      boolean success = false;
      MSMExtension ext = MSMExtension.getInstance();
      collectResponse.putLong("user_monster_id", playerMonster.getID());
      if (this.isRandomCollectionIsland()) {
         PlayerMonster.UnderlingCollectionType collectionType = playerMonster.CurrentCollectionType();
         if (collectionType == PlayerMonster.UnderlingCollectionType.MaxCollectionTypes) {
            collectResponse.putUtfString("message", "invalid currency collection type");
            return false;
         }

         int reward = playerMonster.collectCurrency(player, this);
         String currency = PlayerMonster.getCurrencyStrFromType(collectionType);
         collectResponse.putInt(currency, reward);
         if (reward <= 0) {
            collectResponse.putUtfString("message", "Zap monster: nothing to collect");
            return false;
         }

         switch(collectionType) {
         case Diamonds:
            player.adjustDiamonds(sender, handler, reward);
            if (this.isUnderlingIsland()) {
               ext.stats.trackReward(sender, "underling_monster_collect", "diamonds", (long)reward);
            } else {
               ext.stats.trackReward(sender, "celestial_monster_collect", "diamonds", (long)reward);
            }
            break;
         case Shards:
            player.adjustEthCurrency(sender, handler, reward);
            break;
         case Starpower:
            player.adjustStarpower(sender, handler, (long)reward);
            break;
         case Food:
            player.adjustFood(sender, handler, reward);
            break;
         case Coins:
            player.adjustCoins(sender, handler, reward);
            break;
         case Keys:
            player.adjustKeys(sender, handler, reward);
         }

         monsterUpdateResponse.putInt("collected_coins", playerMonster.getCollectedCoins());
         monsterUpdateResponse.putInt("collected_ethereal", playerMonster.getCollectedEth());
         monsterUpdateResponse.putInt("collected_diamonds", playerMonster.getCollectedDiamonds());
         monsterUpdateResponse.putInt("collected_food", playerMonster.getCollectedFood());
         monsterUpdateResponse.putInt("collected_starpower", playerMonster.getCollectedStarpower());
         monsterUpdateResponse.putInt("collected_keys", playerMonster.getCollectedKeys());
         monsterUpdateResponse.putDouble("collected_relics", (double)playerMonster.getCollectedRelics());
         monsterUpdateResponse.putUtfString("collection_type", PlayerMonster.getCurrencyStrFromType(playerMonster.CurrentCollectionType()));
         monsterUpdateResponse.putInt("random_underling_collection_min", playerMonster.getRandomUnderlingCollectionMin());
         if (playerMonster.debugFasterCurrencyGen()) {
            monsterUpdateResponse.putInt("debug_underling_collection_time_modifier", playerMonster.debugUnderlingCollectionTimeModifier());
         }

         monsterUpdateResponse.putLong("user_monster_id", playerMonster.getID());
         monsterUpdateResponse.putLong("last_collection", playerMonster.getLastCollectedTime());
         success = true;
      } else {
         playerMonster.collectSpecialFromMonster(sender, collectResponse, player, handler);
         int reward = playerMonster.collectCurrency(player, this);
         collectResponse.putInt(this.isEtherealIsland() ? PlayerMonster.getCurrencyStrFromType(PlayerMonster.UnderlingCollectionType.Shards) : (this.isAmberIsland() ? PlayerMonster.getCurrencyStrFromType(PlayerMonster.UnderlingCollectionType.Relics) : PlayerMonster.getCurrencyStrFromType(PlayerMonster.UnderlingCollectionType.Coins)), reward);
         if (reward > 0) {
            if (this.isEtherealIsland()) {
               player.adjustEthCurrency(sender, handler, reward);
               monsterUpdateResponse.putInt("collected_ethereal", playerMonster.getCollectedEth());
            } else if (this.isAmberIsland()) {
               player.adjustRelics(sender, handler, reward);
               monsterUpdateResponse.putDouble("collected_relics", (double)playerMonster.getCollectedRelics());
            } else {
               handler.serverQuestEvent(sender, "collect_coins", 1);
               player.adjustCoins(sender, handler, reward);
               monsterUpdateResponse.putInt("collected_coins", playerMonster.getCollectedCoins());
            }

            success = true;
            monsterUpdateResponse.putLong("user_monster_id", playerMonster.getID());
            monsterUpdateResponse.putLong("last_collection", playerMonster.getLastCollectedTime());
         } else {
            collectResponse.putUtfString("message", "Normal monster: nothing to collect");
            success = false;
         }
      }

      return success;
   }

   public void testCollectFromMonster(PlayerMonster playerMonster, Player player, User sender, GameStateHandler handler, ISFSObject response) throws Exception {
      HashMap<String, ArrayList<Integer>> collected = new HashMap();
      int numRuns = 10000;

      for(int i = 0; i < numRuns; ++i) {
         playerMonster.TESTMaxCollectTime();
         PlayerMonster.UnderlingCollectionType collectionType = playerMonster.CurrentCollectionType();
         if (collectionType == PlayerMonster.UnderlingCollectionType.MaxCollectionTypes) {
            throw new Exception("error with testing collection, invalid collection type");
         }

         int reward = playerMonster.collectCurrency(player, this);
         String currency = PlayerMonster.getCurrencyStrFromType(collectionType);
         if (!collected.containsKey(currency)) {
            collected.put(currency, new ArrayList());
         }

         ((ArrayList)collected.get(currency)).add(reward);
      }

      float percentageDivisor = (float)numRuns / 100.0F;
      SFSArray arr = new SFSArray();
      Logger.trace("Combo probabilities are: ");
      Iterator var19 = collected.entrySet().iterator();

      while(var19.hasNext()) {
         Entry<String, ArrayList<Integer>> combo = (Entry)var19.next();
         String currency = (String)combo.getKey();
         ArrayList<Integer> amounts = (ArrayList)combo.getValue();
         float percentProbability = (float)amounts.size() / percentageDivisor;
         float avgAmt = 0.0F;

         for(int i = 0; i < amounts.size(); ++i) {
            avgAmt += (float)(Integer)amounts.get(i);
         }

         avgAmt /= (float)amounts.size();
         Logger.trace("Chance of getting currency " + currency + " is " + percentProbability + " (avg amt: " + avgAmt + ")");
         SFSObject curProbability = new SFSObject();
         curProbability.putUtfString("currency", currency);
         curProbability.putFloat("probability", percentProbability);
         curProbability.putFloat("avgAmt", avgAmt);
         arr.addSFSObject(curProbability);
      }

      response.putSFSArray("probabilities", arr);
   }

   public PlayerStructure getAwakener() {
      Iterator structures = this.getStructures().iterator();

      PlayerStructure structure;
      do {
         if (!structures.hasNext()) {
            return null;
         }

         structure = (PlayerStructure)structures.next();
      } while(!StructureLookup.get(structure.getType()).isAwakener());

      return structure;
   }

   private ArrayList<PlayerAttuningData> getAttuningData() {
      return this.attuningObjects;
   }

   public PlayerAttuningData startAttuning(long structure, String startGene, String endGene, long startedOn, long endOn, Player player) throws Exception {
      PlayerStructure s = (PlayerStructure)this.structureMap.get(structure);
      if (s.isAttuner()) {
         PlayerAttuningData a = new PlayerAttuningData(structure, startGene, endGene, startedOn, endOn);
         this.attuningObjects.add(a);
         if (!startGene.isEmpty()) {
            int count = this.attunedCritterCounts.containsKey(a.startGene) ? (Integer)this.attunedCritterCounts.get(a.startGene) : 0;
            this.attunedCritterCounts.put(a.startGene, count - 1);
         }

         return a;
      } else {
         return null;
      }
   }

   public boolean finishAttuning(long structure, boolean admin) {
      PlayerAttuningData a = this.getAttuningData(structure);
      if (!admin && a.getTimeRemaining() > 0L) {
         return false;
      } else {
         this.attuningObjects.remove(a);
         return this.addAttunedCritters(a.endGene, 1);
      }
   }

   public boolean hasAvailableCritter(String gene) {
      if (!this.isEtherealWorkshopIsland()) {
         return false;
      } else if (gene.isEmpty()) {
         return this.availableCritters() > 0;
      } else {
         return (Integer)this.attunedCritterCounts.get(gene) > 0;
      }
   }

   public int numCritters() {
      PlayerStructure pStruct = this.getCastle();
      Structure staticStruct = StructureLookup.get(pStruct.getType());
      return staticStruct.getExtra().getInt("critters");
   }

   public int numAttunedCritters() {
      int count = 0;

      Entry entry;
      for(Iterator var2 = this.attunedCritterCounts.entrySet().iterator(); var2.hasNext(); count += (Integer)entry.getValue()) {
         entry = (Entry)var2.next();
      }

      return count;
   }

   public int numAttunedCritters(String gene) {
      return this.attunedCritterCounts.containsKey(gene) ? (Integer)this.attunedCritterCounts.get(gene) : 0;
   }

   public boolean addAttunedCritters(String gene, int num) {
      if (!PlayerAttuningData.isValidGene(gene)) {
         Logger.trace("ERROR: not a valid gene ");
         return false;
      } else if (this.availableCritters() < num) {
         Logger.trace("ERROR: no critters available to attune ");
         return false;
      } else {
         int count = this.attunedCritterCounts.containsKey(gene) ? (Integer)this.attunedCritterCounts.get(gene) : 0;
         this.attunedCritterCounts.put(gene, count + num);
         return true;
      }
   }

   public void removeAttunedCritters(String gene, int num) {
      int count = this.attunedCritterCounts.containsKey(gene) ? (Integer)this.attunedCritterCounts.get(gene) : 0;
      if (count >= num) {
         this.attunedCritterCounts.put(gene, count - num);
      } else {
         Logger.trace("ERROR: not enough attuned critter has " + count + " requires " + num);
      }

   }

   public int availableCritters() {
      int numCritters = this.numCritters();
      int numAttunedCritters = this.numAttunedCritters();
      int numAttuningCritters = this.attuningObjects.size();
      int numSynthesizingCritters = this.numSynthesizingCritters();
      int availableCritters = numCritters - numAttunedCritters - numAttuningCritters - numSynthesizingCritters;
      return availableCritters;
   }

   public PlayerStructure getCastle() {
      Iterator structures = this.getStructures().iterator();

      PlayerStructure structure;
      do {
         if (!structures.hasNext()) {
            return null;
         }

         structure = (PlayerStructure)structures.next();
      } while(!StructureLookup.get(structure.getType()).isCastle());

      return structure;
   }

   public PlayerStructure getSynthesizer() {
      Iterator structures = this.getStructures().iterator();

      PlayerStructure structure;
      do {
         if (!structures.hasNext()) {
            return null;
         }

         structure = (PlayerStructure)structures.next();
      } while(!StructureLookup.get(structure.getType()).isSynthesizer());

      return structure;
   }

   private ArrayList<PlayerSynthesizingData> getSynthesizingData() {
      return this.synthesizingObjects;
   }

   public void addSynthesizingData(PlayerSynthesizingData data) {
      this.synthesizingObjects.add(data);
   }

   public void removeSynthesizingData(PlayerSynthesizingData data) {
      this.synthesizingObjects.remove(data);
   }

   public int numSynthesizingCritters() {
      int total = 0;

      for(Iterator i = this.synthesizingObjects.iterator(); i.hasNext(); total += ((PlayerSynthesizingData)i.next()).getUsedCritters()) {
      }

      return total;
   }
}
