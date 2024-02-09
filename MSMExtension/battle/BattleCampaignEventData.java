package com.bigbluebubble.mysingingmonsters.battle;

import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class BattleCampaignEventData {
   private static final String TEAM_SIZE_KEY = "team_size";
   private static final String REWARD_KEY = "reward";
   private static final String OPPONENTS_KEY = "monsters";
   private static final String REQUIREMENTS_KEY = "requirements";
   private static final int DEFAULT_TEAM_SIZE = 3;
   protected ISFSObject data = new SFSObject();
   protected BattleRewardData reward;
   protected BattleOpponents opponents;
   protected List<BattleRequirements> requirements;

   public BattleCampaignEventData(ISFSObject battleCampaignEventData) {
      if (battleCampaignEventData.containsKey("team_size")) {
         this.data.putInt("team_size", battleCampaignEventData.getInt("team_size"));
      }

      this.opponents = new BattleOpponents();
      ISFSArray opponentData = battleCampaignEventData.getSFSArray("monsters");
      Iterator i = opponentData.iterator();

      while(i.hasNext()) {
         ISFSObject opponentObj = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
         BattleOpponentData opponent = new BattleOpponentData(opponentObj);
         this.opponents.add(opponent);
      }

      this.data.putSFSArray("monsters", opponentData);
      if (battleCampaignEventData.containsKey("reward")) {
         this.reward = new BattleRewardData(battleCampaignEventData.getSFSObject("reward"));
         this.data.putSFSObject("reward", this.reward.data);
      }

      this.requirements = new ArrayList();
      if (battleCampaignEventData.containsKey("requirements")) {
         ISFSArray requirementsData = battleCampaignEventData.getSFSArray("requirements");
         i = requirementsData.iterator();

         while(i.hasNext()) {
            ISFSObject requirementsObj = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
            BattleRequirements requirement = new BattleRequirements(requirementsObj);
            this.requirements.add(requirement);
         }

         this.data.putSFSArray("requirements", requirementsData);
      }

   }

   public BattleOpponents getOpponents() {
      return this.opponents;
   }

   public int getTeamSize() {
      return this.data.containsKey("team_size") ? this.data.getInt("team_size") : 3;
   }

   public BattleRewardData getReward() {
      return this.reward;
   }

   public BattleRequirements getRequirementsForSlot(int slot) {
      return this.requirements != null && slot < this.requirements.size() ? (BattleRequirements)this.requirements.get(slot) : null;
   }
}
