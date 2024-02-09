package com.bigbluebubble.mysingingmonsters.data.timed_events.events;

import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventManager;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.bigbluebubble.mysingingmonsters.player.PlayerFuzeBuddy;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.List;

public class ShortenedFuzingEvent extends TimedEvent {
   private static final String PERCENTAGE_KEY = "percent";
   private static final String NEW_TIME_KEY = "new_time";
   ShortenedFuzingEvent.DecreaseType decreaseType;
   float value;

   public int getKey() {
      return 0;
   }

   public ShortenedFuzingEvent.DecreaseType getDecreaseType() {
      return this.decreaseType;
   }

   public float getValue() {
      return this.value;
   }

   public ShortenedFuzingEvent(ISFSObject timedEventData) throws Exception {
      super(timedEventData);
      this.decreaseType = ShortenedFuzingEvent.DecreaseType.None;
      this.value = 0.0F;
      if (this.eventDataSfsArray.size() != 1) {
         throw new Exception("ShortenedFuzing has invalid number of entries in data field; can only have 1");
      } else {
         ISFSObject eventData = (SFSObject)this.eventDataSfsArray.getElementAt(0);
         this.setDecreaseType(eventData);
         this.value = this.decreaseType.Value(eventData);
      }
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ShortenedFuzingEvent)) {
         return false;
      } else {
         ShortenedFuzingEvent e = (ShortenedFuzingEvent)o;
         return this.decreaseType == e.decreaseType && this.value == e.value && this.getStartDate() == e.getStartDate() && this.getEndDate() == e.getEndDate();
      }
   }

   private void setDecreaseType(ISFSObject salesData) throws Exception {
      ShortenedFuzingEvent.DecreaseType[] enumValues = ShortenedFuzingEvent.DecreaseType.values();
      ShortenedFuzingEvent.DecreaseType[] var3 = enumValues;
      int var4 = enumValues.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ShortenedFuzingEvent.DecreaseType t = var3[var5];
         if (t.isDefined(salesData)) {
            if (this.decreaseType != ShortenedFuzingEvent.DecreaseType.None) {
               throw new Exception("Multiple Decrease Type defined for ShortenedFuzing timed event!! " + salesData.toJson() + ": only one of the following is permitted in a single event: " + "percent" + " or " + "new_time");
            }

            this.decreaseType = t;
         }
      }

      if (this.decreaseType == ShortenedFuzingEvent.DecreaseType.None) {
         throw new Exception("Missing or invalid Decrease Type specified for ShortenedFuzing timed event!! " + salesData.toJson());
      }
   }

   private int getEventFuzeTime() {
      if (this.decreaseType == ShortenedFuzingEvent.DecreaseType.NewTime) {
         return (int)(this.value * 1000.0F);
      } else {
         int origFuzeTime = PlayerFuzeBuddy.BuddyStructure.getBuildTimeMs();
         int newTime = origFuzeTime;
         float currentPercentDiscount = this.getValue();
         if (currentPercentDiscount != 0.0F) {
            newTime = (int)((float)origFuzeTime * (1.0F - currentPercentDiscount) + 0.5F);
         }

         if (newTime < 0) {
            newTime = 0;
         } else if (newTime > origFuzeTime) {
            Logger.trace("SOME SORT OF PROBLEM HERE: TimedEvents: newCost ends up more than original cost for torch sale defaulting to original price");
            newTime = origFuzeTime;
         }

         return newTime;
      }
   }

   public String toString() {
      String str = super.toString();

      try {
         return str + " Torch Lighting sale on to DecreaseType: " + this.getDecreaseType() + " value: " + this.getValue();
      } catch (Exception var3) {
         return str + var3.toString();
      }
   }

   public static boolean hasTimedEventNow(Player player, int islandType) {
      return TimedEventManager.instance().hasTimedEventNow(TimedEventType.ShortenedFuzing, 0, islandType) || player.getTimedEvents().timedEventNow(TimedEventType.ShortenedFuzing, 0, islandType);
   }

   public static int getTimedEventFuzeTime(Player player, int originalFuzeTime, int islandType) {
      int fuzeTime = originalFuzeTime;
      List<TimedEvent> globalEvents = TimedEventManager.instance().currentActiveOnKey(TimedEventType.ShortenedFuzing, 0, islandType);
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
         fuzeTime = ((ShortenedFuzingEvent)globalEvents.get(0)).getEventFuzeTime();
      }

      List<TimedEvent> playerFuzeEvents = player.getTimedEvents().currentActiveOnKey(TimedEventType.ShortenedFuzing, 0, islandType);
      if (playerFuzeEvents.size() > 0) {
         int t = ((ShortenedFuzingEvent)playerFuzeEvents.get(0)).getEventFuzeTime();
         if (t < fuzeTime) {
            fuzeTime = t;
         }
      }

      return fuzeTime;
   }

   public static enum DecreaseType {
      None {
         public String Key() {
            return null;
         }

         public float Value(ISFSObject salesData) {
            return 0.0F;
         }
      },
      Percentage {
         public String Key() {
            return "percent";
         }

         public float Value(ISFSObject salesData) {
            return salesData.getFloat("percent");
         }
      },
      NewTime {
         public String Key() {
            return "new_time";
         }

         public float Value(ISFSObject salesData) {
            return (float)salesData.getInt("new_time");
         }
      };

      private DecreaseType() {
      }

      public abstract String Key();

      public boolean isDefined(ISFSObject salesData) {
         String key = this.Key();
         return key != null && salesData.containsKey(key);
      }

      public abstract float Value(ISFSObject var1);

      // $FF: synthetic method
      DecreaseType(Object x2) {
         this();
      }
   }
}
