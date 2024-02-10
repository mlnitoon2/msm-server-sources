package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.data.groups.TutorialGroup;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class QuestLookup {
   private static Map<Integer, ISFSObject> questIdLookup;
   private static Map<String, ISFSObject> questNameLookup;
   private static Map<Integer, VersionInfo> minServerVersionLookup;
   private static Map<Integer, QuestLookup.SupportedPlatforms> supportedPlatformsLookup;
   private static ISFSArray defaultTutorialQuests;

   public static void init(IDbWrapper db) throws Exception {
      String qhfSql = "SELECT * FROM quest_help_functions";
      ISFSArray qhfResults = db.query("SELECT * FROM quest_help_functions");
      HashMap<String, String> helpFunctions = new HashMap();
      Iterator qhfIterator = qhfResults.iterator();

      while(qhfIterator.hasNext()) {
         SFSObject qhf = (SFSObject)((SFSObject)((SFSDataWrapper)qhfIterator.next()).getObject());
         helpFunctions.put(qhf.getUtfString("name"), qhf.getUtfString("help_function"));
      }

      String sql = "SELECT * FROM quests ORDER BY id";
      ISFSArray results = db.query("SELECT * FROM quests ORDER BY id");
      init(results, helpFunctions);

      try {
         checkQuestDataForErrors(db);
      } catch (Exception var8) {
         Logger.trace(var8, "=====================================================================\n|                    INVALID DATA IN QUEST TABLE!                   |\n=====================================================================\n");
      }

   }

   public static void init(ISFSArray quests, HashMap<String, String> helpFunctions) {
      questIdLookup = new ConcurrentHashMap();
      questNameLookup = new ConcurrentHashMap();
      minServerVersionLookup = new ConcurrentHashMap();
      supportedPlatformsLookup = new ConcurrentHashMap();
      Iterator i = quests.iterator();

      while(i.hasNext()) {
         SFSObject quest = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         int questId = quest.getInt("id");
         questIdLookup.put(questId, quest);
         questNameLookup.put(quest.getUtfString("name"), quest);
         String helpDefinitionStr = quest.getUtfString("help_definition");
         String helpFunctionCustom = quest.getUtfString("help_function_custom");
         quest.removeElement("help_function_custom");
         quest.removeElement("help_definition");
         String helpFunction = "";
         String platform;
         if (helpFunctionCustom != null && helpFunctionCustom.length() > 0 && helpDefinitionStr.equalsIgnoreCase("{}")) {
            if (!helpDefinitionStr.equalsIgnoreCase("{}")) {
               Logger.trace(LogLevel.WARN, "Custom quest definition exists while definition isn't empty.");
            }

            helpFunction = helpFunctionCustom;
         } else if (helpDefinitionStr != null && !helpDefinitionStr.isEmpty() && helpDefinitionStr.startsWith("{")) {
            try {
               JSONObject helpDefinition = new JSONObject(helpDefinitionStr);
               String helpFunctionName = null;
               if (helpDefinition.has("f")) {
                  helpFunctionName = helpDefinition.getString("f");
               }

               if (helpFunctionName != null && helpFunctions.containsKey(helpFunctionName)) {
                  String templateHelpFunction = (String)helpFunctions.get(helpFunctionName);
                  if (helpDefinition.has("args")) {
                     JSONObject args = helpDefinition.getJSONObject("args");

                     String value;
                     for(Iterator keyIterator = args.keys(); keyIterator.hasNext(); helpFunction = templateHelpFunction.replace(platform, value)) {
                        platform = keyIterator.next().toString();
                        value = args.getString(platform);
                     }
                  } else {
                     helpFunction = templateHelpFunction;
                  }
               }
            } catch (JSONException var15) {
               Logger.trace((Exception)var15);
            }
         } else {
            Logger.trace(LogLevel.WARN, "Quest help was malformed for quest " + questId + ": ", helpDefinitionStr);
         }

         quest.putUtfString("help_function", helpFunction);
         if (quest.containsKey("min_server_version")) {
            minServerVersionLookup.put(questId, new VersionInfo(quest.getUtfString("min_server_version")));
         }

         if (quest.containsKey("platforms")) {
            String platforms = quest.getUtfString("platforms");
            if (!platforms.isEmpty()) {
               QuestLookup.SupportedPlatforms supportedPlatforms = new QuestLookup.SupportedPlatforms();
               String[] var22 = platforms.split(",");
               int var25 = var22.length;

               for(int var28 = 0; var28 < var25; ++var28) {
                  platform = var22[var28];
                  if (platform.startsWith("!")) {
                     supportedPlatforms.excludedPlatforms.add(platform.substring(1, platform.length()));
                  } else {
                     supportedPlatforms.includedPlatforms.add(platform);
                  }
               }

               supportedPlatformsLookup.put(questId, supportedPlatforms);
            }
         }

         quest.putSFSArray("goals", SFSArray.newFromJsonData(quest.getUtfString("goals")));
         SFSArray nextArrayObjects;
         if (quest.containsKey("tag")) {
            nextArrayObjects = SFSArray.newFromJsonData(quest.getUtfString("tag"));
            quest.removeElement("tag");
            quest.putSFSArray("tag", nextArrayObjects);
         }

         nextArrayObjects = new SFSArray();
         ISFSArray nextArray = SFSArray.newFromJsonData(quest.getUtfString("next"));
         Iterator nextItr = nextArray.iterator();

         while(nextItr.hasNext()) {
            String next = (String)((SFSDataWrapper)nextItr.next()).getObject();
            ISFSObject nextObject = new SFSObject();
            nextObject.putUtfString("quest", next);
            nextArrayObjects.addSFSObject(nextObject);
         }

         quest.putSFSArray("next", nextArrayObjects);
         ISFSArray rewardsArray = SFSArray.newFromJsonData(quest.getUtfString("rewards"));
         if (rewardsArray.size() > 0) {
            quest.putSFSObject("rewards", rewardsArray.getSFSObject(0));
         } else {
            quest.putSFSObject("rewards", new SFSObject());
         }
      }

      defaultTutorialQuests = new SFSArray();
      TutorialGroup t = (TutorialGroup)TutorialGroup.getGroupById(2);
      if (t != null) {
         ISFSArray initialQuests = t.getInitialQuests();
         if (initialQuests != null) {
            for(int curTut = 0; curTut < initialQuests.size(); ++curTut) {
               defaultTutorialQuests.addUtfString(initialQuests.getUtfString(curTut));
            }
         }
      } else {
         defaultTutorialQuests.addUtfString("BUY_MONSTER_C");
         defaultTutorialQuests.addUtfString("REACH_LEVEL_2");
         defaultTutorialQuests.addUtfString("REACH_LEVEL_3");
         defaultTutorialQuests.addUtfString("REACH_LEVEL_4");
         defaultTutorialQuests.addUtfString("REACH_LEVEL_7");
         defaultTutorialQuests.addUtfString("TRIGGER_VER_POST_300_QUESTS");
      }

   }

   public static ISFSArray getDefaultTutorialQuests() {
      return defaultTutorialQuests;
   }

   public static ISFSObject getById(int id, String platform, String subplatform, VersionInfo version) {
      try {
         ISFSObject questData = (ISFSObject)questIdLookup.get(id);
         if (!supportedPlatformsLookup.containsKey(id) || ((QuestLookup.SupportedPlatforms)supportedPlatformsLookup.get(id)).isSupportedPlatform(platform) && ((QuestLookup.SupportedPlatforms)supportedPlatformsLookup.get(id)).isSupportedPlatform(subplatform)) {
            return questData;
         } else {
            Logger.trace("QuestLookup: platform not supported for quest" + id);
            return null;
         }
      } catch (NullPointerException var5) {
         Logger.trace((Exception)var5, "ERROR: Unable to find quest with id '" + id + "'");
         return null;
      }
   }

   public static ISFSObject getByName(String name) {
      try {
         return (ISFSObject)questNameLookup.get(name);
      } catch (NullPointerException var2) {
         Logger.trace((Exception)var2, "ERROR: Unable to find quest with name '" + name + "'");
         return null;
      }
   }

   public static Collection<ISFSObject> getValues() {
      return questIdLookup.values();
   }

   private static void checkQuestDataForErrors(IDbWrapper db) throws Exception {
      StringBuilder sb = new StringBuilder();
      String sql = "SELECT * FROM quests";
      Object[] query_params = new Object[0];
      SFSArray records = db.query(sql, query_params);

      for(int i = 0; i < records.size(); ++i) {
         ISFSObject curQuest = records.getSFSObject(i);
         String curQuestName = curQuest.getUtfString("name");
         String nextArrayStr = curQuest.getUtfString("next");
         ISFSArray nextArray = SFSArray.newFromJsonData(nextArrayStr);
         Iterator nextItr = nextArray.iterator();

         while(nextItr.hasNext()) {
            String next = (String)((SFSDataWrapper)nextItr.next()).getObject();
            boolean isValid = isAValidQuest(next, records);
            if (!isValid) {
               sb.append("\n------------------------------------------------------------\n");
               sb.append("ERROR: Quest has invalid NEXT quest: \n");
               sb.append("* QUEST: " + curQuestName + "\n");
               sb.append("* Invalid next value: Quest " + next + " does not exist\n");
               sb.append("------------------------------------------------------------\n");
            }
         }
      }

      String errorMsg = sb.toString();
      if (!errorMsg.isEmpty()) {
         Logger.trace(errorMsg);
      }

   }

   private static boolean isAValidQuest(String questName, SFSArray records) {
      for(int i = 0; i < records.size(); ++i) {
         ISFSObject curQuest = records.getSFSObject(i);
         String curQuestName = curQuest.getUtfString("name");
         if (curQuestName.equals(questName)) {
            return true;
         }
      }

      return false;
   }

   static class SupportedPlatforms {
      public List<String> includedPlatforms = new ArrayList();
      public List<String> excludedPlatforms = new ArrayList();

      public SupportedPlatforms() {
      }

      public boolean isSupportedPlatform(String clientPlatform) {
         if (!this.includedPlatforms.isEmpty()) {
            return this.includedPlatforms.contains(clientPlatform);
         } else if (!this.excludedPlatforms.isEmpty()) {
            return !this.excludedPlatforms.contains(clientPlatform);
         } else {
            return true;
         }
      }
   }
}
