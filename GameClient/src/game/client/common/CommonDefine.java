/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.client.common;

import java.awt.Color;

/**
 *
 * @author KenyDinh
 */
public class CommonDefine {

    //config define
    public static final String VALID_CONTENT_KEY = "VALID_CONTENT";
    public static final String VALID_CONTENT_VALUE = "VALID_VALUE";
    public static final String USERNAME_KEY = "USERNAME";
    public static final String HOST_SERVER_KEY = "HOST_SERVER";
    public static final String POST_SERVER_KEY = "PORT_SERVER";
    public static final String IP_ADDRESS = "IP_ADDRESS";
    public static final String MAC_ADDRESS = "MAC_ADDRESS";
    public static final String SERVER_MESSAGE = "SERVER NOTICE";
    public static final int DEFAULT_PORT = 6969;
    public static final int START_COIN_DEFAULT = 0;
    public static final String DATA_COIN_KEY = "d_coin";
    public static final String DATA_LEVEL_DXH_KEY = "d_lvl_dxh";
    public static final String DATA_CARO_WIN_KEY = "d_caro_win";
    public static final String DATA_BOOM_WIN_KEY = "d_boom_win";

    //common define
    public static final String TIME_FORMAT_PATERN = "HH:mm:ss";
    public static final String CONFIG_FILE_NAME = "data\\config.cfg";
    public static final String DATA_FILE_NAME = "data\\data.dat";
    public static final String ITEM_OWN_FILE_NAME = "data\\item_own.dat";
    public static final String MAP_HASH_FILE_NAME = "data\\map.dat";
    public static final String ITEM_INFO_FILE_NAME = "data\\item_info.csv";
    public static final String MAP_FILE_NAME = "data\\map.txt";
    public static final String BREAK_LINE = System.getProperty("line.separator");
    public static final String SEPARATOR_KEY_VALUE = ":";
    public static final String SEPARATOR_GROUP = ";";
    public static final String COMMA = ",";
    public static final String SPACE = " ";
    public static final int MAX_CLIENT_CONNECT = 20;

    public static final String[] LIST_GAME = {"Choose Game", "Cờ Caro", "Bầu Cua", "Boom!!!"};//, "Tiến Lên MN"};

    public static final int GAME_TYPE_CARO_GAME = 1;
    public static final int GAME_TYPE_BTCC_GAME = 2;
    public static final int GAME_TYPE_BOOM_GAME = 3;
    public static final int GAME_TYPE_TLMN_GAME = 4;
    public static final int GAME_TYPE_SL_GAME = 5;
    public static final Color JLIST_SELECTED_BACKGROUND = new Color(183, 242, 248);
    public static final Color JLIST_SELECTED_FOREGROUND = new Color(243, 93, 78);
    public static final Color MAIN_GAME_BOARD_BACKGROUND = new Color(153, 153, 153);
    public static final int MAIN_GAME_WIDTH = 670;
    public static final int MAIN_GAME_HEIGHT = 480;

