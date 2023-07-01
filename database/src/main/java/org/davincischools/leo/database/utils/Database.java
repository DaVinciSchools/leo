package org.davincischools.leo.database.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Strings;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import org.davincischools.leo.database.daos.AdminX;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.Motivation;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.repos.AdminXRepository;
import org.davincischools.leo.database.utils.repos.AssignmentKnowledgeAndSkillRepository;
import org.davincischools.leo.database.utils.repos.AssignmentProjectDefinitionRepository;
import org.davincischools.leo.database.utils.repos.AssignmentRepository;
import org.davincischools.leo.database.utils.repos.ClassXKnowledgeAndSkillRepository;
import org.davincischools.leo.database.utils.repos.ClassXRepository;
import org.davincischools.leo.database.utils.repos.DistrictRepository;
import org.davincischools.leo.database.utils.repos.FileXRepository;
import org.davincischools.leo.database.utils.repos.InterestRepository;
import org.davincischools.leo.database.utils.repos.KnowledgeAndSkillRepository;
import org.davincischools.leo.database.utils.repos.KnowledgeAndSkillRepository.Type;
import org.davincischools.leo.database.utils.repos.LogReferenceRepository;
import org.davincischools.leo.database.utils.repos.LogRepository;
import org.davincischools.leo.database.utils.repos.MotivationRepository;
import org.davincischools.leo.database.utils.repos.PortfolioRepository;
import org.davincischools.leo.database.utils.repos.ProjectDefinitionRepository;
import org.davincischools.leo.database.utils.repos.ProjectImageRepository;
import org.davincischools.leo.database.utils.repos.ProjectInputCategoryRepository;
import org.davincischools.leo.database.utils.repos.ProjectInputRepository;
import org.davincischools.leo.database.utils.repos.ProjectInputValueRepository;
import org.davincischools.leo.database.utils.repos.ProjectMilestoneRepository;
import org.davincischools.leo.database.utils.repos.ProjectMilestoneStepRepository;
import org.davincischools.leo.database.utils.repos.ProjectPostCommentRepository;
import org.davincischools.leo.database.utils.repos.ProjectPostRepository;
import org.davincischools.leo.database.utils.repos.ProjectRepository;
import org.davincischools.leo.database.utils.repos.SchoolRepository;
import org.davincischools.leo.database.utils.repos.StudentClassXRepository;
import org.davincischools.leo.database.utils.repos.StudentRepository;
import org.davincischools.leo.database.utils.repos.StudentSchoolRepository;
import org.davincischools.leo.database.utils.repos.TeacherClassXRepository;
import org.davincischools.leo.database.utils.repos.TeacherRepository;
import org.davincischools.leo.database.utils.repos.TeacherSchoolRepository;
import org.davincischools.leo.database.utils.repos.UserXRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

@Component
@EnableJpaRepositories(basePackageClasses = {Database.class})
@EntityScan(basePackageClasses = {UserX.class})
public class Database {
  public static final String INVALID_ENCODED_PASSWORD = "INVALID ENCODED PASSWORD";
  public static final int USER_MAX_EMAIL_ADDRESS_LENGTH =
      EntityUtils.getColumn(UserX.class, UserX.COLUMN_EMAILADDRESS_NAME).length();
  public static final int USER_MAX_FIRST_NAME_LENGTH =
      EntityUtils.getColumn(UserX.class, UserX.COLUMN_FIRSTNAME_NAME).length();
  public static final int USER_MAX_LAST_NAME_LENGTH =
      EntityUtils.getColumn(UserX.class, UserX.COLUMN_LASTNAME_NAME).length();
  public static final int USER_MIN_PASSWORD_LENGTH = 8;

