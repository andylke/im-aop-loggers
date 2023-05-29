package im.aop.loggers.messageinterpolation;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * {@link ToStringStrategy} implementation using {@link String#valueOf(boolean)}. Return "null" if a
 * is null.
 *
 * @author Andy Lian
 */
public class ObjectToStringStrategy implements ToStringStrategy {

  private ReflectionToStringStrategy reflectionToStringStrategy;

  @Override
  public boolean supports(Class<?> type) {
    return true;
  }

  @Override
  public String toString(Object object) {
    if (object == null
        || reflectionToStringStrategy == null
        || !reflectionToStringStrategy.supports(object.getClass())) {
      return String.valueOf(object);
    } else {
      return reflectionToStringStrategy.toString(object);
    }
  }

  @Autowired
  public void setReflectionToStringStrategy(ReflectionToStringStrategy reflectionToStringStrategy) {
    this.reflectionToStringStrategy = reflectionToStringStrategy;
  }
}
