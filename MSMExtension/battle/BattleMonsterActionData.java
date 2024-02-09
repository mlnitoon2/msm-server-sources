package com.bigbluebubble.mysingingmonsters.battle;

import com.bigbluebubble.mysingingmonsters.staticdata.StaticData;
import com.smartfoxserver.v2.entities.data.ISFSObject;

public class BattleMonsterActionData extends StaticData {
   public static final String ID_KEY = "id";
   public static final String BASE_DAMAGE_KEY = "base_damage";
   public static final String ELEMENT_KEY = "element";
   public static final String REPLACES_KEY = "replaces";

   public BattleMonsterActionData(ISFSObject data) {
      super(data);
   }

   public int getId() {
      return this.data.getInt("id");
   }

   public int replaces() {
      return this.data.containsKey("replaces") ? this.data.getInt("replaces") : -1;
   }

   public int getBaseDamage() {
      return this.data.getInt("base_damage");
   }

   public boolean isElemental() {
      return this.data.containsKey("element") && this.data.getUtfString("element").length() > 0;
   }

   public String getElement() {
      return this.data.containsKey("element") ? this.data.getUtfString("element") : "";
   }
}
