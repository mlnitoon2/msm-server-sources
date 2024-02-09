package com.bigbluebubble.mysingingmonsters;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.ServerEventHandler;
import com.bigbluebubble.BBBServer.util.BBBToken;
import com.bigbluebubble.BBBServer.util.CacheWrapper;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.data.AccessKeys;
import com.bigbluebubble.mysingingmonsters.data.MsmSession;
import com.bigbluebubble.mysingingmonsters.data.groups.Advertisement2018Group;
import com.bigbluebubble.mysingingmonsters.data.groups.BattleHintLevelGroup;
import com.bigbluebubble.mysingingmonsters.data.groups.BonusPromoGroup;
import com.bigbluebubble.mysingingmonsters.data.groups.ComboPackGroup;
import com.bigbluebubble.mysingingmonsters.data.groups.CurrencyGroup;
import com.bigbluebubble.mysingingmonsters.data.groups.DailyDiamondGroup;
import com.bigbluebubble.mysingingmonsters.data.groups.IslandPromoGroup;
import com.bigbluebubble.mysingingmonsters.data.groups.NoAdvertisementGroup;
import com.bigbluebubble.mysingingmonsters.data.groups.RewardedAdvertisementGroup;
import com.bigbluebubble.mysingingmonsters.data.groups.TapjoyTierGroup;
import com.bigbluebubble.mysingingmonsters.data.groups.TutorialGroup;
import com.bigbluebubble.mysingingmonsters.logging.MSMCommandLogging;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.bigbluebubble.mysingingmonsters.util.fix.PlayerFixes;
import com.bigbluebubble.mysingingmonsters.util.ip.Geo;
import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.Zone;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.exceptions.SFSErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.exceptions.SFSLoginException;
import com.smartfoxserver.v2.util.ClientDisconnectionReason;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.spy.memcached.OperationTimeoutException;

public class MSMServerEventHandler extends ServerEventHandler {
   protected void handleLoginData(ISession session, SFSObject data) {
      super.handleLoginData(session, data);
   }

