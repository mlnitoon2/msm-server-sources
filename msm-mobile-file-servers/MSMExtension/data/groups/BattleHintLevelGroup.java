package com.bigbluebubble.mysingingmonsters.data.groups;

import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Iterator;

public class BattleHintLevelGroup extends BaseGroup {
   ISFSArray userGameSettings;

   public BattleHintLevelGroup(String data) throws IllegalArgumentException {
      super(data);
      ISFSObject groupData = SFSObject.newFromJsonData(data);
      this.userGameSettings = groupData.getSFSArray("userGameSettings");
   }

   public String toString() {
      return "BattleHintLevel: " + super.toString();
   }

   public static void initStaticData(ArrayList<BattleHintLevelGroup> groups) {
      Iterator var1 = groups.iterator();

      while(var1.hasNext()) {
         BattleHintLevelGroup group = (BattleHintLevelGroup)var1.next();
         Logger.trace(LogLevel.DEBUG, "### " + group.toString());
      }

   }

   public static int getGroupToAssign(int level, VersionInfo version, String platform) {
      ArrayList<BattleHintLevelGroup> groups = UserGroup.getGroup(UserGroup.GroupType.BattleHintLevel);
      return BaseGroup.determineGroup(level, version, platform, groups);
   }

   public ISFSArray getUserGameSettings() {
      return this.userGameSettings;
   }
}
