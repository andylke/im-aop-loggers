package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ReflectionToStringStrategy}.
 *
 * @author Andy Lian
 */
class ReflectionToStringStrategyTests {

  @Test
  void supports_givenNull() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    final ReflectionToStringStrategy toStringStrategy =
        new ReflectionToStringStrategy(reflectionToStringProperties);
    assertThrows(NullPointerException.class, () -> toStringStrategy.supports(null));
  }

  @Test
  void supports_givenObjectClass() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    final ReflectionToStringStrategy toStringStrategy =
        new ReflectionToStringStrategy(reflectionToStringProperties);
    assertThat(toStringStrategy.supports(Object.class)).isFalse();
  }

  @Test
  void supports_givenObjectClass_withObjectPackageAsBasePackage() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    reflectionToStringProperties.setBasePackages(new String[] {Object.class.getPackageName()});
    final ReflectionToStringStrategy toStringStrategy =
        new ReflectionToStringStrategy(reflectionToStringProperties);
    assertThat(toStringStrategy.supports(Object.class)).isTrue();
  }

  @Test
  void toString_givenNull() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    final ReflectionToStringStrategy toStringStrategy =
        new ReflectionToStringStrategy(reflectionToStringProperties);
    assertThrows(NullPointerException.class, () -> toStringStrategy.toString(null));
  }

  static class TestObject {

    private String value;

    public TestObject(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

  @Test
  void toString_givenTestObject() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    final ReflectionToStringStrategy toStringStrategy =
        new ReflectionToStringStrategy(reflectionToStringProperties);
    assertThat(toStringStrategy.toString(new TestObject("foo"))).isEqualTo("[value=foo]");
  }

  @Test
  void toString_givenNullValueTestObject_withDefaultExcludeNullValues() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    final ReflectionToStringStrategy toStringStrategy =
        new ReflectionToStringStrategy(reflectionToStringProperties);
    assertThat(toStringStrategy.toString(new TestObject(null))).isEqualTo("[]");
  }

  @Test
  void toString_givenNullValueTestObject_withExcludeNullValuesEqualsFalse() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    reflectionToStringProperties.setExcludeNullValues(false);
    final ReflectionToStringStrategy toStringStrategy =
        new ReflectionToStringStrategy(reflectionToStringProperties);
    assertThat(toStringStrategy.toString(new TestObject(null))).isEqualTo("[value=<null>]");
  }

  @Test
  void toString_givenTestObject_withDefaultExcludeFieldNames() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    final ReflectionToStringStrategy toStringStrategy =
        new ReflectionToStringStrategy(reflectionToStringProperties);
    assertThat(toStringStrategy.toString(new TestObject("foo"))).isEqualTo("[value=foo]");
  }

  @Test
  void toString_givenTestObject_withExcludeFieldNamesEqualsValue() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    reflectionToStringProperties.setExcludeFieldNames(new String[] {"value"});
    final ReflectionToStringStrategy toStringStrategy =
        new ReflectionToStringStrategy(reflectionToStringProperties);
    assertThat(toStringStrategy.toString(new TestObject("foo"))).isEqualTo("[]");
  }
}
