package org.davincischools.leo.database.exceptions;

public class DatabaseException extends Exception {

  public DatabaseException() {}

  public DatabaseException(String message) {
    super(message);
  }

  public DatabaseException(String message, Throwable cause) {
    super(message, cause);
  }
}
