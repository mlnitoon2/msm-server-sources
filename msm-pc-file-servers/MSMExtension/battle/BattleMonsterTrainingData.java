package com.bigbluebubble.mysingingmonsters.battle;

import com.bigbluebubble.mysingingmonsters.staticdata.StaticData;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.HashMap;
import java.util.Map;

public class BattleMonsterTrainingData extends StaticData {
   ISFSArray levels;
   protected Map<Integer, ISFSObject> levelMap;

   public BattleMonsterTrainingData(long monsterId) {
      super(new SFSObject());
      this.data.putLong("monster_id", monsterId);
      this.data.putLong("last_changed", 0L);
      this.levels = new SFSArray();
      this.data.putSFSArray("levels", this.levels);
      this.levelMap = new HashMap();
   }

   public void addRowData(ISFSObject rowData) {
      this.levels.addSFSObject(rowData);
      long lastChanged = this.data.getLong("last_changed");
      long rowLastChanged = rowData.getLong("last_changed");
      if (rowLastChanged > lastChanged) {
         this.data.putLong("last_changed", rowLastChanged);
      }

      int level = rowData.getInt("level");
      this.levelMap.put(level, rowData);
   }

   public int getCostForLevel(int monsterLevel) {
      ISFSObject levelData = (ISFSObject)this.levelMap.get(monsterLevel);
      return levelData.getInt("training_cost");
   }

   public int getDurationForLevel(int monsterLevel) {
      ISFSObject levelData = (ISFSObject)this.levelMap.get(monsterLevel);
      return levelData.getInt("training_time");
   }
}
