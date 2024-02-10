package com.bigbluebubble.BBBServer.util;

import com.smartfoxserver.v2.db.DBConfig;
import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.db.SFSDBManager;
import com.smartfoxserver.v2.entities.Zone;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.extensions.ISFSExtension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DbWrapper implements IDbWrapper {
   public static final int SLOW_QUERY_THRESHOLD = 400;
   public static final int SLOW_QUERY_OUTPUT_MAX_LENGTH = 2000;
   private IDBManager mainDb;
   private ISFSExtension parentExtenion;
   private boolean timeQueries = true;
   private boolean inDebugMode = false;
   private String logExtention;
   private String extName;

   public void setDebugMode(boolean debugOn) {
      this.inDebugMode = debugOn;
   }

   public void setTimeQueries(boolean timeQueriesOn) {
      this.timeQueries = timeQueriesOn;
   }

   public DbWrapper(ISFSExtension extension, String logExtention_) {
      this.logExtention = logExtention_;

      try {
         this.parentExtenion = extension;
         this.mainDb = this.parentExtenion.getParentZone().getDBManager();
         SimpleLogger.trace("db isActive: " + this.mainDb.isActive());
         String[] extPieces = this.parentExtenion.getClass().toString().split(" ");
         this.extName = extPieces[1];
      } catch (Exception var4) {
         SimpleLogger.trace(var4);
      }

   }

   public DbWrapper(ISFSExtension extension, DBConfig dbConfig, Zone zone) {
      try {
         this.parentExtenion = extension;
         this.mainDb = new SFSDBManager(dbConfig);
         this.mainDb.init(zone);
         SimpleLogger.trace("db isActive: " + this.mainDb.isActive() + " => " + dbConfig.connectionString);
         String[] extPieces = this.parentExtenion.getClass().toString().split(" ");
         this.extName = extPieces[1];
      } catch (Exception var5) {
         SimpleLogger.trace(var5);
      }

   }

   public SFSArray query(String sql) throws Exception {
      return this.query(sql, new Object[0]);
   }

   private void traceQuery(String msg, long executionTime, String sql, Object[] params) {
      String[] tokens = sql.split("\\?");
      String sqlQuery = new String();
      int tokenIndex = 0;
      String[] var9 = tokens;
      int var10 = tokens.length;

      for(int var11 = 0; var11 < var10; ++var11) {
         String s = var9[var11];
         sqlQuery = sqlQuery + s;
         if (tokenIndex < params.length) {
            sqlQuery = sqlQuery + params[tokenIndex];
            ++tokenIndex;
         }
      }

      if (this.extName.equals(this.logExtention) && executionTime > 400L) {
         String slowQuery;
         if (sqlQuery.length() > 2000) {
            slowQuery = sqlQuery.substring(0, 2000);
         } else {
            slowQuery = sqlQuery;
         }

         SimpleLogger.trace(LogLevel.DEBUG, "DB_TIMED_ACTION\t" + msg + "\t" + executionTime + "\t" + slowQuery);
      }

   }

   private void traceQuery(String msg, String sql, Object[] params) {
      String[] tokens = sql.split("\\?");
      String sqlQuery = new String();
      int tokenIndex = 0;
      String[] var7 = tokens;
      int var8 = tokens.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         String s = var7[var9];
         sqlQuery = sqlQuery + s;
         if (tokenIndex < params.length) {
            sqlQuery = sqlQuery + params[tokenIndex];
            ++tokenIndex;
         }
      }

      SimpleLogger.trace(LogLevel.DEBUG, msg + sqlQuery);
   }

   public SFSArray query(String sql, Object[] params) throws Exception {
      try {
         if (this.inDebugMode && !this.timeQueries) {
            this.traceQuery("querying db with: ", sql, params);
         }

         SFSArray res;
         if (this.timeQueries) {
            long startTime = System.currentTimeMillis();
            res = (SFSArray)this.mainDb.executeQuery(sql, params);
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            this.traceQuery("QUERY", totalTime, sql, params);
         } else {
            res = (SFSArray)this.mainDb.executeQuery(sql, params);
         }

         return res;
      } catch (Exception var10) {
         SimpleLogger.trace(var10);
         throw var10;
      }
   }

   public boolean exists(String sql, Object[] params) throws Exception {
      try {
         if (this.inDebugMode && !this.timeQueries) {
            this.traceQuery("checking db existance with: ", sql, params);
         }

         SFSArray res;
         if (this.timeQueries) {
            long startTime = System.currentTimeMillis();
            res = (SFSArray)this.mainDb.executeQuery(sql, params);
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            this.traceQuery("EXISTS", totalTime, sql, params);
         } else {
            res = (SFSArray)this.mainDb.executeQuery(sql, params);
         }

         return res.size() > 0;
      } catch (Exception var10) {
         SimpleLogger.trace(var10);
         throw var10;
      }
   }

   public void update(String sql) throws Exception {
      this.update(sql, new Object[0]);
   }

   public void update(String sql, Object[] params) throws Exception {
      try {
         if (this.inDebugMode && !this.timeQueries) {
            this.traceQuery("updating db with: ", sql, params);
         }

         if (this.timeQueries) {
            long startTime = System.currentTimeMillis();
            this.mainDb.executeUpdate(sql, params);
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            this.traceQuery("UPDATE", totalTime, sql, params);
         } else {
            this.mainDb.executeUpdate(sql, params);
         }

      } catch (Exception var9) {
         SimpleLogger.trace(var9);
         throw var9;
      }
   }

   public long insertGetId(String sql) throws Exception {
      return this.insertGetId(sql, new Object[0]);
   }

   public long insertGetId(String sql, Object[] params) throws Exception {
      Connection conn = null;
      PreparedStatement stmt = null;
      long newId = -1L;

      try {
         if (this.inDebugMode && !this.timeQueries) {
            this.traceQuery("inserting into db with: ", sql, params);
         }

         conn = this.mainDb.getConnection();
         stmt = conn.prepareStatement(sql, 1);

         for(int x = 1; x <= params.length; ++x) {
            stmt.setString(x, params[x - 1].toString());
         }

         if (this.timeQueries) {
            long startTime = System.currentTimeMillis();
            stmt.executeUpdate();
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            this.traceQuery("INSERT", totalTime, sql, params);
         } else {
            stmt.executeUpdate();
         }

         for(ResultSet keyResult = stmt.getGeneratedKeys(); keyResult.next(); newId = keyResult.getLong(1)) {
         }

         return newId;
      } catch (Exception var16) {
         SimpleLogger.trace(var16);
         throw var16;
      } finally {
         if (stmt != null) {
            stmt.close();
         }

         if (conn != null) {
            conn.close();
         }

      }
   }

   public long insertGetIdNoBruno(String sql, Object[] params) throws Exception {
      Connection conn = null;
      PreparedStatement stmt = null;
      long newId = -1L;

      try {
         if (this.inDebugMode && !this.timeQueries) {
            this.traceQuery("inserting into db with: ", sql, params);
         }

         conn = this.mainDb.getConnection();
         stmt = conn.prepareStatement(sql, 1);

         for(int x = 1; x <= params.length; ++x) {
            stmt.setObject(x, params[x - 1]);
         }

         if (this.timeQueries) {
            long startTime = System.currentTimeMillis();
            stmt.executeUpdate();
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            this.traceQuery("INSERT", totalTime, sql, params);
         } else {
            stmt.executeUpdate();
         }

         for(ResultSet keyResult = stmt.getGeneratedKeys(); keyResult.next(); newId = keyResult.getLong(1)) {
         }

         return newId;
      } catch (Exception var16) {
         SimpleLogger.trace(var16);
         throw var16;
      } finally {
         if (stmt != null) {
            stmt.close();
         }

         if (conn != null) {
            conn.close();
         }

      }
   }

   public void transactionBegin() throws Exception {
      Connection conn = null;

      try {
         conn = this.mainDb.getConnection();
         conn.setAutoCommit(false);
      } catch (Exception var6) {
         SimpleLogger.trace(var6);
         if (conn != null) {
            conn.close();
         }

         throw var6;
      } finally {
         ;
      }
   }

   public void transactionEnd() throws Exception {
      Connection conn = null;

      try {
         conn = this.mainDb.getConnection();
         conn.commit();
         conn.setAutoCommit(true);
      } catch (Exception var6) {
         SimpleLogger.trace(var6);
         throw var6;
      } finally {
         if (conn != null) {
            conn.close();
         }

      }

   }

   public void transactionRollback() {
      Connection conn = null;

      try {
         conn = this.mainDb.getConnection();
         conn.rollback();
         conn.setAutoCommit(true);
      } catch (Exception var11) {
         SimpleLogger.trace(var11);
      } finally {
         if (conn != null) {
            try {
               conn.close();
            } catch (Exception var10) {
            }
         }

      }

   }
}
