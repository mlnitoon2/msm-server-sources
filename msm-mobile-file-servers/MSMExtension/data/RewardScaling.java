package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.mysingingmonsters.player.Player;

public class RewardScaling {
   private static Integer roundToHundreds(Integer amount) {
      int roundTo = 100;
      int remainder = amount % roundTo;
      return remainder >= roundTo / 2 ? amount - remainder + roundTo : amount - remainder;
   }

   public static Integer scaleRewards(int playerLevel, Player.CurrencyType type, int amount, boolean multiplier) {
      if (playerLevel >= GameSettings.getInt("SCRATCHOFF_SCALING_START_LEVEL")) {
         if (type == Player.CurrencyType.Coins) {
            amount = roundToHundreds((int)((float)amount * ((float)playerLevel * GameSettings.getFloat("SCRATCHOFF_SCALING_COIN_VAR_A") + GameSettings.getFloat("SCRATCHOFF_SCALING_COIN_VAR_B"))));
         } else if (type == Player.CurrencyType.Food) {
            amount = roundToHundreds((int)((float)amount * ((float)playerLevel * GameSettings.getFloat("SCRATCHOFF_SCALING_FOOD_VAR_A") + GameSettings.getFloat("SCRATCHOFF_SCALING_FOOD_VAR_B"))));
         }
      }

      amount = multiplier ? GameSettings.getInt("USER_SCRATCH_REWARD_MULTIPLIER") * amount : amount;
      return amount;
   }
}