   protected void userJoinZone(ISFSEvent event) throws Exception {
      try {
         super.userJoinZone(event);
         MSMExtension ext = (MSMExtension)this.getParentExtension();
         User user = (User)event.getParameter(SFSEventParam.USER);
         Zone zone = (Zone)event.getParameter(SFSEventParam.ZONE);
         ISession session = user.getSession();
         String userGameId = (String)session.getProperty("user_game_id");
         long sessionStartTime = MSMExtension.CurrentDBTime();
         user.setProperty("sess_start", sessionStartTime);
         MSMCommandLogging.createClientRequestLog(user);
         if (MSMExtension.isReloadingStaticData) {
            Logger.trace("User trying to login during a stoppage in play - bu-bye");
            user.disconnect(ClientDisconnectionReason.KICK);
            return;
         }

         String minRequiredClientVersion = GameSettings.get("MIN_CLIENT_VERSION");
         String actualClientVersion = (String)session.getProperty("client_version");
         VersionInfo minVerToPlay = new VersionInfo(minRequiredClientVersion);
         VersionInfo clientVer = new VersionInfo(actualClientVersion);
         if (clientVer.compareTo(minVerToPlay) < 0) {
            ext.sendVersionError(user, false);
            return;
         }

         String gameAccessKey = AccessKeys.getKey(clientVer);
         String userAccessKey = (String)session.getProperty("access_key");
         if (gameAccessKey == null) {
            Logger.trace("No game access key in dlc table for user '" + userGameId + "' on client version " + clientVer);
            ext.sendVersionError(user, false);
            return;
         }

         if (!gameAccessKey.equalsIgnoreCase("NONE") && !gameAccessKey.equalsIgnoreCase(userAccessKey)) {
            Logger.trace("Invalid access key '" + userAccessKey + "' for user '" + userGameId + "' on client version " + clientVer);
            ext.sendVersionError(user, false);
            return;
         }

         String clientDevice = session.getProperty("client_device") != null ? (String)session.getProperty("client_device") : "";
         String clientOs = session.getProperty("client_os") != null ? (String)session.getProperty("client_os") : "";
         String clientPlatform = (String)session.getProperty("client_platform");
         String clientSubplatform = session.getProperty("client_subplatform") != null ? (String)session.getProperty("client_subplatform") : "";
         String rawDeviceId = session.getProperty("raw_device_id") != null ? (String)session.getProperty("raw_device_id") : "";
         String ipAddress = session.getFullIpAddress();
         String clientLang = session.getProperty("client_lang") != null ? (String)session.getProperty("client_lang") : "";
         int userID = true;
         long bbbId = -1L;
         boolean isNewUser = false;
         String sql = "SELECT * FROM user_game_id_to_bbb_id WHERE user_game_id=?";
         Object[] args = new Object[]{userGameId};
         SFSArray records = ext.getDB().query(sql, args);
         String lastUpdatedVersion;
         int userID;
         if (records.size() == 0) {
            lastUpdatedVersion = Geo.locate(ipAddress);
            Logger.trace(String.format(">>>> New user in Geo '%s' with ip address '%s'", lastUpdatedVersion, ipAddress));
            bbbId = this.CreateNewBbbIdDbEntry(userGameId);
            session.setProperty("bbb_id", bbbId);
            userID = this.CreateNewPlayer(bbbId, user, clientPlatform, clientVer, lastUpdatedVersion);
            isNewUser = true;
         } else {
            bbbId = records.getSFSObject(0).getLong("bbb_id");
            session.setProperty("bbb_id", bbbId);
            sql = "SELECT user_id, client_platform FROM users WHERE bbb_id=?";
            records = ext.getDB().query(sql, new Object[]{bbbId});
            if (records.size() == 0) {
               lastUpdatedVersion = Geo.locate(ipAddress);
               Logger.trace(String.format(">>>> New user in Geo '%s' with ip address '%s'", lastUpdatedVersion, ipAddress));
               userID = this.CreateNewPlayer(bbbId, user, clientPlatform, clientVer, lastUpdatedVersion);
            } else {
               SFSObject userIdRecord = (SFSObject)records.getSFSObject(0);
               userID = userIdRecord.getInt("user_id");
               sql = "SELECT * FROM user_banned WHERE bbb_id=? AND ban_expiry>NOW()";
               records = ext.getDB().query(sql, new Object[]{bbbId});
               SFSObject response;
               if (records.size() > 0) {
                  response = new SFSObject();
                  response.putUtfString("reason", records.getSFSObject(0).getUtfString("reason"));
                  response.putLong("bbb_id", bbbId);
                  this.send("gs_player_banned", response, user);
                  return;
               }

               sql = "SELECT * FROM admin_user_accounts_currently_being_accessed WHERE user_bbbid=?";
               records = ext.getDB().query(sql, new Object[]{bbbId});
               if (records.size() > 0) {
                  response = new SFSObject();
                  response.putBool("force_logout", true);
                  response.putUtfString("msg", "ACCOUNT_LOCKED_BY_ADMIN");
                  this.send("gs_display_generic_message", response, user);
                  return;
               }

               sql = "DELETE FROM user_banned WHERE bbb_id=?";
               ext.getDB().update(sql, new Object[]{bbbId});
               if (userIdRecord.getUtfString("client_platform") == null || userIdRecord.getUtfString("client_platform").equals("")) {
                  sql = "UPDATE users SET client_platform=? WHERE user_id=?";
                  ext.getDB().update(sql, new Object[]{clientPlatform, userID});
               }
            }
         }

         lastUpdatedVersion = this.getClientLastUpdateVersion(session);
         Player player = ext.createPlayerByID((long)userID, true, clientVer, clientPlatform, clientSubplatform);
         String loginType = (String)session.getProperty("loginType");
         if (loginType.equals("fb") || loginType.equals("gc") || loginType.equals("steam")) {
            ext.updateSocialUsers(bbbId, (String)session.getProperty("username"), loginType, true);
         }

         boolean admin = player.isAdmin();
         user.setPrivilegeId((short)(admin ? 3 : 1));
         if (admin) {
            MSMExtension.getInstance().clearAdminsLockedAccounts(player.getBbbId());
         }

         user.setProperty("player_object", player);
         user.setProperty("bbb_id", bbbId);
         user.setProperty("client_version", actualClientVersion);
         user.setProperty("last_update_version", lastUpdatedVersion);
         user.setProperty("client_device", clientDevice);
         user.setProperty("client_platform", clientPlatform);
         user.setProperty("client_subplatform", clientSubplatform);
         user.setProperty("client_os", clientOs);
         user.setProperty("ip_address", session.getAddress());
         user.setProperty("client_lang", clientLang);
         if (clientPlatform.equals("ios")) {
            user.setProperty("idfv", rawDeviceId);
         } else if (clientPlatform.equals("android")) {
            user.setProperty("android_id", rawDeviceId);
         } else {
            user.setProperty("android_id", rawDeviceId);
         }

         if (player.hasNewerContent(clientVer)) {
            player.setSaveAllowed(false);
            ext.sendVersionError(user, false);
            return;
         }

         PlayerFixes.Apply(player, user);
         ext.updateLastLogin((long)userID);
         ext.sendGameSettings(user);
         ext.sendAdditionalUserGameSettings(user, player.getTutorialGroup());
         ext.sendAdditionalUserGameSettings(user, player.getBattleHintGroup());
         List<UserVariable> userVars = new ArrayList();
         userVars.add(new SFSUserVariable("user_id", userID));
         this.getApi().setUserVariables(user, userVars);
         if (GameSettings.get("AUTO_JOIN") != null) {
            String autoJoinRoom = GameSettings.get("AUTO_JOIN");
            Room room = zone.getRoomByName(autoJoinRoom);
            this.getApi().joinRoom(user, room, (String)null, false, (Room)null);
         }

         if (isNewUser) {
            ext.stats.trackNewUser(user);
         }

         user.setProperty("initialized", true);
         ISFSObject response = new SFSObject();
         response.putLong("bbb_id", bbbId);
         ext.send("gs_initialized", response, user);
      } catch (Exception var36) {
         Logger.trace(var36);
      }

   }

