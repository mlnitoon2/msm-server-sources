package com.bigbluebubble.mysingingmonsters.data.timed_events;

import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TimedEventList {
   protected ConcurrentHashMap<Integer, List<TimedEvent>> eventsInNearFuture = new ConcurrentHashMap();

   public TimedEvent addEvent(TimedEvent timedEvent) {
      int key = timedEvent.getKey();
      Object prevEntityEvents;
      if (!this.eventsInNearFuture.containsKey(key)) {
         prevEntityEvents = new ArrayList();
         this.eventsInNearFuture.put(key, prevEntityEvents);
      } else {
         prevEntityEvents = (List)this.eventsInNearFuture.get(key);
      }

      this.checkForDuplicate((List)prevEntityEvents, timedEvent);
      ((List)prevEntityEvents).add(timedEvent);
      return timedEvent;
   }

   private void checkForDuplicate(List<TimedEvent> eventsOnThisKey, TimedEvent newTimedEvent) {
      Iterator var3 = eventsOnThisKey.iterator();

      while(var3.hasNext()) {
         TimedEvent e = (TimedEvent)var3.next();
         if (e.getStartDate() < newTimedEvent.getEndDate() && newTimedEvent.getStartDate() < e.getEndDate()) {
            Logger.trace("===============================WARNING======================================\nWARNING adding timed_event overlapping with same type of event on item\noverlap of event id " + newTimedEvent.getId() + " with event id " + e.getId() + "\n==========================================================================");
         }
      }

   }

   public void remove(TimedEvent e) throws Exception {
      int key = e.getKey();
      if (this.eventsInNearFuture.containsKey(key)) {
         List<TimedEvent> prevEntityEvents = (List)this.eventsInNearFuture.get(key);
         boolean found = false;
         Iterator entityEventItr = prevEntityEvents.iterator();

         while(entityEventItr.hasNext()) {
            if (e.equals(entityEventItr.next())) {
               entityEventItr.remove();
               found = true;
               break;
            }
         }

         if (!found) {
            throw new Exception(String.format("Failure to remove TimedEvent from sale item's %d prevEntityEvents: event with start date does not match", key));
         }
      }

   }

   public boolean timedEventNow(int key, int islandType) {
      if (!this.eventsInNearFuture.containsKey(key)) {
         return false;
      } else {
         List<TimedEvent> eventsActiveInNearFuture = (List)this.eventsInNearFuture.get(key);

         for(int i = 0; i < eventsActiveInNearFuture.size(); ++i) {
            TimedEvent availEvent = (TimedEvent)eventsActiveInNearFuture.get(i);
            Long currentTime = MSMExtension.CurrentDBTime();
            if (availEvent.getStartDate() < currentTime && currentTime < availEvent.getEndDate() && availEvent.appliesToIsland(islandType)) {
               return true;
            }
         }

         return false;
      }
   }

   public List<TimedEvent> currentActiveOnKey(int key, int islandType) {
      if (!this.eventsInNearFuture.containsKey(key)) {
         return Collections.emptyList();
      } else {
         List<TimedEvent> currentActive = new ArrayList();
         List<TimedEvent> eventsActiveInNearFuture = (List)this.eventsInNearFuture.get(key);

         for(int i = 0; i < eventsActiveInNearFuture.size(); ++i) {
            TimedEvent availEvent = (TimedEvent)eventsActiveInNearFuture.get(i);
            Long currentTime = MSMExtension.CurrentDBTime();
            if (availEvent.getStartDate() < currentTime && currentTime < availEvent.getEndDate() && availEvent.appliesToIsland(islandType)) {
               currentActive.add(availEvent);
            }
         }

         return currentActive;
      }
   }
}
