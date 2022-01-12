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
  void toString_givenNull() {
    final ObjectToStringStrategy toStringStrategy = new ObjectToStringStrategy();
    assertThat(toStringStrategy.toString(null)).isEqualTo("null");
  }

  @Test
  void toString_givenString() {
    final ObjectToStringStrategy toStringStrategy = new ObjectToStringStrategy();
    assertThat(toStringStrategy.toString("foo")).isEqualTo("foo");
  }

  @Test
  void toString_givenStringArray() {
    final ObjectToStringStrategy toStringStrategy = new ObjectToStringStrategy();
    assertThat(toStringStrategy.toString(new String[] {"foo", "bar"})).isEqualTo("[foo, bar]");
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

  @Test
  void toString_givenObjectArray() {
    final ObjectToStringStrategy toStringStrategy = new ObjectToStringStrategy();
    assertThat(
            toStringStrategy.toString(
                new TestObject[] {new TestObject("foo"), new TestObject("bar")}))
        .isEqualTo("[[value=foo], [value=bar]]");
  }
}
