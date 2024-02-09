package com.bigbluebubble.BBBServer.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Settings {
   private static Map<String, String> _settings;
   private static Map<String, VersionInfo> _versions;

   public Settings() {
      _settings = new HashMap();
      _versions = new HashMap();
   }

   public String get(String setting) {
      return (String)_settings.get(setting);
   }

   public void put(String k, String v, VersionInfo minVersion) {
      _settings.put(k, v);
      if (!_versions.containsKey(k)) {
         _versions.put(k, minVersion);
      }

   }

   public String get(String setting, String defaultValue) {
      String strVal = this.get(setting);
      return strVal != null ? strVal : defaultValue;
   }

   public int get(String setting, int defaultValue) {
      String strVal = this.get(setting);
      if (strVal != null) {
         int intValue = Integer.parseInt(strVal);
         return intValue;
      } else {
         return defaultValue;
      }
   }

   public int getInt(String setting) throws NumberFormatException {
      if (!this.isDefined(setting)) {
         SimpleLogger.trace(LogLevel.ERROR, "Missing int setting", setting);
      }

      return Integer.parseInt(this.get(setting));
   }

   public long get(String setting, long defaultValue) {
      String strVal = this.get(setting);
      if (strVal != null) {
         long intValue = Long.parseLong(strVal);
         return intValue;
      } else {
         return defaultValue;
      }
   }

   public long getLong(String setting) throws NumberFormatException {
      if (!this.isDefined(setting)) {
         SimpleLogger.trace(LogLevel.ERROR, "Missing long setting", setting);
      }

      return Long.parseLong(this.get(setting));
   }

   public boolean get(String setting, boolean defaultValue) {
      String strVal = this.get(setting);
      if (strVal != null) {
         boolean boolValue = Boolean.parseBoolean(strVal);
         return boolValue;
      } else {
         return defaultValue;
      }
   }

   public boolean getBoolean(String setting) {
      if (!this.isDefined(setting)) {
         SimpleLogger.trace(LogLevel.ERROR, "Missing boolean setting", setting);
      }

      return Boolean.parseBoolean(this.get(setting));
   }

   public float get(String setting, float defaultValue) {
      String strVal = this.get(setting);
      if (strVal != null) {
         float floatValue = Float.parseFloat(strVal);
         return floatValue;
      } else {
         return defaultValue;
      }
   }

   public float getFloat(String setting) throws NumberFormatException {
      if (!this.isDefined(setting)) {
         SimpleLogger.trace(LogLevel.ERROR, "Missing float setting", setting);
      }

      return Float.parseFloat(this.get(setting));
   }

   public double get(String setting, double defaultValue) {
      String strVal = this.get(setting);
      if (strVal != null) {
         double doubleValue = Double.parseDouble(strVal);
         return doubleValue;
      } else {
         return defaultValue;
      }
   }

   public double getDouble(String setting) throws NumberFormatException {
      if (!this.isDefined(setting)) {
         SimpleLogger.trace(LogLevel.ERROR, "Missing double setting", setting);
      }

      return Double.parseDouble(this.get(setting));
   }

   public VersionInfo getVersion(String setting) {
      return (VersionInfo)_versions.get(setting);
   }

   public Set<String> keysSet() {
      return _settings.keySet();
   }

   public boolean isDefined(String key) {
      return _settings.containsKey(key);
   }
}
