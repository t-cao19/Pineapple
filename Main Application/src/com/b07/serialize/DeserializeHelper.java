package com.b07.serialize;

import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeserializeHelper {

  /**
   * Read the database.
   * 
   * @throws SQLException on failure
   * @throws ClassNotFoundException on failure
   */
  public static void readDB() throws SQLException, ClassNotFoundException {
    try {
      FileInputStream fileIn = new FileInputStream("database_copy.ser");
      ObjectInputStream in = new ObjectInputStream(fileIn);
      Object o;

      System.out.println("Retrieving info from ser and adding the following values into database");
      System.out.println("---------------------------------");

      // Roles

      o = in.readObject();

      @SuppressWarnings("unchecked")
      HashMap<Integer, String> roles = (HashMap<Integer, String>) o;
      // Restore ROLES
      for (int roleId : roles.keySet()) {
        DatabaseInsertHelper.insertRole(roles.get(roleId));
        System.out.println("ROLES: " + roles.get(roleId));
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


          int userId = DatabaseInsertHelper.insertNewUserHashedPwd(u.getName(), u.getAge(),
              u.getAddress(), hashedPwd);
          System.out.println("USERS: " + u.getName());
          System.out.println("USERPWD: " + hashedPwd);
          // Restore UserRole

          if (u instanceof Admin) {
            DatabaseInsertHelper.insertUserRole(userId,
                DatabaseSelectHelper.getRoleId(Roles.ADMIN.toString()));
            System.out.println("USERROLE: Admin");
          } else if (u instanceof Customer) {
            DatabaseInsertHelper.insertUserRole(userId,
                DatabaseSelectHelper.getRoleId(Roles.CUSTOMER.toString()));
            System.out.println("USERROLE: Customer");
          } else if (u instanceof Employee) {
            DatabaseInsertHelper.insertUserRole(userId,
                DatabaseSelectHelper.getRoleId(Roles.EMPLOYEE.toString()));
            System.out.println("USERROLE: Employee");
          }
        }
      }

      // items
      int itemCount = in.readInt();

      while (itemCount-- > 0) {
        o = in.readObject();
        // Restore ITEMS

        Item u = (Item) o;

        DatabaseInsertHelper.insertItem(u.getName(), u.getPrice());
        System.out.println("ITEMS: " + u.getName() + " " + u.getPrice());
      }

      // sales
      int salesCount = in.readInt();
      while (salesCount-- > 0) {
        o = in.readObject();
        Sale s = (Sale) o;
        DatabaseInsertHelper.insertSale(s.getUser().getId(), s.getTotalPrice());
        System.out.println("SALES: saleid = " + s.getId() + " userId = " + s.getUser().getId()
            + " total price " + s.getTotalPrice());
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
              DatabaseInsertHelper.insertItemizedSale(s.getId(), i.getId(), s.getItemMap().get(i));
              System.out.println("ITEMIZEDSALES: saleid = " + s.getId() + " itemId = " + i.getId()
                  + " quantity " + s.getItemMap().get(i));
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
          DatabaseInsertHelper.insertInventory(i.getId(), inv.getItemMap().get(i));
          System.out.println("INVENTORY: " + inv.getItemMap().get(i) + " " + i.getName());
        }
      }

      // Account
      int usercount = in.readInt();

      if (usercount > 0) {
        o = in.readObject();
        @SuppressWarnings("unchecked")
        HashMap<Integer, HashMap<Integer, Boolean>> accountDetails =
            (HashMap<Integer, HashMap<Integer, Boolean>>) o;
        System.out.println("ACCOUNT: id: " + accountDetails);

        for (int userId : accountDetails.keySet()) {
          HashMap<Integer, Boolean> userIdActive = new HashMap<Integer, Boolean>();
          userIdActive = accountDetails.get(userId);
          for (int accountId : userIdActive.keySet()) {
            DatabaseInsertHelper.insertAccount(userId, userIdActive.get(accountId));
            System.out.println("ACCOUNT: userId: " + userId + " account Id:" + accountId + "Active"
                + userIdActive.get(accountId));
          }


        }
      }
      // AccountSummary
      int usercount1 = in.readInt();
      System.out.println("Read: " + usercount1);
      if (usercount1 > 0) {
        o = in.readObject();

        @SuppressWarnings("unchecked")
        HashMap<Integer, HashMap<Integer, Integer>> accountSummary =
            (HashMap<Integer, HashMap<Integer, Integer>>) o;
        for (Integer accountId : accountSummary.keySet()) {
          for (Integer itemId : accountSummary.get(accountId).keySet()) {
            Integer quantity = accountSummary.get(accountId).get(itemId);
            DatabaseInsertHelper.insertAccountLine(accountId, itemId, quantity);
            System.out.println("ACCOUNTSUMMARY: Account Id: " + accountId + "item Id: " + itemId
                + "quantity: " + quantity);
          }
        }
      }

      // Finish
      in.close();
      fileIn.close();
      System.out.println("That's everything being retrieved");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
