package com.bigbluebubble.BBBServer;

import com.bigbluebubble.BBBServer.util.SimpleLogger;
import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.exceptions.SFSLoginException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ConcurrentHashMap;

public class ServerEventHandler extends BaseServerEventHandler {
   public static final boolean KAILEY_DEBUG = false;
   public static boolean SPAM_EVENTS = false;
   private static ConcurrentHashMap<String, Integer> allUsers_ = new ConcurrentHashMap();

   protected static String URLEncode(String s) {
      if (s != null && !s.isEmpty()) {
         try {
            return URLEncoder.encode(s, "UTF-8");
         } catch (UnsupportedEncodingException var2) {
            return "";
         }
      } else {
         return "";
      }
   }

   public final void handleServerEvent(ISFSEvent event) throws SFSException {
      try {
         if (SPAM_EVENTS) {
            SimpleLogger.trace("**** EVENT: " + event.getType() + " ****");
         }

         if (event.getType() == SFSEventType.SERVER_READY) {
            this.serverReady(event);
         } else if (event.getType() == SFSEventType.USER_LOGIN) {
            this.userLogin(event);
         } else if (event.getType() == SFSEventType.USER_LOGOUT) {
            this.userLogout(event);
         } else if (event.getType() == SFSEventType.USER_RECONNECTION_SUCCESS) {
            this.userReconnectSuccess(event);
         } else if (event.getType() == SFSEventType.USER_RECONNECTION_TRY) {
            this.userReconnectTry(event);
         } else if (event.getType() == SFSEventType.USER_DISCONNECT) {
            this.userDisconnect(event);
         } else if (event.getType() == SFSEventType.USER_VARIABLES_UPDATE) {
            this.userVariablesUpdate(event);
         } else if (event.getType() == SFSEventType.USER_JOIN_ZONE) {
            this.userJoinZone(event);
         } else if (event.getType() == SFSEventType.ROOM_ADDED) {
            this.roomAdded(event);
         } else if (event.getType() == SFSEventType.ROOM_REMOVED) {
            this.roomRemoved(event);
         } else if (event.getType() == SFSEventType.ROOM_VARIABLES_UPDATE) {
            this.roomVariablesUpdate(event);
         } else if (event.getType() == SFSEventType.USER_JOIN_ROOM) {
            this.userJoinRoom(event);
         } else if (event.getType() == SFSEventType.USER_LEAVE_ROOM) {
            this.userLeaveRoom(event);
         } else if (event.getType() == SFSEventType.PLAYER_TO_SPECTATOR) {
            this.playerToSpectator(event);
         } else if (event.getType() == SFSEventType.SPECTATOR_TO_PLAYER) {
            this.spectatorToPlayer(event);
         } else if (event.getType() == SFSEventType.GAME_INVITATION_SUCCESS) {
            this.gameInvitationSuccess(event);
         } else if (event.getType() == SFSEventType.GAME_INVITATION_FAILURE) {
            this.gameInvitationFailure(event);
         } else if (event.getType() == SFSEventType.PUBLIC_MESSAGE) {
            this.publicMessage(event);
         } else if (event.getType() == SFSEventType.PRIVATE_MESSAGE) {
            this.privateMessage(event);
         } else if (event.getType() == SFSEventType.BUDDY_ADD) {
            this.buddyAdd(event);
         } else if (event.getType() == SFSEventType.BUDDY_REMOVE) {
            this.buddyRemove(event);
         } else if (event.getType() == SFSEventType.BUDDY_BLOCK) {
            this.buddyBlock(event);
         } else if (event.getType() == SFSEventType.BUDDY_LIST_INIT) {
            this.buddyListInit(event);
         } else if (event.getType() == SFSEventType.BUDDY_MESSAGE) {
            this.buddyMessage(event);
         } else if (event.getType() == SFSEventType.BUDDY_ONLINE_STATE_UPDATE) {
            this.buddyOnlineStatusUpdate(event);
         } else {
            if (event.getType() != SFSEventType.BUDDY_VARIABLES_UPDATE) {
               throw new UnsupportedOperationException("Not supported yet.");
            }

            this.buddyVariablesUpdate(event);
         }
      } catch (SFSException var3) {
         throw var3;
      } catch (Exception var4) {
         SimpleLogger.trace(var4);
      }

   }

