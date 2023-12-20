package org.davincischools.leo.server.utils;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.text.StringEscapeUtils;

public class TextUtils {

  public static String quoteAndEscape(String s) {
    return "\"" + StringEscapeUtils.escapeJava(s) + "\"";
  }

  public static String numberLines(String s) {
    try (BufferedReader br = new BufferedReader(new StringReader(s))) {
      AtomicInteger lineNumber = new AtomicInteger(1);
      StringBuilder sb = new StringBuilder();
      br.lines()
          .forEach(
              line ->
                  sb.append(lineNumber.getAndIncrement()).append(": ").append(line).append("\n"));
      return sb.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
