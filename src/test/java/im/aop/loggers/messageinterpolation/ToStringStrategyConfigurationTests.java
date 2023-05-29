package im.aop.loggers.messageinterpolation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ToStringStrategyConfiguration}.
 *
 * @author Andy Lian
 */
class ToStringStrategyConfigurationTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner().withUserConfiguration(ToStringStrategyConfiguration.class);

  @Test
  void objectToStringStrategyNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(ObjectToStringStrategy.class))
              .isNotNull()
              .isExactlyInstanceOf(ObjectToStringStrategy.class);
        });
  }

  @Test
  void optionalToStringStrategyNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(OptionalToStringStrategy.class))
              .isNotNull()
              .isExactlyInstanceOf(OptionalToStringStrategy.class);
        });
  }

  @Test
  void arrayToStringStrategyNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(ArrayToStringStrategy.class))
              .isNotNull()
              .isExactlyInstanceOf(ArrayToStringStrategy.class);
        });
  }

  @Test
  void iterableToStringStrategyNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(IterableToStringStrategy.class))
              .isNotNull()
              .isExactlyInstanceOf(IterableToStringStrategy.class);
        });
  }

  @Test
  void defaultToStringStrategyFactoryNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(ToStringStrategyFactory.class))
              .isNotNull()
              .isExactlyInstanceOf(DefaultToStringStrategyFactory.class);
        });
  }
}
