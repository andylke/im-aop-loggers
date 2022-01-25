package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ObjectToStringStrategy}.
 *
 * @author Andy Lian
 */
class ObjectToStringStrategyTests {

  @Test
  void supports_givenNull() {
    final ObjectToStringStrategy toStringStrategy = new ObjectToStringStrategy();
    assertThat(toStringStrategy.supports(null)).isTrue();
  }

  @Test
  void supports_givenObjectClass() {
    final ObjectToStringStrategy toStringStrategy = new ObjectToStringStrategy();
    assertThat(toStringStrategy.supports(Object.class)).isTrue();
  }

  @Test
  void toString_givenNull() {
    final ObjectToStringStrategy toStringStrategy = new ObjectToStringStrategy();
    assertThat(toStringStrategy.toString(null)).isEqualTo("null");
  }

  @Test
  void toString_givenString() {
    final ObjectToStringStrategy toStringStrategy = new ObjectToStringStrategy();
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
    final ObjectToStringStrategy toStringStrategy = new ObjectToStringStrategy();
    assertThat(toStringStrategy.toString(new TestObject("foo"))).isEqualTo("[value=foo]");
  }
}
