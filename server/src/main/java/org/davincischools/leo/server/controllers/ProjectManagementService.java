package org.davincischools.leo.server.controllers;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;
import org.apache.commons.text.StringEscapeUtils;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectDefinition;
import org.davincischools.leo.database.daos.ProjectDefinitionCategory;
import org.davincischools.leo.database.daos.ProjectDefinitionCategoryType;
import org.davincischools.leo.database.daos.ProjectInput;
import org.davincischools.leo.database.daos.ProjectInputValue;
import org.davincischools.leo.database.daos.ProjectPost;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.KnowledgeAndSkillRepository.Type;
import org.davincischools.leo.database.utils.repos.ProjectDefinitionCategoryTypeRepository.ValueType;
import org.davincischools.leo.database.utils.repos.ProjectDefinitionRepository.FullProjectDefinition;
import org.davincischools.leo.database.utils.repos.ProjectInputRepository.State;
import org.davincischools.leo.database.utils.repos.ProjectRepository.ProjectWithMilestones;
import org.davincischools.leo.protos.pl_types.Project.ThumbsState;
import org.davincischools.leo.protos.pl_types.ProjectInputCategory;
import org.davincischools.leo.protos.pl_types.ProjectInputCategory.Option;
import org.davincischools.leo.protos.project_management.DeleteProjectPostRequest;
import org.davincischools.leo.protos.project_management.DeleteProjectPostResponse;
import org.davincischools.leo.protos.project_management.GenerateAnonymousProjectsRequest;
import org.davincischools.leo.protos.project_management.GenerateAnonymousProjectsResponse;
import org.davincischools.leo.protos.project_management.GenerateProjectsRequest;
import org.davincischools.leo.protos.project_management.GenerateProjectsResponse;
import org.davincischools.leo.protos.project_management.GetAssignmentProjectDefinitionRequest;
import org.davincischools.leo.protos.project_management.GetAssignmentProjectDefinitionResponse;
import org.davincischools.leo.protos.project_management.GetAssignmentProjectDefinitionsRequest;
import org.davincischools.leo.protos.project_management.GetAssignmentProjectDefinitionsResponse;
import org.davincischools.leo.protos.project_management.GetKnowledgeAndSkillsRequest;
import org.davincischools.leo.protos.project_management.GetKnowledgeAndSkillsResponse;
import org.davincischools.leo.protos.project_management.GetProjectDefinitionCategoryTypesRequest;
import org.davincischools.leo.protos.project_management.GetProjectDefinitionCategoryTypesResponse;
import org.davincischools.leo.protos.project_management.GetProjectDetailsRequest;
import org.davincischools.leo.protos.project_management.GetProjectDetailsResponse;
import org.davincischools.leo.protos.project_management.GetProjectPostsRequest;
import org.davincischools.leo.protos.project_management.GetProjectPostsResponse;
import org.davincischools.leo.protos.project_management.GetProjectsRequest;
import org.davincischools.leo.protos.project_management.GetProjectsResponse;
import org.davincischools.leo.protos.project_management.RegisterAnonymousProjectsRequest;
import org.davincischools.leo.protos.project_management.RegisterAnonymousProjectsResponse;
import org.davincischools.leo.protos.project_management.UpdateProjectRequest;
import org.davincischools.leo.protos.project_management.UpdateProjectResponse;
import org.davincischools.leo.protos.project_management.UpsertKnowledgeAndSkillRequest;
import org.davincischools.leo.protos.project_management.UpsertKnowledgeAndSkillResponse;
import org.davincischools.leo.protos.project_management.UpsertProjectPostRequest;
import org.davincischools.leo.protos.project_management.UpsertProjectPostResponse;
import org.davincischools.leo.server.controllers.project_generators.OpenAi3V2ProjectGenerator;
import org.davincischools.leo.server.utils.OpenAiUtils;
import org.davincischools.leo.server.utils.ProtoDaoConverter;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorException;
import org.davincischools.leo.server.utils.http_executor.HttpExecutors;
import org.davincischools.leo.server.utils.http_user_x.Anonymous;
import org.davincischools.leo.server.utils.http_user_x.Authenticated;
import org.davincischools.leo.server.utils.http_user_x.HttpUserX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProjectManagementService {

  public record GenerateProjectsState(
      GenerateProjectsRequest request,
      FullProjectDefinition definition,
      ProjectInput input,
      List<ImmutableList<ProjectInputValue>> values,
      GenerateProjectsResponse.Builder response) {}

  public record GenerateAnonymousProjectInput(
      ProjectInputCategory input, ProjectDefinitionCategory inputDefinition) {}

  private static final AtomicInteger POSITION_COUNTER = new AtomicInteger(0);

  @Autowired Database db;
  @Autowired OpenAi3V2ProjectGenerator openAi3V2ProjectGenerator;

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
              db.getKnowledgeAndSkillRepository()
                  .findAllByTypes(
                      request.getTypesList().stream().map(Enum::name).toList(),
                      userX.isAdminX() ? null : userX.userXId())
                  .forEach(
                      e ->
                          ProtoDaoConverter.toKnowledgeAndSkillProto(
                              e, response.addKnowledgeAndSkillsBuilder()));

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
                case XQ_COMPETENCY -> userX.isAdminX();
                case UNSET, UNRECOGNIZED -> throw new IllegalArgumentException(
                    "Unknown knowledge and skill type: "
                        + request.getKnowledgeAndSkill().getType().name());
              }) {
                return userX.returnForbidden(UpsertKnowledgeAndSkillResponse.getDefaultInstance());
              }

              db.getKnowledgeAndSkillRepository()
                  .guardedUpsert(
                      ProtoDaoConverter.toKnowledgeAndSkillDao(request.getKnowledgeAndSkill())
                          .setUserX(userX.get().orElseThrow()),
                      userX.isAdminX() ? null : userX.userXId())
                  .ifPresent(
                      ks -> {
                        ProtoDaoConverter.toKnowledgeAndSkillProto(
                            ks, response.getKnowledgeAndSkillBuilder());
                      });

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/GenerateProjects")
  @ResponseBody
  public GenerateProjectsResponse generateProjects(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<GenerateProjectsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(GenerateProjectsResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(GenerateProjectsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              // Generate initial GenerateProjectsState state with definition.
              FullProjectDefinition definition =
                  db.getProjectDefinitionRepository()
                      .findFullProjectDefinitionById(request.getDefinition().getId())
                      .orElseThrow(
                          () ->
                              new IllegalArgumentException(
                                  "Project definition does not exist: "
                                      + request.getDefinition().getId()));

              return new GenerateProjectsState(
                  request,
                  definition,
                  new ProjectInput()
                      .setCreationTime(Instant.now())
                      .setProjectDefinition(definition.definition())
                      .setUserX(userX.get().orElse(null))
                      .setState(State.PROCESSING.name())
                      .setTimeout(Instant.now().plus(Duration.ofMinutes(OpenAiUtils.TIMEOUT_MIN)))
                      .setAssignment(
                          request.hasAssignmentId()
                              ? new Assignment().setId(request.getAssignmentId())
                              : null),
                  new ArrayList<>(),
                  GenerateProjectsResponse.newBuilder());
            })
        .andThen(
            (state, log) -> {
              // Save project values and input.
              if (state.definition.categories().size()
                  != state.request.getDefinition().getInputsList().size()) {
                throw new IllegalArgumentException(
                    "Incorrect number of inputs: "
                        + state.request.getDefinition().getInputsList().size());
              }

              for (int i = 0; i < state.definition.categories().size(); ++i) {
                ProjectDefinitionCategory category = state.definition.categories().get(i);
                var inputProto = state.request.getDefinition().getInputsList().get(i);

                Supplier<ProjectInputValue> newProjectInputValue =
                    () ->
                        new ProjectInputValue()
                            .setCreationTime(Instant.now())
                            .setProjectInput(state.input)
                            .setProjectDefinitionCategory(category)
                            .setPosition((float) POSITION_COUNTER.incrementAndGet());

                Builder<ProjectInputValue> projectInputValues = ImmutableList.builder();
                switch (ValueType.valueOf(
                    category.getProjectDefinitionCategoryType().getValueType())) {
                  case FREE_TEXT -> projectInputValues.addAll(
                      throwIfEmptyCategory(
                          inputProto.getFreeTextsList().stream()
                              .map(s -> newProjectInputValue.get().setFreeTextValue(s))
                              .toList()));
                  case EKS -> projectInputValues.addAll(
                      throwIfEmptyCategory(
                          db
                              .getKnowledgeAndSkillRepository()
                              .findAllByIdsAndType(inputProto.getSelectedIdsList(), Type.EKS.name())
                              .stream()
                              .map(ks -> newProjectInputValue.get().setKnowledgeAndSkillValue(ks))
                              .toList()));
                  case XQ_COMPETENCY -> projectInputValues.addAll(
                      throwIfEmptyCategory(
                          db
                              .getKnowledgeAndSkillRepository()
                              .findAllByIdsAndType(
                                  inputProto.getSelectedIdsList(), Type.XQ_COMPETENCY.name())
                              .stream()
                              .map(ks -> newProjectInputValue.get().setKnowledgeAndSkillValue(ks))
                              .toList()));
                  case MOTIVATION -> projectInputValues.addAll(
                      throwIfEmptyCategory(
                          db
                              .getMotivationRepository()
                              .findAllByIds(inputProto.getSelectedIdsList())
                              .stream()
                              .map(m -> newProjectInputValue.get().setMotivationValue(m))
                              .toList()));
                }
                state.values.add(projectInputValues.build());
              }

              // Save project projectInput.
              db.getProjectInputRepository().save(state.input);
              db.getProjectInputValueRepository()
                  .saveAll(state.values.stream().flatMap(Collection::stream).toList());

              log.addProjectInput(state.input);
              state.response.setProjectInputId(state.input.getId());

              return state;
            })
        .andThen(
            (state, log) -> {
              new Thread(
                      () -> {
                        var failed = new AtomicBoolean(false);
                        ImmutableList.of(openAi3V2ProjectGenerator).parallelStream()
                            .forEach(
                                generator -> {
                                  try {
                                    generator.generateAndSaveProjects(state, httpExecutors, 5);
                                  } catch (Throwable t) {
                                    failed.set(true);
                                  }
                                });
                        db.getProjectInputRepository()
                            .updateState(
                                state.input.getId(),
                                failed.get() ? State.FAILED.name() : State.COMPLETED.name());
                      })
                  .start();

              return state.response.build();
            })
        .onError(
            (error, log) -> {
              if (error.lastInput() instanceof GenerateProjectsState state) {
                db.getProjectInputRepository()
                    .updateState(state.input.getId(), State.FAILED.name());
              }
              return Optional.empty();
            })
        .finish();
  }

  private <T> Iterable<T> throwIfEmptyCategory(Iterable<T> values) {
    if (Iterables.isEmpty(values)) {
      throw new IllegalArgumentException("No projectInput for category");
    }
    return values;
  }

  public static String quoteAndEscape(String s) {
    return "\"" + StringEscapeUtils.escapeJava(s) + "\"";
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/GetProjectDetails")
  @ResponseBody
  public GetProjectDetailsResponse getProjectDetails(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<GetProjectDetailsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(GetProjectDetailsResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(GetProjectDetailsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              checkArgument(request.hasProjectId());

              ProjectWithMilestones project =
                  db.getProjectRepository()
                      .findFullProjectById(request.getProjectId())
                      .orElse(null);
              if (project == null) {
                return userX.returnNotFound(GetProjectDetailsResponse.getDefaultInstance());
              }

              if (!userX.isAdminX()) {
                if (userX.isTeacher()) {
                  // TODO: Verify the requested project's user is in their class.
                } else if (userX.isStudent()) {
                  // Make sure the student is only querying their own projects.
                  if (!Objects.equals(
                      userX.get().orElseThrow().getId(),
                      project.project().getProjectInput().getUserX().getId())) {
                    return userX.returnForbidden(GetProjectDetailsResponse.getDefaultInstance());
                  }
                }
              }

              return GetProjectDetailsResponse.newBuilder()
                  .setProject(ProtoDaoConverter.toProjectProto(project, null))
                  .build();
            })
        .finish();
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
            (request, log) -> {
              int userXId = request.getUserXId();
              if (userX.isAdminX()) {
                // Do nothing.
              } else if (userX.isTeacher()) {
                // TODO: Verify the requested user is in their class.
              } else if (userX.isStudent()) {
                // Make sure the student is only querying about their own projects.
                if (userXId != userX.get().orElseThrow().getId()) {
                  return userX.returnForbidden(GetProjectsResponse.getDefaultInstance());
                }
              }
              var response = GetProjectsResponse.newBuilder();

              response.addAllProjects(
                  db
                      .getProjectRepository()
                      .findProjectsByUserXId(userXId, request.getActiveOnly())
                      .stream()
                      .map(e -> ProtoDaoConverter.toProjectProto(e, null).build())
                      .toList());

              if (request.getIncludeUnsuccessful()) {
                response.addAllUnsuccessfulInputs(
                    db
                        .getProjectInputRepository()
                        .findFullProjectInputByUserXAndUnsuccessful(userXId)
                        .stream()
                        .map(ProtoDaoConverter::toProjectDefinition)
                        .toList());
              }

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/GetAssignmentProjectDefinition")
  @ResponseBody
  public GetAssignmentProjectDefinitionResponse getAssignmentProjectDefinition(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<GetAssignmentProjectDefinitionRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(GetAssignmentProjectDefinitionResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(GetAssignmentProjectDefinitionRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              GetAssignmentProjectDefinitionResponse.Builder response =
                  GetAssignmentProjectDefinitionResponse.newBuilder();

              List<FullProjectDefinition> fullDefinitions =
                  db.getProjectDefinitionRepository()
                      .findFullProjectDefinitionsByAssignmentId(request.getAssignmentId());

              FullProjectDefinition definition =
                  fullDefinitions.stream()
                      .max(
                          Comparator.comparing(
                                  (FullProjectDefinition def) -> def.selected().orElse(Instant.MIN))
                              .reversed()
                              .thenComparing(def -> def.definition().getId()))
                      .orElse(null);

              if (definition == null) {
                return response.build();
              }

              response.getDefinitionBuilder().setId(definition.definition().getId());
              for (ProjectDefinitionCategory category : definition.categories()) {
                extractProjectInputCategory(
                    category,
                    response.getDefinitionBuilder().addInputsBuilder().getCategoryBuilder());
              }

              return response.build();
            })
        .finish();
  }

  // TODO: Merge this with DataAccess.createProjectInputValueProto.
  private void extractProjectInputCategory(
      ProjectDefinitionCategory category, ProjectInputCategory.Builder input) {
    ProjectDefinitionCategoryType type = category.getProjectDefinitionCategoryType();
    if (category.getId() != null) {
      input.setId(category.getId());
    }
    input
        .setTypeId(type.getId())
        .setShortDescr(type.getShortDescr())
        .setInputDescr(type.getInputDescr())
        .setName(type.getName())
        .setHint(type.getHint())
        .setPlaceholder(type.getInputPlaceholder())
        .setValueType(ProjectInputCategory.ValueType.valueOf(type.getValueType()))
        .setMaxNumValues(category.getMaxNumValues());

    switch (input.getValueType()) {
      case EKS -> populateOptions(
          db.getKnowledgeAndSkillRepository().findAll(Type.EKS.name()),
          i -> {
            var option =
                Option.newBuilder()
                    .setId(i.getId())
                    .setName(i.getName())
                    .setShortDescr(i.getShortDescr())
                    .setUserXId(i.getUserX().getId());
            if (i.getCategory() != null) {
              option.setCategory(i.getCategory());
            }
            return option;
          },
          input);
      case XQ_COMPETENCY -> populateOptions(
          db.getKnowledgeAndSkillRepository().findAll(Type.XQ_COMPETENCY.name()),
          i -> {
            var option =
                Option.newBuilder()
                    .setId(i.getId())
                    .setName(i.getName())
                    .setShortDescr(i.getShortDescr())
                    .setUserXId(i.getUserX().getId());
            if (i.getCategory() != null) {
              option.setCategory(i.getCategory());
            }
            return option;
          },
          input);
      case MOTIVATION -> populateOptions(
          db.getMotivationRepository().findAll(),
          i ->
              Option.newBuilder()
                  .setId(i.getId())
                  .setName(i.getName())
                  .setShortDescr(i.getShortDescr()),
          input);
    }
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/GetAssignmentProjectDefinitions")
  @ResponseBody
  public GetAssignmentProjectDefinitionsResponse getAssignmentProjectDefinitions(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<GetAssignmentProjectDefinitionsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(GetAssignmentProjectDefinitionsResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(GetAssignmentProjectDefinitionsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              GetAssignmentProjectDefinitionsResponse.Builder response =
                  GetAssignmentProjectDefinitionsResponse.newBuilder();

              List<FullProjectDefinition> fullDefinitions =
                  db.getProjectDefinitionRepository()
                      .findFullProjectDefinitionsByAssignmentId(request.getAssignmentId());

              FullProjectDefinition selectedFullDefinition =
                  fullDefinitions.stream()
                      .max(
                          Comparator.comparing(
                              (FullProjectDefinition def) -> def.selected().orElse(Instant.MIN)))
                      .orElse(null);

              for (FullProjectDefinition fullDefinition : fullDefinitions) {
                response.addDefinitions(
                    ProtoDaoConverter.toProjectDefinition(fullDefinition).toBuilder()
                        .setSelected(fullDefinition == selectedFullDefinition));
              }

              return response.build();
            })
        .finish();
  }

  private <T> void populateOptions(
      Iterable<T> values,
      Function<T, Option.Builder> toOption,
      ProjectInputCategory.Builder inputCategory) {
    Streams.stream(values)
        .map(toOption)
        .sorted(Comparator.comparing(Option.Builder::getName))
        .forEach(inputCategory::addOptions);
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
              // TODO: This is a quick fix to avoid a lazy loading of the UserX id.
              Optional<ProjectWithMilestones> optionalProject =
                  db.getProjectRepository().findFullProjectById(request.getId());
              if (optionalProject.isEmpty()) {
                return userX.returnNotFound(UpdateProjectResponse.getDefaultInstance());
              }
              Project project = optionalProject.get().project();

              if (userX.isAdminX()) {
                // Do nothing.
              } else if (userX.isTeacher()) {
                // TODO: Verify the project is for a student in their class.
              } else if (userX.isStudent()) {
                // Make sure the student is only updating their own projects.
                if (!Objects.equals(
                    project.getProjectInput().getUserX().getId(),
                    userX.get().orElseThrow().getId())) {
                  return userX.returnForbidden(UpdateProjectResponse.getDefaultInstance());
                }
              }

              org.davincischools.leo.protos.pl_types.Project modifications =
                  request.getModifications();
              if (modifications.hasFavorite()) {
                project.setFavorite(modifications.getFavorite());
              }
              if (modifications.hasThumbsState()) {
                project.setThumbsState(
                    modifications.getThumbsState() == ThumbsState.UNSET
                        ? null
                        : modifications.getThumbsState().name());
              }
              if (modifications.hasThumbsStateReason()) {
                project.setThumbsStateReason(modifications.getThumbsStateReason());
              }
              if (modifications.hasActive()) {
                project.setActive(modifications.getActive());
              }
              db.getProjectRepository().save(project);

              return UpdateProjectResponse.newBuilder()
                  .setProject(ProtoDaoConverter.toProjectProto(project, null))
                  .build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/GetProjectPosts")
  @ResponseBody
  public GetProjectPostsResponse getProjectPosts(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<GetProjectPostsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(GetProjectPostsResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(GetProjectPostsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              var response = GetProjectPostsResponse.newBuilder();
              response.addAllProjectPosts(
                  db.getProjectPostRepository().findAllByProjectId(request.getProjectId()).stream()
                      .map(e -> ProtoDaoConverter.toProjectPostProto(e, null).build())
                      .toList());
              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/UpsertProjectPost")
  @ResponseBody
  public UpsertProjectPostResponse UpsertProjectPost(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<UpsertProjectPostRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(UpsertProjectPostResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(UpsertProjectPostRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              ProjectPost post =
                  db.getProjectPostRepository()
                      .save(
                          new ProjectPost()
                              .setCreationTime(Instant.now())
                              .setUserX(userX.get().orElseThrow())
                              .setProject(new Project().setId(request.getProjectId()))
                              .setPostTime(Instant.now())
                              .setName(request.getProjectPost().getName())
                              .setLongDescrHtml(request.getProjectPost().getLongDescrHtml()));

              return UpsertProjectPostResponse.newBuilder().setProjectPostId(post.getId()).build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/DeleteProjectPost")
  @ResponseBody
  public DeleteProjectPostResponse DeleteProjectPost(
      @Authenticated HttpUserX userX,
      @RequestBody Optional<DeleteProjectPostRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (userX.isNotAuthorized()) {
      return userX.returnForbidden(DeleteProjectPostResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(DeleteProjectPostRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              db.getProjectPostRepository().deleteById(request.getId());

              return DeleteProjectPostResponse.getDefaultInstance();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/GetProjectDefinitionCategoryTypes")
  @ResponseBody
  public GetProjectDefinitionCategoryTypesResponse GetProjectDefinitionCategoryTypes(
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
                      t ->
                          new ProjectDefinitionCategory()
                              .setProjectDefinitionCategoryType(t)
                              .setMaxNumValues(4))
                  .forEach(
                      c -> extractProjectInputCategory(c, response.addInputCategoriesBuilder()));

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/GenerateAnonymousProjects")
  @ResponseBody
  public GenerateAnonymousProjectsResponse generateAnonymousProjects(
      @Anonymous HttpUserX userX,
      @RequestBody Optional<GenerateAnonymousProjectsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    var response = GenerateAnonymousProjectsResponse.newBuilder();

    return httpExecutors
        .start(optionalRequest.orElse(GenerateAnonymousProjectsRequest.getDefaultInstance()))
        // Create the GenerateProjectRequest.
        .andThen(
            (request, log) -> {
              var generateRequest = GenerateProjectsRequest.newBuilder();

              // Create the definition and GenerateProjectRequest.
              ProjectDefinition definition =
                  new ProjectDefinition()
                      .setCreationTime(Instant.now())
                      .setName("Anonymously Created Project Definition");
              if (userX.isAuthenticated()) {
                definition.setUserX(userX.get().orElseThrow());
              }
              db.getProjectDefinitionRepository().save(definition);
              generateRequest.getDefinitionBuilder().setId(definition.getId());

              // Process the inputs.
              request
                  .getInputValuesList()
                  .forEach(
                      input -> {
                        db.getProjectDefinitionCategoryRepository()
                            .upsert(
                                definition,
                                db.getProjectDefinitionCategoryTypeRepository()
                                    .findById(input.getCategory().getTypeId())
                                    .orElseThrow(),
                                e ->
                                    e.setMaxNumValues(input.getCategory().getMaxNumValues())
                                        .setPosition((float) POSITION_COUNTER.incrementAndGet()));
                        generateRequest
                            .getDefinitionBuilder()
                            .addInputsBuilder()
                            .addAllFreeTexts(input.getFreeTextsList())
                            .addAllSelectedIds(input.getSelectedIdsList());
                      });

              return generateRequest;
            })
        .andThen(
            (generateRequest, log) -> {
              GenerateProjectsResponse generateProjectsResponse =
                  generateProjects(userX, Optional.of(generateRequest.build()), httpExecutors);
              return response
                  .setProjectInputId(generateProjectsResponse.getProjectInputId())
                  .build();
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
                  .updateUserX(request.getProjectInputId(), userX.get().get().getId());
              db.getProjectDefinitionRepository()
                  .updateUserX(input.getProjectDefinition().getId(), userX.get().get().getId());

              return response.build();
            })
        .finish();
  }
}