    //notify message
    public static final String NOTICE_MESSAGE_KEY = "NOTICE_";
    public static final String NOTICE_ALERT_MESS_SERVER = "NOTICE_ALERT_MESS_SERVER";
    public static final String NOTICE_FINDING_MATCH = "NOTICE_GAME_FINDING_MATCH";
    public static final String NOTICE_FINDING_MATCH_FAIL = "NOTICE_GAME_FINDING_MATCH_FAIL";
    public static final String NOTICE_SERVER_OFF = "NOTICE_SERVER_IS_OFF";
    public static final String NOTICE_CLIENT_OFF = "NOTICE_CLIENT_IS_OFF";
    public static final String NOTICE_CLIENT_IN = "NOTICE_CLIENT_IN";
    public static final String NOTICE_FULL_GAME = "NOTICE_FULL_GAME";
    public static final String NOTICE_IP_EXIST_ON_SERVER = "NOTICE_IP_EXIST_ON_SERVER";
    public static final String NOTICE_CLIENT_NAME_EXIST = "NOTICE_CLIENT_NAME_EXIST";
    public static final String NOTICE_GAME_ID_EXIST = "NOTICE_GAME_ID_EXIST";
    public static final String NOTICE_FULL_CLIENT_IN_SERVER = "NOTICE_FULL_CLIENT_IN_SERVER";
    public static final String NOTICE_FULL_CLIENT_INGAME = "NOTICE_FULL_CLIENT_INGAME";
    public static final String NOTICE_CLIENT_OUT_GAME = "NOTICE_CLIENT_OUT_GAME";
    public static final String NOTICE_GAME_IS_DESTROY = "NOTICE_GAME_IS_DESTROY";
    public static final String NOTICE_NO_GAME_TO_JOIN = "NOTICE_NO_GAME_TO_JOIN";
    public static final String NOTICE_CHAT_PRIVATE = "NOTICE_SEND_PRIVATE_TO";
    public static final String NOTICE_CHAT_IN_GAME = "NOTICE_CHAT_IN_GAME";
    public static final String NOTICE_BLOCK_CHAT = "NOTICE_BLOCK_CHAT_CLIENT";
    public static final String NOTICE_MAC_ADDRESS_EXIST = "NOTICE_MAC_ADDRESS_EXIST";
    public static final String NOTICE_REQUIRE_COIN = "NOTICE_REQUIRE_COIN";

    // info message ( not hash )
    public static final String INFO_INVALID_CONFIG_FILE = "The config file is invalid!";
    public static final String INFO_ROOM_NUMBER_INPUT_TITLE = "Enter the room number!";
    public static final String INFO_USERNAME_INVALID = "Username is invalid!";
    public static final String INFO_SERVER_OFFLINE = "Server is offline!";
    public static final String INFO_CARO_WAITING_OPPONENT = "Waiting Opponent!!!";
    public static final String INFO_CARO_PLAY_TURN = "This is your turn!";
    public static final String INFO_CARO_WATCHING_GAME = "You can only watch this game!";
    public static final String INFO_RENAME_INVALID = "You don't have permission to rename!";
    public static final String INFO_CARO_GAME_DESTROY = "This game was destroy!";
    public static final String INFO_GAME_FINDING_FAIL = "Finding match fail!";
    public static final String INFO_NO_GAME_AVAILABLE = "No game available to join!";
    public static final String INFO_GAME_IS_FULL = "Not enough slot in this game!";
    public static final String INFO_CAN_NOT_CREATE_GAME = "Can not create game, please join available game!";
    public static final String INFO_ROOM_NUMBER_INPUT_MESS = "Auto join available match if room number is invalid!";
    public static final String INFO_GAME_IS_END = "This game is end!";

    public static final String INFO_UNKNOW_ERROR = "Unknow error!";

    //chat define
    public static final int MAX_CHAT_BLOCK = 5;
    public static final int TIME_BLOCK_CHAT = 10000;
    public static final String CHAT_NONE_1 = "NONE_1";
    public static final String CHAT_NONE_2 = "NONE_2";
    public static final String CHAT_NONE_3 = "NONE_3";
    public static final String CHAT_NONE_4 = "NONE_4";
    public static final String CHAT_NONE_5 = "NONE_5";
    public static final String CHAT_NONE_6 = "NONE_6";
    public static final String CHAT_NONE_7 = "NONE_7";

    //setting define
    public static final String SETTING_KEY = "SETTING_";
    public static final String SETTING_CLIENT_RENAME = "CLIENT_NAME_SET";
    public static final String SETTING_CLIENT_DATA = "SETTING_CLIENT_DATA";
    public static final String SETTING_CLIENT_DXH_DATA = "SETTING_DXH_DATA";
    public static final String SETTING_CLIENT_DXH_BASE_REWARD = "SETTING_CLIENT_DXH_BASE_REWARD";
    public static final String SETTING_ALLOW_RENAME = "SETTING_ALLOW_RENAME";
    public static final String SETTING_UPDATE_COIN = "SETTING_UPDATE_COIN";
    public static final String SETTING_UPDATE_LEVEL_DXH = "SETTING_UPDATE_LEVEL_DXH";
    public static final String SETTING_NONE_5 = "SETTING_NONE_5";
    public static final String SETTING_NONE_6 = "SETTING_NONE_6";
    public static final String SETTING_NONE_7 = "SETTING_NONE_7";

