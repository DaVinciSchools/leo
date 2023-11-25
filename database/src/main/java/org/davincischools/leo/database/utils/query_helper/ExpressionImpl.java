package org.davincischools.leo.database.utils.query_helper;

import jakarta.persistence.criteria.CriteriaBuilder;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@Setter(AccessLevel.PACKAGE)
class ExpressionImpl<T> implements Expression<T> {

  private ExpressionType type;
  private T value;
  private List<Expression<T>> values;

  @SuppressWarnings("unchecked")
  public jakarta.persistence.criteria.Expression<T> toExpression(CriteriaBuilder builder) {
    return switch (type) {
      case CONCAT -> {
        jakarta.persistence.criteria.Expression<String> next =
            builder.concat(
                (jakarta.persistence.criteria.Expression<String>)
                    values.get(0).toExpression(builder),
                (jakarta.persistence.criteria.Expression<String>)
                    values.get(1).toExpression(builder));
        for (int i = 2; i < values.size(); ++i) {
          next =
              builder.concat(
                  next,
                  (jakarta.persistence.criteria.Expression<String>)
                      values.get(i).toExpression(builder));
        }
        yield (jakarta.persistence.criteria.Expression<T>) next;
      }
      case LITERAL -> builder.literal(value);
    };
  }

  @Override
  public String toString() {
    return switch (type) {
      case CONCAT -> {
        StringBuilder builder = new StringBuilder();
        builder.append("CONCAT(").append(values.get(0).toString());
        for (int i = 1; i < values.size(); ++i) {
          builder.append(", ").append(values.get(i).toString());
        }
        builder.append(")");
        yield builder.toString();
      }
      case LITERAL -> value.toString();
    };
  }
}
