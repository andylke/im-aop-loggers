package im.aop.loggers.advice.before;

import im.aop.loggers.AopLoggersProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class LogBeforeConfiguration {

  @Bean
  public LogBeforeAdvice logBeforeAdvice() {
    return new LogBeforeAdvice();
  }

  @Bean
  public LogBeforeService logBeforeService(final AopLoggersProperties aopLoggersProperties) {
    return new LogBeforeService(aopLoggersProperties);
  }
}
