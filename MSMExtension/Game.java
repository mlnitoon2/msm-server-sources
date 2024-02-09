package com.bigbluebubble.mysingingmonsters;

public class Game {
   public static int DiamondsRequiredToComplete(long timeLeft) {
      if (timeLeft > 0L) {
         double hoursLeft = (double)((float)timeLeft / 1000.0F / 3600.0F);
         int diamonds = (int)Math.ceil(hoursLeft);
         return diamonds;
      } else {
         return 0;
      }
   }
}
