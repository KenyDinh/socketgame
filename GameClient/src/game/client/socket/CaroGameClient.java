/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.client.socket;

import game.client.common.CommonDefine;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author tkv-nhukhanhdinh
 */
public class CaroGameClient {

    private static final int CELL_SIZE = CommonDefine.CARO_GAME_CELL_SIZE;
    private static final int NUM_COLUMNS = CommonDefine.CARO_GAME_NUM_COLUMNS;
    private static final int NUM_ROWS = CommonDefine.CARO_GAME_NUM_ROWS;
    private static final int X_START = CommonDefine.CARO_GAME_X_START;
    private static final int Y_START = CommonDefine.CARO_GAME_Y_START;
    private static final int SCALE_SIZE = CommonDefine.CARO_GAME_SCALE_SIZE;

    public static void drawBoard(Graphics g) {
        g.setColor(CommonDefine.CARO_COLOR_TABLE_BORDER);
        for (int i = 0; i <= NUM_COLUMNS; i++) {
            g.drawLine(X_START + (CELL_SIZE * i), Y_START, X_START + (CELL_SIZE * i), CommonDefine.MAIN_GAME_HEIGHT - Y_START);
        }
        for (int j = 0; j <= NUM_ROWS; j++) {
            g.drawLine(X_START, Y_START + (CELL_SIZE * j), CommonDefine.MAIN_GAME_WIDTH - X_START, Y_START + (CELL_SIZE * j));
        }
    }

    public static void removeCurrentPosition(Graphics g,int x_column, int y_row) {
        g.setColor(CommonDefine.CARO_COLOR_TABLE_BORDER);
        g.drawRect(X_START + x_column * CELL_SIZE, Y_START + y_row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }
    
    public static void drawCurrentPosition(Graphics g,int x_column, int y_row) {
        if(x_column == -1 || y_row == -1){
            return;
        }
        g.setColor(CommonDefine.CARO_COLOR_CURRENT_POSITION);
        g.drawRect(X_START + x_column * CELL_SIZE, Y_START + y_row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }
    
    public static void drawPositionHover(Graphics g, int value, int x_column, int y_row){
        if(value == CommonDefine.CARO_GAME_O_VALUE){
            drawO(g, CommonDefine.CARO_COLOR_POSITION_O_HOVER, x_column, y_row);
        }else{
            drawX(g, CommonDefine.CARO_COLOR_POSITION_X_HOVER, x_column, y_row);
        }
    }

    public static void drawO(Graphics g, Color c, int x_column, int y_row) {
        Graphics2D gd2 = (Graphics2D) g.create();
        gd2.setStroke(new BasicStroke(3));
        gd2.setColor(c);
        gd2.drawOval(X_START + SCALE_SIZE + x_column * CELL_SIZE, Y_START + SCALE_SIZE + y_row * CELL_SIZE, CELL_SIZE - (SCALE_SIZE * 2), CELL_SIZE - (SCALE_SIZE * 2));
    }

    public static void rePaintMainGame(Graphics g) {
        g.setColor(CommonDefine.MAIN_GAME_BOARD_BACKGROUND);
        g.fillRect(0, 0, CommonDefine.MAIN_GAME_WIDTH, CommonDefine.MAIN_GAME_HEIGHT);
    }
    
    public static void rePaintjPanel(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, CommonDefine.MAIN_GAME_WIDTH, CommonDefine.MAIN_GAME_HEIGHT);
    }

    public static void drawX(Graphics g, Color c, int x_column, int y_row) {
        Graphics2D gd2 = (Graphics2D) g.create();
        gd2.setStroke(new BasicStroke(3));
        gd2.setColor(c);
        //cross1 + +
        gd2.drawLine(X_START + x_column * CELL_SIZE + (SCALE_SIZE * 2), Y_START + y_row * CELL_SIZE + (SCALE_SIZE * 2), X_START + CELL_SIZE * (x_column + 1) - (SCALE_SIZE * 2), Y_START + (y_row + 1) * CELL_SIZE - (SCALE_SIZE * 2));
        //cross2 + -
        gd2.drawLine(X_START + CELL_SIZE * (x_column + 1) - (SCALE_SIZE * 2), Y_START + y_row * CELL_SIZE + (SCALE_SIZE * 2), X_START + x_column * CELL_SIZE + (SCALE_SIZE * 2), Y_START + (y_row + 1) * CELL_SIZE - (SCALE_SIZE * 2));
    }

    // "\"
    public static void drawCross1(Graphics g, Color c, int x_column, int y_row) {
        Graphics2D gd2 = (Graphics2D) g.create();
        gd2.setStroke(new BasicStroke(3));
        gd2.setColor(c);
        gd2.drawLine(X_START + x_column * CELL_SIZE, Y_START + y_row * CELL_SIZE, X_START + CELL_SIZE * (x_column + 1), Y_START + (y_row + 1) * CELL_SIZE);
    }

    // "/"
    public static void drawCross2(Graphics g, Color c, int x_column, int y_row) {
        Graphics2D gd2 = (Graphics2D) g.create();
        gd2.setStroke(new BasicStroke(3));
        gd2.setColor(c);
        gd2.drawLine(X_START + CELL_SIZE * (x_column + 1), Y_START + y_row * CELL_SIZE, X_START + x_column * CELL_SIZE, Y_START + (y_row + 1) * CELL_SIZE);
    }

    // "--"
    public static void drawHoz(Graphics g, Color c, int x_column, int y_row) {
        Graphics2D gd2 = (Graphics2D) g.create();
        gd2.setStroke(new BasicStroke(3));
        gd2.setColor(c);
        gd2.drawLine(X_START + x_column * CELL_SIZE, Y_START + y_row * CELL_SIZE + CELL_SIZE / 2, X_START + (x_column + 1) * CELL_SIZE, Y_START + y_row * CELL_SIZE + CELL_SIZE / 2);
    }

    // "|"
    public static void drawVer(Graphics g, Color c, int x_column, int y_row) {
        Graphics2D gd2 = (Graphics2D) g.create();
        gd2.setStroke(new BasicStroke(3));
        gd2.setColor(c);
        gd2.drawLine(X_START + x_column * CELL_SIZE + CELL_SIZE / 2, Y_START + y_row * CELL_SIZE, X_START + x_column * CELL_SIZE + CELL_SIZE / 2, Y_START + (y_row + 1) * CELL_SIZE);
    }

    public static void drawButtonPlayAgain(Graphics g, Color back, Color text) {
        Graphics2D gd2 = (Graphics2D) g.create();
        gd2.setStroke(new BasicStroke(3));
        gd2.setColor(back);
        gd2.fill3DRect(300, 225, 70, 30, true);
        gd2.setColor(text);
        gd2.setFont(new Font("Tahoma", Font.BOLD, 12));
        gd2.drawString("Play Again", 303, 244);
    }

}
