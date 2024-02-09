package com.bigbluebubble.mysingingmonsters.data;

public class MonsterEpicData extends MonsterMapData {
   public MonsterEpicData(int commonId, int epicId) {
      super(commonId, epicId);
   }

   public int commonMonsterId() {
      return this.type1Id();
   }

   public int epicMonsterId() {
      return this.type2Id();
   }
}
