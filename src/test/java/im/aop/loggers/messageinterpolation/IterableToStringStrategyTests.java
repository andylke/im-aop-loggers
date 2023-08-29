package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class IterableToStringStrategyTests {

  @Test
  void supports_givenNull_returnFalse() {
    final IterableToStringStrategy toStringStrategy =
        new IterableToStringStrategy(new ObjectToStringStrategy());
    assertThat(toStringStrategy.supports(null)).isFalse();
  }

  @Test
  void supports_givenObjectClass_returnFalse() {
    final IterableToStringStrategy toStringStrategy =
        new IterableToStringStrategy(new ObjectToStringStrategy());
    assertThat(toStringStrategy.supports(new Object())).isFalse();
  }

  @Test
  void supports_givenListClass_returnTrue() {
    final IterableToStringStrategy toStringStrategy =
        new IterableToStringStrategy(new ObjectToStringStrategy());
    assertThat(toStringStrategy.supports(new ArrayList<Object>())).isTrue();
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
