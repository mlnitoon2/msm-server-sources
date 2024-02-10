package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventManager;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.EntityAvailabilityEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.ReturningUserBonusEvent;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.bigbluebubble.mysingingmonsters.player.PlayerBuffs;
import com.bigbluebubble.mysingingmonsters.player.PlayerIsland;
import com.bigbluebubble.mysingingmonsters.player.PlayerTimedEvents;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class BreedingLookup {
   private static BreedingLookup instance;
   private static final int LOWEST_LEVEL_BREED = 4;
   private Map<Entry<Integer, Integer>, List<BreedingCombination>> breedingMap = new ConcurrentHashMap();
   private float maxBreedLevelWeight = GameSettings.getFloat("GOLD_ISLAND_LEVEL") - 4.0F;
   private Random random = new Random();

   public static BreedingLookup getInstance() {
      return instance;
   }

   private BreedingLookup(IDbWrapper db) throws Exception {
      String sql = "SELECT * FROM breeding_combinations";
      ISFSArray results = db.query("SELECT * FROM breeding_combinations");
      Iterator i = results.iterator();

      while(i.hasNext()) {
         SFSObject breedingData = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         if (breedingData != null) {
            this.add(breedingData);
         }
      }

   }

   public static void init(IDbWrapper db) throws Exception {
      instance = new BreedingLookup(db);
   }

   private void add(ISFSObject breedingData) {
      Entry<Integer, Integer> key = new SimpleEntry(breedingData.getInt("monster_1"), breedingData.getInt("monster_2"));
      List<BreedingCombination> breedingList = (List)this.breedingMap.get(key);
      if (breedingList == null) {
         breedingList = new ArrayList();
         this.breedingMap.put(key, breedingList);
      }

      ((List)breedingList).add(new BreedingCombination(breedingData));
   }

   public Monster getBreedingResult(Monster l, Monster r, int leftLevel, int rightLevel, Player player, PlayerIsland island, VersionInfo playerVersion) {
      if (l.getMonsterID() == r.getMonsterID()) {
         return null;
      } else {
         int torchesLit = island.getNumLitTorchesOnIsland();
         float islandThemeTorchMod = island.getIslandThemeModifier("wishing_torch_mod", player);
         float timedEventMod = 1.0F;
         PlayerTimedEvents pte = player.getTimedEvents();
         List<TimedEvent> playerEvents = null;
         if (pte != null) {
            playerEvents = pte.currentActiveOnKey(TimedEventType.ReturningUserBonus, 0, 0);
         }

         if (playerEvents != null && playerEvents.size() > 0) {
            ReturningUserBonusEvent bonusEvent = (ReturningUserBonusEvent)((ReturningUserBonusEvent)playerEvents.get(0));
            timedEventMod = bonusEvent.torchModifierMod();
         } else {
            List<TimedEvent> events = TimedEventManager.instance().currentActiveOnKey(TimedEventType.ReturningUserBonus, 0, 0);
            if (events != null && events.size() > 0) {
               ReturningUserBonusEvent bonusEvent = (ReturningUserBonusEvent)((ReturningUserBonusEvent)events.get(0));
               timedEventMod = bonusEvent.torchModifierMod();
            }
         }

         float buffMod = player.getBuffs().getMultiplier(PlayerBuffs.Buffs.BreedingChanceIncrease, island.getType());
         float totalTorchProbabilityIncr = (float)torchesLit * GameSettings.getFloat("RARE_PROBABILITY_INCR_PER_TORCH") * islandThemeTorchMod * timedEventMod * buffMod;
         Monster leftMonster = l;
         Monster rightMonster = r;
         Monster result = null;
         if (MonsterCommonToRareMapping.rareToCommonMapContainsKey(l.getMonsterID())) {
            leftMonster = MonsterLookup.get(MonsterCommonToRareMapping.rareToCommonMapGet(l.getMonsterID()).commonMonsterId());
         }

         if (MonsterCommonToRareMapping.rareToCommonMapContainsKey(r.getMonsterID())) {
            rightMonster = MonsterLookup.get(MonsterCommonToRareMapping.rareToCommonMapGet(r.getMonsterID()).commonMonsterId());
         }

         List<BreedingCombination> combinations = (List)this.breedingMap.get(new SimpleEntry(leftMonster.getMonsterID(), rightMonster.getMonsterID()));
         if (combinations == null) {
            combinations = (List)this.breedingMap.get(new SimpleEntry(rightMonster.getMonsterID(), leftMonster.getMonsterID()));
         }

         if (combinations != null) {
            List<BreedingCombination> combinations = new ArrayList(combinations);

            for(int i = combinations.size() - 1; i >= 0; --i) {
               BreedingCombination b = (BreedingCombination)combinations.get(i);
               Monster breedResult = b.getResult();
               if (!canBeBredOnIsland(breedResult, player, island, playerVersion)) {
                  combinations.remove(i);
               }
            }

            result = this.getDatabaseBreedingComboResult(combinations, leftMonster, rightMonster, leftLevel, rightLevel, player, island, torchesLit, totalTorchProbabilityIncr, playerVersion);
         } else if (leftMonster.getMonsterID() == rightMonster.getMonsterID()) {
            result = leftMonster;
         }

         boolean gotAnEpic = false;
         if (result != null) {
            Monster rareMonster;
            float probabilityOfRare;
            ArrayList rares;
            int i;
            if (GameSettings.get("DEBUG_EPIC_BREEDING_TOGGLE", 0) == 1) {
               rares = MonsterCommonToEpicMapping.commonToEpicMapGet(result.getMonsterID());
               if (rares != null) {
                  for(i = 0; i < rares.size(); ++i) {
                     MonsterEpicData epicMonstInfo = (MonsterEpicData)rares.get(i);
                     rareMonster = MonsterLookup.get(epicMonstInfo.epicMonsterId());
                     if (rareMonster.supportedByClient(player.getPlatform(), player.getSubplatform(), playerVersion) && rareMonster.allowedOnIsland(IslandLookup.get(island.getType()))) {
                        probabilityOfRare = 0.0F;
                        probabilityOfRare = GameSettings.getFloat("DEBUG_EPIC_BREEDING_OVERWRITE");
                        if (this.random.nextFloat() < probabilityOfRare) {
                           result = rareMonster;
                           gotAnEpic = true;
                        }
                        break;
                     }
                  }
               }
            }

            if (!gotAnEpic) {
               rares = MonsterCommonToRareMapping.commonToRareMapGet(result.getMonsterID());
               if (rares != null) {
                  for(i = 0; i < rares.size(); ++i) {
                     MonsterRareData rareMonstInfo = (MonsterRareData)rares.get(i);
                     rareMonster = MonsterLookup.get(rareMonstInfo.rareMonsterId());
                     if (canBeBredOnIsland(rareMonster, player, island, playerVersion) || GameSettings.getInt("DEBUG_RARE_BREEDING_TOGGLE") == 1) {
                        probabilityOfRare = 0.0F;
                        if (rareMonstInfo.probability > 0.0F) {
                           float multiplier = 1.0F;
                           if (result.isEtherealMonster() && island.isEtherealIslandWithModifiers() && result.genes.length() == 1) {
                              multiplier = GameSettings.getFloat("RARE_ETH_TIER1_BREED_MULTIPLIER");
                           }

                           MonsterRareData commonOfLeft;
                           if (MonsterCommonToRareMapping.rareToCommonMapContainsKey(r.getMonsterID())) {
                              commonOfLeft = MonsterCommonToRareMapping.rareToCommonMapGet(r.getMonsterID());
                              if (commonOfLeft != null && l.getMonsterID() == commonOfLeft.commonMonsterId()) {
                                 multiplier *= GameSettings.getFloat("COMMON_RARE_BREED_MULTIPLIER");
                              }
                           } else if (MonsterCommonToRareMapping.rareToCommonMapContainsKey(l.getMonsterID())) {
                              commonOfLeft = MonsterCommonToRareMapping.rareToCommonMapGet(l.getMonsterID());
                              if (commonOfLeft != null && r.getMonsterID() == commonOfLeft.commonMonsterId()) {
                                 multiplier *= GameSettings.getFloat("COMMON_RARE_BREED_MULTIPLIER");
                              }
                           }

                           probabilityOfRare = rareMonstInfo.probability * multiplier;
                           probabilityOfRare = Math.max(probabilityOfRare, 0.01F);
                           probabilityOfRare += totalTorchProbabilityIncr / 100.0F;
                        }

                        if (GameSettings.get("DEBUG_RARE_BREEDING_TOGGLE", 0) == 1) {
                           probabilityOfRare = GameSettings.getFloat("DEBUG_RARE_BREEDING_OVERWRITE");
                        }

                        if (this.random.nextFloat() < probabilityOfRare) {
                           result = rareMonster;
                        }
                        break;
                     }
                  }
               }
            }
         }

         return result;
      }
   }

   private float getMonsterBreedWeightContribution(Monster monster, int monsterLevel, float totalGenesInvolved) {
      return Math.min((float)(monsterLevel - 4) / this.maxBreedLevelWeight, 1.0F) * ((float)monster.getGenes().length() / totalGenesInvolved);
   }

   private Monster getDatabaseBreedingComboResult(List<BreedingCombination> combinations, Monster leftMonster, Monster rightMonster, int leftLevel, int rightLevel, Player player, PlayerIsland island, int torchesLit, float totalTorchProbabilityIncr, VersionInfo playerVersion) {
      float totalNumGenes = (float)(leftMonster.getGenes().length() + rightMonster.getGenes().length());
      if (totalNumGenes == 0.0F) {
         totalNumGenes = 1.0F;
      }

      float totalContribution = this.getMonsterBreedWeightContribution(leftMonster, leftLevel, totalNumGenes) + this.getMonsterBreedWeightContribution(rightMonster, rightLevel, totalNumGenes);
      List<Integer> rarestIndices = new ArrayList();
      int rarestProbPreMod = 100;
      int nonZeroCombos = 0;
      float totalProbability = 0.0F;

      int randomValue;
      for(randomValue = 0; randomValue < combinations.size(); ++randomValue) {
         if (canBeBredOnIsland(((BreedingCombination)combinations.get(randomValue)).monsterResult, player, island, playerVersion)) {
            BreedingCombination combo = (BreedingCombination)combinations.get(randomValue);
            int comboProbability = combo.getProbability(island.getType());
            if (comboProbability != 0) {
               if (comboProbability <= rarestProbPreMod && combo.monster1.getMonsterID() != combo.monsterResult.getMonsterID() && combo.monster2.getMonsterID() != combo.monsterResult.getMonsterID()) {
                  rarestIndices.add(randomValue);
                  rarestProbPreMod = combo.getProbability(island.getType());
               }

               ++nonZeroCombos;
            }

            totalProbability += (float)comboProbability * (1.0F + totalContribution * (combo.getModifier() - 1.0F));
         }
      }

      if (totalProbability <= 0.0F) {
         return null;
      } else {
         if (torchesLit > 0) {
            totalProbability = this.findTotalProbability(combinations, nonZeroCombos, rarestIndices, totalContribution, player, island, torchesLit, totalTorchProbabilityIncr, playerVersion);
         }

         randomValue = this.random.nextInt((int)totalProbability);
         int resultIndex = -1;
         float currentTotalProbability = 0.0F;

         for(int i = 0; i < combinations.size(); ++i) {
            float comboProbability = this.probabilityWithTorches((BreedingCombination)combinations.get(i), totalContribution, i, player, island, torchesLit, rarestIndices, nonZeroCombos, totalTorchProbabilityIncr, playerVersion);
            if (comboProbability > 0.0F) {
               currentTotalProbability += comboProbability;
            }

            if (0.0F < comboProbability && (float)randomValue < currentTotalProbability) {
               resultIndex = i;
               break;
            }
         }

         return resultIndex != -1 ? ((BreedingCombination)combinations.get(resultIndex)).getResult() : null;
      }
   }

   private float findTotalProbability(List<BreedingCombination> combinations, int nonZeroCombos, List<Integer> rarestIndices, float totalContributionOfMonsters, Player player, PlayerIsland island, int torchesLit, float totalTorchProbabilityIncr, VersionInfo playerVersion) {
      float totalProbability = 0.0F;

      for(int i = 0; i < combinations.size(); ++i) {
         float comboProbability = this.probabilityWithTorches((BreedingCombination)combinations.get(i), totalContributionOfMonsters, i, player, island, torchesLit, rarestIndices, nonZeroCombos, totalTorchProbabilityIncr, playerVersion);
         if (comboProbability > 0.0F) {
            totalProbability += comboProbability;
         }
      }

      return totalProbability;
   }

   private float probabilityWithTorches(BreedingCombination combo, float totalContribution, int comboIndex, Player player, PlayerIsland island, int torchesLit, List<Integer> rarestIndices, int nonZeroCombos, float totalTorchProbabilityIncr, VersionInfo playerVersion) {
      if (!canBeBredOnIsland(combo.getResult(), player, island, playerVersion)) {
         return 0.0F;
      } else {
         float comboProbability = (float)combo.getProbability(island.getType()) * (1.0F + totalContribution * (combo.getModifier() - 1.0F));
         if (torchesLit != 0 && !rarestIndices.isEmpty()) {
            if (rarestIndices.contains(comboIndex)) {
               comboProbability += totalTorchProbabilityIncr / (float)rarestIndices.size();
            } else if (nonZeroCombos > 1) {
               comboProbability -= totalTorchProbabilityIncr / (float)(nonZeroCombos - rarestIndices.size());
            }
         }

         return comboProbability;
      }
   }

   private static boolean canBeBredOnIsland(Monster monster, Player player, PlayerIsland island, VersionInfo playerVersion) {
      if (!monster.supportedByClient(player.getPlatform(), player.getSubplatform(), playerVersion)) {
         return false;
      } else if (!monster.allowedOnIsland(IslandLookup.get(island.getType()))) {
         return false;
      } else {
         return monster.getViewInMarket() != 1 && !StoreReplacements.currentlyReplacesAnotherStoreItem(monster.getEntityId(), island) ? EntityAvailabilityEvent.hasTimedEventNow(monster, player, island.getType()) : true;
      }
   }
}
