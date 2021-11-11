package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
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
  void stringSubstitutorNotNull_whenEnabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=true")
        .run(
            (context) -> {
              assertThat(context.getBean(StringSubstitutor.class))
                  .isNotNull()
                  .isExactlyInstanceOf(StringSubstitutor.class);
            });
  }

  @Test
  void stringSubstitutorIsNull_whenDisabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              assertThrows(
                  NoSuchBeanDefinitionException.class,
                  () -> context.getBean(StringSubstitutor.class));
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
  void elapsedStringSupplierRegistrarNotNull_whenEnabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=true")
        .run(
            (context) -> {
              assertThat(context.getBean(ElapsedStringSupplierRegistrar.class))
                  .isNotNull()
                  .isExactlyInstanceOf(ElapsedStringSupplierRegistrar.class);
            });
  }

  @Test
  void elapsedStringSupplierRegistrarIsNull_whenDisabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              assertThrows(
                  NoSuchBeanDefinitionException.class,
                  () -> context.getBean(ElapsedStringSupplierRegistrar.class));
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
  void elapsedTimeLimitStringSupplierRegistrarNotNull_whenEnabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=true")
        .run(
            (context) -> {
              assertThat(context.getBean(ElapsedTimeLimitStringSupplierRegistrar.class))
                  .isNotNull()
                  .isExactlyInstanceOf(ElapsedTimeLimitStringSupplierRegistrar.class);
            });
  }

  @Test
  void elapsedTimeLimitStringSupplierRegistrarIsNull_whenDisabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              assertThrows(
                  NoSuchBeanDefinitionException.class,
                  () -> context.getBean(ElapsedTimeLimitStringSupplierRegistrar.class));
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
  void exceptionStringSupplierRegistrarNotNull_whenEnabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=true")
        .run(
            (context) -> {
              assertThat(context.getBean(ExceptionStringSupplierRegistrar.class))
                  .isNotNull()
                  .isExactlyInstanceOf(ExceptionStringSupplierRegistrar.class);
            });
  }

  @Test
  void exceptionStringSupplierRegistrarIsNull_whenDisabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              assertThrows(
                  NoSuchBeanDefinitionException.class,
                  () -> context.getBean(ExceptionStringSupplierRegistrar.class));
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
  void joinPointStringSupplierRegistrarNotNull_whenEnabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=true")
        .run(
            (context) -> {
              assertThat(context.getBean(JoinPointStringSupplierRegistrar.class))
                  .isNotNull()
                  .isExactlyInstanceOf(JoinPointStringSupplierRegistrar.class);
            });
  }

  @Test
  void joinPointStringSupplierRegistrarIsNull_whenDisabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              assertThrows(
                  NoSuchBeanDefinitionException.class,
                  () -> context.getBean(JoinPointStringSupplierRegistrar.class));
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

  @Test
  void returnValueStringSupplierRegistrarNotNull_whenEnabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=true")
        .run(
            (context) -> {
              assertThat(context.getBean(ReturnValueStringSupplierRegistrar.class))
                  .isNotNull()
                  .isExactlyInstanceOf(ReturnValueStringSupplierRegistrar.class);
            });
  }

  @Test
  void returnValueStringSupplierRegistrarIsNull_whenDisabled() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              assertThrows(
                  NoSuchBeanDefinitionException.class,
                  () -> context.getBean(ReturnValueStringSupplierRegistrar.class));
            });
  }
}
