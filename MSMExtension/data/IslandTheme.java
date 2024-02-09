package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticData;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class IslandTheme extends StaticData {
   private static final String THEME_ID = "theme_id";
   private static final String NAME = "name";
   private static final String ISLAND = "island";
   private static final String GRAPHIC = "graphic";
   private static final String OBSTACLE_ROCKS = "rocks";
   private static final String OBSTACLE_TREES = "trees";
   private static final String COST_COINS = "cost_coins";
   private static final String COST_SHARDS = "cost_eth_currency";
   private static final String COST_KEYS = "cost_keys";
   private static final String COST_RELICS = "cost_relics";
   private static final String COST_STARPOWER = "cost_starpower";
   private static final String COST_DIAMONDS = "cost_diamonds";
   private static final String MODIFIERS = "modifiers";
   private static final String VIEW_IN_MARKET = "view_in_market";
   private static final String VERSION = "version";
   public static final String NURSERY_SPEED_MODIFIER_KEY = "nursery_speed_mod";
   public static final String COIN_PRODUCTION_MODIFIER_KEY = "coin_production_mod";
   public static final String WISHING_TORCH_MODIFIER_KEY = "wishing_torch_mod";
   public static final String BREED_SPEED_MODIFIER_KEY = "breed_speed_mod";
   public static final String BAKERY_SPEED_MODIFIER_KEY = "bakery_speed_mod";
   public static final String CRUCIBLE_EVOLVE_SPEED_MODIFIER_KEY = "crucible_speed_mod";

   public IslandTheme(ISFSObject islandThemeData) {
      super(islandThemeData);
      if (this.data.containsKey("version")) {
         this.minVersion = new VersionInfo(this.data.getUtfString("version"));
      }

      String graphicString = this.data.getUtfString("graphic");
      if (graphicString != null && graphicString.length() > 0) {
         this.data.putSFSObject("graphic", SFSObject.newFromJsonData(graphicString));
      }

      String rockString = this.data.getUtfString("rocks");
      if (rockString != null && rockString.length() > 0) {
         this.data.putSFSObject("rocks", SFSObject.newFromJsonData(rockString));
      }

      String treeString = this.data.getUtfString("trees");
      if (treeString != null && treeString.length() > 0) {
         this.data.putSFSObject("trees", SFSObject.newFromJsonData(treeString));
      }

      String modifierString = this.data.getUtfString("modifiers");
      if (modifierString != null && modifierString.length() > 0) {
         this.data.putSFSObject("modifiers", SFSObject.newFromJsonData(modifierString));
      }

   }

   public int getId() {
      return this.data.getInt("theme_id");
   }

   public String getName() {
      return this.data.getUtfString("name");
   }

   public int getIsland() {
      return this.data.getInt("island");
   }

   public int getCoinCost() {
      return this.data.getInt("cost_coins");
   }

   public int getShardCost() {
      return this.data.getInt("cost_eth_currency");
   }

   public int getKeyCost() {
      return this.data.getInt("cost_keys");
   }

   public int getRelicCost() {
      return this.data.getInt("cost_relics");
   }

   public int getStarpowerCost() {
      return this.data.getInt("cost_starpower");
   }

   public int getDiamondCost() {
      return this.data.getInt("cost_diamonds");
   }

   public boolean getViewInMarket() {
      return this.data.getInt("view_in_market") != 0;
   }

   public ISFSObject getModifiers() {
      return this.data.getSFSObject("modifiers");
   }
}
