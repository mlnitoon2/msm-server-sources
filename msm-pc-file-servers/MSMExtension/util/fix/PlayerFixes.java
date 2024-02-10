package com.bigbluebubble.mysingingmonsters.util.fix;

import com.bigbluebubble.mysingingmonsters.player.Player;
import com.smartfoxserver.v2.entities.User;

public class PlayerFixes {
   public static void Apply(Player fullPlayer, User user) {
      FixCostumeCredits.ApplyFix(fullPlayer);
      FixBattleTutorial.ApplyFix(fullPlayer, user);
   }
}
