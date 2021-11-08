package im.aop.loggers.advice.after.throwing;

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
public class LogAfterThrowingConfiguration {

  @Bean
  public LogAfterThrowingAdvice logAfterThrowingAdvice() {
    return new LogAfterThrowingAdvice();
  }

  @Bean
  public LogAfterThrowingService logAfterThrowingService(
      final AopLoggersProperties aopLoggersProperties) {
    return new LogAfterThrowingService(aopLoggersProperties);
  }
}
