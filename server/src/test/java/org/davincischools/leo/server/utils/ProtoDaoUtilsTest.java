package org.davincischools.leo.server.utils;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;

import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.ParseException;
import jakarta.persistence.EntityManager;
import org.davincischools.leo.database.test.TestDatabase;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.protos.pl_types.Assignment;
import org.davincischools.leo.protos.pl_types.ClassX;
import org.davincischools.leo.protos.pl_types.District;
import org.davincischools.leo.protos.pl_types.KnowledgeAndSkill;
import org.davincischools.leo.protos.pl_types.Project;
import org.davincischools.leo.protos.pl_types.Project.Milestone;
import org.davincischools.leo.protos.pl_types.ProjectPost;
import org.davincischools.leo.protos.pl_types.ProjectPostComment;
import org.davincischools.leo.protos.pl_types.School;
import org.davincischools.leo.protos.pl_types.Tag;
import org.davincischools.leo.protos.pl_types.UserX;
import org.davincischools.leo.protos.user_x_management.RegisterUserXRequest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProtoDaoUtilsTest.TestApplicationConfiguration.class)
public class ProtoDaoUtilsTest {

  @Configuration
  @ComponentScan(basePackageClasses = {TestDatabase.class, Database.class})
  @EnableAutoConfiguration
  public static class TestApplicationConfiguration {}

  @BeforeClass
  public static void initialSetup() {
    ((DefaultConversionService) DefaultConversionService.getSharedInstance())
        .addConverter(new QueryWithNullsToRecordConverter());
  }

  @Autowired EntityManager entityManager;
  @Autowired Database db;

  @Test
  public void toAssignmentConvertersNull() throws ParseException {
    assertThat(ProtoDaoUtils.toAssignmentProto(null, Assignment::newBuilder)).isEmpty();
  }

