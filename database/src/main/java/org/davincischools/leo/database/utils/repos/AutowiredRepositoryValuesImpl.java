package org.davincischools.leo.database.utils.repos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.davincischools.leo.database.utils.QueryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutowiredRepositoryValuesImpl implements AutowiredRepositoryValues {

  @PersistenceContext private EntityManager entityManager;
  @Autowired private QueryHelper queryHelper;

  @Override
  public EntityManager getEntityManager() {
    return entityManager;
  }

  @Override
  public QueryHelper getQueryHelper() {
    return queryHelper;
  }
}
