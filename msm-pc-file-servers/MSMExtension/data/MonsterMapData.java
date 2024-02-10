package com.bigbluebubble.mysingingmonsters.data;

public class MonsterMapData {
   private int type1MonsterId;
   private int type2MonsterId;

   public MonsterMapData(int type1, int type2) {
      this.type1MonsterId = type1;
      this.type2MonsterId = type2;
   }

   public int type1Id() {
      return this.type1MonsterId;
   }

   public int type2Id() {
      return this.type2MonsterId;
   }
}
