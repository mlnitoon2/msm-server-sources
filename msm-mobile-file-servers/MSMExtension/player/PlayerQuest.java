package com.bigbluebubble.mysingingmonsters.player;

import com.bigbluebubble.BBBServer.util.SFSHelpers;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.data.QuestLookup;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataType;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

public class PlayerQuest {
   private static final int TYPE_AND = 0;
   private static final int TYPE_OR = 1;
   private final ISFSObject data_;
   private Object status_;
   private String statusString_;
   private long id_;
   private int questId_;
   private int user_;
   private boolean collected_;
   private boolean new_;

   public static Object JSONParse(String json) {
      Object obj = null;
      if (json.equals("true")) {
         obj = new Boolean(true);
      } else if (json.equals("false")) {
         obj = new Boolean(false);
      } else {
         try {
            obj = new JSONArray(json);
         } catch (Exception var5) {
            try {
               obj = new JSONObject(json);
            } catch (Exception var4) {
            }
         }
      }

      return obj;
   }

   public static String JSONCreate(Object obj) {
      String json = "";
      if (obj instanceof Boolean) {
         json = (Boolean)obj ? "true" : "false";
      } else if (obj instanceof JSONArray || obj instanceof JSONObject) {
         json = obj.toString();
      }

      return json;
   }

   private static boolean lt(Object left, Object right) {
      if (left instanceof Integer && right instanceof Integer) {
         return (Integer)left < (Integer)right;
      } else if (left instanceof VersionInfo && right instanceof VersionInfo) {
         return ((VersionInfo)left).compareTo((VersionInfo)right) < 0;
      } else {
         return false;
      }
   }

   private static boolean lte(Object left, Object right) {
      if (left instanceof Integer && right instanceof Integer) {
         return (Integer)left <= (Integer)right;
      } else if (left instanceof VersionInfo && right instanceof VersionInfo) {
         return ((VersionInfo)left).compareTo((VersionInfo)right) <= 0;
      } else {
         return false;
      }
   }

   private static boolean ne(Object left, Object right) {
      return !left.equals(right);
   }

   private static boolean gte(Object left, Object right) {
      if (left instanceof Integer && right instanceof Integer) {
         return (Integer)left >= (Integer)right;
      } else if (left instanceof VersionInfo && right instanceof VersionInfo) {
         return ((VersionInfo)left).compareTo((VersionInfo)right) >= 0;
      } else {
         return false;
      }
   }

   private static boolean gt(Object left, Object right) {
      if (left instanceof Integer && right instanceof Integer) {
         return (Integer)left > (Integer)right;
      } else if (left instanceof VersionInfo && right instanceof VersionInfo) {
         return ((VersionInfo)left).compareTo((VersionInfo)right) > 0;
      } else {
         return false;
      }
   }

   private static boolean equal(Object left, Object right) {
      return left.equals(right);
   }

   public static ISFSObject getInitialSFSObject(int id, int questId, int userId, String status, boolean isNew, boolean collected) {
      ISFSObject o = new SFSObject();
      o.putInt("id", id);
      o.putInt("quest_id", questId);
      o.putInt("user", userId);
      o.putUtfString("status", status);
      o.putInt("new", isNew ? 1 : 0);
      o.putInt("collected", collected ? 1 : 0);
      return o;
   }

   public PlayerQuest(Player player, ISFSObject logData) {
      this.data_ = QuestLookup.getById(logData.getInt("quest_id"), player.getPlatform(), player.getSubplatform(), player.lastClientVersion());
      this.statusString_ = logData.getUtfString("status");
      this.questId_ = logData.getInt("quest_id");
      this.user_ = logData.getInt("user");
      this.collected_ = logData.getInt("collected") != 0;
      this.new_ = logData.getInt("new") != 0;
      this.id_ = SFSHelpers.getLong("id", logData);
      this.status_ = JSONParse(this.statusString_);
   }

