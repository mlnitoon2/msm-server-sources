package com.bigbluebubble.mysingingmonsters.data.groups;

import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Iterator;

public class NoAdvertisementGroup extends UserGroup {
   protected final double chance_;
   private static double staticChance = 0.0D;
   private static int staticId = -1;

   public static void initStaticData(ArrayList<NoAdvertisementGroup> groups) {
      NoAdvertisementGroup g;
      for(Iterator var1 = groups.iterator(); var1.hasNext(); staticId = g.id()) {
         g = (NoAdvertisementGroup)var1.next();
         staticChance = g.chance();
      }

      Logger.trace(LogLevel.DEBUG, String.format("### NoAdvertisementGroup has id %d and chance %f", staticId, staticChance));
   }

   public static int getGroupToAssign() {
      double r = Math.random();
      if (r < staticChance) {
         print(String.format("### User IS part of NoAdvertisementGroup %d (%f vs %f)", staticId, r, staticChance));
         return staticId;
      } else {
         print(String.format("### User is NOT part of NoAdvertisementGroup (%f vs %f)", r, staticChance));
         return -1;
      }
   }

   public NoAdvertisementGroup(String data) throws IllegalArgumentException {
      try {
         ISFSObject adData = SFSObject.newFromJsonData(data);
         this.chance_ = adData.getDouble("chance");
         print(String.format(">>> NoAdvertisement group with data '%s' and chance %f", data, this.chance_));
      } catch (NullPointerException var3) {
         throw new IllegalArgumentException(String.format("Invalid No Advertisement Group Data '%s'", data));
      }
   }

   public String toString() {
      return "NoAdvertisementGroup: " + super.toString();
   }

   public double chance() {
      return this.chance_;
   }
}
