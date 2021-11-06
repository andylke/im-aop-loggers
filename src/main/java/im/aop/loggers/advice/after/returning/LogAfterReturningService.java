package im.aop.loggers.advice.after.returning;

import java.time.Duration;
import java.util.Objects;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.logging.Level;
import im.aop.loggers.logging.LoggerService;
import im.aop.loggers.logging.message.JoinPointStringSupplierRegistrar;
import im.aop.loggers.logging.message.ReturnValueStringSupplierRegistrar;
import im.aop.loggers.logging.message.StringSubstitutor;
import im.aop.loggers.logging.message.StringSupplierLookup;

public class LogAfterReturningService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LogAfterReturningService.class);

  private static final StringSubstitutor STRING_SUBSTITUTOR = new StringSubstitutor();

  private static final JoinPointStringSupplierRegistrar JOIN_POINT_STRING_SUPPLIER_REGISTRAR =
      new JoinPointStringSupplierRegistrar();

  private static final ReturnValueStringSupplierRegistrar RETURN_VALUE_STRING_SUPPLIER_REGISTRAR =
      new ReturnValueStringSupplierRegistrar();

  private static final LoggerService LOGGER_SERVICE = new LoggerService();

  private final AopLoggersProperties aopLoggersProperties;

  public LogAfterReturningService(final AopLoggersProperties aopLoggersProperties) {
    this.aopLoggersProperties = Objects.requireNonNull(aopLoggersProperties);
  }

  public void logAfterReturning(
      final JoinPoint joinPoint, final LogAfterReturning annotation, final Object returnValue) {
    if (isDisabled()) {
      return;
    }

    final long enteringTime = System.nanoTime();

    final Logger logger = LOGGER_SERVICE.getLogger(annotation.declaringClass(), joinPoint);
    final Level exitedLevel = getExitedLevel(annotation.level());
    if (isLoggerLevelDisabled(logger, exitedLevel)) {
      logElapsed(enteringTime);
      return;
    }

    final StringSupplierLookup stringLookup = new StringSupplierLookup();

    logExitedMessage(
        joinPoint, exitedLevel, annotation.exitedMessage(), logger, stringLookup, returnValue);
    logElapsed(enteringTime);
  }

  private void logElapsed(long enteringTime) {
    LOGGER.debug(
        "[logAfterReturning] elapsed [{}]", Duration.ofNanos(System.nanoTime() - enteringTime));
  }

  private boolean isDisabled() {
    return aopLoggersProperties.isEnabled() == false;
  }

  private boolean isLoggerLevelDisabled(final Logger logger, final Level level) {
    return LOGGER_SERVICE.isEnabled(logger, level) == false;
  }

  private void logExitedMessage(
      final JoinPoint joinPoint,
      final Level exitedLevel,
      final String exitedMessage,
      final Logger logger,
      final StringSupplierLookup stringLookup,
      final Object returnValue) {
    JOIN_POINT_STRING_SUPPLIER_REGISTRAR.register(stringLookup, joinPoint);
    RETURN_VALUE_STRING_SUPPLIER_REGISTRAR.register(stringLookup, joinPoint, returnValue);

    final String message =
        STRING_SUBSTITUTOR.substitute(getExitedMessage(exitedMessage), stringLookup);
    LOGGER_SERVICE.log(logger, exitedLevel, message);
  }

  private Level getExitedLevel(final Level exitedLevel) {
    return exitedLevel == Level.DEFAULT ? aopLoggersProperties.getExitedLevel() : exitedLevel;
  }

  private String getExitedMessage(final String exitedMessage) {
    return exitedMessage.length() == 0 ? aopLoggersProperties.getExitedMessage() : exitedMessage;
  }
}
