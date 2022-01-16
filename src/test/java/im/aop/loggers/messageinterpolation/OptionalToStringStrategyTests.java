package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class OptionalToStringStrategyTests {

  @Test
  void canSupport_givenNull() {
    final OptionalToStringStrategy toStringStrategy =
        new OptionalToStringStrategy(new ObjectToStringStrategy());
    assertThrows(NullPointerException.class, () -> toStringStrategy.canSupport(null));
  }

  @Test
  void canSupport_givenObjectClass() {
    final OptionalToStringStrategy toStringStrategy =
        new OptionalToStringStrategy(new ObjectToStringStrategy());
    assertThat(toStringStrategy.canSupport(Object.class)).isFalse();
  }

  @Test
  void canSupport_givenOptionalClass() {
    final OptionalToStringStrategy toStringStrategy =
        new OptionalToStringStrategy(new ObjectToStringStrategy());
    assertThat(toStringStrategy.canSupport(Optional.class)).isTrue();
  }

  @Test
  void toString_givenNull() {
    final OptionalToStringStrategy toStringStrategy =
        new OptionalToStringStrategy(new ObjectToStringStrategy());
    assertThrows(NullPointerException.class, () -> toStringStrategy.toString(null));
  }

  @Test
  void toString_givenString() {
    final OptionalToStringStrategy toStringStrategy =
        new OptionalToStringStrategy(new ObjectToStringStrategy());
    assertThrows(ClassCastException.class, () -> toStringStrategy.toString("foo"));
  }

  @Test
  void toString_givenEmptyOptional() {
    final OptionalToStringStrategy toStringStrategy =
        new OptionalToStringStrategy(new ObjectToStringStrategy());
    assertThat(toStringStrategy.toString(Optional.empty())).isEqualTo("null");
  }

  @Test
  void toString_givenNonEmptyOptional() {
    final OptionalToStringStrategy toStringStrategy =
        new OptionalToStringStrategy(new ObjectToStringStrategy());
    assertThat(toStringStrategy.toString(Optional.of("foo"))).isEqualTo("foo");
  }
}
