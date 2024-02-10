package com.bigbluebubble.mysingingmonsters.battle.verification;

import com.bigbluebubble.mysingingmonsters.battle.BattleMonsterActionData;
import com.bigbluebubble.mysingingmonsters.battle.BattleMonsterActionLookup;
import com.bigbluebubble.mysingingmonsters.battle.BattleMonsterStatData;
import com.bigbluebubble.mysingingmonsters.battle.BattleMonsterStatLookup;
import com.bigbluebubble.mysingingmonsters.costumes.CostumeData;
import com.bigbluebubble.mysingingmonsters.costumes.CostumeLookup;
import com.bigbluebubble.mysingingmonsters.data.Monster;
import com.bigbluebubble.mysingingmonsters.data.MonsterLookup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BattlePlayer {
   public BattleMonsterStatData monsterStatData;
   public int level;
   public int health;
   public int totalHealth;
   public List<BattleMonsterActionData> actions;
   public List<BattleState.MonsterElement> elements;
   public Map<BuffType, IBattleBuff> buffs;
   private static final BattleState.MonsterElement[] ETHEREAL_ELEMENTS;

   public BattlePlayer(int monsterId, int level, int costumeId) {
      this.monsterStatData = BattleMonsterStatLookup.get(monsterId);
      this.elements = new ArrayList();
      this.buffs = new HashMap();
      Monster monster = MonsterLookup.get(this.monsterStatData.getMonsterId());
      String genes = monster.getGenes();

      for(int i = 0; i < genes.length(); ++i) {
         this.elements.add(BattleState.GeneToElement(genes.charAt(i)));
      }

      this.level = level;
      this.totalHealth = this.health = BattleState.GetBattleMonsterStaminaForLevel(this.monsterStatData.getBaseStamina(), level);
      this.actions = this.monsterStatData.getActions();
      CostumeData costume = CostumeLookup.get(costumeId);
      if (costume != null && costume.action() > 0) {
         BattleMonsterActionData overrideAction = BattleMonsterActionLookup.get(costume.action());
         if (overrideAction.replaces() >= this.actions.size()) {
            this.actions.add(overrideAction);
         } else {
            this.actions.set(overrideAction.replaces(), overrideAction);
         }
      }

   }

   public List<BattleState.MonsterElement> getMonsterElements() {
      return this.elements;
   }

   public boolean hasElement(BattleState.MonsterElement element) {
      Iterator var2 = this.elements.iterator();

      BattleState.MonsterElement e;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         e = (BattleState.MonsterElement)var2.next();
      } while(element != e);

      return true;
   }

   public boolean isEtherealElement(BattleState.MonsterElement element) {
      BattleState.MonsterElement[] var2 = ETHEREAL_ELEMENTS;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BattleState.MonsterElement etherealElement = var2[var4];
         if (element == etherealElement) {
            return true;
         }
      }

      return false;
   }

   public boolean isEthereal() {
      Iterator var1 = this.elements.iterator();

      BattleState.MonsterElement element;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         element = (BattleState.MonsterElement)var1.next();
      } while(!this.isEtherealElement(element));

      return true;
   }

   public void applyDamage(int damage) {
      this.health = Math.min(Math.max(0, this.health - damage), this.totalHealth);
   }

   public boolean isDead() {
      return this.health == 0;
   }

   public boolean isActionLocked(int actionIdx) {
      return this.level < actionIdx * 5;
   }

   public void addBuff(IBattleBuff buff) {
      if (!this.buffs.containsKey(buff.getType())) {
         this.buffs.put(buff.getType(), buff);
      } else {
         ((IBattleBuff)this.buffs.get(buff.getType())).overwrite(buff);
      }

   }

   public void onAction(BattleState battleState, BattlePlayer defender, BattleMonsterActionData action, BattleActionResult actionResult) {
      Iterator var5 = this.buffs.values().iterator();

      while(var5.hasNext()) {
         IBattleBuff buff = (IBattleBuff)var5.next();
         buff.onAttack(battleState, this, defender, action, actionResult);
      }

   }

   public void onEndTurn(BattleState battleState) {
      Iterator iterator = this.buffs.entrySet().iterator();

      while(iterator.hasNext()) {
         Entry<BuffType, IBattleBuff> entry = (Entry)iterator.next();
         if (((IBattleBuff)entry.getValue()).isExpired()) {
            iterator.remove();
         }
      }

   }

   static {
      ETHEREAL_ELEMENTS = new BattleState.MonsterElement[]{BattleState.MonsterElement.Plasma, BattleState.MonsterElement.Shadow, BattleState.MonsterElement.Mech, BattleState.MonsterElement.Crystal, BattleState.MonsterElement.Poison};
   }
}
