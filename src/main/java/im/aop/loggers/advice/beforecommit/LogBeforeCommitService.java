package im.aop.loggers.advice.beforecommit;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.Level;
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

public class LogBeforeCommitService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LogBeforeCommitService.class);

  @Autowired private StringSubstitutor stringSubstitutor;

  @Autowired private JoinPointStringSupplierRegistrar joinPointStringSupplierRegistrar;

  private final AopLoggersProperties aopLoggersProperties;

  public LogBeforeCommitService(final AopLoggersProperties aopLoggersProperties) {
    this.aopLoggersProperties = Objects.requireNonNull(aopLoggersProperties);
  }

  public void logBeforeCommit(final JoinPoint joinPoint, final LogBeforeCommit annotation) {
    final long startTime = System.nanoTime();

    final Logger logger = LoggerUtil.getLogger(annotation.declaringClass(), joinPoint);
    final Level loggingLevel = getLoggingLevel(annotation.loggingLevel());
    if (isLoggingLevelDisabled(logger, loggingLevel)) {
      logElapsed(startTime);
      return;
    }

    final StringSupplierLookup stringLookup = new StringSupplierLookup();

    logMessage(joinPoint, loggingLevel, annotation.messageTemplate(), logger, stringLookup);
    logElapsed(startTime);
  }

  private void logElapsed(long startTime) {
    LOGGER.debug("[logBeforeCommit] elapsed [{}]", Duration.ofNanos(System.nanoTime() - startTime));
  }

  private boolean isLoggingLevelDisabled(final Logger logger, final Level loggingLevel) {
    return !LoggerUtil.isEnabled(logger, loggingLevel);
  }

  private void logMessage(
      final JoinPoint joinPoint,
      final Level loggingLevel,
      final String messageTemplate,
      final Logger logger,
      final StringSupplierLookup stringLookup) {
    joinPointStringSupplierRegistrar.register(stringLookup, joinPoint);

    final String message =
        stringSubstitutor.substitute(getMessageTemplate(messageTemplate), stringLookup);

    LoggerUtil.log(logger, loggingLevel, message);
  }

  private Level getLoggingLevel(final Level loggingLevel) {
    return loggingLevel == Level.DEFAULT
        ? aopLoggersProperties.getCommittingTransactionLevel()
        : loggingLevel;
  }

  private String getMessageTemplate(final String messageTemplate) {
    return messageTemplate.isEmpty()
        ? aopLoggersProperties.getCommittingTransactionMessage()
        : messageTemplate;
  }
}
