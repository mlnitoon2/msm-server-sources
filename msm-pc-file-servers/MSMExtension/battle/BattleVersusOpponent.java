package com.bigbluebubble.mysingingmonsters.battle;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class BattleVersusOpponent {
   public long playerId;
   public String name;
   public int portraitType;
   public String portraitInfo;
   public BattleOpponents opponents;

   public BattleVersusOpponent() {
      this.playerId = 0L;
      this.name = "V O I D C O R N";
      this.portraitType = 0;
      this.portraitInfo = "12";
      this.opponents = new BattleOpponents();
   }

   public BattleVersusOpponent(ISFSObject data) {
      if (data.containsKey("user_id")) {
         this.playerId = data.getLong("user_id");
      } else {
         this.playerId = 0L;
      }

      if (data.containsKey("display_name")) {
         this.name = data.getUtfString("display_name");
      } else {
         this.name = "V O I D C O R N";
      }

      if (data.containsKey("pp_type")) {
         this.portraitType = data.getInt("pp_type");
      } else {
         this.portraitType = 0;
      }

      if (data.containsKey("pp_info")) {
         this.portraitInfo = data.getUtfString("pp_info");
      } else {
         this.portraitInfo = "12";
      }

      this.opponents = new BattleOpponents(SFSArray.newFromJsonData(data.getUtfString("loadout")));
   }

   public ISFSObject toSFSObject() {
      ISFSObject obj = new SFSObject();
      obj.putLong("player_id", this.playerId);
      obj.putUtfString("name", this.name);
      obj.putInt("pp_type", this.portraitType);
      obj.putUtfString("pp_info", this.portraitInfo);
      obj.putSFSArray("loadout", this.opponents.toSFSArray());
      return obj;
   }
}
