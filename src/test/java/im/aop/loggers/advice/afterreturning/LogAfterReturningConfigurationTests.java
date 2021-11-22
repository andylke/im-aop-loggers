package im.aop.loggers.advice.afterreturning;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
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
}
