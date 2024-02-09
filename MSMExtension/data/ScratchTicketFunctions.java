package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventManager;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.EggstravaganzaEvent;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

public class ScratchTicketFunctions {
   public static ISFSObject initializeScratchOff(User sender, boolean isPaidPlay, String type, boolean test) {
      MSMExtension ext = MSMExtension.getInstance();
      Player player = (Player)sender.getProperty("player_object");
      Island island = IslandLookup.get(player.getActiveIsland().getIndex());
      List<ISFSObject> allTicketPrizes = null;
      if (!type.equalsIgnoreCase("C") && !type.equalsIgnoreCase("M")) {
         if (type.equalsIgnoreCase("S")) {
            allTicketPrizes = PrizeLookup.getSpinWheelPrizes();
         }
      } else {
         allTicketPrizes = PrizeLookup.getScratchOffs();
      }

      if (allTicketPrizes == null) {
         return null;
      } else {
         VersionInfo currentClientVer = new VersionInfo((String)sender.getSession().getProperty("client_version"));
         VersionInfo maxSupportedServerVersion = VersionData.Instance().getMaxServerVersionFromClientVersion(currentClientVer);
         ISFSArray ticketPrizes = new SFSArray();

         int resultIndex;
         for(resultIndex = 0; resultIndex < allTicketPrizes.size(); ++resultIndex) {
            ISFSObject prize = (ISFSObject)allTicketPrizes.get(resultIndex);
            if (prize.getUtfString("type").equalsIgnoreCase(type) && ext.scratchOffPrizeSupportedByServerVer(prize, maxSupportedServerVersion)) {
               if (type.equalsIgnoreCase("M")) {
                  if (island.hasMonster(prize.getInt("amount")) && MonsterLookup.get(prize.getInt("amount")).getLevelUnlocked() <= player.getLevel()) {
                     ticketPrizes.addSFSObject(prize);
                  }
               } else {
                  ticketPrizes.addSFSObject(prize);
               }
            }
         }

         if (ticketPrizes.size() == 0) {
            return null;
         } else {
            int resultIndex = true;
            if ((!type.equalsIgnoreCase("C") || player.getScratchPrize(type) != null) && (!type.equalsIgnoreCase("S") || player.getScratchPrize(type) != null)) {
               resultIndex = chooseWinner(ticketPrizes);
            } else {
               Random random = new Random();
               resultIndex = random.nextInt(ticketPrizes.size());
            }

            if (resultIndex == -1) {
               return null;
            } else {
               if (type.equalsIgnoreCase("S") && ticketPrizes.getSFSObject(resultIndex).getInt("is_top_prize") == 1) {
                  List<ISFSObject> allJackpotPrizes = PrizeLookup.getSpinWheelJackpotPrizes();
                  ticketPrizes = new SFSArray();

                  for(int i = 0; i < allJackpotPrizes.size(); ++i) {
                     ISFSObject prize = (ISFSObject)allJackpotPrizes.get(i);
                     if (prize.getUtfString("type").equalsIgnoreCase(type) && ext.scratchOffPrizeSupportedByServerVer(prize, maxSupportedServerVersion)) {
                        ticketPrizes.addSFSObject(prize);
                     }
                  }

                  if (ticketPrizes.size() == 0) {
                     return null;
                  }

                  resultIndex = chooseWinner(ticketPrizes);
                  if (resultIndex == -1) {
                     return null;
                  }
               }

               ISFSObject prizeObj = new SFSObject();
               prizeObj.putInt("id", ticketPrizes.getSFSObject(resultIndex).getInt("id"));
               prizeObj.putUtfString("type", ticketPrizes.getSFSObject(resultIndex).getUtfString("type"));
               prizeObj.putUtfString("prize", ticketPrizes.getSFSObject(resultIndex).getUtfString("prize"));
               prizeObj.putInt("amount", ticketPrizes.getSFSObject(resultIndex).getInt("amount"));
               prizeObj.putInt("is_top_prize", ticketPrizes.getSFSObject(resultIndex).getInt("is_top_prize"));
               String prizeJson = prizeObj.toJson();

               try {
                  String table = null;
                  if (type.equalsIgnoreCase("M")) {
                     table = "user_monster_scratch_offs";
                  } else if (type.equalsIgnoreCase("C")) {
                     table = "user_scratch_offs";
                  } else if (type.equalsIgnoreCase("S")) {
                     table = "user_spin_wheels";
                  }

                  if (table == null) {
                     return null;
                  }

                  if (!test) {
                     String sql;
                     Object[] args;
                     if (isPaidPlay) {
                        sql = "UPDATE " + table + " SET last_prize=?, num_paid_plays=num_paid_plays+1, prize_claimed=0 WHERE user=?";
                        args = new Object[]{prizeJson, player.getPlayerId()};
                        ext.getDB().update(sql, args);
                     } else {
                        sql = "INSERT INTO " + table + " SET user=?, last_prize=?, last_free_play=NOW(), num_free_plays=1, prize_claimed=0 ON DUPLICATE KEY UPDATE last_prize=?, num_free_plays=num_free_plays+1, last_free_play=NOW(), prize_claimed=0";
                        args = new Object[]{player.getPlayerId(), prizeJson, prizeJson};
                        ext.getDB().update(sql, args);
                     }
                  }

                  int isTopPrize = prizeObj.getInt("is_top_prize");
                  if (!test && isTopPrize == 1) {
                     String sql = "INSERT INTO user_scratch_offs_winners SET prize=?, amount=?, user=?, won_on=NOW()";
                     Object[] args = new Object[]{prizeObj.getUtfString("prize"), prizeObj.getInt("amount"), player.getPlayerId()};
                     ext.getDB().update(sql, args);
                  }

                  player.setUnclaimedScratchOff(type, true);
                  player.setScratchPrize(type, prizeJson);
                  if (!test && !isPaidPlay) {
                     long currentTime = MSMExtension.CurrentDBTime();
                     long timeDelay = 3600000L * (type.equalsIgnoreCase("M") ? (long)monsterScratchOffTimeBetweenFreePlays() : GameSettings.getLong("SCRATCHOFF_TIME_BETWEEN_FREE_PLAYS"));
                     long eligibleToPlay = currentTime + timeDelay;
                     player.setNextScratchOffTime(type, eligibleToPlay);
                  }
               } catch (Exception var22) {
                  Logger.trace(var22);
               }

               return prizeObj;
            }
         }
      }
   }

