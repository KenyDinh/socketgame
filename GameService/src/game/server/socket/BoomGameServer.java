/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.socket;

import game.server.element.BombType;
import game.server.common.CommonDefine;
import game.server.common.CommonMethod;
import game.server.gui.ServerFrame;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author tkv-nhukhanhdinh
 */
public class BoomGameServer {

    private Map<Integer, Integer> mapStatus = new HashMap<>();

    private static final int PLAYER_1 = 1;
    private static final int PLAYER_2 = 2;
    private static final int PLAYER_3 = 3;
    private static final int PLAYER_4 = 4;
    private static final int PLAYER_5 = 5;
    private static final int WALL = 6;
    private static final int WAY = 7;
    private static final int BOX = 8;
    private static final int OTHER = 9;

    // boom 40 -> 59
    // only item great than or equal 69
    public static final int ITEM_1 = 69;//recover 1/5
    public static final int ITEM_2 = 70;//kb
    public static final int ITEM_3 = 71;//eb
    public static final int ITEM_4 = 72;//nj
    public static final int ITEM_5 = 73;//pr
    public static final int ITEM_6 = 74;//bomb
    public static final int ITEM_7 = 75;//bomb
    public static final int ITEM_8 = 76;//bomb
    public static final int ITEM_9 = 77;//bomb
    public static final int ITEM_10 = 78;//bomb
    public static final int ITEM_11 = 79;//bomb
    public static final int ITEM_12 = 80;//bomb
    public static final int ITEM_13 = 81;//bomb
    public static final int ITEM_14 = 82;//bomb
    public static final int ITEM_15 = 83;//bomb
    public static final int ITEM_16 = 84;//bomb
    public static final int ITEM_17 = 85;//bomb
    public static final int ITEM_18 = 86;//bomb
    public static final int ITEM_19 = 87;//bomb
    public static final int ITEM_20 = 88;//recover 1/10
    public static final int ITEM_21 = 89;//recover 1/4
    public static final int ITEM_MONEY_BAG = 90;//coin
    public static final int ITEM_BAG_PLUS = 91;
    public static final int ITEM_BIG_BAG_PLUS = 92;
    public static final int ITEM_BOMB_DIRECTION = 93;
    public static final int ITEM_SPEED_UP = 94;
    
    public static final int ITEM_MAX_VALUE = ITEM_SPEED_UP;

    private static final int STATUS_UP = 1;
    private static final int STATUS_RIGHT = 2;
    private static final int STATUS_DOWN = 3;
    private static final int STATUS_LEFT = 4;
    private static final int MAX_STATUS = 4;

    private static final int MAP_LENGTH = 20;

    private Map<String, ThreadBoom> mapBoom;

    private int gameId;
    private ClientServerSocket[] playerInGame;
    private int[][] map;
    private boolean endGame;
    private ClientServerSocket host;
    private static final int MAX_PLAYER = 5;
    private static final int MAX_HP = 1000;
    private int timeOut = 300;
    private boolean gameStart;
    private int gCoin;
    private Map<Integer, Map<String, Integer>> mapcoordinate = new HashMap<>();

    public BoomGameServer(int gameId) {
        this.gameId = gameId;
        mapBoom = new HashMap<>();
        // map = new int[MAP_LENGTH][MAP_LENGTH];
        endGame = false;
        playerInGame = new ClientServerSocket[MAX_PLAYER];
    }

    public Map<Integer, Integer> getMapStatus() {
        return mapStatus;
    }

    public void setMapStatus(Map<Integer, Integer> mapStatus) {
        this.mapStatus = mapStatus;
    }

    public boolean isGameStart() {
        return gameStart;
    }

