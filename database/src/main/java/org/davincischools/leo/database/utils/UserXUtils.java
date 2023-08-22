package org.davincischools.leo.database.utils;

import java.util.EnumSet;
import org.davincischools.leo.database.daos.UserX;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

public class UserXUtils {

  public enum Role {
    STUDENT,
    TEACHER,
    ADMIN_X
  }

  public static boolean checkPassword(UserX userX, String password) {
    if (userX.getEncodedPassword() == null) {
      return false;
    }
    return PasswordEncoderFactories.createDelegatingPasswordEncoder()
        .matches(password, userX.getEncodedPassword());
  }

  public static UserX setPassword(UserX userX, String password) {
    userX.setEncodedPassword(
        PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password));
    return userX;
  }

  public static boolean isEmailAddressValid(String emailAddress) {
    String[] split = emailAddress.split("@", 3);
    if (split.length != 2) {
      return false;
    }
    if (split[0].isEmpty() || split[1].isEmpty()) {
      return false;
    }
    if (split[1].indexOf('.') < 0) {
      return false;
    }
    return true;
  }

  public static EnumSet<Role> getRoles(UserX userX) {
    EnumSet<Role> roles = EnumSet.noneOf(Role.class);
    if (userX.getAdminX() != null) {
      roles.add(Role.ADMIN_X);
    }
    if (userX.getTeacher() != null) {
      roles.add(Role.TEACHER);
    }
    if (userX.getStudent() != null) {
      roles.add(Role.STUDENT);
    }
    return roles;
  }
}
