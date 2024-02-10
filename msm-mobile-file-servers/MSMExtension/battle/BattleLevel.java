package com.bigbluebubble.mysingingmonsters.battle;

import com.bigbluebubble.mysingingmonsters.staticdata.StaticData;
import com.smartfoxserver.v2.entities.data.ISFSObject;

public class BattleLevel extends StaticData {
   public BattleLevel(ISFSObject levelData) {
      super(levelData);
   }

   public int getLevel() {
      return this.data.getInt("level");
   }

   public int getXp() {
      return this.data.getInt("xp");
   }
}
