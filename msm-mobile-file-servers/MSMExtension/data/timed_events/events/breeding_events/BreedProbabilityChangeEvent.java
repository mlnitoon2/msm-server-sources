package com.bigbluebubble.mysingingmonsters.data.timed_events.events.breeding_events;

import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class BreedProbabilityChangeEvent extends TimedEvent {
   private final String BREEDING_EVENT_TYPE_KEY = "type";
   private final String ICON_KEY = "icon";
   private final String POPUP_DESCRIPTION = "desc";
   protected BreedEventFactory.BreedingEventEnum enumType;
   protected BreedEventType breedEventType;

   public int getKey() {
      return 0;
   }

   public BreedProbabilityChangeEvent(ISFSObject timedEventData) throws Exception {
      super(timedEventData);
      if (this.eventDataSfsArray.size() != 1) {
         throw new Exception("BreedProbabilityChangeEvent has invalid number of entries in data field; can only have 1");
      } else {
         this.enumType = BreedEventFactory.getBreedingEventEnumFromString(((SFSObject)this.eventDataSfsArray.getElementAt(0)).getUtfString("type"));
         this.breedEventType = BreedEventFactory.CreateEvent(this.enumType);
         this.eventDataSfsArray.removeElementAt(0);
         SFSObject data = new SFSObject();
         data.putUtfString("icon", this.breedEventType.alternateIcon());
         data.putUtfString("desc", this.breedEventType.popupDesc());
         this.eventDataSfsArray.addSFSObject(data);
      }
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof BreedProbabilityChangeEvent)) {
         return false;
      } else {
         BreedProbabilityChangeEvent e = (BreedProbabilityChangeEvent)o;
         return this.getStartDate() == e.getStartDate() && this.getEndDate() == e.getEndDate();
      }
   }

   public String toString() {
      String str = super.toString();
      return str + " type: " + this.enumType.toString() + ":\n" + this.breedEventType.toString();
   }

   public boolean affectsBreedingCombo(int monster1, int monster2, int result) {
      return this.breedEventType.affectsBreedingCombo(monster1, monster2, result);
   }

   public int newProbability(int monster1, int monster2, int result) {
      return this.breedEventType.newProbability(monster1, monster2, result);
   }
}
