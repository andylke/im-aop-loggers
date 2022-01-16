package im.aop.loggers.messageinterpolation;

import java.util.function.Supplier;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Register String {@link Supplier} to {@link StringSupplierLookup} for returned value.
 *
 * @author Andy Lian
 */
public class ReturnValueStringSupplierRegistrar {

  private static final String RETURN_VALUE_KEY = "return-value";

  private static final String NO_RETURN_VALUE_STRING = "none";

  @Autowired private ToStringStrategyFactory toStringStrategyFactory;

  public void register(
      StringSupplierLookup stringSupplierLookup, JoinPoint joinPoint, Object source) {
    stringSupplierLookup.addStringSupplier(
        RETURN_VALUE_KEY, () -> returnedValue(methodSignature(joinPoint), source));
  }

  private String returnedValue(final MethodSignature methodSignature, final Object returnValue) {
    if (void.class.equals(methodSignature.getReturnType())) {
      return NO_RETURN_VALUE_STRING;
    }

    return toString(returnValue);
  }

  private String toString(Object object) {
    return toStringStrategyFactory.findOrDefault(object).toString(object);
  }

  private MethodSignature methodSignature(final JoinPoint joinPoint) {
    return (MethodSignature) joinPoint.getSignature();
  }
}
