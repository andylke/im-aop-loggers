package im.aop.loggers.advice.afterthrowing;

import im.aop.loggers.Level;
import java.lang.annotation.*;

/**
 * Log after leaving the target method by throwing an exception.
 *
 * @author Andy Lian
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface LogAfterThrowing {

  /** @return Class name used as Logger's category name */
  Class<?> declaringClass() default void.class;

  /** @return Log Level for exited abnormally message */
  Level level() default Level.DEFAULT;

  /** @return Exited abnormally message template */
  String exitedAbnormallyMessage() default "";

  /** @return Exceptions that will be ignored by Logger */
  Class<? extends Throwable>[] ignoreExceptions() default {};

  /** @return Whether to print exception and its backtrace */
  boolean printStackTrace() default true;
}
