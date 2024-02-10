package com.bigbluebubble.mysingingmonsters.data.timed_events.events;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class EggstravaganzaEvent extends TimedEvent {
   private static final String SCRATCHOFF_PRICE_KEY = "scratchoffPrice";
   private static final String SCRATCHOFF_TIME_KEY = "timeBetweenFreePlays";

   public int getKey() {
      return 0;
   }

   public EggstravaganzaEvent(ISFSObject timedEventData) throws Exception {
      super(timedEventData);
      SFSObject data = new SFSObject();
      data.putInt("scratchoffPrice", this.scratchOffPrice());
      data.putInt("timeBetweenFreePlays", this.scratchOffTimeBetweenFreePlays());
      this.eventDataSfsArray.addSFSObject(data);
   }

   public int scratchOffTimeBetweenFreePlays() {
      return GameSettings.getInt("EGGSTRAVAGANZA_SCRATCHOFF_TIME_BETWEEN_FREE_PLAYS");
   }

   public int scratchOffPrice() {
      return GameSettings.getInt("EGGSTRAVAGANZA_SCRATCHOFF_PRICE");
   }

   public float rareChance() {
      return GameSettings.getFloat("EGGSTRAVAGANZA_SCRATCHOFF_RARE_CHANCE");
   }

   public float epicChance() {
      return GameSettings.getFloat("EGGSTRAVAGANZA_SCRATCHOFF_EPIC_CHANCE");
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof EggstravaganzaEvent)) {
         return false;
      } else {
         EggstravaganzaEvent e = (EggstravaganzaEvent)o;
         return this.getStartDate() == e.getStartDate() && this.getEndDate() == e.getEndDate();
      }
   }

   public String toString() {
      return super.toString() + ": sale price (diamonds): " + this.scratchOffPrice() + ",  hours until next free scratchoff: " + this.scratchOffTimeBetweenFreePlays() + ", scratch rare chance: " + this.rareChance() + ", scratch epic chance: " + this.epicChance();
   }
}
