package com.bigbluebubble.mysingingmonsters.data.timed_events.events;

import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.smartfoxserver.v2.entities.data.ISFSObject;

public class CollectAllTrialEvent extends TimedEvent {
   public int getKey() {
      return 0;
   }

   public CollectAllTrialEvent(ISFSObject timedEventData) throws Exception {
      super(timedEventData);
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof CollectAllTrialEvent)) {
         return false;
      } else {
         CollectAllTrialEvent e = (CollectAllTrialEvent)o;
         return this.getStartDate() == e.getStartDate() && this.getEndDate() == e.getEndDate();
      }
   }
}
