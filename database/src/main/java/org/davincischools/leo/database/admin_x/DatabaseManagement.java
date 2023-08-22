package org.davincischools.leo.database.admin_x;

import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public final class DatabaseManagement {

  private static final Logger log = LogManager.getLogger();

  public static void loadSchema(DataSource db) throws SQLException, IOException {
    try (Connection connection = db.getConnection()) {
      // Get the schema files.
      PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
      List<Resource> resources = Arrays.asList(resolver.getResources("my-sql/*.sql"));

      // Apply each schema file in order.
      resources.sort(Comparator.comparing(Resource::getFilename));
      for (Resource resource : resources) {
        log.atInfo().log("Loading schema: {}", resource.getFilename());
        byte[] sqlBytes = ByteStreams.toByteArray(resource.getInputStream());
        String sql = new String(sqlBytes, StandardCharsets.UTF_8);
        connection.prepareCall(sql).execute();
      }
    }
  }
}
