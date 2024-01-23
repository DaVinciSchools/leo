package org.davincischools.leo.database.utils.query_helper;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.PluralAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@Setter(AccessLevel.PACKAGE)
public class Entity<P, S, F> implements Expression<F> {
  private static final AtomicInteger previousJoinId = new AtomicInteger(0);

  private final int id = previousJoinId.incrementAndGet();
  private final Entity<?, S, P> parent;
  private final EntityManager entityManager;
  private final QueryHelper queryHelper;
  private final Class<S> selectClass;
  private final Class<F> fromClass;

  // These must be <?> because singular attributes are ? and plural attributes are Collection<?>.
  private jakarta.persistence.criteria.Expression<?> jpaEntity;
  private jakarta.persistence.criteria.Expression<?> subqueryJpaEntity;

  private EntityType entityType;
  // This must be <?> because singular attributes are F2 and plural attributes are Collection<F2>.
  private Attribute<? super P, ?> attribute;
  private Entity<?, ?, S> selectEntity;

  private JoinType joinType;
  private final List<Predicate> on = new ArrayList<>();
  private final List<Predicate> where = new ArrayList<>();

  private final Map<Attribute<? super F, ?>, Entity<F, S, ?>> children = new HashMap<>();
  private final List<OrderBy> orderByList = new ArrayList<>();

  @Nullable private final ManagedType<F> managedType;
  // These will be the entities that ids will be pulled from.
  private final List<Entity<?, ?, ?>> nonIdManagedEntities = new ArrayList<>();
  private final List<Entity<?, ?, ?>> subqueries = new ArrayList<>();

  Entity(
      @Nullable Entity<?, S, P> parent,
      QueryHelper queryHelper,
      EntityManager entityManager,
      Class<S> selectClass,
      Class<F> fromClass) {
    this.parent = parent;
    this.queryHelper = checkNotNull(queryHelper);
    this.entityManager = checkNotNull(entityManager);
    this.selectClass = checkNotNull(selectClass);
    this.fromClass = checkNotNull(fromClass);

    ManagedType<F> managedType = null;
    try {
      managedType = entityManager.getMetamodel().managedType(fromClass);
      if (parent == null) {
        nonIdManagedEntities.add(this);
      }
    } catch (IllegalArgumentException e) {
      // There's no other simple way to see if something is a managed entity.
    }
    this.managedType = managedType;
  }

  public Entity<P, S, F> select() {
    checkState(
        selectClass == fromClass,
        "%s must be the same as %s",
        selectClass.getSimpleName(),
        fromClass.getSimpleName());
    @SuppressWarnings("unchecked")
    var sfChild = (Entity<P, S, S>) this;
    getQueryRoot().setSelectEntity(sfChild);
    return this;
  }

  private void setSelectEntity(Entity<?, ?, S> entity) {
    checkState(
        selectClass == entity.fromClass,
        "%s must be the same as %s",
        selectClass.getSimpleName(),
        entity.fromClass.getSimpleName());
    this.selectEntity = entity;
  }

  public <S2, F2> Entity<?, S2, F2> subquery(Class<S2> selectClass, Class<F2> fromClass) {
    checkNotNull(selectClass);
    checkNotNull(fromClass);

    var subquery = new Entity<>(null, queryHelper, entityManager, selectClass, fromClass);

    subqueries.add(subquery);

    return subquery;
  }

  public <S2, F2> Supplier<Entity<?, S2, F2>> supplier(Supplier<Entity<?, S2, F2>> supplier) {
    checkNotNull(supplier);

    return supplier;
  }

  public <S2, F2> Supplier<Entity<?, S2, F2>> supplier(
      Supplier<Entity<?, S2, F2>> supplier, Optional<Iterable<Integer>> inIds) {
    checkNotNull(supplier);
    checkNotNull(inIds);

    if (inIds.isPresent()) {
      supplier.get().requireId(inIds);
    }
    return supplier;
  }

  public Entity<P, S, F> notDeleted() {
    on.add(Predicate.isNull(getDeleted()));
    return this;
  }

