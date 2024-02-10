package com.bigbluebubble.mysingingmonsters.data.groups;

import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.Logger;
import java.util.ArrayList;
import java.util.Iterator;

public class TapjoyTierGroup extends BaseGroup {
   public static final int TEST_GROUP = 24;
   public static final int CONTROL_GROUP = 25;

   public TapjoyTierGroup(String data) throws IllegalArgumentException {
      super(data);
   }

   public String toString() {
      return "TapjoyTierGroup: " + super.toString();
   }

   public static void initStaticData(ArrayList<TapjoyTierGroup> groups) {
      Iterator var1 = groups.iterator();

      while(var1.hasNext()) {
         TapjoyTierGroup group = (TapjoyTierGroup)var1.next();
         Logger.trace(LogLevel.DEBUG, "### " + group.toString());
      }

   }

   public static int getGroupToAssign(int level, VersionInfo version, String platform) {
      ArrayList<TapjoyTierGroup> groups = UserGroup.getGroup(UserGroup.GroupType.TapjoyTier);
      return BaseGroup.determineGroup(level, version, platform, groups);
   }
}
