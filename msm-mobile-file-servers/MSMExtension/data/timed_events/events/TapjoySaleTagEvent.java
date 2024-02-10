package com.bigbluebubble.mysingingmonsters.data.timed_events.events;

import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class TapjoySaleTagEvent extends TimedEvent {
   private static final String CURRENCY_KEY = "currency";
   private Player.CurrencyType c;

   public int getKey() {
      return 0;
   }

   public TapjoySaleTagEvent(ISFSObject timedEventData) throws Exception {
      super(timedEventData);
      if (this.eventDataSfsArray.size() != 1) {
         throw new Exception("TapjoySaleTagEvent has invalid number of entries in data field; can only have 1");
      } else {
         this.c = Player.getCurrencyTypeFromString(((SFSObject)this.eventDataSfsArray.getElementAt(0)).getUtfString("currency"));
      }
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof TapjoySaleTagEvent)) {
         return false;
      } else {
         TapjoySaleTagEvent e = (TapjoySaleTagEvent)o;
         return this.c == e.c && this.getStartDate() == e.getStartDate() && this.getEndDate() == e.getEndDate();
      }
   }

   public String toString() {
      String str = super.toString();
      return str + " currency: " + this.c.getCurrencyKey();
   }
}
