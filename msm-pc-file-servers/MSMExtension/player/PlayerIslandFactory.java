package com.bigbluebubble.mysingingmonsters.player;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.data.IslandLookup;
import com.bigbluebubble.mysingingmonsters.data.Monster;
import com.bigbluebubble.mysingingmonsters.data.MonsterLookup;
import com.bigbluebubble.mysingingmonsters.data.groups.TutorialGroup;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.awt.Point;

public class PlayerIslandFactory {
   static final Point castlePosition = new Point(29, 9);
   static final Point defaultNurseryPosition = new Point(35, 17);
   static final int basicNurseryStructureId = 1;
   static final Point defaultBreedingStructurePosition = new Point(21, 3);
   static final int basicBreedingStructureId = 2;
   static final Point defaultNogginPosition = new Point(23, 12);
   static final int nogginMonsterId = 3;
   static final int tutorialMonsterLevel = 4;
   static final Point defaultMammottPosition = new Point(27, 16);
   static final int mammottMonsterId = 5;
   static final int basicBakeryStructureId = 32;
   static final int toejammerMonsterId = 4;
   static final int basicCrucibleStructureId = 711;
   static final int basicAttunerStructureId = 856;
   static final int basicSynthesizerStructureId = 857;
   public static final int BATTLE_INITIAL_MONSTER_ID = 5;
   static final int BATTLE_INITIAL_MONSTER_STARTING_LEVEL = 4;
   static final Point BATTLE_INITIAL_MONSTER_POSITION = new Point(23, 12);
   static final int BATTLE_HOTEL_STRUCTURE_ID = 534;
   static final Point BATTLE_HOTEL_POSITION = new Point(29, 9);
   static final int BATTLE_GYM_STRUCTURE_ID = 533;
   static final Point BATTLE_GYM_POSITION = new Point(21, 3);
   static final int BATTLE_NURSERY_STRUCTURE_ID = 535;
   static final Point defaultComposerMammottPosition = new Point(10, 28);
   static final int composerMammottMonsterId = 204;
   static final int[] obsPosX = new int[]{14, 5, 9, 22, 7, 6, 16, 5, 7, 11, 13, 10, 20, 15, 12, 16, 22, 11, 26, 24, 18, 23, 3, 16, 2, 8, 14, 28, 6, 16, 13, 2, 22, 19, 11, 13, 28, 29, 22, 19, 6, 14, 26, 11, 12, 17, 21, 10, 7, 17, 1, 15, 9, 19, 25, 14, 21, 19, 10, 9};
   static final int[] obsPosY = new int[]{12, 26, 21, 32, 17, 14, 27, 23, 27, 9, 21, 28, 35, 17, 31, 22, 26, 17, 31, 33, 29, 28, 20, 34, 23, 32, 33, 22, 31, 10, 23, 18, 23, 12, 13, 29, 20, 24, 9, 32, 21, 10, 22, 20, 34, 13, 25, 24, 9, 36, 20, 36, 9, 37, 21, 26, 29, 22, 14, 34};
   static final int[] obsIdOffsets = new int[]{0, 5, 3, 4, 1, 2, 4, 3, 0, 5, 0, 2, 1, 3, 0, 1, 3, 4, 5, 0, 1, 2, 3, 0, 1, 3, 4, 0, 1, 2, 3, 5, 4, 0, 1, 1, 3, 1, 0, 1, 4, 0, 0, 2, 0, 3, 0, 0, 3, 4, 0, 3, 0, 0, 1, 3, 0, 0, 3, 1};
   static final int[] obsPosX13 = new int[]{14, 5, 9, 22, 7, 6, 16, 5, 7, 11, 13, 10, 20, 15, 12, 16, 22, 11, 26, 24, 18, 23, 3, 16, 2, 8, 14, 28, 13, 22, 19, 11, 13, 28, 29, 22, 19, 6, 14, 26, 11, 12, 17, 21, 10, 7, 17, 15, 9, 19, 25, 14, 21, 19, 10};
   static final int[] obsPosY13 = new int[]{12, 26, 21, 32, 17, 14, 27, 23, 27, 9, 21, 28, 35, 17, 31, 22, 26, 17, 31, 33, 29, 28, 20, 34, 23, 32, 33, 22, 23, 23, 12, 13, 29, 20, 24, 9, 32, 21, 10, 22, 20, 34, 13, 25, 24, 9, 36, 36, 9, 37, 21, 26, 29, 22, 14};
   static final int[] obstacles13 = new int[]{0, 5, 3, 4, 1, 2, 4, 3, 0, 5, 0, 2, 1, 3, 0, 1, 3, 4, 5, 0, 1, 2, 3, 0, 1, 3, 4, 0, 3, 4, 0, 1, 1, 3, 1, 0, 1, 4, 0, 0, 2, 0, 3, 0, 0, 3, 4, 3, 0, 0, 1, 3, 0, 0, 3};

