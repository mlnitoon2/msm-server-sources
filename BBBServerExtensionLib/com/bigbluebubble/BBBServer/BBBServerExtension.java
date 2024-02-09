package com.bigbluebubble.BBBServer;

import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import com.bigbluebubble.BBBServer.util.CacheWrapper;
import com.bigbluebubble.BBBServer.util.CloudwatchMetrics;
import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.BBBServer.util.SnsWrapper;
import com.bigbluebubble.BBBServer.util.SqsWrapper;
import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.Zone;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.SFSExtension;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class BBBServerExtension extends SFSExtension {
   protected IDbWrapper db;
   protected IDbWrapper authDb;
   protected SqsWrapper sqs;
   protected SnsWrapper sns;
   protected CacheWrapper cache;
   protected CloudwatchMetrics cw;
   private ConcurrentHashMap<String, IDbWrapper> dbs;
   public Long serverId;
   protected String serverHostname = null;
   protected String serverPublicHostname = null;
   protected Class eventHandlerClass_;

   public void putDB(String name, IDbWrapper newDB) {
      if (this.dbs == null) {
         this.dbs = new ConcurrentHashMap();
      }

      this.dbs.put(name, newDB);
   }

   public IDbWrapper getDB(String name) {
      return this.dbs.containsKey(name) ? (IDbWrapper)this.dbs.get(name) : null;
   }

   public String getServerHostname() {
      return this.serverHostname;
   }

   public String getServerPublicHostname() {
      return this.serverPublicHostname;
   }

   public IDbWrapper getDB() {
      return this.db;
   }

   public IDbWrapper getAuthDB() {
      return this.authDb;
   }

   public SqsWrapper getSQS() {
      return this.sqs;
   }

   public SnsWrapper getSNS() {
      return this.sns;
   }

   public CacheWrapper getCache() {
      return this.cache;
   }

   public CloudwatchMetrics getCloudwatch() {
      return this.cw;
   }

   public BBBServerExtension() {
      this.eventHandlerClass_ = ServerEventHandler.class;
   }

   public BBBServerExtension(Class eventHandlerClass) {
      this.eventHandlerClass_ = eventHandlerClass;
   }

   public void init() {
      this.addEventHandler(SFSEventType.SERVER_READY, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.USER_LOGIN, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.USER_LOGOUT, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.USER_RECONNECTION_SUCCESS, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.USER_RECONNECTION_TRY, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.USER_DISCONNECT, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.USER_VARIABLES_UPDATE, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.USER_JOIN_ZONE, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.ROOM_ADDED, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.ROOM_REMOVED, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.ROOM_VARIABLES_UPDATE, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.USER_JOIN_ROOM, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.USER_LEAVE_ROOM, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.PLAYER_TO_SPECTATOR, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.SPECTATOR_TO_PLAYER, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.GAME_INVITATION_SUCCESS, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.GAME_INVITATION_FAILURE, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.PUBLIC_MESSAGE, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.PRIVATE_MESSAGE, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.BUDDY_ADD, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.BUDDY_REMOVE, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.BUDDY_BLOCK, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.BUDDY_LIST_INIT, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.BUDDY_MESSAGE, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.BUDDY_ONLINE_STATE_UPDATE, this.eventHandlerClass_);
      this.addEventHandler(SFSEventType.BUDDY_VARIABLES_UPDATE, this.eventHandlerClass_);
   }

   public void destroy() {
      Zone zone = this.getParentZone();
      Collection<ISession> sessions = zone.getSessionList();
      if (!sessions.isEmpty()) {
         ISFSObject params = new SFSObject();
         this.getApi().sendAdminMessage((User)null, "Server is going down or restarting", params, sessions);
      }

      Collection<User> users = zone.getUserList();
      Iterator var4 = users.iterator();

      while(var4.hasNext()) {
         User u = (User)var4.next();
         this.getApi().kickUser(u, (User)null, "Extension is reloading", 5);
      }

      if (this.cache != null) {
         this.cache.kill();
         this.cache = null;
      }

      super.destroy();
   }

   public void addCWMetric(String name, double data_point) {
      if (this.cw != null) {
         this.cw.addMetric(name, "", StandardUnit.None, data_point);
      }
   }

   public void addCWMetric(String name, StandardUnit unit, double data_point) {
      if (this.cw != null) {
         this.cw.addMetric(name, "", unit, data_point);
      }
   }

   public void addCWMetric(String name, String namespaceSuffix, double data_point) {
      if (this.cw != null) {
         this.cw.addMetric(name, namespaceSuffix, StandardUnit.None, data_point);
      }
   }

   public void addCWMetric(String name, String namespaceSuffix, StandardUnit unit, double data_point) {
      if (this.cw != null) {
         this.cw.addMetric(name, namespaceSuffix, unit, data_point);
      }
   }

   public void addCWMetric(PutMetricDataRequest request) {
      if (this.cw != null) {
         this.cw.addMetric(request);
      }
   }
}
