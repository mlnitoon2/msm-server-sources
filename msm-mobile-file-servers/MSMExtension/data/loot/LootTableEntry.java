package com.bigbluebubble.mysingingmonsters.data.loot;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.smartfoxserver.v2.entities.data.ISFSObject;

public class LootTableEntry {
   public static final String PROBABILITY_KEY = "p";
   public static final String TYPE_KEY = "type";
   public float probability = 1.0F;
   public ILootable loot;

   public LootTableEntry(ISFSObject sfsObject, IDbWrapper db) throws Exception {
      if (sfsObject.containsKey("p")) {
         this.probability = (float)SFSHelpers.getDouble("p", sfsObject);
      }

      switch(LootType.valueOf(sfsObject.getUtfString("type"))) {
      case TABLE:
         this.loot = new LootTableRef(sfsObject);
         break;
      case SET:
         this.loot = new LootSet(sfsObject, db);
         break;
      case GROUP:
         this.loot = new LootGroup(sfsObject, db);
         break;
      default:
         this.loot = new LootItem(sfsObject);
      }

   }
}
