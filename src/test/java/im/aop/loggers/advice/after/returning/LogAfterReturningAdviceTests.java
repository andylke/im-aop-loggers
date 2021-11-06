package im.aop.loggers.advice.after.returning;

import static org.assertj.core.api.Assertions.assertThat;

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
 * Tests for {@link LogAfterReturningAdvice}.
 *
 * @author Andy Lian
 */
@ExtendWith(OutputCaptureExtension.class)
class LogAfterReturningAdviceTests {

  private ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(LogAfterReturningAdviceTestConfiguration.class)
          .withBean(LogAfterReturningAdvice.class)
          .withBean(AopLoggersProperties.class);

  @EnableAspectJAutoProxy
  @TestConfiguration(proxyBeanMethods = false)
  static class LogAfterReturningAdviceTestConfiguration {

    @Bean
    public LogAfterReturningService logAfterReturningService(
        final AopLoggersProperties aopLoggersProperties) {
      return new LogAfterReturningService(aopLoggersProperties) {

        @Override
        public void logAfterReturning(
            JoinPoint joinPoint, LogAfterReturning logAfterReturning, Object returnedValue) {
          LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType())
              .info("joinPoint={}, returnedValue={}", joinPoint, returnedValue);
        }
      };
    }
  }

  static class TestMethodContext {

    @LogAfterReturning
    public void methodWithoutParameter() {}

    @LogAfterReturning
    public void methodWithParameter(String foo) {}

    @LogAfterReturning
    public String methodWithResult() {
      return "foo";
    }

    @LogAfterReturning
    @Override
    public String toString() {
      return super.toString();
    }
  }

  @Test
  void methodWithoutParameter_annotatedOnMethod(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestMethodContext.class)
        .run(
            context -> {
              final TestMethodContext methodContext = context.getBean(TestMethodContext.class);
              methodContext.methodWithoutParameter();

              assertThat(capturedOutput)
                  .contains(
                      "joinPoint=execution(void "
                          + TestMethodContext.class.getName()
                          + ".methodWithoutParameter())")
                  .contains("returnedValue=null");
            });
  }

  @Test
  void methodWithParameter_annotatedOnMethod(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestMethodContext.class)
        .run(
            context -> {
              final TestMethodContext methodContext = context.getBean(TestMethodContext.class);
              methodContext.methodWithParameter("foo");

              assertThat(capturedOutput)
                  .contains(
                      "joinPoint=execution(void "
                          + TestMethodContext.class.getName()
                          + ".methodWithParameter(String))")
                  .contains("returnedValue=null");
            });
  }

  @Test
  void methodWithResult_annotatedOnMethod(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestMethodContext.class)
        .run(
            context -> {
              final TestMethodContext methodContext = context.getBean(TestMethodContext.class);
              methodContext.methodWithResult();

              assertThat(capturedOutput)
                  .contains(
                      "joinPoint=execution(String "
                          + TestMethodContext.class.getName()
                          + ".methodWithResult())")
                  .contains("returnedValue=foo");
            });
  }

  @Test
  void toString_annotatedOnMethod(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestMethodContext.class)
        .run(
            context -> {
              final TestMethodContext methodContext = context.getBean(TestMethodContext.class);
              methodContext.toString();

              assertThat(capturedOutput)
                  .contains(
                      "joinPoint=execution(String "
                          + TestMethodContext.class.getName()
                          + ".toString())")
                  .contains("returnedValue=" + TestMethodContext.class.getName());
            });
  }

  @LogAfterReturning
  static class TestClassContext {

    public void methodWithoutParameter() {}

    public void methodWithParameter(String foo) {}

    public String methodWithResult() {
      return "foo";
    }

    @Override
    public String toString() {
      return super.toString();
    }
  }

  @Test
  void methodWithoutParameter_annotatedOnClass(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestClassContext.class)
        .run(
            context -> {
              final TestClassContext classContext = context.getBean(TestClassContext.class);
              classContext.methodWithoutParameter();

              assertThat(capturedOutput)
                  .contains(
                      "joinPoint=execution(void "
                          + TestClassContext.class.getName()
                          + ".methodWithoutParameter())")
                  .contains("returnedValue=null");
            });
  }

  @Test
  void methodWithParameter_annotatedOnClass(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestClassContext.class)
        .run(
            context -> {
              final TestClassContext classContext = context.getBean(TestClassContext.class);
              classContext.methodWithParameter("foo");

              assertThat(capturedOutput)
                  .contains(
                      "joinPoint=execution(void "
                          + TestClassContext.class.getName()
                          + ".methodWithParameter(String))")
                  .contains("returnedValue=null");
            });
  }

  @Test
  void methodWithResult_annotatedOnClass(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestClassContext.class)
        .run(
            context -> {
              final TestClassContext classContext = context.getBean(TestClassContext.class);
              classContext.methodWithResult();

              assertThat(capturedOutput)
                  .contains(
                      "joinPoint=execution(String "
                          + TestClassContext.class.getName()
                          + ".methodWithResult())")
                  .contains("returnedValue=foo");
            });
  }

  @Test
  void toString_annotatedOnClass(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestClassContext.class)
        .run(
            context -> {
              final TestClassContext classContext = context.getBean(TestClassContext.class);
              classContext.toString();

              assertThat(capturedOutput)
                  .doesNotContain(
                      "joinPoint=execution(String "
                          + TestClassContext.class.getName()
                          + ".toString())")
                  .doesNotContain("returnedValue=" + TestClassContext.class.getName());
            });
  }

  @Test
  void publicMethod_fulfillCoverageRatio() {
    runner.run(
        (context) -> {
          final LogAfterReturningAdvice logAfterReturningAdvice =
              context.getBean(LogAfterReturningAdvice.class);
          logAfterReturningAdvice.publicMethod();
        });
  }

  @Test
  void toStringMethod_fulfillCoverageRatio() {
    runner.run(
        (context) -> {
          final LogAfterReturningAdvice logAfterReturningAdvice =
              context.getBean(LogAfterReturningAdvice.class);
          logAfterReturningAdvice.toStringMethod();
        });
  }

  @Test
  void logAfterReturningMethodContext_fulfillCoverageRatio() {
    runner.run(
        (context) -> {
          final LogAfterReturningAdvice logAfterReturningAdvice =
              context.getBean(LogAfterReturningAdvice.class);
          logAfterReturningAdvice.logAfterReturningMethodContext(null);
        });
  }

  @Test
  void logAfterReturningClassContext_fulfillCoverageRatio() {
    runner.run(
        (context) -> {
          final LogAfterReturningAdvice logAfterReturningAdvice =
              context.getBean(LogAfterReturningAdvice.class);
          logAfterReturningAdvice.logAfterReturningClassContext(null);
        });
  }
}
