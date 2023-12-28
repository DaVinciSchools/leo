package org.davincischools.leo.database.admin_x;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import jakarta.persistence.EntityManager;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import javax.sql.DataSource;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.daos.AdminX;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.ProjectDefinition;
import org.davincischools.leo.database.daos.ProjectDefinitionCategory;
import org.davincischools.leo.database.daos.ProjectDefinitionCategoryType;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.test.TestData;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.UserXUtils;
import org.davincischools.leo.database.utils.repos.AdminXRepository;
import org.davincischools.leo.database.utils.repos.GetClassXsParams;
import org.davincischools.leo.database.utils.repos.KnowledgeAndSkillRepository.Type;
import org.davincischools.leo.database.utils.repos.ProjectDefinitionCategoryTypeRepository.ValueType;
import org.davincischools.leo.database.utils.repos.UserXRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(scanBasePackageClasses = {Database.class})
public class AdminXUtils {
  public enum DaVinciSchoolsByNickname {
    DVC("Da Vinci Communications High School", "201 N. Douglas St., El Segundo, CA 90245"),
    DVConnect("Da Vinci Connect High School", "550 Continental Blvd., El Segundo, CA 90245"),
    DVD("Da Vinci Design High School", "201 N. Douglas St., El Segundo, CA 90245"),
    DVFlex("Da Vinci Flex High School", "Address TBD"),
    DVRISE("Da Vinci Rise High School-Richstone", "13634 Cordary Avenue, Hawthorne, CA 90250"),
    DVS("Da Vinci Science High School", "201 N. Douglas St., El Segundo, CA 90245");

    private final String name;
    private final String address;

    DaVinciSchoolsByNickname(String name, String address) {
      this.name = name;
      this.address = address;
    }

    public String getName() {
      return name;
    }

    public String getAddress() {
      return address;
    }

    public School createSchool(Database db, District district) {
      return db.getSchoolRepository()
          .upsert(district, this.name(), name, s -> s.setAddress(address));
    }
  }

  public enum XqCategoriesByNickname {
    ID(
        "Interdisciplinary",
        "Communication",
        "Create arguments, support claims, and engage in critical dialogue"),
    HUM(
        "Humanities",
        "Governments",
        "Cultivate, refine, and leverage perspectives on how governments work"
            + " and how they could be improved"),
    STEAM(
        "Science, Technology, Engineering, Arts, and Math (STEM)",
        "Scientific Thinking",
        "Work with quantitative data to understand, represent, and predict relationships"),
    SEL(
        "Social, Emotional Learning (SS)",
        "Relationship Building",
        "Build and maintain healthy relationships");

    private final String category;
    private final String exampleName;
    private final String exampleDescription;

    XqCategoriesByNickname(String category, String exampleName, String exampleDescription) {
      this.category = category;
      this.exampleName = exampleName;
      this.exampleDescription = exampleDescription;
    }

    public String getCategory() {
      return category;
    }

    public String getExampleName() {
      return exampleName;
    }

    public String getExampleDescription() {
      return exampleDescription;
    }
  }

  public static class ProjectDefinitionCategoryTypeBuilder {

    private static final AtomicInteger position = new AtomicInteger();

    private final Database db;
    private final ProjectDefinitionCategoryType type;

    public ProjectDefinitionCategoryTypeBuilder(Database db, ProjectDefinitionCategoryType type) {
      this.db = db;
      this.type = type;
    }

    public ProjectDefinitionCategory addProjectDefinitionCategory(
        ProjectDefinition projectDefinition, Consumer<ProjectDefinitionCategory> modifier) {
      ProjectDefinitionCategory pdc =
          db.getProjectDefinitionCategoryRepository()
              .upsert(
                  projectDefinition,
                  type,
                  pdc2 -> pdc2.setPosition((float) position.getAndIncrement()).setMaxNumValues(4));
      modifier.accept(pdc);
      db.getProjectDefinitionCategoryRepository().save(pdc);
      return pdc;
    }
  }

  public static class ProjectDefinitionCategoryTypes {
    private final Database db;

    private final ProjectDefinitionCategoryTypeBuilder studentInterestsType;
    private final ProjectDefinitionCategoryTypeBuilder careerInterestType;
    private final ProjectDefinitionCategoryTypeBuilder assignmentTopicType;
    private final ProjectDefinitionCategoryTypeBuilder motivationType;
    private final ProjectDefinitionCategoryTypeBuilder projectLeadTheWayType;
    private final ProjectDefinitionCategoryTypeBuilder eksType;
    private final ProjectDefinitionCategoryTypeBuilder xqType;
    private final ProjectDefinitionCategoryTypeBuilder cteType;

