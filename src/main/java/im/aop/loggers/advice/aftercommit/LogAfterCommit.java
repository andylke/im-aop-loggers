package im.aop.loggers.advice.aftercommit;

import im.aop.loggers.Level;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Log after committed the transaction.
 *
 * @author Andy Lian
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface LogAfterCommit {

  /**
   * @return Class name used as Logger's category name
   */
  Class<?> declaringClass() default void.class;

  /**
   * @return Log Level for committed transaction message
   */
  Level loggingLevel() default Level.DEFAULT;

  /**
   * @return Committed transaction message template
   */
  String messageTemplate() default "";
}
