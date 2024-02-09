package com.bigbluebubble.mysingingmonsters.schedules;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ScheduleLookup {
   private static ConcurrentHashMap<Integer, ISFSObject> reservations = new ConcurrentHashMap();
   private static ConcurrentHashMap<Integer, Schedule> schedules = new ConcurrentHashMap();

   public static void init(IDbWrapper db) throws Exception {
      ConcurrentHashMap<Integer, ISFSObject> reservationData = new ConcurrentHashMap();
      String sql = "SELECT * FROM schedule_reservations";
      ISFSArray results = db.query(sql);
      Iterator i = results.iterator();

      while(i.hasNext()) {
         SFSObject data = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         reservationData.put(data.getInt("id"), data);
      }

      ConcurrentHashMap<Integer, Schedule> scheduleData = new ConcurrentHashMap();
      results = db.query("SELECT * FROM schedules");
      i = results.iterator();

      while(i.hasNext()) {
         SFSObject data = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         scheduleData.put(data.getInt("id"), new Schedule(data, reservationData));
      }

      reservations = reservationData;
      schedules = scheduleData;
   }

   public static final ISFSObject reservation(int reservationId) {
      return (ISFSObject)reservations.get(reservationId);
   }

   public static final Schedule schedule(int scheduleId) {
      return (Schedule)schedules.get(scheduleId);
   }

   public static <T extends ISchedulable> ArrayList<T> getActiveTodayOrTomorrow(Iterable<T> things) {
      ArrayList<T> activeThings = new ArrayList();
      Calendar today = Calendar.getInstance(ScheduleReservation.UTC_TIME_ZONE);
      Calendar tomorrow = (Calendar)today.clone();
      tomorrow.add(5, 1);
      Iterator var4 = things.iterator();

      while(true) {
         ISchedulable thing;
         Schedule schedule;
         do {
            if (!var4.hasNext()) {
               return activeThings;
            }

            thing = (ISchedulable)var4.next();
            schedule = schedule(thing.getScheduleID());
         } while(!schedule.isActiveToday(today) && !schedule.isActiveToday(tomorrow));

         activeThings.add(thing);
      }
   }
}
