package im.aop.loggers.advice.beforecommit;

import im.aop.loggers.AopLoggersProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class LogBeforeCommitConfiguration {

  @Bean
  public LogBeforeCommitAdvice logBeforeCommitAdvice() {
    return new LogBeforeCommitAdvice();
  }

  @Bean
  public LogBeforeCommitService logBeforeCommitService(
      final AopLoggersProperties aopLoggersProperties) {
    return new LogBeforeCommitService(aopLoggersProperties);
  }
}
