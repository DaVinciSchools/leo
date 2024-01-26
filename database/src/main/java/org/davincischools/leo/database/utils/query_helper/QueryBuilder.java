package org.davincischools.leo.database.utils.query_helper;

public interface QueryBuilder<S, F> {
  Entity<?, ?, S> configureQuery(Entity<?, S, F> from);
}
