package com.bigbluebubble.mysingingmonsters.logging;

public class MetricCost {
   String resource = "";
   long amount = 0L;

   public MetricCost(long coins, int diamonds, long eth, long starpower, long keys) {
      this.init(coins, diamonds, eth, starpower, keys, 0L, 0);
   }

   public MetricCost(long coins, int diamonds, long eth, long starpower, long keys, long relics) {
      this.init(coins, diamonds, eth, starpower, keys, relics, 0);
   }

   public MetricCost(long coins, int diamonds, long eth, long starpower) {
      this.init(coins, diamonds, eth, starpower, 0L, 0L, 0);
   }

   public MetricCost(long coins, int diamonds, long eth, long starpower, long keys, long relics, int medals) {
      this.init(coins, diamonds, eth, starpower, keys, relics, medals);
   }

   private void init(long coins, int diamonds, long eth, long starpower, long keys, long relics, int medals) {
      if (relics > 0L) {
         this.resource = "relics";
         this.amount = relics;
      } else if (keys > 0L) {
         this.resource = "keys";
         this.amount = keys;
      } else if (starpower > 0L) {
         this.resource = "starpower";
         this.amount = starpower;
      } else if (diamonds > 0) {
         this.resource = "diamonds";
         this.amount = (long)diamonds;
      } else if (eth > 0L) {
         this.resource = "eth_currency";
         this.amount = eth;
      } else if (coins > 0L) {
         this.resource = "coins";
         this.amount = coins;
      } else if (medals > 0) {
         this.resource = "medals";
         this.amount = (long)medals;
      } else {
         this.resource = "free";
         this.amount = 0L;
      }

   }

   public String getResource() {
      return this.resource;
   }

   public long getAmount() {
      return this.amount;
   }
}
