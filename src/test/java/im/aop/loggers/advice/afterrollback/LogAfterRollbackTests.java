package im.aop.loggers.advice.afterrollback;

import static org.assertj.core.api.Assertions.assertThat;

import im.aop.loggers.Level;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LogAfterRollback}.
 *
 * @author Andy Lian
 */
class LogAfterRollbackTests {

  @Test
  void declaringClass_defaultValue() {
    @LogAfterRollback
    class Local {}

    final LogAfterRollback annotation = Local.class.getAnnotation(LogAfterRollback.class);
    assertThat(annotation.declaringClass()).isEqualTo(void.class);
  }

  @Test
  void declaringClass_givenAttributeValue() {
    @LogAfterRollback(declaringClass = Local.class)
    class Local {}

    final LogAfterRollback annotation = Local.class.getAnnotation(LogAfterRollback.class);
    assertThat(annotation.declaringClass()).isEqualTo(Local.class);
  }

  @Test
  void loggingLevel_defaultValue() {
    @LogAfterRollback
    class Local {}

    final LogAfterRollback annotation = Local.class.getAnnotation(LogAfterRollback.class);
    assertThat(annotation.loggingLevel()).isEqualTo(Level.DEFAULT);
  }

  @Test
  void loggingLevel_givenAttributeValue() {
    @LogAfterRollback(loggingLevel = Level.DEBUG)
    class Local {}

    final LogAfterRollback annotation = Local.class.getAnnotation(LogAfterRollback.class);
    assertThat(annotation.loggingLevel()).isEqualTo(Level.DEBUG);
  }

  @Test
  void messageTemplate_defaultValue() {
    @LogAfterRollback
    class Local {}

    final LogAfterRollback annotation = Local.class.getAnnotation(LogAfterRollback.class);
    assertThat(annotation.messageTemplate()).isEmpty();
  }

  @Test
  void messageTemplate_givenAttributeValue() {
    @LogAfterRollback(messageTemplate = "foo")
    class Local {}

    final LogAfterRollback annotation = Local.class.getAnnotation(LogAfterRollback.class);
    assertThat(annotation.messageTemplate()).isEqualTo("foo");
  }
}
