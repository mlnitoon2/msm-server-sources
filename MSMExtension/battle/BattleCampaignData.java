package com.bigbluebubble.mysingingmonsters.battle;

import com.bigbluebubble.mysingingmonsters.schedules.ISchedulable;
import com.bigbluebubble.mysingingmonsters.schedules.Schedule;
import com.bigbluebubble.mysingingmonsters.schedules.ScheduleLookup;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticData;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class BattleCampaignData extends StaticData implements ISchedulable {
   private static final String ID_KEY = "id";
   private static final String NAME_KEY = "name";
   private static final String DEPENDS_KEY = "depends";
   private static final String BATTLES_KEY = "battles";
   private static final String REWARD_KEY = "reward";
   private static final String REWARD_BUYOUT_TIME_KEY = "reward_buyout_time";
   private static final String REWARD_BUYOUT_COST_KEY = "reward_buyout_cost";
   private static final String EXTRA_KEY = "extra";
   private static final String ANIM_FILE_KEY = "anim_file";
   private static final String ANIM_KEY = "anim";
   private static final String BG_ANIM_KEY = "bg_anim";
   private static final String BG_ANIM_FILE_KEY = "bg_anim_file";
   private static final String COSTUME_KEY = "costumeId";
   private static final String LAST_CHANGED_KEY = "last_changed";
   private static final String SCHEDULE_KEY = "schedule_id";
   private static final String SCHEDULE_DATA_KEY = "schedule";
   private static final String REQUIREMENTS_KEY = "requirements";
   private static final String MAX_TRAINING_LEVEL_KEY = "max_training_level";
   private static final String FRAME_KEY = "frame";
   private static final String FRAME_SHEET_KEY = "frame_sheet";
   private static final String ICON_KEY = "icon";
   private static final String ICON_SHEET_KEY = "icon_sheet";
   private static final String DISPLAY_ID_KEY = "display_id";
   private static final String PVP_KEY = "pvp";
   private static final String TIERS_KEY = "tiers";
   private static final String POOL_KEY = "pool_id";
   private static final String PVP_DEFAULT_LOADOUT_KEY = "pvp_default_loadout";
   private static final String SILENT_KEY = "silent";
   protected List<BattleCampaignEventData> battles;
   protected List<BattleVersusTierData> tiers;
   protected BattleRewardData reward;
   protected BattleRequirements requirements;

   public BattleCampaignData(ISFSObject campaignData) {
      super(campaignData);
      SFSArray battleData;
      Iterator i;
      ISFSObject tierObj;
      if (this.isPVP()) {
         this.tiers = new ArrayList();
         battleData = SFSArray.newFromJsonData(campaignData.getUtfString("battles"));
         i = battleData.iterator();

         while(i.hasNext()) {
            tierObj = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
            BattleVersusTierData tier = new BattleVersusTierData(tierObj);
            this.tiers.add(tier);
         }
      } else {
         this.battles = new ArrayList();
         battleData = SFSArray.newFromJsonData(campaignData.getUtfString("battles"));
         i = battleData.iterator();

         while(i.hasNext()) {
            tierObj = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
            BattleCampaignEventData battle = new BattleCampaignEventData(tierObj);
            this.battles.add(battle);
         }
      }

      String requirementsString;
      if (campaignData.containsKey("reward")) {
         requirementsString = campaignData.getUtfString("reward");
         if (!requirementsString.isEmpty()) {
            this.reward = new BattleRewardData(SFSObject.newFromJsonData(requirementsString));
         }
      }

      if (campaignData.containsKey("requirements")) {
         requirementsString = campaignData.getUtfString("requirements");
         if (!requirementsString.isEmpty()) {
            this.requirements = new BattleRequirements(SFSObject.newFromJsonData(requirementsString));
         }
      }

      ISFSObject extraObj = SFSObject.newFromJsonData(campaignData.getUtfString("extra"));
      if (extraObj.containsKey("anim_file")) {
         this.data.putUtfString("anim_file", extraObj.getUtfString("anim_file"));
      }

      if (extraObj.containsKey("anim")) {
         this.data.putUtfString("anim", extraObj.getUtfString("anim"));
      }

      if (extraObj.containsKey("bg_anim_file")) {
         this.data.putUtfString("bg_anim_file", extraObj.getUtfString("bg_anim_file"));
      }

      if (extraObj.containsKey("bg_anim")) {
         this.data.putUtfString("bg_anim", extraObj.getUtfString("bg_anim"));
      }

      if (extraObj.containsKey("costumeId")) {
         this.data.putInt("costumeId", extraObj.getInt("costumeId"));
      }

      if (campaignData.containsKey("max_training_level")) {
         this.data.putInt("max_training_level", campaignData.getInt("max_training_level"));
      }

      if (extraObj.containsKey("frame")) {
         this.data.putUtfString("frame", extraObj.getUtfString("frame"));
      }

      if (extraObj.containsKey("frame_sheet")) {
         this.data.putUtfString("frame_sheet", extraObj.getUtfString("frame_sheet"));
      }

      if (extraObj.containsKey("icon")) {
         this.data.putUtfString("icon", extraObj.getUtfString("icon"));
      }

      if (extraObj.containsKey("icon_sheet")) {
         this.data.putUtfString("icon_sheet", extraObj.getUtfString("icon_sheet"));
      }

      if (extraObj.containsKey("display_id")) {
         this.data.putUtfString("display_id", extraObj.getUtfString("display_id"));
      }

      if (extraObj.containsKey("pvp_default_loadout")) {
         this.data.putSFSArray("pvp_default_loadout", extraObj.getSFSArray("pvp_default_loadout"));
      }

      if (extraObj.containsKey("silent")) {
         this.data.putInt("silent", extraObj.getInt("silent"));
      }

   }

   public int getId() {
      return this.data.getInt("id");
   }

   public String getName() {
      return this.data.getUtfString("name");
   }

   public boolean isPVP() {
      if (this.data.containsKey("pvp")) {
         return this.data.getInt("pvp") == 1;
      } else {
         return false;
      }
   }

   public int getDepends() {
      return this.data.containsKey("depends") ? this.data.getInt("depends") : -1;
   }

   public long getRewardTime() {
      return this.data.containsKey("reward_buyout_time") ? (long)this.data.getInt("reward_buyout_time") : 0L;
   }

   public int getRewardCost() {
      return this.data.containsKey("reward_buyout_cost") ? this.data.getInt("reward_buyout_cost") : 1000;
   }

   public List<BattleCampaignEventData> getBattles() {
      return this.battles;
   }

   public BattleCampaignEventData getBattle(int battleId) {
      return battleId >= 0 && battleId < this.battles.size() ? (BattleCampaignEventData)this.battles.get(battleId) : null;
   }

   public List<BattleVersusTierData> getTiers() {
      return this.tiers;
   }

   public BattleVersusTierData getTier(int tier) {
      return tier >= 0 && tier < this.tiers.size() ? (BattleVersusTierData)this.tiers.get(tier) : null;
   }

   public BattleRequirements getRequirements() {
      return this.requirements;
   }

   public BattleRewardData getReward() {
      return this.reward;
   }

   public ISFSArray getDefaultPvpLoadout() {
      return this.data.containsKey("pvp_default_loadout") ? this.data.getSFSArray("pvp_default_loadout") : null;
   }

   public ISFSObject getData() {
      ISFSObject campaignData = new SFSObject();
      campaignData.putInt("id", this.data.getInt("id"));
      campaignData.putUtfString("name", this.data.getUtfString("name"));
      campaignData.putLong("last_changed", this.data.getLong("last_changed"));
      campaignData.putInt("depends", this.data.getInt("depends"));
      if (this.data.containsKey("schedule_id")) {
         int scheduleId = this.data.getInt("schedule_id");
         if (scheduleId > 0) {
            Schedule schedule = ScheduleLookup.schedule(scheduleId);
            campaignData.putSFSObject("schedule", schedule.getSFSObject());
            if (this.data.containsKey("reward_buyout_time")) {
               campaignData.putInt("reward_buyout_time", this.data.getInt("reward_buyout_time"));
            }

            if (this.data.containsKey("reward_buyout_cost")) {
               campaignData.putInt("reward_buyout_cost", this.data.getInt("reward_buyout_cost"));
            }
         }
      }

      if (this.data.containsKey("pvp")) {
         campaignData.putInt("pvp", this.data.getInt("pvp"));
      }

      SFSArray sfsTiers;
      Iterator i;
      if (this.isPVP()) {
         sfsTiers = new SFSArray();
         i = this.tiers.iterator();

         while(i.hasNext()) {
            BattleVersusTierData tier = (BattleVersusTierData)i.next();
            sfsTiers.addSFSObject(tier.data);
         }

         campaignData.putSFSArray("tiers", sfsTiers);
      } else {
         sfsTiers = new SFSArray();
         i = this.battles.iterator();

         while(i.hasNext()) {
            BattleCampaignEventData battle = (BattleCampaignEventData)i.next();
            sfsTiers.addSFSObject(battle.data);
         }

         campaignData.putSFSArray("battles", sfsTiers);
      }

      if (this.reward != null) {
         campaignData.putSFSObject("reward", this.reward.data);
      }

      if (this.requirements != null) {
         campaignData.putSFSObject("requirements", this.requirements.data());
      }

      if (this.data.containsKey("anim_file")) {
         campaignData.putUtfString("anim_file", this.data.getUtfString("anim_file"));
      }

      if (this.data.containsKey("anim")) {
         campaignData.putUtfString("anim", this.data.getUtfString("anim"));
      }

      if (this.data.containsKey("bg_anim_file")) {
         campaignData.putUtfString("bg_anim_file", this.data.getUtfString("bg_anim_file"));
      }

      if (this.data.containsKey("bg_anim")) {
         campaignData.putUtfString("bg_anim", this.data.getUtfString("bg_anim"));
      }

      if (this.data.containsKey("costumeId")) {
         campaignData.putInt("costumeId", this.data.getInt("costumeId"));
      }

      if (this.data.containsKey("max_training_level")) {
         campaignData.putInt("max_training_level", this.data.getInt("max_training_level"));
      }

      if (this.data.containsKey("frame")) {
         campaignData.putUtfString("frame", this.data.getUtfString("frame"));
      }

      if (this.data.containsKey("frame_sheet")) {
         campaignData.putUtfString("frame_sheet", this.data.getUtfString("frame_sheet"));
      }

      if (this.data.containsKey("icon")) {
         campaignData.putUtfString("icon", this.data.getUtfString("icon"));
      }

      if (this.data.containsKey("icon_sheet")) {
         campaignData.putUtfString("icon_sheet", this.data.getUtfString("icon_sheet"));
      }

      if (this.data.containsKey("display_id")) {
         campaignData.putUtfString("display_id", this.data.getUtfString("display_id"));
      }

      if (this.data.containsKey("silent")) {
         campaignData.putInt("silent", this.data.getInt("silent"));
      }

      return campaignData;
   }

   public int getScheduleID() {
      return this.data.getInt("schedule_id");
   }

   public int getMaxTrainingLevel() {
      return this.data.containsKey("max_training_level") ? this.data.getInt("max_training_level") : 0;
   }

   public int getPool() {
      return this.data.containsKey("pool_id") ? this.data.getInt("pool_id") : 0;
   }

   public boolean shouldAlwaysUpdate() {
      return this.getScheduleID() > 0;
   }
}
