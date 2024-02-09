package com.bigbluebubble.mysingingmonsters.logging;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MSMCommands {
   public static final Map<String, Integer> cmds;
   public static final Map<Integer, String> cmds2;
   public static final int gs_give_me_shit = 1;
   public static final int db_monster = 2;
   public static final int db_gene = 3;
   public static final int db_island_torches = 4;
   public static final int db_versions = 5;
   public static final int db_structure = 6;
   public static final int db_island = 7;
   public static final int db_level = 8;
   public static final int db_store = 9;
   public static final int db_scratch_offs = 10;
   public static final int gs_quest = 11;
   public static final int keep_alive = 12;
   public static final int gs_set_displayname = 13;
   public static final int gs_quest_event = 14;
   public static final int gs_quest_read = 15;
   public static final int gs_quest_collect = 16;
   public static final int gs_update_achievement_status = 17;
   public static final int gs_buy_island = 18;
   public static final int gs_change_island = 19;
   public static final int gs_place_on_gold_island = 20;
   public static final int gs_save_island_warp_speed = 21;
   public static final int gs_buy_egg = 22;
   public static final int gs_hatch_egg = 23;
   public static final int gs_sell_egg = 24;
   public static final int gs_breed_monsters = 25;
   public static final int gs_finish_breeding = 26;
   public static final int gs_feed_monster = 27;
   public static final int gs_move_monster = 28;
   public static final int gs_mute_monster = 29;
   public static final int gs_flip_monster = 30;
   public static final int gs_sell_monster = 31;
   public static final int gs_collect_monster = 32;
   public static final int gs_name_monster = 33;
   public static final int gs_admin_user_neighbors = 34;
   public static final int gs_neighbors = 35;
   public static final int gs_admin_multi_neighbors = 36;
   public static final int gs_multi_neighbors = 37;
   public static final int gs_store_monster = 38;
   public static final int gs_unstore_monster = 39;
   public static final int gs_light_torch = 40;
   public static final int gs_additional_friend_torch_data = 41;
   public static final int gs_get_torchgifts = 42;
   public static final int gs_collect_torchgift = 43;
   public static final int gs_box_activate_monster = 44;
   public static final int gs_box_purchase_fill_cost = 45;
   public static final int gs_box_purchase_fill = 46;
   public static final int gs_box_add_egg = 47;
   public static final int gs_box_add_monster = 48;
   public static final int gs_send_ethereal_monster = 49;
   public static final int gs_send_monster_home = 50;
   public static final int gs_buy_structure = 51;
   public static final int gs_move_structure = 52;
   public static final int gs_sell_structure = 53;
   public static final int gs_start_upgrade_structure = 54;
   public static final int gs_finish_upgrade_structure = 55;
   public static final int gs_finish_structure = 56;
   public static final int gs_flip_structure = 57;
   public static final int gs_mute_structure = 58;
   public static final int gs_start_obstacle = 59;
   public static final int gs_clear_obstacle = 60;
   public static final int gs_clear_obstacle_speed_up = 61;
   public static final int gs_start_baking = 62;
   public static final int gs_finish_baking = 63;
   public static final int gs_collect_from_mine = 64;
   public static final int gs_speed_up_structure = 65;
   public static final int gs_store_decoration = 66;
   public static final int gs_unstore_decoration = 67;
   public static final int gs_speed_up_breeding = 68;
   public static final int gs_speed_up_baking = 69;
   public static final int gs_speed_up_hatching = 70;
   public static final int gs_get_friends = 71;
   public static final int gs_get_messages = 72;
   public static final int gs_delete_message = 73;
   public static final int gs_get_friend_visit_data = 74;
   public static final int gs_visit_specific_friend_island = 75;
   public static final int gs_set_fav_friend = 76;
   public static final int gs_get_random_visit_data = 77;
   public static final int gs_get_ranked_island_data = 78;
   public static final int gs_get_island_rank = 79;
   public static final int gs_collect_invite_reward = 80;
   public static final int gs_rate_island = 81;
   public static final int gs_process_unclaimed_purchases = 82;
   public static final int gs_collect_rewards = 83;
   public static final int gs_collect_facebook_reward = 84;
   public static final int gs_currency_conversion = 85;
   public static final int gs_currency_coins2eth_conversion = 86;
   public static final int gs_currency_diamonds2eth_conversion = 87;
   public static final int gs_currency_eth2diamonds_conversion = 88;
   public static final int gs_referral_request = 89;
   public static final int gs_offer_viewed = 90;
   public static final int gs_player_has_scratch_off = 91;
   public static final int gs_play_scratch_off = 92;
   public static final int gs_purchase_scratch_off = 93;
   public static final int gs_collect_scratch_off = 94;
   public static final int gs_get_memory_game_numbers = 95;
   public static final int gs_memory_minigame_current_cost = 96;
   public static final int gs_purchase_memory_mini_game = 97;
   public static final int gs_collect_memory_mini_game = 98;
   public static final int gs_view_inhouse_ad = 99;
   public static final int gs_thirdparty_ad_viewed = 100;
   public static final int gs_thirdparty_video_ad_viewed = 101;
   public static final int gs_promos = 102;
   public static final int gs_facebook_help_nursery = 103;
   public static final int gs_send_facebook_help = 104;
   public static final int gs_handle_facebook_help_instances = 105;
   public static final int gs_request_facebook_help_permissions = 106;
   public static final int gs_purchase_buyback = 107;
   public static final int gs_admin_purchase_buyback = 108;
   public static final int gs_get_starting_ccu = 109;
   public static final int gs_get_finishing_ccu = 110;
   public static final int gs_get_ccu = 111;
   public static final int gs_admin_refresh_all = 112;
   public static final int gs_admin_check_incomplete_tutorial = 113;
   public static final int gs_admin_move_users_monster = 114;
   public static final int gs_admin_move_users_structure = 115;
   public static final int gs_admin_destroy_users_monster = 116;
   public static final int gs_admin_destroy_users_structure = 117;
   public static final int gs_admin_get_user_visit_data = 118;
   public static final int gs_admin_sell_egg = 119;
   public static final int gs_admin_buy_egg = 120;
   public static final int gs_admin_hatch_egg = 121;
   public static final int gs_admin_buy_structure = 122;
   public static final int gs_admin_buy_island = 123;
   public static final int gs_admin_change_island = 124;
   public static final int gs_admin_destroy_island = 125;
   public static final int gs_admin_feed_monster = 126;
   public static final int gs_admin_start_upgrade_structure = 127;
   public static final int gs_admin_clear_obstacle_speed_up = 128;
   public static final int gs_admin_speed_up_structure = 129;
   public static final int gs_admin_speed_up_breeding = 130;
   public static final int gs_admin_start_baking = 131;
   public static final int gs_admin_speed_up_baking = 132;
   public static final int gs_admin_remove_breeding = 133;
   public static final int gs_admin_finish_breeding = 134;
   public static final int gs_admin_speed_up_hatching = 135;
   public static final int gs_admin_give_me_shit = 136;
   public static final int gs_admin_flip_monster = 137;
   public static final int gs_admin_flip_structure = 138;
   public static final int gs_admin_send_ethereal_monster = 139;
   public static final int gs_admin_send_monster_home = 140;
   public static final int gs_admin_name_monster = 141;
   public static final int gs_admin_quest = 142;
   public static final int gs_admin_complete_quest = 143;
   public static final int gs_player = 144;
   public static final int gs_admin_check_quests = 145;
   public static final int gs_admin_kick_user = 146;
   public static final int gs_admin_store_monster = 147;
   public static final int gs_admin_unstore_monster = 148;
   public static final int gs_admin_store_decoration = 149;
   public static final int gs_admin_unstore_decoration = 150;
   public static final int gs_admin_unlight_torch = 151;
   public static final int gs_admin_box_monster_toggle = 152;
   public static final int gs_admin_box_purchase_fill = 153;
   public static final int gs_admin_box_activate_monster = 154;
   public static final int gs_test_breed_monsters = 155;
   public static final int gs_store_buddy = 156;
   public static final int gs_unstore_buddy = 157;
   public static final int gs_start_fuzing = 158;
   public static final int gs_finish_fuzing = 159;
   public static final int gs_speed_up_fuzing = 160;
   public static final int gs_admin_start_fuzing = 161;
   public static final int gs_admin_speed_up_fuzing = 162;
   public static final int gs_store_replacements = 163;
   public static final int gs_rare_monster_data = 164;
   public static final int gs_timed_events = 165;
   public static final int gs_offer_completed = 166;
   public static final int gs_mega_monster_message = 167;
   public static final int gs_collect_daily_reward = 168;
   public static final int gs_admin_store_buddy = 169;
   public static final int gs_admin_unstore_buddy = 170;
   public static final int gs_admin_finish_fuzing = 171;
   public static final int gs_place_on_tribal = 172;
   public static final int gs_tribal_feed_monster = 173;
   public static final int gs_cancel_tribe_request = 174;
   public static final int gs_send_tribe_request = 175;
   public static final int gs_get_tribal_island_data = 176;
   public static final int gs_join_tribe = 177;
   public static final int gs_leave_tribe_request = 178;
   public static final int gs_kick_tribe_request = 179;
   public static final int gs_send_tribe_invite = 180;
   public static final int gs_cancel_tribe_invite = 181;
   public static final int gs_get_code = 182;
   public static final int gs_admin_get_codes = 183;
   public static final int gs_transfer_code = 184;
   public static final int gs_sticker = 185;
   public static final int gs_delete_composer_template = 186;
   public static final int gs_save_composer_template = 187;
   public static final int gs_save_composer_track = 188;
   public static final int gs_paywall_updated = 189;
   public static final int gs_delete_mail = 190;
   public static final int gs_purchase_flip_mini_game = 191;
   public static final int gs_flip_minigame_cost = 192;
   public static final int gs_collect_flip_mini_game = 193;
   public static final int gs_daily_login_buyback = 194;
   public static final int gs_app_link = 195;
   public static final int gs_activate_island_theme = 196;
   public static final int gs_unlock_breeding_structure = 197;
   public static final int gs_test_spin_probabilities = 198;
   public static final int gs_epic_monster_data = 200;
   public static final int gs_collect_daily_currency_pack = 201;
   public static final int gs_refresh_daily_currency_pack = 202;
   public static final int gs_test_scratch = 203;
   public static final int gs_report_user = 204;
   public static final int gs_start_amber_evolve = 205;
   public static final int gs_finish_amber_evolve = 206;
   public static final int gs_speedup_amber_evolve = 207;
   public static final int gs_collect_cruc_heat = 208;
   public static final int gs_delete_account = 209;
   public static final int gs_viewed_cruc_monst = 210;
   public static final int gs_viewed_cruc_unlock = 211;
   public static final int gs_test_cruc_evolv = 212;
   public static final int gs_set_moniker = 213;
   public static final int gs_flip_levels = 214;
   public static final int gs_flip_boards = 215;
   public static final int gs_collect_flip_level = 216;
   public static final int gs_start_rebake = 217;
   public static final int gs_set_last_timed_theme = 218;
   public static final int gs_quests_read = 219;
   public static final int gs_purchase_evolve_unlock = 220;
   public static final int gs_update_island_tutorials = 221;
   public static final int gs_attempt_early_box_activate = 222;
   public static final int db_flexeggdefs = 223;
   public static final int gs_purchase_evo_powerup_unlock = 224;
   public static final int gs_admin_box_monster_toggle_new = 225;
   public static final int gs_test_collect_monster = 226;
   public static final int gs_start_attuning = 227;
   public static final int gs_finish_attuning = 228;
   public static final int gs_speedup_attuning = 229;
   public static final int db_attuner_gene = 230;
   public static final int gs_start_synthesizing = 231;
   public static final int gs_speedup_synthesizing = 232;
   public static final int gs_collect_synthesizing_failure = 233;
   public static final int gs_test_synthesis = 234;

   static {
      Map<String, Integer> m = new HashMap();
      m.put("gs_give_me_shit", 1);
      m.put("db_monster", 2);
      m.put("db_gene", 3);
      m.put("db_island_torches", 4);
      m.put("db_versions", 5);
      m.put("db_structure", 6);
      m.put("db_island", 7);
      m.put("db_level", 8);
      m.put("db_store", 9);
      m.put("db_scratch_offs", 10);
      m.put("gs_quest", 11);
      m.put("keep_alive", 12);
      m.put("gs_set_displayname", 13);
      m.put("gs_quest_event", 14);
      m.put("gs_quest_read", 15);
      m.put("gs_quest_collect", 16);
      m.put("gs_update_achievement_status", 17);
      m.put("gs_buy_island", 18);
      m.put("gs_change_island", 19);
      m.put("gs_place_on_gold_island", 20);
      m.put("gs_save_island_warp_speed", 21);
      m.put("gs_buy_egg", 22);
      m.put("gs_hatch_egg", 23);
      m.put("gs_sell_egg", 24);
      m.put("gs_breed_monsters", 25);
      m.put("gs_finish_breeding", 26);
      m.put("gs_feed_monster", 27);
      m.put("gs_move_monster", 28);
      m.put("gs_mute_monster", 29);
      m.put("gs_flip_monster", 30);
      m.put("gs_sell_monster", 31);
      m.put("gs_collect_monster", 32);
      m.put("gs_name_monster", 33);
      m.put("gs_admin_user_neighbors", 34);
      m.put("gs_neighbors", 35);
      m.put("gs_admin_multi_neighbors", 36);
      m.put("gs_multi_neighbors", 37);
      m.put("gs_store_monster", 38);
      m.put("gs_unstore_monster", 39);
      m.put("gs_light_torch", 40);
      m.put("gs_additional_friend_torch_data", 41);
      m.put("gs_get_torchgifts", 42);
      m.put("gs_collect_torchgift", 43);
      m.put("gs_box_activate_monster", 44);
      m.put("gs_box_purchase_fill_cost", 45);
      m.put("gs_box_purchase_fill", 46);
      m.put("gs_box_add_egg", 47);
      m.put("gs_box_add_monster", 48);
      m.put("gs_send_ethereal_monster", 49);
      m.put("gs_send_monster_home", 50);
      m.put("gs_buy_structure", 51);
      m.put("gs_move_structure", 52);
      m.put("gs_sell_structure", 53);
      m.put("gs_start_upgrade_structure", 54);
      m.put("gs_finish_upgrade_structure", 55);
      m.put("gs_finish_structure", 56);
      m.put("gs_flip_structure", 57);
      m.put("gs_mute_structure", 58);
      m.put("gs_start_obstacle", 59);
      m.put("gs_clear_obstacle", 60);
      m.put("gs_clear_obstacle_speed_up", 61);
      m.put("gs_start_baking", 62);
      m.put("gs_finish_baking", 63);
      m.put("gs_collect_from_mine", 64);
      m.put("gs_speed_up_structure", 65);
      m.put("gs_store_decoration", 66);
      m.put("gs_unstore_decoration", 67);
      m.put("gs_speed_up_breeding", 68);
      m.put("gs_speed_up_baking", 69);
      m.put("gs_speed_up_hatching", 70);
      m.put("gs_get_friends", 71);
      m.put("gs_get_messages", 72);
      m.put("gs_delete_message", 73);
      m.put("gs_get_friend_visit_data", 74);
      m.put("gs_visit_specific_friend_island", 75);
      m.put("gs_set_fav_friend", 76);
      m.put("gs_get_random_visit_data", 77);
      m.put("gs_get_ranked_island_data", 78);
      m.put("gs_get_island_rank", 79);
      m.put("gs_collect_invite_reward", 80);
      m.put("gs_rate_island", 81);
      m.put("gs_process_unclaimed_purchases", 82);
      m.put("gs_collect_rewards", 83);
      m.put("gs_collect_facebook_reward", 84);
      m.put("gs_currency_conversion", 85);
      m.put("gs_currency_coins2eth_conversion", 86);
      m.put("gs_currency_diamonds2eth_conversion", 87);
      m.put("gs_currency_eth2diamonds_conversion", 88);
      m.put("gs_referral_request", 89);
      m.put("gs_offer_viewed", 90);
      m.put("gs_player_has_scratch_off", 91);
      m.put("gs_play_scratch_off", 92);
      m.put("gs_purchase_scratch_off", 93);
      m.put("gs_collect_scratch_off", 94);
      m.put("gs_get_memory_game_numbers", 95);
      m.put("gs_memory_minigame_current_cost", 96);
      m.put("gs_purchase_memory_mini_game", 97);
      m.put("gs_collect_memory_mini_game", 98);
      m.put("gs_view_inhouse_ad", 99);
      m.put("gs_thirdparty_ad_viewed", 100);
      m.put("gs_thirdparty_video_ad_viewed", 101);
      m.put("gs_promos", 102);
      m.put("gs_facebook_help_nursery", 103);
      m.put("gs_send_facebook_help", 104);
      m.put("gs_handle_facebook_help_instances", 105);
      m.put("gs_request_facebook_help_permissions", 106);
      m.put("gs_purchase_buyback", 107);
      m.put("gs_admin_purchase_buyback", 108);
      m.put("gs_get_starting_ccu", 109);
      m.put("gs_get_finishing_ccu", 110);
      m.put("gs_get_ccu", 111);
      m.put("gs_admin_refresh_all", 112);
      m.put("gs_admin_check_incomplete_tutorial", 113);
      m.put("gs_admin_move_users_monster", 114);
      m.put("gs_admin_move_users_structure", 115);
      m.put("gs_admin_destroy_users_monster", 116);
      m.put("gs_admin_destroy_users_structure", 117);
      m.put("gs_admin_get_user_visit_data", 118);
      m.put("gs_admin_sell_egg", 119);
      m.put("gs_admin_buy_egg", 120);
      m.put("gs_admin_hatch_egg", 121);
      m.put("gs_admin_buy_structure", 122);
      m.put("gs_admin_buy_island", 123);
      m.put("gs_admin_change_island", 124);
      m.put("gs_admin_destroy_island", 125);
      m.put("gs_admin_feed_monster", 126);
      m.put("gs_admin_start_upgrade_structure", 127);
      m.put("gs_admin_clear_obstacle_speed_up", 128);
      m.put("gs_admin_speed_up_structure", 129);
      m.put("gs_admin_speed_up_breeding", 130);
      m.put("gs_admin_start_baking", 131);
      m.put("gs_admin_speed_up_baking", 132);
      m.put("gs_admin_remove_breeding", 133);
      m.put("gs_admin_finish_breeding", 134);
      m.put("gs_admin_speed_up_hatching", 135);
      m.put("gs_admin_give_me_shit", 136);
      m.put("gs_admin_flip_monster", 137);
      m.put("gs_admin_flip_structure", 138);
      m.put("gs_admin_send_ethereal_monster", 139);
      m.put("gs_admin_send_monster_home", 140);
      m.put("gs_admin_name_monster", 141);
      m.put("gs_admin_quest", 142);
      m.put("gs_admin_complete_quest", 143);
      m.put("gs_player", 144);
      m.put("gs_admin_check_quests", 145);
      m.put("gs_admin_kick_user", 146);
      m.put("gs_admin_store_monster", 147);
      m.put("gs_admin_unstore_monster", 148);
      m.put("gs_admin_store_decoration", 149);
      m.put("gs_admin_unstore_decoration", 150);
      m.put("gs_admin_unlight_torch", 151);
      m.put("gs_admin_box_monster_toggle", 152);
      m.put("gs_admin_box_purchase_fill", 153);
      m.put("gs_admin_box_activate_monster", 154);
      m.put("gs_test_breed_monsters", 155);
      m.put("gs_store_buddy", 156);
      m.put("gs_unstore_buddy", 157);
      m.put("gs_start_fuzing", 158);
      m.put("gs_finish_fuzing", 159);
      m.put("gs_speed_up_fuzing", 160);
      m.put("gs_admin_start_fuzing", 161);
      m.put("gs_admin_speed_up_fuzing", 162);
      m.put("gs_store_replacements", 163);
      m.put("gs_rare_monster_data", 164);
      m.put("gs_timed_events", 165);
      m.put("gs_offer_completed", 166);
      m.put("gs_mega_monster_message", 167);
      m.put("gs_collect_daily_reward", 168);
      m.put("gs_admin_store_buddy", 169);
      m.put("gs_admin_unstore_buddy", 170);
      m.put("gs_admin_finish_fuzing", 171);
      m.put("gs_place_on_tribal", 172);
      m.put("gs_tribal_feed_monster", 173);
      m.put("gs_cancel_tribe_request", 174);
      m.put("gs_send_tribe_request", 175);
      m.put("gs_get_tribal_island_data", 176);
      m.put("gs_join_tribe", 177);
      m.put("gs_leave_tribe_request", 178);
      m.put("gs_kick_tribe_request", 179);
      m.put("gs_send_tribe_invite", 180);
      m.put("gs_cancel_tribe_invite", 181);
      m.put("gs_get_code", 182);
      m.put("gs_admin_get_codes", 183);
      m.put("gs_transfer_code", 184);
      m.put("gs_sticker", 185);
      m.put("gs_delete_composer_template", 186);
      m.put("gs_save_composer_template", 187);
      m.put("gs_save_composer_track", 188);
      m.put("gs_paywall_updated", 189);
      m.put("gs_delete_mail", 190);
      m.put("gs_purchase_flip_mini_game", 191);
      m.put("gs_flip_minigame_cost", 192);
      m.put("gs_collect_flip_mini_game", 193);
      m.put("gs_daily_login_buyback", 194);
      m.put("gs_app_link", 195);
      m.put("gs_activate_island_theme", 196);
      m.put("gs_unlock_breeding_structure", 197);
      m.put("gs_test_spin_probabilities", 198);
      m.put("gs_epic_monster_data", 200);
      m.put("gs_collect_daily_currency_pack", 201);
      m.put("gs_refresh_daily_currency_pack", 202);
      m.put("gs_test_scratch", 203);
      m.put("gs_report_user", 204);
      m.put("gs_start_amber_evolve", 205);
      m.put("gs_finish_amber_evolve", 206);
      m.put("gs_speedup_amber_evolve", 207);
      m.put("gs_collect_cruc_heat", 208);
      m.put("gs_delete_account", 209);
      m.put("gs_viewed_cruc_monst", 210);
      m.put("gs_viewed_cruc_unlock", 211);
      m.put("gs_test_cruc_evolv", 212);
      m.put("gs_set_moniker", 213);
      m.put("gs_flip_levels", 214);
      m.put("gs_flip_boards", 215);
      m.put("gs_collect_flip_level", 216);
      m.put("gs_start_rebake", 217);
      m.put("gs_set_last_timed_theme", 218);
      m.put("gs_quests_read", 219);
      m.put("gs_purchase_evolve_unlock", 220);
      m.put("gs_update_island_tutorials", 221);
      m.put("gs_attempt_early_box_activate", 222);
      m.put("db_flexeggdefs", 223);
      m.put("gs_purchase_evo_powerup_unlock", 224);
      m.put("gs_admin_box_monster_toggle_new", 225);
      m.put("gs_test_collect_monster", 226);
      m.put("gs_start_attuning", 227);
      m.put("gs_finish_attuning", 228);
      m.put("gs_speedup_attuning", 229);
      m.put("db_attuner_gene", 230);
      m.put("gs_start_synthesizing", 231);
      m.put("gs_speedup_synthesizing", 232);
      m.put("gs_collect_synthesizing_failure", 233);
      m.put("gs_test_synthesis", 233);
      cmds = Collections.unmodifiableMap(m);
      Map<Integer, String> m2 = new HashMap();
      Iterator it = m.entrySet().iterator();

      while(it.hasNext()) {
         Entry<String, Integer> pair = (Entry)it.next();
         m2.put(pair.getValue(), pair.getKey());
      }

      cmds2 = Collections.unmodifiableMap(m2);
   }
}