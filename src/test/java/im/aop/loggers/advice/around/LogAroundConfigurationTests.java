package im.aop.loggers.advice.around;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import im.aop.loggers.AopLoggersProperties;

/**
 * Tests for {@link LogAroundConfiguration}.
 *
 * @author Andy Lian
 */
class LogAroundConfigurationTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(LogAroundConfiguration.class)
          .withBean(AopLoggersProperties.class);

  @Test
  void logAroundAdvice() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(LogAroundAdvice.class))
              .isNotNull()
              .isExactlyInstanceOf(LogAroundAdvice.class);
        });
  }

  @Test
  void logAroundService() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(LogAroundService.class))
              .isNotNull()
              .isExactlyInstanceOf(LogAroundService.class);
        });
  }
}
