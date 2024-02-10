package com.bigbluebubble.mysingingmonsters.battle.verification;

import com.bigbluebubble.mysingingmonsters.battle.BattleMonsterActionData;

public class BattleBuff implements IBattleBuff {
   public BuffType getType() {
      return BuffType.BUFF_INVALID;
   }

   public boolean isExpired() {
      return true;
   }

   public void overwrite(IBattleBuff buff) {
   }

   public void onAttack(BattleState battleSystem, BattlePlayer attacker, BattlePlayer defender, BattleMonsterActionData action, BattleActionResult actionResult) {
   }
}
