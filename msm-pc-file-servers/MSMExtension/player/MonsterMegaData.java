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

public class MonsterMegaData {
   public static final String STARTED_AT_KEY = "started_at";
   public static final String FINISHED_AT_KEY = "finished_at";
   public static final String PERMAMEGA_ENABLED_KEY = "permamega";
   public static final String CURRENTLY_MEGA_KEY = "currently_mega";
   protected long startedAt = 0L;
   protected long finishedAt = 0L;
   protected boolean permamega = false;
   protected boolean currentlyMega = true;

   MonsterMegaData(MSMExtension ext, boolean permanent) {
      this.permamega = permanent;
      this.currentlyMega = true;
      if (!this.permamega) {
         this.resetTempMegaTime(ext);
      }

   }

   public MonsterMegaData(ISFSObject megaData) {
      this.initFromSFSObject(megaData);
   }

   public void initFromSFSObject(ISFSObject megaData) {
      try {
         Iterator iterator = megaData.iterator();

         while(iterator.hasNext()) {
            Entry<String, SFSDataWrapper> e = (Entry)iterator.next();
            if (((String)e.getKey()).equals("started_at")) {
               this.startedAt = SFSHelpers.getLong((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("finished_at")) {
               this.finishedAt = SFSHelpers.getLong((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("permamega")) {
               this.permamega = SFSHelpers.getBool((SFSDataWrapper)e.getValue());
            } else {
               if (!((String)e.getKey()).equals("currently_mega")) {
                  throw new ClassCastException("Invalid SFSObject key " + (String)e.getKey());
               }

               this.currentlyMega = SFSHelpers.getBool((SFSDataWrapper)e.getValue());
            }
         }
      } catch (ClassCastException var4) {
         Logger.trace((Exception)var4, "Player torch initialization error");
      }

   }

   public ISFSObject toSFSObject() {
      ISFSObject s = new SFSObject();
      s.putBool("permamega", this.permamega);
      s.putBool("currently_mega", this.currentlyMega);
      if (!this.permamega) {
         s.putLong("started_at", this.startedAt);
         s.putLong("finished_at", this.finishedAt);
      }

      return s;
   }

   public boolean isPermaMega() {
      return this.permamega;
   }

   public void resetMega(MSMExtension ext, boolean permanent) {
      if (!this.permamega) {
         this.permamega = permanent;
         this.currentlyMega = true;
         if (!this.permamega) {
            this.resetTempMegaTime(ext);
         }

      }
   }

   private void resetTempMegaTime(MSMExtension ext) {
      this.startedAt = MSMExtension.CurrentDBTime();
      long defaultDurationOfMegaSec = GameSettings.get("DURATION_MEGAMONSTER_SEC", 86400L);
      this.finishedAt = this.startedAt + defaultDurationOfMegaSec * 1000L;
   }

   public boolean megaExpired(long curTime) {
      return !this.permamega && this.finishedAt < curTime;
   }

   public boolean enableMega(boolean e) {
      this.currentlyMega = e;
      return true;
   }

   public ISFSObject getData() {
      return this.toSFSObject();
   }

   public String toString() {
      return this.toSFSObject().getDump();
   }
}
