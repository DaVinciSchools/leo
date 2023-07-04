package org.davincischools.leo.server;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;
import static org.davincischools.leo.server.SpringConstants.LOCAL_SERVER_PORT_PROPERTY;
import static org.davincischools.leo.server.test_helpers.WebSession.DEFAULT_CSRF_COOKIE_NAME;
import static org.davincischools.leo.server.test_helpers.WebSession.DEFAULT_CSRF_PARAMETER_NAME;
import static org.davincischools.leo.server.test_helpers.WebSession.DEFAULT_JSESSIONID_COOKIE_NAME;
import static org.davincischools.leo.server.test_helpers.WebSession.bodyAsProto;
import static org.davincischools.leo.server.test_helpers.WebSession.bodyAsString;
import static org.hamcrest.Matchers.matchesRegex;

import com.google.common.net.HttpHeaders;
import org.davincischools.leo.database.daos.Interest;
import org.davincischools.leo.database.test.TestData;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.protos.interest_service.RegisterInterestRequest;
import org.davincischools.leo.protos.interest_service.RegisterInterestResponse;
import org.davincischools.leo.protos.pl_types.User;
import org.davincischools.leo.server.controllers.ReactResourceController;
import org.davincischools.leo.server.test_helpers.WebSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {ServerApplication.class, TestData.class})
public class ServerApplicationTest {
  @Autowired private ReactResourceController controller;
  @Autowired private Database db;
  @Autowired private TestData testData;

  @Value(value = "${" + LOCAL_SERVER_PORT_PROPERTY + "}")
  private int port;

  private WebSession session = new WebSession();

  @BeforeEach
  public void setup() {
    testData.addTestData();
    session = new WebSession();
  }

  public WebClient webClient() {
    return session.wrap(WebClient.builder().baseUrl("http://localhost:" + port).build());
  }

  public WebTestClient webTestClient() {
    return session.wrap(WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build());
  }

  @Test
  public void controllerLoadsTest() {
    assertThat(controller).isNotNull();
  }

  @Test
  public void indexPageLoadsTest() {
    webTestClient()
        .get()
        .uri("/")
        .exchange()
        .expectAll(
            spec -> spec.expectStatus().isOk(),
            spec ->
                spec.expectCookie().value(DEFAULT_CSRF_COOKIE_NAME, matchesRegex("[-0-9a-z]{36}")),
            spec -> spec.expectCookie().doesNotExist(DEFAULT_JSESSIONID_COOKIE_NAME),
            spec -> assertThat(bodyAsString(spec)).contains("Project Leo"));
  }

  @Test
  public void loginSucceedsTest() throws Exception {
    ClientResponse login = webClient().get().uri("/users/login.html").exchange().block();
    assertThat(login.statusCode().is2xxSuccessful());

    webTestClient()
        .post()
        .uri("/api/login.html")
        .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
        .body(
            BodyInserters.fromFormData("username", testData.getAdmin().getEmailAddress())
                .with("password", TestData.PASSWORD))
        .exchange()
        .expectAll(
            spec -> spec.expectStatus().isOk(),
            spec ->
                spec.expectCookie().value(DEFAULT_CSRF_COOKIE_NAME, matchesRegex("[-0-9a-z]{36}")),
            spec ->
                spec.expectCookie()
                    .value(DEFAULT_JSESSIONID_COOKIE_NAME, matchesRegex("[0-9A-Z]{32}")),
            spec ->
                assertThat(bodyAsProto(spec, User.class))
                    .comparingExpectedFieldsOnly()
                    .isEqualTo(
                        User.newBuilder()
                            .setEmailAddress(testData.getAdmin().getEmailAddress())
                            .build()));
  }

  @Test
  public void logoutSucceedsTest() throws Exception {
    ClientResponse login = webClient().get().uri("/users/login.html").exchange().block();
    assertThat(HttpStatus.resolve(login.statusCode().value())).isEqualTo(HttpStatus.OK);

    ClientResponse loggedIn =
        webClient()
            .post()
            .uri("/api/login.html")
            .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
            .body(
                BodyInserters.fromFormData("username", testData.getAdmin().getEmailAddress())
                    .with("password", TestData.PASSWORD))
            .exchange()
            .block();
    assertThat(loggedIn.statusCode().is2xxSuccessful());
    assertThat(loggedIn.cookies().get(DEFAULT_CSRF_COOKIE_NAME)).hasSize(1);
    assertThat(loggedIn.cookies().get(DEFAULT_JSESSIONID_COOKIE_NAME)).hasSize(1);

    webTestClient()
        .post()
        .uri("/api/logout.html")
        .body(BodyInserters.fromFormData(DEFAULT_CSRF_PARAMETER_NAME, session.getCsrfToken()))
        .exchange()
        .expectAll(
            spec -> spec.expectStatus().is3xxRedirection(),
            spec -> spec.expectCookie().doesNotExist(DEFAULT_JSESSIONID_COOKIE_NAME),
            spec -> assertThat(bodyAsProto(spec, User.class)).isEqualTo(User.getDefaultInstance()));
  }

  @Test
  public void registerInterestTest() throws Exception {
    RegisterInterestRequest request =
        RegisterInterestRequest.newBuilder()
            .setFirstName(String.valueOf(System.nanoTime()))
            .setLastName("last")
            .setEmailAddress("email@address.net")
            .setProfession("profession")
            .setReasonForInterest("interest")
            .setDistrictName("district")
            .setSchoolName("school")
            .setAddressLine1("address1")
            .setAddressLine2("address2")
            .setCity("city")
            .setState("xx")
            .setZipCode("zip")
            .setNumTeachers(7)
            .setNumStudents(11)
            .build();

    RegisterInterestResponse response =
        session.protoRequest(
            webClient(),
            "/api/protos/InterestService/RegisterInterest",
            request,
            RegisterInterestResponse.class);

    Interest interest = db.getInterestRepository().findById(response.getId()).orElseThrow();

    assertThat(
            RegisterInterestRequest.newBuilder()
                .setFirstName(interest.getFirstName())
                .setLastName(interest.getLastName())
                .setEmailAddress(interest.getEmailAddress())
                .setProfession(interest.getProfession())
                .setReasonForInterest(interest.getReasonForInterest())
                .setDistrictName(interest.getDistrictName())
                .setSchoolName(interest.getSchoolName())
                .setAddressLine1(interest.getAddressLine1())
                .setAddressLine2(interest.getAddressLine2())
                .setCity(interest.getCity())
                .setState(interest.getState())
                .setZipCode(interest.getZipCode())
                .setNumTeachers(interest.getNumTeachers())
                .setNumStudents(interest.getNumStudents())
                .build())
        .isEqualTo(request);
  }
}
