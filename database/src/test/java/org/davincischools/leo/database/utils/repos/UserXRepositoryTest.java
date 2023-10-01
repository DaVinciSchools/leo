package org.davincischools.leo.database.utils.repos;

import static com.google.common.truth.Truth.assertThat;

import jakarta.persistence.EntityManager;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.test.TestData;
import org.davincischools.leo.database.test.TestDatabase;
import org.davincischools.leo.database.utils.Database;
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
@SpringBootTest(classes = UserXRepositoryTest.TestApplicationConfiguration.class)
public class UserXRepositoryTest {

  @Configuration
  @ComponentScan(basePackageClasses = {TestDatabase.class, Database.class})
  @EnableAutoConfiguration
  public static class TestApplicationConfiguration {}

  @Autowired private Database db;
  @Autowired private EntityManager em;
  @Autowired private TestData testData;

  @Before
  public void setup() {
    testData.addTestData();
  }

  @Test
  public void getUserXsPage1of4Test() {
    var userXs = db.getUserXRepository().getUserXs(new GetUserXsParams().setPage(0).setPageSize(1));
    assertThat(userXs.getTotalElements()).isEqualTo(4);
    assertThat(userXs.getTotalPages()).isEqualTo(4);
    assertThat(userXs.stream().map(UserX::getId).toList())
        .containsExactly(testData.getAdminX().getId());
  }

  @Test
  public void getUserXsPage2of4Test() {
    var userXs = db.getUserXRepository().getUserXs(new GetUserXsParams().setPage(1).setPageSize(1));
    assertThat(userXs.stream().map(UserX::getId).toList())
        .containsExactly(testData.getDemo().getId());
  }

  @Test
  public void getUserXsPage3of4Test() {
    var userXs = db.getUserXRepository().getUserXs(new GetUserXsParams().setPage(2).setPageSize(1));
    assertThat(userXs.stream().map(UserX::getId).toList())
        .containsExactly(testData.getStudent().getId());
  }

  @Test
  public void getUserXsPage4of4Test() {
    var userXs = db.getUserXRepository().getUserXs(new GetUserXsParams().setPage(3).setPageSize(1));
    assertThat(userXs.stream().map(UserX::getId).toList())
        .containsExactly(testData.getTeacher().getId());
  }

  @Test
  public void getUserXsPage5of4Test() {
    var userXs = db.getUserXRepository().getUserXs(new GetUserXsParams().setPage(4).setPageSize(1));
    assertThat(userXs.stream().map(UserX::getId).toList()).isEmpty();
  }

  @Test
  public void getUserXsPage1of2Test() {
    var userXs = db.getUserXRepository().getUserXs(new GetUserXsParams().setPage(0).setPageSize(2));
    assertThat(userXs.getTotalElements()).isEqualTo(4);
    assertThat(userXs.getTotalPages()).isEqualTo(2);
    assertThat(userXs.stream().map(UserX::getId).toList())
        .containsExactly(testData.getAdminX().getId(), testData.getDemo().getId());
  }

  @Test
  public void getUserXsPage2of2Test() {
    var userXs = db.getUserXRepository().getUserXs(new GetUserXsParams().setPage(1).setPageSize(2));
    assertThat(userXs.stream().map(UserX::getId).toList())
        .containsExactly(testData.getStudent().getId(), testData.getTeacher().getId());
  }

  @Test
  public void getUserXsPage3of2Test() {
    var userXs = db.getUserXRepository().getUserXs(new GetUserXsParams().setPage(2).setPageSize(2));
    assertThat(userXs.stream().map(UserX::getId).toList()).isEmpty();
  }

  @Test
  public void getUserXsPage2of1and1Test() {
    var userXs = db.getUserXRepository().getUserXs(new GetUserXsParams().setPage(1).setPageSize(3));
    assertThat(userXs.stream().map(UserX::getId).toList())
        .containsExactly(testData.getTeacher().getId());
  }
}
