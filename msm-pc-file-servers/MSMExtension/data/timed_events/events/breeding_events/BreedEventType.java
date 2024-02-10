package com.bigbluebubble.mysingingmonsters.data.timed_events.events.breeding_events;

import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.data.MonsterLookup;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

public abstract class BreedEventType {
   protected HashMap<Integer, BreedEventType.BreedingComboMod> breedingComboMods = new HashMap();
   protected String icon;
   protected String desc;

   protected abstract String getSqlDef();

   public String alternateIcon() {
      return this.icon;
   }

   public String popupDesc() {
      return this.desc;
   }

   public BreedEventType(String icon, String desc) {
      this.icon = icon;
      this.desc = desc;
   }

   public boolean affectsBreedingCombo(int monster1, int monster2, int result) {
      return this.breedingComboMods.containsKey(this.getBreedingComboHash(monster1, monster2, result));
   }

   public int newProbability(int monster1, int monster2, int result) {
      int hash = this.getBreedingComboHash(monster1, monster2, result);
      if (this.breedingComboMods.containsKey(hash)) {
         BreedEventType.BreedingComboMod e = (BreedEventType.BreedingComboMod)this.breedingComboMods.get(hash);
         return e.newProbability;
      } else {
         return -1;
      }
   }

   public void GenerateBreedingComboMods() {
      this.GenerateBreedingCombosFromSql();
   }

   public void GenerateBreedingCombosFromSql() {
      String sqlDef = this.getSqlDef();
      String[] lines = sqlDef.split("\r\n");

      for(int queryLineInd = 0; queryLineInd < lines.length; ++queryLineInd) {
         if (!lines[queryLineInd].matches("^--.*") && !lines[queryLineInd].matches("^\\s*$") && lines[queryLineInd].matches("UPDATE `breeding_combinations` SET\\s+.*\\s+WHERE\\s+.*")) {
            String probabilityStr = lines[queryLineInd];
            probabilityStr = probabilityStr.replaceFirst("UPDATE `breeding_combinations` SET `probability` =\\s*", "");
            probabilityStr = probabilityStr.replaceFirst("\\D.*", "");
            int newProbability = Integer.parseInt(probabilityStr);
            String selectQuery = lines[queryLineInd];
            selectQuery = selectQuery.replaceFirst("UPDATE `", "SELECT monster_1, monster_2, result, probability FROM `");
            selectQuery = selectQuery.replaceFirst("`\\s+.*\\s+WHERE\\s+", "` WHERE ");

            try {
               SFSArray sqlResult = MSMExtension.getInstance().getDB().query(selectQuery);

               for(int i = 0; i < sqlResult.size(); ++i) {
                  ISFSObject row = sqlResult.getSFSObject(i);
                  int monster1 = row.getInt("monster_1");
                  int monster2 = row.getInt("monster_2");
                  int result = row.getInt("result");
                  BreedEventType.BreedingComboMod combo = new BreedEventType.BreedingComboMod(monster1, monster2, result, newProbability, row.getInt("probability"));
                  int hash = this.getBreedingComboHash(monster1, monster2, result);
                  if (this.breedingComboMods.containsKey(hash)) {
                     String message = "ERROR in Breeding Event Creation!! HASH COLLISION DETECTED!!!!";
                     message = message + "HASH 2: (" + monster1 + ", " + monster2 + ", " + result + ") COLLIDES WITH: ";
                     BreedEventType.BreedingComboMod collision = (BreedEventType.BreedingComboMod)this.breedingComboMods.get(hash);
                     message = message + "HASH 1: (" + collision.monster1 + ", " + collision.monster2 + ", " + collision.result + ").";
                     throw new Exception(message);
                  }

                  this.breedingComboMods.put(hash, combo);
               }
            } catch (Exception var17) {
               Logger.trace(var17, "**** error in BreedEventType::GenerateBreedingCombosFromSql ****", "   selectQuery : " + selectQuery);
            }
         }
      }

   }

   private int cantorPairing(int a, int b) {
      return (a + b) * (a + b + 1) / 2 + b;
   }

   private int cantorTriple(int a, int b, int c) {
      return (this.cantorPairing(a, b) + c) * (this.cantorPairing(a, b) + c + 1) / 2 + c;
   }

   protected int getBreedingComboHash(int monster1, int monster2, int result) {
      return monster1 <= monster2 ? this.cantorTriple(monster1, monster2, result) : this.cantorTriple(monster2, monster1, result);
   }

   public String toString() {
      String s = new String();
      Set<Entry<Integer, BreedEventType.BreedingComboMod>> entrySet = this.breedingComboMods.entrySet();

      Entry curEntry;
      for(Iterator itr = entrySet.iterator(); itr.hasNext(); s = s + "\t" + ((BreedEventType.BreedingComboMod)curEntry.getValue()).toString() + "\n") {
         curEntry = (Entry)itr.next();
      }

      return s;
   }

   protected class BreedingComboMod {
      public int monster1;
      public int monster2;
      public int result;
      public int newProbability;
      public int oldProbability;

      public BreedingComboMod(int monster1, int monster2, int result, int newProb, int oldProb) {
         this.monster1 = monster1;
         this.monster2 = monster2;
         this.result = result;
         this.newProbability = newProb;
         this.oldProbability = oldProb;
      }

      public String toString() {
         return MonsterLookup.get(this.monster1).commonName() + " + " + MonsterLookup.get(this.monster2).commonName() + " = " + MonsterLookup.get(this.result).commonName() + ": " + this.oldProbability + "->" + this.newProbability;
      }
   }
}
