package org.davincischools.leo.database.test;

import java.time.Instant;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.admin_x.AdminXUtils;
import org.davincischools.leo.database.admin_x.AdminXUtils.DaVinciSchoolsByNickname;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.Interest;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.Motivation;
import org.davincischools.leo.database.daos.School;
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
    // Rename and upsert a new district.
    String districtName = "Project Leo School District";
    db.getDistrictRepository()
        .findByName(districtName)
        .ifPresent(
            d -> db.getDistrictRepository().save(d.setName(d.getName() + "-old." + d.getId())));
    district = db.getDistrictRepository().upsert(districtName);

    // Create schools.
    for (var school : AdminXUtils.DaVinciSchoolsByNickname.values()) {
      db.getSchoolRepository()
          .upsert(
              district, school.name(), school.getName(), s -> s.setAddress(school.getAddress()));
    }
    School school =
        db.getSchoolRepository()
            .findByNickname(district.getId(), DaVinciSchoolsByNickname.DVS.name())
            .orElseThrow();

    // Create motivations.
    // These are currently not unique per call.
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
    String email = "admin@projectleo.net";
    db.getUserXRepository()
        .findByEmailAddress(email)
        .ifPresent(
            u ->
                db.getUserXRepository()
                    .saveAndFlush(
                        u.setEmailAddress(
                            u.getEmailAddress().replace("@", "-old." + u.getId() + "@"))));
    adminX =
        db.getUserXRepository()
            .upsert(
                district,
                email,
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

    email = "teacher@projectleo.net";
    db.getUserXRepository()
        .findByEmailAddress(email)
        .ifPresent(
            u ->
                db.getUserXRepository()
                    .saveAndFlush(
                        u.setEmailAddress(
                            u.getEmailAddress().replace("@", "-old." + u.getId() + "@"))));
    teacher =
        db.getUserXRepository()
            .upsert(
                district,
                email,
                userX ->
                    UserXUtils.setPassword(
                        db.getTeacherRepository()
                            .upsert(userX.setFirstName("Teacher").setLastName("Project Leo")),
                        PASSWORD));
    db.getTeacherSchoolRepository().upsert(teacher.getTeacher(), school);

    email = "student@projectleo.net";
    db.getUserXRepository()
        .findByEmailAddress(email)
        .ifPresent(
            u ->
                db.getUserXRepository()
                    .saveAndFlush(
                        u.setEmailAddress(
                            u.getEmailAddress().replace("@", "-old." + u.getId() + "@"))));
    student =
        db.getUserXRepository()
            .upsert(
                district,
                email,
                userX ->
                    UserXUtils.setPassword(
                        db.getStudentRepository()
                            .upsert(
                                userX.setFirstName("Student").setLastName("Project Leo"),
                                student -> student.setDistrictStudentId(1234).setGrade(9)),
                        PASSWORD));
    db.getStudentSchoolRepository().upsert(student.getStudent(), school);

    email = "demo@projectleo.net";
    db.getUserXRepository()
        .findByEmailAddress(email)
        .ifPresent(
            u ->
                db.getUserXRepository()
                    .saveAndFlush(
                        u.setEmailAddress(
                            u.getEmailAddress().replace("@", "-old." + u.getId() + "@"))));
    demo =
        db.getUserXRepository()
            .upsert(
                /* district= */ null,
                email,
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
    // These are currently not unique per call.
    for (var xqCompetency : AdminXUtils.XqCategoriesByNickname.values()) {
      db.getKnowledgeAndSkillRepository()
          .upsert(
              xqCompetency.getExampleName(),
              Type.XQ_COMPETENCY,
              k ->
                  k.setShortDescr(xqCompetency.getExampleDescription())
                      .setGlobal(true)
                      .setCategory(xqCompetency.getCategory())
                      .setUserX(adminX));
    }

    // Create programming class.
    programmingClassX =
        db.getClassXRepository()
            .upsert(school, "Intro to Programming", classX -> classX.setNumber("CS 101"));
    db.getTeacherClassXRepository().upsert(teacher.getTeacher(), programmingClassX);
    db.getStudentClassXRepository().upsert(student.getStudent(), programmingClassX);

    programmingSortEks =
        db.getKnowledgeAndSkillRepository()
            .upsert(
                "Sort Algorithms",
                Type.EKS,
                eks ->
                    eks.setShortDescr("I understand different sort algorithms.")
                        .setGlobal(true)
                        .setUserX(adminX));
    programmingContainerEks =
        db.getKnowledgeAndSkillRepository()
            .upsert(
                "Containers",
                Type.EKS,
                eks ->
                    eks.setShortDescr("I understand Lists, Sets, and Maps.")
                        .setGlobal(true)
                        .setUserX(adminX));
    db.getClassXKnowledgeAndSkillRepository().upsert(programmingClassX, programmingSortEks);
    db.getClassXKnowledgeAndSkillRepository().upsert(programmingClassX, programmingContainerEks);

    programmingSortAssignment =
        db.getAssignmentRepository()
            .upsert(programmingClassX, "Understand Sort Algorithms", a -> {});
    db.getAssignmentKnowledgeAndSkillRepository()
        .upsert(programmingSortAssignment, programmingSortEks);

    programmingContainerAssignment =
        db.getAssignmentRepository().upsert(programmingClassX, "Understand Containers", a -> {});
    db.getAssignmentKnowledgeAndSkillRepository()
        .upsert(programmingContainerAssignment, programmingContainerEks);

    // Create chemistry class.
    chemistryClassX =
        db.getClassXRepository()
            .upsert(school, "Intro to Chemistry", classX -> classX.setNumber("CHEM 101"));
    db.getTeacherClassXRepository().upsert(teacher.getTeacher(), chemistryClassX);
    db.getStudentClassXRepository().upsert(student.getStudent(), chemistryClassX);

    chemistryPeriodicTableEks =
        db.getKnowledgeAndSkillRepository()
            .upsert(
                "Periodic Table",
                Type.EKS,
                ks ->
                    ks.setShortDescr("I know how to read a periodic table.")
                        .setGlobal(true)
                        .setUserX(adminX));
    chemistryValenceElectronsEks =
        db.getKnowledgeAndSkillRepository()
            .upsert(
                "Valence Electrons",
                Type.EKS,
                ks ->
                    ks.setShortDescr(
                            "I can determine the number of valence electrons for each element.")
                        .setGlobal(true)
                        .setUserX(adminX));
    db.getClassXKnowledgeAndSkillRepository().upsert(chemistryClassX, chemistryPeriodicTableEks);
    db.getClassXKnowledgeAndSkillRepository().upsert(chemistryClassX, chemistryValenceElectronsEks);

    chemistryPeriodicTableAssignment =
        db.getAssignmentRepository().upsert(chemistryClassX, "Reading the Periodic Table", a -> {});
    db.getAssignmentKnowledgeAndSkillRepository()
        .upsert(chemistryPeriodicTableAssignment, chemistryPeriodicTableEks);

    chemistryValenceElectronsAssignment =
        db.getAssignmentRepository()
            .upsert(chemistryClassX, "Understand Valence Electrons", a -> {});
    db.getAssignmentKnowledgeAndSkillRepository()
        .upsert(chemistryValenceElectronsAssignment, chemistryValenceElectronsEks);

    // Create an empty class.
    danceClassX =
        db.getClassXRepository().upsert(school, "Dance", classX -> classX.setNumber("DANCE 101"));
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
    AdminXUtils.addAdminXToDistrictSchoolsAndClassXs(db, adminX);
  }
}
