package im.aop.loggers.advice.afterrollback;

import im.aop.loggers.AopLoggersProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class LogAfterRollbackConfiguration {

  @Bean
  public LogAfterRollbackAdvice logAfterRollbackAdvice() {
    return new LogAfterRollbackAdvice();
  }

  @Bean
  public LogAfterRollbackService logAfterRollbackService(
      final AopLoggersProperties aopLoggersProperties) {
    return new LogAfterRollbackService(aopLoggersProperties);
  }
}
