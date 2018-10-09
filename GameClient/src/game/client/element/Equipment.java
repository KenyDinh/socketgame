/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.client.element;

/**
 *
 * @author tkv-nhukhanhdinh
 */
public class Equipment {

    private int id;             //0
    private int price;          //1
    private String name;        //2
    private String descrip;     //3
    private int damage;         //4
    private int horizontalLen;  //5
    private int verticalLen;    //6
    private int squareLen;      //7
    private int recoverRatio;   //8
    private int carryNum;       //9
    private int category;       //10
    private int type;           //11
    private int param_1;        //12
    private int param_2;        //13
    private int param_3;        //14
    private int param_4;        //15
    private int param_5;        //16

    public Equipment(int id, int price, String name, String descrip, int damage, int horizontalLen, int verticalLen, int squareLen, int recoverRatio, int carryNum, int category, int type, int param_1, int param_2, int param_3, int param_4, int param_5) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.descrip = descrip;
        this.damage = damage;
        this.horizontalLen = horizontalLen;
        this.verticalLen = verticalLen;
        this.squareLen = squareLen;
        this.recoverRatio = recoverRatio;
        this.carryNum = carryNum;
        this.category = category;
        this.type = type;
        this.param_1 = param_1;
        this.param_2 = param_2;
        this.param_3 = param_3;
        this.param_4 = param_4;
        this.param_5 = param_5;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getHorizontalLen() {
        return horizontalLen;
    }

    public void setHorizontalLen(int horizontalLen) {
        this.horizontalLen = horizontalLen;
    }

    public int getVerticalLen() {
        return verticalLen;
    }

    public void setVerticalLen(int verticalLen) {
        this.verticalLen = verticalLen;
    }

    public int getSquareLen() {
        return squareLen;
    }

    public void setSquareLen(int squareLen) {
        this.squareLen = squareLen;
    }

    public int getRecoverRatio() {
        return recoverRatio;
    }

    public void setRecoverRatio(int recoverRatio) {
        this.recoverRatio = recoverRatio;
    }

    public int getCarryNum() {
        return carryNum;
    }

    public void setCarryNum(int carryNum) {
        this.carryNum = carryNum;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getParam_1() {
        return param_1;
    }

    public void setParam_1(int param_1) {
        this.param_1 = param_1;
    }

    public int getParam_2() {
        return param_2;
    }

    public void setParam_2(int param_2) {
        this.param_2 = param_2;
    }

    public int getParam_3() {
        return param_3;
    }

    public void setParam_3(int param_3) {
        this.param_3 = param_3;
    }

    public int getParam_4() {
        return param_4;
    }

    public void setParam_4(int param_4) {
        this.param_4 = param_4;
    }

    public int getParam_5() {
        return param_5;
    }

    public void setParam_5(int param_5) {
        this.param_5 = param_5;
    }

}
