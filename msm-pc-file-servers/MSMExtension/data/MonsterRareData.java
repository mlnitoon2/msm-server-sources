package com.bigbluebubble.mysingingmonsters.data;

public class MonsterRareData extends MonsterMapData {
   public float probability;

   public MonsterRareData(int commonId, int rareId, float prob) {
      super(commonId, rareId);
      this.probability = prob;
   }

   public int commonMonsterId() {
      return this.type1Id();
   }

   public int rareMonsterId() {
      return this.type2Id();
   }
}
