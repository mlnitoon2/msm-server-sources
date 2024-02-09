package com.bigbluebubble.mysingingmonsters.costumes;

import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.mysingingmonsters.player.PlayerIsland;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticData;
import com.smartfoxserver.v2.entities.data.ISFSObject;

public class CostumeData extends StaticData {
   public static final String ID_KEY = "id";
   public static final String MONSTER_KEY = "monster_id";
   public static final String NAME_KEY = "name";
   public static final String DIAMOND_COST_KEY = "diamondCost";
   public static final String MEDAL_COST_KEY = "medalCost";
   public static final String SELL_COST_KEY = "sellCost";
   public static final String ETH_SELL_COST_KEY = "etherealSellCost";
   public static final String UNLOCK_LEVEL_KEY = "unlock_level";
   public static final String UNLOCK_TELEPORT_KEY = "unlock_teleport";
   public static final String IGNORE_LOCKS_KEY = "ignore_locks";
   public static final String ACTION_OVERRIDE_KEY = "action";
   public static final String HIDDEN_KEY = "hidden";
   public static final String ALWAYS_VISIBLE_KEY = "always_visible";
   public static final String BREED_CHANCE_KEY = "breed_chance";

   public CostumeData(ISFSObject data) {
      super(data);
   }

   public int getId() {
      return this.data.getInt("id");
   }

   public int getMonster() {
      return this.data.getInt("monster_id");
   }

   public boolean hidden() {
      return this.data.getInt("hidden") != 0;
   }

   public boolean alwaysVisible() {
      return this.data.getInt("always_visible") != 0;
   }

   public String getName() {
      return this.data.getUtfString("name");
   }

   public int getDiamondCost() {
      return this.data.containsKey("diamondCost") ? this.data.getInt("diamondCost") : 0;
   }

   public int getMedalCost() {
      return this.data.containsKey("medalCost") ? this.data.getInt("medalCost") : 0;
   }

   public int getSecondaryCurrencySellCost(PlayerIsland pi) {
      return !pi.isEtherealIsland() ? this.getCoinSellCost() : this.getEthSellCost();
   }

   private int getCoinSellCost() {
      return this.data.containsKey("sellCost") ? this.data.getInt("sellCost") : 0;
   }

   private int getEthSellCost() {
      return this.data.containsKey("etherealSellCost") ? this.data.getInt("etherealSellCost") : 0;
   }

   public int unlockLevel() {
      return this.data.containsKey("unlock_level") ? this.data.getInt("unlock_level") : 0;
   }

   public boolean unlocksOnTeleport() {
      if (this.data.containsKey("unlock_teleport")) {
         return this.data.getInt("unlock_teleport") != 0;
      } else {
         return false;
      }
   }

   public boolean ignoreLocks() {
      if (this.data.containsKey("ignore_locks")) {
         return this.data.getInt("ignore_locks") != 0;
      } else {
         return false;
      }
   }

   public int action() {
      return this.data.containsKey("action") ? this.data.getInt("action") : 0;
   }

   public double getBreedChance() {
      return this.data.containsKey("breed_chance") ? SFSHelpers.getDouble("breed_chance", this.data) : 0.0D;
   }
}
