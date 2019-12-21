package com.b07.users;

import java.io.Serializable;

/**
 * The Class Employee.
 *
 * @author smetani1
 */
public class Employee extends User implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * a constructor for employee.
   *
   * @param id the id
   * @param name the name
   * @param age the age
   * @param address the address
   */
  public Employee(int id, String name, int age, String address) {
    this.setId(id);
    this.setName(name);
    this.setAge(age);
    this.setAddress(address);
  }

  /**
   * a constructor for employee with the authenticated field.
   *
   * @param id the id
   * @param name the name
   * @param age the age
   * @param address the address
   * @param authenticated the authenticated
   */
  public Employee(int id, String name, int age, String address, boolean authenticated) {
    this(id, name, age, address);
    this.setAuthenticated(authenticated);
  }
}
