package org.davincischools.leo.database.utils.query_helper;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ImmutableList;
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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.support.PageableUtils;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class QueryHelper {

  public static final int DEFAULT_PAGE_SIZE = 25;

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
    queryBuilder.configureQuery(root).notDeleted();

    try (var em = entityManager.getEntityManagerFactory().createEntityManager()) {
      var count = Optional.<Long>empty();
      if (pageable.isPaged()) {
        count = Optional.of(getCount(em, root, new QueryHelperConfig().setJoinOnly(true)));
      }

      em.clear();

      var result =
          PageableExecutionUtils.getPage(
              getEntities(em, root, pageable, new QueryHelperConfig().setJoinOnly(false)),
              pageable,
              count::get);

      em.clear();

      return result;
    }
  }

  private long getCount(EntityManager em, Entity<?, ?> root, QueryHelperConfig config) {
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Tuple> query = builder.createTupleQuery();
    List<Predicate> where = new ArrayList<>();

    root.getId();
    populateJpaEntities(root, /* isRoot= */ true, query, config);
    // Only after all entities have a JPA entity can we create preconditions.
    populateJpaPredicates(root, builder, where, config);

    var emQuery =
        em.createQuery(
            query
                .select(builder.tuple(builder.countDistinct(root.getId().getJpaEntity())))
                .where(
                    ImmutableList.<Predicate>builder()
                        .addAll(where)
                        .add(builder.isNotNull(root.getId().getJpaEntity()))
                        .build()
                        .toArray(Predicate[]::new)));

    return emQuery.getResultList().stream().map(t -> t.get(0, Long.class)).findFirst().orElse(0L);
  }

  private <E> ImmutableList<E> getEntities(
      EntityManager em, Entity<?, E> root, Pageable pageable, QueryHelperConfig config) {
    var builder = em.getCriteriaBuilder();
    var query = builder.createQuery(root.getEntityClass());
    var where = new ArrayList<Predicate>();

    populateJpaEntities(root, /* isRoot= */ true, query, config);
    // Only after all entities have a JPA entity can we create preconditions.
    populateJpaPredicates(root, builder, where, config);

    @SuppressWarnings("unchecked")
    var selectJpaEntity = (Selection<? extends E>) root.getJpaEntity();
    var emQuery =
        em.createQuery(
            query
                .select(selectJpaEntity)
                .distinct(true)
                .where(
                    ImmutableList.<Predicate>builder()
                        .addAll(where)
                        .add(builder.isNotNull(root.getJpaEntity()))
                        .build()
                        .toArray(Predicate[]::new))
                .orderBy(
                    root.getOrderByList().stream()
                        .map(o -> o.toOrder(builder))
                        .toArray(Order[]::new)));

    if (pageable.isPaged()) {
      int pageStart = !pageable.isPaged() ? 0 : PageableUtils.getOffsetAsInteger(pageable);
      int pageEnd = !pageable.isPaged() ? Integer.MAX_VALUE : pageStart + pageable.getPageSize();
      emQuery.setFirstResult(pageStart).setMaxResults(pageEnd - pageStart);
    }

    return ImmutableList.copyOf(emQuery.getResultList());
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
        {
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
        .forEach(e -> populateJpaEntities(e, /* isRoot= */ false, query, config));
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
