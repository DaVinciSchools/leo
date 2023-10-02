package org.davincischools.leo.database.utils;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.davincischools.leo.database.utils.DaoUtils.toJoin;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.FetchParent;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

public class PagedQuery<T> {

  public interface PagedQueryBuilder<T> {
    void configureQuery(
        PagedQuery<T> utils, Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder);
  }

  public static <T> Page<T> getPageResults(
      EntityManager em, Class<T> rootClass, PagedQueryBuilder<T> builder, Pageable pageable) {
    var criteriaBuilder = em.getCriteriaBuilder();

    var query = criteriaBuilder.createQuery(rootClass);
    var root = query.from(rootClass);
    var where = new ArrayList<Predicate>();
    builder.configureQuery(
        new PagedQuery<T>(criteriaBuilder, where, true), root, query, criteriaBuilder);

    return PageableExecutionUtils.getPage(
        em.createQuery(query.select(root).distinct(true).where(where.toArray(new Predicate[0])))
            .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
            .setMaxResults(pageable.getPageSize())
            .getResultList(),
        pageable,
        () -> {
          var countQuery = criteriaBuilder.createQuery(Long.class);
          var countRoot = countQuery.from(rootClass);
          var countWhere = new ArrayList<Predicate>();
          builder.configureQuery(
              new PagedQuery<T>(criteriaBuilder, countWhere, false),
              countRoot,
              countQuery,
              criteriaBuilder);
          return em.createQuery(
                  countQuery
                      .select(criteriaBuilder.count(countRoot))
                      .distinct(true)
                      .where(countWhere.toArray(new Predicate[0])))
              .getSingleResult();
        });
  }

  private final CriteriaBuilder builder;
  private final List<Predicate> where;
  private final boolean useFecth;

  public PagedQuery(CriteriaBuilder builder, List<Predicate> where, boolean useFecth) {
    this.builder = builder;
    this.where = where;
    this.useFecth = useFecth;
  }

  @SuppressWarnings("unchecked")
  public boolean isCount() {
    return !useFecth;
  }

  public void where(Predicate predicate) {
    where.add(predicate);
  }

  public Predicate isTrueOrFalse(Path<Boolean> attribute, Boolean value) {
    if (value) {
      return builder.isTrue(attribute);
    } else {
      return builder.or(builder.isNull(attribute), builder.isFalse(attribute));
    }
  }

  public <X, Y, P extends From<?, X> & FetchParent<?, X>> Join<X, Y> fetch(
      P parent, SingularAttribute<? super X, Y> attribute, JoinType joinType) {
    checkNotNull(parent);
    checkNotNull(attribute);

    if (useFecth) {
      return toJoin(parent.fetch(attribute, joinType));
    } else {
      return parent.join(attribute, joinType);
    }
  }

  @SuppressWarnings("unchecked")
  public <X, Y, P extends From<?, X> & FetchParent<?, X>> Join<X, Y> fetch(
      P parent, SetAttribute<? super X, Y> attribute, JoinType joinType) {
    checkNotNull(parent);
    checkNotNull(attribute);

    if (useFecth) {
      return toJoin(parent.fetch(attribute, joinType));
    } else {
      return parent.join(attribute, joinType);
    }
  }

  public <E, P extends Path<E>> P notDeleted(P path) {
    return DaoUtils.notDeleted(where, path);
  }
}
