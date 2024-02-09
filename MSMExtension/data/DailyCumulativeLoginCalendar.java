package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticData;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DailyCumulativeLoginCalendar extends StaticData {
   public static final String ID_KEY = "id";
   public static final String REWARDS_KEY = "rewards";
   public List<ISFSObject> rewardItems;

   public int getId() {
      return this.data.getInt("id");
   }

   protected DailyCumulativeLoginCalendar(ISFSObject data, IDbWrapper db) throws Exception {
      super(data);
      String SELECT_REWARDS_SQL = "SELECT * FROM daily_cumulative_login_rewards WHERE calendar_id = ? ORDER BY id ASC";
      ISFSArray results = db.query("SELECT * FROM daily_cumulative_login_rewards WHERE calendar_id = ? ORDER BY id ASC", new Object[]{this.getId()});
      data.putSFSArray("rewards", results);
      this.rewardItems = new ArrayList();
      Iterator i = results.iterator();

      while(i.hasNext()) {
         ISFSObject rewardObj = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
         this.rewardItems.add(rewardObj);
      }

   }

   public int getNumRewards() {
      return this.rewardItems.size();
   }

   public DailyCumulativeLoginReward getReward(int rewardId) {
      return new DailyCumulativeLoginReward((ISFSObject)this.rewardItems.get(rewardId));
   }
}
