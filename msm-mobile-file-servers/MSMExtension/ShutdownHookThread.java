package com.bigbluebubble.mysingingmonsters;

public class ShutdownHookThread extends Thread {
   public void run() {
      Logger.trace("*************************************");
      Logger.trace("****  SHUT DOWN THREAD RUNNING   ****");
      Logger.trace("*************************************");
      MSMExtension.getInstance().destroy();
   }
}
