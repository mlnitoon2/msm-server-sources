package com.bigbluebubble.mysingingmonsters.data.timed_events.events;

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

public class EntitySalesEvent extends TimedEvent {
   private static final String ENTITY_ID_KEY = "entity";
   private static final String DISCOUNT_KEY = "discount";
   private static final String DIAMOND_COST_KEY = "diamond_cost";
   private static final String COIN_COST_KEY = "coin_cost";
   private static final String ETH_COST_KEY = "ethereal_cost";
   private static final String KEY_COST_KEY = "key_cost";
   private static final String RELIC_COST_KEY = "relic_cost";
   private static final String MEDAL_COST_KEY = "medal_cost";
   Integer entityId;
   EntitySalesEvent.DiscountType discountType;
   float value;

   public int getKey() {
      return this.getEntityId();
   }

   public Integer getEntityId() {
      return this.entityId;
   }

   public EntitySalesEvent.DiscountType getDiscountType() {
      return this.discountType;
   }

   public float getDiscountValue() {
      return this.value;
   }

   public EntitySalesEvent(ISFSObject timedEventData) throws Exception {
      super(timedEventData);
      this.discountType = EntitySalesEvent.DiscountType.None;
      this.value = 0.0F;
      if (this.eventDataSfsArray.size() != 1) {
         throw new Exception("EntitySalesEvent has invalid number of entries in data field; can only have 1");
      } else {
         ISFSObject eventData = (SFSObject)this.eventDataSfsArray.getElementAt(0);
         this.entityId = eventData.getInt("entity");
         if (this.entityId == null) {
            Logger.trace("EntitySalesEvent: entityId is null");
         }

         this.setDiscountType(eventData);
         this.value = this.discountType.Value(eventData);
      }
   }

   private void setDiscountType(ISFSObject salesData) throws Exception {
      EntitySalesEvent.DiscountType[] enumValues = EntitySalesEvent.DiscountType.values();
      EntitySalesEvent.DiscountType[] var3 = enumValues;
      int var4 = enumValues.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EntitySalesEvent.DiscountType t = var3[var5];
         if (t.isDefined(salesData)) {
            if (this.discountType != EntitySalesEvent.DiscountType.None) {
               throw new Exception("Multiple Discount Type defined for EntitySale timed event!! " + salesData.toJson() + ": only one of the following is permitted in a single event: " + "discount" + " or " + "diamond_cost" + " or " + "coin_cost" + " or " + "key_cost" + " or " + "relic_cost");
            }

            this.discountType = t;
         }
      }