    public ProjectDefinitionCategoryTypes(Database db) {
      this.db = db;

      // The first four below are automatically included for the demo.

      studentInterestsType =
          new ProjectDefinitionCategoryTypeBuilder(
              db,
              db.getProjectDefinitionCategoryTypeRepository()
                  .upsert(
                      "Student Interests",
                      type ->
                          type.setShortDescr(
                                  "Include this category to allow a student to enter their"
                                      + " personal interests. They will be considered when"
                                      + " creating projects for that individual student.")
                              .setIncludeInDemo(true)
                              .setHint("Click to add your personal interests.")
                              .setInputDescr(
                                  "Type in things that interest you that you'd like a project to"
                                      + " include. You can add multiple interests by pressing"
                                      + " ENTER after each one. An interest would be something"
                                      + " like 'playing sports' or 'watching youtube videos about"
                                      + " building things'.")
                              .setInputPlaceholder("Student Interest")
                              .setQueryPrefix("You would like projects that involve")
                              .setValueType(ValueType.FREE_TEXT.name())));

      careerInterestType =
          new ProjectDefinitionCategoryTypeBuilder(
              db,
              db.getProjectDefinitionCategoryTypeRepository()
                  .upsert(
                      "Career Interests",
                      type ->
                          type.setShortDescr(
                                  "Include this category to allow a student to enter their"
                                      + " career interests. They will be considered when"
                                      + " creating projects for that individual student.")
                              .setIncludeInDemo(true)
                              .setHint("Click to add careers.")
                              .setInputDescr(
                                  "Type in careers that interest you. You can add"
                                      + " multiple careers by pressing ENTER after each one. A"
                                      + " career would be something like 'doctor' or 'english"
                                      + " teacher'.")
                              .setInputPlaceholder("Career Interest")
                              .setQueryPrefix(
                                  "You would like projects that help you learn how to be a")
                              .setValueType(ValueType.FREE_TEXT.name())));

      assignmentTopicType =
          new ProjectDefinitionCategoryTypeBuilder(
              db,
              db.getProjectDefinitionCategoryTypeRepository()
                  .upsert(
                      "Assignment Topic",
                      type ->
                          type.setShortDescr(
                                  "This is a demo category that allows you to enter topics of an"
                                      + " imaginary assignment that the projects will be generated"
                                      + " for. Normally, this would be pulled from an actual"
                                      + " class assignment and not entered by the student.")
                              .setIncludeInDemo(true)
                              .setHint("Click to add assignment topic.")
                              .setInputDescr(
                                  "Normally, this would be pulled from an assignment. But, you"
                                      + " can enter example topics that a project should help"
                                      + " a student learn. You can enter multiple topics by"
                                      + " pressing ENTER after each one.A topic would be"
                                      + " something like 'learn how valence electrons work' or"
                                      + " 'learn how to write a persuasive essay'.")
                              .setInputPlaceholder("Assignment Topic")
                              .setQueryPrefix(
                                  "It is very important that every project MUST help you")
                              .setValueType(ValueType.FREE_TEXT.name())));

      motivationType =
          new ProjectDefinitionCategoryTypeBuilder(
              db,
              db.getProjectDefinitionCategoryTypeRepository()
                  .upsert(
                      "TruMotivate",
                      type ->
                          type.setShortDescr(
                                  "This is a demo category where you can select results from a"
                                      + " [TruMotivate](https://trumotivate.com/) assessment."
                                      + " Normally, this would be pulled from a student's actual"
                                      + " TruMotivate assessment results and not entered by the"
                                      + " student.")
                              .setIncludeInDemo(true)
                              .setHint("Click to add motivations.")
                              .setInputDescr(
                                  "Normally, this would be pulled from a student's"
                                      + " [TruMotivate](https://trumotivate.com/) assessment."
                                      + " But, for demo purposes, you can select multiple results"
                                      + " from the options below.")
                              .setInputPlaceholder("Select a Motivation")
                              .setQueryPrefix("You are motivated by")
                              .setValueType(ValueType.MOTIVATION.name())));

      projectLeadTheWayType =
          new ProjectDefinitionCategoryTypeBuilder(
              db,
              db.getProjectDefinitionCategoryTypeRepository()
                  .upsert(
                      "Project Lead The Way",
                      type ->
                          type.setShortDescr(
                                  "This is a demo category for adding [Project Lead The Way"
                                      + " (PLTW)](https://www.pltw.org/) standards that a course"
                                      + " is working towards. Normally, these standards would be"
                                      + " pulled from class information or an assignment and not"
                                      + " entered by the student.")
                              .setIncludeInDemo(true)
                              .setHint("Click to add PLTW standards.")
                              .setInputDescr(
                                  "Normally, this would be pulled from class information or an"
                                      + " assignment. But, for demo purposes, you can enter"
                                      + " examples of standards that a class or assignment might"
                                      + " be working towards. A standard would be something like"
                                      + " 'developing and using models' or 'using mathematics and"
                                      + " computational thinking'.")
                              .setInputPlaceholder("Enter PLTW Standards")
                              .setQueryPrefix(
                                  "It is very important that every project MUST help you with")
                              .setValueType(ValueType.FREE_TEXT.name())));

      eksType =
          new ProjectDefinitionCategoryTypeBuilder(
              db,
              db.getProjectDefinitionCategoryTypeRepository()
                  .upsert(
                      "Knowledge and Skills",
                      type ->
                          type.setShortDescr(
                                  "This is a demo category for selecting Da Vinci School class'"
                                      + " Essential Knowledge and Skills (EKS). These are the"
                                      + " knowledge and skills that the class will teach the"
                                      + " students. Normally, these would be pulled from class"
                                      + " information or an assignment and not entered by the"
                                      + " student.")
                              .setIncludeInDemo(true)
                              .setHint("Click to add desired knowledge and skills.")
                              .setInputDescr(
                                  "Normally, this would be pulled from class information or an"
                                      + " assignment. But, for demo purposes, you can select"
                                      + " multiple EKS' from the options below.")
                              .setInputPlaceholder("Select a Knowledge and Skill")
                              .setQueryPrefix(
                                  "It is very important that every project"
                                      + " MUST help you learn how to")
                              .setValueType(ValueType.EKS.name())));

      xqType =
          new ProjectDefinitionCategoryTypeBuilder(
              db,
              db.getProjectDefinitionCategoryTypeRepository()
                  .upsert(
                      "XQ Competencies",
                      type ->
                          type.setShortDescr(
                                  "This is a demo category for selecting XQ Competencies for"
                                      + " projects. Normally, these would be pulled from class"
                                      + " information or an assignment and not entered by the"
                                      + " student.")
                              .setIncludeInDemo(true)
                              .setHint("Click to add desired XQ competency.")
                              .setInputDescr(
                                  "Normally, this would be pulled from class information or an"
                                      + " assignment. But, for demo purposes, you can select"
                                      + " multiple XQ Competencies from the options below.")
                              .setInputPlaceholder("Select an XQ Competency")
                              .setQueryPrefix(
                                  "It is very important that every project"
                                      + " MUST help you learn how to")
                              .setValueType(ValueType.XQ_COMPETENCY.name())));

      cteType =
          new ProjectDefinitionCategoryTypeBuilder(
              db,
              db.getProjectDefinitionCategoryTypeRepository()
                  .upsert(
                      "Career & Technical Education",
                      type ->
                          type.setShortDescr(
                                  "[Career & Technical Education"
                                      + " (CTE)](https://www.cde.ca.gov/ci/ct/) courses that"
                                      + " integrate core academic knowledge with technical and"
                                      + " occupational knowledge to provide students with a"
                                      + " pathway to postsecondary education and careers.")
                              .setIncludeInDemo(true)
                              .setHint("Click to add CTE Industry Sectors.")
                              .setInputDescr(
                                  "Select [Career & Technical Education"
                                      + " (CTE)](https://www.cde.ca.gov/ci/ct/) industry sectors.")
                              .setInputPlaceholder("Select CTE Industry Sectors")
                              .setQueryPrefix(
                                  "You would like projects that are part of the following"
                                      + " industries")
                              .setValueType(ValueType.CTE.name())));
    }

