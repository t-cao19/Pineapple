package com.b07.inventory;

import java.io.Serializable;
import java.util.HashMap;

/**
 * The Class InventoryImpl.
 *
 * @author smetani1
 */
public class InventoryImpl implements Inventory, Serializable {

  private static final long serialVersionUID = -433306656742153533L;
  private HashMap<Item, Integer> itemMap;
  private int total;

  /**
   * Constructor for InventoryImpl.
   */
  public InventoryImpl() {
    this.itemMap = new HashMap<Item, Integer>();
    this.total = 0;
  }

  /**
   * Gets the item map.
   *
   * @return the item map
   */
  @Override
  public HashMap<Item, Integer> getItemMap() {
    return this.itemMap;
  }

  /**
   * Sets the item map.
   *
   * @param itemMap the item map
   */
  @Override
  public void setItemMap(HashMap<Item, Integer> itemMap) {
    this.itemMap = itemMap;
    int count = 0;
    for (Item i : itemMap.keySet()) {
      count += itemMap.get(i);
    }
    this.total = count;
  }

  /**
   * Update map.
   *
   * @param item the item
   * @param value the value
   */
  @Override
  public void updateMap(Item item, Integer value) {
    this.itemMap.put(item, value);
    this.total += value;
  }

  /**
   * Gets the total items.
   *
   * @return the total items
   */
  @Override
  public int getTotalItems() {
    return this.total;
  }

  /**
   * Sets the total items.
   *
   * @param total the new total items
   */
  @Override
  public void setTotalItems(int total) {
    this.total = total;
  }

}
