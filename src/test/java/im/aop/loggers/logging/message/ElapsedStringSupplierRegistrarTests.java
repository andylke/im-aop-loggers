package im.aop.loggers.logging.message;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ElapsedStringSupplierRegistrar}.
 *
 * @author Andy Lian
 */
class ElapsedStringSupplierRegistrarTests {

  private static final ElapsedStringSupplierRegistrar REGISTRAR =
      new ElapsedStringSupplierRegistrar();

  @Test
  void elapsed() throws NoSuchMethodException, SecurityException {
    final StringSupplierLookup stringSupplierLookup = new StringSupplierLookup();
    REGISTRAR.register(stringSupplierLookup, Duration.ofSeconds(1).toNanos());
    assertThat(stringSupplierLookup.lookup("elapsed")).isEqualTo("PT1S");
  }
}
