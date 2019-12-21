package com.b07.inventory;

import java.math.BigDecimal;

/**
 * The Interface Item.
 */
public interface Item {

  /**
   * get the Id of the item.
   * 
   * @return the Id of the item.
   */
  public int getId();

  /**
   * sets the id of the Item.
   *
   * @param id the new id
   */
  public void setId(int id);

  /**
   * gets the name of the Item.
   * 
   * @return the name of the Item.
   */
  public String getName();

  /**
   * sets the name of the Item.
   *
   * @param name the new name
   */
  public void setName(String name);

  /**
   * gets the price of the Item.
   * 
   * @return the price of the item.
   */
  public BigDecimal getPrice();

  /**
   * sets the price of the Item.
   *
   * @param price the new price
   */
  public void setPrice(BigDecimal price);
}
