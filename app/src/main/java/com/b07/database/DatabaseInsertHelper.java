package com.b07.database;

import android.content.Context;
import com.b07.users.Roles;
import java.math.BigDecimal;
import java.math.RoundingMode;
import com.b07.database.DatabaseDriverAndroid;

public class DatabaseInsertHelper extends DatabaseDriverAndroid {

    public DatabaseInsertHelper(Context context) {
        super(context);
    }

    /**
     * assigns role to the user upon whom the function is called upon.
     *
     * @param name the name
     * @return -1 if could not be inserted, or the roleId
     */
    public long insertRoleHelper(String name) {
        long roleId = -1;
        if (Roles.valueOf(name) == null) {
            return -1;
        }
        roleId = this.insertRole(name);
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
    public long insertNewUserHelper(String name, int age, String address, String password) {
        long userId = -1;
        if (age < 0) {
            System.out.println("Encountered error. Check to make sure your age is correct");
        } else if (address.length() > 100) {
            System.out.println(
                    "Encountered error. Please make sure your "
                            + "address is less than 100 characters");
        } else if (name.isEmpty() | !name.contains(" ")) {
            System.out.println(
                    "Encountered error. Check to make sure your name paramaters is correct."
                            + " For example \"Steven King\"");
        } else {
            userId = this.insertNewUser(name, age, address, password);
            return userId;
        }
        return userId;
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
    public long insertNewUserHashedPwdHelper(String name, int age, String address,
            String password) {
        long userId = -1;
        if (age < 0) {
            System.out.println("Encountered error. Check to make sure your age is correct");
        } else if (address.length() > 100) {
            System.out.println(
                    "Encountered error. Please make sure your "
                            + "address is less than 100 characters");
        } else if (name.isEmpty() | !name.contains(" ")) {
            System.out.println(
                    "Encountered error. Check to make sure your name paramaters is correct."
                            + " For example \"Steven King\"");
        } else {
            userId = this.insertNewUserHashedPwd(name, age, address, password);
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
    public long insertUserRoleHelper(int userId, int roleId) {
        long userRoleId = -1;
        userRoleId = this.insertUserRole(userId, roleId);
        return userRoleId;
    }

    /**
     * inserts item based on its price and name.
     *
     * @param name the name
     * @param price the price of the item
     * @return -1 if could not be inserted, or the itemId
     */
    public long insertItemHelper(String name, BigDecimal price) {
        long itemId = -1;
        if ((name.length() >= 64) | name == null) {
            System.out.println("Input a not null item name less than 64 characters.");
        } else if (price.floatValue() < 0.00) {
            System.out.println("Price must be greater than 0.");
        } else {
            // Change price to 2 decimal points of precision.
            price = price.setScale(2, RoundingMode.HALF_UP);
            itemId = this.insertItem(name, price);
        }
        return itemId;

    }

    /**
     * checks that the quantity parameter is valid helps insert quantity of item into inventory
     * based on itemId.
     *
     * @param itemId the item id
     * @param quantity the quantity
     * @return -1 if could not be inserted, or the inventoryId
     */
    public long insertInventoryHelper(int itemId, int quantity) {
        long inventoryId = -1;
        if (quantity >= 0) {
            inventoryId = this.insertInventory(itemId, quantity);
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
    public long insertSaleHelper(int userId, BigDecimal totalPrice) {
        long saleId = -1;
        saleId = this.insertSale(userId, totalPrice);
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
    public long insertItemizedSaleHelper(int saleId, int itemId, int quantity) {
        long itemizedId = -1;
        itemizedId = this.insertItemizedSale(saleId, itemId, quantity);
        return itemizedId;
    }


    /**
     * insert a single item into a given account for recovery next login.
     *
     * @param accountId the id of the account.
     * @param itemId the item to be inserted.
     * @param quantity the quantity of that item.
     * @return -1 if could not be inserted, or the id of the inserted record.
     */
    public long insertAccountLineHelper(int accountId, int itemId, int quantity) {
        long recordId = -1;
        recordId = this.insertAccountLine(accountId, itemId, quantity);
        return recordId;
    }


    /**
     * Insert a new account into the database.
     *
     * @param userId the userId for the user of the account.
     * @return -1 if could not be inserted, or the id of the inserted record.
     */
    public long insertAccountHelper(int userId, boolean active) {
        long accountId = -1;
        accountId = this.insertAccount(userId, active);
        return accountId;
    }
}
