package im.aop.loggers.messageinterpolation;

import java.util.Arrays;
import java.util.Objects;

/**
 * {@link Objects#toString(Object)} implementation for {@link ToStringStrategy}.
 *
 * @author Andy Lian
 */
public class ObjectToStringStrategy implements ToStringStrategy {

  @Override
  public String toString(Object object) {
    if (object == null) {
      return "null";
    }

    if (object.getClass().isArray()) {
      return Arrays.toString((Object[]) object);
    } else {
      return Objects.toString(object);
    }
  }
}
