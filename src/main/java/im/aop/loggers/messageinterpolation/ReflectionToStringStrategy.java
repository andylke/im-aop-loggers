package im.aop.loggers.messageinterpolation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;

public class ReflectionToStringStrategy implements ToStringStrategy {

  private final ReflectionToStringProperties reflectionToStringProperties;

  private final List<Class<?>> supportedBaseClasses = new ArrayList<Class<?>>();

  public ReflectionToStringStrategy(
      final ReflectionToStringProperties reflectionToStringProperties) {
    this.reflectionToStringProperties = Objects.requireNonNull(reflectionToStringProperties);

    for (String baseClass : reflectionToStringProperties.getBaseClasses()) {
      try {
        supportedBaseClasses.add(Class.forName(baseClass));
      } catch (ClassNotFoundException e) {
        throw new InvalidConfigurationPropertyValueException(
            ReflectionToStringProperties.PREFIX + ".base-classes", baseClass, e.getMessage());
      }
    }
  }

  @Override
  public boolean supports(Class<?> type) {
    for (Class<?> supportedBaseClass : supportedBaseClasses) {
      if (supportedBaseClass.isAssignableFrom(type)) {
        return true;
      }
    }
    return false;
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
