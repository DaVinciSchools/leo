package org.davincischools.leo.database.test;

import static org.davincischools.leo.database.admin_x.AdminXUtils.createAdminX;
import static org.davincischools.leo.database.admin_x.AdminXUtils.createStudent;
import static org.davincischools.leo.database.admin_x.AdminXUtils.createTeacher;
import static org.davincischools.leo.database.admin_x.AdminXUtils.createUserX;
import static org.davincischools.leo.database.utils.DaoUtils.deleteAllRecords;

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
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.Interest;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.KnowledgeAndSkill.Type;
import org.davincischools.leo.database.daos.Motivation;
import org.davincischools.leo.database.daos.ProjectDefinition;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
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

  private final String ENCODED_PASSWORD;

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
    this.ENCODED_PASSWORD =
        PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(PASSWORD);
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

    // Delete district / schools / classes / assignments.
    deleteAllRecords(
        db.getDistrictRepository(),
        District::getDeleted,
        (d, instant) -> d.setDeleted(instant).setName("deleted-" + d.getId() + "-" + d.getName()),
        d -> !d.getName().startsWith("deleted-"));
    deleteAllRecords(
        db.getSchoolRepository(),
        School::getDeleted,
        (school, instant) ->
            school
                .setDeleted(instant)
                .setNickname("deleted-" + school.getId() + "_" + school.getNickname()),
        d -> !d.getNickname().startsWith("deleted-"));
    deleteAllRecords(db.getClassXRepository(), ClassX::getDeleted, ClassX::setDeleted);
    // Delete motivations / Knowledge and skills.
    deleteAllRecords(db.getMotivationRepository(), Motivation::getDeleted, Motivation::setDeleted);
    deleteAllRecords(
        db.getKnowledgeAndSkillRepository(),
        KnowledgeAndSkill::getDeleted,
        KnowledgeAndSkill::setDeleted);
    // Delete users.
    deleteAllRecords(
        db.getUserXRepository(),
        UserX::getDeleted,
        (userX, instant) ->
            userX
                .setDeleted(instant)
                .setEmailAddress("deleted-" + userX.getId() + "_" + userX.getEmailAddress()),
        d -> !d.getEmailAddress().startsWith("deleted-"));
    deleteAllRecords(db.getInterestRepository(), Interest::getDeleted, Interest::setDeleted);
    // Create project definitions / inputs.
    deleteAllRecords(
        db.getProjectDefinitionRepository(),
        ProjectDefinition::getDeleted,
        ProjectDefinition::setDeleted);
    // Don't delete these since they are not recreated.
    //  deleteAllRecords(
    //      db.getProjectDefinitionCategoryTypeRepository(),
    //      ProjectDefinitionCategoryType::getDeleted,
    //      ProjectDefinitionCategoryType::setDeleted);

    entityManager.clear();

    // Upsert a new district.
    district = db.getDistrictRepository().upsert("Project Leo School District");

    // Create schools.
    db.getSchoolRepository()
        .saveAll(
            Arrays.stream(DaVinciSchoolsByNickname.values())
                .map(
                    n ->
                        new School()
                            .setCreationTime(Instant.now())
                            .setDistrict(district)
                            .setNickname(n.name())
                            .setName(n.getName())
                            .setAddress(n.getAddress()))
                .toList());
    school =
        db.getSchoolRepository().findAll().stream()
            .filter(
                s ->
                    s.getDeleted() == null
                        && DaVinciSchoolsByNickname.DVS.name().equals(s.getNickname()))
            .findFirst()
            .orElseThrow();

    // Create motivations.
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
    adminX =
        createUserX(
            db,
            entityManager,
            district,
            "admin@projectleo.net",
            userX -> {
              userX.setFirstName("Admin").setLastName("Project Leo").setViewAiPrompts(true);
              createAdminX(db, userX);
              createTeacher(db, userX);
              createStudent(db, userX, student -> student.setDistrictStudentId(1111).setGrade(12));
              userX.setEncodedPassword(ENCODED_PASSWORD);
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
              userX.setEncodedPassword(ENCODED_PASSWORD);
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
              userX.setEncodedPassword(ENCODED_PASSWORD);
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
              userX.setEncodedPassword(ENCODED_PASSWORD);
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
    for (var xqCompetency : AdminXUtils.XqCategoriesByNickname.values()) {
      db.getKnowledgeAndSkillRepository()
          .save(
              new KnowledgeAndSkill()
                  .setCreationTime(Instant.now())
                  .setName(xqCompetency.getExampleName())
                  .setType(Type.XQ_COMPETENCY)
                  .setGlobal(true)
                  .setCategory(xqCompetency.getCategory())
                  .setUserX(adminX));
    }

    // Create programming class.
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
                    .setType(Type.EKS)
                    .setShortDescr("I understand different sort algorithms.")
                    .setGlobal(true)
                    .setUserX(adminX));
    programmingContainerEks =
        db.getKnowledgeAndSkillRepository()
            .save(
                new KnowledgeAndSkill()
                    .setCreationTime(Instant.now())
                    .setName("Containers")
                    .setType(Type.EKS)
                    .setShortDescr("I understand Lists, Sets, and Maps.")
                    .setGlobal(true)
                    .setUserX(adminX));
    db.getClassXKnowledgeAndSkillRepository().upsert(programmingClassX, programmingSortEks);
    db.getClassXKnowledgeAndSkillRepository().upsert(programmingClassX, programmingContainerEks);

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
                    .setType(Type.EKS)
                    .setShortDescr("I know how to read a periodic table.")
                    .setGlobal(true)
                    .setUserX(adminX));
    chemistryValenceElectronsEks =
        db.getKnowledgeAndSkillRepository()
            .save(
                new KnowledgeAndSkill()
                    .setCreationTime(Instant.now())
                    .setName("Valence Electrons")
                    .setType(Type.EKS)
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
