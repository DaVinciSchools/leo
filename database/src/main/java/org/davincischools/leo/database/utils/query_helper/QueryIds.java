package org.davincischools.leo.database.utils.query_helper;

import com.google.common.collect.BiMap;
import jakarta.persistence.Tuple;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
class QueryIds {
  private BiMap<Entity<?, ?, ?>, Integer> entityIndexes;
  private Set<Object> selectedIds;
  private List<Tuple> selectedResults;
  private long total;
  private Duration queryDuration;
}
