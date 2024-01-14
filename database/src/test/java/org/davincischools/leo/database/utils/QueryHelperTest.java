package org.davincischools.leo.database.utils;

import static com.google.common.truth.Truth.assertThat;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.JoinType;
import org.davincischools.leo.database.daos.AdminX;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.ClassX_;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.TeacherClassX;
import org.davincischools.leo.database.daos.TeacherClassX_;
import org.davincischools.leo.database.daos.Teacher_;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.daos.UserX_;
import org.davincischools.leo.database.test.TestData;
import org.davincischools.leo.database.test.TestDatabase;
import org.davincischools.leo.database.utils.query_helper.Entity;
import org.davincischools.leo.database.utils.query_helper.Predicate;
import org.davincischools.leo.database.utils.query_helper.QueryHelper;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.shaded.com.google.common.collect.ImmutableList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QueryHelperTest.TestApplicationConfiguration.class)
public class QueryHelperTest {

  @Configuration
  @ComponentScan(basePackageClasses = {TestDatabase.class, Database.class})
  @EnableAutoConfiguration
  public static class TestApplicationConfiguration {}

  @Autowired private TestData testData;
  @Autowired private EntityManagerFactory entityManagerFactory;
  @Autowired private QueryHelper queryHelper;

  private SessionFactory sessionFactory;

  @Before
  public void setup() {
    testData.addTestData();
    sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    sessionFactory.getStatistics().setStatisticsEnabled(true);
    sessionFactory.getStatistics().clear();
  }

  @Test
  public void getEmptyResultsTest() {
    var results =
        queryHelper.query(
            UserX.class, userX -> userX.where(Predicate.neq(userX.getId(), userX.getId())));

    assertThat(results).isEmpty();

    assertThat(sessionFactory.getStatistics().getQueries()).hasLength(1);
  }

  @Test
  public void getEntitiesWithNoConfigurationTest() {
    var results = queryHelper.query(UserX.class, Entity::notDeleted);

    assertThat(results.stream().map(UserX::getId).toList())
        .containsExactly(
            testData.getAdminX().getId(),
            testData.getTeacher().getId(),
            testData.getStudent().getId(),
            testData.getDemo().getId());

    assertThat(sessionFactory.getStatistics().getQueries()).hasLength(1);
  }

  @Test
  public void getEntitiesWithSingleJoinTest() {
    var results =
        queryHelper.query(
            UserX.class,
            userX -> {
              userX.notDeleted().join(UserX_.adminX, JoinType.INNER).notDeleted();
              return userX;
            });

    assertThat(results.stream().map(UserX::getId).toList())
        .containsExactly(testData.getAdminX().getId());
    assertThat(Hibernate.isInitialized(results.get(0).getAdminX())).isFalse();

    assertThat(sessionFactory.getStatistics().getQueries()).hasLength(1);
  }

  @Test
  public void getEntitiesWithSingleFetchTest() {
    var results =
        queryHelper.query(
            UserX.class,
            userX -> {
              userX.notDeleted().join(UserX_.adminX, JoinType.INNER).notDeleted().fetch();
              return userX;
            });

    assertThat(results.stream().map(UserX::getId).toList())
        .containsExactly(testData.getAdminX().getId());
    assertThat(Hibernate.isInitialized(results.get(0).getAdminX())).isTrue();

    assertThat(sessionFactory.getStatistics().getQueries()).hasLength(1);
  }

  @Test
  public void getEntitiesWithSetFetchTest() {
    var results =
        queryHelper.query(
            Teacher.class,
            teacher -> {
              teacher
                  .notDeleted()
                  .join(Teacher_.teacherClassXES, JoinType.LEFT)
                  .notDeleted()
                  .fetch()
                  .join(TeacherClassX_.classX, JoinType.LEFT)
                  .notDeleted()
                  .fetch();
              return teacher;
            });

    assertThat(results.stream().map(Teacher::getId).toList())
        .containsExactly(
            testData.getAdminX().getTeacher().getId(), testData.getTeacher().getTeacher().getId());
    assertThat(
            results.stream()
                .map(
                    t ->
                        ImmutableList.of(
                            t.getId(),
                            t.getTeacherClassXES().stream()
                                .map(TeacherClassX::getClassX)
                                .map(ClassX::getId)
                                .sorted()
                                .toList()))
                .toList())
        .containsExactly(
            ImmutableList.of(
                testData.getAdminX().getTeacher().getId(),
                ImmutableList.of(
                        testData.getChemistryClassX().getId(),
                        testData.getProgrammingClassX().getId(),
                        testData.getDanceClassX().getId())
                    .stream()
                    .sorted()
                    .toList()),
            ImmutableList.of(
                testData.getTeacher().getTeacher().getId(),
                ImmutableList.of(
                        testData.getChemistryClassX().getId(),
                        testData.getProgrammingClassX().getId(),
                        testData.getDanceClassX().getId())
                    .stream()
                    .sorted()
                    .toList()));

    assertThat(sessionFactory.getStatistics().getQueries()).hasLength(1);
  }

