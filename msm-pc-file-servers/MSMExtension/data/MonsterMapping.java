package com.bigbluebubble.mysingingmonsters.data;

import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class MonsterMapping<T extends MonsterMapData> {
   private ConcurrentHashMap<Integer, ArrayList<T>> type1ToType2Map;
   private ConcurrentHashMap<Integer, T> type2ToType1Map;
   protected SFSArray cachedDataToSendToClient;

   public SFSArray sfsData() {
      return this.cachedDataToSendToClient;
   }

   public void init() {
      this.type1ToType2Map = new ConcurrentHashMap();
      this.type2ToType1Map = new ConcurrentHashMap();
      this.cachedDataToSendToClient = new SFSArray();
   }

   public void add(Integer type1MonsterId, T type2MonsterData) {
      ArrayList<T> curMonsterRares = null;
      if (this.type1ToType2Map.containsKey(type1MonsterId)) {
         curMonsterRares = (ArrayList)this.type1ToType2Map.get(type1MonsterId);
      } else {
         curMonsterRares = new ArrayList();
         curMonsterRares.add(type2MonsterData);
      }

      this.type1ToType2Map.put(type1MonsterId, curMonsterRares);
      this.type2ToType1Map.put(type2MonsterData.type2Id(), type2MonsterData);
   }

   protected void addSFSObjectToCache(SFSObject rareData) {
      this.cachedDataToSendToClient.addSFSObject(rareData);
   }

   public boolean isType2(int id) {
      return this.type2ToType1Map.containsKey(id);
   }

   public ArrayList<T> type1ToType2MapGet(int id) {
      return (ArrayList)this.type1ToType2Map.get(id);
   }

   public boolean type2ToType1MapContainsKey(int id) {
      return this.type2ToType1Map.containsKey(id);
   }

   public T type2ToType1MapGet(int id) {
      return (MonsterMapData)this.type2ToType1Map.get(id);
   }
}
