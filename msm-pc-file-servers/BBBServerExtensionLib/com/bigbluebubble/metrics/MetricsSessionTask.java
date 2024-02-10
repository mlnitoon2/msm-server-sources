package com.bigbluebubble.metrics;

import com.bigbluebubble.BBBServer.BBBServerExtension;
import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.DoubleBuffer;
import com.bigbluebubble.BBBServer.util.Misc;
import com.bigbluebubble.BBBServer.util.SimpleLogger;
import com.smartfoxserver.v2.SmartFoxServer;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MetricsSessionTask implements Runnable {
   private final String _url;
   private final DoubleBuffer<String> _sessions;

   public static ScheduledFuture<?> schedule(BBBServerExtension ext, DoubleBuffer<String> sessions) {
      int sessionStatsInterval = 0;

      try {
         sessionStatsInterval = GameSettings.getInt("SESSION_STATS_INTERVAL");
      } catch (NumberFormatException var4) {
         SimpleLogger.trace("Unable to determine SESSION_STATS_INTERVAL");
      }

      String sessionStatsURL = GameSettings.get("SESSION_STATS_URL");
      if (sessionStatsURL == null) {
         SimpleLogger.trace("Unable to determine SESSION_STATS_URL");
      }

      if (sessionStatsInterval > 0 && sessionStatsURL != null) {
         SimpleLogger.trace(String.format("Logging session stats to '%s' with intervanl %d", sessionStatsURL, sessionStatsInterval));
         return SmartFoxServer.getInstance().getTaskScheduler().scheduleAtFixedRate(new MetricsSessionTask(ext, sessionStatsURL, sessions), 10, sessionStatsInterval, TimeUnit.SECONDS);
      } else {
         return null;
      }
   }

   public MetricsSessionTask(BBBServerExtension ext, String url, DoubleBuffer<String> sessions) {
      this._url = url;
      this._sessions = sessions;
   }

   public void run() {
      String[] sessions = (String[])this._sessions.swapBuffers();
      if (sessions.length != 0 && sessions[0] != null) {
         HttpURLConnection conn = null;

         try {
            URL url = new URL(this._url);
            conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes("sessions=[");

            for(int i = 0; i < sessions.length && sessions[i] != null; ++i) {
               if (i != 0) {
                  out.writeBytes(",");
               }

               out.writeBytes(Misc.URLEncode(sessions[i]));
            }

            out.writeBytes("]");
            out.flush();
            out.close();
            if (conn.getResponseCode() == 200) {
            }
         } catch (MalformedURLException var10) {
            SimpleLogger.trace((Exception)var10);
         } catch (IOException var11) {
            SimpleLogger.trace((Exception)var11);
         } finally {
            if (conn != null) {
               conn.disconnect();
            }

         }

      }
   }
}
