package im.aop.loggers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

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
  void aopLoggersConfigurationNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(AopLoggersConfiguration.class))
              .isNotNull()
              .isExactlyInstanceOf(AopLoggersConfiguration.class);
        });
  }
}
