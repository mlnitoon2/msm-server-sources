package com.bigbluebubble.mysingingmonsters.data.timed_events;

import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.data.VersionData;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataType;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class TimedEvent {
   public static final String ID_KEY = "id";
   public static final String EVENT_TYPE_KEY = "event_type";
   public static final String EVENT_ID_KEY = "event_id";
   public static final String START_DATE_KEY = "start_date";
   public static final String END_DATE_KEY = "end_date";
   public static final String DATA_KEY = "data";
   public static final String USER_ID_KEY = "user_id";
   public static final String COUNT_KEY = "count";
   public static final String MAX_KEY = "max";
   public static final String DATA_MIN_VER_KEY = "min_client_ver";
   private static final String ISLANDS_KEY = "islands";
   public static final VersionInfo defaultMinVersion = new VersionInfo("1.0.0");
   protected ISFSObject allRowData;
   protected long id = 0L;
   protected VersionInfo minServerVersion;
   protected ArrayList<Integer> islandArr = null;
   public boolean isDirty;
   protected ISFSArray eventDataSfsArray;

   public ISFSObject getSfsObject() {
      return this.allRowData;
   }

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public long getStartDate() {
      return this.allRowData.getLong("start_date");
   }

   public long getEndDate() {
      return this.allRowData.getLong("end_date");
   }

   public TimedEventType getEventType() {
      return TimedEventType.fromInt(this.allRowData.getInt("event_id"));
   }

   public VersionInfo getMinServerVersion() {
      return this.minServerVersion;
   }

   public long getUserId() {
      return this.allRowData.getLong("user_id");
   }

   public void setUserId(long userId) {
      this.allRowData.putLong("user_id", userId);
      this.isDirty = true;
   }

   public int getCount() {
      return this.allRowData.containsKey("count") ? this.allRowData.getInt("count") : 0;
   }

   public void setCount(int count) {
      this.allRowData.putInt("count", count);
      this.isDirty = true;
   }

   public int getMax() {
      return this.allRowData.containsKey("max") ? this.allRowData.getInt("max") : 0;
   }

   public void setMax(int max) {
      this.allRowData.putInt("max", max);
      this.isDirty = true;
   }

   public ISFSArray getEventData() {
      return this.eventDataSfsArray;
   }

   public TimedEvent(long id, TimedEventType type, long startDate, long endDate) {
      this.allRowData = new SFSObject();
      this.allRowData.putLong("id", id);
      this.id = id;
      this.allRowData.putInt("event_id", type.getId());
      this.allRowData.putLong("start_date", startDate);
      this.allRowData.putLong("end_date", endDate);
      this.eventDataSfsArray = new SFSArray();
      this.allRowData.putSFSArray("data", this.eventDataSfsArray);
      this.minServerVersion = defaultMinVersion;
   }

   public TimedEvent(ISFSObject timedEventData) {
      this.allRowData = timedEventData;
      this.id = SFSHelpers.getLong("id", this.allRowData);
      this.eventDataSfsArray = SFSArray.newFromJsonData(this.allRowData.getUtfString("data"));
      this.allRowData.putSFSArray("data", this.eventDataSfsArray);
      if (this.eventDataSfsArray.size() > 0) {
         ISFSObject eventData = (SFSObject)this.eventDataSfsArray.getElementAt(0);
         if (eventData.containsKey("min_client_ver")) {
            VersionInfo minClientVer = new VersionInfo(eventData.getUtfString("min_client_ver"));
            boolean isLowerRange = VersionData.Instance().verifyIsLowerBoundary(minClientVer);
            if (!isLowerRange) {
               Logger.trace("================================================================================");
               Logger.trace("====ERROR! Min client version entered for timed event does not line up with a server version in versioning table, this suggests something is wrong here\n");
               Logger.trace("================================================================================");
            }

            this.minServerVersion = VersionData.Instance().getMaxServerVersionFromClientVersion(minClientVer);
         } else {
            this.minServerVersion = defaultMinVersion;
         }

         if (eventData.containsKey("islands")) {
            SFSDataType dataType = eventData.get("islands").getTypeId();
            if (dataType == SFSDataType.UTF_STRING) {
               String islands = eventData.getUtfString("islands");
               if (islands.charAt(0) == '[' && islands.charAt(islands.length() - 1) == ']') {
                  islands = islands.substring(1, islands.length() - 1);
               } else {
                  Logger.trace("============================================================");
                  Logger.trace("ERROR: TimedEvents data invalid in timed_event table, id " + this.id + " islands data needs square brackets");
                  Logger.trace("============================================================");
               }

               if (!islands.isEmpty()) {
                  if (this.isValidIntRangeInput(islands)) {
                     this.processRanges(islands);
                  } else {
                     Logger.trace("============================================================");
                     Logger.trace("ERROR: TimedEvents data invalid in timed_event table, id " + this.id + ": islands data is in improper format: " + islands);
                     Logger.trace("============================================================");
                  }
               }
            } else if (dataType == SFSDataType.INT_ARRAY) {
               Logger.trace("Error! Expecting a string for islands!");
            }
         } else {
            this.islandArr = null;
         }
      } else {
         this.minServerVersion = defaultMinVersion;
      }

      this.isDirty = false;
      if (this.getEventType() == TimedEventType.None) {
         String eventTypeStr = this.allRowData.getUtfString("event_type");
         TimedEventType eventType = TimedEventType.valueOf(eventTypeStr);
         this.allRowData.putInt("event_id", eventType.getId());
         this.isDirty = true;
      }

   }

   private boolean isValidIntRangeInput(String text) {
      Pattern re_valid = Pattern.compile("# Validate comma separated integers/integer ranges.\n^             # Anchor to start of string.         \n[0-9]+        # Integer of 1st value (required).   \n(?:           # Range for 1st value (optional).    \n  -           # Dash separates range integer.      \n  [0-9]+      # Range integer of 1st value.        \n)?            # Range for 1st value (optional).    \n(?:           # Zero or more additional values.    \n  ,           # Comma separates additional values. \n  [0-9]+      # Integer of extra value (required). \n  (?:         # Range for extra value (optional).  \n    -         # Dash separates range integer.      \n    [0-9]+    # Range integer of extra value.      \n  )?          # Range for extra value (optional).  \n)*            # Zero or more additional values.    \n$             # Anchor to end of string.           ", 4);
      Matcher m = re_valid.matcher(text);
      return m.matches();
   }

   public boolean isOverrideIsland() {
      return this.islandArr != null && this.islandArr.size() != 0;
   }

   public boolean appliesToIsland(int islandType) {
      if (this.islandArr != null && this.islandArr.size() != 0) {
         for(int i = 0; i < this.islandArr.size(); ++i) {
            if ((Integer)this.islandArr.get(i) == islandType) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private void processRanges(String text) {
      Pattern re_next_val = Pattern.compile("# extract next integers/integer range value.    \n([0-9]+)      # $1: 1st integer (Base).         \n(?:           # Range for value (optional).     \n  -           # Dash separates range integer.   \n  ([0-9]+)    # $2: 2nd integer (Range)         \n)?            # Range for value (optional). \n(?:,|$)       # End on comma or string end.", 4);
      Matcher m = re_next_val.matcher(text);
      this.islandArr = new ArrayList();

      while(true) {
         int nextInt;
         do {
            if (!m.find()) {
               return;
            }

            nextInt = Integer.parseInt(m.group(1));
            this.islandArr.add(nextInt);
         } while(m.group(2) == null);

         int lastInt = Integer.parseInt(m.group(2));

         for(int j = nextInt + 1; j <= lastInt; ++j) {
            this.islandArr.add(j);
         }
      }
   }

   public boolean currentlyActive() {
      long curTime = MSMExtension.CurrentDBTime();
      return curTime >= this.getStartDate() && curTime <= this.getEndDate();
   }

   public String toString() {
      return "id: " + this.getId() + " start: " + (new Date(this.getStartDate())).toString() + " end: " + (new Date(this.getEndDate())).toString() + " type: " + this.getEventType().toString();
   }

   public abstract int getKey();
}
