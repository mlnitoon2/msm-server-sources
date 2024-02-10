package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticDataLookup;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IslandThemeLookup extends StaticDataLookup<IslandTheme> {
   private static IslandThemeLookup instance;
   public static Map<Integer, IslandTheme> themes;
   static final String CACHE_NAME = "island_theme_data";

   public static IslandThemeLookup getInstance() {
      return instance;
   }

   private IslandThemeLookup(IDbWrapper db) throws Exception {
      themes = new ConcurrentHashMap();
      String sql = "SELECT * FROM island_themes";
      ISFSArray results = db.query("SELECT * FROM island_themes");
      Iterator i = results.iterator();

      while(i.hasNext()) {
         SFSObject islandThemeData = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         if (islandThemeData != null) {
            IslandTheme islandTheme = new IslandTheme(islandThemeData);
            themes.put(islandTheme.getId(), islandTheme);
         }
      }

   }

   public static void init(IDbWrapper db) throws Exception {
      instance = new IslandThemeLookup(db);
   }

   public static IslandTheme get(int id) {
      return instance.getEntry(id);
   }

   public String getCacheName() {
      return "island_theme_data";
   }

   public Iterable<IslandTheme> entries() {
      return themes.values();
   }

   public IslandTheme getEntry(int id) {
      return (IslandTheme)themes.get(id);
   }
}
