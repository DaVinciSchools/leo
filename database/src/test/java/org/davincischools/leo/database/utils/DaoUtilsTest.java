package org.davincischools.leo.database.utils;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.test.TestDatabase;
import org.hibernate.LazyInitializationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DaoUtilsTest.TestApplicationConfiguration.class)
public class DaoUtilsTest {

  @Configuration
  @ComponentScan(basePackageClasses = {TestDatabase.class, Database.class})
  @EnableAutoConfiguration
  public static class TestApplicationConfiguration {}

  @Autowired TestDatabase testDatabase;
  @Autowired Database db;

  @Test
  public void removeTransientValuesUnusedNoId() throws Exception {
    Exception e =
        assertThrows(
            Exception.class,
            () ->
                db.getClassXRepository()
                    .save(
                        new ClassX()
                            .setCreationTime(Instant.now())
                            .setNumber("TEST101")
                            .setName("Test Class")
                            .setSchool(new School())));
    assertThat(e.getMessage()).contains("unsaved transient instance");
  }

  @Test
  public void removeTransientValuesNull() throws Exception {
    ClassX classX =
        db.getClassXRepository()
            .save(
                DaoUtils.removeTransientValues(
                    new ClassX()
                        .setCreationTime(Instant.now())
                        .setNumber("TEST101")
                        .setName("Test Class")
                        .setSchool(null)));
    assertThat(classX.getSchool()).isNull();
  }

  @Test
  public void removeTransientValuesNoId() throws Exception {
    ClassX classX =
        db.getClassXRepository()
            .save(
                DaoUtils.removeTransientValues(
                    new ClassX()
                        .setCreationTime(Instant.now())
                        .setNumber("TEST101")
                        .setName("Test Class")
                        .setSchool(new School())));
    assertThat(classX.getSchool()).isNull();
  }

  @Test
  public void removeTransientValuesOtherValues() {
    ClassX classX =
        db.getClassXRepository()
            .save(
                DaoUtils.removeTransientValues(
                    new ClassX()
                        .setId(1)
                        .setCreationTime(Instant.now())
                        .setNumber("TEST101")
                        .setName("Test Class")
                        .setSchool(new School().setId(2).setName("School"))));

    assertThat(classX.getSchool().getId()).isEqualTo(2);
    assertThrows(LazyInitializationException.class, () -> classX.getSchool().getName());
  }

  @Test
  public void copyId() {
    ClassX classX =
        new ClassX()
            .setCreationTime(Instant.now())
            .setNumber("TEST101")
            .setName("Test Class")
            .setSchool(new School().setId(2).setName("School"));

    ClassX newClassX = db.getClassXRepository().save(DaoUtils.removeTransientValues(classX));

    DaoUtils.copyId(newClassX, classX);

    assertThat(classX.getSchool().getId()).isEqualTo(2);
    assertThat(classX.getId()).isEqualTo(newClassX.getId());
  }

  @Test
  public void removeTransientValueConsumer() {
    ClassX classX =
        new ClassX()
            .setCreationTime(Instant.now())
            .setNumber("TEST101")
            .setName("Test Class")
            .setSchool(new School().setId(2).setName("School"));

    DaoUtils.removeTransientValues(classX, db.getClassXRepository()::save);

    assertThat(classX.getSchool().getId()).isEqualTo(2);
    assertThat(classX.getId()).isGreaterThan(0);
  }
}
