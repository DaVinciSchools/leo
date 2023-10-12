package org.davincischools.leo.database.utils.repos;

import jakarta.persistence.EntityManager;
import org.davincischools.leo.database.utils.QueryHelper;

public interface AutowiredRepositoryValues {

  EntityManager getEntityManager();

  QueryHelper getQueryHelper();
}
