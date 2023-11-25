package org.davincischools.leo.database.utils.repos;

import static com.google.common.truth.Truth.assertThat;
import static java.util.stream.Collectors.toSet;
import static org.davincischools.leo.database.utils.DaoUtils.listIfInitialized;
import static org.davincischools.leo.database.utils.DaoUtils.streamIfInitialized;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.AssignmentKnowledgeAndSkill;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.ClassXKnowledgeAndSkill;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.test.TestData;
import org.davincischools.leo.database.test.TestDatabase;
import org.davincischools.leo.database.utils.Database;
import org.hibernate.Hibernate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClassXRepositoryTest.TestApplicationConfiguration.class)
public class ClassXRepositoryTest {

  @Configuration
  @ComponentScan(basePackageClasses = {TestDatabase.class, Database.class})
  @EnableAutoConfiguration
  public static class TestApplicationConfiguration {}

  @Autowired private Database db;
  @Autowired private TestData testData;

  @Before
  public void setup() {
    testData.addTestData();
  }

  @Test
  public void getClassXsTest() {
    var classXs = db.getClassXRepository().getClassXs(new GetClassXsParams());

    assertThat(classXs.stream().map(ClassX::getId).toList())
        .containsExactly(
            testData.getProgrammingClassX().getId(),
            testData.getChemistryClassX().getId(),
            testData.getDanceClassX().getId());

    assertThat(classXs.stream().map(ClassX::getSchool).map(Hibernate::isInitialized).toList())
        .containsExactly(false, false, false);
    assertThat(
            classXs.stream()
                .map(ClassX::getClassXKnowledgeAndSkills)
                .map(Hibernate::isInitialized)
                .toList())
        .containsExactly(false, false, false);
    assertThat(classXs.stream().map(ClassX::getAssignments).map(Hibernate::isInitialized).toList())
        .containsExactly(false, false, false);
  }

  @Test
  public void getClassXsIncludeAssignmentsTest() {
    var classXs =
        db.getClassXRepository()
            .getClassXs(new GetClassXsParams().setIncludeAssignments(new GetAssignmentsParams()));

    assertThat(
            classXs.stream()
                .map(
                    classX ->
                        List.of(
                            classX.getName(),
                            streamIfInitialized(classX.getAssignments())
                                .map(Assignment::getName)
                                .collect(toSet())))
                .collect(toSet()))
        .containsExactly(
            List.of(
                testData.getProgrammingClassX().getName(),
                Set.of(
                    testData.getProgrammingContainerAssignment().getName(),
                    testData.getProgrammingSortAssignment().getName())),
            List.of(
                testData.getChemistryClassX().getName(),
                Set.of(
                    testData.getChemistryValenceElectronsAssignment().getName(),
                    testData.getChemistryPeriodicTableAssignment().getName())),
            List.of(testData.getDanceClassX().getName(), Set.of()));
  }

  @Test
  public void getClassXsIncludeKnowledgeAndSkillTest() {
    var classXs =
        db.getClassXRepository()
            .getClassXs(new GetClassXsParams().setIncludeKnowledgeAndSkills(true));

    assertThat(
            classXs.stream()
                .map(
                    classX ->
                        List.of(
                            classX.getName(),
                            streamIfInitialized(classX.getClassXKnowledgeAndSkills())
                                .map(ClassXKnowledgeAndSkill::getKnowledgeAndSkill)
                                .map(KnowledgeAndSkill::getName)
                                .collect(toSet())))
                .collect(toSet()))
        .containsExactly(
            List.of(
                testData.getProgrammingClassX().getName(),
                Set.of(
                    testData.getProgrammingContainerEks().getName(),
                    testData.getProgrammingSortEks().getName())),
            List.of(
                testData.getChemistryClassX().getName(),
                Set.of(
                    testData.getChemistryValenceElectronsEks().getName(),
                    testData.getChemistryPeriodicTableEks().getName())),
            List.of(testData.getDanceClassX().getName(), Set.of()));
  }

  @Test
  public void getClassXsIncludeAssignmentsAndKnowledgeAndSkillTest() {
    var classXs =
        db.getClassXRepository()
            .getClassXs(
                new GetClassXsParams()
                    .setIncludeAssignments(
                        new GetAssignmentsParams().setIncludeKnowledgeAndSkills(true))
                    .setIncludeKnowledgeAndSkills(true));

    assertThat(
            classXs.stream()
                .flatMap(
                    classX -> {
                      if (listIfInitialized(classX.getAssignments()).isEmpty()) {
                        return Stream.of(List.of(classX.getName(), "", Set.of()));
                      } else {
                        return streamIfInitialized(classX.getAssignments())
                            .map(
                                assignment ->
                                    List.of(
                                        classX.getName(),
                                        assignment.getName(),
                                        assignment.getAssignmentKnowledgeAndSkills().stream()
                                            .map(AssignmentKnowledgeAndSkill::getKnowledgeAndSkill)
                                            .map(KnowledgeAndSkill::getName)
                                            .collect(toSet())));
                      }
                    })
                .collect(toSet()))
        .containsExactly(
            List.of(
                testData.getProgrammingClassX().getName(),
                testData.getProgrammingContainerAssignment().getName(),
                Set.of(testData.getProgrammingContainerEks().getName())),
            List.of(
                testData.getProgrammingClassX().getName(),
                testData.getProgrammingSortAssignment().getName(),
                Set.of(testData.getProgrammingSortEks().getName())),
            List.of(
                testData.getChemistryClassX().getName(),
                testData.getChemistryValenceElectronsAssignment().getName(),
                Set.of(testData.getChemistryValenceElectronsEks().getName())),
            List.of(
                testData.getChemistryClassX().getName(),
                testData.getChemistryPeriodicTableAssignment().getName(),
                Set.of(testData.getChemistryPeriodicTableEks().getName())),
            List.of(testData.getDanceClassX().getName(), "", Set.of()));
  }

  @Test
  public void getClassXsIncludeSchoolTest() {
    var classXs =
        db.getClassXRepository().getClassXs(new GetClassXsParams().setIncludeSchool(true));

    assertThat(classXs.stream().map(ClassX::getSchool).map(School::getId).toList())
        .containsExactly(
            testData.getSchool().getId(),
            testData.getSchool().getId(),
            testData.getSchool().getId());
  }
}