    public ProjectDefinitionCategoryTypeBuilder getCareerInterestType() {
      return careerInterestType;
    }

    public ProjectDefinitionCategoryTypeBuilder getMotivationType() {
      return motivationType;
    }

    public ProjectDefinitionCategoryTypeBuilder getEksType() {
      return eksType;
    }

    public ProjectDefinitionCategoryTypeBuilder getXqType() {
      return xqType;
    }

    public ProjectDefinitionCategoryTypeBuilder getStudentInterestsType() {
      return studentInterestsType;
    }

    public ProjectDefinitionCategoryTypeBuilder getAssignmentTopicType() {
      return assignmentTopicType;
    }

    public ProjectDefinitionCategoryTypeBuilder getProjectLeadTheWayType() {
      return projectLeadTheWayType;
    }
  }

  private static final Joiner ERROR_JOINER = Joiner.on("\n - ");

  private static final Logger log = LogManager.getLogger();

  private record Error(String value, Exception e) {}

  private static String createPassword() {
    return new RandomStringGenerator.Builder()
        .withinRange(new char[] {'a', 'z'}, new char[] {'0', '9'})
        .build()
        .generate(20);
  }

  @Autowired private DataSource dataSource;
  @Autowired private Database db;
  @Autowired private EntityManager entityManager;

