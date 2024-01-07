package org.davincischools.leo.server;

import static org.davincischools.leo.server.SpringConstants.LOCAL_SERVER_PORT_PROPERTY;
import static org.davincischools.leo.server.utils.ProtoDaoUtils.toUserXProto;

import com.google.common.base.Strings;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.post_environment_processors.LoadCustomProjectLeoProperties;
import org.davincischools.leo.database.test.TestDatabase;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.repos.GetUserXsParams;
import org.davincischools.leo.server.utils.ApplicationExceptionConsoleLogger;
import org.davincischools.leo.server.utils.QueryWithNullsToRecordConverter;
import org.davincischools.leo.server.utils.http_executor.HttpExecutor;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorArgumentResolver;
import org.davincischools.leo.server.utils.http_user_x.HttpUserXArgumentResolver;
import org.davincischools.leo.server.utils.http_user_x.HttpUserXService;
import org.davincischools.leo.server.utils.http_user_x.UserXDetails;
import org.davincischools.leo.server.utils.task_queue.workers.ReplyToPostsWorker;
import org.davincischools.leo.server.utils.task_queue.workers.project_generators.ProjectGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@SpringBootApplication(
    scanBasePackages = "org.davincischools.leo.server",
    scanBasePackageClasses = {
      ApplicationExceptionConsoleLogger.class,
      Database.class,
      HttpExecutor.class,
      HttpUserXService.class,
      ProjectGenerator.class,
      ReplyToPostsWorker.class,
      TestDatabase.class,
      UserX.class,
    })
public class ServerApplication {

  private static final Logger logger = LogManager.getLogger();

  private static final String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";

  @Configuration
  static class ServerApplicationConfigurer extends WebMvcConfigurationSupport {

    private final Database db;
    private final EntityManager entityManager;

