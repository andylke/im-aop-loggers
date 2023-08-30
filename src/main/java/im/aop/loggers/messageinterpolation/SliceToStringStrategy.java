package im.aop.loggers.messageinterpolation;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;

/**
 * {@link ToStringStrategy} implementation for {@link Slice}, using {@link IterableToStringStrategy}
 * to return a String representation of the list of element.
 *
 * @author Andy Lian
 */
public class SliceToStringStrategy implements ToStringStrategy {

  @Autowired private IterableToStringStrategy iterableToStringStrategy;

  @Override
  public boolean supports(Object object) {
    return object instanceof Slice<?>;
  }

  @Override
  public String toString(Object object) {
    final Slice<?> slice = (Slice<?>) object;
    final ToStringBuilder toStringBuilder =
        new ToStringBuilder(slice, ToStringStyle.NO_CLASS_NAME_STYLE);

    if (slice.getPageable().isPaged()) {
      toStringBuilder
          .append("page", slice.getNumber())
          .append("size", slice.getSize())
          .append("sort", slice.getSort());
    }

    toStringBuilder
        .append("numberOfElements", slice.getNumberOfElements())
        .append("content", iterableToStringStrategy.toString(slice.getContent()));

    return toStringBuilder.toString();
  }
}
