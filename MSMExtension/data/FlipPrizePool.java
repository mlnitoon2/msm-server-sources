package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import java.util.ArrayList;

public class FlipPrizePool {
   public static final String ID_KEY = "id";
   public static final String CONSUMEABLE_KEY = "consumeable";
   public static final String IS_TOP_PRIZE = "is_top_prize";
   public static final String FALLBACK_KEY = "fallback";
   public static final String REWARDS_KEY = "rewards";
   private static final int scalingSignificantFigures = 3;
   private int id;
   private boolean consumeable;
   private boolean isTopPrize;
   private int fallback;
   private String rewardsStr;
   private ISFSArray rewards;

   public FlipPrizePool(ISFSObject s) {
      this.id = s.getInt("id");
      this.consumeable = s.getInt("consumeable") != 0;
      this.isTopPrize = s.getInt("is_top_prize") != 0;
      this.fallback = s.getInt("fallback");
      this.rewardsStr = s.getUtfString("rewards");
      this.rewards = SFSArray.newFromJsonData(this.rewardsStr);
   }

   public int getId() {
      return this.id;
   }

   public int getFallback() {
      return this.fallback;
   }

   public boolean isConsumeable() {
      return this.consumeable;
   }

   public boolean isTopPrize() {
      return this.isTopPrize;
   }

   public ISFSArray getRewards() {
      return this.rewards;
   }

   public ISFSArray getRemainingUnscaled(ISFSArray consumedUnscaledPrizes, int playerLevel) {
      if (this.consumeable && consumedUnscaledPrizes != null && consumedUnscaledPrizes.size() != 0) {
         ArrayList<Integer> consumedIndices = new ArrayList();

         for(int consumedInd = 0; consumedInd < consumedUnscaledPrizes.size(); ++consumedInd) {
            ISFSObject consumed = consumedUnscaledPrizes.getSFSObject(consumedInd);

            for(int rewardInd = 0; rewardInd < this.rewards.size(); ++rewardInd) {
               ISFSObject potentialReward = this.rewards.getSFSObject(rewardInd);
               if (SFSHelpers.equals(consumed, potentialReward) && !consumedIndices.contains(rewardInd)) {
                  consumedIndices.add(rewardInd);
                  break;
               }
            }
         }

         ISFSArray rewardsRemaining = new SFSArray();

         for(int rewardInd = 0; rewardInd < this.rewards.size(); ++rewardInd) {
            if (!consumedIndices.contains(rewardInd)) {
               ISFSObject potentialReward = this.rewards.getSFSObject(rewardInd);
               String type = potentialReward.getUtfString("type");
               Player.CurrencyType rewardType = Player.getCurrencyTypeFromString(type);
               if (rewardType != Player.CurrencyType.Undefined) {
                  rewardsRemaining.addSFSObject(potentialReward);
               } else {
                  Logger.trace(LogLevel.WARN, "UNSUPPORTED MEMORY FLIP REWARD TYPE: " + type);
               }
            }
         }

         return rewardsRemaining;
      } else {
         return this.rewards;
      }
   }

   public static Integer scaleReward(int playerLevel, Player.CurrencyType type, int amount, int prizePoolId) {
      switch(type) {
      case Ethereal:
         playerLevel = Math.max(FlipPrizePoolLookup.getInstance().getMinPlayerScalingLevel(), playerLevel);
         playerLevel = Math.min(playerLevel, FlipPrizePoolLookup.getInstance().getMaxPlayerScalingLevel());
         if (prizePoolId == 3) {
            amount = chopSignificantFigures((int)Math.pow((double)playerLevel, 2.0D), 3);
         } else if (prizePoolId == 2) {
            amount = chopSignificantFigures(playerLevel * 2, 3);
         } else if (prizePoolId == 1) {
            amount = chopSignificantFigures(playerLevel / 5, 3);
         }
         break;
      case Food:
         amount = chopSignificantFigures(ScratchTicketFunctions.scaleScratchReward(playerLevel, type, amount, false), 3);
         break;
      case Coins:
         amount = chopSignificantFigures(ScratchTicketFunctions.scaleScratchReward(playerLevel, type, amount, false), 3);
         break;
      case Xp:
         playerLevel = Math.max(FlipPrizePoolLookup.getInstance().getMinPlayerScalingLevel(), playerLevel);
         playerLevel = Math.min(playerLevel, FlipPrizePoolLookup.getInstance().getMaxPlayerScalingLevel());
         amount = chopSignificantFigures((int)((double)amount * Math.pow((double)playerLevel, 2.0D)), 3);
      }

      return amount;
   }

   public static int chopSignificantFigures(int amount, int maxSignFigures) {
      int leftDigitsMinusOne = (int)Math.floor(Math.log10((double)amount));
      int numZeros = leftDigitsMinusOne - (maxSignFigures - 1);
      double baseNum = Math.pow(10.0D, (double)numZeros);
      int remains = (int)Math.floor((double)amount / baseNum);
      amount = (int)((double)remains * baseNum);
      return amount;
   }
}
