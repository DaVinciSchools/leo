package org.davincischools.leo.database.utils.repos;

import jakarta.persistence.EntityManager;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.QueryHelper;

public interface AutowiredRepositoryValues {

  EntityManager getEntityManager();

  QueryHelper getQueryHelper();

  UserX getProjectLeoCoach(UserXRepository userXRepository);
}
