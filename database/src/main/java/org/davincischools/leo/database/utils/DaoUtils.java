package org.davincischools.leo.database.utils;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DaoUtils {

  private static final Map<Class<?>, Optional<DaoShallowCopyMethods>> daoShallowCopyMethods =
      Collections.synchronizedMap(new HashMap<>());

  @SafeVarargs
  @SuppressWarnings("unchecked")
  private static <T> T coalesce(Callable<T>... values) throws NullPointerException {
    return Stream.of(values)
        .map(
            value -> {
              try {
                return value.call();
              } catch (Exception e) {
                return null;
              }
            })
        .filter(Objects::nonNull)
        .findFirst()
        .orElseThrow(NullPointerException::new);
  }

  private static Optional<DaoShallowCopyMethods> getDaoShallowCopyMethods(Class<?> daoClass) {
    checkNotNull(daoClass);

    return daoShallowCopyMethods.computeIfAbsent(
        daoClass,
        clazz -> {
          if (clazz.getAnnotation(Entity.class) == null) {
            return Optional.empty();
          }

          try {
            Constructor<?> constructor =
                Iterables.getOnlyElement(Arrays.asList(clazz.getConstructors()));
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
                        var innerDaoShallowCopyMethods =
                            DaoUtils.getDaoShallowCopyMethods(get.getReturnType())
                                .orElseThrow(
                                    () ->
                                        new IllegalArgumentException(
                                            "No inner dao shallow copy methods for: " + get));
                        Object innerDao = get.invoke(from);
                        if (innerDao != null) {
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
                    (Object dao) -> {
                      try {
                        return get.invoke(dao);
                      } catch (Exception e) {
                        throw new RuntimeException(e);
                      }
                    };
                setId =
                    (Object dao, Object id) -> {
                      try {
                        set.invoke(dao, id);
                      } catch (Exception e) {
                        throw new RuntimeException(e);
                      }
                    };
              }
            }

            // There should be an ID field.
            checkState(getId != null && setId != null, "No ID field found in %s.", clazz);

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
        DaoUtils.getDaoShallowCopyMethods(dao.getClass())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "No shallow copy methods found: " + dao.getClass()));

    Object id = daoMethods.getId.apply(dao);
    if (id != null) {
      daoMethods.setId.accept(dao, id);
    }

    Object newDao = daoMethods.newInstance().get();
    for (var copyField : daoMethods.copyFields()) {
      copyField.accept(dao, newDao);
    }

    return (T) newDao;
  }

  private record DaoShallowCopyMethods(
      Supplier<Object> newInstance,
      Function<Object, Object> getId,
      BiConsumer<Object, Object> setId,
      ImmutableList<BiConsumer<Object, Object>> copyFields) {}
}
