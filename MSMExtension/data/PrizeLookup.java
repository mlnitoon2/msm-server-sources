package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PrizeLookup {
   private static List<ISFSObject> scratchOffs;
   private static List<ISFSObject> spinWheelPrizes;
   private static List<ISFSObject> spinWheelJackpots;

   public static List<ISFSObject> getScratchOffs() {
      return scratchOffs;
   }

   public static List<ISFSObject> getSpinWheelPrizes() {
      return spinWheelPrizes;
   }

   public static List<ISFSObject> getSpinWheelJackpotPrizes() {
      return spinWheelJackpots;
   }

   private PrizeLookup() {
   }

   public static void init(IDbWrapper db) throws Exception {
      initScratchOffs(db);
      initSpinWheelPrizes(db);
      initSpinWheelJackpots(db);
   }

   private static void initScratchOffs(IDbWrapper db) throws Exception {
      List<ISFSObject> newScratchOffs = new ArrayList(50);
      String sql = "SELECT * FROM scratch_offs ORDER BY type, prize, amount DESC";
      ISFSArray results = db.query("SELECT * FROM scratch_offs ORDER BY type, prize, amount DESC");
      Iterator i = results.iterator();

      while(i.hasNext()) {
         newScratchOffs.add((ISFSObject)((SFSDataWrapper)i.next()).getObject());
      }

      scratchOffs = newScratchOffs;
   }

   private static void initSpinWheelPrizes(IDbWrapper db) throws Exception {
      List<ISFSObject> newSpinWheelPrizes = new ArrayList(25);
      String sql = "SELECT * FROM spin_wheel_prizes ORDER BY type, prize, amount DESC";
      ISFSArray results = db.query("SELECT * FROM spin_wheel_prizes ORDER BY type, prize, amount DESC");
      Iterator i = results.iterator();

      while(i.hasNext()) {
         newSpinWheelPrizes.add((ISFSObject)((SFSDataWrapper)i.next()).getObject());
      }

      spinWheelPrizes = newSpinWheelPrizes;
   }

   private static void initSpinWheelJackpots(IDbWrapper db) throws Exception {
      List<ISFSObject> newSpinWheelJackpots = new ArrayList(20);
      String sql = "SELECT * FROM spin_wheel_jackpot_prizes ORDER BY type, prize, amount DESC";
      ISFSArray results = db.query("SELECT * FROM spin_wheel_jackpot_prizes ORDER BY type, prize, amount DESC");
      Iterator i = results.iterator();

      while(i.hasNext()) {
         newSpinWheelJackpots.add((ISFSObject)((SFSDataWrapper)i.next()).getObject());
      }

      spinWheelJackpots = newSpinWheelJackpots;
   }
}
