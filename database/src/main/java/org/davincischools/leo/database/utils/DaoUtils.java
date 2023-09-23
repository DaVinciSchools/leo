package org.davincischools.leo.database.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.hibernate.Hibernate;

public class DaoUtils {

  private static final Map<Class<?>, Optional<DaoShallowCopyMethods>> daoShallowCopyMethods =
      Collections.synchronizedMap(new HashMap<>());

  private static Optional<DaoShallowCopyMethods> getDaoShallowCopyMethods(Object dao) {
    checkNotNull(dao);

    return daoShallowCopyMethods.computeIfAbsent(
        Hibernate.getClass(dao),
        clazz -> {
          if (clazz.getAnnotation(Entity.class) == null) {
            return Optional.empty();
          }

          try {
            Constructor<?> constructor = clazz.getConstructor();
            List<BiConsumer<Object, Object>> copyFields = new ArrayList<>();
            Function<Object, Object> getId = null;
            BiConsumer<Object, Object> setId = null;

            Map<String, Method> gets = new HashMap<>();
            Map<String, Method> sets = new HashMap<>();
            for (var method : clazz.getMethods()) {
              if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
                sets.put(method.getName().substring(3), method);
              }
              if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                gets.put(method.getName().substring(3), method);
              }
            }

            for (var entry : gets.entrySet()) {
              var get = entry.getValue();
              var set = sets.get(entry.getKey());
              if (set == null) {
                continue;
              }

              if (get.getAnnotation(JoinColumn.class) != null) {
                BiConsumer<Object, Object> copyField =
                    (Object from, Object to) -> {
                      try {
                        // You're not allowed to create other mappings while computing one.
                        Object innerDao = get.invoke(from);
                        // It's okay if this is not initialized since we just want the id.
                        if (innerDao != null) {
                          var innerDaoShallowCopyMethods =
                              DaoUtils.getDaoShallowCopyMethods(innerDao)
                                  .orElseThrow(
                                      () ->
                                          new IllegalArgumentException(
                                              "No inner dao shallow copy methods for: " + get));

                          Object id = innerDaoShallowCopyMethods.getId.apply(innerDao);
                          if (id != null) {
                            Object newInnerDao = innerDaoShallowCopyMethods.newInstance().get();
                            innerDaoShallowCopyMethods.setId.accept(newInnerDao, id);
                            set.invoke(to, newInnerDao);
                          }
                        }
                      } catch (Exception e) {
                        throw new RuntimeException(e);
                      }
                    };
                copyFields.add(copyField);
              } else if (get.getAnnotation(Column.class) != null) {
                BiConsumer<Object, Object> copyField =
                    (Object from, Object to) -> {
                      try {
                        set.invoke(to, get.invoke(from));
                      } catch (Exception e) {
                        throw new RuntimeException(e);
                      }
                    };
                copyFields.add(copyField);
              }
              if (get.getAnnotation(Id.class) != null
                  || get.getAnnotation(EmbeddedId.class) != null) {
                getId =
                    (Object dao2) -> {
                      try {
                        return get.invoke(dao2);
                      } catch (Exception e) {
                        throw new RuntimeException(e);
                      }
                    };
                setId =
                    (Object dao2, Object id) -> {
                      try {
                        set.invoke(dao2, id);
                      } catch (Exception e) {
                        throw new RuntimeException(e);
                      }
                    };
              }
            }

            // There should be an ID field.
            checkState(getId != null, "No ID field found in %s.", clazz);

            return Optional.of(
                new DaoShallowCopyMethods(
                    () -> {
                      try {
                        return constructor.newInstance();
                      } catch (Exception e) {
                        throw new RuntimeException(e);
                      }
                    },
                    getId,
                    setId,
                    ImmutableList.copyOf(copyFields)));
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        });
  }

  @SuppressWarnings("unchecked")
  public static <T> T removeTransientValues(T dao) {
    if (dao == null) {
      return null;
    }

    DaoShallowCopyMethods daoMethods =
        DaoUtils.getDaoShallowCopyMethods(dao)
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "No shallow copy methods found: " + dao.getClass()));

    Object newDao = daoMethods.newInstance().get();
    for (var copyField : daoMethods.copyFields()) {
      copyField.accept(dao, newDao);
    }

    return (T) newDao;
  }

  public static <T> T removeTransientValues(T dao, Consumer<T> processor) {
    T nonTransientDao = removeTransientValues(dao);
    processor.accept(nonTransientDao);
    copyId(nonTransientDao, dao);
    return dao;
  }

  public static void copyId(Object from, Object to) {
    checkNotNull(from);
    checkNotNull(to);
    checkArgument(Hibernate.getClass(from) == Hibernate.getClass(to));

    DaoShallowCopyMethods daoMethods =
        DaoUtils.getDaoShallowCopyMethods(to)
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "No shallow copy methods found: " + to.getClass()));

    daoMethods.setId().accept(to, daoMethods.getId().apply(from));
  }

  public static <T, ID> void updateCollection(
      Iterable<T> oldValues,
      Iterable<T> newValues,
      Function<T, ID> toId,
      Consumer<Iterable<T>> saveAll,
      Consumer<Iterable<T>> deleteAll) {
    Set<ID> oldIds =
        Streams.stream(oldValues).map(toId).filter(Objects::nonNull).collect(Collectors.toSet());
    Set<ID> newIds = Streams.stream(newValues).map(toId).collect(Collectors.toSet());
    deleteAll.accept(
        Streams.stream(oldValues).filter(e -> !newIds.contains(toId.apply(e))).toList());
    saveAll.accept(Streams.stream(newValues).filter(e -> !oldIds.contains(toId.apply(e))).toList());
  }

  @SuppressWarnings("unchecked")
  public static <X, Y> Join<X, Y> toJoin(Fetch<X, Y> fetch) {
    checkNotNull(fetch);

    return ((Join<X, Y>) fetch);
  }

  public static <T> Root<T> notDeleted(List<Predicate> where, Root<T> entity) {
    checkNotNull(entity);

    where.add(entity.get("deleted").isNull());
    return entity;
  }

  public static <X, Y> Join<X, Y> notDeleted(Fetch<X, Y> fetch) {
    checkNotNull(fetch);

    var join = toJoin(fetch);
    join.on(join.get("deleted").isNull());
    return join;
  }

  public static <X, Y> Join<X, Y> addOn(Join<X, Y> join, Predicate predicate) {
    checkNotNull(join);

    if (join.getOn() != null) {
      join.on(join.getOn(), predicate);
    } else {
      join.on(predicate);
    }

    return join;
  }

  private record DaoShallowCopyMethods(
      Supplier<Object> newInstance,
      Function<Object, Object> getId,
      BiConsumer<Object, Object> setId,
      ImmutableList<BiConsumer<Object, Object>> copyFields) {}
}
