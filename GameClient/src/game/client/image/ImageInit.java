/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.client.image;

import game.client.element.Card;
import game.client.socket.BoomGameClient;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author tkv-nhukhanhdinh
 */
public class ImageInit {

    public static boolean isMissingImage;

    public static Map<String, Image> initCardImage() {
        Map<String, Image> imageCards = new HashMap<>();
        for (int type = Card.MIN_VALUE_TYPE; type <= Card.MAX_VALUE_TYPE; type++) {
            for (int num = Card.MIN_VALUE_NUM; num <= Card.MAX_VALUE_NUM; num++) {
                imageCards.put(num + "_" + type, getImage(ImageInit.class.getClass().getResource("/game/client/image/card/" + num + "_" + type + ".png")));
            }
        }
        for (int i = 0; i <= 30; i++) {
            imageCards.put("count_down_" + i, getImage(ImageInit.class.getClass().getResource("/game/client/image/card/count_down_" + i + ".png")));
        }
        for(int i = 0; i < 10; i++){
            imageCards.put("win_" + i, getImage(ImageInit.class.getClass().getResource("/game/client/image/card/win_" + i + ".png")));
        }
        imageCards.put("card_back", getImage(ImageInit.class.getClass().getResource("/game/client/image/card/card_back.png")));
        imageCards.put("player", getImage(ImageInit.class.getClass().getResource("/game/client/image/baucua/user.png")));
        imageCards.put("host", getImage(ImageInit.class.getClass().getResource("/game/client/image/baucua/host.png")));
        imageCards.put("play_game", getImage(ImageInit.class.getClass().getResource("/game/client/image/card/play_game.png")));
        imageCards.put("play_game_hover", getImage(ImageInit.class.getClass().getResource("/game/client/image/card/play_game_hover.png")));
        imageCards.put("play_game_press", getImage(ImageInit.class.getClass().getResource("/game/client/image/card/play_game_press.png")));
        if (isMissingImage) {
            isMissingImage = false;
            return null;
        }
        return imageCards;
    }

    public static Map<String, Image> initDxhImage() {
        Map<String, Image> imageDxhs = new HashMap<>();
        imageDxhs.put("player_2", getImage(ImageInit.class.getClass().getResource("/game/client/image/dxh/top.png")));
        imageDxhs.put("player_1", getImage(ImageInit.class.getClass().getResource("/game/client/image/dxh/bottom.png")));
        imageDxhs.put("player_3", getImage(ImageInit.class.getClass().getResource("/game/client/image/dxh/left.png")));
        imageDxhs.put("player_4", getImage(ImageInit.class.getClass().getResource("/game/client/image/dxh/right.png")));
        imageDxhs.put("box1", getImage(ImageInit.class.getClass().getResource("/game/client/image/dxh/box.png")));
        imageDxhs.put("box2", getImage(ImageInit.class.getClass().getResource("/game/client/image/dxh/boxPoint.png")));
        imageDxhs.put("wall", getImage(ImageInit.class.getClass().getResource("/game/client/image/dxh/wall.png")));
        imageDxhs.put("way", getImage(ImageInit.class.getClass().getResource("/game/client/image/dxh/way.png")));
        imageDxhs.put("point", getImage(ImageInit.class.getClass().getResource("/game/client/image/dxh/point.png")));
        if (isMissingImage) {
            isMissingImage = false;
            return null;
        }
        return imageDxhs;
    }

    public static Map<String, Image> initBauCuaImage() {
        Map<String, Image> imageBauCuas = new HashMap<>();
        imageBauCuas.put("desk", getImage(ImageInit.class.getClass().getResource("/game/client/image/baucua/bau_cua.png")));
        imageBauCuas.put("user", getImage(ImageInit.class.getClass().getResource("/game/client/image/baucua/user.png")));
        imageBauCuas.put("plate", getImage(ImageInit.class.getClass().getResource("/game/client/image/baucua/plate.png")));
        imageBauCuas.put("bowl", getImage(ImageInit.class.getClass().getResource("/game/client/image/baucua/bowl.png")));

        imageBauCuas.put("huou", getImage(ImageInit.class.getClass().getResource("/game/client/image/baucua/_huou.png")));
        imageBauCuas.put("bau", getImage(ImageInit.class.getClass().getResource("/game/client/image/baucua/_bau.png")));
        imageBauCuas.put("ga", getImage(ImageInit.class.getClass().getResource("/game/client/image/baucua/_ga.png")));
        imageBauCuas.put("ca", getImage(ImageInit.class.getClass().getResource("/game/client/image/baucua/_ca.png")));
        imageBauCuas.put("cua", getImage(ImageInit.class.getClass().getResource("/game/client/image/baucua/_cua.png")));
        imageBauCuas.put("tom", getImage(ImageInit.class.getClass().getResource("/game/client/image/baucua/_tom.png")));
        if (isMissingImage) {
            isMissingImage = false;
            return null;
        }
        return imageBauCuas;
    }

