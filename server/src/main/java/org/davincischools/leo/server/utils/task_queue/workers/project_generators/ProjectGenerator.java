package org.davincischools.leo.server.utils.task_queue.workers.project_generators;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ProjectGenerator {

  void generateProjects(ProjectGeneratorIo generatorIo) throws JsonProcessingException;
}
