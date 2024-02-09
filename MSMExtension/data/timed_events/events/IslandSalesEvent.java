package com.bigbluebubble.mysingingmonsters.data.timed_events.events;

import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.data.Island;
import com.bigbluebubble.mysingingmonsters.data.IslandLookup;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventManager;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.List;

public class IslandSalesEvent extends TimedEvent {
   private static final String ISLAND_ID_KEY = "island";
   private static final String DISCOUNT_KEY = "discount";
   private static final String DIAMOND_COST_KEY = "diamond_cost";
   private static final String COIN_COST_KEY = "coin_cost";
   Integer islandId;
   IslandSalesEvent.DiscountType discountType;
   float value;

   public int getKey() {
      return this.getIslandId();
   }

   public Integer getIslandId() {
      return this.islandId;
   }

   public IslandSalesEvent.DiscountType getDiscountType() {
      return this.discountType;
   }

   public float getDiscountValue() {
      return this.value;
   }

   public IslandSalesEvent(ISFSObject timedEventData) throws Exception {
      super(timedEventData);
      this.discountType = IslandSalesEvent.DiscountType.None;
      this.value = 0.0F;
      if (this.eventDataSfsArray.size() != 1) {
         throw new Exception("IslandSalesEvent has invalid number of entries in data field; can only have 1");
      } else {
         ISFSObject eventData = (SFSObject)this.eventDataSfsArray.getElementAt(0);
         this.islandId = eventData.getInt("island");
         if (this.islandId == null) {
            Logger.trace("IslandSalesEvent: islandId is null");
         }

         this.setDiscountType(eventData);
         this.value = this.discountType.Value(eventData);
      }
   }

   private void setDiscountType(ISFSObject salesData) throws Exception {
      IslandSalesEvent.DiscountType[] enumValues = IslandSalesEvent.DiscountType.values();
      IslandSalesEvent.DiscountType[] var3 = enumValues;
      int var4 = enumValues.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         IslandSalesEvent.DiscountType t = var3[var5];
         if (t.isDefined(salesData)) {
            if (this.discountType != IslandSalesEvent.DiscountType.None) {
               throw new Exception("Multiple Discount Type defined for IslandSale timed event!! " + salesData.toJson() + ": only one of the following is permitted in a single event: " + "discount" + " or " + "diamond_cost" + " or " + "coin_cost");
            }

            this.discountType = t;
         }
      }

