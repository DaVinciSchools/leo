package org.davincischools.leo.database.utils.query_helper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@Setter(AccessLevel.PACKAGE)
class QueryHelperConfig {
  private boolean joinOnly = false;
}
