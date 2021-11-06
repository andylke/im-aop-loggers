package im.aop.loggers;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import im.aop.loggers.advice.after.returning.LogAfterReturningConfiguration;
import im.aop.loggers.advice.after.throwing.LogAfterThrowingConfiguration;
import im.aop.loggers.advice.around.LogAroundConfiguration;
import im.aop.loggers.advice.before.LogBeforeConfiguration;

@Configuration(proxyBeanMethods = false)
public class AopLoggersAutoConfiguration {

  @EnableConfigurationProperties({AopLoggersProperties.class})
  @Import({
    LogAfterReturningConfiguration.class,
    LogAfterThrowingConfiguration.class,
    LogAroundConfiguration.class,
    LogBeforeConfiguration.class
  })
  static class AopLoggersConfiguration {}
}
