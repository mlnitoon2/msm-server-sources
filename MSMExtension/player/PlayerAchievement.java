package com.bigbluebubble.mysingingmonsters.player;

import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataType;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class PlayerAchievement {
   public static final String ID_KEY = "user_achievement_id";
   public static final String ACHIEVEMENT_NAME_KEY = "achievement";
   public static final String USER_KEY = "user";
   public static final String USER_QUEST_KEY = "user_quest";
   public static final String GC_POSTED_KEY = "gc_posted";
   public static final String FB_POSTED_KEY = "fb_posted";
   public static final String GP_POSTED_KEY = "gp_posted";
   public static final String GP_ACHIEVE_KEY = "google_achieve_id";
   private String achievement;
   private long user_achievement_id;
   private long user_quest;
   private int user;
   private boolean gc_posted;
   private boolean fb_posted;
   private boolean gp_posted;
   private String gp_acheive_id;
   private transient boolean valid = true;

   public PlayerAchievement(ISFSObject achievementData) {
      this.initFromSFSObject(achievementData);
   }

   public void initFromSFSObject(ISFSObject s) {
      if (s == null) {
         this.valid = false;
      }

      this.achievement = s.getUtfString("achievement");
      this.user_achievement_id = s.getLong("user_achievement_id");
      this.user = s.getInt("user");
      this.gc_posted = s.containsKey("gc_posted") && s.getInt("gc_posted") != 0;
      this.fb_posted = s.containsKey("fb_posted") && s.getInt("fb_posted") != 0;
      this.gp_posted = s.containsKey("gp_posted") && s.getInt("gp_posted") != 0;
      this.gp_acheive_id = s.getUtfString("google_achieve_id");
      this.user_quest = SFSHelpers.getLong("user_quest", s);
   }

   public ISFSObject toSFSObject() {
      ISFSObject s = new SFSObject();
      s.putUtfString("achievement", this.achievementName());
      s.putLong("user_achievement_id", this.getID());
      s.putInt("user", this.getUser());
      s.put("user_quest", this.getWrappedUserQuest());
      s.putInt("gc_posted", this.gameCenterPosted());
      s.putInt("fb_posted", this.facebookPosted());
      s.putInt("gp_posted", this.googlePlayPosted());
      if (this.gp_acheive_id == null) {
         Logger.trace("Need Google Play Achievement ID for " + this.achievementName());
         s.putUtfString("google_achieve_id", "");
      } else {
         s.putUtfString("google_achieve_id", this.gp_acheive_id);
      }

      return s;
   }

   public String toString() {
      return this.toSFSObject().getDump();
   }

   public boolean invalid() {
      return !this.valid;
   }

   public Long getID() {
      return this.user_achievement_id;
   }

   public String achievementName() {
      return this.achievement;
   }

   public int getUser() {
      return this.user;
   }

   public long getUserQuest() {
      return this.user_quest;
   }

   public SFSDataWrapper getWrappedUserQuest() {
      return this.getUserQuest() > 2147483647L ? new SFSDataWrapper(SFSDataType.LONG, this.getUserQuest()) : new SFSDataWrapper(SFSDataType.INT, (int)this.getUserQuest());
   }

   public void setFacebookPosted(int b) {
      this.fb_posted = b != 0;
   }

   public int facebookPosted() {
      return this.fb_posted ? 1 : 0;
   }

   public void setGameCenterPosted(int b) {
      this.gc_posted = b != 0;
   }

   public int gameCenterPosted() {
      return this.gc_posted ? 1 : 0;
   }

   public void setGooglePlayPosted(int b) {
      this.gp_posted = b != 0;
   }

   public int googlePlayPosted() {
      return this.gp_posted ? 1 : 0;
   }
}
