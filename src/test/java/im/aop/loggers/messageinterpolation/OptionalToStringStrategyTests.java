package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OptionalToStringStrategyTests {

  @Mock private ObjectToStringStrategy objectToStringStrategy;

  @InjectMocks private OptionalToStringStrategy toStringStrategy;

  @Test
  void supports_givenNull_returnFalse() {
    assertThat(toStringStrategy.supports(null)).isFalse();
  }

  @Test
  void supports_givenObjectClass_returnFalse() {
    assertThat(toStringStrategy.supports(new Object())).isFalse();
  }

  @Test
  void supports_givenOptionalClass_returnTrue() {
    assertThat(toStringStrategy.supports(Optional.empty())).isTrue();
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
  void toString_givenEmptyOptional() {
    assertThat(toStringStrategy.toString(Optional.empty())).isEqualTo("null");
  }

  @Test
  void toString_givenNonEmptyOptional() {
    when(objectToStringStrategy.toString("foo")).thenReturn("foo");
    assertThat(toStringStrategy.toString(Optional.of("foo"))).isEqualTo("foo");
  }
}