      if (this.discountType == IslandSalesEvent.DiscountType.None) {
         throw new Exception("Missing or invalid Discount Type specified for IslandSale timed event!! " + salesData.toJson());
      }
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof IslandSalesEvent)) {
         return false;
      } else {
         IslandSalesEvent e = (IslandSalesEvent)o;
         return this.islandId == e.islandId && this.discountType == e.discountType && this.value == e.value && this.getStartDate() == e.getStartDate() && this.getEndDate() == e.getEndDate();
      }
   }

   public String toString() {
      String str = super.toString();
      Island i = IslandLookup.get(this.islandId);
      if (i == null) {
         return str + "invalid island";
      } else {
         try {
            return str + " island: " + this.islandId + " on sale " + this.getDiscountType() + " " + this.getDiscountValue();
         } catch (Exception var4) {
            return str + var4.toString();
         }
      }
   }

   public static boolean hasTimedEventNow(Island island, Player player, int islandType) {
      return TimedEventManager.instance().hasTimedEventNow(TimedEventType.IslandSale, island.getID(), islandType) || player.getTimedEvents().timedEventNow(TimedEventType.IslandSale, island.getID(), islandType);
   }

   public static int getTimedEventSaleCostCoins(Island island, Player player) throws Exception {
      return getTimedEventSaleCost(island, player, Player.CurrencyType.Coins);
   }

   public static int getTimedEventSaleCostDiamonds(Island island, Player player) throws Exception {
      return getTimedEventSaleCost(island, player, Player.CurrencyType.Diamonds);
   }

   public static int getTimedEventSaleCost(Island island, Player player, Player.CurrencyType type) throws Exception {
      int originalCost = 0;
      if (type == Player.CurrencyType.Coins) {
         originalCost = island.getCoinCost();
      } else if (type == Player.CurrencyType.Diamonds) {
         originalCost = island.getDiamondCost();
      }

      if (originalCost == 0) {
         return 0;
      } else {
         float currentPercentDiscount = 0.0F;
         int currentCurrencySalePrice = -1;
         List<TimedEvent> globalSales = TimedEventManager.instance().currentActiveOnKey(TimedEventType.IslandSale, island.getID(), player.getActiveIsland().getType());
         IslandSalesEvent preferredEvent = null;

         int newCost;
         for(newCost = 0; newCost < globalSales.size(); ++newCost) {
            TimedEvent e = (TimedEvent)globalSales.get(newCost);
            if (e.isOverrideIsland()) {
               preferredEvent = (IslandSalesEvent)e;
               break;
            }
         }

         if (preferredEvent == null) {
            for(newCost = 0; newCost < globalSales.size(); ++newCost) {
               IslandSalesEvent e = (IslandSalesEvent)globalSales.get(newCost);
               IslandSalesEvent.DiscountType discountType = e.getDiscountType();
               switch(discountType) {
               case Discount:
                  if (e.getDiscountValue() > currentPercentDiscount) {
                     currentPercentDiscount = e.getDiscountValue();
                  }
                  break;
               case NewCostCoins:
                  if (type != Player.CurrencyType.Coins) {
                     throw new Exception("TimedEvent Error: incorrect discount type defined for island: " + island.getID());
                  }

                  if (currentCurrencySalePrice != -1) {
                     throw new Exception("TimedEvent Error: multiple currency costs defined for island: " + island.getID());
                  }

                  currentCurrencySalePrice = (int)e.getDiscountValue();
                  break;
               case NewCostDiamonds:
                  if (type != Player.CurrencyType.Diamonds) {
                     throw new Exception("TimedEvent Error: incorrect discount type defined for island: " + island.getID());
                  }

                  if (currentCurrencySalePrice != -1) {
                     throw new Exception("TimedEvent Error: multiple currency costs defined for island: " + island.getID());
                  }

                  currentCurrencySalePrice = (int)e.getDiscountValue();
               case None:
               }
            }
         } else {
            IslandSalesEvent.DiscountType discountType = preferredEvent.getDiscountType();
            switch(discountType) {
            case Discount:
               if (preferredEvent.getDiscountValue() > currentPercentDiscount) {
                  currentPercentDiscount = preferredEvent.getDiscountValue();
               }
               break;
            case NewCostCoins:
               if (type != Player.CurrencyType.Coins) {
                  throw new Exception("TimedEvent Error: incorrect discount type defined for island: " + island.getID());
               }

               if (currentCurrencySalePrice != -1) {
                  throw new Exception("TimedEvent Error: multiple currency costs defined for island: " + island.getID());
               }

               currentCurrencySalePrice = (int)preferredEvent.getDiscountValue();
               break;
            case NewCostDiamonds:
               if (type != Player.CurrencyType.Diamonds) {
                  throw new Exception("TimedEvent Error: incorrect discount type defined for island: " + island.getID());
               }

               if (currentCurrencySalePrice != -1) {
                  throw new Exception("TimedEvent Error: multiple currency costs defined for island: " + island.getID());
               }

               currentCurrencySalePrice = (int)preferredEvent.getDiscountValue();
            case None:
            }
         }

         if (player.getTimedEvents().timedEventNow(TimedEventType.IslandSale, island.getID(), player.getActiveIsland().getType())) {
            List<TimedEvent> playerSales = player.getTimedEvents().currentActiveOnKey(TimedEventType.IslandSale, island.getID(), player.getActiveIsland().getType());

            for(int i = 0; i < playerSales.size(); ++i) {
               IslandSalesEvent e = (IslandSalesEvent)playerSales.get(i);
               IslandSalesEvent.DiscountType discountType = e.getDiscountType();
               switch(discountType) {
               case Discount:
                  if (e.getDiscountValue() > currentPercentDiscount) {
                     currentPercentDiscount = e.getDiscountValue();
                  }
                  break;
               case NewCostCoins:
                  if (type != Player.CurrencyType.Coins) {
                     throw new Exception("TimedEvent Error: incorrect discount type defined for costume: " + island.getID());
                  }

                  if (currentCurrencySalePrice != -1) {
                     if ((int)e.getDiscountValue() < currentCurrencySalePrice) {
                        currentCurrencySalePrice = (int)e.getDiscountValue();
                     }
                  } else {
                     currentCurrencySalePrice = (int)e.getDiscountValue();
                  }
                  break;
               case NewCostDiamonds:
                  if (type != Player.CurrencyType.Diamonds) {
                     throw new Exception("TimedEvent Error: incorrect discount type defined for costume: " + island.getID());
                  }

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
