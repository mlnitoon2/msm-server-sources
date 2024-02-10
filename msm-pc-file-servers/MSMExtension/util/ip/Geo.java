package com.bigbluebubble.mysingingmonsters.util.ip;

import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import java.util.HashMap;
import java.util.TreeMap;

public class Geo {
   private static TreeMap<Long, Long> _geo_ips;
   private static HashMap<Long, String> _geo_codes;

   public static void init() throws Exception {
      _geo_ips = new TreeMap();
      _geo_codes = new HashMap();

      try {
         String GEO_IPS_SQL = "SELECT ip_max, geo_id FROM geo_ipv4";
         ISFSArray results = MSMExtension.getInstance().getDB().query(GEO_IPS_SQL, new Object[0]);

         for(int i = 0; i < results.size(); ++i) {
            _geo_ips.put(results.getSFSObject(i).getLong("ip_max"), results.getSFSObject(i).getLong("geo_id"));
         }

         String GEO_CODES_SQL = "SELECT geo_id, country_code FROM geo_codes";
         results = MSMExtension.getInstance().getDB().query(GEO_CODES_SQL, new Object[0]);

         for(int i = 0; i < results.size(); ++i) {
            _geo_codes.put(results.getSFSObject(i).getLong("geo_id"), results.getSFSObject(i).getUtfString("country_code"));
         }

      } catch (Exception var4) {
         Logger.trace(var4, "ERROR: Failed to cache GEOLOCATE data");
         throw var4;
      }
   }

   public static String locate(String ipAddress) {
      try {
         if (_geo_codes != null && _geo_ips != null) {
            long ip = convertIp(ipAddress);
            return (String)_geo_codes.get(_geo_ips.ceilingEntry(ip).getValue());
         } else {
            return null;
         }
      } catch (Exception var3) {
         Logger.trace(var3, String.format("GEOLOCATE: Failed to lookup ip address '%s'", ipAddress));
         return null;
      }
   }

   public static String convertIp(Long ip) throws Exception {
      if (ip >= 0L && ip <= 4294967295L) {
         return String.format("%d.%d.%d.%d", (ip & 4278190080L) >> 24, (ip & 16711680L) >> 16, (ip & 65280L) >> 8, ip & 255L);
      } else {
         throw new Exception("Invalid IP address: " + ip);
      }
   }

   public static long convertIp(String ipAddress) throws Exception {
      String[] ip = ipAddress.split("\\:");
      String[] parts = ip[0].split("\\.");
      if (parts.length != 4) {
         throw new Exception("Invalid IP address: " + ipAddress);
      } else {
         long a = Long.parseLong(parts[0]);
         long b = Long.parseLong(parts[1]);
         long c = Long.parseLong(parts[2]);
         long d = Long.parseLong(parts[3]);
         if ((a | 255L) == 255L && (b | 255L) == 255L && (c | 255L) == 255L && (d | 255L) == 255L) {
            return (((a << 8) + b << 8) + c << 8) + d;
         } else {
            throw new Exception("Invalid IP address: " + ipAddress);
         }
      }
   }
}
