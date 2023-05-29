package im.aop.loggers.advice.afterreturning;

import im.aop.loggers.AopLoggersProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
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
