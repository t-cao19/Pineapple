package com.b07.store;

import android.content.Context;
import android.widget.Toast;
import com.b07.inventory.Item;
import com.b07.users.Customer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Class ShoppingCart.
 */
public class ShoppingCart{

  private HashMap<Item, Integer> items;
  private Customer customer;
  private BigDecimal total = new BigDecimal(0);
  private static final BigDecimal TAXRATE = new BigDecimal("1.13");
  private Context context;

  /**
   * Instantiates a new shopping cart.
   *
   * @param customer the customer
   */
  public ShoppingCart(Customer customer, Context context) {
    this.customer = customer;
    items = new HashMap<Item, Integer>();
    this.total = new BigDecimal("0.00");
    this.context = context;
  }

  /**
   * Adds the item.
   *
   * @param item the item
   * @param quantity the quantity
   */
  public void addItem(Item item, int quantity) {
    int newQuantity = quantity;
    if (this.getItemById(item.getId()) != null) {
      item = this.getItemById(item.getId());
    }
    if (this.items.containsKey(item)) {
      int contains = this.items.get(item);
      newQuantity = contains + quantity;
    }
    this.items.put(item, newQuantity);
    total = total.add(item.getPrice().multiply(new BigDecimal(quantity)));
  }

  /**
   * Removes the item.
   *
   * @param item the item
   * @param quantity the quantity
   */
  public void removeItem(Item item, int quantity) {
    item = this.getItemById(item.getId());
    if (this.items.containsKey(item)) {
      int contains = this.items.get(item);
      if (contains == quantity) {
        this.items.remove(item);
        total = total.subtract(item.getPrice().multiply(new BigDecimal(quantity)));
      } else if (contains > quantity) {
        this.items.put(item, (contains - quantity));
        total = total.subtract(item.getPrice().multiply(new BigDecimal(quantity)));
      } else {
        System.out.println("Cannot remove more than is available!");
      }
    }
  }

  /**
   * Gets the item by id.
   *
   * @param id the id
   * @return the item by id
   */
  public Item getItemById(int id) {
    for (Item i : this.items.keySet()) {
      if (i.getId() == id) {
        return i;
      }
    }
    return null;
  }

  /**
   * Gets the items.
   *
   * @return the items
   */
  public List<Item> getItems() {
    List<Item> i = new ArrayList<Item>();
    for (Item t : this.items.keySet()) {
      i.add(t);
    }
    return i;

  }

  /**
   * Gets the item qty.
   *
   * @param item the item
   * @return the item qty
   */
  public Integer getItemQty(Item item) {
    return this.items.get(this.getItemById(item.getId()));
  }

  /**
   * Gets the customer.
   *
   * @return the customer
   */
  public Customer getCustomer() {
    return this.customer;
  }

  /**
   * Gets the total.
   *
   * @return the total
   */
  public BigDecimal getTotal() {
    return this.total.setScale(2, RoundingMode.HALF_UP);
  }

  /**
   * Gets the tax rate.
   *
   * @return the tax rate
   */
  public BigDecimal getTaxRate() {
    return TAXRATE;
  }

  /**
   * Checkout.
   *
   * @return true, if successful
   */
  public boolean checkout() {
    if (customer == null) {
      return false;
    }
    BigDecimal taxedtotal = this.getTotal().multiply(TAXRATE).setScale(2, RoundingMode.HALF_UP);

    com.b07.database.DatabaseSelectHelper selectDb = new com.b07.database.DatabaseSelectHelper(this.context);
    for (Item item : this.items.keySet()) {
      if (this.items.get(item) > selectDb.getInventoryQuantityHelper(item.getId())) {
        return false;
      }
    }

    com.b07.database.DatabaseUpdateHelper updateDb = new com.b07.database.DatabaseUpdateHelper(this.context);
    for (Item item : this.items.keySet()) { // update inventory
      if (!updateDb.updateInventoryQuantityHelper(
              (selectDb.getInventoryQuantityHelper(item.getId()) - this.items.get(item)),
              item.getId())) {

        return false;
      }
    }

    com.b07.database.DatabaseInsertHelper insertDb = new com.b07.database.DatabaseInsertHelper(this.context);
    long saleId = insertDb.insertSaleHelper(this.getCustomer().getId(), taxedtotal);
    for (Item item : this.items.keySet()) { // insert sale
      insertDb.insertItemizedSaleHelper(Math.toIntExact(saleId), item.getId(), this.getItemQty(item));
    }

    this.clearCart(); // clear cart

    return true;

  }

  /**
   * Clear cart.
   */
  public void clearCart() {
    this.items = new HashMap<Item, Integer>();
    this.total = new BigDecimal("0.00");
  }

}
