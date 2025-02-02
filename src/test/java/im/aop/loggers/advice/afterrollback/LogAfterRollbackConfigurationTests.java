package im.aop.loggers.advice.afterrollback;

import static org.assertj.core.api.Assertions.assertThat;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.messageinterpolation.StringSubstitutorConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * Tests for {@link LogAfterRollbackConfiguration}.
 *
 * @author Andy Lian
 */
class LogAfterRollbackConfigurationTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(
              StringSubstitutorConfiguration.class, LogAfterRollbackConfiguration.class)
          .withBean(AopLoggersProperties.class);

  @Test
  void logAfterAdviceNotNull() {
    runner.run(
        context -> {
          assertThat(context.getBean(LogAfterRollbackAdvice.class))
              .isNotNull()
              .isExactlyInstanceOf(LogAfterRollbackAdvice.class);
        });
  }

  @Test
  void logAfterServiceNotNull() {
    runner.run(
        context -> {
          assertThat(context.getBean(LogAfterRollbackService.class))
              .isNotNull()
              .isExactlyInstanceOf(LogAfterRollbackService.class);
        });
  }
}
