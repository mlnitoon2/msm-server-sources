package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.IDbWrapper;

public class TorchLookup {
   private static int maxTorchesAllowed;

   public static void init(IDbWrapper db) throws Exception {
      maxTorchesAllowed = GameSettings.getInt("USER_MAX_NUM_TORCHES_PER_ISLAND");
   }

   public static int getMaxTorchesAllowed() {
      return maxTorchesAllowed;
   }
}
