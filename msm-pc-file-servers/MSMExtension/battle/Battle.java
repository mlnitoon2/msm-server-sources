package com.bigbluebubble.mysingingmonsters.battle;

public class Battle {
   public static final int MAX_SLOTS = 3;
   public static final int MAX_BATTLE_LEVEL = 20;

   public static int GetBattleTrainingCost(int monsterId, int monsterLevel) {
      return BattleMonsterTrainingLookup.GetTrainingCostForMonster(monsterId, monsterLevel);
   }

   public static int GetBattleTrainingDuration(int monsterId, int monsterLevel) {
      return BattleMonsterTrainingLookup.GetTrainingDurationForMonster(monsterId, monsterLevel);
   }
}