   public static int monsterScratchOffTimeBetweenFreePlays() {
      List<TimedEvent> eggstravaganzas = TimedEventManager.instance().currentActiveOnKey(TimedEventType.Eggstravaganza, 0, 0);
      return eggstravaganzas.size() == 0 ? GameSettings.getInt("MONSTER_SCRATCHOFF_TIME_BETWEEN_FREE_PLAYS") : ((EggstravaganzaEvent)eggstravaganzas.get(0)).scratchOffTimeBetweenFreePlays();
   }

   public static ISFSObject getScaledPrizeAmounts(User sender, String type) {
      List<ISFSObject> allTicketPrizes = null;
      if (type.equalsIgnoreCase("S")) {
         allTicketPrizes = PrizeLookup.getSpinWheelPrizes();
      } else {
         allTicketPrizes = PrizeLookup.getScratchOffs();
      }

      ISFSArray scaledTicketPrizes = new SFSArray();

      for(int i = 0; i < allTicketPrizes.size(); ++i) {
         ISFSObject prize = (ISFSObject)allTicketPrizes.get(i);
         if (prize.getUtfString("type").equalsIgnoreCase("C") || prize.getUtfString("type").equalsIgnoreCase("S")) {
            Player.CurrencyType rewardType = Player.getCurrencyTypeFromString(prize.getUtfString("prize"));
            if (rewardType == Player.CurrencyType.Food || rewardType == Player.CurrencyType.Coins) {
               ISFSObject prizeObj = new SFSObject();
               prizeObj.putInt("id", prize.getInt("id"));
               prizeObj.putUtfString("prize", prize.getUtfString("prize"));
               prizeObj.putInt("amount", prize.getInt("amount"));
               Player player = (Player)sender.getProperty("player_object");
               prizeObj.putInt("scaled_amount", scaleScratchReward(player.getLevel(), rewardType, prize.getInt("amount"), false));
               scaledTicketPrizes.addSFSObject(prizeObj);
            }
         }
      }

      ISFSObject prizes = new SFSObject();
      prizes.putSFSArray("prizes", scaledTicketPrizes);
      return prizes;
   }

