package org.davincischools.leo.database.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.repos.AdminXRepository;
import org.davincischools.leo.database.utils.repos.AssignmentKnowledgeAndSkillRepository;
import org.davincischools.leo.database.utils.repos.AssignmentProjectDefinitionRepository;
import org.davincischools.leo.database.utils.repos.AssignmentRepository;
import org.davincischools.leo.database.utils.repos.ClassXKnowledgeAndSkillRepository;
import org.davincischools.leo.database.utils.repos.ClassXRepository;
import org.davincischools.leo.database.utils.repos.CommentXRepository;
import org.davincischools.leo.database.utils.repos.DeadlineRepository;
import org.davincischools.leo.database.utils.repos.DeadlineStatusRepository;
import org.davincischools.leo.database.utils.repos.DistrictRepository;
import org.davincischools.leo.database.utils.repos.FileXRepository;
import org.davincischools.leo.database.utils.repos.InterestRepository;
import org.davincischools.leo.database.utils.repos.KnowledgeAndSkillRepository;
import org.davincischools.leo.database.utils.repos.LogReferenceRepository;
import org.davincischools.leo.database.utils.repos.LogRepository;
import org.davincischools.leo.database.utils.repos.MotivationRepository;
import org.davincischools.leo.database.utils.repos.NotificationRepository;
import org.davincischools.leo.database.utils.repos.PortfolioRepository;
import org.davincischools.leo.database.utils.repos.PostRepository;
import org.davincischools.leo.database.utils.repos.ProjectAssignmentRepository;
import org.davincischools.leo.database.utils.repos.ProjectDefinitionCategoryRepository;
import org.davincischools.leo.database.utils.repos.ProjectDefinitionCategoryTypeRepository;
import org.davincischools.leo.database.utils.repos.ProjectDefinitionRepository;
import org.davincischools.leo.database.utils.repos.ProjectImageRepository;
import org.davincischools.leo.database.utils.repos.ProjectInputAssignmentRepository;
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
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

@Component
@Getter
@EnableJpaRepositories(basePackageClasses = {UserXRepository.class})
@RequiredArgsConstructor
@EntityScan(basePackageClasses = {UserX.class})
public class Database {

  private final AdminXRepository adminXRepository;
  private final AssignmentKnowledgeAndSkillRepository assignmentKnowledgeAndSkillRepository;
  private final AssignmentProjectDefinitionRepository assignmentProjectDefinitionRepository;
  private final AssignmentRepository assignmentRepository;
  private final ClassXKnowledgeAndSkillRepository classXKnowledgeAndSkillRepository;
  private final ClassXRepository classXRepository;
  private final CommentXRepository commentXRepository;
  private final DeadlineRepository deadlineRepository;
  private final DeadlineStatusRepository deadlineStatusRepository;
  private final DistrictRepository districtRepository;
  private final FileXRepository fileXRepository;
  private final InterestRepository interestRepository;
  private final KnowledgeAndSkillRepository knowledgeAndSkillRepository;
  private final LogReferenceRepository logReferenceRepository;
  private final LogRepository logRepository;
  private final MotivationRepository motivationRepository;
  private final NotificationRepository notificationRepository;
  private final PortfolioRepository portfolioRepository;
  private final PostRepository postRepository;
  private final ProjectAssignmentRepository projectAssignmentRepository;
  private final ProjectDefinitionCategoryRepository projectDefinitionCategoryRepository;
  private final ProjectDefinitionCategoryTypeRepository projectDefinitionCategoryTypeRepository;
  private final ProjectDefinitionRepository projectDefinitionRepository;
  private final ProjectImageRepository projectImageRepository;
  private final ProjectInputAssignmentRepository projectInputAssignmentRepository;
  private final ProjectInputFulfillmentRepository projectInputFulfillmentRepository;
  private final ProjectInputRepository projectInputRepository;
  private final ProjectInputValueRepository projectInputValueRepository;
  private final ProjectMilestoneRepository projectMilestoneRepository;
  private final ProjectMilestoneStepRepository projectMilestoneStepRepository;
  private final ProjectPostCommentRepository projectPostCommentRepository;
  private final ProjectPostRatingRepository projectPostRatingRepository;
  private final ProjectPostRepository projectPostRepository;
  private final ProjectRepository projectRepository;
  private final SchoolRepository schoolRepository;
  private final StudentClassXRepository studentClassXRepository;
  private final StudentRepository studentRepository;
  private final StudentSchoolRepository studentSchoolRepository;
  private final TagRepository tagRepository;
  private final TeacherClassXRepository teacherClassXRepository;
  private final TeacherRepository teacherRepository;
  private final TeacherSchoolRepository teacherSchoolRepository;
  private final UserXRepository userXRepository;
}
