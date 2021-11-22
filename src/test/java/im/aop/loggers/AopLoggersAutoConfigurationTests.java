package im.aop.loggers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import im.aop.loggers.advice.afterreturning.LogAfterReturningConfiguration;
import im.aop.loggers.advice.afterthrowing.LogAfterThrowingConfiguration;
import im.aop.loggers.advice.around.LogAroundConfiguration;
import im.aop.loggers.advice.before.LogBeforeConfiguration;
import im.aop.loggers.messageinterpolation.StringSubstitutorConfiguration;

/**
 * Tests for {@link AopLoggersAutoConfiguration}.
 *
 * @author Andy Lian
 */
class AopLoggersAutoConfigurationTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withConfiguration(AutoConfigurations.of(AopLoggersAutoConfiguration.class));

  @Test
  void aopLoggersPropertiesNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(AopLoggersProperties.class))
              .isNotNull()
              .isExactlyInstanceOf(AopLoggersProperties.class);
        });
  }

  @Test
  void aopLoggersPropertiesIsNull_whenEnabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=true")
        .run(
            (context) -> {
              assertThat(context.getBean(AopLoggersProperties.class))
                  .isNotNull()
                  .isExactlyInstanceOf(AopLoggersProperties.class);
            });
  }

  @Test
  void aopLoggersPropertiesIsNull_whenDisabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              assertThrows(
                  NoSuchBeanDefinitionException.class,
                  () -> assertThat(context.getBean(AopLoggersProperties.class)));
            });
  }

  @Test
  void stringSubstitutorConfigurationNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(StringSubstitutorConfiguration.class))
              .isNotNull()
              .isExactlyInstanceOf(StringSubstitutorConfiguration.class);
        });
  }

  @Test
  void stringSubstitutorConfigurationIsNull_whenEnabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=true")
        .run(
            (context) -> {
              assertThat(context.getBean(StringSubstitutorConfiguration.class))
                  .isNotNull()
                  .isExactlyInstanceOf(StringSubstitutorConfiguration.class);
            });
  }

  @Test
  void stringSubstitutorConfigurationIsNull_whenDisabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              assertThrows(
                  NoSuchBeanDefinitionException.class,
                  () -> assertThat(context.getBean(StringSubstitutorConfiguration.class)));
            });
  }

  @Test
  void logAfterReturningConfigurationNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(LogAfterReturningConfiguration.class))
              .isNotNull()
              .isExactlyInstanceOf(LogAfterReturningConfiguration.class);
        });
  }

  @Test
  void logAfterReturningConfigurationIsNull_whenEnabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=true")
        .run(
            (context) -> {
              assertThat(context.getBean(LogAfterReturningConfiguration.class))
                  .isNotNull()
                  .isExactlyInstanceOf(LogAfterReturningConfiguration.class);
            });
  }

  @Test
  void logAfterReturningConfigurationIsNull_whenDisabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              assertThrows(
                  NoSuchBeanDefinitionException.class,
                  () -> assertThat(context.getBean(LogAfterReturningConfiguration.class)));
            });
  }

  @Test
  void logAfterThrowingConfigurationNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(LogAfterThrowingConfiguration.class))
              .isNotNull()
              .isExactlyInstanceOf(LogAfterThrowingConfiguration.class);
        });
  }

  @Test
  void logAfterThrowingConfigurationIsNull_whenEnabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=true")
        .run(
            (context) -> {
              assertThat(context.getBean(LogAfterThrowingConfiguration.class))
                  .isNotNull()
                  .isExactlyInstanceOf(LogAfterThrowingConfiguration.class);
            });
  }

  @Test
  void logAfterThrowingConfigurationIsNull_whenDisabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              assertThrows(
                  NoSuchBeanDefinitionException.class,
                  () -> assertThat(context.getBean(LogAfterThrowingConfiguration.class)));
            });
  }

  @Test
  void logAroundConfigurationNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(LogAroundConfiguration.class))
              .isNotNull()
              .isExactlyInstanceOf(LogAroundConfiguration.class);
        });
  }

  @Test
  void logAroundConfigurationIsNull_whenEnabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=true")
        .run(
            (context) -> {
              assertThat(context.getBean(LogAroundConfiguration.class))
                  .isNotNull()
                  .isExactlyInstanceOf(LogAroundConfiguration.class);
            });
  }

  @Test
  void logAroundConfigurationIsNull_whenDisabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              assertThrows(
                  NoSuchBeanDefinitionException.class,
                  () -> assertThat(context.getBean(LogAroundConfiguration.class)));
            });
  }

  @Test
  void logBeforeConfigurationNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(LogBeforeConfiguration.class))
              .isNotNull()
              .isExactlyInstanceOf(LogBeforeConfiguration.class);
        });
  }

  @Test
  void logBeforeConfigurationIsNull_whenEnabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=true")
        .run(
            (context) -> {
              assertThat(context.getBean(LogBeforeConfiguration.class))
                  .isNotNull()
                  .isExactlyInstanceOf(LogBeforeConfiguration.class);
            });
  }

  @Test
  void logBeforeConfigurationIsNull_whenDisabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              assertThrows(
                  NoSuchBeanDefinitionException.class,
                  () -> assertThat(context.getBean(LogBeforeConfiguration.class)));
            });
  }
}
