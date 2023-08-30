package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;

/**
 * Tests for {@link ReflectionToStringStrategy}.
 *
 * @author Andy Lian
 */
@ExtendWith(MockitoExtension.class)
class ReflectionToStringStrategyTests {

  @Spy private ReflectionToStringProperties reflectionToStringProperties;

  @InjectMocks private ReflectionToStringStrategy toStringStrategy;

  @Test
  void postConstruct_givenInvalidBaseClassesInProperties() {
    when(reflectionToStringProperties.getBaseClasses()).thenReturn(new String[] {"foo"});

    assertThrows(
        InvalidConfigurationPropertyValueException.class, () -> toStringStrategy.postConstruct());
  }

  @Test
  void supports_givenObjectClass_returnFalse() {
    toStringStrategy.postConstruct();
    assertThat(toStringStrategy.supports(new Object())).isFalse();
  }

  @Test
  void supports_givenObjectClassAssignableToBaseClasses_returnTrue() {
    when(reflectionToStringProperties.getBaseClasses())
        .thenReturn(new String[] {Object.class.getName()});
    toStringStrategy.postConstruct();

    assertThat(toStringStrategy.supports(new Object())).isTrue();
  }

  @Test
  void supports_givenObjectClassNotAssignableToBaseClasses_returnFalse() {
    when(reflectionToStringProperties.getBaseClasses())
        .thenReturn(new String[] {String.class.getName()});
    toStringStrategy.postConstruct();

    assertThat(toStringStrategy.supports(new Object())).isFalse();
  }

  @Test
  void toString_givenNull() {
    toStringStrategy.postConstruct();

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
    toStringStrategy.postConstruct();

    assertThat(toStringStrategy.toString(new TestObject("foo"))).isEqualTo("[value=foo]");
  }

  @Test
  void toString_givenNullValueTestObject_withDefaultExcludeNullValues() {
    toStringStrategy.postConstruct();

    assertThat(toStringStrategy.toString(new TestObject(null))).isEqualTo("[]");
  }

  @Test
  void toString_givenNullValueTestObject_withExcludeNullValuesEqualsFalse() {
    when(reflectionToStringProperties.isExcludeNullValues()).thenReturn(false);
    toStringStrategy.postConstruct();

    assertThat(toStringStrategy.toString(new TestObject(null))).isEqualTo("[value=<null>]");
  }

  @Test
  void toString_givenTestObject_withDefaultExcludeFieldNames() {
    toStringStrategy.postConstruct();

    assertThat(toStringStrategy.toString(new TestObject("foo"))).isEqualTo("[value=foo]");
  }

  @Test
  void toString_givenTestObject_withExcludeFieldNamesEqualsValue() {
    when(reflectionToStringProperties.getExcludeFieldNames()).thenReturn(new String[] {"value"});
    toStringStrategy.postConstruct();

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
    toStringStrategy.postConstruct();

    assertThat(toStringStrategy.toString(new CharSequenceHolder(""))).isEqualTo("[]");
  }

  @Test
  void toString_givenEmptyStringTestObject_withExcludeEmptyValuesEqualsFalse() {
    when(reflectionToStringProperties.isExcludeEmptyValues()).thenReturn(false);
    toStringStrategy.postConstruct();

    assertThat(toStringStrategy.toString(new CharSequenceHolder(""))).isEqualTo("[value=]");
  }

  @Test
  void toString_givenNonEmptyStringTestObject_withDefaultExcludeEmptyValues() {
    toStringStrategy.postConstruct();

    assertThat(toStringStrategy.toString(new CharSequenceHolder("foo"))).isEqualTo("[value=foo]");
  }

  @Test
  void toString_givenNonEmptyStringTestObject_withExcludeEmptyValuesEqualsFalse() {
    when(reflectionToStringProperties.isExcludeEmptyValues()).thenReturn(false);
    toStringStrategy.postConstruct();

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
    toStringStrategy.postConstruct();

    assertThat(toStringStrategy.toString(new NumberHolder(Integer.valueOf(0)))).isEqualTo("[]");
  }

  @Test
  void toString_givenZeroIntegerTestObject_withExcludeZeroValuesEqualsFalse() {
    when(reflectionToStringProperties.isExcludeZeroValues()).thenReturn(false);
    toStringStrategy.postConstruct();

    assertThat(toStringStrategy.toString(new NumberHolder(Integer.valueOf(0))))
        .isEqualTo("[value=0]");
  }

  @Test
  void toString_givenNonZeroIntegerTestObject_withDefaultExcludeZeroValues() {
    toStringStrategy.postConstruct();

    assertThat(toStringStrategy.toString(new NumberHolder(Integer.valueOf(1))))
        .isEqualTo("[value=1]");
  }

  @Test
  void toString_givenNonZeroIntegerTestObject_withExcludeZeroValuesEqualsFalse() {
    when(reflectionToStringProperties.isExcludeZeroValues()).thenReturn(false);
    toStringStrategy.postConstruct();

    assertThat(toStringStrategy.toString(new NumberHolder(Integer.valueOf(1))))
        .isEqualTo("[value=1]");
  }

  @Test
  void toString_givenZeroDoubleTestObject_withDefaultExcludeZeroValues() {
    toStringStrategy.postConstruct();

    assertThat(toStringStrategy.toString(new NumberHolder(Double.valueOf(0.0)))).isEqualTo("[]");
  }

  @Test
  void toString_givenZeroDoubleTestObject_withExcludeZeroValuesEqualsFalse() {
    when(reflectionToStringProperties.isExcludeZeroValues()).thenReturn(false);
    toStringStrategy.postConstruct();

    assertThat(toStringStrategy.toString(new NumberHolder(Double.valueOf(0.0))))
        .isEqualTo("[value=0.0]");
  }

  @Test
  void toString_givenNonZeroDoubleTestObject_withDefaultExcludeZeroValues() {
    toStringStrategy.postConstruct();

    assertThat(toStringStrategy.toString(new NumberHolder(Double.valueOf(1.1))))
        .isEqualTo("[value=1.1]");
  }

  @Test
  void toString_givenNonZeroDoubleTestObject_withExcludeZeroValuesEqualsFalse() {
    when(reflectionToStringProperties.isExcludeZeroValues()).thenReturn(false);
    toStringStrategy.postConstruct();

    assertThat(toStringStrategy.toString(new NumberHolder(Double.valueOf(1.1))))
        .isEqualTo("[value=1.1]");
  }

  public interface TestInterface {

    String getValue();
  }

  @Test
  void supports_givenJavaProxy_returnFalse() {
    toStringStrategy.postConstruct();

    TestInterface proxy =
        (TestInterface)
            Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                new Class<?>[] {TestInterface.class},
                new InvocationHandler() {

                  @Override
                  public Object invoke(Object proxy, Method method, Object[] args)
                      throws Throwable {
                    return "foo";
                  }
                });

    assertThat(toStringStrategy.supports(proxy.getClass())).isFalse();
  }

  @Test
  void toString_givenJavaProxy_willThrow() {
    toStringStrategy.postConstruct();

    TestInterface proxy =
        (TestInterface)
            Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                new Class<?>[] {TestInterface.class},
                new InvocationHandler() {

                  @Override
                  public Object invoke(Object proxy, Method method, Object[] args)
                      throws Throwable {
                    return "foo";
                  }
                });

    assertThrows(InaccessibleObjectException.class, () -> toStringStrategy.toString(proxy));
  }
}