   public PlayerQuest(Player player, String compactJSON) throws Exception {
      int id = player.getNextQuestIndex();
      int userId = (int)player.getPlayerId();
      JSONArray ja = new JSONArray(compactJSON);
      this.questId_ = ja.getInt(0);
      this.statusString_ = ja.getString(1).replace("\\\"", "\"");
      this.new_ = ja.getInt(2) != 0;
      this.id_ = (long)id;
      this.user_ = userId;
      this.collected_ = false;
      this.data_ = QuestLookup.getById(this.questId_, player.getPlatform(), player.getSubplatform(), player.lastClientVersion());
      this.status_ = JSONParse(this.statusString_);
   }

   public boolean invalid() {
      return this.data_ == null;
   }

   public ISFSArray toSFS() {
      if (this.invalid()) {
         return null;
      } else {
         ISFSArray quest = new SFSArray();
         quest.addSFSObject(this.getLog());
         quest.addSFSObject(this.data_);
         return quest;
      }
   }

   private int getType() {
      if (this.invalid()) {
         return -1;
      } else {
         return this.data_.getUtfString("type").equalsIgnoreCase("AND") ? 0 : 1;
      }
   }

   public ISFSObject getLog() {
      ISFSObject s = new SFSObject();
      s.putUtfString("status", this.getStatus());
      s.putInt("collected", this.collected());
      s.putInt("user", this.getUser());
      s.putInt("new", this.isNew());
      s.putInt("quest_id", this.questId_);
      s.put("id", this.getWrappedId());
      return s;
   }

   public String getCompactJSON() {
      return "[" + this.getDataId() + ",\"" + this.getStatus().replace("\"", "\\\"") + "\"," + this.isNew() + "]";
   }

   public void markRead() {
      this.new_ = false;
   }

   public int isNew() {
      return this.new_ ? 1 : 0;
   }

   public int collected() {
      return this.collected_ ? 1 : 0;
   }

   public void collect() {
      this.collected_ = true;
   }

   public boolean isComplete() {
      return this.status_ instanceof Boolean && (Boolean)this.status_;
   }

   public long getId() {
      return this.id_;
   }

   public SFSDataWrapper getWrappedId() {
      return this.getId() > 2147483647L ? new SFSDataWrapper(SFSDataType.LONG, this.getId()) : new SFSDataWrapper(SFSDataType.INT, (int)this.getId());
   }

   public int getDataId() {
      return this.invalid() ? -1 : this.data_.getInt("id");
   }

   public String getStatus() {
      return this.statusString_;
   }

   public int getUser() {
      return this.user_;
   }

   public void adminMarkStatusComplete() throws Exception {
      if (!(this.status_ instanceof Boolean) || !(Boolean)this.status_) {
         if (!this.invalid()) {
            ISFSArray goals = this.data_.getSFSArray("goals");
            int i;
            if (this.status_ instanceof Boolean && !(Boolean)this.status_) {
               this.status_ = new JSONArray();

               for(i = 0; i < goals.size(); ++i) {
                  ((JSONArray)this.status_).put(true);
               }
            } else {
               for(i = 0; i < goals.size(); ++i) {
                  ((JSONArray)this.status_).put(i, true);
               }
            }

            this.status_ = consolodateStatusAfterUpdate(this.status_, this.getType());
            this.statusString_ = JSONCreate(this.status_);
         }
      }
   }

   public String getName() {
      return this.invalid() ? "" : this.data_.getUtfString("name");
   }

   public ISFSArray getNext() {
      return this.invalid() ? null : this.data_.getSFSArray("next");
   }

   public ISFSObject getRewards() {
      return this.invalid() ? null : this.data_.getSFSObject("rewards");
   }

   public ISFSArray getGoals() {
      return this.invalid() ? null : this.data_.getSFSArray("goals");
   }

   public boolean onQuestEvent(JSONObject event) {
      if (this.invalid()) {
         return false;
      } else {
         this.status_ = onQuestEvent(event, this.data_.getSFSArray("goals"), this.status_, this.getType());
         this.statusString_ = JSONCreate(this.status_);
         return this.isComplete();
      }
   }

