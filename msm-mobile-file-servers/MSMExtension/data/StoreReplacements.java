package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.player.PlayerIsland;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import java.util.ArrayList;
import java.util.Iterator;

public class StoreReplacements {
   private static ISFSArray sfsData;
   private static ArrayList<StoreReplacementItem> storeReplacementItems = new ArrayList();

   public static void init(IDbWrapper db) throws Exception {
      String sql = "SELECT * FROM entities_store_replacements";
      ISFSArray results = db.query("SELECT * FROM entities_store_replacements");
      init((ISFSArray)results);
   }

   public static void init(ISFSArray dbEntries) {
      sfsData = dbEntries;

      for(int i = 0; i < sfsData.size(); ++i) {
         ISFSObject sfsObject = sfsData.getSFSObject(i);
         String sourceEntityIds = sfsObject.getUtfString("entityIdSource");
         int numOwnedBeforeReplacement = sfsObject.getInt("numOwnedBeforeReplacement");
         int entityIdReplacement = sfsObject.getInt("entityIdReplacement");
         addReplacementItem(sourceEntityIds, numOwnedBeforeReplacement, entityIdReplacement);
      }

   }

   private static void addReplacementItem(String sourceEntityIds, int numToOwn, int replacementEntId) {
      ArrayList<Integer> sourceEntIds = new ArrayList();
      int i;
      if (!sourceEntityIds.isEmpty()) {
         ISFSArray jsonObject = SFSArray.newFromJsonData(sourceEntityIds);

         for(i = 0; i < jsonObject.size(); ++i) {
            sourceEntIds.add(jsonObject.getInt(i));
         }
      }

      StoreReplacementItem newReplacementItem = new StoreReplacementItem(sourceEntIds, numToOwn, replacementEntId);

      for(i = 0; i < sourceEntIds.size(); ++i) {
         storeReplacementItems.add(newReplacementItem);
      }

   }

   public static boolean currentlyReplacesAnotherStoreItem(int structureEntityId, PlayerIsland p) {
      StoreReplacementItem replacementData = getValueEntityId(structureEntityId);
      return replacementData != null ? replacementData.ownsEnoughToMakeReplacement(p) : false;
   }

   private static StoreReplacementItem getValueEntityId(int replacementEntId) {
      Iterator itr = storeReplacementItems.iterator();

      StoreReplacementItem replacementItem;
      do {
         if (!itr.hasNext()) {
            return null;
         }

         replacementItem = (StoreReplacementItem)itr.next();
      } while(replacementItem.replacementEntityId != replacementEntId);

      return replacementItem;
   }

   public static ISFSArray getData() {
      return sfsData;
   }
}
