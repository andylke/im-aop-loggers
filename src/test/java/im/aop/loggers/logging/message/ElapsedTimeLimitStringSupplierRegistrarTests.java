package im.aop.loggers.logging.message;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ElapsedTimeLimitStringSupplierRegistrar}.
 *
 * @author Andy Lian
 */
class ElapsedTimeLimitStringSupplierRegistrarTests {

  private static final ElapsedTimeLimitStringSupplierRegistrar REGISTRAR =
      new ElapsedTimeLimitStringSupplierRegistrar();

  @Test
  void elapsedTimeLimit() throws NoSuchMethodException, SecurityException {
    final StringSupplierLookup stringSupplierLookup = new StringSupplierLookup();
    REGISTRAR.register(stringSupplierLookup, Duration.ofSeconds(1));
    assertThat(stringSupplierLookup.lookup("elapsed-time-limit")).isEqualTo("PT1S");
  }
}
