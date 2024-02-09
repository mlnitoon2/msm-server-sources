package com.bigbluebubble.mysingingmonsters.data.timed_events.events;

import com.bigbluebubble.mysingingmonsters.data.Promo;
import com.bigbluebubble.mysingingmonsters.data.PromoLookup;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class PromoEvent extends TimedEvent {
   Promo promo;

   public Promo getPromo() {
      return this.promo;
   }

   public PromoEvent(Promo promo, long startDate, long endDate) {
      super(0L, TimedEventType.Promo, startDate, endDate);
      this.eventDataSfsArray = new SFSArray();
      ISFSObject promoData = new SFSObject();
      promoData.putInt("promotion_id", promo.getID());
      this.eventDataSfsArray.addSFSObject(promoData);
      this.allRowData.putSFSArray("data", this.eventDataSfsArray);
      this.setMax(promo.getActivations());
   }

   public PromoEvent(ISFSObject timedEventData) {
      super(timedEventData);
      ISFSObject eventData = this.eventDataSfsArray.getSFSObject(0);
      ISFSObject promoData = new SFSObject();
      int promoId = 0;
      if (eventData.containsKey("promotion_id")) {
         promoId = eventData.getInt("promotion_id");
      }

      if (promoId > 0) {
         Promo template = PromoLookup.get(promoId);
         if (template != null) {
            promoData.putInt("promotion_id", promoId);
            promoData.putInt("promo_type", template.getType());
            promoData.putInt("exclusive", template.isExclusive() ? 1 : 0);
            promoData.putUtfString("icon_image", template.getIcon());
            promoData.putUtfString("icon_sheet", template.getSheet());
            promoData.putUtfString("placement", template.getPlacement());
            promoData.putInt("in_market", template.getInMarket());
            promoData.putUtfString("data", template.getData().toJson());
            promoData.putInt("activations", template.getActivations());
         }
      } else {
         promoData.putInt("promotion_id", 0);
         promoData.putInt("promo_type", eventData.getInt("promo_type"));
      }

      if (eventData.containsKey("icon_image")) {
         promoData.putUtfString("icon_image", eventData.getUtfString("icon_image"));
      }

      if (eventData.containsKey("icon_sheet")) {
         promoData.putUtfString("icon_sheet", eventData.getUtfString("icon_sheet"));
      }

      if (eventData.containsKey("placement")) {
         promoData.putUtfString("placement", eventData.getUtfString("placement"));
      }

      if (eventData.containsKey("in_market")) {
         promoData.putInt("in_market", eventData.getInt("in_market"));
      }

      if (eventData.containsKey("data")) {
         ISFSObject innerData = eventData.getSFSObject("data");
         promoData.putUtfString("data", innerData.toJson());
      }

      if (eventData.containsKey("activations")) {
         promoData.putInt("activations", eventData.getInt("activations"));
      }

      if (eventData.containsKey("exclusive")) {
         promoData.putInt("exclusive", eventData.getInt("exclusive"));
      }

      this.promo = new Promo(promoData);
      this.eventDataSfsArray = new SFSArray();
      this.eventDataSfsArray.addSFSObject(this.promo.toSFSObject());
      this.allRowData.putSFSArray("data", this.eventDataSfsArray);
      this.setMax(this.promo.getActivations());
   }

   public int getKey() {
      return 0;
   }
}