    public void setGameStart(boolean gameStart) {
        this.gameStart = gameStart;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public ClientServerSocket getHost() {
        return host;
    }

    public Map<String, ThreadBoom> getMapBoom() {
        return mapBoom;
    }

    public void setMapBoom(Map<String, ThreadBoom> mapBoom) {
        this.mapBoom = mapBoom;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public ClientServerSocket[] getPlayerInGame() {
        return playerInGame;
    }

    public void setPlayerInGame(ClientServerSocket[] playerInGame) {
        this.playerInGame = playerInGame;
    }

    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    public String getMapDataAsString() {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < MAP_LENGTH; x++) {
            for (int y = 0; y < MAP_LENGTH; y++) {
                sb.append(map[x][y]);
                if (y < MAP_LENGTH - 1) {
                    sb.append(",");
                }
            }
            if (x < MAP_LENGTH - 1) {
                sb.append(";");
            }
        }
        return sb.toString();
    }

    //
    public int[][] parseMap(String data) {

        int[][] m = new int[MAP_LENGTH][MAP_LENGTH];
        if (data.length() == 0 || 0 >= data.indexOf(";") || 0 >= data.indexOf(",")) {
            for (int row = 0; row < MAP_LENGTH; row++) {
                for (int column = 0; column < MAP_LENGTH; column++) {
                    if (row == 0 || row == MAP_LENGTH - 1 || column == 0 || column == MAP_LENGTH - 1) {
                        m[row][column] = WALL;
                    } else {
                        m[row][column] = WAY;
                    }
                }
            }
            return m;
        }
        int i = 0;
        int j;
        for (String s : data.split(";")) {
            j = 0;
            for (String v : s.split(",")) {
                m[i][j] = Integer.parseInt(v.trim());
                j++;
            }
            i++;
        }
        return m;
    }
    //

    public boolean isEndGame() {
        return endGame;
    }

    public void setEndGame(boolean endGame) {
        this.endGame = endGame;
    }

    public void updateGameCoin(int coin) {
        gCoin = coin;
        int tc = getCountPlayer() * gCoin;
        sendMessToAllPlayer(CommonDefine.BOOM_GAME_GCOIN + CommonDefine.SEPARATOR_KEY_VALUE + gCoin + CommonDefine.SEPARATOR_KEY_VALUE + tc);
    }

    public void sendMessToAllPlayer(String mess) {
        for (ClientServerSocket cs : playerInGame) {
            if (cs != null) {
                cs.sendMessageToSelf(mess);
            }
        }
    }

    public void startGame() {
        setGameStart(true);
        String mess = CommonDefine.BOOM_GAME_INIT_DATA + CommonDefine.SEPARATOR_KEY_VALUE + getMapDataAsString();
        sendMessToAllPlayer(mess);
        new Thread() {

            @Override
            public void run() {
                for (int i = 7; i >= 0; i--) {
                    try {
                        sleep(1000);
                        if (i > 0 && i <= 5) {
                            sendMessToAllPlayer(CommonDefine.BOOM_GAME_LOAD_BATTLE + CommonDefine.SEPARATOR_KEY_VALUE + i);
                        } else if (i == 0) {
                            runTimeOut();
                            sendMessToAllPlayer(CommonDefine.BOOM_GAME_START_BATTLE);
                            runRandomItem();
                        }
                    } catch (InterruptedException ex) {
                    }

                }
            }
        }.start();
    }

    private void runTimeOut() {
        new Thread() {
            @Override
            public void run() {
                int count = 0;
                while (count < getTimeOut() && !isEndGame()) {
                    try {
                        count++;
                        sendMessToAllPlayer(CommonDefine.BOOM_GAME_SET_TIME_OUT + CommonDefine.SEPARATOR_KEY_VALUE + (getTimeOut() - count));
                        sleep(1000);
                    } catch (InterruptedException ex) {
                    }
                }
                setEndGame(true);
                setGameStart(false);
                removeAllPlayer();
                ServerFrame.getServerFrame().removeBoomGame(getGameId());
            }

        }.start();
    }

    private void runRandomItem() {
        new Thread() {
            @Override
            public void run() {
                while (!isEndGame()) {
                    try {
                        int time = (int) (Math.random() * 6 + 10);//10 - 15 (s)
                        sleep(time * 1000);
                        int x = 0;
                        int y = 0;
                        do {
                            x = (int) (Math.random() * MAP_LENGTH);
                            y = (int) (Math.random() * MAP_LENGTH);
                        } while (map[x][y] != WAY);
                        if (!isEndGame()) {
                            int ran = getRandomItem(200);
                            map[x][y] = ran;
                            sendMessToAllPlayer(CommonDefine.BOOM_GAME_ITEM_RANDOM + CommonDefine.SEPARATOR_KEY_VALUE + x + CommonDefine.SEPARATOR_KEY_VALUE + y + CommonDefine.SEPARATOR_KEY_VALUE + ran);
                        }
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }.start();
    }

    public int getCountPlayer() {
        int count = 0;
        for (int i = 0; i < MAX_PLAYER; i++) {
            if (playerInGame[i] != null) {
                count++;
            }
        }
        return count;
    }

    public boolean isAllPlayerReady() {
        for (ClientServerSocket cs : playerInGame) {
            if (cs != null && !cs.isBoomReady()) {
                return false;
            }
        }
        return true;
    }

    public void addPlayer(ClientServerSocket player) {
        for (int i = 0; i < MAX_PLAYER; i++) {
            if (playerInGame[i] == null) {
                randomPositionPlayer(i + 1);
                player.setBoomGameValue(i + 1);
                int status = i % MAX_STATUS + 1;
                mapStatus.put((i + 1), status);
                player.sendMessageToSelf(CommonDefine.BOOM_GAME_SET_PLAYER_VAL + CommonDefine.SEPARATOR_KEY_VALUE + (i + 1));
                player.setGameId(getGameId());
                player.setBoomHP(1000);
                player.setIsPlayer(true);
                player.setBoomReady(false);
                player.setProtect(false);
                player.setGameType(CommonDefine.GAME_TYPE_BOOM_GAME);
                playerInGame[i] = player;
                break;
            }
        }
        sendMessToAllPlayer(CommonDefine.BOOM_GAME_PLAYER_JOIN + CommonDefine.SEPARATOR_KEY_VALUE + getListPlayerInGame());
        if (getCountPlayer() == 1) {
            host = player;
            player.sendMessageToSelf(CommonDefine.BOOM_GAME_IS_HOST);
        } else {
            if (host != null) {
                host.sendMessageToSelf(CommonDefine.BOOM_GAME_ENABLE_FIGHTING);
            }
        }
        //
        int tc = getCountPlayer() * gCoin;
        sendMessToAllPlayer(CommonDefine.BOOM_GAME_GCOIN + CommonDefine.SEPARATOR_KEY_VALUE + gCoin + CommonDefine.SEPARATOR_KEY_VALUE + tc);
    }

    public void removePlayer(ClientServerSocket player) {
        for (int i = 0; i < MAX_PLAYER; i++) {
            if (playerInGame[i] != null && playerInGame[i].getClientId() == player.getClientId()) {
                int val = player.getBoomGameValue();
                int x = mapcoordinate.get(val).get("x");
                int y = mapcoordinate.get(val).get("y");
                map[x][y] = WAY;
                mapcoordinate.remove(val);
                player.setBoomGameValue(0);
                player.setGameId(0);
                player.setBoomHP(0);
                player.setGameType(0);
                player.setIsPlayer(false);
                player.setBoomReady(false);
                player.setProtect(false);
                playerInGame[i] = null;
                if (isGameStart()) {
                    sendMessToAllPlayer(CommonDefine.BOOM_GAME_REMOVE_PLAYER + CommonDefine.SEPARATOR_KEY_VALUE + val);
                } else {
                    sendMessToAllPlayer(CommonDefine.BOOM_GAME_PLAYER_JOIN + CommonDefine.SEPARATOR_KEY_VALUE + getListPlayerInGame());
                    int tc = getCountPlayer() * gCoin;
                    sendMessToAllPlayer(CommonDefine.BOOM_GAME_GCOIN + CommonDefine.SEPARATOR_KEY_VALUE + gCoin + CommonDefine.SEPARATOR_KEY_VALUE + tc);
                }
                break;
            }
        }
    }

    public ClientServerSocket getPlayerById(int clientId) {
        for (ClientServerSocket cs : playerInGame) {
            if (cs != null && cs.getClientId() == clientId) {
                return cs;
            }
        }
        return null;
    }

    private void randomPositionPlayer(int val) {
        int x = 0;
        int y = 0;
        do {
            x = (int) (Math.random() * MAP_LENGTH);
            y = (int) (Math.random() * MAP_LENGTH);
        } while (map[x][y] != WAY);
        map[x][y] = val;
        Map<String, Integer> temp = new HashMap<>();
        temp.put("x", x);
        temp.put("y", y);
        mapcoordinate.put(val, temp);
    }

    public String getListPlayerInGame() {
        StringBuilder sb = new StringBuilder();
        for (ClientServerSocket cs : playerInGame) {
            if (cs != null) {
                sb.append(cs.getClientName()).append(CommonDefine.COMMA);
            }
        }
        return sb.toString();
    }

    public void addBoom(int x, int y, int val, int p_val) {
        if (map[x][y] == WAY) {
            ThreadBoom boom = new ThreadBoom(x, y, val, p_val, this);
            String key = "" + x + ":" + y;
            map[x][y] = val;
            mapBoom.put(key, boom);
            sendMessToAllPlayer(CommonDefine.BOOM_GAME_ADD_BOOM + CommonDefine.SEPARATOR_KEY_VALUE + x + CommonDefine.SEPARATOR_KEY_VALUE + y + CommonDefine.SEPARATOR_KEY_VALUE + val + CommonDefine.SEPARATOR_KEY_VALUE + p_val);
            boom.start();
        }

    }

    public void movePlayer(int x, int y, int val, int status, String type) {
        boolean flag = true;
        boolean kick = type.equals("kick");
        boolean eat = type.equals("eat");
        int pre_x = x;
        int pre_y = y;
        if (status != mapStatus.get(val)) {
            //quay mat
        } else {
            switch (status) {
                case STATUS_UP:
                    pre_x = x + 1;
                    pre_y = y;
                    break;
                case STATUS_RIGHT:
                    pre_x = x;
                    pre_y = y - 1;
                    break;
                case STATUS_DOWN:
                    pre_x = x - 1;
                    pre_y = y;
                    break;
                case STATUS_LEFT:
                    pre_x = x;
                    pre_y = y + 1;
                    break;
                default:
                    break;
            }
            if (!kick && !eat) {
                if (map[x][y] != WAY && !isItem(map[x][y])) {
                    flag = false;
                }
            } else {
                if (!isBoom(map[x][y])) {
                    flag = false;
                }
            }

        }
        if (flag) {
            map[pre_x][pre_y] = WAY;
            int it = 0;
            String s = "";
            if (isItem(map[x][y])) {
                it = map[x][y];
                if (it == ITEM_5) {//protect
                    ClientServerSocket cs = getClientByBoomValue(val);
                    if (cs != null) {
                        cs.setProtect(true);
                    }
                }
            } else if (isBoom(map[x][y])) {
                s = calcBoomChange(x, y, status, type, map[x][y]);
            }
            String mess = CommonDefine.BOOM_GAME_PLAYER_MOVE + CommonDefine.SEPARATOR_KEY_VALUE + x + CommonDefine.SEPARATOR_KEY_VALUE + y + CommonDefine.SEPARATOR_KEY_VALUE + val + CommonDefine.SEPARATOR_KEY_VALUE + status + CommonDefine.SEPARATOR_KEY_VALUE + it + CommonDefine.SEPARATOR_KEY_VALUE + type + CommonDefine.SEPARATOR_KEY_VALUE + s;
            map[x][y] = val;
            mapStatus.put(val, status);
            sendMessToAllPlayer(mess);
        } else {
            sendMessToAllPlayer(CommonDefine.BOOM_GAME_PLAYER_MOVE + val);
        }
    }

    private ClientServerSocket getClientByBoomValue(int val) {
        for (ClientServerSocket cs : playerInGame) {
            if (cs != null && cs.getBoomGameValue() == val) {
                return cs;
            }
        }
        return null;
    }

    private String calcBoomChange(int x, int y, int status, String type, int val) {
        StringBuilder sb = new StringBuilder();
        if (type.equals("kick")) {
            switch (status) {
                case STATUS_UP:
                    if (map[x - 1][y] == WAY) {
                        int index = 1;
                        while (map[x - index][y] == WAY) {
                            index++;
                        }
                        int new_x = x - index + 1;
                        ThreadBoom tb = mapBoom.get((x + ":" + y));
                        tb.setX(new_x);
                        mapBoom.remove((x + ":" + y));
                        mapBoom.put(new_x + ":" + y, tb);
                        map[new_x][y] = val;
                        sb.append(new_x).append(CommonDefine.SEPARATOR_KEY_VALUE).append(y);
                    }
                    break;
                case STATUS_DOWN:
                    if (map[x + 1][y] == WAY) {
                        int index = 1;
                        while (map[x + index][y] == WAY) {
                            index++;
                        }
                        int new_x = x + index - 1;
                        ThreadBoom tb = mapBoom.get((x + ":" + y));
                        tb.setX(new_x);
                        mapBoom.remove((x + ":" + y));
                        mapBoom.put(new_x + ":" + y, tb);
                        map[new_x][y] = val;
                        sb.append(new_x).append(CommonDefine.SEPARATOR_KEY_VALUE).append(y);
                    }
                    break;
                case STATUS_LEFT:
                    if (map[x][y - 1] == WAY) {
                        int index = 1;
                        while (map[x][y - index] == WAY) {
                            index++;
                        }
                        int new_y = y - index + 1;
                        ThreadBoom tb = mapBoom.get((x + ":" + y));
                        tb.setY(new_y);
                        mapBoom.remove((x + ":" + y));
                        mapBoom.put(x + ":" + new_y, tb);
                        map[x][new_y] = val;
                        sb.append(x).append(CommonDefine.SEPARATOR_KEY_VALUE).append(new_y);
                    }
                    break;
                case STATUS_RIGHT:
                    if (map[x][y + 1] == WAY) {
                        int index = 1;
                        while (map[x][y + index] == WAY) {
                            index++;
                        }
                        int new_y = y + index - 1;
                        ThreadBoom tb = mapBoom.get((x + ":" + y));
                        tb.setY(new_y);
                        mapBoom.remove((x + ":" + y));
                        mapBoom.put(x + ":" + new_y, tb);
                        map[x][new_y] = val;
                        sb.append(x).append(CommonDefine.SEPARATOR_KEY_VALUE).append(new_y);
                    }
                    break;
                default:
                    break;
            }
        } else if (type.equals("eat")) {
            ThreadBoom tb = mapBoom.get((x + ":" + y));
            tb.removeBoom();
            mapBoom.remove((x + ":" + y));
        }
        return sb.toString();
    }

    private String reduceHpPlayer(int damage, int victim_val, int x, int y, int creator_val, int ratio) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < MAX_PLAYER; i++) {
            if (playerInGame[i].getBoomGameValue() == victim_val) {
                if (playerInGame[i].isProtect()) {
                    break;
                }
                if (playerInGame[i].getBoomHP() <= 0) {
                    return sb.toString();
                }
                int pre_hp = playerInGame[i].getBoomHP();
                playerInGame[i].setBoomHP(pre_hp - damage);
                int cur_hp = playerInGame[i].getBoomHP();
                sendMessToAllPlayer(CommonDefine.BOOM_GAME_UPDATE_HP + CommonDefine.SEPARATOR_KEY_VALUE + cur_hp + CommonDefine.SEPARATOR_KEY_VALUE + victim_val);
                if (cur_hp <= 0) {
                    map[x][y] = ITEM_MONEY_BAG;
                    sb.append(CommonDefine.SEPARATOR_KEY_VALUE).append(ITEM_MONEY_BAG);
                }
                if (victim_val != creator_val && creator_val > 0 && ratio > 0) {
                    ClientServerSocket creator = getPlayerByValue(creator_val);
                    if (creator == null) {
                        CommonMethod.log("Get Bomb Creator Null " + creator_val);
                    } else if (creator.getBoomHP() > 0 && creator.getBoomHP() < MAX_HP) {
                        int recover_hp = (int) (((pre_hp - cur_hp) * ratio) / 100);
                        creator.setBoomHP(creator.getBoomHP() + recover_hp);
                        sendMessToAllPlayer(CommonDefine.BOOM_GAME_UPDATE_HP + CommonDefine.SEPARATOR_KEY_VALUE + creator.getBoomHP() + CommonDefine.SEPARATOR_KEY_VALUE + creator_val);
                    }
                }

                break;
            }
        }
        return sb.toString();
    }

    private ClientServerSocket getPlayerByValue(int val) {
        for (ClientServerSocket css : playerInGame) {
            if (css != null && css.getBoomGameValue() == val) {
                return css;
            }
        }
        return null;
    }

    private String calCoordinateExplode(int x, int y, int bomb_val, int creator_val) {
        StringBuilder sb = new StringBuilder();
        if (x >= map.length || x < 0 || y >= map.length || y < 0) {
            return sb.toString();
        }
        if (map[x][y] != WALL && map[x][y] != OTHER) {
            String key = "" + x + ":" + y;
            if (mapBoom.containsKey(key) && isBoom(map[x][y])) {
                sb.append(mapBoom.get(key).explodeBoom());
            } else {
                sb.append(x).append(CommonDefine.SEPARATOR_KEY_VALUE).append(y);
                if (isPlayer(map[x][y])) {
                    BombType bomb_type = BombType.valueOf(bomb_val);
                    sb.append(reduceHpPlayer(bomb_type.getDamage(), map[x][y], x, y, creator_val, bomb_type.getRecoverRatio()));
                } else if (isItem(map[x][y])) {
                    map[x][y] = WAY;
                } else if (map[x][y] == BOX) {
                    int ran = getRandomItem(30);
                    if (isItem(ran)) {
                        map[x][y] = ran;
                    } else {
                        map[x][y] = WAY;
                    }
                    sb.append(CommonDefine.SEPARATOR_KEY_VALUE).append(ran);
                }
                sb.append(CommonDefine.COMMA);
            }
        }

        return sb.toString();
    }

    public String explodeBoom(int x, int y, int bomb_val, int creator_val) {
        StringBuilder sb = new StringBuilder();
        if (!isBoom(map[x][y])) {
            return sb.toString();
        }
        String key = "" + x + ":" + y;
        map[x][y] = WAY;
        if (!mapBoom.containsKey(key)) {
            return sb.toString();
        }
        mapBoom.remove(key);
        sb.append(x).append(CommonDefine.SEPARATOR_KEY_VALUE).append(y).append(CommonDefine.COMMA);
        BombType bomb_type = BombType.valueOf(bomb_val);
        sb.append(calcRangeBombExlode(x, y, bomb_type.getValue(), creator_val, bomb_type.getHorizontalLen(), bomb_type.getVerticalLen()));
        if (bomb_type.getSquareLen() > 0) {
            sb.append(calcSquareBombExplode(x, y, bomb_type.getSquareLen(), bomb_type.getValue(), creator_val));
        }
        return sb.toString();
    }

    // vertical + horizontal
    private String calcRangeBombExlode(int x, int y, int bomb_val, int creator_val, int lenx, int leny) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= lenx; i++) {
            if (x + i > map.length - 1) {
                break;
            }
            if (map[x + i][y] == WALL || map[x + i][y] == BOX || map[x + i][y] == OTHER) {
                if (map[x + i][y] == BOX) {
                    sb.append(calCoordinateExplode(x + i, y, bomb_val, creator_val));
                }
                break;
            }
            sb.append(calCoordinateExplode(x + i, y, bomb_val, creator_val));
        }
        for (int i = 1; i <= lenx; i++) {
            if (x - i < 0) {
                break;
            }
            if (map[x - i][y] == WALL || map[x - i][y] == BOX || map[x - i][y] == OTHER) {
                if (map[x - i][y] == BOX) {
                    sb.append(calCoordinateExplode(x - i, y, bomb_val, creator_val));
                }
                break;
            }
            sb.append(calCoordinateExplode(x - i, y, bomb_val, creator_val));
        }
        for (int i = 1; i <= leny; i++) {
            if (y + i > map.length - 1) {
                break;
            }
            if (map[x][y + i] == WALL || map[x][y + i] == BOX || map[x][y + i] == OTHER) {
                if (map[x][y + i] == BOX) {
                    sb.append(calCoordinateExplode(x, y + i, bomb_val, creator_val));
                }
                break;
            }
            sb.append(calCoordinateExplode(x, y + i, bomb_val, creator_val));
        }
        for (int i = 1; i <= leny; i++) {
            if (y - i < 0) {
                break;
            }
            if (map[x][y - i] == WALL || map[x][y - i] == BOX || map[x][y - i] == OTHER) {
                if (map[x][y - i] == BOX) {
                    sb.append(calCoordinateExplode(x, y - i, bomb_val, creator_val));
                }
                break;
            }
            sb.append(calCoordinateExplode(x, y - i, bomb_val, creator_val));
        }
        return sb.toString();
    }

