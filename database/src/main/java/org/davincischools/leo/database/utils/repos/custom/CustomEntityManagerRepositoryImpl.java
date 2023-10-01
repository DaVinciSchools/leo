package org.davincischools.leo.database.utils.repos.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class CustomEntityManagerRepositoryImpl implements CustomEntityManagerRepository {

  @PersistenceContext private EntityManager entityManager;

  @Override
  public EntityManager getEntityManager() {
    return entityManager;
  }
}
