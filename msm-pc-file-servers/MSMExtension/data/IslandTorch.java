package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.mysingingmonsters.staticdata.StaticData;
import com.smartfoxserver.v2.entities.data.ISFSObject;

public class IslandTorch extends StaticData {
   protected static final String ISLAND_ID_KEY = "island_id";
   protected static final String TORCH_GRAPHIC_KEY = "torch_graphic";

   public IslandTorch(ISFSObject torchData) {
      super(torchData);
   }

   public int getIsland() {
      return this.data.getInt("island_id");
   }

   public String getTorchGraphic() {
      return this.data.getUtfString("torch_graphic");
   }
}