  @Value("${createDistrict:}")
  private String createDistrict;

  @Value("${createAdmin:}")
  private List<String> createAdmins;

  @Value("${importTeachers:}")
  private String importTeachers;

  @Value("${importStudents:}")
  private String importStudents;

  @Value("${importXqEks:}")
  private String importXqEks;

  @Value("${importMotivations:}")
  private String importMotivations;

  @Value("${delimiter:[\\t]}")
  private String delimiter;

  @Value("${resetPassword:}")
  private List<String> resetPasswords;

  @Value("${loadSchema:false}")
  private String loadSchema;

  @Value("${loadTestData:false}")
  private String loadTestData;

  @Autowired private AdminXRepository adminXRepository;

  private District createDistrict() {
    checkArgument(createDistrict != null, "--createDistrict required.");

    return db.getDistrictRepository().upsert(createDistrict);
  }

  public static UserX createUserX(
      Database db,
      EntityManager entityManager,
      District district,
      String emailAddress,
      Consumer<UserX> modifier) {
    checkArgument(!Strings.isNullOrEmpty(emailAddress));
    checkNotNull(modifier);

    UserX userX = db.getUserXRepository().findByEmailAddress(emailAddress).orElse(null);
    if (userX == null) {
      userX =
          new UserX()
              .setCreationTime(Instant.now())
              .setDistrict(district)
              .setEmailAddress(emailAddress)
              .setFirstName("First Name")
              .setLastName("Last Name")
              .setEncodedPassword(UserXRepository.INVALID_ENCODED_PASSWORD);
    }
    userX.setDeleted(null);

    modifier.accept(userX);

    return db.getUserXRepository().save(userX);
  }

  public static UserX createAdminX(Database db, UserX userX) {
    checkNotNull(userX);

    if (userX.getAdminX() == null) {
      userX.setAdminX(db.getAdminXRepository().save(new AdminX().setCreationTime(Instant.now())));
    } else {
      userX.setAdminX(db.getAdminXRepository().findById(userX.getAdminX().getId()).orElseThrow());
    }
    db.getUserXRepository().save(userX);

    return userX;
  }

  public static UserX createTeacher(Database db, UserX userX) {
    checkNotNull(userX);

    if (userX.getTeacher() == null) {
      userX.setTeacher(
          db.getTeacherRepository().save(new Teacher().setCreationTime(Instant.now())));
    } else {
      userX.setTeacher(
          db.getTeacherRepository().findById(userX.getTeacher().getId()).orElseThrow());
    }
    db.getUserXRepository().save(userX);

    return userX;
  }

  public static UserX createStudent(Database db, UserX userX, Consumer<Student> modifier) {
    checkNotNull(userX);

    if (userX.getStudent() == null) {
      userX.setStudent(
          db.getStudentRepository().save(new Student().setCreationTime(Instant.now())));
    } else {
      userX.setStudent(
          db.getStudentRepository().findById(userX.getStudent().getId()).orElseThrow());
    }
    db.getUserXRepository().save(userX);

    modifier.accept(userX.getStudent());

    return userX;
  }

  private void createAdmins() {
    checkArgument(!createAdmins.isEmpty(), "--createAdmin required.");

    for (String createAdminEmail : createAdmins) {
      var password = new AtomicReference<String>(null);
      UserX userX =
          createUserX(
              db,
              entityManager,
              createDistrict(),
              createAdminEmail,
              u -> {
                if (Objects.equals(
                    u.getEncodedPassword(), UserXRepository.INVALID_ENCODED_PASSWORD)) {
                  password.set(createPassword());
                  UserXUtils.setPassword(u, password.get());
                }
              });

      createAdminX(db, userX);
      createTeacher(db, userX);
      createStudent(db, userX, s -> {});

      addAdminXToSchoolsAndClassXs(db, userX);

      if (password.get() != null) {
        log.atWarn()
            .log(
                "IMPORTANT! Log in and change the temporary password:"
                    + " login: \"{}\", temporary password: \"{}\"",
                createAdminEmail,
                password);
      }
    }
  }

