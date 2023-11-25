package org.davincischools.leo.database.utils.query_helper;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.support.PageableUtils;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class helps with queries that would pull in a LOT of extra data due to the cartesian product
 * and N+1 issues with JPA + Hibernate. It breaks queries up into subqueries and performs them
 * separately so that only one of each element is queried.
 *
 * <p>It assumes that ALL tables have an 'id' and 'deleted' (a nullable timestamp) column.
 */
@Component
public class QueryHelper {

  public static final int DEFAULT_PAGE_SIZE = 25;

  private static final Logger logger = LogManager.getLogger();
  private static final IdentityHashMap<Class<?>, Method> getIdMethods = new IdentityHashMap<>();

  private final EntityManager entityManager;

  public QueryHelper(@Autowired EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Transactional(readOnly = true)
  public <E> List<E> query(Class<E> rootClass, QueryBuilder<E> queryBuilder) {
    checkNotNull(rootClass);
    checkNotNull(queryBuilder);

    return query(rootClass, queryBuilder, Pageable.unpaged()).getContent();
  }

  @Transactional(readOnly = true)
  public <E> Page<E> query(Class<E> rootClass, QueryBuilder<E> queryBuilder, Pageable pageable) {
    checkNotNull(rootClass);
    checkNotNull(queryBuilder);
    checkNotNull(pageable);

    var root =
        new Entity<Void, E>(entityManager).setEntityType(EntityType.ROOT).setEntityClass(rootClass);
    queryBuilder.configureQuery(root);

    try (var em = entityManager.getEntityManagerFactory().createEntityManager()) {
      // TODO: Profile and maybe incorporate optimizations.

      boolean getIds = pageable.isPaged();
      if (getIds) {
        getIds(
            em,
            root,
            pageable,
            new QueryHelperConfig().setJoinOnly(true).setStopAtSubqueryBoundaries(false));
      }

      em.clear();

      int size = root.getAllIds().size();

      var result =
          PageableExecutionUtils.getPage(
              getEntities(
                  em,
                  root,
                  getIds,
                  pageable,
                  new QueryHelperConfig().setJoinOnly(false).setStopAtSubqueryBoundaries(false)),
              pageable,
              () -> size);

      em.clear();

      return result;
    }
  }

  private <E> void getIds(
      EntityManager em, Entity<?, E> root, Pageable pageable, QueryHelperConfig config) {
    int pageStart = !pageable.isPaged() ? 0 : PageableUtils.getOffsetAsInteger(pageable);
    int pageEnd = !pageable.isPaged() ? Integer.MAX_VALUE : pageStart + pageable.getPageSize();

    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Tuple> query = builder.createTupleQuery();
    List<Predicate> where = new ArrayList<>();

    var roots = root.getRoots().toArray(Entity[]::new);
    var allIds = Arrays.stream(roots).map(Entity::getAllIds).toArray(Set[]::new);
    var selectedIds = Arrays.stream(roots).map(Entity::getSortedSelectedIds).toArray(List[]::new);

    Arrays.asList(roots).forEach(Entity::getId);
    populateJpaEntities(root, /* isRoot= */ true, query, config);
    // Only after all entities have a JPA entity can we create preconditions.
    populateJpaPredicates(root, builder, where, config);

    // Assemble query.
    var emQuery =
        em.createQuery(
            query
                .select(
                    builder.tuple(
                        Arrays.stream(roots)
                            .map(r -> (Selection<?>) r.getId().getJpaEntity())
                            .toArray(Selection[]::new)))
                .where(where.toArray(Predicate[]::new))
                .orderBy(
                    root.getOrderByList().stream()
                        .map(o -> o.toOrder(builder))
                        .toArray(Order[]::new)));

    // Collect information about selected ids.
    emQuery
        .getResultList()
        .forEach(
            t -> {
              var inPage = false;
              for (int i = 0; i < roots.length; ++i) {
                var id = t.get(i);
                @SuppressWarnings("unchecked")
                var added = allIds[i].add(id);
                if (i == 0) {
                  inPage =
                      !((allIds[i].size() - 1) < pageStart || (allIds[i].size() - 1) >= pageEnd);
                }
                if (added && inPage) {
                  selectedIds[i].add(id);
                }
              }
            });
  }

  private <E> List<E> getEntities(
      EntityManager em,
      Entity<?, E> root,
      boolean gotIds,
      Pageable pageable,
      QueryHelperConfig config) {
    CriteriaBuilder builder = em.getCriteriaBuilder();
    for (var entity : root.getRoots()) {
      var query = builder.createQuery(entity.getEntityClass());
      var where = new ArrayList<Predicate>();

      populateJpaEntities(entity, /* isRoot= */ true, query, config);
      // Only after all entities have a JPA entity can we create predicates.
      populateJpaPredicates(entity, builder, where, config);
      if (gotIds) {
        where.add(entity.getId().getJpaEntity().in(entity.getSortedSelectedIds()));
      }

      // Assemble query.
      query.select(entity.getJpaEntity()).distinct(true).where(where.toArray(Predicate[]::new));
      if (!gotIds) {
        query.orderBy(
            root.getOrderByList().stream().map(o -> o.toOrder(builder)).toArray(Order[]::new));
      }
      logger.atWarn().log("Query: {}", entity);
      var emQuery = em.createQuery(query);
      if (!gotIds && pageable.isPaged()) {
        emQuery
            .setFirstResult(PageableUtils.getOffsetAsInteger(pageable))
            .setMaxResults(pageable.getPageSize());
      }
      entity.getResults().addAll(emQuery.getResultList());

      if (!config.isStopAtSubqueryBoundaries()) {
        break;
      }
    }

    if (!gotIds) {
      return root.getResults();
    } else {
      for (int i = 1; i < root.getRoots().size(); ++i) {
        var child = root.getRoots().get(i);
        Multimap<Object, Object> childrenBySource =
            Multimaps.index(child.getResults(), k -> child.getGetSource().apply(k));
        for (var entry : childrenBySource.asMap().entrySet()) {
          if (child.getAttribute() instanceof SetAttribute<?, ?>) {
            child.getSetSourceTargets().accept(entry.getKey(), Sets.newHashSet(entry.getValue()));
          } else {
            throw new RuntimeException(
                "Unsupported collection type: " + child.getAttribute().getClass());
          }
        }
      }

      Method getIdMethod =
          getIdMethods.computeIfAbsent(
              root.getEntityClass(),
              k -> {
                try {
                  return root.getEntityClass().getMethod("getId");
                } catch (NoSuchMethodException e) {
                  throw new RuntimeException(e);
                }
              });

      var indexedResults =
          Multimaps.index(
              root.getResults(),
              e -> {
                try {
                  return getIdMethod.invoke(e);
                } catch (IllegalAccessException | InvocationTargetException ex) {
                  throw new RuntimeException(ex);
                }
              });

      return root.getSortedSelectedIds().stream()
          .map(i -> Iterables.getOnlyElement(indexedResults.get(i)))
          .toList();
    }
  }

  private static <W, E> void populateJpaEntities(
      Entity<W, E> entity, boolean isRoot, CriteriaQuery<?> query, QueryHelperConfig config) {
    checkNotNull(entity);
    checkNotNull(query);

    // Create a JPA entity for the Entity.
    switch (isRoot ? EntityType.ROOT : entity.getEntityType()) {
      case ROOT:
        entity.setJpaEntity(query.from(entity.getEntityClass()));
        break;
      case GET:
        if (entity.getAttribute() instanceof SingularAttribute<? super W, ?> a
            && entity.getParent().getJpaEntity() instanceof From<?, ?> f) {
          @SuppressWarnings("unchecked")
          var expr = f.get((SingularAttribute<Object, E>) a);
          entity.setJpaEntity(expr);
        } else if (entity.getAttribute() instanceof SetAttribute<?, ?> a
            && entity.getParent().getJpaEntity() instanceof From<?, ?> f) {
          @SuppressWarnings("unchecked")
          var expr = ((From<Object, Object>) f).get((SetAttribute<Object, E>) a);
          entity.setJpaEntity(expr);
        } else {
          throw new RuntimeException("Invalid attribute type.");
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
                      ? f.join((SingularAttribute<Object, E>) a, entity.getJoinType())
                      : (Join<W, E>)
                          f.fetch((SingularAttribute<Object, E>) a, entity.getJoinType());
              entity.setJpaEntity(expr);
            } else if (entity.getAttribute() instanceof SetAttribute<?, ?> a) {
              @SuppressWarnings("unchecked")
              var expr =
                  isJoin
                      ? f.join((SetAttribute<Object, E>) a, entity.getJoinType())
                      : (Join<W, E>) f.fetch((SetAttribute<Object, E>) a, entity.getJoinType());
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
        .forEach(
            e -> {
              if (config.isStopAtSubqueryBoundaries() && e.isSubqueryBoundary()) {
                return;
              }
              populateJpaEntities((Entity<?, Object>) e, /* isRoot= */ false, query, config);
            });
  }

  private void populateJpaPredicates(
      Entity<?, ?> entity,
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
              if (config.isStopAtSubqueryBoundaries() && !isPredicateInBoundary(entity, p)) {
                return;
              }
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
              if (config.isStopAtSubqueryBoundaries() && !isPredicateInBoundary(entity, p)) {
                return;
              }
              where.add(p.toPredicate(builder));
            });

    // Populate Preconditions.
    entity
        .getChildren()
        .values()
        .forEach(
            e -> {
              if (config.isStopAtSubqueryBoundaries() && !isEntityInBoundary(entity, e)) {
                return;
              }
              populateJpaPredicates(e, builder, where, config);
            });
  }

  private boolean isPredicateInBoundary(
      Entity<?, ?> entity, org.davincischools.leo.database.utils.query_helper.Predicate predicate) {
    checkNotNull(entity);
    checkNotNull(predicate);

    if (predicate.getLeft() instanceof Entity<?, ?> leftEntity
        && !isEntityInBoundary(entity, leftEntity)) {
      return false;
    }
    if (predicate.getRight() instanceof Entity<?, ?> rightEntity
        && !isEntityInBoundary(entity, rightEntity)) {
      return false;
    }
    return true;
  }

  private boolean isEntityInBoundary(Entity<?, ?> entity, Entity<?, ?> child) {
    while (child != null) {
      if (child == entity) {
        return true;
      }
      if (child.isSubqueryBoundary()) {
        return false;
      }
      child = child.getParent();
    }
    return false;
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
