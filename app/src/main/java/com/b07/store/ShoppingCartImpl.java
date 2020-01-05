package com.b07.store;

import android.content.Context;
import com.b07.inventory.Item;
import com.b07.users.Customer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Class ShoppingCartImpl.
 */
public class ShoppingCartImpl implements ShoppingCart {

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
  public ShoppingCartImpl(Customer customer, Context context) {
    this.customer = customer;
    items = new HashMap<Item, Integer>();
    this.total = new BigDecimal("0.00");
    this.context = context;
  }

  @Override
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

  @Override
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

  @Override
  public Item getItemById(int id) {
    for (Item i : this.items.keySet()) {
      if (i.getId() == id) {
        return i;
      }
    }
    return null;
  }

  @Override
  public List<Item> getItems() {
    List<Item> i = new ArrayList<Item>();
    for (Item t : this.items.keySet()) {
      i.add(t);
    }
    return i;

  }

  @Override
  public Integer getItemQty(Item item) {
    return this.items.get(this.getItemById(item.getId()));
  }

  @Override
  public Customer getCustomer() {
    return this.customer;
  }

  @Override
  public BigDecimal getTotal() {
    return this.total.setScale(2, RoundingMode.HALF_UP);
  }

  @Override
  public BigDecimal getTaxRate() {
    return TAXRATE;
  }

  @Override
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

  @Override
  public void clearCart() {
    this.items = new HashMap<Item, Integer>();
    this.total = new BigDecimal("0.00");
  }

}
