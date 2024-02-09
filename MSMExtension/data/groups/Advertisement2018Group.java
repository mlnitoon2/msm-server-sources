package com.bigbluebubble.mysingingmonsters.data.groups;

import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.Logger;
import java.util.ArrayList;
import java.util.Iterator;

public class Advertisement2018Group extends BaseGroup {
   public Advertisement2018Group(String data) throws IllegalArgumentException {
      super(data);
   }

   public String toString() {
      return "Advertisement2018Group" + super.toString();
   }

   public static void initStaticData(ArrayList<Advertisement2018Group> groups) {
      Iterator var1 = groups.iterator();

      while(var1.hasNext()) {
         Advertisement2018Group group = (Advertisement2018Group)var1.next();
         Logger.trace(LogLevel.DEBUG, "### " + group.toString());
      }

   }

   public static int getGroupToAssign(int level, VersionInfo version, String platform) {
      ArrayList<Advertisement2018Group> allAds = UserGroup.getGroup(UserGroup.GroupType.Advertisement2018);
      return BaseGroup.determineGroup(level, version, platform, allAds);
   }
}
