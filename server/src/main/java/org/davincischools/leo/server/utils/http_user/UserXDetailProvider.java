package org.davincischools.leo.server.utils.http_user;

import jakarta.persistence.EntityManager;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class UserXDetailProvider {

  @Bean
  static UserDetailsService userDetailsService(Database db, EntityManager entityManager) {
    return (String username) -> {
      UserX userX = db.getUserXRepository().findByEmailAddress(username).orElse(null);
      if (userX == null) {
        throw new UsernameNotFoundException("User " + username + " not found.");
      }

      return new UserXDetails(userX, entityManager);
    };
  }
}
