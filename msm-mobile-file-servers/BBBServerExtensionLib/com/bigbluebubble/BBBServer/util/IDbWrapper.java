package com.bigbluebubble.BBBServer.util;

import com.smartfoxserver.v2.entities.data.SFSArray;

public interface IDbWrapper {
   void setDebugMode(boolean var1);

   void setTimeQueries(boolean var1);

   SFSArray query(String var1) throws Exception;

   SFSArray query(String var1, Object[] var2) throws Exception;

   boolean exists(String var1, Object[] var2) throws Exception;

   void update(String var1) throws Exception;

   void update(String var1, Object[] var2) throws Exception;

   long insertGetId(String var1) throws Exception;

   long insertGetId(String var1, Object[] var2) throws Exception;

   long insertGetIdNoBruno(String var1, Object[] var2) throws Exception;

   void transactionBegin() throws Exception;

   void transactionEnd() throws Exception;

   void transactionRollback();
}
