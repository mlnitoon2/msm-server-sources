package com.bigbluebubble.mysingingmonsters.player;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.data.IslandLookup;
import com.bigbluebubble.mysingingmonsters.data.Structure;
import com.bigbluebubble.mysingingmonsters.data.StructureLookup;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import java.util.Iterator;
import java.util.Map.Entry;

public class PlayerStructure extends PlayerEntity {
   private static final float MIN_DECORATION_SCALE = 0.7F;
   private static final float MAX_DECORATION_SCALE = 1.1F;
   private static int[] obstacleStructIdStartPerIsland = new int[]{106, 112, 118, 125, 131, -1, 194, 208, -1, -1, -1, -1, 361, 399, 430, 471, 498, 590, 631, -1, 661, 699, 777, 875};
   public static final String ID_KEY = "user_structure_id";
   public static final String TYPE_KEY = "structure";
   public static final String DATE_CREATE_KEY = "date_created";
   public static final String DATE_COMPLETE_KEY = "building_completed";
   public static final String SCALE_KEY = "scale";
   public static final String UPGRADING_KEY = "is_upgrading";
   public static final String COMPLETE_KEY = "is_complete";
   public static final String IN_WAREHOUSE_KEY = "in_warehouse";
   public static final String IN_FUZER_KEY = "in_fuzer";
   public static final String SETTINGS_KEY = "settings";
   public static final String COLOR_R_KEY = "colorR";
   public static final String COLOR_Y_KEY = "colorY";
   public static final String COLOR_B_KEY = "colorB";
   public static final String EXTRA_KEY = "ext";
   public static final String DIAMONDS_COLLECTED = "diamonds_collected";
   public static final String PERCENT_COLLECTION_STORED = "collection_waited";
   protected long user_structure_id;
   protected long date_created;
   protected long building_completed;
   protected double numDiamondsCollected;
   protected float percentCollectionStored;
   protected int settings;
   protected float colorR;
   protected float colorY;
   protected float colorB;
   protected float scale;
   protected int structure;
   protected boolean is_upgrading;
   protected boolean is_complete;
   protected boolean in_warehouse;
   protected boolean in_fuzer;
   protected ISFSObject extraObj;

   public static int getObstacleStructIdStartFromIslandIndex(int index) {
      return obstacleStructIdStartPerIsland[IslandLookup.get(index).getType() - 1];
   }

   public PlayerStructure(ISFSObject componentData) {
      super(componentData);
      this.initFromSFSObject(componentData);
   }

