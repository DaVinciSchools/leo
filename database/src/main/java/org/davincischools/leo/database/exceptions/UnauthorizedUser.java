package org.davincischools.leo.database.exceptions;

public class UnauthorizedUser extends DatabaseException {

  public UnauthorizedUser() {}

  public UnauthorizedUser(String message) {
    super(message);
  }

  public UnauthorizedUser(String message, Throwable cause) {
    super(message, cause);
  }
}
