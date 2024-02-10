package com.bigbluebubble.mysingingmonsters.data.groups;

import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.Logger;
import java.util.ArrayList;
import java.util.Iterator;

public class RewardedAdvertisementGroup extends BaseGroup {
   public RewardedAdvertisementGroup(String data) throws IllegalArgumentException {
      super(data);
   }

   public String toString() {
      return "RewardedAdvertisementGroup: " + super.toString();
   }

   public static void initStaticData(ArrayList<RewardedAdvertisementGroup> groups) {
      Iterator var1 = groups.iterator();

      while(var1.hasNext()) {
         RewardedAdvertisementGroup group = (RewardedAdvertisementGroup)var1.next();
         Logger.trace(LogLevel.DEBUG, "### " + group.toString());
      }

   }

   public static int getGroupToAssign(int level, VersionInfo version, String platform) {
      ArrayList<RewardedAdvertisementGroup> groups = UserGroup.getGroup(UserGroup.GroupType.RewardedAdvertisement);
      return BaseGroup.determineGroup(level, version, platform, groups);
   }
}
