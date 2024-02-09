package com.bigbluebubble.mysingingmonsters.logging;

import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.smartfoxserver.v2.entities.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Firebase {
   public static void reportLevel(User user, int level) {
      switch(level) {
      case 7:
      case 10:
         forwardEvent(user, String.format("level_up_%d", level));
      default:
      }
   }

   public static void reportSpeedup(User user) {
      forwardEvent(user, "first_speedup_video");
   }

   public static void reportIsland(User user, int island) {
      switch(island) {
      case 2:
         forwardEvent(user, "get_island_cold");
      default:
      }
   }

   public static void reportPlayerReferral(User user, long bbbId) {
      forwardEvent(user, "player_referral");
   }

   public static void reportOfferCompleted(User user, int numCompleted) {
      if (numCompleted > 0) {
         String eventName = "offer_completed";
         List<String> firebaseEvents = new ArrayList(numCompleted);

         for(int i = 0; i < numCompleted; ++i) {
            firebaseEvents.add(eventName);
         }

         MSMExtension.getInstance().forwardUserEvent(user, "firebase_events", firebaseEvents);
      }
   }

   public static void forwardEvent(User sender, String firebaseEvent) {
      MSMExtension.getInstance().forwardUserEvent(sender, "firebase_events", Collections.singletonList(firebaseEvent));
   }
}
