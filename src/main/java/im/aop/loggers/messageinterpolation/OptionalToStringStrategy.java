package im.aop.loggers.messageinterpolation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * {@link ToStringStrategy} implementation for {@link Optional}, using
 * {@link ObjectToStringStrategy} to return a String representation of the element.
 *
 * @author Andy Lian
 */
public class OptionalToStringStrategy implements ToStringStrategy {

  @Autowired private ObjectToStringStrategy objectToStringStrategy;

  @Override
  public boolean supports(Object object) {
    return object instanceof Optional<?>;
  }

  @Override
  public String toString(Object object) {
    return toString((Optional<?>) object);
  }

  private String toString(final Optional<?> optional) {
    return optional.isEmpty() ? "null" : objectToStringStrategy.toString(optional.get());
  }
}
