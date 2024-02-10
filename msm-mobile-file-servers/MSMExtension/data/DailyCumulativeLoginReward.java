package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.mysingingmonsters.data.loot.LootTable;
import com.bigbluebubble.mysingingmonsters.data.loot.LootTableLookup;
import com.smartfoxserver.v2.entities.data.ISFSObject;

public class DailyCumulativeLoginReward {
   public static final String ID_KEY = "id";
   public static final String LOOT_ID_KEY = "loot_id";
   public static final String TEXT_ID_KEY = "text_id";
   private ISFSObject data;

   public DailyCumulativeLoginReward(ISFSObject data) {
      this.data = data;
   }

   public int rewardId() {
      return this.data.getInt("id");
   }

   public String textId() {
      return this.data.getUtfString("text_id");
   }

   public LootTable getLootTable() {
      return LootTableLookup.get(this.data.getInt("loot_id"));
   }
}
