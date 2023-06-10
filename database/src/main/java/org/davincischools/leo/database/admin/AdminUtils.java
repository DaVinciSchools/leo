package org.davincischools.leo.database.admin;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.ProjectDefinition;
import org.davincischools.leo.database.daos.ProjectInputCategory;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.TeacherSchool;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.UserUtils;
import org.davincischools.leo.database.utils.repos.KnowledgeAndSkillRepository.Type;
import org.davincischools.leo.database.utils.repos.ProjectInputCategoryRepository.ValueType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(scanBasePackageClasses = {Database.class})
public class AdminUtils {

  private enum XqCategory {
    ID("Interdisciplinary"),
    HUM("Humanities"),
    STEAM("Science, Technology, Engineering, Arts, and Math (STEM)"),
    SEL("Social, Emotional Learning (SS)");

    private final String name;

    XqCategory(String name) {
      this.name = name;
    }

    private String getName() {
      return name;
    }
  }

  private static final Joiner ERROR_JOINER = Joiner.on("\n - ");

  private static final Logger log = LogManager.getLogger();

  private record Error(String value, Exception e) {}

  @Autowired private Database db;

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

  private District createDistrict() {
    checkArgument(createDistrict != null, "--createDistrict required.");

    return db.createDistrict(createDistrict);
  }

  private static String createPassword() {
    return new RandomStringGenerator.Builder()
        .withinRange(new char[] {'a', 'z'}, new char[] {'0', '9'})
        .build()
        .generate(20);
  }

  private void createAdmins() {
    checkArgument(!createAdmins.isEmpty(), "--createAdmin required.");

    for (String createAdmin : createAdmins) {
      String password = createPassword();
      District district = createDistrict();
      AtomicBoolean passwordUpdated = new AtomicBoolean(false);
      UserX admin =
          db.createUserX(
              district,
              createAdmin,
              userX -> {
                if (userX.getEncodedPassword().equals(Database.INVALID_ENCODED_PASSWORD)) {
                  UserUtils.setPassword(userX, password);
                  passwordUpdated.set(true);
                }
              });
      db.addAdminXPermission(admin);

      // TODO: Later we don't want to do this. But, for development for now...
      db.addStudentPermission(userX -> userX.getStudent(), admin);
      db.addTeacherPermission(admin);
      for (School school : db.getSchoolRepository().findAll()) {
        db.addTeachersToSchool(school, admin.getTeacher());
        db.addStudentsToSchool(school, admin.getStudent());
      }
      for (ClassX classX : db.getClassXRepository().findAll()) {
        db.addTeachersToClassX(classX, admin.getTeacher());
        db.addStudentsToClassX(classX, admin.getStudent());
      }

      if (passwordUpdated.get()) {
        log.atWarn()
            .log(
                "IMPORTANT! Log in and change the temporary password:"
                    + " login: \"{}\", temporary password: \"{}\"",
                createAdmin,
                password);
      }
    }
  }

  private void resetPasswords() {
    checkArgument(!resetPasswords.isEmpty(), "--resetPassword required.");
    resetPasswords.stream()
        .parallel()
        .forEach(
            resetPassword -> {
              String password = createPassword();
              db.getUserXRepository()
                  .save(
                      UserUtils.setPassword(
                          db.getUserXRepository().findByEmailAddress(resetPassword).orElseThrow(),
                          password));
              log.atWarn()
                  .log(
                      "IMPORTANT! Log in and change the temporary password:"
                          + " login: \"{}\", temporary password: \"{}\"",
                      resetPassword,
                      password);
            });
  }

