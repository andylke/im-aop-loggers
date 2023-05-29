package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * Tests for {@link ElapsedTimeLimitStringSupplierRegistrar}.
 *
 * @author Andy Lian
 */
class ElapsedTimeLimitStringSupplierRegistrarTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner().withBean(ElapsedTimeLimitStringSupplierRegistrar.class);

  @Test
  void elapsedTimeLimit() throws NoSuchMethodException, SecurityException {
    runner.run(
        context -> {
          final ElapsedTimeLimitStringSupplierRegistrar registrar =
              context.getBean(ElapsedTimeLimitStringSupplierRegistrar.class);

          final StringSupplierLookup stringSupplierLookup = new StringSupplierLookup();
          registrar.register(stringSupplierLookup, Duration.ofSeconds(1));
          assertThat(stringSupplierLookup.lookup("elapsed-time-limit")).isEqualTo("PT1S");
        });
  }
}
