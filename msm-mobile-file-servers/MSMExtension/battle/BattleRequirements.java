package com.bigbluebubble.mysingingmonsters.battle;

import com.bigbluebubble.mysingingmonsters.data.EggRequirements;
import com.bigbluebubble.mysingingmonsters.data.Monster;
import com.bigbluebubble.mysingingmonsters.player.PlayerMonster;
import com.smartfoxserver.v2.entities.data.ISFSObject;

public final class BattleRequirements extends EggRequirements {
   public static final String AT_LEAST_LEVEL_KEY = "AtLeastLevel";
   int atLeastLevel = 0;

   protected ISFSObject data() {
      return this.data;
   }

   public BattleRequirements(ISFSObject data) {
      super(data);
      if (data.containsKey("AtLeastLevel")) {
         this.atLeastLevel = data.getInt("AtLeastLevel");
      }

   }

   public boolean evaluate(PlayerMonster m) {
      int monsterLevel = 0;
      if (m != null) {
         monsterLevel = m.getLevel();
      }

      boolean condition;
      if (this.not) {
         condition = monsterLevel < this.atLeastLevel && !super.evaluate(m);
         return condition;
      } else {
         condition = monsterLevel >= this.atLeastLevel && super.evaluate(m);
         return condition;
      }
   }

   public boolean evaluate(Monster m) {
      return this.atLeastLevel != 0 ? false : super.evaluate(m);
   }
}
