package org.davincischools.leo.database.utils.admin_x;

import static com.google.common.truth.Truth.assertThat;
import static org.davincischools.leo.database.utils.JdbcConstants.GET_COLUMNS.*;

import com.google.common.collect.ImmutableSet;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import org.davincischools.leo.database.post_environment_processors.LoadCustomProjectLeoProperties;
import org.davincischools.leo.database.test.TestDatabase;
import org.davincischools.leo.database.utils.Database;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DatabaseSchemaSyncTest.TestApplicationConfiguration.class)
public class DatabaseSchemaSyncTest {

  private static final ImmutableSet<String> TABLE_COLUMNS_TO_IGNORE =
      ImmutableSet.of(COLUMN_NAME, ORDINAL_POSITION, TABLE_CAT, TABLE_NAME);
  private static final ImmutableSet<String> TRIGGER_COLUMNS_TO_IGNORE =
      ImmutableSet.of(CREATED, DEFINER);

  @Configuration
  @ComponentScan(basePackageClasses = {Database.class, TestDatabase.class})
  @EnableAutoConfiguration
  public static class TestApplicationConfiguration {}

  private static final String SPRING_DATASOURCE_DRIVER_CLASS_NAME_PROP_NAME =
      "spring.datasource.driver-class-name";
  private static final String SPRING_DATASOURCE_URL_PROP_NAME = "spring.datasource.url";
  private static final String SPRING_DATASOURCE_USERNAME_PROP_NAME = "spring.datasource.username";
  private static final String SPRING_DATASOURCE_PASSWORD_PROP_NAME = "spring.datasource.password";

  @Test
  public void verifyThatTablesMatch() throws Exception {
    Environment environment = getProjectLeoProperties();

    if (environment.getProperty(SPRING_DATASOURCE_USERNAME_PROP_NAME) == null
        || environment.getProperty(SPRING_DATASOURCE_PASSWORD_PROP_NAME) == null) {
      return;
    }

    var testDataSource = TestDatabase.createTestDataSource(environment);
    var prodDataSource =
        DataSourceBuilder.create()
            .driverClassName(environment.getProperty(SPRING_DATASOURCE_DRIVER_CLASS_NAME_PROP_NAME))
            .url(environment.getProperty(SPRING_DATASOURCE_URL_PROP_NAME))
            .username(environment.getProperty(SPRING_DATASOURCE_USERNAME_PROP_NAME))
            .password(environment.getProperty(SPRING_DATASOURCE_PASSWORD_PROP_NAME))
            .type(MysqlDataSource.class)
            .build();

    var expectedColumns = getTableSchema(testDataSource.getConnection());
    var prodColumns = getTableSchema(prodDataSource.getConnection());

    assertThat(prodColumns.keySet()).containsAtLeastElementsIn(expectedColumns.keySet());
    assertThat(prodColumns).containsAtLeastEntriesIn(expectedColumns);
  }

  @Test
  public void verifyThatTriggersMatch() throws Exception {
    Environment environment = getProjectLeoProperties();

    if (environment.getProperty(SPRING_DATASOURCE_USERNAME_PROP_NAME) == null
        || environment.getProperty(SPRING_DATASOURCE_PASSWORD_PROP_NAME) == null) {
      return;
    }

    var testDataSource = TestDatabase.createTestDataSource(environment);
    var prodDataSource =
        DataSourceBuilder.create()
            .driverClassName(environment.getProperty(SPRING_DATASOURCE_DRIVER_CLASS_NAME_PROP_NAME))
            .url(environment.getProperty(SPRING_DATASOURCE_URL_PROP_NAME))
            .username(environment.getProperty(SPRING_DATASOURCE_USERNAME_PROP_NAME))
            .password(environment.getProperty(SPRING_DATASOURCE_PASSWORD_PROP_NAME))
            .type(MysqlDataSource.class)
            .build();

    var expectedTriggers = getTriggerSchema(testDataSource.getConnection());
    var prodTriggers = getTriggerSchema(prodDataSource.getConnection());

    assertThat(prodTriggers.keySet()).containsAtLeastElementsIn(expectedTriggers.keySet());
    assertThat(prodTriggers).containsAtLeastEntriesIn(expectedTriggers);
  }

  private Map<String, Map<String, Object>> getTableSchema(Connection connection)
      throws SQLException {
    var columnsMap = new TreeMap<String, Map<String, Object>>();
    var columns = connection.getMetaData().getColumns(connection.getCatalog(), null, null, null);
    var metadata = columns.getMetaData();
    while (columns.next()) {
      Map<String, Object> tableSchema = new TreeMap<>();
      for (int i = 1; i <= metadata.getColumnCount(); ++i) {
        if (TABLE_COLUMNS_TO_IGNORE.contains(metadata.getColumnName(i))) {
          continue;
        }
        tableSchema.put(metadata.getColumnName(i), columns.getObject(i));
      }
      columnsMap.put(
          columns.getString(TABLE_NAME) + "." + columns.getString(COLUMN_NAME), tableSchema);
    }
    return columnsMap;
  }

  private Map<String, Map<String, Object>> getTriggerSchema(Connection connection)
      throws SQLException {
    var triggerMap = new TreeMap<String, Map<String, Object>>();
    var triggers = connection.prepareStatement("SHOW TRIGGERS").executeQuery();
    while (triggers.next()) {
      Map<String, Object> tableSchema = new TreeMap<>();
      for (int i = 1; i <= triggers.getMetaData().getColumnCount(); ++i) {
        if (TRIGGER_COLUMNS_TO_IGNORE.contains(triggers.getMetaData().getColumnName(i))) {
          continue;
        }
        tableSchema.put(triggers.getMetaData().getColumnName(i), triggers.getObject(i));
      }
      triggerMap.put(triggers.getString(TRIGGER), tableSchema);
    }
    return triggerMap;
  }

  private MockEnvironment getProjectLeoProperties() throws IOException {
    MockEnvironment environment = new MockEnvironment();
    File projectLeoPropertiesFile =
        new File(Optional.ofNullable(System.getenv("HOME")).orElse(""), "project_leo.properties");

    // Load the custom Project Leo properties file, if it exists.
    if (projectLeoPropertiesFile.exists()
        && !environment
            .getPropertySources()
            .contains(LoadCustomProjectLeoProperties.class.getName())) {
      environment
          .getPropertySources()
          .addFirst(
              new ResourcePropertySource(
                  LoadCustomProjectLeoProperties.class.getName(),
                  projectLeoPropertiesFile.toURI().toString()));
    }
    return environment;
  }
}
