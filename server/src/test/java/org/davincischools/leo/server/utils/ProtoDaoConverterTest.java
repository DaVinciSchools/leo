package org.davincischools.leo.server.utils;

import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;

import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.ParseException;
import jakarta.persistence.EntityManager;
import org.davincischools.leo.protos.pl_types.Assignment;
import org.davincischools.leo.protos.pl_types.ClassX;
import org.davincischools.leo.protos.pl_types.District;
import org.davincischools.leo.protos.pl_types.KnowledgeAndSkill;
import org.davincischools.leo.protos.pl_types.School;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProtoDaoConverterTest {

  @Autowired EntityManager entityManager;

  @Test
  void toAssignmentConvertersNull() throws ParseException {
    assertThat(ProtoDaoConverter.toAssignmentProto(null, null).build())
        .isEqualTo(Assignment.getDefaultInstance());
  }

  @Test
  void toAssignmentConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoConverter.toAssignmentProto(
                    uninitialized(org.davincischools.leo.database.daos.Assignment.class), null)
                .build())
        .isEqualTo(Assignment.newBuilder().setId(0).build());
  }

  @Test
  void toAssignmentConvertersEmpty() throws ParseException {
    Assignment proto = Assignment.getDefaultInstance();
    assertThat(
            ProtoDaoConverter.toAssignmentProto(ProtoDaoConverter.toAssignmentDao(proto), null)
                .build())
        .isEqualTo(proto);
  }

  @Test
  void toAssignmentConvertersAll() throws ParseException {
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
  void toDistrictConvertersNull() throws ParseException {
    assertThat(ProtoDaoConverter.toDistrictProto(null, null).build())
        .isEqualTo(District.getDefaultInstance());
  }

  @Test
  void toDistrictConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoConverter.toDistrictProto(
                    uninitialized(org.davincischools.leo.database.daos.District.class), null)
                .build())
        .isEqualTo(District.newBuilder().setId(0).build());
  }

  @Test
  void toDistrictConvertersEmpty() throws ParseException {
    District proto = District.getDefaultInstance();
    assertThat(
            ProtoDaoConverter.toDistrictProto(ProtoDaoConverter.toDistrictDao(proto), null).build())
        .isEqualTo(proto);
  }

  @Test
  void toDistrictConvertersAll() throws ParseException {
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
  void toClassXConvertersNull() throws ParseException {
    assertThat(ProtoDaoConverter.toClassXProto(null, null).build())
        .isEqualTo(ClassX.getDefaultInstance());
  }

  @Test
  void toClassXConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoConverter.toClassXProto(
                    uninitialized(org.davincischools.leo.database.daos.ClassX.class), null)
                .build())
        .isEqualTo(ClassX.newBuilder().setId(0).build());
  }

  @Test
  void toClassXConvertersEmpty() throws ParseException {
    ClassX proto = ClassX.getDefaultInstance();
    assertThat(ProtoDaoConverter.toClassXProto(ProtoDaoConverter.toClassXDao(proto), null).build())
        .ignoringFieldDescriptors(
            ClassX.getDescriptor().findFieldByNumber(ClassX.KNOWLEDGE_AND_SKILLS_FIELD_NUMBER))
        .isEqualTo(proto);
  }

  @Test
  void toClassXConvertersAll() throws ParseException {
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
  void toFullClassXConverters() throws ParseException {
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
  void toKnowledgeAndSkillConvertersNull() throws ParseException {
    assertThat(ProtoDaoConverter.toKnowledgeAndSkillProto(null, null).build())
        .isEqualTo(KnowledgeAndSkill.getDefaultInstance());
  }

  @Test
  void toKnowledgeAndSkillConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoConverter.toKnowledgeAndSkillProto(
                    uninitialized(org.davincischools.leo.database.daos.KnowledgeAndSkill.class),
                    null)
                .build())
        .isEqualTo(KnowledgeAndSkill.newBuilder().setId(0).build());
  }

  @Test
  void toKnowledgeAndSkillConvertersEmpty() throws ParseException {
    KnowledgeAndSkill proto = KnowledgeAndSkill.getDefaultInstance();
    assertThat(
            ProtoDaoConverter.toKnowledgeAndSkillProto(
                    ProtoDaoConverter.toKnowledgeAndSkillDao(proto), null)
                .build())
        .isEqualTo(proto);
  }

  @Test
  void toKnowledgeAndSkillConvertersAll() throws ParseException {
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
  void toSchoolConvertersNull() throws ParseException {
    assertThat(ProtoDaoConverter.toSchoolProto(null, null).build())
        .isEqualTo(School.getDefaultInstance());
  }

  @Test
  void toSchoolConvertersUninitialized() throws ParseException {
    assertThat(
            ProtoDaoConverter.toSchoolProto(
                    uninitialized(org.davincischools.leo.database.daos.School.class), null)
                .build())
        .isEqualTo(School.newBuilder().setId(0).build());
  }

  @Test
  void toSchoolConvertersEmpty() throws ParseException {
    School proto = TextFormat.parse("""
                id: 1
                """, School.class);

    assertThat(ProtoDaoConverter.toSchoolProto(ProtoDaoConverter.toSchoolDao(proto), null).build())
        .isEqualTo(proto);
  }

  @Test
  void toSchoolConvertersAll() throws ParseException {
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

  private <T> T uninitialized(Class<T> entityClass) {
    return entityManager.getReference(entityClass, 0);
  }
}
