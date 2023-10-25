package org.davincischools.leo.database.test;

import com.google.common.collect.ImmutableList;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Optional;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.admin_x.DatabaseManagement;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.test.util.TestSocketUtils;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@Component
public class TestDatabase {

  public static final String USE_EXTERNAL_DATABASE_PROFILE = "useExternalDatabase";

  private static final Logger logger = LogManager.getLogger();

  // Test database constants.
  private static final String TEST_DATABASE_PORT_KEY = "project_leo.test.database.port";
  private static final String DATABASE_NAME = "leo_container_test";
  private static final String ROOT_USERNAME = "root";
  private static final String ROOT_PASSWORD = "password";
  private static final String USERNAME = "test";
  private static final String PASSWORD = "password";

  // Created connection.
  private static DataSource dataSource = null;

  @Bean("dataSource")
  @Profile("!" + USE_EXTERNAL_DATABASE_PROFILE)
  @Primary
  private static DataSource createTestDataSource(@Autowired Environment environment)
      throws SQLException, IOException {
    try {
      synchronized (TestDatabase.class) {
        if (dataSource != null) {
          return dataSource;
        }

        // Create and start the container.
        int port =
            Optional.ofNullable(environment.getProperty(TEST_DATABASE_PORT_KEY, Integer.class))
                .orElse(-1);
        MySQLContainer<?> container = createAndStartMySqlContainer(port);

        // Configure a data source to point to the container.
        DataSource rootDataSource =
            DataSourceBuilder.create()
                .driverClassName(org.testcontainers.jdbc.ContainerDatabaseDriver.class.getName())
                .url(container.getJdbcUrl())
                .username(ROOT_USERNAME)
                .password(ROOT_PASSWORD)
                .type(MysqlDataSource.class)
                .build();

        // Create the user and grant them permission.
        createUserX(rootDataSource, USERNAME, PASSWORD);
        grantAllAccess(rootDataSource, DATABASE_NAME, USERNAME);

        // Load the schema into it.
        DatabaseManagement.loadSchema(rootDataSource);

        logger
            .atWarn()
            .log(
                "Created a test database at {}. Data will not be persisted.",
                container.getJdbcUrl());

        // Create a data source for the user.
        dataSource =
            DataSourceBuilder.create()
                .driverClassName(org.testcontainers.jdbc.ContainerDatabaseDriver.class.getName())
                .url(container.getJdbcUrl())
                .username(USERNAME)
                .password(PASSWORD)
                .type(MysqlDataSource.class)
                .build();

        return dataSource;
      }
    } catch (IllegalStateException e) {
      // Ths failure is probably due to Docker Desktop not being installed.
      if (e.getMessage().contains("Could not find a valid Docker environment")) {
        throw new IllegalArgumentException(
            "You may need to start or install Docker Desktop. See instructions at: "
                + "https://github.com/DaVinciSchools/leo/blob/main/BUILDING.md#build-dependencies-docker-desktop.",
            e);
      }
      throw e;
    }
  }

  @NotNull
  private static MySQLContainer<?> createAndStartMySqlContainer(int port) {
    // Configure the test database container.
    MySQLContainer<?> container =
        new MySQLContainer<>(DockerImageName.parse("mysql").withTag("8-debian"))
            .withDatabaseName(DATABASE_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD)
            .withEnv("MYSQL_ROOT_PASSWORD", ROOT_PASSWORD);
    container.setStartupAttempts(1);
    container.withStartupTimeout(Duration.ofMinutes(5));
    container.withConnectTimeoutSeconds((int) Duration.ofMinutes(5).getSeconds());

    // Workaround for
    // "STDERR: mysqld: Can't read dir of '/etc/mysql/conf.d/' (Errcode: 13 - Permission denied)"
    // It causes a fatal error and tests hang waiting for the container to start.
    // See:
    // https://github.com/testcontainers/testcontainers-java/issues/914#issuecomment-876965013
    container.addParameter("TC_MY_CNF", null);

    int attempts = 0;
    while (true) {
      int attemptedPort = port > 0 ? port : TestSocketUtils.findAvailableTcpPort();
      logger.atInfo().log("Trying to start container on port {}.", attemptedPort);
      try {
        container.setPortBindings(ImmutableList.of(attemptedPort + ":3306"));
        container.start();
        break;
      } catch (RuntimeException e) {
        if (attempts++ < 10) {
          logger
              .atWarn()
              .withThrowable(e)
              .log("Failed to start container on port {}. Retrying...", attemptedPort);
        } else {
          logger
              .atError()
              .withThrowable(e)
              .log("Failed to start container on port {}.", attemptedPort);
          throw e;
        }
      }
    }
    logger.atInfo().log("Successfully started container.");

    return container;
  }

  private static void createUserX(DataSource source, String username, String password)
      throws SQLException {
    try (Connection connection = source.getConnection()) {
      // Drop an existing user.
      PreparedStatement statement = connection.prepareStatement("DROP USER ?@'%';");
      statement.setString(1, username);
      try {
        statement.executeUpdate();
      } catch (SQLException e) {
        // It's okay if the user doesn't exist and this fails.
      }

      // Create the user.
      statement = connection.prepareStatement("CREATE USER ?@'%' IDENTIFIED BY ?;");
      statement.setString(1, username);
      statement.setString(2, password);
      statement.executeUpdate();
    }
  }

  private static void grantAllAccess(DataSource source, String database, String username)
      throws SQLException {
    try (Connection connection = source.getConnection()) {
      // Grant the user permissions.
      PreparedStatement statement =
          connection.prepareStatement(
              // We can't place the database name in SQL using a prepared
              // statement since the database name isn't a string.
              String.format("GRANT ALL ON %s.* TO ?@'%%';", database));
      statement.setString(1, username);
      statement.executeUpdate();
    }
  }
}
