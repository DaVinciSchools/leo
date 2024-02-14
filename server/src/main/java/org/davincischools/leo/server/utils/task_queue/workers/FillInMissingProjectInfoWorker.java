package org.davincischools.leo.server.utils.task_queue.workers;

import static org.davincischools.leo.database.utils.DaoUtils.listIfInitialized;

import com.google.common.collect.Iterables;
import java.io.IOException;
import java.util.List;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.GetProjectInputsParams;
import org.davincischools.leo.database.utils.repos.GetProjectsParams;
import org.davincischools.leo.protos.task_service.FillInMissingProjectInfoTask;
import org.davincischools.leo.server.utils.OpenAiUtils;
import org.davincischools.leo.server.utils.task_queue.DefaultTaskMetadata;
import org.davincischools.leo.server.utils.task_queue.TaskQueue;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.AiProject;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.ProjectGeneratorIo;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.open_ai.OpenAi3V3ProjectGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class FillInMissingProjectInfoWorker
    extends TaskQueue<FillInMissingProjectInfoTask, DefaultTaskMetadata> {

  private final Database db;
  private final OpenAiUtils openAiUtils;

  public FillInMissingProjectInfoWorker(
      @Autowired Database db, @Autowired OpenAiUtils openAiUtils) {
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

    // Get the existing project.
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

    // Currently only back-filling projects without project input fulfillments.
    if (!listIfInitialized(existingProject.getProjectInputFulfillments()).isEmpty()) {
      return false;
    }

    // Recreate the state that was used to generate the project.
    var generatorIo =
        ProjectGeneratorIo.getProjectGeneratorIo(db, existingProject.getProjectInput().getId());
    if (generatorIo == null) {
      throw new IllegalArgumentException("Unable to update project.");
    }

    generatorIo.setExistingProject(null).setFillInProject(existingProject).setNumberOfProjects(1);

    Project newProject = existingProject;
    try {
      new OpenAi3V3ProjectGenerator(openAiUtils).generateProjects(generatorIo);

      newProject =
          AiProject.aiProjectToProject(
              generatorIo, Iterables.getOnlyElement(generatorIo.getAiProjects().projects));
    } finally {
      newProject.setAiPrompt(generatorIo.getAiPrompt()).setAiResponse(generatorIo.getAiResponse());

      db.getProjectRepository().deeplySaveProjects(db, List.of(newProject));
    }

    return true;
  }
}
