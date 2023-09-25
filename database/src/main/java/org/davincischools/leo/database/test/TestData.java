package org.davincischools.leo.database.test;

import static org.davincischools.leo.database.utils.DaoUtils.deleteAllRecords;
import static org.davincischools.leo.database.utils.DaoUtils.updateAllRecords;

import java.time.Instant;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.admin_x.AdminXUtils;
import org.davincischools.leo.database.admin_x.AdminXUtils.DaVinciSchoolsByNickname;
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
import org.davincischools.leo.database.daos.StudentClassX;
import org.davincischools.leo.database.daos.TeacherClassX;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.UserXUtils;
import org.davincischools.leo.database.utils.repos.KnowledgeAndSkillRepository.Type;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class TestData {

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

  private final Database db;

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

  public TestData(@Autowired Database db) {
    this.db = db;
  }

  public District getDistrict() {
    return district;
  }

  public School getSchool() {
    return school;
  }

  public UserX getTeacher() {
    return teacher;
  }

  public UserX getStudent() {
    return student;
  }

  public UserX getAdminX() {
    return adminX;
  }

  public ClassX getChemistryClassX() {
    return chemistryClassX;
  }

  public KnowledgeAndSkill getChemistryPeriodicTableEks() {
    return chemistryPeriodicTableEks;
  }

  public KnowledgeAndSkill getChemistryValenceElectronsEks() {
    return chemistryValenceElectronsEks;
  }

  public Assignment getChemistryPeriodicTableAssignment() {
    return chemistryPeriodicTableAssignment;
  }

  public Assignment getChemistryValenceElectronsAssignment() {
    return chemistryValenceElectronsAssignment;
  }

  public ClassX getProgrammingClassX() {
    return programmingClassX;
  }

  public KnowledgeAndSkill getProgrammingSortEks() {
    return programmingSortEks;
  }

  public KnowledgeAndSkill getProgrammingContainerEks() {
    return programmingContainerEks;
  }

  public Assignment getProgrammingSortAssignment() {
    return programmingSortAssignment;
  }

  public Assignment getProgrammingContainerAssignment() {
    return programmingContainerAssignment;
  }

  public ClassX getDanceClassX() {
    return danceClassX;
  }

  public Motivation getBeCentralMotivation() {
    return beCentralMotivation;
  }

  public Motivation getExcelMotivation() {
    return excelMotivation;
  }

  public Motivation getExploreMotivation() {
    return exploreMotivation;
  }

  public Motivation getAdvanceMotivation() {
    return advanceMotivation;
  }

  public Motivation getOrganizeMotivation() {
    return organizeMotivation;
  }

  /**
   * This creates new entities each time it is called. So, using the getter methods will return
   * different values for each test that can be used in isolation. The KnowledgeAndSkill table is
   * the only exception because it's not tied to a district or school yet.
   */
  public void addTestData() {
    // Upsert a new district.
    updateAllRecords(
        db.getDistrictRepository(),
        d -> d.setDeleted(Instant.now()).setName("delted-" + d.getId() + "-" + d.getName()));
    district = db.getDistrictRepository().upsert("Project Leo School District");

    // Create schools.
    deleteAllRecords(db.getSchoolRepository(), School::setDeleted);
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
    deleteAllRecords(db.getMotivationRepository(), Motivation::setDeleted);
    beCentralMotivation =
        db.getMotivationRepository()
            .upsert(
                "Be Central",
                "be a key person who holds things together and gives them meaning and/or direction",
                m -> {});
    excelMotivation =
        db.getMotivationRepository()
            .upsert(
                "Excel",
                "give your absolute best as you exceed performance and expectation",
                m -> {});
    exploreMotivation =
        db.getMotivationRepository()
            .upsert(
                "Explore",
                "press beyond the existing limits of your knowledge and experience to discover what"
                    + " is unknown to you",
                m -> {});
    advanceMotivation =
        db.getMotivationRepository()
            .upsert("Advance", "make progress as you accomplish a series of goals", m -> {});
    organizeMotivation =
        db.getMotivationRepository()
            .upsert("Organize", "set up a smooth-running operation", m -> {});

    // Create users.
    deleteAllRecords(
        db.getUserXRepository(),
        (userX, instant) ->
            userX
                .setDeleted(instant)
                .setEmailAddress("deleted-" + userX.getId() + "_" + userX.getEmailAddress()));

    adminX =
        db.getUserXRepository()
            .upsert(
                district,
                "admin@projectleo.net",
                userX ->
                    UserXUtils.setPassword(
                        db.getAdminXRepository()
                            .upsert(
                                db.getTeacherRepository()
                                    .upsert(
                                        db.getStudentRepository()
                                            .upsert(
                                                userX
                                                    .setFirstName("Admin")
                                                    .setLastName("Project Leo"),
                                                student ->
                                                    student
                                                        .setDistrictStudentId(1111)
                                                        .setGrade(12)))),
                        PASSWORD));

    teacher =
        db.getUserXRepository()
            .upsert(
                district,
                "teacher@projectleo.net",
                userX ->
                    UserXUtils.setPassword(
                        db.getTeacherRepository()
                            .upsert(userX.setFirstName("Teacher").setLastName("Project Leo")),
                        PASSWORD));
    db.getTeacherSchoolRepository().upsert(teacher.getTeacher(), school);

    student =
        db.getUserXRepository()
            .upsert(
                district,
                "student@projectleo.net",
                userX ->
                    UserXUtils.setPassword(
                        db.getStudentRepository()
                            .upsert(
                                userX.setFirstName("Student").setLastName("Project Leo"),
                                student -> student.setDistrictStudentId(1234).setGrade(9)),
                        PASSWORD));
    db.getStudentSchoolRepository().upsert(student.getStudent(), school);

    demo =
        db.getUserXRepository()
            .upsert(
                /* district= */ null,
                "demo@projectleo.net",
                userX ->
                    UserXUtils.setPassword(
                            userX.setFirstName("Demo").setLastName("Project Leo"), PASSWORD)
                        .setInterest(
                            db.getInterestRepository()
                                .save(
                                    new Interest()
                                        .setCreationTime(Instant.now())
                                        .setFirstName(userX.getFirstName())
                                        .setLastName(userX.getLastName())
                                        .setEmailAddress(userX.getEmailAddress())
                                        .setProfession("")
                                        .setReasonForInterest(""))));

    // Create XQ Competencies.
    deleteAllRecords(db.getKnowledgeAndSkillRepository(), KnowledgeAndSkill::setDeleted);
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
    deleteAllRecords(db.getClassXRepository(), ClassX::setDeleted);
    deleteAllRecords(db.getTeacherClassXRepository(), TeacherClassX::setDeleted);
    deleteAllRecords(db.getStudentClassXRepository(), StudentClassX::setDeleted);
    deleteAllRecords(
        db.getClassXKnowledgeAndSkillRepository(), ClassXKnowledgeAndSkill::setDeleted);
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

    deleteAllRecords(db.getAssignmentRepository(), Assignment::setDeleted);
    deleteAllRecords(
        db.getAssignmentKnowledgeAndSkillRepository(), AssignmentKnowledgeAndSkill::setDeleted);
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
    deleteAllRecords(db.getProjectDefinitionRepository(), ProjectDefinition::setDeleted);
    deleteAllRecords(
        db.getProjectDefinitionCategoryRepository(), ProjectDefinitionCategory::setDeleted);
    deleteAllRecords(
        db.getProjectDefinitionCategoryTypeRepository(), ProjectDefinitionCategoryType::setDeleted);
    deleteAllRecords(
        db.getAssignmentProjectDefinitionRepository(), AssignmentProjectDefinition::setDeleted);
    AdminXUtils.addIkigaiDiagramDescriptions(
        db,
        adminX,
        Arrays.asList(
            programmingSortAssignment,
            programmingContainerAssignment,
            chemistryPeriodicTableAssignment,
            chemistryValenceElectronsAssignment));

    // Add the admin to all schools and classes
    AdminXUtils.addAdminXToDistrictSchoolsAndClassXs(db, adminX);
  }
}