   public static int streamlinedBakeryStructId() {
      return 32;
   }

   public static int streamlinedToejammerMonstId() {
      return 4;
   }

   public static InitialPlayerIslandData CreateIslandInitialStructures(Player player, int islandIndex, long islandId) throws Exception {
      if (islandIndex == 1) {
         return CreateTutorialIsland(player, islandIndex, islandId);
      } else {
         switch(IslandLookup.get(islandIndex).getType()) {
         case 11:
            return CreateComposerIsland(player, islandIndex, islandId);
         case 20:
            return CreateBattleIsland(player, islandIndex, islandId);
         case 22:
            return CreateAmberIsland(player, islandIndex, islandId);
         case 24:
            return CreateEtherealWorkshopIsland(player, islandIndex, islandId);
         default:
            return CreateTypicalIsland(player, islandIndex, islandId);
         }
      }
   }

   private static InitialPlayerIslandData CreateTutorialIsland(Player player, int islandIndex, long islandId) throws Exception {
      try {
         TutorialGroup t = player.getTutorialGroup();
         if (t != null) {
            return t.CallIslandInitializationFunction(player, islandIndex, islandId);
         }

         CreateBasicTutorialIsland(player, islandIndex, islandId);
      } catch (Exception var5) {
         Logger.trace(var5);
      }

      return CreateBasicTutorialIsland(player, islandIndex, islandId);
   }

   public static InitialPlayerIslandData CreateBasicTutorialIsland(Player player, int islandIndex, long islandId) {
      return CreateTypicalIsland(player, islandIndex, islandId);
   }

   public static InitialPlayerIslandData CreateBreedingTutorialIsland(Player player, int islandIndex, long islandId) {
      long currentTimestamp = MSMExtension.CurrentDBTime();
      SFSArray structures = new SFSArray();
      long nextUniqueStructureId = 1L;
      SFSObject castle = createCastle(islandIndex, islandId, nextUniqueStructureId++, currentTimestamp);
      structures.addSFSObject(castle);
      SFSObject nursery = createNursery(islandIndex, islandId, nextUniqueStructureId, currentTimestamp);
      if (nursery != null) {
         structures.addSFSObject(nursery);
         ++nextUniqueStructureId;
      }

      nextUniqueStructureId = createIslandObstacles(structures, islandIndex, islandId, nextUniqueStructureId, currentTimestamp);
      SFSObject breedingStructure = createBreedingStructure(islandIndex, islandId, nextUniqueStructureId, currentTimestamp);
      if (breedingStructure != null) {
         structures.addSFSObject(breedingStructure);
         ++nextUniqueStructureId;
      }

      SFSArray monsters = new SFSArray();
      SFSObject noggin = createNoggin(4, islandIndex, islandId, player.getNextMonsterIndex(), currentTimestamp);
      monsters.addSFSObject(noggin);
      SFSObject mammott = createTutorialMammott(4, islandIndex, islandId, player.getNextMonsterIndex(), currentTimestamp);
      monsters.addSFSObject(mammott);
      return new InitialPlayerIslandData(structures, monsters, (ISFSObject)null);
   }

