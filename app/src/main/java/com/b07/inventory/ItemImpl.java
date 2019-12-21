package com.b07.inventory;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The Class ItemImpl.
 */
public class ItemImpl implements Item, Serializable {

  private static final long serialVersionUID = 1L;
  private int id;
  private String name;
  private BigDecimal price;

  /**
   * Instantiates a new item impl.
   */
  public ItemImpl() {
    this.id = -1;
    this.name = null;
    this.price = null;
  }

  /**
   * Instantiates a new item impl.
   *
   * @param id the id
   * @param name the name
   * @param price the price
   */
  public ItemImpl(int id, String name, BigDecimal price) {
    this.id = id;
    this.name = name;
    this.price = price;
  }

  /**
   * Gets the id.
   *
   * @return the id
   */
  @Override
  public int getId() {
    return this.id;
  }

  /**
   * Sets the id.
   *
   * @param id the new id
   */
  @Override
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Sets the name.
   *
   * @param name the new name
   */
  @Override
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the price.
   *
   * @return the price
   */
  @Override
  public BigDecimal getPrice() {
    return this.price;
  }

  /**
   * Sets the price.
   *
   * @param price the new price
   */
  @Override
  public void setPrice(BigDecimal price) {
    this.price = price;
  }

}
