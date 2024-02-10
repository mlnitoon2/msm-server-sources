package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class StoreGroupsLookup {
   public static ConcurrentHashMap<Integer, ISFSObject> groups;

   public static void init(IDbWrapper db) throws Exception {
      groups = new ConcurrentHashMap();
      String sql = "SELECT * FROM store_groups";
      ISFSArray results = db.query("SELECT * FROM store_groups");
      Iterator i = results.iterator();

      while(i.hasNext()) {
         SFSObject data = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         int groupId = data.getInt("storegroup_id");
         groups.put(groupId, data);
      }

   }

   public static ISFSObject get(int storeGroupId) {
      return (ISFSObject)groups.get(storeGroupId);
   }

   public static boolean storeGroupSupportedByServerVersion(ISFSObject storeGroup, VersionInfo supportedServerVersion) {
      String minServerVer = storeGroup.getUtfString("min_server_version");
      VersionInfo reqdServerVer = new VersionInfo(minServerVer);
      return reqdServerVer.compareTo(supportedServerVersion) <= 0;
   }
}
