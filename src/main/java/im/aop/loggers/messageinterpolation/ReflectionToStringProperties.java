package im.aop.loggers.messageinterpolation;

import im.aop.loggers.AopLoggersProperties;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = ReflectionToStringProperties.PREFIX)
public class ReflectionToStringProperties {

  public static final String PREFIX = AopLoggersProperties.PREFIX + ".reflection-to-string";

  private String[] baseClasses = new String[]{};

  private boolean excludeNullValues = true;

  private boolean excludeEmptyValues = true;

  private boolean excludeZeroValues = true;

  private String[] excludeFieldNames = new String[]{};

  @PostConstruct
  void postConstruct() {
    excludeFieldNames =
        ArrayUtils.insert(0, excludeFieldNames, "username", "password", "passphrase", "secret");
  }
}
