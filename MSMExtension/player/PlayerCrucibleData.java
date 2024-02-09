package com.bigbluebubble.mysingingmonsters.player;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.data.Monster;
import com.bigbluebubble.mysingingmonsters.data.MonsterLookup;
import com.bigbluebubble.mysingingmonsters.data.VersionData;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventManager;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.CrucibleFlagDayEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.CrucibleHeatDiscountEvent;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import org.json.JSONArray;

public class PlayerCrucibleData {
   private static Random rand = new Random();
   public static final String STRUCTURE_ID_KEY = "struct";
   public static final String MONSTER_KEY = "monster";
   public static final String NEW_MONSTER_KEY = "new_type";
   public static final String STARTED_ON_KEY = "started_on";
   public static final String COMPLETE_ON_KEY = "complete_on";
   public static final String IS_EVOLVING_KEY = "e";
   public static final String HEAT_LEVEL_KEY = "h";
   public static final String COLLECTION_REWARD_KEY = "c";
   public static final String UNLOCK_STAGE = "u";
   public static final String FLAG_AIR_KEY = "flagA";
   public static final String FLAG_PLANT_KEY = "flagB";
   public static final String FLAG_EARTH_KEY = "flagC";
   public static final String FLAG_WATER_KEY = "flagD";
   public static final String FLAG_ICE_KEY = "flagE";
   public static final String FLAG_FIRE_KEY = "flagN";
   public static final String QUEUED_FLAG_KEY = "q";
   private static final Set<String> validKeys = new HashSet(Arrays.asList("struct", "monster", "new_type", "started_on", "complete_on", "e", "h", "c", "u", "flagA", "flagB", "flagC", "flagD", "flagE", "flagN", "q"));
   private static boolean debuggingOn = false;
   private static double debugRareSuccess = 0.0D;
   private static double debugEpicSuccess = 0.0D;
   private static double failTimeMultiplier = 0.5D;
   private static int monsterBoostMin = 5;
   private static int monsterBoostMax = 15;
   private static double boostFromHeatLevel = 0.1D;
   private static double maxFlagBoost = 0.3D;
   private static double boostPerMonsterLevel = 0.005D;
   private static double maxMonsterLevelBoost = 0.05D;
   public static int overheatingLevel = 4;
   private static double boostFromHeatLevelEpicFixed = 0.03D;
   private static double maxFlagBoostEpic = 0.15D;
   private static double boostPerMonsterLevelEpic = 0.002D;
   private static double maxMonsterLevelBoostEpic = 0.02D;
   private static JSONArray heatCosts;
   private static ArrayList<Integer> collectionMins = new ArrayList();
   private static ArrayList<Integer> collectionMaxs = new ArrayList();
   public static final char[] elementals = new char[]{'A', 'B', 'C', 'D', 'E', 'N'};
   private boolean[] elementalsActive = new boolean[]{false, false, false, false, false, false};
   protected int unlockStageViewed = 0;
   protected long structure;
   protected long userMonsterId = 0L;
   protected int newMonster = 0;
   protected long startedOn = 0L;
   protected long completeOn = 0L;
   protected boolean isEvolving = false;
   protected int heatLevel = 0;
   protected int heatCollectReward = 0;
   protected char queuedFlag;
   private static int numUnlockStages = 0;
   protected static final int defaultHeatLevels = 3;

   public static void setDebuggingOn(boolean val) {
      debuggingOn = val;
   }

   public static void setDebugSuccess(double rareSucc, double epicSucc) {
      debugRareSuccess = rareSucc;
      debugEpicSuccess = epicSucc;
   }

   public static void setUnlockStages(String unlockStagesDef) {
      SFSArray unlockJson = SFSArray.newFromJsonData(unlockStagesDef);
      numUnlockStages = unlockJson.size();
   }

   public void setUnlockCrucibleStage(int stage) {
      this.unlockStageViewed = stage;
   }