    public ServerApplicationConfigurer(Database db, EntityManager entityManager) {
      this.db = db;
      this.entityManager = entityManager;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
      // Add the Protobuf converters to the list of converters.
      //
      // These need to be at the beginning of the list to take precedence over
      // other converters. Otherwise, a different, built-in converter fails
      // with a "Direct self-reference leading to cycle" error.
      converters.add(0, new ProtobufHttpMessageConverter());
      converters.add(1, new ProtobufJsonFormatHttpMessageConverter());
    }

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
      argumentResolvers.add(0, new HttpUserXArgumentResolver(db, entityManager));
      argumentResolvers.add(1, new HttpExecutorArgumentResolver(db, entityManager));
    }
  }

  @Configuration
  @EnableWebSecurity
  static class SecurityConfigurer {

    @Autowired Environment environment;
    @Autowired Database db;

    @Bean
    public SecurityFilterChain buildSecurityFilterChain(HttpSecurity http) throws Exception {
      // Public resources.
      RequestMatcher[] publicMatchers =
          new RequestMatcher[] {
            // This needs to be kept in sync with ReactResourceController.
            new AntPathRequestMatcher("/", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/api/login.html", HttpMethod.POST.name()),
            new AntPathRequestMatcher("/api/logout.html", HttpMethod.POST.name()),
            new AntPathRequestMatcher("/api/FileService/GetFile**", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/api/FileService/PostFil**", HttpMethod.POST.name()),
            new AntPathRequestMatcher("/api/protos/ErrorService/**", HttpMethod.POST.name()),
            new AntPathRequestMatcher("/api/protos/InterestService/**", HttpMethod.POST.name()),
            new AntPathRequestMatcher(
                "/api/protos/ProjectManagementService/GetProjectDefinitionCategoryTypes",
                HttpMethod.POST.name()),
            new AntPathRequestMatcher(
                "/api/protos/ProjectManagementService/GenerateProjects", HttpMethod.POST.name()),
            new AntPathRequestMatcher(
                "/api/protos/UserXManagementService/RegisterUserX", HttpMethod.POST.name()),
            new AntPathRequestMatcher("/demos/**", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/docs/**", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/error**", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/favicon.*", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/gtag.js", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/images/**", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/index.html", HttpMethod.GET.name()),
            // The following exists for OAuth2 login. But, it seems to not be needed here.
            // new AntPathRequestMatcher("/login/oauth2/code/**", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/manifest.json", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/oauth2/authorization/**", HttpMethod.POST.name()),
            new AntPathRequestMatcher("/robots.txt", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/schools/**", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/static/**", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/users/login.html", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/users/logout.html", HttpMethod.GET.name()),
            // TODO: Move these out of the prod server.
            // React developer tools plugin.
            new AntPathRequestMatcher("/installHooks.js", HttpMethod.GET.name()),
            // Webpack server hot reload files:
            // https://github.com/webpack/webpack-dev-server.
            new AntPathRequestMatcher("/main.*.hot-update.js", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/main.*.hot-update.js.map", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/main.*.hot-update.json", HttpMethod.GET.name()),
          };

      // https://docs.spring.io/spring-security/reference/5.8/migration/servlet/exploits.html#_i_am_using_angularjs_or_another_javascript_framework
      CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
      XorCsrfTokenRequestAttributeHandler delegate = new XorCsrfTokenRequestAttributeHandler();
      HeaderWriterLogoutHandler clearSiteData =
          new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(Directive.ALL));

      http

          // Public content.
          .authorizeHttpRequests(config -> config.requestMatchers(publicMatchers).permitAll())

          // Prevent Cross-Site Request Forgery.
          .csrf(
              config ->
                  config
                      .ignoringRequestMatchers(publicMatchers)
                      .csrfTokenRepository(tokenRepository)
                      .csrfTokenRequestHandler(
                          (HttpServletRequest request,
                              HttpServletResponse response,
                              Supplier<CsrfToken> csrfToken) -> {
                            // The CSRF cookie isn't actually generated until the get() method
                            // is called, because it's generation is lazy. For the built-in
                            // login page, get() is called as part of creating the login HTML.
                            // But, get() is not called if you use an external login page,
                            // like we are doing. So, in order to force a CSRF token to be
                            // generated we manually call get() here. Once it's generated, it
                            // is automatically included as a cookie in the response.
                            csrfToken.get();

                            // Use only the handle() method of XorCsrfTokenRequestAttributeHandler
                            // and the default implementation of resolveCsrfTokenValue() from
                            // CsrfTokenRequestHandler
                            delegate.handle(request, response, csrfToken);
                          }))

          // Login
          .formLogin(
              config ->
                  config
                      .loginPage("/users/login.html")
                      .loginProcessingUrl("/api/login.html")
                      .successHandler(
                          (HttpServletRequest request,
                              HttpServletResponse response,
                              Authentication authentication) -> {
                            // Return the user in the reply after authentication.
                            UserX userX = ((UserXDetails) authentication.getPrincipal()).getUserX();
                            org.davincischools.leo.protos.pl_types.UserX.Builder userXProto =
                                toUserXProto(
                                        userX,
                                        org.davincischools.leo.protos.pl_types.UserX::newBuilder)
                                    .orElse(null);
                            if (userXProto == null) {
                              throw new IllegalStateException(
                                  "UserX proto is null after authentication.");
                            }

                            response.setContentType(
                                ProtobufHttpMessageConverter.PROTOBUF.toString());
                            response.setHeader(
                                ProtobufHttpMessageConverter.X_PROTOBUF_SCHEMA_HEADER,
                                userXProto.getDescriptorForType().getFile().getName());
                            response.setHeader(
                                ProtobufHttpMessageConverter.X_PROTOBUF_MESSAGE_HEADER,
                                userXProto.getDescriptorForType().getFullName());
                            response.getOutputStream().write(userXProto.build().toByteArray());
                            response.setStatus(HttpServletResponse.SC_OK);
                          })
                      .failureUrl("/users/login.html?failed=true")
                      .permitAll())

          // Logout
          .logout(
              config ->
                  config
                      .logoutUrl("/api/logout.html")
                      .logoutSuccessUrl("/")
                      .clearAuthentication(true)
                      .invalidateHttpSession(true)
                      .addLogoutHandler(clearSiteData)
                      .permitAll())

          // A user's profile requires full authentication.
          .authorizeHttpRequests(
              config ->
                  config
                      .requestMatchers(new AntPathRequestMatcher("/users/my-account.html"))
                      .fullyAuthenticated())

          // Remaining pages require authentication.
          .authorizeHttpRequests(config -> config.anyRequest().authenticated());

      var oauthRegistrations = createClientRegistrations();
      if (!oauthRegistrations.isEmpty()) {
        http // OAuth2 login.
            .oauth2Login(
            config ->
                config
                    .clientRegistrationRepository(
                        new InMemoryClientRegistrationRepository(createClientRegistrations()))
                    .authorizedClientService(
                        new InMemoryOAuth2AuthorizedClientService(
                            new InMemoryClientRegistrationRepository(createClientRegistrations())))
                    .successHandler(
                        (request, response, authentication) -> {
                          if (authentication.getPrincipal()
                                  instanceof OAuth2AuthenticatedPrincipal oauth
                              && !Strings.isNullOrEmpty(oauth.getAttribute("email"))) {
                            String email = oauth.getAttribute("email");
                            var userX =
                                db
                                    .getUserXRepository()
                                    .getUserXs(new GetUserXsParams().setHasEmailAddress(email))
                                    .stream()
                                    .findFirst();
                            if (userX.isEmpty()) {
                              userX =
                                  Optional.of(
                                      new UserX()
                                          .setCreationTime(Instant.now())
                                          .setEmailAddress(email));
                            }
                            db.getUserXRepository()
                                .save(
                                    userX
                                        .get()
                                        .setFirstName(oauth.getAttribute("given_name"))
                                        .setLastName(oauth.getAttribute("family_name"))
                                        .setEmailAddressVerified(
                                            Boolean.TRUE.equals(
                                                oauth.getAttribute("email_verified")))
                                        .setAvatarImageUrl(oauth.getAttribute("picture")));
                          }
                          response.sendRedirect("/users/login.html?loadCredentials=true");
                        })
                    .failureHandler(
                        (request, response, exception) -> {
                          logger
                              .atError()
                              .withThrowable(exception)
                              .log("OAuth2 login failed: {}", exception.getMessage());
                          response.encodeRedirectURL("/users/login.html?failed=true");
                        }));
      }

      // TODO: Set security realm.
      // http.httpBasic(config -> config.realmName("project.leo"))

      return http.build();
    }

    @Bean
    public List<ClientRegistration> createClientRegistrations() {
      var registrations =
          new ArrayList<
              org.springframework.security.oauth2.client.registration.ClientRegistration.Builder>();

      String googleClientId = environment.getProperty(CLIENT_PROPERTY_KEY + "google.client-id");
      String googleSecret = environment.getProperty(CLIENT_PROPERTY_KEY + "google.client-secret");
      if (!Strings.isNullOrEmpty(googleClientId) && !Strings.isNullOrEmpty(googleSecret)) {
        registrations.add(
            CommonOAuth2Provider.GOOGLE
                .getBuilder("google")
                .clientId(googleClientId)
                .clientSecret(googleSecret));
      } else {
        logger.atWarn().log("Google OAuth2 client not configured.");
      }

      String baseUrl = environment.getProperty("project_leo.base_url", "{baseUrl}");
      return registrations.stream()
          .map(
              registration ->
                  registration.redirectUri(baseUrl + "/{action}/oauth2/code/{registrationId}"))
          .map(ClientRegistration.Builder::build)
          .toList();
    }
  }

  public static void main(String[] args) throws IOException {
    // Hibernate annoyingly fails if a column is null when returning a record from a query.
    ((DefaultConversionService) DefaultConversionService.getSharedInstance())
        .addConverter(new QueryWithNullsToRecordConverter());

    // Load custom Project Leo properties into the environment.
    ConfigurableEnvironment environment = new StandardEnvironment();
    LoadCustomProjectLeoProperties.loadCustomProjectLeoProperties(environment);

    // Start the Project Leo server.
    SpringApplication sa = new SpringApplication(ServerApplication.class);
    sa.setEnvironment(environment);
    ApplicationContext context = sa.run(args);

    // Dump the list of beans in the context.
    logger.atInfo().log("Bean names available:");
    String[] beanNames = context.getBeanDefinitionNames();
    Arrays.sort(beanNames);
    for (String beanName : beanNames) {
      logger.atInfo().log("  - {}", beanName);
    }

    // Log the port that the server is running on.
    int serverPort =
        context.getEnvironment().getProperty(LOCAL_SERVER_PORT_PROPERTY, Integer.class, 0);
    logger.atInfo().log("Leo server started on port http://localhost:{}.", serverPort);
  }
}
