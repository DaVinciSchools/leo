package org.davincischools.leo.server.utils;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.truth.Truth.assertThat;

import java.io.File;
import java.io.FileInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.protos.mailjet.MailjetSendRequest;
import org.davincischools.leo.protos.mailjet.MailjetSendRequestFrom;
import org.davincischools.leo.protos.mailjet.MailjetSendRequestMessage;
import org.davincischools.leo.protos.mailjet.MailjetSendRequestTo;
import org.davincischools.leo.protos.mailjet.MailjetSendResponse;
import org.davincischools.leo.protos.mailjet.MailjetSendResponseMessage;
import org.davincischools.leo.protos.mailjet.MailjetSendResponseStatus;
import org.junit.BeforeClass;
import org.junit.Test;

public class MailjetUtilsTest {

  private static final Logger logger = LogManager.getLogger();

  @BeforeClass
  public static void loadProperties() {
    try (var propertiesStream =
        new FileInputStream(new File(System.getProperty("user.home"), "project_leo.properties"))) {
      System.getProperties().load(propertiesStream);
      if (isNullOrEmpty(System.getProperty(MailjetUtils.MAILJET_API_KEY))) {
        throw new IllegalArgumentException("Mailjet API key is not set.");
      }
    } catch (Throwable t) {
      logger
          .atWarn()
          .withThrowable(t)
          .log("Unable to load Mailjet API keys. This is okay if you don't intend to test it.");
    }
  }

  @Test
  public void sendEmailTest() throws Exception {
    // Only run the test if the MailJet API key is available and you are online.
    if (isNullOrEmpty(System.getProperty(MailjetUtils.MAILJET_API_KEY)) || !TestUtils.isOnline()) {
      logger.atWarn().log("Skipping MailJet test due to missing key or being offline.");
      return;
    }

    MailjetSendResponse response =
        MailjetUtils.send(
            MailjetSendRequest.newBuilder()
                .setSandboxMode(true)
                .setAdvanceErrorHandling(true)
                .addMessages(
                    MailjetSendRequestMessage.newBuilder()
                        .setFrom(
                            MailjetSendRequestFrom.newBuilder()
                                .setName("Project Leo A.I.")
                                .setEmail("ai@projectleo.net"))
                        .addTo(
                            MailjetSendRequestTo.newBuilder()
                                .setName("Project Leo Tests")
                                .setEmail("test@projectleo.net"))
                        .setCustomID("MailjetUtilsTest")
                        .setHTMLPart("This is a <b>test</b> message.")
                        .setTextPart("This is a test message.")
                        .build())
                .build());

    assertThat(
            response.getMessagesList().stream().map(MailjetSendResponseMessage::getStatus).toList())
        .containsExactly(MailjetSendResponseStatus.success);
  }
}
