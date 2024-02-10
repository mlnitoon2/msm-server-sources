package com.bigbluebubble.mysingingmonsters.logging;

import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class GooglePlayEvents {
   private static final String REACH_LEVEL_4 = "CgkIvZ_r2qUBEAIQGA";
   private static final String REACH_LEVEL_7 = "CgkIvZ_r2qUBEAIQGQ";
   private static final String REACH_LEVEL_8 = "CgkIvZ_r2qUBEAIQGg";
   private static final String REACH_LEVEL_10 = "CgkIvZ_r2qUBEAIQGw";
   private static final String REACH_LEVEL_14 = "CgkIvZ_r2qUBEAIQHA";
   private static final String REACH_LEVEL_20 = "CgkIvZ_r2qUBEAIQHQ";
   private static final String REACH_LEVEL_30 = "CgkIvZ_r2qUBEAIQHg";
   private static final String REACH_LEVEL_40 = "CgkIvZ_r2qUBEAIQHw";
   private static final String REACH_LEVEL_50 = "CgkIvZ_r2qUBEAIQIA";
   private static final String REACH_LEVEL_60 = "CgkIvZ_r2qUBEAIQIQ";
   private static final String ADD_FRIEND = "CgkIvZ_r2qUBEAIQIg";
   private static final String COLD_ISLAND_CREATE = "CgkIvZ_r2qUBEAIQIw";
   private static final String AIR_ISLAND_CREATE = "CgkIvZ_r2qUBEAIQJA";
   private static final String WATER_ISLAND_CREATE = "CgkIvZ_r2qUBEAIQJQ";
   private static final String EARTH_ISLAND_CREATE = "CgkIvZ_r2qUBEAIQJg";
   private static final String GOLD_ISLAND_CREATE = "CgkIvZ_r2qUBEAIQJw";
   private static final String ETHEREAL_ISLAND_CREATE = "CgkIvZ_r2qUBEAIQKA";
   private static final String SHUGABUSH_ISLAND_CREATE = "CgkIvZ_r2qUBEAIQKQ";
   private static final String TRIBAL_ISLAND_CREATE = "CgkIvZ_r2qUBEAIQKg";
   private static final String PLANT_ISLAND_FURCORN_BREED = "CgkIvZ_r2qUBEAIQKw";
   private static final String PLANT_ISLAND_FWOG_BREED = "CgkIvZ_r2qUBEAIQLA";
   private static final String PLANT_ISLAND_DRUMPLER_BREED = "CgkIvZ_r2qUBEAIQLQ";
   private static final String PLANT_ISLAND_MAW_BREED = "CgkIvZ_r2qUBEAIQLg";
   private static final String PLANT_ISLAND_TROX_BREED = "CgkIvZ_r2qUBEAIQLw";
   private static final String PLANT_ISLAND_ENTBRAT_BREED = "CgkIvZ_r2qUBEAIQMA";
   private static final String PLANT_ISLAND_SHUGABUSH_BREED = "CgkIvZ_r2qUBEAIQMQ";
   private static final String PLANT_ISLAND_GHAZT_BREED = "CgkIvZ_r2qUBEAIQMg";
   private static final String PLANT_ISLAND_FURCORN_BUY = "CgkIvZ_r2qUBEAIQMw";
   private static final String PLANT_ISLAND_FWOG_BUY = "CgkIvZ_r2qUBEAIQNA";
   private static final String PLANT_ISLAND_DRUMPLER_BUY = "CgkIvZ_r2qUBEAIQNQ";
   private static final String PLANT_ISLAND_MAW_BUY = "CgkIvZ_r2qUBEAIQNg";
   private static final String PLANT_ISLAND_TROX_BUY = "CgkIvZ_r2qUBEAIQNw";
   private static final String PLANT_ISLAND_ENTBRAT_BUY = "CgkIvZ_r2qUBEAIQOA";
   private static final String PLANT_ISLAND_SHUGABUSH_BUY = "CgkIvZ_r2qUBEAIQOQ";
   private static final String PLANT_ISLAND_GHAZT_BUY = "CgkIvZ_r2qUBEAIQOg";

   private static void sendEvent(User user, String event) {
      String platform = (String)user.getProperty("client_platform");
      if (platform != null && platform.equalsIgnoreCase("android")) {
         ISFSObject metric = new SFSObject();
         metric.putUtfString("event", event);
         MSMExtension.getInstance().send("metric_event", metric, user);
      }

   }

   public static void reportLevel(User user, int level) {
      switch(level) {
      case 4:
         sendEvent(user, "CgkIvZ_r2qUBEAIQGA");
         break;
      case 7:
         sendEvent(user, "CgkIvZ_r2qUBEAIQGQ");
         break;
      case 8:
         sendEvent(user, "CgkIvZ_r2qUBEAIQGg");
         break;
      case 10:
         sendEvent(user, "CgkIvZ_r2qUBEAIQGw");
         break;
      case 14:
         sendEvent(user, "CgkIvZ_r2qUBEAIQHA");
         break;
      case 20:
         sendEvent(user, "CgkIvZ_r2qUBEAIQHQ");
         break;
      case 30:
         sendEvent(user, "CgkIvZ_r2qUBEAIQHg");
         break;
      case 40:
         sendEvent(user, "CgkIvZ_r2qUBEAIQHw");
         break;
      case 50:
         sendEvent(user, "CgkIvZ_r2qUBEAIQIA");
         break;
      case 60:
         sendEvent(user, "CgkIvZ_r2qUBEAIQIQ");
      }

   }

   public static void reportIsland(User user, int island) {
      switch(island) {
      case 2:
         sendEvent(user, "CgkIvZ_r2qUBEAIQIw");
         break;
      case 3:
         sendEvent(user, "CgkIvZ_r2qUBEAIQJA");
         break;
      case 4:
         sendEvent(user, "CgkIvZ_r2qUBEAIQJQ");
         break;
      case 5:
         sendEvent(user, "CgkIvZ_r2qUBEAIQJg");
         break;
      case 6:
         sendEvent(user, "CgkIvZ_r2qUBEAIQJw");
         break;
      case 7:
         sendEvent(user, "CgkIvZ_r2qUBEAIQKA");
         break;
      case 8:
         sendEvent(user, "CgkIvZ_r2qUBEAIQKQ");
         break;
      case 9:
         sendEvent(user, "CgkIvZ_r2qUBEAIQKg");
      }

   }

   public static void reportMonsterBreed(User user, int island, int monster) {
      if (island == 1) {
         switch(monster) {
         case 12:
            sendEvent(user, "CgkIvZ_r2qUBEAIQKw");
            break;
         case 13:
            sendEvent(user, "CgkIvZ_r2qUBEAIQLA");
            break;
         case 14:
            sendEvent(user, "CgkIvZ_r2qUBEAIQLQ");
            break;
         case 15:
            sendEvent(user, "CgkIvZ_r2qUBEAIQLg");
            break;
         case 24:
            sendEvent(user, "CgkIvZ_r2qUBEAIQLw");
            break;
         case 29:
            sendEvent(user, "CgkIvZ_r2qUBEAIQMA");
            break;
         case 50:
            sendEvent(user, "CgkIvZ_r2qUBEAIQMg");
            break;
         case 53:
            sendEvent(user, "CgkIvZ_r2qUBEAIQMQ");
         }
      }

   }

   public static void reportMonsterBuy(User user, int island, int monster) {
      if (island == 1) {
         switch(monster) {
         case 12:
            sendEvent(user, "CgkIvZ_r2qUBEAIQMw");
            break;
         case 13:
            sendEvent(user, "CgkIvZ_r2qUBEAIQNA");
            break;
         case 14:
            sendEvent(user, "CgkIvZ_r2qUBEAIQNQ");
            break;
         case 15:
            sendEvent(user, "CgkIvZ_r2qUBEAIQNg");
            break;
         case 24:
            sendEvent(user, "CgkIvZ_r2qUBEAIQNw");
            break;
         case 29:
            sendEvent(user, "CgkIvZ_r2qUBEAIQOA");
            break;
         case 50:
            sendEvent(user, "CgkIvZ_r2qUBEAIQOg");
            break;
         case 53:
            sendEvent(user, "CgkIvZ_r2qUBEAIQOQ");
         }
      }

   }

   public static void reportFriend(User user) {
      sendEvent(user, "CgkIvZ_r2qUBEAIQIg");
   }
}
