package im.aop.loggers.logging.message;

import java.time.Duration;

public class ElapsedTimeLimitStringSupplierRegistrar implements StringSupplierRegistrar<Duration> {

  private static final String ELAPSED_TIME_LIMIT_KEY = "elapsed-time-limit";

  @Override
  public void register(StringSupplierLookup stringSupplierLookup, Duration source) {
    stringSupplierLookup.addStringSupplier(ELAPSED_TIME_LIMIT_KEY, () -> elapsedTimeLimit(source));
  }

  private String elapsedTimeLimit(Duration elapsedTimeLimit) {
    return elapsedTimeLimit.toString();
  }
}