    //caro game define
    public static final String CARO_GAME_KEY = "CARO_GAME";
    public static final String CARO_GAME_POSITION = "CARO_GAME_POSITION";
    public static final String CARO_GAME_READY = "CARO_GAME_READY";
    public static final String CARO_GAME_WAITING = "CARO_GAME_WAITING";
    public static final String CARO_GAME_INFO = "CARO_GAME_INFO";
    public static final String CARO_GAME_ALLOW = "CARO_GAME_ALLOW";
    public static final String CARO_GAME_END = "CARO_GAME_END";
    public static final String CARO_GAME_VIEWER = "CARO_GAME_VIEWER";
    public static final String CARO_GAME_VALUE = "CARO_GAME_VALUE";
    public static final String CARO_GAME_FIRST_PLAY = "CARO_GAME_FIRST_PLAY";
    public static final String CARO_GAME_PLAY_AGAIN = "CARO_GAME_PLAY_AGAIN";
    public static final String CARO_GAME_EXIT_ROOM = "CARO_GAME_EXIT_ROOM";

    public static final int CARO_GAME_MAX_GAME = 10;
    public static final int CARO_GAME_CELL_SIZE = 20;
    public static final int CARO_GAME_NUM_COLUMNS = 32;
    public static final int CARO_GAME_NUM_ROWS = 23;
    public static final int CARO_GAME_X_START = 15;
    public static final int CARO_GAME_Y_START = 10;
    public static final int CARO_GAME_SCALE_SIZE = 2;
    public static final int CARO_GAME_X_VALUE = 1;
    public static final int CARO_GAME_O_VALUE = -1;
    public static final int CARO_GAME_COUNT_TO_WIN = 5;

    public static Color CARO_COLOR_X = new Color(217, 98, 98);
    public static Color CARO_COLOR_O = new Color(105, 216, 119);
    public static final Color CARO_COLOR_PA_BACK_HOVER = Color.LIGHT_GRAY;
    public static final Color CARO_COLOR_PA_TEXT_HOVER = Color.CYAN;
    public static final Color CARO_COLOR_PA_BACKGROUND = Color.DARK_GRAY;
    public static final Color CARO_COLOR_PA_TEXT = Color.WHITE;
    public static final Color CARO_COLOR_WIN_LINE = Color.YELLOW;
    public static final Color CARO_COLOR_TABLE_BORDER = Color.BLACK;
    public static final Color CARO_COLOR_CURRENT_POSITION = new Color(255, 255, 0);
    public static final Color CARO_COLOR_POSITION_X_HOVER = new Color(235, 153, 161);
    public static final Color CARO_COLOR_POSITION_O_HOVER = new Color(157, 242, 170);

    public static final int CARO_GAME_MAX_PLAYER = 2;
    public static final int CARO_GAME_MAX_VIEWER = 5;
    public static final int CARO_GAME_END_WITH_CROSS_1 = 1;
    public static final int CARO_GAME_END_WITH_CROSS_2 = 2;
    public static final int CARO_GAME_END_WITH_HOZ = 3;
    public static final int CARO_GAME_END_WITH_VER = 4;
    public static final int CARO_GAME_BUTTON_PA_MIN_X = 284;
    public static final int CARO_GAME_BUTTON_PA_MAX_X = 354;
    public static final int CARO_GAME_BUTTON_PA_MIN_Y = 215;
    public static final int CARO_GAME_BUTTON_PA_MAX_Y = 245;

    public static final String CARO_GAME_NONE_1 = "CARO_GAME_NONE_1";
    public static final String CARO_GAME_NONE_2 = "CARO_GAME_NONE_2";
    public static final String CARO_GAME_NONE_3 = "CARO_GAME_NONE_3";