   private static InitialPlayerIslandData CreateTypicalIsland(Player player, int islandIndex, long islandId) {
      long currentTimestamp = MSMExtension.CurrentDBTime();
      SFSArray structures = new SFSArray();
      long nextUniqueStructureId = 1L;
      if (islandIndex != 10 && islandIndex != 12) {
         SFSObject castle = createCastle(islandIndex, islandId, nextUniqueStructureId++, currentTimestamp);
         structures.addSFSObject(castle);
         SFSObject nursery = createNursery(islandIndex, islandId, nextUniqueStructureId, currentTimestamp);
         if (nursery != null) {
            structures.addSFSObject(nursery);
            ++nextUniqueStructureId;
         }

         if (islandIndex != 6 && islandIndex != 9) {
            SFSObject breedingStructure = createBreedingStructure(islandIndex, islandId, nextUniqueStructureId, currentTimestamp);
            if (breedingStructure != null) {
               structures.addSFSObject(breedingStructure);
               ++nextUniqueStructureId;
            }
         }
      }

      createIslandObstacles(structures, islandIndex, islandId, nextUniqueStructureId, currentTimestamp);
      return new InitialPlayerIslandData(structures, (SFSArray)null, (ISFSObject)null);
   }

   private static InitialPlayerIslandData CreateComposerIsland(Player player, int islandIndex, long islandId) throws Exception {
      long currentTimestamp = MSMExtension.CurrentDBTime();
      SFSArray structures = new SFSArray();
      long nextUniqueStructureId = 1L;
      SFSObject castle = createCastle(islandIndex, islandId, nextUniqueStructureId++, currentTimestamp);
      structures.addSFSObject(castle);
      SFSArray monsters = new SFSArray();
      if (islandIndex == 11) {
         SFSObject mammott = createComposerMammott(player, 1, islandIndex, islandId, player.getNextMonsterIndex(), currentTimestamp);
         monsters.addSFSObject(mammott);
      }

      return new InitialPlayerIslandData(structures, monsters, (ISFSObject)null);
   }

   private static InitialPlayerIslandData CreateBattleIsland(Player player, int islandIndex, long islandId) throws Exception {
      long currentTimestamp = MSMExtension.CurrentDBTime();
      SFSArray monsters = new SFSArray();
      SFSObject initialMonster = createMonster(5, 4, BATTLE_INITIAL_MONSTER_POSITION.x, BATTLE_INITIAL_MONSTER_POSITION.y, 0, islandIndex, islandId, player.getNextMonsterIndex(), currentTimestamp);
      monsters.addSFSObject(initialMonster);
      SFSArray structures = new SFSArray();
      long nextUniqueStructureId = 1L;
      SFSObject nursery = createStructure(535, defaultNurseryPosition.x, defaultNurseryPosition.y, 0, islandId, nextUniqueStructureId++, currentTimestamp);
      structures.addSFSObject(nursery);
      SFSObject battleHotel = createStructure(534, BATTLE_HOTEL_POSITION.x, BATTLE_HOTEL_POSITION.y, 0, islandId, nextUniqueStructureId++, currentTimestamp);
      structures.addSFSObject(battleHotel);
      SFSObject gym = createStructure(533, BATTLE_GYM_POSITION.x, BATTLE_GYM_POSITION.y, 0, islandId, nextUniqueStructureId++, currentTimestamp);
      structures.addSFSObject(gym);
      return new InitialPlayerIslandData(structures, monsters, (ISFSObject)null);
   }

   private static InitialPlayerIslandData CreateAmberIsland(Player player, int islandIndex, long islandId) throws Exception {
      long currentTimestamp = MSMExtension.CurrentDBTime();
      SFSArray structures = new SFSArray();
      long nextUniqueStructureId = 1L;
      SFSObject castle = createCastle(islandIndex, islandId, nextUniqueStructureId++, currentTimestamp);
      structures.addSFSObject(castle);
      SFSObject nursery = createNursery(islandIndex, islandId, nextUniqueStructureId, currentTimestamp);
      if (nursery != null) {
         structures.addSFSObject(nursery);
         ++nextUniqueStructureId;
      }

      SFSObject crucible = createCrucible(islandIndex, islandId, nextUniqueStructureId++, currentTimestamp);
      structures.addSFSObject(crucible);
      createIslandObstacles(structures, islandIndex, islandId, nextUniqueStructureId, currentTimestamp);
      return new InitialPlayerIslandData(structures, (SFSArray)null, (ISFSObject)null);
   }

