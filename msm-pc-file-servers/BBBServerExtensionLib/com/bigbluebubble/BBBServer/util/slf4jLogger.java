package com.bigbluebubble.BBBServer.util;

import java.util.Arrays;
import org.slf4j.Logger;

public class slf4jLogger implements ISimpleLogger {
   private Logger log4j;

   public void SetLogger(Object log4j) {
      this.SetLoggerInternal((Logger)log4j);
   }

   private void SetLoggerInternal(Logger log4j) {
      this.log4j = log4j;
   }

   private static String getTraceMessage(Object[] args) {
      StringBuilder traceMsg = new StringBuilder();
      if (args != null) {
         Object[] var2 = args;
         int var3 = args.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Object o = var2[var4];
            if (o == null) {
               traceMsg.append("NULL").append(" ");
            } else {
               traceMsg.append(o.toString()).append(" ");
            }
         }
      }

      return traceMsg.toString();
   }

   public void trace(LogLevel level, Object... args) {
      String msg = getTraceMessage(args);
      if (this.log4j == null) {
         System.out.println(msg);
      } else {
         switch(level) {
         case DEBUG:
            this.log4j.debug(msg);
            break;
         case INFO:
            this.log4j.info(msg);
            break;
         case WARN:
            this.log4j.warn(msg);
            break;
         case ERROR:
            this.log4j.error(msg);
         }
      }

   }

   public void trace(Object... args) {
      this.trace(LogLevel.INFO, args);
   }

   public void trace(Exception e, Object... args) {
      Object[] argsWithException = Arrays.copyOf(args, args.length + 1);
      argsWithException[args.length] = Misc.getStackTraceAsString(e);
      this.trace(LogLevel.ERROR, argsWithException);
   }
}
