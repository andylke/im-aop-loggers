package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import im.aop.loggers.AopLoggersProperties;

/**
 * Tests for {@link StringSubstitutorConfiguration}.
 *
 * @author Andy Lian
 */
class StringSubstitutorConfigurationTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(StringSubstitutorConfiguration.class)
          .withBean(AopLoggersProperties.class);

  @Test
  void stringSubstitutorNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(StringSubstitutor.class))
              .isNotNull()
              .isExactlyInstanceOf(StringSubstitutor.class);
        });
  }

  @Test
  void elapsedStringSupplierRegistrarNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(ElapsedStringSupplierRegistrar.class))
              .isNotNull()
              .isExactlyInstanceOf(ElapsedStringSupplierRegistrar.class);
        });
  }

  @Test
  void elapsedTimeLimitStringSupplierRegistrarNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(ElapsedTimeLimitStringSupplierRegistrar.class))
              .isNotNull()
              .isExactlyInstanceOf(ElapsedTimeLimitStringSupplierRegistrar.class);
        });
  }

  @Test
  void exceptionStringSupplierRegistrarNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(ExceptionStringSupplierRegistrar.class))
              .isNotNull()
              .isExactlyInstanceOf(ExceptionStringSupplierRegistrar.class);
        });
  }

  @Test
  void joinPointStringSupplierRegistrarNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(JoinPointStringSupplierRegistrar.class))
              .isNotNull()
              .isExactlyInstanceOf(JoinPointStringSupplierRegistrar.class);
        });
  }

  @Test
  void returnValueStringSupplierRegistrarNotNull() {
    runner.run(
        (context) -> {
          assertThat(context.getBean(ReturnValueStringSupplierRegistrar.class))
              .isNotNull()
              .isExactlyInstanceOf(ReturnValueStringSupplierRegistrar.class);
        });
  }
}
