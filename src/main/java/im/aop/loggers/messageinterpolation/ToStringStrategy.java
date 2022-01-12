package im.aop.loggers.messageinterpolation;

/**
 * {@link Object#toString()} strategy interface used by {@link StringSupplierRegistrar} to supply
 * String value.
 *
 * @author Andy Lian
 */
public interface ToStringStrategy {

  String toString(Object object);
}
