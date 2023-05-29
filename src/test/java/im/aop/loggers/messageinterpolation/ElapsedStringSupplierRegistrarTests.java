package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * Tests for {@link ElapsedStringSupplierRegistrar}.
 *
 * @author Andy Lian
 */
class ElapsedStringSupplierRegistrarTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner().withBean(ElapsedStringSupplierRegistrar.class);

  @Test
  void elapsed() throws NoSuchMethodException, SecurityException {
    runner.run(
        context -> {
          final ElapsedStringSupplierRegistrar registrar =
              context.getBean(ElapsedStringSupplierRegistrar.class);

          final StringSupplierLookup stringSupplierLookup = new StringSupplierLookup();
          registrar.register(stringSupplierLookup, Duration.ofSeconds(1).toNanos());
          assertThat(stringSupplierLookup.lookup("elapsed")).isEqualTo("PT1S");
        });
  }
}