   private int CreateNewPlayer(long bbbId, User user, String clientPlatform, VersionInfo clientVersion, String countryCode) throws Exception {
      int userID = this.CreateNewPlayerDbEntry(bbbId, clientPlatform, clientVersion, countryCode);
      String sql = "SELECT * FROM users LEFT OUTER JOIN user_tribal_starpower ON user_id=user WHERE user_id=?";
      ISFSArray result = MSMExtension.getInstance().getDB().query(sql, new Object[]{userID});
      ISFSObject playerData = result.getSFSObject(0);
      Player player = new Player(playerData, true, true);
      this.initializeNewPlayerGroups(player, user, (long)userID, clientPlatform, clientVersion);
      this.initializeFirstIsland(player);
      this.initializeEmptyQuests(player);
      return userID;
   }

   private long CreateNewBbbIdDbEntry(String userGameId) throws Exception {
      String sql = "INSERT INTO user_game_id_to_bbb_id SET user_game_id=?, date_created=NOW()";
      return MSMExtension.getInstance().getDB().insertGetId(sql, new Object[]{userGameId});
   }

   private int CreateNewPlayerDbEntry(long bbbId, String clientPlatform, VersionInfo clientVersion, String countryCode) throws Exception {
      int startCoins = GameSettings.getInt("PLAYER_START_COINS");
      int startDiamonds = GameSettings.getInt("PLAYER_START_DIAMONDS");
      int startFood = GameSettings.getInt("PLAYER_START_FOOD");
      if (clientPlatform.equals("ios")) {
         startCoins = GameSettings.getInt("PLAYER_START_COINS_IOS");
         startDiamonds = GameSettings.getInt("PLAYER_START_DIAMONDS_IOS");
         startFood = GameSettings.getInt("PLAYER_START_FOOD_IOS");
      } else if (clientPlatform.equals("android")) {
         startCoins = GameSettings.getInt("PLAYER_START_COINS_ANDROID");
         startDiamonds = GameSettings.getInt("PLAYER_START_DIAMONDS_ANDROID");
         startFood = GameSettings.getInt("PLAYER_START_FOOD_ANDROID");
      } else if (clientPlatform.equals("amazon")) {
         startCoins = GameSettings.getInt("PLAYER_START_COINS_AMAZON");
         startDiamonds = GameSettings.getInt("PLAYER_START_DIAMONDS_AMAZON");
         startFood = GameSettings.getInt("PLAYER_START_FOOD_AMAZON");
      }

      String sql;
      if (countryCode != null) {
         sql = "INSERT INTO users SET bbb_id=?, display_name='New Player', date_created=NOW(), active_island=0,coins=?, diamonds=?, food=?, last_client_version=?, client_platform=?, country=?";
         return (int)MSMExtension.getInstance().getDB().insertGetId(sql, new Object[]{bbbId, startCoins, startDiamonds, startFood, clientVersion.toString(), clientPlatform, countryCode});
      } else {
         sql = "INSERT INTO users SET bbb_id=?, display_name='New Player', date_created=NOW(), active_island=0,coins=?, diamonds=?, food=?, last_client_version=?, client_platform=?";
         return (int)MSMExtension.getInstance().getDB().insertGetId(sql, new Object[]{bbbId, startCoins, startDiamonds, startFood, clientVersion.toString(), clientPlatform});
      }
   }

