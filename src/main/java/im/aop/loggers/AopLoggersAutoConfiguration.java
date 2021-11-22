package im.aop.loggers;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import im.aop.loggers.advice.afterreturning.LogAfterReturningConfiguration;
import im.aop.loggers.advice.afterthrowing.LogAfterThrowingConfiguration;
import im.aop.loggers.advice.around.LogAroundConfiguration;
import im.aop.loggers.advice.before.LogBeforeConfiguration;
import im.aop.loggers.messageinterpolation.StringSubstitutorConfiguration;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration Auto-configuration} for AOP
 * Loggers.
 *
 * @author Andy Lian
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(
    prefix = AopLoggersProperties.PREFIX,
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
public class AopLoggersAutoConfiguration {

  @EnableConfigurationProperties({AopLoggersProperties.class})
  @Import({
    StringSubstitutorConfiguration.class,
    LogAfterReturningConfiguration.class,
    LogAfterThrowingConfiguration.class,
    LogAroundConfiguration.class,
    LogBeforeConfiguration.class
  })
  static class AopLoggersConfiguration {}
}
