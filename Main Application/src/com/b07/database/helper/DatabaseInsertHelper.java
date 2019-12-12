package com.b07.database.helper;

import com.b07.database.DatabaseInserter;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.users.Roles;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * The Class DatabaseInsertHelper.
 */
public class DatabaseInsertHelper extends DatabaseInserter {

  /**
   * assigns role to the user upon whom the function is called upon.
   *
   * @param name the name
   * @return -1 if could not be inserted, or the roleId
   */
  public static int insertRole(String name) {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    int roleId = -1;
    try {
      if (Roles.valueOf(name) == null) {
        return -1;
      }
      roleId = DatabaseInserter.insertRole(name, connection);
      connection.close();
    } catch (DatabaseInsertException | SQLException e) {
      System.out.println("The new value could not be inserted into database!");
      e.printStackTrace();
    }
    return roleId;
  }

  /**
   * inserts a new user with the given parameters.
   *
   * @param name the name
   * @param age the age
   * @param address the address
   * @param password the password
   * @return -1 if could not be inserted, or the userId
   */
  public static int insertNewUser(String name, int age, String address, String password) {
    int userId = -1;
    if (age < 0) {
      System.out.println("Encountered error. Check to make sure your age is correct");
    } else if (address.length() > 100) {
      System.out.println(
          "Encountered error. Please make sure your " + "address is less than 100 characters");
    } else if (name.isEmpty() | !name.contains(" ")) {
      System.out.println("Encountered error. Check to make sure your name paramaters is correct."
          + " For example \"Steven King\"");
    } else {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      try {
        userId = DatabaseInserter.insertNewUser(name, age, address, password, connection);
        connection.close();
      } catch (DatabaseInsertException e) {
        System.out.println("The new value could not be inserted into database!");
        e.printStackTrace();
      } catch (SQLException e) {
        System.out.println("Could not close the Table!");
        e.printStackTrace();
      }
      return userId;
    }
    return userId;
  }

  /**
   * inserts a new user with the given parameters (with hashed password provided).
   *
   * @param name the name
   * @param age the age
   * @param address the address
   * @param password the password
   * @return -1 if could not be inserted, or the userId
   */
  public static int insertNewUserHashedPwd(String name, int age, String address, String password) {
    int userId = -1;
    if (age < 0) {
      System.out.println("Encountered error. Check to make sure your age is correct");
    } else if (address.length() > 100) {
      System.out.println(
          "Encountered error. Please make sure your " + "address is less than 100 characters");
    } else if (name.isEmpty() | !name.contains(" ")) {
      System.out.println("Encountered error. Check to make sure your name paramaters is correct."
          + " For example \"Steven King\"");
    } else {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      try {
        userId = DatabaseInserter.insertNewUserHashedPwd(name, age, address, password, connection);
        connection.close();
      } catch (DatabaseInsertException e) {
        System.out.println("The new value could not be inserted into database!");
        e.printStackTrace();
      } catch (SQLException e) {
        System.out.println("Could not close the Table!");
        e.printStackTrace();
      }
      return userId;
    }
    return userId;
  }

  /**
   * gives the user a role.
   *
   * @param userId the user id
   * @param roleId the role id
   * @return -1 if could not be inserted, or the userRoleId
   */
  public static int insertUserRole(int userId, int roleId) {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    int userRoleId = -1;
    try {
      userRoleId = DatabaseInserter.insertUserRole(userId, roleId, connection);
      connection.close();
    } catch (DatabaseInsertException e) {
      System.out.println("The UserId + RoleId relation must be unique!");
      e.printStackTrace();
    } catch (SQLException e) {
      System.out.println("Could not close the Table!");
      e.printStackTrace();
    }
    return userRoleId;
  }

