package org.davincischools.leo.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/** This is to verify that JUnit 5 tests are run. Disabled by default. */
public class JUnit5Tests {
  @Test
  @Disabled
  public void testJUnit5() {
    Assertions.fail("JUnit 5 test failed");
  }
}
