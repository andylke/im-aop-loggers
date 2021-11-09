package im.aop.loggers.messageinterpolation;

import java.util.function.Supplier;

/**
 * Strategy Interface for implementation to register String {@link Supplier} to {@link
 * StringSupplierLookup}.
 *
 * @author Andy Lian
 */
public interface StringSupplierRegistrar<T> {

  void register(StringSupplierLookup stringSupplierLookup, T source);
}
