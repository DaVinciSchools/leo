package org.davincischools.leo.database.utils.query_helper;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@Setter(AccessLevel.PACKAGE)
public class Entity<W, E> implements Expression<E> {
  private static final AtomicInteger previousJoinId = new AtomicInteger(0);

  private final int id = previousJoinId.incrementAndGet();
  private final EntityManager entityManager;

  // This must be <?> because singular attributes are E and plural attributes are Collection<E>.
  private jakarta.persistence.criteria.Expression<?> jpaEntity;

  private Entity<?, W> parent;
  private EntityType entityType;
  private Attribute<? super W, ?> attribute;
  private Class<E> entityClass;
  private final List<Entity<Object, Object>> roots = new ArrayList<>();

  private JoinType joinType;
  private Function<E, W> getSource;
  private BiConsumer<W, Collection<E>> setSourceTargets;

  private final List<Predicate> on = new ArrayList<>();
  private final List<Predicate> where = new ArrayList<>();

  private final Map<Attribute<? super E, ?>, Entity<E, ?>> children = new HashMap<>();
  private final List<OrderBy> orderByList = new ArrayList<>();

  @Getter(AccessLevel.PUBLIC)
  private boolean isSubqueryBoundary = false;

  @Getter(AccessLevel.PUBLIC)
  private final Set<Object> allIds = new HashSet<>();

  @Getter(AccessLevel.PUBLIC)
  private final List<Object> sortedSelectedIds = new ArrayList<>();

  @Getter(AccessLevel.PUBLIC)
  private final List<E> results = new ArrayList<>();

  Entity(EntityManager entityManager) {
    this.entityManager = checkNotNull(entityManager);
  }

  public <Y, Z> Supplier<Entity<Y, Z>> supplier(Supplier<Entity<Y, Z>> supplier) {
    checkNotNull(supplier);

    return supplier;
  }

  public <Y, Z> Supplier<Entity<Y, Z>> supplier(
      Supplier<Entity<Y, Z>> supplier, Optional<Iterable<Integer>> inIds) {
    checkNotNull(supplier);
    checkNotNull(inIds);

    if (inIds.isPresent()) {
      supplier.get().requireId(inIds);
    }
    return supplier;
  }

  public Entity<W, E> notDeleted() {
    on.add(Predicate.isNull(getDeleted()));
    return this;
  }

  public <T> Entity<E, T> get(SingularAttribute<E, T> attribute) {
    checkNotNull(attribute);

    return addChild(
        attribute,
        new Entity<E, T>(entityManager)
            .setEntityType(EntityType.GET)
            .setParent(this)
            .setAttribute(attribute)
            .setEntityClass(attribute.getBindableJavaType()));
  }

  public <T> Entity<E, T> get(String attributeName, Class<T> getClass) {
    checkNotNull(attributeName);
    checkNotNull(getClass);

    @SuppressWarnings("unchecked")
    var attribute =
        (Attribute<? super E, T>)
            entityManager.getMetamodel().managedType(entityClass).getAttribute(attributeName);
    checkArgument(getClass.isAssignableFrom(attribute.getJavaType()));

    return addChild(
        attribute,
        new Entity<E, T>(entityManager)
            .setEntityType(EntityType.GET)
            .setParent(this)
            .setAttribute(attribute)
            .setEntityClass(getClass));
  }

  public Entity<E, Object> getId() {
    return get("id", Object.class);
  }

  public Entity<E, Instant> getDeleted() {
    return get("deleted", Instant.class);
  }

  public <T> Entity<E, T> join(SingularAttribute<? super E, T> attribute, JoinType joinType) {
    checkNotNull(attribute);
    checkNotNull(joinType);

    return addChild(
        attribute,
        new Entity<E, T>(entityManager)
            .setEntityType(EntityType.JOIN)
            .setParent(this)
            .setJoinType(joinType)
            .setAttribute(attribute)
            .setEntityClass(attribute.getBindableJavaType()));
  }

  @SuppressWarnings("unchecked")
  public <C extends Collection<T>, T> Entity<E, T> join(
      PluralAttribute<E, C, T> attribute,
      JoinType joinType,
      Function<T, E> getSource,
      BiConsumer<E, C> setSourceTargets) {
    checkNotNull(attribute);
    checkNotNull(joinType);
    checkNotNull(getSource);
    checkNotNull(setSourceTargets);

    var child =
        addChild(
            (Attribute<E, T>) attribute,
            new Entity<E, T>(entityManager)
                .setEntityType(EntityType.JOIN)
                .setParent(this)
                .setJoinType(joinType)
                .setAttribute(attribute)
                .setEntityClass(attribute.getBindableJavaType())
                .setGetSource(getSource)
                .setSetSourceTargets((BiConsumer<E, Collection<T>>) setSourceTargets));
    getRoot().getRoots().add((Entity<Object, Object>) child.setSubqueryBoundary(true));
    return child;
  }

  public Entity<W, E> fetch() {
    if (entityType == EntityType.JOIN) {
      setEntityType(EntityType.FETCH);
      if (getParent() != null) {
        getParent().fetch();
      }
    }

    return this;
  }

