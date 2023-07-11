package org.davincischools.leo.server.controllers;

import static com.google.common.base.Preconditions.checkArgument;

import com.fasterxml.jackson.datatype.jdk8.WrappedIOException;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;
import org.apache.commons.text.StringEscapeUtils;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectDefinition;
import org.davincischools.leo.database.daos.ProjectInput;
import org.davincischools.leo.database.daos.ProjectInputCategory;
import org.davincischools.leo.database.daos.ProjectInputValue;
import org.davincischools.leo.database.daos.ProjectPost;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.KnowledgeAndSkillRepository.Type;
import org.davincischools.leo.database.utils.repos.ProjectDefinitionRepository.ProjectDefinitionInputCategories;
import org.davincischools.leo.database.utils.repos.ProjectInputCategoryRepository.ValueType;
import org.davincischools.leo.database.utils.repos.ProjectInputRepository.State;
import org.davincischools.leo.database.utils.repos.ProjectRepository.ProjectWithMilestones;
import org.davincischools.leo.protos.pl_types.Project.ThumbsState;
import org.davincischools.leo.protos.pl_types.ProjectInputCategory.Option;
import org.davincischools.leo.protos.project_management.DeletePostRequest;
import org.davincischools.leo.protos.project_management.DeletePostResponse;
import org.davincischools.leo.protos.project_management.GenerateProjectsRequest;
import org.davincischools.leo.protos.project_management.GenerateProjectsResponse;
import org.davincischools.leo.protos.project_management.GetEksRequest;
import org.davincischools.leo.protos.project_management.GetEksResponse;
import org.davincischools.leo.protos.project_management.GetProjectDefinitionRequest;
import org.davincischools.leo.protos.project_management.GetProjectDefinitionResponse;
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
import org.davincischools.leo.protos.project_management.UpdateProjectRequest;
import org.davincischools.leo.protos.project_management.UpdateProjectResponse;
import org.davincischools.leo.server.controllers.project_generators.OpenAi3V1ProjectGenerator;
import org.davincischools.leo.server.controllers.project_generators.OpenAi3V2ProjectGenerator;
import org.davincischools.leo.server.utils.DataAccess;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorException;
import org.davincischools.leo.server.utils.http_executor.HttpExecutors;
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
      ProjectDefinitionInputCategories definition,
      ProjectInput input,
      List<ImmutableList<ProjectInputValue>> values,
      GenerateProjectsResponse.Builder response) {}

  @Autowired Database db;
  @Autowired OpenAi3V1ProjectGenerator openAi3V1ProjectGenerator;
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
                  Iterables.transform(
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
                  Iterables.transform(
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
              ProjectDefinitionInputCategories definition =
                  db.getProjectDefinitionRepository()
                      .getProjectDefinition(request.getDefinition().getId())
                      .orElseThrow(
                          () ->
                              new IllegalArgumentException(
                                  "Project definition does not exist: "
                                      + request.getDefinition().getId()));

              GenerateProjectsState state =
                  new GenerateProjectsState(
                      request,
                      definition,
                      new ProjectInput()
                          .setCreationTime(Instant.now())
                          .setProjectDefinition(definition.definition())
                          .setUserX(user.get().orElseThrow())
                          .setState(State.PROCESSING.name())
                          .setTimeout(Instant.now().plus(Duration.ofMinutes(10))),
                      new ArrayList<>(),
                      GenerateProjectsResponse.newBuilder());
              if (request.getAssignmentId() > 0) {
                state.input.setAssignment(new Assignment().setId(request.getAssignmentId()));
              }

              return state;
            })
        .andThen(
            (state, log) -> {
              // Save project values and input.
              if (state.definition.inputCategories().size()
                  != state.request.getDefinition().getInputsList().size()) {
                throw new IllegalArgumentException(
                    "Incorrect number of inputs: "
                        + state.request.getDefinition().getInputsList().size());
              }

              AtomicInteger position = new AtomicInteger(0);
              for (int i = 0; i < state.definition.inputCategories().size(); ++i) {
                ProjectInputCategory category = state.definition.inputCategories().get(i);
                var inputProto = state.request.getDefinition().getInputsList().get(i);

                Supplier<ProjectInputValue> newProjectInputValue =
                    () ->
                        new ProjectInputValue()
                            .setCreationTime(Instant.now())
                            .setProjectInput(state.input)
                            .setProjectInputCategory(category)
                            .setPosition(position.getAndIncrement());

                Builder<ProjectInputValue> projectInputValues = ImmutableList.builder();
                switch (ValueType.valueOf(category.getValueType())) {
                  case FREE_TEXT -> projectInputValues.addAll(
                      throwIfEmptyCategory(
                          inputProto.getFreeTextsList().stream()
                              .map(s -> newProjectInputValue.get().setFreeTextValue(s))
                              .toList()));
                  case EKS -> projectInputValues.addAll(
                      throwIfEmptyCategory(
                          Streams.stream(
                                  db.getKnowledgeAndSkillRepository()
                                      .findAllByIdsAndType(
                                          inputProto.getSelectedIdsList(), Type.EKS.name()))
                              .map(ks -> newProjectInputValue.get().setKnowledgeAndSkillValue(ks))
                              .toList()));
                  case XQ_COMPETENCY -> projectInputValues.addAll(
                      throwIfEmptyCategory(
                          Streams.stream(
                                  db.getKnowledgeAndSkillRepository()
                                      .findAllByIdsAndType(
                                          inputProto.getSelectedIdsList(),
                                          Type.XQ_COMPETENCY.name()))
                              .map(ks -> newProjectInputValue.get().setKnowledgeAndSkillValue(ks))
                              .toList()));
                  case MOTIVATION -> projectInputValues.addAll(
                      throwIfEmptyCategory(
                          Streams.stream(
                                  db.getMotivationRepository()
                                      .findAllByIds(inputProto.getSelectedIdsList()))
                              .map(m -> newProjectInputValue.get().setMotivationValue(m))
                              .toList()));
                }
                state.values.add(projectInputValues.build());
              }

              // Save project projectInput.
              db.getProjectInputRepository().save(state.input);
              log.addProjectInput(state.input);
              db.getProjectInputValueRepository()
                  .saveAll(state.values.stream().flatMap(Collection::stream).toList());

              return state;
            })
        .andThen(
            (state, log) -> {
              List<Project> projects =
                  ImmutableList.of(
                          // openAi3V1ProjectGenerator,
                          openAi3V2ProjectGenerator)
                      .stream()
                      .parallel()
                      .map(
                          generator -> {
                            try {
                              return generator.generateAndSaveProjects(state, httpExecutors, 5);
                            } catch (HttpExecutorException e) {
                              throw new WrappedIOException(e);
                            }
                          })
                      .flatMap(Collection::stream)
                      .toList();

              db.getProjectInputRepository().save(state.input.setState(State.COMPLETED.name()));

              return state.response.build();
            })
        .onError(
            (error, log) -> {
              if (error.lastInput() instanceof GenerateProjectsState state) {
                if (state.input.getId() != null) {
                  db.getProjectInputRepository().save(state.input.setState(State.FAILED.name()));
                }
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
                  db.getProjectRepository().findByProjectId(request.getProjectId()).orElse(null);
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
                  Streams.stream(
                          request.getActiveOnly()
                              ? db.getProjectRepository().findAllActiveByUserXId(userId)
                              : db.getProjectRepository().findAllByUserXId(userId))
                      .map(DataAccess::convertProjectToProto)
                      .toList());

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/GetProjectDefinition")
  @ResponseBody
  public GetProjectDefinitionResponse getProjectDefinition(
      @Authenticated HttpUser user,
      @RequestBody Optional<GetProjectDefinitionRequest> optionalRequest,
      HttpExecutors httpExecutors)
      throws HttpExecutorException {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(GetProjectDefinitionResponse.getDefaultInstance());
    }

    return httpExecutors
        .start(optionalRequest.orElse(GetProjectDefinitionRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              GetProjectDefinitionResponse.Builder response =
                  GetProjectDefinitionResponse.newBuilder();

              // TODO: Just assume there's a single definition.
              ProjectDefinition definitionDao =
                  Iterables.getOnlyElement(
                      db.getProjectDefinitionRepository().findAll().stream()
                          .filter(ProjectDefinition::getTemplate)
                          .toList());
              ProjectDefinitionInputCategories definition =
                  db.getProjectDefinitionRepository()
                      .getProjectDefinition(definitionDao.getId())
                      .orElseThrow();

              response.getDefinitionBuilder().setId(definition.definition().getId());
              for (ProjectInputCategory category : definition.inputCategories()) {
                var input = response.getDefinitionBuilder().addInputsBuilder();
                var inputCategory =
                    input
                        .getCategoryBuilder()
                        .setId(category.getId())
                        .setShortDescr(category.getShortDescr())
                        .setName(category.getName())
                        .setHint(category.getHint())
                        .setPlaceholder(category.getInputPlaceholder())
                        .setValueType(
                            org.davincischools.leo.protos.pl_types.ProjectInputCategory.ValueType
                                .valueOf(category.getValueType()))
                        .setMaxNumValues(category.getMaxNumValues());
                switch (inputCategory.getValueType()) {
                  case EKS -> populateOptions(
                      db.getKnowledgeAndSkillRepository().findAll(Type.EKS.name()),
                      i ->
                          Option.newBuilder()
                              .setId(i.getId())
                              .setName(i.getName())
                              .setShortDescr(i.getShortDescr()),
                      inputCategory);
                  case XQ_COMPETENCY -> populateOptions(
                      db.getKnowledgeAndSkillRepository().findAll(Type.XQ_COMPETENCY.name()),
                      i ->
                          Option.newBuilder()
                              .setId(i.getId())
                              .setName(i.getName())
                              .setShortDescr(i.getShortDescr()),
                      inputCategory);
                  case MOTIVATION -> populateOptions(
                      db.getMotivationRepository().findAll(),
                      i ->
                          Option.newBuilder()
                              .setId(i.getId())
                              .setName(i.getName())
                              .setShortDescr(i.getShortDescr()),
                      inputCategory);
                }
              }

              return response.build();
            })
        .finish();
  }

  private <T> void populateOptions(
      Iterable<T> values,
      Function<T, Option.Builder> toOption,
      org.davincischools.leo.protos.pl_types.ProjectInputCategory.Builder inputCategory) {
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
                  Streams.stream(
                          db.getProjectPostRepository().findAllByProjectId(request.getProjectId()))
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
}
