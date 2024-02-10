package com.bigbluebubble.mysingingmonsters.battle.verification;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.mysingingmonsters.battle.BattleOpponents;
import com.bigbluebubble.mysingingmonsters.battle.verification.buffs.BuffEtherealHealing;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import java.util.Iterator;

public class BattleVerification {
   public static boolean IsValidResult(BattleOpponents playerTeamData, BattleOpponents opponentTeamData, long seed, ISFSArray actions, boolean opponentCanSwap, float pickOptimalPercentage) throws Exception {
      BattleState battleState = new BattleState(seed);
      battleState.opponentCanSwap = opponentCanSwap;
      battleState.pickOptimalPercentage = pickOptimalPercentage;
      BattleTeam playerTeam = new BattleTeam(playerTeamData);
      battleState.teams[0] = playerTeam;
      battleState.activePlayers[0] = (BattlePlayer)battleState.teams[0].players.get(0);
      BattleTeam opponentTeam = new BattleTeam(opponentTeamData);
      battleState.teams[1] = opponentTeam;
      battleState.activePlayers[1] = (BattlePlayer)battleState.teams[1].players.get(0);

      for(int i = 0; i < battleState.teams.length; ++i) {
         for(int j = 0; j < battleState.teams[i].players.size(); ++j) {
            BattlePlayer p = (BattlePlayer)battleState.teams[i].players.get(j);
            if (p != null && p.isEthereal()) {
               p.addBuff(new BuffEtherealHealing(p, GameSettings.get("USER_BATTLE_ETHEREAL_HEAL_AMOUNT", 0.1F), GameSettings.get("USER_BATTLE_ETHEREAL_HEAL_STACKS", 5), GameSettings.get("USER_BATTLE_ETHEREAL_HEAL_VARIANCE_MIN", 1.0F), GameSettings.get("USER_BATTLE_ETHEREAL_HEAL_VARIANCE_MAX", 2.0F)));
            }
         }
      }

      Iterator i = actions.iterator();

      while(i.hasNext()) {
         ISFSObject actionObj = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
         if (actionObj.containsKey("t")) {
            int team = actionObj.getInt("t");
            if (team == 0) {
               battleState.applyPlayerAction(actionObj);
            } else {
               if (team != 1) {
                  return false;
               }

               battleState.applyOpponentAction(actionObj);
            }
         } else {
            battleState.applyPlayerAction(actionObj);
            battleState.applyOpponentAction((ISFSObject)null);
            if (playerTeam.livingCount() == 0) {
               return false;
            }
         }
      }

      return battleState.playerDidWin();
   }
}
