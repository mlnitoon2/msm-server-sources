package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class Structure extends Entity {
   public static final String ID_KEY = "structure_id";
   protected static final String STRUCTURE_TYPE_KEY = "structure_type";
   public static final String EXTRA_KEY = "extra";
   public static final String CASTLE_BEDS_KEY = "beds";
   public static final String MINE_DIAMONDS_KEY = "diamonds";
   public static final String MINING_TIME_KEY = "time";
   public static final String WAREHOUSE_CAPACITY_KEY = "capacity";
   public static final String HOTEL_CAPACITY_KEY = "capacity";
   public static final String BREEDING_SPEED_MODIFIER_KEY = "speed_mod";
   public static final String NURSERY_SPEED_MODIFIER_KEY = "speed_mod";
   public static final String BAKERY_FOOD_OPTIONS_KEY = "food_options";
   public static final String BAKERY_FOOD_OPTION_ID_KEY = "id";
   public static final String BAKERY_FOOD_AMOUNT_KEY = "food";
   public static final String BAKERY_FOOD_COST_KEY = "cost";
   public static final String BAKERY_FOOD_TIME_KEY = "time";
   public static final String BAKERY_FOOD_XP_KEY = "xp";
   public static final String BAKERY_FOOD_LABEL_KEY = "label";
   public static final String NURSERY_ID = "nursery";
   public static final String BREEDING_ID = "breeding";
   public static final String HAPPY_ID = "happiness_tree";
   public static final String BAKERY_ID = "bakery";
   public static final String CASTLE_ID = "castle";
   public static final String MINE_ID = "mine";
   public static final String DECORATION_ID = "decoration";
   public static final String OBSTACLE_ID = "obstacle";
   public static final String WAREHOUSE_ID = "warehouse";
   public static final String HOTEL_ID = "hotel";
   public static final String TORCH_ID = "torch";
   public static final String BUDDY_ID = "buddy";
   public static final String FUZER_ID = "fuzer";
   public static final String TIMEMACHINE_ID = "time_machine";
   public static final String RECORDINGSTUDIO_ID = "recording_studio";
   public static final String BATTLEGYM_ID = "battle_gym";
   public static final String CRUCIBLE_ID = "crucible";
   public static final String AWAKENER_ID = "awakener";
   public static final String ATTUNER_ID = "attuner";
   public static final String SYNTHESIZER_ID = "synthesizer";
   public static final String BATTLEGYM_CAPACITY_KEY = "capacity";
   public static final String BATTLEGYM_TRAINING_COST_KEY = "cost";
   public static final String BATTLEGYM_TRAINING_TIME_KEY = "time";
   public static final String UPGRADES_TO_ID = "upgrades_to";
   public static final String ALLOWED_ON_ISLAND_ID = "allowed_on_island";
   public static final String BATTLE_LEVEL_KEY = "battle_level";
   protected ISFSObject extra;
   protected ISFSArray allowedOnIsland = null;

   public Structure(ISFSObject structureData) {
      super(structureData);
      this.requirements = new SFSArray();
      this.extra = new SFSObject();
      this.allowedOnIsland = new SFSArray();
      String requirementString = this.data.getUtfString("requirements");
      if (requirementString != null && requirementString.length() > 0) {
         this.requirements = SFSArray.newFromJsonData(requirementString);
         this.data.putSFSArray("requirements", this.requirements);
      }

      String graphicString = this.data.getUtfString("graphic");
      if (graphicString != null && graphicString.length() > 0) {
         this.data.putSFSObject("graphic", SFSObject.newFromJsonData(graphicString));
      }

      String extraString = this.data.getUtfString("extra");
      if (extraString != null && extraString.length() > 0) {
         try {
            this.extra = SFSObject.newFromJsonData(extraString);
            this.data.putSFSObject("extra", this.extra);
         } catch (Exception var7) {
            Logger.trace(var7, "structureData : " + structureData.getDump());
         }
      }

      String keywordsString = this.data.getUtfString("keywords");
      if (keywordsString != null && requirementString.length() > 0) {
         this.data.putUtfString("keywords", keywordsString);
      }

      String allowedOnIslandString = this.data.getUtfString("allowed_on_island");
      if (allowedOnIslandString != null && allowedOnIslandString.length() > 0) {
         this.allowedOnIsland = SFSArray.newFromJsonData(allowedOnIslandString);
         this.data.putUtfString("allowed_on_island", this.allowedOnIsland.toJson());
      }

      if (this.isHotel() || this.isWarehouse()) {
         this.data.putFloat("ETHEREAL_CAPACITY_MULTIPLIER", GameSettings.getFloat("ETHEREAL_CAPACITY_MULTIPLIER"));
      }

   }

   public ISFSObject getExtra() {
      return this.data.getSFSObject("extra");
   }

   public String getType() {
      return this.data.getUtfString("structure_type");
   }

   public int getXp() {
      return this.data.getInt("xp");
   }

   public boolean isNursery() {
      return this.getType().equals("nursery");
   }

   public boolean isHappyTree() {
      return this.getType().equals("happiness_tree");
   }

   public boolean isBreeding() {
      return this.getType().equals("breeding");
   }

   public boolean isCrucible() {
      return this.getType().equals("crucible");
   }

   public boolean isAttuner() {
      return this.getType().equals("attuner");
   }

   public boolean isSynthesizer() {
      return this.getType().equals("synthesizer");
   }

   public boolean isBakery() {
      return this.getType().equals("bakery");
   }

   public boolean isCastle() {
      return this.getType().equals("castle");
   }

   public boolean isMine() {
      return this.getType().equals("mine");
   }

   public boolean isDecoration() {
      return this.getType().equals("decoration");
   }

   public boolean isObstacle() {
      return this.getType().equals("obstacle");
   }

   public boolean isHotel() {
      return this.getType().equals("hotel");
   }

   public boolean isWarehouse() {
      return this.getType().equals("warehouse");
   }

   public boolean isTimeMachine() {
      return this.getType().equals("time_machine");
   }

   public boolean isRecordingStudio() {
      return this.getType().equals("recording_studio");
   }

   public boolean isTorch() {
      return this.getType().equals("torch");
   }

   public boolean isBuddy() {
      return this.getType().equals("buddy");
   }

   public boolean isFuzer() {
      return this.getType().equals("fuzer");
   }

   public boolean isBattleGym() {
      return this.getType().equals("battle_gym");
   }

   public boolean isAwakener() {
      return this.getType().equals("awakener");
   }

   public int getID() {
      return this.data.getInt("structure_id");
   }

   public int getUpgradesTo() {
      return this.getData().getInt("upgrades_to");
   }

   public int getBattleLevelUnlocked() {
      return this.getData().getInt("battle_level");
   }

   public ISFSArray getAllowedIslands() {
      return this.allowedOnIsland;
   }

   public boolean allowedOnIsland(Island island) {
      if (this.isAwakener()) {
         return this.allowedOnIsland.size() == 0 || this.allowedOnIsland.contains(island.getID());
      } else {
         return this.allowedOnIsland.size() == 0 || this.allowedOnIsland.contains(island.getType());
      }
   }
}
