package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AccessKeys {
   protected static Map<String, String> _accessKeys;

   public static void init(IDbWrapper db) throws Exception {
      Map<String, String> accessKeys = new HashMap();
      Integer gameId = GameSettings.getInt("AUTH_GAME_ID");
      String sql = "SELECT client_version, access_key FROM dlc WHERE game = ?";
      ISFSArray results = db.query("SELECT client_version, access_key FROM dlc WHERE game = ?", new Object[]{gameId});
      Iterator i = results.iterator();

      while(i.hasNext()) {
         SFSObject data = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         add(data, accessKeys);
      }

      _accessKeys = accessKeys;
   }

   protected static void add(SFSObject entry, Map<String, String> accessKeys) {
      String key = entry.getUtfString("access_key");
      Integer compressedVersion = entry.getInt("client_version");
      VersionInfo version = new VersionInfo(compressedVersion / 100 % 10, compressedVersion / 10 % 10, compressedVersion / 1 % 10);
      accessKeys.put(version.toString(), key);
   }

   public static final String getKey(VersionInfo clientVersion) {
      return (String)_accessKeys.get(clientVersion.toString());
   }
}
