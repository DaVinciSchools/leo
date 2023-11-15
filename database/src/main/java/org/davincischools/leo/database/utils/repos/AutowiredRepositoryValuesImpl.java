package org.davincischools.leo.database.utils.repos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.QueryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutowiredRepositoryValuesImpl implements AutowiredRepositoryValues {

  @PersistenceContext private EntityManager entityManager;
  @Autowired private QueryHelper queryHelper;
  private UserX projectLeoCoach = null;

  @Override
  public EntityManager getEntityManager() {
    return entityManager;
  }

  @Override
  public QueryHelper getQueryHelper() {
    return queryHelper;
  }

  @Override
  public UserX getProjectLeoCoach(UserXRepository userXRepository) {
    if (projectLeoCoach != null) {
      return projectLeoCoach;
    }

    synchronized (this) {
      if (projectLeoCoach != null) {
        return projectLeoCoach;
      }

      projectLeoCoach =
          userXRepository
              .getUserXs(
                  new GetUserXsParams().setHasEmailAddress(UserXRepository.PROJECT_LEO_COACH_EMAIL))
              .stream()
              .findFirst()
              .orElse(null);
      if (projectLeoCoach != null) {
        return projectLeoCoach;
      }

      return projectLeoCoach =
          userXRepository.save(
              new UserX()
                  .setCreationTime(Instant.now())
                  .setEmailAddress(UserXRepository.PROJECT_LEO_COACH_EMAIL)
                  .setFirstName("Coach Leo")
                  .setLastName("")
                  .setEncodedPassword("")
                  .setEmailAddressVerified(true));
    }
  }
}