  public <F2> Entity<F, S, F2> get(SingularAttribute<? super F, F2> attribute) {
    checkNotNull(attribute);

    return addChild(
        attribute,
        new Entity<>(this, queryHelper, entityManager, selectClass, attribute.getBindableJavaType())
            .setEntityType(EntityType.GET)
            .setAttribute(attribute));
  }

  public Entity<F, S, ?> get(String attributeName) {
    checkNotNull(attributeName);

    @SuppressWarnings("unchecked")
    var attribute =
        (Attribute<? super F, Object>)
            entityManager.getMetamodel().managedType(fromClass).getAttribute(attributeName);

    var child =
        new Entity<>(this, queryHelper, entityManager, selectClass, attribute.getJavaType());

    return addChild(attribute, child.setEntityType(EntityType.GET).setAttribute(attribute));
  }

  public Entity<F, S, Object> getId() {
    @SuppressWarnings("unchecked")
    var entity = (Entity<F, S, Object>) get("id");
    return entity;
  }

  public Entity<F, S, Instant> getDeleted() {
    @SuppressWarnings("unchecked")
    var entity = (Entity<F, S, Instant>) get("deleted");
    checkArgument(entity.getFromClass() == Instant.class);
    return entity;
  }

  public <F2> Entity<F, S, F2> join(SingularAttribute<F, F2> attribute, JoinType joinType) {
    checkNotNull(attribute);
    checkNotNull(joinType);

    return addChild(
        attribute,
        new Entity<>(this, queryHelper, entityManager, selectClass, attribute.getBindableJavaType())
            .setEntityType(EntityType.JOIN)
            .setJoinType(joinType)
            .setAttribute(attribute));
  }

  @SuppressWarnings("unchecked")
  public <C extends Collection<F2>, F2> Entity<F, S, F2> join(
      PluralAttribute<F, C, F2> attribute, JoinType joinType) {
    checkNotNull(attribute);
    checkNotNull(joinType);

    return addChild(
        (Attribute<F, F2>) attribute,
        new Entity<>(this, queryHelper, entityManager, selectClass, attribute.getBindableJavaType())
            .setEntityType(EntityType.JOIN)
            .setJoinType(joinType)
            .setAttribute(attribute));
  }

  public Entity<P, S, F> fetch() {
    if (getEntityType() != EntityType.JOIN) {
      return this;
    }
    if (getRoot().getSelectEntity().equals(this)) {
      return this;
    }

    if (getParent() == null) {
      throw new IllegalStateException("A fetch must occur beneath the select entity.");
    }

    getParent().fetch();
    setEntityType(EntityType.FETCH);
    return this;
  }

  public Entity<P, S, F> on(Predicate predicate) {
    checkNotNull(predicate);

    on.add(predicate);
    return this;
  }

  public Entity<P, S, F> where(Predicate predicate) {
    checkNotNull(predicate);

    where.add(predicate);

    return this;
  }

  public <ID> Entity<P, S, F> requireId(Optional<Iterable<ID>> optionalIds) {
    checkNotNull(optionalIds);

    optionalIds.ifPresent(
        ids -> {
          where(Predicate.in(getId(), Sets.newHashSet(ids)));
        });
    return this;
  }

  public <ID> Entity<P, S, F> requireNotId(Optional<Iterable<ID>> optionalIds) {
    checkNotNull(optionalIds);

    if (optionalIds.isPresent() && !Iterables.isEmpty(optionalIds.get())) {
      where(
          Predicate.or(
              Predicate.isNull(this),
              Predicate.not(Predicate.in(getId(), Sets.newHashSet(optionalIds.get())))));
    }

    return this;
  }

  private Entity<?, ?, ?> getRoot() {
    Entity<?, ?, ?> root = this;
    while (root.parent != null) {
      root = root.parent;
    }
    return root;
  }

  private Entity<?, S, ?> getQueryRoot() {
    Entity<?, S, ?> root = this;
    while (root.parent != null && !root.isQuery()) {
      root = root.parent;
    }
    return root;
  }

