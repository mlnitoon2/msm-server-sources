package com.bigbluebubble.mysingingmonsters.data.timed_events;

import java.util.HashMap;
import java.util.Map;

public enum TimedEventType {
   None(0),
   Promo(1),
   CurrencyReplacement(2),
   EntityStoreAvailability(3),
   BreedingPromo(4),
   EntitySale(5),
   CurrencySale(6),
   StarStoreAvailability(7),
   StarSale(8),
   CurrencyAvailability(9),
   PermalightTorchSale(10),
   MegafySale(11),
   ShortenedFuzing(12),
   IslandSale(13),
   TapjoySaleTag(14),
   CostumeAvailability(15),
   CostumeSale(16),
   EvolveAvailability(17),
   CollectAllTrial(18),
   Eggstravaganza(19),
   ReturningUserBonus(20),
   CrucibleHeatDiscount(21),
   CrucibleFlagDay(22),
   NurseryTimeReduction(23),
   BreedingTimeReduction(24),
   BreedingChanceIncrease(25),
   CostumeChanceIncrease(26),
   CurrencyCollectionBonus(27),
   IslandThemeAvailability(28),
   BakingTimeReduction(29);

   private static Map<Integer, TimedEventType> idToEnumValuesMapping = new HashMap();
   private int id;

   public static TimedEventType fromInt(int id) {
      return (TimedEventType)idToEnumValuesMapping.get(id);
   }

   public int getId() {
      return this.id;
   }

   private TimedEventType(int id) {
      this.id = id;
   }

   static {
      TimedEventType[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         TimedEventType t = var0[var2];
         idToEnumValuesMapping.put(t.getId(), t);
      }

   }
}
