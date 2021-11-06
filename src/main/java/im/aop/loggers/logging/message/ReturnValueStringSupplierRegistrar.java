package im.aop.loggers.logging.message;

import java.util.Arrays;
import java.util.function.Supplier;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Register String {@link Supplier} to {@link StringSupplierLookup} for returned value.
 *
 * @author Andy Lian
 */
public class ReturnValueStringSupplierRegistrar {

  private static final String RETURN_VALUE_KEY = "return-value";

  private static final String NO_RETURN_VALUE_STRING = "none";

  private static final String NULL_RETURN_VALUE_STRING = "null";

  public void register(
      StringSupplierLookup stringSupplierLookup, JoinPoint joinPoint, Object source) {
    stringSupplierLookup.addStringSupplier(
        RETURN_VALUE_KEY, () -> returnedValue(methodSignature(joinPoint), source));
  }

  private String returnedValue(final MethodSignature methodSignature, final Object returnValue) {
    if (void.class.equals(methodSignature.getReturnType())) {
      return NO_RETURN_VALUE_STRING;
    }

    if (returnValue == null) {
      return NULL_RETURN_VALUE_STRING;
    }
    if (returnValue.getClass().isArray()) {
      return Arrays.deepToString((Object[]) returnValue);
    }
    return returnValue.toString();
  }

  private MethodSignature methodSignature(final JoinPoint joinPoint) {
    return (MethodSignature) joinPoint.getSignature();
  }
}
