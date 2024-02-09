package com.bigbluebubble.mysingingmonsters;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.data.Promo;
import com.bigbluebubble.mysingingmonsters.data.PromoLookup;
import com.bigbluebubble.mysingingmonsters.data.groups.ComboPackGroup;
import com.bigbluebubble.mysingingmonsters.data.groups.IslandPromoGroup;
import com.bigbluebubble.mysingingmonsters.data.groups.UserGroup;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.TimedEventType;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.PromoEvent;
import com.bigbluebubble.mysingingmonsters.data.timed_events.events.ReturningUserBonusEvent;
import com.bigbluebubble.mysingingmonsters.player.Player;
import com.bigbluebubble.mysingingmonsters.player.PlayerTimedEvents;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PromotionManager {
   private static boolean hasPromoOfType(Collection<Integer> promoIds, int promoType) {
      Iterator var2 = promoIds.iterator();

      Promo promo;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         int i = (Integer)var2.next();
         promo = PromoLookup.get(i);
      } while(promo == null || promo.getType() != promoType);

      return true;
   }

   private static void addPlayerPromo(Player player, int promoId, int durationHours, String metricsLabel) throws Exception {
      MSMExtension ext = MSMExtension.getInstance();
      Promo promo = PromoLookup.get(promoId);
      if (promo == null) {
         throw new Exception("Unable to find promo " + promoId);
      } else {
         PromoEvent promoEvent = new PromoEvent(promo, MSMExtension.CurrentDBTime(), MSMExtension.CurrentDBTime() + (long)durationHours * 60L * 60L * 1000L);
         player.getTimedEvents().addEvent(promoEvent);
         player.getTimedEvents().save(ext.getDB());
         ext.stats.trackPromoStart(player, metricsLabel, promoId, durationHours, player.getLevel());
      }
   }

   public static void setupPlayerLoginPromotions(Player p, VersionInfo clientVer) {
      MSMExtension ext = MSMExtension.getInstance();

      try {
         if (p.timedEventsUnlocked()) {
            PlayerTimedEvents playerTimedEvents = new PlayerTimedEvents(p.getPlayerId());
            playerTimedEvents.loadAll(ext.getDB());
            List<Integer> activeExclusivePromos = new ArrayList();
            Map<Integer, TimedEvent> userPromos = new HashMap();
            Iterator var6 = playerTimedEvents.getEvents().iterator();

            label235:
            while(true) {
               TimedEvent te;
               int promoID;
               do {
                  do {
                     if (!var6.hasNext()) {
                        int bundleLevel = GameSettings.getInt("FIRST_TIME_COMBO_LEVEL");
                        if (p.getLevel() < bundleLevel) {
                           break label235;
                        }

                        boolean giveWarmupBundlePromo = false;
                        boolean giveAnniversaryWarmupPromo = false;
                        boolean giveAnniversaryPromo = false;
                        boolean giveThemeBundlePromo = false;
                        boolean giveIAP999Promo = false;
                        boolean giveIAP2499Promo = false;
                        boolean giveIAP4999Promo = false;
                        int giveTopUpPromo = 0;
                        long anniversaryPromoStart = GameSettings.get("ANNIVERSARY_PROMO_START", 1694012400000L);
                        long anniversaryPromoEnd = GameSettings.get("ANNIVERSARY_PROMO_END", 1697036400000L);
                        if (activeExclusivePromos.isEmpty()) {
                           if ((!p.hasMadePurchase() || p.memberOfGroup(44)) && !hasPromoOfType(userPromos.keySet(), 3)) {
                              giveWarmupBundlePromo = true;
                           }

                           if (MSMExtension.CurrentDBTime() >= anniversaryPromoStart && MSMExtension.CurrentDBTime() < anniversaryPromoEnd && clientVer.greaterThanEqual(new VersionInfo(4, 0, 0))) {
                              TimedEvent promoEvent;
                              if (!p.hasMadePurchase()) {
                                 promoEvent = (TimedEvent)userPromos.get(17);
                                 if (promoEvent == null || MSMExtension.CurrentDBTime() > promoEvent.getEndDate()) {
                                    giveAnniversaryWarmupPromo = true;
                                 }
                              }

                              if (p.hasMadePurchase()) {
                                 promoEvent = (TimedEvent)userPromos.get(18);
                                 if (promoEvent == null || MSMExtension.CurrentDBTime() > promoEvent.getEndDate()) {
                                    giveAnniversaryPromo = true;
                                 }
                              }
                           }

                           if (p.memberOfGroup(45)) {
                              if (p.getIslandByIslandIndex(5) != null) {
                                 if (!userPromos.containsKey(15)) {
                                    giveTopUpPromo = 15;
                                 }
                              } else if (p.getIslandByIslandIndex(4) != null) {
                                 if (!userPromos.containsKey(14)) {
                                    giveTopUpPromo = 14;
                                 }
                              } else if (p.getIslandByIslandIndex(3) != null) {
                                 if (!userPromos.containsKey(13)) {
                                    giveTopUpPromo = 13;
                                 }
                              } else if (p.getIslandByIslandIndex(2) != null && !userPromos.containsKey(6)) {
                                 giveTopUpPromo = 6;
                              }

                              if (giveTopUpPromo != 0) {
                                 ArrayList<IslandPromoGroup> groups = UserGroup.getGroup(UserGroup.GroupType.IslandPromo);
                                 Iterator var20 = groups.iterator();

                                 while(var20.hasNext()) {
                                    IslandPromoGroup group = (IslandPromoGroup)var20.next();
                                    if (p.memberOfGroup(group.id()) && group.promotion() <= 0) {
                                       giveTopUpPromo = 0;
                                    }
                                 }
                              }
                           }
                        }

                        if (!hasPromoOfType(activeExclusivePromos, 3)) {
                           long bonusRepeat = GameSettings.getLong("REPEAT_BONUS_PROMO_HOURS") * 60L * 60L * 1000L;
                           TimedEvent promoEvent = (TimedEvent)userPromos.get(8);
                           if (promoEvent == null) {
                              giveIAP999Promo = true;
                           } else if (promoEvent.getCount() > 0 && MSMExtension.CurrentDBTime() >= promoEvent.getEndDate() + bonusRepeat) {
                              giveIAP999Promo = true;
                           }

                           promoEvent = (TimedEvent)userPromos.get(9);
                           if (promoEvent == null) {
                              giveIAP2499Promo = true;
                           } else if (promoEvent.getCount() > 0 && MSMExtension.CurrentDBTime() >= promoEvent.getEndDate() + bonusRepeat) {
                              giveIAP2499Promo = true;
                           }

                           promoEvent = (TimedEvent)userPromos.get(10);
                           if (promoEvent == null) {
                              giveIAP4999Promo = true;
                           } else if (promoEvent.getCount() > 0 && MSMExtension.CurrentDBTime() >= promoEvent.getEndDate() + bonusRepeat) {
                              giveIAP4999Promo = true;
                           }
                        }

                        int promoDuration;
                        if (giveWarmupBundlePromo) {
                           promoDuration = PromoLookup.getDefaultId();
                           int promoDuration = GameSettings.getInt("COMBO_DURATION_HOURS");
                           int countryPromoId = PromoLookup.getIdForCountry(p.getCountryCode());
                           if (countryPromoId != 0) {
                              promoDuration = countryPromoId;
                           } else {
                              ArrayList<ComboPackGroup> combos = UserGroup.getGroup(UserGroup.GroupType.ComboPack);
                              Iterator var23 = combos.iterator();

                              while(var23.hasNext()) {
                                 ComboPackGroup combo = (ComboPackGroup)var23.next();
                                 if (p.memberOfGroup(combo.id()) && combo.promotion() > 0) {
                                    promoDuration = combo.promotion();
                                    if (combo.durationHours() > 0) {
                                       promoDuration = combo.durationHours();
                                    }
                                 }
                              }
                           }

                           if (!userPromos.containsKey(promoDuration)) {
                              addPlayerPromo(p, promoDuration, promoDuration, "combo_promo");
                           }
                        } else if (giveAnniversaryWarmupPromo) {
                           promoDuration = Math.max(1, (int)((anniversaryPromoEnd - MSMExtension.CurrentDBTime()) / 3600000L));
                           addPlayerPromo(p, 17, promoDuration, "anniversary_warmup_promo");
                        } else if (giveAnniversaryPromo) {
                           promoDuration = Math.max(1, (int)((anniversaryPromoEnd - MSMExtension.CurrentDBTime()) / 3600000L));
                           addPlayerPromo(p, 18, promoDuration, "anniversary_warmup_promo");
                        } else if (giveTopUpPromo > 0) {
                           String metricsPromoName = "unknown_island_promo";
                           switch(giveTopUpPromo) {
                           case 6:
                              metricsPromoName = "cold_island_promo";
                           case 7:
                           case 8:
                           case 9:
                           case 10:
                           case 11:
                           case 12:
                           default:
                              break;
                           case 13:
                              metricsPromoName = "air_island_promo";
                              break;
                           case 14:
                              metricsPromoName = "water_island_promo";
                              break;
                           case 15:
                              metricsPromoName = "earth_island_promo";
                           }

                           addPlayerPromo(p, giveTopUpPromo, GameSettings.getInt("PROMOTION_DURATION_HOURS"), metricsPromoName);
                        } else if (giveThemeBundlePromo) {
                           addPlayerPromo(p, 7, GameSettings.getInt("PROMOTION_DURATION_HOURS"), "cold_earth_theme_combo_promo");
                        }

                        if (!giveWarmupBundlePromo && !giveAnniversaryWarmupPromo && !giveAnniversaryPromo) {
                           promoDuration = 87600;
                           if (giveIAP999Promo) {
                              if (!userPromos.containsKey(8)) {
                                 addPlayerPromo(p, 8, promoDuration, "iap_999_promo");
                              } else {
                                 addPlayerPromo(p, 8, promoDuration, "repeat_iap_999_promo");
                              }
                           }

                           if (giveIAP2499Promo) {
                              if (!userPromos.containsKey(9)) {
                                 addPlayerPromo(p, 9, promoDuration, "iap_2499_promo");
                              } else {
                                 addPlayerPromo(p, 9, promoDuration, "repeat_iap_2499_promo");
                              }
                           }

                           if (giveIAP4999Promo) {
                              if (!userPromos.containsKey(10)) {
                                 addPlayerPromo(p, 10, promoDuration, "iap_4999_promo");
                              } else {
                                 addPlayerPromo(p, 10, promoDuration, "repeat_iap_4999_promo");
                              }
                           }
                        }
                        break label235;
                     }

                     te = (TimedEvent)var6.next();
                  } while(te.getEventType() != TimedEventType.Promo);

                  PromoEvent promoEvent = (PromoEvent)te;
                  if (promoEvent.currentlyActive() && promoEvent.getPromo().isExclusive() && (promoEvent.getMax() == 0 || promoEvent.getCount() < promoEvent.getMax())) {
                     activeExclusivePromos.add(promoEvent.getPromo().getID());
                  }

                  promoID = promoEvent.getPromo().getID();
               } while(userPromos.containsKey(promoID) && te.getEndDate() <= ((TimedEvent)userPromos.get(promoID)).getEndDate());

               userPromos.put(promoID, te);
            }
         }

         p.getTimedEvents().load(ext.getDB());
      } catch (Exception var25) {
         Logger.trace(var25, "Error setting up player promotions");
      }

   }

   public static void setupPlayerTimedEvents(Player p, VersionInfo clientVer) {
      if (p.getTimedEvents().currentActiveOnKey(TimedEventType.ReturningUserBonus, 0, 0).size() == 0) {
         long lastLoginTimeMs = p.LastLoginTime();
         int daysGone = GameSettings.getInt("RETURNING_USER_DAYS_GONE");
         if (lastLoginTimeMs > 0L && daysGone != -1) {
            long msGone = (long)daysGone * 24L * 60L * 60L * 1000L;
            if (MSMExtension.CurrentDBTime() - lastLoginTimeMs >= msGone) {
               ISFSObject eventData = new SFSObject();
               eventData.putLong("id", 0L);
               eventData.putInt("event_id", TimedEventType.ReturningUserBonus.getId());
               eventData.putLong("start_date", MSMExtension.CurrentDBTime());
               eventData.putLong("end_date", MSMExtension.CurrentDBTime() + GameSettings.getLong("RETURNING_USER_MODIFIERS_DURATION_D") * 24L * 60L * 60L * 1000L);
               eventData.putUtfString("data", "[" + GameSettings.get("RETURNING_USER_MODIFIERS") + "]");
               eventData.putLong("user_id", p.getPlayerId());

               try {
                  p.getTimedEvents().addEvent(new ReturningUserBonusEvent(eventData));
                  p.setWelcomeBack(true);
                  p.getTimedEvents().save(MSMExtension.getInstance().getDB());
               } catch (Exception var9) {
                  Logger.trace(var9, "Error setting up returning user modifier");
               }
            }
         }
      }

   }

   public static void handleQuestEvent(User sender, ISFSObject event) {
   }
}
