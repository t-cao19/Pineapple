package com.b07.store;

import com.b07.inventory.Item;
import com.b07.users.User;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * The Interface Sale.
 */
public interface Sale {

  /**
   * gets the id of the Sale.
   * 
   * @return id of the sale.
   */
  public int getId();

  /**
   * sets the id of the sale.
   *
   * @param id the new id
   */
  public void setId(int id);

  /**
   * gets the User of the sale.
   * 
   * @return the user of the sale
   */
  public User getUser();

  /**
   * sets the user of the sale.
   *
   * @param user the new user
   */
  public void setUser(User user);

  /**
   * gets the total price of the sale.
   * 
   * @return total price of sale.
   */
  public BigDecimal getTotalPrice();

  /**
   * sets the total price of the sale.
   *
   * @param price the new total price
   */
  public void setTotalPrice(BigDecimal price);

  /**
   * returns the cart of the sale.
   *
   * @return the item map
   */
  public HashMap<Item, Integer> getItemMap();

  /**
   * sets the user's item cart.
   *
   * @param itemMap the item map
   */
  public void setItemMap(HashMap<Item, Integer> itemMap);

}
