package com.b07.inventory;

import java.util.HashMap;

/**
 * The Interface Inventory.
 */
public interface Inventory {

  /**
   * gets the Item map of the inventory.
   * 
   * @return item map.
   */
  public HashMap<Item, Integer> getItemMap();

  /**
   * sets the inventory item map.
   *
   * @param itemMap the item map
   */
  public void setItemMap(HashMap<Item, Integer> itemMap);

  /**
   * updates the item map with an item and its value.
   *
   * @param item the item
   * @param value the value
   */
  public void updateMap(Item item, Integer value);

  /**
   * gets the total items in the inventory.
   * 
   * @return total items in inventory.
   */
  public int getTotalItems();

  /**
   * sets the total items in the inventory.
   *
   * @param total the new total items
   */
  public void setTotalItems(int total);

}
