package com.bigbluebubble.mysingingmonsters.data;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class Code {
   private static final String CODE_ID = "code_id";
   private static final String ADVERTISER_ID = "advertiser_id";
   private static final String GAME_ID = "game_id";
   private static final String ADVERTISMENT_ID = "advertisment_id";
   private static final String CAMPAIGN_ID = "campaign_id";
   private static final String PROMO_CODE_ID = "promo_code";
   private static final String CODE_LIMIT = "code_limit";
   private static final String REWARDS_ID = "rewards";
   private static final String REWARDS_TEXT_ID = "reward_text";
   private static final String CLAIM_COUNT_ID = "claim_count";
   private static final String START_DATE_ID = "start_date";
   private static final String END_DATE_ID = "end_date";
   private static final String CHECKSUM_ID = "checksum";
   private static final String EXTRA_ID = "extra";
   private static final String CODE_INPUT = "code";
   public static final long DUMMY_BBB_ID = 500000000L;
   private int codeId = 0;
   private int advertiserId = 0;
   private int gameId = 0;
   private int advertismentId = 0;
   private int campaignId = 0;
   private String promoCode = null;
   private long codeLimit = 0L;
   private ISFSObject rewards = null;
   private String rewardText = null;
   private long claimCount = 0L;
   private long startDate = 0L;
   private long endDate = 0L;
   private ISFSObject checksum = null;
   private ISFSObject extra = null;
   private Integer code = null;

   public Code(ISFSObject codeData) {
      this.codeId = codeData.getInt("code_id");
      this.advertiserId = codeData.getInt("advertiser_id");
      this.gameId = codeData.getInt("game_id");
      this.advertismentId = codeData.getInt("advertisment_id");
      this.campaignId = codeData.getInt("campaign_id");
      this.codeLimit = codeData.getLong("code_limit");
      this.rewards = SFSObject.newFromJsonData(codeData.getUtfString("rewards"));
      this.rewardText = codeData.getUtfString("reward_text");
      this.claimCount = codeData.getLong("claim_count");
      if (codeData.containsKey("promo_code")) {
         this.promoCode = codeData.getUtfString("promo_code");
      }

      if (codeData.containsKey("start_date")) {
         this.startDate = codeData.getLong("start_date");
      }

      if (codeData.containsKey("end_date")) {
         this.endDate = codeData.getLong("end_date");
      }

      if (codeData.containsKey("checksum")) {
         this.checksum = SFSObject.newFromJsonData(codeData.getUtfString("checksum"));
      }

      if (codeData.containsKey("extra")) {
         this.extra = SFSObject.newFromJsonData(codeData.getUtfString("extra"));
      }

      if (codeData.containsKey("code")) {
         this.code = codeData.getInt("code");
      }

   }

   public int getCodeID() {
      return this.codeId;
   }

   public int getAdvertiserID() {
      return this.advertiserId;
   }

   public int getGameID() {
      return this.gameId;
   }

   public int getAdvertismentID() {
      return this.advertismentId;
   }

   public int getCampaignID() {
      return this.campaignId;
   }

   public String getPromoCode() {
      return this.promoCode;
   }

   public long getCodeLimit() {
      return this.codeLimit;
   }

   public ISFSObject getRewards() {
      return this.rewards;
   }

   public String getRewardText() {
      return this.rewardText;
   }

   public long getClaimCount() {
      return this.claimCount;
   }

   public long getStartDate() {
      return this.startDate;
   }

   public long getEndDate() {
      return this.endDate;
   }

   public ISFSObject getChecksum() {
      return this.checksum;
   }

   public ISFSObject getExtra() {
      return this.extra;
   }

   public Integer getCode() {
      return this.code;
   }
}
