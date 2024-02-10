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

public class LitPlayerTorch {
   public static final String ID_KEY = "user_torch_id";
   public static final String STRUCTURE_ID_KEY = "user_structure";
   public static final String STARTED_AT_KEY = "started_at";
   public static final String FINISHED_AT_KEY = "finished_at";
   public static final String PERMALIT_KEY = "permalit";
   protected long userTorchId;
   protected long userStructure;
   protected long startedAt;
   protected long finishedAt;
   protected boolean permalit = false;

   public LitPlayerTorch(PlayerStructure torchStruct, PlayerIsland island, boolean permanentlylit) {
      this.userTorchId = island.getNextLitTorchId();
      this.userStructure = torchStruct.getID();
      this.permalit = permanentlylit;
      this.resetTorchTime();
   }

   public void resetTorchLight(boolean permanent) {
      if (!this.permalit) {
         this.permalit = permanent;
         this.resetTorchTime();
      }
   }

   private void resetTorchTime() {
      this.startedAt = MSMExtension.CurrentDBTime();
      long defaultDurationOfFlameSec = GameSettings.get("DURATION_TORCH_LIGHT_SEC", 86400L);
      this.finishedAt = this.startedAt + defaultDurationOfFlameSec * 1000L;
   }

   public LitPlayerTorch(ISFSObject torchData) {
      this.initFromSFSObject(torchData);
   }

   public void initFromSFSObject(ISFSObject torchData) {
      try {
         Iterator iterator = torchData.iterator();

         while(iterator.hasNext()) {
            Entry<String, SFSDataWrapper> e = (Entry)iterator.next();
            if (((String)e.getKey()).equals("user_torch_id")) {
               this.userTorchId = SFSHelpers.getLong((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("user_structure")) {
               this.userStructure = SFSHelpers.getLong((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("started_at")) {
               this.startedAt = SFSHelpers.getLong((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("finished_at")) {
               this.finishedAt = SFSHelpers.getLong((SFSDataWrapper)e.getValue());
            } else {
               if (!((String)e.getKey()).equals("permalit")) {
                  throw new ClassCastException("Invalid SFSObject key " + (String)e.getKey());
               }

               this.permalit = SFSHelpers.getBool((SFSDataWrapper)e.getValue());
            }
         }
      } catch (ClassCastException var4) {
         Logger.trace((Exception)var4, "Player torch initialization error");
      }

   }

   public ISFSObject toSFSObject() {
      ISFSObject s = new SFSObject();
      s.putLong("user_torch_id", this.userTorchId);
      s.putLong("user_structure", this.userStructure);
      s.putLong("started_at", this.startedAt);
      s.putLong("finished_at", this.finishedAt);
      s.putBool("permalit", this.permalit);
      return s;
   }

   public ISFSObject getData() {
      return this.toSFSObject();
   }

   public String toString() {
      return this.toSFSObject().getDump();
   }

   public long getID() {
      return this.userTorchId;
   }

   public long getStructureId() {
      return this.userStructure;
   }

   public boolean isPermalit() {
      return this.permalit;
   }

   public boolean torchExpired(long curTime) {
      return !this.permalit && this.finishedAt < curTime;
   }
}
