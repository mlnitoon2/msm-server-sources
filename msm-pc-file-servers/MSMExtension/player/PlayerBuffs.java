package com.bigbluebubble.mysingingmonsters.player;

import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventManager;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.BuffTimedEvent;
import java.util.List;

public class PlayerBuffs {
   private Player player;

   public PlayerBuffs(Player p) {
      this.player = p;
   }

   public float getMultiplier(PlayerBuffs.Buffs buffType, int islandType) {
      switch(buffType) {
      case NurseryTimeReduction:
         return getNurseryTimeReduction(this.player, islandType);
      case BreedingTimeReduction:
         return getBreedingTimeReduction(this.player, islandType);
      case BreedingChanceIncrease:
         return getBreedingChanceIncrease(this.player, islandType);
      case CostumeChanceIncrease:
         return getCostumeChanceIncrease(this.player, islandType);
      case CurrencyCollectionBonus:
         return getCurrencyCollectionBonus(this.player, islandType);
      case BakingTimeReduction:
         return getBakeryTimeReduction(this.player, islandType);
      default:
         return 1.0F;
      }
   }

   private static float getGenericBuff(Player player, int islandType, TimedEventType timedEventType) {
      float buffValue = 1.0F;
      PlayerTimedEvents pte = player.getTimedEvents();
      List<TimedEvent> playerEvents = null;
      if (pte != null) {
         playerEvents = pte.currentActiveOnKey(timedEventType, 0, islandType);
      }

      if (playerEvents != null && playerEvents.size() > 0) {
         BuffTimedEvent buffEvent = (BuffTimedEvent)((BuffTimedEvent)playerEvents.get(0));
         buffValue = buffEvent.getBuffAmount();
      } else {
         List<TimedEvent> events = TimedEventManager.instance().currentActiveOnKey(timedEventType, 0, islandType);
         if (events != null && events.size() > 0) {
            BuffTimedEvent buffEvent = (BuffTimedEvent)((BuffTimedEvent)events.get(0));
            buffValue = buffEvent.getBuffAmount();
         }
      }

      return buffValue;
   }

   private static float getNurseryTimeReduction(Player player, int islandType) {
      return getGenericBuff(player, islandType, TimedEventType.NurseryTimeReduction);
   }

   private static float getBreedingTimeReduction(Player player, int islandType) {
      return getGenericBuff(player, islandType, TimedEventType.BreedingTimeReduction);
   }

   private static float getBreedingChanceIncrease(Player player, int islandType) {
      return getGenericBuff(player, islandType, TimedEventType.BreedingChanceIncrease);
   }

   private static float getCostumeChanceIncrease(Player player, int islandType) {
      return getGenericBuff(player, islandType, TimedEventType.CostumeChanceIncrease);
   }

   private static float getCurrencyCollectionBonus(Player player, int islandType) {
      return getGenericBuff(player, islandType, TimedEventType.CurrencyCollectionBonus);
   }

   private static float getBakeryTimeReduction(Player player, int islandType) {
      return getGenericBuff(player, islandType, TimedEventType.BakingTimeReduction);
   }

   public static enum Buffs {
      NurseryTimeReduction,
      BreedingTimeReduction,
      BreedingChanceIncrease,
      CostumeChanceIncrease,
      CurrencyCollectionBonus,
      BakingTimeReduction;
   }
}