   public void initFromSFSObject(ISFSObject componentData) {
      this.date_created = this.building_completed = this.last_collection = 0L;
      this.settings = -1;
      this.colorR = this.colorY = this.colorB = -1.0F;
      this.in_fuzer = false;

      try {
         Iterator iterator = componentData.iterator();

         while(iterator.hasNext()) {
            Entry<String, SFSDataWrapper> e = (Entry)iterator.next();
            if (((String)e.getKey()).equals("user_structure_id")) {
               this.user_structure_id = SFSHelpers.getLong((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("island")) {
               this.island = SFSHelpers.getLong((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("structure")) {
               this.structure = (short)((int)SFSHelpers.getLong((SFSDataWrapper)e.getValue()));
            } else if (((String)e.getKey()).equals("date_created")) {
               this.date_created = SFSHelpers.getLong((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("building_completed")) {
               this.building_completed = SFSHelpers.getLong((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("last_collection")) {
               this.last_collection = SFSHelpers.getLong((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("scale")) {
               this.scale = (float)Math.min(Math.max(SFSHelpers.getDouble((SFSDataWrapper)e.getValue()), 0.699999988079071D), 1.100000023841858D);
            } else if (((String)e.getKey()).equals("pos_x")) {
               this.pos_x = (short)((int)SFSHelpers.getLong((SFSDataWrapper)e.getValue()));
            } else if (((String)e.getKey()).equals("pos_y")) {
               this.pos_y = (short)((int)SFSHelpers.getLong((SFSDataWrapper)e.getValue()));
            } else if (((String)e.getKey()).equals("muted")) {
               this.muted = SFSHelpers.getLong((SFSDataWrapper)e.getValue()) != 0L;
            } else if (((String)e.getKey()).equals("flip")) {
               this.flip = SFSHelpers.getLong((SFSDataWrapper)e.getValue()) != 0L;
            } else if (((String)e.getKey()).equals("is_upgrading")) {
               this.is_upgrading = SFSHelpers.getLong((SFSDataWrapper)e.getValue()) != 0L;
            } else if (((String)e.getKey()).equals("is_complete")) {
               this.is_complete = SFSHelpers.getInt((SFSDataWrapper)e.getValue()) != 0;
            } else if (((String)e.getKey()).equals("in_warehouse")) {
               this.in_warehouse = SFSHelpers.getInt((SFSDataWrapper)e.getValue()) != 0;
            } else if (((String)e.getKey()).equals("diamonds_collected")) {
               this.numDiamondsCollected = SFSHelpers.getDouble((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("collection_waited")) {
               this.percentCollectionStored = (float)SFSHelpers.getDouble((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("settings")) {
               this.settings = (int)SFSHelpers.getLong((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("colorR")) {
               this.colorR = (float)SFSHelpers.getDouble((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("colorY")) {
               this.colorY = (float)SFSHelpers.getDouble((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("colorB")) {
               this.colorB = (float)SFSHelpers.getDouble((SFSDataWrapper)e.getValue());
            } else if (((String)e.getKey()).equals("in_fuzer")) {
               this.in_fuzer = SFSHelpers.getInt((SFSDataWrapper)e.getValue()) != 0;
            } else if (((String)e.getKey()).equals("book_value")) {
               this.bookValue = SFSHelpers.getInt((SFSDataWrapper)e.getValue());
            } else {
               if (!((String)e.getKey()).equals("ext")) {
                  throw new ClassCastException("Invalid SFSObject key " + (String)e.getKey());
               }

               this.extraObj = (ISFSObject)((SFSDataWrapper)e.getValue()).getObject();
            }
         }
      } catch (ClassCastException var4) {
         Logger.trace((Exception)var4, "Player Structure initialization error");
      }

      if (!StructureLookup.get(this.structure).isDecoration()) {
         this.scale = 1.0F;
      }

   }

   public ISFSObject toSFSObject(PlayerIsland pi) {
      ISFSObject s = super.toSFSObject(pi);
      s.putLong("user_structure_id", this.getID());
      s.putInt("structure", this.getType());
      s.putDouble("scale", this.getScale());
      s.putInt("is_upgrading", this.is_upgrading ? 1 : 0);
      s.putInt("is_complete", this.is_complete ? 1 : 0);
      s.putInt("in_warehouse", this.in_warehouse ? 1 : 0);
      if (this.date_created != 0L) {
         s.putLong("date_created", this.date_created);
      }

      if (this.building_completed != 0L) {
         s.putLong("building_completed", this.building_completed);
      }

      if (this.numDiamondsCollected != 0.0D) {
         s.putDouble("diamonds_collected", this.numDiamondsCollected);
      }

      if (this.percentCollectionStored != 0.0F) {
         s.putDouble("collection_waited", (double)this.percentCollectionStored);
      }

      if (this.settings != -1) {
         s.putInt("settings", this.settings);
      }

      if (this.colorR != -1.0F) {
         s.putDouble("colorR", (double)this.colorR);
      }

      if (this.colorY != -1.0F) {
         s.putDouble("colorY", (double)this.colorY);
      }

      if (this.colorB != -1.0F) {
         s.putDouble("colorB", (double)this.colorB);
      }

      if (this.in_fuzer) {
         s.putInt("in_fuzer", 1);
      }

      if (this.extraObj != null) {
         s.putSFSObject("ext", this.extraObj);
      }

      return s;
   }

   public long getID() {
      return this.user_structure_id;
   }

   public void setID(long id) {
      this.user_structure_id = id;
   }

   public int getEntityId() {
      return StructureLookup.get(this.getType()).getEntityId();
   }

   public int getType() {
      return this.structure;
   }

   public String getStructureType() {
      return StructureLookup.get(this.getType()).getType();
   }

   public double getScale() {
      return (double)this.scale;
   }

   public long getTimeRemaining() {
      return this.building_completed == 0L ? 0L : Math.max(this.building_completed - MSMExtension.CurrentDBTime(), 0L);
   }

   public long getInitialTimeRemaining() {
      return Math.max(this.building_completed - this.date_created, 0L);
   }

   public float getR() {
      return this.colorR;
   }

   public float getY() {
      return this.colorY;
   }

   public float getB() {
      return this.colorB;
   }

   public long getLastCollectionTime() {
      return this.last_collection;
   }

   public long getBuildingCompletedTime() {
      return this.building_completed;
   }

   public void finishBuildingNow() {
      this.building_completed = MSMExtension.CurrentDBTime();
   }

   public long getDateCreated() {
      return this.date_created;
   }

   public boolean isComplete() {
      return this.is_complete;
   }

   public boolean isUpgrading() {
      return this.is_upgrading;
   }

   public int getMuted() {
      return this.muted ? 1 : 0;
   }

   public boolean isBakery() {
      return StructureLookup.get(this.getType()).isBakery();
   }

   public boolean isWarehouse() {
      return StructureLookup.get(this.getType()).isWarehouse();
   }

   public boolean isNursery() {
      return StructureLookup.get(this.getType()).isNursery();
   }

   public boolean isBreeding() {
      return StructureLookup.get(this.getType()).isBreeding();
   }

   public boolean isCrucible() {
      return StructureLookup.get(this.getType()).isCrucible();
   }

   public boolean isAttuner() {
      return StructureLookup.get(this.getType()).isAttuner();
   }

   public boolean isSynthesizer() {
      return StructureLookup.get(this.getType()).isSynthesizer();
   }

   public boolean isHotel() {
      return StructureLookup.get(this.getType()).isHotel();
   }

   public boolean isTorch() {
      return StructureLookup.get(this.getType()).isTorch();
   }

   public boolean isBuddy() {
      return StructureLookup.get(this.getType()).isBuddy();
   }

   public boolean isFuzer() {
      return StructureLookup.get(this.getType()).isFuzer();
   }

   public boolean isAwakener() {
      return StructureLookup.get(this.getType()).isAwakener();
   }

   public boolean inWarehouse() {
      return this.in_warehouse;
   }

   public boolean inFuzer() {
      return this.in_fuzer;
   }

   public ISFSObject getExtra() {
      return this.extraObj;
   }

   public void setPosition(int x, int y, double s) {
      if (StructureLookup.get(this.structure).isDecoration()) {
         this.scale = (float)Math.min(Math.max(s, 0.699999988079071D), 1.100000023841858D);
      }

      super.setPosition(x, y);
   }

   public void setBuildingCompletedTime(long t) {
      this.building_completed = t;
   }

   public void resetLastCollectionTime() {
      this.last_collection = MSMExtension.CurrentDBTime();
   }

   public void setDateCreated(long d) {
      this.date_created = d;
   }

   public void setSettings(int newSettings) {
      this.settings = newSettings;
   }

   public void setColor(float r, float y, float b) {
      this.colorR = r;
      this.colorY = y;
      this.colorB = b;
   }

   private void setComplete(boolean isComplete) {
      this.is_complete = isComplete;
   }

   private void setUpgrading(boolean isUpgrading) {
      this.is_upgrading = isUpgrading;
   }

   private void setType(int structureTypeId) {
      this.structure = structureTypeId;
   }

   public void toggleMute() {
      this.muted = !this.muted;
   }

   public void setInWarehouse(boolean inWarehouse) {
      this.in_warehouse = inWarehouse;
   }

   public void setInFuzer(boolean inFuzer) {
      this.in_fuzer = inFuzer;
   }

   public void setExtra(ISFSObject extra) {
      this.extraObj = extra;
   }

   public void startUpgradeStructure(Structure newStructure) {
      this.setComplete(false);
      this.setUpgrading(true);
      this.setDateCreated(MSMExtension.CurrentDBTime());
      this.setBuildingCompletedTime(MSMExtension.CurrentDBTime() + (long)newStructure.getBuildTimeMs());
      this.setBookValue(-1);
   }

   public Structure finishUpgradeStructure() {
      Structure oldStructure = StructureLookup.get(this.getType());
      Structure newStructure = StructureLookup.getNextUpgrade(oldStructure);
      if (newStructure == null) {
         return newStructure;
      } else {
         if (oldStructure.isMine()) {
            this.storeDiamondsSoFar();
         }

         this.setComplete(true);
         this.setUpgrading(false);
         this.setType(newStructure.getID());
         this.setDateCreated(0L);
         this.setBuildingCompletedTime(MSMExtension.CurrentDBTime());
         return newStructure;
      }
   }

   public void finishBuildingStructure() {
      this.setComplete(true);
      this.setUpgrading(false);
   }

   public void reduceStructureTimeByVideo() {
      long reduceTimeAmount = GameSettings.getLong("USER_SPEED_UP_DURATION") * 60L * 1000L;
      this.building_completed -= reduceTimeAmount;
      this.date_created -= reduceTimeAmount;
   }

   public int collectFromMine() {
      Structure staticStructure = StructureLookup.get(this.getType());
      long elapsedTime = Math.max((MSMExtension.CurrentDBTime() - this.getLastCollectionTime()) / 60000L, 0L);
      int timeRequired = staticStructure.getExtra().getInt("time");
      if (elapsedTime < (long)timeRequired) {
         return 0;
      } else {
         double diamondsStored = this.clearStoredDiamonds();
         double diamondsToCollect = (double)staticStructure.getExtra().getInt("diamonds");
         if (diamondsStored > 0.0D) {
            double percentCurrentDiamondsToCollect = (double)(1.0F - this.percentCollectionStored);
            diamondsToCollect *= percentCurrentDiamondsToCollect;
            diamondsToCollect += diamondsStored;
         }

         this.resetLastCollectionTime();
         return (int)(diamondsToCollect + 0.5D);
      }
   }

   private void storeDiamondsSoFar() {
      Structure oldStaticStructure = StructureLookup.get(this.getType());
      long elapsedTimeMs = Math.max(MSMExtension.CurrentDBTime() - this.getLastCollectionTime(), 0L);
      float timeRequiredMs = (float)(oldStaticStructure.getExtra().getInt("time") * '\uea60');
      float percent = (float)elapsedTimeMs / timeRequiredMs;
      this.percentCollectionStored = Math.min(percent, 1.0F);
      int fullDiamondAmount = oldStaticStructure.getExtra().getInt("diamonds");
      this.numDiamondsCollected = (double)(this.percentCollectionStored * (float)fullDiamondAmount);
   }

   private double clearStoredDiamonds() {
      double diamondsPreviouslyStored = this.numDiamondsCollected;
      this.numDiamondsCollected = 0.0D;
      this.percentCollectionStored = 0.0F;
      return diamondsPreviouslyStored;
   }

   public boolean canHoldEggs() {
      return this.isNursery() || this.isSynthesizer();
   }
}
