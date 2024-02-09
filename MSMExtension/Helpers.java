package com.bigbluebubble.mysingingmonsters;

import com.bigbluebubble.BBBServer.util.Misc;
import com.smartfoxserver.v2.util.IWordFilter;
import com.smartfoxserver.v2.util.filters.FilteredMessage;
import com.smartfoxserver.v2.util.filters.WordsFilterMode;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class Helpers {
   public static final String CYRILLIC_ALPHABET = " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!¨\"#$&'()*+,-./:;<=>?@{}0123456789|£©¿®`~^ÀÁÂÄÇÈÉÊËÌÍÎÏÑÒÓÔÖÙÚÛÜßàáâäçèéêëìíîïñòóôöùúûü_ÆæÃãÕõАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя€₽¡";
   public static final String LATIN_ALPHABET = " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!¨\"#$&'()*+,-./:;<=>?@{}0123456789|£©¿®`~^ÀÁÂÄÇÈÉÊËÌÍÎÏÑÒÓÔÖÙÚÛÜßàáâäçèéêëìíîïñòóôöùúûü_ÆæÃãÕõ€¡";

   public static String implode(String[] ary, String delim) {
      StringBuilder out = new StringBuilder();

      for(int i = 0; i < ary.length; ++i) {
         if (i != 0) {
            out.append(delim);
         }

         out.append(ary[i]);
      }

      return out.toString();
   }

   public static String decompressJsonDataField(String data, String defaultEmptyString) throws Exception {
      if (data != null && !data.isEmpty()) {
         return data.charAt(0) != '[' && data.charAt(0) != '{' ? Misc.decompressTextData(data) : data;
      } else {
         return defaultEmptyString;
      }
   }

   public static String compressJsonDataField(String textData, String compressData, MSMExtension ext) {
      return compressJsonDataField(textData, compressData != null ? Integer.parseInt(compressData) : 0, 2, ext);
   }

   public static String compressJsonDataField(String textData, int compressData, int minDataLen, MSMExtension ext) {
      try {
         if (compressData != 0 && textData.length() > minDataLen) {
            String textDataCompressed = Misc.compressTextData(textData);
            String textDataUncompressed = Misc.decompressTextData(textDataCompressed);
            if (!textData.equals(textDataUncompressed)) {
               throw new Exception("ERROR: Decompressed user json data does not match original");
            }

            if (compressData == 2) {
               return textDataCompressed;
            }
         }
      } catch (Exception var6) {
         Logger.trace(var6);
         ext.stats.trackSFSError("COMPRESSION_ERROR", String.format("Decompressed user json data does not match original"));
      }

      return textData;
   }

   public static String sanitizeName(String name, String alphabet) {
      char replacementChar = true;
      char whitespaceChar = true;
      StringBuilder sanitizedName = new StringBuilder();
      if (name != null) {
         CharacterIterator it = new StringCharacterIterator(name);

         for(char c = it.first(); c != '\uffff'; c = it.next()) {
            if (Character.isHighSurrogate(c)) {
               sanitizedName.append('?');
               if (it.next() == '\uffff') {
                  break;
               }
            } else if (alphabet.indexOf(c) != -1) {
               sanitizedName.append(c);
            } else if (Character.isWhitespace(c)) {
               sanitizedName.append(' ');
            } else {
               sanitizedName.append('?');
            }
         }
      }

      return sanitizedName.toString();
   }

   public static String invalidName(long userId, String name, String type, List<String> exclusions) {
      if (name.contains("%")) {
         return "INVALID_CHAR_DISPLAY_NAME";
      } else if (name.contains("<c")) {
         return "INVALID_CHAR_DISPLAY_NAME";
      } else if (name.contains("</")) {
         return "INVALID_CHAR_DISPLAY_NAME";
      } else if (isBadWord(name, exclusions)) {
         if (userId != 0L) {
            logBadWord(userId, name, type);
         }

         return "BAD_WORD_DISPLAY_NAME";
      } else {
         return name.matches("^\\s*$") ? "INVALID_WHITESPACE_DISPLAY_NAME" : null;
      }
   }

   public static boolean isBadWord(String word, List<String> exclusions) {
      String strippedWord;
      for(Iterator var2 = exclusions.iterator(); var2.hasNext(); word = word.replaceAll("(?i)" + Pattern.quote(strippedWord), "")) {
         strippedWord = (String)var2.next();
      }

      if (word.isEmpty()) {
         return false;
      } else {
         String strippedWhiteSpace = "[ ¨\"'*+,\\-./:;|`~\\^_]";
         strippedWord = word.replaceAll("[ ¨\"'*+,\\-./:;|`~\\^_]", "");
         IWordFilter wordFilter = MSMExtension.getInstance().getParentZone().getWordFilter();
         wordFilter.setFilterMode(WordsFilterMode.BLACK_LIST);
         FilteredMessage filtered = wordFilter.apply(word);
         FilteredMessage strippedFiltered = wordFilter.apply(strippedWord);
         return !word.equalsIgnoreCase(filtered.getMessage()) || !strippedWord.equalsIgnoreCase(strippedFiltered.getMessage());
      }
   }

   public static void logBadWord(long userId, String word, String type) {
      try {
         String BAD_WORD_SQL = "INSERT INTO user_bad_words (`user_id`, `word`, `type`) VALUES (?, ?, ?)";
         MSMExtension.getInstance().getDB().insertGetId("INSERT INTO user_bad_words (`user_id`, `word`, `type`) VALUES (?, ?, ?)", new Object[]{userId, word, type});
      } catch (Exception var5) {
         Logger.trace(var5, String.format("Exception trying to log bad word '%s' as '%s' for user '%d'", word, type, userId));
      }

   }
}
