package com.bigbluebubble.mysingingmonsters.battle;

import com.bigbluebubble.BBBServer.GameSettings;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class BattlePlayerState {
   public static final String XP_KEY = "xp";
   public static final String LEVEL_KEY = "level";
   public static final String LOADOUT_KEY = "loadout";
   public static final String LOADOUT_VERSUS_KEY = "loadout_versus";
   public static final String MEDALS_KEY = "medals";
   public static final String MAX_TRAINING_LEVEL_KEY = "max_training_level";
   ISFSObject data_;
   BattleLoadout loadout_;
   BattleLoadout loadoutVersus_;

   public BattlePlayerState() {
      this.data_ = new SFSObject();
      this.data_.putInt("xp", 0);
      this.data_.putInt("level", 1);
      this.loadout_ = new BattleLoadout();
      this.loadoutVersus_ = new BattleLoadout();
      this.data_.putInt("medals", 0);
      this.data_.putInt("max_training_level", GameSettings.getInt("INITIAL_BATTLE_MAX_TRAINING_LEVEL"));
   }

   public BattlePlayerState(ISFSObject data) {
      this.data_ = data;

      String loadoutJsonData;
      try {
         loadoutJsonData = this.data_.getUtfString("loadout");
         this.loadout_ = new BattleLoadout(SFSObject.newFromJsonData(loadoutJsonData));
      } catch (Exception var4) {
         this.loadout_ = new BattleLoadout();
      }

      try {
         loadoutJsonData = this.data_.getUtfString("loadout_versus");
         this.loadoutVersus_ = new BattleLoadout(SFSObject.newFromJsonData(loadoutJsonData));
      } catch (Exception var3) {
         this.loadoutVersus_ = new BattleLoadout();
      }

      if (!this.data_.containsKey("max_training_level")) {
         data.putInt("max_training_level", GameSettings.getInt("INITIAL_BATTLE_MAX_TRAINING_LEVEL"));
      }

   }

   public ISFSObject toSFSObject() {
      this.data_.putUtfString("loadout", this.loadout_.toSFSObject().toJson());
      this.data_.putUtfString("loadout_versus", this.loadoutVersus_.toSFSObject().toJson());
      return this.data_;
   }

   public int getXp() {
      return this.data_.getInt("xp");
   }

   public int getLevel() {
      return this.data_.getInt("level");
   }

   public int getMedals() {
      return this.data_.getInt("medals");
   }

   public BattleLoadout getLoadout() {
      return this.loadout_;
   }

   public BattleLoadout getLoadoutVersus() {
      return this.loadoutVersus_;
   }

   public int getMaxTrainingLevel() {
      return this.data_.getInt("max_training_level");
   }

   public void setMaxTrainingLevel(int maxTrainingLevel) {
      this.data_.putInt("max_training_level", maxTrainingLevel);
   }

   public boolean rewardXp(int xp) {
      boolean levelUp = false;
      int currentLevel = this.getLevel();
      int currentXp = this.getXp();
      if (currentLevel < BattleLevelLookup.getMaxLevel()) {
         currentXp += xp;

         for(int neededXp = BattleLevelLookup.get(currentLevel + 1).getXp(); currentXp >= neededXp; neededXp = BattleLevelLookup.get(currentLevel + 1).getXp()) {
            currentXp -= neededXp;
            ++currentLevel;
            levelUp = true;
            if (currentLevel >= BattleLevelLookup.getMaxLevel()) {
               break;
            }
         }

         this.data_.putInt("xp", currentXp);
         this.data_.putInt("level", currentLevel);
      }

      return levelUp;
   }

   public void adjustMedals(int amount) {
      int medals = this.data_.getInt("medals");
      medals += amount;
      this.data_.putInt("medals", medals);
   }
}
