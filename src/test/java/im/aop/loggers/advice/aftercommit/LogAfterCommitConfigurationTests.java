package im.aop.loggers.advice.aftercommit;

import static org.assertj.core.api.Assertions.assertThat;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.messageinterpolation.StringSubstitutorConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * Tests for {@link LogAfterCommitConfiguration}.
 *
 * @author Andy Lian
 */
class LogAfterCommitConfigurationTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(
              StringSubstitutorConfiguration.class, LogAfterCommitConfiguration.class)
          .withBean(AopLoggersProperties.class);

  @Test
  void logAfterAdviceNotNull() {
    runner.run(
        context -> {
          assertThat(context.getBean(LogAfterCommitAdvice.class))
              .isNotNull()
              .isExactlyInstanceOf(LogAfterCommitAdvice.class);
        });
  }

  @Test
  void logAfterServiceNotNull() {
    runner.run(
        context -> {
          assertThat(context.getBean(LogAfterCommitService.class))
              .isNotNull()
              .isExactlyInstanceOf(LogAfterCommitService.class);
        });
  }
}
