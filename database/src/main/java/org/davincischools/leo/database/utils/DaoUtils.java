package org.davincischools.leo.database.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.dao_interfaces.DaoWithPosition;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.springframework.data.jpa.repository.JpaRepository;

public class DaoUtils {

  private static final Logger logger = LogManager.getLogger();

  private static final Map<Class<?>, Optional<DaoShallowCopyMethods>> daoShallowCopyMethods =
      Collections.synchronizedMap(new HashMap<>());

  private static Optional<DaoShallowCopyMethods> getDaoShallowCopyMethods(Object dao) {
    checkNotNull(dao);

    return daoShallowCopyMethods.computeIfAbsent(
        getDaoClass(dao),
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
                              getDaoShallowCopyMethods(innerDao)
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
        getDaoShallowCopyMethods(dao)
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

  @SuppressWarnings("unchecked")
  public static <T> T removeTransientValues(T daoOrIterableDaos, Consumer<T> processor) {
    if (daoOrIterableDaos instanceof Iterable) {
      List<Object> daos = Lists.newArrayList((Iterable<Object>) daoOrIterableDaos);
      List<Object> nonTransientDaos = daos.stream().map(DaoUtils::removeTransientValues).toList();
      if (daoOrIterableDaos instanceof Set) {
        processor.accept((T) new HashSet<>(nonTransientDaos));
      } else {
        processor.accept((T) nonTransientDaos);
      }
      for (int i = 0; i < daos.size(); i++) {
        copyId(nonTransientDaos.get(i), daos.get(i));
      }
    } else {
      T nonTransientDao = removeTransientValues(daoOrIterableDaos);
      processor.accept(nonTransientDao);
      copyId(nonTransientDao, daoOrIterableDaos);
    }
    return daoOrIterableDaos;
  }

  public static void copyId(Object from, Object to) {
    checkNotNull(from);
    checkNotNull(to);
    checkArgument(getDaoClass(from) == getDaoClass(to));

    DaoShallowCopyMethods daoMethods =
        getDaoShallowCopyMethods(to)
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

  public static Optional<Object> getId(@Nullable Object entity) {
    if (entity == null) {
      return Optional.empty();
    }
    var methods = getDaoShallowCopyMethods(entity);
    return methods.map(shallowCopyMethods -> shallowCopyMethods.getId().apply(entity));
  }

  public static <T> boolean isInitialized(@Nullable T entity) {
    return entity != null && Hibernate.isInitialized(entity);
  }

  public static <T> Optional<T> ifInitialized(@Nullable T entity) {
    return isInitialized(entity) ? Optional.of(entity) : Optional.empty();
  }

  public static <T> void ifInitialized(@Nullable Iterable<T> entities, Consumer<T> processFn) {
    if (isInitialized(entities)) {
      entities.forEach(processFn);
    }
  }

  public static <T, E> void ifInitialized(
      @Nullable Iterable<T> entities, Comparator<T> comparator, Consumer<T> processFn) {
    if (isInitialized(entities)) {
      Streams.stream(entities).sorted(comparator).forEach(processFn);
    }
  }

  public static <T> ImmutableList<T> listIfInitialized(@Nullable Iterable<T> entity) {
    return isInitialized(entity) ? ImmutableList.copyOf(entity) : ImmutableList.of();
  }

  public static <T> Stream<T> streamIfInitialized(@Nullable Iterable<T> entities) {
    if (isInitialized(entities)) {
      return Streams.stream(entities);
    }
    return Stream.empty();
  }

  public static <T extends DaoWithPosition> ImmutableList<T> sortByPosition(Iterable<T> daos) {
    return Streams.stream(daos)
        .sorted(Comparator.comparing(DaoWithPosition::getPosition))
        .collect(toImmutableList());
  }

  public static <T, R> List<T> getJoinTableDaos(
      @Nullable Set<R> sourceTableRows, Function<R, T> getTargetFn) {
    checkNotNull(getTargetFn);

    return isInitialized(sourceTableRows)
        ? sourceTableRows.stream().map(getTargetFn).filter(Objects::nonNull).toList()
        : Collections.emptyList();
  }

  public static <F, T, R> Set<R> createJoinTableRows(
      F from, @Nullable Iterable<T> targets, BiFunction<F, T, R> createFn) {
    checkNotNull(from);
    checkNotNull(createFn);

    if (targets == null) {
      return Collections.emptySet();
    }
    return Streams.stream(targets)
        .filter(Objects::nonNull)
        .map(target -> createFn.apply(from, target))
        .collect(Collectors.toSet());
  }

  public static <E, R, T> void saveJoinTableAndTargets(
      E entity,
      Function<E, Iterable<R>> toRowsFn,
      Function<R, T> getTargetFn,
      Consumer<T> saveTargetFn,
      BiConsumer<E, Iterable<T>> setTargetsFn) {
    checkNotNull(entity);
    checkNotNull(toRowsFn);
    checkNotNull(getTargetFn);
    checkNotNull(saveTargetFn);
    checkNotNull(setTargetsFn);
    checkArgument(isInitialized(entity));

    ifInitialized(toRowsFn.apply(entity))
        .ifPresent(
            rows -> {
              List<T> targets = Streams.stream(rows).map(getTargetFn).toList();
              targets.forEach(target -> removeTransientValues(target, saveTargetFn));
              setTargetsFn.accept(entity, targets);
            });
  }

  public static <E> void updateAllRecords(JpaRepository<E, ?> repository, Consumer<E> setFn) {
    checkNotNull(repository);
    checkNotNull(setFn);

    List<E> entities = repository.findAll();
    entities.forEach(setFn);
    repository.saveAll(entities);
  }

  public static <E> void deleteAllRecords(
      JpaRepository<E, ?> repository,
      Function<E, Instant> getDeletedFn,
      BiConsumer<E, Instant> setFn) {
    deleteAllRecords(repository, getDeletedFn, setFn, e -> false);
  }

  public static <E> void deleteAllRecords(
      JpaRepository<E, ?> repository,
      Function<E, Instant> getDeletedFn,
      BiConsumer<E, Instant> setFn,
      Function<E, Boolean> shouldDeleteAnywayFn) {
    checkNotNull(repository);
    checkNotNull(setFn);

    List<E> deleted =
        repository.findAll().stream()
            .filter(e -> getDeletedFn.apply(e) == null || shouldDeleteAnywayFn.apply(e))
            .toList();
    deleted.forEach(entity -> setFn.accept(entity, Instant.now()));
    repository.saveAll(deleted);
  }

  public static <D> Class<?> getDaoClass(D dao) {
    LazyInitializer lazyInitializer = HibernateProxy.extractLazyInitializer(dao);
    if (lazyInitializer != null) {
      return lazyInitializer.getPersistentClass();
    } else {
      return dao.getClass();
    }
  }

  private record DaoShallowCopyMethods(
      Supplier<Object> newInstance,
      Function<Object, Object> getId,
      BiConsumer<Object, Object> setId,
      ImmutableList<BiConsumer<Object, Object>> copyFields) {}
}
