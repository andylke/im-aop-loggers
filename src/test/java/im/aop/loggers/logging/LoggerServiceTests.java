package im.aop.loggers.logging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

/**
 * Tests for {@link LoggerService}.
 *
 * @author Andy Lian
 */
class LoggerServiceTests {

  private static final LoggerService LOGGER_SERVICE = new LoggerService();

  static class TestFoo {
    void foo() {}
  }

  static class TestBar {
    void bar() {}
  }

  @Test
  void getLogger_givenDeclaringClass() throws NoSuchMethodException {
    final MethodSignature methodSignature = mockMethodSignature(TestFoo.class, "foo");
    final JoinPoint joinPoint = mockJoinPoint(methodSignature);

    final Logger logger = LOGGER_SERVICE.getLogger(TestBar.class, joinPoint);
    assertThat(logger.getName()).isEqualTo(TestBar.class.getName());
  }

  @Test
  void getLogger_givenDeclaringClassEqualsVoid() throws NoSuchMethodException {
    final MethodSignature methodSignature = mockMethodSignature(TestFoo.class, "foo");
    final JoinPoint joinPoint = mockJoinPoint(methodSignature);

    final Logger logger = LOGGER_SERVICE.getLogger(void.class, joinPoint);
    assertThat(logger.getName()).isEqualTo(TestFoo.class.getName());
  }

  @Test
  void getLogger_givenDeclaringClassEqualsNull() throws NoSuchMethodException {
    final MethodSignature methodSignature = mockMethodSignature(TestFoo.class, "foo");
    final JoinPoint joinPoint = mockJoinPoint(methodSignature);

    final Logger logger = LOGGER_SERVICE.getLogger(null, joinPoint);
    assertThat(logger.getName()).isEqualTo(TestFoo.class.getName());
  }

  @Test
  void isEnabled_givenLevelEqualsTrace() {
    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel("foo", LogLevel.TRACE);

    final Logger logger = LoggerFactory.getLogger("foo");
    assertThat(LOGGER_SERVICE.isEnabled(logger, Level.TRACE)).isTrue();
  }

  @Test
  void isEnabled_givenLevelEqualsDebug() {
    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel("foo", LogLevel.DEBUG);

    final Logger logger = LoggerFactory.getLogger("foo");
    assertThat(LOGGER_SERVICE.isEnabled(logger, Level.DEBUG)).isTrue();
  }

  @Test
  void isEnabled_givenLevelEqualsInfo() {
    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel("foo", LogLevel.INFO);

    final Logger logger = LoggerFactory.getLogger("foo");
    assertThat(LOGGER_SERVICE.isEnabled(logger, Level.INFO)).isTrue();
  }

  @Test
  void isEnabled_givenLevelEqualsWarn() {
    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel("foo", LogLevel.WARN);

    final Logger logger = LoggerFactory.getLogger("foo");
    assertThat(LOGGER_SERVICE.isEnabled(logger, Level.WARN)).isTrue();
  }

  @Test
  void isEnabled_givenLevelEqualsError() {
    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel("foo", LogLevel.ERROR);

    final Logger logger = LoggerFactory.getLogger("foo");
    assertThat(LOGGER_SERVICE.isEnabled(logger, Level.ERROR)).isTrue();
  }

  @Test
  void isEnabled_givenLevelEqualsDefault() {
    final Logger logger = LoggerFactory.getLogger("foo");
    assertThat(LOGGER_SERVICE.isEnabled(logger, Level.DEFAULT)).isFalse();
  }

  @Test
  @ExtendWith(OutputCaptureExtension.class)
  void log_givenLevelEqualsTrace(final CapturedOutput capturedOutput) {
    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel("foo", LogLevel.TRACE);

    final Logger logger = LoggerFactory.getLogger("foo");
    LOGGER_SERVICE.log(logger, Level.TRACE, "foo");
    assertThat(capturedOutput).containsOnlyOnce("TRACE foo - foo");
  }

  @Test
  @ExtendWith(OutputCaptureExtension.class)
  void log_givenLevelEqualsDebug(final CapturedOutput capturedOutput) {
    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel("foo", LogLevel.DEBUG);

    final Logger logger = LoggerFactory.getLogger("foo");
    LOGGER_SERVICE.log(logger, Level.DEBUG, "foo");
    assertThat(capturedOutput).containsOnlyOnce("DEBUG foo - foo");
  }

  @Test
  @ExtendWith(OutputCaptureExtension.class)
  void log_givenLevelEqualsInfo(final CapturedOutput capturedOutput) {
    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel("foo", LogLevel.INFO);

    final Logger logger = LoggerFactory.getLogger("foo");
    LOGGER_SERVICE.log(logger, Level.INFO, "foo");
    assertThat(capturedOutput).containsOnlyOnce("INFO foo - foo");
  }

