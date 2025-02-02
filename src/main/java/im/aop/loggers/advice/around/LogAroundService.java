package im.aop.loggers.advice.around;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.Level;
import im.aop.loggers.messageinterpolation.ElapsedStringSupplierRegistrar;
import im.aop.loggers.messageinterpolation.ElapsedTimeLimitStringSupplierRegistrar;
import im.aop.loggers.messageinterpolation.ExceptionStringSupplierRegistrar;
import im.aop.loggers.messageinterpolation.JoinPointStringSupplierRegistrar;
import im.aop.loggers.messageinterpolation.ReturnValueStringSupplierRegistrar;
import im.aop.loggers.messageinterpolation.StringSubstitutor;
import im.aop.loggers.messageinterpolation.StringSupplierLookup;
import im.aop.loggers.util.LoggerUtil;
import java.time.Duration;
import java.util.Objects;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class LogAroundService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LogAroundService.class);

  @Autowired private StringSubstitutor stringSubstitutor;

  @Autowired private JoinPointStringSupplierRegistrar joinPointStringSupplierRegistrar;

  @Autowired private ReturnValueStringSupplierRegistrar returnValueStringSupplierRegistrar;

  @Autowired private ExceptionStringSupplierRegistrar exceptionStringSupplierRegistrar;

  @Autowired private ElapsedStringSupplierRegistrar elapsedStringSupplierRegistrar;

  @Autowired
  private ElapsedTimeLimitStringSupplierRegistrar elapsedTimeLimitStringSupplierRegistrar;

  private final AopLoggersProperties aopLoggersProperties;

  public LogAroundService(final AopLoggersProperties aopLoggersProperties) {
    this.aopLoggersProperties = Objects.requireNonNull(aopLoggersProperties);
  }

  public Object logAround(final ProceedingJoinPoint joinPoint, final LogAround logAround)
      throws Throwable {
    final long startTime = System.nanoTime();

    final Logger logger = LoggerUtil.getLogger(logAround.declaringClass(), joinPoint);
    final StringSupplierLookup stringLookup = new StringSupplierLookup();

    logEnteringMessage(joinPoint, logAround, logger, stringLookup);
    final long proceedStartTime = System.nanoTime();

    try {

      final Object returnValue = joinPoint.proceed();

      final long proceedElapsedTime = System.nanoTime() - proceedStartTime;
      logExitedMessage(joinPoint, logAround, logger, stringLookup, returnValue);
      logElapsedTime(logAround, logger, stringLookup, proceedElapsedTime);
      logElapsedWarning(logAround, logger, stringLookup, proceedElapsedTime);

      logElapsed(startTime, proceedElapsedTime);

      return returnValue;

    } catch (Throwable e) {

      final long proceedElapsedTime = System.nanoTime() - proceedStartTime;
      logExitedAbnormallyMessage(logAround, logger, stringLookup, e);
      logElapsedTime(logAround, logger, stringLookup, proceedElapsedTime);
      logElapsedWarning(logAround, logger, stringLookup, proceedElapsedTime);

      logElapsed(startTime, proceedElapsedTime);
      throw e;
    }
  }

  private void logElapsed(final long startTime, final long proceedElapsedTime) {
    LOGGER.debug(
        "[logAround] elapsed [{}]",
        Duration.ofNanos(System.nanoTime() - startTime - proceedElapsedTime));
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

  private void logEnteringMessage(
      final ProceedingJoinPoint joinPoint,
      final LogAround annotation,
      final Logger logger,
      final StringSupplierLookup stringLookup) {
    final Level enteringLevel =
        getLoggingLevel(annotation.level(), aopLoggersProperties.getEnteringLevel());
    if (isLoggingLevelDisabled(logger, enteringLevel)) {
      return;
    }

    joinPointStringSupplierRegistrar.register(stringLookup, joinPoint);

    final String enteringMessage =
        stringSubstitutor.substitute(
            getMessageTemplate(
                annotation.enteringMessage(), aopLoggersProperties.getEnteringMessage()),
            stringLookup);
    LoggerUtil.log(logger, enteringLevel, enteringMessage);
  }

  private void logElapsedTime(
      final LogAround annotation,
      final Logger logger,
      final StringSupplierLookup stringLookup,
      final long elapsedTime) {
    final Level elapsedLevel =
        getLoggingLevel(annotation.level(), aopLoggersProperties.getElapsedLevel());
    if (isLoggingLevelDisabled(logger, elapsedLevel)) {
      return;
    }

    elapsedStringSupplierRegistrar.register(stringLookup, elapsedTime);

    final String elapsedMessage =
        stringSubstitutor.substitute(
            getMessageTemplate(
                annotation.elapsedMessage(), aopLoggersProperties.getElapsedMessage()),
            stringLookup);
    LoggerUtil.log(logger, elapsedLevel, elapsedMessage);
  }

  private void logElapsedWarning(
      final LogAround annotation,
      final Logger logger,
      final StringSupplierLookup stringLookup,
      final long elapsedTime) {
    final Level elapsedWarningLevel =
        getLoggingLevel(
            annotation.elapsedWarningLevel(), aopLoggersProperties.getElapsedWarningLevel());
    if (isLoggingLevelDisabled(logger, elapsedWarningLevel)) {
      return;
    }

    if (annotation.elapsedTimeLimit() == 0) {
      return;
    }
    final Duration elapsedTimeLimit =
        Duration.of(annotation.elapsedTimeLimit(), annotation.elapsedTimeUnit());
    if (!elapsedTimeLimit.minusNanos(elapsedTime).isNegative()) {
      return;
    }

    elapsedStringSupplierRegistrar.register(stringLookup, elapsedTime);
    elapsedTimeLimitStringSupplierRegistrar.register(stringLookup, elapsedTimeLimit);

    final String elapsedWarningMessage =
        stringSubstitutor.substitute(
            getMessageTemplate(
                annotation.elapsedWarningMessage(),
                aopLoggersProperties.getElapsedWarningMessage()),
            stringLookup);
    LoggerUtil.log(logger, elapsedWarningLevel, elapsedWarningMessage);
  }

  private void logExitedMessage(
      final ProceedingJoinPoint joinPoint,
      final LogAround annotation,
      final Logger logger,
      final StringSupplierLookup stringLookup,
      final Object returnValue) {
    final Level exitedLevel =
        getLoggingLevel(annotation.level(), aopLoggersProperties.getExitedLevel());
    if (isLoggingLevelDisabled(logger, exitedLevel)) {
      return;
    }

    returnValueStringSupplierRegistrar.register(stringLookup, joinPoint, returnValue);

    final String exitedMessage =
        stringSubstitutor.substitute(
            getMessageTemplate(annotation.exitedMessage(), aopLoggersProperties.getExitedMessage()),
            stringLookup);
    LoggerUtil.log(logger, exitedLevel, exitedMessage);
  }

  private void logExitedAbnormallyMessage(
      final LogAround annotation,
      final Logger logger,
      final StringSupplierLookup stringLookup,
      final Throwable exception) {
    final Level exitedAbnormallyLevel =
        getLoggingLevel(
            annotation.exitedAbnormallyLevel(), aopLoggersProperties.getExitedAbnormallyLevel());

    if (isLoggingLevelDisabled(logger, exitedAbnormallyLevel)
        || isIgnoredException(exception, annotation.ignoreExceptions())) {
      return;
    }

    exceptionStringSupplierRegistrar.register(stringLookup, exception);

    final String exitedAbnormallyMessage =
        stringSubstitutor.substitute(
            getMessageTemplate(
                annotation.exitedAbnormallyMessage(),
                aopLoggersProperties.getExitedAbnormallyMessage()),
            stringLookup);

    if (annotation.printStackTrace()) {
      LoggerUtil.logException(logger, exitedAbnormallyLevel, exitedAbnormallyMessage, exception);
    } else {
      LoggerUtil.log(logger, exitedAbnormallyLevel, exitedAbnormallyMessage);
    }
  }

  private Level getLoggingLevel(final Level loggingLevel, final Level defaultLoggingLevel) {
    return loggingLevel == Level.DEFAULT ? defaultLoggingLevel : loggingLevel;
  }

  private String getMessageTemplate(
      final String messageTemplate, final String defaultMessageTemplate) {
    return !messageTemplate.isEmpty() ? messageTemplate : defaultMessageTemplate;
  }
}
