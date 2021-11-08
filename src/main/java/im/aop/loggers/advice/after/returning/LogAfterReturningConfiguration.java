package im.aop.loggers.advice.after.returning;

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
public class LogAfterReturningConfiguration {

  @Bean
  public LogAfterReturningAdvice logAfterReturningAdvice() {
    return new LogAfterReturningAdvice();
  }

  @Bean
  public LogAfterReturningService logAfterReturningService(
      final AopLoggersProperties aopLoggersProperties) {
    return new LogAfterReturningService(aopLoggersProperties);
  }
}
