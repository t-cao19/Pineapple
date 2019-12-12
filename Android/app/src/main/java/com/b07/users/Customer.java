package com.b07.users;

import java.io.Serializable;

/**
 * The Class Customer.
 */
public class Customer extends User implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * a constructor for a customer.
   *
   * @param id the id
   * @param name the name
   * @param age the age
   * @param address the address
   */
  public Customer(int id, String name, int age, String address) {
    this.setId(id);
    this.setName(name);
    this.setAge(age);
    this.setAddress(address);

  }

  /**
   * a constructor for customer with the authenticated field.
   *
   * @param id the id
   * @param name the name
   * @param age the age
   * @param address the address
   * @param authenticated the authenticated
   */
  public Customer(int id, String name, int age, String address, boolean authenticated) {
    this(id, name, age, address);
    this.setAuthenticated(authenticated);
  }

}
