package com.b07.store;

import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.inventory.InventoryImpl;
import com.b07.inventory.Item;
import com.b07.inventory.ItemTypes;
import com.b07.serialize.DeserializeHelper;
import com.b07.serialize.SerializeHelper;
import com.b07.users.Admin;
import com.b07.users.Customer;
import com.b07.users.Employee;
import com.b07.users.Roles;
import com.b07.users.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Class SalesApplication.
 */
public class SalesApplication {
  /**
   * This is the main method to run your entire program! Follow the "Pulling it together"
   * instructions to finish this off.
   * 
   * @param argv unused.
   */
  public static void main(String[] argv) {

    Connection connection = DatabaseDriverExtender.connectOrCreateDataBase();

    if (connection == null) {
      System.out.println("NOOO");
    }

    try {
      if (argv.length == 1) {
        if (argv[0].equals("-1")) {

          DatabaseDriverExtender.initialize(connection);

          DatabaseInsertHelper.insertRole("ADMIN");
          DatabaseInsertHelper.insertRole("EMPLOYEE");
          DatabaseInsertHelper.insertRole("CUSTOMER");

          BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

          while (true) {
            System.out.println("Make new Admin: ");
            System.out.print("Name: ");
            String name = bufferedReader.readLine();
            if (name.equals("")) {
              System.out.println("No empty inputs!");
              continue;
            }

            System.out.print("Age: ");
            String age = bufferedReader.readLine();
            System.out.print("Address: ");
            String address = bufferedReader.readLine();
            System.out.print("Password: ");
            String pwd = bufferedReader.readLine();

            if (age.equals("") || address.equals("") || pwd.equals("")) {
              System.out.println("No empty inputs!");
              continue;
            }

            try {
              int userId =
                  DatabaseInsertHelper.insertNewUser(name, Integer.parseInt(age), address, pwd);
              int roleId = DatabaseSelectHelper.getRoleId("ADMIN");
              int userRoleCombiId = DatabaseInsertHelper.insertUserRole(userId, roleId);
              // Checking if everything is successfully inserted
              if (userId == -1 || roleId == -1 || userRoleCombiId == -1) {
                System.out.println("Could not record this new user!");
                continue;
              }

              System.out.println("Admin ID: " + userId);
              break;
            } catch (Exception e) {
              System.out.println("Please enter valid input!");
            }
          }

          while (true) {
            System.out.println("Make new Employee: ");
            System.out.print("Name: ");
            String name = bufferedReader.readLine();
            if (name.equals("")) {
              System.out.println("No empty inputs!");
              continue;
            }

            System.out.print("Age: ");
            String age = bufferedReader.readLine();
            System.out.print("Address: ");
            String address = bufferedReader.readLine();
            System.out.print("Password: ");
            String pwd = bufferedReader.readLine();

            if (age.equals("") || address.equals("") || pwd.equals("")) {
              System.out.println("No empty inputs!");
              continue;
            }

            try {
              int userId =
                  DatabaseInsertHelper.insertNewUser(name, Integer.parseInt(age), address, pwd);
              int roleId = DatabaseSelectHelper.getRoleId("EMPLOYEE");
              int userRoleCombiId = DatabaseInsertHelper.insertUserRole(userId, roleId);
              // Checking if everything is successfully inserted
              if (userId == -1 || roleId == -1 || userRoleCombiId == -1) {
                System.out.println("Could not record this new user!");
                continue;
              }

              System.out.println("Employee ID: " + userId);
              break;
            } catch (Exception e) {
              System.out.println("Please enter valid input!");
            }
          }

          int frId = DatabaseInsertHelper.insertItem(ItemTypes.FISHING_ROD.name(),
              new BigDecimal("25.99"));
          int hcId = DatabaseInsertHelper.insertItem(ItemTypes.HOCKEY_STICK.name(),
              new BigDecimal("15.99"));
          int pbId =
              DatabaseInsertHelper.insertItem(ItemTypes.PROTEIN_BAR.name(), new BigDecimal("3.99"));
          int rsId = DatabaseInsertHelper.insertItem(ItemTypes.RUNNING_SHOES.name(),
              new BigDecimal("65.99"));
          int skId =
              DatabaseInsertHelper.insertItem(ItemTypes.SKATES.name(), new BigDecimal("45.99"));

          // Checking if everything is successfully inserted
          if (frId == -1 || hcId == -1 || pbId == -1 || rsId == -1 || skId == -1) {
            System.out.println("Could not record some of the items!");
            throw new Exception("Something went wrong trying to insert new items.");
          }

          if (DatabaseInsertHelper.insertInventory(frId, 123) == -1
              || DatabaseInsertHelper.insertInventory(hcId, 20) == -1
              || DatabaseInsertHelper.insertInventory(pbId, 75) == -1
              || DatabaseInsertHelper.insertInventory(rsId, 32) == -1
              || DatabaseInsertHelper.insertInventory(skId, 5) == -1) {
            throw new Exception("Something went wrong trying to insert new items.");
          }

        } else if (argv[0].equals("-2")) {

          DatabaseDriverExtender.initialize(connection);

          DatabaseInsertHelper.insertRole("ADMIN");
          DatabaseInsertHelper.insertRole("EMPLOYEE");
          DatabaseInsertHelper.insertRole("CUSTOMER");

          DeserializeHelper.readDB();
        } else if (argv[0].equals("1")) {

          System.out.println("- Admin Login -");
          System.out.println("Press 0 to exit.");

          BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
          String input = "";

          Admin admin = null;

          while (true) {
            System.out.print("Admin ID: ");
            input = bufferedReader.readLine();

            if (input.equals("0")) {
              break;
            }

            try {
              Integer.parseInt(input);
            } catch (NumberFormatException e) {
              System.out.println("Admin ID must be a number.");
              continue;
            }

            int adminId = Integer.parseInt(input);
            System.out.print("Password: ");
            String password = bufferedReader.readLine();

            User user = DatabaseSelectHelper.getUserDetails(adminId);

            if (user == null) {
              System.out.println("Invalid id.");
            } else if (!(user instanceof Admin)) {
              System.out.println("This is not an admin account!");
            } else if (!user.authenticate(password)) {
              System.out.println("Invalid password.");
            } else {
              admin = (Admin) user;

              input = "";
              while (!input.equals("7")) {
                System.out.println("1. Promote Employee");
                System.out.println("2. View Books");
                System.out.println("3. View inactive accounts");
                System.out.println("4. View active accounts");
                System.out.println("5. Serialize Current Database");
                System.out.println("6. Deserialize Database");
                System.out.println("7. Exit Admin Mode");
                input = bufferedReader.readLine();

                if (input.equals("1")) {
                  System.out.println("Which employee would you like to promote? Press 0 to exit.");

                  List<Integer> list = DatabaseSelectHelper
                      .getUsersByRole(DatabaseSelectHelper.getRoleId(Roles.EMPLOYEE.name()));
                  for (int i = 0; i < list.size(); i++) {
                    System.out.println(list.get(i));
                  }

                  System.out.print("ID: ");
                  String employeeId = bufferedReader.readLine();

                  try {
                    if (!employeeId.equals("0")) {
                      User emUser =
                          DatabaseSelectHelper.getUserDetails(Integer.parseInt(employeeId));

                      if (emUser != null) {
                        Employee employee = (Employee) emUser;
                        admin.promoteEmployee(employee);
                      } else {
                        System.out.println("Employee promoted!");
                      }
                    }
                  } catch (Exception e) {
                    System.out.println("Please input an ID!");
                  }

                } else if (input.equals("2")) {
                  SalesLogImpl salesLog = (SalesLogImpl) DatabaseSelectHelper.consolidateSalesLog();
                  List<Sale> sales = salesLog.getSales();

                  for (Sale sale : sales) {
                    System.out.println("Customer: " + sale.getUser().getName());
                    System.out.println("Purchase Number: " + sale.getId());
                    System.out.println("Total Purchase Price: " + sale.getTotalPrice());
                    System.out.println("Itemized Breakdown: ");

                    HashMap<Item, Integer> itemMap = sale.getItemMap();
                    for (Item item : itemMap.keySet()) {
                      System.out.println(item.getName() + ": " + itemMap.get(item));
                    }

                    System.out.println("---------------------------------------");
                  }

                  HashMap<Integer, Integer> itemMaps = salesLog.getItemMaps();
                  for (int itemId : itemMaps.keySet()) {
                    Item item = DatabaseSelectHelper.getItem(itemId);
                    System.out
                        .println("Number of " + item.getName() + " sold: " + itemMaps.get(itemId));
                  }
                  System.out.println("TOTAL SALES: " + salesLog.getGrandTotal());
                } else if (input.equals("3") || input.equals("4")) {
                  System.out.println("List of customers");

                  int roleId = DatabaseSelectHelper.getRoleId(Roles.CUSTOMER.name());
                  List<Integer> customerIds = DatabaseSelectHelper.getUsersByRole(roleId);
                  for (int id : customerIds) {
                    System.out.println(id);
                  }

                  System.out.print("Input customer ID: ");
                  String customerId = bufferedReader.readLine();

                  int id = 0;
                  try {
                    id = Integer.parseInt(customerId);
                  } catch (Exception e) {
                    System.out.println("Numbers only!");
                  }

                  if (customerIds.contains(id)) {
                    if (input.equals("3")) {
                      List<Integer> inactive = DatabaseSelectHelper.getUserInactiveAccounts(id);

                      if (inactive.isEmpty()) {
                        System.out.println("No inactive accounts of this customer.");
                        continue;
                      }

                      for (int inactiveId : inactive) {
                        System.out.println(inactiveId);
                      }

                    } else { // input = 4
                      List<Integer> active = DatabaseSelectHelper.getUserActiveAccounts(id);

                      if (active.isEmpty()) {
                        System.out.println("No active accounts of this customer.");
                        continue;
                      }

                      for (int activeId : active) {
                        System.out.println(activeId);
                      }
                    }

                  } else {
                    System.out.println("Please input a customer id!");
                  }
                } else if (input.equals("5")) {
                  SerializeHelper.writeDb();
                } else if (input.equals("6")) {
                  // Clear the database
                  DatabaseDriverExtender.reInitialize();
                  DeserializeHelper.readDB();
                }
                System.out.println();
              }
              break;
            }
          }
        }
      }

      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
      String input = "";

      while (!input.equals("0")) {

        if (input.equals("1")) {
          runEmployee();
        } else if (input.equals("2")) {
          runCustomer();
        }

        System.out.println("1 - Employee Login");
        System.out.println("2 - Customer Login");
        System.out.println("0 - Exit");
        System.out.print("Enter Selection: ");
        input = bufferedReader.readLine();

        System.out.println();
      }

      System.out.println("Exiting...");

    } catch (Exception e) {
      System.out.println("Sorry, application not working!");
      e.printStackTrace();

    } finally {
      try {
        connection.close();
        System.out.println("Thank you!");
      } catch (Exception e) {
        System.out.println("Looks like it was closed already :)");
      }
    }

  }