  // TODO: For now admins are teachers and students of all schools and classes in their district.
  public static void addAdminXToSchoolsAndClassXs(Database db, UserX admin) {
    Set<Integer> schoolIds = new HashSet<>();
    for (School school : db.getSchoolRepository().findAll()) {
      if (Objects.equals(school.getDistrict().getId(), admin.getDistrict().getId())) {
        schoolIds.add(school.getId());
        db.getTeacherSchoolRepository().upsert(admin.getTeacher(), school);
        db.getStudentSchoolRepository().upsert(admin.getStudent(), school);
      }
    }
    for (ClassX classX : db.getClassXRepository().findAll()) {
      if (classX.getSchool() == null) {
        continue;
      }
      if (schoolIds.contains(classX.getSchool().getId())) {
        db.getTeacherClassXRepository().upsert(admin.getTeacher(), classX);
        db.getStudentClassXRepository().upsert(admin.getStudent(), classX);
      }
    }
    db.getUserXRepository().save(admin);
  }

  private void resetPasswords() {
    checkArgument(!resetPasswords.isEmpty(), "--resetPassword required.");
    resetPasswords.stream()
        .parallel()
        .forEach(
            emailAddress -> {
              UserX userX = db.getUserXRepository().findByEmailAddress(emailAddress).orElseThrow();

              String password = createPassword();
              db.getUserXRepository().save(UserXUtils.setPassword(userX, password));
              log.atWarn()
                  .log(
                      "IMPORTANT! Log in and change the temporary password:"
                          + " login: \"{}\", temporary password: \"{}\"",
                      emailAddress,
                      password);
            });
  }

  private void importTeachers() throws IOException {
    checkArgument(!createAdmins.isEmpty(), "--createAdmin required.");
    checkArgument(importTeachers != null, "--importTeachers required.");

    District district = createDistrict();
    UserX userX = db.getUserXRepository().findByEmailAddress(createAdmins.get(0)).orElseThrow();

    List<Error> errors = Collections.synchronizedList(new ArrayList<>());

    List<Object> userXs =
        Files.readLines(new File(importTeachers), StandardCharsets.UTF_8).stream()
            .map(
                line -> {
                  try {
                    String[] cells = line.split(delimiter);
                    if (cells.length < 4) {
                      throw new IllegalArgumentException(
                          "Unexpected number of columns: " + cells.length + ".");
                    }

                    String firstName = cells[0];
                    String lastName = cells[1];
                    String emailAddress = cells[2];
                    String schoolNickname = cells[3];

                    checkArgument(!firstName.isEmpty(), "First name required.");
                    checkArgument(!lastName.isEmpty(), "Last name required.");
                    checkArgument(!emailAddress.isEmpty(), "Email address required.");
                    checkArgument(!schoolNickname.isEmpty(), "School nickname required.");
                    checkArgument(emailAddress.contains("@"), "Invalid e-mail address.");

                    // UserX teacher =
                    // db.getUserXRepository()
                    // .upsert(
                    // district,
                    // emailAddress,
                    // userX ->
                    // db.getTeacherRepository()
                    // .upsert(userX)
                    // .setFirstName(firstName)
                    // .setLastName(lastName));
                    // School school =
                    // DaVinciSchoolsByNickname.valueOf(schoolNickname).createSchool(db, district);
                    // db.getTeacherSchoolRepository().upsert(teacher.getTeacher(), school);

                    if (cells.length >= 5) {
                      String className = cells[4];

                      checkArgument(!className.isEmpty(), "Class name required.");

                      // ClassX classX = db.getClassXRepository().upsert(school, className, c ->
                      // {});
                      // db.getTeacherClassXRepository().upsert(teacher.getTeacher(), classX);

                      for (int eksIndex : new int[] {5, 7}) {
                        if (cells.length >= eksIndex + 2) {
                          String eksName = cells[eksIndex];
                          String eksDescr = cells[eksIndex + 1];

                          checkArgument(!eksName.isEmpty(), "EKS name required.");
                          checkArgument(!eksDescr.isEmpty(), "EKS description required.");

                          KnowledgeAndSkill eks =
                              db.getKnowledgeAndSkillRepository()
                                  .save(
                                      new KnowledgeAndSkill()
                                          .setCreationTime(Instant.now())
                                          .setName(eksName)
                                          .setType(Type.EKS.name())
                                          .setShortDescr(eksDescr)
                                          .setGlobal(true)
                                          .setUserX(userX));
                        }
                      }
                    }

                    log.atTrace().log("Imported: {}", line);

                    // return teacher;
                    return null;
                  } catch (Exception e) {
                    log.atError().withThrowable(e).log("Error: {}", line);
                    errors.add(new Error(line, e));
                    return null;
                  }
                })
            .filter(Objects::nonNull)
            // .parallel()
            // .peek(
            // userX -> {
            // if (userX.getEncodedPassword().equals(UserXRepository.INVALID_ENCODED_PASSWORD)) {
            // UserUtils.setPassword(userX, userX.getEmailAddress());
            // }
            // })
            .toList();

    // db.getUserXRepository().saveAll(userXs);

    if (!errors.isEmpty()) {
      log.atError()
          .log(
              "There were errors during the import of the following teachers:\n - {}\n",
              ERROR_JOINER.join(
                  Lists.transform(errors, e -> e.value + "  --  " + e.e.getMessage())));
    }

    log.atInfo().log("Done importing teachers.");
  }

