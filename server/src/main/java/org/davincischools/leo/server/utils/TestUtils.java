package org.davincischools.leo.server.utils;

import java.io.IOException;
import java.net.URL;

public class TestUtils {
  public static boolean isOnline() {
    try {
      new URL("https://www.google.com").openConnection().connect();
    } catch (IOException e) {
      return false;
    }
    return true;
  }
}
