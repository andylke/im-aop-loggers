package im.aop.loggers.advice.before;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.messageinterpolation.StringSubstitutorConfiguration;

/**
 * Tests for {@link LogBeforeConfiguration}.
 *
 * @author Andy Lian
 */
class LogBeforeConfigurationTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(StringSubstitutorConfiguration.class, LogBeforeConfiguration.class)
          .withBean(AopLoggersProperties.class);

  @Test
  void logBeforeAdviceNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(LogBeforeAdvice.class))
              .isNotNull()
              .isExactlyInstanceOf(LogBeforeAdvice.class);
        });
  }

  @Test
  void logBeforeServiceNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(LogBeforeService.class))
              .isNotNull()
              .isExactlyInstanceOf(LogBeforeService.class);
        });
  }
}
