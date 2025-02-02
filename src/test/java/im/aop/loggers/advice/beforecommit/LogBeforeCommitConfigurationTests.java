package im.aop.loggers.advice.beforecommit;

import static org.assertj.core.api.Assertions.assertThat;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.messageinterpolation.StringSubstitutorConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * Tests for {@link LogBeforeCommitConfiguration}.
 *
 * @author Andy Lian
 */
class LogBeforeCommitConfigurationTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(
              StringSubstitutorConfiguration.class, LogBeforeCommitConfiguration.class)
          .withBean(AopLoggersProperties.class);

  @Test
  void logBeforeAdviceNotNull() {
    runner.run(
        context -> {
          assertThat(context.getBean(LogBeforeCommitAdvice.class))
              .isNotNull()
              .isExactlyInstanceOf(LogBeforeCommitAdvice.class);
        });
  }

  @Test
  void logBeforeServiceNotNull() {
    runner.run(
        context -> {
          assertThat(context.getBean(LogBeforeCommitService.class))
              .isNotNull()
              .isExactlyInstanceOf(LogBeforeCommitService.class);
        });
  }
}
