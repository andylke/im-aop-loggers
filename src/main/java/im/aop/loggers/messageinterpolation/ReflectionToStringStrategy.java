package im.aop.loggers.messageinterpolation;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ReflectionToStringStrategy implements ToStringStrategy {

  private final ReflectionToStringProperties reflectionToStringProperties;

  public ReflectionToStringStrategy(
      final ReflectionToStringProperties reflectionToStringProperties) {
    this.reflectionToStringProperties = Objects.requireNonNull(reflectionToStringProperties);
  }

  @Override
  public boolean supports(Class<?> type) {
    return StringUtils.startsWithAny(
        type.getPackageName(), reflectionToStringProperties.getBasePackages());
  }

  @Override
  public String toString(Object object) {
    final ReflectionToStringBuilder builder =
        new ReflectionToStringBuilder(object, ToStringStyle.NO_CLASS_NAME_STYLE);
    builder.setExcludeNullValues(reflectionToStringProperties.isExcludeNullValues());
    builder.setExcludeFieldNames(reflectionToStringProperties.getExcludeFieldNames());

    return builder.toString();
  }
}
