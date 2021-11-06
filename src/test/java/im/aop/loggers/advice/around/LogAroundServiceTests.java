package im.aop.loggers.advice.around;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.temporal.ChronoUnit;

import org.aspectj.lang.ProceedingJoinPoint;
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

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.logging.Level;

/**
 * Tests for {@link LogAroundService}.
 *
 * @author Andy Lian
 */
@ExtendWith(OutputCaptureExtension.class)
class LogAroundServiceTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(AopLoggersPropertiesTestConfiguration.class)
          .withBean(LogAroundService.class);

  @TestConfiguration(proxyBeanMethods = false)
  @EnableConfigurationProperties({AopLoggersProperties.class})
  static class AopLoggersPropertiesTestConfiguration {}

  static class Foo {

    void foo() {}
  }

  private MethodSignature methodSignature;

  private ProceedingJoinPoint joinPoint;

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
              final LogAround annotation = mockLogAroundForEntering(Level.DEFAULT, "foo");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.logAround(joinPoint, annotation);

              assertThat(capturedOutput).contains("DEBUG " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logEnteringMessage_customLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".entering-level=DEBUG")
        .run(
            (context) -> {
              final LogAround annotation = mockLogAroundForEntering(Level.INFO, "foo");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.logAround(joinPoint, annotation);

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logEnteringMessage_defaultMessage(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".entering-message=foo")
        .run(
            (context) -> {
              final LogAround annotation = mockLogAroundForEntering(Level.INFO, "");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.logAround(joinPoint, annotation);

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logEnteringMessage_customMessage(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          final LogAround annotation = mockLogAroundForEntering(Level.INFO, "foo");
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.logAround(joinPoint, annotation);

          assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void doesNotLogEnteringMessage_whenDisabled(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              final LogAround annotation = mockLogAroundForEntering(Level.INFO, "foo");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.logAround(joinPoint, annotation);

              assertThat(capturedOutput).doesNotContain("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void doesNotLogEnteringMessage_whenLoggerLevelDisabled(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          final LogAround annotation = mockLogAroundForEntering(Level.DEBUG, "foo");
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.logAround(joinPoint, annotation);

          assertThat(capturedOutput).doesNotContain("DEBUG " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void logEnteringMessage_whenEnabled_willReturnValue(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenReturn("foo");

          final LogAround annotation = mockLogAroundForEntering(Level.INFO, "");
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          assertThat(service.logAround(joinPoint, annotation)).isEqualTo("foo");
        });
  }

  @Test
  void logEnteringMessage_whenDisabled_willReturnValue(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenReturn("foo");

              final LogAround annotation = mockLogAroundForEntering(Level.INFO, "foo");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              assertThat(service.logAround(joinPoint, annotation)).isEqualTo("foo");
            });
  }

  private LogAround mockLogAroundForEntering(final Level level, final String enteringMessage) {
    final LogAround annotation = mock(LogAround.class);

    when(annotation.level()).thenReturn(level);
    when(annotation.enteringMessage()).thenReturn(enteringMessage);

    when(annotation.exitedAbnormallyLevel()).thenReturn(Level.TRACE);
    when(annotation.elapsedWarningLevel()).thenReturn(Level.TRACE);

    when(annotation.exitedMessage()).thenReturn("");
    when(annotation.exitedAbnormallyMessage()).thenReturn("");
    when(annotation.elapsedMessage()).thenReturn("");
    when(annotation.elapsedWarningMessage()).thenReturn("");

    when(annotation.ignoreExceptions()).thenReturn(null);

    when(annotation.elapsedTimeLimit()).thenReturn(0L);
    when(annotation.elapsedTimeUnit()).thenReturn(ChronoUnit.MILLIS);

    return annotation;
  }

  @Test
  void logExitedNormallyMessage_defaultLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".exited-level=DEBUG")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenReturn("foo");

              final LogAround annotation = mockLogAroundForExitedNormally(Level.DEFAULT, "foo");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.logAround(joinPoint, annotation);

              assertThat(capturedOutput).contains("DEBUG " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logExitedNormallyMessage_customLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".exited-level=DEBUG")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenReturn("foo");

              final LogAround annotation = mockLogAroundForExitedNormally(Level.INFO, "foo");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.logAround(joinPoint, annotation);

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logExitedNormallyMessage_defaultMessage(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".exited-message=foo")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenReturn("foo");

              final LogAround annotation = mockLogAroundForExitedNormally(Level.INFO, "");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.logAround(joinPoint, annotation);

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logExitedNormallyMessage_customMessage(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenReturn("foo");

          final LogAround annotation = mockLogAroundForExitedNormally(Level.INFO, "foo");
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.logAround(joinPoint, annotation);

          assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void doesNotLogExitedNormallyMessage_whenDisabled(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenReturn("foo");

              final LogAround annotation = mockLogAroundForExitedNormally(Level.INFO, "foo");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.logAround(joinPoint, annotation);

              assertThat(capturedOutput).doesNotContain("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void doesNotLogExitedNormallyMessage_whenLoggerLevelDisabled(
      final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenReturn("foo");

          final LogAround annotation = mockLogAroundForExitedNormally(Level.DEBUG, "foo");
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.logAround(joinPoint, annotation);

          assertThat(capturedOutput).doesNotContain("DEBUG " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void logExitedNormallyMessage_whenEnabled_willReturnValue(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenReturn("foo");

          final LogAround annotation = mockLogAroundForExitedNormally(Level.INFO, "foo");
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          assertThat(service.logAround(joinPoint, annotation)).isEqualTo("foo");
        });
  }

  @Test
  void logExitedNormallyMessage_whenDisabled_willReturnValue(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenReturn("foo");

              final LogAround annotation = mockLogAroundForExitedNormally(Level.INFO, "foo");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              assertThat(service.logAround(joinPoint, annotation)).isEqualTo("foo");
            });
  }

  private LogAround mockLogAroundForExitedNormally(final Level level, final String exitedMessage) {
    final LogAround annotation = mock(LogAround.class);

    when(annotation.level()).thenReturn(level);
    when(annotation.exitedMessage()).thenReturn(exitedMessage);

    when(annotation.exitedAbnormallyLevel()).thenReturn(Level.TRACE);
    when(annotation.elapsedWarningLevel()).thenReturn(Level.TRACE);

    when(annotation.enteringMessage()).thenReturn("");
    when(annotation.exitedAbnormallyMessage()).thenReturn("");
    when(annotation.elapsedMessage()).thenReturn("");
    when(annotation.elapsedWarningMessage()).thenReturn("");

    when(annotation.ignoreExceptions()).thenReturn(null);

    when(annotation.elapsedTimeLimit()).thenReturn(0L);
    when(annotation.elapsedTimeUnit()).thenReturn(ChronoUnit.MILLIS);

    return annotation;
  }

  @Test
  void logExitedAbnormallyMessage_defaultLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".exited-abnormally-level=DEBUG")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenThrow(new RuntimeException("foo"));

              final LogAround annotation =
                  mockLogAroundForExitAbnormally(Level.DEFAULT, "foo", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

              final LogAroundService service = context.getBean(LogAroundService.class);
              assertThrows(RuntimeException.class, () -> service.logAround(joinPoint, annotation));

              assertThat(capturedOutput).contains("DEBUG " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logExitedAbnormallyMessage_customLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".exited-abnormally-level=DEBUG")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenThrow(new RuntimeException());

              final LogAround annotation = mockLogAroundForExitAbnormally(Level.INFO, "foo", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              assertThrows(RuntimeException.class, () -> service.logAround(joinPoint, annotation));

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logExitedAbnormallyMessage_defaultMessage(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".exited-abnormally-message=foo")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenThrow(new RuntimeException());

              final LogAround annotation = mockLogAroundForExitAbnormally(Level.INFO, "", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              assertThrows(RuntimeException.class, () -> service.logAround(joinPoint, annotation));

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logExitedAbnormallyMessage_customMessage(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenThrow(new RuntimeException());

          final LogAround annotation = mockLogAroundForExitAbnormally(Level.INFO, "foo", null);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          assertThrows(RuntimeException.class, () -> service.logAround(joinPoint, annotation));

          assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void doesNotLogExitedAbnormallyMessage_whenDisabled(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenThrow(new RuntimeException("foo"));

              final LogAround annotation = mockLogAroundForExitAbnormally(Level.ERROR, "foo", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              assertThrows(RuntimeException.class, () -> service.logAround(joinPoint, annotation));

              assertThat(capturedOutput).doesNotContain("ERROR " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void doesNotLogExitedAbnormallyMessage_whenLoggerLevelDisabled(
      final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenThrow(new RuntimeException());

          final LogAround annotation = mockLogAroundForExitAbnormally(Level.DEBUG, "foo", null);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          assertThrows(RuntimeException.class, () -> service.logAround(joinPoint, annotation));

          assertThat(capturedOutput).doesNotContain("DEBUG " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void doesNotLogExitedAbnormallyMessage_whenMatchPropertyIgnoreExceptions(
      final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(
            AopLoggersProperties.PREFIX + ".ignore-exceptions=java.lang.RuntimeException")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenThrow(new RuntimeException());

              final LogAround annotation =
                  mockLogAroundForExitAbnormally(Level.DEFAULT, "foo", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              assertThrows(RuntimeException.class, () -> service.logAround(joinPoint, annotation));

              assertThat(capturedOutput).doesNotContain("ERROR " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void doesNotLogExitedAbnormallyMessage_whenMatchAttributeIgnoreExceptions(
      final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenThrow(new RuntimeException());

          final LogAround annotation =
              mockLogAroundForExitAbnormally(
                  Level.DEFAULT, "foo", Arrays.array(RuntimeException.class));
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          assertThrows(RuntimeException.class, () -> service.logAround(joinPoint, annotation));

          assertThat(capturedOutput).doesNotContain("ERROR " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void logExitedAbnormallyMessage_whenNotMatchIgnoreException(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenThrow(new RuntimeException());

          final LogAround annotation =
              mockLogAroundForExitAbnormally(
                  Level.ERROR, "foo", Arrays.array(ClassNotFoundException.class));
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          assertThrows(RuntimeException.class, () -> service.logAround(joinPoint, annotation));

          assertThat(capturedOutput).contains("ERROR " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void logExitedAbnormallyMessage_givenEmptyIgnoreExceptions(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenThrow(new RuntimeException());

          final LogAround annotation =
              mockLogAroundForExitAbnormally(Level.ERROR, "foo", Arrays.array());
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.ERROR);

          final LogAroundService service = context.getBean(LogAroundService.class);
          assertThrows(RuntimeException.class, () -> service.logAround(joinPoint, annotation));

          assertThat(capturedOutput).contains("ERROR " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void logExitedAbnormallyMessage_whenExceptionInIgnoreExceptionsIsNull(
      final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenThrow(new RuntimeException());

          final LogAround annotation =
              mockLogAroundForExitAbnormally(
                  Level.ERROR, "foo", Arrays.array((Class<Exception>) null));
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.ERROR);

          final LogAroundService service = context.getBean(LogAroundService.class);
          assertThrows(RuntimeException.class, () -> service.logAround(joinPoint, annotation));

          assertThat(capturedOutput).contains("ERROR " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void logExitedAbnormallyMessage_whenEnabled_willThrowException(
      final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenThrow(new RuntimeException("foo"));

          final LogAround annotation = mockLogAroundForExitAbnormally(Level.ERROR, "foo", null);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          assertThrows(RuntimeException.class, () -> service.logAround(joinPoint, annotation));
        });
  }

  @Test
  void logExitedAbnormallyMessage_whenDisabled_willThrowException(
      final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenThrow(new RuntimeException("foo"));

              final LogAround annotation = mockLogAroundForExitAbnormally(Level.ERROR, "foo", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              assertThrows(RuntimeException.class, () -> service.logAround(joinPoint, annotation));
            });
  }

  private LogAround mockLogAroundForExitAbnormally(
      final Level exitedAbnormallyLevel,
      final String exitAbnormallyMessage,
      final Class<? extends Throwable>[] ignoreExceptions) {
    final LogAround annotation = mock(LogAround.class);

    when(annotation.exitedAbnormallyLevel()).thenReturn(exitedAbnormallyLevel);
    when(annotation.exitedAbnormallyMessage()).thenReturn(exitAbnormallyMessage);

    when(annotation.level()).thenReturn(Level.TRACE);
    when(annotation.elapsedWarningLevel()).thenReturn(Level.TRACE);

    when(annotation.enteringMessage()).thenReturn("");
    when(annotation.exitedMessage()).thenReturn("");
    when(annotation.elapsedMessage()).thenReturn("");
    when(annotation.elapsedWarningMessage()).thenReturn("");

    when(annotation.ignoreExceptions()).thenReturn(ignoreExceptions);
    when(annotation.printStackTrace()).thenReturn(true);

    when(annotation.elapsedTimeLimit()).thenReturn(0L);
    when(annotation.elapsedTimeUnit()).thenReturn(ChronoUnit.MILLIS);

    return annotation;
  }

  @Test
  void logExceptionStackTrace_whenEnablePrintStackTrace(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenThrow(new RuntimeException("foo"));

          final LogAround annotation = mockLogAroundForExitAbnormally(Level.INFO, "foo", true);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          assertThrows(RuntimeException.class, () -> service.logAround(joinPoint, annotation));

          assertThat(capturedOutput)
              .containsSubsequence(
                  "INFO " + Foo.class.getName() + " - foo", "java.lang.RuntimeException: foo");
        });
  }

  @Test
  void doesNotLogExceptionStackTrace_whenDisablePrintStackTrace(
      final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenThrow(new RuntimeException());

          final LogAround annotation = mockLogAroundForExitAbnormally(Level.INFO, "foo", false);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          assertThrows(RuntimeException.class, () -> service.logAround(joinPoint, annotation));

          assertThat(capturedOutput)
              .contains("INFO " + Foo.class.getName() + " - foo")
              .doesNotContain("java.lang.RuntimeException: foo");
        });
  }

  private LogAround mockLogAroundForExitAbnormally(
      final Level exitedAbnormallyLevel,
      final String exitAbnormallyMessage,
      final boolean printStackTrace) {
    final LogAround annotation = mock(LogAround.class);

    when(annotation.exitedAbnormallyLevel()).thenReturn(exitedAbnormallyLevel);
    when(annotation.exitedAbnormallyMessage()).thenReturn(exitAbnormallyMessage);

    when(annotation.level()).thenReturn(Level.TRACE);
    when(annotation.elapsedWarningLevel()).thenReturn(Level.TRACE);

    when(annotation.enteringMessage()).thenReturn("");
    when(annotation.exitedMessage()).thenReturn("");
    when(annotation.elapsedMessage()).thenReturn("");
    when(annotation.elapsedWarningMessage()).thenReturn("");

    when(annotation.ignoreExceptions()).thenReturn(null);
    when(annotation.printStackTrace()).thenReturn(printStackTrace);

    when(annotation.elapsedTimeLimit()).thenReturn(0L);
    when(annotation.elapsedTimeUnit()).thenReturn(ChronoUnit.MILLIS);

    return annotation;
  }

  @Test
  void logElapsedMessage_defaultLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".elapsed-level=DEBUG")
        .run(
            (context) -> {
              final LogAround annotation = mockLogAroundForElapsed(Level.DEFAULT, "foo");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.logAround(joinPoint, annotation);

              assertThat(capturedOutput).contains("DEBUG " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logElapsedMessage_customLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".elapsed-level=DEBUG")
        .run(
            (context) -> {
              final LogAround annotation = mockLogAroundForElapsed(Level.INFO, "foo");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.logAround(joinPoint, annotation);

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logElapsedMessage_defaultMessage(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".elapsed-message=foo")
        .run(
            (context) -> {
              final LogAround annotation = mockLogAroundForElapsed(Level.INFO, "");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.logAround(joinPoint, annotation);

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logElapsedMessage_customMessage(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          final LogAround annotation = mockLogAroundForElapsed(Level.INFO, "foo");
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.logAround(joinPoint, annotation);

          assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void doesNotLogElapsedMessage_whenDisabled(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              final LogAround annotation = mockLogAroundForElapsed(Level.INFO, "foo");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.logAround(joinPoint, annotation);

              assertThat(capturedOutput).doesNotContain("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void doesNotLogElapsedMessage_whenLoggerLevelDisabled(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          final LogAround annotation = mockLogAroundForElapsed(Level.DEBUG, "foo");
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.logAround(joinPoint, annotation);

          assertThat(capturedOutput).doesNotContain("DEBUG " + Foo.class.getName() + " - foo");
        });
  }

  private LogAround mockLogAroundForElapsed(final Level level, final String elapsedMessage) {
    final LogAround annotation = mock(LogAround.class);

    when(annotation.level()).thenReturn(level);
    when(annotation.elapsedMessage()).thenReturn(elapsedMessage);

    when(annotation.exitedAbnormallyLevel()).thenReturn(Level.TRACE);
    when(annotation.elapsedWarningLevel()).thenReturn(Level.TRACE);

    when(annotation.enteringMessage()).thenReturn("");
    when(annotation.exitedMessage()).thenReturn("");
    when(annotation.exitedAbnormallyMessage()).thenReturn("");
    when(annotation.elapsedWarningMessage()).thenReturn("");

    when(annotation.ignoreExceptions()).thenReturn(null);

    when(annotation.elapsedTimeLimit()).thenReturn(0L);
    when(annotation.elapsedTimeUnit()).thenReturn(ChronoUnit.MILLIS);

    return annotation;
  }

  @Test
  void logElapsedMessage_afterExitedMessage(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          final LogAround annotation =
              mockLogAroundForExitedAndElapsed(Level.INFO, "exited", "elapsed");
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.logAround(joinPoint, annotation);

          assertThat(capturedOutput)
              .containsSubsequence(
                  "INFO " + Foo.class.getName() + " - exited",
                  "INFO " + Foo.class.getName() + " - elapsed");
        });
  }

  private LogAround mockLogAroundForExitedAndElapsed(
      final Level level, final String exitedMessage, final String elapsedMessage) {
    final LogAround annotation = mock(LogAround.class);

    when(annotation.level()).thenReturn(level);
    when(annotation.exitedMessage()).thenReturn(exitedMessage);
    when(annotation.elapsedMessage()).thenReturn(elapsedMessage);

    when(annotation.exitedAbnormallyLevel()).thenReturn(Level.TRACE);
    when(annotation.elapsedWarningLevel()).thenReturn(Level.TRACE);

    when(annotation.enteringMessage()).thenReturn("");
    when(annotation.exitedAbnormallyMessage()).thenReturn("");
    when(annotation.elapsedWarningMessage()).thenReturn("");

    when(annotation.ignoreExceptions()).thenReturn(null);

    when(annotation.elapsedTimeLimit()).thenReturn(0L);
    when(annotation.elapsedTimeUnit()).thenReturn(ChronoUnit.MILLIS);

    return annotation;
  }

  @Test
  void logElapsedMessage_afterExitedAbnormallyMessage(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenThrow(new RuntimeException("foo"));

          final LogAround annotation =
              mockLogAroundForExitedAbnormallyAndElapsed(
                  Level.INFO, Level.ERROR, "exited-abnormally", "elapsed");
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

          final LogAroundService service = context.getBean(LogAroundService.class);
          assertThrows(RuntimeException.class, () -> service.logAround(joinPoint, annotation));

          assertThat(capturedOutput)
              .containsSubsequence(
                  "ERROR " + Foo.class.getName() + " - exited-abnormally",
                  "INFO " + Foo.class.getName() + " - elapsed");
        });
  }

  private LogAround mockLogAroundForExitedAbnormallyAndElapsed(
      final Level level,
      final Level exitedAbnormallyLevel,
      final String exitedAbnormallyMessage,
      final String elapsedMessage) {
    final LogAround annotation = mock(LogAround.class);

    when(annotation.level()).thenReturn(level);
    when(annotation.exitedAbnormallyLevel()).thenReturn(exitedAbnormallyLevel);
    when(annotation.exitedAbnormallyMessage()).thenReturn(exitedAbnormallyMessage);
    when(annotation.elapsedMessage()).thenReturn(elapsedMessage);

    when(annotation.elapsedWarningLevel()).thenReturn(Level.TRACE);

    when(annotation.enteringMessage()).thenReturn("");
    when(annotation.exitedMessage()).thenReturn("");
    when(annotation.elapsedWarningMessage()).thenReturn("");

    when(annotation.ignoreExceptions()).thenReturn(null);

    when(annotation.elapsedTimeLimit()).thenReturn(0L);
    when(annotation.elapsedTimeUnit()).thenReturn(ChronoUnit.MILLIS);

    return annotation;
  }

  @Test
  void logElapsedWarningMessage_defaultLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".elapsed-warning-level=DEBUG")
        .run(
            (context) -> {
              final LogAround annotation =
                  mockLogAroundForElapsedWarning(Level.DEFAULT, "foo", 1, ChronoUnit.NANOS);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.logAround(joinPoint, annotation);

              assertThat(capturedOutput).contains("DEBUG " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logElapsedWarningMessage_customLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".elapsed-warning-level=DEBUG")
        .run(
            (context) -> {
              final LogAround annotation =
                  mockLogAroundForElapsedWarning(Level.INFO, "foo", 1, ChronoUnit.NANOS);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.logAround(joinPoint, annotation);

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logElapsedWarningMessage_defaultMessage(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".elapsed-warning-message=foo")
        .run(
            (context) -> {
              final LogAround annotation =
                  mockLogAroundForElapsedWarning(Level.INFO, "", 1, ChronoUnit.NANOS);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.logAround(joinPoint, annotation);

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logElapsedWarningMessage_customMessage(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          final LogAround annotation =
              mockLogAroundForElapsedWarning(Level.INFO, "foo", 1, ChronoUnit.NANOS);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.logAround(joinPoint, annotation);

          assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void doesNotLogElapsedWarningMessage_whenElapsedTimeLimitEqualsZero(
      final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          final LogAround annotation =
              mockLogAroundForElapsedWarning(Level.INFO, "foo", 0, ChronoUnit.NANOS);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.logAround(joinPoint, annotation);

          assertThat(capturedOutput).doesNotContain("INFO " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void doesNotLogElapsedWarningMessage_whenElapsedTimeNotExceedLimit(
      final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          final LogAround annotation =
              mockLogAroundForElapsedWarning(Level.INFO, "foo", 1, ChronoUnit.MINUTES);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.logAround(joinPoint, annotation);

          assertThat(capturedOutput).doesNotContain("INFO " + Foo.class.getName() + " - foo");
        });
  }

  private LogAround mockLogAroundForElapsedWarning(
      final Level elapsedWarningLevel,
      final String elapsedWarningMessage,
      final long elapsedTimeLimit,
      final ChronoUnit elapsedTimeUnit) {
    final LogAround annotation = mock(LogAround.class);

    when(annotation.elapsedWarningLevel()).thenReturn(elapsedWarningLevel);
    when(annotation.elapsedWarningMessage()).thenReturn(elapsedWarningMessage);
    when(annotation.elapsedTimeLimit()).thenReturn(elapsedTimeLimit);
    when(annotation.elapsedTimeUnit()).thenReturn(elapsedTimeUnit);

    when(annotation.level()).thenReturn(Level.TRACE);
    when(annotation.exitedAbnormallyLevel()).thenReturn(Level.TRACE);

    when(annotation.enteringMessage()).thenReturn("");
    when(annotation.exitedMessage()).thenReturn("");
    when(annotation.exitedAbnormallyMessage()).thenReturn("");
    when(annotation.elapsedMessage()).thenReturn("");

    when(annotation.ignoreExceptions()).thenReturn(null);

    return annotation;
  }

  @Test
  void logElapsedWarningMessage_afterElapsedMessage(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          final LogAround annotation =
              mockLogAroundForElapsedAndElapsedWarning(
                  Level.INFO, "elapsed", Level.WARN, "elapsed-warning", 1, ChronoUnit.NANOS);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.logAround(joinPoint, annotation);

          assertThat(capturedOutput)
              .containsSubsequence(
                  "INFO " + Foo.class.getName() + " - elapsed",
                  "WARN " + Foo.class.getName() + " - elapsed-warning");
        });
  }

  private LogAround mockLogAroundForElapsedAndElapsedWarning(
      final Level level,
      final String elapsedMessage,
      final Level elapsedWarningLevel,
      final String elapsedWarningMessage,
      final long elapsedTimeLimit,
      final ChronoUnit elapsedTimeUnit) {
    final LogAround annotation = mock(LogAround.class);

    when(annotation.level()).thenReturn(level);
    when(annotation.elapsedMessage()).thenReturn(elapsedMessage);

    when(annotation.elapsedWarningLevel()).thenReturn(elapsedWarningLevel);
    when(annotation.elapsedWarningMessage()).thenReturn(elapsedWarningMessage);
    when(annotation.elapsedTimeLimit()).thenReturn(elapsedTimeLimit);
    when(annotation.elapsedTimeUnit()).thenReturn(elapsedTimeUnit);

    when(annotation.exitedAbnormallyLevel()).thenReturn(Level.TRACE);

    when(annotation.enteringMessage()).thenReturn("");
    when(annotation.exitedMessage()).thenReturn("");
    when(annotation.exitedAbnormallyMessage()).thenReturn("");

    when(annotation.ignoreExceptions()).thenReturn(null);

    return annotation;
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

  private ProceedingJoinPoint mockJoinPoint(final MethodSignature methodSignature) {
    final ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);

    when(joinPoint.getSignature()).thenReturn(methodSignature);

    return joinPoint;
  }

  @Test
  void doesNotLogElapsed_whenDisabled(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              final LogAround annotation = mockLogAroundForEntering(Level.INFO, "foo");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(LogAroundService.class.getName(), LogLevel.DEBUG);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.logAround(joinPoint, annotation);

              assertThat(capturedOutput).doesNotContain("[logAround] elapsed [");
            });
  }

  @Test
  void logElapsed_whenExitedNormally(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          final LogAround annotation = mockLogAroundForExitedNormally(Level.INFO, "foo");
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(LogAroundService.class.getName(), LogLevel.DEBUG);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.logAround(joinPoint, annotation);

          assertThat(capturedOutput).contains("[logAround] elapsed [");
        });
  }

  @Test
  void logElapsed_whenExitedAbnormally(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenThrow(new RuntimeException());

          final LogAround annotation = mockLogAroundForExitAbnormally(Level.ERROR, "foo", null);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.ERROR);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(LogAroundService.class.getName(), LogLevel.DEBUG);

          final LogAroundService service = context.getBean(LogAroundService.class);
          assertThrows(RuntimeException.class, () -> service.logAround(joinPoint, annotation));

          assertThat(capturedOutput).contains("[logAround] elapsed [");
        });
  }
}
