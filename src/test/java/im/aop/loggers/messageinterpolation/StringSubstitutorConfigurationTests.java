package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;

import im.aop.loggers.AopLoggersProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

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
  void dataDomainToStringStrategyConfigurationNotNull() {
    runner.run(
        context -> {
          assertThat(context.getBean(DataDomainToStringStrategyConfiguration.class)).isNotNull();
        });
  }

  @Test
  void toStringStrategyConfigurationNotNull() {
    runner.run(
        context -> {
          assertThat(context.getBean(ToStringStrategyConfiguration.class)).isNotNull();
        });
  }

  @Test
  void toStringStrategyFactoryNotNull() {
    runner.run(
        context -> {
          assertThat(context.getBean(ToStringStrategyFactory.class)).isNotNull();
        });
  }

  @Test
  void stringSubstitutorNotNull() {
    runner.run(
        context -> {
          assertThat(context.getBean(StringSubstitutor.class))
              .isNotNull()
              .isExactlyInstanceOf(StringSubstitutor.class);
        });
  }

  @Test
  void elapsedStringSupplierRegistrarNotNull() {
    runner.run(
        context -> {
          assertThat(context.getBean(ElapsedStringSupplierRegistrar.class))
              .isNotNull()
              .isExactlyInstanceOf(ElapsedStringSupplierRegistrar.class);
        });
  }

  @Test
  void elapsedTimeLimitStringSupplierRegistrarNotNull() {
    runner.run(
        context -> {
          assertThat(context.getBean(ElapsedTimeLimitStringSupplierRegistrar.class))
              .isNotNull()
              .isExactlyInstanceOf(ElapsedTimeLimitStringSupplierRegistrar.class);
        });
  }

  @Test
  void exceptionStringSupplierRegistrarNotNull() {
    runner.run(
        context -> {
          assertThat(context.getBean(ExceptionStringSupplierRegistrar.class))
              .isNotNull()
              .isExactlyInstanceOf(ExceptionStringSupplierRegistrar.class);
        });
  }

  @Test
  void joinPointStringSupplierRegistrarNotNull() {
    runner.run(
        context -> {
          assertThat(context.getBean(JoinPointStringSupplierRegistrar.class))
              .isNotNull()
              .isExactlyInstanceOf(JoinPointStringSupplierRegistrar.class);
        });
  }

  @Test
  void returnValueStringSupplierRegistrarNotNull() {
    runner.run(
        context -> {
          assertThat(context.getBean(ReturnValueStringSupplierRegistrar.class))
              .isNotNull()
              .isExactlyInstanceOf(ReturnValueStringSupplierRegistrar.class);
        });
  }
}