  private void importTeachers() throws IOException {
    checkArgument(importTeachers != null, "--importTeachers required.");

    District district = createDistrict();

    List<Error> errors = Collections.synchronizedList(new ArrayList<>());

    List<UserX> userXs =
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

                    UserX teacher =
                        db.createUserX(
                            district,
                            emailAddress,
                            userX -> userX.setFirstName(firstName).setLastName(lastName));
                    db.addTeacherPermission(teacher);
                    db.addStudentPermission(userX -> userX.getStudent(), teacher);

                    School school = db.createSchool(district, schoolNickname);
                    db.addTeachersToSchool(school, teacher.getTeacher());

                    if (cells.length >= 5) {
                      String className = cells[4];
                      checkArgument(!className.isEmpty(), "Class name required.");

                      ClassX classX = db.createClassX(school, className, c -> {});
                      db.addTeachersToClassX(classX, teacher.getTeacher());
                      db.addStudentsToClassX(classX, teacher.getStudent());

                      for (int eksIndex : new int[] {5, 7}) {
                        if (cells.length >= eksIndex + 2) {
                          String eksName = cells[eksIndex];
                          String eksDescr = cells[eksIndex + 1];

                          checkArgument(!eksName.isEmpty(), "EKS name required.");
                          checkArgument(!eksDescr.isEmpty(), "EKS description required.");

                          db.createAssignment(
                              classX,
                              eksName,
                              db.createKnowledgeAndSkill(classX, eksName, eksDescr, Type.EKS));
                        }
                      }
                    }

                    db.addTeachersToSchool(school, teacher.getTeacher());

                    log.atInfo().log("Imported: {}", line);

                    return teacher;
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
                  if (userX.getEncodedPassword().equals(Database.INVALID_ENCODED_PASSWORD)) {
                    UserUtils.setPassword(userX, userX.getEmailAddress());
                  }
                })
            .toList();

    db.getUserXRepository().saveAllAndFlush(userXs);

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
                        db.createUserX(
                            district,
                            emailAddress,
                            userX -> userX.setFirstName(firstName).setLastName(lastName));
                    db.addStudentPermission(
                        u -> u.getStudent().setStudentId(id).setGrade(grade), student);

                    School school =
                        db.getSchoolRepository()
                            .findByNickname(district.getId(), schoolNickname)
                            .orElseThrow();
                    db.addStudentsToSchool(
                        db.getSchoolRepository()
                            .findByNickname(district.getId(), schoolNickname)
                            .orElseThrow(),
                        student.getStudent());
                    db.getClassXRepository()
                        .findAllBySchool(school)
                        .forEach(classX -> db.addStudentsToClassX(classX, student.getStudent()));

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
                  if (userX.getEncodedPassword().equals(Database.INVALID_ENCODED_PASSWORD)) {
                    UserUtils.setPassword(
                        userX, userX.getLastName() + userX.getStudent().getStudentId());
                  }
                })
            .toList();

    db.getUserXRepository().saveAllAndFlush(students);

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
    checkArgument(importXqEks != null, "--importXqEks required.");

    District district = createDistrict();

    List<Error> errors = Collections.synchronizedList(new ArrayList<>());
    School school = db.createSchool(district, "DVRISE");
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
        XqCategory xqCategory = XqCategory.valueOf(cells[3]);

        checkArgument(!xqName.isEmpty(), "Title required.");
        checkArgument(!xqDescr.isEmpty(), "Description required.");
        checkArgument(!outcome.isEmpty(), "Outcome required.");

        ClassX classX = db.createClassX(school, xqCategory.getName(), c -> {});
        db.createAssignment(
            classX,
            xqName,
            db.createKnowledgeAndSkill(classX, xqName, xqDescr, Type.XQ_COMPETENCY));
        classXIds.add(classX.getId());

        log.atInfo().log("Imported: {}", line);
      } catch (Exception e) {
        log.atError().withThrowable(e).log("Error: {}", line);
        errors.add(new Error(line, e));
      }
    }
    try {
      for (TeacherSchool teacherSchool : db.getTeacherSchoolRepository().findAll()) {
        if (teacherSchool.getSchool().getId().equals(school.getId())) {
          UserX userX =
              db.getUserXRepository()
                  .findByTeacherId(teacherSchool.getTeacher().getId())
                  .orElseThrow();
          for (int classXId : classXIds) {
            ClassX classX = new ClassX().setId(classXId);
            db.addTeachersToClassX(classX, userX.getTeacher());
            db.addStudentsToClassX(classX, userX.getStudent());
          }
        }
      }
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

        db.createMotivation(title, descr);

        log.atInfo().log("Imported: {}", line);
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

  private void addIkigaiDiagramDescriptions() {
    log.atInfo().log("Creating Ikigai Diagram descriptions");

    String projectDefinitionName = "2023-06-01 DVD Trial";
    ProjectDefinition projectDefinition =
        db.getProjectDefinitionRepository()
            .save(
                db.getProjectDefinitionRepository()
                    .findByName(projectDefinitionName)
                    .orElseGet(() -> new ProjectDefinition().setCreationTime(Instant.now()))
                    .setName(projectDefinitionName)
                    .setTemplate(true));

    String careerInterestsTitle = "Career Interests";
    db.getProjectInputCategoryRepository()
        .save(
            db.getProjectInputCategoryRepository()
                .findByTitle(careerInterestsTitle)
                .orElseGet(() -> new ProjectInputCategory().setCreationTime(Instant.now()))
                .setShortDescr("Career interests free text")
                .setPosition(0)
                .setTitle(careerInterestsTitle)
                .setHint("Click to add careers.")
                .setInputDescr("Enter career interests:")
                .setInputPlaceholder("Career Interest")
                .setQueryPrefix("You are passionate about a career in")
                .setValueType(ValueType.FREE_TEXT.name())
                .setMaxNumValues(4)
                .setProjectDefinition(projectDefinition));

    String motivationsTitle = "Motivations";
    db.getProjectInputCategoryRepository()
        .save(
            db.getProjectInputCategoryRepository()
                .findByTitle(motivationsTitle)
                .orElseGet(() -> new ProjectInputCategory().setCreationTime(Instant.now()))
                .setShortDescr("Motivation selections")
                .setPosition(1)
                .setTitle(motivationsTitle)
                .setHint("Click to add motivations.")
                .setInputDescr("Select motivations:")
                .setInputPlaceholder("Select a Motivation")
                .setQueryPrefix("You are motivated by")
                .setValueType(ValueType.MOTIVATION.name())
                .setMaxNumValues(4)
                .setProjectDefinition(projectDefinition));

    String eksTitle = "Knowledge and Skills";
    db.getProjectInputCategoryRepository()
        .save(
            db.getProjectInputCategoryRepository()
                .findByTitle(eksTitle)
                .orElseGet(() -> new ProjectInputCategory().setCreationTime(Instant.now()))
                .setShortDescr("EKS selections")
                .setPosition(2)
                .setTitle(eksTitle)
                .setHint("Click to add desired knowledge and skills.")
                .setInputDescr("Select knowledge and skills:")
                .setInputPlaceholder("Select a Knowledge and Skill")
                .setQueryPrefix("You want to improve your ability to")
                .setValueType(ValueType.EKS.name())
                .setMaxNumValues(4)
                .setProjectDefinition(projectDefinition));

    String studentInterestsTitle = "Student Interests";
    db.getProjectInputCategoryRepository()
        .save(
            db.getProjectInputCategoryRepository()
                .findByTitle(studentInterestsTitle)
                .orElseGet(() -> new ProjectInputCategory().setCreationTime(Instant.now()))
                .setShortDescr("Student interest free text")
                .setPosition(3)
                .setTitle(studentInterestsTitle)
                .setHint("Click to add student interests.")
                .setInputDescr("Enter student interests:")
                .setInputPlaceholder("Student Interest")
                .setQueryPrefix("You are passionate about")
                .setValueType(ValueType.FREE_TEXT.name())
                .setMaxNumValues(4)
                .setProjectDefinition(projectDefinition));

    for (Assignment assignment : db.getAssignmentRepository().findAll()) {
      db.getAssignmentProjectDefinitionRepository()
          .save(
              db.getAssignmentProjectDefinitionRepository()
                  .newAssignmentProjectDefinition(assignment, projectDefinition)
                  .setCreationTime(Instant.now())
                  .setSelected(true));
    }

    log.atInfo().log("Done creating Ikigai Diagram descriptions");
  }

  private void processCommands() throws IOException {
    if (!createDistrict.isEmpty()) {
      log.atInfo().log("Creating district: {}", createDistrict);
      District district = createDistrict();

      // For now, just create one of each school.
      db.createSchool(district, "DVC");
      db.createSchool(district, "DVConnect");
      db.createSchool(district, "DVD");
      db.createSchool(district, "DVFlex");
      db.createSchool(district, "DVRISE");
      db.createSchool(district, "DVS");
    }

    if (!importTeachers.isEmpty()) {
      log.atInfo().log("Importing teachers: {}", importStudents);
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
    addIkigaiDiagramDescriptions();
    if (!createAdmins.isEmpty()) {
      log.atInfo().log("Creating admin: {}", createAdmins);
      createAdmins();
    }
    if (!resetPasswords.isEmpty()) {
      log.atInfo().log("Resetting passwords: {}", resetPasswords);
      resetPasswords();
    }
  }

  public static void main(String[] argsArray) throws IOException {
    try {
      ApplicationContext context = SpringApplication.run(AdminUtils.class, argsArray);
      context.getBean(AdminUtils.class).processCommands();
      System.exit(0);
    } catch (Exception e) {
      log.atError().withThrowable(e).log("Failed to execute commands.");
    }
  }
}
