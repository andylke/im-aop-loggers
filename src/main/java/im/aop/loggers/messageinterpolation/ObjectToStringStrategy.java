package im.aop.loggers.messageinterpolation;

/**
 * {@link ToStringStrategy} implementation using {@link String#valueOf(boolean)}. Return "null" if a
 * is null.
 *
 * @author Andy Lian
 */
public class ObjectToStringStrategy implements ToStringStrategy {

  @Override
  public boolean canSupport(Class<?> type) {
    return true;
  }

  @Override
  public String toString(Object object) {
    return String.valueOf(object);
  }
}
