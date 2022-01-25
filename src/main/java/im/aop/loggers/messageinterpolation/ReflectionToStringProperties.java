package im.aop.loggers.messageinterpolation;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import im.aop.loggers.AopLoggersProperties;

@ConfigurationProperties(prefix = ReflectionToStringProperties.PREFIX)
public class ReflectionToStringProperties {

  public static final String PREFIX = AopLoggersProperties.PREFIX + ".reflection-to-string";

  private String[] basePackages = new String[] {};

  private boolean excludeNullValues = true;

  private String[] excludeFieldNames = new String[] {};

  @PostConstruct
  void postConstruct() {
    excludeFieldNames =
        ArrayUtils.insert(
            0, excludeFieldNames, new String[] {"username", "password", "passphrase", "secret"});
  }

  public String[] getBasePackages() {
    return basePackages;
  }

  public void setBasePackages(String[] basePackages) {
    this.basePackages = basePackages;
  }

  public boolean isExcludeNullValues() {
    return excludeNullValues;
  }

  public void setExcludeNullValues(boolean excludeNullValues) {
    this.excludeNullValues = excludeNullValues;
  }

  public String[] getExcludeFieldNames() {
    return excludeFieldNames;
  }

  public void setExcludeFieldNames(String[] excludeFieldNames) {
    this.excludeFieldNames = excludeFieldNames;
  }
}
