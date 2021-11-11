package im.aop.loggers.advice.after.returning;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.messageinterpolation.StringSubstitutorConfiguration;

/**
 * Tests for {@link LogAfterReturningConfiguration}.
 *
 * @author Andy Lian
 */
class LogAfterReturningConfigurationTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(
              StringSubstitutorConfiguration.class, LogAfterReturningConfiguration.class)
          .withBean(AopLoggersProperties.class);

  @Test
  void logAfterReturningAdviceNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(LogAfterReturningAdvice.class))
              .isNotNull()
              .isExactlyInstanceOf(LogAfterReturningAdvice.class);
        });
  }

  @Test
  void logAfterReturningServiceNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(LogAfterReturningService.class))
              .isNotNull()
              .isExactlyInstanceOf(LogAfterReturningService.class);
        });
  }

  @Test
  void logAfterReturningAdviceBeanNotNull_whenEnabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=true")
        .run(
            (context) -> {
              assertThat(context.getBean(LogAfterReturningAdvice.class))
                  .isNotNull()
                  .isExactlyInstanceOf(LogAfterReturningAdvice.class);
            });
  }

  @Test
  void logAfterReturningServiceNotNull_whenEnabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=true")
        .run(
            (context) -> {
              assertThat(context.getBean(LogAfterReturningService.class))
                  .isNotNull()
                  .isExactlyInstanceOf(LogAfterReturningService.class);
            });
  }

  @Test
  void logAfterReturningAdviceBeanIsNull_whenDisabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              assertThrows(
                  NoSuchBeanDefinitionException.class,
                  () -> assertThat(context.getBean(LogAfterReturningAdvice.class)));
            });
  }

  @Test
  void logAfterReturningServiceIsNull_whenDisabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              assertThrows(
                  NoSuchBeanDefinitionException.class,
                  () -> assertThat(context.getBean(LogAfterReturningService.class)));
            });
  }
}
