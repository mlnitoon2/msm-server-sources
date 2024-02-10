package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticDataLookup;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DailyCumulativeLoginLookup extends StaticDataLookup<DailyCumulativeLoginCalendar> {
   private static DailyCumulativeLoginLookup instance;
   private Map<Integer, DailyCumulativeLoginCalendar> rewardCalendars_ = new ConcurrentHashMap();
   static final String CACHE_NAME = "daily_cumulative_login_data";

   public static DailyCumulativeLoginLookup getInstance() {
      return instance;
   }

   public static void init(IDbWrapper db) throws Exception {
      instance = new DailyCumulativeLoginLookup(db);
   }

   public static DailyCumulativeLoginCalendar get(int calendarId) {
      return instance.getEntry(calendarId);
   }

   private DailyCumulativeLoginLookup(IDbWrapper db) throws Exception {
      this.lastChanged_ = 0L;
      String SELECT_CALENDAR_SQL = "SELECT * FROM daily_cumulative_login_calendar";
      ISFSArray results = db.query("SELECT * FROM daily_cumulative_login_calendar");

      DailyCumulativeLoginCalendar rewardCalendar;
      for(Iterator i = results.iterator(); i.hasNext(); this.lastChanged_ = Math.max(this.lastChanged_, rewardCalendar.lastChanged())) {
         SFSObject calendarData = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         rewardCalendar = new DailyCumulativeLoginCalendar(calendarData, db);
         this.rewardCalendars_.put(rewardCalendar.getId(), rewardCalendar);
      }

   }

   public String getCacheName() {
      return "daily_cumulative_login_data";
   }

   public Iterable<DailyCumulativeLoginCalendar> entries() {
      return this.rewardCalendars_.values();
   }

   public DailyCumulativeLoginCalendar getEntry(int id) {
      if (!this.rewardCalendars_.containsKey(id)) {
         Logger.trace("Rewards Calendar not found:" + id);
      }

      return (DailyCumulativeLoginCalendar)this.rewardCalendars_.get(id);
   }
}
