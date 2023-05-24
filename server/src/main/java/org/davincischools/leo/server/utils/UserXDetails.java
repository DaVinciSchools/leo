package org.davincischools.leo.server.utils;

import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.repos.UserXRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserXDetails extends User {

  private final UserX userX;

  public UserXDetails(UserX userX) {
    super(
        userX.getEmailAddress(),
        userX.getEncodedPassword(),
        UserXRepository.getRoles(userX).stream()
            .map(Enum::name)
            .map(SimpleGrantedAuthority::new)
            .toList());
    this.userX = userX;
  }

  public UserX getUserX() {
    return userX;
  }
}
