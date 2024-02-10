package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PromoLookup {
   public static final int NONE = 0;
   public static final int IAP_DOUBLE = 1;
   public static final int BUNDLE_WARMUP = 3;
   public static final int BUNDLE_WARMUP2 = 5;
   public static final int BUNDLE_TOPUP_COLD = 6;
   public static final int BUNDLE_THEME = 7;
   public static final int IAP_999_BONUS = 8;
   public static final int IAP_2499_BONUS = 9;
   public static final int IAP_4999_BONUS = 10;
   public static final int BUNDLE_WARMUP3 = 11;
   public static final int BUNDLE_WARMUP4 = 12;
   public static final int BUNDLE_TOPUP_AIR = 13;
   public static final int BUNDLE_TOPUP_WATER = 14;
   public static final int BUNDLE_TOPUP_EARTH = 15;
   public static final int BUNDLE_WARMUP5 = 16;
   public static final int BUNDLE_ANNIVERSARY1 = 17;
   public static final int BUNDLE_ANNIVERSARY2 = 18;
   protected static Map<Integer, Promo> promos;
   protected static Map<String, Integer> countryIds;
   protected static int defaultId = 0;
   private static final String DEFAULT_COMBO_PACK = "{\"id\":11, \"segments\":[]}";

   public static void init(IDbWrapper db) throws Exception {
      promos = new HashMap();
      String sql = "SELECT * FROM promotions";
      ISFSArray results = db.query("SELECT * FROM promotions");
      Iterator i = results.iterator();

      while(i.hasNext()) {
         Promo promo = new Promo((SFSObject)((SFSDataWrapper)i.next()).getObject());
         promos.put(promo.getID(), promo);
      }

      ISFSObject settings = SFSObject.newFromJsonData(GameSettings.get("DEFAULT_COMBO_PACK", "{\"id\":11, \"segments\":[]}"));
      defaultId = settings.getInt("id");
      countryIds = new HashMap();
      ISFSArray segments = settings.getSFSArray("segments");

      for(int i = 0; i < segments.size(); ++i) {
         ISFSObject s = segments.getSFSObject(i);
         countryIds.put(s.getUtfString("country"), s.getInt("id"));
      }

   }

   public static int getIdForCountry(String country) {
      Integer i = (Integer)countryIds.get(country);
      return i != null ? i : 0;
   }

   public static int getDefaultId() {
      return defaultId;
   }

   public static Promo get(int promoId) {
      return (Promo)promos.get(promoId);
   }
}