  @Test
  @ExtendWith(OutputCaptureExtension.class)
  void log_givenLevelEqualsWarn(final CapturedOutput capturedOutput) {
    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel("foo", LogLevel.WARN);

    final Logger logger = LoggerFactory.getLogger("foo");
    LOGGER_SERVICE.log(logger, Level.WARN, "foo");
    assertThat(capturedOutput).containsOnlyOnce("WARN foo - foo");
  }

  @Test
  @ExtendWith(OutputCaptureExtension.class)
  void log_givenLevelEqualsError(final CapturedOutput capturedOutput) {
    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel("foo", LogLevel.ERROR);

    final Logger logger = LoggerFactory.getLogger("foo");
    LOGGER_SERVICE.log(logger, Level.ERROR, "foo");
    assertThat(capturedOutput).containsOnlyOnce("ERROR foo - foo");
  }

  @Test
  @ExtendWith(OutputCaptureExtension.class)
  void log_givenLevelEqualsDefault(final CapturedOutput capturedOutput) {
    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel("foo", LogLevel.DEBUG);

    final Logger logger = LoggerFactory.getLogger("foo");
    LOGGER_SERVICE.log(logger, Level.DEFAULT, "foo");
    assertThat(capturedOutput).doesNotContain("foo - foo");
  }

  @Test
  @ExtendWith(OutputCaptureExtension.class)
  void logException_givenLevelEqualsTrace(final CapturedOutput capturedOutput) {
    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel("foo", LogLevel.TRACE);

    final Logger logger = LoggerFactory.getLogger("foo");
    LOGGER_SERVICE.logException(logger, Level.TRACE, "foo", new RuntimeException("foo"));
    assertThat(capturedOutput)
        .containsOnlyOnce("TRACE foo - foo")
        .containsSubsequence("TRACE foo - foo", "java.lang.RuntimeException: foo");
  }

  @Test
  @ExtendWith(OutputCaptureExtension.class)
  void logException_givenLevelEqualsDebug(final CapturedOutput capturedOutput) {
    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel("foo", LogLevel.DEBUG);

    final Logger logger = LoggerFactory.getLogger("foo");
    LOGGER_SERVICE.logException(logger, Level.DEBUG, "foo", new RuntimeException("foo"));
    assertThat(capturedOutput)
        .containsOnlyOnce("DEBUG foo - foo")
        .containsSubsequence("DEBUG foo - foo", "java.lang.RuntimeException: foo");
  }

  @Test
  @ExtendWith(OutputCaptureExtension.class)
  void logException_givenLevelEqualsInfo(final CapturedOutput capturedOutput) {
    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel("foo", LogLevel.INFO);

    final Logger logger = LoggerFactory.getLogger("foo");
    LOGGER_SERVICE.logException(logger, Level.INFO, "foo", new RuntimeException("foo"));
    assertThat(capturedOutput)
        .containsOnlyOnce("INFO foo - foo")
        .containsSubsequence("INFO foo - foo", "java.lang.RuntimeException: foo");
  }

  @Test
  @ExtendWith(OutputCaptureExtension.class)
  void logException_givenLevelEqualsWarn(final CapturedOutput capturedOutput) {
    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel("foo", LogLevel.WARN);

    final Logger logger = LoggerFactory.getLogger("foo");
    LOGGER_SERVICE.logException(logger, Level.WARN, "foo", new RuntimeException("foo"));
    assertThat(capturedOutput)
        .containsOnlyOnce("WARN foo - foo")
        .containsSubsequence("WARN foo - foo", "java.lang.RuntimeException: foo");
  }

  @Test
  @ExtendWith(OutputCaptureExtension.class)
  void logException_givenLevelEqualsError(final CapturedOutput capturedOutput) {
    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel("foo", LogLevel.ERROR);

    final Logger logger = LoggerFactory.getLogger("foo");
    LOGGER_SERVICE.logException(logger, Level.ERROR, "foo", new RuntimeException("foo"));
    assertThat(capturedOutput)
        .containsOnlyOnce("ERROR foo - foo")
        .containsSubsequence("ERROR foo - foo", "java.lang.RuntimeException: foo");
  }

  @Test
  @ExtendWith(OutputCaptureExtension.class)
  void logException_givenLevelEqualsDefault(final CapturedOutput capturedOutput) {
    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel("foo", LogLevel.DEBUG);

    final Logger logger = LoggerFactory.getLogger("foo");
    LOGGER_SERVICE.logException(logger, Level.DEFAULT, "foo", new RuntimeException("foo"));
    assertThat(capturedOutput)
        .doesNotContain("foo - foo")
        .doesNotContain("java.lang.RuntimeException: foo");
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
}
