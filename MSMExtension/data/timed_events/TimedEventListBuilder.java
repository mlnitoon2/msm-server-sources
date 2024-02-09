package com.bigbluebubble.mysingingmonsters.data.timed_events;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.data.VersionData;
import com.bigbluebubble.mysingingmonsters.exceptions.InvalidTimedEventException;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Map.Entry;

class TimedEventListBuilder {
   private static final int MS_NEAR_FUTURE = 14400000;
   private TreeMap<Long, List<ISFSObject>> inactiveFutureEventsSortedByStartDate = new TreeMap();
   private TreeMap<Long, List<TimedEvent>> nearFutureEventsSortedByEndDate = new TreeMap();
   private TreeMap<VersionInfo, SFSArray> cachedNearFutureEvents = new TreeMap();
   private static final int NUM_EVENT_UPDATES_BEFORE_SFS_UPDATED = 0;
   private int elapsedUpdatesSinceLastSFSUpdate = 0;

   public TimedEventListBuilder() {
   }

   public SFSArray activeEventsInNearFuture(VersionInfo clientVersion) {
      if (clientVersion == null) {
         return null;
      } else {
         VersionInfo serverVersion = VersionData.Instance().getMaxServerVersionFromClientVersion(clientVersion);
         NavigableMap<VersionInfo, SFSArray> it = this.cachedNearFutureEvents.headMap(serverVersion, true);
         Entry<VersionInfo, SFSArray> lastEntry = it.lastEntry();
         return lastEntry != null ? (SFSArray)lastEntry.getValue() : new SFSArray();
      }
   }

   protected void addNewEventFromDb(ISFSObject s) throws InvalidTimedEventException {
      this.verifyType(s);
      Long startDate = s.getLong("start_date");
      Object listOfEventsForStartDate;
      if (!this.inactiveFutureEventsSortedByStartDate.containsKey(startDate)) {
         listOfEventsForStartDate = new ArrayList();
         this.inactiveFutureEventsSortedByStartDate.put(startDate, listOfEventsForStartDate);
      } else {
         listOfEventsForStartDate = (List)this.inactiveFutureEventsSortedByStartDate.get(startDate);
      }

      ((List)listOfEventsForStartDate).add(s);
   }

