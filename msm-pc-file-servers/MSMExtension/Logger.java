package com.bigbluebubble.mysingingmonsters;

import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.BBBServer.util.Misc;
import com.smartfoxserver.v2.extensions.ExtensionLogLevel;
import java.util.Arrays;

public class Logger {
   private static final String TAG = "{MySingingMonsters}:";

   private static String getTraceMessage(Object[] args) {
      StringBuilder traceMsg = (new StringBuilder()).append("{MySingingMonsters}:");
      if (args != null) {
         Object[] var2 = args;
         int var3 = args.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Object o = var2[var4];
            traceMsg.append(o.toString()).append(" ");
         }
      }

      return traceMsg.toString();
   }

   public static void trace(LogLevel level, Object... args) {
      MSMExtension ext = MSMExtension.getInstance();
      if (ext != null) {
         switch(level) {
         case DEBUG:
            ext.trace(ExtensionLogLevel.DEBUG, args);
            break;
         case INFO:
            ext.trace(ExtensionLogLevel.INFO, args);
            break;
         case WARN:
            ext.trace(ExtensionLogLevel.WARN, args);
            break;
         case ERROR:
            ext.trace(ExtensionLogLevel.ERROR, args);
         }
      } else {
         String msg = getTraceMessage(args);
         System.out.println(msg);
      }

   }

   public static void trace(Object... args) {
      trace(LogLevel.INFO, args);
   }

   public static void trace(Exception e, Object... args) {
      Object[] argsWithException = Arrays.copyOf(args, args.length + 1);
      argsWithException[args.length] = Misc.getStackTraceAsString(e);
      trace(LogLevel.ERROR, argsWithException);
   }
}
