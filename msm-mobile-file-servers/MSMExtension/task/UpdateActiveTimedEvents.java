package com.bigbluebubble.mysingingmonsters.task;

import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventManager;
import com.smartfoxserver.v2.SmartFoxServer;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class UpdateActiveTimedEvents implements Runnable {
   private static final int elapsedTimeBeforeUpdateActiveEventsSec = 60;

   public static ScheduledFuture<?> schedule() {
      return SmartFoxServer.getInstance().getTaskScheduler().scheduleAtFixedRate(new UpdateActiveTimedEvents(), 1, 60, TimeUnit.SECONDS);
   }

   public void run() {
      TimedEventManager.instance().updateActiveTimedEvents();
   }
}