   private void initializeNewPlayerGroups(Player player, User user, long userID, String clientPlatform, VersionInfo clientVersion) throws Exception {
      this.addUserToGroupTable(userID, this.selectTutorialGroupForNewUser(clientPlatform, clientVersion));
      this.addUserToGroupTable(userID, this.selectNoAdvertisementGroupForNewUser());
      this.addUserToGroupTable(userID, this.selectAdvertisement2018GroupForNewUser(clientPlatform, clientVersion));
      this.addUserToGroupTable(userID, this.selectComboPackGroupForNewUser(clientPlatform, clientVersion));
      this.addUserToGroupTable(userID, this.selectIslandPromoGroupForNewUser(clientPlatform, clientVersion));
      this.addUserToGroupTable(userID, this.selectBonusPromoGroupForNewUser(clientPlatform, clientVersion));
      this.addUserToGroupTable(userID, this.selectTapjoyTierGroupForNewUser(clientPlatform, clientVersion));
      this.addUserToGroupTable(userID, this.selectDailyDiamondGroupForNewUser(clientPlatform, clientVersion));
      this.addUserToGroupTable(userID, this.selectCurrencyExcludesForNewUser(clientPlatform, clientVersion));
      this.addUserToGroupTable(userID, this.selectRewardedAdvertisementGroupForNewUser(clientPlatform, clientVersion));
      this.addUserToGroupTable(userID, this.selectBattleHintLevelGroupForNewUser(clientPlatform, clientVersion));
      this.addUserToInitialGroupsTable(user, userID);
      MSMExtension.getInstance().initializePlayerGroupsFromUserGroupsTable(player, new Object[]{userID});
      MSMExtension.getInstance().stats.sendPlayerGroups(player);
      player.cacheTutorialGroup();
   }

   private void initializeFirstIsland(Player player) throws Exception {
      long islandId = MSMExtension.getInstance().createPlayerIsland(player);
      String sql = "UPDATE users SET active_island=? WHERE user_id=?";
      MSMExtension.getInstance().getDB().update(sql, new Object[]{islandId, player.getPlayerId()});
   }

   private void initializeEmptyQuests(Player player) throws Exception {
      String sql = "INSERT INTO user_quest_data (user, active, completed) VALUES (?, ?, ?)";
      Object[] arguments = new Object[]{player.getPlayerId(), "[]", "[]"};
      MSMExtension.getInstance().getDB().update(sql, arguments);
   }

   protected void addUserToInitialGroupsTable(User user, long userId) {
      try {
         MSMExtension ext = MSMExtension.getInstance();
         ISession session = user.getSession();
         ISFSObject initialGroups = SFSObject.newFromJsonData(GameSettings.get("INITIAL_GROUPS", "{}"));
         if (initialGroups.containsKey("groups")) {
            ISFSArray groups = initialGroups.getSFSArray("groups");
            Iterator it = groups.iterator();

            while(it.hasNext()) {
               ISFSObject o = (ISFSObject)((SFSDataWrapper)it.next()).getObject();

               try {
                  Integer group = o.getInt("group");
                  String key = o.getUtfString("key");
                  ISFSArray values = o.getSFSArray("values");
                  String s = (String)session.getProperty(key);
                  Iterator vit = values.iterator();

                  while(vit.hasNext()) {
                     String vs = (String)((SFSDataWrapper)vit.next()).getObject();
                     if (vs.equals(s)) {
                        String sql = "INSERT INTO user_groups VALUES (?, ?)";
                        Object[] arguments = new Object[]{userId, group};
                        ext.getDB().update(sql, arguments);
                        break;
                     }
                  }
               } catch (Exception var18) {
                  Logger.trace(var18, "Error checking group", o.getDump());
               }
            }
         }
      } catch (Exception var19) {
         Logger.trace(var19, "Error checking initial groups");
      }

   }

   private int selectTutorialGroupForNewUser(String clientPlatform, VersionInfo clientVersion) {
      return TutorialGroup.getGroupToAssign(1, clientVersion, clientPlatform);
   }

   private int selectNoAdvertisementGroupForNewUser() {
      return NoAdvertisementGroup.getGroupToAssign();
   }

