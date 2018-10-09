package game.client.socket;

import game.client.gui.DxhFrame;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Map;

/**
 *
 * @author tkv-vule
 */
public class DayXeHangGameClient {

    private static final int START_VALUE = 7;
    private static final int WALL_VALUE = 1;
    private static final int WAY_VALUE = 2;
    private static final int BOX1_VALUE = 3;
    private static final int POINT_VALUE = 4;
    private static final int BOX2_VALUE = 5;
    private static final int SIZE = 40;
    public static final int IMG_MOVE_DOWN = 1;
    public static final int IMG_MOVE_UP = 2;
    public static final int IMG_MOVE_LEFT = 3;
    public static final int IMG_MOVE_RIGHT = 4;
    private static final Color BACK_COLOR = Color.BLACK;
    private static int[][] map;
    private static final int MAX_SIZE_MAP = 800;
    private static int position_start;
    private static Map<String, Image> mapImage;
    private static final String IMG_PLAYER_NAME = "player_";
    private static int status_player = 1;
    private static int player_x;
    private static int player_y;

    public static void setPlayerX(int px) {
        player_x = px;
    }

    public static int getPx() {
        return player_x;
    }

    public static void setPlayerY(int py) {
        player_y = py;
    }

    public static int getPy() {
        return player_y;
    }
    
    public static void setStatusPlayer(int status){
        status_player = status;
    }
    
    public static int getStatusPlayer(){
        return status_player;
    }

    public static void initGameData(int[][] data) {
        map = data;
    }

    public static void initImage(Map<String, Image> images) {
        mapImage = images;
    }

    public static int[][] getCurrentMap() {
        return map;
    }

