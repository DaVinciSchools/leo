package org.davincischools.leo.database.dao_interfaces;

import jakarta.persistence.Column;
import org.davincischools.leo.database.daos.ProjectDefinitionCategory;

public interface DaoWithPosition {

  @Column(name = ProjectDefinitionCategory.COLUMN_POSITION_NAME, nullable = false)
  Float getPosition();
}
