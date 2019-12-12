package com.b07.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import com.b07.database.DatabaseDriverAndroid;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseSelectHelper extends DatabaseDriverAndroid {

    public DatabaseSelectHelper(Context context) {

        super(context);
    }

    /**
     * gets a hashmap of roles from the database
     *
     * @return the role id and role names in a hashmap
     */
    public HashMap<Integer, String> getRolesHelper() {
        HashMap<Integer, String> allRoles = new HashMap<Integer, String>();
        Cursor cursorSet = this.getRoles();
        int columnId = cursorSet.getColumnIndex("ID");
        int columnName = cursorSet.getColumnIndex("NAME");
        while (cursorSet.moveToNext()) {
            allRoles.put(cursorSet.getInt(columnId), cursorSet.getString(columnName));
        }
        cursorSet.close();
        return allRoles;
    }

    /**
     * gets a list of roleIds from the database.
     *
     * @return the role ids
     */
    public List<Integer> getRoleIdsHelper() {
        Cursor results = this.getRoles();
        List<Integer> ids = new ArrayList<>();
        while (results.moveToNext()) {
            ids.add(results.getInt(results.getColumnIndex("ID")));
        }
        results.close();
        return ids;
    }

    /**
     * Return the role id of the role.
     *
     * @param name the name of the role
     * @return the roleId of the role, -1 if role not found
     */
    public int getRoleIdHelper(String name) {
        List<Integer> roleIds = getRoleIdsHelper();
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
     */
    public String getRoleName(int roleId) {
        return this.getRole(roleId);
    }

    /**
     * gets the user role id based on the user id.
     *
     * @param userId the user id
     * @return roleId
     */
    public int getUserRoleId(int userId) {

        return this.getUserRole(userId);
    }

    /**
     * gets a list of users who have the same roleId.
     *
     * @param roleId the role id
     * @return a list of users with the same roleId
     */
    public List<Integer> getUsersByRoleHelper(int roleId) {
        Cursor results = this.getUsersByRole(roleId);
        int columnIndex = results.getColumnIndex("USERID");
        List<Integer> userIds = new ArrayList<>();
        while (results.moveToNext()) {
            int userId = results.getInt(columnIndex);
            userIds.add(userId);
        }
        results.close();
        return userIds;
    }

    /**
     * gets the details of the users and returns them in a list.
     *
     * @return a list of Users
     */
    public List<User> getUsersDetailsHelper() {
        Cursor result = this.getUsersDetails();
        List<User> users = new ArrayList<>();

        int id;
        int age;
        String name;
        String address;
        while (result.moveToNext()) {
            id = result.getInt(result.getColumnIndex("ID"));
            age = result.getInt(result.getColumnIndex("AGE"));
            name = result.getString(result.getColumnIndex("NAME"));
            address = result.getString(result.getColumnIndex("ADDRESS"));

            int roleId = getUserRoleId(id);

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
        result.close();
        return users;
    }

    /**
     * gets the user details.
     *
     * @param userId the user id
     * @return the user details
     * @returns the user
     */
    public User getUserDetailsHelper(int userId) {
        Cursor result = this.getUserDetails(userId);
        User user = null;

        int id = 0;
        int age = 0;
        String name = null;
        String address = null;

        while (result.getCount() > 0 && result.moveToNext()) {
            id = result.getInt(result.getColumnIndex("ID"));
            age = result.getInt(result.getColumnIndex("AGE"));
            name = result.getString(result.getColumnIndex("NAME"));
            address = result.getString(result.getColumnIndex("ADDRESS"));
        }
        result.close();

        if (id > 0) {

            int roleId = getUserRoleId(id);

            if (this.getRole(roleId).equalsIgnoreCase(Roles.ADMIN.name())) {
                user = new Admin(id, name, age, address);
            }
            if (this.getRole(roleId).equalsIgnoreCase(Roles.EMPLOYEE.name())) {
                user = new Employee(id, name, age, address);
            }
            if (this.getRole(roleId).equalsIgnoreCase(Roles.CUSTOMER.name())) {
                user = new Customer(id, name, age, address);
            }
        }

        return user;
    }

    /**
     * gets the password based on the user id.
     *
     * @param userId the user id
     * @return password
     */
    public String getPasswordHelper(int userId) {
        return this.getPassword(userId);
    }

    /**
     * gets all the items.
     *
     * @return a list of items
     */
    public List<Item> getAllItemsHelper() {
        Cursor results = this.getAllItems();
        List<Item> items = new ArrayList<>();
        int id = 0;
        String name = null;
        BigDecimal price = null;
        while (results.moveToNext()) {
            id = results.getInt(results.getColumnIndex("ID"));
            name = results.getString(results.getColumnIndex("NAME"));
            price = new BigDecimal(results.getString(results.getColumnIndex("PRICE")));

            Item tempItem = new ItemImpl();
            tempItem.setId(id);
            tempItem.setName(name);
            tempItem.setPrice(price);

            items.add(tempItem);
        }
        results.close();
        return items;
    }

    /**
     * gets a specific item based on its id.
     *
     * @param itemId the item id
     * @return item
     */
    public Item getItemHelper(int itemId) {
        Cursor results = this.getItem(itemId);
        int id = -1;
        String name = null;
        BigDecimal price = null;

        while (results.moveToNext()) {
            id = results.getInt(results.getColumnIndex("ID"));
            name = results.getString(results.getColumnIndex("NAME"));
            price = new BigDecimal(results.getString(results.getColumnIndex("PRICE")));
        }

        results.close();

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
     */
    public Inventory getInventoryHelper() {
        Cursor results = this.getInventory();
        int itemId = -1;
        String quantity = null;

        InventoryImpl inv = new InventoryImpl();

        while (results.moveToNext()) {
            itemId = results.getInt(results.getColumnIndex("ITEMID"));
            quantity = results.getString(results.getColumnIndex("QUANTITY"));
            Item item = getItemHelper(itemId);
            inv.updateMap(item, Integer.parseInt(quantity));
        }

        results.close();
        return inv;
    }

    /**
     * gets the quantity of item in inventory.
     *
     * @param itemId the item id
     * @return quantity
     */
    public int getInventoryQuantityHelper(int itemId) {
        return this.getInventoryQuantity(itemId);
    }

    /**
     * gets the sales log.
     *
     * @return sales log
     */
    public SalesLog getSalesHelper() {
        Cursor results = this.getSales();
        SalesLogImpl saleLog = new SalesLogImpl();
        ArrayList<Sale> salesList = new ArrayList<Sale>();

        while (results.moveToNext()) {
            Sale sale = getItemizedSaleByIdHelper(results.getInt(results.getColumnIndex("ID")));
            salesList.add(sale);
        }

        saleLog.setSales(salesList);
        results.close();
        return saleLog;
    }

    /**
     * gets the sales log based on the sale id.
     *
     * @param saleId the sale id
     * @return Sale
     */
    public Sale getSaleByIdHelper(int saleId) {
        Cursor results = this.getSaleById(saleId);
        Sale s = null;

        while (results.moveToNext()) {
            s = new SaleImpl();
            s.setId(results.getInt(results.getColumnIndex("ID")));
            s.setUser(getUserDetailsHelper(results.getInt(results.getColumnIndex("USERID"))));
            s.setTotalPrice(
                    new BigDecimal(results.getString(results.getColumnIndex("TOTALPRICE"))));
        }

        results.close();
        return s;
    }

    /**
     * gets a list of sales to specific user.
     *
     * @param userId the user id
     * @return list of sales
     */
    public List<Sale> getSalesToUserHelper(int userId) {
        Cursor results = this.getSalesToUser(userId);
        List<Sale> sales = new ArrayList<>();

        while (results.moveToNext()) {
            Sale s = new SaleImpl();
            s.setId(results.getInt(results.getColumnIndex("ID")));
            s.setUser(getUserDetailsHelper(results.getInt(results.getColumnIndex("USERID"))));
            s.setTotalPrice(
                    new BigDecimal(results.getString(results.getColumnIndex("TOTALPRICE"))));
            sales.add(s);
        }
        results.close();
        return sales;
    }

    /**
     * gets an itemized sale based on the sale id.
     *
     * @param saleId the sale id
     * @return sale
     */

    public Sale getItemizedSaleByIdHelper(int saleId) {
        Cursor results = this.getItemizedSaleById(saleId);
        Sale s = getSaleByIdHelper(saleId);

        while (results.moveToNext()) {
            Item item = getItemHelper((results.getInt(results.getColumnIndex("ITEMID"))));
            if (item != null) {
                s.getItemMap().put(item, results.getInt(results.getColumnIndex("QUANTITY")));
            }
        }
        results.close();
        return s;
    }

    /**
     * gets a sales log of itemized sales.
     *
     * @return SalesLog
     */

    //TODO: CURSOR SET
    public SalesLog getItemizedSalesHelper() {
        Cursor results = this.getItemizedSales();

        SalesLogImpl salesLog = new SalesLogImpl();
        HashMap<Integer, Integer> itemMaps = new HashMap<Integer, Integer>();
        BigDecimal total = new BigDecimal(0);

        while (results.moveToNext()) {
            int itemId = results.getInt(results.getColumnIndex("ITEMID"));
            int qty = results.getInt(results.getColumnIndex("QUANTITY"));

            if (itemMaps.containsKey(itemId)) {
                int oldQty = itemMaps.get(itemId);
                itemMaps.put(itemId, qty + oldQty);
            } else {
                itemMaps.put(itemId, qty);
            }

            total = total.add(getItemHelper(itemId).getPrice().multiply(new BigDecimal(qty)));
        }

        salesLog.setItemMaps(itemMaps);
        salesLog.setGrandTotal(total);

        results.close();
        return salesLog;
    }

    /**
     * Combines sales log of Sales and Itemized Sales for a full version.
     *
     * @return salesLog
     */
    public SalesLog consolidateSalesLog() {
        SalesLogImpl sales = (SalesLogImpl) getSalesHelper();
        SalesLogImpl itemizedSales = (SalesLogImpl) getItemizedSalesHelper();
        SalesLogImpl salesLog = new SalesLogImpl();

        salesLog.setSales(sales.getSales());
        salesLog.setItemMaps(itemizedSales.getItemMaps());
        salesLog.setGrandTotal(itemizedSales.getGrandTotal());

        return salesLog;
    }

    /**
     * Get the accounts assigned to a given user.
     *
     * @param userId the id of the user.
     * @return a list containing the id's of the accounts.
     */
    public List<Integer> getUserAccountsHelper(int userId) {
        Cursor results = this.getUserAccounts(userId);
        List<Integer> accounts = new ArrayList<>();
        int accountId = 0;
        while (results.moveToNext()) {
            accountId = results.getInt(results.getColumnIndex("ID"));
            accounts.add(accountId);
        }
        results.close();
        return accounts;
    }

    /**
     * Get the details of a given account.
     *
     * @param accountId the ID of the account.
     * @return a hash map mapping from the itemId to its quantity in an account
     */
    public HashMap<Integer, Integer> getAccountDetailsHelper(int accountId) {
        Cursor results = this.getAccountDetails(accountId);
        HashMap<Integer, Integer> account = new HashMap<Integer, Integer>();
        int itemId = 0;
        int quantity = 0;
        while (results.moveToNext()) {
            itemId = results.getInt(results.getColumnIndex("ITEMID"));
            quantity = results.getInt(results.getColumnIndex("QUANTITY"));
            account.put(itemId, quantity);
        }
        return account;
    }

    /**
     * Returns a list of active accounts for a given user.
     *
     * @param userId the ID of the user being inquired about
     * @return a list of active accounts
     */
    public List<Integer> getUserActiveAccountsHelper(int userId) {
        if (getUserAccountsHelper(userId).isEmpty()) {
            return getUserAccountsHelper(userId);
        } else {
            List<Integer> active = new ArrayList<Integer>();
            Cursor results = this.getUserActiveAccounts(userId);
            while (results.moveToNext()) {
                active.add(results.getInt(results.getColumnIndex("ID")));
            }
            results.close();
            return active;
        }
    }

    /**
     * Returns a list of inactive accounts for a given user.
     *
     * @param userId the ID of the user being inquired about
     * @return a list of inactive accounts
     */
    public List<Integer> getUserInactiveAccountsHelper(int userId) {
        if (getUserAccountsHelper(userId).isEmpty()) {
            return getUserAccountsHelper(userId);
        } else {
            List<Integer> inactive = new ArrayList<Integer>();
            Cursor results = this.getUserInactiveAccounts(userId);
            while (results.moveToNext()) {
                inactive.add(results.getInt(results.getColumnIndex("ID")));
            }
            results.close();
            return inactive;
        }
    }


}

