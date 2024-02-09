package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.bigbluebubble.mysingingmonsters.player.PlayerMonster;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Monster extends Entity {
   public static final String MONSTER_ID_KEY = "monster_id";
   protected static final String HAPPINESS_KEY = "happiness";
   protected static final String GENES_KEY = "genes";
   protected static final String BOX_MONSTER_REQUIREMENTS_KEY = "box_monster_requirements";
   protected static final String NAMES_KEY = "names";
   public static final String COINS_KEY = "coins";
   public static final String ETHEREAL_RATE_KEY = "ethereal_currency";
   public static final String RELIC_RATE_KEY = "relics";
   public static final String MAX_COINS_KEY = "max_coins";
   public static final String MAX_ETHEREAL_KEY = "max_ethereal";
   public static final String MAX_RELIC_KEY = "max_relics";
   protected static final String BEDS_KEY = "beds";
   protected static final String UPGRADES_TO = "levelup_island";
   protected static final String EVOLVE_REQS_NEEDED_KEY = "evolve_requirements";
   protected static final String EVOLVE_INTO_KEY = "evolve_into";
   protected static final String EVOLVE_ENABLED_KEY = "evolve_enabled";
   protected static final String EVOLVE_KEY_COST_KEY = "crucible_access_key_cost";
   protected static final String EVOLVE_REQ_FLEXEGGS_KEY = "evolve_req_flexeggs";
   protected static final String EVOLVE_GFX_KEY = "evolve_gfx";
   protected static final String EVOLVE_SFX_KEY = "evolve_sfx";
   protected ConcurrentHashMap<Integer, ISFSObject> levelMap;
   protected ISFSArray likes;
   protected SFSArray dislikes;
   protected String genes;
   protected ISFSArray names;
   protected ISFSArray boxMonsterRequirements;
   protected static ISFSArray goldBoxMonsterRequirements = null;
   protected static ISFSArray goldRareBoxMonsterRequirements = null;
   protected static ISFSArray goldEpicBoxMonsterRequirements = null;
   int timeToFillSec;
   protected ISFSArray evolveRequirements;
   protected ISFSArray evolveReqFlexEggs;
   protected int evolveInto;
   protected boolean evolveEnabled;
   protected int evolveKeyCost;

   public Monster(ISFSObject monsterData) {
      this(monsterData, (ISFSArray)null);
   }

   public Monster(ISFSObject monsterData, ISFSArray levelData) {
      super(monsterData);
      this.levelMap = new ConcurrentHashMap();
      this.boxMonsterRequirements = null;
      this.timeToFillSec = -1;
      this.evolveRequirements = null;
      this.evolveReqFlexEggs = null;
      this.evolveInto = 0;
      this.evolveEnabled = false;
      this.evolveKeyCost = 0;
      this.requirements = new SFSArray();
      this.likes = new SFSArray();
      this.dislikes = new SFSArray();
      String requirementString = this.data.getUtfString("requirements");
      if (requirementString != null && requirementString.length() > 0) {
         this.requirements = SFSArray.newFromJsonData(requirementString);
         this.data.putSFSArray("requirements", this.requirements);
      }

      this.genes = this.data.getUtfString("genes");
      if (levelData != null) {
         Iterator it = levelData.iterator();

         while(it.hasNext()) {
            SFSObject ld = (SFSObject)((SFSObject)((SFSDataWrapper)it.next()).getObject());
            this.levelMap.put(ld.getInt("level"), ld);
         }

         if (levelData != null) {
            SFSArray onlyTheNecessary = new SFSArray();

            for(int i = 0; i < levelData.size(); ++i) {
               SFSObject levelObj = new SFSObject();
               ISFSObject curLevel = levelData.getSFSObject(i);
               levelObj.putInt("level", curLevel.getInt("level"));
               levelObj.putInt("food", curLevel.getInt("food"));
               if (curLevel.containsKey("coins")) {
                  levelObj.putInt("coins", curLevel.getInt("coins"));
               }

               if (curLevel.containsKey("max_coins")) {
                  levelObj.putInt("max_coins", curLevel.getInt("max_coins"));
               }

               if (curLevel.containsKey("ethereal_currency")) {
                  levelObj.putInt("ethereal_currency", curLevel.getInt("ethereal_currency"));
               }

               if (curLevel.containsKey("max_ethereal")) {
                  levelObj.putInt("max_ethereal", curLevel.getInt("max_ethereal"));
               }

               if (curLevel.containsKey("relics")) {
                  levelObj.putDouble("relics", curLevel.getDouble("relics"));
               }

               if (curLevel.containsKey("max_relics")) {
                  levelObj.putDouble("max_relics", curLevel.getDouble("max_relics"));
               }

               onlyTheNecessary.addSFSObject(levelObj);
            }

            this.data.putSFSArray("levels", onlyTheNecessary);
         }
      }

      String graphicString = this.data.getUtfString("graphic");
      if (graphicString != null && graphicString.length() > 0) {
         this.data.putSFSObject("graphic", SFSObject.newFromJsonData(graphicString));
      }

      String sfxString = this.data.getUtfString("sfx");
      if (sfxString != null && sfxString.length() > 0) {
         this.data.putSFSObject("sfx", SFSObject.newFromJsonData(sfxString));
      }

      String evolveGfxString = this.data.getUtfString("evolve_gfx");
      if (evolveGfxString != null && evolveGfxString.length() > 0) {
         this.data.putSFSObject("evolve_gfx", SFSObject.newFromJsonData(evolveGfxString));
      }

      String evolveSfxString = this.data.getUtfString("evolve_sfx");
      if (evolveSfxString != null && evolveSfxString.length() > 0) {
         this.data.putSFSObject("evolve_sfx", SFSObject.newFromJsonData(evolveSfxString));
      }

      String happinessString = this.data.getUtfString("happiness");
      if (happinessString != null && happinessString.length() > 0) {
         SFSArray happinessArray = SFSArray.newFromJsonData(happinessString);
         this.data.putSFSArray("happiness", happinessArray);
         this.likes = happinessArray;
      }

      String boxMonsReqString = this.data.getUtfString("box_monster_requirements");
      if (boxMonsReqString != null && boxMonsReqString.length() > 0) {
         this.boxMonsterRequirements = SFSArray.newFromJsonData(boxMonsReqString);
      }

      String evolveReqs = this.data.getUtfString("evolve_requirements");
      if (evolveReqs != null && evolveReqs.length() > 0) {
         this.evolveRequirements = SFSArray.newFromJsonData(evolveReqs);
         this.evolveInto = this.data.getInt("evolve_into");
         this.evolveEnabled = this.data.getInt("evolve_enabled") != 0;
         this.evolveKeyCost = this.data.getInt("crucible_access_key_cost");
         String flexEggsStr = this.data.getUtfString("evolve_req_flexeggs");
         if (flexEggsStr != null && flexEggsStr.length() > 0) {
            this.evolveReqFlexEggs = SFSArray.newFromJsonData(flexEggsStr);
         }
      }

      this.names = SFSArray.newFromJsonData(this.data.getUtfString("names"));
      this.data.removeElement("names");
      this.data.putBool("box_monster", this.boxMonsterRequirements != null);
      if (this.data.containsKey("time_to_fill_sec")) {
         this.timeToFillSec = this.data.getInt("time_to_fill_sec");
      }

   }

   public double specialCollectionRate(PlayerMonster pm) {
      int level = pm.getLevel();
      int goldLevel = GameSettings.getInt("GOLD_ISLAND_LEVEL");
      double someNumber = 1.0D;
      double rareNumberAlso = 1.0D;
      if (level > goldLevel) {
         int numGenes = this.genes.length();
         if (numGenes < 1) {
            numGenes = 1;
         }

         String monsterClass = this.data.getUtfString("class");
         if (!monsterClass.equals("CLASS_SEASON_THANKSGIVING") && !monsterClass.equals("CLASS_SEASON_HALLOWEEN") && !monsterClass.equals("CLASS_SEASON_CHRISTMAS") && !monsterClass.equals("CLASS_SEASON_VALENTINE") && !monsterClass.equals("CLASS_SEASON_EASTER") && !monsterClass.equals("CLASS_SEASON_SUMMER") && !monsterClass.equals("CLASS_RARE_SEASONAL")) {
            if (monsterClass.equals("CLASS_SUPERNATURAL")) {
               someNumber = 1.5D;
            } else if (monsterClass.equals("CLASS_SUPERETHEREAL")) {
               someNumber = 0.8D;
            } else if (monsterClass.equals("CLASS_RARE_SUPERNAT")) {
               someNumber = 5.0D;
            } else if (monsterClass.equals("CLASS_LEGENDARY")) {
               someNumber = 1.5D;
            } else if (monsterClass.equals("CLASS_DIPSTER")) {
               someNumber = 2.0D;
            } else if (monsterClass.equals("CLASS_LEGENDARY_WERDO")) {
               someNumber = 6.0D;
            } else if (monsterClass.equals("CLASS_ETHEREAL") || monsterClass.equals("CLASS_RARE_ETH") || monsterClass.equals("CLASS_EPIC_ETHEREAL")) {
               someNumber = 1.25D;
            }
         } else {
            someNumber = 1.5D;
         }

         if (monsterClass.equals("CLASS_RARE_NATURAL") || monsterClass.equals("CLASS_RARE_ETH") || monsterClass.equals("CLASS_RARE_SUPERNAT") || monsterClass.equals("CLASS_RARE_SEASONAL") || monsterClass.equals("CLASS_RARE_FIRE") || monsterClass.equals("CLASS_EPIC_RARE") || monsterClass.contentEquals("CLASS_EPIC_SEASONAL") || monsterClass.equals("CLASS_EPIC_ETHEREAL")) {
            rareNumberAlso = 1.2D;
         }

         return someNumber * rareNumberAlso * (double)numGenes * (double)(level - goldLevel) / 100.0D;
      } else {
         return 0.0D;
      }
   }

   private ISFSArray getNames() {
      return this.names;
   }

   public String generateRandomMonsterName() {
      String generatedName = "New Monster";
      ISFSArray monsterNames = this.getNames();
      if (monsterNames.size() != 0) {
         Random random = new Random();
         generatedName = monsterNames.getUtfString(random.nextInt(monsterNames.size()));
      }

      return generatedName;
   }

   public String getGenes() {
      return this.genes;
   }

   public int getTimeToFillSec() {
      return this.timeToFillSec;
   }

   public boolean isSeasonalMonster() {
      return this.genes.isEmpty();
   }

   public boolean isDipster() {
      return this.genes.equals("Q");
   }

   public boolean isWubbox() {
      return this.genes.equals("F");
   }

   public boolean isUnderling() {
      return this.genes.equals("U");
   }

   public boolean isCelestial() {
      return this.genes.equals("T");
   }

   boolean hasGene(String gene) {
      return this.genes.contains(gene);
   }

   public boolean isBoxMonsterType() {
      return this.boxMonsterRequirements != null;
   }

   public ISFSArray getBoxRequirements() {
      return this.boxMonsterRequirements;
   }

   public boolean isEvolvable() {
      return this.evolveRequirements != null || this.evolveReqFlexEggs != null;
   }

   public boolean evolveEnabled() {
      return this.evolveEnabled;
   }

   public int evolveInto() {
      return this.evolveInto;
   }

   public int evolvedFrom() {
      Iterator itr = MonsterLookup.getInstance().entries().iterator();

      Monster next;
      do {
         if (!itr.hasNext()) {
            return 0;
         }

         next = (Monster)itr.next();
      } while(next.evolveInto() != this.getEntityId());

      return next.getEntityId();
   }

   public int getEvolveKeyCost() {
      return this.evolveKeyCost;
   }

   public ISFSArray getEvolveReqsStatic() {
      return this.evolveRequirements;
   }

   public ISFSArray getEvolveReqsFlex() {
      return this.evolveReqFlexEggs;
   }

   public static void initGoldBoxMonsterRequirements() {
      goldBoxMonsterRequirements = SFSArray.newFromJsonData(GameSettings.get("GOLD_ISLAND_WUBBOX_REQUIREMENTS"));
      goldRareBoxMonsterRequirements = SFSArray.newFromJsonData(GameSettings.get("GOLD_ISLAND_RARE_WUBBOX_REQUIREMENTS"));
      goldEpicBoxMonsterRequirements = SFSArray.newFromJsonData(GameSettings.get("GOLD_ISLAND_EPIC_WUBBOX_REQUIREMENTS"));
   }

   public boolean isLegendaryMonster() {
      return this.getGenes().contains("Z") || this.getGenes().contains("z");
   }

   public boolean isGoldUpgradeMonster() {
      return this.upgradesTo().equals("gold") || this.upgradesTo().equals("gold_and_shugga") || this.upgradesTo().equals("gold_and_seasonal");
   }

   public boolean isEtherealMonster() {
      return this.upgradesTo().equals("ethereal") || this.upgradesTo().equals("magical_ethereal");
   }

   public boolean isSesaonalMonster() {
      return this.upgradesTo().equals("gold_and_seasonal") || this.upgradesTo().equals("seasonal");
   }

   public static ISFSArray getGoldBoxMonsterRequirements() {
      return goldBoxMonsterRequirements;
   }

   public static ISFSArray getGoldRareBoxMonsterRequirements() {
      return goldRareBoxMonsterRequirements;
   }

   public static ISFSArray getGoldEpicBoxMonsterRequirements() {
      return goldEpicBoxMonsterRequirements;
   }

   public ISFSArray getLikes() {
      return this.likes;
   }

   public ISFSArray getDislikes() {
      return this.dislikes;
   }

   public ISFSObject getLevel(int level) {
      return (ISFSObject)this.levelMap.get(level);
   }

   public int getMonsterID() {
      return this.data.getInt("monster_id");
   }

   public double coinRate(PlayerMonster pm) {
      ISFSObject levelDat = this.getLevel(pm.getLevel());
      Integer coinsObj = levelDat.getInt("coins");
      if (coinsObj != null) {
         double coins = (double)coinsObj;
         return coins / 60.0D;
      } else {
         return 0.0D;
      }
   }

   public double etherealRate(PlayerMonster pm) {
      ISFSObject levelData = this.getLevel(pm.getLevel());
      Integer ethRateObj = levelData.getInt("ethereal_currency");
      if (ethRateObj != null) {
         double eth = (double)ethRateObj;
         return eth / 3600.0D;
      } else {
         return 0.0D;
      }
   }

   public double relicRate(PlayerMonster pm) {
      ISFSObject levelData = this.getLevel(pm.getLevel());
      Double relicRateObj = levelData.getDouble("relics");
      return relicRateObj != null ? relicRateObj / ((double)GameSettings.getInt("USER_RELIC_GENERATION_DAYS") * 80640.0D) : 0.0D;
   }

   public int beds() {
      return this.data.getInt("beds");
   }

   public String upgradesTo() {
      return this.data.getUtfString("levelup_island");
   }

   public boolean allowedOnIsland(Island island) {
      return island.hasMonster(this.getMonsterID());
   }

   public boolean teleportableToBattle() {
      return this.allowedOnIsland(IslandLookup.get(20));
   }

   public boolean isTrackedUncommon() {
      return this.genes.length() == 4 || this.genes.equals("") || this.isLegendaryMonster() || this.isEtherealMonster() || this.isRare() && !this.isBoxMonsterType() || this.isEpic() && !this.isBoxMonsterType();
   }

   public boolean isInInventory(Player player) {
      boolean inInventory = super.isInInventory(player);
      if (!inInventory) {
         int baseMonsterId = MonsterIslandToIslandMapping.monsterSourceGivenAnyIsland(this.getMonsterID());
         if (baseMonsterId != 0 && baseMonsterId != this.getMonsterID()) {
            Monster baseMonster = MonsterLookup.get(baseMonsterId);
            inInventory = player.getInventory().hasItem(baseMonster.getEntityId());
         }
      }

      return inInventory;
   }

   public boolean isCommon() {
      return !this.isRare() && !this.isEpic();
   }

   public boolean isRare() {
      return MonsterCommonToRareMapping.isRare(this.getMonsterID());
   }

   public boolean isEpic() {
      return MonsterCommonToEpicMapping.isEpic(this.getMonsterID());
   }

   public int evolveTier() {
      int evolveTier = 0;

      for(int curMonstEntId = this.getEntityId(); curMonstEntId != 0 && MonsterLookup.getFromEntityId(curMonstEntId).evolvedFrom() != 0; curMonstEntId = MonsterLookup.getFromEntityId(curMonstEntId).evolvedFrom()) {
         ++evolveTier;
      }

      return evolveTier;
   }

   public boolean isYouth() {
      return this.evolveTier() == 0;
   }

   public boolean isAdult() {
      return this.evolveTier() == 1;
   }

   public boolean isElder() {
      return this.evolveTier() == 2;
   }
}
