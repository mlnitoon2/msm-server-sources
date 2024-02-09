package com.bigbluebubble.mysingingmonsters.data.groups;

import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.Logger;
import java.util.ArrayList;
import java.util.Iterator;

public class BonusPromoGroup extends BaseGroup {
   public static final int TEST_GROUP = 22;
   public static final int CONTROL_GROUP = 23;

   public BonusPromoGroup(String data) throws IllegalArgumentException {
      super(data);
   }

   public String toString() {
      return "BonusPromoGroup promotion: " + super.toString();
   }

   public static void initStaticData(ArrayList<BonusPromoGroup> groups) {
      Iterator var1 = groups.iterator();

      while(var1.hasNext()) {
         BonusPromoGroup group = (BonusPromoGroup)var1.next();
         Logger.trace(LogLevel.DEBUG, "### " + group.toString());
      }

   }

   public static int getGroupToAssign(int level, VersionInfo version, String platform) {
      ArrayList<BonusPromoGroup> groups = UserGroup.getGroup(UserGroup.GroupType.BonusPromo);
      return BaseGroup.determineGroup(level, version, platform, groups);
   }
}
