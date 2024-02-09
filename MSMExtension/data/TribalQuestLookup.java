package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;

public class TribalQuestLookup {
   private static ISFSArray questLookup;

   public static void init(IDbWrapper db) throws Exception {
      String sql = "SELECT * FROM tribal_quests";
      ISFSArray results = db.query("SELECT * FROM tribal_quests");
      init((ISFSArray)results);
   }

   public static void init(ISFSArray quests) {
      Iterator i = quests.iterator();

      while(i.hasNext()) {
         SFSObject quest = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         quest.putSFSArray("goals", SFSArray.newFromJsonData(quest.getUtfString("goals")));
         ISFSArray rewardsArray = SFSArray.newFromJsonData(quest.getUtfString("rewards"));
         if (rewardsArray.size() > 0) {
            quest.putSFSObject("rewards", rewardsArray.getSFSObject(0));
         } else {
            quest.putSFSObject("rewards", new SFSObject());
         }
      }

      questLookup = quests;
   }

   public static ISFSArray getValues() {
      return questLookup;
   }
}
