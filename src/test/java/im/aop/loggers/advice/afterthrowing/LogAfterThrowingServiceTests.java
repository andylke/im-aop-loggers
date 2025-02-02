package im.aop.loggers.advice.afterthrowing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.Level;
import im.aop.loggers.advice.afterreturning.LogAfterReturningService;
import im.aop.loggers.messageinterpolation.StringSubstitutorConfiguration;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.assertj.core.util.Arrays;
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

/**
 * Tests for {@link LogAfterReturningService}.
 *
 * @author Andy Lian
 */
@ExtendWith(OutputCaptureExtension.class)
class LogAfterThrowingServiceTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(
              AopLoggersPropertiesTestConfiguration.class, StringSubstitutorConfiguration.class)
          .withBean(LogAfterThrowingService.class);

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
  void logMessage_defaultLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".exited-abnormally-level=WARN")
        .run(
            context -> {
              final LogAfterThrowing annotation = mockLogAfterThrowing(Level.DEFAULT, "foo", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAfterThrowingService service =
                  context.getBean(LogAfterThrowingService.class);
              service.logAfterThrowing(joinPoint, annotation, new RuntimeException("foo"));

              assertThat(capturedOutput).contains("WARN " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logMessage_customLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".exited-abnormally-level=WARN")
        .run(
            context -> {
              final LogAfterThrowing annotation = mockLogAfterThrowing(Level.ERROR, "foo", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAfterThrowingService service =
                  context.getBean(LogAfterThrowingService.class);
              service.logAfterThrowing(joinPoint, annotation, new RuntimeException("foo"));

              assertThat(capturedOutput).contains("ERROR " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logMessage_defaultMessage(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".exited-abnormally-message=foo")
        .run(
            context -> {
              final LogAfterThrowing annotation = mockLogAfterThrowing(Level.INFO, "", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAfterThrowingService service =
                  context.getBean(LogAfterThrowingService.class);
              service.logAfterThrowing(joinPoint, annotation, new RuntimeException());

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logMessage_customMessage(final CapturedOutput capturedOutput) {
    runner.run(
        context -> {
          final LogAfterThrowing annotation = mockLogAfterThrowing(Level.INFO, "foo", null);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAfterThrowingService service = context.getBean(LogAfterThrowingService.class);
          service.logAfterThrowing(joinPoint, annotation, new RuntimeException());

          assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void doesNotLogMessage_whenLoggerLevelDisabled(final CapturedOutput capturedOutput) {
    runner.run(
        context -> {
          final LogAfterThrowing annotation = mockLogAfterThrowing(Level.DEBUG, "foo", null);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAfterThrowingService service = context.getBean(LogAfterThrowingService.class);
          service.logAfterThrowing(joinPoint, annotation, new RuntimeException("foo"));

          assertThat(capturedOutput).doesNotContain("ERROR " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void doesNotLogMessage_whenMatchPropertyIgnoreExceptions(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(
            AopLoggersProperties.PREFIX + ".ignore-exceptions=java.lang.RuntimeException")
        .run(
            context -> {
              final LogAfterThrowing annotation = mockLogAfterThrowing(Level.ERROR, "foo", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.ERROR);

              final LogAfterThrowingService service =
                  context.getBean(LogAfterThrowingService.class);
              service.logAfterThrowing(joinPoint, annotation, new RuntimeException());

              assertThat(capturedOutput).doesNotContain("ERROR " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void doesNotLogMessage_whenMatchAttributeIgnoreExceptions(final CapturedOutput capturedOutput) {
    runner.run(
        context -> {
          final LogAfterThrowing annotation =
              mockLogAfterThrowing(Level.ERROR, "foo", Arrays.array(RuntimeException.class));
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.ERROR);

          final LogAfterThrowingService service = context.getBean(LogAfterThrowingService.class);
          service.logAfterThrowing(joinPoint, annotation, new RuntimeException());

          assertThat(capturedOutput).doesNotContain("ERROR " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void logMessage_whenNotMatchIgnoreException(final CapturedOutput capturedOutput) {
    runner.run(
        context -> {
          final LogAfterThrowing annotation =
              mockLogAfterThrowing(Level.ERROR, "foo", Arrays.array(ClassNotFoundException.class));
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.ERROR);

          final LogAfterThrowingService service = context.getBean(LogAfterThrowingService.class);
          service.logAfterThrowing(joinPoint, annotation, new RuntimeException());

          assertThat(capturedOutput).contains("ERROR " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void doesNotLogMessage_givenNullException(final CapturedOutput capturedOutput) {
    runner.run(
        context -> {
          final LogAfterThrowing annotation = mockLogAfterThrowing(Level.ERROR, "foo", null);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.ERROR);

          final LogAfterThrowingService service = context.getBean(LogAfterThrowingService.class);
          service.logAfterThrowing(joinPoint, annotation, null);

          assertThat(capturedOutput).doesNotContain("ERROR " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void logMessage_givenEmptyIgnoreExceptions(final CapturedOutput capturedOutput) {
    runner.run(
        context -> {
          final LogAfterThrowing annotation =
              mockLogAfterThrowing(Level.ERROR, "foo", Arrays.array());
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.ERROR);

          final LogAfterThrowingService service = context.getBean(LogAfterThrowingService.class);
          service.logAfterThrowing(joinPoint, annotation, new RuntimeException());

          assertThat(capturedOutput).contains("ERROR " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void logMessage_whenExceptionInIgnoreExceptionsIsNull(final CapturedOutput capturedOutput) {
    runner.run(
        context -> {
          final LogAfterThrowing annotation =
              mockLogAfterThrowing(Level.ERROR, "foo", Arrays.array((Class<Exception>) null));
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.ERROR);

          final LogAfterThrowingService service = context.getBean(LogAfterThrowingService.class);
          service.logAfterThrowing(joinPoint, annotation, new RuntimeException());

          assertThat(capturedOutput).contains("ERROR " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void logElapsed_whenLoggerLevelDisabled(final CapturedOutput capturedOutput) {
    runner.run(
        context -> {
          final LogAfterThrowing annotation = mockLogAfterThrowing(Level.ERROR, "foo", null);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.ERROR);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(LogAfterThrowingService.class.getName(), LogLevel.DEBUG);

          final LogAfterThrowingService service = context.getBean(LogAfterThrowingService.class);
          service.logAfterThrowing(joinPoint, annotation, new RuntimeException("foo"));

          assertThat(capturedOutput).contains("[logAfterThrowing] elapsed [");
        });
  }

  @Test
  void logElapsed_whenMatchPropertyIgnoreExceptions(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(
            AopLoggersProperties.PREFIX + ".ignore-exceptions=java.lang.RuntimeException")
        .run(
            context -> {
              final LogAfterThrowing annotation = mockLogAfterThrowing(Level.ERROR, "foo", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.ERROR);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(LogAfterThrowingService.class.getName(), LogLevel.DEBUG);

              final LogAfterThrowingService service =
                  context.getBean(LogAfterThrowingService.class);
              service.logAfterThrowing(joinPoint, annotation, new RuntimeException());

              assertThat(capturedOutput).contains("[logAfterThrowing] elapsed [");
            });
  }

  @Test
  void logElapsed_whenMatchAttributeIgnoreExceptions(final CapturedOutput capturedOutput) {
    runner.run(
        context -> {
          final LogAfterThrowing annotation =
              mockLogAfterThrowing(Level.ERROR, "foo", Arrays.array(RuntimeException.class));
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.ERROR);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(LogAfterThrowingService.class.getName(), LogLevel.DEBUG);

          final LogAfterThrowingService service = context.getBean(LogAfterThrowingService.class);
          service.logAfterThrowing(joinPoint, annotation, new RuntimeException());

          assertThat(capturedOutput).contains("[logAfterThrowing] elapsed [");
        });
  }

  @Test
  void logElapsed_whenEnabled(final CapturedOutput capturedOutput) {
    runner.run(
        context -> {
          final LogAfterThrowing annotation = mockLogAfterThrowing(Level.ERROR, "foo", null);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.ERROR);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(LogAfterThrowingService.class.getName(), LogLevel.DEBUG);

          final LogAfterThrowingService service = context.getBean(LogAfterThrowingService.class);
          service.logAfterThrowing(joinPoint, annotation, new RuntimeException("foo"));

          assertThat(capturedOutput).contains("[logAfterThrowing] elapsed [");
        });
  }

  private LogAfterThrowing mockLogAfterThrowing(
      final Level level,
      final String message,
      final Class<? extends Throwable>[] ignoreExceptions) {
    final LogAfterThrowing annotation = mock(LogAfterThrowing.class);

    when(annotation.level()).thenReturn(level);
    when(annotation.exitedAbnormallyMessage()).thenReturn(message);
    when(annotation.ignoreExceptions()).thenReturn(ignoreExceptions);
    when(annotation.printStackTrace()).thenReturn(true);

    return annotation;
  }

  @Test
  void logExceptionStackTrace_whenEnablePrintStackTrace(final CapturedOutput capturedOutput) {
    runner.run(
        context -> {
          final LogAfterThrowing annotation = mockLogAfterThrowing(Level.INFO, "foo", true);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAfterThrowingService service = context.getBean(LogAfterThrowingService.class);
          service.logAfterThrowing(joinPoint, annotation, new RuntimeException("foo"));

          assertThat(capturedOutput)
              .containsSubsequence(
                  "INFO " + Foo.class.getName() + " - foo", "java.lang.RuntimeException: foo");
        });
  }

  @Test
  void doesNotLogExceptionStackTrace_whenDisablePrintStackTrace(
      final CapturedOutput capturedOutput) {
    runner.run(
        context -> {
          final LogAfterThrowing annotation = mockLogAfterThrowing(Level.INFO, "foo", false);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAfterThrowingService service = context.getBean(LogAfterThrowingService.class);
          service.logAfterThrowing(joinPoint, annotation, new RuntimeException("foo"));

          assertThat(capturedOutput)
              .contains("INFO " + Foo.class.getName() + " - foo")
              .doesNotContain("java.lang.RuntimeException: foo");
        });
  }

  private LogAfterThrowing mockLogAfterThrowing(
      final Level level, final String message, final boolean printStackTrace) {
    final LogAfterThrowing annotation = mock(LogAfterThrowing.class);

    when(annotation.level()).thenReturn(level);
    when(annotation.exitedAbnormallyMessage()).thenReturn(message);
    when(annotation.ignoreExceptions()).thenReturn(null);
    when(annotation.printStackTrace()).thenReturn(printStackTrace);

    return annotation;
  }

  private MethodSignature mockMethodSignature(
      final Class<?> declaringClass, final String methodName, Class<?>... methodParameterTypes)
      throws NoSuchMethodException {
    final MethodSignature mock = mock(MethodSignature.class);

    when(mock.getDeclaringType()).thenReturn(declaringClass);
    when(mock.getMethod())
        .thenReturn(declaringClass.getDeclaredMethod(methodName, methodParameterTypes));
    when(mock.getReturnType()).thenReturn(String.class);

    return mock;
  }

  private JoinPoint mockJoinPoint(final MethodSignature methodSignature) {
    final JoinPoint mock = mock(JoinPoint.class);

    when(mock.getSignature()).thenReturn(methodSignature);

    return mock;
  }
}
