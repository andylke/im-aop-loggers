package im.aop.loggers.advice.before;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.logging.Level;

/**
 * Tests for {@link LogBeforeService}.
 *
 * @author Andy Lian
 */
@ExtendWith(OutputCaptureExtension.class)
class LogBeforeServiceTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(AopLoggersPropertiesTestConfiguration.class)
          .withBean(LogBeforeService.class);

  @TestConfiguration(proxyBeanMethods = false)
  @EnableConfigurationProperties({AopLoggersProperties.class})
  static class AopLoggersPropertiesTestConfiguration {}

  static class Foo {

    void foo() {}
  }

  private MethodSignature methodSignature;

  private JoinPoint joinPoint;

  @BeforeEach
  void beforeEach() throws NoSuchMethodException {
    methodSignature = mockMethodSignature(Foo.class, "foo");
    joinPoint = mockJoinPoint(methodSignature);
  }

  @Test
  void logEnteringMessage_defaultLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".entering-level=DEBUG")
        .run(
            (context) -> {
              final LogBefore annotation = mockLogBefore(Level.DEFAULT, "foo");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

              final LogBeforeService service = context.getBean(LogBeforeService.class);
              service.logBefore(joinPoint, annotation);

              assertThat(capturedOutput).contains("DEBUG " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logEnteringMessage_customLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".entering-level=DEBUG")
        .run(
            (context) -> {
              final LogBefore annotation = mockLogBefore(Level.INFO, "foo");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

              final LogBeforeService service = context.getBean(LogBeforeService.class);
              service.logBefore(joinPoint, annotation);

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logEnteringMessage_defaultMessage(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".entering-message=foo")
        .run(
            (context) -> {
              final LogBefore annotation = mockLogBefore(Level.INFO, "");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogBeforeService service = context.getBean(LogBeforeService.class);
              service.logBefore(joinPoint, annotation);

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logEnteringMessage_customMessage(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          final LogBefore annotation = mockLogBefore(Level.INFO, "foo");
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogBeforeService service = context.getBean(LogBeforeService.class);
          service.logBefore(joinPoint, annotation);

          assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void doesNotLogEnteringMessage_whenDisabled(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              final LogBefore annotation = mockLogBefore(Level.INFO, "foo");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogBeforeService service = context.getBean(LogBeforeService.class);
              service.logBefore(joinPoint, annotation);

              assertThat(capturedOutput).doesNotContain("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void doesNotLogEnteringMessage_whenLoggerLevelDisabled(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          final LogBefore annotation = mockLogBefore(Level.DEBUG, "foo");
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogBeforeService service = context.getBean(LogBeforeService.class);
          service.logBefore(joinPoint, annotation);

          assertThat(capturedOutput).doesNotContain("INFO " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void doesNotLogElapsed_whenDisabled(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              final LogBefore annotation = mockLogBefore(Level.INFO, "foo");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(LogBeforeService.class.getName(), LogLevel.DEBUG);

              final LogBeforeService service = context.getBean(LogBeforeService.class);
              service.logBefore(joinPoint, annotation);

              assertThat(capturedOutput).doesNotContain("[logBefore] elapsed [");
            });
  }

  @Test
  void logElapsed_whenLoggerLevelDisabled(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          final LogBefore annotation = mockLogBefore(Level.DEBUG, "foo");
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(LogBeforeService.class.getName(), LogLevel.DEBUG);

          final LogBeforeService service = context.getBean(LogBeforeService.class);
          service.logBefore(joinPoint, annotation);

          assertThat(capturedOutput).contains("[logBefore] elapsed [");
        });
  }

  @Test
  void logElapsed_whenEnabled(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          final LogBefore annotation = mockLogBefore(Level.INFO, "foo");
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(LogBeforeService.class.getName(), LogLevel.DEBUG);

          final LogBeforeService service = context.getBean(LogBeforeService.class);
          service.logBefore(joinPoint, annotation);

          assertThat(capturedOutput).contains("[logBefore] elapsed [");
        });
  }

  private MethodSignature mockMethodSignature(
      final Class<?> declaringClass, final String methodName, Class<?>... methodParameterTypes)
      throws NoSuchMethodException {
    final MethodSignature methodSignature = mock(MethodSignature.class);

    when(methodSignature.getDeclaringType()).thenReturn(declaringClass);
    when(methodSignature.getMethod())
        .thenReturn(declaringClass.getDeclaredMethod(methodName, methodParameterTypes));

    return methodSignature;
  }

  private JoinPoint mockJoinPoint(final MethodSignature methodSignature) {
    final JoinPoint joinPoint = mock(JoinPoint.class);

    when(joinPoint.getSignature()).thenReturn(methodSignature);

    return joinPoint;
  }

  private LogBefore mockLogBefore(final Level level, final String message) {
    final LogBefore annotation = mock(LogBefore.class);

    when(annotation.level()).thenReturn(level);
    when(annotation.enteringMessage()).thenReturn(message);

    return annotation;
  }
}
