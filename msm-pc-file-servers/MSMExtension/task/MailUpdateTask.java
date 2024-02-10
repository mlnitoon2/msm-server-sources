package com.bigbluebubble.mysingingmonsters.task;

import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.bigbluebubble.mysingingmonsters.data.Mail;
import com.bigbluebubble.mysingingmonsters.data.MailLookup;
import com.bigbluebubble.mysingingmonsters.data.SegmentationGroup;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

public class MailUpdateTask implements Runnable {
   public void run() {
      ConcurrentHashMap<Long, Mail> validMail = new ConcurrentHashMap();
      ConcurrentHashMap<Long, Mail> allMail = new ConcurrentHashMap();
      ConcurrentHashMap segments = new ConcurrentHashMap();

      try {
         String sql = "SELECT * FROM mail WHERE message_id < 1000000";
         ISFSArray results = MSMExtension.getInstance().getDB().query(sql);

         int i;
         Mail curMessage;
         for(i = 0; i < results.size(); ++i) {
            curMessage = new Mail(results.getSFSObject(i));
            allMail.put(curMessage.getID(), curMessage);
         }

         sql = "SELECT * FROM mail WHERE message_id < 1000000 AND enabled = 1 AND repeat_mode = 'never' AND start_date <= NOW() AND end_date > NOW()";
         results = MSMExtension.getInstance().getDB().query(sql);

         for(i = 0; i < results.size(); ++i) {
            curMessage = (Mail)allMail.get(results.getSFSObject(i).getLong("message_id"));
            validMail.put(curMessage.getID(), curMessage);
         }

         sql = "SELECT * FROM mail WHERE message_id < 1000000 AND enabled = 1 AND repeat_mode != 'never' AND start_date <= NOW()";
         results = MSMExtension.getInstance().getDB().query(sql);
         Calendar current = (Calendar)Calendar.getInstance(TimeZone.getTimeZone("UTC")).clone();
         current.setTimeInMillis(MSMExtension.CurrentDBTime());

         int i;
         for(i = 0; i < results.size(); ++i) {
            Mail curMessage = (Mail)allMail.get(results.getSFSObject(i).getLong("message_id"));
            Calendar start = (Calendar)Calendar.getInstance(TimeZone.getTimeZone("UTC")).clone();
            start.setTimeInMillis(curMessage.getStartDate());
            Calendar end = (Calendar)Calendar.getInstance(TimeZone.getTimeZone("UTC")).clone();
            end.setTimeInMillis(curMessage.getEndDate());
            if (curMessage.getRepeatMode() == Mail.MailRepeatMode.Never) {
               if (start.before(current) && end.after(current)) {
                  validMail.put(curMessage.getID(), curMessage);
               }
            } else {
               int repeatCount = curMessage.getRepeatCount();
               int repeat = 0;
               int calendarField = 5;
               int calendarValue = 1;
               if (curMessage.getRepeatMode() == Mail.MailRepeatMode.Weekly) {
                  calendarValue = 7;
               } else if (curMessage.getRepeatMode() == Mail.MailRepeatMode.Monthly) {
                  calendarField = 2;
               } else if (curMessage.getRepeatMode() == Mail.MailRepeatMode.Yearly) {
                  calendarField = 1;
               }

               while(start.before(current) && repeat <= repeatCount) {
                  if (end.after(current)) {
                     validMail.put(curMessage.getID(), curMessage);
                     break;
                  }

                  ++repeat;
                  start.setTimeInMillis(curMessage.getStartDate());
                  end.setTimeInMillis(curMessage.getEndDate());
                  start.add(calendarField, repeat * calendarValue);
                  end.add(calendarField, repeat * calendarValue);
               }

               curMessage.setCurRepeat(repeat);
            }
         }

         sql = "SELECT * FROM segments";
         results = MSMExtension.getInstance().getDB().query(sql);

         for(i = 0; i < results.size(); ++i) {
            SegmentationGroup curGroup = new SegmentationGroup(results.getSFSObject(i));
            segments.put(curGroup.getID(), curGroup);
         }

         MailLookup.updateMail(validMail, allMail, segments);
      } catch (Exception var15) {
         Logger.trace(var15, "Error reloading static mail data");
      }

   }
}
