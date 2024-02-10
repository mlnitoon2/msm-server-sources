package com.bigbluebubble.mysingingmonsters.task;

import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;

public class CheckUserMailTask implements Runnable {
   long maxId;

   public CheckUserMailTask() {
      this.updateMaxId();
   }

   void updateMaxId() {
      try {
         this.maxId = MSMExtension.getInstance().getDB().query("SELECT MAX(user_mail_id) as max_id FROM user_mail").getSFSObject(0).getLong("max_id");
      } catch (Exception var2) {
         Logger.trace(var2);
      }

   }

   public void run() {
      try {
         String SQL = "SELECT DISTINCT user_mail.user, user_game_id_to_bbb_id.user_game_id FROM user_mail LEFT JOIN users ON users.user_id = user_mail.user LEFT JOIN user_game_id_to_bbb_id ON users.bbb_id = user_game_id_to_bbb_id.bbb_id WHERE user_mail_id > ?";
         ISFSArray users = MSMExtension.getInstance().getDB().query("SELECT DISTINCT user_mail.user, user_game_id_to_bbb_id.user_game_id FROM user_mail LEFT JOIN users ON users.user_id = user_mail.user LEFT JOIN user_game_id_to_bbb_id ON users.bbb_id = user_game_id_to_bbb_id.bbb_id WHERE user_mail_id > ?", new Object[]{this.maxId});
         Iterator i = users.iterator();

         while(i.hasNext()) {
            String username = ((ISFSObject)((SFSDataWrapper)i.next()).getObject()).getUtfString("user_game_id");
            User user = MSMExtension.getInstance().getParentZone().getUserByName(username);
            if (user != null) {
               Player player = (Player)user.getProperty("player_object");
               player.updateMail();
               SFSArray updateVars = new SFSArray();
               SFSObject updateObj = new SFSObject();
               updateObj.putSFSArray("mailbox", player.getMailbox());
               updateVars.addSFSObject(updateObj);
               updateObj = new SFSObject();
               updateObj.putBool("new_mail", true);
               updateVars.addSFSObject(updateObj);
               ISFSObject response = new SFSObject();
               response.putSFSArray("properties", updateVars);
               MSMExtension.getInstance().send("gs_update_properties", response, user);
            }
         }

         this.updateMaxId();
      } catch (Exception var10) {
         Logger.trace(var10);
      }

   }
}
