package im.aop.loggers.advice.around;

import im.aop.loggers.AopLoggersProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
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
