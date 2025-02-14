package im.aop.loggers.advice.afterreturning;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.Level;
import im.aop.loggers.messageinterpolation.JoinPointStringSupplierRegistrar;
import im.aop.loggers.messageinterpolation.ReturnValueStringSupplierRegistrar;
import im.aop.loggers.messageinterpolation.StringSubstitutor;
import im.aop.loggers.messageinterpolation.StringSupplierLookup;
import im.aop.loggers.util.LoggerUtil;
import java.time.Duration;
import java.util.Objects;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class LogAfterReturningService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LogAfterReturningService.class);

  @Autowired private StringSubstitutor stringSubstitutor;

  @Autowired private JoinPointStringSupplierRegistrar joinPointStringSupplierRegistrar;

  @Autowired private ReturnValueStringSupplierRegistrar returnValueStringSupplierRegistrar;

  private final AopLoggersProperties aopLoggersProperties;

  public LogAfterReturningService(final AopLoggersProperties aopLoggersProperties) {
    this.aopLoggersProperties = Objects.requireNonNull(aopLoggersProperties);
  }

  public void logAfterReturning(
      final JoinPoint joinPoint, final LogAfterReturning annotation, final Object returnValue) {
    final long startTime = System.nanoTime();

    final Logger logger = LoggerUtil.getLogger(annotation.declaringClass(), joinPoint);
    final Level loggingLevel = getLoggingLevel(annotation.level());
    if (isLoggingLevelDisabled(logger, loggingLevel)) {
      logElapsed(startTime);
      return;
    }

    final StringSupplierLookup stringLookup = new StringSupplierLookup();

    logMessage(
        joinPoint, loggingLevel, annotation.exitedMessage(), logger, stringLookup, returnValue);
    logElapsed(startTime);
  }

  private void logElapsed(long startTime) {
    LOGGER.debug(
        "[logAfterReturning] elapsed [{}]", Duration.ofNanos(System.nanoTime() - startTime));
  }

  private boolean isLoggingLevelDisabled(final Logger logger, final Level loggingLevel) {
    return !(LoggerUtil.isEnabled(logger, loggingLevel));
  }

  private void logMessage(
      final JoinPoint joinPoint,
      final Level loggingLevel,
      final String messageTemplate,
      final Logger logger,
      final StringSupplierLookup stringLookup,
      final Object returnValue) {
    joinPointStringSupplierRegistrar.register(stringLookup, joinPoint);
    returnValueStringSupplierRegistrar.register(stringLookup, joinPoint, returnValue);

    final String message =
        stringSubstitutor.substitute(getMessageTemplate(messageTemplate), stringLookup);
    LoggerUtil.log(logger, loggingLevel, message);
  }

  private Level getLoggingLevel(final Level loggingLevel) {
    return loggingLevel == Level.DEFAULT ? aopLoggersProperties.getExitedLevel() : loggingLevel;
  }

  private String getMessageTemplate(final String messageTemplate) {
    return messageTemplate.isEmpty() ? aopLoggersProperties.getExitedMessage() : messageTemplate;
  }
}
