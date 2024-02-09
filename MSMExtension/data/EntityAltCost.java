package com.bigbluebubble.mysingingmonsters.data;

import com.smartfoxserver.v2.entities.data.ISFSObject;

public class EntityAltCost {
   public int entityId;
   public int islandId;
   public int costCoins;
   public int costEth;
   public int costDiamonds;
   public int costKeys;
   public int costRelics;
   public int costStarpower;
   public ISFSObject data;

   public EntityAltCost(ISFSObject sfsData) {
      this.data = sfsData;
      this.entityId = this.data.getInt("entity_id");
      this.islandId = this.data.getInt("island");
      this.costCoins = this.data.getInt("cost_coins");
      this.costEth = this.data.getInt("cost_eth_currency");
      this.costDiamonds = this.data.getInt("cost_diamonds");
      this.costKeys = this.data.getInt("cost_keys");
      this.costRelics = this.data.getInt("cost_relics");
      this.costStarpower = this.data.getInt("cost_starpower");
   }
}