   public boolean fullyUnlocked() {
      return this.unlockStageViewed >= numUnlockStages;
   }

   public static void setFailTimeMultiplier(double val) {
      failTimeMultiplier = val;
   }

   public static void setHeatCosts(JSONArray c) {
      heatCosts = c;
   }

   public static int maxNumHeatLevels() {
      return heatCosts.length();
   }

   public double getHeatBoost(boolean isEpic, int heatLevel) {
      return !isEpic ? (double)heatLevel * boostFromHeatLevel : boostFromHeatLevelEpicFixed;
   }

   public static int defaultNumCrucibleHeatLevels() {
      return 3;
   }

   public static int getHeatCost(PlayerCrucibleData c, int toLevel, int islandType, int numSupportedHeatLevels) throws Exception {
      return getHeatCost(c == null ? 0 : c.getHeatLevel(), toLevel, islandType, numSupportedHeatLevels);
   }

   private static int getHeatCost(int fromLevel, int toLevel, int islandType, int numSupportedHeatLevels) throws Exception {
      List<TimedEvent> timedEvents = TimedEventManager.instance().currentActiveOnKey(TimedEventType.CrucibleHeatDiscount, 0, islandType);
      CrucibleHeatDiscountEvent bonusEvent = null;
      if (timedEvents.size() > 0) {
         bonusEvent = (CrucibleHeatDiscountEvent)((CrucibleHeatDiscountEvent)timedEvents.get(0));
      }

      int sum = 0;

      for(int i = fromLevel + 1; i <= toLevel && i <= numSupportedHeatLevels; ++i) {
         sum = (int)((float)sum + (float)heatCosts.getInt(i - 1) * (bonusEvent == null ? 1.0F : bonusEvent.getDiscountValue()));
      }

      return sum;
   }

   public static void setBoostFromHeatLevel(double normal, double epicFixedBoost) {
      boostFromHeatLevel = normal;
      boostFromHeatLevelEpicFixed = epicFixedBoost;
   }

   public int getHeatLevel() {
      return this.heatLevel;
   }

   public int collectExcessHeat() throws Exception {
      int temp = this.heatCollectReward;
      this.heatCollectReward = 0;
      this.heatLevel = 0;
      return temp;
   }

   private static int calcRandomHeatCollection(int postEvolveHeatLevel, int islandType) throws Exception {
      int min = minCollection(postEvolveHeatLevel);
      int max = maxCollection(postEvolveHeatLevel);
      if (min > max) {
         throw new Exception("invalid min, max set: " + min + ", " + max);
      } else if (max <= 0) {
         return 0;
      } else {
         int returnedRelics = min;
         if (min != max) {
            returnedRelics = min + rand.nextInt(max - min);
            if (returnedRelics < min || returnedRelics > max) {
               throw new Exception("invalid random returnedRelics: " + returnedRelics);
            }
         }

         List<TimedEvent> timedEvents = TimedEventManager.instance().currentActiveOnKey(TimedEventType.CrucibleHeatDiscount, 0, islandType);
         if (timedEvents.size() > 0) {
            CrucibleHeatDiscountEvent bonusEvent = (CrucibleHeatDiscountEvent)((CrucibleHeatDiscountEvent)timedEvents.get(0));
            returnedRelics = (int)((float)returnedRelics * bonusEvent.getDiscountValue());
         }

         if (returnedRelics < 1) {
            returnedRelics = 1;
         }

         return returnedRelics;
      }
   }

   public static void setHeatCollectionRange(int heatLevel, int min, int max) throws Exception {
      if (heatLevel >= 1 && heatCosts.length() >= heatLevel) {
         collectionMins.add(heatLevel - 1, min);
         collectionMaxs.add(heatLevel - 1, max);
      } else {
         throw new Exception("invalid heat level specified in call to setHeatCollectionRange: " + heatLevel);
      }
   }

