package com.bigbluebubble.mysingingmonsters.data.timed_events;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.BuffTimedEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.CollectAllTrialEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.CostumeAvailabilityEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.CostumeSalesEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.CrucibleFlagDayEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.CrucibleHeatDiscountEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.CurrencyAvailabilityEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.CurrencySalesEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.EggstravaganzaEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.EntityAvailabilityEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.EntitySalesEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.EvolveAvailabilityEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.IslandSalesEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.IslandThemeAvailabilityEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.MegafySaleEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.PermalightTorchSaleEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.PromoEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.ReturningUserBonusEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.ShortenedFuzingEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.StarAvailabilityEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.StarSalesEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.TapjoySaleTagEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.breeding_events.BreedProbabilityChangeEvent;
import com.bigbluebubble.mysingingmonsters.exceptions.InvalidTimedEventException;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TimedEventManager {
   private static TimedEventManager instance_ = null;
   private TimedEventListBuilder eventListBuilder = new TimedEventListBuilder();
   private Map<TimedEventType, TimedEventList> eventMap = new ConcurrentHashMap();

   public static TimedEventManager instance() {
      return instance_;
   }

   public static void init(IDbWrapper db) throws Exception {
      instance_ = new TimedEventManager(db);
   }

   private TimedEventManager(IDbWrapper db) throws Exception {
      this.eventMap.put(TimedEventType.EntityStoreAvailability, new TimedEventList());
      this.eventMap.put(TimedEventType.EntitySale, new TimedEventList());
      this.eventMap.put(TimedEventType.EvolveAvailability, new TimedEventList());
      this.eventMap.put(TimedEventType.StarStoreAvailability, new TimedEventList());
      this.eventMap.put(TimedEventType.StarSale, new TimedEventList());
      this.eventMap.put(TimedEventType.CostumeAvailability, new TimedEventList());
      this.eventMap.put(TimedEventType.CostumeSale, new TimedEventList());
      this.eventMap.put(TimedEventType.CurrencySale, new TimedEventList());
      this.eventMap.put(TimedEventType.CurrencyAvailability, new TimedEventList());
      this.eventMap.put(TimedEventType.BreedingPromo, new TimedEventList());
      this.eventMap.put(TimedEventType.PermalightTorchSale, new TimedEventList());
      this.eventMap.put(TimedEventType.ShortenedFuzing, new TimedEventList());
      this.eventMap.put(TimedEventType.MegafySale, new TimedEventList());
      this.eventMap.put(TimedEventType.IslandSale, new TimedEventList());
      this.eventMap.put(TimedEventType.TapjoySaleTag, new TimedEventList());
      this.eventMap.put(TimedEventType.CollectAllTrial, new TimedEventList());
      this.eventMap.put(TimedEventType.Eggstravaganza, new TimedEventList());
      this.eventMap.put(TimedEventType.ReturningUserBonus, new TimedEventList());
      this.eventMap.put(TimedEventType.Promo, new TimedEventList());
      this.eventMap.put(TimedEventType.CrucibleHeatDiscount, new TimedEventList());
      this.eventMap.put(TimedEventType.CrucibleFlagDay, new TimedEventList());
      this.eventMap.put(TimedEventType.NurseryTimeReduction, new TimedEventList());
      this.eventMap.put(TimedEventType.BreedingTimeReduction, new TimedEventList());
      this.eventMap.put(TimedEventType.BreedingChanceIncrease, new TimedEventList());
      this.eventMap.put(TimedEventType.CostumeChanceIncrease, new TimedEventList());
      this.eventMap.put(TimedEventType.CurrencyCollectionBonus, new TimedEventList());
      this.eventMap.put(TimedEventType.IslandThemeAvailability, new TimedEventList());
      this.eventMap.put(TimedEventType.BakingTimeReduction, new TimedEventList());
      if (db != null) {
         String sql = "SELECT * FROM timed_events WHERE NOW() < end_date;";
         ISFSArray results = db.query("SELECT * FROM timed_events WHERE NOW() < end_date;");
         Iterator i = results.iterator();

         while(i.hasNext()) {
            this.addNewEventFromDb((ISFSObject)((SFSDataWrapper)i.next()).getObject());
         }
      }

   }

   public TimedEvent createTimedEvent(TimedEventType timedEventType, ISFSObject timedEventData) throws Exception {
      switch(timedEventType) {
      case CostumeAvailability:
         return new CostumeAvailabilityEvent(timedEventData);
      case CostumeSale:
         return new CostumeSalesEvent(timedEventData);
      case CurrencyAvailability:
         return new CurrencyAvailabilityEvent(timedEventData);
      case CurrencySale:
         return new CurrencySalesEvent(timedEventData);
      case EntitySale:
         return new EntitySalesEvent(timedEventData);
      case EntityStoreAvailability:
         return new EntityAvailabilityEvent(timedEventData);
      case EvolveAvailability:
         return new EvolveAvailabilityEvent(timedEventData);
      case IslandSale:
         return new IslandSalesEvent(timedEventData);
      case MegafySale:
         return new MegafySaleEvent(timedEventData);
      case PermalightTorchSale:
         return new PermalightTorchSaleEvent(timedEventData);
      case ShortenedFuzing:
         return new ShortenedFuzingEvent(timedEventData);
      case StarSale:
         return new StarSalesEvent(timedEventData);
      case StarStoreAvailability:
         return new StarAvailabilityEvent(timedEventData);
      case TapjoySaleTag:
         return new TapjoySaleTagEvent(timedEventData);
      case CollectAllTrial:
         return new CollectAllTrialEvent(timedEventData);
      case Eggstravaganza:
         return new EggstravaganzaEvent(timedEventData);
      case ReturningUserBonus:
         return new ReturningUserBonusEvent(timedEventData);
      case Promo:
         return new PromoEvent(timedEventData);
      case BreedingPromo:
         return new BreedProbabilityChangeEvent(timedEventData);
      case CrucibleHeatDiscount:
         return new CrucibleHeatDiscountEvent(timedEventData);
      case CrucibleFlagDay:
         return new CrucibleFlagDayEvent(timedEventData);
      case NurseryTimeReduction:
      case BreedingTimeReduction:
      case BreedingChanceIncrease:
      case CostumeChanceIncrease:
      case CurrencyCollectionBonus:
      case BakingTimeReduction:
         return new BuffTimedEvent(timedEventData);
      case IslandThemeAvailability:
         return new IslandThemeAvailabilityEvent(timedEventData);
      default:
         throw new Exception("Invalid Timed Event");
      }
   }

   public SFSArray activeEventsInNearFuture(VersionInfo clientVersion) {
      return this.eventListBuilder.activeEventsInNearFuture(clientVersion);
   }

   public void addNewEventFromDb(ISFSObject s) {
      try {
         this.eventListBuilder.addNewEventFromDb(s);
      } catch (InvalidTimedEventException var3) {
         Logger.trace((Exception)var3, String.format("Discarding Invalid Timed Event '%s'", s.toJson()));
      }

   }

   public void updateActiveTimedEvents() {
      try {
         this.eventListBuilder.updateActiveTimedEvents();
      } catch (Exception var2) {
         Logger.trace(var2, "Failure in UpdateActiveTimedEvents");
      }

   }

   public boolean hasTimedEventNow(TimedEventType type, Integer key, int islandType) {
      TimedEventList list = this.getListFromType(type);
      return list != null ? list.timedEventNow(key, islandType) : false;
   }

   public List<TimedEvent> currentActiveOnKey(TimedEventType type, Integer key, int islandType) {
      TimedEventList list = this.getListFromType(type);
      return list != null ? list.currentActiveOnKey(key, islandType) : Collections.emptyList();
   }

   protected TimedEvent addEvent(TimedEventType type, ISFSObject timedEventSfs) throws Exception {
      TimedEventList list = this.getListFromType(type);
      return list != null ? list.addEvent(this.createTimedEvent(type, timedEventSfs)) : null;
   }

   protected void remove(TimedEvent e) throws Exception {
      TimedEventList list = this.getListFromType(e.getEventType());
      if (list != null) {
         list.remove(e);
      }

   }

   protected TimedEventList getListFromType(TimedEventType type) {
      return (TimedEventList)this.eventMap.get(type);
   }
}
