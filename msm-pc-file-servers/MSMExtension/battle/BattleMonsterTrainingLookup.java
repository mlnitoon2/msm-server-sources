package com.bigbluebubble.mysingingmonsters.battle;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticDataLookup;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BattleMonsterTrainingLookup extends StaticDataLookup<BattleMonsterTrainingData> {
   public static BattleMonsterTrainingLookup instance;
   private Map<Integer, BattleMonsterTrainingData> trainingData_ = new HashMap();
   static final String CACHE_NAME = "battle_monster_training_data";

   private BattleMonsterTrainingLookup(IDbWrapper db) throws Exception {
      this.lastChanged_ = 0L;
      String sql = "SELECT * FROM battle_monster_training";
      ISFSArray results = db.query(sql);

      BattleMonsterTrainingData entry;
      for(Iterator i = results.iterator(); i.hasNext(); this.lastChanged_ = Math.max(this.lastChanged_, entry.lastChanged())) {
         ISFSObject rowData = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
         int monsterId = rowData.getInt("monster_id");
         entry = null;
         if (!this.trainingData_.containsKey(monsterId)) {
            entry = new BattleMonsterTrainingData((long)monsterId);
            this.trainingData_.put(monsterId, entry);
         } else {
            entry = (BattleMonsterTrainingData)this.trainingData_.get(monsterId);
         }

         entry.addRowData(rowData);
      }

   }

   public static void init(IDbWrapper db) throws Exception {
      instance = new BattleMonsterTrainingLookup(db);
   }

   public static int GetTrainingCostForMonster(int monsterId, int monsterLevel) {
      BattleMonsterTrainingData monsterTrainingData = instance.getEntry(monsterId);
      return monsterTrainingData.getCostForLevel(monsterLevel);
   }

   public static int GetTrainingDurationForMonster(int monsterId, int monsterLevel) {
      BattleMonsterTrainingData monsterTrainingData = instance.getEntry(monsterId);
      return monsterTrainingData.getDurationForLevel(monsterLevel);
   }

   public String getCacheName() {
      return "battle_monster_training_data";
   }

   public Iterable<BattleMonsterTrainingData> entries() {
      return this.trainingData_.values();
   }

   public BattleMonsterTrainingData getEntry(int id) {
      return (BattleMonsterTrainingData)this.trainingData_.get(id);
   }
}
