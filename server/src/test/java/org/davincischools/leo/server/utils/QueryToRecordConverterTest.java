package org.davincischools.leo.server.utils;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.davincischools.leo.database.test.TestData;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.server.ServerApplication;
import org.junit.Before;
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
public class QueryToRecordConverterTest {

  @Autowired private Database db;
  @Autowired private TestData testData;

  @BeforeClass
  public static void setupAll() {
    ((DefaultConversionService) DefaultConversionService.getSharedInstance())
        .addConverter(new QueryWithNullsToRecordConverter());
  }

  @Before
  public void setup() {
    testData.addTestData();
  }

  @Test
  public void queryToRecordTest() {
    var assignments =
        ImmutableList.copyOf(
            db.getAssignmentRepository()
                .findAllByTeacherId(testData.getTeacher().getTeacher().getId()));
    assertThat(assignments).hasSize(3);
    assertThat(
            assignments.stream()
                .map(
                    a ->
                        ImmutableList.of(
                            a.classX().getName(),
                            a.assignments().stream()
                                .map(e -> e.assignment().getName())
                                .collect(ImmutableSet.toImmutableSet())))
                .toList())
        .containsExactly(
            ImmutableList.of(
                testData.getProgrammingClassX().getName(),
                ImmutableSet.of(
                    testData.getProgrammingContainerAssignment().getName(),
                    testData.getProgrammingSortAssignment().getName())),
            ImmutableList.of(
                testData.getChemistryClassX().getName(),
                ImmutableSet.of(
                    testData.getChemistryValenceElectronsAssignment().getName(),
                    testData.getChemistryPeriodicTableAssignment().getName())),
            ImmutableList.of(testData.getDanceClassX().getName(), ImmutableSet.of()));
  }
}
