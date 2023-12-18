package org.davincischools.leo.server.utils;

import java.nio.charset.StandardCharsets;
import org.jsoup.Jsoup;

public class HtmlUtils {
  public static String stripOutHtml(String html) {
    return Jsoup.parse(html, StandardCharsets.ISO_8859_1.name()).text();
  }
}