    public static Map<String, Image> initBoomImage() {
        Map<String, Image> imageBoom = new HashMap<>();
        imageBoom.put("super-man", getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/super-man.png")));
        imageBoom.put("setting", getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/setting.png")));
        imageBoom.put("ready", getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/ready.png")));
        imageBoom.put("not-ready", getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/not-ready.png")));
        imageBoom.put("fight", getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/fight.png")));
        for (int i = 1; i <= 5; i++) {
            imageBoom.put("time_load_" + i, getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/time_load_" + i + ".png")));
        }
        imageBoom.put("box", getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/box.png")));
        imageBoom.put("way", getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/way.png")));
        imageBoom.put("wall", getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/wall.png")));
        imageBoom.put("fire_1", getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/fire_1.png")));
        imageBoom.put("fire_2", getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/fire_2.png")));
        imageBoom.put("item_it", getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/item_it.png")));
        imageBoom.put("item_bm_ef", getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/item_bm_ef.png")));
        imageBoom.put("coin", getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/coin.png")));
        imageBoom.put("hp", getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/hp.png")));
        imageBoom.put("rip", getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/rip.png")));
        imageBoom.put("back_side", getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/back_side.png")));
        imageBoom.put("next_side", getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/next_side.png")));
        for (int i = BoomGameClient.ITEM_RECOVERY_1; i <= BoomGameClient.ITEM_MAX_VALUE; i++) {
            if (!BoomGameClient.isBombItem(i)) {
                imageBoom.put("item_" + i, getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/item_" + i + ".png")));
            }
        }
        imageBoom.put("item_100", getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/item_100.png")));
        for (int i = BoomGameClient.BOOM_1; i <= BoomGameClient.BOOM_29; i++) {
            imageBoom.put("bomb_" + i, getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/bomb_" + i + ".png")));
        }
        for (int i = 1; i <= BoomGameClient.OTHER_COUNT; i++) {
            imageBoom.put("other_" + i, getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/other_" + i + ".png")));
        }

        imageBoom.put("player_" + BoomGameClient.STATUS_UP, getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/p_top.png")));
        imageBoom.put("player_" + BoomGameClient.STATUS_RIGHT, getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/p_right.png")));
        imageBoom.put("player_" + BoomGameClient.STATUS_DOWN, getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/p_bottom.png")));
        imageBoom.put("player_" + BoomGameClient.STATUS_LEFT, getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/p_left.png")));
        imageBoom.put("enemy_" + BoomGameClient.STATUS_UP, getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/e_top.png")));
        imageBoom.put("enemy_" + BoomGameClient.STATUS_RIGHT, getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/e_right.png")));
        imageBoom.put("enemy_" + BoomGameClient.STATUS_DOWN, getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/e_bottom.png")));
        imageBoom.put("enemy_" + BoomGameClient.STATUS_LEFT, getImage(ImageInit.class.getClass().getResource("/game/client/image/boom/e_left.png")));
        if (isMissingImage) {
            isMissingImage = false;
            return null;
        }
        return imageBoom;
    }

    private static Image getImage(String path) {
        File file = new File(path);
        if (file.exists()) {
            try {
                Image img = ImageIO.read(file);
                //CommonMethod.log("Init Image " + path + " Successfully!");
                return img;
            } catch (IOException ex) {
                //CommonMethod.log("Init Image " + path + " Encountered Error!");
            }
        } else {
            //CommonMethod.log("Image " + path + " Not Found!");
        }
        isMissingImage = true;
        return null;
    }

    private static Image getImage(URL url) {
        try {
            ImageIcon imgic = new ImageIcon(url);
            Image img = imgic.getImage();
            if (img != null) {
                return img;
            }
        } catch (NullPointerException e) {
            isMissingImage = true;
        }
        return null;
    }
}
