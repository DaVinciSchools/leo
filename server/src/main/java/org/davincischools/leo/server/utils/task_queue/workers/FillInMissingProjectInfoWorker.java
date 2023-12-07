package org.davincischools.leo.server.utils.task_queue.workers;

import static org.davincischools.leo.database.utils.DaoUtils.listIfInitialized;

import com.google.common.collect.Iterables;
import java.io.IOException;
import java.util.List;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.GetProjectInputsParams;
import org.davincischools.leo.database.utils.repos.GetProjectsParams;
import org.davincischools.leo.database.utils.repos.ProjectInputRepository.State;
import org.davincischools.leo.protos.task_service.FillInMissingProjectInfoTask;
import org.davincischools.leo.server.utils.task_queue.DefaultTaskMetadata;
import org.davincischools.leo.server.utils.task_queue.TaskQueue;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.ProjectGeneratorInput;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.open_ai.OpenAi3V3ProjectGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class FillInMissingProjectInfoWorker
    extends TaskQueue<FillInMissingProjectInfoTask, DefaultTaskMetadata> {

  private final Database db;
  private final OpenAi3V3ProjectGenerator openAiGenerator;

  public FillInMissingProjectInfoWorker(
      @Autowired Database db, @Autowired OpenAi3V3ProjectGenerator openAiGenerator) {
    super(15);
    this.db = db;
    this.openAiGenerator = openAiGenerator;
  }

  @Override
  protected DefaultTaskMetadata createDefaultMetadata() {
    return new DefaultTaskMetadata().setRetries(2);
  }

  @Override
  protected void scanForTasks() {
    db.getProjectRepository()
        .getProjects(new GetProjectsParams().setIncludeFulfillments(true).setIncludeInactive(true))
        .forEach(
            p -> {
              if (listIfInitialized(p.getProjectInputFulfillments()).isEmpty()) {
                submitTask(
                    FillInMissingProjectInfoTask.newBuilder().setProjectId(p.getId()).build());
              }
            });
  }

  @Override
  protected boolean processTask(FillInMissingProjectInfoTask task, DefaultTaskMetadata metadata)
      throws IOException {

    // Get the project.
    var existingProject =
        Iterables.getOnlyElement(
            db.getProjectRepository()
                .getProjects(
                    new GetProjectsParams()
                        .setProjectIds(List.of(task.getProjectId()))
                        .setIncludeInputs(
                            new GetProjectInputsParams()
                                .setIncludeProcessing(true)
                                .setIncludeComplete(true))
                        .setIncludeMilestones(true)
                        .setIncludeFulfillments(true)
                        .setIncludeInactive(true)),
            null);
    if (existingProject == null) {
      throw new IllegalArgumentException("Existing project not found.");
    }
    if (!listIfInitialized(existingProject.getProjectInputFulfillments()).isEmpty()) {
      return false;
    }

    // Recreate the state that was used to generate the project.
    var generatorInput =
        ProjectGeneratorInput.getProjectGeneratorInput(
            db, existingProject.getProjectInput().getId());
    if (generatorInput == null) {
      throw new IllegalArgumentException("Unable to create project generator input.");
    }
    generatorInput.setFillInProject(existingProject);

    var projects = openAiGenerator.generateProjects(generatorInput, 1);
    db.getProjectRepository().deeplySaveProjects(db, projects);
    db.getProjectInputRepository()
        .updateState(generatorInput.getProjectInput().getId(), State.COMPLETED.name());
    return true;
  }
}