  /**
   * Run employee.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws SQLException the SQL exception
   */
  private static void runEmployee() throws IOException, SQLException {

    System.out.println("- Employee Login -");
    System.out.println("Press 0 to exit.");

    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    String input = "";

    Employee employee = null;

    while (true) {
      System.out.print("Employee User ID: ");
      input = bufferedReader.readLine();

      if (input.equals("0")) {
        return;
      }

      try {
        Integer.parseInt(input);
      } catch (NumberFormatException e) {
        System.out.println("User ID must be a number");
        continue;
      }

      int userId = Integer.parseInt(input);
      System.out.print("Password: ");
      String password = bufferedReader.readLine();

      User user = DatabaseSelectHelper.getUserDetails(userId);

      if (user == null) {
        System.out.println("Invalid id.");
      } else if (!(user instanceof Employee)) {
        System.out.println("This is not an employee account!");
      } else if (!user.authenticate(password)) {
        System.out.println("Invalid password.");
      } else {
        employee = (Employee) user;
        break;
      }
    }

    EmployeeInterface emp = new EmployeeInterface(employee, DatabaseSelectHelper.getInventory());
    emp.setCurrentEmployee(employee);
    input = "";

    while (!input.equals("6")) {

      System.out.println("1. Authenticate new employee");
      System.out.println("2. Make new User");
      System.out.println("3. Make new account");
      System.out.println("4. Make new Employee");
      System.out.println("5. Restock Inventory");
      System.out.println("6. Exit");
      System.out.print("Selection: ");
      input = bufferedReader.readLine();

      if (input.equals("1")) {

        System.out.println("Login new employee:");
        System.out.println("Press 0 to exit.");

        String emId = "";

        Employee newEmployee = null;

        while (true) {
          System.out.print("Employee ID: ");
          emId = bufferedReader.readLine();

          if (emId.equals("0")) {
            return;
          }

          try {
            Integer.parseInt(emId);
          } catch (NumberFormatException e) {
            System.out.println("Employee ID must be a number.");
            continue;
          }

          int employeeId = Integer.parseInt(emId);
          System.out.print("Password: ");
          String password = bufferedReader.readLine();

          User user = DatabaseSelectHelper.getUserDetails(employeeId);

          if (user == null) {
            System.out.println("Invalid id.");
          } else if (!(user instanceof Employee)) {
            System.out.println("This is not an employee account!");
          } else if (!user.authenticate(password)) {
            System.out.println("Invalid password.");
          } else {
            newEmployee = (Employee) user;
            emp.setCurrentEmployee(newEmployee);
            System.out.println("New employee logged on.");
            break;
          }
        }

      } else if (input.equals("2")) {
        System.out.println("Make new customer - Enter information: ");
        System.out.print("Name: ");
        String name = bufferedReader.readLine();
        if (name.equals("")) {
          System.out.println("No empty inputs!");
          continue;
        }

        System.out.print("Age: ");
        String age = bufferedReader.readLine();
        System.out.print("Address: ");
        String address = bufferedReader.readLine();
        System.out.print("Password: ");
        String password = bufferedReader.readLine();

        if (age.equals("") || address.equals("") || password.equals("")) {
          System.out.println("No empty inputs!");
          continue;
        }

        try {
          int id = emp.createCustomer(name, Integer.parseInt(age), address, password);
          System.out.println("New customer created! ID: " + id);
        } catch (NumberFormatException e) {
          System.out.println("Age must be a number!");
        }

      } else if (input.equals("3")) {
        System.out.println("Enter customer ID to be associated with account");

        int roleId = DatabaseSelectHelper.getRoleId(Roles.CUSTOMER.name());
        List<Integer> customerIds = DatabaseSelectHelper.getUsersByRole(roleId);
        for (int id : customerIds) {
          System.out.println(id);
        }

        System.out.print("Input customer ID: ");
        String customerId = bufferedReader.readLine();

        int id = 0;
        try {
          id = Integer.parseInt(customerId);
        } catch (Exception e) {
          System.out.println("Numbers only!");
        }

        runCreateAccount(id);

      } else if (input.equals("4")) {
        System.out.println("Make new employee - Enter information: ");
        System.out.print("Name: ");
        String name = bufferedReader.readLine();
        if (name.equals("")) {
          System.out.println("No empty inputs!");
          continue;
        }

        System.out.print("Age: ");
        String age = bufferedReader.readLine();
        System.out.print("Address: ");
        String address = bufferedReader.readLine();
        System.out.print("Password: ");
        String password = bufferedReader.readLine();

        if (age.equals("") || address.equals("") || password.equals("")) {
          System.out.println("No empty inputs!");
          continue;
        }

        try {
          int id = emp.createEmployee(name, Integer.parseInt(age), address, password);
          System.out.println("New employee created! ID: " + id);
        } catch (NumberFormatException e) {
          System.out.println("Age must be a number!");
        }

      } else if (input.equals("5")) {

        HashMap<Item, Integer> invMap = emp.getCurrentInventory().getItemMap();
        for (Item itm : invMap.keySet()) {
          System.out.println(
              "ID:" + itm.getId() + " Item:" + itm.getName() + ", Qty: " + invMap.get(itm));
        }

        System.out.print("Enter item id to restock: ");
        String itemId = bufferedReader.readLine();

        try {
          Integer.parseInt(itemId);
        } catch (NumberFormatException e) {
          System.out.println("Item ID must be a number!");
          continue;
        }

        Item item = DatabaseSelectHelper.getItem(Integer.parseInt(itemId));
        if (item == null) {
          continue;
        }

        System.out.print("Quantity: ");

        String quantity = bufferedReader.readLine();

        try {
          Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
          System.out.println("Quantity must be a number!");
          continue;
        }

        emp.restockInventory(item, Integer.parseInt(quantity));

        emp.setCurrentInventory(DatabaseSelectHelper.getInventory());

        invMap = emp.getCurrentInventory().getItemMap();
        for (Item itm : invMap.keySet()) {
          System.out.println(
              "ID:" + itm.getId() + " Item:" + itm.getName() + ", Qty: " + invMap.get(itm));
        }

        System.out.println("Successfully restocked.");

      }
      System.out.println();
    }
  }