  @Test
  public void toAssignmentConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoUtils.toAssignmentProto(
                newUninitialized(org.davincischools.leo.database.daos.Assignment.class),
                Assignment::newBuilder))
        .isEmpty();
  }

  @Test
  public void toAssignmentConvertersEmpty() throws ParseException {
    Assignment proto = Assignment.getDefaultInstance();
    assertThat(
            ProtoDaoUtils.toAssignmentProto(
                    ProtoDaoUtils.toAssignmentDao(proto), Assignment::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toAssignmentConvertersAll() throws ParseException {
    Assignment proto =
        TextFormat.parse(
            """
            id: 1
            name: "name"
            nickname: "nickname"
            short_descr: "short"
            long_descr_html: "long"
            class_x {
              id: 2
              name: "class name"
            }
            """,
            Assignment.class);

    assertThat(
            ProtoDaoUtils.toAssignmentProto(
                    ProtoDaoUtils.toAssignmentDao(proto), Assignment::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toDistrictConvertersNull() throws ParseException {
    assertThat(ProtoDaoUtils.toDistrictProto(null, District::newBuilder)).isEmpty();
  }

  @Test
  public void toDistrictConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoUtils.toDistrictProto(
                newUninitialized(org.davincischools.leo.database.daos.District.class),
                District::newBuilder))
        .isEmpty();
  }

  @Test
  public void toDistrictConvertersEmpty() throws ParseException {
    District proto = District.getDefaultInstance();
    assertThat(
            ProtoDaoUtils.toDistrictProto(ProtoDaoUtils.toDistrictDao(proto), District::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toDistrictConvertersAll() throws ParseException {
    District proto =
        TextFormat.parse(
            """
            id: 1
            name: "name"
            """, District.class);

    assertThat(
            ProtoDaoUtils.toDistrictProto(ProtoDaoUtils.toDistrictDao(proto), District::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toClassXConvertersNull() throws ParseException {
    assertThat(ProtoDaoUtils.toClassXProto(null, ClassX::newBuilder)).isEmpty();
  }

  @Test
  public void toClassXConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoUtils.toClassXProto(
                newUninitialized(org.davincischools.leo.database.daos.ClassX.class),
                ClassX::newBuilder))
        .isEmpty();
  }

  @Test
  public void toClassXConvertersEmpty() throws ParseException {
    ClassX proto = ClassX.getDefaultInstance();
    assertThat(
            ProtoDaoUtils.toClassXProto(ProtoDaoUtils.toClassXDao(proto), ClassX::newBuilder)
                .orElseThrow()
                .build())
        .ignoringFieldDescriptors(
            ClassX.getDescriptor().findFieldByNumber(ClassX.KNOWLEDGE_AND_SKILLS_FIELD_NUMBER))
        .isEqualTo(proto);
  }

  @Test
  public void toClassXConvertersAll() throws ParseException {
    ClassX proto =
        TextFormat.parse(
            """
                id: 1
                name: "name"
                number: "number"
                period: "period"
                grade: "grade"
                short_descr: "short"
                long_descr_html: "long"
                school {
                  id: 2
                  name: "school 2 name"
                }
                knowledge_and_skills {
                  id: 3
                  name: "school 3 name"
                }
                knowledge_and_skills {
                  id: 4
                  name: "school 4 name"
                }
                """,
            ClassX.class);

    assertThat(
            ProtoDaoUtils.toClassXProto(ProtoDaoUtils.toClassXDao(proto), ClassX::newBuilder)
                .orElseThrow()
                .build())
        .ignoringRepeatedFieldOrderOfFieldDescriptors(
            ClassX.getDescriptor().findFieldByNumber(ClassX.KNOWLEDGE_AND_SKILLS_FIELD_NUMBER))
        .isEqualTo(proto);
  }

  @Test
  public void toKnowledgeAndSkillConvertersNull() throws ParseException {
    assertThat(ProtoDaoUtils.toKnowledgeAndSkillProto(null, KnowledgeAndSkill::newBuilder))
        .isEmpty();
  }

  @Test
  public void toKnowledgeAndSkillConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoUtils.toKnowledgeAndSkillProto(
                newUninitialized(org.davincischools.leo.database.daos.KnowledgeAndSkill.class),
                KnowledgeAndSkill::newBuilder))
        .isEmpty();
  }

  @Test
  public void toKnowledgeAndSkillConvertersEmpty() throws ParseException {
    KnowledgeAndSkill proto = KnowledgeAndSkill.getDefaultInstance();
    assertThat(
            ProtoDaoUtils.toKnowledgeAndSkillProto(
                    ProtoDaoUtils.toKnowledgeAndSkillDao(proto), KnowledgeAndSkill::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toKnowledgeAndSkillConvertersAll() throws ParseException {
    KnowledgeAndSkill proto =
        TextFormat.parse(
            """
                id: 1
                type: EKS
                name: "name"
                category: "category"
                short_descr: "short"
                long_descr_html: "long"
                global: true
                user_x {
                  id: 9
                  is_demo: true
                  is_authenticated: true
                }
                """,
            KnowledgeAndSkill.class);

    assertThat(
            ProtoDaoUtils.toKnowledgeAndSkillProto(
                    ProtoDaoUtils.toKnowledgeAndSkillDao(proto), KnowledgeAndSkill::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toSchoolConvertersNull() throws ParseException {
    assertThat(ProtoDaoUtils.toSchoolProto(null, School::newBuilder)).isEmpty();
  }

  @Test
  public void toSchoolConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoUtils.toSchoolProto(
                newUninitialized(org.davincischools.leo.database.daos.School.class),
                School::newBuilder))
        .isEmpty();
  }

  @Test
  public void toSchoolConvertersEmpty() throws ParseException {
    School proto = TextFormat.parse("""
                id: 1
                """, School.class);

    assertThat(
            ProtoDaoUtils.toSchoolProto(ProtoDaoUtils.toSchoolDao(proto), School::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toSchoolConvertersAll() throws ParseException {
    School proto =
        TextFormat.parse(
            """
                id: 1
                name: "school name"
                nickname: "nickname"
                address: "address"
                district {
                  id: 2
                  name: "district name"
                }
                """,
            School.class);

    assertThat(
            ProtoDaoUtils.toSchoolProto(ProtoDaoUtils.toSchoolDao(proto), School::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toUserXConvertersNull() throws ParseException {
    assertThat(ProtoDaoUtils.toUserXProto(null, UserX::newBuilder)).isEmpty();
  }

  @Test
  public void toUserXConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoUtils.toUserXProto(
                newUninitialized(org.davincischools.leo.database.daos.UserX.class),
                UserX::newBuilder))
        .isEmpty();
  }

  @Test
  public void toUserXConvertersJustId() throws ParseException {
    UserX proto =
        TextFormat.parse(
            """
                id: 1
                is_demo: true
                is_authenticated: true
                """,
            UserX.class);

    assertThat(
            ProtoDaoUtils.toUserXProto(ProtoDaoUtils.toUserXDao(proto), UserX::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toUserXConvertersEmpty() throws ParseException {
    UserX proto =
        TextFormat.parse(
            """
                is_demo: false
                is_authenticated: false
                """,
            UserX.class);

    assertThat(
            ProtoDaoUtils.toUserXProto(
                    ProtoDaoUtils.toUserXDao(UserX.getDefaultInstance()), UserX::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toUserXConvertersAllNonDemo() throws ParseException {
    UserX proto =
        TextFormat.parse(
            """
                id: 1
                district_id: 2
                first_name: "first"
                last_name: "last"
                email_address: "email"
                is_admin_x: true
                admin_x_id: 3
                is_teacher: true
                teacher_id: 4
                is_student: true
                student_id: 5
                is_demo: false
                is_authenticated: true
                """,
            UserX.class);

    assertThat(
            ProtoDaoUtils.toUserXProto(ProtoDaoUtils.toUserXDao(proto), UserX::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toUserXConvertersAllDemo() throws ParseException {
    UserX proto =
        TextFormat.parse(
            """
                id: 1
                district_id: 2
                first_name: "first"
                last_name: "last"
                email_address: "email"
                is_demo: true
                is_authenticated: true
                """,
            UserX.class);

    assertThat(
            ProtoDaoUtils.toUserXProto(ProtoDaoUtils.toUserXDao(proto), UserX::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toProjectPostConvertersNull() throws ParseException {
    assertThat(
            ProtoDaoUtils.toProjectPostProto(
                (org.davincischools.leo.database.daos.ProjectPost) null, ProjectPost::newBuilder))
        .isEmpty();
  }

  @Test
  public void toProjectPostConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoUtils.toProjectPostProto(
                newUninitialized(org.davincischools.leo.database.daos.ProjectPost.class),
                ProjectPost::newBuilder))
        .isEmpty();
  }

  @Test
  public void toProjectPostConvertersEmpty() throws ParseException {
    ProjectPost proto = ProjectPost.getDefaultInstance();

    assertThat(
            ProtoDaoUtils.toProjectPostProto(
                    ProtoDaoUtils.toProjectPostDao(proto), ProjectPost::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toProjectPostConvertersAll() throws ParseException {
    ProjectPost proto =
        TextFormat.parse(
            """
                id: 1
                user_x {
                  id: 2
                  is_demo: true
                  is_authenticated: true
                }
                project {
                  id: 3
                }
                name: "name"
                long_descr_html: "long_descr_html"
                desired_feedback: "desired_feedback"
                tags {
                  user_x_id: 4
                  text: "tag1"
                }
                tags {
                  user_x_id: 5
                  text: "tag2"
                }
                tags {
                  user_x_id: 6
                  text: "tag3"
                }
                comments {
                  id: 11
                  user_x {
                    id: 12
                    is_demo: true
                    is_authenticated: true
                  }
                  project_post {
                    id: 13
                  }
                  long_descr_html: "long_descr_html"
                  post_time_ms: 14
                  being_edited: true
                }
                comments {
                  id: 7
                  user_x {
                    id: 8
                    is_demo: true
                    is_authenticated: true
                  }
                  project_post {
                    id: 9
                  }
                  long_descr_html: "long_descr_html"
                  post_time_ms: 10
                  being_edited: true
                }
                post_time_ms: 15
                being_edited: true
                """,
            ProjectPost.class);

    assertThat(
            ProtoDaoUtils.toProjectPostProto(
                    ProtoDaoUtils.toProjectPostDao(proto), ProjectPost::newBuilder)
                .orElseThrow()
                .build())
        .ignoringFields(ProjectPost.TAGS_FIELD_NUMBER)
        .ignoringFields(ProjectPost.COMMENTS_FIELD_NUMBER)
        .isEqualTo(proto);
  }

  @Test
  public void toFullProjectPostConverter() throws ParseException {
    ProjectPost proto =
        TextFormat.parse(
            """
                id: 1
                user_x {
                  id: 2
                  is_demo: true
                  is_authenticated: true
                }
                project {
                  id: 3
                }
                name: "name"
                long_descr_html: "long_descr_html"
                desired_feedback: "desired_feedback"
                tags {
                  user_x_id: 4
                  text: "tag1"
                }
                tags {
                  user_x_id: 5
                  text: "tag2"
                }
                tags {
                  user_x_id: 6
                  text: "tag3"
                }
                comments {
                  id: 11
                  user_x {
                    id: 12
                    is_demo: true
                    is_authenticated: true
                  }
                  project_post {
                    id: 13
                  }
                  long_descr_html: "long_descr_html"
                  post_time_ms: 14
                  being_edited: true
                }
                comments {
                  id: 7
                  user_x {
                    id: 8
                    is_demo: true
                    is_authenticated: true
                  }
                  project_post {
                    id: 9
                  }
                  long_descr_html: "long_descr_html"
                  post_time_ms: 10
                  being_edited: true
                }
                post_time_ms: 7
                being_edited: true
                """,
            ProjectPost.class);

    assertThat(
            ProtoDaoUtils.toProjectPostProto(
                    ProtoDaoUtils.toFullProjectPostRecord(proto), ProjectPost::newBuilder)
                .orElseThrow()
                .build())
        .ignoringFields(ProjectPost.TAGS_FIELD_NUMBER)
        .isEqualTo(proto);
  }

  @Test
  public void toInterestConvertersNull() throws ParseException {
    assertThat(ProtoDaoUtils.toRegisterUserXRequestProto(null, RegisterUserXRequest::newBuilder))
        .isEmpty();
  }

  @Test
  public void toInterestConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoUtils.toRegisterUserXRequestProto(
                newUninitialized(org.davincischools.leo.database.daos.Interest.class),
                RegisterUserXRequest::newBuilder))
        .isEmpty();
  }

  @Test
  public void toInterestConvertersEmpty() throws ParseException {
    RegisterUserXRequest proto = RegisterUserXRequest.getDefaultInstance();

    assertThat(
            ProtoDaoUtils.toRegisterUserXRequestProto(
                    ProtoDaoUtils.toInterestDao(proto), RegisterUserXRequest::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toInterestConvertersAll() throws ParseException {
    RegisterUserXRequest proto =
        TextFormat.parse(
            """
                first_name: "first_name"
                last_name: "last_name"
                email_address: "email_address"
                password: "password"
                verify_password: "verify_password"
                profession: "profession"
                reason_for_interest: "reason_for_interest"
                district_name: "district_name"
                school_name: "school_name"
                address_line_1: "address_line_1"
                address_line_2: "address_line_2"
                city: "city"
                state: "state"
                zip_code: "zip_code"
                num_teachers: 1
                num_students: 2
                """,
            RegisterUserXRequest.class);

    assertThat(
            ProtoDaoUtils.toRegisterUserXRequestProto(
                    ProtoDaoUtils.toInterestDao(proto), RegisterUserXRequest::newBuilder)
                .orElseThrow()
                .build())
        .ignoringFieldDescriptors(
            RegisterUserXRequest.getDescriptor()
                .findFieldByNumber(RegisterUserXRequest.PASSWORD_FIELD_NUMBER),
            RegisterUserXRequest.getDescriptor()
                .findFieldByNumber(RegisterUserXRequest.VERIFY_PASSWORD_FIELD_NUMBER))
        .isEqualTo(proto);
  }

  @Test
  public void toProjectMilestoneStepConvertersNull() throws ParseException {
    assertThat(ProtoDaoUtils.toMilestoneStepProto(null, Milestone.Step::newBuilder)).isEmpty();
  }

  @Test
  public void toProjectMilestoneStepConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoUtils.toMilestoneStepProto(
                newUninitialized(org.davincischools.leo.database.daos.ProjectMilestoneStep.class),
                Milestone.Step::newBuilder))
        .isEmpty();
  }

  @Test
  public void toProjectMilestoneStepConvertersEmpty() throws ParseException {
    Project.Milestone.Step proto = Project.Milestone.Step.getDefaultInstance();

    assertThat(
            ProtoDaoUtils.toMilestoneStepProto(
                    ProtoDaoUtils.toProjectMilestoneStepDao(proto), Milestone.Step::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toProjectMilestoneStepConvertersAll() throws ParseException {
    Project.Milestone.Step proto =
        TextFormat.parse(
            """
                id: 1
                name: "name"
                """,
            Project.Milestone.Step.class);

    assertThat(
            ProtoDaoUtils.toMilestoneStepProto(
                    ProtoDaoUtils.toProjectMilestoneStepDao(proto), Milestone.Step::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toProjectMilestoneConvertersNull() throws ParseException {
    assertThat(
            ProtoDaoUtils.toMilestoneProto(
                (org.davincischools.leo.database.daos.ProjectMilestone) null,
                Milestone::newBuilder))
        .isEmpty();
  }

  @Test
  public void toProjectMilestoneConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoUtils.toMilestoneProto(
                newUninitialized(org.davincischools.leo.database.daos.ProjectMilestone.class),
                Milestone::newBuilder))
        .isEmpty();
  }

  @Test
  public void toProjectMilestoneConvertersEmpty() throws ParseException {
    Project.Milestone proto = Project.Milestone.getDefaultInstance();

    assertThat(
            ProtoDaoUtils.toMilestoneProto(
                    ProtoDaoUtils.toProjectMilestoneDao(proto), Milestone::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toProjectMilestoneConvertersAll() throws ParseException {
    Project.Milestone proto =
        TextFormat.parse(
            """
                id: 1
                name: "name"
                steps {
                  id: 2
                  name: "step name"
                }
                """,
            Project.Milestone.class);

    assertThat(
            ProtoDaoUtils.toMilestoneProto(
                    ProtoDaoUtils.toProjectMilestoneDao(proto), Milestone::newBuilder)
                .orElseThrow()
                .build())
        .ignoringFields(Project.Milestone.STEPS_FIELD_NUMBER)
        .isEqualTo(proto);
  }

  @Test
  public void toMilestoneWithStepsConverters() throws ParseException {
    Project.Milestone proto =
        TextFormat.parse(
            """
                id: 1
                name: "name"
                steps {
                  id: 2
                  name: "step name 2"
                }
                steps {
                  id: 3
                  name: "step name 3"
                }
                """,
            Project.Milestone.class);

    assertThat(
            ProtoDaoUtils.toMilestoneProto(
                    ProtoDaoUtils.toMilestoneWithStepsRecord(proto), Milestone::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toProjectConvertersNull() throws ParseException {
    assertThat(
            ProtoDaoUtils.toProjectProto(
                (org.davincischools.leo.database.daos.Project) null, Project::newBuilder))
        .isEmpty();
  }

  @Test
  public void toProjectConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoUtils.toProjectProto(
                newUninitialized(org.davincischools.leo.database.daos.Project.class),
                Project::newBuilder))
        .isEmpty();
  }

  @Test
  public void toProjectConvertersEmpty() throws ParseException {
    Project proto = Project.getDefaultInstance();

    assertThat(
            ProtoDaoUtils.toProjectProto(ProtoDaoUtils.toProjectDao(proto), Project::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toProjectConvertersAll() throws ParseException {
    Project proto =
        TextFormat.parse(
            """
                id: 1
                name: "name"
                short_descr: "short"
                long_descr_html: "long"
                favorite: true
                thumbs_state: THUMBS_UP
                thumbs_state_reason: "reason"
                archived: true
                active: true
                assignment {
                  id: 2
                  name: "assignment name"
                  short_descr: "assignment short"
                  long_descr_html: "assignment long"
                }
                milestones {
                  id: 3
                  name: "milestone name 3"
                  steps {
                    id: 4
                    name: "step name 4"
                  }
                  steps {
                    id: 5
                    name: "step name 5"
                  }
                }
                milestones {
                  id: 6
                  name: "milestone name 6"
                }
                """,
            Project.class);

    assertThat(
            ProtoDaoUtils.toProjectProto(ProtoDaoUtils.toProjectDao(proto), Project::newBuilder)
                .orElseThrow()
                .build())
        .ignoringFields(Project.MILESTONES_FIELD_NUMBER)
        .isEqualTo(proto);
  }

  @Test
  public void toProjectConvertersEnum() throws ParseException {
    Project proto =
        TextFormat.parse(
            """
                thumbs_state: UNSET
                """, Project.class);

    assertThat(
            ProtoDaoUtils.toProjectProto(ProtoDaoUtils.toProjectDao(proto), Project::newBuilder)
                .orElseThrow()
                .build())
        .ignoringFields(Project.MILESTONES_FIELD_NUMBER)
        .isEqualTo(Project.getDefaultInstance());
  }

  @Test
  public void toProjectWithMilestoneConvertersAll() throws ParseException {
    Project proto =
        TextFormat.parse(
            """
                id: 1
                name: "name"
                short_descr: "short"
                long_descr_html: "long"
                favorite: true
                thumbs_state: THUMBS_UP
                thumbs_state_reason: "reason"
                archived: true
                active: true
                assignment {
                  id: 2
                  name: "assignment name"
                  short_descr: "assignment short"
                  long_descr_html: "assignment long"
                }
                milestones {
                  id: 3
                  name: "milestone name 3"
                  steps {
                    id: 4
                    name: "step name 4"
                  }
                  steps {
                    id: 5
                    name: "step name 5"
                  }
                }
                milestones {
                  id: 6
                  name: "milestone name 6"
                }
                """,
            Project.class);

    assertThat(
            ProtoDaoUtils.toProjectProto(
                    ProtoDaoUtils.toProjectWithMilestonesRecord(proto), Project::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toTagConvertersNull() throws ParseException {
    assertThat(ProtoDaoUtils.toTagProto(null, Tag::newBuilder)).isEmpty();
  }

  @Test
  public void toTagConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoUtils.toTagProto(
                newUninitialized(org.davincischools.leo.database.daos.Tag.class), Tag::newBuilder))
        .isEmpty();
  }

  @Test
  public void toTagConvertersEmpty() throws ParseException {
    Tag proto = Tag.getDefaultInstance();

    assertThat(
            ProtoDaoUtils.toTagProto(ProtoDaoUtils.toTagDao(proto), Tag::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toTagConvertersAll() throws ParseException {
    Tag proto =
        TextFormat.parse(
            """
                text: "text"
                user_x_id: 2
                """,
            Tag.class);

    assertThat(
            ProtoDaoUtils.toTagProto(ProtoDaoUtils.toTagDao(proto), Tag::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toProjectPostCommentConvertersNull() throws ParseException {
    assertThat(
            ProtoDaoUtils.toProjectPostCommentProto(
                (org.davincischools.leo.database.daos.ProjectPostComment) null,
                ProjectPostComment::newBuilder))
        .isEmpty();
  }

  @Test
  public void toProjectPostCommentConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoUtils.toProjectPostCommentProto(
                newUninitialized(org.davincischools.leo.database.daos.ProjectPostComment.class),
                ProjectPostComment::newBuilder))
        .isEmpty();
  }

  @Test
  public void toProjectPostCommentConvertersEmpty() throws ParseException {
    ProjectPostComment proto = ProjectPostComment.getDefaultInstance();

    assertThat(
            ProtoDaoUtils.toProjectPostCommentProto(
                    ProtoDaoUtils.toProjectPostCommentDao(proto), ProjectPostComment::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toProjectPostCommentConvertersAll() throws ParseException {
    ProjectPostComment proto =
        TextFormat.parse(
            """
                id: 1
                user_x {
                  id: 2
                  is_demo: true
                  is_authenticated: true
                }
                project_post {
                  id: 3
                }
                long_descr_html: "long_descr_html"
                post_time_ms: 4
                being_edited: true
                """,
            ProjectPostComment.class);

    assertThat(
            ProtoDaoUtils.toProjectPostCommentProto(
                    ProtoDaoUtils.toProjectPostCommentDao(proto), ProjectPostComment::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toFullProjectPostCommentConverter() throws ParseException {
    ProjectPostComment proto =
        TextFormat.parse(
            """
                id: 1
                user_x {
                  id: 2
                  is_demo: true
                  is_authenticated: true
                }
                project_post: {
                  id: 3
                  user_x {
                    id: 4
                    is_demo: true
                    is_authenticated: true
                  }
                }
                long_descr_html: "long_descr_html"
                post_time_ms: 4
                being_edited: true
                """,
            ProjectPostComment.class);

    assertThat(
            ProtoDaoUtils.toProjectPostCommentProto(
                    ProtoDaoUtils.toFullProjectPostComment(proto), ProjectPostComment::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  private <T> T newUninitialized(Class<T> entityClass) {
    return entityManager.getReference(entityClass, 0);
  }
}
