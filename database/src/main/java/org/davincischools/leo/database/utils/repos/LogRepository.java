package org.davincischools.leo.database.utils.repos;

import java.nio.charset.StandardCharsets;
import org.davincischools.leo.database.daos.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {

  private static int convertByteCountToCharCount(long bytes) {
    return (int)
        Math.min(
            Integer.MAX_VALUE,
            Math.min(
                Math.round(
                    Math.floor(bytes / StandardCharsets.UTF_8.newEncoder().maxBytesPerChar())),
                bytes / /* MySql bytes per char using UTF8MB4 */ 4));
  }

  long LONGTEXT_MAX_BYTES = /* 4GB= */ 4L * 1024 * 1024 * 1024;
  int LONGTEXT_MAX_CHARS = convertByteCountToCharCount(LONGTEXT_MAX_BYTES);

  int MAX_LOG_BYTES = /* 16MB= */ 16 * 1024 * 1024;
  int MAX_LOG_CHARS = convertByteCountToCharCount(MAX_LOG_BYTES);

  enum Status {
    ERROR,
    SUCCESS
  }

  static String trimToLogLength(String input) {
    if (input.length() < MAX_LOG_CHARS) {
      return input;
    }
    return input.substring(0, MAX_LOG_CHARS);
  }
}
