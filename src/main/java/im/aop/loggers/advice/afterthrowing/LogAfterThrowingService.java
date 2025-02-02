package im.aop.loggers.advice.afterthrowing;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.Level;
import im.aop.loggers.messageinterpolation.ExceptionStringSupplierRegistrar;
import im.aop.loggers.messageinterpolation.JoinPointStringSupplierRegistrar;
import im.aop.loggers.messageinterpolation.StringSubstitutor;
import im.aop.loggers.messageinterpolation.StringSupplierLookup;
import im.aop.loggers.util.LoggerUtil;
import java.time.Duration;
import java.util.Objects;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
    final long startTime = System.nanoTime();

    final Logger logger = LoggerUtil.getLogger(annotation.declaringClass(), joinPoint);
    final Level loggingLevel = getLoggingLevel(annotation.level());
    if (isLoggingLevelDisabled(logger, loggingLevel)
        || isIgnoredException(exception, annotation.ignoreExceptions())) {
      logElapsed(startTime);
      return;
    }

    final StringSupplierLookup stringLookup = new StringSupplierLookup();

    logMessage(joinPoint, loggingLevel, annotation, logger, stringLookup, exception);
    logElapsed(startTime);
  }

  private void logElapsed(long startTime) {
    LOGGER.debug(
        "[logAfterThrowing] elapsed [{}]", Duration.ofNanos(System.nanoTime() - startTime));
  }

  private boolean isLoggingLevelDisabled(final Logger logger, final Level loggingLevel) {
    return !(LoggerUtil.isEnabled(logger, loggingLevel));
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

  private void logMessage(
      final JoinPoint joinPoint,
      final Level loggingLevel,
      final LogAfterThrowing annotation,
      final Logger logger,
      final StringSupplierLookup stringLookup,
      final Throwable exception) {
    joinPointStringSupplierRegistrar.register(stringLookup, joinPoint);
    exceptionStringSupplierRegistrar.register(stringLookup, exception);

    final String message =
        stringSubstitutor.substitute(
            getMessageTemplate(annotation.exitedAbnormallyMessage()), stringLookup);

    if (annotation.printStackTrace()) {
      LoggerUtil.logException(logger, loggingLevel, message, exception);
    } else {
      LoggerUtil.log(logger, loggingLevel, message);
    }
  }

  private Level getLoggingLevel(final Level loggingLevel) {
    return loggingLevel == Level.DEFAULT
        ? aopLoggersProperties.getExitedAbnormallyLevel()
        : loggingLevel;
  }

  private String getMessageTemplate(final String messageTemplate) {
    return messageTemplate.isEmpty()
        ? aopLoggersProperties.getExitedAbnormallyMessage()
        : messageTemplate;
  }
}
