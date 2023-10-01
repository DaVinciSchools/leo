package org.davincischools.leo.database.utils.repos.custom;

import jakarta.persistence.EntityManager;

public interface CustomEntityManagerRepository {
  EntityManager getEntityManager();
}
