package org.davincischools.leo.database.utils.query_helper;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Streams.stream;

import jakarta.persistence.criteria.CriteriaBuilder;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@Setter(AccessLevel.PRIVATE)
public class Predicate implements Expression<Boolean> {

  public static final Predicate FALSE = new Predicate().setType(PredicateType.FALSE);
  public static final Predicate TRUE = new Predicate().setType(PredicateType.TRUE);

  private PredicateType type;
  private Expression<?> value;
  private Expression<?> left;
  private Expression<?> right;
  private List<? extends Expression<?>> values;

  public static <V> Predicate eq(V left, V right) {
    checkNotNull(left);
    checkNotNull(right);

    return new Predicate()
        .setType(PredicateType.EQ)
        .setLeft(left instanceof Expression ? (Expression<?>) left : Expression.literal(left))
        .setRight(right instanceof Expression ? (Expression<?>) right : Expression.literal(right));
  }

  public static <V> Predicate eq(Expression<V> left, V right) {
    return eq(left, Expression.literal(right));
  }

  public static <V> Predicate in(Expression<V> left, Iterable<? extends V> values) {
    checkNotNull(left);
    checkNotNull(values);

    return new Predicate()
        .setType(PredicateType.IN)
        .setLeft(left)
        .setValues(stream(values).map(Expression::literal).toList());
  }

  public static Predicate isNotNull(Expression<?> value) {
    checkNotNull(value);

    return new Predicate().setType(PredicateType.IS_NOT_NULL).setValue(value);
  }

  public static Predicate isNull(Expression<?> value) {
    checkNotNull(value);

    return new Predicate().setType(PredicateType.IS_NULL).setValue(value);
  }

  public static Predicate isTrue(Expression<Boolean> value) {
    checkNotNull(value);

    return eq(value, Expression.TRUE);
  }

  public static Predicate like(Expression<String> left, String right) {
    return new Predicate()
        .setType(PredicateType.LIKE)
        .setLeft(left)
        .setRight(Expression.literal(right));
  }

  public static Predicate like(Expression<String> left, Expression<String> right) {
    return new Predicate().setType(PredicateType.LIKE).setLeft(left).setRight(right);
  }

  public static <V> Predicate neq(V left, V right) {
    checkNotNull(left);
    checkNotNull(right);

    return new Predicate()
        .setType(PredicateType.NEQ)
        .setLeft(left instanceof Expression ? (Expression<?>) left : Expression.literal(left))
        .setRight(right instanceof Expression ? (Expression<?>) right : Expression.literal(right));
  }

  public static <V> Predicate neq(Expression<V> left, V right) {
    return neq(
        left, right instanceof Expression ? (Expression<?>) right : Expression.literal(right));
  }

  public static Predicate not(Expression<Boolean> expression) {
    checkNotNull(expression);

    return new Predicate().setType(PredicateType.NOT).setValue(expression);
  }

  @SafeVarargs
  public static Predicate or(Expression<Boolean>... expressions) {
    checkNotNull(expressions);

    return new Predicate().setType(PredicateType.OR).setValues(Arrays.asList(expressions));
  }

  @SuppressWarnings("unchecked")
  jakarta.persistence.criteria.Predicate toPredicate(CriteriaBuilder builder) {
    checkNotNull(builder);

    return switch (type) {
      case EQ -> builder.equal(left.toExpression(builder), right.toExpression(builder));
      case FALSE -> builder.isTrue(builder.literal(false));
      case IN -> values.isEmpty()
          ? FALSE.toPredicate(builder)
          : left.toExpression(builder)
              .in(values.stream().map(v -> v.toExpression(builder)).toList());
      case IS_NOT_NULL -> builder.isNotNull(value.toExpression(builder));
      case IS_NULL -> builder.isNull(value.toExpression(builder));
      case LIKE -> builder.like(
          (jakarta.persistence.criteria.Expression<String>) left.toExpression(builder),
          (jakarta.persistence.criteria.Expression<String>) right.toExpression(builder));
      case NEQ -> builder.notEqual(left.toExpression(builder), right.toExpression(builder));
      case NOT -> builder.isFalse(
          (jakarta.persistence.criteria.Expression<Boolean>) value.toExpression(builder));
      case OR -> builder.or(
          values.stream()
              .map(v -> Predicate.isTrue((Expression<Boolean>) v).toPredicate(builder))
              .toArray(jakarta.persistence.criteria.Predicate[]::new));
      case TRUE -> builder.isTrue(builder.literal(true));
    };
  }

  @Override
  public jakarta.persistence.criteria.Expression<Boolean> toExpression(CriteriaBuilder builder) {
    return toPredicate(builder);
  }

  @Override
  public String toString() {
    return switch (type) {
      case EQ -> left.toString() + " = " + right.toString();
      case FALSE -> "FALSE";
      case IN -> left.toString() + " IN (" + values.toString() + ")";
      case IS_NOT_NULL -> value.toString() + " IS NOT NULL";
      case IS_NULL -> value.toString() + " IS NULL";
      case LIKE -> left.toString() + " LIKE " + right.toString();
      case NEQ -> left.toString() + " != " + right.toString();
      case NOT -> "NOT " + value.toString();
      case OR -> values.stream()
          .map(Expression::toString)
          .reduce((a, b) -> a + "\n  OR " + b)
          .orElse("?");
      case TRUE -> "TRUE";
    };
  }
}
