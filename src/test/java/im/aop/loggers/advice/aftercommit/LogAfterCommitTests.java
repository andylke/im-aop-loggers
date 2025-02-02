package im.aop.loggers.advice.aftercommit;

import static org.assertj.core.api.Assertions.assertThat;

import im.aop.loggers.Level;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LogAfterCommit}.
 *
 * @author Andy Lian
 */
class LogAfterCommitTests {

  @Test
  void declaringClass_defaultValue() {
    @LogAfterCommit
    class Local {}

    final LogAfterCommit annotation = Local.class.getAnnotation(LogAfterCommit.class);
    assertThat(annotation.declaringClass()).isEqualTo(void.class);
  }

  @Test
  void declaringClass_givenAttributeValue() {
    @LogAfterCommit(declaringClass = Local.class)
    class Local {}

    final LogAfterCommit annotation = Local.class.getAnnotation(LogAfterCommit.class);
    assertThat(annotation.declaringClass()).isEqualTo(Local.class);
  }

  @Test
  void loggingLevel_defaultValue() {
    @LogAfterCommit
    class Local {}

    final LogAfterCommit annotation = Local.class.getAnnotation(LogAfterCommit.class);
    assertThat(annotation.loggingLevel()).isEqualTo(Level.DEFAULT);
  }

  @Test
  void loggingLevel_givenAttributeValue() {
    @LogAfterCommit(loggingLevel = Level.DEBUG)
    class Local {}

    final LogAfterCommit annotation = Local.class.getAnnotation(LogAfterCommit.class);
    assertThat(annotation.loggingLevel()).isEqualTo(Level.DEBUG);
  }

  @Test
  void messageTemplate_defaultValue() {
    @LogAfterCommit
    class Local {}

    final LogAfterCommit annotation = Local.class.getAnnotation(LogAfterCommit.class);
    assertThat(annotation.messageTemplate()).isEmpty();
  }

  @Test
  void messageTemplate_givenAttributeValue() {
    @LogAfterCommit(messageTemplate = "foo")
    class Local {}

    final LogAfterCommit annotation = Local.class.getAnnotation(LogAfterCommit.class);
    assertThat(annotation.messageTemplate()).isEqualTo("foo");
  }
}
