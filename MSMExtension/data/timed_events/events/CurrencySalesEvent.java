package com.bigbluebubble.mysingingmonsters.data.timed_events.events;

import com.bigbluebubble.mysingingmonsters.data.StoreItemsLookup;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class CurrencySalesEvent extends TimedEvent {
   private static final String STORE_ID_KEY = "store_item_id";
   private static final String NEW_AMOUNT_KEY = "sale_amount";
   private Integer storeItemId;
   private Integer saleAmount;

   public int getKey() {
      return this.getSaleItemId();
   }

   public CurrencySalesEvent(ISFSObject timedEventData) throws Exception {
      super(timedEventData);
      if (this.eventDataSfsArray.size() != 1) {
         throw new Exception("CurrencySaleEvent has invalid number of entries in data field; can only have 1");
      } else {
         this.storeItemId = ((SFSObject)this.eventDataSfsArray.getElementAt(0)).getInt("store_item_id");
         if (this.storeItemId == null) {
            throw new Exception("CurrencySaleEvent: storeItemId is null");
         } else {
            this.saleAmount = ((SFSObject)this.eventDataSfsArray.getElementAt(0)).getInt("sale_amount");
            if (this.saleAmount == null) {
               throw new Exception("CurrencySaleEvent: saleAmount is null");
            }
         }
      }
   }

   public Integer getSaleItemId() {
      return this.storeItemId;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof CurrencySalesEvent)) {
         return false;
      } else {
         CurrencySalesEvent e = (CurrencySalesEvent)o;
         return this.storeItemId == e.storeItemId && this.getStartDate() == e.getStartDate() && this.getEndDate() == e.getEndDate();
      }
   }

   public String toString() {
      String str = super.toString();
      ISFSObject storeItem = StoreItemsLookup.get(this.storeItemId);
      if (storeItem == null) {
         str = str + "invalid store item";
      } else {
         int storeItemId = storeItem.getInt("storeitem_id");
         String itemTitle = storeItem.getUtfString("item_title");
         int price = storeItem.getInt("price");
         int regularAmount = storeItem.getInt("amount");
         str = str + " store item: " + storeItemId + " " + itemTitle + ": price: " + price + " old amount: " + regularAmount + " new sale amount: " + this.saleAmount;
      }

      return str;
   }
}
