package com.bigbluebubble.mysingingmonsters.battle.verification;

import com.bigbluebubble.mysingingmonsters.battle.BattleOpponentData;
import com.bigbluebubble.mysingingmonsters.battle.BattleOpponents;
import java.util.ArrayList;
import java.util.List;

public class BattleTeam {
   List<BattlePlayer> players = new ArrayList();

   BattleTeam(BattleOpponents teamData) {
      for(int i = 0; i < teamData.size(); ++i) {
         BattleOpponentData opponentData = teamData.get(i);
         this.players.add(new BattlePlayer(opponentData.getId(), opponentData.getLevel(), opponentData.getCostume()));
      }

   }

   int livingCount() {
      int living = 0;

      for(int i = 0; i < this.players.size(); ++i) {
         if (!((BattlePlayer)this.players.get(i)).isDead()) {
            ++living;
         }
      }

      return living;
   }

   int remainingHealth() {
      int health = 0;

      for(int i = 0; i < this.players.size(); ++i) {
         health += ((BattlePlayer)this.players.get(i)).health;
      }

      return health;
   }
}
