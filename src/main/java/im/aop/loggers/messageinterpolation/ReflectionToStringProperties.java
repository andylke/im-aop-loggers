package im.aop.loggers.messageinterpolation;

import jakarta.annotation.PostConstruct;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import im.aop.loggers.AopLoggersProperties;

@ConfigurationProperties(prefix = ReflectionToStringProperties.PREFIX)
public class ReflectionToStringProperties {

  public static final String PREFIX = AopLoggersProperties.PREFIX + ".reflection-to-string";

  private String[] baseClasses = new String[] {};

  private boolean excludeNullValues = true;

  private boolean excludeEmptyValues = true;

  private boolean excludeZeroValues = true;

  private String[] excludeFieldNames = new String[] {};

  @PostConstruct
  void postConstruct() {
    excludeFieldNames =
        ArrayUtils.insert(
            0, excludeFieldNames, new String[] {"username", "password", "passphrase", "secret"});
  }

  public String[] getBaseClasses() {
    return baseClasses;
  }

  public void setBaseClasses(String[] baseClasses) {
    this.baseClasses = baseClasses;
  }

  public boolean isExcludeNullValues() {
    return excludeNullValues;
  }

  public void setExcludeNullValues(boolean excludeNullValues) {
    this.excludeNullValues = excludeNullValues;
  }

  public boolean isExcludeEmptyValues() {
    return excludeEmptyValues;
  }

  public void setExcludeEmptyValues(boolean excludeEmptyValues) {
    this.excludeEmptyValues = excludeEmptyValues;
  }

  public boolean isExcludeZeroValues() {
    return excludeZeroValues;
  }

  public void setExcludeZeroValues(boolean excludeZeroValues) {
    this.excludeZeroValues = excludeZeroValues;
  }

  public String[] getExcludeFieldNames() {
    return excludeFieldNames;
  }

  public void setExcludeFieldNames(String[] excludeFieldNames) {
    this.excludeFieldNames = excludeFieldNames;
  }
}
