package com.bigbluebubble.mysingingmonsters.data.groups;

import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Iterator;

public class IslandPromoGroup extends BaseGroup {
   private int promotion_ = 0;

   public IslandPromoGroup(String data) throws IllegalArgumentException {
      super(data);

      try {
         ISFSObject groupData = SFSObject.newFromJsonData(data);
         this.promotion_ = groupData.getInt("promotion");
      } catch (Exception var3) {
         throw new IllegalArgumentException(String.format("Invalid Group Data '%s'", data));
      }
   }

   public String toString() {
      return "IslandPromoGroup promotion: " + this.promotion() + super.toString();
   }

   public int promotion() {
      return this.promotion_;
   }

   public static void initStaticData(ArrayList<IslandPromoGroup> groups) {
      Iterator var1 = groups.iterator();

      while(var1.hasNext()) {
         IslandPromoGroup group = (IslandPromoGroup)var1.next();
         Logger.trace(LogLevel.DEBUG, "### " + group.toString());
      }

   }

   public static int getGroupToAssign(int level, VersionInfo version, String platform) {
      ArrayList<IslandPromoGroup> groups = UserGroup.getGroup(UserGroup.GroupType.IslandPromo);
      return BaseGroup.determineGroup(level, version, platform, groups);
   }
}
