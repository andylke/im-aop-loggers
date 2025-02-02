package im.aop.loggers.advice.afterthrowing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.messageinterpolation.StringSubstitutorConfiguration;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Tests for {@link LogAfterThrowingAdvice}.
 *
 * @author Andy Lian
 */
@ExtendWith(OutputCaptureExtension.class)
class LogAfterThrowingAdviceTests {

  private ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(
              StringSubstitutorConfiguration.class, LogAfterThrowingAdviceTestConfiguration.class)
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

  @Nested
  class MethodContextTests {

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
                assertThrows(
                    RuntimeException.class, () -> methodContext.methodWithParameter("foo"));
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

  @Nested
  class ClassContextTests {

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
  }

  @LogAfterThrowing
  static class ParentClassContext {

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

  static class ChildClassContext extends ParentClassContext {}

  @Nested
  class ChildClassContextTests {

    @Test
    void methodWithoutParameter_annotatedOnChildClass(final CapturedOutput capturedOutput) {
      runner
          .withBean(ChildClassContext.class)
          .run(
              context -> {
                final ChildClassContext classContext = context.getBean(ChildClassContext.class);
                assertThrows(RuntimeException.class, () -> classContext.methodWithoutParameter());
                assertThat(capturedOutput)
                    .contains(
                        "joinPoint=execution(void "
                            + ParentClassContext.class.getName()
                            + ".methodWithoutParameter())")
                    .contains("thrownException=" + RuntimeException.class.getName());
              });
    }

    @Test
    void methodWithParameter_annotatedOnChildClass(final CapturedOutput capturedOutput) {
      runner
          .withBean(ChildClassContext.class)
          .run(
              context -> {
                final ChildClassContext classContext = context.getBean(ChildClassContext.class);
                assertThrows(RuntimeException.class, () -> classContext.methodWithParameter("foo"));
                assertThat(capturedOutput)
                    .contains(
                        "joinPoint=execution(void "
                            + ParentClassContext.class.getName()
                            + ".methodWithParameter(String))")
                    .contains("thrownException=" + RuntimeException.class.getName());
              });
    }

    @Test
    void methodWithResult_annotatedOnChildClass(final CapturedOutput capturedOutput) {
      runner
          .withBean(ChildClassContext.class)
          .run(
              context -> {
                final ChildClassContext classContext = context.getBean(ChildClassContext.class);
                assertThrows(RuntimeException.class, () -> classContext.methodWithResult());
                assertThat(capturedOutput)
                    .contains(
                        "joinPoint=execution(String "
                            + ParentClassContext.class.getName()
                            + ".methodWithResult())")
                    .contains("thrownException=" + RuntimeException.class.getName());
              });
    }

    @Test
    void toString_annotatedOnChildClass(final CapturedOutput capturedOutput) {
      runner
          .withBean(ChildClassContext.class)
          .run(
              context -> {
                final ChildClassContext classContext = context.getBean(ChildClassContext.class);
                assertThrows(RuntimeException.class, () -> classContext.toString());
                assertThat(capturedOutput)
                    .doesNotContain(
                        "joinPoint=execution(String "
                            + ParentClassContext.class.getName()
                            + ".toString())")
                    .doesNotContain("thrownException=" + RuntimeException.class.getName());
              });
    }
  }
}
