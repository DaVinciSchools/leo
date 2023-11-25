package org.davincischools.leo.database.utils.query_helper;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import jakarta.persistence.criteria.CriteriaBuilder;
import java.util.Arrays;

public interface Expression<T> {

  Expression<Boolean> FALSE = Expression.literal(false);
  Expression<Boolean> TRUE = Expression.literal(true);

  @SafeVarargs
  static Expression<String> concat(Expression<String>... values) {
    checkNotNull(values);
    checkArgument(values.length >= 2);

    return new ExpressionImpl<String>()
        .setType(ExpressionType.CONCAT)
        .setValues(Arrays.asList(values));
  }

  static <V> Expression<V> literal(V value) {
    checkNotNull(value);
    checkArgument(!(value instanceof Predicate) && !(value instanceof Expression));

    return new ExpressionImpl<V>().setType(ExpressionType.LITERAL).setValue(value);
  }

  jakarta.persistence.criteria.Expression<T> toExpression(CriteriaBuilder builder);
}
