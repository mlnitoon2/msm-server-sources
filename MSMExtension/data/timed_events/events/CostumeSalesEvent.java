package com.bigbluebubble.mysingingmonsters.data.timed_events.events;

import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.costumes.CostumeData;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventManager;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.List;

public class CostumeSalesEvent extends TimedEvent {
   private static final String COSTUME_ID_KEY = "costume";
   private static final String DISCOUNT_KEY = "discount";
   private static final String DIAMOND_COST_KEY = "diamondCost";
   private static final String MEDAL_COST_KEY = "medalCost";
   Integer costumeId;
   CostumeSalesEvent.DiscountType discountType;
   float value;

   public int getKey() {
      return this.getCostumeId();
   }

   public Integer getCostumeId() {
      return this.costumeId;
   }

   public CostumeSalesEvent.DiscountType getDiscountType() {
      return this.discountType;
   }

   public float getDiscountValue() {
      return this.value;
   }

   public CostumeSalesEvent(ISFSObject timedEventData) throws Exception {
      super(timedEventData);
      this.discountType = CostumeSalesEvent.DiscountType.None;
      this.value = 0.0F;
      if (this.eventDataSfsArray.size() != 1) {
         throw new Exception("CostumeSaleEvent has invalid number of entries in data field; can only have 1");
      } else {
         ISFSObject eventData = (SFSObject)this.eventDataSfsArray.getElementAt(0);
         this.costumeId = eventData.getInt("costume");
         if (this.costumeId == null) {
            Logger.trace("CostumeSaleEvent: costumeId is null");
         }

         this.setDiscountType(eventData);
         this.value = this.discountType.Value(eventData);
      }
   }

   private void setDiscountType(ISFSObject salesData) throws Exception {
      CostumeSalesEvent.DiscountType[] enumValues = CostumeSalesEvent.DiscountType.values();
      CostumeSalesEvent.DiscountType[] var3 = enumValues;
      int var4 = enumValues.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         CostumeSalesEvent.DiscountType t = var3[var5];
         if (t.isDefined(salesData)) {
            if (this.discountType != CostumeSalesEvent.DiscountType.None) {
               throw new Exception("Multiple Discount Type defined for CostumeSale timed event!! " + salesData.toJson() + ": only one of the following is permitted in a single event: " + "discount" + " or " + "diamondCost" + " or " + "medalCost");
            }

            this.discountType = t;
         }
      }