   private static int minCollection(int heatLevel) throws Exception {
      if (heatLevel == 0) {
         return 0;
      } else if (heatLevel >= 1 && collectionMins.size() >= heatLevel) {
         return (Integer)collectionMins.get(heatLevel - 1);
      } else {
         throw new Exception("invalid heat level specified in call to minCollection: " + heatLevel);
      }
   }

   private static int maxCollection(int heatLevel) throws Exception {
      if (heatLevel == 0) {
         return 0;
      } else if (heatLevel >= 1 && collectionMaxs.size() >= heatLevel) {
         return (Integer)collectionMaxs.get(heatLevel - 1);
      } else {
         throw new Exception("invalid heat level specified in call to maxCollection: " + heatLevel);
      }
   }

   public static int elementalInd(char element) {
      for(int i = 0; i < elementals.length; ++i) {
         if (element == elementals[i]) {
            return i;
         }
      }

      return -1;
   }

   public static void setMaxFlagBoost(double normal, double epic) {
      maxFlagBoost = normal;
      maxFlagBoostEpic = epic;
   }

   private void resetFlags() {
      for(int i = 0; i < this.elementalsActive.length; ++i) {
         this.elementalsActive[i] = false;
      }

      this.queuedFlag = 0;
   }

   private int selectInactiveFlag() {
      int[] inactive = new int[this.elementalsActive.length];
      int inactiveSize = 0;

      int select;
      for(select = 0; select < this.elementalsActive.length; ++select) {
         if (!this.elementalsActive[select]) {
            inactive[inactiveSize++] = select;
         }
      }

      if (inactiveSize != 0) {
         select = rand.nextInt(inactiveSize);
         return inactive[select];
      } else {
         return -1;
      }
   }

   private void QAOnlyFlagSet(boolean flagA, boolean flagB, boolean flagC, boolean flagD, boolean flagE, boolean flagN) {
      this.resetFlags();
      this.elementalsActive[elementalInd('A')] = flagA;
      this.elementalsActive[elementalInd('B')] = flagB;
      this.elementalsActive[elementalInd('C')] = flagC;
      this.elementalsActive[elementalInd('D')] = flagD;
      this.elementalsActive[elementalInd('E')] = flagE;
      this.elementalsActive[elementalInd('N')] = flagN;
   }

   private boolean elementalFlagActive(char gene) {
      int ind = elementalInd(gene);
      return ind == -1 ? false : this.elementalsActive[ind];
   }

   private double getFlagBoost(boolean isEpic, String monsterGeneStr, int islandType) {
      List<TimedEvent> timedEvents = TimedEventManager.instance().currentActiveOnKey(TimedEventType.CrucibleFlagDay, 0, islandType);
      CrucibleFlagDayEvent bonusEvent = null;
      if (timedEvents.size() > 0) {
         bonusEvent = (CrucibleFlagDayEvent)((CrucibleFlagDayEvent)timedEvents.get(0));
      }

      int numApplicableFlags = 0;
      int numGenes = monsterGeneStr.length();

      for(int i = 0; i < numGenes; ++i) {
         char gene = monsterGeneStr.charAt(i);
         if (this.elementalFlagActive(gene) || bonusEvent != null && bonusEvent.flagActive(gene)) {
            ++numApplicableFlags;
         }
      }

      if (numApplicableFlags == numGenes) {
         return isEpic ? maxFlagBoostEpic : maxFlagBoost;
      } else {
         return 0.0D;
      }
   }

   public static void setLevelBoostRange(int min, int max) {
      monsterBoostMin = min;
      monsterBoostMax = max;
   }

   public static void setMaxMonsterLevelBoost(double normal, double epic) {
      maxMonsterLevelBoost = normal;
      maxMonsterLevelBoostEpic = epic;
   }

   public static void setBoostPerMonsterLevel(double normal, double epic) {
      boostPerMonsterLevel = normal;
      boostPerMonsterLevelEpic = epic;
   }

