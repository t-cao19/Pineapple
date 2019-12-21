package com.b07.store;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface SalesLog {

  public List<Sale> getSales();

  public void setSales(List<Sale> salesList);

  public HashMap<Integer, Integer> getItemMaps();

  public void setItemMaps(HashMap<Integer, Integer> itemMaps);

  public BigDecimal getGrandTotal();

  public void setGrandTotal(BigDecimal grandTotal);

}
