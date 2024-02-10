package com.bigbluebubble.mysingingmonsters.data.groups;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class UserGroup {
   protected static boolean DEBUG_LOG = false;
   protected int id_;
   protected UserGroup.GroupType type_;
   protected static ArrayList<ArrayList<UserGroup>> groups_ = new ArrayList();

   public int id() {
      return this.id_;
   }

   public UserGroup.GroupType type() {
      return this.type_;
   }

   public String toString() {
      return String.format("Num: %d Type: %s", this.id_, this.type_);
   }

   public static UserGroup getGroupById(int n) {
      Iterator var1 = groups_.iterator();

      while(var1.hasNext()) {
         ArrayList<UserGroup> a = (ArrayList)var1.next();
         Iterator var3 = a.iterator();

         while(var3.hasNext()) {
            UserGroup g = (UserGroup)var3.next();
            if (g.id() == n) {
               return g;
            }
         }
      }

      return null;
   }

   public static <T extends UserGroup> ArrayList<T> getGroup(UserGroup.GroupType type) {
      return (ArrayList)groups_.get(type.ordinal());
   }

   public static void initStaticData(IDbWrapper db) throws Exception {
      String sql = "SELECT * FROM groups";
      ISFSArray results = db.query("SELECT * FROM groups");
      Iterator i = results.iterator();

      while(i.hasNext()) {
         add((SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject()));
      }

      ArrayList<TutorialGroup> tutorials = getGroup(UserGroup.GroupType.Tutorial);
      TutorialGroup.initStaticData(tutorials);
      ArrayList<AdvertisementGroup> ads = getGroup(UserGroup.GroupType.Advertisement);
      AdvertisementGroup.initStaticData(ads);
      ArrayList<NoAdvertisementGroup> noads = getGroup(UserGroup.GroupType.NoAdvertisement);
      NoAdvertisementGroup.initStaticData(noads);
      ArrayList<Advertisement2018Group> ads2018 = getGroup(UserGroup.GroupType.Advertisement2018);
      Advertisement2018Group.initStaticData(ads2018);
      ArrayList<ComboPackGroup> combos = getGroup(UserGroup.GroupType.ComboPack);
      ComboPackGroup.initStaticData(combos);
      ArrayList<IslandPromoGroup> islandPromos = getGroup(UserGroup.GroupType.IslandPromo);
      IslandPromoGroup.initStaticData(islandPromos);
      ArrayList<BonusPromoGroup> bonusPromos = getGroup(UserGroup.GroupType.BonusPromo);
      BonusPromoGroup.initStaticData(bonusPromos);
      ArrayList<TapjoyTierGroup> tapjoyTiers = getGroup(UserGroup.GroupType.TapjoyTier);
      TapjoyTierGroup.initStaticData(tapjoyTiers);
      ArrayList<DailyDiamondGroup> dailyDiamonds = getGroup(UserGroup.GroupType.DailyDiamond);
      DailyDiamondGroup.initStaticData(dailyDiamonds);
      ArrayList<CurrencyGroup> currencyExcludes = getGroup(UserGroup.GroupType.CurrencyExclude);
      CurrencyGroup.initStaticData(currencyExcludes);
      ArrayList<RewardedAdvertisementGroup> rewardedAds = getGroup(UserGroup.GroupType.RewardedAdvertisement);
      RewardedAdvertisementGroup.initStaticData(rewardedAds);
      ArrayList<BattleHintLevelGroup> battleHintLevels = getGroup(UserGroup.GroupType.BattleHintLevel);
      BattleHintLevelGroup.initStaticData(battleHintLevels);
   }

   public static void add(ISFSObject o) {
      try {
         Integer id = o.getInt("id");
         String type = o.getUtfString("type");
         String data = o.getUtfString("data");
         UserGroup.GroupType t = UserGroup.GroupType.valueOf(type);
         UserGroup g = t.create(data);
         g.id_ = id;
         g.type_ = t;
         if (getGroupById(id) != null) {
            throw new Exception(String.format("Error, Group with id %d already exists", id));
         }

         print(String.format("Created test group: %s", g));
         print(String.format("Current value of text enum: %s, %d", t, t.ordinal()));
         print(String.format("Max value of test enum: %s, %d", UserGroup.GroupType.None, UserGroup.GroupType.None.ordinal()));
         getGroup(t).add(g);
         print(String.format("Added group %s to group list %d", g, t.ordinal()));
      } catch (Exception var6) {
         Logger.trace(var6, String.format("=======================\n========================Unable to create group from '%s'", o.getDump(false)));
      }

   }

   protected static void print(String s) {
      if (DEBUG_LOG) {
         Logger.trace(s);
      }

   }

   static {
      for(int i = 0; i < UserGroup.GroupType.None.ordinal(); ++i) {
         groups_.add(new ArrayList());
      }

   }

   public static enum GroupType {
      CurrencyExclude {
         public UserGroup create(String data) throws IllegalArgumentException {
            return new CurrencyGroup(data);
         }
      },
      Tutorial {
         public UserGroup create(String data) throws IllegalArgumentException {
            return new TutorialGroup(data);
         }
      },
      Advertisement {
         public UserGroup create(String data) throws IllegalArgumentException {
            return new AdvertisementGroup(data);
         }
      },
      NoAdvertisement {
         public UserGroup create(String data) throws IllegalArgumentException {
            return new NoAdvertisementGroup(data);
         }
      },
      BrandedAdvertisement {
         public UserGroup create(String data) throws IllegalArgumentException {
            return new BrandedAdvertisementGroup(data);
         }
      },
      Advertisement2018 {
         public UserGroup create(String data) throws IllegalArgumentException {
            return new Advertisement2018Group(data);
         }
      },
      ComboPack {
         public UserGroup create(String data) throws IllegalArgumentException {
            return new ComboPackGroup(data);
         }
      },
      IslandPromo {
         public UserGroup create(String data) throws IllegalArgumentException {
            return new IslandPromoGroup(data);
         }
      },
      BonusPromo {
         public UserGroup create(String data) throws IllegalArgumentException {
            return new BonusPromoGroup(data);
         }
      },
      TapjoyTier {
         public UserGroup create(String data) throws IllegalArgumentException {
            return new TapjoyTierGroup(data);
         }
      },
      DailyDiamond {
         public UserGroup create(String data) throws IllegalArgumentException {
            return new DailyDiamondGroup(data);
         }
      },
      RewardedAdvertisement {
         public UserGroup create(String data) throws IllegalArgumentException {
            return new RewardedAdvertisementGroup(data);
         }
      },
      BattleHintLevel {
         public UserGroup create(String data) throws IllegalArgumentException {
            return new BattleHintLevelGroup(data);
         }
      },
      ScientificRevenue {
         public UserGroup create(String data) throws IllegalArgumentException {
            return new DeprecatedGroup(data);
         }
      },
      None {
         public UserGroup create(String data) throws IllegalArgumentException {
            throw new IllegalArgumentException("'None' invalid group type");
         }
      };

      public static final UserGroup.GroupType[] values = values();

      private GroupType() {
      }

      public abstract UserGroup create(String var1);

      // $FF: synthetic method
      GroupType(Object x2) {
         this();
      }
   }
}
