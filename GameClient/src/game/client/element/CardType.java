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
public enum CardType {
    INVALID(0),
    SPADE(1),
    CLUB(2),
    DIAMOND(3),
    HEART(4),
    ;
    private int value;

    private CardType(int value) {
        this.value = value;
    }
    
    public int getValue(){
        return value;
    }
    
    public static CardType valueOf(int value){
        for(CardType type : CardType.values()){
            if(type.getValue() == value){
                return type;
            }
        }
        return INVALID;
    }
}