   private static InitialPlayerIslandData CreateEtherealWorkshopIsland(Player player, int islandIndex, long islandId) throws Exception {
      long currentTimestamp = MSMExtension.CurrentDBTime();
      ISFSObject volatiles = SFSObject.newFromJsonData(GameSettings.get("ETHEREAL_WORKSHOP_INTIAL_VOLATILE_DATA"));
      SFSArray structures = new SFSArray();
      long nextUniqueStructureId = 1L;
      SFSObject castle = createCastle(islandIndex, islandId, nextUniqueStructureId++, currentTimestamp);
      structures.addSFSObject(castle);
      SFSObject attuner = createAttuner(islandIndex, islandId, nextUniqueStructureId++, currentTimestamp);
      structures.addSFSObject(attuner);
      SFSObject synthesizer = createSyntheszier(islandIndex, islandId, nextUniqueStructureId++, currentTimestamp);
      structures.addSFSObject(synthesizer);
      createIslandObstacles(structures, islandIndex, islandId, nextUniqueStructureId, currentTimestamp);
      return new InitialPlayerIslandData(structures, (SFSArray)null, volatiles);
   }

   private static SFSObject createAttuner(int islandIndex, long islandId, long nextUniqueStructureId, long currentTimestamp) {
      return createStructure(856, defaultBreedingStructurePosition.x, defaultBreedingStructurePosition.y, 0, islandId, nextUniqueStructureId, currentTimestamp);
   }

   private static SFSObject createSyntheszier(int islandIndex, long islandId, long nextUniqueStructureId, long currentTimestamp) {
      return createStructure(857, defaultNurseryPosition.x, defaultNurseryPosition.y, 0, islandId, nextUniqueStructureId, currentTimestamp);
   }

   private static SFSObject createCrucible(int islandIndex, long islandId, long nextUniqueStructureId, long currentTimestamp) {
      return createStructure(711, defaultBreedingStructurePosition.x, defaultBreedingStructurePosition.y, 0, islandId, nextUniqueStructureId, currentTimestamp);
   }

   private static SFSObject createCastle(int islandIndex, long islandId, long nextUniqueStructureId, long currentTimestamp) {
      return createStructure(IslandLookup.get(islandIndex).getCastleStructureId(), castlePosition.x, castlePosition.y, 0, islandId, nextUniqueStructureId, currentTimestamp);
   }

   private static SFSObject createNursery(int islandIndex, long islandId, long nextUniqueStructureId, long currentTimestamp) {
      SFSObject newStructure = null;
      if (islandIndex != 6 && islandIndex != 11) {
         newStructure = createStructure(1, defaultNurseryPosition.x, defaultNurseryPosition.y, 0, islandId, nextUniqueStructureId, currentTimestamp);
      }

      return newStructure;
   }

   private static SFSObject createBreedingStructure(int islandIndex, long islandId, long nextUniqueStructureId, long currentTimestamp) {
      SFSObject newStructure = null;
      if (islandIndex != 6 && islandIndex != 11) {
         newStructure = createStructure(2, defaultBreedingStructurePosition.x, defaultBreedingStructurePosition.y, 0, islandId, nextUniqueStructureId, currentTimestamp);
      }

      return newStructure;
   }

   private static SFSObject createNoggin(int level, int islandIndex, long islandId, long nextUniqueMonsterId, long currentTimestamp) {
      SFSObject newMonsterData = createMonster(3, level, defaultNogginPosition.x, defaultNogginPosition.y, 0, islandIndex, islandId, nextUniqueMonsterId, currentTimestamp);
      return newMonsterData;
   }

   private static SFSObject createTutorialMammott(int level, int islandIndex, long islandId, long nextUniqueMonsterId, long currentTimestamp) {
      SFSObject newMonsterData = createMonster(5, level, defaultMammottPosition.x, defaultMammottPosition.y, 0, islandIndex, islandId, nextUniqueMonsterId, currentTimestamp);
      return newMonsterData;
   }

