package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.mysingingmonsters.player.PlayerIsland;
import com.bigbluebubble.mysingingmonsters.player.PlayerMonster;

public class FlexEgg {
   int monsterId;
   int flexDefId;

   public FlexEgg(int monsterId, int flexDefId) {
      this.monsterId = monsterId;
      this.flexDefId = flexDefId;
   }

   public int diamondFillCost(PlayerMonster boxMonster, PlayerIsland activeIsland) {
      return this.monsterId != 0 ? getDiamondFillCostForAnyStaticEgg(boxMonster, activeIsland) : MonsterFlexEggDefLookup.getInstance().getEntry(this.flexDefId).diamondFillCost();
   }

   public int eggWildcardFillCost() {
      if (this.monsterId == 0) {
         EggRequirements requirements = MonsterFlexEggDefLookup.getInstance().getEntry(this.flexDefId).def();
         return getEggWildcardFillCost(requirements.numGenes(), requirements.getMonsterRarity(), requirements.hasMagicalGene(), requirements.hasFireGene(), requirements.hasEtherealGene(), requirements.hasMythicalGene(), requirements.isSeasonal(), requirements.hasWublinGene());
      } else {
         Monster monsterData = MonsterLookup.get(this.monsterId);
         boolean hasMagicalGene = monsterData.hasGene("R") || monsterData.hasGene("Y") || monsterData.hasGene("V") || monsterData.hasGene("W");
         boolean hasEtherealGene = monsterData.hasGene("G") || monsterData.hasGene("J") || monsterData.hasGene("K") || monsterData.hasGene("L") || monsterData.hasGene("M");
         boolean hasMythicalGene = monsterData.hasGene("P") || monsterData.hasGene("H");
         return getEggWildcardFillCost(monsterData.getGenes().length(), EggRequirements.getMonsterRarity(this.monsterId), hasMagicalGene, monsterData.hasGene("N"), hasEtherealGene, hasMythicalGene, monsterData.isSeasonalMonster(), monsterData.hasGene("U"));
      }
   }

   private static int getDiamondFillCostForAnyStaticEgg(PlayerMonster boxMonster, PlayerIsland activeIsland) {
      boolean isGoldIsland = activeIsland.isGoldIsland();
      int diamondsPerMonster = false;
      int diamondsPerMonster;
      if (isGoldIsland) {
         if (boxMonster.isEpic()) {
            diamondsPerMonster = GameSettings.get("GOLD_EPIC_BOX_INVENTORY_DIAMOND_PRICE_PER_MONSTER", 500);
         } else if (boxMonster.isRare()) {
            diamondsPerMonster = GameSettings.getInt("GOLD_RARE_BOX_INVENTORY_DIAMOND_PRICE_PER_MONSTER");
         } else {
            diamondsPerMonster = GameSettings.getInt("GOLD_BOX_INVENTORY_DIAMOND_PRICE_PER_MONSTER");
         }
      } else if (activeIsland.isEtherealIslandWithModifiers()) {
         if (boxMonster.isRare()) {
            diamondsPerMonster = GameSettings.getInt("RARE_ETHEREAL_BOX_INVENTORY_DIAMOND_PRICE_PER_MONSTER");
         } else {
            diamondsPerMonster = GameSettings.getInt("ETHEREAL_BOX_INVENTORY_DIAMOND_PRICE_PER_MONSTER");
         }
      } else {
         Monster staticMonster;
         Monster evolveIntoMonster;
         if (activeIsland.isUnderlingIsland()) {
            staticMonster = MonsterLookup.get(boxMonster.getType());
            if (staticMonster.isWubbox()) {
               diamondsPerMonster = GameSettings.getInt("WUBLIN_BOX_INVENTORY_DIAMOND_PRICE_PER_MONSTER");
            } else if (boxMonster.isEvolvable()) {
               evolveIntoMonster = MonsterLookup.getFromEntityId(staticMonster.evolveInto());
               if (evolveIntoMonster.isRare()) {
                  diamondsPerMonster = GameSettings.getInt("EVOLVE_INVENTORY_DIAMOND_PRICE_PER_EGG_RARE");
               } else if (evolveIntoMonster.isEpic()) {
                  diamondsPerMonster = GameSettings.getInt("EVOLVE_INVENTORY_DIAMOND_PRICE_PER_EGG_EPIC");
               } else {
                  diamondsPerMonster = GameSettings.getInt("EVOLVE_INVENTORY_DIAMOND_PRICE_PER_EGG");
               }
            } else {
               diamondsPerMonster = GameSettings.getInt("UNDERLING_INVENTORY_DIAMOND_PRICE_PER_EGG");
            }
         } else if (activeIsland.isCelestialIsland()) {
            staticMonster = MonsterLookup.get(boxMonster.getType());
            if (staticMonster.isWubbox()) {
               diamondsPerMonster = GameSettings.getInt("WUBLIN_BOX_INVENTORY_DIAMOND_PRICE_PER_MONSTER");
            } else if (boxMonster.isInactiveBoxMonster()) {
               diamondsPerMonster = GameSettings.getInt("CELESTIAL_INVENTORY_DIAMOND_PRICE_PER_EGG");
            } else if (boxMonster.isEvolvable()) {
               evolveIntoMonster = MonsterLookup.getFromEntityId(staticMonster.evolveInto());
               if (evolveIntoMonster.isAdult()) {
                  diamondsPerMonster = GameSettings.getInt("ASCEND_INVENTORY_DIAMOND_PRICE_PER_EGG_RARE");
               } else if (evolveIntoMonster.isElder()) {
                  diamondsPerMonster = GameSettings.getInt("ASCEND_INVENTORY_DIAMOND_PRICE_PER_EGG_EPIC");
               } else {
                  diamondsPerMonster = GameSettings.getInt("ASCEND_INVENTORY_DIAMOND_PRICE_PER_EGG");
               }
            } else {
               diamondsPerMonster = GameSettings.getInt("CELESTIAL_INVENTORY_DIAMOND_PRICE_PER_EGG");
            }
         } else if (activeIsland.isAmberIsland()) {
            diamondsPerMonster = GameSettings.getInt("AMBER_BOX_INVENTORY_DIAMOND_PRICE_PER_EGG");
         } else if (boxMonster.isRare()) {
            diamondsPerMonster = GameSettings.getInt("RARE_BOX_INVENTORY_DIAMOND_PRICE_PER_MONSTER");
         } else if (boxMonster.isEpic()) {
            diamondsPerMonster = GameSettings.getInt("EPIC_BOX_INVENTORY_DIAMOND_PRICE_PER_MONSTER");
         } else {
            diamondsPerMonster = GameSettings.getInt("BOX_INVENTORY_DIAMOND_PRICE_PER_MONSTER");
         }
      }

      return diamondsPerMonster;
   }

