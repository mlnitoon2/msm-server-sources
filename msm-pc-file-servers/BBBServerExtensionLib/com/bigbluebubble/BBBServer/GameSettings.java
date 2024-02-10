package com.bigbluebubble.BBBServer;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.BBBServer.util.Settings;
import com.bigbluebubble.BBBServer.util.SimpleLogger;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameSettings {
   private static final boolean USE_CACHING = true;
   private static Map<String, Integer> cachedIntSettings;
   private static Map<String, Long> cachedLongSettings;
   private static Map<String, Boolean> cachedBooleanSettings;
   private static Map<String, Float> cachedFloatSettings;
   private static Map<String, Double> cachedDoubleSettings;
   private static Settings settings;

   public static void init() {
      cachedIntSettings = new HashMap();
      cachedLongSettings = new HashMap();
      cachedBooleanSettings = new HashMap();
      cachedFloatSettings = new HashMap();
      cachedDoubleSettings = new HashMap();
      settings = new Settings();
   }

   public static void loadFromDB(IDbWrapper db, String sql, boolean allowOverride) throws Exception {
      ISFSArray rows = db.query(sql);
      Iterator i = rows.iterator();

      while(true) {
         String key;
         String value;
         VersionInfo version;
         while(true) {
            if (!i.hasNext()) {
               return;
            }

            SFSObject setting = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
            key = setting.getUtfString("setting");
            value = setting.getUtfString("value");
            version = new VersionInfo(setting.containsKey("min_version") ? setting.getUtfString("min_version") : null);
            if (!settings.isDefined(key)) {
               break;
            }

            if (allowOverride) {
               SimpleLogger.trace(String.format("Overriding game setting: %s -->  new value = %s", key, value));
               break;
            }
         }

         settings.put(key, value, version);
      }
   }

   public static String get(String setting) {
      return settings.get(setting);
   }

   public static String get(String setting, String defaultValue) {
      return settings.get(setting, defaultValue);
   }

   public static int get(String setting, int defaultValue) {
      if (!cachedIntSettings.containsKey(setting)) {
         Integer val = settings.get(setting, defaultValue);
         cachedIntSettings.put(setting, val);
      }

      return (Integer)cachedIntSettings.get(setting);
   }

   public static int getInt(String setting) {
      if (!cachedIntSettings.containsKey(setting)) {
         Integer val = settings.getInt(setting);
         cachedIntSettings.put(setting, val);
      }

      return (Integer)cachedIntSettings.get(setting);
   }

   public static long get(String setting, long defaultValue) {
      if (!cachedLongSettings.containsKey(setting)) {
         Long val = settings.get(setting, defaultValue);
         cachedLongSettings.put(setting, val);
      }

      return (Long)cachedLongSettings.get(setting);
   }

   public static long getLong(String setting) {
      if (!cachedLongSettings.containsKey(setting)) {
         Long val = settings.getLong(setting);
         cachedLongSettings.put(setting, val);
      }

      return (Long)cachedLongSettings.get(setting);
   }

   public static boolean get(String setting, boolean defaultValue) {
      if (!cachedBooleanSettings.containsKey(setting)) {
         Boolean val = settings.get(setting, defaultValue);
         cachedBooleanSettings.put(setting, val);
      }

      return (Boolean)cachedBooleanSettings.get(setting);
   }

   public static boolean getBoolean(String setting) {
      if (!cachedBooleanSettings.containsKey(setting)) {
         Boolean val = settings.getBoolean(setting);
         cachedBooleanSettings.put(setting, val);
      }

      return (Boolean)cachedBooleanSettings.get(setting);
   }

   public static float get(String setting, float defaultValue) {
      if (!cachedFloatSettings.containsKey(setting)) {
         Float val = settings.get(setting, defaultValue);
         cachedFloatSettings.put(setting, val);
      }

      return (Float)cachedFloatSettings.get(setting);
   }

   public static float getFloat(String setting) throws NumberFormatException {
      if (!cachedFloatSettings.containsKey(setting)) {
         Float val = settings.getFloat(setting);
         cachedFloatSettings.put(setting, val);
      }

      return (Float)cachedFloatSettings.get(setting);
   }

   public static double get(String setting, double defaultValue) {
      if (!cachedDoubleSettings.containsKey(setting)) {
         Double val = settings.get(setting, defaultValue);
         cachedDoubleSettings.put(setting, val);
      }

      return (Double)cachedDoubleSettings.get(setting);
   }

   public static double getDouble(String setting) throws NumberFormatException {
      if (!cachedDoubleSettings.containsKey(setting)) {
         Double val = settings.getDouble(setting);
         cachedDoubleSettings.put(setting, val);
      }

      return (Double)cachedDoubleSettings.get(setting);
   }

   public static ISFSArray getUserGameSettings(VersionInfo clientServerVersion) {
      ISFSArray userGameSettings = new SFSArray();
      Iterator var2 = settings.keysSet().iterator();

      while(var2.hasNext()) {
         String key = (String)var2.next();
         if (key.startsWith("USER_") && clientServerVersion.greaterThanEqual(settings.getVersion(key))) {
            ISFSObject item = new SFSObject();
            item.putUtfString("key", key);
            item.putUtfString("value", settings.get(key));
            userGameSettings.addSFSObject(item);
         }
      }

      return userGameSettings;
   }
}
