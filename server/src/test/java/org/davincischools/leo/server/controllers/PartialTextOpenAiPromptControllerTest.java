package org.davincischools.leo.server.controllers;

import static com.google.common.truth.Truth.assertThat;

import org.davincischools.leo.database.test.TestData;
import org.davincischools.leo.server.ServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {ServerApplication.class, TestData.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class PartialTextOpenAiPromptControllerTest {
  String LIST_RESPONSE_CONTENT =
      "\n\n"
          // We want responses that look like the following.
          + "1. Typing challenges\n"
          + "\n"
          + "1. Typing exams";

  @Test
  public void parseContentIntoListTest() {
    assertThat(PartialTextOpenAiPromptController.parseContentIntoList(LIST_RESPONSE_CONTENT))
        .containsExactly("Typing challenges", "Typing exams")
        .inOrder();
  }
}
