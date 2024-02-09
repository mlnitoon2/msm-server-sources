package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DailyCurrencyPackLookup {
   public static Map<Integer, DailyCurrencyPack> dailyCurrencyPacks;

   public static void init(IDbWrapper db) throws Exception {
      dailyCurrencyPacks = new HashMap();
      String sql = "SELECT * from daily_currency_packs";
      ISFSArray results = db.query("SELECT * from daily_currency_packs");
      Iterator i = results.iterator();

      while(i.hasNext()) {
         DailyCurrencyPack dailyCurrencyPack = new DailyCurrencyPack((SFSObject)((SFSDataWrapper)i.next()).getObject());
         dailyCurrencyPacks.put(dailyCurrencyPack.getID(), dailyCurrencyPack);
      }

   }

   public static DailyCurrencyPack get(int dailyCurrencyPackId) {
      return (DailyCurrencyPack)dailyCurrencyPacks.get(dailyCurrencyPackId);
   }
}
