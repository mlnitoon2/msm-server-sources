package com.bigbluebubble.mysingingmonsters.costumes;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Collection;

public class MonsterCostumeState {
   public static final String EQUIPPED_KEY = "eq";
   public static final String PURCHASED_KEY = "p";
   int equipped = 0;
   Collection<Integer> purchased;

   public MonsterCostumeState() {
      this.purchased = new ArrayList();
   }

   public MonsterCostumeState(ISFSObject data) {
      if (data.containsKey("eq")) {
         this.equipped = data.getInt("eq");
      }

      this.purchased = new ArrayList();
      if (data.containsKey("p")) {
         this.purchased.addAll(data.getIntArray("p"));
      }

   }

   public int getEquipped() {
      return this.equipped;
   }

   public void setEquipped(int costumeId) {
      this.equipped = costumeId;
   }

   public Collection<Integer> getPurchased() {
      return this.purchased;
   }

   public boolean hasPurchased(int costumeId) {
      return costumeId == 0 ? true : this.purchased.contains(costumeId);
   }

   public void setPurchased(int costumeId) {
      this.purchased.add(costumeId);
   }

   public ISFSObject toSFSObject() {
      ISFSObject data = new SFSObject();
      data.putInt("eq", this.equipped);
      data.putIntArray("p", this.purchased);
      return data;
   }
}
