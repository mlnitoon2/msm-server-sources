package com.bigbluebubble.mysingingmonsters.data.timed_events.events;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventManager;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.List;

public class PermalightTorchSaleEvent extends TimedEvent {
   private static final String DISCOUNT_KEY = "discount";
   private static final String DIAMOND_COST_KEY = "diamond_cost";
   PermalightTorchSaleEvent.DiscountType discountType;
   float value;

   public int getKey() {
      return 0;
   }

   public PermalightTorchSaleEvent.DiscountType getDiscountType() {
      return this.discountType;
   }

   public float getDiscountValue() {
      return this.value;
   }

   public PermalightTorchSaleEvent(ISFSObject timedEventData) throws Exception {
      super(timedEventData);
      this.discountType = PermalightTorchSaleEvent.DiscountType.None;
      this.value = 0.0F;
      if (this.eventDataSfsArray.size() != 1) {
         throw new Exception("PermalightTorchSaleEvent has invalid number of entries in data field; can only have 1");
      } else {
         ISFSObject eventData = (SFSObject)this.eventDataSfsArray.getElementAt(0);
         this.setDiscountType(eventData);
         this.value = this.discountType.Value(eventData);
      }
   }

   private void setDiscountType(ISFSObject salesData) throws Exception {
      PermalightTorchSaleEvent.DiscountType[] enumValues = PermalightTorchSaleEvent.DiscountType.values();
      PermalightTorchSaleEvent.DiscountType[] var3 = enumValues;
      int var4 = enumValues.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         PermalightTorchSaleEvent.DiscountType t = var3[var5];
         if (t.isDefined(salesData)) {
            if (this.discountType != PermalightTorchSaleEvent.DiscountType.None) {
               throw new Exception("Multiple Discount Type defined for PermalightTorchSale timed event!! " + salesData.toJson() + ": only one of the following is permitted in a single event: " + "discount" + " or " + "diamond_cost");
            }

            this.discountType = t;
         }
      }

      if (this.discountType == PermalightTorchSaleEvent.DiscountType.None) {
         throw new Exception("Missing or invalid Discount Type specified for PermalightTorchSale timed event!! " + salesData.toJson());
      }
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PermalightTorchSaleEvent)) {
         return false;
      } else {
         PermalightTorchSaleEvent e = (PermalightTorchSaleEvent)o;
         return this.discountType == e.discountType && this.value == e.value && this.getStartDate() == e.getStartDate() && this.getEndDate() == e.getEndDate();
      }
   }

   private int getSaleCost() throws Exception {
      PermalightTorchSaleEvent.DiscountType t = this.getDiscountType();
      if (t == PermalightTorchSaleEvent.DiscountType.NewDiamondCost) {
         return (int)this.getDiscountValue();
      } else {
         int originalCost = GameSettings.getInt("USER_DIAMOND_COST_PER_PERMALIT_TORCH");
         int newCost = originalCost;
         float currentPercentDiscount = this.getDiscountValue();
         if (currentPercentDiscount != 0.0F) {
            newCost = (int)((double)((float)originalCost * (1.0F - currentPercentDiscount)) + 0.5D);
         }

         if (newCost < 0) {
            newCost = 0;
         } else if (newCost > originalCost) {
            Logger.trace("SOME SORT OF PROBLEM HERE: TimedEvents: newCost ends up more than original cost for torch sale defaulting to original price");
            newCost = originalCost;
         }

         return newCost;
      }
   }

   public String toString() {
      String str = super.toString();

      try {
         return str + " Torches on sale to DiscountType: " + this.getDiscountType() + " value: " + this.getDiscountValue();
      } catch (Exception var3) {
         return str + var3.toString();
      }
   }

   public static boolean hasTimedEventNow(Player player, int islandType) {
      return TimedEventManager.instance().hasTimedEventNow(TimedEventType.PermalightTorchSale, 0, islandType) || player.getTimedEvents().timedEventNow(TimedEventType.PermalightTorchSale, 0, islandType);
   }

   public static int getSaleCostDiamonds(Player player, int originalCost, int islandType) throws Exception {
      int saleCost = 0;
      List<TimedEvent> globalEvents = TimedEventManager.instance().currentActiveOnKey(TimedEventType.PermalightTorchSale, 0, islandType);
      TimedEvent preferredEvent = null;

      for(int i = 0; i < globalEvents.size(); ++i) {
         TimedEvent e = (TimedEvent)globalEvents.get(i);
         if (e.isOverrideIsland()) {
            preferredEvent = e;
            break;
         }

         if (preferredEvent == null) {
            preferredEvent = e;
         }
      }

      if (preferredEvent != null) {
         saleCost = ((PermalightTorchSaleEvent)preferredEvent).getSaleCost();
      }

      List<TimedEvent> playerEvents = player.getTimedEvents().currentActiveOnKey(TimedEventType.PermalightTorchSale, 0, islandType);
      if (playerEvents.size() > 0) {
         int t = ((PermalightTorchSaleEvent)playerEvents.get(0)).getSaleCost();
         if (t < saleCost) {
            saleCost = t;
         }
      }

      return saleCost;
   }

   public static enum DiscountType {
      None {
         public String Key() {
            return null;
         }

         public float Value(ISFSObject salesData) {
            return 0.0F;
         }
      },
      Discount {
         public String Key() {
            return "discount";
         }

         public float Value(ISFSObject salesData) {
            return salesData.getFloat("discount");
         }
      },
      NewDiamondCost {
         public String Key() {
            return "diamond_cost";
         }

         public float Value(ISFSObject salesData) {
            return (float)salesData.getInt("diamond_cost");
         }
      };

      private DiscountType() {
      }

      public abstract String Key();

      public boolean isDefined(ISFSObject salesData) {
         String key = this.Key();
         return key != null && salesData.containsKey(key);
      }

      public abstract float Value(ISFSObject var1);

      // $FF: synthetic method
      DiscountType(Object x2) {
         this();
      }
   }
}
