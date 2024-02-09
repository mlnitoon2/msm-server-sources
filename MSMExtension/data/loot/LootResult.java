package com.bigbluebubble.mysingingmonsters.data.loot;

import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.mysingingmonsters.GameStateHandler;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.data.RewardScaling;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.BuffTimedEvent;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.bigbluebubble.mysingingmonsters.player.PlayerIsland;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class LootResult {
   private static final String TYPE_KEY = "type";
   private static final String ID_KEY = "id";
   private static final String AMOUNT_KEY = "amount";
   private static final String EXTRA_KEY = "extra";
   private static final String DURATION_KEY = "duration";
   private static final String ISLAND_KEY = "island";
   private static final String ISLANDS_KEY = "islands";
   LootType type;
   int id;
   int amount;
   ISFSObject extra;

   public LootType getType() {
      return this.type;
   }

   public LootResult(LootType lootType, int lootId, int amount, ISFSObject extra) {
      this.type = lootType;
      this.id = lootId;
      this.amount = amount;
      this.extra = extra;
   }

   public ISFSObject toSFSObject() {
      ISFSObject obj = new SFSObject();
      obj.putInt("type", this.type.getId());
      obj.putInt("id", this.id);
      obj.putInt("amount", this.amount);
      if (this.extra != null) {
         obj.putSFSObject("extra", this.extra);
      }

      return obj;
   }

   public String toString() {
      String str = this.type.toString();
      if (this.id != 0) {
         str = str + " (" + this.id + ")";
      }

      str = str + " x" + this.amount;
      return str;
   }

   public int getTargetIsland() {
      return this.extra != null && this.extra.containsKey("island") ? this.extra.getInt("island") : 0;
   }

   public static List<LootResult> Merge(List<LootResult> resultsA, List<LootResult> resultsB) {
      Iterator var2 = resultsB.iterator();

      while(var2.hasNext()) {
         LootResult rB = (LootResult)var2.next();
         boolean found = false;
         Iterator var5 = resultsA.iterator();

         while(var5.hasNext()) {
            LootResult rA = (LootResult)var5.next();
            if (rA.type == rB.type && rA.id == rB.id) {
               found = true;
               rA.amount += rB.amount;
               break;
            }
         }

         if (!found) {
            resultsA.add(rB);
         }
      }

      return resultsA;
   }

   public static List<LootResult> ScaleToLevel(List<LootResult> results, int level) {
      Iterator var2 = results.iterator();

      while(var2.hasNext()) {
         LootResult r = (LootResult)var2.next();
         switch(r.type) {
         case COINS:
            r.amount = RewardScaling.scaleRewards(level, Player.CurrencyType.Coins, r.amount, false);
            break;
         case FOOD:
            r.amount = RewardScaling.scaleRewards(level, Player.CurrencyType.Food, r.amount, false);
         }
      }

      return results;
   }

   public static void Collect(Player p, List<LootResult> loot, User sender, GameStateHandler gs) throws Exception {
      Iterator var4 = loot.iterator();

      while(var4.hasNext()) {
         LootResult lootItem = (LootResult)var4.next();
         switch(lootItem.type) {
         case COINS:
            lootItem.amount = RewardScaling.scaleRewards(p.getLevel(), Player.CurrencyType.Coins, lootItem.amount, false);
            p.adjustCoins(sender, gs, lootItem.amount);
            break;
         case FOOD:
            lootItem.amount = RewardScaling.scaleRewards(p.getLevel(), Player.CurrencyType.Food, lootItem.amount, false);
            p.adjustFood(sender, gs, lootItem.amount);
            break;
         case DIAMONDS:
            p.adjustDiamonds(sender, gs, lootItem.amount);
            MSMExtension.getInstance().stats.trackReward(sender, "login_calendar_collect", "diamonds", (long)lootItem.amount);
            break;
         case KEYS:
            p.adjustKeys(sender, gs, lootItem.amount);
            break;
         case RELICS:
            p.adjustRelics(sender, gs, lootItem.amount);
            break;
         case SHARDS:
            p.adjustEthCurrency(sender, gs, lootItem.amount);
            break;
         case STARPOWER:
            p.adjustStarpower(sender, gs, (long)lootItem.amount);
            break;
         case COSTUME:
            PlayerIsland targetIsland = null;
            if (lootItem.extra != null && lootItem.extra.containsKey("island")) {
               targetIsland = p.getIslandByIslandIndex(lootItem.extra.getInt("island"));
            }

            if (targetIsland != null) {
               targetIsland.getCostumeState().addCredit(lootItem.id, lootItem.amount);
            } else {
               p.getCostumes().inventory().addItem(lootItem.id, lootItem.amount);
               p.saveCostumes();
            }
            break;
         case BUFF:
            TimedEventType buffEventType = TimedEventType.fromInt(lootItem.id);
            int buffAmount = lootItem.extra.getInt("amount");
            long durationMs = 86400000L;
            if (lootItem.extra.containsKey("duration")) {
               durationMs = SFSHelpers.getLong("duration", lootItem.extra);
            }

            Collection<Integer> islands = null;
            if (lootItem.extra.containsKey("islands")) {
               islands = lootItem.extra.getIntArray("islands");
            }

            BuffTimedEvent buff = new BuffTimedEvent(buffEventType, buffAmount, MSMExtension.CurrentDBTime(), MSMExtension.CurrentDBTime() + durationMs, islands);
            p.getTimedEvents().addEvent(buff);
            p.getTimedEvents().save(MSMExtension.getInstance().getDB());
            break;
         case MONSTER:
         case STRUCTURE:
            p.getInventory().addItem(lootItem.id, lootItem.amount);
            p.saveInventory();
            break;
         case SET:
         case GROUP:
         case TABLE:
         default:
            throw new Exception(" wtf bad loot!");
         }
      }

   }
}
