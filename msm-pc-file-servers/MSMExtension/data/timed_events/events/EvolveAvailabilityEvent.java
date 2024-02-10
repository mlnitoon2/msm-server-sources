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

public class EvolveAvailabilityEvent extends TimedEvent {
   private static final String ENTITY_ID_KEY = "from_entity";
   private Integer entityId;

   public int getKey() {
      return this.getEntityId();
   }

   public EvolveAvailabilityEvent(ISFSObject timedEventData) throws Exception {
      super(timedEventData);
      if (this.eventDataSfsArray.size() != 1) {
         throw new Exception("EvolveAvailabilityEvent has invalid number of entries in data field; can only have 1");
      } else {
         this.entityId = ((SFSObject)this.eventDataSfsArray.getElementAt(0)).getInt("from_entity");
         if (this.entityId == null) {
            Logger.trace("EvolveAvailabilityEvent: from_entity is null");
         }

      }
   }

   public Integer getEntityId() {
      return this.entityId;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof EvolveAvailabilityEvent)) {
         return false;
      } else {
         EvolveAvailabilityEvent e = (EvolveAvailabilityEvent)o;
         return this.entityId == e.entityId && this.getStartDate() == e.getStartDate() && this.getEndDate() == e.getEndDate();
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

      return str + " from_entity: " + this.entityId + ": " + ((Entity)e).getName();
   }

   public static boolean hasTimedEventNow(Entity entity, Player player, int islandType) {
      return TimedEventManager.instance().hasTimedEventNow(TimedEventType.EvolveAvailability, entity.getEntityId(), islandType) || player.getTimedEvents().timedEventNow(TimedEventType.EvolveAvailability, entity.getEntityId(), islandType);
   }
}
