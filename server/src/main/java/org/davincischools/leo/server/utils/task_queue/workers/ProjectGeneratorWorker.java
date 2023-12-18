package org.davincischools.leo.server.utils.task_queue.workers;

import static org.davincischools.leo.database.utils.DaoUtils.getId;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Objects;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.GetProjectInputsParams;
import org.davincischools.leo.database.utils.repos.ProjectInputRepository.State;
import org.davincischools.leo.protos.task_service.GenerateProjectsTask;
import org.davincischools.leo.server.utils.OpenAiUtils;
import org.davincischools.leo.server.utils.task_queue.DefaultTaskMetadata;
import org.davincischools.leo.server.utils.task_queue.TaskQueue;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.ProjectGeneratorInput;
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
              if (Objects.equals(projectInput.getState(), State.PROCESSING.name())
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

    var generatorInput =
        ProjectGeneratorInput.getProjectGeneratorInput(db, task.getProjectInputId());
    if (generatorInput == null) {
      throw new IllegalArgumentException("Unable to create project generator input.");
    }
    if (!(Objects.equals(generatorInput.getProjectInput().getState(), State.PROCESSING.name())
        && getId(generatorInput.getProjectInput().getExistingProject()).isEmpty())) {
      return false;
    }

    var projects = new OpenAi3V3ProjectGenerator(openAiUtils).generateProjects(generatorInput, 5);
    db.getProjectRepository().deeplySaveProjects(db, projects);
    db.getProjectInputRepository()
        .updateState(generatorInput.getProjectInput().getId(), State.COMPLETED.name());
    return true;
  }

  @Override
  protected void taskFailed(GenerateProjectsTask task, DefaultTaskMetadata metadata, Throwable t) {
    db.getProjectInputRepository().updateState(task.getProjectInputId(), State.FAILED.name());
  }
}
