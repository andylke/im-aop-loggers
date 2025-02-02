package im.aop.loggers.advice.beforecommit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Spring's AOP Advice for {@link LogBeforeCommit}.
 *
 * @author Andy Lian
 */
@Aspect
public class LogBeforeCommitAdvice {

  @Autowired private LogBeforeCommitService logBeforeCommitService;

  @Pointcut("execution(public * *(..))")
  void publicMethod() {}

  @Pointcut("execution(String *.toString())")
  void toStringMethod() {}

  @Pointcut(value = "@annotation(logBeforeCommit)", argNames = "logBeforeCommit")
  void logBeforeCommitMethodContext(final LogBeforeCommit logBeforeCommit) {}

  @Before(
      value = "publicMethod() && logBeforeCommitMethodContext(logBeforeCommit)",
      argNames = "joinPoint, logBeforeCommit")
  void logBeforeCommitMethodContext(
      final JoinPoint joinPoint, final LogBeforeCommit logBeforeCommit) {
    logBeforeCommit(joinPoint, logBeforeCommit);
  }

  @Pointcut(value = "@within(logBeforeCommit)", argNames = "logBeforeCommit")
  void logBeforeCommitClassContext(final LogBeforeCommit logBeforeCommit) {}

  @Before(
      value = "publicMethod() && !toStringMethod() && logBeforeCommitClassContext(logBeforeCommit)",
      argNames = "joinPoint, logBeforeCommit")
  void logBeforeCommitClassContext(
      final JoinPoint joinPoint, final LogBeforeCommit logBeforeCommit) {
    logBeforeCommit(joinPoint, logBeforeCommit);
  }

  protected void logBeforeCommit(final JoinPoint joinPoint, final LogBeforeCommit logBeforeCommit) {
    if (!TransactionSynchronizationManager.isSynchronizationActive()) {
      return;
    }

    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {

          @Override
          public void beforeCommit(boolean readOnly) {
            logBeforeCommitService.logBeforeCommit(joinPoint, logBeforeCommit);
          }
        });
  }
}
