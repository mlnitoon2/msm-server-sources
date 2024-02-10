package com.bigbluebubble.mysingingmonsters.data.timed_events.events;

import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventManager;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class CrucibleHeatDiscountEvent extends TimedEvent {
   private static final String DISCOUNT_KEY = "discount";
   float discount = 0.0F;

   public int getKey() {
      return 0;
   }

   public float getDiscountValue() {
      return this.discount;
   }

   public CrucibleHeatDiscountEvent(ISFSObject timedEventData) throws Exception {
      super(timedEventData);
      if (this.eventDataSfsArray.size() != 1) {
         throw new Exception("CrucibleHeatDiscountEvent has invalid number of entries in data field; can only have 1");
      } else {
         ISFSObject eventData = (SFSObject)this.eventDataSfsArray.getElementAt(0);
         this.discount = eventData.getFloat("discount");
      }
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof CrucibleHeatDiscountEvent)) {
         return false;
      } else {
         CrucibleHeatDiscountEvent e = (CrucibleHeatDiscountEvent)o;
         return this.discount == e.discount && this.getStartDate() == e.getStartDate() && this.getEndDate() == e.getEndDate();
      }
   }

   public String toString() {
      String str = super.toString();

      try {
         return str + " CrucibleHeatDiscount on of discount value: " + this.getDiscountValue();
      } catch (Exception var3) {
         return str + var3.toString();
      }
   }

   public static boolean hasTimedEventNow(Player player, int islandType) {
      return TimedEventManager.instance().hasTimedEventNow(TimedEventType.CrucibleHeatDiscount, 0, islandType) || player.getTimedEvents().timedEventNow(TimedEventType.CrucibleHeatDiscount, 0, islandType);
   }
}