   protected void serverReady(ISFSEvent event) {
   }

   protected void handleLoginData(ISession session, SFSObject data) {
      if (session != null && data != null) {
         if (data.containsKey("last_updated")) {
            session.setProperty("last_updated", data.getLong("last_updated"));
         }

         if (data.containsKey("client_version")) {
            session.setProperty("client_version", data.getUtfString("client_version"));
         }

         if (data.containsKey("last_update_version")) {
            session.setProperty("last_update_version", data.getUtfString("last_update_version"));
         }

         if (data.containsKey("client_device")) {
            session.setProperty("client_device", data.getUtfString("client_device"));
         } else {
            session.setProperty("client_device", "");
         }

         if (data.containsKey("client_os")) {
            session.setProperty("client_os", data.getUtfString("client_os"));
         } else {
            session.setProperty("client_os", "");
         }

         if (data.containsKey("client_platform")) {
            session.setProperty("client_platform", data.getUtfString("client_platform"));
         } else {
            session.setProperty("client_platform", "ios");
         }

         if (data.containsKey("client_subplatform")) {
            session.setProperty("client_subplatform", data.getUtfString("client_subplatform"));
         } else {
            session.setProperty("client_subplatform", "");
         }

         if (data.containsKey("raw_device_id")) {
            session.setProperty("raw_device_id", data.getUtfString("raw_device_id"));
         } else {
            session.setProperty("raw_device_id", "");
         }

         if (data.containsKey("client_lang")) {
            session.setProperty("client_lang", data.getUtfString("client_lang"));
         } else {
            session.setProperty("client_lang", "");
         }

         if (data.containsKey("access_key")) {
            session.setProperty("access_key", data.getUtfString("access_key"));
         }
      }

   }

   protected void userLogin(ISFSEvent event) throws SFSLoginException {
      SimpleLogger.trace("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
      SimpleLogger.trace("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% user login %%%%%%%%%%%%%%%%%%%%%%%%%%%");
      SimpleLogger.trace("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
      throw new SFSLoginException("Invalid Zone");
   }

   protected void userLogout(ISFSEvent event) {
   }

   protected void userReconnectSuccess(ISFSEvent event) {
   }

   protected void userReconnectTry(ISFSEvent event) {
   }

   protected void userDisconnect(ISFSEvent event) throws Exception {
   }

   protected void userVariablesUpdate(ISFSEvent event) {
   }

   protected void userJoinZone(ISFSEvent event) throws Exception {
   }

   protected void roomAdded(ISFSEvent event) {
   }

   protected void roomRemoved(ISFSEvent event) {
   }

   protected void roomVariablesUpdate(ISFSEvent event) {
   }

   protected void userJoinRoom(ISFSEvent event) throws Exception {
   }

   protected void userLeaveRoom(ISFSEvent event) {
   }

   protected void playerToSpectator(ISFSEvent event) {
   }

   protected void spectatorToPlayer(ISFSEvent event) {
   }

   protected void gameInvitationSuccess(ISFSEvent event) {
   }

   protected void gameInvitationFailure(ISFSEvent event) {
   }

   protected void publicMessage(ISFSEvent event) {
   }

   protected void privateMessage(ISFSEvent event) {
   }

   protected void buddyAdd(ISFSEvent event) {
   }

   protected void buddyRemove(ISFSEvent event) {
   }

   protected void buddyBlock(ISFSEvent event) {
   }

   protected void buddyListInit(ISFSEvent event) {
   }

   protected void buddyMessage(ISFSEvent event) {
   }

   protected void buddyOnlineStatusUpdate(ISFSEvent event) {
   }

   protected void buddyVariablesUpdate(ISFSEvent event) {
   }
}
