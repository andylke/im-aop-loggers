package im.aop.loggers.messageinterpolation;

/**
 * {@link Object#toString()} strategy interface used by {@link StringSupplierRegistrar} to supply
 * String value.
 *
 * @author Andy Lian
 */
public interface ToStringStrategy {

  /**
   * Does not check for null instance.
   *
   * @param object
   * @return
   */
  boolean supports(Object object);

  String toString(Object object);
}
