package com.bigbluebubble.mysingingmonsters.schedules;

import com.bigbluebubble.mysingingmonsters.Logger;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

public class ScheduleReservation {
   private final int id;
   private long startTime = Long.MAX_VALUE;
   private int maxRepeatCount = -1;
   private String name = "MISSING_NAME";
   private int repeatPeriodInDays = 7;
   private int durationInDays = 7;
   TreeSet<StartTimeDuration> startTimesInRepeat = new TreeSet();
   public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");
   private static final long MS_PER_DAY = 86400000L;

   public int getId() {
      return this.id;
   }

   public long getStartTime() {
      return this.startTime;
   }

   public int getMaxRepeatCount() {
      return this.maxRepeatCount;
   }

   public String getName() {
      return this.name;
   }

   public int getRepeatPeriodInDays() {
      return this.repeatPeriodInDays;
   }

   public int getDurationInDays() {
      return this.durationInDays;
   }

   public ScheduleReservation(long instanceStartTime, ISFSObject data) {
      this.id = data.getInt("id");
      this.name = data.getUtfString("name");
      this.startTime = instanceStartTime + this.stringToTime(data.getUtfString("start_time"));
      this.repeatPeriodInDays = data.getInt("repeat_interval_days");
      this.maxRepeatCount = data.getInt("max_repeats");
      this.durationInDays = data.getInt("duration_days");
      ArrayList<StartTimeDuration> startTimeDurations = new ArrayList();
      String[] days;
      int var9;
      if (data.containsKey("start_time_duration")) {
         String startTimeDurationStr = data.getUtfString("start_time_duration");
         if (startTimeDurationStr.length() > 0) {
            String[] durations = startTimeDurationStr.split(",");
            days = durations;
            int var8 = durations.length;

            for(var9 = 0; var9 < var8; ++var9) {
               String startTimeString = days[var9];

               try {
                  String[] times = startTimeString.substring(0, startTimeString.length() - 1).split("\\(");
                  long startTime = this.stringToTime(times[0]);
                  long duration = TimeUnit.MINUTES.toMillis((long)(60.0F * Float.parseFloat(times[1])));
                  startTimeDurations.add(new StartTimeDuration(startTime, duration));
               } catch (Exception var16) {
                  Logger.trace(var16, "PARSE ERROR ON START TIME DURATION (Schedule " + this.id + ")");
               }
            }
         }
      } else {
         startTimeDurations.add(new StartTimeDuration(this.startTime % 86400000L, 86400000L));
      }

      ArrayList<DayOfWeek> daysActive = new ArrayList(Arrays.asList(DayOfWeek.values()));
      if (data.containsKey("days")) {
         String daysStr = data.getUtfString("days");
         if (daysStr.length() > 0) {
            daysActive.clear();
            days = daysStr.split(",");
            String[] var19 = days;
            var9 = days.length;

            for(int var20 = 0; var20 < var9; ++var20) {
               String day = var19[var20];
               daysActive.add(DayOfWeek.valueOf(day.replaceAll("'", "")));
            }
         }
      }

      this.startTimesInRepeat = calculateStartTimes(this.startTime, this.durationInDays, daysActive, startTimeDurations);
   }

   public boolean isActiveToday(Calendar currentTime) {
      Calendar midnight = Calendar.getInstance(UTC_TIME_ZONE);
      midnight.setTimeInMillis(currentTime.getTimeInMillis() - currentTime.getTimeInMillis() % 86400000L);
      StartTimeDuration nextDuration = this.getNextStartTimeDuration(midnight);
      StartTimeDuration lastDuration = this.getPreviousStartTimeDuration(midnight);
      boolean lastOverlaps = lastDuration != null && lastDuration.startTime + lastDuration.duration > midnight.getTimeInMillis();
      boolean nextIsToday = nextDuration != null && nextDuration.startTime < midnight.getTimeInMillis() + 86400000L;
      return lastOverlaps || nextIsToday;
   }

   public long nextStartTime(Calendar currentTime) {
      StartTimeDuration nextDuration = this.getNextStartTimeDuration(currentTime);
      return nextDuration == null ? -1L : nextDuration.startTime;
   }

