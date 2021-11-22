package im.aop.loggers.advice.afterthrowing;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.messageinterpolation.StringSubstitutorConfiguration;

/**
 * Tests for {@link LogAfterThrowingConfiguration}.
 *
 * @author Andy Lian
 */
class LogAfterThrowingConfigurationTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(
              StringSubstitutorConfiguration.class, LogAfterThrowingConfiguration.class)
          .withBean(AopLoggersProperties.class);

  @Test
  void logAfterThrowingAdviceNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(LogAfterThrowingAdvice.class))
              .isNotNull()
              .isExactlyInstanceOf(LogAfterThrowingAdvice.class);
        });
  }

  @Test
  void logAfterThrowingServiceNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(LogAfterThrowingService.class))
              .isNotNull()
              .isExactlyInstanceOf(LogAfterThrowingService.class);
        });
  }
}
