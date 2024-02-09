package com.bigbluebubble.mysingingmonsters.data.timed_events.events;

import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.costumes.CostumeData;
import com.bigbluebubble.mysingingmonsters.costumes.CostumeLookup;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventManager;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class CostumeAvailabilityEvent extends TimedEvent {
   public static final String ID_KEY = "costume";
   private Integer costumeId;

   public int getKey() {
      return this.getCostumeId();
   }

   public CostumeAvailabilityEvent(ISFSObject timedEventData) throws Exception {
      super(timedEventData);
      if (this.eventDataSfsArray.size() != 1) {
         throw new Exception("CostumeAvailability has invalid number of entries in data field; can only have 1");
      } else {
         this.costumeId = ((SFSObject)this.eventDataSfsArray.getElementAt(0)).getInt("costume");
         if (this.costumeId == null) {
            Logger.trace("CostumeAvailability: entityId is null");
         }

      }
   }

   public Integer getCostumeId() {
      return this.costumeId;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof CostumeAvailabilityEvent)) {
         return false;
      } else {
         CostumeAvailabilityEvent e = (CostumeAvailabilityEvent)o;
         return this.costumeId == e.costumeId && this.getStartDate() == e.getStartDate() && this.getEndDate() == e.getEndDate();
      }
   }

   public String toString() {
      String str = super.toString();
      CostumeData c = CostumeLookup.get(this.costumeId);
      if (c != null) {
         str = str + " costume: " + this.costumeId + ": " + c.getName();
      } else {
         str = str + " invalid costumeId " + this.costumeId;
      }

      return str;
   }

   public static boolean hasTimedEventNow(int costumeId, Player player, int islandType) {
      return TimedEventManager.instance().hasTimedEventNow(TimedEventType.CostumeAvailability, costumeId, islandType) || player.getTimedEvents().timedEventNow(TimedEventType.CostumeAvailability, costumeId, islandType);
   }
}
