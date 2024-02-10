package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.mysingingmonsters.player.PlayerMonster;
import com.smartfoxserver.v2.entities.data.ISFSObject;

public class EggRequirements {
   public static final String DESCRIPTION_KEY = "Description";
   public static final String AT_LEAST_LEVEL_KEY = "AtLeastLevel";
   public static final String EXACT_MONSTER_KEY = "ExactMonster";
   public static final String EXACT_GENES_KEY = "ExactGenes";
   public static final String CONTAINS_GENES_KEY = "ContainsGenes";
   public static final String EXACT_NUM_GENES_KEY = "ExactNumGenes";
   public static final String AT_LEAST_NUM_GENES_KEY = "AtLeastNumGenes";
   public static final String FROM_ISLAND_KEY = "FromIsland";
   public static final String NEGATION_KEY = "Not";
   public static final String RARITY_KEY = "Rarity";
   int atLeastLevel = 0;
   int exactMonsterType = 0;
   String exactGenes = "";
   String containsGenes = "";
   int exactNumGenes = 0;
   int atLeastNumGenes = 0;
   int fromIsland = 0;
   EggRequirements.Rarity rarity;
   protected boolean not;
   protected ISFSObject data;

   private EggRequirements.Rarity rarityFromString(String rarityStr) {
      if (rarityStr.equals("common")) {
         return EggRequirements.Rarity.Common;
      } else if (rarityStr.equals("rare")) {
         return EggRequirements.Rarity.Rare;
      } else {
         return rarityStr.equals("epic") ? EggRequirements.Rarity.Epic : EggRequirements.Rarity.Undefined;
      }
   }

   public EggRequirements(ISFSObject data) {
      this.rarity = EggRequirements.Rarity.Undefined;
      this.not = false;
      this.data = data;
      if (data.containsKey("AtLeastLevel")) {
         this.atLeastLevel = data.getInt("AtLeastLevel");
      }

      if (data.containsKey("ExactMonster")) {
         this.exactMonsterType = data.getInt("ExactMonster");
      }

      if (data.containsKey("ExactGenes")) {
         this.exactGenes = data.getUtfString("ExactGenes");
      }

      if (data.containsKey("ContainsGenes")) {
         this.containsGenes = data.getUtfString("ContainsGenes");
      }

      if (data.containsKey("ExactNumGenes")) {
         this.exactNumGenes = data.getInt("ExactNumGenes");
      }

      if (data.containsKey("AtLeastNumGenes")) {
         this.atLeastNumGenes = data.getInt("AtLeastNumGenes");
      }

      if (data.containsKey("FromIsland")) {
         this.fromIsland = data.getInt("FromIsland");
      }

      if (data.containsKey("Not")) {
         this.not = data.getInt("Not") == 1;
      }

      if (data.containsKey("Rarity")) {
         this.rarity = this.rarityFromString(data.getUtfString("Rarity"));
      }

   }

   boolean doesTargetContainAllGenes(String target, String contains) {
      for(int i = 0; i < contains.length(); ++i) {
         char gene = contains.charAt(i);
         if (target.indexOf(gene) == -1) {
            return false;
         }
      }

      return true;
   }

   boolean doesTargetContainAnyGenes(String target, String contains) {
      for(int i = 0; i < contains.length(); ++i) {
         char gene = contains.charAt(i);
         if (target.indexOf(gene) != -1) {
            return true;
         }
      }

      return false;
   }

   boolean isMonsterFromIsland(int monsterType, int islandId) {
      return IslandLookup.get(islandId).hasMonster(monsterType);
   }

   boolean hasMagicalGene() {
      if (this.exactMonsterType == 0) {
         if (this.exactGenes.length() != 0) {
            return this.doesTargetContainAnyGenes(this.exactGenes, "RYVW");
         } else {
            return this.containsGenes.length() != 0 ? this.doesTargetContainAnyGenes(this.containsGenes, "RYVW") : false;
         }
      } else {
         Monster monsterData = MonsterLookup.get(this.exactMonsterType);
         return monsterData.hasGene("R") || monsterData.hasGene("Y") || monsterData.hasGene("V") || monsterData.hasGene("W");
      }
   }

   boolean hasEtherealGene() {
      if (this.exactMonsterType == 0) {
         if (this.exactGenes.length() != 0) {
            return this.doesTargetContainAnyGenes(this.exactGenes, "GJKLM");
         } else {
            return this.containsGenes.length() != 0 ? this.doesTargetContainAnyGenes(this.containsGenes, "GJKLM") : false;
         }
      } else {
         Monster monsterData = MonsterLookup.get(this.exactMonsterType);
         return monsterData.hasGene("G") || monsterData.hasGene("J") || monsterData.hasGene("K") || monsterData.hasGene("L") || monsterData.hasGene("M");
      }
   }

   boolean hasMythicalGene() {
      if (this.exactMonsterType == 0) {
         if (this.exactGenes.length() != 0) {
            return this.doesTargetContainAnyGenes(this.exactGenes, "PH");
         } else {
            return this.containsGenes.length() != 0 ? this.doesTargetContainAnyGenes(this.containsGenes, "PH") : false;
         }
      } else {
         Monster monsterData = MonsterLookup.get(this.exactMonsterType);
         return monsterData.hasGene("P") || monsterData.hasGene("H");
      }
   }

