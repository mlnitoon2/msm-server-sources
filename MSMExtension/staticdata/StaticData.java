package com.bigbluebubble.mysingingmonsters.staticdata;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.data.VersionData;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class StaticData implements IStaticData {
   protected static final String MIN_VERSION_KEY = "min_version";
   protected static final String LAST_CHANGED_KEY = "last_changed";
   protected static final String SUPPORTED_CLIENT_PLATFORMS = "platforms";
   protected ISFSObject data;
   List<String> includedPlatforms = new ArrayList();
   List<String> excludedPlatforms = new ArrayList();
   protected VersionInfo minVersion;

   protected StaticData(ISFSObject data) {
      this.data = data;
      if (data.containsKey("min_version")) {
         this.minVersion = new VersionInfo(data.getUtfString("min_version"));
      } else {
         this.minVersion = new VersionInfo(0, 0, 0);
      }

      if (data.containsKey("platforms")) {
         String platforms = data.getUtfString("platforms");
         if (!platforms.isEmpty()) {
            String[] var3 = platforms.split(",");
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               String platform = var3[var5];
               if (platform.startsWith("!")) {
                  this.excludedPlatforms.add(platform.substring(1, platform.length()));
               } else {
                  this.includedPlatforms.add(platform);
               }
            }
         }
      }

   }

   public ISFSObject getData() {
      return this.data;
   }

   public VersionInfo minVersion() {
      return this.minVersion;
   }

   public boolean isSupportedPlatform(String clientPlatform) {
      if (!this.includedPlatforms.isEmpty()) {
         return this.includedPlatforms.contains(clientPlatform);
      } else if (!this.excludedPlatforms.isEmpty()) {
         return !this.excludedPlatforms.contains(clientPlatform);
      } else {
         return true;
      }
   }

   public boolean supportedByClient(String clientPlatform, String clientSubplatform, VersionInfo maxSupportedServerVer) {
      return maxSupportedServerVer.compareTo(this.minVersion) >= 0 && this.isSupportedPlatform(clientPlatform) && (clientSubplatform == null || clientSubplatform.isEmpty() || this.isSupportedPlatform(clientSubplatform));
   }

   public boolean shouldAlwaysUpdate() {
      return false;
   }

   public long lastChanged() {
      return this.data.containsKey("last_changed") ? this.data.getLong("last_changed") : 0L;
   }

   public static <T extends IStaticData> ISFSObject getStaticData(User sender, ISFSObject params, IStaticDataLookup<T> lookup) {
      ISFSObject response = new SFSObject();
      long serverTime = MSMExtension.CurrentDBTime();
      response.putLong("server_time", serverTime);
      Long lastUpdate = params.getLong("last_updated");
      if (lastUpdate == null || lastUpdate > serverTime) {
         lastUpdate = 0L;
      }

      String clientPlatform = (String)sender.getProperty("client_platform");
      String clientSubplatform = (String)sender.getProperty("client_subplatform");
      VersionInfo currentClientVer = new VersionInfo((String)sender.getSession().getProperty("client_version"));
      VersionInfo lastUpdatedClient = new VersionInfo((String)sender.getProperty("last_update_version"));
      VersionInfo maxSupportedServerVersion = VersionData.Instance().getMaxServerVersionFromClientVersion(currentClientVer);
      if (currentClientVer.compareTo(lastUpdatedClient) > 0) {
         lastUpdate = 0L;
      }

      long newestUpdate = 0L;
      ISFSArray data = new SFSArray();
      if (GameSettings.get("SKIP_STATIC_DATA_ITERATION", false) && lookup.lastChanged() != Long.MAX_VALUE && lookup.lastChanged() <= lastUpdate) {
         newestUpdate = lookup.lastChanged();
      } else {
         Iterator var15 = lookup.entries().iterator();

         while(var15.hasNext()) {
            IStaticData entry = (IStaticData)var15.next();
            long currentUpdate = entry.lastChanged();
            if ((entry.shouldAlwaysUpdate() || lastUpdate < currentUpdate) && entry.supportedByClient(clientPlatform, clientSubplatform, maxSupportedServerVersion)) {
               data.addSFSObject(entry.getData());
            }

            if (currentUpdate > newestUpdate) {
               newestUpdate = currentUpdate;
            }
         }
      }

      if (data.size() > 0) {
         response.putSFSArray(lookup.getCacheName(), data);
      }

      response.putLong("last_updated", Math.min(serverTime, newestUpdate));
      return response;
   }
}
