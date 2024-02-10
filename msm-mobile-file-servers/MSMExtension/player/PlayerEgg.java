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

public class PlayerEgg {
   public static final String ID_KEY = "user_egg_id";
   public static final String PARENT_ID_KEY = "island";
   public static final String PARENT_STRUCT_KEY = "structure";
   public static final String TYPE_KEY = "monster";
   public static final String LAID_ON_KEY = "laid_on";
   public static final String HATCHES_ON_KEY = "hatches_on";
   public static final String PREV_NAME = "previous_name";
   public static final String PREV_PERMA_MEGA_DATA = "prev_permamega";
   private static final HashSet<String> validKeys = new HashSet(Arrays.asList("user_egg_id", "island", "structure", "monster", "laid_on", "hatches_on", "previous_name", "prev_permamega", "costume", "book_value", "boxed_eggs"));
   protected long user_egg_id;
   protected long island;
   protected long structure;
   protected int monster;
   protected long laid_on;
   protected long hatches_on;
   protected String prevName = "";
   protected ISFSObject prevPermaMega = null;
   protected MonsterCostumeState costumeState = null;
   protected String boxedEggs = null;
   protected int bookValue = -1;

   public PlayerEgg(ISFSObject componentData) throws PlayerLoadingException {
      this.initFromSFSObject(componentData);
   }

   public void initFromSFSObject(ISFSObject data) throws PlayerLoadingException {
      try {
         this.user_egg_id = SFSHelpers.getLong(data.get("user_egg_id"));
         this.island = SFSHelpers.getLong(data.get("island"));
         this.structure = SFSHelpers.getLong(data.get("structure"));
         if (data.containsKey("monster")) {
            this.monster = SFSHelpers.getInt(data.get("monster"));
         }

         if (data.containsKey("laid_on")) {
            this.laid_on = SFSHelpers.getLong(data.get("laid_on"));
         }

         if (data.containsKey("hatches_on")) {
            this.hatches_on = SFSHelpers.getLong(data.get("hatches_on"));
         }

         if (data.containsKey("previous_name")) {
            this.prevName = data.getUtfString("previous_name");
         }

         if (data.containsKey("prev_permamega")) {
            this.prevPermaMega = data.getSFSObject("prev_permamega");
         }

         if (data.containsKey("costume")) {
            this.costumeState = new MonsterCostumeState(data.getSFSObject("costume"));
         } else {
            this.costumeState = new MonsterCostumeState();
         }

         if (data.containsKey("book_value")) {
            this.bookValue = data.getInt("book_value");
         }

         if (data.containsKey("boxed_eggs")) {
            this.boxedEggs = data.getUtfString("boxed_eggs");
         }

         PlayerDataValidation.validateKeys(data, validKeys);
      } catch (PlayerLoadingException var3) {
         throw var3;
      } catch (Exception var4) {
         throw new PlayerLoadingException(var4, "PlayerEgg initialization error");
      }
   }

   public ISFSObject getData() {
      return this.toSFSObject();
   }

   public ISFSObject toSFSObject() {
      ISFSObject s = new SFSObject();
      s.putLong("island", this.getParentID());
      s.putLong("structure", this.getStructureID());
      s.putLong("user_egg_id", this.getID());
      s.putInt("monster", this.getType());
      s.putLong("laid_on", this.laid_on);
      s.putLong("hatches_on", this.hatches_on);
      if (this.prevName.length() > 0) {
         s.putUtfString("previous_name", this.prevName);
      }

      if (this.prevPermaMega != null) {
         s.putSFSObject("prev_permamega", this.prevPermaMega);
      }

      if (this.costumeState != null) {
         s.putSFSObject("costume", this.costumeState.toSFSObject());
      }

      if (this.bookValue != -1) {
         s.putInt("book_value", this.bookValue);
      }

      if (this.boxedEggs != null) {
         s.putUtfString("boxed_eggs", this.boxedEggs);
      }

      return s;
   }

   public String toString() {
      return this.toSFSObject().getDump();
   }

   public long getID() {
      return this.user_egg_id;
   }

   public int getType() {
      return this.monster;
   }

   public long getParentID() {
      return this.island;
   }

   public long getStructureID() {
      return this.structure;
   }

   public void setBookValue(int coins) {
      this.bookValue = coins;
   }

   public int bookValue() {
      return this.bookValue;
   }

   public String getPreviousName() {
      return this.prevName;
   }

   public ISFSObject getPrevPermaMega() {
      return this.prevPermaMega;
   }

   public long getTimeRemaining() {
      long timeNow = MSMExtension.CurrentDBTime();
      if (this.hatches_on <= 0L) {
         return 0L;
      } else {
         long timeLeft = this.hatches_on - timeNow;
         return Math.max(timeLeft, 0L);
      }
   }

   public long getInitialTimeRemaining() {
      return Math.max(this.hatches_on - this.laid_on, 0L);
   }

   public void finishHatchingNow() {
      this.hatches_on = MSMExtension.CurrentDBTime();
   }

   public void reduceHatchByFacebookHelpTime() {
      float timeToBuildMs = (float)MonsterLookup.get(this.getType()).getBuildTimeMs();
      long hatchTimeReduction = (long)(timeToBuildMs * GameSettings.getFloat("FACEBOOK_HELP_INSTANCE_PERCENT"));
      this.hatches_on -= hatchTimeReduction;
      this.laid_on -= hatchTimeReduction;
   }

   public long getCompletionTime() {
      return this.hatches_on;
   }

   public long getStartTime() {
      return this.laid_on;
   }

   public void reduceHatchingTimeByVideo() {
      long reduceTimeAmount = GameSettings.getLong("USER_SPEED_UP_DURATION") * 60L * 1000L;
      this.hatches_on -= reduceTimeAmount;
      this.laid_on -= reduceTimeAmount;
   }

   public ISFSObject getCostumeData() {
      return this.costumeState.toSFSObject();
   }

   public String getBoxedEggs() {
      return this.boxedEggs;
   }

   public void setCostumeData(ISFSObject costumeData) {
      this.costumeState = new MonsterCostumeState(costumeData);
   }

   public void setCostumeData(MonsterCostumeState monsterCostumeState) {
      this.costumeState = monsterCostumeState;
   }

   public boolean hasEquippedCostume() {
      return this.costumeState != null && this.costumeState.getEquipped() != 0;
   }

   public int equippedCostume() {
      return this.costumeState == null ? 0 : this.costumeState.getEquipped();
   }
}
