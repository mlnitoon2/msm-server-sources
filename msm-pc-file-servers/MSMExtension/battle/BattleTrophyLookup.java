package com.bigbluebubble.mysingingmonsters.battle;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class BattleTrophyLookup {
   public static final String ID_KEY = "id";
   public static final String ENTITY_KEY = "entity_id";
   public static final String MESSAGE_ID_KEY = "mail_message_id";
   public static ConcurrentHashMap<Integer, ISFSObject> trophies_;

   public static void init(IDbWrapper db) throws Exception {
      trophies_ = new ConcurrentHashMap();
      String sql = "SELECT * from battle_trophies";
      ISFSArray results = db.query(sql);
      Iterator i = results.iterator();

      while(i.hasNext()) {
         ISFSObject obj = (ISFSObject)((SFSDataWrapper)i.next()).getObject();
         trophies_.put(obj.getInt("id"), obj);
      }

   }

   public static long getMailboxIdFromTrophyId(int id) {
      if (trophies_.containsKey(id)) {
         ISFSObject obj = (ISFSObject)trophies_.get(id);
         if (obj == null) {
            return 0L;
         }

         if (obj.containsKey("mail_message_id")) {
            return obj.getLong("mail_message_id");
         }
      }

      return 0L;
   }

   public static int getEntityIdForTrophy(int trophyId) {
      if (trophies_.containsKey(trophyId)) {
         ISFSObject obj = (ISFSObject)trophies_.get(trophyId);
         if (obj != null && obj.containsKey("entity_id")) {
            return obj.getInt("entity_id");
         }
      }

      return 0;
   }
}