   boolean hasFireGene() {
      if (this.exactMonsterType != 0) {
         Monster monsterData = MonsterLookup.get(this.exactMonsterType);
         return monsterData.hasGene("N");
      } else if (this.exactGenes.length() != 0) {
         return this.doesTargetContainAnyGenes(this.exactGenes, "N");
      } else {
         return this.containsGenes.length() != 0 ? this.doesTargetContainAnyGenes(this.containsGenes, "N") : false;
      }
   }

   int numGenes() {
      if (this.exactMonsterType != 0) {
         Monster monsterData = MonsterLookup.get(this.exactMonsterType);
         return monsterData.getGenes().length();
      } else if (this.exactGenes.length() != 0) {
         return this.exactGenes.length();
      } else {
         return this.exactNumGenes != 0 ? this.exactNumGenes : 0;
      }
   }

   EggRequirements.Rarity getMonsterRarity() {
      return this.exactMonsterType != 0 ? getMonsterRarity(this.exactMonsterType) : this.rarity;
   }

   static EggRequirements.Rarity getMonsterRarity(int monsterId) {
      if (MonsterCommonToRareMapping.isRare(monsterId)) {
         return EggRequirements.Rarity.Rare;
      } else {
         return MonsterCommonToEpicMapping.isEpic(monsterId) ? EggRequirements.Rarity.Epic : EggRequirements.Rarity.Common;
      }
   }

   boolean isSeasonal() {
      if (this.exactMonsterType != 0) {
         Monster monsterData = MonsterLookup.get(this.exactMonsterType);
         return monsterData.isSeasonalMonster();
      } else {
         return false;
      }
   }

   boolean hasWublinGene() {
      if (this.exactMonsterType != 0) {
         Monster monsterData = MonsterLookup.get(this.exactMonsterType);
         return monsterData.hasGene("U");
      } else if (this.exactGenes.length() != 0) {
         return this.doesTargetContainAnyGenes(this.exactGenes, "U");
      } else {
         return this.containsGenes.length() != 0 ? this.doesTargetContainAnyGenes(this.containsGenes, "U") : false;
      }
   }

   public boolean evaluate(Monster monsterData) {
      int monsterType = 0;
      String monsterGenes = "";
      if (monsterData != null) {
         monsterType = monsterData.getMonsterID();
         monsterGenes = monsterData.getGenes();
      }

      return this.evaluate(monsterType, monsterGenes);
   }

   private boolean evaluate(int monsterType, String monsterGenes) {
      boolean condition;
      if (this.not) {
         condition = (this.exactMonsterType == 0 || monsterType != this.exactMonsterType) && (this.exactGenes.length() == 0 || !monsterGenes.equals(this.exactGenes)) && (this.containsGenes.length() == 0 || !this.doesTargetContainAnyGenes(monsterGenes, this.containsGenes)) && (this.exactNumGenes == 0 || monsterGenes.length() != this.exactNumGenes) && (this.atLeastNumGenes == 0 || monsterGenes.length() < this.atLeastNumGenes) && (this.fromIsland == 0 || !this.isMonsterFromIsland(monsterType, this.fromIsland)) && (this.rarity == EggRequirements.Rarity.Undefined || MonsterCommonToRareMapping.rareToCommonMapContainsKey(monsterType) && this.rarity != EggRequirements.Rarity.Rare || MonsterCommonToEpicMapping.epicToCommonMapContainsKey(monsterType) && this.rarity != EggRequirements.Rarity.Epic || this.rarity != EggRequirements.Rarity.Common);
         return condition;
      } else {
         condition = (this.exactMonsterType == 0 || monsterType == this.exactMonsterType) && (this.exactGenes.length() == 0 || monsterGenes.equals(this.exactGenes)) && (this.containsGenes.length() == 0 || this.doesTargetContainAnyGenes(monsterGenes, this.containsGenes)) && (this.exactNumGenes == 0 || monsterGenes.length() == this.exactNumGenes) && (this.atLeastNumGenes == 0 || monsterGenes.length() >= this.atLeastNumGenes) && (this.fromIsland == 0 || this.isMonsterFromIsland(monsterType, this.fromIsland)) && (this.rarity == EggRequirements.Rarity.Undefined || MonsterCommonToRareMapping.rareToCommonMapContainsKey(monsterType) && this.rarity == EggRequirements.Rarity.Rare || MonsterCommonToEpicMapping.epicToCommonMapContainsKey(monsterType) && this.rarity == EggRequirements.Rarity.Epic || this.rarity == EggRequirements.Rarity.Common);
         return condition;
      }
   }

   public boolean evaluate(PlayerMonster m) {
      return m != null ? this.evaluate(MonsterLookup.get(m.getType())) : this.evaluate(0, "");
   }

   public static enum Rarity {
      Undefined,
      Common,
      Rare,
      Epic;
   }
}
