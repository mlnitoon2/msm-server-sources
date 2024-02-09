package com.bigbluebubble.mysingingmonsters.data;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class Promo {
   public static final String PROMO_ID = "promotion_id";
   public static final String PROMO_TYPE = "promo_type";
   public static final String PROMO_EXCLUSIVE = "exclusive";
   public static final String PROMO_ICON = "icon_image";
   public static final String PROMO_SHEET = "icon_sheet";
   public static final String PROMO_PLACEMENT = "placement";
   public static final String PROMO_IN_MARKET = "in_market";
   public static final String PROMO_DATA = "data";
   public static final String PROMO_ACTIVATIONS = "activations";
   private int id = 0;
   private int type = 0;
   private int exclusive = 0;
   private String icon = "";
   private String sheet = "";
   private String placement = "";
   private int inMarket = 0;
   private int activations = 0;
   private ISFSObject data = null;

   public Promo(ISFSObject promoData) {
      this.id = promoData.getInt("promotion_id");
      this.type = promoData.getInt("promo_type");
      this.data = SFSObject.newFromJsonData(promoData.getUtfString("data"));
      if (promoData.containsKey("exclusive")) {
         this.exclusive = promoData.getInt("exclusive");
      }

      if (promoData.containsKey("icon_image")) {
         this.icon = promoData.getUtfString("icon_image");
      }

      if (promoData.containsKey("icon_sheet")) {
         this.sheet = promoData.getUtfString("icon_sheet");
      }

      if (promoData.containsKey("placement")) {
         this.placement = promoData.getUtfString("placement");
      }

      if (promoData.containsKey("in_market")) {
         this.inMarket = promoData.getInt("in_market");
      }

      if (promoData.containsKey("activations")) {
         this.activations = promoData.getInt("activations");
      }

   }

   public int getID() {
      return this.id;
   }

   public int getType() {
      return this.type;
   }

   public boolean isExclusive() {
      return this.exclusive != 0;
   }

   public String getIcon() {
      return this.icon;
   }

   public String getSheet() {
      return this.sheet;
   }

   public String getPlacement() {
      return this.placement;
   }

   public int getInMarket() {
      return this.inMarket;
   }

   public ISFSObject getData() {
      return this.data;
   }

   public int getActivations() {
      return this.activations;
   }

   public boolean isCombo() {
      return this.getType() == 3 || this.getType() == 5;
   }

   public String toString() {
      return String.format("Promo %d  Type %d  exclusive %b  icon_image '%s'  icon_sheet '%s'  data '%s'", this.getID(), this.getType(), this.isExclusive(), this.getIcon(), this.getSheet(), this.getData().toJson().toString());
   }

   public ISFSObject toSFSObject() {
      ISFSObject obj = new SFSObject();
      obj.putInt("promotion_id", this.id);
      obj.putInt("promo_type", this.type);
      obj.putUtfString("icon_image", this.icon);
      obj.putUtfString("icon_sheet", this.sheet);
      obj.putUtfString("placement", this.placement);
      obj.putInt("in_market", this.inMarket);
      obj.putInt("activations", this.activations);
      obj.putSFSObject("data", this.data);
      return obj;
   }
}
