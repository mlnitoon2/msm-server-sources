package com.bigbluebubble.mysingingmonsters.battle.verification.buffs;

import com.bigbluebubble.mysingingmonsters.battle.BattleMonsterActionData;
import com.bigbluebubble.mysingingmonsters.battle.verification.BattleActionResult;
import com.bigbluebubble.mysingingmonsters.battle.verification.BattleBuff;
import com.bigbluebubble.mysingingmonsters.battle.verification.BattlePlayer;
import com.bigbluebubble.mysingingmonsters.battle.verification.BattleState;
import com.bigbluebubble.mysingingmonsters.battle.verification.BuffType;

public class BuffEtherealHealing extends BattleBuff {
   private BattlePlayer owner_;
   private float healingPerStack_;
   private int stacks_;
   private float varianceMin_;
   private float varianceMax_;

   public BuffEtherealHealing(BattlePlayer owner, float healingPerStack, int stacks, float varianceMin, float varianceMax) {
      this.owner_ = owner;
      this.healingPerStack_ = healingPerStack;
      this.stacks_ = stacks;
      this.varianceMin_ = varianceMin;
      this.varianceMax_ = varianceMax;
   }

   public BuffType getType() {
      return BuffType.BUFF_ETHEREAL_HEALING;
   }

   public boolean isExpired() {
      return this.stacks_ <= 0;
   }

   public void onAttack(BattleState battleSystem, BattlePlayer attacker, BattlePlayer defender, BattleMonsterActionData action, BattleActionResult actionResult) {
      if (attacker == this.owner_) {
         BattleState.MonsterElement attackElement = BattleState.ParseElement(action.getElement());
         if (this.owner_.hasElement(attackElement)) {
            if (this.stacks_ > 0) {
               float variance = this.varianceMin_ + battleSystem.rand.nextFloat() * (this.varianceMax_ - this.varianceMin_);
               actionResult.selfHeal = (int)((float)this.stacks_ * this.healingPerStack_ * (float)actionResult.damage * variance);
               this.owner_.applyDamage(-actionResult.selfHeal);
               --this.stacks_;
            }

         }
      }
   }
}
