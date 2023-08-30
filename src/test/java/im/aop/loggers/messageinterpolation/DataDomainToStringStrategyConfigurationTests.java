package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * Tests for {@link DataDomainToStringStrategyConfiguration}.
 *
 * @author Andy Lian
 */
class DataDomainToStringStrategyConfigurationTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withBean(ReflectionToStringProperties.class)
          .withBean(ReflectionToStringStrategy.class)
          .withBean(ObjectToStringStrategy.class)
          .withBean(IterableToStringStrategy.class)
          .withUserConfiguration(DataDomainToStringStrategyConfiguration.class);

  @Test
  void pageableToStringStrategyNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(PageableToStringStrategy.class))
              .isNotNull()
              .isExactlyInstanceOf(PageableToStringStrategy.class);
        });
  }

  @Test
  void pageToStringStrategyNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(PageToStringStrategy.class))
              .isNotNull()
              .isExactlyInstanceOf(PageToStringStrategy.class);
        });
  }

  @Test
  void sliceToStringStrategyNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(SliceToStringStrategy.class))
              .isNotNull()
              .isExactlyInstanceOf(SliceToStringStrategy.class);
        });
  }
}
