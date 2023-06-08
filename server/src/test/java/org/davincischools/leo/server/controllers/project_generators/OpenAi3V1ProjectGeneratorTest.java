package org.davincischools.leo.server.controllers.project_generators;

import com.google.common.truth.Truth;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorLog;
import org.junit.Test;
import org.mockito.Mockito;

public class OpenAi3V1ProjectGeneratorTest {
  HttpExecutorLog mockLog = Mockito.mock(HttpExecutorLog.class);

  @Test
  public void testProjectNumberColon() {
    Truth.assertThat(OpenAi3V1ProjectGenerator.normalizeAndCheckString("Project 1: Title's Title"))
        .isEqualTo("Title's Title");
  }

  @Test
  public void testProjectNumberColonQuoted() {
    Truth.assertThat(
            OpenAi3V1ProjectGenerator.normalizeAndCheckString("Project 1: \"Title's Title\""))
        .isEqualTo("Title's Title");
  }

  @Test
  public void testProjectNumberColonLabelColon() {
    Truth.assertThat(
            OpenAi3V1ProjectGenerator.normalizeAndCheckString(
                "Project 1: Title: Primary Title: Subtitle"))
        .isEqualTo("Primary Title: Subtitle");
  }

  @Test
  public void testProjectNumberColonLabelColonQuoted() {
    Truth.assertThat(
            OpenAi3V1ProjectGenerator.normalizeAndCheckString(
                "Project 1: Title: \"Title's Title\""))
        .isEqualTo("Title's Title");
  }

  @Test
  public void testShortDescrNumberColon() {
    Truth.assertThat(
            OpenAi3V1ProjectGenerator.normalizeAndCheckString("Short Description: Title's Title"))
        .isEqualTo("Title's Title");
  }

  @Test
  public void testShortDescrNumberColonQuoted() {
    Truth.assertThat(
            OpenAi3V1ProjectGenerator.normalizeAndCheckString("Short Description: “Title's Title”"))
        .isEqualTo("Title's Title");
  }

  @Test
  public void testDescrNumberColon() {
    Truth.assertThat(
            OpenAi3V1ProjectGenerator.normalizeAndCheckString("Description: Title's Title"))
        .isEqualTo("Title's Title");
  }

  @Test
  public void testNumberDotLabelColon() {
    Truth.assertThat(OpenAi3V1ProjectGenerator.normalizeAndCheckString("1. Title: This: Title"))
        .isEqualTo("This: Title");
  }
}
