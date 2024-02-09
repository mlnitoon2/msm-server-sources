package com.bigbluebubble.mysingingmonsters.data.timed_events.events.breeding_events;

public class BreedEventFactory {
   public static final String[] breedingIconStrings = new String[]{"button_breedpromoeth", "button_breedpromoeth", "button_breedpromoeth", "button_breedpromomyth", "Undefined"};
   public static final String[] descStrings = new String[]{"BREEDING_EVENT_DESC_ETHBONANZA", "BREEDING_EVENT_DESC_ETHBONANZA", "BREEDING_EVENT_DESC_ETHBONANZA_SUPER", "BREEDING_EVENT_DESC_MYTHBONANZA", "Undefined"};

   public static BreedEventFactory.BreedingEventEnum getBreedingEventEnumFromString(String str) {
      return BreedEventFactory.BreedingEventEnum.valueOf(str);
   }

   public static BreedEventType CreateEvent(BreedEventFactory.BreedingEventEnum b) {
      int ordinal = b.ordinal();
      BreedEventType breedEventType = null;
      switch(b) {
      case EtherealPromotion_5:
         breedEventType = new EtherealPromotion(breedingIconStrings[ordinal], descStrings[ordinal]);
         break;
      case EtherealBonanza_8:
         breedEventType = new EtherealBonanza(breedingIconStrings[ordinal], descStrings[ordinal]);
         break;
      case EtherealSuperBonanza_10:
         breedEventType = new EtherealSuperBonanza(breedingIconStrings[ordinal], descStrings[ordinal]);
         break;
      case MythicPromotion_10:
         breedEventType = new MythicPromotion(breedingIconStrings[ordinal], descStrings[ordinal]);
         break;
      default:
         breedEventType = null;
      }

      if (breedEventType != null) {
         ((BreedEventType)breedEventType).GenerateBreedingComboMods();
      }

      return (BreedEventType)breedEventType;
   }

   public static enum BreedingEventEnum {
      EtherealPromotion_5,
      EtherealBonanza_8,
      EtherealSuperBonanza_10,
      MythicPromotion_10,
      Undefined;
   }
}