  private void importStudents() throws IOException {
    checkArgument(importStudents != null, "--importStudents required.");

    District district = createDistrict();

    List<Error> errors = Collections.synchronizedList(new ArrayList<>());

    List<UserX> students =
        Files.readLines(new File(importStudents), StandardCharsets.UTF_8).stream()
            .map(
                line -> {
                  try {
                    String[] cells = line.split(delimiter);
                    if (cells.length < 6) {
                      throw new IllegalArgumentException(
                          "Unexpected number of columns: " + cells.length + ".");
                    }

                    int id = Integer.parseInt(cells[0]);
                    int grade = Integer.parseInt(cells[1]);
                    String lastName = cells[2];
                    String firstName = cells[3];
                    String emailAddress = cells[4];
                    String schoolNickname = cells[5];

                    checkArgument(!lastName.isEmpty(), "Last name required.");
                    checkArgument(!firstName.isEmpty(), "First name required.");
                    checkArgument(!emailAddress.isEmpty(), "Email address required.");
                    checkArgument(!schoolNickname.isEmpty(), "School nickname required.");

                    UserX student =
                        createUserX(
                            db,
                            entityManager,
                            district,
                            emailAddress,
                            u -> {
                              u.setFirstName(firstName).setLastName(lastName);
                            });
                    createStudent(
                        db,
                        student,
                        s -> {
                          s.setDistrictStudentId(id).setGrade(grade);
                        });

                    School school =
                        DaVinciSchoolsByNickname.valueOf(schoolNickname).createSchool(db, district);
                    db.getStudentSchoolRepository().upsert(student.getStudent(), school);

                    db.getClassXRepository()
                        .getClassXs(
                            new GetClassXsParams().setSchoolIds(ImmutableList.of(school.getId())))
                        .forEach(
                            classX ->
                                db.getStudentClassXRepository()
                                    .upsert(student.getStudent(), classX));

                    log.atTrace().log("Imported: {}", line);

                    return student;
                  } catch (Exception e) {
                    log.atError().withThrowable(e).log("Error: {}", line);
                    errors.add(new Error(line, e));
                    return null;
                  }
                })
            .filter(Objects::nonNull)
            .parallel()
            .peek(
                userX -> {
                  if (userX.getEncodedPassword().equals(UserXRepository.INVALID_ENCODED_PASSWORD)) {
                    UserXUtils.setPassword(
                        userX, userX.getLastName() + userX.getStudent().getDistrictStudentId());
                  }
                })
            .toList();

    db.getUserXRepository().saveAll(students);

    if (!errors.isEmpty()) {
      log.atError()
          .log(
              "There were errors during the import of the following students:\n - {}\n",
              ERROR_JOINER.join(
                  Iterables.transform(errors, e -> e.value + "  --  " + e.e.getMessage())));
    }

    log.atInfo().log("Done importing students.");
  }

