package com.b07.store;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class SalesLogImpl implements SalesLog, Serializable {

  private static final long serialVersionUID = 2630160763277236689L;
  private List<Sale> salesList;
  private HashMap<Integer, Integer> itemMaps;
  private BigDecimal grandTotal;

  @Override
  public List<Sale> getSales() {
    return this.salesList;
  }

  @Override
  public void setSales(List<Sale> salesList) {
    this.salesList = salesList;
  }

  @Override
  public HashMap<Integer, Integer> getItemMaps() {
    return this.itemMaps;
  }

  @Override
  public void setItemMaps(HashMap<Integer, Integer> itemMaps) {
    this.itemMaps = itemMaps;
  }

  @Override
  public BigDecimal getGrandTotal() {
    return this.grandTotal;
  }

  @Override
  public void setGrandTotal(BigDecimal grandTotal) {
    this.grandTotal = grandTotal;
  }
}
