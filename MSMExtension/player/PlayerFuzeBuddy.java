package com.bigbluebubble.mysingingmonsters.player;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.data.Structure;
import com.bigbluebubble.mysingingmonsters.data.StructureLookup;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;
import java.util.Map.Entry;

public class PlayerFuzeBuddy {
   public static final String STRUCTURE_KEY = "structure_id";
   public static final String COLOR_R_KEY = "colorR";
   public static final String COLOR_Y_KEY = "colorY";
   public static final String COLOR_B_KEY = "colorB";
   public static final String STARTED_ON_KEY = "started_on";
   public static final String FINISHED_ON_KEY = "finished_on";
   public static final String CREATE_KEY = "create";
   public static Structure BuddyStructure = null;
   protected long structure;
   protected float colorR;
   protected float colorY;
   protected float colorB;
   protected long started_on;
   protected long finished_on;
   protected boolean create;

   public PlayerFuzeBuddy(ISFSObject componentData) {
      this.initFromSFSObject(componentData);
   }

   public static void initStaticData() {
      if (BuddyStructure == null) {
         Iterator var0 = StructureLookup.getInstance().entries().iterator();

         while(var0.hasNext()) {
            Structure structure = (Structure)var0.next();
            if (structure.isBuddy()) {
               BuddyStructure = structure;
               break;
            }
         }
      }

   }

   public void initFromSFSObject(ISFSObject componentData) {
      try {
         Iterator iterator = componentData.iterator();

         while(iterator.hasNext()) {
            Entry<String, SFSDataWrapper> e = (Entry)iterator.next();
            if (((String)e.getKey()).equals("structure_id")) {
               this.structure = SFSHelpers.getLong((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("colorR")) {
               this.colorR = (float)SFSHelpers.getDouble((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("colorY")) {
               this.colorY = (float)SFSHelpers.getDouble((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("colorB")) {
               this.colorB = (float)SFSHelpers.getDouble((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("started_on")) {
               this.started_on = SFSHelpers.getLong((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("finished_on")) {
               this.finished_on = SFSHelpers.getLong((SFSDataWrapper)e.getValue());
            } else {
               if (!((String)e.getKey()).equals("create")) {
                  throw new ClassCastException("Invalid SFSObject key " + (String)e.getKey());
               }

               this.create = SFSHelpers.getBool((SFSDataWrapper)e.getValue());
            }
         }
      } catch (ClassCastException var4) {
         Logger.trace((Exception)var4, "Player Fuzer Buddy initialization error");
      }

   }

   public ISFSObject getData() {
      return this.toSFSObject();
   }

   public ISFSObject toSFSObject() {
      ISFSObject s = new SFSObject();
      s.putLong("structure_id", this.getStructureID());
      s.putDouble("colorR", (double)this.colorR);
      s.putDouble("colorY", (double)this.colorY);
      s.putDouble("colorB", (double)this.colorB);
      s.putLong("started_on", this.started_on);
      s.putLong("finished_on", this.finished_on);
      s.putBool("create", this.create);
      return s;
   }

   public String toString() {
      return this.toSFSObject().getDump();
   }

   public long getStructureID() {
      return this.structure;
   }

   public long getTimeRemaining() {
      return this.finished_on <= 0L ? 0L : Math.max(this.finished_on - MSMExtension.CurrentDBTime(), 0L);
   }

   public long getInitialTimeRemaining() {
      return Math.max(this.finished_on - this.started_on, 0L);
   }

   public void finishNow() {
      this.finished_on = MSMExtension.CurrentDBTime();
   }

   public long getCompletionTime() {
      return this.finished_on;
   }

   public long getStartTime() {
      return this.started_on;
   }

   public boolean getCreate() {
      return this.create;
   }

   public float getR() {
      return this.colorR;
   }

   public float getY() {
      return this.colorY;
   }

   public float getB() {
      return this.colorB;
   }

   public void reduceFuzingTimeByVideo() {
      long reduceTimeAmount = GameSettings.getLong("USER_SPEED_UP_DURATION") * 60L * 1000L;
      this.finished_on -= reduceTimeAmount;
      this.started_on -= reduceTimeAmount;
   }
}
