package org.davincischools.leo.server.utils;

import static com.google.common.truth.Truth8.assertThat;
import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toAssignmentDao;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toAssignmentProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toClassXDao;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toClassXProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toDistrictDao;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toDistrictProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toInterestDao;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toKnowledgeAndSkillDao;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toKnowledgeAndSkillProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toMilestoneProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toMilestoneStepProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toOptionProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toProjectDao;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toProjectDefinitionCategoryDao;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toProjectDefinitionDao;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toProjectDefinitionProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toProjectInputCategoryProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toProjectInputDao;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toProjectMilestoneDao;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toProjectMilestoneStepDao;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toProjectPostCommentDao;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toProjectPostCommentProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toProjectPostDao;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toProjectPostProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toProjectPostRatingDao;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toProjectPostRatingProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toProjectProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toRegisterUserXRequestProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toSchoolDao;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toSchoolProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toTagDao;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toTagProto;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toUserXDao;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toUserXProto;

import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.ParseException;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import org.davincischools.leo.database.test.TestDatabase;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.protos.pl_types.Assignment;
import org.davincischools.leo.protos.pl_types.ClassX;
import org.davincischools.leo.protos.pl_types.District;
import org.davincischools.leo.protos.pl_types.KnowledgeAndSkill;
import org.davincischools.leo.protos.pl_types.Project;
import org.davincischools.leo.protos.pl_types.Project.Milestone;
import org.davincischools.leo.protos.pl_types.ProjectDefinition;
import org.davincischools.leo.protos.pl_types.ProjectInputCategory;
import org.davincischools.leo.protos.pl_types.ProjectInputCategory.Option;
import org.davincischools.leo.protos.pl_types.ProjectInputCategory.ValueType;
import org.davincischools.leo.protos.pl_types.ProjectPost;
import org.davincischools.leo.protos.pl_types.ProjectPostComment;
import org.davincischools.leo.protos.pl_types.ProjectPostRating;
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

  @Test
  public void toAssignmentProtoNull() {
    assertThat(toAssignmentProto(null, true, Assignment::newBuilder)).isEmpty();
  }

  @Test
  public void toAssignmentDaoNull() {
    assertThat(toAssignmentDao(null)).isEmpty();
  }

  @Test
  public void toAssignmentProtoUninitialized() {
    assertThat(
            toAssignmentProto(
                newUninitialized(org.davincischools.leo.database.daos.Assignment.class),
                true,
                Assignment::newBuilder))
        .isEmpty();
  }

  @Test
  public void toAssignmentDaoUninitialized() {
    assertThat(toAssignmentDao(Assignment.getDefaultInstance())).isEmpty();
  }

  @Test
  public void toAssignmentRoundTrip() throws ParseException {
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
            knowledge_and_skills {
              id: 3
              name: "ks 3 name"
            }
            knowledge_and_skills {
              id: 4
              name: "ks 4 name"
            }
            project_definitions {
              name: "pd 5 name"
              inputs {
                category {
                  id: 6
                }
              }
            }
            project_definitions {
              name: "pd 7 name"
              selected: true
              inputs {
                category {
                  id: 8
                }
              }
            }
            """,
            Assignment.class);

    assertThat(
            toAssignmentProto(toAssignmentDao(proto).orElseThrow(), true, Assignment::newBuilder)
                .orElseThrow()
                .build())
        .ignoringRepeatedFieldOrderOfFields(
            Assignment.KNOWLEDGE_AND_SKILLS_FIELD_NUMBER,
            Assignment.PROJECT_DEFINITIONS_FIELD_NUMBER)
        .isEqualTo(proto);
  }

  @Test
  public void toDistrictProtoNull() throws ParseException {
    assertThat(toDistrictProto(null, District::newBuilder)).isEmpty();
  }

  @Test
  public void toDistrictDaoNull() throws ParseException {
    assertThat(toDistrictDao(null)).isEmpty();
  }

  @Test
  public void toDistrictProtoUninitialized() throws ParseException {
    assertThat(
            toDistrictProto(
                newUninitialized(org.davincischools.leo.database.daos.District.class),
                District::newBuilder))
        .isEmpty();
  }

  @Test
  public void toDistrictDaoUninitialized() throws ParseException {
    assertThat(toDistrictDao(District.getDefaultInstance())).isEmpty();
  }

  @Test
  public void toDistrictRoundTrip() throws ParseException {
    District proto =
        TextFormat.parse(
            """
            id: 1
            name: "name"
            """, District.class);

    assertThat(
            toDistrictProto(toDistrictDao(proto).orElseThrow(), District::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toClassXProtoNull() {
    assertThat(toClassXProto(null, true, ClassX::newBuilder)).isEmpty();
  }

  @Test
  public void toClassXDaoNull() {
    assertThat(toClassXDao(null)).isEmpty();
  }

  @Test
  public void toClassXProtoUninitialized() {
    assertThat(
            toClassXProto(
                newUninitialized(org.davincischools.leo.database.daos.ClassX.class),
                true,
                ClassX::newBuilder))
        .isEmpty();
  }

  @Test
  public void toClassXDaoUninitialized() {
    assertThat(toClassXDao(ClassX.getDefaultInstance())).isEmpty();
  }

  @Test
  public void toClassXRoundTrip() throws ParseException {
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
                  id: 1
                  name: "school name"
                }
                knowledge_and_skills {
                  id: 2
                  name: "ks 2 name"
                }
                knowledge_and_skills {
                  id: 3
                  name: "ks 3 name"
                }
                assignments: {
                  id: 4
                  name: "assignment 4 name"
                  knowledge_and_skills {
                    id: 5
                    name: "ks 5 name"
                  }
                }
                assignments: {
                  id: 6
                  name: "assignment 6 name"
                  knowledge_and_skills {
                    id: 7
                    name: "ks 7 name"
                  }
                }
                """,
            ClassX.class);

    assertThat(
            toClassXProto(toClassXDao(proto).orElseThrow(), true, ClassX::newBuilder)
                .orElseThrow()
                .build())
        .ignoringRepeatedFieldOrder()
        .isEqualTo(proto);
  }

  @Test
  public void toKnowledgeAndSkillProtoNull() throws ParseException {
    assertThat(toKnowledgeAndSkillProto(null, KnowledgeAndSkill::newBuilder)).isEmpty();
  }

  @Test
  public void toKnowledgeAndSkillDaoNull() throws ParseException {
    assertThat(toKnowledgeAndSkillProto(null, KnowledgeAndSkill::newBuilder)).isEmpty();
  }

  @Test
  public void toKnowledgeAndSkillProtoUninitialized() throws ParseException {
    assertThat(
            toKnowledgeAndSkillProto(
                newUninitialized(org.davincischools.leo.database.daos.KnowledgeAndSkill.class),
                KnowledgeAndSkill::newBuilder))
        .isEmpty();
  }

  @Test
  public void toKnowledgeAndSkillDaoUninitialized() throws ParseException {
    assertThat(toKnowledgeAndSkillDao(KnowledgeAndSkill.getDefaultInstance())).isEmpty();
  }

  @Test
  public void toKnowledgeAndSkillRoundTrip() throws ParseException {
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
                  is_admin_x: false
                  is_teacher: false
                  is_student: false
                  is_demo: true
                  is_authenticated: true
                }
                """,
            KnowledgeAndSkill.class);

    assertThat(
            toKnowledgeAndSkillProto(
                    toKnowledgeAndSkillDao(proto).orElseThrow(), KnowledgeAndSkill::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toSchoolProtoNull() throws ParseException {
    assertThat(toSchoolProto(null, School::newBuilder)).isEmpty();
  }

  @Test
  public void toSchoolDaoNull() throws ParseException {
    assertThat(toSchoolDao(null)).isEmpty();
  }

  @Test
  public void toSchoolProtoUninitialized() throws ParseException {
    assertThat(
            toSchoolProto(
                newUninitialized(org.davincischools.leo.database.daos.School.class),
                School::newBuilder))
        .isEmpty();
  }

  @Test
  public void toSchoolDaoUninitialized() throws ParseException {
    assertThat(toSchoolDao(School.getDefaultInstance())).isEmpty();
  }

  @Test
  public void toSchoolRoundTrip() throws ParseException {
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
            toSchoolProto(toSchoolDao(proto).orElseThrow(), School::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toUserXProtoNull() throws ParseException {
    assertThat(toUserXProto(null, UserX::newBuilder)).isEmpty();
  }

  @Test
  public void toUserXDaoNull() throws ParseException {
    assertThat(toUserXDao(null)).isEmpty();
  }

  @Test
  public void toUserXProtoUninitialized() throws ParseException {
    assertThat(
            toUserXProto(
                newUninitialized(org.davincischools.leo.database.daos.UserX.class),
                UserX::newBuilder))
        .isEmpty();
  }

  @Test
  public void toUserXDaoUninitialized() throws ParseException {
    assertThat(toUserXDao(UserX.getDefaultInstance())).isEmpty();
  }

  @Test
  public void toUserXRoundTrip() throws ParseException {
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
            toUserXProto(toUserXDao(proto).orElseThrow(), UserX::newBuilder).orElseThrow().build())
        .isEqualTo(proto);
  }

  @Test
  public void toUserXRoundTripDemoOnly() throws ParseException {
    UserX proto =
        TextFormat.parse(
            """
                id: 1
                district_id: 2
                first_name: "first"
                last_name: "last"
                email_address: "email"
                is_admin_x: false
                is_teacher: false
                is_student: false
                is_demo: true
                is_authenticated: true
                """,
            UserX.class);

    assertThat(
            toUserXProto(toUserXDao(proto).orElseThrow(), UserX::newBuilder).orElseThrow().build())
        .isEqualTo(proto);
  }

  @Test
  public void toProjectPostProtoNull() throws ParseException {
    assertThat(toProjectPostProto(null, true, ProjectPost::newBuilder)).isEmpty();
  }

  @Test
  public void toProjectPostDaoNull() throws ParseException {
    assertThat(toProjectPostDao(null)).isEmpty();
  }

  @Test
  public void toProjectPostProtoUninitialized() throws ParseException {
    assertThat(
            toProjectPostProto(
                newUninitialized(org.davincischools.leo.database.daos.ProjectPost.class),
                true,
                ProjectPost::newBuilder))
        .isEmpty();
  }

  @Test
  public void toProjectPostDaoUninitialized() throws ParseException {
    assertThat(toProjectPostDao(ProjectPost.getDefaultInstance())).isEmpty();
  }

  @Test
  public void toProjectPostRoundTrip() throws ParseException {
    ProjectPost proto =
        TextFormat.parse(
            """
                id: 1
                user_x {
                  id: 2
                  is_admin_x: false
                  is_teacher: false
                  is_student: false
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
                    is_admin_x: false
                    is_teacher: false
                    is_student: false
                    is_demo: true
                    is_authenticated: true
                  }
                  long_descr_html: "long_descr_html"
                  post_time_ms: 14
                  being_edited: true
                }
                comments {
                  id: 7
                  user_x {
                    id: 8
                    is_admin_x: false
                    is_teacher: false
                    is_student: false
                    is_demo: true
                    is_authenticated: true
                  }
                  long_descr_html: "long_descr_html"
                  post_time_ms: 10
                  being_edited: true
                }
                post_time_ms: 15
                being_edited: true
                rating_categories {
                  project_input_fulfillment_id: 16
                  category: "category_1"
                  value: "value_1"
                  value_type: MOTIVATION
                }
                rating_categories {
                  project_input_fulfillment_id: 17
                  category: "category_2"
                  value: "value_2"
                  value_type: FREE_TEXT
                }
                rating_categories {
                  project_input_fulfillment_id: 18
                  category: "category_3"
                  value: "value_3"
                  value_type: EKS
                }
                ratings {
                  id: 19
                  user_x {
                    id: 20
                    is_admin_x: false
                    is_teacher: false
                    is_student: false
                    is_demo: true
                    is_authenticated: true
                  }
                  rating: 5
                  rating_type: INITIAL_1_TO_5
                  project_input_fulfillment_id: 16
                }
                ratings {
                  id: 22
                  user_x {
                    id: 23
                    is_admin_x: false
                    is_teacher: false
                    is_student: false
                    is_demo: true
                    is_authenticated: true
                  }
                  rating: 24
                  rating_type: GOAL_COMPLETE_PCT
                  project_input_fulfillment_id: 17
                }
                ratings {
                  id: 25
                  user_x {
                    id: 26
                    is_admin_x: false
                    is_teacher: false
                    is_student: false
                    is_demo: true
                    is_authenticated: true
                  }
                  rating: 27
                  rating_type: GOAL_COMPLETE_PCT
                  project_input_fulfillment_id: 18
                }
                """,
            ProjectPost.class);

    assertThat(
            toProjectPostProto(toProjectPostDao(proto).orElseThrow(), true, ProjectPost::newBuilder)
                .orElseThrow()
                .build())
        .ignoringRepeatedFieldOrder()
        .isEqualTo(proto);
  }

  @Test
  public void toInterestProtoNull() throws ParseException {
    assertThat(toRegisterUserXRequestProto(null, RegisterUserXRequest::newBuilder)).isEmpty();
  }

  @Test
  public void toInterestDaoNull() throws ParseException {
    assertThat(toInterestDao(null)).isEmpty();
  }

  @Test
  public void toInterestProtoUninitialized() throws ParseException {
    assertThat(
            toRegisterUserXRequestProto(
                newUninitialized(org.davincischools.leo.database.daos.Interest.class),
                RegisterUserXRequest::newBuilder))
        .isEmpty();
  }

  @Test
  public void toInterestDaoUninitialized() throws ParseException {
    assertThat(toInterestDao(RegisterUserXRequest.getDefaultInstance())).isEmpty();
  }

  @Test
  public void toInterestRoundTrip() throws ParseException {
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
            toRegisterUserXRequestProto(
                    toInterestDao(proto).orElseThrow(), RegisterUserXRequest::newBuilder)
                .orElseThrow()
                .build())
        .ignoringFields(
            RegisterUserXRequest.PASSWORD_FIELD_NUMBER,
            RegisterUserXRequest.VERIFY_PASSWORD_FIELD_NUMBER)
        .isEqualTo(proto);
  }

  @Test
  public void toProjectMilestoneStepProtoNull() throws ParseException {
    assertThat(toMilestoneStepProto(null, Milestone.Step::newBuilder)).isEmpty();
  }

  @Test
  public void toProjectMilestoneStepDaoNull() throws ParseException {
    assertThat(toProjectMilestoneStepDao(null)).isEmpty();
  }

  @Test
  public void toProjectMilestoneStepProtoUninitialized() throws ParseException {
    assertThat(
            toMilestoneStepProto(
                newUninitialized(org.davincischools.leo.database.daos.ProjectMilestoneStep.class),
                Milestone.Step::newBuilder))
        .isEmpty();
  }

  @Test
  public void toProjectMilestoneStepDaoUninitialized() throws ParseException {
    assertThat(toProjectMilestoneStepDao(Milestone.Step.getDefaultInstance())).isEmpty();
  }

  @Test
  public void toProjectMilestoneStepRoundTrip() throws ParseException {
    Project.Milestone.Step proto =
        TextFormat.parse(
            """
                id: 1
                name: "name"
                """,
            Project.Milestone.Step.class);

    assertThat(
            toMilestoneStepProto(
                    toProjectMilestoneStepDao(proto).orElseThrow(), Milestone.Step::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toProjectMilestoneProtoNull() throws ParseException {
    assertThat(toMilestoneProto(null, Milestone::newBuilder)).isEmpty();
  }

  @Test
  public void toProjectMilestoneDaoNull() throws ParseException {
    assertThat(toProjectMilestoneDao(null)).isEmpty();
  }

  @Test
  public void toProjectMilestoneProtoUninitialized() throws ParseException {
    assertThat(
            toMilestoneProto(
                newUninitialized(org.davincischools.leo.database.daos.ProjectMilestone.class),
                Milestone::newBuilder))
        .isEmpty();
  }

  @Test
  public void toProjectMilestoneDaoUninitialized() throws ParseException {
    assertThat(toProjectMilestoneDao(Milestone.getDefaultInstance())).isEmpty();
  }

  @Test
  public void toProjectMilestoneRoundTrip() throws ParseException {
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
            toMilestoneProto(toProjectMilestoneDao(proto).orElseThrow(), Milestone::newBuilder)
                .orElseThrow()
                .build())
        .ignoringFields(Project.Milestone.STEPS_FIELD_NUMBER)
        .isEqualTo(proto);
  }

  @Test
  public void toProjectProtoNull() throws ParseException {
    assertThat(toProjectProto(null, true, Project::newBuilder)).isEmpty();
  }

  @Test
  public void toProjectDaoNull() throws ParseException {
    assertThat(toProjectDao(null)).isEmpty();
  }

  @Test
  public void toProjectProtoUninitialized() throws ParseException {
    assertThat(
            toProjectProto(
                newUninitialized(org.davincischools.leo.database.daos.Project.class),
                true,
                Project::newBuilder))
        .isEmpty();
  }

  @Test
  public void toProjectDaoUninitialized() throws ParseException {
    assertThat(toProjectDao(Project.getDefaultInstance())).isEmpty();
  }

  @Test
  public void toProjectRoundTrip() throws ParseException {
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
            toProjectProto(toProjectDao(proto).orElseThrow(), true, Project::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toTagProtoNull() throws ParseException {
    assertThat(toTagProto(null, Tag::newBuilder)).isEmpty();
  }

  @Test
  public void toTagDaoNull() throws ParseException {
    assertThat(toTagDao(null)).isEmpty();
  }

  @Test
  public void toTagProtoUninitialized() throws ParseException {
    assertThat(
            toTagProto(
                newUninitialized(org.davincischools.leo.database.daos.Tag.class), Tag::newBuilder))
        .isEmpty();
  }

  @Test
  public void toTagDaoUninitialized() throws ParseException {
    assertThat(toTagDao(Tag.getDefaultInstance())).isEmpty();
  }

  @Test
  public void toTagRoundTrip() throws ParseException {
    Tag proto =
        TextFormat.parse(
            """
                text: "text"
                user_x_id: 2
                """,
            Tag.class);

    assertThat(toTagProto(toTagDao(proto).orElseThrow(), Tag::newBuilder).orElseThrow().build())
        .isEqualTo(proto);
  }

  @Test
  public void toProjectPostCommentProtoNull() throws ParseException {
    assertThat(
            toProjectPostCommentProto(
                (org.davincischools.leo.database.daos.ProjectPostComment) null,
                ProjectPostComment::newBuilder))
        .isEmpty();
  }

  @Test
  public void toProjectPostCommentDaoNull() throws ParseException {
    assertThat(toProjectPostCommentDao(null)).isEmpty();
  }

  @Test
  public void toProjectPostCommentProtoUninitialized() throws ParseException {
    assertThat(
            toProjectPostCommentProto(
                newUninitialized(org.davincischools.leo.database.daos.ProjectPostComment.class),
                ProjectPostComment::newBuilder))
        .isEmpty();
  }

  @Test
  public void toProjectPostCommentDaoUninitialized() throws ParseException {
    assertThat(toProjectPostCommentDao(ProjectPostComment.getDefaultInstance())).isEmpty();
  }

  @Test
  public void toProjectPostCommentRoundTrip() throws ParseException {
    ProjectPostComment proto =
        TextFormat.parse(
            """
                id: 1
                user_x {
                  id: 2
                  is_admin_x: false
                  is_teacher: false
                  is_student: false
                  is_demo: true
                  is_authenticated: true
                }
                project_post {
                  id: 3
                  user_x {
                    id: 4
                    is_admin_x: false
                    is_teacher: false
                    is_student: false
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
            toProjectPostCommentProto(
                    toProjectPostCommentDao(proto).orElseThrow(), ProjectPostComment::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toProjectPostRatingProtoNull() {
    assertThat(toProjectPostRatingProto(null, ProjectPostRating::newBuilder)).isEmpty();
  }

  @Test
  public void toProjectPostRatingDaoNull() {
    assertThat(toProjectPostRatingDao(null)).isEmpty();
  }

  @Test
  public void toProjectPostRatingProtoUninitialized() {
    assertThat(
            toProjectPostRatingProto(
                newUninitialized(org.davincischools.leo.database.daos.ProjectPostRating.class),
                ProjectPostRating::newBuilder))
        .isEmpty();
  }

  @Test
  public void toProjectPostRatingDaoUninitialized() {
    assertThat(toProjectPostRatingDao(ProjectPostRating.getDefaultInstance())).isEmpty();
  }

  @Test
  public void toProjectPostRatingRoundTrip() throws ParseException {
    ProjectPostRating proto =
        TextFormat.parse(
            """
                id: 1
                user_x {
                  id: 2
                  is_admin_x: false
                  is_teacher: false
                  is_student: false
                  is_demo: true
                  is_authenticated: true
                }
                project_post {
                  id: 3
                  user_x {
                    id: 4
                    is_admin_x: false
                    is_teacher: false
                    is_student: false
                    is_demo: true
                    is_authenticated: true
                  }
                }
                knowledge_and_skill {
                  id: 5
                  name: "name_1"
                }
                knowledge_and_skill {
                  id: 6
                  name: "name_2"
                }
                rating: 1
                rating_type: INITIAL_1_TO_5
                project_input_fulfillment_id: 3
                """,
            ProjectPostRating.class);

    assertThat(
            toProjectPostRatingProto(
                    toProjectPostRatingDao(proto).orElseThrow(), ProjectPostRating::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toProjectInputCategoryProtoNull() {
    assertThat(toProjectInputCategoryProto(null, ProjectInputCategory::newBuilder)).isEmpty();
  }

  @Test
  public void toProjectInputCategoryDaoNull() {
    assertThat(toProjectDefinitionCategoryDao(null)).isEmpty();
  }

  @Test
  public void toProjectInputCategoryProtoUninitialized() {
    assertThat(
            toProjectInputCategoryProto(
                newUninitialized(
                    org.davincischools.leo.database.daos.ProjectDefinitionCategory.class),
                ProjectInputCategory::newBuilder))
        .isEmpty();
  }

  @Test
  public void toProjectInputCategoryDaoUninitialized() {
    assertThat(toProjectDefinitionCategoryDao(ProjectInputCategory.getDefaultInstance())).isEmpty();
  }

  @Test
  public void toProjectInputCategoryRoundTrip() throws ParseException {
    // Options are pulled in separately from this translation.
    ProjectInputCategory proto =
        TextFormat.parse(
            """
                id: 1
                type_id: 2
                name: 'name'
                short_descr: 'short_descr'
                input_descr: 'input_descr'
                hint: 'hint'
                placeholder: 'placeholder'
                value_type: EKS
                max_num_values: 3
                """,
            ProjectInputCategory.class);

    assertThat(
            toProjectInputCategoryProto(
                    toProjectDefinitionCategoryDao(proto).orElseThrow(),
                    ProjectInputCategory::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toProjectDefinitionProtoNull() {
    assertThat(
            toProjectDefinitionProto(
                (org.davincischools.leo.database.daos.ProjectDefinition) null,
                ProjectDefinition::newBuilder))
        .isEmpty();
  }

  @Test
  public void toProjectDefinitionDaoNull() {
    assertThat(toProjectDefinitionDao(null)).isEmpty();
  }

  @Test
  public void toProjectDefinitionProtoUninitialized() {
    assertThat(
            toProjectDefinitionProto(
                newUninitialized(org.davincischools.leo.database.daos.ProjectDefinition.class),
                ProjectDefinition::newBuilder))
        .isEmpty();
  }

  @Test
  public void toProjectDefinitionDaoUninitialized() {
    assertThat(toProjectDefinitionDao(ProjectDefinition.getDefaultInstance()));
  }

  @Test
  public void toProjectDefinitionRoundTrip() throws ParseException {
    // Options are pulled in separately from this translation.
    ProjectDefinition proto =
        TextFormat.parse(
            """
                id: 1
                name: 'name'
                template: true
                inputs {
                  category {
                    id: 4
                    type_id: 5
                    name: 'name'
                    short_descr: 'short_descr'
                    input_descr: 'input_descr'
                    hint: 'hint'
                    placeholder: 'placeholder'
                    value_type: EKS
                    max_num_values: 6
                  }
                }
                inputs {
                  category {
                    id: 8
                    type_id: 9
                    name: '7_name'
                    short_descr: '7_short_descr'
                    input_descr: '7_input_descr'
                    hint: '7_hint'
                    placeholder: '7_placeholder'
                    value_type: FREE_TEXT
                    max_num_values: 10
                  }
                }
                """,
            ProjectDefinition.class);

    assertThat(
            toProjectDefinitionProto(
                    toProjectDefinitionDao(proto).orElseThrow(), ProjectDefinition::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toProjectDefinitionProtoFromInputNull() {
    assertThat(
            toProjectDefinitionProto(
                (org.davincischools.leo.database.daos.ProjectInput) null,
                ProjectDefinition::newBuilder))
        .isEmpty();
  }

  @Test
  public void toProjectDefinitionDaoFromInputNull() {
    assertThat(
            toProjectDefinitionProto(
                (org.davincischools.leo.database.daos.ProjectInput) null,
                ProjectDefinition::newBuilder))
        .isEmpty();
  }

  @Test
  public void toProjectDefinitionProtoFromInputUninitialized() {
    assertThat(
            toProjectDefinitionProto(
                newUninitialized(org.davincischools.leo.database.daos.ProjectInput.class),
                ProjectDefinition::newBuilder))
        .isEmpty();
  }

  @Test
  public void toProjectDefinitionDaoFromInputUninitialized() {
    assertThat(toProjectDefinitionDao(ProjectDefinition.getDefaultInstance())).isEmpty();
  }

  @Test
  public void toProjectDefinitionFromInputRoundTript() throws ParseException {
    // Options are pulled in separately from the translation.
    ProjectDefinition proto =
        TextFormat.parse(
            """
                id: 1
                input_id: -1
                state: COMPLETED
                inputs {
                  category {
                    id: 3
                    type_id: 4
                    name: 'nameA'
                    short_descr: 'short_descrA'
                    input_descr: 'input_descrA'
                    hint: 'hintA'
                    placeholder: 'placeholderA'
                    value_type: EKS
                    max_num_values: 5
                  }
                  selected_ids: 6
                  selected_ids: 7
                }
                inputs {
                  category {
                    id: 9
                    type_id: 10
                    name: 'nameB'
                    short_descr: 'short_descrB'
                    input_descr: 'input_descrB'
                    hint: 'hintB'
                    placeholder: 'placeholderB'
                    value_type: MOTIVATION
                    max_num_values: 11
                  }
                  selected_ids: 12
                  selected_ids: 13
                }
                inputs {
                  category {
                    id: 15
                    type_id: 16
                    name: 'nameC'
                    short_descr: 'short_descrC'
                    input_descr: 'input_descrC'
                    hint: 'hintC'
                    placeholder: 'placeholderC'
                    value_type: FREE_TEXT
                    max_num_values: 17
                  }
                  free_texts: "free_textA"
                  free_texts: "free_textB"
                }
                assignment: {
                  id: 18
                  name: "assignment name"
                  short_descr: "assignment short"
                  long_descr_html: "assignment long"
                }
                """,
            ProjectDefinition.class);

    assertThat(
            toProjectDefinitionProto(
                    toProjectInputDao(proto).orElseThrow(), ProjectDefinition::newBuilder)
                .orElseThrow()
                .build())
        .isEqualTo(proto);
  }

  public void toOptionProtoFromKnowledgeAndSkillNull() {
    assertThat(
            toOptionProto(
                (org.davincischools.leo.database.daos.KnowledgeAndSkill) null, Option::newBuilder))
        .isEmpty();
  }

  @Test
  public void toOptionProtoFromKnowledgeAndSkillUninitialized() {
    assertThat(
            toOptionProto(
                newUninitialized(org.davincischools.leo.database.daos.KnowledgeAndSkill.class),
                Option::newBuilder))
        .isEmpty();
  }

  @Test
  public void toOptionProtoFromKnowledgeAndSkillDao() throws ParseException {
    var dao =
        new org.davincischools.leo.database.daos.KnowledgeAndSkill()
            .setId(1)
            .setCreationTime(Instant.MIN)
            .setName("name")
            .setCategory("category")
            .setType(ValueType.EKS.name())
            .setShortDescr("short_descr")
            .setLongDescrHtml("long_descr_html")
            .setUserX(new org.davincischools.leo.database.daos.UserX().setId(2))
            .setGlobal(true);
    assertThat(toOptionProto(dao, Option::newBuilder).orElseThrow().build())
        .isEqualTo(
            TextFormat.parse(
                """
                id: 1
                name: 'name'
                category: 'category'
                short_descr: 'short_descr'
                user_x {
                  id: 2
                  is_admin_x: false
                  is_teacher: false
                  is_student: false
                  is_demo: true
                  is_authenticated: true
                }
                """,
                Option.class));
  }

  public void toOptionProtoFromMotivationNull() {
    assertThat(
            toOptionProto(
                (org.davincischools.leo.database.daos.Motivation) null, Option::newBuilder))
        .isEmpty();
  }

  @Test
  public void toOptionProtoFromMotivationUninitialized() {
    assertThat(
            toOptionProto(
                newUninitialized(org.davincischools.leo.database.daos.Motivation.class),
                Option::newBuilder))
        .isEmpty();
  }

  @Test
  public void toOptionProtoFromMotivationDao() throws ParseException {
    var dao =
        new org.davincischools.leo.database.daos.Motivation()
            .setId(1)
            .setCreationTime(Instant.MIN)
            .setName("name")
            .setShortDescr("short_descr")
            .setLongDescrHtml("long_descr_html");
    assertThat(toOptionProto(dao, Option::newBuilder).orElseThrow().build())
        .isEqualTo(
            TextFormat.parse(
                """
                id: 1
                name: 'name'
                short_descr: 'short_descr'
                """,
                Option.class));
  }

  private <T> T newUninitialized(Class<T> entityClass) {
    return entityManager.getReference(entityClass, 0);
  }
}
