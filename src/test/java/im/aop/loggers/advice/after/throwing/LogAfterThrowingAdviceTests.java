package im.aop.loggers.advice.after.throwing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import im.aop.loggers.AopLoggersProperties;

/**
 * Tests for {@link LogAfterThrowingAdvice}.
 *
 * @author Andy Lian
 */
@ExtendWith(OutputCaptureExtension.class)
class LogAfterThrowingAdviceTests {

  private ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(LogAfterThrowingAdviceTestConfiguration.class)
          .withBean(LogAfterThrowingAdvice.class)
          .withBean(AopLoggersProperties.class);

  @EnableAspectJAutoProxy
  @TestConfiguration(proxyBeanMethods = false)
  static class LogAfterThrowingAdviceTestConfiguration {

    @Bean
    public LogAfterThrowingService LogAfterThrowingService(
        final AopLoggersProperties aopLoggersProperties) {
      return new LogAfterThrowingService(aopLoggersProperties) {

        @Override
        public void logAfterThrowing(
            JoinPoint joinPoint, LogAfterThrowing logAfterThrowing, Throwable thrownException) {
          LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType())
              .info(
                  "joinPoint={}, thrownException={}",
                  joinPoint,
                  thrownException.getClass().getName());
        }
      };
    }
  }

  static class TestMethodContext {

    @LogAfterThrowing
    public void methodWithoutParameter() {
      throw new RuntimeException();
    }

    @LogAfterThrowing
    public void methodWithParameter(String foo) {
      throw new RuntimeException();
    }

    @LogAfterThrowing
    public String methodWithResult() {
      throw new RuntimeException();
    }

    @LogAfterThrowing
    @Override
    public String toString() {
      throw new RuntimeException();
    }
  }

  @Test
  void methodWithoutParameter_annotatedOnMethod(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestMethodContext.class)
        .run(
            context -> {
              final TestMethodContext methodContext = context.getBean(TestMethodContext.class);

              assertThrows(RuntimeException.class, () -> methodContext.methodWithoutParameter());
              assertThat(capturedOutput)
                  .contains(
                      "joinPoint=execution(void "
                          + TestMethodContext.class.getName()
                          + ".methodWithoutParameter())")
                  .contains("thrownException=" + RuntimeException.class.getName());
            });
  }

  @Test
  void methodWithParameter_annotatedOnMethod(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestMethodContext.class)
        .run(
            context -> {
              final TestMethodContext methodContext = context.getBean(TestMethodContext.class);
              assertThrows(RuntimeException.class, () -> methodContext.methodWithParameter("foo"));
              assertThat(capturedOutput)
                  .contains(
                      "joinPoint=execution(void "
                          + TestMethodContext.class.getName()
                          + ".methodWithParameter(String))")
                  .contains("thrownException=" + RuntimeException.class.getName());
            });
  }

  @Test
  void methodWithResult_annotatedOnMethod(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestMethodContext.class)
        .run(
            context -> {
              final TestMethodContext methodContext = context.getBean(TestMethodContext.class);
              assertThrows(RuntimeException.class, () -> methodContext.methodWithResult());
              assertThat(capturedOutput)
                  .contains(
                      "joinPoint=execution(String "
                          + TestMethodContext.class.getName()
                          + ".methodWithResult())")
                  .contains("thrownException=" + RuntimeException.class.getName());
            });
  }

  @Test
  void toString_annotatedOnMethod(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestMethodContext.class)
        .run(
            context -> {
              final TestMethodContext methodContext = context.getBean(TestMethodContext.class);
              assertThrows(RuntimeException.class, () -> methodContext.toString());
              assertThat(capturedOutput)
                  .contains(
                      "joinPoint=execution(String "
                          + TestMethodContext.class.getName()
                          + ".toString())")
                  .contains("thrownException=" + RuntimeException.class.getName());
            });
  }

  @LogAfterThrowing
  static class TestClassContext {

    public void methodWithoutParameter() {
      throw new RuntimeException();
    }

    public void methodWithParameter(String foo) {
      throw new RuntimeException();
    }

    public String methodWithResult() {
      throw new RuntimeException();
    }

    @Override
    public String toString() {
      throw new RuntimeException();
    }
  }

  @Test
  void methodWithoutParameter_annotatedOnClass(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestClassContext.class)
        .run(
            context -> {
              final TestClassContext classContext = context.getBean(TestClassContext.class);
              assertThrows(RuntimeException.class, () -> classContext.methodWithoutParameter());
              assertThat(capturedOutput)
                  .contains(
                      "joinPoint=execution(void "
                          + TestClassContext.class.getName()
                          + ".methodWithoutParameter())")
                  .contains("thrownException=" + RuntimeException.class.getName());
            });
  }

  @Test
  void methodWithParameter_annotatedOnClass(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestClassContext.class)
        .run(
            context -> {
              final TestClassContext classContext = context.getBean(TestClassContext.class);
              assertThrows(RuntimeException.class, () -> classContext.methodWithParameter("foo"));
              assertThat(capturedOutput)
                  .contains(
                      "joinPoint=execution(void "
                          + TestClassContext.class.getName()
                          + ".methodWithParameter(String))")
                  .contains("thrownException=" + RuntimeException.class.getName());
            });
  }

  @Test
  void methodWithResult_annotatedOnClass(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestClassContext.class)
        .run(
            context -> {
              final TestClassContext classContext = context.getBean(TestClassContext.class);
              assertThrows(RuntimeException.class, () -> classContext.methodWithResult());
              assertThat(capturedOutput)
                  .contains(
                      "joinPoint=execution(String "
                          + TestClassContext.class.getName()
                          + ".methodWithResult())")
                  .contains("thrownException=" + RuntimeException.class.getName());
            });
  }

  @Test
  void toString_annotatedOnClass(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestClassContext.class)
        .run(
            context -> {
              final TestClassContext classContext = context.getBean(TestClassContext.class);
              assertThrows(RuntimeException.class, () -> classContext.toString());
              assertThat(capturedOutput)
                  .doesNotContain(
                      "joinPoint=execution(String "
                          + TestClassContext.class.getName()
                          + ".toString())")
                  .doesNotContain("thrownException=" + RuntimeException.class.getName());
            });
  }

  @Test
  void publicMethod_fulfillCoverageRatio() {
    runner.run(
        (context) -> {
          final LogAfterThrowingAdvice logAfterThrowingAdvice =
              context.getBean(LogAfterThrowingAdvice.class);
          logAfterThrowingAdvice.publicMethod();
        });
  }

  @Test
  void toStringMethod_fulfillCoverageRatio() {
    runner.run(
        (context) -> {
          final LogAfterThrowingAdvice logAfterThrowingAdvice =
              context.getBean(LogAfterThrowingAdvice.class);
          logAfterThrowingAdvice.toStringMethod();
        });
  }

  @Test
  void logAfterThrowingMethodContext_fulfillCoverageRatio() {
    runner.run(
        (context) -> {
          final LogAfterThrowingAdvice logAfterThrowingAdvice =
              context.getBean(LogAfterThrowingAdvice.class);
          logAfterThrowingAdvice.logAfterThrowingMethodContext(null);
        });
  }

  @Test
  void logAfterThrowingClassContext_fulfillCoverageRatio() {
    runner.run(
        (context) -> {
          final LogAfterThrowingAdvice logAfterThrowingAdvice =
              context.getBean(LogAfterThrowingAdvice.class);
          logAfterThrowingAdvice.logAfterThrowingClassContext(null);
        });
  }
}
