package org.davincischools.leo.server.utils;

import java.io.Serial;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.repos.UserXRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserXDetails extends User {

  @Serial private static final long serialVersionUID = -2242755819061848280L;

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
