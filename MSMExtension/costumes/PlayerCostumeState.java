package com.bigbluebubble.mysingingmonsters.costumes;

import com.bigbluebubble.mysingingmonsters.player.PlayerInventory;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import java.util.Collection;
import java.util.HashSet;

public class PlayerCostumeState {
   public static final String UNLOCKED_KEY = "unlocked";
   private Collection<Integer> unlockedCostumes_;
   private PlayerInventory inventory_;
   private boolean isDirty_ = false;

   public Collection<Integer> unlockedCostumes() {
      return this.unlockedCostumes_;
   }

   public PlayerInventory inventory() {
      return this.inventory_;
   }

   public boolean isDirty() {
      return this.isDirty_ || this.inventory_.isDirty();
   }

   public void setDirty(boolean dirty) {
      this.isDirty_ = dirty;
      this.inventory_.setDirty(dirty);
   }

   public PlayerCostumeState() {
      this.unlockedCostumes_ = new HashSet();
      this.inventory_ = new PlayerInventory();
   }

   public PlayerCostumeState(ISFSObject data) {
      if (data.containsKey("unlocked")) {
         this.unlockedCostumes_ = data.getIntArray("unlocked");
      } else {
         this.unlockedCostumes_ = new HashSet();
      }

      this.inventory_ = new PlayerInventory(data);
   }

   public boolean isCostumeUnlocked(int costumeId) {
      return costumeId == 0 ? true : this.unlockedCostumes_.contains(costumeId);
   }

   public void unlockCostume(int costumeId) {
      if (!this.isCostumeUnlocked(costumeId)) {
         this.unlockedCostumes_.add(costumeId);
         this.isDirty_ = true;
      }
   }

   public ISFSObject toSFSObject() {
      ISFSObject data = this.inventory_.toSFSObject();
      data.putIntArray("unlocked", this.unlockedCostumes_);
      return data;
   }
}
