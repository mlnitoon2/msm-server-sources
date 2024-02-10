package com.bigbluebubble.mysingingmonsters.util.pcg;

import java.util.concurrent.atomic.AtomicLong;

public class Pcg32 {
   private long state;
   private long inc;
   private static final long MULTIPLIER = 6364136223846793005L;
   private double nextNextGaussian;
   private boolean haveNextNextGaussian = false;
   private static final AtomicLong streamUniquifier = new AtomicLong((long)System.identityHashCode(Pcg32.class));

   public Pcg32(long initState, long initSeq) {
      this.seed(initState, initSeq);
   }

   public Pcg32() {
      this.seed();
   }

   public int nextInt() {
      long oldState = this.state;
      this.state = oldState * 6364136223846793005L + this.inc;
      int xorShifted = (int)((oldState >>> 18 ^ oldState) >>> 27);
      int rot = (int)(oldState >>> 59);
      return Integer.rotateRight(xorShifted, rot);
   }

   public int nextInt(int n) {
      if (n <= 0) {
         throw new IllegalArgumentException("n must be positive");
      } else {
         int bits;
         int val;
         do {
            bits = this.nextInt() >>> 1;
            val = bits % n;
         } while(bits - val + (n - 1) < 0);

         return val;
      }
   }

   public long nextLong() {
      return ((long)this.nextInt() << 32) + (long)this.nextInt();
   }

   public long nextLong(long n) {
      if (n <= 0L) {
         throw new IllegalArgumentException("n must be positive");
      } else {
         long bits;
         long val;
         do {
            bits = this.nextLong() >>> 1;
            val = bits % n;
         } while(bits - val + (n - 1L) < 0L);

         return val;
      }
   }

   public boolean nextBoolean() {
      return (this.nextInt() & 1) != 0;
   }

   public float nextFloat() {
      return (float)this.nextBits(24) / 1.6777216E7F;
   }

   public float nextFloat(float bound) {
      return bound * this.nextFloat();
   }

   public double nextDouble() {
      return (double)(((long)this.nextBits(26) << 27) + (long)this.nextBits(27)) / 9.007199254740992E15D;
   }

   public double nextDouble(double bound) {
      return bound * this.nextDouble();
   }

   public int nextBits(int bits) {
      return this.nextInt() >>> 32 - bits;
   }

   public double nextGaussian() {
      if (this.haveNextNextGaussian) {
         this.haveNextNextGaussian = false;
         return this.nextNextGaussian;
      } else {
         double v1;
         double v2;
         double s;
         do {
            do {
               v1 = 2.0D * this.nextDouble() - 1.0D;
               v2 = 2.0D * this.nextDouble() - 1.0D;
               s = v1 * v1 + v2 * v2;
            } while(s >= 1.0D);
         } while(s == 0.0D);

         double multiplier = StrictMath.sqrt(-2.0D * StrictMath.log(s) / s);
         this.nextNextGaussian = v2 * multiplier;
         this.haveNextNextGaussian = true;
         return v1 * multiplier;
      }
   }

   public double nextGaussian(double mean, double standardDeviation) {
      return this.nextGaussian() * standardDeviation + mean;
   }

   public void seed(long initState, long initSeq) {
      this.state = 0L;
      this.inc = 2L * initSeq + 1L;
      this.nextInt();
      this.state += initState;
      this.nextInt();
   }

   public void seed() {
      this.seed(System.nanoTime(), streamUniquifier());
   }

   private static long streamUniquifier() {
      long current;
      long next;
      do {
         current = streamUniquifier.get();
         next = current * 181783497276652981L;
      } while(!streamUniquifier.compareAndSet(current, next));

      return next;
   }
}
