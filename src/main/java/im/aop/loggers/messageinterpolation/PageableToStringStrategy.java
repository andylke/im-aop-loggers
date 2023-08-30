package im.aop.loggers.messageinterpolation;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.domain.Pageable;

/**
 * {@link ToStringStrategy} implementation for {@link Pageable}, using {@link ToStringBuilder} to
 * return a String representation of the element.
 *
 * @author Andy Lian
 */
public class PageableToStringStrategy implements ToStringStrategy {

  @Override
  public boolean supports(Object object) {
    return object instanceof Pageable;
  }

  @Override
  public String toString(Object object) {
    final Pageable pageable = (Pageable) object;

    final ToStringBuilder toStringBuilder =
        new ToStringBuilder(pageable, ToStringStyle.NO_CLASS_NAME_STYLE);
    if (pageable.isPaged()) {
      toStringBuilder
          .append("page", pageable.getPageNumber())
          .append("size", pageable.getPageSize())
          .append("sort", pageable.getSort());
    } else {
      toStringBuilder.append("UNPAGED");
    }
    return toStringBuilder.toString();
  }
}
