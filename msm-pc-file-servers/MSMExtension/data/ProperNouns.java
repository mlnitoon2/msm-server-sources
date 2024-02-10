package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ProperNouns {
   private static ArrayList<String> _properNouns;

   public static void init(IDbWrapper db) {
      ArrayList nouns = new ArrayList();

      try {
         String sql = "SELECT * FROM proper_nouns";
         ISFSArray results = db.query("SELECT * FROM proper_nouns", new Object[0]);

         for(int i = 0; i < results.size(); ++i) {
            nouns.add(results.getSFSObject(i).getUtfString("noun"));
         }

         Collections.sort(nouns, new Comparator<String>() {
            public int compare(String s1, String s2) {
               if (s1.length() == s2.length()) {
                  return s1.compareTo(s2);
               } else {
                  return s1.length() > s2.length() ? -1 : 1;
               }
            }
         });
      } catch (Exception var5) {
         Logger.trace(LogLevel.WARN, "Unable to read from proper_nouns table");
      }

      _properNouns = nouns;
   }

   public static ArrayList<String> getNouns() {
      return _properNouns;
   }
}
