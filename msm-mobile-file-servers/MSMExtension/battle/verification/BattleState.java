package com.bigbluebubble.mysingingmonsters.battle.verification;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.mysingingmonsters.battle.BattleMonsterActionData;
import com.bigbluebubble.mysingingmonsters.battle.BattleMonsterStatData;
import com.bigbluebubble.mysingingmonsters.util.pcg.Pcg32;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BattleState {
   public static final boolean DEBUG_BATTLE = false;
   public Pcg32 rand = new Pcg32();
   BattleTeam[] teams;
   BattlePlayer[] activePlayers;
   boolean isTutorial;
   int currentTeam;
   public boolean opponentCanSwap = false;
   public float pickOptimalPercentage = 0.0F;
   final int NUM_TEAMS = 2;
   final long PCG_SHIFTED_INC = 721347520444481703L;
   static final Map<Character, BattleState.MonsterElement> geneToElementMap = new HashMap<Character, BattleState.MonsterElement>() {
      private static final long serialVersionUID = 1L;

      {
         this.put('A', BattleState.MonsterElement.Air);
         this.put('B', BattleState.MonsterElement.Plant);
         this.put('C', BattleState.MonsterElement.Earth);
         this.put('D', BattleState.MonsterElement.Water);
         this.put('E', BattleState.MonsterElement.Cold);
         this.put('G', BattleState.MonsterElement.Plasma);
         this.put('J', BattleState.MonsterElement.Shadow);
         this.put('K', BattleState.MonsterElement.Mech);
         this.put('L', BattleState.MonsterElement.Crystal);
         this.put('M', BattleState.MonsterElement.Poison);
      }
   };
   static final Map<BattleState.MonsterElement, BattleState.MonsterElement> elementPriorityMap = new HashMap<BattleState.MonsterElement, BattleState.MonsterElement>() {
      private static final long serialVersionUID = 1L;

      {
         this.put(BattleState.MonsterElement.Air, BattleState.MonsterElement.Plant);
         this.put(BattleState.MonsterElement.Plant, BattleState.MonsterElement.Water);
         this.put(BattleState.MonsterElement.Earth, BattleState.MonsterElement.Air);
         this.put(BattleState.MonsterElement.Water, BattleState.MonsterElement.Cold);
         this.put(BattleState.MonsterElement.Cold, BattleState.MonsterElement.Earth);
         this.put(BattleState.MonsterElement.Plasma, BattleState.MonsterElement.Poison);
         this.put(BattleState.MonsterElement.Shadow, BattleState.MonsterElement.Plasma);
         this.put(BattleState.MonsterElement.Mech, BattleState.MonsterElement.Shadow);
         this.put(BattleState.MonsterElement.Crystal, BattleState.MonsterElement.Mech);
         this.put(BattleState.MonsterElement.Poison, BattleState.MonsterElement.Crystal);
      }
   };
   static final Map<BattleState.MonsterElement, BattleState.MonsterElement> reverseElementPriorityMap = new HashMap<BattleState.MonsterElement, BattleState.MonsterElement>() {
      private static final long serialVersionUID = 1L;

      {
         this.put(BattleState.MonsterElement.Air, BattleState.MonsterElement.Earth);
         this.put(BattleState.MonsterElement.Plant, BattleState.MonsterElement.Air);
         this.put(BattleState.MonsterElement.Earth, BattleState.MonsterElement.Cold);
         this.put(BattleState.MonsterElement.Water, BattleState.MonsterElement.Plant);
         this.put(BattleState.MonsterElement.Cold, BattleState.MonsterElement.Water);
         this.put(BattleState.MonsterElement.Plasma, BattleState.MonsterElement.Shadow);
         this.put(BattleState.MonsterElement.Shadow, BattleState.MonsterElement.Mech);
         this.put(BattleState.MonsterElement.Mech, BattleState.MonsterElement.Crystal);
         this.put(BattleState.MonsterElement.Crystal, BattleState.MonsterElement.Poison);
         this.put(BattleState.MonsterElement.Poison, BattleState.MonsterElement.Plasma);
      }
   };

   public static BattleState.MonsterElement ParseElement(String str) {
      if (str.equals("air")) {
         return BattleState.MonsterElement.Air;
      } else if (str.equals("plant")) {
         return BattleState.MonsterElement.Plant;
      } else if (str.equals("earth")) {
         return BattleState.MonsterElement.Earth;
      } else if (str.equals("water")) {
         return BattleState.MonsterElement.Water;
      } else if (str.equals("cold")) {
         return BattleState.MonsterElement.Cold;
      } else if (str.equals("plasma")) {
         return BattleState.MonsterElement.Plasma;
      } else if (str.equals("shadow")) {
         return BattleState.MonsterElement.Shadow;
      } else if (str.equals("mech")) {
         return BattleState.MonsterElement.Mech;
      } else if (str.equals("crystal")) {
         return BattleState.MonsterElement.Crystal;
      } else {
         return str.equals("poison") ? BattleState.MonsterElement.Poison : BattleState.MonsterElement.Undefined;
      }
   }

   public static BattleState.MonsterElement GeneToElement(char gene) {
      return geneToElementMap.containsKey(gene) ? (BattleState.MonsterElement)geneToElementMap.get(gene) : BattleState.MonsterElement.Undefined;
   }

   public static BattleState.MonsterElement ElementIsStrongerThan(BattleState.MonsterElement e) {
      return elementPriorityMap.containsKey(e) ? (BattleState.MonsterElement)elementPriorityMap.get(e) : BattleState.MonsterElement.Undefined;
   }

   public static BattleState.MonsterElement ElementIsWeakerThan(BattleState.MonsterElement e) {
      return reverseElementPriorityMap.containsKey(e) ? (BattleState.MonsterElement)reverseElementPriorityMap.get(e) : BattleState.MonsterElement.Undefined;
   }

   public static int GetBattleMonsterStaminaForLevel(int baseStamina, int level) {
      int stamina = (int)((double)baseStamina * Math.pow(1.1D, (double)(level - 1)));
      return stamina;
   }

   public static int GetBattleMonsterPowerForLevel(int basePower, int level) {
      int power = (int)((double)basePower * Math.pow(1.1D, (double)(level - 1)));
      return power;
   }

   public static int GetBattleMonsterActionPowerForLevel(BattleMonsterStatData monster, BattleMonsterActionData action, int level) {
      float power = (float)action.getBaseDamage() * 0.01F;
      return (int)Math.floor((double)(power * (float)GetBattleMonsterPowerForLevel(monster.getBasePower(), level)));
   }

   public static float CalculateElementalMultiplier(BattleState.MonsterElement attackElement, BattlePlayer defender) {
      List<BattleState.MonsterElement> defenderElements = defender.getMonsterElements();
      if (defenderElements.size() == 0) {
         return 1.0F;
      } else {
         float elementalMultiplier = 0.0F;

         for(int i = 0; i < defenderElements.size(); ++i) {
            BattleState.MonsterElement defenderElement = (BattleState.MonsterElement)defenderElements.get(i);
            if (ElementIsStrongerThan(defenderElement) == attackElement) {
               elementalMultiplier += 0.5F;
            } else if (ElementIsWeakerThan(defenderElement) == attackElement) {
               elementalMultiplier += 2.0F;
            } else {
               ++elementalMultiplier;
            }
         }

         elementalMultiplier /= (float)defenderElements.size();
         return elementalMultiplier;
      }
   }

   public BattleState(long seed) {
      this.rand.seed(seed, 721347520444481703L);
      this.teams = new BattleTeam[2];
      this.activePlayers = new BattlePlayer[2];
      this.isTutorial = false;
      this.opponentCanSwap = false;
      this.currentTeam = 0;
   }

   int calculatePotentialDamage() {
      int potentialDamage = 0;
      BattlePlayer activePlayer = this.activePlayers[0];
      BattlePlayer activeOpponent = this.activePlayers[1];

      for(int i = 0; i < activePlayer.actions.size(); ++i) {
         if (!activePlayer.isActionLocked(i)) {
            BattleMonsterActionData action = (BattleMonsterActionData)activePlayer.actions.get(i);
            int damage = GetBattleMonsterActionPowerForLevel(activePlayer.monsterStatData, action, activePlayer.level);
            if (action.isElemental()) {
               BattleState.MonsterElement attackElement = ParseElement(action.getElement());
               damage = (int)((float)damage * CalculateElementalMultiplier(attackElement, activeOpponent));
            }

            if (damage > potentialDamage) {
               potentialDamage = damage;
            }
         }
      }

      return potentialDamage;
   }

   int selectOptimalPlayer() {
      return this.calculatePotentialDamage() > this.activePlayers[1].health ? this.selectHealthiestPlayer() : this.selectStrongestPlayer();
   }

   int selectOptimalAction() {
      BattlePlayer opponent = this.activePlayers[1];
      int optimalAction = -1;
      int mostDamage = 0;

      for(int i = 0; i < opponent.actions.size(); ++i) {
         if (!opponent.isActionLocked(i)) {
            BattleMonsterActionData action = (BattleMonsterActionData)opponent.actions.get(i);
            int damage = GetBattleMonsterActionPowerForLevel(opponent.monsterStatData, action, opponent.level);
            if (action.isElemental()) {
               BattleState.MonsterElement attackElement = ParseElement(action.getElement());
               damage = (int)((float)damage * CalculateElementalMultiplier(attackElement, this.activePlayers[0]));
            }

            if (damage > mostDamage) {
               mostDamage = damage;
               optimalAction = i;
            }
         }
      }

      return optimalAction;
   }

   int selectHealthiestPlayer() {
      int healthiestPlayer = -1;
      int mostHealth = 0;

      for(int i = 0; i < this.teams[1].players.size(); ++i) {
         int health = ((BattlePlayer)this.teams[1].players.get(i)).health;
         if (health >= mostHealth) {
            mostHealth = health;
            healthiestPlayer = i;
         }
      }

      return healthiestPlayer;
   }

   int selectStrongestPlayer() {
      int strongestPlayer = -1;
      int strongestDamage = 0;

      for(int i = 0; i < this.teams[1].players.size(); ++i) {
         BattlePlayer opponent = (BattlePlayer)this.teams[1].players.get(i);
         if (!opponent.isDead()) {
            for(int j = 0; j < opponent.actions.size(); ++j) {
               if (!opponent.isActionLocked(j)) {
                  BattleMonsterActionData action = (BattleMonsterActionData)opponent.actions.get(j);
                  int damage = GetBattleMonsterActionPowerForLevel(opponent.monsterStatData, action, opponent.level);
                  if (action.isElemental()) {
                     BattleState.MonsterElement attackElement = ParseElement(action.getElement());
                     damage = (int)((float)damage * CalculateElementalMultiplier(attackElement, this.activePlayers[0]));
                  }

                  if (damage > strongestDamage) {
                     strongestDamage = damage;
                     strongestPlayer = i;
                  }
               }
            }
         }
      }

      return strongestPlayer;
   }

   public void applyPlayerAction(ISFSObject data) throws Exception {
      if (this.currentTeam != 0) {
         throw new Exception("Wrong Team");
      } else {
         int playerId = data.getInt("p");
         int actionId = data.getInt("a");
         BattleExpectedActionResult expectedResult = BattleExpectedActionResult.CreateFromSfsObject(data);
         if (playerId >= 0 && playerId < this.teams[0].players.size()) {
            this.activePlayers[0] = (BattlePlayer)this.teams[0].players.get(playerId);
            if (this.activePlayers[0].isDead()) {
               throw new Exception("Selected Player is DEAD");
            } else if (this.activePlayers[0].isActionLocked(actionId)) {
               throw new Exception("Selected Action is LOCKED");
            } else if (actionId >= 0 && actionId < this.activePlayers[0].actions.size()) {
               BattleMonsterActionData playerAction = (BattleMonsterActionData)this.activePlayers[0].actions.get(actionId);
               this.applyAction(this.activePlayers[0], this.activePlayers[1], playerAction, expectedResult);
               this.currentTeam = 1;
            } else {
               throw new Exception("Invalid Action Index : " + actionId);
            }
         } else {
            throw new Exception("Invalid Player Index: " + playerId);
         }
      }
   }

   public void applyOpponentAction(ISFSObject data) throws Exception {
      if (this.currentTeam != 1) {
         throw new Exception("Wrong Team");
      } else {
         int expectedPlayerId = data == null ? -1 : data.getInt("p");
         int expectedActionId = data == null ? -1 : data.getInt("a");
         BattleExpectedActionResult expectedResult = BattleExpectedActionResult.CreateFromSfsObject(data);
         boolean hasSwapped = false;
         int selectedPlayerId = -1;

         int availableActions;
         for(availableActions = 0; availableActions < this.teams[1].players.size(); ++availableActions) {
            if (this.activePlayers[1] == this.teams[1].players.get(availableActions)) {
               selectedPlayerId = availableActions;
               break;
            }
         }

         if (this.activePlayers[1].isDead()) {
            if (this.opponentCanSwap) {
               selectedPlayerId = this.selectOptimalPlayer();
            } else {
               for(availableActions = 0; availableActions < this.teams[1].players.size(); ++availableActions) {
                  if (!((BattlePlayer)this.teams[1].players.get(availableActions)).isDead()) {
                     selectedPlayerId = availableActions;
                     break;
                  }
               }
            }

            hasSwapped = true;
            if (selectedPlayerId == -1) {
               this.activePlayers[1] = null;
            } else {
               this.activePlayers[1] = (BattlePlayer)this.teams[1].players.get(selectedPlayerId);
            }
         }

         if (this.activePlayers[1] != null) {
            if (this.opponentCanSwap && !hasSwapped) {
               availableActions = this.selectOptimalPlayer();
               if (availableActions != -1) {
                  hasSwapped = true;
                  this.activePlayers[1] = (BattlePlayer)this.teams[1].players.get(availableActions);
                  selectedPlayerId = availableActions;
               }
            }

            availableActions = 0;
            int prefAction = -1;
            int selectedActionId = true;

            for(int i = 0; i < this.activePlayers[1].actions.size(); ++i) {
               if (!this.activePlayers[1].isActionLocked(i)) {
                  if (!((BattleMonsterActionData)this.activePlayers[1].actions.get(i)).isElemental()) {
                     prefAction = i;
                  }

                  ++availableActions;
               }
            }

            BattleMonsterActionData opponentAction = null;
            int selectedActionId;
            if (this.isTutorial && prefAction != -1) {
               selectedActionId = prefAction;
               opponentAction = (BattleMonsterActionData)this.activePlayers[1].actions.get(prefAction);
            } else if (this.pickOptimalPercentage > 0.0F && this.rand.nextFloat() <= this.pickOptimalPercentage) {
               selectedActionId = this.selectOptimalAction();
               opponentAction = (BattleMonsterActionData)this.activePlayers[1].actions.get(selectedActionId);
            } else {
               int randomAction = this.rand.nextInt(availableActions);
               selectedActionId = randomAction;
               opponentAction = (BattleMonsterActionData)this.activePlayers[1].actions.get(randomAction);
            }

            if (expectedPlayerId != -1 && expectedPlayerId != selectedPlayerId) {
            }

            if (expectedActionId != -1 && expectedActionId != selectedActionId) {
            }

            this.applyAction(this.activePlayers[1], this.activePlayers[0], opponentAction, expectedResult);
            this.currentTeam = 0;
         }

      }
   }

   public void applyAction(BattlePlayer attacker, BattlePlayer defender, BattleMonsterActionData action, BattleExpectedActionResult expectedResult) throws Exception {
      if (attacker == null) {
         throw new Exception("Null Attacker");
      } else if (defender == null) {
         throw new Exception("Null Defender");
      } else if (action == null) {
         throw new Exception("Null Action");
      } else {
         BattleActionResult battleActionResult = new BattleActionResult();
         int damage = GetBattleMonsterActionPowerForLevel(attacker.monsterStatData, action, attacker.level);
         if (!this.isTutorial) {
            float randf = this.rand.nextFloat();
            damage = (int)((float)damage + (float)damage * (randf * 0.5F - 0.25F));
         }

         if (action.isElemental()) {
            BattleState.MonsterElement attackElement = ParseElement(action.getElement());
            float elementalMultiplier = CalculateElementalMultiplier(attackElement, defender);
            damage = Math.max(1, (int)((float)damage * elementalMultiplier));
         }

         battleActionResult.damage = damage;
         attacker.onAction(this, defender, action, battleActionResult);
         if (expectedResult.getExpectedSelfHeal() > 0 && battleActionResult.selfHeal != expectedResult.getExpectedSelfHeal() && Math.abs(battleActionResult.selfHeal - expectedResult.getExpectedSelfHeal()) < GameSettings.get("BATTLE_FUDGE_DAMAGE_THRESHOLD", 5)) {
            battleActionResult.selfHeal = expectedResult.getExpectedSelfHeal();
         }

         if (expectedResult.getExpectedDamage() != -1 && battleActionResult.damage != expectedResult.getExpectedDamage() && Math.abs(battleActionResult.damage - expectedResult.getExpectedDamage()) < GameSettings.get("BATTLE_FUDGE_DAMAGE_THRESHOLD", 5)) {
            battleActionResult.damage = expectedResult.getExpectedDamage();
         }

         defender.applyDamage(damage);
         attacker.onEndTurn(this);
      }
   }

   public boolean playerDidWin() {
      int minHealth = GameSettings.get("BATTLE_VERIFICATION_MIN_HEALTH", 10);
      return this.teams[0].remainingHealth() > 0 && this.teams[1].remainingHealth() <= minHealth;
   }

   public static enum MonsterElement {
      Undefined,
      Air,
      Plant,
      Earth,
      Water,
      Cold,
      Plasma,
      Shadow,
      Mech,
      Crystal,
      Poison;
   }
}
