package im.aop.loggers.logging.message;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ExceptionStringSupplierRegistrar}.
 *
 * @author Andy Lian
 */
class ExceptionStringSupplierRegistrarTests {

  private static final ExceptionStringSupplierRegistrar REGISTRAR =
      new ExceptionStringSupplierRegistrar();

  @Test
  void exception() throws NoSuchMethodException, SecurityException {
    final StringSupplierLookup stringSupplierLookup = new StringSupplierLookup();
    REGISTRAR.register(stringSupplierLookup, new RuntimeException("foo"));
    assertThat(stringSupplierLookup.lookup("exception"))
        .isEqualTo("type=RuntimeException, message=foo");
  }
}
