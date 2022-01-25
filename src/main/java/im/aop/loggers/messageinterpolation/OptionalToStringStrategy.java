package im.aop.loggers.messageinterpolation;

import java.util.Objects;
import java.util.Optional;

/**
 * {@link ToStringStrategy} implementation for {@link Optional}, using {@link
 * ObjectToStringStrategy} to return a String representation of the element.
 *
 * @author Andy Lian
 */
public class OptionalToStringStrategy implements ToStringStrategy {

  private final ObjectToStringStrategy objectToStringStrategy;

  public OptionalToStringStrategy(final ObjectToStringStrategy objectToStringStrategy) {
    this.objectToStringStrategy = Objects.requireNonNull(objectToStringStrategy);
  }

  @Override
  public boolean supports(Class<?> type) {
    return Optional.class.isAssignableFrom(type);
  }

  @Override
  public String toString(Object object) {
    return toString((Optional<?>) object);
  }

  private String toString(final Optional<?> optional) {
    return optional.isEmpty() ? "null" : objectToStringStrategy.toString(optional.get());
  }
}
