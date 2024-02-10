package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.mysingingmonsters.MSMExtension;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Calendar;
import java.util.TimeZone;

public class Mail {
   private static final String MAIL_ID = "message_id";
   private static final String MAIL_TITLE = "title";
   private static final String MAIL_SHORT_TITLE = "short_title";
   private static final String MAIL_MESSAGE = "message";
   private static final String MAIL_SENDER = "from";
   private static final String MAIL_ICON = "icon";
   private static final String MAIL_ATTACHMENT = "attachment";
   private static final String MAIL_START_DATE = "start_date";
   private static final String MAIL_END_DATE = "end_date";
   private static final String MAIL_REPEAT_MODE = "repeat_mode";
   private static final String MAIL_REPEAT_COUNT = "repeat_count";
   private static final String MAIL_EXPIRY = "expiry";
   private static final String MAIL_URGENT_FLAG = "urgent";
   private static final String MAIL_ENABLED_FLAG = "enabled";
   private static final String MAIL_SEGMENTATION = "segmentation";
   private static final String MAIL_BBBID = "bbb_id";
   private long id = 0L;
   private String title = null;
   private String shortTitle = null;
   private String message = null;
   private String sender = null;
   private String icon = null;
   private ISFSObject attachment = null;
   private long startDate = 0L;
   private long endDate = 0L;
   private Mail.MailRepeatMode repeatMode;
   private int repeatCount;
   private int curRepeat;
   private int expiry;
   private boolean urgent;
   private boolean enabled;
   private ISFSObject segmentation;
   private long bbbId;

   public Mail(ISFSObject mailData) {
      this.repeatMode = Mail.MailRepeatMode.Never;
      this.repeatCount = 0;
      this.curRepeat = 0;
      this.expiry = 0;
      this.urgent = false;
      this.enabled = true;
      this.segmentation = null;
      this.bbbId = 0L;
      this.id = mailData.getLong("message_id");
      this.title = mailData.getUtfString("title");
      if (mailData.containsKey("short_title")) {
         this.shortTitle = mailData.getUtfString("short_title");
      }

      this.message = mailData.getUtfString("message");
      this.sender = mailData.getUtfString("from");
      this.icon = mailData.getUtfString("icon");
      if (mailData.containsKey("attachment")) {
         this.attachment = SFSObject.newFromJsonData(mailData.getUtfString("attachment"));
      }

      this.startDate = mailData.getLong("start_date");
      this.endDate = mailData.getLong("end_date");
      String repeatString = mailData.getUtfString("repeat_mode");
      if (repeatString.equals("daily")) {
         this.repeatMode = Mail.MailRepeatMode.Daily;
      } else if (repeatString.equals("weekly")) {
         this.repeatMode = Mail.MailRepeatMode.Weekly;
      } else if (repeatString.equals("monthly")) {
         this.repeatMode = Mail.MailRepeatMode.Monthly;
      } else if (repeatString.equals("yearly")) {
         this.repeatMode = Mail.MailRepeatMode.Yearly;
      }

      this.repeatCount = mailData.getInt("repeat_count");
      this.expiry = mailData.getInt("expiry");
      this.urgent = mailData.getInt("urgent") == 1;
      this.enabled = mailData.getInt("enabled") == 1;
      if (mailData.containsKey("segmentation")) {
         this.segmentation = SFSObject.newFromJsonData(mailData.getUtfString("segmentation"));
      }

      if (mailData.containsKey("bbb_id")) {
         this.bbbId = mailData.getLong("bbb_id");
      }

   }

   public ISFSObject toSFSObject() {
      ISFSObject obj = new SFSObject();
      obj.putLong("message_id", this.id);
      obj.putUtfString("title", this.title);
      if (this.shortTitle != null) {
         obj.putUtfString("short_title", this.shortTitle);
      }

      obj.putUtfString("message", this.message);
      obj.putUtfString("from", this.sender);
      obj.putUtfString("icon", this.icon);
      if (this.attachment != null) {
         obj.putSFSObject("attachment", this.attachment);
      }

      if (this.expiry == 0) {
         Calendar end = (Calendar)Calendar.getInstance(TimeZone.getTimeZone("UTC")).clone();
         end.setTimeInMillis(this.endDate);
         int calendarField = 5;
         int calendarValue = 1;
         if (this.repeatMode == Mail.MailRepeatMode.Weekly) {
            calendarValue = 7;
         } else if (this.repeatMode == Mail.MailRepeatMode.Monthly) {
            calendarField = 2;
         } else if (this.repeatMode == Mail.MailRepeatMode.Yearly) {
            calendarField = 1;
         }

         end.add(calendarField, calendarValue * this.curRepeat);
         obj.putInt("expiry", (int)((end.getTimeInMillis() - MSMExtension.CurrentDBTime()) / 1000L));
      } else {
         obj.putInt("expiry", this.expiry);
      }

      obj.putBool("urgent", this.urgent);
      return obj;
   }

   public long getID() {
      return this.id;
   }

   public String getTitle() {
      return this.title;
   }

   public String getShortTitle() {
      return this.shortTitle;
   }

   public String getMessage() {
      return this.message;
   }

   public String getSender() {
      return this.sender;
   }

   public String getIcon() {
      return this.icon;
   }

   public ISFSObject getAttachment() {
      return this.attachment;
   }

   public long getStartDate() {
      return this.startDate;
   }

   public long getEndDate() {
      return this.endDate;
   }

   public Mail.MailRepeatMode getRepeatMode() {
      return this.repeatMode;
   }

   public int getRepeatCount() {
      return this.repeatCount;
   }

   public int getCurRepeat() {
      return this.curRepeat;
   }

   public void setCurRepeat(int repeat) {
      this.curRepeat = repeat;
   }

   public int getExpiry() {
      return this.expiry;
   }

   public boolean getUrgentFlag() {
      return this.urgent;
   }

   public boolean getEnabledFlag() {
      return this.enabled;
   }

   public ISFSObject getSegmentation() {
      return this.segmentation;
   }

   public long getBBBID() {
      return this.bbbId;
   }

   public static enum MailRepeatMode {
      Never,
      Daily,
      Weekly,
      Monthly,
      Yearly;
   }
}
