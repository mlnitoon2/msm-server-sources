package com.bigbluebubble.mysingingmonsters.player;

import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public abstract class PlayerEntity {
   public static final String PARENT_ID_KEY = "island";
   public static final String LAST_COLLECTION_KEY = "last_collection";
   public static final String POS_X_KEY = "pos_x";
   public static final String POS_Y_KEY = "pos_y";
   public static final String MUTED_KEY = "muted";
   public static final String FLIP_KEY = "flip";
   public static final String BOOK_VALUE_KEY = "book_value";
   protected long island;
   protected long last_collection;
   protected short pos_x;
   protected short pos_y;
   protected boolean muted;
   protected boolean flip;
   protected int bookValue = -1;

   public PlayerEntity(ISFSObject componentData) {
   }

   public ISFSObject toSFSObject(PlayerIsland pi) {
      ISFSObject s = new SFSObject();
      s.putLong("island", this.getParentID());
      s.putInt("pos_x", this.getXPosition());
      s.putInt("pos_y", this.getYPosition());
      s.putInt("muted", this.getMuted());
      s.putInt("flip", this.getFlip());
      if (this.last_collection != 0L) {
         s.putLong("last_collection", this.last_collection);
      }

      if (this.bookValue != -1) {
         s.putInt("book_value", this.bookValue);
      }

      return s;
   }

   public String toString(PlayerIsland pi) {
      return this.toSFSObject(pi).getDump();
   }

   public void setBookValue(int coins) {
      this.bookValue = coins;
   }

   public int bookValue() {
      return this.bookValue;
   }

   public long getParentID() {
      return this.island;
   }

   public int getXPosition() {
      return this.pos_x;
   }

   public int getYPosition() {
      return this.pos_y;
   }

   public long getLastCollectionTime() {
      return this.last_collection;
   }

   public void toggleFlip() {
      this.flip = !this.flip;
   }

   public void setFlip(boolean f) {
      this.flip = f;
   }

   public int getFlip() {
      return this.flip ? 1 : 0;
   }

   public int getMuted() {
      return this.muted ? 1 : 0;
   }

   public void setParentID(long ID) {
      this.island = ID;
   }

   public void setPosition(int x, int y) {
      this.pos_x = (short)x;
      this.pos_y = (short)y;
   }

   public void resetLastCollectionTime() {
      this.last_collection = MSMExtension.CurrentDBTime();
   }

   public void toggleMute() {
      this.muted = !this.muted;
   }
}
