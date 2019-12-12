package com.b07.store;

import com.b07.inventory.Item;
import com.b07.users.User;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * The Class SaleImpl.
 */
public class SaleImpl implements Sale, Serializable {

  private static final long serialVersionUID = -1425282910861475631L;
  private int id;
  private User user;
  private BigDecimal price;
  private HashMap<Item, Integer> itemMap;

  /**
   * Instantiates a new sale impl.
   */
  public SaleImpl() {
    this.id = -1;
    this.user = null;
    this.price = null;
    this.itemMap = new HashMap<Item, Integer>();
  }

  /**
   * Gets the id.
   *
   * @return the id
   */
  @Override
  public int getId() {
    return this.id;
  }

  /**
   * Sets the id.
   *
   * @param id the new id
   */
  @Override
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Gets the user.
   *
   * @return the user
   */
  @Override
  public User getUser() {
    return this.user;
  }

  /**
   * Sets the user.
   *
   * @param user the new user
   */
  @Override
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Gets the total price.
   *
   * @return the total price
   */
  @Override
  public BigDecimal getTotalPrice() {
    return this.price;
  }

  /**
   * Sets the total price.
   *
   * @param price the new total price
   */
  @Override
  public void setTotalPrice(BigDecimal price) {
    this.price = price;
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
  }

}
