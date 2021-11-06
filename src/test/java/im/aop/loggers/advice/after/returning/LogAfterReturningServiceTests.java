package im.aop.loggers.advice.after.returning;

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
 * Tests for {@link LogAfterReturningService}.
 *
 * @author Andy Lian
 */
@ExtendWith(OutputCaptureExtension.class)
class LogAfterReturningServiceTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(AopLoggersPropertiesTestConfiguration.class)
          .withBean(LogAfterReturningService.class);

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
  void logExitedMessage_defaultMessage(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".exited-message=foo")
        .run(
            (context) -> {
              final LogAfterReturning annotation = mockLogAfterReturning(Level.INFO, "");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAfterReturningService service =
                  context.getBean(LogAfterReturningService.class);
              service.logAfterReturning(joinPoint, annotation, "foo");

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logExitedMessage_customMessage(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          final LogAfterReturning annotation = mockLogAfterReturning(Level.INFO, "foo");
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAfterReturningService service = context.getBean(LogAfterReturningService.class);
          service.logAfterReturning(joinPoint, annotation, "foo");

          assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void logExitedMessage_defaultLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".exited-level=DEBUG")
        .run(
            (context) -> {
              final LogAfterReturning annotation = mockLogAfterReturning(Level.DEFAULT, "foo");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

              final LogAfterReturningService service =
                  context.getBean(LogAfterReturningService.class);
              service.logAfterReturning(joinPoint, annotation, "foo");

              assertThat(capturedOutput).contains("DEBUG " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logExitedMessage_customLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".exited-level=DEBUG")
        .run(
            (context) -> {
              final LogAfterReturning annotation = mockLogAfterReturning(Level.INFO, "foo");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

              final LogAfterReturningService service =
                  context.getBean(LogAfterReturningService.class);
              service.logAfterReturning(joinPoint, annotation, "foo");

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void doesNotLogExitedMessage_whenDisabled(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              final LogAfterReturning annotation = mockLogAfterReturning(Level.INFO, "foo");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAfterReturningService service =
                  context.getBean(LogAfterReturningService.class);
              service.logAfterReturning(joinPoint, annotation, "foo");

              assertThat(capturedOutput).doesNotContain("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void doesNotLogExitedMessage_whenLoggerLevelDisabled(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          final LogAfterReturning annotation = mockLogAfterReturning(Level.DEBUG, "foo");
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAfterReturningService service = context.getBean(LogAfterReturningService.class);
          service.logAfterReturning(joinPoint, annotation, "foo");

          assertThat(capturedOutput).doesNotContain("INFO " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void doesNotLogElapsed_whenDisabled(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              final LogAfterReturning annotation = mockLogAfterReturning(Level.INFO, "foo");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(LogAfterReturningService.class.getName(), LogLevel.DEBUG);

              final LogAfterReturningService service =
                  context.getBean(LogAfterReturningService.class);
              service.logAfterReturning(joinPoint, annotation, "foo");

              assertThat(capturedOutput).doesNotContain("[logAfterReturning] elapsed [");
            });
  }

  @Test
  void logElapsed_whenLoggerLevelDisabled(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          final LogAfterReturning annotation = mockLogAfterReturning(Level.DEBUG, "foo");
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(LogAfterReturningService.class.getName(), LogLevel.DEBUG);

          final LogAfterReturningService service = context.getBean(LogAfterReturningService.class);
          service.logAfterReturning(joinPoint, annotation, "foo");

          assertThat(capturedOutput).contains("[logAfterReturning] elapsed [");
        });
  }

  @Test
  void logElapsed_whenEnabled(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          final LogAfterReturning annotation = mockLogAfterReturning(Level.INFO, "foo");
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(LogAfterReturningService.class.getName(), LogLevel.DEBUG);

          final LogAfterReturningService service = context.getBean(LogAfterReturningService.class);
          service.logAfterReturning(joinPoint, annotation, "foo");

          assertThat(capturedOutput).contains("[logAfterReturning] elapsed [");
        });
  }

  private MethodSignature mockMethodSignature(
      final Class<?> declaringClass, final String methodName, Class<?>... methodParameterTypes)
      throws NoSuchMethodException {
    final MethodSignature methodSignature = mock(MethodSignature.class);

    when(methodSignature.getDeclaringType()).thenReturn(declaringClass);
    when(methodSignature.getMethod())
        .thenReturn(declaringClass.getDeclaredMethod(methodName, methodParameterTypes));
    when(methodSignature.getReturnType()).thenReturn(String.class);

    return methodSignature;
  }

  private JoinPoint mockJoinPoint(final MethodSignature methodSignature) {
    final JoinPoint joinPoint = mock(JoinPoint.class);

    when(joinPoint.getSignature()).thenReturn(methodSignature);

    return joinPoint;
  }

  private LogAfterReturning mockLogAfterReturning(final Level level, final String message) {
    final LogAfterReturning annotation = mock(LogAfterReturning.class);

    when(annotation.level()).thenReturn(level);
    when(annotation.exitedMessage()).thenReturn(message);

    return annotation;
  }
}
