package com.bigbluebubble.mysingingmonsters.data.timed_events.events;

import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.data.IslandTheme;
import com.bigbluebubble.mysingingmonsters.data.IslandThemeLookup;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventManager;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class IslandThemeAvailabilityEvent extends TimedEvent {
   private static final String THEME_ID_KEY = "theme";
   private Integer themeId;

   public int getKey() {
      return this.getThemeId();
   }

   public IslandThemeAvailabilityEvent(ISFSObject timedEventData) throws Exception {
      super(timedEventData);
      if (this.eventDataSfsArray.size() != 1) {
         throw new Exception("IslandThemeAvailabilityEvent has invalid number of entries in data field; can only have 1");
      } else {
         this.themeId = ((SFSObject)this.eventDataSfsArray.getElementAt(0)).getInt("theme");
         if (this.themeId == null) {
            Logger.trace("IslandThemeAvailabilityEvent: themeId is null");
         }

      }
   }

   public Integer getThemeId() {
      return this.themeId;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof IslandThemeAvailabilityEvent)) {
         return false;
      } else {
         IslandThemeAvailabilityEvent e = (IslandThemeAvailabilityEvent)o;
         return this.themeId == e.themeId && this.getStartDate() == e.getStartDate() && this.getEndDate() == e.getEndDate();
      }
   }

   public String toString() {
      String str = super.toString();
      IslandTheme t = IslandThemeLookup.get(this.themeId);
      return t == null ? str + " invalid theme" : str + " theme: " + this.themeId + ": " + t.getName();
   }

   public static boolean hasTimedEventNow(IslandTheme theme, Player player, int islandType) {
      return TimedEventManager.instance().hasTimedEventNow(TimedEventType.IslandThemeAvailability, theme.getId(), islandType) || player.getTimedEvents().timedEventNow(TimedEventType.IslandThemeAvailability, theme.getId(), islandType);
   }
}
