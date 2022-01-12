package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

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
          assertThat(context.getBean(ToStringStrategy.class))
              .isNotNull()
              .isExactlyInstanceOf(ObjectToStringStrategy.class);
        });
  }
}
