package org.davincischools.leo.server.utils;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import org.davincischools.leo.database.test.TestData;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.server.ServerApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {ServerApplication.class, TestData.class})
public class QueryToRecordConverterTest {
  @Autowired private Database db;
  @Autowired private TestData testData;

  @BeforeEach
  public void setup() {
    testData.addTestData();
  }

  @Test
  public void queryToRecordTest() {
    var assignments =
        ImmutableList.copyOf(
            db.getAssignmentRepository()
                .findAllByTeacherId(testData.getTeacher().getTeacher().getId()));
    assertThat(assignments).hasSize(5);
    assertThat(
            assignments.stream()
                .map(
                    a ->
                        ImmutableList.of(
                            a.getClassX().getName(),
                            a.getAssignment() != null ? a.getAssignment().getName() : "[null]"))
                .toList())
        .containsAtLeast(
            ImmutableList.of(
                testData.getChemistryClass().getName(),
                testData.getChemistryValenceElectronsAssignment().getName()),
            ImmutableList.of(testData.getDanceClass().getName(), "[null]"));
  }
}
