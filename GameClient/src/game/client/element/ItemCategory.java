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
public enum ItemCategory {
    SHOP_ITEM(1);

    private int id;

    private ItemCategory(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