   private static SFSObject createComposerMammott(Player player, int level, int islandIndex, long islandId, long nextUniqueMonsterId, long currentTimestamp) throws Exception {
      SFSObject newMonsterData = createMonster(204, level, defaultComposerMammottPosition.x, defaultComposerMammottPosition.y, 0, islandIndex, islandId, nextUniqueMonsterId, currentTimestamp);
      String sql = "INSERT INTO user_tracks SET user=?, bintrack='', format=1";
      long trackID = MSMExtension.getInstance().getDB().insertGetId(sql, new Object[]{player.getPlayerId()});
      ISFSObject trackObj = new SFSObject();
      trackObj.putLong("user_track_id", trackID);
      trackObj.putLong("user", player.getPlayerId());
      trackObj.putByteArray("bintrack", new byte[0]);
      player.getTracks().addSFSObject(trackObj);
      ISFSArray songs = player.getSongs();
      boolean found = false;

      for(int i = 0; i < songs.size(); ++i) {
         ISFSObject song = songs.getSFSObject(i);
         if (song.getLong("island") == islandId) {
            found = true;
            break;
         }
      }

      if (!found) {
         sql = "INSERT INTO user_songs SET island=?, user=?, tracks='[]'";
         MSMExtension.getInstance().getDB().update(sql, new Object[]{islandId, player.getPlayerId()});
      }

      MSMExtension.getInstance().initializePlayerMusic(player, new Object[]{player.getPlayerId()});
      songs = player.getSongs();
      ISFSObject songObject = new SFSObject();
      songObject.putLong("monster", newMonsterData.getLong("user_monster_id"));
      songObject.putLong("track", trackID);

      for(int i = 0; i < songs.size(); ++i) {
         ISFSObject song = songs.getSFSObject(i);
         if (song.getLong("island") == islandId) {
            song.getSFSArray("tracks").addSFSObject(songObject);
            sql = "UPDATE user_songs SET tracks=? WHERE island=?";
            MSMExtension.getInstance().getDB().update(sql, new Object[]{song.getSFSArray("tracks").toJson(), islandId});
            break;
         }
      }

      return newMonsterData;
   }

   private static SFSObject createMonster(int monsterId, int level, int x, int y, int flip, int islandIndex, long islandId, long nextUniqueMonsterId, long currentTimestamp) {
      Monster monster = MonsterLookup.get(monsterId);
      SFSObject newMonsterData = PlayerMonster.createMonsterSFS(monsterId, monster.generateRandomMonsterName(), (ISFSObject)null, (ISFSObject)null, islandIndex, islandId, x, y, flip, nextUniqueMonsterId, level, currentTimestamp, currentTimestamp, false, 0L, (String)null, false, false);
      return newMonsterData;
   }

   private static long createIslandObstacles(SFSArray structures, int islandIndex, long islandId, long nextUniqueStructureId, long currentTimestamp) {
      int obstacleBase = PlayerStructure.getObstacleStructIdStartFromIslandIndex(islandIndex);
      if (obstacleBase != -1) {
         int[] posX = null;
         int[] posY = null;
         int[] obstacles = null;
         int[] posY;
         int[] obstacles;
         int[] posX;
         if (islandIndex != 13) {
            posX = obsPosX;
            posY = obsPosY;
            obstacles = obsIdOffsets;
         } else {
            posX = obsPosX13;
            posY = obsPosY13;
            obstacles = obstacles13;
         }

         for(int x = 0; x < posX.length; ++x) {
            SFSObject newStructure = createObstacle(obstacleBase + obstacles[x], posX[x], posY[x], 0, islandId, nextUniqueStructureId++);
            structures.addSFSObject(newStructure);
         }
      }

      return nextUniqueStructureId;
   }

   private static SFSObject createStructure(int structureId, int x, int y, int flip, long islandId, long nextUniqueStructureId, long currentTimestamp) {
      SFSObject newStructure = createNoCompletionStructure(structureId, x, y, flip, islandId, nextUniqueStructureId);
      newStructure.putLong("date_created", currentTimestamp);
      newStructure.putLong("building_completed", currentTimestamp);
      newStructure.putLong("last_collection", currentTimestamp);
      return newStructure;
   }

   private static SFSObject createObstacle(int structureId, int x, int y, int flip, long islandId, long nextUniqueStructureId) {
      return createNoCompletionStructure(structureId, x, y, flip, islandId, nextUniqueStructureId);
   }

   private static SFSObject createNoCompletionStructure(int structureId, int x, int y, int flip, long islandId, long nextUniqueStructureId) {
      SFSObject newStructure = new SFSObject();
      newStructure.putLong("user_structure_id", nextUniqueStructureId);
      newStructure.putLong("island", islandId);
      newStructure.putLong("structure", (long)structureId);
      newStructure.putInt("pos_x", x);
      newStructure.putInt("pos_y", y);
      newStructure.putDouble("scale", 1.0D);
      newStructure.putInt("flip", flip);
      newStructure.putInt("is_complete", 1);
      return newStructure;
   }
}
