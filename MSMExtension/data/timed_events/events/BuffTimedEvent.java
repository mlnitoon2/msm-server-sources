package com.bigbluebubble.mysingingmonsters.data.timed_events.events;

import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataType;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Collection;

public class BuffTimedEvent extends TimedEvent {
   private static final String AMOUNT_KEY = "amount";
   private int buffAmount = 0;

   public BuffTimedEvent(TimedEventType buffEventType, int buffAmount, long startDate, long endDate, Collection<Integer> islands) throws Exception {
      super(0L, buffEventType, startDate, endDate);
      this.islandArr = new ArrayList(islands);
      switch(buffEventType) {
      case NurseryTimeReduction:
      case BreedingTimeReduction:
      case BreedingChanceIncrease:
      case CostumeChanceIncrease:
      case CurrencyCollectionBonus:
      case BakingTimeReduction:
         this.buffAmount = buffAmount;
         this.eventDataSfsArray = new SFSArray();
         ISFSObject buffData = new SFSObject();
         buffData.putInt("amount", buffAmount);
         if (this.islandArr != null) {
            buffData.putUtfString("islands", this.islandArr.toString());
         }

         this.eventDataSfsArray.addSFSObject(buffData);
         this.allRowData.putSFSArray("data", this.eventDataSfsArray);
         return;
      default:
         throw new Exception("Unsupported TimedEventType");
      }
   }

   public BuffTimedEvent(ISFSObject timedEventData) {
      super(timedEventData);
      ISFSObject sfsObj = this.eventDataSfsArray.getSFSObject(0);
      if (sfsObj.get("amount").getTypeId() == SFSDataType.INT) {
         this.buffAmount = sfsObj.getInt("amount");
      } else {
         this.buffAmount = (int)(sfsObj.getFloat("amount") * 100.0F);
      }

   }

   public int getKey() {
      return 0;
   }

   public float getBuffAmount() {
      return (float)this.buffAmount / 100.0F;
   }

   public static BuffTimedEvent CreateNurseryTimeReduction(int buffAmount, long startDate, long endDate, Collection<Integer> islands) throws Exception {
      return new BuffTimedEvent(TimedEventType.NurseryTimeReduction, buffAmount, startDate, endDate, islands);
   }

   public static BuffTimedEvent CreateBreedingTimeReduction(int buffAmount, long startDate, long endDate, Collection<Integer> islands) throws Exception {
      return new BuffTimedEvent(TimedEventType.BreedingTimeReduction, buffAmount, startDate, endDate, islands);
   }

   public static BuffTimedEvent CreateBreedingChanceIncrease(int buffAmount, long startDate, long endDate, Collection<Integer> islands) throws Exception {
      return new BuffTimedEvent(TimedEventType.BreedingChanceIncrease, buffAmount, startDate, endDate, islands);
   }

   public static BuffTimedEvent CreateCostumeChanceIncrease(int buffAmount, long startDate, long endDate, Collection<Integer> islands) throws Exception {
      return new BuffTimedEvent(TimedEventType.CostumeChanceIncrease, buffAmount, startDate, endDate, islands);
   }

   public static BuffTimedEvent CreateCurrencyCollectionBonus(int buffAmount, long startDate, long endDate, Collection<Integer> islands) throws Exception {
      return new BuffTimedEvent(TimedEventType.CurrencyCollectionBonus, buffAmount, startDate, endDate, islands);
   }

   public static BuffTimedEvent CreateBakingTimeReduction(int buffAmount, long startDate, long endDate, Collection<Integer> islands) throws Exception {
      return new BuffTimedEvent(TimedEventType.BakingTimeReduction, buffAmount, startDate, endDate, islands);
   }
}
