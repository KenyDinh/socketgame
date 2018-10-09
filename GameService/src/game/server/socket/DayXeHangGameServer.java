package game.server.socket;

import game.server.common.CommonDefine;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

/**
 *
 * @author tkv-vule
 */
public class DayXeHangGameServer {

    private static final int START_VALUE = 7;
    private static final int WALL_VALUE = 1;
    private static final int WAY_VALUE = 2;
    private static final int BOX1_VALUE = 3;
    private static final int POINT_VALUE = 4;
    private static final int BOX2_VALUE = 5;//box point
    private static final int SIZE = 40;
    public static final int IMG_MOVE_DOWN = 1;
    public static final int IMG_MOVE_UP = 2;
    public static final int IMG_MOVE_LEFT = 3;
    public static final int IMG_MOVE_RIGHT = 4;
    private static final Color BACK_COLOR = Color.BLACK;
    private static int[][] map;
    private static final int MAX_SIZE_MAP = 800;
    private static int position_start;
    private static Map<String, Image> mapImage = new HashMap<>();
    private static final String IMG_PLAYER_NAME = "player_1";
    private static int player_x;
    private static int player_y;
    private static Map<Integer, String> mapData = new HashMap<>();

    public static void initGameData(String data) {
        if (data.contains(CommonDefine.BREAK_LINE)) {
            String[] arr = data.split(CommonDefine.BREAK_LINE);
            for (String s : arr) {
                if (s.contains(":")) {
                    mapData.put(Integer.parseInt(s.split(":")[0].trim()), s.split(":")[1].trim());
                }
            }
        }
        mapImage.put("player_2", new ImageIcon("img\\dxh\\top.png").getImage());
        mapImage.put("player_1", new ImageIcon("img\\dxh\\bottom.png").getImage());
        mapImage.put("player_3", new ImageIcon("img\\dxh\\left.png").getImage());
        mapImage.put("player_4", new ImageIcon("img\\dxh\\right.png").getImage());
        mapImage.put("box1", new ImageIcon("img\\dxh\\box.png").getImage());
        mapImage.put("box2", new ImageIcon("img\\dxh\\boxPoint.png").getImage());
        mapImage.put("wall", new ImageIcon("img\\dxh\\wall.png").getImage());
        mapImage.put("way", new ImageIcon("img\\dxh\\way.png").getImage());
        mapImage.put("point", new ImageIcon("img\\dxh\\point.png").getImage());
    }

    public static void setLevel(Graphics g, int level) {
        if (mapData.isEmpty()) {
            return;
        }
        if (!mapData.containsKey(level)) {
            return;
        }
        String str_data = mapData.get(level);
        String[] arrLine = str_data.split(";");
        map = new int[arrLine.length][arrLine.length];
        int i = 0;
        for (String line : arrLine) {
            String[] d = line.split(",");
            int j = 0;
            for (String e : d) {
                map[i][j] = Integer.parseInt(e.trim());
                j++;
            }
            i++;
        }

        drawMainGame(g);
    }


    private static void drawMainGame(Graphics g) {
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
        drawUnit(g, mapImage.get(IMG_PLAYER_NAME), player_x, player_y, 0);
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
}
