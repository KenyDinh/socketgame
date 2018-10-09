/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.client.socket;

import game.client.common.CommonDefine;
import game.client.element.Equipment;
import game.client.gui.BoomFrame;
import game.client.gui.ClientFrame;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import static java.lang.Thread.sleep;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tkv-nhukhanhdinh
 */
public class BoomGameClient {

    private static int KICK_BOOM_COUNT_DOWN = 30;
    private static int EAT_BOOM_COUNT_DOWN = 5;
    private static int PROTECT_TIME_COUNT_DOWN = 10;
    private static int NINJA_TIME_COUNT_DOWN = 30;
    private static int RECOVERY_RAITO_1 = 10;
    private static int RECOVERY_RAITO_2 = 20;
    private static int RECOVERY_RAITO_3 = 25;
    private static int BAG_PLUS_PARAM = 2;
    private static int BIG_BAG_PLUS_PARAM = 3;
    private static int MONEY_BAG_PARAM = 500;
    private static int SPEED_UP_PARAM = 2;
    private static int SPEED_UP_TIME_PARAM = 15;

    private static final Color COLOR_BACK_1 = new Color(84, 143, 185);
    private static final String READY_TEXY = "Ready";
    public static final int READY_X = 20;
    public static final int READY_Y = 320;
    public static final int READY_SIZE = 40;
    private static final String SETTING_TEXT = "Setting";
    public static final int SETTING_X = 20;
    public static final int SETTING_Y = READY_Y + READY_SIZE + 10;
    public static final int SETTING_SIZE = READY_SIZE;
    public static final int FIGHT_WIDHT = 150;
    public static final int FIGHT_HEIGHT = 68;
    public static final int FIGHT_X = (CommonDefine.MAIN_GAME_WIDTH - FIGHT_WIDHT) / 2;
    public static final int FIGHT_Y = (CommonDefine.MAIN_GAME_HEIGHT - FIGHT_HEIGHT) / 2;

    public static final int PLAYER_1 = 1;
    private static final int PLAYER_2 = 2;
    private static final int PLAYER_3 = 3;
    private static final int PLAYER_4 = 4;
    public static final int PLAYER_5 = 5;
    private static final int SHADOW = -1;

    private static final int WALL = 6;
    private static final int WAY = 7;
    private static final int BOX = 8;

    private static final int OTHER = 9;
    public static final int OTHER_COUNT = 18;

    // boom 40 -> 50
    public static final int BOOM_1 = 40;
    public static final int BOOM_2 = 41;
    public static final int BOOM_3 = 42;
    public static final int BOOM_4 = 43;
    public static final int BOOM_5 = 44;

    public static final int BOOM_6 = 45;
    public static final int BOOM_7 = 46;
    public static final int BOOM_8 = 47;
    public static final int BOOM_9 = 48;
    public static final int BOOM_10 = 49;

    public static final int BOOM_11 = 50;
    public static final int BOOM_12 = 51;
    public static final int BOOM_13 = 52;
    public static final int BOOM_14 = 53;
    public static final int BOOM_15 = 54;

    public static final int BOOM_16 = 55;
    public static final int BOOM_17 = 56;
    public static final int BOOM_18 = 57;
    public static final int BOOM_19 = 58;
    public static final int BOOM_20 = 59;
    public static final int BOOM_21 = 60;
    public static final int BOOM_22 = 61;
    public static final int BOOM_23 = 62;
    public static final int BOOM_24 = 63;
    public static final int BOOM_25 = 64;
    public static final int BOOM_26 = 65;
    public static final int BOOM_27 = 66;
    public static final int BOOM_28 = 67;
    public static final int BOOM_29 = 68;

    // only item greater than or equal 69
    public static final int ITEM_RECOVERY_1 = 69;//recover 1/10
    public static final int ITEM_SUPER_KICK = 70;//kb
    public static final int ITEM_SUPER_EAT = 71;//eb
    public static final int ITEM_NINJA = 72;//nj
    public static final int ITEM_PROTECTED = 73;//pr
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
    public static final int ITEM_RECOVERY_2 = 88;//recover 1/5
    public static final int ITEM_RECOVERY_3 = 89;//recover 1/4
    public static final int ITEM_MONEY_BAG = 90;//coin
    public static final int ITEM_BAG_PLUS = 91;
    public static final int ITEM_BIG_BAG_PLUS = 92;
    public static final int ITEM_BOMB_DIRECTION = 93;
    public static final int ITEM_SPEED_UP = 94;

    public static final int ITEM_MAX_VALUE = ITEM_SPEED_UP;

    public static final int STATUS_UP = 1;
    public static final int STATUS_RIGHT = 2;
    public static final int STATUS_DOWN = 3;
    public static final int STATUS_LEFT = 4;
    private static final int MAX_HP = 1000;

    private static int SIZE = 40;
    private static int MAP_SIZE = 800;
    private static final int HP_HEIGHT = 5;
    private static final Color BACKGROUND = new Color(130, 130, 130);
    //game data
    private int myValue;
    private int myPX;
    private int myPY;
    private int myBoomValue;
    private boolean isBoomCharge;
    private int myTotalBoom;
    private int maxBoomCreate;
    private int timeCharge;
    private int m_coin;
    private int g_coin;
    private int totalCoin;
    private int luckyRatio;
    private int speed = 1;
    private boolean isReady;
    private boolean hasBagPlus;
    private boolean hasBigBagPlus;
    private boolean hasBombDirection;
    private boolean backBombDirection;
    private BoomFrame boomFrame;
    private int[][] map;
    private static Map<String, Image> mapImg;
    private Map<Integer, Integer> mapStatus;
    private Map<Integer, Integer> mapHP;
    private Map<String, Integer> mapcoordinate;
    private Map<Integer, Map<String, Boolean>> mapFlag;
    private Map<Integer, Map<String, Integer>> mapCount;

    public BoomGameClient() {

    }

    public BoomGameClient(BoomFrame boomFrame) {
        this.myValue = 0;
        this.myPX = 0;
        this.myPY = 0;
        this.myBoomValue = 0;
        this.isBoomCharge = false;
        this.myTotalBoom = 0;
        this.maxBoomCreate = 0;
        this.m_coin = 0;
        this.g_coin = 0;
        this.totalCoin = 0;
        this.isReady = false;
        this.boomFrame = boomFrame;
        this.map = null;
        this.mapStatus = new HashMap<>();
        this.mapHP = new HashMap<>();
        this.mapcoordinate = new HashMap<>();
        this.mapFlag = new HashMap<>();
        this.mapCount = new HashMap<>();

        initParamItem();
    }

    public static void setSIZE(int SIZE) {
        BoomGameClient.SIZE = SIZE;
    }

    public static void setMAP_SIZE(int MAP_SIZE) {
        BoomGameClient.MAP_SIZE = MAP_SIZE;
    }

    public boolean isHasBombDirection() {
        return hasBombDirection;
    }

    public void setHasBombDirection(boolean hasBombDirection) {
        this.hasBombDirection = hasBombDirection;
    }

