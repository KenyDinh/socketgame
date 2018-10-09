/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.client.socket;

import game.client.common.CommonDefine;
import game.client.gui.ClientFrame;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tkv-nhukhanhdinh
 */
public class BauCuaGameClient {

    public static final int PLATE_SIZE = 150;
    public static final int BOWL_SIZE = 110;
    public static final int DICE_SIZE = 30;
    public static final int X_FILL_PLATE = (CommonDefine.MAIN_GAME_WIDTH - PLATE_SIZE) / 2;
    public static final int Y_FILL_PLATE = (CommonDefine.MAIN_GAME_HEIGHT - PLATE_SIZE) / 2 + 80;

    public static final int Y_START_ROW_1 = 110;
    public static final int Y_START_ROW_2 = 185;
    public static final int X_START_COLUMN_1 = 215;
    public static final int X_START_COLUMN_2 = 297;
    public static final int X_START_COLUMN_3 = 379;

    public static final int CELL_HEIGHT = 56;
    public static final int CELL_WIDTH = 76;

    public static void rePaintMainGame(Graphics g) {
        g.setColor(CommonDefine.MAIN_GAME_BOARD_BACKGROUND);
        g.fillRect(0, 0, CommonDefine.MAIN_GAME_WIDTH, CommonDefine.MAIN_GAME_HEIGHT);
    }

