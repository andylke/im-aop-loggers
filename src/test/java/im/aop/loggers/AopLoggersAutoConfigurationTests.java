package im.aop.loggers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import im.aop.loggers.advice.after.returning.LogAfterReturningAdvice;
import im.aop.loggers.advice.after.returning.LogAfterReturningService;
import im.aop.loggers.advice.after.throwing.LogAfterThrowingAdvice;
import im.aop.loggers.advice.after.throwing.LogAfterThrowingService;
import im.aop.loggers.advice.around.LogAroundAdvice;
import im.aop.loggers.advice.around.LogAroundService;
import im.aop.loggers.advice.before.LogBeforeAdvice;
import im.aop.loggers.advice.before.LogBeforeService;

/**
 * Tests for {@link AopLoggersAutoConfiguration}.
 *
 * @author Andy Lian
 */
class AopLoggersAutoConfigurationTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withConfiguration(AutoConfigurations.of(AopLoggersAutoConfiguration.class));

  @Test
  void aopLoggersProperties() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(AopLoggersProperties.class))
              .isNotNull()
              .isExactlyInstanceOf(AopLoggersProperties.class);
        });
  }

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
