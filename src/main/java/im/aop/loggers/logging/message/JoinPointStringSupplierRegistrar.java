package im.aop.loggers.logging.message;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Supplier;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Register String {@link Supplier} to {@link StringSupplierLookup} for {@link JoinPoint} variables.
 *
 * @author Andy Lian
 */
public class JoinPointStringSupplierRegistrar implements StringSupplierRegistrar<JoinPoint> {

  private static final String METHOD_KEY = "method";

  private static final String PARAMETERS_KEY = "parameters";

  private static final String NO_PARAMETERS_STRING = "none";

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
    if (method.getParameterCount() == 0) {
      return NO_PARAMETERS_STRING;
    }

    final String[] parameterNames = methodSignature.getParameterNames();
    final Object[] parameterValues = joinPoint.getArgs();

    final String[] parameterStrings = new String[parameterNames.length];
    for (int index = 0; index < parameterNames.length; index++) {
      parameterStrings[index] =
          parameterNames[index] + "=" + Objects.toString(parameterValues[index]);
    }
    return String.join(", ", parameterStrings);
  }

  private String methodParameterTypes(final Class<?>[] parameterTypes) {
    final String[] parameterTypeStrings = new String[parameterTypes.length];
    for (int index = 0; index < parameterTypes.length; index++) {
      parameterTypeStrings[index] = parameterTypes[index].getSimpleName();
    }
    return String.join(", ", parameterTypeStrings);
  }
}
