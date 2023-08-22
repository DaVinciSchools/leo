package org.davincischools.leo.database.exceptions;

public class UnauthorizedUserX extends DatabaseException {

  public UnauthorizedUserX() {}

  public UnauthorizedUserX(String message) {
    super(message);
  }

  public UnauthorizedUserX(String message, Throwable cause) {
    super(message, cause);
  }
}