  private void importXqEks() throws IOException {
    checkArgument(!createAdmins.isEmpty(), "--createAdmin required.");
    checkArgument(importXqEks != null, "--importXqEks required.");

    District district = createDistrict();
    UserX userX = db.getUserXRepository().findByEmailAddress(createAdmins.get(0)).orElseThrow();
    School school = DaVinciSchoolsByNickname.DVRISE.createSchool(db, district);

    List<Error> errors = Collections.synchronizedList(new ArrayList<>());
    Set<Integer> classXIds = new HashSet<>();

    for (String line : Files.readLines(new File(importXqEks), StandardCharsets.UTF_8)) {
      try {
        String[] cells = line.split(delimiter);
        if (cells.length != 4) {
          throw new IllegalArgumentException("Unexpected number of columns: " + cells.length + ".");
        }

        String xqName = cells[0];
        String xqDescr = cells[1];
        String outcome = cells[2];
        XqCategoriesByNickname xqCategory = XqCategoriesByNickname.valueOf(cells[3]);

        checkArgument(!xqName.isEmpty(), "Title required.");
        checkArgument(!xqDescr.isEmpty(), "Description required.");
        checkArgument(!outcome.isEmpty(), "Outcome required.");

        KnowledgeAndSkill knowledgeAndSkill =
            db.getKnowledgeAndSkillRepository()
                .save(
                    new KnowledgeAndSkill()
                        .setCreationTime(Instant.now())
                        .setName(xqName)
                        .setType(Type.XQ_COMPETENCY.name())
                        .setShortDescr(xqDescr)
                        .setGlobal(true)
                        .setCategory(xqCategory.getCategory())
                        .setUserX(userX));

        // ClassX classX =
        // db.getClassXRepository().upsert(school, xqName, c -> c.setShortDescr(xqDescr));
        // db.getClassXKnowledgeAndSkillRepository().upsert(classX, knowledgeAndSkill);
        //
        // Assignment assignment =
        // db.getAssignmentRepository().upsert(classX, xqName, a -> a.setShortDescr(xqDescr));
        // db.getAssignmentKnowledgeAndSkillRepository().upsert(assignment, knowledgeAndSkill);
        //
        // classXIds.add(classX.getId());

        log.atTrace().log("Imported: {}", line);
      } catch (Exception e) {
        log.atError().withThrowable(e).log("Error: {}", line);
        errors.add(new Error(line, e));
      }
    }
    try {
      // for (TeacherSchool teacherSchool : db.getTeacherSchoolRepository().findAll()) {
      // if (teacherSchool.getSchool().getId().equals(school.getId())) {
      // for (int classXId : classXIds) {
      // ClassX classX = new ClassX().setId(classXId);
      // db.getTeacherClassXRepository().upsert(teacherSchool.getTeacher(), classX);
      // }
      // }
      // }
    } catch (Exception e) {
      log.atError().withThrowable(e).log("Error: adding teachers to classes.");
      errors.add(new Error("N/A", e));
    }

    if (!errors.isEmpty()) {
      log.atError()
          .log(
              "There were errors during the import of the following XQ EKS':\n - {}\n",
              ERROR_JOINER.join(
                  Lists.transform(errors, e -> e.value + "  --  " + e.e.getMessage())));
    }

    log.atInfo().log("Done importing XQ EKS'.");
  }

  private void importMotivations() throws IOException {
    checkArgument(importMotivations != null, "--importMotivations required.");

    List<Error> errors = Collections.synchronizedList(new ArrayList<>());

    for (String line : Files.readLines(new File(importMotivations), StandardCharsets.UTF_8)) {
      try {
        String[] cells = line.split(delimiter);
        if (cells.length != 2) {
          throw new IllegalArgumentException("Unexpected number of columns: " + cells.length + ".");
        }

        String title = cells[0];
        String descr = cells[1];

        checkArgument(!title.isEmpty(), "Title required.");
        checkArgument(!descr.isEmpty(), "Description required.");

        checkArgument(!title.equals("Title"), "Header row.");

        db.getMotivationRepository().upsert(title, descr, m -> {});

        log.atTrace().log("Imported: {}", line);
      } catch (Exception e) {
        log.atError().withThrowable(e).log("Error: {}", line);
        errors.add(new Error(line, e));
      }
    }

    if (!errors.isEmpty()) {
      log.atError()
          .log(
              "There were errors during the import of the following motivations:\n - {}\n",
              ERROR_JOINER.join(
                  Lists.transform(errors, e -> e.value + "  --  " + e.e.getMessage())));
    }

    log.atInfo().log("Done importing motivations.");
  }

