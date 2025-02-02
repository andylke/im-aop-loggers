package im.aop.loggers.advice.beforecommit;

import static org.assertj.core.api.Assertions.assertThat;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.messageinterpolation.StringSubstitutorConfiguration;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Tests for {@link LogBeforeCommitAdvice}.
 *
 * @author Andy Lian
 */
@ExtendWith(OutputCaptureExtension.class)
class LogBeforeCommitAdviceTests {

  private ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(
              StringSubstitutorConfiguration.class, LogBeforeCommitAdviceTestConfiguration.class)
          .withBean(LogBeforeCommitAdvice.class)
          .withBean(AopLoggersProperties.class);

  @EnableAspectJAutoProxy
  @TestConfiguration(proxyBeanMethods = false)
  static class LogBeforeCommitAdviceTestConfiguration {

    @Bean
    public LogBeforeCommitService logBeforeCommitService(
        final AopLoggersProperties aopLoggersProperties) {
      return new LogBeforeCommitService(aopLoggersProperties) {
        @Override
        public void logBeforeCommit(JoinPoint joinPoint, LogBeforeCommit logBeforeCommit) {
          LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType())
              .info("{}", joinPoint);
        }
      };
    }
  }

  static class TestMethodContext {

    @LogBeforeCommit
    public void methodWithoutParameter() {}

    @LogBeforeCommit
    public void methodWithParameter(String foo) {}

    @LogBeforeCommit
    public String methodWithResult() {
      return "foo";
    }

    @LogBeforeCommit
    @Override
    public String toString() {
      return super.toString();
    }
  }

  @Nested
  class MethodContextWithoutTransactionSynchronizationTests {

    @Test
    void methodWithoutParameter_annotatedOnMethod(final CapturedOutput capturedOutput) {
      runner
          .withBean(TestMethodContext.class)
          .run(
              context -> {
                final TestMethodContext methodContext = context.getBean(TestMethodContext.class);
                methodContext.methodWithoutParameter();

                assertThat(capturedOutput)
                    .doesNotContain(
                        "execution(void "
                            + TestMethodContext.class.getName()
                            + ".methodWithoutParameter())");
              });
    }
  }

  @Nested
  class MethodContextTests {

    @BeforeEach
    void beforeEach() {
      TransactionSynchronizationManager.initSynchronization();
    }

    @AfterEach
    void afterEach() {
      TransactionSynchronizationManager.clearSynchronization();
    }

    @Test
    void methodWithoutParameter_annotatedOnMethod(final CapturedOutput capturedOutput) {
      runner
          .withBean(TestMethodContext.class)
          .run(
              context -> {
                final TestMethodContext methodContext = context.getBean(TestMethodContext.class);
                methodContext.methodWithoutParameter();

                TransactionSynchronizationManager.getSynchronizations()
                    .forEach(synchronization -> synchronization.beforeCommit(false));

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

                TransactionSynchronizationManager.getSynchronizations()
                    .forEach(synchronization -> synchronization.beforeCommit(false));

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

                TransactionSynchronizationManager.getSynchronizations()
                    .forEach(synchronization -> synchronization.beforeCommit(false));

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

                TransactionSynchronizationManager.getSynchronizations()
                    .forEach(synchronization -> synchronization.beforeCommit(false));

                assertThat(capturedOutput)
                    .contains(
                        "execution(String " + TestMethodContext.class.getName() + ".toString())");
              });
    }
  }

  @LogBeforeCommit
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

  @Nested
  class ClassContextTests {

    @BeforeEach
    void beforeEach() {
      TransactionSynchronizationManager.initSynchronization();
    }

    @AfterEach
    void afterEach() {
      TransactionSynchronizationManager.clearSynchronization();
    }

    @Test
    void methodWithoutParameter_annotatedOnClass(final CapturedOutput capturedOutput) {
      runner
          .withBean(TestClassContext.class)
          .run(
              context -> {
                final TestClassContext classContext = context.getBean(TestClassContext.class);
                classContext.methodWithoutParameter();

                TransactionSynchronizationManager.getSynchronizations()
                    .forEach(synchronization -> synchronization.beforeCommit(false));

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

                TransactionSynchronizationManager.getSynchronizations()
                    .forEach(synchronization -> synchronization.beforeCommit(false));

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

                TransactionSynchronizationManager.getSynchronizations()
                    .forEach(synchronization -> synchronization.beforeCommit(false));

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

                TransactionSynchronizationManager.getSynchronizations()
                    .forEach(synchronization -> synchronization.beforeCommit(false));

                assertThat(capturedOutput)
                    .doesNotContain(
                        "execution(String " + TestClassContext.class.getName() + ".toString())");
              });
    }
  }

  @LogBeforeCommit
  static class ParentClassContext {

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

  static class ChildClassContext extends ParentClassContext {}

  @Nested
  class ChildContextTests {

    @BeforeEach
    void beforeEach() {
      TransactionSynchronizationManager.initSynchronization();
    }

    @AfterEach
    void afterEach() {
      TransactionSynchronizationManager.clearSynchronization();
    }

    @Test
    void methodWithoutParameter_annotatedOnChildClass(final CapturedOutput capturedOutput) {
      runner
          .withBean(ChildClassContext.class)
          .run(
              context -> {
                final ChildClassContext classContext = context.getBean(ChildClassContext.class);
                classContext.methodWithoutParameter();

                TransactionSynchronizationManager.getSynchronizations()
                    .forEach(synchronization -> synchronization.beforeCommit(false));

                assertThat(capturedOutput)
                    .contains(
                        "execution(void "
                            + ParentClassContext.class.getName()
                            + ".methodWithoutParameter())");
              });
    }

    @Test
    void methodWithParameter_annotatedOnChildClass(final CapturedOutput capturedOutput) {
      runner
          .withBean(ChildClassContext.class)
          .run(
              context -> {
                final ChildClassContext classContext = context.getBean(ChildClassContext.class);
                classContext.methodWithParameter("foo");

                TransactionSynchronizationManager.getSynchronizations()
                    .forEach(synchronization -> synchronization.beforeCommit(false));

                assertThat(capturedOutput)
                    .contains(
                        "execution(void "
                            + ParentClassContext.class.getName()
                            + ".methodWithParameter(String))");
              });
    }

    @Test
    void methodWithResult_annotatedOnChildClass(final CapturedOutput capturedOutput) {
      runner
          .withBean(ChildClassContext.class)
          .run(
              context -> {
                final ChildClassContext classContext = context.getBean(ChildClassContext.class);
                classContext.methodWithResult();

                TransactionSynchronizationManager.getSynchronizations()
                    .forEach(synchronization -> synchronization.beforeCommit(false));

                assertThat(capturedOutput)
                    .contains(
                        "execution(String "
                            + ParentClassContext.class.getName()
                            + ".methodWithResult())");
              });
    }

    @Test
    void toString_annotatedOnChildClass(final CapturedOutput capturedOutput) {
      runner
          .withBean(ChildClassContext.class)
          .run(
              context -> {
                final ChildClassContext classContext = context.getBean(ChildClassContext.class);
                classContext.toString();

                TransactionSynchronizationManager.getSynchronizations()
                    .forEach(synchronization -> synchronization.beforeCommit(false));

                assertThat(capturedOutput)
                    .doesNotContain(
                        "execution(String " + ParentClassContext.class.getName() + ".toString())");
              });
    }
  }
}
