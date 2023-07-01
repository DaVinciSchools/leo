package org.davincischools.leo.database.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.admin.AdminUtils;
import org.davincischools.leo.database.admin.AdminUtils.DaVinciSchoolsByNickname;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.UserUtils;
import org.davincischools.leo.database.utils.repos.KnowledgeAndSkillRepository.Type;
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
  public class LoadTestDataOnStartup implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LogManager.getLogger();

    @Autowired TestData testData;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
      testData.addTestData();
      logger.atInfo().log("Added test data to the test database.");
    }
  }

  private final Database db;

  private UserX teacher;
  private UserX student;
  private UserX admin;

  private ClassX chemistryClass;
  private KnowledgeAndSkill chemistryPeriodicTableEks, chemistryValenceElectronsEks;
  private Assignment chemistryPeriodicTableAssignment, chemistryValenceElectronsAssignment;

  private ClassX programmingClass;
  private KnowledgeAndSkill programmingSortEks, programmingContainerEks;
  private Assignment programmingSortAssignment, programmingContainerAssignment;

  public TestData(@Autowired Database db) {
    this.db = db;
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

  public void addTestData() {
    District district = db.getDistrictRepository().upsert("Wiseburn Unified School District");

    for (var school : AdminUtils.DaVinciSchoolsByNickname.values()) {
      db.getSchoolRepository()
          .upsert(
              district, school.name(), school.getName(), s -> s.setAddress(school.getAddress()));
    }
    School school =
        db.getSchoolRepository()
            .findByNickname(district.getId(), DaVinciSchoolsByNickname.DVS.name())
            .orElseThrow();

    for (var xqCompetency : AdminUtils.XqCategoriesByNickname.values()) {
      db.getKnowledgeAndSkillRepository().upsert(xqCompetency.name(), Type.XQ_COMPETENCY, k -> {});
    }

    admin =
        db.getUserXRepository()
            .upsert(
                district,
                "admin@davincischools.org",
                userX ->
                    UserUtils.setPassword(
                        db.getAdminXRepository()
                            .upsert(userX.setFirstName("Admin").setLastName("Da Vinci")),
                        PASSWORD));

    teacher =
        db.getUserXRepository()
            .upsert(
                district,
                "teacher@davincischools.org",
                userX ->
                    UserUtils.setPassword(
                        db.getTeacherRepository()
                            .upsert(userX.setFirstName("Teacher").setLastName("Da Vinci")),
                        PASSWORD));
    db.getTeacherSchoolRepository().upsert(teacher.getTeacher(), school);

    student =
        db.getUserXRepository()
            .upsert(
                district,
                "student@student.davincischools.org",
                userX ->
                    UserUtils.setPassword(
                        db.getStudentRepository()
                            .upsert(
                                userX.setFirstName("Student").setLastName("Da Vinci"),
                                student -> student.setDistrictStudentId(1234).setGrade(9)),
                        PASSWORD));
    db.getStudentSchoolRepository().upsert(student.getStudent(), school);

    programmingClass =
        db.getClassXRepository().upsert(school, "Intro to Programming", classX -> {});
    db.getTeacherClassXRepository().upsert(teacher.getTeacher(), programmingClass);
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
            .upsert(programmingClass, "Understand sort algorithms.", a -> {});
    db.getAssignmentKnowledgeAndSkillRepository()
        .upsert(programmingSortAssignment, programmingSortEks);

    programmingContainerAssignment =
        db.getAssignmentRepository().upsert(programmingClass, "Understand containers.", a -> {});
    db.getAssignmentKnowledgeAndSkillRepository()
        .upsert(programmingContainerAssignment, programmingContainerEks);

    chemistryClass = db.getClassXRepository().upsert(school, "Intro to Chemistry", classX -> {});
    db.getTeacherClassXRepository().upsert(teacher.getTeacher(), chemistryClass);
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
  }
}
