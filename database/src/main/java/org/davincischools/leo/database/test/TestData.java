package org.davincischools.leo.database.test;

import static org.davincischools.leo.database.admin_x.AdminXUtils.createAdminX;
import static org.davincischools.leo.database.admin_x.AdminXUtils.createStudent;
import static org.davincischools.leo.database.admin_x.AdminXUtils.createTeacher;
import static org.davincischools.leo.database.admin_x.AdminXUtils.createUserX;
import static org.davincischools.leo.database.utils.DaoUtils.deleteAllRecords;
import static org.davincischools.leo.database.utils.UserXUtils.setPassword;

import com.google.common.base.Strings;
import jakarta.persistence.EntityManager;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Arrays;
import javax.sql.DataSource;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.admin_x.AdminXUtils;
import org.davincischools.leo.database.admin_x.AdminXUtils.DaVinciSchoolsByNickname;
import org.davincischools.leo.database.daos.AdminX;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.AssignmentKnowledgeAndSkill;
import org.davincischools.leo.database.daos.AssignmentProjectDefinition;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.ClassXKnowledgeAndSkill;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.Interest;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.Motivation;
import org.davincischools.leo.database.daos.ProjectDefinition;
import org.davincischools.leo.database.daos.ProjectDefinitionCategory;
import org.davincischools.leo.database.daos.ProjectDefinitionCategoryType;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.StudentClassX;
import org.davincischools.leo.database.daos.StudentSchool;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.TeacherClassX;
import org.davincischools.leo.database.daos.TeacherSchool;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.KnowledgeAndSkillRepository.Type;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TestData {
  private static final Logger logger = LogManager.getLogger();

  public static final String PASSWORD = "password";

  @Component
  @Profile("!" + TestDatabase.USE_EXTERNAL_DATABASE_PROFILE)
  public static class LoadTestDataOnStartup implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LogManager.getLogger();

    @Autowired TestData testData;

    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
      testData.addTestData();
      logger.atInfo().log("Added test data to the test database.");
    }
  }

  private final DataSource dataSource;
  private final Database db;
  private final EntityManager entityManager;

  private District district;
  private School school;
  private UserX teacher;
  private UserX student;
  private UserX adminX;
  private UserX demo;

  private ClassX chemistryClassX;
  private KnowledgeAndSkill chemistryPeriodicTableEks, chemistryValenceElectronsEks;
  private Assignment chemistryPeriodicTableAssignment, chemistryValenceElectronsAssignment;

  private ClassX programmingClassX;
  private KnowledgeAndSkill programmingSortEks, programmingContainerEks;
  private Assignment programmingSortAssignment, programmingContainerAssignment;

  private ClassX danceClassX;

  private Motivation beCentralMotivation;
  private Motivation excelMotivation;
  private Motivation exploreMotivation;
  private Motivation advanceMotivation;
  private Motivation organizeMotivation;

  public TestData(
      @Autowired DataSource dataSource,
      @Autowired Database db,
      @Autowired EntityManager entityManager) {
    this.dataSource = dataSource;
    this.db = db;
    this.entityManager = entityManager;
  }

  /**
   * This creates new entities each time it is called. So, using the getter methods will return
   * different values for each test that can be used in isolation. The KnowledgeAndSkill table is
   * the only exception because it's not tied to a district or school yet.
   */
  public void addTestData() {
    try {
      if (!Strings.nullToEmpty(dataSource.getConnection().getMetaData().getUserName())
          .startsWith("test@")) {
        logger
            .atError()
            .log(
                "Not loading test data. It may only be loaded to a database using"
                    + " 'test' as the username. But, the username was '{}'.",
                dataSource.getConnection().getMetaData().getUserName());
        return;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    // Upsert a new district.
    deleteAllRecords(
        db.getDistrictRepository(),
        District::getDeleted,
        (d, instant) -> d.setDeleted(instant).setName("deleted-" + d.getId() + "-" + d.getName()));
    district = db.getDistrictRepository().upsert("Project Leo School District");

    // Create schools.
    deleteAllRecords(db.getSchoolRepository(), School::getDeleted, School::setDeleted);
    deleteAllRecords(
        db.getStudentSchoolRepository(), StudentSchool::getDeleted, StudentSchool::setDeleted);
    deleteAllRecords(
        db.getTeacherSchoolRepository(), TeacherSchool::getDeleted, TeacherSchool::setDeleted);
    for (var school : AdminXUtils.DaVinciSchoolsByNickname.values()) {
      db.getSchoolRepository()
          .save(
              new School()
                  .setCreationTime(Instant.now())
                  .setDistrict(district)
                  .setNickname(school.name())
                  .setName(school.getName())
                  .setAddress(school.getAddress()));
    }
    school =
        db.getSchoolRepository().findAll().stream()
            .filter(
                s ->
                    s.getDeleted() == null
                        && DaVinciSchoolsByNickname.DVS.name().equals(s.getNickname()))
            .findFirst()
            .orElseThrow();

    // Create motivations.
    deleteAllRecords(db.getMotivationRepository(), Motivation::getDeleted, Motivation::setDeleted);
    beCentralMotivation =
        db.getMotivationRepository()
            .save(
                new Motivation()
                    .setCreationTime(Instant.now())
                    .setName("Be Central")
                    .setShortDescr(
                        "be a key person who holds things together and gives them meaning and/or"
                            + " direction"));
    excelMotivation =
        db.getMotivationRepository()
            .save(
                new Motivation()
                    .setCreationTime(Instant.now())
                    .setName("Excel")
                    .setShortDescr(
                        "give your absolute best as you exceed performance and expectation"));
    exploreMotivation =
        db.getMotivationRepository()
            .save(
                new Motivation()
                    .setCreationTime(Instant.now())
                    .setName("Explore")
                    .setShortDescr(
                        "press beyond the existing limits of your knowledge and experience to"
                            + " discover what is unknown to you"));
    advanceMotivation =
        db.getMotivationRepository()
            .save(
                new Motivation()
                    .setCreationTime(Instant.now())
                    .setName("Advance")
                    .setShortDescr("make progress as you accomplish a series of goals"));
    organizeMotivation =
        db.getMotivationRepository()
            .save(
                new Motivation()
                    .setCreationTime(Instant.now())
                    .setName("Organize")
                    .setShortDescr("set up a smooth-running operation"));

    // Create users.
    deleteAllRecords(
        db.getUserXRepository(),
        UserX::getDeleted,
        (userX, instant) ->
            userX
                .setDeleted(instant)
                .setEmailAddress("deleted-" + userX.getId() + "_" + userX.getEmailAddress()));
    deleteAllRecords(db.getAdminXRepository(), AdminX::getDeleted, AdminX::setDeleted);
    deleteAllRecords(db.getTeacherRepository(), Teacher::getDeleted, Teacher::setDeleted);
    deleteAllRecords(db.getStudentRepository(), Student::getDeleted, Student::setDeleted);

    adminX =
        createUserX(
            db,
            entityManager,
            district,
            "admin@projectleo.net",
            userX -> {
              userX.setFirstName("Admin").setLastName("Project Leo");
              createAdminX(db, userX);
              createTeacher(db, userX);
              createStudent(db, userX, student -> student.setDistrictStudentId(1111).setGrade(12));
              setPassword(userX, PASSWORD);
            });

    teacher =
        createUserX(
            db,
            entityManager,
            district,
            "teacher@projectleo.net",
            userX -> {
              userX.setFirstName("Teacher").setLastName("Project Leo");
              createTeacher(db, userX);
              setPassword(userX, PASSWORD);
            });
    db.getTeacherSchoolRepository().upsert(teacher.getTeacher(), school);

    student =
        createUserX(
            db,
            entityManager,
            district,
            "student@projectleo.net",
            userX -> {
              userX.setFirstName("Student").setLastName("Project Leo");
              createStudent(db, userX, s -> s.setDistrictStudentId(1234).setGrade(9));
              setPassword(userX, PASSWORD);
            });
    db.getStudentSchoolRepository().upsert(student.getStudent(), school);

    demo =
        createUserX(
            db,
            entityManager,
            /* district= */ null,
            "demo@projectleo.net",
            userX -> {
              userX.setFirstName("Demo").setLastName("Project Leo");
              setPassword(userX, PASSWORD);
              userX.setInterest(
                  db.getInterestRepository()
                      .save(
                          new Interest()
                              .setCreationTime(Instant.now())
                              .setFirstName(userX.getFirstName())
                              .setLastName(userX.getLastName())
                              .setEmailAddress(userX.getEmailAddress())
                              .setProfession("")
                              .setReasonForInterest("")));
            });

    // Create XQ Competencies.
    deleteAllRecords(
        db.getKnowledgeAndSkillRepository(),
        KnowledgeAndSkill::getDeleted,
        KnowledgeAndSkill::setDeleted);
    for (var xqCompetency : AdminXUtils.XqCategoriesByNickname.values()) {
      db.getKnowledgeAndSkillRepository()
          .save(
              new KnowledgeAndSkill()
                  .setCreationTime(Instant.now())
                  .setName(xqCompetency.getExampleName())
                  .setType(Type.XQ_COMPETENCY.name())
                  .setGlobal(true)
                  .setCategory(xqCompetency.getCategory())
                  .setUserX(adminX));
    }

    // Create programming class.
    deleteAllRecords(db.getClassXRepository(), ClassX::getDeleted, ClassX::setDeleted);
    deleteAllRecords(
        db.getTeacherClassXRepository(), TeacherClassX::getDeleted, TeacherClassX::setDeleted);
    deleteAllRecords(
        db.getStudentClassXRepository(), StudentClassX::getDeleted, StudentClassX::setDeleted);
    deleteAllRecords(
        db.getClassXKnowledgeAndSkillRepository(),
        ClassXKnowledgeAndSkill::getDeleted,
        ClassXKnowledgeAndSkill::setDeleted);
    programmingClassX =
        db.getClassXRepository()
            .upsert(
                new ClassX()
                    .setCreationTime(Instant.now())
                    .setSchool(school)
                    .setName("Intro to Programming")
                    .setNumber("CS 101"));
    db.getTeacherClassXRepository().upsert(teacher.getTeacher(), programmingClassX);
    db.getStudentClassXRepository().upsert(student.getStudent(), programmingClassX);

    programmingSortEks =
        db.getKnowledgeAndSkillRepository()
            .save(
                new KnowledgeAndSkill()
                    .setCreationTime(Instant.now())
                    .setName("Sort Algorithms")
                    .setType(Type.EKS.name())
                    .setShortDescr("I understand different sort algorithms.")
                    .setGlobal(true)
                    .setUserX(adminX));
    programmingContainerEks =
        db.getKnowledgeAndSkillRepository()
            .save(
                new KnowledgeAndSkill()
                    .setCreationTime(Instant.now())
                    .setName("Containers")
                    .setType(Type.EKS.name())
                    .setShortDescr("I understand Lists, Sets, and Maps.")
                    .setGlobal(true)
                    .setUserX(adminX));
    db.getClassXKnowledgeAndSkillRepository().upsert(programmingClassX, programmingSortEks);
    db.getClassXKnowledgeAndSkillRepository().upsert(programmingClassX, programmingContainerEks);

    deleteAllRecords(db.getAssignmentRepository(), Assignment::getDeleted, Assignment::setDeleted);
    deleteAllRecords(
        db.getAssignmentKnowledgeAndSkillRepository(),
        AssignmentKnowledgeAndSkill::getDeleted,
        AssignmentKnowledgeAndSkill::setDeleted);
    programmingSortAssignment =
        db.getAssignmentRepository()
            .upsert(
                db,
                new Assignment()
                    .setCreationTime(Instant.now())
                    .setClassX(programmingClassX)
                    .setName("Sort Algorithms"));
    db.getAssignmentKnowledgeAndSkillRepository()
        .upsert(programmingSortAssignment, programmingSortEks);

    programmingContainerAssignment =
        db.getAssignmentRepository()
            .upsert(
                db,
                new Assignment()
                    .setCreationTime(Instant.now())
                    .setClassX(programmingClassX)
                    .setName("Understand Containers"));
    db.getAssignmentKnowledgeAndSkillRepository()
        .upsert(programmingContainerAssignment, programmingContainerEks);

    // Create chemistry class.
    chemistryClassX =
        db.getClassXRepository()
            .upsert(
                new ClassX()
                    .setCreationTime(Instant.now())
                    .setSchool(school)
                    .setName("Intro to Chemistry")
                    .setNumber("CHEM 101"));
    db.getTeacherClassXRepository().upsert(teacher.getTeacher(), chemistryClassX);
    db.getStudentClassXRepository().upsert(student.getStudent(), chemistryClassX);

    chemistryPeriodicTableEks =
        db.getKnowledgeAndSkillRepository()
            .save(
                new KnowledgeAndSkill()
                    .setCreationTime(Instant.now())
                    .setName("Periodic Table")
                    .setType(Type.EKS.name())
                    .setShortDescr("I know how to read a periodic table.")
                    .setGlobal(true)
                    .setUserX(adminX));
    chemistryValenceElectronsEks =
        db.getKnowledgeAndSkillRepository()
            .save(
                new KnowledgeAndSkill()
                    .setCreationTime(Instant.now())
                    .setName("Valence Electrons")
                    .setType(Type.EKS.name())
                    .setShortDescr(
                        "I can determine the number of valence electrons for each element.")
                    .setGlobal(true)
                    .setUserX(adminX));
    db.getClassXKnowledgeAndSkillRepository().upsert(chemistryClassX, chemistryPeriodicTableEks);
    db.getClassXKnowledgeAndSkillRepository().upsert(chemistryClassX, chemistryValenceElectronsEks);

    chemistryPeriodicTableAssignment =
        db.getAssignmentRepository()
            .upsert(
                db,
                new Assignment()
                    .setCreationTime(Instant.now())
                    .setClassX(chemistryClassX)
                    .setName("Reading the Periodic Table"));
    db.getAssignmentKnowledgeAndSkillRepository()
        .upsert(chemistryPeriodicTableAssignment, chemistryPeriodicTableEks);

    chemistryValenceElectronsAssignment =
        db.getAssignmentRepository()
            .upsert(
                db,
                new Assignment()
                    .setCreationTime(Instant.now())
                    .setClassX(chemistryClassX)
                    .setName("Understand Valence Electrons"));
    db.getAssignmentKnowledgeAndSkillRepository()
        .upsert(chemistryValenceElectronsAssignment, chemistryValenceElectronsEks);

    // Create an empty class.
    danceClassX =
        db.getClassXRepository()
            .upsert(
                new ClassX()
                    .setCreationTime(Instant.now())
                    .setSchool(school)
                    .setName("Dance")
                    .setNumber("DANCE 101"));
    db.getTeacherClassXRepository().upsert(teacher.getTeacher(), danceClassX);
    db.getStudentClassXRepository().upsert(student.getStudent(), danceClassX);

    // Create project definitions.
    deleteAllRecords(
        db.getProjectDefinitionRepository(),
        ProjectDefinition::getDeleted,
        ProjectDefinition::setDeleted);
    deleteAllRecords(
        db.getProjectDefinitionCategoryRepository(),
        ProjectDefinitionCategory::getDeleted,
        ProjectDefinitionCategory::setDeleted);
    deleteAllRecords(
        db.getProjectDefinitionCategoryTypeRepository(),
        ProjectDefinitionCategoryType::getDeleted,
        ProjectDefinitionCategoryType::setDeleted);
    deleteAllRecords(
        db.getAssignmentProjectDefinitionRepository(),
        AssignmentProjectDefinition::getDeleted,
        AssignmentProjectDefinition::setDeleted);
    AdminXUtils.addIkigaiDiagramDescriptions(
        db,
        adminX,
        Arrays.asList(
            programmingSortAssignment,
            programmingContainerAssignment,
            chemistryPeriodicTableAssignment,
            chemistryValenceElectronsAssignment));

    // Add the admin to all schools and classes
    AdminXUtils.addAdminXToSchoolsAndClassXs(db, adminX);
  }
}
