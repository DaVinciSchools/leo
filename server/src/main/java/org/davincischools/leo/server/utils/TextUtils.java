package org.davincischools.leo.server.utils;

import org.apache.commons.text.StringEscapeUtils;

public class TextUtils {

  public static String quoteAndEscape(String s) {
    return "\"" + StringEscapeUtils.escapeJava(s) + "\"";
  }
}
