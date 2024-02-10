package com.bigbluebubble.mysingingmonsters.schedules;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

public class StartTimeDuration implements Comparable<StartTimeDuration> {
   public final long startTime;
   public final long duration;

   public StartTimeDuration(long startTime, long duration) {
      this.startTime = startTime;
      this.duration = duration;
   }

   public int compareTo(StartTimeDuration otherStartTime) {
      long difference = this.startTime - otherStartTime.startTime;
      if (difference == 0L) {
         return 0;
      } else {
         return difference > 0L ? 1 : -1;
      }
   }

   public ISFSObject toSFSObject() {
      ISFSObject time = new SFSObject();
      time.putLong("startTime", this.startTime);
      time.putLong("duration", this.duration);
      return time;
   }

   public static TreeSet<StartTimeDuration> joinDurations(List<StartTimeDuration> unjoinedStartTimeDurations) {
      Collections.sort(unjoinedStartTimeDurations);
      TreeSet<StartTimeDuration> joinedDurations = new TreeSet();
      if (unjoinedStartTimeDurations.size() > 0) {
         StartTimeDuration currentDuration = (StartTimeDuration)unjoinedStartTimeDurations.get(0);

         for(int i = 1; i < unjoinedStartTimeDurations.size(); ++i) {
            StartTimeDuration nextDuration = (StartTimeDuration)unjoinedStartTimeDurations.get(i);
            long currentEndTime = currentDuration.startTime + currentDuration.duration;
            long nextEndTime = nextDuration.startTime + nextDuration.duration;
            if (nextDuration.startTime <= currentEndTime && nextEndTime > currentEndTime) {
               currentDuration = new StartTimeDuration(currentDuration.startTime, nextEndTime - currentDuration.startTime);
            } else if (nextDuration.startTime > currentEndTime) {
               joinedDurations.add(currentDuration);
               currentDuration = nextDuration;
            }
         }

         joinedDurations.add(currentDuration);
      }

      return joinedDurations;
   }
}