  @Autowired private AdminXRepository adminXRepository;
  @Autowired private AssignmentKnowledgeAndSkillRepository assignmentKnowledgeAndSkillRepository;
  @Autowired private AssignmentProjectDefinitionRepository assignmentProjectDefinitionRepository;
  @Autowired private AssignmentRepository assignmentRepository;
  @Autowired private ClassXKnowledgeAndSkillRepository classXKnowledgeAndSkillRepository;
  @Autowired private ClassXRepository classXRepository;
  @Autowired private DistrictRepository districtRepository;
  @Autowired private FileXRepository fileXRepository;
  @Autowired private InterestRepository interestRepository;
  @Autowired private KnowledgeAndSkillRepository knowledgeAndSkillRepository;
  @Autowired private LogReferenceRepository logReferenceRepository;
  @Autowired private LogRepository logRepository;
  @Autowired private MotivationRepository motivationRepository;
  @Autowired private PortfolioRepository portfolioRepository;
  @Autowired private ProjectMilestoneRepository projectMilestoneRepository;
  @Autowired private ProjectMilestoneStepRepository projectMilestoneStepRepository;
  @Autowired private ProjectDefinitionRepository projectDefinitionRepository;
  @Autowired private ProjectImageRepository projectImageRepository;
  @Autowired private ProjectInputCategoryRepository projectInputCategoryRepository;
  @Autowired private ProjectInputRepository projectInputRepository;
  @Autowired private ProjectInputValueRepository projectInputValueRepository;
  @Autowired private ProjectPostCommentRepository projectPostCommentRepository;
  @Autowired private ProjectPostRepository projectPostRepository;
  @Autowired private ProjectRepository projectRepository;
  @Autowired private SchoolRepository schoolRepository;
  @Autowired private StudentClassXRepository studentClassXRepository;
  @Autowired private StudentRepository studentRepository;
  @Autowired private StudentSchoolRepository studentSchoolRepository;
  @Autowired private TeacherClassXRepository teacherClassXRepository;
  @Autowired private TeacherRepository teacherRepository;
  @Autowired private TeacherSchoolRepository teacherSchoolRepository;
  @Autowired private UserXRepository userXRepository;

  public AdminXRepository getAdminXRepository() {
    return adminXRepository;
  }

  public AssignmentKnowledgeAndSkillRepository getAssignmentKnowledgeAndSkillRepository() {
    return assignmentKnowledgeAndSkillRepository;
  }

  public AssignmentProjectDefinitionRepository getAssignmentProjectDefinitionRepository() {
    return assignmentProjectDefinitionRepository;
  }

  public AssignmentRepository getAssignmentRepository() {
    return assignmentRepository;
  }

  public ClassXRepository getClassXRepository() {
    return classXRepository;
  }

  public ClassXKnowledgeAndSkillRepository getClassXKnowledgeAndSkillRepository() {
    return classXKnowledgeAndSkillRepository;
  }

  public DistrictRepository getDistrictRepository() {
    return districtRepository;
  }

  public FileXRepository getImageRepository() {
    return fileXRepository;
  }

  public InterestRepository getInterestRepository() {
    return interestRepository;
  }

  public KnowledgeAndSkillRepository getKnowledgeAndSkillRepository() {
    return knowledgeAndSkillRepository;
  }

  public LogReferenceRepository getLogReferenceRepository() {
    return logReferenceRepository;
  }

  public LogRepository getLogRepository() {
    return logRepository;
  }

  public MotivationRepository getMotivationRepository() {
    return motivationRepository;
  }

  public PortfolioRepository getPortfolioRepository() {
    return portfolioRepository;
  }

  public ProjectMilestoneRepository getProjectMilestoneRepository() {
    return projectMilestoneRepository;
  }

  public ProjectMilestoneStepRepository getProjectMilestoneStepRepository() {
    return projectMilestoneStepRepository;
  }

  public ProjectDefinitionRepository getProjectDefinitionRepository() {
    return projectDefinitionRepository;
  }

  public ProjectImageRepository getProjectImageRepository() {
    return projectImageRepository;
  }

  public ProjectInputCategoryRepository getProjectInputCategoryRepository() {
    return projectInputCategoryRepository;
  }

