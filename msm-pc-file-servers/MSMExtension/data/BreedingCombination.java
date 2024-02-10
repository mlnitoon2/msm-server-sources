package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventManager;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.breeding_events.BreedProbabilityChangeEvent;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import java.util.Iterator;
import java.util.List;

public class BreedingCombination {
   protected ISFSObject data;
   protected int breedingId;
   protected Monster monster1;
   protected Monster monster2;
   protected Monster monsterResult;
   protected int probability;
   protected float modifier;

   public BreedingCombination(ISFSObject breedingData) {
      this.data = breedingData;
      this.monster1 = MonsterLookup.get(breedingData.getInt("monster_1"));
      this.monster2 = MonsterLookup.get(breedingData.getInt("monster_2"));
      this.monsterResult = MonsterLookup.get(breedingData.getInt("result"));
      this.probability = breedingData.getInt("probability");
      this.modifier = breedingData.getDouble("modifier").floatValue();
   }

   public Monster getMonster1() {
      return this.monster1;
   }

   public Monster getMonster2() {
      return this.monster2;
   }

   public Monster getResult() {
      return this.monsterResult;
   }

   public int getProbability(int islandType) {
      if (TimedEventManager.instance().hasTimedEventNow(TimedEventType.BreedingPromo, 0, islandType)) {
         List<TimedEvent> b = TimedEventManager.instance().currentActiveOnKey(TimedEventType.BreedingPromo, 0, islandType);
         Iterator itr = b.iterator();

         while(itr.hasNext()) {
            TimedEvent te = (TimedEvent)itr.next();
            BreedProbabilityChangeEvent be = (BreedProbabilityChangeEvent)te;
            if (be.affectsBreedingCombo(this.monster1.getMonsterID(), this.monster2.getMonsterID(), this.monsterResult.getMonsterID())) {
               return be.newProbability(this.monster1.getMonsterID(), this.monster2.getMonsterID(), this.monsterResult.getMonsterID());
            }
         }
      }

      return this.probability;
   }

   public float getModifier() {
      return this.modifier;
   }

   public ISFSObject getData() {
      return this.data;
   }
}
