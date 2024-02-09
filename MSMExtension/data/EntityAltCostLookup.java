package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityAltCostLookup {
   private static EntityAltCostLookup instance;
   public static Map<Integer, Map<Integer, EntityAltCost>> altCosts;
   private static ISFSArray cachedToSendToClient;

   public static EntityAltCostLookup getInstance() {
      return instance;
   }

   private EntityAltCostLookup(IDbWrapper db) throws Exception {
      altCosts = new ConcurrentHashMap();
      String sql = "SELECT * FROM entities_alternate_cost";
      ISFSArray results = db.query("SELECT * FROM entities_alternate_cost");
      setSFSData(results);
      Iterator i = results.iterator();

      while(i.hasNext()) {
         SFSObject data = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         if (data != null) {
            this.add(new EntityAltCost(data));
         }
      }

   }

   public static void init(IDbWrapper db) throws Exception {
      instance = new EntityAltCostLookup(db);
   }

   public static ISFSArray sfsData() {
      return cachedToSendToClient;
   }

   public static void setSFSData(ISFSArray data) {
      cachedToSendToClient = data;
   }

   public void add(EntityAltCost eac) throws Exception {
      if (altCosts.containsKey(eac.entityId)) {
         Map<Integer, EntityAltCost> islandData = (Map)altCosts.get(eac.entityId);
         if (islandData.containsKey(eac.islandId)) {
            throw new Exception("adding duplicate EntityAltCost for entity " + eac.entityId + " on island " + eac.islandId);
         }

         islandData.put(eac.islandId, eac);
      } else {
         Map<Integer, EntityAltCost> islandData = new ConcurrentHashMap();
         islandData.put(eac.islandId, eac);
         altCosts.put(eac.entityId, islandData);
      }

   }

   public static boolean hasAltCost(Integer entityId, Integer islandId) {
      return altCosts.containsKey(entityId) && ((Map)altCosts.get(entityId)).containsKey(islandId);
   }

   public static EntityAltCost get(Integer entityId, Integer islandId) {
      return altCosts.containsKey(entityId) && ((Map)altCosts.get(entityId)).containsKey(islandId) ? (EntityAltCost)((Map)altCosts.get(entityId)).get(islandId) : null;
   }
}
