package com.bigbluebubble.mysingingmonsters.battle.verification;

import com.smartfoxserver.v2.entities.data.ISFSObject;

public class BattleExpectedActionResult {
   private int damage_;
   private int selfHeal_;

   public int getExpectedDamage() {
      return this.damage_;
   }

   public int getExpectedSelfHeal() {
      return this.selfHeal_;
   }

   private BattleExpectedActionResult(int damage, int selfHeal) {
      this.damage_ = damage;
      this.selfHeal_ = selfHeal;
   }

   public static BattleExpectedActionResult CreateFromSfsObject(ISFSObject data) {
      int expectedDamage = data.containsKey("d") ? data.getInt("d") : -1;
      int expectedSelfHeal = data.containsKey("h") ? data.getInt("h") : 0;
      return new BattleExpectedActionResult(expectedDamage, expectedSelfHeal);
   }
}
