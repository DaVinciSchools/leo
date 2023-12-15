package org.davincischools.leo.server.controllers;

import static org.davincischools.leo.database.utils.DaoUtils.listIfInitialized;
import static org.davincischools.leo.database.utils.DaoUtils.removeTransientValues;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.addProjectInputCategoryOptions;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.enumNameOrNull;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.listOrNull;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toAssignmentDao;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toKnowledgeAndSkillDao;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toKnowledgeAndSkillProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toProjectDefinitionProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toProjectInputCategoryProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toProjectProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.valueOrNull;

import com.google.common.collect.Iterables;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.ExistingProjectUseType;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectDefinitionCategory;
import org.davincischools.leo.database.daos.ProjectDefinitionCategoryType;
import org.davincischools.leo.database.daos.ProjectInput;
import org.davincischools.leo.database.daos.ProjectInputValue;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.DaoUtils;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.GetAssignmentsParams;
import org.davincischools.leo.database.utils.repos.GetProjectDefinitionsParams;
import org.davincischools.leo.database.utils.repos.GetProjectInputsParams;
import org.davincischools.leo.database.utils.repos.GetProjectsParams;
import org.davincischools.leo.database.utils.repos.ProjectInputRepository.State;
import org.davincischools.leo.protos.pl_types.ProjectDefinition;
import org.davincischools.leo.protos.project_management.GenerateProjectsRequest;
import org.davincischools.leo.protos.project_management.GenerateProjectsResponse;
import org.davincischools.leo.protos.project_management.GetKnowledgeAndSkillsRequest;
import org.davincischools.leo.protos.project_management.GetKnowledgeAndSkillsResponse;
import org.davincischools.leo.protos.project_management.GetProjectDefinitionCategoryTypesRequest;
import org.davincischools.leo.protos.project_management.GetProjectDefinitionCategoryTypesResponse;
import org.davincischools.leo.protos.project_management.GetProjectInputsRequest;
import org.davincischools.leo.protos.project_management.GetProjectInputsResponse;
import org.davincischools.leo.protos.project_management.GetProjectsRequest;
import org.davincischools.leo.protos.project_management.GetProjectsResponse;
import org.davincischools.leo.protos.project_management.RegisterAnonymousProjectsRequest;
import org.davincischools.leo.protos.project_management.RegisterAnonymousProjectsResponse;
import org.davincischools.leo.protos.project_management.UpdateProjectRequest;
import org.davincischools.leo.protos.project_management.UpdateProjectResponse;
import org.davincischools.leo.protos.project_management.UpsertKnowledgeAndSkillRequest;
import org.davincischools.leo.protos.project_management.UpsertKnowledgeAndSkillResponse;
import org.davincischools.leo.protos.task_service.GenerateProjectsTask;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorException;
import org.davincischools.leo.server.utils.http_executor.HttpExecutors;
import org.davincischools.leo.server.utils.http_user_x.Anonymous;
import org.davincischools.leo.server.utils.http_user_x.Authenticated;
import org.davincischools.leo.server.utils.http_user_x.HttpUserX;
import org.davincischools.leo.server.utils.task_queue.workers.ProjectGeneratorWorker;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.open_ai.OpenAi3V3ProjectGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProjectManagementService {

  private static final AtomicInteger positionCounter = new AtomicInteger(0);

  @Autowired Database db;
  @Autowired ProjectGeneratorWorker projectGeneratorWorker;
  @Autowired OpenAi3V3ProjectGenerator openAi3V3ProjectGenerator;

  @PostMapping(value = "/api/protos/ProjectManagementService/GetKnowledgeAndSkills")
  @ResponseBody
  public GetKnowledgeAndSkillsResponse getKnowledgeAndSkills(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<GetKnowledgeAndSkillsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(GetKnowledgeAndSkillsResponse.getDefaultInstance());
    }

    var response = GetKnowledgeAndSkillsResponse.newBuilder();

    return httpExecutors
        .start(optionalRequest.orElse(GetKnowledgeAndSkillsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              db
                  .getKnowledgeAndSkillRepository()
                  .findAllByTypes(
                      request.getTypesList().stream().map(Enum::name).toList(),
                      userX.isAdminX() ? null : userX.getUserXIdOrNull())
                  .stream()
                  .filter(e -> e.getDeleted() == null)
                  .forEach(
                      e -> toKnowledgeAndSkillProto(e, response::addKnowledgeAndSkillsBuilder));

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/UpsertKnowledgeAndSkill")
  @ResponseBody
  public UpsertKnowledgeAndSkillResponse upsertKnowledgeAndSkill(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<UpsertKnowledgeAndSkillRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(UpsertKnowledgeAndSkillResponse.getDefaultInstance());
    }

    var response = UpsertKnowledgeAndSkillResponse.newBuilder();

    return httpExecutors
        .start(optionalRequest.orElse(UpsertKnowledgeAndSkillRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              if (!switch (request.getKnowledgeAndSkill().getType()) {
                case EKS -> userX.isAdminX() || userX.isTeacher();
                case CTE, XQ_COMPETENCY -> userX.isAdminX();
                case UNSET_TYPE, UNRECOGNIZED -> throw new IllegalArgumentException(
                    "Unknown knowledge and skill type: "
                        + request.getKnowledgeAndSkill().getType().name());
              }) {
                return userX.returnForbidden(UpsertKnowledgeAndSkillResponse.getDefaultInstance());
              }

              db.getKnowledgeAndSkillRepository()
                  .guardedUpsert(
                      toKnowledgeAndSkillDao(request.getKnowledgeAndSkill())
                          .orElseThrow()
                          .setUserX(userX.get().orElseThrow()),
                      userX.isAdminX() ? null : userX.getUserXIdOrNull())
                  .ifPresent(
                      ks -> {
                        toKnowledgeAndSkillProto(ks, response::getKnowledgeAndSkillBuilder);
                      });

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/GenerateProjects")
  @ResponseBody
  @Transactional
  public GenerateProjectsResponse generateProjects(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<GenerateProjectsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(GenerateProjectsResponse.getDefaultInstance());
    }

    var projectInputRef = new AtomicReference<ProjectInput>();
    return httpExecutors
        .start(optionalRequest.orElse(GenerateProjectsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              // Get the definition.
              org.davincischools.leo.database.daos.ProjectDefinition definition;
              if (request.getDefinition().getId() == 0) {
                definition = createAndSaveProjectDefinition(userX, request.getDefinition());
              } else {
                definition =
                    Iterables.getOnlyElement(
                        db.getProjectDefinitionRepository()
                            .getProjectDefinitions(
                                new GetProjectDefinitionsParams()
                                    .setProjectDefinitionIds(
                                        List.of(request.getDefinition().getId()))));
              }

              // Verify that the categories match.
              if (request.getDefinition().getInputsCount()
                  != listIfInitialized(definition.getProjectDefinitionCategories()).size()) {
                throw new IllegalArgumentException(
                    "Incorrect number of input categories: " + request);
              }

              // Save project input.
              var projectInput =
                  createAndSaveProjectInput(userX, request.getDefinition(), definition);
              projectInputRef.set(projectInput);

              // Submit the task to generate projects.
              projectGeneratorWorker.submitTask(
                  GenerateProjectsTask.newBuilder().setProjectInputId(projectInput.getId()).build(),
                  Duration.ofSeconds(5));

              return GenerateProjectsResponse.newBuilder()
                  .setProjectInputId(projectInput.getId())
                  .build();
            })
        .onError(
            (error, log) -> {
              if (projectInputRef.get() != null) {
                db.getProjectInputRepository()
                    .updateState(projectInputRef.get().getId(), State.FAILED.name());
              }
              return Optional.empty();
            })
        .finish();
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  protected org.davincischools.leo.database.daos.ProjectDefinition createAndSaveProjectDefinition(
      HttpUserX userX, ProjectDefinition definition) {
    var definitionDao =
        new org.davincischools.leo.database.daos.ProjectDefinition()
            .setCreationTime(Instant.now())
            .setName(
                definition.hasName()
                    ? definition.getName()
                    : "Anonymously Created Project Definition")
            .setUserX(userX.getUserXOrNull());

    // Create the definition categories.
    definitionDao.setProjectDefinitionCategories(
        new LinkedHashSet<>(
            definition.getInputsList().stream()
                .map(
                    input ->
                        new ProjectDefinitionCategory()
                            .setCreationTime(Instant.now())
                            .setProjectDefinition(definitionDao)
                            .setPosition((float) positionCounter.incrementAndGet())
                            .setMaxNumValues(input.getCategory().getMaxNumValues())
                            .setProjectDefinitionCategoryType(
                                new ProjectDefinitionCategoryType()
                                    .setId(input.getCategory().getTypeId())))
                .toList()));

    removeTransientValues(definitionDao, db.getProjectDefinitionRepository()::save);
    removeTransientValues(
        definitionDao.getProjectDefinitionCategories(),
        db.getProjectDefinitionCategoryRepository()::saveAll);

    return definitionDao;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  protected ProjectInput createAndSaveProjectInput(
      HttpUserX userX,
      org.davincischools.leo.protos.pl_types.ProjectDefinition definition,
      org.davincischools.leo.database.daos.ProjectDefinition definitionDao) {
    // Convert request to ProjectInput.
    var projectInput =
        new ProjectInput()
            .setCreationTime(Instant.now())
            .setProjectDefinition(definitionDao)
            .setUserX(userX.getUserXOrNull())
            .setAssignment(
                definition.getAssignment().hasId()
                    ? new Assignment().setId(definition.getAssignment().getId())
                    : null)
            .setState(State.PROCESSING.name())
            .setExistingProject(
                definition.hasExistingProject()
                    ? new Project().setId(definition.getExistingProject().getId())
                    : null)
            .setExistingProjectUseType(
                definition.hasExistingProjectUseType()
                    ? ExistingProjectUseType.valueOf(definition.getExistingProjectUseType().name())
                    : null);

    // Convert request inputs to ProjectInputValues.
    projectInput.setProjectInputValues(new LinkedHashSet<>());
    for (int i = 0; i < definition.getInputsCount(); ++i) {
      // Get category at index i.
      var inputs = definition.getInputs(i);
      Supplier<ProjectInputValue> newProjectInputValue =
          () ->
              new ProjectInputValue()
                  .setCreationTime(Instant.now())
                  .setProjectInput(projectInput)
                  .setProjectDefinitionCategory(
                      new ProjectDefinitionCategory().setId(inputs.getCategory().getId()))
                  .setPosition((float) positionCounter.incrementAndGet());

      // Get inputs at index i.
      switch (inputs.getCategory().getValueType()) {
        case FREE_TEXT -> {
          projectInput
              .getProjectInputValues()
              .addAll(
                  inputs.getFreeTextsList().stream()
                      .map(t -> newProjectInputValue.get().setFreeTextValue(t))
                      .toList());
        }
        case MOTIVATION -> {
          projectInput
              .getProjectInputValues()
              .addAll(
                  db.getMotivationRepository().findAllByIds(inputs.getSelectedIdsList()).stream()
                      .map(m -> newProjectInputValue.get().setMotivationValue(m))
                      .toList());
        }
        default -> {
          projectInput
              .getProjectInputValues()
              .addAll(
                  db
                      .getKnowledgeAndSkillRepository()
                      .findAllById(inputs.getSelectedIdsList())
                      .stream()
                      .map(ks -> newProjectInputValue.get().setKnowledgeAndSkillValue(ks))
                      .toList());
        }
      }
    }

    removeTransientValues(projectInput, db.getProjectInputRepository()::save);
    removeTransientValues(
        projectInput.getProjectInputValues(), db.getProjectInputValueRepository()::saveAll);

    return projectInput;
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/GetProjects")
  @ResponseBody
  public GetProjectsResponse getProjects(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<GetProjectsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(GetProjectsResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(GetProjectsRequest.getDefaultInstance()))
        .andThen(
            (initialRequest, log) -> {
              var request = initialRequest.toBuilder();
              if (!userX.isAdminX() && !userX.isTeacher()) {
                request.clearUserXIds();
                request.addUserXIds(userX.get().map(UserX::getId).orElse(0));
              }

              var response = GetProjectsResponse.newBuilder();

              db.getProjectRepository()
                  .getProjects(
                      new GetProjectsParams()
                          .setUserXIds(
                              listOrNull(request, GetProjectsRequest.USER_X_IDS_FIELD_NUMBER))
                          .setProjectIds(
                              listOrNull(request, GetProjectsRequest.PROJECT_IDS_FIELD_NUMBER))
                          .setIncludeInactive(
                              valueOrNull(
                                  request, GetProjectsRequest.INCLUDE_INACTIVE_FIELD_NUMBER))
                          .setIncludeTags(
                              valueOrNull(request, GetProjectsRequest.INCLUDE_TAGS_FIELD_NUMBER))
                          .setIncludeInputs(
                              request.getIncludeInputs()
                                  ? new GetProjectInputsParams()
                                      .setIncludeProcessing(true)
                                      .setIncludeComplete(true)
                                      .setIncludeAssignment(
                                          request.getIncludeAssignment()
                                              ? new GetAssignmentsParams()
                                              : null)
                                  : null)
                          .setIncludeFulfillments(
                              valueOrNull(
                                  request, GetProjectsRequest.INCLUDE_FULFILLMENTS_FIELD_NUMBER))
                          .setIncludeAssignment(
                              request.getIncludeAssignment() ? new GetAssignmentsParams() : null)
                          .setIncludeMilestones(
                              valueOrNull(
                                  request, GetProjectsRequest.INCLUDE_MILESTONES_FIELD_NUMBER)))
                  .forEach(project -> toProjectProto(project, true, response::addProjectsBuilder));
              if (request.getIncludeInputOptions()) {
                addProjectInputCategoryOptions(
                    db,
                    response.getProjectsBuilderList().stream()
                        .map(
                            org.davincischools.leo.protos.pl_types.Project.Builder
                                ::getProjectDefinitionBuilder)
                        .flatMap(d -> d.getInputsBuilderList().stream())
                        .map(
                            org.davincischools.leo.protos.pl_types.ProjectInputValue.Builder
                                ::getCategoryBuilder)
                        .toList());
              }

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/GetProjectInputs")
  @ResponseBody
  public GetProjectInputsResponse getProjectInputs(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<GetProjectInputsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(GetProjectInputsResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(GetProjectInputsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              var response = GetProjectInputsResponse.newBuilder();

              db.getProjectInputRepository()
                  .getProjectInputs(
                      new GetProjectInputsParams()
                          .setUserXIds(
                              listOrNull(request, GetProjectInputsRequest.USER_X_IDS_FIELD_NUMBER))
                          .setProjectInputIds(
                              listOrNull(
                                  request, GetProjectInputsRequest.PROJECT_INPUT_IDS_FIELD_NUMBER))
                          .setIncludeComplete(
                              valueOrNull(
                                  request, GetProjectInputsRequest.INCLUDE_COMPLETE_FIELD_NUMBER))
                          .setIncludeProcessing(
                              valueOrNull(
                                  request, GetProjectInputsRequest.INCLUDE_PROCESSING_FIELD_NUMBER))
                          .setIncludeAssignment(
                              valueOrNull(
                                  request,
                                  GetProjectInputsRequest.INCLUDE_ASSIGNMENT_FIELD_NUMBER)))
                  .forEach(
                      projectInput ->
                          toProjectDefinitionProto(projectInput, response::addProjectsBuilder));

              var filteredInputs =
                  response.getProjectsBuilderList().stream()
                      .filter(def -> def.getState() != ProjectDefinition.State.FAILED)
                      .toList();
              response.clearProjects();
              filteredInputs.forEach(response::addProjects);

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/UpdateProject")
  @ResponseBody
  public UpdateProjectResponse updateProject(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<UpdateProjectRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(UpdateProjectResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(UpdateProjectRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              List<Integer> userXIds = null;
              if (!userX.isAdminX() && !userX.isTeacher()) {
                // Make sure the user is only updating their own projects.
                userXIds = List.of(userX.get().orElseThrow().getId());
              }

              Project project =
                  Iterables.getOnlyElement(
                      db.getProjectRepository()
                          .getProjects(
                              new GetProjectsParams()
                                  .setProjectIds(List.of(request.getProject().getId()))
                                  .setUserXIds(userXIds)
                                  .setIncludeInactive(true)),
                      null);
              if (project == null) {
                return userX.returnNotFound(UpdateProjectResponse.getDefaultInstance());
              }

              var reqProject = request.getProject();
              project
                  .setName(reqProject.getName())
                  .setShortDescr(reqProject.getShortDescr())
                  .setLongDescrHtml(reqProject.getLongDescrHtml())
                  .setFavorite(reqProject.getFavorite())
                  .setThumbsState(enumNameOrNull(reqProject.getThumbsState()))
                  .setThumbsStateReason(reqProject.getThumbsStateReason())
                  .setActive(reqProject.getActive());
              toAssignmentDao(reqProject.getAssignment()).ifPresent(project::setAssignment);

              DaoUtils.removeTransientValues(project, db.getProjectRepository()::save);

              var response = UpdateProjectResponse.newBuilder();
              toProjectProto(project, true, response::getProjectBuilder);
              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/GetProjectDefinitionCategoryTypes")
  @ResponseBody
  public GetProjectDefinitionCategoryTypesResponse getProjectDefinitionCategoryTypes(
      @Anonymous HttpUserX userX,
      @RequestBody Optional<GetProjectDefinitionCategoryTypesRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {

    return httpExecutors
        .start(
            optionalRequest.orElse(GetProjectDefinitionCategoryTypesRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              GetProjectDefinitionCategoryTypesResponse.Builder response =
                  GetProjectDefinitionCategoryTypesResponse.newBuilder();

              db
                  .getProjectDefinitionCategoryTypeRepository()
                  .findAllByCategories(request.getIncludeDemos())
                  .stream()
                  .map(
                      categoryType ->
                          new ProjectDefinitionCategory()
                              .setProjectDefinitionCategoryType(categoryType)
                              .setMaxNumValues(4))
                  .forEach(
                      category ->
                          toProjectInputCategoryProto(
                              category, response::addInputCategoriesBuilder));
              addProjectInputCategoryOptions(db, response.getInputCategoriesBuilderList());

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/RegisterAnonymousProjects")
  @ResponseBody
  public RegisterAnonymousProjectsResponse registerAnonymousProjects(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<RegisterAnonymousProjectsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    return httpExecutors
        .start(optionalRequest.orElse(RegisterAnonymousProjectsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              var response = RegisterAnonymousProjectsResponse.newBuilder();

              ProjectInput input =
                  db.getProjectInputRepository()
                      .findById(request.getProjectInputId())
                      .orElseThrow();
              db.getProjectInputRepository()
                  .updateUserX(
                      request.getProjectInputId(),
                      Objects.requireNonNull(userX.getUserXIdOrNull()));
              db.getProjectDefinitionRepository()
                  .updateUserX(input.getProjectDefinition().getId(), userX.getUserXIdOrNull());

              return response.build();
            })
        .finish();
  }
}