  private boolean isQuery() {
    return entityType == EntityType.ROOT || entityType == EntityType.SUBQUERY;
  }

  public Entity<P, S, F> orderByAsc(Entity<?, ?, ?> entity) {
    checkNotNull(entity);

    getRoot().orderByList.add(new OrderBy(entity, OrderDirection.ASC));

    return this;
  }

  public Entity<P, S, F> orderByDesc(Entity<?, ?, ?> entity) {
    checkNotNull(entity);

    getRoot().orderByList.add(new OrderBy(entity, OrderDirection.DESC));

    return this;
  }

  Entity<P, S, F> setEntityType(EntityType entityType) {
    checkNotNull(entityType);

    this.entityType = entityType;

    return this;
  }

  Entity<P, S, F> setJoinType(JoinType joinType) {
    // Null is a valid value.

    this.joinType = joinType;

    return this;
  }

  private <F2> Entity<F, S, F2> addChild(
      Attribute<? super F, F2> attribute, Entity<F, S, F2> child) {
    checkNotNull(attribute);
    checkNotNull(child);

    @SuppressWarnings("unchecked")
    Entity<F, S, F2> entity =
        (Entity<F, S, F2>)
            children.computeIfAbsent(
                attribute,
                k -> {
                  boolean isId = attribute instanceof SingularAttribute<?, ?> s && s.isId();
                  if (child.managedType != null && !isId) {
                    getRoot().nonIdManagedEntities.add(child);
                  }
                  return child;
                });

    // The (new) child should be a basic template for the entity. Not many fields should be set.
    checkArgument(child.getJpaEntity() == null);
    checkArgument(child.getParent() == entity.getParent());
    checkArgument(child.getSelectClass() == entity.getSelectClass());
    checkArgument(child.getFromClass() == entity.getFromClass());

    checkArgument(child.getOn().isEmpty());
    checkArgument(child.getWhere().isEmpty());
    checkArgument(child.getChildren().isEmpty());
    checkArgument(child.getOrderByList().isEmpty());

    // Merge changes in the child into the existing.
    entity.setEntityType(child.getEntityType()).setJoinType(child.getJoinType());
    entity.setAttribute(child.getAttribute());

    // Verify that the required fields are set on the result.
    checkState(entity.getEntityType() != null);
    checkState(entity.getParent() != null);
    checkState(entity.getAttribute() != null);
    checkState(entity.getSelectClass() != null);
    checkState(entity.getFromClass() != null);

    return entity;
  }

  @SuppressWarnings("unchecked")
  @Override
  public jakarta.persistence.criteria.Expression<F> toExpression(CriteriaBuilder builder) {
    if (entityType == EntityType.SUBQUERY) {
      return (jakarta.persistence.criteria.Expression<F>) subqueryJpaEntity;
    }
    return (jakarta.persistence.criteria.Expression<F>) jpaEntity;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    switch (entityType) {
      case ROOT -> sb.append("Root(")
          .append(fromClass.getSimpleName())
          .append("->")
          .append(selectClass.getSimpleName())
          .append(")");
      case SUBQUERY -> sb.append("Subquery(")
          .append(fromClass.getSimpleName())
          .append("->")
          .append(selectClass.getSimpleName())
          .append(")");
      case GET -> sb.append("Get(")
          .append(attribute.getName())
          .append(":")
          .append(fromClass.getSimpleName())
          .append(")");
      case JOIN, FETCH -> sb.append("Join(")
          .append(attribute.getName())
          .append(":")
          .append(fromClass.getSimpleName())
          .append(")");
    }

    return sb.toString();
  }

  public boolean isFetchingPluralAttributes() {
    for (var child : children.values()) {
      if (child.getAttribute() instanceof PluralAttribute<?, ?, ?>
              && child.getEntityType() == EntityType.FETCH
          || child.isFetchingPluralAttributes()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Entity<?, ?, ?> entity = (Entity<?, ?, ?>) o;
    return id == entity.id;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