      if (this.discountType == EntitySalesEvent.DiscountType.None) {
         throw new Exception("Missing or invalid Discount Type specified for EntitySale timed event!! " + salesData.toJson());
      }
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof EntitySalesEvent)) {
         return false;
      } else {
         EntitySalesEvent e = (EntitySalesEvent)o;
         return this.entityId == e.entityId && this.discountType == e.discountType && this.value == e.value && this.getStartDate() == e.getStartDate() && this.getEndDate() == e.getEndDate();
      }
   }

   public String toString() {
      String str = super.toString();
      Entity e = MonsterLookup.getFromEntityId(this.entityId);
      if (e == null) {
         e = StructureLookup.getFromEntityId(this.entityId);
         if (e == null) {
            return str + "invalid entity";
         }
      }

      try {
         return str + " entity: " + this.entityId + ": " + ((Entity)e).getName() + " on sale for " + this.getDiscountType() + " " + this.getDiscountValue();
      } catch (Exception var4) {
         return str + var4.toString();
      }
   }

   public static boolean hasTimedEventNow(Entity entity, Player player, int islandType) {
      return TimedEventManager.instance().hasTimedEventNow(TimedEventType.EntitySale, entity.getEntityId(), islandType) || player.getTimedEvents().timedEventNow(TimedEventType.EntitySale, entity.getEntityId(), islandType);
   }

   public static int getTimedEventSaleCost(Entity entity, Player player, Player.CurrencyType currency, int islandType) throws Exception {
      int originalCost = entity.getCurrencyCost(currency, islandType);
      if (originalCost == 0) {
         return 0;
      } else {
         List<TimedEvent> globalSales = TimedEventManager.instance().currentActiveOnKey(TimedEventType.EntitySale, entity.getEntityId(), islandType);
         EntitySalesEvent preferredEvent = null;

         for(int i = 0; i < globalSales.size(); ++i) {
            TimedEvent e = (TimedEvent)globalSales.get(i);
            if (e.isOverrideIsland()) {
               preferredEvent = (EntitySalesEvent)e;
               break;
            }
         }

         float currentPercentDiscount = 0.0F;
         int currentCurrencySalePrice = -1;
         int newCost;
         if (preferredEvent == null) {
            for(newCost = 0; newCost < globalSales.size(); ++newCost) {
               EntitySalesEvent e = (EntitySalesEvent)globalSales.get(newCost);
               EntitySalesEvent.DiscountType discountType = e.getDiscountType();
               if (discountType == EntitySalesEvent.DiscountType.Discount) {
                  if (e.getDiscountValue() > currentPercentDiscount) {
                     currentPercentDiscount = e.getDiscountValue();
                  }
               } else if (discountType == currency.SpecificCurrencyDiscountType()) {
                  if (currentCurrencySalePrice != -1) {
                     throw new Exception("TimedEvent Error: double currency Discount defined for entity: " + entity.getEntityId());
                  }

                  currentCurrencySalePrice = (int)e.getDiscountValue();
               }
            }
         } else {
            EntitySalesEvent.DiscountType discountType = preferredEvent.getDiscountType();
            if (discountType == EntitySalesEvent.DiscountType.Discount) {
               currentPercentDiscount = preferredEvent.getDiscountValue();
            } else if (discountType == currency.SpecificCurrencyDiscountType()) {
               if (currentCurrencySalePrice != -1) {
                  throw new Exception("TimedEvent Error: double currency Discount defined for entity: " + entity.getEntityId());
               }

               currentCurrencySalePrice = (int)preferredEvent.getDiscountValue();
            }
         }

         if (player.getTimedEvents().timedEventNow(TimedEventType.StarSale, entity.getEntityId(), islandType)) {
            List<TimedEvent> playerSales = player.getTimedEvents().currentActiveOnKey(TimedEventType.StarSale, entity.getEntityId(), islandType);

            for(int i = 0; i < playerSales.size(); ++i) {
               EntitySalesEvent e = (EntitySalesEvent)playerSales.get(i);
               EntitySalesEvent.DiscountType discountType = e.getDiscountType();
               if (discountType == EntitySalesEvent.DiscountType.Discount) {
                  if (e.getDiscountValue() > currentPercentDiscount) {
                     currentPercentDiscount = e.getDiscountValue();
                  }
               } else if (discountType == currency.SpecificCurrencyDiscountType()) {
                  if (currentCurrencySalePrice != -1) {
                     if ((int)e.getDiscountValue() < currentCurrencySalePrice) {
                        currentCurrencySalePrice = (int)e.getDiscountValue();
                     }
                  } else {
                     currentCurrencySalePrice = (int)e.getDiscountValue();
                  }
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
      NewCostDiamonds {
         public String Key() {
            return "diamond_cost";
         }

         public float Value(ISFSObject salesData) {
            return (float)salesData.getInt("diamond_cost");
         }
      },
      NewCostCoins {
         public String Key() {
            return "coin_cost";
         }

         public float Value(ISFSObject salesData) {
            return (float)salesData.getInt("coin_cost");
         }
      },
      NewCostEthereal {
         public String Key() {
            return "ethereal_cost";
         }

         public float Value(ISFSObject salesData) {
            return (float)salesData.getInt("ethereal_cost");
         }
      },
      NewCostKey {
         public String Key() {
            return "key_cost";
         }

         public float Value(ISFSObject salesData) {
            return (float)salesData.getInt("key_cost");
         }
      },
      NewCostRelic {
         public String Key() {
            return "relic_cost";
         }

         public float Value(ISFSObject salesData) {
            return (float)salesData.getInt("relic_cost");
         }
      },
      NewCostMedal {
         public String Key() {
            return "medal_cost";
         }

         public float Value(ISFSObject salesData) {
            return (float)salesData.getInt("medal_cost");
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
