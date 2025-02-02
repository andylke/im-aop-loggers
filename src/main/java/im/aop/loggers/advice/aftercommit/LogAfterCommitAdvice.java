package im.aop.loggers.advice.aftercommit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Spring's AOP Advice for {@link LogAfterCommit}.
 *
 * @author Andy Lian
 */
@Aspect
public class LogAfterCommitAdvice {

  @Autowired private LogAfterCommitService logAfterCommitService;

  @Pointcut("execution(public * *(..))")
  void publicMethod() {}

  @Pointcut("execution(String *.toString())")
  void toStringMethod() {}

  @Pointcut(value = "@annotation(logAfterCommit)", argNames = "logAfterCommit")
  void logAfterCommitMethodContext(final LogAfterCommit logAfterCommit) {}

  @Before(
      value = "publicMethod() && logAfterCommitMethodContext(logAfterCommit)",
      argNames = "joinPoint, logAfterCommit")
  void logAfterCommitMethodContext(final JoinPoint joinPoint, final LogAfterCommit logAfterCommit) {
    logAfterCommit(joinPoint, logAfterCommit);
  }

  @Pointcut(value = "@within(logAfterCommit)", argNames = "logAfterCommit")
  void logAfterCommitClassContext(final LogAfterCommit logAfterCommit) {}

  @Before(
      value = "publicMethod() && !toStringMethod() && logAfterCommitClassContext(logAfterCommit)",
      argNames = "joinPoint, logAfterCommit")
  void logAfterCommitClassContext(final JoinPoint joinPoint, final LogAfterCommit logAfterCommit) {
    logAfterCommit(joinPoint, logAfterCommit);
  }

  protected void logAfterCommit(final JoinPoint joinPoint, final LogAfterCommit logAfterCommit) {
    if (!TransactionSynchronizationManager.isSynchronizationActive()) {
      return;
    }

    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {

          @Override
          public void afterCommit() {
            logAfterCommitService.logAfterCommit(joinPoint, logAfterCommit);
          }
        });
  }
}