      if (this.discountType == CostumeSalesEvent.DiscountType.None) {
         throw new Exception("Missing or invalid Discount Type specified for CostumeSale timed event!! " + salesData.toJson());
      }
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof CostumeSalesEvent)) {
         return false;
      } else {
         CostumeSalesEvent e = (CostumeSalesEvent)o;
         return this.costumeId == e.costumeId && this.discountType == e.discountType && this.value == e.value && this.getStartDate() == e.getStartDate() && this.getEndDate() == e.getEndDate();
      }
   }

   public String toString() {
      String str = super.toString();

      try {
         str = str + " costume: " + this.costumeId + " on sale " + this.getDiscountType() + " " + this.getDiscountValue();
      } catch (Exception var3) {
         str = str + var3.toString();
      }

      return str;
   }

   public static boolean hasTimedEventNow(CostumeData costumeData, Player player, int islandType) {
      return TimedEventManager.instance().hasTimedEventNow(TimedEventType.CostumeSale, costumeData.getId(), islandType) || player.getTimedEvents().timedEventNow(TimedEventType.CostumeSale, costumeData.getId(), islandType);
   }

   public static int getTimedEventSaleCost(CostumeData costumeData, Player player, Player.CurrencyType type, int islandType) throws Exception {
      int originalCost = 0;
      if (type == Player.CurrencyType.Diamonds) {
         originalCost = costumeData.getDiamondCost();
      } else if (type == Player.CurrencyType.Medals) {
         originalCost = costumeData.getMedalCost();
      }

      if (originalCost == 0) {
         return 0;
      } else {
         float currentPercentDiscount = 0.0F;
         int currencyOverride = -1;
         List<TimedEvent> globalSales = TimedEventManager.instance().currentActiveOnKey(TimedEventType.CostumeSale, costumeData.getId(), islandType);
         CostumeSalesEvent preferredEvent = null;

         int newCost;
         for(newCost = 0; newCost < globalSales.size(); ++newCost) {
            TimedEvent e = (TimedEvent)globalSales.get(newCost);
            if (e.isOverrideIsland()) {
               preferredEvent = (CostumeSalesEvent)e;
               break;
            }
         }

         if (preferredEvent == null) {
            for(newCost = 0; newCost < globalSales.size(); ++newCost) {
               CostumeSalesEvent e = (CostumeSalesEvent)globalSales.get(newCost);
               CostumeSalesEvent.DiscountType discountType = e.getDiscountType();
               switch(discountType) {
               case Discount:
                  if (e.getDiscountValue() > currentPercentDiscount) {
                     currentPercentDiscount = e.getDiscountValue();
                  }
                  break;
               case NewCostDiamonds:
                  if (type != Player.CurrencyType.Diamonds) {
                     throw new Exception("TimedEvent Error: incorrect discount type defined for costume: " + costumeData.getId());
                  }

                  if (currencyOverride != -1) {
                     throw new Exception("TimedEvent Error: multiple currency costs defined for costume: " + costumeData.getId());
                  }

                  currencyOverride = (int)e.getDiscountValue();
                  break;
               case NewCostMedals:
                  if (type != Player.CurrencyType.Medals) {
                     throw new Exception("TimedEvent Error: incorrect discount type defined for costume: " + costumeData.getId());
                  }

                  if (currencyOverride != -1) {
                     throw new Exception("TimedEvent Error: multiple currency costs defined for costume: " + costumeData.getId());
                  }

                  currencyOverride = (int)e.getDiscountValue();
               case None:
               }
            }
         } else {
            CostumeSalesEvent.DiscountType discountType = preferredEvent.getDiscountType();
            switch(discountType) {
            case Discount:
               if (preferredEvent.getDiscountValue() > currentPercentDiscount) {
                  currentPercentDiscount = preferredEvent.getDiscountValue();
               }
               break;
            case NewCostDiamonds:
               if (type != Player.CurrencyType.Diamonds) {
                  throw new Exception("TimedEvent Error: incorrect discount type defined for costume: " + costumeData.getId());
               }

               if (currencyOverride != -1) {
                  throw new Exception("TimedEvent Error: multiple currency costs defined for costume: " + costumeData.getId());
               }

               currencyOverride = (int)preferredEvent.getDiscountValue();
               break;
            case NewCostMedals:
               if (type != Player.CurrencyType.Medals) {
                  throw new Exception("TimedEvent Error: incorrect discount type defined for costume: " + costumeData.getId());
               }

               if (currencyOverride != -1) {
                  throw new Exception("TimedEvent Error: multiple currency costs defined for costume: " + costumeData.getId());
               }

               currencyOverride = (int)preferredEvent.getDiscountValue();
            case None:
            }
         }

         if (player.getTimedEvents().timedEventNow(TimedEventType.CostumeSale, costumeData.getId(), islandType)) {
            List<TimedEvent> playerSales = player.getTimedEvents().currentActiveOnKey(TimedEventType.CostumeSale, costumeData.getId(), islandType);

            for(int i = 0; i < playerSales.size(); ++i) {
               CostumeSalesEvent e = (CostumeSalesEvent)playerSales.get(i);
               CostumeSalesEvent.DiscountType discountType = e.getDiscountType();
               switch(discountType) {
               case Discount:
                  if (e.getDiscountValue() > currentPercentDiscount) {
                     currentPercentDiscount = e.getDiscountValue();
                  }
                  break;
               case NewCostDiamonds:
                  if (type != Player.CurrencyType.Diamonds) {
                     throw new Exception("TimedEvent Error: incorrect discount type defined for costume: " + costumeData.getId());
                  }

                  if (currencyOverride != -1) {
                     if ((int)e.getDiscountValue() < currencyOverride) {
                        currencyOverride = (int)e.getDiscountValue();
                     }
                  } else {
                     currencyOverride = (int)e.getDiscountValue();
                  }
                  break;
               case NewCostMedals:
                  if (type != Player.CurrencyType.Medals) {
                     throw new Exception("TimedEvent Error: incorrect discount type defined for costume: " + costumeData.getId());
                  }

                  if (currencyOverride != -1) {
                     if ((int)e.getDiscountValue() < currencyOverride) {
                        currencyOverride = (int)e.getDiscountValue();
                     }
                  } else {
                     currencyOverride = (int)e.getDiscountValue();
                  }
               case None:
               }
            }
         }

         newCost = originalCost;
         if (currencyOverride != -1) {
            newCost = currencyOverride;
         }

         if (currentPercentDiscount > 0.0F) {
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
            return "diamondCost";
         }

         public float Value(ISFSObject salesData) {
            return (float)salesData.getInt("diamondCost");
         }
      },
      NewCostMedals {
         public String Key() {
            return "medalCost";
         }

         public float Value(ISFSObject salesData) {
            return (float)salesData.getInt("medalCost");
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
