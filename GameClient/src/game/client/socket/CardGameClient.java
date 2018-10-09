/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.client.socket;

import game.client.common.CommonDefine;
import game.client.element.Card;
import game.client.element.CardType;
import game.client.element.Player;
import game.client.gui.ClientFrame;
import game.client.func.CardFunc;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author tkv-nhukhanhdinh
 */
public class CardGameClient {

    private static final int MAX_PLAYER = 4;
    private static final int GAME_HEIGHT = 600;
    private static final int GAME_WIDTH = 900;
    private static final int CARD_P_HEIGHT = 150;
    private static final int CARD_P_WIDTH = 120;
    private static final int CARD_F_HEIGHT = 100;
    private static final int CARD_F_WIDTH = 80;
    private static final int CARD_E_HEIGHT = 60;
    private static final int CARD_E_WIDTH = 40;
    private static final int PLAYER_SIZE = 40;

    private static final int CARD_PX_SCALE = CARD_P_WIDTH / 4;
    private static final int CARD_FX_SCALE = CARD_F_WIDTH / 4;
    private static final int CARD_EX_SCALE = CARD_E_WIDTH / 4;

    private static final int BUTTON_HEIGHT = 30;
    private static final int BUTTON_WIDTH = 50;
    private static final int BUTTON_X_START = 780;
    private static final int BUTTON_Y_START = 535;
    private static final int BUTTON_X_SCALE = 10;
    private static final int BUTTON_PLAY_WIDTH = 201;
    private static final int BUTTON_PLAY_HEIGHT = 48;

    private static final int IMG_COUNT_DOWN_X_START = 680;
    private static final int IMG_COUNT_DOWN_Y_START = 535;
    private static final int IMG_COUNT_DOWN_WIDTH = 50;
    private static final int IMG_COUNT_DOWN_HEIGHT = 28;

    private static final Color BACKGROUND_COLOR = new Color(102, 102, 102);
    private static final Color CARD_HIDE_COLOR = new Color(0, 0, 0, 127);
    private static final Color PLAYER_INFO_COLOR = new Color(40, 180, 99);

    private static final int LIMIT_CARD = 13;

    private List<Card> listOwnCard;
    private List<List<Card>> listCardDeck;
    private Map<String, Image> listImageCard;
    private List<Player> listPlayer;

    ////////reset when next turn
    private List<Card> listPreviousFight = null;
    private int preValueList = 0;
    //////////////
    private int myValue;
    private int widthOfList;
    private int xStartOfList;
    private int yStartOfList;
    private int currentVal;
    private int gameType;
    private boolean enable;

    private Graphics g;
    private JPanel panel_uer;
    private JLabel label_name;
    private JLabel label_coin;

    public CardGameClient(Graphics g, JPanel panel_uer, JLabel label_name, JLabel label_coin) {
        this.g = g;
        this.panel_uer = panel_uer;
        this.label_name = label_name;
        this.label_coin = label_coin;
    }

    //test
    public CardGameClient(Graphics g) {
        this.g = g;
    }

    public void initListImage(Map<String, Image> listImage) {
        this.listImageCard = listImage;
    }

