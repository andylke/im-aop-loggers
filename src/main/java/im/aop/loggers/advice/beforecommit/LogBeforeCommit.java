package im.aop.loggers.advice.beforecommit;

import im.aop.loggers.Level;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Log before committing the transaction.
 *
 * @author Andy Lian
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface LogBeforeCommit {

  /**
   * @return Class name used as Logger's category name
   */
  Class<?> declaringClass() default void.class;

  /**
   * @return Logging Level for committing transaction message
   */
  Level loggingLevel() default Level.DEFAULT;

  /**
   * @return Committing transaction message template
   */
  String messageTemplate() default "";
}