    //bau cua game define
    public static final String BAUCUA_GAME_KEY = "BAUCUA_GAME";
    public static final int BAUCUA_GAME_MAX_PLAYER = 10; //
    public static final int BAUCUA_GAME_MAX_CREATED = 10;
    public static final int BAUCUA_VALUE_OF_HUOU = 1;
    public static final int BAUCUA_VALUE_OF_BAU = 2;
    public static final int BAUCUA_VALUE_OF_GA = 3;
    public static final int BAUCUA_VALUE_OF_CA = 4;
    public static final int BAUCUA_VALUE_OF_CUA = 5;
    public static final int BAUCUA_VALUE_OF_TOM = 6;
    public static final String BAUCUA_STR_OF_HUOU = "Hươu";
    public static final String BAUCUA_STR_OF_BAU = "Hồ Lô";
    public static final String BAUCUA_STR_OF_GA = "Gà";
    public static final String BAUCUA_STR_OF_CA = "Cá";
    public static final String BAUCUA_STR_OF_CUA = "Cua";
    public static final String BAUCUA_STR_OF_TOM = "Tôm";

    public static final String BAUCUA_GAME_READY_PLAY = "BAUCUA_GAME_READY_PLAY";
    public static final String BAUCUA_GAME_IS_HOST = "BAUCUA_GAME_IS_HOST";
    public static final String BAUCUA_GAME_CHANGE_HOST = "BAUCUA_GAME_CHANGE_HOST";
    public static final String BAUCUA_GAME_RESULT_VALUE = "BAUCUA_GAME_RESULT_VALUE";
    public static final String BAUCUA_GAME_START_SHAKE = "BAUCUA_GAME_START_SHAKE";
    public static final String BAUCUA_GAME_FINISH_SHAKE = "BAUCUA_GAME_FINISH_SHAKE";
    public static final String BAUCUA_GAME_JOIN_GAME = "BAUCUA_GAME_JOIN_GAME";
    public static final String BAUCUA_GAME_PLAYER_OUT = "BAUCUA_GAME_PLAYER_OUT";
    public static final String BAUCUA_GAME_VALUE_CHOOSE = "BAUCUA_GAME_VALUE_CHOOSE";
    public static final String BAUCUA_GAME_RESET = "BAUCUA_GAME_RESET";
    public static final String BAUCUA_GAME_UPDATE_TC = "BAUCUA_GAME_UPDATE_TC";
    public static final String BAUCUA_GAME_START_OPEN = "BAUCUA_GAME_START_OPEN";
    public static final String BAUCUA_GAME_FINISH_OPEN = "BAUCUA_GAME_FINISH_OPEN";
//    public static final String BAUCUA_GAME_READY_PLAY           = "BAUCUA_GAME_READY_PLAY";

    public static final int BOOM_GAME_MAX_GAME = 5;
    public static final int BOOM_GAME_MAX_PLAYER = 5;
    public static final String BOOM_GAME_KEY = "BOOM_GAME";
    public static final String BOOM_GAME_READY = "BOOM_GAME_READY";
    public static final String BOOM_GAME_IS_HOST = "BOOM_GAME_IS_HOST";
    public static final String BOOM_GAME_ENABLE_FIGHTING = "BOOM_GAME_ENABLE_FIGHTING";
    public static final String BOOM_GAME_LOAD_BATTLE = "BOOM_GAME_LOAD_BATTLE";
    public static final String BOOM_GAME_START_BATTLE = "BOOM_GAME_START_BATTLE";
    public static final String BOOM_GAME_PLAYER_JOIN = "BOOM_GAME_PLAYER_JOIN";
    public static final String BOOM_GAME_PLAYER_MOVE = "BOOM_GAME_PLAYER_MOVE";
    public static final String BOOM_GAME_ADD_BOOM = "BOOM_GAME_ADD_BOOM";
    public static final String BOOM_GAME_GET_ITEM = "BOOM_GAME_GET_ITEM";
    public static final String BOOM_GAME_USE_ITEM = "BOOM_GAME_USE_ITEM";
    public static final String BOOM_GAME_UPDATE_HP = "BOOM_GAME_UPDATE_HP";
    public static final String BOOM_GAME_SET_PLAYER_VAL = "BOOM_GAME_SET_PLAYER_VAL";
    public static final String BOOM_GAME_INIT_DATA = "BOOM_GAME_INIT_DATA";
    public static final String BOOM_GAME_EXPLODE_BOOM = "BOOM_GAME_EXPLODE_BOOM";
    public static final String BOOM_GAME_REMOVE_PR = "BOOM_GAME_REMOVE_PR";
    public static final String BOOM_GAME_REMOVE_PLAYER = "BOOM_GAME_REMOVE_PLAYER";
    public static final String BOOM_GAME_SET_TIME_OUT = "BOOM_GAME_SET_TIME_OUT";
    public static final String BOOM_GAME_ITEM_RANDOM = "BOOM_GAME_ITEM_RANDOM";
    public static final String BOOM_GAME_END_GAME = "BOOM_GAME_END_GAME";
    public static final String BOOM_GAME_GCOIN = "BOOM_GAME_GCOIN";
    public static final String BOOM_GAME_TCOIN = "BOOM_GAME_TCOIN";

