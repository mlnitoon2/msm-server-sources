package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticData;
import com.smartfoxserver.v2.entities.data.ISFSObject;

public class Sticker extends StaticData {
   protected static final String ID_KEY = "sticker_id";
   protected static final String LEVEL_KEY = "level";
   protected static final String DESC_KEY = "desc";
   protected static final String FILE_KEY = "file";
   protected static final String MIN_VER_KEY = "min_server_version";

   public Sticker(ISFSObject stickerData) throws Exception {
      super(stickerData);
      this.minVersion = new VersionInfo(this.data.getUtfString("min_server_version"));
   }

   public int getID() {
      return this.data.getInt("sticker_id");
   }
}
