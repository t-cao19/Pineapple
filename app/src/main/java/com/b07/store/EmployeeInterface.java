package com.b07.store;

import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.users.Employee;

public interface EmployeeInterface {

    /**
     * Sets the current employee.
     *
     * @param employee the new current employee
     */
    public void setCurrentEmployee(Employee employee);

    /**
     * Sets the current inventory.
     *
     * @param inventory the new current inventory
     */
    public void setCurrentInventory(Inventory inventory);

    /**
     * Gets the current inventory.
     *
     * @return the current inventory
     */
    public Inventory getCurrentInventory();

    /**
     * Checks for current employee.
     *
     * @return true, if there is a current employee
     */
    public boolean hasCurrentEmployee();

    /**
     * Restock inventory.
     *
     * @param item the item
     * @param quantity the quantity
     * @return true, if successful
     */
    public boolean restockInventory(Item item, int quantity);

    /**
     * Creates the customer.
     *
     * @param name the name
     * @param age the age
     * @param address the address
     * @param password the password
     * @return the int
     */
    public int createCustomer(String name, int age, String address, String password);
}
