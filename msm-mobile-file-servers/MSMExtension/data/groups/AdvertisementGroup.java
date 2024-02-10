package com.bigbluebubble.mysingingmonsters.data.groups;

import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Iterator;

public class AdvertisementGroup extends UserGroup {
   protected final int level_;
   protected final double chance_;
   private static double advertisementGroupTotalChanceSum_ = 0.0D;
   private static int advertisementMinimumLevel_ = Integer.MAX_VALUE;

   public static void initStaticData(ArrayList<AdvertisementGroup> groups) {
      double totalChance = 0.0D;
      int minLevel = Integer.MAX_VALUE;
      AdvertisementGroup g;
      if (groups != null) {
         for(Iterator var4 = groups.iterator(); var4.hasNext(); minLevel = Math.min(g.level(), minLevel)) {
            g = (AdvertisementGroup)var4.next();
            totalChance += g.chance();
         }
      }

      Logger.trace(LogLevel.DEBUG, "### Advertisements have a total sum chance of " + totalChance);
      Logger.trace(LogLevel.DEBUG, "### Advertisements have a minimum level of " + minLevel);
      advertisementGroupTotalChanceSum_ = totalChance;
      advertisementMinimumLevel_ = minLevel;
   }

   public static boolean AdvertisementGroupsExist() {
      return advertisementGroupTotalChanceSum_ > 0.0D;
   }

   public static int getGroupToAssign(Player player) {
      if (AdvertisementGroupsExist()) {
         if (player.getLevel() < advertisementMinimumLevel_) {
            return -1;
         }

         ArrayList<AdvertisementGroup> ads = UserGroup.getGroup(UserGroup.GroupType.Advertisement);
         Iterator var2 = ads.iterator();

         while(var2.hasNext()) {
            AdvertisementGroup group = (AdvertisementGroup)var2.next();
            if (player.memberOfGroup(group.id())) {
               return -1;
            }
         }

         double r = Math.random() * advertisementGroupTotalChanceSum_;

         AdvertisementGroup group;
         for(Iterator var4 = ads.iterator(); var4.hasNext(); r -= group.chance()) {
            group = (AdvertisementGroup)var4.next();
            if (!(r > group.chance()) && player.getLevel() >= group.level()) {
               return group.id();
            }
         }
      }

      return -1;
   }

   public AdvertisementGroup(String data) throws IllegalArgumentException {
      try {
         ISFSObject adData = SFSObject.newFromJsonData(data);
         this.level_ = adData.getInt("level");
         this.chance_ = adData.getDouble("chance");
         print(String.format(">>> Advertisement group with data '%s' and chance %f", data, this.chance_));
         print(String.format(">>> " + this.toString()));
      } catch (NullPointerException var3) {
         throw new IllegalArgumentException(String.format("Invalid Advertisement Group Data '%s'", data));
      }
   }

   public String toString() {
      return "AdvertisementGroup: " + super.toString();
   }

   public int level() {
      return this.level_;
   }

   public double chance() {
      return this.chance_;
   }
}
