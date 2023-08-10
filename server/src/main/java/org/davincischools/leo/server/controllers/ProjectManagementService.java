package org.davincischools.leo.server.controllers;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
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
import org.davincischools.leo.protos.project_management.DeletePostRequest;
import org.davincischools.leo.protos.project_management.DeletePostResponse;
import org.davincischools.leo.protos.project_management.GenerateAnonymousProjectsRequest;
import org.davincischools.leo.protos.project_management.GenerateAnonymousProjectsResponse;
import org.davincischools.leo.protos.project_management.GenerateProjectsRequest;
import org.davincischools.leo.protos.project_management.GenerateProjectsResponse;
import org.davincischools.leo.protos.project_management.GetAssignmentProjectDefinitionRequest;
import org.davincischools.leo.protos.project_management.GetAssignmentProjectDefinitionResponse;
import org.davincischools.leo.protos.project_management.GetAssignmentProjectDefinitionsRequest;
import org.davincischools.leo.protos.project_management.GetAssignmentProjectDefinitionsResponse;
import org.davincischools.leo.protos.project_management.GetEksRequest;
import org.davincischools.leo.protos.project_management.GetEksResponse;
import org.davincischools.leo.protos.project_management.GetProjectDefinitionCategoryTypesRequest;
import org.davincischools.leo.protos.project_management.GetProjectDefinitionCategoryTypesResponse;
import org.davincischools.leo.protos.project_management.GetProjectDetailsRequest;
import org.davincischools.leo.protos.project_management.GetProjectDetailsResponse;
import org.davincischools.leo.protos.project_management.GetProjectPostsRequest;
import org.davincischools.leo.protos.project_management.GetProjectPostsResponse;
import org.davincischools.leo.protos.project_management.GetProjectsRequest;
import org.davincischools.leo.protos.project_management.GetProjectsResponse;
import org.davincischools.leo.protos.project_management.GetXqCompetenciesRequest;
import org.davincischools.leo.protos.project_management.GetXqCompetenciesResponse;
import org.davincischools.leo.protos.project_management.PostMessageRequest;
import org.davincischools.leo.protos.project_management.PostMessageResponse;
import org.davincischools.leo.protos.project_management.RegisterAnonymousProjectsRequest;
import org.davincischools.leo.protos.project_management.RegisterAnonymousProjectsResponse;
import org.davincischools.leo.protos.project_management.UpdateProjectRequest;
import org.davincischools.leo.protos.project_management.UpdateProjectResponse;
import org.davincischools.leo.server.controllers.project_generators.OpenAi3V2ProjectGenerator;
import org.davincischools.leo.server.utils.DataAccess;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorException;
import org.davincischools.leo.server.utils.http_executor.HttpExecutors;
import org.davincischools.leo.server.utils.http_user.Anonymous;
import org.davincischools.leo.server.utils.http_user.Authenticated;
import org.davincischools.leo.server.utils.http_user.HttpUser;
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

  @PostMapping(value = "/api/protos/ProjectManagementService/GetEks")
  @ResponseBody
  public GetEksResponse getEks(
      @Authenticated HttpUser user,
      @RequestBody Optional<GetEksRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(GetEksResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(GetEksRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              var response = GetEksResponse.newBuilder();

              response.addAllEks(
                  Lists.transform(
                      db.getKnowledgeAndSkillRepository().findAll(Type.EKS.name()),
                      DataAccess::getProtoEks));

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/GetXqCompetencies")
  @ResponseBody
  public GetXqCompetenciesResponse getXqCompetencies(
      @Authenticated HttpUser user,
      @RequestBody Optional<GetXqCompetenciesRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(GetXqCompetenciesResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(GetXqCompetenciesRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              var response = GetXqCompetenciesResponse.newBuilder();

              response.addAllXqCompentencies(
                  Lists.transform(
                      db.getKnowledgeAndSkillRepository().findAll(Type.XQ_COMPETENCY.name()),
                      DataAccess::orProtoXqCompetency));

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/GenerateProjects")
  @ResponseBody
  public GenerateProjectsResponse generateProjects(
      @Authenticated HttpUser user,
      @RequestBody Optional<GenerateProjectsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(GenerateProjectsResponse.getDefaultInstance());
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
                      .setUserX(user.get().orElse(null))
                      .setState(State.PROCESSING.name())
                      .setTimeout(Instant.now().plus(Duration.ofMinutes(10)))
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
      @Authenticated HttpUser user,
      @RequestBody Optional<GetProjectDetailsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(GetProjectDetailsResponse.getDefaultInstance());
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
                return user.returnNotFound(GetProjectDetailsResponse.getDefaultInstance());
              }

              if (!user.isAdmin()) {
                if (user.isTeacher()) {
                  // TODO: Verify the requested project's user is in their class.
                } else if (user.isStudent()) {
                  // Make sure the student is only querying their own projects.
                  if (!Objects.equals(
                      user.get().orElseThrow().getId(),
                      project.project().getProjectInput().getUserX().getId())) {
                    return user.returnForbidden(GetProjectDetailsResponse.getDefaultInstance());
                  }
                }
              }

              return GetProjectDetailsResponse.newBuilder()
                  .setProject(DataAccess.convertProjectWithMilestonesToProto(project))
                  .build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/GetProjects")
  @ResponseBody
  public GetProjectsResponse getProjects(
      @Authenticated HttpUser user,
      @RequestBody Optional<GetProjectsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(GetProjectsResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(GetProjectsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              int userId = request.getUserXId();
              if (user.isAdmin()) {
                // Do nothing.
              } else if (user.isTeacher()) {
                // TODO: Verify the requested user is in their class.
              } else if (user.isStudent()) {
                // Make sure the student is only querying about their own projects.
                if (userId != user.get().orElseThrow().getId()) {
                  return user.returnForbidden(GetProjectsResponse.getDefaultInstance());
                }
              }
              var response = GetProjectsResponse.newBuilder();

              response.addAllProjects(
                  db
                      .getProjectRepository()
                      .findProjectsByUserXId(userId, request.getActiveOnly())
                      .stream()
                      .map(DataAccess::convertProjectToProto)
                      .toList());

              if (request.getIncludeUnsuccessful()) {
                response.addAllUnsuccessfulInputs(
                    db
                        .getProjectInputRepository()
                        .findFullProjectInputByUserAndUnsuccessful(userId)
                        .stream()
                        .map(DataAccess::convertFullProjectInput)
                        .map(
                            org.davincischools.leo.protos.pl_types.ProjectDefinition.Builder::build)
                        .toList());
              }

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/GetAssignmentProjectDefinition")
  @ResponseBody
  public GetAssignmentProjectDefinitionResponse getAssignmentProjectDefinition(
      @Authenticated HttpUser user,
      @RequestBody Optional<GetAssignmentProjectDefinitionRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(GetAssignmentProjectDefinitionResponse.getDefaultInstance());
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
          i ->
              Option.newBuilder()
                  .setId(i.getId())
                  .setName(i.getName())
                  .setShortDescr(i.getShortDescr()),
          input);
      case XQ_COMPETENCY -> populateOptions(
          db.getKnowledgeAndSkillRepository().findAll(Type.XQ_COMPETENCY.name()),
          i ->
              Option.newBuilder()
                  .setId(i.getId())
                  .setName(i.getName())
                  .setShortDescr(i.getShortDescr()),
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
      @Authenticated HttpUser user,
      @RequestBody Optional<GetAssignmentProjectDefinitionsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(GetAssignmentProjectDefinitionsResponse.getDefaultInstance());
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
                    DataAccess.convertFullProjectDefinition(fullDefinition)
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
      @Authenticated HttpUser user,
      @RequestBody Optional<UpdateProjectRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(UpdateProjectResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(UpdateProjectRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              Optional<Project> optionalProject =
                  db.getProjectRepository().findById(request.getId());
              if (optionalProject.isEmpty()) {
                return user.returnNotFound(UpdateProjectResponse.getDefaultInstance());
              }
              Project project = optionalProject.get();

              if (user.isAdmin()) {
                // Do nothing.
              } else if (user.isTeacher()) {
                // TODO: Verify the project is for a student in their class.
              } else if (user.isStudent()) {
                // Make sure the student is only updating their own projects.
                if (!Objects.equals(
                    project.getProjectInput().getUserX().getId(),
                    user.get().orElseThrow().getId())) {
                  return user.returnForbidden(UpdateProjectResponse.getDefaultInstance());
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
                  .setProject(DataAccess.convertProjectToProto(project))
                  .build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/GetProjectPosts")
  @ResponseBody
  public GetProjectPostsResponse getProjectPosts(
      @Authenticated HttpUser user,
      @RequestBody Optional<GetProjectPostsRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(GetProjectPostsResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(GetProjectPostsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              var response = GetProjectPostsResponse.newBuilder();
              response.addAllProjectPosts(
                  db.getProjectPostRepository().findAllByProjectId(request.getProjectId()).stream()
                      .map(DataAccess::convertProjectPostToProto)
                      .toList());
              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/PostMessage")
  @ResponseBody
  public PostMessageResponse postMessage(
      @Authenticated HttpUser user,
      @RequestBody Optional<PostMessageRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(PostMessageResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(PostMessageRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              ProjectPost post =
                  db.getProjectPostRepository()
                      .save(
                          new ProjectPost()
                              .setCreationTime(Instant.now())
                              .setUserX(user.get().orElseThrow())
                              .setProject(new Project().setId(request.getProjectId()))
                              .setName(request.getName())
                              .setMessageHtml(request.getMessageHtml()));

              return PostMessageResponse.newBuilder().setProjectPostId(post.getId()).build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/DeletePost")
  @ResponseBody
  public DeletePostResponse deletePost(
      @Authenticated HttpUser user,
      @RequestBody Optional<DeletePostRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(DeletePostResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(DeletePostRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              db.getProjectPostRepository().deleteById(request.getId());

              return DeletePostResponse.getDefaultInstance();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/GetProjectDefinitionCategoryTypes")
  @ResponseBody
  public GetProjectDefinitionCategoryTypesResponse GetProjectDefinitionCategoryTypes(
      @Anonymous HttpUser user,
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
      @Anonymous HttpUser user,
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
              if (user.isAuthenticated()) {
                definition.setUserX(user.get().orElseThrow());
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
                  generateProjects(user, Optional.of(generateRequest.build()), httpExecutors);
              return response
                  .setProjectInputId(generateProjectsResponse.getProjectInputId())
                  .build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/RegisterAnonymousProjects")
  @ResponseBody
  public RegisterAnonymousProjectsResponse registerAnonymousProjects(
      @Authenticated HttpUser user,
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
                  .updateUserX(request.getProjectInputId(), user.get().get().getId());
              db.getProjectDefinitionRepository()
                  .updateUserX(input.getProjectDefinition().getId(), user.get().get().getId());

              return response.build();
            })
        .finish();
  }
}