    //square
    private String calcSquareBombExplode(int x, int y, int len, int bomb_val, int creator_val) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= len; i++) {
            for (int j = 1; j <= len; j++) {
                int mlen = map.length;
                if (x + i < mlen && y + j < mlen) {
                    int temp_1 = map[x + i][y + j];
                    if (temp_1 != WALL || temp_1 != OTHER) {
                        sb.append(calCoordinateExplode(x + i, y + j, bomb_val, creator_val));
                    }
                }
                if (x + i < mlen && y - j >= 0) {
                    int temp_2 = map[x + i][y - j];
                    if (temp_2 != WALL || temp_2 != OTHER) {
                        sb.append(calCoordinateExplode(x + i, y - j, bomb_val, creator_val));
                    }
                }
                if (x - i >= 0 && y + j < mlen) {
                    int temp_3 = map[x - i][y + j];
                    if (temp_3 != WALL || temp_3 != OTHER) {
                        sb.append(calCoordinateExplode(x - i, y + j, bomb_val, creator_val));
                    }
                }
                if (x - i >= 0 && y - j >= 0) {
                    int temp_4 = map[x - i][y - j];
                    if (temp_4 != WALL || temp_4 != OTHER) {
                        sb.append(calCoordinateExplode(x - i, y - j, bomb_val, creator_val));
                    }
                }
            }
        }
        return sb.toString();
    }

    public void checkGameEndByHp() {
        if (isEndGame()) {
            return;
        }
        int count = 0;
        for (int i = 0; i < MAX_PLAYER; i++) {
            if (playerInGame[i] != null && playerInGame[i].getBoomHP() > 0) {
                count++;
            }
        }
        if (count <= 1) {
            setEndGame(true);
            setGameStart(false);
            sendMessToAllPlayer(CommonDefine.BOOM_GAME_END_GAME);
            removeAllPlayer();
            ServerFrame.getServerFrame().removeBoomGame(getGameId());
        }
    }

    public void removeAllPlayer() {
        for (ClientServerSocket player : playerInGame) {
            if (player != null) {
                player.setBoomGameValue(0);
                player.setGameId(0);
                player.setBoomHP(0);
                player.setGameType(0);
                player.setIsPlayer(false);
                player.setBoomReady(false);
                player.setProtect(false);
            }
        }
    }

    private int getRandomItem(int per) {
        int p = (int) (Math.random() * 100);
        if (p < per) {
            return (int) (Math.random() * (ITEM_SPEED_UP - ITEM_1 + 1) + ITEM_1);
        }
        return -1;
    }

    private static boolean isPlayer(int value) {
        return (value >= PLAYER_1 && value <= PLAYER_5);
    }

    private static boolean isItem(int value) {
        return (value >= ITEM_1 && value <= ITEM_SPEED_UP);
    }

    private static boolean isBoom(int value) {
        return (value >= BombType.MIN_BOMB_VALUE && value <= BombType.MAX_BOMB_VALUE);
    }
}