  /**
   * inserts item based on its price and name.
   *
   * @param name the name
   * @param price the price of the item
   * @return -1 if could not be inserted, or the itemId
   */
  public static int insertItem(String name, BigDecimal price) {
    int itemId = -1;
    if ((name.length() >= 64) | name == null) {
      System.out.println("Input a not null item name less than 64 characters.");
    } else if (price.floatValue() < 0.00) {
      System.out.println("Price must be greater than 0.");
    } else {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      try {
        // Change price to 2 decimal points of precision.
        price = price.setScale(2, RoundingMode.HALF_UP);
        itemId = DatabaseInserter.insertItem(name, price, connection);
        connection.close();
      } catch (DatabaseInsertException e) {
        System.out.println("There is some problem checking existing items.");
        e.printStackTrace();
      } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Cannot close the table.");
      }
    }
    return itemId;

  }

  /**
   * checks that the quantity parameter is valid helps insert quantity of item into inventory based
   * on itemId.
   *
   * @param itemId the item id
   * @param quantity the quantity
   * @return -1 if could not be inserted, or the inventoryId
   */
  public static int insertInventory(int itemId, int quantity) {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    int inventoryId = -1;
    if (quantity >= 0) {
      try {
        inventoryId = DatabaseInserter.insertInventory(itemId, quantity, connection);
        connection.close();
      } catch (DatabaseInsertException e) {
        System.out.println("The new value could not be inserted into database!");
        e.printStackTrace();
      } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Cannot close the table.");
      }
    } else {
      System.out.println("Input a quantity that is at least 0.");
    }
    return inventoryId;
  }

  /**
   * inserts sale and returns the sale id.
   *
   * @param userId the user id
   * @param totalPrice the total price
   * @return -1 if could not be inserted, or the saleId
   */
  public static int insertSale(int userId, BigDecimal totalPrice) {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    int saleId = -1;
    try {
      saleId = DatabaseInserter.insertSale(userId, totalPrice, connection);
      connection.close();
    } catch (DatabaseInsertException e) {
      System.out.println("The new value could not be inserted into database!");
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Cannot close the table.");
    }
    return saleId;
  }

  /**
   * inserts an itemized sale.
   *
   * @param saleId the sale id
   * @param itemId the item id
   * @param quantity the quantity
   * @return -1 if could not be inserted, or the itemizedId
   */
  public static int insertItemizedSale(int saleId, int itemId, int quantity) {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    int itemizedId = -1;
    try {
      itemizedId = DatabaseInserter.insertItemizedSale(saleId, itemId, quantity, connection);
      connection.close();
    } catch (DatabaseInsertException e) {
      System.out.println("The new value could not be inserted into database!");
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Cannot close the table.");
    }
    return itemizedId;
  }

  /*
   * 
   * PHASE 2 NEW METHODS START
   * 
   */


  /**
   * insert a single item into a given account for recovery next login.
   * 
   * @param accountId the id of the account.
   * @param itemId the item to be inserted.
   * @param quantity the quantity of that item.
   * @return -1 if could not be inserted, or the id of the inserted record.
   */
  public static int insertAccountLine(int accountId, int itemId, int quantity) {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    int recordId = -1;
    try {
      recordId = DatabaseInserter.insertAccountLine(accountId, itemId, quantity, connection);

      connection.close();
    } catch (DatabaseInsertException e) {
      System.out.println("The new value could not be inserted into database!");
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Cannot close the table.");
    }
    return recordId;
  }


  /*
   * 
   * PHASE 2 NEW METHODS END
   * 
   */

  /*
   * PHASE 3 NEW METHODS START
   */

  /**
   * Insert a new account into the database.
   * 
   * @param userId the userId for the user of the account.
   * @return -1 if could not be inserted, or the id of the inserted record.
   */
  public static int insertAccount(int userId, boolean active) {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    int acccountId = -1;
    try {
      acccountId = DatabaseInserter.insertAccount(userId, active, connection);
      connection.close();
    } catch (DatabaseInsertException e) {
      System.out.println("The new value could not be inserted into database!");
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Cannot close the table.");
    }
    return acccountId;
  }

  /*
   * PHASE 3 NEW METHODS END
   */
}
