package im.aop.loggers.advice.afterrollback;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Spring's AOP Advice for {@link LogAfterRollback}.
 *
 * @author Andy Lian
 */
@Aspect
public class LogAfterRollbackAdvice {

  @Autowired private LogAfterRollbackService logAfterRollbackService;

  @Pointcut("execution(public * *(..))")
  void publicMethod() {}

  @Pointcut("execution(String *.toString())")
  void toStringMethod() {}

  @Pointcut(value = "@annotation(logAfterRollback)", argNames = "logAfterRollback")
  void logAfterRollbackMethodContext(final LogAfterRollback logAfterRollback) {}

  @Before(
      value = "publicMethod() && logAfterRollbackMethodContext(logAfterRollback)",
      argNames = "joinPoint, logAfterRollback")
  void logAfterRollbackMethodContext(
      final JoinPoint joinPoint, final LogAfterRollback logAfterRollback) {
    logAfterRollback(joinPoint, logAfterRollback);
  }

  @Pointcut(value = "@within(logAfterRollback)", argNames = "logAfterRollback")
  void logAfterRollbackClassContext(final LogAfterRollback logAfterRollback) {}

  @Before(
      value =
          "publicMethod() && !toStringMethod() && logAfterRollbackClassContext(logAfterRollback)",
      argNames = "joinPoint, logAfterRollback")
  void logAfterRollbackClassContext(
      final JoinPoint joinPoint, final LogAfterRollback logAfterRollback) {
    logAfterRollback(joinPoint, logAfterRollback);
  }

  protected void logAfterRollback(
      final JoinPoint joinPoint, final LogAfterRollback logAfterRollback) {
    if (!TransactionSynchronizationManager.isSynchronizationActive()) {
      return;
    }

    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {
          @Override
          public void afterCompletion(int status) {
            if (status == STATUS_ROLLED_BACK) {
              logAfterRollbackService.logAfterRollback(joinPoint, logAfterRollback);
            }
          }
        });
  }
}
