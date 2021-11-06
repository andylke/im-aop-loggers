package im.aop.loggers.advice.around;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

import im.aop.loggers.logging.Level;

/**
 * Log before entering and after leaving the target method, regardless of leaving normally or
 * throwing an exception.
 *
 * @author Andy Lian
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface LogAround {

  /** @return Class name used as Logger's category name */
  Class<?> declaringClass() default void.class;

  /** @return Log Level for entering, exited normally and elapsed message */
  Level level() default Level.DEFAULT;

  /** @return Entering message template */
  String enteringMessage() default "";

  /** @return Exited message template */
  String exitedMessage() default "";

  /** @return Log level for exited abnormally message */
  Level exitedAbnormallyLevel() default Level.DEFAULT;

  /** @return Exited abnormally message template */
  String exitedAbnormallyMessage() default "";

  /** @return Exceptions that will be ignored by Logger */
  Class<? extends Throwable>[] ignoreExceptions() default {};

  /** @return Whether to print exception and its backtrace */
  boolean printStackTrace() default true;

  /** @return Elapsed message template */
  String elapsedMessage() default "";

  /** @return Log level for elapsed warning message */
  Level elapsedWarningLevel() default Level.DEFAULT;

  /** @return Elapsed warning message template */
  String elapsedWarningMessage() default "";

  /** @return Elapsed time limit to log elapsed warning message */
  long elapsedTimeLimit() default 0;

  /** @return Elapsed time unit */
  ChronoUnit elapsedTimeUnit() default ChronoUnit.MILLIS;
}
