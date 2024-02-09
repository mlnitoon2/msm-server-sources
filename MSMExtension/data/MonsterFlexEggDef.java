package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticData;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class MonsterFlexEggDef extends StaticData {
   private int id;
   private EggRequirements def;
   private int xp;
   private String name;
   private int diamondFillCost;
   private int costCoins;

   public MonsterFlexEggDef(ISFSObject d) {
      super(d);
      this.id = d.getInt("id");
      this.diamondFillCost = d.getInt("cost_diamonds");
      this.costCoins = d.getInt("cost_coins");
      this.xp = d.getInt("xp");
      this.name = d.getUtfString("mastertext_desc");
      String flexEggDefStr = this.data.getUtfString("def");
      ISFSObject sfsDef = null;
      if (flexEggDefStr != null && flexEggDefStr.length() > 0) {
         sfsDef = SFSObject.newFromJsonData(flexEggDefStr);
      }

      if (sfsDef != null) {
         this.data.putSFSObject("def", sfsDef);
      } else {
         this.data.removeElement("def");
      }

      this.def = new EggRequirements(sfsDef);
   }

   public int id() {
      return this.id;
   }

   public int diamondFillCost() {
      return this.diamondFillCost;
   }

   public int costCoins() {
      return this.costCoins;
   }

   public int xp() {
      return this.xp;
   }

   public String name() {
      return this.name;
   }

   public EggRequirements def() {
      return this.def;
   }

   public boolean evaluate(Monster m) {
      return this.def.evaluate(m);
   }

   public boolean supportedByClient(String clientPlatform, String clientSubplatform, VersionInfo maxSupportedServerVer) {
      return super.supportedByClient(clientPlatform, clientSubplatform, maxSupportedServerVer);
   }

   public ISFSObject getData() {
      return this.data;
   }
}
