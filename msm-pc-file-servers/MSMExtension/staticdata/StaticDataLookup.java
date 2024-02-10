package com.bigbluebubble.mysingingmonsters.staticdata;

public abstract class StaticDataLookup<T extends IStaticData> implements IStaticDataLookup<T> {
   protected long lastChanged_ = Long.MAX_VALUE;

   public long lastChanged() {
      return this.lastChanged_;
   }
}