   public static boolean Evaluate(String evaluator, Object eventValue, Object goalValue) {
      if (evaluator.equals("<")) {
         return lt(eventValue, goalValue);
      } else if (evaluator.equals("<=")) {
         return lte(eventValue, goalValue);
      } else if (!evaluator.equals("<>") && !evaluator.equals("!=")) {
         if (evaluator.equals(">=")) {
            return gte(eventValue, goalValue);
         } else {
            return evaluator.equals(">") ? gt(eventValue, goalValue) : equal(eventValue, goalValue);
         }
      } else {
         return ne(eventValue, goalValue);
      }
   }

   public static Object onQuestEvent(JSONObject event, ISFSArray goals, Object status, int type) {
      int i = false;
      if (status instanceof Boolean && (Boolean)status) {
         return status;
      } else {
         int i;
         if (status instanceof Boolean && !(Boolean)status) {
            status = new JSONArray();

            for(i = 0; i < goals.size(); ++i) {
               ((JSONArray)status).put(false);
            }
         }

         try {
            for(i = 0; i < goals.size(); ++i) {
               ISFSObject goal = goals.getSFSObject(i);
               if (!((JSONArray)status).optBoolean(i, false)) {
                  HashMap<String, ISFSObject> goalProps = new HashMap();
                  Iterator var7 = goal.getKeys().iterator();

                  while(var7.hasNext()) {
                     String goalProp = (String)var7.next();
                     if (!goalProp.equals("eval") && !goalProp.equals("num") && !goalProp.equals("unique")) {
                        if (goal.get(goalProp).getObject() instanceof ISFSObject) {
                           ISFSObject goalInfo = SFSObject.newFromJsonData(goal.getSFSObject(goalProp).toJson());
                           if (!goalInfo.containsKey("eval") && goal.containsKey("eval")) {
                              goalInfo.putUtfString("eval", goal.getUtfString("eval"));
                           }

                           goalProps.put(goalProp, goalInfo);
                        } else {
                           ISFSObject goalInfo = new SFSObject();
                           goalInfo.put(goalProp, goal.get(goalProp));
                           if (goal.containsKey("eval")) {
                              goalInfo.putUtfString("eval", goal.getUtfString("eval"));
                           }

                           goalProps.put(goalProp, goalInfo);
                        }
                     }
                  }

                  String[] eventProps = JSONObject.getNames(event);
                  int foundCount = 0;
                  String[] var20 = eventProps;
                  int var10 = eventProps.length;

                  String prop;
                  for(int var11 = 0; var11 < var10; ++var11) {
                     prop = var20[var11];
                     if (goalProps.containsKey(prop)) {
                        ++foundCount;
                     }
                  }

                  if (foundCount == goalProps.size()) {
                     boolean allValid = true;
                     JSONArray eventValues = new JSONArray();
                     Iterator var23 = goalProps.keySet().iterator();

                     while(var23.hasNext()) {
                        prop = (String)var23.next();
                        if (!allValid) {
                           break;
                        }

                        ISFSObject goalInfo = (ISFSObject)goalProps.get(prop);
                        Object eventValue = event.get(prop);
                        allValid &= isValid(goalInfo, eventValue, prop);
                        eventValues.put(eventValue);
                     }

                     if (allValid) {
                        if (goal.containsKey("num")) {
                           if (((JSONArray)status).get(i) instanceof Boolean) {
                              ((JSONArray)status).put(i, new JSONArray());
                           }

                           JSONArray numArray = (JSONArray)((JSONArray)status).get(i);
                           boolean insert = true;
                           if (goal.containsKey("unique")) {
                              for(int uniqueIdx = 0; insert && uniqueIdx < numArray.length(); ++uniqueIdx) {
                                 if (eventValues.length() == 1 && eventValues.get(0).equals(numArray.get(uniqueIdx))) {
                                    insert = false;
                                 } else if (eventValues.equals(numArray.get(uniqueIdx))) {
                                    insert = false;
                                 }
                              }
                           }

                           if (insert) {
                              if (eventValues.length() == 1) {
                                 numArray.put(eventValues.get(0));
                              } else {
                                 numArray.put(eventValues);
                              }
                           }

                           if (numArray.length() >= goal.getInt("num")) {
                              ((JSONArray)status).put(i, true);
                           }
                        } else {
                           ((JSONArray)status).put(i, true);
                        }
                     }
                  }
               }
            }

            status = consolodateStatusAfterUpdate(status, type);
            return status;
         } catch (Exception var15) {
            Logger.trace(var15);
            return false;
         }
      }
   }

