package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Iterator;

public class MonsterCommonToRareMapping {
   static MonsterMapping<MonsterRareData> mapping;

   public static SFSArray sfsData() {
      return mapping.sfsData();
   }

   public static void init(IDbWrapper db) throws Exception {
      mapping = new MonsterMapping();
      mapping.init();
      String sql = "SELECT * FROM monsters_rare_mapping";
      ISFSArray results = db.query(sql);
      Iterator i = results.iterator();

      while(i.hasNext()) {
         SFSObject mappingData = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         int commonMonsterId = mappingData.getInt("common_monster_id");
         int rareMonsterId = mappingData.getInt("rare_monster_id");
         double probability = mappingData.getDouble("probability");
         MonsterRareData rareDat = new MonsterRareData(commonMonsterId, rareMonsterId, (float)probability);
         add(commonMonsterId, rareDat);
      }

   }

   public static void add(Integer commonMonsterId, MonsterRareData rareMonsterData) {
      mapping.add(commonMonsterId, rareMonsterData);
      SFSObject rareData = new SFSObject();
      rareData.putInt("rare_id", rareMonsterData.rareMonsterId());
      rareData.putInt("common_id", commonMonsterId);
      mapping.addSFSObjectToCache(rareData);
   }

   public static boolean isRare(int rareId) {
      return mapping.isType2(rareId);
   }

   public static ArrayList<MonsterRareData> commonToRareMapGet(int id) {
      return mapping.type1ToType2MapGet(id);
   }

   public static boolean rareToCommonMapContainsKey(int id) {
      return mapping.type2ToType1MapContainsKey(id);
   }

   public static MonsterRareData rareToCommonMapGet(int id) {
      return (MonsterRareData)mapping.type2ToType1MapGet(id);
   }
}
