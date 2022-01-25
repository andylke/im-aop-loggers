package im.aop.loggers.messageinterpolation;

/**
 * {@link Object#toString()} strategy interface used by {@link StringSupplierRegistrar} to supply
 * String value.
 *
 * @author Andy Lian
 */
public interface ToStringStrategy {

  boolean supports(Class<?> type);

  String toString(Object object);
}