  public ProjectInputRepository getProjectInputRepository() {
    return projectInputRepository;
  }

  public ProjectInputValueRepository getProjectInputValueRepository() {
    return projectInputValueRepository;
  }

  public ProjectPostCommentRepository getProjectPostCommentRepository() {
    return projectPostCommentRepository;
  }

  public ProjectPostRepository getProjectPostRepository() {
    return projectPostRepository;
  }

  public ProjectRepository getProjectRepository() {
    return projectRepository;
  }

  public SchoolRepository getSchoolRepository() {
    return schoolRepository;
  }

  public StudentClassXRepository getStudentClassXRepository() {
    return studentClassXRepository;
  }

  public StudentRepository getStudentRepository() {
    return studentRepository;
  }

  public StudentSchoolRepository getStudentSchoolRepository() {
    return studentSchoolRepository;
  }

  public TeacherClassXRepository getTeacherClassXRepository() {
    return teacherClassXRepository;
  }

  public TeacherRepository getTeacherRepository() {
    return teacherRepository;
  }

  public TeacherSchoolRepository getTeacherSchoolRepository() {
    return teacherSchoolRepository;
  }

  public UserXRepository getUserXRepository() {
    return userXRepository;
  }

  public District createDistrict(String name) {
    checkArgument(!Strings.isNullOrEmpty(name));

    return getDistrictRepository()
        .findByName(name)
        .orElseGet(
            () ->
                getDistrictRepository()
                    .saveAndFlush(new District().setCreationTime(Instant.now()).setName(name)));
  }

  public School createSchool(District district, String nickname) {
    checkNotNull(district);
    checkArgument(!Strings.isNullOrEmpty(nickname));

    return getSchoolRepository()
        .findByNickname(district.getId(), nickname)
        .orElseGet(
            () ->
                getSchoolRepository()
                    .saveAndFlush(
                        new School()
                            .setCreationTime(Instant.now())
                            .setDistrict(district)
                            .setNickname(nickname)
                            .setName(
                                switch (nickname) {
                                  case "DVC" -> "Da Vinci Communications High School";
                                  case "DVConnect" -> "Da Vinci Connect High School";
                                  case "DVD" -> "Da Vinci Design High School";
                                  case "DVFlex" -> "Da Vinci Flex High School";
                                  case "DVRISE" -> "Da Vinci Rise High School-Richstone";
                                  case "DVS" -> "Da Vinci Science High School";
                                  default -> throw new IllegalArgumentException(
                                      "Unrecognized school nickname: " + nickname);
                                })
                            .setAddress(
                                switch (nickname) {
                                  case "DVC",
                                      "DVD",
                                      "DVS" -> "201 N. Douglas St., El Segundo, CA 90245";
                                  case "DVConnect" -> "550 Continental Blvd., El Segundo, CA 90245";
                                  case "DVFlex" -> "Address TBD";
                                  case "DVRISE" -> "13634 Cordary Avenue, Hawthorne, CA 90250";
                                  default -> throw new IllegalArgumentException(
                                      "Unrecognized school nickname: " + nickname);
                                })));
  }

  public UserX createUserX(District district, String emailAddress, Consumer<UserX> modifier) {
    checkNotNull(district);
    checkArgument(!Strings.isNullOrEmpty(emailAddress));

    UserX userX =
        getUserXRepository()
            .findByEmailAddress(emailAddress)
            .orElseGet(
                () ->
                    new UserX()
                        .setCreationTime(Instant.now())
                        .setFirstName("First Name")
                        .setLastName("Last Name")
                        .setEncodedPassword(INVALID_ENCODED_PASSWORD))
            .setDistrict(district)
            .setEmailAddress(emailAddress);

    modifier.accept(userX);

    getUserXRepository().saveAndFlush(userX);
    return userX;
  }