    public static void rePaintjPanel(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, CommonDefine.MAIN_GAME_WIDTH, CommonDefine.MAIN_GAME_HEIGHT);
    }

    public static void drawBaucuaDesk(Graphics g, Image img) {
        int height = 140;
        int width = (int) (img.getWidth(null) * height / img.getHeight(null));
        int x = (CommonDefine.MAIN_GAME_WIDTH - width) / 2;
        int y = (CommonDefine.MAIN_GAME_HEIGHT - height) / 2 - 65;
        g.drawImage(img, x, y, width, height, null);
    }

    private static void drawResultImg(Graphics g, Image image1, Image image2, Image image3) {
        int height = DICE_SIZE;
        int width = DICE_SIZE;
        int space_size = 1;
        int scale_y = 6;
        int x1 = (CommonDefine.MAIN_GAME_WIDTH - width) / 2;
        int y1 = (CommonDefine.MAIN_GAME_HEIGHT - height) / 2 + 80 - height / 2 - space_size - scale_y;

        int x2 = (CommonDefine.MAIN_GAME_WIDTH - width) / 2 - width / 2 - space_size;
        int y2 = (CommonDefine.MAIN_GAME_HEIGHT - height) / 2 + 80 + height / 2 + space_size - scale_y;

        int x3 = (CommonDefine.MAIN_GAME_WIDTH - width) / 2 + width / 2 + space_size;
        int y3 = y2;

        g.drawImage(image1, x1, y1, width, height, null);
        g.drawImage(image2, x2, y2, width, height, null);
        g.drawImage(image3, x3, y3, width, height, null);
    }

    private static void drawBaucuaPlate(Graphics g, Image img, int h) {
        int height = h;
        int width = PLATE_SIZE;
        int x = (CommonDefine.MAIN_GAME_WIDTH - width) / 2;
        int y = (CommonDefine.MAIN_GAME_HEIGHT - height) / 2 + 80;
        g.drawImage(img, x, y, width, height, null);
    }

    private static void drawBaucuaBowl(Graphics g, Image img, int h, int x_start) {
        int height = h;
        int width = BOWL_SIZE;
        int y = (CommonDefine.MAIN_GAME_HEIGHT - height) / 2 + 78;
        g.drawImage(img, x_start, y, width, height, null);
    }

    public static void lacBaucua(Graphics g, Image plate, Image bowl, boolean isHost, int totalCoins) {
        // mac dinh khong cho phep dat cuoc truoc khi lac
        new Thread() {
            @Override
            public void run() {
                int x_start_bowl = (CommonDefine.MAIN_GAME_WIDTH - BOWL_SIZE) / 2;
                g.setColor(CommonDefine.MAIN_GAME_BOARD_BACKGROUND);
                g.fillRect(X_FILL_PLATE, Y_FILL_PLATE, PLATE_SIZE, PLATE_SIZE);
                drawBaucuaPlate(g, plate, PLATE_SIZE);
                drawBaucuaBowl(g, bowl, BOWL_SIZE, x_start_bowl);
                try {
                    sleep(600);
                } catch (InterruptedException ex) {
                    Logger.getLogger(BauCuaGameClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                for (int i = 0; i < 10; i++) {
                    g.fillRect(X_FILL_PLATE, Y_FILL_PLATE, PLATE_SIZE, PLATE_SIZE);
                    drawBaucuaPlate(g, plate, PLATE_SIZE - 10);
                    drawBaucuaBowl(g, bowl, BOWL_SIZE - 10, x_start_bowl);
                    try {
                        sleep(110);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BauCuaGameClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    g.fillRect(X_FILL_PLATE, Y_FILL_PLATE, PLATE_SIZE, PLATE_SIZE);
                    drawBaucuaPlate(g, plate, PLATE_SIZE);
                    drawBaucuaBowl(g, bowl, BOWL_SIZE, x_start_bowl);
                    try {
                        sleep(110);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BauCuaGameClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //send mess to server cho phep dat cuoc
                if(isHost){
                    ClientFrame.getClientFrame().sendMessage(CommonDefine.BAUCUA_GAME_FINISH_SHAKE + CommonDefine.SEPARATOR_KEY_VALUE + totalCoins);
                    drawButton(g, false);
                }
            }
        }.start();
    }

    public static void openBaucua(Graphics g, Image plate, Image bowl, Image image1, Image image2, Image image3, boolean isHost) {
        //truoc khi goi ham nay: send mess to server khong cho phep dat cuoc
        new Thread() {
            @Override
            public void run() {
                int x_move = 3;
                int x_start_bowl = (CommonDefine.MAIN_GAME_WIDTH - BOWL_SIZE) / 2;
                g.setColor(CommonDefine.MAIN_GAME_BOARD_BACKGROUND);
                int num = 30;
                for (int i = 1; i <= num; i++) {
                    g.fillRect(X_FILL_PLATE, Y_FILL_PLATE, PLATE_SIZE + i * x_move, PLATE_SIZE);
                    drawBaucuaPlate(g, plate, PLATE_SIZE);
                    drawResultImg(g, image1, image2, image3);
                    if (i < num) {
                        drawBaucuaBowl(g, bowl, BOWL_SIZE, x_start_bowl + i * x_move);
                    }
                    try {
                        sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BauCuaGameClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //send mess to server set isOpenBaucua = true;
                if(isHost){
                    ClientFrame.getClientFrame().sendMessage(CommonDefine.BAUCUA_GAME_FINISH_OPEN);
                    drawButton(g, true);
                }
            }
        }.start();
    }

    public static void drawUserIcon(Graphics g, Image img, int index, String username) {
        int height = 40;
        int width = (int) (img.getWidth(null) * height / img.getHeight(null));
        int x;
        int y;
        int x_text;
        Font f = new Font("Times New Roman", Font.PLAIN, 13);
        g.setFont(f);
        int height_text = 12;
        int text_width = g.getFontMetrics(f).stringWidth(username);
        switch (index) {
            case 0:
                x = 0;
                y = CommonDefine.MAIN_GAME_HEIGHT - height - height_text;
                if (text_width >= width) {
                    x_text = 0;
                } else {
                    x_text = width / 2 - text_width / 2;
                }
                break;
            case 1:
                x = 0;
                y = (CommonDefine.MAIN_GAME_HEIGHT - height) / 2;
                if (text_width >= width) {
                    x_text = 0;
                } else {
                    x_text = width / 2 - text_width / 2;
                }
                break;
            case 2:
                x = 0;
                y = 0;
                if (text_width >= width) {
                    x_text = 0;
                } else {
                    x_text = width / 2 - text_width / 2;
                }
                break;
            case 3:
                x = CommonDefine.MAIN_GAME_WIDTH / 3 - width / 2;
                y = 0;
                x_text = CommonDefine.MAIN_GAME_WIDTH / 3 - text_width / 2;
                break;
            case 4:
                x = (CommonDefine.MAIN_GAME_WIDTH * 2) / 3 - width / 2;
                y = 0;
                x_text = (CommonDefine.MAIN_GAME_WIDTH * 2) / 3 - text_width / 2;
                break;
            case 5:
                x = CommonDefine.MAIN_GAME_WIDTH - width;
                y = 0;
                x_text = CommonDefine.MAIN_GAME_WIDTH - text_width / 2 - width / 2;
                break;
            case 6:
                x = CommonDefine.MAIN_GAME_WIDTH - width;
                y = (CommonDefine.MAIN_GAME_HEIGHT - height) / 2;
                x_text = CommonDefine.MAIN_GAME_WIDTH - text_width / 2 - width / 2;
                break;
            case 7:
                x = CommonDefine.MAIN_GAME_WIDTH - width;
                y = CommonDefine.MAIN_GAME_HEIGHT - height - height_text;
                x_text = CommonDefine.MAIN_GAME_WIDTH - text_width / 2 - width / 2;
                break;
            case 8:
                x = (CommonDefine.MAIN_GAME_WIDTH * 2) / 3 - width / 2;
                y = CommonDefine.MAIN_GAME_HEIGHT - height - height_text;
                x_text = (CommonDefine.MAIN_GAME_WIDTH * 2) / 3 - text_width / 2;
                break;
            case 9:
                x = CommonDefine.MAIN_GAME_WIDTH / 3 - width / 2;
                y = CommonDefine.MAIN_GAME_HEIGHT - height - height_text;
                x_text = CommonDefine.MAIN_GAME_WIDTH / 3 - text_width / 2;
                break;
            default:
                x = -1;
                y = -1;
                x_text = -1;
                break;
        }
        removeUser(g, index);
        if (x + y + x_text >= 0) {
            g.drawImage(img, x, y, width, height, null);
            g.setColor(Color.BLACK);
            g.drawString(username, x_text, y + width + height_text - 5);
        }
    }

    public static void drawTienCuoc(Graphics g, String[] tiencuoc) {
        g.setColor(CommonDefine.MAIN_GAME_BOARD_BACKGROUND);
        g.fillRect(75, 140, 125, 160);
        int x_start = 80;
        int y_start = 160;
        Font f = new Font("Tahoma", Font.BOLD, 13);
        g.setFont(f);
        g.setColor(Color.GREEN);
        g.drawString("Total Coin:", x_start, y_start);
        for (String s : tiencuoc) {
            if (s != null) {
                y_start += 20;
                g.drawString(s, x_start, y_start);
            }
        }
    }

    public static void drawResultStr(Graphics g, String[] resultStr) {
        g.setColor(CommonDefine.MAIN_GAME_BOARD_BACKGROUND);
        g.fillRect(490, 140, 80, 160);
        int x_start = 500;
        int y_start = 160;
        Font f = new Font("Tahoma", Font.BOLD, 15);
        g.setFont(f);
        g.setColor(Color.GREEN);
        g.drawString("Result:", x_start, y_start);
        for (String s : resultStr) {
            if (s != null) {
                y_start += 38;
                g.drawString(s, x_start, y_start);
            }
        }
    }

    public static void repaintBaucua(Graphics g, String[] listUser, Image[] resultImg, boolean isOpenBaucua, String[] tiencuoc, String[] resultStr, Image desk, Image plate, Image bowl, Image user, boolean isHost) {
        rePaintMainGame(g);
        for (int i = 0; i < CommonDefine.BAUCUA_GAME_MAX_PLAYER; i++) {
            if (listUser[i] != null) {
                drawUserIcon(g, user, i, listUser[i]);
            }
        }
        drawBaucuaDesk(g, desk);
        drawTienCuoc(g, tiencuoc);
        drawResultStr(g, resultStr);
        drawBaucuaPlate(g, plate, PLATE_SIZE);
        if (resultImg.length == 3 && resultImg[0] != null && resultImg[1] != null && resultImg[2] != null) {
            drawResultImg(g, resultImg[0], resultImg[1], resultImg[2]);
        }
        if (!isOpenBaucua) {
            int x_start_bowl = (CommonDefine.MAIN_GAME_WIDTH - BOWL_SIZE) / 2;
            drawBaucuaBowl(g, bowl, BOWL_SIZE, x_start_bowl);
        }
        if(isHost){
            drawButton(g, isOpenBaucua);
        }
    }

    public static void removeUser(Graphics g, int index) {
        int x;
        int y;
        int w = 60;
        int h = 60;
        switch (index) {
            case 0:
                x = 0;
                y = CommonDefine.MAIN_GAME_HEIGHT - h;
                break;
            case 1:
                x = 0;
                y = (CommonDefine.MAIN_GAME_HEIGHT - h) / 2;
                break;
            case 2:
                x = 0;
                y = 0;
                break;
            case 3:
                x = CommonDefine.MAIN_GAME_WIDTH / 3 - w / 2;
                y = 0;
                break;
            case 4:
                x = (CommonDefine.MAIN_GAME_WIDTH * 2) / 3 - w / 2;
                y = 0;
                break;
            case 5:
                x = CommonDefine.MAIN_GAME_WIDTH - w;
                y = 0;
                break;
            case 6:
                x = CommonDefine.MAIN_GAME_WIDTH - w;
                y = (CommonDefine.MAIN_GAME_HEIGHT - h) / 2;
                break;
            case 7:
                x = CommonDefine.MAIN_GAME_WIDTH - w;
                y = CommonDefine.MAIN_GAME_HEIGHT - h;
                break;
            case 8:
                x = (CommonDefine.MAIN_GAME_WIDTH * 2) / 3 - w / 2;
                y = CommonDefine.MAIN_GAME_HEIGHT - h;
                break;
            case 9:
                x = CommonDefine.MAIN_GAME_WIDTH / 3 - w / 2;
                y = CommonDefine.MAIN_GAME_HEIGHT - h;
                break;
            default:
                x = -1;
                y = -1;
                break;
        }
        if (x + y >= 0) {
            g.setColor(CommonDefine.MAIN_GAME_BOARD_BACKGROUND);
            g.fillRect(x, y, w, h);
        }
    }

    public static void drawButton(Graphics g, boolean isOpenBauCua) {
        g.setColor(CommonDefine.MAIN_GAME_BOARD_BACKGROUND);
        g.fillOval(309, 399, 52, 27);
        g.setColor(new Color(0, 155, 0));
        g.fillOval(310, 400, 50, 25);
        g.setColor(new Color(232, 233, 223));
        g.drawOval(310, 400, 50, 25);
        g.setFont(new Font("Consolas", Font.BOLD, 18));
        g.setColor(new Color(231, 231, 231));
        if (isOpenBauCua) {
            g.drawString("Lắc", 321, 418);
        } else {
            g.drawString("Mở", 325, 418);
        }
    }

    public static int getChoice(int x, int y) {
        if (y >= Y_START_ROW_2 && y <= Y_START_ROW_2 + CELL_HEIGHT) {
            if (x >= X_START_COLUMN_3 && x <= X_START_COLUMN_3 + CELL_WIDTH) {
                return CommonDefine.BAUCUA_VALUE_OF_TOM;
            }
            if (x >= X_START_COLUMN_2 && x <= X_START_COLUMN_2 + CELL_WIDTH) {
                return CommonDefine.BAUCUA_VALUE_OF_CUA;
            }
            if (x >= X_START_COLUMN_1 && x <= X_START_COLUMN_1 + CELL_WIDTH) {
                return CommonDefine.BAUCUA_VALUE_OF_CA;
            }
        }
        if (y >= Y_START_ROW_1 && y <= Y_START_ROW_1 + CELL_HEIGHT) {
            if (x >= X_START_COLUMN_3 && x <= X_START_COLUMN_3 + CELL_WIDTH) {
                return CommonDefine.BAUCUA_VALUE_OF_GA;
            }
            if (x >= X_START_COLUMN_2 && x <= X_START_COLUMN_2 + CELL_WIDTH) {
                return CommonDefine.BAUCUA_VALUE_OF_BAU;
            }
            if (x >= X_START_COLUMN_1 && x <= X_START_COLUMN_1 + CELL_WIDTH) {
                return CommonDefine.BAUCUA_VALUE_OF_HUOU;
            }
        }
        return 0;
    }
}
