package com.bigbluebubble.mysingingmonsters.battle.verification;

import com.bigbluebubble.mysingingmonsters.battle.BattleMonsterActionData;

public interface IBattleBuff {
   BuffType getType();

   boolean isExpired();

   void overwrite(IBattleBuff var1);

   void onAttack(BattleState var1, BattlePlayer var2, BattlePlayer var3, BattleMonsterActionData var4, BattleActionResult var5);
}
