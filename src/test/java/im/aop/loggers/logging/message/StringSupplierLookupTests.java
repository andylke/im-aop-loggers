package im.aop.loggers.logging.message;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link StringSupplierLookup}.
 *
 * @author Andy Lian
 */
class StringSupplierLookupTests {

  @Test
  void lookup_withVariable() {
    final StringSupplierLookup stringSupplierLookup = new StringSupplierLookup();
    stringSupplierLookup.addStringSupplier("foo", () -> "bar");
    assertThat(stringSupplierLookup.lookup("foo")).isEqualTo("bar");
  }

  @Test
  void lookup_withoutVariable() {
    final StringSupplierLookup stringSupplierLookup = new StringSupplierLookup();
    assertThat(stringSupplierLookup.lookup("foo")).isNull();
  }
}
