package im.aop.loggers.advice.afterthrowing;

import java.time.Duration;
import java.util.Objects;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.Level;
import im.aop.loggers.messageinterpolation.ExceptionStringSupplierRegistrar;
import im.aop.loggers.messageinterpolation.JoinPointStringSupplierRegistrar;
import im.aop.loggers.messageinterpolation.StringSubstitutor;
import im.aop.loggers.messageinterpolation.StringSupplierLookup;
import im.aop.loggers.util.LoggerUtil;

public class LogAfterThrowingService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LogAfterThrowingService.class);

  @Autowired private StringSubstitutor stringSubstitutor;

  @Autowired private JoinPointStringSupplierRegistrar joinPointStringSupplierRegistrar;

  @Autowired private ExceptionStringSupplierRegistrar exceptionStringSupplierRegistrar;

  private final AopLoggersProperties aopLoggersProperties;

  public LogAfterThrowingService(final AopLoggersProperties aopLoggersProperties) {
    this.aopLoggersProperties = Objects.requireNonNull(aopLoggersProperties);
  }

  public void logAfterThrowing(
      final JoinPoint joinPoint, final LogAfterThrowing annotation, final Throwable exception) {
    final long enteringTime = System.nanoTime();

    final Logger logger = LoggerUtil.getLogger(annotation.declaringClass(), joinPoint);
    final Level exitedAbnormallyLevel = getExitedAbnormallyLevel(annotation.level());
    if (isLoggerLevelDisabled(logger, exitedAbnormallyLevel)
        || isIgnoredException(exception, annotation.ignoreExceptions())) {
      logElapsed(enteringTime);
      return;
    }

    final StringSupplierLookup stringLookup = new StringSupplierLookup();

    logExitedAbnormallyMessage(
        joinPoint, exitedAbnormallyLevel, annotation, logger, stringLookup, exception);
    logElapsed(enteringTime);
  }

  private void logElapsed(long enteringTime) {
    LOGGER.debug(
        "[logAfterThrowing] elapsed [{}]", Duration.ofNanos(System.nanoTime() - enteringTime));
  }

  private boolean isLoggerLevelDisabled(final Logger logger, final Level level) {
    return LoggerUtil.isEnabled(logger, level) == false;
  }

  private boolean isIgnoredException(
      final Throwable exception, final Class<? extends Throwable>[] ignoredExceptions) {
    if (exception == null) {
      return true;
    }

    return matchesIgnoreExceptions(exception, ignoredExceptions)
        || matchesIgnoreExceptions(exception, aopLoggersProperties.getIgnoreExceptions());
  }

  private boolean matchesIgnoreExceptions(
      final Throwable exception, final Class<? extends Throwable>[] ignoredExceptions) {
    if (ignoredExceptions == null || ignoredExceptions.length == 0) {
      return false;
    }
    for (Class<? extends Throwable> ignoredException : ignoredExceptions) {
      if (ignoredException == null) {
        continue;
      }
      if (ignoredException.isInstance(exception)) {
        return true;
      }
    }
    return false;
  }

  private void logExitedAbnormallyMessage(
      final JoinPoint joinPoint,
      final Level exitedAbnormallyLevel,
      final LogAfterThrowing annotation,
      final Logger logger,
      final StringSupplierLookup stringLookup,
      final Throwable exception) {
    joinPointStringSupplierRegistrar.register(stringLookup, joinPoint);
    exceptionStringSupplierRegistrar.register(stringLookup, exception);

    final String message =
        stringSubstitutor.substitute(
            getExitedAbnormallyMessage(annotation.exitedAbnormallyMessage()), stringLookup);

    if (annotation.printStackTrace()) {
      LoggerUtil.logException(logger, exitedAbnormallyLevel, message, exception);
    } else {
      LoggerUtil.log(logger, exitedAbnormallyLevel, message);
    }
  }

  private Level getExitedAbnormallyLevel(final Level exitedAbnormallyLevel) {
    return exitedAbnormallyLevel == Level.DEFAULT
        ? aopLoggersProperties.getExitedAbnormallyLevel()
        : exitedAbnormallyLevel;
  }

  private String getExitedAbnormallyMessage(final String exitedAbnormallyMessage) {
    return exitedAbnormallyMessage.length() == 0
        ? aopLoggersProperties.getExitedAbnormallyMessage()
        : exitedAbnormallyMessage;
  }
}
