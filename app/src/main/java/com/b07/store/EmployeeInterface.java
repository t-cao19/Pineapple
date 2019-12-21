package com.b07.store;

import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.users.Employee;
import com.b07.users.Roles;
import java.sql.SQLException;
import java.util.List;

/**
 * The Class EmployeeInterface.
 *
 * @author smetani1
 */
public class EmployeeInterface {

  private Employee currentEmployee = null;
  private Inventory currentInventory = null;

  /**
   * Instantiates a new employee interface.
   *
   * @param employee the employee
   * @param inventory the inventory
   */
  public EmployeeInterface(Employee employee, Inventory inventory) {
    currentEmployee = employee;
    currentInventory = inventory;
  }

  /**
   * Instantiates a new employee interface.
   *
   * @param inventory the inventory
   */
  public EmployeeInterface(Inventory inventory) {
    this.currentInventory = inventory;
  }

  /**
   * Sets the current employee.
   *
   * @param employee the new current employee
   */
  public void setCurrentEmployee(Employee employee) {
    this.currentEmployee = employee;
  }

  /**
   * Sets the current inventory.
   *
   * @param inventory the new current inventory
   */
  public void setCurrentInventory(Inventory inventory) {
    this.currentInventory = inventory;
  }

  /**
   * Gets the current inventory.
   *
   * @return the current inventory
   */
  public Inventory getCurrentInventory() {
    return this.currentInventory;
  }

  /**
   * Checks for current employee.
   *
   * @return true, if successful
   */
  public boolean hasCurrentEmployee() {
    if (this.currentEmployee != null) {
      return true;
    }
    return false;
  }

  /**
   * Restock inventory.
   *
   * @param item the item
   * @param quantity the quantity
   * @return true, if successful
   * @throws SQLException the SQL exception
   */
  public boolean restockInventory(Item item, int quantity) throws SQLException {
    return DatabaseUpdateHelper.updateInventoryQuantity(
        quantity + DatabaseSelectHelper.getInventoryQuantity(item.getId()), item.getId());

  }

  /**
   * Gets the role id.
   *
   * @param role the role
   * @return the role id
   * @throws SQLException the SQL exception
   */
  private int getRoleId(Roles role) throws SQLException {
    List<Integer> roleIds = DatabaseSelectHelper.getRoleIds();
    for (int i : roleIds) {
      if (role.name().equalsIgnoreCase(DatabaseSelectHelper.getRoleName(i))) {
        return i;
      }
    }

    return -1;
  }

  /**
   * Creates the customer.
   *
   * @param name the name
   * @param age the age
   * @param address the address
   * @param password the password
   * @return the int
   * @throws SQLException the SQL exception
   */
  public int createCustomer(String name, int age, String address, String password)
      throws SQLException {
    int customerId;
    customerId = DatabaseInsertHelper.insertNewUser(name, age, address, password);
    return DatabaseInsertHelper.insertUserRole(customerId,
        DatabaseSelectHelper.getRoleId(Roles.CUSTOMER.name()));
  }

  /**
   * Creates the employee.
   *
   * @param name the name
   * @param age the age
   * @param address the address
   * @param password the password
   * @return the int
   * @throws SQLException the SQL exception
   */
  public int createEmployee(String name, int age, String address, String password)
      throws SQLException {
    int employeeId;
    employeeId = DatabaseInsertHelper.insertNewUser(name, age, address, password);
    return DatabaseInsertHelper.insertUserRole(employeeId, this.getRoleId(Roles.EMPLOYEE));
  }
}