    public void toggleBackBombDirection() {
        this.backBombDirection = !this.backBombDirection;
    }

    public boolean isBackBombDirection() {
        return backBombDirection;
    }

    public void setHasBagPlus(boolean hasBagPlus) {
        this.hasBagPlus = hasBagPlus;
    }

    public void setHasBigBagPlus(boolean hasBigBagPlus) {
        this.hasBigBagPlus = hasBigBagPlus;
    }

    public Map<Integer, Integer> getMapHP() {
        return mapHP;
    }

    public int getMyValue() {
        return myValue;
    }

    public int getM_coin() {
        return m_coin;
    }

    public int getG_coin() {
        return g_coin;
    }

    private void initParamItem() {
        Equipment kb = ClientFrame.getClientFrame().getEquipmentById(ITEM_SUPER_KICK);
        if (kb != null) {
            KICK_BOOM_COUNT_DOWN = kb.getParam_1();
        }
        Equipment eb = ClientFrame.getClientFrame().getEquipmentById(ITEM_SUPER_EAT);
        if (eb != null) {
            EAT_BOOM_COUNT_DOWN = eb.getParam_1();
        }
        Equipment nj = ClientFrame.getClientFrame().getEquipmentById(ITEM_NINJA);
        if (nj != null) {
            NINJA_TIME_COUNT_DOWN = nj.getParam_1();
        }
        Equipment pr = ClientFrame.getClientFrame().getEquipmentById(ITEM_PROTECTED);
        if (pr != null) {
            PROTECT_TIME_COUNT_DOWN = pr.getParam_1();
        }
        Equipment recover_1 = ClientFrame.getClientFrame().getEquipmentById(ITEM_RECOVERY_1);
        if (recover_1 != null) {
            RECOVERY_RAITO_1 = recover_1.getParam_1();
        }
        Equipment recover_2 = ClientFrame.getClientFrame().getEquipmentById(ITEM_RECOVERY_2);
        if (recover_2 != null) {
            RECOVERY_RAITO_2 = recover_2.getParam_1();
        }
        Equipment recover_3 = ClientFrame.getClientFrame().getEquipmentById(ITEM_RECOVERY_3);
        if (recover_3 != null) {
            RECOVERY_RAITO_3 = recover_3.getParam_1();
        }
        Equipment bag_plus = ClientFrame.getClientFrame().getEquipmentById(ITEM_BAG_PLUS);
        if (bag_plus != null) {
            BAG_PLUS_PARAM = bag_plus.getParam_1();
        }
        Equipment big_bag_plus = ClientFrame.getClientFrame().getEquipmentById(ITEM_BIG_BAG_PLUS);
        if (big_bag_plus != null) {
            BIG_BAG_PLUS_PARAM = big_bag_plus.getParam_1();
        }
        Equipment money_bag = ClientFrame.getClientFrame().getEquipmentById(ITEM_MONEY_BAG);
        if (money_bag != null) {
            MONEY_BAG_PARAM = money_bag.getParam_1();
        }
        Equipment speed_up = ClientFrame.getClientFrame().getEquipmentById(ITEM_SPEED_UP);
        if (speed_up != null) {
            if (speed_up.getParam_1() < SPEED_UP_PARAM) {
                SPEED_UP_PARAM = speed_up.getParam_1();
            }
            if (speed_up.getParam_2() >= 5) {
                SPEED_UP_TIME_PARAM = speed_up.getParam_2();
            }
        }
    }

    public void initGameData(int[][] mapdata, int my_value, int boomValue, int maxBoom, int timeC, int luckyRate) {
        map = mapdata;
        myValue = my_value;
        myBoomValue = boomValue;
        myTotalBoom = maxBoom;
        maxBoomCreate = maxBoom;
        timeCharge = timeC;
        luckyRatio = luckyRate;
        mapStatus.put(PLAYER_1, 1);
        mapStatus.put(PLAYER_2, 2);
        mapStatus.put(PLAYER_3, 3);
        mapStatus.put(PLAYER_4, 4);
        mapStatus.put(PLAYER_5, 1);

        mapHP.put(PLAYER_1, MAX_HP);
        mapHP.put(PLAYER_2, MAX_HP);
        mapHP.put(PLAYER_3, MAX_HP);
        mapHP.put(PLAYER_4, MAX_HP);
        mapHP.put(PLAYER_5, MAX_HP);

        //<editor-fold defaultstate="collapse" desc="Init Map Count">
        Map<String, Integer> temp_1 = new HashMap<>();
        temp_1.put("kb_count", 0);
        temp_1.put("eb_count", 0);
        temp_1.put("pr_count", 0);
        temp_1.put("nj_count", 0);
        Map<String, Integer> temp_2 = new HashMap<>();
        temp_2.put("kb_count", 0);
        temp_2.put("eb_count", 0);
        temp_2.put("pr_count", 0);
        temp_2.put("nj_count", 0);
        Map<String, Integer> temp_3 = new HashMap<>();
        temp_3.put("kb_count", 0);
        temp_3.put("eb_count", 0);
        temp_3.put("pr_count", 0);
        temp_3.put("nj_count", 0);
        Map<String, Integer> temp_4 = new HashMap<>();
        temp_4.put("kb_count", 0);
        temp_4.put("eb_count", 0);
        temp_4.put("pr_count", 0);
        temp_4.put("nj_count", 0);
        Map<String, Integer> temp_5 = new HashMap<>();
        temp_5.put("kb_count", 0);
        temp_5.put("eb_count", 0);
        temp_5.put("pr_count", 0);
        temp_5.put("nj_count", 0);

        mapCount.put(PLAYER_1, temp_1);
        mapCount.put(PLAYER_2, temp_2);
        mapCount.put(PLAYER_3, temp_3);
        mapCount.put(PLAYER_4, temp_4);
        mapCount.put(PLAYER_5, temp_5);
        //</editor-fold>

        //<editor-fold defaultstate="collapse" desc="Init Map Flag">
        Map<String, Boolean> b_1 = new HashMap<>();
        b_1.put("kb", false);
        b_1.put("eb", false);
        b_1.put("pr", false);
        b_1.put("nj", false);
        b_1.put("ot", false);
        Map<String, Boolean> b_2 = new HashMap<>();
        b_2.put("kb", false);
        b_2.put("eb", false);
        b_2.put("pr", false);
        b_2.put("nj", false);
        b_2.put("ot", false);
        Map<String, Boolean> b_3 = new HashMap<>();
        b_3.put("kb", false);
        b_3.put("eb", false);
        b_3.put("pr", false);
        b_3.put("nj", false);
        b_3.put("ot", false);
        Map<String, Boolean> b_4 = new HashMap<>();
        b_4.put("kb", false);
        b_4.put("eb", false);
        b_4.put("pr", false);
        b_4.put("nj", false);
        b_4.put("ot", false);
        Map<String, Boolean> b_5 = new HashMap<>();
        b_5.put("kb", false);
        b_5.put("eb", false);
        b_5.put("pr", false);
        b_5.put("nj", false);
        b_5.put("ot", false);

        mapFlag.put(PLAYER_1, b_1);
        mapFlag.put(PLAYER_2, b_2);
        mapFlag.put(PLAYER_3, b_3);
        mapFlag.put(PLAYER_4, b_4);
        mapFlag.put(PLAYER_5, b_5);

        //</editor-fold>
        //<editor-fold defaultstate="collapse" desc="Init Map Boom Value">
        //</editor-fold>
    }

