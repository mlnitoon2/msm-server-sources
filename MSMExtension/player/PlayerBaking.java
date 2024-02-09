package com.bigbluebubble.mysingingmonsters.player;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;
import java.util.Map.Entry;

public class PlayerBaking {
   public static final String ID_KEY = "user_baking_id";
   public static final String STRUCTURE_ID_KEY = "user_structure";
   public static final String FOOD_OPTION_ID_KEY = "food_option_id";
   public static final String PARENT_ID_KEY = "island";
   public static final String FOOD_COUNT_KEY = "food_count";
   public static final String STARTED_AT_KEY = "started_at";
   public static final String FINISHED_AT_KEY = "finished_at";
   protected long user_baking_id;
   protected long user_structure;
   protected int food_option_id;
   protected long island;
   protected int food_count;
   protected long started_at;
   protected long finished_at;

   public PlayerBaking(ISFSObject breedingData) {
      this.initFromSFSObject(breedingData);
   }

   public void initFromSFSObject(ISFSObject componentData) {
      try {
         Iterator iterator = componentData.iterator();

         while(iterator.hasNext()) {
            Entry<String, SFSDataWrapper> e = (Entry)iterator.next();
            if (((String)e.getKey()).equals("user_baking_id")) {
               this.user_baking_id = SFSHelpers.getLong((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("user_structure")) {
               this.user_structure = SFSHelpers.getLong((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("food_option_id")) {
               this.food_option_id = SFSHelpers.getInt((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("island")) {
               this.island = (long)((int)SFSHelpers.getLong((SFSDataWrapper)e.getValue()));
            } else if (((String)e.getKey()).equals("food_count")) {
               this.food_count = SFSHelpers.getInt((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("started_at")) {
               this.started_at = SFSHelpers.getLong((SFSDataWrapper)e.getValue());
            } else {
               if (!((String)e.getKey()).equals("finished_at")) {
                  throw new ClassCastException("Invalid SFSObject key " + (String)e.getKey());
               }

               this.finished_at = SFSHelpers.getLong((SFSDataWrapper)e.getValue());
            }
         }
      } catch (ClassCastException var4) {
         Logger.trace((Exception)var4, "Player baking initialization error");
      }

   }

   public ISFSObject toSFSObject() {
      ISFSObject s = new SFSObject();
      s.putLong("user_baking_id", this.getID());
      s.putLong("island", this.getParentID());
      s.putLong("user_structure", this.user_structure);
      s.putInt("food_option_id", this.food_option_id);
      s.putInt("food_count", this.food_count);
      s.putLong("started_at", this.started_at);
      s.putLong("finished_at", this.finished_at);
      return s;
   }

   public ISFSObject getData() {
      return this.toSFSObject();
   }

   public String toString() {
      return this.toSFSObject().getDump();
   }

   public long getID() {
      return this.user_baking_id;
   }

   public long getStructureId() {
      return this.user_structure;
   }

   public long getParentID() {
      return this.island;
   }

   public void setParentID(long parentId) {
      this.island = parentId;
   }

   public long getTimeRemaining() {
      long timeNow = MSMExtension.CurrentDBTime();
      long timeLeft = this.finished_at - timeNow;
      return Math.max(timeLeft, 0L);
   }

   public long getInitialTimeRemaining() {
      return Math.max(this.finished_at - this.started_at, 0L);
   }

   public void finishBakingNow() {
      this.finished_at = MSMExtension.CurrentDBTime();
   }

   public long getCompletionTime() {
      return this.finished_at;
   }

   public long getStartTime() {
      return this.started_at;
   }

   public void reduceBakingTimeByVideo() {
      long reduceTimeAmount = (long)GameSettings.getInt("USER_SPEED_UP_DURATION") * 60L * 1000L;
      this.started_at -= reduceTimeAmount;
      this.finished_at -= reduceTimeAmount;
   }
}
