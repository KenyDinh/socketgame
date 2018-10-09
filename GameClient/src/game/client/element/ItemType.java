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
public enum ItemType {
    RECOVERY(1),
    SUPER_KICK(2),
    SUPER_EAT(3),
    PROTECT(4),
    NINJA(5),
    BOMB_CHARGE_QUICKLY(6),
    BAG_INCREASE(7),
    LUCKY_CHEST(8),
    CHANGE_DIRECTION(9),
    MONEY(10),
    SPEED_UP(11),
    ;

    private int id;

    private ItemType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
