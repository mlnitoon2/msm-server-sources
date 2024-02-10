package com.bigbluebubble.mysingingmonsters.battle;

import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataType;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;

public class BattleIslandCampaignState {
   private final String CAMPAIGNS_KEY = "campaigns";
   private final String ID_KEY = "id";
   private final String BATTLE_KEY = "b";
   private final String STARTED_KEY = "s";
   private final String COMPLETED_KEY = "c";
   private final String PURCHASED_REWARD_KEY = "p";
   ISFSArray campaigns_ = new SFSArray();

   public BattleIslandCampaignState() {
   }

   public BattleIslandCampaignState(ISFSObject campaignData) {
      if (campaignData.containsKey("campaigns")) {
         ISFSArray campaigns = campaignData.getSFSArray("campaigns");
         Iterator itr = campaigns.iterator();

         while(itr.hasNext()) {
            ISFSObject campaignObj = (ISFSObject)((ISFSObject)((SFSDataWrapper)itr.next()).getObject());
            if (campaignObj.containsKey("id") && campaignObj.getInt("id") > 0) {
               if (!campaignObj.containsKey("b")) {
                  campaignObj.putInt("b", 0);
               }

               if (campaignObj.containsKey("c") && campaignObj.get("c").getTypeId() != SFSDataType.LONG) {
                  campaignObj.removeElement("c");
               }

               this.campaigns_.addSFSObject(campaignObj);
            }
         }
      }

   }

   ISFSObject getCampaign(int campaignId) {
      Iterator itr = this.campaigns_.iterator();

      ISFSObject campaignObj;
      do {
         if (!itr.hasNext()) {
            ISFSObject campaignObj = new SFSObject();
            campaignObj.putInt("id", campaignId);
            campaignObj.putInt("b", 0);
            this.campaigns_.addSFSObject(campaignObj);
            return campaignObj;
         }

         campaignObj = (ISFSObject)((ISFSObject)((SFSDataWrapper)itr.next()).getObject());
      } while(campaignObj.getInt("id") != campaignId);

      return campaignObj;
   }

   public void resetCampaign(int campaignId) {
      ISFSObject campaign = this.getCampaign(campaignId);
      campaign.putInt("b", 0);
      if (campaign.containsKey("s")) {
         campaign.removeElement("s");
      }

      if (campaign.containsKey("c")) {
         campaign.removeElement("c");
      }

      if (campaign.containsKey("p")) {
         campaign.removeElement("p");
      }

   }

   public boolean hasStartedCampaign(int campaignId) {
      ISFSObject campaign = this.getCampaign(campaignId);
      return campaign.containsKey("s") && campaign.getLong("s") > 0L;
   }

   public long getCampaignStartTime(int campaignId) {
      ISFSObject campaign = this.getCampaign(campaignId);
      return campaign.containsKey("s") ? campaign.getLong("s") : 0L;
   }

   public void startCampaign(int campaignId, long timestamp) {
      ISFSObject campaign = this.getCampaign(campaignId);
      campaign.putLong("s", timestamp);
   }

   public boolean hasCompletedCampaign(int campaignId) {
      ISFSObject campaign = this.getCampaign(campaignId);
      return campaign.containsKey("c") && campaign.getLong("c") > 0L;
   }

   public long getCampaignCompletedTime(int campaignId) {
      ISFSObject campaign = this.getCampaign(campaignId);
      return campaign.containsKey("c") ? campaign.getLong("c") : 0L;
   }

   public void completeCampaign(int campaignId, long timestamp) {
      ISFSObject campaign = this.getCampaign(campaignId);
      campaign.putLong("c", timestamp);
   }

   public int getCampaignProgress(int campaignId) {
      ISFSObject campaign = this.getCampaign(campaignId);
      return campaign.getInt("b");
   }

   public void setCampaignProgress(int campaignId, int progress) {
      ISFSObject campaign = this.getCampaign(campaignId);
      campaign.putInt("b", progress);
   }

   public boolean hasPurchasedCampaignReward(int campaignId) {
      ISFSObject campaign = this.getCampaign(campaignId);
      return campaign.containsKey("p") && campaign.getInt("p") > 0;
   }

   public void purchaseCampaignReward(int campaignId) {
      ISFSObject campaign = this.getCampaign(campaignId);
      campaign.putInt("p", 1);
   }

   public ISFSObject toSFSObject() {
      ISFSObject obj = new SFSObject();
      obj.putSFSArray("campaigns", this.campaigns_);
      return obj;
   }
}
