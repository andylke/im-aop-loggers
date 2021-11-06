package im.aop.loggers.advice.around;

import java.time.Duration;
import java.util.Objects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.logging.Level;
import im.aop.loggers.logging.LoggerService;
import im.aop.loggers.logging.message.ElapsedStringSupplierRegistrar;
import im.aop.loggers.logging.message.ElapsedTimeLimitStringSupplierRegistrar;
import im.aop.loggers.logging.message.ExceptionStringSupplierRegistrar;
import im.aop.loggers.logging.message.JoinPointStringSupplierRegistrar;
import im.aop.loggers.logging.message.ReturnValueStringSupplierRegistrar;
import im.aop.loggers.logging.message.StringSubstitutor;
import im.aop.loggers.logging.message.StringSupplierLookup;

public class LogAroundService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LogAroundService.class);

  private static final LoggerService LOGGER_SERVICE = new LoggerService();

  private static final StringSubstitutor STRING_SUBSTITUTOR = new StringSubstitutor();

  private static final JoinPointStringSupplierRegistrar JOIN_POINT_STRING_SUPPLIER_REGISTRAR =
      new JoinPointStringSupplierRegistrar();

  private static final ReturnValueStringSupplierRegistrar RETURN_VALUE_STRING_SUPPLIER_REGISTRAR =
      new ReturnValueStringSupplierRegistrar();

  private static final ExceptionStringSupplierRegistrar EXCEPTION_STRING_SUPPLIER_REGISTRAR =
      new ExceptionStringSupplierRegistrar();

  private static final ElapsedStringSupplierRegistrar ELAPSED_STRING_SUPPLIER_REGISTRAR =
      new ElapsedStringSupplierRegistrar();

  private static final ElapsedTimeLimitStringSupplierRegistrar
      ELAPSED_TIME_LIMIT_STRING_SUPPLIER_REGISTRAR = new ElapsedTimeLimitStringSupplierRegistrar();

  private final AopLoggersProperties aopLoggersProperties;

  public LogAroundService(final AopLoggersProperties aopLoggersProperties) {
    this.aopLoggersProperties = Objects.requireNonNull(aopLoggersProperties);
  }

  public Object logAround(final ProceedingJoinPoint joinPoint, final LogAround logAround)
      throws Throwable {
    if (isDisabled()) {
      return joinPoint.proceed();
    }

    final long enteringTime = System.nanoTime();

    final Logger logger = LOGGER_SERVICE.getLogger(logAround.declaringClass(), joinPoint);
    final StringSupplierLookup stringLookup = new StringSupplierLookup();

    logEnteringMessage(joinPoint, logAround, logger, stringLookup);
    final long beforeProceedTime = System.nanoTime();

    try {

      final Object returnValue = joinPoint.proceed();

      final long proceedElapsedTime = System.nanoTime() - beforeProceedTime;
      logExitedMessage(joinPoint, logAround, logger, stringLookup, returnValue);
      logElapsedTime(joinPoint, logAround, logger, stringLookup, proceedElapsedTime);
      logElapsedWarning(joinPoint, logAround, logger, stringLookup, proceedElapsedTime);

      logElapsed(enteringTime, proceedElapsedTime);

      return returnValue;

    } catch (Throwable e) {

      final long proceedElapsedTime = System.nanoTime() - beforeProceedTime;
      logExitedAbnormallyMessage(joinPoint, logAround, logger, stringLookup, e);
      logElapsedTime(joinPoint, logAround, logger, stringLookup, proceedElapsedTime);
      logElapsedWarning(joinPoint, logAround, logger, stringLookup, proceedElapsedTime);

      logElapsed(enteringTime, proceedElapsedTime);
      throw e;
    }
  }

  private void logElapsed(final long enteringTime, final long proceedElapsedTime) {
    LOGGER.debug(
        "[logAround] elapsed [{}]",
        Duration.ofNanos(System.nanoTime() - enteringTime - proceedElapsedTime));
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

  private void logEnteringMessage(
      final ProceedingJoinPoint joinPoint,
      final LogAround annotation,
      final Logger logger,
      final StringSupplierLookup stringLookup) {
    final Level enteringLevel =
        getLevel(annotation.level(), aopLoggersProperties.getEnteringLevel());
    if (isLoggerLevelDisabled(logger, enteringLevel)) {
      return;
    }

    JOIN_POINT_STRING_SUPPLIER_REGISTRAR.register(stringLookup, joinPoint);

    final String enteringMessage =
        STRING_SUBSTITUTOR.substitute(
            getMessage(annotation.enteringMessage(), aopLoggersProperties.getEnteringMessage()),
            stringLookup);
    LOGGER_SERVICE.log(logger, enteringLevel, enteringMessage);
  }

  private void logElapsedTime(
      final ProceedingJoinPoint joinPoint,
      final LogAround annotation,
      final Logger logger,
      final StringSupplierLookup stringLookup,
      final long elapsedTime) {
    final Level elapsedLevel = getLevel(annotation.level(), aopLoggersProperties.getElapsedLevel());
    if (isLoggerLevelDisabled(logger, elapsedLevel)) {
      return;
    }

    ELAPSED_STRING_SUPPLIER_REGISTRAR.register(stringLookup, elapsedTime);

    final String elapsedMessage =
        STRING_SUBSTITUTOR.substitute(
            getMessage(annotation.elapsedMessage(), aopLoggersProperties.getElapsedMessage()),
            stringLookup);
    LOGGER_SERVICE.log(logger, elapsedLevel, elapsedMessage);
  }

  private void logElapsedWarning(
      final ProceedingJoinPoint joinPoint,
      final LogAround annotation,
      final Logger logger,
      final StringSupplierLookup stringLookup,
      final long elapsedTime) {
    final Level elapsedWarningLevel =
        getLevel(annotation.elapsedWarningLevel(), aopLoggersProperties.getElapsedWarningLevel());
    if (isLoggerLevelDisabled(logger, elapsedWarningLevel)) {
      return;
    }

    if (annotation.elapsedTimeLimit() == 0) {
      return;
    }
    final Duration elapsedTimeLimit =
        Duration.of(annotation.elapsedTimeLimit(), annotation.elapsedTimeUnit());
    if (elapsedTimeLimit.minusNanos(elapsedTime).isNegative() == false) {
      return;
    }

    ELAPSED_STRING_SUPPLIER_REGISTRAR.register(stringLookup, elapsedTime);
    ELAPSED_TIME_LIMIT_STRING_SUPPLIER_REGISTRAR.register(stringLookup, elapsedTimeLimit);

    final String elapsedWarningMessage =
        STRING_SUBSTITUTOR.substitute(
            getMessage(
                annotation.elapsedWarningMessage(),
                aopLoggersProperties.getElapsedWarningMessage()),
            stringLookup);
    LOGGER_SERVICE.log(logger, elapsedWarningLevel, elapsedWarningMessage);
  }

  private void logExitedMessage(
      final ProceedingJoinPoint joinPoint,
      final LogAround annotation,
      final Logger logger,
      final StringSupplierLookup stringLookup,
      final Object returnValue) {
    final Level exitedLevel = getLevel(annotation.level(), aopLoggersProperties.getExitedLevel());
    if (isLoggerLevelDisabled(logger, exitedLevel)) {
      return;
    }

    RETURN_VALUE_STRING_SUPPLIER_REGISTRAR.register(stringLookup, joinPoint, returnValue);

    final String exitedMessage =
        STRING_SUBSTITUTOR.substitute(
            getMessage(annotation.exitedMessage(), aopLoggersProperties.getExitedMessage()),
            stringLookup);
    LOGGER_SERVICE.log(logger, exitedLevel, exitedMessage);
  }

  private void logExitedAbnormallyMessage(
      final ProceedingJoinPoint joinPoint,
      final LogAround annotation,
      final Logger logger,
      final StringSupplierLookup stringLookup,
      final Throwable exception) {
    final Level exitedAbnormallyLevel =
        getLevel(
            annotation.exitedAbnormallyLevel(), aopLoggersProperties.getExitedAbnormallyLevel());
    if (isLoggerLevelDisabled(logger, exitedAbnormallyLevel)
        || isIgnoredException(exception, annotation.ignoreExceptions())) {
      return;
    }

    EXCEPTION_STRING_SUPPLIER_REGISTRAR.register(stringLookup, exception);

    final String exitedAbnormallyMessage =
        STRING_SUBSTITUTOR.substitute(
            getMessage(
                annotation.exitedAbnormallyMessage(),
                aopLoggersProperties.getExitedAbnormallyMessage()),
            stringLookup);

    if (annotation.printStackTrace()) {
      LOGGER_SERVICE.logException(
          logger, exitedAbnormallyLevel, exitedAbnormallyMessage, exception);
    } else {
      LOGGER_SERVICE.log(logger, exitedAbnormallyLevel, exitedAbnormallyMessage);
    }
  }

  private Level getLevel(final Level level, final Level defaultLevel) {
    return level == Level.DEFAULT ? defaultLevel : level;
  }

  private String getMessage(final String message, final String defaultMessage) {
    return message.length() > 0 ? message : defaultMessage;
  }
}
