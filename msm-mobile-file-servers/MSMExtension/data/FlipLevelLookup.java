package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;

public class FlipLevelLookup {
   private static FlipLevelLookup instance;
   private static int FIRST_LEVEL = 1;
   private ArrayList<SFSObject> levels;
   private ArrayList<ISFSObject> unscaledEndgameRewards;
   private int maxLevel_ = 0;
   private ArrayList<Float> diamondLevelChances;
   private ArrayList<FlipSpecialLevel> diamondLevels;
   private boolean disableDiamondLevels = false;

   public static FlipLevelLookup getInstance() {
      return instance;
   }

   public int maxLevel() {
      return this.maxLevel_;
   }

   public float getChanceOfDiamondReward(int numDiamondsSinceReset) {
      return numDiamondsSinceReset >= this.diamondLevelChances.size() ? (Float)this.diamondLevelChances.get(this.diamondLevelChances.size() - 1) : (Float)this.diamondLevelChances.get(numDiamondsSinceReset);
   }

   public static void init(IDbWrapper db) throws Exception {
      instance = new FlipLevelLookup(db);
   }

   public FlipLevelLookup(IDbWrapper db) throws Exception {
      FIRST_LEVEL = GameSettings.get("FLIP_MINIGAME_DEBUG_LEVEL_START", 1);
      this.levels = new ArrayList();
      this.unscaledEndgameRewards = new ArrayList();
      this.diamondLevelChances = new ArrayList();
      this.diamondLevels = new ArrayList();
      String sql = "SELECT * FROM memory_flip_levels ORDER BY level";
      ISFSArray results = db.query("SELECT * FROM memory_flip_levels ORDER BY level");
      Iterator itr = results.iterator();

      int level;
      while(itr.hasNext()) {
         SFSObject levelData = (SFSObject)((SFSObject)((SFSDataWrapper)itr.next()).getObject());
         if (GameSettings.get("FLIP_MINIGAME_DEBUG_FORCE_SIMPLE_LVLS", 0) == 1) {
            levelData.putUtfString("shape", "2x1");
         }

         level = levelData.getInt("level");
         if (level > this.maxLevel_) {
            this.maxLevel_ = level;
         }

         this.levels.add(levelData);
         String unscaledPrizeStr = levelData.getUtfString("unscaled_prize");
         this.unscaledEndgameRewards.add(unscaledPrizeStr != null ? SFSObject.newFromJsonData(unscaledPrizeStr) : null);
      }

      this.disableDiamondLevels = GameSettings.get("FLIP_MINIGAME_DISABLE_DIAMOND_LVLS", 0) != 0;
      JSONArray lvlChances = new JSONArray(GameSettings.get("FLIP_MINIGAME_DIAMOND_LVL_CHANCES", "[1.0, 0.5, 0.25]"));

      for(level = 0; level < lvlChances.length(); ++level) {
         this.diamondLevelChances.add((float)lvlChances.getDouble(level));
      }

      this.initDiamondLevels();
   }

   public void initDiamondLevels() throws Exception {
      SFSArray diamondLevelDefs = SFSArray.newFromJsonData(GameSettings.get("FLIP_MINIGAME_DIAMOND_LVL_DEFS"));
      Iterator itr = diamondLevelDefs.iterator();

      while(itr.hasNext()) {
         SFSObject levelDef = (SFSObject)((SFSObject)((SFSDataWrapper)itr.next()).getObject());
         FlipSpecialLevel lvl = new FlipSpecialLevel(levelDef);
         this.diamondLevels.add(lvl);
      }

   }

   public int firstLevel() {
      return FIRST_LEVEL;
   }

   public SFSObject getStartLevel() {
      Iterator i = this.levels.iterator();

      SFSObject level;
      do {
         if (!i.hasNext()) {
            return null;
         }

         level = (SFSObject)i.next();
      } while(level.getInt("level") != FIRST_LEVEL);

      return level;
   }

   public SFSObject getLevelByLevel(int level) {
      Iterator i = this.levels.iterator();

      SFSObject levelSfs;
      do {
         if (!i.hasNext()) {
            return null;
         }

         levelSfs = (SFSObject)i.next();
      } while(levelSfs.getInt("level") != level);

      return levelSfs;
   }

   public SFSObject getLevelById(int id) {
      Iterator i = this.levels.iterator();

      SFSObject levelSfs;
      do {
         if (!i.hasNext()) {
            return null;
         }

         levelSfs = (SFSObject)i.next();
      } while(levelSfs.getInt("id") != id);

      return levelSfs;
   }

   public boolean disabledDiamondLevels() {
      return this.disableDiamondLevels;
   }

   public SFSObject getSpecialLevelId(int level) {
      Iterator itr = this.diamondLevels.iterator();

      while(true) {
         FlipSpecialLevel levelDef;
         do {
            if (!itr.hasNext()) {
               return null;
            }

            levelDef = (FlipSpecialLevel)itr.next();
         } while(!levelDef.appliesToLevel(level));

         int specialId = levelDef.id();
         Iterator i = this.levels.iterator();

         while(i.hasNext()) {
            SFSObject levelSfs = (SFSObject)i.next();
            if (levelSfs.getInt("id") == specialId) {
               return levelSfs;
            }
         }
      }
   }

   public ISFSObject unscaledEndgameRewardsByLevel(int level) {
      for(int i = 0; i < this.levels.size(); ++i) {
         if (((SFSObject)this.levels.get(i)).getInt("level") == level) {
            return (ISFSObject)this.unscaledEndgameRewards.get(i);
         }
      }

      return null;
   }

   public Iterator<SFSObject> iterator() {
      return this.levels.iterator();
   }
}
