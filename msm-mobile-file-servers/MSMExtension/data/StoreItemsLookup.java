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

public class StoreItemsLookup {
   public static Map<Integer, ISFSObject> items;

   public static void init(IDbWrapper db) throws Exception {
      items = new ConcurrentHashMap();
      String sql = "SELECT * FROM store_items";
      ISFSArray results = db.query("SELECT * FROM store_items");
      Iterator i = results.iterator();

      while(i.hasNext()) {
         SFSObject data = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         int itemId = data.getInt("storeitem_id");
         items.put(itemId, data);
      }

   }

   public static ISFSObject get(int storeItemId) {
      return (ISFSObject)items.get(storeItemId);
   }

   public static boolean storeItemSupportedByServerVersion(ISFSObject storeItem, VersionInfo supportedServerVersion) {
      String minServerVer = storeItem.getUtfString("min_server_version");
      VersionInfo reqdServerVer = new VersionInfo(minServerVer);
      return reqdServerVer.compareTo(supportedServerVersion) <= 0;
   }
}
