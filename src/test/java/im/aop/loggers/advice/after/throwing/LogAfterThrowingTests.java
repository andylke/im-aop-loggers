package im.aop.loggers.advice.after.throwing;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import im.aop.loggers.advice.before.LogBefore;
import im.aop.loggers.logging.Level;

/**
 * Tests for {@link LogAfterThrowing}.
 *
 * @author Andy Lian
 */
class LogAfterThrowingThrowingTests {

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
    @LogAfterThrowing
    class Local {};

    final LogAfterThrowing annotation = Local.class.getAnnotation(LogAfterThrowing.class);
    assertThat(annotation.level()).isEqualTo(Level.DEFAULT);
  }

  @Test
  void level_givenAttributeValue() {
    @LogAfterThrowing(level = Level.DEBUG)
    class Local {};

    final LogAfterThrowing annotation = Local.class.getAnnotation(LogAfterThrowing.class);
    assertThat(annotation.level()).isEqualTo(Level.DEBUG);
  }

  @Test
  void exitedAbnormallyMessage_defaultValue() {
    @LogAfterThrowing
    class Local {};

    final LogAfterThrowing annotation = Local.class.getAnnotation(LogAfterThrowing.class);
    assertThat(annotation.exitedAbnormallyMessage()).isEmpty();
  }

  @Test
  void exitedAbnormallyMessage_givenAttributeValue() {
    @LogAfterThrowing(exitedAbnormallyMessage = "foo")
    class Local {};

    final LogAfterThrowing annotation = Local.class.getAnnotation(LogAfterThrowing.class);
    assertThat(annotation.exitedAbnormallyMessage()).isEqualTo("foo");
  }

  @Test
  void ignoreExceptions_defaultValue() {
    @LogAfterThrowing
    class Local {};

    final LogAfterThrowing annotation = Local.class.getAnnotation(LogAfterThrowing.class);
    assertThat(annotation.ignoreExceptions()).isEmpty();
  }

  @Test
  void ignoreExceptions_givenAttributeValue() {
    @LogAfterThrowing(ignoreExceptions = {RuntimeException.class})
    class Local {};

    final LogAfterThrowing annotation = Local.class.getAnnotation(LogAfterThrowing.class);
    assertThat(annotation.ignoreExceptions()).containsExactly(RuntimeException.class);
  }

  @Test
  void printStackTrace_defaultValue() {
    @LogAfterThrowing
    class Local {};

    final LogAfterThrowing annotation = Local.class.getAnnotation(LogAfterThrowing.class);
    assertThat(annotation.printStackTrace()).isTrue();
  }

  @Test
  void printStackTrace_givenAttributeValue() {
    @LogAfterThrowing(printStackTrace = false)
    class Local {};

    final LogAfterThrowing annotation = Local.class.getAnnotation(LogAfterThrowing.class);
    assertThat(annotation.printStackTrace()).isFalse();
  }
}
