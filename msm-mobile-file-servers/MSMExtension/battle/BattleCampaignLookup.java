package com.bigbluebubble.mysingingmonsters.battle;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.schedules.Schedule;
import com.bigbluebubble.mysingingmonsters.schedules.ScheduleLookup;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticDataLookup;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BattleCampaignLookup extends StaticDataLookup<BattleCampaignData> {
   public static BattleCampaignLookup instance;
   private Map<Integer, BattleCampaignData> campaigns_ = new HashMap();
   static final String CACHE_NAME = "battle_campaign_data";

   private BattleCampaignLookup(IDbWrapper db) throws Exception {
      String sql = "SELECT * from battle_campaign";
      ISFSArray results = db.query(sql);
      Iterator i = results.iterator();

      while(i.hasNext()) {
         BattleCampaignData campaignData = new BattleCampaignData((ISFSObject)((SFSDataWrapper)i.next()).getObject());
         this.campaigns_.put(campaignData.getId(), campaignData);
      }

   }

   public static void init(IDbWrapper db) throws Exception {
      instance = new BattleCampaignLookup(db);
      validate();
   }

   public static BattleCampaignData get(int id) {
      return instance.getEntry(id);
   }

   public String getCacheName() {
      return "battle_campaign_data";
   }

   public Iterable<BattleCampaignData> entries() {
      return this.campaigns_.values();
   }

   public BattleCampaignData getEntry(int id) {
      return (BattleCampaignData)this.campaigns_.get(id);
   }

   public static BattleCampaignData getCurrentPvpSeason() {
      Iterator it = instance.entries().iterator();

      while(it.hasNext()) {
         BattleCampaignData obj = (BattleCampaignData)it.next();
         if (obj != null && obj.isPVP()) {
            Schedule s = ScheduleLookup.schedule(obj.getScheduleID());
            if (s != null && s.isActive() && s.timeRemaining() > 0L) {
               return obj;
            }
         }
      }

      return null;
   }

   private static void validate() {
      int activePvpCampaign = 0;
      Iterator it = instance.entries().iterator();

      while(it.hasNext()) {
         BattleCampaignData campaignData = (BattleCampaignData)it.next();
         if (campaignData.isPVP()) {
            Schedule s = ScheduleLookup.schedule(campaignData.getScheduleID());
            if (s == null) {
               Logger.trace("### Error! Found PVP Campaign with no schedule! campaignId = " + campaignData.getId());
            }

            if (s.isActive() && s.timeRemaining() > 0L) {
               if (activePvpCampaign != 0) {
                  Logger.trace("### Error! Multiple PVP Campaigns active! " + activePvpCampaign + " & " + campaignData.getId());
               } else {
                  activePvpCampaign = campaignData.getId();
               }
            }
         }
      }

   }
}
