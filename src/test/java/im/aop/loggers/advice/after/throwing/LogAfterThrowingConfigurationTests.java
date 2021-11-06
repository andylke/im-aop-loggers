package im.aop.loggers.advice.after.throwing;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import im.aop.loggers.AopLoggersProperties;

/**
 * Tests for {@link LogAfterThrowingConfiguration}.
 *
 * @author Andy Lian
 */
class LogAfterThrowingConfigurationTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(LogAfterThrowingConfiguration.class)
          .withBean(AopLoggersProperties.class);

  @Test
  void logAfterThrowingAdvice() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(LogAfterThrowingAdvice.class))
              .isNotNull()
              .isExactlyInstanceOf(LogAfterThrowingAdvice.class);
        });
  }

  @Test
  void logAfterThrowingService() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(LogAfterThrowingService.class))
              .isNotNull()
              .isExactlyInstanceOf(LogAfterThrowingService.class);
        });
  }
}
