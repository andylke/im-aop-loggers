package im.aop.loggers.advice.before;

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
 * Tests for {@link LogBeforeAdvice}.
 *
 * @author Andy Lian
 */
@ExtendWith(OutputCaptureExtension.class)
class LogBeforeAdviceTests {

  private ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(LogBeforeAdviceTestConfiguration.class)
          .withBean(LogBeforeAdvice.class)
          .withBean(AopLoggersProperties.class);

  @EnableAspectJAutoProxy
  @TestConfiguration(proxyBeanMethods = false)
  static class LogBeforeAdviceTestConfiguration {

    @Bean
    public LogBeforeService logBeforeService(final AopLoggersProperties aopLoggersProperties) {
      return new LogBeforeService(aopLoggersProperties) {
        @Override
        public void logBefore(JoinPoint joinPoint, LogBefore logBefore) {
          LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType())
              .info("{}", joinPoint);
        }
      };
    }
  }

  static class TestMethodContext {

    @LogBefore
    public void methodWithoutParameter() {}

    @LogBefore
    public void methodWithParameter(String foo) {}

    @LogBefore
    public String methodWithResult() {
      return "foo";
    }

    @LogBefore
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
                      "execution(void "
                          + TestMethodContext.class.getName()
                          + ".methodWithoutParameter())");
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
                      "execution(void "
                          + TestMethodContext.class.getName()
                          + ".methodWithParameter(String))");
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
                      "execution(String "
                          + TestMethodContext.class.getName()
                          + ".methodWithResult())");
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
                      "execution(String " + TestMethodContext.class.getName() + ".toString())");
            });
  }

  @LogBefore
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
                      "execution(void "
                          + TestClassContext.class.getName()
                          + ".methodWithoutParameter())");
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
                      "execution(void "
                          + TestClassContext.class.getName()
                          + ".methodWithParameter(String))");
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
                      "execution(String "
                          + TestClassContext.class.getName()
                          + ".methodWithResult())");
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
                      "execution(String " + TestClassContext.class.getName() + ".toString())");
            });
  }

  @Test
  void publicMethod_fulfillCoverageRatio() {
    runner.run(
        (context) -> {
          final LogBeforeAdvice logBeforeAdvice = context.getBean(LogBeforeAdvice.class);
          logBeforeAdvice.publicMethod();
        });
  }

  @Test
  void toStringMethod_fulfillCoverageRatio() {
    runner.run(
        (context) -> {
          final LogBeforeAdvice logBeforeAdvice = context.getBean(LogBeforeAdvice.class);
          logBeforeAdvice.toStringMethod();
        });
  }

  @Test
  void logBeforeMethodContext_fulfillCoverageRatio() {
    runner.run(
        (context) -> {
          final LogBeforeAdvice logBeforeAdvice = context.getBean(LogBeforeAdvice.class);
          logBeforeAdvice.logBeforeMethodContext(null);
        });
  }

  @Test
  void logBeforeClassContext_fulfillCoverageRatio() {
    runner.run(
        (context) -> {
          final LogBeforeAdvice logBeforeAdvice = context.getBean(LogBeforeAdvice.class);
          logBeforeAdvice.logBeforeClassContext(null);
        });
  }
}
