package com.bigbluebubble.mysingingmonsters.battle;

import com.bigbluebubble.mysingingmonsters.player.Player;
import com.bigbluebubble.mysingingmonsters.player.PlayerMonster;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BattleLoadout {
   ISFSObject data;
   public static final String[] SlotKeys = new String[]{"slot0", "slot1", "slot2"};

   public BattleLoadout() {
      this.data = new SFSObject();

      for(int i = 0; i < SlotKeys.length; ++i) {
         this.data.putLong(SlotKeys[i], 0L);
      }

   }

   public BattleLoadout(ISFSObject data) {
      this.data = data;

      for(int i = 0; i < SlotKeys.length; ++i) {
         if (!data.containsKey(SlotKeys[i])) {
            data.putLong(SlotKeys[i], 0L);
         }
      }

   }

   public long getSlot(int idx) {
      return this.data.getLong(SlotKeys[idx]);
   }

   public void setSlot(int idx, long uniqueMonsterId) {
      this.data.putLong(SlotKeys[idx], uniqueMonsterId);
   }

   public ISFSObject toSFSObject() {
      return this.data;
   }

   public Iterable<Long> slots() {
      return new Iterable<Long>() {
         public Iterator<Long> iterator() {
            return new Iterator<Long>() {
               int pos = 0;

               public boolean hasNext() {
                  return this.pos < BattleLoadout.SlotKeys.length;
               }

               public Long next() {
                  if (!this.hasNext()) {
                     throw new NoSuchElementException();
                  } else {
                     return BattleLoadout.this.getSlot(this.pos++);
                  }
               }

               public void remove() {
                  throw new UnsupportedOperationException("remove");
               }
            };
         }
      };
   }

   public BattleOpponents convertToBattleOpponents(Player player, int loadoutSize) {
      BattleOpponents battleOpponents = new BattleOpponents();

      for(int i = 0; i < loadoutSize; ++i) {
         long uniqueMonsterId = this.getSlot(i);
         PlayerMonster monster = player.getActiveIsland().getMonsterByID(uniqueMonsterId);
         BattleOpponentData opponentData = new BattleOpponentData();
         opponentData.setId(monster.getType());
         opponentData.setLevel(monster.getLevel());
         opponentData.setCostume(monster.getCostumeState().getEquipped());
         battleOpponents.add(opponentData);
      }

      return battleOpponents;
   }
}
