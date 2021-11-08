package im.aop.loggers.advice.before;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
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

  @Test
  void logBeforeAdviceNotNull_whenEnabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=true")
        .run(
            (context) -> {
              assertThat(context.getBean(LogBeforeAdvice.class))
                  .isNotNull()
                  .isExactlyInstanceOf(LogBeforeAdvice.class);
            });
  }

  @Test
  void logBeforeServiceNotNull_whenEnabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=true")
        .run(
            (context) -> {
              assertThat(context.getBean(LogBeforeService.class))
                  .isNotNull()
                  .isExactlyInstanceOf(LogBeforeService.class);
            });
  }

  @Test
  void logBeforeAdviceIsNull_whenDisabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              assertThrows(
                  NoSuchBeanDefinitionException.class,
                  () -> context.getBean(LogBeforeAdvice.class));
            });
  }

  @Test
  void logBeforeServiceIsNull_whenDisabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              assertThrows(
                  NoSuchBeanDefinitionException.class,
                  () -> context.getBean(LogBeforeService.class));
            });
  }
}
