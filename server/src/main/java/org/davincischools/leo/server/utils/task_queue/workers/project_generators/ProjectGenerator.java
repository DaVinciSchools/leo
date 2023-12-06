package org.davincischools.leo.server.utils.task_queue.workers.project_generators;

import java.util.List;
import org.davincischools.leo.database.daos.Project;

public interface ProjectGenerator {

  List<Project> generateProjects(ProjectGeneratorInput input, int numberOfProjects)
      throws Throwable;
}
