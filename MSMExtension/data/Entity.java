package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.EntityAvailabilityEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.EntitySalesEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.StarAvailabilityEvent;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.bigbluebubble.mysingingmonsters.player.PlayerIsland;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticData;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;

public class Entity extends StaticData {
   public static final String REQUIREMENTS_KEY = "requirements";
   public static final String GRAPHIC_KEY = "graphic";
   public static final String SFX_KEY = "sfx";
   public static final String KEYWORDS_KEY = "keywords";
   public static final String LEVEL_KEY = "level";
   public static final String XP_KEY = "xp";
   public static final String ENTITY_ID_KEY = "entity_id";
   public static final String VIEW_MARKET_KEY = "view_in_market";
   public static final String VIEW_STARMARKET_KEY = "view_in_starmarket";
   public static final String PREMIUM_KEY = "premium";
   protected static final String ENTITY_NAME = "name";
   protected static final String COMMON_NAME = "common_name";
   public static final String COST_COINS_KEY = "cost_coins";
   public static final String COST_DIAMONDS_KEY = "cost_diamonds";
   public static final String COST_ETH_KEY = "cost_eth_currency";
   public static final String COST_STARPOWER_KEY = "cost_starpower";
   public static final String COST_KEYS_KEY = "cost_keys";
   public static final String COST_RELICS_KEY = "cost_relics";
   public static final String COST_MEDALS_KEY = "cost_medals";
   public static final String COST_SALE_KEY = "cost_sale";
   public static final String BUILD_TIME_KEY = "build_time";
   public static final String MIN_VER_KEY = "min_server_version";
   protected SFSArray requirements;
   protected int buildTimeMs = 0;

   public Entity(ISFSObject d) {
      super(d);
      this.data = d;
      this.buildTimeMs = this.data.getInt("build_time") * 1000;
      if (this.data.containsKey("min_server_version")) {
         this.minVersion = new VersionInfo(this.data.getUtfString("min_server_version"));
      } else {
         this.minVersion = new VersionInfo(0, 0, 0);
      }

      if (VersionData.Instance().getMinClientVersionFromServerVersion(this.minVersion) == null) {
         Logger.trace(LogLevel.WARN, "\n============================================================================================================================\nERROR!! Entity " + this.getEntityId() + " has unsupported server version " + this.minVersion + "... Please add new server versions to the versioning table.\n============================================================================================================================\n");
      }

   }

   public int getEntityId() {
      return this.data.getInt("entity_id");
   }

   public ISFSArray getRequirements() {
      return this.requirements;
   }

   public int getBuildTimeMs() {
      return this.buildTimeMs;
   }

   public int ethSellingPrice(Player player, int curIslandType) {
      float price = (float)this.getCostEth(curIslandType);
      if (EntitySalesEvent.hasTimedEventNow(this, player, curIslandType)) {
         try {
            price = (float)EntitySalesEvent.getTimedEventSaleCost(this, player, Player.CurrencyType.Ethereal, curIslandType);
         } catch (Exception var6) {
            Logger.trace(var6);
         }
      }

      double sellingPercentage;
      if (curIslandType != 22) {
         sellingPercentage = (double)GameSettings.getFloat("USER_SELLING_PERCENTAGE");
      } else {
         sellingPercentage = (double)GameSettings.getFloat("USER_VESSEL_RELIC_TRADE_PERCENT");
      }

      return (int)((double)price * sellingPercentage);
   }

   public int coinSellingPrice(Player player, int curIslandType) {
      float price = (float)this.getCostCoins(curIslandType);
      if (EntitySalesEvent.hasTimedEventNow(this, player, curIslandType)) {
         try {
            price = (float)EntitySalesEvent.getTimedEventSaleCost(this, player, Player.CurrencyType.Coins, curIslandType);
         } catch (Exception var5) {
            Logger.trace(var5);
         }
      }

      return (int)(price * GameSettings.getFloat("USER_SELLING_PERCENTAGE"));
   }

   public int getSecondaryCurrencyCost(Player player, PlayerIsland island, boolean withSales, boolean inRelics) {
      int price;
      if (inRelics) {
         price = this.getCostRelics(island.getType());
         if (withSales && EntitySalesEvent.hasTimedEventNow(this, player, island.getType())) {
            try {
               price = EntitySalesEvent.getTimedEventSaleCost(this, player, Player.CurrencyType.Relics, island.getType());
            } catch (Exception var7) {
               Logger.trace(var7);
            }
         }

         return price;
      } else if (island.isEtherealIsland()) {
         price = this.getCostEth(island.getType());
         if (withSales && EntitySalesEvent.hasTimedEventNow(this, player, island.getType())) {
            try {
               price = EntitySalesEvent.getTimedEventSaleCost(this, player, Player.CurrencyType.Ethereal, island.getType());
            } catch (Exception var8) {
               Logger.trace(var8);
            }
         }

         return price;
      } else {
         price = this.getCostCoins(island.getType());
         if (withSales && EntitySalesEvent.hasTimedEventNow(this, player, island.getType())) {
            try {
               price = EntitySalesEvent.getTimedEventSaleCost(this, player, Player.CurrencyType.Coins, island.getType());
            } catch (Exception var9) {
               Logger.trace(var9);
            }
         }

         return price;
      }
   }

