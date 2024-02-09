package com.bigbluebubble.mysingingmonsters.data;

import com.smartfoxserver.v2.entities.data.SFSObject;

public class AttunerOffsetData {
   private int offset;
   private int cost;
   private int duration;

   public AttunerOffsetData(SFSObject sfsDef) {
      this.offset = sfsDef.getInt("offset");
      this.cost = sfsDef.getInt("cost");
      this.duration = sfsDef.getInt("duration");
   }

   public int offset() {
      return this.offset;
   }

   public int cost() {
      return this.cost;
   }

   public int durationInHours() {
      return this.duration;
   }
}
