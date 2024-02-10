package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.mysingingmonsters.staticdata.StaticData;
import com.smartfoxserver.v2.entities.data.ISFSObject;

public class Level extends StaticData {
   public static final String LEVEL_KEY = "level";
   public static final String XP_KEY = "xp";
   public static final String TITLE_KEY = "title";
   public static final String MAX_BAKERIES = "max_bakeries";

   public Level(ISFSObject levelData) {
      super(levelData);
   }

   public int getLevel() {
      return this.data.getInt("level");
   }

   public int getXp() {
      return this.data.getInt("xp");
   }

   public String getTitle() {
      return this.data.getUtfString("title");
   }

   public int maxBakeries() {
      return this.data.getInt("max_bakeries");
   }
}
