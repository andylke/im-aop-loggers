package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ArrayToStringStrategyTests {

  @Mock private ObjectToStringStrategy objectToStringStrategy;

  @InjectMocks private ArrayToStringStrategy toStringStrategy;

  @Test
  void supports_givenNull_returnFalse() {
    assertThat(toStringStrategy.supports(null)).isFalse();
  }

  @Test
  void supports_givenObjectClass_returnFalse() {
    assertThat(toStringStrategy.supports(new Object())).isFalse();
  }

  @Test
  void supports_givenArrayClass_returnTrue() {
    assertThat(toStringStrategy.supports(new Object[] {})).isTrue();
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
  void toString_givenStringArray() {
    when(objectToStringStrategy.toString("foo")).thenReturn("foo");
    when(objectToStringStrategy.toString("bar")).thenReturn("bar");
    assertThat(toStringStrategy.toString(new String[] {"foo", "bar"})).isEqualTo("[foo, bar]");
  }

  @Test
  void toString_givenEmptyStringArray() {
    assertThat(toStringStrategy.toString(new String[] {})).isEqualTo("[]");
  }

  @Test
  void toString_givenStringArray_withNullElement() {
    when(objectToStringStrategy.toString("foo")).thenReturn("foo");
    assertThat(toStringStrategy.toString(new String[] {"foo", null})).isEqualTo("[foo, null]");
  }
}