   private int selectAdvertisement2018GroupForNewUser(String clientPlatform, VersionInfo clientVersion) {
      return Advertisement2018Group.getGroupToAssign(1, clientVersion, clientPlatform);
   }

   private int selectComboPackGroupForNewUser(String clientPlatform, VersionInfo clientVersion) {
      return ComboPackGroup.getGroupToAssign(1, clientVersion, clientPlatform);
   }

   private int selectIslandPromoGroupForNewUser(String clientPlatform, VersionInfo clientVersion) {
      return IslandPromoGroup.getGroupToAssign(1, clientVersion, clientPlatform);
   }

   private int selectBonusPromoGroupForNewUser(String clientPlatform, VersionInfo clientVersion) {
      return BonusPromoGroup.getGroupToAssign(1, clientVersion, clientPlatform);
   }

   private int selectTapjoyTierGroupForNewUser(String clientPlatform, VersionInfo clientVersion) {
      return TapjoyTierGroup.getGroupToAssign(1, clientVersion, clientPlatform);
   }

   private int selectDailyDiamondGroupForNewUser(String clientPlatform, VersionInfo clientVersion) {
      return DailyDiamondGroup.getGroupToAssign(1, clientVersion, clientPlatform);
   }

   private int selectBattleHintLevelGroupForNewUser(String clientPlatform, VersionInfo clientVersion) {
      return BattleHintLevelGroup.getGroupToAssign(1, clientVersion, clientPlatform);
   }

   private int selectCurrencyExcludesForNewUser(String clientPlatform, VersionInfo clientVersion) {
      return CurrencyGroup.getGroupToAssign(1, clientVersion, clientPlatform);
   }

   private int selectRewardedAdvertisementGroupForNewUser(String clientPlatform, VersionInfo clientVersion) {
      return RewardedAdvertisementGroup.getGroupToAssign(1, clientVersion, clientPlatform);
   }

   private void addUserToGroupTable(long userId, int groupId) {
      if (groupId != -1) {
         MSMExtension ext = MSMExtension.getInstance();

         try {
            String sql = "INSERT INTO user_groups VALUES (?, ?)";
            Object[] arguments = new Object[]{userId, groupId};
            ext.getDB().update(sql, arguments);
         } catch (Exception var7) {
            Logger.trace(var7, "Error inserting into user_groups table");
         }
      }

   }

   protected boolean userHasNewerDataThanClientVersion(Player player, VersionInfo clientVer) {
      return player.hasNewerContent(clientVer);
   }

   private String getClientLastUpdateVersion(ISession session) throws Exception {
      String lastUpdateVersion = (String)session.getProperty("last_update_version");
      if (lastUpdateVersion == null) {
         MSMExtension ext = (MSMExtension)this.getParentExtension();
         long bbbId = (Long)session.getProperty("bbb_id");
         String actualClientVersion = (String)session.getProperty("client_version");
         VersionInfo clientVer = new VersionInfo(actualClientVersion);
         String sql = "SELECT last_client_version FROM users WHERE bbb_id=?";
         Object[] args = new Object[]{bbbId};
         SFSArray userRecord = ext.getDB().query(sql, args);
         if (userRecord.size() == 0) {
            lastUpdateVersion = "";
         } else {
            String lastClientStr = userRecord.getSFSObject(0).getUtfString("last_client_version");
            VersionInfo lastClientVer = new VersionInfo(lastClientStr);
            if (lastClientVer.compareTo(clientVer) <= 0) {
               lastUpdateVersion = lastClientStr;
            } else {
               lastUpdateVersion = actualClientVersion;
            }
         }
      }

      return lastUpdateVersion != null ? lastUpdateVersion : "";
   }

   protected void userLogin(ISFSEvent event) throws SFSLoginException {
      try {
         MSMExtension ext = (MSMExtension)this.getParentExtension();
         ISession session = (ISession)event.getParameter(SFSEventParam.SESSION);
         SFSObject additionalData = (SFSObject)event.getParameter(SFSEventParam.LOGIN_IN_DATA);
         if (!additionalData.containsKey("token")) {
            throw new Exception("ERROR: user attempting to login without auth token");
         } else {
            BBBToken token = new BBBToken(BBBToken.decryptToken(additionalData.getUtfString("token"), GameSettings.get("AUTH2_ENCRYPTION_VECTOR"), GameSettings.get("AUTH2_ENCRYPTION_SECRET")));
            if (token != null && token.isTokenValid()) {
               String userGameId = (String)token.getUserGameIds().get(0);
               if (!ext.isDevServer()) {
                  ext.getCache().set("online_users_" + userGameId, 3600, ext.serverId.toString());
               }

               session.setProperty("user_game_id", userGameId);
               session.setProperty("username", token.username());
               session.setProperty("loginType", token.loginType());
               this.handleLoginData(session, additionalData);
            } else {
               throw new Exception("ERROR: user attempting to login with invalid auth token");
            }
         }
      } catch (Exception var7) {
         Logger.trace(var7);
         SFSErrorData errData = new SFSErrorData(SFSErrorCode.GENERIC_ERROR);
         errData.addParameter("04");
         throw new SFSLoginException("Generic Error", errData);
      }
   }

