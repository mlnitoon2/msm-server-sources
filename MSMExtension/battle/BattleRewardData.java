package com.bigbluebubble.mysingingmonsters.battle;

import com.smartfoxserver.v2.entities.data.ISFSObject;

public final class BattleRewardData {
   private static final String COINS_KEY = "coins";
   private static final String DIAMONDS_KEY = "diamonds";
   private static final String MEDALS_KEY = "medals";
   private static final String XP_KEY = "xp";
   private static final String COSTUME_KEY = "costumeId";
   private static final String TROPHY_KEY = "trophy";
   private static final String TROPHY_ENTITY_KEY = "trophyEntityId";
   private static final String FOOD_KEY = "food";
   private static final String STARPOWER_KEY = "starpower";
   private static final String RELICS_KEY = "relics";
   private static final String KEYS_KEY = "keys";
   protected ISFSObject data;

   public BattleRewardData(ISFSObject rewardData) {
      this.data = rewardData;
      if (this.data.containsKey("trophy")) {
         this.data.putInt("trophyEntityId", BattleTrophyLookup.getEntityIdForTrophy(this.data.getInt("trophy")));
      }

   }

   public int coins() {
      return this.data.containsKey("coins") ? this.data.getInt("coins") : 0;
   }

   public int diamonds() {
      return this.data.containsKey("diamonds") ? this.data.getInt("diamonds") : 0;
   }

   public int medals() {
      return this.data.containsKey("medals") ? this.data.getInt("medals") : 0;
   }

   public int xp() {
      return this.data.containsKey("xp") ? this.data.getInt("xp") : 0;
   }

   public int costume() {
      return this.data.containsKey("costumeId") ? this.data.getInt("costumeId") : 0;
   }

   public int trophy() {
      return this.data.containsKey("trophy") ? this.data.getInt("trophy") : 0;
   }

   public int food() {
      return this.data.containsKey("food") ? this.data.getInt("food") : 0;
   }

   public int starpower() {
      return this.data.containsKey("starpower") ? this.data.getInt("starpower") : 0;
   }

   public int relics() {
      return this.data.containsKey("relics") ? this.data.getInt("relics") : 0;
   }

   public int keys() {
      return this.data.containsKey("keys") ? this.data.getInt("keys") : 0;
   }
}