   public long timeRemaining(Calendar currentTime) {
      long time = currentTime.getTimeInMillis();
      if (time >= this.startTime) {
         if (this.repeatPeriodInDays == this.durationInDays && this.startTimeDurationIsFullPeriod()) {
            if (this.maxRepeatCount == -1) {
               return -1L;
            }

            return Math.max(0L, this.startTime + (long)((this.maxRepeatCount + 1) * this.repeatPeriodInDays) * 86400000L - time);
         }

         StartTimeDuration lastStartedDuration = this.getPreviousStartTimeDuration(currentTime);
         if (lastStartedDuration != null && lastStartedDuration.startTime + lastStartedDuration.duration > time) {
            return Math.max(0L, lastStartedDuration.startTime + lastStartedDuration.duration - time);
         }
      }

      return 0L;
   }

   public ArrayList<StartTimeDuration> getStartTimes(int daysToCheck, Calendar startTime) {
      ArrayList<StartTimeDuration> startTimes = new ArrayList();
      long startTimeTime = startTime.getTimeInMillis();
      Calendar currentDay = Calendar.getInstance(UTC_TIME_ZONE);
      currentDay.setTimeInMillis(startTime.getTimeInMillis() - startTime.getTimeInMillis() % 86400000L);
      StartTimeDuration currentDuration = this.getPreviousStartTimeDuration(startTime);
      if (currentDuration == null) {
         currentDuration = this.getNextStartTimeDuration(startTime);
      }

      for(int i = 0; i <= daysToCheck; ++i) {
         long currentDurationEndTime;
         for(long currentDayTime = currentDay.getTimeInMillis(); currentDuration != null && currentDuration.startTime < currentDayTime; currentDuration = this.getNextStartTimeDuration(currentDurationEndTime)) {
            currentDurationEndTime = currentDuration.startTime + currentDuration.duration;
            if (currentDurationEndTime > startTimeTime) {
               startTimes.add(currentDuration);
            }
         }

         currentDay.add(5, 1);
      }

      return startTimes;
   }

   private long stringToTime(String s) {
      String[] clock = s.split(":");
      return TimeUnit.HOURS.toMillis((long)Integer.parseInt(clock[0])) + TimeUnit.MINUTES.toMillis((long)Integer.parseInt(clock[1]));
   }

   private int currentRepeat(long currentTime) {
      return (int)(TimeUnit.MILLISECONDS.toDays(currentTime - this.startTime) / (long)this.repeatPeriodInDays);
   }

   public long currentRepeatStartTime(long currentTime) {
      long dayDifference = TimeUnit.MILLISECONDS.toDays(currentTime - this.startTime);
      return this.startTime + 86400000L * (dayDifference - dayDifference % (long)this.repeatPeriodInDays);
   }

   private long nextRepeatStartTime(long currentTime) {
      return this.currentRepeatStartTime(currentTime) + 86400000L * (long)this.repeatPeriodInDays;
   }

   private long previousRepeatStartTime(long currentTime) {
      return this.currentRepeatStartTime(currentTime) - 86400000L * (long)this.repeatPeriodInDays;
   }

   private long absoluteToRelativeTimeInRepeat(long currentTime) {
      return currentTime - this.currentRepeatStartTime(currentTime);
   }

   private boolean startTimeDurationIsFullPeriod() {
      if (this.startTimesInRepeat.size() != 1) {
         return false;
      } else {
         StartTimeDuration duration = (StartTimeDuration)this.startTimesInRepeat.first();
         return duration.duration == (long)this.repeatPeriodInDays * 86400000L && duration.startTime == 0L;
      }
   }

   public StartTimeDuration getPreviousStartTimeDuration(Calendar currentTime) {
      return this.getPreviousStartTimeDuration(currentTime.getTimeInMillis());
   }