    public void setMyBoomValue(int myBoomValue) {
        this.myBoomValue = myBoomValue;
    }

    public void setMaxBoomCreate(int maxBoomCreate) {
        this.maxBoomCreate = maxBoomCreate;
    }

    public void setTimeCharge(int timeCharge) {
        this.timeCharge = timeCharge;
    }

    public void initCoinGame(int mc, int gc, int tc) {
        m_coin = mc;
        g_coin = gc;
        totalCoin = tc;
    }

    public void toggleReady() {
        isReady = !isReady;
    }

    public boolean IsReady() {
        return isReady;
    }

    public static void initImageGame(Map<String, Image> imgs) {
        mapImg = imgs;
    }

    public void drawMainGame(Graphics g) {
        g.setColor(BACKGROUND);
        g.fillRect(0, 0, MAP_SIZE, MAP_SIZE);
        if (map == null) {
            return;
        }

        boomFrame.updateHPImage(mapImg.get("hp"));
        if (mapHP.get(myValue) <= 0) {
            boomFrame.updateBombImage(mapImg.get("rip"));
        } else {
            boomFrame.updateBombImage(mapImg.get("bomb_" + myBoomValue));
        }
        boomFrame.updateCNImage(mapImg.get("coin"));
        if (mapFlag.get(myValue).get("kb")) {
            boomFrame.updateKBImage(mapImg.get("item_" + ITEM_SUPER_KICK));
        }
        if (mapFlag.get(myValue).get("eb")) {
            boomFrame.updateEBImage(mapImg.get("item_" + ITEM_SUPER_EAT));
        }
        if (mapFlag.get(myValue).get("nj")) {
            boomFrame.updateNJImage(mapImg.get("item_" + ITEM_NINJA));
        }
        if (mapFlag.get(myValue).get("pr")) {
            boomFrame.updatePRImage(mapImg.get("item_" + ITEM_PROTECTED));
        }

        int len = map.length;

        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                drawUnit(g, i, j, map[i][j]);
            }
        }
    }

    private void drawUnit(Graphics g, int i, int j, int val) {
        switch (val) {
            case PLAYER_1:
            case PLAYER_2:
            case PLAYER_3:
            case PLAYER_4:
            case PLAYER_5:
                drawStartPlayer(g, i, j, val);
                break;

            case WAY:
            case SHADOW:
                drawWayStart(g, i, j);
                break;

            case WALL:
                drawWallStart(g, i, j);
                break;

            case BOX:
                drawBoxStart(g, i, j);
                break;

            case OTHER:
                drawOther(g, i, j, val);
                break;
            case BOOM_1:
            case BOOM_2:
            case BOOM_3:
            case BOOM_4:
            case BOOM_5:
            case BOOM_6:
            case BOOM_7:
            case BOOM_8:
            case BOOM_9:
            case BOOM_10:
            case BOOM_11:
            case BOOM_12:
            case BOOM_13:
            case BOOM_14:
            case BOOM_15:
            case BOOM_16:
            case BOOM_17:
            case BOOM_18:
            case BOOM_19:
            case BOOM_20:
            case BOOM_21:
            case BOOM_22:
            case BOOM_23:
            case BOOM_24:
            case BOOM_25:
            case BOOM_26:
            case BOOM_27:
            case BOOM_28:
            case BOOM_29:
                drawBoomStart(g, i, j, val);
                break;
            case ITEM_RECOVERY_1:
            case ITEM_SUPER_KICK:
            case ITEM_SUPER_EAT:
            case ITEM_NINJA:
            case ITEM_PROTECTED:
            case ITEM_6:
            case ITEM_7:
            case ITEM_8:
            case ITEM_9:
            case ITEM_10:
            case ITEM_11:
            case ITEM_12:
            case ITEM_13:
            case ITEM_14:
            case ITEM_15:
            case ITEM_16:
            case ITEM_17:
            case ITEM_18:
            case ITEM_19:
            case ITEM_RECOVERY_2:
            case ITEM_RECOVERY_3:
            case ITEM_MONEY_BAG:
            case ITEM_BAG_PLUS:
            case ITEM_BIG_BAG_PLUS:
            case ITEM_BOMB_DIRECTION:
            case ITEM_SPEED_UP:
                drawItem(g, i, j, val);
                break;
            default:
                break;
        }
    }

    public void reDrawPositionTimeLoad(Graphics g) {
        drawUnit(g, 9, 9, map[9][9]);
        drawUnit(g, 9, 10, map[9][10]);
        drawUnit(g, 10, 9, map[10][9]);
        drawUnit(g, 10, 10, map[10][10]);
    }

    private void drawHP(Graphics g, int x, int y, int hp) {
        g.setColor(new Color(15, 15, 15));
        g.fillRect(y * SIZE, x * SIZE, SIZE, HP_HEIGHT);//
        int len = (int) ((SIZE * hp) / MAX_HP);
        g.setColor(new Color(213, 51, 22));
        g.fillRect(y * SIZE, x * SIZE, len, HP_HEIGHT);//
        g.setColor(new Color(64, 64, 64));
        g.drawRect(y * SIZE, x * SIZE, len, HP_HEIGHT);
    }

    public void updateHpPlayer(Graphics g, int hp, int victim_val) {
        int x = mapcoordinate.get("x_" + victim_val);
        int y = mapcoordinate.get("y_" + victim_val);
        mapHP.put(victim_val, hp);
        if (victim_val == myValue) {
            boomFrame.updateHPTime(hp);
        }
        if (hp <= 0) {
            if (victim_val == myValue) {
                boomFrame.updateHPTime(0);
                boomFrame.updatePlayerDie();
                boomFrame.updateBombImage(mapImg.get("rip"));
                isBoomCharge = true;
                myTotalBoom = maxBoomCreate;
                //m_coin = g_coin * -1;
                boomFrame.updateCNTime(m_coin);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(1000);
                            boomFrame.updateBombCharge(0, 0);
                        } catch (InterruptedException ex) {
                        }
                    }
                }.start();
            }
        } else {
            drawHP(g, x, y, hp);
        }
    }

    private void drawMiniItem(Graphics g, int x, int y, int val) {
        int i = 0;
        int h = (int) (SIZE / 4);
        if (mapFlag.get(val).get("kb")) {
            i++;
            g.drawImage(mapImg.get("item_" + ITEM_SUPER_KICK), y * SIZE + SIZE - h, x * SIZE + HP_HEIGHT, h, h, null);
        } else if (mapFlag.get(val).get("eb")) {
            g.drawImage(mapImg.get("item_" + ITEM_SUPER_EAT), y * SIZE + SIZE - h, x * SIZE + HP_HEIGHT, h, h, null);
            i++;
        }
        if (mapFlag.get(val).get("pr")) {
            g.drawImage(mapImg.get("item_" + ITEM_PROTECTED), y * SIZE + SIZE - h - i * h, x * SIZE + HP_HEIGHT, h, h, null);
        }
    }

    private void drawStartPlayer(Graphics g, int cur_x, int cur_y, int cur_val) {
        //g.drawImage(mapImg.get("way"), cur_y * SIZE, cur_x * SIZE, SIZE, SIZE, null);
        drawWayStart(g, cur_x, cur_y);
        if (mapHP.get(cur_val) <= 0) {
            return;
        }
        mapcoordinate.put("x_" + cur_val, cur_x);
        mapcoordinate.put("y_" + cur_val, cur_y);
        if (cur_val == myValue) {
            myPX = cur_x;
            myPY = cur_y;
            g.drawImage(mapImg.get("player_" + mapStatus.get(cur_val)), cur_y * SIZE, cur_x * SIZE, SIZE, SIZE - HP_HEIGHT, null);
            drawHP(g, cur_x, cur_y, mapHP.get(cur_val));
            drawMiniItem(g, cur_x, cur_y, cur_val);
        } else {
            if (!mapFlag.get(cur_val).get("nj")) {
                g.drawImage(mapImg.get("enemy_" + mapStatus.get(cur_val)), cur_y * SIZE, cur_x * SIZE, SIZE, SIZE - HP_HEIGHT, null);
                drawHP(g, cur_x, cur_y, mapHP.get(cur_val));
                drawMiniItem(g, cur_x, cur_y, cur_val);
            }
        }

    }

    private void drawWayStart(Graphics g, int x, int y) {
        g.setColor(BACKGROUND);
        g.fillRect(y * SIZE, x * SIZE, SIZE, SIZE);
        g.drawImage(mapImg.get("way"), y * SIZE, x * SIZE, SIZE, SIZE, null);
    }

    private void drawBoomStart(Graphics g, int x, int y, int val) {
        //g.drawImage(mapImg.get("way"), y * SIZE, x * SIZE, SIZE, SIZE, null);
        int scale = (int) (SIZE / 8);
        drawWayStart(g, x, y);
        g.drawImage(mapImg.get("bomb_" + val), y * SIZE + scale, x * SIZE + scale, SIZE - 2 * scale, SIZE - 2 * scale, null);
    }

    private void drawWallStart(Graphics g, int x, int y) {
        drawWayStart(g, x, y);
        g.drawImage(mapImg.get("wall"), y * SIZE, x * SIZE, SIZE, SIZE, null);
    }

    private void drawBoxStart(Graphics g, int x, int y) {
        drawWayStart(g, x, y);
        g.drawImage(mapImg.get("box"), y * SIZE, x * SIZE, SIZE, SIZE, null);
    }

    private void drawItem(Graphics g, int x, int y, int val) {
        drawWayStart(g, x, y);
        if (isBombItem(val)) {
            int b_scale = (int) (SIZE / 4);
            int ef_scale = (int) (SIZE * 3 / 20);
            g.drawImage(mapImg.get("bomb_" + getBombItem(val)), y * SIZE + b_scale, x * SIZE + b_scale, SIZE - 2 * b_scale, SIZE - 2 * b_scale, null);
            g.drawImage(mapImg.get("item_bm_ef"), y * SIZE + ef_scale, x * SIZE + ef_scale, SIZE - 2 * ef_scale, SIZE - 2 * ef_scale, null);
        } else {
            int it_scale = (int) (SIZE * 3 / 20);
            int ef_scale = (int) (SIZE / 4);
            g.drawImage(mapImg.get("item_" + val), y * SIZE + it_scale, x * SIZE + it_scale, SIZE - 2 * it_scale, SIZE - 2 * it_scale, null);
            g.drawImage(mapImg.get("item_it"), y * SIZE + ef_scale, x * SIZE, SIZE - ef_scale, SIZE - ef_scale, null);
        }

    }

    public void createRandomItem(Graphics g, int x, int y, int val) {
        if (map[x][y] == WAY) {
            map[x][y] = val;
            drawItem(g, x, y, val);
        }
    }

    private void drawOther(Graphics g, int x, int y, int val) {
        int v = (x + y + val) % (OTHER_COUNT) + 1;
        Image img = mapImg.get("other_" + v);
        int img_width = img.getWidth(null);
        int img_height = img.getHeight(null);
        int sw, sh;
        if (img_width > img_height) {
            sw = SIZE;
            sh = (sw * img_height) / img_width;
        } else {
            sh = SIZE;
            sw = (sh * img_width) / img_height;
        }
        drawWayStart(g, x, y);
        g.drawImage(img, y * SIZE + (SIZE - sw) / 2, x * SIZE + (SIZE - sh) / 2, sw, sh, null);
    }

    public void addItemRandom(Graphics g, int x, int y, int val) {
        if (isItem(val) && map[x][y] == WAY) {
            map[x][y] = val;
            drawItem(g, x, y, val);
        }
    }

    private void drawBoomChange(Graphics g, int p_val, String type, int nx, int ny, int val) {
        if (type.equals("kick")) {
            if (nx != 0 && ny != 0) {
                map[nx][ny] = val;
                drawBoomStart(g, nx, ny, val);
            }
        } else if (type.equals("eat")) {
            if (mapCount.get(p_val).get("eb_count") + 1 >= EAT_BOOM_COUNT_DOWN) {
                mapFlag.get(p_val).put("eb", false);
                mapCount.get(p_val).put("eb_count", 0);
                if (myValue == p_val) {
                    boomFrame.updateEBTime(0);
                    boomFrame.updateEBImage(null);
                }
            } else {
                mapCount.get(p_val).put("eb_count", mapCount.get(p_val).get("eb_count") + 1);
                if (myValue == p_val) {
                    boomFrame.updateEBTime(EAT_BOOM_COUNT_DOWN - mapCount.get(p_val).get("eb_count"));
                }
            }
        }
    }

    private void calcPlayerGetItem(Graphics g, int p_val, int item) {
        switch (item) {
            case ITEM_RECOVERY_1://recover hp
            case ITEM_RECOVERY_2:
            case ITEM_RECOVERY_3:
                int hp = mapHP.get(p_val);
                switch (item) {
                    case ITEM_RECOVERY_1:
                        hp += (MAX_HP * RECOVERY_RAITO_1) / 100;
                        break;
                    case ITEM_RECOVERY_2:
                        hp += (MAX_HP * RECOVERY_RAITO_2) / 100;
                        break;
                    case ITEM_RECOVERY_3:
                        hp += (MAX_HP * RECOVERY_RAITO_3) / 100;
                        break;
                    default:
                        break;
                }
                if (hp > MAX_HP) {
                    hp = MAX_HP;
                }
                ClientFrame.getClientFrame().sendMessage(CommonDefine.BOOM_GAME_UPDATE_HP + CommonDefine.SEPARATOR_KEY_VALUE + hp);
                updateHpPlayer(g, hp, p_val);
                break;
            case ITEM_SUPER_KICK://kick bomb
                if (mapFlag.get(p_val).get("eb")) {
                    mapFlag.get(p_val).put("eb", false);
                    mapCount.get(p_val).put("eb_count", 0);
                    if (myValue == p_val) {
                        boomFrame.updateEBImage(null);
                        boomFrame.updateEBTime(0);
                    }
                }
                if (mapFlag.get(p_val).get("kb")) {
                    mapCount.get(p_val).put("kb_count", 0);
                } else {
                    mapFlag.get(p_val).put("kb", true);
                    if (myValue == p_val) {
                        boomFrame.updateKBImage(mapImg.get("item_" + item));
                    }
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                while (mapCount.get(p_val).get("kb_count") < KICK_BOOM_COUNT_DOWN && boomFrame.isStartGame() && mapFlag.get(p_val).get("kb")) {

                                    if (myValue == p_val) {
                                        boomFrame.updateKBTime(KICK_BOOM_COUNT_DOWN - mapCount.get(p_val).get("kb_count"));
                                    }
                                    sleep(1000);//sleep 1 second
                                    mapCount.get(p_val).put("kb_count", mapCount.get(p_val).get("kb_count") + 1);
                                }
                                mapFlag.get(p_val).put("kb", false);
                                mapCount.get(p_val).put("kb_count", 0);
                                if (myValue == p_val) {
                                    boomFrame.updateKBTime(0);
                                    boomFrame.updateKBImage(null);
                                }
                            } catch (InterruptedException ex) {
                            }
                        }
                    }.start();
                }
                break;
            case ITEM_SUPER_EAT://eat bomb
                if (mapFlag.get(p_val).get("kb")) {
                    mapFlag.get(p_val).put("kb", false);
                    mapCount.get(p_val).put("kb_count", 0);
                    if (myValue == p_val) {
                        boomFrame.updateKBImage(null);
                        boomFrame.updateKBTime(0);
                    }
                }
                if (mapFlag.get(p_val).get("eb")) {
                    mapCount.get(p_val).put("eb_count", 0);
                    if (myValue == p_val) {
                        boomFrame.updateEBTime(EAT_BOOM_COUNT_DOWN);
                    }
                } else {
                    mapFlag.get(p_val).put("eb", true);
                    if (myValue == p_val) {
                        boomFrame.updateEBImage(mapImg.get("item_" + item));
                        boomFrame.updateEBTime(EAT_BOOM_COUNT_DOWN);
                    }
                }
                break;
            case ITEM_NINJA://invisible
                if (mapFlag.get(p_val).get("nj")) {
                    mapCount.get(p_val).put("nj_count", 0);
                } else {
                    mapFlag.get(p_val).put("nj", true);
                    if (myValue == p_val) {
                        boomFrame.updateNJImage(mapImg.get("item_" + item));
                    }
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                while (mapCount.get(p_val).get("nj_count") < NINJA_TIME_COUNT_DOWN && boomFrame.isStartGame() && mapFlag.get(p_val).get("nj")) {
                                    //display time here
                                    if (myValue == p_val) {
                                        boomFrame.updateNJTime(NINJA_TIME_COUNT_DOWN - mapCount.get(p_val).get("nj_count"));
                                    }
                                    sleep(1000);//sleep 1 second
                                    mapCount.get(p_val).put("nj_count", mapCount.get(p_val).get("nj_count") + 1);
                                }
                                mapFlag.get(p_val).put("nj", false);
                                mapCount.get(p_val).put("nj_count", 0);
                                if (myValue == p_val) {
                                    boomFrame.updateNJTime(0);
                                    boomFrame.updateNJImage(null);
                                }
                            } catch (InterruptedException ex) {
                            }
                        }
                    }.start();
                }
                break;
            case ITEM_PROTECTED://protect
                if (mapFlag.get(p_val).get("pr")) {
                    mapCount.get(p_val).put("pr_count", 0);
                } else {
                    mapFlag.get(p_val).put("pr", true);
                    if (myValue == p_val) {
                        boomFrame.updatePRImage(mapImg.get("item_" + item));
                    }
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                while (mapCount.get(p_val).get("pr_count") < PROTECT_TIME_COUNT_DOWN && boomFrame.isStartGame() && mapFlag.get(p_val).get("pr")) {
                                    //display time here
                                    if (myValue == p_val) {
                                        boomFrame.updatePRTime(PROTECT_TIME_COUNT_DOWN - mapCount.get(p_val).get("pr_count"));
                                    }
                                    sleep(1000);//sleep 1 second
                                    mapCount.get(p_val).put("pr_count", mapCount.get(p_val).get("pr_count") + 1);
                                }
                                mapFlag.get(p_val).put("pr", false);
                                mapCount.get(p_val).put("pr_count", 0);
                                if (myValue == p_val) {
                                    boomFrame.updatePRTime(0);
                                    boomFrame.updatePRImage(null);
                                }
                                ClientFrame.getClientFrame().sendMessage(CommonDefine.BOOM_GAME_REMOVE_PR);
                            } catch (InterruptedException ex) {
                            }
                        }
                    }.start();
                }
                break;
            case ITEM_6:
            case ITEM_7:
            case ITEM_8:
            case ITEM_9:
            case ITEM_10:
            case ITEM_11:
            case ITEM_12:
            case ITEM_13:
            case ITEM_14:
            case ITEM_15:
            case ITEM_16:
            case ITEM_17:
            case ITEM_18:
            case ITEM_19:
                if (p_val == myValue) {
                    myBoomValue = getBombItem(item);
                    boomFrame.updateBombImage(mapImg.get("bomb_" + myBoomValue));
                }
                break;
            case ITEM_MONEY_BAG:
                if (myValue == p_val) {
                    if (totalCoin != 0) {
                        m_coin += (int) ((totalCoin / 2) * luckyRatio);
                    } else {
                        m_coin += (MONEY_BAG_PARAM * luckyRatio);
                    }
                    boomFrame.updateCNTime(m_coin);
                }
                break;
            case ITEM_BAG_PLUS:
                if (!hasBagPlus) {
                    if (myTotalBoom == maxBoomCreate) {
                        boomFrame.updateBombCharge(myTotalBoom + BAG_PLUS_PARAM, 0);
                    }
                    maxBoomCreate += BAG_PLUS_PARAM;
                    hasBagPlus = true;
                }
                break;
            case ITEM_BIG_BAG_PLUS:
                if (!hasBigBagPlus) {
                    if (myTotalBoom == maxBoomCreate) {
                        boomFrame.updateBombCharge(myTotalBoom + BIG_BAG_PLUS_PARAM, 0);
                    }
                    maxBoomCreate += BIG_BAG_PLUS_PARAM;
                    hasBigBagPlus = true;
                }
                break;
            case ITEM_BOMB_DIRECTION:
                hasBombDirection = true;
                break;
            case ITEM_SPEED_UP:
                if (myValue == p_val) {
                    speed = SPEED_UP_PARAM;
                    boomFrame.updateSpeedImage(mapImg.get("item_" + ITEM_SPEED_UP));
                    boomFrame.updateSpeedTime(SPEED_UP_TIME_PARAM);
                    new Thread(() -> {
                        try {
                            for (int i = 1; i <= SPEED_UP_TIME_PARAM; i++) {
                                sleep(1000);
                                boomFrame.updateSpeedTime(SPEED_UP_TIME_PARAM - i);
                            }
                        } catch (InterruptedException ex) {
                        } finally {
                            speed = 1;
                            boomFrame.updateSpeedImage(null);
                            boomFrame.updateSpeedTime(0);
                        }
                    }).start();
                }
                break;
            default:
                break;
        }
    }

    public void drawPlayerMove(Graphics g, int next_x, int next_y, int next_val, int status, int it, String type, int nx, int ny) {
        if (isBomb(map[next_x][next_y])) {
            drawBoomChange(g, next_val, type, nx, ny, map[next_x][next_y]);
        }
        if (it != 0) {
            calcPlayerGetItem(g, next_val, it);
        }

        if (status != mapStatus.get(next_val)) {
            mapStatus.put(next_val, status);
            drawStartPlayer(g, next_x, next_y, next_val);
            boomFrame.enableAction();
        } else {
            int pre_x = -1;
            int pre_y = -1;
            switch (status) {
                case STATUS_UP:
                    pre_x = next_x + 1;
                    pre_y = next_y;
                    break;
                case STATUS_RIGHT:
                    pre_x = next_x;
                    pre_y = next_y - 1;
                    break;
                case STATUS_DOWN:
                    pre_x = next_x - 1;
                    pre_y = next_y;
                    break;
                case STATUS_LEFT:
                    pre_x = next_x;
                    pre_y = next_y + 1;
                    break;
                default:
                    break;
            }
            if (pre_x != -1 && pre_y != -1) {
                map[pre_x][pre_y] = SHADOW;
                map[next_x][next_y] = next_val;
                mapcoordinate.put("x_" + next_val, next_x);
                mapcoordinate.put("y_" + next_val, next_y);
                if (next_val == myValue) {
                    myPX = next_x;
                    myPY = next_y;
                }
                if (mapHP.get(next_val) > 0) {
                    mapStatus.put(next_val, status);
                    drawAnimatePlayerMove(g, pre_x, pre_y, next_x, next_y, next_val, status);
                }
            }
        }
    }

    private void drawHpAnimate(Graphics g, int x, int y, int hp) {
        g.setColor(new Color(15, 15, 15));
        g.fillRect(y, x, SIZE - 2, HP_HEIGHT);//
        int len = (int) (((SIZE - 2) * hp) / MAX_HP);
        g.setColor(new Color(213, 51, 22));
        g.fillRect(y, x, len, HP_HEIGHT);//
        g.setColor(new Color(64, 64, 64));
        g.drawRect(y, x, len, HP_HEIGHT);
    }

    private void drawMiniItemAnimate(Graphics g, int x, int y, int val) {
        int i = 0;
        int h = (int) (SIZE / 4);
        if (mapFlag.get(val).get("kb")) {
            i++;
            g.drawImage(mapImg.get("item_" + ITEM_SUPER_KICK), y + SIZE - h, x + HP_HEIGHT, h, h, null);
        } else if (mapFlag.get(val).get("eb")) {
            g.drawImage(mapImg.get("item_" + ITEM_SUPER_EAT), y + SIZE - h, x + HP_HEIGHT, h, h, null);
            i++;
        }
        if (mapFlag.get(val).get("pr")) {
            g.drawImage(mapImg.get("item_" + ITEM_PROTECTED), y + SIZE - h - i * h, x + HP_HEIGHT, h, h, null);
        }
    }

    public boolean isAnimate = false;

    public boolean isAnimatePlayer() {
        return isAnimate;
    }

    private void drawAnimatePlayerMove(Graphics g, int pre_x, int pre_y, int next_x, int next_y, int val, int status) {
        new Thread(() -> {
            isAnimate = true;
            int distance = (int) SIZE / 3 + 1;
            int count = 0;
            long time = 100 - 10 * (speed - 1) - 10 * speed;
            try {
                while (count < SIZE) {
                    count += (distance * speed);
                    if (count > SIZE) {
                        count = SIZE;
                    }
                    drawWayStart(g, pre_x, pre_y);
                    drawWayStart(g, next_x, next_y);
                    int cur_x = pre_x * SIZE + (next_x - pre_x) * count;
                    int cur_y = pre_y * SIZE + (next_y - pre_y) * count;
                    if (val == myValue || !mapFlag.get(val).get("nj")) {
                        g.drawImage(mapImg.get("player_" + status), cur_y, cur_x, SIZE, SIZE - HP_HEIGHT, null);//
                        drawHpAnimate(g, cur_x, cur_y, mapHP.get(val));
                        drawMiniItemAnimate(g, cur_x, cur_y, val);
                    }
                    if(count >= SIZE){
                        break;
                    }
                    sleep(time);
                }
            } catch (InterruptedException ex) {
            }finally{
                map[pre_x][pre_y] = WAY;
                if (myValue == val) {
                    boomFrame.enableAction();
                    isAnimate = false;
                }
            }
        }).start();
    }

    public void movePlayer(int status) {
        int next_x = myPX;
        int next_y = myPY;
        boolean flag = true;
        String type = "type";
        switch (status) {
            case STATUS_UP:
                if (status == mapStatus.get(myValue)) {
                    if (map[next_x - 1][next_y] == WAY || isItem(map[next_x - 1][next_y]) || ((mapFlag.get(myValue).get("kb") || mapFlag.get(myValue).get("eb")) && isBomb(map[next_x - 1][next_y]))) {
                        if (isBomb(map[next_x - 1][next_y]) && mapFlag.get(myValue).get("kb")) {
                            if (map[next_x - 2][next_y] == WAY) {
                                type = "kick";
                            } else {
                                flag = false;
                            }
                        } else if (isBomb(map[next_x - 1][next_y]) && mapFlag.get(myValue).get("eb")) {
                            type = "eat";
                        }
                        --next_x;
                    } else {
                        flag = false;
                    }
                }
                break;
            case STATUS_RIGHT:
                if (status == mapStatus.get(myValue)) {
                    if (map[next_x][next_y + 1] == WAY || isItem(map[next_x][next_y + 1]) || ((mapFlag.get(myValue).get("kb") || mapFlag.get(myValue).get("eb")) && isBomb(map[next_x][next_y + 1]))) {
                        if (isBomb(map[next_x][next_y + 1]) && mapFlag.get(myValue).get("kb")) {
                            if (map[next_x][next_y + 2] == WAY) {
                                type = "kick";
                            } else {
                                flag = false;
                            }
                        } else if (isBomb(map[next_x][next_y + 1]) && mapFlag.get(myValue).get("eb")) {
                            type = "eat";
                        }
                        ++next_y;
                    } else {
                        flag = false;
                    }
                }
                break;
            case STATUS_DOWN:
                if (status == mapStatus.get(myValue)) {
                    if (map[next_x + 1][next_y] == WAY || isItem(map[next_x + 1][next_y]) || ((mapFlag.get(myValue).get("kb") || mapFlag.get(myValue).get("eb")) && isBomb(map[next_x + 1][next_y]))) {
                        if (isBomb(map[next_x + 1][next_y]) && mapFlag.get(myValue).get("kb")) {
                            if (map[next_x + 2][next_y] == WAY) {
                                type = "kick";
                            } else {
                                flag = false;
                            }
                        } else if (isBomb(map[next_x + 1][next_y]) && mapFlag.get(myValue).get("eb")) {
                            type = "eat";
                        }
                        ++next_x;
                    } else {
                        flag = false;
                    }
                }
                break;
            case STATUS_LEFT:
                if (status == mapStatus.get(myValue)) {
                    if (map[next_x][next_y - 1] == WAY || isItem(map[next_x][next_y - 1]) || ((mapFlag.get(myValue).get("kb") || mapFlag.get(myValue).get("eb")) && isBomb(map[next_x][next_y - 1]))) {
                        if (isBomb(map[next_x][next_y - 1]) && mapFlag.get(myValue).get("kb")) {
                            if (map[next_x][next_y - 2] == WAY) {
                                type = "kick";
                            } else {
                                flag = false;
                            }
                        } else if (isBomb(map[next_x][next_y - 1]) && mapFlag.get(myValue).get("eb")) {
                            type = "eat";
                        }
                        --next_y;
                    } else {
                        flag = false;
                    }
                }
                break;
            default:
                break;
        }
        if (flag) {
            //send mess to server
            boomFrame.disableAction();
            String mess = CommonDefine.BOOM_GAME_PLAYER_MOVE + CommonDefine.SEPARATOR_KEY_VALUE + next_x + CommonDefine.SEPARATOR_KEY_VALUE + next_y + CommonDefine.SEPARATOR_KEY_VALUE + myValue + CommonDefine.SEPARATOR_KEY_VALUE + status + CommonDefine.SEPARATOR_KEY_VALUE + type;
            ClientFrame.getClientFrame().sendMessage(mess);//send x, y, value, status,
        }
    }

    public void createBoom() {
        if (myTotalBoom <= 0) {
            return;
        }
        int boom_x = myPX;
        int boom_y = myPY;
        boolean flag = true;
        int status = mapStatus.get(myValue);
        if (backBombDirection) {
            switch (status) {
                case STATUS_UP:
                    status = STATUS_DOWN;
                    break;
                case STATUS_DOWN:
                    status = STATUS_UP;
                    break;
                case STATUS_LEFT:
                    status = STATUS_RIGHT;
                    break;
                case STATUS_RIGHT:
                    status = STATUS_LEFT;
                    break;
                default:
                    break;
            }
        }
        switch (status) {
            case STATUS_UP:
                if (map[boom_x - 1][boom_y] == WAY) {
                    --boom_x;
                } else {
                    flag = false;
                }
                break;
            case STATUS_RIGHT:
                if (map[boom_x][boom_y + 1] == WAY) {
                    ++boom_y;
                } else {
                    flag = false;
                }
                break;
            case STATUS_DOWN:
                if (map[boom_x + 1][boom_y] == WAY) {
                    ++boom_x;
                } else {
                    flag = false;
                }
                break;
            case STATUS_LEFT:
                if (map[boom_x][boom_y - 1] == WAY) {
                    --boom_y;
                } else {
                    flag = false;
                }
                break;
            default:
                flag = false;
                break;
        }
        if (flag) {
            //send mess to server
            ClientFrame.getClientFrame().sendMessage(CommonDefine.BOOM_GAME_ADD_BOOM + CommonDefine.SEPARATOR_KEY_VALUE + boom_x + CommonDefine.SEPARATOR_KEY_VALUE + boom_y + CommonDefine.SEPARATOR_KEY_VALUE + myBoomValue + CommonDefine.SEPARATOR_KEY_VALUE + myValue);//send x, y, value, status, item
        }
    }

    public void drawBoomCreate(Graphics g, int boomValue, int x, int y, int m_val) {
        map[x][y] = boomValue;
        drawBoomStart(g, x, y, boomValue);
        if (m_val == myValue) {
            --myTotalBoom;
            // thread boom charge
            if (!isBoomCharge) {
                isBoomCharge = true;
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            while (myTotalBoom < maxBoomCreate && boomFrame.isStartGame()) {
                                for (int i = timeCharge; i >= 1; i--) {
                                    boomFrame.updateBombCharge(myTotalBoom, i);
                                    sleep(1000);
                                }
                                ++myTotalBoom;
                            }
                            isBoomCharge = false;
                            boomFrame.updateBombCharge(myTotalBoom, 0);
                        } catch (InterruptedException ex) {
                        }
                    }
                }.start();
            }
        }
    }

    public void drawBoomExplode(Graphics g, String line) {
        for (String elem : line.split(CommonDefine.COMMA)) {
            String[] arrValue = elem.split(CommonDefine.SEPARATOR_KEY_VALUE);
            int x = Integer.parseInt(arrValue[0]);
            int y = Integer.parseInt(arrValue[1]);
            if (isItem(map[x][y]) || isBomb(map[x][y])) {
                map[x][y] = WAY;
            }
            new Thread() {
                @Override
                public void run() {
                    for (int i = 1; i <= 2; i++) {
                        try {
                            g.drawImage(mapImg.get("fire_" + i), y * SIZE, x * SIZE, SIZE, SIZE, null);
                            sleep(100);
                        } catch (InterruptedException ex) {
                        }
                    }
                    drawWayStart(g, x, y);
                    if (isPlayer(map[x][y])) {
                        drawStartPlayer(g, x, y, map[x][y]);
                        if (mapHP.get(map[x][y]) <= 0) {
                            if (arrValue.length == 3) {
                                int val = Integer.parseInt(arrValue[2]);
                                if (isItem(val)) {
                                    map[x][y] = val;
                                    drawItem(g, x, y, val);
                                } else {
                                    map[x][y] = WAY;
                                }
                            }
                        }
                    } else if (map[x][y] == BOX) {
                        if (arrValue.length == 3) {
                            int val = Integer.parseInt(arrValue[2]);
                            if (isItem(val)) {
                                map[x][y] = val;
                                drawItem(g, x, y, val);
                            } else {
                                map[x][y] = WAY;
                            }
                        }
                    } else if (isBomb(map[x][y])) {
                        drawBoomStart(g, x, y, map[x][y]);
                    } else if (isItem(map[x][y])) {
                        drawItem(g, x, y, map[x][y]);
                    } else if (map[x][y] == SHADOW) {
                        drawWayStart(g, x, y);
                    }
                }
            }.start();
        }

    }

    public void drawPlayerJoin(Graphics g, List<String> players) {
        g.setColor(COLOR_BACK_1);
        g.fillRect(0, 0, CommonDefine.MAIN_GAME_WIDTH, CommonDefine.MAIN_GAME_HEIGHT);
        final int img_size = 100;
        final int scale = 15;
        int img_sx = (CommonDefine.MAIN_GAME_WIDTH - (players.size() * (100 + scale) - scale)) / 2;
        int img_y = 50;
        int index = 0;
        for (String name : players) {
            //
            int img_x = (img_size + scale) * index + img_sx;
            Graphics2D gd = (Graphics2D) g.create();
            gd.setStroke(new BasicStroke(2));
            gd.setColor(new Color(255, 247, 0));
            gd.drawRect(img_x, img_y, img_size, img_size);
            g.drawImage(mapImg.get("super-man"), img_x, img_y, img_size, img_size, null);
            //
            g.setColor(Color.BLACK);
            Font f = new Font("Segoe UI Semibold", Font.BOLD, 13);
            g.setFont(f);
            int name_w = g.getFontMetrics(f).stringWidth(name);
            int name_h = g.getFontMetrics(f).getHeight();
            //
            int name_x = img_x + img_size / 2 - name_w / 2;
            int name_y = img_y + img_size + name_h;
            g.drawString(name, name_x, name_y);
            //
            ++index;
        }
    }

    public void drawPlayerExit(Graphics g, int val) {
        int x = mapcoordinate.get("x_" + val);
        int y = mapcoordinate.get("y_" + val);
        map[x][y] = WAY;
        drawWayStart(g, x, y);
//        mapCount.remove(val);
//        mapFlag.remove(val);
//        mapHP.remove(val);
//        mapStatus.remove(val);
    }

    public void drawReadyIcon(Graphics g) {
        g.setColor(COLOR_BACK_1);
        g.fillRect(READY_X, READY_Y, READY_SIZE, READY_SIZE);
        Font f = new Font("Segoe UI Semibold", Font.BOLD, 15);
        g.setFont(f);
        int text_h = g.getFontMetrics(f).getHeight();
        int text_x = READY_X + READY_SIZE + 5;
        int text_y = READY_Y + READY_SIZE / 2 + text_h / 3;
        if (!isReady) {
            g.drawImage(mapImg.get("not-ready"), READY_X, READY_Y, READY_SIZE, READY_SIZE, null);
            g.setColor(new Color(207, 207, 207));
            g.drawString(READY_TEXY, text_x, text_y);
        } else {
            g.drawImage(mapImg.get("ready"), READY_X, READY_Y, READY_SIZE, READY_SIZE, null);
            g.setColor(new Color(43, 202, 24));
            g.drawString(READY_TEXY, text_x, text_y);
        }
    }

    public void drawSettingIcon(Graphics g) {
        g.setColor(COLOR_BACK_1);
        g.fillRect(SETTING_X, SETTING_Y, SETTING_SIZE, SETTING_SIZE);
        Font f = new Font("Segoe UI Semibold", Font.BOLD, 15);
        g.setFont(f);
        g.setColor(new Color(78, 233, 246));
        int text_h = g.getFontMetrics(f).getHeight();
        int text_x = SETTING_X + SETTING_SIZE + 5;
        int text_y = SETTING_Y + SETTING_SIZE / 2 + text_h / 3;
        g.drawString(SETTING_TEXT, text_x, text_y);
        g.drawImage(mapImg.get("setting"), SETTING_X, SETTING_Y, SETTING_SIZE, SETTING_SIZE, null);
    }

    public void drawGameCoin(Graphics g, String gCoin) {
        g.setColor(COLOR_BACK_1);
        g.fillRect(READY_X + 400, READY_Y, READY_SIZE + 70, READY_SIZE);
        Font f = new Font("Segoe UI Semibold", Font.BOLD, 15);
        g.setFont(f);
        int text_h = g.getFontMetrics(f).getHeight();
        int text_x = READY_X + READY_SIZE + 5 + 400;
        int text_y = READY_Y + READY_SIZE / 2 + text_h / 3;
        g.drawImage(mapImg.get("coin"), READY_X + 400, READY_Y, READY_SIZE, READY_SIZE, null);
        g.setColor(new Color(43, 202, 24));
        g.drawString(gCoin, text_x, text_y);
    }

    public void drawRewardCoin(Graphics g, String rCoin) {
        g.setColor(COLOR_BACK_1);
        g.fillRect(SETTING_X + 400, SETTING_Y, SETTING_SIZE + 70, SETTING_SIZE);
        Font f = new Font("Segoe UI Semibold", Font.BOLD, 15);
        g.setFont(f);
        int text_h = g.getFontMetrics(f).getHeight();
        int text_x = SETTING_X + SETTING_SIZE + 5 + 400;
        int text_y = SETTING_Y + SETTING_SIZE / 2 + text_h / 3;
        g.drawImage(mapImg.get("item_" + ITEM_MONEY_BAG), SETTING_X + 400, SETTING_Y, SETTING_SIZE, SETTING_SIZE, null);
        g.setColor(new Color(43, 202, 24));
        g.drawString(rCoin, text_x, text_y);
    }

    public void drawFightingIcon(Graphics g) {
        g.setColor(COLOR_BACK_1);
        g.fillRect(FIGHT_X, FIGHT_Y, FIGHT_WIDHT, FIGHT_HEIGHT);
        g.drawImage(mapImg.get("fight"), FIGHT_X, FIGHT_Y, FIGHT_WIDHT, FIGHT_HEIGHT, null);
    }

    public static void rePaintMainGame(Graphics g) {
        g.setColor(CommonDefine.MAIN_GAME_BOARD_BACKGROUND);
        g.fillRect(0, 0, CommonDefine.MAIN_GAME_WIDTH, CommonDefine.MAIN_GAME_HEIGHT);
    }

    public void drawLoadingBackground(Graphics g, String time) {
        if (time == null) {
            drawMainGame(g);
            return;
        }
        reDrawPositionTimeLoad(g);
        String key = "time_load_" + time;
        Image img = mapImg.get(key);
        g.drawImage(img, 9 * SIZE, 9 * SIZE, SIZE * 2, SIZE * 2, null);
    }

    private boolean isPlayer(int value) {
        return (value >= PLAYER_1 && value <= PLAYER_5);
    }

    private boolean isItem(int value) {
        return (value >= ITEM_RECOVERY_1 && value <= ITEM_MAX_VALUE);
    }

    public static boolean isBomb(int value) {
        return (value >= BOOM_1 && value <= BOOM_29);
    }

    public static boolean isBombItem(int value) {
        return (value >= ITEM_6 && value <= ITEM_19);
    }

    public static int getBombItem(int val) {
        return (val - 33);
    }
    
}