    public void initMyValue(int myValue) {
        this.myValue = myValue;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public void initTEST() {
        listOwnCard = new ArrayList<>();
        listPlayer = new ArrayList<>();
        listOwnCard.add(new Card(1, CardType.HEART));
        listOwnCard.add(new Card(2, CardType.SPADE));
        listOwnCard.add(new Card(3, CardType.DIAMOND));
        listOwnCard.add(new Card(4, CardType.HEART));
        listOwnCard.add(new Card(5, CardType.SPADE));
        listOwnCard.add(new Card(6, CardType.SPADE));
        listOwnCard.add(new Card(7, CardType.SPADE));
        listOwnCard.add(new Card(8, CardType.SPADE));
        listOwnCard.add(new Card(10, CardType.CLUB));
        //listOwnCard.add(new Card(11, CardType.SPADE));
        //listOwnCard.add(new Card(12, CardType.SPADE));
        listOwnCard.add(new Card(13, CardType.HEART));
        listOwnCard.add(new Card(9, CardType.SPADE));

        widthOfList = CARD_PX_SCALE * (listOwnCard.size() - 1) + CARD_P_WIDTH;
        xStartOfList = (BUTTON_X_START - widthOfList) / 2;
        yStartOfList = GAME_HEIGHT - CARD_P_HEIGHT + CARD_PX_SCALE;
        System.out.println("W of list:" + widthOfList);
        System.out.println("X of list:" + xStartOfList);
        System.out.println("y of list:" + yStartOfList);

        listPlayer.add(new Player(1, "player__0_0_0_1", listOwnCard.size(), 1000));
        listPlayer.add(new Player(2, "player_2", listOwnCard.size(), 5000));
        listPlayer.add(new Player(3, "player_3", listOwnCard.size(), 300000));
        listPlayer.add(new Player(4, "player_4", listOwnCard.size(), 2000));
        myValue = 2;
        drawPlayersInfo(g);

    }

    public void repaintGame(Graphics g) {
        drawPlayersInfo(g);
        drawPreviousListCard(g);
    }

    public void initListOwnCardDeck(Graphics g, String data) {
        listOwnCard = new ArrayList<>();
        String[] cardDatas = data.split(CommonDefine.COMMA);
        for (String strCard : cardDatas) {
            String[] elements = strCard.split("_");
            if (elements.length == 2) {
                int num = Integer.parseInt(elements[0].trim());
                CardType type = CardType.valueOf(Integer.parseInt(elements[1].trim()));
                if (type != CardType.INVALID) {
                    Card card = new Card(num, type);
                    listOwnCard.add(card);
                }
            }
        }
        if (LIMIT_CARD != listOwnCard.size()) {
            System.out.println("Card is missing! list card size: " + listOwnCard.size());
        }
        for (Player p : listPlayer) {
            p.setCardNums(LIMIT_CARD);
        }
        CardFunc.sortCardByNumAndType(listOwnCard);
        widthOfList = CARD_PX_SCALE * (listOwnCard.size() - 1) + CARD_P_WIDTH;
        xStartOfList = (BUTTON_X_START - widthOfList) / 2;
        yStartOfList = GAME_HEIGHT - CARD_P_HEIGHT + CARD_PX_SCALE;
        System.out.println("W of list:" + widthOfList);
        System.out.println("X of list:" + xStartOfList);
        System.out.println("y of list:" + yStartOfList);
        drawPlayersInfo(g);
    }

    public void initPlayer(Graphics g, List<Player> listPlayer) {
        this.listPlayer = listPlayer;
        drawPlayersInfo(g);
    }

    public void addPlayer(Graphics g, Player player) {
        listPlayer.add(player);
        drawEnemyInfo(g, player);
        drawEnemyListCard(g, player);
    }

    public void drawPlayersInfo(Graphics g) {
        if (listPlayer == null || listPlayer.isEmpty()) {
            return;
        }
        for (Player p : listPlayer) {
            if (p.getValue() != myValue) {
                drawEnemyInfo(g, p);
                drawEnemyListCard(g, p);
            } else {
                drawOwnInfomation(g, p);
                drawListOwnCard(g);
            }
        }
    }

    public void drawOwnInfomation(Graphics g, Player p) {
        Image img = listImageCard.get("player");
        panel_uer.getGraphics().drawImage(img, 0, 0, panel_uer.getWidth(), panel_uer.getHeight(), null);
        label_name.setText("Username: " + p.getName());
        label_coin.setText("Coin: " + p.getCoin());
    }

    public void drawEnemyInfo(Graphics g, Player p) {
        Image img = listImageCard.get("player");
        Font f = new Font("Tahoma", Font.BOLD, 13);
        int n_w = g.getFontMetrics(f).stringWidth(p.getName());
        int c_w = g.getFontMetrics(f).stringWidth(String.valueOf(p.getCoin()));
        int str_w = n_w > c_w ? n_w : c_w;
        int str_h = g.getFontMetrics(f).getHeight();
        int x_img, y_img, x_name, y_name;
        if (isNextValue(p.getValue(), myValue)) {
            x_img = (GAME_WIDTH - PLAYER_SIZE - PLAYER_SIZE / 4 - str_w);
            y_img = (GAME_HEIGHT / 2 - CARD_E_HEIGHT / 2 - PLAYER_SIZE / 2);
        } else if (isPreviousValue(p.getValue(), myValue)) {
            x_img = 0;
            y_img = (GAME_HEIGHT / 2 - CARD_E_HEIGHT / 2 - PLAYER_SIZE / 2);
        } else {
            x_img = (GAME_WIDTH - (PLAYER_SIZE + PLAYER_SIZE / 4 + str_w)) / 2;
            y_img = 0;
        }
        y_name = y_img + (PLAYER_SIZE - str_h * 2 - str_h / 4) / 2;
        x_name = x_img + PLAYER_SIZE + PLAYER_SIZE / 4;
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(x_img, y_img, PLAYER_SIZE, PLAYER_SIZE);
        g.drawImage(img, x_img, y_img, PLAYER_SIZE, PLAYER_SIZE, null);
        g.setFont(f);
        g.setColor(PLAYER_INFO_COLOR);
        g.drawString(p.getName(), x_name, y_name + str_h);
        g.drawString(String.valueOf(p.getCoin()), x_name, y_name + 2 * str_h);

    }

    public void drawEnemyListCard(Graphics g, Player p) {
        Image img = listImageCard.get("card_back");
        int x, y, remove_x;
        if (isNextValue(p.getValue(), myValue)) {
            x = (GAME_WIDTH - (CARD_EX_SCALE * (p.getCardNums() - 1) + CARD_E_WIDTH));
            y = (GAME_HEIGHT / 2 - CARD_E_HEIGHT / 2 + PLAYER_SIZE / 2);
            remove_x = (GAME_WIDTH - (CARD_EX_SCALE * (LIMIT_CARD - 1) + CARD_E_WIDTH));
        } else if (isPreviousValue(p.getValue(), myValue)) {
            x = 0;
            y = (GAME_HEIGHT / 2 - CARD_E_HEIGHT / 2 + PLAYER_SIZE / 2);
            remove_x = 0;
        } else {
            x = (GAME_WIDTH - (CARD_EX_SCALE * (p.getCardNums() - 1) + CARD_E_WIDTH)) / 2;
            y = PLAYER_SIZE;
            remove_x = (GAME_WIDTH - (CARD_EX_SCALE * (LIMIT_CARD - 1) + CARD_E_WIDTH)) / 2;
        }
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(remove_x, y, (CARD_EX_SCALE * (LIMIT_CARD - 1) + CARD_E_WIDTH), CARD_E_HEIGHT);
        for (int i = 0; i < p.getCardNums(); i++) {
            g.drawImage(img, x + CARD_EX_SCALE * i, y, CARD_E_WIDTH, CARD_E_HEIGHT, null);
        }
    }

    public boolean onClickPass() {
        ClientFrame.getClientFrame().sendMessage(CommonDefine.CARD_GAME_ACTION_PASS);
        return true;
    }

    public boolean onClickFight(Graphics g) {
        List<Card> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (Card card : listOwnCard) {
            if (card.isSelected()) {
                list.add(card);
                sb.append(card.getRealNum()).append("_").append(card.getType().getValue()).append(CommonDefine.COMMA);
            }
        }
//        if (listPreviousFight == null) {
//            if (CardFunc.checkAtkTLMN(list)) {
//                ClientFrame.getClientFrame().sendMessage(CommonDefine.CARD_GAME_ACTION_FIGHT + sb.toString());
//                return true;
//            }
//            System.out.println("invalid!!!atk");
//            return false;
//        } else {
//            if (CardFunc.checkDefTLMN(listPreviousFight, list)) {
//                ClientFrame.getClientFrame().sendMessage(CommonDefine.CARD_GAME_ACTION_FIGHT + sb.toString());
//                return true;
//            }
//            System.out.println("invalid!!!atk");
//            return false;
//        }
        return false;
    }

    public void selectCard(Graphics g, int x, int y) {
        System.out.println("X: " + x);
        System.out.println("Y: " + y);
        if (listOwnCard == null || listOwnCard.isEmpty()) {
            return;
        }
        for (int i = 0; i < listOwnCard.size(); i++) {
            int s_w;
            int s_h = 0;
            if (listOwnCard.get(i).isSelected()) {
                s_h = CARD_PX_SCALE;
            }
            if (i != listOwnCard.size() - 1) {
                s_w = CARD_PX_SCALE;
            } else {
                s_w = CARD_P_WIDTH;
            }
            if (x > xStartOfList + CARD_PX_SCALE * i && x < xStartOfList + s_w + CARD_PX_SCALE * i && y > yStartOfList - s_h && y < GAME_HEIGHT) {
                listOwnCard.get(i).setSelected(!listOwnCard.get(i).isSelected());
                drawListOwnCard(g);
                break;
            }
        }
    }

    public void drawListOwnCard(Graphics g) {
        if (listOwnCard == null) {
            return;
        }
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(xStartOfList, GAME_HEIGHT - CARD_P_HEIGHT - CARD_PX_SCALE, widthOfList, CARD_P_HEIGHT + CARD_PX_SCALE);
        for (int i = 0; i < listOwnCard.size(); i++) {
            drawOwnCardImage(g, listOwnCard.get(i), xStartOfList + i * CARD_PX_SCALE, yStartOfList);
        }
    }

    private Player getPlayerByValue(int val) {
        for (Player p : listPlayer) {
            if (p.getValue() == val) {
                return p;
            }
        }
        return null;
    }

    public void drawListCardFight(Graphics g, List<Card> list, int p_val) {
        if (list.isEmpty()) {
            return;
        }
        if (p_val == myValue) {
            for (Card rCard : list) {
                listOwnCard.remove(rCard);
            }
            drawListOwnCard(g);
        } else {
            for (Player p : listPlayer) {
                if (p.getValue() == p_val) {
                    p.setCardNums(p.getCardNums() - list.size());
                    break;
                }
            }
            Player p = getPlayerByValue(p_val);
            if (p != null) {
                drawEnemyListCard(g, p);
            }
        }
        int start_x = (GAME_WIDTH - (CARD_FX_SCALE * (list.size() - 1) + CARD_F_WIDTH)) / 2;
        int start_y = (GAME_HEIGHT - CARD_F_HEIGHT) / 2;
        CardFunc.sortCardByNumAndType(list);
        if (listPreviousFight != null && !listPreviousFight.isEmpty()) {
            int x = (GAME_WIDTH - (CARD_FX_SCALE * (listPreviousFight.size() - 1) + CARD_F_WIDTH)) / 2;
            int y = (GAME_HEIGHT - CARD_F_HEIGHT) / 2;
            if (preValueList != 0) {
                if (preValueList == myValue) {
                    y += CARD_FX_SCALE;
                } else {
                    if (isNextValue(preValueList, myValue)) {
                        x += CARD_FX_SCALE;
                    } else if (isPreviousValue(preValueList, myValue)) {
                        x -= CARD_FX_SCALE;
                    } else {
                        y -= CARD_FX_SCALE;
                    }
                }
                g.setColor(CARD_HIDE_COLOR);
                g.fillRect(x, y, (CARD_FX_SCALE * (listPreviousFight.size() - 1) + CARD_F_WIDTH), CARD_F_HEIGHT);
            }
        }
        listPreviousFight = list;
        preValueList = p_val;
        if (p_val == myValue) {
            start_y += CARD_FX_SCALE;
        } else {
            if (isNextValue(p_val, myValue)) {
                start_x += CARD_FX_SCALE;
            } else if (isPreviousValue(p_val, myValue)) {
                start_x -= CARD_FX_SCALE;
            } else {
                start_y -= CARD_FX_SCALE;
            }
        }
        for (int i = 0; i < list.size(); i++) {
            drawCardImage(g, list.get(i), start_x + i * CARD_FX_SCALE, start_y, CARD_F_WIDTH, CARD_F_HEIGHT);
        }
    }

    private void drawPreviousListCard(Graphics g) {
        clearRound(g);
        if (listPreviousFight != null && !listPreviousFight.isEmpty()) {
            int x = (GAME_WIDTH - (CARD_FX_SCALE * (listPreviousFight.size() - 1) + CARD_F_WIDTH)) / 2;
            int y = (GAME_HEIGHT - CARD_F_HEIGHT) / 2;
            for (int i = 0; i < listPreviousFight.size(); i++) {
                drawCardImage(g, listPreviousFight.get(i), x + i * CARD_FX_SCALE, y, CARD_F_WIDTH, CARD_F_HEIGHT);
            }
        }
    }

    private void drawCardImage(Graphics g, Card card, int x, int y, int w, int h) {
        Image img = listImageCard.get(card.getRealNum() + "_" + card.getType().getValue());
        g.drawImage(img, x, y, w, h, null);
    }

    private void drawOwnCardImage(Graphics g, Card card, int x, int y) {
        Image img = listImageCard.get(card.getRealNum() + "_" + card.getType().getValue());
        if (card.isSelected()) {
            g.drawImage(img, x, y - CARD_PX_SCALE, CARD_P_WIDTH, CARD_P_HEIGHT, null);
        } else {
            g.drawImage(img, x, y, CARD_P_WIDTH, CARD_P_HEIGHT, null);
        }
    }

    public void clearRound(Graphics g) {
        listPreviousFight = null;
        g.setColor(BACKGROUND_COLOR);
        int w = (CARD_FX_SCALE * (LIMIT_CARD - 1) + CARD_F_WIDTH + 2 * CARD_FX_SCALE);
        int h = (CARD_F_HEIGHT + 2 * CARD_FX_SCALE);
        g.fillRect((GAME_WIDTH - w) / 2, (GAME_HEIGHT - h) / 2, w, h);
    }

    public void clearTimeCounDown(Graphics g) {
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(IMG_COUNT_DOWN_X_START, IMG_COUNT_DOWN_Y_START, IMG_COUNT_DOWN_WIDTH, IMG_COUNT_DOWN_HEIGHT);
    }

    public void drawCountDownImg(Graphics g, int timecd, int maxTime, boolean isTurn) {
        if (isTurn) {
            clearTimeCounDown(g);
            Image img = listImageCard.get("count_down_" + timecd);
            int w = img.getWidth(null);
            g.drawImage(img, (IMG_COUNT_DOWN_X_START + (IMG_COUNT_DOWN_WIDTH - w) / 2), IMG_COUNT_DOWN_Y_START, w, IMG_COUNT_DOWN_HEIGHT, null);
        } else {

        }
    }

    public void drawPlayButton(Graphics g, boolean isHover) {
        clearRound(g);
        Image img = listImageCard.get("play_game");
        if (isHover) {
            img = listImageCard.get("play_game_hover");
        }
        g.drawImage(img, (GAME_WIDTH - BUTTON_PLAY_WIDTH) / 2, (GAME_HEIGHT - BUTTON_PLAY_HEIGHT) / 2, BUTTON_PLAY_WIDTH, BUTTON_PLAY_HEIGHT, null);
    }

    public void drawPlayButtonPress(Graphics g) {
        clearRound(g);
        Image img = listImageCard.get("play_game_press");
        int w = 176;
        int h = 44;
        g.drawImage(img, (GAME_WIDTH - 176) / 2, (GAME_HEIGHT - 44) / 2, 176, 44, null);
    }

    public void drawFightButton(Graphics g, boolean isHover) {
        String text = "Đánh";
        Font f = new Font("Tahoma", Font.BOLD, 12);
        int txt_w = g.getFontMetrics(f).stringWidth(text);
        int txt_h = g.getFontMetrics(f).getHeight();
        if (isHover) {
            g.setColor(new Color(39, 174, 96));//hover color
            g.fill3DRect(BUTTON_X_START, BUTTON_Y_START, BUTTON_WIDTH, BUTTON_HEIGHT, true);
            g.setFont(f);
            g.setColor(new Color(230, 126, 34));
            g.drawString(text, BUTTON_X_START + (BUTTON_WIDTH - txt_w) / 2, BUTTON_Y_START + BUTTON_HEIGHT / 2 + txt_h / 3);
        } else {
            g.setColor(new Color(230, 126, 34));//color
            g.fill3DRect(BUTTON_X_START, BUTTON_Y_START, BUTTON_WIDTH, BUTTON_HEIGHT, true);
            g.setFont(f);
            g.setColor(new Color(244, 246, 247));
            g.drawString(text, BUTTON_X_START + (BUTTON_WIDTH - txt_w) / 2, BUTTON_Y_START + BUTTON_HEIGHT / 2 + txt_h / 3);
        }
    }

    public void drawPassButton(Graphics g, boolean isHover) {
        String text = "Bỏ qua";
        Font f = new Font("Tahoma", Font.BOLD, 12);
        int txt_w = g.getFontMetrics(f).stringWidth(text);
        int txt_h = g.getFontMetrics(f).getHeight();
        if (isHover) {
            g.setColor(new Color(224, 224, 224));
            g.fill3DRect(BUTTON_X_START + BUTTON_WIDTH + BUTTON_X_SCALE, BUTTON_Y_START, BUTTON_WIDTH, BUTTON_HEIGHT, true);
            g.setFont(f);
            g.setColor(new Color(75, 75, 75));
            g.drawString(text, BUTTON_X_START + BUTTON_WIDTH + BUTTON_X_SCALE + (BUTTON_WIDTH - txt_w) / 2, BUTTON_Y_START + BUTTON_HEIGHT / 2 + txt_h / 3);
        } else {
            g.setColor(new Color(100, 100, 100));
            g.fill3DRect(BUTTON_X_START + BUTTON_WIDTH + BUTTON_X_SCALE, BUTTON_Y_START, BUTTON_WIDTH, BUTTON_HEIGHT, true);
            g.setFont(f);
            g.setColor(new Color(240, 240, 240));
            g.drawString(text, BUTTON_X_START + BUTTON_WIDTH + BUTTON_X_SCALE + (BUTTON_WIDTH - txt_w) / 2, BUTTON_Y_START + BUTTON_HEIGHT / 2 + txt_h / 3);
        }
    }

    private boolean isNextValue(int e_val, int p_val) {
        return (e_val == p_val + 1 || (p_val == 4 && e_val == 1));
    }

    private boolean isPreviousValue(int e_val, int p_val) {
        return (e_val == p_val - 1 || (e_val == 4 && p_val == 1));
    }

    public boolean isPlayGameHover(int x, int y) {
        return (x > (GAME_WIDTH - BUTTON_PLAY_WIDTH) / 2 && x < (GAME_WIDTH + BUTTON_PLAY_WIDTH) / 2 && y > (GAME_HEIGHT - BUTTON_PLAY_HEIGHT) / 2 && y < (GAME_HEIGHT + BUTTON_PLAY_HEIGHT) / 2);
    }

    public boolean isListCardHover(int x, int y) {
        return (x > xStartOfList && x < xStartOfList + widthOfList && y > yStartOfList - CARD_PX_SCALE && y < GAME_HEIGHT);
    }

    public boolean isButtonFightHover(int x, int y) {
        return (x > BUTTON_X_START && x < BUTTON_X_START + BUTTON_WIDTH && y > BUTTON_Y_START && y < BUTTON_Y_START + BUTTON_HEIGHT);
    }

    public boolean isButtonPassHover(int x, int y) {
        return (x > BUTTON_X_START + BUTTON_WIDTH + BUTTON_X_SCALE && x < BUTTON_X_START + 2 * BUTTON_WIDTH + BUTTON_X_SCALE && y > BUTTON_Y_START && y < BUTTON_Y_START + BUTTON_HEIGHT);
    }
}
