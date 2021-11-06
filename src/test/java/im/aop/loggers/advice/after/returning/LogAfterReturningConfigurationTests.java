package im.aop.loggers.advice.after.returning;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import im.aop.loggers.AopLoggersProperties;

/**
 * Tests for {@link LogAfterReturningConfiguration}.
 *
 * @author Andy Lian
 */
class LogAfterReturningConfigurationTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(LogAfterReturningConfiguration.class)
          .withBean(AopLoggersProperties.class);

  @Test
  void logAfterReturningAdvice() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(LogAfterReturningAdvice.class))
              .isNotNull()
              .isExactlyInstanceOf(LogAfterReturningAdvice.class);
        });
  }

  @Test
  void logAfterReturningService() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(LogAfterReturningService.class))
              .isNotNull()
              .isExactlyInstanceOf(LogAfterReturningService.class);
        });
  }
}
