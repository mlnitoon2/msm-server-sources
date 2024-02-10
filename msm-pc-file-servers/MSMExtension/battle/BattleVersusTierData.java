package com.bigbluebubble.mysingingmonsters.battle;

import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class BattleVersusTierData {
   private static final String TEAM_SIZE_KEY = "team_size";
   private static final String BEDS_KEY = "beds";
   private static final String STARS_KEY = "stars";
   private static final String REWARD_KEY = "reward";
   private static final String REQUIREMENTS_KEY = "requirements";
   private static final String PENALTY_KEY = "penalty";
   private static final String NUM_CHAMPS_KEY = "num_champs";
   private static final int DEFAULT_TEAM_SIZE = 3;
   private static final int DEFAULT_BEDS = -1;
   private static final int DEFAULT_STARS = 0;
   private static final BattleVersusTierData.Penalty DEFAULT_PENALTY;
   private static final int DEFAULT_NUM_CHAMPS = 0;
   protected ISFSObject data = new SFSObject();
   protected BattleRewardData reward;
   protected List<BattleRequirements> requirements;

   public BattleVersusTierData(ISFSObject battleCampaignEventData) {
      if (battleCampaignEventData.containsKey("team_size")) {
         this.data.putInt("team_size", battleCampaignEventData.getInt("team_size"));
      }

      if (battleCampaignEventData.containsKey("beds")) {
         this.data.putInt("beds", battleCampaignEventData.getInt("beds"));
      }

      if (battleCampaignEventData.containsKey("stars")) {
         this.data.putInt("stars", battleCampaignEventData.getInt("stars"));
      }

      if (battleCampaignEventData.containsKey("penalty")) {
         this.data.putUtfString("penalty", battleCampaignEventData.getUtfString("penalty"));
      }

      if (battleCampaignEventData.containsKey("num_champs")) {
         this.data.putInt("num_champs", battleCampaignEventData.getInt("num_champs"));
      }

      if (battleCampaignEventData.containsKey("reward")) {
         this.reward = new BattleRewardData(battleCampaignEventData.getSFSObject("reward"));
         this.data.putSFSObject("reward", this.reward.data);
      }

      this.requirements = new ArrayList();
      if (battleCampaignEventData.containsKey("requirements")) {
         ISFSArray requirementsData = battleCampaignEventData.getSFSArray("requirements");
         Iterator i = requirementsData.iterator();

         while(i.hasNext()) {
            ISFSObject requirementsObj = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
            BattleRequirements requirement = new BattleRequirements(requirementsObj);
            this.requirements.add(requirement);
         }

         this.data.putSFSArray("requirements", requirementsData);
      }

   }

   public int getTeamSize() {
      return this.data.containsKey("team_size") ? this.data.getInt("team_size") : 3;
   }

   public int getBeds() {
      return this.data.containsKey("beds") ? this.data.getInt("beds") : -1;
   }

   public int getStars() {
      return this.data.containsKey("stars") ? this.data.getInt("stars") : 0;
   }

   public BattleVersusTierData.Penalty getPenalty() {
      return this.data.containsKey("penalty") ? BattleVersusTierData.Penalty.valueOf(this.data.getUtfString("penalty")) : DEFAULT_PENALTY;
   }

   public int getNumChamps() {
      return this.data.containsKey("num_champs") ? this.data.getInt("num_champs") : 0;
   }

   public BattleRewardData getReward() {
      return this.reward;
   }

   public BattleRequirements getRequirementsForSlot(int slot) {
      return this.requirements != null && slot < this.requirements.size() ? (BattleRequirements)this.requirements.get(slot) : null;
   }

   static {
      DEFAULT_PENALTY = BattleVersusTierData.Penalty.NONE;
   }

   public static enum Penalty {
      NONE,
      STARS,
      TIER;
   }
}
