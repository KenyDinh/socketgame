/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.element;

import game.server.common.CommonDefine;
import game.server.common.CommonMethod;

/**
 *
 * @author tkv-nhukhanhdinh
 */
public enum BombType {

    BOMB_1(40, 200, 1, 1, 0, 0),
    BOMB_2(41, 200, 2, 1, 0, 0),
    BOMB_3(42, 200, 2, 2, 0, 0),
    BOMB_4(43, 200, 3, 2, 0, 0),
    BOMB_5(44, 200, 3, 3, 0, 0),
    //    
    BOMB_6(45, 300, 1, 3, 0, 0),
    BOMB_7(46, 300, 3, 2, 0, 0),
    BOMB_8(47, 300, 3, 4, 0, 0),
    BOMB_9(48, 300, 4, 3, 0, 0),
    BOMB_10(49, 300, 4, 4, 0, 0),
    //    
    BOMB_11(50, 250, 1, 1, 1, 0),
    BOMB_12(51, 250, 2, 1, 1, 0),
    BOMB_13(52, 250, 3, 1, 1, 0),
    BOMB_14(53, 250, 2, 2, 2, 0),
    BOMB_15(54, 250, 3, 2, 2, 0),
    //    
    BOMB_16(55, 200, 2, 2, 0, 80),
    BOMB_17(56, 200, 2, 2, 1, 60),
    BOMB_18(57, 200, 3, 3, 1, 40),
    BOMB_19(58, 200, 4, 4, 0, 30),
    BOMB_20(59, 200, 4, 4, 1, 20),
    //    
    BOMB_21(60, 150, 2, 2, 2, 60),
    BOMB_22(61, 150, 2, 3, 2, 55),
    BOMB_23(62, 150, 3, 3, 2, 50),
    BOMB_24(63, 150, 4, 2, 2, 45),
    BOMB_25(64, 150, 3, 4, 2, 40),
    //    
    BOMB_26(65, 200, 3, 3, 2, 80),
    BOMB_27(66, 200, 4, 4, 2, 70),
    BOMB_28(67, 200, 5, 5, 2, 60),
    BOMB_29(68, 200, 6, 6, 2, 50);

    public static final int MIN_BOMB_VALUE = 40;
    public static final int MAX_BOMB_VALUE = 68;

    private int value;
    private int damage;
    private int horizontalLen;
    private int verticalLen;
    private int squareLen;
    private int recoverRatio;
    private int rarity;

    private BombType(int value, int damage, int horizontalLen, int verticalLen, int squareLen, int recoverRatio) {
        this.value = value;
        this.damage = damage;
        this.horizontalLen = horizontalLen;
        this.verticalLen = verticalLen;
        this.squareLen = squareLen;
        this.recoverRatio = recoverRatio;
    }

    public int getValue() {
        return value;
    }

    public int getDamage() {
        return damage;
    }

    public int getHorizontalLen() {
        return horizontalLen;
    }

    public int getVerticalLen() {
        return verticalLen;
    }

    public int getSquareLen() {
        return squareLen;
    }

    public int getRecoverRatio() {
        return recoverRatio;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setHorizontalLen(int horizontalLen) {
        this.horizontalLen = horizontalLen;
    }

    public void setVerticalLen(int verticalLen) {
        this.verticalLen = verticalLen;
    }

    public void setSquareLen(int squareLen) {
        this.squareLen = squareLen;
    }

    public void setRecoverRatio(int recoverRatio) {
        this.recoverRatio = recoverRatio;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(getValue()).append(",").append(getDamage()).append(",").append(getHorizontalLen()).append(",").append(getVerticalLen()).append(",").append(getSquareLen()).append(",").append(getRecoverRatio());
        return sb.toString();
    }

    public static BombType valueOf(int value) {
        for (BombType bt : BombType.values()) {
            if (bt.getValue() == value) {
                return bt;
            }
        }
        return BOMB_1;
    }

    public static void initBombTypeData() {
        String data = CommonMethod.getShopData();
        if (data.isEmpty()) {
            return;
        }
        for (String line : data.split(CommonDefine.BREAK_LINE)) {
            if (line.contains(CommonDefine.COMMA)) {
                String arr[] = line.split(CommonDefine.COMMA);
                if (!isValidNumber(arr[0]) || !isValidNumber(arr[1]) || !isValidNumber(arr[4]) || !isValidNumber(arr[5]) || !isValidNumber(arr[6]) || !isValidNumber(arr[7]) || !isValidNumber(arr[8])) {
                    continue;
                }
                int val = Integer.parseInt(arr[0].trim());
//                int price = Integer.parseInt(arr[1].trim());
//                String name = arr[2].trim();
//                String des = arr[3].trim();
                if(val >= MIN_BOMB_VALUE && val <= MAX_BOMB_VALUE){
                    int damage = Integer.parseInt(arr[4].trim());
                    int horLen = Integer.parseInt(arr[5].trim());
                    int verLen = Integer.parseInt(arr[6].trim());
                    int squLen = Integer.parseInt(arr[7].trim());
                    int recover = Integer.parseInt(arr[8].trim());
                    BombType bt = valueOf(val);
                    if(val != BOMB_1.getValue() && bt.getValue() == BOMB_1.getValue()){
                        continue;
                    }
                    bt.setDamage(damage);
                    bt.setHorizontalLen(horLen);
                    bt.setRecoverRatio(recover);
                    bt.setSquareLen(squLen);
                    bt.setVerticalLen(verLen);
                }
            }
        }
        ////
        for(BombType bombType : BombType.values()){
            CommonMethod.log("Init Bomb: " + bombType.toString());
        }
    }

    private static boolean isValidNumber(String s) {
        return s.matches("[0-9]+");
    }
}
