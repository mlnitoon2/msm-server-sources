package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Iterator;

public class MonsterCommonToEpicMapping {
   static MonsterMapping<MonsterEpicData> mapping;

   public static SFSArray sfsData() {
      return mapping.sfsData();
   }

   public static void init(IDbWrapper db) throws Exception {
      mapping = new MonsterMapping();
      mapping.init();
      String sql = "SELECT * FROM monsters_epic_mapping";
      ISFSArray results = db.query("SELECT * FROM monsters_epic_mapping");
      Iterator i = results.iterator();

      while(i.hasNext()) {
         SFSObject mappingData = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         int commonMonsterId = mappingData.getInt("common_monster_id");
         int epicMonsterId = mappingData.getInt("epic_monster_id");
         MonsterEpicData epicDat = new MonsterEpicData(commonMonsterId, epicMonsterId);
         add(commonMonsterId, epicDat);
      }

   }

   public static void add(Integer commonMonsterId, MonsterEpicData epicMonsterData) {
      mapping.add(commonMonsterId, epicMonsterData);
      SFSObject epicData = new SFSObject();
      epicData.putInt("epic_id", epicMonsterData.epicMonsterId());
      epicData.putInt("common_id", commonMonsterId);
      mapping.addSFSObjectToCache(epicData);
   }

   public static boolean isEpic(int epicId) {
      return mapping.isType2(epicId);
   }

   public static ArrayList<MonsterEpicData> commonToEpicMapGet(int id) {
      return mapping.type1ToType2MapGet(id);
   }

   public static boolean epicToCommonMapContainsKey(int id) {
      return mapping.type2ToType1MapContainsKey(id);
   }

   public static MonsterEpicData epicToCommonMapGet(int id) {
      return (MonsterEpicData)mapping.type2ToType1MapGet(id);
   }
}
