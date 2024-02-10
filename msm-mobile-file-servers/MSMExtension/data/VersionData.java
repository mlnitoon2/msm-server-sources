package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Map.Entry;

public class VersionData implements Iterable<Entry<VersionInfo, VersionInfo>> {
   private static String MIN_CLIENT_VERSION_KEY = "min_client_version";
   private static String SERVER_VERSION_KEY = "server_content_version";
   public TreeMap<VersionInfo, VersionInfo> clientToServerVer;
   private static VersionData _instance;
   static Comparator<VersionInfo> versionComparator = new Comparator<VersionInfo>() {
      public int compare(VersionInfo s1, VersionInfo s2) {
         return s1.compareTo(s2);
      }
   };

   public static VersionData Instance() {
      if (_instance == null) {
         _instance = new VersionData();
      }

      return _instance;
   }

   private VersionData() {
      this.clientToServerVer = new TreeMap(versionComparator);
   }

   public boolean verifyIsLowerBoundary(VersionInfo minClientVer) {
      Iterator it = this.clientToServerVer.values().iterator();

      VersionInfo cur;
      do {
         if (!it.hasNext()) {
            return false;
         }

         cur = (VersionInfo)it.next();
      } while(cur.compareTo(minClientVer) != 0);

      return true;
   }

   public NavigableMap<VersionInfo, VersionInfo> tailMap(VersionInfo fromVersion, boolean inclusive) {
      return this.clientToServerVer.tailMap(fromVersion, inclusive);
   }

   public void init(IDbWrapper db) throws Exception {
      String sql = "SELECT * FROM versioning";
      ISFSArray results = db.query("SELECT * FROM versioning");
      Iterator i = results.iterator();

      while(i.hasNext()) {
         SFSObject data = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         this.add(data);
      }

   }

   public void add(SFSObject data) {
      String clientVer = data.getUtfString(MIN_CLIENT_VERSION_KEY);
      String serverVer = data.getUtfString(SERVER_VERSION_KEY);
      if (clientVer != null && serverVer != null) {
         this.add(clientVer, serverVer);
      }

   }

   public void add(String clientVer, String serverVer) {
      this.clientToServerVer.put(new VersionInfo(clientVer), new VersionInfo(serverVer));
   }

   public VersionInfo getMaxServerVersionFromClientVersion(VersionInfo clientVer) {
      if (clientVer == null) {
         return null;
      } else if (this.clientToServerVer.containsKey(clientVer)) {
         return (VersionInfo)this.clientToServerVer.get(clientVer);
      } else {
         Entry<VersionInfo, VersionInfo> mostRecentVersion = this.clientToServerVer.floorEntry(clientVer);
         return mostRecentVersion != null ? (VersionInfo)mostRecentVersion.getValue() : null;
      }
   }

   public VersionInfo getMinClientVersionFromServerVersion(VersionInfo serverVer) {
      Iterator itr = this.clientToServerVer.entrySet().iterator();

      Entry cur;
      do {
         if (!itr.hasNext()) {
            return null;
         }

         cur = (Entry)itr.next();
      } while(!((VersionInfo)cur.getValue()).equals(serverVer));

      return (VersionInfo)cur.getKey();
   }

   public Iterator<Entry<VersionInfo, VersionInfo>> iterator() {
      return this.clientToServerVer.entrySet().iterator();
   }
}
