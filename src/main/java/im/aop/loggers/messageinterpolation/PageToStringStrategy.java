package im.aop.loggers.messageinterpolation;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * {@link ToStringStrategy} implementation for {@link Page}, using {@link IterableToStringStrategy}
 * to return a String representation of the list of element.
 *
 * @author Andy Lian
 */
public class PageToStringStrategy implements ToStringStrategy {

  @Autowired private IterableToStringStrategy iterableToStringStrategy;

  @Override
  public boolean supports(Object object) {
    return object instanceof Page<?>;
  }

  @Override
  public String toString(Object object) {
    final Page<?> page = (Page<?>) object;
    final ToStringBuilder toStringBuilder =
        new ToStringBuilder(page, ToStringStyle.NO_CLASS_NAME_STYLE);

    if (page.getPageable().isPaged()) {
      toStringBuilder
          .append("page", page.getNumber())
          .append("size", page.getSize())
          .append("sort", page.getSort())
          .append("totalElements", page.getTotalElements())
          .append("totalPages", page.getTotalPages());
    }

    toStringBuilder
        .append("numberOfElements", page.getNumberOfElements())
        .append("content", iterableToStringStrategy.toString(page.getContent()));

    return toStringBuilder.toString();
  }
}
