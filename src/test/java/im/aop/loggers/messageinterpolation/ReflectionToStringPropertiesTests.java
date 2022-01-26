package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * Tests for {@link ReflectionToStringProperties}.
 *
 * @author Andy Lian
 */
class ReflectionToStringPropertiesTests {

  @TestConfiguration(proxyBeanMethods = false)
  @EnableConfigurationProperties({ReflectionToStringProperties.class})
  static class ReflectionToStringPropertiesTestConfiguration {}

  private ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(ReflectionToStringPropertiesTestConfiguration.class);

  @Test
  void baseClasses_defaultValue() {
    runner.run(
        (context) -> {
          final ReflectionToStringProperties properties =
              context.getBean(ReflectionToStringProperties.class);
          assertThat(properties.getBaseClasses()).isEmpty();
        });
  }

  @Test
  void baseClasses_givenPropertyValue() {
    runner
        .withPropertyValues(
            ReflectionToStringProperties.PREFIX + ".base-classes.0=foo",
            ReflectionToStringProperties.PREFIX + ".base-classes.1=bar")
        .run(
            (context) -> {
              final ReflectionToStringProperties properties =
                  context.getBean(ReflectionToStringProperties.class);
              assertThat(properties.getBaseClasses()).containsExactly("foo", "bar");
            });
  }

  @Test
  void excludeNullValues_defaultValue() {
    runner.run(
        (context) -> {
          final ReflectionToStringProperties properties =
              context.getBean(ReflectionToStringProperties.class);
          assertThat(properties.isExcludeNullValues()).isTrue();
        });
  }

  @Test
  void excludeNullValues_givenPropertyValue() {
    runner
        .withPropertyValues(ReflectionToStringProperties.PREFIX + ".exclude-null-values=false")
        .run(
            (context) -> {
              final ReflectionToStringProperties properties =
                  context.getBean(ReflectionToStringProperties.class);
              assertThat(properties.isExcludeNullValues()).isFalse();
            });
  }

  @Test
  void excludeFieldNames_defaultValue() {
    runner.run(
        (context) -> {
          final ReflectionToStringProperties properties =
              context.getBean(ReflectionToStringProperties.class);
          assertThat(properties.getExcludeFieldNames())
              .containsExactly("username", "password", "passphrase", "secret");
        });
  }

  @Test
  void excludeFieldNames_givenPropertyValue() {
    runner
        .withPropertyValues(
            ReflectionToStringProperties.PREFIX + ".exclude-field-names.0=foo",
            ReflectionToStringProperties.PREFIX + ".exclude-field-names.1=bar")
        .run(
            (context) -> {
              final ReflectionToStringProperties properties =
                  context.getBean(ReflectionToStringProperties.class);
              assertThat(properties.getExcludeFieldNames())
                  .containsExactly("username", "password", "passphrase", "secret", "foo", "bar");
            });
  }
}
