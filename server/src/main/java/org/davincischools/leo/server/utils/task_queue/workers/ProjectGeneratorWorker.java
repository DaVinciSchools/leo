package org.davincischools.leo.server.utils.task_queue.workers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.GetProjectInputsParams;
import org.davincischools.leo.database.utils.repos.ProjectInputRepository.State;
import org.davincischools.leo.protos.task_service.GenerateProjectTask;
import org.davincischools.leo.server.utils.task_queue.DefaultTaskMetadata;
import org.davincischools.leo.server.utils.task_queue.TaskQueue;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.ProjectGeneratorInput;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.open_ai.OpenAi3V3ProjectGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectGeneratorWorker extends TaskQueue<GenerateProjectTask, DefaultTaskMetadata> {

  private final Database db;
  private final OpenAi3V3ProjectGenerator openAiGenerator;

  protected ProjectGeneratorWorker(
      @Autowired Database db, @Autowired OpenAi3V3ProjectGenerator openAiGenerator) {
    super(20);
    this.db = db;
    this.openAiGenerator = openAiGenerator;
  }

  @Override
  protected DefaultTaskMetadata createDefaultMetadata() {
    return new DefaultTaskMetadata().setRetries(2);
  }

  @Override
  protected void scanForTasks() {
    db.getProjectInputRepository()
        .getProjectInputs(new GetProjectInputsParams().setIncludeProcessing(true))
        .forEach(
            projectInput ->
                submitTask(
                    GenerateProjectTask.newBuilder()
                        .setProjectInputId(projectInput.getId())
                        .build()));
  }

  @Override
  protected void processTask(GenerateProjectTask task, DefaultTaskMetadata metadata)
      throws JsonProcessingException {

    var generatorInput =
        ProjectGeneratorInput.getProjectGeneratorInput(db, task.getProjectInputId());
    if (generatorInput == null
        || State.valueOf(generatorInput.getProjectInput().getState()) != State.PROCESSING) {
      return;
    }

    var projects = openAiGenerator.generateProjects(generatorInput, 5);
    db.getProjectRepository().deeplySaveProjects(db, projects);
    db.getProjectInputRepository()
        .updateState(generatorInput.getProjectInput().getId(), State.COMPLETED.name());
  }

  @Override
  protected void taskFailed(GenerateProjectTask task, DefaultTaskMetadata metadata, Throwable t) {
    db.getProjectInputRepository().updateState(task.getProjectInputId(), State.FAILED.name());
  }
}
