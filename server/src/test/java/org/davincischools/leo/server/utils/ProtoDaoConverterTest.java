package org.davincischools.leo.server.utils;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.ParseException;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import org.davincischools.leo.database.test.TestData;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.protos.pl_types.Assignment;
import org.davincischools.leo.protos.pl_types.ClassX;
import org.davincischools.leo.protos.pl_types.District;
import org.davincischools.leo.protos.pl_types.KnowledgeAndSkill;
import org.davincischools.leo.protos.pl_types.Project;
import org.davincischools.leo.protos.pl_types.ProjectPost;
import org.davincischools.leo.protos.pl_types.School;
import org.davincischools.leo.protos.pl_types.UserX;
import org.davincischools.leo.protos.user_x_management.RegisterUserXRequest;
import org.davincischools.leo.server.ServerApplication;
import org.hibernate.LazyInitializationException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {ServerApplication.class, TestData.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class ProtoDaoConverterTest {

  @BeforeClass
  public static void initialSetup() {
    ((DefaultConversionService) DefaultConversionService.getSharedInstance())
        .addConverter(new QueryWithNullsToRecordConverter());
  }

  @Autowired EntityManager entityManager;
  @Autowired Database db;

  @Test
  public void toAssignmentConvertersNull() throws ParseException {
    assertThat(ProtoDaoConverter.toAssignmentProto(null, null).build())
        .isEqualTo(Assignment.getDefaultInstance());
  }

  @Test
  public void toAssignmentConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoConverter.toAssignmentProto(
                    newUninitialized(org.davincischools.leo.database.daos.Assignment.class), null)
                .build())
        .isEqualTo(Assignment.getDefaultInstance());
  }

  @Test
  public void toAssignmentConvertersEmpty() throws ParseException {
    Assignment proto = Assignment.getDefaultInstance();
    assertThat(
            ProtoDaoConverter.toAssignmentProto(ProtoDaoConverter.toAssignmentDao(proto), null)
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
            short_descr: "short"
            long_descr_html: "long"
            class_x {
              id: 2
              name: "class name"
            }
            """,
            Assignment.class);

    assertThat(
            ProtoDaoConverter.toAssignmentProto(ProtoDaoConverter.toAssignmentDao(proto), null)
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toDistrictConvertersNull() throws ParseException {
    assertThat(ProtoDaoConverter.toDistrictProto(null, null).build())
        .isEqualTo(District.getDefaultInstance());
  }

  @Test
  public void toDistrictConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoConverter.toDistrictProto(
                    newUninitialized(org.davincischools.leo.database.daos.District.class), null)
                .build())
        .isEqualTo(District.getDefaultInstance());
  }

  @Test
  public void toDistrictConvertersEmpty() throws ParseException {
    District proto = District.getDefaultInstance();
    assertThat(
            ProtoDaoConverter.toDistrictProto(ProtoDaoConverter.toDistrictDao(proto), null).build())
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
            ProtoDaoConverter.toDistrictProto(ProtoDaoConverter.toDistrictDao(proto), null).build())
        .isEqualTo(proto);
  }

  @Test
  public void toClassXConvertersNull() throws ParseException {
    assertThat(ProtoDaoConverter.toClassXProto(null, null).build())
        .isEqualTo(ClassX.getDefaultInstance());
  }

  @Test
  public void toClassXConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoConverter.toClassXProto(
                    newUninitialized(org.davincischools.leo.database.daos.ClassX.class), null)
                .build())
        .isEqualTo(ClassX.getDefaultInstance());
  }

  @Test
  public void toClassXConvertersEmpty() throws ParseException {
    ClassX proto = ClassX.getDefaultInstance();
    assertThat(ProtoDaoConverter.toClassXProto(ProtoDaoConverter.toClassXDao(proto), null).build())
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

    assertThat(ProtoDaoConverter.toClassXProto(ProtoDaoConverter.toClassXDao(proto), null).build())
        .ignoringFieldDescriptors(
            ClassX.getDescriptor().findFieldByNumber(ClassX.KNOWLEDGE_AND_SKILLS_FIELD_NUMBER))
        .isEqualTo(proto);
  }

  @Test
  public void toFullClassXConverters() throws ParseException {
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
                school: {
                  id: 2
                  name: "school 2 name"
                }
                knowledge_and_skills {
                  id: 3
                  name: "knowledge and skill 3 name"
                }
                knowledge_and_skills {
                  id: 4
                  name: "knowledge and skill 4 name"
                }
                """,
            ClassX.class);

    assertThat(
            ProtoDaoConverter.toFullClassXProto(ProtoDaoConverter.toFullClassXRecord(proto), null)
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toKnowledgeAndSkillConvertersNull() throws ParseException {
    assertThat(ProtoDaoConverter.toKnowledgeAndSkillProto(null, null).build())
        .isEqualTo(KnowledgeAndSkill.getDefaultInstance());
  }

  @Test
  public void toKnowledgeAndSkillConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoConverter.toKnowledgeAndSkillProto(
                    newUninitialized(org.davincischools.leo.database.daos.KnowledgeAndSkill.class),
                    null)
                .build())
        .isEqualTo(KnowledgeAndSkill.getDefaultInstance());
  }

  @Test
  public void toKnowledgeAndSkillConvertersEmpty() throws ParseException {
    KnowledgeAndSkill proto = KnowledgeAndSkill.getDefaultInstance();
    assertThat(
            ProtoDaoConverter.toKnowledgeAndSkillProto(
                    ProtoDaoConverter.toKnowledgeAndSkillDao(proto), null)
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
                user_x_id: 9
                """,
            KnowledgeAndSkill.class);

    assertThat(
            ProtoDaoConverter.toKnowledgeAndSkillProto(
                    ProtoDaoConverter.toKnowledgeAndSkillDao(proto), null)
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toSchoolConvertersNull() throws ParseException {
    assertThat(ProtoDaoConverter.toSchoolProto(null, null).build())
        .isEqualTo(School.getDefaultInstance());
  }

  @Test
  public void toSchoolConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoConverter.toSchoolProto(
                    newUninitialized(org.davincischools.leo.database.daos.School.class), null)
                .build())
        .isEqualTo(School.getDefaultInstance());
  }

  @Test
  public void toSchoolConvertersEmpty() throws ParseException {
    School proto = TextFormat.parse("""
                id: 1
                """, School.class);

    assertThat(ProtoDaoConverter.toSchoolProto(ProtoDaoConverter.toSchoolDao(proto), null).build())
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

    assertThat(ProtoDaoConverter.toSchoolProto(ProtoDaoConverter.toSchoolDao(proto), null).build())
        .isEqualTo(proto);
  }

  @Test
  public void toUserXConvertersNull() throws ParseException {
    assertThat(ProtoDaoConverter.toUserXProto(null, null).build())
        .isEqualTo(UserX.getDefaultInstance());
  }

  @Test
  public void toUserXConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoConverter.toUserXProto(
                    newUninitialized(org.davincischools.leo.database.daos.UserX.class), null)
                .build())
        .isEqualTo(UserX.getDefaultInstance());
  }

  @Test
  public void toUserXConvertersJustId() throws ParseException {
    UserX proto =
        TextFormat.parse(
            """
                user_x_id: 1
                is_demo: true
                is_authenticated: true
                """,
            UserX.class);

    assertThat(ProtoDaoConverter.toUserXProto(ProtoDaoConverter.toUserXDao(proto), null).build())
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
            ProtoDaoConverter.toUserXProto(
                    ProtoDaoConverter.toUserXDao(UserX.getDefaultInstance()), null)
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toUserXConvertersAllNonDemo() throws ParseException {
    UserX proto =
        TextFormat.parse(
            """
                user_x_id: 1
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

    assertThat(ProtoDaoConverter.toUserXProto(ProtoDaoConverter.toUserXDao(proto), null).build())
        .isEqualTo(proto);
  }

  @Test
  public void toUserXConvertersAllDemo() throws ParseException {
    UserX proto =
        TextFormat.parse(
            """
                user_x_id: 1
                district_id: 2
                first_name: "first"
                last_name: "last"
                email_address: "email"
                is_demo: true
                is_authenticated: true
                """,
            UserX.class);

    assertThat(ProtoDaoConverter.toUserXProto(ProtoDaoConverter.toUserXDao(proto), null).build())
        .isEqualTo(proto);
  }

  @Test
  public void toProjectPostConvertersNull() throws ParseException {
    assertThat(ProtoDaoConverter.toProjectPostProto(null, null).build())
        .isEqualTo(ProjectPost.getDefaultInstance());
  }

  @Test
  public void toProjectPostConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoConverter.toProjectPostProto(
                    newUninitialized(org.davincischools.leo.database.daos.ProjectPost.class), null)
                .build())
        .isEqualTo(ProjectPost.getDefaultInstance());
  }

  @Test
  public void toProjectPostConvertersEmpty() throws ParseException {
    ProjectPost proto = ProjectPost.getDefaultInstance();

    assertThat(
            ProtoDaoConverter.toProjectPostProto(ProtoDaoConverter.toProjectPostDao(proto), null)
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
                  user_x_id: 2
                  is_demo: true
                  is_authenticated: true
                }
                name: "name"
                long_descr_html: "long_descr_html"
                desired_feedback: "desired_feedback"
                tags: "tag1"
                tags: "tag2"
                tags: "tag3"
                post_time_ms: 3
                """,
            ProjectPost.class);

    assertThat(
            ProtoDaoConverter.toProjectPostProto(ProtoDaoConverter.toProjectPostDao(proto), null)
                .build())
        .ignoringFields(ProjectPost.TAGS_FIELD_NUMBER)
        .isEqualTo(proto);
  }

  @Test
  public void toInterestConvertersNull() throws ParseException {
    assertThat(ProtoDaoConverter.toRegisterUserXRequestProto(null, null).build())
        .isEqualTo(RegisterUserXRequest.getDefaultInstance());
  }

  @Test
  public void toInterestConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoConverter.toRegisterUserXRequestProto(
                    newUninitialized(org.davincischools.leo.database.daos.Interest.class), null)
                .build())
        .isEqualTo(RegisterUserXRequest.getDefaultInstance());
  }

  @Test
  public void toInterestConvertersEmpty() throws ParseException {
    RegisterUserXRequest proto = RegisterUserXRequest.getDefaultInstance();

    assertThat(
            ProtoDaoConverter.toRegisterUserXRequestProto(
                    ProtoDaoConverter.toInterestDao(proto), null)
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
            ProtoDaoConverter.toRegisterUserXRequestProto(
                    ProtoDaoConverter.toInterestDao(proto), null)
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
    assertThat(ProtoDaoConverter.toMilestoneStepProto(null, null).build())
        .isEqualTo(Project.Milestone.Step.getDefaultInstance());
  }

  @Test
  public void toProjectMilestoneStepConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoConverter.toMilestoneStepProto(
                    newUninitialized(
                        org.davincischools.leo.database.daos.ProjectMilestoneStep.class),
                    null)
                .build())
        .isEqualTo(Project.Milestone.Step.getDefaultInstance());
  }

  @Test
  public void toProjectMilestoneStepConvertersEmpty() throws ParseException {
    Project.Milestone.Step proto = Project.Milestone.Step.getDefaultInstance();

    assertThat(
            ProtoDaoConverter.toMilestoneStepProto(
                    ProtoDaoConverter.toProjectMilestoneStepDao(proto), null)
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
            ProtoDaoConverter.toMilestoneStepProto(
                    ProtoDaoConverter.toProjectMilestoneStepDao(proto), null)
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toProjectMilestoneConvertersNull() throws ParseException {
    assertThat(
            ProtoDaoConverter.toMilestoneProto(
                    (org.davincischools.leo.database.daos.ProjectMilestone) null, null)
                .build())
        .isEqualTo(Project.Milestone.getDefaultInstance());
  }

  @Test
  public void toProjectMilestoneConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoConverter.toMilestoneProto(
                    newUninitialized(org.davincischools.leo.database.daos.ProjectMilestone.class),
                    null)
                .build())
        .isEqualTo(Project.Milestone.getDefaultInstance());
  }

  @Test
  public void toProjectMilestoneConvertersEmpty() throws ParseException {
    Project.Milestone proto = Project.Milestone.getDefaultInstance();

    assertThat(
            ProtoDaoConverter.toMilestoneProto(ProtoDaoConverter.toProjectMilestoneDao(proto), null)
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
            ProtoDaoConverter.toMilestoneProto(ProtoDaoConverter.toProjectMilestoneDao(proto), null)
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
            ProtoDaoConverter.toMilestoneProto(
                    ProtoDaoConverter.toMilestoneWithStepsRecord(proto), null)
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void toProjectConvertersNull() throws ParseException {
    assertThat(
            ProtoDaoConverter.toProjectProto(
                    (org.davincischools.leo.database.daos.Project) null, null)
                .build())
        .isEqualTo(Project.getDefaultInstance());
  }

  @Test
  public void toProjectConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoConverter.toProjectProto(
                    newUninitialized(org.davincischools.leo.database.daos.Project.class), null)
                .build())
        .isEqualTo(Project.getDefaultInstance());
  }

  @Test
  public void toProjectConvertersEmpty() throws ParseException {
    Project proto = Project.getDefaultInstance();

    assertThat(
            ProtoDaoConverter.toProjectProto(ProtoDaoConverter.toProjectDao(proto), null).build())
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
            ProtoDaoConverter.toProjectProto(ProtoDaoConverter.toProjectDao(proto), null).build())
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
            ProtoDaoConverter.toProjectProto(ProtoDaoConverter.toProjectDao(proto), null).build())
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
            ProtoDaoConverter.toProjectProto(
                    ProtoDaoConverter.toProjectWithMilestonesRecord(proto), null)
                .build())
        .isEqualTo(proto);
  }

  @Test
  public void removeTransientValuesUnusedNoId() throws Exception {
    Exception e =
        assertThrows(
            Exception.class,
            () ->
                db.getClassXRepository()
                    .save(
                        new org.davincischools.leo.database.daos.ClassX()
                            .setCreationTime(Instant.now())
                            .setNumber("TEST101")
                            .setName("Test Class")
                            .setSchool(new org.davincischools.leo.database.daos.School())));
    assertThat(e.getMessage()).contains("unsaved transient instance");
  }

  @Test
  public void removeTransientValuesNull() throws Exception {
    org.davincischools.leo.database.daos.ClassX classX =
        db.getClassXRepository()
            .save(
                ProtoDaoConverter.removeTransientValues(
                    new org.davincischools.leo.database.daos.ClassX()
                        .setCreationTime(Instant.now())
                        .setNumber("TEST101")
                        .setName("Test Class")
                        .setSchool(null)));
    assertThat(classX.getSchool()).isNull();
  }

  @Test
  public void removeTransientValuesNoId() throws Exception {
    org.davincischools.leo.database.daos.ClassX classX =
        db.getClassXRepository()
            .save(
                ProtoDaoConverter.removeTransientValues(
                    new org.davincischools.leo.database.daos.ClassX()
                        .setCreationTime(Instant.now())
                        .setNumber("TEST101")
                        .setName("Test Class")
                        .setSchool(new org.davincischools.leo.database.daos.School())));
    assertThat(classX.getSchool()).isNull();
  }

  @Test
  public void removeTransientValuesOtherValues() throws Exception {
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
                """,
            ClassX.class);

    org.davincischools.leo.database.daos.ClassX classX =
        db.getClassXRepository()
            .save(ProtoDaoConverter.removeTransientValues(ProtoDaoConverter.toClassXDao(proto)));
    assertThat(classX.getSchool().getId()).isEqualTo(2);
    assertThrows(LazyInitializationException.class, () -> classX.getSchool().getName());
  }

  private <T> T newUninitialized(Class<T> entityClass) {
    return entityManager.getReference(entityClass, 0);
  }
}
