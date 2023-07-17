package org.davincischools.leo.database.test;

import com.google.common.collect.ImmutableList;
import java.time.Instant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.admin.AdminUtils;
import org.davincischools.leo.database.admin.AdminUtils.DaVinciSchoolsByNickname;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.Motivation;
import org.davincischools.leo.database.daos.ProjectDefinition;
import org.davincischools.leo.database.daos.ProjectDefinitionCategoryType;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.UserUtils;
import org.davincischools.leo.database.utils.repos.KnowledgeAndSkillRepository.Type;
import org.davincischools.leo.database.utils.repos.ProjectDefinitionCategoryTypeRepository.ValueType;
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
    public void onApplicationEvent(ContextRefreshedEvent event) {
      testData.addTestData();
      logger.atInfo().log("Added test data to the test database.");
    }
  }

  private final Database db;

  private District district;
  private UserX teacher;
  private UserX student;
  private UserX admin;

  private ClassX chemistryClass;
  private KnowledgeAndSkill chemistryPeriodicTableEks, chemistryValenceElectronsEks;
  private Assignment chemistryPeriodicTableAssignment, chemistryValenceElectronsAssignment;

  private ClassX programmingClass;
  private KnowledgeAndSkill programmingSortEks, programmingContainerEks;
  private Assignment programmingSortAssignment, programmingContainerAssignment;

  private ClassX danceClass;

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

  public UserX getAdmin() {
    return admin;
  }

  public ClassX getChemistryClass() {
    return chemistryClass;
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

  public ClassX getProgrammingClass() {
    return programmingClass;
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

  public ClassX getDanceClass() {
    return danceClass;
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
    for (var school : AdminUtils.DaVinciSchoolsByNickname.values()) {
      db.getSchoolRepository()
          .upsert(
              district, school.name(), school.getName(), s -> s.setAddress(school.getAddress()));
    }
    School school =
        db.getSchoolRepository()
            .findByNickname(district.getId(), DaVinciSchoolsByNickname.DVS.name())
            .orElseThrow();

    // Create XQ Competencies.
    // These are currently not unique per call.
    for (var xqCompetency : AdminUtils.XqCategoriesByNickname.values()) {
      db.getKnowledgeAndSkillRepository()
          .upsert(
              xqCompetency.getName() + ": " + xqCompetency.getExampleName(),
              Type.XQ_COMPETENCY,
              k -> k.setShortDescr(xqCompetency.getExampleDescription()));
    }

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
    admin =
        db.getUserXRepository()
            .upsert(
                district,
                email,
                userX ->
                    UserUtils.setPassword(
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
    db.getTeacherSchoolRepository().upsert(admin.getTeacher(), school);
    db.getStudentSchoolRepository().upsert(admin.getStudent(), school);

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
                    UserUtils.setPassword(
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
                    UserUtils.setPassword(
                        db.getStudentRepository()
                            .upsert(
                                userX.setFirstName("Student").setLastName("Project Leo"),
                                student -> student.setDistrictStudentId(1234).setGrade(9)),
                        PASSWORD));
    db.getStudentSchoolRepository().upsert(student.getStudent(), school);

    // Create programming class.
    programmingClass =
        db.getClassXRepository().upsert(school, "Intro to Programming", classX -> {});
    db.getTeacherClassXRepository().upsert(admin.getTeacher(), programmingClass);
    db.getTeacherClassXRepository().upsert(teacher.getTeacher(), programmingClass);
    db.getStudentClassXRepository().upsert(admin.getStudent(), programmingClass);
    db.getStudentClassXRepository().upsert(student.getStudent(), programmingClass);

    programmingSortEks =
        db.getKnowledgeAndSkillRepository()
            .upsert(
                "Sort Algorithms",
                Type.EKS,
                eks -> eks.setShortDescr("I understand different sort algorithms."));
    programmingContainerEks =
        db.getKnowledgeAndSkillRepository()
            .upsert(
                "Containers",
                Type.EKS,
                eks -> eks.setShortDescr("I understand Lists, Sets, and Maps."));
    db.getClassXKnowledgeAndSkillRepository().upsert(programmingClass, programmingSortEks);
    db.getClassXKnowledgeAndSkillRepository().upsert(programmingClass, programmingContainerEks);

    programmingSortAssignment =
        db.getAssignmentRepository()
            .upsert(programmingClass, "Understand Sort Algorithms", a -> {});
    db.getAssignmentKnowledgeAndSkillRepository()
        .upsert(programmingSortAssignment, programmingSortEks);

    programmingContainerAssignment =
        db.getAssignmentRepository().upsert(programmingClass, "Understand Containers", a -> {});
    db.getAssignmentKnowledgeAndSkillRepository()
        .upsert(programmingContainerAssignment, programmingContainerEks);

    // Create chemistry class.
    chemistryClass = db.getClassXRepository().upsert(school, "Intro to Chemistry", classX -> {});
    db.getTeacherClassXRepository().upsert(admin.getTeacher(), chemistryClass);
    db.getTeacherClassXRepository().upsert(teacher.getTeacher(), chemistryClass);
    db.getStudentClassXRepository().upsert(admin.getStudent(), chemistryClass);
    db.getStudentClassXRepository().upsert(student.getStudent(), chemistryClass);

    chemistryPeriodicTableEks =
        db.getKnowledgeAndSkillRepository()
            .upsert(
                "Periodic Table",
                Type.EKS,
                ks -> ks.setShortDescr("I know how to read a periodic table."));
    chemistryValenceElectronsEks =
        db.getKnowledgeAndSkillRepository()
            .upsert(
                "Valence Electrons",
                Type.EKS,
                ks ->
                    ks.setShortDescr(
                        "I can determine the number of valence electrons for each element."));
    db.getClassXKnowledgeAndSkillRepository().upsert(chemistryClass, chemistryPeriodicTableEks);
    db.getClassXKnowledgeAndSkillRepository().upsert(chemistryClass, chemistryValenceElectronsEks);

    chemistryPeriodicTableAssignment =
        db.getAssignmentRepository().upsert(chemistryClass, "Reading the Periodic Table", a -> {});
    db.getAssignmentKnowledgeAndSkillRepository()
        .upsert(chemistryPeriodicTableAssignment, chemistryPeriodicTableEks);

    chemistryValenceElectronsAssignment =
        db.getAssignmentRepository()
            .upsert(chemistryClass, "Understand Valence Electrons", a -> {});
    db.getAssignmentKnowledgeAndSkillRepository()
        .upsert(chemistryValenceElectronsAssignment, chemistryValenceElectronsEks);

    // Create an empty class.
    danceClass = db.getClassXRepository().upsert(school, "Dance", classX -> {});
    db.getTeacherClassXRepository().upsert(admin.getTeacher(), danceClass);
    db.getTeacherClassXRepository().upsert(teacher.getTeacher(), danceClass);
    db.getStudentClassXRepository().upsert(admin.getStudent(), danceClass);
    db.getStudentClassXRepository().upsert(student.getStudent(), danceClass);

    // Create project definitions.
    {
      ProjectDefinitionCategoryType careerInterestsType =
          db.getProjectDefinitionCategoryTypeRepository()
              .upsert(
                  "Career Interests",
                  type ->
                      type.setShortDescr("Career interests free text.")
                          .setHint("Click to add careers.")
                          .setInputDescr("Enter career interests:")
                          .setInputPlaceholder("Career Interest")
                          .setQueryPrefix("You are passionate about a career in")
                          .setValueType(ValueType.FREE_TEXT.name()));

      ProjectDefinitionCategoryType motivationType =
          db.getProjectDefinitionCategoryTypeRepository()
              .upsert(
                  "Motivations",
                  type ->
                      type.setShortDescr("Motivation selections.")
                          .setHint("Click to add motivations.")
                          .setInputDescr("Select motivations:")
                          .setInputPlaceholder("Select a Motivation")
                          .setQueryPrefix("You are motivated by")
                          .setValueType(ValueType.MOTIVATION.name()));

      ProjectDefinitionCategoryType ksType =
          db.getProjectDefinitionCategoryTypeRepository()
              .upsert(
                  "Knowledge and Skills",
                  type ->
                      type.setShortDescr("Knowledge and skill selections.")
                          .setHint("Click to add desired knowledge and skills.")
                          .setInputDescr("Select knowledge and skills:")
                          .setInputPlaceholder("Select a Knowledge and Skill")
                          .setQueryPrefix("You want to improve your ability to")
                          .setValueType(ValueType.EKS.name()));

      ProjectDefinitionCategoryType xqType =
          db.getProjectDefinitionCategoryTypeRepository()
              .upsert(
                  "XQ Competencies",
                  type ->
                      type.setShortDescr("XQ competency selections.")
                          .setHint("Click to add desired XQ competency.")
                          .setInputDescr("Select XQ Competency:")
                          .setInputPlaceholder("Select a XQ Competency")
                          .setQueryPrefix("You want to improve your ability to")
                          .setValueType(ValueType.XQ_COMPETENCY.name()));

      ProjectDefinitionCategoryType studentInterestsType =
          db.getProjectDefinitionCategoryTypeRepository()
              .upsert(
                  "Student Interests",
                  type ->
                      type.setShortDescr("Student interest free text.")
                          .setHint("Click to add student interests.")
                          .setInputDescr("Enter student interests:")
                          .setInputPlaceholder("Student Interest")
                          .setQueryPrefix("You are passionate about")
                          .setValueType(ValueType.FREE_TEXT.name()));

      ProjectDefinition xqProjectDefinition =
          db.getProjectDefinitionRepository()
              .upsert("Definition with XQ Competencies", admin, def -> {});
      db.getProjectDefinitionCategoryRepository()
          .upsert(
              xqProjectDefinition,
              careerInterestsType,
              entity -> entity.setPosition(1f).setMaxNumValues(4));
      db.getProjectDefinitionCategoryRepository()
          .upsert(
              xqProjectDefinition,
              motivationType,
              entity -> entity.setPosition(2f).setMaxNumValues(4));
      db.getProjectDefinitionCategoryRepository()
          .upsert(xqProjectDefinition, xqType, entity -> entity.setPosition(3f).setMaxNumValues(4));
      db.getProjectDefinitionCategoryRepository()
          .upsert(
              xqProjectDefinition,
              studentInterestsType,
              entity -> entity.setPosition(4f).setMaxNumValues(4));

      ProjectDefinition eksProjectDefinition =
          db.getProjectDefinitionRepository()
              .upsert("Definition with Knowledge and Skills", admin, def -> {});
      db.getProjectDefinitionCategoryRepository()
          .upsert(
              eksProjectDefinition,
              careerInterestsType,
              entity -> entity.setPosition(1f).setMaxNumValues(4));
      db.getProjectDefinitionCategoryRepository()
          .upsert(
              eksProjectDefinition,
              motivationType,
              entity -> entity.setPosition(2f).setMaxNumValues(4));
      db.getProjectDefinitionCategoryRepository()
          .upsert(
              eksProjectDefinition, xqType, entity -> entity.setPosition(3f).setMaxNumValues(4));
      db.getProjectDefinitionCategoryRepository()
          .upsert(
              eksProjectDefinition,
              studentInterestsType,
              entity -> entity.setPosition(4f).setMaxNumValues(4));

      // Assign project definitions to assignments.
      for (Assignment assignment :
          ImmutableList.of(programmingSortAssignment, chemistryPeriodicTableAssignment)) {
        db.getAssignmentProjectDefinitionRepository()
            .upsert(assignment, xqProjectDefinition, apd -> apd.setSelected(Instant.now()));
      }
      for (Assignment assignment :
          ImmutableList.of(programmingContainerAssignment, chemistryValenceElectronsAssignment)) {
        db.getAssignmentProjectDefinitionRepository()
            .upsert(assignment, eksProjectDefinition, apd -> apd.setSelected(Instant.now()));
      }
    }

    // Create old project definitions.
    String projectDefinitionName = "Generic Template";
    db.getProjectDefinitionRepository()
        .findByName(projectDefinitionName)
        .ifPresent(
            pd ->
                db.getProjectDefinitionRepository()
                    .save(pd.setName(pd.getName() + "-old." + pd.getId()).setTemplate(false)));
    ProjectDefinition projectDefinition =
        db.getProjectDefinitionRepository()
            .upsert(projectDefinitionName, admin, pd -> pd.setTemplate(true));

    db.getProjectInputCategoryRepository()
        .upsert(
            "Career Interests",
            projectDefinition,
            pic ->
                pic.setPosition(0f)
                    .setShortDescr("Career interests free text.")
                    .setHint("Click to add careers.")
                    .setInputDescr("Enter career interests:")
                    .setInputPlaceholder("Career Interest")
                    .setQueryPrefix("You are passionate about a career in")
                    .setValueType(ValueType.FREE_TEXT.name())
                    .setMaxNumValues(4));

    db.getProjectInputCategoryRepository()
        .upsert(
            "Motivations",
            projectDefinition,
            pic ->
                pic.setPosition(1f)
                    .setShortDescr("Motivation selections.")
                    .setHint("Click to add motivations.")
                    .setInputDescr("Select motivations:")
                    .setInputPlaceholder("Select a Motivation")
                    .setQueryPrefix("You are motivated by")
                    .setValueType(ValueType.MOTIVATION.name())
                    .setMaxNumValues(4));

    db.getProjectInputCategoryRepository()
        .upsert(
            "Knowledge and Skills",
            projectDefinition,
            pic ->
                pic.setPosition(2f)
                    .setShortDescr("Knowledge and skill selections.")
                    .setHint("Click to add desired knowledge and skills.")
                    .setInputDescr("Select knowledge and skills:")
                    .setInputPlaceholder("Select a Knowledge and Skill")
                    .setQueryPrefix("You want to improve your ability to")
                    .setValueType(ValueType.EKS.name())
                    .setMaxNumValues(4));

    db.getProjectInputCategoryRepository()
        .upsert(
            "Student Interests",
            projectDefinition,
            pic ->
                pic.setPosition(3f)
                    .setShortDescr("Student interest free text.")
                    .setHint("Click to add student interests.")
                    .setInputDescr("Enter student interests:")
                    .setInputPlaceholder("Student Interest")
                    .setQueryPrefix("You are passionate about")
                    .setValueType(ValueType.FREE_TEXT.name())
                    .setMaxNumValues(4));

    // Assign project definitions to assignments.
    for (Assignment assignment :
        ImmutableList.of(
            programmingSortAssignment, programmingContainerAssignment,
            chemistryPeriodicTableAssignment, chemistryValenceElectronsAssignment)) {
      db.getAssignmentProjectDefinitionRepository()
          .upsert(assignment, projectDefinition, apd -> apd.setSelected(Instant.now()));
    }
  }
}
