package com.bigbluebubble.mysingingmonsters.task;

import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MSMAdminCatchUnreleasedAccount implements Runnable {
   public static final int numTimeUnitsToWait = 20;
   public static final TimeUnit timeUnit;
   private static ConcurrentHashMap<Long, MSMAdminCatchUnreleasedAccount> listOfLockedUsers;
   long playersBbbid;
   long dateAccessed;
   public ScheduledFuture<?> mySchedule;

   public static void addAccountToCSRPermaLockPreventionList(long bbbid, long date) {
      MSMAdminCatchUnreleasedAccount lockedAccount;
      if (listOfLockedUsers.containsKey(bbbid)) {
         lockedAccount = (MSMAdminCatchUnreleasedAccount)listOfLockedUsers.get(bbbid);
         lockedAccount.cancelSchedule();
      }

      lockedAccount = new MSMAdminCatchUnreleasedAccount(bbbid, date);
      listOfLockedUsers.put(bbbid, lockedAccount);
      lockedAccount.mySchedule = SmartFoxServer.getInstance().getTaskScheduler().schedule(lockedAccount, 20, timeUnit);
   }

   public static void resetAccountAccessTime(long bbbid) {
      if (listOfLockedUsers.containsKey(bbbid)) {
         MSMAdminCatchUnreleasedAccount oldUserEntry = (MSMAdminCatchUnreleasedAccount)listOfLockedUsers.get(bbbid);
         oldUserEntry.resetAccountAccessTime();
      }

   }

   public void resetAccountAccessTime() {
      this.cancelSchedule();
      this.dateAccessed = MSMExtension.CurrentDBTime();
      Logger.trace("resetAccountAccessTime: update date_accessed: " + this.dateAccessed);
      String sql = "UPDATE admin_user_accounts_currently_being_accessed SET date_accessed=? WHERE user_bbbid=?";
      Object[] args = new Object[]{this.dateAccessed, this.playersBbbid};

      try {
         MSMExtension.getInstance().getDB().update(sql, args);
      } catch (Exception var4) {
         Logger.trace(var4, "error updating user " + this.playersBbbid + " access time in admin_user_accounts_currently_being_accessed");
      }

      this.mySchedule = SmartFoxServer.getInstance().getTaskScheduler().schedule(this, 20, timeUnit);
   }

   public MSMAdminCatchUnreleasedAccount(long bbbid, long datetime) {
      this.playersBbbid = bbbid;
      this.dateAccessed = datetime;
      Logger.trace("new MSMAdminCatchUnreleasedAccount, set dateAccessed: " + this.dateAccessed);
   }

   public void cancelSchedule() {
      this.mySchedule.cancel(false);
   }

   public void run() {
      this.clearUnreleasedAcccount();
   }

   public static void removeFromLockedAccountsList(long bbbid) {
      MSMAdminCatchUnreleasedAccount lockedAccount = (MSMAdminCatchUnreleasedAccount)listOfLockedUsers.get(bbbid);
      if (lockedAccount != null) {
         lockedAccount.cancelSchedule();
      }

   }

   public void clearUnreleasedAcccount() {
      String sql = "SELECT date_accessed FROM admin_user_accounts_currently_being_accessed WHERE user_bbbid=?";
      Object[] args = new Object[]{this.playersBbbid};

      try {
         ISFSArray results = MSMExtension.getInstance().getDB().query(sql, args);
         if (results.size() > 0) {
            long dbDateAccessed = results.getSFSObject(0).getLong("date_accessed");
            Logger.trace("date_accessed from db: " + dbDateAccessed);
            Logger.trace("date_accessed from previously saved value: " + this.dateAccessed);
            if (dbDateAccessed == this.dateAccessed) {
               MSMExtension.getInstance().markAccountAdminEditingComplete(this.playersBbbid);
            }
         }
      } catch (Exception var6) {
         Logger.trace(var6, "error checking csr accessed user " + this.playersBbbid + " from admin_user_accounts_currently_being_accessed after 3 hours...");
      }

   }

   static {
      timeUnit = TimeUnit.MINUTES;
      listOfLockedUsers = new ConcurrentHashMap();
   }
}
