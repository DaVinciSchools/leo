package org.davincischools.leo.server.utils.task_queue.workers;

import static org.davincischools.leo.database.utils.DaoUtils.getId;
import static org.davincischools.leo.database.utils.DaoUtils.removeTransientValues;

import com.google.common.collect.Iterables;
import java.io.IOException;
import java.util.List;
import org.davincischools.leo.database.daos.ProjectInput.StateType;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.GetProjectInputsParams;
import org.davincischools.leo.database.utils.repos.GetProjectsParams;
import org.davincischools.leo.protos.task_service.GenerateDerivedProjectsTask;
import org.davincischools.leo.server.utils.OpenAiUtils;
import org.davincischools.leo.server.utils.task_queue.DefaultTaskMetadata;
import org.davincischools.leo.server.utils.task_queue.TaskQueue;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.AiProject;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.ProjectGeneratorIo;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.open_ai.OpenAi3V3ProjectGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenerateDerivedProjectsWorker
    extends TaskQueue<GenerateDerivedProjectsTask, DefaultTaskMetadata> {

  private final Database db;
  private final OpenAiUtils openAiUtils;

  public GenerateDerivedProjectsWorker(@Autowired Database db, @Autowired OpenAiUtils openAiUtils) {
    super(15);
    this.db = db;
    this.openAiUtils = openAiUtils;
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
            projectInput -> {
              if (projectInput.getState() == StateType.PROCESSING
                  && getId(projectInput.getExistingProject()).isPresent()) {
                submitTask(
                    GenerateDerivedProjectsTask.newBuilder()
                        .setProjectInputId(projectInput.getId())
                        .setExistingProjectId(projectInput.getExistingProject().getId())
                        .build());
              }
            });
  }

  @Override
  protected boolean processTask(GenerateDerivedProjectsTask task, DefaultTaskMetadata metadata)
      throws IOException {
    var generatorIo = ProjectGeneratorIo.getProjectGeneratorIo(db, task.getProjectInputId());
    if (generatorIo == null) {
      throw new IllegalArgumentException("Unable to create derived projects.");
    }

    // If the expected data is not there yet, fail and retry later.
    if (generatorIo.getProjectInput().getState() != StateType.PROCESSING
        || getId(generatorIo.getProjectInput().getExistingProject()).isEmpty()) {
      return false;
    }

    // Get the existing project.
    var existingProject =
        Iterables.getOnlyElement(
            db.getProjectRepository()
                .getProjects(
                    new GetProjectsParams()
                        .setProjectIds(List.of(task.getExistingProjectId()))
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

    generatorIo
        .setExistingProject(existingProject)
        .setExistingProjectUseType(generatorIo.getProjectInput().getExistingProjectUseType())
        .setNumberOfProjects(5);

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
  protected void taskFailed(
      GenerateDerivedProjectsTask task, DefaultTaskMetadata metadata, Throwable t) {
    db.getProjectInputRepository().updateState(task.getProjectInputId(), StateType.FAILED);
  }
}
