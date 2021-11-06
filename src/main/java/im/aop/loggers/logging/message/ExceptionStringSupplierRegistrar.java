package im.aop.loggers.logging.message;

public class ExceptionStringSupplierRegistrar implements StringSupplierRegistrar<Throwable> {

  private static final String EXCEPTION_KEY = "exception";

  @Override
  public void register(StringSupplierLookup stringSupplierLookup, Throwable source) {
    stringSupplierLookup.addStringSupplier(EXCEPTION_KEY, () -> exception(source));
  }

  private String exception(final Throwable exception) {
    return "type=" + exception.getClass().getSimpleName() + ", message=" + exception.getMessage();
  }
}
