package com.bigbluebubble.mysingingmonsters.task;

import com.amazonaws.util.EC2MetadataUtils;
import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class MSMServerStatusTask implements Runnable {
   private static MSMServerStatusTask instance;
   final boolean showReadyForRealMessage = true;
   boolean printedReadyMsg = false;
   boolean isReady = true;
   boolean isDead = false;
   String identifier = "";
   ReentrantLock lock = new ReentrantLock();

   public static String getIdentifier() {
      return instance == null ? "" : instance.identifier;
   }

   public static void schedule(int authServerStatsInterval) {
      instance = new MSMServerStatusTask();
      SmartFoxServer.getInstance().getTaskScheduler().scheduleAtFixedRate(instance, authServerStatsInterval, authServerStatsInterval, TimeUnit.SECONDS);
   }

   public static void stop() {
      if (instance != null) {
         instance.stopInteral();
      }
   }

   private void stopInteral() {
      this.isDead = true;
      this.lock.lock();

      try {
         MSMExtension ext = MSMExtension.getInstance();
         IDbWrapper authDb = ext.getDB();
         if (authDb != null) {
            try {
               String sql = "DELETE FROM servers WHERE server_id = ?";
               Object[] params = new Object[]{ext.serverId};
               authDb.update(sql, params);
               Logger.trace("Server removed database: ", ext.serverId);
            } catch (Exception var8) {
               Logger.trace(var8);
            }
         }
      } finally {
         this.lock.unlock();
      }

   }

   private MSMServerStatusTask() {
      MSMExtension ext = MSMExtension.getInstance();
      IDbWrapper authDb = ext.getDB();
      if (authDb != null) {
         try {
            Logger.trace("Server Status Task is registering this SmartFoxServer with Auth");
            String serverHostname = ext.getServerHostname();
            String serverPublicHostname = ext.getServerPublicHostname();
            int maxUsers = ext.getParentZone().getMaxAllowedUsers();
            String sql = "SELECT server_id, max_users FROM servers WHERE name = ? AND game = ?";
            Object[] params = new Object[]{serverHostname, GameSettings.get("AUTH_GAME_ID")};
            ISFSArray results = authDb.query(sql, params);
            if (results.size() > 0) {
               ISFSObject result = results.getSFSObject(0);
               ext.serverId = result.getLong("server_id");
               int max_users = result.getInt("max_users");
               ext.getParentZone().setMaxAllowedUsers(max_users);
               sql = "UPDATE servers SET public_hostname=?, ready=0, user_count=0, last_updated=NOW() WHERE server_id = ?";
               params = new Object[]{serverPublicHostname, ext.serverId};
               authDb.update(sql, params);
            } else {
               String flattenedVersion = GameSettings.get("MIN_CONNECTING_VERSION_AUTH2");
               if (flattenedVersion == null) {
                  flattenedVersion = GameSettings.get("MIN_CLIENT_VERSION");
               }

               flattenedVersion = flattenedVersion.replace(".", "");
               String privateIp = null;

               try {
                  if (!ext.isDevServer()) {
                     privateIp = EC2MetadataUtils.getPrivateIpAddress();
                  }
               } catch (Exception var12) {
                  Logger.trace("Warning: Unable to determine private IP");
               }

               if (privateIp != null) {
                  this.identifier = privateIp;
                  sql = "INSERT INTO servers ( game, public_hostname, name, private_ip, client_version, max_users ) VALUES (?,?,?,?,?,?) ";
                  params = new Object[]{GameSettings.get("AUTH_GAME_ID"), serverPublicHostname, serverHostname, privateIp, flattenedVersion, maxUsers};
               } else {
                  this.identifier = serverHostname;
                  sql = "INSERT INTO servers ( game, public_hostname, name, client_version, max_users ) VALUES (?,?,?,?,?) ";
                  params = new Object[]{GameSettings.get("AUTH_GAME_ID"), serverPublicHostname, serverHostname, flattenedVersion, maxUsers};
               }

               ext.serverId = authDb.insertGetId(sql, params);
            }
         } catch (Exception var13) {
            Logger.trace(var13);
         }
      } else {
         Logger.trace("Stats Task is NOT registering this SmartFoxServer with Auth");
      }

   }

   private void update() {
      this.lock.lock();

      try {
         MSMExtension ext = MSMExtension.getInstance();
         IDbWrapper authDb = ext.getDB();
         if (authDb != null) {
            try {
               String sql = "SELECT max_users FROM servers WHERE server_id = ?";
               Object[] params = new Object[]{ext.serverId};
               ISFSArray results = authDb.query(sql, params);
               if (results.size() > 0) {
                  ISFSObject result = results.getSFSObject(0);
                  int max_users = result.getInt("max_users");
                  if (max_users != ext.getParentZone().getMaxAllowedUsers()) {
                     ext.getParentZone().setMaxAllowedUsers(max_users);
                  }
               }

               int userCount = ext.getParentZone().getUserCount();
               sql = "UPDATE servers SET user_count=?, ready=?, last_updated=NOW() WHERE server_id=?";
               params = new Object[]{userCount, true, ext.serverId};
               authDb.update(sql, params);
            } catch (Exception var11) {
               Logger.trace(var11);
            }
         }
      } finally {
         this.lock.unlock();
      }

   }

   public void run() {
      if (!this.isDead) {
         this.update();
         if (!this.printedReadyMsg) {
            Logger.trace("READY FOR REAL!!");
            this.printedReadyMsg = true;
         }

      }
   }
}
