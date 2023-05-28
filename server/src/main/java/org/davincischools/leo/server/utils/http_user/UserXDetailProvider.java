package org.davincischools.leo.server.utils.http_user;

import java.util.Optional;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class UserXDetailProvider {

  @Bean
  static UserDetailsService userDetailsService(@Autowired Database db) {
    return (String username) -> {
      Optional<UserX> optionalUserX = db.getUserXRepository().findByEmailAddress(username);
      if (optionalUserX.isEmpty()) {
        throw new UsernameNotFoundException("User " + username + " not found.");
      }

      return new UserXDetails(optionalUserX.get());
    };
  }
}
