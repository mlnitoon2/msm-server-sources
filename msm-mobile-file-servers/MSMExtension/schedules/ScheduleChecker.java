package com.bigbluebubble.mysingingmonsters.schedules;

import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.mysingingmonsters.Logger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.TimeZone;

public class ScheduleChecker {
   public static void Check(Collection<Schedule> schedules, long startTime, int numDaysToCheck) {
      Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
      calendar.setTimeInMillis(startTime);
      calendar.set(11, 0);
      calendar.set(12, 0);
      calendar.set(13, 0);
      calendar.set(14, 0);
      String dateFormatString = "E, MMM d, yyyy HH:mm:ss z";
      SimpleDateFormat sdf = new SimpleDateFormat("E, MMM d, yyyy HH:mm:ss z");
      sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
      String dateFormatString2 = "HH:mm:ss z";
      SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss z");
      sdf2.setTimeZone(TimeZone.getTimeZone("UTC"));
      Logger.trace(LogLevel.INFO, "===============================================================================");
      Logger.trace(LogLevel.INFO, "Schedule Report For Next " + numDaysToCheck + " Days:");
      Logger.trace(LogLevel.INFO, "===============================================================================");

      for(int i = 0; i < numDaysToCheck; ++i) {
         Logger.trace(sdf.format(calendar.getTime()));
         Iterator var10 = schedules.iterator();

         while(var10.hasNext()) {
            Schedule schedule = (Schedule)var10.next();
            boolean isScheduleActive = schedule.isActive(calendar);
            long timeRemaining = schedule.timeRemaining(calendar);
            if (isScheduleActive) {
               Logger.trace(LogLevel.INFO, "\tSchedule " + schedule.getId() + " is active! Remaining time: " + (double)(timeRemaining / 1000L / 60L) / 60.0D + " hrs");
            } else if (schedule.isActiveToday(calendar)) {
               StartTimeDuration std = (StartTimeDuration)schedule.getStartTimes(1, calendar).first();
               Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
               c.setTimeInMillis(std.startTime);
               Logger.trace("\tSchedule " + schedule.getId() + " will be active: " + sdf2.format(c.getTime()) + " Duration: " + (double)(std.duration / 1000L / 60L) / 60.0D + " hrs");
            }
         }

         calendar.add(5, 1);
      }

      Logger.trace(LogLevel.INFO, "===============================================================================");
   }
}
