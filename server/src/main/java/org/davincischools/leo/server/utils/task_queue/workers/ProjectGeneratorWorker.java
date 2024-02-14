package org.davincischools.leo.server.utils.task_queue.workers;

import static org.davincischools.leo.database.utils.DaoUtils.getId;
import static org.davincischools.leo.database.utils.DaoUtils.removeTransientValues;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.davincischools.leo.database.daos.ProjectInput.StateType;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.GetProjectInputsParams;
import org.davincischools.leo.protos.task_service.GenerateProjectsTask;
import org.davincischools.leo.server.utils.OpenAiUtils;
import org.davincischools.leo.server.utils.task_queue.DefaultTaskMetadata;
import org.davincischools.leo.server.utils.task_queue.TaskQueue;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.AiProject;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.ProjectGeneratorIo;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.open_ai.OpenAi3V3ProjectGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectGeneratorWorker extends TaskQueue<GenerateProjectsTask, DefaultTaskMetadata> {

  private final Database db;
  private final OpenAiUtils openAiUtils;

  protected ProjectGeneratorWorker(@Autowired Database db, @Autowired OpenAiUtils openAiUtils) {
    super(20);
    this.db = db;
    this.openAiUtils = openAiUtils;
  }

  @Override
  protected DefaultTaskMetadata createDefaultMetadata() {
    return new DefaultTaskMetadata().setRetries(4);
  }

  @Override
  protected void scanForTasks() {
    db.getProjectInputRepository()
        .getProjectInputs(new GetProjectInputsParams().setIncludeProcessing(true))
        .forEach(
            projectInput -> {
              if (projectInput.getState() == StateType.PROCESSING
                  && getId(projectInput.getExistingProject()).isEmpty()) {
                submitTask(
                    GenerateProjectsTask.newBuilder()
                        .setProjectInputId(projectInput.getId())
                        .build());
              }
            });
  }

  @Override
  protected boolean processTask(GenerateProjectsTask task, DefaultTaskMetadata metadata)
      throws JsonProcessingException {
    var generatorIo = ProjectGeneratorIo.getProjectGeneratorIo(db, task.getProjectInputId());
    if (generatorIo == null) {
      throw new IllegalArgumentException("Unable to create projects.");
    }

    // If the expected data is not there yet, fail and retry later.
    if (generatorIo.getProjectInput().getState() != StateType.PROCESSING
        || getId(generatorIo.getProjectInput().getExistingProject()).isPresent()) {
      return false;
    }

    generatorIo.setExistingProject(null).setFillInProject(null).setNumberOfProjects(5);

    try {
      new OpenAi3V3ProjectGenerator(openAiUtils).generateProjects(generatorIo);

      db.getProjectRepository()
          .deeplySaveProjects(
              db,
              generatorIo.getAiProjects().projects.stream()
                  .map(p -> AiProject.aiProjectToProject(generatorIo, p))
                  .toList());

      generatorIo.getProjectInput().setState(StateType.COMPLETED);
    } finally {
      generatorIo
          .getProjectInput()
          .setAiPrompt(generatorIo.getAiPrompt())
          .setAiResponse(generatorIo.getAiResponse());

      removeTransientValues(generatorIo.getProjectInput(), db.getProjectInputRepository()::save);
    }

    return true;
  }

  @Override
  protected void taskFailed(GenerateProjectsTask task, DefaultTaskMetadata metadata, Throwable t) {
    db.getProjectInputRepository().updateState(task.getProjectInputId(), StateType.FAILED);
  }
}
