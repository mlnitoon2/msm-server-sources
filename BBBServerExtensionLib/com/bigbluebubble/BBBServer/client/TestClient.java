package com.bigbluebubble.BBBServer.client;

import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import sfs2x.client.SmartFox;
import sfs2x.client.core.BaseEvent;
import sfs2x.client.core.IEventListener;
import sfs2x.client.requests.LoginRequest;

public class TestClient implements IEventListener {
   private SmartFox sfs = new SmartFox(false);
   public static final String DEFAULT_USERNAME = "timsa";
   public static final String DEFAULT_PASSWORD = "pass";
   public static final String DEFAULT_ZONE_NAME = "RaftPirates";
   public static final String DEFAULT_ACCT_TYPE = "email";

   public static void main(String[] args) {
      TestClient test = new TestClient();
      test.initAndStart();
   }

   private TestClient() {
   }

   private void initAndStart() {
      this.sfs.addEventListener("configLoadSuccess", this);
      this.sfs.addEventListener("configLoadFailure", this);
      this.sfs.addEventListener("connection", this);
      this.sfs.addEventListener("connectionLost", this);
      this.sfs.addEventListener("login", this);
      this.sfs.addEventListener("loginError", this);
      this.sfs.addEventListener("extensionResponse", this);
      this.sfs.addEventListener("roomJoin", this);
      this.sfs.addEventListener("roomJoinError", this);
      (new Thread() {
         public void run() {
            TestClient.this.sfs.connect("127.0.0.1", 9339);
         }
      }).start();
   }

   public void dispatch(BaseEvent be) throws SFSException {
      System.out.println("***** " + be.getType().toUpperCase() + " *****");
      if (be.getType().equalsIgnoreCase("connection")) {
         SFSObject args = new SFSObject();
         args.putUtfString("acctType", "email");
         this.sfs.send(new LoginRequest("timsa", "pass", "RaftPirates", args));
      } else {
         if (!be.getType().equalsIgnoreCase("roomGroupSubscribe")) {
            if (be.getType().equalsIgnoreCase("connectionLost")) {
               return;
            }

            if (be.getType().equalsIgnoreCase("configLoadSuccess")) {
               return;
            }

            if (be.getType().equalsIgnoreCase("configLoadFailure")) {
               return;
            }

            if (be.getType().equalsIgnoreCase("login")) {
               System.out.println("Login OK...Here is your room list");
               System.out.println(this.sfs.getRoomList().toString());
               return;
            }

            if (be.getType().equalsIgnoreCase("loginError")) {
               System.out.println(be.getArguments().get("errorMessage"));
               return;
            }

            if (be.getType().equalsIgnoreCase("roomJoin")) {
               return;
            }

            if (be.getType().equalsIgnoreCase("roomJoinError")) {
               System.out.println(be.getArguments().get("errorMessage"));
               return;
            }

            if (be.getType().equalsIgnoreCase("roomVariablesUpdate")) {
               return;
            }

            if (!be.getType().equalsIgnoreCase("extensionResponse")) {
               throw new UnsupportedOperationException(be.getType().toString() + " Not supported yet.");
            }
         }

      }
   }
}
