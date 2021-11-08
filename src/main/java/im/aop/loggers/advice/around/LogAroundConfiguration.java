package im.aop.loggers.advice.around;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import im.aop.loggers.AopLoggersProperties;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(
    prefix = "im.aop.loggers",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
public class LogAroundConfiguration {

  @Bean
  public LogAroundAdvice logAroundAdvice() {
    return new LogAroundAdvice();
  }

  @Bean
  public LogAroundService logAroundService(final AopLoggersProperties aopLoggersProperties) {
    return new LogAroundService(aopLoggersProperties);
  }
}
