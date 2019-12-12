package com.b07.serialize;

import com.b07.database.DatabaseSelectHelper;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.store.Sale;
import com.b07.store.SalesLog;
import com.b07.users.User;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import android.content.Context;

public class SerializeHelper {

    private Context context;

    public SerializeHelper(Context context) {
        this.context = context;
    }

    /**
     * Create and return database_copy.ser that stores all data from database.
     *
     * @return string a log
     */
    public String writeDb(File file) {

        String log = "";
        try {
            log += "Now try to create database_copy.ser:\n";

            FileOutputStream fileOut = new FileOutputStream(new File(file, "database_copy.ser"));
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            log += "Now serializing the following tables and values:\n";
            log += "----------------------------" + "\n";

            // Roles
            DatabaseSelectHelper select = new DatabaseSelectHelper(this.context);
            List<Integer> roleIds = select.getRoleIdsHelper();
            if (roleIds != null) {
                HashMap<Integer, String> roles = new HashMap<Integer, String>();
                for (int roleId : roleIds) {
                    roles.put(roleId, select.getRoleName(roleId));
                    log += "ROLES: " + select.getRoleName(roleId) + "\n";
                }
                out.writeObject(roles);

            } else {
                log += "ROLES: No role been serialized" + "\n";
            }

            // users and password
            List<User> users = select.getUsersDetailsHelper();
            if (users != null) {
                out.writeInt(users.size());
                for (User u : users) {
                    int userId = u.getId();
                    HashMap<User, String> userPasswordSet = new HashMap<User, String>();
                    userPasswordSet.put(u, select.getPasswordHelper(userId));
                    out.writeObject(userPasswordSet);
                    log += "USERS: " + u.getName() + "\n";
                    log += "USERROLE: " + select.getUserRoleId(userId) + "\n";
                    log += "USERPWD: " + select.getPasswordHelper(userId) + "\n";
                }
            } else {
                out.writeInt(0);
                log += "USERS, USERROLE and USERPWD: No user been serialized" + "\n";
            }

            // items
            List<Item> items = select.getAllItemsHelper();
            if (items != null) {
                out.writeInt(items.size());
                for (Item i : items) {
                    out.writeObject(i);
                    log += "ITEMS: " + i.getName() + "  " + i.getPrice() + "\n";
                }
            } else {
                out.writeInt(0);
                log += "ITEMS: No item been serialized" + "\n";
            }

            // sales
            List<Sale> sales = null;
            try {
                if (sales.isEmpty()) {
                    out.writeInt(sales.size());
                    for (Sale s : sales) {
                        out.writeObject(s);
                        log += "SALES: saleid = " + s.getId() + " userId = " + s.getUser().getId()
                                + " total price " + s.getTotalPrice() + "\n";
                    }
                } else {
                    out.writeInt(0);
                    log += "SALES: No sale been serialized" + "\n";
                }
            } catch (Exception e) {
                out.writeInt(0);
                log += "SALES: No sale been serialized" + "\n";
            }

            // itemized sales
            SalesLog itemizedsales = null;
            try {
                itemizedsales = select.getItemizedSalesHelper();
                if (itemizedsales != null) {
                    out.writeInt(itemizedsales.getSales().size());
                    out.writeObject(itemizedsales);
                    log += itemizedsales + "\n";
                    for (Sale is : itemizedsales.getSales()) {
                        for (Item i : is.getItemMap().keySet()) {
                            log += "ITEMIZEDSALES: saleid = " + is.getId() + " itemId = " + i
                                    .getId()
                                    + " quantity " + is.getItemMap().get(i) + "\n";
                        }
                    }
                } else {
                    out.writeInt(0);
                    log += "ITEMIZEDSALES: No sale been serialized" + "\n";
                }
            } catch (Exception e) {
                out.writeInt(0);
                log += "ITEMIZEDSALES: No sale been serialized" + "\n";
            }

            // inventory
            Inventory inv = select.getInventoryHelper();

            if (inv != null) {
                out.writeInt(inv.getTotalItems());
                out.writeObject(inv);
                log += "INVENTORY: total items:" + inv.getTotalItems() + "\n";
            } else {
                out.writeInt(0);
                log += "INVENTORY: No inventory been serialized" + "\n";
            }

            // account
            List<User> users1 = select.getUsersDetailsHelper();

            if (users1 != null) {
                out.writeInt(users.size());

                HashMap<Integer, HashMap<Integer, Boolean>> accountDetails =
                        new HashMap<Integer, HashMap<Integer, Boolean>>();

                for (User user : users1) {
                    HashMap<Integer, Boolean> userIdActive = new HashMap<Integer, Boolean>();
                    int id = user.getId();
                    List<Integer> accountIds = select.getUserAccountsHelper(id);

                    for (Integer accountId : accountIds) {
                        boolean active = (select.getUserActiveAccountsHelper(id)
                                .contains(accountId));
                        userIdActive.put(accountId, active);
                        log += "id: " + id + " account Id:" + accountId + "Active" + active + "\n";
                    }
                    accountDetails.put((Integer) id, userIdActive);
                }
                log += "ACCOUNT: " + accountDetails + "\n";
                out.writeObject(accountDetails);
            } else {
                out.writeInt(0);
            }

            // account summary
            if (users1 != null) {
                out.writeInt(users.size());
                HashMap<Integer, HashMap<Integer, Integer>> accountSummary =
                        new HashMap<Integer, HashMap<Integer, Integer>>();
                for (User u : users1) {
                    int id = u.getId();
                    List<Integer> accountIds = select.getUserAccountsHelper(id);

                    for (Integer accountId : accountIds) {
                        HashMap<Integer, Integer> summary = new HashMap<Integer, Integer>();
                        summary = select.getAccountDetailsHelper(accountId);
                        accountSummary.put(accountId, summary);
                    }
                }
                out.writeObject(accountSummary);
                log += "ACCOUNTSUMMARY: " + accountSummary + "\n";
            } else {
                out.writeInt(0);
            }

            // Finished
            out.close();
            fileOut.close();
            log += "Serialized data is saved in database_copy.ser" + "\n";

        } catch (IOException e) {
            log += "Some value or table could not be serialized" + "\n";
            e.printStackTrace();
        } catch (Exception e) {
            log += e.getLocalizedMessage();
        }
        return log;
    }
}