   public StartTimeDuration getPreviousStartTimeDuration(long currentTime) {
      if (!this.startTimesInRepeat.isEmpty()) {
         StartTimeDuration s = (StartTimeDuration)this.startTimesInRepeat.floor(new StartTimeDuration(this.absoluteToRelativeTimeInRepeat(currentTime), 0L));
         if (s == null && this.currentRepeat(currentTime) > 0) {
            s = (StartTimeDuration)this.startTimesInRepeat.last();
            if (s != null) {
               return new StartTimeDuration(this.previousRepeatStartTime(currentTime) + s.startTime, s.duration);
            }
         } else if (s != null && (this.maxRepeatCount == -1 || this.currentRepeat(currentTime) >= 0 && this.currentRepeat(currentTime) <= this.maxRepeatCount)) {
            return new StartTimeDuration(this.currentRepeatStartTime(currentTime) + s.startTime, s.duration);
         }
      }

      return null;
   }

   private StartTimeDuration getNextStartTimeDuration(Calendar currentTime) {
      return this.getNextStartTimeDuration(currentTime.getTimeInMillis());
   }

   private StartTimeDuration getNextStartTimeDuration(long currentTime) {
      if (!this.startTimesInRepeat.isEmpty()) {
         StartTimeDuration s = (StartTimeDuration)this.startTimesInRepeat.ceiling(new StartTimeDuration(this.absoluteToRelativeTimeInRepeat(currentTime), 0L));
         if (s != null || this.maxRepeatCount != -1 && (this.currentRepeat(currentTime) < 0 || this.currentRepeat(currentTime) >= this.maxRepeatCount)) {
            if (s != null && (this.maxRepeatCount == -1 || this.currentRepeat(currentTime) >= 0 && this.currentRepeat(currentTime) <= this.maxRepeatCount)) {
               return new StartTimeDuration(this.currentRepeatStartTime(currentTime) + s.startTime, s.duration);
            }
         } else {
            s = (StartTimeDuration)this.startTimesInRepeat.first();
            if (s != null) {
               return new StartTimeDuration(this.nextRepeatStartTime(currentTime) + s.startTime, s.duration);
            }
         }
      }

      return null;
   }

   private static TreeSet<StartTimeDuration> calculateStartTimes(long startTime, int durationInDays, ArrayList<DayOfWeek> validDays, ArrayList<StartTimeDuration> startTimeDurations) {
      ArrayList<StartTimeDuration> unjoinedStartTimeDurations = new ArrayList();
      Calendar startDate = Calendar.getInstance(UTC_TIME_ZONE);
      startDate.setTimeInMillis(startTime);
      Calendar currentDate = (Calendar)startDate.clone();
      currentDate.setTimeInMillis(startTime - startTime % 86400000L);

      for(int i = 0; i < durationInDays; ++i) {
         if (validDays.contains(convertCalendarDay(currentDate.get(7)))) {
            Iterator var9 = startTimeDurations.iterator();

            label28:
            while(true) {
               StartTimeDuration startTimeDuration;
               long actualStartTime;
               long difference;
               do {
                  if (!var9.hasNext()) {
                     break label28;
                  }

                  startTimeDuration = (StartTimeDuration)var9.next();
                  long relativeStartTime = currentDate.getTimeInMillis() - startDate.getTimeInMillis() + startTimeDuration.startTime;
                  actualStartTime = Math.max(0L, relativeStartTime);
                  difference = actualStartTime - relativeStartTime;
               } while(difference != 0L && difference >= startTimeDuration.duration);

               unjoinedStartTimeDurations.add(new StartTimeDuration(actualStartTime, startTimeDuration.duration - difference));
            }
         }

         currentDate.add(5, 1);
      }

      return StartTimeDuration.joinDurations(unjoinedStartTimeDurations);
   }

   private static DayOfWeek convertCalendarDay(int day) {
      switch(day) {
      case 1:
      default:
         return DayOfWeek.SUNDAY;
      case 2:
         return DayOfWeek.MONDAY;
      case 3:
         return DayOfWeek.TUESDAY;
      case 4:
         return DayOfWeek.WEDNESDAY;
      case 5:
         return DayOfWeek.THURSDAY;
      case 6:
         return DayOfWeek.FRIDAY;
      case 7:
         return DayOfWeek.SATURDAY;
      }
   }
}
