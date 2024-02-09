package com.bigbluebubble.mysingingmonsters.player;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventManager;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayerTimedEvents {
   long userId;
   List<TimedEvent> events_;

   public PlayerTimedEvents(long userId) {
      this.userId = userId;
      this.events_ = new ArrayList();
   }

   private void load(IDbWrapper db, String sql, Object[] params) throws PlayerLoadingException {
      try {
         this.events_.clear();
         ISFSArray results = db.query(sql, params);

         for(int i = 0; i < results.size(); ++i) {
            ISFSObject eventData = results.getSFSObject(i);

            try {
               TimedEventType eventType = TimedEventType.fromInt(eventData.getInt("event_id"));
               if (eventType == TimedEventType.None) {
                  eventType = TimedEventType.valueOf(eventData.getUtfString("event_type"));
               }

               TimedEvent timedEvent = TimedEventManager.instance().createTimedEvent(eventType, eventData);
               if (timedEvent != null) {
                  this.events_.add(timedEvent);
               }
            } catch (Exception var9) {
               Logger.trace(var9, "Error Creating PlayerTimedEvent:\n" + eventData.getDump());
            }
         }

      } catch (Exception var10) {
         throw new PlayerLoadingException(var10, String.format("Error loading player timed events for user %d", this.userId));
      }
   }

   public void load(IDbWrapper db) throws PlayerLoadingException {
      this.checkMigration(db);
      this.load(db, "SELECT * FROM user_timed_events_v2 WHERE user_id = ? AND NOW() < end_date", new Object[]{this.userId});
   }

   public void loadAll(IDbWrapper db) throws PlayerLoadingException {
      this.checkMigration(db);
      this.load(db, "SELECT * FROM user_timed_events_v2 WHERE user_id = ?", new Object[]{this.userId});
   }

   private void checkMigration(IDbWrapper db) throws PlayerLoadingException {
      try {
         ISFSArray results = db.query("SELECT * FROM user_timed_events WHERE user_id = ?", new Object[]{this.userId});
         if (results.size() > 0) {
            for(int i = 0; i < results.size(); ++i) {
               ISFSObject eventData = results.getSFSObject(i);
               db.update("INSERT INTO user_timed_events_v2 SET user_id = ?, event_id = ?, data = ?, start_date = ?, end_date = ?, count = ?, max = ?", new Object[]{eventData.getLong("user_id"), TimedEventType.valueOf(eventData.getUtfString("event_type")).getId(), eventData.getUtfString("data"), new Timestamp(eventData.getLong("start_date")), new Timestamp(eventData.getLong("end_date")), eventData.getInt("count"), eventData.getInt("max")});
            }

            db.update("DELETE FROM user_timed_events WHERE user_id = ?", new Object[]{this.userId});
         }

      } catch (Exception var5) {
         throw new PlayerLoadingException(var5, "Error migrating player timed events");
      }
   }

   public void save(IDbWrapper db) {
      try {
         Iterator var2 = this.events_.iterator();

         while(var2.hasNext()) {
            TimedEvent te = (TimedEvent)var2.next();
            this.savePlayerTimedEvent(te, db);
         }
      } catch (Exception var4) {
         Logger.trace(var4);
      }

   }

   public ISFSArray toSFSArray() {
      ISFSArray events = new SFSArray();
      Iterator var2 = this.events_.iterator();

      while(var2.hasNext()) {
         TimedEvent e = (TimedEvent)var2.next();
         events.addSFSObject(e.getSfsObject());
      }

      return events;
   }

   public boolean timedEventNow(TimedEventType timedEventType, int key, int islandType) {
      Iterator var4 = this.events_.iterator();

      TimedEvent te;
      do {
         if (!var4.hasNext()) {
            return false;
         }

         te = (TimedEvent)var4.next();
      } while(te.getEventType() != timedEventType || te.getKey() != key || !te.appliesToIsland(islandType));

      return true;
   }

   public List<TimedEvent> currentActiveOnKey(TimedEventType timedEventType, int key, int islandType) {
      List<TimedEvent> currentEvents = new ArrayList();
      Iterator var5 = this.events_.iterator();

      while(var5.hasNext()) {
         TimedEvent te = (TimedEvent)var5.next();
         if (te.getEventType() == timedEventType && te.getKey() == key && te.appliesToIsland(islandType)) {
            currentEvents.add(te);
         }
      }

      return currentEvents;
   }

   public List<TimedEvent> getEvents() {
      return this.events_;
   }

   public void addEvent(TimedEvent timedEvent) {
      timedEvent.setUserId(this.userId);
      this.events_.add(timedEvent);
   }

   public boolean hasEventOfType(TimedEventType type) {
      for(int i = 0; i < this.events_.size(); ++i) {
         if (((TimedEvent)this.events_.get(i)).getEventType() == type) {
            return true;
         }
      }

      return false;
   }

   void savePlayerTimedEvent(TimedEvent timedEvent, IDbWrapper db) throws Exception {
      if (timedEvent.isDirty) {
         String UPDATE_SQL;
         if (timedEvent.getId() > 0L) {
            UPDATE_SQL = "UPDATE user_timed_events_v2 SET count = ? WHERE id = ?";
            db.update("UPDATE user_timed_events_v2 SET count = ? WHERE id = ?", new Object[]{timedEvent.getCount(), timedEvent.getId()});
         } else {
            UPDATE_SQL = "INSERT user_timed_events_v2 SET user_id = ?, event_id = ?, data = ?, start_date = ?, end_date = ?, count = ?, max = ?";
            long id = db.insertGetId("INSERT user_timed_events_v2 SET user_id = ?, event_id = ?, data = ?, start_date = ?, end_date = ?, count = ?, max = ?", new Object[]{this.userId, timedEvent.getEventType().getId(), timedEvent.getEventData().toJson(), new Timestamp(timedEvent.getStartDate()), new Timestamp(timedEvent.getEndDate()), timedEvent.getCount(), timedEvent.getMax()});
            timedEvent.setId(id);
         }

         timedEvent.isDirty = false;
      }

   }
}