    public static void drawMainGame(Graphics g) {
        if (map == null) {
            return;
        }
        g.setColor(BACK_COLOR);
        g.fillRect(0, 0, MAX_SIZE_MAP, MAX_SIZE_MAP);
        int len = map.length;
        int map_size = len * SIZE;
        position_start = (MAX_SIZE_MAP - map_size) / 2;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                switch (map[i][j]) {
                    case START_VALUE:
                        player_x = j;
                        player_y = i;
                        map[i][j] = WAY_VALUE;
                        break;
                    case WALL_VALUE:
                        g.drawImage(mapImage.get("wall"), j * SIZE + position_start, i * SIZE + position_start, SIZE, SIZE, null);
                        break;
                    case WAY_VALUE:
                        g.drawImage(mapImage.get("way"), j * SIZE + position_start, i * SIZE + position_start, SIZE, SIZE, null);
                        break;
                    case BOX1_VALUE:
                        drawUnit(g, mapImage.get("box1"), j, i, 0);
                        break;
                    case BOX2_VALUE:
                        drawUnit(g, mapImage.get("box2"), j, i, 0);
                        break;
                    case POINT_VALUE:
                        g.drawImage(mapImage.get("point"), j * SIZE + position_start, i * SIZE + position_start, SIZE, SIZE, null);
                        break;
                }

            }
        }
        drawUnit(g, mapImage.get(IMG_PLAYER_NAME + status_player), player_x, player_y, 0);
        if (isGameEnd()) {
            drawEndGame(g);
        }
    }

    /**
     *
     * @param g
     * @param point
     * @param way
     * @param img
     * @param x destination x
     * @param y destination y
     * @param status
     */
    //when move
    private static void drawUnit(Graphics g, Image img, int x, int y, int status) {
        int pre_x = x;
        int pre_y = y;
        switch (status) {
            case IMG_MOVE_DOWN:
                pre_y--;
                break;
            case IMG_MOVE_UP:
                pre_y++;
                break;
            case IMG_MOVE_LEFT:
                pre_x++;
                break;
            case IMG_MOVE_RIGHT:
                pre_x--;
                break;
            default:
                break;
        }
        g.setColor(BACK_COLOR);
        g.fillRect(pre_x, pre_y, SIZE, SIZE);
        if (map[pre_y][pre_x] == POINT_VALUE) {
            g.drawImage(mapImage.get("point"), pre_x * SIZE + position_start, pre_y * SIZE + position_start, SIZE, SIZE, null);
        } else {
            g.drawImage(mapImage.get("way"), pre_x * SIZE + position_start, pre_y * SIZE + position_start, SIZE, SIZE, null);
        }
        g.drawImage(img, x * SIZE + position_start, y * SIZE + position_start, SIZE, SIZE, null);
    }

    public static boolean isGameEnd() {
        int len = map.length;
        for (int j = 0; j < len; j++) {
            for (int i = 0; i < len; i++) {
                if (map[j][i] == POINT_VALUE) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     *
     * @param g
     * @param status
     */
    public static void moveUnit(Graphics g, int status) {
        if (isGameEnd()) {
            return;
        }
        if (map == null) {
            return;
        }
        int next_x = player_x;
        int next_y = player_y;
        int next_n_x = player_x;
        int next_n_y = player_y;
        switch (status) {
            case IMG_MOVE_DOWN:
                status_player = IMG_MOVE_DOWN;
                next_y++;
                next_n_y += 2;
                break;
            case IMG_MOVE_UP:
                status_player = IMG_MOVE_UP;
                next_y--;
                next_n_y -= 2;
                break;
            case IMG_MOVE_LEFT:
                status_player = IMG_MOVE_LEFT;
                next_x--;
                next_n_x -= 2;
                break;
            case IMG_MOVE_RIGHT:
                status_player = IMG_MOVE_RIGHT;
                next_x++;
                next_n_x += 2;
                break;
            default:
                status_player = 1;
                break;
        }
        if (map[next_y][next_x] == WAY_VALUE || map[next_y][next_x] == POINT_VALUE) {
            drawUnit(g, mapImage.get(IMG_PLAYER_NAME + status), next_x, next_y, status);
            player_x = next_x;
            player_y = next_y;
            DxhFrame.getDxhFrame().changeCountMove(map, player_x, player_y, status_player);
        } else if (map[next_y][next_x] == BOX1_VALUE || map[next_y][next_x] == BOX2_VALUE) {
            if (map[next_n_y][next_n_x] == WAY_VALUE || map[next_n_y][next_n_x] == POINT_VALUE) {
                player_x = next_x;
                player_y = next_y;
                if (map[next_n_y][next_n_x] == WAY_VALUE) {
                    drawUnit(g, mapImage.get("box1"), next_n_x, next_n_y, status);
                    drawUnit(g, mapImage.get(IMG_PLAYER_NAME + status), next_x, next_y, status);
                } else if (map[next_n_y][next_n_x] == POINT_VALUE) {
                    drawUnit(g, mapImage.get("box2"), next_n_x, next_n_y, status);
                    drawUnit(g, mapImage.get(IMG_PLAYER_NAME + status), next_x, next_y, status);
                }
                map[next_y][next_x]--;
                map[next_n_y][next_n_x]++;
                //
                DxhFrame.getDxhFrame().changeCountMove(map, player_x, player_y, status_player);
                if (isGameEnd()) {
                    //send mess to server
                    drawEndGame(g);
                    DxhFrame.getDxhFrame().enableContinue();
                    DxhFrame.getDxhFrame().updateCoin();
                    DxhFrame.getDxhFrame().resetMoveWhenEndGame();
                    DxhFrame.getDxhFrame().setEndGame();
                }
            }
        }
    }

    private static void drawEndGame(Graphics g) {
        Font f = new Font("Times New Roman", Font.BOLD, 25);
        g.setFont(f);
        g.setColor(Color.CYAN);
        int h = g.getFontMetrics(f).getHeight();
        int w = g.getFontMetrics(f).stringWidth("Mission Complete");
        g.drawString("Mission Complete", (MAX_SIZE_MAP - w) / 2, (MAX_SIZE_MAP - h) / 2);
    }

}
