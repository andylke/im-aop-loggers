package im.aop.loggers.advice.beforecommit;

import static org.assertj.core.api.Assertions.assertThat;

import im.aop.loggers.Level;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LogBeforeCommit}.
 *
 * @author Andy Lian
 */
class LogBeforeCommitTests {

  @Test
  void declaringClass_defaultValue() {
    @LogBeforeCommit
    class Local {}

    final LogBeforeCommit annotation = Local.class.getAnnotation(LogBeforeCommit.class);
    assertThat(annotation.declaringClass()).isEqualTo(void.class);
  }

  @Test
  void declaringClass_givenAttributeValue() {
    @LogBeforeCommit(declaringClass = Local.class)
    class Local {}

    final LogBeforeCommit annotation = Local.class.getAnnotation(LogBeforeCommit.class);
    assertThat(annotation.declaringClass()).isEqualTo(Local.class);
  }

  @Test
  void loggingLevel_defaultValue() {
    @LogBeforeCommit
    class Local {}

    final LogBeforeCommit annotation = Local.class.getAnnotation(LogBeforeCommit.class);
    assertThat(annotation.loggingLevel()).isEqualTo(Level.DEFAULT);
  }

  @Test
  void loggingLevel_givenAttributeValue() {
    @LogBeforeCommit(loggingLevel = Level.DEBUG)
    class Local {}

    final LogBeforeCommit annotation = Local.class.getAnnotation(LogBeforeCommit.class);
    assertThat(annotation.loggingLevel()).isEqualTo(Level.DEBUG);
  }

  @Test
  void messageTemplate_defaultValue() {
    @LogBeforeCommit
    class Local {}

    final LogBeforeCommit annotation = Local.class.getAnnotation(LogBeforeCommit.class);
    assertThat(annotation.messageTemplate()).isEmpty();
  }

  @Test
  void messageTemplate_givenAttributeValue() {
    @LogBeforeCommit(messageTemplate = "foo")
    class Local {}

    final LogBeforeCommit annotation = Local.class.getAnnotation(LogBeforeCommit.class);
    assertThat(annotation.messageTemplate()).isEqualTo("foo");
  }
}
