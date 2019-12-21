package com.b07.database;

import android.content.Context;
import com.b07.users.Roles;
import com.b07.database.DatabaseDriverAndroid;
import com.b07.database.DatabaseSelectHelper;
import java.math.BigDecimal;

public class DatabaseUpdateHelper extends DatabaseDriverAndroid {

  public DatabaseUpdateHelper(Context context) {
    super(context);
  }

  /**
   * updates the role name .
   *
   * @param name the name
   * @param id the id
   * @return true if the role name updated successfully
   */
  public boolean updateRoleNameHelper(String name, int id) {
    if (Roles.valueOf(name) == null) {
      return false;
    }

    return this.updateRoleName(name, id);
  }

  /**
   * updates the users name.
   *
   * @param name the name
   * @param userId the user id
   * @return true if updated name successfully
   */
  public boolean updateUserNameHelper(String name, int userId){
    if (name == null | name == "") {
      return false;
    }

    return this.updateUserName(name, userId);
  }

  /**
   * updates the user age.
   *
   * @param age the age
   * @param userId the user id
   * @return true if the age was updated successfully
   */
  public boolean updateUserAgeHelper(int age, int userId) {
    if (age <= 0) {
      return false;
    }
    return this.updateUserAge(age, userId);
  }

  /**
   * updates the user's address.
   *
   * @param address the address
   * @param userId the user id
   * @return true if updated user address successfully
   */
  public boolean updateUserAddressHelper(String address, int userId) {
    if (address.length() > 100 | address == null | address == "") {
      return false;
    }

    return this.updateUserAddress(address, userId);
  }

  /**
   * updates the user role.
   *
   * @param roleId the role id
   * @param userId the user id
   * @return true if updated the user role successfully
   */
  public boolean updateUserRoleHelper(int roleId, int userId){
    return this.updateUserRole(roleId, userId);
  }

  /**
   * updates the item name.
   *
   * @param name the name
   * @param itemId the item id
   * @return true if item name was updated successfully
   */
  public boolean updateItemNameHelper(String name, int itemId){
    if (name == null | name == "") {
      return false;
    }

    return this.updateItemName(name, itemId);
  }

  /**
   * updates the item price.
   *
   * @param price the price
   * @param itemId the item id
   * @return true if the item price was updated successfully
   */
  public boolean updateItemPriceHelper(BigDecimal price, int itemId){
    if (price.scale() != 2 | (price.floatValue() < 0.00)) {
      return false;
    }

    return this.updateItemPrice(price, itemId);
  }

  /**
   * updates inventory quantity of itemId.
   *
   * @param quantity the quantity
   * @param itemId the item id
   * @return true if inventory quantity of item was updated successfully
   */
  public boolean updateInventoryQuantityHelper(int quantity, int itemId){
    if (quantity < 0) {
      return false;
    }

    return this.updateInventoryQuantity(quantity, itemId);
  }

  /**
   * Update the status of an account.
   *
   * @param accountId the id of the account.
   * @param active the status the account should receive.
   * @return true if successful, false otherwise.
   */
  public boolean updateAccountStatusHelper(int accountId, boolean active, Context context){
    DatabaseSelectHelper db = new DatabaseSelectHelper(context);

    if (db.getAccountDetailsHelper(accountId).isEmpty()) {
      return false;
    }
    return this.updateAccountStatus(accountId, active);
  }

}