  public Entity<W, E> on(Predicate predicate) {
    checkNotNull(predicate);

    on.add(predicate);
    return this;
  }

  public Entity<W, E> where(Predicate predicate) {
    checkNotNull(predicate);

    where.add(predicate);
    return this;
  }

  public Entity<W, E> requireId(Optional<Iterable<Integer>> optionalIds) {
    checkNotNull(optionalIds);

    optionalIds.ifPresent(
        ids -> {
          setJoinType(JoinType.INNER);
          where(Predicate.or(Predicate.isNull(this), Predicate.in(getId(), Sets.newHashSet(ids))));
        });
    return this;
  }

  public Entity<W, E> requireNotId(Optional<Iterable<Integer>> optionalIds) {
    checkNotNull(optionalIds);

    optionalIds.ifPresent(
        ids -> {
          setJoinType(JoinType.INNER);
          where(
              Predicate.or(
                  Predicate.isNull(this),
                  Predicate.not(Predicate.in(getId(), Sets.newHashSet(ids)))));
        });
    return this;
  }

  private Entity<?, ?> getRoot() {
    Entity<?, ?> root = this;
    while (root.parent != null) {
      root = root.parent;
    }
    return root;
  }

  public void orderByAsc(Entity<?, ?> entity) {
    checkNotNull(entity);

    getRoot().orderByList.add(new OrderBy(entity, OrderDirection.ASC));
  }

  public void orderByDesc(Entity<?, ?> entity) {
    checkNotNull(entity);

    getRoot().orderByList.add(new OrderBy(entity, OrderDirection.DESC));
  }

  Entity<W, E> setEntityType(EntityType entityType) {
    checkNotNull(entityType);

    this.entityType = entityType;

    if (entityType == EntityType.ROOT) {
      @SuppressWarnings("unchecked")
      var objThis = (Entity<Object, Object>) this;
      getRoot().getRoots().add(objThis);
    }
    if (entityType == EntityType.FETCH) {
      if (parent != null && parent.getEntityType() == EntityType.JOIN) {
        parent.setEntityType(EntityType.FETCH);
      }
    }

    return this;
  }

  Entity<W, E> setJoinType(JoinType joinType) {
    // Null is a valid value.

    this.joinType = joinType;

    if (joinType == JoinType.INNER) {
      if (parent != null && parent.getJoinType() != JoinType.INNER) {
        parent.setJoinType(JoinType.INNER);
      }
    }

    return this;
  }

  private <Y> Entity<E, Y> addChild(Attribute<? super E, Y> attribute, Entity<E, Y> child) {
    checkNotNull(attribute);
    checkNotNull(child);

    @SuppressWarnings("unchecked")
    Entity<E, Y> entity = (Entity<E, Y>) children.computeIfAbsent(attribute, k -> child);

    // The (new) child should be a basic template for the entity. Not many fields should be set.
    checkArgument(child.getJpaEntity() == null);
    checkArgument(child.getParent() == entity.getParent());
    checkArgument(child.getEntityClass() == entity.getEntityClass());
    checkArgument(child.roots.isEmpty());

    checkArgument(child.getGetSource() == entity.getGetSource());
    checkArgument(child.getSetSourceTargets() == entity.getSetSourceTargets());

    checkArgument(child.getOn().isEmpty());
    checkArgument(child.getWhere().isEmpty());
    checkArgument(child.getChildren().isEmpty());
    checkArgument(child.getOrderByList().isEmpty());

    // Merge changes in the child into the existing.
    entity.setEntityType(child.getEntityType()).setJoinType(child.getJoinType());
    entity.setEntityClass(
        entity.getEntityClass() != Object.class ? entity.getEntityClass() : child.getEntityClass());
    entity.setAttribute(child.getAttribute());

    // Verify that the required fields are set on the result.
    checkState(entity.getEntityType() != null);
    checkState(entity.getParent() != null);
    checkState(entity.getAttribute() != null);
    checkState(entity.getEntityClass() != null);
    if (entity.attribute instanceof PluralAttribute<?, ?, ?>) {
      checkState(entity.getGetSource() != null);
      checkState(entity.getSetSourceTargets() != null);
    }

    return entity;
  }

  @SuppressWarnings("unchecked")
  @Override
  public jakarta.persistence.criteria.Expression<E> toExpression(CriteriaBuilder builder) {
    return (jakarta.persistence.criteria.Expression<E>) jpaEntity;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    switch (entityType) {
      case ROOT -> sb.append("Root(").append(entityClass.getSimpleName()).append(")");
      case GET -> sb.append("Get(")
          .append(attribute.getName())
          .append(": ")
          .append(entityClass.getSimpleName())
          .append(")");
      case JOIN, FETCH -> sb.append("Join(")
          .append(attribute.getName())
          .append(": ")
          .append(entityClass.getSimpleName())
          .append(")");
    }

    for (var p : on) {
      sb.append(System.lineSeparator());
      sb.append("On: ").append(p);
    }
    for (var p : where) {
      sb.append(System.lineSeparator());
      sb.append("Where: ").append(p);
    }

    return sb.toString();
  }
}
