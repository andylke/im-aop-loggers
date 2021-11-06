package im.aop.loggers.advice.after.throwing;

import java.time.Duration;
import java.util.Objects;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.logging.Level;
import im.aop.loggers.logging.LoggerService;
import im.aop.loggers.logging.message.ExceptionStringSupplierRegistrar;
import im.aop.loggers.logging.message.JoinPointStringSupplierRegistrar;
import im.aop.loggers.logging.message.StringSubstitutor;
import im.aop.loggers.logging.message.StringSupplierLookup;

public class LogAfterThrowingService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LogAfterThrowingService.class);

  private static final StringSubstitutor STRING_SUBSTITUTOR = new StringSubstitutor();

  private static final JoinPointStringSupplierRegistrar JOIN_POINT_STRING_SUPPLIER_REGISTRAR =
      new JoinPointStringSupplierRegistrar();

  private static final ExceptionStringSupplierRegistrar EXCEPTION_STRING_SUPPLIER_REGISTRAR =
      new ExceptionStringSupplierRegistrar();

  private static final LoggerService LOGGER_SERVICE = new LoggerService();

  private final AopLoggersProperties aopLoggersProperties;

  public LogAfterThrowingService(final AopLoggersProperties aopLoggersProperties) {
    this.aopLoggersProperties = Objects.requireNonNull(aopLoggersProperties);
  }

  public void logAfterThrowing(
      final JoinPoint joinPoint, final LogAfterThrowing annotation, final Throwable exception) {
    if (isDisabled()) {
      return;
    }

    final long enteringTime = System.nanoTime();

    final Logger logger = LOGGER_SERVICE.getLogger(annotation.declaringClass(), joinPoint);
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

  private boolean isDisabled() {
    return aopLoggersProperties.isEnabled() == false;
  }

  private boolean isLoggerLevelDisabled(final Logger logger, final Level level) {
    return LOGGER_SERVICE.isEnabled(logger, level) == false;
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
    JOIN_POINT_STRING_SUPPLIER_REGISTRAR.register(stringLookup, joinPoint);
    EXCEPTION_STRING_SUPPLIER_REGISTRAR.register(stringLookup, exception);

    final String message =
        STRING_SUBSTITUTOR.substitute(
            getExitedAbnormallyMessage(annotation.exitedAbnormallyMessage()), stringLookup);

    if (annotation.printStackTrace()) {
      LOGGER_SERVICE.logException(logger, exitedAbnormallyLevel, message, exception);
    } else {
      LOGGER_SERVICE.log(logger, exitedAbnormallyLevel, message);
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