   private static Object consolodateStatusAfterUpdate(Object status, int type) {
      boolean complete;
      int i;
      if (type == 0) {
         complete = true;

         for(i = 0; i < ((JSONArray)status).length(); ++i) {
            complete = complete && ((JSONArray)status).optBoolean(i);
         }

         if (complete) {
            status = new Boolean(true);
         }
      } else {
         complete = false;

         for(i = 0; !complete && i < ((JSONArray)status).length(); ++i) {
            complete = complete || ((JSONArray)status).optBoolean(i);
         }

         if (complete) {
            status = new Boolean(true);
         }
      }

      return status;
   }

   private static boolean isValid(ISFSObject goal, Object eventValue, String prop) throws Exception {
      boolean validated = false;
      if (goal.containsKey("eval")) {
         String evaluator = goal.getUtfString("eval");
         Object goalValue = goal.get(prop).getObject();
         if (goalValue instanceof ISFSArray) {
            ISFSArray goalValues = (ISFSArray)goalValue;
            if (eventValue instanceof JSONArray) {
               JSONArray eventArray = (JSONArray)eventValue;
               ArrayList<Boolean> goalCounter = new ArrayList();

               int g;
               for(g = 0; g < goalValues.size(); ++g) {
                  goalCounter.add(false);
               }

               for(g = 0; g < eventArray.length(); ++g) {
                  for(int g = 0; g < goalValues.size(); ++g) {
                     Object currentGoalValue = goalValues.getElementAt(g);
                     Object currentEventValue = eventArray.get(g);
                     if (prop.compareTo("client_version") == 0 && currentGoalValue instanceof String && currentEventValue instanceof String) {
                        currentGoalValue = new VersionInfo((String)currentGoalValue);
                        currentEventValue = new VersionInfo((String)currentEventValue);
                     }

                     if (!(Boolean)goalCounter.get(g) && Evaluate(evaluator, currentEventValue, currentGoalValue)) {
                        goalCounter.set(g, true);
                     }
                  }
               }

               validated = true;

               for(g = 0; validated && g < goalCounter.size(); ++g) {
                  validated &= (Boolean)goalCounter.get(g);
               }
            } else {
               for(int g = 0; !validated && g < goalValues.size(); ++g) {
                  Object currentGoalValue = goalValues.getElementAt(g);
                  Object currentEventValue = eventValue;
                  if (prop.compareTo("client_version") == 0 && currentGoalValue instanceof String && eventValue instanceof String) {
                     currentGoalValue = new VersionInfo((String)currentGoalValue);
                     currentEventValue = new VersionInfo((String)eventValue);
                  }

                  validated |= Evaluate(evaluator, currentEventValue, currentGoalValue);
               }
            }
         } else {
            Object currentGoalValue = goalValue;
            Object currentEventValue = eventValue;
            if (prop.compareTo("client_version") == 0 && goalValue instanceof String && eventValue instanceof String) {
               currentGoalValue = new VersionInfo((String)goalValue);
               currentEventValue = new VersionInfo((String)eventValue);
            }

            validated = Evaluate(evaluator, currentEventValue, currentGoalValue);
         }
      } else {
         validated = true;
      }

      return validated;
   }

   public ArrayList<String> getGoalEvents() {
      ArrayList<String> events = new ArrayList();
      if (this.invalid()) {
         return events;
      } else {
         ISFSArray goals = this.data_.getSFSArray("goals");

         for(int i = 0; i < goals.size(); ++i) {
            String[] keys = (String[])goals.getSFSObject(i).getKeys().toArray(new String[0]);

            for(int j = 0; j < keys.length; ++j) {
               if (!events.contains(keys[j])) {
                  events.add(keys[j]);
               }
            }
         }

         return events;
      }
   }
}
