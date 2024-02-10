package com.bigbluebubble.mysingingmonsters.data.timed_events.events;

import com.bigbluebubble.mysingingmonsters.data.StoreItemsLookup;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class CurrencyAvailabilityEvent extends TimedEvent {
   private static final String STORE_ID_KEY = "store_item_id";
   private Integer storeItemId;

   public int getKey() {
      return this.getSaleItemId();
   }

   public CurrencyAvailabilityEvent(ISFSObject timedEventData) throws Exception {
      super(timedEventData);
      if (this.eventDataSfsArray.size() != 1) {
         throw new Exception("CurrencySaleEvent has invalid number of entries in data field; can only have 1");
      } else {
         this.storeItemId = ((SFSObject)this.eventDataSfsArray.getElementAt(0)).getInt("store_item_id");
         if (this.storeItemId == null) {
            throw new Exception("CurrencySaleEvent: storeItemId is null");
         }
      }
   }

   public Integer getSaleItemId() {
      return this.storeItemId;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof CurrencyAvailabilityEvent)) {
         return false;
      } else {
         CurrencyAvailabilityEvent e = (CurrencyAvailabilityEvent)o;
         return this.storeItemId == e.storeItemId && this.getStartDate() == e.getStartDate() && this.getEndDate() == e.getEndDate();
      }
   }

   public String toString() {
      String str = super.toString();
      ISFSObject storeItem = StoreItemsLookup.get(this.storeItemId);
      if (storeItem == null) {
         str = str + " invalid store item";
      } else {
         int storeItemId = storeItem.getInt("storeitem_id");
         String itemTitle = storeItem.getUtfString("item_title");
         str = str + " store item: " + storeItemId + " " + itemTitle + " available";
      }

      return str;
   }
}