  public static void addIkigaiDiagramDescriptions(
      Database db, UserX creator, Iterable<Assignment> assignments) {
    log.atInfo().log("Creating Ikigai Diagram descriptions");

    ProjectDefinitionCategoryTypes types = new ProjectDefinitionCategoryTypes(db);

    // Create project definitions.
    ProjectDefinition ksDefinition =
        db.getProjectDefinitionRepository()
            .upsert("Knowledge and Skills + Motivations", creator, def -> def.setUserX(creator));
    types.getCareerInterestType().addProjectDefinitionCategory(ksDefinition, entity -> {});
    types.getMotivationType().addProjectDefinitionCategory(ksDefinition, entity -> {});
    types.getEksType().addProjectDefinitionCategory(ksDefinition, entity -> {});
    types.getStudentInterestsType().addProjectDefinitionCategory(ksDefinition, entity -> {});

    ProjectDefinition xqDefinition =
        db.getProjectDefinitionRepository()
            .upsert("XQ Competencies", creator, def -> def.setUserX(creator));
    types.getCareerInterestType().addProjectDefinitionCategory(xqDefinition, entity -> {});
    types.getXqType().addProjectDefinitionCategory(xqDefinition, entity -> {});
    types.getStudentInterestsType().addProjectDefinitionCategory(xqDefinition, entity -> {});

    ProjectDefinition requirementDefinition =
        db.getProjectDefinitionRepository()
            .upsert("Requirements Template", creator, pd -> pd.setUserX(creator).setTemplate(true));
    types.getEksType().addProjectDefinitionCategory(requirementDefinition, entity -> {});
    types.getXqType().addProjectDefinitionCategory(requirementDefinition, entity -> {});
    types.getMotivationType().addProjectDefinitionCategory(requirementDefinition, entity -> {});
    types
        .getStudentInterestsType()
        .addProjectDefinitionCategory(requirementDefinition, entity -> {});

    int assignmentCount = 0;
    for (Assignment assignment : assignments) {
      ++assignmentCount;
      int projectDefinitionCount = 0;
      for (ProjectDefinition projectDefinition :
          new ProjectDefinition[] {ksDefinition, xqDefinition, requirementDefinition}) {
        ++projectDefinitionCount;
        var apd =
            db.getAssignmentProjectDefinitionRepository()
                .upsert(assignment, projectDefinition, entity -> {});
        if ((assignmentCount % 3) == (projectDefinitionCount % 3)) {
          db.getAssignmentProjectDefinitionRepository().save(apd.setSelected(Instant.now()));
        }
      }
    }

    log.atInfo().log("Done creating Ikigai Diagram descriptions");
  }

  private void processCommands() throws IOException, SQLException {
    if (!createDistrict.isEmpty()) {
      log.atInfo().log("Creating district: {}", createDistrict);
      District district = createDistrict();

      // For now, just create one of each school.
      Arrays.stream(DaVinciSchoolsByNickname.values())
          .forEach(dvs -> dvs.createSchool(db, district));
    }

    if (!importTeachers.isEmpty()) {
      log.atInfo().log("Importing teachers: {}", importTeachers);
      importTeachers();
    }
    if (!importStudents.isEmpty()) {
      log.atInfo().log("Importing students: {}", importStudents);
      importStudents();
    }
    if (!importXqEks.isEmpty()) {
      log.atInfo().log("Importing XQ EKS: {}", importXqEks);
      importXqEks();
    }
    if (!importMotivations.isEmpty()) {
      log.atInfo().log("Importing Motivations: {}", importMotivations);
      importMotivations();
    }
    if (!createAdmins.isEmpty()) {
      log.atInfo().log("Creating admin: {}", createAdmins);
      createAdmins();
    }
    if (!Objects.equals(loadSchema, "false")) {
      log.atInfo().log("Loading schema");
      DatabaseManagement.loadSchema(dataSource);
    }
    if (!Objects.equals(loadTestData, "false")) {
      log.atInfo().log("Loading test data");
      new TestData(dataSource, db, entityManager).addTestData();
    } else {
      checkArgument(!createAdmins.isEmpty(), "--createAdmin required.");
      UserX userX = db.getUserXRepository().findByEmailAddress(createAdmins.get(0)).orElseThrow();
      addIkigaiDiagramDescriptions(db, userX, db.getAssignmentRepository().findAll());
    }
    if (!resetPasswords.isEmpty()) {
      log.atInfo().log("Resetting passwords: {}", resetPasswords);
      resetPasswords();
    }
  }

  public static void main(String[] argsArray) throws IOException {
    try {
      ApplicationContext context = SpringApplication.run(AdminXUtils.class, argsArray);
      context.getBean(AdminXUtils.class).processCommands();
      System.exit(0);
    } catch (Exception e) {
      log.atError().withThrowable(e).log("Failed to execute commands.");
    }
  }
}
