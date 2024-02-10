package com.bigbluebubble.mysingingmonsters.battle;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class BattleIslandState {
   private final String CAMPAIGN_KEY = "campaign_data";
   private BattleIslandCampaignState campaignState_;
   private final String MUSIC_KEY = "music_data";
   private BattleIslandMusicState musicState_;
   private final String SEED_KEY = "seed";
   private long seed;

   public BattleIslandCampaignState getCampaignState() {
      return this.campaignState_;
   }

   public BattleIslandMusicState getMusicState() {
      return this.musicState_;
   }

   public BattleIslandState() {
      this.campaignState_ = new BattleIslandCampaignState();
      this.musicState_ = new BattleIslandMusicState();
   }

   public BattleIslandState(ISFSObject islandData) {
      if (islandData.containsKey("campaign_data")) {
         this.campaignState_ = new BattleIslandCampaignState(islandData.getSFSObject("campaign_data"));
      } else {
         this.campaignState_ = new BattleIslandCampaignState();
      }

      if (islandData.containsKey("music_data")) {
         this.musicState_ = new BattleIslandMusicState(islandData.getSFSObject("music_data"));
      } else {
         this.musicState_ = new BattleIslandMusicState();
      }

      if (islandData.containsKey("seed")) {
         this.seed = islandData.getLong("seed");
      }

   }

   public long getSeed() {
      return this.seed;
   }

   public void setSeed(long seed) {
      this.seed = seed;
   }

   public ISFSObject toSFSObject() {
      ISFSObject obj = new SFSObject();
      obj.putSFSObject("campaign_data", this.campaignState_.toSFSObject());
      obj.putSFSObject("music_data", this.musicState_.toSFSObject());
      if (this.seed > 0L) {
         obj.putLong("seed", this.seed);
      }

      return obj;
   }
}
