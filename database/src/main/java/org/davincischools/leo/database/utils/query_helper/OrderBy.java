package org.davincischools.leo.database.utils.query_helper;

import static com.google.common.base.Preconditions.checkNotNull;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;

record OrderBy(Entity<?, ?> entity, OrderDirection orderDirection) {

  public jakarta.persistence.criteria.Order toOrder(CriteriaBuilder builder) {
    checkNotNull(builder);

    return switch (orderDirection) {
      case ASC -> builder.asc((Expression<?>) entity.getJpaEntity());
      case DESC -> builder.desc((Expression<?>) entity.getJpaEntity());
    };
  }
}
