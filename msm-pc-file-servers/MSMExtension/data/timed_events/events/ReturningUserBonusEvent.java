package com.bigbluebubble.mysingingmonsters.data.timed_events.events;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;

public class ReturningUserBonusEvent extends TimedEvent {
   public static final String NURSERY_SPEED_MODIFIER_KEY = "nursery_speed_mod";
   public static final String COIN_PRODUCTION_MODIFIER_KEY = "coin_production_mod";
   public static final String WISHING_TORCH_MODIFIER_KEY = "wishing_torch_mod";
   private float nurserySpeedMod = 1.0F;
   private float coinProductionMod = 1.0F;
   private float torchModifierMod = 1.0F;

   public int getKey() {
      return 0;
   }

   public ReturningUserBonusEvent(ISFSObject timedEventData) throws Exception {
      super(timedEventData);
      if (this.eventDataSfsArray.size() == 0) {
         String modifiersStr = GameSettings.get("RETURNING_USER_MODIFIERS");
         this.allRowData.putUtfString("data", "[" + modifiersStr + "]");
         this.eventDataSfsArray = SFSArray.newFromJsonData(this.allRowData.getUtfString("data"));
         this.allRowData.putSFSArray("data", this.eventDataSfsArray);
      }

      for(int i = 0; i < this.eventDataSfsArray.size(); ++i) {
         ISFSObject sfsObj = this.eventDataSfsArray.getSFSObject(i);
         if (sfsObj.containsKey("nursery_speed_mod")) {
            this.nurserySpeedMod = sfsObj.getFloat("nursery_speed_mod");
         }

         if (sfsObj.containsKey("coin_production_mod")) {
            this.coinProductionMod = sfsObj.getFloat("coin_production_mod");
         }

         if (sfsObj.containsKey("wishing_torch_mod")) {
            this.torchModifierMod = sfsObj.getFloat("wishing_torch_mod");
         }
      }

   }

   public float nurserySpeedMod() {
      return this.nurserySpeedMod;
   }

   public float coinProductionMod() {
      return this.coinProductionMod;
   }

   public float torchModifierMod() {
      return this.torchModifierMod;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ReturningUserBonusEvent)) {
         return false;
      } else {
         ReturningUserBonusEvent e = (ReturningUserBonusEvent)o;
         return this.getStartDate() == e.getStartDate() && this.getEndDate() == e.getEndDate();
      }
   }

   public String toString() {
      String str = super.toString();
      return str + " nurserySpeedMod: " + this.nurserySpeedMod + " coinProductionMod: " + this.coinProductionMod + " torchModifierMod: " + this.torchModifierMod;
   }
}
