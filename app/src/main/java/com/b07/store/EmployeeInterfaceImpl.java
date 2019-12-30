package com.b07.store;

import android.content.Context;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.users.Employee;
import com.b07.users.Roles;
import java.sql.SQLException;
import java.util.List;

/**
 * The Class EmployeeInterfaceImpl.
 *
 * @author smetani1
 */
public class EmployeeInterfaceImpl implements EmployeeInterface {

  private Employee currentEmployee = null;
  private Inventory currentInventory = null;
  private Context context;

  /**
   * Instantiates a new employee interface.
   *
   * @param employee the employee
   * @param inventory the inventory
   */
  public EmployeeInterfaceImpl(Employee employee, Inventory inventory, Context context) {
    currentEmployee = employee;
    currentInventory = inventory;
    this.context = context;
  }

  /**
   * Instantiates a new employee interface without an employee.
   *
   * @param inventory the inventory
   */
  public EmployeeInterfaceImpl(Inventory inventory, Context context) {
    this.currentInventory = inventory;
    this.context = context;
  }

  @Override
  public void setCurrentEmployee(Employee employee) {
    this.currentEmployee = employee;
  }

  @Override
  public void setCurrentInventory(Inventory inventory) {
    this.currentInventory = inventory;
  }

  @Override
  public Inventory getCurrentInventory() {
    return this.currentInventory;
  }

  @Override
  public boolean hasCurrentEmployee() {
    if (this.currentEmployee != null) {
      return true;
    }
    return false;
  }

  @Override
  public boolean restockInventory(Item item, int quantity){
    com.b07.database.DatabaseUpdateHelper updateDb = new com.b07.database.DatabaseUpdateHelper(this.context);
    com.b07.database.DatabaseSelectHelper selectDb = new com.b07.database.DatabaseSelectHelper(this.context);
    return updateDb.updateInventoryQuantityHelper(
            quantity + selectDb.getInventoryQuantityHelper(item.getId()), item.getId());
//            DatabaseUpdateHelper.updateInventoryQuantity(
//        quantity + DatabaseSelectHelper.getInventoryQuantity(item.getId()), item.getId());

  }

  /**
   * Gets the role id.
   *
   * @param role the role
   * @return the role id
   */
  private int getRoleId(Roles role){
    com.b07.database.DatabaseSelectHelper selectDb = new com.b07.database.DatabaseSelectHelper(this.context);
    List<Integer> roleIds = selectDb.getRoleIdsHelper();
    for (int i : roleIds) {
      if (role.name().equalsIgnoreCase(selectDb.getRoleName(i))) {
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
   */
  public int createCustomer(String name, int age, String address, String password) {
    com.b07.database.DatabaseInsertHelper insertDb = new com.b07.database.DatabaseInsertHelper(this.context);
    int customerId = Math.toIntExact(insertDb.insertNewUserHelper(name, age, address, password));
    com.b07.database.DatabaseSelectHelper selectDb = new com.b07.database.DatabaseSelectHelper(this.context);
    return Math.toIntExact(insertDb.insertUserRoleHelper(
            customerId, selectDb.getRoleIdHelper(Roles.CUSTOMER.toString())));
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
    employeeId = com.b07.database.helper.DatabaseInsertHelper
            .insertNewUser(name, age, address, password);
    return com.b07.database.helper.DatabaseInsertHelper
            .insertUserRole(employeeId, this.getRoleId(Roles.EMPLOYEE));
  }
}
