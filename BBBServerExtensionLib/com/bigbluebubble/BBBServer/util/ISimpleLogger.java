package com.bigbluebubble.BBBServer.util;

public interface ISimpleLogger {
   void SetLogger(Object var1);

   void trace(LogLevel var1, Object... var2);

   void trace(Object... var1);

   void trace(Exception var1, Object... var2);
}
