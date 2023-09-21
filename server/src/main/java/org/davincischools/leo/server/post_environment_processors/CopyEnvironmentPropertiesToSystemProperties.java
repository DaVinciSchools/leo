package org.davincischools.leo.server.post_environment_processors;

import com.google.common.collect.ImmutableList;
import java.util.Optional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/** Copies Spring properties to the System properties for classes that are not aware of Spring. */
public class CopyEnvironmentPropertiesToSystemProperties implements EnvironmentPostProcessor {

  @Override
  public void postProcessEnvironment(
      ConfigurableEnvironment environment, SpringApplication application) {
    // These environment properties need to be copied to System properties.
    ImmutableList.of(
            "hibernate.dialect",
            "jdk.httpclient.allowRestrictedHeaders",
            "spring.jpa.hibernate.ddl-auto")
        .forEach(property -> copyEnvironmentPropertyToSystemProperty(environment, property));
  }

  void copyEnvironmentPropertyToSystemProperty(Environment environment, String propertyName) {
    if (System.getProperty(propertyName) == null) {
      Optional.ofNullable(environment.getProperty(propertyName))
          .ifPresent(value -> System.setProperty(propertyName, value));
    }
  }
}