   private double getMonsterLevelBoost(boolean isEpicEvolve, int monsterLevel) {
      double boostPerLevel = boostPerMonsterLevel;
      double maxBoost = maxMonsterLevelBoost;
      if (isEpicEvolve) {
         boostPerLevel = boostPerMonsterLevelEpic;
         maxBoost = maxMonsterLevelBoostEpic;
      }

      return Math.min(boostPerLevel * (double)Math.min(Math.max(monsterLevel - monsterBoostMin, 0), monsterBoostMax - monsterBoostMin), maxBoost);
   }

   public double chanceOfEvolution(Monster sourceMonster, Monster destMonster, int monsterLevel, int islandType) {
      if (!sourceMonster.evolveEnabled() && !TimedEventManager.instance().hasTimedEventNow(TimedEventType.EvolveAvailability, sourceMonster.getEntityId(), islandType)) {
         return 0.0D;
      } else {
         boolean isEpicDest = destMonster.isEpic();
         if (!debuggingOn) {
            return this.getHeatBoost(isEpicDest, this.heatLevel) + this.getFlagBoost(isEpicDest, sourceMonster.getGenes(), islandType) + this.getMonsterLevelBoost(isEpicDest, monsterLevel);
         } else {
            return !isEpicDest ? debugRareSuccess : debugEpicSuccess;
         }
      }
   }

   public PlayerCrucibleData(ISFSObject crucibleData) throws PlayerLoadingException {
      this.initFromSFSObject(crucibleData);
   }

   public void initFromSFSObject(ISFSObject data) throws PlayerLoadingException {
      try {
         this.structure = SFSHelpers.getLong(data.get("struct"));
         if (data.containsKey("u")) {
            this.unlockStageViewed = SFSHelpers.getInt(data.get("u"));
         }

         if (data.containsKey("e")) {
            this.isEvolving = SFSHelpers.getInt(data.get("e")) != 0;
         }

         if (data.containsKey("h")) {
            this.heatLevel = SFSHelpers.getInt(data.get("h"));
         }

         if (data.containsKey("c")) {
            this.heatCollectReward = SFSHelpers.getInt(data.get("c"));
         }

         for(int i = 0; i < elementals.length; ++i) {
            String elementStr = String.valueOf(elementals[i]);
            String elementKey = "flag" + elementStr;
            if (data.containsKey(elementKey)) {
               this.elementalsActive[i] = SFSHelpers.getInt(data.get(elementKey)) != 0;
            } else {
               this.elementalsActive[i] = false;
            }
         }

         if (data.containsKey("monster")) {
            this.userMonsterId = SFSHelpers.getLong(data.get("monster"));
         }

         if (data.containsKey("new_type")) {
            this.newMonster = SFSHelpers.getInt(data.get("new_type"));
         }

         if (data.containsKey("started_on")) {
            this.startedOn = SFSHelpers.getLong(data.get("started_on"));
         }

         if (data.containsKey("complete_on")) {
            this.completeOn = SFSHelpers.getLong(data.get("complete_on"));
         }

         if (data.containsKey("q")) {
            String s = SFSHelpers.getUtfString(data.get("q"));
            if (!s.isEmpty()) {
               this.queuedFlag = s.charAt(0);
            } else {
               this.queuedFlag = 0;
            }
         }

         PlayerDataValidation.validateKeys(data, validKeys);
      } catch (Exception var5) {
         throw new PlayerLoadingException(var5, "PlayerCrucibleData initialization error");
      }
   }

