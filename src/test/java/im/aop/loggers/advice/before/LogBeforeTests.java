package im.aop.loggers.advice.before;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import im.aop.loggers.logging.Level;

/**
 * Tests for {@link LogBefore}.
 *
 * @author Andy Lian
 */
class LogBeforeTests {

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
    @LogBefore
    class Local {};

    final LogBefore annotation = Local.class.getAnnotation(LogBefore.class);
    assertThat(annotation.level()).isEqualTo(Level.DEFAULT);
  }

  @Test
  void level_givenAttributeValue() {
    @LogBefore(level = Level.DEBUG)
    class Local {};

    final LogBefore annotation = Local.class.getAnnotation(LogBefore.class);
    assertThat(annotation.level()).isEqualTo(Level.DEBUG);
  }

  @Test
  void enteringMessage_defaultValue() {
    @LogBefore
    class Local {};

    final LogBefore annotation = Local.class.getAnnotation(LogBefore.class);
    assertThat(annotation.enteringMessage()).isEmpty();
  }

  @Test
  void enteringMessage_givenAttributeValue() {
    @LogBefore(enteringMessage = "foo")
    class Local {};

    final LogBefore annotation = Local.class.getAnnotation(LogBefore.class);
    assertThat(annotation.enteringMessage()).isEqualTo("foo");
  }
}
