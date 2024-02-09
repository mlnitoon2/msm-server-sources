package com.bigbluebubble.mysingingmonsters.player;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

public class PlayerSynthesizingData {
   public static final String STRUCTURE_ID_KEY = "structure";
   public static final String STARTED_ON_KEY = "started_on";
   public static final String COMPLETE_ON_KEY = "complete_on";
   public static final String MONSTER_KEY = "monster";
   public static final String SUCCESS_KEY = "success";
   public static final String USED_CRITTERS_KEY = "used_critters";
   public static final String USED_MONSTER_KEY = "used_monster";
   public static final String CRITTER_GENE_KEY = "gene";
   public static final String CRITTER_NUM_KEY = "num";
   private static final Set<String> validKeys = new HashSet(Arrays.asList("structure", "started_on", "complete_on", "monster", "success", "used_critters", "used_monster"));
   protected long structure;
   protected long startedOn = 0L;
   protected long completeOn = 0L;
   protected int monster;
   protected boolean success;
   protected long usedMonster;
   protected String reattunedCritters = "";
   HashMap<String, Integer> usedCritterCounts = new HashMap();

   public PlayerSynthesizingData(ISFSObject synthesizingData) throws PlayerLoadingException {
      this.initFromSFSObject(synthesizingData);
   }

   public void initFromSFSObject(ISFSObject data) throws PlayerLoadingException {
      try {
         this.structure = SFSHelpers.getLong(data.get("structure"));
         this.startedOn = SFSHelpers.getLong(data.get("started_on"));
         this.completeOn = SFSHelpers.getLong(data.get("complete_on"));
         this.monster = SFSHelpers.getInt(data.get("monster"));
         this.success = SFSHelpers.getBool(data.get("success"));
         if (data.containsKey("used_monster")) {
            this.usedMonster = SFSHelpers.getLong(data.get("used_monster"));
         }

         ISFSArray critters = data.getSFSArray("used_critters");

         for(int i = critters.size() - 1; i >= 0; --i) {
            ISFSObject sfs = critters.getSFSObject(i);
            if (this.usedCritterCounts.containsKey(sfs.getUtfString("gene"))) {
               throw new PlayerLoadingException("duplicate critter entry");
            }

            this.usedCritterCounts.put(sfs.getUtfString("gene"), sfs.getInt("num"));
         }

         PlayerDataValidation.validateKeys(data, validKeys);
      } catch (Exception var5) {
         throw new PlayerLoadingException(var5, "PlayerAttuningData initialization error");
      }
   }

   public ISFSObject getData() {
      return this.toSFSObject();
   }

   public ISFSObject toSFSObject() {
      ISFSObject s = new SFSObject();
      s.putLong("structure", this.structure);
      s.putLong("started_on", this.startedOn);
      s.putLong("complete_on", this.completeOn);
      s.putInt("monster", this.monster);
      s.putBool("success", this.success);
      if (this.usedMonster != 0L) {
         s.putLong("used_monster", this.usedMonster);
      }

      ISFSArray critters = new SFSArray();
      Iterator var3 = this.usedCritterCounts.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<String, Integer> entry = (Entry)var3.next();
         ISFSObject count = new SFSObject();
         count.putUtfString("gene", (String)entry.getKey());
         count.putInt("num", (Integer)entry.getValue());
         critters.addSFSObject(count);
      }

      s.putSFSArray("used_critters", critters);
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

   public void finishSynthesizingNow() {
      this.completeOn = MSMExtension.CurrentDBTime();
   }

   public long getCompletionTime() {
      return this.completeOn;
   }

   public long getStartTime() {
      return this.startedOn;
   }

   public int getMonster() {
      return this.monster;
   }

   public boolean getSuccess() {
      return this.success;
   }

   public int getUsedCritters() {
      int count = 0;

      Integer amount;
      for(Iterator var2 = this.usedCritterCounts.values().iterator(); var2.hasNext(); count += amount) {
         amount = (Integer)var2.next();
      }

      return count;
   }

   public String getUsedCritterGenes() {
      StringBuffer genes = new StringBuffer();
      Iterator var2 = this.usedCritterCounts.keySet().iterator();

      while(var2.hasNext()) {
         String gene = (String)var2.next();
         genes.append(gene);
      }

      return genes.toString();
   }

   public long getUsedMonster() {
      return this.usedMonster;
   }

   public String getReattunedCritters() {
      return this.reattunedCritters;
   }

   public void reduceAttuningTimeByVideo() {
      long reduceTimeAmount = (long)GameSettings.getInt("USER_SPEED_UP_DURATION") * 60L * 1000L;
      this.startedOn -= reduceTimeAmount;
      this.completeOn -= reduceTimeAmount;
   }

   public PlayerSynthesizingData(long structure, int monster, long startOn, long endOn, boolean success, HashMap<String, Integer> usedCritters, long usedMonster) {
      this.structure = structure;
      this.startedOn = startOn;
      this.completeOn = endOn;
      this.monster = monster;
      this.success = success;
      this.usedMonster = usedMonster;
      this.usedCritterCounts = usedCritters;
   }
}