   private static int getEggWildcardFillCost(int numGenes, EggRequirements.Rarity rarity, boolean hasMagicalGene, boolean hasFireGene, boolean hasEtherealGene, boolean hasMythicalGene, boolean hasSeasonalGene, boolean isWublin) {
      if (numGenes == 0) {
         numGenes = 1;
      }

      if (isWublin) {
         return GameSettings.getInt("USER_WUBLIN_BOX_INV_WILDCARDS_PRICE_PER_EGG");
      } else if (!hasMagicalGene && !hasFireGene) {
         if (!hasEtherealGene && !hasMythicalGene && !hasSeasonalGene) {
            if (rarity == EggRequirements.Rarity.Rare) {
               return GameSettings.getInt("USER_NATURAL_RARE_BOX_INV_WILDCARDS_PRICE_PER_GENE") * numGenes;
            } else {
               return rarity == EggRequirements.Rarity.Epic ? GameSettings.getInt("USER_NATURAL_EPIC_BOX_INV_WILDCARDS_PRICE_PER_GENE") * numGenes : GameSettings.getInt("USER_NATURAL_COMMON_BOX_INV_WILDCARDS_PRICE_PER_EGG");
            }
         } else if (rarity == EggRequirements.Rarity.Rare) {
            return GameSettings.getInt("USER_ETHEREAL_RARE_BOX_INV_WILDCARDS_PRICE_PER_GENE") * numGenes;
         } else {
            return rarity == EggRequirements.Rarity.Epic ? GameSettings.getInt("USER_ETHEREAL_EPIC_BOX_INV_WILDCARDS_PRICE_PER_GENE") * numGenes : GameSettings.getInt("USER_ETHEREAL_COMMON_BOX_INV_WILDCARDS_PRICE_PER_GENE") * numGenes;
         }
      } else if (rarity == EggRequirements.Rarity.Rare) {
         return GameSettings.getInt("USER_FIRE_RARE_BOX_INV_WILDCARDS_PRICE_PER_GENE") * numGenes;
      } else {
         return rarity == EggRequirements.Rarity.Epic ? GameSettings.getInt("USER_FIRE_EPIC_BOX_INV_WILDCARDS_PRICE_PER_GENE") * numGenes : GameSettings.getInt("USER_FIRE_COMMON_BOX_INV_WILDCARDS_PRICE_PER_GENE") * numGenes;
      }
   }

   public String debugPrintConcise() {
      return this.monsterId != 0 ? "m" + this.monsterId : "f" + this.flexDefId;
   }

   public String debugPrint() {
      return this.monsterId != 0 ? MonsterLookup.get(this.monsterId).getName() : MonsterFlexEggDefLookup.getInstance().getEntry(this.flexDefId).name();
   }
}
