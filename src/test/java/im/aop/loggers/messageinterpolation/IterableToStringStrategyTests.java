package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class IterableToStringStrategyTests {

  @Test
  void canSupport_givenNull() {
    final IterableToStringStrategy toStringStrategy =
        new IterableToStringStrategy(new ObjectToStringStrategy());
    assertThrows(NullPointerException.class, () -> toStringStrategy.canSupport(null));
  }

  @Test
  void canSupport_givenObjectClass() {
    final IterableToStringStrategy toStringStrategy =
        new IterableToStringStrategy(new ObjectToStringStrategy());
    assertThat(toStringStrategy.canSupport(Object.class)).isFalse();
  }

  @Test
  void canSupport_givenIterableClass() {
    final IterableToStringStrategy toStringStrategy =
        new IterableToStringStrategy(new ObjectToStringStrategy());
    assertThat(toStringStrategy.canSupport(Iterable.class)).isTrue();
  }

  @Test
  void canSupport_givenListClass() {
    final IterableToStringStrategy toStringStrategy =
        new IterableToStringStrategy(new ObjectToStringStrategy());
    assertThat(toStringStrategy.canSupport(List.class)).isTrue();
  }

  @Test
  void toString_givenNull() {
    final IterableToStringStrategy toStringStrategy =
        new IterableToStringStrategy(new ObjectToStringStrategy());
    assertThrows(NullPointerException.class, () -> toStringStrategy.toString(null));
  }

  @Test
  void toString_givenString() {
    final IterableToStringStrategy toStringStrategy =
        new IterableToStringStrategy(new ObjectToStringStrategy());
    assertThrows(ClassCastException.class, () -> toStringStrategy.toString("foo"));
  }

  @Test
  void toString_givenIterableString() {
    final IterableToStringStrategy toStringStrategy =
        new IterableToStringStrategy(new ObjectToStringStrategy());
    assertThat(toStringStrategy.toString(Arrays.asList("foo", "bar"))).isEqualTo("[foo, bar]");
  }

  @Test
  void toString_givenEmptyIterableString() {
    final IterableToStringStrategy toStringStrategy =
        new IterableToStringStrategy(new ObjectToStringStrategy());
    assertThat(toStringStrategy.toString(Arrays.asList())).isEqualTo("[]");
  }

  @Test
  void toString_givenIterableString_withNullElement() {
    final IterableToStringStrategy toStringStrategy =
        new IterableToStringStrategy(new ObjectToStringStrategy());
    assertThat(toStringStrategy.toString(Arrays.asList("foo", null))).isEqualTo("[foo, null]");
  }
}
