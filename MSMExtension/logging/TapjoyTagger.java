package com.bigbluebubble.mysingingmonsters.logging;

import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.smartfoxserver.v2.entities.User;
import java.util.Collection;
import java.util.Collections;

public class TapjoyTagger {
   public static void reportLevel(Player player, User user, int level) {
      if (level >= 24) {
         MSMExtension.getInstance().updateTapjoyUserTags(user, Collections.singletonList("offertier3"), Collections.singletonList("offertier2"));
      } else if (level >= 10) {
         MSMExtension.getInstance().updateTapjoyUserTags(user, Collections.singletonList("offertier2"), Collections.singletonList("offertier1"));
      } else if (level >= 4) {
         MSMExtension.getInstance().updateTapjoyUserTags(user, Collections.singletonList("offertier1"), (Collection)null);
      }

   }
}
