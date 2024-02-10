package com.bigbluebubble.mysingingmonsters.player;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.costumes.MonsterCostumeState;
import com.bigbluebubble.mysingingmonsters.data.MonsterLookup;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PlayerBreeding {
   public static final String ID_KEY = "user_breeding_id";
   public static final String PARENT_ID_KEY = "island";
   public static final String STRUCTURE_ID_KEY = "structure";
   public static final String MONSTER_1_KEY = "monster_1";
   public static final String MONSTER_2_KEY = "monster_2";
   public static final String NEW_MONSTER_KEY = "new_monster";
   public static final String STARTED_ON_KEY = "started_on";
   public static final String COMPLETE_ON_KEY = "complete_on";
   private static final Set<String> validKeys = new HashSet(Arrays.asList("user_breeding_id", "island", "structure", "monster_1", "monster_2", "new_monster", "started_on", "complete_on", "costume"));
   protected long user_breeding_id;
   protected long island;
   protected long structure;
   protected int monster_1;
   protected int monster_2;
   protected int new_monster;
   protected long started_on;
   protected long complete_on;
   protected ISFSObject costume = null;

   public PlayerBreeding(ISFSObject breedingData) throws PlayerLoadingException {
      this.initFromSFSObject(breedingData);
   }

   public void initFromSFSObject(ISFSObject data) throws PlayerLoadingException {
      try {
         this.user_breeding_id = SFSHelpers.getLong(data.get("user_breeding_id"));
         this.island = SFSHelpers.getLong(data.get("island"));
         this.structure = SFSHelpers.getLong(data.get("structure"));
         this.monster_1 = SFSHelpers.getInt(data.get("monster_1"));
         this.monster_2 = SFSHelpers.getInt(data.get("monster_2"));
         this.new_monster = SFSHelpers.getInt(data.get("new_monster"));
         this.started_on = SFSHelpers.getLong(data.get("started_on"));
         this.complete_on = SFSHelpers.getLong(data.get("complete_on"));
         if (data.containsKey("costume")) {
            this.costume = data.getSFSObject("costume");
         }

         PlayerDataValidation.validateKeys(data, validKeys);
      } catch (Exception var3) {
         throw new PlayerLoadingException(var3, "PlayerBreeding initialization error");
      }
   }

   public ISFSObject getData() {
      return this.toSFSObject();
   }

   public ISFSObject toSFSObject() {
      ISFSObject s = new SFSObject();
      s.putLong("island", this.getParentID());
      s.putLong("user_breeding_id", this.getID());
      s.putLong("structure", this.getStructureID());
      s.putInt("monster_1", this.monster_1);
      s.putInt("monster_2", this.monster_2);
      s.putInt("new_monster", this.new_monster);
      s.putLong("started_on", this.started_on);
      s.putLong("complete_on", this.complete_on);
      if (this.costume != null) {
         s.putSFSObject("costume", this.costume);
      }

      return s;
   }

   public String toString() {
      return this.toSFSObject().getDump();
   }

   public long getID() {
      return this.user_breeding_id;
   }

   public int getChildType() {
      return this.new_monster;
   }

   private long getParentID() {
      return this.island;
   }

   public long getStructureID() {
      return this.structure;
   }

   public void setID(long id) {
      this.user_breeding_id = id;
   }

   public long getTimeRemaining() {
      long timeNow = MSMExtension.CurrentDBTime();
      if (this.complete_on <= 0L) {
         return 0L;
      } else {
         long timeLeft = this.complete_on - timeNow;
         return Math.max(timeLeft, 0L);
      }
   }

   public long getInitialTimeRemaining() {
      return Math.max(this.complete_on - this.started_on, 0L);
   }

   public void finishBreedingNow() {
      this.complete_on = MSMExtension.CurrentDBTime();
   }

   public long getCompletionTime() {
      return this.complete_on;
   }

   public long getStartTime() {
      return this.started_on;
   }

   public void reduceBreedingByFacebookHelpTime() {
      float timeToBreedMs = (float)MonsterLookup.get(this.getChildType()).getBuildTimeMs();
      long hatchTimeReduction = (long)(timeToBreedMs * GameSettings.getFloat("FACEBOOK_HELP_INSTANCE_PERCENT"));
      this.started_on -= hatchTimeReduction;
      this.complete_on -= hatchTimeReduction;
   }

   public void reduceBreedingTimeByVideo() {
      long reduceTimeAmount = (long)GameSettings.getInt("USER_SPEED_UP_DURATION") * 60L * 1000L;
      this.started_on -= reduceTimeAmount;
      this.complete_on -= reduceTimeAmount;
   }

   public ISFSObject getCostumeData() {
      return this.costume;
   }

   public void setCostumeData(MonsterCostumeState monsterCostumeState) {
      if (monsterCostumeState != null) {
         this.costume = monsterCostumeState.toSFSObject();
      } else {
         this.costume = null;
      }

   }
}
