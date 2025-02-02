package im.aop.loggers.advice.beforecommit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.Level;
import im.aop.loggers.messageinterpolation.StringSubstitutorConfiguration;
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

/**
 * Tests for {@link LogBeforeCommitService}.
 *
 * @author Andy Lian
 */
@ExtendWith(OutputCaptureExtension.class)
class LogBeforeCommitServiceTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(
              AopLoggersPropertiesTestConfiguration.class, StringSubstitutorConfiguration.class)
          .withBean(LogBeforeCommitService.class);

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
  void logEnteringMessage_defaultLoggingLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".entering-level=DEBUG")
        .run(
            context -> {
              final LogBeforeCommit annotation = mockLogBeforeCommit(Level.DEFAULT, "foo");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

              final LogBeforeCommitService service = context.getBean(LogBeforeCommitService.class);
              service.logBeforeCommit(joinPoint, annotation);

              assertThat(capturedOutput).contains("DEBUG " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logEnteringMessage_customLoggingLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".entering-level=DEBUG")
        .run(
            context -> {
              final LogBeforeCommit annotation = mockLogBeforeCommit(Level.INFO, "foo");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

              final LogBeforeCommitService service = context.getBean(LogBeforeCommitService.class);
              service.logBeforeCommit(joinPoint, annotation);

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logEnteringMessage_defaultMessage(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".committing-transaction-message=foo")
        .run(
            context -> {
              final LogBeforeCommit annotation = mockLogBeforeCommit(Level.INFO, "");
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogBeforeCommitService service = context.getBean(LogBeforeCommitService.class);
              service.logBeforeCommit(joinPoint, annotation);

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logEnteringMessage_customMessage(final CapturedOutput capturedOutput) {
    runner.run(
        context -> {
          final LogBeforeCommit annotation = mockLogBeforeCommit(Level.INFO, "foo");
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogBeforeCommitService service = context.getBean(LogBeforeCommitService.class);
          service.logBeforeCommit(joinPoint, annotation);

          assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void doesNotLogEnteringMessage_whenLoggerLoggingLevelDisabled(
      final CapturedOutput capturedOutput) {
    runner.run(
        context -> {
          final LogBeforeCommit annotation = mockLogBeforeCommit(Level.DEBUG, "foo");
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogBeforeCommitService service = context.getBean(LogBeforeCommitService.class);
          service.logBeforeCommit(joinPoint, annotation);

          assertThat(capturedOutput).doesNotContain("INFO " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void logElapsed_whenLoggerLoggingLevelDisabled(final CapturedOutput capturedOutput) {
    runner.run(
        context -> {
          final LogBeforeCommit annotation = mockLogBeforeCommit(Level.DEBUG, "foo");
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(LogBeforeCommitService.class.getName(), LogLevel.DEBUG);

          final LogBeforeCommitService service = context.getBean(LogBeforeCommitService.class);
          service.logBeforeCommit(joinPoint, annotation);

          assertThat(capturedOutput).contains("[logBeforeCommit] elapsed [");
        });
  }

  @Test
  void logElapsed_whenEnabled(final CapturedOutput capturedOutput) {
    runner.run(
        context -> {
          final LogBeforeCommit annotation = mockLogBeforeCommit(Level.INFO, "foo");
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(LogBeforeCommitService.class.getName(), LogLevel.DEBUG);

          final LogBeforeCommitService service = context.getBean(LogBeforeCommitService.class);
          service.logBeforeCommit(joinPoint, annotation);

          assertThat(capturedOutput).contains("[logBeforeCommit] elapsed [");
        });
  }

  private MethodSignature mockMethodSignature(
      final Class<?> declaringClass, final String methodName, Class<?>... methodParameterTypes)
      throws NoSuchMethodException {
    final MethodSignature mock = mock(MethodSignature.class);

    when(mock.getDeclaringType()).thenReturn(declaringClass);
    when(mock.getMethod())
        .thenReturn(declaringClass.getDeclaredMethod(methodName, methodParameterTypes));

    return mock;
  }

  private JoinPoint mockJoinPoint(final MethodSignature methodSignature) {
    final JoinPoint mock = mock(JoinPoint.class);

    when(mock.getSignature()).thenReturn(methodSignature);

    return mock;
  }

  private LogBeforeCommit mockLogBeforeCommit(final Level level, final String message) {
    final LogBeforeCommit annotation = mock(LogBeforeCommit.class);

    when(annotation.loggingLevel()).thenReturn(level);
    when(annotation.messageTemplate()).thenReturn(message);

    return annotation;
  }
}
