package com.bigbluebubble.mysingingmonsters.battle;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class BattleIslandMusicState {
   public static final int DEFAULT_SONG_ID = 1;
   public static final String CURRENTLY_PLAYING_KEY = "currently_playing";
   public static final String MUTED_KEY = "muted";
   int currentlyPlaying = 1;
   boolean muted = false;

   public BattleIslandMusicState() {
   }

   public BattleIslandMusicState(ISFSObject data) {
      if (data.containsKey("currently_playing")) {
         this.currentlyPlaying = data.getInt("currently_playing");
      } else {
         this.currentlyPlaying = 1;
      }

      if (data.containsKey("muted")) {
         this.muted = data.getBool("muted");
      } else {
         this.muted = false;
      }

   }

   public int getCurrentlyPlaying() {
      return this.currentlyPlaying;
   }

   public void setCurrentlyPlaying(int id) {
      this.currentlyPlaying = id;
   }

   public boolean getMuted() {
      return this.muted;
   }

   public void setMuted(boolean muted) {
      this.muted = muted;
   }

   public ISFSObject toSFSObject() {
      ISFSObject obj = new SFSObject();
      obj.putInt("currently_playing", this.currentlyPlaying);
      obj.putBool("muted", this.muted);
      return obj;
   }
}
