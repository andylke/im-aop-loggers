package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;

/**
 * Tests for {@link ReflectionToStringStrategy}.
 *
 * @author Andy Lian
 */
class ReflectionToStringStrategyTests {

  @Test
  void instantiate_givenInvalidBaseClassesInProperties() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    reflectionToStringProperties.setBaseClasses(new String[] {"foo"});

    assertThrows(
        InvalidConfigurationPropertyValueException.class,
        () -> new ReflectionToStringStrategy(reflectionToStringProperties));
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
  void supports_givenObjectClassAssignableToBaseClasses() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    reflectionToStringProperties.setBaseClasses(new String[] {Object.class.getName()});
    final ReflectionToStringStrategy toStringStrategy =
        new ReflectionToStringStrategy(reflectionToStringProperties);
    assertThat(toStringStrategy.supports(Object.class)).isTrue();
  }

  @Test
  void supports_givenObjectClassNotAssignableToBaseClasses() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    reflectionToStringProperties.setBaseClasses(new String[] {String.class.getName()});
    final ReflectionToStringStrategy toStringStrategy =
        new ReflectionToStringStrategy(reflectionToStringProperties);
    assertThat(toStringStrategy.supports(Object.class)).isFalse();
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

  static class CharSequenceHolder {

    private CharSequence value;

    public CharSequenceHolder(CharSequence value) {
      this.value = value;
    }

    public CharSequence getValue() {
      return value;
    }
  }

  @Test
  void toString_givenEmptyStringTestObject_withDefaultExcludeEmptyValues() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    final ReflectionToStringStrategy toStringStrategy =
        new ReflectionToStringStrategy(reflectionToStringProperties);
    assertThat(toStringStrategy.toString(new CharSequenceHolder(""))).isEqualTo("[]");
  }

  @Test
  void toString_givenEmptyStringTestObject_withExcludeEmptyValuesEqualsFalse() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    reflectionToStringProperties.setExcludeEmptyValues(false);
    final ReflectionToStringStrategy toStringStrategy =
        new ReflectionToStringStrategy(reflectionToStringProperties);
    assertThat(toStringStrategy.toString(new CharSequenceHolder(""))).isEqualTo("[value=]");
  }

  @Test
  void toString_givenNonEmptyStringTestObject_withDefaultExcludeEmptyValues() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    final ReflectionToStringStrategy toStringStrategy =
        new ReflectionToStringStrategy(reflectionToStringProperties);
    assertThat(toStringStrategy.toString(new CharSequenceHolder("foo"))).isEqualTo("[value=foo]");
  }

  @Test
  void toString_givenNonEmptyStringTestObject_withExcludeEmptyValuesEqualsFalse() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    reflectionToStringProperties.setExcludeEmptyValues(false);
    final ReflectionToStringStrategy toStringStrategy =
        new ReflectionToStringStrategy(reflectionToStringProperties);
    assertThat(toStringStrategy.toString(new CharSequenceHolder("foo"))).isEqualTo("[value=foo]");
  }

  static class NumberHolder {

    private Number value;

    public NumberHolder(Number value) {
      this.value = value;
    }

    public Number getValue() {
      return value;
    }
  }

  @Test
  void toString_givenZeroIntegerTestObject_withDefaultExcludeZeroValues() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    final ReflectionToStringStrategy toStringStrategy =
        new ReflectionToStringStrategy(reflectionToStringProperties);
    assertThat(toStringStrategy.toString(new NumberHolder(Integer.valueOf(0)))).isEqualTo("[]");
  }

  @Test
  void toString_givenZeroIntegerTestObject_withExcludeZeroValuesEqualsFalse() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    reflectionToStringProperties.setExcludeZeroValues(false);
    final ReflectionToStringStrategy toStringStrategy =
        new ReflectionToStringStrategy(reflectionToStringProperties);
    assertThat(toStringStrategy.toString(new NumberHolder(Integer.valueOf(0))))
        .isEqualTo("[value=0]");
  }

  @Test
  void toString_givenNonZeroIntegerTestObject_withDefaultExcludeZeroValues() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    final ReflectionToStringStrategy toStringStrategy =
        new ReflectionToStringStrategy(reflectionToStringProperties);
    assertThat(toStringStrategy.toString(new NumberHolder(Integer.valueOf(1))))
        .isEqualTo("[value=1]");
  }

  @Test
  void toString_givenNonZeroIntegerTestObject_withExcludeZeroValuesEqualsFalse() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    reflectionToStringProperties.setExcludeZeroValues(false);
    final ReflectionToStringStrategy toStringStrategy =
        new ReflectionToStringStrategy(reflectionToStringProperties);
    assertThat(toStringStrategy.toString(new NumberHolder(Integer.valueOf(1))))
        .isEqualTo("[value=1]");
  }

  @Test
  void toString_givenZeroDoubleTestObject_withDefaultExcludeZeroValues() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    final ReflectionToStringStrategy toStringStrategy =
        new ReflectionToStringStrategy(reflectionToStringProperties);
    assertThat(toStringStrategy.toString(new NumberHolder(Double.valueOf(0.0)))).isEqualTo("[]");
  }

  @Test
  void toString_givenZeroDoubleTestObject_withExcludeZeroValuesEqualsFalse() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    reflectionToStringProperties.setExcludeZeroValues(false);
    final ReflectionToStringStrategy toStringStrategy =
        new ReflectionToStringStrategy(reflectionToStringProperties);
    assertThat(toStringStrategy.toString(new NumberHolder(Double.valueOf(0.0))))
        .isEqualTo("[value=0.0]");
  }

  @Test
  void toString_givenNonZeroDoubleTestObject_withDefaultExcludeZeroValues() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    final ReflectionToStringStrategy toStringStrategy =
        new ReflectionToStringStrategy(reflectionToStringProperties);
    assertThat(toStringStrategy.toString(new NumberHolder(Double.valueOf(1.1))))
        .isEqualTo("[value=1.1]");
  }

  @Test
  void toString_givenNonZeroDoubleTestObject_withExcludeZeroValuesEqualsFalse() {
    final ReflectionToStringProperties reflectionToStringProperties =
        new ReflectionToStringProperties();
    reflectionToStringProperties.setExcludeZeroValues(false);
    final ReflectionToStringStrategy toStringStrategy =
        new ReflectionToStringStrategy(reflectionToStringProperties);
    assertThat(toStringStrategy.toString(new NumberHolder(Double.valueOf(1.1))))
        .isEqualTo("[value=1.1]");
  }
}
