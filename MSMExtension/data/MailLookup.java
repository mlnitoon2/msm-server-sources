package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.mysingingmonsters.player.Player;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class MailLookup {
   public static ConcurrentHashMap<Long, Mail> validMail;
   public static ConcurrentHashMap<Long, Mail> allMail;
   public static ConcurrentHashMap<Long, SegmentationGroup> segments;
   public static ConcurrentHashMap<String, SegmentationGroup.SegmentCompare> segmentFunctions;

   public static void init() {
      validMail = new ConcurrentHashMap();
      allMail = new ConcurrentHashMap();
      segments = new ConcurrentHashMap();
      segmentFunctions = new ConcurrentHashMap();
      segmentFunctions.put("level", SegmentationGroup.SegmentCompare.LEVEL);
      segmentFunctions.put("days_since_install", SegmentationGroup.SegmentCompare.DAYS_SINCE_INSTALL);
      segmentFunctions.put("last_login", SegmentationGroup.SegmentCompare.LAST_LOGIN);
      segmentFunctions.put("date_created", SegmentationGroup.SegmentCompare.DATE_CREATED);
      segmentFunctions.put("amount_spent", SegmentationGroup.SegmentCompare.AMOUNT_SPENT);
      segmentFunctions.put("daily_login_day", SegmentationGroup.SegmentCompare.DAILY_LOGIN_DAY);
      segmentFunctions.put("version", SegmentationGroup.SegmentCompare.VERSION);
      segmentFunctions.put("platform", SegmentationGroup.SegmentCompare.PLATFORM);
      segmentFunctions.put("group", SegmentationGroup.SegmentCompare.GROUP);
      segmentFunctions.put("island", SegmentationGroup.SegmentCompare.ISLAND);
      segmentFunctions.put("entity", SegmentationGroup.SegmentCompare.ENTITY);
   }

   public static Mail get(long messageId) {
      return (Mail)allMail.get(messageId);
   }

   public static Collection<Mail> getValidValues() {
      return validMail.values();
   }

   public static boolean inSegment(long segmentId, Player p) {
      SegmentationGroup g = (SegmentationGroup)segments.get(segmentId);
      if (g == null) {
         return false;
      } else {
         ISFSObject definition = g.getDefinition();
         ISFSArray conditions = definition.getSFSArray("conditions");
         boolean ands = definition.getUtfString("type").equals("and");

         for(int i = 0; i < conditions.size(); ++i) {
            ISFSObject cond = conditions.getSFSObject(i);
            SegmentationGroup.SegmentCompare func = (SegmentationGroup.SegmentCompare)segmentFunctions.get(cond.getUtfString("property"));
            if (func == null) {
               if (ands) {
                  return false;
               }
            } else {
               boolean result = func.compare(cond.getUtfString("operator"), p, cond.getUtfString("value"));
               if (ands && !result) {
                  return false;
               }

               if (!ands && result) {
                  return true;
               }
            }
         }

         return ands;
      }
   }

   public static void updateMail(ConcurrentHashMap<Long, Mail> newValid, ConcurrentHashMap<Long, Mail> newAll, ConcurrentHashMap<Long, SegmentationGroup> newSeg) {
      validMail = newValid;
      allMail = newAll;
      segments = newSeg;
   }
}
