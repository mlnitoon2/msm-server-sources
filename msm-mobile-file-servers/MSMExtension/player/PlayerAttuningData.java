package com.bigbluebubble.mysingingmonsters.player;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PlayerAttuningData {
   public static final String STRUCTURE_ID_KEY = "structure";
   public static final String STARTED_ON_KEY = "started_on";
   public static final String COMPLETE_ON_KEY = "complete_on";
   public static final String START_GENE_KEY = "start_gene";
   public static final String END_GENE_KEY = "end_gene";
   private static final Set<String> validKeys = new HashSet(Arrays.asList("structure", "started_on", "complete_on", "start_gene", "end_gene"));
   protected long structure;
   protected long startedOn = 0L;
   protected long completeOn = 0L;
   protected String startGene = "";
   protected String endGene = "";
   protected boolean isAttuning = false;
   public static final char[] validGenes = new char[]{'G', 'J', 'K', 'L', 'M'};

   public PlayerAttuningData(ISFSObject attuningData) throws PlayerLoadingException {
      this.initFromSFSObject(attuningData);
   }

   public void initFromSFSObject(ISFSObject data) throws PlayerLoadingException {
      try {
         this.structure = SFSHelpers.getLong(data.get("structure"));
         this.startedOn = SFSHelpers.getLong(data.get("started_on"));
         this.completeOn = SFSHelpers.getLong(data.get("complete_on"));
         this.startGene = SFSHelpers.getUtfString(data.get("start_gene"));
         this.endGene = SFSHelpers.getUtfString(data.get("end_gene"));
         PlayerDataValidation.validateKeys(data, validKeys);
      } catch (Exception var3) {
         throw new PlayerLoadingException(var3, "PlayerAttuningData initialization error");
      }
   }

   public ISFSObject getData() {
      return this.toSFSObject();
   }

   public ISFSObject toSFSObject() {
      ISFSObject s = new SFSObject();
      s.putLong("structure", this.getStructureID());
      s.putLong("started_on", this.startedOn);
      s.putLong("complete_on", this.completeOn);
      s.putUtfString("start_gene", this.startGene);
      s.putUtfString("end_gene", this.endGene);
      return s;
   }

   public String toString() {
      return this.toSFSObject().getDump();
   }

   public long getStructureID() {
      return this.structure;
   }

   public long getTimeRemaining() {
      long timeNow = MSMExtension.CurrentDBTime();
      if (this.completeOn <= 0L) {
         return 0L;
      } else {
         long timeLeft = this.completeOn - timeNow;
         return Math.max(timeLeft, 0L);
      }
   }

   public long getInitialTimeRemaining() {
      return Math.max(this.completeOn - this.startedOn, 0L);
   }

   public void finishAttuningNow() {
      this.completeOn = MSMExtension.CurrentDBTime();
   }

   public long getCompletionTime() {
      return this.completeOn;
   }

   public long getStartTime() {
      return this.startedOn;
   }

   public String endGene() {
      return this.endGene;
   }

   public static boolean isValidGene(String gene) {
      for(int i = 0; i < validGenes.length; ++i) {
         if (gene.equals(String.valueOf(validGenes[i]))) {
            return true;
         }
      }

      return false;
   }

   public void reduceAttuningTimeByVideo() {
      long reduceTimeAmount = (long)GameSettings.getInt("USER_SPEED_UP_DURATION") * 60L * 1000L;
      this.startedOn -= reduceTimeAmount;
      this.completeOn -= reduceTimeAmount;
   }

   public char convertIslandToGene(long islandId) {
      return validGenes[(int)islandId - 1];
   }

   public PlayerAttuningData(long structureId, String startGene, String endGene, long startOn, long endOn) {
      this.structure = structureId;
      this.startedOn = startOn;
      this.completeOn = endOn;
      this.startGene = startGene;
      this.endGene = endGene;
   }
}
