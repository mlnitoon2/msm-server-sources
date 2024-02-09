package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.GameSettings;
import com.smartfoxserver.v2.entities.data.ISFSObject;

public final class DailyCurrencyPack {
   private static final String PACK_ID = "pack_id";
   private static final String PACK_NAME = "name";
   private static final String PACK_DESCRIPTION = "description";
   private static final String PACK_CURRENCY = "currency";
   private static final String PACK_AMOUNT = "amount";
   private static final String PACK_REFRESH_KEY = "refresh_in_hours";
   private static final String PACK_DURATION = "duration_in_hours";
   private static final String PACK_COOLDOWN = "cooldown_in_hours";
   private static final String PACK_STORE_ITEM = "store_item_id";
   private final int id;
   private final String name;
   private final String description;
   private final String currency;
   private final int amount;
   private final int refresh;
   private final int duration;
   private final int cooldown;
   private final int storeItemId;

   public DailyCurrencyPack(ISFSObject dailyCurrencyPackData) {
      this.id = dailyCurrencyPackData.getInt("pack_id");
      this.name = dailyCurrencyPackData.getUtfString("name");
      this.description = dailyCurrencyPackData.getUtfString("description");
      this.currency = dailyCurrencyPackData.getUtfString("currency");
      this.amount = dailyCurrencyPackData.getInt("amount");
      this.refresh = dailyCurrencyPackData.getInt("refresh_in_hours");
      this.duration = dailyCurrencyPackData.getInt("duration_in_hours");
      this.cooldown = dailyCurrencyPackData.getInt("cooldown_in_hours");
      this.storeItemId = dailyCurrencyPackData.getInt("store_item_id");
   }

   public int getID() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public String getDescription() {
      return this.description;
   }

   public String getCurrency() {
      return this.currency;
   }

   public int getAmount() {
      return this.amount;
   }

   public int getRefresh() {
      return this.refresh;
   }

   public int getDuration() {
      return this.duration;
   }

   public int getCooldown() {
      return this.cooldown;
   }

   public int getStoreItemId() {
      return this.storeItemId;
   }

   public long getRefreshUnit() {
      return GameSettings.getLong("USER_DAILY_CURRENCY_REFRESH_PERIOD") * 1000L;
   }

   public String toString() {
      return String.format("DailyCurrencyPack %d  name '%s'  description '%s'  currency '%s'  amount %d  duration (in hours) %d cooldown (in hours) %d storeItemId %d", this.getID(), this.getName(), this.getDescription(), this.getCurrency(), this.getAmount(), this.getDuration(), this.getCooldown(), this.getStoreItemId());
   }
}
