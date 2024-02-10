package com.bigbluebubble.mysingingmonsters.data.groups;

import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Iterator;

public class ComboPackGroup extends BaseGroup {
   private int promotion_ = 0;
   private int durationHours_ = 0;

   public ComboPackGroup(String data) throws IllegalArgumentException {
      super(data);

      try {
         ISFSObject groupData = SFSObject.newFromJsonData(data);
         this.promotion_ = groupData.getInt("promotion");
         if (groupData.containsKey("duration_hours")) {
            this.durationHours_ = groupData.getInt("duration_hours");
         }

      } catch (Exception var3) {
         throw new IllegalArgumentException(String.format("Invalid Group Data '%s'", data));
      }
   }

   public String toString() {
      return "ComboPackGroup promotion: " + this.promotion() + " duration: " + this.durationHours() + super.toString();
   }

   public int promotion() {
      return this.promotion_;
   }

   public int durationHours() {
      return this.durationHours_;
   }

   public static void initStaticData(ArrayList<ComboPackGroup> groups) {
      Iterator var1 = groups.iterator();

      while(var1.hasNext()) {
         ComboPackGroup group = (ComboPackGroup)var1.next();
         Logger.trace(LogLevel.DEBUG, "### " + group.toString());
      }

   }

   public static int getGroupToAssign(int level, VersionInfo version, String platform) {
      ArrayList<ComboPackGroup> combos = UserGroup.getGroup(UserGroup.GroupType.ComboPack);
      return BaseGroup.determineGroup(level, version, platform, combos);
   }
}
