package com.bigbluebubble.mysingingmonsters.battle;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.mysingingmonsters.Helpers;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.data.MonsterLookup;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.bigbluebubble.mysingingmonsters.player.PlayerIsland;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;

public class BattleFriendPvp {
   public static final int MAX_TEAM_SIZE = 3;

   public static boolean validateFriendBattle(User sender, Player player, PlayerIsland playerIsland, long opponentFriendBbbId) throws Exception {
      if (!playerIsland.isBattleIsland()) {
         MSMExtension.getInstance().sendErrorResponse("battle_start_friend", "Can't start a battle on this island", sender);
         return false;
      } else {
         BattleIslandCampaignState battleIslandCampaignState = playerIsland.getBattleIslandState().getCampaignState();
         if (player.getBattleState() != null && battleIslandCampaignState.hasCompletedCampaign(GameSettings.get("USER_PVP_CAMPAIGN_DEPENDS", 6))) {
            String sql = "SELECT * FROM user_friends WHERE (user_1=? AND user_2=?) OR (user_2=? AND user_1=?) ";
            Object[] args = new Object[]{player.getBbbId(), opponentFriendBbbId, player.getBbbId(), opponentFriendBbbId};
            ISFSArray results = MSMExtension.getInstance().getDB().query(sql, args);
            if (results.size() == 0) {
               MSMExtension.getInstance().sendErrorResponse("battle_start_friend", "Users are not friends", sender);
               return false;
            } else {
               return true;
            }
         } else {
            MSMExtension.getInstance().sendErrorResponse("battle_start_friend", "User battle level not high enough", sender);
            return false;
         }
      }
   }

   public static SFSArray getFriendPvpLoadout(User sender, long opponentFriendUserId) throws Exception {
      String sql = "SELECT loadout FROM user_battle_pvp WHERE user_id=? AND started_on IN (SELECT MAX(started_on) FROM user_battle_pvp WHERE user_id=?)";
      Object[] args = new Object[]{opponentFriendUserId, opponentFriendUserId};
      ISFSArray results = MSMExtension.getInstance().getDB().query(sql, args);
      if (results.size() > 1) {
         MSMExtension.getInstance().sendErrorResponse("battle_start_friend", "more than one recent campaign by friend", sender);
         return null;
      } else {
         return results.size() > 0 ? SFSArray.newFromJsonData(results.getSFSObject(0).getUtfString("loadout")) : null;
      }
   }

   public static SFSArray getFriendSoloCampaignLoadout(User sender, long opponentFriendUserId) throws Exception {
      ISFSObject loadoutSfs = getFriendSoloCampaignLoadoutSfs(sender, opponentFriendUserId);
      long slot0Uid = SFSHelpers.getLong(BattleLoadout.SlotKeys[0], loadoutSfs);
      long slot1Uid = SFSHelpers.getLong(BattleLoadout.SlotKeys[1], loadoutSfs);
      long slot2Uid = SFSHelpers.getLong(BattleLoadout.SlotKeys[2], loadoutSfs);
      long islandUid = getFriendBattleIslandUid(sender, opponentFriendUserId);
      ISFSArray monstersArr = getFriendIslandMonsters(sender, opponentFriendUserId, islandUid);
      SFSArray opponentLoadout = new SFSArray();
      Iterator i = monstersArr.iterator();

      while(true) {
         ISFSObject monsterSfs;
         while(i.hasNext()) {
            monsterSfs = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
            Long monsterUid = SFSHelpers.getLong("user_monster_id", monsterSfs);
            if (slot0Uid != 0L && slot0Uid == monsterUid) {
               opponentLoadout.addSFSObject(loadOpponentData(monsterSfs));
            } else if (slot1Uid != 0L && slot1Uid == monsterUid) {
               opponentLoadout.addSFSObject(loadOpponentData(monsterSfs));
            } else if (slot2Uid != 0L && slot2Uid == monsterUid) {
               opponentLoadout.addSFSObject(loadOpponentData(monsterSfs));
            }
         }

         if (opponentLoadout.size() == 0) {
            i = monstersArr.iterator();

            while(i.hasNext() && opponentLoadout.size() < BattleLoadout.SlotKeys.length) {
               monsterSfs = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
               opponentLoadout.addSFSObject(loadOpponentData(monsterSfs));
            }
         }

         if (opponentLoadout.size() == 0) {
            opponentLoadout.addSFSObject(loadOpponentData((ISFSObject)null));
         }

         return opponentLoadout;
      }
   }

   private static ISFSObject getFriendSoloCampaignLoadoutSfs(User sender, long opponentFriendUserId) throws Exception {
      String sql = "SELECT loadout FROM user_battle WHERE user_id = ?";
      Object[] args = new Object[]{opponentFriendUserId};
      ISFSArray results = MSMExtension.getInstance().getDB().query(sql, args);
      if (results.size() == 0) {
         MSMExtension.getInstance().sendErrorResponse("battle_start_friend", "friend doesn't have battle data", sender);
         return null;
      } else {
         return SFSObject.newFromJsonData(results.getSFSObject(0).getUtfString("loadout"));
      }
   }

   private static long getFriendBattleIslandUid(User sender, long opponentFriendUserId) throws Exception {
      String sql = "SELECT user_island_id FROM user_islands WHERE user = ? AND island=20";
      Object[] args = new Object[]{opponentFriendUserId};
      SFSArray results = MSMExtension.getInstance().getDB().query(sql, args);
      if (results.size() == 0) {
         MSMExtension.getInstance().sendErrorResponse("battle_start_friend", "unexpected error retrieving friend data", sender);
         return 0L;
      } else {
         return results.getSFSObject(0).getLong("user_island_id");
      }
   }

   private static ISFSArray getFriendIslandMonsters(User sender, long opponentFriendUserId, long islandUid) throws Exception {
      ISFSArray monsterData = null;
      String sql = "SELECT monsters FROM user_island_data WHERE user = ? AND island=?";
      Object[] args = new Object[]{opponentFriendUserId, islandUid};
      SFSArray results = MSMExtension.getInstance().getDB().query(sql, args);
      if (results.size() > 0) {
         ISFSObject compactIslandData = results.getSFSObject(0);
         String jsonMonsterData = Helpers.decompressJsonDataField(compactIslandData.getUtfString("monsters"), "[]");
         monsterData = SFSArray.newFromJsonData(jsonMonsterData);
      }

      return monsterData;
   }

   private static SFSObject loadOpponentData(ISFSObject islandMonsterData) {
      SFSObject slot = new SFSObject();
      if (islandMonsterData != null) {
         slot.putInt("monsterId", islandMonsterData.getInt("monster"));
         slot.putInt("level", islandMonsterData.getInt("level"));
         slot.putUtfString("name", islandMonsterData.getUtfString("name"));
         if (islandMonsterData.containsKey("costume")) {
            ISFSObject costumeSFS = islandMonsterData.getSFSObject("costume");
            if (costumeSFS.containsKey("eq")) {
               slot.putInt("costumeId", costumeSFS.getInt("eq"));
            }
         }
      } else {
         slot.putInt("monsterId", 5);
         slot.putInt("level", 1);
         slot.putUtfString("name", MonsterLookup.get(5).generateRandomMonsterName());
      }

      return slot;
   }
}
