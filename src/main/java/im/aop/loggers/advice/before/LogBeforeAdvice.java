package im.aop.loggers.advice.before;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring's AOP Advice for {@link LogBefore}.
 *
 * @author Andy Lian
 */
@Aspect
public class LogBeforeAdvice {

  @Autowired private LogBeforeService logBeforeService;

  @Pointcut("execution(public * *(..))")
  void publicMethod() {}

  @Pointcut("execution(String *.toString())")
  void toStringMethod() {}

  @Pointcut(value = "@annotation(logBefore)", argNames = "logBefore")
  void logBeforeMethodContext(final LogBefore logBefore) {}

  @Before(
      value = "publicMethod() && logBeforeMethodContext(logBefore)",
      argNames = "joinPoint, logBefore")
  void logBeforeMethodContext(final JoinPoint joinPoint, final LogBefore logBefore)
      throws Throwable {
    logBefore(joinPoint, logBefore);
  }

  @Pointcut(value = "@within(logBefore)", argNames = "logBefore")
  void logBeforeClassContext(final LogBefore logBefore) {}

  @Before(
      value = "publicMethod() && !toStringMethod() && logBeforeClassContext(logBefore)",
      argNames = "joinPoint, logBefore")
  void logBeforeClassContext(final JoinPoint joinPoint, final LogBefore logBefore)
      throws Throwable {
    logBefore(joinPoint, logBefore);
  }

  protected void logBefore(final JoinPoint joinPoint, final LogBefore logBefore) throws Throwable {
    logBeforeService.logBefore(joinPoint, logBefore);
  }
}
