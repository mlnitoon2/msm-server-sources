package com.bigbluebubble.mysingingmonsters.costumes;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.CostumeAvailabilityEvent;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticDataLookup;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CostumeLookup extends StaticDataLookup<CostumeData> {
   public static CostumeLookup instance;
   private Map<Integer, CostumeData> costumes_ = new HashMap();
   static final String CACHE_NAME = "costume_data";

   private CostumeLookup(IDbWrapper db) throws Exception {
      this.lastChanged_ = 0L;
      if (db != null) {
         String sql = "SELECT * from costumes";
         ISFSArray results = db.query(sql);

         CostumeData costumeData;
         for(Iterator i = results.iterator(); i.hasNext(); this.lastChanged_ = Math.max(this.lastChanged_, costumeData.lastChanged())) {
            costumeData = new CostumeData((ISFSObject)((SFSDataWrapper)i.next()).getObject());
            this.costumes_.put(costumeData.getId(), costumeData);
         }
      }

   }

   public static void init(IDbWrapper db) throws Exception {
      instance = new CostumeLookup(db);
   }

   public static CostumeData get(int id) {
      return instance != null ? instance.getEntry(id) : null;
   }

   public String getCacheName() {
      return "costume_data";
   }

   public Iterable<CostumeData> entries() {
      return this.costumes_.values();
   }

   public CostumeData getEntry(int id) {
      return (CostumeData)this.costumes_.get(id);
   }

   public List<CostumeData> getCostumesForMonster(Player player, int monsterId, int islandType, String clientPlatform, String clientSubplatform, VersionInfo maxSupportedServerVersion) {
      List<CostumeData> costumes = new ArrayList();
      Iterator var8 = this.entries().iterator();

      while(true) {
         CostumeData costumeData;
         do {
            do {
               do {
                  if (!var8.hasNext()) {
                     return costumes;
                  }

                  costumeData = (CostumeData)var8.next();
               } while(costumeData.getMonster() != monsterId);
            } while(!costumeData.supportedByClient(clientPlatform, clientSubplatform, maxSupportedServerVersion));
         } while(costumeData.hidden() && !CostumeAvailabilityEvent.hasTimedEventNow(costumeData.getId(), player, islandType));

         costumes.add(costumeData);
      }
   }
}
