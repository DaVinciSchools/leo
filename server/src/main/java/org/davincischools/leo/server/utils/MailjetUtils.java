package org.davincischools.leo.server.utils;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

import com.google.protobuf.util.JsonFormat;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import java.io.IOException;
import org.apache.http.HttpStatus;
import org.davincischools.leo.protos.mailjet.MailjetSendRequest;
import org.davincischools.leo.protos.mailjet.MailjetSendResponse;
import org.json.JSONObject;

public class MailjetUtils {

  public static final String MAILJET_API_KEY = "mailjet.api.key";
  public static final String MAILJET_API_SECRET_KEY = "mailjet.api.secret.key";

  public static MailjetSendResponse send(MailjetSendRequest request) throws IOException {
    checkNotNull(request);

    String apiKey = System.getProperty(MAILJET_API_KEY);
    if (isNullOrEmpty(apiKey)) {
      throw new IllegalArgumentException("Mailjet API key is not set.");
    }

    String apiSecretKey = System.getProperty(MAILJET_API_SECRET_KEY);
    if (isNullOrEmpty(apiSecretKey)) {
      throw new IllegalArgumentException("Mailjet API secret key is not set.");
    }

    try {
      var mailjetClient =
          new MailjetClient(
              ClientOptions.builder().apiKey(apiKey).apiSecretKey(apiSecretKey).build());
      var mailjetRequest = new MailjetRequest(Emailv31.resource);
      var jsonObject = new JSONObject(JsonFormat.printer().print(request));
      for (var key : jsonObject.keySet()) {
        mailjetRequest.property(key, jsonObject.get(key));
      }

      MailjetResponse mailjetResponse = mailjetClient.post(mailjetRequest);
      if (mailjetResponse.getStatus() != HttpStatus.SC_OK) {
        throw new MailjetException(
            "Mailjet request failed with status code: " + mailjetResponse.getStatus());
      }

      var response = MailjetSendResponse.newBuilder();
      JsonFormat.parser().merge(mailjetResponse.getRawResponseContent(), response);

      return response.build();
    } catch (Exception e) {
      throw new IOException("Sending mail failed.", e);
    }
  }
}