   protected void userLogout(ISFSEvent event) {
      try {
         this.userDisconnect(event);
      } catch (Exception var3) {
         Logger.trace(var3);
      }

   }

   protected void userDisconnect(ISFSEvent event) throws Exception {
      User user = (User)event.getParameter(SFSEventParam.USER);
      MSMExtension ext = (MSMExtension)this.getParentExtension();
      ext.savePlayer(user);
      Player player;
      if (user.getPrivilegeId() == 3) {
         player = (Player)user.getProperty("friend_object");
         if (player != null) {
            MSMExtension.getInstance().markAccountAdminEditingComplete(player.getBbbId());
         }
      }

      player = (Player)user.getProperty("player_object");
      if (player != null) {
         ISession session = user.getSession();
         Boolean versionFail = false;
         if (session.getProperty("version_fail") != null) {
            versionFail = (Boolean)session.getProperty("version_fail");
         }

         long sessionStartTime = 0L;
         if (user.getProperty("sess_start") != null) {
            sessionStartTime = (Long)user.getProperty("sess_start") / 1000L;
         }

         long sessionLength = MSMExtension.CurrentDBTime() / 1000L - sessionStartTime;
         MsmSession sessionData = new MsmSession();
         sessionData.userGameId = (String)session.getProperty("user_game_id");
         sessionData.userId = player.getPlayerId();
         sessionData.bbbId = (Long)session.getProperty("bbb_id");
         sessionData.level = player != null ? player.getLevel() : 0;
         sessionData.starpower = player != null ? player.getActualStarpower() : 0L;
         sessionData.diamonds = player != null ? player.getActualDiamonds() : 0L;
         sessionData.coins = player != null ? player.getActualCoins() : 0L;
         sessionData.ethCurrency = player != null ? player.getActualEthCurrency() : 0L;
         sessionData.food = player != null ? player.getActualFood() : 0L;
         sessionData.relics = player != null ? player.getActualRelics() : 0L;
         sessionData.keys = player != null ? player.getActualKeys() : 0L;
         sessionData.eggWildcards = player != null ? player.getActualEggWildcards() : 0L;
         sessionData.gameId = GameSettings.getInt("AUTH_GAME_ID");
         sessionData.gameServerId = ext.serverId;
         sessionData.sessionStartTime = sessionStartTime;
         sessionData.sessionLength = sessionLength;
         sessionData.versionFail = versionFail;
         sessionData.clientVersion = (String)user.getProperty("client_version");
         sessionData.clientPlatform = (String)user.getProperty("client_platform");
         sessionData.clientDevice = (String)user.getProperty("client_device");
         sessionData.clientOS = (String)user.getProperty("client_os");
         sessionData.clientIP = session.getAddress();
         sessionData.clientLang = (String)user.getProperty("client_lang");
         ext.stats.trackSession(user, sessionData);
         MSMCommandLogging.flushClientRequestLog(user);
         if (player != null) {
            player.getActiveIslandStats().islandClosed(player);
         }

         user.removeProperty("player_object");
         player = null;

         try {
            CacheWrapper cache = ext.getCache();
            String data = null;
            if (!ext.isDevServer()) {
               data = (String)cache.get("online_users_" + sessionData.userGameId);
            }

            if (data != null && data.length() > 0) {
               cache.delete("online_users_" + sessionData.userGameId);
            }
         } catch (OperationTimeoutException var14) {
            Logger.trace((Exception)var14, "Memcache operation timed out during disconnect trying to remove user from online_users");
         } catch (IllegalStateException var15) {
            Logger.trace((Exception)var15, "Memcache is in an illegal state ... not intialized on local server?");
         }

         super.userDisconnect(event);
      }
   }

   protected void userReconnectSuccess(ISFSEvent event) {
   }

   protected void userReconnectTry(ISFSEvent event) {
   }
}
