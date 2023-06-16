package im.aop.loggers.messageinterpolation;

import java.lang.reflect.Method;
import java.util.function.Supplier;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Register String {@link Supplier} to {@link StringSupplierLookup} for {@link JoinPoint}
 * variables.
 *
 * @author Andy Lian
 */
public class JoinPointStringSupplierRegistrar implements StringSupplierRegistrar<JoinPoint> {

  private static final String METHOD_KEY = "method";

  private static final String PARAMETERS_KEY = "parameters";

  private static final String NO_PARAMETERS_STRING = "none";

  @Autowired
  private ToStringStrategyFactory toStringStrategyFactory;

  @Override
  public void register(StringSupplierLookup stringSupplierLookup, JoinPoint source) {
    stringSupplierLookup.addStringSupplier(
        METHOD_KEY, () -> method(methodSignature(source).getMethod()));
    stringSupplierLookup.addStringSupplier(PARAMETERS_KEY, () -> methodParameters(source));
  }

  private MethodSignature methodSignature(final JoinPoint joinPoint) {
    return (MethodSignature) joinPoint.getSignature();
  }

  private String method(final Method method) {
    return method.getReturnType().getSimpleName()
        + " "
        + method.getName()
        + "("
        + methodParameterTypes(method.getParameterTypes())
        + ")";
  }

  private String methodParameters(final JoinPoint joinPoint) {
    final MethodSignature methodSignature = methodSignature(joinPoint);
    final Method method = methodSignature.getMethod();
    final int parameterCount = method.getParameterCount() - 1;
    if (parameterCount == -1) {
      return NO_PARAMETERS_STRING;
    }

    final Object[] parameterValues = joinPoint.getArgs();
    final StringBuilder builder = new StringBuilder();
    for (int index = 0; ; index++) {
      builder.append(toString(parameterValues[index]));
      if (index == parameterCount) {
        return builder.toString();
      }
      builder.append(", ");
    }
  }

  private String toString(Object object) {
    return toStringStrategyFactory.findOrDefault(object).toString(object);
  }

  private String methodParameterTypes(final Class<?>[] parameterTypes) {
    final int parameterCount = parameterTypes.length - 1;
    if (parameterCount == -1) {
      return "";
    }

    final StringBuilder builder = new StringBuilder();
    for (int index = 0; ; index++) {
      builder.append(parameterTypes[index].getSimpleName());
      if (index == parameterCount) {
        return builder.toString();
      }
      builder.append(", ");
    }
  }
}
