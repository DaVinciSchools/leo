package org.davincischools.leo.database.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.annotation.Nullable;
import org.hibernate.FetchNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  /**
   * Utility methods to use when building a query. These MUST be used rather than the methods
   * available on the query elements.
   */
  public interface QueryHelperUtils {
    public void where(Predicate... predicates);

    public void orderBy(Order... orders);

    public <X, Y> Join<X, Y> join(
        From<?, X> parent, SingularAttribute<? super X, Y> attribute, JoinType joinType);

    public <X, Y> From<?, Y> join(
        From<?, X> parent, SetAttribute<? super X, Y> attribute, JoinType joinType);

    public <X, Y> Join<?, Y> fetch(
        From<?, X> parent, SingularAttribute<? super X, Y> attribute, JoinType joinType);

    public <X, Y> From<?, Y> fetch(
        From<?, X> parent,
        SetAttribute<? super X, Y> attribute,
        JoinType joinType,
        Function<Y, X> getSource,
        BiConsumer<X, Set<Y>> setSourceTargets);

    public <X, Y> Join<X, Y> toJoin(Fetch<X, Y> fetch);

    public <X, Y> Join<X, Y> notDeleted(Fetch<X, Y> fetch);

    public <X, Y> Join<X, Y> notDeleted(Join<X, Y> join);

    public <X, Y> Join<X, Y> addJoinOn(Join<X, Y> join, Predicate predicate);

    public <E, P extends Path<E>> P notDeleted(P path);
  }

  public interface QueryBuilder<T> {
    void configureQuery(QueryHelperUtils u, From<?, T> root, CriteriaBuilder builder);
  }

  private final EntityManager entityManager;

  public QueryHelper(@Autowired EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Transactional(readOnly = true)
  public <T> Page<T> query(
      Class<T> rootClass, QueryBuilder<T> queryBuilder, @Nullable Pageable pageable) {
    var queryHelper = new QueryHelperImpl<T>(rootClass, queryBuilder);
    queryHelper.getIds(pageable);
    return PageableExecutionUtils.getPage(
        queryHelper.getEntities(),
        pageable != null ? pageable : Pageable.unpaged(),
        queryHelper.rootIdsSet::size);
  }

  public <T> List<T> query(Class<T> rootClass, QueryBuilder<T> queryBuilder) {
    var queryHelper = new QueryHelperImpl<T>(rootClass, queryBuilder);
    return queryHelper.getEntities();
  }

  class QueryHelperImpl<T> implements QueryHelperUtils {

    private record IdsHolder(Path<?> path, Set<Object> ids) {}

    private final Class<T> rootClass;
    private final QueryBuilder<T> queryBuilder;

    // Getting ids.
    private final List<IdsHolder> allIdsHolders = new ArrayList<>();
    private final Set<Object> rootIdsSet = new HashSet<>();
    private final List<Object> rootIds = new ArrayList<>();

    // Building the query.
    private boolean gettingIds = false;
    private final List<Predicate> where = new ArrayList<>();
    private final List<Order> orderBy = new ArrayList<>();

    public QueryHelperImpl(Class<T> rootClass, QueryBuilder<T> queryBuilder) {
      this.rootClass = rootClass;
      this.queryBuilder = queryBuilder;
    }

    private void getIds(Pageable pageable) {
      gettingIds = true;
      allIdsHolders.clear();
      rootIdsSet.clear();
      rootIds.clear();
      where.clear();
      orderBy.clear();

      var criteriaBuilder = entityManager.getCriteriaBuilder();
      var query = criteriaBuilder.createTupleQuery();
      var root = query.from(rootClass);
      allIdsHolders.add(new IdsHolder(root, new HashSet<>()));

      int pageStart = pageable == null ? 0 : pageable.getPageNumber() * pageable.getPageSize();
      int pageEnd = pageable == null ? Integer.MAX_VALUE : pageStart + pageable.getPageSize();

      queryBuilder.configureQuery(this, root, criteriaBuilder);

      entityManager
          .createQuery(
              query
                  .select(
                      criteriaBuilder.tuple(
                          allIdsHolders.stream()
                              .map(i -> i.path.get("id"))
                              .toArray(Selection[]::new)))
                  .where(where.toArray(new Predicate[0]))
                  .orderBy(orderBy))
          .getResultList()
          .forEach(
              r -> {
                boolean added = rootIdsSet.add(r.get(0));
                if ((rootIdsSet.size() - 1) < pageStart || (rootIdsSet.size() - 1) >= pageEnd) {
                  return;
                }
                if (added && r.get(0) != null) {
                  rootIds.add(r.get(0));
                }
                for (int i = 0; i < allIdsHolders.size(); i++) {
                  var id = r.get(i);
                  if (id != null) {
                    allIdsHolders.get(i).ids().add(id);
                  }
                }
              });
    }

    private List<T> getEntities() {
      gettingIds = false;
      where.clear();
      orderBy.clear();

      var criteriaBuilder = entityManager.getCriteriaBuilder();
      var query = criteriaBuilder.createQuery(rootClass);
      var root = query.from(rootClass);

      if (rootIds.size() < rootIdsSet.size()) {
        where.add(root.get("id").in(rootIds));
      }
      queryBuilder.configureQuery(this, root, criteriaBuilder);

      try {
        return entityManager
            .createQuery(query.select(root).where(where.toArray(new Predicate[0])).orderBy(orderBy))
            .getResultList();
      } catch (FetchNotFoundException e) {
        return List.of();
      }
    }

    @Override
    public void where(Predicate... predicates) {
      where.addAll(Arrays.asList(predicates));
    }

    public void orderBy(Order... order) {
      orderBy.addAll(Arrays.asList(order));
    }

    public <X, Y> Join<X, Y> join(
        From<?, X> parent, SingularAttribute<? super X, Y> attribute, JoinType joinType) {
      checkNotNull(parent);
      checkNotNull(attribute);

      var join = parent.join(attribute, joinType);
      if (gettingIds) {
        allIdsHolders.add(new IdsHolder(join, new HashSet<>()));
      }
      return join;
    }

    public <X, Y> From<?, Y> join(
        From<?, X> parent, SetAttribute<? super X, Y> attribute, JoinType joinType) {
      checkNotNull(parent);
      checkNotNull(attribute);

      var join = parent.join(attribute, joinType);
      if (gettingIds) {
        allIdsHolders.add(new IdsHolder(join, new HashSet<>()));
      }
      return join;
    }

    public <X, Y> Join<?, Y> fetch(
        From<?, X> parent, SingularAttribute<? super X, Y> attribute, JoinType joinType) {
      checkNotNull(parent);
      checkNotNull(attribute);

      if (gettingIds) {
        return join(parent, attribute, joinType);
      }
      return toJoin(parent.fetch(attribute, joinType));
    }

    public <X, Y> From<?, Y> fetch(
        From<?, X> parent,
        SetAttribute<? super X, Y> attribute,
        JoinType joinType,
        Function<Y, X> getSource,
        BiConsumer<X, Set<Y>> setSourceTargets) {
      checkNotNull(parent);
      checkNotNull(attribute);

      if (gettingIds) {
        return join(parent, attribute, joinType);
      }
      return toJoin(parent.fetch(attribute, joinType));
    }

    @SuppressWarnings("unchecked")
    public <X, Y> Join<X, Y> toJoin(Fetch<X, Y> fetch) {
      checkNotNull(fetch);

      return (Join<X, Y>) fetch;
    }

    public <X, Y> Join<X, Y> notDeleted(Fetch<X, Y> fetch) {
      checkNotNull(fetch);

      return notDeleted(toJoin(fetch));
    }

    public <X, Y> Join<X, Y> notDeleted(Join<X, Y> join) {
      checkNotNull(join);

      return addJoinOn(join, join.get("deleted").isNull());
    }

    public <X, Y> Join<X, Y> addJoinOn(Join<X, Y> join, Predicate predicate) {
      checkNotNull(join);
      checkNotNull(predicate);

      if (join.getOn() != null) {
        join.on(join.getOn(), predicate);
      } else {
        join.on(predicate);
      }

      return join;
    }

    public <E, P extends Path<E>> P notDeleted(P path) {
      checkNotNull(path);

      if (path instanceof Join) {
        notDeleted((Join<?, E>) path);
      } else {
        where.add(path.get("deleted").isNull());
      }

      return path;
    }
  }
}
