/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.client.element;

import java.util.Objects;

/**
 *
 * @author tkv-nhukhanhdinh
 */
public class Card {

    public static final int MAX_VALUE_TYPE = 4;
    public static final int MIN_VALUE_TYPE = 1;
    public static final int MAX_VALUE_NUM = 13;
    public static final int MIN_VALUE_NUM = 1;
    public static final int MIN_CARD_SEPARATOR = 3;
    public static final int CARD_TYPE_RED_COLOR = 1;
    public static final int CARD_TYPE_BLACK_COLOR = 0;
    

    private int num;
    private CardType type;
    private boolean selected;
    private boolean avaiable;

    public Card(int num, CardType type) {
        this.num = num;
        this.type = type;
        this.selected = false;
        this.avaiable = true;
    }

    public int getFakeNum() {
        if (num < MIN_CARD_SEPARATOR) {
            return num + MAX_VALUE_NUM;
        }
        return num;
    }

    public int getRealNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isAvaiable() {
        return avaiable;
    }

    public void setAvaiable(boolean avaiable) {
        this.avaiable = avaiable;
    }
    
    public int getCardTypeColor(){
        if(getType().getValue() == CardType.CLUB.getValue() || getType().getValue() == CardType.SPADE.getValue()){
            return CARD_TYPE_BLACK_COLOR;
        }
        return CARD_TYPE_RED_COLOR;
    }

    @Override
    public String toString(){
        return getRealNum() + "_" + getType().getValue();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Card) {
            Card c = (Card) obj;
            return (c.getRealNum() == this.getRealNum() && c.getType().getValue() == this.getType().getValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.num;
        hash = 97 * hash + Objects.hashCode(this.type);
        return hash;
    }
    
}
