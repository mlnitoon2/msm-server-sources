package com.bigbluebubble.BBBServer.util;

public class SimpleLogger {
   private static ISimpleLogger iSimpleLogger = new slf4jLogger();

   private SimpleLogger() {
   }

   public static void SetupLogger(ISimpleLogger simpleLogger) {
      iSimpleLogger = simpleLogger;
   }

   public static void SetLogger(Object logger) {
      iSimpleLogger.SetLogger(logger);
   }

   public static void trace(LogLevel level, Object... args) {
      iSimpleLogger.trace(level, args);
   }

   public static void trace(Object... args) {
      iSimpleLogger.trace(args);
   }

   public static void trace(Exception e, Object... args) {
      iSimpleLogger.trace(e, args);
   }
}
