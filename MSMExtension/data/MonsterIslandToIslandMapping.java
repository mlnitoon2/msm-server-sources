package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class MonsterIslandToIslandMapping {
   public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, MonsterIslandData>> islandTeleportMap;
   private static ISFSArray cachedislandMapDataToSendToClient;

   public static ISFSArray sfsData() {
      return cachedislandMapDataToSendToClient;
   }

   public static void setSFSData(ISFSArray sfs) {
      cachedislandMapDataToSendToClient = sfs;
   }

   public static void init(IDbWrapper db) throws Exception {
      islandTeleportMap = new ConcurrentHashMap();
      String sql = "SELECT * FROM monster_island_2_island_map";
      ISFSArray results = db.query("SELECT * FROM monster_island_2_island_map");
      setSFSData(results);

      int sourceMonster;
      MonsterIslandData dest;
      ConcurrentHashMap curIslandMap;
      for(Iterator i = results.iterator(); i.hasNext(); curIslandMap.put(sourceMonster, dest)) {
         SFSObject mappingData = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         int sourceIsle = mappingData.getInt("source_island");
         sourceMonster = mappingData.getInt("source_monster");
         int destIsle = mappingData.getInt("dest_island");
         int destMonster = mappingData.getInt("dest_monster");
         dest = new MonsterIslandData(destIsle, destMonster);
         curIslandMap = null;
         if (islandTeleportMap.containsKey(sourceIsle)) {
            curIslandMap = (ConcurrentHashMap)islandTeleportMap.get(sourceIsle);
         } else {
            curIslandMap = new ConcurrentHashMap();
            islandTeleportMap.put(sourceIsle, curIslandMap);
         }
      }

   }

   public static MonsterIslandData get(int island, int monster) {
      ConcurrentHashMap<Integer, MonsterIslandData> curIslandMap = null;
      curIslandMap = (ConcurrentHashMap)islandTeleportMap.get(island);
      return curIslandMap != null ? (MonsterIslandData)curIslandMap.get(monster) : null;
   }

   public static boolean monsterTeleportsToIslandAs(int sourceMonsterId, int destMonsterId, int destIslandId) {
      Enumeration e = islandTeleportMap.elements();

      MonsterIslandData data;
      do {
         if (!e.hasMoreElements()) {
            return false;
         }

         ConcurrentHashMap<Integer, MonsterIslandData> curElement = (ConcurrentHashMap)e.nextElement();
         if (!curElement.containsKey(sourceMonsterId)) {
            return false;
         }

         data = (MonsterIslandData)curElement.get(sourceMonsterId);
      } while(data.monsterId != destMonsterId || data.islandId != IslandLookup.get(destIslandId).getType());

      return true;
   }

   public static boolean monsterTeleportsFromIslandAs(int destMonsterId, int sourceMonsterId, int sourceIslandId) {
      if (!islandTeleportMap.containsKey(IslandLookup.get(sourceIslandId).getType())) {
         return false;
      } else {
         ConcurrentHashMap<Integer, MonsterIslandData> fromSourceIsland = (ConcurrentHashMap)islandTeleportMap.get(IslandLookup.get(sourceIslandId).getType());
         if (!fromSourceIsland.containsKey(sourceMonsterId)) {
            return false;
         } else {
            MonsterIslandData data = (MonsterIslandData)fromSourceIsland.get(sourceMonsterId);
            return data.monsterId == destMonsterId;
         }
      }
   }

   public static int monsterSourceGivenDestIsland(int destMonster, int destIsland) {
      Enumeration sourceIslandItr = islandTeleportMap.elements();

      while(sourceIslandItr.hasMoreElements()) {
         ConcurrentHashMap<Integer, MonsterIslandData> curIsland = (ConcurrentHashMap)sourceIslandItr.nextElement();
         Set<Entry<Integer, MonsterIslandData>> sourceMonsterEntries = curIsland.entrySet();
         Iterator monsterItr = sourceMonsterEntries.iterator();

         while(monsterItr.hasNext()) {
            Entry<Integer, MonsterIslandData> curMonster = (Entry)monsterItr.next();
            MonsterIslandData destinationData = (MonsterIslandData)curMonster.getValue();
            if (destinationData.monsterId == destMonster && destinationData.islandId == destIsland) {
               return (Integer)curMonster.getKey();
            }
         }
      }

      return 0;
   }

   public static int monsterDestGivenSourceIsland(int sourceMonster, int sourceIsland) {
      ConcurrentHashMap<Integer, MonsterIslandData> map = (ConcurrentHashMap)islandTeleportMap.get(sourceIsland);
      if (map != null) {
         MonsterIslandData destData = (MonsterIslandData)map.get(sourceMonster);
         if (destData != null) {
            return destData.monsterId;
         }
      }

      return 0;
   }

   public static int monsterSourceGivenAnyIsland(int destMonster) {
      Enumeration sourceIslandItr = islandTeleportMap.elements();

      while(sourceIslandItr.hasMoreElements()) {
         ConcurrentHashMap<Integer, MonsterIslandData> curIsland = (ConcurrentHashMap)sourceIslandItr.nextElement();
         Set<Entry<Integer, MonsterIslandData>> sourceMonsterEntries = curIsland.entrySet();
         Iterator monsterItr = sourceMonsterEntries.iterator();

         while(monsterItr.hasNext()) {
            Entry<Integer, MonsterIslandData> curMonster = (Entry)monsterItr.next();
            MonsterIslandData destinationData = (MonsterIslandData)curMonster.getValue();
            if (destinationData.monsterId == destMonster) {
               return (Integer)curMonster.getKey();
            }
         }
      }

      return 0;
   }

   public static boolean equivMonstersGivenAnyIsland(int sourceMonster, int destMonster) {
      Enumeration sourceIslandItr = islandTeleportMap.elements();

      ConcurrentHashMap curIsland;
      do {
         if (!sourceIslandItr.hasMoreElements()) {
            return false;
         }

         curIsland = (ConcurrentHashMap)sourceIslandItr.nextElement();
      } while(!curIsland.containsKey(sourceMonster) || ((MonsterIslandData)curIsland.get(sourceMonster)).monsterId != destMonster);

      return true;
   }
}
