package com.bigbluebubble.mysingingmonsters.data.groups;

import com.bigbluebubble.BBBServer.util.LogLevel;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Iterator;

public class CurrencyGroup extends BaseGroup {
   protected final ArrayList<String> currencyNames_;

   public CurrencyGroup(String data) throws IllegalArgumentException {
      super(data);

      try {
         this.currencyNames_ = new ArrayList(SFSObject.newFromJsonData(data).getUtfStringArray("item_names"));
      } catch (NullPointerException var3) {
         throw new IllegalArgumentException(String.format("Invalid Currency Data '%s'", data));
      }
   }

   public String toString() {
      return "CurrencyGroup: Excluded Currencies: " + this.excludedCurrencies() + " " + super.toString();
   }

   protected String excludedCurrencies() {
      String currencies = "(";

      String s;
      for(Iterator var2 = this.currencyNames_.iterator(); var2.hasNext(); currencies = currencies + s) {
         s = (String)var2.next();
         currencies = currencies + (currencies.length() <= 1 ? "" : ", ");
      }

      currencies = currencies + ")";
      return currencies;
   }

   public boolean excludesCurrency(String currency) {
      Iterator var2 = this.currencyNames_.iterator();

      String s;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         s = (String)var2.next();
      } while(!s.equals(currency));

      return true;
   }

   public static void initStaticData(ArrayList<CurrencyGroup> groups) {
      Iterator var1 = groups.iterator();

      while(var1.hasNext()) {
         CurrencyGroup group = (CurrencyGroup)var1.next();
         Logger.trace(LogLevel.DEBUG, "### " + group.toString());
      }

   }

   public static int getGroupToAssign(int level, VersionInfo version, String platform) {
      ArrayList<CurrencyGroup> groups = UserGroup.getGroup(UserGroup.GroupType.CurrencyExclude);
      return BaseGroup.determineGroup(level, version, platform, groups);
   }
}
