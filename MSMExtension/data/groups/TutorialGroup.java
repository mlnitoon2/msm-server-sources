package com.bigbluebubble.mysingingmonsters.data.groups;

import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.player.InitialPlayerIslandData;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.bigbluebubble.mysingingmonsters.player.PlayerIslandFactory;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

public class TutorialGroup extends BaseGroup {
   Method islandSetupFn;
   String tutorialClient;
   ISFSArray initialQuests;
   ISFSArray userGameSettings;

   public static void initStaticData(ArrayList<TutorialGroup> tutorials) {
      Iterator var1 = tutorials.iterator();

      while(var1.hasNext()) {
         TutorialGroup group = (TutorialGroup)var1.next();
         Logger.trace(LogLevel.DEBUG, "### " + group.toString());
      }

   }

   public static int getGroupToAssign(int level, VersionInfo version, String platform) {
      ArrayList<TutorialGroup> tutorials = UserGroup.getGroup(UserGroup.GroupType.Tutorial);
      return BaseGroup.determineGroup(level, version, platform, tutorials);
   }

   public TutorialGroup(String data) throws IllegalArgumentException {
      super(data);
      ISFSObject groupData = SFSObject.newFromJsonData(data);
      String islandSetup = groupData.getUtfString("island_setup");
      this.tutorialClient = groupData.getUtfString("tutorial_client");
      this.initialQuests = groupData.getSFSArray("initialQuests");
      this.userGameSettings = groupData.getSFSArray("userGameSettings");

      try {
         this.islandSetupFn = PlayerIslandFactory.class.getMethod(islandSetup, Player.class, Integer.TYPE, Long.TYPE);
      } catch (NoSuchMethodException var5) {
         throw new IllegalArgumentException("Tutorial Group data \"island_setup\" is invalid function name or signature: " + islandSetup + " : " + var5.toString());
      } catch (NullPointerException var6) {
         throw new IllegalArgumentException("Tutorial Group data \"island_setup\" is null function name : " + var6.toString());
      } catch (SecurityException var7) {
         throw new IllegalArgumentException("Tutorial Group data \"island_setup\" is invalid function name or signature: " + islandSetup + " : " + var7.toString());
      }
   }

   public ISFSArray getInitialQuests() {
      return this.initialQuests;
   }

   public ISFSArray getUserGameSettings() {
      return this.userGameSettings;
   }

   public boolean hasUserGameSetting(String setting) {
      if (this.userGameSettings == null) {
         return false;
      } else {
         for(int i = 0; i < this.userGameSettings.size(); ++i) {
            String key = this.userGameSettings.getUtfString(i);
            if (key.compareTo(setting) == 0) {
               return true;
            }
         }

         return false;
      }
   }

   public InitialPlayerIslandData CallIslandInitializationFunction(Player player, int islandIndex, long islandId) throws Exception {
      return (InitialPlayerIslandData)this.islandSetupFn.invoke((Object)null, player, islandIndex, islandId);
   }

   public String clientTutorial() {
      return this.tutorialClient;
   }

   public String toString() {
      return "TutorialGroup " + super.toString();
   }
}
