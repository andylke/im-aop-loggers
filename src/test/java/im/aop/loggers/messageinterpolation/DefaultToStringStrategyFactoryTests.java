package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;

/**
 * Tests for {@link DefaultToStringStrategyFactory}.
 *
 * @author Andy Lian
 */
@ExtendWith(MockitoExtension.class)
class DefaultToStringStrategyFactoryTests {

  @Mock private ObjectToStringStrategy objectToStringStrategy;

  @Mock private ObjectProvider<ToStringStrategy> toStringStrategiesProvider;

  @InjectMocks private DefaultToStringStrategyFactory factory;

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
  void findOrDefault_givenNull_withEmptyToStringStrategies() {
    when(toStringStrategiesProvider.stream()).thenReturn(Stream.empty());
    factory.postConstruct();

    assertThat(factory.findOrDefault(null)).isInstanceOf(ObjectToStringStrategy.class);
  }

  @Test
  void findOrDefault_givenTestClass_withEmptyToStringStrategies() {
    when(toStringStrategiesProvider.stream()).thenReturn(Stream.empty());
    factory.postConstruct();

    assertThat(factory.findOrDefault(new TestClass())).isInstanceOf(ObjectToStringStrategy.class);
  }

  @Test
  void findOrDefault_givenTestClass_WithTestToStringStrategy() {
    when(toStringStrategiesProvider.stream()).thenReturn(Stream.of(new TestToStringStrategy()));
    factory.postConstruct();

    assertThat(factory.findOrDefault(new TestClass()))
        .isExactlyInstanceOf(TestToStringStrategy.class);
  }

  @Test
  void findOrDefault_givenTestInterfaceProxy_withEmptyToStringStrategies() {
    when(toStringStrategiesProvider.stream()).thenReturn(Stream.empty());
    factory.postConstruct();

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

    assertThat(factory.findOrDefault(proxy)).isInstanceOf(ObjectToStringStrategy.class);
  }
}
