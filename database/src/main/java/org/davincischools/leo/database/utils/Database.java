package org.davincischools.leo.database.utils;

import lombok.Getter;
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
import org.davincischools.leo.database.utils.repos.LogReferenceRepository;
import org.davincischools.leo.database.utils.repos.LogRepository;
import org.davincischools.leo.database.utils.repos.MotivationRepository;
import org.davincischools.leo.database.utils.repos.PortfolioRepository;
import org.davincischools.leo.database.utils.repos.ProjectDefinitionCategoryRepository;
import org.davincischools.leo.database.utils.repos.ProjectDefinitionCategoryTypeRepository;
import org.davincischools.leo.database.utils.repos.ProjectDefinitionRepository;
import org.davincischools.leo.database.utils.repos.ProjectImageRepository;
import org.davincischools.leo.database.utils.repos.ProjectInputFulfillmentRepository;
import org.davincischools.leo.database.utils.repos.ProjectInputRepository;
import org.davincischools.leo.database.utils.repos.ProjectInputValueRepository;
import org.davincischools.leo.database.utils.repos.ProjectMilestoneRepository;
import org.davincischools.leo.database.utils.repos.ProjectMilestoneStepRepository;
import org.davincischools.leo.database.utils.repos.ProjectPostCommentRepository;
import org.davincischools.leo.database.utils.repos.ProjectPostRatingRepository;
import org.davincischools.leo.database.utils.repos.ProjectPostRepository;
import org.davincischools.leo.database.utils.repos.ProjectRepository;
import org.davincischools.leo.database.utils.repos.SchoolRepository;
import org.davincischools.leo.database.utils.repos.StudentClassXRepository;
import org.davincischools.leo.database.utils.repos.StudentRepository;
import org.davincischools.leo.database.utils.repos.StudentSchoolRepository;
import org.davincischools.leo.database.utils.repos.TagRepository;
import org.davincischools.leo.database.utils.repos.TeacherClassXRepository;
import org.davincischools.leo.database.utils.repos.TeacherRepository;
import org.davincischools.leo.database.utils.repos.TeacherSchoolRepository;
import org.davincischools.leo.database.utils.repos.UserXRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

@Component
@Getter
@EnableJpaRepositories(basePackageClasses = {UserXRepository.class})
@EntityScan(basePackageClasses = {UserX.class})
public class Database {

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
  @Autowired private ProjectDefinitionCategoryRepository projectDefinitionCategoryRepository;

  @Autowired
  private ProjectDefinitionCategoryTypeRepository projectDefinitionCategoryTypeRepository;

  @Autowired private ProjectDefinitionRepository projectDefinitionRepository;
  @Autowired private ProjectImageRepository projectImageRepository;
  @Autowired private ProjectInputFulfillmentRepository projectInputFulfillmentRepository;
  @Autowired private ProjectInputRepository projectInputRepository;
  @Autowired private ProjectInputValueRepository projectInputValueRepository;
  @Autowired private ProjectMilestoneRepository projectMilestoneRepository;
  @Autowired private ProjectMilestoneStepRepository projectMilestoneStepRepository;
  @Autowired private ProjectPostCommentRepository projectPostCommentRepository;
  @Autowired private ProjectPostRatingRepository projectPostRatingRepository;
  @Autowired private ProjectPostRepository projectPostRepository;
  @Autowired private ProjectRepository projectRepository;
  @Autowired private SchoolRepository schoolRepository;
  @Autowired private StudentClassXRepository studentClassXRepository;
  @Autowired private StudentRepository studentRepository;
  @Autowired private StudentSchoolRepository studentSchoolRepository;
  @Autowired private TagRepository tagRepository;
  @Autowired private TeacherClassXRepository teacherClassXRepository;
  @Autowired private TeacherRepository teacherRepository;
  @Autowired private TeacherSchoolRepository teacherSchoolRepository;
  @Autowired private UserXRepository userXRepository;
}
