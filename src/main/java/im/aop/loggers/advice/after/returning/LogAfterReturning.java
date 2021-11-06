package im.aop.loggers.advice.after.returning;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import im.aop.loggers.logging.Level;

/**
 * Log after leaving the target method normally.
 *
 * @author Andy Lian
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface LogAfterReturning {

  /** @return Class name used as Logger's category name */
  Class<?> declaringClass() default void.class;

  /** @return Log Level for exited message */
  Level level() default Level.DEFAULT;

  /** @return Exited message template */
  String exitedMessage() default "";
}
