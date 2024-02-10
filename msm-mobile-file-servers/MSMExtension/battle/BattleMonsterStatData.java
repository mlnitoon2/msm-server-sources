package com.bigbluebubble.mysingingmonsters.battle;

import com.bigbluebubble.mysingingmonsters.staticdata.StaticData;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BattleMonsterStatData extends StaticData {
   public static final String ID_KEY = "monster_id";
   public static final String BASE_POWER_KEY = "base_power";
   public static final String BASE_STAMINA_KEY = "base_stamina";
   public static final String ACTIONS_KEY = "actions";

   public BattleMonsterStatData(ISFSObject data) {
      super(data);
   }

   public int getMonsterId() {
      return this.data.getInt("monster_id");
   }

   public int getBasePower() {
      return this.data.getInt("base_power");
   }

   public int getBaseStamina() {
      return this.data.getInt("base_stamina");
   }

   public List<BattleMonsterActionData> getActions() {
      List<BattleMonsterActionData> actions = new ArrayList();
      String actionString = this.data.getUtfString("actions");
      StringTokenizer tokenizer = new StringTokenizer(actionString, "[],");

      while(tokenizer.hasMoreTokens()) {
         int actionId = Integer.parseInt(tokenizer.nextToken());
         actions.add(BattleMonsterActionLookup.get(actionId));
      }

      return actions;
   }
}
