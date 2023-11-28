package org.davincischools.leo.database.utils.query_helper;

public interface QueryBuilder<T> {
  Entity<?, T> configureQuery(Entity<?, T> root);
}
