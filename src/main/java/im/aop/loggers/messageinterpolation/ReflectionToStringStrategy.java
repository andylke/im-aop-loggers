package im.aop.loggers.messageinterpolation;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;

import jakarta.annotation.PostConstruct;

public class ReflectionToStringStrategy implements ToStringStrategy {

  @Autowired private ReflectionToStringProperties reflectionToStringProperties;

  private final List<Class<?>> supportedBaseClasses = new ArrayList<>();

  @PostConstruct
  void postConstruct() {
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
  public boolean supports(Object object) {
    if (Proxy.isProxyClass(object.getClass())) {
      return false;
    }

    for (Class<?> supportedBaseClass : supportedBaseClasses) {
      if (supportedBaseClass.isAssignableFrom(object.getClass())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString(Object object) {
    final ReflectionToStringBuilder builder =
        new ReflectionToStringBuilder(object, ToStringStyle.NO_CLASS_NAME_STYLE) {

          @Override
          public ToStringBuilder append(
              final String fieldName, final Object obj, final boolean fullDetail) {
            if (!isExcludedValue(obj)) {
              super.append(fieldName, obj, fullDetail);
            }
            return this;
          }
        };

    builder.setExcludeNullValues(reflectionToStringProperties.isExcludeNullValues());
    builder.setExcludeFieldNames(reflectionToStringProperties.getExcludeFieldNames());

    return builder.toString();
  }

  private boolean isExcludedValue(Object value) {
    return isExcludedEmptyValue(value) || isExcludedZeroValue(value);
  }

  private boolean isExcludedEmptyValue(Object value) {
    return value instanceof CharSequence charSequence
        && reflectionToStringProperties.isExcludeEmptyValues()
        && charSequence.length() == 0;
  }

  private boolean isExcludedZeroValue(Object value) {
    return value instanceof Number number
        && reflectionToStringProperties.isExcludeZeroValues()
        && Double.compare(number.doubleValue(), 0.0) == 0;
  }
}
