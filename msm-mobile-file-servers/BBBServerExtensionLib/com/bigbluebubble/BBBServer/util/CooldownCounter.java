package com.bigbluebubble.BBBServer.util;

public class CooldownCounter {
   private int currentCount = 0;
   private long lastIncrementTime = 0L;
   private long cooldownTime;

   public CooldownCounter(long cooldownTime) {
      this.cooldownTime = cooldownTime;
   }

   public void increment() {
      ++this.currentCount;
      this.lastIncrementTime = System.currentTimeMillis();
   }

   public void decrement() {
      --this.currentCount;
   }

   public void reset() {
      this.currentCount = 0;
      this.lastIncrementTime = 0L;
   }

   public boolean isOnCooldown() {
      return System.currentTimeMillis() - this.lastIncrementTime <= this.cooldownTime;
   }

   public int count() {
      return this.currentCount;
   }
}
