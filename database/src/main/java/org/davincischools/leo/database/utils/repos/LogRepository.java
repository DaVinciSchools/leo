package org.davincischools.leo.database.utils.repos;

import java.nio.charset.StandardCharsets;
import org.davincischools.leo.database.daos.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {

  long LONGTEXT_MAX_BYTES = /* 4GB= */ 4L * 1024 * 1024 * 1024;
  int LONGTEXT_MAX_CHARS =
      (int)
          Math.min(
              Integer.MAX_VALUE,
              Math.min(
                  Math.round(
                      Math.floor(
                          LONGTEXT_MAX_BYTES
                              / StandardCharsets.UTF_8.newEncoder().maxBytesPerChar())),
                  LONGTEXT_MAX_BYTES / /* MySql max bytes per char */ 4));

  enum Status {
    ERROR,
    SUCCESS
  }

  public static String trimToLogLength(String input) {
    if (input.length() < LONGTEXT_MAX_CHARS) {
      return input;
    }
    return input.substring(0, Math.min(input.length(), LONGTEXT_MAX_CHARS));
  }
}
