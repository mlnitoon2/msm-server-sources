package com.bigbluebubble.BBBServer.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.spy.memcached.MemcachedClient;

public class CacheWrapper {
   MemcachedClient client;
   CustomSerializingTranscoder transcoder;
   public static final int TIMEOUT_TWENTY_MINUTES = 1200;
   public static final int TIMEOUT_THIRTY_MINUTES = 1800;
   public static final int TIMEOUT_SIXTY_MINUTES = 3600;
   public static final int TIMEOUT_FOUR_HOURS = 14400;
   private String prefix;

   public CacheWrapper(String endPoint, int port, String prefix) {
      try {
         this.prefix = prefix;
         System.setProperty("net.spy.log.LoggerImpl", "net.spy.memcached.compat.log.SunLogger");
         Logger.getLogger("net.spy.memcached").setLevel(Level.WARNING);
         this.client = new MemcachedClient(new InetSocketAddress[]{new InetSocketAddress(endPoint, port)});
         this.transcoder = new CustomSerializingTranscoder();
      } catch (IOException var5) {
         SimpleLogger.trace((Exception)var5);
      }

   }

   public void kill() {
      this.client.shutdown();
   }

   public void set(String key, int expInSecondsOrUnixTime, Object val) {
      this.client.set(this.prefix + key, expInSecondsOrUnixTime, val, this.transcoder);
   }

   public Object get(String key) {
      return this.client.get(this.prefix + key, this.transcoder);
   }

   public Boolean delete(String key) throws InterruptedException, ExecutionException {
      Future<Boolean> result = this.client.delete(this.prefix + key);
      return (Boolean)result.get();
   }

   public long increment(String key, int by) {
      return this.increment(this.prefix + key, (long)by);
   }

   public long increment(String key, long by) {
      return this.client.incr(this.prefix + key, by);
   }

   public long decrement(String key, int by) {
      return this.decrement(this.prefix + key, (long)by);
   }

   public long decrement(String key, long by) {
      return this.client.decr(this.prefix + key, by);
   }
}
