package com.bigbluebubble.mysingingmonsters.battle;

import com.bigbluebubble.mysingingmonsters.staticdata.StaticData;
import com.smartfoxserver.v2.entities.data.ISFSObject;

public class BattleMusicData extends StaticData {
   public BattleMusicData(ISFSObject data) {
      super(data);
   }

   public int getId() {
      return this.data.getInt("id");
   }

   public int getCost() {
      return this.data.getInt("cost");
   }

   public int getUnlockLevel() {
      return this.data.getInt("unlock_level");
   }
}
