package im.aop.loggers.advice.around;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import im.aop.loggers.advice.before.LogBefore;
import im.aop.loggers.logging.Level;

/**
 * Tests for {@link LogAround}.
 *
 * @author Andy Lian
 */
class LogAroundTests {

  @Test
  void declaringClass_defaultValue() {
    @LogBefore
    class Local {};

    final LogBefore annotation = Local.class.getAnnotation(LogBefore.class);
    assertThat(annotation.declaringClass()).isEqualTo(void.class);
  }

  @Test
  void declaringClass_givenAttributeValue() {
    @LogBefore(declaringClass = Local.class)
    class Local {};

    final LogBefore annotation = Local.class.getAnnotation(LogBefore.class);
    assertThat(annotation.declaringClass()).isEqualTo(Local.class);
  }

  @Test
  void level_defaultValue() {
    @LogAround
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.level()).isEqualTo(Level.DEFAULT);
  }

  @Test
  void level_givenAttributeValue() {
    @LogAround(level = Level.DEBUG)
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.level()).isEqualTo(Level.DEBUG);
  }

  @Test
  void enteringMessage_defaultValue() {
    @LogAround
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.enteringMessage()).isEmpty();
  }

  @Test
  void enteringMessage_givenAttributeValue() {
    @LogAround(enteringMessage = "foo")
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.enteringMessage()).isEqualTo("foo");
  }

  @Test
  void exitedMessage_defaultValue() {
    @LogAround
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.exitedMessage()).isEmpty();
  }

  @Test
  void exitedMessage_givenAttributeValue() {
    @LogAround(exitedMessage = "foo")
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.exitedMessage()).isEqualTo("foo");
  }

  @Test
  void exitedAbnormallyLevel_defaultValue() {
    @LogAround
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.exitedAbnormallyLevel()).isEqualTo(Level.DEFAULT);
  }

  @Test
  void exitedAbnormallyLevel_givenAttributeValue() {
    @LogAround(exitedAbnormallyLevel = Level.DEBUG)
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.exitedAbnormallyLevel()).isEqualTo(Level.DEBUG);
  }

  @Test
  void exitedAbnormallyMessage_defaultValue() {
    @LogAround
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.exitedAbnormallyMessage()).isEmpty();
  }

  @Test
  void exitedAbnormallyMessage_givenAttributeValue() {
    @LogAround(exitedAbnormallyMessage = "foo")
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.exitedAbnormallyMessage()).isEqualTo("foo");
  }

  @Test
  void ignoreExceptions_defaultValue() {
    @LogAround
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.ignoreExceptions()).isEmpty();
  }

  @Test
  void ignoreExceptions_givenAttributeValue() {
    @LogAround(ignoreExceptions = {RuntimeException.class})
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.ignoreExceptions()).containsExactly(RuntimeException.class);
  }

  @Test
  void printStackTrace_defaultValue() {
    @LogAround
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.printStackTrace()).isTrue();
  }

  @Test
  void printStackTrace_givenAttributeValue() {
    @LogAround(printStackTrace = false)
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.printStackTrace()).isFalse();
  }

  @Test
  void elapsedMessage_defaultValue() {
    @LogAround
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.elapsedMessage()).isEmpty();
  }

  @Test
  void elapsedMessage_givenAttributeValue() {
    @LogAround(elapsedMessage = "foo")
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.elapsedMessage()).isEqualTo("foo");
  }

  @Test
  void elapsedWarningLevel_defaultValue() {
    @LogAround
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.elapsedWarningLevel()).isEqualTo(Level.DEFAULT);
  }

  @Test
  void elapsedWarningLevel_givenAttributeValue() {
    @LogAround(elapsedWarningLevel = Level.DEBUG)
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.elapsedWarningLevel()).isEqualTo(Level.DEBUG);
  }

  @Test
  void elapsedWarningMessage_defaultValue() {
    @LogAround
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.elapsedWarningMessage()).isEmpty();
  }

  @Test
  void elapsedWarningMessage_givenAttributeValue() {
    @LogAround(elapsedWarningMessage = "foo")
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.elapsedWarningMessage()).isEqualTo("foo");
  }

  @Test
  void elapsedTimeLimit_defaultValue() {
    @LogAround
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.elapsedTimeLimit()).isZero();
  }

  @Test
  void elapsedTimeLimit_givenAttributeValue() {
    @LogAround(elapsedTimeLimit = 1)
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.elapsedTimeLimit()).isOne();
  }

  @Test
  void elapsedTimeUnit_defaultValue() {
    @LogAround
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.elapsedTimeUnit()).isEqualTo(ChronoUnit.MILLIS);
  }

  @Test
  void elapsedTimeUnit_givenAttributeValue() {
    @LogAround(elapsedTimeUnit = ChronoUnit.SECONDS)
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.elapsedTimeUnit()).isEqualTo(ChronoUnit.SECONDS);
  }
}
