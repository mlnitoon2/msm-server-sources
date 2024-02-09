package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticData;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Arrays;
import java.util.Iterator;

public class Island extends StaticData {
   protected static final String ID_KEY = "island_id";
   protected static final String TYPE_KEY = "island_type";
   protected static final String GRAPHIC_KEY = "graphic";
   protected static final String LAST_CHANGED_KEY = "last_changed";
   protected static final String COST_COINS_KEY = "cost_coins";
   protected static final String COST_DIAMONDS_KEY = "cost_diamonds";
   protected static final String COST_STARPOWER_KEY = "cost_starpower";
   protected static final String CASTLE_STRUCTURE_KEY = "castle_structure_id";
   protected static final String MONSTERS_KEY = "monsters";
   protected static final String MIN_VER_KEY = "min_server_version";
   VersionInfo maxVersion;

   public Island(ISFSObject islandData) throws Exception {
      super(islandData);
      this.minVersion = new VersionInfo(this.data.getUtfString("min_server_version"));
      this.maxVersion = this.getMaxVersionedMonsters();
      String graphicString = this.data.getUtfString("graphic");
      if (graphicString != null && graphicString.length() > 0) {
         this.data.putSFSObject("graphic", SFSObject.newFromJsonData(graphicString));
      }

   }

   public int getID() {
      return this.data.getInt("island_id");
   }

   public int getType() {
      return this.data.getInt("island_type");
   }

   public VersionInfo maxVersion() {
      return this.maxVersion;
   }

   public ISFSArray getMonsters() {
      return this.data.getSFSArray("monsters");
   }

   private VersionInfo getMaxVersionedMonsters() throws Exception {
      ISFSArray islandMonsters = this.getMonsters();
      Iterator<SFSDataWrapper> it = islandMonsters.iterator();
      VersionInfo maxVer = new VersionInfo("0.0.0");

      while(it.hasNext()) {
         ISFSObject s = (ISFSObject)((SFSDataWrapper)it.next()).getObject();
         Integer m = s.getInt("monster");
         if (m == null) {
            throw new Exception("Invalid data");
         }

         Monster staticMonster = MonsterLookup.get(m);
         VersionInfo monsterServerVersion = staticMonster.minVersion();
         if (maxVer.compareTo(monsterServerVersion) < 0) {
            maxVer = monsterServerVersion;
         }
      }

      return maxVer;
   }

   public boolean hasMonster(int monsterId) {
      Iterator i = this.data.getSFSArray("monsters").iterator();

      ISFSObject monster;
      do {
         if (!i.hasNext()) {
            return false;
         }

         monster = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
      } while(monster.getInt("monster") != monsterId);

      return true;
   }

   public int getCastleStructureId() {
      return this.data.getInt("castle_structure_id");
   }

   public int getCoinCost() {
      return this.data.getInt("cost_coins");
   }

   public int getStarpowerCost() {
      return this.data.getInt("cost_starpower");
   }

   public int getDiamondCost() {
      return this.data.getInt("cost_diamonds");
   }

   public static ISFSObject getValidIslandDataForUser(Island i, String clientPlatform, String clientSubplatform, VersionInfo maxSupportedServerVersion) throws Exception {
      ISFSObject newIslandData = SFSHelpers.clone(i.getData());
      ISFSArray monsters = newIslandData.getSFSArray("monsters");
      if (monsters != null) {
         ISFSArray newMonsters = new SFSArray();
         Iterator it = monsters.iterator();

         while(it.hasNext()) {
            try {
               ISFSObject s = (ISFSObject)((SFSDataWrapper)it.next()).getObject();
               VersionInfo minVersion = new VersionInfo(s.getUtfString("min_version"));
               if (maxSupportedServerVersion.compareTo(minVersion) >= 0) {
                  Integer m = s.getInt("monster");
                  Monster staticMonster = MonsterLookup.get(m);
                  if (staticMonster.supportedByClient(clientPlatform, clientSubplatform, maxSupportedServerVersion)) {
                     newMonsters.addSFSObject(s);
                  }
               }
            } catch (Exception var12) {
               Logger.trace(var12);
            }
         }

         newIslandData.putSFSArray("monsters", newMonsters);
      }

      return newIslandData;
   }

   public Monster getMonsterWithGenes(String genes) {
      ISFSArray monsters = this.getMonsters();

      for(int i = 0; i < monsters.size(); ++i) {
         ISFSObject s = monsters.getSFSObject(i);
         Integer m = s.getInt("monster");
         Monster staticMonster = MonsterLookup.get(m);
         String monsterGenes = staticMonster.getGenes();
         if (genes.length() == monsterGenes.length()) {
            char[] genesArray = genes.toCharArray();
            char[] monsterGenesArray = monsterGenes.toCharArray();
            Arrays.sort(genesArray);
            Arrays.sort(monsterGenesArray);
            if (Arrays.equals(genesArray, monsterGenesArray)) {
               return staticMonster;
            }
         }
      }

      return null;
   }
}
