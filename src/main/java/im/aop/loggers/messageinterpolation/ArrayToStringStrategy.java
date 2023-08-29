package im.aop.loggers.messageinterpolation;

import java.util.Objects;

/**
 * {@link ToStringStrategy} implementation for array, using {@link ObjectToStringStrategy} to return
 * a String representation of the element. Encloses element within square brackets "[]".
 *
 * @author Andy Lian
 */
public class ArrayToStringStrategy implements ToStringStrategy {

  private final ObjectToStringStrategy objectToStringStrategy;

  public ArrayToStringStrategy(final ObjectToStringStrategy objectToStringStrategy) {
    this.objectToStringStrategy = Objects.requireNonNull(objectToStringStrategy);
  }

  @Override
  public boolean supports(Object object) {
    return object != null && object.getClass().isArray();
  }

  @Override
  public String toString(Object object) {
    return toString((Object[]) object);
  }

  private String toString(final Object[] array) {
    final int maxIndex = array.length - 1;
    if (maxIndex == -1) {
      return "[]";
    }

    final StringBuilder builder = new StringBuilder("[");
    for (int index = 0; ; index++) {
      builder.append(toStringOrNullString(array[index]));
      if (index == maxIndex) {
        return builder.append("]").toString();
      }

      builder.append(", ");
    }
  }

  private Object toStringOrNullString(Object element) {
    return element == null ? "null" : objectToStringStrategy.toString(element);
  }
}
