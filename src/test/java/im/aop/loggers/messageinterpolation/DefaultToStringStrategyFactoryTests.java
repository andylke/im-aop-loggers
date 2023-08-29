package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultToStringStrategyFactory}.
 *
 * @author Andy Lian
 */
class DefaultToStringStrategyFactoryTests {

  @Test
  void instantiate_givenNullObjectToStringStrategy() {
    assertThrows(
        NullPointerException.class, () -> new DefaultToStringStrategyFactory(null, List.of()));
  }

  @Test
  void instantiate_givenNullToStringStrategies() {
    assertDoesNotThrow(
        () -> new DefaultToStringStrategyFactory(new ObjectToStringStrategy(), null));
  }

  static class TestClass {}

  static class TestToStringStrategy implements ToStringStrategy {

    @Override
    public boolean supports(Object object) {
      return object instanceof TestClass;
    }

    @Override
    public String toString(Object object) {
      throw new IllegalStateException();
    }
  }

  static interface TestInterface {}

  @Test
  void findOrDefault_givenNull_withNullToStringStrategies() {
    final DefaultToStringStrategyFactory factory =
        new DefaultToStringStrategyFactory(new ObjectToStringStrategy(), null);
    assertThat(factory.findOrDefault(null)).isExactlyInstanceOf(ObjectToStringStrategy.class);
  }

  @Test
  void findOrDefault_givenTestClass_withNullToStringStrategies() {
    final DefaultToStringStrategyFactory factory =
        new DefaultToStringStrategyFactory(new ObjectToStringStrategy(), null);
    assertThat(factory.findOrDefault(new TestClass()))
        .isExactlyInstanceOf(ObjectToStringStrategy.class);
  }

  @Test
  void findOrDefault_givenTestClass_withEmptyToStringStrategies() {
    final DefaultToStringStrategyFactory factory =
        new DefaultToStringStrategyFactory(new ObjectToStringStrategy(), List.of());
    assertThat(factory.findOrDefault(new TestClass()))
        .isExactlyInstanceOf(ObjectToStringStrategy.class);
  }

  @Test
  void findOrDefault_givenTestClass_WithTestToStringStrategy() {
    final DefaultToStringStrategyFactory factory =
        new DefaultToStringStrategyFactory(
            new ObjectToStringStrategy(), List.of(new TestToStringStrategy()));
    assertThat(factory.findOrDefault(new TestClass()))
        .isExactlyInstanceOf(TestToStringStrategy.class);
  }

  @Test
  void findOrDefault_givenTestInterfaceProxy_withNullToStringStrategies() {
    TestInterface proxy =
        (TestInterface)
            Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                new Class<?>[] {TestInterface.class},
                new InvocationHandler() {

                  @Override
                  public Object invoke(Object proxy, Method method, Object[] args)
                      throws Throwable {
                    return null;
                  }
                });

    final DefaultToStringStrategyFactory factory =
        new DefaultToStringStrategyFactory(new ObjectToStringStrategy(), null);
    assertThat(factory.findOrDefault(proxy)).isExactlyInstanceOf(ObjectToStringStrategy.class);
  }

  @Test
  void findOrDefault_givenTestInterfaceProxy_withEmptyToStringStrategies() {
    TestInterface proxy =
        (TestInterface)
            Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                new Class<?>[] {TestInterface.class},
                new InvocationHandler() {

                  @Override
                  public Object invoke(Object proxy, Method method, Object[] args)
                      throws Throwable {
                    return null;
                  }
                });

    final DefaultToStringStrategyFactory factory =
        new DefaultToStringStrategyFactory(new ObjectToStringStrategy(), List.of());
    assertThat(factory.findOrDefault(proxy)).isExactlyInstanceOf(ObjectToStringStrategy.class);
  }
}