  /**
   * Run customer.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws SQLException the SQL exception
   */
  private static void runCustomer() throws IOException, SQLException {

    System.out.println("- Customer Login -");
    System.out.println("Press 0 to exit.");

    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    String input = "";

    Customer customer = null;

    while (true) {
      System.out.print("Customer User ID: ");
      input = bufferedReader.readLine();

      if (input.equals("0")) {
        return;
      }

      try {
        Integer.parseInt(input);
      } catch (NumberFormatException e) {
        System.out.println("Customer User ID must be a number");
        continue;
      }

      int userId = Integer.parseInt(input);
      System.out.print("Password: ");
      String password = bufferedReader.readLine();

      User user = DatabaseSelectHelper.getUserDetails(userId);

      if (user == null) {
        System.out.println("Invalid id.");
      } else if (!(user instanceof Customer)) {
        System.out.println("This is not an customer account!");
      } else if (!user.authenticate(password)) {
        System.out.println("Invalid password.");
      } else {
        customer = (Customer) user;
        break;
      }

    }

    ShoppingCart cart = new ShoppingCart(customer);
    input = "";
    List<Integer> restoredAccounts = new ArrayList<Integer>();

    while (!input.equals("7")) {

      System.out.println("1. List current items in cart");
      System.out.println("2. Add a quantity of an item to the cart");
      System.out.println("3. Check total price of items in the cart");
      System.out.println("4. Remove a quantity of an item from the cart");
      System.out.println("5. check out");
      System.out.println("6. Restore previous shopping cart");
      System.out.println("7. Exit");
      System.out.print("Selection: ");
      input = bufferedReader.readLine();

      if (input.equals("1")) {
        List<Item> cartItems = cart.getItems();
        for (Item i : cartItems) {
          System.out.println(
              "ID: " + i.getId() + " Name: " + i.getName() + " Qty: " + cart.getItemQty(i));
        }

        if (cartItems.size() == 0) {
          System.out.println("Cart is empty!");
        }

      } else if (input.equals("2")) {
        InventoryImpl inventory = (InventoryImpl) DatabaseSelectHelper.getInventory();
        HashMap<Item, Integer> itemMap = inventory.getItemMap();
        for (Item item : itemMap.keySet()) {
          System.out.println("ID:" + item.getId() + " " + item.getName());
        }

        System.out.print("Enter item id to add to cart: ");
        String itemId = bufferedReader.readLine();

        try {
          Integer.parseInt(itemId);
        } catch (NumberFormatException e) {
          System.out.println("Item ID must be a number!");
          continue;
        }

        Item item = DatabaseSelectHelper.getItem(Integer.parseInt(itemId));

        if (item == null) {
          System.out.println("Item doesn't exist!");
          continue;
        }

        System.out.print("Quantity: ");
        String quantity = bufferedReader.readLine();

        try {
          cart.addItem(item, Integer.parseInt(quantity));
          System.out.println("Successfully added item to cart");
        } catch (NumberFormatException e) {
          System.out.println("Quantity must be a number!");
        }

      } else if (input.equals("3")) {
        System.out.println("Total Price: " + cart.getTotal());

      } else if (input.equals("4")) {
        List<Item> cartItems = cart.getItems();
        for (Item i : cartItems) {
          System.out.println(
              "ID: " + i.getId() + " Name: " + i.getName() + " Qty: " + cart.getItemQty(i));
        }

        System.out.print("Enter item id to be removed from cart: ");
        String itemId = bufferedReader.readLine();

        try {
          Integer.parseInt(itemId);
        } catch (NumberFormatException e) {
          System.out.println("Item ID must be a number!");
          continue;
        }

        Item item = DatabaseSelectHelper.getItem(Integer.parseInt(itemId));

        if (item == null) {
          System.out.println("Item doesn't exist!");
          continue;
        }

        System.out.print("Quantity: ");
        String quantity = bufferedReader.readLine();

        try {
          cart.removeItem(item, Integer.parseInt(quantity));
        } catch (NumberFormatException e) {
          System.out.println("Quantity must be a number!");
        }

      } else if (input.equals("5")) {
        BigDecimal total =
            cart.getTotal().multiply(cart.getTaxRate()).setScale(2, RoundingMode.HALF_UP);
        System.out.println("TOTAL: " + total);
        System.out.println("Processing checkout...");

        if (cart.checkout()) {
          System.out.println("Checkout successful!");
          // Set all restored accounts within this session to be false
          for (int i = 0; i < restoredAccounts.size(); i++) {
            DatabaseUpdateHelper.updateAccountStatus(restoredAccounts.get(i), false);
          }
          // Clear the list of restored accounts
          restoredAccounts.clear();

          System.out.println("Continue with shopping? y/n");

          String cont = bufferedReader.readLine();

          while (!cont.equals("n") && !cont.equals("y")) {
            System.out.println("Please put valid input.");
            cont = bufferedReader.readLine();
          }

          if (cont.equals("n")) {
            System.out.println("Thanks for shopping! Logging out...");
            break;
          }

        } else {
          System.out.println("Checkout unsuccessful! Insufficent inventory.");
        }
      } else if (input.equals("6")) {

        List<Integer> accountIds = DatabaseSelectHelper.getUserActiveAccounts(customer.getId());
        if (!accountIds.isEmpty()) {
          int accountId;
          int i;
          if (accountIds.size() > 1) {
            System.out.println("Multiple account IDs detected");

            for (i = 0; i < accountIds.size(); i++) {
              System.out.println("Account ID:" + accountIds.get(i));
            }

            System.out.println("Choose account ID to get saved cart from");
            accountId = Integer.parseInt(bufferedReader.readLine());

            while (!accountIds.contains(accountId)) {
              System.out.println("Invalid account ID!");
              System.out.println("Please choose account ID to get saved cart from");
              accountId = Integer.parseInt(bufferedReader.readLine());
            }
          } else {
            accountId = accountIds.get(0);
          }

          HashMap<Integer, Integer> itemsToQuan = DatabaseSelectHelper.getAccountDetails(accountId);
          if (!itemsToQuan.isEmpty()) {
            itemsToQuan.forEach((k, v) -> {
              try {
                Item oneItem = DatabaseSelectHelper.getItem(k);
                cart.addItem(oneItem, v);
              } catch (SQLException e) {
                System.out.println("Cannot retrieve items from database");
              }
            });

            System.out.println("Succesfully retrieved cart!");
            restoredAccounts.add(accountId);
          } else {
            System.out.println("Sorry no previous shopping cart has been saved");
          }
        } else {
          System.out.println(
              "Invalid option as no account has been created! Please see employee for details");
        }
      }
      System.out.println();
    }

    System.out.println("Would you like to save your current shopping cart for next time? (y/n)");
    String save = bufferedReader.readLine();

    while (!(save.equals("n") || save.equals("y"))) {
      System.out.println("Invalid input! Would you like to save your cart? (y/n)");
      save = bufferedReader.readLine();
    }

    if (save.equals("y")) {
      if (!cart.getItems().isEmpty()) {
        List<Integer> accountIds = DatabaseSelectHelper.getUserActiveAccounts(customer.getId());
        int accountId;
        if (!accountIds.isEmpty()) {
          int i;
          if (accountIds.size() > 1) {
            System.out.println("Multiple account IDs detected");

            for (i = 0; i < accountIds.size(); i++) {
              System.out.println("Account ID:" + accountIds.get(i));
            }

            System.out.println("Choose account ID to save the current cart to");
            accountId = Integer.parseInt(bufferedReader.readLine());

            while (!accountIds.contains(accountId)) {
              System.out.println("Invalid account ID!");
              System.out.println("Please choose account ID to save the current cart to");
              accountId = Integer.parseInt(bufferedReader.readLine());
            }

          } else {
            accountId = accountIds.get(0);
          }

          Item oneItem;
          List<Item> cartItems = cart.getItems();
          HashMap<Integer, Integer> itemToQuantity =
              DatabaseSelectHelper.getAccountDetails(accountId);
          List<Integer> accountItems = new ArrayList<Integer>(itemToQuantity.keySet());

          for (i = 0; i < cartItems.size(); i++) {
            oneItem = cartItems.get(i);
            if (!accountItems.contains(oneItem.getId())) {
              DatabaseInsertHelper.insertAccountLine(accountId, oneItem.getId(),
                  cart.getItemQty(oneItem));
            } else {
              System.out.println("Item already saved detected! Not saved again");
            }
          }
          System.out.println("Current cart saved successfully for next time!");
        } else {
          System.out
              .println("Cannot save cart as no account has been created. Please see employee");
        }
      } else {
        System.out.println("Current cart is empty! Nothing to save");
      }
    }
  }

  /**
   * Run create account.
   * 
   * @param customerId the id of the customer
   */
  private static void runCreateAccount(int customerId) {

    try {
      User user = DatabaseSelectHelper.getUserDetails(customerId);
      if (!user.equals(null) && user instanceof Customer) {
        int accountId = DatabaseInsertHelper.insertAccount(customerId, true);
        System.out.println("Account succesfully created with account ID:" + accountId);
      } else {
        System.out.println("Sorry account not created as ID passed in was not a valid customer ID");
      }

    } catch (Exception e) {
      System.out.println("Sorry unable to create account");
    }
  }

}
