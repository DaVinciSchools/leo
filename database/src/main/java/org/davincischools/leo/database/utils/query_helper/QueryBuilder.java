package org.davincischools.leo.database.utils.query_helper;

public interface QueryBuilder<T> {
  void configureQuery(Entity<?, T> root);
}
