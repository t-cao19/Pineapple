package com.b07.users;

import com.b07.database.helper.DatabaseUpdateHelper;
import java.io.Serializable;
import java.sql.SQLException;

/**
 * The Class Admin.
 *
 * @author smetani1
 */
public class Admin extends User implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new admin.
   *
   * @param id the id
   * @param name the name
   * @param age the age
   * @param address the address
   */
  public Admin(int id, String name, int age, String address) {
    this.setId(id);
    this.setName(name);
    this.setAge(age);
    this.setAddress(address);
  }

  /**
   * Instantiates a new admin.
   *
   * @param id the id
   * @param name the name
   * @param age the age
   * @param address the address
   * @param authenticated the authenticated
   */
  public Admin(int id, String name, int age, String address, boolean authenticated) {
    this(id, name, age, address);
    this.setAuthenticated(authenticated);
  }



  /**
   * Promote employee.
   *
   * @param employee the employee
   * @return true, if successful
   */
  public boolean promoteEmployee(Employee employee) {

    try {
      return DatabaseUpdateHelper.updateUserRole(this.getId(), employee.getId());
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

}
