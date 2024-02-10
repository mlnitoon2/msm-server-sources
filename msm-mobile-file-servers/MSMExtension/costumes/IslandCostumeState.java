package com.bigbluebubble.mysingingmonsters.costumes;

import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class IslandCostumeState {
   private final String COSTUMES_KEY = "costumes";
   ISFSArray costumes;

   public IslandCostumeState() {
      this.costumes = new SFSArray();
   }

   public IslandCostumeState(ISFSObject costumeData) {
      if (!costumeData.containsKey("costumes")) {
         this.costumes = new SFSArray();
      } else {
         this.costumes = costumeData.getSFSArray("costumes");
      }

   }

   public boolean hasCostume(int costumeId) {
      if (costumeId == 0) {
         return true;
      } else {
         return this.getCostume(costumeId) != null;
      }
   }

   public ISFSObject getCostume(int costumeId) {
      Iterator i = this.costumes.iterator();

      ISFSObject costumeData;
      do {
         if (!i.hasNext()) {
            return null;
         }

         costumeData = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
      } while(costumeData.getInt("id") != costumeId);

      return costumeData;
   }

   public void addCostume(int costumeId) {
      ISFSObject costumeData = new SFSObject();
      costumeData.putInt("id", costumeId);
      costumeData.putInt("v", 0);
      this.costumes.addSFSObject(costumeData);
   }

   public int getCredits(int costumeId) {
      ISFSObject costumeData = this.getCostume(costumeId);
      return costumeData != null ? costumeData.getInt("v") : 0;
   }

   public void addCredit(int costumeId, int amount) {
      ISFSObject costumeData = this.getCostume(costumeId);
      if (costumeData == null) {
         this.addCostume(costumeId);
         costumeData = this.getCostume(costumeId);
      }

      int credits = costumeData.getInt("v");
      costumeData.putInt("v", credits + amount);
   }

   public void removeCredit(int costumeId, int amount) throws Exception {
      ISFSObject costumeData = this.getCostume(costumeId);
      if (costumeData == null) {
         this.addCostume(costumeId);
         costumeData = this.getCostume(costumeId);
      }

      int credits = costumeData.getInt("v");
      if (credits >= amount) {
         costumeData.putInt("v", credits - amount);
      } else {
         throw new Exception("not enough credits");
      }
   }

   public ISFSObject toSFSObject() {
      ISFSObject obj = new SFSObject();
      obj.putSFSArray("costumes", this.costumes);
      return obj;
   }

   public Iterable<Integer> costumeIds() {
      return new Iterable<Integer>() {
         public Iterator<Integer> iterator() {
            return new Iterator<Integer>() {
               int pos = 0;

               public boolean hasNext() {
                  if (IslandCostumeState.this.costumes == null) {
                     return false;
                  } else {
                     return this.pos < IslandCostumeState.this.costumes.size();
                  }
               }

               public Integer next() {
                  if (!this.hasNext()) {
                     throw new NoSuchElementException();
                  } else {
                     return IslandCostumeState.this.costumes.getSFSObject(this.pos++).getInt("id");
                  }
               }

               public void remove() {
                  throw new UnsupportedOperationException("remove");
               }
            };
         }
      };
   }
}
