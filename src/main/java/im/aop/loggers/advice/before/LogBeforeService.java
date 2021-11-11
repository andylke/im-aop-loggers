package im.aop.loggers.advice.before;

import java.time.Duration;
import java.util.Objects;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.Level;
import im.aop.loggers.messageinterpolation.JoinPointStringSupplierRegistrar;
import im.aop.loggers.messageinterpolation.StringSubstitutor;
import im.aop.loggers.messageinterpolation.StringSupplierLookup;
import im.aop.loggers.util.LoggerUtil;

public class LogBeforeService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LogBeforeService.class);

  @Autowired private StringSubstitutor stringSubstitutor;

  @Autowired private JoinPointStringSupplierRegistrar joinPointStringSupplierRegistrar;

  private final AopLoggersProperties aopLoggersProperties;

  public LogBeforeService(final AopLoggersProperties aopLoggersProperties) {
    this.aopLoggersProperties = Objects.requireNonNull(aopLoggersProperties);
  }

  public void logBefore(final JoinPoint joinPoint, final LogBefore annotation) {
    final long enteringTime = System.nanoTime();

    final Logger logger = LoggerUtil.getLogger(annotation.declaringClass(), joinPoint);
    final Level enteringLevel = getEnteringLevel(annotation.level());
    if (isLoggerLevelDisabled(logger, enteringLevel)) {
      logElapsed(enteringTime);
      return;
    }

    final StringSupplierLookup stringLookup = new StringSupplierLookup();

    logEnteringMessage(
        joinPoint, enteringLevel, annotation.enteringMessage(), logger, stringLookup);
    logElapsed(enteringTime);
  }

  private void logElapsed(long enteringTime) {
    LOGGER.debug("[logBefore] elapsed [{}]", Duration.ofNanos(System.nanoTime() - enteringTime));
  }

  private boolean isLoggerLevelDisabled(final Logger logger, final Level level) {
    return LoggerUtil.isEnabled(logger, level) == false;
  }

  private void logEnteringMessage(
      final JoinPoint joinPoint,
      final Level enteringLevel,
      final String enteringMessage,
      final Logger logger,
      final StringSupplierLookup stringLookup) {
    joinPointStringSupplierRegistrar.register(stringLookup, joinPoint);

    final String message =
        stringSubstitutor.substitute(getEnteringMesage(enteringMessage), stringLookup);

    LoggerUtil.log(logger, enteringLevel, message);
  }

  private Level getEnteringLevel(final Level enteringLevel) {
    return enteringLevel == Level.DEFAULT ? aopLoggersProperties.getEnteringLevel() : enteringLevel;
  }

  private String getEnteringMesage(final String enteringMessage) {
    return enteringMessage.length() == 0
        ? aopLoggersProperties.getEnteringMessage()
        : enteringMessage;
  }
}
