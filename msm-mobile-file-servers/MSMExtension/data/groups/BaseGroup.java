package com.bigbluebubble.mysingingmonsters.data.groups;

import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

abstract class BaseGroup extends UserGroup {
   protected VersionInfo minVersion_;
   protected VersionInfo maxVersion_;
   protected Integer minLevel_;
   protected Integer maxLevel_;
   protected List<String> platforms_;
   protected Double chance_;

   protected BaseGroup(String data) throws IllegalArgumentException {
      try {
         ISFSObject groupData = SFSObject.newFromJsonData(data);
         if (groupData.containsKey("min_level")) {
            this.minLevel_ = groupData.getInt("min_level");
         }

         if (groupData.containsKey("max_level")) {
            this.maxLevel_ = groupData.getInt("max_level");
         }

         if (groupData.containsKey("min_version")) {
            this.minVersion_ = new VersionInfo(groupData.getUtfString("min_version"));
         }

         if (groupData.containsKey("max_version")) {
            this.maxVersion_ = new VersionInfo(groupData.getUtfString("max_version"));
         }

         if (groupData.containsKey("chance")) {
            this.chance_ = SFSHelpers.getDouble("chance", groupData);
         }

         if (groupData.containsKey("platforms")) {
            this.platforms_ = new ArrayList(groupData.getUtfStringArray("platforms"));
         }
      } catch (Exception var3) {
         throw new IllegalArgumentException(String.format("Invalid Group Data '%s'", data));
      }

      if (this.minLevel_ != null && this.maxLevel_ != null && this.minLevel_ > this.maxLevel_) {
         throw new IllegalArgumentException(String.format("Invalid min/max levels: %d / %d", this.minLevel_, this.maxLevel_));
      } else if (this.minVersion_ != null && this.maxVersion_ != null && this.minVersion_.compareTo(this.maxVersion_) > 0) {
         throw new IllegalArgumentException(String.format("Invalid min/max versions: %s / %s", this.minVersion_, this.maxVersion_));
      } else if (this.chance_ != null && this.chance_ < 0.0D) {
         throw new IllegalArgumentException(String.format("Invalid chance: %f", this.chance_));
      }
   }

   protected boolean supportsLevel(int clientLevel) {
      if (this.minLevel_ != null && this.minLevel_ > clientLevel) {
         return false;
      } else {
         return this.maxLevel_ == null || this.maxLevel_ >= clientLevel;
      }
   }

   protected boolean supportsVersion(VersionInfo clientVersion) {
      if (this.minVersion_ != null && this.minVersion_.compareTo(clientVersion) > 0) {
         return false;
      } else {
         return this.maxVersion_ == null || this.maxVersion_.compareTo(clientVersion) >= 0;
      }
   }

   protected boolean supportsPlatform(String clientPlatform) {
      return this.platforms_ == null || this.platforms_.contains(clientPlatform);
   }

   protected Double getChance() {
      return this.chance_;
   }

   protected static int determineGroup(int level, VersionInfo version, String platform, List<? extends BaseGroup> groups) {
      ArrayList<BaseGroup> possibleGroups = new ArrayList();
      double totalChance = 0.0D;
      Iterator var7 = groups.iterator();

      while(true) {
         while(var7.hasNext()) {
            BaseGroup group = (BaseGroup)var7.next();
            if (group.supportsLevel(level) && group.supportsVersion(version) && group.supportsPlatform(platform) && group.getChance() > 0.0D) {
               print(String.format("User supported by group %d", group.id()));
               possibleGroups.add(group);
               totalChance += group.getChance();
            } else {
               print(String.format("User NOT supported by group %d", group.id()));
            }
         }

         if (totalChance > 0.0D) {
            double r = Math.random() * totalChance;
            print(String.format("Generated random value %f", r));
            Iterator var9 = possibleGroups.iterator();

            BaseGroup group;
            do {
               if (!var9.hasNext()) {
                  print(String.format("A thing happened and we chose the last group"));
                  return ((BaseGroup)possibleGroups.get(possibleGroups.size() - 1)).id();
               }

               group = (BaseGroup)var9.next();
               r -= group.getChance();
            } while(!(r < 0.0D));

            print(String.format("Chose group %d", group.id()));
            return group.id();
         }

         print(String.format("No valid groups to choose from"));
         return -1;
      }
   }

   public String toString() {
      StringBuilder s = new StringBuilder();
      if (this.minLevel_ != null) {
         s.append(String.format(" min level: %d", this.minLevel_));
      }

      if (this.maxLevel_ != null) {
         s.append(String.format(" max level: %d", this.maxLevel_));
      }

      if (this.minVersion_ != null) {
         s.append(String.format(" min version: %s", this.minVersion_));
      }

      if (this.maxVersion_ != null) {
         s.append(String.format(" max version: %s", this.maxVersion_));
      }

      if (this.platforms_ != null) {
         s.append(" platforms: ");
         if (!this.platforms_.isEmpty()) {
            Iterator i = this.platforms_.iterator();

            while(i.hasNext()) {
               s.append((String)i.next());
               if (i.hasNext()) {
                  s.append(", ");
               }
            }
         } else {
            s.append("<NONE>");
         }
      }

      if (this.chance_ != null) {
         s.append(String.format(" chance: %f", this.chance_));
      }

      s.append(" ");
      return s.toString() + super.toString();
   }
}