   public static Integer scaleScratchReward(int playerLevel, Player.CurrencyType type, int amount, boolean multiplier) {
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

   private static Integer roundToHundreds(Integer amount) {
      int roundTo = 100;
      int remainder = amount % roundTo;
      return remainder >= roundTo / 2 ? amount - remainder + roundTo : amount - remainder;
   }

   public static Integer getAlternativeMonsterScratch(User user, Island island, int monster) {
      Player player = (Player)user.getProperty("player_object");
      List<ISFSObject> allTicketPrizes = PrizeLookup.getScratchOffs();
      int probability = -1;

      for(int i = 0; i < allTicketPrizes.size(); ++i) {
         ISFSObject prize = (ISFSObject)allTicketPrizes.get(i);
         if (prize.getUtfString("type").equalsIgnoreCase("M") && prize.getInt("amount") == monster) {
            probability = prize.getInt("probability");
            break;
         }
      }

      VersionInfo currentClientVer = new VersionInfo((String)user.getSession().getProperty("client_version"));
      VersionInfo maxSupportedServerVersion = VersionData.Instance().getMaxServerVersionFromClientVersion(currentClientVer);

      for(int i = 0; i < allTicketPrizes.size(); ++i) {
         ISFSObject prize = (ISFSObject)allTicketPrizes.get(i);
         if (MSMExtension.getInstance().scratchOffPrizeSupportedByServerVer(prize, maxSupportedServerVersion) && prize.getUtfString("type").equalsIgnoreCase("M") && prize.getInt("probability") == probability) {
            int newMonster = prize.getInt("amount");
            if (island.hasMonster(newMonster) && MonsterLookup.get(newMonster).getLevelUnlocked() <= player.getLevel()) {
               return newMonster;
            }
         }
      }

      return null;
   }

   public static int getPossibleRare(int monster, int islandType) {
      float rareChance = 0.0F;
      float epicChance = 0.0F;
      if (!TimedEventManager.instance().hasTimedEventNow(TimedEventType.Eggstravaganza, 0, 0)) {
         rareChance = GameSettings.getFloat("MONSTER_SCRATCHOFF_RARE_CHANCE");
         epicChance = GameSettings.getFloat("MONSTER_SCRATCHOFF_EPIC_CHANCE");
      } else {
         List<TimedEvent> eggstravaganza = TimedEventManager.instance().currentActiveOnKey(TimedEventType.Eggstravaganza, 0, 0);
         EggstravaganzaEvent eggs = (EggstravaganzaEvent)eggstravaganza.get(0);
         rareChance = eggs.rareChance();
         epicChance = eggs.epicChance();
      }

      Random r = new Random();
      float chance = r.nextFloat();
      ArrayList rare;
      if (chance < epicChance) {
         rare = MonsterCommonToEpicMapping.commonToEpicMapGet(monster);
         if (rare != null && rare.size() > 0) {
            monster = ((MonsterEpicData)rare.get(0)).epicMonsterId();
         }
      } else if (chance < epicChance + rareChance) {
         rare = MonsterCommonToRareMapping.commonToRareMapGet(monster);
         if (rare != null && rare.size() > 0) {
            monster = ((MonsterRareData)rare.get(0)).rareMonsterId();
         }
      }

      return monster;
   }

   public static int chooseWinner(ISFSArray rewards) {
      int resultIndex = -1;
      int totalProbability = 0;

      for(int i = 0; i < rewards.size(); ++i) {
         totalProbability += rewards.getSFSObject(i).getInt("probability");
      }

      Random random = new Random();
      int randomValue = random.nextInt(totalProbability);
      totalProbability = 0;

      for(int i = 0; i < rewards.size(); ++i) {
         int curPrizeProb = rewards.getSFSObject(i).getInt("probability");
         if (0 > curPrizeProb) {
            totalProbability = 0;
         }

         totalProbability += curPrizeProb;
         if (curPrizeProb > 0 && randomValue < totalProbability) {
            resultIndex = i;
            break;
         }
      }

      return resultIndex;
   }

   public static void testSpinWheel(User sender, ISFSObject response) {
      List<ISFSObject> spinWheelPrizes = PrizeLookup.getSpinWheelPrizes();
      HashMap<Integer, Integer> spinResults = new HashMap();
      VersionInfo currentClientVer = new VersionInfo((String)sender.getSession().getProperty("client_version"));
      VersionInfo maxSupportedServerVersion = VersionData.Instance().getMaxServerVersionFromClientVersion(currentClientVer);
      ISFSArray ticketPrizes = new SFSArray();

      for(int i = 0; i < spinWheelPrizes.size(); ++i) {
         ISFSObject prize = (ISFSObject)spinWheelPrizes.get(i);
         if (MSMExtension.getInstance().scratchOffPrizeSupportedByServerVer(prize, maxSupportedServerVersion) && prize.getUtfString("type").equalsIgnoreCase("S")) {
            ticketPrizes.addSFSObject(prize);
         }
      }

      int numRuns = 10000;

      for(int j = 0; j < numRuns; ++j) {
         int resultIndex = chooseWinner(ticketPrizes);
         boolean foundKey = false;
         Iterator var11 = spinResults.entrySet().iterator();

         while(var11.hasNext()) {
            Entry<Integer, Integer> result = (Entry)var11.next();
            if ((Integer)result.getKey() == resultIndex) {
               result.setValue((Integer)result.getValue() + 1);
               foundKey = true;
               break;
            }
         }

         if (!foundKey) {
            spinResults.put(resultIndex, 1);
         }
      }

      SFSArray arr = new SFSArray();
      float percentageDivisor = (float)numRuns / 100.0F;
      Logger.trace("Spin probabilities are: ");
      Iterator var20 = spinResults.entrySet().iterator();

      while(var20.hasNext()) {
         Entry<Integer, Integer> result = (Entry)var20.next();
         int resultIndex = (Integer)result.getKey();
         float percentProbability = (float)(Integer)result.getValue() / percentageDivisor;
         int id = ticketPrizes.getSFSObject(resultIndex).getInt("id");
         Logger.trace("Chance of getting result at (id) " + id + " is " + percentProbability);
         SFSObject curProbability = new SFSObject();
         curProbability.putInt("id", id);
         curProbability.putFloat("probability", percentProbability);
         arr.addSFSObject(curProbability);
      }

      response.putSFSArray("probabilities", arr);
   }
}
