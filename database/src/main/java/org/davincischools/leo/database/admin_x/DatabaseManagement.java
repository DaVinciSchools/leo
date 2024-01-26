package org.davincischools.leo.database.admin_x;

import static org.davincischools.leo.database.utils.JdbcConstants.GET_COLUMNS.TABLE_NAME;

import com.google.common.base.CaseFormat;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import jakarta.persistence.JoinColumn;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.dao_interfaces.PropagateDeleteFrom;
import org.davincischools.leo.database.dao_interfaces.PropagateDeleteTo;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.test.TestDatabase;
import org.davincischools.leo.database.utils.JdbcConstants.GET_COLUMNS;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public final class DatabaseManagement {

  private static final Logger logger = LogManager.getLogger();

  public static void loadSchema(DataSource db) throws SQLException, IOException {
    try (Connection connection = db.getConnection()) {
      // Get the schema files.
      PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
      List<Resource> resources = Arrays.asList(resolver.getResources("my-sql/*.sql"));

      // Apply each schema file in order.
      resources.sort(Comparator.comparing(Resource::getFilename));
      for (Resource resource : resources) {
        logger.atInfo().log("Loading schema: {}", resource.getFilename());
        byte[] sqlBytes = ByteStreams.toByteArray(resource.getInputStream());
        String sql = new String(sqlBytes, StandardCharsets.UTF_8);
        try {
          connection.prepareCall(sql).execute();
        } catch (SQLException e) {
          if (!resource.getFilename().endsWith("-FAILABLE.sql")) {
            throw e;
          }
        }
      }

      // Apply generated files in order.
      var sqlFiles = new TreeMap<String, String>();
      generateSchema(db, sqlFiles);
      for (var entry : sqlFiles.entrySet()) {
        logger.atInfo().log("Loading generated schema: {}", entry.getKey());
        connection.prepareCall(entry.getValue()).execute();
      }
    }
  }

  public static void generateSchema(DataSource db, Map<String, String> sqlFiles)
      throws SQLException, IOException {
    try (Connection connection = db.getConnection()) {
      int maxNameLength = connection.getMetaData().getMaxProcedureNameLength();
      maxNameLength = maxNameLength != 0 ? maxNameLength : 64;

      var tableResults =
          connection.getMetaData().getTables(connection.getCatalog(), null, null, null);
      while (tableResults.next()) {
        String tableName = tableResults.getString(TABLE_NAME);
        var importedKeys =
            connection.getMetaData().getImportedKeys(connection.getCatalog(), null, tableName);
        while (importedKeys.next()) {
          String pkTable = importedKeys.getString(GET_COLUMNS.PKTABLE_NAME);
          String pkColumn = importedKeys.getString(GET_COLUMNS.PKCOLUMN_NAME);
          String fkTable = importedKeys.getString(GET_COLUMNS.FKTABLE_NAME);
          String fkColumn = importedKeys.getString(GET_COLUMNS.FKCOLUMN_NAME);

          String fromName = "delete_from." + fkTable + "." + fkColumn;
          createDeleteFromUpdateTrigger(
                  clipString(fromName, maxNameLength), pkTable, pkColumn, fkTable, fkColumn)
              .ifPresent(trigger -> sqlFiles.put("999-" + fromName + "-trigger.sql", trigger));

          String toName = "delete_to." + fkTable + "." + fkColumn;
          createDeleteToUpdateTrigger(
                  clipString(toName, maxNameLength), pkTable, pkColumn, fkTable, fkColumn)
              .ifPresent(trigger -> sqlFiles.put("999-" + toName + "-trigger.sql", trigger));
        }
      }
    }
  }

  private static Optional<String> createDeleteFromUpdateTrigger(
      String triggerName, String pkTable, String pkColumn, String fkTable, String fkColumn)
      throws IOException {
    if (!isPropagateDeleteFrom(pkTable, fkTable, fkColumn)) {
      return Optional.empty();
    }

    return Optional.of(
        """
CREATE TRIGGER IF NOT EXISTS `$triggerName`
AFTER UPDATE
ON $pkTable
FOR EACH ROW
  UPDATE $fkTable
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND $fkColumn = NEW.$pkColumn;
"""
            .replaceAll("\\$triggerName", triggerName)
            .replaceAll("\\$pkTable", pkTable)
            .replaceAll("\\$pkColumn", pkColumn)
            .replaceAll("\\$fkTable", fkTable)
            .replaceAll("\\$fkColumn", fkColumn));
  }

  private static Optional<String> createDeleteToUpdateTrigger(
      String triggerName, String pkTable, String pkColumn, String fkTable, String fkColumn)
      throws IOException {
    if (!isPropagateDeleteTo(pkTable, fkTable, fkColumn)) {
      return Optional.empty();
    }

    return Optional.of(
        """
CREATE TRIGGER IF NOT EXISTS `$triggerName`
AFTER UPDATE
ON $fkTable
FOR EACH ROW
  UPDATE $pkTable
  SET deleted = NEW.deleted
  WHERE deleted IS NULL
  AND NEW.deleted IS NOT NULL
  AND $pkColumn = NEW.$fkColumn;
"""
            .replaceAll("\\$triggerName", triggerName)
            .replaceAll("\\$pkTable", pkTable)
            .replaceAll("\\$pkColumn", pkColumn)
            .replaceAll("\\$fkTable", fkTable)
            .replaceAll("\\$fkColumn", fkColumn));
  }

  public static boolean isPropagateDeleteFrom(String pkTable, String fkTable, String fkColumn)
      throws IOException {
    try {
      var fkDaoClass =
          Class.forName(
              UserX.class.getPackageName()
                  + "."
                  + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, fkTable));
      var pkDaoClass =
          Class.forName(
              UserX.class.getPackageName()
                  + "."
                  + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, pkTable));

      for (var method : fkDaoClass.getMethods()) {
        if (!method.getName().startsWith("get") || !method.getReturnType().equals(pkDaoClass)) {
          continue;
        }

        var joinColumn = method.getAnnotation(JoinColumn.class);
        if (joinColumn == null) {
          continue;
        }
        if (!joinColumn.name().equals(fkColumn)) {
          continue;
        }

        return method.getAnnotation(PropagateDeleteFrom.class) != null;
      }

      throw new IOException(fkTable + "." + fkColumn + " joining to " + pkTable + ".id not found.");
    } catch (ClassNotFoundException e) {
      throw new IOException(e);
    }
  }

  public static boolean isPropagateDeleteTo(String pkTable, String fkTable, String fkColumn)
      throws IOException {
    try {
      var fkDaoClass =
          Class.forName(
              UserX.class.getPackageName()
                  + "."
                  + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, fkTable));
      var pkDaoClass =
          Class.forName(
              UserX.class.getPackageName()
                  + "."
                  + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, pkTable));

      for (var method : fkDaoClass.getMethods()) {
        if (!method.getName().startsWith("get") || !method.getReturnType().equals(pkDaoClass)) {
          continue;
        }

        var joinColumn = method.getAnnotation(JoinColumn.class);
        if (joinColumn == null) {
          continue;
        }
        if (!joinColumn.name().equals(fkColumn)) {
          continue;
        }

        return method.getAnnotation(PropagateDeleteTo.class) != null;
      }

      throw new IOException(fkTable + "." + fkColumn + " joining to " + pkTable + ".id not found.");
    } catch (ClassNotFoundException e) {
      throw new IOException(e);
    }
  }

  private static void saveGeneratedSchema(DataSource dataSource, File directory)
      throws SQLException, IOException {
    loadSchema(dataSource);
    var sqlFiles = new TreeMap<String, String>();
    generateSchema(dataSource, sqlFiles);
    for (var entry : sqlFiles.entrySet()) {
      var file = new File(directory, entry.getKey());
      Files.write(entry.getValue().getBytes(StandardCharsets.UTF_8), file);
      logger.atInfo().log("Saved schema: {}", entry.getKey());
    }
  }

  private static String clipString(String text, int maxLength) {
    return text.substring(0, Math.min(text.length(), maxLength));
  }

  public static void main(String[] args) throws SQLException, IOException {
    saveGeneratedSchema(
        TestDatabase.createTestDataSource(new StandardEnvironment()),
        new File(args[0]).getAbsoluteFile());
  }
}
