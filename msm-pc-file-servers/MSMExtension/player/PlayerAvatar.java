package com.bigbluebubble.mysingingmonsters.player;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class PlayerAvatar {
   public static final String TYPE_KEY = "pp_type";
   public static final String INFO_KEY = "pp_info";
   ISFSObject data_;
   boolean isDirty_ = false;

   public PlayerAvatar() {
      this.data_ = new SFSObject();
      this.data_.putInt("pp_type", 0);
      this.data_.putUtfString("pp_info", "0");
      this.isDirty_ = true;
   }

   public PlayerAvatar(ISFSObject data) {
      this.data_ = data;
   }

   public ISFSObject toSFSObject() {
      return this.data_;
   }

   public boolean isDirty() {
      return this.isDirty_;
   }

   public int getType() {
      return this.data_.getInt("pp_type");
   }

   public void setType(int type) {
      this.isDirty_ = this.getType() != type;
      this.data_.putInt("pp_type", type);
   }

   public String getInfo() {
      return this.data_.getUtfString("pp_info");
   }

   public void setInfo(String info) {
      this.isDirty_ = this.getInfo() != info;
      this.data_.putUtfString("pp_info", info);
   }
}
