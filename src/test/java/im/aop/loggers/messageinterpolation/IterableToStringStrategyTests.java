package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IterableToStringStrategyTests {

  @Mock private ObjectToStringStrategy objectToStringStrategy;

  @InjectMocks private IterableToStringStrategy toStringStrategy;

  @Test
  void supports_givenNull_returnFalse() {
    assertThat(toStringStrategy.supports(null)).isFalse();
  }

  @Test
  void supports_givenObjectClass_returnFalse() {
    assertThat(toStringStrategy.supports(new Object())).isFalse();
  }

  @Test
  void supports_givenListClass_returnTrue() {
    assertThat(toStringStrategy.supports(new ArrayList<Object>())).isTrue();
  }

  @Test
  void toString_givenNull() {
    assertThrows(NullPointerException.class, () -> toStringStrategy.toString(null));
  }

  @Test
  void toString_givenString() {
    assertThrows(ClassCastException.class, () -> toStringStrategy.toString("foo"));
  }

  @Test
  void toString_givenIterableString() {
    when(objectToStringStrategy.toString("foo")).thenReturn("foo");
    when(objectToStringStrategy.toString("bar")).thenReturn("bar");
    assertThat(toStringStrategy.toString(Arrays.asList("foo", "bar"))).isEqualTo("[foo, bar]");
  }

  @Test
  void toString_givenEmptyIterableString() {
    assertThat(toStringStrategy.toString(Arrays.asList())).isEqualTo("[]");
  }

  @Test
  void toString_givenIterableString_withNullElement() {
    when(objectToStringStrategy.toString("foo")).thenReturn("foo");
    assertThat(toStringStrategy.toString(Arrays.asList("foo", null))).isEqualTo("[foo, null]");
  }
}