  @Test
  public void getUserXsPage1of1sTest() {
    var results =
        queryHelper.query(
            UserX.class,
            userX -> {
              userX.notDeleted().orderByAsc(userX.get(UserX_.emailAddress));
              return userX;
            },
            PageRequest.of(0, 1));

    assertThat(results.stream().map(UserX::getId).toList())
        .containsExactly(testData.getAdminX().getId());
  }

  @Test
  public void getUserXsPage2of1sTest() {
    var results =
        queryHelper.query(
            UserX.class,
            userX -> {
              userX.notDeleted().orderByAsc(userX.get(UserX_.emailAddress));
              return userX;
            },
            PageRequest.of(1, 1));

    assertThat(results.stream().map(UserX::getId).toList())
        .containsExactly(testData.getDemo().getId());
  }

  @Test
  public void getUserXsPage3of1sTest() {
    var results =
        queryHelper.query(
            UserX.class,
            userX -> {
              userX.notDeleted().orderByAsc(userX.get(UserX_.emailAddress));
              return userX;
            },
            PageRequest.of(2, 1));

    assertThat(results.stream().map(UserX::getId).toList())
        .containsExactly(testData.getStudent().getId());
  }

  @Test
  public void getUserXsPage4of1sTest() {
    var results =
        queryHelper.query(
            UserX.class,
            userX -> {
              userX.notDeleted().orderByAsc(userX.get(UserX_.emailAddress));
              return userX;
            },
            PageRequest.of(3, 1));

    assertThat(results.stream().map(UserX::getId).toList())
        .containsExactly(testData.getTeacher().getId());
  }

  @Test
  public void getUserXsPage5of1sTest() {
    var results =
        queryHelper.query(
            UserX.class,
            userX -> {
              userX.notDeleted().orderByAsc(userX.get(UserX_.emailAddress));
              return userX;
            },
            PageRequest.of(4, 1));

    assertThat(results).isEmpty();
  }

  @Test
  public void getUserXsPage1of2sTest() {
    var results =
        queryHelper.query(
            UserX.class,
            userX -> {
              userX.notDeleted().orderByAsc(userX.get(UserX_.emailAddress));
              return userX;
            },
            PageRequest.of(0, 2));

    assertThat(results.stream().map(UserX::getId).toList())
        .containsExactly(testData.getAdminX().getId(), testData.getDemo().getId());
  }

  @Test
  public void getUserXsPage2of2sTest() {
    var results =
        queryHelper.query(
            UserX.class,
            userX -> {
              userX.notDeleted().orderByAsc(userX.get(UserX_.emailAddress));
              return userX;
            },
            PageRequest.of(1, 2));

    assertThat(results.stream().map(UserX::getId).toList())
        .containsExactly(testData.getStudent().getId(), testData.getTeacher().getId());
  }

  @Test
  public void getUserXsPage3of2sTest() {
    var results =
        queryHelper.query(
            UserX.class,
            userX -> {
              userX.notDeleted().orderByAsc(userX.get(UserX_.emailAddress));
              return userX;
            },
            PageRequest.of(2, 2));

    assertThat(results).isEmpty();
  }

  @Test
  public void getUserXsPage1of3sTest() {
    var results =
        queryHelper.query(
            UserX.class,
            userX -> {
              userX.notDeleted().orderByAsc(userX.get(UserX_.emailAddress));
              return userX;
            },
            PageRequest.of(0, 3));

    assertThat(results.stream().map(UserX::getId).toList())
        .containsExactly(
            testData.getAdminX().getId(),
            testData.getDemo().getId(),
            testData.getStudent().getId());
  }