    public static final String BOOM_GAME_EQUIP_DATA = "BOOM_GAME_EQUIP_DATA";
    public static final String BOOM_GAME_ADD_ITEM = "BOOM_GAME_ADD_ITEM";
    public static final String BOOM_GAME_ITEM_OWN_DATA = "BOOM_GAME_ITEM_OWN_DATA";

    //card 
    public static final int CARD_GAME_MAX_GAME = 10;
    public static final String CARD_GAME_KEY = "CARD_GAME";
    public static final String CARD_GAME_PLAYER_JOIN = "CARD_GAME_PLAYER_JOIN";
    public static final String CARD_GAME_ADD_PLAYER = "CARD_GAME_ADD_PLAYER";
    public static final String CARD_GAME_SETTING = "CARD_GAME_SETTING";// + coin per card
    public static final String CARD_GAME_IS_HOST = "CARD_GAME_IS_HOST";
    public static final String CARD_GAME_SET_HOST = "CARD_GAME_SET_HOST";
    public static final String CARD_GAME_SET_VALUE = "CARD_GAME_SET_VALUE";
    public static final String CARD_GAME_BETTING_COIN = "CARD_GAME_BETTING_COIN";
    public static final String CARD_GAME_ENABLE_PLAY = "CARD_GAME_ENABLE_PLAY";
    public static final String CARD_GAME_START_GAME = "CARD_GAME_START_GAME";
    public static final String CARD_GAME_PLAYER_CARD = "CARD_GAME_PLAYER_CARD";
    public static final String CARD_GAME_PLAYER_REST_CARD = "CARD_GAME_PLAYER_REST_CARD";
    public static final String CARD_GAME_TIME_COUNT_DOWN = "CARD_GAME_TIME_COUNT_DOWN";
    public static final String CARD_GAME_ENABLE_TURN = "CARD_GAME_ENABLE_TURN";//+ p_val
    public static final String CARD_GAME_NEXT_ROUND = "CARD_GAME_NEXT_ROUND";
    public static final String CARD_GAME_ACTION_FIGHT = "CARD_GAME_ACTION_FIGHT";
    public static final String CARD_GAME_ACTION_PASS = "CARD_GAME_ACTION_PASS";
    public static final String CARD_GAME_END_GAME = "CARD_GAME_END_GAME";
    public static final String CARD_GAME_UPDATE_COIN_END_GAME = "CARD_GAME_UPDATE_COIN_END_GAME";
    public static final String CARD_GAME_UPDATE_COIN = "CARD_GAME_UPDATE_COIN";
    public static final String CARD_GAME_RESTART_GAME_ENABLE = "CARD_GAME_RESTART_GAME_ENABLE";
    public static final String CARD_GAME_SEND_MESS = "CARD_GAME_SEND_MESS";
    public static final String CARD_GAME_PLAYER_OUT = "CARD_GAME_PLAYER_OUT";
    public static final String CARD_GAME_WAITING = "CARD_GAME_WAITING";

    //other define
    public static final int MAX_NUMBER_RANDOM = 8888888;
    public static final int MIN_NUMBER_RANDOM = 1111111;

}
