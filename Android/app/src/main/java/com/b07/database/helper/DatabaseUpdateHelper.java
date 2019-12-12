package com.b07.database.helper;

import com.b07.database.DatabaseUpdater;
import com.b07.users.Roles;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * The Class DatabaseUpdateHelper.
 */
public class DatabaseUpdateHelper extends DatabaseUpdater {

  /**
   * updates the role name .
   *
   * @param name the name
   * @param id the id
   * @return true if the role name updated successfully
   * @throws SQLException the SQL exception
   */
  public static boolean updateRoleName(String name, int id) throws SQLException {
    if (Roles.valueOf(name) == null) {
      return false;
    }

    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateRoleName(name, id, connection);
    connection.close();
    return complete;
  }

  /**
   * updates the users name.
   *
   * @param name the name
   * @param userId the user id
   * @return true if updated name successfully
   * @throws SQLException the SQL exception
   */
  public static boolean updateUserName(String name, int userId) throws SQLException {
    if (name == null | name == "") {
      return false;
    }

    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateUserName(name, userId, connection);
    connection.close();
    return complete;
  }

  /**
   * updates the user age.
   *
   * @param age the age
   * @param userId the user id
   * @return true if the age was updated successfully
   * @throws SQLException the SQL exception
   */
  public static boolean updateUserAge(int age, int userId) throws SQLException {
    if (age <= 0) {
      return false;
    }
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateUserAge(age, userId, connection);
    connection.close();
    return complete;
  }

  /**
   * updates the user's address.
   *
   * @param address the address
   * @param userId the user id
   * @return true if updated user address successfully
   * @throws SQLException the SQL exception
   */
  public static boolean updateUserAddress(String address, int userId) throws SQLException {
    if (address.length() > 100 | address == null | address == "") {
      return false;
    }

    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateUserAddress(address, userId, connection);
    connection.close();
    return complete;
  }

  /**
   * updates the user role.
   *
   * @param roleId the role id
   * @param userId the user id
   * @return true if updated the user role successfully
   * @throws SQLException the SQL exception
   */
  public static boolean updateUserRole(int roleId, int userId) throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateUserRole(roleId, userId, connection);
    connection.close();
    return complete;
  }

  /**
   * updates the item name.
   *
   * @param name the name
   * @param itemId the item id
   * @return true if item name was updated successfully
   * @throws SQLException the SQL exception
   */
  public static boolean updateItemName(String name, int itemId) throws SQLException {
    if (name == null | name == "") {
      return false;
    }

    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateItemName(name, itemId, connection);
    connection.close();
    return complete;
  }

  /**
   * updates the item price.
   *
   * @param price the price
   * @param itemId the item id
   * @return true if the item price was updated successfully
   * @throws SQLException the SQL exception
   */
  public static boolean updateItemPrice(BigDecimal price, int itemId) throws SQLException {
    if (price.scale() != 2 | (price.floatValue() < 0.00)) {
      return false;
    }

    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateItemPrice(price, itemId, connection);
    connection.close();
    return complete;
  }

  /**
   * updates inventory quantity of itemId.
   *
   * @param quantity the quantity
   * @param itemId the item id
   * @return true if inventory quantity of item was updated successfully
   * @throws SQLException the SQL exception
   */
  public static boolean updateInventoryQuantity(int quantity, int itemId) throws SQLException {
    if (quantity <= 0) {
      return false;
    }

    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateInventoryQuantity(quantity, itemId, connection);
    connection.close();
    return complete;
  }

  /**
   * Update the status of an account.
   * 
   * @param accountId the id of the account.
   * @param active the status the account should receive.
   * @return true if successful, false otherwise.
   * @throws SQLException on failure
   */
  public static boolean updateAccountStatus(int accountId, boolean active) throws SQLException {
    if (DatabaseSelectHelper.getAccountDetails(accountId).isEmpty()) {
      return false;
    }

    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateAccountStatus(accountId, active, connection);
    connection.close();
    return complete;
  }
}
