package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    public boolean canSupport(Class<?> type) {
      return type == TestClass.class;
    }

    @Override
    public String toString(Object object) {
      throw new IllegalStateException();
    }
  }

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
}
