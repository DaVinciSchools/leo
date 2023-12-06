package org.davincischools.leo.server.utils.task_queue.workers.project_generators;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.davincischools.leo.database.daos.ProjectDefinitionCategory;
import org.davincischools.leo.database.daos.ProjectInputValue;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
public class ProjectDefinitionCategoryInputValues {
  private ProjectDefinitionCategory definitionCategory;
  private List<ProjectInputValue> inputValues;
}
