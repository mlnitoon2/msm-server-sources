package com.bigbluebubble.mysingingmonsters.staticdata;

public interface IStaticDataLookup<T extends IStaticData> {
   long ALWAYS_UPDATE = Long.MAX_VALUE;

   String getCacheName();

   Iterable<T> entries();

   T getEntry(int var1);

   long lastChanged();
}
