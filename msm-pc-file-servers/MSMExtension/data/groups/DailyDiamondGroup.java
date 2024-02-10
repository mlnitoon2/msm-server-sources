package com.bigbluebubble.mysingingmonsters.data.groups;

import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.Logger;
import java.util.ArrayList;
import java.util.Iterator;

public class DailyDiamondGroup extends BaseGroup {
   public DailyDiamondGroup(String data) throws IllegalArgumentException {
      super(data);
   }

   public String toString() {
      return "DailyDiamondGroup promotion: " + super.toString();
   }

   public static void initStaticData(ArrayList<DailyDiamondGroup> groups) {
      Iterator var1 = groups.iterator();

      while(var1.hasNext()) {
         DailyDiamondGroup group = (DailyDiamondGroup)var1.next();
         Logger.trace(LogLevel.DEBUG, "### " + group.toString());
      }

   }

   public static int getGroupToAssign(int level, VersionInfo version, String platform) {
      ArrayList<DailyDiamondGroup> groups = UserGroup.getGroup(UserGroup.GroupType.DailyDiamond);
      return BaseGroup.determineGroup(level, version, platform, groups);
   }
}