   public void testEvolve(PlayerMonster monster, long startedOn, int heatLevel, boolean flagA, boolean flagB, boolean flagC, boolean flagD, boolean flagE, boolean flagN, int islandType, Player player, ISFSObject response) throws Exception {
      HashMap<Integer, Integer> listOfCombos = new HashMap();
      HashMap<String, Integer> listOfFlagCombos = new HashMap();
      int numRuns = 10000;

      for(int i = 0; i < numRuns; ++i) {
         this.QAOnlyFlagSet(flagA, flagB, flagC, flagD, flagE, flagN);
         this.startEvolve(monster, startedOn, heatLevel, islandType, player);
         int newMonsterResult = this.newMonster;
         if (newMonsterResult == 0) {
            newMonsterResult = monster.getType();
         }

         if (!listOfCombos.containsKey(newMonsterResult)) {
            listOfCombos.put(newMonsterResult, 0);
         }

         listOfCombos.put(newMonsterResult, (Integer)listOfCombos.get(newMonsterResult) + 1);
         if (this.queuedFlag != 0) {
            String flag = String.valueOf(this.queuedFlag);
            if (!listOfFlagCombos.containsKey(flag)) {
               listOfFlagCombos.put(flag, 0);
            }

            listOfFlagCombos.put(flag, (Integer)listOfFlagCombos.get(flag) + 1);
         }

         this.errorHandlingReset();
      }

      float percentageDivisor = (float)numRuns / 100.0F;
      SFSArray arr = new SFSArray();
      Logger.trace("Combo probabilities are: ");
      Iterator var27 = listOfCombos.entrySet().iterator();

      Entry combo;
      float percentProbability;
      SFSObject curProbability;
      while(var27.hasNext()) {
         combo = (Entry)var27.next();
         int resultMonsterId = (Integer)combo.getKey();
         percentProbability = (float)(Integer)combo.getValue() / percentageDivisor;
         Logger.trace("Chance of getting monster " + resultMonsterId + " is " + percentProbability);
         curProbability = new SFSObject();
         curProbability.putInt("resultMonsterId", resultMonsterId);
         curProbability.putFloat("probability", percentProbability);
         arr.addSFSObject(curProbability);
      }

      var27 = listOfFlagCombos.entrySet().iterator();

      while(var27.hasNext()) {
         combo = (Entry)var27.next();
         String resultFlagId = (String)combo.getKey();
         percentProbability = (float)(Integer)combo.getValue() / percentageDivisor;
         Logger.trace("Chance of getting flag " + resultFlagId + " is " + percentProbability);
         curProbability = new SFSObject();
         curProbability.putUtfString("resultFlagId", resultFlagId);
         curProbability.putFloat("probability", percentProbability);
         arr.addSFSObject(curProbability);
      }

      response.putSFSArray("probabilities", arr);
   }

   public int doEvolveRolls(Monster sourceMonster, Monster attemptedMonster, int heatLevel, int monsterLevel, int islandType) {
      Monster commonMonster = sourceMonster.isCommon() ? sourceMonster : null;
      Monster rareMonster = sourceMonster.isRare() ? sourceMonster : (attemptedMonster.isRare() ? attemptedMonster : null);
      Monster epicMonster = attemptedMonster.isEpic() ? attemptedMonster : null;
      if (commonMonster != null && epicMonster != null) {
         rareMonster = MonsterLookup.getFromEntityId(commonMonster.evolveInto());
         if (!rareMonster.isRare()) {
            return 0;
         }
      }

      if (heatLevel != overheatingLevel && epicMonster != null) {
         epicMonster = null;
      }

      int monsterOutput = false;
      int monsterOutput;
      if (epicMonster == null) {
         if (commonMonster == null || rareMonster == null) {
            return 0;
         }

         if (rand.nextDouble() < this.chanceOfEvolution(commonMonster, rareMonster, monsterLevel, islandType)) {
            monsterOutput = rareMonster.getMonsterID();
         } else {
            monsterOutput = 0;
         }
      } else if (commonMonster == null) {
         if (rareMonster == null || epicMonster == null) {
            return 0;
         }

         if (rand.nextDouble() < this.chanceOfEvolution(rareMonster, epicMonster, monsterLevel, islandType)) {
            monsterOutput = epicMonster.getMonsterID();
         } else {
            monsterOutput = 0;
         }
      } else {
         if (commonMonster == null || rareMonster == null || epicMonster == null) {
            return 0;
         }

         if (rand.nextDouble() < this.chanceOfEvolution(commonMonster, rareMonster, monsterLevel, islandType)) {
            this.resetFlags();
            if (rand.nextDouble() < this.chanceOfEvolution(rareMonster, epicMonster, monsterLevel, islandType)) {
               monsterOutput = epicMonster.getMonsterID();
            } else {
               monsterOutput = rareMonster.getMonsterID();
            }
         } else {
            monsterOutput = 0;
            this.resetFlags();
         }
      }

      return monsterOutput != 0 ? monsterOutput : sourceMonster.getMonsterID();
   }

