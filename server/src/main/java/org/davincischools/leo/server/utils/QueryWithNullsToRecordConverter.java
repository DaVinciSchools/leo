package org.davincischools.leo.server.utils;

import com.google.common.collect.Iterables;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.lang.Nullable;

// Hibernate annoyingly fails if a column is null when returning a record from a query.
public class QueryWithNullsToRecordConverter implements ConditionalGenericConverter {
  @Override
  public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
    return sourceType.isMap()
        && targetType.getObjectType().isRecord()
        && sourceType.getObjectType().getSimpleName().contains("TupleBackedMap");
  }

  @Override
  public Set<ConvertiblePair> getConvertibleTypes() {
    return null;
  }

  @Override
  @Nullable
  public Object convert(
      @Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
    try {
      if (source instanceof Map<?, ?> m) {
        return Iterables.getOnlyElement(Arrays.asList(targetType.getObjectType().getConstructors()))
            .newInstance(m.values().toArray());
      }
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
    throw new IllegalArgumentException();
  }
}
