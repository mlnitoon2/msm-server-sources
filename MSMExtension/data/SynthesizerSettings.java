package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.mysingingmonsters.Logger;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;

public class SynthesizerSettings {
   public static ConcurrentHashMap<Integer, SynthesizerSettings.SynthesizerGeneSetting> geneSettings;

   public static void init(JSONArray costs, JSONArray numRequiredPerGenes, JSONArray basePercentages, JSONArray failDurations) {
      geneSettings = new ConcurrentHashMap();

      try {
         int numGenes = 3;

         for(int i = 0; i < costs.length(); ++i) {
            geneSettings.put(numGenes, new SynthesizerSettings.SynthesizerGeneSetting(costs.getInt(i), numRequiredPerGenes.getInt(i), (float)basePercentages.getDouble(i), failDurations.getInt(i)));
            ++numGenes;
         }
      } catch (Exception var6) {
         Logger.trace(var6);
      }

   }

   public static int GetCost(int numGenes) {
      return ((SynthesizerSettings.SynthesizerGeneSetting)geneSettings.get(numGenes)).cost();
   }

   public static int GetNumPerGene(int numGenes) {
      return ((SynthesizerSettings.SynthesizerGeneSetting)geneSettings.get(numGenes)).numPerGene();
   }

   public static float GetBasePercentage(int numGenes) {
      return ((SynthesizerSettings.SynthesizerGeneSetting)geneSettings.get(numGenes)).basePercentage();
   }

   public static int GetFailDuration(int numGenes) {
      return ((SynthesizerSettings.SynthesizerGeneSetting)geneSettings.get(numGenes)).failDuration();
   }

   private static class SynthesizerGeneSetting {
      private int cost;
      private float basePercentage;
      private int numPerGene;
      private int failDuration;

      public SynthesizerGeneSetting(int cost, int numRequiredPerGene, float basePercentage, int failDuration) {
         this.cost = cost;
         this.basePercentage = basePercentage;
         this.numPerGene = numRequiredPerGene;
         this.failDuration = failDuration;
      }

      public int cost() {
         return this.cost;
      }

      public float basePercentage() {
         return this.basePercentage;
      }

      public int numPerGene() {
         return this.numPerGene;
      }

      public int failDuration() {
         return this.failDuration;
      }
   }
}
