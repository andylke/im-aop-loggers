package im.aop.loggers.advice.after.throwing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
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

  @Test
  void logAfterThrowingAdviceNotNull_whenEnabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=true")
        .run(
            (context) -> {
              assertThat(context.getBean(LogAfterThrowingAdvice.class))
                  .isNotNull()
                  .isExactlyInstanceOf(LogAfterThrowingAdvice.class);
            });
  }

  @Test
  void logAfterThrowingServiceNotNull_whenEnabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=true")
        .run(
            (context) -> {
              assertThat(context.getBean(LogAfterThrowingService.class))
                  .isNotNull()
                  .isExactlyInstanceOf(LogAfterThrowingService.class);
            });
  }

  @Test
  void logAfterAdviceIsNull_whenDisabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              assertThrows(
                  NoSuchBeanDefinitionException.class,
                  () -> context.getBean(LogAfterThrowingAdvice.class));
            });
  }

  @Test
  void logAfterServiceIsNull_whenDisabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              assertThrows(
                  NoSuchBeanDefinitionException.class,
                  () -> context.getBean(LogAfterThrowingService.class));
            });
  }
}