   public void startEvolve(PlayerMonster monster, long startedOn, int heatLevel, int islandType, Player player) throws Exception {
      Monster sourceMonster = MonsterLookup.get(monster.getType());
      this.heatLevel = heatLevel;
      this.userMonsterId = monster.getID();
      this.startedOn = startedOn;
      Monster staticEvolve1 = MonsterLookup.getFromEntityId(sourceMonster.evolveInto());
      if (staticEvolve1 == null) {
         throw new Exception("Trying to evolve a monster that can't be evolved");
      } else if (!staticEvolve1.supportedByClient(player.getPlatform(), player.getSubplatform(), VersionData.Instance().getMaxServerVersionFromClientVersion(player.lastClientVersion()))) {
         throw new Exception("Trying to evolve a monster that can't be evolved");
      } else {
         Monster staticEvolve2 = null;
         if (heatLevel == overheatingLevel) {
            staticEvolve2 = MonsterLookup.getFromEntityId(staticEvolve1.evolveInto());
            if (staticEvolve2 != null && !staticEvolve2.supportedByClient(player.getPlatform(), player.getSubplatform(), VersionData.Instance().getMaxServerVersionFromClientVersion(player.lastClientVersion()))) {
               staticEvolve2 = null;
            }
         }

         int outputMonster = this.doEvolveRolls(sourceMonster, staticEvolve2 == null ? staticEvolve1 : staticEvolve2, heatLevel, monster.getLevel(), islandType);
         if (outputMonster == 0) {
            throw new Exception("Invalid evolve input");
         } else {
            PlayerIsland island = player.getIslandByIslandIndex(islandType);
            float islandThemeMod = 1.0F - (1.0F - island.getIslandThemeModifier("crucible_speed_mod", player));
            if (outputMonster == sourceMonster.getMonsterID()) {
               this.newMonster = 0;
               this.completeOn = startedOn + (long)((double)sourceMonster.getBuildTimeMs() * failTimeMultiplier * (double)islandThemeMod);
               if (heatLevel < overheatingLevel) {
                  int flagInd = this.selectInactiveFlag();
                  if (flagInd == -1) {
                     this.queuedFlag = 0;
                  } else {
                     this.queuedFlag = elementals[flagInd];
                  }
               } else {
                  this.resetFlags();
                  this.queuedFlag = 0;
               }
            } else {
               this.newMonster = outputMonster;
               this.completeOn = startedOn + (long)((float)MonsterLookup.get(this.newMonster).getBuildTimeMs() * islandThemeMod);
               this.queuedFlag = 0;
            }

            this.isEvolving = true;
            this.heatCollectReward = calcRandomHeatCollection(heatLevel - 1, islandType);
         }
      }
   }

