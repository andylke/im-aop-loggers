package im.aop.loggers.logging;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerService {

  public Logger getLogger(final Class<?> declaringClass, final JoinPoint joinPoint) {
    return LoggerFactory.getLogger(
        declaringClass == null || declaringClass == void.class
            ? joinPoint.getSignature().getDeclaringType()
            : declaringClass);
  }

  public boolean isEnabled(final Logger logger, final Level level) {
    switch (level) {
      case TRACE:
        return logger.isTraceEnabled();
      case DEBUG:
        return logger.isDebugEnabled();
      case WARN:
        return logger.isWarnEnabled();
      case INFO:
        return logger.isInfoEnabled();
      case ERROR:
        return logger.isErrorEnabled();
      default:
        return false;
    }
  }

  public void log(final Logger logger, final Level level, final String message) {
    switch (level) {
      case TRACE:
        logger.trace(message);
        break;
      case DEBUG:
        logger.debug(message);
        break;
      case WARN:
        logger.warn(message);
        break;
      case INFO:
        logger.info(message);
        break;
      case ERROR:
        logger.error(message);
        break;
      default:
        break;
    }
  }

  public void logException(
      final Logger logger, final Level level, final String message, final Throwable exception) {
    switch (level) {
      case TRACE:
        logger.trace(message, exception);
        break;
      case DEBUG:
        logger.debug(message, exception);
        break;
      case WARN:
        logger.warn(message, exception);
        break;
      case INFO:
        logger.info(message, exception);
        break;
      case ERROR:
        logger.error(message, exception);
        break;
      default:
        break;
    }
  }
}
