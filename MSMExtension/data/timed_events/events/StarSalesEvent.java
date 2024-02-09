package com.bigbluebubble.mysingingmonsters.data.timed_events.events;

import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.data.Entity;
import com.bigbluebubble.mysingingmonsters.data.MonsterLookup;
import com.bigbluebubble.mysingingmonsters.data.StructureLookup;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventManager;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.List;

public class StarSalesEvent extends TimedEvent {
   private static final String ENTITY_ID_KEY = "entity";
   private static final String DISCOUNT_KEY = "discount";
   private static final String STARPOWER_COST_KEY = "starpower_cost";
   Integer entityId;
   StarSalesEvent.DiscountType discountType;
   float value;

   public int getKey() {
      return this.getEntityId();
   }

   public Integer getEntityId() {
      return this.entityId;
   }

   public StarSalesEvent.DiscountType getDiscountType() {
      return this.discountType;
   }

   public float getDiscountValue() {
      return this.value;
   }

   public StarSalesEvent(ISFSObject timedEventData) throws Exception {
      super(timedEventData);
      this.discountType = StarSalesEvent.DiscountType.None;
      this.value = 0.0F;
      if (this.eventDataSfsArray.size() != 1) {
         throw new Exception("StarSalesEvent has invalid number of entries in data field; can only have 1");
      } else {
         ISFSObject eventData = (SFSObject)this.eventDataSfsArray.getElementAt(0);
         this.entityId = eventData.getInt("entity");
         if (this.entityId == null) {
            Logger.trace("StarSalesEvent: entityId is null");
         }

         this.setDiscountType(eventData);
         this.value = this.discountType.Value(eventData);
      }
   }

   private void setDiscountType(ISFSObject salesData) throws Exception {
      StarSalesEvent.DiscountType[] enumValues = StarSalesEvent.DiscountType.values();
      StarSalesEvent.DiscountType[] var3 = enumValues;
      int var4 = enumValues.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         StarSalesEvent.DiscountType t = var3[var5];
         if (t.isDefined(salesData)) {
            if (this.discountType != StarSalesEvent.DiscountType.None) {
               throw new Exception("Multiple Discount Type defined for StarSale timed event!! " + salesData.toJson() + ": only one of the following is permitted in a single event: " + "discount" + " or " + "starpower_cost");
            }

            this.discountType = t;
         }
      }

      if (this.discountType == StarSalesEvent.DiscountType.None) {
         throw new Exception("Missing or invalid Discount Type specified for StarSale timed event!! " + salesData.toJson());
      }
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof StarSalesEvent)) {
         return false;
      } else {
         StarSalesEvent e = (StarSalesEvent)o;
         return this.entityId == e.entityId && this.discountType == e.discountType && this.value == e.value && this.getStartDate() == e.getStartDate() && this.getEndDate() == e.getEndDate();
      }
   }

   public String toString() {
      String str = super.toString();
      Entity e = MonsterLookup.getFromEntityId(this.entityId);
      if (e == null) {
         e = StructureLookup.getFromEntityId(this.entityId);
         if (e == null) {
            return str + " invalid entity";
         }
      }

      try {
         return str + " entity: " + this.entityId + ": " + ((Entity)e).getName() + " on sale from " + ((Entity)e).getCostStarpower(0) + " to DiscountType: " + this.getDiscountType() + " value: " + this.getDiscountValue();
      } catch (Exception var4) {
         return str + var4.toString();
      }
   }

   public static boolean hasTimedEventNow(Entity entity, Player player, int islandType) {
      return TimedEventManager.instance().hasTimedEventNow(TimedEventType.StarSale, entity.getEntityId(), islandType) || player.getTimedEvents().timedEventNow(TimedEventType.StarSale, entity.getEntityId(), islandType);
   }

   public static int getTimedEventSaleCost(Entity entity, Player player, int islandType, VersionInfo clientVer) throws Exception {
      int originalCost = false;
      int originalCost = entity.getCostStarpower(islandType);
      if (originalCost == 0) {
         return 0;
      } else {
         float currentPercentDiscount = 0.0F;
         int currentCurrencySalePrice = -1;
         List<TimedEvent> globalSales = TimedEventManager.instance().currentActiveOnKey(TimedEventType.StarSale, entity.getEntityId(), islandType);
         StarSalesEvent preferredEvent = null;

         int newCost;
         for(newCost = 0; newCost < globalSales.size(); ++newCost) {
            TimedEvent e = (TimedEvent)globalSales.get(newCost);
            if (e.isOverrideIsland()) {
               preferredEvent = (StarSalesEvent)e;
               break;
            }
         }

         if (preferredEvent == null) {
            for(newCost = 0; newCost < globalSales.size(); ++newCost) {
               StarSalesEvent e = (StarSalesEvent)globalSales.get(newCost);
               StarSalesEvent.DiscountType t = e.getDiscountType();
               switch(t) {
               case Discount:
                  if (e.getDiscountValue() > currentPercentDiscount) {
                     currentPercentDiscount = e.getDiscountValue();
                  }
                  break;
               case NewCostStarpower:
                  if (currentCurrencySalePrice != -1) {
                     throw new Exception("TimedEvent Error: multiple starpower costs defined for entity: " + entity.getEntityId());
                  }

                  currentCurrencySalePrice = (int)e.getDiscountValue();
               case None:
               }
            }
         } else {
            StarSalesEvent.DiscountType t = preferredEvent.getDiscountType();
            switch(t) {
            case Discount:
               if (preferredEvent.getDiscountValue() > currentPercentDiscount) {
                  currentPercentDiscount = preferredEvent.getDiscountValue();
               }
               break;
            case NewCostStarpower:
               if (currentCurrencySalePrice != -1) {
                  throw new Exception("TimedEvent Error: multiple starpower costs defined for entity: " + entity.getEntityId());
               }

               currentCurrencySalePrice = (int)preferredEvent.getDiscountValue();
            case None:
            }
         }

         if (player.getTimedEvents().timedEventNow(TimedEventType.StarSale, entity.getEntityId(), islandType)) {
            List<TimedEvent> playerSales = player.getTimedEvents().currentActiveOnKey(TimedEventType.StarSale, entity.getEntityId(), islandType);

            for(int i = 0; i < playerSales.size(); ++i) {
               StarSalesEvent e = (StarSalesEvent)playerSales.get(i);
               StarSalesEvent.DiscountType discountType = e.getDiscountType();
               switch(discountType) {
               case Discount:
                  if (e.getDiscountValue() > currentPercentDiscount) {
                     currentPercentDiscount = e.getDiscountValue();
                  }
                  break;
               case NewCostStarpower:
                  if (currentCurrencySalePrice != -1) {
                     if ((int)e.getDiscountValue() < currentCurrencySalePrice) {
                        currentCurrencySalePrice = (int)e.getDiscountValue();
                     }
                  } else {
                     currentCurrencySalePrice = (int)e.getDiscountValue();
                  }
               case None:
               }
            }
         }

         newCost = originalCost;
         if (currentCurrencySalePrice != -1) {
            newCost = currentCurrencySalePrice;
         }

         if (currentPercentDiscount != 0.0F) {
            newCost = (int)((float)originalCost * (1.0F - currentPercentDiscount) + 0.5F);
         }

         if (newCost < 0) {
            newCost = 0;
         } else if (newCost > originalCost) {
            newCost = originalCost;
         }

         return newCost;
      }
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
      NewCostStarpower {
         public String Key() {
            return "starpower_cost";
         }

         public float Value(ISFSObject salesData) {
            return (float)salesData.getInt("starpower_cost");
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
