package com.b07.database.helper;

import com.b07.database.DatabaseSelector;
import com.b07.inventory.Inventory;
import com.b07.inventory.InventoryImpl;
import com.b07.inventory.Item;
import com.b07.inventory.ItemImpl;
import com.b07.store.Sale;
import com.b07.store.SaleImpl;
import com.b07.store.SalesLog;
import com.b07.store.SalesLogImpl;
import com.b07.users.Admin;
import com.b07.users.Customer;
import com.b07.users.Employee;
import com.b07.users.Roles;
import com.b07.users.User;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Class DatabaseSelectHelper.
 */
public class DatabaseSelectHelper extends DatabaseSelector {

  /**
   * gets a list of roleIds from the database.
   *
   * @return the role ids
   * @throws SQLException the SQL exception
   */
  public static List<Integer> getRoleIds() throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getRoles(connection);
    List<Integer> ids = new ArrayList<>();
    while (results.next()) {
      ids.add(results.getInt("ID"));
    }
    results.close();
    connection.close();
    return ids;
  }

  /**
   * Return the role id of the role.
   * 
   * @param name the name of the role
   * @return the roleId of the role, -1 if role not found
   * @throws SQLException on failure
   */
  public static int getRoleId(String name) throws SQLException {
    List<Integer> roleIds = getRoleIds();

    for (int i = 0; i < roleIds.size(); i++) {
      if (getRoleName(roleIds.get(i)).equals(name)) {
        return roleIds.get(i);
      }
    }

    return -1;
  }

  /**
   * gets the role name based on the role id.
   *
   * @param roleId the role id
   * @return role
   * @throws SQLException the SQL exception
   */
  public static String getRoleName(int roleId) throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    String role = DatabaseSelector.getRole(roleId, connection);
    connection.close();
    return role;
  }

  /**
   * gets the user role id based on the user id.
   *
   * @param userId the user id
   * @return roleId
   * @throws SQLException the SQL exception
   */
  public static int getUserRoleId(int userId) throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    int roleId = DatabaseSelector.getUserRole(userId, connection);
    connection.close();
    return roleId;
  }

  /**
   * gets a list of users who have the same roleId.
   *
   * @param roleId the role id
   * @return a list of users with the same roleId
   * @throws SQLException the SQL exception
   */
  public static List<Integer> getUsersByRole(int roleId) throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getUsersByRole(roleId, connection);
    List<Integer> userIds = new ArrayList<>();
    while (results.next()) {
      int userId = results.getInt("USERID");
      userIds.add(userId);
    }
    results.close();
    connection.close();
    return userIds;

  }

  /**
   * gets the details of the users and returns them in a list.
   *
   * @return a list of Users
   * @throws SQLException the SQL exception
   */
  public static List<User> getUsersDetails() throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getUsersDetails(connection);
    List<User> users = new ArrayList<>();

    int id;
    int age;
    String name;
    String address;
    while (results.next()) {
      id = results.getInt("ID");
      age = results.getInt("AGE");
      name = results.getString("NAME");
      address = results.getString("ADDRESS");

      int roleId = getUserRole(id, connection);

      User tempUser = null;

      if (Roles.ADMIN.name().equalsIgnoreCase(getRoleName(roleId))) {
        tempUser = new Admin(id, name, age, address);
      } else if (Roles.CUSTOMER.name().equalsIgnoreCase(getRoleName(roleId))) {
        tempUser = new Customer(id, name, age, address);
      } else if (Roles.EMPLOYEE.name().equalsIgnoreCase(getRoleName(roleId))) {
        tempUser = new Employee(id, name, age, address);
      }

      if (tempUser != null) {
        users.add(tempUser);
      }

    }
    results.close();
    connection.close();
    return users;
  }

  /**
   * gets the user details.
   *
   * @param userId the user id
   * @return the user details
   * @throws SQLException the SQL exception
   * @returns the user
   */
  public static User getUserDetails(int userId) throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getUserDetails(userId, connection);

    User user = null;

    int id = 0;
    int age = 0;
    String name = null;
    String address = null;

    while (results.next()) {

      id = results.getInt("ID");
      name = results.getString("NAME");
      age = results.getInt("AGE");
      address = results.getString("ADDRESS");
    }
    results.close();

    if (id > 0) {

      int roleId = getUserRoleId(id);

      if (getRoleName(roleId).equalsIgnoreCase(Roles.ADMIN.name())) {
        user = new Admin(id, name, age, address);
      }
      if (getRoleName(roleId).equalsIgnoreCase(Roles.EMPLOYEE.name())) {
        user = new Employee(id, name, age, address);
      }
      if (getRoleName(roleId).equalsIgnoreCase(Roles.CUSTOMER.name())) {
        user = new Customer(id, name, age, address);
      }
    }

    connection.close();
    return user;
  }

  /**
   * gets the password based on the user id.
   *
   * @param userId the user id
   * @return password
   */
  public static String getPassword(int userId) {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    String password = null;
    try {
      password = DatabaseSelector.getPassword(userId, connection);
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return password;
  }

  /**
   * gets all the items.
   *
   * @return a list of items
   * @throws SQLException the SQL exception
   */
  public static List<Item> getAllItems() throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getAllItems(connection);
    List<Item> items = new ArrayList<>();
    int id = 0;
    String name = null;
    BigDecimal price = null;

    while (results.next()) {
      id = results.getInt("ID");
      name = results.getString("NAME");
      price = new BigDecimal(results.getString("PRICE"));

      Item tempItem = new ItemImpl();
      tempItem.setId(id);
      tempItem.setName(name);
      tempItem.setPrice(price);

      items.add(tempItem);
    }

    results.close();
    connection.close();
    return items;
  }

  /**
   * gets a specific item based on its id.
   *
   * @param itemId the item id
   * @return item
   * @throws SQLException the SQL exception
   */
  public static Item getItem(int itemId) throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getItem(itemId, connection);
    int id = -1;
    String name = null;
    BigDecimal price = null;

    while (results.next()) {
      id = results.getInt("ID");
      name = results.getString("NAME");
      price = new BigDecimal(results.getString("PRICE"));
    }

    results.close();
    connection.close();

    if (id == -1 | name == null | price == null) {
      System.out.println("Such item does not exist");
      return null;
    }

    Item tempItem = new ItemImpl();
    tempItem.setId(id);
    tempItem.setName(name);
    tempItem.setPrice(price);

    return tempItem;
  }

  /**
   * gets the inventory.
   *
   * @return the inventory
   * @throws SQLException the SQL exception
   * @returns the inventory
   */
  public static Inventory getInventory() throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getInventory(connection);
    int itemId = -1;
    String quantity = null;

    InventoryImpl inv = new InventoryImpl();

    while (results.next()) {
      itemId = results.getInt("ITEMID");
      quantity = results.getString("QUANTITY");
      Item item = getItem(itemId);
      inv.updateMap(item, Integer.parseInt(quantity));
    }

    results.close();
    connection.close();
    return inv;
  }

  /**
   * gets the quantity of item in inventory.
   *
   * @param itemId the item id
   * @return quantity
   * @throws SQLException the SQL exception
   */
  public static int getInventoryQuantity(int itemId) throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    int quantity = DatabaseSelector.getInventoryQuantity(itemId, connection);
    connection.close();
    return quantity;
  }

  /**
   * gets the sales log.
   *
   * @return sales log
   * @throws SQLException the SQL exception
   */
  public static SalesLog getSales() throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getSales(connection);

    SalesLogImpl saleLog = new SalesLogImpl();
    ArrayList<Sale> salesList = new ArrayList<Sale>();

    while (results.next()) {
      Sale sale = getItemizedSaleById(results.getInt("ID"));
      salesList.add(sale);
    }

    saleLog.setSales(salesList);

    results.close();
    connection.close();
    return saleLog;
  }

  /**
   * gets the sales log based on the sale id.
   *
   * @param saleId the sale id
   * @return Sale
   * @throws SQLException the SQL exception
   */
  public static Sale getSaleById(int saleId) throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getSaleById(saleId, connection);
    Sale s = null;

    while (results.next()) {
      s = new SaleImpl();
      s.setId(results.getInt("ID"));
      s.setUser(DatabaseSelectHelper.getUserDetails(results.getInt("USERID")));
      s.setTotalPrice(new BigDecimal(results.getString("TOTALPRICE")));
    }

    results.close();
    connection.close();
    return s;
  }

  /**
   * gets a list of sales to specific user.
   *
   * @param userId the user id
   * @return list of sales
   * @throws SQLException the SQL exception
   */
  public static List<Sale> getSalesToUser(int userId) throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelectHelper.getSalesToUser(userId, connection);
    List<Sale> sales = new ArrayList<>();

    while (results.next()) {
      Sale s = new SaleImpl();
      s.setId(results.getInt("ID"));
      s.setUser(DatabaseSelectHelper.getUserDetails(results.getInt("USERID")));
      s.setTotalPrice(new BigDecimal(results.getString("TOTALPRICE")));

      sales.add(s);
    }

    results.close();
    connection.close();
    return sales;
  }

  /**
   * gets an itemized sale based on the sale id.
   *
   * @param saleId the sale id
   * @return sale
   * @throws SQLException the SQL exception
   */
  public static Sale getItemizedSaleById(int saleId) throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getItemizedSaleById(saleId, connection);
    Sale s = getSaleById(saleId);

    while (results.next()) {
      Item itm = DatabaseSelectHelper.getItem(results.getInt("ITEMID"));
      if (itm != null) {
        s.getItemMap().put(itm, results.getInt("QUANTITY"));
      }
    }

    results.close();
    connection.close();
    return s;
  }

  /**
   * gets a sales log of itemized sales.
   *
   * @return SalesLog
   * @throws SQLException the SQL exception
   */
  public static SalesLog getItemizedSales() throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getItemizedSales(connection);

    SalesLogImpl salesLog = new SalesLogImpl();
    HashMap<Integer, Integer> itemMaps = new HashMap<Integer, Integer>();
    BigDecimal total = new BigDecimal(0);
    List<Sale> salesList = new ArrayList<Sale>();


    while (results.next()) {
      int itemId = results.getInt("ITEMID");
      int qty = results.getInt("QUANTITY");
      if (itemMaps.containsKey(itemId)) {
        int oldQty = itemMaps.get(itemId);
        itemMaps.put(itemId, qty + oldQty);
      } else {
        itemMaps.put(itemId, qty);
      }

      total = total.add(getItem(itemId).getPrice().multiply(new BigDecimal(qty)));

      Sale sale = getItemizedSaleById(results.getInt("SALEID"));
      salesList.add(sale);
    }

    salesLog.setItemMaps(itemMaps);
    salesLog.setGrandTotal(total);


    salesLog.setSales(salesList);
    results.close();
    connection.close();
    return salesLog;
  }

  /**
   * Combines sales log of Sales and Itemized Sales for a full version.
   * 
   * @return salesLog
   * @throws SQLException on failure
   */
  public static SalesLog consolidateSalesLog() throws SQLException {
    SalesLogImpl sales = (SalesLogImpl) getSales();
    SalesLogImpl itemizedSales = (SalesLogImpl) getItemizedSales();
    SalesLogImpl salesLog = new SalesLogImpl();

    salesLog.setSales(sales.getSales());
    salesLog.setItemMaps(itemizedSales.getItemMaps());
    salesLog.setGrandTotal(itemizedSales.getGrandTotal());

    return salesLog;
  }

  /*
   * 
   * PHASE 2 ADDED METHODS START
   * 
   */

  /**
   * Get the accounts assigned to a given user.
   * 
   * @param userId the id of the user.
   * @return a list containing the id's of the accounts.
   * @throws SQLException if something goes wrong.
   */
  public static List<Integer> getUserAccounts(int userId) throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getUserAccounts(userId, connection);
    List<Integer> accounts = new ArrayList<>();
    int accountId = 0;
    while (results.next()) {
      accountId = results.getInt("ID");
      accounts.add(accountId);
    }
    results.close();
    connection.close();
    return accounts;
  }

  /**
   * Get the details of a given account.
   * 
   * @param accountId the ID of the account.
   * @return a hash map mapping from the itemId to its quantity in an account
   * @throws SQLException if something goes wrong.
   */
  public static HashMap<Integer, Integer> getAccountDetails(int accountId) throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getAccountDetails(accountId, connection);
    HashMap<Integer, Integer> account = new HashMap<Integer, Integer>();
    int itemId = 0;
    int quantity = 0;
    while (results.next()) {
      itemId = results.getInt("ITEMID");
      quantity = results.getInt("QUANTITY");
      account.put(itemId, quantity);
    }
    results.close();
    connection.close();
    return account;
  }

  /*
   * 
   * PHASE 2 ADDED METHODS END
   * 
   */

  /*
   * 
   * PHASE 3 ADDED METHODS START
   * 
   */


  /**
   * Returns a list of active accounts for a given user.
   * 
   * @param userId the ID of the user being inquired about
   * @return a list of active accounts
   * @throws SQLException when data cannot be read from the database
   */
  public static List<Integer> getUserActiveAccounts(int userId) throws SQLException {
    if (getUserAccounts(userId).isEmpty()) {
      return getUserAccounts(userId);
    } else {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      List<Integer> active = new ArrayList<Integer>();
      ResultSet results = DatabaseSelector.getUserActiveAccounts(userId, connection);
      while (results.next()) {
        active.add(results.getInt("ID"));
      }
      results.close();
      connection.close();
      return active;
    }
  }

  /**
   * Returns a list of inactive accounts for a given user.
   * 
   * @param userId the ID of the user being inquired about
   * @return a list of inactive accounts
   * @throws SQLException when the data cannot be read from the database
   */
  public static List<Integer> getUserInactiveAccounts(int userId) throws SQLException {
    if (getUserAccounts(userId).isEmpty()) {
      return getUserAccounts(userId);
    } else {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      List<Integer> inactive = new ArrayList<Integer>();
      ResultSet results = DatabaseSelector.getUserInactiveAccounts(userId, connection);
      while (results.next()) {
        inactive.add(results.getInt("ID"));
      }
      results.close();
      connection.close();
      return inactive;
    }
  }


  /*
   * 
   * PHASE 3 ADDED METHODS END
   * 
   */
}
