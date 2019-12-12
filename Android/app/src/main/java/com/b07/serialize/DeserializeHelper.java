package com.b07.serialize;

import android.content.Context;

import com.b07.database.DatabaseInsertHelper;
import com.b07.database.DatabaseSelectHelper;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.store.Sale;
import com.b07.store.SalesLog;
import com.b07.users.Admin;
import com.b07.users.Customer;
import com.b07.users.Employee;
import com.b07.users.Roles;
import com.b07.users.User;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeserializeHelper {

    private Context context;

    public DeserializeHelper(Context context) {
        this.context = context;
    }


    /**
     * Read the database.
     *
     * @return string a log
     * @throws ClassNotFoundException on failure
     */
    public String readDB(File file) throws ClassNotFoundException {
        String log = "";

        try {
            DatabaseInsertHelper insert = new DatabaseInsertHelper(this.context);
            DatabaseSelectHelper select = new DatabaseSelectHelper(this.context);
            FileInputStream fileIn = new FileInputStream(new File(file, "database_copy.ser"));
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object o;

            log += "Retrieving info from ser and adding the following values into database" + "\n";
            log += "---------------------------------" + "\n";

            // Roles

            o = in.readObject();

            @SuppressWarnings("unchecked")
            HashMap<Integer, String> roles = (HashMap<Integer, String>) o;
            // Restore ROLES
            for (int roleId : roles.keySet()) {
                insert.insertRoleHelper(roles.get(roleId));
                log += "ROLES: " + roles.get(roleId) + "\n";
            }

            // users
            int userCount = in.readInt();

            while (userCount-- > 0) {
                o = in.readObject();
                @SuppressWarnings("unchecked")
                HashMap<User, String> userPasswordSet = (HashMap<User, String>) o;
                for (User u : userPasswordSet.keySet()) {
                    // Restore USERS and USERPWD
                    String hashedPwd = userPasswordSet.get(u);

                    long userIdLong = insert.insertNewUserHashedPwdHelper(u.getName(), u.getAge(),
                            u.getAddress(), hashedPwd);
                    int userId = Math.toIntExact(userIdLong);

                    log += "USERS: " + u.getName() + "\n";
                    log += "USERPWD: " + hashedPwd + "\n";
                    // Restore UserRole

                    if (u instanceof Admin) {
                        insert.insertUserRoleHelper(userId,
                                select.getRoleIdHelper(Roles.ADMIN.toString()));
                        log += "USERROLE: Admin" + "\n";
                    } else if (u instanceof Customer) {
                        insert.insertUserRoleHelper(userId,
                                select.getRoleIdHelper(Roles.CUSTOMER.toString()));
                        log += "USERROLE: Customer" + "\n";
                    } else if (u instanceof Employee) {
                        insert.insertUserRoleHelper(userId,
                                select.getRoleIdHelper(Roles.EMPLOYEE.toString()));
                        log += "USERROLE: Employee" + "\n";
                    }
                }
            }

            // items
            int itemCount = in.readInt();

            while (itemCount-- > 0) {
                o = in.readObject();
                // Restore ITEMS

                Item u = (Item) o;

                insert.insertItemHelper(u.getName(), u.getPrice());
                log += "ITEMS: " + u.getName() + " " + u.getPrice() + "\n";
            }

            // sales
            int salesCount = in.readInt();
            while (salesCount-- > 0) {
                o = in.readObject();
                Sale s = (Sale) o;
                insert.insertSaleHelper(s.getUser().getId(), s.getTotalPrice());
                log += "SALES: saleid = " + s.getId() + " userId = " + s.getUser().getId()
                        + " total price " + s.getTotalPrice() + "\n";
            }

            // itemized sales
            int isalesCount = in.readInt();
            if (isalesCount > 0) {
                SalesLog sl = (SalesLog) in.readObject();
                List<Integer> salesRecord = new ArrayList<>();
                for (Sale s : sl.getSales()) {
                    if (!salesRecord.contains(s.getId())) {
                        salesRecord.add(s.getId());
                        for (Item i : s.getItemMap().keySet()) {
                            insert.insertItemizedSaleHelper(s.getId(), i.getId(),
                                    s.getItemMap().get(i));
                            log += "ITEMIZEDSALES: saleid = " + s.getId() + " itemId = " + i.getId()
                                    + " quantity " + s.getItemMap().get(i) + "\n";
                        }
                    }

                }
            }

            // inventory
            int invCount = in.readInt();
            if (invCount > 0) {
                o = in.readObject();
                Inventory inv = (Inventory) o;
                for (Item i : inv.getItemMap().keySet()) {
                    insert.insertInventoryHelper(i.getId(), inv.getItemMap().get(i));
                    log += "INVENTORY: " + inv.getItemMap().get(i) + " " + i.getName() + "\n";
                }
            }

            // Account
            int usercount = in.readInt();

            if (usercount > 0) {
                o = in.readObject();
                @SuppressWarnings("unchecked")
                HashMap<Integer, HashMap<Integer, Boolean>> accountDetails =
                        (HashMap<Integer, HashMap<Integer, Boolean>>) o;
                log += "ACCOUNT: id: " + accountDetails + "\n";

                for (int userId : accountDetails.keySet()) {
                    HashMap<Integer, Boolean> userIdActive = new HashMap<Integer, Boolean>();
                    userIdActive = accountDetails.get(userId);
                    for (int accountId : userIdActive.keySet()) {
                        insert.insertAccountHelper(userId, userIdActive.get(accountId));
                        log += "ACCOUNT: userId: " + userId + " account Id:" + accountId + "Active"
                                + userIdActive.get(accountId) + "\n";
                    }


                }
            }
            // AccountSummary
            int usercount1 = in.readInt();
            log += "Read: " + usercount1 + "\n";
            if (usercount1 > 0) {
                o = in.readObject();

                @SuppressWarnings("unchecked")
                HashMap<Integer, HashMap<Integer, Integer>> accountSummary =
                        (HashMap<Integer, HashMap<Integer, Integer>>) o;
                for (Integer accountId : accountSummary.keySet()) {
                    for (Integer itemId : accountSummary.get(accountId).keySet()) {
                        Integer quantity = accountSummary.get(accountId).get(itemId);
                        insert.insertAccountLineHelper(accountId, itemId, quantity);
                        log += "ACCOUNTSUMMARY: Account Id: " + accountId + "item Id: " + itemId
                                + "quantity: " + quantity + "\n";
                    }
                }
            }

            // Finish
            in.close();
            fileIn.close();
            log += "That's everything being retrieved" + "\n";

        } catch (IOException e) {
            e.printStackTrace();
        }
        return log;
    }

}
