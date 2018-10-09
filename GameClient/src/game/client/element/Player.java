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
public class Player {

    private int value;
    private String name;
    private int cardNums;
    private int coin;

    public Player(int value, String name, int cardNums, int coin) {
        this.value = value;
        this.name = name;
        this.cardNums = cardNums;
        this.coin = coin;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        if(this.value < 0){
            this.value = 0;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCardNums() {
        return cardNums;
    }

    public void setCardNums(int cardNums) {
        this.cardNums = cardNums;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
        if(this.coin < 0){
            this.coin = 0;
        }
    }
    
    @Override
    public String toString(){
        return "Name: " + getName() + ", Card Num: " + getCardNums() + ", Value:" + getValue() + ", Coin:" + getCoin();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Player){
            Player p = (Player) obj;
            return p.getValue() == this.getValue();
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.value;
        return hash;
    }
    
}
