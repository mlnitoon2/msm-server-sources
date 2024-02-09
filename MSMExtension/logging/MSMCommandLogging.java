package com.bigbluebubble.mysingingmonsters.logging;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.ByteBuffer;
import com.bigbluebubble.BBBServer.util.MigBase64;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.smartfoxserver.v2.entities.User;
import java.util.Arrays;

public class MSMCommandLogging {
   private static boolean _logRequests = false;
   private static int _maxRequests = 0;
   private static String _requestQueue = "";
   private static boolean _debugLog = false;

   public static boolean logRequests() {
      return _logRequests;
   }

   public static int maxRequests() {
      return _maxRequests;
   }

   public static String requestQueue() {
      return _requestQueue;
   }

   public static void init() {
      try {
         _logRequests = GameSettings.getInt("COMMAND_LOGGING") != 0;
         _maxRequests = GameSettings.getInt("COMMAND_LOGGING_MAX");
         _requestQueue = GameSettings.get("COMMAND_LOGGING_QUEUE_URL");
      } catch (Exception var1) {
         Logger.trace(var1, "Error initializing command logging ... disabled");
         _logRequests = false;
      }

   }

   public static void createClientRequestLog(User user) {
      if (logRequests() && maxRequests() > 0) {
         user.setProperty("commands", new ByteBuffer(16 + maxRequests() * 4));
         initClientRequestLog(user);
      }

   }

   public static void addClientRequestLog(User user, String request) {
      try {
         if (logRequests()) {
            Integer c = (Integer)MSMCommands.cmds.get(request);
            if (c == null) {
               c = 0;
            }

            long t = (System.currentTimeMillis() - user.getLoginTime()) / 1000L;
            if (t < 0L || t > 65535L) {
               debugLog(String.format("Invalid command time %d", t));
               t = Math.max(0L, Math.min(t, 65535L));
            }

            ByteBuffer list = (ByteBuffer)user.getProperty("commands");
            synchronized(list) {
               if (list.available() < 4) {
                  sendClientRequestLog(user);
                  initClientRequestLog(user);
               }

               list.add((byte)(c >>> 0 & 255));
               list.add((byte)(c >>> 8 & 255));
               list.add((byte)((int)(t >>> 0 & 255L)));
               list.add((byte)((int)(t >>> 8 & 255L)));
            }

            debugLog(String.format(">>>>Received command: %s (%d) from user %d at time %d", request, c, user.getSession().getId(), t));
         }
      } catch (Exception var9) {
         Logger.trace(var9);
      }

   }

   public static void flushClientRequestLog(User user) {
      if (logRequests()) {
         try {
            ByteBuffer list = (ByteBuffer)user.getProperty("commands");
            synchronized(list) {
               sendClientRequestLog(user);
            }
         } catch (Exception var5) {
            Logger.trace(var5);
         }
      }

   }

   private static void initClientRequestLog(User user) {
      try {
         ByteBuffer buffer = (ByteBuffer)user.getProperty("commands");
         buffer.clear();
         long bbbId = (Long)user.getSession().getProperty("bbb_id");
         long start = user.getLoginTime();
         debugLog(String.format(">>>>>>> Adding user %d at time %d to command log", bbbId, start));

         int i;
         for(i = 0; i < 8; ++i) {
            buffer.add((byte)((int)(bbbId >>> i * 8 & 255L)));
         }

         for(i = 0; i < 8; ++i) {
            buffer.add((byte)((int)(start >>> i * 8 & 255L)));
         }
      } catch (Exception var7) {
         Logger.trace(var7);
      }

   }

   private static void sendClientRequestLog(User user) {
      MSMExtension ext = MSMExtension.getInstance();

      try {
         long bbbId = (Long)user.getSession().getProperty("bbb_id");
         ByteBuffer buffer = (ByteBuffer)user.getProperty("commands");
         if (buffer.used() > 16) {
            debugLog(String.format("**** Sending %d commands for user %d to %s", buffer.used() / 4, bbbId, requestQueue()));
            byte[] b = Arrays.copyOf(buffer.getBytes(), buffer.used());
            String msg = MigBase64.encodeToString(b, false);
            ext.getSQS().sendToQueue(requestQueue(), msg);
         }
      } catch (Exception var7) {
         Logger.trace(var7);
      }

   }

   private static void debugLog(String s) {
      if (_debugLog) {
         Logger.trace(s);
      }

   }
}