   private void verifyType(ISFSObject s) throws InvalidTimedEventException {
      if (s.getInt("event_id") <= 0) {
         TimedEventType[] values = TimedEventType.values();
         String typeEnumStr = s.getUtfString("event_type");
         boolean found = false;
         TimedEventType[] var5 = values;
         int var6 = values.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            TimedEventType t = var5[var7];
            if (t.toString().equals(typeEnumStr)) {
               found = true;
               break;
            }
         }

         if (!found) {
            throw new InvalidTimedEventException(String.format("Invalid TimedEventType enum '%s' from timed_events table", typeEnumStr));
         }
      }
   }

   protected void updateActiveTimedEvents() {
      try {
         this.removeFinishedEvents();
         this.addNewlyActiveEvents();
         ++this.elapsedUpdatesSinceLastSFSUpdate;
         if (this.elapsedUpdatesSinceLastSFSUpdate > 0) {
            this.updateCachedSFSArrayOfEvents();
            this.elapsedUpdatesSinceLastSFSUpdate = 0;
         }
      } catch (Exception var2) {
         Logger.trace(var2, "Failure in UpdateActiveTimedEvents");
      }

   }

   private void removeFinishedEvents() {
      Iterator eventDatesThatHaveEnded = this.nearFutureEventsSortedByEndDate.headMap(MSMExtension.CurrentDBTime()).values().iterator();

      while(eventDatesThatHaveEnded.hasNext()) {
         for(Iterator eventsWithEndDateIterator = ((List)eventDatesThatHaveEnded.next()).iterator(); eventsWithEndDateIterator.hasNext(); eventsWithEndDateIterator.remove()) {
            TimedEvent event = (TimedEvent)eventsWithEndDateIterator.next();
            Logger.trace("REMOVE " + event.toString());

            try {
               TimedEventManager.instance().remove(event);
            } catch (Exception var5) {
               Logger.trace(var5);
            }
         }

         eventDatesThatHaveEnded.remove();
      }

   }

   private void addNewlyActiveEvents() throws Exception {
      Long nearFutureFromNow = MSMExtension.CurrentDBTime() + 14400000L;
      Iterator newlyStartedInactiveEvents = this.inactiveFutureEventsSortedByStartDate.headMap(nearFutureFromNow).values().iterator();

      while(newlyStartedInactiveEvents.hasNext()) {
         List<ISFSObject> listOfEventsForStartDate = (List)newlyStartedInactiveEvents.next();

         for(int i = 0; i < listOfEventsForStartDate.size(); ++i) {
            ISFSObject currentEvent = (ISFSObject)listOfEventsForStartDate.get(i);
            this.addToActiveEvents(currentEvent);
         }

         newlyStartedInactiveEvents.remove();
      }

   }

   private void addToActiveEvents(ISFSObject s) throws Exception {
      Long endDate = s.getLong("end_date");
      TimedEventType type = TimedEventType.fromInt(s.getInt("event_id"));
      if (type == TimedEventType.None) {
         type = TimedEventType.valueOf(s.getUtfString("event_type"));
      }

      if (type != TimedEventType.None) {
         TimedEvent e = TimedEventManager.instance().addEvent(type, s);
         if (e != null) {
            Object listOfEventsWithEndDate;
            if (!this.nearFutureEventsSortedByEndDate.containsKey(endDate)) {
               listOfEventsWithEndDate = new ArrayList();
               this.nearFutureEventsSortedByEndDate.put(endDate, listOfEventsWithEndDate);
            } else {
               listOfEventsWithEndDate = (List)this.nearFutureEventsSortedByEndDate.get(endDate);
            }

            Logger.trace("ADD: " + e.toString());
            ((List)listOfEventsWithEndDate).add(e);
         } else {
            Logger.trace("Specific type class for " + type.name() + " does not exist yet, do not add to active events list");
         }
      }

   }

   private void updateCachedSFSArrayOfEvents() {
      VersionInfo minServerVer = VersionData.Instance().getMaxServerVersionFromClientVersion(new VersionInfo(GameSettings.get("MIN_CLIENT_VERSION", "0.0")));
      TreeMap<VersionInfo, SFSArray> tempBuilderArray = new TreeMap();
      Iterator it = VersionData.Instance().iterator();

      while(it.hasNext()) {
         Entry<VersionInfo, VersionInfo> v = (Entry)it.next();
         if (minServerVer.lessThanEqual((VersionInfo)v.getValue())) {
            tempBuilderArray.put(v.getValue(), new SFSArray());
         }
      }

      Iterator eventEndDateItr = this.nearFutureEventsSortedByEndDate.values().iterator();

      while(eventEndDateItr.hasNext()) {
         List<TimedEvent> allEventsForCurrentEndDate = (List)eventEndDateItr.next();

         for(int i = 0; i < allEventsForCurrentEndDate.size(); ++i) {
            TimedEvent curTimedEvent = (TimedEvent)allEventsForCurrentEndDate.get(i);
            Iterator versionCaches = tempBuilderArray.entrySet().iterator();

            while(versionCaches.hasNext()) {
               Entry<VersionInfo, SFSArray> cache = (Entry)versionCaches.next();
               SFSArray sfsArr = (SFSArray)cache.getValue();
               if (curTimedEvent.getMinServerVersion().lessThanEqual((VersionInfo)cache.getKey())) {
                  sfsArr.addSFSObject(curTimedEvent.getSfsObject());
               }
            }
         }
      }

      synchronized(this.cachedNearFutureEvents) {
         this.cachedNearFutureEvents = tempBuilderArray;
      }
   }
}
