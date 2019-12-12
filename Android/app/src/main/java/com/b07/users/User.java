package com.b07.users;

import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.security.PasswordHelpers;
import java.io.Serializable;

/**
 * The Class User.
 */
public abstract class User implements Serializable {

  private static final long serialVersionUID = 1L;
  private int id;
  private String name;
  private int age;
  private String address;
  private int roleId;
  private transient boolean authenticated;

  /**
   * Gets the id.
   *
   * @return the id
   */
  public int getId() {
    return this.id;
  }

  /**
   * Sets the id.
   *
   * @param id the new id
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets the name.
   *
   * @param name the new name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the age.
   *
   * @return the age
   */
  public int getAge() {
    return this.age;
  }

  /**
   * Sets the age.
   *
   * @param age the new age
   */
  public void setAge(int age) {
    this.age = age;
  }

  /**
   * Gets the address.
   *
   * @return the address
   */
  public String getAddress() {
    return this.address;
  }

  /**
   * Sets the address.
   *
   * @param address the new address
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Gets the role id.
   *
   * @return the role id
   */
  public int getRoleId() {
    return this.roleId;
  }

  /**
   * Authenticate.
   *
   * @param password the password
   * @return true, if successful
   */
  public final boolean authenticate(String password) {

    if (!authenticated) {
      authenticated =
          PasswordHelpers.comparePassword(DatabaseSelectHelper.getPassword(this.id), password);
    }
    return authenticated;
  }

  /**
   * Sets the authenticated.
   *
   * @param auth the new authenticated
   */
  protected void setAuthenticated(boolean auth) {
    this.authenticated = auth;
  }

}