   public boolean finishEvolve(PlayerMonster m, boolean verify, PlayerIsland island) {
      if (m != null && m.getID() == this.userMonsterId) {
         int ind;
         if (this.newMonster != 0) {
            ind = MonsterLookup.get(m.getType()).evolveInto();
            int secondEvolve = 0;
            if (ind != 0) {
               secondEvolve = MonsterLookup.getFromEntityId(ind).evolveInto();
            }

            if (this.newMonster != MonsterLookup.getFromEntityId(ind).getMonsterID() && (secondEvolve == 0 || this.newMonster != MonsterLookup.getFromEntityId(secondEvolve).getMonsterID())) {
               return false;
            }
         }

         if (!verify) {
            if (this.newMonster != 0) {
               m.evolve(MonsterLookup.get(this.newMonster).getEntityId(), island);
               this.resetFlags();
            } else {
               ind = elementalInd(this.queuedFlag);
               if (ind != -1) {
                  this.elementalsActive[ind] = true;
               }

               this.queuedFlag = 0;
            }

            if (this.heatLevel != overheatingLevel) {
               this.heatLevel = Math.max(0, this.heatLevel - 1);
            } else {
               this.heatLevel = 0;
            }

            this.isEvolving = false;
            this.userMonsterId = 0L;
            this.newMonster = 0;
            this.startedOn = 0L;
            this.completeOn = 0L;
         }

         return true;
      } else {
         return false;
      }
   }

   public void errorHandlingReset() {
      this.resetFlags();
      this.heatLevel = 0;
      this.isEvolving = false;
      this.userMonsterId = 0L;
      this.newMonster = 0;
      this.startedOn = 0L;
      this.completeOn = 0L;
   }

   public ISFSObject getData() {
      return this.toSFSObject();
   }

   public ISFSObject toSFSObject() {
      ISFSObject s = new SFSObject();
      s.putLong("struct", this.getStructureID());
      if (this.unlockStageViewed != 0) {
         s.putInt("u", this.unlockStageViewed);
      }

      if (this.isEvolving) {
         s.putInt("e", 1);
      }

      if (this.heatLevel != 0) {
         s.putInt("h", this.heatLevel);
      }

      if (this.heatCollectReward != 0) {
         s.putInt("c", this.heatCollectReward);
      }

      for(int i = 0; i < elementals.length; ++i) {
         if (this.elementalsActive[i]) {
            String elementStr = String.valueOf(elementals[i]);
            s.putInt("flag" + elementStr, this.elementalsActive[i] ? 1 : 0);
         }
      }

      if (this.userMonsterId != 0L) {
         s.putLong("monster", this.userMonsterId);
      }

      if (this.newMonster != 0) {
         s.putInt("new_type", this.newMonster);
      }

      if (this.startedOn != 0L) {
         s.putLong("started_on", this.startedOn);
      }

      if (this.completeOn != 0L) {
         s.putLong("complete_on", this.completeOn);
      }

      if (this.queuedFlag != 0) {
         s.putUtfString("q", String.valueOf(this.queuedFlag));
      }

      return s;
   }

   public String toString() {
      return this.toSFSObject().getDump();
   }

   public long getStructureID() {
      return this.structure;
   }

   public long getUserMonsterId() {
      return this.userMonsterId;
   }

   public int getNewMonster() {
      return this.newMonster;
   }

   public char getQueuedFlag() {
      return this.queuedFlag;
   }

   public long getTimeRemaining() {
      long timeNow = MSMExtension.CurrentDBTime();
      if (this.completeOn <= 0L) {
         return 0L;
      } else {
         long timeLeft = this.completeOn - timeNow;
         return Math.max(timeLeft, 0L);
      }
   }

   public long getInitialTimeRemaining() {
      return Math.max(this.completeOn - this.startedOn, 0L);
   }

   public void finishEvolvingNow() {
      this.completeOn = MSMExtension.CurrentDBTime();
   }

   public long getCompletionTime() {
      return this.completeOn;
   }

   public long getStartTime() {
      return this.startedOn;
   }

   public void reduceEvolvingTimeByVideo() {
      long reduceTimeAmount = (long)GameSettings.getInt("USER_SPEED_UP_DURATION") * 60L * 1000L;
      this.startedOn -= reduceTimeAmount;
      this.completeOn -= reduceTimeAmount;
   }
}
