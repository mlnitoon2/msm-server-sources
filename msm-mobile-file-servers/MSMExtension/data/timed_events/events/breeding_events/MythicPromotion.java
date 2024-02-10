package com.bigbluebubble.mysingingmonsters.data.timed_events.events.breeding_events;

public class MythicPromotion extends BreedEventType {
   String sqlDef = "UPDATE `breeding_combinations` SET `probability` = 10, last_changed = NOW() WHERE `monster_1` IN (17, 18, 21, 24, 281) AND `monster_2` IN (19, 23, 24, 31, 284) AND `result` IN (402, 403, 597, 616, 628, 641);\r\n";

   protected String getSqlDef() {
      return this.sqlDef;
   }

   public MythicPromotion(String icon, String desc) {
      super(icon, desc);
   }
}
