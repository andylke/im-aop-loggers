package im.aop.loggers.logging.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ReturnValueStringSupplierRegistrar}.
 *
 * @author Andy Lian
 */
class ReturnValueStringSupplierRegistrarTests {

  private static final ReturnValueStringSupplierRegistrar REGISTRAR =
      new ReturnValueStringSupplierRegistrar();

  @Test
  void returnValue_nonNull() throws NoSuchMethodException, SecurityException {
    final JoinPoint joinPoint = mockJoinPoint(mockMethodSignature(String.class));

    final StringSupplierLookup stringSupplierLookup = new StringSupplierLookup();
    REGISTRAR.register(stringSupplierLookup, joinPoint, "foo");
    assertThat(stringSupplierLookup.lookup("return-value")).isEqualTo("foo");
  }

  @Test
  void returnValue_null() throws NoSuchMethodException, SecurityException {
    final JoinPoint joinPoint = mockJoinPoint(mockMethodSignature(String.class));

    final StringSupplierLookup stringSupplierLookup = new StringSupplierLookup();
    REGISTRAR.register(stringSupplierLookup, joinPoint, null);
    assertThat(stringSupplierLookup.lookup("return-value")).isEqualTo("null");
  }

  @Test
  void returnValue_none() throws NoSuchMethodException, SecurityException {
    final JoinPoint joinPoint = mockJoinPoint(mockMethodSignature(void.class));

    final StringSupplierLookup stringSupplierLookup = new StringSupplierLookup();
    REGISTRAR.register(stringSupplierLookup, joinPoint, null);
    assertThat(stringSupplierLookup.lookup("return-value")).isEqualTo("none");
  }

  @Test
  void returnValue_stringArray() throws NoSuchMethodException, SecurityException {
    final JoinPoint joinPoint = mockJoinPoint(mockMethodSignature(String[].class));

    final StringSupplierLookup stringSupplierLookup = new StringSupplierLookup();
    REGISTRAR.register(stringSupplierLookup, joinPoint, new String[] {"foo", "bar"});
    assertThat(stringSupplierLookup.lookup("return-value")).isEqualTo("[foo, bar]");
  }

  private MethodSignature mockMethodSignature(final Class<?> returnType)
      throws NoSuchMethodException {
    final MethodSignature methodSignature = mock(MethodSignature.class);

    when(methodSignature.getReturnType()).thenReturn(returnType);

    return methodSignature;
  }

  private JoinPoint mockJoinPoint(final MethodSignature methodSignature) {
    final JoinPoint joinPoint = mock(JoinPoint.class);

    when(joinPoint.getSignature()).thenReturn(methodSignature);
    return joinPoint;
  }
}
