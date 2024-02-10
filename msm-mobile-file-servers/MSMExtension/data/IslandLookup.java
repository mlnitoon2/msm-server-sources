package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticDataLookup;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IslandLookup extends StaticDataLookup<Island> {
   private static IslandLookup instance;
   private Map<Integer, Island> islands_;
   public static int maxIslandTypeIndex = 0;
   static final String CACHE_NAME = "islands_data";

   public static IslandLookup getInstance() {
      return instance;
   }

   private IslandLookup(IDbWrapper db) throws Exception {
      maxIslandTypeIndex = 0;
      this.islands_ = new ConcurrentHashMap();
      this.lastChanged_ = 0L;
      String SELECT_ISLANDS_SQL = "SELECT * FROM islands";
      ISFSArray results = db.query("SELECT * FROM islands");
      Iterator i = results.iterator();

      while(i.hasNext()) {
         SFSObject islandData = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         if (islandData != null) {
            String SELECT_ISLAND_MONSTERS_SQL = "SELECT monster, instrument, book_x, book_y, book_z, book_flip, min_version, bom  FROM island_monsters WHERE island=?";
            Object[] params = new Object[]{islandData.getInt("island_type")};
            ISFSArray monster_results = db.query("SELECT monster, instrument, book_x, book_y, book_z, book_flip, min_version, bom  FROM island_monsters WHERE island=?", params);
            islandData.putSFSArray("monsters", monster_results);
            String SELECT_ISLAND_STRUCTURES_SQL = "SELECT structure, instrument, min_version FROM island_structures WHERE island=?";
            ISFSArray structure_results = db.query("SELECT structure, instrument, min_version FROM island_structures WHERE island=?", params);
            islandData.putSFSArray("structures", structure_results);
            Island island = new Island(islandData);
            if (island.getType() > maxIslandTypeIndex) {
               maxIslandTypeIndex = island.getType();
            }

            this.islands_.put(island.getID(), island);
            this.lastChanged_ = Math.max(this.lastChanged_, island.lastChanged());
         }
      }

   }

   public static void init(IDbWrapper db) throws Exception {
      instance = new IslandLookup(db);
   }

   public static Island get(int islandId) {
      return instance.getEntry(islandId);
   }

   public String getCacheName() {
      return "islands_data";
   }

   public Iterable<Island> entries() {
      return this.islands_.values();
   }

   public Island getEntry(int id) {
      return (Island)this.islands_.get(id);
   }
}
