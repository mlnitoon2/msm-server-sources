package com.bigbluebubble.mysingingmonsters.staticdata;

import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.smartfoxserver.v2.entities.data.ISFSObject;

public interface IStaticData {
   ISFSObject getData();

   boolean supportedByClient(String var1, String var2, VersionInfo var3);

   boolean shouldAlwaysUpdate();

   VersionInfo minVersion();

   long lastChanged();
}
