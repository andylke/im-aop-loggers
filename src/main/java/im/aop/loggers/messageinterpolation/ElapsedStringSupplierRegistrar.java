package im.aop.loggers.messageinterpolation;

import java.time.Duration;

public class ElapsedStringSupplierRegistrar implements StringSupplierRegistrar<Long> {

  private static final String ELAPSED_KEY = "elapsed";

  @Override
  public void register(StringSupplierLookup stringSupplierLookup, Long source) {
    stringSupplierLookup.addStringSupplier(ELAPSED_KEY, () -> elapsed(source));
  }

  private String elapsed(long elapsed) {
    return Duration.ofNanos(elapsed).toString();
  }
}
