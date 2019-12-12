package com.b07.serialize;

import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.store.Sale;
import com.b07.store.SalesLog;
import com.b07.users.User;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class SerializeHelper {

  /**
   * Create and return database_copy.ser that stores all data from database.
   * 
   * @throws SQLException on failure
   */
  public static void writeDb() throws SQLException {
    try {
      FileOutputStream fileOut = new FileOutputStream("database_copy.ser");
      ObjectOutputStream out = new ObjectOutputStream(fileOut);
      System.out.println("Now serializing the following tables and values:");
      System.out.println("----------------------------");

      // Roles
      List<Integer> roleIds = DatabaseSelectHelper.getRoleIds();
      if (roleIds != null) {
        HashMap<Integer, String> roles = new HashMap<Integer, String>();
        for (int roleId : roleIds) {
          roles.put(roleId, DatabaseSelectHelper.getRoleName(roleId));
          System.out.println("ROLES: " + DatabaseSelectHelper.getRoleName(roleId));
        }
        out.writeObject(roles);

      } else {
        System.out.println("ROLES: No role been serialized");
      }

      // users and password
      List<User> users = DatabaseSelectHelper.getUsersDetails();
      if (users != null) {
        out.writeInt(users.size());
        for (User u : users) {
          int userId = u.getId();
          HashMap<User, String> userPasswordSet = new HashMap<User, String>();
          userPasswordSet.put(u, DatabaseSelectHelper.getPassword(userId));
          out.writeObject(userPasswordSet);
          System.out.println("USERS: " + u.getName());
          System.out.println("USERROLE: " + DatabaseSelectHelper.getUserRoleId(userId));
          System.out.println("USERPWD: " + DatabaseSelectHelper.getPassword(userId));
        }
      } else {
        out.writeInt(0);
        System.out.println("USERS, USERROLE and USERPWD: No user been serialized");
      }

      // items
      List<Item> items = DatabaseSelectHelper.getAllItems();
      if (items != null) {
        out.writeInt(items.size());
        for (Item i : items) {
          out.writeObject(i);
          System.out.println("ITEMS: " + i.getName());
        }
      } else {
        out.writeInt(0);
        System.out.println("ITEMS: No item been serialized");
      }

      // sales
      List<Sale> sales = DatabaseSelectHelper.getSales().getSales();

      if (sales != null) {
        out.writeInt(sales.size());
        for (Sale s : sales) {
          out.writeObject(s);
          System.out.println("SALES: saleid = " + s.getId() + " userId = " + s.getUser().getId()
              + " total price " + s.getTotalPrice());
        }
      } else {
        out.writeInt(0);
        System.out.println("SALES: No sale been serialized");
      }

      // itemized sales
      SalesLog itemizedsales = DatabaseSelectHelper.getItemizedSales();
      if (itemizedsales != null) {
        out.writeInt(itemizedsales.getSales().size());
        out.writeObject(itemizedsales);
        System.out.println(itemizedsales);
        for (Sale is : itemizedsales.getSales()) {
          for (Item i : is.getItemMap().keySet()) {
            System.out.println("ITEMIZEDSALES: saleid = " + is.getId() + " itemId = " + i.getId()
                + " quantity " + is.getItemMap().get(i));
          }
        }
      } else {
        out.writeInt(0);
        System.out.println("ITEMIZEDSALES: No sale been serialized");
      }

      // inventory
      Inventory inv = DatabaseSelectHelper.getInventory();

      if (inv != null) {
        out.writeInt(inv.getTotalItems());
        out.writeObject(inv);
        System.out.println("INVENTORY: total items:" + inv.getTotalItems());
      } else {
        out.writeInt(0);
        System.out.println("INVENTORY: No inventory been serialized");
      }

      // account
      List<User> users1 = DatabaseSelectHelper.getUsersDetails();

      if (users1 != null) {
        out.writeInt(users.size());

        HashMap<Integer, HashMap<Integer, Boolean>> accountDetails =
            new HashMap<Integer, HashMap<Integer, Boolean>>();

        for (User user : users1) {
          HashMap<Integer, Boolean> userIdActive = new HashMap<Integer, Boolean>();
          int id = user.getId();
          List<Integer> accountIds = DatabaseSelectHelper.getUserAccounts(id);

          for (Integer accountId : accountIds) {
            boolean active = (DatabaseSelectHelper.getUserActiveAccounts(id).contains(accountId));
            userIdActive.put(accountId, active);
            System.out.println("id: " + id + " account Id:" + accountId + "Active" + active);
          }
          accountDetails.put((Integer) id, userIdActive);
        }
        System.out.println("ACCOUNT: " + accountDetails);
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
          List<Integer> accountIds = DatabaseSelectHelper.getUserAccounts(id);

          for (Integer accountId : accountIds) {
            HashMap<Integer, Integer> summary = new HashMap<Integer, Integer>();
            summary = DatabaseSelectHelper.getAccountDetails(accountId);
            accountSummary.put(accountId, summary);
          }
        }
        out.writeObject(accountSummary);
        System.out.println("ACCOUNTSUMMARY: " + accountSummary);
      } else {
        out.writeInt(0);
      }

      // Finished
      out.close();
      fileOut.close();
      System.out.printf("Serialized data is saved in database_copy.ser");

    } catch (IOException e) {
      System.out.println("Some value or table could not be serialized");
      e.printStackTrace();
    }
  }

}
