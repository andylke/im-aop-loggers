package im.aop.loggers.advice.after.throwing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import im.aop.loggers.AopLoggersProperties;

@Configuration(proxyBeanMethods = false)
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
