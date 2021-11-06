package im.aop.loggers.advice.after.returning;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring's AOP Advice for {@link LogAfterReturning}.
 *
 * @author Andy Lian
 */
@Aspect
public class LogAfterReturningAdvice {

  @Autowired private LogAfterReturningService logAfterReturningService;

  @Pointcut("execution(public * *(..))")
  void publicMethod() {}

  @Pointcut("execution(String *.toString())")
  void toStringMethod() {}

  @Pointcut(value = "@annotation(logAfterReturning)", argNames = "logAfterReturning")
  void logAfterReturningMethodContext(final LogAfterReturning logAfterReturning) {}

  @AfterReturning(
      value = "publicMethod() && logAfterReturningMethodContext(logAfterReturning)",
      argNames = "joinPoint, logAfterReturning, returnValue",
      returning = "returnValue")
  void logAfterReturningMethodContext(
      final JoinPoint joinPoint,
      final LogAfterReturning logAfterReturning,
      final Object returnValue)
      throws Throwable {
    logAfterReturning(joinPoint, logAfterReturning, returnValue);
  }

  @Pointcut(value = "@within(logAfterReturning)", argNames = "logAfterReturning")
  void logAfterReturningClassContext(final LogAfterReturning logAfterReturning) {}

  @AfterReturning(
      value =
          "publicMethod() && !toStringMethod() && logAfterReturningClassContext(logAfterReturning)",
      argNames = "joinPoint, logAfterReturning, returnValue",
      returning = "returnValue")
  void logAfterReturningClassContext(
      final JoinPoint joinPoint,
      final LogAfterReturning logAfterReturning,
      final Object returnValue)
      throws Throwable {
    logAfterReturning(joinPoint, logAfterReturning, returnValue);
  }

  protected void logAfterReturning(
      final JoinPoint joinPoint,
      final LogAfterReturning logAfterReturning,
      final Object returnValue)
      throws Throwable {
    logAfterReturningService.logAfterReturning(joinPoint, logAfterReturning, returnValue);
  }
}
