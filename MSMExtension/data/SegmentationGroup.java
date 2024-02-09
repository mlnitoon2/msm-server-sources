package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.bigbluebubble.mysingingmonsters.player.PlayerIsland;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;

public class SegmentationGroup {
   private static final String SEGMENTATION_ID = "SegmentID";
   private static final String SEGMENTATION_NAME = "Name";
   private static final String SEGMENTATION_DESC = "Description";
   private static final String SEGMENTATION_DEF = "Definition";
   private long id = 0L;
   private String name = null;
   private String description = null;
   ISFSObject definition = null;

   public SegmentationGroup(ISFSObject groupData) {
      this.id = (long)groupData.getInt("SegmentID");
      this.name = groupData.getUtfString("Name");
      this.description = groupData.getUtfString("Description");
      this.definition = SFSObject.newFromJsonData(groupData.getUtfString("Definition"));
   }

   public long getID() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public String getDescription() {
      return this.description;
   }

   public ISFSObject getDefinition() {
      return this.definition;
   }

   public static enum SegmentCompare {
      LEVEL {
         public boolean compare(String operand, Player p, String val) {
            if (operand.equals("<")) {
               return p.getLevel() < Integer.valueOf(val);
            } else if (operand.equals("<=")) {
               return p.getLevel() <= Integer.valueOf(val);
            } else if (operand.equals(">")) {
               return p.getLevel() > Integer.valueOf(val);
            } else if (operand.equals(">=")) {
               return p.getLevel() >= Integer.valueOf(val);
            } else if (operand.equals("==")) {
               return p.getLevel() == Integer.valueOf(val);
            } else if (operand.equals("!=")) {
               return p.getLevel() != Integer.valueOf(val);
            } else if (operand.equals("in")) {
               SFSArray levels = SFSArray.newFromJsonData(val);

               for(int i = 0; i < levels.size(); ++i) {
                  if (p.getLevel() == levels.getInt(i)) {
                     return true;
                  }
               }

               return false;
            } else {
               return false;
            }
         }
      },
      DAYS_SINCE_INSTALL {
         public boolean compare(String operand, Player p, String val) {
            long daysSinceInstall = (MSMExtension.CurrentDBTime() - p.getDateCreatedTime()) / 86400000L;
            if (operand.equals("<")) {
               return daysSinceInstall < Long.valueOf(val);
            } else if (operand.equals("<=")) {
               return daysSinceInstall <= Long.valueOf(val);
            } else if (operand.equals(">")) {
               return daysSinceInstall > Long.valueOf(val);
            } else if (operand.equals(">=")) {
               return daysSinceInstall >= Long.valueOf(val);
            } else if (operand.equals("==")) {
               return daysSinceInstall == Long.valueOf(val);
            } else if (operand.equals("!=")) {
               return daysSinceInstall != Long.valueOf(val);
            } else if (operand.equals("in")) {
               SFSArray days = SFSArray.newFromJsonData(val);

               for(int i = 0; i < days.size(); ++i) {
                  if (daysSinceInstall == days.getLong(i)) {
                     return true;
                  }
               }

               return false;
            } else {
               return false;
            }
         }
      },
      LAST_LOGIN {
         public boolean compare(String operand, Player p, String val) {
            if (operand.equals("<")) {
               return p.LastLoginTime() < Long.valueOf(val);
            } else if (operand.equals("<=")) {
               return p.LastLoginTime() <= Long.valueOf(val);
            } else if (operand.equals(">")) {
               return p.LastLoginTime() > Long.valueOf(val);
            } else if (operand.equals(">=")) {
               return p.LastLoginTime() >= Long.valueOf(val);
            } else {
               long daysx;
               if (operand.equals("==")) {
                  daysx = (p.LastLoginTime() - Long.valueOf(val)) / 86400000L;
                  if (daysx == 0L) {
                     return true;
                  }
               }

               if (operand.equals("!=")) {
                  daysx = (p.LastLoginTime() - Long.valueOf(val)) / 86400000L;
                  if (daysx != 0L) {
                     return true;
                  }
               }

               if (operand.equals("in")) {
                  SFSArray dates = SFSArray.newFromJsonData(val);

                  for(int i = 0; i < dates.size(); ++i) {
                     long days = (p.LastLoginTime() - dates.getLong(i)) / 86400000L;
                     if (days == 0L) {
                        return true;
                     }
                  }

                  return false;
               } else {
                  return false;
               }
            }
         }
      },
      DATE_CREATED {
         public boolean compare(String operand, Player p, String val) {
            if (operand.equals("<")) {
               return p.getDateCreatedTime() < Long.valueOf(val);
            } else if (operand.equals("<=")) {
               return p.getDateCreatedTime() <= Long.valueOf(val);
            } else if (operand.equals(">")) {
               return p.getDateCreatedTime() > Long.valueOf(val);
            } else if (operand.equals(">=")) {
               return p.getDateCreatedTime() >= Long.valueOf(val);
            } else {
               long daysx;
               if (operand.equals("==")) {
                  daysx = (p.getDateCreatedTime() - Long.valueOf(val)) / 86400000L;
                  if (daysx == 0L) {
                     return true;
                  }
               }

               if (operand.equals("!=")) {
                  daysx = (p.getDateCreatedTime() - Long.valueOf(val)) / 86400000L;
                  if (daysx != 0L) {
                     return true;
                  }
               }

               if (operand.equals("in")) {
                  SFSArray dates = SFSArray.newFromJsonData(val);

                  for(int i = 0; i < dates.size(); ++i) {
                     long days = (p.getDateCreatedTime() - dates.getLong(i)) / 86400000L;
                     if (days == 0L) {
                        return true;
                     }
                  }

                  return false;
               } else {
                  return false;
               }
            }
         }
      },
      AMOUNT_SPENT {
         public boolean compare(String operand, Player p, String val) {
            if (operand.equals("<")) {
               return p.getInGamePurchaseAmountTotal() < Integer.valueOf(val);
            } else if (operand.equals("<=")) {
               return p.getInGamePurchaseAmountTotal() <= Integer.valueOf(val);
            } else if (operand.equals(">")) {
               return p.getInGamePurchaseAmountTotal() > Integer.valueOf(val);
            } else if (operand.equals(">=")) {
               return p.getInGamePurchaseAmountTotal() >= Integer.valueOf(val);
            } else if (operand.equals("==")) {
               return p.getInGamePurchaseAmountTotal() == Integer.valueOf(val);
            } else if (operand.equals("!=")) {
               return p.getInGamePurchaseAmountTotal() != Integer.valueOf(val);
            } else if (operand.equals("in")) {
               SFSArray amounts = SFSArray.newFromJsonData(val);

               for(int i = 0; i < amounts.size(); ++i) {
                  if (p.getInGamePurchaseAmountTotal() == amounts.getInt(i)) {
                     return true;
                  }
               }

               return false;
            } else {
               return false;
            }
         }
      },
      DAILY_LOGIN_DAY {
         public boolean compare(String operand, Player p, String val) {
            long rewardDay = (long)p.getPendingRewardDay();
            if (operand.equals("<")) {
               return rewardDay < Long.valueOf(val);
            } else if (operand.equals("<=")) {
               return rewardDay <= Long.valueOf(val);
            } else if (operand.equals(">")) {
               return rewardDay > Long.valueOf(val);
            } else if (operand.equals(">=")) {
               return rewardDay >= Long.valueOf(val);
            } else if (operand.equals("==")) {
               return rewardDay == Long.valueOf(val);
            } else if (operand.equals("!=")) {
               return rewardDay != Long.valueOf(val);
            } else if (operand.equals("in")) {
               SFSArray days = SFSArray.newFromJsonData(val);

               for(int i = 0; i < days.size(); ++i) {
                  if (rewardDay == days.getLong(i)) {
                     return true;
                  }
               }

               return false;
            } else {
               return false;
            }
         }
      },
      VERSION {
         public boolean compare(String operand, Player p, String val) {
            int resx;
            if (operand.equals("<")) {
               resx = p.lastClientVersion().compareTo(val);
               return resx < 0;
            } else if (operand.equals("<=")) {
               resx = p.lastClientVersion().compareTo(val);
               return resx <= 0;
            } else if (operand.equals(">")) {
               resx = p.lastClientVersion().compareTo(val);
               return resx > 0;
            } else if (operand.equals(">=")) {
               resx = p.lastClientVersion().compareTo(val);
               return resx >= 0;
            } else if (operand.equals("==")) {
               resx = p.lastClientVersion().compareTo(val);
               return resx == 0;
            } else if (operand.equals("!=")) {
               resx = p.lastClientVersion().compareTo(val);
               return resx != 0;
            } else if (operand.equals("in")) {
               SFSArray versions = SFSArray.newFromJsonData(val);

               for(int i = 0; i < versions.size(); ++i) {
                  int res = p.lastClientVersion().compareTo(versions.getUtfString(i));
                  if (res == 0) {
                     return true;
                  }
               }

               return false;
            } else {
               return false;
            }
         }
      },
      PLATFORM {
         public boolean compare(String operand, Player p, String val) {
            if (p.getPlatform() == null) {
               return false;
            } else if (operand.equals("==")) {
               return p.getPlatform().equals(val);
            } else if (operand.equals("!=")) {
               return !p.getPlatform().equals(val);
            } else if (operand.equals("in")) {
               SFSArray platforms = SFSArray.newFromJsonData(val);

               for(int i = 0; i < platforms.size(); ++i) {
                  if (p.getPlatform().equals(platforms.getUtfString(i))) {
                     return true;
                  }
               }

               return false;
            } else {
               return false;
            }
         }
      },
      GROUP {
         public boolean compare(String operand, Player p, String val) {
            if (operand.equals("==")) {
               return p.memberOfGroup(Integer.valueOf(val));
            } else if (operand.equals("!=")) {
               return !p.memberOfGroup(Integer.valueOf(val));
            } else if (operand.equals("in")) {
               SFSArray groups = SFSArray.newFromJsonData(val);

               for(int i = 0; i < groups.size(); ++i) {
                  if (p.memberOfGroup(groups.getInt(i))) {
                     return true;
                  }
               }

               return false;
            } else {
               return false;
            }
         }
      },
      ISLAND {
         public boolean compare(String operand, Player p, String val) {
            if (operand.equals("==")) {
               return p.getIslandByID((long)Integer.valueOf(val)) != null;
            } else if (operand.equals("!=")) {
               return p.getIslandByID((long)Integer.valueOf(val)) == null;
            } else if (operand.equals("in")) {
               SFSArray islands = SFSArray.newFromJsonData(val);

               for(int i = 0; i < islands.size(); ++i) {
                  if (p.getIslandByID((long)islands.getInt(i)) != null) {
                     return true;
                  }
               }

               return false;
            } else {
               return false;
            }
         }
      },
      ENTITY {
         public boolean compare(String operand, Player p, String val) {
            Monster mx;
            Structure s;
            Iterator itr;
            PlayerIsland island;
            if (operand.equals("==")) {
               mx = MonsterLookup.getFromEntityId(Integer.valueOf(val));
               s = StructureLookup.getFromEntityId(Integer.valueOf(val));
               if (mx == null && s == null) {
                  return false;
               } else {
                  itr = p.getIslands().iterator();

                  do {
                     if (!itr.hasNext()) {
                        return false;
                     }

                     island = (PlayerIsland)itr.next();
                     if (mx != null && island.getMonsterByID((long)mx.getMonsterID()) != null) {
                        return true;
                     }
                  } while(s == null || island.getStructureByID((long)s.getID()) == null);

                  return true;
               }
            } else if (operand.equals("!=")) {
               mx = MonsterLookup.getFromEntityId(Integer.valueOf(val));
               s = StructureLookup.getFromEntityId(Integer.valueOf(val));
               if (mx == null && s == null) {
                  return true;
               } else {
                  itr = p.getIslands().iterator();

                  do {
                     if (!itr.hasNext()) {
                        return true;
                     }

                     island = (PlayerIsland)itr.next();
                     if (mx != null && island.getMonsterByID((long)mx.getMonsterID()) != null) {
                        return false;
                     }
                  } while(s == null || island.getStructureByID((long)s.getID()) == null);

                  return false;
               }
            } else if (!operand.equals("in")) {
               return false;
            } else {
               SFSArray entities = SFSArray.newFromJsonData(val);

               for(int i = 0; i < entities.size(); ++i) {
                  Monster m = MonsterLookup.getFromEntityId(entities.getInt(i));
                  Structure sx = StructureLookup.getFromEntityId(entities.getInt(i));
                  if (m != null || sx != null) {
                     Iterator itrx = p.getIslands().iterator();

                     while(itrx.hasNext()) {
                        PlayerIsland islandx = (PlayerIsland)itrx.next();
                        if (m != null && islandx.getMonsterByID((long)m.getMonsterID()) != null) {
                           return true;
                        }

                        if (sx != null && islandx.getStructureByID((long)sx.getID()) != null) {
                           return true;
                        }
                     }
                  }
               }

               return false;
            }
         }
      };

      private SegmentCompare() {
      }

      public abstract boolean compare(String var1, Player var2, String var3);

      // $FF: synthetic method
      SegmentCompare(Object x2) {
         this();
      }
   }
}
