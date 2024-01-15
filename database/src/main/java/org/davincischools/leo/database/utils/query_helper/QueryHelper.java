package org.davincischools.leo.database.utils.query_helper;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ImmutableList;
import jakarta.persistence.EntityManager;
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
  public <SF> List<SF> query(Class<SF> selectFromClass, QueryBuilder<SF, SF> queryBuilder) {
    checkNotNull(selectFromClass);
    checkNotNull(queryBuilder);

    return query(
            selectFromClass,
            selectFromClass,
            entity -> queryBuilder.configureQuery(entity.select()),
            Pageable.unpaged())
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
        pageable);
  }

  @Transactional(readOnly = true)
  public <S, F> List<S> query(
      Class<S> selectClass, Class<F> fromClass, QueryBuilder<S, F> queryBuilder) {
    checkNotNull(selectClass);
    checkNotNull(fromClass);
    checkNotNull(queryBuilder);

    return query(selectClass, fromClass, queryBuilder, Pageable.unpaged()).getContent();
  }

  @Transactional(readOnly = true)
  public <S, F> Page<S> query(
      Class<S> selectClass,
      Class<F> fromClass,
      QueryBuilder<S, F> queryBuilder,
      Pageable pageable) {
    checkNotNull(selectClass);
    checkNotNull(fromClass);
    checkNotNull(queryBuilder);
    checkNotNull(pageable);

    var rootEntity =
        new Entity<Void, S, F>(null, this, entityManager, selectClass, fromClass)
            .setEntityType(EntityType.ROOT);
    queryBuilder.configureQuery(rootEntity);

    try (var em = entityManager.getEntityManagerFactory().createEntityManager()) {
      var count = Optional.<Long>empty();
      if (pageable.isPaged()) {
        count =
            Optional.of(
                getCount(em, rootEntity, pageable, new QueryHelperConfig().setJoinOnly(true)));
      }

      em.clear();

      var result =
          PageableExecutionUtils.getPage(
              getEntities(em, rootEntity, pageable, new QueryHelperConfig().setJoinOnly(false)),
              pageable,
              count::get);

      em.clear();

      return result;
    }
  }

  private Long getCount(
      EntityManager em, Entity<?, ?, ?> rootEntity, Pageable pageable, QueryHelperConfig config) {
    var builder = em.getCriteriaBuilder();
    var query = builder.createTupleQuery();
    var where = new ArrayList<Predicate>();

    populateJpaEntities(rootEntity, /* isRoot= */ true, query, config);
    // Only after all entities have a JPA entity can we create preconditions.
    populateJpaPredicates(rootEntity, builder, where, config);

    var emQuery =
        em.createQuery(
            query
                .select(
                    builder.tuple(
                        builder.countDistinct(rootEntity.getSelectEntity().getJpaEntity())))
                .distinct(true)
                .where(
                    ImmutableList.<Predicate>builder()
                        .addAll(where)
                        .add(builder.isNotNull(rootEntity.getSelectEntity().getJpaEntity()))
                        .build()
                        .toArray(Predicate[]::new)));

    return emQuery.getResultList().stream().map(t -> t.get(0, Long.class)).findFirst().orElse(0L);
  }

  private <S, F> List<S> getEntities(
      EntityManager em, Entity<?, S, F> rootEntity, Pageable pageable, QueryHelperConfig config) {
    var builder = em.getCriteriaBuilder();
    var query = builder.createQuery(rootEntity.getSelectEntity().getFromClass());
    var where = new ArrayList<Predicate>();

    populateJpaEntities(rootEntity, /* isRoot= */ true, query, config);
    // Only after all entities have a JPA entity can we create preconditions.
    populateJpaPredicates(rootEntity, builder, where, config);

    @SuppressWarnings("unchecked")
    var selectJpaEntity = (Selection<? extends S>) rootEntity.getSelectEntity().getJpaEntity();
    var emQuery =
        em.createQuery(
            query
                .select(selectJpaEntity)
                .distinct(true)
                .where(
                    ImmutableList.<Predicate>builder()
                        .addAll(where)
                        .add(builder.isNotNull(rootEntity.getSelectEntity().getJpaEntity()))
                        .build()
                        .toArray(Predicate[]::new))
                .orderBy(
                    rootEntity.getOrderByList().stream()
                        .map(o -> o.toOrder(builder))
                        .toArray(Order[]::new)));

    if (pageable.isPaged()) {
      int pageStart = !pageable.isPaged() ? 0 : PageableUtils.getOffsetAsInteger(pageable);
      int pageEnd = !pageable.isPaged() ? Integer.MAX_VALUE : pageStart + pageable.getPageSize();
      emQuery.setFirstResult(pageStart).setMaxResults(pageEnd - pageStart);
    }

    return ImmutableList.copyOf(emQuery.getResultList());
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
      case GET:
        {
          if (entity.getAttribute() instanceof SingularAttribute<? super P, ?> a
              && entity.getParent().getJpaEntity() instanceof From<?, ?> f) {
            @SuppressWarnings("unchecked")
            var expr = f.get((SingularAttribute<Object, F>) a);
            entity.setJpaEntity(expr);
          } else if (entity.getAttribute() instanceof SetAttribute<?, ?> a
              && entity.getParent().getJpaEntity() instanceof From<?, ?> f) {
            @SuppressWarnings("unchecked")
            var expr = ((From<Object, Object>) f).get((SetAttribute<Object, F>) a);
            entity.setJpaEntity(expr);
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
  }

  private void populateJpaPredicates(
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
