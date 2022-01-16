package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ArrayToStringStrategyTests {

  @Test
  void canSupport_givenNull() {
    final ArrayToStringStrategy toStringStrategy =
        new ArrayToStringStrategy(new ObjectToStringStrategy());
    assertThrows(NullPointerException.class, () -> toStringStrategy.canSupport(null));
  }

  @Test
  void canSupport_givenObjectClass() {
    final ArrayToStringStrategy toStringStrategy =
        new ArrayToStringStrategy(new ObjectToStringStrategy());
    assertThat(toStringStrategy.canSupport(Object.class)).isFalse();
  }

  @Test
  void canSupport_givenArrayClass() {
    final ArrayToStringStrategy toStringStrategy =
        new ArrayToStringStrategy(new ObjectToStringStrategy());
    assertThat(toStringStrategy.canSupport(Object[].class)).isTrue();
  }

  @Test
  void toString_givenNull() {
    final ArrayToStringStrategy toStringStrategy =
        new ArrayToStringStrategy(new ObjectToStringStrategy());
    assertThrows(NullPointerException.class, () -> toStringStrategy.toString(null));
  }

  @Test
  void toString_givenString() {
    final ArrayToStringStrategy toStringStrategy =
        new ArrayToStringStrategy(new ObjectToStringStrategy());
    assertThrows(ClassCastException.class, () -> toStringStrategy.toString("foo"));
  }

  @Test
  void toString_givenStringArray() {
    final ArrayToStringStrategy toStringStrategy =
        new ArrayToStringStrategy(new ObjectToStringStrategy());
    assertThat(toStringStrategy.toString(new String[] {"foo", "bar"})).isEqualTo("[foo, bar]");
  }

  @Test
  void toString_givenEmptyStringArray() {
    final ArrayToStringStrategy toStringStrategy =
        new ArrayToStringStrategy(new ObjectToStringStrategy());
    assertThat(toStringStrategy.toString(new String[] {})).isEqualTo("[]");
  }

  @Test
  void toString_givenStringArray_withNullElement() {
    final ArrayToStringStrategy toStringStrategy =
        new ArrayToStringStrategy(new ObjectToStringStrategy());
    assertThat(toStringStrategy.toString(new String[] {"foo", null})).isEqualTo("[foo, null]");
  }
}
