package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * Tests for {@link JoinPointStringSupplierRegistrar}.
 *
 * @author Andy Lian
 */
class JoinPointStringSupplierRegistrarTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(ToStringStrategyConfiguration.class)
          .withBean(JoinPointStringSupplierRegistrar.class);

  static class TestClass {

    public void methodWithoutParameter() {}

    public void methodWithOneParameter(String foo) {}

    public void methodWithTwoParameter(String foo, String bar) {}

    public String methodWithResult() {
      return "foo";
    }
  }

  @Test
  void method_methodWithoutParameter() throws SecurityException {
    runner.run(
        context -> {
          final JoinPointStringSupplierRegistrar registrar =
              context.getBean(JoinPointStringSupplierRegistrar.class);

          final MethodSignature methodSignature =
              mockMethodSignature(TestClass.class, "methodWithoutParameter", null, null);
          final JoinPoint joinPoint = mockJoinPoint(methodSignature, null);

          final StringSupplierLookup stringSupplierLookup = new StringSupplierLookup();
          registrar.register(stringSupplierLookup, joinPoint);
          assertThat(stringSupplierLookup.lookup("method"))
              .isEqualTo("void methodWithoutParameter()");
        });
  }

  @Test
  void method_methodWithOneParameter() throws SecurityException {
    runner.run(
        context -> {
          final JoinPointStringSupplierRegistrar registrar =
              context.getBean(JoinPointStringSupplierRegistrar.class);

          final MethodSignature methodSignature =
              mockMethodSignature(
                  TestClass.class, "methodWithOneParameter", Arrays.array(String.class), null);
          final JoinPoint joinPoint = mockJoinPoint(methodSignature, null);

          final StringSupplierLookup stringSupplierLookup = new StringSupplierLookup();
          registrar.register(stringSupplierLookup, joinPoint);
          assertThat(stringSupplierLookup.lookup("method"))
              .isEqualTo("void methodWithOneParameter(String)");
        });
  }

  @Test
  void method_methodWithTwoParameter() throws SecurityException {
    runner.run(
        context -> {
          final JoinPointStringSupplierRegistrar registrar =
              context.getBean(JoinPointStringSupplierRegistrar.class);

          final MethodSignature methodSignature =
              mockMethodSignature(
                  TestClass.class,
                  "methodWithTwoParameter",
                  Arrays.array(String.class, String.class),
                  null);
          final JoinPoint joinPoint = mockJoinPoint(methodSignature, null);

          final StringSupplierLookup stringSupplierLookup = new StringSupplierLookup();
          registrar.register(stringSupplierLookup, joinPoint);
          assertThat(stringSupplierLookup.lookup("method"))
              .isEqualTo("void methodWithTwoParameter(String, String)");
        });
  }

  @Test
  void method_methodWithResult() throws SecurityException {
    runner.run(
        context -> {
          final JoinPointStringSupplierRegistrar registrar =
              context.getBean(JoinPointStringSupplierRegistrar.class);

          final MethodSignature methodSignature =
              mockMethodSignature(TestClass.class, "methodWithResult", null, null);
          final JoinPoint joinPoint = mockJoinPoint(methodSignature, null);

          final StringSupplierLookup stringSupplierLookup = new StringSupplierLookup();
          registrar.register(stringSupplierLookup, joinPoint);
          assertThat(stringSupplierLookup.lookup("method")).isEqualTo("String methodWithResult()");
        });
  }

  @Test
  void methodParameters_withoutParameter() throws SecurityException {
    runner.run(
        context -> {
          final JoinPointStringSupplierRegistrar registrar =
              context.getBean(JoinPointStringSupplierRegistrar.class);

          final MethodSignature methodSignature =
              mockMethodSignature(TestClass.class, "methodWithoutParameter", null, null);
          final JoinPoint joinPoint = mockJoinPoint(methodSignature, null);

          final StringSupplierLookup stringSupplierLookup = new StringSupplierLookup();
          registrar.register(stringSupplierLookup, joinPoint);
          assertThat(stringSupplierLookup.lookup("parameters")).isEqualTo("none");
        });
  }

  @Test
  void methodParameters_methodWithOneParameter() throws SecurityException {
    runner.run(
        context -> {
          final JoinPointStringSupplierRegistrar registrar =
              context.getBean(JoinPointStringSupplierRegistrar.class);

          final MethodSignature methodSignature =
              mockMethodSignature(
                  TestClass.class,
                  "methodWithOneParameter",
                  Arrays.array(String.class),
                  Arrays.array("foo"));
          final JoinPoint joinPoint = mockJoinPoint(methodSignature, Arrays.array("a"));

          final StringSupplierLookup stringSupplierLookup = new StringSupplierLookup();
          registrar.register(stringSupplierLookup, joinPoint);
          assertThat(stringSupplierLookup.lookup("parameters")).isEqualTo("a");
        });
  }

  @Test
  void methodParameters_methodWithTwoParameter() throws SecurityException {
    runner.run(
        context -> {
          final JoinPointStringSupplierRegistrar registrar =
              context.getBean(JoinPointStringSupplierRegistrar.class);

          final MethodSignature methodSignature =
              mockMethodSignature(
                  TestClass.class,
                  "methodWithTwoParameter",
                  Arrays.array(String.class, String.class),
                  null);
          final JoinPoint joinPoint = mockJoinPoint(methodSignature, Arrays.array("a", "b"));

          when(methodSignature.getParameterNames()).thenReturn(new String[] {"foo", "bar"});

          final StringSupplierLookup stringSupplierLookup = new StringSupplierLookup();
          registrar.register(stringSupplierLookup, joinPoint);
          assertThat(stringSupplierLookup.lookup("parameters")).isEqualTo("a, b");
        });
  }

  private MethodSignature mockMethodSignature(
      final Class<?> declaringClass,
      final String methodName,
      final Class<?>[] methodParameterTypes,
      final String[] methodParameterNames)
      throws NoSuchMethodException {
    final MethodSignature methodSignature = mock(MethodSignature.class);

    when(methodSignature.getDeclaringType()).thenReturn(declaringClass);
    when(methodSignature.getMethod())
        .thenReturn(declaringClass.getDeclaredMethod(methodName, methodParameterTypes));
    when(methodSignature.getParameterNames()).thenReturn(methodParameterNames);

    return methodSignature;
  }

  private ProceedingJoinPoint mockJoinPoint(
      final MethodSignature methodSignature, final Object[] args) {
    final ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);

    when(joinPoint.getSignature()).thenReturn(methodSignature);
    when(joinPoint.getArgs()).thenReturn(args);
    return joinPoint;
  }
}
