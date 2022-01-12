package im.aop.loggers.messageinterpolation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link Configuration} for {@link ToStringStrategy}.
 *
 * @author Andy Lian
 */
@Configuration(proxyBeanMethods = false)
class ToStringStrategyConfiguration {

  @Bean
  @ConditionalOnMissingBean({ToStringStrategy.class})
  public ObjectToStringStrategy toStringStrategy() {
    return new ObjectToStringStrategy();
  }
}
