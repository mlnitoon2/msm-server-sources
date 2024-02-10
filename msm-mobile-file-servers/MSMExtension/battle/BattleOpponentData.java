package com.bigbluebubble.mysingingmonsters.battle;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public final class BattleOpponentData {
   public static final String ID_KEY = "monsterId";
   public static final String NAME_KEY = "name";
   public static final String LEVEL_KEY = "level";
   public static final String COSTUME_KEY = "costumeId";
   protected ISFSObject data;

   public BattleOpponentData() {
      this.data = new SFSObject();
   }

   public BattleOpponentData(ISFSObject opponentData) {
      this.data = opponentData;
   }

   public int getId() {
      return this.data.getInt("monsterId");
   }

   public void setId(int id) {
      this.data.putInt("monsterId", id);
   }

   public String getName() {
      return this.data.getUtfString("name");
   }

   public void setName(String name) {
      this.data.putUtfString("name", name);
   }

   public int getLevel() {
      return this.data.getInt("level");
   }

   public void setLevel(int level) {
      this.data.putInt("level", level);
   }

   public int getCostume() {
      return this.data.containsKey("costumeId") ? this.data.getInt("costumeId") : 0;
   }

   public void setCostume(int costumeId) {
      if (costumeId == 0) {
         if (this.data.containsKey("costumeId")) {
            this.data.removeElement("costumeId");
         }
      } else {
         this.data.putInt("costumeId", costumeId);
      }

   }

   public ISFSObject toSFSObject() {
      return this.data;
   }
}
