package org.davincischools.leo.database.utils.query_helper;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.Attribute;
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
  private final EntityManager entityManager;
  private final QueryHelper queryHelper;
  private final Class<S> selectClass;
  private final Class<F> fromClass;

  // This must be <?> because singular attributes are F2 and plural attributes are Collection<F2>.
  private jakarta.persistence.criteria.Expression<?> jpaEntity;

  private Entity<?, S, P> parent;
  private EntityType entityType;
  // This must be <?> because singular attributes are F2 and plural attributes are Collection<F2>.
  private Attribute<? super P, ?> attribute;
  private Entity<?, ?, S> selectEntity;

  private JoinType joinType;
  private final List<Predicate> on = new ArrayList<>();
  private final List<Predicate> where = new ArrayList<>();

  private final Map<Attribute<? super F, ?>, Entity<F, S, ?>> children = new HashMap<>();
  private final List<OrderBy> orderByList = new ArrayList<>();

  Entity(
      QueryHelper queryHelper,
      EntityManager entityManager,
      Class<S> selectClass,
      Class<F> fromClass) {
    this.queryHelper = checkNotNull(queryHelper);
    this.entityManager = checkNotNull(entityManager);
    this.selectClass = checkNotNull(selectClass);
    this.fromClass = checkNotNull(fromClass);
  }

  public Entity<P, S, F> select() {
    checkState(selectClass == fromClass);
    @SuppressWarnings("unchecked")
    var sfChild = (Entity<P, S, S>) this;
    setSelectEntity(sfChild);
    return this;
  }

  private void setSelectEntity(Entity<?, ?, S> entity) {
    checkState(selectClass == entity.fromClass);
    this.selectEntity = entity;
    if (parent != null && parent.selectEntity == null) {
      parent.setSelectEntity(entity);
    }
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
        new Entity<F, S, F2>(
                queryHelper, entityManager, selectClass, attribute.getBindableJavaType())
            .setEntityType(EntityType.GET)
            .setParent(this)
            .setAttribute(attribute));
  }

  public <F2> Entity<F, S, F2> get(String attributeName, Class<F2> getClass) {
    checkNotNull(attributeName);
    checkNotNull(getClass);

    @SuppressWarnings("unchecked")
    var attribute =
        (Attribute<? super F, F2>)
            entityManager.getMetamodel().managedType(fromClass).getAttribute(attributeName);
    checkArgument(getClass.isAssignableFrom(attribute.getJavaType()));

    return addChild(
        attribute,
        new Entity<F, S, F2>(queryHelper, entityManager, selectClass, getClass)
            .setEntityType(EntityType.GET)
            .setParent(this)
            .setAttribute(attribute));
  }

  public Entity<F, S, Object> getId() {
    return get("id", Object.class);
  }

  public Entity<F, S, Instant> getDeleted() {
    return get("deleted", Instant.class);
  }

  public <F2> Entity<F, S, F2> join(SingularAttribute<? super F, F2> attribute, JoinType joinType) {
    checkNotNull(attribute);
    checkNotNull(joinType);

    return addChild(
        attribute,
        new Entity<F, S, F2>(
                queryHelper, entityManager, selectClass, attribute.getBindableJavaType())
            .setEntityType(EntityType.JOIN)
            .setParent(this)
            .setJoinType(joinType)
            .setAttribute(attribute));
  }

  @SuppressWarnings("unchecked")
  public <C extends Collection<F2>, F2> Entity<F, S, F2> join(
      PluralAttribute<? super F, C, F2> attribute, JoinType joinType) {
    checkNotNull(attribute);
    checkNotNull(joinType);

    var child =
        addChild(
            (Attribute<? super F, F2>) attribute,
            new Entity<F, S, F2>(
                    queryHelper, entityManager, selectClass, attribute.getBindableJavaType())
                .setEntityType(EntityType.JOIN)
                .setParent(this)
                .setJoinType(joinType)
                .setAttribute(attribute));
    return child;
  }

  public Entity<P, S, F> fetch() {
    if (entityType == EntityType.JOIN) {
      setEntityType(EntityType.FETCH);
      if (getParent() != null) {
        getParent().fetch();
      }
    }

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

  public Entity<P, S, F> requireId(Optional<Iterable<Integer>> optionalIds) {
    checkNotNull(optionalIds);

    optionalIds.ifPresent(
        ids -> {
          where(Predicate.in(getId(), Sets.newHashSet(ids)));
        });
    return this;
  }

  public Entity<P, S, F> requireNotId(Optional<Iterable<Integer>> optionalIds) {
    checkNotNull(optionalIds);

    if (optionalIds.isPresent() && !Iterables.isEmpty(optionalIds.get())) {
      where(
          Predicate.or(
              Predicate.isNull(this),
              Predicate.not(Predicate.in(getId(), Sets.newHashSet(optionalIds.get())))));
    }

    return this;
  }

  private Entity<?, S, ?> getRoot() {
    Entity<?, S, ?> root = this;
    while (root.parent != null) {
      root = root.parent;
    }
    return root;
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

    if (entityType == EntityType.FETCH) {
      if (parent != null && parent.getEntityType() == EntityType.JOIN) {
        parent.setEntityType(EntityType.FETCH);
      }
    }

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
    Entity<F, S, F2> entity = (Entity<F, S, F2>) children.computeIfAbsent(attribute, k -> child);

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
    return (jakarta.persistence.criteria.Expression<F>) jpaEntity;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    switch (entityType) {
      case ROOT -> sb.append("Root(").append(fromClass.getSimpleName()).append(")");
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
}
