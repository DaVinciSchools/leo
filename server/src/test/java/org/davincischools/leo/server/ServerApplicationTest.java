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
import org.davincischools.leo.protos.pl_types.UserX;
import org.davincischools.leo.protos.user_x_management.RegisterUserXRequest;
import org.davincischools.leo.protos.user_x_management.RegisterUserXResponse;
import org.davincischools.leo.server.controllers.ReactResourceController;
import org.davincischools.leo.server.test_helpers.WebSession;
import org.davincischools.leo.server.utils.ProtoDaoConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {ServerApplication.class, TestData.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class ServerApplicationTest {
  @Autowired public ReactResourceController controller;
  @Autowired public Database db;
  @Autowired public TestData testData;

  @Value(value = "${" + LOCAL_SERVER_PORT_PROPERTY + "}")
  private int port;

  private WebSession session = new WebSession();

  @Before
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
            BodyInserters.fromFormData("username", testData.getAdminX().getEmailAddress())
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
                assertThat(bodyAsProto(spec, UserX.class))
                    .comparingExpectedFieldsOnly()
                    .isEqualTo(
                        UserX.newBuilder()
                            .setEmailAddress(testData.getAdminX().getEmailAddress())
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
                BodyInserters.fromFormData("username", testData.getAdminX().getEmailAddress())
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
            spec ->
                assertThat(bodyAsProto(spec, UserX.class)).isEqualTo(UserX.getDefaultInstance()));
  }

  @Test
  public void registerUserXTest() throws Exception {
    RegisterUserXRequest request =
        RegisterUserXRequest.newBuilder()
            .setFirstName("first")
            .setLastName("last")
            .setEmailAddress("email@address.net")
            .setPassword("password")
            .setVerifyPassword("verify_password")
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

    RegisterUserXResponse response =
        session.protoRequest(
            webClient(),
            "/api/protos/UserXManagementService/RegisterUserX",
            request,
            RegisterUserXResponse.class);

    org.davincischools.leo.database.daos.UserX userX =
        db.getUserXRepository().findByEmailAddress(request.getEmailAddress()).orElseThrow();
    Interest interest =
        db.getInterestRepository().findById(userX.getInterest().getId()).orElseThrow();

    assertThat(ProtoDaoConverter.toUserXProto(userX, null).build())
        .ignoringFields(UserX.USER_X_ID_FIELD_NUMBER)
        .isEqualTo(
            UserX.newBuilder()
                .setEmailAddress(request.getEmailAddress())
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName())
                .setIsDemo(true)
                .setIsAuthenticated(true)
                .build());

    assertThat(ProtoDaoConverter.toRegisterUserXRequestProto(interest, null).build())
        .ignoringFields(
            RegisterUserXRequest.PASSWORD_FIELD_NUMBER,
            RegisterUserXRequest.VERIFY_PASSWORD_FIELD_NUMBER)
        .isEqualTo(request);
  }
}
