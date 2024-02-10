package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticDataLookup;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StructureLookup extends StaticDataLookup<Structure> {
   private static StructureLookup instance;
   private Map<Integer, Structure> structures_ = new ConcurrentHashMap();
   private Map<Integer, Structure> structuresFromEntityId_ = new ConcurrentHashMap();
   static final String CACHE_NAME = "structures_data";

   public static StructureLookup getInstance() {
      return instance;
   }

   private StructureLookup(IDbWrapper db) throws Exception {
      this.lastChanged_ = 0L;
      String sql = "SELECT structure_id, entity_id, min_server_version, view_in_market, view_in_starmarket, premium, allowed_on_island, name, description, entity_type, graphic, size_x, size_y, level, battle_level, show_in_levelup, xp, keywords, requirements, upgrades_to, build_time, cost_coins, cost_eth_currency, cost_diamonds, cost_sale, cost_starpower, cost_keys, cost_relics, cost_medals,movable, structure_type, extra, y_offset, sound, platforms, GREATEST(entities.last_changed,structures.last_changed) AS last_changed FROM structures LEFT OUTER JOIN entities ON structures.entity=entities.entity_id";
      ISFSArray results = db.query("SELECT structure_id, entity_id, min_server_version, view_in_market, view_in_starmarket, premium, allowed_on_island, name, description, entity_type, graphic, size_x, size_y, level, battle_level, show_in_levelup, xp, keywords, requirements, upgrades_to, build_time, cost_coins, cost_eth_currency, cost_diamonds, cost_sale, cost_starpower, cost_keys, cost_relics, cost_medals,movable, structure_type, extra, y_offset, sound, platforms, GREATEST(entities.last_changed,structures.last_changed) AS last_changed FROM structures LEFT OUTER JOIN entities ON structures.entity=entities.entity_id");
      Iterator i = results.iterator();

      while(i.hasNext()) {
         SFSObject structureData = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         if (structureData != null) {
            Structure structure = new Structure(structureData);
            this.structures_.put(structure.getID(), structure);
            this.structuresFromEntityId_.put(structure.getEntityId(), structure);
            this.lastChanged_ = Math.max(this.lastChanged_, structure.lastChanged());
         }
      }

   }

   public static void init(IDbWrapper db) throws Exception {
      instance = new StructureLookup(db);
   }

   public static Structure get(int structureId) {
      return instance.getEntry(structureId);
   }

   public static Structure getFromEntityId(int entityId) {
      return !instance.structuresFromEntityId_.containsKey(entityId) ? null : (Structure)instance.structuresFromEntityId_.get(entityId);
   }

   public static Structure getNextUpgrade(Structure structure) {
      int upgradesToId = structure.getUpgradesTo();
      return upgradesToId == 0 ? null : get(structure.getUpgradesTo());
   }

   public String getCacheName() {
      return "structures_data";
   }

   public Iterable<Structure> entries() {
      return this.structures_.values();
   }

   public Structure getEntry(int id) {
      return (Structure)this.structures_.get(id);
   }
}
