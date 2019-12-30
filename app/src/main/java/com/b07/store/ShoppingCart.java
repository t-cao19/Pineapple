package com.b07.store;

import com.b07.inventory.Item;
import com.b07.users.Customer;
import java.math.BigDecimal;
import java.util.List;


public interface ShoppingCart {

    /**
     * Adds the item.
     *
     * @param item the item
     * @param quantity the quantity
     */
    public void addItem(Item item, int quantity);

    /**
     * Removes the item.
     *
     * @param item the item
     * @param quantity the quantity
     */
    public void removeItem(Item item, int quantity);

    /**
     * Gets the item by id.
     *
     * @param id the id
     * @return the item by id
     */
    public Item getItemById(int id);

    /**
     * Gets the items.
     *
     * @return the items
     */
    public List<Item> getItems();

    /**
     * Gets the item qty.
     *
     * @param item the item
     * @return the item qty
     */
    public Integer getItemQty(Item item);

    /**
     * Gets the customer.
     *
     * @return the customer
     */
    public Customer getCustomer();

    /**
     * Gets the total.
     *
     * @return the total
     */
    public BigDecimal getTotal();

    /**
     * Gets the tax rate.
     *
     * @return the tax rate
     */
    public BigDecimal getTaxRate();

    /**
     * Checkout.
     *
     * @return true, if successful
     */
    public boolean checkout();

    /**
     * Clear cart.
     */
    public void clearCart();

}
