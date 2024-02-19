package org.davincischools.leo.server;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/** This is to verify that JUnit 4 tests are run. Disabled by default. */
public class JUnit4Tests {
  @Test
  @Ignore
  public void testJUnit4() {
    Assert.fail("JUnit 4 test failed");
  }
}
