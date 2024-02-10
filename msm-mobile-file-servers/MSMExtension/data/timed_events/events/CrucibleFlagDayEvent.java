package com.bigbluebubble.mysingingmonsters.data.timed_events.events;

import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.mysingingmonsters.data.Entity;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventManager;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.bigbluebubble.mysingingmonsters.player.PlayerCrucibleData;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class CrucibleFlagDayEvent extends TimedEvent {
   private final boolean[] flagsActive = new boolean[]{false, false, false, false, false, false};

   public int getKey() {
      return 0;
   }

   public boolean flagActive(char gene) {
      int ind = PlayerCrucibleData.elementalInd(gene);
      return ind == -1 ? false : this.flagsActive[ind];
   }

   public CrucibleFlagDayEvent(ISFSObject timedEventData) throws Exception {
      super(timedEventData);
      if (this.eventDataSfsArray.size() < 1) {
         throw new Exception("CrucibleFlagDayEvent has invalid number of entries in data field; can only have 1");
      } else {
         ISFSObject eventData = (SFSObject)this.eventDataSfsArray.getElementAt(0);
         this.updateFlags(eventData);
      }
   }

   public void updateFlags(ISFSObject eventData) {
      for(int i = 0; i < PlayerCrucibleData.elementals.length; ++i) {
         String elementStr = String.valueOf(PlayerCrucibleData.elementals[i]);
         String elementKey = "flag" + elementStr;
         if (eventData.containsKey(elementKey)) {
            this.flagsActive[i] = SFSHelpers.getInt(eventData.get(elementKey)) != 0;
         } else {
            this.flagsActive[i] = false;
         }
      }

   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof CrucibleFlagDayEvent)) {
         return false;
      } else {
         CrucibleFlagDayEvent e = (CrucibleFlagDayEvent)o;
         if (this.getStartDate() == e.getStartDate() && this.getEndDate() == e.getEndDate()) {
            for(int i = 0; i < PlayerCrucibleData.elementals.length; ++i) {
               if (this.flagsActive[i] != e.flagsActive[i]) {
                  return false;
               }
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public String toString() {
      String str = super.toString();
      str = str + " for flags: ";

      for(int i = 0; i < PlayerCrucibleData.elementals.length; ++i) {
         if (this.flagActive(PlayerCrucibleData.elementals[i])) {
            str = str + PlayerCrucibleData.elementals[i] + " ";
         }
      }

      return str;
   }

   public static boolean hasTimedEventNow(Entity entity, Player player, int islandType) {
      return TimedEventManager.instance().hasTimedEventNow(TimedEventType.CrucibleFlagDay, entity.getEntityId(), islandType) || player.getTimedEvents().timedEventNow(TimedEventType.CrucibleFlagDay, entity.getEntityId(), islandType);
   }
}
