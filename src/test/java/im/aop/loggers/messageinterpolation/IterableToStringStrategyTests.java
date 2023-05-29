package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class IterableToStringStrategyTests {

  @Test
  void supports_givenNull() {
    final IterableToStringStrategy toStringStrategy =
        new IterableToStringStrategy(new ObjectToStringStrategy());
    assertThrows(NullPointerException.class, () -> toStringStrategy.supports(null));
  }

  @Test
  void supports_givenObjectClass() {
    final IterableToStringStrategy toStringStrategy =
        new IterableToStringStrategy(new ObjectToStringStrategy());
    assertThat(toStringStrategy.supports(Object.class)).isFalse();
  }

  @Test
  void supports_givenIterableClass() {
    final IterableToStringStrategy toStringStrategy =
        new IterableToStringStrategy(new ObjectToStringStrategy());
    assertThat(toStringStrategy.supports(Iterable.class)).isTrue();
  }

  @Test
  void supports_givenListClass() {
    final IterableToStringStrategy toStringStrategy =
        new IterableToStringStrategy(new ObjectToStringStrategy());
    assertThat(toStringStrategy.supports(List.class)).isTrue();
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
