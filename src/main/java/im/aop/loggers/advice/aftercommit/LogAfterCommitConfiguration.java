package im.aop.loggers.advice.aftercommit;

import im.aop.loggers.AopLoggersProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class LogAfterCommitConfiguration {

  @Bean
  public LogAfterCommitAdvice logAfterCommitAdvice() {
    return new LogAfterCommitAdvice();
  }

  @Bean
  public LogAfterCommitService logAfterCommitService(
      final AopLoggersProperties aopLoggersProperties) {
    return new LogAfterCommitService(aopLoggersProperties);
  }
}
