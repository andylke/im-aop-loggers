package im.aop.loggers.advice.afterrollback;

import im.aop.loggers.Level;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Log after rollback the transaction due to an exception.
 *
 * @author Andy Lian
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface LogAfterRollback {

  /**
   * @return Class name used as Logger's category name
   */
  Class<?> declaringClass() default void.class;

  /**
   * @return Logging Level for rollbacked transaction message
   */
  Level loggingLevel() default Level.DEFAULT;

  /**
   * @return Rollbacked message template
   */
  String messageTemplate() default "";
}