   public int getCurrencyCost(Player.CurrencyType c, int curIslandType) {
      if (!EntityAltCostLookup.hasAltCost(this.getEntityId(), curIslandType)) {
         return this.data.getInt(c.getEntityCostKey());
      } else {
         switch(c) {
         case Keys:
            return EntityAltCostLookup.get(this.getEntityId(), curIslandType).costKeys;
         case Relics:
            return EntityAltCostLookup.get(this.getEntityId(), curIslandType).costRelics;
         case Starpower:
            return EntityAltCostLookup.get(this.getEntityId(), curIslandType).costStarpower;
         case Diamonds:
            return EntityAltCostLookup.get(this.getEntityId(), curIslandType).costDiamonds;
         case Coins:
            return EntityAltCostLookup.get(this.getEntityId(), curIslandType).costCoins;
         case Ethereal:
            return EntityAltCostLookup.get(this.getEntityId(), curIslandType).costEth;
         default:
            return 0;
         }
      }
   }

   public int getCostCoins(int curIslandType) {
      return EntityAltCostLookup.hasAltCost(this.getEntityId(), curIslandType) ? EntityAltCostLookup.get(this.getEntityId(), curIslandType).costCoins : this.data.getInt("cost_coins");
   }

   public int getCostEth(int curIslandType) {
      return EntityAltCostLookup.hasAltCost(this.getEntityId(), curIslandType) ? EntityAltCostLookup.get(this.getEntityId(), curIslandType).costEth : this.data.getInt("cost_eth_currency");
   }

   public int getCostDiamonds(int curIslandType) {
      return EntityAltCostLookup.hasAltCost(this.getEntityId(), curIslandType) ? EntityAltCostLookup.get(this.getEntityId(), curIslandType).costDiamonds : this.data.getInt("cost_diamonds");
   }

   public int getCostKeys(int curIslandType) {
      return EntityAltCostLookup.hasAltCost(this.getEntityId(), curIslandType) ? EntityAltCostLookup.get(this.getEntityId(), curIslandType).costKeys : this.data.getInt("cost_keys");
   }

   public int getCostStarpower(int curIslandType) {
      return EntityAltCostLookup.hasAltCost(this.getEntityId(), curIslandType) ? EntityAltCostLookup.get(this.getEntityId(), curIslandType).costStarpower : this.data.getInt("cost_starpower");
   }

   public int getCostRelics(int curIslandType) {
      return EntityAltCostLookup.hasAltCost(this.getEntityId(), curIslandType) ? EntityAltCostLookup.get(this.getEntityId(), curIslandType).costRelics : this.data.getInt("cost_relics");
   }

   public int getCostMedals(int curIslandType) {
      return this.data.getInt("cost_medals");
   }

   public int getXp() {
      return this.data.getInt("xp");
   }

   public int getViewInMarket() {
      return this.data.getInt("view_in_market");
   }

   public int getViewInStarmarket() {
      return this.data.getInt("view_in_starmarket");
   }

   public boolean isPremium() {
      return this.data.getInt("premium") == 1;
   }

   public String getName() {
      return this.getData().getUtfString("name");
   }

   public String commonName() {
      return this.getData().getUtfString("common_name");
   }

   public String toString() {
      return this.data.getDump();
   }

   public boolean isInInventory(Player player) {
      return player.getInventory().hasItem(this.getEntityId());
   }

   public boolean canPurchaseFromStore(boolean admin, Player player, PlayerIsland playerIsland, boolean starpowerPurchase, VersionInfo playerVersion) {
      if (!this.supportedByClient(player.getPlatform(), player.getSubplatform(), playerVersion)) {
         return false;
      } else if (admin) {
         return true;
      } else if (this.isInInventory(player)) {
         return true;
      } else if (!this.allowedOnIsland(IslandLookup.get(playerIsland.getType()))) {
         return false;
      } else if (!starpowerPurchase) {
         return this.getViewInMarket() != 1 && !StoreReplacements.currentlyReplacesAnotherStoreItem(this.getEntityId(), playerIsland) ? EntityAvailabilityEvent.hasTimedEventNow(this, player, playerIsland.getType()) : true;
      } else {
         return this.getViewInStarmarket() == 1 || StarAvailabilityEvent.hasTimedEventNow(this, player, playerIsland.getType());
      }
   }

   public boolean allowedOnIsland(Island island) {
      return true;
   }

   public boolean userAllowedToPurchase(Player player, boolean admin) {
      return !this.isPremium() || player.hasMadePurchase() || admin;
   }

   public int getLevelUnlocked() {
      return this.getData().getInt("level");
   }
}