  @Test
  public void getUserXsPage2of3sTest() {
    var results =
        queryHelper.query(
            UserX.class,
            userX -> {
              userX.notDeleted().orderByAsc(userX.get(UserX_.emailAddress));
              return userX;
            },
            PageRequest.of(1, 3));

    assertThat(results.stream().map(UserX::getId).toList())
        .containsExactly(testData.getTeacher().getId());
  }

  @Test
  public void getUserXsPage1of5sTest() {
    var results =
        queryHelper.query(
            UserX.class,
            userX -> {
              userX.notDeleted().orderByAsc(userX.get(UserX_.emailAddress));
              return userX;
            },
            PageRequest.of(0, 5));

    assertThat(results.stream().map(UserX::getId).toList())
        .containsExactly(
            testData.getAdminX().getId(),
            testData.getDemo().getId(),
            testData.getStudent().getId(),
            testData.getTeacher().getId());
  }

  @Test
  public void getResultsWithDifferentNonEntitySelect() {
    var results =
        queryHelper.query(
            String.class,
            UserX.class,
            userX -> userX.notDeleted().get(UserX_.emailAddress).select());

    assertThat(results)
        .containsExactly(
            testData.getAdminX().getEmailAddress(),
            testData.getTeacher().getEmailAddress(),
            testData.getStudent().getEmailAddress(),
            testData.getDemo().getEmailAddress());

    assertThat(sessionFactory.getStatistics().getQueries()).hasLength(1);
  }

  @Test
  public void getResultsWithDifferentNonEntitySelectWithPage() {
    var results =
        queryHelper.query(
            String.class,
            UserX.class,
            userX ->
                userX
                    .notDeleted()
                    .get(UserX_.emailAddress)
                    .select()
                    .orderByAsc(userX.get(UserX_.emailAddress)),
            Pageable.ofSize(1).withPage(0));

    assertThat(results).containsExactly(testData.getAdminX().getEmailAddress());

    assertThat(sessionFactory.getStatistics().getQueries()).hasLength(2);
  }

  @Test
  public void getResultsWithGetDifferentEntitySelect() {
    var results =
        queryHelper.query(
            AdminX.class, UserX.class, userX -> userX.notDeleted().get(UserX_.adminX).select());

    assertThat(results.stream().map(AdminX::getId).toList())
        .containsExactly(testData.getAdminX().getAdminX().getId());

    assertThat(sessionFactory.getStatistics().getQueries()).hasLength(1);
  }

  @Test
  public void getResultsWithGetDifferentEntitySelectWithPage() {
    var results =
        queryHelper.query(
            AdminX.class,
            UserX.class,
            userX -> userX.notDeleted().get(UserX_.adminX).select(),
            Pageable.ofSize(1).withPage(0));

    assertThat(results.stream().map(AdminX::getId).toList())
        .containsExactly(testData.getAdminX().getAdminX().getId());

    assertThat(sessionFactory.getStatistics().getQueries()).hasLength(2);
  }

  @Test
  public void getResultsWithJoinDifferentEntitySelect() {
    var results =
        queryHelper.query(
            ClassX.class,
            UserX.class,
            userX ->
                userX
                    .notDeleted()
                    .join(UserX_.teacher, JoinType.LEFT)
                    .join(Teacher_.teacherClassXES, JoinType.LEFT)
                    .join(TeacherClassX_.classX, JoinType.LEFT)
                    .select());

    assertThat(results.stream().map(ClassX::getId).toList())
        .containsExactly(
            testData.getChemistryClassX().getId(),
            testData.getProgrammingClassX().getId(),
            testData.getDanceClassX().getId());

    assertThat(sessionFactory.getStatistics().getQueries()).hasLength(1);
  }

  @Test
  public void getResultsWithJoinDifferentEntitySelectWithPage() {
    var results =
        queryHelper.query(
            ClassX.class,
            UserX.class,
            userX -> {
              var classX =
                  userX
                      .notDeleted()
                      .join(UserX_.teacher, JoinType.LEFT)
                      .join(Teacher_.teacherClassXES, JoinType.LEFT)
                      .join(TeacherClassX_.classX, JoinType.LEFT)
                      .select();
              classX.orderByAsc(classX.get(ClassX_.name));
              return classX;
            },
            Pageable.ofSize(1).withPage(0));

    assertThat(results.stream().map(ClassX::getId).toList())
        .containsExactly(testData.getDanceClassX().getId());

    assertThat(sessionFactory.getStatistics().getQueries()).hasLength(2);
  }
}
