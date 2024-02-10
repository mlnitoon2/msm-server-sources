package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticDataLookup;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MonsterLookup extends StaticDataLookup<Monster> {
   private static MonsterLookup instance;
   private Map<Integer, Monster> monsters_ = new ConcurrentHashMap();
   private Map<Integer, Monster> monstersFromEntityId_ = new ConcurrentHashMap();
   public static ISFSArray prizes = null;
   public static double minimumPrizeTimer = 0.0D;
   public static double globalPrizeRollover = 0.0D;
   public static double monsterPrizeRollover = 0.0D;
   public static int maximumDailyPrizes = 0;
   public static double prizeDuration = 0.0D;
   public static List<Integer> cantBeBredMonsters;
   static final String CACHE_NAME = "monsters_data";

   public static MonsterLookup getInstance() {
      return instance;
   }

   private MonsterLookup(IDbWrapper db) throws Exception {
      this.lastChanged_ = 0L;
      String SELECT_MONSTERS_SQL = "SELECT monsters.monster_id, entities.entity_id, min_server_version, view_in_market, view_in_starmarket, premium, name, entities.common_name, description, entity_type, graphic, sfx, size_x, size_y, level, xp, requirements, build_time, beds, genes, spore_graphic, select_sound, portrait_graphic, levelup_island, class, cost_coins, cost_eth_currency, cost_diamonds, cost_sale, cost_starpower, cost_keys, cost_relics, cost_medals,movable, happiness, y_offset, names, GREATEST(entities.last_changed, monsters.last_changed) AS last_changed, box_monster_requirements, evolve_requirements,evolve_req_flexeggs,evolve_into, evolve_enabled,crucible_access_key_cost,evolve_gfx,evolve_sfx, monsters_seasonal.monster_id, season_event_name, month_string, sigil_graphic, fanfare_graphic, keywords FROM monsters LEFT OUTER JOIN entities ON monsters.entity=entities.entity_id LEFT OUTER JOIN monsters_seasonal ON monsters.monster_id=monsters_seasonal.monster_id LEFT OUTER JOIN monster_evolutions ON monsters.entity=monster_evolutions.entity_id";
      ISFSArray results = db.query("SELECT monsters.monster_id, entities.entity_id, min_server_version, view_in_market, view_in_starmarket, premium, name, entities.common_name, description, entity_type, graphic, sfx, size_x, size_y, level, xp, requirements, build_time, beds, genes, spore_graphic, select_sound, portrait_graphic, levelup_island, class, cost_coins, cost_eth_currency, cost_diamonds, cost_sale, cost_starpower, cost_keys, cost_relics, cost_medals,movable, happiness, y_offset, names, GREATEST(entities.last_changed, monsters.last_changed) AS last_changed, box_monster_requirements, evolve_requirements,evolve_req_flexeggs,evolve_into, evolve_enabled,crucible_access_key_cost,evolve_gfx,evolve_sfx, monsters_seasonal.monster_id, season_event_name, month_string, sigil_graphic, fanfare_graphic, keywords FROM monsters LEFT OUTER JOIN entities ON monsters.entity=entities.entity_id LEFT OUTER JOIN monsters_seasonal ON monsters.monster_id=monsters_seasonal.monster_id LEFT OUTER JOIN monster_evolutions ON monsters.entity=monster_evolutions.entity_id");
      Iterator i = results.iterator();

      while(true) {
         SFSObject monsterData;
         do {
            if (!i.hasNext()) {
               this.initPrizes(db);
               this.initCantBreeds(db);
               return;
            }

            monsterData = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         } while(monsterData == null);

         String SELECT_MONSTER_LEVELS_SQL = "SELECT * FROM monster_levels WHERE monster=?";
         ISFSArray levels = db.query("SELECT * FROM monster_levels WHERE monster=?", new Object[]{monsterData.getInt("monster_id")});
         String SELECT_UNDERLING_FILL_TIMES_SQL;
         if (levels == null || levels.size() == 0) {
            SELECT_UNDERLING_FILL_TIMES_SQL = "SELECT * FROM monster_levels_underling WHERE monster=?";
            levels = db.query("SELECT * FROM monster_levels_underling WHERE monster=?", new Object[]{monsterData.getInt("monster_id")});
         }

         if (levels == null || levels.size() == 0) {
            SELECT_UNDERLING_FILL_TIMES_SQL = "SELECT * FROM monster_levels_amber WHERE monster=?";
            levels = db.query("SELECT * FROM monster_levels_amber WHERE monster=?", new Object[]{monsterData.getInt("monster_id")});
         }

         SELECT_UNDERLING_FILL_TIMES_SQL = "SELECT * FROM underling_fill_times WHERE monster_id=?";
         ISFSArray fillTimes = db.query("SELECT * FROM underling_fill_times WHERE monster_id=?", new Object[]{monsterData.getInt("monster_id")});
         if (fillTimes != null && fillTimes.size() > 0) {
            monsterData.putInt("time_to_fill_sec", fillTimes.getSFSObject(0).getInt("time_to_fill_sec"));
         } else {
            monsterData.putInt("time_to_fill_sec", -1);
         }

         Monster monster;
         if (levels != null && levels.size() > 0) {
            monster = new Monster(monsterData, levels);
         } else {
            monster = new Monster(monsterData);
         }

         this.monsters_.put(monster.getMonsterID(), monster);
         this.monstersFromEntityId_.put(monster.getEntityId(), monster);
         this.lastChanged_ = Math.max(this.lastChanged_, monster.lastChanged());
      }
   }

   private void initPrizes(IDbWrapper db) throws Exception {
      String sql = "SELECT * FROM monster_prizes";
      ISFSArray results = db.query("SELECT * FROM monster_prizes");
      prizes = results;
      minimumPrizeTimer = GameSettings.getDouble("MINIMUM_PRIZE_TIMER");
      globalPrizeRollover = GameSettings.getDouble("GLOBAL_PRIZE_WINDOW_DURATION");
      monsterPrizeRollover = GameSettings.getDouble("MONSTER_PRIZE_WINDOW_DURATION");
      maximumDailyPrizes = GameSettings.getInt("MAXIMUM_DAILY_PRIZES");
      prizeDuration = GameSettings.getDouble("PRIZE_ROLLOVER_DURATION");
   }

   private void initCantBreeds(IDbWrapper db) throws Exception {
      cantBeBredMonsters = new ArrayList();
      String sql = "SELECT monsters.monster_id FROM monsters JOIN entities ON entity_id=entity WHERE monster_id NOT IN (SELECT DISTINCT result FROM breeding_combinations) AND monster_id NOT IN (SELECT monster FROM island_monsters WHERE island = 11) AND entity_id != 93 AND monster_id NOT IN (SELECT rare_monster_id FROM monsters_rare_mapping WHERE probability!=0.0)";
      ISFSArray results = db.query("SELECT monsters.monster_id FROM monsters JOIN entities ON entity_id=entity WHERE monster_id NOT IN (SELECT DISTINCT result FROM breeding_combinations) AND monster_id NOT IN (SELECT monster FROM island_monsters WHERE island = 11) AND entity_id != 93 AND monster_id NOT IN (SELECT rare_monster_id FROM monsters_rare_mapping WHERE probability!=0.0)");
      Iterator i = results.iterator();

      while(i.hasNext()) {
         SFSObject data = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         cantBeBredMonsters.add(data.getInt("monster_id"));
      }

   }

   public static void init(IDbWrapper db) throws Exception {
      instance = new MonsterLookup(db);
   }

   public static Monster get(int monsterId) {
      return instance.getEntry(monsterId);
   }

   public static Monster getFromEntityId(int entityId) {
      return !instance.monstersFromEntityId_.containsKey(entityId) ? null : (Monster)instance.monstersFromEntityId_.get(entityId);
   }

   public static Monster get(String genes) {
      Iterator var1 = instance.entries().iterator();

      Monster monster;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         monster = (Monster)var1.next();
      } while(!monster.getGenes().equalsIgnoreCase(genes));

      return monster;
   }

   public String getCacheName() {
      return "monsters_data";
   }

   public Iterable<Monster> entries() {
      return this.monsters_.values();
   }

   public Monster getEntry(int id) {
      if (!this.monsters_.containsKey(id)) {
         Logger.trace("Monster not found:" + id);
      }

      return (Monster)this.monsters_.get(id);
   }
}
