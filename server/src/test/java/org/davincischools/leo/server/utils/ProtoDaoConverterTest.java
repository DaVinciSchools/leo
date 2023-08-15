package org.davincischools.leo.server.utils;

import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;

import com.google.common.truth.extensions.proto.ProtoTruth;
import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.ParseException;
import java.time.Instant;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.protos.pl_types.ClassX;
import org.davincischools.leo.protos.pl_types.KnowledgeAndSkill;
import org.junit.jupiter.api.Test;

class ProtoDaoConverterTest {

  void toAssignmentConverters() throws ParseException {
    ProtoTruth.assertThat(
            ProtoDaoConverter.toAssignmentProto(
                    new Assignment()
                        .setCreationTime(Instant.now())
                        .setId(1)
                        .setName("assignment name")
                        .setShortDescr("assignment short")
                        .setLongDescrHtml("assignment long")
                        .setClassX(
                            new org.davincischools.leo.database.daos.ClassX()
                                .setCreationTime(Instant.now())
                                .setId(2)
                                .setNumber("class number")
                                .setName("class name")
                                .setShortDescr("class short")
                                .setLongDescrHtml("class long")
                                .setGrade("class grade")
                                .setPeriod("class period")),
                    null)
                .build())
        .isEqualTo(
            """
                id: 1
                name: "assignment name"
                short_descr: "assignment short"
                long_descr_html: "assignment long"
                class_x {
                  id: 2
                  number: "class number"
                  name: "class name"
                  short_descr: "class short"
                  long_descr_html: "class long"
                  grade: "class grade"
                  period: "class period"
                }
                """);
  }

  @Test
  void toClassXConverters() throws ParseException {
    ClassX proto =
        TextFormat.parse(
            """
                id: 1
                name: "2"
                number: "3"
                period: "4"
                grade: "5"
                short_descr: "6"
                long_descr_html: "7"
                knowledge_and_skills {
                  id: 8
                }
                knowledge_and_skills {
                  id: 9
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
                name: "2"
                number: "3"
                period: "4"
                grade: "5"
                short_descr: "6"
                long_descr_html: "7"
                knowledge_and_skills {
                  id: 8
                }
                knowledge_and_skills {
                  id: 9
                }
                """,
            ClassX.class);

    assertThat(
            ProtoDaoConverter.toFullClassXProto(ProtoDaoConverter.toFullClassXRecord(proto), null)
                .build())
        .isEqualTo(proto);
  }

  @Test
  void toKnowledgeAndSkillConverters() throws ParseException {
    KnowledgeAndSkill proto =
        TextFormat.parse(
            """
                id: 1
                type: EKS
                name: "4"
                category: "6"
                short_descr: "7"
                long_descr_html: "8"
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
}
