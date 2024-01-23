package org.davincischools.leo.database.utils.query_helper;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Stopwatch;
import com.google.common.collect.HashBiMap;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.metamodel.PluralAttribute;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.criteria.JpaPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.support.PageableUtils;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class QueryHelper {

  private static final Logger logger = LogManager.getLogger();

  public static final int DEFAULT_PAGE_SIZE = 25;

  private static final IdentityHashMap<Class<?>, Method> getIdMethods = new IdentityHashMap<>();

  private final EntityManager entityManager;

  public QueryHelper(@Autowired EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Transactional(readOnly = true)
  public <SF> List<SF> query(Class<SF> selectFromClass, QueryBuilder<SF, SF> queryBuilder) {
    checkNotNull(selectFromClass);
    checkNotNull(queryBuilder);

    return query(
            selectFromClass,
            selectFromClass,
            entity -> queryBuilder.configureQuery(entity.select()),
            Pageable.unpaged(),
            /* distinct= */ true)
        .getContent();
  }

  @Transactional(readOnly = true)
  public <SF> Page<SF> query(
      Class<SF> selectFromClass, QueryBuilder<SF, SF> queryBuilder, Pageable pageable) {
    checkNotNull(selectFromClass);
    checkNotNull(queryBuilder);

    return query(
        selectFromClass,
        selectFromClass,
        entity -> queryBuilder.configureQuery(entity.select()),
        pageable,
        /* distinct= */ true);
  }

  @Transactional(readOnly = true)
  public <S, F> List<S> query(
      Class<S> selectClass, Class<F> fromClass, QueryBuilder<S, F> queryBuilder) {
    checkNotNull(selectClass);
    checkNotNull(fromClass);
    checkNotNull(queryBuilder);

    return query(selectClass, fromClass, queryBuilder, Pageable.unpaged(), /* distinct= */ true)
        .getContent();
  }

  @Transactional(readOnly = true)
  public <S, F> Page<S> query(
      Class<S> selectClass,
      Class<F> fromClass,
      QueryBuilder<S, F> queryBuilder,
      Pageable pageable,
      boolean distinct) {
    checkNotNull(selectClass);
    checkNotNull(fromClass);
    checkNotNull(queryBuilder);
    checkNotNull(pageable);

    var rootEntity =
        new Entity<Void, S, F>(null, this, entityManager, selectClass, fromClass)
            .setEntityType(EntityType.ROOT);
    queryBuilder.configureQuery(rootEntity);

    try (var em = entityManager.getEntityManagerFactory().createEntityManager()) {
      if (pageable.isPaged()
          && rootEntity.getNonIdManagedEntities().contains(rootEntity.getSelectEntity())
          && rootEntity.getSelectEntity().isFetchingPluralAttributes()) {
        return getEntities(
            em, rootEntity, getQueryIds(em, rootEntity, pageable, distinct), pageable, distinct);
      } else {
        return getEntities(em, rootEntity, Optional.empty(), pageable, distinct);
      }
    }
  }

  // TODO: Optimization: Select only the main element id and trim the joins, when possible.
  static Optional<QueryIds> getQueryIds(
      EntityManager em, Entity<?, ?, ?> rootEntity, Pageable pageable, boolean distinct) {
    checkNotNull(em);
    checkNotNull(rootEntity);
    checkNotNull(pageable);

    checkState(
        rootEntity.getNonIdManagedEntities().contains(rootEntity.getSelectEntity()),
        "Getting the ids only makes sense if you are selecting an entity that has an"
            + " id attribute.");
    checkState(distinct, "non-distinct not supported.");

    var stopwatch = Stopwatch.createStarted();

    var builder = em.getCriteriaBuilder();
    var query = builder.createTupleQuery();
    var where = new ArrayList<Predicate>();
    var managedEntities = rootEntity.getNonIdManagedEntities();
    var entityIndexes = HashBiMap.<Entity<?, ?, ?>, Integer>create(managedEntities.size());

    for (int i = 0; i < managedEntities.size(); ++i) {
      entityIndexes.put(managedEntities.get(i), i);
    }

    // All entities need an id so that they are assigned a JPAEntity.
    managedEntities.forEach(Entity::getId);

    // Get only the ids. No fetching of entire entities wanted here.
    var config = new QueryHelperConfig().setJoinOnly(true);

    populateJpaEntities(rootEntity, /* isRoot= */ true, query, config);
    // Only after all entities have a JPA entity can we create preconditions.
    populateJpaPredicates(rootEntity, builder, where, config);

    // Do not return null results.
    where.add(builder.isNotNull(rootEntity.getSelectEntity().getJpaEntity()));

    var emQuery =
        em.createQuery(
            query
                .select(
                    builder.tuple(
                        managedEntities.stream()
                            .map(Entity::getId)
                            .map(Entity::getJpaEntity)
                            .toArray(Selection<?>[]::new)))
                .where(where.toArray(Predicate[]::new))
                .orderBy(
                    rootEntity.getOrderByList().stream()
                        .map(o -> o.toOrder(builder))
                        .toArray(Order[]::new)));

    int pageStart = !pageable.isPaged() ? 0 : PageableUtils.getOffsetAsInteger(pageable);
    int pageEnd = !pageable.isPaged() ? Integer.MAX_VALUE : pageStart + pageable.getPageSize();

    int selectEntityIndex = checkNotNull(entityIndexes.get(rootEntity.getSelectEntity()));
    var selectIds = new HashSet<>();
    var pageSelectedIds = new HashSet<>();

    var pageResults = new ArrayList<Tuple>();

    var allResults = emQuery.getResultList();
    for (var results : allResults) {
      Object selectedId = results.get(selectEntityIndex);
      if (!pageSelectedIds.contains(selectedId)) {
        if (!selectIds.add(selectedId)) {
          continue;
        }
        if (selectIds.size() <= pageStart) {
          continue;
        }
        if (selectIds.size() > pageEnd) {
          continue;
        }
        pageSelectedIds.add(selectedId);
      }
      pageResults.add(results);
    }

    return Optional.of(
        new QueryIds(
            entityIndexes, pageSelectedIds, pageResults, selectIds.size(), stopwatch.elapsed()));
  }

  private <S, F> long getCount(EntityManager em, Entity<?, S, F> rootEntity, boolean distinct) {
    checkNotNull(em);
    checkNotNull(rootEntity);

    var builder = em.getCriteriaBuilder();
    var query = builder.createQuery(Long.class);
    var where = new ArrayList<Predicate>();

    // Get only the ids. No fetching of entire entities wanted here.
    var config = new QueryHelperConfig().setJoinOnly(true);

    populateJpaEntities(rootEntity, /* isRoot= */ true, query, config);
    // Only after all entities have a JPA entity can we create preconditions.
    populateJpaPredicates(rootEntity, builder, where, config);

    // Do not count null results.
    where.add(builder.isNotNull(rootEntity.getSelectEntity().getJpaEntity()));

    var emQuery =
        em.createQuery(
            query
                .select(
                    distinct
                        ? builder.countDistinct(rootEntity.getSelectEntity().getJpaEntity())
                        : builder.count(rootEntity.getSelectEntity().getJpaEntity()))
                .where(where.toArray(Predicate[]::new)));

    return Optional.ofNullable(emQuery.getSingleResult()).orElse(0L);
  }

  // TODO: Optimization: Partition the query into smaller queries by plural attribute boundaries.
  private <S, F> Page<S> getEntities(
      EntityManager em,
      Entity<?, S, F> rootEntity,
      Optional<QueryIds> queryIds,
      Pageable pageable,
      boolean distinct) {
    var stopwatch = Stopwatch.createStarted();

    try {
      var builder = em.getCriteriaBuilder();
      var query = builder.createQuery(rootEntity.getSelectEntity().getFromClass());
      var where = new ArrayList<Predicate>();

      // The select entity need an id so that it is assigned a JPAEntity.
      if (rootEntity.getSelectEntity().getManagedType() != null) {
        rootEntity.getSelectEntity().getId();
      }

      var config = new QueryHelperConfig();

      populateJpaEntities(rootEntity, /* isRoot= */ true, query, config);
      // Only after all entities have a JPA entity can we create preconditions.
      populateJpaPredicates(rootEntity, builder, where, config);

      // Do not return null results.
      where.add(builder.isNotNull(rootEntity.getSelectEntity().getJpaEntity()));

      // Only select the entities with the ids wanted.
      queryIds.ifPresent(
          ids ->
              where.add(
                  rootEntity.getSelectEntity().getId().getJpaEntity().in(ids.getSelectedIds())));

      @SuppressWarnings("unchecked")
      var selectJpaEntity = (Selection<S>) rootEntity.getSelectEntity().getJpaEntity();
      var emQuery =
          em.createQuery(
              query
                  .select(selectJpaEntity)
                  .distinct(distinct)
                  .where(where.toArray(Predicate[]::new))
                  .orderBy(
                      rootEntity.getOrderByList().stream()
                          .map(o -> o.toOrder(builder))
                          .toArray(Order[]::new)));

      if (pageable.isPaged() && queryIds.isEmpty()) {
        var firstResult = PageableUtils.getOffsetAsInteger(pageable);
        emQuery.setFirstResult(firstResult).setMaxResults(pageable.getPageSize());
        return PageableExecutionUtils.getPage(
            emQuery.getResultList(), pageable, () -> getCount(em, rootEntity, distinct));
      }

      var results = emQuery.getResultList();
      return PageableExecutionUtils.getPage(
          results, pageable, () -> queryIds.map(QueryIds::getTotal).orElse((long) results.size()));
    } finally {
      Duration queryDuration = stopwatch.elapsed();
      logger.atDebug().log(
          "Select of {} from {}: total duration: {}, ids duration: {}, entities duration: {}",
          rootEntity,
          rootEntity.getSelectEntity(),
          queryIds
              .map(QueryIds::getQueryDuration)
              .orElse(Duration.ZERO)
              .plus(queryDuration)
              .toMillis(),
          queryIds.map(QueryIds::getQueryDuration).orElse(Duration.ZERO).toMillis(),
          queryDuration.toMillis());
    }
  }

  private static <P, S, F> void populateJpaEntities(
      Entity<P, S, F> entity, boolean isRoot, CriteriaQuery<?> query, QueryHelperConfig config) {
    checkNotNull(entity);
    checkNotNull(query);

    // Create a JPA entity for the Entity.
    switch (isRoot ? EntityType.ROOT : entity.getEntityType()) {
      case ROOT:
        entity.setJpaEntity(query.from(entity.getFromClass()));
        break;
      case SUBQUERY:
        {
          var subquery = query.subquery(entity.getSelectClass());
          @SuppressWarnings("unchecked")
          var select = (Expression<S>) entity.getSelectEntity().getJpaEntity();
          subquery.select(select);
          entity.setSubqueryJpaEntity(subquery);
          entity.setJpaEntity(subquery.from(entity.getFromClass()));
        }
        break;
      case GET:
        {
          @SuppressWarnings("unchecked")
          var p = (JpaPath<P>) entity.getParent().getJpaEntity();
          if (entity.getAttribute() instanceof SingularAttribute<? super P, ?> a1) {
            checkState(entity.getFromClass() == a1.getBindableJavaType());
            entity.setJpaEntity(p.get(a1));
          } else if (entity.getAttribute() instanceof PluralAttribute<? super P, ?, ?> a2) {
            checkState(entity.getFromClass() == a2.getBindableJavaType());
            @SuppressWarnings("unchecked")
            var typedA2 = (PluralAttribute<P, Collection<F>, F>) a2;
            entity.setJpaEntity(p.get(typedA2));
          } else {
            throw new RuntimeException("Invalid attribute type: " + entity.getAttribute());
          }
        }
        break;
      case JOIN:
      case FETCH:
        {
          boolean isJoin = entity.getEntityType() == EntityType.JOIN || config.isJoinOnly();
          if (entity.getParent().getJpaEntity() instanceof From<?, ?> f) {
            if (entity.getAttribute() instanceof SingularAttribute<?, ?> a) {
              @SuppressWarnings("unchecked")
              var expr =
                  isJoin
                      ? f.join((SingularAttribute<Object, F>) a, entity.getJoinType())
                      : (Join<? super P, F>)
                          f.fetch((SingularAttribute<Object, F>) a, entity.getJoinType());
              entity.setJpaEntity(expr);
            } else if (entity.getAttribute() instanceof SetAttribute<?, ?> a) {
              @SuppressWarnings("unchecked")
              var expr =
                  isJoin
                      ? f.join((SetAttribute<Object, F>) a, entity.getJoinType())
                      : (Join<? super P, F>)
                          f.fetch((SetAttribute<Object, F>) a, entity.getJoinType());
              entity.setJpaEntity(expr);
            } else {
              throw new RuntimeException(
                  "Unsupported attribute type: " + entity.getAttribute().getClass());
            }
          } else {
            throw new RuntimeException(
                "Unsupported join type: " + entity.getParent().getJpaEntity().getClass());
          }
        }
        break;
    }

    // Create JPA entities for the children.
    entity
        .getChildren()
        .values()
        .forEach(e -> populateJpaEntities(e, /* isRoot= */ false, query, config));

    // Create JPA entities for the subqueries.
    entity
        .getSubqueries()
        .forEach(
            e -> {
              populateJpaEntities((Entity<?, ?, ?>) e, /* isRoot= */ true, query, config);
            });
  }

  private static void populateJpaPredicates(
      Entity<?, ?, ?> entity,
      CriteriaBuilder builder,
      List<Predicate> where,
      QueryHelperConfig config) {
    checkNotNull(entity);
    checkNotNull(builder);
    checkNotNull(where);

    entity
        .getOn()
        .forEach(
            p -> {
              if (entity.getEntityType() == EntityType.ROOT
                  || entity.getJpaEntity() instanceof Root<?>) {
                where.add(p.toPredicate(builder));
              } else {
                checkState(entity.getJoinType() != null);
                addJoinOn(entity.getJpaEntity(), p.toPredicate(builder));
              }
            });

    entity
        .getWhere()
        .forEach(
            p -> {
              where.add(p.toPredicate(builder));
            });

    entity
        .getChildren()
        .values()
        .forEach(
            e -> {
              populateJpaPredicates(e, builder, where, config);
            });

    entity
        .getSubqueries()
        .forEach(
            e -> {
              populateJpaPredicates(e, builder, where, config);
            });
  }

  private static void addJoinOn(Object jpaEntity, Predicate predicate) {
    checkNotNull(jpaEntity);
    checkNotNull(predicate);

    if (jpaEntity instanceof Join<?, ?> join) {
      Predicate p = join.getOn();
      if (p == null) {
        join.on(predicate);
      } else {
        join.on(p, predicate);
      }
    } else {
      throw new IllegalArgumentException("Entity is not a join: " + jpaEntity.getClass());
    }
  }
}
