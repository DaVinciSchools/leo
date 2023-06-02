package org.davincischools.leo.server.controllers;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Joiner;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.text.StringEscapeUtils;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.Motivation;
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
import org.davincischools.leo.protos.open_ai.OpenAiMessage;
import org.davincischools.leo.protos.open_ai.OpenAiRequest;
import org.davincischools.leo.protos.open_ai.OpenAiResponse;
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
import org.davincischools.leo.server.utils.DataAccess;
import org.davincischools.leo.server.utils.LogUtils;
import org.davincischools.leo.server.utils.LogUtils.LogExecutionError;
import org.davincischools.leo.server.utils.LogUtils.LogOperations;
import org.davincischools.leo.server.utils.LogUtils.Status;
import org.davincischools.leo.server.utils.OpenAiUtils;
import org.davincischools.leo.server.utils.http_user.Authenticated;
import org.davincischools.leo.server.utils.http_user.HttpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProjectManagementService {

  private static final Joiner COMMA_AND_JOINER = Joiner.on(", and ");

  private static final Pattern REMOVE_FLUFF =
      Pattern.compile(
          // Match newlines with the '.' character.
          "(?s)"
              // Match the start of the line.
              + "^"
              // A number, e.g., "1: " or "1. "
              + "([0-9]+[:.])?\\s*"
              // A label, e.g., "Title - " or "Full Description: "
              + "(([Pp]roject\\s*[0-9]*|[Tt]itle|[Ss]hort|[Ff]ull|[Ss]ummary)?\\s*"
              + "([Dd]escription)?)?\\s*"
              // The end of the label, e.g., the ":" or " - " from above.
              + "(\\.|:| -)?\\s*" //
              // A second number for, e.g., "Project: 1. "
              + "([0-9]+[:.])?\\s*" //
              // A title after the label, e.g., "Project 1 - Title: "
              + "(Title:)?\\s*" //
              // The main text, either quoted or not. Quotes are removed.
              + "([\"“”](?<quotedText>.*)[\"“”]|(?<unquotedText>.*))"
              // Match the end of the line.
              + "$");
  private static final ImmutableList<String> TEXT_GROUPS =
      ImmutableList.of("quotedText", "unquotedText");

  // Do NOT have any special Regex characters in these delimiters.
  static final String START_OF_PROJECT = "~~prj:ProjectLeoDelimiter~~";
  static final String END_OF_TITLE = "~~title:ProjectLeoDelimiter~~";
  static final String END_OF_SHORT = "~~short:ProjectLeoDelimiter~~";

  @Autowired Database db;
  @Autowired OpenAiUtils openAiUtils;

  @PostMapping(value = "/api/protos/ProjectManagementService/GetEks")
  @ResponseBody
  public GetEksResponse getEks(
      @Authenticated HttpUser user, @RequestBody Optional<GetEksRequest> optionalRequest)
      throws LogExecutionError {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(GetEksResponse.getDefaultInstance());
    }

    return LogUtils.executeAndLog(db, optionalRequest.orElse(GetEksRequest.getDefaultInstance()))
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
      @Authenticated HttpUser user, @RequestBody Optional<GetXqCompetenciesRequest> optionalRequest)
      throws LogExecutionError {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(GetXqCompetenciesResponse.getDefaultInstance());
    }

    return LogUtils.executeAndLog(
            db, optionalRequest.orElse(GetXqCompetenciesRequest.getDefaultInstance()))
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

  public record GenerateProjectsState(
      GenerateProjectsRequest request,
      ProjectDefinitionInputCategories definition,
      ProjectInput input,
      List<ImmutableList<ProjectInputValue>> values,
      GenerateProjectsResponse.Builder response) {}

  @PostMapping(value = "/api/protos/ProjectManagementService/GenerateProjects")
  @ResponseBody
  public GenerateProjectsResponse generateProjects(
      @Authenticated HttpUser user, @RequestBody Optional<GenerateProjectsRequest> optionalRequest)
      throws LogExecutionError {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(GenerateProjectsResponse.getDefaultInstance());
    }

    return LogUtils.executeAndLog(
            db, optionalRequest.orElse(GenerateProjectsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
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
                          .setUserX(user.get())
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
        .retryNextStep(3, 1000)
        .andThen(
            (state, log) -> {
              // Query OpenAI for projects.
              StringBuilder sb = new StringBuilder();
              sb.append(
                  "You are a senior student who wants to spend 60 hours to build a project. ");
              for (int i = 0; i < state.definition.inputCategories().size(); ++i) {
                ProjectInputCategory category = state.definition.inputCategories().get(i);
                ImmutableList<ProjectInputValue> values = state.values.get(i);

                sb.append(category.getQueryPrefix()).append(' ');
                switch (ValueType.valueOf(category.getValueType())) {
                  case FREE_TEXT -> sb.append(
                      COMMA_AND_JOINER.join(
                          values.stream()
                              .map(ProjectInputValue::getFreeTextValue)
                              .map(ProjectManagementService::quoteAndEscape)
                              .toList()));
                  case EKS, XQ_COMPETENCY -> sb.append(
                      COMMA_AND_JOINER.join(
                          values.stream()
                              .map(ProjectInputValue::getKnowledgeAndSkillValue)
                              .map(KnowledgeAndSkill::getShortDescr)
                              .map(ProjectManagementService::quoteAndEscape)
                              .toList()));
                  case MOTIVATION -> sb.append(
                      COMMA_AND_JOINER.join(
                          values.stream()
                              .map(ProjectInputValue::getMotivationValue)
                              .map(Motivation::getShortDescr)
                              .map(ProjectManagementService::quoteAndEscape)
                              .toList()));
                }
                sb.append(". ");
              }

              OpenAiRequest aiRequest =
                  OpenAiRequest.newBuilder()
                      .setModel(OpenAiUtils.GPT_3_5_TURBO_MODEL)
                      .addMessages(
                          OpenAiMessage.newBuilder().setRole("system").setContent(sb.toString()))
                      .addMessages(
                          OpenAiMessage.newBuilder()
                              .setRole("user")
                              .setContent(
                                  String.format(
                                      // Notes:
                                      //
                                      // "then a declarative summary of the project in one
                                      // sentence" caused the short description to be skipped.
                                      //
                                      // Ending the block with the project delimiter caused it
                                      // to be included prematurely, before the full description
                                      // was finished generating.
                                      "Generate 5 projects that would fit the criteria. For each"
                                          + " project, return: 1) the text \"%s\", 2) then a"
                                          + " title, 3) then the text \"%s\", 4) then a short"
                                          + " declarative command statement that summarizes the"
                                          + " project, 5) then the text \"%s\", 6) then a detailed"
                                          + " description of the project followed by major steps"
                                          + " to complete it. Do not return any text before the"
                                          + " first project and do not format the output.",
                                      StringEscapeUtils.escapeJava(START_OF_PROJECT),
                                      StringEscapeUtils.escapeJava(END_OF_TITLE),
                                      StringEscapeUtils.escapeJava(END_OF_SHORT))))
                      .build();

              OpenAiResponse aiResponse =
                  openAiUtils.sendOpenAiRequest(aiRequest, OpenAiResponse.newBuilder()).build();

              List<Project> projects =
                  extractProjects(
                      log,
                      state.response,
                      state.input,
                      aiResponse.getChoices(0).getMessage().getContent());
              db.getProjectRepository().saveAll(projects);
              projects.forEach(log::addProject);

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
            })
        .finish();
  }

  private <T> Iterable<T> throwIfEmptyCategory(Iterable<T> values) {
    if (Iterables.isEmpty(values)) {
      throw new IllegalArgumentException("No projectInput for category");
    }
    return values;
  }

  private static String quoteAndEscape(String s) {
    return "\"" + StringEscapeUtils.escapeJava(s) + "\"";
  }

  static String normalizeAndCheckString(String input) {
    input = input.trim().replaceAll("\\n\\n", "\n");
    Matcher fluffMatcher = REMOVE_FLUFF.matcher(input);
    if (fluffMatcher.matches()) {
      for (String groupName : TEXT_GROUPS) {
        String groupText = fluffMatcher.group(groupName);
        if (groupText != null) {
          input = groupText;
          break;
        }
      }
    }
    return input.trim().replaceAll("\\n\\n", "\n");
  }

  static List<Project> extractProjects(
      LogOperations log,
      GenerateProjectsResponse.Builder response,
      ProjectInput projectInput,
      String aiResponse) {
    List<Project> projects = new ArrayList<>();
    for (String projectText : aiResponse.split(START_OF_PROJECT)) {
      try {
        if (normalizeAndCheckString(projectText).isEmpty()) {
          continue;
        }
        String[] pieces_of_information =
            projectText.trim().split(END_OF_TITLE + "|" + END_OF_SHORT);
        checkArgument(pieces_of_information.length == 3);

        projects.add(
            new Project()
                .setCreationTime(Instant.now())
                .setProjectInput(projectInput)
                .setName(normalizeAndCheckString(pieces_of_information[0]))
                .setShortDescr(normalizeAndCheckString(pieces_of_information[1]))
                .setLongDescr(normalizeAndCheckString(pieces_of_information[2])));
        response.addProjects(DataAccess.convertProjectToProto(projects.get(projects.size() - 1)));
      } catch (Throwable e) {
        log.setStatus(Status.ERROR);
        log.addNote("Could not parse project text because \"%s\": %s", e.getMessage(), projectText);
      }
    }
    return projects;
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/GetProjects")
  @ResponseBody
  public GetProjectsResponse getProjects(
      @Authenticated HttpUser user, @RequestBody Optional<GetProjectsRequest> optionalRequest)
      throws LogExecutionError {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(GetProjectsResponse.getDefaultInstance());
    }

    return LogUtils.executeAndLog(
            db, optionalRequest.orElse(GetProjectsRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              int userId = request.getUserXId();
              if (user.isAdmin()) {
                // Do nothing.
              } else if (user.isTeacher()) {
                // TODO: Verify the requested user is in their class.
              } else if (user.isStudent()) {
                // Make sure the student is only querying about their own projects.
                if (userId != user.get().getId()) {
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
      @RequestBody Optional<GetProjectDefinitionRequest> optionalRequest)
      throws LogExecutionError {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(GetProjectDefinitionResponse.getDefaultInstance());
    }

    return LogUtils.executeAndLog(
            db, optionalRequest.orElse(GetProjectDefinitionRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              GetProjectDefinitionResponse.Builder response =
                  GetProjectDefinitionResponse.newBuilder();

              // TODO: Just assume there's a single definition.
              ProjectDefinition definitionDao =
                  Iterables.getOnlyElement(db.getProjectDefinitionRepository().findAll());
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
                        .setTitle(category.getTitle())
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
                              .setDescription(i.getShortDescr()),
                      inputCategory);
                  case XQ_COMPETENCY -> populateOptions(
                      db.getKnowledgeAndSkillRepository().findAll(Type.XQ_COMPETENCY.name()),
                      i ->
                          Option.newBuilder()
                              .setId(i.getId())
                              .setName(i.getName())
                              .setDescription(i.getShortDescr()),
                      inputCategory);
                  case MOTIVATION -> populateOptions(
                      db.getMotivationRepository().findAll(),
                      i ->
                          Option.newBuilder()
                              .setId(i.getId())
                              .setName(i.getName())
                              .setDescription(i.getShortDescr()),
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
      @Authenticated HttpUser user, @RequestBody Optional<UpdateProjectRequest> optionalRequest)
      throws LogExecutionError {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(UpdateProjectResponse.getDefaultInstance());
    }

    return LogUtils.executeAndLog(
            db, optionalRequest.orElse(UpdateProjectRequest.getDefaultInstance()))
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
                    project.getProjectInput().getUserX().getId(), user.get().getId())) {
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
      @Authenticated HttpUser user, @RequestBody Optional<GetProjectPostsRequest> optionalRequest)
      throws LogExecutionError {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(GetProjectPostsResponse.getDefaultInstance());
    }

    return LogUtils.executeAndLog(
            db, optionalRequest.orElse(GetProjectPostsRequest.getDefaultInstance()))
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
      @Authenticated HttpUser user, @RequestBody Optional<PostMessageRequest> optionalRequest)
      throws LogExecutionError {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(PostMessageResponse.getDefaultInstance());
    }

    return LogUtils.executeAndLog(
            db, optionalRequest.orElse(PostMessageRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              ProjectPost post =
                  db.getProjectPostRepository()
                      .save(
                          new ProjectPost()
                              .setCreationTime(Instant.now())
                              .setUserX(user.get())
                              .setProject(new Project().setId(request.getProjectId()))
                              .setTitle(request.getTitle())
                              .setMessage(request.getMessage()));

              return PostMessageResponse.newBuilder().setProjectPostId(post.getId()).build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ProjectManagementService/DeletePost")
  @ResponseBody
  public DeletePostResponse deletePost(
      @Authenticated HttpUser user, @RequestBody Optional<DeletePostRequest> optionalRequest)
      throws LogExecutionError {
    if (user.isNotAuthorized()) {
      return user.returnForbidden(DeletePostResponse.getDefaultInstance());
    }

    return LogUtils.executeAndLog(
            db, optionalRequest.orElse(DeletePostRequest.getDefaultInstance()))
        .andThen(
            (request, log) -> {
              db.getProjectPostRepository().deleteById(request.getId());

              return DeletePostResponse.getDefaultInstance();
            })
        .finish();
  }
}