  public void addAdminXPermission(UserX... userXs) {
    for (var userX : userXs) {
      if (userX.getAdminX() == null) {
        userX.setAdminX(
            getAdminXRepository().saveAndFlush(new AdminX().setCreationTime(Instant.now())));
        getUserXRepository().saveAndFlush(userX);
      }
    }
  }

  public void addTeacherPermission(UserX... teachers) {
    for (var teacher : teachers) {
      if (teacher.getTeacher() == null) {
        teacher.setTeacher(
            getTeacherRepository().saveAndFlush(new Teacher().setCreationTime(Instant.now())));
        getUserXRepository().saveAndFlush(teacher);
      }
    }
  }

  public void addStudentPermission(Consumer<UserX> modifier, UserX... students) {
    for (var student : students) {
      if (student.getStudent() == null) {
        student.setStudent(new Student().setCreationTime(Instant.now()));
      }

      modifier.accept(student);

      getStudentRepository().saveAndFlush(student.getStudent());
      getUserXRepository().saveAndFlush(student);
    }
  }

  public void addTeachersToSchool(School school, Teacher... teachers) {
    Arrays.asList(teachers)
        .forEach(teacher -> getTeacherSchoolRepository().saveTeacherSchool(teacher, school));
  }

  public void addStudentsToSchool(School school, Student... students) {
    Arrays.asList(students)
        .forEach(student -> getStudentSchoolRepository().saveStudentSchool(student, school));
  }

  public ClassX createClassX(School school, String name, Consumer<ClassX> modifier) {
    ClassX classX =
        getClassXRepository()
            .findByName(school.getId(), name)
            .orElseGet(
                () -> new ClassX().setCreationTime(Instant.now()).setSchool(school).setName(name));

    modifier.accept(classX);

    getClassXRepository().saveAndFlush(classX);

    return classX;
  }

  public void addTeachersToClassX(ClassX classX, Teacher... teachers) {
    Arrays.asList(teachers)
        .forEach(teacher -> getTeacherClassXRepository().saveTeacherClassX(teacher, classX));
  }

  public void addStudentsToClassX(ClassX classX, Student... students) {
    Arrays.asList(students)
        .forEach(
            student -> {
              getStudentClassXRepository().saveStudentClassX(student, classX);
            });
  }

  public KnowledgeAndSkill createKnowledgeAndSkill(
      ClassX classX, String name, String descr, Type type) {
    return getKnowledgeAndSkillRepository()
        .findByName(classX.getId(), name)
        .orElseGet(
            () ->
                getKnowledgeAndSkillRepository()
                    .saveAndFlush(
                        new KnowledgeAndSkill()
                            .setCreationTime(Instant.now())
                            .setClassX(classX)
                            .setName(name)
                            .setShortDescr(descr)
                            .setShortDescrQuill(QuillInitializer.toQuillDelta(descr))
                            .setType(type.name())));
  }

  public Assignment createAssignment(
      ClassX classX, String name, KnowledgeAndSkill... knowledgeAndSkills) {
    Assignment assignment =
        getAssignmentRepository()
            .findByName(classX.getId(), name)
            .orElseGet(
                () ->
                    getAssignmentRepository()
                        .saveAndFlush(
                            new Assignment()
                                .setCreationTime(Instant.now())
                                .setClassX(classX)
                                .setName(name)));

    Arrays.asList(knowledgeAndSkills)
        .forEach(
            knowledgeAndSkill -> {
              checkArgument(Objects.equals(knowledgeAndSkill.getClassX().getId(), classX.getId()));
              getAssignmentKnowledgeAndSkillRepository()
                  .saveAssignmentKnowledgeAndSkill(assignment, knowledgeAndSkill);
            });
    return assignment;
  }

  public Motivation createMotivation(String name, String descr) {
    return getMotivationRepository()
        .save(
            getMotivationRepository()
                .findByName(name)
                .orElseGet(() -> new Motivation().setCreationTime(Instant.now()))
                .setName(name)
                .setShortDescr(descr));
  }
}
