package com.bigbluebubble.mysingingmonsters.schedules;

import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Schedule {
   static Logger logger = LoggerFactory.getLogger(Schedule.class);
   private int id;
   private List<ScheduleReservation> reservations = new ArrayList();
   private static TimeZone timeZone = TimeZone.getTimeZone("UTC");

   public int getId() {
      return this.id;
   }

   public Schedule(ISFSObject data, Map<Integer, ISFSObject> scheduleData) {
      this.id = data.getInt("id");
      String startTimes = data.getUtfString("start_times");

      try {
         if (startTimes != null && startTimes.length() > 0) {
            String[] var4 = startTimes.split(",");
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               String s = var4[var6];
               String[] scheduleInstance = s.split(":");
               this.reservations.add(new ScheduleReservation(Long.parseLong(scheduleInstance[0]), (ISFSObject)scheduleData.get(Integer.parseInt(scheduleInstance[1]))));
            }
         }
      } catch (Exception var9) {
         logger.error("Error creating schedule " + this.id + ": " + var9.getMessage());
      }

   }

   public boolean isActive() {
      return this.isActive(Calendar.getInstance(timeZone));
   }

   public boolean isActive(Calendar currentTime) {
      return this.timeRemaining(currentTime) != 0L;
   }

   public boolean isActiveToday() {
      return this.isActiveToday(Calendar.getInstance(timeZone));
   }

   public boolean isActiveToday(Calendar currentTime) {
      Iterator var2 = this.reservations.iterator();

      ScheduleReservation schedule;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         schedule = (ScheduleReservation)var2.next();
      } while(!schedule.isActiveToday(currentTime));

      return true;
   }

   public long timeRemaining() {
      return this.timeRemaining(Calendar.getInstance(timeZone));
   }

   public long timeRemaining(Calendar currentTime) {
      Iterator var2 = this.reservations.iterator();

      long remaining;
      do {
         if (!var2.hasNext()) {
            return 0L;
         }

         ScheduleReservation reservation = (ScheduleReservation)var2.next();
         remaining = reservation.timeRemaining(currentTime);
      } while(remaining == 0L);

      return remaining;
   }

   public long getCurrentRepeatStartTime() {
      Calendar currentTime = Calendar.getInstance(timeZone);
      Iterator var2 = this.reservations.iterator();

      ScheduleReservation reservation;
      long remaining;
      do {
         if (!var2.hasNext()) {
            return 0L;
         }

         reservation = (ScheduleReservation)var2.next();
         remaining = reservation.timeRemaining(currentTime);
      } while(remaining == 0L);

      return reservation.currentRepeatStartTime(currentTime.getTimeInMillis());
   }

   public ISFSObject getSFSObject() {
      return this.getSFSObject(2);
   }

   public ISFSObject getSFSObject(Calendar currentTime) {
      return this.getSFSObject(2, currentTime);
   }

   public ISFSObject getSFSObject(int daysOfStartTimes) {
      return this.getSFSObject(daysOfStartTimes, Calendar.getInstance(timeZone));
   }

   public TreeSet<StartTimeDuration> getStartTimes(int daysToCheck, Calendar currentTime) {
      ArrayList<StartTimeDuration> allStartTimes = new ArrayList();
      Iterator var4 = this.reservations.iterator();

      while(var4.hasNext()) {
         ScheduleReservation s = (ScheduleReservation)var4.next();
         allStartTimes.addAll(s.getStartTimes(daysToCheck, currentTime));
      }

      return StartTimeDuration.joinDurations(allStartTimes);
   }

   public ISFSObject getSFSObject(int daysOfStartTimes, Calendar currentTime) {
      ISFSArray startTimesData = new SFSArray();
      Iterator var4 = this.getStartTimes(daysOfStartTimes, currentTime).iterator();

      while(var4.hasNext()) {
         StartTimeDuration duration = (StartTimeDuration)var4.next();
         startTimesData.addSFSObject(duration.toSFSObject());
      }

      ISFSObject responseData = new SFSObject();
      responseData.putSFSArray("startTimes", startTimesData);
      responseData.putLong("timeRemaining", this.timeRemaining(currentTime));
      return responseData;
   }

   public StartTimeDuration getStartTimeDuration(long startTime) {
      StartTimeDuration std = null;
      Iterator var4 = this.reservations.iterator();

      while(var4.hasNext()) {
         ScheduleReservation s = (ScheduleReservation)var4.next();
         long actualStartTime = s.currentRepeatStartTime(startTime);
         std = s.getPreviousStartTimeDuration(actualStartTime);
         if (std != null) {
            break;
         }
      }

      return std;
   }

   public long getNextStartTime(int daysOfStartTimes) {
      return this.getNextStartTime(daysOfStartTimes, Calendar.getInstance(timeZone));
   }

   public long getNextStartTime(int daysToCheck, Calendar currentTime) {
      ArrayList<StartTimeDuration> allStartTimes = new ArrayList();
      Iterator var4 = this.reservations.iterator();

      while(var4.hasNext()) {
         ScheduleReservation s = (ScheduleReservation)var4.next();
         allStartTimes.addAll(s.getStartTimes(daysToCheck, currentTime));
      }

      long now = currentTime.getTimeInMillis();
      long nextStartTime = 0L;
      Iterator var8 = allStartTimes.iterator();

      while(var8.hasNext()) {
         StartTimeDuration s = (StartTimeDuration)var8.next();
         if (s.startTime > now) {
            if (nextStartTime == 0L) {
               nextStartTime = s.startTime;
            } else {
               nextStartTime = Math.min(s.startTime, nextStartTime);
            }
         }
      }

      return nextStartTime;
   }
}
