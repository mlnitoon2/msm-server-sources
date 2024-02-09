package com.bigbluebubble.mysingingmonsters.player;

import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;
import java.util.Map.Entry;

public class PlayerBuyback {
   public static final String EXTRA_DATA_KEY = "extra_data";
   public static final String BOXED_DATA_KEY = "boxed_eggs";
   public static final String COIN_COST_KEY = "coin_cost";
   protected long entity_id;
   protected String name;
   protected int level;
   protected int times_fed;
   protected ISFSArray boxed_eggs;
   protected ISFSObject megaState;
   protected ISFSObject costumeState;
   protected int secondaryCurrencyCost = -1;
   protected boolean evolutionUnlocked = false;
   protected boolean powerupUnlocked = false;
   protected int bookValue = -1;

   public PlayerBuyback(ISFSObject buybackData) {
      this.initFromSFSObject(buybackData);
   }

   public PlayerBuyback(PlayerMonster playerMonster, int coinCost, boolean hasBoxingExpiry) {
      this.entity_id = (long)playerMonster.getEntityId();
      this.name = playerMonster.getName();
      this.level = playerMonster.getLevel();
      this.times_fed = playerMonster.getTimesFed();
      this.megaState = playerMonster.getMegaSFS();
      this.costumeState = playerMonster.getCostumeSFS();
      this.secondaryCurrencyCost = coinCost;
      this.bookValue = playerMonster.bookValue();
      if (playerMonster.isInactiveBoxMonster() && playerMonster.boxedEggs() != null) {
         if (!hasBoxingExpiry) {
            this.boxed_eggs = playerMonster.boxedEggs();
         } else {
            this.boxed_eggs = new SFSArray();
         }
      } else if (playerMonster.isEvolvable()) {
         this.evolutionUnlocked = playerMonster.isUnderlingEvolveUnlocked();
         this.powerupUnlocked = playerMonster.isCelestialPowerupUnlocked();
      }

   }

   public void initFromSFSObject(ISFSObject componentData) {
      try {
         Iterator iterator = componentData.iterator();

         while(true) {
            while(iterator.hasNext()) {
               Entry<String, SFSDataWrapper> e = (Entry)iterator.next();
               if (((String)e.getKey()).equals("entity_id")) {
                  this.entity_id = SFSHelpers.getLong((SFSDataWrapper)e.getValue());
               } else if (((String)e.getKey()).equals("name")) {
                  this.name = SFSHelpers.getUtfString((SFSDataWrapper)e.getValue());
               } else if (((String)e.getKey()).equals("level")) {
                  this.level = (int)SFSHelpers.getLong((SFSDataWrapper)e.getValue());
               } else if (((String)e.getKey()).equals("times_fed")) {
                  this.times_fed = SFSHelpers.getInt((SFSDataWrapper)e.getValue());
               } else if (!((String)e.getKey()).equals("extra_data") && !((String)e.getKey()).equals("boxed_eggs")) {
                  if (((String)e.getKey()).equals("megamonster")) {
                     this.megaState = (ISFSObject)((SFSDataWrapper)e.getValue()).getObject();
                  } else if (((String)e.getKey()).contentEquals("costume")) {
                     this.costumeState = (ISFSObject)((SFSDataWrapper)e.getValue()).getObject();
                  } else if (((String)e.getKey()).equals("coin_cost")) {
                     this.secondaryCurrencyCost = SFSHelpers.getInt((SFSDataWrapper)e.getValue());
                  } else if (((String)e.getKey()).equals("evolve_unlocked")) {
                     this.evolutionUnlocked = SFSHelpers.getInt((SFSDataWrapper)e.getValue()) != 0;
                  } else if (((String)e.getKey()).equals("powerup_unlocked")) {
                     this.powerupUnlocked = SFSHelpers.getInt((SFSDataWrapper)e.getValue()) != 0;
                  } else if (!((String)e.getKey()).equals("costume")) {
                     throw new ClassCastException("Invalid SFSObject key " + (String)e.getKey());
                  }
               } else {
                  this.boxed_eggs = (ISFSArray)((SFSDataWrapper)e.getValue()).getObject();
               }
            }

            return;
         }
      } catch (ClassCastException var4) {
         Logger.trace((Exception)var4, "PlayerBuyback initialization error");
      }
   }

   public ISFSObject toSFSObject() {
      ISFSObject s = new SFSObject();
      s.putLong("entity_id", this.getID());
      s.putUtfString("name", this.name);
      s.putInt("level", this.level);
      s.putInt("times_fed", this.times_fed);
      if (this.megaState != null) {
         s.putSFSObject("megamonster", this.megaState);
      }

      if (this.costumeState != null) {
         s.putSFSObject("costume", this.costumeState);
      }

      if (this.boxed_eggs != null) {
         s.putSFSArray("boxed_eggs", this.boxed_eggs);
         s.putSFSArray("extra_data", this.boxed_eggs);
      }

      if (this.evolutionUnlocked) {
         s.putInt("evolve_unlocked", 1);
      }

      if (this.powerupUnlocked) {
         s.putInt("powerup_unlocked", 1);
      }

      if (this.secondaryCurrencyCost != -1) {
         s.putInt("coin_cost", this.secondaryCurrencyCost);
      }

      return s;
   }

   public long getID() {
      return this.entity_id;
   }

   public String getName() {
      return this.name;
   }

   public int getLevel() {
      return this.level;
   }

   public int getTimesFed() {
      return this.times_fed;
   }

   public ISFSArray getBoxedEggs() {
      return this.boxed_eggs;
   }

   public int bookValue() {
      return this.bookValue;
   }

   public ISFSObject getMegaMonsterData() {
      return this.megaState;
   }

   public ISFSObject getCostumeData() {
      return this.costumeState;
   }

   public int getCoinCost() {
      return this.secondaryCurrencyCost;
   }

   public boolean getEvolutionUnlocked() {
      return this.evolutionUnlocked;
   }

   public boolean getPowerupUnlocked() {
      return this.powerupUnlocked;
   }
}
