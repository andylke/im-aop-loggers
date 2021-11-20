package im.aop.loggers.advice.around;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.messageinterpolation.StringSubstitutorConfiguration;

/**
 * Tests for {@link LogAroundConfiguration}.
 *
 * @author Andy Lian
 */
class LogAroundConfigurationTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(StringSubstitutorConfiguration.class, LogAroundConfiguration.class)
          .withBean(AopLoggersProperties.class);

  @Test
  void logAroundAdviceNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(LogAroundAdvice.class))
              .isNotNull()
              .isExactlyInstanceOf(LogAroundAdvice.class);
        });
  }

  @Test
  void logAroundServiceNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(LogAroundService.class))
              .isNotNull()
              .isExactlyInstanceOf(LogAroundService.class);
        });
  }
}
