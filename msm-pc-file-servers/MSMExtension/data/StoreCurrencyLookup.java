package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StoreCurrencyLookup {
   public static Map<Integer, ISFSObject> currency;

   public static void init(IDbWrapper db) throws Exception {
      currency = new ConcurrentHashMap();
      String sql = "SELECT * FROM store_currency";
      ISFSArray results = db.query("SELECT * FROM store_currency");
      Iterator i = results.iterator();

      while(i.hasNext()) {
         SFSObject data = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         Integer currencyId = data.getInt("storecur_id");
         currency.put(currencyId, data);
      }

   }

   public static ISFSObject get(int storeGroupId) {
      return (ISFSObject)currency.get(storeGroupId);
   }

   public static boolean storeCurrencySupportedByServerVersion(ISFSObject storeCurrency, VersionInfo supportedServerVersion) {
      String minServerVer = storeCurrency.getUtfString("min_server_version");
      VersionInfo reqdServerVer = new VersionInfo(minServerVer);
      return reqdServerVer.compareTo(supportedServerVersion) <= 0;
   }
}
