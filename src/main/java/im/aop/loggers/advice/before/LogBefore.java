package im.aop.loggers.advice.before;

import im.aop.loggers.Level;
import java.lang.annotation.*;

/**
 * Log before entering the target method.
 *
 * @author Andy Lian
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface LogBefore {

  /** @return Class name used as Logger's category name */
  Class<?> declaringClass() default void.class;

  /** @return Log Level for entering message */
  Level level() default Level.DEFAULT;

  /** @return Entering message template */
  String enteringMessage() default "";
}
