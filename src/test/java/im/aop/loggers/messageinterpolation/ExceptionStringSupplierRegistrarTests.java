package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * Tests for {@link ExceptionStringSupplierRegistrar}.
 *
 * @author Andy Lian
 */
class ExceptionStringSupplierRegistrarTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner().withBean(ExceptionStringSupplierRegistrar.class);

  @Test
  void exception() throws SecurityException {
    runner.run(
        context -> {
          final ExceptionStringSupplierRegistrar registrar =
              context.getBean(ExceptionStringSupplierRegistrar.class);

          final StringSupplierLookup stringSupplierLookup = new StringSupplierLookup();
          registrar.register(stringSupplierLookup, new RuntimeException("foo"));
          assertThat(stringSupplierLookup.lookup("exception"))
              .isEqualTo("type=RuntimeException, message=foo");
        });
  }
}
