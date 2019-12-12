package com.b07.database.helper;

import com.b07.database.DatabaseDriver;
import java.sql.Connection;

/**
 * The Class DatabaseDriverHelper.
 */
public class DatabaseDriverHelper extends DatabaseDriver {

  /**
   * Connect or create data base.
   *
   * @return the connection
   */
  protected static Connection connectOrCreateDataBase() {
    return DatabaseDriver.connectOrCreateDataBase();
  }

}
