package rs.emulate.modern.script.disasm

import rs.emulate.modern.Cache
import rs.emulate.modern.Container
import rs.emulate.modern.FileStore
import rs.emulate.modern.ReferenceTable
import rs.emulate.modern.script.ClientScript666
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.HashMap

/**
 * Dumps clientscripts.
 */
object ClientScriptDumper {

    /**
     * The map of opcodes to mnemonics.
     */
    val opcodes: MutableMap<Int, String> = HashMap()

    init {
        opcodes[0] = "push_i" // push int
        opcodes[1] = "push_icfg" // push an int config ('varp')
        opcodes[2] = "set_icfg" // pop an int config ('varp')
        opcodes[3] = "push_s" // push string
        opcodes[6] = "jump" // jump
        opcodes[7] = "if_ne_i" // branch if ints not equal
        opcodes[8] = "if_eq_i" // branch if ints equal
        opcodes[9] = "if_lt_i" // branch if int <
        opcodes[10] = "if_gt_i" // branch if int >
        opcodes[21] = "return" // return int
        opcodes[25] = "push_vbit_i" // push int varbit
        opcodes[27] = "set_vbit_i" // set int varbit
        opcodes[31] = "if_lteq_i" // branch if int <=
        opcodes[32] = "if_gteq_i" // branch if int >=
        opcodes[33] = "load_i" // load int
        opcodes[34] = "store_i" // store int
        opcodes[35] = "load_s" // load string
        opcodes[36] = "store_s" // store string
        opcodes[37] = "concat_s" // concatenate
        opcodes[38] = "pop_i" // pop int
        opcodes[39] = "pop_s" // pop string
        opcodes[40] = "call" // invoke script
        opcodes[42] = "load_global_i" // load global int
        opcodes[43] = "store_global_i" // store global int
        opcodes[44] = "dim" // init array
        opcodes[45] = "push_array" // push from array[idx]
        opcodes[46] = "set_array" // set array[idx]
        opcodes[47] = "load_global_s" // loads a global string
        opcodes[48] = "store_global_s" // stores a global string
        opcodes[51] = "switch" // switch statement
        opcodes[55] = "pop_l" // pop long
        opcodes[66] = "load_l" // load long
        opcodes[67] = "store_l" // store long
        opcodes[68] = "if_ne_l" // branch if longs not equal
        opcodes[69] = "if_eq_l" // branch if longs equal
        opcodes[70] = "if_lt_l" // branch if long <
        opcodes[71] = "if_gt_l" // branch if long >
        opcodes[72] = "if_lteq_l" // branch if long <=
        opcodes[73] = "if_gteq_l" // branch if long >=
        opcodes[86] = "if_true" // branch if true (i.e. popi == 1)
        opcodes[87] = "if_false" // branch if false (i.e. popi == 0)
        opcodes[106] = "push_citvari" // push a citadel int variable
        opcodes[107] = "push_citvarbit" // push a citadel varbit
        opcodes[108] = "push_citvarl" // push a citadel long variable
        opcodes[109] = "push_citvars" // push a citadel string variable
        opcodes[112] = "push_cvari" // push a clan int variable
        opcodes[113] = "push_cvarbit" // push a clan varbit
        opcodes[114] = "push_cvarl" // push a clan long variable
        opcodes[115] = "push_cvars" // push a clan string variable

        opcodes[150] = "open_wgt" // opens a child widget
        opcodes[151] = "rm_wgt_comp" // remove widget component
        opcodes[152] = "rma_wgt_comps" // remove all widget components

        opcodes[403] = "set_part" // set kit part
        opcodes[404] = "recolor_part" // recolor body part
        opcodes[410] = "to_female" // change gender
        opcodes[411] = "equip_item" // equip item in slot

        opcodes[1000] = "set_wgt_bnds" // set the widget bounds int x int y int xdrawoffset2 int ydrawoffset2
        opcodes[1001] = "set_wgt_size" // set the size of the widget
        opcodes[1003] = "set_wgt_hidden" // sets the hidden flag of the widget

        opcodes[1101] = "set_wgt_color" // sets the colour of the widget
        opcodes[1102] = "set_wgt_filled" // sets the filled flag of the widget (i.e. draw an outline or fill).
        opcodes[1103] = "set_wgt_transp" // sets the transparency flag of the widget

        opcodes[1105] = "set_wgt_sprite" // sets the sprite of the widget

        opcodes[1108] = "set_wgt_model" // sets the model id of the widget
        opcodes[1110] = "set_wgt_anim" // sets the animation id of the widget
        opcodes[1112] = "set_wgt_txt" // sets the text of the widget
        opcodes[1113] = "set_wgt_font" // sets the font of the widget
        opcodes[1114] = "set_wgt_txt_align" // sets the text alignment of the widget

        opcodes[1120] = "set_wgt_scroll_max" // sets the maximum scroll of the widget

        opcodes[1504] = "push_wgt_hidden"// sets whether or not the widget is hidden

        opcodes[1602] = "push_wgt_txt" // push the text of the widget

        opcodes[1801] = "push_wgt_action" // push the widget action with the appr. id
        opcodes[1802] = "push_wgt_title" // push widget title text

        opcodes[2500] = "push_wgt_pos_x" // push x position of the widget
        opcodes[2501] = "push_wgt_pos_y" // push x position of the widget
        opcodes[2502] = "push_wgt_width" // push the width of the widget
        opcodes[2503] = "push_wgt_height"// push the height of the widget
        opcodes[2504] = "push_wgt_hidden" // push widget hidden/displayed (1/0)
        opcodes[2505] = "push_wgt_parent" // push parent id of widget
        opcodes[2506] = "push_wgt_key" // push the key value of the widget (used in the hash table)
        opcodes[2507] = "push_wgt_color" // push colour of widget

        opcodes[2600] = "push_wgt_scrl_x" // push widget scroll bar x
        opcodes[2601] = "push_wgt_scrl_y"
        opcodes[2602] = "push_wgt_text" // pushes the text of the widget
        opcodes[2603] = "push_wgt_scrl_width" // pushes the scroll bar width
        opcodes[2604] = "push_wgt_scrl_height" // pushes the scroll bar height
        opcodes[2605] = "push_wgt_mdl_zoom" // pushes the widget's model zoom
        opcodes[2606] = "push_wgt_mdl_rot_x" // pushes the widget's model rotation, in the x direction
        opcodes[2607] = "push_wgt_mdl_rot_y" // pushes the widget's model rotation, in the y direction
        opcodes[2608] = "push_wgt_mdl_rot_z" // pushes the widget's model rotation, in the z direction
        opcodes[2609] = "push_wgt_opacity" // push the opacity of the widget
        opcodes[2610] = "push_wgt_mdl_off_x" // pushes the widget's model's x offset
        opcodes[2611] = "push_wgt_mdl_off_y"// pushes the widget's model's y offset
        opcodes[2612] = "push_wgt_sprt" // pushes the id of the widget's sprite
        opcodes[2613] = "push_wgt_sprt_rot" // pushes the rotation of the widget's sprite
        opcodes[2614] = "push_wgt_mdl_id" // pushes the id of the model

        opcodes[2700] = "push_wgt_item_id" // push the id of the item in the widget's inv slot
        opcodes[2701] = "push_wgt_item_amt" // push the amount of the item in the widget's inv slot

        opcodes[3103] = "close_wgt" // send the close widget frame

        opcodes[3300] = "push_tick" // push current tick
        opcodes[3301] = "inv_slot_id" // push id of item in inv slot
        opcodes[3302] = "inv_slot_amt" // push amount of item in slot of inv
        opcodes[3303] = "inv_item_amt" // push total amount of item in inv
        opcodes[3304] = "inv_size" // push inventory size
        opcodes[3305] = "skill_max_lvl" // push max skill lvl
        opcodes[3306] = "skill_curr_lvl" // push current skill lvl by id
        opcodes[3307] = "skill_exp" // push skill exp amount by id
        opcodes[3308] = "pack_pos" // packs position into an int
        opcodes[3309] = "unpack_posx" // unpacks the x coordinate from pos val
        opcodes[3310] = "unpack_posz" // unpacks the height lvl from pos val
        opcodes[3311] = "unpack_posy" // unpacks the y coordinate from pos val

        opcodes[3312] = "mem_world" // push members_world
        opcodes[3313] = "oth_inv_slot_id" // push id of item in inv slot viewable to other players
        opcodes[3314] = "oth_inv_slot_amt" // push amount of item in slot of inv viewable to other players
        opcodes[3315] = "oth_inv_item_amt" // push total amount of item in inv viewable to other players

        opcodes[3317] = "sys_updt_time" // push system update time
        opcodes[3318] = "connct_id" // push connection id
        opcodes[3321] = "run_energy" // push run energy
        opcodes[3322] = "weight" // push weight
        opcodes[3323] = "is_muted" // has more than 5 black marks
        opcodes[3324] = "push_blackmark_cnt"
        opcodes[3325] = "is_mem" // push is_member state (1/0)
        opcodes[3326] = "combat_lvl" // push combat level
        opcodes[3327] = "is_female" // push is_female state (1/0)
        opcodes[3330] = "empty_inv_slots" // push amount of unused inv slots

        opcodes[3335] = "lang_id" // push the language id

        opcodes[3337] = "aff_id" // pushes affliate id
        opcodes[3338] = "profile_cpu" // profiles the cpu, pushes time to profile
        opcodes[3339] = "push_zero" // push '0' onto int stack
        opcodes[3340] = "client_focus" // push client focus
        opcodes[3341] = "billing_redrct" // push redirect_from_billing state (1/)
        opcodes[3342] = "mouse_x" // push current mouse x
        opcodes[3343] = "mouse_y" // push current mouse y
        opcodes[3349] = "cur_yaw" // push current yaw value of player (div 8)

        opcodes[3351] = "push_hld_mous_btns"

        opcodes[3600] = "friend_cnt" // push friend count
        opcodes[3601] = "friend_old_name" // push old name of friend by idx
        opcodes[3602] = "friend_world" // push friend world id
        opcodes[3603] = "push_usr_fc_rank" // push rank of usr in own fc
        opcodes[3604] = "set_usr_fc_rank" // set rank of usr in own fc
        opcodes[3605] = "add_friend" // add user to friend list by name
        opcodes[3606] = "rm_friend" // remove user from friend list by name
        opcodes[3607] = "add_ignore" // add user to ignore list by name
        opcodes[3608] = "rm_ignore" // remove user from ignore list by name
        opcodes[3609] = "is_frnd" // push is_friend state (1/0) by name
        opcodes[3610] = "frnd_game" // push friend game id by idx
        opcodes[3611] = "fc_name" // push name of current fc
        opcodes[3612] = "fc_size" // push fc size
        opcodes[3613] = "fc_usr_name" // push name of user in current fc by idx
        opcodes[3614] = "fc_usr_world" // push world of user in current fc by idx
        opcodes[3615] = "fc_usr_rank" // push rank of user in current fc by idx
        opcodes[3616] = "fc_kick_rank" // push rank req to kick in current fc
        opcodes[3617] = "kick_fc_user" // kick user in friend chat by name
        opcodes[3618] = "fc_rank" // push rank in current friend chat
        opcodes[3619] = "join_fc" // join friend chat by name
        opcodes[3620] = "exit_fc" // exit the current friend chat
        opcodes[3621] = "ignore_cnt" // push ignored player count

        opcodes[3625] = "fc_owner" // push friend chat owner name

        opcodes[3629] = "country_id" // push country id
        opcodes[3630] = "ignore_user" // ignore user from name

        opcodes[3750] = "in_guest_clan" // push in_guest_clan state (1/0)
        opcodes[3751] = "in_clan" // push in_clan state (1/0)
        opcodes[3752] = "clan_name" // push name of clan
        opcodes[3753] = "clan_kick_rank" // push rank required to kick in clan
        opcodes[3754] = "clan_talk_rank" // push rank required to talk in clan
        opcodes[3755] = "clan_mem_cnt" // push clan member count
        opcodes[3756] = "clan_mem_name" // push name of clan member
        opcodes[3757] = "clan_rank" // push rank of clan member, from member id
        opcodes[3758] = "clan_world" // push world of clan member, from member idx
        opcodes[3759] = "clan_kick_idx" // clan kick member by idx
        opcodes[3760] = "clan_idx_of_name" // push index of clan member from name
        opcodes[3761] = "clan_idx" // push index of clan member from member idx

        opcodes[3903] = "ge_off_is_sell" // push ge offer is_sell?
        opcodes[3904] = "ge_off_id" // push ge offer item id
        opcodes[3905] = "ge_off_price" // push ge offer price
        opcodes[3906] = "ge_off_quant" // push ge offer quantity
        opcodes[3907] = "ge_off_transf" // push ge offer transferred (1/0)
        opcodes[3908] = "ge_off_ttl_val" // push ge offer total value (amount * price)

        opcodes[4000] = "add_i" // add two ints on top of stack
        opcodes[4001] = "sub_i" // sub two ints on top of stack
        opcodes[4002] = "mult_i" // multiplty two ints on top of stack
        opcodes[4003] = "div_i" // divide
        opcodes[4004] = "randi_exc" // exclusive random int
        opcodes[4005] = "randi_inc" // inclusive random int
        opcodes[4006] = "lerp_y" // find a y on a line between two points, given x

        opcodes[4010] = "set_bit" // istack[--sp] & 2**istack[--sp]
        opcodes[4011] = "mod_i" // a modulo b
        opcodes[4012] = "pow" // a**b
        opcodes[4013] = "root" // nth root
        opcodes[4014] = "and" // a & b
        opcodes[4015] = "or" // a | b
        opcodes[4016] = "min" // min(a, b)
        opcodes[4017] = "max" // max(a, b)

        opcodes[4020] = "hsl2hsv" // convert hsl value to hsv

        opcodes[4100] = "append_i" // append int to str
        opcodes[4101] = "concat2" // concat 2 strs
        opcodes[4102] = "append_plusi" // append "+"i to str (e.g. "ab" and 10 -> "ab+10")
        opcodes[4103] = "lowcase_str" // str.toLowerCase
        opcodes[4104] = "append_date" // append current date
        opcodes[4105] = "cond_gender" // push(player.female ? sstack[sp - 1] : stack[sp - 2]
        opcodes[4106] = "tostr_i" // int to str
        opcodes[4107] = "cmp_str" // compare strings
        opcodes[4108] = "line_cnt" // push line count of text
        opcodes[4109] = "str_wdth_wrap" // push width of string, wrap lines
        opcodes[4110] = "str_bool" // istack[sp - 1] == 1 ? sstack[sp - 1] : sstack[sp - 2]
        opcodes[4111] = "html_esc" // escapes chevrons in a str
        opcodes[4112] = "append_c" // append char to str
        // mgi has 4113 as "is valid char" but doesn't mention any context
        opcodes[4114] = "alphanum_c" // push(alphanumeric(istack[sp - 1]) ? 1 : 0)
        opcodes[4115] = "alpha_c" // push(alpa(istack[sp - 1]) ? 1 : 0)
        opcodes[4116] = "num_c" // push(numeric(istack[sp - 1]) ? 1 : 0)
        opcodes[4117] = "str_len" // push str len
        opcodes[4118] = "substr" // push substring
        opcodes[4119] = "filter_xml" // filter anything in str between chevrons
        opcodes[4120] = "str_idx_c" // indexOf(char, startIdx)
        opcodes[4121] = "str_idx_str" // indexOf(str, startIdx)
        opcodes[4122] = "lowcase_c" // char.lowercase(c)
        opcodes[4123] = "upcase_c" // char.uppercase(c)
        opcodes[4124] = "fmt_i" // format an int (e.g. 9999 -> 9,999)
        opcodes[4125] = "str_wdth" // push width of string, no line wrapping
        opcodes[4126] = "push_time" // push time in UTC to str stack
        opcodes[4127] = "tostr_hex" // (long) hex to string

        opcodes[4200] = "item_name" // push item name
        opcodes[4201] = "item_grnd_action" // push item ground action
        opcodes[4202] = "item_wgt_action" // push item widget action
        opcodes[4203] = "item_cost" // push cost of item
        opcodes[4204] = "item_stackable" // push item stackability (1/0)
        opcodes[4205] = "item_note1" // if template doesn't exist?
        opcodes[4206] = "item_note2" // if template exists?
        opcodes[4207] = "item_members" // members item (1/0)
        opcodes[4209] = "item_wgt_cursor" // push item widget cursor value
        opcodes[4210] = "item_search_cnt" // item search result count (?)

        opcodes[5000] = "push_pub_cmode" // push public chat mode
        opcodes[5001] = "set_pub_cmode" // set the public chat mode
        opcodes[5002] = "report_player" // reports a player
        opcodes[5003] = "push_chatmsg" // pushes the public chat message text w/ appr. id
        opcodes[5004] = "push_chatmsg_typ" // pushes the public chat message type
        opcodes[5005] = "push_priv_cmode" // push private chat mode
        opcodes[5006] = "set_chat_view" // mgi says this 'sets the view of the chat' (whatever the hell this means -
        // sends some sort of frame anyway)
        opcodes[5008] = "send_msg" // sends a message to the server
        opcodes[5009] = "send_priv_msg" // sends a private chat message
        opcodes[5010] = "push_chatmsg_name" // pushes the name of the person who said a chat message
        opcodes[5011] = "push_chatmsg_chan" // pushes the current channel of the person who said a chat message
        opcodes[5012] = "push_chatmsq_qc_id" // pushes the quickchat id of a chat message
        opcodes[5015] = "push_local_name_fmt" // pushes the formatted name of the current player
        opcodes[5016] = "push_trad_cmode" // push trade chat mode
        opcodes[5019] = "push_chatmsg_name_nofil" // pushes the unfiltered name of a player who said a message
        opcodes[5020] = "push_local_name_nofmt" // pushes the unformatted name of the current player

        opcodes[5050] = "push_qc_cat_name" // push name of quick chat category
        opcodes[5051] = "push_qc_subcat_cnt" // push amount of quick chat subcategories
        opcodes[5052] = "push_qc_subcat_id" // push id of quick chat subcategory
        opcodes[5053] = "push_qc_msg_cnt" // push quick chat msg count in category
        opcodes[5054] = "push_qc_msg_id" // push quick chat msg id
        opcodes[5055] = "push_qc_msg" // push text of a qc msg
        opcodes[5056] = "push_qc_resp_cnt" // push quickchat response count
        opcodes[5057] = "push_qc_resp_id" // push id of response msg

        opcodes[5100] = "v_key_pressed" // if 'V' is pressed
        opcodes[5101] = "r_key_pressed" // if 'R' is pressed
        opcodes[5102] = "q_key_pressed" // if 'Q' is pressed

        opcodes[5419] = "push_cur_ip" // pushes the current ip address
        opcodes[5420] = "signed_client" // is the client signed 1/0

        opcodes[5423] = "println" // print str to std out

        opcodes[5429] = "exec_cmd" // executes a console command
        opcodes[5430] = "js_acc_creat" // executes the "accountcreated" js method
        opcodes[5431] = "js_acc_creat_start" // executes the "accountcreatestarted" js method

        opcodes[5435] = "use_js" // push use_js state (1/0)
        opcodes[5436] = "valid_java" // valid java version (1/0)

        opcodes[5505] = "push_cam_pitch" // pushes the current camera pitch
        opcodes[5506] = "push_cam_yaw" // pushes the current camera yaw

        opcodes[5507] = "mv_cam_up" // move the camera up
        opcodes[5508] = "mv_cam_down" // move the camera down
        opcodes[5509] = "mv_cam_right" // move the camera to the right
        opcodes[5510] = "mv_cam_left" // move the camera to the left

        opcodes[5604] = "check_email" // send check email frame
        opcodes[5605] = "create_acc" // send account creation frame

        opcodes[5609] = "push_lobby_resp" // push the lobby response onto the int stack

        opcodes[5616] = "logout" // logs the player out

        opcodes[5618] = "pop_i" // pop int
        opcodes[5619] = "pop_i" // pop int
        opcodes[5620] = "push_zero" // push zero onto the stack
        opcodes[5621] = "pop2_str_i" // pop 2 strings and 2 ints from their respective stacks
        opcodes[5622] = "nop"

        opcodes[5625] = "underage" // age < 13 ? 1 : 0

        opcodes[5630] = "cancel_login" // stops the client logging in

        // client preferences (usually changeable in settings) creds method

        opcodes[6001] = "set_bright_pref"
        opcodes[6002] = "set_anim_bg_pref" // animated background
        opcodes[6003] = "set_rm_roof_pref" // remove roofs
        opcodes[6005] = "set_grnd_dec_pref" // ground decoration
        opcodes[6007] = "set_idl_anim_pref" // idle animations
        opcodes[6008] = "set_flick_fx_pref" // set flicking effect
        opcodes[6010] = "set_char_shad_pref" // set character shadows
        opcodes[6011] = "set_scene_shad_pref"
        opcodes[6012] = "set_light_det_pref" // light detail
        opcodes[6014] = "set_water_det_pref"
        opcodes[6016] = "set_fog_pref"
        opcodes[6017] = "set_stereo_snd_pref"
        opcodes[6018] = "set_snd_fx_vol_pref" // set sound effects preference
        opcodes[6019] = "set_music_vol_pref"
        opcodes[6020] = "set_area_snd_vol_pref" // set area sound volume
        opcodes[6023] = "set_particl_pref"
        opcodes[6024] = "set_anti_alias_pref"

        opcodes[6027] = "set_bloom"
        opcodes[6028] = "set_cust_cursr_pref"

        opcodes[6101] = "push_bright_pref"
        opcodes[6102] = "anim_bg_on"
        opcodes[6103] = "rm_roof_on"
        opcodes[6105] = "grnd_dec_on"
        opcodes[6107] = "push_idl_anim_pref"
        opcodes[6108] = "flick_fx_on"
        opcodes[6110] = "char_shad_on"
        opcodes[6111] = "push_scene_shad_pref"
        opcodes[6112] = "light_det_high"
        opcodes[6114] = "water_det_high"
        opcodes[6115] = "fog_enabled"
        opcodes[6116] = "push_multisamp_pref"
        opcodes[6117] = "stereo_snd_on"
        opcodes[6118] = "push_snd_fx_vol_pref"
        opcodes[6119] = "push_mus_vol_pref"
        opcodes[6120] = "push_area_snd_vol_pref"

        opcodes[6124] = "push_anti_alias_pref"
        opcodes[6126] = "bloom_on"
        opcodes[6127] = "cust_cursr_on"
        opcodes[6129] = "push_idl_anim_pref"
        opcodes[6130] = "blend_grnd_on"
        opcodes[6131] = "curr_toolkit_val"
        opcodes[6132] = "wanted_toolkit_val"
        opcodes[6135] = "push_cpu_usage"
        opcodes[6136] = "push_text_pref"
        opcodes[6139] = "push_max_scrn_sz_pref"
        opcodes[6142] = "push_voiceovr_vol_pref"
        opcodes[6143] = "push_theme_mus_vol_pref"

        opcodes[6148] = "in_safe_mode"
        opcodes[6149] = "push_skybox_pref"

        opcodes[6300] = "push_time_mins"
        opcodes[6301] = "push_time_days"

        opcodes[6303] = "push_cur_year"
        opcodes[6304] = "is_leap_yr"
        opcodes[6305] = "push_ddmmyyyy"
        opcodes[6306] = "push_cur_day"

        opcodes[6405] = "showing_vid_ad"
        opcodes[6406] = "not_showing_vid_ad"

        opcodes[6501] = "push_fst_wrld_info"
        opcodes[6502] = "push_nxt_wrld_info"
        opcodes[6503] = "open_connection"
        opcodes[6506] = "push_wrld_info"
        opcodes[6507] = "sort_wlrd_list"
        opcodes[6508] = "connect_auto_wrld"

        opcodes[6901] = "activ_subscrptn"
        opcodes[6902] = "recovery_date"
        opcodes[6903] = "unread_msg_cnt"
        opcodes[6904] = "last_login_date"
        opcodes[6905] = "push_ip"
        opcodes[6906] = "push_email_status"

        opcodes[7005] = "to_safe_mode" // switches the client to safe mode
        opcodes[7103] = "jagtheora_present"

        opcodes[7201] = "grnd_dec_support" // current toolkit supports ground decoration
        opcodes[7202] = "char_shad_support"
        opcodes[7203] = "scene_shad_support"
        opcodes[7204] = "water_det_support"
        opcodes[7205] = "anti_alias_support"
        opcodes[7206] = "particl_support"

        opcodes[7208] = "bloom_support"
        opcodes[7209] = "grnd_blnd_support"
        opcodes[7210] = "texture_support"
        opcodes[7211] = "max_scrn_sz_support"
        opcodes[7212] = "fog_support"
        opcodes[7213] = "ortho_mode_support"
        opcodes[7214] = "wanted_toolkit_support"
        opcodes[7215] = "skybox_support"
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val store = FileStore.open("./data/resources/666")

        Cache(store).use { cache ->
            val table = ReferenceTable.decode(Container.decode(store.read(255, 12)).data)
            val output = Paths.get("./data/dump/cs/", Integer.toString(table.version))
            Files.createDirectories(output)

            for (id in 0 until table.capacity()) {
                table.getEntry(id) ?: continue

                val script = ClientScript666.decode(cache.read(12, id).data)
                val file = output.resolve(id.toString() + ".cscript")

                Files.newBufferedWriter(file, StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING).use { writer ->

                    for (op in 0 until script.length) {
                        val opcode = script.getOpcode(op)

                        var string = script.getStringOperand(op)
                        var number = script.getIntOperand(op).toLong()

                        var name: String? = opcodes[opcode]
                        if (name == null) {
                            name = "op$opcode"
                        }

                        if (string == null) {
                            if (number == 0L) {
                                number = script.getLongOperand(op)
                            }
                            string = java.lang.Long.toString(number)
                        } else {
                            string = '"' + string.replace("\"", "\\\"") + '"' // delimit double quotes
                        }

                        writer.write("$name $string\n")
                    }
                }
            }
        }
    }

}
