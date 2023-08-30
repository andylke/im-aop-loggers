package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for {@link ObjectToStringStrategy}.
 *
 * @author Andy Lian
 */
@ExtendWith(MockitoExtension.class)
class ObjectToStringStrategyTests {

  @Mock private ReflectionToStringStrategy reflectionToStringStrategy;

  @InjectMocks private ObjectToStringStrategy toStringStrategy;

  @Test
  void supports_givenNull() {
    assertThat(toStringStrategy.supports(null)).isTrue();
  }

  @Test
  void supports_givenObjectClass() {
    assertThat(toStringStrategy.supports(new Object())).isTrue();
  }

  @Test
  void toString_givenNull() {
    assertThat(toStringStrategy.toString(null)).isEqualTo("null");
  }

  @Test
  void toString_givenString() {
    assertThat(toStringStrategy.toString("foo")).isEqualTo("foo");
  }

  static class TestObject {

    private String value;

    public TestObject(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return "[value=" + value + "]";
    }
  }

  @Test
  void toString_givenObject() {
    when(reflectionToStringStrategy.supports(any())).thenReturn(false);

    assertThat(toStringStrategy.toString(new TestObject("foo"))).isEqualTo("[value=foo]");
  }

  @Test
  void toString_withReflectionToStringStrategy() {
    when(reflectionToStringStrategy.supports(any())).thenReturn(true);
    when(reflectionToStringStrategy.toString(any())).thenReturn("x");

    assertThat(toStringStrategy.toString(new TestObject("foo"))).isEqualTo("x");
  }
}
