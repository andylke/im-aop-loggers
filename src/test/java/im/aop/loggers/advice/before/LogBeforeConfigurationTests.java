package im.aop.loggers.advice.before;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import im.aop.loggers.AopLoggersProperties;

/**
 * Tests for {@link LogBeforeConfiguration}.
 *
 * @author Andy Lian
 */
class LogBeforeConfigurationTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(LogBeforeConfiguration.class)
          .withBean(AopLoggersProperties.class);

  @Test
  void logBeforeAdvice() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(LogBeforeAdvice.class))
              .isNotNull()
              .isExactlyInstanceOf(LogBeforeAdvice.class);
        });
  }

  @Test
  void logBeforeService() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(LogBeforeService.class))
              .isNotNull()
              .isExactlyInstanceOf(LogBeforeService.class);
        });
  }
}
